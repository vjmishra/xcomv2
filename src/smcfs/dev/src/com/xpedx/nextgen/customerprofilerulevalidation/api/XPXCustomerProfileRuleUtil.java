package com.xpedx.nextgen.customerprofilerulevalidation.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.Vector;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCDateTimeUtil;
import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.xpedx.nextgen.customerprofilerulevalidation.api.XPXCustomerProfileRuleConstant.CustomerProfileRuleID;
import com.xpedx.nextgen.priceAndAvailabilityService.XPXItem;
import com.xpedx.nextgen.priceAndAvailabilityService.XPXPriceAndAvailability;
import com.xpedx.nextgen.priceAndAvailabilityService.XPXPriceandAvailabilityUtil;
import com.xpedx.nextgen.priceAndAvailabilityService.XPXWarehouseLocation;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXCustomerProfileRuleUtil {
	private static YIFApi api = null;
	private String lastReqDeliveryDate = null;
	private boolean firstOrderlineCheck = true;
	private Document orderXML = null;
	private HashMap<String, HashMap<String, String>> ruleMap = null;
	private Element orderLevelErrorElement = null;
	private Element orderLineLevelErrorElement = null;
	private Document custOutPutDoc = null;
	private Document outputCommonErrorListDoc=null;
	XPXPriceAndAvailability outputPnAREsponse=null;
	private HashMap<String, String> errorDescMap=new HashMap<String, String>();
	public Document getOrderXML() {
		return orderXML;
	}

	public void setOrderXML(Document orderXML) {
		this.orderXML = orderXML;
	}
	
	public XPXCustomerProfileRuleUtil(Document orderXML,HashMap<String, HashMap<String, String>> ruleMap) throws Exception {
		api = YIFClientFactory.getInstance().getApi();
		setOrderXML(orderXML);
		this.ruleMap = ruleMap;
	}

	private void setLastReqDeliveryDate(String lastReqDeliveryDate) {
		this.lastReqDeliveryDate = lastReqDeliveryDate;
	}

	private String getLastReqDeliveryDate() {
		return lastReqDeliveryDate;
	}

	private boolean getFirstOrderlineCheck() {
		return firstOrderlineCheck;
	}

	private void setFirstOrderlineCheck(boolean firstOrderlineCheck) {
		this.firstOrderlineCheck = firstOrderlineCheck;
	}
	
	/* Order level P &A call */
	public void validateAcceptPriceOverRide(YFSEnvironment env, HashMap<String, String> paramMap)
			throws Exception {
		//Override not required
//		if (isRuleOverriddenAtOrderLevel(XPXCustomerProfileRuleConstant.ACCEPT_PRICE_OVERRIDE)) {
//			return;
//		}
		Document inXML = getOrderXML();		
		//get PnA for all the lines
		XPXPriceAndAvailability output = outputPnAREsponse;
		//Check price variance comapring with pna output for each line
		NodeList nodList = inXML.getElementsByTagName("OrderLine");
		int length = nodList.getLength();
		if (length > 0) {
			for (int counter = 0; counter < length; counter++) {
				orderLineLevelErrorElement=null;
				Element orderLineEle = (Element) nodList.item(counter);
				Vector items = output.getItems();
				Iterator it = items.iterator();
				while(it.hasNext())
				{
					XPXItem item = (XPXItem) it.next();
					//check for condition where pna corresponds to this line
					if(findMatchingPnaLineResponse(orderLineEle.getAttribute("PrimeLineNo"),item.getLineNumber()))
					{
						String orderLineUnitPrice = SCXmlUtil
								.getXpathAttribute(orderLineEle,
										"LinePriceInfo/@UnitPrice");
						Float lineUnitPrice = Float
								.parseFloat(orderLineUnitPrice);
						Float pandAUnitPrice = Float.parseFloat(item
								.getUnitPricePerRequestedUOM());
						Float tempDouble = Float.parseFloat("0.00");
						if (pandAUnitPrice.compareTo(tempDouble) != 0) {
							tempDouble = ((lineUnitPrice - pandAUnitPrice) / pandAUnitPrice) * 100;
						}
						tempDouble=Math.abs(tempDouble);
						String param1 = paramMap.get("Param1");
						Float param1Float = null;
						if (!YFCCommon.isVoid(param1)) {
							param1Float = Float.parseFloat(param1);
						}
						if (!YFCCommon.isVoid(param1Float)
								&& tempDouble.compareTo(param1Float) > 0) {

							addErrorMsgToOrderLine(
									env,
									orderLineEle,
									XPXCustomerProfileRuleConstant.ACCEPT_PRICE_OVERRIDE_ERROR_TEXT,
									XPXCustomerProfileRuleConstant.ACCEPT_PRICE_OVERRIDE);
						}

					}

				}
			}
		}

	}		

	private boolean findMatchingPnaLineResponse(String orderLineNumber, String pNaLineNumber) {
		// TODO Auto-generated method stub
		int i = Integer.valueOf(orderLineNumber).intValue(); 
		int j = Integer.valueOf(pNaLineNumber).intValue(); 
		if(i==j)
			return true;
		else
			return false;	
	}

	/* Order level */
	public void validatePeventAutoPlace(YFSEnvironment env) throws Exception {
		if (isRuleOverriddenAtOrderLevel(XPXCustomerProfileRuleConstant.PREVENT_AUTO_PLACE)) {
			return;
		}
		addErrorMsgToOrder(env, XPXCustomerProfileRuleConstant.PREVENT_AUTO_ORDERPLACE_ERROR_TEXT,
					XPXCustomerProfileRuleConstant.PREVENT_AUTO_PLACE);		
	}

	/* Order level */
	public void validateHeaderCommentByCustomer(YFSEnvironment env)
			throws Exception {
		//Override removed
		if (isRuleOverriddenAtOrderLevel(XPXCustomerProfileRuleConstant.HEADER_COMMENT_BY_CUSTOMER)) {
			return;
		}
		Document inXML = getOrderXML();
		String headerComment = "HEADER";
		String instructionText = SCXmlUtil.getXpathAttribute(inXML
				.getDocumentElement(), "/Order/Instructions/Instruction[@InstructionType ='" + headerComment + "']/@InstructionText");
		Element extnElement = SCXmlUtil.getChildElement(inXML
				.getDocumentElement(), "Extn");
		if (!YFCCommon.isVoid(instructionText)) {
			extnElement.setAttribute("ExtnWebHoldFlag", "Y");
			addErrorMsgToOrder(env, XPXCustomerProfileRuleConstant.CUSTOMER_COMMENTS_ERROR_TEXT,
					XPXCustomerProfileRuleConstant.HEADER_COMMENT_BY_CUSTOMER);
		}else if("Y".equals(extnElement.getAttribute("ExtnWebHoldFlag"))){
			extnElement.setAttribute("ExtnWebHoldFlag", "N");
		}
		
	}
	//Added By Manas
	/* Order level */
	public void validateWebHoldCustomer(YFSEnvironment env)
			throws Exception {
		//Override removed
		Document inXML = getOrderXML();
		if(null!= inXML){
		Element extnElement = SCXmlUtil.getChildElement(inXML.getDocumentElement(), "Extn");
		if(null!=extnElement){
		extnElement.setAttribute("ExtnWebHoldFlag", "Y");
		}
		}
		return;
		
		
	}

	/* Order level */
	public void validateDuplicatePO(YFSEnvironment env) throws Exception {
		if (isRuleOverriddenAtOrderLevel(XPXCustomerProfileRuleConstant.DUPLICATE_PO)) {
			return;
		}
		Document inXML = getOrderXML();
		String customerPONumber = SCXmlUtil.getXpathAttribute(inXML
				.getDocumentElement(), "/Order/@CustomerPONo");
		YFCDocument inputDoc = YFCDocument.createDocument("Order");
		YFCElement inputElement = inputDoc.getDocumentElement();
		if (!YFCCommon.isVoid(customerPONumber)) {
			inputElement.setAttribute("CustomerPONo", customerPONumber);

			// inputElement.setAttribute("OrganizationCode", "xpedx");
			Document outputListDoc = api.invoke(env, "getOrderList", inputDoc
					.getDocument());
			NodeList orderList = outputListDoc.getElementsByTagName("Order");
			int orderListLength = orderList.getLength();
			if (orderListLength > 1) {
				addErrorMsgToOrder(
						env,
						XPXCustomerProfileRuleConstant.CUSTOMER_PONO_ERROR_TEXT,
						XPXCustomerProfileRuleConstant.DUPLICATE_PO);
			}
		}
	}

	/* TBD Order Level */
	public void validateNonStatndardShipMethod(YFSEnvironment env)
			throws Exception {
		if (isRuleOverriddenAtOrderLevel(XPXCustomerProfileRuleConstant.NON_STANDARD_SHIP_METHOD)) {
			return;
		}
		
		Document inXML = getOrderXML();
		String shippingMethod = SCXmlUtil.getXpathAttribute(inXML
				.getDocumentElement(), "/Order/@CarrierServiceCode");
		if (!XPXCustomerProfileRuleConstant.STANDARD.equals(shippingMethod)) {
			addErrorMsgToOrder(env,
					XPXCustomerProfileRuleConstant.SHIP_METHOD_ERROR_TEXT,
					XPXCustomerProfileRuleConstant.NON_STANDARD_SHIP_METHOD);
		}
	}

	/* Order Level */
	public void validateCustomerSelecteShipComplete(YFSEnvironment env)
			throws Exception {
		if (isRuleOverriddenAtOrderLevel(XPXCustomerProfileRuleConstant.CUSTOMER_SELECTED_SHIP_COMPLETE)) {
			return;
		}
		Document inXML = getOrderXML();
		String extnShipComplete = SCXmlUtil.getXpathAttribute(inXML
				.getDocumentElement(), "/Order/Extn/@ExtnShipComplete");
		
		if ("Y".equalsIgnoreCase(extnShipComplete)) {
			addErrorMsgToOrder(
					env,
					XPXCustomerProfileRuleConstant.SHIP_COMPLETE_ERROR_TEXT,
					XPXCustomerProfileRuleConstant.CUSTOMER_SELECTED_SHIP_COMPLETE);
		}		
	}

	/* Order Level */
	public void validateShipToZipCode(YFSEnvironment env) throws Exception {
		if (isRuleOverriddenAtOrderLevel(XPXCustomerProfileRuleConstant.VALIDATE_SHIPTO_ZIP_CODE)) {
			return;
		}
		Document inXML = getOrderXML();
		String tagName = "ZipCode";
		String orderZipCode = SCXmlUtil.getXpathAttribute(inXML
				.getDocumentElement(), "/Order/PersonInfoShipTo/@ZipCode");

		Element orderElement = inXML.getDocumentElement();
		if (YFCCommon.isVoid(custOutPutDoc)) 
			callCustomerList(env);
		
		String customerZipCode = SCXmlUtil.getXpathAttribute(
				custOutPutDoc.getDocumentElement(),
				"/CustomerList/Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@ZipCode");
		if (orderZipCode != null && !orderZipCode.equals(customerZipCode)) {
			addErrorMsgToOrder(env, XPXCustomerProfileRuleConstant.VALIDATE_SHIPTO_ZIP_ERROR_TEXT,
					XPXCustomerProfileRuleConstant.VALIDATE_SHIPTO_ZIP_CODE);
		}
	}

	public void validateShipDateNotNextBusinessDay(YFSEnvironment env)
			throws Exception {
		if (isRuleOverriddenAtOrderLevel(XPXCustomerProfileRuleConstant.SHIPDATE_NOT_NEXTBUSINESS_DAY)) {
			return;
		}
		Document inXML = getOrderXML();
		String reqShipDateStr = SCXmlUtil.getXpathAttribute(inXML
				.getDocumentElement(), "/Order/@ReqDeliveryDate");
		// String reqShipDate = "2010-05-07T15:09:37-04:00";
		if (!YFCCommon.isVoid(reqShipDateStr)) {
			Date reqShipDate = SCDateTimeUtil.getDate(reqShipDateStr, SCDateTimeUtil.ISO_DATE_FORMAT, TimeZone
					.getDefault());		
			reqShipDateStr = formatDate(reqShipDate);
			String nextBusinessdayStr = getNextBusinessDay();
			if (!(reqShipDateStr.equals(nextBusinessdayStr))) {
				addErrorMsgToOrder(
						env,
						XPXCustomerProfileRuleConstant.ACCEPT_NO_NEXTBUSINESSDAY_ERROR_TEXT,
						XPXCustomerProfileRuleConstant.SHIPDATE_NOT_NEXTBUSINESS_DAY);
			}
		}
	}

	private String getNextBusinessDay() {
        Date currentDate = new Date();   
        Calendar calendar = Calendar.getInstance();   
        calendar.setTime(currentDate);   
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);   
        if (dayOfWeek == Calendar.FRIDAY) {   
            calendar.add(Calendar.DATE, 3);   
        } else if (dayOfWeek == Calendar.SATURDAY) {   
            calendar.add(Calendar.DATE, 2);   
        } else {   
            calendar.add(Calendar.DATE, 1);   
        }   
        Date nextBusinessDay = calendar.getTime();
	    String nextBusinessdayStr = formatDate(nextBusinessDay);
	    
        return nextBusinessdayStr;
	}

	private String formatDate(Date nextBusinessDay) {
		String DATE_FORMAT = "yyyy-MM-dd";
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	    String nextBusinessdayStr= sdf.format(nextBusinessDay);
		return nextBusinessdayStr;
	}

	/* Removed */
	/*public void validateIncorrectBuyerId(YFSEnvironment env) throws Exception {
		if (isRuleOverriddenAtOrderLevel(XPXCustomerProfileRuleConstant.INCORRECT_BUYER_ID)) {
			return;
		}
		Document inXML = getOrderXML();
		String tagName = null;
		addErrorMsgToOrder(env, tagName,
				XPXCustomerProfileRuleConstant.INCORRECT_BUYER_ID);
	}
	 */
	/* TBD */
	public void validateIncorrectEtradingId(YFSEnvironment env)
			throws Exception {
		String tagName = null;
		if (isRuleOverriddenAtOrderLevel(XPXCustomerProfileRuleConstant.INCORRECT_ETRADING_ID)) {
			return;
		}
		Document inXML = getOrderXML();
		Element orderElement = inXML.getDocumentElement();
		String extnETradingID = SCXmlUtil.getXpathAttribute(orderElement,
		"/Order/Extn/@ExtnETradingID");
		if (YFCCommon.isVoid(custOutPutDoc)) 
				callCustomerList(env);		
		String customerEtrading = SCXmlUtil.getXpathAttribute(
				custOutPutDoc.getDocumentElement(),
				"/CustomerList/Customer/Extn/@ExtnETradingID");
		if (!YFCCommon.isVoid(extnETradingID)
				&& !extnETradingID.equals(customerEtrading)) {
			addErrorMsgToOrder(
					env,
					XPXCustomerProfileRuleConstant.INCORRECT_ETRADING_ID_ERROR_TEXT,
					XPXCustomerProfileRuleConstant.INCORRECT_ETRADING_ID);
		}
	}

	/* Order Line Validations */
	public void validateLineCommentsByCustomer(YFSEnvironment env,
			Element orderLineEle) throws Exception {
		Document inXML =getOrderXML();
		String orderLineKey =  orderLineEle.getAttribute("OrderLineKey");
		//Over Ride not required
		if(isRuleOverriddenAtOrderLineLevel(XPXCustomerProfileRuleConstant.LINE_COMMENTS_BY_CUSTOMER, orderLineKey)){
			return;
		}
		String headerComment = "LINE";
		String instructionText = SCXmlUtil.getXpathAttribute(orderLineEle,
				"Instructions/Instruction[@InstructionType ='" + headerComment + "']/@InstructionText");
		Element extnElement = SCXmlUtil.getChildElement(inXML.getDocumentElement(), "Extn");
		if (!YFCCommon.isVoid(instructionText)) {
			extnElement.setAttribute("ExtnWebHoldFlag", "Y");
			addErrorMsgToOrderLine(env, orderLineEle, XPXCustomerProfileRuleConstant.LINE_COMMENTS_ERROR_TEXT,
					XPXCustomerProfileRuleConstant.LINE_COMMENTS_BY_CUSTOMER);
		}
	}

	public void validateRequiredCustomerLinePO(YFSEnvironment env,
			Element orderLineEle) throws Exception {
		//String orderLineKey =  orderLineEle.getAttribute("OrderLineKey");
		//Override not required
		/*if(isRuleOverriddenAtOrderLineLevel(XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_PO, orderLineKey)){
			return;
		}*/
		String customerPONo = SCXmlUtil.getXpathAttribute(orderLineEle,
				"@CustomerPONo");
		if (YFCCommon.isVoid(customerPONo)) {
			addErrorMsgToOrderLine(env, orderLineEle, XPXCustomerProfileRuleConstant.CUSTOMER_LINE_PONO_ERROR_TEXT,
					XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_PO);
		}
	}

	public void validateRequiredCustomerLineField(YFSEnvironment env,
			Element orderLineEle, HashMap<String, String> paramMap)
			throws Exception {
		String param = paramMap.get("Param1");
		if (YFCCommon.isVoid(custOutPutDoc)) 
			callCustomerList(env);
		validateCustomerLineField(env,orderLineEle,param);	
	}

	/*public void validateRequiredCustomerLineField2(YFSEnvironment env,
			Element orderLineEle, HashMap<String, String> paramMap)
			throws Exception {
		String orderLineKey =  orderLineEle.getAttribute("OrderLineKey");
		//Override not required
		if(isRuleOverriddenAtOrderLineLevel(XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_FIELD2, orderLineKey)){
			return;
		}
		String tagName = paramMap.get("Param1");
		Element customerLineField2 = SCXmlUtil
				.getXpathElement(orderLineEle,
						"Instructions/Instruction[@InstructionType ='"
								+ tagName + "']");
		String customerLineField2 = SCXmlUtil.getXpathAttribute(orderLineEle,
		"Extn/@ExtnCustLineField2");
		if (YFCCommon.isVoid(customerLineField2)) {
			addErrorMsgToOrderLine(
					env,
					orderLineEle,
					XPXCustomerProfileRuleConstant.CUST_LINE2_ERROR_TEXT,
					XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_FIELD2);
		}
	}*/

	/*public void validateRequiredCustomerLineField3(YFSEnvironment env,
			Element orderLineEle, HashMap<String, String> paramMap)
			throws Exception {
		String orderLineKey =  orderLineEle.getAttribute("OrderLineKey");
		//Override not required
		if(isRuleOverriddenAtOrderLineLevel(XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_FIELD3, orderLineKey)){
			return;
		}
		String tagName = paramMap.get("Param1");
		Element customerLineField3 = SCXmlUtil
				.getXpathElement(orderLineEle,
						"Instructions/Instruction[@InstructionType ='"
								+ tagName + "']");
		String customerLineField3 = SCXmlUtil.getXpathAttribute(orderLineEle,
		"Extn/@ExtnCustLineField3");
		if (YFCCommon.isVoid(customerLineField3)) {
			addErrorMsgToOrderLine(
					env,
					orderLineEle,
					XPXCustomerProfileRuleConstant.CUST_LINE3_ERROR_TEXT,
					XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_FIELD3);
		}
	}*/

	public void validateAllDeliverydatesDonotMatch(YFSEnvironment env,
			Element orderLineEle) throws Exception {
		if (isRuleOverriddenAtOrderLevel(XPXCustomerProfileRuleConstant.ALL_DELIVERY_DATES_DONOT_MATCH)) {
			return;
		}
		boolean firstCheck = getFirstOrderlineCheck();
		String reqDeliveryDate = SCXmlUtil.getXpathAttribute(orderLineEle,"@ReqDeliveryDate");
		lastReqDeliveryDate = getLastReqDeliveryDate();
		if (!YFCCommon.isVoid(reqDeliveryDate)) {
			Date reqShipDate = SCDateTimeUtil.getDate(reqDeliveryDate, SCDateTimeUtil.ISO_DATE_FORMAT, TimeZone
					.getDefault());		
			reqDeliveryDate = formatDate(reqShipDate);
		}
		if (!YFCCommon.isVoid(lastReqDeliveryDate)) {
			Date reqShipDate = SCDateTimeUtil.getDate(lastReqDeliveryDate, SCDateTimeUtil.ISO_DATE_FORMAT, TimeZone
					.getDefault());		
			lastReqDeliveryDate = formatDate(reqShipDate);
		}		
		if (!firstCheck && !reqDeliveryDate.equals(lastReqDeliveryDate)) {
			addErrorMsgToOrder(
					env,
					XPXCustomerProfileRuleConstant.REQUESTED_DELIVERY_DATE_ERROR_TEXT,
					XPXCustomerProfileRuleConstant.ALL_DELIVERY_DATES_DONOT_MATCH);
		}
		setLastReqDeliveryDate(reqDeliveryDate);
		if (firstCheck) {
			setFirstOrderlineCheck(false);
		}
	}

	public void validatePreventBackOrder(YFSEnvironment env) throws Exception {
		//EDI Specific rule

		Document inXML = getOrderXML();
		if (isRuleOverriddenAtOrderLevel(XPXCustomerProfileRuleConstant.PREVENT_BACK_ORDER)) {
			return;
		}
		XPXPriceAndAvailability output = outputPnAREsponse;
		boolean hasBackOrderLines=false;
		NodeList nodList = inXML.getElementsByTagName("OrderLine");
		int length = nodList.getLength();
		if (length > 0) {
			for (int counter = 0; counter < length; counter++) {
				orderLineLevelErrorElement=null;
				Element orderLineEle = (Element) nodList.item(counter);
				Element itemElement = SCXmlUtil.getChildElement(orderLineEle, "ItemDetails");
				Element extn = SCXmlUtil.getChildElement(itemElement, "Extn");
				String itemType = null;
				if(!YFCCommon.isVoid(extn))
					itemType = extn.getAttribute("@ExtnItemType");
				if(!YFCCommon.isVoid(itemType)&&!"STOCK".equals(itemType)){
					return;
				}
				String orderedQty = orderLineEle.getAttribute("OrderedQty") ;
				int orderQty = 0;
				if(!YFCCommon.isVoid(orderedQty)){
					orderQty = Float.valueOf(orderedQty).intValue();
				}	
				Vector items = output.getItems();
				Iterator it = items.iterator();
				while(it.hasNext())
				{
					XPXItem item = (XPXItem) it.next();
					//check for condition where pna corresponds to this line
					if(findMatchingPnaLineResponse(orderLineEle.getAttribute("PrimeLineNo"),item.getLineNumber()))
					{
						Vector wareHouseList = item.getWarehouseLocationList();
						XPXWarehouseLocation wareHouseItem = null;
						Float availableQty = Float.parseFloat("0.00");
						for (Object wareHouse : wareHouseList) {
							wareHouseItem = (XPXWarehouseLocation) wareHouse;
							//next day availability
						//	if(wareHouseItem.getNumberOfDays().equals("1")){ Modified code for Jira 2278  for Prevent Back Order Functionality.   
							availableQty += Float.valueOf(wareHouseItem
									.getAvailableQty());
						//	}
						}
						if (orderQty > availableQty.intValue()) {
							hasBackOrderLines = true;
							addErrorMsgToOrderLine(
									env,
									orderLineEle,
									XPXCustomerProfileRuleConstant.PREVENT_BACKORDER_ERROR_TEXT,
									XPXCustomerProfileRuleConstant.PREVENT_BACK_ORDER);
						}
					}
				}
			}		
		}
		if(hasBackOrderLines)
		{
			addErrorMsgToOrder(env, XPXCustomerProfileRuleConstant.PREVENT_BACKORDER_ERROR_TEXT,XPXCustomerProfileRuleConstant.PREVENT_BACK_ORDER);
		}
	}

	public void validateItemNotAllowedForNextDayShipment(YFSEnvironment env,
			Element orderLineEle) throws Exception {
		// EDI Specific rule
		Document inXML = getOrderXML();
		Element itemElement = SCXmlUtil.getChildElement(orderLineEle, "Item");
		Element extn = SCXmlUtil.getChildElement(itemElement, "Extn");
		String itemType= null;
		if(!YFCCommon.isVoid(extn))
		 itemType = extn.getAttribute("@ExtnItemType");
		if(!"STOCK".equals(itemType)){
			return;
		}
		String orderLineKey = orderLineEle.getAttribute("OrderLineKey");
		/*if (isRuleOverriddenAtOrderLineLevel(
				XPXCustomerProfileRuleConstant.ITEM_NOT_ALLOWED_FOR_NEXT_DAY_SHIPMENT,
				orderLineKey)) {
			return;
		}*/
					XPXItem item = getPnaReseponseItem(inXML,orderLineEle);
					if(!SCUtil.isVoid(item)){
						Vector wareHouseList = item.getWarehouseLocationList();
						XPXWarehouseLocation wareHouseItem = null;
						boolean itemNotPresentNextShipDay = false;
						for (Object wareHouse : wareHouseList) {
							wareHouseItem = (XPXWarehouseLocation) wareHouse;
							if (wareHouseItem.getNumberOfDays().equals("1")) {
								// Next day
								itemNotPresentNextShipDay = true;
							}
						}
						if (!itemNotPresentNextShipDay) {
							addErrorMsgToOrderLine(
									env,
									orderLineEle,
									XPXCustomerProfileRuleConstant.ALLOW_BACKORDER_ERROR_TEXT,
									XPXCustomerProfileRuleConstant.ITEM_NOT_ALLOWED_FOR_NEXT_DAY_SHIPMENT);
						}
					}
	
	}

	/*public void validateLineLevelCodeByCustomer(YFSEnvironment env,
			Element orderLineEle) throws Exception {
		String orderLineKey =  orderLineEle.getAttribute("OrderLineKey");
		if(isRuleOverriddenAtOrderLineLevel(XPXCustomerProfileRuleConstant.LINE_LEVEL_CODE_BY_CUSTOMER, orderLineKey)){
			return;
		}
		String tagName = null;
		addErrorMsgToOrderLine(env, orderLineEle, tagName,
				XPXCustomerProfileRuleConstant.LINE_LEVEL_CODE_BY_CUSTOMER);
	}

	public void validatePreventPriceBelowCost(YFSEnvironment env,
			Element orderLineEle) throws Exception {
		String orderLineKey =  orderLineEle.getAttribute("OrderLineKey");
		if(isRuleOverriddenAtOrderLineLevel(XPXCustomerProfileRuleConstant.PREVENT_PRICE_BELOEW_COST, orderLineKey)){
			return;
		}
		String tagName = null;
		addErrorMsgToOrderLine(env, orderLineEle, tagName,
				XPXCustomerProfileRuleConstant.PREVENT_PRICE_BELOEW_COST);
	}*/

	public void validatePriceDiscrepency(YFSEnvironment env,
			Element orderLineEle, HashMap<String, String> paramMap)
			throws Exception {
		Document inXML = getOrderXML();
		String orderLineKey = orderLineEle.getAttribute("OrderLineKey");
		/*if (ruleMap
				.containsKey(XPXCustomerProfileRuleConstant.ACCEPT_PRICE_OVERRIDE)
				|| isRuleOverriddenAtOrderLineLevel(
						XPXCustomerProfileRuleConstant.PRICE_DISCREPENCY,
						orderLineKey)) {
			return;
		}*/
		if (ruleMap
				.containsKey(XPXCustomerProfileRuleConstant.ACCEPT_PRICE_OVERRIDE)) {
			return;
		}
					XPXItem item = getPnaReseponseItem(inXML,orderLineEle);
					
					if(!SCUtil.isVoid(item)){
						String orderLineUnitPrice = SCXmlUtil.getXpathAttribute(orderLineEle,
						"LinePriceInfo/@UnitPrice");
				Float lineUnitPrice = Float.parseFloat(orderLineUnitPrice);
				Float pandAUnitPrice = Float.parseFloat(item
						.getUnitPricePerRequestedUOM());
				Float tempDouble = Float.parseFloat("0.00");
				if(pandAUnitPrice.compareTo(tempDouble)!=0){
					tempDouble = ((lineUnitPrice - pandAUnitPrice) / pandAUnitPrice) * 100;
				}
				String param1 = paramMap.get("Param1");
				Float param1Float = null;
				if (param1 != null) {
					param1Float = Float.parseFloat(param1);
				}
				String tagName = null;
				if (param1Float != null & tempDouble.compareTo(param1Float) > 0) {

					addErrorMsgToOrderLine(env, orderLineEle, tagName,
							XPXCustomerProfileRuleConstant.PRICE_DISCREPENCY);
				}
				}

	}

	/*public void validatePartNumber(YFSEnvironment env, Element orderLineEle)
			throws Exception {
		String orderLineKey =  orderLineEle.getAttribute("OrderLineKey");
		if(isRuleOverriddenAtOrderLineLevel(XPXCustomerProfileRuleConstant.PART_NUMBER_VALIDATION, orderLineKey)){
			return;
		}
		addErrorMsgToOrderLine(env, orderLineEle, XPXCustomerProfileRuleConstant.ITEM_ID_ERROR_TEXT,
				XPXCustomerProfileRuleConstant.PART_NUMBER_VALIDATION);
	}*/

	/* Order Line Validations ends */
	private void addErrorMsgToOrder(YFSEnvironment env, String name,
			String ruleID) throws Exception {
		
		Document inXML = getOrderXML();
		Element extnElement = SCXmlUtil.getChildElement(inXML.getDocumentElement(), "Extn");
		if(extnElement!=null)
			extnElement.setAttribute("ExtnOrdHdrLevelFailedRuleID", ruleID);
		
		String errorMsg = getCommonCodeForRuleId(env, ruleID);
		if(orderLevelErrorElement == null)
		 orderLevelErrorElement = SCXmlUtil.getChildElement(inXML.getDocumentElement(),"Error", true);
		orderLevelErrorElement.setAttribute(name, errorMsg);
	}

	private void addErrorMsgToOrderLine(YFSEnvironment env, Element ele,
			String errorCode, String ruleID) throws Exception {
		//Element errorEle = SCXmlUtil.createChild(ele, "Error");
		Element extnLineElement = SCXmlUtil.getChildElement(ele, "Extn");
		if(extnLineElement!=null)
			extnLineElement.setAttribute("ExtnOrdLineLevelFailedRuleID", ruleID);
		
		String errorMsg = getCommonCodeForRuleId(env, ruleID);
		if(orderLineLevelErrorElement == null)
			orderLineLevelErrorElement = SCXmlUtil.getChildElement(ele,"Error", true);
		orderLineLevelErrorElement.setAttribute(errorCode, errorMsg);
	}

	private String getCommonCodeForRuleId(YFSEnvironment env, String ruleID)
	throws Exception {
		String desc = "";
		// form the input
		if(YFCCommon.isVoid(outputCommonErrorListDoc)){
			getRuleEngineErrorMessages(env);
		}
		if(errorDescMap.containsKey(ruleID)){
			desc=errorDescMap.get(ruleID);
		}
		return desc;
	}
	private void getRuleEngineErrorMessages(YFSEnvironment env) throws Exception {

		YFCDocument inputDoc = YFCDocument.createDocument("CommonCode");
		YFCElement inputElement = inputDoc.getDocumentElement();
		inputElement.setAttribute("CodeType", "XPXRuleID");
		inputElement.setAttribute("OrganizationCode", "xpedx");
		 outputCommonErrorListDoc = api.invoke(env, "getCommonCodeList", inputDoc
			.getDocument());
		NodeList orgList = outputCommonErrorListDoc.getElementsByTagName("CommonCode");
		int orgLength = orgList.getLength();
		if (orgLength > 0) {
			for(int i=0;i<orgLength;i++){
			Element listElement = (Element) orgList.item(i);
			String tmpRuleid=listElement.getAttribute("CodeValue");
			String descTmp = listElement.getAttribute("CodeShortDescription");
			errorDescMap.put(tmpRuleid, descTmp);
			}
		}
		
	}
	public void validateOrderLines(YFSEnvironment env, HashMap<String, HashMap<String,String>> lineRulesMap)
			throws Exception {
		Document inXML = getOrderXML();
		NodeList nodList = inXML.getElementsByTagName("OrderLine");
		int length = nodList.getLength();
		if (length > 0) {
			for (int counter = 0; counter < length; counter++) {
				orderLineLevelErrorElement=null;
				Element ele = (Element) nodList.item(counter);
				Element extnLineElement = SCXmlUtil.getChildElement(ele, "Extn");
				if(extnLineElement!=null)
					extnLineElement.setAttribute("ExtnOrdLineLevelFailedRuleID", "");
				
				if(XPXCustomerProfileRuleConstant.CHARGE_TYPE.equals(ele.getAttribute("LineType"))){
					continue;
				}
				/*** Start of Fix for Jira   XNGTP-2572 by Hemachandra****/
				if(ele.getAttribute("Status").equals("Cancelled")|| ele.getAttribute("Status").equals("")||ele.getAttribute("Status")==null){
					
					continue;
				}
				/*****End of Fix for Jira XNGTP-2572   *****/
				
				for(Entry<String,HashMap<String,String>> entry:lineRulesMap.entrySet()){
					CustomerProfileRuleID ruleid = CustomerProfileRuleID.valueOf(entry.getKey());
					String orderLineKey =  ele.getAttribute("OrderLineKey");
					if (isRuleOverriddenAtOrderLineLevel(entry.getKey(),orderLineKey))
						continue;
					
					switch (ruleid) {
					case LineCommentsByCustomer:
						validateLineCommentsByCustomer(env, ele);
						break;
					case RequiredCustomerLinePO:
						validateRequiredCustomerLinePO(env, ele);
						break;
					case RequireCustomerLineField1:
					case RequireCustomerLineField2:
					case RequireCustomerLineField3:
						validateRequiredCustomerLineField(env, ele, entry.getValue());
						break;
					case AllDeliveryDatesDoNotMatch:
						validateAllDeliverydatesDonotMatch(env, ele);
						break;
					case CustomerSelectedShipComplete:	
						/*Moved to order header*/
//					case PreventBackOrder:
//						validatePreventBackOrder(env, ele);
//						break;
					case ItemNotAvailableForNextDayShipment:
						validateItemNotAllowedForNextDayShipment(env, ele);
						break;
					/*case LineLevelCodeByCustomer:
						validateLineLevelCodeByCustomer(env, ele);
						break;
					case PreventPriceBelowCost:
						validatePreventPriceBelowCost(env, ele);
						break;
					case PriceDiscrepency:
						validatePriceDiscrepency(env, ele, entry.getValue());
						break;
					case PartNumberValidation:
						validatePartNumber(env, ele);
						break;*/
					case RequiredCustomerLineSequenceNo:
						validateCustomerLineSequenceNo(env,ele);
						break;
					case RequiredCustomerLineAccountNo:
						validateCustomerLineAccountNo(env,ele);
						break;
					case GrossTradingMargin:
						validateGrossTradingMargin(env, ele, entry.getValue());
						break;						
					default:
					}
				}				
			}
		}
	}

	private boolean isRuleOverriddenAtOrderLevel(String ruleID) {
		Document inXML = getOrderXML();
		boolean ruleOverridden = false;
		ruleOverridden = Boolean.parseBoolean(SCXmlUtil.getXpathAttribute(inXML
				.getDocumentElement(),
				"/Order/Extn/XPXDashboardOverrideList/XPXDashboardOverride[@RuleId='"
						+ ruleID + "']/@OverrideFlag='Y'"));
		return ruleOverridden;
	}

	private boolean isRuleOverriddenAtOrderLineLevel(
			String ruleID, String orderLineKey) {
		Document inXML = getOrderXML();
		boolean ruleOverridden = false;
		ruleOverridden = Boolean.parseBoolean(SCXmlUtil.getXpathAttribute(inXML
				.getDocumentElement(),
				"/Order/Extn/XPXDashboardOverrideList/XPXDashboardOverride[@RuleId='"
						+ ruleID + "' and @OrderLineKey ='" + orderLineKey
						+ "']/@OverrideFlag='Y'"));
		return ruleOverridden;
	};	 

	public XPXPriceAndAvailability callPAndAService(YFSEnvironment env, Document inXML){
		ArrayList<XPXItem> inputItems = new ArrayList<XPXItem>();
		NodeList nodList = inXML.getElementsByTagName("OrderLine");
		String customerId= inXML.getDocumentElement().getAttribute("BuyerOrganizationCode");
		int length = nodList.getLength();
		if (length > 0) {
			for (int counter = 0; counter < length; counter++) {
				orderLineLevelErrorElement=null;
				Element orderLineEle = (Element) nodList.item(counter);
				Element tranElement = SCXmlUtil.getChildElement(orderLineEle, "OrderLineTranQuantity");
				Element itemElement = SCXmlUtil.getChildElement(orderLineEle, "Item");
				XPXItem inputItem = new XPXItem();
				inputItem.setLegacyProductCode(itemElement.getAttribute("ItemID"));
				inputItem.setRequestedQtyUOM(tranElement.getAttribute("TransactionalUOM"));
				inputItem.setRequestedQty(tranElement.getAttribute("OrderedQty"));
				inputItem.setLineNumber(orderLineEle.getAttribute("PrimeLineNo"));
				inputItems.add(inputItem);
			}
		}
		XPXPriceAndAvailability output = XPXPriceandAvailabilityUtil.getPriceAndAvailability(env, inputItems, customerId);
		this.outputPnAREsponse=output;
		return output;
	}	
	/* Tempalate to get ZipCode 
	<Customer>
	<BuyerOrganization> 
	  <CorporatePersonInfo ZipCode="" /> 
	</BuyerOrganization>
	</Customer> */
	private Document createTemplateForCustomerList() {
		YFCDocument customeListTemplateDoc = YFCDocument
				.createDocument("Customer");
		YFCElement customerList = customeListTemplateDoc
				.getDocumentElement();
		YFCElement extn = customerList
		.createChild("Extn");
		extn.setAttribute("ExtnETradingID", "");
		extn.setAttribute("ExtnCustLineField1Label", "");
		extn.setAttribute("ExtnCustLineField2Label", "");
		extn.setAttribute("ExtnCustLineField3Label", "");
//		YFCElement buyerOrganization = customerList
//				.createChild("BuyerOrganization");
//		YFCElement corporatePersonInfo = buyerOrganization
//				.createChild("CorporatePersonInfo");
//		corporatePersonInfo.setAttribute("ZipCode", "");
		YFCElement customerAddtlAddressList = customerList
				.createChild("CustomerAdditionalAddressList");
		YFCElement customerAdditionalAddress = customerAddtlAddressList
				.createChild("CustomerAdditionalAddress");
		YFCElement personInfo = customerAdditionalAddress
				.createChild("PersonInfo");
		personInfo.setAttribute("ZipCode", "");
		return customeListTemplateDoc.getDocument();
	}
	private void callCustomerList(YFSEnvironment env) throws Exception{
		Element orderElement = getOrderXML().getDocumentElement();
		//String customerID = orderElement.getAttribute("BillToID");
		String customerID = orderElement.getAttribute("BuyerOrganizationCode");
		String organizationCode = orderElement
				.getAttribute("EnterpriseCode");
		YFCDocument customeListDoc = YFCDocument.createDocument("Customer");
		YFCElement customerElement = customeListDoc.getDocumentElement();
		customerElement.setAttribute("CustomerID", customerID);
		customerElement.setAttribute("OrganizationCode", organizationCode);
//		env.setApiTemplate("XPXGetCustomerListService",
//				createTemplateForCustomerList());
		custOutPutDoc = api.executeFlow(env, "XPXGetCustomerInfoForCC_OMService",
				customeListDoc.getDocument());
//		env.clearApiTemplate("getCustomerList");
	}
	private void validateCustomerLineField(YFSEnvironment env,Element orderLineEle, String param)throws Exception{
		String customerField1 = SCXmlUtil.getXpathAttribute(
				custOutPutDoc.getDocumentElement(),
				"/CustomerList/Customer/Extn/@ExtnCustLineField1Label");
		String customerField2 = SCXmlUtil.getXpathAttribute(
				custOutPutDoc.getDocumentElement(),
				"/CustomerList/Customer/Extn/@ExtnCustLineField2Label");
		String customerField3 = SCXmlUtil.getXpathAttribute(
				custOutPutDoc.getDocumentElement(),
				"/CustomerList/Customer/Extn/@ExtnCustLineField3Label");
		String orderLineField = null;
		if(param.equals(customerField1)){
			orderLineField = SCXmlUtil.getXpathAttribute(orderLineEle,
			"Extn/@ExtnCustLineField1");
			if(YFCCommon.isVoid(orderLineField)){
				addErrorMsgToOrderLine(
						env,
						orderLineEle,
						XPXCustomerProfileRuleConstant.CUST_LINE1_ERROR_TEXT,
						XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_FIELD1);
			}
				
		}else if(param.equals(customerField2)){
			orderLineField = SCXmlUtil.getXpathAttribute(orderLineEle,
			"Extn/@ExtnCustLineField2");
			if(YFCCommon.isVoid(orderLineField)){
				addErrorMsgToOrderLine(
						env,
						orderLineEle,
						XPXCustomerProfileRuleConstant.CUST_LINE2_ERROR_TEXT,
						XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_FIELD2);
			}
		} else if(param.equals(customerField3)){
			orderLineField = SCXmlUtil.getXpathAttribute(orderLineEle,
			"Extn/@ExtnCustLineField3");
			if(YFCCommon.isVoid(orderLineField)){
				addErrorMsgToOrderLine(
						env,
						orderLineEle,
						XPXCustomerProfileRuleConstant.CUST_LINE3_ERROR_TEXT,
						XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_FIELD3);
			}
		}		
	}
	public void validateCustomerLineSequenceNo(YFSEnvironment env,
			Element orderLineEle) throws Exception {
		//String orderLineKey =  orderLineEle.getAttribute("OrderLineKey");
		/*if (isRuleOverriddenAtOrderLineLevel(XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_SEQUENCE_NO,orderLineKey)) {
			return;
		}*/
		/* Using CustomerLinePONo as CustomerLineSeqNo*/
		String customerLineSeqNo = SCXmlUtil.getXpathAttribute(orderLineEle,
				"@CustomerLinePONo");
		if (YFCCommon.isVoid(customerLineSeqNo)) {
			addErrorMsgToOrderLine(env, orderLineEle, XPXCustomerProfileRuleConstant.MANDATORY_CUST_LINE_SEQ_NO,
					XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_SEQUENCE_NO);
		}
	}
	public void validateCustomerLineAccountNo(YFSEnvironment env,
			Element orderLineEle) throws Exception {
		/*String orderLineKey =  orderLineEle.getAttribute("OrderLineKey");
		if (isRuleOverriddenAtOrderLineLevel(XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_ACCOUNT_NO,orderLineKey)) {
			return;
		}*/
		String customerLineAcctNo = SCXmlUtil.getXpathAttribute(orderLineEle,
				"Extn/@ExtnCustLineAccNo");
		if (YFCCommon.isVoid(customerLineAcctNo)) {
			addErrorMsgToOrderLine(env, orderLineEle, XPXCustomerProfileRuleConstant.MANDATORY_CUST_LINE_ACCT_NO,
				XPXCustomerProfileRuleConstant.REQUIRED_CUSTOMER_LINE_ACCOUNT_NO);
		}
	}

	public void validateRequiredCustomerPO(YFSEnvironment env) throws Exception {
		// TODO Auto-generated method stub
		Document inXML = getOrderXML();
		String customerPONumber = SCXmlUtil.getXpathAttribute(inXML.getDocumentElement(), "/Order/@CustomerPONo");		
		if (YFCCommon.isVoid(customerPONumber)) {
			//TODO:CHECK CUSTOMER_PONO_ERROR_TEXT ALREADY THERE
			addErrorMsgToOrder(env, XPXCustomerProfileRuleConstant.REQ_CUSTOMER_PONO_ERROR_TEXT,
					XPXCustomerProfileRuleConstant.REQUIRE_CUSTOMER_PO);
		}		
	}
	private void validateGrossTradingMargin(YFSEnvironment env, Element orderLineEle, HashMap<String, String> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Document inXML = getOrderXML();
		String orderLineKey = orderLineEle.getAttribute("OrderLineKey");
		Element extnOrderLineEle=SCXmlUtils.getChildElement(orderLineEle, "Extn");
		String priceOverrideFlag = extnOrderLineEle.getAttribute("ExtnPriceOverrideFlag");
		
		if (isRuleOverriddenAtOrderLineLevel(
						XPXCustomerProfileRuleConstant.GROSS_TRADING_MARGIN,
						orderLineKey)) {
			return;
		}
		XPXItem item = getPnaReseponseItem(inXML,orderLineEle);
					
		if(!SCUtil.isVoid(item)){					
		if (!YFCCommon.isVoid(priceOverrideFlag)&&"Y".equalsIgnoreCase(priceOverrideFlag)){
		String orderLineUnitPrice =extnOrderLineEle.getAttribute("ExtnUnitPrice");
		Float lineUnitPrice = Float.parseFloat(orderLineUnitPrice);
		Float pandACost = Float.parseFloat(item
							.getItemCost());
		Float tempDouble = Float.parseFloat("0.00");
		if (pandACost.compareTo(tempDouble) != 0) {
			//	tempDouble = (lineUnitPrice / pandACost) * 100;
			tempDouble=((lineUnitPrice-pandACost)/pandACost)*100;
			}
							String param1 = paramMap.get("Param1");
							String param2= paramMap.get("Param2");
							Float param1Float = null;
							Float param2Float = null;
							if (!YFCCommon.isVoid(param1)) {
								param1Float = Float.parseFloat(param1);
							}
							if (!YFCCommon.isVoid(param2)) {
								param2Float = Float.parseFloat(param2);
							}
							
							if ((param1Float != null && tempDouble<param1Float) || (param2Float != null && tempDouble>param2Float)) {

								addErrorMsgToOrderLine(
										env,
										orderLineEle,
										XPXCustomerProfileRuleConstant.GROSS_TRADING_MARGIN_ERROR_TEXT,
										XPXCustomerProfileRuleConstant.GROSS_TRADING_MARGIN);
							}
							}
							
							else{


								Float pandACost = Float.parseFloat(item
										.getItemCost());
								String orderLineUnitPrice = extnOrderLineEle.getAttribute("ExtnUnitPrice");
								Float lineUnitPrice = Float.parseFloat(orderLineUnitPrice);
								Float tempDouble = Float.parseFloat("0.00");
								if(pandACost.compareTo(tempDouble)!=0){
								//	tempDouble = (pandAUnitPrice) / pandACost * 100;
									tempDouble=((lineUnitPrice-pandACost)/pandACost)*100;
								}
								String param1 = paramMap.get("Param1");
								String param2= paramMap.get("Param2");
								Float param1Float = Float.parseFloat("0.00");
								Float param2Float = Float.parseFloat("0.00");
								if (!YFCCommon.isVoid(param1)) {
									param1Float = Float.parseFloat(param1);
								}
								if (!YFCCommon.isVoid(param2)) {
									param2Float = Float.parseFloat(param2);
								}
								if ((param1Float != null && tempDouble<param1Float) || (param2Float != null && tempDouble>param2Float)){

									addErrorMsgToOrderLine(env, orderLineEle, XPXCustomerProfileRuleConstant.GROSS_TRADING_MARGIN_ERROR_TEXT,
											XPXCustomerProfileRuleConstant.GROSS_TRADING_MARGIN);
								}			
							
							
						}
		}
	}	
	
	private XPXItem getPnaReseponseItem(Document inXML, Element orderLineEle){
		XPXPriceAndAvailability output = outputPnAREsponse;
				NodeList nodList = inXML.getElementsByTagName("OrderLine");
				XPXItem itemReturn=null;
		int length = nodList.getLength();
		if (length > 0) {
			Vector items = output.getItems();
			Iterator it = items.iterator();
			
			for (int counter = 0; counter < length; counter++) {
				
				while(it.hasNext())
				{
					XPXItem item = (XPXItem) it.next();
					if(findMatchingPnaLineResponse(orderLineEle.getAttribute("PrimeLineNo"),item.getLineNumber()))
					{
						itemReturn= item;
					}
					
				}
			}
		}
		return itemReturn;
		
	}
}
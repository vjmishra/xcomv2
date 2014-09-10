package com.sterlingcommerce.xpedx.webchannel.order;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.tools.datavalidator.XmlUtils;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.order.OrderConstants;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

import edu.emory.mathcs.backport.java.util.Collections;

public class XPEDXPrintOrderDetailAction extends XPEDXExtendedOrderDetailPrintAction {

	private static final String customerExtnInformation = "xpedx-customer-getCustomerAllExtnInformation";
	private static final String ORGANIZATION_KEY_NAME_MAP = "OrganizationKeyNameMap";
	public static HashMap<String, String> orgsKeyNameMap = new HashMap<String, String>();


	public static HashMap<String, String> getOrgsKeyNameMap() {
		return orgsKeyNameMap;
	}


	public static void setOrgsKeyNameMap(HashMap<String, String> orgsKeyNameMap) {
		XPEDXPrintOrderDetailAction.orgsKeyNameMap = orgsKeyNameMap;
	}
	
	public  Map<String, String> getDivisionList() throws CannotBuildInputException {
		Element outputElem;
		Element orgElem;
		String organizationKey;
		String organizationName;
		Map<String, String> orgsKeyNameMap = (HashMap<String, String>)XPEDXWCUtils.getObjectFromCache("OrgKeyNameMAP");
		if(orgsKeyNameMap == null) 
		{
		outputElem =  prepareAndInvokeMashup("XPXGetDivisionList");	
		Element outputOrgElem = outputElem.getOwnerDocument().getDocumentElement();			
		NodeList orgList = outputOrgElem.getElementsByTagName("Organization");
			for (int i = 0; orgList != null && i < orgList.getLength(); i++) {
				orgElem = (Element) orgList.item(i);
				organizationKey = orgElem.getAttribute("OrganizationCode");
				organizationName = orgElem.getAttribute("OrganizationName");
				orgsKeyNameMap.put(organizationKey, organizationName);
				wcContext.setWCAttribute("OrgKeyNameMAP", orgsKeyNameMap,
						WCAttributeScope.SESSION);
			}
		}
		XPEDXWCUtils.setObectInCache("OrgKeyNameMAP", orgsKeyNameMap);
		return orgsKeyNameMap;
	}
	
	@Override
	public String execute() {
		//XPEDXWCUtils.resetPendingChanges(getOrderHeaderKey(),wcContext);
		//registerLogin();
		String returnString = super.execute();
		XPEDXWCUtils.setItemDetailBackPageURLinSession();
		setHeaderComment(getHeaderCommentValue());
		setDisplayTaxAndShipHandlingAmt(getMaxOrderStatusValue());
		//BEGIN: sort the orderlines based on legacy line number
		ArrayList<Element> tempMajorLineElements = getMajorLineElements();
		Collections.sort(tempMajorLineElements, new XpedxLineSeqNoComparator());
		//END: sort the orderlines based on legacy line number
				
		userKey = (String)getWCContext().getSCUIContext().getSession().getAttribute(ENC_USER_KEY);
		if (userKey == null || userKey.trim().equals("")) {
			//Getting the User Key from loginID
			String loginId = getWCContext().getLoggedInUserId();
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/User/@Loginid", loginId);
			Element inputEle;
			try {
				inputEle = WCMashupHelper.getMashupInput("XPEDXUserListMashup",
						valueMap, wcContext.getSCUIContext());

				Element userEle = (Element)WCMashupHelper.invokeMashup("XPEDXUserListMashup",
						inputEle, wcContext.getSCUIContext());
				userKey = SCXmlUtil.getXpathAttribute(userEle, "//UserList/User/@UserKey");
				if(userKey != null && userKey.trim().length() > 0) {
					userKey = XPEDXWCUtils.encrypt(userKey);
					userKey = URLEncoder.encode(userKey);
					getWCContext().getSCUIContext().getSession().setAttribute(ENC_USER_KEY, userKey);
				}
				
			} catch (CannotBuildInputException e) {
				XPEDXWCUtils.logExceptionIntoCent(e); //JIRA 4289
				LOG.error("Error while getting user key : "+ e);
			} catch (Exception e) {
				XPEDXWCUtils.logExceptionIntoCent(e);  //JIRA 4289
				LOG.error("Error while getting user key : "+ e);
			}
		}
			try {
				getDivisionList();
			} catch (CannotBuildInputException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Document orderDoc = getOutputDocument();
			Element orderLinesElement = SCXmlUtil.getChildElement(orderDoc.getDocumentElement(), "OrderLines");
			ArrayList<Element> orderLineElemList = SCXmlUtil.getElements(orderLinesElement, "OrderLine");
			for (int i = 0; i < orderLineElemList.size(); i++) {
				Element orderLineElement = (Element)orderLineElemList.get(i);
				String lineType = orderLineElement.getAttribute("LineType");
				if(lineType.equals("P")){
					lineItemCount++;
				}
				else{
					lineOtherCount++;
				}
			}
			
		setValuesForChainedOrderMap();
		setOrderSummaryFlagValues();
		getCustomerLineDetails();
		//Added For Jira - 4326		
		isCSRReviewHold();
		
		try{
			getAllItemSKUs();
		}
		catch (Exception ex) {
			LOG.debug(ex.getMessage());
		}
		
		invoiceURL = YFSSystem.getProperty("xpedx.invoicing.url"); //to take it from properties files.
		
		return returnString;
	}
	
	//Added this method for Jira 4326 - CSR Review For FO
	private void isCSRReviewHold()
	{
		Element orderElement=getElementOrder();
		//Kubra Jira 4326
		NodeList holdtypes =orderElement.getElementsByTagName("OrderHoldType");
		if(holdtypes != null && !"Customer".equals(orderElement.getAttribute("OrderType")) && !isCSRReview )
		{
			//4326 - Kubra
			for(int i =0; i<holdtypes.getLength();i++)
			{
				Element holdType=(Element)holdtypes.item(i);
				if(holdType != null )
				{
					String holdTypeName=holdType.getAttribute("HoldType");
					if(XPEDXConstants.HOLD_TYPE_FOR_LEGACY_LINE_HOLD.equals(holdTypeName)
							|| XPEDXConstants.HOLD_TYPE_FOR_LEGACY_CNCL_ORD_HOLD.equals(holdTypeName) 
							|| XPEDXConstants.HOLD_TYPE_FOR_FATAL_ERR_HOLD.equals(holdTypeName)
							|| XPEDXConstants.HOLD_TYPE_FOR_NEEDS_ATTENTION.equals(holdTypeName)
							|| XPEDXConstants.HOLD_TYPE_FOR_ORDER_EXCEPTION_HOLD.equals(holdTypeName))
					{
						isFOCSRReview = true;
						break;
					}
				}	
			}
		}
	}
	private void getAllItemSKUs() throws CannotBuildInputException, XPathExpressionException
	{
		//Fetch all the extn fields from customer
		
		String useCustSku = (String)wcContext.getWCAttribute(XPEDXConstants.CUSTOMER_USE_SKU,WCAttributeScope.LOCAL_SESSION);
		/*String envCode = (String)wcContext.getWCAttribute(XPEDXConstants.ENVIRONMENT_CODE,WCAttributeScope.LOCAL_SESSION);
		String companyCode = (String)wcContext.getWCAttribute(XPEDXConstants.COMPANY_CODE,WCAttributeScope.LOCAL_SESSION);
		String legacyCustomerNumber = (String)wcContext.getWCAttribute(XPEDXConstants.LEGACY_CUST_NUMBER,WCAttributeScope.LOCAL_SESSION);*/
		XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		//String useCustSku = shipToCustomer.getExtnUseCustSKU();
		String envCode = shipToCustomer.getExtnEnvironmentCode();
		String companyCode = shipToCustomer.getExtnCompanyCode();
		String legacyCustomerNumber = shipToCustomer.getExtnLegacyCustNumber();
		
		if(envCode == null || companyCode==null || legacyCustomerNumber == null)
		{
			Set mashupSet = buildSetFromDelmitedList("draftOrderGetCustomerLineFields");
			Map outputMap = prepareAndInvokeMashups(mashupSet);
			Element outputEl = (Element) outputMap.get("draftOrderGetCustomerLineFields");
			Element customerOrganizationExtnEle = XMLUtilities.getChildElementByName(outputEl, "Extn");
			envCode = SCXmlUtil.getAttribute(customerOrganizationExtnEle,"ExtnEnvironmentCode");
			useCustSku = SCXmlUtil.getAttribute(customerOrganizationExtnEle,"ExtnUseCustSKU");
			companyCode = SCXmlUtil.getAttribute(customerOrganizationExtnEle,"ExtnCompanyCode");
			legacyCustomerNumber = SCXmlUtil.getAttribute(customerOrganizationExtnEle,"ExtnLegacyCustNumber");
			//set this in the session
			//wcContext.setWCAttribute(XPEDXConstants.CUSTOMER_USE_SKU,useCustSku,WCAttributeScope.LOCAL_SESSION);
			/*wcContext.setWCAttribute(XPEDXConstants.ENVIRONMENT_CODE,envCode,WCAttributeScope.LOCAL_SESSION);
			wcContext.setWCAttribute(XPEDXConstants.COMPANY_CODE,companyCode,WCAttributeScope.LOCAL_SESSION);
			wcContext.setWCAttribute(XPEDXConstants.LEGACY_CUST_NUMBER,legacyCustomerNumber,WCAttributeScope.LOCAL_SESSION);*/
			
			shipToCustomer.setExtnUseCustSKU(useCustSku);
			shipToCustomer.setExtnEnvironmentCode(envCode);
			shipToCustomer.setExtnCompanyCode(companyCode);
			shipToCustomer.setExtnLegacyCustNumber(legacyCustomerNumber);
			XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER, shipToCustomer);
		}
		
		if(useCustSku!=null && useCustSku.length()>0)
		{
			setCustomerSku(useCustSku);
		}
		
		//Fetch all the items in Cart and get their respective SKUs
		Document orderDoc = getOutputDocument();
		Element orderLinesElement = SCXmlUtil.getChildElement(orderDoc
				.getDocumentElement(), "OrderLines");
		ArrayList<Element> orderLineElemList = SCXmlUtil.getElements(orderLinesElement, "OrderLine");
		if(orderLineElemList==null || orderLineElemList.size()==0)
			return;
		
		setSkuMap(new HashMap<String, HashMap<String,String>>());
		
		ArrayList<String> itemIdList = new ArrayList<String>();		
		HashMap<String, HashMap<String,String>> itemsSkuMap = new LinkedHashMap<String, HashMap<String,String>>();
		//Get the customer extn fields
		for (int i = 0; i < orderLineElemList.size(); i++) {
			HashMap<String, String> primaryInfoSKUItemsMap = new HashMap<String, String>();  //JIRA 3935 changes done
			Element orderLineElement = (Element)orderLineElemList.get(i);
			
			String customerItem = orderLineElement.getAttribute("CustomerItem");
			String lineType = orderLineElement.getAttribute("LineType");
//			if(lineType.equals("P")){
//				lineItemCount++;
//			}
//			else{
//				lineOtherCount++;
//			}
			Element itemElement = SCXmlUtil.getChildElement(orderLineElement,
					"Item");
			String itemId = itemElement.getAttribute("ItemID");// orderline/item
			
			if(skuMap.containsKey(itemId))
				continue;
			/*Begin - Changes made by Mitesh Parikh for JIRA 3581*/
			Element itemDetailsElement = SCXmlUtil.getChildElement(orderLineElement, "ItemDetails");
			Element primeInfoElem = XMLUtilities.getElement(itemDetailsElement, "PrimaryInformation");
			if(primeInfoElem!=null)
			{
				String manufactureItem = primeInfoElem.getAttribute("ManufacturerItem");
				if(manufactureItem!=null && manufactureItem.length()>0)
					primaryInfoSKUItemsMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_MANUFACTURER_ITEM, manufactureItem);
			}
			Element extnElem = XMLUtilities.getElement(itemDetailsElement, "Extn");
			if(extnElem!=null)
			{
				String mpcCode = extnElem.getAttribute("ExtnMpc");
				if(mpcCode!=null && mpcCode.length()>0)
					primaryInfoSKUItemsMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_MPC_ITEM, mpcCode);
			}
			itemIdList.add(itemId);
			itemsSkuMap.put(itemId, primaryInfoSKUItemsMap);
			
			//HashMap<String, String> itemSkuMap = XPEDXWCUtils.getAllSkusForItem(wcContext, itemId);
			//skuMap.put(itemId, itemSkuMap);
		}
		
		if(orderLineElemList.size()>0){
			skuMap = XPEDXWCUtils.getAllSkusForItem(wcContext, itemIdList, itemsSkuMap);
		}
		/*End - Changes made by Mitesh Parikh for JIRA 3581*/
	}
	
	/**
	 * JIRA 243
	 * Modified getCustomerDetails method to consider the mashup to be invoked
	 * so that, we get only the required information - Extn fields.
	 * @param inputItems
	 * @return
	 */
	
	protected LinkedHashMap getCustomerFieldsMapfromSession(){
		/*HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
        HashMap customerFieldsSessionMap = (HashMap)localSession.getAttribute("customerFieldsSessionMap");*/
		XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
        LinkedHashMap customerFieldsSessionMap = (LinkedHashMap)XPEDXWCUtils.getObjectFromCache("customerFieldsSessionMap");
        return customerFieldsSessionMap;
	}
	
	protected void getCustomerLineDetails() {
		//get the map from the session. if null query the DB
		LinkedHashMap customerFieldsSessionMap = getCustomerFieldsMapfromSession();
        if(null != customerFieldsSessionMap && customerFieldsSessionMap.size() >= 0){
        	LOG.debug("Found customerFieldsMap in the session");
        	customerFieldsMap = customerFieldsSessionMap;
        	return;
        }
     /* HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
		
        String customerId = wcContext.getCustomerId();
		String orgCode = wcContext.getStorefrontId();

		Document customerDoc;
		try {
			customerDoc = XPEDXWCUtils.getCustomerDetails(customerId, orgCode, customerExtnInformation);
			customerFieldsMap = XPEDXOrderUtils
					.getCustomerLineFieldMap(customerDoc);
			localSession.setAttribute("customerFieldsSessionMap", customerFieldsMap);
		} catch (CannotBuildInputException e) {
			LOG.error("Unable to get Customer Line details for " + customerId
					+ ". " + e);
			if (null == customerFieldsMap) {
				customerFieldsMap = new HashMap<String, String>();
			}
		}
*/
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sterlingcommerce.webchannel.order.OrderDetailAction#
	 * getOrderDetailsMashupName()
	 */
	@Override
	protected String getOrderDetailsMashupName() {
		return XPEDXOrderConstants.XPEDX_COMPLETE_ORDER_DETAIL_MASHUP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sterlingcommerce.webchannel.order.OrderDetailAction#isCancel()
	 * Overriding this method to include other Order statuses for XPEDX which
	 * needs to be allowed to Cancel
	 */
	@Override
	public boolean isCancel() {
		if (!isUserHaveResourceAccessPermission("/swc/order/authenticatedOrdering"))
			return false;
		String supportLevel = SCXmlUtils.getAttribute(getElementOrder(),
				"SupportLevel");
		boolean bSupportLevel = false;
		bSupportLevel = supportLevel.equals("Full")
				|| supportLevel.equals("Medium");
		if (!bSupportLevel)
			return false;
		boolean containPendingChanges = SCXmlUtils.getBooleanAttribute(
				getElementOrder(), "HasPendingChanges");
		if (containPendingChanges)
			return false;
		/*
		 * if(SCXmlUtils.getAttribute(getElementOrder(),
		 * "MaxOrderStatus").equals("9000")) return false;
		 */
		String maxOrderStatus = SCXmlUtils.getAttribute(getElementOrder(),
				"MaxOrderStatus");
		if (!maxOrderStatus.equals("1100") && // Created
				!maxOrderStatus.equals("1100.0100") && // Placed
				!maxOrderStatus.equals("1100.5100") && // Legacy Open
				!maxOrderStatus.equals("1100.5200") && // Legacy web hold
				!maxOrderStatus.equals("1100.5300")) // Legacy System/Customer
														// hold
			return false;
		Element cancelModificationRuleAtOrder = SCXmlUtils
				.getElementByAttribute(getElementOrder(),
						"Modifications/Modification", "ModificationType",
						"CANCEL");
		return SCXmlUtils.getBooleanAttribute(cancelModificationRuleAtOrder,
				"ModificationAllowed");
	}

	protected String getHeaderCommentValue(){
		Element orderElem = getElementOrder();
		Element instructionsElem = SCXmlUtil.getChildElement(orderElem, "Instructions");
		
		if(instructionsElem != null){
			NodeList instructionElemList = instructionsElem.getElementsByTagName("Instruction");

			for (int i = 0; i < instructionElemList.getLength(); i++) {
				Element instructionElem = (Element) instructionElemList.item(i);
				if("HEADER".equals(instructionElem.getAttribute("InstructionType"))){
					return instructionElem.getAttribute("InstructionText");
				}
			}
		}
		
		return getHeaderComment();

	}
	
	protected void setOrderSummaryFlagValues() {
		try {
			String holdTypeForApproval = XPEDXConstants.HOLD_TYPE_FOR_PENDING_APPROVAL;
			
			Element orderElem = getOrderElementFromOutputDocument();
			Element extnElem = XMLUtilities.getChildElementByName(orderElem,
					"Extn");
			
			/*
			 * CR 3999 Start-ReasonText will be dispaly in UI
			 */
			
			Element orderHoldTypesElem = XMLUtilities.getChildElementByName(orderElem,
			"OrderHoldTypes");			
			
			ArrayList<Element> orderholdtypeelemlist = SCXmlUtils.getInstance().getChildren(orderHoldTypesElem,OrderConstants.ORDER_HOLD_TYPE);
			if(orderholdtypeelemlist.size() >0){
			for (Iterator<Element> iter = orderholdtypeelemlist.iterator(); iter.hasNext();) {
				Element orderholdtypeelem = (Element) iter.next();
				if ((orderholdtypeelem.getAttribute(OrderConstants.HOLD_TYPE))
						.trim().equals(holdTypeForApproval)) {
					String holdstatus = orderholdtypeelem.getAttribute(
							OrderConstants.STATUS).trim();
					if(holdstatus.equalsIgnoreCase(OrderConstants.REJECT_HOLD_STATUS) || holdstatus.equalsIgnoreCase(OrderConstants.RESOLVE_HOLD_STATUS))
					{ //REJECT_HOLD_STATUS
						pendingHoldStatus = holdstatus;
						 reasonText = orderholdtypeelem.getAttribute("ReasonText");
						
					}
					
				}
			  
			}}
			
			/*
			 * CR 3999 End -ReasonText will be display in UI
			 */
			/*orderHearderPendComments.put("pendinStatus", pendinStatus);
			orderHearderPendComments.put("reasonText",reasonText);
	*/
			String dhFlag = SCXmlUtils.getAttribute(extnElem,
					"ExtnDeliveryHoldFlag");
			String wcFlag = SCXmlUtils.getAttribute(extnElem, "ExtnWillCall");
			String shipCmpl = SCXmlUtils.getAttribute(extnElem,
					"ExtnShipComplete");
			String orgCode = SCXmlUtils.getAttribute(extnElem,
					"ExtnCustomerDivision");

			if ("Y".equals(dhFlag))
				deliveryHoldFlag = true;
			if ("Y".equals(wcFlag))
				willCallFlag = true;
			// Commenting the below check since, even though the ship complete option is not selected on OP even though the check box 
			// was available(Y), the msg was being displayed on order details page. 
			if (/*"Y".equals(shipCmpl) || */"C".equals(shipCmpl))
				shipComplete = true;
			String extnInvoiceNo = extnElem.getAttribute("ExtnInvoiceNo");
			String extnInvoicedDate = "";
			if (extnInvoiceNo != null && extnInvoiceNo.trim().length() > 0) {
				extnInvoicedDate = XPEDXWCUtils.getDateFromInvoiceNo(extnInvoiceNo);
				encInvoiceDate = XPEDXWCUtils.encrypt(extnInvoicedDate);
				encInvoiceDate = URLEncoder.encode(encInvoiceDate);
				extnInvoiceNo = XPEDXWCUtils.getInvoiceNoWithoutDate(extnInvoiceNo);
				encInvoiceNo = XPEDXWCUtils.encrypt(extnInvoiceNo);
				encInvoiceNo = URLEncoder.encode(encInvoiceNo);
			}
			/*String extnInvoicedDate = extnElem.getAttribute("ExtnInvoicedDate");
			if (extnInvoicedDate != null && extnInvoicedDate.trim().length() > 0 && extnInvoicedDate!="") {
				//Start -Date format change to YYYYMMDD
			SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd");

			Date date=new Date();

			try {

			//String dateTmp;
			date = sdfSource.parse(extnInvoicedDate);

			
			} catch (ParseException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

			}

			SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyyMMdd");

			extnInvoicedDate  = sdfDestination.format(date);
			} */
			//End -Date format change to YYYYMMDD
			
			
			
			/*if (extnInvoicedDate != null && extnInvoicedDate.trim().length() > 0) {
				encInvoiceDate = XPEDXWCUtils.encrypt(extnInvoicedDate);
				encInvoiceDate = URLEncoder.encode(encInvoiceDate);
			}*/
			String shipToId = orderElem.getAttribute("ShipToID");
			String[] custDetails = shipToId.split("-");
			if(custDetails!=null && custDetails.length>3)
			{
				custSuffix = custDetails[2];
				if (custSuffix != null && custSuffix.trim().length() > 0) {
					custSuffix = XPEDXWCUtils.encrypt(custSuffix);
					custSuffix = URLEncoder.encode(custSuffix);
				}
			}
		} catch (Exception ex) {
			LOG.error("Expception while getting order element", ex);
		}
	}

	/*
	 * Prepares a Map of FulfillmentOrders against each OrderLine
	 */
	public void setValuesForChainedOrderMap() {
		chainedOrderMap = new HashMap<String, ArrayList<String>>();
		// get all the orderlinekeys for the order
		ArrayList<String> orderLineKeyList = getOrderLineKeys();
		try {
			// Call the xpedxOrderDetailChainedOrderLineList mashup to get all
			// the ChainedOrderLines
			Document chainOrderLineList = getXpedxChainedOrderLineList(orderLineKeyList);
			// get all the Orderlines from the document
			NodeList nlOrderLineList = chainOrderLineList
					.getElementsByTagName("OrderLine");
			int length = nlOrderLineList.getLength();
			Element orderLine = null;
			for (int i = 0; i < length; i++) {
				// Get each orderline
				orderLine = (Element) nlOrderLineList.item(i);
				// Extract Order element from each orderline
				Element order = XMLUtilities.getElement(orderLine, "Order");
				Element orderExtn = SCXmlUtil.getChildElement(order, "Extn");
				Element orderLinetranQty = SCXmlUtil.getChildElement(orderLine,
						"OrderLineTranQuantity");
				String chainedFromOrderLineKey = orderLine
						.getAttribute("ChainedFromOrderLineKey");
				String orderHeaderKey = order.getAttribute("OrderHeaderKey");
				String webConfNumber = orderExtn.getAttribute("ExtnWebConfNum");
				String legacyOrderNo = orderExtn.getAttribute("ExtnLegacyOrderNo");
				String headerStatusCode = orderExtn.getAttribute("ExtnHeaderStatusCode");
				String quantity = orderLinetranQty.getAttribute("OrderedQty");
				String legacyOrderNumber = orderExtn
						.getAttribute("ExtnLegacyOrderNo");
				String status = order.getAttribute("Status");
				String extnInvoiceNumber = orderExtn.getAttribute("ExtnInvoiceNo");
				String extnInvoiceDate = "";
				String encInvoiceNumber = "";
				//Kubra - Jir 4326  Line Level CSRReviwing for Customer Order on FO

				NodeList orderHoldTypesElem=orderLine.getElementsByTagName("OrderHoldType");
				boolean isFOCSRReview=false;
				if(orderHoldTypesElem != null)
				{
					for(int j=0;j<orderHoldTypesElem.getLength();j++ )
					{
						Element orderHoldType=(Element)orderHoldTypesElem.item(j);
						if(orderHoldType != null)
						{
							String holdTypeName=orderHoldType.getAttribute("HoldType");
							if(XPEDXConstants.HOLD_TYPE_FOR_LEGACY_LINE_HOLD.equals(holdTypeName)
									|| XPEDXConstants.HOLD_TYPE_FOR_LEGACY_CNCL_ORD_HOLD.equals(holdTypeName) 
									|| XPEDXConstants.HOLD_TYPE_FOR_FATAL_ERR_HOLD.equals(holdTypeName)
									|| XPEDXConstants.HOLD_TYPE_FOR_NEEDS_ATTENTION.equals(holdTypeName)
									|| XPEDXConstants.HOLD_TYPE_FOR_ORDER_EXCEPTION_HOLD.equals(holdTypeName))
							{
								isFOCSRReview=true;
								break;
							}
						}
					}
				}
				if(null == legacyOrderNumber || "".equals(legacyOrderNumber.trim())) {
					isCSRReview = true;
				}
				
				if(extnInvoiceNumber != null && extnInvoiceNumber.trim().length() > 0) {
					try {
						extnInvoiceDate = XPEDXWCUtils.getDateFromInvoiceNo(extnInvoiceNumber);
						extnInvoiceDate = XPEDXWCUtils.encrypt(extnInvoiceDate);
						extnInvoiceDate = URLEncoder.encode(extnInvoiceDate);
						extnInvoiceNumber = XPEDXWCUtils.getInvoiceNoWithoutDate(extnInvoiceNumber);
						encInvoiceNumber = XPEDXWCUtils.encrypt(extnInvoiceNumber);
						encInvoiceNumber = URLEncoder.encode(encInvoiceNumber);
					} catch (Exception e) {
						LOG.error("Error encrypting invoice no " + e);
					}
				}
				//String extnInvoiceDate = orderExtn.getAttribute("ExtnInvoicedDate");				
				/*if(extnInvoiceDate != null && extnInvoiceDate.trim().length() > 0) {
					try {						
						//String inputFormat = "yyyy-MM-dd";
						//extnInvoiceDate = XPEDXWCUtils.getUnformattedDate(inputFormat, extnInvoiceDate);									
						extnInvoiceDate = XPEDXWCUtils.encrypt(extnInvoiceDate);
						extnInvoiceDate = URLEncoder.encode(extnInvoiceDate);
					} catch (Exception e) {
						LOG.error("Error encrypting invoice date " + e);
					}
				}*/
				String shipToId = order.getAttribute("ShipToID");
				String[] custDetails = shipToId.split("-");
				String chainCustSuffix = "";
				if(custDetails!=null && custDetails.length>3)
				{
					chainCustSuffix = custDetails[2];
					if (chainCustSuffix != null && chainCustSuffix.trim().length() > 0) {
						try {
							chainCustSuffix = XPEDXWCUtils.encrypt(chainCustSuffix);
						} catch (Exception e) {
							LOG.error("Error encrypting ship to id " + e);
						}
						chainCustSuffix = URLEncoder.encode(chainCustSuffix);
					}
				}
				
				//Added for JIRA 2731
				String formattedLegacyOrderNumber="";
				
				if(legacyOrderNo != null && !legacyOrderNo.equals("")){
					formattedLegacyOrderNumber = XPEDXOrderUtils.getFormattedOrderNumber(orderExtn);
				}else {
					formattedLegacyOrderNumber="In progress";
				}
			
				if((null != formattedLegacyOrderNumber) && !("".equals(formattedLegacyOrderNumber))){
					if(!chainedFOMap.containsKey(orderHeaderKey))
					{
						chainedFOMap.put(orderHeaderKey, formattedLegacyOrderNumber);
					}
				}

				/*else if((null != legacyOrderNo) && !("".equals(legacyOrderNumber))){
					if(!chainedFOMap.containsKey(orderHeaderKey))
					{
						chainedFOMap.put(orderHeaderKey, legacyOrderNo);						
					}
				} commented for 2731*/			
				
				//added for 2731
				/*if(legacyOrderNumber == null || !legacyOrderNumber.equals("")){
					legacyOrderNumber="In progress";
				}*/
				if(isFOCSRReview)
					status=status+"(CSRReviewing)";
				if(!"Cancelled".equals(orderLine.getAttribute("Status"))){
					if (null != chainedOrderMap
							&& chainedOrderMap.containsKey(chainedFromOrderLineKey)) {
						ArrayList<String> attrList = chainedOrderMap
								.get(chainedFromOrderLineKey);
						attrList.add(orderHeaderKey);
						attrList.add(legacyOrderNumber);
						attrList.add(quantity);
						attrList.add(status);
						//Added for JIRA 2731
						attrList.add(formattedLegacyOrderNumber);
						attrList.add(encInvoiceNumber);
						attrList.add(chainCustSuffix);
						attrList.add(extnInvoiceDate);
						attrList.add(extnInvoiceNumber);
					} else {
						ArrayList<String> chainedOrderAttributes = new ArrayList<String>();
						chainedOrderAttributes.add(orderHeaderKey);
						chainedOrderAttributes.add(legacyOrderNumber);
						chainedOrderAttributes.add(quantity);
						chainedOrderAttributes.add(status);
						chainedOrderAttributes.add(formattedLegacyOrderNumber);
						chainedOrderAttributes.add(encInvoiceNumber);
						chainedOrderAttributes.add(chainCustSuffix);
						chainedOrderAttributes.add(extnInvoiceDate);
						chainedOrderAttributes.add(extnInvoiceNumber);
						//Added for JIRA 2731
						chainedOrderMap.put(chainedFromOrderLineKey,
								chainedOrderAttributes);
					}
					
					YFCDocument inputDocument = YFCDocument.createDocument("Order");
					YFCElement documentElement = inputDocument.getDocumentElement();
					documentElement.setAttribute("OrderHeaderKey", orderHeaderKey);
					documentElement.setAttribute("LegacyOrderNumber", legacyOrderNumber);
					documentElement.setAttribute("Quantity", quantity);
					documentElement.setAttribute("Status", status);
					documentElement.setAttribute("FormattedLegacyOrderNumber", formattedLegacyOrderNumber);
					documentElement.setAttribute("ExtnInvoiceNo", extnInvoiceNumber);
					documentElement.setAttribute("EncInvoiceNo", encInvoiceNumber);
					documentElement.setAttribute("ExtnInvoicedDate", extnInvoiceDate);
					documentElement.setAttribute("ShipToID", chainCustSuffix);

					if(chainedOrderCountMap.containsKey(chainedFromOrderLineKey))
					{
						List<YFCElement> orderHeaderList =chainedOrderCountMap.get(chainedFromOrderLineKey);
						
						
						orderHeaderList.add(documentElement);
						chainedOrderCountMap.put(chainedFromOrderLineKey, orderHeaderList);					
					}
					else
					{
						List<YFCElement> orderHeaderList =new ArrayList<YFCElement>();
						orderHeaderList.add(documentElement);
						chainedOrderCountMap.put(chainedFromOrderLineKey, orderHeaderList);
					}
				}
				
				if (null != chainedOrderMap
						&& chainedOrderMap.size() > 0) {
					isFOCreated = true;
				}
			}
		} catch (CannotBuildInputException e) {
			LOG.error("Unable to get chained order lines." + e);
		} catch (XPathExpressionException e) {
			LOG.error("Unable to get chained order lines." + e);
		}
	}

	protected ArrayList<String> getOrderLineKeys() {
		ArrayList<String> orderLineKeyList = new ArrayList<String>();
		Document orderDoc = getOutputDocument();
		assert (orderDoc != null);
		NodeList orderLineNodeList = orderDoc.getElementsByTagName("OrderLine");
		for (int i = 0; i < orderLineNodeList.getLength(); i++) {
			Element orderLine = (Element) orderLineNodeList.item(i);
			SCXmlUtil.getString(orderLine);
			String orderLineKey = orderLine.getAttribute("OrderLineKey");
			orderLineKeyList.add(orderLineKey);
		}
		return orderLineKeyList;
	}

	/*
	 * getXpedxChainedOrderLineList takes arraylist of orderlinekeys and returns
	 * all the chained order lines for each of the input orderlines
	 */
	protected Document getXpedxChainedOrderLineList(
			ArrayList<String> chainedOrderFromLineKeylist)
			throws CannotBuildInputException {
		Document outputDoc = null;

		if (null == chainedOrderFromLineKeylist
				|| chainedOrderFromLineKeylist.isEmpty()) {
			LOG
					.debug("getXpedxChainedOrderLineList: Atleast one order line key is required. Use getOrderLineList API to get all the orderlines");
			return outputDoc;
		}
		Element input = WCMashupHelper.getMashupInput(
				"xpedxOrderDetailChainedOrderLineList", getWCContext()
						.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element) inputNodeList.item(0);
		for (int i = 0; i < chainedOrderFromLineKeylist.size(); i++) {
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			Element expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", "ChainedFromOrderLineKey");
			expElement
					.setAttribute("Value", chainedOrderFromLineKeylist.get(i));

			inputNodeListElemt.appendChild(inputDoc
					.importNode(expElement, true));
		}
		inputXml = SCXmlUtil.getString(input);
		LOG.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup(
				"xpedxOrderDetailChainedOrderLineList", input, getWCContext()
						.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}

		return outputDoc;
	}

	public boolean isDeliveryHold() {
		return deliveryHoldFlag;
	}

	public boolean isWillCall() {
		return willCallFlag;
	}

	public boolean isShipComplete() {
		return shipComplete;
	}

	/**
	 * @return the customerFieldsMap
	 */
	public LinkedHashMap<String, String> getCustomerFieldsMap() {
		return customerFieldsMap;
	}

	/**
	 * @param customerFieldsMap
	 *            the customerFieldsMap to set
	 */
	public void setCustomerFieldsMap(LinkedHashMap<String, String> customerFieldsMap) {
		this.customerFieldsMap = customerFieldsMap;
	}

	/**
	 * @return the chainedOrderMap
	 */
	public HashMap<String, ArrayList<String>> getChainedOrderMap() {
		return chainedOrderMap;
	}

	/**
	 * @param chainedOrderMap
	 *            the chainedOrderMap to set
	 */
	public void setChainedOrderMap(
			HashMap<String, ArrayList<String>> chainedOrderMap) {
		this.chainedOrderMap = chainedOrderMap;
	}

	public boolean isCustomerOrder(Element orderElement) {
		boolean isCustomerOrder = false;
		String orderType = orderElement.getAttribute("OrderType");
		SCXmlUtil.getString(orderElement);
		if (null != orderType && orderType.equals("Customer")) {
			isCustomerOrder = true;
		}
		return isCustomerOrder;
	}
	
	public static String getApproversUserName(String UserID) throws CannotBuildInputException, XPathExpressionException
	{
		if(YFCCommon.isVoid(UserID))
			return "";
		String FirstName;
		String LastName;
		String UserName = null;
		Document outputDoc1 = null;
		Map<String, String> valueMap1 = new HashMap<String, String>();
		valueMap1.put("/CustomerContact/@CustomerContactID", UserID);
		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		Element input1 = WCMashupHelper.getMashupInput(
				"xpedxItemListUserContactName", valueMap1, wcContext
						.getSCUIContext());
		Object obj1 = WCMashupHelper.invokeMashup(
				"xpedxItemListUserContactName", input1, wcContext
						.getSCUIContext());
		outputDoc1 = ((Element) obj1).getOwnerDocument();
		Element outputE2 = outputDoc1.getDocumentElement();
		Element CustomerContact = XMLUtilities.getElement(outputE2,	"CustomerContact");
		String customerContactId = SCXmlUtil.getAttribute(CustomerContact, "CustomerContactID");
		if(customerContactId == null ){
			return "";
		} 
		FirstName = SCXmlUtil.getAttribute(CustomerContact, "FirstName");
		LastName = SCXmlUtil.getAttribute(CustomerContact, "LastName");
		
		try {
			if(FirstName!=null || LastName!=null)
			{
					UserName = FirstName + " " + LastName;
			}
			else
				UserName = UserID;
		} catch (Exception e) {
			LOG.error("error in getting the approvers Username"+e.getMessage());
		}
		return UserName;
	}

	protected boolean deliveryHoldFlag = false;
	protected boolean willCallFlag = false;
	protected boolean shipComplete = false;
	protected boolean isFOCreated = false;
	protected boolean isCSRReview = false;
	protected boolean isFOCSRReview = false;
	protected String resetWithError = "N"; //added for XBT-109
	/*
	JIRA 3999 Start -Set and Get method for pending hold status and reson text
	*/
	
	protected String reasonText = "";
	protected String pendingHoldStatus = "";
	public int lineItemCount=0;
	public int getLineItemCount() {
		return lineItemCount;
	}
	public void setLineItemCount(int lineItemCount) {
		this.lineItemCount = lineItemCount;
	}
	public int getLineOtherCount() {
		return lineOtherCount;
	}
	public void setLineOtherCount(int lineOtherCount) {
		this.lineOtherCount = lineOtherCount;
	}

	public int lineOtherCount = 0;

	public boolean isFOCSRReview() {
		return isFOCSRReview;
	}

	public void setFOCSRReview(boolean isFOCSRReview) {
		this.isFOCSRReview = isFOCSRReview;
	}

	public String getPendingHoldStatus() {
		return pendingHoldStatus;
	}

	public void setPendingHoldStatus(String pendingHoldStatus) {
		this.pendingHoldStatus = pendingHoldStatus;
	}

	public String getReasonText() {
		return reasonText;
	}

	public void setReasonText(String reasonText) {
		this.reasonText = reasonText;
	}
	//start of XBT-109
	public String getResetWithError() {
		return resetWithError;
	}

	public void setResetWithError(String resetWithError) {
		this.resetWithError = resetWithError;
	}
	//end of XBT-109
	/*
	JIRA 3999 End -Set and Get method for pending hold status and reson text
	*/
	

	protected String userKey = "";
	protected String headerComment = "";
	private HashMap<String, HashMap<String,String>> skuMap;
	private String customerSku;
	protected LinkedHashMap<String, String> customerFieldsMap;
	protected HashMap<String, ArrayList<String>> chainedOrderMap;
	protected LinkedHashMap<String, List<YFCElement>> chainedOrderCountMap=new LinkedHashMap<String, List<YFCElement>>();
	protected HashMap<String, String> chainedFOMap=new HashMap<String, String>();
	private static final String ENC_USER_KEY = "ENC_USER_KEY";
	private static final Logger LOG = Logger
			.getLogger(XPEDXOrderDetailAction.class);
	protected ArrayList<String> enteredWebLineNumbers;
	private String custSuffix = "";
	private String invoiceURL = "";
	private String encInvoiceNo = "";
	private String encInvoiceDate = "";
	private String displayTaxAndShipHandlingAmt="N";	
private String shipFromDivision;
	

	public String getShipFromDivision() {
		return shipFromDivision;
	}

	public void setShipFromDivision(String shipFromDivision) {
		this.shipFromDivision = shipFromDivision;
	}
	
	public String getDisplayTaxAndShipHandlingAmt() {
		return displayTaxAndShipHandlingAmt;
	}

	public void setDisplayTaxAndShipHandlingAmt(String displayTaxAndShipHandlingAmt) {
		this.displayTaxAndShipHandlingAmt = displayTaxAndShipHandlingAmt;
	}

	public boolean isCSRReview() {
		return isCSRReview;
	}

	public void setCSRReview(boolean isCSRReview) {
		this.isCSRReview = isCSRReview;
	}

	public String getEncInvoiceNo() {
		return encInvoiceNo;
	}

	public void setEncInvoiceNo(String encInvoiceNo) {
		this.encInvoiceNo = encInvoiceNo;
	}

	public String getEncInvoiceDate() {
		return encInvoiceDate;
	}

	public void setEncInvoiceDate(String encInvoiceDate) {
		this.encInvoiceDate = encInvoiceDate;
	}

	public String getCustSuffix() {
		return custSuffix;
	}

	public void setCustSuffix(String custSuffix) {
		this.custSuffix = custSuffix;
	}

	public String getInvoiceURL() {
		return invoiceURL;
	}

	public void setInvoiceURL(String invoiceURL) {
		this.invoiceURL = invoiceURL;
	}

	/*set the errorMsg coming from either order cancel -> This page will be called from order cancel once the task is complete*/
	protected String errorMsg;
	
	public String getErrorMsg() {
		return errorMsg;
	}



	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	/**
	 * @return the skuMap
	 */
	public HashMap<String, HashMap<String, String>> getSkuMap() {
		return skuMap;
	}

	/**
	 * @param skuMap the skuMap to set
	 */
	public void setSkuMap(HashMap<String, HashMap<String, String>> skuMap) {
		this.skuMap = skuMap;
	}

	/**
	 * @return the customerSku
	 */
	public String getCustomerSku() {
		return customerSku;
	}

	/**
	 * @param customerSku the customerSku to set
	 */
	public void setCustomerSku(String customerSku) {
		this.customerSku = customerSku;
	}
	
	public LinkedHashMap<String, List<YFCElement>> getChainedOrderCountMap() {
		return chainedOrderCountMap;
	}

	public void setChainedOrderCountMap(
			LinkedHashMap<String, List<YFCElement>> chainedOrderCountMap) {
		this.chainedOrderCountMap = chainedOrderCountMap;
	}

	public boolean isFOCreated() {
		return isFOCreated;
	}

	public void setFOCreated(boolean isFOCreated) {
		this.isFOCreated = isFOCreated;
	}

	public String getHeaderComment() {
		return headerComment;
	}

	public void setHeaderComment(String headerComment) {
		this.headerComment = headerComment;
	}

	public HashMap<String, String> getChainedFOMap() {
		return chainedFOMap;
	}

	public void setChainedFOMap(HashMap<String, String> chainedFOMap) {
		this.chainedFOMap = chainedFOMap;
	}

	public String getCalculatedOrderedQuantityWithoutDecimal(Element orderLineElem){
		String calculatedOrderedQuantity = getCalculatedOrderedQuantity(orderLineElem);
		return calculatedOrderedQuantity.replace(".0", "");
	}
	public String getCalculatedOrderedQuantity(Element orderLineElem){
		String calculatedOrderedQuantity = "";
		if(orderLineElem!=null){
			double orderLineOrderedQty = Double.parseDouble(orderLineElem.getAttribute("OrderedQty"));
			double orderLineOriginalOrderedQty = Double.parseDouble(orderLineElem.getAttribute("OriginalOrderedQty"));
			double orderLineTranQuantityOrderedQty;
			Element orderLineTranQuantityElem = XmlUtils.getChildElement(orderLineElem, "OrderLineTranQuantity");
			if(orderLineTranQuantityElem!=null)
				orderLineTranQuantityOrderedQty = Double.parseDouble(orderLineTranQuantityElem.getAttribute("OrderedQty"));
			else 
				return "";
			//calculatedOrderedQuantity = ((orderLineOriginalOrderedQty * orderLineTranQuantityOrderedQty)/orderLineOrderedQty) + "";
			return orderLineTranQuantityOrderedQty + "";
		}
		return calculatedOrderedQuantity;
	}
	
	public String getMaxOrderStatusValue(){
		String maxOrderStatus = SCXmlUtil.getAttribute(getElementOrder(),"MaxOrderStatus");
		if (maxOrderStatus.equals("1100.5700") || // INVOICED
		    maxOrderStatus.equals("1100.5950") || // INVOICE ONLY
		    maxOrderStatus.equals("1100.5750"))   // RETURN
		{
			return "Y";			
		} else {
			return "N";
			
		}
	}	

}

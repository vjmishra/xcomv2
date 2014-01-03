package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.order.OrderSummaryUpdateAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXOrderSummaryUpdateAction extends OrderSummaryUpdateAction {

	private static final long serialVersionUID = -6062859669898581376L;
	private static final String EXTN_SOURCE_TYPE_WEB="3";
	private static final String CHANGE_ORDEROUTPUT_ORDER_UPDATE_SESSION_OBJ = "changeOrderAPIOutputForOU";
	private static final String SAVE_ORDER_SUMMARY_MASHUP = "XPEDXDraftOrderSummaryOnOrderPlace";
	private static final String EDIT_ORDER_SUMMARY_MASHUP = "XPEDXDraftOrderSummaryUpdateOnOrderPlace";
	private static final String APPROVE_ORDER_SUMMARY_MASHUP = "XPEDXDraftOrderSummaryUpdateOnApproveOrder";
	private static final String RUSH_ORDER="RUSH ORDER";
	private static final String REQUESTED_DELIVERY_DATE="REQUESTED DELIVERY DATE";
	private boolean isDraftOrder=false;
	@Override
	public String execute() {
		try {
			String messageType = getMessageType();
			setCustomerContactId(getWCContext().getCustomerContactId());
			if (!YFCCommon.isVoid(messageType)
					&& "OrderPlace".equalsIgnoreCase(messageType)) {
				// Fill up all the XPEDX related fields in the Order and Save it
				// by calling Change Order
				setXPEDXFields();
			}
			updateUserProfile();
			setYFSEnvironmentVariables();
			if (isDraftOrder())
				isDraftOrder=true;
			
			Element outElement = null;
			
			if(isDraftOrder){
				//outElement = prepareAndInvokeMashup(SAVE_ORDER_SUMMARY_MASHUP);
				Set<String> mashupList = new HashSet<String> ();
				/*<Order OrderHeaderKey="" ="" ReqDeliveryDate="" Override="Y" CustomerContactID="">
				<Extn ExtnDeliveryHoldFlag="" =""
					="" ="" =""
					="" ="" =""
					="" ="" ="" =""/>*/	
		        mashupList.add(SAVE_ORDER_SUMMARY_MASHUP);
				Map<String,Element> mashupInputs = prepareMashupInputs(mashupList);
				Element changeOrderInput=mashupInputs.get(SAVE_ORDER_SUMMARY_MASHUP);
				setOrderPlaceTransactionObjMap(changeOrderInput);
				setInventoryIndicatorMap();
				
			}	else {
				if(customerHoldCheck != null && "true".equals(customerHoldCheck) ){
					setExtnWebHoldFlag("Y");
				}
				
				String approveOrderFlag="false";
				Object approveOrderSessionVar=XPEDXWCUtils.getObjectFromCache(XPEDXConstants.APPROVE_ORDER_FLAG);
				if(approveOrderSessionVar!=null) {
					approveOrderFlag=approveOrderSessionVar.toString();
				}
				
				if("true".equals(approveOrderFlag)) {
					Set<String> mashupId=new HashSet<String>();
					mashupId.add(APPROVE_ORDER_SUMMARY_MASHUP);
					
					Map<String, Element> changeOrderInputObj=prepareMashupInputs(mashupId);
					Document changeOrderInputDoc = changeOrderInputObj.get(APPROVE_ORDER_SUMMARY_MASHUP).getOwnerDocument();
					Element changeOrderInputElem = changeOrderInputDoc.getDocumentElement();
					Element holdTypesElem = SCXmlUtil.getChildElement(changeOrderInputElem, "OrderHoldTypes");
					Element holdTypeElem = SCXmlUtil.getChildElement(holdTypesElem, "OrderHoldType");
					holdTypeElem.setAttribute("HoldType", "ORDER_LIMIT_APPROVAL");
					holdTypeElem.setAttribute("Status", "1300");
					holdTypeElem.setAttribute("ResolverUserId", getWCContext().getLoggedInUserId());
					
					Element changeOrderExtnElem = SCXmlUtil.getChildElement(changeOrderInputElem, "Extn");
					changeOrderExtnElem.setAttribute("ExtnOrderEditCustContactID", wcContext.getLoggedInUserId());
					changeOrderExtnElem.setAttribute("ExtnLastOrderOperation", "OrderApproved");
					changeOrderExtnElem.setAttribute("ExtnOrderConfirmationEmailSentFlag", "N");
					outElement = (Element) WCMashupHelper.invokeMashup(APPROVE_ORDER_SUMMARY_MASHUP, changeOrderInputElem, wcContext.getSCUIContext());
				
				} else {
					outElement = prepareAndInvokeMashup(EDIT_ORDER_SUMMARY_MASHUP);
					
				}				
				
				/*Begin - Changes made by Mitesh Parikh for JIRA#3594*/
				Document orderOutDoc = outElement.getOwnerDocument();
				getWCContext().getSCUIContext().getSession().setAttribute(CHANGE_ORDEROUTPUT_ORDER_UPDATE_SESSION_OBJ, orderOutDoc);
				/*End - Changes made by Mitesh Parikh for JIRA#3594*/
			}
			//callChangeOrder();
			updateCVVNumbers();
			setShipCompleteOption();
			XPEDXWCUtils.releaseEnv(wcContext);
		} catch (Exception e) {
			XPEDXWCUtils.logExceptionIntoCent(e.getMessage());
			XPEDXWCUtils.removeObectFromCache("ORDER_PLACE_CHNAGE_ORDER_DOC_MAP");
			XPEDXWCUtils.removeObectFromCache("INVENTORY_INDICATOR_MAP");
			LOG.error(e.getMessage(), e);
			return "error";
		}
		return "success";
	}
	private void setInventoryIndicatorMap()
	{
		Map<String,String> inventoryIndicator=new HashMap<String,String>();
		if(orderLineKeyLists != null)
		{
			for(int i=0;i<orderLineKeyLists.size();i++ )
			{
				String orderLineKey=(String)orderLineKeyLists.get(i);
				if(inventoryInds != null)
				{
					
					if(inventoryInds.contains(orderLineKeyLists.get(i)))
						inventoryIndicator.put(orderLineKey, "DIRECT");
					else
						inventoryIndicator.put(orderLineKey, "STOCK");
				}
				else
					inventoryIndicator.put(orderLineKey, "STOCK");
			}
		}
		XPEDXWCUtils.setObectInCache("INVENTORY_INDICATOR_MAP", inventoryIndicator);
	}
	private void setOrderPlaceTransactionObjMap(Element changeOrderInput )
	{

		Map orderPlaceTransactionObjMap=new HashMap();
		if(changeOrderInput != null)
		{
			orderPlaceTransactionObjMap.put("CustomerPONo", 
					changeOrderInput.getAttribute("CustomerPONo"));
			orderPlaceTransactionObjMap.put("ReqDeliveryDate", 
					changeOrderInput.getAttribute("ReqDeliveryDate"));
			orderPlaceTransactionObjMap.put("ShipNode", 
					changeOrderInput.getAttribute("ShipNode"));
			Element orderExtnElement=(Element)changeOrderInput.getElementsByTagName("Extn").item(0);
			if(orderExtnElement != null )
			{
				orderPlaceTransactionObjMap.put("ExtnDeliveryHoldFlag", 
						orderExtnElement.getAttribute("ExtnDeliveryHoldFlag"));
				orderPlaceTransactionObjMap.put("ExtnRushOrderComments", 
						orderExtnElement.getAttribute("ExtnRushOrderComments"));
				orderPlaceTransactionObjMap.put("ExtnShipComplete", 
						orderExtnElement.getAttribute("ExtnShipComplete"));
				orderPlaceTransactionObjMap.put("ExtnWillCall", 
						orderExtnElement.getAttribute("ExtnWillCall"));
				orderPlaceTransactionObjMap.put("ExtnDeliveryHoldDate", 
						orderExtnElement.getAttribute("ExtnDeliveryHoldDate"));
				orderPlaceTransactionObjMap.put("ExtnDeliveryHoldTime", 
						orderExtnElement.getAttribute("ExtnDeliveryHoldTime"));
				orderPlaceTransactionObjMap.put("ExtnAttentionName", 
						orderExtnElement.getAttribute("ExtnAttentionName"));
				orderPlaceTransactionObjMap.put("ExtnHeaderComments", SpecialInstructions);
				orderPlaceTransactionObjMap.put("ExtnAddnlEmailAddr", 
						orderExtnElement.getAttribute("ExtnAddnlEmailAddr"));
				orderPlaceTransactionObjMap.put("ExtnRushOrderFlag", 
						orderExtnElement.getAttribute("ExtnRushOrderFlag"));
				orderPlaceTransactionObjMap.put("ExtnWebHoldFlag", 
						orderExtnElement.getAttribute("ExtnWebHoldFlag"));
				orderPlaceTransactionObjMap.put("ExtnOrderedByName", 
						orderExtnElement.getAttribute("ExtnOrderedByName"));
			}
		}
		XPEDXWCUtils.setObectInCache("ORDER_PLACE_CHNAGE_ORDER_DOC_MAP", orderPlaceTransactionObjMap);
	}
	
	/*private void callChangeOrder()
	{
		try
		{
			
			Map<String,Element> orderLineMap=new HashMap<String,Element>();
			Map<String,Element> orderHeaderMap=new HashMap<String,Element>();
			HashMap<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/XPXUeAdditionalAttribsXml/@OrderHeaderKey", orderHeaderKey);
			valueMap.put("/XPXUeAdditionalAttribsXml/@XMLType", "ChangeOrder");
	
			Element input=null;
			try {
				input = WCMashupHelper.getMashupInput(
						"xpedx_get_pna_responsexml", valueMap, wcContext
								.getSCUIContext());
			} catch (CannotBuildInputException e) {
				// Error in invoking mashup
				//return custInfoMap;
			}
			Object obj = WCMashupHelper.invokeMashup(
					"xpedx_get_pna_responsexml", input, wcContext
							.getSCUIContext());
			if(obj != null)
			{
				Element changeOrderElem = ((Element) obj).getOwnerDocument().getDocumentElement();
				
				String changeOrderXML=changeOrderElem.getAttribute("ResponseXML");
				Document inputDocument=SCXmlUtil.createFromString(changeOrderXML);
				
				XPEDXWCUtils.setYFSEnvironmentVariables(wcContext);
				WCMashupHelper.invokeMashup("xpedx_draftOrderSummaryUpdateExtnFields", inputDocument.getDocumentElement(), wcContext.getSCUIContext());
			}
			
			
			
			//UtilBean util=new UtilBean();
			//Document inputDocument =SCXmlUtil.createDocument("Order");
			Element orderChangeElem=inputDocument.getDocumentElement();
			Element orderExtn=util.getElement(orderElem, "Extn");
			
			orderChangeElem.setAttribute("OrderHeaderKey", orderElem.getAttribute("OrderHeaderKey"));
			Element orderExtnChanegElem=SCXmlUtil.createChild(orderChangeElem, "Extn");
			orderExtnChanegElem.setAttribute("ExtnOrderCouponDiscount", orderExtn.getAttribute("ExtnOrderCouponDiscount"));
			orderExtnChanegElem.setAttribute("ExtnOrderDiscount", orderExtn.getAttribute("ExtnOrderDiscount"));
			orderExtnChanegElem.setAttribute("ExtnOrderSubTotal", orderExtn.getAttribute("ExtnOrderSubTotal"));
			orderExtnChanegElem.setAttribute("ExtnTotOrdValWithoutTaxes", orderExtn.getAttribute("ExtnTotOrdValWithoutTaxes"));
			orderExtnChanegElem.setAttribute("ExtnTotOrderAdjustments", orderExtn.getAttribute("ExtnTotOrderAdjustments"));
			orderExtnChanegElem.setAttribute("ExtnTotalOrderValue", orderExtn.getAttribute("ExtnTotalOrderValue"));
			
			Element orderLinesChanegElem=SCXmlUtil.createChild(orderChangeElem, "OrderLines");
			if("N".equals(orderElem.getAttribute("DraftOrderFlag")))
			{
				Element orderPendingChangeElem=SCXmlUtil.createChild(orderChangeElem, "PendingChanges");
				orderPendingChangeElem.setAttribute("RecordPendingChanges", "Y");
			}
			List<Element> orderLines=SCXmlUtil.getElements(orderElem, "/OrderLines/OrderLine");
			
			if(orderLines.size()>0)
			{
				for(Element orderLineEle : orderLines)
				{
					
					Element orderLineChanegElem=SCXmlUtil.createChild(orderLinesChanegElem, "OrderLine");
					orderLineChanegElem.setAttribute("Action", "MODIFY");
					orderLineChanegElem.setAttribute("OrderLineKey", orderLineEle.getAttribute("OrderLineKey"));
					
					Element orderLineExtnChange=SCXmlUtil.createChild(orderLineChanegElem, "Extn");
					Element orderLineExtn=util.getElement(orderLineEle, "Extn");
					orderLineExtnChange.setAttribute("ExtnAdjDollarAmt", orderLineExtn.getAttribute("ExtnAdjDollarAmt"));
					orderLineExtnChange.setAttribute("ExtnAdjUOMUnitPrice", orderLineExtn.getAttribute("ExtnAdjUOMUnitPrice"));
					orderLineExtnChange.setAttribute("ExtnExtendedPrice", orderLineExtn.getAttribute("ExtnExtendedPrice"));
					orderLineExtnChange.setAttribute("ExtnLineCouponDiscount", orderLineExtn.getAttribute("ExtnLineCouponDiscount"));
					orderLineExtnChange.setAttribute("ExtnLineOrderedTotal", orderLineExtn.getAttribute("ExtnLineOrderedTotal"));
					orderLineExtnChange.setAttribute("ExtnPricingUOM", orderLineExtn.getAttribute("ExtnPricingUOM"));
					orderLineExtnChange.setAttribute("ExtnReqUOMUnitPrice", orderLineExtn.getAttribute("ExtnReqUOMUnitPrice"));
					orderLineExtnChange.setAttribute("ExtnUnitPrice", orderLineExtn.getAttribute("ExtnUnitPrice"));
					orderLineExtnChange.setAttribute("ExtnUnitPriceDiscount", orderLineExtn.getAttribute("ExtnUnitPriceDiscount"));
					
					Element orderLineTranChange=SCXmlUtil.createChild(orderLineChanegElem, "Item");
					Element itemElem=util.getElement(orderLineEle, "Item");
					orderLineTranChange.setAttribute("ItemID", itemElem.getAttribute("ItemID"));
					orderLineTranChange.setAttribute("UnitOfMeasure",  itemElem.getAttribute("UnitOfMeasure"));
					
				}
				
			//}
		}
		catch(Exception e)
		{
			LOG.error("Error while calling change order to Update the price from PNA");
		}
	}*/
	private void setYFSEnvironmentVariables() {
		if (orderHeaderKey != null && !"".equals(orderHeaderKey))
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("CartOrderHeaderKey", orderHeaderKey);
			map.put("isPriceLocking", "true");
			//Commenting PnA call since pna is not required on Submit Order Action.
			//map.put("isPnACall", "true");
			map.put("isDiscountCalculate", "false");
			XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext(), map);
		}
	}
	
	@Override
	protected void manipulateMashupInputs(Map<String, Element> mashupInputs)
			throws CannotBuildInputException{
		
		Element input=null;
		if (isDraftOrder) {
			input=mashupInputs.get(SAVE_ORDER_SUMMARY_MASHUP);
		
		} else  
		{
			input=mashupInputs.get(EDIT_ORDER_SUMMARY_MASHUP);
		}
		
		if(input !=null) {
			if(SpecialInstructions!=null && SpecialInstructions.trim().length()>0) {
				Document inputDocument = input.getOwnerDocument();
				Element instructionsElem = inputDocument.createElement("Instructions");
				Element instructionElem = inputDocument.createElement("Instruction");
				instructionElem.setAttribute("InstructionText", SpecialInstructions);
				instructionElem.setAttribute("InstructionDetailKey",SpecialInstructionsKey);
				instructionElem.setAttribute("InstructionType", "HEADER");
				instructionsElem.appendChild(instructionElem);
				input.appendChild(instructionsElem);
			}
		}
		super.manipulateMashupInputs(mashupInputs);
	}
	private void updateUserProfile() throws Exception{
		if ((!this.isEmailAddrSaveNeeded() || YFCCommon.isVoid(getNewEmailAddr())) && (!this.isPoNumberSaveNeeded() || YFCCommon.isVoid(getNewPoNumber())))
			return;
		
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		String customerID = context.getCustomerId();
		String storeFrontId = context.getStorefrontId();
		String custContactId = context.getCustomerContactId();
		
		// get the customer's current additional email addresses
		Document custOutDoc = XPEDXWCUtils.getUserInfo(custContactId,storeFrontId).getOwnerDocument();
		YFCDocument custDoc = YFCDocument.getDocumentFor(custOutDoc);
		YFCElement custContactListEle = custDoc.getDocumentElement();
		YFCIterable<YFCElement> custContactsEle = custContactListEle.getChildren("CustomerContact");
		YFCElement custContactEle = null;
		String addnlEmailAddrs = null;
		String addnlPoList = null;
		String custContRefKey = null;
		Map<String, String> attributeMap = new HashMap<String, String>();
		boolean createCCExtn = false;
		for(;custContactsEle.hasNext();){
			custContactEle = (YFCElement)custContactsEle.next();
			String contactId = custContactEle.getAttribute("CustomerContactID");
			String customerId = custContactEle.getChildElement("Customer").getAttribute("CustomerID");
			if (!(contactId.equals(custContactId)&&customerId.equals(XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext)))){
				continue;
			}
			else{
				YFCElement custExtnEle = custContactEle.getChildElement("Extn");
				/*addnlEmailAddrs = custExtnEle.getAttribute("ExtnAddnlEmailAddrs");
				addnlPoList = custExtnEle.getAttribute("ExtnPOList");*/
				String customerContactId = wcContext.getCustomerContactId();
				Element xpxCustContExtnEle= XPEDXWCUtils.getXPXCustomerContactExtn(wcContext ,customerContactId);
				if(xpxCustContExtnEle == null)
					createCCExtn = true;
				else {
					custContRefKey = xpxCustContExtnEle.getAttribute("CustContRefKey");
					addnlEmailAddrs = xpxCustContExtnEle.getAttribute("AddnlEmailAddrs");
					addnlPoList = xpxCustContExtnEle.getAttribute("POList");
				}
				break;
			}
		}
		if (YFCCommon.isVoid(addnlEmailAddrs)){
			addnlEmailAddrs = "";
		}
		else{
			addnlEmailAddrs = addnlEmailAddrs.trim();
			char  lastChar = addnlEmailAddrs.charAt(addnlEmailAddrs.length()-1);
			if(lastChar != ','){
				addnlEmailAddrs = addnlEmailAddrs+",";
			}
		}
		if (YFCCommon.isVoid(addnlPoList)){
			addnlPoList = "";
		}
		// Copy the additional email addresses & po List
		StringBuffer extnAddnlEmailAddrs = new StringBuffer();
		StringBuffer extnPoList = new StringBuffer();
		if (!YFCCommon.isVoid(getNewEmailAddr()) && this.isEmailAddrSaveNeeded()){
			addnlEmailAddrs = addnlEmailAddrs + getNewEmailAddr();
			String[] splitAddrs = addnlEmailAddrs.split(",");
			Set<String> addrs= new HashSet<String>();
			for (int i =0; i < splitAddrs.length; i++){
				addrs.add(splitAddrs[i].trim());
			}
			
			for (String addr: addrs){
				extnAddnlEmailAddrs.append(addr.trim() + ",");
			}
			
			attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_ADDLN_EMAIL_ATTR, extnAddnlEmailAddrs.toString());
			XPEDXWCUtils.setObectInCache(XPEDXConstants.XPX_CUSTCONTACT_EXTN_ADDLN_EMAIL_ATTR, extnAddnlEmailAddrs.toString());
			//valueMap.put("/Customer/CustomerContactList/CustomerContact/Extn/@ExtnAddnlEmailAddrs",extnAddnlEmailAddrs.toString());
		}
		if(custContRefKey!=null && custContRefKey.length()>0)
			attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_REF_ATTR, custContRefKey);
		
		if (!YFCCommon.isVoid(getNewPoNumber()) && this.isPoNumberSaveNeeded()){
			addnlPoList = addnlPoList + getNewPoNumber();
			String[] splitPOs = addnlPoList.split(",");
			//Replacing session object PO List - to have PO's added in the session - JIRA 3645
			getWCContext().setWCAttribute("addnlPOList", addnlPoList, WCAttributeScope.LOCAL_SESSION);
			Set<String> pos= new HashSet<String>();
			for (int i =0; i < splitPOs.length; i++){
				pos.add(splitPOs[i].trim());
			}
			
			for (String po: pos){
				extnPoList.append(po.trim() + ",");
			}
			attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_PO_LIST_ATTR, extnPoList.toString());
			//valueMap.put("/Customer/CustomerContactList/CustomerContact/Extn/@ExtnPOList",extnPoList.toString());
		}
			
			/*valueMap.put("/Customer/@CustomerID", XPEDXWCUtils.getLoggedInCustomerFromSession(context));
			valueMap.put("/Customer/@OrganizationCode", context.getStorefrontId());
			valueMap.put("/Customer/CustomerContactList/CustomerContact/@CustomerContactID", context.getCustomerContactId());
			Element input = WCMashupHelper.getMashupInput("ManageXpedxContactEmailAddrs", valueMap, wcContext.getSCUIContext());
			Element outDoc = (Element)WCMashupHelper.invokeMashup("ManageXpedxContactEmailAddrs",input , getWCContext().getSCUIContext());*/
		
			Element outDoc = (Element)XPEDXWCUtils.updateXPXCustomerContactExtn(context,context.getCustomerContactId(), createCCExtn, attributeMap);
			
		
	}
	
	/**
	 * JIRA - 243, changed the mashup to get only some of the Extn fields
	 * xpedx-customer-getCustomExtnFieldsInformation
	 * @throws Exception
	 */
	protected void setShipCompleteOption() throws Exception {

		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		XPEDXShipToCustomer	shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		/*String customerID = context.getCustomerId();
		String storeFrontId = context.getStorefrontId();
		Document CustDetails = XPEDXWCUtils.getCustomerDetails(customerID,storeFrontId, "xpedx-customer-getCustomExtnFieldsInformation");*/
		shipCompleteOption = shipToCustomer.getExtnShipComplete();
			/*SCXmlUtil.getXpathAttribute(CustDetails
				.getDocumentElement(), "/Customer/Extn/@ExtnShipComplete")*/;
	}
	
	private Document getCustomerDetails() throws Exception{
		if (!YFCCommon.isVoid(getCustomerDetailDoc())){
			return getCustomerDetailDoc();
		}
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		String customerID = context.getCustomerId();
		String storeFrontId = context.getStorefrontId();
		this.customerDetailDoc = XPEDXWCUtils.getCustomerDetails(customerID,storeFrontId);
		return getCustomerDetailDoc(); 
	}

	public String getShipCompleteOption() {
		return shipCompleteOption;
	}
	protected void setXPEDXFields() throws Exception {
		try {			
			
			//Boolean isSalesRep = (Boolean) getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP");
			Boolean isSalesRep = null;
			String isSalesRepStr = (String) getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP");
			if(isSalesRepStr != null){
			isSalesRep = Boolean.valueOf(isSalesRepStr);
			}

			//Document custProfileDoc = //getCustomerDetails();
			/*Element custExtn = SCXmlUtil.getChildElement(custProfileDoc
					.getDocumentElement(), "Extn");*/
			XPEDXShipToCustomer	shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			//SCXmlUtil.getString(custExtn);
			setExtnCompanyId(shipToCustomer.getExtnCompanyCode()/*custExtn.getAttribute("ExtnCompanyCode")*/);
			setExtnCustomerNo(shipToCustomer.getExtnLegacyCustNumber()/*custExtn.getAttribute("ExtnLegacyCustNumber")*/);
			setExtnEnvtId(shipToCustomer.getExtnEnvironmentCode()/*custExtn.getAttribute("ExtnEnvironmentCode")*/);
			String shipFromBranch=shipToCustomer.getExtnShipFromBranch();
			String extnShipFromDivision=!YFCCommon.isVoid(shipFromBranch) ? shipFromBranch: shipToCustomer.getExtnCustOrderBranch();
			String envCode = shipToCustomer.getExtnEnvironmentCode();//SCXmlUtil.getAttribute(custExtn,"ExtnEnvironmentCode");
			if (!(envCode != null && envCode.trim().length() > 0))
				envCode =shipToCustomer.getExtnOrigEnvironmentCode(); //SCXmlUtil.getAttribute(custExtn,"ExtnOrigEnvironmentCode");
			if (!(envCode != null && envCode.trim().length() > 0))
				LOG.error("Environment Code not defined for the Customer "
						+ wcContext.getCustomerId());
			setExtnShipFromDiv(extnShipFromDivision + "_" + envCode);			
			setExtnShipToName(shipToCustomer.getExtnCustomerName()/*custExtn.getAttribute("ExtnCustomerName")*/);
			setExtnSourceType(EXTN_SOURCE_TYPE_WEB);
			setExtnBillToName(shipToCustomer.getExtnCustomerName()/*custExtn.getAttribute("ExtnCustomerName")*/);
			setExtnOrderDivision(!YFCCommon.isVoid(shipFromBranch/*custExtn.getAttribute("ExtnShipFromBranch")*/)
					? shipFromBranch /*custExtn.getAttribute("ExtnShipFromBranch")*/+"_"+envCode : 
						shipToCustomer.getExtnCustOrderBranch()/*custExtn.getAttribute("ExtnCustOrderBranch")*/+"_"+envCode);
			setExtnCustomerDivision(shipToCustomer.getExtnCustomerDivision()/*custExtn.getAttribute("ExtnCustomerDivision")*/+"_"+envCode);
			XPEDXCustomerContactInfoBean custContBean=(XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache("XPEDX_Customer_Contact_Info_Bean");
			//Added to sent the user name for sales rep as an input
			if(isSalesRep!=null && isSalesRep){
				String salesRepUserName = (String)getWCContext().getSCUIContext().getSession().getAttribute("loggedInUserName");
				setOrderedByName(salesRepUserName);
			}
			else{
			setOrderedByName(custContBean.getFirstName()+" "+custContBean.getLastName());
			}
			// OP Task 88
			if("true".equals(getRushOrdrFlag())){
				setExtnWebHoldFlag("Y");
				setRushOrdrFlag("Y");
			}else if("false".equals(getRushOrdrFlag()) ||
					YFCUtils.isVoid(getRushOrdrFlag())){
				setExtnWebHoldFlag("N");
				setRushOrdrFlag("N");
			}
			/* Code changes for JIRA 246
			 * If customer selects either of the following shipping options on the Checkout page - append a message to the Header Comment Field. 
			 * Rush Order, Charges may apply, MUST add delivery info. in comments - append ‘RUSH ORDER’
			 * Requested delivery Date, MUST add delivery date in Comments for deliveries outside your normal schedule. - append ‘REQUESTED DELIVERY DATE’
			 * if customer select both above options - append 'RUSH ORDER, REQUESTED DELIVERY DATE'
			 */
			if(SpecialInstructions!=null && SpecialInstructions.trim().length()>0) {
				StringBuilder instructions = new StringBuilder(SpecialInstructions);
				if("Y".equals(getRushOrdrFlag())){
					instructions.append(" "+RUSH_ORDER);
					/*Commented for EB 1975 if("true".equals(getRushOrdrDateFlag())){
						instructions.append(", "+REQUESTED_DELIVERY_DATE);
					}
				}else{
					if("true".equals(getRushOrdrDateFlag())){
						instructions.append(" "+REQUESTED_DELIVERY_DATE);
					}*/
				}
				setSpecialInstructions(instructions.toString());
			}
			// webtrend code starts
			HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
	        HttpSession localSession = httpRequest.getSession();	        
	        localSession.setAttribute("rushOrdrFlag",rushOrdrFlag);
	        // webtrend code End
			// OP Task 66
			//The below code is commented because draftShipComplete is being used to set the ExtnShipComplete flag for the order
			/*if("true".equals(getShipComplete()) 
					|| "Y".equals(getShipComplete()) 
						|| "C".equals(getShipComplete())){
				setShipComplete("C");
			}else if("false".equals(getShipComplete()) 
					|| "N".equals(getShipComplete())){
				setShipComplete("N");
			}else{
				setShipComplete("Y");
			}*/
			
			setXPEDXOrderLineFields();
		} catch (CannotBuildInputException e) {
			LOG.error("Cannot fetch customer profile detail. " + e);
			throw new CannotBuildInputException(
					"Cannot fetch customer profile detail. " + e);
		}
	}

	protected void setXPEDXOrderLineFields() throws Exception {
		/*Document orderDoc = getXPEDXOrderDetailsForOrderPlace(
				getOrderHeaderKey(), getWCContext());
		if (null == orderDoc) {
			LOG.error("Error getting thr order details for "
					+ getOrderHeaderKey());
			throw new Exception("Error getting thr order details for "
					+ getOrderHeaderKey());
		}
		NodeList orderLinesNodeList = orderDoc
				.getElementsByTagName("OrderLine");*/
		if (null == orderLineKeyLists) {
			LOG.error("setXPEDXOrderLineFields(): Zero Items in the cart for "
					+ getOrderHeaderKey());
			return;
		}
		int noOfOrderLines = orderLineKeyLists.size();
		for (int i = 0; i < noOfOrderLines; i++) {
			orderLineKeyList.add(orderLineKeyLists.get(i).toString());
			orderLineShipNodeList.add(getExtnShipFromDiv());
		}
	}

	public static Document getXPEDXOrderDetailsForOrderPlace(
			String orderHeaderKey, IWCContext wcContext)
			throws CannotBuildInputException {
		Document outputDoc = null;

		if (YFCCommon.isVoid(orderHeaderKey)) {
			LOG
					.debug("getXPEDXOrderDetailsForOrderPlace: Order header key is a required field. Returning a empty document");
			return outputDoc;
		}

		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Order/@OrderHeaderKey", orderHeaderKey);

		Element input = WCMashupHelper.getMashupInput(
				"XPEDXGetOrderDetailsForOrderPlace", valueMap, wcContext
						.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		LOG.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup(
				"XPEDXGetOrderDetailsForOrderPlace", input, wcContext
						.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}

		return outputDoc;
	}

	public String getDeliveryHoldDate() {
		deliveryHoldDate = getUtilBean().deFormatDate(deliveryHoldDate,
				getWCContext());
		return deliveryHoldDate;
	}

	public void setDeliveryHoldDate(String delHoldDt) {
		deliveryHoldDate = delHoldDt;
	}

	public String getDeliveryHoldFlag() {
		return deliveryHoldFlag;
	}

	public void setDeliveryHoldFlag(String delHoldFlag) {
		if ("true".equals(delHoldFlag))
			deliveryHoldFlag = "Y";
	}

	public String getWillCallFlag() {
		return willCallFlag;
	}

	public void setWillCallFlag(String wcFlag) {
		if ("true".equals(wcFlag))
			willCallFlag = "Y";
	}

	public String getShipComplete() {
		return shipComplete;
	}

	public void setShipComplete(String sCmpl) {
		if ("true".equals(sCmpl))
			shipComplete = "Y";
		else 
			shipComplete = sCmpl;
	}
	
	
	public String getDraftShipComplete() {
		return draftShipComplete;
	}

	public void setDraftShipComplete(String draftShipComplete) {
		this.draftShipComplete = draftShipComplete;
	}

	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return the extnBillToName
	 */
	public String getExtnBillToName() {
		return extnBillToName;
	}

	/**
	 * @param extnBillToName
	 *            the extnBillToName to set
	 */
	public void setExtnBillToName(String extnBillToName) {
		this.extnBillToName = extnBillToName;
	}

	/**
	 * @return the extnCompanyId
	 */
	public String getExtnCompanyId() {
		return extnCompanyId;
	}

	/**
	 * @param extnCompanyId
	 *            the extnCompanyId to set
	 */
	public void setExtnCompanyId(String extnCompanyId) {
		this.extnCompanyId = extnCompanyId;
	}

	/**
	 * @return the extnCustomerDivision
	 */
	public String getExtnCustomerDivision() {
		return extnCustomerDivision;
	}

	/**
	 * @param extnCustomerDivision
	 *            the extnCustomerDivision to set
	 */
	public void setExtnCustomerDivision(String extnCustomerDivision) {
		this.extnCustomerDivision = extnCustomerDivision;
	}

	/**
	 * @return the extnCustomerNo
	 */
	public String getExtnCustomerNo() {
		return extnCustomerNo;
	}

	/**
	 * @param extnCustomerNo
	 *            the extnCustomerNo to set
	 */
	public void setExtnCustomerNo(String extnCustomerNo) {
		this.extnCustomerNo = extnCustomerNo;
	}

	/**
	 * @return the extnEnvtId
	 */
	public String getExtnEnvtId() {
		return extnEnvtId;
	}

	/**
	 * @param extnEnvtId
	 *            the extnEnvtId to set
	 */
	public void setExtnEnvtId(String extnEnvtId) {
		this.extnEnvtId = extnEnvtId;
	}

	/**
	 * @return the extnOrderDivision
	 */
	public String getExtnOrderDivision() {
		return extnOrderDivision;
	}

	/**
	 * @param extnOrderDivision
	 *            the extnOrderDivision to set
	 */
	public void setExtnOrderDivision(String extnOrderDivision) {
		this.extnOrderDivision = extnOrderDivision;
	}

	/**
	 * @return the extnShipFromDiv
	 */
	public String getExtnShipFromDiv() {
		return extnShipFromDiv;
	}

	/**
	 * @param extnShipFromDiv
	 *            the extnShipFromDiv to set
	 */
	public void setExtnShipFromDiv(String extnShipFromDiv) {
		this.extnShipFromDiv = extnShipFromDiv;
	}

	/**
	 * @return the extnShipToName
	 */
	public String getExtnShipToName() {
		return extnShipToName;
	}

	/**
	 * @param extnShipToName
	 *            the extnShipToName to set
	 */
	public void setExtnShipToName(String extnShipToName) {
		this.extnShipToName = extnShipToName;
	}

	/**
	 * @return the extnSourceType
	 */
	public String getExtnSourceType() {
		return extnSourceType;
	}

	/**
	 * @param extnSourceType
	 *            the extnSourceType to set
	 */
	public void setExtnSourceType(String extnSourceType) {
		this.extnSourceType = extnSourceType;
	}

	/**
	 * @return the orderHeaderKey
	 */
	public String getOrderHeaderKey() {
		return orderHeaderKey;
	}

	/**
	 * @param orderHeaderKey
	 *            the orderHeaderKey to set
	 */
	public void setOrderHeaderKey(String orderHeaderKey) {
		this.orderHeaderKey = orderHeaderKey;
	}

	/**
	 * @return the orderLineKeyList
	 */
	public ArrayList<String> getOrderLineKeyList() {
		return orderLineKeyList;
	}

	/**
	 * @param orderLineKeyList
	 *            the orderLineKeyList to set
	 */
	public void setOrderLineKeyList(ArrayList<String> orderLineKeyList) {
		this.orderLineKeyList = orderLineKeyList;
	}

	/**
	 * @return the orderLineShipNodeList
	 */
	public ArrayList<String> getOrderLineShipNodeList() {
		return orderLineShipNodeList;
	}

	/**
	 * @param orderLineShipNodeList
	 *            the orderLineShipNodeList to set
	 */
	public void setOrderLineShipNodeList(ArrayList<String> orderLineShipNodeList) {
		this.orderLineShipNodeList = orderLineShipNodeList;
	}


	protected String deliveryHoldFlag = "N";
	protected String deliveryHoldDate = "";
	protected String willCallFlag = "N";
	protected String shipComplete = "N";
	protected String draftShipComplete = "N";
	private static final Logger LOG = Logger
			.getLogger(XPEDXOrderSummaryUpdateAction.class);
	private String messageType = "";
	private String extnEnvtId = "";
	private String extnCompanyId = "";
	private String extnOrderDivision = "";
	private String extnShipFromDiv = "";
	private String extnCustomerDivision = "";
	private String extnCustomerNo = "";
	private String extnShipToName = "";
	private String extnBillToName = "";
	private String extnSourceType = "";
	private String orderHeaderKey = "";
	private ArrayList<String> orderLineKeyList = new ArrayList<String>();
	private ArrayList<String> orderLineShipNodeList = new ArrayList<String>();
	protected String shipCompleteOption;
	private String emailAddresses; // ';' separated email list for order header
	private String newEmailAddr; // ',' separated email list for user' customer contact addnlEmailAddrs field 
	private Set<String> addnlEmailAddrList; // the list that gives the selection option
	private boolean emailAddrSaveNeeded; // the checkbox value to check if the user customer contact need to be updated
	private String customerId;
	private String customerContactId;
	private String orgCode;
	private Document customerDetailDoc;
	private Document userInfoDoc;
	private boolean poNumberSaveNeeded;// the checkbox value to check if the customer po number need to be updated
	//private String customerPoNumbers; // ';' separated po list for order header
	private String newPoNumber; // ',' separated po number list for user' customer contact addnlPoNumberList field 
	private Set<String> addnlPoNumberList; // the list that gives the selection option
	private String SpecialInstructions;
	private String SpecialInstructionsKey;
	private ArrayList orderLineKeyLists;
	private String customerPoNumber; // selected customer PO for order
	private String extnWebHoldFlag;
	private String rushOrdrFlag="N";
	private String orderedByName;
	private ArrayList<String> inventoryInds;
	private String customerHoldCheck;
	private String rushOrdrDateFlag = "false";
	
	public String getRushOrdrDateFlag() {
		return rushOrdrDateFlag;
	}
	public void setRushOrdrDateFlag(String rushOrdrDateFlag) {
		this.rushOrdrDateFlag = rushOrdrDateFlag;
	}
	public String getCustomerHoldCheck() {
		return customerHoldCheck;
	}
	public void setCustomerHoldCheck(String customerHoldCheck) {
		this.customerHoldCheck = customerHoldCheck;
	}
	public ArrayList getOrderLineKeyLists() {
		return orderLineKeyLists;
	}

	public void setOrderLineKeyLists(ArrayList orderLineKeyLists) {
		this.orderLineKeyLists = orderLineKeyLists;
	}
	
	
	/**
	 * @return the customerPoNumber
	 */
	 public String getCustomerPoNumber() {
		if (!YFCCommon.isVoid(this.newPoNumber)){
			String newPoNumber = this.newPoNumber;
			setAddnlPoNumberList(newPoNumber);
			
		}
		
		StringBuffer poNumBuf = new StringBuffer();
		for(String poNumber : this.addnlPoNumberList){
			if (!YFCCommon.isVoid(poNumber) && !poNumber.equalsIgnoreCase("-1")){
				poNumBuf.append(poNumber);
				
			}
		}
		customerPoNumber = poNumBuf.toString();
		return customerPoNumber;
	}
	 
	/**
	 * @param customerPoNumber the customerPoNumber to set
	 */
	public void setCustomerPoNumber(String customerPoNumber) {
		this.customerPoNumber = customerPoNumber;
	}

	public String getNewPoNumber() {
		return newPoNumber;
	}

	public void setNewPoNumber(String newPoNumber) {
		this.newPoNumber = newPoNumber;
	}

	public List<String> getAddnlPoNumberList() {
		return new ArrayList<String>(this.addnlPoNumberList);
	}

	public void setAddnlPoNumberList(List<String> addnlPoNumberList) {
		this.addnlPoNumberList = new HashSet<String>(addnlPoNumberList);
	}
	
	public void setAddnlPoNumberList(String addnlPoNumber) {
		if (YFCCommon.isVoid(this.addnlPoNumberList)){
			this.addnlPoNumberList = new HashSet<String>();
		}
		this.addnlPoNumberList.add(addnlPoNumber);
	}
	

	
	public boolean isPoNumberSaveNeeded() {
		return poNumberSaveNeeded;
	}
	
	public void setPoNumberSaveNeeded(String poNumberSaveNeeded) {
		if (YFCCommon.isVoid(poNumberSaveNeeded)){
			this.poNumberSaveNeeded = false;
			return;
		}
		if (poNumberSaveNeeded.trim().equalsIgnoreCase("TRUE") || poNumberSaveNeeded.trim().equalsIgnoreCase("ON")){
			this.poNumberSaveNeeded = true;
			return;
		}
		this.poNumberSaveNeeded = false;
	}

	/**
	 * @return
	 */
	public Document getUserInfoDoc() {
		return userInfoDoc;
	}

	/**
	 * @param userInfoDoc
	 */
	public void setUserInfoDoc(Document userInfoDoc) {
		this.userInfoDoc = userInfoDoc;
	}

	/**
	 * @return
	 */
	public Document getCustomerDetailDoc() {
		return customerDetailDoc;
	}

	/**
	 * @param customerDetailDoc
	 */
	public void setCustomerDetailDoc(Document customerDetailDoc) {
		this.customerDetailDoc = customerDetailDoc;
	}

	/**
	 * @return
	 */
	public List<String> getAddnlEmailAddrList() {
		return new ArrayList<String>(this.addnlEmailAddrList);
	}

	/**
	 * @param addnlEmailAddrList
	 */
	public void setAddnlEmailAddrList(List<String> addnlEmailAddrList) {
		this.addnlEmailAddrList = new HashSet<String>(addnlEmailAddrList);
	}
	
	/**
	 * @param addnlEmailAddrList
	 */
	public void setAddnlEmailAddrList(String addnlEmailAddr) {
		if (YFCCommon.isVoid(this.addnlEmailAddrList)){
			this.addnlEmailAddrList = new HashSet<String>();
		}
		this.addnlEmailAddrList.add(addnlEmailAddr);
	}
	/**
	 * Getter for the ';' separated email addresses stored in order header table
	 * @return String of the email addresses
	 */
	public String getEmailAddresses() {
			if (!YFCCommon.isVoid(this.newEmailAddr)){
				String[] emailListSplit = this.newEmailAddr.replace(" ","").split(",");
				for (int i = 0; i < emailListSplit.length; i++) {
					if (!YFCCommon.isVoid(emailListSplit[i])){
						setAddnlEmailAddrList(emailListSplit[i]);
					}
				}		
			}
		StringBuffer emailBuf = new StringBuffer();
		for(String email : this.addnlEmailAddrList){
			if (!YFCCommon.isVoid(email)){
				emailBuf.append(email);
				emailBuf.append(";");
			}
		}
		emailAddresses = emailBuf.toString().replace(",", ";");
		return emailAddresses;

	}
	
	/**
	 * @param emailAddresses
	 */
	public void setEmailAddresses(String emailAddresses) {
		this.emailAddresses = emailAddresses;
	}
	
	/** 
	 * Getter for the ',' separated email addresses stored in user's customer contact table
	 * @return String of the email addresses
	 */
	public String getNewEmailAddr() {
		return newEmailAddr;
	}
	
	/**
	 * @param newEmailAddr
	 */
	public void setNewEmailAddr(String newEmailAddr) {
		this.newEmailAddr = newEmailAddr;
	}
	
	/**
	 * @return
	 */
	public boolean isEmailAddrSaveNeeded() {
		return emailAddrSaveNeeded;
	}
	
	/**
	 * @param needEmailAddrSaved
	 */
	public void setEmailAddrSaveNeeded(String emailAddrSaveNeeded) {
		if (YFCCommon.isVoid(emailAddrSaveNeeded)){
			this.emailAddrSaveNeeded = false;
			return;
		}
		if (emailAddrSaveNeeded.trim().equalsIgnoreCase("TRUE") || emailAddrSaveNeeded.trim().equalsIgnoreCase("ON")){
			this.emailAddrSaveNeeded = true;
			return;
		}
		this.emailAddrSaveNeeded = false;
	}
	
	/**
	 * @param shipCompleteOption
	 */
	public void setShipCompleteOption(String shipCompleteOption) {
		this.shipCompleteOption = shipCompleteOption;
	}
	
	/**
	 * @param customerId
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	/**
	 * @return
	 */
	public String getCustomerId() {
		return customerId;
	}
	
	/**
	 * @param orgCode
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	/**
	 * @return
	 */
	public String getOrgCode() {
		return orgCode;
	}
	
	/**
	 * @return
	 */
	public String getCustomerContactId() {
		return customerContactId;
	}

	/**
	 * @param customerContactId
	 */
	public void setCustomerContactId(String customerContactId) {
		this.customerContactId = customerContactId;
	}

	public String getSpecialInstructions() {
		return SpecialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		SpecialInstructions = specialInstructions;
	}

	public String getSpecialInstructionsKey() {
		return SpecialInstructionsKey;
	}

	public void setSpecialInstructionsKey(String specialInstructionsKey) {
		SpecialInstructionsKey = specialInstructionsKey;
	}
	/**
	 * @return the extnWebHoldFlag
	 */
	public String getExtnWebHoldFlag() {
		return extnWebHoldFlag;
	}

	/**
	 * @param extnWebHoldFlag the extnWebHoldFlag to set
	 */
	public void setExtnWebHoldFlag(String extnWebHoldFlag) {
		this.extnWebHoldFlag = extnWebHoldFlag;
	}

	/**
	 * @return the rushOrdrFlag
	 */
	public String getRushOrdrFlag() {
		return rushOrdrFlag;
	}

	/**
	 * @param rushOrdrFlag the rushOrdrFlag to set
	 */
	public void setRushOrdrFlag(String rushOrdrFlag) {
		this.rushOrdrFlag = rushOrdrFlag;
	}

	public String getOrderedByName() {
		return orderedByName;
	}

	public void setOrderedByName(String orderedByName) {
		this.orderedByName = orderedByName;
	}

	public ArrayList<String> getInventoryInds() {
		return inventoryInds;
	}

	public void setInventoryInds(ArrayList<String> inventoryInds) {
		this.inventoryInds = inventoryInds;
	}

}

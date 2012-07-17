package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.xml.xpath.XPathExpressionException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.order.DraftOrderSummaryAction;
import com.sterlingcommerce.webchannel.order.OrderConstants;
import com.sterlingcommerce.webchannel.order.utilities.CreditCardCVVStorageHelper;
import com.sterlingcommerce.webchannel.utilities.BusinessRuleUtil;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXPaymentMethodHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXAlphanumericSorting;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItem;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItemPricingInfo;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceAndAvailability;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfc.util.YFCException;

public class XPEDXDraftOrderSummaryAction extends DraftOrderSummaryAction {

	private static final String CHANGE_ORDEROUTPUT_CHECKOUT_SESSION_OBJ = "changeOrderAPIOutputForCheckout";
	private static final long serialVersionUID = -2966568751589581889L;
	private XPEDXPaymentMethodHelper xPEDXPaymentMethodHelper = null;
    public static final String MASHUP_DOD_VALIDATE_ITEM_FOR_ORDERING = "me_dod_validateItemForOrdering";
    public static final String MASHUP_ITEM_ENTITLEMENT_CHECK ="xpedx_CheckOrderedItemsEntitlements";
    public static final String ERROR_MSG_SEPARATOR = " ";
    private HashMap<String, String> invalidOrderLinesMap;
    private ArrayList<String> ItemIDs = new ArrayList<String>();
    private ArrayList<String> entitledItems = new ArrayList<String>();
    private boolean isItemEntitlmentsChanged = false;
    private Document shipToCusotmerDocument;
    protected HashMap<String, String> inventoryIndicatorMap = new HashMap<String, String>();
    protected String orderBillToID = "";
	protected String orderShipToID = "";
	protected String custmerPONumber = "";
	private boolean rushOrderFlag;
	String ajaxLineStatusCodeMsg = "";
	String ajaxDisplayStatusCodeMsg = "";
	protected String isEditOrder;
	protected String customerFieldsValidated;
	protected String isCustomerPOMandatory="false";
	String lastModifiedUserId = "";
	XPEDXShipToCustomer shipToCustomer;
	private Document shipFromDoc;
	
	public Document getShipFromDoc() {
		return shipFromDoc;
	}

	public void setShipFromDoc(Document shipFromDoc) {
		this.shipFromDoc = shipFromDoc;
	}

	//added for 2769
	protected YFCDate lastModifiedDate = new YFCDate();
	protected String lastModifiedDateString = "";
	//added for jira 2885
	private  Map<String,String> pnALineErrorMessage=new HashMap<String,String>(); 
	
	public Map<String, String> getPnALineErrorMessage() {
		return pnALineErrorMessage;
	}

	public void setPnALineErrorMessage(Map<String, String> pnALineErrorMessage) {
		this.pnALineErrorMessage = pnALineErrorMessage;
	}
	//end of jira 2885

	public String getLastModifiedUserId() {
		return lastModifiedUserId;
	}

	public void setLastModifiedUserId(String lastModifiedUserId) {
		this.lastModifiedUserId = lastModifiedUserId;
	}

	public XPEDXDraftOrderSummaryAction() {
		deliveryHoldFlag = false;
		willCallFlag = false;
		rushOrderFlag = false;
		shipCompleteOption = "N";// default option is N - Kill & Fill ??
		isBOMValidationEnabled= null;
        invalidOrderLinesMap = new HashMap<String, String>();
        customerFieldsValidated = "N";
	}

	protected String getOrderDetailsMashupName() {
		return "xpedx_draftOrderSummary";
	}

	public String getAjaxLineStatusCodeMsg() {
		return ajaxLineStatusCodeMsg;
	}

	public void setAjaxLineStatusCodeMsg(String ajaxLineStatusCodeMsg) {
		this.ajaxLineStatusCodeMsg = ajaxLineStatusCodeMsg;
	}
	
	public String execute() {
		long startTime=System.currentTimeMillis();
		XPEDXWCUtils xpedxwcUtils = new XPEDXWCUtils();
		XPEDXOrderUtils orderUtils = new XPEDXOrderUtils();
		/* Begin - Changes made by Mitesh Parikh for 2422 JIRA */
		setItemDtlBackPageURL((wcContext.getSCUIContext().getRequest().getRequestURL().append("?").append(wcContext.getSCUIContext().getRequest().getQueryString())).toString());
		/* End - Changes made by Mitesh Parikh for 2422 JIRA */
		//Commented for JIRA 2909
		/*try {
			boolean committedDateProcessingSuccessful = processCommittedDatesIfRequired();
			if (!committedDateProcessingSuccessful)
				return "committedDateBindingFailure";
		} catch (DraftOrderFlagMismatchException dofme) {
			handleValidityCheckExceptions(dofme, LOG);
			return "DraftOrderFlagMismatchError";
		} catch (AuthorizedClientMismatchException acme) {
			handleValidityCheckExceptions(acme, LOG);
			return "AuthorizedClientMismatchError";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			WCUtils.setErrorInContext(getWCContext(), e);
			return "error";
		}*/
		try {
			getCompleteOrderDetailsDoc();
			String editedOrderHeaderKey=XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
			if("true".equals(isEditOrder) && YFCCommon.isVoid(editedOrderHeaderKey))
			{
				editedOrderHeaderKey=orderHeaderKey;
			}
			if(YFCCommon.isVoid(editedOrderHeaderKey))
			{			
				XPEDXWCUtils.setMiniCartDataInToCache(getOrderElementFromOutputDocument(), wcContext);
			}
			else if(!YFCCommon.isVoid(editedOrderHeaderKey)&& !editedOrderHeaderKey.equals(orderHeaderKey))
				XPEDXWCUtils.setMiniCartDataInToCache(getOrderElementFromOutputDocument(), wcContext);		
			
			
			custmerPONumber = orderOutputDoc.getDocumentElement().getAttribute("CustomerPONo");
			
			Document rulesDoc = (Document) wcContext.getWCAttribute("rulesDoc");
			if(rulesDoc == null){
				rulesDoc = XPEDXOrderUtils.getValidationRulesForCustomer(getOrderElementFromOutputDocument(), wcContext);
				wcContext.setWCAttribute("rulesDoc", rulesDoc, WCAttributeScope.LOCAL_SESSION);	
			}
			XPEDXOrderUtils.validateCustomerFieldValues(getOrderElementFromOutputDocument(),rulesDoc, wcContext);
			
			HttpSession session = getWCContext().getSCUIContext().getRequest().getSession();
			Object isCustomerPoRequired=session.getAttribute(XPEDXConstants.REQUIRE_CUSTOMER_PO_NO_RULE);
			if(isCustomerPoRequired != null)
			{
				isCustomerPOMandatory=(String)isCustomerPoRequired;
			}
			HashMap<String, ArrayList<String>> requiredCustFields = (HashMap<String, ArrayList<String>>)session.getAttribute("requiredCustFieldsErrorMap");
			setEditOrder();
			if(requiredCustFields!=null && !requiredCustFields.isEmpty()){
				setCustomerFieldsValidated("Y");
				return "Required_CustFields_Error";
			}
			shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			//String shipFromBranch = (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH,WCAttributeScope.LOCAL_SESSION);
			String shipFromBranch=shipToCustomer.getExtnShipFromBranch();
			setInventoryIndicatorMap(xpedxwcUtils.getInventoryCheckMap(getOutputDocument(), shipFromBranch, getWCContext()));
			getEmailAddrs();
			getAllItemSKUs();
			//getCustomerPONumbers();
			// Get the customer fields
			getCustomerDisplayFields();
			processOrderLines();
			// If edit order then sort the order line as per JIRA # 2851
			//if(!YFCCommon.isVoid(editedOrderHeaderKey)){
				ArrayList<Element> tempMajorLines1 = getMajorLineElements();
				Collections.sort(tempMajorLines1, new XpedxLineSeqNoComparator());
			//}
			//Added for PNA call
			//ArrayList<XPEDXItem> inputItems = getPnAInputDoc(orderOutDoc);
			/*Begin - Changes made by Mitesh Parikh for JIRA#3595*/
			ArrayList<Element> ueAdditionalAttrElemList = SCXmlUtil.getElements(getOrderElementFromOutputDocument(), "Extn/XPXUeAdditionalAttrXmlList/XPXUeAdditionalAttrXml");
			XPEDXPriceAndAvailability pna=new XPEDXPriceAndAvailability();
			if(ueAdditionalAttrElemList!=null && ueAdditionalAttrElemList.size()>0)
				pna = XPEDXPriceandAvailabilityUtil.getPriceAndAvailability(wcContext,ueAdditionalAttrElemList.get(0));
			/*End - Changes made by Mitesh Parikh for JIRA#3595*/
			//XPEDXPriceAndAvailability pna = XPEDXPriceandAvailabilityUtil.getPriceAndAvailability(wcContext,orderHeaderKey);	
			//This takes care of displaying message to Users based on ServiceDown, Transmission Error, HeaderLevelError, LineItemError 
			ajaxDisplayStatusCodeMsg  =   XPEDXPriceandAvailabilityUtil.getAjaxDisplayStatusCodeMsg(pna) ;
			
			setAjaxLineStatusCodeMsg(ajaxDisplayStatusCodeMsg);
			Document lineTpeMDoc=SCXmlUtil.createDocument("Items");
			setPnaHoverForEditOrderLine(pna,lineTpeMDoc);
			pnaHoverMap = XPEDXPriceandAvailabilityUtil.getPnAHoverMap(pna.getItems(),true);
			//PNA call end
			//Setting the price hover map
			//added for jira 2885 
			if(pna.getHeaderStatusCode().equalsIgnoreCase("00")){
				pnALineErrorMessage=XPEDXPriceandAvailabilityUtil.getLineErrorMessageMap(pna.getItems());
			}
			
			//setPriceHoverMap(XPEDXPriceandAvailabilityUtil.getPricingInfoFromItemDetails(pna.getItems(), wcContext,true,lineTpeMDoc.getDocumentElement()));
			setPriceHoverMap(XPEDXPriceandAvailabilityUtil.getPricingInfoFromItemDetails(pna.getItems(), wcContext, true, lineTpeMDoc.getDocumentElement(), true, orderOutputDoc));
			// Code for displaying Last Modified by in cart page
			String createUserIDStr = "";
			String modifyUserIdStr = "";
			String lastModifiedDateStr = "";
			
			modifyUserIdStr = getOrderElementFromOutputDocument().getAttribute("Modifyuserid");
			if(YFCUtils.isVoid(modifyUserIdStr)){
				createUserIDStr = getOrderElementFromOutputDocument().getAttribute("Createuserid");
				setLastModifiedUserId(createUserIDStr);
			}else{
				setLastModifiedUserId(modifyUserIdStr);
			}
			//added for 2769
			lastModifiedDateStr = getOrderElementFromOutputDocument().getAttribute("Modifyts");
			if(lastModifiedDateStr !=null){
				setLastModifiedDateString(lastModifiedDateStr);
				setLastModifiedDate(YFCDate.getYFCDate(lastModifiedDateStr));
			}
			
			
			xPEDXPaymentMethodHelper = new XPEDXPaymentMethodHelper(
					getWCContext(), getOutputDocument().getDocumentElement());
			xPEDXPaymentMethodHelper.processRemainingAmountToAuth();
			creditCardCVVStorageHelper = new CreditCardCVVStorageHelper(
					getWCContext());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			XPEDXWCUtils.logExceptionIntoCent(e.getMessage());
		}
		checkIfCouponEntryAllowed();
		relayCouponOperationError();
		setOrderSummaryFlagValues();
		//commenting below method since we have already verified entitlement while adding item.
		//checkItemEntitlements();
		setOrderCutOffDate();
		// TASK 30 OP
		HashMap<String, String> billToShipToMap = orderUtils.getBillToShipToFromOrder(orderOutputDoc,getOrderHeaderKey(), wcContext);
		setOrderBillToID(billToShipToMap.get("OrderBillToID"));
		setOrderShipToID(billToShipToMap.get("OrderShipToID"));
		
		/*if("true".equals(isEditOrder ))
		{
			setEditOrdersMap(orderHeaderKey);	
		}*/
		
		try 
		{
	        // Validate the order and process any errors returned.
	       // getAndProcessOrderValidationDoc();
			setShipCompleteOption(shipToCusotmerDocument);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime=System.currentTimeMillis();
		System.out.println("Time taken in milliseconds in XPEDXDraftOrderSummaryAction class : "+(endTime-startTime));
		return "success";
	
	}
	
	private void setPnaHoverForEditOrderLine(XPEDXPriceAndAvailability pna,Document lineTpeMDoc)
	{
		try
		{
			if("true".equals(isEditOrder))
			{
				XPEDXItem item=null;
				Element orderLeme=(Element)getOrderElementFromOutputDocument().getElementsByTagName("PriceInfo").item(0);
				String currencyCode=orderLeme.getAttribute("Currency");
				Iterator<Element> it=getMajorLineElements().iterator();
				
				 Element lineTypeElem=lineTpeMDoc.getDocumentElement();
				while(it.hasNext())
				{
					Element orderLineElem=it.next();
					Element extnElem=(Element)orderLineElem.getElementsByTagName("Extn").item(0);
					String extnEditOrderFlag=extnElem.getAttribute("ExtnEditOrderFlag");
					Element orderLineTran=(Element)orderLineElem.getElementsByTagName("OrderLineTranQuantity").item(0);
					String lineType=orderLineElem.getAttribute("LineType");
					if(!"Y".equals(extnEditOrderFlag))
					{
						Element itemElem=(Element)orderLineElem.getElementsByTagName("Item").item(0);
						item=new XPEDXItem();
						item.setLegacyProductCode(itemElem.getAttribute("ItemID"));
						item.setPriceCurrencyCode(currencyCode);
						item.setPricingUOM(extnElem.getAttribute("ExtnPricingUOM"));
						item.setUnitPricePerPricingUOM(extnElem.getAttribute("ExtnUnitPrice"));
						item.setRequestedQtyUOM(orderLineTran.getAttribute("TransactionalUOM"));
						item.setUnitPricePerRequestedUOM(extnElem.getAttribute("ExtnReqUOMUnitPrice"));
						item.setLineNumber(orderLineElem.getAttribute("PrimeLineNo"));
						if("M".equals(lineType))
						{
							Element lineTypeMElem=SCXmlUtil.createChild(lineTypeElem, "Item");
							lineTypeMElem.setAttribute("ItemID", itemElem.getAttribute("ItemID"));
							lineTypeMElem.setAttribute("UnitOfMeasure", extnElem.getAttribute("ExtnPricingUOM"));
							Element primaryInfo=SCXmlUtil.createChild(lineTypeMElem, "PrimaryInformation");
							primaryInfo.setAttribute("MinOrderQuantity", "1");
							primaryInfo.setAttribute("PricingUOM", extnElem.getAttribute("ExtnPricingUOM"));
							primaryInfo.setAttribute("PricingQuantityConvFactor", "1");
						}
						pna.getItems().add(item);
					}
				}
			}
		}
		catch(Exception e)
		{
			LOG.error("Error setting the price for existing user during edititng cart "+e.getMessage());
		}
	}
	private void setEditOrder()
	{
		Element orderElem=getOutputDocument().getDocumentElement();
		if("N".equals(orderElem.getAttribute("DraftOrderFlag")) && "Y".equals(orderElem.getAttribute("HasPendingChanges")))
		{
			this.isEditOrder="true";
		}
		else
		{
			this.isEditOrder = "false";
		}
	}
	
	/*private void setEditOrdersMap(String orderHeaderKey)
	{
		Map <String,Map<String,Element>> orderMap=XPEDXWCUtils.getOrderLineFromChangeOrderXML(wcContext,orderHeaderKey);
		if(orderMap != null && orderMap.size() >0)
		{
			editOrderOrderMap=orderMap.get("OrderHeader");
			editOrderOrderLineMap=orderMap.get("OrderLines");
		}
	}*/
	private void getAllItemSKUs() throws CannotBuildInputException, XPathExpressionException
	{
		//Fetch all the extn fields from customer
		
		String useCustSku = (String)wcContext.getWCAttribute(XPEDXConstants.CUSTOMER_USE_SKU,WCAttributeScope.LOCAL_SESSION);
		/*String envCode = (String)wcContext.getWCAttribute(XPEDXConstants.ENVIRONMENT_CODE,WCAttributeScope.LOCAL_SESSION);
		String companyCode = (String)wcContext.getWCAttribute(XPEDXConstants.COMPANY_CODE,WCAttributeScope.LOCAL_SESSION);
		String legacyCustomerNumber = (String)wcContext.getWCAttribute(XPEDXConstants.LEGACY_CUST_NUMBER,WCAttributeScope.LOCAL_SESSION);*/
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
			HashMap<String, String> primaryInfoSKUItemsMap = new HashMap<String, String>();
			Element orderLineElement = (Element)orderLineElemList.get(i);
			String lineType=orderLineElement.getAttribute("LineType");
			if("C".equalsIgnoreCase(lineType) && "M".equalsIgnoreCase(lineType)) {
				continue;
			}
			
			Element itemElement = SCXmlUtil.getChildElement(orderLineElement,
					"Item");
			String itemId = itemElement.getAttribute("ItemID");// orderline/item
			
			if(skuMap.containsKey(itemId))
				continue;
			
			/*Begin - Changes made by Mitesh Parikh for JIRA 3595*/
			Element itemDetailsElement = SCXmlUtil.getChildElement(orderLineElement, "ItemDetails");
			if(itemDetailsElement!=null)
			{
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
				
			}
			
			//HashMap<String, String> itemSkuMap = XPEDXWCUtils.getAllSkusForItem(wcContext, itemId);
			//skuMap.put(itemId, itemSkuMap);
		}
		
		if(itemIdList.size()>0){
			skuMap = XPEDXWCUtils.getAllSkusForItem(wcContext, itemIdList, itemsSkuMap);
		}
		/*End - Changes made by Mitesh Parikh for JIRA 3595*/
	}
	protected void setOrderCutOffDate(){
		String customerID = wcContext.getCustomerId();
		String storeFrontID = wcContext.getStorefrontId();
		String shipFromDivision = null;
		deliveryCutOffTime=shipToCustomer.getShipToDivDeliveryCutOffTime();
		//Added For Jira 3465
		deliveryInfo=shipToCustomer.getShipToDivdeliveryInfo();
		
		if(deliveryCutOffTime==null)
		{
			try {
				shipFromDivision =shipToCustomer.getExtnShipFromBranch();// XPEDXWCUtils.getCustomerShipFromDivision(customerID, storeFrontID);
			} catch (Exception e) {
				LOG.error("Unable to get CustomerShipFromDivision for "+customerID+". ",e);
				return;
			}
			if(YFCCommon.isVoid(shipFromDivision)){
				LOG.error("ShipFromDivision for "+customerID+" is NULL. Returning back to the caller.");
				return;
			}
			//get the Ship Node information for shipFromDivision
			
			String envCode=shipToCustomer.getExtnEnvironmentCode();//XPEDXWCUtils.getEnvironmentCode(customerID);
			Map<String, String> valueMap = new HashMap<String, String>();
			if(envCode!=null && envCode.trim().length()>0){
				valueMap.put("/Organization/@OrganizationCode", shipFromDivision+"_"+envCode);
			}else{
				LOG.error("EnvCode for "+customerID+" is NULL. Returning back to the caller.");
				return;
			}
			try {
				
				Element input = WCMashupHelper.getMashupInput("XPEDXGetShipOrgNodeDetails", valueMap, getWCContext().getSCUIContext());
				Object obj = WCMashupHelper.invokeMashup("XPEDXGetShipOrgNodeDetails", input, getWCContext().getSCUIContext());
				Document outputDoc = ((Element) obj).getOwnerDocument();
				setShipFromDoc(outputDoc);
				if(YFCCommon.isVoid(outputDoc)){
					LOG.error("No DB record exist for Node "+ shipFromDivision+"_"+envCode+". Cannot set Delivery Cut off time");
					return;
				}
				/*  TODO -FXD1-3 This value coming in staging.. */
				deliveryCutOffTime = SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnDeliveryCutOffTime");
				//Added For Jira 3465
				deliveryInfo = SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnDeliveryInfo");
				shipToCustomer.setShipToDivDeliveryCutOffTime(deliveryCutOffTime);
				//Added For Jira 3465
				shipToCustomer.setShipToDivdeliveryInfo(deliveryInfo);
				XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER, shipToCustomer);
			} catch (CannotBuildInputException e) {
				LOG.error("Unable to get XPEDXGetShipOrgNodeDetails for "+ shipFromDivision+"_"+envCode+". ",e);
				return;
			}
			catch (Exception e) {
				LOG.error("Unable to get XPEDXGetShipOrgNodeDetails for "+ shipFromDivision+"_"+envCode+". ",e);
				return;
			}
		}
		
	}
	
	private void checkItemEntitlements() {
		Document orderDoc = getOutputDocument();
		assert (orderDoc != null);
		Element OrderLinesElement = SCXmlUtil.getChildElement(orderDoc
				.getDocumentElement(), "OrderLines");
		NodeList orderLineNodeList = OrderLinesElement
				.getElementsByTagName("OrderLine");
		for (int i = 0; i < orderLineNodeList.getLength(); i++) {
			Node orderLineNode = orderLineNodeList.item(i);
			Element itemElem = SCXmlUtil.getChildElement((Element) orderLineNode, "Item");
			String itemID = SCXmlUtil.getAttribute(itemElem, "ItemID");
			String lineType = SCXmlUtil.getAttribute((Element) orderLineNode, "LineType");
			if(itemID!=null && itemID.trim().length()>0 && !lineType.equalsIgnoreCase("C")) {
				if(ItemIDs != null) {
					ItemIDs.add(itemID);
				}
			}
		}
		try{
			if(ItemIDs != null && ItemIDs.size()>0) {
				Element itemEntitlementsOutput = prepareAndInvokeMashup(MASHUP_ITEM_ENTITLEMENT_CHECK);
				if(itemEntitlementsOutput!=null) {
					List<Element> itemsList = SCXmlUtil.getChildrenList(itemEntitlementsOutput);
					int itemIdsSize = ItemIDs.size();
					if(itemsList!=null) {
						int itemListSize = itemsList.size();
						if(itemListSize<=itemIdsSize){
							if(itemListSize<itemIdsSize)
								setItemEntitlmentsChanged(true);
							for(int i=0;i<itemIdsSize;i++) {
								Element entitledItemElem = SCXmlUtil.getElementByAttribute(itemEntitlementsOutput, "Item", "ItemID", ItemIDs.get(i));						
								if(entitledItemElem != null) {
									entitledItems.add(ItemIDs.get(i));
								}
							}
						}
					}
				}
			}
		}
		catch (Exception ex){
			LOG.debug(ex.getMessage());
			ex.printStackTrace();
		}
	}

	/*
	 * Prepares a Arraylist of XPEDXItem, after looping through the orderlines
	 */
	private ArrayList<XPEDXItem> getPnAInputDoc() {
		ArrayList<XPEDXItem> pnaList = new ArrayList<XPEDXItem>();
		Document orderDoc = getOutputDocument();
		assert (orderDoc != null);
		Element OrderLinesElement = SCXmlUtil.getChildElement(orderDoc
				.getDocumentElement(), "OrderLines");
		NodeList orderLineNodeList = OrderLinesElement
				.getElementsByTagName("OrderLine");
		for (int i = 0; i < orderLineNodeList.getLength(); i++) {
			Node orderLineNode = orderLineNodeList.item(i);
			Document doc = YFCDocument.createDocument().getDocument();
			doc.appendChild(doc.importNode(orderLineNode, true));
			doc.renameNode(doc.getDocumentElement(), doc.getNamespaceURI(),
					"OrderLine");
			Element orderLineElement = doc.getDocumentElement();
			Element itemElement = SCXmlUtil.getChildElement(orderLineElement,
					"Item");
			SCXmlUtil.getString(orderLineElement);
			Element olTranElement = SCXmlUtil.getChildElement(orderLineElement,
					"OrderLineTranQuantity");
			String lineNumber = orderLineElement.getAttribute("PrimeLineNo");// combination
			// of
			// subline#
			// and
			// primeline#
			String legacyProductCode = itemElement.getAttribute("ItemID");// orderline/item
			// details
			String requestedQtyUOM = olTranElement
					.getAttribute("TransactionalUOM");// transaction table
			String requestedQty = olTranElement.getAttribute("OrderedQty");// transactional
			XPEDXItem item = new XPEDXItem();
			item.setLegacyProductCode(legacyProductCode);
			item.setLineNumber(lineNumber);
			item.setRequestedQty(requestedQty);
			item.setRequestedQtyUOM(requestedQtyUOM);
			LOG.debug("Adding a item to the P&A input list: "
					+ legacyProductCode);
			pnaList.add(item);

		}
		return pnaList;
	}
	
	   protected void getAndProcessOrderValidationDoc() throws Exception
	    {
	        Set mashupSet = buildSetFromDelmitedList(MASHUP_DOD_VALIDATE_ITEM_FOR_ORDERING);
	        Map<String,Element> outputMap = prepareAndInvokeMashups(mashupSet);
	        
	        Element outputEl = outputMap.get(MASHUP_DOD_VALIDATE_ITEM_FOR_ORDERING);
	        YFCElement ele = YFCDocument.getDocumentFor(outputEl.getOwnerDocument()).getDocumentElement();
	        if (outputEl != null)
	        {
	            Document validationDoc = getDocFromOutput(outputEl);
	            List<Element> orderLines = XMLUtilities.getElements(validationDoc, "Order/OrderLines/OrderLine");
	            if(orderLines != null)
	            {
	                String unknownError = getText("UnknownItemError");
	                Iterator lineIter = orderLines.iterator();
	                while(lineIter.hasNext())
	                {
	                    StringBuffer errorStringBuffer = new StringBuffer();
	                    Element orderLine = (Element)lineIter.next();
	                    String orderLineKey = orderLine.getAttribute("OrderLineKey");
	                    List<Element> errors = XMLUtilities.getElements(orderLine, "Errors/Error");
	                    if( (errors != null) && (!errors.isEmpty()) )
	                    {
	                        String errorString = null;
	                        Iterator errorIter = errors.iterator();
	                        while(errorIter.hasNext())
	                        {
	                            Element error = (Element)errorIter.next();
	                            String errorCode = error.getAttribute("ErrorCode");

	                            if(OrderConstants.ITEM_VALIDATION_ERROR_ITEM_NOT_EFFECTIVE.equals(errorCode))
	                            {
	                                errorString = getText("ItemNotCurrentlyEffective");
	                            }
	                            else if(OrderConstants.ITEM_VALIDATION_ERROR_CANT_BE_SOLD_SEPARATELY.equals(errorCode))
	                            {
	                                errorString = getText("ItemCannotBeSoldSeparately");
	                            }
	                            else if(OrderConstants.ITEM_VALIDATION_ERROR_ITEM_STATUS_INVALID.equals(errorCode))
	                            {
	                                errorString = getText("ItemStatusNotValid");
	                            }
	                            else if(OrderConstants.ITEM_VALIDATION_ERROR_MIN_QTY_NOT_MET.equals(errorCode))
	                            {
	                                String param = "<?>";
	                                ArrayList args = new ArrayList();
	                                Element minQtyAttributeElem = XMLUtilities.getElement(error, "Attribute[@Name='MinOrderQuantity']");
	                                if(minQtyAttributeElem != null)
	                                {
	                                    String value = minQtyAttributeElem.getAttribute("Value");
	                                    if(value != null)
	                                    {
	                                        param = value;
	                                    }
	                                }
	                                else
	                                {
	                                    LOG.error("Missing MinOrderQuantity Attribute for validateItemForOrdering ErrorCode " + errorCode);
	                                }
	                                args.add(param);
	                                errorString = getText("ItemMinQuantity", args);
	                            }
	                            else if(OrderConstants.ITEM_VALIDATION_ERROR_MAX_QTY_EXCEEDED.equals(errorCode))
	                            {
	                                String param = "<?>";
	                                ArrayList args = new ArrayList();
	                                Element maxQtyAttributeElem = XMLUtilities.getElement(error, "Attribute[@Name='MaxOrderQuantity']");
	                                if(maxQtyAttributeElem != null)
	                                {
	                                    String value = maxQtyAttributeElem.getAttribute("Value");
	                                    if(value != null)
	                                    {
	                                        param = value;
	                                    }
	                                }
	                                else
	                                {
	                                    LOG.error("Missing MaxOrderQuantity Attribute for validateItemForOrdering ErrorCode " + errorCode);
	                                }
	                                args.add(param);
	                                errorString = getText("ItemMaxQuantity", args);
	                            }
	                            else if(OrderConstants.ITEM_VALIDATION_ERROR_CONFIG_INVALID.equals(errorCode))
	                            {
	                                errorString = getText("ItemRequresReconfiguration");
	                            }
	                            else if(OrderConstants.ITEM_VALIDATION_ERROR_MODEL_MISSING_OR_INCORRECT.equals(errorCode))
	                            {
	                                errorString = getText("ItemHasMissingOrInvalidModel");
	                            }
	                            else if(OrderConstants.ITEM_VALIDATION_ERROR_ITEM_REQUIRES_CONFIGURATION.equals(errorCode))
	                            {
	                                errorString = getText("ItemRequiresConfiguration");
	                            }
	                            else if(OrderConstants.ITEM_VALIDATION_ERROR_NOT_ENTITLED.equals(errorCode))
	                            {
	                                errorString = getText("ItemNotEntitled");
	                            }
	                            else if(OrderConstants.ITEM_VALIDATION_ERROR_VARIANT_ITEM.equals(errorCode))
	                            {
	                                errorString = getText("ItemIsVariant");
	                            }
	                            else
	                            {
	                                errorString = unknownError;
	                            }
	                            
	                            if(errorStringBuffer.length() > 0)
	                            {
	                                errorStringBuffer.append(ERROR_MSG_SEPARATOR);
	                            }
	                            errorStringBuffer.append(errorString);
	                        }
	                        
	                        invalidOrderLinesMap.put(orderLineKey, errorStringBuffer.toString());
	                    }
	                }
	            }
	        }
	    }
	   
	    public HashMap<String, String> getInvalidOrderLinesMap()
	    {
	        return invalidOrderLinesMap;
	    }
	private void getEmailAddrs() throws Exception{
		// fetch all the email addresses from the user profile
/*		Document custOutDoc = getCustomerDetails();
		YFCDocument custDoc = YFCDocument.getDocumentFor(custOutDoc);
		YFCElement custContactListEle = custDoc.getDocumentElement();
		YFCElement custContactEle=(YFCElement)YFCDocument.getNodeFor(XPEDXWCUtils.getMSAPCustomerContactFromContacts(custOutDoc,wcContext));
		
		if(custContactEle!= null)
		{
			custContactListEle =custContactEle;
		}
		//YFCElement custContactEle = custContactListEle.getElementsByTagName(elementName)
		YFCElement custExtnEle = custContactListEle.getElementsByTagName("Extn").item(0);
		/*String addnlEmailAddrs = custExtnEle.getAttribute("ExtnAddnlEmailAddrs");*/
		/**
		 * Changes Start for JIRA 3382
		 */
		String userEmailIdinSession = "";
		XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
		String userEmailId = "";
		userEmailIdinSession = (String) wcContext.getWCAttribute("emailId");
		if(userEmailIdinSession != null && !userEmailIdinSession.trim().equalsIgnoreCase("")){
			userEmailId = userEmailIdinSession;
			xpedxCustomerContactInfoBean.setPersonInfoEmailID(userEmailIdinSession);
			XPEDXWCUtils.setObectInCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean, xpedxCustomerContactInfoBean);
			
		}
		else{
			userEmailId = xpedxCustomerContactInfoBean.getMsapEmailID();
		}
		/**
		 * Changes End for JIRA 3382
		 */
//		Element xpxCustContExtnEle= XPEDXWCUtils.getXPXCustomerContactExtn(wcContext, wcContext.getCustomerContactId());
		String addnlEmailAddrs = null;
		String addnlPOList = null;
		
/*		if(xpxCustContExtnEle!=null){
			addnlEmailAddrs = xpxCustContExtnEle.getAttribute("AddnlEmailAddrs");
			addnlPOList = xpxCustContExtnEle.getAttribute("POList");
		}*/
		
		//addnlEmailAddrs = (String) wcContext.getWCAttribute("addnlEmailAddrs");
		if(xpedxCustomerContactInfoBean.getAddEmailID()!=null && xpedxCustomerContactInfoBean.getAddEmailID().trim()!=""){
		addnlEmailAddrs = xpedxCustomerContactInfoBean.getAddEmailID();
		}else{
			addnlEmailAddrs = (String) wcContext.getWCAttribute("addnlEmailAddrs");
		}
		
		/*Element xpxCustContExtnEle= XPEDXWCUtils.getXPXCustomerContactExtn(wcContext, wcContext.getCustomerContactId());
		
		if(xpxCustContExtnEle!=null){
			addnlEmailAddrs = xpxCustContExtnEle.getAttribute("AddnlEmailAddrs");
		}*/
		addnlPOList = (String) wcContext.getWCAttribute("addnlPOList");;
		
/* JIRA 3382 removed userprofile email address from addition email address list*/
	/*	if(YFCCommon.isVoid(addnlEmailAddrs))
			addnlEmailAddrs = userEmailId;
		else
			addnlEmailAddrs = addnlEmailAddrs.concat(userEmailId);// Concatenating the users email address too as a requirement for OP Task #68
END of JIRA 3382*/
		
		if (!YFCCommon.isVoid(addnlEmailAddrs)){
			String[] emailListSplit = addnlEmailAddrs.replace(" ","").split(",");
			for (int i = 0; i < emailListSplit.length; i++) {
				addAddnlEmailAddrList(emailListSplit[i]);
			}			
		}
		
		
		// fetch all the email id from the order 
		//
		Document orderOutDoc = getOutputDocument();
		YFCDocument orderDoc = YFCDocument.getDocumentFor(orderOutDoc);
		YFCElement orderEle = orderDoc.getDocumentElement();
		YFCElement orderExtnElem = orderEle.getChildElement("Extn");
		String addnlEmailAddr = orderExtnElem.getAttribute("ExtnAddnlEmailAddr");
		if (!YFCCommon.isVoid(addnlEmailAddr)){
			String[] 	emailListSplit = addnlEmailAddr.replace(" ","").split(";");
			for (int i = 0; i < emailListSplit.length; i++) {
				addSelectedAddnlEmailAddrList(emailListSplit[i]);
				addAddnlEmailAddrList(getSelectedAddnlEmailAddrList());
			}
		}
		//Set addnlPoNumberList with empty ArrayList to avoid error on Select PO # dropdown
		setAddnlPoNumberList(new ArrayList<String>());
		//Fecth all the customer POs from the user profile
		/*String addnlPOList = custExtnEle.getAttribute("ExtnPOList");*/
		if (!YFCCommon.isVoid(addnlPOList)){
			//Start for JIRA 3645 - Sorting 
			String[] poListSplit = addnlPOList.split(",");
			
			Arrays.sort(poListSplit, new XPEDXAlphanumericSorting());
			/*for (int i = 0; i < poListSplit.length; i++) {
				addAddnlPoNumberList(poListSplit[i]);
			}	*/
			addnlPoNumberList = new LinkedHashSet<String>();
			addnlPoNumberList.addAll(Arrays.asList(poListSplit));
			addnlPoNumberList.remove(null);
			//End for JIRA 3645 - Sorting 
		}
		
		//Fetch all the customer POs from the order
		getCustomerPONumbers();
	}
	
	private void getCustomerPONumbers() throws Exception{
		// fetch all the customer po numbers from the order
		String customerPONoForOrder = getOrderElementFromOutputDocument().getAttribute("CustomerPONo");
		
		if (!YFCCommon.isVoid(customerPONoForOrder)){
			if(getAddnlPoNumberList()!=null && getAddnlPoNumberList().containsKey(customerPONoForOrder))
			{
				addSelectedPoNumberList(customerPONoForOrder);
			}
			else
				setNewPoNumber(customerPONoForOrder);
		}
		if(isCouponApplied()) {			
			setCustmerPONumber(getNewPoNumber());
		}
	}
	
	//Fetching all customer fields at order line level
	private void getCustomerDisplayFields() {
		//get the map from the session. if null query the DB
		HashMap customerFieldsSessionMap = getCustomerFieldsMapfromSession();
        if(null != customerFieldsSessionMap && customerFieldsSessionMap.size() >= 0){
        	LOG.debug("Found customerFieldsMap in the session");
        	customerFieldsMap = customerFieldsSessionMap;
        	return;
        }
   /*     HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
		try {
			setCustomerFieldsMap(new HashMap<String, String>());
			IWCContext context = WCContextHelper.getWCContext(ServletActionContext
					.getRequest());
			shipToCusotmerDocument = XPEDXWCUtils.getCustomerDetails(context
					.getCustomerId(), context.getStorefrontId(), "draftOrderGetCustomerLineFields");
			setCustomerFieldsMap(XPEDXOrderUtils.getCustomerLineFieldMap(shipToCusotmerDocument));
			localSession.setAttribute("customerFieldsSessionMap", customerFieldsMap);
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		} */
	}
	
	protected HashMap getCustomerFieldsMapfromSession(){
		/*HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
        HashMap customerFieldsSessionMap = (HashMap)localSession.getAttribute("customerFieldsSessionMap");*/
		XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
		HashMap customerFieldsSessionMap = (HashMap)XPEDXWCUtils.getObjectFromCache("customerFieldsSessionMap");
        return customerFieldsSessionMap;
	}

	private Document getCustomerDetails()  throws Exception{
		
			if (YFCCommon.isVoid(wcContext)){
				wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
			}
		
			// first fetch customerId and orgCodefrom the parameter
			String custId = wcContext.getCustomerContactId();
			String orgCode = wcContext.getStorefrontId();
			
			// if the customerId is not found in parameter fetch from the context.
			if (YFCCommon.isVoid(custId)){
				throw new YFCException("CustomerId is null.");
			}

			// if the orgCode is not found in parameter fetch from the context.
			if (YFCCommon.isVoid(orgCode)){
				throw new YFCException("Organization Code is null.");
			}
			
			// fetch the customer details using the customerId and orgCode
			setCustomerOutputDoc(XPEDXWCUtils.getUserInfo(custId, orgCode).getOwnerDocument());
			return getCustomerOutputDoc();
	}

	protected void setShipCompleteOption(Document customerDocument) throws Exception {
		//Commented for Performacne issue . since we have shipcompletion in to cache we need to get it from cache.
		/*if (customerDocument == null)
			customerDocument = XPEDXWCUtils.getCustomerDetails(wcContext
					.getCustomerId(), wcContext.getStorefrontId());
		shipCompleteOption = SCXmlUtil.getXpathAttribute(customerDocument
				.getDocumentElement(), "/Customer/Extn/@ExtnShipComplete");*/
		shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		shipCompleteOption=shipToCustomer.getExtnShipComplete();
		
	}

	protected void setOrderSummaryFlagValues() {
		try {
			Element orderElem = getOrderElementFromOutputDocument();
			Element extnElem = XMLUtilities.getChildElementByName(orderElem,
					"Extn");
			String dhFlag = SCXmlUtil.getAttribute(extnElem,
					"ExtnDeliveryHoldFlag");
			String wcFlag = SCXmlUtil.getAttribute(extnElem, "ExtnWillCall");
			String shipCmpl = SCXmlUtil.getAttribute(extnElem,
					"ExtnShipComplete");
			String orgCode = SCXmlUtil.getAttribute(extnElem,
					"ExtnCustomerDivision");
			String rushOrder = SCXmlUtil.getAttribute(extnElem,"ExtnRushOrderFlag");

			setShipFromNodeDetails(orgCode);
			if ("Y".equals(dhFlag))
				deliveryHoldFlag = true;
			if ("Y".equals(wcFlag))
				willCallFlag = true;
			//commented for JIRA 2156 and changed from Y to C for a shipping options
			//if ("Y".equals(shipCmpl))
			if ("C".equals(shipCmpl) || "N".equals(shipCmpl) || "Y".equals(shipCmpl))
				shipComplete = shipCmpl;
			if("Y".equals(rushOrder))
				setRushOrderFlag(true);
		} catch (Exception ex) {
			LOG.error("Expception while getting order element", ex);
		}
	}

	/**
	 * Method that populates the org node details.
	 * 
	 * @param orgCode
	 */
	
	protected void setShipFromNodeDetails(String orgCode) {
		if(!YFCCommon.isVoid(orgCode))
			setOrgDetailsBean(XPEDXWCUtils.getShipFromNodeDetails(orgCode, wcContext,null));
	}
	
	//Commented for performance issue since we storing organization details in cache so commenting to get it from cache.
	/*protected void setShipFromNodeDetails(String orgCode) {
		try {
			orgDetailsBean = new XPEDXOrgNodeDetailsBean();
			Map<String, String> valueMap = new HashMap<String, String>();
			String customerId=getWCContext().getCustomerId();
			String envCode=shipToCustomer.getExtnEnvironmentCode();
			orgDetailsBean=shipToCustomer.getOrganization();
			//String envCode=XPEDXWCUtils.getEnvironmentCode(customerId);
			if(orgDetailsBean == null)
			{
				if(envCode!=null && envCode.trim().length()>0){
					valueMap.put("/Organization/@OrganizationCode", orgCode+"_"+envCode);
				}else{
					valueMap.put("/Organization/@OrganizationCode", orgCode);
				}
				Element input = WCMashupHelper.getMashupInput(
						"XPEDXGetShipOrgNodeDetails", valueMap, getWCContext()
								.getSCUIContext());
				Object obj = WCMashupHelper.invokeMashup(
						"XPEDXGetShipOrgNodeDetails", input, getWCContext()
								.getSCUIContext());
				Document outputDoc = ((Element) obj).getOwnerDocument();
				if (null != outputDoc) {
					Element orgElem, corpElem = null;
					NodeList orgNodes = outputDoc
							.getElementsByTagName("Organization");
					if (orgNodes != null && orgNodes.getLength() > 0) {
						orgElem = (Element) orgNodes.item(0);
						orgDetailsBean.setOrgName(orgElem
								.getAttribute("OrganizationName"));
						corpElem = XMLUtilities.getElement(orgElem,
								"CorporatePersonInfo");
						if (corpElem != null) {
							orgDetailsBean.setAddressLineOne(corpElem
									.getAttribute("AddressLine1"));
							orgDetailsBean.setAddressLineTwo(corpElem
									.getAttribute("AddressLine2"));
							orgDetailsBean.setAddressLineThree(corpElem
									.getAttribute("AddressLine3"));
							orgDetailsBean.setAddressLineFour(corpElem
									.getAttribute("AddressLine4"));
							orgDetailsBean.setAddressLineFive(corpElem
									.getAttribute("AddressLine5"));
							orgDetailsBean.setAddressLineSix(corpElem
									.getAttribute("AddressLine6"));
							orgDetailsBean.setFirstName(corpElem
									.getAttribute("FirstName"));
							orgDetailsBean.setLastName(corpElem
									.getAttribute("LastName"));
							orgDetailsBean.setCity(corpElem.getAttribute("City"));
							orgDetailsBean.setCountry(corpElem
									.getAttribute("Country"));
							orgDetailsBean.setEmailId(corpElem
									.getAttribute("EMailID"));
							orgDetailsBean.setState(corpElem.getAttribute("State"));
							orgDetailsBean.setZipCode(corpElem
									.getAttribute("ZipCode"));
						}						
						shipToCustomer.setOrganization(orgDetailsBean);
						XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER, shipToCustomer);
					}
				}
			}
		} catch (CannotBuildInputException ex) {
			LOG.error("Error while invoking mashup", ex);
		} catch (XPathExpressionException xpEx) {
			LOG.error("Error accessing Organization Document", xpEx);
		}
	}*/

	public boolean isDeliveryHold() {
		return deliveryHoldFlag;
	}

	public boolean isWillCall() {
		return willCallFlag;
	}

	public String getShipCompleteOption() {
		return shipCompleteOption;
	}

	public XPEDXOrgNodeDetailsBean getOrgDetailsBean() {
		return orgDetailsBean;
	}

	public void setOrgDetailsBean(XPEDXOrgNodeDetailsBean orgDetailsBean) {
		this.orgDetailsBean = orgDetailsBean;
	}

	public Map<String, String> getAddnlEmailAddrList() {
		Map<String, String> newMap = new HashMap<String, String>();
		for(String email : this.addnlEmailAddrList){
			newMap.put(email, email);
		}
		if (newMap.size() == 0){
			newMap.put(" " , "  ");
		}
		return newMap;
	}

	public void setAddnlEmailAddrList(List<String> addnlEmailAddrList) {
		this.addnlEmailAddrList = new HashSet<String>(addnlEmailAddrList);
	}
	
	private void addAddnlEmailAddrList(String emailAddress){
		if (YFCCommon.isVoid(this.addnlEmailAddrList)){
			this.addnlEmailAddrList = new HashSet<String>();
		}
		if (!YFCCommon.isVoid(emailAddress)){
			this.addnlEmailAddrList.add(emailAddress.trim());
		}
	}

	private void addAddnlEmailAddrList(List<String> emailAddresses){
		if (YFCCommon.isVoid(this.addnlEmailAddrList)){
			this.addnlEmailAddrList = new HashSet<String>();
		}
		if (!YFCCommon.isVoid(emailAddresses)){
			this.addnlEmailAddrList.addAll(emailAddresses);
		}
	}
	
	public List<String> getSelectedAddnlEmailAddrList() {
		return new ArrayList<String>(selectedAddnlEmailAddrList);
	}

	public void setSelectedAddnlEmailAddrList(List<String> selectedAddnlEmailAddrList) {
		this.selectedAddnlEmailAddrList = new HashSet<String>(selectedAddnlEmailAddrList);
	}
	
	private void addSelectedAddnlEmailAddrList(String emailAddress){
		if (YFCCommon.isVoid(this.selectedAddnlEmailAddrList)){
			this.selectedAddnlEmailAddrList = new HashSet<String>();
		}
		if (!YFCCommon.isVoid(emailAddress)){
			this.selectedAddnlEmailAddrList.add(emailAddress.trim());
		}
	}
	
    public boolean getIsBOMValidationEnabled()
    {
        if(isBOMValidationEnabled == null)
        {
            boolean enabled = false;
            String ruleValue = BusinessRuleUtil.getBusinessRule("SWC_VALIDATE_CART_ITEMS", "0001", getWCContext());
            if("Y".equals(ruleValue))
                enabled = true;
            isBOMValidationEnabled = new Boolean(enabled);
        }
        return isBOMValidationEnabled.booleanValue();
    }

    public String getSkipBOMValidationsValue()
    {
        if(getIsBOMValidationEnabled())
            return "N";
        else
            return "Y";
    }

	public Document getCustomerOutputDoc() {
		return customerOutputDoc;
	}

	public void setCustomerOutputDoc(Document customerOutputDoc) {
		this.customerOutputDoc = customerOutputDoc;
	}
	
	
	public HashMap<String, JSONObject> getPnaHoverMap() {
		return pnaHoverMap;
	}

	public void setPnaHoverMap(HashMap<String, JSONObject> pnaHoverMap) {
		this.pnaHoverMap = pnaHoverMap;
	}
	
	
	public HashMap<String, XPEDXItemPricingInfo> getPriceHoverMap() {
		return priceHoverMap;
	}

	public void setPriceHoverMap(HashMap<String, XPEDXItemPricingInfo> priceHoverMap) {
		this.priceHoverMap = priceHoverMap;
	}

	public Map<String, String> getAddnlPoNumberList() {
		Map<String, String> newMap = new LinkedHashMap<String, String>();//Retain insertion Order - JIRA 3645
		for(String poNumber : this.addnlPoNumberList){
			newMap.put(poNumber, poNumber);
		}
		return newMap;
	}
	
	/**
	 * @return the newPoNumber
	 */
	public String getNewPoNumber() {
		return newPoNumber;
	}

	/**
	 * @param newPoNumber the newPoNumber to set
	 */
	public void setNewPoNumber(String newPoNumber) {
		this.newPoNumber = newPoNumber;
	}
	
	public List<String> getSelectedPoNumberList() {
		return new ArrayList<String>(selectedPoNumberList);
	}

	public void setSelectedPoNumberList(List<String> selectedAddnlPoNumberList) {
		this.selectedPoNumberList = new HashSet<String>(selectedAddnlPoNumberList);
	}
	
	private void addSelectedPoNumberList(String poNumber){
		if (YFCCommon.isVoid(this.selectedPoNumberList)){
			this.selectedPoNumberList = new HashSet<String>();
		}
		if (!YFCCommon.isVoid(poNumber)){
			this.selectedPoNumberList.add(poNumber.trim());
		}
	}

	public void setAddnlPoNumberList(List<String> addnlPoNumberList) {
		this.addnlPoNumberList = new HashSet<String>(addnlPoNumberList);
	}
	
	private void addAddnlPoNumberList(String customerPoNumber){
		if (YFCCommon.isVoid(this.addnlPoNumberList)){
			this.addnlPoNumberList = new HashSet<String>();
		}
		if (!YFCCommon.isVoid(customerPoNumber)){
			this.addnlPoNumberList.add(customerPoNumber.trim());
		}
	}

	private void addAddnlPoNumberList(List<String> customerPoNumbers){
		if (YFCCommon.isVoid(this.addnlPoNumberList)){
			this.addnlPoNumberList = new HashSet<String>();
		}
		if (!YFCCommon.isVoid(customerPoNumbers)){
			this.addnlPoNumberList.addAll(customerPoNumbers);
		}
	}
	
	public HashMap<String, String> getCustomerFieldsMap() {
		return customerFieldsMap;
	}

	public void setCustomerFieldsMap(HashMap<String, String> customerFieldsMap) {
		this.customerFieldsMap = customerFieldsMap;
	}

	protected boolean deliveryHoldFlag;
	protected boolean willCallFlag;
	protected String shipComplete = "";
	protected String shipCompleteOption;
	protected XPEDXOrgNodeDetailsBean orgDetailsBean;
	protected Set <String> addnlEmailAddrList;
	protected Set <String> selectedAddnlEmailAddrList;
	protected Document customerOutputDoc;
    protected Boolean isBOMValidationEnabled;
    private HashMap<String, JSONObject> pnaHoverMap;
    private HashMap<String, XPEDXItemPricingInfo> priceHoverMap;
    private HashMap<String, HashMap<String,String>> skuMap;
	private String customerSku;
	protected boolean couponApplied = false;
    //Added for Customer PO number
    private String newPoNumber;
    private String instructionText;
	protected Set <String> addnlPoNumberList;
	private Set <String> selectedPoNumberList;
	//Added for customer line fields
	private HashMap<String, String> customerFieldsMap;
	private static final Logger LOG = Logger
			.getLogger(XPEDXDraftOrderSummaryAction.class);
	protected String deliveryCutOffTime = "";
	//Added For Jira 3465
	protected String deliveryInfo = "";
	
	protected Map<String,Element> editOrderOrderMap = new HashMap<String,Element>();
	protected Map<String,Element> editOrderOrderLineMap = new HashMap<String,Element>();
	
	private String itemDtlBackPageURL="";
	
	public String getDeliveryInfo() {
		return deliveryInfo;
	}

	public void setDeliveryInfo(String deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}
	public String getItemDtlBackPageURL() {
		return itemDtlBackPageURL;
	}

	public void setItemDtlBackPageURL(String itemDtlBackPageURL) {
		this.itemDtlBackPageURL = itemDtlBackPageURL;
	}

	public String getInstructionText() {
		return instructionText;
	}

	public void setInstructionText(String instructionText) {
		this.instructionText = instructionText;
	}

	public boolean isCouponApplied() {
		return couponApplied;
	}

	public void setCouponApplied(boolean couponApplied) {
		this.couponApplied = couponApplied;
	}

	public String getDeliveryCutOffTime() {
		return deliveryCutOffTime;
	}

	public void setDeliveryCutOffTime(String deliveryCutOffTime) {
		this.deliveryCutOffTime = deliveryCutOffTime;
	}

	public ArrayList<String> getItemIDs() {
		return ItemIDs;
	}

	public void setItemIDs(ArrayList<String> itemIDs) {
		ItemIDs = itemIDs;
	}

	public ArrayList<String> getEntitledItems() {
		return entitledItems;
	}

	public void setEntitledItems(ArrayList<String> entitledItems) {
		this.entitledItems = entitledItems;
	}

	public boolean isItemEntitlmentsChanged() {
		return isItemEntitlmentsChanged;
	}

	public void setItemEntitlmentsChanged(boolean isItemEntitlmentsChanged) {
		this.isItemEntitlmentsChanged = isItemEntitlmentsChanged;
	}
	
		/**
	 * @return the inventoryIndicatorMap
	 */
	public HashMap<String, String> getInventoryIndicatorMap() {
		return inventoryIndicatorMap;
	}

	/**
	 * @param inventoryIndicatorMap the inventoryIndicatorMap to set
	 */
	public void setInventoryIndicatorMap(
			HashMap<String, String> inventoryIndicatorMap) {
		this.inventoryIndicatorMap = inventoryIndicatorMap;
	}

	/**
	 * @return the orderBillToID
	 */
	public String getOrderBillToID() {
		return orderBillToID;
	}

	/**
	 * @param orderBillToID the orderBillToID to set
	 */
	public void setOrderBillToID(String orderBillToID) {
		this.orderBillToID = orderBillToID;
	}

	/**
	 * @return the orderShipToID
	 */
	public String getOrderShipToID() {
		return orderShipToID;
	}

	/**
	 * @param orderShipToID the orderShipToID to set
	 */
	public void setOrderShipToID(String orderShipToID) {
		this.orderShipToID = orderShipToID;
	}

	/**
	 * @param rushOrderFlag the rushOrderFlag to set
	 */
	public void setRushOrderFlag(boolean rushOrderFlag) {
		this.rushOrderFlag = rushOrderFlag;
	}

	/**
	 * @return the rushOrderFlag
	 */
	public boolean isRushOrderFlag() {
		return rushOrderFlag;
	}
	
	
	public Map<String, Element> getEditOrderOrderMap() {
		return editOrderOrderMap;
	}

	public void setEditOrderOrderMap(Map<String, Element> editOrderOrderMap) {
		this.editOrderOrderMap = editOrderOrderMap;
	}

	public Map<String, Element> getEditOrderOrderLineMap() {
		return editOrderOrderLineMap;
	}

	public void setEditOrderOrderLineMap(Map<String, Element> editOrderOrderLineMap) {
		this.editOrderOrderLineMap = editOrderOrderLineMap;
	}

	public String getIsEditOrder()
	{	
		return isEditOrder;
	}

	public void setIsEditOrder(String isEditOrder) 
	{		
			this.isEditOrder = isEditOrder;
	}
	
	
	/**
	 * @return the customerFieldsValidated
	 */
	public String getCustomerFieldsValidated() {
		return customerFieldsValidated;
	}

	/**
	 * @param customerFieldsValidated the customerFieldsValidated to set
	 */
	public void setCustomerFieldsValidated(String customerFieldsValidated) {
		this.customerFieldsValidated = customerFieldsValidated;
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

	public String getIsCustomerPOMandatory() {
		return isCustomerPOMandatory;
	}

	public void setIsCustomerPOMandatory(String isCustomerPOMandatory) {
		this.isCustomerPOMandatory = isCustomerPOMandatory;
	}

	public String getShipComplete() {
		return shipComplete;
	}

	public void setShipComplete(String shipComplete) {
		this.shipComplete = shipComplete;
	}

	public String getCustmerPONumber() {
		return custmerPONumber;
	}

	public void setCustmerPONumber(String custmerPONumber) {
		this.custmerPONumber = custmerPONumber;
	}
	
	public YFCDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(YFCDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public String getLastModifiedDateString() {
		return lastModifiedDateString;
	}

	public void setLastModifiedDateString(String lastModifiedDateString) {
		this.lastModifiedDateString = lastModifiedDateString;
	}
	
	public String getLastModifiedDateToDisplay() {
		UtilBean utilBean = new UtilBean();
		String dateToDisplay="";
		if(lastModifiedDateString != null){
			dateToDisplay = utilBean.formatDate(lastModifiedDateString, wcContext, null, "MM/dd/yyyy");			
		}
		return dateToDisplay;
	}
	
	public String replaceString(String qty,String replaceStr,String withString){
		String split[]=qty.split("\\.");
		if(split != null && split.length >1)
		{
			int decimalPlace=Integer.parseInt(split[1]);
			if(decimalPlace==0)
				return split[0];
		}
		return qty;
	}
	
	/*Begin - Changes made by Mitesh Parikh for JIRA#3595*/
	protected void getCompleteOrderDetailsDoc() throws Exception
	{
		Document orderOutputDocument=null;
		if(getWCContext().getSCUIContext().getSession().getAttribute(CHANGE_ORDEROUTPUT_CHECKOUT_SESSION_OBJ)!=null)
		{
			orderOutputDocument=(Document)getWCContext().getSCUIContext().getSession().getAttribute(CHANGE_ORDEROUTPUT_CHECKOUT_SESSION_OBJ);
			setOutputDocument(orderOutputDocument);
			getWCContext().getSCUIContext().getSession().removeAttribute(CHANGE_ORDEROUTPUT_CHECKOUT_SESSION_OBJ);
			addModificationRuleToOrderListElement(getOrderElementFromOutputDocument());
		
		} else {
			super.getCompleteOrderDetailsDoc();
			
		}
		
	}
	/*End - Changes made by Mitesh Parikh for JIRA#3595*/
	
	private void addModificationRuleToOrderListElement(Element orderElement)
	{
		Element modificationsOrderElem=SCXmlUtil.createChild(orderElement, "Modifications");
		
		Element modificationOrderElem=SCXmlUtil.createChild(modificationsOrderElem, "Modification");
		modificationOrderElem.setAttribute("ModificationType", "PRICE");
		modificationOrderElem.setAttribute("ModificationAllowed", "Y");
		modificationsOrderElem.appendChild(modificationOrderElem);

		modificationOrderElem=SCXmlUtil.createChild(modificationsOrderElem, "Modification");
		modificationOrderElem.setAttribute("ModificationType", "SHIPTO");
		modificationOrderElem.setAttribute("ModificationAllowed", "Y");
		modificationsOrderElem.appendChild(modificationOrderElem);

		modificationOrderElem=SCXmlUtil.createChild(modificationsOrderElem, "Modification");
		modificationOrderElem.setAttribute("ModificationType", "CARRIER_SERVICE_CODE");
		modificationOrderElem.setAttribute("ModificationAllowed", "N");
		modificationsOrderElem.appendChild(modificationOrderElem);

		modificationOrderElem=SCXmlUtil.createChild(modificationsOrderElem, "Modification");
		modificationOrderElem.setAttribute("ModificationType", "REQ_SHIP_DATE");
		modificationOrderElem.setAttribute("ModificationAllowed", "Y");
		modificationsOrderElem.appendChild(modificationOrderElem);
		
		modificationOrderElem=SCXmlUtil.createChild(modificationsOrderElem, "Modification");
		modificationOrderElem.setAttribute("ModificationType", "SOLDTO");
		modificationOrderElem.setAttribute("ModificationAllowed", "N");
		modificationsOrderElem.appendChild(modificationOrderElem);		

		modificationOrderElem=SCXmlUtil.createChild(modificationsOrderElem, "Modification");
		modificationOrderElem.setAttribute("ModificationType", "CHANGE_ORDER_DATE");
		modificationOrderElem.setAttribute("ModificationAllowed", "N");
		modificationsOrderElem.appendChild(modificationOrderElem);
		
		modificationOrderElem=SCXmlUtil.createChild(modificationsOrderElem, "Modification");
		modificationOrderElem.setAttribute("ModificationType", "BILLTO");
		modificationOrderElem.setAttribute("ModificationAllowed", "Y");
		modificationsOrderElem.appendChild(modificationOrderElem);
		
		modificationOrderElem=SCXmlUtil.createChild(modificationsOrderElem, "Modification");
		modificationOrderElem.setAttribute("ModificationType", "ADD_LINE");
		modificationOrderElem.setAttribute("ModificationAllowed", "Y");
		modificationsOrderElem.appendChild(modificationOrderElem);

		modificationOrderElem=SCXmlUtil.createChild(modificationsOrderElem, "Modification");
		modificationOrderElem.setAttribute("ModificationType", "OTHERS");
		modificationOrderElem.setAttribute("ModificationAllowed", "Y");
		modificationsOrderElem.appendChild(modificationOrderElem);
		
		modificationOrderElem=SCXmlUtil.createChild(modificationsOrderElem, "Modification");
		modificationOrderElem.setAttribute("ModificationType", "PAYMENT_METHOD");
		modificationOrderElem.setAttribute("ModificationAllowed", "N");
		modificationsOrderElem.appendChild(modificationOrderElem);
		
		modificationOrderElem=SCXmlUtil.createChild(modificationsOrderElem, "Modification");
		modificationOrderElem.setAttribute("ModificationType", "CHANGE_PROMOTION");
		modificationOrderElem.setAttribute("ModificationAllowed", "Y");
		modificationsOrderElem.appendChild(modificationOrderElem);	
		
		ArrayList<Element> orderLineList=SCXmlUtil.getElements(orderElement, "/OrderLines/OrderLine");
		if(orderLineList!= null && orderLineList.size()>0) {
			for(int i=0;i<orderLineList.size();i++)
			{
				Element orderLineElement=orderLineList.get(i);		
				Element modificationsOrderLineElem=SCXmlUtil.createChild(orderLineElement, "Modifications");
				
				Element modificationOrderLineElem=SCXmlUtil.createChild(modificationsOrderLineElem, "Modification");
				modificationOrderLineElem.setAttribute("ModificationType", "PRICE");
				modificationOrderLineElem.setAttribute("ModificationAllowed", "Y");
				modificationsOrderLineElem.appendChild(modificationOrderLineElem);
				
				modificationOrderLineElem=SCXmlUtil.createChild(modificationsOrderLineElem, "Modification");
				modificationOrderLineElem.setAttribute("ModificationType", "SHIPTO");
				modificationOrderLineElem.setAttribute("ModificationAllowed", "Y");
				modificationsOrderLineElem.appendChild(modificationOrderLineElem);
				
				modificationOrderLineElem=SCXmlUtil.createChild(modificationsOrderLineElem, "Modification");
				modificationOrderLineElem.setAttribute("ModificationType", "CARRIER_SERVICE_CODE");
				modificationOrderLineElem.setAttribute("ModificationAllowed", "N");
				modificationsOrderLineElem.appendChild(modificationOrderLineElem);
				
				modificationOrderLineElem=SCXmlUtil.createChild(modificationsOrderLineElem, "Modification");
				modificationOrderLineElem.setAttribute("ModificationType", "REQ_SHIP_DATE");
				modificationOrderLineElem.setAttribute("ModificationAllowed", "Y");
				modificationsOrderLineElem.appendChild(modificationOrderLineElem);
				
				modificationOrderLineElem=SCXmlUtil.createChild(modificationsOrderLineElem, "Modification");
				modificationOrderLineElem.setAttribute("ModificationType", "OTHERS");
				modificationOrderLineElem.setAttribute("ModificationAllowed", "Y");
				modificationsOrderLineElem.appendChild(modificationOrderLineElem);
				
				modificationOrderLineElem=SCXmlUtil.createChild(modificationsOrderLineElem, "Modification");
				modificationOrderLineElem.setAttribute("ModificationType", "CHANGE_ORDER_DATE");
				modificationOrderLineElem.setAttribute("ModificationAllowed", "N");
				modificationsOrderLineElem.appendChild(modificationOrderLineElem);				
				
			}
		}
	}
	
}

/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.order;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.xml.xpath.XPathExpressionException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.comergent.appservices.configuredItem.XMLUtils;
import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.order.DraftOrderDetailsAction;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItem;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItemPricingInfo;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceAndAvailability;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCException;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfs.japi.YFSUserExitException;
/**
 * @author rugrani/Manohar/Manoj
 */
@SuppressWarnings("all")
public class XPEDXDraftOrderDetailsAction extends DraftOrderDetailsAction {
	XPEDXShipToCustomer shipToCustomer;
	XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean;
	private String customerItemFlag;
	private String mfgItemFlag;
	public String getCustomerItemFlag() {
		return customerItemFlag;
	}


	public void setCustomerItemFlag(String customerItemFlag) {
		this.customerItemFlag = customerItemFlag;
	}


	public String getMfgItemFlag() {
		return mfgItemFlag;
	}


	public void setMfgItemFlag(String mfgItemFlag) {
		this.mfgItemFlag = mfgItemFlag;
	}


	public String execute() {
		/* Begin - Changes made by Mitesh Parikh for 2422 JIRA */
		setItemDtlBackPageURL((wcContext.getSCUIContext().getRequest().getRequestURL().append("?").append(wcContext.getSCUIContext().getRequest().getQueryString())).toString());			
		/* End - Changes made by Mitesh Parikh for 2422 JIRA */
		XPEDXWCUtils.checkMultiStepCheckout();
		shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		xpedxCustomerContactInfoBean=(XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
		String ajaxDisplayStatusCodeMsg = "";
		String editedOrderHeaderKey = XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
		
		if(draftOrderError != null && "true".equalsIgnoreCase(draftOrderError)){
			return draftFlagError;
		}
		boolean isChangeOrderCalled=false;
		try {
			
			setDefaultShipToIntoContext();
			getCustomerLineDetails();
			
			if("true".equals(isPNACallOnLoad) || "Y".equals(isPNACallOnLoad))
			{
				callChangeOrder();
				if("true".equals(draftOrderFail) && "true".equals(isPNACallOnLoad)){
					return draftFlagError;
				}
				if(isOUErrorPage == true)
				{
					return "OUErrorPage";
				}
				
			} else {				
				changeOrderOutputDoc = (Document) getWCContext().getSCUIContext().getSession().getAttribute(CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ);
				if(changeOrderOutputDoc!=null)
				{
					setOutputDocument(changeOrderOutputDoc);
					getWCContext().getSCUIContext().getSession().removeAttribute(CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ);
				
				}				
			}			
			
			//DOMDocFromXMLString doc = new DOMDocFromXMLString();
			//Document orderOutputDocument=doc.createDomDocFromXMLString("C:\\xpedx\\NextGen\\src\\WebChannel\\main\\resources\\NewFile.xml");
			//setOutputDocument(orderOutputDocument);
			super.execute();
			LOG.debug("CHANGE ORDER API OUTPUT IN DRAFT ORDER DETAILS ACTION CLASS : "+SCXmlUtil.getString(getOutputDocument()));
			
			if("true".equals(isEditOrder) && YFCCommon.isVoid(editedOrderHeaderKey))
			{
				XPEDXWCUtils.setEditedOrderHeaderKeyInSession(wcContext, orderHeaderKey);
				editedOrderHeaderKey=orderHeaderKey;
				String newDefaultShipToForEditOrder  = getOrderElementFromOutputDocument().getAttribute("BuyerOrganizationCode");
				setCurrentCustomerIntoContext(newDefaultShipToForEditOrder);
				
			}
			if(YFCCommon.isVoid(editedOrderHeaderKey))
			{			
				if(getDraftOrderList() !=null && getDraftOrderList().equals("Y"))
						XPEDXCommerceContextHelper.createNewCartInContext(getWCContext(),getOrderElementFromOutputDocument(),orderHeaderKey);
				XPEDXWCUtils.setMiniCartDataInToCache(getOrderElementFromOutputDocument(), wcContext);
			}
			else if(!YFCCommon.isVoid(editedOrderHeaderKey)&& !editedOrderHeaderKey.equals(orderHeaderKey) )
				XPEDXWCUtils.setMiniCartDataInToCache(getOrderElementFromOutputDocument(), wcContext);
			//BEGIN: sort the orderlines based on legacy line number - RUgrani
			//if(null != isEditOrder && isEditOrder.equalsIgnoreCase("true")){			
				ArrayList<Element> tempMajorLines = getMajorLineElements();
				Collections.sort(tempMajorLines, new XpedxLineSeqNoComparator());
				
			//}
			//END: sort the orderlines based on legacy line number - RUgrani
			
			//Get the field validateCustomerFields. It will be set to Y when cart is updated
			if(getValidateCustomerFields()!=null && getValidateCustomerFields().equals("Y"))
			{
				Document rulesDoc = (Document) wcContext.getWCAttribute("rulesDoc");
				if(rulesDoc == null){
					rulesDoc = XPEDXOrderUtils.getValidationRulesForCustomer(getOrderElementFromOutputDocument(), wcContext);
					wcContext.setWCAttribute("rulesDoc", rulesDoc, WCAttributeScope.LOCAL_SESSION);	
				}
				XPEDXOrderUtils.validateCustomerFieldValues(getOrderElementFromOutputDocument(),rulesDoc, wcContext);
				setCustomerFieldsValidated("Y");
			}
			
			//Read the result of customer fields validation from session
			if(getCustomerFieldsValidated()!=null && getCustomerFieldsValidated().equals("Y")) {
				HttpSession session = getWCContext().getSCUIContext().getRequest().getSession();
				HashMap<String, ArrayList<String>> requiredCustFields = (HashMap<String, ArrayList<String>>)session.getAttribute("requiredCustFieldsErrorMap");
				if(requiredCustFields!=null && !requiredCustFields.isEmpty()) {
					setRequiredCustFieldsErrorMap(requiredCustFields);
					session.removeAttribute("requiredCustFieldsErrorMap");
				}
			}
			XPEDXWCUtils xPEDXWCUtils = new XPEDXWCUtils();
			String billToId = getOrderElementFromOutputDocument().getAttribute(
					"BillToID");
			String buyerOrganizationCode = getOrderElementFromOutputDocument()
					.getAttribute("BuyerOrganizationCode");
			String orderHeaderKey = getOrderElementFromOutputDocument()
					.getAttribute("OrderHeaderKey");
			// Code for displaying Last Modified by in cart page
			String createUserIDStr = "";
			String modifyUserIdStr = "";
			String lastModifiedDateStr = "";
			
			//added for XBT-146
			String isSalesRep = (String) wcContext.getSCUIContext().getSession().getAttribute("IS_SALES_REP");
			if(isSalesRep!=null && isSalesRep.equalsIgnoreCase("true")){
				 salesreploggedInUserName = (String)wcContext.getSCUIContext().getSession().getAttribute("loggedInUserName");
			}
			else{
				modifyUserIdStr = getOrderElementFromOutputDocument().getAttribute("Modifyuserid");
				if(YFCUtils.isVoid(modifyUserIdStr)){
					createUserIDStr = getOrderElementFromOutputDocument().getAttribute("Createuserid");
					setLastModifiedUserId(createUserIDStr);
				}else{
					setLastModifiedUserId(modifyUserIdStr);
				}
			}
			//end of XBT-146 changes
			lastModifiedDateStr = getOrderElementFromOutputDocument().getAttribute("Modifyts");
			setLastModifiedDateString(lastModifiedDateStr);
			/*
			XPEDXWCUtils.changeOrderOwnerToSelectedCustomer(getWCContext(),orderHeaderKey);
			There is no need to change the order owner
			*/
			getItemUOMs();
			checkforEntitlement();
			//Start of XB-689 - Adding a message , when there are duplicate items in the cart
			if(allItemIDsWithDuplicates !=null && allItemIDsWithDuplicates.size()>0){
				if(allItemIDsWithDuplicates.size() == 1)
					duplicateInfoMsg = "Please note that item " + allItemIDsWithDuplicates.get(0) +" have been added to your cart more than once.";
				else{
					String temp = "";
					for(int i =0; i<allItemIDsWithDuplicates.size(); i++){
						if(i == allItemIDsWithDuplicates.size()-1)
							temp = temp + allItemIDsWithDuplicates.get(i);
						else
							temp = temp + allItemIDsWithDuplicates.get(i) + ", ";
					}
						duplicateInfoMsg = "Please note that items " + temp +" have been added to your cart more than once.";
				}
			}//End of XB-689
			getAllItemSKUs();
//			getAllItemList();
			// BEGIN P&A Call: RUgrani
			//ArrayList<XPEDXItem> inputItems = getPnAInputDoc();
			if(getMajorLineElements().size()>0){
				ArrayList<Element> ueAdditionalAttrElem = SCXmlUtil.getElements(getOrderElementFromOutputDocument(), "Extn/XPXUeAdditionalAttrXmlList/XPXUeAdditionalAttrXml");
				XPEDXPriceAndAvailability pna=new XPEDXPriceAndAvailability();
				if(ueAdditionalAttrElem!=null && ueAdditionalAttrElem.size()>0)
					pna = XPEDXPriceandAvailabilityUtil.getPriceAndAvailability(wcContext,ueAdditionalAttrElem.get(0));			
				
				//XPEDXPriceAndAvailability pna = XPEDXPriceandAvailabilityUtil.getPriceAndAvailability(wcContext,orderHeaderKey);			
				//This takes care of displaying message to Users based on ServiceDown, Transmission Error, HeaderLevelError, LineItemError 
				Document lineTpeMDoc=SCXmlUtil.createDocument("Items");
				
				isChangeOrderCalled=setPnaHoverForEditOrderLine(pna,lineTpeMDoc);
				/*if(!isChangeOrderCalled)
					isChangeOrderCalled=isNewLine(getMajorLineElements());*/
				ajaxDisplayStatusCodeMsg = XPEDXPriceandAvailabilityUtil.getAjaxDisplayStatusCodeMsg(pna) ;
				//if(isChangeOrderCalled)
					setAjaxLineStatusCodeMsg(ajaxDisplayStatusCodeMsg);
		
				/*			
				if (inputItems != null && inputItems.size() > 0) {
					if (null == pna || pna.getItems() == null
						|| YFCCommon.isVoid(pna.getTransactionStatus())
						|| pna.getTransactionStatus().equals("F")) {
						setAjaxLineStatusCodeMsg("-P1-Error getting pricing detail: Transaction Failure");
					}
					if (pna.getHeaderStatusCode() != null
						&& !pna.getHeaderStatusCode().equalsIgnoreCase("00")) {
						setAjaxLineStatusCodeMsg("-P2-Error getting pricing detail: HeaderStatusCode Error");
					}
				}
				 */
				
				pnaHoverMap = XPEDXPriceandAvailabilityUtil.getPnAHoverMap(pna.getItems(),true);
				orderMultipleMapFromSourcing = XPEDXPriceandAvailabilityUtil.getOrderMultipleMapFromSourcing(pna.getItems(),true);
				useOrderMultipleMapFromSourcing = XPEDXPriceandAvailabilityUtil.useOrderMultipleErrorMapFromMax(pna.getItems());
				//Setting the price hover map
				//added for jira 2885 
				if(pna.getHeaderStatusCode() != null && pna.getHeaderStatusCode().equalsIgnoreCase("00")){
					pnALineErrorMessage=XPEDXPriceandAvailabilityUtil.getLineErrorMessageMap(pna.getItems());
				}
				
				//setPriceHoverMap(XPEDXPriceandAvailabilityUtil.getPricingInfoFromItemDetails(pna.getItems(), wcContext,true,lineTpeMDoc.getDocumentElement()));
					setPriceHoverMap(XPEDXPriceandAvailabilityUtil.getPricingInfoFromItemDetails(pna.getItems(), wcContext,true,lineTpeMDoc.getDocumentElement(),true, getOutputDocument()));
				// END P&A Call: RUgrani
				//processPandA(pna.getItems());
				getCustomerDetails();
			}
			if(getPriceHoverMap()!=null)
				LOG.debug("PRICEHOVERMAP KEYS in XPEDXDraftOrderDetails class  : "+getPriceHoverMap());
			/*setDivisionName(XPEDXWCUtils.getDivisionName());
			setState(XPEDXWCUtils.getState());
			This is removed and the same function is implemented in setDivsionAndState method
			 */
			//String shipFromBranch = (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH,WCAttributeScope.LOCAL_SESSION);
			//String envCode =(String)wcContext.getWCAttribute(XPEDXConstants.ENVIRONMENT_CODE,WCAttributeScope.LOCAL_SESSION);
			custStatus = shipToCustomer.getCustomerStatus();
			String shipFromBranch = shipToCustomer.getExtnShipFromBranch();
			String envCode =shipToCustomer.getExtnEnvironmentCode();
			setDivsionAndState(shipFromBranch,envCode);
			setInventoryMap(xPEDXWCUtils.getInventoryCheckMap(getOutputDocument(), shipFromBranch, getWCContext()));
			//added for Jira 1624
			setSkuTypeList(XPEDXWCUtils.getSkuTypesForQuickAdd(getWCContext()));			
			/*if("true".equals(isEditOrder))
			{
				setEditOrdersMap(orderHeaderKey);
			}*/
			
			XPEDXWCUtils.releaseEnv(wcContext);
		} catch (Exception ex) {
			if (ex != null && ex.toString() != null
					&& ex.toString().contains("YFC0101")) {
				this.execute();
			}
//			XPEDXWCUtils.logExceptionIntoCent(ex.getMessage());
			LOG.error("Error in Draft Order Details action class : "+ex.getMessage());
		}
		return SUCCESS;
	}
	
	
	private void checkforEntitlement(){
		//Added for JIRA 3523
		String item = "";
		ArrayList<String> entlErrorList = new ArrayList<String>();
		//Document entitledItemsDoc;
		try {
			// Removing call to getXpedxEntitledItemDetails for performance fix. JIRA 4020
			//entitledItemsDoc = XPEDXOrderUtils.getXpedxEntitledItemDetails(allItemIds, wcContext.getCustomerId(), wcContext.getStorefrontId(), wcContext,"xpedxEntitledCheckOnly");
		
		Iterator productIDIter = allItemIds.iterator();
		ArrayList<Element> itemlist = new ArrayList<Element>(); 
		allItemID = new ArrayList<String>();
		allItemIDsWithDuplicates = new ArrayList<String>();
		Iterator<Element> it=getMajorLineElements().iterator();
		int lineCount = 0;
		String firstItem = "";
		String firstItemCategoryPath = "";
		String OrganizationCodeForFirstItem = "";
		while(it.hasNext())
		{
			
			Element orderLineElem=it.next();			
			Element itemElement = (Element)orderLineElem.getElementsByTagName("Item").item(0);
			Element itemDetailEle = SCXmlUtil.getChildElement(orderLineElem, "ItemDetails");
			String itemId = itemElement.getAttribute("ItemID");
			if ( lineCount == 0 && itemDetailEle != null ) {
				Element catlistEle = SCXmlUtil.getChildElement(itemDetailEle, "CategoryList");
				Element catElement = (Element)catlistEle.getElementsByTagName("Category").item(0);			
				firstItemCategoryPath = catElement.getAttribute("CategoryPath");
				OrganizationCodeForFirstItem = catElement.getAttribute("OrganizationCode");
				firstItem = itemId;
				lineCount++;
			}
			String lineType=orderLineElem.getAttribute("LineType");
			if(!"M".equals(lineType) &&  !"C".equals(lineType) && !allItemID.contains(itemId))
			{
				
				allItemID.add(itemId);
			}// Added for XB-689 
			else if(!"M".equals(lineType) &&  !"C".equals(lineType) && !allItemIDsWithDuplicates.contains(itemId)){
				allItemIDsWithDuplicates.add(itemId);
			}//End of XB-689
		}
		if(!("").equals(firstItem) && !("").equals(OrganizationCodeForFirstItem) && !("").equals(firstItemCategoryPath)) {
			setAdjCatTwoShortDesc(XPEDXWCUtils.getCatTwoDescFromItemIdForpath(firstItem, OrganizationCodeForFirstItem, firstItemCategoryPath));
		}
		
		if(entitledItemEleList != null) {
			 //itemlist  = getXMLUtils().getElements(entitledItemsDoc.getDocumentElement(), "//Item");
			 itemlist  = entitledItemEleList;
		}
		
		if(itemlist.size() == 0){
				for(int i=0; i<allItemID.size();i++) {
					entlErrorList.add(allItemID.get(i));
				}	
				if(entlErrorList.size() == 0) {
					erroMsg = "";
					
				} else {
					if(entlErrorList.size()> 1){
						Iterator itr = entlErrorList.iterator();
						String strVal="";
						while(itr.hasNext())
						{
							erroMsg+= itr.next().toString()+",";

						}
						int lastIndex = erroMsg.lastIndexOf(",");
						erroMsg = erroMsg.substring(0,lastIndex);

					}
					else{
						erroMsg=entlErrorList.get(0);
					}					
				}
		}
		else{
		for(int i=0;i<itemlist.size();i++){	
			HashMap<String, String> itemSkuMap = new LinkedHashMap<String, String>();
			Element itemElem=itemlist.get(i);
			String itemId = itemlist.get(i).getAttribute("ItemID");
			itemList.add(itemId);
			//Added for performance issue 
			if(itemElem!=null)
			{
				Element primeInfoElem = XMLUtilities.getElement(itemElem, "PrimaryInformation");
				if(primeInfoElem!=null)
				{
					String manufactureItem = primeInfoElem.getAttribute("ManufacturerItem");
					if(manufactureItem!=null && manufactureItem.length()>0)
						itemSkuMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_MANUFACTURER_ITEM, manufactureItem);
				}
				Element extnElem = XMLUtilities.getElement(itemElem, "Extn");
				if(extnElem!=null)
				{
					String mpcCode = extnElem.getAttribute("ExtnMpc");
					if(mpcCode!=null && mpcCode.length()>0)
						itemSkuMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_MPC_ITEM, mpcCode);
				}
			}
			skuMap.put(itemId, itemSkuMap);
			//
		}	
			for(int i=0; i<allItemID.size();i++) {
				
				if(!itemList.contains(allItemID.get(i))){
					entlErrorList.add(allItemID.get(i));
				}
			}
				 if(entlErrorList != null && entlErrorList.size() > 0){
				if(entlErrorList.size()> 1){
					Iterator itr = entlErrorList.iterator();
					String strVal="";
					while(itr.hasNext())
					{
						erroMsg+= itr.next().toString()+",";

					}
					int lastIndex = erroMsg.lastIndexOf(",");
					erroMsg = erroMsg.substring(0,lastIndex);

				}
				else {
					erroMsg=entlErrorList.get(0);
				}
				}
			
		}	
	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
	//End of JIRA 3523
	}
	
	
	private void changeCurrentCartOwner() {
		String orderHeaderKey = XPEDXCommerceContextHelper
				.getCartInContextOrderHeaderKey(getWCContext());
		if (orderHeaderKey != null) {
			LOG.debug("Changing the owner of the order");
			XPEDXWCUtils.changeOrderOwnerToSelectedCustomer(getWCContext(),
					orderHeaderKey);
			LOG.debug("Changed the cart owner");
		}
	}

public void resetOrganizationValuesForShipToCustomer(){
		XPEDXShipToCustomer shipToCustomer = (XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		shipToCustomer.setShipToOrgExtnMinOrderAmt(null);
		shipToCustomer.setShipToOrgExtnSmallOrderFee(null);
		shipToCustomer.setShipToOrgOrganizationName(null);
		shipToCustomer.setShipToOrgCorporatePersonInfoState(null);
		shipToCustomer.setShipToDivDeliveryCutOffTime(null);
		//Added For Jira 3465
		shipToCustomer.setShipToDivdeliveryInfo(null);
		shipToCustomer.setShipToOrgExtnApplyMinOrderCharge(null);
		XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER, shipToCustomer);
}

public void setCurrentCustomerIntoContext(String ordersBuyerOrganizationCode) throws CannotBuildInputException {
		//added for jira 4306	
		XPEDXWCUtils.setObectInCache("SHIPTO_BEFORE_EDIT_ORDER",getWCContext().getCustomerId() );
		XPEDXWCUtils.setObectInCache("CUSTOMER_CONTACT_ID_BEFORE_EDIT_ORDER",wcContext.getCustomerContactId());
		//end for jira 4306
		String selectedCustomerContactId = getWCContext().getSCUIContext().getRequest().getParameter("selectedCustomerContactId");
		if(SCUtil.isVoid(selectedCustomerContactId))
			selectedCustomerContactId = wcContext.getCustomerContactId();
		String  contaxtCustomerContactID = wcContext.getCustomerContactId();
		
		XPEDXWCUtils.setObectInCache(XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT, "true");
        resetOrganizationValuesForShipToCustomer();
		if(contaxtCustomerContactID.equals(selectedCustomerContactId) ){
			
			XPEDXWCUtils.setCurrentCustomerIntoContext(
					ordersBuyerOrganizationCode, getWCContext());
			
				getWCContext().getSCUIContext().getSession().removeAttribute(
						XPEDXWCUtils.XPEDX_SHIP_TO_ADDRESS_OVERIDDEN);
			//added for jira 4306
			if(!"true".equals(isEditOrder))
				changeCurrentCartOwner();
			
		}
		
			/* commented for jira 4306 
			try {
				setSelectedShipToAsDefault(ordersBuyerOrganizationCode);
			} catch (Exception e) {
				LOG.error("Cannot set the customer as default. please try again later");
				e.printStackTrace();
			}*/
		
			
	}

public void setSelectedShipToAsDefault(String selectedCustomerID) throws CannotBuildInputException,
	InstantiationException, IllegalAccessException,
	YIFClientCreationException, YFSException, RemoteException {
	ISCUITransactionContext scuiTransactionContext = null;
	YFSEnvironment env = null;
	SCUIContext wSCUIContext = null;
	String selectedCustomerContactId = "";
	try {
	if (XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext)) {
		
		
		selectedCustomerContactId = wcContext.getSCUIContext().getRequest().getParameter("selectedCustomerContactId");
		if(!(selectedCustomerContactId != null && selectedCustomerContactId.trim().length()>0)){
			selectedCustomerContactId = wcContext.getCustomerContactId();
		}
			
		
		if(SCUtil.isVoid(selectedCustomerID))
		{
			selectedCustomerID=wcContext.getCustomerId();
		}
		
		String inputXml = "<Customer CustomerID='"
				+ XPEDXWCUtils
						.getLoggedInCustomerFromSession(wcContext)
				+ "' " + "OrganizationCode='"
				+ wcContext.getStorefrontId() + "'> "
				+ "<CustomerContactList>"
				+ "<CustomerContact CustomerContactID='"
				+ selectedCustomerContactId + "'> "
				+ "<Extn ExtnDefaultShipTo='"
				+ selectedCustomerID + "' /> "
				+ "</CustomerContact> " + "</CustomerContactList>"
				+ "</Customer> ";
		Document document = getXMLUtils().createFromString(inputXml);
		wSCUIContext = wcContext.getSCUIContext();
		scuiTransactionContext = wSCUIContext
				.getTransactionContext(true);
		env = (YFSEnvironment) scuiTransactionContext
				.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
		YIFApi api = YIFClientFactory.getInstance().getApi();
		Document outputListDocument = api.invoke(env, "manageCustomer",
				document);
		LOG.debug(SCXmlUtil.getString(outputListDocument));
	}
	}
	catch(Exception ex){
		LOG.debug(ex);
	}
	finally {
	SCUITransactionContextHelper.releaseTransactionContext(
			scuiTransactionContext, wSCUIContext);
	scuiTransactionContext = null;
	env = null;
	}
	}
	
	private void setDivsionAndState(String shipFromBranch, String envCode) {
		
		if(shipToCustomer== null){
			LOG.error("shipToCustomer object from session is null... Creating the Object and Putting it in the session");
			
			try {
				XPEDXWCUtils.setCustomerObjectInCache(XPEDXWCUtils
						.getCustomerDetails(getWCContext().getCustomerId(),
								getWCContext().getStorefrontId())
						.getDocumentElement());
			} catch (CannotBuildInputException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		}
		
		try {
			if(shipToCustomer.getShipToOrgOrganizationName() == null && shipToCustomer.getShipToOrgCorporatePersonInfoState() ==  null &&
				shipToCustomer.getShipToOrgExtnMinOrderAmt() == null && shipToCustomer.getShipToOrgExtnSmallOrderFee() == null){
				if (shipFromBranch != null && shipFromBranch.trim().length() > 0) {
					Document organizationDetails = null;
					if (envCode != null && envCode.trim().length() > 0) {
						organizationDetails = XPEDXWCUtils.getOrganizationDetails(shipFromBranch
								+ "_" + envCode);
					} else {
						organizationDetails = XPEDXWCUtils.getOrganizationDetails(shipFromBranch);
					}
					shipToCustomer.setShipToOrgOrganizationName(SCXmlUtil.getXpathAttribute(organizationDetails.getDocumentElement(), "/OrganizationList/Organization/@OrganizationName"));
					shipToCustomer.setShipToOrgCorporatePersonInfoState(SCXmlUtil.getXpathAttribute(organizationDetails.getDocumentElement(), "/OrganizationList/Organization/CorporatePersonInfo/@State"));
					shipToCustomer.setShipToOrgExtnMinOrderAmt(SCXmlUtil.getXpathAttribute(organizationDetails.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnMinOrderAmt"));
					shipToCustomer.setShipToOrgExtnSmallOrderFee(SCXmlUtil.getXpathAttribute(organizationDetails.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnSmallOrderFee"));
					shipToCustomer.setShipToDivDeliveryCutOffTime(SCXmlUtil.getXpathAttribute(organizationDetails.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnDeliveryCutOffTime"));
					shipToCustomer.setShipToOrgExtnApplyMinOrderCharge(SCXmlUtil.getXpathAttribute(organizationDetails.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnApplyMinOrderCharge"));
					//Added For Jira 3465
					shipToCustomer.setShipToDivdeliveryInfo(SCXmlUtil.getXpathAttribute(organizationDetails.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnDeliveryInfo"));
					XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER, shipToCustomer);
				}
			}
			setDivisionName(shipToCustomer.getShipToOrgOrganizationName());
			setState(shipToCustomer.getShipToOrgCorporatePersonInfoState());
		}catch (Exception e) {
			//oops some error getting the Organization Details
			LOG.error("Error getting the Division Name and State for the ship from brach"+ shipFromBranch+"_"+envCode);
			e.printStackTrace();
		}
	}
	
/*	private void setDivsionAndState(String shipFromBranch, String envCode) {
		String divisionName;
		String state;
		try {
			if (shipFromBranch != null && shipFromBranch.trim().length() > 0) {
				Document organizationDetails = null;
				if (envCode != null && envCode.trim().length() > 0) {
					organizationDetails = XPEDXWCUtils.getOrganizationDetails(shipFromBranch
							+ "_" + envCode);
				} else {
					organizationDetails = XPEDXWCUtils.getOrganizationDetails(shipFromBranch);
				}
				divisionName = SCXmlUtil.getXpathAttribute(organizationDetails.getDocumentElement(),
						"/OrganizationList/Organization/@OrganizationName");
				setDivisionName(divisionName);
				state = SCXmlUtil.getXpathAttribute(organizationDetails.getDocumentElement(),
						"/OrganizationList/Organization/CorporatePersonInfo/@State");
				setState(state);
			}
		} catch (Exception e) {
			//oops some error getting the Organization Details
			LOG.error("Error getting the Division Name and State for the ship from brach"+ shipFromBranch+"_"+envCode);
			e.printStackTrace();
		}
	}
*/
	/*private boolean isNewLine(ArrayList<Element> majorLineElements)
	{
		boolean newLine=false;
		//List<Element> orderLines=SCXmlUtil.getElements(majorElements, "/OrderLines/OrderLine");
		for(int i=0;i<majorLineElements.size();i++)
		{
			Element orderLine=majorLineElements.get(i);
			Element extnElem=(Element)orderLine.getElementsByTagName("Extn").item(0);
			String extnEditOrderFlag=extnElem.getAttribute("ExtnEditOrderFlag");
			if("Y".equals(extnEditOrderFlag))
			{
				newLine=true;
				break;
			}
		}
		return newLine; 
	}*/
	private boolean setPnaHoverForEditOrderLine(XPEDXPriceAndAvailability pna,Document lineTpeMDoc)
	{
		boolean newLine=false;
		try
		{
			
			/*if("true".equals(isEditOrder))
			{*/
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
					if("M".equals(lineType))
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
						
						
							Element lineTypeMElem=SCXmlUtil.createChild(lineTypeElem, "Item");
							lineTypeMElem.setAttribute("ItemID", itemElem.getAttribute("ItemID"));
							lineTypeMElem.setAttribute("UnitOfMeasure", extnElem.getAttribute("ExtnPricingUOM"));
							Element primaryInfo=SCXmlUtil.createChild(lineTypeMElem, "PrimaryInformation");
							primaryInfo.setAttribute("MinOrderQuantity", "1");
							primaryInfo.setAttribute("PricingUOM", extnElem.getAttribute("ExtnPricingUOM"));
							primaryInfo.setAttribute("PricingQuantityConvFactor", "1");
						//}
						pna.getItems().add(item);
					}
					if("Y".equals(extnEditOrderFlag))
						newLine=true;
				}
			//}
		}
		catch(Exception e)
		{
			LOG.error("Error setting the price for existing user during edititng cart "+e.getMessage());
		}
		return newLine;
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
	
	private void callChangeOrder()
	{
		try
		{
			String editedOrderHeaderKey = XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
			if("true".equals(isEditOrder) && YFCCommon.isVoid(editedOrderHeaderKey))
			{
				XPEDXWCUtils.setEditedOrderHeaderKeyInSession(wcContext, orderHeaderKey);
				editedOrderHeaderKey=orderHeaderKey;
			}			
			if(YFCCommon.isVoid(editedOrderHeaderKey)){
				draftOrderFlag="Y";	
			}
			else {
				draftOrderFlag="N";	
			}
			LOG.debug("Calling Change Order");
			Map<String, String> valueMap1 = new HashMap<String, String>();
			valueMap1.put("/Order/@OrderHeaderKey", orderHeaderKey);
			valueMap1.put("/Order/@DraftOrderFlag", draftOrderFlag);
			if("true".equals(isEditOrder)) {
				valueMap1.put("/Order/PendingChanges/@RecordPendingChanges", "Y");
			} else {
				valueMap1.put("/Order/PendingChanges/@RecordPendingChanges", "N");
			}
			
			Element input1;
			Element orderElem = null;
			
			setYFSEnvironmentVariables();
			input1 = WCMashupHelper.getMashupInput("XPEDXCallChangeOrder",
					valueMap1, wcContext.getSCUIContext());
			Object obj1 = WCMashupHelper.invokeMashup("XPEDXCallChangeOrder",
						input1, wcContext.getSCUIContext());
			
			setOutputDocument(((Element)obj1).getOwnerDocument());

			//orderElem = ((Element) obj1).getOwnerDocument().getDocumentElement();
			
			/*if("true".equals(isEditOrder))
			{
				input1 = WCMashupHelper.getMashupInput("xpedx_get_completeorderList",
						valueMap1, wcContext.getSCUIContext());
				Object obj1 = WCMashupHelper.invokeMashup("xpedx_get_completeorderList",
							input1, wcContext.getSCUIContext());
	
				orderElem = ((Element) obj1).getOwnerDocument().getDocumentElement();
			}
			else
			{
				input1 = WCMashupHelper.getMashupInput("xpedx_get_orderList",
						valueMap1, wcContext.getSCUIContext());
				Object obj1 = WCMashupHelper.invokeMashup("xpedx_get_orderList",
							input1, wcContext.getSCUIContext());
	
				Element orderListElem = ((Element) obj1).getOwnerDocument().getDocumentElement();
				if(orderListElem != null)
				{
					orderElem=(Element)orderListElem.getElementsByTagName("Order").item(0);
				}
			}
			UtilBean util=new UtilBean();
			Document inputDocument =SCXmlUtil.createDocument("Order");
			Element orderChangeElem=inputDocument.getDocumentElement();
			Element orderExtn=util.getElement(orderElem, "Extn");
			
			orderChangeElem.setAttribute("OrderHeaderKey", orderElem.getAttribute("OrderHeaderKey"));
			if(!YFCCommon.isVoid(orderElem.getAttribute("OrderName")))
			{
				orderChangeElem.setAttribute("OrderName", orderElem.getAttribute("OrderName"));
			}
			else
			{
				orderChangeElem.setAttribute("OrderName", orderExtn.getAttribute("ExtnLegacyOrderNo"));
			}
			Element orderExtnChanegElem=SCXmlUtil.createChild(orderChangeElem, "Extn");
			orderExtnChanegElem.setAttribute("ExtnOrderDesc", orderExtn.getAttribute("ExtnOrderDesc"));
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
					orderLineChanegElem.setAttribute("OrderedQty", orderLineEle.getAttribute("OrderedQty"));
					
					Element orderLineExtnChange=SCXmlUtil.createChild(orderLineChanegElem, "Extn");
					Element orderLineExtn=util.getElement(orderLineEle, "Extn");
					orderLineExtnChange.setAttribute("ExtnCustLineAccNo", orderLineExtn.getAttribute("ExtnCustLineAccNo"));
					orderLineExtnChange.setAttribute("ExtnCustLineField1", orderLineExtn.getAttribute("ExtnCustLineField1"));
					orderLineExtnChange.setAttribute("ExtnCustLineField2", orderLineExtn.getAttribute("ExtnCustLineField2"));
					orderLineExtnChange.setAttribute("ExtnCustLineField3", orderLineExtn.getAttribute("ExtnCustLineField3"));
					orderLineExtnChange.setAttribute("ExtnCustomerLinePONo", orderLineExtn.getAttribute("ExtnCustomerLinePONo"));
					orderLineExtnChange.setAttribute("ExtnCustomerPONo", orderLineExtn.getAttribute("ExtnCustomerPONo"));
					orderLineExtnChange.setAttribute("ExtnLineOrderedTotal", orderLineExtn.getAttribute("ExtnLineOrderedTotal"));
					orderLineExtnChange.setAttribute("ExtnReqUOMUnitPrice", orderLineExtn.getAttribute("ExtnReqUOMUnitPrice"));
					orderLineExtnChange.setAttribute("ExtnUnitPrice", orderLineExtn.getAttribute("ExtnUnitPrice"));
					
					Element orderLineTranChange=SCXmlUtil.createChild(orderLineChanegElem, "OrderLineTranQuantity");
					Element orderLineTran=util.getElement(orderLineEle, "OrderLineTranQuantity");
					orderLineTranChange.setAttribute("OrderedQty", orderLineTran.getAttribute("OrderedQty"));
					orderLineTranChange.setAttribute("TransactionalUOM",  orderLineTran.getAttribute("TransactionalUOM"));
					
				}
				LOG.debug("Input XML = "+SCXmlUtil.getString(inputDocument) );
				setYFSEnvironmentVariables();
				WCMashupHelper.invokeMashup("xpedx_me_changeOrderLineDetails", inputDocument.getDocumentElement(), wcContext.getSCUIContext());*/
			//}
		}
		catch(XMLExceptionWrapper e)
        {
              YFCElement errorXML=e.getXML();
              YFCElement errorElement=(YFCElement)errorXML.getElementsByTagName("Error").item(0);
              String errorDeasc=errorElement.getAttribute("ErrorDescription");
              if(errorDeasc.contains("Key Fields cannot be modified."))
              {
                    YFCNodeList listAttribute=errorElement.getElementsByTagName("Attribute");
                    for(int i=0;i<listAttribute.getLength();i++)
                    {
                          YFCElement attributeELement=(YFCElement)listAttribute.item(i);
                          String value=attributeELement.getAttribute("Value");
                          if("DraftOrderFlag".equals(value))
                          {
                                draftOrderFail = "true";
                                break;
                          }
                    }
              }
              YFCNodeList<YFCElement> errorNodeList=errorXML.getElementsByTagName("Error");
    			for(YFCElement errorEle:errorNodeList)
    			{
    				String errorCode=errorEle.getAttribute("ErrorCode");
    				if(XPEDXConstants.UE_ERROR_CODE.equalsIgnoreCase(errorCode) || XPEDXConstants.UE_ERROR_CODE1.equalsIgnoreCase(errorCode))
    				{
    					isOUErrorPage=true;
    					break;
    				}
    			}
        }
		catch(YFCException e)
		{
			YFCElement errorXML=e.getXML().getDocumentElement();
			YFCNodeList<YFCElement> errorNodeList=errorXML.getElementsByTagName("Error");
			for(YFCElement errorEle:errorNodeList)
			{
				String errorCode=errorEle.getAttribute("ErrorCode");
				if(XPEDXConstants.UE_ERROR_CODE.equalsIgnoreCase(errorCode) || XPEDXConstants.UE_ERROR_CODE1.equalsIgnoreCase(errorCode))
				{
					isOUErrorPage=true;
					break;
				}
			}
		}
		catch(Exception e)
		{
			LOG.error("Error while calling change order to Update the price from PNA");
		}
		XPEDXWCUtils.releaseEnv(wcContext);
	}
	private void getCustomerDetails()
	throws RemoteException {
		ISCUITransactionContext scuiTransactionContext=null;
		try
		{
			//Form the input
			if(wcContext.getCustomerId()!=null) {
/*				
				YFCDocument inputCustomerDoc = YFCDocument.createDocument("Customer");
				YFCElement inputCustomerElement = inputCustomerDoc.getDocumentElement();
				inputCustomerElement.setAttribute("CustomerID", wcContext.getCustomerId());
				//form the template
				YFCDocument customerListTemplate = YFCDocument.createDocument("CustomerList");
				YFCElement customerListTemplateElement = customerListTemplate.getDocumentElement();
				YFCElement customerTemplateElement = customerListTemplate.createElement("Customer");
				customerListTemplateElement.appendChild(customerTemplateElement);			
				YFCElement extnTemplateElement = customerListTemplate.createElement("Extn");			
				customerTemplateElement.appendChild(extnTemplateElement);
				YFCElement parentCustomerTemplateElement = customerListTemplate.createElement("ParentCustomer");
				YFCElement parentCustomerExtnTemplateElement = customerListTemplate.createElement("Extn");
				parentCustomerTemplateElement.appendChild(parentCustomerExtnTemplateElement);
				
				customerTemplateElement.appendChild(parentCustomerTemplateElement);
				//invoke a getCustomerList
				//env.setApiTemplate("getCustomerList", customerListTemplate);
				YFCElement inputCustDoc=inputCustomerDoc.getDocumentElement();
				scuiTransactionContext  = wcContext.getSCUIContext().getTransactionContext(true);
				YFCElement customerList=SCUIPlatformUtils.invokeXAPI(
						"getCustomerList", inputCustomerDoc.getDocumentElement(), customerListTemplate
								.getDocumentElement(), wcContext.getSCUIContext());
				//Document customerList = api.invoke(env, "getCustomerList", inputCustomerDoc);
				
				//env.clearApiTemplate("getCustomerList");
				if(customerList!=null){
					YFCNodeList customerNodeList = customerList.getElementsByTagName("Customer");
					int customerLength = customerNodeList.getLength();
					if(customerLength > 0)
					{
						YFCElement customerElement = (YFCElement)customerNodeList.item(0);
						YFCElement extnElement = customerElement.getElementsByTagName("Extn").item(0);
						String minOrderAmountStr=extnElement.getAttribute("ExtnMinOrderAmount");
						String chargeAmountStr=extnElement.getAttribute("ExtnMinChargeAmount");
						if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr)))  &&
								(!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) ))
						{
							minOrderAmount = Float.parseFloat(minOrderAmountStr);	
							if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
							{
								chargeAmount = Float.parseFloat(chargeAmountStr);
							}
						}
						else
						{
							if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
							{
								chargeAmount = Float.parseFloat(chargeAmountStr);
							}
							YFCElement parentElement = customerElement.getElementsByTagName("ParentCustomer").item(0);
							if(parentElement != null )
							{
								YFCElement extnParentElement = parentElement.getElementsByTagName("Extn").item(0);
								minOrderAmountStr=extnParentElement.getAttribute("ExtnMinOrderAmount");
								chargeAmountStr=extnParentElement.getAttribute("ExtnMinChargeAmount");
								if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr))) &&
										(!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) ))
								{
									minOrderAmount = Float.parseFloat(minOrderAmountStr);	
									if(chargeAmount <=0 && chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
									{
											chargeAmount = Float.parseFloat(chargeAmountStr);
									}
								}
								else
								{
									if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)) && chargeAmount <=0)
									{
										chargeAmount = Float.parseFloat(chargeAmountStr);
									}
									//String shipFromDivision =(String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH,WCAttributeScope.LOCAL_SESSION);
									//String envCode =(String)wcContext.getWCAttribute(XPEDXConstants.ENVIRONMENT_CODE,WCAttributeScope.LOCAL_SESSION);
									String shipFromBranch = shipToCustomer.getExtnShipFromBranch();
									String envCode =shipToCustomer.getExtnEnvironmentCode();
									Map<String, String> valueMap = new HashMap<String, String>();
									if(envCode!=null && envCode.trim().length()>0){
										valueMap.put("/Organization/@OrganizationCode", shipFromDivision+"_"+envCode);
									}else{
										LOG.error("EnvCode is NULL. Returning back to the caller.");
										return;
									}
									try {
										Element input = WCMashupHelper.getMashupInput("XPEDXGetShipOrgNodeDetails", valueMap, getWCContext().getSCUIContext());
										Object obj = WCMashupHelper.invokeMashup("XPEDXGetShipOrgNodeDetails", input, getWCContext().getSCUIContext());
										Document outputDoc = ((Element) obj).getOwnerDocument();
										
										if(YFCCommon.isVoid(outputDoc)){
											LOG.error("No DB record exist for Node "+ shipFromDivision+"_"+envCode+". ");
											return;
										}
										
										minOrderAmountStr = SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnMinOrderAmt");
										chargeAmountStr= SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnSmallOrderFee");
										if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr))) &&
												(!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) ))
										{
											minOrderAmount = Float.parseFloat(minOrderAmountStr);				
											if( chargeAmount <=0 && chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
											{
													chargeAmount = Float.parseFloat(chargeAmountStr);
											}
										}
										
									} catch (CannotBuildInputException e) {
										LOG.error("Unable to get XPEDXGetShipOrgNodeDetails for "+ shipFromDivision+"_"+envCode+". ",e);
										return;
									}
								}
							}
						}
					}
				}//if customerList is not null
*/
				
				
				//JIRA 3488 start
				String maxOrderAmountStr=xpedxCustomerContactInfoBean.getExtnmaxOrderAmount();
				if(maxOrderAmountStr != null && (!("".equals(maxOrderAmountStr)))  &&
						Float.parseFloat(maxOrderAmountStr)>0)
				{
					setMaxOrderAmount(Float.parseFloat(maxOrderAmountStr));	
				
				}
				else {
					XPEDXShipToCustomer billToElement = shipToCustomer.getBillTo();
					if(billToElement != null )
					{
						maxOrderAmountStr=billToElement.getExtnMaxOrderAmount();
						if(maxOrderAmountStr != null && (!("".equals(maxOrderAmountStr))) &&
								Float.parseFloat(maxOrderAmountStr)>0)
						{
							setMaxOrderAmount(Float.parseFloat(maxOrderAmountStr));	
				}
					}
				}
				//JIRA 3488 end
						if(shipToCustomer== null){
							LOG.error("shipToCustomer object from session is null... Creating the Object and Putting it in the session");
							
							XPEDXWCUtils.setCustomerObjectInCache(XPEDXWCUtils
									.getCustomerDetails(getWCContext().getCustomerId(),
											getWCContext().getStorefrontId())
									.getDocumentElement());
							shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
						}
				String minOrderAmountStr=shipToCustomer.getExtnMinOrderAmount();
				String chargeAmountStr=shipToCustomer.getExtnMinChargeAmount();;
						if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr)))  &&
								(!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) ))
						{
							minOrderAmount = Float.parseFloat(minOrderAmountStr);	
							if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
							{
								chargeAmount = Float.parseFloat(chargeAmountStr);
							}
						}
						else
						{
							if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
							{
								chargeAmount = Float.parseFloat(chargeAmountStr);
							}
							XPEDXShipToCustomer parentElement = shipToCustomer.getBillTo();
							if(parentElement != null )
							{
								minOrderAmountStr=parentElement.getExtnMinOrderAmount();
								chargeAmountStr=parentElement.getExtnMinChargeAmount();
								if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr))) &&
										(!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) ))
								{
									minOrderAmount = Float.parseFloat(minOrderAmountStr);	
									if(chargeAmount <=0 && chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
									{
											chargeAmount = Float.parseFloat(chargeAmountStr);
									}
								}
								else
								{
									if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)) && chargeAmount <=0)
									{
										chargeAmount = Float.parseFloat(chargeAmountStr);
									}
									//String shipFromDivision =(String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH,WCAttributeScope.LOCAL_SESSION);
									//String envCode =(String)wcContext.getWCAttribute(XPEDXConstants.ENVIRONMENT_CODE,WCAttributeScope.LOCAL_SESSION);
									if(shipToCustomer.getShipToOrgExtnMinOrderAmt() == null && shipToCustomer.getShipToOrgExtnSmallOrderFee() == null &&
											shipToCustomer.getShipToOrgOrganizationName() == null && shipToCustomer.getShipToOrgCorporatePersonInfoState() == null){
										shipFromDivision = shipToCustomer.getExtnShipFromBranch();
										String envCode =shipToCustomer.getExtnEnvironmentCode();
										Map<String, String> valueMap = new HashMap<String, String>();
										if(envCode!=null && envCode.trim().length()>0){
											valueMap.put("/Organization/@OrganizationCode", shipFromDivision+"_"+envCode);
										}else{
											LOG.error("EnvCode is NULL. Returning back to the caller.");
											return;
										}
										try {
											Element input = WCMashupHelper.getMashupInput("XPEDXGetShipOrgNodeDetails", valueMap, getWCContext().getSCUIContext());
											Object obj = WCMashupHelper.invokeMashup("XPEDXGetShipOrgNodeDetails", input, getWCContext().getSCUIContext());
											Document outputDoc = ((Element) obj).getOwnerDocument();
											
											if(YFCCommon.isVoid(outputDoc)){
												LOG.error("No DB record exist for Node "+ shipFromDivision+"_"+envCode+". ");
												return;
											}
											shipToCustomer.setShipToOrgExtnMinOrderAmt(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnMinOrderAmt"));
											shipToCustomer.setShipToOrgExtnSmallOrderFee(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnSmallOrderFee"));
											shipToCustomer.setShipToOrgOrganizationName(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/@OrganizationName"));
											shipToCustomer.setShipToOrgCorporatePersonInfoState(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/CorporatePersonInfo/@State"));
											shipToCustomer.setShipToDivDeliveryCutOffTime(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnDeliveryCutOffTime"));
											//Added For Jira 3465
											shipToCustomer.setShipToDivdeliveryInfo(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnDeliveryInfo"));
											shipToCustomer.setShipToOrgExtnApplyMinOrderCharge(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnApplyMinOrderCharge"));
											XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER, shipToCustomer);
										} catch (CannotBuildInputException e) {
											LOG.error("Unable to get XPEDXGetShipOrgNodeDetails for "+ shipFromDivision+"_"+envCode+". ",e);
											return;
										}
									}
									minOrderAmountStr = shipToCustomer.getShipToOrgExtnMinOrderAmt();
									chargeAmountStr= shipToCustomer.getShipToOrgExtnSmallOrderFee();
									if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr))) &&
											(!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) ))
									{
										minOrderAmount = Float.parseFloat(minOrderAmountStr);				
										if( chargeAmount <=0 && chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
										{
												chargeAmount = Float.parseFloat(chargeAmountStr);
									}
								}
							}
						}
				}
			}// if customerId is not null

			
	} catch (Exception ex) {
		//log.error(ex.getMessage());
		scuiTransactionContext.rollback();
	} finally {
		if (scuiTransactionContext != null) {
			SCUITransactionContextHelper.releaseTransactionContext(
					scuiTransactionContext, wcContext.getSCUIContext());
		}
	}
			
	}
	
	public String getAjaxLineStatusCodeMsg() {
		return ajaxLineStatusCodeMsg;
	}

	public void setAjaxLineStatusCodeMsg(String ajaxLineStatusCodeMsg) {
		this.ajaxLineStatusCodeMsg = ajaxLineStatusCodeMsg;
	}

	private void setDefaultShipToIntoContext() {
		boolean isCustomerSelectedIntoContext = XPEDXWCUtils
				.isCustomerSelectedIntoConext(getWCContext());
		if (!isCustomerSelectedIntoContext) {
			String defaultShipToCustomerId = XPEDXWCUtils.getDefaultShipTo();
			if (defaultShipToCustomerId != null
					&& defaultShipToCustomerId.trim().length() > 0) {
				XPEDXWCUtils.setCurrentCustomerIntoContext(
						defaultShipToCustomerId, getWCContext());
			}
		}
	}

	// BEGIN: Test P&A - TODO: Remove the code after testing - RUgrani
	/**
	 * @return the pnaHoverMap
	 */
	public HashMap<String, JSONObject> getPnaHoverMap() {
		return pnaHoverMap;
	}

	public boolean isOwnerOfNonCartInContextDraftOrder() throws Exception {
		return false;
	}

	/**
	 * @param pnaHoverMap
	 *            the pnaHoverMap to set
	 */
	public void setPnaHoverMap(HashMap<String, JSONObject> pnaHoverMap) {
		this.pnaHoverMap = pnaHoverMap;
	}
	
	// END

	public HashMap<String, XPEDXItemPricingInfo> getPriceHoverMap() {
		return priceHoverMap;
	}
	public void setPriceHoverMap(HashMap<String, XPEDXItemPricingInfo> priceHoverMap) {
		this.priceHoverMap = priceHoverMap;
	}
	
	public XPEDXDraftOrderDetailsAction() {
		super();
		xpedxItemIDUOMToReplacementListMap = new HashMap<String,ArrayList<Element>>();
		//xpedxItemIDUOMToRelatedItemsListMap = new HashMap<String,ArrayList<Element>>();
		xpedxPopularAccessoriesItems =  new ArrayList<Element>();
		xpedxYouMightConsiderItems = new ArrayList<Element>();
		customerFieldsMap = new LinkedHashMap();
		itemUOMsMap = new HashMap();
		itemOrderMultipleMap = new HashMap();
		requiredCustFieldsErrorMap = new HashMap<String, ArrayList<String>>();
	}

	public LinkedHashMap getCustomerFieldsMap() {
		return customerFieldsMap;
	}

	public void setCustomerFieldsMap(LinkedHashMap customerFieldsMap) {
		this.customerFieldsMap = customerFieldsMap;
	}
	public HashMap<String, HashMap<String, String>> getSkuMap() {
		return skuMap;
	}

	public void setSkuMap(HashMap<String, HashMap<String, String>> skuMap) {
		this.skuMap = skuMap;
	}

	public String getCustomerSku() {
		return customerSku;
	}

	public void setCustomerSku(String customerSku) {
		this.customerSku = customerSku;
	}
@Override
	protected void getAndProcessItemDetails() throws Exception {

		//getRelatedItemsForDraftOrder();
		setXPEDXItemAssociation();
		//super.getAndProcessItemDetails();

	}
@Override
	protected void deriveAssocToCountMaps() throws Exception
    {
	
    }
@Override
	 protected void getAndProcessOrderValidationDoc() throws Exception
	 {

	 }
	protected void setXPEDXItemAssociation() throws XPathExpressionException, CannotBuildInputException{
		Set itemIDUOMs = itemIDUOMToOrderLineKeyListMap.keySet();
		Iterator itemIDUOMiter = itemIDUOMs.iterator();
		String divisionNumber = null;
		try {
			//String divisionNumberFromSession = (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH);
			String divisionNumberFromSession = shipToCustomer.getExtnShipFromBranch();
			if(divisionNumberFromSession!=null && divisionNumberFromSession.trim().length()>0)
				divisionNumber = divisionNumberFromSession;
			else
				divisionNumber = getDivisionNumber();
		} catch (CannotBuildInputException e) {
			LOG.error("Error while getting division number for the customer: "+wcContext.getCustomerId(),e);
		}
		ArrayList<String> itemIDList = new ArrayList<String>();
		while (itemIDUOMiter.hasNext()) {
			String itemIDUOM = (String) itemIDUOMiter.next();
			int delimIndex = itemIDUOM.lastIndexOf(":_:");
			itemID = itemIDUOM.substring(0, delimIndex);
			itemIDList.add(itemID);
		}
		if(YFCCommon.isVoid(itemIDList) || itemIDList.size() <=0){
			LOG.error("No items in the cart. No association list could be found.");
			return;
		}
		prepareXPEDXItemAssociation(itemIDList);
	}
	
	protected void prepareXPEDXItemAssociation(ArrayList<String> itemIDList) throws XPathExpressionException, CannotBuildInputException{
		/*prepareXpedxItemAssociationMap(itemIDList);
		prepareXpedxItemBranchItemAssociationMap(itemIDList);*/
		ArrayList<String> youMightConsiderItemIDs = new ArrayList<String>();
		HashMap<String, HashMap<String,ArrayList<Element>>> allAssociatedItemsMap = XPEDXOrderUtils.getXpedxAssociationsForItems(itemIDList, wcContext, true);
		if(allAssociatedItemsMap!=null && !allAssociatedItemsMap.isEmpty())
		{
			if(allAssociatedItemsMap.containsKey(XPEDXOrderUtils.REPLACEMENT_ITEMS_KEY))
				xpedxItemIDUOMToReplacementListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.REPLACEMENT_ITEMS_KEY);
			if(allAssociatedItemsMap.containsKey(XPEDXOrderUtils.ITEM_EXTN_KEY))
				xpedxItemIDToItemExtnMap = allAssociatedItemsMap.get(XPEDXOrderUtils.ITEM_EXTN_KEY);
			HashMap<String,ArrayList<Element>> alternateListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.ALTERNATE_ITEMS_KEY);
			if(alternateListMap!=null && !alternateListMap.isEmpty())
			{
				Set altItemKeys = alternateListMap.keySet();
				if(altItemKeys!=null){
					Iterator<String> altKeyIter = altItemKeys.iterator();
					while(altKeyIter.hasNext())
					{
						String key = altKeyIter.next();
						ArrayList<Element> currentItemAltList = alternateListMap.get(key);
						for(Element altItem: currentItemAltList)
						{
							String altItemID = XMLUtils.getAttributeValue(altItem, "ItemID");							
							if(!SCUtil.isVoid(altItemID) && !youMightConsiderItemIDs.contains(altItemID))
							{
								addToYouMightAlsoConsiderItemList(altItem);
								youMightConsiderItemIDs.add(altItemID);
							}
						}
					}
				}
			}
			HashMap<String,ArrayList<Element>> upgradeListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.UPGRADE_ITEMS_KEY);
			if(upgradeListMap!=null && !upgradeListMap.isEmpty())
			{
				Set upItemKeys = upgradeListMap.keySet();
				if(upItemKeys!=null){
					Iterator<String> upKeyIter = upItemKeys.iterator();
					while(upKeyIter.hasNext())
					{
						String key = upKeyIter.next();
						ArrayList<Element> currentItemUpgradeList = upgradeListMap.get(key);
						for(Element upgradeItem: currentItemUpgradeList)
						{
							String upgradeItemID = XMLUtils.getAttributeValue(upgradeItem, "ItemID");							
							if(!SCUtil.isVoid(upgradeItemID) && !youMightConsiderItemIDs.contains(upgradeItemID))
							{
								addToYouMightAlsoConsiderItemList(upgradeItem);
								youMightConsiderItemIDs.add(upgradeItemID);
							}
						}
					}
				}
			}
			HashMap<String,ArrayList<Element>> upSellListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.UP_SELL_ITEMS_KEY);
			if(upSellListMap!=null && !upSellListMap.isEmpty())
			{
				Set upSellItemKeys = upSellListMap.keySet();
				if(upSellItemKeys!=null){
					Iterator<String> upSellKeyIter = upSellItemKeys.iterator();
					while(upSellKeyIter.hasNext())
					{
						String key = upSellKeyIter.next();
						ArrayList<Element> currentItemUSList = upSellListMap.get(key);
						for(Element usItem: currentItemUSList)
						{
							String upSellItemID = XMLUtils.getAttributeValue(usItem, "ItemID");							
							if(!SCUtil.isVoid(upSellItemID) && !youMightConsiderItemIDs.contains(upSellItemID))
							{
								addToYouMightAlsoConsiderItemList(usItem);
								youMightConsiderItemIDs.add(upSellItemID);
							}
						}
					}
				}
			}
			//XB-673 Changes Start - This changes will be on Shopping Cart screen
			HashMap<String,ArrayList<Element>> alternateSBCListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.ALTERNATE_SBC_ITEMS_KEY);
			if(alternateSBCListMap!=null && !alternateSBCListMap.isEmpty())
			{
				Set upSellItemKeys = alternateSBCListMap.keySet();
				if(upSellItemKeys!=null){
					Iterator<String> alternateSBCKeyIter = upSellItemKeys.iterator();
					while(alternateSBCKeyIter.hasNext())
					{
						String key = alternateSBCKeyIter.next();
						ArrayList<Element> currentItemUSList = alternateSBCListMap.get(key);
						for(Element alternateSBCItem: currentItemUSList)
						{
							String alternateSBCItemID = XMLUtils.getAttributeValue(alternateSBCItem, "ItemID");							
							if(!SCUtil.isVoid(alternateSBCItemID) && !youMightConsiderItemIDs.contains(alternateSBCItemID))
							{
								addToYouMightAlsoConsiderItemList(alternateSBCItem);
								youMightConsiderItemIDs.add(alternateSBCItemID);
							}
						}
					}
				}
			}
			//XB-673 Changes End
			HashMap<String,ArrayList<Element>> compListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.COMPLEMENTARY_ITEMS_KEY);
			if(compListMap!=null && !compListMap.isEmpty())
			{
				Set compItemKeys = compListMap.keySet();
				if(compItemKeys!=null){
					Iterator<String> compKeyIter = compItemKeys.iterator();
					while(compKeyIter.hasNext())
					{
						String key = compKeyIter.next();
						ArrayList<Element> currentItemCompList = compListMap.get(key);
						for(Element compItem: currentItemCompList)
						{
							if(!xpedxPopularAccessoriesItems.contains(compItem))
								xpedxPopularAccessoriesItems.add(compItem);
						}
					}
				}
			}
			HashMap<String,ArrayList<Element>> crossSellListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.CROSS_SELL_ITEMS_KEY);
			if(crossSellListMap!=null && !crossSellListMap.isEmpty())
        {
				Set crossSellItemKeys = crossSellListMap.keySet();
				if (crossSellItemKeys != null) {
					Iterator<String> crossSellKeyIter = crossSellItemKeys
							.iterator();
					while (crossSellKeyIter.hasNext()) {
						String key = crossSellKeyIter.next();
						ArrayList<Element> currentItemCSList = crossSellListMap
								.get(key);
						//XB-673 Changes Start - Cross Sell needs to be displayed on Shopping Cart Screen
						for (Element csItem : currentItemCSList) {
							String crossSellItemID = XMLUtils
									.getAttributeValue(csItem, "ItemID");
							if (!SCUtil.isVoid(crossSellItemID)
									&& !youMightConsiderItemIDs
											.contains(crossSellItemID)) {
								addToYouMightAlsoConsiderItemList(csItem);
								youMightConsiderItemIDs.add(crossSellItemID);
							}
						//XB-673 Changes End - Cross Sell needs to be displayed on Shopping Cart Screen	
							/*
							 * if(!xpedxPopularAccessoriesItems.contains(csItem))
							 * xpedxPopularAccessoriesItems.add(csItem);
							 */
						}
					}
				}
			}
			HashMap<String,ArrayList<Element>> itemEleListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.ITEM_LIST_KEY);
			if(itemEleListMap != null && !itemEleListMap.isEmpty()) {
				ArrayList<Element> itemEleList = itemEleListMap.get(XPEDXOrderUtils.ITEM_LIST_KEY);
				setEntitledItemEleList (itemEleList);
			}
			
		}
	}
	
	/*protected void prepareXpedxItemBranchItemAssociationMap(ArrayList<String> itemIDList) throws CannotBuildInputException, XPathExpressionException{
		String custID = wcContext.getCustomerId();
		String customerDivision;
		String divisionNumberFromSession = (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH);
		if(divisionNumberFromSession!=null && divisionNumberFromSession.trim().length()>0)
			customerDivision = divisionNumberFromSession;
		else
			customerDivision = getDivisionNumber();
		String envCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.ENVIRONMENT_CODE);getEnvCode();
		if(envCode==null || envCode.trim().length()==0)
			envCode = getEnvCode();
		Document itemBranchItemAssociationDoc = null;
		try {
			itemBranchItemAssociationDoc = XPEDXOrderUtils.getXpedxItemBranchItemAssociationDetails(itemIDList, custID, customerDivision, envCode, wcContext);
		} catch (Exception e) {
			LOG.error("Error getting item branch item association.",e);
			return;
		}
		if(null == itemBranchItemAssociationDoc){
			LOG.error("No national level item association could be found");
			return;
		}
		HashMap<String, ArrayList<String>> replacementMap =  new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> relatedMap =  new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> alternateMap =  new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> complimentaryMap =  new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> upgradeMap =  new HashMap<String, ArrayList<String>>();
		
		ArrayList<String> itemIDListForGetCompleteItemList = new ArrayList<String>();
		//loop through the itemIDList
		for(int i=0;i<itemIDList.size();i++){
			String itemID = itemIDList.get(i);
			List<Element> xPXItemExtnList = XMLUtilities.getElements(itemBranchItemAssociationDoc.getDocumentElement(), "XPXItemExtn[@ItemID='"+itemIDList.get(i)+"']");
			if(xPXItemExtnList == null || xPXItemExtnList.size() <= 0){
				continue;
			}
			//but take only one, since we dont want duplicate elements
			Element xPXItemExtnElement = xPXItemExtnList.get(0);
			Element xPXItemAssociationsListElement = SCXmlUtil.getChildElement(xPXItemExtnElement, "XPXItemAssociationsList");
			//only replacement items will go to the xpedxItemIDUOMToReplacementListMap. Other items will go to the xpedxItemIDUOMToRelatedItemsListMap.
			List<Element> replacementList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='R']");
			List<Element> alternateList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='A']");
			List<Element> complementaryList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='C']");
			List<Element> upgradeList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='U']");
			
			//prepare the map for replacement
			if(null!=replacementList && replacementList.size() >=0){
				ArrayList<String> replacementAssItemIDList = new ArrayList<String>();
				for(Element replacementItem:replacementList){
					String associatedItemID = SCXmlUtil.getAttribute(replacementItem, "AssociatedItemID");
					if(!YFCCommon.isVoid(associatedItemID)){
						replacementAssItemIDList.add(associatedItemID);
						itemIDListForGetCompleteItemList.add(associatedItemID);
					}
				}
				if(replacementAssItemIDList.size()>0){
					replacementMap.put(itemID, replacementAssItemIDList);
				}
			}
			//prepare the map for alternate and complimentary
			ArrayList<String> relatedAssItemIDList = new ArrayList<String>();
			//get the alternateList
			if(null!=alternateList && alternateList.size() >=0){
				for(Element alterItemItem:alternateList){
					String associatedItemID = SCXmlUtil.getAttribute(alterItemItem, "AssociatedItemID");
					if(!YFCCommon.isVoid(associatedItemID)){
						relatedAssItemIDList.add(associatedItemID);
						itemIDListForGetCompleteItemList.add(associatedItemID);
					}
				}
			}
			//get the complementaryList
			if(null!=complementaryList && complementaryList.size() >=0){
				for(Element comItem:complementaryList){
					String associatedItemID = SCXmlUtil.getAttribute(comItem, "AssociatedItemID");
					if(!YFCCommon.isVoid(associatedItemID)){
						relatedAssItemIDList.add(associatedItemID);
						itemIDListForGetCompleteItemList.add(associatedItemID);
					}
				}
			}
			
			//get the upgraded items
			ArrayList<String> upgradeAssItemIDList = new ArrayList<String>();
			if(null!=upgradeList && upgradeList.size() >=0){
				for(Element upgradeItem:upgradeList){
					String associatedItemID = SCXmlUtil.getAttribute(upgradeItem, "AssociatedItemID");
					if(!YFCCommon.isVoid(associatedItemID)){
						relatedAssItemIDList.add(associatedItemID);
						itemIDListForGetCompleteItemList.add(associatedItemID);
					}
				}
				if(upgradeAssItemIDList.size()>0){
					upgradeMap.put(itemID, upgradeAssItemIDList);
				}
			}
			//add it to the relatedMap
			if(relatedAssItemIDList.size()>0){
				relatedMap.put(itemID, relatedAssItemIDList);
			}
			
		}//End of for loop
		if(null==itemIDListForGetCompleteItemList || itemIDListForGetCompleteItemList.size()<=0){
			LOG.debug("No branch level associated items.");
			return;
		}
		//remove the duplicate items and call getCompleteItemList
		HashSet hs = new HashSet();
		hs.addAll(itemIDListForGetCompleteItemList);
		itemIDListForGetCompleteItemList.clear();
		itemIDListForGetCompleteItemList.addAll(hs);
		//call getCompleteItemList
		Document itemDetailsListDoc = null;
		try {
			// invoking a different function which will give onyl the entitiled items - 734
			//itemDetailsListDoc = XPEDXOrderUtils.getXpedxMinimalItemDetails(itemIDListForGetCompleteItemList, custID, wcContext.getStorefrontId(), wcContext);
			itemDetailsListDoc = XPEDXOrderUtils.getXpedxEntitledItemDetails(itemIDListForGetCompleteItemList, custID, wcContext.getStorefrontId(), wcContext);
		} catch (Exception e) {
			LOG.error("Exception while getting item details for associated items",e);
			return;
		}
		//prepare the xpedxItemIDUOMToReplacementListMap
		Set replacementMapKeySet = replacementMap.keySet();
		Iterator<String> replacementIterator = replacementMapKeySet.iterator();
		while(replacementIterator.hasNext()){
			ArrayList replacementItemsElementList = new ArrayList();
			String itemID = replacementIterator.next();
			ArrayList<String> asscociatedItemIDList = replacementMap.get(itemID);
			for(String assID:asscociatedItemIDList){
				List<Element> itemDetailsList = XMLUtilities.getElements(itemDetailsListDoc.getDocumentElement(),"Item[@ItemID='"+assID+"']");
				if(null==itemDetailsList || itemDetailsList.size()<=0)
					continue;
				replacementItemsElementList.add(itemDetailsList.get(0));
			}
			xpedxItemIDUOMToReplacementListMap.put(itemID, replacementItemsElementList);
		}//end while loop
		
		
		
		//add the complementary and alternate items to the xpedxItemIDUOMToRelatedItemsListMap
		Set relatedMapKeySet = relatedMap.keySet();
		Iterator<String> relatedIterator = relatedMapKeySet.iterator();
		while(relatedIterator.hasNext()){
			ArrayList relatedItemsElementlist = new ArrayList();
			String itemID = relatedIterator.next();
			ArrayList relatedItemsValue = (ArrayList)xpedxItemIDUOMToRelatedItemsListMap.get(itemID);
			ArrayList<String> asscociatedItemIDList = relatedMap.get(itemID);
			for(String assID:asscociatedItemIDList){
				List<Element> itemDetailsList = XMLUtilities.getElements(itemDetailsListDoc.getDocumentElement(),"Item[@ItemID='"+assID+"']");
				if(null==itemDetailsList || itemDetailsList.size()<=0)
					continue;
				relatedItemsValue.add(itemDetailsList.get(0));
			}
			xpedxItemIDUOMToRelatedItemsListMap.remove(itemID);
			xpedxItemIDUOMToRelatedItemsListMap.put(itemID, relatedItemsValue);
		}//end while loop
		
		
	}*/
	
	/*protected void prepareXpedxItemAssociationMap(ArrayList<String> itemIDList) throws XPathExpressionException{
		String custID = wcContext.getCustomerId();
		String callingOrgCode = wcContext.getStorefrontId();
		Document itemAssociationDoc = null;
		//get the getXpedxAssociationlItemDetails
		try {
			itemAssociationDoc = XPEDXOrderUtils.getXpedxAssociationlItemDetails(itemIDList, custID, callingOrgCode, wcContext);
		} catch (CannotBuildInputException e) {
			LOG.error("Error getting National Level item association.",e);
			return;
		}
		if(null == itemAssociationDoc){
			LOG.error("No national level item association could be found");
			return;
		}
		NodeList nItemList = itemAssociationDoc.getElementsByTagName("Item");
		int itemNodeListLength = nItemList.getLength();
		for (int i = 0; i < itemNodeListLength; i++) {
			Element itemElement = (Element)nItemList.item(i);
			String itemID = SCXmlUtil.getAttribute(itemElement, "ItemID");
			if(null !=xpedxItemIDUOMToRelatedItemsListMap && xpedxItemIDUOMToRelatedItemsListMap.containsKey(itemID)){
				LOG.debug("xpedxItemIDUOMToRelatedItemsListMap already contains national level item association for "+itemID);
				continue;
			}
			LOG.debug("Preparing national level association for item Id "+itemID);
			ArrayList relatedItems = new ArrayList();
			Element associationTypeListElem = null;
			associationTypeListElem = XMLUtilities.getElement(itemElement.getOwnerDocument(), "AssociationTypeList");
			if (associationTypeListElem != null) {
				List<Element> crossSellElements = XMLUtilities.getElements(associationTypeListElem,"AssociationType[@Type='CrossSell']");
				List<Element> upSellElements = XMLUtilities.getElements(associationTypeListElem,"AssociationType[@Type='UpSell']");
				for (int j = 0; j < crossSellElements.size(); j++) {
					Element associationTypeElem = crossSellElements.get(i);
					Element associationListElem = XMLUtilities.getElement(associationTypeElem, "AssociationList");
					List associationList = XMLUtilities.getChildElements(associationListElem, "Association");
					if (associationList != null&& !associationList.isEmpty()) {
						Iterator associationIter = associationList.iterator();
						while (associationIter.hasNext()) {
							Element association = (Element) associationIter.next();
							Element associateditemEl = XMLUtilities.getElement(association, "Item");
							relatedItems.add(associateditemEl);
							if(!xpedxPopularAccessoriesItems.contains(associateditemEl))
								xpedxPopularAccessoriesItems.add(associateditemEl);
						}
					}
				}//for cross sell
				for (int k = 0; k < upSellElements.size(); k++) {
					Element associationTypeElem = upSellElements.get(k);
					Element associationListElem = XMLUtilities.getElement(associationTypeElem, "AssociationList");
					List associationList = XMLUtilities.getChildElements(associationListElem, "Association");
					if (associationList != null && !associationList.isEmpty()) {
						Iterator associationIter = associationList.iterator();
						while (associationIter.hasNext()) {
							Element association = (Element) associationIter.next();
							Element associateditemEl = XMLUtilities.getElement(association, "Item");
							relatedItems.add(associateditemEl);
							if(!xpedxYouMightConsiderItems.contains(associateditemEl))
								xpedxYouMightConsiderItems.add(associateditemEl);
						}
					}
				}//for upsell
			}//if
			//add relatedItems to the map
			xpedxItemIDUOMToRelatedItemsListMap.put(itemID, relatedItems);
		}
	}
	*/
	

	// override to read the new mashup id : Manoj Kodagali
	protected String getOrderDetailsMashupName() {
		return "xpedx_me_draftOrderDetails";
	}

	protected void getItemUOMs() throws Exception {
		Set itemIDUOMs = itemIDUOMToOrderLineKeyListMap.keySet();
		customerId = wcContext.getCustomerId();
		organizationCode = wcContext.getStorefrontId();
		List items = new ArrayList();
		Map itemIDToItemIDUOMMap = new HashMap();
		Iterator iter = itemIDUOMs.iterator();

		Map<String, String> uoms = null;

		while (iter.hasNext()) {
			ArrayList relatedItems = new ArrayList();
			String itemIDUOM = (String) iter.next();
			int delimIndex = itemIDUOM.lastIndexOf(":_:");
			itemID = itemIDUOM.substring(0, delimIndex);
			items.add(itemID);
			unitOfMeasure = itemIDUOM.substring(delimIndex + ":_:".length());
			itemIDToItemIDUOMMap.put(itemID, itemIDUOM);
		}
		
		//Fetching the uoms for main and replacement items
		Set<String> itemIdList = new HashSet<String>();
		
		for (int i = 0; i < items.size(); i++) {
			String itemId =  (String)items.get(i);
			itemIdList.add(itemId);
		}
		
		if(xpedxItemIDUOMToReplacementListMap!=null && !xpedxItemIDUOMToReplacementListMap.isEmpty())
		{
			Set<String> itemIdKeys =  xpedxItemIDUOMToReplacementListMap.keySet();
			Iterator<String> keyIter = itemIdKeys.iterator();
			while(keyIter.hasNext())
			{
				String currentItemId = keyIter.next();
				List<Element> replacementItems =  (ArrayList<Element>) xpedxItemIDUOMToReplacementListMap.get(currentItemId);
				if(replacementItems!=null && replacementItems.size()>0)
				{
					for(Element itemElement: replacementItems)
					{
						String replacementItemId = itemElement.getAttribute("ItemID");
						itemIdList.add(replacementItemId);
					}
				}
			}
			
		}
		allItemIds.addAll(itemIdList);
		
		//This sets the inventory indicator map and also order multiple map 
		setInventoryAndOrderMultipleMap();
		/*
		 * getting all the items UOMs at the same time using a complex query
		 */
		itemIdsUOMsDescMap = XPEDXOrderUtils.getXpedxUOMDescList(wcContext.getCustomerId(), allItemIds, wcContext.getStorefrontId(),false);
		itemIdsUOMsMap = (Map<String, Map<String, String>>) XPEDXWCUtils.getObjectFromCache("itemsUOMMap");//XPEDXOrderUtils.getXpedxUOMList(wcContext.getCustomerId(), allItemIds, wcContext.getStorefrontId());
		itemAndCustomerUomHashMap = XPEDXOrderUtils.getItemCustomerUomHashMap();
		itemAndCustomerUomWithConvHashMap = XPEDXOrderUtils.getItemCustomerUomConvFactHashMap();
		XPEDXWCUtils.setObectInCache("ItemCustomerUomWithConvFactors", itemAndCustomerUomWithConvHashMap);
		/*if(itemIdsUOMsMap!=null && itemIdsUOMsMap.keySet()!=null) {
			ArrayList<String> itemIdsList = new ArrayList<String>();
			itemIdsList.addAll(itemIdsUOMsMap.keySet());
			Iterator<String> iterator = itemIdsList.iterator();
			while(iterator.hasNext()) {
				String itemIdForUom = iterator.next();
				Map uommap = itemIdsUOMsMap.get(itemIdForUom);
				ArrayList<String> uomKeys = new ArrayList<String>();
				uomKeys.addAll(uommap.keySet());
				Iterator<String> uomIterator = uomKeys.iterator();
				Map<String, String> newUomMap = new HashMap(itemIdsUOMsMap.get(itemIdForUom));
				itemIdConVUOMMap.put(itemIdForUom, newUomMap);
				while(uomIterator.hasNext()) {
					String uom = uomIterator.next();
					String convFactor = (String) uommap.get(uom);
					long convFac = Math.round(Double.parseDouble(convFactor));
					if(1 == convFac) {
						uommap.put(uom, XPEDXWCUtils.getUOMDescription(uom));
					}
					else {
						//-FXD-1 UOM description & Conversion Factor spacing between them.
						uommap.put(uom, XPEDXWCUtils.getUOMDescription(uom)+" ("+convFac+")" );
					}
				}
				
				itemIdsUOMsDescMap.put(itemIdForUom, uommap);
			}
		}*/
		for (int i = 0; i < items.size(); i++) {
			String newItemID = (String)items.get(i);
			String itemIDUOM = (String)itemIDToItemIDUOMMap.get(newItemID);
			// Set uomKeys = uoms.keySet();
			uoms = itemIdsUOMsMap.get(newItemID);
			if(uoms == null)
				uoms =  new HashMap<String,String>();
			itemUOMsMap.put(itemIDUOM, uoms);
			if (displayItemUOMsMap == null) {
				displayItemUOMsMap = new HashMap();
			}
			Map displayuomsMap = new HashMap();
			for (Iterator it = uoms.keySet().iterator(); it.hasNext();) {
				String uomDesc = (String) it.next();
				Object o = uoms.get(uomDesc);
				if("1".equals(o))
				{
					displayuomsMap.put(uomDesc,XPEDXWCUtils.getUOMDescription(uomDesc));
				}
				else{
					displayuomsMap.put(uomDesc,XPEDXWCUtils.getUOMDescription(uomDesc)+ " (" + o + ")");
				}
				
			}

			displayItemUOMsMap.put(itemIDUOM, displayuomsMap);
		}

	}
	
	
	public String getJsonStringForMap(HashMap map){
		String jsonString = "";
		JSONObject jsonObject = JSONObject.fromObject( map );
		jsonString = jsonObject.toString();
		return jsonString;
	}

	protected void getCustomerLineDetails() throws Exception {
		//get the map from the session. if null query the DB
		LinkedHashMap customerFieldsSessionMap = getCustomerFieldsMapfromSession();
        if(null != customerFieldsSessionMap && customerFieldsSessionMap.size() >= 0){
        	LOG.debug("Found customerFieldsMap in the session");
        	customerFieldsMap = customerFieldsSessionMap;
        	return;
        }
	/*	customerId = wcContext.getCustomerId();
		organizationCode = wcContext.getStorefrontId();
		HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
		Set mashupSet = buildSetFromDelmitedList("draftOrderGetCustomerLineFields");
		Map outputMap = prepareAndInvokeMashups(mashupSet);
		Element outputEl = (Element) outputMap.get("draftOrderGetCustomerLineFields");
		Element customerOrganizationExtnEle = XMLUtilities.getChildElementByName(outputEl, "Extn");
		shipFromDivision = SCXmlUtils.getAttribute(customerOrganizationExtnEle,"ExtnShipFromBranch");
		customerFieldsMap = XPEDXOrderUtils.getCustomerLineFieldMap(outputEl.getOwnerDocument());
		//set this in the session
		localSession.setAttribute("customerFieldsSessionMap", customerFieldsMap); */
	}
	
	private void getAllItemSKUs() throws CannotBuildInputException, XPathExpressionException
	{
		
		mfgItemFlag =(String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.BILL_TO_CUST_MFG_ITEM_FLAG);
		customerItemFlag =(String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.BILL_TO_CUST_PART_ITEM_FLAG);
		XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
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
			companyCode = SCXmlUtil.getAttribute(customerOrganizationExtnEle,"ExtnCompanyCode");
			legacyCustomerNumber = SCXmlUtil.getAttribute(customerOrganizationExtnEle,"ExtnLegacyCustNumber");
			shipToCustomer.setExtnEnvironmentCode(envCode);
			shipToCustomer.setExtnCompanyCode(companyCode);
			shipToCustomer.setExtnLegacyCustNumber(legacyCustomerNumber);
			XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER, shipToCustomer);
		}
		
		/*if(useCustSku!=null && useCustSku.length()>0)
		{
			setCustomerSku(useCustSku);
		}*/
		
		//Fetch all the items in Cart and get their respective SKUs
		Document orderDoc = getOutputDocument();
		Element orderLinesElement = SCXmlUtil.getChildElement(orderDoc
				.getDocumentElement(), "OrderLines");
		ArrayList<Element> orderLineElemList = SCXmlUtil.getElements(orderLinesElement, "OrderLine");
		if(orderLineElemList==null || orderLineElemList.size()==0)
			return;
		HashMap<String, String> itemSkuMap = new LinkedHashMap<String, String>();
		setSkuMap(new HashMap<String, HashMap<String,String>>());
		ArrayList<String> itemIdList = new ArrayList<String>();	
		
		if(!SCUtil.isVoid(mfgItemFlag) && mfgItemFlag.equalsIgnoreCase("Y")) {				
			HashMap<String, HashMap<String,String>> itemsSkuMap = new LinkedHashMap<String, HashMap<String,String>>();
			for (int i = 0; i < orderLineElemList.size(); i++) {
				Element orderLineElement = (Element)orderLineElemList.get(i);
				Element itemElement = SCXmlUtil.getChildElement(orderLineElement,"Item");
				String itemId = itemElement.getAttribute("ItemID");

				if(skuMap.containsKey(itemId))
					continue;
				/*Begin - Changes made by Mitesh Parikh for JIRA 3581*/
				Element itemDetailsElement = SCXmlUtil.getChildElement(orderLineElement, "ItemDetails");
				Element primeInfoElem = XMLUtilities.getElement(itemDetailsElement, "PrimaryInformation");
				if(primeInfoElem!=null)
				{
					String manufactureItem = primeInfoElem.getAttribute("ManufacturerItem");
					if(manufactureItem!=null && manufactureItem.length()>0)
						itemSkuMap.put(XPEDXConstants.MFG_ITEM_NUMBER, manufactureItem);
				}

				itemIdList.add(itemId);
				skuMap.put(itemId, (HashMap<String, String>)itemSkuMap.clone());
				itemSkuMap.clear();
			}

		}
		if(!SCUtil.isVoid(customerItemFlag) && customerItemFlag.equalsIgnoreCase("Y")) {
			Document itemcustXrefDoc = XPEDXWCUtils.getXpxItemCustXRefDoc(itemIdList, getWCContext());
			if(itemcustXrefDoc!=null) {
				Element itemCustXRefList = itemcustXrefDoc.getDocumentElement();
				ArrayList<Element> itemCustXrefElems = SCXmlUtil.getElements(itemCustXRefList, "XPXItemcustXref");
				if(itemCustXrefElems!=null && itemCustXrefElems.size()>0) {
					Iterator<Element> xrefIter = itemCustXrefElems.iterator();
					while(xrefIter.hasNext()) {
						Element xref = xrefIter.next();
						String legacyItemNum = xref.getAttribute("LegacyItemNumber");
						if(itemIdList.contains(legacyItemNum.trim())) {
							String customerPartNumber = xref.getAttribute("CustomerItemNumber");
							if(customerPartNumber!=null && customerPartNumber.length()>0)
							{
								HashMap<String, String> tmpSkuMap = skuMap.get(legacyItemNum);
								if(SCUtil.isVoid(tmpSkuMap))
									itemSkuMap = new HashMap<String, String>();
								else
									itemSkuMap = tmpSkuMap;
							itemSkuMap.put(XPEDXConstants.CUST_PART_NUMBER, customerPartNumber);
							skuMap.put(legacyItemNum, (HashMap<String, String>)itemSkuMap.clone());
							itemSkuMap.clear();
							}
						}
					}
				}
			}
	   }
	}
	protected LinkedHashMap getCustomerFieldsMapfromSession(){
		/*HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
        HashMap customerFieldsSessionMap = (HashMap)localSession.getAttribute("customerFieldsSessionMap");*/
		XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
		LinkedHashMap customerFieldsSessionMap = (LinkedHashMap)XPEDXWCUtils.getObjectFromCache("customerFieldsSessionMap");
        return customerFieldsSessionMap;
	}

	private void getAllItemList() throws Exception {
		Document outputDoc = null;
		itemListMap = new HashMap();
		String customerId = wcContext.getCustomerId();
		outputDoc = XPEDXWCUtils.getAllItemList(customerId);
		Element outputEl = outputDoc.getDocumentElement();

		ArrayList<Element> listofWishLists = getXMLUtils().getElements(
				outputEl, "XPEDXMyItemsList");
		if (listofWishLists != null) {
			for (Element list : listofWishLists) {
				itemListMap.put(list.getAttribute("MyItemsListKey"), list
						.getAttribute("Name"));
			}
		}
		// itemListMap.put("key1", "First list");
	}

	private void setYFSEnvironmentVariables() {
		if (orderHeaderKey != null && !"".equals(orderHeaderKey))
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("OrderWithoutLineToProcess", "true");
			map.put("isPnACall", "true");
			map.put("isDiscountCalculate", "true");
			XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext(), map);
		}
	}

	protected void getCompleteOrderDetailsDoc() throws Exception {
		//XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext());
		if(getOutputDocument()==null)
		{
			super.getCompleteOrderDetailsDoc();
		
		} else {
			validateRestoredOrder();
			addModificationRuleToOrderListElement(getOrderElementFromOutputDocument()); 
			
		}			
	}

	private void processPandA(Vector items) {
		Document orderDoc = getOutputDocument();
		assert (orderDoc != null);
		Element OrderLinesElement = SCXmlUtil.getChildElement(orderDoc
				.getDocumentElement(), "OrderLines");
		NodeList orderLineNodeList = OrderLinesElement
				.getElementsByTagName("OrderLine");
		for (int i = 0; i < orderLineNodeList.getLength(); i++) {
			Element orderLineEle = (Element) orderLineNodeList.item(i);
			Element extnElement = SCXmlUtil.getChildElement(orderLineEle,
					"Extn");
			extnElement.getAttribute("");
		}
	}

	/**
	 * @return
	 * @throws CannotBuildInputException
	 */
	protected String getDivisionNumber() throws CannotBuildInputException {
		if(null == divisionNumber){
			divisionNumber = XPEDXWCUtils.getCustomerShipFromDivision(getWCContext().getCustomerId(), getWCContext().getStorefrontId());
			if(null==divisionNumber){
				divisionNumber="";
			}
		}
		return divisionNumber;
	}
	
	/*protected String getDivisionName() throws CannotBuildInputException {
		if(null == divisionName){
			divisionName = XPEDXWCUtils.getDivisionName();
			if(null==divisionName){
				divisionName="";
			}
		}
		return divisionName;
	}*/
	
	protected void setDivisionName(String divisionName) throws CannotBuildInputException {
		this.divisionName = divisionName;
	}

	public String getState() {
		if(null==state){
			state="";
		}
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDivisionName() {
		if(null==divisionName){
			divisionName="";
		}
		return divisionName;
	}
	/**
	 * @return the xpedxItemIDUOMToReplacementListMap
	 */
	public HashMap getXpedxItemIDUOMToReplacementListMap() {
		return xpedxItemIDUOMToReplacementListMap;
	}
	
	public void setXpedxItemIDUOMToReplacementListMap(
			HashMap xpedxItemIDUOMToReplacementListMap) {
		this.xpedxItemIDUOMToReplacementListMap = xpedxItemIDUOMToReplacementListMap;
	}
	
	protected void manipulateMashupInputs(Map<String, Element> mashupInputs)
			throws CannotBuildInputException {
		super.manipulateMashupInputs(mashupInputs);
	}

	/*public HashMap getXpedxItemIDUOMToRelatedItemsListMap() {
		return xpedxItemIDUOMToRelatedItemsListMap;
	}

	public void setXpedxItemIDUOMToRelatedItemsListMap(
			HashMap xpedxItemIDUOMToRelatedItemsListMap) {
		this.xpedxItemIDUOMToRelatedItemsListMap = xpedxItemIDUOMToRelatedItemsListMap;
	}*/

	public HashMap getItemUOMsMap() {
		return itemUOMsMap;
	}

	public void setItemUOMsMap(HashMap itemUOMsMap) {
		this.itemUOMsMap = itemUOMsMap;
	}

	public Map getItemOrderMultipleMap() {
		return itemOrderMultipleMap;
	}

	public void setItemOrderMultipleMap(Map itemOrderMultipleMap) {
		this.itemOrderMultipleMap = itemOrderMultipleMap;
	}

	public String getShipFromDivision() {
		return shipFromDivision;
	}

	public String getEnvCode() {
		String envCode = XPEDXWCUtils.getEnvironmentCode(getWCContext()
				.getCustomerId());
		return envCode;
	}

	public void setShipFromDivision(String shipFromDivision) {
		this.shipFromDivision = shipFromDivision;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public Map getItemListMap() {
		if (itemListMap == null) {
			itemListMap = new HashMap();
		}
		return itemListMap;
	}

	public void setItemListMap(Map itemListMap) {
		this.itemListMap = itemListMap;
	}
	
	/*
	 * This will check if there is a cart in the context, if present opens the cart and opens the Quick add Link in the Draft Order details page
	 * else Creates a new cart and then opens it and opens the quick add.
	 */
	public String openQuickAdd() {
		String returnVal = SUCCESS;
		String editedOrderHeaderKey=XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
		boolean isDraftSet=false;
		if(!YFCCommon.isVoid(editedOrderHeaderKey))
		{
			orderHeaderKey=editedOrderHeaderKey;
			setDraft("N");
			isEditOrder="true";
			isDraftSet=true;
		}
		else
		{
			orderHeaderKey = (String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");//XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(wcContext);
		}
		/*Changes done for JIRA 3024 - Start*/
		if((orderHeaderKey==null || orderHeaderKey.equals("") || orderHeaderKey.equals("_CREATE_NEW_")) && XPEDXOrderUtils.isCartOnBehalfOf(getWCContext())){
			 XPEDXOrderUtils.createNewCartInContext(getWCContext());
			 //orderHeaderKey =getWCContext().getWCAttribute("CommerceContextObject")
			 orderHeaderKey=(String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");
			 //orderHeaderKey=XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext());
			 
		}
		/*else if((orderHeaderKey!=null && orderHeaderKey.trim().length()>0)  ) {
			if(!isDraftSet)
				setDraft("Y");
			returnVal = execute();
		}*/
		/*else {
			
			returnVal = execute();
		}*/
		
		/*Changes done for JIRA 3024 - End*/
		
		if(!isDraftSet)
			setDraft("Y");
		returnVal = execute();
		return returnVal;
			
	}
	/*
	 * Prepares a Arraylist of XPEDXItem, after looping through the orderlines
	 */
	public ArrayList<XPEDXItem> getPnAInputDoc() {
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
		int k = 0;
		if(xpedxItemIDUOMToReplacementListMap != null && xpedxItemIDUOMToReplacementListMap.size()>0) {
			Set keySet = new HashSet();
			keySet = xpedxItemIDUOMToReplacementListMap.keySet();
			ArrayList<String> replacementItemIds = new ArrayList<String>();
			replacementItemIds.addAll(keySet);
			for(int i=0;i<replacementItemIds.size();i++) {
				ArrayList<Element> replacementItemsElemList = (ArrayList<Element>) xpedxItemIDUOMToReplacementListMap.get(replacementItemIds.get(i));
				if(replacementItemsElemList!=null && replacementItemsElemList.size()>0) {
					for(int j=0;j<replacementItemsElemList.size();j++) {
						Element replacementItemElem = replacementItemsElemList.get(j);
						String legacyProductCode = replacementItemElem.getAttribute("ItemID");
						String requestedQtyUOM = SCXmlUtil.getChildElement(replacementItemElem, "PrimaryInformation").getAttribute("PricingUOM");
						String requestedQty= "1";
						
						XPEDXItem item = new XPEDXItem();
						item.setLegacyProductCode(legacyProductCode);
						item.setRequestedQty(requestedQty);
						item.setRequestedQtyUOM(requestedQtyUOM);
						int seqNumber = orderLineNodeList.getLength() + k+1;
						String lineNumber = Integer.toString(seqNumber);
						item.setLineNumber(lineNumber);
						LOG.debug("Adding a item to the P&A input list: "
								+ legacyProductCode);
						pnaList.add(item);
					}
				}
			}
		}
		return pnaList;
	}
	
	/*
	 * Parses the XpxItemExtn Document and then sets the ordermultiple and also the inventory maps accordingly
	 */
	
	private void setInventoryAndOrderMultipleMap() {
		
		//String customerDivision = (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH);
		//String envCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.ENVIRONMENT_CODE);
		String customerDivision = shipToCustomer.getExtnShipFromBranch();
		String envCode = shipToCustomer.getExtnEnvironmentCode();
		if(envCode==null || envCode.trim().length()==0)
			envCode = XPEDXWCUtils.getEnvironmentCode(wcContext.getCustomerId());
		HashMap<String,String> inventoryMap = new HashMap<String, String>();
		HashMap<String,String> orderMultipleMap = new HashMap<String, String>();
		if(allItemIds!=null && xpedxItemIDToItemExtnMap!=null) {
			Iterator<String> itemIdIterator = allItemIds.iterator();
			while(itemIdIterator.hasNext()) {
				String currItemId = itemIdIterator.next();
				ArrayList<Element> itemExtnElement = (ArrayList<Element>) xpedxItemIDToItemExtnMap.get(currItemId);
				if(itemExtnElement!=null && itemExtnElement.size()>0) {
					Element itemExtn = itemExtnElement.get(0);
					if(itemExtn!=null) {
						String division = SCXmlUtil.getAttribute(itemExtn, "XPXDivision");
						String environmentId = SCXmlUtil.getAttribute(itemExtn, "EnvironmentID");
						if(environmentId.equalsIgnoreCase(envCode) && division.equalsIgnoreCase(customerDivision)) {
							String inventoryIndicator = SCXmlUtil.getAttribute(itemExtn, "InventoryIndicator");
							String orderMultiple = SCXmlUtil.getAttribute(itemExtn, "OrderMultiple");
							if (inventoryIndicator.equalsIgnoreCase("W"))
								inventoryMap.put(currItemId, "Y");
							else
								inventoryMap.put(currItemId, "N");
							if (orderMultiple == null || orderMultiple.trim().length() == 0) {
								orderMultiple = "1";
							}
							orderMultipleMap.put(currItemId, orderMultiple);
						}
						else {
							inventoryMap.put(currItemId, "N");
							orderMultipleMap.put(currItemId, "1");
						}
					}
				}
				else {
					inventoryMap.put(currItemId, "N");
					orderMultipleMap.put(currItemId, "1");
				}
			}
		}
		setItemOrderMultipleMap(orderMultipleMap);
		setInventoryMap(inventoryMap);
	}

	public boolean isSpecialLine(Element orderLine) {
		String lineType = orderLine.getAttribute("LineType");
		if (lineType != null && lineType.trim().length() > 0) {
			return lineType.equals("C");
		}

		return false;
	}

	public Map getDisplayItemUOMsMap() {
		return displayItemUOMsMap;
	}

	public void setDisplayItemUOMsMap(Map displayItemUOMsMap) {
		this.displayItemUOMsMap = displayItemUOMsMap;
	}

	
	
	public float getMinOrderAmount() {
		return minOrderAmount;
	}
	public void setMinOrderAmount(float minOrderAmount) {
		this.minOrderAmount = minOrderAmount;
	}
	public float getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(float chargeAmount) {
		this.chargeAmount = chargeAmount;
	}



	protected HashMap xpedxItemIDUOMToReplacementListMap;
	private static final Logger LOG = Logger
			.getLogger(XPEDXDraftOrderDetailsAction.class);
	// By Manoj
	//protected HashMap xpedxItemIDUOMToRelatedItemsListMap;
	protected ArrayList<Element> xpedxYouMightConsiderItems;
	protected ArrayList<Element> xpedxPopularAccessoriesItems;
	protected LinkedHashMap customerFieldsMap;
	private HashMap<String, HashMap<String,String>> skuMap=new HashMap<String, HashMap<String,String>>();
	private String customerSku;
	private String adjCatTwoShortDesc = "";
	protected String customerId;
	protected String shipFromDivision;
	protected String organizationCode;
	protected HashMap itemUOMsMap;
	protected HashMap orderMultipleMapFromSourcing;
	public HashMap getOrderMultipleMapFromSourcing() {
		return orderMultipleMapFromSourcing;
	}


	public void setOrderMultipleMapFromSourcing(HashMap orderMultipleMapFromSourcing) {
		this.orderMultipleMapFromSourcing = orderMultipleMapFromSourcing;
	}



	protected Map itemOrderMultipleMap;
	protected Map itemListMap;
	private HashMap<String, JSONObject> pnaHoverMap;
	private HashMap<String, XPEDXItemPricingInfo> priceHoverMap;
	protected Map displayItemUOMsMap;
	protected String ajaxLineStatusCodeMsg = "";
	protected String divisionNumber = null;
	protected String divisionName = null;
	protected String state = null;
	private String 	uniqueId = ""; 
	private float minOrderAmount;
	private float chargeAmount;
	public String draftFlagError = "draftFlagError";
	private String 	erroMsg = "";
	public String getErroMsg() {
		return erroMsg;
	}

	public void setErroMsg(String erroMsg) {
		this.erroMsg = erroMsg;
	}


	public String getAdjCatTwoShortDesc() {
		return adjCatTwoShortDesc;
	}

	public void setAdjCatTwoShortDesc(String adjCatTwoShortDesc) {
		this.adjCatTwoShortDesc = adjCatTwoShortDesc;
	}

	public ArrayList<String> itemList = new ArrayList<String>();
	

	public ArrayList<String> getItemMap() {
		return itemList;
	}

	public void setItemMap(ArrayList<String> itemList) {
		this.itemList = itemList;
	}



	public ArrayList<String> allItemID;
	
// Start for XB-689
	public ArrayList<String> allItemIDsWithDuplicates;

	
	public ArrayList<String> getAllItemIDsWithDuplicates() {
		return allItemIDsWithDuplicates;
	}

	public String duplicateInfoMsg;
	

	public String getDuplicateInfoMsg() {
		return duplicateInfoMsg;
	}


	public void setDuplicateInfoMsg(String duplicateInfoMsg) {
		this.duplicateInfoMsg = duplicateInfoMsg;
	}


	public void setAllItemIDsWithDuplicates(
			ArrayList<String> allItemIDsWithDuplicates) {
		this.allItemIDsWithDuplicates = allItemIDsWithDuplicates;
	}
// end for XB-689
	
	
	public ArrayList<String> getAllItemID() {
		return allItemID;
	}

	public void setAllItemID(ArrayList<String> allItemID) {
		this.allItemID = allItemID;
	}



	protected ArrayList<String> allItemIds = new ArrayList<String>();
	protected Map<String,Map<String,String>> itemIdsUOMsMap=new HashMap<String,Map<String,String>>();
	
	//Added for EB-64 - This map contains item ids and their customer UOM , if exist.
	protected LinkedHashMap<String, String> itemAndCustomerUomHashMap = new LinkedHashMap<String, String>();
	//Added for EB-64 - This map contains item ids and their customer UOM with conversion factor with pipe separated , if exist. eg : 2001020, PC|2
	protected LinkedHashMap<String, String> itemAndCustomerUomWithConvHashMap = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> getItemAndCustomerUomWithConvHashMap() {
		return itemAndCustomerUomWithConvHashMap;
	}


	public void setItemAndCustomerUomWithConvHashMap(
			LinkedHashMap<String, String> itemAndCustomerUomWithConvHashMap) {
		this.itemAndCustomerUomWithConvHashMap = itemAndCustomerUomWithConvHashMap;
	}


	public LinkedHashMap<String, String> getItemAndCustomerUomHashMap() {
		return itemAndCustomerUomHashMap;
	}


	public void setItemAndCustomerUomHashMap(
			LinkedHashMap<String, String> itemAndCustomerUomHashMap) {
		this.itemAndCustomerUomHashMap = itemAndCustomerUomHashMap;
	}


	public Map<String, Map<String, String>> getItemIdConVUOMMap() {
		return itemIdConVUOMMap;
	}

	public void setItemIdConVUOMMap(
			Map<String, Map<String, String>> itemIdConVUOMMap) {
		this.itemIdConVUOMMap = itemIdConVUOMMap;
	}
//JIRA 3488 start
	private float maxOrderAmount;
	public void setMaxOrderAmount(float maxOrderAmount) {
		this.maxOrderAmount = maxOrderAmount;
	}

	public float getMaxOrderAmount() {
		return maxOrderAmount;
	}
	//JIRA 3488 end

	protected Map<String,Map<String,String>> itemIdConVUOMMap=new HashMap<String,Map<String,String>>();
	protected ArrayList<Element> entitledItemEleList = new ArrayList<Element>();
	
	protected Map<String,Map<String,String>> itemIdsUOMsDescMap=new HashMap<String,Map<String,String>>();
	String lastModifiedDateString = "";
	String lastModifiedUserId = "";
	protected String isEditOrder="false";
	protected String resetDesc = "false";
	private boolean quickAdd = false;
	protected String isPNACallOnLoad="true";
	protected Map<String,Element> editOrderOrderMap = new HashMap<String,Element>();
	protected Map<String,Element> editOrderOrderLineMap = new HashMap<String,Element>();
	//Added to remember the filter selection in My items List page.
	protected String customerFieldsValidated = "N";
	protected String validateCustomerFields = "N";
	protected HashMap xpedxItemIDToItemExtnMap;
	//added for jira 2885
	private  Map<String,String> pnALineErrorMessage=new HashMap<String,String>(); 
	private String draftOrderList;
	public String draftOrderFail="false";
	private boolean isOUErrorPage=false;
	
	protected HashMap useOrderMultipleMapFromSourcing;
	public HashMap getUseOrderMultipleMapFromSourcing() {
		return useOrderMultipleMapFromSourcing;
	}

	public void setUseOrderMultipleMapFromSourcing(
			HashMap useOrderMultipleMapFromSourcing) {
		this.useOrderMultipleMapFromSourcing = useOrderMultipleMapFromSourcing;
	}
	
	public String getDraftOrderFail() {
		return draftOrderFail;
	}


	public void setDraftOrderFail(String draftOrderFail) {
		this.draftOrderFail = draftOrderFail;
	}



	protected HashMap<String, ArrayList<String>> requiredCustFieldsErrorMap;	
	private String itemDtlBackPageURL="";
	private String salesreploggedInUserName; //added for XBT-146
	//XBT - 248 & 252
	public String draftOrderError;
	
	public String draftOrderFlag;
	
	public String getDraftOrderError() {
		return draftOrderError;
	}

	public void setDraftOrderError(String draftOrderError) {
		this.draftOrderError = draftOrderError;
	}
	//End of XBT 248 & 252
	
	public String getSalesreploggedInUserName() {
		return salesreploggedInUserName;
	}

	public void setSalesreploggedInUserName(String salesreploggedInUserName) {
		this.salesreploggedInUserName = salesreploggedInUserName;
	}

	public String custStatus;
	public String getCustStatus() {
		return custStatus;
	}

	public void setCustStatus(String custStatus) {
		this.custStatus = custStatus;
	}
	
	public ArrayList<Element> getEntitledItemEleList() {
		return entitledItemEleList;
	}


	public void setEntitledItemEleList(ArrayList<Element> entitledItemEleList) {
		this.entitledItemEleList = entitledItemEleList;
	}

	protected Document changeOrderOutputDoc=null;
	protected String isDeleteOrder="false";
	public static final String CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ = "changeOrderAPIOutputForOrderLinesModification";
	
	public Document getChangeOrderOutputDoc() {
		return changeOrderOutputDoc;
	}

	public void setChangeOrderOutputDoc(Document changeOrderOutputDoc) {
		this.changeOrderOutputDoc = changeOrderOutputDoc;
	}

	public String getItemDtlBackPageURL() {
		return itemDtlBackPageURL;
	}

	public void setItemDtlBackPageURL(String itemDtlBackPageURL) {
		this.itemDtlBackPageURL = itemDtlBackPageURL;
	}

	public String getResetDesc() {
		return resetDesc;
	}


	public void setResetDesc(String resetDesc) {
		this.resetDesc = resetDesc;
	}


	public String getUniqueId() {
		uniqueId = System.currentTimeMillis() + "";
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public Map<String, Map<String, String>> getItemIdsUOMsMap() {
		return itemIdsUOMsMap;
	}
	public void setItemIdsUOMsMap(Map<String, Map<String, String>> itemIdsUOMsMap) {
		this.itemIdsUOMsMap = itemIdsUOMsMap;
	}
	public Map<String, Map<String, String>> getItemIdsUOMsDescMap() {
		return itemIdsUOMsDescMap;
	}
	public void setItemIdsUOMsDescMap(
			Map<String, Map<String, String>> itemIdsUOMsDescMap) {
		this.itemIdsUOMsDescMap = itemIdsUOMsDescMap;
	}

	private HashMap<String, String> skuTypeList = new HashMap<String, String>();
	

	public HashMap<String, String> getSkuTypeList() {
		return skuTypeList;
	}
	public void setSkuTypeList(HashMap<String, String> skuTypeList) {
		this.skuTypeList = skuTypeList;
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
	 * @return the validateCustomerFields
	 */
	public String getValidateCustomerFields() {
		return validateCustomerFields;
	}

	/**
	 * @param validateCustomerFields the validateCustomerFields to set
	 */
	public void setValidateCustomerFields(String validateCustomerFields) {
		this.validateCustomerFields = validateCustomerFields;
	}

	protected HashMap<String, String> inventoryMap = new HashMap<String, String>();
	/**
	 * @return the inventoryMap
	 */
	public HashMap<String, String> getInventoryMap() {
		return inventoryMap;
	}

	/**
	 * @param inventoryMap the inventoryMap to set
	 */
	public void setInventoryMap(HashMap<String, String> inventoryMap) {
		this.inventoryMap = inventoryMap;
	}
	public ArrayList<Element> getXpedxYouMightConsiderItems() {
		return xpedxYouMightConsiderItems;
	}
	public void setXpedxYouMightConsiderItems(
			ArrayList<Element> xpedxYouMightConsiderItems) {
		this.xpedxYouMightConsiderItems = xpedxYouMightConsiderItems;
	}
	
	public void addToYouMightAlsoConsiderItemList(Element elm) {
		String currItemId = SCXmlUtil.getAttribute(elm, "ItemID");
		String unitOfMeasure = SCXmlUtil.getAttribute(elm, "UnitOfMeasure");
		
		try {
			LOG.debug(" currItemId " + currItemId );
			LOG.debug(" unitOfMeasure " + unitOfMeasure );
		
		if( (currItemId != null &&  currItemId.length() > 0 ) && (unitOfMeasure != null &&  unitOfMeasure.length() > 0 ) )
			this.xpedxYouMightConsiderItems.add(elm) ;
		else
			LOG.warn("ItemId or UOM missing for Carousel Display Items. Item not added to list\n Details : " + SCXmlUtils.getString(elm) );
		

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	public ArrayList<Element> getXpedxPopularAccessoriesItems() {
		return xpedxPopularAccessoriesItems;
	}
	public void setXpedxPopularAccessoriesItems(
			ArrayList<Element> xpedxPopularAccessoriesItems) {
		this.xpedxPopularAccessoriesItems = xpedxPopularAccessoriesItems;
	}
	public String getLastModifiedDateString() {
		return lastModifiedDateString;
	}
	public void setLastModifiedDateString(String lastModifiedDateString) {
		this.lastModifiedDateString = lastModifiedDateString;
	}
	public String getLastModifiedDateToDisplay() {
		UtilBean utilBean = new UtilBean();
		String dateToDisplay = utilBean.formatDate(lastModifiedDateString, wcContext, null, "M/d/yyyy");	
		return dateToDisplay;
	}
	public String getLastModifiedUserId() {
		return lastModifiedUserId;
	}
	public void setLastModifiedUserId(String lastModifiedUserId) {
		this.lastModifiedUserId = lastModifiedUserId;
	}
	public String getIsEditOrder() {
		return isEditOrder;
	}
	public void setIsEditOrder(String isEditOrder) {
		this.isEditOrder = isEditOrder;
	}
	public boolean isQuickAdd() {
		return quickAdd;
	}
	public void setQuickAdd(boolean quickAdd) {
		this.quickAdd = quickAdd;
	}

	public String getIsPNACallOnLoad() {
		return isPNACallOnLoad;
	}

	public void setIsPNACallOnLoad(String isPNACallOnLoad) {
		this.isPNACallOnLoad = isPNACallOnLoad;
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
	
	/**
	 * @return the requiredCustFieldsErrorMap
	 */
	public HashMap<String, ArrayList<String>> getRequiredCustFieldsErrorMap() {
		return requiredCustFieldsErrorMap;
	}

	/**
	 * @param requiredCustFieldsErrorMap the requiredCustFieldsErrorMap to set
	 */
	public void setRequiredCustFieldsErrorMap(
			HashMap<String, ArrayList<String>> requiredCustFieldsErrorMap) {
		this.requiredCustFieldsErrorMap = requiredCustFieldsErrorMap;
	}
//added for jira 2885
	public Map<String, String> getPnALineErrorMessage() {
		return pnALineErrorMessage;
	}

	public void setPnALineErrorMessage(Map<String, String> pnALineErrorMessage) {
		this.pnALineErrorMessage = pnALineErrorMessage;
	}


	public String getDraftOrderList() {
		return draftOrderList;
	}


	public void setDraftOrderList(String draftOrderList) {
		this.draftOrderList = draftOrderList;
	}	
	
	private void addModificationRuleToOrderListElement(Element orderElement)
	{
		ArrayList<Element> orderLineList=SCXmlUtil.getElements(orderElement, "/OrderLines/OrderLine");
		if(orderLineList!= null && orderLineList.size()>0) {
			for(int i=0;i<orderLineList.size();i++)
			{
				Element orderLineElement=orderLineList.get(i);		
				Element modificationsElem=SCXmlUtil.createChild(orderLineElement, "Modifications");
				Element modificationElem=SCXmlUtil.createChild(modificationsElem, "Modification");
				modificationElem.setAttribute("ModificationType", "DELIVERY_CODE");
				modificationElem.setAttribute("ModificationAllowed", "N");
				modificationsElem.appendChild(modificationElem);
				
				modificationElem=SCXmlUtil.createChild(modificationsElem, "Modification");
				modificationElem.setAttribute("ModificationType", "SHIP_NODE");
				modificationElem.setAttribute("ModificationAllowed", "Y");
				modificationsElem.appendChild(modificationElem);				
				
				modificationElem=SCXmlUtil.createChild(modificationsElem, "Modification");
				modificationElem.setAttribute("ModificationType", "CHANGE_ORDER_DATE");
				modificationElem.setAttribute("ModificationAllowed", "N");
				modificationsElem.appendChild(modificationElem);
				
				modificationElem=SCXmlUtil.createChild(modificationsElem, "Modification");
				modificationElem.setAttribute("ModificationType", "REQ_SHIP_DATE");
				modificationElem.setAttribute("ModificationAllowed", "Y");
				modificationsElem.appendChild(modificationElem);
				
				modificationElem=SCXmlUtil.createChild(modificationsElem, "Modification");
				modificationElem.setAttribute("ModificationType", "ADD_QUANTITY");
				modificationElem.setAttribute("ModificationAllowed", "Y");
				modificationsElem.appendChild(modificationElem);
				
				modificationElem=SCXmlUtil.createChild(modificationsElem, "Modification");
				modificationElem.setAttribute("ModificationType", "CANCEL");
				modificationElem.setAttribute("ModificationAllowed", "Y");
				modificationsElem.appendChild(modificationElem);
				
				modificationElem=SCXmlUtil.createChild(modificationsElem, "Modification");
				modificationElem.setAttribute("ModificationType", "CHANGE_BUNDLE_DEFINITION");
				modificationElem.setAttribute("ModificationAllowed", "N");
				modificationsElem.appendChild(modificationElem);
			}
		}
	}
	
	public String getImagePath(Element primaryInfo) {
		String imageUrl = "/xpedx/images/INF_150x150.jpg";
		XPEDXSCXmlUtils xpedxScxmlUtil = new XPEDXSCXmlUtils();
		if(primaryInfo!=null) {
			String ImageLocation = xpedxScxmlUtil.getAttribute(primaryInfo, "ImageLocation");
			String ImageID = xpedxScxmlUtil.getAttribute(primaryInfo, "ImageID");
			if(ImageLocation!= null && ImageID!=null && ImageLocation!="" && ImageID!="") {
				if(ImageLocation.lastIndexOf("/") == ImageLocation.length()-1)
					imageUrl = ImageLocation+ImageID;
				else
					imageUrl = ImageLocation+"/"+ImageID;
			}
		}
		return imageUrl;
	}
	
}

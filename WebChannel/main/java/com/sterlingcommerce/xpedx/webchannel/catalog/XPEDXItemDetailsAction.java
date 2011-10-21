package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.xpath.XPathExpressionException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.comergent.appservices.configuredItem.XMLUtils;
import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.catalog.ItemDetailsAction;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.utilities.WCConstants;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItem;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceAndAvailability;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSException;

public class XPEDXItemDetailsAction extends ItemDetailsAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4415525560179441485L;

	public String execute() {
		// Start - Webtrends meta tag
			HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
			HttpSession localSession = httpRequest.getSession();
		// End - Webtrends meta tag
		
		//Jira 2421
		 if(request.getParameter("selectedView")!=null){
	        	localSession.setAttribute("selView", request.getParameter("selectedView"));
	        }
		// end for Jira 2421
		
		String returnVal = null;
		try {
			String orderedQty = null;
			try {
				if (getItemID().trim().equals("")) {
					setItemID("INVALID_ITEM");
				}
			} catch (Exception e) {
				setItemID("INVALID_ITEM");
			}
			returnVal = super.execute();
			setMSDSUrls();
			if (getWCContext().getCustomerId() != null && !wcContext.isGuestUser()) {
				
				getItemDetails();
				getCustomerDetails(); // Template Trim Required
//				getExtnItemDetails();//Extn Template should be trimmed
				getCustomerPartNumber(); 
				getCustomerLineDetails(); 

//				getAllItemList(); // This is not being used at all in the item Details Page so removed it for performance
				//call it after calling getCustomerDetails() as shipFromDivision
				// would be available for logged in users.
				try{
					getRelatedItems(); // Lets use the same method used for the My Items List action
				}catch (Exception ex){
					LOG.info("getRelatedItems retrival failed : " + ex.getMessage());
				}
				getExtnItemDetails();
				getItemUOMs();

				if (requestedUOM == null && itemUOMsMap != null
						&& !itemUOMsMap.isEmpty()) {
					requestedUOM = (String) itemUOMsMap.keySet().iterator()
							.next();
				} else {
					setUpdateAvailability(true);
				}
				
				if (requestedQty != null && requestedQty.trim().length() > 0){
					orderedQty = requestedQty;
				}
				else if(minOrderQty != null && minOrderQty.equals("0.00")){
					orderedQty = "1";
				}
				else if (minOrderQty != null && minOrderQty.trim().length() > 0){
						orderedQty = minOrderQty;
				}else{
						orderedQty = "1";
				}
			}
			
		} catch (Exception ex) {
			LOG.error("Exception while processing item details: ", ex);
		}
		
		// Webtrends meta tag starts here		
        setUpdatePA(true);
		localSession.setAttribute("isUpdatePA", isUpdatePA());		
		// Webtrends meta tag end here
		return returnVal;
	}
	
	public String callPriceAndAvailability(){
		String returnVal = SUCCESS;
		ArrayList<XPEDXItem> inputItems = new ArrayList<XPEDXItem>();
		XPEDXItem item = new XPEDXItem();
		item.setLegacyProductCode(itemID);
		item.setRequestedQtyUOM(requestedUOM);
		item.setRequestedQty(pnaRequestedQty);
		inputItems.add(item);
		XPEDXPriceAndAvailability pna = XPEDXPriceandAvailabilityUtil.getPriceAndAvailability(inputItems);
		
		//This takes care of displaying message to Users based on ServiceDown, Transmission Error, HeaderLevelError, LineItemError 
		ajaxDisplayStatusCodeMsg  =   XPEDXPriceandAvailabilityUtil.getAjaxDisplayStatusCodeMsg(pna) ;
		setAjaxLineStatusCodeMsg(ajaxDisplayStatusCodeMsg);
		
		if(null == pna || pna.getItems() == null || YFCCommon.isVoid(pna.getTransactionStatus()) || pna.getTransactionStatus().equals("F")){
		//	setAjaxLineStatusCodeMsg("Error getting pricing detail: Transaction Failure");
			return returnVal;
		}
		if(pna.getHeaderStatusCode()!=null && !pna.getHeaderStatusCode().equalsIgnoreCase("00")){
		//	setAjaxLineStatusCodeMsg("Error getting pricing detail: HeaderStatusCode Error");
			return returnVal;
		}
		pnaHoverMap = XPEDXPriceandAvailabilityUtil.getPnAHoverMap(pna.getItems());
		Vector<XPEDXItem> items = pna.getItems();
		String lineStatusErrorMsg = "";
		for (XPEDXItem pandAItem1 : items) {
			if (pandAItem1.getLegacyProductCode().equals(itemID)) {
				lineStatusErrorMsg = XPEDXPriceandAvailabilityUtil
						.getPnALineErrorMessage(pandAItem1);
				ajaxDisplayStatusCodeMsg = ajaxDisplayStatusCodeMsg + lineStatusErrorMsg ;
				if (!YFCCommon.isVoid(lineStatusErrorMsg)) {
					setAjaxLineStatusCodeMsg(ajaxDisplayStatusCodeMsg);
				} else {
					processPandAData(pandAItem1);
				}
				break;
			}
		}
		return SUCCESS;
	}


	protected void getCustomerLineDetails() throws Exception {
		//get the map from the session. if null query the DB
		HashMap<String,String> customerFieldsSessionMap = getCustomerFieldsMapfromSession();
		HashMap<String,String> customerFieldsMap =new HashMap<String,String>();
        if(null != customerFieldsSessionMap && customerFieldsSessionMap.size() >= 0){
        	LOG.debug("Found customerFieldsMap in the session");
        }else
	    {
	       //Commenting for perormance issue since we already have util method calling that instead
        	/* 
			customerId = wcContext.getCustomerId();
			organizationCode = wcContext.getStorefrontId();
			HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
	        HttpSession localSession = httpRequest.getSession();
			Set mashupSet = buildSetFromDelmitedList("draftOrderGetCustomerLineFields");
			Map outputMap = prepareAndInvokeMashups(mashupSet);
			Element outputEl = (Element) outputMap.get("draftOrderGetCustomerLineFields");
			Element customerOrganizationExtnEle = XMLUtilities.getChildElementByName(outputEl, "Extn");
			customerFieldsSessionMap=XPEDXOrderUtils.getCustomerLineFieldMap(outputEl.getOwnerDocument());
			//set this in the session
			localSession.setAttribute("customerFieldsSessionMap", customerFieldsMap);*/
        	XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
	    }
		for (String field : customerFieldsSessionMap.keySet())
    	{
    		if("CustomerPONo".equals(field))
    		{
    			isCustomerPO="Y";
    			customerPOLabel=customerFieldsSessionMap.get(field);
    		}
    		if("CustLineAccNo".equals(field))
    		{
    			isCustomerLinAcc="Y";
    			custLineAccNoLabel=customerFieldsSessionMap.get(field);
    		}
    	}
    	
	}
	
	protected Set buildSetFromDelmitedList(String delimitedList)
    {
        Set returnSet = new LinkedHashSet<String> ();
        //tokenize the ids using comma as the delimiter
        String tempArr[] = delimitedList.split(WCConstants.MASHUP_IDS_DELIMITER);        
        for(String s : tempArr) {
            s = s.trim();
            returnSet.add(s);
        }

        return returnSet;
    }

	protected HashMap getCustomerFieldsMapfromSession(){
		/*HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();*/
        XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
        HashMap customerFieldsSessionMap = (HashMap)XPEDXWCUtils.getObjectFromCache("customerFieldsSessionMap");
        return customerFieldsSessionMap;
	}

	@SuppressWarnings("unchecked")
	private void setMSDSUrls() {
		if(m_itemListElem!=null) {
			ArrayList<Element> assetList = new ArrayList<Element>();
			ArrayList<Element> MSDSAssetList = SCXmlUtil.getElementsByAttribute(m_itemListElem, "Item/AssetList/Asset", "Type", XPEDXConstants.MSDS_ASSET_TYPE_URL);
			ArrayList<Element> MSDSAssetListDataSheet = SCXmlUtil.getElementsByAttribute(m_itemListElem, "Item/AssetList/Asset", "Type", XPEDXConstants.MSDS_ASSET_TYPE_DATA_SHEET);
			if(!SCUtil.isVoid(MSDSAssetList))
				assetList.addAll(MSDSAssetList);
			if(!SCUtil.isVoid(MSDSAssetListDataSheet))
				assetList.addAll(MSDSAssetListDataSheet);
			XPEDXSCXmlUtils xpedxSCXmlUtils = new XPEDXSCXmlUtils();
			if(assetList!=null && assetList.size()>0) {
				//commented for JIRA 2853, to display msds link
				//Iterator<Element> msdsIter = MSDSAssetList.iterator();
				Iterator<Element> msdsIter = assetList.iterator();
				while(msdsIter.hasNext()) {
					Element msdsAssetElem = msdsIter.next();
					String assetType = xpedxSCXmlUtils.getAttribute(msdsAssetElem, "Type");
					String msdsLocation = xpedxSCXmlUtils.getAttribute(msdsAssetElem, "ContentLocation");
					String msdsContentId = xpedxSCXmlUtils.getAttribute(msdsAssetElem, "ContentID");
					String msdsLink = msdsLocation+"/"+msdsContentId;
					String msdsLinkDesc = xpedxSCXmlUtils.getAttribute(msdsAssetElem, "Description");
					if("URL".equalsIgnoreCase(assetType)) {
						msdsLink = msdsLocation;
						msdsLinkDesc = XPEDXConstants.MSDS_URL_DISPLAY;
					}
					if(msdsLinkMap.isEmpty())
						msdsLinkMap = new HashMap<String, String>();
					msdsLinkMap.put(msdsLinkDesc, msdsLink);
				}
			}
		}		
	}



	public String getAjaxLineStatusCodeMsg() {
		return ajaxLineStatusCodeMsg;
	}

	public void setAjaxLineStatusCodeMsg(String ajaxLineStatusCodeMsg) {
		this.ajaxLineStatusCodeMsg = ajaxLineStatusCodeMsg;
	}

	private void processPandAData(XPEDXItem pandAItem) {
		priceCurrencyCode = pandAItem.getPriceCurrencyCode();
		pricingUOM = pandAItem.getPricingUOM();
		String pricingUOMUnitPrice = pandAItem.getUnitPricePerPricingUOM();
		requestedUOM = pandAItem.getRequestedQtyUOM();
		BigDecimal pricingUOMPrice = new BigDecimal(pricingUOMUnitPrice);
		BigDecimal prodWeight = null;
		BigDecimal priceForCWTUom = null;
		BigDecimal priceForTHUom = null;
		String BaseUomDesc = null;
		String RequestedQtyUOMDesc = null;
		String PricingUOMDesc = null;
		String cwtUOMDesc = null;
		String thUOMDesc = null;
		
		setIsBracketPricing("false");
		try {
			BaseUomDesc = XPEDXWCUtils.getUOMDescription(baseUOM);
			RequestedQtyUOMDesc = XPEDXWCUtils.getUOMDescription(pandAItem
					.getRequestedQtyUOM());
			PricingUOMDesc = XPEDXWCUtils.getUOMDescription(pandAItem
					.getPricingUOM());
		} catch (Exception e) {

		}
		
//		displayUOMs.add(BaseUomDesc); //removed as specified in the bug 185 comments on 03/Jan/11 3:58 PM by Barb Widmer
		
		if (XPEDXPriceandAvailabilityUtil.TH_UOM_M.equalsIgnoreCase(pricingUOM) || XPEDXPriceandAvailabilityUtil.TH_UOM_A.equalsIgnoreCase(pricingUOM)) {
			// hardcode for now.
			//displayUOMs.add("Cwt");
			if (prodMweight != null && prodMweight.trim().length() > 0 )
				prodWeight = new BigDecimal(prodMweight);
			else
				prodWeight = new BigDecimal(100); // this will make pricing for
			// TH and CWT same.
			try
			{				
			//priceForCWTUom = pricingUOMPrice.divide(prodWeight.divide(new BigDecimal(100)),2, RoundingMode.HALF_UP);
			priceForCWTUom = XPEDXPriceandAvailabilityUtil.divideBDWithPrecision(pricingUOMPrice , XPEDXPriceandAvailabilityUtil.divideBDWithPrecision(prodWeight , new BigDecimal(100)) );
			cwtUOMDesc = XPEDXWCUtils.getUOMDescription(XPEDXPriceandAvailabilityUtil.CWT_UOM_M);
			if(cwtUOMDesc==null || cwtUOMDesc.length()==0)
				cwtUOMDesc = XPEDXWCUtils.getUOMDescription(XPEDXPriceandAvailabilityUtil.CWT_UOM_A);
			displayUOMs.add(cwtUOMDesc);
			}
			
			catch(Exception e) 
			{				
				priceForCWTUom=pricingUOMPrice;						
			}
			
		}
		//Moved code from above to bottom for JIRA 1835
		displayUOMs.add(PricingUOMDesc);
		if (XPEDXPriceandAvailabilityUtil.CWT_UOM_M.equalsIgnoreCase(pricingUOM) || XPEDXPriceandAvailabilityUtil.CWT_UOM_A.equalsIgnoreCase(pricingUOM)) {
			//displayUOMs.add("Thousand");
			if (prodMweight != null && prodMweight.trim().length() > 0 )
				prodWeight = new BigDecimal(prodMweight);
			else
				prodWeight = new BigDecimal(100); // this will make
			// pricing for CW
			try
			{
				//priceForTHUom = pricingUOMPrice.multiply(prodWeight.divide(new BigDecimal(100)));
				priceForTHUom = pricingUOMPrice.multiply(XPEDXPriceandAvailabilityUtil.divideBDWithPrecision(prodWeight  ,new BigDecimal(100)) );
				thUOMDesc = XPEDXWCUtils.getUOMDescription(XPEDXPriceandAvailabilityUtil.TH_UOM_M);
				if(thUOMDesc==null || thUOMDesc.length()==0)
					thUOMDesc = XPEDXWCUtils.getUOMDescription(XPEDXPriceandAvailabilityUtil.TH_UOM_A);
				displayUOMs.add(thUOMDesc);
			}
			catch(Exception e)
			{	
				priceForTHUom = pricingUOMPrice;
			}
		}
		if(pricingUOM!=null && !pricingUOM.equals(pandAItem.getRequestedQtyUOM()))
			displayUOMs.add(RequestedQtyUOMDesc);
		displayUOMs.add(" ");

		if (pricingUOMConvFactor != null
				&& ((new BigDecimal(0)).compareTo(new BigDecimal(
						pricingUOMConvFactor)) <= 0)) {
			pricingUOMConvFactor = "1";
		}
		//Commenting the following code as it causes nullpointer when item is not entitled, base price is anyway not required
		/*BigDecimal basePrice = pricingUOMPrice.divide(new BigDecimal(
				pricingUOMConvFactor));*/

//		displayPriceForUoms.add(basePrice.toString());  //removed as specified in the bug 185 comments on 03/Jan/11 3:58 PM by Barb Widmer
		
		if (priceForCWTUom != null){
			displayPriceForUoms.add(priceForCWTUom.toString());
		}
		//Moved code from above to bottom for JIRA 1835
		displayPriceForUoms.add(pricingUOMUnitPrice);

		if (priceForTHUom != null){
			displayPriceForUoms.add(priceForTHUom.toString());
		}
		if(pricingUOM!=null && !pricingUOM.equals(pandAItem.getRequestedQtyUOM()))
			displayPriceForUoms.add(pandAItem.getUnitPricePerRequestedUOM());
		displayPriceForUoms.add(pandAItem.getExtendedPrice());

		bracketsPricingList = pandAItem.getBrackets();
		setIsBracketPricing(XPEDXPriceandAvailabilityUtil.isBracketPricingAvailable(bracketsPricingList));
		
		itemCost = pandAItem.getItemCost();
		itemCostCurrency = pandAItem.getCostCurrencyCode();

	}

	private void getItemDetails() throws Exception {
		Element itemEle = XMLUtilities.getElement(m_itemListElem, "Item");
		Element primaryInfoEle = XMLUtilities.getElement(itemEle,"PrimaryInformation");
		Element itemExtnEle = XMLUtilities.getElement(itemEle, "Extn");
		
		minOrderQty = SCXmlUtil.getAttribute(primaryInfoEle,"MinOrderQuantity");
		pricingUOM = SCXmlUtil.getAttribute(primaryInfoEle, "PricingUOM");
		pricingUOMConvFactor = SCXmlUtil.getAttribute(primaryInfoEle,"PricingQuantityConvFactor");
		baseUOM = SCXmlUtil.getAttribute(itemEle, "UnitOfMeasure");
		prodMweight = SCXmlUtil.getAttribute(itemExtnEle, "ExtnMwt");
		certFlag = SCXmlUtil.getAttribute(itemExtnEle, "ExtnCert");
		MPC = SCXmlUtil.getAttribute(itemExtnEle, "ExtnMpc");
		ManufacturerPartNumber = SCXmlUtil.getAttribute(primaryInfoEle, "ManufacturerItem");
		//added for jira 2084
		//Manufacturer = SCXmlUtil.getAttribute(primaryInfoEle, "ManufacturerName");
		//added for jira 2511
		Manufacturer = SCXmlUtil.getAttribute(itemExtnEle, "ExtnSupplierNameDisplay");
		
	}

	private void getExtnItemDetails()  throws Exception {
		itemOrderMultipleMap = new HashMap();
		if(itemExtnElement == null) {
			Document itemExtnDoc = XPEDXOrderUtils.getXPEDXItemAssociation(wcContext.getCustomerId(), shipFromDivision, itemID, getWCContext());
			if(itemExtnDoc!=null)
				itemExtnElement = SCXmlUtil.getChildElement(itemExtnDoc.getDocumentElement(),"XPXItemExtn");
		}
		if(itemExtnElement == null) {
			String orderMul = "1";
			setIsStocked("N");
			if(SCUtil.isVoid(orderMul))
				orderMul="1";
			setOrderMultiple(orderMul);
			itemOrderMultipleMap.put(itemID, orderMul);
		}
		else {
			String div = itemExtnElement.getAttribute("XPXDivision");
			if (shipFromDivision.equalsIgnoreCase(div)) {
				String orderMul = itemExtnElement.getAttribute("OrderMultiple");
				setIsStocked(itemExtnElement.getAttribute("InventoryIndicator"));
				if(SCUtil.isVoid(orderMul))
					orderMul="1";
				setOrderMultiple(orderMul);
				itemOrderMultipleMap.put(itemID, orderMul);
			}
		}
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

	public void getCustomerDetails() throws Exception {
		Document outputDoc = null;
/*	Removed this Database call as at Line 368 customer details call will take care of the requirement
 * 		Document outputDoc1 = null;
		Map<String, String> valueMap1 = new HashMap<String, String>();
		valueMap1.put("/Customer/@CustomerID", getWCContext().getCustomerId());
		valueMap1.put("/Customer/@OrganizationCode", getWCContext()
				.getStorefrontId());
		Element input1 = WCMashupHelper.getMashupInput("xpedxgetCustomerList",
				valueMap1, wcContext.getSCUIContext());
		Object obj1 = WCMashupHelper.invokeMashup("xpedxgetCustomerList",
				input1, wcContext.getSCUIContext());
		outputDoc1 = ((Element) obj1).getOwnerDocument();
		Element outputE2 = outputDoc1.getDocumentElement();
		Element customerOrganizationExtnEle2 = SCXmlUtil.getXpathElement(outputE2, "Customer/Extn");
		Email1 = SCXmlUtil.getAttribute(customerOrganizationExtnEle2,
				"ExtnSampleRoomEmailAddress");
		-------- End of the unnecessary database call ---------
		----------- Email1 is set at Line 417 , XNGTP-1032------------ jkotha
				 */
		String custDetailsMashupId = "xpedx-itemdetails-customerInfo";
		outputDoc = XPEDXWCUtils.getCustomerDetails(getWCContext().getCustomerId(), getWCContext().getStorefrontId(),custDetailsMashupId); // trim of the template is Required
		Element outputEl = outputDoc.getDocumentElement();

		Element customerOrganizationExtnEle = XMLUtilities.getElement(outputEl,"Extn");
		shipFromDivision = SCXmlUtil.getAttribute(customerOrganizationExtnEle,"ExtnShipFromBranch");
		//Fetching and setting the other customer profile settings here to use in getCustomerPartNumber
		envCode = SCXmlUtil.getAttribute(customerOrganizationExtnEle,"ExtnEnvironmentCode");
		customerCode = SCXmlUtil.getAttribute(customerOrganizationExtnEle,"ExtnCompanyCode");
		customerLegNo = SCXmlUtil.getAttribute(customerOrganizationExtnEle,"ExtnLegacyCustNumber");
		useOrderMulUOMFlag = SCXmlUtil.getAttribute(customerOrganizationExtnEle,"ExtnUseOrderMulUOMFlag");
		customerBranch = shipFromDivision;
		/*customerBranch =  SCXmlUtil.getAttribute(customerOrganizationExtnEle,
				"ExtnCustomerDivision");*/
		//Getting the customer Sku flag from MSAP Customer
		custSKU = (String)wcContext.getWCAttribute(XPEDXConstants.CUSTOMER_USE_SKU,WCAttributeScope.LOCAL_SESSION);
		DivisionNumber = SCXmlUtil.getAttribute(customerOrganizationExtnEle,"ExtnCustomerDivision");
		SalesRepresentative1 = SCXmlUtil.getAttribute(customerOrganizationExtnEle, "ExtnPrimarySalesRep");
		setSampleRequestFlagInSession();// This can be edited
		Element customerOrganizationExtnEle1 = null;
		NodeList CustomerList = outputDoc.getElementsByTagName("PersonInfo");
		for (int customerNo = 0; customerNo < CustomerList.getLength(); customerNo++) {
			customerOrganizationExtnEle1 = (Element) CustomerList.item(0);

		}
		Email2 = SCXmlUtil.getAttribute(customerOrganizationExtnEle1,"EMailID");
		Email1 = SCXmlUtil.getAttribute(customerOrganizationExtnEle1,"ExtnSampleRoomEmailAddress");
		CustomerName = SCXmlUtil.getAttribute(customerOrganizationExtnEle1,
				"FirstName")
				+ ""
				+ SCXmlUtil.getAttribute(customerOrganizationExtnEle1,
						"MiddleName")
				+ ""
				+ SCXmlUtil.getAttribute(customerOrganizationExtnEle1,
						"LastName");
		CustomerContactName = SCXmlUtil.getAttribute(
				customerOrganizationExtnEle1, "AddressID");
		ContactPhoneNumber = SCXmlUtil.getAttribute(
				customerOrganizationExtnEle1, "DayPhone");
		ShippingAddress1 = SCXmlUtil.getAttribute(
				customerOrganizationExtnEle1, "AddressLine1");
		ShippingAddress2 = SCXmlUtil.getAttribute(
				customerOrganizationExtnEle1, "AddressLine2");
		ShippingAddress3 = SCXmlUtil.getAttribute(
				customerOrganizationExtnEle1, "AddressLine3");
		CityRequest = SCXmlUtil.getAttribute(customerOrganizationExtnEle1,
				"City");
		StateRequest = SCXmlUtil.getAttribute(customerOrganizationExtnEle1,
				"State");
		ZipRequest = SCXmlUtil.getAttribute(customerOrganizationExtnEle1,
				"ZipCode");
		CustomerList = outputDoc.getElementsByTagName("CustomerPaymentMethod");
		for (int customerNo1 = 0; customerNo1 < CustomerList.getLength(); customerNo1++) {
			customerOrganizationExtnEle1 = (Element) CustomerList.item(0);
		}
		AccountNumber = SCXmlUtil.getAttribute(customerOrganizationExtnEle1,
				"CustomerAccountNo");
	}

	protected void getItemUOMs() throws Exception {
		/*String customerId = wcContext.getCustomerId();
		String organizationCode = wcContext.getStorefrontId();

		itemUOMsMap = XPEDXOrderUtils.getXpedxUOMList(customerId, itemID,
				organizationCode);*/
		LinkedHashMap<String, String> wUOMsToConversionFactors = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> uomListMap = new LinkedHashMap<String, String>();
		if(getItemListElem() != null) {
			Element itemElement = SCXmlUtil.getChildElement(m_itemListElem,"Item");
			Element alternateUomList = SCXmlUtil.getChildElement(itemElement,"AlternateUOMList");
			NodeList alternateUoms = alternateUomList.getChildNodes();
			int alternateUomLenght = alternateUoms.getLength();
			for (int i = 0; i < alternateUomLenght; i++) {
				Node AlternateUOM = alternateUoms.item(i);
				NamedNodeMap namedNodeMap = AlternateUOM.getAttributes();
				Node IsOrderingUOMNode = namedNodeMap
						.getNamedItem("IsOrderingUOM");
				String IsOrderingUOM = IsOrderingUOMNode.getTextContent();
				if (IsOrderingUOM != null && IsOrderingUOM.equals("Y")) {
					Node unitOfMeasureNode = namedNodeMap
							.getNamedItem("UnitOfMeasure");
					String unitOfMeasure = unitOfMeasureNode.getTextContent();
					Node quantityNode = namedNodeMap.getNamedItem("Quantity");
					String quantity = quantityNode.getTextContent();
					wUOMsToConversionFactors.put(unitOfMeasure, quantity);
				}
			}
		}
		// sending the entry type as null as this is not a B2B request
		handleXpxItemcustXrefList(itemID,useOrderMulUOMFlag, orderMultiple,wUOMsToConversionFactors,null);
		
		if (baseUOM != null
				&& baseUOM.trim().length() > 0
				&& (ExtnIsCustUOMExcl == null
						|| ExtnIsCustUOMExcl.trim().length() == 0 || !ExtnIsCustUOMExcl
						.equals("Y")))
		{
			uomListMap.put(baseUOM, "1");
		}
		uomListMap.putAll(wUOMsToConversionFactors);
				
		displayItemUOMsMap = new HashMap();
		itemUOMsMap = uomListMap;
		/*for (Iterator it = itemUOMsMap.keySet().iterator(); it.hasNext();) {
			String uomDesc = (String) it.next();
			Object o = itemUOMsMap.get(uomDesc);
			if("1".equals(o))
			{
				displayItemUOMsMap.put(uomDesc,XPEDXWCUtils.getUOMDescription(uomDesc));
			}
			else{
				displayItemUOMsMap.put(uomDesc,XPEDXWCUtils.getUOMDescription(uomDesc)
						+ " (" + o + ")");
			}
			
		}*/
		
		if(itemUOMsMap!=null && itemUOMsMap.keySet()!=null) {
			
			Iterator<String> iterator = itemUOMsMap.keySet().iterator();
			while(iterator.hasNext()) {
					String uomCode = iterator.next();
					String convFactor = (String) itemUOMsMap.get(uomCode);
					long convFac = Math.round(Double.parseDouble(convFactor));
					if(1 == convFac) {
						displayItemUOMsMap.put(uomCode, XPEDXWCUtils.getUOMDescription(uomCode));
					}
					else {
						//--FXD-- Adding space between UOM Description & Conversion factor
						displayItemUOMsMap.put(uomCode, XPEDXWCUtils.getUOMDescription(uomCode)+" ("+convFac+")" );
					}
			}
		}
		
		//itemOrderMultipleMap = XPEDXOrderUtils.getOrderMultipleForItems(items);

	}
	
	private void handleXpxItemcustXrefList(String itemID,String useOrderMulUOMFlag, String orderMultiple,
			HashMap<String, String> wUOMsToConversionFactors, String entryType)
			throws YFSException, RemoteException, YIFClientCreationException {
		
		Node customerUnitNode = null;
		
		ArrayList<Element> XpxItemcustXrefList = SCXmlUtil.getElements(itemCustXrefList, "XPXItemcustXref[@LegacyItemNumber="+itemID+"]");
		int length3 = XpxItemcustXrefList.size();
		for (int m = 0; m < length3; m++) {
			Node XpxItemcustXref = XpxItemcustXrefList.get(m);
			NamedNodeMap XpxItemcustXrefAttributes = XpxItemcustXref.getAttributes();
			Node ExtnIsCustUOMExclNode = XpxItemcustXrefAttributes.getNamedItem("IsCustUOMExcl");
			if (ExtnIsCustUOMExclNode != null) {
				ExtnIsCustUOMExcl = ExtnIsCustUOMExclNode.getTextContent();
			}
			
			
			if(entryType == null || entryType.trim().length()<=0)
			{
			   customerUnitNode = XpxItemcustXrefAttributes
					.getNamedItem("CustomerUom");
			}
			else
			{
				//Its a B2B request
				customerUnitNode = XpxItemcustXrefAttributes
				.getNamedItem("LegacyUom");
			}
			if(customerUnitNode==null)
			{
				customerUnitNode = XpxItemcustXrefAttributes
				.getNamedItem("CustomerUnit");
			}
			String customerUnit = customerUnitNode.getTextContent();
			Node ConvFactorNode = XpxItemcustXrefAttributes.getNamedItem("ConvFactor");
			String ConvFactor = ConvFactorNode.getTextContent();
			
			if (ExtnIsCustUOMExcl != null && ExtnIsCustUOMExcl.equals("Y")) {
				wUOMsToConversionFactors.clear();
				wUOMsToConversionFactors.put(customerUnit, ConvFactor);
				return;
			}
			// Null check added.
			if (useOrderMulUOMFlag != null && useOrderMulUOMFlag.equals("Y")) {
				int conversion = getConversion(ConvFactor, orderMultiple);
				if (conversion != -1 && customerUnit != null
						&& customerUnit.length() > 0) {
					if (currentConversion == 0
							|| (currentConversion != 0 && conversion < currentConversion)) {
						lowestConvUOM = customerUnit;
						currentConversion = conversion;
					}
				}
			}
			wUOMsToConversionFactors.put(customerUnit, ConvFactor);
		}
	}
	
	private int getConversion(String convFactor, String orderMultiple) {
		if (convFactor != null && convFactor.length() > 0
				&& orderMultiple != null && orderMultiple.length() > 0) {
			double convFactorD = Double.parseDouble(convFactor);
			double orderMultipleD = Double.parseDouble(orderMultiple);
			double factor = (convFactorD / orderMultipleD);
			if (Math.abs(factor) == factor) {
				return (int) Math.abs(factor);
			}
		}
		return -1;
	}
	
	private void getCustomerPartNumber() throws Exception {
		/*String customerId = wcContext.getCustomerId();
		StringTokenizer str = new StringTokenizer(customerId, "-");
		if (str.hasMoreTokens())
			customerBranch = str.nextToken();
		if (str.hasMoreTokens())
			customerLegNo = str.nextToken();
		if (str.hasMoreTokens())
			customerSuffix = str.nextToken();
		if (str.hasMoreTokens())
			customerCode = str.nextToken();
		if (str.hasMoreTokens())
			envCode = str.nextToken();
        */
		//String customerUseSKU = "1";//(String) wcContext.getWCAttribute("customerUseSKU");
		//if("1".equalsIgnoreCase(custSKU)){
			IWCContext wcContext =getWCContext();
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/XPXItemcustXref/@EnvironmentCode", envCode);
			valueMap.put("/XPXItemcustXref/@CustomerDivision",DivisionNumber);
			valueMap.put("/XPXItemcustXref/@CompanyCode",customerCode);
			valueMap.put("/XPXItemcustXref/@LegacyItemNumber", itemID);
			valueMap.put("/XPXItemcustXref/@CustomerNumber", customerLegNo);
			Element input;
			try {
				input = WCMashupHelper.getMashupInput("xpedxItemCustXRef", valueMap, wcContext.getSCUIContext());		
				itemCustXrefList = (Element)WCMashupHelper.invokeMashup("xpedxItemCustXRef", input, wcContext.getSCUIContext());
				Element itemCustXrefEle = XMLUtilities.getElement(itemCustXrefList,"XPXItemcustXref");
				custPartNumber = SCXmlUtil.getAttribute(itemCustXrefEle,"CustomerItemNumber");
			} catch (Exception e) {
				LOG.error("Error while retrieving the customer part number:getCustomerPartNumber " + e.getMessage(), e);
			} 
			
		//}		
		/*Element outputEl = prepareAndInvokeMashup("xpedxItemCustXRef");
		Element custXrefEle = XMLUtilities.getElement(outputEl,"XPXItemcustXref");
		SCXmlUtil.getString(outputEl);
		System.out.println(SCXmlUtil.getString(custXrefEle));*/		
		//custPartNumber = SCXmlUtil.getAttribute(custXrefEle,"CustomerItemNumber");
                //Fetching the MPC from the Item details - refer method prepareXpedxItemAssociationMap
		//MPC = SCXmlUtil.getAttribute(custXrefEle, "MPC");

	}

	private void getRelatedItems() throws Exception {
		/*Element relatedItemListElem = null;
		relatedItemListElem = prepareAndInvokeMashup("xpedxRelatedItemDetails");
		Document itemDoc = relatedItemListElem.getOwnerDocument();
		NodeList nlItemAssociationsList = itemDoc
				.getElementsByTagName("XPXItemAssociations");
		Element itemAssociation = null;
		int length = nlItemAssociationsList.getLength();*/
		upgradeAssociatedItems = new ArrayList();
		complimentAssociatedItems = new ArrayList();
		alternateAssociatedItems = new ArrayList();
		upSellAssociatedItems = new ArrayList();
		crossSellAssociatedItems = new ArrayList();
        replacementAssociatedItems = new ArrayList();
		prepareXpedxItemAssociationMap();
	}
	
	protected void prepareXpedxItemAssociationMap() throws XPathExpressionException{
		String custID = wcContext.getCustomerId();
		Document itemAssociationDoc = null;
		ArrayList<String> itemList = new ArrayList<String>();
		itemList.add(itemID);
		//get the getXpedxAssociationlItemDetails
		Element relatedItemListElem = null;
		ArrayList<String> itemIDListForGetCompleteItemList = new ArrayList<String>();
		ArrayList<String> crossSellItemIDs = new ArrayList<String>();
		ArrayList<String> upSellItemIDs = new ArrayList<String>();
		try {
			if(getItemListElem()== null)
				relatedItemListElem = prepareAndInvokeMashup("xpedxRelatedItemDetails");
			else
				relatedItemListElem = getItemListElem();
			itemAssociationDoc = relatedItemListElem.getOwnerDocument();
			//itemAssociationDoc = XPEDXOrderUtils.getXpedxAssociationlItemDetails(itemList, custID, callingOrgCode, wcContext);
		} catch (CannotBuildInputException e) {
			LOG.error("Error getting National Level item association.",e);
			return;
		}
		if(null == itemAssociationDoc){
			LOG.error("No national level item association could be found");
			return;
		}
		NodeList nItemList = itemAssociationDoc.getElementsByTagName("Item");
		Element itemElement = (Element)nItemList.item(0);
		String itemID = SCXmlUtil.getAttribute(itemElement, "ItemID");
		LOG.debug("Preparing national level association for item Id "+itemID);
		Element associationTypeListElem = null;
		associationTypeListElem = XMLUtilities.getElement(itemElement, "AssociationTypeList");
	
		if (associationTypeListElem != null) {
			List<Element> crossSellElements = XMLUtilities.getElements(associationTypeListElem,"AssociationType[@Type='CrossSell']");
			List<Element> upSellElements = XMLUtilities.getElements(associationTypeListElem,"AssociationType[@Type='UpSell']");
			//Cross sell items
			for (int j = 0; j < crossSellElements.size(); j++) {
				Element associationTypeElem = crossSellElements.get(j);
				Element associationListElem = XMLUtilities.getElement(associationTypeElem, "AssociationList");
				List associationList = XMLUtilities.getChildElements(associationListElem, "Association");
				if (associationList != null&& !associationList.isEmpty()) {
					Iterator associationIter = associationList.iterator();
					while (associationIter.hasNext()) {
						Element association = (Element) associationIter.next();
						Element associateditemEl = XMLUtilities.getElement(association, "Item");
						String curritemid = XMLUtils.getAttributeValue(associateditemEl, "ItemID");
						if(!itemIDListForGetCompleteItemList.contains(curritemid)){
							itemIDListForGetCompleteItemList.add(curritemid);
						}
						if(!crossSellItemIDs.contains(curritemid)){
							crossSellItemIDs.add(curritemid);
						}
					}
				}
			}//for cross sell
			
			//Up sell items
			for (int k = 0; k < upSellElements.size(); k++) {
				Element associationTypeElem = upSellElements.get(k);
				Element associationListElem = XMLUtilities.getElement(associationTypeElem, "AssociationList");
				List associationList = XMLUtilities.getChildElements(associationListElem, "Association");
				if (associationList != null && !associationList.isEmpty()) {
					Iterator associationIter = associationList.iterator();
					while (associationIter.hasNext()) {
						Element association = (Element) associationIter.next();
						Element associateditemEl = XMLUtilities.getElement(association, "Item");
						String curritemid = XMLUtils.getAttributeValue(associateditemEl, "ItemID");
						if(!itemIDListForGetCompleteItemList.contains(curritemid)){
							itemIDListForGetCompleteItemList.add(curritemid);
						}
						if(!upSellItemIDs.contains(curritemid)){
							upSellItemIDs.add(curritemid);
						}
					}
				}
			}//for upsell
		}//if

		//Adding Alternate and Complimentary Items
		Document XPXItemExtnListElement= null;
		try {
			XPXItemExtnListElement = XPEDXOrderUtils.getXPEDXItemAssociation(custID, shipFromDivision, itemID, getWCContext());
		} catch (Exception e1) {
			LOG.error("Error getting the Item Branch Information for Item id "+itemID);
			e1.printStackTrace();
		}
//		if(primaryInfoElem!=null)ManufacturerPartNumber = primaryInfoElem.getAttribute("ManufacturerItem");
			//Get the MPC Item Id
//			MPC = extnElement.getAttribute("ExtnMpc");
			//Get the replated items
//            XPXItemExtnListElement = XMLUtilities.getElement(extnElement, "XPXItemExtnList");
			if(XPXItemExtnListElement != null){
				List<Element> xPXItemExtn = XMLUtilities.getElements(XPXItemExtnListElement.getDocumentElement(), "XPXItemExtn[@ItemID='"+itemID+"']");
				if(xPXItemExtn != null && xPXItemExtn.size() > 0){
					Iterator iterxPXItemExtn = xPXItemExtn.iterator();
					List<Element> alternateList = new ArrayList<Element>();
					List<Element> complementaryList = new ArrayList<Element>();
					List<Element> upgradeAssocationList = new ArrayList<Element>();
                                        List<Element> replacementList = new ArrayList<Element>();
					while(iterxPXItemExtn.hasNext()){
						Element xPXItemExtnElement = (Element)iterxPXItemExtn.next();
						//Check the ItemExtn for the current customer
						String companyCode = xPXItemExtnElement.getAttribute("CompanyCode");
						String environmentCode = xPXItemExtnElement.getAttribute("EnvironmentID");
						String division = xPXItemExtnElement.getAttribute("XPXDivision");
						
						if (shipFromDivision!=null && envCode!=null &&
								(!(shipFromDivision.equalsIgnoreCase(division) &&
								envCode.equalsIgnoreCase(environmentCode)))){
							continue;
						}
						itemExtnElement = xPXItemExtnElement;
                        try{
						Element xPXItemAssociationsListElement = SCXmlUtil.getChildElement(xPXItemExtnElement, "XPXItemAssociationsList");
							//Add the Replacement Items
							List<Element> replacementItemList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='R']");
							if(replacementItemList != null && replacementItemList.size() > 0){
								replacementList.addAll(replacementItemList);
							}
							List<Element> associatedItemList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='A']");
							if(associatedItemList != null && associatedItemList.size() > 0){
								alternateList.addAll(associatedItemList);
	                        }
							List<Element> complementaryItemList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='C']");
							if(complementaryItemList != null && complementaryItemList.size() > 0){
								complementaryList.addAll(complementaryItemList);
	                        }
							List<Element> upgradeItemList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='U']");
								if(upgradeItemList != null && upgradeItemList.size() > 0){
								upgradeAssocationList.addAll(upgradeItemList);
							}
						}// try block ends
						catch(XPathExpressionException e){
							
						}
					}
					ArrayList<String> altItemIds = new ArrayList<String>();
					ArrayList<String> compItemIds = new ArrayList<String>();
					ArrayList<String> upItemIds = new ArrayList<String>();
					ArrayList<String> repItemIds = new ArrayList<String>();
					//prepare the map for alternate and complimentary
					
					//get the alternateList
					if(null!=alternateList && alternateList.size() >=0){
						for(Element alterItemItem:alternateList){
							String associatedItemID = SCXmlUtil.getAttribute(alterItemItem, "AssociatedItemID");
							if(!YFCCommon.isVoid(associatedItemID) && !associatedItemID.equals("")){
								if(!itemIDListForGetCompleteItemList.contains(associatedItemID)){
									itemIDListForGetCompleteItemList.add(associatedItemID);
								}
								altItemIds.add(associatedItemID);
							}
						}
					}			
					//get the complementaryList
					if(null!=complementaryList && complementaryList.size() >=0){
						for(Element comItem:complementaryList){
							String associatedItemID = SCXmlUtil.getAttribute(comItem, "AssociatedItemID");
							if(!YFCCommon.isVoid(associatedItemID) && !associatedItemID.equals("")){
								if(!itemIDListForGetCompleteItemList.contains(associatedItemID)){
								itemIDListForGetCompleteItemList.add(associatedItemID);
								}
								compItemIds.add(associatedItemID);
							}
						}
					}
					if(null!=upgradeAssocationList && upgradeAssocationList.size() >=0){
						for(Element upgradeItem:upgradeAssocationList){
							String associatedItemID = SCXmlUtil.getAttribute(upgradeItem, "AssociatedItemID");
							if(!YFCCommon.isVoid(associatedItemID) && !associatedItemID.equals("")){
								if(!itemIDListForGetCompleteItemList.contains(associatedItemID)){
									itemIDListForGetCompleteItemList.add(associatedItemID);
									}
								upItemIds.add(associatedItemID);
							}
						}
					}
					//Get the replacement items
					if(null!=replacementList && replacementList.size() >=0){
						for(Element repItem:replacementList){
							String associatedItemID = SCXmlUtil.getAttribute(repItem, "AssociatedItemID");
							if(!YFCCommon.isVoid(associatedItemID) && !associatedItemID.equals("")){
								if(!itemIDListForGetCompleteItemList.contains(associatedItemID)){
									itemIDListForGetCompleteItemList.add(associatedItemID);
									}
								repItemIds.add(associatedItemID);
							}
						}
					}	

					//call getCompleteItemList
					Document itemDetailsListDoc = null;
					try {
						// invoking a different function which will give onyl the entitiled items - 734
						itemDetailsListDoc = XPEDXOrderUtils.getXpedxEntitledItemDetails(itemIDListForGetCompleteItemList, custID, wcContext.getStorefrontId(), wcContext);
					} catch (Exception e) {
						LOG.error("Exception while getting item details for associated items",e);
						return;
					}
					if(itemDetailsListDoc!=null) {
						NodeList itemDetailsList = itemDetailsListDoc.getElementsByTagName("Item");
						for(int i = 0;i< itemDetailsList.getLength();i++){
							Element curritem = (Element)itemDetailsList.item(i);
							String curritemID = XMLUtils.getAttributeValue(curritem, "ItemID");
							if(altItemIds.contains(curritemID)){
								alternateAssociatedItems.add(curritem);
							}
							if(compItemIds.contains(curritemID)){
								complimentAssociatedItems.add(curritem);
							}
							if(upItemIds.contains(curritemID)){
								upgradeAssociatedItems.add(curritem);
							}
							if(crossSellItemIDs.contains(curritemID)){
								crossSellAssociatedItems.add(curritem);
							}
							if(upSellItemIDs.contains(curritemID)){
								upSellAssociatedItems.add(curritem);
							}
					        if(repItemIds.contains(curritemID))
					        {
						        replacementAssociatedItems.add(curritem);
					        }
						}
					}
				}
			}
		}
	
	//Added by - Jira 1700
	private void setSampleRequestFlagInSession() throws CannotBuildInputException
	{
		if(null == wcContext.getSCUIContext().getSession().getAttribute("showSampleRequest")
				|| wcContext.getSCUIContext().getSession().getAttribute("showSampleRequest").equals(""))
		{
			XPEDXShipToCustomer shipToCustomer =(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache("shipToCustomer");
			XPEDXWCUtils.setSampleRequestFlag(shipToCustomer.getExtnSampleRequestFlag(),shipToCustomer.getExtnServiceOptCode(),wcContext);
			// Commented for performance issue
			/*String billToCustomerId = XPEDXWCUtils.getParentCustomer(wcContext.getCustomerId(), wcContext);
			XPEDXWCUtils.setSampleRequestFlag(billToCustomerId,wcContext);*/
		}
	}
	//Ends

	
	private boolean itemExistsInElementList(String itemid, ArrayList<Element> xpedxItemIDUOMToRelatedItemsList){
		boolean success = false;
		
		Iterator iterItems = xpedxItemIDUOMToRelatedItemsList.iterator();
		while(iterItems.hasNext() ){
			Element curritem = (Element)iterItems.next();
			String curritemid = XMLUtils.getAttributeValue(curritem, "ItemID");
			if(curritemid.equals(itemid)){
				success = true;
				break;
			}
		}
		
		return success;
	}

	String itemCost = null;	

	public String getItemCost() {
		return itemCost;
	}


	public void setItemCost(String itemCost) {
		this.itemCost = itemCost;
	}
	String itemCostCurrency = null;
	public String getItemCostCurrency() {
		return itemCostCurrency;
	}


	public void setItemCostCurrency(String itemCostCurrency) {
		this.itemCostCurrency = itemCostCurrency;
	}

	
	public boolean isShowSampleRequest() {
		return showSampleRequest;
	}

	public void setShowSampleRequest(boolean showSampleRequest) {
		this.showSampleRequest = showSampleRequest;
	}

	public Map getItemUOMsMap() {
		return itemUOMsMap;
	}

	public void setItemUOMsMap(Map itemUOMsMap) {
		this.itemUOMsMap = itemUOMsMap;
	}

	public Map getDisplayItemUOMsMap() {
		return displayItemUOMsMap;
	}

	public void setDisplayItemUOMsMap(Map displayItemUOMsMap) {
		this.displayItemUOMsMap = displayItemUOMsMap;
	}

	public Map getItemOrderMultipleMap() {
		return itemOrderMultipleMap;
	}

	public void setItemOrderMultipleMap(Map itemOrderMultipleMap) {
		this.itemOrderMultipleMap = itemOrderMultipleMap;
	}

	public String getCustomerBranch() {
		return customerBranch;
	}

	public void setCustomerBranch(String customerBranch) {
		this.customerBranch = customerBranch;
	}

	public String getCustomerLegNo() {
		return customerLegNo;
	}

	public void setCustomerLegNo(String customerLegNo) {
		this.customerLegNo = customerLegNo;
	}

	public String getCustomerSuffix() {
		return customerSuffix;
	}

	public void setCustomerSuffix(String customerSuffix) {
		this.customerSuffix = customerSuffix;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getEnvCode() {
		/*envCode = XPEDXWCUtils.getEnvironmentCode(getWCContext()
				.getCustomerId());*/
		return envCode;
	}

	public void setEnvCode(String envCode) {
		this.envCode = envCode;
	}

	public String getCustPartNumber() {
		return custPartNumber;
	}

	public void setCustPartNumber(String custPartNumber) {
		this.custPartNumber = custPartNumber;
	}

	public String getMPC() {
		return MPC;
	}
	
	public void setMPC(String mPC) {
		MPC = mPC;
	}
	
	public String getCustSKU() {
		return custSKU;
	}


	public void setCustSKU(String custSKU) {
		this.custSKU = custSKU;
	}


	public String getRequestedUOM() {
		return requestedUOM;
	}

	public void setRequestedUOM(String requestedUOM) {
		this.requestedUOM = requestedUOM;
	}

	public List getUpgradeAssociatedItems() {
		return upgradeAssociatedItems;
	}

	public void setUpgradeAssociatedItems(List upgradeAssociatedItems) {
		this.upgradeAssociatedItems = upgradeAssociatedItems;
	}

	public List getComplimentAssociatedItems() {
		return complimentAssociatedItems;
	}

	public void setComplimentAssociatedItems(List complimentAssociatedItems) {
		this.complimentAssociatedItems = complimentAssociatedItems;
	}

	public List getAlternateAssociatedItems() {
		return alternateAssociatedItems;
	}

	public void setAlternateAssociatedItems(List alternateAssociatedItems) {
		this.alternateAssociatedItems = alternateAssociatedItems;
	}

	public List getReplacementAssociatedItems() {
		return replacementAssociatedItems;
	}


	public void setReplacementAssociatedItems(List replacementAssociatedItems) {
		this.replacementAssociatedItems = replacementAssociatedItems;
	}


	public String getShipFromDivision() {
		return shipFromDivision;
	}

	public void setShipFromDivision(String shipFromDivision) {
		this.shipFromDivision = shipFromDivision;
	}

	public Map getItemListMap() {
		return itemListMap;
	}

	public void setItemListMap(Map itemListMap) {
		this.itemListMap = itemListMap;
	}

	public String getRequestedQty() {
		return requestedQty;
	}

	public void setRequestedQty(String requestedQty) {
		this.requestedQty = requestedQty;
	}

	public List getDisplayUOMs() {
		return displayUOMs;
	}

	public void setDisplayUOMs(List displayUOMs) {
		this.displayUOMs = displayUOMs;
	}

	public List getDisplayPriceForUoms() {
		return displayPriceForUoms;
	}

	public void setDisplayPriceForUoms(List displayPriceForUoms) {
		this.displayPriceForUoms = displayPriceForUoms;
	}

	public List getBracketsPricingList() {
		return bracketsPricingList;
	}

	public void setBracketsPricingList(List bracketsPricingList) {
		this.bracketsPricingList = bracketsPricingList;
	}

	/**
	 * @return the pnaHoverMap
	 */
	public HashMap<String, JSONObject> getPnaHoverMap() {
		return pnaHoverMap;
	}

	/**
	 * @param pnaHoverMap
	 *            the pnaHoverMap to set
	 */
	public void setPnaHoverMap(HashMap<String, JSONObject> pnaHoverMap) {
		this.pnaHoverMap = pnaHoverMap;
	}

	// Added by anil start

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public String getCustomerContactName() {
		return CustomerContactName;
	}

	public void setCustomerContactName(String CustomerContactName) {
		this.CustomerContactName = CustomerContactName;
	}

	public String getContactPhoneNumber() {
		return ContactPhoneNumber;
	}

	public void setContactPhoneNumber(String ContactPhoneNumber) {
		this.ContactPhoneNumber = ContactPhoneNumber;
	}

	public String getShippingAddress1() {
		return ShippingAddress1;
	}

	public void setShippingAddress1(String ShippingAddress1) {
		this.ShippingAddress1 = ShippingAddress1;
	}

	public String getShippingAddress2() {
		return ShippingAddress2;
	}

	public void setShippingAddress2(String ShippingAddress2) {
		this.ShippingAddress2 = ShippingAddress2;
	}

	public String getShippingAddress3() {
		return ShippingAddress3;
	}

	public void setShippingAddress3(String ShippingAddress3) {
		this.ShippingAddress3 = ShippingAddress3;
	}

	public String getCityRequest() {
		return CityRequest;
	}

	public void setCityRequest(String CityRequest) {
		this.CityRequest = CityRequest;
	}

	public String getStateRequest() {
		return StateRequest;
	}

	public void setStateRequest(String StateRequest) {
		this.StateRequest = StateRequest;
	}

	public String getZipRequest() {
		return ZipRequest;
	}

	public void setZipRequest(String ZipRequest) {
		this.ZipRequest = ZipRequest;
	}

	public String getAccountNumber() {
		return AccountNumber;
	}

	public void setAccountNumber(String AccountNumber) {
		this.AccountNumber = AccountNumber;
	}

	public String getDivisionNumber() {
		return DivisionNumber;
	}

	public void setDivisionNumber(String DivisionNumber) {
		this.DivisionNumber = DivisionNumber;
	}

	public String getSalesRepresentative1() {
		return SalesRepresentative1;
	}

	public void setSalesRepresentative1(String SalesRepresentative1) {
		this.SalesRepresentative1 = SalesRepresentative1;
	}

	public String getRequestedDeliveryDate() {
		return RequestedDeliveryDate;
	}

	public void setRequestedDeliveryDate(String RequestedDeliveryDate) {
		this.RequestedDeliveryDate = RequestedDeliveryDate;
	}

	public String getCustomerJobTitle() {
		return CustomerJobTitle;
	}

	public void setCustomerJobTitle(String CustomerJobTitle) {
		this.CustomerJobTitle = CustomerJobTitle;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String Notes) {
		this.Notes = Notes;
	}

	public String getUnitofMeasure1() {
		return UnitofMeasure1;
	}

	public void setUnitofMeasure1(String UnitofMeasure1) {
		this.UnitofMeasure1 = UnitofMeasure1;
	}

	public String getManufacturer() {
		return Manufacturer;
	}

	public void setManufacturer(String Manufacturer) {
		this.Manufacturer = Manufacturer;
	}

	public String getManufacturerPartNumber() {
		return ManufacturerPartNumber;
	}

	public void setManufacturerPartNumber(String ManufacturerPartNumber) {
		this.ManufacturerPartNumber = ManufacturerPartNumber;
	}

	public String getItemdescription() {
		return Itemdescription;
	}

	public void setItemdescription(String Itemdescription) {
		this.Itemdescription = Itemdescription;
	}

	public String getQuantity() {
		return Quantity;
	}

	public void setQuantity(String Quantity) {
		this.Quantity = Quantity;
	}

	public String getEmail1() {
		return Email1;
	}

	public void setEmail1(String Email1) {
		this.Email1 = Email1;
	}

	public String getEmail2() {
		return Email2;
	}

	public void setEmail2(String Email2) {
		this.Email2 = Email2;
	}

	// Added by anil end

	HashMap<String, JSONObject> pnaHoverMap = null;

	/**
	 * @return the certImagePath
	 */
	public String getCertImagePath() {
		return certImagePath;
	}

	public String getPriceCurrencyCode() {
		return priceCurrencyCode;
	}

	public void setPriceCurrencyCode(String priceCurrencyCode) {
		this.priceCurrencyCode = priceCurrencyCode;
	}

	public boolean isUpdateAvailability() {
		return updateAvailability;
	}

	public void setUpdateAvailability(boolean updateAvailability) {
		this.updateAvailability = updateAvailability;
	}

	public String getIsBracketPricing() {
		return isBracketPricing;
	}

	public void setIsBracketPricing(String isBracketPricing) {
		this.isBracketPricing = isBracketPricing;
	}

	public void setOrderMultiple(String orderMultiple) {
		this.orderMultiple = orderMultiple;
	}

	public String getIsStocked() {
		return isStocked;
	}

	public void setIsStocked(String stocked) {
		if (stocked.equalsIgnoreCase("W"))
			this.isStocked = "Y";
		else
			this.isStocked="N";
	}
	
	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}



	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	

	public String getIsCustomerPO() {
		return isCustomerPO;
	}


	public void setIsCustomerPO(String isCustomerPO) {
		this.isCustomerPO = isCustomerPO;
	}
	

	public String getCustomerPOLabel() {
		return customerPOLabel;
	}
	
	public void setCustomerPOLabel(String customerPOLabel) {
		this.customerPOLabel = customerPOLabel;
	}
	
	public ArrayList getUpSellAssociatedItems() {
		return upSellAssociatedItems;
	}


	public void setUpSellAssociatedItems(ArrayList upSellAssociatedItems) {
		this.upSellAssociatedItems = upSellAssociatedItems;
	}

	public ArrayList getCrossSellAssociatedItems() {
		return crossSellAssociatedItems;
	}

	public void setCrossSellAssociatedItems(ArrayList crossSellAssociatedItems) {
		this.crossSellAssociatedItems = crossSellAssociatedItems;
	}
	
	
		
	public String getIsCustomerLinAcc() {
		return isCustomerLinAcc;
	}


	public void setIsCustomerLinAcc(String isCustomerLinAcc) {
		this.isCustomerLinAcc = isCustomerLinAcc;
	}
	
	public String getPricingUOM() {
		return pricingUOM;
	}
	public void setPricingUOM(String pricingUOM) {
		this.pricingUOM = pricingUOM;
	}

	List upgradeAssociatedItems = null;
	List complimentAssociatedItems = null;
	List alternateAssociatedItems = null;
	List replacementAssociatedItems = null;
	ArrayList upSellAssociatedItems = null;
	ArrayList crossSellAssociatedItems = null;
	List displayUOMs = new ArrayList();
	List displayPriceForUoms = new ArrayList();
	List bracketsPricingList = null;
	boolean showSampleRequest = false;
	protected Map itemUOMsMap;
	protected Map displayItemUOMsMap;
	protected Map itemOrderMultipleMap;
	String customerBranch = null;
	String customerLegNo = null;
	String customerSuffix = null;
	String customerCode = null;
	String envCode = null;
	String custPartNumber = null;
	String requestedUOM = null;
	String custSKU = null;
	String MPC = null;
	String requestedQty = null;
	String minOrderQty = null;
	String baseUOM = null;
	String pricingUOM = null;
	String pricingUOMConvFactor = null;
	String prodMweight = null;
	String priceCurrencyCode = null;
	String orderMultiple = null;
	String isStocked = "N";
	// Added by anil start

	String AccountNumber = null;
	String CustomerName = null;
	String CustomerContactName = null;
	String ContactPhoneNumber = null;
	String ShippingAddress1 = null;
	String ShippingAddress2 = null;
	String ShippingAddress3 = null;
	String CityRequest = null;
	String StateRequest = null;
	String ZipRequest = null;
	String DivisionNumber = null;
	String SalesRepresentative1 = null;

	String RequestedDeliveryDate = null;
	String CustomerJobTitle = null;
	String Notes = null;
	String UnitofMeasure1 = null;
	String Manufacturer = null;
	String ManufacturerPartNumber = null;
	String Itemdescription = null;
	String Quantity = null;
	String Email1 = null;
	String Email2 = null;
	// Added by anil end

	String certFlag = null;
	 
	public String getCertFlag() {
		return certFlag;
	}

	public void setCertFlag(String certFlag) {
		this.certFlag = certFlag;
	}
	// Webtrends meta tag starts here	
	private boolean isUpdatePA;

	public boolean isUpdatePA() {
		return isUpdatePA;
	}
	public void setUpdatePA(boolean isUpdatePA) {
		this.isUpdatePA = isUpdatePA;
	}
	//Webtrends meta tag end here
	
	protected String certImagePath;
	protected String shipFromDivision;
	protected Map itemListMap;
	protected String ajaxLineStatusCodeMsg = "";
	protected String ajaxDisplayStatusCodeMsg = "";
	boolean updateAvailability = false;
	protected String isBracketPricing ;
	protected Map msdsLinkMap = new HashMap<String, String>();
	protected String isCustomerPO="N";
	protected String isCustomerLinAcc="N";
	protected String customerPOLabel="";
	protected String custLineAccNoLabel="";
	protected String organizationCode;
	protected String customerId;
	protected Element itemExtnElement;
	protected Element itemCustXrefList;
	private String useOrderMulUOMFlag;
	private String lowestConvUOM = "";
	private int currentConversion;
	private String ExtnIsCustUOMExcl = "";
	private String pnaRequestedQty;
	private static final Logger LOG = Logger
			.getLogger(XPEDXItemDetailsAction.class);

	public Map getMsdsLinkMap() {
		return msdsLinkMap;
	}



	public void setMsdsLinkMap(Map msdsLinkMap) {
		this.msdsLinkMap = msdsLinkMap;
	}
	public String getCustLineAccNoLabel() {
		return custLineAccNoLabel;
	}
	public void setCustLineAccNoLabel(String custLineAccNoLabel) {
		this.custLineAccNoLabel = custLineAccNoLabel;
	}

	public String getPnaRequestedQty() {
		return pnaRequestedQty;
	}

	public void setPnaRequestedQty(String pnaRequestedQty) {
		this.pnaRequestedQty = pnaRequestedQty;
	}

	public String getBaseUOM() {
		return baseUOM;
	}

	public void setBaseUOM(String baseUOM) {
		this.baseUOM = baseUOM;
	}

	public String getPricingUOMConvFactor() {
		return pricingUOMConvFactor;
	}

	public void setPricingUOMConvFactor(String pricingUOMConvFactor) {
		this.pricingUOMConvFactor = pricingUOMConvFactor;
	}

	public String getProdMweight() {
		return prodMweight;
	}

	public void setProdMweight(String prodMweight) {
		this.prodMweight = prodMweight;
	}
}

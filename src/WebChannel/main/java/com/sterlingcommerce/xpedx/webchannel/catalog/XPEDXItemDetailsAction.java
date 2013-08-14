package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.apache.struts2.ServletActionContext;
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
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils;
import com.sterlingcommerce.xpedx.webchannel.common.XpedxSortUOMListByConvFactor;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItem;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceAndAvailability;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.util.YFCUtils;
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
					LOG.error("getRelatedItems retrival failed : " + ex.getMessage());
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
				if(requestedDefaultUOM == null && defaultShowUOMMap!=null && !defaultShowUOMMap.isEmpty()){
					requestedDefaultUOM = (String)defaultShowUOMMap.keySet().iterator().next();
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
		XPEDXPriceAndAvailability pna = XPEDXPriceandAvailabilityUtil.getPriceAndAvailability(inputItems,isOrderData);
		
		//EB-225 - getCustomerUOM of the item, so that in price section of item details page, only display the UOM code without M_		
		if(getCustomerUOM()!=null){
			pnaRequestedCustomerUOM = getCustomerUOM();
		}
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
		orderMultipleMapFromSourcing = XPEDXPriceandAvailabilityUtil.getOrderMultipleMapFromSourcing(pna.getItems(),false);
		useOrderMultipleMapFromSourcing = XPEDXPriceandAvailabilityUtil.useOrderMultipleErrorMapFromMax(pna.getItems());
		/*if("true".equals(isOrderData))
		{
			Set<String> itemSet=pnaHoverMap.keySet();
			for(String _itemID:itemSet)
			{
				itemID=_itemID;
			}
		}*/
		Vector<XPEDXItem> items = pna.getItems();
		String lineStatusErrorMsg = "";
		for (XPEDXItem pandAItem1 : items) {
			if (pandAItem1.getLegacyProductCode().equals(itemID)) {
				//Added for XB 214 BR1
				//added for jira 2885
					pnALineErrorMessage=XPEDXPriceandAvailabilityUtil.getLineErrorMessageMap(pna.getItems());//XB 214
					lineStatusErrorMsg = XPEDXPriceandAvailabilityUtil
						.getPnALineErrorMessage(pandAItem1);
			
				//End for XB 214 BR1aa
				//	for jira 2885 ajaxDisplayStatusCodeMsg = ajaxDisplayStatusCodeMsg + " "+lineStatusErrorMsg;
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
		LinkedHashMap<String,String> customerFieldsSessionMap = getCustomerFieldsMapfromSession();		
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
        	//XPEDXWCUtils.setSAPCustomerExtnFieldsInCache(); Commented since this method is called in getCustomerFieldsMapfromSession()
	    }
        
        if(customerFieldsSessionMap!=null)
        {
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

	
	protected LinkedHashMap getCustomerFieldsMapfromSession(){
		/*HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();*/
        XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
        LinkedHashMap customerFieldsSessionMap = (LinkedHashMap)XPEDXWCUtils.getObjectFromCache("customerFieldsSessionMap");
        return customerFieldsSessionMap;
	}

	@SuppressWarnings("unchecked")
	private void setMSDSUrls() {
		//Modified this method for Jira 2634,3151
		String msdsLink="";
		String msdsLinkDesc="";
		String assetLink="";
		String assetLinkDesc="";
		//Creating new Map for Jira 2634
		assetLinkMap = new HashMap<String, String>();
		ArrayList<Element> specAssetList = new ArrayList<Element>();
		if(m_itemListElem!=null) {
			ArrayList<Element> assetList = new ArrayList<Element>();
			NodeList list = SCXmlUtil.getXpathNodes(m_itemListElem, "Item/AssetList/Asset");
			Element reqNode;
			for (int i = 0; i < list.getLength(); i++) {
				  reqNode = (Element) list.item(i);
				  assetList.add(reqNode);
                 }
			XPEDXSCXmlUtils xpedxSCXmlUtils = new XPEDXSCXmlUtils();
			if(assetList!=null && assetList.size()>0) {
				//commented for JIRA 2853, to display msds link
				Iterator<Element> assetIter = assetList.iterator();
				while(assetIter.hasNext()) {
					Element AssetElem = assetIter.next();
					String assetType = xpedxSCXmlUtils.getAttribute(AssetElem, "Type");
					String msdsLocation = xpedxSCXmlUtils.getAttribute(AssetElem, "ContentLocation");
					String msdsContentId = xpedxSCXmlUtils.getAttribute(AssetElem, "ContentID");
					//Adding .pdf to the file, as per requirement
					if(!msdsContentId.contains(".") && !msdsContentId.endsWith(".pdf")){
						msdsContentId = msdsContentId + ".pdf";
					}
					String msdsAssetId = xpedxSCXmlUtils.getAttribute(AssetElem, "AssetID");
					if(!SCUtil.isVoid(msdsLocation) && msdsLocation.endsWith("/")){
						 assetLink = msdsLocation+msdsContentId;
						 assetLinkDesc = xpedxSCXmlUtils.getAttribute(AssetElem, "Description");
					}else{
						assetLink = msdsLocation+"/"+msdsContentId;
						assetLinkDesc = xpedxSCXmlUtils.getAttribute(AssetElem, "Description");
					}
					
					//Handling "/" if exist in msdsLocation, as an extra "/" was coming
					boolean isMSDSLink=false;
					if("ITEM_DATA_SHEET".equalsIgnoreCase(assetType)) {
						msdsLinkDesc = XPEDXConstants.MSDS_URL_DISPLAY;	
						if(!SCUtil.isVoid(msdsLocation) && msdsLocation.endsWith("/")){
							 msdsLink = msdsLocation+msdsContentId;							 
						}else{
							msdsLink = msdsLocation+"/"+msdsContentId;							
						}
						isMSDSLink=true;
					}
					if("URL".equalsIgnoreCase(assetType)){
						msdsLinkDesc = XPEDXConstants.MSDS_URL_DISPLAY;	
						if(!SCUtil.isVoid(msdsLocation)){
							msdsLink = msdsLocation;							 
						}
						isMSDSLink=true;
					}
					if( msdsLinkMap.isEmpty())
					{
						msdsLinkMap = new HashMap<String, String>();						
					}
					if(isMSDSLink)
						msdsLinkMap.put(msdsLinkDesc, msdsLink);
					
					
					if(assetLinkMap.isEmpty() )
					{
						assetLinkMap = new HashMap<String, String>();
					}
					
					assetLinkMap.put(msdsAssetId, assetLink);
						
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
		List<String> uomsList= createItemUOMMap(pandAItem.getLegacyProductCode());
		if(null != prodMweight && (prodMweight.equals("0") || prodMweight.equals("0.0")) ){
			prodMweight = null;
		}
		
//		displayUOMs.add(BaseUomDesc); //removed as specified in the bug 185 comments on 03/Jan/11 3:58 PM by Barb Widmer
		
		//Moved code from below to above for JIRA XB-558
		boolean isPricingUOMAdded=false;
		boolean isThAndCwtAdded=false;
		if(PricingUOMDesc != null && PricingUOMDesc.toLowerCase().contains("thousand"))
		{
			displayUOMs.add(PricingUOMDesc);
			displayPriceForUoms.add(pricingUOMUnitPrice);
			isPricingUOMAdded=true;
			isThAndCwtAdded=true;
		}
		
		if ((XPEDXPriceandAvailabilityUtil.CWT_UOM_M.equalsIgnoreCase(pricingUOM) || XPEDXPriceandAvailabilityUtil.CWT_UOM_A.equalsIgnoreCase(pricingUOM))
				&& (uomsList.contains(XPEDXPriceandAvailabilityUtil.TH_UOM_M) || uomsList.contains(XPEDXPriceandAvailabilityUtil.TH_UOM_A))) {
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
				isThAndCwtAdded=true;
			}
			catch(Exception e)
			{	
				priceForTHUom = pricingUOMPrice;
			}
		}
		if ((XPEDXPriceandAvailabilityUtil.TH_UOM_M.equalsIgnoreCase(pricingUOM) || XPEDXPriceandAvailabilityUtil.TH_UOM_A.equalsIgnoreCase(pricingUOM)) && 
				(uomsList.contains(XPEDXPriceandAvailabilityUtil.CWT_UOM_M) || uomsList.contains(XPEDXPriceandAvailabilityUtil.CWT_UOM_A))) {
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
				if(prodMweight != null){
					displayUOMs.add(cwtUOMDesc);
					isThAndCwtAdded=true;
				}
			}
			
			catch(Exception e) 
			{				
				priceForCWTUom=pricingUOMPrice;						
			}
			
		}
		if(!isPricingUOMAdded )
		{
			displayUOMs.add(PricingUOMDesc);
		}
		boolean isDisplayReqUOM=true;
		for(int i=0;i<XPEDXConstants.DO_NOT_DISPLAY_REQUESTED_UOMS.length;i++)
		{
			if(XPEDXConstants.DO_NOT_DISPLAY_REQUESTED_UOMS[i].equals(pandAItem.getRequestedQtyUOM()))
			{
				isDisplayReqUOM=false;
				break;
			}
		}
		if(isThAndCwtAdded &&( RequestedQtyUOMDesc.toLowerCase().contains("thousand") || 
				RequestedQtyUOMDesc.toLowerCase().contains("cwt")))
		{
			isDisplayReqUOM=false;
		}
		if(pricingUOM!=null && !pricingUOM.equals(pandAItem.getRequestedQtyUOM()) 
				&& isDisplayReqUOM)
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
		//Moved code from bottom to above for XB-558

		if (priceForTHUom != null &&
				(uomsList.contains(XPEDXPriceandAvailabilityUtil.TH_UOM_M ) || uomsList.contains(XPEDXPriceandAvailabilityUtil.TH_UOM_A))){
			displayPriceForUoms.add(priceForTHUom.toString());
		}
		if (priceForCWTUom != null && prodMweight != null &&
				(uomsList.contains(XPEDXPriceandAvailabilityUtil.CWT_UOM_M) || uomsList.contains(XPEDXPriceandAvailabilityUtil.CWT_UOM_A))){
			displayPriceForUoms.add(priceForCWTUom.toString());
		}
		if(!isPricingUOMAdded )
		{
			displayPriceForUoms.add(pricingUOMUnitPrice);
		}
		if(pricingUOM!=null && !pricingUOM.equals(pandAItem.getRequestedQtyUOM())
				&& isDisplayReqUOM)
			displayPriceForUoms.add(pandAItem.getUnitPricePerRequestedUOM());
		displayPriceForUoms.add(pandAItem.getExtendedPrice());

		bracketsPricingList = pandAItem.getBrackets();
		setIsBracketPricing(XPEDXPriceandAvailabilityUtil.isBracketPricingAvailable(bracketsPricingList));
		
		itemCost = pandAItem.getItemCost();
		itemCostCurrency = pandAItem.getCostCurrencyCode();

	}

	private  List<String> createItemUOMMap(String itemID)
	{
		
		List<String> uoms=new ArrayList<String>();
		ArrayList<String> itemIDList=new ArrayList<String>();
		itemIDList.add(itemID);
		try
		{
			Document pricingInfoDoc = XPEDXOrderUtils.getItemDetailsForPricingInfo(itemIDList,wcContext
					.getCustomerId(), wcContext.getStorefrontId(), wcContext);
			if(pricingInfoDoc !=null)
			{
				NodeList items=pricingInfoDoc.getDocumentElement().getElementsByTagName("Item");
				for(int i=0;i<items.getLength();i++)
				{
					
					Element itemElem=(Element)items.item(i);
					uoms.add(itemElem.getAttribute("UnitOfMeasure"));
					ArrayList<Element> itemUomList=SCXmlUtil.getElements(itemElem, "AlternateUOMList/AlternateUOM");
					if(itemUomList != null)
					{
						for(Element alternateUOM :itemUomList)
						{
							uoms.add(alternateUOM.getAttribute("UnitOfMeasure"));
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			LOG.error("Error while getting aleternate UOM for validation!");
		}
		return uoms;
	}
	
	private void getItemDetails() throws Exception {
		Element itemEle = XMLUtilities.getElement(m_itemListElem, "Item");
		ArrayList<Element> catPath = SCXmlUtil.getElements(itemEle, "CategoryList/Category");
		String categoryPath = catPath.get(0).getAttribute("CategoryPath");
		Map<String,String> mainCats = XPEDXWCUtils.getMainCategories();
		String pathElements[] = categoryPath.split("/");
		String currentCategoryName="";
		for(int i = 0; i <pathElements.length ; i++) {			
			if(mainCats !=null && mainCats.size() > 0 )
				if( mainCats.containsKey(pathElements[i]) ){ 
					currentCategoryName = mainCats.get(pathElements[i]);
					XPEDXConstants.logMessage("currentCategoryName : " + currentCategoryName );
					currentCategoryName = currentCategoryName.replaceAll(" ", "");
					continue;
				}
			
		}
		Element primaryInfoEle = XMLUtilities.getElement(itemEle,"PrimaryInformation");
		Element itemExtnEle = XMLUtilities.getElement(itemEle, "Extn");
		catagory = currentCategoryName;
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
		//get the map from the session. For Minicart Jira - 3481
		//HashMap<String,String> OrderMultipleMapFromSessionMinicart = getOrderMultipleMapFromSessionMiniCart();
		HashMap<String,String> itemMapObj = (HashMap<String, String>) XPEDXWCUtils.getObjectFromCache("itemMap");
		
		if(itemExtnElement == null) {
			Document itemExtnDoc = XPEDXOrderUtils.getXPEDXItemAssociation(wcContext.getCustomerId(), shipFromDivision, itemID, getWCContext());
			if(itemExtnDoc!=null)
				itemExtnElement = SCXmlUtil.getChildElement(itemExtnDoc.getDocumentElement(),"XPXItemExtn");
		}
		if(itemExtnElement == null) {
			String orderMul = "1";
			setIsStocked("N");
			//Added for webtrends 
			itemType="Mfg";
			
			if(SCUtil.isVoid(orderMul))
				orderMul="1";
			setOrderMultiple(orderMul);
			itemOrderMultipleMap.put(itemID, orderMul);
			if(itemMapObj !=null )
			{
				itemMapObj.put(itemID, orderMul);
			}
		}
		else {
			String div = itemExtnElement.getAttribute("XPXDivision");
			if (shipFromDivision.equalsIgnoreCase(div)) {
				String orderMul = itemExtnElement.getAttribute("OrderMultiple");
				setIsStocked(itemExtnElement.getAttribute("InventoryIndicator"));
				
				/*start of webtrends */
				String inventoryIndicator = itemExtnElement.getAttribute("InventoryIndicator");				
				if (inventoryIndicator.equalsIgnoreCase("W")){
					itemType = "Stocked";
				}
				else if (inventoryIndicator.equalsIgnoreCase("I")){
					itemType="InDirect";
				}else if (inventoryIndicator.equalsIgnoreCase("") || inventoryIndicator.equalsIgnoreCase("M")){
					itemType="Mill";
				}
				/*End of webtrends */
				if(SCUtil.isVoid(orderMul))
					orderMul="1";
				setOrderMultiple(orderMul);
				itemOrderMultipleMap.put(itemID, orderMul);
				if(itemMapObj !=null )
				{
					itemMapObj.put(itemID, orderMul);
				}
			}
		}
		//Set itemMap MAP again in session
		XPEDXWCUtils.setObectInCache("itemMap",itemMapObj);
		//set a itemsUOMMap in Session for ConvFactor
		//XPEDXWCUtils.setObectInCache("itemsUOMMap",XPEDXOrderUtils.getXpedxUOMList(wcContext.getCustomerId(), itemID, wcContext.getStorefrontId()));
		//Added for webtrends -XBT-35
		XPEDXWCUtils.setObectInCache("itemType",itemType);
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
	//Commented for performance issue - Itemdetail.action			 
	/*	String custDetailsMashupId = "xpedx-itemdetails-customerInfo";
		outputDoc = XPEDXWCUtils.getCustomerDetails(getWCContext().getCustomerId(), getWCContext().getStorefrontId(),custDetailsMashupId); // trim of the template is Required
		Element outputEl = outputDoc.getDocumentElement();
		*/
		// For performance issue - Itemdetail.action
		XPEDXShipToCustomer shipToCustomer =(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache("shipToCustomer");
		shipFromDivision = shipToCustomer.getExtnShipFromBranch();
		//Fetching and setting the other customer profile settings here to use in getCustomerPartNumber
		envCode = shipToCustomer.getExtnEnvironmentCode();
		//Commented for EB 47 customerCode = shipToCustomer.getExtnCompanyCode();
		customerLegNo = shipToCustomer.getExtnLegacyCustNumber();
		useOrderMulUOMFlag = shipToCustomer.getExtnUseOrderMulUOMFlag();
		customerBranch = shipFromDivision;
		//Getting the customer Sku flag from MSAP Customer
		custSKU = (String)wcContext.getWCAttribute(XPEDXConstants.CUSTOMER_USE_SKU,WCAttributeScope.LOCAL_SESSION);
		DivisionNumber = shipToCustomer.getExtnCustomerDivision();
		SalesRepresentative1 = shipToCustomer.getExtnPrimarySalesRep();
		setSampleRequestFlagInSession();// This can be edited
		Email2 = shipToCustomer.getEMailID();
		Email1 = shipToCustomer.getExtnSampleRoomEmailAddress();
		CustomerName = shipToCustomer.getFirstName()
				+ ""
				+ shipToCustomer.getMiddleName()
				+ ""
				+ shipToCustomer.getLastName();
		//CustomerContactName = SCXmlUtil.getAttribute(
			//	customerOrganizationExtnEle1, "AddressID");
		ContactPhoneNumber = shipToCustomer.getDayPhone();
		List arrAddress = shipToCustomer.getAddressList();
		if(arrAddress!=null ) {
			if(arrAddress.size()>0 && arrAddress.get(0)!=null)ShippingAddress1 = (String) arrAddress.get(0);
			if(arrAddress.size()>1 && arrAddress.get(1)!=null)ShippingAddress2 = (String) arrAddress.get(1);
			if(arrAddress.size()>2 && arrAddress.get(2)!=null)ShippingAddress3 = (String) arrAddress.get(2);
		}		
		CityRequest = shipToCustomer.getCity();
		StateRequest = shipToCustomer.getState();
		ZipRequest = shipToCustomer.getZipCode();
		AccountNumber = shipToCustomer.getAccountNumber();
		// End of performance issue - itemdetail.action
		//Added for EB 47
		extnMfgItemFlag = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.BILL_TO_CUST_MFG_ITEM_FLAG);
		extnCustomerItemFlag = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.BILL_TO_CUST_PART_ITEM_FLAG);
		//End of EB 47
		
		/*Element customerOrganizationExtnEle = XMLUtilities.getElement(outputEl,"Extn");
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
		/*custSKU = (String)wcContext.getWCAttribute(XPEDXConstants.CUSTOMER_USE_SKU,WCAttributeScope.LOCAL_SESSION);
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
		//SCXmlUtil.getString(CustomerList);
		for (int customerNo1 = 0; customerNo1 < CustomerList.getLength(); customerNo1++) {
			customerOrganizationExtnEle1 = (Element) CustomerList.item(0);
		}
		AccountNumber = SCXmlUtil.getAttribute(customerOrganizationExtnEle1,
				"CustomerAccountNo");
		*/
	}

	protected void getItemUOMs() throws Exception {
		String customerId = wcContext.getCustomerId();
		String organizationCode = wcContext.getStorefrontId();

		/*itemUOMsMap = XPEDXOrderUtils.getXpedxUOMList(customerId, itemID,
			organizationCode);
		/*LinkedHashMap<String, String> wUOMsToConversionFactors = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> uomListMap = new LinkedHashMap<String, String>();
		if(getItemListElem() != null) {
			Element itemElement = SCXmlUtil.getChildElement(m_itemListElem,"Item");
			Element alternateUomList = SCXmlUtil.getChildElement(itemElement,"AlternateUOMList");
			List<Element> listConv = SCXmlUtil.getChildrenList(alternateUomList);
			if (listConv != null) {
				//2964 start
			Collections.sort(listConv,new Comparator<Element>() {
				public int compare(Element elem, Element elem1) {		
					double fromQuantity =0;
					double fromQuantity1 = 0;SCXmlUtil.getString(elem);
					if(!YFCUtils.isVoid(elem.getAttribute("Quantity"))){
						 
						fromQuantity = Double.valueOf(elem.getAttribute("Quantity"));	
					}
					
					if(!YFCUtils.isVoid(elem1.getAttribute("Quantity"))){
						 
						fromQuantity1 = Double.valueOf(elem1.getAttribute("Quantity"));
						
					}
					if(fromQuantity >fromQuantity1){
						return 1;
					}
					else if(fromQuantity < fromQuantity1){
						return -1;
					}
					else 
						return 0;
					}
			});
			
			NodeList alternateUoms = alternateUomList.getChildNodes();
			//int alternateUomLenght = alternateUoms.getLength();
			for(Element AlternateUOM :listConv ){
			//for (int i = 0; i < alternateUomLenght; i++) {
				//Node AlternateUOM = alternateUoms.item(i);
				NamedNodeMap namedNodeMap = AlternateUOM.getAttributes();
				if(namedNodeMap!=null){
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
				}}
			}
		}}
		// sending the entry type as null as this is not a B2B request
		//Start - Code added to fix XNGTP 2964
		String msapOrderMultipleFlag = "";
		XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
		if(xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag()!=null && xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag()!=""){
			msapOrderMultipleFlag = xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag();	
			}
		//End - Code added to fix XNGTP 2964
		handleXpxItemcustXrefList(itemID,msapOrderMultipleFlag, orderMultiple,wUOMsToConversionFactors,null);
		if (baseUOM != null
				&& baseUOM.trim().length() > 0
				&& (ExtnIsCustUOMExcl == null
						|| ExtnIsCustUOMExcl.trim().length() == 0 || !ExtnIsCustUOMExcl
						.equals("Y")))
		{
			uomListMap.put(baseUOM, "1");
		}
		uomListMap.putAll(wUOMsToConversionFactors);
		
		
		itemUOMsMap = uomListMap;*/
		//2964 start
		//displayItemUOMsMap = new HashMap();
		//displayItemUOMsMap = itemUOMsMap;
		//2964 end
		
		
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
		//Changes start for JIRA 2964
	/*
		double minFractUOM = 0.00;
    	double maxFractUOM = 0.00;
    	String lowestUOM = "";
    	String highestUOM = "";
    	String minUOMsDesc = "";
    	String maxUOMsDesc = "";
    	String defaultConvUOM = "";
		String defaultUOM = "";
		String defaultUOMCode = "";
		//String orderMultiple = "";
		defaultShowUOMMap = new HashMap<String,String>();
		//Start - Code added to fix XNGTP 2964
		String msapOrderMultipleFlag = "";
		XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
		if(xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag()!=null && xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag()!=""){
			msapOrderMultipleFlag = xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag();	
		}
		//End - Code added to fix XNGTP 2964
    	
		if(itemUOMsMap!=null && itemUOMsMap.keySet()!=null) {
			
			Iterator<String> iterator = itemUOMsMap.keySet().iterator();
			while(iterator.hasNext()) {
					String uomCode = iterator.next();
					String convFactor = (String) itemUOMsMap.get(uomCode);
					//Start 2964 
					itemIdConVUOMMap.put(uomCode, convFactor);
					//End 2964 
					if("Y".equals(msapOrderMultipleFlag) && Integer.valueOf(orderMultiple) > 1 && !"1".equals(convFactor))
						{
							
						
							if(convFactor.toString() == orderMultiple){
								minFractUOM = 1;
								lowestUOM = uomCode;
								minUOMsDesc =  XPEDXWCUtils.getUOMDescription(lowestUOM)+ " (" + Math.round(Double.parseDouble((String)convFactor)) + ")";
								
								
							}
							else {
								double conversion = getConversion(convFactor, orderMultiple);
								if (conversion != -1 && uomCode != null
										&& uomCode.length() > 0) {
									if(conversion <= 1 && conversion >= minFractUOM){
										minFractUOM = conversion;
										lowestUOM = uomCode;
										minUOMsDesc =  XPEDXWCUtils.getUOMDescription(lowestUOM)+ " (" + Math.round(Double.parseDouble((String)convFactor)) + ")";
										
										
									}else if(conversion>1 && ( conversion < maxFractUOM || maxFractUOM == 0)){
										maxFractUOM = conversion;
										highestUOM = uomCode;
										maxUOMsDesc =  XPEDXWCUtils.getUOMDescription(highestUOM)+ " (" + Math.round(Double.parseDouble((String)convFactor)) + ")";
										
									
									}
								}
							}
						}
					
					long convFac = Math.round(Double.parseDouble(convFactor));
					if(1 == convFac) {
						displayItemUOMsMap.put(uomCode, XPEDXWCUtils.getUOMDescription(uomCode));
					}
					else {
						//--FXD-- Adding space between UOM Description & Conversion factor
						displayItemUOMsMap.put(uomCode, XPEDXWCUtils.getUOMDescription(uomCode)+" ("+convFac+")" );
					}
			}
			if(minFractUOM == 1.0 && minFractUOM != 0.0){
				defaultConvUOM = lowestUOM;
				defaultUOM = minUOMsDesc;
				defaultUOMCode = lowestUOM;
				
			}else if(maxFractUOM > 1.0){
				defaultConvUOM = highestUOM;
				defaultUOM = maxUOMsDesc;
				lowestUOM = highestUOM;
			}else{
				
				defaultConvUOM = lowestUOM;
				defaultUOM = minUOMsDesc;
				defaultUOMCode = lowestUOM;
				
			}
			
		}
		defaultShowUOMMap.put(defaultUOMCode, defaultUOM);*/
		//Changes End for JIRA 2964
		
		//Start of EB-164
		defaultShowUOMMap = new HashMap<String,String>();		
		displayItemUOMsMap = XPEDXOrderUtils.getXpedxUOMDescList(customerId, itemID, organizationCode);
		//defaultShowUOMMap = XPEDXOrderUtils.getDefaultShowUOMMap();	
		defaultShowUOMMap = (Map<String, String>)ServletActionContext.getRequest().getAttribute("defaultShowUOMMap");
		ServletActionContext.getRequest().removeAttribute("defaultShowUOMMap");
		if(defaultShowUOMMap == null)
			defaultShowUOMMap =  new HashMap<String, String>();
		//itemIdConVUOMMap = XPEDXOrderUtils.getUomsAndConFactors();	
		itemIdConVUOMMap = (LinkedHashMap<String, String>)ServletActionContext.getRequest().getAttribute("uomsAndConFactors");
		ServletActionContext.getRequest().removeAttribute("uomsAndConFactors");
		if(itemIdConVUOMMap == null)
			itemIdConVUOMMap =  new LinkedHashMap<String, String>();
		//End of EB-164
		//EB-225 - Getting the customer UOM if exist for a item
		customerUOM = XPEDXOrderUtils.getStrCustomerUOM();
		
	}
	
	private void handleXpxItemcustXrefList(String itemID,String useOrderMulUOMFlag, String orderMultiple,
			HashMap<String, String> wUOMsToConversionFactors, String entryType)
			throws XPathExpressionException, YFSException, RemoteException, YIFClientCreationException {
		
		Node customerUnitNode = null;
		/*Begin - Changes made by Mitesh Parikh for JIRA# 3641*/
		//ArrayList<Element> XpxItemcustXrefList = SCXmlUtil.getElements(itemCustXrefList, "XPXItemcustXref[@LegacyItemNumber="+itemID+"]");
		List<Element> XpxItemcustXrefList = XMLUtilities.getElements(itemCustXrefList, "XPXItemcustXref[@LegacyItemNumber="+itemID+"]");
		/*End - Changes made by Mitesh Parikh for JIRA# 3641*/
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
			//XBT-121 -Added not null condition
			if(customerUnit != null && customerUnit.trim().length() >0)
			{
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
			//Commented for EB 47 valueMap.put("/XPXItemcustXref/@CompanyCode",customerCode);
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
		*/		
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
        //XB-673 - Changes Start
        alternateSBCAssociatedItems = new ArrayList();
        //XB-673 - Changes End
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
		//XB-673- Changes Start
		ArrayList<String> alternateSBCItemIDs = new ArrayList<String>();
		//XB-673 Changes End
		try {
			if(getItemListElem()== null){
				//System.out.println(SCXmlUtil.getString(getItemListElem()));
				relatedItemListElem = prepareAndInvokeMashup("xpedxRelatedItemDetails");
			}else
				relatedItemListElem = getItemListElem();
			itemAssociationDoc = relatedItemListElem.getOwnerDocument();
			//System.out.println(SCXmlUtil.getString(itemAssociationDoc));
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
			//XB-673 - Changes Start
			List<Element> alternateSBCElements = XMLUtilities.getElements(associationTypeListElem,"AssociationType[@Type='Alternative.Y']");
			//XB-673 - Changes End
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
			//For ALternative items
			//XB-673 - Changes Start
			for (int k = 0; k < alternateSBCElements.size(); k++) {
				Element associationTypeElem = alternateSBCElements.get(k);
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
						if(!alternateSBCItemIDs.contains(curritemid)){
							alternateSBCItemIDs.add(curritemid);
						}
					}
				}
			}//for Alternative
			//XB-673 - Changes End
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
						//System.out.println("ItemListDoc "+SCXmlUtil.getString(itemDetailsListDoc));
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
								if(crossSellItemIDs.contains(curritemID)||compItemIds.contains(curritemID)){
									crossSellAssociatedItems.add(curritem);
								}
								continue;
							}
							if(compItemIds.contains(curritemID)){
								complimentAssociatedItems.add(curritem);
								if(upSellItemIDs.contains(curritemID) || altItemIds.contains(curritemID) || upItemIds.contains(curritemID) || alternateSBCItemIDs.contains(curritemID)){
									upSellAssociatedItems.add(curritem);
								}
								continue;
							}
							if(upItemIds.contains(curritemID)){
								upgradeAssociatedItems.add(curritem);
								if(crossSellItemIDs.contains(curritemID)||compItemIds.contains(curritemID)){
									crossSellAssociatedItems.add(curritem);
								}
								continue;
							}
							if(crossSellItemIDs.contains(curritemID)){
								crossSellAssociatedItems.add(curritem);
								if(upSellItemIDs.contains(curritemID) || altItemIds.contains(curritemID) || upItemIds.contains(curritemID) || alternateSBCItemIDs.contains(curritemID)){
									upSellAssociatedItems.add(curritem);
								}
								continue;
							}
							if(upSellItemIDs.contains(curritemID)){
								upSellAssociatedItems.add(curritem);
								if(crossSellItemIDs.contains(curritemID)||compItemIds.contains(curritemID)){
									crossSellAssociatedItems.add(curritem);
								}	
								continue;
							}
							//XB-673 - Changes Start - 
							/**
							 * As it need to display under you might also consider section, we need to add under same upSellAssociated 
							 * items list
							 */
							if(alternateSBCItemIDs.contains(curritemID)){
								upSellAssociatedItems.add(curritem);
								if(crossSellItemIDs.contains(curritemID)||compItemIds.contains(curritemID)){
									crossSellAssociatedItems.add(curritem);
								}
								continue;
							}
							//XB-673 - Changes End -
							
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
			//added for jira 2971
			if(shipToCustomer.getBillTo() != null)
					XPEDXWCUtils.setSampleRequestFlag(shipToCustomer.getBillTo().getExtnSampleRequestFlag(),shipToCustomer.getBillTo().getExtnServiceOptCode(),wcContext);
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
	
	LinkedHashMap<String, String> itemIdConVUOMMap = new LinkedHashMap<String, String>();
	
	public LinkedHashMap<String, String> getItemIdConVUOMMap() {
		return itemIdConVUOMMap;
	}

	public void setItemIdConVUOMMap(LinkedHashMap<String, String> itemIdConVUOMMap) {
		this.itemIdConVUOMMap = itemIdConVUOMMap;
	}

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
	
	protected HashMap orderMultipleMapFromSourcing;

	public HashMap getOrderMultipleMapFromSourcing() {
		return orderMultipleMapFromSourcing;
	}

	public void setOrderMultipleMapFromSourcing(HashMap orderMultipleMapFromSourcing) {
		this.orderMultipleMapFromSourcing = orderMultipleMapFromSourcing;
	}
	
	protected HashMap useOrderMultipleMapFromSourcing;
	public HashMap getUseOrderMultipleMapFromSourcing() {
		return useOrderMultipleMapFromSourcing;
	}

	public void setUseOrderMultipleMapFromSourcing(
			HashMap useOrderMultipleMapFromSourcing) {
		this.useOrderMultipleMapFromSourcing = useOrderMultipleMapFromSourcing;
	}

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
	//XB-673 - Changes Start
	ArrayList alternateSBCAssociatedItems = null;
	//XB-673 - Changes End
	List displayUOMs = new ArrayList();
	List displayPriceForUoms = new ArrayList();
	List bracketsPricingList = null;
	boolean showSampleRequest = false;
	protected Map<String, String> itemUOMsMap;
	
	protected Map <String, String> displayItemUOMsMap;
	protected Map itemOrderMultipleMap;
	String customerBranch = null;
	String customerLegNo = null;
	String customerSuffix = null;
	String customerCode = null;
	String envCode = null;
	String custPartNumber = null;
	String requestedUOM = null;
	String requestedDefaultUOM = null;
	//added for EB 47
	private String extnMfgItemFlag;
	private String extnCustomerItemFlag;
	public String getExtnMfgItemFlag() {
		return extnMfgItemFlag;
	}

	public void setExtnMfgItemFlag(String extnMfgItemFlag) {
		this.extnMfgItemFlag = extnMfgItemFlag;
	}

	public String getExtnCustomerItemFlag() {
		return extnCustomerItemFlag;
	}

	public void setExtnCustomerItemFlag(String extnCustomerItemFlag) {
		this.extnCustomerItemFlag = extnCustomerItemFlag;
	}
	//End of EB 47


	//added for jira 2422
	private String goBackFlag;

	public String getGoBackFlag() {
		return goBackFlag;
	}

	public void setGoBackFlag(String goBackFlag) {
		this.goBackFlag = goBackFlag;
	}

	public String getRequestedDefaultUOM() {
		return requestedDefaultUOM;
	}

	public void setRequestedDefaultUOM(String requestedDefaultUOM) {
		this.requestedDefaultUOM = requestedDefaultUOM;
	}

	public String Description;
	

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

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
	private Map <String,String>defaultShowUOMMap;
	
	public Map<String, String> getDefaultShowUOMMap() {
		return defaultShowUOMMap;
	}

	public void setDefaultShowUOMMap(Map<String, String> defaultShowUOMMap) {
		this.defaultShowUOMMap = defaultShowUOMMap;
	}

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
	//Added for Jira 4272 - Start
	String promoheight=null;
	 
	public String getPromoheight() {
		promoheight="0";
		int intpromoheight=0; 
		int finalpromoHeight=0;
		boolean uMightFlag=false;
		boolean populareAccFlag=false;
		/*upgradeAssociatedItems.add("abv");
		upSellAssociatedItems.add("bcd");
		alternateAssociatedItems.add("ghi");
		crossSellAssociatedItems.add("kkk");
		complimentAssociatedItems.add("zzz");*/
		//XB-673 Changes Start
		if((upSellAssociatedItems != null && upSellAssociatedItems.size() > 0) || (upgradeAssociatedItems != null && upgradeAssociatedItems.size() > 0) 
				|| (alternateAssociatedItems != null && alternateAssociatedItems.size() > 0) || (alternateSBCAssociatedItems != null && alternateSBCAssociatedItems.size() > 0)){
			//XB-673 Changes End
			uMightFlag=true;
			if(upgradeAssociatedItems.size() > 0){
				intpromoheight = Integer.valueOf(promoheight);
				intpromoheight = intpromoheight+upgradeAssociatedItems.size();
				promoheight=String.valueOf(intpromoheight);
			}
			if(upSellAssociatedItems.size() > 0){
				intpromoheight = Integer.valueOf(promoheight);
				intpromoheight = intpromoheight+upSellAssociatedItems.size();
				promoheight=String.valueOf(intpromoheight);
			}
			if(alternateAssociatedItems.size() > 0){
				intpromoheight = Integer.valueOf(promoheight);
				intpromoheight = intpromoheight+alternateAssociatedItems.size();
				promoheight=String.valueOf(intpromoheight);
			}
			//XB-673 Changes Start
			if(alternateSBCAssociatedItems.size() > 0){
				intpromoheight = Integer.valueOf(promoheight);
				intpromoheight = intpromoheight+alternateSBCAssociatedItems.size();
				promoheight=String.valueOf(intpromoheight);
			}
			//XB-673 Changes End
		}
		if((crossSellAssociatedItems != null && crossSellAssociatedItems.size() > 0) || (complimentAssociatedItems != null && complimentAssociatedItems.size() > 0)){
			populareAccFlag=true;
			if(complimentAssociatedItems.size() > 0){
				intpromoheight = Integer.valueOf(promoheight);
				intpromoheight = intpromoheight+complimentAssociatedItems.size();
				promoheight=String.valueOf(intpromoheight);
			}
			if(crossSellAssociatedItems.size() > 0){
				intpromoheight = Integer.valueOf(promoheight);
				intpromoheight = intpromoheight+crossSellAssociatedItems.size();
				promoheight=String.valueOf(intpromoheight);
			}
		} 
		if(intpromoheight>0){
			finalpromoHeight = intpromoheight * 230;		
			if(uMightFlag){
				finalpromoHeight = finalpromoHeight+20;
			}
			if(populareAccFlag){
				finalpromoHeight = finalpromoHeight+20;
			}
			if(finalpromoHeight > 460)
				promoheight = String.valueOf(finalpromoHeight)+"px";
			else
				promoheight = "auto";
		}
		else
			promoheight=null;
		
		return promoheight;
	}
//end of Jira 4272
	public void setPromoheight(String promoheight) {
		this.promoheight = promoheight;
	}

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
	protected Map assetLinkMap = new HashMap<String, String>();
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
	private String isOrderData ="false";
	
	//EB-225 - CustomerUOM if exist for a item in Item detail page, to set as hidden field in Item detail page
	private String customerUOM ="";
	//EB-225 - if requestedUOM is same as customer UOM for pna of a item in Item detail page, this field will have the value, else it will be empty
	private String pnaRequestedCustomerUOM = "";
	
	public String getPnaRequestedCustomerUOM() {
		return pnaRequestedCustomerUOM;
	}

	public void setPnaRequestedCustomerUOM(String pnaRequestedCustomerUOM) {
		this.pnaRequestedCustomerUOM = pnaRequestedCustomerUOM;
	}

	public String getCustomerUOM() {
		return customerUOM;
	}

	public void setCustomerUOM(String customerUOM) {
		this.customerUOM = customerUOM;
	}

	public String getIsOrderData() {
		return isOrderData;
	}

	public void setIsOrderData(String isOrderData) {
		this.isOrderData = isOrderData;
	}

	private static final Logger LOG = Logger
			.getLogger(XPEDXItemDetailsAction.class);
	
	public String Category;

	public String catagory;
	
	//Added for webtrends- Start
	private String itemType="";
	

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	//Added for webtrends -End
	public String getCatagory() {
		return catagory;
	}

	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}
	
	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public String validateOrderMul;
	
	
	public String getValidateOrderMul() {
		return validateOrderMul;
	}

	public void setValidateOrderMul(String validateOrderMul) {
		this.validateOrderMul = validateOrderMul;
	}

	//added for jira 2885
	private  Map<String,String> pnALineErrorMessage=new HashMap<String,String>(); 
	
	public Map<String, String> getPnALineErrorMessage() {
		return pnALineErrorMessage;
	}

	public void setPnALineErrorMessage(Map<String, String> pnALineErrorMessage) {
		this.pnALineErrorMessage = pnALineErrorMessage;
	}

	public Map getAssetLinkMap() {
		return assetLinkMap;
	}

	public void setAssetLinkMap(Map assetLinkMap) {
		this.assetLinkMap = assetLinkMap;
	}

	
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

package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.catalog.ProductComparisonAction;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;

public class XPEDXProductComparisonAction extends ProductComparisonAction {
	String myString = null;
	// YFCDocument inputItemAttrDoc;
	YFCDocument inputItemAttrDoc;
	String sinputItmAttrDoc = "";
	
	private Map<String, Map<String, String>> msdsItemLinkMap = new HashMap<String, Map<String, String>>();
	/*String[] sAttrDesc;

	String[] sArrimmediate;
	String[] sArrNextDay;
	String[] sArrTwoPlusDays;
	String[] sArrAvailability;*/
	

	public Map<String, Map<String, String>> getMsdsItemLinkMap() {	
		return msdsItemLinkMap;
	}




	public void setMsdsItemLinkMap(Map<String, Map<String, String>> msdsItemLinkMap) {
		this.msdsItemLinkMap = msdsItemLinkMap;
	}




	public String execute() {
		String returnVal = null;
		try {			
			returnVal = super.execute();
			
			/* Begin - Changes made by Mitesh Parikh for 2422 JIRA */
			setItemDtlBackPageURL((wcContext.getSCUIContext().getRequest().getRequestURL().append("?").append(wcContext.getSCUIContext().getRequest().getQueryString())).toString());
			/* End - Changes made by Mitesh Parikh for 2422 JIRA */
			//PnA call removed as per Pawan's mail dated 9/4/2011
			getProductComparisonOutputDetails();			
			/**** Code for adding of additional attributes ********/
			String sProdComparisonDoc = SCXmlUtil
					.getString(prodComparisonOutputDoc);			
			String sItemAttributeE = "</ItemAttribute>";
			int nodeLength = sItemAttributeE.length();
			int lastIndex = sProdComparisonDoc.lastIndexOf(sItemAttributeE);
			if (lastIndex <= 0) {
				log.debug("No attribute to compare between "
						+ "the selected compare set");
			} else {
				int length = lastIndex + nodeLength;
				String temporaryString = sProdComparisonDoc
						.substring(0, length);
				temporaryString = temporaryString + sinputItmAttrDoc;
				temporaryString = temporaryString.replace(
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
				sProdComparisonDoc = temporaryString
						+ sProdComparisonDoc.substring(length,
								sProdComparisonDoc.length());
				super.prodComparisonOutputDoc = SCXmlUtil
						.createFromString(sProdComparisonDoc.replace("\\n", ""));
				checkEmptyAttributeElements();
			}
			getAllItemList();
		} catch (Exception e) {
			log.debug(e);
		}

		return returnVal;
	}

	
	/*EB-694 xpedx catalog URL display for FSC, PEFC and SFI*/	
	
	private void setMSDSUrls(Element outputItem) {
		String msdsLink="";
		String msdsLinkDesc="";
		String assetLinkDesc="";	
		HashMap<String, String> assetLinkMap = new HashMap<String, String>();
		Map<String, String> msdsLinkMap = new HashMap<String, String>();
		if(outputItem!=null) {
			NodeList outputItemList = outputItem.getElementsByTagName("Item");
			for (int i = 0; i < outputItemList.getLength(); i++) {				
				Element eleItmElement = (Element) outputItemList.item(i);				
				String itemId = SCXmlUtil.getAttribute(eleItmElement, "ItemID");
				NodeList list = SCXmlUtil.getXpathNodes(eleItmElement, "AssetList/Asset");
				ArrayList<Element> assetList = new ArrayList<Element>();
				XPEDXSCXmlUtils xpedxSCXmlUtils = new XPEDXSCXmlUtils();
				Element reqNode;
				for (int j = 0; j < list.getLength(); j++) {
					reqNode = (Element) list.item(j);
					assetList.add(reqNode);
				}
				boolean isMSDSLink=false;
				String msdsAssetId = null;
				String assetLink = null;
				if(assetList!=null && assetList.size()>0) {					
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
						msdsAssetId = xpedxSCXmlUtils.getAttribute(AssetElem, "AssetID");
						if(!SCUtil.isVoid(msdsLocation) && msdsLocation.endsWith("/")){
							assetLink = msdsLocation+msdsContentId;
							assetLinkDesc = xpedxSCXmlUtils.getAttribute(AssetElem, "Description");
						}else{
							assetLink = msdsLocation+"/"+msdsContentId;
							assetLinkDesc = xpedxSCXmlUtils.getAttribute(AssetElem, "Description");
						}						
						//Handling "/" if exist in msdsLocation, as an extra "/" was coming

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
						}if( msdsLinkMap.isEmpty())
						{
							msdsLinkMap = new HashMap<String, String>();						
						}
						if(isMSDSLink)
							msdsLinkMap.put(msdsLinkDesc, msdsLink);

						assetLinkMap.put(msdsAssetId, assetLink);

					}

				}
				
				msdsItemLinkMap.put(itemId, assetLinkMap);				
			}

		}				
	}
		
	

	private void checkEmptyAttributeElements() {
		Document docProdCmpOputDocDetails = prodComparisonOutputDoc;
		String sAssignedValue = "";
		NodeList nlItemAttributeList = docProdCmpOputDocDetails
				.getElementsByTagName("ItemAttribute");
		for (int i = 0; i < nlItemAttributeList.getLength(); i++) {
			Element eleItmAttribElement = (Element) nlItemAttributeList.item(i);
			NodeList nlItmList = eleItmAttribElement
					.getElementsByTagName("Item");
			for (int j = 0; j < nlItmList.getLength(); j++) {
				Element eleItmElement = (Element) nlItmList.item(j);
				if (sAssignedValue == "" || sAssignedValue == null) {
					sAssignedValue = SCXmlUtil.getXpathAttribute(eleItmElement,
							"./AssignedValueList/AssignedValue/@Value");
				}
			}
			if (sAssignedValue == null || sAssignedValue.trim().length() == 0) {
				eleItmAttribElement.getParentNode().removeChild(
						eleItmAttribElement);
			}
			sAssignedValue = "";
		}
	}
	
	//PnA call removed as per Pawan's mail dated 9/4/2011
	private void getProductComparisonOutputDetails()
			throws InstantiationException, IllegalAccessException {
		String sProdComparisonDoc = getXMLUtils().getString(
				prodComparisonOutputDoc);
		Document docProdCmpOputDocDetails = prodComparisonOutputDoc;
		Element eleOrder = docProdCmpOputDocDetails.getDocumentElement();
		int iTotItmList = Integer.parseInt(SCXmlUtil.getAttribute(eleOrder,
				"TotalItemList"));		
		NodeList nlItemDetails = docProdCmpOputDocDetails
				.getElementsByTagName("Item");
		Element eleItemDetails;
		// YFCDocument inputItemAttrDoc;
		YFCElement inputItmAttrElement;
		YFCElement inputAttributeElement;
		YFCElement inputItmListElement;
		YFCElement inputItmElement;
		YFCElement inputAssignedValueListElement;
		YFCElement inputAssignedValueElement;
		String[] sItmIds = new String[iTotItmList];
		String[] sUoms = new String[iTotItmList];
		String[] sArrListPrice = new String[iTotItmList];
		String[] sAttrDesc = new String[] { "__BRAND__#", "BaseUom"/*, "Price",
				"Availability", "Immediate", "NextDay", "TwoPlusDays" */};
		String[] sTmp = null;
		Double price = 0.0;
		
		
		for (int i = 0; i < iTotItmList; i++) {
			eleItemDetails = (Element) nlItemDetails.item(i);
			if(log.isDebugEnabled()){
			log.debug(SCXmlUtil.getAttribute(eleItemDetails, "ItemID"));
			}
			sItmIds[i] = SCXmlUtil.getAttribute(eleItemDetails, "ItemID");
			sUoms[i] = SCXmlUtil.getAttribute(eleItemDetails, "UnitOfMeasure");
			/*String listPrice = SCXmlUtil.getXpathAttribute(eleItemDetails,
					"./ComputedPrice/@ListPrice");
			if (listPrice != null && listPrice.trim().length() > 0) {
				price = Double.parseDouble(listPrice);
			}
			sArrListPrice[i] = price > 0 ? "$" + price : "";*/
		}
		/*EB-694 xpedx catalog URL display for FSC, PEFC and SFI*/	
		Element outputItem = null;
		try {
			outputItem = getItemElement(sItmIds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setMSDSUrls(outputItem);
		for (int k = 0; k < sAttrDesc.length; k++) {
			String sThisAttrDesc = sAttrDesc[k];
			if ("__BRAND__#".equals(sThisAttrDesc)) {
				sTmp = sItmIds;
				
				// eb-2405: Display brand instead of hard-coded 'xpedx'
				sThisAttrDesc = sThisAttrDesc.replace("__BRAND__", getWCContext().getStorefrontId());
			} else if ("BaseUom".equals(sThisAttrDesc)) {
				sTmp = sUoms;
			} /*else if (sThisAttrDesc == "Immediate") {
				sTmp = sArrimmediate;
			} else if (sThisAttrDesc == "Price") {
				sTmp = sArrListPrice;
			} else if (sThisAttrDesc == "NextDay") {
				sTmp = sArrNextDay;
			} else if (sThisAttrDesc == "TwoPlusDays") {
				sTmp = sArrTwoPlusDays;
			} else if (sThisAttrDesc == "Availability") {
				sTmp = sArrAvailability;
			}*/

			inputItemAttrDoc = YFCDocument.createDocument("ItemAttribute");
			inputItmAttrElement = inputItemAttrDoc.getDocumentElement();
			inputItmAttrElement.setAttribute("AttributeDomainID", "");
			inputItmAttrElement.setAttribute("IsKeyAttribute", "");
			inputItmAttrElement.setAttribute("ItemAttributeDescription",
					sThisAttrDesc);
			inputItmAttrElement.setAttribute("ItemAttributeGroupKey", "");
			inputItmAttrElement.setAttribute("ItemAttributeKey", "");
			inputItmAttrElement.setAttribute("ItemAttributeName", "");
			inputItmAttrElement.setAttribute("SequenceNo", "");
			inputItmAttrElement.setAttribute("Value", "");

			inputAttributeElement = inputItemAttrDoc.createElement("Attribute");
			inputAttributeElement.setAttribute("AttributeDomainID", "");
			inputAttributeElement.setAttribute("AttributeGroupID", "");
			inputAttributeElement.setAttribute("AttributeID", sThisAttrDesc);
			inputAttributeElement.setAttribute("AttributeKey", "");
			inputAttributeElement.setAttribute("DataType", "TEXT");
			inputAttributeElement.setAttribute("IsAllowedValueDefined", "");
			inputAttributeElement.setAttribute("LongDescription", "");
			inputAttributeElement.setAttribute("SequenceNo", "");
			inputAttributeElement.setAttribute("ShortDescription", "");
			
			inputItmAttrElement.appendChild(inputAttributeElement);
			inputItmListElement = inputItemAttrDoc.createElement("ItemList");
			inputItmAttrElement.appendChild(inputItmListElement);

			for (int j = 0; j < iTotItmList; j++) {
				inputItmElement = inputItemAttrDoc.createElement("Item");

				inputItmElement.setAttribute("ItemGroupCode", "");
				inputItmElement.setAttribute("ItemID", sItmIds[j]);
				inputItmElement.setAttribute("ItemKey", "");
				inputItmElement.setAttribute("OrganizationCode", "");
				inputItmElement.setAttribute("UnitOfMeasure", "");

				inputAssignedValueListElement = inputItemAttrDoc
						.createElement("AssignedValueList");
				inputAssignedValueElement = inputItemAttrDoc
						.createElement("AssignedValue");
				inputAssignedValueElement.setAttribute("Value", sTmp[j]);

				inputAssignedValueListElement
						.appendChild(inputAssignedValueElement);
				inputItmElement.appendChild(inputAssignedValueListElement);
				inputItmListElement.appendChild(inputItmElement);

			}
			sinputItmAttrDoc = sinputItmAttrDoc + inputItemAttrDoc;
		}
	}

	/*private void processPandA(String[] sItmIds, String[] sUoms) {
		ArrayList<XPEDXItem> inputItems = new ArrayList<XPEDXItem>();
		XPEDXItem item = new XPEDXItem();

		sArrimmediate = new String[sItmIds.length];
		sArrNextDay = new String[sItmIds.length];
		sArrTwoPlusDays = new String[sItmIds.length];
		sArrAvailability = new String[sItmIds.length];
		for (int i = 0; i < sItmIds.length; i++) {
			item.setLegacyProductCode(sItmIds[i]); // item id is to be set
			item.setRequestedQtyUOM(sUoms[i]);
			item.setRequestedQty("2");
			inputItems.add(item);
			XPEDXPriceAndAvailability pna = XPEDXPriceandAvailabilityUtil
					.getPriceAndAvailability(inputItems);
			pnaHoverMap = XPEDXPriceandAvailabilityUtil.getPnAHoverMap(pna
					.getItems());
			if (pnaHoverMap.containsKey(sItmIds[i])) {
				// 
				JSONObject json = pnaHoverMap.get(sItmIds[i]);
				sArrimmediate[i] = json.get("Immediate").toString();
				sArrNextDay[i] = json.get("NextDay").toString();
				sArrTwoPlusDays[i] = json.get("TwoPlusDays").toString();
				sArrAvailability[i] = json.get("Availability").toString();

			} else {
				sArrimmediate[i] = "";
				sArrNextDay[i] = "";
				sArrTwoPlusDays[i] = "";
				sArrAvailability[i] = "";
			}
		}

	}*/

	private void getAllItemList() throws Exception {
		Document outputDoc = null;
		itemListMap = new HashMap();
		String customerId = wcContext.getCustomerId();
		outputDoc = XPEDXWCUtils.getAllItemList(customerId);
		Element outputEl = outputDoc.getDocumentElement();

		ArrayList<Element> listofWishLists = getXMLUtils().getElements(outputEl, "XPEDXMyItemsList");
		
		for(Element elem : listofWishLists){
			List<Element> items = getXMLUtils().getElements(elem, "XPEDXMyItemsItemsList");
			String itemCount="0";
			if(items != null && items.size()>0){
				Element itemElem=items.get(0);
				itemCount=itemElem.getAttribute("TotalNumberOfRecords");
			}
			listSizeMap.put(elem.getAttribute("MyItemsListKey"), itemCount);
		}
		
		if (listofWishLists != null) {
			for (Element list : listofWishLists) {
				itemListMap.put(list.getAttribute("MyItemsListKey"), list
						.getAttribute("Name"));
			}
		}
	}

	public Map getItemListMap() {
		return itemListMap;
	}
	
	@Override
	protected void manipulateMashupInputs(Map<String, Element> mashupInputs)
			throws CannotBuildInputException {
		Element prodComparisonInput = mashupInputs.get("ProductComparison");
		checkForNullItemKey(prodComparisonInput);
		Element prodComparisonExcludeInput = mashupInputs.get("ProductComparisonExcluded");
		checkForNullItemKey(prodComparisonExcludeInput);
		super.manipulateMashupInputs(mashupInputs);
	}
	
	private void checkForNullItemKey(Element prodComparisonInput) {
		if(prodComparisonInput!=null) {
			Element complexQuerElem = SCXmlUtil.getChildElement(prodComparisonInput, "ComplexQuery");
			if(complexQuerElem!=null) {
				Element orElem = SCXmlUtil.getChildElement(complexQuerElem, "Or");
				ArrayList<Element> andElems = SCXmlUtil.getElements(orElem, "And");
				if(andElems!=null && andElems.size()>0) {
					Iterator<Element> andIter = andElems.iterator();
					while(andIter.hasNext()) {
						Element andElem = andIter.next();
						Element exp = SCXmlUtil.getChildElement(andElem, "Exp");
						String itemKey = SCXmlUtil.getAttribute(exp, "Value");
						if(itemKey!=null && itemKey.trim().length()<=0)
							exp.setAttribute("Value", "null");
					}
				}
			}
		}
	}

	public String getItemListMapHTMLString(String itemId) {
		StringBuffer stringBuffer = new StringBuffer();
		String string = "<select name='itemListSelect_"+itemId+"' id='itemListSelect_"+itemId+"' onchange='javascript:addItemToList("+itemId+")'>";
		String optionValues = "<option value='-1'>Add item to List</option>";
		stringBuffer.append(string);
		Set<String> itemListMapSet = itemListMap.keySet();
		Iterator<String> iterator = itemListMapSet.iterator();
		String value = "";
		String key = "";
		while (iterator.hasNext()) {
			key = iterator.next();
			value = (String) itemListMap.get(key);
			optionValues += "<option value='" + key + "'>" + value
					+ "</option>";
		}
		stringBuffer.append(optionValues);
		stringBuffer.append("</select>");
		itemListMapHTMLString = stringBuffer.toString();
		itemListMapHTMLString=itemListMapHTMLString.replace("'", "\"").trim();
		return itemListMapHTMLString;
	}

	public void setItemListMap(Map itemListMap) {
		this.itemListMap = itemListMap;
	}
	
	public String replaceChar(String string) {
		if(string!=null) {
			do {
				string = string.replace("\'", "&#39;");
				string = string.replace("\"", "&#34;");
				string = string.replace(",", "");	
				
			}
			while(string.indexOf("\'")!=-1 || string.indexOf("\"")!= -1 || string.indexOf(",")!= -1);
		}
		return string;
	}

	HashMap<String, JSONObject> pnaHoverMap;
	protected Map itemListMap = new HashMap();
	protected String minOrderQty = null;
	protected String pricingUOM = null;
	protected String pricingUOMConvFactor = null;
	protected String baseUOM = null;
	protected String prodMweight = null;
	protected Element m_itemListElem;	

	private static final Logger log = Logger
			.getLogger(XPEDXProductComparisonAction.class);
	private String itemListMapHTMLString;

	private HashMap<String, String> listSizeMap = new HashMap<String, String>();
	
	public HashMap<String, String> getListSizeMap() {
		return listSizeMap;
	}

	public void setListSizeMap(HashMap<String, String> listSizeMap) {
		this.listSizeMap = listSizeMap;
	}
	
	private String itemDtlBackPageURL="";
	
	public String getItemDtlBackPageURL() {
		return itemDtlBackPageURL;
	}

	public void setItemDtlBackPageURL(String itemDtlBackPageURL) {
		this.itemDtlBackPageURL = itemDtlBackPageURL;
	}
    
	/*EB-694 xpedx catalog URL display for FSC, PEFC and SFI*/
	
	private Element getItemElement(String[] itemId) throws Exception {
    	Map<String, String> valueMap = new HashMap<String, String>();	
    	Element outputItem=null;
    	try {
    	IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		Element inputXpedxGetItemListElement = WCMashupHelper.getMashupInput("xpedxgetItemList", wcContext.getSCUIContext());
		String inputXpedxGetItemListXml = SCXmlUtil.getString(inputXpedxGetItemListElement);
		Element complexQuery = SCXmlUtil.getChildElement(inputXpedxGetItemListElement, "ComplexQuery");
		Element OrElem = SCXmlUtil.getChildElement(complexQuery, "Or");
		
		for(String itemIdlist : itemId) {
			Element exp = inputXpedxGetItemListElement.getOwnerDocument().createElement("Exp");
			exp.setAttribute("Name", "ItemID");
			exp.setAttribute("Value", itemIdlist);
			SCXmlUtil.importElement(OrElem, exp);
		}
		Object obj = WCMashupHelper.invokeMashup("xpedxgetItemList", inputXpedxGetItemListElement,	wcContext.getSCUIContext());
		Document outputDoc  = ((Element) obj).getOwnerDocument();
		outputItem = outputDoc.getDocumentElement();
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
			return outputItem;
		}
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}		
		return outputItem;
    }
}

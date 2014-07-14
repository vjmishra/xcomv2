package com.sterlingcommerce.xpedx.webchannel.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

@SuppressWarnings("serial")

public class XPEDXEstimatingFilesAction extends WCMashupAction {
	
	private String pricingWareHouse;
	private String organizationName;
	
	@Override
	public String execute() {
		
		Document outputDoc = null;
		try {
			String brandCode="";
			String storeFront = wcContext.getStorefrontId();
			if(storeFront.equalsIgnoreCase("xpedx")){
				brandCode="XPED";
			}
			else if(storeFront.equalsIgnoreCase("Saalfeld")){
				brandCode="SAAL";
			}
			outputDoc=XPEDXWCUtils.getCustomerDetails(getWCContext().getCustomerId(), getWCContext().getStorefrontId());
			String environmentCode = SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/Customer/Extn/@ExtnEnvironmentCode");
			/*Start code for Jira 4315*/
			String shipFromBranch = SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/Customer/Extn/@ExtnShipFromBranch");
			//pricingWareHouse = SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/Customer/Extn/@ExtnPriceWarehouse");
			if(environmentCode!=null && !environmentCode.isEmpty() && shipFromBranch!=null && !shipFromBranch.isEmpty()) {
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("/Organization/@OrganizationCode", (shipFromBranch + "_" + environmentCode));
				Element input = WCMashupHelper.getMashupInput("XPEDXgetPricingWarehouse", valueMap, getWCContext());
				Element outputE2 = (Element)WCMashupHelper.invokeMashup("XPEDXgetPricingWarehouse", input, wcContext.getSCUIContext());
				pricingWareHouse = SCXmlUtil.getXpathAttribute(outputE2, "/OrganizationList/Organization/Extn/@ExtnPriceWarehouse");
			}			
			/*End of code for Jira 4315*/
			
			if(environmentCode!=null && !environmentCode.isEmpty() && pricingWareHouse!=null && !pricingWareHouse.isEmpty()) {
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("/XPXCatalogExp/@OrganizationKey", (pricingWareHouse + "_" + environmentCode));
				valueMap.put("/XPXCatalogExp/@BrandCode", brandCode);

				Element input = WCMashupHelper.getMashupInput("XPEDXCatalogExport", valueMap, getWCContext());
				
				Element outputE2 = (Element)WCMashupHelper.invokeMashup("XPEDXCatalogExport", input, wcContext.getSCUIContext());
				//organizationName = SCXmlUtil.getXpathAttribute(outputE2, "/OrganizationList/Organization/@OrganizationName");
				Element custXrefEle = XMLUtilities.getElement(outputE2,"XPXCatalogExp");				
				NodeList CatalogExpList = outputE2.getElementsByTagName("XPXCatalogExp");		
					for(int LegacyUomCounter = 0;LegacyUomCounter<CatalogExpList.getLength();LegacyUomCounter++)
					{
						Element LegacyElement = (Element)CatalogExpList.item(LegacyUomCounter);
						String Label = LegacyElement.getAttribute("Label");
						String Url = LegacyElement.getAttribute("Url");
						//unSortedCatalogExp.put(Label,Url);						
						unSortedCatalogExp.put(Url,Label);						
					}
					
			//Sort Data before presenting to UI ...
			CatalogExp = sortHashMap(unSortedCatalogExp);
			}			
			
		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return SUCCESS;
	}
	
	public String getPricingWareHouse() {
		return pricingWareHouse;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public HashMap<String, String> getCatalogExp() {
		return CatalogExp;
	}
	public void setCatalogExp(HashMap<String, String> catalogExp) {
		CatalogExp = catalogExp;
	}
	
	private HashMap<String,String> CatalogExp = new HashMap<String, String>();
	
	private HashMap<String,String> unSortedCatalogExp = new HashMap<String, String>();
	
	private HashMap<String, String> sortHashMap(HashMap<String, String> input){
		
	    Map<String, String> tempMap = new HashMap<String, String>();
	    
	    for (String wsState : input.keySet()){
	        tempMap.put(wsState,input.get(wsState));
	    }

	    List<String> mapKeys = new ArrayList<String>(tempMap.keySet());
	    
	    List<String> mapValues = new ArrayList<String>(tempMap.values());
	    
	    HashMap<String, String> sortedMap = new LinkedHashMap<String, String>();
	    
	    TreeSet<String> sortedSet = new TreeSet<String>(mapValues);
	    
	    Object[] sortedArray = sortedSet.toArray();
	    
	    int size = sortedArray.length;
	    
	    for (int i=0; i < size; i++){
	        sortedMap.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), 
	                      (String)sortedArray[i]);
	    }
	    return sortedMap;
	}
	
}

package com.xpedx.nextgen.pricelistmanagement.api;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXLoadPriceListAPI implements YIFCustomApi
{
	
	private static YFCLogCategory log;
	private static YIFApi api = null;
	private final String brokenUOM = "M_BKN";

	String getPricelistLineListTemplate = "global/template/api/getPricelistLineList.XPXCreatePriceListService.xml";
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
			
		}
	}
	
	
	public Document loadPriceList(YFSEnvironment env, Document inputXML) throws Exception
	{
		/** try-catch block added by Arun Sekhar on 24-Jan-2011 for CENT tool logging **/
		try{

			
			ArrayList priceListHeaderElements = new ArrayList();
			ArrayList priceListLineElements = new ArrayList();
			/*
			 * I/p xml
			 *  <PriceBooks>
	         *  <PriceBook EnvironmentId =”” CompanyCode =”’ ProcessCode=”” MasterProductCode=”” LegacyProductCode=”” StockIndicator=”” 
	         *  Warehouse=”” Segment=”” MSRP=””  PricingUoM=”” CurrencyCode=””>
	         *  <PriceBrackets>
		     *  <PriceBracket UOM=”” Qty=”” Price=”” />
	         *  </PriceBrackets >
	         *  </PriceBook>
	         *  </PriceBooks >
	         *  
	         *  O/p xml
	         *  
	         *  <PricelistHeader Currency="xml:/PriceBooks/PriceBook/@CurrencyCode" Description="" EndDateActive="<TBD>" 
	         *  Operation="xml:/PriceBooks/PriceBook/@ProcessCode" OrganizationCode="xpedx" PricelistName="EnvironmentId-CompanyCode-Warehouse" 
	         *  PricingStatus="ACTIVE" StartDateActive="<sysdate>" />
			 */

			Element inputXMLRoot = inputXML.getDocumentElement();
			
			NodeList priceBookList = inputXMLRoot.getElementsByTagName(XPXLiterals.E_PRICE_BOOK);
			
			for(int i=0; i < priceBookList.getLength(); i++)
			{
				Element priceBookElement = (Element) priceBookList.item(i);
				String itemId = priceBookElement.getAttribute(XPXLiterals.E_LEGACY_PRODUCT_CODE);
				
				//ProcessCode is no more sent in the load hence commenting it out...
				String processCode = priceBookElement.getAttribute(XPXLiterals.A_PROCESS_CODE);
				
				String inventoryUOM = getInventoryUOM(env,itemId);
				
				if(inventoryUOM != null && inventoryUOM.trim().length() !=0)
				{
					// Means Item exists in YFS_ITEM because if it existed then UnitOfMeasure value would have been populated
					
					//Make sure that all the tier brackets have the same UOM (Else log it in CENT tool) - Jira#2637
					//XB - 562 - commented the UOM tier check
					/*boolean isAllTierUOMSame = checkTierUOMs(priceBookElement,env);
					if(!isAllTierUOMSame){
						//do not create the price list record.ignore it.
						log.error("Tier UOM mismatch between different tiers blocks.Pricebook record for LegacyProductCode = "+ itemId+" will be ignored.");
						continue;
					}*/
				
					Document managePriceListHeaderInputDoc = createPriceListHeaderInputDoc(env,priceBookElement);
				
							
				Document managePriceListLineInputDoc = createManagePriceListLineInputDoc(env,priceBookElement,inventoryUOM);
				
				/** Below try-catch block commented by Arun Sekhar,
				 * as all the exceptions need to be handled by the outer try-catch block newly added for CENT Tool integration **/			
				/*try {*/
					
					/*if(!processCode.equalsIgnoreCase("D"))
					{*/
						log.debug("The input to managePricelistHeaderList is: "+SCXmlUtil.getString(managePriceListHeaderInputDoc));	
					    api.invoke(env,XPXLiterals.MANAGE_PRICE_LIST_HEADER_API, managePriceListHeaderInputDoc);
					//}
					
					log.debug("The input to managePricelistLineList is: "+SCXmlUtil.getString(managePriceListLineInputDoc));
					api.invoke(env,XPXLiterals.MANAGE_PRICE_LIST_LINE_API, managePriceListLineInputDoc);
					
				/*} catch (YFSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				}
				
				else
				{
					// Create an alert for invalid Item Id
					
					/*try {*/
						
						Document priceBookDocument = createDocument(XPXLiterals.E_PRICE_BOOK);
						priceBookDocument = getDocument(priceBookElement, true);
						
						log.debug("The document being sent is: "+SCXmlUtil.getString(priceBookDocument));
						
						api.executeFlow(env, "XPXCreateAlertForInvalidItemIdService", priceBookDocument);
/*					} catch (YFSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					
				}
			}
		}catch (NullPointerException ne) {
			log.error("NullPointerException: " + ne.getStackTrace());	
			prepareErrorObject(ne, XPXLiterals.PB_B_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inputXML);	
			throw ne;
		} catch (YFSException yfe) {
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.PB_B_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inputXML);
			throw yfe;
		} catch (RemoteException re) {
			log.error("YFSException: " + re.getStackTrace());
			prepareErrorObject(re, XPXLiterals.PB_B_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inputXML);
			throw re;
		}catch (Exception e) {
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.PB_B_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inputXML);
			throw e;
		}
		
		return inputXML;
	}
	
	/*
	 * checkTierUOMs() methods checks to see if all the tier UOM's are same.
	 */
	private boolean checkTierUOMs(Element priceBookElement, YFSEnvironment env) {
		boolean isAllTierUOMSame = true;
		String itemId = priceBookElement.getAttribute(XPXLiterals.E_LEGACY_PRODUCT_CODE);
		Element e_brackets = SCXmlUtil.getChildElement(priceBookElement, XPXLiterals.E_PRICE_BRACKETS);
		if(null == e_brackets){
			return isAllTierUOMSame;
		}
		//loop through all the brackets
		NodeList nl_bracket = e_brackets.getElementsByTagName(XPXLiterals.E_PRICE_BRACKET);
		int iBracket = nl_bracket.getLength();
		if(iBracket <=0){
			return isAllTierUOMSame;
		}
		String lastTierUOM = "";
		for(int i=0;i<iBracket;i++){
			Element priceBracketsElement = (Element)nl_bracket.item(i);
			String currentTierUOM = priceBracketsElement.getAttribute(XPXLiterals.UOM);
			if(i==0){
				lastTierUOM = currentTierUOM;
			}
			if(YFCCommon.isVoid(currentTierUOM)){
				currentTierUOM = "";
			}
			if(lastTierUOM.equals(currentTierUOM)){
				lastTierUOM = currentTierUOM;
				continue;
			}else{
				//log it in the CENT and return false;
				isAllTierUOMSame =  false;
				log.error("Tier UOM is not matching between different tiers in a pricebook.Check CENT for more details. LegacyProductCode = "+ itemId);
				throw new YFSException("Tier UOM is not matching between different tiers in a pricebook.",
						"ERROR_LOAD_PRICE_BOOK",
						"Tier UOM is not matching between different tiers in a pricebook.Check CENT for more details.");
			}
		}
		return isAllTierUOMSame;
	}

	/**@author asekhar-tw on 24-Jan-2011
	 * This method prepares the error object with the exception details which in turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML){
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();	
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}
	
	private Document createManagePriceListLineInputDoc(YFSEnvironment env, Element priceBookElement, String inventoryUOM)
	{
		log.debug("Inside pricelist line method");
		Document  createPriceListLineInputDoc = null;
		String priceListLineKey = null;
		String operation = null;
		ArrayList pricelistlinekeyList = new ArrayList();
		
		String legacyProductCode = priceBookElement.getAttribute(XPXLiterals.E_LEGACY_PRODUCT_CODE);
		String processCode = priceBookElement.getAttribute(XPXLiterals.A_PROCESS_CODE);
		String pricingUOM = priceBookElement.getAttribute(XPXLiterals.A_PRICING_UOM);
		String envtId  = priceBookElement.getAttribute(XPXLiterals.A_ENVIRONMENT_ID);
		String companyCode = priceBookElement.getAttribute(XPXLiterals.A_COMPANY_CODE);
		String wareHouse = priceBookElement.getAttribute(XPXLiterals.A_WAREHOUSE);
		String masterProductCode = priceBookElement.getAttribute(XPXLiterals.A_MASTER_PRODUCT_CODE);
		String stockIndicator = priceBookElement.getAttribute(XPXLiterals.A_STOCK_INDICATOR);
		
		if(processCode.equalsIgnoreCase("A") || processCode.equalsIgnoreCase("C"))
		{
	           operation  = XPXLiterals.MANAGE;
		}
		else if(processCode.equalsIgnoreCase("D"))
		{
	           operation  = XPXLiterals.DELETE;
		}
		
		String priceListName = envtId.trim()+"-"+companyCode.trim()+"-"+wareHouse.trim();
		
		Document getPriceListLineListInputDoc = createPriceListLineListInputDoc(legacyProductCode,priceListName);
		
		env.setApiTemplate(XPXLiterals.GET_PRICE_LIST_LINE_LIST_API, getPricelistLineListTemplate);
		Document getPriceListLineListOutputDoc = null;
		try {
				getPriceListLineListOutputDoc = api.invoke(env, XPXLiterals.GET_PRICE_LIST_LINE_LIST_API, getPriceListLineListInputDoc);
			} catch (YFSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			env.clearApiTemplate(XPXLiterals.GET_PRICE_LIST_LINE_LIST_API);
			
			log.debug("The getPriceListLineList output is: "+SCXmlUtil.getString(getPriceListLineListOutputDoc));
			
			NodeList priceListLineList = getPriceListLineListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_PRICE_LIST_LINE);
			if(priceListLineList.getLength()> 0)
			{
				for(int i=0; i<priceListLineList.getLength(); i++)
				{
					Element priceListLine = (Element)priceListLineList.item(i);
					
					Element priceListQuantityTierList = (Element)priceListLine.getElementsByTagName(XPXLiterals.E_PRICE_LIST_LINE_QTY_TIER_LIST).item(0);
					
					if(priceListQuantityTierList!=null)
					{
						NodeList priceListQuantityTier = priceListQuantityTierList.getElementsByTagName(XPXLiterals.E_PRICE_LIST_LINE_QTY_TIER);
						/*Begin - Changes made by Mitesh Parikh for JIRA# 3206*/
						priceListLineKey = priceListLine.getAttribute(XPXLiterals.A_PRICE_LIST_LINE_KEY);
				        if(priceListLineKey!=null)
				          pricelistlinekeyList.add(priceListLineKey);
				        
				        /*if(priceListQuantityTier.getLength() > 0)
				        {
					          priceListLineKey = priceListLine.getAttribute(XPXLiterals.A_PRICE_LIST_LINE_KEY);
					          pricelistlinekeyList.add(priceListLineKey);
				        
				        }*/
				        /*End - Changes made by Mitesh Parikh for JIRA# 3206*/
					}
				}
				
			    if(pricelistlinekeyList.size()>0)
			    {
			    	//Delete all existing price list lines
			    	Document managePriceListLineInputDoc = createDocument(XPXLiterals.E_PRICE_LIST_LINE_LIST);
			    	managePriceListLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, XPXLiterals.A_ORGANIZATIONCODE_VALUE);
					
					for(int i=0; i<pricelistlinekeyList.size(); i++)
					{
						Element priceListLineElement = managePriceListLineInputDoc.createElement(XPXLiterals.E_PRICE_LIST_LINE);
						priceListLineElement.setAttribute(XPXLiterals.A_PRICE_LIST_LINE_KEY, (String)pricelistlinekeyList.get(i));
						priceListLineElement.setAttribute(XPXLiterals.A_OPERATION, XPXLiterals.DELETE);
						
						managePriceListLineInputDoc.getDocumentElement().appendChild(priceListLineElement);
					}
					
					log.debug("The input to managePricelistLineList is: "+SCXmlUtil.getString(managePriceListLineInputDoc));
					try {
						api.invoke(env,XPXLiterals.MANAGE_PRICE_LIST_LINE_API, managePriceListLineInputDoc);
					} catch (YFSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			
			}	
		
		YFCDate activeDate = new YFCDate();
		//End date is TBD. Currently, giving it as High date from current date
		//YFCDate endDate = activeDate.getNewDate(3650);				
		
		Element priceBracketsElement = (Element)priceBookElement.getElementsByTagName(XPXLiterals.E_PRICE_BRACKETS).item(0);
		NodeList priceBracketList = priceBracketsElement.getElementsByTagName(XPXLiterals.E_PRICE_BRACKET);
		Element firstPriceBracketElement = (Element)priceBracketsElement.getElementsByTagName(XPXLiterals.E_PRICE_BRACKET).item(0);
		String firstTierUOM = firstPriceBracketElement.getAttribute(XPXLiterals.UOM);
		String firstListPrice = firstPriceBracketElement.getAttribute(XPXLiterals.PRICE);		
		String firstQuantity = firstPriceBracketElement.getAttribute(XPXLiterals.QTY);
		
		if(firstTierUOM.equals(brokenUOM) && priceBracketList.getLength() > 1){
			Element secondPriceBracketElement = (Element)priceBracketsElement.getElementsByTagName(XPXLiterals.E_PRICE_BRACKET).item(1);
			firstTierUOM = secondPriceBracketElement.getAttribute(XPXLiterals.UOM);
			firstListPrice = secondPriceBracketElement.getAttribute(XPXLiterals.PRICE);
			firstQuantity = secondPriceBracketElement.getAttribute(XPXLiterals.QTY);
		}
		
		if(!firstQuantity.equals("1")){
			firstTierUOM = "dummyUOM";
		}
		
        createPriceListLineInputDoc = createDocument(XPXLiterals.E_PRICE_LIST_LINE_LIST);
        createPriceListLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, XPXLiterals.A_ORGANIZATIONCODE_VALUE);
        
        Element priceListLineElement = createPriceListLineInputDoc.createElement(XPXLiterals.E_PRICE_LIST_LINE);
        priceListLineElement.setAttribute(XPXLiterals.A_ITEM_ID, legacyProductCode);
        priceListLineElement.setAttribute(XPXLiterals.A_OPERATION, operation);
        priceListLineElement.setAttribute(XPXLiterals.A_PRICING_STATUS, XPXLiterals.ACTIVE);
        priceListLineElement.setAttribute(XPXLiterals.A_START_DATE_ACTIVE, activeDate.getString());
        priceListLineElement.setAttribute(XPXLiterals.A_END_DATE_ACTIVE, activeDate.HIGH_DATE.getString());
        //List Price changed to 0 for - EB-2268
        priceListLineElement.setAttribute(XPXLiterals.A_LIST_PRICE, "0");
        priceListLineElement.setAttribute(XPXLiterals.A_UNIT_OF_MEASURE, inventoryUOM);
        //priceListLineElement.setAttribute(XPXLiterals.A_UNIT_OF_MEASURE, pricingUOM);
        
        Element  priceListLineExtnElement = createPriceListLineInputDoc.createElement(XPXLiterals.E_EXTN);
        priceListLineExtnElement.setAttribute(XPXLiterals.A_EXTN_PRICING_UOM, pricingUOM);
        priceListLineExtnElement.setAttribute(XPXLiterals.A_EXTN_TIER_UOM, firstTierUOM);
        
        //Added as per review comments---adding item details to line level
        priceListLineExtnElement.setAttribute(XPXLiterals.A_EXTN_MASTER_PRODUCT_CODE, masterProductCode);
        priceListLineExtnElement.setAttribute(XPXLiterals.A_EXTN_STOCK_INDICATOR, stockIndicator);
        
        priceListLineElement.appendChild(priceListLineExtnElement);
        
        Element pricelistLineQuantityTierList = createPriceListLineInputDoc.createElement(XPXLiterals.E_PRICE_LIST_LINE_QTY_TIER_LIST);
        
        NodeList priceBracketElementList = priceBracketsElement.getElementsByTagName(XPXLiterals.E_PRICE_BRACKET);
        for(int i=0; i<priceBracketElementList.getLength(); i++)
        {
        	Element priceBracketElement = (Element)priceBracketElementList.item(i);
        	String fromQty = priceBracketElement.getAttribute(XPXLiterals.QTY);
    		String listPrice = priceBracketElement.getAttribute(XPXLiterals.PRICE);
    		String tierUOM =  priceBracketElement.getAttribute(XPXLiterals.UOM);
    		 
            Element pricelistLineQuantityTier = createPriceListLineInputDoc.createElement(XPXLiterals.E_PRICE_LIST_LINE_QTY_TIER);
            /*
            // START - Fix for Jira# 1469
            if (fromQty != null && fromQty.trim().length() > 0 && Integer.parseInt(fromQty) <= 1)
               	pricelistLineQuantityTier.setAttribute(XPXLiterals.A_FROM_QUANTITY, "2");
            else
            	pricelistLineQuantityTier.setAttribute(XPXLiterals.A_FROM_QUANTITY, fromQty);
            // END - Fix for Jira# 1469
            */
            /*XB - 562 - skip the first item since it is already added*/
          //Commented below line for - EB-2268
            /*if (fromQty != null && listPrice != null && tierUOM != null && fromQty.equals(firstQuantity) && listPrice.equals(firstListPrice) && tierUOM.equals(firstTierUOM)){
            	continue;
            }*/
            /*XB - 562 */
            
            pricelistLineQuantityTier.setAttribute(XPXLiterals.A_FROM_QUANTITY, fromQty);
            
            pricelistLineQuantityTier.setAttribute(XPXLiterals.A_LIST_PRICE, listPrice);
            pricelistLineQuantityTier.setAttribute(XPXLiterals.A_OPERATION, operation);
            pricelistLineQuantityTierList.appendChild(pricelistLineQuantityTier);
            if(i==priceBracketElementList.getLength()-1){
            	pricelistLineQuantityTier.setAttribute("ToQuantity", "999999999");
            }
            
            //Begin: set the line extn elements - Jira2637
            Element  priceListTierLineExtnElement = pricelistLineQuantityTier.getOwnerDocument().createElement(XPXLiterals.E_EXTN);
            priceListTierLineExtnElement.setAttribute(XPXLiterals.A_EXTN_PRICING_UOM, pricingUOM);
            priceListTierLineExtnElement.setAttribute(XPXLiterals.A_EXTN_TIER_UOM, tierUOM);
            pricelistLineQuantityTier.appendChild(priceListTierLineExtnElement);
          //End:set the line extn elements - Jira2637
        }
        priceListLineElement.appendChild(pricelistLineQuantityTierList);
        
        Element priceListHeaderElement = createPriceListLineInputDoc.createElement(XPXLiterals.E_PRICE_LIST_HEADER);
        priceListHeaderElement.setAttribute(XPXLiterals.A_PRICE_LIST_NAME, priceListName);
        priceListLineElement.appendChild(priceListHeaderElement);
        
        createPriceListLineInputDoc.getDocumentElement().appendChild(priceListLineElement);		
        
        log.debug("The manage Price List input doc is: "+SCXmlUtil.getString(createPriceListLineInputDoc));
		return createPriceListLineInputDoc;
	}
	private Document createPriceListLineListInputDoc(String legacyProductCode, String priceListName) {


		Document priceListLineListInputDoc = createDocument(XPXLiterals.E_PRICE_LIST_LINE);
		priceListLineListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ITEM_ID, legacyProductCode);
		
		Element priceListHeader = priceListLineListInputDoc.createElement(XPXLiterals.E_PRICE_LIST_HEADER);
		priceListHeader.setAttribute(XPXLiterals.A_PRICE_LIST_NAME, priceListName);
		
		priceListLineListInputDoc.getDocumentElement().appendChild(priceListHeader);
		
		return priceListLineListInputDoc;
	}

	private String getInventoryUOM(YFSEnvironment env, String itemId) {

        String inventoryUOM = null;
		
		Document getItemListInputDoc = createDocument(XPXLiterals.E_ITEM);
		getItemListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ITEM_ID, itemId);
		getItemListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE,XPXLiterals.A_ORGANIZATIONCODE_VALUE);
		
		try
		{
			Document getItemListOutputDoc = api.invoke(env, XPXLiterals.GET_ITEM_LIST_API, getItemListInputDoc);
			Element itemElement = (Element)getItemListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_ITEM).item(0);
			
			if(itemElement != null)
			{
			inventoryUOM = itemElement.getAttribute(XPXLiterals.A_UNIT_OF_MEASURE);
			}
			/*else
			{
				log.debug("There is no Item in the table");
			}*/
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.debug("The inventory UOM returned is: "+inventoryUOM);
		return inventoryUOM;
	}
	private Document createPriceListHeaderInputDoc(YFSEnvironment env, Element priceBookElement)
	{
		//String operationPerformed = null;
		Document managePriceListHeaderInputDoc = createDocument(XPXLiterals.E_PRICE_LIST_HEADER);
		
		String currencyCode = priceBookElement.getAttribute(XPXLiterals.A_CURRENCY_CODE);		
		//String processCode = priceBookElement.getAttribute(XPXLiterals.A_PROCESS_CODE);
		String envtId  = priceBookElement.getAttribute(XPXLiterals.A_ENVIRONMENT_ID);
		String companyCode = priceBookElement.getAttribute(XPXLiterals.A_COMPANY_CODE);
		String wareHouse = priceBookElement.getAttribute(XPXLiterals.A_WAREHOUSE);
		//String legacyProductCode = priceBookElement.getAttribute(XPXLiterals.E_LEGACY_PRODUCT_CODE);
		
		/******************************Fix for bug#11665 by Prasanth Kumar M.***************************************/
		
		//String masterProductCode = priceBookElement.getAttribute(XPXLiterals.A_MASTER_PRODUCT_CODE);
		//String stockIndicator = priceBookElement.getAttribute(XPXLiterals.A_STOCK_INDICATOR);
		String pricingUOM = priceBookElement.getAttribute(XPXLiterals.A_PRICING_UOM);
		
		/**********************************************************************************************************/
		
		/*if(processCode.equalsIgnoreCase("A") || processCode.equalsIgnoreCase("C"))
		{
			operationPerformed  = XPXLiterals.MANAGE;
		}
		else if(processCode.equalsIgnoreCase("D"))
		{
			operationPerformed  = XPXLiterals.DELETE;
		}*/
		
		
		String priceListName = envtId.trim()+"-"+companyCode.trim()+"-"+wareHouse.trim();
		
		/********Check if price list header already exists********************/
		
		Document getPricelistHeaderListOutputDoc = getPricelistHeaderList(env, priceListName);		
		
		YFCDate activeDate = new YFCDate();
		
		//End date is TBD. Currently, giving it as HIGH DATE from current date
		//YFCDate endDate = activeDate.getNewDate(3650);
				
		managePriceListHeaderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CURRENCY, currencyCode);
		managePriceListHeaderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OPERATION, XPXLiterals.MANAGE);
		managePriceListHeaderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, XPXLiterals.A_ORGANIZATIONCODE_VALUE);
		managePriceListHeaderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_PRICE_LIST_NAME, priceListName);
		
		
		if(getPricelistHeaderListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_PRICE_LIST_HEADER).getLength()==0)
		{
		  //Means price list header does not exist	
	      managePriceListHeaderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_PRICING_STATUS, XPXLiterals.ACTIVE);
		  managePriceListHeaderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_START_DATE_ACTIVE, activeDate.getString());
		  managePriceListHeaderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_END_DATE_ACTIVE, activeDate.HIGH_DATE.getString());
		}  
		managePriceListHeaderInputDoc.getDocumentElement().setAttribute("Description",priceListName);
		
		Element managePriceListHeaderInputDocExtn = managePriceListHeaderInputDoc.createElement(XPXLiterals.E_EXTN);
		managePriceListHeaderInputDocExtn.setAttribute(XPXLiterals.A_EXTN_ENVT_ID, envtId);
		managePriceListHeaderInputDocExtn.setAttribute(XPXLiterals.A_EXTN_COMPANY_CODE, companyCode);
		managePriceListHeaderInputDocExtn.setAttribute(XPXLiterals.A_EXTN_PRICE_WAREHOUSE, wareHouse);
		
		
		/******************Fix for bug# 11665 by Prasanth Kumar M.***********************************************/
		
        
		managePriceListHeaderInputDocExtn.setAttribute(XPXLiterals.A_EXTN_PRICING_UOM, pricingUOM);
		
		/********************************************************************************************************/
		
		managePriceListHeaderInputDoc.getDocumentElement().appendChild(managePriceListHeaderInputDocExtn);
		
		
		log.debug("The input xml to managePriceListHeader is: "+SCXmlUtil.getString(managePriceListHeaderInputDoc));
		
		return managePriceListHeaderInputDoc;
	}
	private Document getPricelistHeaderList(YFSEnvironment env, String priceListName) 
	{
		Document priceListHeaderListOutputDoc = null;
		
		Document priceListHeaderListInputDoc = createDocument(XPXLiterals.E_PRICE_LIST_HEADER);
		priceListHeaderListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_PRICE_LIST_NAME, priceListName);
		
		
		try {
			priceListHeaderListOutputDoc = api.invoke(env, XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API, priceListHeaderListInputDoc );
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return priceListHeaderListOutputDoc;
	}

	private Document createDocument(String docElementTag) {

		Document doc = getNewDocument();
		Element ele = doc.createElement(docElementTag);
		doc.appendChild(ele);
		return doc;			

	}

	public static Document getNewDocument() {
		return new DocumentImpl();
	}
	
	private Document getDocument(Element inputElement, boolean deep)
	throws IllegalArgumentException, Exception{

//		Validate input element
if (inputElement == null) {
	throw new IllegalArgumentException(
			"Input element cannot be null in "
			+ "XmlUtils.getDocument method");
}

// Create a new document
Document outputDocument = getDocument();

// Import data from input element and
// set as root element for output document
outputDocument.appendChild(outputDocument
		.importNode(inputElement, deep));

// return output document
return outputDocument;
	}



	private Document getDocument() throws ParserConfigurationException {
		// Create a new Document Bilder Factory instance
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
		.newInstance();

		// Create new document builder
		DocumentBuilder documentBuilder = documentBuilderFactory
		.newDocumentBuilder();

		// Create and return document object
		return documentBuilder.newDocument();
	}
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

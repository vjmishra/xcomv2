package com.xpedx.nextgen.stockcheck.api;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfs.japi.YFSUserExitException;

public class XPXStockCheckReqRespAPI implements YIFCustomApi
{
	private static final String getCustomerListTemplate = "global/template/api/getCustomerList.XPXB2BDraftOrderCreationService.xml"; //TODO doesn't exist? Need?
	private static final String getCategoryListTemplate = "global/template/api/getCategoryList.XPXStockCheck.xml";
	private static final String getItemListTemplate = "global/template/api/getItemList.XPXB2BStockCheckService.xml";
	private static final String getItemUomMasterListTemplate = "global/template/api/getItemUomMasterList.XPXMasterUomLoad.xml";

	private static final String ERROR_LEVEL_COMPLETE_SUCCESS = "0";
	private static final String ERROR_LEVEL_PARTIAL_FAILURE = "1";
	private static final String ERROR_LEVEL_COMPLETE_FAILURE = "2";
	private static final String errorMessage_503 = "503 - An application error has occurred. If this error persists, please call the eBusiness " +
			"Customer Support desk at 1-877-269-1784.";
	private static final String errorMessage_105 = "105 - The stock check request is not in the correct format.  " +
			"Please contact your CSR or eBusiness Customer Support at 1- 877-269-1784.";
	private static final String errorMessage_104 = "104 - Please restrict your Stock Check request to 200 items or less.";
	private static final String errorMessage_100 = "100 - Sorry, we could not verify the User ID / Password that was sent.";
	private static final String errorMessage_101 = "101 - Sorry, the user credentials supplied is not authorized to access the Stock Check Web Service.";
	private static final String errorMessage_102 = "102 - Sorry, the customer is not authorized to access the Stock Check Web Service.";
	private static final String errorMessage_103 = "103 - Sorry, the user is not authorized on the specified customer location (Invalid eTrading ID/Customer Location).";
	private static final String B2BSourceIndicator = "1";

	private static YFCLogCategory log;
	private static YIFApi api = null;
	private static Map<String,String> uomDescMap;
	private static final Map<String,String> maxHeaderErrorMap;
	private static final Map<String,String> maxItemErrorMap;

    private String masterCustomerId;
    private Document shipToCustomerListOutputDoc ;
    private Document masterCustomerDetailsOutputDoc;
    private String userEmail;
    private String password;
    private String eTradingId;
    private Document stockCheckOutputDocument;
    private NodeList stockCheckItemList;
    private Map<Integer,String> requestedXpedxItemsMap; // Valid items for for P and A request
    private Map<Integer,String> qtysForValidPositions; //Quantities of valid Items for P and A request
    private Map<Integer,String> uomsForValidPositions; //UOMS of valid Items for P and A request
    private Map<String,Element> validItemElementsMap; // Valid Elements map
    private Map<String,String> xpedxItemsOMQtyMap; //Order multiple quantities map of valid  items
    private Map<String,String> xpedxItemsInventoryMap; // Inventory indicator map of valid  items
    private Map<String,String> validItemsUOMMap; // one of the valid UOMs for each valid Item
    private Set<Integer> inValidItemPositions; // invalid positions
    private Set<Integer> inValidUOMPositionsForValidItems; // invalid UOM postions for valid Items
    private Map<String,Element> uomListForValidItemsMap; // List of UOMs(Ex: Carton, package, Sheet) for each valid item
    private Map<String,String> uomToLegacyMap; // UOM to Legacy Map
    private Map<String,String> legacyToUOMMap; // Legacy to UOM Map
    
    /* Ship-to information */
    private String envtId = null;
    private String companyCode = null;
    private String shipToSuffix = null;
    private String shipFromBranch = null;
    private String legacyCustomerNumber = null;
    private String customerOrderBranch = null;
    private String customerEnvtId = null;
    private String customerID = null;
    private String organizationCode = null;
    private String sapParentAccountNo = null;
    
    private Document pAndAResponseDocument;
    
	static {
		// MAX header error codes with messages
		maxHeaderErrorMap = new HashMap<String, String>();
		maxHeaderErrorMap.put("01", "Invalid Customer");
		maxHeaderErrorMap.put("02", "Company # missing/blank");
		maxHeaderErrorMap.put("03", "Invalid Ship-to# for this customer");

		// MAX item error codes with messages
		maxItemErrorMap = new HashMap<String, String>(20);
		maxItemErrorMap.put("01", "Invalid Item Number");
		maxItemErrorMap.put("02", "Item number missing");
		maxItemErrorMap.put("03", "Bad UOM Not a MAX Primary or alternate UOM");
		maxItemErrorMap.put("04", "Overflow error");
		maxItemErrorMap.put("05", "Order Branch Missing");
		maxItemErrorMap.put("06", "Item is suspended");
		maxItemErrorMap.put("07", "Item is non-standard");
		maxItemErrorMap.put("08", "Item Balance Record Missing");
		maxItemErrorMap.put("09", "Suspended Item Balance");
		maxItemErrorMap.put("10", "Requested Quantity has non-numberic data");
		maxItemErrorMap.put("11", "Requested UOM missing");
		maxItemErrorMap.put("12", "Requested UOM not in EWF003");
		maxItemErrorMap.put("14", "Order Multiple Error");

		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {
			e1.printStackTrace();
		}
	}

	public Document sendStockCheckResponse(YFSEnvironment env, Document inputXML) throws YFSUserExitException, RemoteException
	{
		log.info("Received Stock Check Web Service request");
		log.debug("The stock check input xml is: "+SCXmlUtil.getString(inputXML));

		Document pAndArequestInputDocument = null;

		boolean formatValidationFlag = true;
		boolean dataValidationFlag = true;
		
		Element stockCheckInputDocRoot = inputXML.getDocumentElement();
		log.info("stockCheckInputDocRoot : "+SCXmlUtil.getString(stockCheckInputDocRoot));
		try {
			formatValidationFlag = validateRequestXML(stockCheckInputDocRoot);
			if(!formatValidationFlag)
				return stockCheckOutputDocument;

			dataValidationFlag = validateRequestXMLData(env,stockCheckInputDocRoot);
			if(!dataValidationFlag)
				return stockCheckOutputDocument;
			
			initialization();			
		    validateXpedxItemNumbers(env);
		    if(inValidItemPositions.size() == stockCheckItemList.getLength()){
		    	stockCheckOutputDocument = createStockCheckOutput(env,stockCheckInputDocRoot);
		    	return stockCheckOutputDocument;
		    }
		    setQtysAndUOMsForValidItems(env);			
			
			if(isPandACallNeeded()){
				pAndArequestInputDocument = createPandARequestInputDocument(env,stockCheckInputDocRoot);
				log.info("The P&A request xml is: "+SCXmlUtil.getString(pAndArequestInputDocument)); //TODO debug log

				pAndAResponseDocument = api.executeFlow(env, "XPXPandAWebService", pAndArequestInputDocument);
				log.info("The P&A reponse output is: "+SCXmlUtil.getString(pAndAResponseDocument)); //TODO debug log

				if (pAndAResponseDocument!=null) {
					Element pAndAResponseDocRoot = pAndAResponseDocument.getDocumentElement();
					String headerStatusCode = SCXmlUtil.getXpathElement(pAndAResponseDocRoot,"./HeaderStatusCode").getTextContent();

					if (Integer.parseInt(headerStatusCode) == 0 ) {
						// load the uom descriptions for later lookups. we need only if PandA response success
						uomDescMap = getUOMList(env);
						stockCheckOutputDocument = createStockCheckOutput(env,stockCheckInputDocRoot);
					}
					else {
						String errorMsg = "Problem getting Pricing and Availability - HeaderStatusCode: " + headerStatusCode;
						String message = maxHeaderErrorMap.get(headerStatusCode);
						if (message != null) {
							errorMsg =  headerStatusCode + "-" + message;
						}
						stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,
								errorMessage_503 + " MAX: " + errorMsg);
					}
				}
				else {
					stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,errorMessage_503); //TODO if P&A returns null;
				}
			}else{
				stockCheckOutputDocument = createStockCheckOutput(env,stockCheckInputDocRoot);	
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,errorMessage_503);
		}
		log.info("Final stock check doc: "+SCXmlUtil.getString(stockCheckOutputDocument)); //TODO log debug
		log.info("Completed Stock Check Web Service request");
		return stockCheckOutputDocument;
	}

	public static Element importElement(Element parentEle,
			Element ele2beImported) {
		Element child = null;
		if (parentEle != null && ele2beImported != null) {
			child = (Element) parentEle.getOwnerDocument().importNode(
					ele2beImported, true);
			parentEle.appendChild(child);
		}
		return child;
	}


	private Document createStockCheckOutput(YFSEnvironment env, Element stockCheckInputDocRoot)
			throws YFSException, RemoteException, DOMException, YIFClientCreationException,NullPointerException, YFSException, Exception {

		String inventoryIndicator = null;

		// If init these null, have seen case where MAX unexpectedly returns only two warehouses which causes NPE later on twoDayQty
		String sameDayQty = "0";
		String nextDayQty = "0";
		String twoDayQty = "0";


		Map<Integer,Element> pAndAResponseMap= new HashMap<Integer,Element>(); 

		if(pAndAResponseDocument!=null && pAndAResponseDocument.getDocumentElement()!=null){
			Element pAndAResponseDocRoot = pAndAResponseDocument.getDocumentElement();
			Element aItemsFromPandA = (Element) pAndAResponseDocRoot.getElementsByTagName("Items").item(0);			

			if(aItemsFromPandA!=null)
			{
				NodeList itemListFromPandA = aItemsFromPandA.getElementsByTagName("Item");

				if(itemListFromPandA.getLength()>0)
				{
					//Items exist in P&A response

					for(int i=0; i<itemListFromPandA.getLength(); i++)
					{
						Element itemElementFromPandA = (Element) itemListFromPandA.item(i);
						pAndAResponseMap.put(Integer.parseInt(SCXmlUtil.getXpathElement(itemElementFromPandA,"./LineNumber").getTextContent()), itemElementFromPandA);
					}
				}
			} // end Items
		}

		Document stockCheckResponseDocument = YFCDocument.createDocument("xpedxStockCheckWSResponse").getDocument();
		Element stockCheckResponseDocRoot = stockCheckResponseDocument.getDocumentElement();

		/**
		 * Root Error Info----Gap here so left it at just element creation(Level 1 error codes)
		 */
		Element rootErrorInfo = stockCheckResponseDocument.createElement("RootErrorInfo");

		Element errorCode = stockCheckResponseDocument.createElement("ErrorCode");
		rootErrorInfo.appendChild(errorCode);

		Element errorMessage = stockCheckResponseDocument.createElement("ErrorMessage");
		rootErrorInfo.appendChild(errorMessage);

		stockCheckResponseDocRoot.appendChild(rootErrorInfo);

		/****************************************************************************/

		/**
		 * Sender Credentials
		 */
		Element senderCredentials = stockCheckResponseDocument.createElement("SenderCredentials");

		Element userEmailElement = stockCheckResponseDocument.createElement("UserEmail");
		userEmailElement.setTextContent(userEmail);
		senderCredentials.appendChild(userEmailElement);

		Element userPassword = stockCheckResponseDocument.createElement("UserPassword");
		userPassword.setTextContent(password);
		senderCredentials.appendChild(userPassword);

		stockCheckResponseDocRoot.appendChild(senderCredentials);

		/*****************************************************************************/

		/**
		 * eTradingPartnerID
		 */
		Element eTradingIDElement = stockCheckResponseDocument.createElement("eTradingPartnerID");
		eTradingIDElement.setTextContent(eTradingId);
		stockCheckResponseDocRoot.appendChild(eTradingIDElement);

		/*****************************************************************************/

		/**
		 * Customer Number
		 */
		Element customerNumber = stockCheckResponseDocument.createElement("CustomerNumber");
		customerNumber.setTextContent(sapParentAccountNo);
		stockCheckResponseDocRoot.appendChild(customerNumber);

		Element items = stockCheckResponseDocument.createElement("Items"); 




		for(int i=0; i<stockCheckItemList.getLength(); i++)	{
			Element requestedItemElement = (Element) stockCheckItemList.item(i);

			Element item = stockCheckResponseDocument.createElement("Item");

			Element indexID = stockCheckResponseDocument.createElement("IndexID");
			indexID.setTextContent(SCXmlUtil.getXpathElement(requestedItemElement,"./IndexID").getTextContent());		

			Element customerPartNumber = stockCheckResponseDocument.createElement("CustomerPartNumber");
			customerPartNumber.setTextContent(SCXmlUtil.getXpathElement(requestedItemElement,"./CustomerPartNumber").getTextContent());

			Element xpedxPartNumber = stockCheckResponseDocument.createElement("xpedxPartNumber");			
			Element quantity = stockCheckResponseDocument.createElement("Quantity");
			Element unitOfMeasure = stockCheckResponseDocument.createElement("UnitOfMeasure");

			Element itemLevelErrorCode = stockCheckResponseDocument.createElement("ErrorCode");
			Element itemLevelErrorMessage = stockCheckResponseDocument.createElement("ErrorMessage");

			Element category1 = stockCheckResponseDocument.createElement("Category1");
			Element category2 = stockCheckResponseDocument.createElement("Category2");
			Element category3 = stockCheckResponseDocument.createElement("Category3");
			Element category4 = stockCheckResponseDocument.createElement("Category4");

			Element itemDescription = stockCheckResponseDocument.createElement("ItemDescription");
			Element itemSellText = stockCheckResponseDocument.createElement("ItemSellText");

			Element sameDayDesc = stockCheckResponseDocument.createElement("SameDayDescription");
			Element sameDayQuantity = stockCheckResponseDocument.createElement("SameDayQuantity");
			Element nextDayDesc = stockCheckResponseDocument.createElement("NextDayDescription");
			Element nextDayQuantity = stockCheckResponseDocument.createElement("NextDayQuantity");
			Element twoDayDesc = stockCheckResponseDocument.createElement("TwoDayDescription");
			Element twoDayQuantity = stockCheckResponseDocument.createElement("TwoDayQuantity");
			Element availMessage = stockCheckResponseDocument.createElement("AvailabilityMessage");
			Element backOrderMessage = stockCheckResponseDocument.createElement("BackOrderMessage");

			Element orderMultipleElement = stockCheckResponseDocument.createElement("OrderMultiple");
			Element orderMultipleMessage = stockCheckResponseDocument.createElement("OrderMultipleMessage");
			Element totalPrice = stockCheckResponseDocument.createElement("TotalPrice");
			Element manufacturer = stockCheckResponseDocument.createElement("Manufacturer");
			Element manufacturerPartNo = stockCheckResponseDocument.createElement("ManufacturerPartNumber");
			Element itemStatus = stockCheckResponseDocument.createElement("ItemStatus");

			Element unitPrice1 = stockCheckResponseDocument.createElement("UnitPrice1");
			Element unitPrice2 = stockCheckResponseDocument.createElement("UnitPrice2");

			Element uomNodeList = stockCheckResponseDocument.createElement("UOMList");
			Element catalogAttributeList = stockCheckResponseDocument.createElement("CatalogAttributeList");



			if(inValidItemPositions.contains(i)){
				xpedxPartNumber.setTextContent(SCXmlUtil.getXpathElement(requestedItemElement,"./xpedxPartNumber").getTextContent());
				quantity.setTextContent(SCXmlUtil.getXpathElement(requestedItemElement,"./Quantity").getTextContent());
				unitOfMeasure.setTextContent(SCXmlUtil.getXpathElement(requestedItemElement,"./UOM").getTextContent());
				itemLevelErrorCode.setTextContent(ERROR_LEVEL_COMPLETE_FAILURE);
				itemLevelErrorMessage.setTextContent("1003 - The item is not stocked in the Ordering Warehouse. Please enter another Part Number.");

			}else{
				String legacyProductCode = requestedXpedxItemsMap.get(i);
				xpedxPartNumber.setTextContent(legacyProductCode);

				Element itemElementFromPandA = (Element) pAndAResponseMap.get(i);
				if(itemElementFromPandA!=null){
					quantity.setTextContent(SCXmlUtil.getXpathElement(itemElementFromPandA,"./RequestedQty").getTextContent());	
					String requestedUOM = (SCXmlUtil.getXpathElement(itemElementFromPandA,"./RequestedQtyUOM").getTextContent());
					String requestedUOMDescription = uomDescMap.get(requestedUOM);			
					unitOfMeasure.setTextContent(legacyToUOMMap.get(requestedUOM).replace(envtId+"_",""));
					String maxErrorCode = SCXmlUtil.getXpathElement(itemElementFromPandA,"./LineStatusCode").getTextContent();
					String respErrorCode = ERROR_LEVEL_COMPLETE_SUCCESS;
					String respErrorMessage = "";

					if (Integer.parseInt(maxErrorCode) != 0) {
						log.info("SCWS: MAX returned error on P&A for item " + legacyProductCode +" - "+ maxErrorCode); //TODO make debug
						respErrorCode = ERROR_LEVEL_COMPLETE_FAILURE;
						respErrorMessage = convertMaxErrorCode(maxErrorCode);
					}
					itemLevelErrorCode.setTextContent(respErrorCode);
					itemLevelErrorMessage.setTextContent(respErrorMessage);		

					// Going by the logic that the first element in the warehouse locations is the primary warehouse.
					// -> if warehouse info missing in P&A, skipping these - is that ok?
					Element wareHouseLocations = SCXmlUtil.getXpathElement(itemElementFromPandA, "./WarehouseLocationList");
					if (wareHouseLocations != null)
					{
						NodeList wareHouseLocationsList =  wareHouseLocations.getElementsByTagName("WarehouseLocation");

						if(wareHouseLocationsList != null && wareHouseLocationsList.getLength()>0)
						{
							for(int m=0; m<wareHouseLocationsList.getLength();m++)
							{
								Element wareHouseLocation = (Element) wareHouseLocationsList.item(m);

								if(m==0)
								{
									//Primary Warehouse					
									sameDayDesc.setTextContent(SCXmlUtil.getXpathElement(wareHouseLocation,"./Warehouse").getTextContent());			
									sameDayQty = SCXmlUtil.getXpathElement(wareHouseLocation,"./AvailableQty").getTextContent();
									sameDayQuantity.setTextContent(sameDayQty);								
								}

								if(m==1)
								{
									// Next Day warehouse
									// Hard coded to next day								
									nextDayDesc.setTextContent("Next Day");	
									nextDayQty = SCXmlUtil.getXpathElement(wareHouseLocation,"./AvailableQty").getTextContent();
									nextDayQuantity.setTextContent(nextDayQty);								
								}

								if(m==2)
								{
									//Two Day Warehouse
									//Hardcoded to 2+days								
									twoDayDesc.setTextContent("2+ Days");
									twoDayQty = SCXmlUtil.getXpathElement(wareHouseLocation,"./AvailableQty").getTextContent();
									twoDayQuantity.setTextContent(twoDayQty);								
								}
							}
						}

						//Logic for availability message					
						String availabilityMessage = getAvailabilityMessage(env,quantity.getTextContent(),sameDayQty,nextDayQty,twoDayQty);
						availMessage.setTextContent(availabilityMessage);			

						// Logic for back order message	
						if(Integer.parseInt(quantity.getTextContent()) > 0 &&
								(Integer.parseInt(quantity.getTextContent()) > (Integer.parseInt(sameDayQty)+Integer.parseInt(nextDayQty)+ Integer.parseInt(twoDayQty))))
						{
							int backOrderQty = Integer.parseInt(quantity.getTextContent()) - (Integer.parseInt(sameDayQty)+Integer.parseInt(nextDayQty)+ Integer.parseInt(twoDayQty));
							String backOrderedMessage = Integer.toString(backOrderQty)+""+unitOfMeasure.getTextContent()+""+"not currently available";
							backOrderMessage.setTextContent(backOrderedMessage);

						}

					}//end no warehouse info in P&A

					String orderMultipleQty = SCXmlUtil.getXpathElement(itemElementFromPandA,"./OrderMultipleQty").getTextContent();
					String orderMultipleUOM = SCXmlUtil.getXpathElement(itemElementFromPandA,"./OrderMultipleUOM").getTextContent();
					if(!YFCUtils.isVoid(orderMultipleQty) && Integer.parseInt(orderMultipleQty) > 0 && !YFCUtils.isVoid(orderMultipleUOM)){
						orderMultipleElement.setTextContent(orderMultipleQty);				
						String orderMultipleUOMDescription = getUomDesc(orderMultipleUOM, orderMultipleUOM.replace(envtId+"_",""));
						orderMultipleMessage.setTextContent("Must be ordered in units of "+orderMultipleQty+" "+orderMultipleUOMDescription);
					}

					// Logic for Total Price					
					totalPrice.setTextContent(SCXmlUtil.getXpathElement(itemElementFromPandA,"./ExtendedPrice").getTextContent());
					unitPrice1.setTextContent(SCXmlUtil.getXpathElement(itemElementFromPandA,"./UnitPricePerRequestedUOM").getTextContent() +"/"+requestedUOMDescription);			
					if(!SCXmlUtil.getXpathElement(itemElementFromPandA,"./UnitPricePerRequestedUOM").getTextContent().
							equals(SCXmlUtil.getXpathElement(itemElementFromPandA,"./UnitPricePerPricingUOM").getTextContent()))
					{
						String pricingUom = SCXmlUtil.getXpathElement(itemElementFromPandA,"./PricingUOM").getTextContent();
						String convertedPricingUom = legacyToUOMMap.get(pricingUom);
						String pricingUomDescription = getUomDesc(pricingUom, convertedPricingUom);
						unitPrice2.setTextContent(SCXmlUtil.getXpathElement(itemElementFromPandA,"./UnitPricePerPricingUOM").getTextContent() +"/"+pricingUomDescription);						
					}	
				}	else{
					if(inValidUOMPositionsForValidItems.contains(i)){
						quantity.setTextContent(SCXmlUtil.getXpathElement(requestedItemElement,"./Quantity").getTextContent());
						unitOfMeasure.setTextContent(SCXmlUtil.getXpathElement(requestedItemElement,"./UOM").getTextContent());
						itemLevelErrorCode.setTextContent(ERROR_LEVEL_COMPLETE_FAILURE);
						itemLevelErrorMessage.setTextContent("1004 - Invalid UOM value. Please enter valid UOM.");
					}
				}

				Element itemElement = validItemElementsMap.get(legacyProductCode);
				if(itemElement!=null){
					if (itemElement.getElementsByTagName("CategoryList").getLength() > 0)
					{
						Element categoryListElement = (Element) itemElement.getElementsByTagName("CategoryList").item(0);
						if(categoryListElement.getElementsByTagName("Category").getLength()>0)
						{
							Element categoryElement = (Element) categoryListElement.getElementsByTagName("Category").item(0);

							String[] cats = categoryElement.getAttribute("CategoryPath").split("/");

							if (cats.length > 4)
							{
								category1.setTextContent(getCategoryShortDescription(cats[2], organizationCode, env));
								category2.setTextContent(getCategoryShortDescription(cats[3], organizationCode, env));
								category3.setTextContent(getCategoryShortDescription(cats[4], organizationCode, env));
								// If no cat4, output "<Category4/>" (or skip?)
								//  Does "Paper Category" in fields count as cat4?
								String cat4 = "";
								if (cats.length > 5)
								{
									cat4 = getCategoryShortDescription(cats[5], organizationCode, env);
								}
								category4.setTextContent(cat4);
							}
						}
					}
					// Item description and Item Sell text				
					Element primaryInfoElement = (Element) itemElement.getElementsByTagName("PrimaryInformation").item(0);
					itemDescription.setTextContent(primaryInfoElement.getAttribute("ExtendedDescription"));				 
					Element itemExtnElement = (Element) itemElement.getElementsByTagName("Extn").item(0);
					itemSellText.setTextContent(itemExtnElement.getAttribute("ExtnSellText"));
					manufacturer.setTextContent(itemExtnElement.getAttribute("ExtnSupplierNameDisplay"));
					manufacturerPartNo.setTextContent(primaryInfoElement.getAttribute("ManufacturerItem"));
					inventoryIndicator = xpedxItemsInventoryMap.get(requestedXpedxItemsMap.get(i));
					//Setting error code as 1000 if the item is a mill item
					if(!YFCUtils.isVoid(inventoryIndicator))
					{
						if(inventoryIndicator.equalsIgnoreCase("M"))
						{
							itemLevelErrorCode.setTextContent(ERROR_LEVEL_COMPLETE_FAILURE);
							itemLevelErrorMessage.setTextContent("1000 - This item is currently not stocked in your primary warehouse. " +
									"However, we can source this product from the manufacturer. Please contact your CSR or eBusiness " +
									"Customer Support at 1- 877-269-1784.");
						}
					}
					itemStatus.setTextContent(inventoryIndicator);					
					createUOMListForStockCheckResponse(legacyProductCode,uomNodeList,stockCheckResponseDocument);
					createCatalogAttributeListForStockCheckResponse(itemElement,catalogAttributeList,stockCheckResponseDocument);
				}
			}

			item.appendChild(indexID);
			item.appendChild(xpedxPartNumber);
			item.appendChild(customerPartNumber);
			item.appendChild(quantity);
			item.appendChild(unitOfMeasure);
			item.appendChild(itemLevelErrorCode);
			item.appendChild(itemLevelErrorMessage);
			item.appendChild(category1);
			item.appendChild(category2);
			item.appendChild(category3);
			item.appendChild(category4);
			item.appendChild(itemDescription);
			item.appendChild(itemSellText);
			item.appendChild(sameDayDesc);
			item.appendChild(sameDayQuantity);
			item.appendChild(nextDayDesc);
			item.appendChild(nextDayQuantity);
			item.appendChild(twoDayDesc);
			item.appendChild(twoDayQuantity);
			item.appendChild(availMessage);
			item.appendChild(backOrderMessage);
			item.appendChild(orderMultipleElement);
			item.appendChild(orderMultipleMessage);
			item.appendChild(totalPrice);
			item.appendChild(manufacturer);
			item.appendChild(manufacturerPartNo);
			item.appendChild(itemStatus);
			item.appendChild(unitPrice1);
			item.appendChild(unitPrice2);
			item.appendChild(uomNodeList);
			item.appendChild(catalogAttributeList);

			items.appendChild(item);
		}


		stockCheckResponseDocRoot.appendChild(items);

		NodeList itemListInStockCheckResponse = items.getElementsByTagName("Item");
		int errorCount = 0;
		int successCount = 0;
		int numItemsInResponse = itemListInStockCheckResponse.getLength();

		for (int i=0; i<numItemsInResponse;i++)
		{
			Element itemElement = (Element) itemListInStockCheckResponse.item(i);

			String errorCodeValue = SCXmlUtil.getXpathElement(itemElement, "./ErrorCode").getTextContent();

			if (errorCodeValue!=null && errorCodeValue.trim().length()>0)
			{
				if (errorCodeValue.equalsIgnoreCase(ERROR_LEVEL_COMPLETE_FAILURE))
				{
					errorCount ++;
				}
				if (errorCodeValue.equalsIgnoreCase(ERROR_LEVEL_COMPLETE_SUCCESS))
				{
					successCount ++;
				}
			}
		}
		if (errorCount > 0) {
			log.info("SCWS response has " + errorCount + " item failures (out of " + numItemsInResponse + " items)");
		}

		if (errorCount == numItemsInResponse)
		{
			errorCode.setTextContent(ERROR_LEVEL_COMPLETE_FAILURE);
			errorMessage.setTextContent("1011 - One or more Items have not completely succeeded. " +
					"Please check the item level error for more details.");
		}
		else if (errorCount > 0 && errorCount != numItemsInResponse)
		{
			errorCode.setTextContent(ERROR_LEVEL_PARTIAL_FAILURE);
			errorMessage.setTextContent("1011 - One or more Items have not completely succeeded. " +
					"Please check the item level error for more details.");
		}
		else if (successCount == numItemsInResponse)
		{
			errorCode.setTextContent(ERROR_LEVEL_COMPLETE_SUCCESS);
		}

		return stockCheckResponseDocument;
	}

	private String convertMaxErrorCode(String maxErrorCode) {

		String message = maxItemErrorMap.get(maxErrorCode);

		if (message != null) {
			return maxErrorCode + "-" + message;
		}
		return "Problem getting Pricing and Availability - MAX Error Code: " + maxErrorCode;
	}

	/**
	 * Get UOM description using previously loaded info
	 * @param legacyUom - uom to get description for (e.g. M_SHT)
	 * @param customerUom - value to be used in if can't get description
	 */
	private String getUomDesc(String legacyUom, String customerUom) {
		String uomDesc = uomDescMap.get(legacyUom);
		if (uomDesc==null || uomDesc.equals("")) {
			uomDesc = customerUom;
		}
		return uomDesc;
	}




	private String getAvailabilityMessage(YFSEnvironment env, String reqQty, String sameDayQty, String nextDayQty, String twoDayQty)
	{
		String availabilityMessage = null;

		if(Integer.parseInt(reqQty) <= Integer.parseInt(sameDayQty))
		{
			availabilityMessage = "Ready To Ship";
		}
		else if((Integer.parseInt(reqQty) > Integer.parseInt(sameDayQty)) &&
				   (Integer.parseInt(reqQty) <= (Integer.parseInt(sameDayQty)+Integer.parseInt(nextDayQty))))
		{
	             availabilityMessage = "Ready To Ship Next Day";
		}
		else if((Integer.parseInt(reqQty) > (Integer.parseInt(sameDayQty)+Integer.parseInt(nextDayQty)))&&
				(Integer.parseInt(reqQty) <= (Integer.parseInt(sameDayQty)+Integer.parseInt(nextDayQty)+ Integer.parseInt(twoDayQty))))
		{
	             availabilityMessage = "Ready To Ship Next Day";
		}
		else if((Integer.parseInt(sameDayQty)+Integer.parseInt(nextDayQty)+ Integer.parseInt(twoDayQty))==0)
		{
			     availabilityMessage = "Not Available";
		}
		else
		{
			availabilityMessage = "Partial Quantity Available";
		}

		return availabilityMessage;
	}


	private Document createPandARequestInputDocument(YFSEnvironment env, Element stockCheckInputDocRoot) throws Exception
	{
		Document pAndARequestInputDoc = YFCDocument.createDocument("PriceAndAvailability").getDocument();
		Element pAndARequestInputDocRoot = pAndARequestInputDoc.getDocumentElement();

		Element aItems = pAndARequestInputDoc.createElement("Items");
		for(int i=0; i<stockCheckItemList.getLength(); i++)
		{

			if(!inValidItemPositions.contains(i) && !inValidUOMPositionsForValidItems.contains(i)){

				Element aItem = pAndARequestInputDoc.createElement("Item");
				Element sLineNumber = pAndARequestInputDoc.createElement("LineNumber");
				sLineNumber.setTextContent(""+i);

				Element sLegacyProductCode = pAndARequestInputDoc.createElement("LegacyProductCode");
				sLegacyProductCode.setTextContent(requestedXpedxItemsMap.get(i));

				Element sRequestedQtyUOM = pAndARequestInputDoc.createElement("RequestedQtyUOM");
				Element sRequestedQty = pAndARequestInputDoc.createElement("RequestedQty");
				sRequestedQty.setTextContent(qtysForValidPositions.get(i)); 
				sRequestedQtyUOM.setTextContent(uomsForValidPositions.get(i));

				aItem.appendChild(sLineNumber);
				aItem.appendChild(sLegacyProductCode);
				aItem.appendChild(sRequestedQtyUOM);
				aItem.appendChild(sRequestedQty);

				aItems.appendChild(aItem);
			}
		}
		// Creating the P&A request xml with Header level attributes
		Element sSourceIndicator = pAndARequestInputDoc.createElement("SourceIndicator");
		sSourceIndicator.setTextContent(B2BSourceIndicator);

		Element sEnvironmentId = pAndARequestInputDoc.createElement("EnvironmentId");
		sEnvironmentId.setTextContent(envtId);

		Element sCustomerEnvironmentId = pAndARequestInputDoc.createElement("CustomerEnvironmentId");
		sCustomerEnvironmentId.setTextContent(customerEnvtId);

		Element sCompany = pAndARequestInputDoc.createElement("Company");
		sCompany.setTextContent(companyCode);

		Element sCustomerBranch = pAndARequestInputDoc.createElement("CustomerBranch");
		sCustomerBranch.setTextContent(shipFromBranch);

		Element sCustomerNumber = pAndARequestInputDoc.createElement("CustomerNumber");
		sCustomerNumber.setTextContent(legacyCustomerNumber);

		Element sShipToSuffix = pAndARequestInputDoc.createElement("ShipToSuffix");
		sShipToSuffix.setTextContent(shipToSuffix);

		Element sOrderBranch = pAndARequestInputDoc.createElement("OrderBranch");
		sOrderBranch.setTextContent(customerOrderBranch);

		pAndARequestInputDocRoot.appendChild(sSourceIndicator);
		pAndARequestInputDocRoot.appendChild(sEnvironmentId);
		pAndARequestInputDocRoot.appendChild(sCustomerEnvironmentId);
		pAndARequestInputDocRoot.appendChild(sCompany);
		pAndARequestInputDocRoot.appendChild(sCustomerBranch);
		pAndARequestInputDocRoot.appendChild(sCustomerNumber);
		pAndARequestInputDocRoot.appendChild(sShipToSuffix);
		pAndARequestInputDocRoot.appendChild(sOrderBranch);

		pAndARequestInputDocRoot.appendChild(aItems);
		return pAndARequestInputDoc;
	}

	/**
	 * Get the short desc of the category
	 *
	 * @param categoryID from category path (e.g. "300057")
	 * @param organizationCode is this always "xpedx"?
	 */
	private String getCategoryShortDescription(String categoryID, String organizationCode, YFSEnvironment env) throws Exception
	{
		//TODO cache the categories??

		Document inputDoc = YFCDocument.createDocument("Category").getDocument();
		inputDoc.getDocumentElement().setAttribute("CategoryID", categoryID);
		inputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);

		env.setApiTemplate("getCategoryList", getCategoryListTemplate);
		Document outputDoc = api.invoke(env, "getCategoryList", inputDoc);
		env.clearApiTemplate("getCategoryList");

		NodeList outputList = outputDoc.getElementsByTagName("Category");
		String desc = "";
		if(outputList.getLength() > 0)
		{
			Element catelem = (Element) outputList.item(0);
			desc = catelem.getAttribute("ShortDescription");
		}

		return desc;
	}

	// load all the UOM descriptions from the server for lookups
	// (copied from XPXPendingApprovalOrders.java - maybe put in shared loc)
	private Map<String,String> getUOMList(YFSEnvironment env){
		Map<String,String> UOMDesriptionMap = new HashMap<String,String>();
		try
		{
			Document getItemUomMasterListInputDoc = YFCDocument.createDocument("ItemUOMMaster").getDocument();
			getItemUomMasterListInputDoc.getDocumentElement().setAttribute("UnitOfMeasure","" );
			getItemUomMasterListInputDoc.getDocumentElement().setAttribute("OrganizationCode", "");

			env.setApiTemplate("getItemUOMMasterList", getItemUomMasterListTemplate);
			Document getItemUomMasterListOutputDoc = api.invoke(env, "getItemUOMMasterList", getItemUomMasterListInputDoc);
			env.clearApiTemplate("getItemUOMMasterList");

			NodeList itemUOMMasterList = getItemUomMasterListOutputDoc.getDocumentElement().getElementsByTagName("ItemUOMMaster");
			if (itemUOMMasterList != null)
			{
				int itemUOMLength = itemUOMMasterList.getLength();
				if(itemUOMLength > 0)
				{
					for(int i=0; i < itemUOMLength ;i++)
					{
						String itemUomDescriptionMaster ="";
						String itemUomMaster = "";
						Element itemUomMasterElement = (Element) itemUOMMasterList.item(i);
						if (itemUomMasterElement.hasAttribute("Description"))
						{
							itemUomDescriptionMaster = itemUomMasterElement.getAttribute("Description");
							itemUomMaster = itemUomMasterElement.getAttribute("UnitOfMeasure");
						}
						else
						{
							itemUomDescriptionMaster = "";
						}
						UOMDesriptionMap.put(itemUomMaster, itemUomDescriptionMaster);
					}
				}
			}
		}
		catch(Exception e)
		{
			log.error("getUOMList: Error while getting UOM Descriptions"+e.getMessage());
		}
		return UOMDesriptionMap;
	}

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// Auto-generated method stub
	}

	/**
	 * All the input XML format validations
	 * @param stockCheckInputDocRoot
	 * @return
	 */
	private boolean validateRequestXML(Element stockCheckInputDocRoot){
		Element userEmeialElement = SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./SenderCredentials/UserEmail");
		if(userEmeialElement==null || YFCUtils.isVoid(userEmeialElement.getTextContent())){
			stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,errorMessage_105);
			return false;
		}
		Element pwdElement = SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./SenderCredentials/UserPassword");
		if(pwdElement==null || YFCUtils.isVoid(pwdElement.getTextContent())){
			stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,errorMessage_105);
			return false;
		}
		Element eTradingPartnerIDElement = SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./eTradingPartnerID");
		if(eTradingPartnerIDElement==null || YFCUtils.isVoid(eTradingPartnerIDElement.getTextContent())){
			stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,errorMessage_105);
			return false;
		}
		Element stockCheckItems = SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./Items");
		if(stockCheckItems==null){
			stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,errorMessage_105);
			return false;
		}
		stockCheckItemList = stockCheckItems.getElementsByTagName("Item");
		if(stockCheckItemList == null || stockCheckItemList.getLength()==0){
			stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,errorMessage_105);
			return false;
		}
		if(stockCheckItemList.getLength()>200){
			stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,errorMessage_104);
			return false;
		}
		return true;
	}

	/**
	 * All the input XML data validations
	 * @param env
	 * @param stockCheckInputDocRoot
	 * @return
	 * @throws RemoteException
	 */
	private boolean validateRequestXMLData(YFSEnvironment env,Element stockCheckInputDocRoot) throws RemoteException{

		userEmail= SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./SenderCredentials/UserEmail").getTextContent();
		password= SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./SenderCredentials/UserPassword").getTextContent();
		eTradingId= SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./eTradingPartnerID").getTextContent();
		Document loginInputDocument = YFCDocument.createDocument("Login").getDocument();
		loginInputDocument.getDocumentElement().setAttribute("LoginID", userEmail);
		loginInputDocument.getDocumentElement().setAttribute("Password",password);

		try
		{

			log.debug("The input to Login API is: "+SCXmlUtil.getString(loginInputDocument));
			Document loginOutputDocument = api.invoke(env, "login", loginInputDocument);
			log.info("The output after Login API is: "+SCXmlUtil.getString(loginOutputDocument));
			masterCustomerId = loginOutputDocument.getDocumentElement().getAttribute("OrganizationCode");
		}
		catch (YFSException e)
		{
			stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,errorMessage_100);
			return false;
		}
		masterCustomerDetailsOutputDoc = getMasterCustomerDetailsOutput(env);
		Element masterCustomerDetailsElement =  masterCustomerDetailsOutputDoc.getDocumentElement();
		Element customerContact = (Element)masterCustomerDetailsElement.getElementsByTagName("CustomerContactList").item(0);
		Element customerContactExtn = (Element)customerContact.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

		if(!"Y".equalsIgnoreCase(customerContactExtn.getAttribute("ExtnStockCheckWS")))
		{
			stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,errorMessage_101);
			return false;

		}
		String rootCustomerKeyForShipTos = masterCustomerDetailsElement.getAttribute("CustomerKey");
		Element masterCustomerExtnElement = (Element) masterCustomerDetailsElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

		if(!"Y".equalsIgnoreCase(masterCustomerExtnElement.getAttribute("ExtnStockCheckOption")))		{
			stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,errorMessage_102);
			return false;
		}
		shipToCustomerListOutputDoc = getShipToCustomerListDocument(env,rootCustomerKeyForShipTos);

		if(shipToCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).getLength()==0)
		{
			stockCheckOutputDocument = createErrorDocumentForCompleteFailure(stockCheckInputDocRoot,errorMessage_103);
			return false;
		}

		return true;
	}

	/**
	 * This method will get the ShipToList based on eTradingId, suffix type and root customer key. Always only one ship-to on this list.
	 * @param env
	 * @param rootCustomerKey
	 * @return
	 * @throws RemoteException
	 */
	private Document getShipToCustomerListDocument(YFSEnvironment env, String rootCustomerKey) throws RemoteException
	{
		Document getShipToCustomerListOutputDoc = null;

		Document getCustomerListInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER).getDocument();
		getCustomerListInputDoc.getDocumentElement().setAttribute("RootCustomerKey", rootCustomerKey);
		Element customerExtnElement = getCustomerListInputDoc.createElement(XPXLiterals.E_EXTN);
		customerExtnElement.setAttribute("ExtnETradingID", eTradingId);
		customerExtnElement.setAttribute("ExtnSuffixType", "S");
		getCustomerListInputDoc.getDocumentElement().appendChild(customerExtnElement);
		env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
		getShipToCustomerListOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc);
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		log.info("Ship-To Customer List Output: "+SCXmlUtil.getString(getShipToCustomerListOutputDoc)); //TODO log debug
		return getShipToCustomerListOutputDoc;
	}

	/**
	 * This method will get the master customer info and user info by invoking getCustomerDetails API.
	 * @param env
	 * @return
	 * @throws RemoteException
	 */
	private Document getMasterCustomerDetailsOutput(YFSEnvironment env) throws RemoteException{

		YFCDocument customerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
		customerDetailsInputDoc.getDocumentElement().setAttribute("CustomerID", masterCustomerId);
		String organizationCode="xpedx";
		if(masterCustomerId.contains("SAAL")){
			organizationCode= "Saalfeld";
		}
		customerDetailsInputDoc.getDocumentElement().setAttribute("OrganizationCode", organizationCode);
		YFCElement customerContactElement = customerDetailsInputDoc.createElement("CustomerContact");
		customerContactElement.setAttribute("CustomerContactID", userEmail);
		customerContactElement.setAttribute("UserID", userEmail);
		customerDetailsInputDoc.getDocumentElement().appendChild(customerContactElement);

		String customerDetailsTemplate =   "<Customer  CustomerID='' CustomerKey='' CustomerType=''  OrganizationCode=''>"
				+ "<Extn ExtnStockCheckOption=''/>"
				+ "<CustomerContactList>"
				+ "<CustomerContact>"
				+	"<Extn ExtnStockCheckWS=''/>"
				+ "</CustomerContact>"
				+ "</CustomerContactList>"
				+" </Customer>";

		env.setApiTemplate("getCustomerDetails", SCXmlUtil.createFromString(customerDetailsTemplate));
		Document customerDetailsOutputDoc = api.invoke(env, "getCustomerDetails", customerDetailsInputDoc.getDocument());
		env.clearApiTemplate("getCustomerDetails");customerDetailsOutputDoc.getDocumentElement();
		log.info("Master Customer Details Output: "+SCXmlUtil.getString(customerDetailsOutputDoc)); //TODO log debug
		return customerDetailsOutputDoc;
	}

	/**
	 * This method will create complete failure document with error message.
	 * @param stockCheckInputDocRoot
	 * @param errorMessage
	 * @return
	 */
	private Document createErrorDocumentForCompleteFailure(Element stockCheckInputDocRoot,String errorMessage)
	{
		Document stockCheckErrorDoc = YFCDocument.createDocument("xpedxStockCheckWSResponse").getDocument();

		Element rootErrorInfo = stockCheckErrorDoc.createElement("RootErrorInfo");


		Element errorCodeElement = stockCheckErrorDoc.createElement("ErrorCode");
		errorCodeElement.setTextContent(ERROR_LEVEL_COMPLETE_FAILURE);
		rootErrorInfo.appendChild(errorCodeElement);

		Element errorMessageElement = stockCheckErrorDoc.createElement("ErrorMessage");
		errorMessageElement.setTextContent(errorMessage);
		rootErrorInfo.appendChild(errorMessageElement);

		Element senderCredentials = stockCheckErrorDoc.createElement("SenderCredentials");

		Element userEmailElement = stockCheckErrorDoc.createElement("UserEmail");
		if(!YFCUtils.isVoid(userEmail)){
			userEmailElement.setTextContent(userEmail);
		}
		senderCredentials.appendChild(userEmailElement);
		Element userPassword = stockCheckErrorDoc.createElement("UserPassword");
		if(!YFCUtils.isVoid(password)){
			userPassword.setTextContent(password);
		}
		senderCredentials.appendChild(userPassword);

		Element eTradingIDElement = stockCheckErrorDoc.createElement("eTradingPartnerID");
		if(!YFCUtils.isVoid(eTradingId)){
			eTradingIDElement.setTextContent(eTradingId);
		}

		stockCheckErrorDoc.getDocumentElement().appendChild(rootErrorInfo);
		stockCheckErrorDoc.getDocumentElement().appendChild(senderCredentials);
		stockCheckErrorDoc.getDocumentElement().appendChild(eTradingIDElement);
		if(stockCheckItemList != null && stockCheckItemList.getLength()>0){
			Element itemsElementFromInput = SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./Items");
			importElement(stockCheckErrorDoc.getDocumentElement(),itemsElementFromInput);
		}
		return stockCheckErrorDoc;
	}
	
	/**
	 * get and validate XPEDX Item numbers
	 * @param env
	 * @throws YFSException
	 * @throws RemoteException
	 */
	private void validateXpedxItemNumbers(YFSEnvironment env) throws YFSException, RemoteException {
		requestedXpedxItemsMap = new HashMap<Integer,String>(stockCheckItemList.getLength());
		 Set<String> customerPartNoSet = new HashSet<String>(stockCheckItemList.getLength());
		 inValidItemPositions = new TreeSet<Integer>();
		 Set<Integer> customerPartNoPositions = new TreeSet<Integer>();
		 Map<String,String> cpToXpedx = null;
		 
		for(int i=0; i<stockCheckItemList.getLength(); i++)
		{
			Element itemElement = (Element) stockCheckItemList.item(i);
			Element xpedxItemElement = SCXmlUtil.getXpathElement(itemElement,"./xpedxPartNumber");
			if(xpedxItemElement!=null && !YFCUtils.isVoid(xpedxItemElement.getTextContent())){ // XPEDX number
				requestedXpedxItemsMap.put(i,xpedxItemElement.getTextContent().trim());
			}else{ // no XPEDX number
				Element customerPartNumberElement = SCXmlUtil.getXpathElement(itemElement,"./CustomerPartNumber");
				if(customerPartNumberElement!=null && !YFCUtils.isVoid(customerPartNumberElement.getTextContent())){ // Customer Part number	
					customerPartNoSet.add(customerPartNumberElement.getTextContent().trim());
					customerPartNoPositions.add(i);
				}else{ // no XPEDX number and  Customer Part number
					inValidItemPositions.add(i);	
				}
			}
		}
		
			if(customerPartNoSet!=null && customerPartNoSet.size() > 0){ 
				cpToXpedx = getItemNumbersForCustomerPartNumbers(env,customerPartNoSet); //for getting XPEDX numbers for corresponding Customer Part numbers
		
			}
		
			for (Integer customerPartNoPosition : customerPartNoPositions) {
				Element itemElement = (Element) stockCheckItemList.item(customerPartNoPosition);
				Element customerPartNumberElement = SCXmlUtil.getXpathElement(itemElement,"./CustomerPartNumber");
				if(cpToXpedx!= null && !YFCUtils.isVoid(cpToXpedx.get(customerPartNumberElement.getTextContent().trim()))){ //found XPEDX number for corresponding Customer Part number
					requestedXpedxItemsMap.put(customerPartNoPosition, cpToXpedx.get(customerPartNumberElement.getTextContent().trim()));
				}else{ //not found XPEDX number for corresponding Customer Part number
					inValidItemPositions.add(customerPartNoPosition);
				}
			}
		
			validItemElementsMap = getItemsDetails(env);
	}
	
	/**
	 * Get XPEDX item number for corresponding Customer Item numbers
	 * @param env
	 * @param customerPartNumbers
	 * @throws YFSException
	 * @throws RemoteException
	 */
	private Map<String,String> getItemNumbersForCustomerPartNumbers(YFSEnvironment env, Set<String> customerPartNumbers) throws YFSException, RemoteException
	{
		Document xpedxItemCustXRefInputDoc = YFCDocument.createDocument("XPXItemcustXref").getDocument();
		xpedxItemCustXRefInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_COMPANY_CODE, companyCode);
		xpedxItemCustXRefInputDoc.getDocumentElement().setAttribute("EnvironmentCode", envtId);
		xpedxItemCustXRefInputDoc.getDocumentElement().setAttribute("CustomerNumber",legacyCustomerNumber);
		Element complexQueryElement = SCXmlUtil.createChild(xpedxItemCustXRefInputDoc.getDocumentElement(), "ComplexQuery");
		Element orElement = SCXmlUtil.createChild(complexQueryElement, "Or");	
		for (String customerPartNumber : customerPartNumbers) {
			Element expElement = SCXmlUtil.createChild(orElement, "Exp");
			expElement.setAttribute("Name", "CustomerItemNumber");
			expElement.setAttribute("Value", customerPartNumber);
		}
		Document xpedxItemCustXRefOutputDoc = api.executeFlow(env, XPXLiterals.GET_XREF_LIST, xpedxItemCustXRefInputDoc);
		Element xpedxItemCustXRefOutputElement = xpedxItemCustXRefOutputDoc.getDocumentElement();	
		Map <String,String>xpedxItemIdsForCustomerItems = new LinkedHashMap<String, String>(customerPartNumbers.size());
		ArrayList<Element> xpedxItemCustXRefElements = SCXmlUtil.getElements(xpedxItemCustXRefOutputElement, XPXLiterals.E_XPX_ITEM_CUST_XREF);
		for (Element xpedxItemCustXRefElement : xpedxItemCustXRefElements) {
			if (xpedxItemCustXRefElement != null) {
				xpedxItemIdsForCustomerItems.put(xpedxItemCustXRefElement.getAttribute("CustomerItemNumber"), xpedxItemCustXRefElement.getAttribute("LegacyItemNumber"));
			}			
		}
		return xpedxItemIdsForCustomerItems;
	}
	
	/**
	 * get the Qtys for P and A.
	 * @param env
	 * @throws YFSException
	 * @throws RemoteException
	 */
	private void setQtysAndUOMsForValidItems(YFSEnvironment env) throws YFSException, RemoteException {
		qtysForValidPositions = new HashMap<Integer,String>(stockCheckItemList.getLength()); // this map will be used for P&A requested qty
		xpedxItemsOMQtyMap = getOMQtyForXpedxItems(env, validItemElementsMap.keySet());	//getting the order multiples for valid items.
		
		uomListForValidItemsMap = getXPXUomListForValidItems(env);//get the UOM List for all Items
		getUOMAndLegacyUOMsForValidXPXItems(env);
		
		inValidUOMPositionsForValidItems = new TreeSet<Integer>();
		uomsForValidPositions = new HashMap<Integer,String>();
		for(int i=0; i<stockCheckItemList.getLength(); i++)	{
			if(!inValidItemPositions.contains(i)){
				Element itemElement = (Element) stockCheckItemList.item(i);
				Element qtyElement	= SCXmlUtil.getXpathElement(itemElement,"./Quantity");
				Element uomElement	= SCXmlUtil.getXpathElement(itemElement,"./UOM");
				if (qtyElement==null || YFCUtils.isVoid(qtyElement.getTextContent()) 
						|| uomElement == null 
						|| YFCUtils.isVoid(uomElement.getTextContent())
						|| !isPositivAndNonZeroInteger(qtyElement.getTextContent().trim())) 
				{
					String omQty = xpedxItemsOMQtyMap.get(requestedXpedxItemsMap.get(i));
					qtysForValidPositions.put(i,!YFCUtils.isVoid(omQty) ? omQty:"1");
					uomsForValidPositions.put(i,validItemsUOMMap.get(requestedXpedxItemsMap.get(i)));
				}else{
					qtysForValidPositions.put(i,qtyElement.getTextContent().trim());
					
					String uom = SCXmlUtil.getXpathElement(itemElement,"./UOM").getTextContent().trim();
					String legacyUOM = uomToLegacyMap.get(envtId+"_"+uom);
					if(legacyUOM!=null && isValidUOMForItem(requestedXpedxItemsMap.get(i),legacyUOM)){
					 uomsForValidPositions.put(i,legacyUOM);
					}
					else{
						inValidUOMPositionsForValidItems.add(i);
					}
				}
			}	
		}
	}
	
	/**
	 * get the Order multiple using Complex Query and Inventory indicator
	 * @param env
	 * @param itemsForOMQtySet
	 * @return
	 * @throws YFSException
	 * @throws RemoteException
	 */
	private Map<String,String> getOMQtyForXpedxItems(YFSEnvironment env,Set<String> itemsForOMQtySet) throws YFSException, RemoteException{
		Document itemBranchDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_XPX_ITEM_EXTN).getDocument();
		itemBranchDetailsInputDoc.getDocumentElement().setAttribute("EnvironmentID",envtId);
		itemBranchDetailsInputDoc.getDocumentElement().setAttribute("CompanyCode",companyCode);
		itemBranchDetailsInputDoc.getDocumentElement().setAttribute("XPXDivision",shipFromBranch);	
		Element complexQueryElement = SCXmlUtil.createChild(itemBranchDetailsInputDoc.getDocumentElement(), "ComplexQuery");
		Element orElement = SCXmlUtil.createChild(complexQueryElement, "Or");	
		for (String itemId : itemsForOMQtySet) {
			Element expElement = SCXmlUtil.createChild(orElement, "Exp");
			expElement.setAttribute("Name", "ItemID");
			expElement.setAttribute("Value", itemId);
		}
		log.info("itemBranchDetailsInputDoc: "+SCXmlUtil.getString(itemBranchDetailsInputDoc.getDocumentElement()));
		String  itemBranchDetailsTemplate =   "<XPXItemExtnList>"
				+ "<XPXItemExtn InventoryIndicator ='' ItemID ='' OrderMultiple ='' />"
				+" </XPXItemExtnList>";
		
		env.setApiTemplate(XPXLiterals.GET_XPX_ITEM_BRANCH_LIST_SERVICE, SCXmlUtil.createFromString(itemBranchDetailsTemplate));
		Document itemBranchDetailsOutputDoc = api.executeFlow(env,XPXLiterals.GET_XPX_ITEM_BRANCH_LIST_SERVICE, itemBranchDetailsInputDoc);
		env.clearApiTemplate(XPXLiterals.GET_XPX_ITEM_BRANCH_LIST_SERVICE); 
		
		Element itemBranchDetailsOutputElement = itemBranchDetailsOutputDoc.getDocumentElement();		 
		ArrayList<Element> XPXItemExtnElements = SCXmlUtil.getElements(itemBranchDetailsOutputElement, XPXLiterals.E_XPX_ITEM_EXTN);
		Map<String,String> omQtyMap = new HashMap<String,String>(XPXItemExtnElements.size());
		xpedxItemsInventoryMap = new HashMap<String,String>(XPXItemExtnElements.size());	
		for (Element XPXItemExtnElement : XPXItemExtnElements) {
			if (XPXItemExtnElement != null) {
				omQtyMap.put(XPXItemExtnElement.getAttribute("ItemID"), !YFCUtils.isVoid(XPXItemExtnElement.getAttribute("OrderMultiple")) ? XPXItemExtnElement.getAttribute("OrderMultiple"):"1");
				xpedxItemsInventoryMap.put(XPXItemExtnElement.getAttribute("ItemID"),XPXItemExtnElement.getAttribute(XPXLiterals.A_INVENTORY_INDICATOR));
			}			
		}
		return omQtyMap;
	}

	/**
	 * get the Items details
	 * @param env
	 * @return
	 * @throws YFSException
	 * @throws RemoteException
	 */
	private Map<String,Element> getItemsDetails(YFSEnvironment env) throws YFSException, RemoteException	{	
		Document xpedxItemCustXRefInputDoc = YFCDocument.createDocument("Item").getDocument();
		xpedxItemCustXRefInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_COMPANY_CODE, companyCode);
		xpedxItemCustXRefInputDoc.getDocumentElement().setAttribute("EnvironmentCode", envtId);
		xpedxItemCustXRefInputDoc.getDocumentElement().setAttribute("CustomerNumber",legacyCustomerNumber);
		Element complexQueryElement = SCXmlUtil.createChild(xpedxItemCustXRefInputDoc.getDocumentElement(), "ComplexQuery");
		Element orElement = SCXmlUtil.createChild(complexQueryElement, "Or");
		for (Integer position : requestedXpedxItemsMap.keySet()) {
			Element expElement = SCXmlUtil.createChild(orElement, "Exp");
			expElement.setAttribute("Name", "ItemID");
			expElement.setAttribute("Value", requestedXpedxItemsMap.get(position));			
		}
		
		env.setApiTemplate(XPXLiterals.GET_ITEM_LIST_API, getItemListTemplate);
		Document xpedxItemCustXRefOutputDoc = api.invoke(env, XPXLiterals.GET_ITEM_LIST_API, xpedxItemCustXRefInputDoc);
		env.clearApiTemplate(XPXLiterals.GET_ITEM_LIST_API); 
		
		Element xpedxItemCustXRefOutputElement = xpedxItemCustXRefOutputDoc.getDocumentElement();
		ArrayList<Element> xpedxItemElements = SCXmlUtil.getElements(xpedxItemCustXRefOutputElement, XPXLiterals.E_ITEM);
		validItemsUOMMap  = new HashMap<String,String>(xpedxItemElements.size());
		Map<String,Element> validXpedxItemElements = new HashMap<String,Element>(xpedxItemElements.size());
		for (Element xpedxItemElement : xpedxItemElements) {
			if (xpedxItemElement != null) {
				validItemsUOMMap.put(xpedxItemElement.getAttribute("ItemID"), xpedxItemElement.getAttribute("UnitOfMeasure"));
				validXpedxItemElements.put(xpedxItemElement.getAttribute("ItemID"), xpedxItemElement);
			}

		}
		/* for trying to find invalid items */
		if( validXpedxItemElements.size()>=0){
			for (Integer position : requestedXpedxItemsMap.keySet()) {
				String itemId	= requestedXpedxItemsMap.get(position);
				if(!validXpedxItemElements.keySet().contains(itemId)) inValidItemPositions.add(position);		
			}
		}
		return validXpedxItemElements;
	}
	
/**
 * get the UOM List for all Items. For example: 2001020 has 3 UOMS( Carton, Package, Sheet)
 * @param env
 * @throws YFSException
 * @throws RemoteException
 */
	private Map<String,Element> getXPXUomListForValidItems(YFSEnvironment env) throws YFSException, RemoteException
	{
		Document xpxUomListForValidItemInputDoc = YFCDocument.createDocument("UOM").getDocument();
		xpxUomListForValidItemInputDoc.getDocumentElement().setAttribute("CustomerID", customerID);
		xpxUomListForValidItemInputDoc.getDocumentElement().setAttribute("OrganizationCode", organizationCode);
		Element complexQueryElement = SCXmlUtil.createChild(xpxUomListForValidItemInputDoc.getDocumentElement(), "ComplexQuery");
		Element orElement = SCXmlUtil.createChild(complexQueryElement, "Or");
		for (String itemId : validItemElementsMap.keySet()) {
			Element expElement = SCXmlUtil.createChild(orElement, "Exp");
			expElement.setAttribute("Name", "ItemID");
			expElement.setAttribute("Value", itemId);			
		}   
		
		String  xpxUOMListAPITemplate =  " <ItemList>" 
										 +   "<Item ItemID=''>"
										 +    "<UOMList><UOM Conversion='' UnitOfMeasure=''/></UOMList>"
									     +  "</Item>"
										 + "</ItemList>";		
		env.setApiTemplate("XPXUOMListAPI", SCXmlUtil.createFromString(xpxUOMListAPITemplate));
		Document xpxUomListForValidItemOutputDoc = api.executeFlow(env, "XPXUOMListAPI", xpxUomListForValidItemInputDoc);
		env.clearApiTemplate("XPXUOMListAPI");
		
		ArrayList<Element> xpedxItemUomListElements = SCXmlUtil.getElements(xpxUomListForValidItemOutputDoc.getDocumentElement(), XPXLiterals.E_ITEM);
		Map<String,Element> uomListForValidItemsMap = new HashMap<String,Element>(xpedxItemUomListElements.size());
		for (Element xpedxItemUomListElement : xpedxItemUomListElements) {
			if (xpedxItemUomListElement != null) {				
				uomListForValidItemsMap.put(xpedxItemUomListElement.getAttribute("ItemID"),  SCXmlUtil.getElements(xpedxItemUomListElement, "UOMList").get(0));
			}		
	   }
		return uomListForValidItemsMap;
	}
	
	/**
	 * 
	 * @param env
	 * @throws YFSException
	 * @throws RemoteException
	 */
	private void getUOMAndLegacyUOMsForValidXPXItems(YFSEnvironment env) throws YFSException, RemoteException{
		Document uOMAndLegacyUOMsInputDoc = YFCDocument.createDocument("XPEDXLegacyUomXref").getDocument();
		uOMAndLegacyUOMsInputDoc.getDocumentElement().setAttribute("LegacyType", envtId);
		uOMAndLegacyUOMsInputDoc.getDocumentElement().setAttribute("CompanyCode", companyCode);
		
		String  getLegacyUomXrefServiceTemplate =   "<XPEDXLegacyUomXrefList><XPEDXLegacyUomXref UOM ='' LegacyUOM =''/></XPEDXLegacyUomXrefList>";
		env.setApiTemplate("XPXGetLegacyUomXrefService", SCXmlUtil.createFromString(getLegacyUomXrefServiceTemplate));
		Document uOMAndLegacyUOMsOutputDoc = api.executeFlow(env, "XPXGetLegacyUomXrefService", uOMAndLegacyUOMsInputDoc); 
		env.clearApiTemplate("XPXGetLegacyUomXrefService"); 
		
		ArrayList<Element>  uOMAndLegacyUOMElements = SCXmlUtil.getElements(uOMAndLegacyUOMsOutputDoc.getDocumentElement(), "XPEDXLegacyUomXref");
		uomToLegacyMap = new HashMap<String,String>(uOMAndLegacyUOMElements.size());
		legacyToUOMMap = new HashMap<String,String>(uOMAndLegacyUOMElements.size());
		for (Element uOMAndLegacyUOMElement : uOMAndLegacyUOMElements) {
			if (uOMAndLegacyUOMElement != null) {				
				uomToLegacyMap.put(uOMAndLegacyUOMElement.getAttribute("UOM"),uOMAndLegacyUOMElement.getAttribute("LegacyUOM"));
				legacyToUOMMap.put(uOMAndLegacyUOMElement.getAttribute("LegacyUOM"),uOMAndLegacyUOMElement.getAttribute("UOM"));
			}		
	   }
	}
	/**
	 * 
	 * @param itemId
	 * @param uom
	 * @return
	 */
	private boolean isValidUOMForItem(String itemId,String uom){		
		Element xpedxItemUomListElement = uomListForValidItemsMap.get(itemId);
	    ArrayList<Element> xpedxItemUoms = SCXmlUtil.getElements(xpedxItemUomListElement, "UOM");
		for (Element xpedxItemUom : xpedxItemUoms) {
			if (xpedxItemUom != null && uom.equalsIgnoreCase(xpedxItemUom.getAttribute("UnitOfMeasure"))) {	
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checking Qty postivive or not
	 * @param stringValue
	 * @return
	 */
	private boolean isPositivAndNonZeroInteger(String stringValue){
		try{
			Integer integerValue = Integer.parseInt(stringValue);
			if(integerValue.intValue() > 0){
				return true;
			}
		}catch(Exception ex){
			return false;
		}
		return false;
	}
	
	/**
	 * initializing ship to information
	 */
	private void initialization() {
		Element customerElement = (Element) shipToCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
		Element customerExtnElement = (Element) customerElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

		envtId = customerExtnElement.getAttribute("ExtnEnvironmentCode");
		companyCode = customerExtnElement.getAttribute("ExtnCompanyCode");
		shipToSuffix = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_SHIP_TO_SUFFIX);
		shipFromBranch = customerExtnElement.getAttribute("ExtnShipFromBranch");
		legacyCustomerNumber = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_LEGACY_CUST_NO);
		customerOrderBranch = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_CUSTOMER_ORDER_BRANCH);
		customerEnvtId = customerExtnElement.getAttribute("ExtnOrigEnvironmentCode");
		customerID = customerElement.getAttribute(XPXLiterals.A_CUSTOMER_ID);
		organizationCode = customerElement.getAttribute(XPXLiterals.A_ORGANIZATION_CODE);
		sapParentAccountNo = customerExtnElement.getAttribute("ExtnSAPParentAccNo");
	}
	
	/**
	 * checking P&A call needed or not.
	 * @return
	 */
	private boolean isPandACallNeeded(){
		
		int inValidItemsCount = inValidItemPositions!=null? inValidItemPositions.size():0;
		int inValidUOMForValidItemsCount = inValidUOMPositionsForValidItems!=null? inValidUOMPositionsForValidItems.size():0;		
		int totalInvalid = inValidItemsCount +inValidUOMForValidItemsCount;
		int rquestedStockCheckItemCount = stockCheckItemList.getLength();
		
		if(totalInvalid == rquestedStockCheckItemCount){
			return false;
		}		
		return true;
	}
	
	/**
	 * create UOMList  for stock check  response
	 * @param itemId
	 * @param uomNodeList
	 * @param stockCheckResponseDocument
	 */
	private void createUOMListForStockCheckResponse(String itemId,Element uomNodeList,Document stockCheckResponseDocument){
		Element itemUOMListElement = uomListForValidItemsMap.get(itemId);					
		ArrayList<Element> xpedxItemUoms = SCXmlUtil.getElements(itemUOMListElement, "UOM");
		for (Element xpedxItemUom : xpedxItemUoms) {
			if (xpedxItemUom != null && !YFCUtils.isVoid(xpedxItemUom.getAttribute("UnitOfMeasure"))) {	
				
				String xpxUom =  xpedxItemUom.getAttribute("UnitOfMeasure");
				String convertedToCustomerUom = legacyToUOMMap.get(xpxUom);
				String uomDesc = getUomDesc(xpxUom, convertedToCustomerUom);
				
				Element uom = stockCheckResponseDocument.createElement("UOM");
				
				Element uomCode = stockCheckResponseDocument.createElement("UOMCode");
				uomCode.setTextContent(convertedToCustomerUom.replace(envtId+"_",""));
				
				Element uomDescription = stockCheckResponseDocument.createElement("UOMDescription");							
				uomDescription.setTextContent(uomDesc);
				
				uom.appendChild(uomCode);
				uom.appendChild(uomDescription);
				uomNodeList.appendChild(uom);
			}		
	   }
	}
	
 /**
  * create Catalog Attribute List  for stock check  response	
  * @param itemElement
  * @param catalogAttributeList
  * @param stockCheckResponseDocument
  */
  private void createCatalogAttributeListForStockCheckResponse(Element itemElement,Element catalogAttributeList,Document stockCheckResponseDocument){
		int listLength = 0;
		Element additionalAttrList = (Element) itemElement.getElementsByTagName("AdditionalAttributeList").item(0);
		if (additionalAttrList != null) {
			NodeList additionalAttributeList = additionalAttrList.getElementsByTagName("AdditionalAttribute");

			if(additionalAttributeList.getLength()>0)
			{
				//Catalog attributes exist
				additionalAttributeList.getLength();
				if(additionalAttributeList.getLength()<75)
				{
					listLength = additionalAttributeList.getLength();
				}
				else
				{
					listLength = 75;
				}

				for(int a=0; a<listLength; a++)
				{
					Element catalogAttribute = stockCheckResponseDocument.createElement("CatalogAttribute");
					
					Element additionalAttributeElement = (Element) additionalAttributeList.item(a);

					Element catalogAttributeName = stockCheckResponseDocument.createElement("Name");
					catalogAttributeName.setTextContent(additionalAttributeElement.getAttribute("Name"));
					Element catalogAttributeValue = stockCheckResponseDocument.createElement("Value");
					catalogAttributeValue.setTextContent(additionalAttributeElement.getAttribute("Value"));

					catalogAttribute.appendChild(catalogAttributeName);
					catalogAttribute.appendChild(catalogAttributeValue);
					catalogAttributeList.appendChild(catalogAttribute);
				}
			}
		}
 }
}

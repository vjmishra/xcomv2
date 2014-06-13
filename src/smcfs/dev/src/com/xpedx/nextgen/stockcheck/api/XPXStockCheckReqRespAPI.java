package com.xpedx.nextgen.stockcheck.api;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
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

	private static final String errorMessage_503 = "503 - An application error has occurred. If this error persists, please call the eBusiness " +
			"Customer Support desk at 1-877-269-1784.";
	private static final String errorMessage_105 = "105 – The stock check request is not in the correct format.  " +
			"Please contact your CSR or eBusiness Customer Support at 1- 877-269-1784.";
	private static final String errorMessage_104 = "104 – Please restrict your Stock Check request to 200 items or less.";
	private static final String errorMessage_100 = "100 - Sorry, we could not verify the User ID / Password that was sent.";
	private static final String errorMessage_101 = "101 - Sorry, the user credentials supplied is not authorized to access the Stock Check Web Service.";
	private static final String errorMessage_103 = "103 - Sorry, the user is not authorized on the specified customer location (Invalid eTrading ID/Customer Location).";

	private static YFCLogCategory log;
	private static YIFApi api = null;
	private static Map<String,String> uomDescMap;

	private final String B2BSourceIndicator = "1";

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

	public Document sendStockCheckResponse(YFSEnvironment env, Document inputXML) throws YFSUserExitException, RemoteException
	{
		// The uncompleted version of this code from the past called API that doesn't exist now.
		// So replaced with call to existing P&A that MIL uses.

		log.info("Received Stock Check Web Service request");
		log.debug("The stock check input xml is: "+SCXmlUtil.getString(inputXML));

		Document stockCheckOutputDocument = null;
		Document pAndArequestInputDocument = null;

		boolean isLoginValid = true;

		Element stockCheckInputDocRoot = inputXML.getDocumentElement();

		isLoginValid = checkIfLoginValid(env,stockCheckInputDocRoot);

		if(isLoginValid)
		{
			// load the uom descriptions for later lookups
			uomDescMap = getUOMList(env);

		  try
		  {
		     pAndArequestInputDocument = createPandARequestInputDocument(env,stockCheckInputDocRoot);
		     log.debug("The P&A request xml is: "+SCXmlUtil.getString(pAndArequestInputDocument));
		  }
		  catch(Exception e)
		  {
			    com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();

				errorObject.setTransType("B2B-PA");
				errorObject.setErrorClass("Unexpected / Invalid");
				errorObject.setInputDoc(inputXML);

				/*YFSException exceptionMessage = new YFSException();
				exceptionMessage.setErrorDescription("No Items Sent in input xml");*/

				errorObject.setException(e);
				ErrorLogger.log(errorObject, env);

				stockCheckOutputDocument = createErrorDocument(stockCheckInputDocRoot,errorMessage_503);
				return stockCheckOutputDocument;
		  }

		  if(pAndArequestInputDocument.getDocumentElement().getTagName().equalsIgnoreCase("PriceAndAvailability"))
		  {
			  Document pAndAResponseDocument = api.executeFlow(env, "XPXPandAWebService", pAndArequestInputDocument);
			  log.info("The P&A reponse output is: "+SCXmlUtil.getString(pAndAResponseDocument)); //TODO debug log

			  if(pAndAResponseDocument!=null)
			  {
		    	try
		    	{
		           stockCheckOutputDocument = createStockCheckOutput(env,pAndAResponseDocument,stockCheckInputDocRoot);
		    	}
		    	catch(Exception e) {
	    			e.printStackTrace();

	    			//TODO does this work?
	    			com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();

					errorObject.setTransType("B2B-PA");
					errorObject.setErrorClass("Unexpected / Invalid");
					errorObject.setInputDoc(inputXML);

					/*YFSException exceptionMessage = new YFSException();
					exceptionMessage.setErrorDescription("No Items Sent in input xml");*/

					errorObject.setException(e);
					ErrorLogger.log(errorObject, env);
		    	}
		   }
		    }
		    else
		    {
		    	//This is the stock check error document.
                com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();

				errorObject.setTransType("B2B-PA");
				errorObject.setErrorClass("Unexpected / Invalid");
				errorObject.setInputDoc(inputXML);

				YFSException exceptionMessage = new YFSException();
				exceptionMessage.setErrorDescription("P&A returned a null response");

				ErrorLogger.log(errorObject, env);
		    	return pAndArequestInputDocument;
		    }
	    }
		else
		{
			//Login is invalid
			com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();

			errorObject.setTransType("B2B-PA");
			errorObject.setErrorClass("Unexpected / Invalid");
			errorObject.setInputDoc(inputXML);

			YFSException exceptionMessage = new YFSException();
			exceptionMessage.setErrorDescription("Login credentials are invalid");

			ErrorLogger.log(errorObject, env);

			stockCheckOutputDocument = createErrorDocument(stockCheckInputDocRoot,errorMessage_100);
		    return stockCheckOutputDocument;
		}

		log.info("Final stock check doc: "+SCXmlUtil.getString(stockCheckOutputDocument)); //TODO log debug
		log.info("Completed Stock Check Web Service request");

		return stockCheckOutputDocument;
	}

	private Document createErrorDocument(Element stockCheckInputDocRoot,String errorMessage)
	{
		Document stockCheckErrorDoc = YFCDocument.createDocument("xpedxStockCheckWSResponse").getDocument();

		Element rootErrorInfo = stockCheckErrorDoc.createElement("RootErrorInfo");


		Element errorCodeElement = stockCheckErrorDoc.createElement("ErrorCode");
		errorCodeElement.setTextContent("2");
		rootErrorInfo.appendChild(errorCodeElement);

		Element errorMessageElement = stockCheckErrorDoc.createElement("ErrorMessage");
		errorMessageElement.setTextContent(errorMessage);
		rootErrorInfo.appendChild(errorMessageElement);

		/**
		 * Sender Credentials
		 */

		Element senderCredentials = stockCheckErrorDoc.createElement("SenderCredentials");

		Element userEmail = stockCheckErrorDoc.createElement("UserEmail");
		userEmail.setTextContent(SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./SenderCredentials/UserEmail").getTextContent());
		senderCredentials.appendChild(userEmail);

		Element userPassword = stockCheckErrorDoc.createElement("UserPassword");
		userPassword.setTextContent(SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./SenderCredentials/UserPassword").getTextContent());
		senderCredentials.appendChild(userPassword);



		/*****************************************************************************/

		/**
		 *  Stock Check Response Element
		 */
		Element stockCheckResponses = stockCheckErrorDoc.createElement("StockCheckResponses");
		Element stockCheckResponse  = stockCheckErrorDoc.createElement("StockCheckResponse"); //Dont forget to append to root element
		// and check if you have coded for repeating stock check response elements.

		Element eTradingID = stockCheckErrorDoc.createElement("eTradingPartnerID");
		eTradingID.setTextContent(SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./StockCheckRequests/StockCheckRequest/eTradingPartnerID").getTextContent());
		stockCheckResponse.appendChild(eTradingID);

		Element buyerID = stockCheckErrorDoc.createElement("BuyerID");
		buyerID.setTextContent(SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./StockCheckRequests/BuyerID").getTextContent());
		stockCheckResponses.appendChild(buyerID);

		Element itemsElementFromInput = SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./StockCheckRequests/StockCheckRequest/Items");

		importElement(stockCheckResponse,itemsElementFromInput);

		stockCheckResponses.appendChild(stockCheckResponse);
		stockCheckErrorDoc.getDocumentElement().appendChild(stockCheckResponses);
		stockCheckErrorDoc.getDocumentElement().appendChild(senderCredentials);
		stockCheckErrorDoc.getDocumentElement().appendChild(rootErrorInfo);
		return stockCheckErrorDoc;
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

	private boolean checkIfLoginValid(YFSEnvironment env, Element stockCheckInputDocRoot)
	{
		boolean isLoginValid = true;

		String loginId = SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./SenderCredentials/UserEmail").getTextContent();

		String password = SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./SenderCredentials/UserPassword").getTextContent();

		Document loginInputDocument = YFCDocument.createDocument("Login").getDocument();
		loginInputDocument.getDocumentElement().setAttribute("LoginID", loginId);
		loginInputDocument.getDocumentElement().setAttribute("Password",password);

		try
		{

			log.debug("The input to Login API is: "+SCXmlUtil.getString(loginInputDocument));
			Document loginOutputDocument = api.invoke(env, "login", loginInputDocument);
			log.debug("The output after Login API is: "+SCXmlUtil.getString(loginOutputDocument));
		}
		catch (YFSException e)
		{
			isLoginValid = false;
			//e.printStackTrace();

			com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();

			errorObject.setTransType("B2B-PA");
			errorObject.setErrorClass("Unexpected / Invalid");
			errorObject.setInputDoc(stockCheckInputDocRoot.getOwnerDocument());

			errorObject.setException(e);
			ErrorLogger.log(errorObject, env);
		} catch (RemoteException re)
		{
			isLoginValid = false;
			//e.printStackTrace();
            com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();

			errorObject.setTransType("B2B-PA");
			errorObject.setErrorClass("Unknown Error");
			errorObject.setInputDoc(stockCheckInputDocRoot.getOwnerDocument());
			errorObject.setException(re);
			ErrorLogger.log(errorObject, env);
		}

		log.debug("The value of isLoginValid being returned is: "+isLoginValid);
		return isLoginValid;
	}

	private Document createStockCheckOutput(YFSEnvironment env, Document pandAResponseDocument, Element stockCheckInputDocRoot) throws
	                              YFSException, RemoteException, DOMException, YIFClientCreationException,NullPointerException, YFSException, Exception
	{
		//TODO clean up unused
		String envtId = null;
		String companyCode = null;
		String shipToSuffix = null;
		String shipFromBranch = null;
		String legacyCustomerNumber = null;
		String customerOrderBranch = null;
		String customerEnvtId = null;
		String customerItemNumber = null;
		String customerID = null;
		String organizationCode = null;
		String sameDayQty = null;
		String nextDayQty = null;
		String twoDayQty = null;
		String inventoryIndicator = null;
		String orderMultiple = null;
		String sapParentAccountNo = null;

		int listLength = 0;

		Document stockCheckResponseDocument = YFCDocument.createDocument("xpedxStockCheckWSResponse").getDocument();
		Element stockCheckResponseDocRoot = stockCheckResponseDocument.getDocumentElement();

		Element pAndAResponseDocRoot = pandAResponseDocument.getDocumentElement();

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

		Element userEmail = stockCheckResponseDocument.createElement("UserEmail");
		userEmail.setTextContent(SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./SenderCredentials/UserEmail").getTextContent());
		senderCredentials.appendChild(userEmail);

		Element userPassword = stockCheckResponseDocument.createElement("UserPassword");
		userPassword.setTextContent(SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./SenderCredentials/UserPassword").getTextContent());
		senderCredentials.appendChild(userPassword);

		stockCheckResponseDocRoot.appendChild(senderCredentials);

		/*****************************************************************************/

		/**
		 *  Stock Check Response Element
		 */
		Element stockCheckResponses = stockCheckResponseDocument.createElement("StockCheckResponses");
		Element stockCheckResponse  = stockCheckResponseDocument.createElement("StockCheckResponse");
		//TODO OLD: Dont forget to append to root element and check if you have coded for repeating stock check response elements.

		Element eTradingID = stockCheckResponseDocument.createElement("eTradingPartnerID");
		String eTradingIdIn = SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./StockCheckRequests/StockCheckRequest/eTradingPartnerID").getTextContent();
		eTradingID.setTextContent(eTradingIdIn);
		stockCheckResponse.appendChild(eTradingID);

		Element buyerID = stockCheckResponseDocument.createElement("BuyerID");
		String buyerIdIn = SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./StockCheckRequests/BuyerID").getTextContent();
		buyerID.setTextContent(buyerIdIn);
		stockCheckResponses.appendChild(buyerID);
		stockCheckResponseDocRoot.appendChild(stockCheckResponses);

		Document getSAPCustomerDetailsOutputDoc = getSAPCustomerDetailsOutput(env, buyerIdIn);

		Element sapCustomerElement = (Element) getSAPCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
		String sapCustOrgCode = sapCustomerElement.getAttribute(XPXLiterals.A_ORGANIZATION_CODE);

		//With the SAP parent customer org code and the etrading id,we retrieve the ship to customer
		Document getCustomerListOutputDoc = getCustomerListOutput(env, eTradingIdIn,sapCustOrgCode);

		// Get the customer details
		if(getCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).getLength()>0)
		{
			//Customer exists
			Element customerElement = (Element) getCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);

			customerID = customerElement.getAttribute(XPXLiterals.A_CUSTOMER_ID);
			organizationCode = customerElement.getAttribute(XPXLiterals.A_ORGANIZATION_CODE);

			Element customerExtnElement = (Element) customerElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

			envtId = customerExtnElement.getAttribute("ExtnEnvironmentCode");
			companyCode = customerExtnElement.getAttribute("ExtnCompanyCode");
			shipToSuffix = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_SHIP_TO_SUFFIX);
			shipFromBranch = customerExtnElement.getAttribute("ExtnShipFromBranch");
			legacyCustomerNumber = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_LEGACY_CUST_NO);
			customerOrderBranch = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_CUSTOMER_ORDER_BRANCH);
			customerEnvtId = customerExtnElement.getAttribute("ExtnOrigEnvironmentCode");
			sapParentAccountNo = customerExtnElement.getAttribute("ExtnSAPParentAccNo");
			log.debug("The sap parent account number from getCustomer output is: "+sapParentAccountNo);
	    }

		//Dont know what to map(Level 2 error codes)
		Element errorCode1 = stockCheckResponseDocument.createElement("ErrorCode");
		stockCheckResponse.appendChild(errorCode1);

		//Dont know what to map(Level 2 error codes)
		Element errorMessage1 = stockCheckResponseDocument.createElement("ErrorMessage");
		stockCheckResponse.appendChild(errorMessage1);


		Element items = stockCheckResponseDocument.createElement("Items"); //Dont forget to append to stockCheckResponse

		Element aItemsFromPandA = (Element) pAndAResponseDocRoot.getElementsByTagName("Items").item(0);

		// Creating the item element in the stock check response
		if(aItemsFromPandA!=null)
		{
			NodeList itemListFromPandA = aItemsFromPandA.getElementsByTagName("Item");

			if(itemListFromPandA.getLength()>0)
			{
				//Items exist in P&A response

				for(int i=0; i<itemListFromPandA.getLength(); i++)
				{
					  Element itemElementFromPandA = (Element) itemListFromPandA.item(i);

					  Element item = stockCheckResponseDocument.createElement("Item");

					  Element indexID = stockCheckResponseDocument.createElement("IndexID");
					  indexID.setTextContent(SCXmlUtil.getXpathElement(itemElementFromPandA,"./LineNumber").getTextContent());
					  item.appendChild(indexID);

					  Element xpedxPartNumber = stockCheckResponseDocument.createElement("xpedxPartNumber");
					  xpedxPartNumber.setTextContent(SCXmlUtil.getXpathElement(itemElementFromPandA,"./LegacyProductCode").getTextContent());
					  item.appendChild(xpedxPartNumber);

					  //Retrieving CustomerItemNo from XRef table using legacy product code passed
					  Element customerPartNumber = stockCheckResponseDocument.createElement("CustomerPartNumber");
					  customerItemNumber = getCustomerItemNumberFromXref(env,xpedxPartNumber.getTextContent(),envtId,companyCode,legacyCustomerNumber);
					  customerPartNumber.setTextContent(customerItemNumber);
					  item.appendChild(customerPartNumber);

					  Element quantity = stockCheckResponseDocument.createElement("Quantity");
					  quantity.setTextContent(SCXmlUtil.getXpathElement(itemElementFromPandA,"./RequestedQty").getTextContent());
					  item.appendChild(quantity);

					  //[OLD: Not sure as to why requestedUOM is not passed here but the pricing UOM is passed...need to ask Prashant...I changed it to RequestedUom...
					  Element unitOfMeasure = stockCheckResponseDocument.createElement("UnitOfMeasure");
					  String baseUom = SCXmlUtil.getXpathElement(itemElementFromPandA,"./RequestedQtyUOM").getTextContent();
					  String convertedBaseUom = XPXUtils.replaceOutgoingUOMFromLegacy(
							  env, baseUom, xpedxPartNumber.getTextContent());
					  String baseUomDescription = getUomDesc(baseUom, convertedBaseUom);
					  unitOfMeasure.setTextContent(convertedBaseUom);
					  item.appendChild(unitOfMeasure);

					  //Dont know what to map(Level 3 error codes)
					  Element errorCode2 = stockCheckResponseDocument.createElement("ErrorCode");
					  errorCode2.setTextContent("0");
					  item.appendChild(errorCode2);

					  //Dont know what to map(Level 3 error codes)
					  Element errorMessage2 = stockCheckResponseDocument.createElement("ErrorMessage");
					  item.appendChild(errorMessage2);

					  Element customerNumber = stockCheckResponseDocument.createElement("CustomerNumber");

					  //Replacing customerid with SAP Parent account number
					  customerNumber.setTextContent(sapParentAccountNo);
					  item.appendChild(customerNumber);

					  Document getItemDetailsOutputDoc = getItemDetails(env,SCXmlUtil.getXpathElement(itemElementFromPandA,"./LegacyProductCode").getTextContent());
					  log.debug("The item details output is: "+SCXmlUtil.getString(getItemDetailsOutputDoc));

					  Element getCategoryOutputDocRoot =  getItemDetailsOutputDoc.getDocumentElement();
					  Element itemElement = (Element) getCategoryOutputDocRoot.getElementsByTagName("Item").item(0);
					  if(itemElement!=null)
					  {
						  // Obtain descriptions for Category1-4.  XML data from item response includes this:
						  //   "<CategoryList><Category CategoryPath="/MasterCatalog/300057/300166/300340"/></CategoryList>"
						  if (itemElement.getElementsByTagName("CategoryList").getLength() > 0)
						  {
							  Element categoryListElement = (Element) itemElement.getElementsByTagName("CategoryList").item(0);
							  if(categoryListElement.getElementsByTagName("Category").getLength()>0)
							  {
								  Element categoryElement = (Element) categoryListElement.getElementsByTagName("Category").item(0);

								  String[] cats = categoryElement.getAttribute("CategoryPath").split("/");
								    // cat[0]="", cat[1]="MasterCatalog"
								  if (cats.length > 4)
								  {
									  Element category1 = stockCheckResponseDocument.createElement("Category1");
									  category1.setTextContent(getCategoryShortDescription(cats[2], organizationCode, env));
									  item.appendChild(category1);

									  Element category2 = stockCheckResponseDocument.createElement("Category2");
									  category2.setTextContent(getCategoryShortDescription(cats[3], organizationCode, env));
									  item.appendChild(category2);

									  Element category3 = stockCheckResponseDocument.createElement("Category3");
									  category3.setTextContent(getCategoryShortDescription(cats[4], organizationCode, env));
									  item.appendChild(category3);

									  // If no cat4, output "<Category4/>" (or skip?)
									  //  Does "Paper Category" in fields count as cat4?
									  String cat4 = "";
									  if (cats.length > 5)
									  {
										  cat4 = getCategoryShortDescription(cats[5], organizationCode, env);
									  }
									  Element category4 = stockCheckResponseDocument.createElement("Category4");
									  category4.setTextContent(cat4);
									  item.appendChild(category4);
								  }
							  }
						  }

					  // Item description and Item Sell text
					  Element itemDescription = stockCheckResponseDocument.createElement("ItemDescription");
					  Element primaryInfoElement = (Element) itemElement.getElementsByTagName("PrimaryInformation").item(0);
					  itemDescription.setTextContent(primaryInfoElement.getAttribute("ExtendedDescription"));
					  item.appendChild(itemDescription);

					  Element itemSellText = stockCheckResponseDocument.createElement("ItemSellText");
					  Element itemExtnElement = (Element) itemElement.getElementsByTagName("Extn").item(0);
					  itemSellText.setTextContent(itemExtnElement.getAttribute("ExtnSellText"));
					  item.appendChild(itemSellText);

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
				    			  Element sameDayDesc = stockCheckResponseDocument.createElement("SameDayDescription");
								  sameDayDesc.setTextContent(SCXmlUtil.getXpathElement(wareHouseLocation,"./Warehouse").getTextContent());
								  item.appendChild(sameDayDesc);

				    			  Element sameDayQuantity = stockCheckResponseDocument.createElement("SameDayQuantity");
				    			  sameDayQty = SCXmlUtil.getXpathElement(wareHouseLocation,"./AvailableQty").getTextContent();
				    			  sameDayQuantity.setTextContent(sameDayQty);
								  item.appendChild(sameDayQuantity);
				    		  }

				    		  if(m==1)
				    		  {
				    			  // Next Day warehouse
                                  // Hard coded to next day
					    		  Element nextDayDesc = stockCheckResponseDocument.createElement("NextDayDescription");
				    			  nextDayDesc.setTextContent("Next Day");
								  item.appendChild(nextDayDesc);

								  Element nextDayQuantity = stockCheckResponseDocument.createElement("NextDayQuantity");
				    			  nextDayQty = SCXmlUtil.getXpathElement(wareHouseLocation,"./AvailableQty").getTextContent();
				    			  nextDayQuantity.setTextContent(nextDayQty);
								  item.appendChild(nextDayQuantity);
				    		  }

				    		  if(m==2)
				    		  {
				    			  //Two Day Warehouse
				    			  //Hardcoded to 2+days
				    			  Element twoDayDesc = stockCheckResponseDocument.createElement("TwoDayDescription");
				    			  twoDayDesc.setTextContent("2+ Days");
								  item.appendChild(twoDayDesc);

								  Element twoDayQuantity = stockCheckResponseDocument.createElement("TwoDayQuantity");
				    			  twoDayQty = SCXmlUtil.getXpathElement(wareHouseLocation,"./AvailableQty").getTextContent();
				    			  twoDayQuantity.setTextContent(twoDayQty);
								  item.appendChild(twoDayQuantity);
				    		  }
				    	  }
				        }

				      //Logic for availability message
				      Element availMessage = stockCheckResponseDocument.createElement("AvailabilityMessage");
				      String availabilityMessage = getAvailabilityMessage(env,quantity.getTextContent(),sameDayQty,nextDayQty,twoDayQty);
				      availMessage.setTextContent(availabilityMessage);
				      item.appendChild(availMessage);

				      // Logic for back order message
				      Element backOrderMessage = stockCheckResponseDocument.createElement("BackOrderMessage");
				      item.appendChild(backOrderMessage);

				      log.debug("The quantity value is: "+quantity.getTextContent());
				      log.debug("The sameDayQty value is: "+sameDayQty);
				      log.debug("The nextDayQty value is: "+nextDayQty);
				      log.debug("The twoDayQty value is: "+twoDayQty);

				      if(Integer.parseInt(quantity.getTextContent()) > 0 &&
				    	(Integer.parseInt(quantity.getTextContent()) > (Integer.parseInt(sameDayQty)+Integer.parseInt(nextDayQty)+ Integer.parseInt(twoDayQty))))
				      {
					     int backOrderQty = Integer.parseInt(quantity.getTextContent()) - (Integer.parseInt(sameDayQty)+Integer.parseInt(nextDayQty)+ Integer.parseInt(twoDayQty));

					     String backOrderedMessage = Integer.toString(backOrderQty)+""+unitOfMeasure.getTextContent()+""+"not currently available";

					     backOrderMessage.setTextContent(backOrderedMessage);
					     item.appendChild(backOrderMessage);
				      }

				      }//end no warehouse info in P&A

					  // Logic for OrderMultiple and OrderMultiple Message and ItemStatus by invoking ItemBranch table
				      Document getItemBranchDetailsOutputDoc = getItemBranchDetails(env, envtId, companyCode, shipFromBranch, xpedxPartNumber.getTextContent());
				      Element getXPXItemBranchListOutputDocRoot = getItemBranchDetailsOutputDoc.getDocumentElement();
					  Element XPXItemExtnElement = (Element)getXPXItemBranchListOutputDocRoot.getElementsByTagName(XPXLiterals.E_XPX_ITEM_EXTN).item(0);

						if(XPXItemExtnElement!=null)
						{
							inventoryIndicator = XPXItemExtnElement.getAttribute(XPXLiterals.A_INVENTORY_INDICATOR);
							orderMultiple = XPXItemExtnElement.getAttribute("OrderMultiple");
						}

						Element orderMultipleElement = stockCheckResponseDocument.createElement("OrderMultiple");
						orderMultipleElement.setTextContent(orderMultiple);
						item.appendChild(orderMultipleElement);

						if(orderMultiple!=null && orderMultiple.trim().length()!=0)
						{
						  Element orderMultipleMessage = stockCheckResponseDocument.createElement("OrderMultipleMessage");
						  orderMultipleMessage.setTextContent("Must be ordered in units of "+orderMultiple+" "+baseUomDescription);
						  item.appendChild(orderMultipleMessage);
						}

						//Setting error code as 1000 if the item is a mill item
						if(inventoryIndicator!=null && inventoryIndicator.trim().length()>0)
						{
							if(inventoryIndicator.equalsIgnoreCase("M"))
							{
								errorCode2.setTextContent("2");
								errorMessage2.setTextContent("1000 – This item is currently not stocked in your primary warehouse. " +
										"However, we can source this product from the manufacturer. Please contact your CSR or eBusiness " +
										"Customer Support at 1- 877-269-1784.");
							}
						}
						else if(XPXItemExtnElement!=null)
						{
								errorCode2.setTextContent("2");
								errorMessage2.setTextContent("1000 – This item is currently not stocked in your primary warehouse. " +
										"However, we can source this product from the manufacturer. Please contact your CSR or eBusiness " +
										"Customer Support at 1- 877-269-1784.");
						}

					    // Logic for Total Price
						Element totalPrice = stockCheckResponseDocument.createElement("TotalPrice");
						totalPrice.setTextContent(SCXmlUtil.getXpathElement(itemElementFromPandA,"./ExtendedPrice").getTextContent());
						item.appendChild(totalPrice);

						//Logic for Manufacturer and Manufacturer Part Number
						Element manufacturer = stockCheckResponseDocument.createElement("Manufacturer");
						manufacturer.setTextContent(itemExtnElement.getAttribute("ExtnSupplierNameDisplay"));
						item.appendChild(manufacturer);

						Element manufacturerPartNo = stockCheckResponseDocument.createElement("ManufacturerPartNumber");
						manufacturerPartNo.setTextContent(primaryInfoElement.getAttribute("ManufacturerItem"));
						item.appendChild(manufacturerPartNo);

						Element itemStatus = stockCheckResponseDocument.createElement("ItemStatus");
						itemStatus.setTextContent(inventoryIndicator);
						item.appendChild(itemStatus);

						//Logic for UnitPrice 1&2, UOMCode1&2 and UOMDescription1&2...right now only requested UOM and pricing UOM are returned
						//Need to confirm with Prashant 28-09-2010
						Element unitPrice1 = stockCheckResponseDocument.createElement("UnitPrice1");
						Element unitPrice2 = stockCheckResponseDocument.createElement("UnitPrice2");
						unitPrice1.setTextContent(SCXmlUtil.getXpathElement(itemElementFromPandA,"./UnitPricePerRequestedUOM").getTextContent()
								+"/"+baseUomDescription);
						item.appendChild(unitPrice1);

						if(!SCXmlUtil.getXpathElement(itemElementFromPandA,"./UnitPricePerRequestedUOM").getTextContent().
								equals(SCXmlUtil.getXpathElement(itemElementFromPandA,"./UnitPricePerPricingUOM").getTextContent()))
						{
							String pricingUom = SCXmlUtil.getXpathElement(itemElementFromPandA,"./PricingUOM").getTextContent();
							String convertedPricingUom = XPXUtils.replaceOutgoingUOMFromLegacy(
									env, pricingUom, xpedxPartNumber.getTextContent());

							String pricingUomDescription = getUomDesc(pricingUom, convertedPricingUom);
							unitPrice2.setTextContent(SCXmlUtil.getXpathElement(itemElementFromPandA,"./UnitPricePerPricingUOM").getTextContent()
									+"/"+pricingUomDescription);
							item.appendChild(unitPrice2);
						}


						//Logic for retrieving UOM Codes and description(?) is to retrieve UOMs from XPXUOMListAPI
						Document getXPXUOMListAPIOutputDoc = getXPXUomList(env,xpedxPartNumber.getTextContent(), customerID, organizationCode);
						NodeList uomList = getXPXUOMListAPIOutputDoc.getDocumentElement().getElementsByTagName("UOM");

						Element uomNodeList = stockCheckResponseDocument.createElement("UOMList");

						for(int b=0; b<uomList.getLength();b++)
						{
							Element uomElement  =  (Element) uomList.item(b);
							String xpxUom = uomElement.getAttribute("UnitOfMeasure");

							Element uom = stockCheckResponseDocument.createElement("UOM");
							Element uomCode1 = stockCheckResponseDocument.createElement("UOMCode"+(b+1));
							Element uomDescription1 = stockCheckResponseDocument.createElement("UOMDescription"+(b+1));

							String convertedToCustomerUom = XPXUtils.replaceOutgoingUOMFromLegacy(
									env, xpxUom, xpedxPartNumber.getTextContent());

							String uomDesc = getUomDesc(xpxUom, convertedToCustomerUom);

							uomCode1.setTextContent(convertedToCustomerUom);
							uom.appendChild(uomCode1);
							uomDescription1.setTextContent(uomDesc);
							uom.appendChild(uomDescription1);
							uomNodeList.appendChild(uom);
						}
						item.appendChild(uomNodeList);


						// Logic for catalog attribute name and value...Not sure which one....ItemAttribute or Attribute
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
									Element additionalAttributeElement = (Element) additionalAttributeList.item(a);

									Element catalogAttributeName = stockCheckResponseDocument.createElement("CatalogAttributeName"+(a+1));
									catalogAttributeName.setTextContent(additionalAttributeElement.getAttribute("Name"));
									Element catalogAttributeValue = stockCheckResponseDocument.createElement("CatalogAttributeValue"+(a+1));
									catalogAttributeValue.setTextContent(additionalAttributeElement.getAttribute("Value"));

									item.appendChild(catalogAttributeName);
									item.appendChild(catalogAttributeValue);
								}
							}
						}
					  }
					  else
					  {
						  //Item does not exist in catalog, so its error code
						  errorCode2.setTextContent("2");
						  errorMessage2.setTextContent("1003 – The item is not stocked in the Ordering Warehouse. " +
						  		"Please enter another Part Number.");
					  }

					  if(!errorCode2.getTextContent().equalsIgnoreCase("2"))
						{
							errorCode2.setTextContent("0");
						}

						log.debug("The final item element is: "+SCXmlUtil.getString(item));
						items.appendChild(item);
				}
			}
		}

		stockCheckResponse.appendChild(items);

		//Setting error code at Level 2...checking if one or more items have succeeded or failed.
		NodeList itemListInStockCheckResponse = items.getElementsByTagName("Item");
		int countError2 = 0;
		int countError1 = 0;
		int countError0 = 0;
		for(int i=0; i<itemListInStockCheckResponse.getLength();i++)
		{
			Element itemElement = (Element) itemListInStockCheckResponse.item(i);

			String errorCodeValue = SCXmlUtil.getXpathElement(itemElement, "./ErrorCode").getTextContent();

			if(errorCodeValue!=null && errorCodeValue.trim().length()>0)
			{
				if(errorCodeValue.equalsIgnoreCase("2"))
				{
					countError2 ++;
				}
				if(errorCodeValue.equalsIgnoreCase("1"))
				{
					countError1 ++;
				}
				if(errorCodeValue.equalsIgnoreCase("0"))
				{
					countError0 ++;
				}
		 	}
		}

		if(countError2==itemListInStockCheckResponse.getLength())
		{
			errorCode1.setTextContent("2");
			errorMessage1.setTextContent("1011 – One or more Items have not completely succeeded. " +
					"Please check the item level error for more details.");
		}
		else if(countError1 > 0 || (countError2 > 0 && countError2!=itemListInStockCheckResponse.getLength()))
		{
			errorCode1.setTextContent("1");
			errorMessage1.setTextContent("1011 – One or more Items have not completely succeeded. " +
					"Please check the item level error for more details.");
		}
		else if(countError0==itemListInStockCheckResponse.getLength())
		{
			errorCode1.setTextContent("0");
		}

		stockCheckResponses.appendChild(stockCheckResponse);

		NodeList stockCheckResponseList = stockCheckResponses.getElementsByTagName("StockCheckResponse");
		int countError2SecondLevel = 0;
		int countError1SecondLevel = 0;
		int countError0SecondLevel = 0;
		for(int i=0; i<stockCheckResponseList.getLength(); i++)
		{
			Element stockChkResponse = (Element) stockCheckResponseList.item(i);

			Element errorCodeSecondLevel  = (Element) stockChkResponse.getElementsByTagName("ErrorCode").item(0);

			if(errorCodeSecondLevel.getTextContent().equalsIgnoreCase("2"))
			{
				countError2SecondLevel ++;
			}
			if(errorCodeSecondLevel.getTextContent().equalsIgnoreCase("1"))
			{
				countError1SecondLevel ++;
			}
			if(errorCodeSecondLevel.getTextContent().equalsIgnoreCase("0"))
			{
				countError0SecondLevel ++;
			}
		}

		if(countError2SecondLevel==stockCheckResponseList.getLength())
		{
			errorCode.setTextContent("2");
			errorMessage.setTextContent("1011 – One or more Items have not completely succeeded. " +
					"Please check the item level error for more details.");
		}
		else if(countError1SecondLevel > 0 || (countError2SecondLevel > 0 && countError2SecondLevel!=stockCheckResponseList.getLength()))
		{
			errorCode.setTextContent("1");
			errorMessage.setTextContent("1011 – One or more Items have not completely succeeded. " +
					"Please check the item level error for more details.");
		}
		else if(countError0SecondLevel==stockCheckResponseList.getLength())
		{
			errorCode.setTextContent("0");
		}

		return stockCheckResponseDocument;
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

	private Document getXPXUomList(YFSEnvironment env, String itemID, String customerID, String organizationCode) throws YFSException, RemoteException
	{
		Document uomListOutputDoc = null;

		Document invokeUOMlistAPIDoc = YFCDocument.createDocument("UOM").getDocument();
		invokeUOMlistAPIDoc.getDocumentElement().setAttribute("ItemID", itemID);
		invokeUOMlistAPIDoc.getDocumentElement().setAttribute("CustomerID", customerID);
		invokeUOMlistAPIDoc.getDocumentElement().setAttribute("OrganizationCode", organizationCode);

    	log.info("XPXUOMListAPI input: "+SCXmlUtil.getString(invokeUOMlistAPIDoc)); //TODO remove
		uomListOutputDoc = api.executeFlow(env, "XPXUOMListAPI", invokeUOMlistAPIDoc);
    	log.info("XPXUOMListAPI output: "+SCXmlUtil.getString(uomListOutputDoc)); //TODO remove

		return uomListOutputDoc;
	}

	private Document getItemBranchDetails(YFSEnvironment env,String envtId, String companyCode, String division, String itemId)
	{
		Document getItemBranchDetailsOutputDoc = null;

		Document getItemBranchDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_XPX_ITEM_EXTN).getDocument();
		getItemBranchDetailsInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ITEM_ID, itemId);
		getItemBranchDetailsInputDoc.getDocumentElement().setAttribute("EnvironmentID",envtId);
		getItemBranchDetailsInputDoc.getDocumentElement().setAttribute("CompanyCode",companyCode);
		getItemBranchDetailsInputDoc.getDocumentElement().setAttribute("XPXDivision",division);

		try {
			getItemBranchDetailsOutputDoc = api.executeFlow(env,XPXLiterals.GET_XPX_ITEM_BRANCH_LIST_SERVICE, getItemBranchDetailsInputDoc);
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return getItemBranchDetailsOutputDoc;
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

	private Document getItemDetails(YFSEnvironment env, String itemID)
	{
		Document getItemListOutputDoc = null;
		if(itemID!=null && itemID.trim().length()>0)
	    {
        Document getItemDetailsInputDoc = YFCDocument.createDocument("Item").getDocument();


        getItemDetailsInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ITEM_ID,itemID);

    	try {
        	log.debug("The input to getItemList: "+SCXmlUtil.getString(getItemDetailsInputDoc));
			 env.setApiTemplate(XPXLiterals.GET_ITEM_LIST_API, getItemListTemplate);
			 getItemListOutputDoc = api.invoke(env, XPXLiterals.GET_ITEM_LIST_API, getItemDetailsInputDoc);
			 env.clearApiTemplate(XPXLiterals.GET_ITEM_LIST_API);
	        log.debug("The output from getItemList: "+SCXmlUtil.getString(getItemListOutputDoc));
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	    }
		return getItemListOutputDoc;
	}

	private String getCustomerItemNumberFromXref(YFSEnvironment env, String xpedxPartNo, String envtId, String companyCode, String legacyCustomerNumber)
	{
		Document getItemXRefListOutputDoc = null;
		String customerItemNumber = null;

         Document getItemXRefListDoc = YFCDocument.createDocument("XPXItemcustXref").getDocument();

         getItemXRefListDoc.getDocumentElement().setAttribute(XPXLiterals.A_COMPANY_CODE, companyCode);
         getItemXRefListDoc.getDocumentElement().setAttribute("EnvironmentCode", envtId);
         getItemXRefListDoc.getDocumentElement().setAttribute(XPXLiterals.A_EXTN_LEGACY_CUST_NO,legacyCustomerNumber);
         getItemXRefListDoc.getDocumentElement().setAttribute("LegacyItemNumber", xpedxPartNo);

         try {
        	log.debug("The input to customer Xref list: "+SCXmlUtil.getString(getItemXRefListDoc));
			getItemXRefListOutputDoc = api.executeFlow(env, XPXLiterals.GET_XREF_LIST, getItemXRefListDoc);
			log.debug("The output of customer Xref list: "+SCXmlUtil.getString(getItemXRefListDoc));
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		if(getItemXRefListOutputDoc != null)
		{
		Element XREFOutputDocRoot = getItemXRefListOutputDoc.getDocumentElement();

		NodeList XREFCustElementList = XREFOutputDocRoot.getElementsByTagName(XPXLiterals.E_XPX_ITEM_CUST_XREF);

		if(XREFCustElementList.getLength() > 0)
		{

			        Element XREFElement = (Element) XREFCustElementList.item(0);

			        customerItemNumber = XREFElement.getAttribute("CustomerItemNumber");
			        log.debug("The customer item number is: "+customerItemNumber);
		}

	    }

		return customerItemNumber;
	}

	private Document createPandARequestInputDocument(YFSEnvironment env, Element stockCheckInputDocRoot) throws Exception
	{
		String envtId = null;
		String companyCode = null;
		String shipToSuffix = null;
		String shipFromBranch = null;
		String legacyCustomerNumber = null;
		String customerOrderBranch = null;
		String customerEnvtId = null;
		String legacyItemNumber = null;
		String customerID = null;
		String organizationCode = null;

		Document stockCheckErrorDocument = null;

		Document pAndARequestInputDoc = YFCDocument.createDocument("PriceAndAvailability").getDocument();
		Element pAndARequestInputDocRoot = pAndARequestInputDoc.getDocumentElement();

		String buyerID = SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./StockCheckRequests/BuyerID").getTextContent();
		log.debug("The buyerID is: "+buyerID);

		String eTradingID = SCXmlUtil.getXpathElement(stockCheckInputDocRoot,"./StockCheckRequests/StockCheckRequest/eTradingPartnerID").getTextContent();
		log.debug("The eTradingID is: "+eTradingID);

		Document getSAPCustomerDetailsOutputDoc = getSAPCustomerDetailsOutput(env,buyerID);

		if(getSAPCustomerDetailsOutputDoc ==null || getSAPCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName("Customer").getLength()<=0)
		{
			stockCheckErrorDocument = createErrorDocument(stockCheckInputDocRoot,errorMessage_105);
			return stockCheckErrorDocument;
		}

		log.debug("The SAP customer details are: "+SCXmlUtil.getString(getSAPCustomerDetailsOutputDoc));
		Element sapCustomerElement = (Element) getSAPCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
		String sapCustOrgCode = sapCustomerElement.getAttribute(XPXLiterals.A_ORGANIZATION_CODE);
		Element sapCustomerExtnElement = (Element) sapCustomerElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

		if(!sapCustomerExtnElement.getAttribute("ExtnStockCheckOption").equalsIgnoreCase("Y"))
		{
			stockCheckErrorDocument = createErrorDocument(stockCheckInputDocRoot,errorMessage_101);
			return stockCheckErrorDocument;
		}

		//With the SAP parent customer org code and the etrading id,we retrieve the ship to customer

		Document getShipToCustomerDetailsOutputDoc = getCustomerListOutput(env,eTradingID,sapCustOrgCode);

		if(getShipToCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).getLength()>0)
		  {
			//Customer exists
			Element customerElement = (Element) getShipToCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);

			customerID = customerElement.getAttribute(XPXLiterals.A_CUSTOMER_ID);
			organizationCode = customerElement.getAttribute(XPXLiterals.A_ORGANIZATION_CODE);

			Element customerExtnElement = (Element) customerElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

			envtId = customerExtnElement.getAttribute("ExtnEnvironmentCode");
			companyCode = customerExtnElement.getAttribute("ExtnCompanyCode");
			shipToSuffix = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_SHIP_TO_SUFFIX);
			shipFromBranch = customerExtnElement.getAttribute("ExtnShipFromBranch");
			legacyCustomerNumber = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_LEGACY_CUST_NO);
			customerOrderBranch = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_CUSTOMER_ORDER_BRANCH);
			customerEnvtId = customerExtnElement.getAttribute("ExtnOrigEnvironmentCode");

		   }
		else
		{
			//Post to CENT Tool
			com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();

			errorObject.setTransType("B2B-PA");
			errorObject.setErrorClass("Unexpected / Invalid Data");
			errorObject.setInputDoc(stockCheckInputDocRoot.getOwnerDocument());

			YFSException exceptionMessage = new YFSException();
			exceptionMessage.setErrorDescription(errorMessage_103);

			errorObject.setException(exceptionMessage);
			ErrorLogger.log(errorObject, env);

			stockCheckErrorDocument = createErrorDocument(stockCheckInputDocRoot,errorMessage_103);
			return stockCheckErrorDocument;
		}


	    Element aItems = pAndARequestInputDoc.createElement("Items");

	    // Iterating through StockCheckRequest for Item info

	    Element stockCheckRequestsElement = (Element) stockCheckInputDocRoot.getElementsByTagName("StockCheckRequests").item(0);
	    Element stockCheckItemsElement = (Element) stockCheckRequestsElement.getElementsByTagName("Items").item(0);

	    if(stockCheckItemsElement!=null)
	    {
	    	NodeList stockCheckItemList = stockCheckItemsElement.getElementsByTagName("Item");

	    	if(stockCheckItemList.getLength()>0 && stockCheckItemList.getLength()<=200)
	    	{
	    		//Items exist

	    		for(int i=0; i<stockCheckItemList.getLength(); i++)
	    		{
	    			Element itemElement = (Element) stockCheckItemList.item(i);

	    			Element aItem = pAndARequestInputDoc.createElement("Item");

	    			Element sLineNumber = pAndARequestInputDoc.createElement("LineNumber");
	    			sLineNumber.setTextContent(SCXmlUtil.getXpathElement(itemElement,"./IndexID").getTextContent());

	    			Element sLegacyProductCode = pAndARequestInputDoc.createElement("LegacyProductCode");

	    			if(SCXmlUtil.getXpathElement(itemElement,"./CustomerPartNumber").getTextContent()!=null &&
	    					SCXmlUtil.getXpathElement(itemElement,"./CustomerPartNumber").getTextContent().trim().length()!=0)
	    			{
	    				log.debug("xpedxPartNo not avaialble.....");
	    				String customerPartNo = SCXmlUtil.getXpathElement(itemElement,"./CustomerPartNumber").getTextContent();


	    			    legacyItemNumber = getLegacyItemNumberFromXref(env,customerPartNo,envtId,companyCode,legacyCustomerNumber);

	    			    sLegacyProductCode.setTextContent(legacyItemNumber);

	    				//xpedxPartNo exists so setting this value as Legacy Product Code
	    			}
	    			else
	    			{
	    				//xpedxPartNo not avaialble.Get Customer Part No and query Customer X ref table to retrieve LegacyItemNumber

	    				log.debug("As xpedxPARTNo is there.....");
	    				legacyItemNumber = SCXmlUtil.getXpathElement(itemElement,"./xpedxPartNumber").getTextContent();
	    				sLegacyProductCode.setTextContent(legacyItemNumber);
	    			}

	    			Element sRequestedQtyUOM = pAndARequestInputDoc.createElement("RequestedQtyUOM");
	    			Element sRequestedQty = pAndARequestInputDoc.createElement("RequestedQty");

	    			if(SCXmlUtil.getXpathElement(itemElement,"./Quantity").getTextContent()==null ||
	    					SCXmlUtil.getXpathElement(itemElement,"./Quantity").getTextContent().trim().length()==0)
	    			{
	    				//Quantity not sent in the stock check request...so fetch MinimumOrderQty from Item table..as per logic
	    				// in ItemDetails paage in SWC...need to check with Prashant as this goes against what he mentioned.

	    				String minimumOrderQty = getMinOrderQty(env,legacyItemNumber);

	    				sRequestedQty.setTextContent(minimumOrderQty);

	    				// As quantity was not sent in request, ignore the UOM sent in and query using Manohar's method to fetch it.

	    				Document invokeUOMListApiInputDoc = YFCDocument.createDocument(XPXLiterals.E_ORDER).getDocument();
	    				invokeUOMListApiInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ITEM_ID, legacyItemNumber);
	    				invokeUOMListApiInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID,customerID);
	    				invokeUOMListApiInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);
	    				invokeUOMListApiInputDoc.getDocumentElement().setAttribute("EntryType","B2B");

	    					log.debug("The input to Manohar's method is(1): "+SCXmlUtil.getString(invokeUOMListApiInputDoc));
							Document invokeUOMListApiOutputDoc = api.executeFlow(env,"XPXUOMListAPI",invokeUOMListApiInputDoc );
							log.debug("The output of Manohar's method is(1): "+SCXmlUtil.getString(invokeUOMListApiOutputDoc));

							if(invokeUOMListApiOutputDoc != null)
							{
								Element firstUOMElement = (Element) invokeUOMListApiOutputDoc.getDocumentElement().getElementsByTagName("UOM").item(0);

								String baseUOM = firstUOMElement.getAttribute("UnitOfMeasure");

								sRequestedQtyUOM.setTextContent(baseUOM);
							}
	    			}

	    			else
	    			{
                        String itemQty = SCXmlUtil.getXpathElement(itemElement,"./Quantity").getTextContent();

	    				for(int strLength = 0; strLength<itemQty.trim().length(); strLength++)
	    				{
	    					//If we find a non-digit character we return false.

	    					log.debug("The item qty is: "+itemQty);
	    					if (!Character.isDigit(itemQty.charAt(strLength)))
	    					{
	    						log.debug("Getting into the IF loop");
	    						stockCheckErrorDocument = createErrorDocument(stockCheckInputDocRoot,errorMessage_105);
	    						return stockCheckErrorDocument;
	    					}
	    				}
	    				sRequestedQty.setTextContent(SCXmlUtil.getXpathElement(itemElement,"./Quantity").getTextContent());

	    				//Prashant had asked to include a dummy method to convert the UOM sent in Stock check to the the EDI or Legacy or
	    				// Customer preferred UOM...right now no clarity on that so not doing it

	    				if(SCXmlUtil.getXpathElement(itemElement,"./UOM").getTextContent()==null ||
		    					SCXmlUtil.getXpathElement(itemElement,"./UOM").getTextContent().trim().length()==0)
	    				{
	    				    //UOM empty so populate it from the XPXUOMListAPI

	    					Document invokeUOMListApiInputDoc = YFCDocument.createDocument(XPXLiterals.E_ORDER).getDocument();
		    				invokeUOMListApiInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ITEM_ID, legacyItemNumber);
		    				invokeUOMListApiInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID,customerID);
		    				invokeUOMListApiInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);
		    				//Added to distinguish this request as a B2B order
		    				invokeUOMListApiInputDoc.getDocumentElement().setAttribute("EntryType","B2B");

		    					log.debug("The input to XPXUOMListAPI (2) is: "+SCXmlUtil.getString(invokeUOMListApiInputDoc));
								Document invokeUOMListApiOutputDoc = api.executeFlow(env,"XPXUOMListAPI",invokeUOMListApiInputDoc );
								//log.debug("The input to XPXUOMListAPI (2)is: "+SCXmlUtil.getString(invokeUOMListApiInputDoc));
								if(invokeUOMListApiOutputDoc != null)
								{
									Element firstUOMElement = (Element) invokeUOMListApiOutputDoc.getDocumentElement().getElementsByTagName("UOM").item(0);

									String baseUOM = firstUOMElement.getAttribute("UnitOfMeasure");

									log.debug("The base UOM is: "+baseUOM);

									sRequestedQtyUOM.setTextContent(baseUOM);
								}
	    				}
	    				else
	    				{
	    					String uom = SCXmlUtil.getXpathElement(itemElement,"./UOM").getTextContent();

	    					String convertedUom = XPXUtils.replaceIncomingUOMFromCustomer(
	    							env, uom, sLegacyProductCode.getTextContent(), getSAPCustomerDetailsOutputDoc);
	    					sRequestedQtyUOM.setTextContent(convertedUom);
	    				}
	    			}

	    			aItem.appendChild(sLineNumber);
	    			aItem.appendChild(sLegacyProductCode);
	    			aItem.appendChild(sRequestedQtyUOM);
	    			aItem.appendChild(sRequestedQty);

	    			aItems.appendChild(aItem);
	    		}
	    	}
	    	else
	    	{
	    		stockCheckErrorDocument = createErrorDocument(stockCheckInputDocRoot,errorMessage_104);
				return stockCheckErrorDocument;
	    	}
	    	pAndARequestInputDocRoot.appendChild(aItems);
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

	    return pAndARequestInputDoc;
	}


	private Document getSAPCustomerDetailsOutput(YFSEnvironment env, String buyerId) throws Exception{

		Document getCustomerDetailsOutputDoc = null;

        YFCDocument getCustomerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
        YFCElement extnElement = getCustomerDetailsInputDoc.createElement(XPXLiterals.E_EXTN);
        extnElement.setAttribute("ExtnBuyerID", buyerId);

        getCustomerDetailsInputDoc.getDocumentElement().appendChild(extnElement);

		env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
		getCustomerDetailsOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerDetailsInputDoc.getDocument());
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);

		return getCustomerDetailsOutputDoc;
	}

	private String getMinOrderQty(YFSEnvironment env, String legacyItemNumber) throws Exception
	{
        String minOrderQty = null;

		Document getItemListOutputDoc = null;
        Document getItemListInputDoc = YFCDocument.createDocument("Item").getDocument();

        if(legacyItemNumber!=null && legacyItemNumber.trim().length()!=0)
        {
        	getItemListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ITEM_ID, legacyItemNumber);
        }

        	log.debug("The input to getItemList for Minimum OrderQty is: "+SCXmlUtil.getString(getItemListInputDoc));
			getItemListOutputDoc = api.invoke(env, XPXLiterals.GET_ITEM_LIST_API, getItemListInputDoc);
			log.debug("The output of getItemList for Minimum OrderQty is: "+SCXmlUtil.getString(getItemListOutputDoc));


		    if(getItemListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_ITEM).getLength() > 0 )
		    {
		    	NodeList itemsList = getItemListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_ITEM);
		    	for(int i=0; i<itemsList.getLength();i++)
		    	{
		    		Element itemElement = (Element) itemsList.item(i);

		    		Element primInfo = (Element) itemElement.getElementsByTagName("PrimaryInformation").item(0);

		    		minOrderQty = primInfo.getAttribute("MinOrderQuantity");

		    		log.debug("The minimum order quantity is: "+minOrderQty);
		    	}
		    }

		return minOrderQty;
	}

	private String getLegacyItemNumberFromXref(YFSEnvironment env, String customerPartNo, String envtId,
			String companyCode, String legacyCustomerNumber) throws Exception
	{
		Document getItemXRefListOutputDoc = null;
		String legacyItemNumber = null;

         Document getItemXRefListDoc = YFCDocument.createDocument("XPXItemcustXref").getDocument();

         getItemXRefListDoc.getDocumentElement().setAttribute(XPXLiterals.A_COMPANY_CODE, companyCode);
         getItemXRefListDoc.getDocumentElement().setAttribute("EnvironmentCode", envtId);
         getItemXRefListDoc.getDocumentElement().setAttribute("CustomerNumber",legacyCustomerNumber);
         getItemXRefListDoc.getDocumentElement().setAttribute("CustomerItemNumber", customerPartNo);

        	 log.debug("The input to Xref TABLE is: "+SCXmlUtil.getString(getItemXRefListDoc));
			getItemXRefListOutputDoc = api.executeFlow(env, XPXLiterals.GET_XREF_LIST, getItemXRefListDoc);
			log.debug("The output of Xref TABLE is: "+SCXmlUtil.getString(getItemXRefListOutputDoc));


		if(getItemXRefListOutputDoc != null)
		{
		Element XREFOutputDocRoot = getItemXRefListOutputDoc.getDocumentElement();

		NodeList XREFCustElementList = XREFOutputDocRoot.getElementsByTagName(XPXLiterals.E_XPX_ITEM_CUST_XREF);

		if(XREFCustElementList.getLength() > 0)
		{

			        Element XREFElement = (Element) XREFCustElementList.item(0);

			        legacyItemNumber = XREFElement.getAttribute("LegacyItemNumber");
			        log.debug("The legacy item number is: "+legacyItemNumber);
		}

	    }

		return legacyItemNumber;
	}

	private Document getCustomerListOutput(YFSEnvironment env, String tradingID, String sapCustOrgCode) throws Exception
	{
		Document getCustomerListOutputDoc = null;

		Document getCustomerListInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER).getDocument();
		getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, sapCustOrgCode);

		Element customerExtnElement = getCustomerListInputDoc.createElement(XPXLiterals.E_EXTN);
		customerExtnElement.setAttribute("ExtnETradingID", tradingID);

		getCustomerListInputDoc.getDocumentElement().appendChild(customerExtnElement);

		//log.info("SC getCustomerList: " + SCXmlUtil.getString(getCustomerListInputDoc)); //TODO remove
		env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
		getCustomerListOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc);
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		//log.info("SC getCustomerList: " + SCXmlUtil.getString(getCustomerListOutputDoc)); //TODO remove

		return getCustomerListOutputDoc;
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

}

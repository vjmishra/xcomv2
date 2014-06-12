package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXB2BOrderTranslationAPI implements YIFCustomApi
{
	private static YFCLogCategory log;
	private static YIFApi api = null;
	
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXB2BDraftOrderCreationService.xml";
	
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
	public Document createDraftOrder(YFSEnvironment env, Document inputXML) throws Exception 
	{
		String legacyItemNumber = null;
		String h_customerPONumber = null;
		String h_orderDate = null;
		String h_currencyCode = null;
		String headerLiaisonMessageId = null;
		String shipToName = null;
		String shipToAddressLine1 = null;
		String shipToAddressLine2 = null;
		String shipToAddressLine3 = null;
		String orderRequestedDeliveryDate = null;
		String customerContactID = null;
		String shipToCountry = null;
		String shipMethod = null;
		String shipToZipCode = null;
		String shipToCity = null;
		String shipToState = null;
		String orderHeaderInstruction = null;
		String orderAttentionName = null;
		String customerLineNumber = null;
		String customerLinePONumber = null;
		String lineReqDeliveryDate = null;
		String legacyProductCode = null;
		String customerPartNo = null;
		String convertedReqUnitOfMeasure = null;
		String orderedByName = null;
		String isEtradingIDInvalidFlag = "N";
		String lineLiaisonMessageId = "";
		String refOrderLineKey = "";
		String refOrderHeaderKey = "";
		String shipToNameFromCustProfile = "";
		
				
		YFCDocument createDraftOrderInputDoc = null;
		
		Element inputXMLRoot = inputXML.getDocumentElement();	
				
		try
		{			
		/* Start - Changes made for CR-853
		 * On successful reprocess of the reference order, invalid etrading id flag in Reference Order Header table needs to be updated to 'N' */
			Object tranObject = env.getTxnObject("ReferenceOrderHeaderKey");
			if(tranObject != null){
				if(log.isDebugEnabled()){
					log.debug("refOrderHeaderKey from Transaction object.");
				}
			refOrderHeaderKey = tranObject.toString();
			}else{
				if(log.isDebugEnabled()){
					log.debug("refOrderHeaderKey not present in transaction object.");
				}
			}
		int IsInvalidEtradingIDExist = 	inputXML.getElementsByTagName("IsInvalidEtradingID").getLength();	
		// To check if attribute 'IsInvalidEtradingID' exist in Input xml.
		if(IsInvalidEtradingIDExist > 0){						
			isEtradingIDInvalidFlag = SCXmlUtil.getXpathElement(inputXMLRoot, "./IsInvalidEtradingID").getTextContent();
		}		
		if(isEtradingIDInvalidFlag != null && isEtradingIDInvalidFlag.equalsIgnoreCase("Y")){		
			// refOrderHeaderKey = SCXmlUtil.getXpathElement(inputXMLRoot, "./RefOrderHdrKey").getTextContent();
			// Creating the input document to call 'changeXPXRefOrderHdr' service.
			Document inputDoc = YFCDocument.createDocument("XPXRefOrderHdr").getDocument();
			Element xpxRefOrderHdrElement = inputDoc.getDocumentElement();
			xpxRefOrderHdrElement.setAttribute("RefOrderHdrKey", refOrderHeaderKey);
			xpxRefOrderHdrElement.setAttribute("IsInvalidETradingID", "N");		
								
			// To update the 'IsInvalidETradingID' flag in XPXRefOrderHdr table.
			Document outxml = api.executeFlow(env, "changeXPXRefOrderHdr", inputDoc);			
		}
			
		/* End - Changes made for CR-853 */
		
		//String h_customerPONumber = SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderHeader/OrderNumber/BuyerOrderNumber").getTextContent();
		if(SCXmlUtil.getXpathElement(inputXMLRoot, "./CustomerPO")!=null)
		{
		   h_customerPONumber = SCXmlUtil.getXpathElement(inputXMLRoot, "./CustomerPO").getTextContent();
			if(log.isDebugEnabled()){
		   log.debug("The customer PO Number is: "+h_customerPONumber);
			}
		}
		
		//String h_orderDate = SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderHeader/OrderIssueDate").getTextContent();
		if(SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderCreateDate")!=null)
		{
		   h_orderDate = SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderCreateDate").getTextContent();
			if(log.isDebugEnabled()){
		   log.debug("The order creation date is: "+h_orderDate);
			}
		}
		
		//String h_currencyCode = SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderHeader/OrderCurrency/Currency/CurrencyCoded").getTextContent();
		if(SCXmlUtil.getXpathElement(inputXMLRoot, "./CurrencyCode")!=null)
		{
		 h_currencyCode = SCXmlUtil.getXpathElement(inputXMLRoot, "./CurrencyCode").getTextContent();
		 
		}
		if(h_currencyCode==null || h_currencyCode.trim().length()<=0)
		{
			h_currencyCode = "USD";
		}
		if(log.isDebugEnabled()){
		log.debug("The currency code is: "+h_currencyCode);
		}
		//String eTradingId = SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderHeader/OrderParty/ShipToParty/" +
				//"Party/PartyID/Identifier/Ident").getTextContent();
				
		String eTradingId = SCXmlUtil.getXpathElement(inputXMLRoot, "./EtradingId").getTextContent();
		if(log.isDebugEnabled()){
		log.debug("The etradingID is: "+eTradingId);
		}
		//String headerLiaisonId = SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderHeader/OrderNumber/ListOfMessageID/MessageID").getTextContent();
		if(SCXmlUtil.getXpathElement(inputXMLRoot, "./LiaisonMessageId")!=null)
		{
		   headerLiaisonMessageId = SCXmlUtil.getXpathElement(inputXMLRoot, "./LiaisonMessageId").getTextContent();
			if(log.isDebugEnabled()){
				log.debug("The header liaison message id is: "+headerLiaisonMessageId);
			}
		}
        //Getting the parent SAP customer of the BillTo customer with the BuyerId
		
		//String buyerId = SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderHeader/OrderParty/BuyerParty/Party/PartyID/Identifier/Ident").getTextContent();
		String buyerId = SCXmlUtil.getXpathElement(inputXMLRoot, "./BuyerId").getTextContent();
		if(log.isDebugEnabled()){
		log.debug("The buyerId is: "+buyerId);
		}
		
		Document getSAPCustomerDetailsOutputDoc = getSAPCustomerDetailsOutput(env,buyerId); 
		if(log.isDebugEnabled()){
		log.debug("The SAP Customer details output is: "+SCXmlUtil.getString(getSAPCustomerDetailsOutputDoc));
		}
		Element sapCustomerElement = (Element) getSAPCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
		String sapCustOrgCode = sapCustomerElement.getAttribute(XPXLiterals.A_ORGANIZATION_CODE);	
		
		
		//With the SAP parent customer org code and the etrading id,we retrieve the ship to customer
		
		Document getShipToCustomerDetailsOutputDoc = getCustomerDetailsOutput(env,eTradingId,sapCustOrgCode);  
		if(log.isDebugEnabled()){
		log.debug("The shipTo customer details are: "+SCXmlUtil.getString(getShipToCustomerDetailsOutputDoc));
		}
		Element customerElement = (Element) getShipToCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
		String buyerOrganizationCode = customerElement.getAttribute(XPXLiterals.A_CUSTOMER_ID);		
		Element parentCustomerElement = (Element) customerElement.getElementsByTagName(XPXLiterals.E_PARENT_CUSTOMER).item(0);
		String billToId = parentCustomerElement.getAttribute(XPXLiterals.A_CUSTOMER_ID);
		
        //Fix added for JIRA 1710
		
		Element buyerOrgElement = (Element) customerElement.getElementsByTagName(XPXLiterals.E_BUYER_ORGANIZATION).item(0);
		if(buyerOrgElement!=null)
		{
		     shipToNameFromCustProfile = buyerOrgElement.getAttribute(XPXLiterals.A_ORGANIZATION_NAME);
		}
		
		/*Start - changes made for CR 1182*/
		//Retrieve eCSR1 and eCSR2 from BillToId.
		Document getBillToCustomerDetailsOutputDoc = getBillToCutomerDetailsOutput(env,billToId,sapCustOrgCode);
		/*End*/
		
		Element customerExtnElement = (Element) customerElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		String envtId = customerExtnElement.getAttribute("ExtnEnvironmentCode");
		String companyCode = customerExtnElement.getAttribute("ExtnCompanyCode");
		String legacyCustomerNumber = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_LEGACY_CUST_NO);
		String shipToSuffix = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_SHIP_TO_SUFFIX);
		String billToSuffix = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_BILL_TO_SUFFIX);
		String customerDivision = customerExtnElement.getAttribute("ExtnCustomerDivision");
		
        /************JIRA #2226 bug Fix by Prasanth Kumar M.****************************/
		
		Element custAddressList = (Element) customerElement.getElementsByTagName("CustomerAdditionalAddressList").item(0);
		Element custAddress = (Element) custAddressList.getElementsByTagName("CustomerAdditionalAddress").item(0);
		if(custAddress!=null)
		{
	        Element personInfo = (Element) custAddress.getElementsByTagName("PersonInfo").item(0);
	        if(personInfo!=null)
	        {
	            shipToAddressLine1 = personInfo.getAttribute("AddressLine1");
	            shipToAddressLine2 = personInfo.getAttribute("AddressLine2");
	            shipToAddressLine3 = personInfo.getAttribute("AddressLine3");
	            shipToZipCode = personInfo.getAttribute("ZipCode");
	            shipToCity = personInfo.getAttribute("City");
	            shipToState = personInfo.getAttribute("State");
	            shipToCountry = personInfo.getAttribute("Country");
	        }    
		}     
		/*******************************************************************************/
		
		
		
		//String shipToName = SCXmlUtil.getXpathElement(inputXMLRoot,"./OrderHeader/OrderParty/ShipToParty/Party/NameAddress/Name1").getTextContent();
		if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToName")!=null)
		{
		    shipToName = SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToName").getTextContent();
		}
		/*String shipToAddressLine1 = SCXmlUtil.getXpathElement(inputXMLRoot,
				"./OrderHeader/OrderParty/ShipToParty/Party/NameAddress/Street").getTextContent();*/
		/*if(SCXmlUtil.getXpathElement(inputXMLRoot,	"./ShipToAddress1")!=null)
		{
		   shipToAddressLine1 = SCXmlUtil.getXpathElement(inputXMLRoot,	"./ShipToAddress1").getTextContent();
		}*/
		/*String shipToZipCode = SCXmlUtil.getXpathElement(inputXMLRoot,
				"./OrderHeader/OrderParty/ShipToParty/Party/NameAddress/PostalCode").getTextContent();*/
		/*if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToZIP")!=null)
		{
		 shipToZipCode = SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToZIP").getTextContent();
		}*/
		/*String shipToCity = SCXmlUtil.getXpathElement(inputXMLRoot,
				"./OrderHeader/OrderParty/ShipToParty/Party/NameAddress/City").getTextContent();*/
		/*if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToCity")!=null)
		{
		   shipToCity = SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToCity").getTextContent();
		}*/
		/*String shipToState = SCXmlUtil.getXpathElement(inputXMLRoot,
				"./OrderHeader/OrderParty/ShipToParty/Party/NameAddress/Region/RegionCoded").getTextContent();*/
		/*if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToState")!=null)
		{
		  shipToState = SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToState").getTextContent();
		}*/
				
		/*String orderHeaderInstruction = SCXmlUtil.getXpathElement(inputXMLRoot,
				"./OrderHeader/OrderHeaderNote").getTextContent();*/
		if(SCXmlUtil.getXpathElement(inputXMLRoot,"./HeaderComments")!=null)
		{
		   orderHeaderInstruction = SCXmlUtil.getXpathElement(inputXMLRoot,"./HeaderComments").getTextContent();
		}
		/*String orderAttentionName = SCXmlUtil.getXpathElement(inputXMLRoot,
				"./OrderHeader/OrderParty/ShipToParty/Party/OrderContact/Contact/ContactName").getTextContent();*/
		if(SCXmlUtil.getXpathElement(inputXMLRoot,"./AttentionName")!=null)
		{
		   orderAttentionName = SCXmlUtil.getXpathElement(inputXMLRoot,"./AttentionName").getTextContent();
		}
		
		// New values added as per updated mapping sheet in Design doc v 1.5
		
		/*String shipToAddressLine2 = SCXmlUtil.getXpathElement(inputXMLRoot,
		"./OrderHeader/OrderParty/ShipToParty/Party/NameAddress/StreetSupplement1").getTextContent();*/
		/*if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToAddress2")!=null)
		{
		  shipToAddressLine2 = SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToAddress2").getTextContent();
		}*/
		/*String orderRequestedDeliveryDate = SCXmlUtil.getXpathElement(inputXMLRoot,
		"./OrderHeader/OrderHeaderDates/RequestedDeliverByDate").getTextContent();*/
		if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipDate")!=null)
		{
		 orderRequestedDeliveryDate = SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipDate").getTextContent();
		}
		/*String customerContactID = SCXmlUtil.getXpathElement(inputXMLRoot,
		"./OrderHeader/OrderParty/ShipToParty/Party/OrderContact/Contact/ContactName").getTextContent();*/
		if(SCXmlUtil.getXpathElement(inputXMLRoot,"./OrderedByName")!=null)
		{
		   orderedByName = SCXmlUtil.getXpathElement(inputXMLRoot,"./OrderedByName").getTextContent();
		}
		/*if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToAddress3")!=null)
		{
		  shipToAddressLine3 = SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToAddress3").getTextContent();
		}
		if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToCountryCode")!=null)
		{
		  shipToCountry = SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToCountryCode").getTextContent();
		}*/
		if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipMethod")!=null)
		{
		  shipMethod = SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipMethod").getTextContent();
		}
		
		//Constructing the createDraftOrderInput for OrderHeader values
		
		createDraftOrderInputDoc = YFCDocument.createDocument(XPXLiterals.E_ORDER);
		createDraftOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_BILL_TO_ID, billToId);
		createDraftOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE, buyerOrganizationCode);
		createDraftOrderInputDoc.getDocumentElement().setAttribute("ShipToID", buyerOrganizationCode);
		if(h_orderDate!=null && h_orderDate.trim().length()>=0)
		{
		createDraftOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_DATE, h_orderDate);
		}
		createDraftOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_PO_NO, h_customerPONumber);
		createDraftOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTERPRISE_CODE, XPXLiterals.A_ORGANIZATIONCODE_VALUE);
		createDraftOrderInputDoc.getDocumentElement().setAttribute("DraftOrderFlag", "Y");
		createDraftOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_TYPE, XPXLiterals.CUSTOMER);
		createDraftOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTRY_TYPE, "B2B");
		createDraftOrderInputDoc.getDocumentElement().setAttribute("ReqDeliveryDate",orderRequestedDeliveryDate);
		//createDraftOrderInputDoc.getDocumentElement().setAttribute("CustomerContactID",customerContactID);
		createDraftOrderInputDoc.getDocumentElement().setAttribute("CustomerID",billToId);
		createDraftOrderInputDoc.getDocumentElement().setAttribute("CarrierServiceCode", shipMethod);
		
		YFCElement priceInfoElement = createDraftOrderInputDoc.createElement(XPXLiterals.E_PRICE_INFO);
		priceInfoElement.setAttribute(XPXLiterals.A_CURRENCY, h_currencyCode);
		createDraftOrderInputDoc.getDocumentElement().appendChild(priceInfoElement);
		
		YFCElement orderExtnElement = createDraftOrderInputDoc.createElement(XPXLiterals.E_EXTN);
		orderExtnElement.setAttribute("ExtnETradingID", eTradingId);
		/**********Commenting out as per requirement in JIRA #1710*******************************/
		orderExtnElement.setAttribute("ExtnShipToName", shipToNameFromCustProfile);
		//orderExtnElement.setAttribute("ExtnBillToName", billToName);
		orderExtnElement.setAttribute("ExtnAttentionName", orderAttentionName);
		orderExtnElement.setAttribute("ExtnMsgHeaderId", headerLiaisonMessageId);
		orderExtnElement.setAttribute("ExtnSourceType","1");
		orderExtnElement.setAttribute("ExtnOrderedByName", orderedByName);
		orderExtnElement.setAttribute(XPXLiterals.A_EXTN_BILL_TO_SUFFIX, billToSuffix);
		orderExtnElement.setAttribute(XPXLiterals.A_EXTN_SHIP_TO_SUFFIX, shipToSuffix);
		orderExtnElement.setAttribute("ExtnCustomerNo",legacyCustomerNumber);
		orderExtnElement.setAttribute("ExtnCustomerDivision", customerDivision+"_"+envtId);
		createDraftOrderInputDoc.getDocumentElement().appendChild(orderExtnElement);
		
		YFCElement personInfoShipToElement = createDraftOrderInputDoc.createElement(XPXLiterals.E_PERSON_INFO_SHIP_TO);
		personInfoShipToElement.setAttribute("AddressLine1", shipToAddressLine1);
		personInfoShipToElement.setAttribute("AddressLine2", shipToAddressLine2);
		personInfoShipToElement.setAttribute("AddressLine3", shipToAddressLine3);
		personInfoShipToElement.setAttribute("ZipCode",shipToZipCode);
		personInfoShipToElement.setAttribute("City", shipToCity);
		personInfoShipToElement.setAttribute("State", shipToState);
		personInfoShipToElement.setAttribute("Country", shipToCountry);
		createDraftOrderInputDoc.getDocumentElement().appendChild(personInfoShipToElement);
		
		
		//Commenting out as bill to info is not sent
		
		/*YFCElement personInfoBillToElement = createDraftOrderInputDoc.createElement(XPXLiterals.E_PERSON_INFO_BILL_TO);
		personInfoBillToElement.setAttribute("AddressLine1", billToAddressLine1);
		personInfoBillToElement.setAttribute("ZipCode",billToZipCode);
		personInfoBillToElement.setAttribute("City", billToCity);
		personInfoBillToElement.setAttribute("State", billToState);
		createDraftOrderInputDoc.getDocumentElement().appendChild(personInfoBillToElement);*/
		
		if(orderHeaderInstruction != null && orderHeaderInstruction.trim().length()>0)
		{
		YFCElement orderInstructionsElement = createDraftOrderInputDoc.createElement(XPXLiterals.E_INSTRUCTIONS);
		YFCElement orderInstructionElement = createDraftOrderInputDoc.createElement("Instruction");
		orderInstructionElement.setAttribute("InstructionType", "HEADER");
		orderInstructionElement.setAttribute("InstructionText", orderHeaderInstruction);
		orderInstructionsElement.appendChild(orderInstructionElement);
		createDraftOrderInputDoc.getDocumentElement().appendChild(orderInstructionsElement);
		}
		
		
		//Capturing the orderLine details
		
		//Element orderDetailElement = (Element) inputXMLRoot.getElementsByTagName("OrderDetail").item(0);
		//Element listOfItemDetailElement = (Element) orderDetailElement.getElementsByTagName("ListOfItemDetail").item(0);
		Element listOfItemDetailElement = (Element) inputXMLRoot.getElementsByTagName("LineItems").item(0);
		
		//NodeList itemDetailList = listOfItemDetailElement.getElementsByTagName("ItemDetail");
		NodeList itemDetailList = listOfItemDetailElement.getElementsByTagName("LineItem");
		
		if(itemDetailList.getLength()>0)
		{
			YFCElement orderLinesElement = createDraftOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINES);
			
			for(int i=0;i<itemDetailList.getLength(); i++)
			{
				Element itemDetailElement = (Element) itemDetailList.item(i);
				
				YFCElement orderLineElement = createDraftOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINE);
				/*orderLineElement.setAttribute(XPXLiterals.A_PRIME_LINE_NO, SCXmlUtil.getXpathElement(itemDetailElement,
						"./BaseItemDetail/LineItemNum/BuyerLineItemNum").getTextContent());*/
				/*orderLineElement.setAttribute(XPXLiterals.A_PRIME_LINE_NO, SCXmlUtil.getXpathElement(itemDetailElement,
				"./BaseItemDetail/LineItemNum/BuyerLineItemNum").getTextContent());*/
				orderLineElement.setAttribute(XPXLiterals.A_LINE_TYPE, "P");// Temporarily hard coding it to line type of LegacyProdcutCode
				
				/*String customerLineNumber = SCXmlUtil.getXpathElement(itemDetailElement,
				"./BaseItemDetail/LineItemNum/SellerLineItemNum").getTextContent();*/
			    if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerLineNumber")!=null)
			    {
				customerLineNumber = SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerLineNumber").getTextContent();
			    orderLineElement.setAttribute("CustomerLinePONo",customerLineNumber);
			    }
			    
				/*String customerLinePONumber = SCXmlUtil.getXpathElement(itemDetailElement,
				"./BaseItemDetail/ListOfItemReferences/ListOfReferenceCoded/ReferenceCoded/OriginalPurchaseOrder/PrimaryReference/Reference/RefNum").getTextContent();*/
			    if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerLinePONumber")!=null)
			    {
				customerLinePONumber = SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerLinePONumber").getTextContent();
				orderLineElement.setAttribute("CustomerPONo",customerLinePONumber);
			    }
				//New Mapping
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./LineRequestedDeliveryDate")!=null)
				{
				lineReqDeliveryDate = SCXmlUtil.getXpathElement(itemDetailElement,"./LineRequestedDeliveryDate").getTextContent();
				orderLineElement.setAttribute("ReqDeliveryDate",lineReqDeliveryDate);
				}
				
								
				YFCElement orderLineExtnElement = createDraftOrderInputDoc.createElement(XPXLiterals.E_EXTN);
				/*orderLineExtnElement.setAttribute("ExtnUnitPrice", SCXmlUtil.getXpathElement(itemDetailElement,
						"./PricingDetail/ListOfPrice/Price/UnitPrice/UnitPriceValue").getTextContent());*/
				/*if(SCXmlUtil.getXpathElement(itemDetailElement,"./UnitPrice")!=null)
				{
				orderLineExtnElement.setAttribute("ExtnUnitPrice", SCXmlUtil.getXpathElement(itemDetailElement,"./UnitPrice").getTextContent());
				orderLineExtnElement.setAttribute("ExtnUnitPriceDiscount", SCXmlUtil.getXpathElement(itemDetailElement,"./UnitPrice").getTextContent());
				}*///Commented out as part of fix for JIRA # 2213
				/*orderLineExtnElement.setAttribute("ExtnPricingUOM", SCXmlUtil.getXpathElement(itemDetailElement,
						"./PricingDetail/ListOfPrice/Price/UnitPrice/UnitOfMeasurement/UOMCoded").getTextContent());*/
				if(SCXmlUtil.getXpathElement(itemDetailElement,	"./PriceUnitOfMeasure")!=null)
				{
				orderLineExtnElement.setAttribute("ExtnPricingUOM", SCXmlUtil.getXpathElement(itemDetailElement,
				"./PriceUnitOfMeasure").getTextContent());
				}
				
								
				/*String customerLineAccountNumber = SCXmlUtil.getXpathElement(itemDetailElement,
				"./ListOfStructuredNote/StructuredNote/GeneralNote/NoteID").getTextContent();*/
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerLineAccountNumber")!=null)
				{
				String customerLineAccountNumber = SCXmlUtil.getXpathElement(itemDetailElement,
				"./CustomerLineAccountNumber").getTextContent();
				orderLineExtnElement.setAttribute("ExtnCustLineAccNo",customerLineAccountNumber);
				}
				
				/*String customerUserField1 = SCXmlUtil.getXpathElement(itemDetailElement,
				"./BaseItemDetail/ListOfItemReferences/ListOfReferenceCoded/ReferenceCoded/ReleaseNumber/PrimaryReference/Reference/RefNum").getTextContent();*/
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerUserField1")!=null)
				{
				String customerUserField1 = SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerUserField1").getTextContent();
				orderLineExtnElement.setAttribute("ExtnCustLineField1",customerUserField1 );
				}
				/*String customerUserField2 = SCXmlUtil.getXpathElement(itemDetailElement,
				"./BaseItemDetail/ListOfItemReferences/ListOfReferenceCoded/ReferenceCoded/CustomerReferenceNumber/PrimaryReference/Reference/RefNum").getTextContent();*/
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerUserField2")!=null)
				{
				String customerUserField2 = SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerUserField2").getTextContent();
				orderLineExtnElement.setAttribute("ExtnCustLineField2",customerUserField2);
				}
				
				/*******Added as a fix for JIRA bug # 1450************************/
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerUserField3")!=null)
				{
				String customerUserField3 = SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerUserField3").getTextContent();
				orderLineExtnElement.setAttribute("ExtnCustLineField3",customerUserField3);
				}
				/******************************************************************/
				
				/*String lineLiaisonMessageId = SCXmlUtil.getXpathElement(itemDetailElement,
				"./BaseItemDetail/LineItemType/LineItemTypeCode/LineItemTypeCoded").getTextContent();*/
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./POLineID")!=null)
				{
				lineLiaisonMessageId = SCXmlUtil.getXpathElement(itemDetailElement,"./POLineID").getTextContent();
				orderLineExtnElement.setAttribute("ExtnMsgLineId",lineLiaisonMessageId);
				}
				orderLineElement.appendChild(orderLineExtnElement);
				
				
				YFCElement itemElement  = createDraftOrderInputDoc.createElement(XPXLiterals.E_ITEM);
				
				/*String legacyProductCode = SCXmlUtil.getXpathElement(itemDetailElement,
				"./BaseItemDetail/ItemIdentifiers/PartNumbers/SellerPartNumber/PartNum/PartID").getTextContent();*/
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./LegacyProductCode")!=null)
				{
				 legacyProductCode = SCXmlUtil.getXpathElement(itemDetailElement,"./LegacyProductCode").getTextContent();
				}
				/*String customerPartNo = SCXmlUtil.getXpathElement(itemDetailElement,
				"./BaseItemDetail/ItemIdentifiers/PartNumbers/BuyerPartNumber/PartNum/PartID").getTextContent();*/
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerProductCode")!=null)
				{
				  customerPartNo = SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerProductCode").getTextContent();
				  itemElement.setAttribute("CustomerItem",SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerProductCode").getTextContent());
				}
				
				if(customerPartNo != null && customerPartNo.trim().length()!=0)
				{
					//Use Customer Part Number to query Xref table to get the item id
					
					legacyItemNumber = getLegacyItemNumberFromXref(env,customerPartNo,envtId,companyCode,legacyCustomerNumber);
					
					itemElement.setAttribute(XPXLiterals.A_ITEM_ID, legacyItemNumber);
					if(log.isDebugEnabled()){
					log.debug("The item id is: "+legacyItemNumber);
					}
				}
				
				else
				{
					itemElement.setAttribute(XPXLiterals.A_ITEM_ID, legacyProductCode);
					if(log.isDebugEnabled()){
					log.debug("The item id is: "+legacyProductCode);
					}
				}
				
				/* Start - changes made for CR - 850 
				 * To check the item id is invalid and stamp the IsInvalidItemID flag in xpx_ref_order_line table.
				 * */
				String itemId = itemElement.getAttribute(XPXLiterals.A_ITEM_ID);	
				
				if(itemId.isEmpty()){
					// To get the Reference Order Line key from the transaction object.
					refOrderLineKey = getRefOrdLineKey(env,lineLiaisonMessageId);			
					// Update IsInvalidItemFlag in xpx_ref_order_line table.
					updateInvalidFlag(env,refOrderHeaderKey,refOrderLineKey,"Item");
					if(log.isDebugEnabled()){
					log.debug("Item ID is empty.");
					}
					// Error logged in CENT tool.				
					
					/*Start CR-1182 changes*/
					//Send an email to the eCSR1 and eCSR2 of the BillTO customer
					
					sendEmail(env,getBillToCustomerDetailsOutputDoc,"Item",inputXMLRoot,sapCustOrgCode);
					
					/*End*/
					
					YFSException exceptionMessage = new YFSException();
					exceptionMessage.setErrorDescription("Item Id is empty in XPXB2BOrderTranslationAPI.java");		
					log.error("Item Id is empty in XPXB2BOrderTranslationAPI.java");				
					throw exceptionMessage;						
				}else{
					// getItemList to check if this ItemId exists in the YFS_ITEM table. 
					
					// Form an input document to call getItemList API
					// <Item ItemID="" />
					Document itemInputDoc = SCXmlUtil.createDocument("Item");
					itemInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ITEM_ID,itemId );
					// Call getItemList					
					Document outputItemDoc = api.invoke(env, XPXLiterals.GET_ITEM_LIST_API, itemInputDoc);					
					if(!outputItemDoc.getDocumentElement().hasChildNodes()){
						if(log.isDebugEnabled()){
						log.debug("Item ID is invalid.");
						}
						// To get the Reference Order Line key from the transaction object.
						refOrderLineKey = getRefOrdLineKey(env,lineLiaisonMessageId);
						// Stamp IsInvalidItemFlag in xpx_ref_order_line table.
						updateInvalidFlag(env,refOrderHeaderKey,refOrderLineKey,"Item");						
						// Error logged in CENT tool.
						
						/*Start CR-1182 changes*/
						//Send an email to the eCSR1 and eCSR2 of the BillTO customer						
						sendEmail(env,getBillToCustomerDetailsOutputDoc,"Item",inputXMLRoot,sapCustOrgCode);
						
						YFSException exceptionMessage = new YFSException();
						exceptionMessage.setErrorDescription("Item Id is invalid in XPXB2BOrderTranslationAPI.java");		
						log.error("Item Id is invalid in XPXB2BOrderTranslationAPI.java");				
						throw exceptionMessage;
					}
				}				
				/* End - changes made for CR - 850 */				
								
				/*itemElement.setAttribute("ItemDesc", SCXmlUtil.getXpathElement(itemDetailElement,
						"./BaseItemDetail/ItemIdentifiers/ItemDescription").getTextContent());*/
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./LineDescription")!=null)
				{
				itemElement.setAttribute("ItemShortDesc", SCXmlUtil.getXpathElement(itemDetailElement,"./LineDescription").getTextContent());
				}
				orderLineElement.appendChild(itemElement);
				
							
				
				// Ends here for line items				
				
				YFCElement orderLineTranQuantityElement = createDraftOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY);
				/*orderLineTranQuantityElement.setAttribute(XPXLiterals.A_TRANSACTIONAL_UOM, SCXmlUtil.getXpathElement(itemDetailElement,
						"./BaseItemDetail/TotalQuantity/Quantity/UnitOfMeasurement/UOMCoded").getTextContent());*/
				
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./RequestedUnitOfMeasure")!=null)
				{
				String reqUnitOfMeasure = SCXmlUtil.getXpathElement(itemDetailElement,"./RequestedUnitOfMeasure").getTextContent();
				
				 try {
						convertedReqUnitOfMeasure = XPXUtils.replaceIncomingUOMFromCustomer(env, reqUnitOfMeasure, itemElement.getAttribute(XPXLiterals.A_ITEM_ID),
								buyerId, eTradingId);
					} catch (Exception e)
					{
						/* Start - changes made for CR - 850 */
						// To get the Reference Order Line key from the transaction object.
						refOrderLineKey = getRefOrdLineKey(env,lineLiaisonMessageId);
						// Update IsInvalidItemFlag in xpx_ref_order_line table.
						updateInvalidFlag(env,refOrderHeaderKey,refOrderLineKey,"UOM");	
						if(log.isDebugEnabled()){
						log.debug("Invalid UOM");
						}
						/* End - changes made for CR - 850 */
						
						/*Start CR-1182 changes*/
						//Send an email to the eCSR1 and eCSR2 of the BillTO customer						
						sendEmail(env,getBillToCustomerDetailsOutputDoc,"UOM",inputXMLRoot,sapCustOrgCode);
						
						// Error logged in CENT.
						log.error("Invalid UOM. Exception in XPXB2BOPTranslationAPI...YFSException");
						throw e;
					}
				 orderLineTranQuantityElement.setAttribute(XPXLiterals.A_TRANSACTIONAL_UOM, convertedReqUnitOfMeasure);
				}
				
				/*orderLineTranQuantityElement.setAttribute(XPXLiterals.A_ORDERED_QTY, SCXmlUtil.getXpathElement(itemDetailElement,
						"./BaseItemDetail/TotalQuantity/Quantity/QuantityValue").getTextContent());*/
				
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./RequestedOrderQuantity")!=null)
				{
				orderLineTranQuantityElement.setAttribute(XPXLiterals.A_ORDERED_QTY, SCXmlUtil.getXpathElement(itemDetailElement,
				"./RequestedOrderQuantity").getTextContent());
				}
				orderLineElement.appendChild(orderLineTranQuantityElement);
				
				YFCElement linePriceInfoElement = createDraftOrderInputDoc.createElement(XPXLiterals.E_LINE_PRICE_INFO);
				/*linePriceInfoElement.setAttribute(XPXLiterals.A_CURRENCY, SCXmlUtil.getXpathElement(itemDetailElement,
						"./PricingDetail/ListOfPrice/Price/UnitPrice/Currency/CurrencyCoded").getTextContent());*/
				linePriceInfoElement.setAttribute(XPXLiterals.A_CURRENCY, h_currencyCode);
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./UnitPrice")!=null)
				{
				linePriceInfoElement.setAttribute("UnitPrice", SCXmlUtil.getXpathElement(itemDetailElement,"./UnitPrice").getTextContent());
				linePriceInfoElement.setAttribute(XPXLiterals.A_IS_PRICE_LOCKED, XPXLiterals.BOOLEAN_FLAG_Y);
				}
				orderLineElement.appendChild(linePriceInfoElement);
				
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./LineNotes")!=null)
				{
				String lineInstructions = SCXmlUtil.getXpathElement(itemDetailElement,"./LineNotes").getTextContent();
				YFCElement orderLineInstructionsElement = createDraftOrderInputDoc.createElement(XPXLiterals.E_INSTRUCTIONS);
				YFCElement orderLineInstructionElement = createDraftOrderInputDoc.createElement("Instruction");
				/*orderLineInstructionElement.setAttribute("InstructionText", SCXmlUtil.getXpathElement(itemDetailElement,
						"./LineItemNote").getTextContent());*/
				orderLineInstructionElement.setAttribute("InstructionText",lineInstructions);
				orderLineInstructionElement.setAttribute("InstructionType", "LINE");
				orderLineInstructionsElement.appendChild(orderLineInstructionElement);
				orderLineElement.appendChild(orderLineInstructionsElement);
				}
				
				//New Mappings
				
				//YFCElement primaryInfoElement = createDraftOrderInputDoc.createElement("PrimaryInformation");
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./ManufacturerProductCode")!=null)
				{
				itemElement.setAttribute("ManufacturerItem", SCXmlUtil.getXpathElement(itemDetailElement,
				"./ManufacturerProductCode").getTextContent());
				}
				//itemElement.appendChild(primaryInfoElement);
				
				YFCElement itemExtnElement = createDraftOrderInputDoc.createElement("Extn");
				if(SCXmlUtil.getXpathElement(itemDetailElement,"./MasterProductCode")!=null)
				{
				itemExtnElement.setAttribute("ExtnSupplierNameDisplay", SCXmlUtil.getXpathElement(itemDetailElement,
				"./MasterProductCode").getTextContent()); 
				}
				itemElement.appendChild(itemExtnElement);
				
				orderLinesElement.appendChild(orderLineElement);
				
			}
			
			createDraftOrderInputDoc.getDocumentElement().appendChild(orderLinesElement);
		}
		else
		{
			if(log.isDebugEnabled()){
			log.debug("There are no items sent and hence no order will be created...");
			}
			
			YFSException exceptionMessage = new YFSException();
			exceptionMessage.setErrorDescription("No Items Sent in input xml...Exception thrown from XPXB2BOPTranslationAPI");
			log.error("Exception in XPXB2BOPTranslationAPI...as there are no items sent in the input xml");
			
			throw exceptionMessage;
		}
		
		    
		}
		catch(NullPointerException ne)
		{
			if(log.isDebugEnabled()){
			log.debug("Inside null pointer catch block in XPXB2BOrderTranslationAPI");
			}
                  	
	        log.error("Exception in XPXB2BOPTranslationAPI...NullPointerException");
			throw ne;
		}
		catch(YFSException yfe)
		{
			
			log.error("Exception in XPXB2BOPTranslationAPI...YFSException"+yfe.getErrorDescription());
			throw yfe;
		}
				
		
		
		setProgressYFSEnvironmentVariables(env);
		if(log.isDebugEnabled()){
		log.debug("The draft order creation input xml is: "+SCXmlUtil.getString(createDraftOrderInputDoc.getDocument()));
		}
		return createDraftOrderInputDoc.getDocument();
	}
	
	
	
	private void sendEmail(YFSEnvironment env, Document getBillToCustomerDetailsOutputDoc, String invalidEntity, Element inputXMLRoot, String sapCustOrgCode)
    {
		
		boolean isECSR1Empty = false;
		boolean isECSR2Empty = false;
				
		if(getBillToCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName("Customer").getLength() > 0)
		{
			//BillTo customer exists
			
			Element customerElement = (Element) getBillToCustomerDetailsOutputDoc.getDocumentElement().getElementsByTagName("Customer").item(0);
			Element extnElement = (Element) customerElement.getElementsByTagName("Extn").item(0);
			String eCSR1MailId = extnElement.getAttribute("ExtnECsr1EMailID");
			String eCSR2MailId = extnElement.getAttribute("ExtnECsr2EMailID");
			
			if(eCSR1MailId == null || eCSR1MailId.trim().length()<=0)
			{
				isECSR1Empty = true;
			}
			
			if(eCSR2MailId == null || eCSR2MailId.trim().length()<=0)
			{
				isECSR2Empty = true;
			}
						
			if(isECSR1Empty && isECSR2Empty)
			{
				log.debug("There are no To email addresses and hence mail will not be sent!!!");
			}
			else
			{
				//Prepare the input that needs to be sent out the email component
				   /* <RefOrder CSR1="" CSR2="" BuyerID="12345" ETradingID="12345" LiaisonMessageID="12345" InvalidEntity="item" OrderDate="20101124T14:10:07" 
					ShipToAddress1="1200 EL CAMINO REAL" ShipToAddress2="Mn. Junction" CustomerPO="123" ShipToCity="Cincin" ShipToState="Ohio" ShipToZIP="123" 
					ShipToCountryCode="US" LogoPath="D:/workspace/XpedxArtifacts/smcfs/Design/Templates/EMail Templates - Business Approved Content/xpedx_r_rgb_lo.jpg">
					<RefOrderLines>
					<RefOrderLine LegacyProductCode="123" LineDescription="FirstItem" RequestedOrderQuantity="4" RequestedUnitOfMeasure="EACH" 
					PriceUnitOfMeasure="EA" UnitPrice="10.00" POLineID="123" CustomerProductCode="123"/>
					<RefOrderLine LegacyProductCode="456" LineDescription="SecondItem" RequestedOrderQuantity="5" RequestedUnitOfMeasure="CTN" 
					PriceUnitOfMeasure="EA" UnitPrice="12.00" POLineID="123" CustomerProductCode="123"/>
					</RefOrderLines>
					</RefOrder>*/
				
				
				
				Document sendEmailInputDoc = YFCDocument.createDocument("RefOrder").getDocument();
				sendEmailInputDoc.getDocumentElement().setAttribute("ETradingID",SCXmlUtil.getXpathElement(inputXMLRoot, "./EtradingId").getTextContent());
				sendEmailInputDoc.getDocumentElement().setAttribute("BuyerID",SCXmlUtil.getXpathElement(inputXMLRoot, "./BuyerId").getTextContent());
				sendEmailInputDoc.getDocumentElement().setAttribute("InvalidEntity",invalidEntity);
				sendEmailInputDoc.getDocumentElement().setAttribute("CSR1",eCSR1MailId);
				sendEmailInputDoc.getDocumentElement().setAttribute("CSR2",eCSR2MailId);
				sendEmailInputDoc.getDocumentElement().setAttribute("LogoPath",YFSSystem.getProperty("ImagesRootFolder")+"/"+sapCustOrgCode+"_r_rgb_lo.jpg");
				if(SCXmlUtil.getXpathElement(inputXMLRoot, "./CustomerPO")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("CustomerPO",SCXmlUtil.getXpathElement(inputXMLRoot, "./CustomerPO").getTextContent());
				}
				
				if(SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderCreateDate")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("OrderDate",SCXmlUtil.getXpathElement(inputXMLRoot, "./OrderCreateDate").getTextContent());
				}				
				if(SCXmlUtil.getXpathElement(inputXMLRoot,	"./ShipToAddress1")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("ShipToAddress1",SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToAddress1").getTextContent());
				}
				
				if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToAddress2")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("ShipToAddress2",SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToAddress2").getTextContent());
				}
				
				if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToAddress3")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("ShipToAddress3",SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToAddress3").getTextContent());
				}
				
				
				if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToZIP")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("ShipToZIP",SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToZIP").getTextContent());
				}
				
				if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToCity")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("ShipToCity",SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToCity").getTextContent());
				}
				
				if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToState")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("ShipToState",SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToState").getTextContent());
				}
				if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToCountryCode")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("ShipToCountryCode",SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToCountryCode").getTextContent());
				}
				if(SCXmlUtil.getXpathElement(inputXMLRoot, "./LiaisonMessageId")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("LiaisonMessageID",SCXmlUtil.getXpathElement(inputXMLRoot, "./LiaisonMessageId").getTextContent());
				}
				
				if(SCXmlUtil.getXpathElement(inputXMLRoot,"./AttentionName")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("AttentionName",SCXmlUtil.getXpathElement(inputXMLRoot,"./AttentionName").getTextContent());
				}
				
				
				if(SCXmlUtil.getXpathElement(inputXMLRoot,"./OrderedByName")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("OrderedByName",SCXmlUtil.getXpathElement(inputXMLRoot,"./OrderedByName").getTextContent());
				}
				
				if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipDate")!=null)
				{
					//	 orderRequestedDeliveryDate = SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipDate").getTextContent();
					
					sendEmailInputDoc.getDocumentElement().setAttribute("OrderRequestedDeliveryDate",SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipDate").getTextContent());
				}
				
				
				if(SCXmlUtil.getXpathElement(inputXMLRoot,"./HeaderComments")!=null)
				{
				  // orderHeaderInstruction = SCXmlUtil.getXpathElement(inputXMLRoot,"./HeaderComments").getTextContent();
					
					sendEmailInputDoc.getDocumentElement().setAttribute("HeaderComments",SCXmlUtil.getXpathElement(inputXMLRoot,"./HeaderComments").getTextContent());
				}
				
				if(SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToName")!=null)
				{
					sendEmailInputDoc.getDocumentElement().setAttribute("ShipToName",SCXmlUtil.getXpathElement(inputXMLRoot,"./ShipToName").getTextContent());
				}
				
				Element refOrderLinesElement = sendEmailInputDoc.createElement("RefOrderLines");
				sendEmailInputDoc.getDocumentElement().appendChild(refOrderLinesElement);
				
				Element listOfItemDetailElement = (Element) inputXMLRoot.getElementsByTagName("LineItems").item(0);
				
				//NodeList itemDetailList = listOfItemDetailElement.getElementsByTagName("ItemDetail");
				NodeList itemDetailList = listOfItemDetailElement.getElementsByTagName("LineItem");
				
				if(itemDetailList.getLength()>0)
				{
					for(int i=0;i<itemDetailList.getLength(); i++)
					{
						Element itemDetailElement = (Element) itemDetailList.item(i);
						Element reforderLineElement = sendEmailInputDoc.createElement("RefOrderLine");
						
						if(SCXmlUtil.getXpathElement(itemDetailElement,"./UnitPrice")!=null)
						{
							reforderLineElement.setAttribute("UnitPrice", SCXmlUtil.getXpathElement(itemDetailElement,"./UnitPrice").getTextContent());
						}
						if(SCXmlUtil.getXpathElement(itemDetailElement,	"./PriceUnitOfMeasure")!=null)
						{
							reforderLineElement.setAttribute("PriceUnitOfMeasure", SCXmlUtil.getXpathElement(itemDetailElement,
						"./PriceUnitOfMeasure").getTextContent());
						}
						if(SCXmlUtil.getXpathElement(itemDetailElement,"./POLineID")!=null)
						{
							reforderLineElement.setAttribute("POLineID",SCXmlUtil.getXpathElement(itemDetailElement,"./POLineID").getTextContent());
						}
											
						if(SCXmlUtil.getXpathElement(itemDetailElement,"./LegacyProductCode")!=null)
						{
							reforderLineElement.setAttribute("LegacyProductCode",SCXmlUtil.getXpathElement(itemDetailElement,"./LegacyProductCode").getTextContent());
						}
						if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerProductCode")!=null)
						{
							reforderLineElement.setAttribute("CustomerProductCode",SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerProductCode").getTextContent());
						}
						if(SCXmlUtil.getXpathElement(itemDetailElement,"./RequestedUnitOfMeasure")!=null)
						{
							reforderLineElement.setAttribute("RequestedUnitOfMeasure",SCXmlUtil.getXpathElement(itemDetailElement,"./RequestedUnitOfMeasure").getTextContent());
						}
						if(SCXmlUtil.getXpathElement(itemDetailElement,"./RequestedOrderQuantity")!=null)
						{
							reforderLineElement.setAttribute("RequestedOrderQuantity", SCXmlUtil.getXpathElement(itemDetailElement,
						"./RequestedOrderQuantity").getTextContent());
						}
						if(SCXmlUtil.getXpathElement(itemDetailElement,"./LineDescription")!=null)
						{
							reforderLineElement.setAttribute("LineDescription", SCXmlUtil.getXpathElement(itemDetailElement,"./LineDescription").getTextContent());
						}
						
						if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerLineNumber")!=null)
					    {
							reforderLineElement.setAttribute("CustomerLineNo",SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerLineNumber").getTextContent());
					     
					    }
					    
						/*String customerLinePONumber = SCXmlUtil.getXpathElement(itemDetailElement,
						"./BaseItemDetail/ListOfItemReferences/ListOfReferenceCoded/ReferenceCoded/OriginalPurchaseOrder/PrimaryReference/Reference/RefNum").getTextContent();*/
					    if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerLinePONumber")!=null)
					    {
					    	reforderLineElement.setAttribute("CustomerLinePONumber",SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerLinePONumber").getTextContent());
						
					    }
						    
						 if(SCXmlUtil.getXpathElement(itemDetailElement,"./LineNotes")!=null){
							 reforderLineElement.setAttribute("LineNotes",SCXmlUtil.getXpathElement(itemDetailElement,"./LineNotes").getTextContent());
						   }
						 
						 if(SCXmlUtil.getXpathElement(itemDetailElement,"./ManufacturerProductCode")!=null){
							 reforderLineElement.setAttribute("ManufacturerItem", SCXmlUtil.getXpathElement(itemDetailElement,
							"./ManufacturerProductCode").getTextContent());
						 }
						 
						 if(SCXmlUtil.getXpathElement(itemDetailElement,"./MasterProductCode")!=null){
							 reforderLineElement.setAttribute("MasterProductCode",SCXmlUtil.getXpathElement(itemDetailElement,"./MasterProductCode").getTextContent());
						 }
						 if(SCXmlUtil.getXpathElement(itemDetailElement,"./LineRequestedDeliveryDate")!=null)
							{
							 reforderLineElement.setAttribute("LineRequestedDeliveryDate",SCXmlUtil.getXpathElement(itemDetailElement,"./LineRequestedDeliveryDate").getTextContent());
							 
							}
						
						 if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerUserField1")!=null)
							{							
								reforderLineElement.setAttribute("CustLineField1",SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerUserField1").getTextContent() );
							}
								
							if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerUserField2")!=null)
							{
								reforderLineElement.setAttribute("CustLineField2",SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerUserField2").getTextContent() );
							}
								
							if(SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerUserField3")!=null)
							{
								reforderLineElement.setAttribute("CustLineField3",SCXmlUtil.getXpathElement(itemDetailElement,"./CustomerUserField3").getTextContent() );
							}
						 
						refOrderLinesElement.appendChild(reforderLineElement);
					}	
					
				}			
				
				try
                {
					api.executeFlow(env, "XPXSendBilltoECSRMails", sendEmailInputDoc);
					
				} catch (YFSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*//MessageHeader FromAddress="" MailHost="" MailPort="" MailProtocol="" SubjectText="" TemplateFile="">
            Element messageHeader  = sendMailInputDoc.createElement("MessageHeader");
            messageHeader.setAttribute("FromAddress", YFSSystem.getProperty("EMailFromAddresses"));
            messageHeader.setAttribute("MailHost",YFSSystem.getProperty("EMailServer"));
            messageHeader.setAttribute("MailPort","25");
            messageHeader.setAttribute("SubjectText","Error Notification Mail For Invalid "+""+invalidEntity);
            messageHeader.setAttribute("TemplateFile","/global/template/email/InvalidETradingIDMail.xsl");*/
		}
		
	}



	private Document getBillToCutomerDetailsOutput(YFSEnvironment env, String billToId, String sapCustOrgCode) throws YFSException, RemoteException
	{
		Document getCustomerDetailsOutputDoc = null;
		
        YFCDocument getCustomerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
        getCustomerDetailsInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, sapCustOrgCode);
        getCustomerDetailsInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, billToId);
        
        env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
		getCustomerDetailsOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerDetailsInputDoc.getDocument());
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
	
		
		return getCustomerDetailsOutputDoc;
	}



	private String getLegacyItemNumberFromXref(YFSEnvironment env, String customerPartNo, String envtId, String companyCode, String legacyCustomerNumber) {

		Document getItemXRefListOutputDoc = null;
		String legacyItemNumber = null;
		
         Document getItemXRefListDoc = YFCDocument.createDocument("XPXItemcustXref").getDocument();
         
         getItemXRefListDoc.getDocumentElement().setAttribute(XPXLiterals.A_COMPANY_CODE, companyCode);
         getItemXRefListDoc.getDocumentElement().setAttribute("EnvironmentCode", envtId);
         getItemXRefListDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_NO,legacyCustomerNumber);
         getItemXRefListDoc.getDocumentElement().setAttribute("CustomerItemNumber", customerPartNo);
         
         try {
        	getItemXRefListOutputDoc = api.executeFlow(env, XPXLiterals.GET_XREF_LIST, getItemXRefListDoc);
        	if(log.isDebugEnabled()){
			log.debug("The output of getXrefList is: "+SCXmlUtil.getString(getItemXRefListOutputDoc));
        	}
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
		if(getItemXRefListOutputDoc != null)
		{		
		Element XREFOutputDocRoot = getItemXRefListOutputDoc.getDocumentElement();
		
		NodeList XREFCustElementList = XREFOutputDocRoot.getElementsByTagName(XPXLiterals.E_XPX_ITEM_CUST_XREF);
		
		if(XREFCustElementList.getLength() > 0)
		{
			
			        Element XREFElement = (Element) XREFCustElementList.item(0);
			        
			        legacyItemNumber = XREFElement.getAttribute("LegacyItemNumber");
			        if(log.isDebugEnabled()){
			        log.debug("The legacy item number is: "+legacyItemNumber);
			        }
			        			        	
		}
		
	    }         

		return legacyItemNumber;
		
	}



	private Document getSAPCustomerDetailsOutput(YFSEnvironment env, String buyerId) {

		Document getCustomerDetailsOutputDoc = null;
		
        YFCDocument getCustomerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
        YFCElement extnElement = getCustomerDetailsInputDoc.createElement(XPXLiterals.E_EXTN);
        extnElement.setAttribute("ExtnBuyerID", buyerId);
        
        getCustomerDetailsInputDoc.getDocumentElement().appendChild(extnElement);

         
		   try {
			   env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
			getCustomerDetailsOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerDetailsInputDoc.getDocument());
			env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getCustomerDetailsOutputDoc;
		
		
	}



	private Document getCustomerDetailsOutput(YFSEnvironment env, String eTradingId, String sapCustOrgCode) {
 
		   Document getCustomerDetailsOutputDoc = null;
				
           YFCDocument getCustomerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
           getCustomerDetailsInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, sapCustOrgCode);
           YFCElement extnElement = getCustomerDetailsInputDoc.createElement(XPXLiterals.E_EXTN);           
           extnElement.setAttribute("ExtnETradingID", eTradingId);
           
           getCustomerDetailsInputDoc.getDocumentElement().appendChild(extnElement);

		   try {
			   env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
			getCustomerDetailsOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerDetailsInputDoc.getDocument());
			
			if(log.isDebugEnabled()){
			  log.debug("GETCustomerDetailsOutputDoc::"+SCXmlUtil.getString(getCustomerDetailsOutputDoc));
			}
			  
			  env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
			
			
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getCustomerDetailsOutputDoc;
		
	}
	
	/* Start - changes made for CR 853 */
	
	public void updateInvalidFlag(YFSEnvironment env,String refOrderHdrKey,String refOrderLineKey,String flag)
	throws Exception {
		
		if(refOrderLineKey != null && !refOrderLineKey.isEmpty() && 
				refOrderHdrKey != null && !refOrderHdrKey.isEmpty()){		
		Document refOrderOutputDoc;
		Document refOrderInputDoc = YFCDocument.createDocument("XPXRefOrderHdr").getDocument();
		Element xpxRefOrderHdrElement = refOrderInputDoc.getDocumentElement();
		xpxRefOrderHdrElement.setAttribute("RefOrderHdrKey", refOrderHdrKey);
		Element XPXRefOrderLineListElement = refOrderInputDoc.createElement("XPXRefOrderLineList");
		Element XPXRefOrderLineElement = refOrderInputDoc.createElement("XPXRefOrderLine");
		xpxRefOrderHdrElement.appendChild(XPXRefOrderLineListElement);
		XPXRefOrderLineListElement.appendChild(XPXRefOrderLineElement);
		
		XPXRefOrderLineElement.setAttribute("RefOrderLineKey", refOrderLineKey);
		// flag denotes which attribute needs to be added in the input xml.
		if(flag != null){
			if(flag.equalsIgnoreCase("UOM")){
			XPXRefOrderLineElement.setAttribute("IsInvalidUOM", "Y");	
			}else{
			XPXRefOrderLineElement.setAttribute("IsInvalidItemID", "Y");
			}
		}
		if(log.isDebugEnabled()){
		log.debug("refOrderInputDoc = " + SCXmlUtil.getString(refOrderInputDoc));
		}
			// Call to save the invalid item flag in the xpx_ref_order_line table.
			try{
			refOrderOutputDoc = api.executeFlow(env, "changeXPXRefOrderHdr", refOrderInputDoc);
			if(log.isDebugEnabled()){
			log.debug("outputDoc = " + SCXmlUtil.getString(refOrderOutputDoc));
			}
			}catch(Exception e){
				// Error logged in CENT tool.
				YFSException exceptionMessage = new YFSException();
				exceptionMessage.setErrorDescription("Exception occured in calling service 'changeXPXRefOrderHdr' - XPXB2BOrderTranslationAPI.java");		
				log.error("Exception occured in calling service 'changeXPXRefOrderHdr' - XPXB2BOrderTranslationAPI.java");				
				throw exceptionMessage;	
			}
		}else{
			// Error logged in CENT tool.
			YFSException exceptionMessage = new YFSException();
			exceptionMessage.setErrorDescription("Reference Order Header / Ref Order line key is empty. ");		
			log.error("Reference Order Header / Ref Order line key is empty.");				
			throw exceptionMessage;
		}
	}
	
	// Gets the Reference Order Line key from the transaction object.
    public String getRefOrdLineKey(YFSEnvironment env,String lineLiaisonMessageId){
    	
    	Map refOrderLineMap = new HashMap();
    	String refOrderLineKey = "";
    	try{
	    	refOrderLineMap = (HashMap) env.getTxnObject("refOrderLineMap");
			if(lineLiaisonMessageId != null){
			refOrderLineKey = refOrderLineMap.get(lineLiaisonMessageId).toString();
			}
    		}catch(Exception e){
			// Error logged in CENT tool.
			YFSException exceptionMessage = new YFSException();
			exceptionMessage.setErrorDescription("Exception occured while retrieving 'refOrderLineMap' from transaction object... XPXB2BOrderTranslationAPI.java");		
			log.error("Exception occured while retrieving 'refOrderLineMap' from transaction object... XPXB2BOrderTranslationAPI.java");				
			throw exceptionMessage;
		}
		return refOrderLineKey;
    }
	
	
	/* End - changes made for CR 853 */
	
    private void setProgressYFSEnvironmentVariables(YFSEnvironment env) {
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				envVariablesmap.put("isDiscountCalculate", "true");
				envVariablesmap.put("isPnACall", "false");
			}
			else{
				envVariablesmap=new HashMap();
				envVariablesmap.put("isDiscountCalculate", "true");
				envVariablesmap.put("isPnACall", "false");
			}
			
			clientVersionSupport.setClientProperties(envVariablesmap);
		}
	}

	public void setProperties(Properties arg0) throws Exception
	{
		// TODO Auto-generated method stub
		
	}

}

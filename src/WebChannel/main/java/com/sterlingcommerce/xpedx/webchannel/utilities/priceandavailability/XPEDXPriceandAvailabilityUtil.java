/**
 * This util class gets the PriceAndAvailability input Object and returs the output Object
 */
package com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONObject;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.tools.ant.filters.StringInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.util.YFCCommon;

/**
 * @author rugrani
 * 
 */
public class XPEDXPriceandAvailabilityUtil {

	/*	Commented for Jira #2316, May comeback for more specific messages.
 	public static final String WS_PRICEANDAVAILABILITY_WITH_SERVICESTATUSDOWN_ERROR = "Sorry, real time price and availability is not available at this time, please contact customer service.";
	public static final String WS_PRICEANDAVAILABILITY_TRANSMISSIONSTATUS_ERROR = "Sorry, there was a problem processing your request.  Please contact customer service or try again later.";
	public static final String WS_PRICEANDAVAILABILITY_HEADERSTATUS_ERROR = "Sorry, we could not verify your account information, please contact your customer service representative for price and availability.";
	 
	public static final String WS_PRICEANDAVAILABILITY_LINESTATUS_ERROR = "Sorry, the item information for this item is not valid.  Please contact customer service for price and availability."; 
	public static final String WS_PRICEANDAVAILABILITY_LINESTATUS_ERROR = "One or more items may not have price or availability, please try again or contact customer service.";
	public static final String WS_PRICEANDAVAILABILITY_LINESTATUS_ERROR = "Price & Avaialbility is not available, please try again or contact customer service.";
	*/
	
	private static final String customerExtnMashUp= "xpedx-customer-getCustomerAllExtnInformation";
	
	public static final String TH_UOM_A = "A_M";
	
	public static final String TH_UOM_M = "M_M";
	
	public static final String CWT_UOM_A = "A_CWT";
	
	public static final String CWT_UOM_M = "M_CWT";
	
	public static final int DECIMAL_PRECISION_NEEDED = 6;
	
	
	
	public static int PANDA_WS_AUTO_RESPONSE = 1;
	public static int PANDA_WS_DUMMY_RESPONSE = 2;
	public static int PANDA_WS_PROD = 0;
	
	public static int PANDA_WS_MODE = PANDA_WS_PROD ;
	
	//Start- Fix for 3105
	public static final String WS_PRICEANDAVAILABILITY_WITH_SERVICESTATUSDOWN_ERROR = "Real time My Price & Availability is not available at this time. Please contact Customer Service.";
	//changed the PnA messages from max specific error messages to generic error message for jira 3707
	public static final String WS_PRICEANDAVAILABILITY_TRANSMISSIONSTATUS_ERROR = "Real time My Price & Availability is not available at this time. Please contact Customer Service.";//"Sorry, there was a problem processing your request. Please contact customer service or try again later.";Sorry, Price & Availability is not available, please try again or contact customer service.";
	public static final String WS_PRICEANDAVAILABILITY_HEADERSTATUS_ERROR = "Real time My Price & Availability is not available at this time. Please contact Customer Service.";//"Sorry, we could not verify your account information, please contact your customer service representative for price and availability.";"Sorry, Price & Availability is not available, please contact customer service.";
	public static final String WS_PRICEANDAVAILABILITY_LINESTATUS_ERROR = "Real time My Price & Availability is not available at this time. Please contact Customer Service.";//"Sorry, the item information for this item is not valid. Please contact customer service for price and availability.";
	//end of changes for jira 3707		
	public static final String WS_PRICEANDAVAILABILITY_OUTPUT_XMLDOC_WITH_SERVICESTATUSDOWN_ERROR = "WSPriceAndAvailabilityOutputXmldocWithServiceStatusDownError.xml";
	public static final String WS_PRICEANDAVAILABILITY_OUTPUT_XMLDOC_WITH_TRANSMISSIONSTATUS_ERROR = "WSPriceAndAvailabilityOutputXmldocWithTransmissionStatusError.xml";
	public static final String WS_PRICEANDAVAILABILITY_OUTPUT_XMLDOC_WITH_HEADERSTATUS_ERROR = "WSPriceAndAvailabilityOutputXmldocWithHeaderStatusError.xml";
	public static final String WS_PRICEANDAVAILABILITY_OUTPUT_XMLDOC_WITH_LINESTATUS_ERROR = "WSPriceAndAvailabilityOutputXmldocWithLineStatusError.xml";

	
	
	/**
	 * Purpose of the method:1234960438
	 * 1] Calling PNA webservice
	 * 2] Handling errors and corresponding messages
	 * 3] Use Dummy Webservice call, Manipulate response for creating different scenarios.
	 * 4] Finally Creating XPEDXPriceAndAvailability Object
	 * 
	 * @param inputItems
	 * @return
	 */
	public static XPEDXPriceAndAvailability getPriceAndAvailability(
			ArrayList<XPEDXItem> inputItems) {
		XPEDXPriceAndAvailability pnaOutput = new XPEDXPriceAndAvailability();
		if (null == inputItems || inputItems.size() <= 0) {
			log.debug("getPriceAndAvailability(): Item list is empty... Cannot call the service...");
			return pnaOutput;
		}
		
		Document inputDoc = prepareInputDoc(inputItems);
		String inputXML = SCXmlUtil.getString(inputDoc);
		log.debug("getPriceAndAvailability: inputXML for P&A Webservice: "
				+ inputXML);
		String displayErrorMsgToUser = "";
		Document outputDoc = null;
		
		// TODO: Call the actual service
		try{
					if( PANDA_WS_MODE == PANDA_WS_AUTO_RESPONSE ){		//In case Auto it takes legacy number from input and sends the response
							outputDoc = getPandADummyAutoResponse(  inputXML );	
					}
					else if( PANDA_WS_MODE == PANDA_WS_DUMMY_RESPONSE  ){	//Response based on DummyXML
							outputDoc = getDummyOutPutDoc( ); 
					}
					else { //Makes real P&A Call needed for Prod environment
						outputDoc = XPEDXCallPnAService(inputDoc);
					}
			
			}catch (Exception e){
				displayErrorMsgToUser = WS_PRICEANDAVAILABILITY_WITH_SERVICESTATUSDOWN_ERROR  ; 
			}
		
		if(null == outputDoc){
			
			log.error("PnA did not respond or it sent a Empty xml back.");		
			//added for jira 3974 : error message for delayed PnA response
			displayErrorMsgToUser = "Your request has timed out. Please try again.";
			pnaOutput.setStatusVerboseMsg(displayErrorMsgToUser);
			//end of jira 3974
			return pnaOutput;
			
		} else {	
		
			//Return Response valid and proceed further processing 			
			pnaOutput = new XPEDXPriceAndAvailability();
			String responseXML = SCXmlUtil.getString(outputDoc);
			log.debug("getPriceAndAvailability: response for P&A Webservice: " + responseXML);
			
			displayErrorMsgToUser = handlePandAWebServiceResponseSatuseses(outputDoc  , pnaOutput , displayErrorMsgToUser);
						
			//Check for the TransactionStatus
			if ( "F".equalsIgnoreCase(pnaOutput.getTransactionStatus() ) ) {
				return pnaOutput;
			}
			
			//Fill the pnaOutput with java objects generated after parsing respXML.
			pnaOutput = setBusinessObjects(outputDoc);
			pnaOutput.setStatusVerboseMsg(displayErrorMsgToUser);
		}
		return pnaOutput;
	}
	
	
	public static XPEDXPriceAndAvailability getPriceAndAvailability(IWCContext wcContext, Element ueAdditionalAttrElem) {
		XPEDXPriceAndAvailability pnaOutput = new XPEDXPriceAndAvailability();
		
		/*Begin - Changes made by Mitesh Parikh for JIRA#3595*/		
		/*HashMap<String, String> valueMap = new HashMap<String, String>();
		
		valueMap.put("/XPXUeAdditionalAttrXml/@OrderHeaderKey", orderHeaderKey);
		valueMap.put("/XPXUeAdditionalAttrXml/@XMLType", "PNA");

		Element input=null;
		try {
			input = WCMashupHelper.getMashupInput(
					"xpedx_get_pna_responsexml", valueMap, wcContext
							.getSCUIContext());
		} catch (CannotBuildInputException e) {
			// Error in invoking mashup
			//return custInfoMap;
		}
		Object obj = WCMashupHelper.invokeMashup(
				"xpedx_get_pna_responsexml", input, wcContext
						.getSCUIContext());*/
		/*End - Changes made by Mitesh Parikh for JIRA#3595*/
		Document outputDoc = null;
		String displayErrorMsgToUser = "";
		String pnaXML="";
		if(ueAdditionalAttrElem != null)
		{
			//Element allChildDoc = ((Element) obj);
			//String pnaXML=allChildDoc.getAttribute("ResponseXML");
			
			pnaXML=ueAdditionalAttrElem.getAttribute("ResponseXML");
			
			if(pnaXML != null)
			{
				outputDoc = SCXmlUtil.createFromString(pnaXML);
			
			}	
		}
		if(null == outputDoc){
			//jira 2885
			displayErrorMsgToUser = WS_PRICEANDAVAILABILITY_WITH_SERVICESTATUSDOWN_ERROR  ; 
			pnaOutput.setStatusVerboseMsg(displayErrorMsgToUser);
			log.error("PnA did not respond or it sent a Empty xml back.");		
			return pnaOutput;
			
		} else {	
		
			//Return Response valid and proceed further processing 			
			pnaOutput = new XPEDXPriceAndAvailability();
			//String responseXML = SCXmlUtil.getString(outputDoc);
			log.debug("getPriceAndAvailability: response for P&A Webservice: " + pnaXML);
			
			//Use this for testing different scenarios
			//outputDoc  = manipulateXML ( responseXML );
			
			displayErrorMsgToUser = handlePandAWebServiceResponseSatuseses(outputDoc  , pnaOutput , displayErrorMsgToUser);
						
			//Check for the TransactionStatus
			if ( "F".equalsIgnoreCase(pnaOutput.getTransactionStatus() ) ) {
				return pnaOutput;
			}
			
			//Fill the pnaOutput with java objects generated after parsing respXML.
			pnaOutput = setBusinessObjects(outputDoc);
			pnaOutput.setStatusVerboseMsg(displayErrorMsgToUser);
		}
		return pnaOutput;
	}
	
	

	/*
	 * Process Error handling for PriceAndAvailability Response Doc
	 * 
	 * Handle Errors for scenarios like 
	 * 		a] Exception like ServiceDown
	 * 		b] TransactionStatus
	 * 		c] HeaderStatusCode
	 * 		d] LineItem level 'LineStatusCode'
	 * 
	 */
	public static String handlePandAWebServiceResponseSatuseses (Document outputDoc, XPEDXPriceAndAvailability pna, String displayErrorMsgToUser ) {
		
		displayErrorMsgToUser = "";
		
		//Check for validity of the responseDoc and set appropriate message 
		if (outputDoc == null ) {			
			displayErrorMsgToUser = WS_PRICEANDAVAILABILITY_WITH_SERVICESTATUSDOWN_ERROR ;
			pna.setStatusVerboseMsg(displayErrorMsgToUser);
			return displayErrorMsgToUser;
		}
		
		//TransactionStatus
		Element transactionStatusElement = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "TransactionStatus");
		if(transactionStatusElement != null )	{	
			String transactionStatusCode = transactionStatusElement.getTextContent();
			if(YFCCommon.isVoid(transactionStatusCode) || transactionStatusCode.equalsIgnoreCase("F")){
				log.error("PnA TransactionStatus failure.Returning empty PnA Business object");
				pna.setTransactionStatus("F");
				
				displayErrorMsgToUser = WS_PRICEANDAVAILABILITY_TRANSMISSIONSTATUS_ERROR  ; 
			}
		}
		
		//HeaderStatusCode
		Element headerStatusElement = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "HeaderStatusCode");
		if(headerStatusElement != null )	{	
			String headerStatusCode = headerStatusElement.getTextContent();
			if(YFCCommon.isVoid(headerStatusCode) || !headerStatusCode.equalsIgnoreCase("00")){
				log.error("PnA HeaderStatusCode failure.Indicates bad data from Sterling.Returning empty PnA Business object");
				pna.setHeaderStatusCode ( headerStatusCode );
				displayErrorMsgToUser = WS_PRICEANDAVAILABILITY_HEADERSTATUS_ERROR  ; 
			}
			/* (commented for jira 2885 as there is a seperate method for setting lineStatus error msg) else
			{
				//LineStatusCode
				String lineStatusCodeCode = SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/PriceAndAvailability/Items/Item/LineStatusCode");
			

				if(YFCCommon.isVoid(lineStatusCodeCode) || !lineStatusCodeCode.equalsIgnoreCase("00")){
					displayErrorMsgToUser = WS_PRICEANDAVAILABILITY_LINESTATUS_ERROR  ; 
					pna.setStatusVerboseMsg(displayErrorMsgToUser);
					
				}
			}*/
		}
		
		
		//set message to on pna Object
		pna.setStatusVerboseMsg(displayErrorMsgToUser);
		
		return displayErrorMsgToUser;
	}

	
	/**
	 * AutoDummy response for debug purpose (Purma) Helpful for debug on local system.
	 * 
	 * @param inputXML
	 * @return
	 */
	static Document getPandADummyAutoResponse (String inputXML) {
		
		String legacyProdCode = StringUtils.substringBetween(inputXML, "<LegacyProductCode>", "</LegacyProductCode>")	;
		if(log.isDebugEnabled()){
		log.debug( "inputXML : " + PANDA_WS_AUTO_RESPONSE + " : \n" +  inputXML);
		}
		StringBuffer sb =  new StringBuffer();
		
		sb.append("<?xml version='1.0' encoding='utf-8'?>" );
		sb.append("<PriceAndAvailability>" );
		sb.append("<TransactionStatus>Success</TransactionStatus>" );
		sb.append("<EnvironmentId/>" );
		sb.append("<Company>Sterling Commerce</Company>" );
		sb.append("<CustomerBranch/>" );
		sb.append("<CustomerNumber/>" );
		sb.append("<ShipToSuffix/>" );
		sb.append("<OrderBranch/>" );
		sb.append("<HeaderStatusCode>00</HeaderStatusCode>" );
				sb.append("<Items>" );
					sb.append("<Item>" );
					sb.append("<LineNumber>123</LineNumber>" );
					sb.append("<LegacyProductCode>" + legacyProdCode +"</LegacyProductCode>" );
					sb.append("<RequestedQtyUOM>M_BAG</RequestedQtyUOM>" );
					sb.append("<RequestedQty>1</RequestedQty>" );
					sb.append("<PurchaseOrderQty/>" );
					sb.append("<PricingUOM>CASE</PricingUOM>" );
					sb.append("<PriceCurrencyCode/>" );
					sb.append("<UnitPricePerPricingUOM>1560.20</UnitPricePerPricingUOM>" );
					sb.append("<UnitPricePerRequestedUOM>175.0712</UnitPricePerRequestedUOM>" );
					sb.append("<ExtendedPrice>1879.00</ExtendedPrice>" );
					sb.append("<ItemCost>29.87</ItemCost>" );
					sb.append("<CostCurrencyCode/>" );
					sb.append("<Brackets>" );
						sb.append("<Bracket>" );
							sb.append("<BracketQTY>23</BracketQTY>" );
							sb.append("<BracketUOM>EA</BracketUOM>" );
							sb.append("<BracketPrice>56.78</BracketPrice>" );
						sb.append("</Bracket>" );
					sb.append("</Brackets>" );
					sb.append("<WarehouseLocationList>" );
						sb.append("<WarehouseLocation>" );
						sb.append("<Warehouse>Gangolli</Warehouse>" );
						sb.append("<AvailableQty>344</AvailableQty>" );
						sb.append("<NumberOfDays>67</NumberOfDays>" );
					sb.append("</WarehouseLocation>" );
				sb.append("</WarehouseLocationList>" );
		             sb.append("<LineStatusCode>00</LineStatusCode>" );
		       sb.append("</Item>" );
		     sb.append("</Items>" );
		sb.append("</PriceAndAvailability>" );
				
				
		Document newDocument = SCXmlUtils.createFromString(sb.toString());
	return newDocument;
	}
	
	public static Document XPEDXCallPnAService(Document inputDoc) {
		Document pnaResponse = null;
		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		try {
			Object obj = WCMashupHelper.invokeMashup("xpedxPnAWebService",
					inputDoc.getDocumentElement(), wcContext.getSCUIContext());
			pnaResponse = ((Element) obj).getOwnerDocument();
		} catch (Exception e) {
			log.error("Error calling the PnA Webservice. ", e);
			pnaResponse = null;
		}
		return pnaResponse;
	}

	/**
	 * JIRA 243
	 * Modified getCustomerDetails method to consider the mashup to be invoked
	 * so that, we get only the required information - here Extn field only.
	 * @param inputItems
	 * @return
	 */
	private static Document prepareInputDoc(ArrayList<XPEDXItem> inputItems) {
		Document returnDoc = null;
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		Document customerDoc = null;
		try {
			customerDoc = XPEDXWCUtils.getCustomerDetails(context
					.getCustomerId(), context.getStorefrontId(), customerExtnMashUp);
		} catch (CannotBuildInputException e) {
			log
					.error("prepareInputDoc: Not able to get customer details....Returning empty doc:"
							+ e);
			return returnDoc;
		}
		if (null == customerDoc) {
			log
					.debug("prepareInputDoc: Empty customer doc....Returning empty doc");
			return returnDoc;
		}
		returnDoc = SCXmlUtil
				.createFromString("<PriceAndAvailability><Items></Items></PriceAndAvailability>");
		Element custExtn = SCXmlUtil.getChildElement(customerDoc
				.getDocumentElement(), "Extn");
		String envId = custExtn.getAttribute("ExtnEnvironmentCode");
		String custEnvId = custExtn.getAttribute("ExtnOrigEnvironmentCode");
		String Company = custExtn.getAttribute("ExtnCompanyCode");
		String CustomerBranch = custExtn.getAttribute("ExtnCustomerDivision");
		String ShipToSuffix = custExtn.getAttribute("ExtnShipToSuffix");
		String OrderBranch = !YFCCommon.isVoid(custExtn
				.getAttribute("ExtnCustOrderBranch")) ? custExtn
				.getAttribute("ExtnCustOrderBranch") : custExtn
				.getAttribute("ExtnShipFromBranch");
		String customerNumber = custExtn.getAttribute("ExtnLegacyCustNumber");
		addXMLTag(returnDoc, "SourceIndicator", "1");// 1 for WebChannel
		addXMLTag(returnDoc, "EnvironmentId", envId);
		addXMLTag(returnDoc, "CustomerEnvironmentId", custEnvId);
		addXMLTag(returnDoc, "Company", Company);
		addXMLTag(returnDoc, "CustomerBranch", CustomerBranch);
		addXMLTag(returnDoc, "CustomerNumber", customerNumber);
		addXMLTag(returnDoc, "ShipToSuffix", ShipToSuffix);
		addXMLTag(returnDoc, "OrderBranch", OrderBranch);
		NodeList inputNodeList = returnDoc.getElementsByTagName("Items");
		Element inputNodeListElemt = (Element) inputNodeList.item(0);
		for (XPEDXItem item : inputItems) {
			Document itemDoc = YFCDocument.createDocument("Item").getDocument();
			Element itemElement = itemDoc.getDocumentElement();
			addXMLTag(itemDoc, "LineNumber", item.getLineNumber());
			addXMLTag(itemDoc, "LegacyProductCode", item.getLegacyProductCode());
			addXMLTag(itemDoc, "RequestedQtyUOM", item.getRequestedQtyUOM());
			addXMLTag(itemDoc, "RequestedQty", item.getRequestedQty());
			inputNodeListElemt.appendChild(returnDoc.importNode(itemElement,
					true));
		}
		if(log.isDebugEnabled()){
		log.debug(SCXmlUtil.getString(returnDoc));
		}
		return returnDoc;
	}

	private static void addXMLTag(Document doc, String tagName, String tagValue) {
		Element eleRoot = doc.getDocumentElement();
		Element textNode = SCXmlUtil.createChild(eleRoot, tagName);
		Text txt = doc.createTextNode(tagName);
		/*Begin - Changes made for JIRA 3969*/
		if(tagValue==null)
		{
			tagValue="";		
		}
		/*End - Changes made for JIRA 3969*/
		txt.setTextContent(tagValue);
		textNode.appendChild(txt);
	}

	
	
	/**
	 * @param outputDoc
	 * @return
	 */
	private static XPEDXPriceAndAvailability setBusinessObjects(
			Document outputDoc) {
		
		XPEDXPriceAndAvailability output = null;
		
		if (null == outputDoc) {
			log
					.error("setBusinessObjects(): Empty request received. Returning empty object!");
			return output;
		}
		URL ruleXMLURL = XPEDXPriceandAvailabilityUtil.class
				.getClassLoader()
				.getResource(
						"/global/template/resource/extn/XPEDXPriceaAndAvailabilityOutputDigester.xml");
		if (null == ruleXMLURL) {
			log
					.error("setBusinessObjects(): Unable to locate XPEDXPriceaAndAvailabilityOutputDigester.xml in resources.jar. Returning empty object!");
			return output;
		}
		log.debug("The XML rule loaded to memory: " + ruleXMLURL.getPath());
		try {
			Digester digester = DigesterLoader.createDigester(ruleXMLURL);
			digester.setValidating(false);
			output = (XPEDXPriceAndAvailability) digester
					.parse(new StringInputStream(SCXmlUtil.getString(outputDoc)));
			//added for jira 2885
			Vector<XPEDXItem> items=output.getItems(); 
			if(items !=null)
			{
				for(int i=0;i<items.size();i++)
				{
					XPEDXItem item=items.get(i);
					if(!"00".equals(item.getLineStatusCode()))
						//commented for jira 3707 item.setLineStatusErrorMsg(WS_PRICEANDAVAILABILITY_LINESTATUS_ERROR +"  "+getPnALineErrorMessage(item));
						item.setLineStatusErrorMsg(WS_PRICEANDAVAILABILITY_LINESTATUS_ERROR);
				}
			}
		} catch (MalformedURLException e) {
			log
					.error("Unable to create XPEDXPriceAndAvailability bean. Unable to locate rule file XPEDXPriceaAndAvailabilityOutputDigester.xml ...URL:"
							+ ruleXMLURL.toString() + " \n" + e);
		} catch (IOException e) {
			log
					.error("Unable to create XPEDXPriceAndAvailability bean. IOException while parsing the incoming xml ...\n XML: "
							+ outputDoc.toString() + " \n" + e);
		} catch (SAXException e) {
			log
					.error("Unable to create XPEDXPriceAndAvailability bean. Unable to parse the incoming xml ...\n XML: "
							+ outputDoc.toString() + " \n" + e);
		}
		return output;
	}
	
	public static HashMap<String, JSONObject> getPnAHoverMap(
			Vector<XPEDXItem> items) {
		
		return getPnAHoverMap(items,false);
	}
	
	public static HashMap<String, JSONObject> getPnAHoverMap(
			Vector<XPEDXItem> items,boolean isLineNumberRequired) {
		HashMap<String, JSONObject> pnaHoverDisplayMap = new HashMap<String, JSONObject>();
		if (null == items || items.size() <= 0) {
			return pnaHoverDisplayMap;
		}		
		for (XPEDXItem item : items) {
			log.debug("Start Creating the JSON object for "+ item.getLegacyProductCode());
			Vector wareHouseList = item.getWarehouseLocationList();
			XPEDXWarehouseLocation wareHouseItem = null;
			// changed datatype to double from integer for jira 3201
			Double totalForImmediate = new Double(0);
			Double totalForNextDay = new Double(0);
			Double totalForTwoPlus = new Double(0);
			Double toalAvailable = new Double(0);
			for (Object wareHouse : wareHouseList) {
				wareHouseItem = (XPEDXWarehouseLocation) wareHouse;
				String availQtyStr = wareHouseItem.getAvailableQty();
				if(YFCCommon.isVoid(availQtyStr)){
					log.error("Empty available quantity for "+item.getLegacyProductCode()+" in warehouse "+wareHouseItem.getWarehouse());
					continue;
				}
				String noOfDaysString = wareHouseItem.getNumberOfDays();
				if(YFCCommon.isVoid(noOfDaysString) || !isANumber(noOfDaysString)){
					log.error("Empty or Corrupt NumberOfDays for "+item.getLegacyProductCode()+" in warehouse "+wareHouseItem.getWarehouse());
					continue;
				}
				//Integer availQtyFloat; modified for jira 3201
				Double availQtyFloat;
				try{
					//modified for jira 3201
					availQtyFloat = new BigDecimal(availQtyStr.trim()).doubleValue();
				}catch (Exception e) {
					log.error("Corrupt available Qty for item "+item.getLegacyProductCode()+" in warehouse "+wareHouseItem.getWarehouse());
					availQtyFloat = new Double(0);
				}
				int noOfDays = Integer.parseInt(wareHouseItem.getNumberOfDays());
				if (noOfDays == 0) {
					//Immediate
					totalForImmediate += availQtyFloat;
				} else if (noOfDays == 1) {
					//Next day
					totalForNextDay += availQtyFloat;
				} else {
					//2+ days
					totalForTwoPlus += availQtyFloat;
				}
			}
			// Prepare the JSON Object
			toalAvailable = totalForImmediate + totalForNextDay
					+ totalForTwoPlus;
			//Fix for JIRA issue 1306
			//Availability For next day = Availability for today + Availability for next day
			totalForNextDay += totalForImmediate;
			JSONObject jsonData = new JSONObject();
			
			String requestedQtyStr = item.getRequestedQty();
			if(YFCCommon.isVoid(requestedQtyStr)){
				log.error("Empty requested Qty for "+item.getLegacyProductCode());
				requestedQtyStr = "1";
			}
			Integer requestedQtyFloat;
			try{
				requestedQtyFloat = new BigDecimal(requestedQtyStr.trim()).intValue();
			}catch (Exception e) {
				log.error("Corrupt requested Qty for item "+item.getLegacyProductCode());
				requestedQtyFloat = new Integer(0);
			}
			
			if (requestedQtyFloat <= totalForImmediate) {
				jsonData.put("Availability", "Immediate");
			} else if (requestedQtyFloat <= totalForNextDay) {
				jsonData.put("Availability", "Next Day");
			} else if (requestedQtyFloat <= totalForTwoPlus) {
				jsonData.put("Availability", "2+ days");
			}else if(toalAvailable > 0){
				jsonData.put("Availability", "Availability");
			}

			//"".toLowerCase().contains("b");
			jsonData.put("UOM", item.getRequestedQtyUOM());
			if (totalForImmediate > 0) {
				jsonData.put("Immediate", totalForImmediate.toString());
			}
			if (totalForNextDay > 0) {
				jsonData.put("NextDay", totalForNextDay.toString());
			}
			if (totalForTwoPlus > 0) {
				jsonData.put("TwoPlusDays", totalForTwoPlus.toString());
			}
			jsonData.put("Total", toalAvailable.toString());

			jsonData.put("PricingUOMUnitPrice", item.getUnitPricePerPricingUOM());			
			jsonData.put("UnitPricePerRequestedUOM", item.getUnitPricePerRequestedUOM());			
			jsonData.put("ExtendedPrice", item.getExtendedPrice());
			jsonData.put("PricingUOM", item.getPricingUOM());
			if(isLineNumberRequired)
			{
				int lineNumber=Integer.parseInt(item.getLineNumber());
				pnaHoverDisplayMap.put(item.getLegacyProductCode()+"_"+lineNumber, jsonData);
			}
			else
			{
				pnaHoverDisplayMap.put(item.getLegacyProductCode(), jsonData);
			}
			log.debug("Finished Creating the JSON object for "+ item.getLegacyProductCode());
		}
		return pnaHoverDisplayMap;
	}

	public static Double getLineTotal(String myPrice,String qty,String adjustments){
		
			Double lineTotal =new Double("0");
			Double myPriceDouble =new Double("0");
			Double qtyDouble =new Double("0");
			Double adjustPrice =new Double("0");
			try{
				if(! "".equals(myPrice.trim())){
					myPriceDouble =Double.valueOf(myPrice);
				}
				if(! "".equals(qty.trim())){
					qtyDouble=Double.valueOf(qty);
				}
				if(! "".equals(adjustments.trim())){
					adjustPrice=Double.valueOf(adjustments);
				}
				lineTotal=(myPriceDouble * qtyDouble)+adjustPrice;
			}			
			catch (Exception e) {
				log.error("Exception while calculating line total price :: "+e);
				lineTotal = new Double("0.0");
			}
			
			try{
				BigDecimal bd=new BigDecimal(""+lineTotal);
				String lineTotalStr=""+bd.setScale(4,BigDecimal.ROUND_FLOOR);
				String lineTotalRound=""+bd.setScale(3,BigDecimal.ROUND_FLOOR);
				int num=lineTotalStr.indexOf(".");
					String decimalStr=lineTotalStr.substring(num+1, lineTotalStr.length());
					if('5'==decimalStr.charAt(2) && '0'==decimalStr.charAt(3)){
						lineTotal=new Double(lineTotalRound+"1");
					}
				bd=new BigDecimal(""+lineTotal);
				lineTotalStr=""+bd.setScale(2,BigDecimal.ROUND_HALF_EVEN);
				lineTotal=new Double(lineTotalStr);
			}
			catch(Exception e){
				log.error("Exception while Rounding "+e);
			}
			
		return lineTotal;
	}
	public static String getPnALineErrorMessage(XPEDXItem item) {
		String errorMessage = "";
		if (null == item) {
			log.debug("getPnALineErrorMessage: Item is null. Returning a empty message.");
		}
		String lineStatusCode = item.getLineStatusCode();

		if (YFCCommon.isVoid(lineStatusCode)) {
			log.debug("getPnALineErrorMessage: lineStatusCode is null. Returning a empty message.");
			return errorMessage;
		}
		if (lineStatusCode.equals("00")) {
			log.debug("getPnALineErrorMessage: lineStatusCode is zero.Successful. Returning a empty message.");
			return errorMessage;
		}
		if (isANumber(lineStatusCode)) {
			switch (Integer.parseInt(lineStatusCode)) {
			case 0:
				errorMessage = "";
				break;
			case 1:
				errorMessage = "Invalid Item number";
				break;
			case 2:
				errorMessage = "Item Number missing";
				break;
			case 3:
				errorMessage = "Bad UOM";
				break;
			case 4:
				errorMessage = "Overflow error";
				break;
			case 5:
				errorMessage = "Order Branch Missing";//Price Error";
				break;
			case 6:
				errorMessage = "Item is suspended";
				break;
			case 7:
				errorMessage = "Item is non-standard";
				break;
			case 8:
				errorMessage = " Item Balance record is missing";
				break;
			case 9:
				errorMessage = "Suspended Item Balance";
				break;
			case 10:
				errorMessage = "Requested Quantity has non-numeric data";
				break;
			case 11:
				errorMessage = "Requested UOM missing";
				break;
			case 12:
				errorMessage = " Requested UOM not in eCommerce UOM";
				break;
			default:
				errorMessage = "Unknown error";
			}
		}
		return errorMessage;
	}

	private static boolean isANumber(String value) {
		try {
			Integer.parseInt(value);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
	
	//Added to check whether the bracket pricing is available and price is greater than 0 return true else false
	public static String isBracketPricingAvailable(List brackets)
	{
		String isBracketPricing="false";
		try
		{
			if(brackets  != null){
				for(Object bracket : brackets)
				{
					
					XPEDXBracket brck=(XPEDXBracket)bracket;
					BigDecimal bracketPrice = new BigDecimal(brck.getBracketPrice()); 
					if (bracketPrice.compareTo(BigDecimal.ZERO) > 0)
					{
						isBracketPricing="true";
						break;
					}
				}
			}
		}catch(Exception e){
			log.error("Error Getting bracket price :"+e.getMessage());
		}
		return isBracketPricing;
	}
	
	

	
	/*
	 * Utility for dividing value for given precision
	 * Takes Strings as input parameters.
	 */
	public static BigDecimal divideBDWithPrecision(String strVal1, String strVal2) throws Exception {

		BigDecimal bdVal1 = new BigDecimal(strVal1);
		BigDecimal bdVal2 = new BigDecimal(strVal2);

		return divideBDWithPrecision( bdVal1, bdVal2);
	}
	
	/*
	 * Utility for dividing value for given precision
	 * Takes BigDecimals as input parameters
	 */
	public static BigDecimal divideBDWithPrecision(BigDecimal bdVal1, BigDecimal bdVal2) throws Exception {
		
		BigDecimal result = new BigDecimal("0");


			try {
			
				result = bdVal1.divide(bdVal2, DECIMAL_PRECISION_NEEDED, RoundingMode.HALF_UP);

			} catch (Exception e) {
				log.debug("Error Occured For Divisor : " + bdVal1 + " , Dividor : " + bdVal2 );
				log.debug(e.getMessage() ,  e);
				throw e;
			}
			
			
			return result;
	}
	
	public static HashMap<String,XPEDXItemPricingInfo> getPricingInfoFromItemDetails(Vector<XPEDXItem> items,IWCContext wcContext) throws Exception
	{
		return getPricingInfoFromItemDetails(items,wcContext,false,null,false,null);
	}
	
	public static HashMap<String,XPEDXItemPricingInfo> getPricingInfoFromItemDetails(Vector<XPEDXItem> items,IWCContext wcContext,boolean isLineNuberRequired) throws Exception
	{
		return getPricingInfoFromItemDetails(items,wcContext,isLineNuberRequired,null,false,null);
	}
	
	public static HashMap<String,XPEDXItemPricingInfo> getPricingInfoFromItemDetails(Vector<XPEDXItem> items,IWCContext wcContext,boolean isLineNuberRequired,
																					 Element lineTypeMEle, boolean isComingFromCheckoutOrDraftOrderDetails, Document orderOutputDocument) throws Exception
	{
		HashMap<String, XPEDXItemPricingInfo> priceUomDisplayMap = new HashMap<String, XPEDXItemPricingInfo>();
		
		ArrayList<String> itemIDList = new ArrayList<String>();
		for(XPEDXItem item:items)
		{
			String itemId = item.getLegacyProductCode();
			itemIDList.add(itemId);
		}
		
		Document pricingInfoDoc=null;
		Map<String,List<String>> itemsUOMMap=null;
		/*Begin - Changes made by Mitesh Parikh for JIRA#3595*/
		if(isComingFromCheckoutOrDraftOrderDetails)
		{
			pricingInfoDoc=orderOutputDocument;
			itemsUOMMap = createItemUOMMap(pricingInfoDoc.getDocumentElement());		
		
		} else {
			pricingInfoDoc = XPEDXOrderUtils.getItemDetailsForPricingInfo(itemIDList,wcContext
					.getCustomerId(), wcContext.getStorefrontId(), wcContext);
			itemsUOMMap= createItemUOMMap(pricingInfoDoc);
			
		}
		/*End - Changes made by Mitesh Parikh for JIRA#3595*/
		
		if(pricingInfoDoc!=null)
		{
			ArrayList<Element> allItemElems=null;
			if(isComingFromCheckoutOrDraftOrderDetails)
			{
				allItemElems = getOrderItemDetailsElement(pricingInfoDoc.getDocumentElement());
			
			}else {				
				allItemElems = SCXmlUtil.getElements(pricingInfoDoc.getDocumentElement(), "Item");
				
			}
			
			if(lineTypeMEle != null)
			{
				allItemElems.addAll(SCXmlUtil.getElements(lineTypeMEle, "Item"));
			}
			
			if(allItemElems!=null && allItemElems.size()>0)
			{
				
				for(Element itemEle : allItemElems)
				{
					SCXmlUtil.getString(itemEle);
					String itemId = SCXmlUtil.getAttribute(itemEle, "ItemID");
					if(log.isDebugEnabled()){
					log.debug("itemId " + itemId);
					}
					Element primaryInfoEle = SCXmlUtil.getChildElement(itemEle,
							"PrimaryInformation");
					Element itemExtnEle = SCXmlUtil.getChildElement(itemEle, "Extn");
					String minOrderQty = SCXmlUtil.getAttribute(primaryInfoEle,
							"MinOrderQuantity");
					String pricingUOM = SCXmlUtil.getAttribute(primaryInfoEle,
							"PricingUOM");
					String pricingUOMConvFactor = SCXmlUtil.getAttribute(primaryInfoEle,
							"PricingQuantityConvFactor");
					String baseUOM = SCXmlUtil.getAttribute(itemEle, "UnitOfMeasure");
					String prodMweight = SCXmlUtil.getAttribute(itemExtnEle, "ExtnMwt");
					//Added for JIRA 2600 - Rugrani
					if(null != prodMweight && (prodMweight.equals("0") || prodMweight.equals("0.0"))){
						prodMweight = null;
					}
					
					
					XPEDXItemPricingInfo pricingInfo = null;
					for (XPEDXItem pandAItem : items) {
						if (pandAItem.getLegacyProductCode().equals(itemId)) {
							String priceCurrencyCode = null;
							pricingInfo = new XPEDXItemPricingInfo();
							if (pandAItem.getPriceCurrencyCode() != null
									&& pandAItem.getPriceCurrencyCode().trim().length() > 0) {
								pricingInfo.setPriceCurrencyCode(pandAItem.getPriceCurrencyCode());
								
							}
							else{
								//If price currency code is not set,set the currency code from customer profile
								//String custCurrencyCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.CUSTOMER_CURRENCY_CODE);
								//added for jira 3788 - display of currency code in mil non edit
								XPEDXShipToCustomer shipToCustomer= new XPEDXShipToCustomer();
								shipToCustomer = (XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
								String custCurrencyCode = shipToCustomer.getBillTo().getExtnCurrencyCode();
								pricingInfo.setPriceCurrencyCode(custCurrencyCode);
							}
							List<String> uomsList=itemsUOMMap.get(itemId);
							priceCurrencyCode = pricingInfo.getPriceCurrencyCode();
							pricingUOM = pandAItem.getPricingUOM();
							String pricingUOMUnitPrice = pandAItem
									.getUnitPricePerPricingUOM();
							BigDecimal pricingUOMPrice = new BigDecimal(0);
							if(pricingUOMUnitPrice != null && !"".equals(pricingUOMUnitPrice))
								pricingUOMPrice = new BigDecimal(pricingUOMUnitPrice);
							BigDecimal prodWeight = null;
							BigDecimal priceForCWTUom = null;
							BigDecimal priceForTHUom = null;
							
							String BaseUomDesc = null;
							String RequestedQtyUOMDesc = null;
							String PricingUOMDesc = null;
							try {
								BaseUomDesc = XPEDXWCUtils.getUOMDescription(baseUOM);
								RequestedQtyUOMDesc = XPEDXWCUtils
										.getUOMDescription(pandAItem.getRequestedQtyUOM());
								PricingUOMDesc = XPEDXWCUtils.getUOMDescription(pricingUOM);
							} catch (Exception e) {
	
							}
							
							String cwtUOMDesc = null;
							String thUOMDesc = null;
	//						displayUOMs.add(baseUOM); //removed as specified in the bug 185 comments on 03/Jan/11 3:58 PM by Barb Widmer
							//Fix done as per Pawan's mail dated 25/03/2011
							if (TH_UOM_M.equalsIgnoreCase(pricingUOM) || TH_UOM_A.equalsIgnoreCase(pricingUOM)) {
								// hardcode for now.
								
								if (prodMweight != null && prodMweight.trim().length() > 0)
									prodWeight = new BigDecimal(prodMweight);
								else
									prodWeight = new BigDecimal(100); // this will make
								// pricing for TH
								// and CWT same.
								/* priceForCWTUom = pricingUOMPrice.divide(prodWeight
										.divide(new BigDecimal(100))); */
								
								priceForCWTUom = divideBDWithPrecision(pricingUOMPrice , divideBDWithPrecision(prodWeight , new BigDecimal(100)) );
								cwtUOMDesc = XPEDXWCUtils.getUOMDescription(CWT_UOM_M);
								if(cwtUOMDesc==null || cwtUOMDesc.length()==0)
									cwtUOMDesc = XPEDXWCUtils.getUOMDescription(CWT_UOM_A);
							}
							//Fix done as per Pawan's mail dated 25/03/2011
							if (CWT_UOM_M.equalsIgnoreCase(pricingUOM) || CWT_UOM_A.equalsIgnoreCase(pricingUOM)) {
								
								if (prodMweight != null && prodMweight.trim().length() > 0)
									prodWeight = new BigDecimal(prodMweight);
								else
									prodWeight = new BigDecimal(100); // this will make
								// pricing for CW
								// priceForCWTUom =
								// pricingUOMPrice.divide(prodWeight.divide(new
								// BigDecimal(100)));
								
							/*	priceForTHUom = pricingUOMPrice.multiply(prodWeight
										.divide(new BigDecimal(100)));
										*/
								
								priceForTHUom = pricingUOMPrice.multiply(divideBDWithPrecision(prodWeight  ,new BigDecimal(100)) );
								thUOMDesc = XPEDXWCUtils.getUOMDescription(TH_UOM_M);
								if(thUOMDesc==null || thUOMDesc.length()==0)
									thUOMDesc = XPEDXWCUtils.getUOMDescription(TH_UOM_A);
							}
							
							if (YFCCommon.isVoid(pricingUOMConvFactor)) {
								pricingUOMConvFactor = "1";
							}
	
							if (pricingUOMConvFactor != null
									&& ((new BigDecimal(0)).compareTo(new BigDecimal(
											pricingUOMConvFactor)) <= 0)) {
								pricingUOMConvFactor = "1";
							}
							
							BigDecimal basePrice = divideBDWithPrecision ( pricingUOMPrice,  new BigDecimal(pricingUOMConvFactor));
	
							XPEDXUtilBean xpedxUtilBean = new XPEDXUtilBean();
							
							
							ArrayList<XPEDXBracket> displayPriceForUoms = new ArrayList<XPEDXBracket>();
							
							if (priceForCWTUom != null && prodMweight != null && prodMweight.trim().length() > 0 &&
									(uomsList.contains(CWT_UOM_M) || uomsList.contains(CWT_UOM_A)))
							{
								String fmtPriceForCWTUom = xpedxUtilBean.formatPriceWithCurrencySymbolWithPrecisionFive(wcContext, priceCurrencyCode, priceForCWTUom.toString());
								displayPriceForUoms.add(new XPEDXBracket(null, cwtUOMDesc, fmtPriceForCWTUom));
							}
							//Moved code from above to bottom for JIRA 1835
							String fmtPricePerUom =  xpedxUtilBean.formatPriceWithCurrencySymbolWithPrecisionFive(wcContext, priceCurrencyCode,pricingUOMUnitPrice);
							displayPriceForUoms.add(new XPEDXBracket(null, PricingUOMDesc, fmtPricePerUom));
							
							if (priceForTHUom != null &&
									(uomsList.contains(TH_UOM_M) || uomsList.contains(TH_UOM_A)))
							{ 
								String fmtPriceForTHUom = xpedxUtilBean.formatPriceWithCurrencySymbolWithPrecisionFive(wcContext, priceCurrencyCode, priceForTHUom.toString());
								displayPriceForUoms.add(new XPEDXBracket(null, thUOMDesc,fmtPriceForTHUom ));
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
							if(pricingUOM!=null && !pricingUOM.equals(pandAItem.getRequestedQtyUOM()) && isDisplayReqUOM) {
								String fmtPricingUOM  = xpedxUtilBean.formatPriceWithCurrencySymbolWithPrecisionFive(wcContext, priceCurrencyCode, pandAItem.getUnitPricePerRequestedUOM());
								displayPriceForUoms.add(new XPEDXBracket(null, RequestedQtyUOMDesc, fmtPricingUOM));
							}

							String extendedPrice =  xpedxUtilBean.formatPriceWithCurrencySymbol(wcContext, priceCurrencyCode, pandAItem.getExtendedPrice());
							displayPriceForUoms.add(new XPEDXBracket(null, null, extendedPrice));
							pricingInfo.setDisplayPriceForUoms(displayPriceForUoms);
							
							List bracketsPricingList = pandAItem.getBrackets();
							pricingInfo.setBracketsPricingList(bracketsPricingList);
							
							pricingInfo.setIsBracketPricing(XPEDXPriceandAvailabilityUtil
									.isBracketPricingAvailable(bracketsPricingList));
							if(isLineNuberRequired)
							{
								int lineNumber=Integer.parseInt(pandAItem.getLineNumber());
								log.debug("PRICEHOVERMAP KEYS FOR IF CONDITION OF SWC XPEDXPriceandAvailabilityUtil class - MAP.KEY : "+pandAItem.getLegacyProductCode()+"_"+lineNumber);
								log.debug("PRICEHOVERMAP VALUES FOR IF CONDITION OF SWC XPEDXPriceandAvailabilityUtil class - MAP.VALUE.extendedPrice : "+extendedPrice);
								priceUomDisplayMap.put(pandAItem.getLegacyProductCode()+"_"+lineNumber, pricingInfo);
							}
							else
							{
								log.debug("PRICEHOVERMAP KEYS FOR ELSE CONDITION OF SWC XPEDXPriceandAvailabilityUtil class - MAP.KEY : "+pandAItem.getLegacyProductCode());
								log.debug("PRICEHOVERMAP VALUES FOR ELSE CONDITION OF SWC XPEDXPriceandAvailabilityUtil class - MAP.VALUE.extendedPrice : "+extendedPrice);								
								priceUomDisplayMap.put(pandAItem.getLegacyProductCode(), pricingInfo);
							}
						}
					}
				}
			}
		}
		return priceUomDisplayMap;
		
	}
	
	private static Map<String,List<String>> createItemUOMMap(Document pricingInfoDoc)
	{
		Map<String,List<String>> itemUOMMap=new HashMap<String,List<String>>();
		if(pricingInfoDoc !=null)
		{
			NodeList items=pricingInfoDoc.getDocumentElement().getElementsByTagName("Item");
			for(int i=0;i<items.getLength();i++)
			{
				List<String> uoms=new ArrayList<String>();
				Element itemElem=(Element)items.item(i);
				uoms.add(itemElem.getAttribute("UnitOfMeasure"));
				ArrayList<Element> itemUomList=SCXmlUtil.getElements(itemElem, "AlternateUOMList/AlternateUOM");
				if(itemUomList != null)
				{
					for(Element alternateUOM :itemUomList)
					{
						uoms.add(alternateUOM.getAttribute("UnitOfMeasure"));
					}
					itemUOMMap.put(itemElem.getAttribute("ItemID"),uoms);
				}
			}
		}
		return itemUOMMap;
	}
	
	private static Map<String,List<String>> createItemUOMMap(Element pricingInfoDocElement)
	{
		Map<String,List<String>> itemUOMMap=new HashMap<String,List<String>>();
		if(pricingInfoDocElement !=null)
		{
			NodeList items=pricingInfoDocElement.getElementsByTagName("ItemDetails");
			for(int i=0;i<items.getLength();i++)
			{
				List<String> uoms=new ArrayList<String>();
				Element itemElem=(Element)items.item(i);
				uoms.add(itemElem.getAttribute("UnitOfMeasure"));
				ArrayList<Element> itemUomList=SCXmlUtil.getElements(itemElem, "AlternateUOMList/AlternateUOM");
				if(itemUomList != null)
				{
					for(Element alternateUOM :itemUomList)
					{
						uoms.add(alternateUOM.getAttribute("UnitOfMeasure"));
					}
					itemUOMMap.put(itemElem.getAttribute("ItemID"),uoms);
				}
			}
		}
		return itemUOMMap;
	}
	/*
	 * This takes care of displaying message to Users based on 
	 * 		1] ServiceDown 2] Transmission Error 3] HeaderLevelError, 4] LineItemError  
	 */
	public static String getAjaxDisplayStatusCodeMsg(XPEDXPriceAndAvailability pna) {
		
		String ajaxMsg = "";	
		//modified for jira 2885
		if(( pna == null || pna.getItems().size()==0) && ("F".equals(pna.getTransactionStatus())))
			ajaxMsg  =   XPEDXPriceandAvailabilityUtil.WS_PRICEANDAVAILABILITY_WITH_SERVICESTATUSDOWN_ERROR;
		else
			ajaxMsg  =   pna.getStatusVerboseMsg();
		
		return ajaxMsg;
	}
	
	/**
	 * This returns a sample output doc, for testing purpose
	 * 
	 * @return
	 */
	static Document getDummyOutPutDoc( ) {
		Document returnDoc = null;
		InputStream is = XPEDXPriceandAvailabilityUtil.class
				.getClassLoader()
				.getResourceAsStream(
						"com/sterlingcommerce/xpedx/webchannel/utilities/priceandavailability/XPEDXDummyOutputDoc.xml");
		returnDoc = SCXmlUtil.createFromStream(is);
		return returnDoc;
	}

	/*
	 * Utility for testing
	 */
	static Document getDummyPandAOutputDocByFileName(String xmlDocName ) {
		Document returnDoc = null;
		InputStream is = XPEDXPriceandAvailabilityUtil.class
				.getClassLoader()
				.getResourceAsStream(
						"com/sterlingcommerce/xpedx/webchannel/utilities/priceandavailability/" + xmlDocName 
						);
		returnDoc = SCXmlUtil.createFromStream(is);
		return returnDoc;
	}

	/*
	 * Utility for Testing ..
	 */
	static Document manipulatePnAResponseXML(String inputXML) {
		
		String legacyProdCode = StringUtils.substringBetween(inputXML, "<LegacyProductCode>", "</LegacyProductCode>")	;
		StringBuffer sb =  new StringBuffer();
		
		sb.append("<?xml version='1.0' encoding='utf-8'?>" );
		sb.append("<PriceAndAvailability>" );
		sb.append("<TransactionStatus>Success</TransactionStatus>" );
//		sb.append("<TransactionStatus>F</TransactionStatus>" );
		sb.append("<EnvironmentId/>" );
		sb.append("<Company>Sterling Commerce</Company>" );
		sb.append("<CustomerBranch/>" );
		sb.append("<CustomerNumber/>" );
		sb.append("<ShipToSuffix/>" );
		sb.append("<OrderBranch/>" );
		sb.append("<HeaderStatusCode>00</HeaderStatusCode>" );
//		sb.append("<HeaderStatusCode>03</HeaderStatusCode>" );
				sb.append("<Items>" );
					sb.append("<Item>" );
					sb.append("<LineNumber>123</LineNumber>" );
					sb.append("<LegacyProductCode>5220012</LegacyProductCode>" );
					//sb.append("<LegacyProductCode>" + legacyProdCode +"</LegacyProductCode>" );
					sb.append("<RequestedQtyUOM>M_BAG</RequestedQtyUOM>" );
					sb.append("<RequestedQty>1</RequestedQty>" );
					sb.append("<PurchaseOrderQty/>" );
					sb.append("<PricingUOM>CASE</PricingUOM>" );
					sb.append("<PriceCurrencyCode/>" );
					sb.append("<UnitPricePerPricingUOM>1560.20</UnitPricePerPricingUOM>" );
					sb.append("<UnitPricePerRequestedUOM>75.0712</UnitPricePerRequestedUOM>" );
					sb.append("<ExtendedPrice>1879.00</ExtendedPrice>" );
					sb.append("<ItemCost>29.87</ItemCost>" );
					sb.append("<CostCurrencyCode/>" );
					sb.append("<Brackets>" );
						sb.append("<Bracket>" );
							sb.append("<BracketQTY>23</BracketQTY>" );
							sb.append("<BracketUOM>EA</BracketUOM>" );
							sb.append("<BracketPrice>56.78</BracketPrice>" );
						sb.append("</Bracket>" );
					sb.append("</Brackets>" );
					sb.append("<WarehouseLocationList>" );
						sb.append("<WarehouseLocation>" );
						sb.append("<Warehouse>Gangolli</Warehouse>" );
						sb.append("<AvailableQty>34</AvailableQty>" );
						sb.append("<NumberOfDays>67</NumberOfDays>" );
					sb.append("</WarehouseLocation>" );
				sb.append("</WarehouseLocationList>" );
		             sb.append("<LineStatusCode>00</LineStatusCode>" );
		            // sb.append("<LineStatusCode>02</LineStatusCode>" );
		       sb.append("</Item>" );
		     sb.append("</Items>" );
		sb.append("</PriceAndAvailability>" );
				
				
		Document newDocument = SCXmlUtils.createFromString(sb.toString());
	return newDocument;
	}
	
	//added for jira 2885
	public static Map<String,String> getLineErrorMessageMap(Vector<XPEDXItem> items)
	{
		HashMap<String,String> pnALineErrorMessage=new HashMap<String,String>(); 
		String errorMessage = "";
		if(items !=null)
		{
			for(int i=0;i<items.size();i++)
			{
				XPEDXItem item=items.get(i);
				String itemID=item.getLegacyProductCode();
				//if(!"00".equals(item.getLineStatusCode())){
				if(item.getLineStatusErrorMsg() == null){
					errorMessage="";
				}
				else{
				       errorMessage = item.getLineStatusErrorMsg();
				}
				pnALineErrorMessage.put(itemID, errorMessage);
			}
		}
		return pnALineErrorMessage;
	}
	
	/**
	 * Logger
	 */
	private static final Logger log = Logger
			.getLogger(XPEDXPriceandAvailabilityUtil.class);
	
	
	/**
	 * main function for quick / simple testing / Utility
	 */
	  public static void main(String[] args) {
		    if(log.isDebugEnabled()){
			log.debug(" Test : testHandlePandAWebServiceResponseSatuseses " );		
		    }
			String actuialErrorMsgToUser = null;
			XPEDXPriceAndAvailability pna = new XPEDXPriceAndAvailability();	
			
			XPEDXPriceandAvailabilityUtil testClass = new XPEDXPriceandAvailabilityUtil();
			
			Document newDoc = testClass.manipulatePnAResponseXML ( null );
			String newXmlDoc = SCXmlUtils.getString( newDoc );
			if(log.isDebugEnabled()){
			log.debug(" newXmlDoc " + newXmlDoc );
			}
			
			XPEDXPriceandAvailabilityUtil.handlePandAWebServiceResponseSatuseses(newDoc, pna, actuialErrorMsgToUser );
			actuialErrorMsgToUser = pna.getStatusVerboseMsg();
			if(log.isDebugEnabled()){
			log.debug(" actuialErrorMsgToUser " + actuialErrorMsgToUser );	
			}
	}
	  
	public static ArrayList<Element> getOrderItemDetailsElement(Element orderEl) {
		ArrayList<Element> orderLineElements = SCXmlUtil.getElements(orderEl, "OrderLines/OrderLine");
		ArrayList<Element> itemDetailsElements = new ArrayList<Element>();
	    if (orderLineElements.size() > 0)
	    {
	      for (int i = 0; i < orderLineElements.size(); i++)
	      {
	        Element orderLineEl = (Element)orderLineElements.get(i);
	        itemDetailsElements.add(SCXmlUtil.getChildElement(orderLineEl, "ItemDetails"));

	      }
	    }
	    
	    return itemDetailsElements;
	    
	}

}

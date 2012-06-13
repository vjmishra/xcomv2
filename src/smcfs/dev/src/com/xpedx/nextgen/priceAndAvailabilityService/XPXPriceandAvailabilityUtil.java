package com.xpedx.nextgen.priceAndAvailabilityService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.log4j.Logger;
import org.apache.tools.ant.filters.StringInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXPriceandAvailabilityUtil {
	private static YIFApi api = null;
	
	public static XPXPriceAndAvailability getPriceAndAvailability(YFSEnvironment env, ArrayList<XPXItem> inputItems, String customerID)
	{
		
		XPXPriceAndAvailability output = null;
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {
			
			e1.printStackTrace();
		}
		
		if(null==inputItems || inputItems.size()<=0){
			log.debug("getPriceAndAvailability(): Item list is empty... Cannot call the service...");	
			return output;
		}
		Document inputDoc = prepareInputDoc(env,inputItems,customerID);
		String inputXML = SCXmlUtil.getString(inputDoc);
		log.debug("getPriceAndAvailability: inputXML for P&A Webservice: "+inputXML);
		Document outputDoc = null;
		try {
			outputDoc = api.executeFlow(env, "XPXPandAWebService", inputDoc);		
			//outputDoc = getDummyOutPutDoc();
			if(null!=outputDoc){
				String responseXML = SCXmlUtil.getString(outputDoc);
				//Check for the TransactionStatus
				Element tranElement = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "TransactionStatus");
				String transactionStatus = tranElement.getTextContent();
				if(YFCCommon.isVoid(transactionStatus) || transactionStatus.equalsIgnoreCase("F")){
					log.error("PnA TransactionStatus failure.Returning empty PnA Business object");
					output = new XPXPriceAndAvailability();
					output.setResponseXml(responseXML);
					output.setTransactionStatus("F");
					/*YFSException excep=new YFSException("PnA TransactionStatus failure.Logging error");
					excep.setErrorDescription("PnA TransactionStatus failure.Logging error");
					excep.setErrorCode("PnA Failed");
					throw excep;*/
					return output;
				}
				
				//Check for the HeaderStatusCode
				Element headerStatusElement = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "HeaderStatusCode");
				String headerStatusCode = headerStatusElement.getTextContent();
				if(YFCCommon.isVoid(headerStatusCode) || !headerStatusCode.equalsIgnoreCase("00")){
					log.error("PnA HeaderStatusCode failure.Indicates bad data from Sterling.Returning empty PnA Business object");					
					output = new XPXPriceAndAvailability();
					output.setResponseXml(responseXML);
					output.setTransactionStatus("F");
					return output;
				}			
				output = setBusinessObjects(outputDoc);
			}else{
				log.error("PnA did not respond or it sent a Empty xml back.");				
			}
			
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
	
	private static Document getDummyOutPutDoc() {
		Document returnDoc = null;
		InputStream is = XPXPriceandAvailabilityUtil.class
				.getClassLoader()
				.getResourceAsStream(
						"com/sterlingcommerce/xpedx/webchannel/utilities/priceandavailability/XPEDXDummyOutputDoc.xml");
		returnDoc = SCXmlUtil.createFromStream(is);
		return returnDoc;
	}
	
	private static Document prepareInputDoc(YFSEnvironment env, ArrayList<XPXItem> inputItems, String customerId)
	{
		Document returnDoc = null;
		Document outputDoc = null;
		
		try {
			YFCDocument customeListDoc = YFCDocument.createDocument("Customer");
			YFCElement customerElement = customeListDoc.getDocumentElement();
			customerElement.setAttribute("CustomerID", customerId);
			env.setApiTemplate("getCustomerList",
					createTemplateForCustomerList());
			outputDoc = api.invoke(env, "getCustomerList", customeListDoc.getDocument());;
			env.clearApiTemplate("getCustomerList");						
			returnDoc = SCXmlUtil.createFromString("<PriceAndAvailability><Items></Items></PriceAndAvailability>");
			Element customerEle = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "Customer");
			Element custExtn = SCXmlUtil.getChildElement(customerEle, "Extn");
			String envId = custExtn.getAttribute("ExtnEnvironmentCode");
			String custEnvId = custExtn.getAttribute("ExtnOrigEnvironmentCode");
			String customerNumber = custExtn.getAttribute("ExtnLegacyCustNumber");
			String Company = custExtn.getAttribute("ExtnCompanyCode");
			String CustomerBranch = custExtn.getAttribute("ExtnCustomerDivision");
			String ShipToSuffix = custExtn.getAttribute("ExtnShipToSuffix");
			String OrderBranch = !YFCCommon.isVoid(custExtn.getAttribute("ExtnCustOrderBranch"))?
									custExtn.getAttribute("ExtnCustOrderBranch"):
										custExtn.getAttribute("ExtnShipFromBranch");
			addXMLTag(returnDoc,"SourceIndicator","1");//1 for WebChannel
			addXMLTag(returnDoc,"EnvironmentId",envId);
			addXMLTag(returnDoc, "CustomerEnvironmentId", custEnvId);
			addXMLTag(returnDoc,"Company",Company);
			addXMLTag(returnDoc,"CustomerBranch",CustomerBranch);
			addXMLTag(returnDoc,"CustomerNumber",customerNumber);
			addXMLTag(returnDoc,"ShipToSuffix",ShipToSuffix);
			addXMLTag(returnDoc,"OrderBranch",OrderBranch);
			NodeList inputNodeList = returnDoc.getElementsByTagName("Items");
			Element inputNodeListElemt = (Element)inputNodeList.item(0);
			for(XPXItem item: inputItems){
				Document itemDoc = YFCDocument.createDocument("Item").getDocument();
				Element itemElement = itemDoc.getDocumentElement();
				addXMLTag(itemDoc,"LineNumber",item.getLineNumber());
				addXMLTag(itemDoc,"LegacyProductCode",item.getLegacyProductCode());
				addXMLTag(itemDoc,"RequestedQtyUOM",item.getRequestedQtyUOM());
				addXMLTag(itemDoc,"RequestedQty",item.getRequestedQty());
				inputNodeListElemt.appendChild(returnDoc.importNode(itemElement, true));
			}									
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnDoc;
	}
	
	
	private static Document createTemplateForCustomerList() {
		YFCDocument customeListTemplateDoc = YFCDocument
				.createDocument("Customer");
		YFCElement customerList = customeListTemplateDoc
				.getDocumentElement();
		YFCElement extn = customerList
		.createChild("Extn");		
		return customeListTemplateDoc.getDocument();
	}

	public static XPXPriceAndAvailability getPriceAndAvailability(YFSEnvironment env, ArrayList<XPXItem> inputItems, Document inXML){
		XPXPriceAndAvailability output = null;
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {
			
			e1.printStackTrace();
		}
		
		if(null==inputItems || inputItems.size()<=0){
			log.debug("getPriceAndAvailability(): Item list is empty... Cannot call the service...");	
			return output;
		}
		Document inputDoc = prepareInputDoc(inputItems,inXML);
		String inputXML = SCXmlUtil.getString(inputDoc);
		log.debug("getPriceAndAvailability: inputXML for P&A Webservice: "+inputXML);
		Document outputDoc = null;
		try {
			outputDoc = api.executeFlow(env, "XPXPandAWebService", inputDoc);			
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		output =  setBusinessObjects(outputDoc);
		return output;
	}
	
	/**
	 * @param inputItems
	 * @return
	 */
	private static Document prepareInputDoc(ArrayList<XPXItem> inputItems, Document  inXML) {
		Document returnDoc = null;
		Document customerDoc = null;
		
		returnDoc = SCXmlUtil.createFromString("<PriceAndAvailability><Items></Items></PriceAndAvailability>");
		String customerId = inXML.getDocumentElement().getAttribute("BillToID");
		Element extnElement = SCXmlUtil.getChildElement(inXML.getDocumentElement(), "Extn");
		String envId = extnElement.getAttribute("ExtnEnvtId");
		String custEnvId = extnElement.getAttribute("ExtnOrigEnvironmentCode");
		String customerNumber = extnElement.getAttribute("ExtnLegacyCustNumber");
		String Company = extnElement.getAttribute("ExtnCompanyId");
		String CustomerBranch = extnElement.getAttribute("ExtnOrderDivision");
		String ShipToSuffix = extnElement.getAttribute("ExtnShipToSuffix");//??
		
		//??
		String OrderBranch = !YFCCommon.isVoid(extnElement.getAttribute("ExtnCustOrderBranch"))?
				extnElement.getAttribute("ExtnCustOrderBranch"):
					extnElement.getAttribute("ExtnShipFromBranch");
		addXMLTag(returnDoc,"SourceIndicator","1");//1 for WebChannel//??
		addXMLTag(returnDoc,"EnvironmentId",envId);
		addXMLTag(returnDoc, "CustomerEnvironmentId", custEnvId);
		addXMLTag(returnDoc,"Company",Company);
		addXMLTag(returnDoc,"CustomerBranch",CustomerBranch);
		addXMLTag(returnDoc,"CustomerNumber",customerNumber);
		addXMLTag(returnDoc,"ShipToSuffix",ShipToSuffix);
		addXMLTag(returnDoc,"OrderBranch",OrderBranch);
		NodeList inputNodeList = returnDoc.getElementsByTagName("Items");
		Element inputNodeListElemt = (Element)inputNodeList.item(0);
		for(XPXItem item: inputItems){
			Document itemDoc = YFCDocument.createDocument("Item").getDocument();
			Element itemElement = itemDoc.getDocumentElement();
			addXMLTag(itemDoc,"LineNumber",item.getLineNumber());
			addXMLTag(itemDoc,"LegacyProductCode",item.getLegacyProductCode());
			addXMLTag(itemDoc,"RequestedQtyUOM",item.getRequestedQtyUOM());
			addXMLTag(itemDoc,"RequestedQty",item.getRequestedQty());
			inputNodeListElemt.appendChild(returnDoc.importNode(itemElement, true));
		}
		return returnDoc;
	}
	
	private static void addXMLTag(Document doc, String tagName, String tagValue){
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
	
	private static XPXPriceAndAvailability setBusinessObjects(Document outputDoc) {
		XPXPriceAndAvailability output = null;
		if (null == outputDoc) {
			log.error("setBusinessObjects(): Empty request received. Returning empty object!");
			return output;
		}
		String responseXML = SCXmlUtil.getString(outputDoc);
		URL ruleXMLURL = XPXPriceandAvailabilityUtil.class.getClassLoader().getResource("/global/template/resource/extn/XPXPriceaAndAvailabilityOutputDigester.xml");
		if(null == ruleXMLURL){
			log.error("setBusinessObjects(): Unable to locate XPXPriceaAndAvailabilityOutputDigester.xml in resources.jar. Returning empty object!");
			return output;
		}
		log.debug("The XML rule loaded to memory: " + ruleXMLURL.getPath());
		try {
			Digester digester = DigesterLoader.createDigester(ruleXMLURL);
			digester.setValidating(false);
			output = (XPXPriceAndAvailability) digester.parse(new StringInputStream(SCXmlUtil.getString(outputDoc)));
			output.setResponseXml(responseXML);
		} catch (MalformedURLException e) {
			log.error("Unable to create XPXPriceandAvailabilityUtil bean. Unable to locate rule file XPEDXPriceaAndAvailabilityOutputDigester.xml ...URL:"+ ruleXMLURL.toString() + " \n" + e);
		} catch (IOException e) {
			log.error("Unable to create XPXPriceandAvailabilityUtil bean. IOException while parsing the incoming xml ...\n XML: "+ outputDoc.toString() + " \n" + e);
		} catch (SAXException e) {
			log.error("Unable to create XPXPriceandAvailabilityUtil bean. Unable to parse the incoming xml ...\n XML: "+ outputDoc.toString() + " \n" + e);
		}
		return output;
	}
	

	private static final Logger log= Logger.getLogger(XPXPriceandAvailabilityUtil.class);
	
}

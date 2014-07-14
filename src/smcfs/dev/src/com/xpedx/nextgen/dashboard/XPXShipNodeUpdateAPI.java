package com.xpedx.nextgen.dashboard;

import java.util.HashMap;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXShipNodeUpdateAPI  implements YIFCustomApi
{

	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXCreateChainedOrderService.xml";
	
	private static YFCLogCategory log;
	private static YIFApi api = null;
	Properties props;
	String entryType = "";
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Document updateShipNode(YFSEnvironment env, Document inputXML) throws Exception
	{
		
		String shipNode = null;
		String envtCode = null;
		String webConfirmationNumber = null;
		String actualDate = null;
		
		log.debug("The input xml to updateShipNode API is: "+SCXmlUtil.getString(inputXML));
	
        String orderHeaderKey = inputXML.getDocumentElement().getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
		
		Document inputOrderDoc = YFCDocument.createDocument("Order").getDocument();
        Element inputOrderElement = inputOrderDoc.getDocumentElement();
        inputOrderElement.setAttribute("OrderHeaderKey",orderHeaderKey);
        Document orderListTemplateDoc = setOrderListTemplate(env);
        env.setApiTemplate("getOrderList", orderListTemplateDoc);
        Document orderListDocument = api.invoke(env, "getOrderList", inputOrderDoc);
        log.debug("The new getOrderList doc is: "+SCXmlUtil.getString(orderListDocument));
        env.clearApiTemplate("getOrderList");
        
		
		Element inputDocRoot = (Element) orderListDocument.getDocumentElement().getElementsByTagName("Order").item(0);
		
		//String orderHeaderKey = inputDocRoot.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
		
		String buyerOrganizationCode = inputDocRoot.getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
		
		String enterpriseCode = inputDocRoot.getAttribute(XPXLiterals.A_ENTERPRISE_CODE);
		
		log.debug("The buyer organization code is: "+buyerOrganizationCode);
		
		//shipNode = inputDocRoot.getAttribute(XPXLiterals.A_SHIP_NODE);
		
		if(buyerOrganizationCode!=null || buyerOrganizationCode.trim().length()!=0)
		{						
			String[] splitArrayOnBuyerOrgCode =  buyerOrganizationCode.split("-");
            
            for(int i=0; i<splitArrayOnBuyerOrgCode.length; i++)
            {
           	 
           	    if(i==3)
           	    {
           		envtCode = splitArrayOnBuyerOrgCode[i];
           		log.debug("The envt code is: "+envtCode);
           	    }         	
           	 
            }
		}
		
		webConfirmationNumber = generateWebConfirmationNumber(orderHeaderKey,envtCode, inputDocRoot);
		log.debug("webConfirmationNumber ::" + webConfirmationNumber);
		//added to retrieve same at confirmation email event
		//if(webConfirmationNumber!=null && webConfirmationNumber.trim().length()>0)
		if(!webConfirmationNumber.isEmpty())	
		{
			env.setTxnObject("WebConfirmationNumber", webConfirmationNumber);
		}
				
		//Commented out as a fix for JIRA #1429
		/*if(shipNode == null || shipNode.trim().length()==0)//If shipNode already exists, it will not be overwritten
		{*/
		/*Document getCustomerListInputDoc = createGetCustomerListInputDoc(buyerOrganizationCode);
		
		
		env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
		Document getCustomerListOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc);
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		
		NodeList customerList = getCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER);
		
		if(customerList.getLength()>0)
		{
			Element customerElement = (Element) getCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
			
			Element extnElement  =  (Element) customerElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
			
			shipNode = extnElement.getAttribute(XPXLiterals.A_EXTN_CUSTOMER_ORDER_BRANCH);
			
			log.debug("The value of shipnode is: "+shipNode);
			
		}
			
		HashMap divisionCodeMap = XPXUtils.getDivisionCode(env,buyerOrganizationCode,enterpriseCode);
	    
		if(divisionCodeMap!=null)
		{
			shipNode = (String)divisionCodeMap.get(XPXLiterals.A_EXTN_CUSTOMER_ORDER_BRANCH);
			log.debug("The ship node is: "+shipNode);
		}
		
		if(shipNode!=null && shipNode.trim().length()>0)
		{
			Document changeOrderInputDoc = createChangeOrderInputDoc(orderHeaderKey,shipNode,env,webConfirmationNumber,envtCode,inputDocRoot);
			log.debug("The changeOrder input doc for ship node stamp is: "+SCXmlUtil.getString(changeOrderInputDoc));
			api.invoke(env, XPXLiterals.CHANGE_ORDER_API, changeOrderInputDoc);
		}
		
		}
		
		else
		{*/
	           //Change order to stamp the web conf number and web line numbers only
			    Document changeOrderInputDoc = createChangeOrderForWebNumbers(env,orderHeaderKey,webConfirmationNumber,envtCode,inputDocRoot);
			    log.debug("The changeOrder input doc without ship node stamp is: "+SCXmlUtil.getString(changeOrderInputDoc));
			    api.invoke(env, XPXLiterals.CHANGE_ORDER_API, changeOrderInputDoc);
		//}
		return inputXML;
	}
	private Document setOrderListTemplate(YFSEnvironment env)
	{
		//set output template
		Document orderTemplateDoc = YFCDocument.createDocument("OrderList").getDocument();
		Element orderTemplateElement = orderTemplateDoc.getDocumentElement();
		Element orderElement = orderTemplateDoc.createElement("Order");
		orderTemplateElement.appendChild(orderElement);
		orderElement.setAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE, "");
		orderElement.setAttribute(XPXLiterals.A_SHIP_NODE,"");
		orderElement.setAttribute(XPXLiterals.A_ENTERPRISE_CODE, "");
		orderElement.setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, "");
		orderElement.setAttribute(XPXLiterals.A_ENTRY_TYPE, "");
		Element orderExtnElement = orderTemplateDoc.createElement("Extn");
		orderElement.appendChild(orderExtnElement);
		orderExtnElement.setAttribute(XPXLiterals.A_WEB_CONF_NUMBER, "");
		Element oLinesElement = orderTemplateDoc.createElement("OrderLines");
		orderElement.appendChild(oLinesElement);
		Element oLineElement = orderTemplateDoc.createElement("OrderLine");
		oLinesElement.appendChild(oLineElement);
		oLineElement.setAttribute(XPXLiterals.A_ORDER_LINE_KEY, "");
		Element orderLineExtnElement = orderTemplateDoc.createElement("Extn");
		oLineElement.appendChild(orderLineExtnElement);
		orderLineExtnElement.setAttribute(XPXLiterals.A_WEB_LINE_NUMBER, "");
		return orderTemplateDoc;
	}
	private String generateWebConfirmationNumber(String orderHeaderKey, String envtCode, Element inputDocRoot) {

		String webConfirmationNumber = "";
		String uniqueSequence = "";
		String year = ""; 
		String month = "";
		String day = "";
		int uniqueSequenceLength = 7;
		int orderHeaderKeylength = orderHeaderKey.trim().length();
		
		YFCDate currentSystemDate = new YFCDate();
		String currentSystemDateString = currentSystemDate.toString();
		log.debug("The current systemDate is : "+currentSystemDateString);
		year = currentSystemDateString.substring(2,4);
		month = currentSystemDateString.substring(4,6);
		day = currentSystemDateString.substring(6,8);
		
		if (orderHeaderKey != null && orderHeaderKeylength != 0 
				&& orderHeaderKeylength > 8)
		{
			

			int startIndex = orderHeaderKeylength-uniqueSequenceLength;
			uniqueSequence = orderHeaderKey.substring(startIndex);

		}	

		entryType = inputDocRoot.getAttribute(XPXLiterals.A_ENTRY_TYPE);
		log.debug("Entry type = " + entryType);
		/* Changes made to fix issue 926 
		IF order is placed from B2B,WEB or COM  Environment ID is Changed to constant('E') */
		if(entryType != null && (XPXLiterals.SOURCE_TYPE_B2B.equals(entryType) || XPXLiterals.SOURCE_WEB.equals(entryType) 
				|| XPXLiterals.SOURCE_COM.equals(entryType))){
			envtCode = XPXLiterals.SYSTEM_SPECIFIER_WEB;
		}
		
		webConfirmationNumber = year+month+day+envtCode+uniqueSequence;
		log.debug("The web confirmation number is: "+webConfirmationNumber);

		return webConfirmationNumber;
	}
	private Document createChangeOrderForWebNumbers(YFSEnvironment env, String orderHeaderKey, String webConfirmationNumber, String envtCode, Element inputDocRoot) 
	{
		Document changeOrderInputDoc = null;
		String webLineNumber = null;	
		
		changeOrderInputDoc = YFCDocument.createDocument("Order").getDocument();
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, orderHeaderKey);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTERPRISE_CODE, XPXLiterals.A_ORGANIZATIONCODE_VALUE);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_DOCUMENT_TYPE, "0001");
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OVERRIDE, XPXLiterals.BOOLEAN_FLAG_Y);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ACTION, XPXLiterals.MODIFY);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_TYPE, XPXLiterals.CUSTOMER);
		
		Element orderExtn = changeOrderInputDoc.createElement(XPXLiterals.E_EXTN);
		orderExtn.setAttribute(XPXLiterals.A_WEB_CONF_NUMBER, webConfirmationNumber);
		changeOrderInputDoc.getDocumentElement().appendChild(orderExtn);
		
		Element changeOrderLinesElement = changeOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINES);
		
		Element orderLines = (Element) inputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);
		NodeList orderLinesList = orderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);
		for(int i=0; i<orderLinesList.getLength(); i++)
		{
			Element orderLineElement = (Element) orderLinesList.item(i);
			String orderLineKey = orderLineElement.getAttribute(XPXLiterals.A_ORDER_LINE_KEY);
			
			Element changeOrderLineElement = changeOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINE);
			changeOrderLineElement.setAttribute(XPXLiterals.A_ORDER_LINE_KEY, orderLineKey);
            //Getting the next unique sequence number from the custom sequence created
			long uniqueSequenceNo = CallDBSequence.getNextDBSequenceNo(env, XPXLiterals.WEB_LINE_SEQUENCE);
			/* changes made to fix issue 926 */
			// webLineNumber = generateWebLineNumber(uniqueSequenceNo,envtCode);
			webLineNumber = XPXAddParametersAPI.generateWebLineNumber(entryType, uniqueSequenceNo,envtCode);
			log.debug("webLineNumber :::: " + webLineNumber);
			
			Element orderLineExtn = changeOrderInputDoc.createElement(XPXLiterals.E_EXTN);
			orderLineExtn.setAttribute(XPXLiterals.A_WEB_LINE_NUMBER, webLineNumber);
			changeOrderLineElement.appendChild(orderLineExtn);
			
			changeOrderLinesElement.appendChild(changeOrderLineElement);
		}
		
		changeOrderInputDoc.getDocumentElement().appendChild(changeOrderLinesElement);
		
		
		return changeOrderInputDoc;
	}
	
//	public static String generateWebLineNumber(long uniqueSequenceNo,String envtCode) {
//		String webLineNumber = "";
//		String uniqueSequence = "";
//		String formatted = "";
//
//		int uniqueSequenceLength = 8;
//		int keyLength = String.valueOf(uniqueSequenceNo).length();
//		log.debug("KeyLength = "+keyLength);
//		if(keyLength < 8)
//		{
//			formatted = String.format("%08d", uniqueSequenceNo); 
//
//			log.debug("Number with leading zeros: " + formatted); 
//		}
//		else if(keyLength > 8)
//		{
//
//			int startIndex = keyLength-uniqueSequenceLength;
//			formatted = String.valueOf(uniqueSequenceNo).substring(startIndex);
//
//		}
//		else
//		{
//			formatted = String.valueOf(uniqueSequenceNo);
//		}
//
//		webLineNumber = envtCode+formatted;
//
//		return webLineNumber;
//	}
	private Document createChangeOrderInputDoc(String orderHeaderKey, String shipNode, YFSEnvironment env, String webConfirmationNumber, String envtCode, Element inputDocRoot) {
		
		String webLineNumber = null;
		
		Document getChangeOrderInputDoc = YFCDocument.createDocument(XPXLiterals.E_ORDER).getDocument();
        getChangeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, orderHeaderKey);
        getChangeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_SHIP_NODE, shipNode);
		getChangeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTERPRISE_CODE, XPXLiterals.A_ORGANIZATIONCODE_VALUE);
		getChangeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_DOCUMENT_TYPE, "0001");
		getChangeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OVERRIDE, XPXLiterals.BOOLEAN_FLAG_Y);
		getChangeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ACTION, XPXLiterals.MODIFY);
		
		Element orderExtn = getChangeOrderInputDoc.createElement(XPXLiterals.E_EXTN);
		orderExtn.setAttribute(XPXLiterals.A_WEB_CONF_NUMBER, webConfirmationNumber);
		getChangeOrderInputDoc.getDocumentElement().appendChild(orderExtn);
		
		Element changeOrderLinesElement = getChangeOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINES);
		
		Element orderLines = (Element) inputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);
		NodeList orderLinesList = orderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);
		for(int i=0; i<orderLinesList.getLength(); i++)
		{
			Element orderLineElement = (Element) orderLinesList.item(i);
			String orderLineKey = orderLineElement.getAttribute(XPXLiterals.A_ORDER_LINE_KEY);
			
			Element changeOrderLineElement = getChangeOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINE);
			changeOrderLineElement.setAttribute(XPXLiterals.A_ORDER_LINE_KEY, orderLineKey);
            //Getting the next unique sequence number from the custom sequence created
			long uniqueSequenceNo = CallDBSequence.getNextDBSequenceNo(env, XPXLiterals.WEB_LINE_SEQUENCE);
			/* changes made to fix issue 926 */
			// webLineNumber = generateWebLineNumber(uniqueSequenceNo,envtCode);
			webLineNumber = XPXAddParametersAPI.generateWebLineNumber(entryType, uniqueSequenceNo,envtCode);
			log.debug("webLineNumber :::: " + webLineNumber);
			
			Element orderLineExtn = getChangeOrderInputDoc.createElement(XPXLiterals.E_EXTN);
			orderLineExtn.setAttribute(XPXLiterals.A_WEB_LINE_NUMBER, webLineNumber);
			changeOrderLineElement.appendChild(orderLineExtn);
			
			changeOrderLinesElement.appendChild(changeOrderLineElement);
		}
		
		getChangeOrderInputDoc.getDocumentElement().appendChild(changeOrderLinesElement);
        
        return getChangeOrderInputDoc;
		
	}
	private Document createGetCustomerListInputDoc(String buyerOrganizationCode) {

            YFCDocument getCustomerListInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
            
            getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, buyerOrganizationCode);
            
            getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, XPXLiterals.A_ORGANIZATIONCODE_VALUE);
            
                       
            return getCustomerListInputDoc.getDocument();
		
	}
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Properties;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.Error;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**@author asekhar-tw on 10-Jan-2011
 * Using the literals used for CENT tool logging 
 */
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.customermanagement.api.XPXCustomerXReferenceFeed;
/**@author asekhar-tw on 11-Jan-2011
 * For logging
 */
import com.yantra.yfc.log.YFCLogCategory;

public class XPXSendPOAckOnCreateOrderAPI implements YIFCustomApi{

	private static YIFApi api = null;
	String getOrderListTemplate = "global/template/api/getOrderList.XPXSendPOAckOnCreateOrderAPI.xml";
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXLegacyOrderCreationService.xml";
	String getOrderListgetOrderNoTemplate = "global/template/api/getOrderList.XPXGetOrderNo.xml";

	/**@author asekhar-tw on 11-Jan-2011
	 * For error logging
	 */
	private static YFCLogCategory yfcLogCatalog;
	static{
		
		try {
			api = YIFClientFactory.getInstance().getApi();
			
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		yfcLogCatalog = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");	
	}
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	//sendPOAckWrapper
	
	public Document sendPOAckWrapper(YFSEnvironment env, Document inXML) throws Exception
	
	{
		/**@author asekhar-tw on 21-Jan-2011
		 * Added try-catch
		 */
		try{
			
		if(yfcLogCatalog.isDebugEnabled()){
			yfcLogCatalog.debug("SendPOAckWrapper_InXML :" + SCXmlUtil.getString(inXML));
		}
		/*****Start JIRA fix for # 2318 by Prasanth Kumar M.****************************/
		//String strExtnWebConfNum = env.getTxnObject("strExtnWebConfNum").toString();
		String strExtnWebConfNum ="";
		if(SCXmlUtil.getXpathElement(inXML.getDocumentElement(), "./WebConfirmationNumber")!=null)
		{
			strExtnWebConfNum = SCXmlUtil.getXpathElement(inXML.getDocumentElement(), "./WebConfirmationNumber").getTextContent();
		   
		}
		if(yfcLogCatalog.isDebugEnabled()){
			yfcLogCatalog.debug("strExtnWebConfNum : " + strExtnWebConfNum);
		}
		String legacyOrderNumber ="";
		if(SCXmlUtil.getXpathElement(inXML.getDocumentElement(), "./LegacyOrderNumber")!=null)
		{
			legacyOrderNumber = SCXmlUtil.getXpathElement(inXML.getDocumentElement(), "./LegacyOrderNumber").getTextContent();
		   
		}
		if(yfcLogCatalog.isDebugEnabled()){
			yfcLogCatalog.debug("legacyOrderNumber : " + legacyOrderNumber);
		}
		/***********End JIRA fix for # 2318**********************************************/
		if(strExtnWebConfNum == null){
			YFSException exceptionMessage = new YFSException();
			exceptionMessage.setErrorDescription("ExtnWebConfNum is empty in XPXSendPOAckOnCreateOrderAPI.java");		
			yfcLogCatalog.error("ExtnWebConfNum is empty in XPXSendPOAckOnCreateOrderAPI.java");				
			throw exceptionMessage;
		}
		
		//invoke getOrderList and obtain order no and invoke SendPOAckOnCreateOrder
		
		// String strOrderNo = getOrderNo(env,strExtnWebConfNum);
		
		//prepare input xml to SendPOAckOnCreateOrder
		//<Order OrderNo="Y100000702"/>
		
		Document sendPOAckOnCreateOrderInDoc = SCXmlUtil.getDocumentBuilder().newDocument();
		Element orderElem = sendPOAckOnCreateOrderInDoc.createElement("Order");
		// orderElem.setAttribute("OrderNo", strOrderNo);
		Element extnElement = sendPOAckOnCreateOrderInDoc.createElement("Extn");
		extnElement.setAttribute("ExtnWebConfNum", strExtnWebConfNum);	
		
		/*****Start JIRA fix for # 2318 by Prasanth Kumar M.****************************/
		extnElement.setAttribute("ExtnLegacyOrderNo", legacyOrderNumber);	
		/***********End JIRA fix for # 2318**********************************************/
		
		orderElem.appendChild(extnElement);
		
		sendPOAckOnCreateOrderInDoc.appendChild(orderElem);
		if(yfcLogCatalog.isDebugEnabled()){
			yfcLogCatalog.debug("SendPOAckOnCreateOrderInDoc : " + SCXmlUtil.getString(sendPOAckOnCreateOrderInDoc));
		}
		api.executeFlow(env, "SendPOAckOnCreateOrder", sendPOAckOnCreateOrderInDoc);
		
		}catch (NullPointerException ne) {
			yfcLogCatalog.error("NullPointerException: " + ne.getStackTrace());	
			prepareErrorObject(ne, XPXLiterals.PO_ACK_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inXML);	
			/** Since this is part of a large transaction, in case of an exception we dont need it to get rolled back, but just handling it **/
			//throw ne;
		} catch (YFSException yfe) {
			yfcLogCatalog.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.PO_ACK_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inXML);			
		} catch (Exception e) {
			yfcLogCatalog.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.PO_ACK_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inXML);
		}	
		
		
		return inXML;
		
		
	}
	
	private String getOrderNo(YFSEnvironment env, String strExtnWebConfNum) throws ParserConfigurationException, FactoryConfigurationError, YIFClientCreationException, YFSException, RemoteException {
		
		//prepare input xml to getOrderList
		/** <Order OrderType="Customer" >
		  * <Extn ExtnWebConfNum="110113DEV1489852"/>
		  *  </Order>*/
		
		Document getOrderListInDoc = SCXmlUtil.getDocumentBuilder().newDocument();
		Element orderElem = getOrderListInDoc.createElement("Order");
		getOrderListInDoc.appendChild(orderElem);
		
		orderElem.setAttribute("OrderType", "Customer");
		
		Element extnElem = getOrderListInDoc.createElement("Extn");
		orderElem.appendChild(extnElem);
		
		extnElem.setAttribute("ExtnWebConfNum", strExtnWebConfNum);
		
		//set the output template for getOrderList 
		env.setApiTemplate("getOrderList", getOrderListgetOrderNoTemplate);
		api = YIFClientFactory.getInstance().getApi();
		Document getOrderListOutDoc = api.invoke(env, "getOrderList", getOrderListInDoc);
		env.clearApiTemplate("getOrderList");
		if(yfcLogCatalog.isDebugEnabled()){
			yfcLogCatalog.debug("getOrderListOutDoc :" + SCXmlUtil.getString(getOrderListOutDoc));
		}
		Element orderOutElem = (Element) getOrderListOutDoc.getElementsByTagName("Order").item(0);
		String strOrderNo = orderOutElem.getAttribute("OrderNo");
		if(yfcLogCatalog.isDebugEnabled()){
			yfcLogCatalog.debug("strOrderNo : " + strOrderNo);
		}
		return strOrderNo;
	}

	public Document sendPOAck(YFSEnvironment env, Document inXML) throws Exception
																	
	{
		Document customerOrderDoc = null;

		boolean poACKRequired = false;
		String sendPOACK = "N";
// 		ArrayList<String> orderArray = new ArrayList<String>();
		api = YIFClientFactory.getInstance().getApi();
		String response = "N";
//		String etradingID = "";
		String liasonMsgId = "";
		
		String orderHeaderKey = "";
//	    orderArray = getOrderDetails(env,inXML);
		
//
//		/**@author asekhar-tw on 10-Jan-2011
//		 * Handling the scecnario where getOrderList API returns no order details
//		 */
//		if(orderArray.size() == 0){
//			prepareErrorObject(new IndexOutOfBoundsException(), XPXLiterals.PO_ACK_TRANS_TYPE, XPXLiterals.IOBE_ERROR_CLASS, env, inXML);
//			throw new IndexOutOfBoundsException();			
//		}
		
		
		
		// liasonMsgId = orderArray.get(3);
		Element inputElement = inXML.getDocumentElement();
		//form the input xml to get the FO order...the name of the method is a misnomer and should not be confused with CO. As per B2B DDD, this Customer Order
		// refers to the FO and was changed as per reqmt for JIRA # 2318.  
		customerOrderDoc = formInputToGetCustomerOrder(env, inputElement);
		Element orderElement = customerOrderDoc.getDocumentElement();
//		etradingID = SCXmlUtil.getXpathAttribute(orderElement, "./Extn/@ExtnETradingID");
		orderHeaderKey = orderElement.getAttribute("OrderHeaderKey");
		env.setTxnObject("orderHeaderKey", orderHeaderKey);		
		liasonMsgId = SCXmlUtil.getXpathAttribute(orderElement, "./Extn/@ExtnMsgHeaderId");		
		
		//check if PO ack needs to be sent.
		poACKRequired = checkIfPOACKRequired(env,inputElement);		
		Element customerOrderElement = customerOrderDoc.getDocumentElement();		
		if(poACKRequired)
		{
			//check if the ack is sent or not			
			String poAckSent = SCXmlUtil.getXpathAttribute(customerOrderElement, "./Extn/@ExtnIsProcessedFlag");
			//String poAckSent = "N";
			if(poAckSent.equals("N"))
			{
				//logic for first stock order
				// response = checkWhenAckHasToBeSent(env, inputElement,customerOrderDoc);
				response = "Y";				
			}
			
//			customerOrderElement.setAttribute("Response", response);
			
			
			//check for mark complete on reference order
			//get the reference order detail
			//for reference order after successful creation of sterling order
			getReferenceOrderDetails(env, liasonMsgId, customerOrderDoc);
		}
		customerOrderElement.setAttribute("Response", response);		
		return customerOrderDoc;
	}

	/**@author asekhar-tw on 10-Jan-2011
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
	
	private void getReferenceOrderDetails(YFSEnvironment env,
			String liasonMsgId, Document customerOrderDoc)
			throws RemoteException {
		Document inputCustomRefDoc = YFCDocument.createDocument("XPXRefOrderHdr").getDocument();
		Element inputCustomRefElement = inputCustomRefDoc.getDocumentElement();
		//inputCustomRefElement.setAttribute("EtradingID", etradingID);
		/***********Fixed as part of JIRA bug # 1840 by Prasanth Kumar M.****************************************/
		//inputCustomRefElement.setAttribute("LiasonMsgID", liasonMsgId);
		inputCustomRefElement.setAttribute("MsgHeaderId", liasonMsgId);
		/***********************************************************************************/
		Document customReferenceOrderList = api.executeFlow(env, "getXPXRefOrderHdrList", inputCustomRefDoc);
		NodeList customRefNodeList = customReferenceOrderList.getElementsByTagName("XPXRefOrderHdr");
		int customRefLength = customRefNodeList.getLength();
		Document customRefDoc = null;
		Element customRefElement = null;
		if(customRefLength != 0)
		{
			customRefElement = (Element)customRefNodeList.item(0);
			customRefDoc = YFCDocument.createDocument().getDocument();
			customRefDoc.appendChild(customRefDoc.importNode(customRefElement, true));
			customRefDoc.renameNode(customRefDoc.getDocumentElement(), customRefDoc.getNamespaceURI(), "XPXRefOrderHdr");
		}
		String markComplete = SCXmlUtil.getXpathAttribute(customRefElement, "./Extn/@ExtnMarkComplete");
		if(!markComplete.equals("Y")){
		Node customReferenceNode = customerOrderDoc.importNode(customRefElement, true);
		Element customReferenceElement = (Element)customReferenceNode;
		customerOrderDoc.getDocumentElement().appendChild(customReferenceElement);
		
		//change the reference order to mark it processed
		//form the input doc
		Document inputRefOrderDoc = SCXmlUtil.createDocument("XPXRefOrderHdr");
		Element inputrefOrderElement = inputRefOrderDoc.getDocumentElement();
		inputrefOrderElement.setAttribute("IsProcessedFlag", "Y");
		inputrefOrderElement.setAttribute("RefOrderHdrKey", customReferenceElement.getAttribute("RefOrderHdrKey"));
		api.executeFlow(env, "changeXPXRefOrderHdr", inputRefOrderDoc);
		
		
		}
	}
	
	private String checkWhenAckHasToBeSent(YFSEnvironment env, Element inputElement, Document customerOrderDoc) throws YFSException, RemoteException
	{
		ArrayList<String> orderTypeArray = new ArrayList<String>();
		String response = "N";
		String orderType = "";
//		String inputOrderType = "";
		String inputOrderNumber = "";
//		inputOrderType = inputElement.getAttribute("OrderType");
//      inputOrderNumber(fullfilment order) should be stamped in the transaction object in XPXCreateFulfillmentOrderAPI.java
		// start - Changes made on 01/03/2011
//		inputOrderNumber = inputElement.getAttribute("OrderNo");
		try{
		inputOrderNumber = env.getTxnObject("fullfilmentOrderNo").toString();
		if(yfcLogCatalog.isDebugEnabled()){
			yfcLogCatalog.debug("inputOrderNumber : " + inputOrderNumber);
		}
		}catch(Exception e){
			// Error logged in CENT tool if the fullfilment Order number is empty.
			YFSException exceptionMessage = new YFSException();
			exceptionMessage.setErrorDescription("Fullfilment Order Number is empty in XPXSendPOAckOnCreateOrderAPI.java");		
			yfcLogCatalog.error("Fullfilment Order Number is empty in XPXSendPOAckOnCreateOrderAPI.java");				
			throw exceptionMessage;		
		}
		// End - Changes made on 01/03/2011 
		//form the input doc
		Document inputOrderDoc = SCXmlUtil.createDocument("Order");
		Element inputOrderElement = inputOrderDoc.getDocumentElement();
		Element extnElement = inputOrderDoc.createElement("Extn");
		extnElement.setAttribute("ExtnWebConfNum", SCXmlUtil.getXpathAttribute(inputElement, "./Extn/@ExtnWebConfNum"));
		inputOrderElement.appendChild(extnElement);
		env.setApiTemplate("getOrderList", getOrderListTemplate);
		Document orderListDoc = api.invoke(env, "getOrderList", inputOrderDoc);
		env.clearApiTemplate("getOrderList");
		NodeList orderNodeList = orderListDoc.getElementsByTagName("Order");
		int orderLength = orderNodeList.getLength();
		//if only customer order exists and legacy order was not created
		if(orderLength == 1)
		{
		response = sendPOAckEvenIfLegacyOrderCreationFails(response, orderLength);
		}
		if(orderLength > 1)
		{
		//if the customer order has non stock items then send it on the first legacy order created
		populateOrderTypeArray(orderTypeArray, orderNodeList, orderLength);
		
		if(!orderTypeArray.contains("STOCK_ORDER"))
		{
			// For Direct Orders.
		response = sendResponseForNonStockItemOrder(orderTypeArray, response,
				inputOrderNumber, orderNodeList);
		}
		//if legacy order is created for the first stock order we send response
		if(orderTypeArray.contains("STOCK_ORDER"))
		{
			// For Stock Order.
		response = sendResponseForFirstStock(orderTypeArray, response,
				inputOrderNumber, orderNodeList, orderLength);
		}
		}
		
		return response;
	}

	private String sendResponseForFirstStock(ArrayList<String> orderTypeArray,
			String response, String inputOrderNumber, NodeList orderNodeList,
			int orderLength) {
		String firstStock = "";
		String firstStockOrderNo = "";
		if(orderTypeArray.contains("STOCK_ORDER"))
		{
			for(int orderCounter = 1 ;orderCounter<orderLength;orderCounter++)
			{
				Element orderElement = (Element)orderNodeList.item(orderCounter);
				if(orderElement.getAttribute("OrderType").equals("STOCK_ORDER"))
				{
					firstStockOrderNo = orderElement.getAttribute("OrderNo");
					break;
					
				}
			}
			if(inputOrderNumber.equals(firstStockOrderNo))
			{
				response = "Y";
			}
		}
		return response;
	}

	private String sendResponseForNonStockItemOrder(
			ArrayList<String> orderTypeArray, String response,
			String inputOrderNumber, NodeList orderNodeList) {
		if(!orderTypeArray.contains("STOCK_ORDER"))
		{
			//get the first orderElement
			Element orderElement = (Element)orderNodeList.item(1);
			if(inputOrderNumber.equals(orderElement.getAttribute("OrderNo")))
			{
				response = "Y";
			}
		}
		return response;
	}

	private void populateOrderTypeArray(ArrayList<String> orderTypeArray,
			NodeList orderNodeList, int orderLength) {
		if(orderLength > 1)
		{
			
			//check if order is non stock item order
			for(int orderCounter = 0 ;orderCounter<orderLength;orderCounter++)
			{
				Element orderElement = (Element)orderNodeList.item(orderCounter);
				String orderType1 = orderElement.getAttribute("OrderType");
				orderTypeArray.add(orderType1);
				
				
			}
		}
	}

	private String sendPOAckEvenIfLegacyOrderCreationFails(String response,
			int orderLength) {
		if(orderLength == 1)
		{
			response = "Y";
		}
		return response;
	}
	
	private boolean checkIfPOACKRequired(YFSEnvironment env, Element inputElement) throws YFSException, RemoteException,Exception
	{
		String customerID = "";
		String mSAPCustomerkey = "";
		boolean isPOACKRequired = false;
		//customerID = inputElement.getAttribute("BillToID");		
		customerID = inputElement.getAttribute("BuyerOrganizationCode");		
		//To get the customer details
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerID", customerID);
		env.setApiTemplate("getCustomerList", getCustomerListTemplate);
		Document getCustomerListOutputDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		if(getCustomerListOutputDoc != null){
			yfcLogCatalog.debug("The shipTo getCustomerList is: "+SCXmlUtil.getString(getCustomerListOutputDoc));
		}
		Element customerElement = (Element) getCustomerListOutputDoc.getDocumentElement().getElementsByTagName("Customer").item(0);
		Element parentCustomerElement = (Element) customerElement.getElementsByTagName("ParentCustomer").item(0);
		String billToCustomerId  = parentCustomerElement.getAttribute("CustomerID");
		
		inputCustomerElement.setAttribute("CustomerID", billToCustomerId);
		Document getBillToCustomerListOutputDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		if(getBillToCustomerListOutputDoc != null){
			yfcLogCatalog.debug("The billTo getCustomerList is: "+SCXmlUtil.getString(getBillToCustomerListOutputDoc));
		}
		Element billToCustomerElement = (Element) getBillToCustomerListOutputDoc.getDocumentElement().getElementsByTagName("Customer").item(0);
		Element billToParentCustomerElement = (Element) billToCustomerElement.getElementsByTagName("ParentCustomer").item(0);
		String sapCustomerId  = billToParentCustomerElement.getAttribute("CustomerID");
		inputCustomerElement.setAttribute("CustomerID", sapCustomerId);
		Document getSAPCustomerListOutputDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		if(getSAPCustomerListOutputDoc != null){
			yfcLogCatalog.debug("The SAP getCustomerList is: "+SCXmlUtil.getString(getSAPCustomerListOutputDoc));
		}
		Element sapCustomerElement = (Element) getSAPCustomerListOutputDoc.getDocumentElement().getElementsByTagName("Customer").item(0);
		Element sapExtnCustomerElement = (Element) sapCustomerElement.getElementsByTagName("Extn").item(0);
		String poAckRequired  = sapExtnCustomerElement.getAttribute("ExtnPOAckFlag");
		if(null != poAckRequired && poAckRequired.equalsIgnoreCase("Y")){
			isPOACKRequired = true;						
		}
		/*YFCDocument customerListDoc = YFCDocument.getDocumentFor(api.invoke(env, "getCustomerList", inputCustomerDoc));		
		
		// To get the MSAP customer key.
		if(customerListDoc != null){
			YFCElement customerListElement = customerListDoc.getDocumentElement();			
			YFCNodeList customerList = customerListElement.getElementsByTagName("Customer");
			if(customerList.getLength() > 0){
				YFCElement custElement = (YFCElement) customerList.item(0);				
				mSAPCustomerkey = custElement.getAttribute("RootCustomerKey");
			}			
		}*/
		/*yfcLogCatalog.debug("MSAPCustomerkey = " + mSAPCustomerkey);
		log.debug("MSAPCustomerkey = " + mSAPCustomerkey);*/
		// To check PO Ack is required.
		/*if(mSAPCustomerkey != null && mSAPCustomerkey != ""){
			// removed the attribute below to re-use the existing document.
			inputCustomerElement.removeAttribute("CustomerID");
			inputCustomerElement.setAttribute("CustomerKey", mSAPCustomerkey);						
			Document mSAPCustomerListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
			if(mSAPCustomerListDoc != null){
				Element mSAPCustomerListElement = mSAPCustomerListDoc.getDocumentElement();	
				NodeList mSAPCustomerList = mSAPCustomerListElement.getElementsByTagName("Customer");
				if(mSAPCustomerListElement != null && mSAPCustomerList.getLength() > 0){
					Element mSAPCustElement = (Element) mSAPCustomerList.item(0);					
					String poAckRequired  = SCXmlUtil.getXpathAttribute(mSAPCustElement, "./Extn/@ExtnPOAckFlag");
					if(null != poAckRequired && poAckRequired.equalsIgnoreCase("Y")){
						isPOACKRequired = true;						
					}
				}
			}
		} else {
			throw new Exception("PO Acknowledgement will not be sent as MSAP customer isn't available.");
		}*/		
		env.clearApiTemplate("getCustomerList");
		if(!YFCObject.isNull(isPOACKRequired) && !YFCObject.isVoid(isPOACKRequired)) {
			yfcLogCatalog.debug("isPOACKRequired = " + isPOACKRequired);
		}
		return isPOACKRequired;
	}
	
	private Document formInputToGetCustomerOrder(YFSEnvironment env, Element inputElement) throws YFSException, RemoteException
	{
		Document outDoc = null;
		Document inputOrderDoc = SCXmlUtil.createDocument("Order");
		Element inputOrderElement = inputOrderDoc.getDocumentElement();
		
		/*****Start JIRA fix for # 2318 by Prasanth Kumar M.****************************/
		/*inputOrderElement.setAttribute(XPXLiterals.A_ORDER_TYPE, XPXLiterals.CUSTOMER);
		inputOrderElement.setAttribute("OrderTypeQryType", "EQ");*/
		Element extnElement = inputOrderDoc.createElement("Extn");
		extnElement.setAttribute("ExtnWebConfNum", SCXmlUtil.getXpathAttribute(inputElement, "./Extn/@ExtnWebConfNum"));
		extnElement.setAttribute("ExtnLegacyOrderNo", SCXmlUtil.getXpathAttribute(inputElement, "./Extn/@ExtnLegacyOrderNo"));
		/***********End JIRA fix for # 2318**********************************************/
		
		inputOrderElement.appendChild(extnElement);
		env.setApiTemplate("getOrderList", getOrderListTemplate);
		Document orderListDoc = api.invoke(env, "getOrderList", inputOrderDoc);
		env.clearApiTemplate("getOrderList");
		NodeList orderNodeList = orderListDoc.getElementsByTagName("Order");
		int orderLength = orderNodeList.getLength();
		if(orderLength != 0)
		{
			Element orderElement = (Element)orderNodeList.item(0);
			// Changes made on 25/feb to fix issue 855. 'BillToID' attribute will be used to check if PO Acknowledgement is required or not.
            //	inputElement.setAttribute("BillToID", orderElement.getAttribute("BillToID"));
			//Change made for JIRA 1490..Inheritance Hierarchy
			inputElement.setAttribute("BuyerOrganizationCode", orderElement.getAttribute("BuyerOrganizationCode"));
			outDoc = YFCDocument.createDocument().getDocument();
			outDoc.appendChild(outDoc.importNode(orderElement, true));
			outDoc.renameNode(outDoc.getDocumentElement(), outDoc.getNamespaceURI(), "Order");
		}
		return outDoc;
	}
	
	private ArrayList<String> getOrderDetails(YFSEnvironment env, Document inputXML) throws YFSException, RemoteException
	{
		ArrayList<String> orderArray = new ArrayList<String>();
		//ExtnETradingID
		String etradingID = "";
		String orderNo = "";
		String orderHeaderKey = "";
		String liasonMsgID = "";
		//form the inputDoc
		Document inputDoc = SCXmlUtil.createDocument("Order");
		Element inputElement = inputDoc.getDocumentElement();
		inputElement.setAttribute("OrderNo", inputXML.getDocumentElement().getAttribute("OrderNo"));
		env.setApiTemplate("getOrderList", getOrderListTemplate);
		Document orderListDoc = api.invoke(env, "getOrderList", inputDoc);
		env.clearApiTemplate("getOrderList");
		NodeList orderNodeList = orderListDoc.getElementsByTagName("Order");
		int orderLength = orderNodeList.getLength();
		
		if(orderLength != 0)
		{
			Element orderElement = (Element)orderNodeList.item(0);
			orderNo = orderElement.getAttribute("OrderNo");
			etradingID = SCXmlUtil.getXpathAttribute(orderElement, "./Extn/@ExtnETradingID");
			orderHeaderKey = orderElement.getAttribute("OrderHeaderKey");
			liasonMsgID = SCXmlUtil.getXpathAttribute(orderElement, "./Extn/@ExtnLiasonMsgID");
			orderArray.add(orderNo);
			orderArray.add(etradingID);
			orderArray.add(orderHeaderKey);
			orderArray.add(liasonMsgID);
		}
		return orderArray;
	}

}

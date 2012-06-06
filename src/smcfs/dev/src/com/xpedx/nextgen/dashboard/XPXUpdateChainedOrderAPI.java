package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.xpedx.nextgen.customerprofilerulevalidation.api.XPXCustomerProfileRuleConstant;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXUpdateChainedOrderAPI implements YIFCustomApi{
	
	private Element inputElement;
	private String customerOrderHeaderKey = "";
	ArrayList<String> fulfillmentOrderDetails = new ArrayList<String>();
	ArrayList<String> customerOrderDetails = new ArrayList<String>();
	String entryType = "";	
	String envtCode = "";
	List custOrdStatusList = new ArrayList();
	List cancelledQtyWebLineNoList = new ArrayList();
	
	Hashtable<String, Element> htCurrentOrder = new Hashtable<String, Element>();
	Hashtable<String, Element> htFulfillmentOrder = new Hashtable<String, Element>();
	Hashtable<String, Element> htCustomerOrder = new Hashtable<String, Element>();
	
	Hashtable<String, Element> htChangeFulfillmentOrder = new Hashtable<String, Element>();
	Hashtable<String, Element> htChangeStatusOnCustomerOrder = new Hashtable<String, Element>();
	Hashtable<String, Element> htChangeCustomerOrder = new Hashtable<String, Element>();
	Hashtable<String, Element> htLegacyOrder = new Hashtable<String, Element>();
	Hashtable<String, Document> htDirectOrder = new Hashtable<String, Document>();
	
	Document sOrderToLegacyDoc = YFCDocument.createDocument().getDocument();
	Document inputOrderStatusChangeDoc = YFCDocument.createDocument("OrderStatusChange").getDocument();
	
	Float newFOExtnTotalOrderValue = new Float(0);
	Float currFOExtnTotalOrderValue = new Float(0);
	Float newCOExtnTotalOrderValue = new Float(0);
	Float currCOExtnTotalOrderValue = new Float(0);
	
	private static YFCLogCategory log;
	private static YIFApi api = null;
	
	String changeOrderTemplate = "global/template/api/createOrder.XPXLegacyOrderCreationService.xml";
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXUpdateChainedOrderAPI.xml";
	
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
	
	public XPXUpdateChainedOrderAPI(){
		inputElement=null;
	}

	public Document updateChainedOrder(YFSEnvironment env, Document inputXML)  throws Exception
	{
		log.beginTimer("XPXUpdateChainedOrderAPI.updateChainedOrder");
		Document directOrderDoc = null;
		if(log.isDebugEnabled()){
		log.debug("**********************************************************************************************************************");
		log.debug("");
		log.debug("XPXUpdateChainedOrderAPI-InXML:"+YFCDocument.getDocumentFor(inputXML).getString());
		log.debug("");
		}
		log.verbose("**********************************************************************************************************************");
		log.verbose("");
		log.verbose("XPXUpdateChainedOrderAPI-InXML:"+YFCDocument.getDocumentFor(inputXML).getString());
		log.verbose("");
		
		inputElement =  inputXML.getDocumentElement();
		
		//1. Get Fulfillment Order Details, populate Fulfillment Order Hashtable, build Header of SterlingOrder to Legacy with fulfillment Order
		getFulfillmentOrderDetails(env,inputElement);
		
		//2. Get Customer Order Details and populate Customer Order Hashtable
		getCustomerOrderDetails(env,inputElement);
		
		//3. Populate Current Order hashtable
		populateCurrentOrderHashtable(env,inputElement);
		
		// To set web hold flag as 'Y' if instruction exist.
		checkInstructionsInOrder(env, inputElement);
		
		//4. Loop through current order XML and identify changes
		identifyChanges(env);
		
		//5. invoke changeOrder on Fulfillment Order
		Document changeOrderFODoc = callChangeFulfillmentOrder(env, inputElement);
		
		//create new Fulfillment order for line type change.
		//createNewFulfillmentOrder(env);
		
		//6. invoke changeOrder on Customer Order
		int changeCustomerOrderSize = htChangeCustomerOrder.size();
		if(changeCustomerOrderSize>0)
		{
		callChangeCustomerOrder(env, inputElement);
		}
		
		int directSize = htDirectOrder.size();
		if(directSize != 0)
		{
			// To create Direct Fulfillment order.
			Document createOrderDoc = htDirectOrder.get("Direct");
			if(createOrderDoc != null){
			log.debug("XPXUpdateChainedOrderAPI_DirectOrderCreateDoc:"+SCXmlUtil.getString(createOrderDoc));
			}
			env.setApiTemplate("createOrder",changeOrderTemplate);
			directOrderDoc = api.invoke(env, "createOrder", createOrderDoc);
			if(directOrderDoc != null){
			log.debug("XPXUpdateChainedOrderAPI_DirectOrderCreateOutputDoc:"+SCXmlUtil.getString(directOrderDoc));
			}
			env.clearApiTemplate("createOrder");
		}
		
		//6. Call changeOrderStatus on Customer Order
		callChangeOrderStatusOnCustomerOrder(env);
		
		//7. Business rule validation for fulfillment order.
		Document businessRuleOutputDoc = api.executeFlow(env, "XPXBusinessRuleValidationFOService", inputXML);
		
		//8. Send Sterling Order to Legacy
		sendUpdatedOrderToLegacy(env,businessRuleOutputDoc,changeOrderFODoc,directOrderDoc);
		 
		log.endTimer("XPXUpdateChainedOrderAPI.updateChainedOrder");
		return inputXML;
	}
	
	public void identifyChanges(YFSEnvironment env) throws Exception{
		//1. Start loop on htCurrentOrder
		Enumeration<String> Enumeration = htCurrentOrder.keys (); 
	    while (Enumeration.hasMoreElements ()) { 
	        String weblinekey = (String) Enumeration.nextElement (); 

	        if(!YFCObject.isNull(weblinekey) && !YFCObject.isVoid(weblinekey)) {
	        log.debug("weblinekey of identifyChanges = " + weblinekey);	        
	        }
	        //2. Check if line exists on Fulfillment Order
	        Element foLineElement = htFulfillmentOrder.get(weblinekey);
	        if(foLineElement==null){
	        	//New Line - Add New line
	        	addNewLine(weblinekey, env);
	        }else{
	        	
	        	/************************/
	        	
	        	//check if it is a line type change
	        	
	        	/*boolean lineTypeChangeFlag = identifyLineTypeChanges(weblinekey);
	        	if(lineTypeChangeFlag)
	        	{
	        		//make the quantity zero in the fulfillment order line
	        		cancelLineOnFulfillment(weblinekey);
	        		
	        		
	        		
	        	}
	        	else
	        	{*/
	        	
	        	/**************************/
	        	 
	        	
	        	//3. check qty changes
	        	identifyQtyChanges(weblinekey);
	        	
	        	//4. check price changes
	        	identifyPriceChanges(weblinekey);
	        	
	        	//5. Check for other changes
	        	identifyOtherChanges(weblinekey);
	        	//}
	        	

	        	}
	        	
	       
	    }
	    //5. modify header level changes attributes if necessary
	    makeHeaderLevelChanges();
	}
	
	public void identifyOtherChanges(String weblineno){
		
		Element oLineElement = htCurrentOrder.get(weblineno);
		//Element foLineElement = htFulfillmentOrder.get(weblineno);
		//Element coLineElement = htCustomerOrder.get(weblineno);		
		//Element legacyLineElement = htLegacyOrder.get(weblineno);
		
		Element chFulLineElement = htChangeFulfillmentOrder.get(weblineno);
		if(chFulLineElement==null){
			chFulLineElement = (Element)oLineElement.cloneNode(true);
		}

		htChangeFulfillmentOrder.put(weblineno, chFulLineElement);
	}
	
	public void addNewLine(String weblinekey, YFSEnvironment env) throws Exception{
		String primeLineNo = "";
		ArrayList<String> lineTypeList = new ArrayList<String>();
		lineTypeList = identityLineTypeChange(weblinekey, env);
		String changeFlag = lineTypeList.get(0);
		if(changeFlag.equals("false"))
		{
		//check if the input line type and order time are same or different.
		//If its direct create a new fulfillment order
		//Else append it to the existing order.
		
		// 1. Line has to be first added on Customer Order
		Document inputDoc = YFCDocument.createDocument("Order").getDocument();
		Element orderEle = inputDoc.getDocumentElement();
		orderEle.setAttribute("OrderHeaderKey", customerOrderDetails.get(0));
		orderEle.setAttribute("DocumentType", "001");
		orderEle.setAttribute("OrganizationCode", "xpedx");
		
		Element orderLinesEle  = inputDoc.createElement("OrderLines");
		orderEle.appendChild(orderLinesEle);
		
		Element oLineElement = htCurrentOrder.get(weblinekey);
		//Change the orderline to suit the new line
		Element tempOLineElement = changeOrderLineForNewLine(oLineElement);
		//SCXmlUtil.getString(oLineElement);
		Element torderLineEle = (Element)inputDoc.importNode(tempOLineElement, true);
		orderLinesEle.appendChild(torderLineEle);
		
		Document templateDoc = setTemplateForChangeOrder(env);
		env.setApiTemplate("changeOrder", templateDoc);
		if(log.isDebugEnabled()){
		log.debug ("AddNewLine changeOrder input XML" + SCXmlUtil.getString(inputDoc) );
		}
		Document outputDoc = api.invoke(env, "changeOrder", inputDoc);
		if(log.isDebugEnabled()){
		log.debug ("AddNewLine changeOrder output XML" + SCXmlUtil.getString(outputDoc) );
		}
		
		// 2. Form Element on Fulfillment Order for input
		
		//looping through the outputDoc and fetching the PrimeLineNo.
		Element outOrderElement = outputDoc.getDocumentElement();
		
		NodeList outOrderLinesElementNL = outOrderElement.getElementsByTagName("OrderLines");
		Element outOrderLinesElement = (Element)outOrderLinesElementNL.item(0);
		
		NodeList outOrderLineElementNL = outOrderLinesElement.getElementsByTagName("OrderLine");
		for(int i=0; i<outOrderLineElementNL.getLength(); i++){
			Element outOrderLineElement = (Element)outOrderLineElementNL.item(i);
			String checkWebLineNo = SCXmlUtil.getXpathAttribute(outOrderLineElement, "./Extn/@ExtnWebLineNumber");
		    //String itemID = SCXmlUtil.getXpathAttribute(outOrderLineElement, "./Item/@ItemID");
		    //String shipNode = outOrderLineElement.getAttribute("ShipNode");
		      
		      //String checkWebLinekey = checkWebLineNo+"|"+itemID+"|"+shipNode;
			if(getWeblinenoFromWeblinekey(weblinekey).equals(checkWebLineNo)){
				primeLineNo = outOrderLineElement.getAttribute("PrimeLineNo");
			}
		}
		
		Document tempDoc = YFCDocument.createDocument().getDocument();
		//Element chFulLineElement = tempDoc.createElement("OrderLine");
		//stamp ExtnLineType on the orderline element 
		NodeList extnList = oLineElement.getElementsByTagName("Extn");
		int extnLength = extnList.getLength();
		if(extnLength != 0)
		{
			Element extnElement = (Element)extnList.item(0);
			extnElement.setAttribute("ExtnLineType", lineTypeList.get(2));
		}
		Element chFulLineElement = (Element)tempDoc.importNode(oLineElement, true);
		//chFulLineElement = (Element)oLineElement.cloneNode(true);
		
		Element chainedFromElement = tempDoc.createElement("ChainedFrom");
		chainedFromElement.setAttribute("DocumentType", "0001");
		chainedFromElement.setAttribute("OrganizationCode", "xpedx");
		chainedFromElement.setAttribute("OrderHeaderKey", customerOrderDetails.get(0));
		chainedFromElement.setAttribute("PrimeLineNo", primeLineNo);
		chainedFromElement.setAttribute("SubLineNo", "1");
		
		chFulLineElement.appendChild(chainedFromElement);
		htChangeFulfillmentOrder.put(weblinekey, chFulLineElement);
		
		//1. Adding to Legacy Order
		oLineElement.setAttribute("LineProcessCode", "A");
		htLegacyOrder.put(weblinekey, oLineElement);
		
		}
		else
		{			
			//new fulfillment order is created
			// 1. Line has to be first added on Customer Order
			Document inputDoc = YFCDocument.createDocument("Order").getDocument();
			Element orderEle = inputDoc.getDocumentElement();
			orderEle.setAttribute("OrderHeaderKey", customerOrderDetails.get(0));
			orderEle.setAttribute("DocumentType", "001");
			orderEle.setAttribute("OrganizationCode", "xpedx");
			
			Element orderLinesEle  = inputDoc.createElement("OrderLines");
			orderEle.appendChild(orderLinesEle);
			
			Element oLineElement = htCurrentOrder.get(weblinekey);
			Element orderLineEle = (Element)inputDoc.importNode(oLineElement, true);
			orderLinesEle.appendChild(orderLineEle);
			
			Document templateDoc = setTemplateForChangeOrder(env);
			env.setApiTemplate("changeOrder", templateDoc);
			if(log.isDebugEnabled()){
			log.debug ("AddNewLine changeOrder input XML" + SCXmlUtil.getString(inputDoc) );
			}
			Document outputDoc = api.invoke(env, "changeOrder", inputDoc);
			if(log.isDebugEnabled()){
			log.debug ("AddNewLine changeOrder output XML" + SCXmlUtil.getString(outputDoc) );
			}
			int directLength = htDirectOrder.size();
			if(directLength == 0)
			{
				createHtDirectOrder(weblinekey, env, primeLineNo, lineTypeList, outputDoc);
			}
			else
			{
				updateHtDirectOrder(weblinekey, primeLineNo, outputDoc, lineTypeList);	
			}		
		}	
	}
	
	protected Element changeOrderLineForNewLine(Element oLineInputElement){
		String orderedQty = SCXmlUtil.getAttribute(oLineInputElement, "OrderedQty");
		String lineType = SCXmlUtil.getAttribute(oLineInputElement, "LineType");
		
		Document orderLineDoc = YFCDocument.createDocument("OrderLine").getDocument();
		Element orderLineEle = orderLineDoc.getDocumentElement();
		orderLineEle.setAttribute("OrderedQty", orderedQty);
		orderLineEle.setAttribute("LineType", lineType);
		
		
		Element itemElement = SCXmlUtil.getChildElement(oLineInputElement, "Item");
		Element priceElement = SCXmlUtil.getChildElement(oLineInputElement, "LinePriceInfo");
		Element extnElement = SCXmlUtil.getChildElement(oLineInputElement, "Extn");
		Element instructionElement = SCXmlUtil.getChildElement(oLineInputElement, "Instructions");
		Element tranElement = SCXmlUtil.getChildElement(oLineInputElement, "OrderLineTranQuantity");
		Element notesElement = SCXmlUtil.getChildElement(oLineInputElement, "Notes");
		
		if(itemElement != null){
		Element iElement = (Element)orderLineDoc.importNode(itemElement, true);
		orderLineEle.appendChild(iElement);
		}
		
		if(priceElement != null){
			Element pElement = (Element)orderLineDoc.importNode(priceElement, true);
			orderLineEle.appendChild(pElement);
		}
		
		
		
		if(extnElement != null){
		Element eElement = (Element)orderLineDoc.importNode(extnElement, true);
		orderLineEle.appendChild(eElement);
		}
		
		if(instructionElement != null){
		Element inElement = (Element)orderLineDoc.importNode(instructionElement, true);
		orderLineEle.appendChild(inElement);
		}
		
		if(tranElement != null){
		Element tElement = (Element)orderLineDoc.importNode(tranElement, true);
		orderLineEle.appendChild(tElement);
		}
		
		if(notesElement != null){
		Element nElement = (Element)orderLineDoc.importNode(notesElement, true);
		orderLineEle.appendChild(nElement);
		}
		
		/*inputDoc.importNode(itemElement, true);
		inputDoc.importNode(extnElement, true);
		inputDoc.importNode(instructionElement, true);
		inputDoc.importNode(tranElement, true);
		inputDoc.importNode(notesElement, true);*/
		
		return orderLineEle;
	}

	private void updateHtDirectOrder(String weblinekey, String primeLineNo,
			Document outputDoc, ArrayList<String> lineTypeList) {
		Document createOrderDoc = htDirectOrder.get("Direct");
		Element createOrderElement = createOrderDoc.getDocumentElement();
		NodeList linesElement = createOrderElement.getElementsByTagName("OrderLines");
		Element orderLinesElement = (Element)linesElement.item(0);
		//Element orderLinesElement = createOrderDoc.createElement("OrderLines");
		//createOrderElement.appendChild(orderLinesElement);
		//get the newly added line
		Element addElement = htCurrentOrder.get(weblinekey);
		Node addNode = createOrderDoc.importNode(addElement, true);
		Element aElement = (Element)addNode;
		NodeList extnList = aElement.getElementsByTagName("Extn");
		int extnLength = extnList.getLength();
		if(extnLength != 0)
		{
			Element extnElement = (Element)extnList.item(0);
			extnElement.setAttribute("ExtnLineType", lineTypeList.get(2));
		}
		orderLinesElement.appendChild(aElement);
		
		//looping through the outputDoc and fetching the PrimeLineNo.
		Element outOrderElement = outputDoc.getDocumentElement();
		
		NodeList outOrderLinesElementNL = outOrderElement.getElementsByTagName("OrderLines");
		Element outOrderLinesElement = (Element)outOrderLinesElementNL.item(0);
		
		NodeList outOrderLineElementNL = outOrderLinesElement.getElementsByTagName("OrderLine");
		for(int i=0; i<outOrderLineElementNL.getLength(); i++){
			Element outOrderLineElement = (Element)outOrderLineElementNL.item(i);
			String checkWebLineNo = SCXmlUtil.getXpathAttribute(outOrderLineElement, "./Extn/@ExtnWebLineNumber");
		    //String itemID = SCXmlUtil.getXpathAttribute(outOrderLineElement, "./Item/@ItemID");
		    //String shipNode = outOrderLineElement.getAttribute("ShipNode");
		      
		      //String checkWebLinekey = checkWebLineNo+"|"+itemID+"|"+shipNode;
			if(getWeblinenoFromWeblinekey(weblinekey).equals(checkWebLineNo)){
				primeLineNo = outOrderLineElement.getAttribute("PrimeLineNo");
			}
		}
		
		Element chainedFromElement = createOrderDoc.createElement("ChainedFrom");
		chainedFromElement.setAttribute("DocumentType", "0001");
		chainedFromElement.setAttribute("OrganizationCode", "xpedx");
		chainedFromElement.setAttribute("OrderHeaderKey", customerOrderDetails.get(0));
		chainedFromElement.setAttribute("PrimeLineNo", primeLineNo);
		chainedFromElement.setAttribute("SubLineNo", "1");
		aElement.appendChild(chainedFromElement);
		htDirectOrder.put("Direct", createOrderDoc);
	}

	private void createHtDirectOrder(String weblinekey, YFSEnvironment env,
			String primeLineNo, ArrayList<String> lineTypeList,
			Document outputDoc) throws RemoteException {

		Element currentElement = htCurrentOrder.get(weblinekey);
		//String item = currentElement.getAttribute("ItemID");
		String item = SCXmlUtil.getXpathAttribute(currentElement, "./Item/@ItemID");
		
		//create an fulfillment order with order type "DIRECT"
		Document inputOrderDoc = YFCDocument.createDocument("Order").getDocument();
		Element inputOrderElement = inputOrderDoc.getDocumentElement();
		//inputOrderElement.setAttribute("OrderHeaderKey", fulfillmentOrderDetails.get(0));
		inputOrderElement.setAttribute("OrderHeaderKey", inputElement.getAttribute("OrderHeaderKey"));
		Document fulfillmentOrderList = api.invoke(env, "getOrderList", inputOrderDoc);
		NodeList orderList = fulfillmentOrderList.getElementsByTagName("Order");
		int oLength = orderList.getLength();
		if(oLength != 0)
		{
			Element orderElement = (Element)orderList.item(0);
			Document createOrderDoc = YFCDocument.createDocument("Order").getDocument();
			Element createOrderElement = createOrderDoc.getDocumentElement();
			createOrderElement.setAttribute("BillToID", orderElement.getAttribute("BillToID"));
			createOrderElement.setAttribute("BuyerOrganizationCode", orderElement.getAttribute("BuyerOrganizationCode"));
			createOrderElement.setAttribute("CustCustPONo", orderElement.getAttribute("CustCustPONo"));
			createOrderElement.setAttribute("CustomerPONo", orderElement.getAttribute("CustomerPONo"));
			createOrderElement.setAttribute("Division", orderElement.getAttribute("Division"));
			createOrderElement.setAttribute("DocumentType", orderElement.getAttribute("DocumentType"));
			//createOrderElement.setAttribute("DraftOrderFlag", orderElement.getAttribute("DraftOrderFlag"));
			createOrderElement.setAttribute("EnterpriseCode", orderElement.getAttribute("EnterpriseCode"));
			createOrderElement.setAttribute("EntryType", orderElement.getAttribute("EntryType"));
			createOrderElement.setAttribute("OrderDate", orderElement.getAttribute("OrderDate"));
			createOrderElement.setAttribute("SellerOrganizationCode", orderElement.getAttribute("SellerOrganizationCode"));
			createOrderElement.setAttribute("ShipToID", orderElement.getAttribute("ShipToID"));
			createOrderElement.setAttribute("ShipNode", orderElement.getAttribute("ShipNode"));
			createOrderElement.setAttribute("OrderType", lineTypeList.get(1));
			
			Element orderLinesElement = createOrderDoc.createElement("OrderLines");
			createOrderElement.appendChild(orderLinesElement);
			//get the newly added line
			Element addElement = htCurrentOrder.get(weblinekey);
			Node addNode = createOrderDoc.importNode(addElement, true);
			Element aElement = (Element)addNode;
			NodeList extnList = aElement.getElementsByTagName("Extn");
			int extnLength = extnList.getLength();
			if(extnLength != 0)
			{
				Element extnElement = (Element)extnList.item(0);				
				extnElement.setAttribute("ExtnLineType", lineTypeList.get(2));				
			}
			orderLinesElement.appendChild(aElement);
			
			//looping through the outputDoc and fetching the PrimeLineNo.
			Element outOrderElement = outputDoc.getDocumentElement();
			
			NodeList outOrderLinesElementNL = outOrderElement.getElementsByTagName("OrderLines");
			Element outOrderLinesElement = (Element)outOrderLinesElementNL.item(0);
			
			NodeList outOrderLineElementNL = outOrderLinesElement.getElementsByTagName("OrderLine");
			for(int i=0; i<outOrderLineElementNL.getLength(); i++){
				Element outOrderLineElement = (Element)outOrderLineElementNL.item(i);
				String checkWebLineNo = SCXmlUtil.getXpathAttribute(outOrderLineElement, "./Extn/@ExtnWebLineNumber");
			    //String itemID = SCXmlUtil.getXpathAttribute(outOrderLineElement, "./Item/@ItemID");
			    //String shipNode = outOrderLineElement.getAttribute("ShipNode");
			      
			      //String checkWebLinekey = checkWebLineNo+"|"+itemID+"|"+shipNode;
				if(getWeblinenoFromWeblinekey(weblinekey).equals(checkWebLineNo)){
					primeLineNo = outOrderLineElement.getAttribute("PrimeLineNo");
				}
			}
			
			Element chainedFromElement = createOrderDoc.createElement("ChainedFrom");
			chainedFromElement.setAttribute("DocumentType", "0001");
			chainedFromElement.setAttribute("OrganizationCode", "xpedx");
			chainedFromElement.setAttribute("OrderHeaderKey", customerOrderDetails.get(0));
			chainedFromElement.setAttribute("PrimeLineNo", primeLineNo);
			chainedFromElement.setAttribute("SubLineNo", "1");
			aElement.appendChild(chainedFromElement);
			
			//log.debug("createOrderDoc"+SCXmlUtil.getString(createOrderDoc));
			//create direct order
			//Document directOrderDoc = api.invoke(env, "createOrder", createOrderDoc);
			htDirectOrder.put("Direct", createOrderDoc);
		}
	}
	
	
	public void makeHeaderLevelChanges() throws Exception{
		//1. If there are any price changes, loop through all lines and calculate /Order/Extn/@ExtnTotalOrderValue 
		//For Fulfillment Order Looping through htLegacyOrder since it contains all lines and also LineProcessCode="A/D/C";
		Enumeration<String> Enumeration = htLegacyOrder.keys();
		while(Enumeration.hasMoreElements()){
			String weblineno = (String) Enumeration.nextElement();
			Element legacyLineElement = htLegacyOrder.get(weblineno);
			
			//Float lqty = Float.parseFloat( legacyLineElement.getAttribute("OrderedQty"));
			
			/***********Added by Prasanth Kumar M. for Bug #11348*********************/
			
			Float lqty =  Float.parseFloat( SCXmlUtil.getXpathAttribute(legacyLineElement,"./OrderLineTranQuantity/@OrderedQty"));
			
			/*************************************************************************/
			
			Float lprice = Float.parseFloat( SCXmlUtil.getXpathAttribute(legacyLineElement,"./LinePriceInfo/@UnitPrice"));
			String lpc = legacyLineElement.getAttribute("LineProcessCode");
			
			if("C".equals(lpc) || "A".equals(lpc)){
				newFOExtnTotalOrderValue += (lqty * lprice);
			}
		}
		
		//For Customer Order find diff b/w curr and new FO extnTotalOrderValue.
		//Apply difference on new CO extnTotalOrderValue
		newCOExtnTotalOrderValue = currCOExtnTotalOrderValue + (newFOExtnTotalOrderValue - currFOExtnTotalOrderValue);
		
		//2. Order Header Attributes have to be passed on the Legacy Order
		Element legacyOrderElement = sOrderToLegacyDoc.getDocumentElement();
		NodeList iOrderExtnNL = inputElement.getElementsByTagName("Extn");
		if(iOrderExtnNL.getLength()!=0){
			Element iOrderExtn = (Element)iOrderExtnNL.item(0);
			
			
			NodeList lOrderExtnNL = legacyOrderElement.getElementsByTagName("Extn");
			if(lOrderExtnNL.getLength()!=0){
				Element lOrderExtn = (Element)lOrderExtnNL.item(0);
				copyAttributes(iOrderExtn,lOrderExtn);
				
			}
		}
		
		//3. if Header Comment exist in input XML, modify it and Pass it to Legacy
		NodeList iOrderInssNL = inputElement.getElementsByTagName("Instructions");
		if(iOrderInssNL.getLength()!=0){
			Element iOrderInssEle = (Element)iOrderInssNL.item(0);
			NodeList iOrderInsNL = iOrderInssEle.getElementsByTagName("Instruction");
			if(iOrderInsNL.getLength()!=0){
				Element iOrderInsEle = (Element)iOrderInsNL.item(0);
				iOrderInsEle.setAttribute("InstructionType", "HEADER");
				iOrderInsEle.setAttribute("SequenceNo", "1");
				
				NodeList lOrderInssNL = legacyOrderElement.getElementsByTagName("Instructions");
				if(lOrderInssNL.getLength()!=0){
					Element lOrderInssEle = (Element)lOrderInssNL.item(0);
					NodeList lOrderInsNL = lOrderInssEle.getElementsByTagName("Instruction");
					if(lOrderInsNL.getLength()!=0){
						Element lOrderInsEle = (Element)lOrderInsNL.item(0);
						lOrderInsEle.setAttribute("InstructionText", iOrderInsEle.getAttribute("InstructionText"));
					}
					else {
						Element newLegacyInsEle =  (Element)sOrderToLegacyDoc.importNode(iOrderInsEle, true);
						legacyOrderElement.appendChild(newLegacyInsEle);
					}
				}
			}
		}
		
		
	}
	
	public void identifyPriceChanges(String weblineno){
		Element oLineElement = htCurrentOrder.get(weblineno);
		Element foLineElement = htFulfillmentOrder.get(weblineno);
		Element coLineElement = htCustomerOrder.get(getWeblinenoFromWeblinekey(weblineno));
		
		Element legacyLineElement = htLegacyOrder.get(weblineno);
		
		String oPriceString = SCXmlUtil.getXpathAttribute(oLineElement,"./LinePriceInfo/@UnitPrice");
		if(oPriceString==null || "".equals(oPriceString))
			return;
		Float oPrice = Float.parseFloat(oPriceString);
		Float foPrice = Float.parseFloat( SCXmlUtil.getXpathAttribute(foLineElement,"./LinePriceInfo/@UnitPrice"));
		Float coPrice = Float.parseFloat( SCXmlUtil.getXpathAttribute(coLineElement,"./LinePriceInfo/@UnitPrice"));
		Float currCustLineTotal = Float.parseFloat(SCXmlUtil.getXpathAttribute(coLineElement,"./LineOverallTotals/@ExtendedPrice"));
		


		Float oQty;
		//Float foQty = Float.parseFloat(foLineElement.getAttribute("OrderedQty"));
		//Float coQty = Float.parseFloat(coLineElement.getAttribute("OrderedQty"));
		//String oQtyString = oLineElement.getAttribute("OrderedQty");
		
		/***********Added by Prasanth Kumar M. for Bug #11348*********************/
		
		Float foQty =  Float.parseFloat( SCXmlUtil.getXpathAttribute(foLineElement,"./OrderLineTranQuantity/@OrderedQty"));
		Float coQty =  Float.parseFloat( SCXmlUtil.getXpathAttribute(coLineElement,"./OrderLineTranQuantity/@OrderedQty"));
		String oQtyString = SCXmlUtil.getXpathAttribute(oLineElement,"./OrderLineTranQuantity/@OrderedQty");
		/*************************************************************************/
		
		
		
		if(oQtyString==null || "".equals(oQtyString))
			oQty = foQty;
		else
			oQty = Float.parseFloat(oQtyString);
		Float newCOQty = coQty - foQty + oQty;
		
		
		Element chCustLineElement = htChangeCustomerOrder.get(getWeblinenoFromWeblinekey(weblineno));
		if(chCustLineElement==null)
			chCustLineElement= (Element)coLineElement.cloneNode(true);
		Element chFulLineElement = htChangeFulfillmentOrder.get(weblineno);
		if(chFulLineElement==null)
			chFulLineElement = (Element)oLineElement.cloneNode(true);
		
		if(oPrice.compareTo(foPrice)!=0){
			//1. Change price on fulfillment Order
			NodeList chFulLinePriceInfoNL = chFulLineElement.getElementsByTagName("LinePriceInfo");
			Element chFulLinePriceInfoElement = (Element)chFulLinePriceInfoNL.item(0);
			chFulLinePriceInfoElement.setAttribute("UnitPrice", oPrice.toString());		
			htChangeFulfillmentOrder.put(weblineno, chFulLineElement);
			
			//2. Change Price on Customer Order
			//2a. Find the average Price.

				/*
				 * linePriceDiff = (oQty*oPrice) - (foQty*foPrice)
				 * newCustTotal = oldCustTotal + linePriceDiff
				 * newCustUnitPrice = newCustTotal / newQty
				 */
			
			Float linePriceDiff = (oQty*oPrice) - (foPrice * foQty);
			Float newCustLineTotal = linePriceDiff + currCustLineTotal;
			Float newCustUnitPrice = newCustLineTotal / newCOQty;
			
			NodeList chCustLinePriceInfoNL = chCustLineElement.getElementsByTagName("LinePriceInfo");
			Element chCustLinePriceInfoElement = (Element)chCustLinePriceInfoNL.item(0);
			chCustLinePriceInfoElement.setAttribute("UnitPrice", newCustUnitPrice.toString());
			chCustLinePriceInfoElement.setAttribute("IsPriceLocked", "Y");
			htChangeCustomerOrder.put(getWeblinenoFromWeblinekey(weblineno), chCustLineElement);
			
    		//3. Set Legacy Element Attributes
    		legacyLineElement.setAttribute("LineProcessCode", "C");
			NodeList chLegacyLinePriceInfoNL = legacyLineElement.getElementsByTagName("LinePriceInfo");
			Element chLegacyLinePriceInfoElement = (Element)chLegacyLinePriceInfoNL.item(0);
			chLegacyLinePriceInfoElement.setAttribute("UnitPrice", oPrice.toString());    		
     		htLegacyOrder.put(weblineno, legacyLineElement);
     		
     		//4. Copy /Order/OrderLines/OrderLine/LineOverAllTotals/@ExtendedPrice to /OrderLine/Extn/@ExtnLineOrderedTotal
     		//Since the this OOB attribute is not calculated until we make a call to changeOrder, 
     		//manually calculating this value and setting the extended field ExtnLineOrderedTotal
     		// Passing on Customer Order and Fulfillment Order
     		
     		Float newFulLineTotal = oPrice * oQty;
			NodeList chFulLineExtnNL = chFulLineElement.getElementsByTagName("Extn");
			Element chFulLineExtnElement = (Element)chFulLineExtnNL.item(0);
			chFulLineExtnElement.setAttribute("ExtnLineOrderedTotal", newFulLineTotal.toString());
			htChangeFulfillmentOrder.put(weblineno, chFulLineElement);
			
     		NodeList chCustLineExtnNL = chCustLineElement.getElementsByTagName("Extn");
			Element chCustLineExtnElement = (Element)chCustLineExtnNL.item(0);
			chCustLineExtnElement.setAttribute("ExtnLineOrderedTotal", newCustLineTotal.toString());
			htChangeCustomerOrder.put(getWeblinenoFromWeblinekey(weblineno), chCustLineElement);
			
			NodeList legLineExtnNL = legacyLineElement.getElementsByTagName("Extn");
			Element legLineExtnElement = (Element)legLineExtnNL.item(0);
			legLineExtnElement.setAttribute("ExtnLineOrderedTotal", newFulLineTotal.toString());
			htLegacyOrder.put(weblineno, legacyLineElement);
     		
			
		}
		
		
	}
	
	public void identifyQtyChanges(String weblineno){
		Element oLineElement = htCurrentOrder.get(weblineno);
		Element foLineElement = htFulfillmentOrder.get(weblineno);
		Element coLineElement = htCustomerOrder.get(getWeblinenoFromWeblinekey(weblineno));
		
		Element legacyLineElement = htLegacyOrder.get(weblineno);
		
		//String oQtyString = oLineElement.getAttribute("OrderedQty");
		
        /***********Added by Prasanth Kumar M. for Bug #11348*********************/
		
		String oQtyString = SCXmlUtil.getXpathAttribute(oLineElement,"./OrderLineTranQuantity/@OrderedQty");
		/*************************************************************************/
		
		if(oQtyString==null || "".equals(oQtyString))
			return;
		Float oQty = Float.parseFloat(oQtyString);
		//Float foQty = Float.parseFloat(foLineElement.getAttribute("OrderedQty"));
		//Float coQty = Float.parseFloat(coLineElement.getAttribute("OrderedQty"));
		
        /***********Added by Prasanth Kumar M. for Bug #11348*********************/
		
		Float foQty =  Float.parseFloat( SCXmlUtil.getXpathAttribute(foLineElement,"./OrderLineTranQuantity/@OrderedQty"));
		Float coQty =  Float.parseFloat( SCXmlUtil.getXpathAttribute(coLineElement,"./OrderLineTranQuantity/@OrderedQty"));
		/*************************************************************************/
		
		Element chCustLineElement = (Element)coLineElement.cloneNode(true);
		Element chFulLineElement = (Element)oLineElement.cloneNode(true);
		Element chCustOrderStatusLineElement = inputOrderStatusChangeDoc.createElement("OrderLine");
		
		//1. Check if qty=0
    	if(oQty==0.0){
    		
    		//2. check if fo.qty == co.qty
    		if(foQty.compareTo(coQty)==0){
    			// Line has to be deleted from customer Order
    			//chCustLineElement.setAttribute("OrderedQty", "0");
    			
    			/***********Added by Prasanth Kumar M. for Bug #11348*********************/
    		    Element chCustOrderLineTranQtyElement = (Element)chCustLineElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
    		    chCustOrderLineTranQtyElement.setAttribute("OrderedQty", "0");
    		    /*************************************************************************/
    		    
    			htChangeCustomerOrder.put(getWeblinenoFromWeblinekey(weblineno), chCustLineElement);
    			
    			//b. Customer Order Line status will move to Unscheduled, this has to be moved to "Created".
//    			chCustOrderStatusLineElement.setAttribute("BaseDropStatus", "1100");
//    			chCustOrderStatusLineElement.setAttribute("PrimeLineNo",coLineElement.getAttribute("PrimeLineNo"));
//    			chCustOrderStatusLineElement.setAttribute("SubLineNo",coLineElement.getAttribute("SubLineNo"));
//    			chCustOrderStatusLineElement.setAttribute("Quantity", foQty.toString());
//    			htChangeStatusOnCustomerOrder.put(getWeblinenoFromWeblinekey(weblineno),chCustOrderStatusLineElement);
    			
    		}else{
    			//a. Line has to be deleted from fulfillment Order
    			//chFulLineElement.setAttribute("OrderedQty", "0");
    			
    			/***********Added by Prasanth Kumar M. for Bug #11348*********************/
    		    Element chFulOrderLineTranQtyElement = (Element)chFulLineElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
    		    chFulOrderLineTranQtyElement.setAttribute("OrderedQty", "0");
    		    /*************************************************************************/
    			
    			htChangeFulfillmentOrder.put(weblineno, chFulLineElement);
    			
    			//b. Customer Order Line status will move to Unscheduled, this has to be moved to "Created".
    			chCustOrderStatusLineElement.setAttribute("BaseDropStatus", "1100.0100");
    			chCustOrderStatusLineElement.setAttribute("PrimeLineNo",coLineElement.getAttribute("PrimeLineNo"));
    			chCustOrderStatusLineElement.setAttribute("SubLineNo",coLineElement.getAttribute("SubLineNo"));
    			chCustOrderStatusLineElement.setAttribute("Quantity", foQty.toString());
    			htChangeStatusOnCustomerOrder.put(getWeblinenoFromWeblinekey(weblineno),chCustOrderStatusLineElement);
    			
    			//c. Line on customer order has to be reduced by foQty
    			Float nCOQty = coQty - foQty;
    			//chCustLineElement.setAttribute("OrderedQty", nCOQty.toString());
    			
    			/***********Added by Prasanth Kumar M. for Bug #11348*********************/
    		    Element chCustOrderLineTranQtyElement = (Element)chCustLineElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
    		    chCustOrderLineTranQtyElement.setAttribute("OrderedQty", nCOQty.toString());
    		    /*************************************************************************/
    			htChangeCustomerOrder.put(getWeblinenoFromWeblinekey(weblineno), chCustLineElement);
    			
    		}
    		// Set Legacy Element Attributes
    		//legacyLineElement.setAttribute("OrderedQty", "0");
    		
    		/***********Added by Prasanth Kumar M. for Bug #11348*********************/
    		 Element legacyOrderLineTranQtyElement = (Element)legacyLineElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
    		 legacyOrderLineTranQtyElement.setAttribute("OrderedQty",  "0");
    		/*************************************************************************/
    		
    		legacyLineElement.setAttribute("LineProcessCode", "D");
    		htLegacyOrder.put(weblineno, legacyLineElement);
    		
    	//3. Check if there is any Qty Decrease
    	}else if(oQty.compareTo(foQty)<0){ //qty decrease
    		Float diffQty = foQty - oQty;
    		
			//3a. Line has to be decreased from fulfillment Order
			//chFulLineElement.setAttribute("OrderedQty", oQty.toString());
    		
    		/***********Added by Prasanth Kumar M. for Bug #11348*********************/
   		 Element chFulOrderLineTranQtyElement = (Element)chFulLineElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
   		chFulOrderLineTranQtyElement.setAttribute("OrderedQty", oQty.toString());
   		/*************************************************************************/
    		
			htChangeFulfillmentOrder.put(weblineno, chFulLineElement);
			
			//3b. Customer Order Line status will move to Unscheduled, this has to be moved to "Created".
			chCustOrderStatusLineElement.setAttribute("BaseDropStatus", "1100.0100");
			chCustOrderStatusLineElement.setAttribute("PrimeLineNo",coLineElement.getAttribute("PrimeLineNo"));
			chCustOrderStatusLineElement.setAttribute("SubLineNo",coLineElement.getAttribute("SubLineNo"));
			chCustOrderStatusLineElement.setAttribute("Quantity", diffQty.toString());
			htChangeStatusOnCustomerOrder.put(getWeblinenoFromWeblinekey(weblineno),chCustOrderStatusLineElement);
			
			//3c. Line on customer order has to be reduced by diffQty
			Float nCOQty = coQty - diffQty;
			//chCustLineElement.setAttribute("OrderedQty", nCOQty.toString());
			
			/***********Added by Prasanth Kumar M. for Bug #11348*********************/
	   		 Element chCustOrderLineTranQtyElement = (Element)chCustLineElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
	   		chCustOrderLineTranQtyElement.setAttribute("OrderedQty", nCOQty.toString());
	   		/*************************************************************************/
			htChangeCustomerOrder.put(getWeblinenoFromWeblinekey(weblineno), chCustLineElement);
			
			//3d. Set Legacy Element Attributes
    		//legacyLineElement.setAttribute("OrderedQty", oQty.toString());
			
			/***********Added by Prasanth Kumar M. for Bug #11348*********************/
	   		 Element legacyOrderLineTranQtyElement = (Element)legacyLineElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
	   		legacyOrderLineTranQtyElement.setAttribute("OrderedQty", oQty.toString());
	   		/*************************************************************************/
    		legacyLineElement.setAttribute("LineProcessCode", "C");
    		htLegacyOrder.put(weblineno, legacyLineElement);
    		
    		// To form a map to identify qty decreased for a line.
    		cancelledQtyWebLineNoList.add(weblineno);
    		
    	//4. Check if there is any Qty Increase	
    	}else if(oQty.compareTo(foQty)>0){ //qty increase
    		Float diffQty = oQty - foQty;
    		
			//4a. Line has to be increased from fulfillment Order
			//chFulLineElement.setAttribute("OrderedQty", oQty.toString());
    		
    		/***********Added by Prasanth Kumar M. for Bug #11348*********************/
	   		 Element chFulLineTranQtyElement = (Element)chFulLineElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
	   		chFulLineTranQtyElement.setAttribute("OrderedQty", oQty.toString());
	   		/*************************************************************************/
			htChangeFulfillmentOrder.put(weblineno, chFulLineElement);
			
			//4b. Customer Order Line status will move to Unscheduled, this has to be moved to "Created".
			chCustOrderStatusLineElement.setAttribute("BaseDropStatus", "1100.0100");
			chCustOrderStatusLineElement.setAttribute("PrimeLineNo",coLineElement.getAttribute("PrimeLineNo"));
			chCustOrderStatusLineElement.setAttribute("SubLineNo",coLineElement.getAttribute("SubLineNo"));
			chCustOrderStatusLineElement.setAttribute("Quantity", diffQty.toString());
			htChangeStatusOnCustomerOrder.put(getWeblinenoFromWeblinekey(weblineno),chCustOrderStatusLineElement);
			
			//4c. Line on customer order has to be reduced by diffQty - NOT REQUIRED FOR QTY INCREASE
			Float nCOQty = coQty + diffQty;
			//chCustLineElement.setAttribute("OrderedQty", nCOQty.toString());
			
			/***********Added by Prasanth Kumar M. for Bug #11348*********************/
	   		 Element chCustLineTranQtyElement = (Element)chCustLineElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
	   		chCustLineTranQtyElement.setAttribute("OrderedQty",nCOQty.toString());
	   		/*************************************************************************/
			htChangeCustomerOrder.put(getWeblinenoFromWeblinekey(weblineno), chCustLineElement);
			
			//4d. Set Legacy Element Attributes
    		//legacyLineElement.setAttribute("OrderedQty", oQty.toString());
			
			/***********Added by Prasanth Kumar M. for Bug #11348*********************/
	   		 Element legacyLineTranQtyElement = (Element)legacyLineElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
	   		legacyLineTranQtyElement.setAttribute("OrderedQty",oQty.toString());
	   		/*************************************************************************/
			
    		legacyLineElement.setAttribute("LineProcessCode", "C");
    		htLegacyOrder.put(weblineno, legacyLineElement);
    		
    	}
    	//5. Check if all lines are deleted. if true send HeaderProcessCode as D to Legacy
    	boolean hpcChangeFlag = false;
    	Enumeration<String> Enumeration = htLegacyOrder.keys (); 
	    while (Enumeration.hasMoreElements ()) { 
	        String webLineNo = (String) Enumeration.nextElement (); 
        
	        Element OrderLineElement = htLegacyOrder.get(webLineNo);
	        String lpc = OrderLineElement.getAttribute("LineProcessCode");
	        if("C".equalsIgnoreCase(lpc) || "A".equalsIgnoreCase(lpc)){
	        		hpcChangeFlag = true;
	        		break;
	        }
	    }
	    if(!hpcChangeFlag){
	    	Element inputLegacyOrderElement  = sOrderToLegacyDoc.getDocumentElement();
	    	inputLegacyOrderElement.setAttribute("HeaderProcessCode", "D");
	    }
    	
		
		
	}
	
	public void sendUpdatedOrderToLegacy(YFSEnvironment env, Document businessRuleOuputDoc,
			Document changeOrderFODoc,Document directOrderDoc) throws Exception {
			
		buildSterlingOrderToLegacy(businessRuleOuputDoc);
			
		if(directOrderDoc == null){
			// Fulfillment order output needs to be sent to legacy.
			mergeLegacyAttributes(env,sOrderToLegacyDoc,changeOrderFODoc);
			sOrderToLegacyDoc = changeOrderFODoc;
		} else {
			// Direct order created output needs to be sent to legacy.
			mergeLegacyAttributes(env,sOrderToLegacyDoc,directOrderDoc);
			sOrderToLegacyDoc = directOrderDoc;
		}
		if(sOrderToLegacyDoc != null){
		log.debug("sOrderToLegacyDoc input to Legacy: "+SCXmlUtil.getString(sOrderToLegacyDoc));
		}
		Document legacyFormatOrderDoc = api.executeFlow(env, "XPXConvertToLegacyOrder", sOrderToLegacyDoc);
		if(log.isDebugEnabled()){
		log.debug("Converted Legacy Order" + SCXmlUtil.getString(legacyFormatOrderDoc));
		}
	}
	
	
	public void buildSterlingOrderToLegacy(Document businessRuleOuputDoc){

		//1. Order Header is already in place for sOrderToLegacyDoc, including the lines from htLegacyOrder hashtable
		String orderHoldFlag = "";
		String orderLineQtyStr = "";
		
		Element inputOrderElement  = sOrderToLegacyDoc.getDocumentElement();
		
		if(businessRuleOuputDoc != null && businessRuleOuputDoc.getDocumentElement().hasAttribute("OrderHoldFlag")){
		orderHoldFlag = businessRuleOuputDoc.getDocumentElement().getAttribute("OrderHoldFlag");
		}
		if(!YFCObject.isNull(orderHoldFlag) && orderHoldFlag.equalsIgnoreCase("Y")){
			// To add ExtnWebHoldFlag to 'Y'
			NodeList extnList = inputOrderElement.getElementsByTagName("Extn");
			if(extnList.getLength()>0){
				Element extnElement = (Element) extnList.item(0);
				extnElement.setAttribute("ExtnWebHoldFlag", "Y");
				extnElement.setAttribute("ExtnWebHoldReason", XPXCustomerProfileRuleConstant.NEEDS_ATTENTION_HOLD_DESC);	
			}			
		}
		
		NodeList chLegacyOverallTotalsNL = inputOrderElement.getElementsByTagName("OverallTotals");
		Element chOverallTotalsElement = (Element)chLegacyOverallTotalsNL.item(0);
		inputOrderElement.removeChild(chOverallTotalsElement);
		
		NodeList orderExtnNL = inputOrderElement.getElementsByTagName("Extn");
		if(orderExtnNL.getLength()!=0){
			Element orderExtn = (Element)orderExtnNL.item(0);
			orderExtn.setAttribute("ExtnTotalOrderValue", newFOExtnTotalOrderValue.toString());
		}
		
		Element OrderLinesElement = sOrderToLegacyDoc.createElement("OrderLines");
		
		
		inputOrderElement.appendChild(OrderLinesElement);
		
		Enumeration<String> Enumeration = htLegacyOrder.keys (); 
	    while (Enumeration.hasMoreElements ()) { 
	        String webLineNo = (String) Enumeration.nextElement (); 
        
	        Element OrderLineElement = htLegacyOrder.get(webLineNo);  
	        
			NodeList chLegacyLineOverallTotalsNL = OrderLineElement.getElementsByTagName("LineOverallTotals");
			if(chLegacyLineOverallTotalsNL.getLength()>0){
				Element chLineOverallTotalsElement = (Element)chLegacyLineOverallTotalsNL.item(0);
				OrderLineElement.removeChild(chLineOverallTotalsElement);
			}

       
	        Node nOrderLineElement = sOrderToLegacyDoc.importNode(OrderLineElement, true);
	        OrderLinesElement.appendChild(nOrderLineElement);
	    }
	    if(log.isDebugEnabled()){
	    log.debug("Sterling to Order Input XML" + SCXmlUtil.getString(sOrderToLegacyDoc));
		 }
	}

	public void callChangeOrderStatusOnCustomerOrder(YFSEnvironment env) throws Exception{
		Document inputOrderDoc = buildChangeOrderStatusXML();
		if(log.isDebugEnabled()){
		log.debug("changeOrderStatus of XPXUpdateChainedOrderAPI is :"+SCXmlUtil.getString(inputOrderDoc));
		}
		Document outputOrderDoc = api.invoke(env, "changeOrderStatus", inputOrderDoc);
	}
	
	public Document buildChangeOrderStatusXML(){
		
		Element inputOrderStatusChangeElement = inputOrderStatusChangeDoc.getDocumentElement();
		inputOrderStatusChangeElement.setAttribute("DocumentType", "0001");
		inputOrderStatusChangeElement.setAttribute("EnterpriseCode", "xpedx");
		inputOrderStatusChangeElement.setAttribute("IgnoreTransactionDependencies", "Y");
		inputOrderStatusChangeElement.setAttribute("ModificationReasonCode", "CHANGE_FULFILLMENT_TYPE");
		inputOrderStatusChangeElement.setAttribute("ModificationReasonText", "Changed Fulfillment Type");
		inputOrderStatusChangeElement.setAttribute("OrderHeaderKey", customerOrderDetails.get(0));
		inputOrderStatusChangeElement.setAttribute("TransactionId", "XPX_CHN_CRT_STATUS_TXN.0001.ex");
		//inputOrderStatusChangeElement.setAttribute("TransactionId", "xpedxUPDATE_ORD_QTY.0001.ex");
		Element orderLinesElement = inputOrderStatusChangeDoc.createElement("OrderLines");
		inputOrderStatusChangeElement.appendChild(orderLinesElement);
		
		Enumeration<String> Enumeration = htChangeStatusOnCustomerOrder.keys (); 
		while (Enumeration.hasMoreElements ()) { 
	        String webLineNo = (String) Enumeration.nextElement (); 
        
	        Element OrderLineElement = htChangeStatusOnCustomerOrder.get(webLineNo);

            if(!YFCObject.isNull(custOrdStatusList) && custOrdStatusList.size() > 0 && custOrdStatusList.contains(webLineNo)){
	        Node nOrderLineElement = inputOrderStatusChangeDoc.importNode(OrderLineElement, true);
	        orderLinesElement.appendChild(nOrderLineElement);
            }
	    }
		if(log.isDebugEnabled()){
		log.debug("changeOrderStatus on Customer Order Input XML" + SCXmlUtil.getString(inputOrderStatusChangeDoc));
		}
		return inputOrderStatusChangeDoc;
	}
	
	public void callChangeCustomerOrder(YFSEnvironment env, Element inputXML) throws Exception{
		
		String status = "";
		String statusQty = "";
		Document inputOrderDoc = buildChangeCustomerOrderInputXML(inputXML);
		if(log.isDebugEnabled()){
		log.debug("inputOrderDoc of changeOrder of XPXUpdateChainedOrderAPI is : "+SCXmlUtil.getString(inputOrderDoc));
		}
		Document outputOrderDoc = api.invoke(env, "changeOrder", inputOrderDoc);
		if(outputOrderDoc != null){
		log.debug("ChangeOrder_CO_OutputDoc:"+SCXmlUtil.getString(outputOrderDoc));
		}
		env.clearApiTemplate("changeOrder");
		
		/***** Start - changes made for issue 1910 ****/
		
		// To form a list which holds web line numbers of status '2100' - Chained order created status.
		if(outputOrderDoc != null){
		Element orderElem = outputOrderDoc.getDocumentElement();
		NodeList orderLineList = orderElem.getElementsByTagName("OrderLine");
		int orderLinesize = orderLineList.getLength();
		for(int lineCount = 0 ; lineCount < orderLinesize; lineCount++){
			Element orderLineElem = (Element) orderLineList.item(lineCount);
			NodeList orderStatuslist = orderLineElem.getElementsByTagName("OrderStatus");
			int orderStatusLength = orderStatuslist.getLength();
			for(int statusCount=0;statusCount < orderStatusLength;statusCount++){
				Element statusElement = (Element) orderStatuslist.item(statusCount);
				status = statusElement.getAttribute("Status");
				statusQty = statusElement.getAttribute("StatusQty");  
				if(status.equals("2100") && !statusQty.equals("0")){
					Element extnElement = (Element) orderLineElem.getElementsByTagName("Extn").item(0);
					String webLineNum = extnElement.getAttribute("ExtnWebLineNumber");
					if(!YFCObject.isNull(webLineNum)){
						custOrdStatusList.add(webLineNum);
					}
					break;
				}
			}	
		}	
		}	
		/***** End - changes made for issue 1910 ****/	
	}
	
	

	public Document callChangeFulfillmentOrder(YFSEnvironment env, Element inputXML) throws Exception{
		Document inputOrderDoc = buildChangeFulfillmentOrderInputXML(inputXML);	
		if(inputOrderDoc != null){
		log.debug("callChangeFulfillmentOrder_inputOrderDoc:"+SCXmlUtil.getString(inputOrderDoc));
		}
		env.setApiTemplate("changeOrder",changeOrderTemplate);
		Document outputOrderDoc = api.invoke(env, "changeOrder", inputOrderDoc);
		if(outputOrderDoc != null){
		log.debug("ChangeOrder_FO_OutputDoc:"+SCXmlUtil.getString(outputOrderDoc));
		}
	return outputOrderDoc;	
	}
	
		
	public Document buildChangeCustomerOrderInputXML(Element inputXML){
		/*Document inputOrderDoc = YFCDocument.createDocument().getDocument();
		inputOrderDoc.appendChild(inputOrderDoc.importNode(inputXML, true));
		inputOrderDoc.renameNode(inputOrderDoc.getDocumentElement(), inputOrderDoc.getNamespaceURI(), "Order");
		
		
		Element inputOrderElement  = inputOrderDoc.getDocumentElement();
		
		NodeList rOrderLinesNodeList = inputOrderElement.getElementsByTagName("OrderLines");
		int orderLinesLength = rOrderLinesNodeList.getLength();
		if(orderLinesLength != 0){
			Element rOrderLinesElement = (Element)rOrderLinesNodeList.item(0);
			inputOrderElement.removeChild(rOrderLinesElement);
		}*/
		
		Document inputCustOrderDoc = YFCDocument.createDocument("Order").getDocument();
		Element custOrderElement = inputCustOrderDoc.getDocumentElement();
		custOrderElement.setAttribute("OrderHeaderKey", customerOrderDetails.get(0));
		custOrderElement.setAttribute("OrganizationCode","xpedx");
		custOrderElement.setAttribute("DocumentType", "0001");
		
		Element orderExtn = inputCustOrderDoc.createElement("Extn");
		orderExtn.setAttribute("ExtnTotalOrderValue", newCOExtnTotalOrderValue.toString());
		custOrderElement.appendChild(orderExtn);
		
		
			
		Element OrderLinesElement = inputCustOrderDoc.createElement("OrderLines");
		custOrderElement.appendChild(OrderLinesElement);
		
		Enumeration<String> Enumeration = htChangeCustomerOrder.keys (); 
	    while (Enumeration.hasMoreElements ()) { 
	        String webLineNo = (String) Enumeration.nextElement (); 
        
	        Element OrderLineElement = htChangeCustomerOrder.get(webLineNo);
	        
			NodeList chCustLineOverallTotalsNL = OrderLineElement.getElementsByTagName("LineOverallTotals");
			if(chCustLineOverallTotalsNL.getLength()>0){
				Element chLineOverallTotalsElement = (Element)chCustLineOverallTotalsNL.item(0);
				OrderLineElement.removeChild(chLineOverallTotalsElement);
			}
	
			
	        Node nOrderLineElement = inputCustOrderDoc.importNode(OrderLineElement, true);
	        
	        OrderLinesElement.appendChild(nOrderLineElement);
	    }
	    if(log.isDebugEnabled()){
	    log.debug("Change Customer Order Input XML" + SCXmlUtil.getString(inputCustOrderDoc));
	    }
	    return inputCustOrderDoc;
	}
	
	public Document buildChangeFulfillmentOrderInputXML(Element inputXML){
		
		Document inputOrderDoc = YFCDocument.createDocument().getDocument();
		inputOrderDoc.appendChild(inputOrderDoc.importNode(inputXML, true));
		inputOrderDoc.renameNode(inputOrderDoc.getDocumentElement(), inputOrderDoc.getNamespaceURI(), "Order");
		
		
		Element inputOrderElement  = inputOrderDoc.getDocumentElement();
		
		NodeList orderExtnNL = inputOrderElement.getElementsByTagName("Extn");
		if(orderExtnNL.getLength()!=0){
			Element orderExtn = (Element)orderExtnNL.item(0);
			orderExtn.setAttribute("ExtnTotalOrderValue", newFOExtnTotalOrderValue.toString());
		}
		
		NodeList rOrderLinesNodeList = inputOrderElement.getElementsByTagName("OrderLines");
		int orderLinesLength = rOrderLinesNodeList.getLength();
		if(orderLinesLength != 0){
			Element rOrderLinesElement = (Element)rOrderLinesNodeList.item(0);
			inputOrderElement.removeChild(rOrderLinesElement);
		}
			
		Element OrderLinesElement = inputOrderDoc.createElement("OrderLines");
		inputOrderElement.appendChild(OrderLinesElement);
		
		Enumeration<String> Enumeration = htChangeFulfillmentOrder.keys (); 
	    while (Enumeration.hasMoreElements ()) { 
	        String webLineNo = (String) Enumeration.nextElement (); 
        
	        Element OrderLineElement = htChangeFulfillmentOrder.get(webLineNo);
	        
			NodeList chFulLineOverallTotalsNL = OrderLineElement.getElementsByTagName("LineOverallTotals");
			if(chFulLineOverallTotalsNL.getLength()>0){
				Element chLineOverallTotalsElement = (Element)chFulLineOverallTotalsNL.item(0);
				OrderLineElement.removeChild(chLineOverallTotalsElement);
			}
			
			// To remove 'OrderLineTranQuantity' element in case of Qty decrease as Qty will be adjusted changing the CO order.
			if(!YFCObject.isNull(cancelledQtyWebLineNoList) && cancelledQtyWebLineNoList.size() > 0){
				if(cancelledQtyWebLineNoList.contains(webLineNo)){
				Element oLTranQuantityElem = (Element)OrderLineElement.getElementsByTagName("OrderLineTranQuantity").item(0);
				OrderLineElement.removeChild(oLTranQuantityElem);
				}
			}	

	        Node nOrderLineElement = inputOrderDoc.importNode(OrderLineElement, true);
	        
	        OrderLinesElement.appendChild(nOrderLineElement);
	    }
	    
	    Document pendingChangesDoc = YFCDocument.createDocument("PendingChanges").getDocument();
	    Element pendingChangesEle = pendingChangesDoc.getDocumentElement();
	    pendingChangesEle.setAttribute("ApplyPendingChanges", "Y");
	    
	    Element pChangedElement = (Element) inputOrderDoc.importNode(pendingChangesEle, true);
	    inputOrderElement.appendChild(pChangedElement);
	    if(log.isDebugEnabled()){
	    log.debug("Change Fulfillment Order Input XML" + SCXmlUtil.getString(inputOrderDoc));
	    }


	    return inputOrderDoc;
		
	}
	
	public void getFulfillmentOrderDetails(YFSEnvironment env, Element inputElement) throws Exception{
		
		//get the fulfillmentOrder using the OrderHeaderKey
		String oHKey = inputElement.getAttribute("OrderHeaderKey");
		YFCDocument inputOrderDoc = YFCDocument.createDocument("Order");
		YFCElement inputOrderElement = inputOrderDoc.getDocumentElement();
		inputOrderElement.setAttribute("OrderHeaderKey", oHKey);
		if(log.isDebugEnabled()){
		log.debug("getOrderList of XPXUpdateChainedOrderAPI is : "+SCXmlUtil.getString(inputOrderDoc.getDocument()));
		}
		Document templateDoc = setTemplate(env);
		env.setApiTemplate("getOrderList", templateDoc);
		// invoke the orderList
		Document outOrderListDoc = api.invoke(env, "getOrderList", inputOrderDoc.getDocument());
		env.clearApiTemplate("getOrderList");
		if(log.isDebugEnabled()){
		log.debug("outOrderListDoc of getOrderList in XPXUpdateChainedOrderAPI is :"+SCXmlUtil.getString(outOrderListDoc));
		}
		Element outOrderListElement = outOrderListDoc.getDocumentElement();
		String totalNumber = outOrderListElement.getAttribute("TotalOrderList");
		if(!totalNumber.equals("0"))
		{
			NodeList orderNode = outOrderListDoc.getElementsByTagName("Order");
			Element orderElement = (Element)orderNode.item(0);
			
			setOrderHeaderLevelAttributes(inputElement, orderElement);
			
			NodeList overallTotalsNL = orderElement.getElementsByTagName("OverallTotals");
			if(overallTotalsNL.getLength()!=0){
				Element overallTotalsElement = (Element)overallTotalsNL.item(0);
				currFOExtnTotalOrderValue = Float.parseFloat(overallTotalsElement.getAttribute("LineSubTotal"));
			}
			
			
			NodeList orderLinesNodeList = orderElement.getElementsByTagName("OrderLines");
			int orderLinesLength = orderLinesNodeList.getLength();
			if(orderLinesLength != 0)
			{
				Element orderLinesElement = (Element)orderLinesNodeList.item(0);
				NodeList orderLineNodeList = orderLinesElement.getElementsByTagName("OrderLine");
				int orderLineLength = orderLineNodeList.getLength();
				if(orderLineLength != 0)
				{
					Element orderLineElement = (Element)orderLineNodeList.item(0);
					/******************Populate the Hashtable *********************/
					for(int oCounter = 0;oCounter<orderLineLength;oCounter++)
					{
						Element oElement = (Element)orderLineNodeList.item(oCounter);
					
						/*String webLineNo = SCXmlUtil.getXpathAttribute(oElement, "./Extn/@ExtnWebLineNumber");
					    if(webLineNo==""){
								long uniqueSequenceNo = CallDBSequence.getNextDBSequenceNo(env, XPXLiterals.WEB_LINE_SEQUENCE);
								webLineNo = XPXAddParametersAPI.generateWebLineNumber(uniqueSequenceNo);
					    }*/
					    String weblinekey = getWebLineKey(env, oElement);
					    if(log.isDebugEnabled()){
					    log.debug("webLineKey in XPXUpdateChainedOrderAPI is : "+ weblinekey);
					    }
						htFulfillmentOrder.put(weblinekey, oElement);
						Element legacyElement = (Element)oElement.cloneNode(true);
						legacyElement.setAttribute("LineProcessCode", "C");
						htLegacyOrder.put(weblinekey, legacyElement);
					}
				}
			}
			
			//build the Sterling Order to Legacy Header & set HeaderProcessCode = "C"
			NodeList sOrderNodeList = outOrderListElement.getElementsByTagName("Order");
			Element sOrderNode = (Element)sOrderNodeList.item(0);
			sOrderToLegacyDoc = YFCDocument.createDocument().getDocument();
			sOrderToLegacyDoc.appendChild(sOrderToLegacyDoc.importNode(sOrderNode, true));
			sOrderToLegacyDoc.renameNode(sOrderToLegacyDoc.getDocumentElement(), sOrderToLegacyDoc.getNamespaceURI(), "Order");
			
			
			Element sOrderToLegacyOrderElement  = sOrderToLegacyDoc.getDocumentElement();
			NodeList rOrderLinesNodeList = sOrderToLegacyOrderElement.getElementsByTagName("OrderLines");
			int rOrderLinesLength = rOrderLinesNodeList.getLength();
			if(rOrderLinesLength != 0){
				Element rOrderLinesElement = (Element)rOrderLinesNodeList.item(0);
				sOrderToLegacyOrderElement.removeChild(rOrderLinesElement);
			}
	
			sOrderToLegacyOrderElement.setAttribute("HeaderProcessCode", "C");
			if(log.isDebugEnabled()){
			log.debug("Sterling Order to Legacy Header " + SCXmlUtil.getString(sOrderToLegacyDoc));
			}
		
		}
		else
		{
			if(log.isDebugEnabled()){
			log.debug("fulfillment order does not exist");
			}
		}

	}
	
	public void getCustomerOrderDetails(YFSEnvironment env, Element inputElement) throws Exception{
		String customerOrderNo = "";
		String customerOrderHK = "";
		
		NodeList extnNode = inputElement.getElementsByTagName("Extn");
		Element extnInputElement = (Element)extnNode.item(0);
		String webConfNum = extnInputElement.getAttribute("ExtnWebConfNum");
		
		YFCDocument inputOrderDoc = YFCDocument.createDocument("Order");
		YFCElement inputOrderElement = inputOrderDoc.getDocumentElement();
		YFCElement extnElement = inputOrderDoc.createElement("Extn");
		extnElement.setAttribute("ExtnWebConfNum", webConfNum);
		inputOrderElement.appendChild(extnElement);
		if(log.isDebugEnabled()){
		log.debug("inputCustomerOrderDoc of getOrderList in XPXUpdateChainedOrderAPI is"+SCXmlUtil.getString(inputOrderDoc.getDocument()));
		}
		Document templateDoc = setTemplate(env);
		env.setApiTemplate("getOrderList", templateDoc);
		Document outputCustomerOrderDoc = api.invoke(env, "getOrderList", inputOrderDoc.getDocument());
		env.clearApiTemplate("getOrderList");
		if(log.isDebugEnabled()){
		log.debug("outputCustomerOrderDoc of getOrderList in XPXUpdateChainedOrderAPI is "+SCXmlUtil.getString(outputCustomerOrderDoc));
		}
		NodeList orderNode = outputCustomerOrderDoc.getElementsByTagName("Order");
		Element orderElement = (Element)orderNode.item(0);
		customerOrderNo = orderElement.getAttribute("OrderNo");
		customerOrderHK = orderElement.getAttribute("OrderHeaderKey");

		if(!YFCObject.isNull(customerOrderNo) && !YFCObject.isVoid(customerOrderNo)) {
		log.debug("customerOrderNo"+customerOrderNo);
		}

		if(!YFCObject.isNull(customerOrderHK) && !YFCObject.isVoid(customerOrderHK)) {
		log.debug("customerOrderHeaderKey"+customerOrderHK);
		}
		/* Start -  Changes made to fix issue 926 */
		entryType = orderElement.getAttribute(XPXLiterals.A_ENTRY_TYPE);
		NodeList extnNodeCustomerDoc = orderElement.getElementsByTagName("Extn");
		Element extnEle = (Element)extnNodeCustomerDoc.item(0);
		envtCode = extnEle.getAttribute("ExtnEnvtId");	
	    /* End -  Changes made to fix issue 926 */
	    
		customerOrderDetails.add(customerOrderHK);
		customerOrderDetails.add(customerOrderNo);
		
		NodeList overallTotalsNL = orderElement.getElementsByTagName("OverallTotals");
		if(overallTotalsNL.getLength()!=0){
			Element overallTotalsElement = (Element)overallTotalsNL.item(0);
			currCOExtnTotalOrderValue = Float.parseFloat(overallTotalsElement.getAttribute("LineSubTotal"));
		}
			
		NodeList orderLinesNodeList = orderElement.getElementsByTagName("OrderLines");
		int orderLinesLength = orderLinesNodeList.getLength();
		if(orderLinesLength != 0)
		{
			Element orderLinesElement = (Element)orderLinesNodeList.item(0);
			NodeList orderLineNodeList = orderLinesElement.getElementsByTagName("OrderLine");
			int orderLineLength = orderLineNodeList.getLength();
			if(orderLineLength != 0)
			{
				/******************Populate the Hashtable *********************/
				for(int oCounter = 0;oCounter<orderLineLength;oCounter++)
				{
					Element oElement = (Element)orderLineNodeList.item(oCounter);
					String webLineNo = SCXmlUtil.getXpathAttribute(oElement, "./Extn/@ExtnWebLineNumber");
					/*
				    if(webLineNo==""){
						long uniqueSequenceNo = CallDBSequence.getNextDBSequenceNo(env, XPXLiterals.WEB_LINE_SEQUENCE);
						webLineNo = XPXAddParametersAPI.generateWebLineNumber(uniqueSequenceNo);
			    	}*/
			    	//String weblinekey = getWebLineKey(env, oElement);
			    	
					htCustomerOrder.put(webLineNo, oElement);
				}
			}
		}
	}
	
	//changed this method
	
	public void populateCurrentOrderHashtable(YFSEnvironment env, Element inputElement) throws Exception{
		
		Document parentDoc = inputElement.getOwnerDocument();	
		
		NodeList orderNode = inputElement.getElementsByTagName("OrderLines");
		int orderLineLength = orderNode.getLength();
		if(orderLineLength != 0){
			Element orderElement = (Element)orderNode.item(0);
			NodeList oList = orderElement.getElementsByTagName("OrderLine");
			int oLength = oList.getLength();
			if(oLength != 0)
			{
				for(int oCounter = 0;oCounter<oLength;oCounter++)
				{
					Element oElement = (Element)oList.item(oCounter);
					String webLineNo = SCXmlUtil.getXpathAttribute(oElement, "./Extn/@ExtnWebLineNumber");

					if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo)) {
					log.debug("webLineNo"+webLineNo);
					}				
					
				    if(webLineNo==""){
						long uniqueSequenceNo = CallDBSequence.getNextDBSequenceNo(env, XPXLiterals.WEB_LINE_SEQUENCE);
						webLineNo = XPXAddParametersAPI.generateWebLineNumber(entryType,uniqueSequenceNo,envtCode);
			    	}
				    if(!YFCObject.isNull(webLineNo) && !YFCObject.isVoid(webLineNo)) {
				    log.debug("webLineNo in populateCurrentOrderHashtable method = " + webLineNo);
				    }
					//Element extnOrderLineElement = parentDoc.createElement("Extn");
					//oElement.appendChild(extnOrderLineElement);
				    //stamp the weblinenumber on the orderline
				    NodeList extnList = oElement.getElementsByTagName("Extn");
				    int extnLength = extnList.getLength();
				    if(extnLength != 0)
				    {
				    	Element extnElement = (Element)extnList.item(0);
				    	extnElement.setAttribute("ExtnWebLineNumber", webLineNo);
				    }
				   if(log.isDebugEnabled())
				   {
				   log.debug("oElement"+SCXmlUtil.getString(oElement));
				   }
			    	String weblinekey = getWebLineKey(env, oElement);
			    	
					htCurrentOrder.put(weblinekey, oElement);
				}
			}
			
		}
	}
	
	private Document setTemplate(YFSEnvironment env) throws Exception
	{
		Document templateDoc = YFCDocument.createDocument("OrderList").getDocument();
		Element templateElement = templateDoc.getDocumentElement();
		Element orderElement = templateDoc.createElement("Order");
		orderElement.setAttribute("OrderNo", "");
		orderElement.setAttribute("OrderHeaderKey", "");
		orderElement.setAttribute("BuyerOrganizationCode", "");
		orderElement.setAttribute("ShipToID", "");
		orderElement.setAttribute("DocumentType", "");
		orderElement.setAttribute("EnterpriseCode", "");
		//Added to fetch EntryType for XPXConvertToLegacyOrder
		orderElement.setAttribute("EntryType", "");
		templateElement.appendChild(orderElement);
		Element orderExtnElement = templateDoc.createElement("Extn");		
		orderExtnElement.setAttribute("ExtnEnvtId", "");
		//orderExtnElement.setAttribute("ExtnTotalOrderValue", "");
		orderElement.appendChild(orderExtnElement);
		Element orderInssElement = templateDoc.createElement("Instructions");
		orderElement.appendChild(orderInssElement);
		Element orderInsElement = templateDoc.createElement("Instruction");
		orderInssElement.appendChild(orderInsElement);
		Element oLinesElement = templateDoc.createElement("OrderLines");
		orderElement.appendChild(oLinesElement);
		Element oLineElement = templateDoc.createElement("OrderLine");
		oLineElement.setAttribute("OrderLineKey", "");
		oLineElement.setAttribute("PrimeLineNo", "");
		oLineElement.setAttribute("SubLineNo", "");
		oLineElement.setAttribute("OrderedQty","");
		oLineElement.setAttribute("ShipNode","");
		oLineElement.setAttribute("LineType", "");
		oLinesElement.appendChild(oLineElement);
		Element extnElement = templateDoc.createElement("Extn");
		extnElement.setAttribute("ExtnWebLineNumber", "");	
		extnElement.setAttribute("ExtnLegacyLineNumber","" );
		//extnElement.setAttribute("ExtnLineOrderedTotal", "");
		oLineElement.appendChild(extnElement);
		
		Element itemElement = templateDoc.createElement("Item");
		itemElement.setAttribute("ItemID", "");
		itemElement.setAttribute("UnitOfMeasure", "");
		oLineElement.appendChild(itemElement);
		
		Element linePriceElement = templateDoc.createElement("LinePriceInfo");
		linePriceElement.setAttribute("UnitPrice", "");
		oLineElement.appendChild(linePriceElement);

		Element lineOverallTotalsElement = templateDoc.createElement("LineOverallTotals");
		lineOverallTotalsElement.setAttribute("ExtendedPrice", "");
		oLineElement.appendChild(lineOverallTotalsElement);
		
		/***********Added by Prasanth Kumar M. for Bug #11348*********************/
		
		Element orderLineTranQtyElement = templateDoc.createElement(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY);
		orderLineTranQtyElement.setAttribute(XPXLiterals.A_ORDERED_QTY, "");
		oLineElement.appendChild(orderLineTranQtyElement);
		
		/*************************************************************************/

		Element overallTotalsElement = templateDoc.createElement("OverallTotals");
		overallTotalsElement.setAttribute("LineSubTotal", "");
		orderElement.appendChild(overallTotalsElement);
		
		
		return templateDoc;
	}
	
	private Document setTemplateForChangeOrder(YFSEnvironment env) throws Exception
	{
		Document templateDoc = YFCDocument.createDocument("Order").getDocument();
		//Element templateElement = templateDoc.getDocumentElement();
		Element orderElement = templateDoc.getDocumentElement();
		orderElement.setAttribute("OrderNo", "");
		orderElement.setAttribute("OrderHeaderKey", "");
		orderElement.setAttribute("BuyerOrganizationCode", "");
		orderElement.setAttribute("ShipToID", "");
		orderElement.setAttribute("DocumentType", "");
		orderElement.setAttribute("EnterpriseCode", "");
		//templateElement.appendChild(orderElement);
		Element orderExtnElement = templateDoc.createElement("Extn");
		orderExtnElement.setAttribute("ExtnWebConfNum", "");
		orderExtnElement.setAttribute("ExtnTotalOrderValue", "");
		orderElement.appendChild(orderExtnElement);
		Element oLinesElement = templateDoc.createElement("OrderLines");
		orderElement.appendChild(oLinesElement);
		Element oLineElement = templateDoc.createElement("OrderLine");
		oLineElement.setAttribute("OrderLineKey", "");
		oLineElement.setAttribute("PrimeLineNo", "");
		oLineElement.setAttribute("SubLineNo", "");
		oLineElement.setAttribute("OrderedQty","");
		oLineElement.setAttribute("ShipNode", "");
		oLineElement.setAttribute("LineType", "");
		oLinesElement.appendChild(oLineElement);
		Element extnElement = templateDoc.createElement("Extn");
		extnElement.setAttribute("ExtnWebLineNumber", "");	
		extnElement.setAttribute("ExtnLegacyLineNumber","" );
		//extnElement.setAttribute("ExtnLineOrderedTotal", "");
		oLineElement.appendChild(extnElement);
		
		Element itemElement = templateDoc.createElement("Item");
		itemElement.setAttribute("ItemID", "");
		itemElement.setAttribute("UnitOfMeasure", "");
		oLineElement.appendChild(itemElement);
		
		Element linePriceElement = templateDoc.createElement("LinePriceInfo");
		linePriceElement.setAttribute("UnitPrice", "");
		oLineElement.appendChild(linePriceElement);

		Element lineOverallTotalsElement = templateDoc.createElement("LineOverallTotals");
		lineOverallTotalsElement.setAttribute("ExtendedPrice", "");
		oLineElement.appendChild(lineOverallTotalsElement);
		
        /***********Added by Prasanth Kumar M. for Bug #11348*********************/
		
		Element orderLineTranQtyElement = templateDoc.createElement(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY);
		orderLineTranQtyElement.setAttribute(XPXLiterals.A_ORDERED_QTY, "");
		oLineElement.appendChild(orderLineTranQtyElement);
		
		/*************************************************************************/

		Element overallTotalsElement = templateDoc.createElement("OverallTotals");
		overallTotalsElement.setAttribute("LineSubTotal", "");
		orderElement.appendChild(overallTotalsElement);
		
		
		return templateDoc;
	}
	
	private void setOrderHeaderLevelAttributes(Element inputElement,
			Element outputLineElement) {
		if(inputElement.hasAttribute("ShipNode"))
		outputLineElement.setAttribute("ShipNode", inputElement.getAttribute("ShipNode"));
		if(inputElement.hasAttribute("CustomerPONo"))
		outputLineElement.setAttribute("CustomerPONo", inputElement.getAttribute("CustomerPONo"));
		if(inputElement.hasAttribute("ReqDeliveryDate"))
		outputLineElement.setAttribute("ReqDeliveryDate", inputElement.getAttribute("ReqDeliveryDate"));
		if(inputElement.hasAttribute("OrderDate"))
		outputLineElement.setAttribute("OrderDate", inputElement.getAttribute("OrderDate"));
		if(inputElement.hasAttribute("SourceType"))
		outputLineElement.setAttribute("SourceType", inputElement.getAttribute("SourceType"));
	}
	
	public void printHashtables(){

		if(log.isDebugEnabled()) {
		log.debug("htFulfillmentOrder Size is : "+htFulfillmentOrder.size());
		}
		Enumeration<String> Enumeration = htFulfillmentOrder.keys (); 
	    while (Enumeration.hasMoreElements ()) { 
	        String key = (String) Enumeration.nextElement ();
	        if(log.isDebugEnabled()) {
	        log.debug("WeblineNo Key for htFulfillmentOrder is :  " + key);
	        }
	    }
	    if(log.isDebugEnabled()) {    
		log.debug("htCustomerOrder Size is : "+htCustomerOrder.size());
	    }
		Enumeration = htCustomerOrder.keys (); 
	    while (Enumeration.hasMoreElements ()) { 
	        String key = (String) Enumeration.nextElement (); 
	        if(log.isDebugEnabled()) {
	        log.debug("WeblineNo Key for htCustomerOrder is : " + key);
	        }
	    }		
	    if(log.isDebugEnabled()) {
		log.debug("htCurrentOrder Size is : "+htCurrentOrder.size());
	    }
		Enumeration = htCurrentOrder.keys (); 
	    while (Enumeration.hasMoreElements ()) { 
	        String key = (String) Enumeration.nextElement (); 
	        if(log.isDebugEnabled()) {
	        log.debug("WeblineNo Key for htCurrentOrder is : " + key);
	        }
	    }
	}
	
	private String getWeblinenoFromWeblinekey(String weblinekey){
		String weblineno="";
		weblineno = weblinekey.substring(0, weblinekey.indexOf("|"));
		return weblineno;
	}
	
	private String getWebLineKey(YFSEnvironment env, Element inputLineElement) throws Exception

	{

	      String key="";
	      String webLineNumber = "";
	      //String itemID = "";
	      //String shipNode = "";
	      String legacyLineNumber="";	      
	      
	      webLineNumber = SCXmlUtil.getXpathAttribute(inputLineElement, "./Extn/@ExtnWebLineNumber");
	      legacyLineNumber = SCXmlUtil.getXpathAttribute(inputLineElement, "./Extn/@ExtnLegacyLineNumber");
	      
	     // itemID = SCXmlUtil.getXpathAttribute(inputLineElement, "./Item/@ItemID");
	     // shipNode = inputLineElement.getAttribute("ShipNode");
	      
	      	      
		 if(webLineNumber==""){
				long uniqueSequenceNo = CallDBSequence.getNextDBSequenceNo(env, XPXLiterals.WEB_LINE_SEQUENCE);
				webLineNumber = XPXAddParametersAPI.generateWebLineNumber(entryType,uniqueSequenceNo,envtCode);
				Element extnOrderLineElement = (Element)inputLineElement.getElementsByTagName("Extn").item(0);
				extnOrderLineElement.setAttribute("ExtnWebLineNumber", webLineNumber);
	      }
	      
	      
	      key = webLineNumber+"|"+legacyLineNumber;
	      return key;

	}

	
	public static void copyAttributes(Node fromNode, Node toNode)
	throws Exception
	{
		NamedNodeMap fromAttrbMap = fromNode.getAttributes();
	
		Element toElement = (Element)toNode;
		if (fromAttrbMap != null) {
			int fromAttrbMapLength = fromAttrbMap.getLength();
	
			for (int i = 0; i < fromAttrbMapLength; i++) {
				Node fAttrbNode = fromAttrbMap.item(i);
	
				if ((fAttrbNode == null)
		            || (fAttrbNode.getNodeType() != Node.ATTRIBUTE_NODE)) {
		        	continue;
				}
	
			    String fAttrbName = fAttrbNode.getNodeName();
			    String fAttrbVal = fAttrbNode.getNodeValue();
			    toElement.setAttribute(fAttrbName, fAttrbVal);
	
			}
		}
	}

	
	/*public boolean identifyLineTypeChanges(String weblineno){
		boolean lineTypeFlag = false;
		Element oLineElement = htCurrentOrder.get(weblineno);
		Element foLineElement = htFulfillmentOrder.get(weblineno);
		Element coLineElement = htCustomerOrder.get(getWeblinenoFromWeblinekey(weblineno));
		log.debug("oLineElement"+SCXmlUtil.getString(oLineElement));
		log.debug("foLineElement"+SCXmlUtil.getString(foLineElement));
		log.debug("coLineElement"+SCXmlUtil.getString(coLineElement));
		String inputLinetype = oLineElement.getAttribute("LineType");
		log.debug("inputLinetype"+inputLinetype);
		String foLineType = foLineElement.getAttribute("LineType");
		if(!inputLinetype.equals(foLineType))
		{
			lineTypeFlag = true;
		}
		
		return lineTypeFlag;
		
	}*/
	
	
	/**
	 * @param weblineno
	 * 
	 * Method to cancel the line on fulfillment order on line type change
	 */
	/*public void cancelLineOnFulfillment(String weblineno){
		Element coLineElement = htCustomerOrder.get(getWeblinenoFromWeblinekey(weblineno));
		log.debug("Cancel existing line");
		//make the quantity 0 and set it in the hashtable
		Element oLineElement = htCurrentOrder.get(weblineno);
		log.debug("oLineElement"+SCXmlUtil.getString(oLineElement));
		//set the quantity to zero
		oLineElement.setAttribute("OrderedQty", "0");
		//set the quantity to zero in OrderLineTranQuantity
		NodeList oLineList = oLineElement.getElementsByTagName("OrderLineTranQuantity");
		Element tranQuantityElement = (Element)oLineList.item(0);
		tranQuantityElement.setAttribute("OrderedQty", "0");
		log.debug("oLineElement"+SCXmlUtil.getString(oLineElement));
		//htCurrentOrder.put(weblineno, oLineElement);
		htChangeFulfillmentOrder.put(weblineno, oLineElement);
		Element foLineElement = htFulfillmentOrder.get(weblineno);
		Float foQty =  Float.parseFloat( SCXmlUtil.getXpathAttribute(foLineElement,"./OrderLineTranQuantity/@OrderedQty"));
		//b. Customer Order Line status will move to Unscheduled, this has to be moved to "Created".
		Element chCustOrderStatusLineElement = inputOrderStatusChangeDoc.createElement("OrderLine");
		chCustOrderStatusLineElement.setAttribute("BaseDropStatus", "1100");
		chCustOrderStatusLineElement.setAttribute("PrimeLineNo",coLineElement.getAttribute("PrimeLineNo"));
		chCustOrderStatusLineElement.setAttribute("SubLineNo",coLineElement.getAttribute("SubLineNo"));
		chCustOrderStatusLineElement.setAttribute("Quantity", foQty.toString());
		htChangeStatusOnCustomerOrder.put(getWeblinenoFromWeblinekey(weblineno),chCustOrderStatusLineElement);
		
	}*/
	
	
	public Document createFulfillmentOrder(String weblinekey,YFSEnvironment env, String lineType) throws YFSException, RemoteException{
		
		//form the input xml
		Document newInputDoc1 = YFCDocument.createDocument().getDocument();
		newInputDoc1.appendChild(newInputDoc1.importNode(inputElement, true));
		newInputDoc1.renameNode(newInputDoc1.getDocumentElement(), newInputDoc1.getNamespaceURI(), "Order");
		Element newInputElement = newInputDoc1.getDocumentElement();
		newInputElement.setAttribute("Action", "CREATE");
		newInputElement.removeAttribute("OrderHeaderKey");
		newInputElement.removeAttribute("OrderNo");
		NodeList orderLinesList = newInputElement.getElementsByTagName("OrderLines");
		Element orderLinesElement = (Element)orderLinesList.item(0);
		newInputElement.removeChild(orderLinesElement);
		Element newOrderLinesElement = newInputDoc1.createElement("OrderLines");
		newInputElement.appendChild(newOrderLinesElement);
		Element orderLineElement = htCurrentOrder.get(weblinekey);
		Node orderLineNode = newInputDoc1.importNode(orderLineElement, true);
		Element orderElement = (Element)orderLineNode;
		orderElement.removeAttribute("OrderLineKey");
		Element fulElement = htFulfillmentOrder.get(weblinekey);
		orderElement.setAttribute("OrderedQty", fulElement.getAttribute("OrderedQty"));
		NodeList tranList = orderElement.getElementsByTagName("OrderLineTranQuantity");
		Element tranElement = (Element)tranList.item(0);
		tranElement.setAttribute("OrderedQty", fulElement.getAttribute("OrderedQty"));
		newOrderLinesElement.appendChild(orderElement);
		Element chainedElement = newInputDoc1.createElement("ChainedFrom");
		chainedElement.setAttribute("DocumentType", "0001");
		chainedElement.setAttribute("EnterpriseCode", "xpedx");
		chainedElement.setAttribute("OrderNo", customerOrderDetails.get(1));
		chainedElement.setAttribute("SubLineNo", "1");
		//get the prime line number to be chained
		Element customerElement = htCustomerOrder.get(getWeblinenoFromWeblinekey(weblinekey));
		String primeLine = customerElement.getAttribute("PrimeLineNo");
		chainedElement.setAttribute("PrimeLineNo", primeLine);
		orderElement.appendChild(chainedElement);
		return newInputDoc1;
		
		
	}
	
	/**
	 * @param env
	 * @throws RemoteException 
	 * @throws YFSException 
	 * @throws YFSException
	 * @throws RemoteException
	 * 
	 * Method to create a new Fulfillment order for the cancelled line on Line Type change
	 */
	/*public void createNewFulfillmentOrder(YFSEnvironment env) throws YFSException, RemoteException
	{
		Enumeration<String> Enumeration = htCurrentOrder.keys (); 
	    while (Enumeration.hasMoreElements ()) { 
	        String weblinekey = (String) Enumeration.nextElement (); 
	        boolean lineTypeChangeFlag = identifyLineTypeChanges(weblinekey);
        	if(lineTypeChangeFlag)
        	{
        		Element orderLineElement = htCurrentOrder.get(weblinekey);
        		String lineType = orderLineElement.getAttribute("LineType");
        		//check if already a fulfillment order with this line type exists
        		Document inputCreateDoc = createFulfillmentOrder(weblinekey, env, lineType);
        		Document newOrderDoc = api.invoke(env, "createOrder", inputCreateDoc);
        		
        	}
	    }
	}*/
	
	public ArrayList<String> identityLineTypeChange(String webLineKey, YFSEnvironment env) throws YFSException, RemoteException
	{
		String changeFlag = "false";
		String currentLineType = "";
		String extnLineType = "";
		String extnOrderType = "";	
		String shipToID = "";
		String allowDirectOrderFlag = "";		
		//get the orderType on the fulfillment order
		String orderType = inputElement.getAttribute("OrderType");
		Element currentElement = htCurrentOrder.get(webLineKey);
		//String item = currentElement.getAttribute("ItemID");
		String item = SCXmlUtil.getXpathAttribute(currentElement, "./Item/@ItemID");
		String shipNode = currentElement.getAttribute("ShipNode");
		extnLineType = getLineType(env, item, shipNode);
		//if(!orderType.equals(extnLineType) && extnLineType.equals("DIRECT"))
		
		/* Start - Changes made for Split order logic */
		if(currentElement.hasAttribute("LineType")){
			currentLineType = currentElement.getAttribute("LineType");
		}			
		extnLineType = XPXUtils.getLineTypeFromInventoryIndicator(env, extnLineType, item, currentLineType);	
		
		extnOrderType = extnLineType.toUpperCase()+"_"+"ORDER";
		
		// Order split needs to be done based on 'Allow direct orders' flag at Ship To customer level.
		if(inputElement.hasAttribute("ShipToID")){
		shipToID = inputElement.getAttribute("ShipToID");
			if( !YFCObject.isNull(shipToID) && !YFCObject.isVoid(shipToID)){
			allowDirectOrderFlag = XPXUtils.getAllowDirectOrderForShipToCust(env, shipToID);
			}
		}
		/* End - Changes made for Split order logic */
		
		// 'allowDirectOrderFlag' defines whether a split is required or not. Flag is stamped at 'Ship To' customer level.
		if(!orderType.equalsIgnoreCase(extnOrderType) && extnOrderType.equals("DIRECT_ORDER") 
				&& allowDirectOrderFlag.equalsIgnoreCase("Y")){
			changeFlag = "true";
		}

		
		log.debug("");
		if(!YFCObject.isNull(orderType) && !YFCObject.isVoid(orderType)) {
		log.debug("orderType = " + orderType);
		}
		if(!YFCObject.isNull(extnOrderType) && !YFCObject.isVoid(extnOrderType)) {
		log.debug("extnOrderType = " + extnOrderType);
		}
		if(!YFCObject.isNull(extnLineType) && !YFCObject.isVoid(extnLineType)) {
		log.debug("extnLineType = " + extnLineType);
		}
		if(!YFCObject.isNull(changeFlag) && !YFCObject.isVoid(changeFlag)) {
		log.debug("changeFlag = " + changeFlag);
		}
		log.debug("");
		
		ArrayList<String> lineTypeList = new ArrayList<String>();
		lineTypeList.add(changeFlag);
		lineTypeList.add(extnOrderType);
		lineTypeList.add(extnLineType);
		
		return lineTypeList;
	}
	
	public String getLineType(YFSEnvironment env, String item, String shipNode) throws YFSException, RemoteException
	{
		String lineType = "";
		if(!YFCObject.isNull(item) && !YFCObject.isVoid(item) && !YFCObject.isNull(shipNode) && !YFCObject.isVoid(shipNode)){
			//form the input xml and get the sourceIndicator to find the extnLineType
			Document inputItemBranchDoc = YFCDocument.createDocument("XPXItemExtn").getDocument();
			Element inputItemBranchElement = inputItemBranchDoc.getDocumentElement();
			inputItemBranchElement.setAttribute("ItemID", item);
			if(shipNode.contains("_")){
				// Need to remove the environment id from ship node.
				String[] splitArrayOnUom = shipNode.split("_");			
				if(splitArrayOnUom.length > 0){
				shipNode = splitArrayOnUom[0];
				}
			}
			inputItemBranchElement.setAttribute("XPXDivision", shipNode);
			//get the list
			Document itemBranchList = api.executeFlow(env, "getXPXItemBranchListService", inputItemBranchDoc);
			NodeList itemExtnList = itemBranchList.getElementsByTagName("XPXItemExtn");
			int itemLength = itemExtnList.getLength();
			if(itemLength != 0)
			{
				Element itemExtnElement = (Element)itemExtnList.item(0);
				lineType = itemExtnElement.getAttribute("InventoryIndicator");
			}				
		}
		return lineType;
	}
	
	public void checkInstructionsInOrder(YFSEnvironment env, Element inputElement) throws YFSException, RemoteException
	{	
		// To get the ExtcheckInstructionsInOrdern element at the order level to stamp the ExtnWebHoldFlag attribute.
	    Element extnElem = null;
	    NodeList extnNodeList  = inputElement.getElementsByTagName("Extn");
		if(extnNodeList.getLength() > 0){
		extnElem = (Element) extnNodeList.item(0);
		}
		
		// Check for instructions in the order and set the web hold flag to 'Y' if instruction exist.
		if(extnElem != null){
			NodeList instructionsList = inputElement.getElementsByTagName("Instruction");
			int instructionListSize = instructionsList.getLength();
			if(!YFCObject.isNull(instructionListSize) && !YFCObject.isVoid(instructionListSize)) {
			log.debug("instructionListSize = " + instructionListSize);
			}
			for(int i=0;i<instructionListSize;i++){
				Element instructionElement = (Element) instructionsList.item(i);			
				if(instructionElement.hasAttribute("InstructionType")){
					String instructionType = instructionElement.getAttribute("InstructionType");
					if(!YFCObject.isNull(instructionType) && !YFCObject.isVoid(instructionType)) {
					log.debug("instructionType = " + instructionType);
					}
					if(!YFCObject.isNull(instructionType) && !YFCObject.isVoid(instructionType) 
							&& (instructionType.equalsIgnoreCase("HEADER") || instructionType.equalsIgnoreCase("LINE")) ){
						extnElem.setAttribute("ExtnWebHoldFlag", "Y");
						extnElem.setAttribute("ExtnWebHoldReason", "Order has Header or Line Instructions.");
						break;
					}
				}
			}
		}
	}
	
	public void mergeLegacyAttributes(YFSEnvironment env,Document sOrderToLegacyDoc,Document changeOrderFODoc) throws YFSException, RemoteException{

		HashMap foOrdLineMap = new HashMap();
		String headerProcessCode = "";
		
		// To form a map which holds ordered quantity of each line.
		if(sOrderToLegacyDoc != null){
		Element orderElem = sOrderToLegacyDoc.getDocumentElement();
		NodeList orderLineListF = orderElem.getElementsByTagName("OrderLine");
		int orderLinesize = orderLineListF.getLength();
		for(int lineCount = 0 ; lineCount < orderLinesize; lineCount++){
			Element orderLineElem = (Element) orderLineListF.item(lineCount);
			Element extnElement = (Element) orderLineElem.getElementsByTagName("Extn").item(0);
			String webLineNum = extnElement.getAttribute("ExtnWebLineNumber");
			if(!YFCObject.isNull(webLineNum) && !YFCObject.isVoid(webLineNum) ){
			foOrdLineMap.put(webLineNum, orderLineElem);
			}
		}
		
		
		// To set the 'HeaderProcessCode' attribute.
		headerProcessCode = sOrderToLegacyDoc.getDocumentElement().getAttribute("HeaderProcessCode");
		Element chgOrdRootElem = changeOrderFODoc.getDocumentElement();
		chgOrdRootElem.setAttribute("HeaderProcessCode",headerProcessCode);
		Element extnOrderElemT = (Element) chgOrdRootElem.getElementsByTagName("Extn").item(0);
		
		// To copy Web Hold flag and Web Hold reason.
		Element extnOrderElemF = (Element) orderElem.getElementsByTagName("Extn").item(0);
		if(extnOrderElemF.hasAttribute("ExtnWebHoldFlag")){
			String webHoldFlag = extnOrderElemF.getAttribute("ExtnWebHoldFlag");
			extnOrderElemT.setAttribute("ExtnWebHoldFlag", webHoldFlag);					
		}
		if(extnOrderElemF.hasAttribute("ExtnWebHoldReason")){
			String webHoldReason = extnOrderElemF.getAttribute("ExtnWebHoldReason");
			extnOrderElemT.setAttribute("ExtnWebHoldReason", webHoldReason);					
		}
		
		// To set the line level attributes.
		if(foOrdLineMap != null && foOrdLineMap.size() > 0 ){
			NodeList orderLineList = chgOrdRootElem.getElementsByTagName("OrderLine");
			int ordLineListSize = orderLineList.getLength();
			for(int i=0;i<ordLineListSize;i++){
				Element orderLineElemT = (Element) orderLineList.item(i);
				Element extnElementT = (Element)orderLineElemT.getElementsByTagName("Extn").item(0);
				String webLineNo = extnElementT.getAttribute("ExtnWebLineNumber");
				// To get the order line which holds the legacy attributes.
				Element orderLineElemF = (Element)foOrdLineMap.get(webLineNo);
				Element extnLineElementF = (Element)orderLineElemF.getElementsByTagName("Extn").item(0);
				String lineProcessCode = orderLineElemF.getAttribute("LineProcessCode");
				// To set LineProcessCode.
				orderLineElemT.setAttribute("LineProcessCode", lineProcessCode);
						
			}	
		}
		
		// To set the web hold reason if web hold flag is Y.
		if(extnOrderElemT.hasAttribute("ExtnWebHoldFlag")){
			String webHoldFlag = extnOrderElemT.getAttribute("ExtnWebHoldFlag");
			
			if(!YFCObject.isNull(webHoldFlag) && webHoldFlag.equalsIgnoreCase("Y")){
				String webHoldReason = extnOrderElemT.getAttribute("ExtnWebHoldReason");
				if(YFCObject.isNull(webHoldReason) || YFCObject.isVoid(webHoldReason)){
					
					// To get order update flag from Customer.
					String customerId = chgOrdRootElem.getAttribute("BuyerOrganizationCode");
					Document getCustomerListInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER).getDocument();
					getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, customerId);
					env.setApiTemplate("getCustomerList", getCustomerListTemplate);
					Document getCustListOutputDoc = api.invoke(env, "getCustomerList", getCustomerListInputDoc);
					env.clearApiTemplate("getCustomerList");
					Element customerElem = (Element) getCustListOutputDoc.getDocumentElement().getElementsByTagName("Customer").item(0);
					Element customerExtnElement = (Element) customerElem.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
					String orderUpdateFlag = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_ORDER_UPDATE_FLAG);
					
					// To get willCallFlag.
					String willCallFlag =  extnOrderElemT.getAttribute(XPXLiterals.A_EXTN_WILL_CALL_FLAG);
					
					// To update web hold reason if web hold flag is 'Y'
					if((XPXLiterals.CONSTANT_CHAR_P).equalsIgnoreCase(willCallFlag) &&
							!(XPXLiterals.BOOLEAN_FLAG_N).equalsIgnoreCase(orderUpdateFlag))
					{
						log.debug("Inside first if loop of mergeLegacyAttributes in XPXUpdateChainedOrderAPI ");
						extnOrderElemT.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON,XPXLiterals.WEB_HOLD_FLAG_REASON_1);
					}
					
					else if((XPXLiterals.BOOLEAN_FLAG_N).equalsIgnoreCase(orderUpdateFlag) &&
							!(XPXLiterals.CONSTANT_CHAR_P).equalsIgnoreCase(willCallFlag))
					{
						log.debug("Inside second if loop of mergeLegacyAttributes in XPXUpdateChainedOrderAPI ");
						extnOrderElemT.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON,XPXLiterals.WEB_HOLD_FLAG_REASON_2);
					}
					
					else if ((XPXLiterals.CONSTANT_CHAR_P).equalsIgnoreCase(willCallFlag) &&
							(XPXLiterals.BOOLEAN_FLAG_N).equalsIgnoreCase(orderUpdateFlag))
					{
						log.debug("Inside third if loop of mergeLegacyAttributes in XPXUpdateChainedOrderAPI ");
						extnOrderElemT.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON,XPXLiterals.WEB_HOLD_FLAG_REASON_1_AND_2);
					} else {
						log.debug("Inside else loop of mergeLegacyAttributes in XPXUpdateChainedOrderAPI");
						extnOrderElemT.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON,"Order is on Web Hold.");
					}
					
				}
			}
			
		}
		
		}
	}
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub	
	}
}

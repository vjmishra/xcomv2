package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXUpdateLegacyOrderNoAPI implements YIFCustomApi
{
	private static YFCLogCategory log;
	private static YIFApi api = null;
	
	String getOrganizationListTemplate = "global/template/api/getOrderList.XPXUpdateExtnOrderStatus.xml";
	
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
	
	public Document updateSterlingOrders(YFSEnvironment env, Document inputXML)
	{
		Document changeOrderInputDoc = null;
		Element rootElem = inputXML.getDocumentElement();
		HashMap foDetailsMap = new HashMap();
		
		String orderHeaderKeyFO = SCXmlUtil.getXpathElement(rootElem, "./OrderHeaderKey").getTextContent();
		String legacyOrderNumber = SCXmlUtil.getXpathElement(rootElem, "./LegacyOrderNumber").getTextContent();
		String generationNumber = SCXmlUtil.getXpathElement(rootElem, "./GenerationNumber").getTextContent();
		
		HashMap webLineNumberList = getWebLineNumber(rootElem);
		
		if(!YFCObject.isNull(orderHeaderKeyFO) && !YFCObject.isVoid(orderHeaderKeyFO)){
			foDetailsMap = getFulFillOrderDetails(env,rootElem,orderHeaderKeyFO);
		} else {	
			foDetailsMap = getFulFilOrderDetails(env,rootElem);
		}
		// String webConfirmationNumber = SCXmlUtil.getXpathElement(rootElem, "./WebConfirmationNumber").getTextContent();
		//String customerOrderHeaderKey = getCustomerOrderHeaderKey(env,webConfirmationNumber);
		
		changeOrderInputDoc = getChangeOrderInputDoc(foDetailsMap,webLineNumberList,legacyOrderNumber,generationNumber);
		if(inputXML != null){
			log.debug("UpdateSterlingOrders_ChangeOrderInputDoc:"+ SCXmlUtil.getString(changeOrderInputDoc));
		}
		try {
			api.invoke(env, XPXLiterals.CHANGE_ORDER_API, changeOrderInputDoc);
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputXML;
	}
	
	private Document getChangeOrderInputDoc(HashMap fulfilOrderDetails, HashMap webLineNumberList, String legacyOrderNumber,String generationNumber)
	{
		
		Document changeOrderInputDoc = YFCDocument.createDocument("Order").getDocument();
		changeOrderInputDoc.getDocumentElement().setAttribute("OrderHeaderKey", (String) fulfilOrderDetails.get("OrderHeaderKey"));
		changeOrderInputDoc.getDocumentElement().setAttribute("Action", "MODIFY");
		
		Element orderExtn  = changeOrderInputDoc.createElement("Extn");
		orderExtn.setAttribute("ExtnLegacyOrderNo", legacyOrderNumber);
		orderExtn.setAttribute("ExtnGenerationNo", generationNumber);
		
		/***************Added as part of CR# 2589 by Prasanth Kumar M.*************/
		orderExtn.setAttribute("ExtnIsReprocessibleFlag", "N");
		/**************************************************************************/
		
		changeOrderInputDoc.getDocumentElement().appendChild(orderExtn);
		
		
		Element orderLines = changeOrderInputDoc.createElement("OrderLines");
		
		Set<Entry<String, String>> set = webLineNumberList.entrySet();
		Iterator<Entry<String, String>> iter = set.iterator();

		while (iter.hasNext()) 
		{

			Map.Entry me1 = iter.next();

			String webLineNumberListKey = (String) me1.getKey();
			String webLineNumberListValue = (String) me1.getValue();
			
			String fulfilOrderDetailsValue = (String) fulfilOrderDetails.get(webLineNumberListKey);
			
			Element orderLineElement = changeOrderInputDoc.createElement("OrderLine");
			orderLineElement.setAttribute("OrderLineKey", fulfilOrderDetailsValue);
			Element extnElement = changeOrderInputDoc.createElement("Extn");
			extnElement.setAttribute("ExtnLegacyLineNumber", webLineNumberListValue);
			
			orderLineElement.appendChild(extnElement);
			orderLines.appendChild(orderLineElement);

		}
		changeOrderInputDoc.getDocumentElement().appendChild(orderLines);
		if(log.isDebugEnabled()){
			log.debug("The final changeOrder input doc is: "+SCXmlUtil.getString(changeOrderInputDoc));
		}
		return changeOrderInputDoc;
	}
	
	private HashMap getFulFillOrderDetails(YFSEnvironment env,Element rootElem, String OrderHeaderKeyFO)
	{
        HashMap fulfilOrderDetails = new HashMap();
        fulfilOrderDetails.put("OrderHeaderKey", OrderHeaderKeyFO);
		
		Document getOrderListInputDoc = YFCDocument.createDocument(XPXLiterals.E_ORDER).getDocument();
		getOrderListInputDoc.getDocumentElement().setAttribute("OrderHeaderKey", OrderHeaderKeyFO);
		
		try
		{
			env.setApiTemplate(XPXLiterals.GET_ORDER_LIST_API, getOrganizationListTemplate);
			Document getOrderListOutputDoc = api.invoke(env, XPXLiterals.GET_ORDER_LIST_API, getOrderListInputDoc);
			
			env.clearApiTemplate(XPXLiterals.GET_ORDER_LIST_API);
			
			YFCDocument yfcDoc = YFCDocument.getDocumentFor(getOrderListOutputDoc);
			YFCElement orderListElem = yfcDoc.getDocumentElement();
			
			if(orderListElem.hasChildNodes()){
				YFCElement orderElem = orderListElem.getChildElement("Order");
				YFCElement ordLinesEle = orderElem.getChildElement("OrderLines");
				if(ordLinesEle != null) {
					YFCIterable yfcItr = ordLinesEle.getChildren("OrderLine");
					while(yfcItr.hasNext()) {
						YFCElement ordLineEle = (YFCElement) yfcItr.next();
						if(ordLineEle != null) {
							YFCElement extnElem = ordLineEle.getChildElement("Extn");
							if(extnElem != null) {
								fulfilOrderDetails.put(extnElem.getAttribute("ExtnWebLineNumber"), ordLineEle.getAttribute("OrderLineKey"));
							}
						}
					}
				}
			}
						
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return fulfilOrderDetails;
	}
	
	private HashMap getFulFilOrderDetails(YFSEnvironment env,Element inputDocRoot)
	{
        String fulfillmentOrderHeaderKey="";
        HashMap fulfilOrderDetails = new HashMap();
		
		Document getOrderLineListInputDoc = YFCDocument.createDocument(XPXLiterals.E_ORDER_LINE).getDocument();
	
		Element orderLineExtn = getOrderLineListInputDoc.createElement(XPXLiterals.E_EXTN);
		
		String firstWebLineNumber = getFirstWebLineNumber(inputDocRoot);
		if(log.isDebugEnabled()){
        	log.debug("The first webline number is:"+firstWebLineNumber); 
		}
		orderLineExtn.setAttribute("ExtnWebLineNumber", firstWebLineNumber);
		
		getOrderLineListInputDoc.getDocumentElement().appendChild(orderLineExtn);
		
		Element orderElem = getOrderLineListInputDoc.createElement(XPXLiterals.E_ORDER);
		orderElem.setAttribute(XPXLiterals.A_ORDER_TYPE, XPXLiterals.CUSTOMER);
		orderElem.setAttribute("OrderTypeQryType", "NE");
		getOrderLineListInputDoc.getDocumentElement().appendChild(orderElem);
		
		
		
		Document getOrderLineListTemplate = YFCDocument.createDocument("OrderLineList").getDocument();
		Element orderLine = getOrderLineListTemplate.createElement("OrderLine");
		orderLine.setAttribute("OrderHeaderKey", "");
		//orderLine.setAttribute("OrderLineKey", "");
		Element order = getOrderLineListTemplate.createElement("Order");
		order.setAttribute("OrderType", "");
		Element orderLines = getOrderLineListTemplate.createElement("OrderLines");
		Element orderLineElement = getOrderLineListTemplate.createElement("OrderLine");
		orderLineElement.setAttribute("OrderLineKey", "");
		Element orderLineExtnElement = getOrderLineListTemplate.createElement("Extn");
		orderLineExtnElement.setAttribute("ExtnWebLineNumber","");
		
		orderLineElement.appendChild(orderLineExtnElement);
		orderLines.appendChild(orderLineElement);
		order.appendChild(orderLines);
		orderLine.appendChild(order);
		getOrderLineListTemplate.getDocumentElement().appendChild(orderLine);
			
		//Document getOrderLineListTemplate = getGetOrderLineListOutputTemplate();
		if(log.isDebugEnabled()){
			log.debug("The template to getOrderLineList API is: "+SCXmlUtil.getString(getOrderLineListTemplate));
		}
		
		try
		
		{
			if(log.isDebugEnabled()){
				log.debug("The template to getOrderLineList API is: "+SCXmlUtil.getString(getOrderLineListTemplate));
				log.debug("The input to getOrderLineListAPI is: "+SCXmlUtil.getString(getOrderLineListInputDoc));
			}
			env.setApiTemplate(XPXLiterals.GET_ORDER_LINE_LIST_API, getOrderLineListTemplate);
			Document getOrderLineListOutputDoc = api.invoke(env, XPXLiterals.GET_ORDER_LINE_LIST_API, getOrderLineListInputDoc);
			env.clearApiTemplate(XPXLiterals.GET_ORDER_LINE_LIST_API);
			if(log.isDebugEnabled()){
				log.debug("The output of getOrderLineList api is: "+SCXmlUtil.getString(getOrderLineListOutputDoc));
			}
			
			Element orderLineElements = (Element)getOrderLineListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_ORDER_LINE).item(0);
			fulfillmentOrderHeaderKey = orderLineElements.getAttribute("OrderHeaderKey");
			
			Element orderElement = (Element) orderLineElements.getElementsByTagName("Order").item(0);
			Element orderLinesElement = (Element) orderElement.getElementsByTagName("OrderLines").item(0);
			
			NodeList orderLineList = orderLinesElement.getElementsByTagName("OrderLine");
					
			//NodeList orderLineList = getOrderLineListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_ORDER_LINE);
			//Element firstOrderLineElement = (Element) orderLineList.item(0);
			fulfillmentOrderHeaderKey = orderLineElements.getAttribute("OrderHeaderKey");
			
			
			for(int i=0; i<orderLineList.getLength(); i++)
			{
				Element ordLine = (Element) orderLineList.item(i);
				
				Element ordLineExtn = (Element) ordLine.getElementsByTagName("Extn").item(0);
				
				fulfilOrderDetails.put(ordLineExtn.getAttribute("ExtnWebLineNumber"), ordLine.getAttribute("OrderLineKey"));
								
			}
				
			fulfilOrderDetails.put("OrderHeaderKey", fulfillmentOrderHeaderKey);
			
			
			if(log.isDebugEnabled()){
				log.debug("The fulfillment order header key is: "+fulfillmentOrderHeaderKey);
			}			
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return fulfilOrderDetails;
	}
	
	/** Added by Arun Sekhar on 02-March-2011 **/
	
	private Document getGetOrderLineListOutputTemplate(){
		Document getOrderLineListTemplate = YFCDocument.createDocument("OrderLineList").getDocument();
		Element orderLine = getOrderLineListTemplate.createElement("OrderLine");
		orderLine.setAttribute("OrderHeaderKey", "");
		orderLine.setAttribute("OrderLineKey", "");
		Element order = getOrderLineListTemplate.createElement("Order");
		order.setAttribute("OrderType", "");
		orderLine.appendChild(order);
		
		Element orderLineExtnElement = getOrderLineListTemplate.createElement("Extn");
		orderLineExtnElement.setAttribute("ExtnWebLineNumber","");	
		orderLine.appendChild(orderLineExtnElement);

		getOrderLineListTemplate.getDocumentElement().appendChild(orderLine);
		
		return getOrderLineListTemplate;
	}
	private String getFirstWebLineNumber(Element inputDocRoot)
	{

        Element lineItemsElement =  (Element)inputDocRoot.getElementsByTagName("LineItems").item(0);
        
        Element firstwebLineElement = (Element) lineItemsElement.getElementsByTagName("LineItem").item(0);
        
        String webLineNumber = SCXmlUtil.getXpathElement(firstwebLineElement, "./WebLineNumber").getTextContent();
		
		return webLineNumber;
	}
	private HashMap getWebLineNumber(Element inputDocRoot) 
	{
		HashMap webLineListMap = new HashMap();
        Element lineItemsElement =  (Element)inputDocRoot.getElementsByTagName("LineItems").item(0); 
        NodeList webLineList = lineItemsElement.getElementsByTagName("LineItem");
        for(int i=0; i<webLineList.getLength(); i++)
        {
        	Element webLine = (Element) webLineList.item(i);	
        	String webLineNumber = SCXmlUtil.getXpathElement(webLine, "./WebLineNumber").getTextContent();
        	String legacyLineNumber = SCXmlUtil.getXpathElement(webLine, "./LegacyLineNumber").getTextContent();	
        	webLineListMap.put(webLineNumber,legacyLineNumber);
        }	
		return webLineListMap;
	}
	
	private String getCustomerOrderHeaderKey(YFSEnvironment env, String webConfirmationNumber) 
	{
		String customerOrderHeaderKey="";
		
		Document getOrderListInputDoc = YFCDocument.createDocument(XPXLiterals.E_ORDER).getDocument();
		getOrderListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_TYPE, XPXLiterals.CUSTOMER);
		getOrderListInputDoc.getDocumentElement().setAttribute("OrderTypeQryType","EQ");
		Element orderExtn = getOrderListInputDoc.createElement(XPXLiterals.E_EXTN);
		orderExtn.setAttribute(XPXLiterals.A_WEB_CONF_NUMBER, webConfirmationNumber);
		
		getOrderListInputDoc.getDocumentElement().appendChild(orderExtn);
		
		try
		{
			Document getOrderListOutputDoc = api.invoke(env, XPXLiterals.GET_ORDER_LIST_API, getOrderListInputDoc);
			Element orderElement = (Element) getOrderListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_ORDER).item(0);
			customerOrderHeaderKey = orderElement.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
			if(log.isDebugEnabled()){
				log.debug("The customer order header key is: "+customerOrderHeaderKey);
			}
						
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return customerOrderHeaderKey;
	}
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}

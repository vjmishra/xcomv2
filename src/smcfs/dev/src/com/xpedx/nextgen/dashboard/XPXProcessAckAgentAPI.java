package com.xpedx.nextgen.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXProcessAckAgentAPI extends YCPBaseAgent{

	
	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory
	.getLogger("com.xpedx.nextgen.log");
	String getOrderListTemplate = "global/template/api/getOrderList.XPXSendPOAckOnCreateOrderAPI.xml";
	public List getJobs(YFSEnvironment env, Document criteria,Document lastMessageCreated) throws Exception {
		// get YIFApi instance.
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		//form the input document
		Document inputOrderDocument = SCXmlUtil.createDocument("Order");
		Element inputOrderElement = inputOrderDocument.getDocumentElement();
		inputOrderElement.setAttribute("OrderType", "Customer");
		inputOrderElement.setAttribute("OrderTypeQryType", "LIKE");
		Element extnElement = inputOrderDocument.createElement("Extn");
		extnElement.setAttribute("ExtnIsProcessedFlag", "Y");
		extnElement.setAttribute("ExtnIsProcessedFlagQryType", "NE");
		inputOrderElement.appendChild(extnElement);
		Element orderByElement = inputOrderDocument.createElement("OrderBy");
		inputOrderElement.appendChild(orderByElement);
		Element attributeElement = inputOrderDocument.createElement("Attribute");
		attributeElement.setAttribute("Name", "OrderHeaderKey");
		attributeElement.setAttribute("Desc", "N");
		orderByElement.appendChild(attributeElement);
		String totalNoOfRecords = criteria.getDocumentElement().getAttribute("NumRecordsToBuffer");
		inputOrderElement.setAttribute("MaximumRecords", totalNoOfRecords);
		
		if(lastMessageCreated != null)
		{
			//create a complex query element
			Element complexQueryElement = inputOrderDocument.createElement("ComplexQuery");
			inputOrderElement.appendChild(complexQueryElement);
			//create an or element
			Element orElement = inputOrderDocument.createElement("Or");
			complexQueryElement.appendChild(orElement);
			//get the order header key in the last created message
			String orderHeaderKeyInLastMessage = lastMessageCreated.getDocumentElement().getAttribute("OrderHeaderKey");
			//create an expression element
			Element expElement = inputOrderDocument.createElement("Exp");
			orElement.appendChild(expElement);
			expElement.setAttribute("Name", "OrderHeaderKey");
			expElement.setAttribute("Value", orderHeaderKeyInLastMessage);
			expElement.setAttribute("QryType", "GT");
		}
		//add EntryType="B2B" to input xml 
		inputOrderElement.setAttribute("EntryType", "B2B");
		if(log.isDebugEnabled()){
			log.debug("The Input Document for getOrderList of XPXProcessAckAgentAPI is :"+SCXmlUtil.getString(inputOrderDocument));
		}
		//get the order list
		env.setApiTemplate("getOrderList", getOrderListTemplate);
		Document orderListDocument = api.invoke(env, "getOrderList", inputOrderDocument);
		env.clearApiTemplate("getOrderList");
		NodeList orderNodeList = orderListDocument.getElementsByTagName("Order");
		int orderLength = orderNodeList.getLength();
		List listOrders = SCXmlUtil.getChildrenList(orderListDocument.getDocumentElement());
		List listOfJobs = new ArrayList();
		for(int counter = 0;counter<listOrders.size();counter++)
		{
			Element OrderElement = (Element)listOrders.get(counter);
			Document newOrderDoc = YFCDocument.createDocument().getDocument();
			newOrderDoc.appendChild(newOrderDoc.importNode(OrderElement, true));
			newOrderDoc.renameNode(newOrderDoc.getDocumentElement(), newOrderDoc.getNamespaceURI(), "Order");
			
			listOfJobs.add(newOrderDoc);
			
		}
		return listOfJobs;
	}
	@Override
	public void executeJob(YFSEnvironment env, Document inputDoc) throws Exception {
		// TODO Auto-generated method stub
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		//XPXPOAckReProcessService
		try {
			if(log.isDebugEnabled()){
				log.debug("Input document for executing jobs of XPXProcessAckAgentAPI is : "+SCXmlUtil.getString(inputDoc));
			}
			api.executeFlow(env, "SendPOAckOnCreateOrder", inputDoc);
		} 
		/**@author asekhar-tw on 21-Jan-2011
		 * For error logging
		 */
		catch (NullPointerException ne) {
			log.error("NullPointerException: " + ne.getStackTrace());	
			prepareErrorObject(ne, XPXLiterals.PO_ACK_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inputDoc);			
			throw ne;
		} catch (YFSException yfe) {
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.PO_ACK_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inputDoc);
			throw yfe;	
			
		} catch (Exception e) {
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.PO_ACK_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inputDoc);
			throw e;
		}	
		
	}
	
	/**@author asekhar-tw on 21-Jan-2011
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

}

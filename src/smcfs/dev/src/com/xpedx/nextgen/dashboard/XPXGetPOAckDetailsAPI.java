package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXGetPOAckDetailsAPI implements YIFCustomApi{
	private static YIFApi api = null;
	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	String getOrderListTemplate = "global/template/api/getOrderList.XPXGetPOAckDetailsAPI.xml";
	
	public Document createReferenceOrder(YFSEnvironment env,Document inXML) throws Exception
	{
		api = YIFClientFactory.getInstance().getApi();
		ArrayList<String> orderArray = new ArrayList<String>();
		String orderNo = "";
		String etradingID = "";
		log.debug("The input XML for XPXGetPOAckDetailsAPI is :" + SCXmlUtil.getString(inXML));
		orderArray = getOrderDetails(env,inXML);
		orderNo = orderArray.get(0);
		etradingID = orderArray.get(1);
		//output document
		Document outputAckDocument = null;
		//Element outputAckElement = outputAckDocument.getDocumentElement();
		//get the order details
		env.setApiTemplate("getOrderList", getOrderListTemplate);
		Document orderListDoc = api.invoke(env, "getOrderList", inXML);
		if(log.isDebugEnabled()){
			log.debug("The output document after calling getOrderList is :"+SCXmlUtil.getString(orderListDoc));
		}
		env.clearApiTemplate("getOrderList");
		NodeList orderNodeList = orderListDoc.getElementsByTagName("Order");
		int orderLength = orderNodeList.getLength();
		if(orderLength != 0)
		{
			Element orderElement = (Element)orderNodeList.item(0);
			//form the document
			outputAckDocument = YFCDocument.createDocument().getDocument();
			outputAckDocument.appendChild(outputAckDocument.importNode(orderElement, true));
			outputAckDocument.renameNode(outputAckDocument.getDocumentElement(), outputAckDocument.getNamespaceURI(), "Order");
		}
					
		//get the reference order detail
		//for reference order after successful creation of sterling order
		Document inputCustomRefDoc = YFCDocument.createDocument("XPXRefOrderHdr").getDocument();
		Element inputCustomRefElement = inputCustomRefDoc.getDocumentElement();
		inputCustomRefElement.setAttribute("EtradingID", etradingID);
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
		Node customReferenceNode = outputAckDocument.importNode(customRefElement, true);
		Element customReferenceElement = (Element)customReferenceNode;
		outputAckDocument.getDocumentElement().appendChild(customReferenceElement);
		
		return outputAckDocument;
	}
	
	private ArrayList<String> getOrderDetails(YFSEnvironment env, Document inputXML) throws YFSException, RemoteException
	{
		ArrayList<String> orderArray = new ArrayList<String>();
		//ExtnETradingID
		String etradingID = "";
		String orderNo = "";
		Document orderListDoc = api.invoke(env, "getOrderList", inputXML);
		NodeList orderNodeList = orderListDoc.getElementsByTagName("Order");
		int orderLength = orderNodeList.getLength();
		if(orderLength != 0)
		{
			Element orderElement = (Element)orderNodeList.item(0);
			orderNo = orderElement.getAttribute("OrderNo");
			etradingID = SCXmlUtil.getXpathAttribute(orderElement, "./Extn/@ExtnETradingID");
			orderArray.add(orderNo);
			orderArray.add(etradingID);
		}
		return orderArray;
	}

}

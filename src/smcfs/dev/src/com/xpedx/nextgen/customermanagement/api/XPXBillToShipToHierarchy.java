package com.xpedx.nextgen.customermanagement.api;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;

/*
 * Input
 * <Customer CustomerKey=""/>
 * 
 */
public class XPXBillToShipToHierarchy implements YIFCustomApi{

	private static YIFApi api = null;
	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Method to get the billto shipto hierarchy
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document invokeGetBillToShipToOrganizationList(YFSEnvironment env,Document inXML) throws Exception
	{
		String customerKey = inXML.getDocumentElement().getAttribute("CustomerKey");
		
		//form the input xml
		Document outputDoc = YFCDocument.createDocument("BillToList").getDocument();
		Element outputElement = outputDoc.getDocumentElement();
		api = YIFClientFactory.getInstance().getApi();
		//if(customerKey != "")
		if(!YFCCommon.isVoid(customerKey))
		{
		getBillToList(env, customerKey, outputDoc);
		}
		return outputDoc;
		
		
	}

	/**
	 * Method to get all the bill to  from the level customer has logged in.
	 * @param env
	 * @param customerKey
	 * @param outputDoc
	 * @throws RemoteException
	 */
	private void getBillToList(YFSEnvironment env, String customerKey,
			Document outputDoc) throws RemoteException {
		//form the input customer xml
		Document inputCustomerDoc = YFCDocument.createDocument("Customer").getDocument();
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("ParentCustomerKey", customerKey);
		Element extnElement = inputCustomerDoc.createElement("Extn");
		extnElement.setAttribute("ExtnSuffixTypeQryType", "LIKE");
		extnElement.setAttribute("ExtnSuffixType", "B");
		inputCustomerElement.appendChild(extnElement);
		//get bill to customer list
		Document billtoListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		NodeList btNodeList = billtoListDoc.getElementsByTagName("Customer");
		int billtoLength = btNodeList.getLength();
		if(billtoLength != 0)
		{
			getShipToList(env, outputDoc, btNodeList);
		}
	}

	/**
	 * Method to get all the ship to for the bill to
	 * @param env
	 * @param outputDoc
	 * @param btNodeList
	 * @throws RemoteException
	 */
	private void getShipToList(YFSEnvironment env, Document outputDoc,
			 NodeList btNodeList)
			throws RemoteException {
		Element outputElement = outputDoc.getDocumentElement();
		int billtoLength = btNodeList.getLength();
		for(int Counter = 0;Counter < billtoLength;Counter++)
		{
			Element btElement = (Element)btNodeList.item(Counter);
			Element billToElement = outputDoc.createElement("BillTo");
			billToElement.setAttribute("BillToID", btElement.getAttribute("CustomerID"));
			outputElement.appendChild(billToElement);
			Element shiptoListElement = outputDoc.createElement("ShipToList");
			billToElement.appendChild(shiptoListElement);
			//get the shipto list
			Document inputShipToDoc = YFCDocument.createDocument("Customer").getDocument();
			Element inputShipToElement = inputShipToDoc.getDocumentElement();
			inputShipToElement.setAttribute("ParentCustomerKey", btElement.getAttribute("CustomerKey"));
			Element extnShipElement = inputShipToDoc.createElement("Extn");
			extnShipElement.setAttribute("ExtnSuffixTypeQryType", "LIKE");
			extnShipElement.setAttribute("ExtnSuffixType", "S");
			inputShipToElement.appendChild(extnShipElement);
			Document shiptoListDoc = api.invoke(env, "getCustomerList", inputShipToDoc);
			NodeList stNodeList = shiptoListDoc.getElementsByTagName("Customer");
			int stLength = stNodeList.getLength();
			if(stLength != 0)
			{
				for(int sCounter = 0;sCounter<stLength;sCounter++)
				{
					Element stElement = (Element)stNodeList.item(sCounter);
					Element shiptoElement = outputDoc.createElement("ShipTo");
					shiptoElement.setAttribute("ShipToID", stElement.getAttribute("CustomerID"));
					shiptoListElement.appendChild(shiptoElement);
				}
				
				
			}
		}
	}
}

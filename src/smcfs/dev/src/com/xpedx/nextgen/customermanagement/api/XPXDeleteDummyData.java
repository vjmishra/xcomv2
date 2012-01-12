package com.xpedx.nextgen.customermanagement.api;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.sterlingcommerce.baseutil.SCXmlUtil;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXDeleteDummyData {
	private static YIFApi api = null;
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	
	
	public void invokeModifyCustomer(YFSEnvironment env,Document inXML) throws Exception
	{
		//get all MAX customers
		//form the input xml
		try
		{
		api = YIFClientFactory.getInstance().getApi();
		Document customerDocument = YFCDocument.createDocument("Customer").getDocument();
		Element customerElement = customerDocument.getDocumentElement();
		customerElement.setAttribute("CustomerIDQryType", "LIKE");
		customerElement.setAttribute("OrganizationCode", "xpedx");
		customerElement.setAttribute("CustomerID", "N3");
		Document outCustomerListDoc = api.invoke(env, "getCustomerList",customerDocument);
		NodeList listNode = outCustomerListDoc.getElementsByTagName("Customer");
		for(int i=0;i<listNode.getLength();i++)
		{
			Element delCustomerElement = (Element)listNode.item(i);
			//form input for manage
			String customerId = delCustomerElement.getAttribute("CustomerID");
			Document inputDoc = YFCDocument.createDocument("Customer").getDocument();
			Element inputElement = inputDoc.getDocumentElement();
			inputElement.setAttribute("CustomerID", delCustomerElement.getAttribute("CustomerID"));
			inputElement.setAttribute("OrganizationCode", "xpedx");
			inputElement.setAttribute("BuyerOrganizationCode", delCustomerElement.getAttribute("BuyerOrganizationCode"));
			inputElement.setAttribute("CustomerKey", delCustomerElement.getAttribute("CustomerKey"));
			inputElement.setAttribute("Operation", "Delete");
			
			if(customerId.contains("MSAP"))
			{
				api.invoke(env, "manageCustomer", inputDoc);
			}
		}
		}
		catch(Exception e)
		{
		}
	}

}

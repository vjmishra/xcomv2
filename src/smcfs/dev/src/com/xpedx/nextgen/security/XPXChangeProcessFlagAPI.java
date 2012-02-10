package com.xpedx.nextgen.security;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXChangeProcessFlagAPI implements YIFCustomApi{
	
	private static YIFApi api = null;
	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	String contactTemplate = "global/template/api/getCustomerContactList.XPXChangeProcessFlagAPI.xml";
	static {
		
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}
	
	public Document changeProcessFlag(YFSEnvironment env, Document inputXML)  throws Exception
	{
		//form the input
		Document inputContactDoc = YFCDocument.createDocument("CustomerContact").getDocument();
		Element inputContactElement = inputContactDoc.getDocumentElement();
		Element extnContactElement = inputContactDoc.createElement("Extn");
		extnContactElement.setAttribute("ExtnProcessedFlag", "Y");
		extnContactElement.setAttribute("ExtnProcessedFlagQryType", "LIKE");
		inputContactElement.appendChild(extnContactElement);
		
		//get customerContactlist
		env.setApiTemplate("getCustomerContactList", contactTemplate);
		Document outContactListDoc = api.invoke(env, "getCustomerContactList", inputContactDoc);
		env.clearApiTemplate("getCustomerContactList");
		NodeList contactNodeList = outContactListDoc.getElementsByTagName("CustomerContact");
		int contactLength = contactNodeList.getLength();
		if(contactLength != 0)
		{
			for(int ccCounter=0;ccCounter<contactLength;ccCounter++)
			{
				Element contactElement = (Element)contactNodeList.item(ccCounter);
				Document changeContactDoc = YFCDocument.createDocument("Customer").getDocument();
				Element changeCustomerElement = changeContactDoc.getDocumentElement();
				changeCustomerElement.setAttribute("Action", "Modify");
				changeCustomerElement.setAttribute("CustomerKey", contactElement.getAttribute("CustomerKey"));
				Element customerContactListElement = changeContactDoc.createElement("CustomerContactList");
				changeCustomerElement.appendChild(customerContactListElement);
				Element customerContactElement = changeContactDoc.createElement("CustomerContact");
				customerContactElement.setAttribute("CustomerContactID", contactElement.getAttribute("CustomerContactID"));
				customerContactElement.setAttribute("UserID", contactElement.getAttribute("UserID"));
				customerContactListElement.appendChild(customerContactElement);
				Element extnElement = changeContactDoc.createElement("Extn");
				extnElement.setAttribute("ExtnProcessedFlag", "N");
				customerContactElement.appendChild(extnElement);
				if(log.isDebugEnabled()){
				log.debug("changeContactDoc:"+SCXmlUtil.getString(changeContactDoc));
				}
				Document outDoc = api.invoke(env, "manageCustomer", changeContactDoc);
			}
		}
		//form the input 
		Document inputCustomerDoc = YFCDocument.createDocument("Customer").getDocument();
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		Element extnCustomerElement = inputCustomerDoc.createElement("Extn");
		extnCustomerElement.setAttribute("ExtnProcessedFlag", "Y");
		extnCustomerElement.setAttribute("ExtnProcessedFlagQryType", "LIKE");
		inputCustomerElement.appendChild(extnCustomerElement);
		//get customerlist
		Document outCustomerListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		NodeList customerNodeList = outCustomerListDoc.getElementsByTagName("Customer");
		int customerLength = customerNodeList.getLength();
		if(customerLength != 0)
		{
			for(int cCounter=0;cCounter<customerLength;cCounter++)
			{
				Element customerElement = (Element)customerNodeList.item(cCounter);
				Document changeCustomerDoc = YFCDocument.createDocument("Customer").getDocument();
				Element changeCustomerElement = changeCustomerDoc.getDocumentElement();
				changeCustomerElement.setAttribute("Action", "Modify");
				changeCustomerElement.setAttribute("CustomerKey", customerElement.getAttribute("CustomerKey"));
				Element extnElement = changeCustomerDoc.createElement("Extn");
				extnElement.setAttribute("ExtnProcessedFlag", "N");
				changeCustomerElement.appendChild(extnElement);
				if(log.isDebugEnabled()){
				log.debug("changeCustomerDoc:"+SCXmlUtil.getString(changeCustomerDoc));
				}
				Document outDoc = api.invoke(env, "manageCustomer", changeCustomerDoc);
			}
		}
		return inputXML;
	}

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
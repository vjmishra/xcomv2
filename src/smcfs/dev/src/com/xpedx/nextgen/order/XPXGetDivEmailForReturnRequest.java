package com.xpedx.nextgen.order;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXGetDivEmailForReturnRequest implements YIFCustomApi {

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	
	public Document invoke(YFSEnvironment env, Document inXML)
	throws Exception
	{
		YFCDocument outDoc = YFCDocument.createDocument("EMail");
		YFCElement eMail = outDoc.getDocumentElement();
		
		YIFApi api = YIFClientFactory.getInstance().getApi();
		//we get the BillToID from the input
		
		YFCDocument getCustListTemplate = YFCDocument.createDocument("CustomerList");
		YFCElement eCustomerListTemplate = getCustListTemplate.getDocumentElement();
		YFCElement eCustomerTemplate = getCustListTemplate.createElement("Customer"); 
		eCustomerListTemplate.appendChild(eCustomerTemplate);
		
		eCustomerTemplate.setAttribute("CustomerID", "");
		eCustomerTemplate.setAttribute("OrganizationCode", "");
		
		YFCElement eCustomerExtnTemplate = getCustListTemplate.createElement("Extn"); 
		eCustomerTemplate.appendChild(eCustomerExtnTemplate);
		
		eCustomerExtnTemplate.setAttribute("ExtnCustOrderBranch", "");
		eCustomerExtnTemplate.setAttribute("ExtnOrigEnvironmentCode", "");
		
		env.setApiTemplate("getCustomerList", getCustListTemplate.getDocument());
		Document custListXML = api.invoke(env, "getCustomerList", inXML);
		env.clearApiTemplate("getCustomerList");
		
		Element eCustomerListOut = custListXML.getDocumentElement();
		Element eExtn = (Element)eCustomerListOut.getElementsByTagName("Extn").item(0);
		
		String sCustomerOrdBranch = eExtn.getAttribute("ExtnCustOrderBranch");
		String sCustomerEnvCode = eExtn.getAttribute("ExtnOrigEnvironmentCode");
		
		if(null == sCustomerOrdBranch || "".equalsIgnoreCase(sCustomerOrdBranch.trim()) || null == sCustomerEnvCode || "".equalsIgnoreCase(sCustomerEnvCode.trim())  )
				{
					//no associated customer order branch, return blank email id
					eMail.setAttribute("EMailID", "");
				}
		else
		{
			sCustomerOrdBranch = sCustomerOrdBranch+"_"+sCustomerEnvCode;
			YFCDocument getOrgListInDoc = YFCDocument.createDocument("Organization");
			YFCElement eOrgList = getOrgListInDoc.getDocumentElement();
			eOrgList.setAttribute("OrganizationCode", sCustomerOrdBranch);
			
			YFCDocument getOrgListTemplate = YFCDocument.createDocument("OrganizationList");
			YFCElement eOrgListTemplate = getOrgListTemplate.getDocumentElement();
			YFCElement eOrgTemplate = getOrgListTemplate.createElement("Organization");
			eOrgListTemplate.appendChild(eOrgTemplate);
			
			eOrgTemplate.setAttribute("OrganizationCode", "");
			
			YFCElement eCorpPersonInfoTemplate = getOrgListTemplate.createElement("CorporatePersonInfo");
			eOrgTemplate.appendChild(eCorpPersonInfoTemplate);
			
			eOrgList.setAttribute("EMailID", "");
			
			env.setApiTemplate("getOrganizationList", getOrgListTemplate.getDocument());
			Document getOrgListOut = api.invoke(env, "getOrganizationList", getOrgListInDoc.getDocument());
			env.clearApiTemplate("getOrganizationList");
			
			Element eOrgListOut = getOrgListOut.getDocumentElement();
			Element eCorporatePersonInfoOut = (Element)eOrgListOut.getElementsByTagName("CorporatePersonInfo").item(0);
			
			String sDivEMailID = eCorporatePersonInfoOut.getAttribute("EMailID");
			
			eMail.setAttribute("EMailID", sDivEMailID);
		}
		
		
		
		
		return outDoc.getDocument();
	}

}

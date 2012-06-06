package com.xpedx.nextgen.manageuser.agent.nontaskq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCException;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXBulkPasswordResetAgent extends YCPBaseAgent {

	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	private static Set<String> customerContactKeys = new HashSet<String>();
	
	private static HashMap customerContactUserContactKeys = new HashMap();
	private static MultiValueMap customerContactDetailsForAlertCreation = new MultiValueMap();
	
	@Override
	public List getJobs(YFSEnvironment env, Document criteria, Document lastMessageCreated) throws Exception {
		List listOfJobs = new ArrayList();
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		
		Document inputCustomerContactDoc = SCXmlUtil.createFromString("<CustomerContact><OrderBy><Attribute Name='CustomerContactKey' Desc='N' /></OrderBy></CustomerContact>");
		Element inputCustomerContactElement = inputCustomerContactDoc.getDocumentElement();

		String totalNumberOfRecords = getCriteriaParamValue(criteria,"NumRecordsToBuffer");
			inputCustomerContactElement.setAttribute("MaximumRecords",totalNumberOfRecords);
		
		log.debug("lastMessageCreated doc is: " + SCXmlUtil.getString(lastMessageCreated));
		if (lastMessageCreated != null) 
		{
			// get the CustomerContact which have failed
			//this.formInputToContactListWithLastMessageCreated(lastMessageCreated, inputCustomerContactDoc, inputCustomerContactElement);
			
			String customerContactKeyInLastCreatedMessage = "";
			Element lastCCElement = null;
			NodeList ccNodeList = lastMessageCreated.getElementsByTagName("ResetPassword");
			int ccLength = ccNodeList.getLength();
			if(ccLength != 0)
			{
				lastCCElement = (Element)ccNodeList.item(ccLength-1);
			
			String userKey = lastCCElement.getAttribute("UserKey");
			
			customerContactKeyInLastCreatedMessage=(String) customerContactUserContactKeys.get(userKey);
			inputCustomerContactDoc.getDocumentElement().setAttribute("CustomerContactKey", customerContactKeyInLastCreatedMessage);
			inputCustomerContactDoc.getDocumentElement().setAttribute("CustomerContactKeyQryType", "GT");
			}
		}
		log.debug("inputCustomerDoc"+ SCXmlUtil.getString(inputCustomerContactDoc));
		// template for customer contact list
		Document customerContactListTemplateDoc = SCXmlUtil.createFromString("<CustomerContactList><CustomerContact><User/></CustomerContact></CustomerContactList>");
		env.setApiTemplate("getCustomerContactList",customerContactListTemplateDoc);
		Document docCustContacts = api.invoke(env, "getCustomerContactList", inputCustomerContactDoc);
		env.clearApiTemplate("getCustomerContactList");
		
		if(docCustContacts.getDocumentElement().getElementsByTagName("CustomerContact").getLength()>0)
		{
			NodeList custContactList = docCustContacts.getDocumentElement().getElementsByTagName("CustomerContact");
			
			for(int i=0; i<custContactList.getLength(); i++)
			{
				Element customerContactElement = (Element) custContactList.item(i);
				
				Element userElement = (Element) customerContactElement.getElementsByTagName("User").item(0);
				
				customerContactUserContactKeys.put(userElement.getAttribute("UserKey"), customerContactElement.getAttribute("CustomerContactKey"));
				
				
				customerContactDetailsForAlertCreation.put(userElement.getAttribute("UserKey"), customerContactElement.getAttribute("CustomerContactID"));
				customerContactDetailsForAlertCreation.put(userElement.getAttribute("UserKey"), userElement.getAttribute("EnterpriseCode"));
				customerContactDetailsForAlertCreation.put(userElement.getAttribute("UserKey"), userElement.getAttribute("OrganizationKey"));
				
			}
		}
		
		this.updateListOfJobs(criteria, listOfJobs, docCustContacts);
		
		return listOfJobs;
	}

	/**
	 * @param criteria
	 * @param parameterName 
	 * @return
	 */
	private String getCriteriaParamValue(Document criteria, String parameterName) {
		String parameterValue = criteria.getDocumentElement().getAttribute(parameterName);
		if("NumRecordsToBuffer".equals(parameterName)){
			try{
				Integer.parseInt(parameterValue);
			} catch (Exception e) {
				YFCException ex = new YFCException(e);
				ex.setErrorDescription("Invalid Number! NumRecordsToBuffer");
				ex.setAttribute(parameterName, parameterValue);
			}
		}
		return parameterValue;
	}
	private void formInputToContactListWithLastMessageCreated(Document lastMessageCreated, Document inputCustomerContactDoc, Element inputCustomerElement) {
		Element complexQueryElement = inputCustomerContactDoc.createElement("ComplexQuery");
		/*inputCustomerElement.appendChild(complexQueryElement);
		Element orElement = inputCustomerContactDoc.createElement("or");*/
		/*for (String customerContactKey : customerContactKeys)
		{
			complexQueryElement.appendChild(orElement);
			Element expElement = inputCustomerContactDoc.createElement("Exp");
			expElement.setAttribute("Name", "CustomerContactKey");
			expElement.setAttribute("Value", customerContactKey);

			orElement.appendChild(expElement);
		}*/

		String customerContactKeyInLastCreatedMessage = "";
		Element lastCCElement = null;
		NodeList ccNodeList = lastMessageCreated.getElementsByTagName("ResetPassword");
		int ccLength = ccNodeList.getLength();
		if(ccLength != 0)
		{
			lastCCElement = (Element)ccNodeList.item(ccLength-1);
		
		String userKey = lastCCElement.getAttribute("UserKey");
		
		customerContactKeyInLastCreatedMessage=(String) customerContactUserContactKeys.get(userKey);
		
		log.debug("customerKeyInLastCreatedMessage"+ customerContactKeyInLastCreatedMessage);
		Element expNextElement = inputCustomerContactDoc.createElement("Exp");
		expNextElement.setAttribute("Name", "CustomerContactKey");
		expNextElement.setAttribute("Value",customerContactKeyInLastCreatedMessage);
		expNextElement.setAttribute("QryType", "GT");
		complexQueryElement.appendChild(expNextElement);
		}
	}
	
	private void updateListOfJobs(Document criteria, List listOfJobs,
			Document docCustContacts) {
		
		Element eleContact = null;
		List contacts = SCXmlUtil.getChildrenList(docCustContacts.getDocumentElement());
		
		for (int counter = 0; counter < contacts.size(); counter++)
		{
			Document docOrderNew = SCXmlUtil.createDocument("ResetPassword");
			eleContact = (Element) contacts.get(counter);
			
			Element userElement = (Element) eleContact.getElementsByTagName(XPXLiterals.E_USER).item(0);
			
			
				docOrderNew.getDocumentElement().setAttribute("ResetType", "Email");
				docOrderNew.getDocumentElement().setAttribute("UserKey", userElement.getAttribute("UserKey"));
				
                 //Add the Order to listOfJobs to be processed
				listOfJobs.add(docOrderNew);
			
			
			//SCXmlUtil.mergeAttributes(eleOrder, docOrderNew .getDocumentElement(), false);

			
			
				
		}
		return ;
	}

	@Override
	public void executeJob(YFSEnvironment env, Document inputDoc) throws Exception {
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		try {
			log.debug("The input document to requestResetPassword is: "+SCXmlUtil.getString(inputDoc));
			api.invoke(env, "requestPasswordReset", inputDoc);
		} catch (Exception e) {
			e.printStackTrace();
			
			String customerContactId = "";
			String enterpriseCode = "";
			String customerOrgainzationKey = "";
			
			//String customerContactKey = (String) customerContactUserContactKeys.get(inputDoc.getDocumentElement().getAttribute("UserKey"));
			//customerContactKeys.add(SCXmlUtil.getAttribute(inputDoc.getDocumentElement(), "CustomerContactKey"));
			
			YFCDocument inputForAlertCreationDoc = YFCDocument.createDocument("Order");

			if (customerContactDetailsForAlertCreation.containsKey(inputDoc.getDocumentElement().getAttribute("UserKey")))
			{
				ArrayList arr = (ArrayList) customerContactDetailsForAlertCreation.getCollection(inputDoc.getDocumentElement().getAttribute("UserKey"));

				for(int j=0; j <arr.size();j++)
				{
					if( j==0)
					{
						customerContactId = (String)arr.get(j);
					}
					if( j==1 )
					{
						enterpriseCode = (String)arr.get(j);
					}
					if(j ==2)
					{
						customerOrgainzationKey = (String)arr.get(j);
					}
					
				}    					
			}
			
			inputForAlertCreationDoc.getDocumentElement().setAttribute("CustomerContactId", customerContactId);
			inputForAlertCreationDoc.getDocumentElement().setAttribute("EnterpriseCode", enterpriseCode);
			inputForAlertCreationDoc.getDocumentElement().setAttribute("CustomerOrganization", customerOrgainzationKey);
			
			api.executeFlow(env, "XPXCreateAlertForPasswordResetService", inputForAlertCreationDoc.getDocument());
			
			//customerContactKeys.add(customerContactKey);
		}

	}

}

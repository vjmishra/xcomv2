package com.xpedx.nextgen.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXRRDSecurityFileGenerationAgent extends YCPBaseAgent {

	private static YFCLogCategory log = YFCLogCategory
			.instance(XPXRRDSecurityFileGenerationAgent.class);

	private static Set<String> customerContactKeys = new HashSet<String>();
	private static Set<String> customerKeys = new HashSet<String>();

	public List getJobs(YFSEnvironment env, Document criteria,
			Document lastMessageCreated) throws Exception {
		
		List listOfJobs = new ArrayList();
		int userCount = 0;
		int invoiceHdrLength = 0;
		// get YIFApi instance.
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		// form the document as input for customerContactList
		Document inputCustomerDoc = YFCDocument.createDocument("CustomerContact").getDocument();
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		Element orderByElement = inputCustomerDoc.createElement("OrderBy");
		inputCustomerElement.appendChild(orderByElement);
		Element attributeElement = inputCustomerDoc.createElement("Attribute");
		attributeElement.setAttribute("Name", "CustomerContactKey");
		attributeElement.setAttribute("Desc", "N");
		orderByElement.appendChild(attributeElement);
		String totalNumberOfRecords = criteria.getDocumentElement().getAttribute("NumRecordsToBuffer");
		inputCustomerElement.setAttribute("MaximumRecords",totalNumberOfRecords);
			
		if ((lastMessageCreated != null)&&(lastMessageCreated.getDocumentElement().getNodeName().equals("CustomerContactList"))) 
		{
			// get the CustomerContact which have failed
			//complex query to get the list in ascending order and greater than last message
			formInputToContactListWithLastMessageCreated(lastMessageCreated,
					inputCustomerDoc, inputCustomerElement);
			
			// template for customer contact list
			Document customerContactListTemplateDoc = YFCDocument.createDocument("CustomerContactList").getDocument();
			Element customerContactListTemplateElement = customerContactListTemplateDoc.getDocumentElement();
			Element customerContactTemplateElement = customerContactListTemplateDoc.createElement("CustomerContact");
			customerContactListTemplateElement.appendChild(customerContactTemplateElement);
			Element customerTemplateElement = customerContactListTemplateDoc.createElement("Customer");
			customerContactTemplateElement.appendChild(customerTemplateElement);
			env.setApiTemplate("getCustomerContactList",customerContactListTemplateDoc);
			if(log.isDebugEnabled())
			{
				log.debug("The input document for getCustomerContactList in XPXRRDSecurityFileGenerationAgent is  "+SCXmlUtil.getString(inputCustomerDoc));
			}
			// get the invoice list
			Document outputInvoiceHeaderListDoc = api.invoke(env, "getCustomerContactList", inputCustomerDoc);
			env.clearApiTemplate("getCustomerContactList");
			NodeList invoiceHdrNodeList = outputInvoiceHeaderListDoc.getElementsByTagName("CustomerContact");
			invoiceHdrLength = invoiceHdrNodeList.getLength();
				
			//this is to populate the arraylist with the customercontact list jobs
			userCount = formListOfJobsForContact(
					listOfJobs, userCount, outputInvoiceHeaderListDoc);
		}

			if(lastMessageCreated == null)
			{
				int invoiceHdrLength1 = 0;
				
				// template for customer contact list
				Document customerContactListTemplateDoc1 = YFCDocument.createDocument("CustomerContactList").getDocument();
				Element customerContactListTemplateElement1 = customerContactListTemplateDoc1.getDocumentElement();
				Element customerContactTemplateElement1 = customerContactListTemplateDoc1.createElement("CustomerContact");
				customerContactListTemplateElement1.appendChild(customerContactTemplateElement1);
				Element customerTemplateElement1 = customerContactListTemplateDoc1.createElement("Customer");
				customerContactTemplateElement1.appendChild(customerTemplateElement1);
				env.setApiTemplate("getCustomerContactList",customerContactListTemplateDoc1);
				if(log.isDebugEnabled())
				{
					log.debug("The input document for getCustomerContactList in XPXRRDSecurityFileGenerationAgent is  "+SCXmlUtil.getString(inputCustomerDoc));
				}
				// get the invoice list
				Document outputInvoiceHeaderListDoc1 = api.invoke(env, "getCustomerContactList", inputCustomerDoc);
				env.clearApiTemplate("getCustomerContactList");
				NodeList invoiceHdrNodeList1 = outputInvoiceHeaderListDoc1.getElementsByTagName("CustomerContact");
				invoiceHdrLength1 = invoiceHdrNodeList1.getLength();
					
				//this is to populate the arraylist with the customercontact list jobs
				userCount = formListOfJobsForContact(
						listOfJobs, userCount, outputInvoiceHeaderListDoc1);
			}

		
		
		//for getting the CSR details
		boolean a = (invoiceHdrLength == 0);
		//this is to check that the customer contact job is over and customer job has started
		if((invoiceHdrLength == 0)&& !(lastMessageCreated == null))
		{
			if(lastMessageCreated.getDocumentElement().getNodeName().equals("CustomerContactList")){
				lastMessageCreated = null;
			}
			
			//gte the customerlist
			//form the input
			Document inputCsrDoc = YFCDocument.createDocument("Customer").getDocument();
			Element inputCsrElement = inputCsrDoc.getDocumentElement();
			Element orderByElementCsr = inputCsrDoc.createElement("OrderBy");
			inputCustomerElement.appendChild(orderByElement);
			Element attributeElementCsr = inputCsrDoc.createElement("Attribute");
			attributeElement.setAttribute("Name", "CustomerKey");
			attributeElement.setAttribute("Desc", "N");
			orderByElementCsr.appendChild(attributeElementCsr);
			String totalNumberOfRecords1 = criteria.getDocumentElement().getAttribute("NumRecordsToBuffer");
			inputCsrElement.setAttribute("MaximumRecords",totalNumberOfRecords1);
			if (lastMessageCreated != null) 
			{
				//form the input to get the customer list in ascending order 
				//with customerkey greater than the last message
				formInputToCustomerList(lastMessageCreated, inputCsrDoc,
						inputCsrElement);
			
			}
			
			// template for customer contact list
			Document customerListTemplateDoc = YFCDocument.createDocument("CustomerList").getDocument();
			Element customerListTemplateElement = customerListTemplateDoc.getDocumentElement();
			Element customerTemplateElement1 = customerListTemplateDoc.createElement("Customer");
			customerListTemplateElement.appendChild(customerTemplateElement1);
			env.setApiTemplate("getCustomerList",customerListTemplateDoc);
			if(log.isDebugEnabled())
			{
				log.debug("The input document for getCustomerList in XPXRRDSecurityFileGenerationAgent is  "+SCXmlUtil.getString(inputCsrDoc));
			}
			// get the invoice list
			Document outputCustomerListDoc = api.invoke(env, "getCustomerList", inputCsrDoc);
			env.clearApiTemplate("getCustomerList");
			NodeList customerNodeList = outputCustomerListDoc.getElementsByTagName("Customer");
			
			//adding the customers to the array list
			formListOfJobsForCustomer(listOfJobs, userCount,
					outputCustomerListDoc);
		}
		return listOfJobs;
	}

	/**
	 * Method to for the arraylist of jobs for customer
	 * @param listOfJobs
	 * @param userCount
	 * @param outputCustomerListDoc
	 */
	private void formListOfJobsForCustomer(List listOfJobs, int userCount,
			Document outputCustomerListDoc) {
		int customerCount = 0;
		Document customerListDoc = null;
		customerListDoc = YFCDocument.createDocument("CustomerList").getDocument();
		Element customerListElement = customerListDoc.getDocumentElement();
		List listCustomers = SCXmlUtil.getChildrenList(outputCustomerListDoc.getDocumentElement());
		//List customerJobs = new ArrayList();
		for(int cCounter=0;cCounter<listCustomers.size();cCounter++)
		{
			if(cCounter%5 == 0)
			{
				customerListDoc = YFCDocument.createDocument("CustomerList").getDocument();
				customerListElement = customerListDoc.getDocumentElement();
			}
			Element customerElement = (Element) listCustomers.get(cCounter);

			Node contactNode = customerListDoc.importNode(customerElement, true);
			customerListElement.appendChild((Element) contactNode);
			userCount++;

			if ((userCount == 5) || (cCounter == listCustomers.size() - 1))
			{
				listOfJobs.add(customerListDoc);
				userCount = 0;
			
			}
		}
	}

	/**
	 * Method to form the input to customerlist api
	 * @param lastMessageCreated
	 * @param inputCsrDoc
	 * @param inputCsrElement
	 */
	private void formInputToCustomerList(Document lastMessageCreated,
			Document inputCsrDoc, Element inputCsrElement) {
		// get the CustomerContact which have failed
		Element complexQueryElement1 = inputCsrDoc.createElement("ComplexQuery");
		inputCsrElement.appendChild(complexQueryElement1);
		Element orElement1 = inputCsrDoc.createElement("or");
		for (String customerKey : customerKeys)
		{
			complexQueryElement1.appendChild(orElement1);
			Element expElement = inputCsrDoc.createElement("Exp");
			expElement.setAttribute("Name", "CustomerKey");
			expElement.setAttribute("Value", customerKey);

			orElement1.appendChild(expElement);
		}

		
		String customerKeyInLastCreatedMessage = "";
		Element lastCElement = null;
		NodeList cNodeList = lastMessageCreated.getElementsByTagName("Customer");
		int cLength = cNodeList.getLength();
		if(cLength != 0)
		{
			lastCElement = (Element)cNodeList.item(cLength-1);
			customerKeyInLastCreatedMessage = lastCElement.getAttribute("CustomerKey");
		if(log.isDebugEnabled()){
		log.debug("customerKeyInLastCreatedMessage"+ customerKeyInLastCreatedMessage);
		}
		Element expNextElement = inputCsrDoc.createElement("Exp");
		expNextElement.setAttribute("Name", "CustomerKey");
		expNextElement.setAttribute("Value",customerKeyInLastCreatedMessage);
		expNextElement.setAttribute("QryType", "GT");
		complexQueryElement1.appendChild(expNextElement);
		}
	}

	/**
	 * Method to form the arraylist of jobs for customerContact
	 * @param listOfJobs
	 * @param userCount
	 * @param outputInvoiceHeaderListDoc
	 * @return
	 */
	private int formListOfJobsForContact(List listOfJobs,
			int userCount, Document outputInvoiceHeaderListDoc) {
		Document userListDoc = null;
		userListDoc = YFCDocument.createDocument("CustomerContactList").getDocument();
		Element userListElement = userListDoc.getDocumentElement();
		List listOrders = SCXmlUtil.getChildrenList(outputInvoiceHeaderListDoc.getDocumentElement());
		//List listOfJobs = new ArrayList();
		for (int counter = 0; counter < listOrders.size(); counter++)
		{
				if(counter%5 == 0)
				{
					userListDoc = YFCDocument.createDocument("CustomerContactList").getDocument();
					userListElement = userListDoc.getDocumentElement();
				}
				Element customerElement = (Element) listOrders.get(counter);

				Node contactNode = userListDoc.importNode(customerElement, true);
				userListElement.appendChild((Element) contactNode);
				userCount++;

				if ((userCount == 5) || (counter == listOrders.size() - 1)) 
				{
					listOfJobs.add(userListDoc);
					userCount = 0;
				
				}
		
		}
		return userCount;
	}

	/**
	 * @param lastMessageCreated
	 * @param inputCustomerDoc
	 * @param inputCustomerElement
	 */
	private void formInputToContactListWithLastMessageCreated(
			Document lastMessageCreated, Document inputCustomerDoc,
			Element inputCustomerElement) {
		Element complexQueryElement = inputCustomerDoc.createElement("ComplexQuery");
		inputCustomerElement.appendChild(complexQueryElement);
		Element orElement = inputCustomerDoc.createElement("or");
		for (String customerContactKey : customerContactKeys)
		{
			complexQueryElement.appendChild(orElement);
			Element expElement = inputCustomerDoc.createElement("Exp");
			expElement.setAttribute("Name", "CustomerContactKey");
			expElement.setAttribute("Value", customerContactKey);

			orElement.appendChild(expElement);
		}

		String customerContactKeyInLastCreatedMessage = "";
		Element lastCCElement = null;
		NodeList ccNodeList = lastMessageCreated.getElementsByTagName("CustomerContact");
		int ccLength = ccNodeList.getLength();
		if(ccLength != 0)
		{
			lastCCElement = (Element)ccNodeList.item(ccLength-1);
		
		customerContactKeyInLastCreatedMessage = lastCCElement.getAttribute("CustomerContactKey");
		if(log.isDebugEnabled()){
		log.debug("customerKeyInLastCreatedMessage :"+ customerContactKeyInLastCreatedMessage);
		}
		Element expNextElement = inputCustomerDoc.createElement("Exp");
		expNextElement.setAttribute("Name", "CustomerContactKey");
		expNextElement.setAttribute("Value",customerContactKeyInLastCreatedMessage);
		expNextElement.setAttribute("QryType", "GT");
		complexQueryElement.appendChild(expNextElement);
		}
	}

	@Override
	public void executeJob(YFSEnvironment env, Document inputDoc)
			throws Exception {
		// TODO Auto-generated method stub
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		try {
			api.executeFlow(env, "XPXSecurityFileGeneratorService", inputDoc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			customerContactKeys.add(SCXmlUtil.getAttribute(inputDoc.getDocumentElement(), "CustomerContactKey"));
		}

	}

}

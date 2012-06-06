package com.xpedx.nextgen.security;

import java.rmi.RemoteException;
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
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXRRDSecurityFileGenerationAPI implements YIFCustomApi{
	
	private static YIFApi api = null;
	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	static {
		
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}
	
	
	String userType = "";
	String customer = "";
	String envID = "";
	String customerBranch = "";
	String customerNumber = "";
	String suffixType = "";
	String userID = "";
	String firstName = "";
	String lastName = "";
	String invoiceMethod = "";
	String emailID = "";
	String emailForInvoice = "";
	String branchID = "";
	String role = "";
	String viewInvoiceRole = "";
	String viewInvoice = "";
	String customerOrderDiv = "";
	String division = "";
	
	public Document securityFileGeneration(YFSEnvironment env, Document inputXML)  throws Exception
	{
		if(inputXML != null){
			log.debug("InputXML to securityFileGeneration"+SCXmlUtil.getString(inputXML));
		}
		Element inputElement = inputXML.getDocumentElement();
		String nodeName = inputElement.getNodeName();
		//form the outPut Doc
		Document fileListDoc = YFCDocument.createDocument("RRDUserList").getDocument();
		Element fileListElement = fileListDoc.getDocumentElement();
		if(nodeName.equals("CustomerContactList"))
		{
			formFileGenerationXMLForCustomerContact(env, inputXML, fileListDoc,
					fileListElement);
		}
		else
		{
			formFileGenarationXMLForCustomer(env, inputXML, fileListDoc,
					fileListElement);
			
			
		}
		
		
		if(log.isDebugEnabled()){
			log.debug("RRDUserList document is : "+SCXmlUtil.getString(fileListDoc));
		}
		return fileListDoc;
	}

	private void formFileGenarationXMLForCustomer(YFSEnvironment env,
			Document inputXML, Document fileListDoc, Element fileListElement)
			throws RemoteException {
	
		String customerID = "";
		String invoiceViewFlag = "";
		String teamKey = "";
		NodeList customerList = inputXML.getElementsByTagName("Customer");
		int customerLength = customerList.getLength();
		for(int i=0;i<customerLength;i++)
		{
			Element customerElement = (Element)customerList.item(i);
			if(log.isDebugEnabled()){
				log.debug("Customer Element : "+SCXmlUtil.getString(customerElement));
				log.debug("Customer Key : "+customerElement.getAttribute("CustomerKey"));
			}
			customerID = customerElement.getAttribute("CustomerID");
							
			Document getCustomerDoc = getCustomerList(env, customer);
			Element getCustomerElement = getCustomerDoc.getDocumentElement();
			invoiceViewFlag = SCXmlUtil.getXpathAttribute(getCustomerElement, "./Extn/@ExtnCanViewInvFlag");
			if(invoiceViewFlag.equals("Y"))
			{
				
				customer = "admin";
				emailID = "Do not email";
				role = "1";
				//form the input for getCustomerAssignmentList
				Document inputCustomerAssignmentDoc = YFCDocument.createDocument("CustomerAssignment").getDocument();
				Element inputCustomerAssignmentElement = inputCustomerAssignmentDoc.getDocumentElement();
				inputCustomerAssignmentElement.setAttribute("CustomerID", customerID);
				Document assignmentListdoc = api.invoke(env, "getCustomerAssignmentList", inputCustomerAssignmentDoc);
				NodeList assignmentNodeList = assignmentListdoc.getElementsByTagName("CustomerAssignment");
				int assignmentLength = assignmentNodeList.getLength();
				if(assignmentLength != 0)
				{
					for(int aCounter=0;aCounter<assignmentLength;aCounter++)
					{
						Element assignmentElement = (Element)assignmentNodeList.item(aCounter);
						teamKey = assignmentElement.getAttribute("TeamKey");
						userID = assignmentElement.getAttribute("UserId");
						//get the team list
						Document inputTeamDoc = YFCDocument.createDocument("Team").getDocument();
						Element inputTeamElement = inputTeamDoc.getDocumentElement();
						inputTeamElement.setAttribute("TeamKey", teamKey);
						Document teamListDoc = api.invoke(env, "getTeamList", inputTeamDoc);
						NodeList teamNodeList = teamListDoc.getElementsByTagName("TeamNodes");
						int teamLength = teamNodeList.getLength();
						if(teamLength != 0)
						{
							for(int tCounter=0;tCounter<teamLength;tCounter++)
							{
								Element teamElement = (Element)teamNodeList.item(tCounter);
								branchID = envID.charAt(0)+teamElement.getAttribute("ShipnodeKey");
								Element fileElement = fileListDoc.createElement("RRDUser");
								fileElement.setAttribute("Customer", customer);
								fileElement.setAttribute("UserID", userID);
								fileElement.setAttribute("FirstName", "");
								fileElement.setAttribute("LastName", "");
								fileElement.setAttribute("InvoiceMethod", "");
								fileElement.setAttribute("Email", emailID);
								fileElement.setAttribute("BranchID", branchID);
								fileElement.setAttribute("Admin", role);
								fileListElement.appendChild(fileElement);
							}
						}
					}
					
				}
				
				
				
			}
		}
	}

	private void formFileGenerationXMLForCustomerContact(YFSEnvironment env,
			Document inputXML, Document fileListDoc, Element fileListElement)
			throws RemoteException {
		if(log.isDebugEnabled()){
			log.debug("Entered the customer contact loop");
		}
		//get the customer mark the process flag to 'Y'
		//Element inputElement = inputXML.getDocumentElement();
		NodeList contactList = inputXML.getElementsByTagName("CustomerContact");
		int inputContactLength = contactList.getLength();
		for(int count1=0; count1<inputContactLength;count1++)
		{
			Element inputContactElement = (Element)contactList.item(count1); 
			String customerContactUserID = inputContactElement.getAttribute("UserID");
			String customerID = SCXmlUtil.getXpathAttribute(inputContactElement, "./Customer/@CustomerID");

			//get the customer document
			Document customerDoc = getCustomerList(env, customerID);
			Element customerElement = customerDoc.getDocumentElement();
			//get the user profile list for the customer
			Document customerContactListDoc = getUserProfile(env, customerContactUserID);
			NodeList customerContactList = customerContactListDoc.getElementsByTagName("CustomerContact");
			int contactLength = customerContactList.getLength();
			if(contactLength != 0)
			{
				for(int count = 0;count < contactLength;count++)
				{
					
					Element contactElement = (Element)customerContactList.item(count);
					viewInvoiceRole = SCXmlUtil.getXpathAttribute(contactElement, "./Extn/@ExtnViewInvoiceRole");
					viewInvoice = SCXmlUtil.getXpathAttribute(contactElement, "./Extn/@ExtnViewInvoice");
					userType = SCXmlUtil.getXpathAttribute(contactElement, "./Extn/@ExtnUserType");
					if((viewInvoiceRole.equals("Y")&&(userType.equals("EXTERNAL")))||((viewInvoice.equals("Y"))&&(userType.equals("INTERNAL"))))
					{
						if(log.isDebugEnabled()){
							log.debug("User is eligible to be in the file");
						}
						if(userType.equals("EXTERNAL"))
						{
							if(log.isDebugEnabled()){
								log.debug("External User");
							}
							envID = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnEnvironmentCode");
							customerBranch = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnCustomerDivision");
							customerNumber = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnLegacyCustNumber");
							char envChar = envID.charAt(0);
							customer = envChar+customerBranch+customerNumber;
							//email
							emailForInvoice = SCXmlUtil.getXpathAttribute(contactElement, "./Extn/@ExtnEmailForInv");
							if(YFCCommon.isVoid(emailForInvoice))
							{
								emailID = "Do not Email";
							}
							else
							{
								emailID = emailForInvoice;
							}
							//external users the admin role holds a value zero
							role = "0";
							customerOrderDiv = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnCustOrderBranch");
							branchID = envID+customerOrderDiv;
						}
						else
							if(userType.equals("INTERNAL"))
							{
								if(log.isDebugEnabled()){
									log.debug("Internal User");
								}
								customer = "admin";
								//email
								emailID = "Do not Email";
								//internal users the admin role holds a value one
								role = "1";
								char envChar = envID.charAt(0);
								division = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnCustomerDivision");
								branchID =  envChar+division;
							}
			
						userID = contactElement.getAttribute("UserID");
						firstName = contactElement.getAttribute("FirstName");
						lastName = contactElement.getAttribute("LastName");
						invoiceMethod = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnInvoiceDistMethod");
			
						//form email id
			
						Element fileElement = fileListDoc.createElement("RRDUser");
						fileElement.setAttribute("Customer", customer);
						fileElement.setAttribute("UserID", userID);
						fileElement.setAttribute("FirstName", firstName);
						fileElement.setAttribute("LastName", lastName);
						fileElement.setAttribute("InvoiceMethod", invoiceMethod);
						//branchid and the role has to be added.
						fileElement.setAttribute("BranchID", branchID);
						fileElement.setAttribute("Admin", role);
						fileElement.setAttribute("EmailID", emailID);
			
						fileListElement.appendChild(fileElement);
			
					}
				}
			}
		}
		}
	
	private Document getUserProfile(YFSEnvironment env, String customerContactUserID) throws YFSException, RemoteException
	{
		if(log.isDebugEnabled()){
			log.debug("getCustomerContactList in XPXRRDSecurityFileGenerationAPI ");
		}
		Document inputCustomerContactDoc = YFCDocument.createDocument("CustomerContact").getDocument();
		Element inputCustomerContactElement = inputCustomerContactDoc.getDocumentElement();
		inputCustomerContactElement.setAttribute("UserID", customerContactUserID);
		//template Doc
		Document customerContactTemplateDoc = YFCDocument.createDocument("CustomerContact").getDocument();
		Element customerContactTemplateElement = customerContactTemplateDoc.getDocumentElement();
		Element extnCustomerContactTemplateElement = customerContactTemplateDoc.createElement("Extn");
		customerContactTemplateElement.appendChild(extnCustomerContactTemplateElement);
		env.setApiTemplate("getCustomerContactList", customerContactTemplateDoc);
		if(log.isDebugEnabled()){
			log.debug("The input to getCustomerContactList in XPXRRDSecurityFileGenerationAPI is : "+SCXmlUtil.getString(inputCustomerContactDoc));
		}
		Document customerContactList = api.invoke(env, "getCustomerContactList", inputCustomerContactDoc);
		env.clearApiTemplate("getCustomerContactList");
		return customerContactList;
	}
	
	private Document getCustomerList(YFSEnvironment env, String customerID) throws YFSException, RemoteException
	{
		if(log.isDebugEnabled()){
			log.debug("getCustomer list in XPXRRDSecurityFileGenerationAPI");
		}
		Element customerElement = null;
		//form the input doc
		Document inputCustomerDoc = YFCDocument.createDocument("Customer").getDocument();
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerID", customerID);
		//template doc
		Document customerTemplateDoc = YFCDocument.createDocument("Customer").getDocument();
		Element customerTemplateElement = customerTemplateDoc.getDocumentElement();
		Element extnCustomerTemplateElement = customerTemplateDoc.createElement("Extn");
		customerTemplateElement.appendChild(extnCustomerTemplateElement);
		env.setApiTemplate("getCustomerList", customerTemplateDoc);
		if(log.isDebugEnabled()){
			log.debug("The input to getCustomerList in XPXRRDSecurityFileGenerationAPI is : "+ SCXmlUtil.getString(inputCustomerDoc));
		}
		Document outputCustomerListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		env.clearApiTemplate("getCustomerList");
		NodeList customerList = outputCustomerListDoc.getElementsByTagName("Customer");
		int customerLength = customerList.getLength();
		if(customerLength != 0)
		{
			customerElement = (Element)customerList.item(0);
		}
		Document customerDoc = YFCDocument.createDocument().getDocument();
		customerDoc.appendChild(customerDoc.importNode(customerElement, true));
		customerDoc.renameNode(customerDoc.getDocumentElement(), customerDoc.getNamespaceURI(), "Customer");
		return customerDoc;
	}

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}

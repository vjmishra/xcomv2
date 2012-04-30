package com.xpedx.nextgen.customermanagement.api;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfs.japi.YFSEnvironment;

/*
 * input xml
 * <Customer CustomerID="" OrganizationCode="" CustomerKey="" (newly added)/>
 */

/*
 * outputXML
 * <OrganizaionList>
 * <Organization CustomerID="" CustomerName="" CustomerSuffixType="" OrganizationCode="" ParentCustomerID="" /> 

 * </OrganizaionList>
 */
public class XPXGetChildOrganizationList implements YIFCustomApi{
	
	private static YIFApi api = null;

	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	
	/**
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 *             Method to get a list of Child Organizations
	 */
	public Document invokeGetChildOrganization(YFSEnvironment env,
			Document inXML) throws Exception

	{
		Element inputElement = inXML.getDocumentElement();
		api = YIFClientFactory.getInstance().getApi();
		// form the output doc to be returned.
		YFCDocument orgListDoucument = YFCDocument
				.createDocument("OrganizationList");
		YFCElement orgListElement = orgListDoucument.getDocumentElement();
		// get the customer key

		String customerKey = "";
		
		if (!inputElement.hasAttribute("CustomerKey")){
			customerKey = getCustomerKey(env, inXML);
		}
		else
		{
			customerKey = inputElement.getAttribute("CustomerKey");
		}
		

		if (customerKey != "") {
			// get the list of child orgs under this customer
			getChildOrgList(env, orgListDoucument, orgListElement, customerKey,inputElement.getAttribute("OrganizationCode"));
		}

		return orgListDoucument.getDocument();
	}

	/**
	 * @param env
	 * @param orgListDoucment
	 * @param orgListElement
	 * @param customerKey
	 * @throws RemoteException
	 *             Method to form the Organization element(this method gets
	 *             called recursively to fetch all the child organizations)
	 */
	private void getChildOrgList(YFSEnvironment env,
			YFCDocument orgListDoucment, YFCElement orgListElement,
			String customerKey,String organizationCode) throws RemoteException {
		// Form the input to fetch the child org list
		YFCDocument inputCustomerDocument = YFCDocument
				.createDocument("XPXCustomerHierarchyView");
		YFCElement inputCustomerElement = inputCustomerDocument
				.getDocumentElement();
		YFCElement complexQueryElement = inputCustomerDocument
		.createElement("ComplexQuery");
		
		YFCElement orElement = inputCustomerDocument
		.createElement("Or");
		complexQueryElement.appendChild(orElement);
		YFCElement expElement = inputCustomerDocument
		.createElement("Exp");
		expElement.setAttribute("Name", "ShipToCustomerKey");
		expElement.setAttribute("Value", customerKey);
		expElement.setAttribute("QryType", "FLIKE");
		orElement.appendChild(expElement);
		
		YFCElement exp1Element = inputCustomerDocument
		.createElement("Exp");
		exp1Element.setAttribute("Name", "BillToCustomerKey");
		exp1Element.setAttribute("Value", customerKey);
		exp1Element.setAttribute("QryType", "FLIKE");
		orElement.appendChild(exp1Element);
		
		YFCElement exp2Element = inputCustomerDocument
		.createElement("Exp");
		exp2Element.setAttribute("Name", "SAPCustomerKey");
		exp2Element.setAttribute("Value", customerKey);
		exp2Element.setAttribute("QryType", "FLIKE");
		orElement.appendChild(exp2Element);
		
		YFCElement exp3Element = inputCustomerDocument
		.createElement("Exp");
		exp3Element.setAttribute("Name", "MSAPCustomerKey");
		exp3Element.setAttribute("Value", customerKey);
		exp3Element.setAttribute("QryType", "FLIKE");
		orElement.appendChild(exp3Element);
		inputCustomerElement.appendChild(complexQueryElement);
		YFCDocument outputCustomerListTemplateDoc = YFCDocument
				.createDocument("XPXCustHierarchyViewList");
		YFCElement outputCustomerListTemplateElement = outputCustomerListTemplateDoc
				.getDocumentElement();
		outputCustomerListTemplateElement.setAttribute("TotalNumberOfRecords",
				"");
		YFCElement outputCustomerTemplateElement = outputCustomerListTemplateDoc
				.createElement("XPXCustHierarchyView");
		outputCustomerListTemplateElement
				.appendChild(outputCustomerTemplateElement);
		env.setApiTemplate("XPXCustomerHierarchyViewService", outputCustomerListTemplateDoc
				.getDocument());
		Document outputCustomerDocument = api.executeFlow(env, "XPXCustomerHierarchyViewService",
				inputCustomerDocument.getDocument());
		env.clearApiTemplate("XPXCustomerHierarchyViewService");
		Element outputCustomerElement = outputCustomerDocument
				.getDocumentElement();
		if (!outputCustomerElement.getAttribute("TotalNumberOfRecords").equals(
				"0")) {
			createOrganization(outputCustomerElement,customerKey,orgListElement,
			orgListDoucment,organizationCode);
			
		}
	}
	
	private void createOrganization(Element outputCustomerElement,String customerKey,YFCElement orgListElement,
			YFCDocument orgListDoucment,String organizationCode)
	{
		ArrayList<Element> childElementList=SCXmlUtil.getElements(outputCustomerElement, "XPXCustHierarchyView");
		int root=0;
		
		if(childElementList != null && childElementList.size()>0)
		{
			Element rootElement =childElementList.get(0);
			if(customerKey.equals(rootElement.getAttribute("ShipToCustomerKey")))
				root=0;
			if(customerKey.equals(rootElement.getAttribute("BillToCustomerKey")))
				root=1;
			if(customerKey.equals(rootElement.getAttribute("SAPCustomerKey")))
				root=2;
			if(customerKey.equals(rootElement.getAttribute("MSAPCustomerKey")))
				root=3;
			getCustomer(childElementList,orgListElement,orgListDoucment,organizationCode,root);
		}
		
	}
	private void getCustomer(ArrayList<Element> childElementList,YFCElement orgListElement,YFCDocument orgListDoucment,
			String organizationCode,int root)
	{
		String customersArr[]={"ShipTo","BillTo","SAP","MSAP"};	
		ArrayList<String> addedCustomer=new ArrayList<String>();
		for(int i=0;i<childElementList.size();i++)
		{
			Element customerElement=childElementList.get(i);
			for(int j=0;j<root;j++)
			{
				if(!addedCustomer.contains(customerElement.getAttribute(customersArr[j]+"CustomerID")))
				{
						YFCElement orgElement = orgListDoucment.createElement("Organization");
						orgElement.setAttribute("OrganizationCode", organizationCode);
						orgElement.setAttribute("CustomerID", customerElement.getAttribute(customersArr[j]+"CustomerID"));
						addedCustomer.add(customerElement.getAttribute(customersArr[j]+"CustomerID"));
						orgElement.setAttribute("ParentCustomerID", customerElement.getAttribute(customersArr[j+1]+"CustomerID"));
						if(j==0)
							orgElement.setAttribute("CustomerSuffixType", "S");
						if(j==1)
							orgElement.setAttribute("CustomerSuffixType", "B");
						if(j==2)
							orgElement.setAttribute("CustomerSuffixType", "C");
						orgElement.setAttribute("CustomerName", customerElement.getAttribute(customersArr[j]+"CustomerName"));
						orgElement.setAttribute("SAPParentAccNo", customerElement.getAttribute(customersArr[j]+"SAPParentAccNo"));
						orgElement.setAttribute("SAPNumber", customerElement.getAttribute(customersArr[j]+"SAPNumber"));
						orgListElement.appendChild(orgElement);
				}
			}
		}
	}
		
	/**
	 * @param env
	 * @param orgListDoucment
	 * @param orgListElement
	 * @param customerKey
	 * @throws RemoteException
	 *             Method to form the Organization element(this method gets
	 *             called recursively to fetch all the child organizations)
	 *//*
	private void getChildOrgList(YFSEnvironment env,
			YFCDocument orgListDoucment, YFCElement orgListElement,
			String customerKey) throws RemoteException {
		// Form the input to fetch the child org list
		YFCDocument inputCustomerDocument = YFCDocument
				.createDocument("Customer");
		YFCElement inputCustomerElement = inputCustomerDocument
				.getDocumentElement();
		inputCustomerElement.setAttribute("ParentCustomerKey", customerKey);
		// create a output template
		YFCDocument outputCustomerListTemplateDoc = YFCDocument
				.createDocument("CustomerList");
		YFCElement outputCustomerListTemplateElement = outputCustomerListTemplateDoc
				.getDocumentElement();
		outputCustomerListTemplateElement.setAttribute("TotalNumberOfRecords",
				"");
		YFCElement outputCustomerTemplateElement = outputCustomerListTemplateDoc
				.createElement("Customer");
		// outputCustomerTemplateElement.setAttribute("BuyerOrganizationCode",
		// "");
		outputCustomerTemplateElement.setAttribute("OrganizationCode", "");
		outputCustomerTemplateElement.setAttribute("CustomerID", "");
		outputCustomerTemplateElement.setAttribute("CustomerKey", "");
		outputCustomerListTemplateElement
				.appendChild(outputCustomerTemplateElement);
		YFCElement outputCustomerExtnTemplateElement = outputCustomerListTemplateDoc.createElement("Extn");
		outputCustomerExtnTemplateElement.setAttribute("ExtnSuffixType", "");
		outputCustomerExtnTemplateElement.setAttribute("ExtnCustomerName", "");
		outputCustomerExtnTemplateElement.setAttribute("ExtnSAPParentAccNo", "");
		outputCustomerExtnTemplateElement.setAttribute("ExtnSAPNumber", "");
		outputCustomerTemplateElement.appendChild(outputCustomerExtnTemplateElement);
		YFCElement outputParentCustomerTemplateElement = outputCustomerListTemplateDoc.createElement("ParentCustomer");
		outputParentCustomerTemplateElement.setAttribute("CustomerID", "");
		outputCustomerTemplateElement.appendChild(outputParentCustomerTemplateElement);
		env.setApiTemplate("getCustomerList", outputCustomerListTemplateDoc
				.getDocument());
		// System.out.println(YFCDocument.getDocumentFor(inputCustomerDocument.getDocument()));
		Document outputCustomerDocument = api.invoke(env, "getCustomerList",
				inputCustomerDocument.getDocument());
		// System.out.println(YFCDocument.getDocumentFor(outputCustomerDocument));
		env.clearApiTemplate("getCustomerList");
		Element outputCustomerElement = outputCustomerDocument
				.getDocumentElement();
		if (!outputCustomerElement.getAttribute("TotalNumberOfRecords").equals(
				"0")) {
			NodeList customerList = outputCustomerDocument
					.getElementsByTagName("Customer");
			int customerListLength = customerList.getLength();
			for (int orgCounter = 0; orgCounter < customerListLength; orgCounter++) {
				Element customerElement = (Element) customerList
						.item(orgCounter);
				YFCElement orgElement = orgListDoucment
						.createElement("Organization");
				// orgElement.setAttribute("OrganizationCode",
				// customerElement.getAttribute("BuyerOrganizationCode"));
				orgElement.setAttribute("OrganizationCode", customerElement
						.getAttribute("OrganizationCode"));
				orgElement.setAttribute("CustomerID", customerElement
						.getAttribute("CustomerID"));
				orgElement.setAttribute("ParentCustomerID", SCXmlUtil.getXpathAttribute(customerElement, "./ParentCustomer/@CustomerID"));
				orgElement.setAttribute("CustomerSuffixType", SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnSuffixType"));
				orgElement.setAttribute("CustomerName", SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnCustomerName"));
				orgElement.setAttribute("SAPParentAccNo", SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnSAPParentAccNo"));
				orgElement.setAttribute("SAPNumber", SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnSAPNumber"));
				
				orgListElement.appendChild(orgElement);
				// call customer key function for this org
				// form the input xml
				YFCDocument inputXML = YFCDocument.createDocument("Customer");
				YFCElement inputElement = inputXML.getDocumentElement();
				inputElement.setAttribute("CustomerID", customerElement
						.getAttribute("CustomerID"));
				// inputElement.setAttribute("OrganizationCode",
				// customerElement.getAttribute("BuyerOrganizationCode"));
				inputElement.setAttribute("OrganizationCode", customerElement
						.getAttribute("OrganizationCode"));
				// String customerKeyForChildOrg = getCustomerKey(env,
				// inputXML.getDocument());
				String customerKeyForChildOrg = customerElement
						.getAttribute("CustomerKey");

				// get the list of child orgs under this customer
				getChildOrgList(env, orgListDoucment, orgListElement,
						customerKeyForChildOrg);

			}
		}*/
	/**
	 * @param env
	 * @param inXML
	 * @return
	 * @throws RemoteException
	 *             Method to get the CustomerKey to fetch the child
	 *             Organizations.
	 */
	private String getCustomerKey(YFSEnvironment env, Document inXML)
			throws RemoteException {
		Element inputElement = inXML.getDocumentElement();
		// form the input xml
		// pass customerid and organization code
		String customerKey = "";
		YFCDocument inputCustomerDocument = YFCDocument
				.createDocument("Customer");
		YFCElement inputCustomerElement = inputCustomerDocument
				.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerID", inputElement
				.getAttribute("CustomerID"));
		// inputCustomerElement.setAttribute("BuyerOrganizationCode",
		// inputElement.getAttribute("OrganizationCode"));
		inputCustomerElement.setAttribute("OrganizationCode", inputElement
				.getAttribute("OrganizationCode"));
		// create a output template
		YFCDocument outputCustomerListTemplateDoc = YFCDocument
				.createDocument("CustomerList");
		YFCElement outputCustomerListTemplateElement = outputCustomerListTemplateDoc
				.getDocumentElement();
		outputCustomerListTemplateElement.setAttribute("TotalNumberOfRecords",
				"");
		YFCElement outputCustomerTemplateElement = outputCustomerListTemplateDoc
				.createElement("Customer");
		outputCustomerTemplateElement.setAttribute("CustomerKey", "");
		outputCustomerListTemplateElement
				.appendChild(outputCustomerTemplateElement);
		env.setApiTemplate("getCustomerList", outputCustomerListTemplateDoc
				.getDocument());
		// System.out.println(inputCustomerDocument);
		// System.out.println(YFCDocument.getDocumentFor(inputCustomerDocument.getDocument()));
		// Document outputCustomerDocument = api.invoke(env, "getCustomerList",
		// inputCustomerDocument.getDocument());
		Document outputCustomerDocument = api.invoke(env, "getCustomerList",
				inputCustomerDocument.getDocument());
		// System.out.println(YFCDocument.getDocumentFor(outputCustomerDocument));
		env.clearApiTemplate("getCustomerList");
		if (!outputCustomerDocument.getDocumentElement().getAttribute(
				"TotalNumberOfRecords").equals("0")) {
			Element outputCustomerElement = (Element) outputCustomerDocument
					.getElementsByTagName("Customer").item(0);
			customerKey = outputCustomerElement.getAttribute("CustomerKey");
			//System.out.println("CustomerKey" + customerKey);
		}
		return customerKey;
	}

}

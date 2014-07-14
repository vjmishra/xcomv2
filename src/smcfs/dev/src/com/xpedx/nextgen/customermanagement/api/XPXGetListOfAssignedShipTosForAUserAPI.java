package com.xpedx.nextgen.customermanagement.api;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author suneetha k
 */
public class XPXGetListOfAssignedShipTosForAUserAPI implements YIFCustomApi {
	/** API object. */
	private static YIFApi api = null;
	private Set<String> assignedShipToCustomers = new HashSet<String>();


	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
	}
	static {
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			e.printStackTrace();
		}
	}
	
	/*** Service: XPXGetListOfAssignedShipTosForAUserService
	 * 
	 *  
	 * <pre>
	 * &lt;CustomerAssignment UserId='required' &gt;
	 * &lt;Template&gt;
	 * &lt;CustomerList&gt;
	 *    &lt;Customer CustomerKey='' CustomerID='' OrganizationCode=''&gt;
	 *       &lt;Extn ExtnSuffixType=''/&gt;
	 *       &lt;CustomerAdditionalAddressList TotalNumberOfRecords=''&gt;
	 *          &lt;CustomerAdditionalAddress&gt;
	 *             &lt;PersonInfo/&gt;
	 *          &lt;/CustomerAdditionalAddress&gt;
	 *       &lt;/CustomerAdditionalAddressList&gt;
	 *    &lt;/Customer&gt;
	 * &lt;/CustomerList&gt;
	 * &lt;/Template&gt;
	 * &lt;/CustomerAssignment&gt;
	 * </pre>
	 * 
	 * <br/> <b>Default output template:</b>
	 * 
	 * <pre>
	 * &lt;CustomerList&gt;
	 * &lt;Customer CustomerKey='' CustomerID='' OrganizationCode=''&gt;
	 * &lt;Extn ExtnSuffixType=''/&gt;
	 * &lt;/Customer&gt;
	 * &lt;/CustomerList&gt;
	 * </pre>
	 * 
	 * <i>Note:</i> <b>Template</b> element is optional, if provided it will be merged with the default output template of this API.
	 * 
	 * @param env
	 * @param docInput
	 * @return Document
	 * @throws Exception***/

	public Document invokeGetCustomerAssignmentList(YFSEnvironment env,	Document inXML) throws Exception {

		//Document for ShipTo Customer List
		Document docShipToCustomerList = SCXmlUtil.createDocument("CustomerList");
		Element eleShipToCustomerList = docShipToCustomerList.getDocumentElement();
		Set<String> setOfCustomers = new HashSet<String>();
		Document docQrydCustomerList=null;
		Document outputCustomerAssignmentList = null;
		String extnSuffixType=null;

		// Getting the User ID
		if (inXML == null) {
			// Throw new YFSException with the description
			throw new YFSException("Input XML document cannot be null or invalid to XPXGetListOfAssignedShipTosForAUserAPI.invokeGetCustomerAssignmentList().");
		}
		//Validating the User ID
		if (SCUtil.isVoid(inXML.getDocumentElement().getAttribute("UserId"))) {
			throw new YFSException("Invalid Input XPXGetListOfAssignedShipTosForAUserAPI.invokeGetCustomerAssignmentList(): UserId is mandatory.");
		}

		try { 
			// 1. Merge and Update getCustomerAssignmentList and getCustomerList APIs templates.
			this.mergeAndUpdateOutputTemplates(env, inXML);

			// Invoke getCustomerAssignmentList API - get Assignment Customer List for the given UserId
			outputCustomerAssignmentList = api.invoke(env, XPXLiterals.GET_CUSTOMER_ASSIGNMENT_API, inXML);

			//Read and update Ship To Customer elements into the ShipTos Document from Assigned Customer List
			NodeList assignedCustomerList=outputCustomerAssignmentList.getDocumentElement().getElementsByTagName("Customer");
			for (int  i=0;i<assignedCustomerList.getLength();i++){

				Element assignedCustomerElement=(Element) assignedCustomerList.item(i);

				//Getting ExtnSuffixType value. It will be either MC / C / B / S
				extnSuffixType=SCXmlUtil.getXpathAttribute(assignedCustomerElement,"Extn/@ExtnSuffixType");
				String customerID = SCXmlUtil.getAttribute(assignedCustomerElement, "CustomerID");

				//if extnSuffixType=='S' then add to docShipToCustomerList document
				if(XPXLiterals.CHAR_S.equals(extnSuffixType)){
					if(!assignedShipToCustomers.contains(customerID)) {
						SCXmlUtil.importElement(eleShipToCustomerList, assignedCustomerElement);
						assignedShipToCustomers.add(customerID);
					}
				}
				else{
					setOfCustomers.add(assignedCustomerElement.getAttribute(XPXLiterals.A_CUSTOMER_KEY));
				}
				extnSuffixType=null;
			}

			// If some MC/C/B customer are assigned then query from it's child, goes in loop till SET[setOfCustomers].size==0
			while(setOfCustomers.size()>0){

				// 1. prepare getCustomerList API input
				Document docQryCustomers = SCXmlUtil.createDocument(XPXLiterals.E_CUSTOMER);
				Element eleQryCustomers = docQryCustomers.getDocumentElement();
				Element eleComplexQuery = SCXmlUtil.createChild(eleQryCustomers, XPXLiterals.E_COMPLEX_QUERY);
				Element eleOr = SCXmlUtil.createChild(eleComplexQuery, XPXLiterals.E_Or);
				for (String qryCustomerKey : setOfCustomers) {
					Element eleExp = SCXmlUtil.createChild(eleOr, XPXLiterals.E_EXP);
					eleExp.setAttribute(XPXLiterals.A_NAME, XPXLiterals.A_PARENT_CUSTOMER_KEY);
					eleExp.setAttribute(XPXLiterals.A_VALUE, qryCustomerKey);
				}
				// 2. invoke getCustomerList API
				docQrydCustomerList = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, docQryCustomers);

				//3. update eleShipToCustomerList
				setOfCustomers = this.mungeOutput(docQrydCustomerList, docShipToCustomerList);
			}

			// Clear the template set in environment.
			env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
			env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_ASSIGNMENT_API);

		} catch (Exception e) {
			e.printStackTrace();
			throw new YFSException("XPXGetListOfAssignedShipTosForAUserAPI.invokeGetCustomerAssignmentList()-->Unable to get the list of Assigned Ship-Tos Locations.");
		}
		return docShipToCustomerList;
	}

	private void mergeAndUpdateOutputTemplates(YFSEnvironment env,
			Document inXML) {
		//			Get Requested Customer Template

		Element eleReqTemplate = SCXmlUtil.getChildElement(inXML.getDocumentElement(), "Template");
		Element eleReqCustomerListTemplate = null;
		Element eleReqCustomerTemplate = null;
		if(null != eleReqTemplate){

			eleReqCustomerListTemplate = SCXmlUtil.getChildElement(eleReqTemplate, "CustomerList");
			if(null != eleReqCustomerListTemplate){
				eleReqCustomerTemplate = SCXmlUtil.getChildElement(eleReqCustomerListTemplate, "Customer");
			}
		}

		//Get the getCustomerList API template and set in the Environment
		Document docDefaultTemplate = this.mergeInputReqTemplateWithDefault(eleReqCustomerTemplate);
		env.setApiTemplate("getCustomerList", docDefaultTemplate);
		docDefaultTemplate = null;

		//Get the getCustomerAssignmentList API template and set in the Environment
		Document docDefaultCustomerAssignmentTemplate = this.mergeInputReqCustomerAssignmentTemplateWithDefault(eleReqCustomerTemplate);
		env.setApiTemplate("getCustomerAssignmentList", docDefaultCustomerAssignmentTemplate);
		docDefaultCustomerAssignmentTemplate = null;

		// remove Template Element from the input
		if(null != eleReqTemplate)
			SCXmlUtil.removeNode(eleReqTemplate);
	}

	private Set<String> mungeOutput(Document docQrydCustomerList, Document docShipToCustomerList) {
		Set<String> setOfCustomers = new HashSet<String>();
		List<Element> listCustomers = SCXmlUtil.getChildrenList(docQrydCustomerList.getDocumentElement());
		for (Element eleQrydCustomer : listCustomers) {
			String customerID = SCXmlUtil.getAttribute(eleQrydCustomer, "CustomerID");
			if(XPXLiterals.CHAR_S.equals(SCXmlUtil.getXpathAttribute(eleQrydCustomer,"Extn/@ExtnSuffixType"))){
				if(!assignedShipToCustomers.contains(customerID)) {
					SCXmlUtil.importElement(docShipToCustomerList.getDocumentElement(), eleQrydCustomer);
					assignedShipToCustomers.add(customerID);
				}
			} else {
				setOfCustomers.add(eleQrydCustomer.getAttribute(XPXLiterals.A_CUSTOMER_KEY));
			}
		}
		return setOfCustomers;
	}
	/**
	 * Merges Customer Element only
	 * @param eleReqCustomerTemplate
	 * @return
	 */
	private Document mergeInputReqTemplateWithDefault(Element eleReqCustomerTemplate) {
		//Default template for Customer List
		String strDefaultTemplate = new StringBuffer().append("<CustomerList>")
		.append("<Customer CustomerKey='' CustomerID='' OrganizationCode='' BuyerOrganizationCode=''>")
		.append("<Extn ExtnSuffixType=''/>")
		.append("<CustomerAdditionalAddressList><CustomerAdditionalAddress>")
		.append("<PersonInfo AddressLine1='' AddressLine2='' AddressLine3='' City='' State='' ZipCode='' Country='' EMailID='' />")
		.append("</CustomerAdditionalAddress></CustomerAdditionalAddressList>")
		.append("</Customer></CustomerList>").toString();
		Document docDefaultTemplate = SCXmlUtil.createFromString(strDefaultTemplate);

		// Merge Customer Element alone
		if(null != eleReqCustomerTemplate){
			Element eleCustomer = SCXmlUtil.getChildElement(docDefaultTemplate.getDocumentElement(), "Customer");
			if(null != eleCustomer){
				SCXmlUtil.mergeElement(eleReqCustomerTemplate, eleCustomer, true);
			}
		}
		return docDefaultTemplate;
	}
	/**
	 * Merges Customer Element only
	 * @param eleReqCustomerTemplate
	 * @return
	 */
	private Document mergeInputReqCustomerAssignmentTemplateWithDefault(Element eleReqCustomerTemplate) {

		//Default template for Customer Assignment List
		String strDefaultTemplate = new StringBuffer().append("<CustomerAssignmentList><CustomerAssignment UserId=''>")
		.append("<Customer CustomerKey='' CustomerID='' OrganizationCode='' BuyerOrganizationCode=''>")
		.append("<Extn ExtnSuffixType=''/>")
		.append("<CustomerAdditionalAddressList><CustomerAdditionalAddress>")
		.append("<PersonInfo AddressLine1='' AddressLine2='' AddressLine3='' City='' State='' ZipCode='' Country='' EMailID='' />")
		.append("</CustomerAdditionalAddress></CustomerAdditionalAddressList>")
		.append("</Customer></CustomerAssignment></CustomerAssignmentList>").toString();
		Document docDefaultTemplate = SCXmlUtil.createFromString(strDefaultTemplate);

		if(null != eleReqCustomerTemplate){

			Element eleCustomerAssignment = SCXmlUtil.getChildElement(docDefaultTemplate.getDocumentElement(), "CustomerAssignment");
			Element eleCustomer = SCXmlUtil.getChildElement(eleCustomerAssignment, "Customer");
			SCXmlUtil.mergeElement(eleReqCustomerTemplate, eleCustomer, true);
		}
		return docDefaultTemplate;
	}
}

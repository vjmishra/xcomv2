package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.xpedx.nextgen.common.util.XPXUtils;

public class XPXEmailHandlerAPI implements YIFCustomApi {

	private static YIFApi api = null;
	String getItemUomMasterListTemplate = "global/template/api/getItemUomMasterList.XPXMasterUomLoad.xml";
	String getOrderListTemplate = "global/template/api/getOrderList.XPXSendConfirmationEmail.xml";
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXLegacyOrderCreationService.xml";
	String getCustomerContactListTemplate = "global/template/api/getCustomerContactList.XPXConfirmationEmailService.xml";
	String getCustomerListCSREmailTemplate ="global/template/api/getCustomerList.XPXCSRFlagEmails.xml";
	String getSalesRepListTemplate ="global/template/api/getSalesRepList.XPXSREmails.xml";
	String getUserListTemplate = "global/template/api/getUserListTemplate.xml";
	/** Added by Arun Sekhar on 13-April-2011 for Email templates **/
	String orderNo = "";
	String customerDivision = "";
	private static YFCLogCategory yfcLogCatalog;

	static {
		yfcLogCatalog = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}

	/***************************************************************/

	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub8
	}

	// processOrdConfEmail method

	/**
	 * @param env
	 * @param inXML
	 */
	public Document processOrdConfEmail(YFSEnvironment env, Document inXML)
			throws Exception {
		api = YIFClientFactory.getInstance().getApi();
		try {
			api.executeFlow(env, "XPXSendOrderConfirmationEmailService", inXML);
		} catch (Exception e) {
			yfcLogCatalog.debug("Exception caught in processOrdConfEmail");
			// To add the component of handle failure scenario
		}
		return inXML;
	}

	public static String getCustomerID(String customerId, YFSEnvironment env) throws Exception{
		String msapId="";
		if(customerId == null ) {
			return msapId;
		}
		String AttributeToQry = "ShipToCustomerID";
		
			Document inputDoc = SCXmlUtil.createDocument("XPXCustHierachyView");
			inputDoc.getDocumentElement().setAttribute("ShipToCustomerID", customerId);
			System.out.println("************getCustomerID : inputDoc="+SCXmlUtil.getString(inputDoc));
			Document obj=api.executeFlow(env, "XPXCustomerHierarchyViewService", inputDoc);
			
			Element outputElement = obj.getDocumentElement();
			System.out.println("************getCustomerID : outputElement="+SCXmlUtil.getString(outputElement));
			NodeList customerHierView = outputElement.getElementsByTagName("XPXCustHierarchyView");
			for(int j=0;j<customerHierView.getLength();j++) {
				Element customerHierViewElem = (Element)customerHierView.item(j);
				msapId = customerHierViewElem.getAttribute("MSAPCustomerID");
			}	
			System.out.println("************getCustomerID : msapId="+msapId);
		return msapId;
	}
	
	private String getSalesRepEmailForSalesPro(String msapCustomerID, YFSEnvironment env, String salesRepId) throws Exception
	{
		String emailId="";
		System.out.println("************getSalesRepEmailForSalesPro : salesRepId="+salesRepId);
		Document inputDoc = SCXmlUtil.createDocument("XPXSalesRepCustomers");
		inputDoc.getDocumentElement().setAttribute("CustomerID", msapCustomerID);		
		Document obj=api.executeFlow(env, "XPXGetSRCustomersListService", inputDoc);
		
		Element outputElement = obj.getDocumentElement();
		System.out.println("************getSalesRepEmailForSalesPro :outputElement="+SCXmlUtil.getString(outputElement));
		NodeList customerHierView = outputElement.getElementsByTagName("XPXSalesRepCustomers");
		for(int j=0;j<customerHierView.getLength();j++) {
			Element customerHierViewElem = (Element)customerHierView.item(j);
			String salesRepIdFromView="";
			salesRepIdFromView = customerHierViewElem.getAttribute("SalesRepId");
			System.out.println("************getSalesRepEmailForSalesPro : salesRepIdFromView="+salesRepIdFromView);
			if(salesRepId!=null && salesRepId.trim().length()>0 && salesRepId.equalsIgnoreCase(salesRepIdFromView)){
				emailId = customerHierViewElem.getAttribute("EmailID");
				System.out.println("************getSalesRepEmailForSalesPro : emailId="+emailId);
			}
		}		
		return emailId;
	}
	public Document sendEmail(YFSEnvironment env, Document inXML) throws Exception {
		
		yfcLogCatalog.debug("XPXEmailHandlerAPI_InXML: "+ SCXmlUtil.getString(inXML));
		Document customerDoc= null;
		api = YIFClientFactory.getInstance().getApi();
		
		Element inputElement = inXML.getDocumentElement();
		
		
		Document orderListOutput = formInputToGetCustomerOrder(env, inputElement);

		NodeList orderNodeList = orderListOutput.getDocumentElement().getElementsByTagName("Order");
		int orderLength = orderNodeList.getLength();
		if (orderLength != 0) {
			Element orderElement = (Element) orderNodeList.item(0);
			String readEnv = YFSSystem.getProperty("environment");
			orderElement.setAttribute("EnvironmentID", readEnv);
			if(orderLength >2)
				orderElement.setAttribute("IsOrderSplit", "Y");
			else
				orderElement.setAttribute("IsOrderSplit", "N");
			//This is assumed as the Customer order
			customerDoc = YFCDocument.createDocument().getDocument();
			customerDoc.appendChild(customerDoc.importNode(orderElement, true));
			customerDoc.renameNode(customerDoc.getDocumentElement(),
					customerDoc.getNamespaceURI(), "Order");

		}
		
		/************* Added by Arun Sekhar on 13-April-2011 for Email templates ************/
		if(customerDoc != null)
		{
		    Element inputExtnElement = (Element) customerDoc.getDocumentElement().getElementsByTagName(
				"Extn").item(0);
		    customerDivision = inputExtnElement
				.getAttribute("ExtnCustomerDivision");
		    if(customerDivision != null) {
		    customerDivision = customerDivision.substring(0, customerDivision.length() - 2);
		    }
		}   
		/*************************************************************************************/
		
		/******* Added by Arun Sekhar on 14-April-2011 *******/
		XPXUtils utilObj = new XPXUtils();
		customerDoc = utilObj.stampBrandLogo(env, customerDoc);
		/*****************************************************/

		// String sendMail = getLegacyOrders(env,customerDoc);
		// if(sendMail.equals("Y"))
		// {
		stampLegacyOrderNoOnCustomerOrderLine(env, customerDoc,orderListOutput);
		
		/******* Added by Arun Sekhar on 28-April-2011 *******/
		utilObj.stampOrderSubjectLine(env, customerDoc);
		yfcLogCatalog.debug("inputDocument with SubjectLine: "
				+ SCXmlUtil.getString(customerDoc));
		
		/*****************************************************/
		
		// }
		// yfcLogCatalog.debug("customerDoc ::" +
		// SCXmlUtil.getString(customerDoc));
		// yfcLogCatalog.debug("inputElement :: " +
		// SCXmlUtil.getString(inputElement));

		// get the email ids to be sent in to and cc list
		Document getCCListDoc = getCCList(env, inputElement);
		// Changes made on 18/02/2011 to handle if Customer Contact List
		// Document is null.
		if (getCCListDoc != null) {
			System.out.println("************sendEmail : getCCListDoc="+SCXmlUtil.getString(getCCListDoc));
			Element getCustomerContactElement = (Element) getCCListDoc
					.getElementsByTagName("CustomerContact").item(0);
			String strToEmailid = "";
			
			String isSalesRepFlag = SCXmlUtil.getXpathAttribute(
					getCustomerContactElement, "./Extn/@ExtnIsSalesRep");
			/*
			 * JIRA 4093 Start -If sales rep flag is Y toemail will be yfs_person_info
			 */
			System.out.println("************sendEmail : isSalesRepFlag="+isSalesRepFlag);
			if("Y".equalsIgnoreCase(isSalesRepFlag)){
				String buyerOrgCode = "";	
				String msapCustomerId = "";
				YFCDocument inDoc = YFCDocument.getDocumentFor(inputElement.getOwnerDocument());
				YFCElement rootElem = inDoc.getDocumentElement();
				String salesRepId="";
				String customerContactId="";
				if(rootElem!=null){
					buyerOrgCode = rootElem.getAttribute("BuyerOrganizationCode");
					customerContactId = rootElem.getAttribute("CustomerContactID");
					System.out.println("************sendEmail : customerContactId="+customerContactId);
					if(customerContactId!=null && customerContactId.trim().length()>0){
						salesRepId = customerContactId.substring(0, customerContactId.indexOf('@'));
					}
				}
				msapCustomerId = getCustomerID(buyerOrgCode,env);
				strToEmailid = getSalesRepEmailForSalesPro(msapCustomerId,env,salesRepId);
			
			}else{
				strToEmailid = getCustomerContactElement
				.getAttribute("EmailID");
		
			}
		
			
			

			/*if("Y".equalsIgnoreCase(isSalesRepFlag)){
				NodeList nlCustomerContact = customerElem
				.getElementsByTagName("CustomerContact");
				Element personInfo=null;
				Element tempCustomerContact = null;
				int nlCustomerContactLen = nlCustomerContact.getLength();
				for (int i = 0; i < nlCustomerContactLen; i++) {
					tempCustomerContact = (Element) nlCustomerContact.item(i);
					personInfo = (Element) tempCustomerContact
							.getElementsByTagName("User/ContactPersonInfo").item(0);
					strToEmailid = personInfo.getAttribute("EmailID");
				}
			}
			*/	
			customerDoc.getDocumentElement().setAttribute("strToEmailid",
					strToEmailid);

			// to list ends here
			
			//added by ritesh - to get the Billto of order and csr1 and csr2 from the bill to 
			//will reform the exception block, once it goes well with dev testing.
			
			/*Changes done for order confirmation email -Start -Remove comment*/
			try
			{
				
				setCSREmails(env,customerDoc);
				getSalesRepEmail(env, customerDoc);
				
			}catch(Exception  ex)
			{
				
				ex.printStackTrace();
			}
			
			
			/*Changes done for order confirmation email - End*/
			

			/*Element customerElem = (Element) getCustomerContactElement
					.getElementsByTagName("Customer").item(0);

			String strCustomerAdminEmailList = "";
			*//** Added by Arun Sekhar on 14-April-2011 for Email templates **//*
			*//**
			 * Check if this CustomerContactID corresponds to a Sales Rep If
			 * 'Yes' (ExtnIsSalesRep=Y), it is assumes that it is a dummy user,
			 * in which case, we don't need to consider its email ID in the 'To'
			 * list
			 *//*
			String isSalesRepFlag = SCXmlUtil.getXpathAttribute(
					getCustomerContactElement, "./Extn/@ExtnIsSalesRep");

            
			if ("N".equalsIgnoreCase(isSalesRepFlag)) {

				NodeList nlCustomerContact = customerElem
						.getElementsByTagName("CustomerContact");

				int nlCustomerContactLen = nlCustomerContact.getLength();

				yfcLogCatalog.debug("nlCustomerContactLen :: "
						+ nlCustomerContactLen);
				Element tempCustomerContact = null;
				String GROUP_BUYER_ADMIN = "BUYER-ADMIN";
				NodeList nlUserGroupList = null;
				Element tempUserGroupList = null;
				String strUsergroupId = null;
				int nlUserGroupListLen = 0;
				Element extnCCElem = null;
				String strExtnOrderConfEmailFlag = null;

				for (int i = 0; i < nlCustomerContactLen; i++) {
					tempCustomerContact = (Element) nlCustomerContact.item(i);
					extnCCElem = (Element) tempCustomerContact
							.getElementsByTagName("Extn").item(0);
					strExtnOrderConfEmailFlag = extnCCElem
							.getAttribute("ExtnOrderConfEmailFlag");
					yfcLogCatalog.debug("strExtnOrderConfEmailFlag :: "
							+ strExtnOrderConfEmailFlag);

					if (!strExtnOrderConfEmailFlag.equalsIgnoreCase("N")) {

						nlUserGroupList = tempCustomerContact
								.getElementsByTagName("UserGroupList");
						nlUserGroupListLen = nlUserGroupList.getLength();

						yfcLogCatalog.debug(" nlUserGroupListLen :: "
								+ nlUserGroupListLen);

						for (int j = 0; j < nlUserGroupListLen; j++) {
							tempUserGroupList = (Element) nlUserGroupList
									.item(j);
							yfcLogCatalog.debug(" tempUserGroupList :: "
									+ SCXmlUtil.getString(tempUserGroupList));

							strUsergroupId = tempUserGroupList
									.getAttribute("UsergroupKey");
							yfcLogCatalog.debug("strCustomerAdminEmailList :: "
									+ strCustomerAdminEmailList);
							yfcLogCatalog.debug("strUsergroupId :: "
									+ strUsergroupId);

							if (strUsergroupId
									.equalsIgnoreCase(GROUP_BUYER_ADMIN)) {

								strCustomerAdminEmailList = strCustomerAdminEmailList
										+ ","
										+ tempCustomerContact
												.getAttribute("EmailID");

							}

						}// end for(int j=0;j<nlUserGroupListLen;j++){

					}// end if(!strExtnOrderConfEmailFlag.equalsIgnoreCase("N"))
						// {

				}// end for(int i=0;i<nlCustomerContactLen;i++){
			}
			*//** ********************************************************* **//*
			customerDoc.getDocumentElement().setAttribute(
					"strCustomerAdminEmailList", strCustomerAdminEmailList);*/

			// Make the additional email addresses chosen in Order as comma
			// separated
			//String toEmailId = customerDoc
			/**
			 * JIRA 4093 Start -If to Email address is blank or null then CC email address will be to email address
			 */
			/*String toEmailId = customerDoc.getDocumentElement().getAttribute("strToEmailid");  
			String customerAdminEmail = customerDoc.getDocumentElement().getAttribute("strCustomerAdminEmailList");
			String extnEcsr2EmailId = customerDoc.getDocumentElement().getAttribute("strExtnECsr2EMailID");
			String extnEcsr1EmailId = customerDoc.getDocumentElement().getAttribute("strExtnECsr1EMailID");
			String replaceToEmailId = "";
			*//**
			 * JIRA 4093 End -If to Email address is blank or null then CC email address will be to email address
			 */
			
			NodeList extnElementList = customerDoc.getElementsByTagName("Extn");
			int length = extnElementList.getLength();
			if (length != 0) {
				Element extnElement = (Element) extnElementList.item(0);
				String addlnEmailAddresses = SCXmlUtil.getXpathAttribute(
						extnElement, "./@ExtnAddnlEmailAddr");
				if (addlnEmailAddresses.indexOf(";") > -1) {
					addlnEmailAddresses = addlnEmailAddresses.replace(";", ",");
					yfcLogCatalog.debug("addln email addresses ::"
							+ addlnEmailAddresses);
				}
				/**
				 * JIRA 4093 Start -If to Email address is blank or null then CC email address will be to email address
				 */
				
				/*if(toEmailId == null || toEmailId.trim().equals("")){
					
					if(addlnEmailAddresses != null && addlnEmailAddresses.trim().length() > 0){
						replaceToEmailId = addlnEmailAddresses ;
						addlnEmailAddresses = "";
					}
					if(replaceToEmailId == null || replaceToEmailId.trim().length() == 0){
						replaceToEmailId = extnEcsr1EmailId ;
						extnEcsr1EmailId = "";
					}
					else if(extnEcsr1EmailId!= null && extnEcsr1EmailId.trim().length() > 0){
						replaceToEmailId = replaceToEmailId+","+extnEcsr1EmailId ;
						extnEcsr1EmailId = "";
					}
					if(replaceToEmailId == null || replaceToEmailId.trim().length() == 0){
						replaceToEmailId = extnEcsr2EmailId ;
						extnEcsr2EmailId = "";
					}
					else if(extnEcsr2EmailId!= null && extnEcsr2EmailId.trim().length() > 0){
						replaceToEmailId = replaceToEmailId+","+extnEcsr2EmailId ;
						extnEcsr2EmailId = "";
					}
					
					if(replaceToEmailId == null || replaceToEmailId.trim().length() == 0){
						replaceToEmailId = customerAdminEmail ;
						customerAdminEmail = "";
					}
					else if(customerAdminEmail!= null && customerAdminEmail.trim().length() > 0){
						replaceToEmailId = replaceToEmailId+","+customerAdminEmail ;
						customerAdminEmail = "";
					}
					if(replaceToEmailId == null)
						replaceToEmailId="";
						
					customerDoc.getDocumentElement().setAttribute("strToEmailid", replaceToEmailId);
					
				}
			*/	/**
				 * JIRA 4093 End -If to Email address is blank or null then CC email address will be to email address
				 */
				
							
				extnElement.setAttribute("ExtnAddnlEmailAddr",
						addlnEmailAddresses);
			}
			
			yfcLogCatalog.debug("XPXEmailHandlerAPI_OutXML:"+ SCXmlUtil.getString(customerDoc));
		} // End of if loop if Customer Contact list doc is empty.
		return customerDoc;
	}

	/**
	 * 
	 * @param env
	 * @param inputElement
	 * @return
	 * @throws FactoryConfigurationError
	 * @throws ParserConfigurationException
	 * @throws YIFClientCreationException
	 * @throws RemoteException
	 * @throws YFSException
	 */

	private Document getCCList(YFSEnvironment env, Element inputElement)
			throws ParserConfigurationException, FactoryConfigurationError,
			YIFClientCreationException, YFSException, RemoteException {
		
		
		// Prepare Input XML for API getCustomerContactList

		/**
		 * <CustomerContact CustomerContactID="BillToB2BUser"> <Customer
		 * CustomerID="" OrganizationCode=""/> </CustomerContact>
		 */
		Document getCCListDoc = null;

		Document docCustomerContact = SCXmlUtil.getDocumentBuilder()
				.newDocument();
		
		Element CustomerContact = docCustomerContact
				.createElement("CustomerContact");
		
		
		docCustomerContact.appendChild(CustomerContact);

		String strCustomerContactID = inputElement.getAttribute("CustomerContactID"); //Changes for JIRA 3157
		
		/*Changes done for order confirmation email - Issue Start*/
		
		//String strCustomerContactID = SCXmlUtil.getXpathAttribute(
				//inputElement, "./CustomerContact/@CustomerContactID");
		
		
		/*Changes done for order confirmation email - Issue End*/
		
		
		
		if (strCustomerContactID != null && strCustomerContactID != "") {
			CustomerContact.setAttribute("CustomerContactID",
					strCustomerContactID);	

			yfcLogCatalog.debug("input xml to getCustomerContactList api :: "
					+ SCXmlUtil.getString(docCustomerContact));
			
			
			api = YIFClientFactory.getInstance().getApi();
			env.setApiTemplate("getCustomerContactList",
					getCustomerContactListTemplate);
			
			getCCListDoc = api.getCustomerContactList(env, docCustomerContact);

			yfcLogCatalog.debug("getCCListDoc ::"
					+ SCXmlUtil.getString(getCCListDoc));
			
		}
		// invoke getCustomerContactList with template

		return getCCListDoc;
	}

	private void stampLegacyOrderNoOnCustomerOrderLine(YFSEnvironment env,
			Document customerDoc, Document orderListOutput) throws YFSException, RemoteException {
		Element customerElement = customerDoc.getDocumentElement();
		// get the legacy order no
		HashMap<String, String> legacyMap = new HashMap<String, String>();
		Map<String,String>UOMDesriptionMap = new HashMap<String,String>();
		String itemUomDescriptionMaster = "";
		String itemUomMaster = "";
		
		Document getItemUomMasterListInputDoc = YFCDocument.createDocument("ItemUOMMaster").getDocument();
    	getItemUomMasterListInputDoc.getDocumentElement().setAttribute("UnitOfMeasure","" );
    	getItemUomMasterListInputDoc.getDocumentElement().setAttribute("OrganizationCode", "");
    	env.setApiTemplate("getItemUOMMasterList", getItemUomMasterListTemplate);
    	Document getItemUomMasterListOutputDoc = api.invoke(env, "getItemUOMMasterList", getItemUomMasterListInputDoc);
    	env.clearApiTemplate("getItemUOMMasterList");
    	env.clearApiTemplate("getItemUOMMasterList");
    	NodeList itemUOMMasterList = getItemUomMasterListOutputDoc.getDocumentElement().getElementsByTagName("ItemUOMMaster");
    	if (itemUOMMasterList != null) {
        	int itemUOMLength = itemUOMMasterList.getLength();
        	if(itemUOMLength > 0) {
        		for(int i=0; i < itemUOMLength ;i++){
        		Element itemUomMasterElement = (Element) itemUOMMasterList.item(i);
        		if (itemUomMasterElement.hasAttribute("Description")) {
        		   itemUomDescriptionMaster = itemUomMasterElement.getAttribute("Description");
        		   itemUomMaster = itemUomMasterElement.getAttribute("UnitOfMeasure");
        		   } else {
        			itemUomDescriptionMaster = "";
        		}
        		UOMDesriptionMap.put(itemUomMaster, itemUomDescriptionMaster);
        		  }
        	}
        	}
    	
		/*Document inputOrderDoc = SCXmlUtil.createDocument("Order");
		Element inputOrderElement = inputOrderDoc.getDocumentElement();
		Element extnElement = inputOrderDoc.createElement("Extn");
		extnElement.setAttribute("ExtnWebConfNum", SCXmlUtil.getXpathAttribute(
				customerElement, "./Extn/@ExtnWebConfNum"));
		inputOrderElement.appendChild(extnElement);
		env.setApiTemplate("getOrderList", getOrderListTemplate);
		Document orderListDoc = api.invoke(env, "getOrderList", inputOrderDoc);
		env.clearApiTemplate("getOrderList");*/
		NodeList orderNodeList = orderListOutput.getDocumentElement().getElementsByTagName("Order");
		int orderLength = orderNodeList.getLength();
		String strOrderType = null;
		if (orderLength > 0) {
			for (int counter = 0; counter < orderLength; counter++) {
				Element orderElement = (Element) orderNodeList.item(counter);
				strOrderType = orderElement.getAttribute("OrderType");
				yfcLogCatalog.debug("strOrderType ::" + strOrderType);
				if (!strOrderType.equalsIgnoreCase("Customer")) {
					legacyMap = populateLegacyMap(env, orderElement, legacyMap);
				}

			}
		}

		legacyOnCustomer(env, customerDoc, legacyMap,UOMDesriptionMap);
	}

	private void legacyOnCustomer(YFSEnvironment env, Document customerDoc,
			HashMap<String, String> legacyMap,Map<String,String>UOMDesriptionMap) throws YFSException, RemoteException {
		String webLineNo = "";
		String legacyOrderNo = "";
		String transactionalUOM = "";
		String extnPrisingUOM = "";
		String[] valueArray =  new String[5];
		String[] unique = null ;
		HashSet<String> hs = new HashSet<String>();
		
		
		/** Added by Arun Sekhar on 31-March-2011 for Email Template **/
		String value = null;
		String extnGenerationNo = null;
		/**************************************************************/

		//Generation number and Division number logic to form order number
		//Similar to logic implemented in XPEDX Order Details Of WC
		
		Iterator<String> mapIterator = legacyMap.keySet().iterator();
		yfcLogCatalog.debug("Size" + legacyMap.size());
		while (mapIterator.hasNext()) {
			String itemUomDescription = "";
			String priceUOMDescription = "";
			webLineNo = mapIterator.next();
            
			/** Modified by Arun Sekhar on 31-March-2011 for Email Template **/
			/* legacyOrderNo = legacyMap.get(webLineNo); */
			value = legacyMap.get(webLineNo);
			
			if(value!=null && !"".equalsIgnoreCase(value))
			{
			     valueArray = value.split("\\|");
			
				 if(valueArray.length>0)
			     {
			       legacyOrderNo = valueArray[0];
			     }
			     if(valueArray.length>1)
			     {
			       extnGenerationNo = valueArray[1];
			     }
			}
			
			if(extnGenerationNo!=null && extnGenerationNo.length()>0)
			{
				if(extnGenerationNo.trim().length()==1)
				{
					extnGenerationNo="0"+extnGenerationNo;
				}					
			}
			/*****************************************************************/
			/************* Added by Arun Sekhar on 13-April-2011 *************/
			if (null != customerDivision
					&& !"".equalsIgnoreCase(customerDivision.trim())
					&& null != legacyOrderNo
					&& !"".equalsIgnoreCase(legacyOrderNo.trim())
					&& null != extnGenerationNo
					&& !"".equalsIgnoreCase(extnGenerationNo.trim())) {

					hs.add(orderNo.concat(customerDivision).concat("-")

					.concat(legacyOrderNo).concat("-")

					.concat(extnGenerationNo));

					yfcLogCatalog.debug("After concatenation - OrderNo: " + orderNo);
					

					}

			/** **************************************************************/
			
			NodeList orderLineList = customerDoc
					.getElementsByTagName("OrderLine");
			int length = orderLineList.getLength();
			if (length != 0) {
				for (int counter = 0; counter < length; counter++) {
					Element orderLineElement = (Element) orderLineList
							.item(counter);
					Element orderLineExtnElem=(Element)orderLineElement.getElementsByTagName("Extn").item(0);
					String webLine = orderLineExtnElem.getAttribute("ExtnWebLineNumber");
					 extnPrisingUOM = orderLineExtnElem.getAttribute("ExtnPricingUOM");
					
					 Element orderLineTranQtyElem=(Element)orderLineElement.getElementsByTagName("OrderLineTranQuantity").item(0);
					transactionalUOM = orderLineTranQtyElem.getAttribute("TransactionalUOM");
					if (webLineNo.equals(webLine)) {
						orderLineElement.setAttribute("LegacyOrderNo",
								legacyOrderNo);
						
						/**
						 * Added by Arun Sekhar on 31-March-2011 for Email
						 * Template
						 **/
						
						orderLineElement.setAttribute("GenerationNo",
								extnGenerationNo);
						/*****************************************************************/
					}
					if(transactionalUOM!=null && transactionalUOM!=""){
					itemUomDescription = UOMDesriptionMap.get(transactionalUOM);
					}if(extnPrisingUOM!=null && extnPrisingUOM!=""){
						priceUOMDescription = UOMDesriptionMap.get(extnPrisingUOM);
						
					}
					if(itemUomDescription!=null && !itemUomDescription.equals("")){
						orderLineTranQtyElem.setAttribute("UOMDescription",itemUomDescription);
						
					}else
					{
						orderLineTranQtyElem.setAttribute("UOMDescription",transactionalUOM);
						
					}
					if(priceUOMDescription!=null && !priceUOMDescription.equals("")){
						orderLineExtnElem.setAttribute("ExtnPricingUOMDescription",priceUOMDescription);
						
					}else{
						orderLineExtnElem.setAttribute("ExtnPricingUOMDescription",extnPrisingUOM);
						
					}
					/*if(transactionalUOM!=null && transactionalUOM!=""){
						Document getItemUomMasterListInputDoc = YFCDocument.createDocument("ItemUOMMaster").getDocument();
			        	getItemUomMasterListInputDoc.getDocumentElement().setAttribute("UnitOfMeasure", transactionalUOM);
			        	getItemUomMasterListInputDoc.getDocumentElement().setAttribute("OrganizationCode", "xpedx");
			        	env.setApiTemplate("getItemUOMMasterList", getItemUomMasterListTemplate);
			        	Document getItemUomMasterListOutputDoc = api.invoke(env, "getItemUOMMasterList", getItemUomMasterListInputDoc);
			        	env.clearApiTemplate("getItemUOMMasterList");
			        	NodeList itemUOMMasterList = getItemUomMasterListOutputDoc.getDocumentElement().getElementsByTagName("ItemUOMMaster");
			        	if (itemUOMMasterList != null) {
				        	int itemUOMLength = itemUOMMasterList.getLength();
				        	if(itemUOMLength > 0) {
				        		Element itemUomMasterElement = (Element) itemUOMMasterList.item(0);
				        		if (itemUomMasterElement.hasAttribute("Description")) {
				        			itemUomDescription = itemUomMasterElement.getAttribute("Description");
				        		} else {
				        			itemUomDescription = transactionalUOM;
				        		}
				        	}
				        	orderLineElement.setAttribute("UOMDescription",itemUomDescription);
			        	}
					}*/	
				}
			}

		}
		
		
		String orderNumber = "";

		if(hs.isEmpty()){

		orderNumber = "In Progress";

		}

		else if(!hs.isEmpty())

		{

		unique = (String[]) hs.toArray(new String[hs.size()]);

		}



		if( unique != null && unique.length > 0){

		for (int i = 0; i < unique.length; i++) {

		orderNumber = orderNumber + unique[i] + ",";

		}

		}

		orderNumber = orderNumber.substring(0, orderNumber.length() - 1);

		if (!"".equalsIgnoreCase(orderNo)) {
			/** Logic to remove the last comma **/
			formatOrderNo();
		}
		
		customerDoc.getDocumentElement().setAttribute("OrderNo", orderNumber);
		
		yfcLogCatalog.debug("customerDoc after legacyOnCustomer(): "
				+ SCXmlUtil.getString(customerDoc));
	}
	
	/** Added by Arun Sekhar on 13-April-2011 for Email templates **/
	private void formatOrderNo() {

		int strLen = orderNo.length();
		int lastIdx = strLen - 1;

		char last = orderNo.charAt(lastIdx);
		yfcLogCatalog.debug("last: " + last);
		if (last == ' ') {
			int clipIdx = lastIdx - 1;
			char clipChar = orderNo.charAt(clipIdx);
			yfcLogCatalog.debug("clipChar: " + clipChar);
			if (clipChar == ',') {
				orderNo = orderNo.substring(0, clipIdx);
			}
		}
	}

	/** ********************************************************** **/

	private HashMap<String, String> populateLegacyMap(YFSEnvironment env,
			Element orderElement, HashMap<String, String> legacyMap) {
		yfcLogCatalog.debug(" entering populateLegacyMap ");
		String webLineNo = "";
		String legacyOrderNo = "";

		/** Added by Arun Sekhar on 31-March-2011 for Email Template **/
		String value = "";
		String extnGenerationNo = "";
		/****************************/

		NodeList orderLineList = orderElement.getElementsByTagName("OrderLine");
		int orderLineLength = orderLineList.getLength();
		if (orderLineLength != 0) {
			for (int counter = 0; counter < orderLineLength; counter++) {
				Element orderLineElement = (Element) orderLineList
						.item(counter);
				webLineNo = SCXmlUtil.getXpathAttribute(orderLineElement,
						"./Extn/@ExtnWebLineNumber");
				legacyOrderNo = SCXmlUtil.getXpathAttribute(orderElement,
						"./Extn/@ExtnLegacyOrderNo");

				/*************** Added by Arun Sekhar on 31-March-2011 for Email Template **************/
				extnGenerationNo = SCXmlUtil.getXpathAttribute(orderElement,
						"./Extn/@ExtnGenerationNo");
				//extnGenerationNo="0";
				value = legacyOrderNo + "|" + extnGenerationNo;
				/****************************************************************************************/

				/** Modified by Arun Sekhar on 31-March-2011 for Email Template **/
				legacyMap.put(webLineNo, value);
			}
		}
		return legacyMap;
	}

	private Document formInputToGetCustomerOrder(YFSEnvironment env,Element inputElement) throws YFSException, RemoteException {

		yfcLogCatalog.debug("XPXEmailHandlerAPI_formInputToGetCustomerOrder():" + SCXmlUtil.getString(inputElement));
		
		Document inputOrderDoc = SCXmlUtil.createDocument("Order");
		Element inputOrderElement = inputOrderDoc.getDocumentElement();
		Element extnElement = inputOrderDoc.createElement("Extn");
		
		// To Get WebConfirmation Number
		String strWebConfirmationNumber = null;
		YFCDocument inDoc = YFCDocument.getDocumentFor(inputElement.getOwnerDocument());
		YFCElement rootElem = inDoc.getDocumentElement();
		if (rootElem != null) {
			YFCElement extnElem = rootElem.getChildElement("Extn");
			if (extnElem != null && extnElem.hasAttribute("ExtnWebConfNum")) {
				strWebConfirmationNumber = extnElem.getAttribute("ExtnWebConfNum");
			}
		}		
		
		if(yfcLogCatalog.isDebugEnabled()){
			yfcLogCatalog.debug("strWebConfirmationNumber :"+strWebConfirmationNumber);
		}
		
		if (YFCObject.isNull(strWebConfirmationNumber) || YFCObject.isVoid(strWebConfirmationNumber)) {
			YFSException emptyWebConfEx = new YFSException("WebConfirmationNumber is Null or Empty","YFSWEBConfNullOrEmpty","WebConfirmationNumber is Null or Empty");
			throw emptyWebConfEx;
		}
		
		extnElement.setAttribute("ExtnWebConfNum", strWebConfirmationNumber);
		inputOrderElement.appendChild(extnElement);
		env.setApiTemplate("getOrderList", getOrderListTemplate);
		yfcLogCatalog.debug("getOrderList_InXML :"+ SCXmlUtil.getString(inputOrderDoc));
		Document orderListDoc = api.invoke(env, "getOrderList",inputOrderDoc);
		yfcLogCatalog.debug("getOrderList_OutXML :"+ SCXmlUtil.getString(orderListDoc));
		env.clearApiTemplate("getOrderList");
		
		return orderListDoc;
	}
	
	public Document setCSREmails(YFSEnvironment env, Document customerDoc)
			throws ParserConfigurationException, FactoryConfigurationError,
			YFSException, RemoteException, YIFClientCreationException {
 
		api = YIFClientFactory.getInstance().getApi();
		
//		if(api == null){
//			yfcLogCatalog.debug(" inside null for API ");
//			api = YIFClientFactory.getInstance().getApi();
//			yfcLogCatalog.debug("api  --> " + api);
//		}
		yfcLogCatalog.debug("setCSREmails " +  SCXmlUtil.getString(customerDoc));
			 
		
		YFCDocument docCustomer = YFCDocument.createDocument("Customer");
		YFCElement inputDocElement = docCustomer.getDocumentElement();
		
		inputDocElement.setAttribute("CustomerID", customerDoc.getDocumentElement()
				.getAttribute("BillToID"));			
		
		String billToId = customerDoc.getDocumentElement().getAttribute("BillToID");
		
		
		if(billToId != null && !billToId.equalsIgnoreCase(""))
		{
			env.setApiTemplate("getCustomerList", getCustomerListCSREmailTemplate);
			yfcLogCatalog.debug("env " + env);
			yfcLogCatalog.debug("docCustomer  " + SCXmlUtil.getString(docCustomer.getDocument()));
			yfcLogCatalog.debug(" getCustomerListCSREmailTemplate " + getCustomerListCSREmailTemplate);
			//yfcLogCatalog.debug(" api " + api.toString());
			Document custListDoc = api.getCustomerList(env,docCustomer.getDocument());
			yfcLogCatalog.debug("api getCustomerList outdoc "
					+ SCXmlUtil.getString(custListDoc));
			env.clearApiTemplate("getCustomerList");
	
			// get the email ids to be sent in to and cc list ends here
			String strExtnECsr1EMailID = null;
			String strExtnECsr2EMailID = null;
			String strExtnECSR1EmailConfirmFlag = null;
			String strExtnECSR2EmailConfirmFlag = null;
	
			NodeList custNodeList = custListDoc.getElementsByTagName("Extn");
			int custLength = custNodeList.getLength();
			if (custLength != 0) {
				Element custExtnElement = (Element) custNodeList.item(0);
				strExtnECSR1EmailConfirmFlag = custExtnElement
						.getAttribute("ExtnECSR1EmailConfirmFlag");
				if (!strExtnECSR1EmailConfirmFlag.equalsIgnoreCase("N")) {
					strExtnECsr1EMailID = custExtnElement
							.getAttribute("ExtnECsr1EMailID");
					yfcLogCatalog.debug("strExtnECsr1EMailID ::"
							+ strExtnECsr1EMailID);
					customerDoc.getDocumentElement().setAttribute(
							"strExtnECsr1EMailID", strExtnECsr1EMailID);
				}
				strExtnECSR2EmailConfirmFlag = custExtnElement
						.getAttribute("ExtnECSR2EmailConfirmFlag");
				if (!strExtnECSR2EmailConfirmFlag.equalsIgnoreCase("N")) {
					strExtnECsr2EMailID = custExtnElement
							.getAttribute("ExtnECsr2EMailID");
					yfcLogCatalog.debug("strExtnECsr2EMailID ::"
							+ strExtnECsr2EMailID);
					customerDoc.getDocumentElement().setAttribute(
							"strExtnECsr2EMailID", strExtnECsr2EMailID);
				}
	
			}
			
			NodeList parentCustNodeList = custListDoc.getElementsByTagName("ParentCustomer");
			int parentCustLength = parentCustNodeList.getLength();
			if (parentCustLength != 0) {
				
				Element parentCustExtnElement = (Element) parentCustNodeList.item(0);
				String strParentCustExtn = parentCustExtnElement.getAttribute("CustomerKey");			
				customerDoc.getDocumentElement().setAttribute("parentCustomerKey",strParentCustExtn);
				
			}
		}
		return customerDoc;
	}
	
	public Document getSalesRepEmail(YFSEnvironment env,
			Document inputDocument) throws YFSException, RemoteException, YIFClientCreationException
	{
		api = YIFClientFactory.getInstance().getApi();
		yfcLogCatalog.debug("getSalesRepEmail " +  SCXmlUtil.getString(inputDocument));
		String salesRepEmail = "";
		
		YFCDocument inputDoc = YFCDocument.createDocument("Customer");
		YFCElement element = inputDoc.getDocumentElement();
		String parentCustomerKey = inputDocument.getDocumentElement().getAttribute("parentCustomerKey");
		yfcLogCatalog.debug("parentCustomerKey " +parentCustomerKey);
		if(parentCustomerKey != null && !parentCustomerKey.equalsIgnoreCase(""))
		{
			element.setAttribute("CustomerKey", parentCustomerKey);
			yfcLogCatalog.debug(" inputDoc.getDocument() " + inputDoc.getDocument());
			env.setApiTemplate("getCustomerList", getSalesRepListTemplate);
			yfcLogCatalog.debug("getSalesRepEmail before Invoke.. " +  SCXmlUtil.getString(inputDoc.getDocument()));
			
			
			Document outputDoc = api.getCustomerList(env,inputDoc.getDocument());
			env.clearApiTemplate("getCustomerList");
	
			NodeList nodeList  = outputDoc.getElementsByTagName("XPEDXSalesRep");  
			int salesRepLength = nodeList.getLength();
			List<String> salesRepUserKeys = new ArrayList<String>();
			
			if (salesRepLength > 1) {
				for (int counter = 1; counter < salesRepLength ; counter++) {
					Element salesRep = (Element) nodeList.item(counter);
					salesRepUserKeys.add(salesRep.getAttribute("SalesUserKey"));
				}
			}
		   if(salesRepLength > 1)
			{
				YFCDocument inputXML = YFCDocument.getDocumentFor("<User>"+
					"<ComplexQuery Operation=\"AND\">" +
					"<Or>" +
					"</Or>" +
					"</ComplexQuery>" +
					"</User>");
					
					YFCElement complexQryEle = inputXML.getDocumentElement().getChildElement("ComplexQuery");
					YFCElement ele = complexQryEle.getChildElement("Or");
	
					Iterator<String> itr = salesRepUserKeys.iterator();
					while(itr.hasNext()) {
						String itemId = (String)itr.next();
						YFCElement expEle = inputXML.getOwnerDocument().createElement("Exp");
						expEle.setAttribute("Name", "UserKey");
						expEle.setAttribute("Value", itemId);
						ele.appendChild(expEle);
					}
					
					env.setApiTemplate("getUserList", getUserListTemplate);
					Document outputDoc1 = api.invoke(env, "getUserList", inputXML.getDocument());
					env.clearApiTemplate("getUserList");
	
					
					NodeList nodeList1  = outputDoc1.getElementsByTagName("ContactPersonInfo");
					int contactPersonInfoLength = nodeList1.getLength();
					if (contactPersonInfoLength > 1) {
						for (int counter = 1; counter < salesRepLength ; counter++) {
							Element contactPersonInfo1 = (Element) nodeList1.item(counter);
							if(contactPersonInfo1 != null && contactPersonInfo1.getAttribute("EmailID") != null)
								//salesRepEmail = salesRepEmail + ";"+contactPersonInfo1.getAttribute("EmailID");
								/*Make the changes on 10/10/2011 start */
								salesRepEmail = salesRepEmail + ";"+SCXmlUtil.getXpathAttribute(
										contactPersonInfo1, "./@EMailID");
							/*Make the changes on 10/10/2011 End*/
							
							}
						}
			}
		}		
		//temp added by ritesh to test.
		yfcLogCatalog.debug(" salesRepEmail " + salesRepEmail);
		if(salesRepEmail != null )
		{
			inputDocument.getDocumentElement().setAttribute("salesRepEmail", salesRepEmail);
			
		}
		
		return inputDocument ;
	}
}
package com.xpedx.nextgen.customermanagement.api;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSConnectionHolder;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

import com.xpedx.constants.XpedxConstants;
/*
 * input xml
 * <CustomerList>
	<Customer 
		EnvironmentId="" CompanyCode="" ProcessCode="" CustomerDivision="" LegacyCustomerNumber="" 
		SuffixType="" ShipToSuffix="" BillToSuffix="" CustomerOrderBranch="" ShipFromBranch=""
		CustomerStatus="" CustomerName="" BrandCode="" CustomerClass="" ServiceOptimizationCode=""
		CurrencyCode="" InvoiceDistributionMethod="" NationalAccountNumber=""
		SAPNumber="" SAPParentAccountNumber="" ShipComplete="" OrderUpdateFlag=""
		CapsId="">	
		<AddressList>
			<Address AddressLine1="" AddressLine2="" AddressLine3="" City="" State="" Country="" ZipCode=""/>
		</AddressList>
		<SalesReps>
			<SalesRep>
				<EmployeeId>
			</SalesRep>
		</SalesReps>
	</Customer>
</CustomerList>

 */

/**
 * @author mnayak-tw
 * @Modified by Prasanth Kumar M.
 *
 */
public class XPXCustomerBatchProcess implements YIFCustomApi  {

	private static YFCLogCategory log;
	private static YIFApi api = null;
	//private static String strSkip = "FALSE";
	public static final String getCustomerListTemplate = "global/template/api/getCustomerList.XPXCreateChainedOrderService.xml";
	public static final String getCustomerContactListTemplate = "global/template/api/getCustomerContactList.XPXCustomerBatchProcess.xml";
	public static final String getCustomerListForParentTemplate = "global/template/api/getCustomerList.XPXCustomerBatchProcessAPI.xml";
	public static final String getUserListTemplate = "global/template/api/getUserList.XPXCustomerBatchFeedService.xml";
	public static final String manageEntitlementRuleTemplate = "global/template/api/manageEntitlementRule.XPXCustomerBatchFeedService.xml";
	public static final String errorDescription = "Customer batch feed alert";
	public static final String errorReason = "No Parent Customer";
	public static final String exceptionType = "BusinessAlert";
	public static final String referenceType = "Extended Field";
	private boolean isCustomerActive = false;
	private ArrayList<String> arrChildCustomerIds = null;

	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		/*try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}*/
	}
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		/*String strSkipValue = arg0.getProperty("SKIP");
		if(strSkipValue != null && !"".equalsIgnoreCase(strSkipValue.trim()))
				{
					strSkip = strSkipValue;
				}*/
	}

	/**
	 * This is the method which gets invoked on the hit of the service.
	 * This forms the complete input xml for manageCustomer api
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document invokeModifyCustomer(YFSEnvironment env,Document inXML) throws Exception
	{
		/**try-catch added bu Arun.Sekhar on 21-Jan-2011
		 */
		try{

			//log.debug("inXML"+SCXmlUtil.getString(inXML));
			boolean isSapCustomerCreated = false;

			String suffixType = null;
			String shipToParentCustId = null;
			String customerDivision = null;
			String legacyCustNo = null;
			String billToSuffix = null;
			String customerID = null;
			String legacyCustomerNumber = null;
			String shipToSuffix = null;
			String masterSapCustomerId = null;
			String organizationCode = null;
			String parentCustomerOrganizationCode = null;
			String customerPOReqdFlag = null;

			YFCDocument getCustomerListInputDoc = null;
			Document getCustomerListOutputDoc = null;

			ArrayList networkIdList = new ArrayList();
			ArrayList customerAssignmentKeys = new ArrayList();

			HashMap salesRepTeam = new HashMap();
			arrChildCustomerIds=new ArrayList<String>();
			String existingMSAPNumber = null;
			String existingMSAPName = null;
			
			//JIRA 3740 Start
			String existingSAPNumber = null;
			String existingSAPName = null;
			Document documentXML = null;
			boolean masterSAPUnchanged = false;
			//JIRA 3740 End
			
			
			api = YIFClientFactory.getInstance().getApi();
			//populate the team name array list
			Document outputCustomerDoc = null;
			Element customerElement = inXML.getDocumentElement();
			//get the organis=zation code from the common code list
			
			//JIRA 3740 Start - Added attribute to verify if need to execute BuyerOrganization changes
			boolean isBuyerOrganization = false;
			//JIRA 3740 End
			
			//String organizationCode = "xpedx";
			//Ask question?
			//get the input document

			NodeList customerList = customerElement.getElementsByTagName(XPXLiterals.E_CUSTOMER);

			if(customerList.getLength()>0)
			{

				int customerListLength = customerList.getLength();
				//for each customer
				for(int customerNo = 0;customerNo<customerListLength;customerNo++)
				{
					//form the input xml
					Element custElement = (Element)customerList.item(customerNo);
					String sapCustomerId = null;
					organizationCode = getOrganizationCode(env,custElement); 
					log.debug("The organization code returned from the CommonCode list is: "+organizationCode);

					//log.debug(SCXmlUtil.getString(custElement));
					String processCode = custElement.getAttribute(XPXLiterals.A_PROCESS_CODE);
					String shipFrom = custElement.getAttribute(XPXLiterals.A_SHIP_FROM_BRANCH);
					/************Added as per new logic*****************************************/
					String masterSapAccountNumber  = custElement.getAttribute(XPXLiterals.A_SAP_PARENT_ACCOUNT_NO);
					String sapAccountNumber = custElement.getAttribute(XPXLiterals.A_SAP_NUMBER);
					String brandCode = custElement.getAttribute(XPXLiterals.A_BRAND_CODE);

					//sayan added to obtain the MSAP and SAP names START
					String strSAPName = custElement.getAttribute("SAPName");
					String strMSAPName = custElement.getAttribute("ParentSAPName");
					//sayan added to obtain the MSAP and SAP names END

					//sayan - added to check for SAP and MSAP based on suffix type START
					suffixType = custElement.getAttribute(XPXLiterals.E_SUFFIX_TYPE);
					log.debug("The suffix type is: "+suffixType);

					/******************No customer will be created if sap number is not available and it is a Bill To record***********************/
					if((sapAccountNumber == null || sapAccountNumber.trim().length() == 0 || masterSapAccountNumber == null ||
							masterSapAccountNumber.trim().length()==0) && (suffixType != null && XPXLiterals.CHAR_B.equalsIgnoreCase(suffixType.trim())))
						//sayan - added to check for SAP and MSAP based on suffix type END
					{
						YFSException exceptionMessage = new YFSException();
						exceptionMessage.setErrorDescription("There is no SAP/MSAP name/number, hence no record will be created!!!");

						prepareErrorObject(exceptionMessage, XPXLiterals.CUST_B_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inXML);	

						return outputCustomerDoc;
					}
					else
					{
						// Creating the msap and sap customer ids

						masterSapCustomerId  = "CD"+"-"+masterSapAccountNumber+"-"+"M"+"-"+brandCode+"-"+"CC";
						sapCustomerId = "CD"+"-"+sapAccountNumber+"-"+"S"+"-"+brandCode+"-"+"CC";


					}
					/*****************************************************************************/


					String envtId = custElement.getAttribute(XPXLiterals.A_ENVIRONMENT_ID);
					log.debug("The envt id from the ip xml is: "+envtId);
					String companyCode = custElement.getAttribute(XPXLiterals.A_COMPANY_CODE);
					String pricingWareHouse = custElement.getAttribute(XPXLiterals.A_PRICING_WAREHOUSE);


					legacyCustomerNumber = custElement.getAttribute(XPXLiterals.A_LEGACY_CUSTOMER_NO);

					/**************************Added by Prasanth Kumar M. as per new reqmts**************************/


					customerDivision = custElement.getAttribute(XPXLiterals.E_CUSTOMER_DIVISION);
					//log.debug("The customer division is: "+customerDivision);

					legacyCustNo = custElement.getAttribute(XPXLiterals.E_LEGACY_CUSTOMER_NO);
					//log.debug("The legacy customer no is: "+legacyCustNo);

					billToSuffix = custElement.getAttribute(XPXLiterals.E_BILL_TO_SUFFIX);
					//log.debug("The bill to suffix is: "+billToSuffix);

					shipToSuffix = custElement.getAttribute(XPXLiterals.E_SHIP_TO_SUFFIX);
					//log.debug("The ship to suffix is: "+shipToSuffix);

					customerPOReqdFlag = custElement.getAttribute("CustomerPORequiredFlag");
					log.debug("The customer PO reqd flag is: "+customerPOReqdFlag);

					/************************************************************************************************/




					if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_B))
					{
						customerID = customerDivision+"-"+legacyCustomerNumber+"-"+billToSuffix+"-"+envtId+"-" +companyCode+"-B";
					}
					else if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_S))
					{
						customerID = customerDivision+"-"+legacyCustomerNumber+"-"+shipToSuffix+"-"+envtId+"-"+companyCode+"-S";
					}

					/* sayan commented
	            //Temporarily hard coded till Customer batch feed gets fixed to include environment id
				if(envtId == null || envtId.trim().length() == 0)
				{
					envtId = "DEV";
					log.debug("The environment id is: "+envtId);
				}
					 */

					if(processCode.equalsIgnoreCase("C")){
						boolean _isExitingCustomer = checkIsCustomerAvailableInSystem(env, customerID, organizationCode);
						if(_isExitingCustomer && !isCustomerActive()){
							log.debug("The customer PO reqd flag is: "+customerPOReqdFlag);
							//activate the customer
							YFCDocument manageCustomerInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
							manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);
							manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE,organizationCode);
							manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_STATUS, "10");
							api.invoke(env, XPXLiterals.MANAGE_CUSTOMER_API, manageCustomerInputDoc.getDocument());
						}
					}

					if(processCode.equalsIgnoreCase("A")||processCode.equalsIgnoreCase("C"))
					{
						//Create an input xml
						YFCDocument inputCustomerDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
						YFCElement inputCustomerElement = formCustomerElement(
								customerID, inputCustomerDoc, organizationCode,envtId,companyCode,custElement);
						//create Extn element
						YFCElement extnElement = formExtnElement(custElement, inputCustomerDoc);
						inputCustomerElement.appendChild(extnElement);
						//JIRA 3740 Start
						boolean sapUnchanged = false ;
						
						boolean isCustomerAvaiable = checkIsCustomerAvailableInSystem(env, customerID, organizationCode);
						//JIRA 3740 End

						//This method is temporarily used to hardcode the SAP ParentAccount Number until Customer Batch Feed gets fixed
						if(processCode.equalsIgnoreCase("A"))
						{
							if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_B))
							{	 

								isSapCustomerCreated = checkIsCustomerAvailableInSystem(env, sapCustomerId, organizationCode);
								if(isSapCustomerCreated == false)
								{
									log.debug("Inside loop to create the sap and master sap customers");
									//No existing SAP customers created so have to create it

									Document manageCustomerOutputDoc = createCustomerWithSAPAccountNumber(env,sapCustomerId,masterSapCustomerId, strSAPName, strMSAPName, organizationCode,custElement);
								}

								YFCElement parentCustomerElement = inputCustomerDoc.createElement("ParentCustomer");
								parentCustomerElement.setAttribute("CustomerID", sapCustomerId);
								parentCustomerElement.setAttribute("OrganizationCode", organizationCode);
								inputCustomerElement.appendChild(parentCustomerElement);					
							}
							else if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_S))
							{

								//Invoking getCustomerList to check if there is a customer with
								//CustomerId=<Customer Division>-<Legacy Customer Number>-<Bill To Suffix>-<EnvID>-<CompanyCode> 

								shipToParentCustId = customerDivision+"-"+legacyCustNo+"-"+billToSuffix+"-"+envtId+"-"+companyCode+"-B";
								//log.debug("The shipToParentCustId is: "+shipToParentCustId);

								getCustomerListInputDoc = createGetCustomerListInput(env,shipToParentCustId,organizationCode);

								//log.debug("Invoking getCustomerList");
								env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
								getCustomerListOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc.getDocument());
								env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);

								Element getCustomerListOutputDocRoot = getCustomerListOutputDoc.getDocumentElement();
								NodeList customerElementList = getCustomerListOutputDocRoot.getElementsByTagName(XPXLiterals.E_CUSTOMER);

								if(customerElementList.getLength()>0)
								{
									//Parent customer exists

									YFCElement parentCustomerElelemnt = inputCustomerDoc.createElement(XPXLiterals.E_PARENT_CUSTOMER);
									//right now customer id is hardcoded
									parentCustomerElelemnt.setAttribute(XPXLiterals.A_CUSTOMER_ID, shipToParentCustId);
									parentCustomerElelemnt.setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);
									inputCustomerElement.appendChild(parentCustomerElelemnt);
								}
								else
								{
									//Parent customer does not exist, so create an alert

									YFSException exceptionMessage = new YFSException();
									exceptionMessage.setErrorDescription("There is no parent BillTo customer for this ShipTo, hence no record will be created!!!");

									prepareErrorObject(exceptionMessage, XPXLiterals.CUST_B_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inXML);		

									return outputCustomerDoc;
								}					       
							}
							/*Begin - Changes made by Mitesh Parikh for CR 2670*/	
						} else if(processCode.equalsIgnoreCase("C")){					
							long startTime = System.currentTimeMillis();
							Element custMSAPElement = getMSAPCustomerElement(env, customerID, organizationCode);
							if(custMSAPElement!=null){
							existingMSAPNumber = custMSAPElement.getAttribute("ExtnSAPParentAccNo");
							existingMSAPName = custMSAPElement.getAttribute("ExtnSAPParentName");
							//JIRA 3740 Start
							existingSAPNumber = custMSAPElement.getAttribute("ExtnSAPNumber");
							existingSAPName = custMSAPElement.getAttribute("ExtnSAPName");
							sapUnchanged = existingSAPNumber.trim().equals(sapAccountNumber);
							//JIRA 3740 End
							masterSAPUnchanged=existingMSAPNumber.trim().equals(masterSapAccountNumber);												
							
							
							if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_B) || suffixType.equalsIgnoreCase(XPXLiterals.CHAR_S) )
							{
								//JIRA 3740 Start
								//Currently log to CENT if the SAP is different. We are not handling this case at the moment
								if(!sapUnchanged) {
									
									YFSException exceptionMessage = new YFSException();
									exceptionMessage.setErrorDescription("SAP account number is different than the existing one. No records will be updated");

									prepareErrorObject(exceptionMessage, XPXLiterals.CUST_B_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inXML);
									return outputCustomerDoc;
								}
								//JIRA 3740 End
								//Modify SAPName if Changed
								//JIRA 3740 Start
								if (sapUnchanged && !existingSAPName.equalsIgnoreCase(strSAPName)) {
                                    
									isBuyerOrganization = updateOrganizationName (sapCustomerId,organizationCode,env,custElement,inputCustomerElement, masterSapCustomerId,"SAPName",isCustomerAvaiable,strSAPName);
									//3740 - Updating all Bill-to and Ship-to with SAPNAme Changes
									updateAllBillToandShipToWithMasterSAPAccountNumber(env, organizationCode, sapCustomerId, masterSapAccountNumber, strMSAPName,custElement,existingMSAPName,false,strSAPName);
									//3740 -  Updating all Bill-to and Ship-to for SAPname Changes						
									
								}
								// for JIRA 3740 End
								if(!masterSAPUnchanged)
								{
									boolean isAnExistingMSAP=checkIsCustomerAvailableInSystem(env, masterSapCustomerId, organizationCode);
									if(isAnExistingMSAP){
										if(!isCustomerActive()){
											//activate the customer
											YFCDocument manageCustomerInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
											manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, masterSapCustomerId);
											manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE,organizationCode);
											manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_STATUS, "10");
											api.invoke(env, XPXLiterals.MANAGE_CUSTOMER_API, manageCustomerInputDoc.getDocument());
										}
										isBuyerOrganization = updateCustomerWithMSAPAccountNumber(env, organizationCode, sapCustomerId, masterSapCustomerId, masterSapAccountNumber, strMSAPName,"C",custElement,existingMSAPName,isAnExistingMSAP,null);

									} else {
										createCustomerWithMasterSAPAccountNumber(env, masterSapCustomerId, strMSAPName, organizationCode, custElement);
										isBuyerOrganization = updateCustomerWithMSAPAccountNumber(env, organizationCode, sapCustomerId, masterSapCustomerId,masterSapAccountNumber,strMSAPName,"C",custElement,existingMSAPName,isAnExistingMSAP,null);
									}
									
									arrChildCustomerIds.add(sapCustomerId);
									
									updateAllBillToandShipToWithMasterSAPAccountNumber(env, organizationCode, sapCustomerId, masterSapAccountNumber, strMSAPName,custElement,existingMSAPName,false,strSAPName);
									long endTime = System.currentTimeMillis();
									log.info("Extra Time taken to process new code changes : ["+(endTime-startTime)+"]");

									//getChildCustomerList(env,customerID,organizationCode);

									/*Begin - Changes made by Mitesh Parikh for JIRA 3002*/
									/*Creating an input xml to send an email to ebusiness@xpedx.com for reporting the changes made in Parent SAP*/
									YFCDocument reportParentSAPChangeDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);									
									YFCElement reportParentSAPChangeCustElement = reportParentSAPChangeDoc.getDocumentElement();
									reportParentSAPChangeCustElement.setAttribute(XPXLiterals.A_SELLER_ORGANIZATION_CODE, organizationCode);
									reportParentSAPChangeCustElement.setAttribute(XPXLiterals.A_ENVIRONMENT_ID, envtId);
									reportParentSAPChangeCustElement.setAttribute(XPXLiterals.A_COMPANY_CODE, companyCode);
									String custRecordType="";
									if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_B))
									{
										custRecordType = "Bill-To";
									}
									else if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_S))
									{
										custRecordType = "Ship-To";
									}
									reportParentSAPChangeCustElement.setAttribute(XPXLiterals.A_CUSTOMER_RECORD_TYPE, custRecordType);
									reportParentSAPChangeCustElement.setAttribute(XPXLiterals.A_SUFFIX_TYPE, suffixType);
									reportParentSAPChangeCustElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);								
									reportParentSAPChangeCustElement.setAttribute(XPXLiterals.OLD_SAP_PARENT_ACCOUNT_NO, existingMSAPNumber);
									reportParentSAPChangeCustElement.setAttribute(XPXLiterals.OLD_SAP_PARENT_NAME, existingMSAPName);
									reportParentSAPChangeCustElement.setAttribute(XPXLiterals.NEW_SAP_PARENT_ACCOUNT_NO, masterSapAccountNumber);
									reportParentSAPChangeCustElement.setAttribute(XPXLiterals.NEW_SAP_PARENT_NAME, strMSAPName);
									
									YFCElement reportParentSAPChangeUsersElement = reportParentSAPChangeDoc.createElement(XPXLiterals.A_USERS);
									YFCElement reportParentSAPChangeChildUserElement = null;
									/*End - Changes made by Mitesh Parikh for JIRA 3002*/
									
									String existingMSAPId = "CD"+"-"+existingMSAPNumber+"-"+"M"+"-"+brandCode+"-"+"CC";
									Document ccDoc = getCustomerContactList(env, existingMSAPId);
									Element ccElem = ccDoc.getDocumentElement();
									NodeList childList = ccElem.getChildNodes();																		
									for(int counter = 0; counter < childList.getLength(); counter ++) {
										Element childElem =(Element)childList.item(counter); 
										String userId = childElem.getAttribute("UserID");
										/*Begin - Changes made by Mitesh Parikh for JIRA 3002*/
										reportParentSAPChangeChildUserElement = reportParentSAPChangeDoc.createElement(XPXLiterals.E_USER);
										reportParentSAPChangeChildUserElement.setAttribute(XPXLiterals.A_USER_ID, userId);
										reportParentSAPChangeUsersElement.appendChild(reportParentSAPChangeChildUserElement);
										/*End - Changes made by Mitesh Parikh for JIRA 3002*/
										//arrUserList.add(userId);
										ArrayList<String> arrUserAssgnList = new ArrayList<String>();
										
										Document assgnDoc = getCustomerAssignmentList(env, userId);
										Element custAssgnElem = assgnDoc.getDocumentElement();
										NodeList assgnNodeList = custAssgnElem.getElementsByTagName("Customer");
										for(int counter1=0; counter1 < assgnNodeList.getLength(); counter1++) {
											Element custElem = (Element)assgnNodeList.item(counter1);
											arrUserAssgnList.add(custElem.getAttribute("CustomerID") );
										}
										Collection retainAllCollection = CollectionUtils.retainAll(arrUserAssgnList, arrChildCustomerIds);
										
										if(!arrUserAssgnList.isEmpty() && !retainAllCollection.isEmpty()
												&& retainAllCollection.size() <= arrUserAssgnList.size()) {
											//Remove the assignments
											
											Collection intersection = CollectionUtils.intersection(arrUserAssgnList, arrChildCustomerIds);
											Iterator iterCustId = intersection.iterator();
											while(iterCustId.hasNext()){
												String assgnCustId = (String) iterCustId.next();

												YFCDocument manageCustomerAssgnInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER_ASSIGNMENT);
												manageCustomerAssgnInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, assgnCustId);
												manageCustomerAssgnInputDoc.getDocumentElement().setAttribute("IgnoreOrdering","Y");
												manageCustomerAssgnInputDoc.getDocumentElement().setAttribute("Operation", "Delete");
												manageCustomerAssgnInputDoc.getDocumentElement().setAttribute("OrganizationCode", existingMSAPId);
												manageCustomerAssgnInputDoc.getDocumentElement().setAttribute("UserId", userId);
												
												api.invoke(env, XPXLiterals.MANAGE_CUSTOMER_ASSIGNMENT_API, manageCustomerAssgnInputDoc.getDocument());												
											}											
										} 
										
										if(!arrUserAssgnList.isEmpty() && !retainAllCollection.isEmpty()
												&& retainAllCollection.size() == arrUserAssgnList.size()) {
											//Move the login
											log.info("Following logins have no assignments ---");
											log.info("Login ID ---" + userId);
											log.info("Old MSAP hierarchy ---" + existingMSAPId);
											log.info("New MSAP hierarchy ---" + masterSapCustomerId);
										}
									}
									/*Begin - Changes made by Mitesh Parikh for JIRA 3002*/																		
									reportParentSAPChangeCustElement.appendChild(reportParentSAPChangeUsersElement);
									/* Making an asynchronous call to a new service to report Parent SAP changes. 
									 * Below mentioned service just puts the xml in a Weblogic JMS which in turn forwards it to
									 * 'XPXReportParentSAPChanges' service.
									 */	
									
									api.executeFlow(env, "XPXPutParentSAPChangesInQueue", reportParentSAPChangeDoc.getDocument());	
									/*End - Changes made by Mitesh Parikh for JIRA 3002*/	
									
								 }	
									//JIRA 3740 - Start
									//Modify MSAPName if Changed in input xml
									if (masterSAPUnchanged && !existingMSAPName.equalsIgnoreCase(strMSAPName)) {
										isBuyerOrganization = updateOrganizationName (masterSapCustomerId,organizationCode,env,custElement,inputCustomerElement, masterSapCustomerId,"ParentSAPName",isCustomerAvaiable,strMSAPName);
										//3740 - Updating all Bill-to and Ship-to with SAPNAme Changes
										updateAllBillToandShipToWithMasterSAPAccountNumber(env, organizationCode, sapCustomerId, masterSapAccountNumber, strMSAPName,custElement,existingMSAPName,false,strSAPName);
										//3740 -  Updating all Bill-to and Ship-to for SAPname Changes
		
									}
									//JIRA 3740 - End
		
	
								}

							
							}else{
								YFSException exceptionMessage = new YFSException();
								exceptionMessage.setErrorDescription("MSAP account number is different than the existing one. No records in Database exist for this MSAP");

								prepareErrorObject(exceptionMessage, XPXLiterals.CUST_B_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inXML);
								return outputCustomerDoc;
							}
						}				
						/*End - Changes made by Mitesh Parikh for CR 2670*/
						//create XPEDXSalesRep element
						/**************************************/
						HashSet<String> salesRepSet = new HashSet<String>();
						if(processCode.equalsIgnoreCase("A")|| processCode.equalsIgnoreCase("C"))
						{
							YFCElement salesRepListElement = inputCustomerDoc.createElement(XPXLiterals.E_XPEDX_SALES_REP_LIST);

							//sayan added to handle the requirement that sales rep is not mandatory on customer batch feed START
							NodeList nlSalesReps = custElement.getElementsByTagName(XPXLiterals.E_SALES_REPS);

							if(nlSalesReps.getLength() != 0)
							{
								//sayan added to handle the requirement that sales rep is not mandatory on customer batch feed END
								Element inputXMLSalesRepListElement = (Element)nlSalesReps.item(0);

								if(inputXMLSalesRepListElement!=null)
								{
									NodeList nlInputXMLSalesRep = inputXMLSalesRepListElement.getElementsByTagName(XPXLiterals.E_SALES_REP);

									if(nlInputXMLSalesRep.getLength()!= 0)
									{
										/*sayan commented
								  Element eInputXMLSalesRep = (Element)nlInputXMLSalesRep.item(0);
										 */
										for(int i=0; i<nlInputXMLSalesRep.getLength(); i++)
										{
											ArrayList networkIdSalesRepList = new ArrayList();
											Element inputXMLSalesRepElement = (Element) nlInputXMLSalesRep.item(i);
											//Element inputXMLEmployeeElement = (Element)inputXMLSalesRepElement.getElementsByTagName(XPXLiterals.E_EMPLOYEE_ID).item(0);
											String  inputXMLEmployeeId = inputXMLSalesRepElement.getAttribute("EmployeeId");
											//fix
											ArrayList<String> userDetails = checkUserExists(env, inputXMLEmployeeId);
											//boolean userExists = checkUserExists(env, inputXMLEmployeeId);
											String userExists = userDetails.get(0);

											/********User Exists check commented out by Prasanth Kumar M. as per review comments on 02/08/2011**********/
											//if(userExists.equals("true"))
											//{
											if(!salesRepSet.contains(inputXMLEmployeeId))
											{
												salesRepSet.add(inputXMLEmployeeId);
												if(!inputXMLEmployeeId.equals("0000"))
												{
													//ArrayList<String> userList = new ArrayList<String>();

													if(inputXMLEmployeeId == null || "".equals(inputXMLEmployeeId.trim()))
													{
														continue;
													}
													//replace 
													//networkIdSalesRepList = invokeGetUserList(env,inputXMLEmployeeId) ;

													//log.debug("The network id retrieved: "+networkId);
													//Only For testing purposes
													/*if(i==1)
									{
										networkId = "pnairSecond";
									}*/


													YFCElement salesRepElement = inputCustomerDoc.createElement(XPXLiterals.E_XPEDX_SALES_REP);
													salesRepElement.setAttribute(XPXLiterals.A_SALES_REP_ID, inputXMLEmployeeId);

													// Start - changes made on 15/02/2011								        								        
													salesRepElement.setAttribute(XPXLiterals.MSAP_CUSTOMER_NAME, strMSAPName);
													salesRepElement.setAttribute(XPXLiterals.MSAP_CUSTOMER_NUMBER,masterSapAccountNumber);								        
													// End - changes made on 15/02/2011								        								        

													// userList = getNetworkID(env,inputXMLEmployeeId);
													//fix
													int userLength = userDetails.size();
													if(userLength > 1)
													{
														//replace
														//salesRepElement.setAttribute(XPXLiterals.A_NETWORK_ID, (String)networkIdSalesRepList.get(0));
														//salesRepElement.setAttribute(XPXLiterals.A_NETWORK_ID, "networkID");
														salesRepElement.setAttribute(XPXLiterals.A_NETWORK_ID, userDetails.get(1));
														salesRepElement.setAttribute("SalesUserKey", userDetails.get(2));
													}
													else
													{
														//code to be sent to the CENT tool
														//to do
														log.debug("EmployeeID  is not populated");

													}
													salesRepElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);

													if(i==0)
													{
														//PrimarySalesRep
														salesRepElement.setAttribute(XPXLiterals.A_PRIMARY_SALES_REP_FLAG,XPXLiterals.BOOLEAN_FLAG_Y);
													}
													salesRepListElement.appendChild(salesRepElement);


													//replace
													/*if(networkIdSalesRepList.size()>0)
										{
										  networkIdList.add((String)networkIdSalesRepList.get(0));	
										  salesRepTeam.put((String)networkIdSalesRepList.get(0), (String)networkIdSalesRepList.get(1));
										}*/
												}       

											}
											//}
										}
									}
								}
							}

							if(processCode.equalsIgnoreCase("C"))
							{
								salesRepListElement.setAttribute(XPXLiterals.A_RESET, XPXLiterals.BOOLEAN_FLAG_Y);

							}
							extnElement.appendChild(salesRepListElement);


						}

						/*********Added by Prasanth Kumar M. for CR 968****************************/

						if("Y".equalsIgnoreCase(customerPOReqdFlag) && suffixType.equalsIgnoreCase(XPXLiterals.CHAR_B))
						{
							//If flag is set as Y, add the RequiredCustomerPO rule for the bill to customer(after first checking if its available)

							//Get the rule key from the Rules table

							Document getRuleDefnInputDoc = YFCDocument.createDocument("XPXRuleDefn").getDocument();
							getRuleDefnInputDoc.getDocumentElement().setAttribute("RuleID", "RequireCustomerPO");
							log.debug("The input xml to XPXGetRuleDefnList is: "+SCXmlUtil.getString(getRuleDefnInputDoc));
							Document getRuleDefnOutputDoc = api.executeFlow(env, "XPXGetRuleDefnList", getRuleDefnInputDoc);
							log.debug("The output xml to XPXGetRuleDefnList is: "+SCXmlUtil.getString(getRuleDefnOutputDoc));

							if(getRuleDefnOutputDoc.getDocumentElement().getElementsByTagName("XPXRuleDefn").getLength() > 0)
							{
								//Rule exists in table

								Element ruleDefnElement = (Element) getRuleDefnOutputDoc.getDocumentElement().getElementsByTagName("XPXRuleDefn").item(0);	
								String ruleKey = ruleDefnElement.getAttribute("RuleKey");

								//Check if rule is already applied for customer
								Document getCustomerProfileRuleInputDoc = YFCDocument.createDocument("XPXCustomerRulesProfile").getDocument();
								getCustomerProfileRuleInputDoc.getDocumentElement().setAttribute("CustomerID", customerID);
								getCustomerProfileRuleInputDoc.getDocumentElement().setAttribute("RuleKey",ruleKey);
								log.debug("The input xml to XPXGetCustomerProfileRuleList is: "+SCXmlUtil.getString(getCustomerProfileRuleInputDoc));
								Document getCustomerProfileRuleOutputDoc = api.executeFlow(env, "XPXGetCustomerProfileRuleList", getCustomerProfileRuleInputDoc);
								log.debug("The output xml to XPXGetCustomerProfileRuleList is: "+SCXmlUtil.getString(getCustomerProfileRuleOutputDoc));

								if(getCustomerProfileRuleOutputDoc.getDocumentElement().getElementsByTagName("XPXCustomerRulesProfile").getLength()<=0)
								{
									//Rule needs to be applied
									YFCElement customerProfileRuleListElement = inputCustomerDoc.createElement("XPXCustomerRulesProfileList");
									YFCElement customerProfileRuleElement = inputCustomerDoc.createElement("XPXCustomerRulesProfile");
									customerProfileRuleElement.setAttribute("CustomerID", customerID);
									customerProfileRuleElement.setAttribute("RuleKey",ruleKey);
									customerProfileRuleElement.setAttribute("OrganizationCode",organizationCode);
									customerProfileRuleListElement.appendChild(customerProfileRuleElement);

									extnElement.appendChild(customerProfileRuleListElement);
								}

							}
							else
							{
								log.debug("There is no RequiredCustomerPO rule in the system");
							}


						}
						else if("N".equalsIgnoreCase(customerPOReqdFlag) && suffixType.equalsIgnoreCase(XPXLiterals.CHAR_B))
						{
							//If flag is set as N, delete the RequiredCustomerPO rule for the bill to customer(after first checking if its available)

							//Get the rule key from the Rules table

							Document getRuleDefnInputDoc = YFCDocument.createDocument("XPXRuleDefn").getDocument();
							getRuleDefnInputDoc.getDocumentElement().setAttribute("RuleID", "RequireCustomerPO");
							log.debug("The input xml to XPXGetRuleDefnList is: "+SCXmlUtil.getString(getRuleDefnInputDoc));
							Document getRuleDefnOutputDoc = api.executeFlow(env, "XPXGetRuleDefnList", getRuleDefnInputDoc);
							log.debug("The output xml to XPXGetRuleDefnList is: "+SCXmlUtil.getString(getRuleDefnOutputDoc));

							if(getRuleDefnOutputDoc.getDocumentElement().getElementsByTagName("XPXRuleDefn").getLength() > 0)
							{
								//Rule exists, so retrieve the rule key

								Element ruleDefnElement = (Element) getRuleDefnOutputDoc.getDocumentElement().getElementsByTagName("XPXRuleDefn").item(0);	
								String ruleKey = ruleDefnElement.getAttribute("RuleKey");

								//Check if rule is already removed for customer

								Document getCustomerProfileRuleInputDoc = YFCDocument.createDocument("XPXCustomerRulesProfile").getDocument();
								getCustomerProfileRuleInputDoc.getDocumentElement().setAttribute("CustomerID", customerID);
								getCustomerProfileRuleInputDoc.getDocumentElement().setAttribute("RuleKey",ruleKey);
								log.debug("The input xml to XPXGetCustomerProfileRuleList is: "+SCXmlUtil.getString(getCustomerProfileRuleInputDoc));
								Document getCustomerProfileRuleOutputDoc = api.executeFlow(env, "XPXGetCustomerProfileRuleList", getCustomerProfileRuleInputDoc);
								log.debug("The output xml to XPXGetCustomerProfileRuleList is: "+SCXmlUtil.getString(getCustomerProfileRuleOutputDoc));

								if(getCustomerProfileRuleOutputDoc.getDocumentElement().getElementsByTagName("XPXCustomerRulesProfile").getLength()>0)
								{
									//Rule exists for customer...so remove the rule

									Element customerProfileRuleElement = (Element) getCustomerProfileRuleOutputDoc.getDocumentElement().getElementsByTagName("XPXCustomerRulesProfile").item(0);
									String customerProfileRuleKey = customerProfileRuleElement.getAttribute("CustomerRuleProfileKey");

									Document deleteCustomerProfileRuleInputDoc = YFCDocument.createDocument("XPXCustomerRulesProfile").getDocument();
									deleteCustomerProfileRuleInputDoc.getDocumentElement().setAttribute("CustomerRuleProfileKey", customerProfileRuleKey);
									log.debug("The output xml to XPXDeleteCustomerProfileRule is: "+SCXmlUtil.getString(deleteCustomerProfileRuleInputDoc)); 
									api.executeFlow(env, "XPXDeleteCustomerProfileRule", deleteCustomerProfileRuleInputDoc);

								}

							}
						}

						//JIRA 3740 - Start [Added Condition to Check if BuyerOrganization element already modified]
						YFCElement buyerOrgElement = null;
						//if(!isBuyerOrganization){
						buyerOrgElement = formBUyerOrgElement(custElement, inputCustomerDoc,
								inputCustomerElement, sapCustomerId, organizationCode,"CustomerName",isCustomerAvaiable,null);
						inputCustomerElement.appendChild(buyerOrgElement);
						//updateAddressbook(custElement,buyerOrgElement,inputCustomerDoc,suffixType,outputCustomerDoc,env);
						//get the address element
						NodeList addressList = custElement.getElementsByTagName("AddressList");
						int addressListLength = addressList.getLength();

						if(addressListLength>0)
						{
							Element addressNodeElement = (Element)addressList.item(0);
							//log.debug(SCXmlUtil.getString(addressNodeElement));
							YFCElement billingAddressElement = formBillingPersonInfoElement( addressNodeElement, inputCustomerDoc);
							YFCElement contactAddressElement = formContactPersonInfoElement( addressNodeElement, inputCustomerDoc);

							/******************************Fix for Bug#11699 by Prasanth Kumar M.*********************************/

							YFCElement customerAdditionalAddressElement = formAdditionalAddressElement(addressNodeElement, inputCustomerDoc,suffixType);
							inputCustomerDoc.getDocumentElement().appendChild(customerAdditionalAddressElement);

							/****************************************************************************************************/

							buyerOrgElement.appendChild(billingAddressElement);
							buyerOrgElement.appendChild(contactAddressElement);

						}
						
						log.debug("the input doc for manageCustomer formed is: "+ SCXmlUtil.getString(inputCustomerDoc.getDocument()));
						//invoke manageCustomer

						outputCustomerDoc = api.invoke(env, "manageCustomer", inputCustomerDoc.getDocument());

						//}
						//JIRA 3740 - End
					
						//Added:mnayak
						//create a user at the MSAP level and assign the user to customer
						//first time create it second time just assigning
						//if(processCode.equalsIgnoreCase("A"))
						//{
						//createMasterUser(env, customerID, suffixType);
						//}

						//new requirement
						HashSet<String> salesRepSetForUser = new HashSet<String>();
						if(processCode.equalsIgnoreCase("A")||processCode.equalsIgnoreCase("C"))
						{
							NodeList nlSalesReps = custElement.getElementsByTagName(XPXLiterals.E_SALES_REPS);
							if(nlSalesReps.getLength() != 0)
							{
								//sayan added to handle the requirement that sales rep is not mandatory on customer batch feed END
								Element inputXMLSalesRepListElement = (Element)nlSalesReps.item(0);

								if(inputXMLSalesRepListElement!=null)
								{
									NodeList nlInputXMLSalesRep = inputXMLSalesRepListElement.getElementsByTagName(XPXLiterals.E_SALES_REP);

									if(nlInputXMLSalesRep.getLength()!= 0)
									{
										/*sayan commented
								  Element eInputXMLSalesRep = (Element)nlInputXMLSalesRep.item(0);
										 */
										for(int i=0; i<nlInputXMLSalesRep.getLength(); i++)
										{
											ArrayList networkIdSalesRepList = new ArrayList();
											Element inputXMLSalesRepElement = (Element) nlInputXMLSalesRep.item(i);
											String salesRep = inputXMLSalesRepElement.getAttribute("EmployeeId");
											if(!salesRepSetForUser.contains(salesRep))
											{
												salesRepSetForUser.add(salesRep);

												//check if the user exists .if user does not exist then create it and asign it to customer
												if(!salesRep.equals("0000"))
												{
													//check if this user exists in User table
													//boolean userExists = checkUserExists(env,salesRep);
													ArrayList<String> userDetails = checkUserExists(env,salesRep);
													String userExists = userDetails.get(0);

													/********User Exists check commented out by Prasanth Kumar M. as per review comments on 02/08/2011**********/
													//if(userExists.equals("true"))
													//{
													createMasterUser(env, customerID, suffixType, salesRep);
													//}
												}
											}
										}
									}
								}
							}
						} 


						/*
						 * Commenting out temporarily
						 */
						//assign an entitlement
						/*Begin: CR 2277
					if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_S))
					{
					   //As per business rule, no BillTo customers will have a group entitlement	
					createEntitlementForCustomer(env, shipFrom, suffixType, envtId, pricingWareHouse,processCode,customerID,organizationCode);
					}
					End: CR 2277*/

						//assign a pricelist
						createPriceListAssignmentForCustomer(env, shipFrom,envtId,companyCode,pricingWareHouse,customerID,organizationCode);

						if(processCode.equalsIgnoreCase("C"))
						{
							//get the existing assignments
							ArrayList<String> existingAssignmentKeys = new ArrayList<String>();
							existingAssignmentKeys = getCustomerAssignments(env,customerID,organizationCode,suffixType);
							if(existingAssignmentKeys.size() > 0)
							{
								deleteExistingAssignments(env,existingAssignmentKeys);
							}
							//then assign the customer to the new CSRs
							manageCustomerAssignmentforA(env,customerID,organizationCode,inXML);

						}

						else if (processCode.equalsIgnoreCase("A"))
						{
							//added mnayak
							//create team and assign the customers to the team
							manageCustomerAssignmentforA(env,customerID,organizationCode,inXML);

						}


					}
					else if(processCode.equalsIgnoreCase("D"))
					{
						//Soft Delete the customer i.e make the customer inactive by setting status="30"

						boolean isCustomerAvailableInSystem = false;

						isCustomerAvailableInSystem = checkIsCustomerAvailableInSystem(env,customerID,organizationCode);

						if(isCustomerAvailableInSystem)
						{
							YFCDocument manageCustomerInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
							manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);
							manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE,organizationCode);

							manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_STATUS, "30");

							api.invoke(env, XPXLiterals.MANAGE_CUSTOMER_API, manageCustomerInputDoc.getDocument());
						}
						else
						{
							log.error("The customer: "+customerID+" is not available for soft delete");
						}
					}

					else if("E".equalsIgnoreCase(processCode))
					{
						// Hard delete the customer i.e remove the customer details from the system

						boolean isCustomerAvailableInSystem = false;

						isCustomerAvailableInSystem = checkIsCustomerAvailableInSystem(env,customerID,organizationCode);

						if(isCustomerAvailableInSystem)
						{
							YFCDocument manageCustomerInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
							manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);
							manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);

							manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OPERATION, XPXLiterals.DELETE);

							//added:mnayak
							//get all the assignments
							ArrayList<String> existingTeamKeys = getAllCustomerAssignments(env, customerID, organizationCode, suffixType);
							deleteExistingAssignments(env, existingTeamKeys);

							api.invoke(env, XPXLiterals.MANAGE_CUSTOMER_API, manageCustomerInputDoc.getDocument());



						}
						else
						{
							log.error("The customer: "+customerID+" is not available for hard delete");
						}
					}
					else
					{
						log.error("The processCode mentioned: "+processCode+""+" is invalid!!!");
					}
					
					//3740 - Starts - Need to update MSAPName in all rows of yfs_customer table for concerned MSAP Root customer Key
					if (processCode.equalsIgnoreCase("C") && isBuyerOrganization && masterSAPUnchanged && !existingMSAPName.equalsIgnoreCase(strMSAPName)) {
						Connection connection = null;
						Statement stmt = null;
						try {
							connection = getDBConnection(env, documentXML);
							String rootCustomerKey = getCustomerKey(env, masterSapCustomerId, true);
							String query = "Update yfs_customer set extn_sap_parent_name=" + "'" + strMSAPName + "'" + " where root_customer_key=" + "'" + rootCustomerKey + "'";
							stmt = connection.createStatement();
							stmt.execute(query);
							connection.commit();
						} catch (Exception exception) {
							exception.printStackTrace();
						} 

					}
				 //3740 - Ends	
				}

			}
			return outputCustomerDoc;
		}catch (NullPointerException ne) {
			log.error("NullPointerException: " + ne.getStackTrace());	
			prepareErrorObject(ne, XPXLiterals.CUST_B_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inXML);
			throw ne;
		} catch (YFSException yfe) {
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.CUST_B_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inXML);	
			throw yfe;
		} catch (Exception e) {
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.CUST_B_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inXML);
			throw e;
		}			
	}
	


	//JIRA 3740 - Start
	/**
	 *updateOrganizationName  - Operation is called to update MSAP and SAP name based on changees 
	 * @param sapCustomerId
	 * @param organizationCode
	 * @param env
	 * @param custElement
	 * @param inputCustomerElement
	 * @param masterSapCustomerId
	 * @param sapName
	 * @param isAnExistingMSAP
	 * @param existingSAPName 
	 * @return booleanValue
	 */
	private boolean updateOrganizationName (String sapCustomerId, String organizationCode, YFSEnvironment env, Element custElement, YFCElement inputCustomerElement, String masterSapCustomerId, String sapName, boolean isAnExistingMSAP, String existingSAPName) {
		boolean returnData=true;
		YFCDocument updateSAPNameInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
		updateSAPNameInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, sapCustomerId);
		updateSAPNameInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OPERATION, "Modify");
		updateSAPNameInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);
		updateSAPNameInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE, sapCustomerId);
		YFCElement buyerOrgElement = formBUyerOrgElement(custElement, updateSAPNameInputDoc, inputCustomerElement, masterSapCustomerId, organizationCode, sapName,isAnExistingMSAP,sapCustomerId);
		updateSAPNameInputDoc.getDocumentElement().appendChild(buyerOrgElement);
		if(existingSAPName!=null && !sapName.equalsIgnoreCase("ParentSAPName")){
		YFCElement extnElement = updateSAPNameInputDoc.createElement(XPXLiterals.E_EXTN);
		extnElement.setAttribute("ExtnSAPName", existingSAPName);
		updateSAPNameInputDoc.getDocumentElement().appendChild(extnElement);
		}
		if(existingSAPName!=null && !sapName.equalsIgnoreCase("SAPName")){
			YFCElement extnElement = updateSAPNameInputDoc.createElement(XPXLiterals.E_EXTN);
			extnElement.setAttribute("ExtnSAPParentName", existingSAPName);
			updateSAPNameInputDoc.getDocumentElement().appendChild(extnElement);
		}
		log.info("updateMSAPnSAPName - update xml : " + SCXmlUtil.getString(updateSAPNameInputDoc.getDocument()));
		try {
			api.invoke(env, XPXLiterals.MANAGE_CUSTOMER_API, updateSAPNameInputDoc.getDocument());

		} catch (YFSException e) {
			// TODO Auto-generated catch block
			returnData = false;
			e.printStackTrace();
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			returnData = false;
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			returnData = false;
			e.printStackTrace();
		}
		
	  return returnData;	
	}
	//JIRA 3740 - End

	/**@author asekhar-tw on 21-Jan-2011
	 * This method prepares the error object with the exception details which in turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML){
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();	
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}

	private ArrayList<String> checkUserExists(YFSEnvironment env, String salesRep) throws YFSException, RemoteException
	{
		ArrayList<String> userDetails = new ArrayList<String>();
		String userExists = "false";
		String networkID = "";
		String userID = "";
		String userKey = "";
		Document inputUserDoc = SCXmlUtil.createDocument("User");
		Element inputUserElement = inputUserDoc.getDocumentElement();
		Element extnElement = inputUserDoc.createElement("Extn");
		extnElement.setAttribute("ExtnEmployeeId", salesRep);
		inputUserElement.appendChild(extnElement);
		Document outputUserListDoc = api.invoke(env, "getUserList", inputUserDoc);
		NodeList userList = outputUserListDoc.getElementsByTagName("User");
		int userLength = userList.getLength();
		if(userLength != 0)
		{
			userExists = "true";
			userDetails.add(userExists);
			NodeList customerContactNodeList = outputUserListDoc.getElementsByTagName("User");
			int contactLength = customerContactNodeList.getLength();
			if(contactLength != 0)
			{
				Element contactElement = (Element)customerContactNodeList.item(0);
				//networkID = SCXmlUtil.getXpathAttribute(contactElement, "./Extn/@ExtnUserNetworkID");
				networkID = contactElement.getAttribute("Loginid");
				//userID = contactElement.getAttribute("UserID");
				userDetails.add(networkID);
				//userKey = getUserKey(env,userID);
				userKey = contactElement.getAttribute("UserKey");
				userDetails.add(userKey);


			}
		}
		else
		{
			userExists = "false";
			userDetails.add(userExists);
		}

		return userDetails;
	}

	/*private ArrayList<String> getNetworkID(YFSEnvironment env, String employeeID) throws YFSException, RemoteException
		{
		ArrayList<String> ContactList = new ArrayList<String>();
		String networkID = "";
		String userID = "";
		String userKey = "";
		//form the input xml
		Document inputDoc = SCXmlUtil.createDocument("User");
		//Document inputDoc = SCXmlUtil.createDocument("User");
		Element inputElement = inputDoc.getDocumentElement();
		Element extnElement = inputDoc.createElement("Extn");
		inputElement.appendChild(extnElement);
		extnElement.setAttribute("ExtnEmployeeID", employeeID);
		Document customerContactList = api.invoke(env, "getUserList", inputDoc);
		//Document customerContactList = api.invoke(env, "getUserList", inputDoc);
		NodeList customerContactNodeList = customerContactList.getElementsByTagName("User");
		int contactLength = customerContactNodeList.getLength();
		if(contactLength != 0)
		{
			Element contactElement = (Element)customerContactNodeList.item(0);
			//networkID = SCXmlUtil.getXpathAttribute(contactElement, "./Extn/@ExtnUserNetworkID");
			networkID = contactElement.getAttribute("Loginid");
			//userID = contactElement.getAttribute("UserID");
			ContactList.add(networkID);
			//userKey = getUserKey(env,userID);
			userKey = contactElement.getAttribute("UserKey");
			ContactList.add(userKey);

		}

			return ContactList;
		}*/


	/*private String getUserKey(YFSEnvironment env, String userID) throws YFSException, RemoteException
	{
		String userKey = "";
		//form the input
		Document inputUserDoc = SCXmlUtil.createDocument("User");
		Element inputUserElement = inputUserDoc.getDocumentElement();
		inputUserElement.setAttribute("userID", userID);
		Document userListDoc = api.invoke(env, "getUserList", inputUserDoc);
		NodeList userList = userListDoc.getElementsByTagName("User");
		int userLength = userList.getLength();
		if(userLength != 0)
		{
			Element userElement = (Element)userList.item(0);
			userKey = userElement.getAttribute("UserKey");
		}
		return userKey;
	}*/
	//method to create a master user for the Customer coming in at the MSAP level and assign this 
	//customer to the master user
	private void createMasterUser(YFSEnvironment env, String customerID, String suffixType,String salesRepId) throws YFSException, RemoteException
	{
		ArrayList<String> MSAPList = new ArrayList<String>();
		String rootCustomerKey = "";
		String customerName ="";
		String masterCustomerId = "";
		//get the root customer key
		rootCustomerKey = getRootCustomerKey(env, customerID);
		//get the customer name
		MSAPList = getCustomerNameForMSAP(env,rootCustomerKey);
		customerName = MSAPList.get(0);
		masterCustomerId = MSAPList.get(1);
		//String masterCustomerUser = "MasterUser"+"@"+customerName+".com";
		/* changes made on 15/02/2011 
		 *  Master customer user has been formed with master customer id and not with customer name.
		 * */
		String masterCustomerUser = salesRepId+"@"+masterCustomerId+".com";

		//check if the mastercustomer user already exists
		//requirement change
		boolean masterCustomerexists = false;
		masterCustomerexists = checkForMasterCustomer(env, masterCustomerUser);
		/*if(masterCustomerexists)
		{
			//then assign this user to the customer
			assignMasterCustomerUserToCustomer(env, customerID,masterCustomerUser, suffixType,masterCustomerId);
		}*/
		if(!masterCustomerexists)
		{
			//create the master customer user and assign it to customer
			//createMaterCustomer
			createMasterCustomer(env, masterCustomerUser,rootCustomerKey,customerID);
			assignMasterCustomerUserToCustomer(env, customerID,masterCustomerUser, suffixType,masterCustomerId);
		}

	}


	//method to create master customer user
	private void createMasterCustomer(YFSEnvironment env, String masterCustomerUser, String customerKey, String customerId) throws YFSException, RemoteException
	{
		
		/*** Start of COde for JIra 3838 *********/
		//form the input 
		Document inputPwdPolicyDoc =SCXmlUtil.createDocument("PasswordPolicy");
		//<PasswordPolicy CallingOrganizationCode="" Description="" Name="" OrganizationCode="" PasswordPolicyKey="" Priority="" Status=""/>
		Element inputPwdPolicyElement=inputPwdPolicyDoc.getDocumentElement();
		inputPwdPolicyElement.setAttribute("Name",XpedxConstants.SalesRep_Password_Policy);
		inputPwdPolicyElement.setAttribute("OrganizationCode","xpedx");
		
		
		//System.out.println("inputPwdPolicyDoc:::"+SCXmlUtil.getString(inputPwdPolicyDoc));
		Document pwdPolicyDetailDoc =api.invoke(env, "getPasswordPolicyDetails", inputPwdPolicyDoc);
		
		//System.out.println("OutputPwdPolicyDoc"+SCXmlUtil.getString(pwdPolicyDetailDoc));
		
		String pwdPolicyKey = null;
		if(null!=pwdPolicyDetailDoc){
			NodeList policyElemsList = pwdPolicyDetailDoc.getElementsByTagName("PasswordPolicy");
			Element policyElem;
			if( policyElemsList!=null){
				policyElem = (Element)policyElemsList.item(0);
				pwdPolicyKey = policyElem.getAttribute("PasswordPolicyKey");
				
				//System.out.println("pwdPolicyKey is :: "+pwdPolicyKey);
				
			}
		}
		
		
		
		/*** End of COde for JIra 3838 *********/
		
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerKey", customerKey);
		inputCustomerElement.setAttribute("Operation", "Modify");
		Element inputContactListElement = inputCustomerDoc.createElement("CustomerContactList");
		inputCustomerElement.appendChild(inputContactListElement);
		Element inputContactElement = inputCustomerDoc.createElement("CustomerContact");
		inputContactElement.setAttribute("Operation", "");
		inputContactElement.setAttribute("CustomerContactID", masterCustomerUser);
		inputContactListElement.appendChild(inputContactElement);
		Element extnElement = inputCustomerDoc.createElement("Extn");
		extnElement.setAttribute("ExtnIsSalesRep", "Y");
		/*Begin - Changes made by Mitesh Parikh for JIRA# 2856 */
		extnElement.setAttribute("ExtnViewReportsFlag", "Y");
		extnElement.setAttribute("ExtnB2BCatalogView", "3");
		/*End - Changes made by Mitesh Parikh for JIRA# 2856 */
		inputContactElement.appendChild(extnElement);
		Element userElement = inputCustomerDoc.createElement("User");
		inputContactElement.appendChild(userElement);
		userElement.setAttribute("Activateflag", "Y");
		userElement.setAttribute("PasswordPolicyKey",pwdPolicyKey);//modified code for jira 3838
		
		userElement.setAttribute("Loginid", masterCustomerUser);
		userElement.setAttribute("Password", masterCustomerUser);
		userElement.setAttribute("CreatorOrganizationKey", customerId);
		userElement.setAttribute("Localecode", "en_US_EST");
		Element userGroupsElement = inputCustomerDoc.createElement("UserGroupLists");
		userElement.appendChild(userGroupsElement);
		Element userGroupElement = inputCustomerDoc.createElement("UserGroupList");
		userGroupElement.setAttribute("UsergroupKey", "BUYER-ADMIN");
		Element userGroupElement1 = inputCustomerDoc.createElement("UserGroupList");
		userGroupElement1.setAttribute("UsergroupKey", "BUYER-USER");
		userGroupsElement.appendChild(userGroupElement);
		userGroupsElement.appendChild(userGroupElement1);
		log.debug("inputCustomerDoc"+SCXmlUtil.getString(inputCustomerDoc));
		
	//	System.out.println("inputCustomerDoc"+SCXmlUtil.getString(inputCustomerDoc));
		api.invoke(env, "manageCustomer", inputCustomerDoc);
	}


	//method to assign master customer user to the customer
	private void assignMasterCustomerUserToCustomer(YFSEnvironment env, String customerID, String masterCustomerUser, String suffixType,String masterCustomerId) throws YFSException, RemoteException
	{
		//form the input doc
		Document inputAssignmentDoc = SCXmlUtil.createDocument("CustomerAssignment");
		Element inputAssignmentElement = inputAssignmentDoc.getDocumentElement();
		inputAssignmentElement.setAttribute("CustomerID", customerID);
		inputAssignmentElement.setAttribute("UserId", masterCustomerUser);
		inputAssignmentElement.setAttribute("Operation", "Create");
		if(suffixType.equals("B"))
		{
			//inputAssignmentElement.setAttribute("OrganizationCode", customerID);
			inputAssignmentElement.setAttribute("OrganizationCode", masterCustomerId);
			//}
			/*sals reps are expected only with billto*/
			/*else
		if(suffixType.equals("S")){
			//get the billto customer
			getBillToForShipTo(env, customerID, inputAssignmentElement);

		}*/
			log.debug("inputAssignmentDoc"+SCXmlUtil.getString(inputAssignmentDoc));
			api.invoke(env, "manageCustomerAssignment", inputAssignmentDoc);
		}

	}


	//method to get the organization code which manages the customer
	private void getBillToForShipTo(YFSEnvironment env, String customerID,
			Element inputAssignmentElement) throws RemoteException {
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		inputCustomerDoc.getDocumentElement().setAttribute("CustomerID", customerID);
		env.setApiTemplate("getCustomerList", getCustomerListForParentTemplate);
		Document customerListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		NodeList customerNodeList = customerListDoc.getElementsByTagName("Customer");
		int customerLength = customerNodeList.getLength();
		if(customerLength != 0)
		{
			Element customerElement = (Element)customerNodeList.item(0);
			String orgCode = SCXmlUtil.getXpathAttribute(customerElement, "./ParentCustomer/@CustomerID");
			inputAssignmentElement.setAttribute("OrganizationCode", orgCode);
		}
	}


	//method to check if the master customer user already exists
	private boolean checkForMasterCustomer(YFSEnvironment env, String masterCustomerUser) throws YFSException, RemoteException
	{
		boolean customerExists = false;
		Document inputCustomerContactDoc = SCXmlUtil.createDocument("CustomerContact");
		Element inputCustomerContactElement = inputCustomerContactDoc.getDocumentElement();
		inputCustomerContactElement.setAttribute("CustomerContactID", masterCustomerUser);
		Document contactListDoc = api.invoke(env, "getCustomerContactList", inputCustomerContactDoc);
		NodeList contactNodeList = contactListDoc.getElementsByTagName("CustomerContact");
		int contactLength = contactNodeList.getLength();
		if(contactLength != 0)
		{
			customerExists = true;
		}
		return customerExists;

	}

	private ArrayList<String> getCustomerNameForMSAP(YFSEnvironment env, String rootCustomerKey) throws YFSException, RemoteException
	{
		ArrayList<String> MSAPList = new ArrayList<String>();
		String customerID = "";
		String customerName = "";
		//form the input
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerKey", rootCustomerKey);
		env.setApiTemplate("getCustomerList", getCustomerListForParentTemplate);
		Document customerListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		env.clearApiTemplate("getCustomerList");
		NodeList customerNodeList = customerListDoc.getElementsByTagName("Customer");
		int customerLength = customerNodeList.getLength();
		if(customerLength != 0)
		{
			Element customerElement = (Element)customerNodeList.item(0);
			customerID = customerElement.getAttribute("CustomerID");
			/* Start - changes made on 15/02/2011 */
			// customerName = SCXmlUtil.getXpathAttribute(customerElement, "./BuyerOrganization/@OrganizationCode");
			customerName = SCXmlUtil.getXpathAttribute(customerElement, "./BuyerOrganization/@OrganizationName");
			/* End - changes made on 15/02/2011 */
		}

		MSAPList.add(customerName);
		MSAPList.add(customerID);
		return MSAPList;
	}

	//method to get the rootcustomer key
	private String getRootCustomerKey(YFSEnvironment env, String customerID)
	throws RemoteException {
		String rootCustomerKey = "";
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerID", customerID);
		env.setApiTemplate("getCustomerList", getCustomerListForParentTemplate);
		Document customerListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		env.clearApiTemplate("getCustomerList");
		NodeList customerNodeList = customerListDoc.getElementsByTagName("Customer");
		int customerLength = customerNodeList.getLength();
		if(customerLength != 0)
		{
			Element customerElement = (Element)customerNodeList.item(0);
			rootCustomerKey = customerElement.getAttribute("RootCustomerKey");
		}
		return rootCustomerKey;
	}

	//method to delete the existing assignments for the customer
	private void deleteExistingAssignments(YFSEnvironment env,ArrayList<String> existingTeamKeys) throws YFSException, RemoteException
	{
		int records = existingTeamKeys.size();
		Document multiApiDoc = SCXmlUtil.createDocument("MultiApi");
		Element multiApiElement = multiApiDoc.getDocumentElement();
		for(int counter=0;counter<records;counter++)
		{
			Element apiElement = multiApiDoc.createElement("API");
			apiElement.setAttribute("Name", "manageCustomerAssignment");
			multiApiElement.appendChild(apiElement);
			Element inputElement = multiApiDoc.createElement("Input");
			apiElement.appendChild(inputElement);
			Element customerAssignmentElement = multiApiDoc.createElement("CustomerAssignment");
			customerAssignmentElement.setAttribute("CustomerAssignmentKey", existingTeamKeys.get(counter));
			customerAssignmentElement.setAttribute("Operation", "Delete");
			inputElement.appendChild(customerAssignmentElement);

		}
		log.info("The input to delete assignments multiApi is: "+SCXmlUtil.getString(multiApiDoc));
		api.invoke(env, "multiApi", multiApiDoc);
	}
	//this method is to delete all the customer assignments for a customer
	private ArrayList<String> getAllCustomerAssignments(YFSEnvironment env, String customerID, String organizationCode,String suffixType) throws YFSException, RemoteException
	{
		String customerKey = "";
		ArrayList<String> customerAssignmentList = new ArrayList<String>();
		ArrayList<String> customerKeyList = new ArrayList<String>();
		String customerAssignmentKey = "";
		if(suffixType.equals("S"))
		{
			customerKey = getCustomerKey(env,customerID,false);
			customerKeyList.add(customerKey);
		}
		else
			if(suffixType.equals("B"))
			{
				customerKey = getCustomerKey(env, customerID,false);

				populateCustomerKeyList(env,customerKey,customerKeyList);
				customerKeyList.add(customerKey);
			}
		//form the input
		int customerKeySize = customerKeyList.size();
		if(customerKeySize > 0)
		{
			for(int keyCounter=0;keyCounter<customerKeySize;keyCounter++)
			{
				Document inputCustomerAssignmentDoc = SCXmlUtil.createDocument("CustomerAssignment");
				Element inputCustomerAssignmentElement = inputCustomerAssignmentDoc.getDocumentElement();

				inputCustomerAssignmentElement.setAttribute("CustomerKey", customerKeyList.get(keyCounter));
				Document customerAssignmentListDoc = api.invoke(env, "getCustomerAssignmentList", inputCustomerAssignmentDoc);
				NodeList customerAssignmentNodeList = customerAssignmentListDoc.getElementsByTagName("CustomerAssignment");
				int customerAssignmentLength = customerAssignmentNodeList.getLength();
				if(customerAssignmentLength != 0)
				{
					for(int counter=0;counter<customerAssignmentLength;counter++)
					{
						Element customerAssignmentElement = (Element)customerAssignmentNodeList.item(counter);
						customerAssignmentKey = customerAssignmentElement.getAttribute("CustomerAssignmentKey");
						customerAssignmentList.add(customerAssignmentKey);
					}
				}
			}

		}
		return customerAssignmentList;
	}

	//method to populate customer keys of all shiptos below billto for deleting customer assignments
	private void populateCustomerKeyList(YFSEnvironment env,String customerKey,ArrayList<String> customerKeyList) throws YFSException, RemoteException
	{
		String childCustomerKey = "";
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("ParentCustomerKey", customerKey);
		Document customerListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		NodeList customerNodeList = customerListDoc.getElementsByTagName("Customer");
		int customerLength = customerNodeList.getLength();
		if(customerLength != 0)
		{
			for(int counter=0;counter<customerLength;counter++)
			{
				Element customerElement = (Element)customerNodeList.item(counter);
				childCustomerKey = customerElement.getAttribute("CustomerKey");
				customerKeyList.add(childCustomerKey);

			}
		}


	}

	//method to get the customer key
	//3740 - Method Input is modified ,added attribute for deciding to get root ket or customer key
	private String getCustomerKey(YFSEnvironment env, String customerID, boolean isRootKey) throws YFSException, RemoteException
	{
		String customerKey = "";
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		inputCustomerDoc.getDocumentElement().setAttribute("CustomerID", customerID);
		Document templateListDoc = SCXmlUtil.createDocument("CustomerList");
		Element templateListElement = templateListDoc.getDocumentElement();
		Element templateElement = templateListDoc.createElement("Customer");
		//3740 - Added if condtion to fetch Root customer key or customer key
		if(isRootKey){
		templateElement.setAttribute("RootCustomerKey", "");	
		}else{
		templateElement.setAttribute("CustomerKey", "");
		}
		//3740 - End
		templateListElement.appendChild(templateElement);
		env.setApiTemplate("getCustomerList", templateListDoc);
		Document customerListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		env.clearApiTemplate("getCustomerList");
		NodeList customerNodeList = customerListDoc.getElementsByTagName("Customer");
		int customerlength = customerNodeList.getLength();
		if(customerlength != 0)
		{
			Element customerElement = (Element)customerNodeList.item(0);
			//3740 - Added if condtion to fetch Root customer key or customer key
			if(isRootKey){
			customerKey = customerElement.getAttribute("RootCustomerKey");	
			}else{
			customerKey = customerElement.getAttribute("CustomerKey");
			}
			//3740 End
		}
		return customerKey;
	}

	//method to get the existing customer assignments for the customer
	private ArrayList<String> getCustomerAssignments(YFSEnvironment env, String customerID, String organizationCode,String suffixType) throws YFSException, RemoteException
	{
		ArrayList<String> customerAssignmentList = new ArrayList<String>();
		String customerAssignmentKey = "";
		//form the input
		Document inputCustomerAssignmentDoc = SCXmlUtil.createDocument("CustomerAssignment");
		Element inputCustomerAssignmentElement = inputCustomerAssignmentDoc.getDocumentElement();
		inputCustomerAssignmentElement.setAttribute("CustomerID", customerID);
		if(suffixType.equals("B"))
		{
			inputCustomerAssignmentElement.setAttribute("OrganizationCode", customerID);
		}
		else
			if(suffixType.equals("S"))
			{
				getBillToForShipTo(env, customerID,
						inputCustomerAssignmentElement);

			}

		//inputCustomerAssignmentElement.setAttribute("UserIdQryType", "ISNULL");
		Document customerAssignmentListDoc = api.invoke(env, "getCustomerAssignmentList", inputCustomerAssignmentDoc);
		NodeList customerAssignmentNodeList = customerAssignmentListDoc.getElementsByTagName("CustomerAssignment");
		int customerAssignmentLength = customerAssignmentNodeList.getLength();
		if(customerAssignmentLength != 0)
		{
			for(int counter=0;counter<customerAssignmentLength;counter++)
			{
				Element customerAssignmentElement = (Element)customerAssignmentNodeList.item(counter);
				customerAssignmentKey = customerAssignmentElement.getAttribute("CustomerAssignmentKey");
				customerAssignmentList.add(customerAssignmentKey);
			}
		}
		return customerAssignmentList;
	}

	//method to create sales rep teams and assign the customer to the team

	private void manageCustomerAssignmentforA(YFSEnvironment env,String customerID, String organizationCode,Document inXML) throws YFSException, RemoteException
	{
		HashSet<String> salesRepForTeam = new HashSet<String>();
		String suffixType = "";
		String team = "";
		String salesRep = "";
		Element inputElement = inXML.getDocumentElement();
		suffixType = SCXmlUtil.getXpathAttribute(inputElement, "./Customer/@SuffixType");
		ArrayList<String> teamList = new ArrayList<String>();
		//get the sales rep node list
		NodeList salesRepsNodeList = inXML.getElementsByTagName("SalesReps");
		int salesRepsLength = salesRepsNodeList.getLength();
		//if there are sales rep existing in the input
		if(salesRepsLength != 0)
		{
			Element salesRepsElement = (Element)salesRepsNodeList.item(0); 
			NodeList salesRepNodeList = salesRepsElement.getElementsByTagName("SalesRep");
			int salesRepLength = salesRepNodeList.getLength();
			if(salesRepLength != 0)
			{
				for(int counter=0;counter<salesRepLength;counter++)
				{
					Element salesRepElement = (Element)salesRepNodeList.item(counter);
					//check if the team for this sales rep exists
					salesRep = salesRepElement.getAttribute("EmployeeId");
					if(!salesRepForTeam.contains(salesRep))
					{
						salesRepForTeam.add(salesRep);
						if(!salesRep.equals("0000")){
							ArrayList<String> userDetails = checkUserExists(env, salesRep);
							//boolean userExists = checkUserExists(env, salesRep);
							String userExists = userDetails.get(0);
							/* Changes made for issue 633 - Below If condition has been commented as the sales rep data may or
						   may not exist in User table. */
							//if(userExists.equals("true"))
							//{
							log.debug("Manage Customer Assignment for the customer = " + counter);
							teamList = checkForSalesRepTeam(env,salesRep,organizationCode);
							int teamSize = teamList.size();
							if(teamSize > 0)
							{
								//do the customer assignment to the existing team
								assignCustomerToTeam(env,customerID,teamList,organizationCode,suffixType);

								if(userExists.equals("true"))
								{
									//Modify the user to assign the team...Added on 14/04/2011
									Document modifyUserHierarchyInputDoc = YFCDocument.createDocument("User").getDocument();
									modifyUserHierarchyInputDoc.getDocumentElement().setAttribute("Loginid", userDetails.get(1));
									modifyUserHierarchyInputDoc.getDocumentElement().setAttribute("UserKey", userDetails.get(2));
									modifyUserHierarchyInputDoc.getDocumentElement().setAttribute("DataSecurityGroupId", teamList.get(0));

									api.invoke(env, "modifyUserHierarchy", modifyUserHierarchyInputDoc);

								}
							}
							else
							{
								//create a new team
								String newteamID = createAndAssignCustomerToTeam(env,customerID,salesRep,organizationCode,suffixType);
								if(userExists.equals("true"))
								{
									//Modify the user to assign the team...Added on 14/04/2011
									Document modifyUserHierarchyInputDoc = YFCDocument.createDocument("User").getDocument();
									modifyUserHierarchyInputDoc.getDocumentElement().setAttribute("Loginid", userDetails.get(1));
									modifyUserHierarchyInputDoc.getDocumentElement().setAttribute("UserKey", userDetails.get(2));
									modifyUserHierarchyInputDoc.getDocumentElement().setAttribute("DataSecurityGroupId", newteamID);

									api.invoke(env, "modifyUserHierarchy", modifyUserHierarchyInputDoc);
								}
							}
							//}
						}
					}
				}
			}
		}
	}

	//method to create a team and assign the customer to the team
	private String createAndAssignCustomerToTeam(YFSEnvironment env, String customerID, String salesRep,String org, String suffixType) throws YFSException, RemoteException
	{
		String teamID = salesRep+"_"+"Team";
		String desc = salesRep+" Team";
		//create a team
		//input doc
		Document inputTeamDoc = SCXmlUtil.createDocument("Team");
		Element inputTeamElement = inputTeamDoc.getDocumentElement();
		inputTeamElement.setAttribute("CustomerAccessMode", "03");
		inputTeamElement.setAttribute("Description", desc);
		inputTeamElement.setAttribute("DocumentTypeAccessMode", "01");
		inputTeamElement.setAttribute("EnterpriseAccessMode", "01");
		inputTeamElement.setAttribute("Operation", "Create");
		inputTeamElement.setAttribute("OrganizationCode", "xpedx");
		inputTeamElement.setAttribute("TeamId", teamID);

		//Added by Prasanth Kumar M as a fix for JIRA defect ----> 633

		Element teamEnterpriseListElement = inputTeamDoc.createElement("TeamEnterpriseList");
		Element teamEnterpriseElement = inputTeamDoc.createElement("TeamEnterprise");
		teamEnterpriseElement.setAttribute("EnterpriseOrgCode", "xpedx");
		teamEnterpriseElement.setAttribute("Operation", "Create");

		teamEnterpriseListElement.appendChild(teamEnterpriseElement);
		inputTeamElement.appendChild(teamEnterpriseListElement);

		Document teamDoc = api.invoke(env, "manageTeam", inputTeamDoc);

		ArrayList<String> teamList = new ArrayList<String>();
		teamList.add(teamID);
		teamList.add(teamDoc.getDocumentElement().getAttribute("TeamKey"));
		teamList.add(salesRep);

		assignCustomerToTeam(env, customerID, teamList, org, suffixType);

		return teamID;
	}

	//method to retrieve the team key given team id
	private String getTeamKey(YFSEnvironment env, String teamID) throws YFSException, RemoteException
	{
		String teamKey = "";
		Document inputTeamDoc = SCXmlUtil.createDocument("Team");
		Element inputTeamElement = inputTeamDoc.getDocumentElement();
		inputTeamElement.setAttribute("TeamId", teamID);
		inputTeamElement.setAttribute("OrganizationCode", "xpedx");
		Document teamListDoc = api.invoke(env, "getTeamList", inputTeamDoc);
		NodeList teamNodeList = teamListDoc.getElementsByTagName("Team");
		int teamLength = teamNodeList.getLength();
		if(teamLength != 0)
		{
			Element teamElement = (Element)teamNodeList.item(0);
			teamKey = teamElement.getAttribute("TeamKey");
			//log.debug("TeamKey"+teamKey);
		}
		return teamKey;
	}
	//method to get the userid
	private String getUserId(YFSEnvironment env,String salesRep) throws YFSException, RemoteException
	{
		String userID = "";
		//form the input 
		Document inputUserDoc = SCXmlUtil.createDocument("User");
		Element inputUserElement = inputUserDoc.getDocumentElement();
		Element extnElement = inputUserDoc.createElement("Extn");
		extnElement.setAttribute("ExtnEmployeeId", salesRep);
		inputUserElement.appendChild(extnElement);
		Document userList = api.invoke(env, "getUserList", inputUserDoc);
		NodeList userNodeList = userList.getElementsByTagName("User");
		int userLength = userNodeList.getLength();
		if(userLength != 0)
		{
			userID = ((Element)userNodeList.item(0)).getAttribute("Loginid");
		}
		return userID;
	}
	//method to assign a customer to a team
	private void assignCustomerToTeam(YFSEnvironment env, String customerID, ArrayList<String> teamList,String organizationCode,String suffixType) throws YFSException, RemoteException
	{
		//form the input
		Document customerAssignmentDoc = SCXmlUtil.createDocument("CustomerAssignment");
		Element customerAssignmentElement = customerAssignmentDoc.getDocumentElement();
		customerAssignmentElement.setAttribute("CustomerID", customerID);
		//customerAssignmentElement.setAttribute("CustomerOrganizationCode", organizationCode);
		customerAssignmentElement.setAttribute("Operation", "Create");
		if(suffixType.equals("B"))
		{
			customerAssignmentElement.setAttribute("OrganizationCode", customerID);
		}
		else
			if(suffixType.equals("S"))
			{
				getBillToForShipTo(env, customerID, customerAssignmentElement);
			}

		customerAssignmentElement.setAttribute("TeamCode", teamList.get(0));
		//get the team key
		//String teamKey = getTeamKey(env, teamList.get(0));...Added on 14/04/2011
		String teamKey = getTeamKey(env, teamList.get(0));
		//customerAssignmentElement.setAttribute("TeamKey", teamList.get(1));//...Added on 14/04/2011
		customerAssignmentElement.setAttribute("TeamKey", teamKey);

		api.invoke(env, "manageCustomerAssignment", customerAssignmentDoc);
	}

	//method to check if the sales rep team already exists or not
	private ArrayList<String> checkForSalesRepTeam(YFSEnvironment env, String salesRep, String organizationCode) throws YFSException, RemoteException
	{
		String team = "";
		String teamKey = "";
		ArrayList<String> teamList = new ArrayList<String>();
		//for the input
		Document inputTeamDoc = SCXmlUtil.createDocument("Team");
		Element inputTeamElement = inputTeamDoc.getDocumentElement();
		inputTeamElement.setAttribute("TeamId", salesRep);
		inputTeamElement.setAttribute("TeamIdQryType", "LIKE");
		inputTeamElement.setAttribute("OrganizationCode","xpedx");//...Added on 14/04/2011
		Document teamListDoc = api.invoke(env, "getTeamList", inputTeamDoc);
		NodeList teamNodeList = teamListDoc.getElementsByTagName("Team");
		int teamLength = teamNodeList.getLength();
		if(teamLength != 0)
		{
			Element teamElement = (Element)teamNodeList.item(0);
			team = teamElement.getAttribute("TeamId");
			teamKey = teamElement.getAttribute("TeamKey");
			teamList.add(team);
			teamList.add(teamKey);
			teamList.add(salesRep);
		}
		return teamList;
	}

	/*private Document createGetCustomerListInputForQuery(YFSEnvironment env, String sapCustomerId, String organizationCode)
	{
         YFCDocument getCustomerListInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);*/
	/*sayan commented
         getCustomerListInputDoc.getDocumentElement().setAttribute("CustomerIDQryType", "LIKE");
	 */
	/*  getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, sapCustomerId);
         getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);


		return getCustomerListInputDoc.getDocument();
	}*/

	private YFCElement formAdditionalAddressElement(Element addressNodeElement, YFCDocument inputCustomerDoc, String suffixType) {

		String addressLine1 = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@AddressLine1");
		String addressLine2 = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@AddressLine2");
		String addressLine3 = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@AddressLine3");
		String city = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@City");
		String state = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@State");
		String country = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@Country");
		String zipCode = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@ZipCode");

		YFCElement inputCustomerAddnlAddressListElement = inputCustomerDoc.createElement("CustomerAdditionalAddressList");
		inputCustomerAddnlAddressListElement.setAttribute(XPXLiterals.A_RESET, XPXLiterals.BOOLEAN_FLAG_Y);

		YFCElement inputCustomerAddnlAddressElement = inputCustomerDoc.createElement("CustomerAdditionalAddress");
		if("S".equalsIgnoreCase(suffixType))
		{
			//ShipTo customer
			inputCustomerAddnlAddressElement.setAttribute("IsDefaultShipTo", "Y");
			inputCustomerAddnlAddressElement.setAttribute("AddressType", "ShipTo");
			inputCustomerAddnlAddressElement.setAttribute("IsShipTo", "Y");
		}
		else
		{
			//BillTo Customer
			inputCustomerAddnlAddressElement.setAttribute("IsDefaultBillTo", "Y");
			inputCustomerAddnlAddressElement.setAttribute("AddressType", "BillTo");
			inputCustomerAddnlAddressElement.setAttribute("IsBillTo", "Y");
		}


		YFCElement inputPersonInfoElement = inputCustomerDoc.createElement("PersonInfo");
		inputPersonInfoElement.setAttribute("AddressLine1", addressLine1);
		inputPersonInfoElement.setAttribute("AddressLine2", addressLine2);
		inputPersonInfoElement.setAttribute("AddressLine3", addressLine3);
		inputPersonInfoElement.setAttribute("City", city);
		inputPersonInfoElement.setAttribute("State", state);
		inputPersonInfoElement.setAttribute("Country", country);
		inputPersonInfoElement.setAttribute("ZipCode", zipCode);   

		inputCustomerAddnlAddressElement.appendChild(inputPersonInfoElement);
		inputCustomerAddnlAddressListElement.appendChild(inputCustomerAddnlAddressElement);


		return inputCustomerAddnlAddressListElement;
	}

	private boolean checkIsCustomerAvailableInSystem(YFSEnvironment env, String customerID,String organizationCode)
	throws Exception
	{

		boolean isCustomerAvailableInSytem = false;

		YFCDocument getCustomerListInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
		getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);
		getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE,organizationCode);

		//log.debug("getCustomerListInputDoc:" + getCustomerListInputDoc);
		Document templateListDoc = SCXmlUtil.createDocument("CustomerList");
		Element templateListElement = templateListDoc.getDocumentElement();
		Element templateElement = templateListDoc.createElement("Customer");
		templateElement.setAttribute("Status", "");
		templateListElement.appendChild(templateElement);
		env.setApiTemplate( XPXLiterals.GET_CUSTOMER_LIST_API, templateListDoc);
		Document getCustomerListOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc.getDocument());
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		NodeList customerList = getCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER);


		if(customerList != null && customerList.getLength() >0){
			isCustomerAvailableInSytem = true;
			/*check if the customer is active. and set the class variable. This is to avoid double call to the api.
			 * checkIsCustomerAvailableInSystem() and isCustomerActive() should be used in sequence, and isCustomerActive() cannot be
			 * used seperately.
			 */
			Element _customerElement = SCXmlUtil.getFirstChildElement(getCustomerListOutputDoc.getDocumentElement());
			if(_customerElement.hasAttribute("Status")){
				String _customerStatus = SCXmlUtil.getAttribute(_customerElement, "Status");
				if(null == _customerStatus || _customerStatus.equalsIgnoreCase("10")){
					setCustomerActive(true);
				}else{
					setCustomerActive(false);
				}
			}else{
				setCustomerActive(true);
			}
		}

		return isCustomerAvailableInSytem;
	}

	/*	private ArrayList retrieveExistingAssignmentKeys(YFSEnvironment env, String teamKey, String customerID, String organizationCode) {

        ArrayList <String> existingCustomerAssgnmentKeys = new ArrayList<String>();

		YFCDocument getCustomerAssgmtInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER_ASSIGNMENT);
		getCustomerAssgmtInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);
		getCustomerAssgmtInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID,customerID);
		//getCustomerAssgmtInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_TEAM_KEY,teamKey);

		try {
			Document getCustomerAssgmtOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_ASSIGNMENT_API, getCustomerAssgmtInputDoc.getDocument());

			NodeList getCustomerAssgmtList = getCustomerAssgmtOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER_ASSIGNMENT);

			if(getCustomerAssgmtList.getLength()>0)
			{
			    for(int i=0; i<getCustomerAssgmtList.getLength();i++)
			    {
				        Element customerAssgmtElement = (Element)getCustomerAssgmtList.item(i);

				        //sayan added to retrieve only the Sales Rep teams START
				        String strTeamKey = customerAssgmtElement.getAttribute("TeamKey");

				        String strTeamID = retrieveTeamID(env, strTeamKey, organizationCode);
				        if(strTeamID != null && strTeamID.startsWith("SR_"))
				        {
				        	existingCustomerAssgnmentKeys.add(customerAssgmtElement.getAttribute(XPXLiterals.A_CUSTOMER_ASSIGNMENT_KEY));
				        }
				      //sayan added to retrieve only the Sales Rep teams END
			     }
			} 

		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return existingCustomerAssgnmentKeys;
	}*/

	/*private void invokeMultiApiForExistingAssgmtDeletion(YFSEnvironment env, ArrayList customerAssignmentKeys, 
			String customerID, ArrayList networkIdList, String organizationCode, HashMap salesRepTeam) {

		 YFCDocument multiApiInputDoc = YFCDocument.createDocument(XPXLiterals.E_MULTI_API);


	     for(int i=0; i<customerAssignmentKeys.size(); i++)
	     {


	    	 YFCElement apiElement = multiApiInputDoc.createElement(XPXLiterals.E_API);
		     apiElement.setAttribute(XPXLiterals.A_NAME, XPXLiterals.MANAGE_CUSTOMER_ASSIGNMENT_API);

	         YFCElement inputElement = multiApiInputDoc.createElement(XPXLiterals.E_INPUT);

	         YFCElement manageCustomerAssgmtElement =  multiApiInputDoc.createElement(XPXLiterals.E_CUSTOMER_ASSIGNMENT);
	         manageCustomerAssgmtElement.setAttribute(XPXLiterals.A_CUSTOMER_ASSIGNMENT_KEY, (String)customerAssignmentKeys.get(i));
	         manageCustomerAssgmtElement.setAttribute(XPXLiterals.A_OPERATION, XPXLiterals.DELETE);
	         manageCustomerAssgmtElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE,organizationCode);
	         manageCustomerAssgmtElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);

	         inputElement.appendChild(manageCustomerAssgmtElement);
	         apiElement.appendChild(inputElement);

	         multiApiInputDoc.getDocumentElement().appendChild(apiElement);
	     }

	     if(networkIdList.size() > 0)
	     {
	        for(int i=0; i<networkIdList.size(); i++)
	        {
	    	 YFCElement apiElement = multiApiInputDoc.createElement(XPXLiterals.E_API);
		     apiElement.setAttribute(XPXLiterals.A_NAME, XPXLiterals.MANAGE_CUSTOMER_ASSIGNMENT_API);

	         YFCElement inputElement = multiApiInputDoc.createElement(XPXLiterals.E_INPUT);

	           YFCElement manageCustomerAssignmentElement = multiApiInputDoc.createElement(XPXLiterals.E_CUSTOMER_ASSIGNMENT);
		       manageCustomerAssignmentElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);
		       manageCustomerAssignmentElement.setAttribute(XPXLiterals.A_OPERATION, "Create");
		       manageCustomerAssignmentElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE,organizationCode);
		       manageCustomerAssignmentElement.setAttribute(XPXLiterals.A_TEAM_CODE, (String)salesRepTeam.get((String)networkIdList.get(i)));
		       manageCustomerAssignmentElement.setAttribute(XPXLiterals.A_USER_ID, (String)networkIdList.get(i));

	         inputElement.appendChild(manageCustomerAssignmentElement);
	         apiElement.appendChild(inputElement);

	         multiApiInputDoc.getDocumentElement().appendChild(apiElement);
	        }
	     }
	     try {
	    	 //log.debug("The input to deletion of CustomerAssgmt is: "+SCXmlUtil.getString(multiApiInputDoc.getDocument()));

	    	 if(networkIdList.size() > 0)
		     { 
			api.invoke(env, XPXLiterals.MULTI_API, multiApiInputDoc.getDocument());
		     }
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/


	/*private String retrieveTeamID(YFSEnvironment env, String teamKey, String organizationCode) {

		String teamID = null;

		YFCDocument getTeamListInputDoc = YFCDocument.createDocument(XPXLiterals.E_TEAM);
		getTeamListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_TEAM_KEY, teamKey);
		getTeamListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);

		try {
			Document getTeamListOutputDoc = api.invoke(env, XPXLiterals.GET_TEAM_LIST_API, getTeamListInputDoc.getDocument());

			Element teamElement = (Element)getTeamListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_TEAM).item(0);
			teamID = teamElement.getAttribute(XPXLiterals.A_TEAM_ID);

			log.debug("The team ID value is: "+teamID);

		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return teamKey;
	}*/

	/*private void manageCustomerAssignmentToXPXSalesRepTeam(YFSEnvironment env, String customerID, ArrayList networkIdList, HashMap salesRepTeam, String organizationCode)
	{
		//Invoke multiApi to manageCustomerAssgmt

		 YFCDocument multiApiInputDoc = YFCDocument.createDocument(XPXLiterals.E_MULTI_API);


	     for(int i=0; i<networkIdList.size(); i++)
	     {
	    	 YFCElement apiElement = multiApiInputDoc.createElement(XPXLiterals.E_API);
		     apiElement.setAttribute(XPXLiterals.A_NAME, XPXLiterals.MANAGE_CUSTOMER_ASSIGNMENT_API);

	         YFCElement inputElement = multiApiInputDoc.createElement(XPXLiterals.E_INPUT);

		       YFCElement manageCustomerAssignmentElement = multiApiInputDoc.createElement(XPXLiterals.E_CUSTOMER_ASSIGNMENT);
		       manageCustomerAssignmentElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);
		       manageCustomerAssignmentElement.setAttribute(XPXLiterals.A_OPERATION, "Create");
		       manageCustomerAssignmentElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE,organizationCode);
		       manageCustomerAssignmentElement.setAttribute(XPXLiterals.A_TEAM_CODE, (String)salesRepTeam.get((String)networkIdList.get(i)));
		       manageCustomerAssignmentElement.setAttribute(XPXLiterals.A_USER_ID, (String)networkIdList.get(i));


		       inputElement.appendChild(manageCustomerAssignmentElement);
		       apiElement.appendChild(inputElement);

		       multiApiInputDoc.getDocumentElement().appendChild(apiElement);


	     }
		try {
			//log.debug("The multiApi input to manageCustomerAssgmt is: "+SCXmlUtil.getString(multiApiInputDoc.getDocument()));

			if(networkIdList.size()>0)
			{
			api.invoke(env, XPXLiterals.MULTI_API, multiApiInputDoc.getDocument());
			}
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} */

	/*private ArrayList invokeGetUserList(YFSEnvironment env, String inputXMLEmployeeId) {

         String networkId = null;
         String salesRepTeamId = null;
         ArrayList salesRepTeamDetails = new ArrayList();

         YFCDocument getUserListInputDoc = YFCDocument.createDocument(XPXLiterals.E_USER); 

         //sayan added user organization code, will always be xpedx START
         YFCElement eUser = getUserListInputDoc.getDocumentElement();
         eUser.setAttribute("OrganizationKey", "xpedx");
       //sayan added user organization code, will always be xpedx END
         YFCElement getUserListExtn = getUserListInputDoc.createElement(XPXLiterals.E_EXTN);
         getUserListExtn.setAttribute(XPXLiterals.A_EXTN_EMPLOYEE_ID, inputXMLEmployeeId);
         eUser.appendChild(getUserListExtn);

         try {
        	 env.setApiTemplate( XPXLiterals.GET_USER_LIST_API, getUserListTemplate);
			Document getUserListOutputDoc = api.invoke(env, XPXLiterals.GET_USER_LIST_API, getUserListInputDoc.getDocument());
			env.clearApiTemplate(XPXLiterals.GET_USER_LIST_API);
			Element userElement = (Element)getUserListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_USER).item(0);

			if(userElement!=null)
			{
				networkId = userElement.getAttribute(XPXLiterals.A_LOGIN_ID);
				salesRepTeamId = userElement.getAttribute(XPXLiterals.A_DATA_SECURITY_GROUP);
				salesRepTeamDetails.add(networkId);
				salesRepTeamDetails.add(salesRepTeamId);

			}

			//networkId="pnair";
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

         //TemporarilyHardCoded
         //networkId = "pnair";


		return salesRepTeamDetails;
	}*/

	//sayan updated signature to include MSAP name START
	private Document createCustomerWithMasterSAPAccountNumber(YFSEnvironment env, String masterSapCustomerId, String strMSAPName, 
            String organizationCode, Element custElement) 
	throws Exception

{
      //sayan updated signature to include MSAP name END

      Document manageCustomerOutputDoc = null;
      YFCDocument manageCustomerInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
      manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, masterSapCustomerId);
      manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_TYPE,XPXLiterals.CUSTOMER_TYPE_BUSINESS);
      //manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OPERATION,"Create");
      manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OPERATION,"Manage");
      manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE,organizationCode);
      manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_STATUS,"10");
      //add suffix type for MSAP Customer

     YFCElement buyerOrgElement = manageCustomerInputDoc.createElement(XPXLiterals.E_BUYER_ORGANIZATION);
      buyerOrgElement.setAttribute(XPXLiterals.A_IS_BUYER, XPXLiterals.BOOLEAN_FLAG_Y);
      buyerOrgElement.setAttribute(XPXLiterals.A_LOCALE_CODE,"en_US_EST");
      buyerOrgElement.setAttribute(XPXLiterals.A_ORGANIZATION_NAME,strMSAPName);
      buyerOrgElement.setAttribute(XPXLiterals.A_PRIMARY_ENTERPRISE_KEY,organizationCode);
      buyerOrgElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE,masterSapCustomerId);

      YFCElement customerCurrencyListElement = manageCustomerInputDoc.createElement(XPXLiterals.E_CUSTOMER_CURRENCY_LIST);
      customerCurrencyListElement.setAttribute(XPXLiterals.A_RESET,XPXLiterals.BOOLEAN_FLAG_Y);

      YFCElement customerCurrencyElement = manageCustomerInputDoc.createElement(XPXLiterals.E_CUSTOMER_CURRENCY);
      customerCurrencyElement.setAttribute(XPXLiterals.A_CURRENCY, "USD");
      customerCurrencyElement.setAttribute(XPXLiterals.A_IS_DEFAULT_CURRENCY,XPXLiterals.BOOLEAN_FLAG_Y);
      customerCurrencyListElement.appendChild(customerCurrencyElement);

      manageCustomerInputDoc.getDocumentElement().appendChild(buyerOrgElement);
      manageCustomerInputDoc.getDocumentElement().appendChild(customerCurrencyListElement);
      YFCElement extnElement = manageCustomerInputDoc.createElement("Extn");
      extnElement.setAttribute("ExtnSuffixType", "MC");
      /*** Start of code for jira 3552***/
      extnElement.setAttribute("ExtnUseCustSKU",XpedxConstants.CUST_SKU_FLAG_FOR_MANUFACTURER_ITEM);
      /*** End of code for jira 3552***/
      manageCustomerInputDoc.getDocumentElement().appendChild(extnElement);

//    api.invoke(env, XPXLiterals.MANAGE_CUSTOMER_API, manageCustomerInputDoc.getDocument());
      //log.debug("manageCustomerInputDoc:" + manageCustomerInputDoc);

      manageCustomerOutputDoc = api.invoke(env,XPXLiterals.MANAGE_CUSTOMER_API,manageCustomerInputDoc.getDocument());            
      return  manageCustomerOutputDoc;
}


	//sayan updated signature to include SAP name START
	private Document createCustomerWithSAPAccountNumber(YFSEnvironment env, String sapCustomerId, String masterSapCustomerId, String strSAPName, 
			String strMSAPName, String organizationCode, Element custElement)
	throws Exception
	{
		//sayan updated signature to include SAP name END

		boolean bMasterCustomerCreated = checkIsCustomerAvailableInSystem(env, masterSapCustomerId, organizationCode);

		if(!bMasterCustomerCreated)
		{
			Document masterManageCustomerOutputDoc = createCustomerWithMasterSAPAccountNumber(env,masterSapCustomerId, strMSAPName, organizationCode,custElement);
		}
		Document manageCustomerOutputDoc = null;

		YFCDocument manageCustomerInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
		manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, sapCustomerId);
		manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_TYPE,XPXLiterals.CUSTOMER_TYPE_BUSINESS);
		//manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OPERATION,"Create");
		manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OPERATION,"Manage");
		manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE,organizationCode);
		manageCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_STATUS,"10");
		//add suffic type for SAP customer
		YFCElement extnElement = manageCustomerInputDoc.createElement("Extn");
		extnElement.setAttribute("ExtnSuffixType", "C");
		manageCustomerInputDoc.getDocumentElement().appendChild(extnElement);

		YFCElement buyerOrgElement = manageCustomerInputDoc.createElement(XPXLiterals.E_BUYER_ORGANIZATION);
		buyerOrgElement.setAttribute(XPXLiterals.A_IS_BUYER, XPXLiterals.BOOLEAN_FLAG_Y);
		buyerOrgElement.setAttribute(XPXLiterals.A_LOCALE_CODE,"en_US_EST");
		buyerOrgElement.setAttribute(XPXLiterals.A_ORGANIZATION_NAME,strSAPName);
		buyerOrgElement.setAttribute(XPXLiterals.A_PRIMARY_ENTERPRISE_KEY,organizationCode);
		buyerOrgElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE,sapCustomerId);

		YFCElement parentCustomerElement = manageCustomerInputDoc.createElement(XPXLiterals.E_PARENT_CUSTOMER);
		parentCustomerElement.setAttribute("CustomerID", masterSapCustomerId);//to be reviewed later if this info is correct
		parentCustomerElement.setAttribute("OrganizationCode", masterSapCustomerId);


		YFCElement customerCurrencyListElement = manageCustomerInputDoc.createElement(XPXLiterals.E_CUSTOMER_CURRENCY_LIST);
		customerCurrencyListElement.setAttribute(XPXLiterals.A_RESET,XPXLiterals.BOOLEAN_FLAG_Y);


		YFCElement customerCurrencyElement = manageCustomerInputDoc.createElement(XPXLiterals.E_CUSTOMER_CURRENCY);
		customerCurrencyElement.setAttribute(XPXLiterals.A_CURRENCY, "USD");
		customerCurrencyElement.setAttribute(XPXLiterals.A_IS_DEFAULT_CURRENCY,XPXLiterals.BOOLEAN_FLAG_Y);


		customerCurrencyListElement.appendChild(customerCurrencyElement);
		manageCustomerInputDoc.getDocumentElement().appendChild(buyerOrgElement);
		manageCustomerInputDoc.getDocumentElement().appendChild(customerCurrencyListElement);
		manageCustomerInputDoc.getDocumentElement().appendChild(parentCustomerElement);

		//log.debug("manageCustomerInputDoc:" + manageCustomerInputDoc);

		try {
			manageCustomerOutputDoc = api.invoke(env, XPXLiterals.MANAGE_CUSTOMER_API,  manageCustomerInputDoc.getDocument());

		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return  manageCustomerOutputDoc;
	}

	private YFCDocument createGetCustomerListInput(YFSEnvironment env, String shipToParentCustId, String organizationCode) {

		YFCDocument getCustomerListInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);

		getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, shipToParentCustId);
		getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);

		return getCustomerListInputDoc;

	}

	/**
	 * This method assigns a PriceList for the customer when the customer gets created.
	 * The pricelist which is already assigned to the customer or division or group is assigned to the customer.
	 * @param env
	 * @param companyCode 
	 * @param envtId 
	 * @param pricingWareHouse 
	 * @param customerID 
	 * @param organizationCode 
	 * @param sapAccountNumber 
	 * @throws RemoteException
	 */
	private void createPriceListAssignmentForCustomer(YFSEnvironment env,String shipFrom, String envtId, String companyCode, String pricingWareHouse, String customerID, String organizationCode)
	throws RemoteException {

		String priceListName = null;

		if(pricingWareHouse == null || pricingWareHouse.trim().length() ==0)
		{
			pricingWareHouse = getPricingWareHouse(env,shipFrom);
		}

		if(pricingWareHouse!=null && pricingWareHouse.trim().length()!= 0)
		{

			priceListName = envtId + "-"+companyCode+"-"+pricingWareHouse;

			Document existingPriceListDoc = getExistingPriceListHeader(env,priceListName,organizationCode) ;

			Element outCustPriceListElement = existingPriceListDoc.getDocumentElement();
			String customerLength = outCustPriceListElement.getAttribute(XPXLiterals.A_TOT_NO_OF_RECORDS);

			if(!customerLength.equals("0"))
			{
				YFCDocument managePriceListAssgmtInputDoc = YFCDocument.createDocument(XPXLiterals.E_PRICE_LIST_ASSIGNMENT);
				managePriceListAssgmtInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);
				//managePriceListAssgmtInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_TYPE,XPXLiterals.CUSTOMER_TYPE_BUSINESS);
				managePriceListAssgmtInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OPERATION, XPXLiterals.MANAGE);
				managePriceListAssgmtInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_SHAREABLE,XPXLiterals.BOOLEAN_FLAG_Y);
				managePriceListAssgmtInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTERPRISE_CODE,organizationCode);

				YFCElement priceListHeaderElement = managePriceListAssgmtInputDoc.createElement(XPXLiterals.E_PRICE_LIST_HEADER);
				priceListHeaderElement.setAttribute(XPXLiterals.A_PRICE_LIST_NAME, priceListName);
				managePriceListAssgmtInputDoc.getDocumentElement().appendChild(priceListHeaderElement);

				//log.debug("The input to managePriceListAssgmt is: "+SCXmlUtil.getString(managePriceListAssgmtInputDoc.getDocument()));
				api.invoke(env, XPXLiterals.MANAGE_PRICE_LIST_ASSIGNMENT_API, managePriceListAssgmtInputDoc.getDocument());

			}

			else
			{
				YFCDocument alertInputsDoc =  YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
				alertInputsDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);
				alertInputsDoc.getDocumentElement().setAttribute(XPXLiterals.A_SHIP_FROM_BRANCH, shipFrom);
				alertInputsDoc.getDocumentElement().setAttribute(XPXLiterals.A_PRICING_WAREHOUSE, pricingWareHouse);
				alertInputsDoc.getDocumentElement().setAttribute(XPXLiterals.A_COMPANY_CODE,companyCode);
				api.executeFlow(env, "XPXCreateAlertForCustomerFeedPriceList", alertInputsDoc.getDocument());
			}

			/*Document outPLListDocument = getExistingPriceList(env, shipFrom,sapAccountNumber);

		Element outPLListElement = outPLListDocument.getDocumentElement();
		String pLength = outPLListElement.getAttribute("TotalNumberOfRecords");
		if(!pLength.equals("0"))
		{
			log.debug(SCXmlUtil.getString(outPLListDocument));

			NodeList plNodeList = outPLListDocument.getElementsByTagName("PricelistHeader");
			Element plElement = (Element)plNodeList.item(0);
			String priceListName = plElement.getAttribute("PricelistName");
			String priceListHeaderKey = plElement.getAttribute("PricelistHeaderKey");
			//form the input to  managePricelistAssignment api
			YFCDocument inputPLAssignmentDoc = YFCDocument.createDocument("PricelistAssignment");
			YFCElement inputPLAssignmentElement = inputPLAssignmentDoc.getDocumentElement();
			inputPLAssignmentElement.setAttribute("CustomerID", customerID);
			//inputPLAssignmentElement.setAttribute("CustomerType", "01");
			inputPLAssignmentElement.setAttribute("EnterpriseCode", "xpedx");
			inputPLAssignmentElement.setAttribute("Operation", "Create");
			inputPLAssignmentElement.setAttribute("PricelistHeaderKey", priceListHeaderKey);
			YFCElement pricelistHeaderElement =  inputPLAssignmentDoc.createElement("PricelistHeader");
			pricelistHeaderElement.setAttribute("PricelistName", priceListName);
			inputPLAssignmentElement.appendChild(pricelistHeaderElement);
			//invoke  managePricelistAssignment api
			log.debug(SCXmlUtil.getString(inputPLAssignmentDoc.getDocument()));
			api.invoke(env, "managePricelistAssignment", inputPLAssignmentDoc.getDocument());

		}*/
		}
		else
		{
			log.error("No pricelist assigned to customer "+customerID+""+"due to no pricingWarehouse");
		}
	}

	private Document getExistingPriceListHeader(YFSEnvironment env, String priceListName, String organizationCode) {

		Document outCustPriceListDoc = null;

		YFCDocument inputPriceListDoc = YFCDocument.createDocument(XPXLiterals.E_PRICE_LIST_HEADER);
		YFCElement inputPriceListElement = inputPriceListDoc.getDocumentElement();		
		inputPriceListElement.setAttribute(XPXLiterals.A_PRICE_LIST_NAME, priceListName);
		inputPriceListElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);

		YFCDocument priceListHeaderListTemplate = YFCDocument.createDocument(XPXLiterals.E_PRICE_LIST_HEADER_LIST);
		priceListHeaderListTemplate.getDocumentElement().setAttribute(XPXLiterals.A_TOT_NO_OF_RECORDS, "");
		YFCElement priceListHeaderElement = priceListHeaderListTemplate.createElement(XPXLiterals.E_PRICE_LIST_HEADER);
		priceListHeaderListTemplate.getDocumentElement().appendChild(priceListHeaderElement);

		env.setApiTemplate(XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API, priceListHeaderListTemplate.getDocument());
		try {
			outCustPriceListDoc = api.invoke(env, XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API, inputPriceListDoc.getDocument());
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		env.clearApiTemplate(XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API);

		return outCustPriceListDoc;
	}

	private String getPricingWareHouse(YFSEnvironment env, String shipFrom) {

		String pricingWarehouse = "";
		Document outOrgListDocument = null;

		//form the input Document
		YFCDocument inputOrgDoc = YFCDocument.createDocument(XPXLiterals.E_ORGANIZATION);
		YFCElement inputOrgElement = inputOrgDoc.getDocumentElement();
		inputOrgElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE, shipFrom);
		//form the template
		YFCDocument outputTemplateDoc = YFCDocument.createDocument(XPXLiterals.E_ORGANIZATION_LIST);
		YFCElement outputTemplateElement = outputTemplateDoc.getDocumentElement();
		outputTemplateElement.setAttribute(XPXLiterals.A_TOT_NO_OF_RECORDS, "");
		YFCElement orgTemplateElement = outputTemplateDoc.createElement(XPXLiterals.E_ORGANIZATION);
		orgTemplateElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE, "");
		outputTemplateElement.appendChild(orgTemplateElement);
		YFCElement extnTemplateElement = outputTemplateDoc.createElement(XPXLiterals.E_EXTN);
		extnTemplateElement.setAttribute("ExtnPriceWarehouse", "");
		orgTemplateElement.appendChild(extnTemplateElement);
		env.setApiTemplate("getOrganizationList", outputTemplateDoc.getDocument());

		try {
			outOrgListDocument = api.invoke(env, "getOrganizationList", inputOrgDoc.getDocument());
			//log.debug("The getOrganizationList output doc is: "+SCXmlUtil.getString(outOrgListDocument));
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		env.clearApiTemplate("getOrganizationList");
		Element outOrgListElement = outOrgListDocument.getDocumentElement();
		String orgLength = outOrgListElement.getAttribute(XPXLiterals.A_TOT_NO_OF_RECORDS);
		if(!orgLength.equals("0")){
			Element orgElement = (Element)outOrgListElement.getElementsByTagName(XPXLiterals.E_ORGANIZATION).item(0);
			Element orgExtnElement = (Element)orgElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
			pricingWarehouse = orgExtnElement.getAttribute("ExtnPriceWarehouse");

			//log.debug("The first pricing warehouse returned is: "+pricingWarehouse);
		}             

		//log.debug("The pricingWarehouse returned is: "+pricingWarehouse);     

		return pricingWarehouse;
	}

	/**
	 * This method is used to get the existing PriceList for the customer
	 * using the customer name or division name or group name.
	 * @param env
	 * @param shipFrom
	 * @param sapAccountNumber 
	 * @return
	 * @throws RemoteException
	 */
	private Document getExistingPriceList(YFSEnvironment env, String shipFrom, String sapAccountNumber) throws RemoteException {
		YFCDocument inputPriceListDoc = YFCDocument.createDocument("PricelistHeader");
		YFCElement inputPriceListElement = inputPriceListDoc.getDocumentElement();
		inputPriceListElement.setAttribute("PricelistNameQryType", "LIKE");
		//its hardcoded here
		inputPriceListElement.setAttribute("PricelistName", sapAccountNumber);
		//invoke getPricelistHeaderList api

		YFCDocument priceListHeaderListTemplate = YFCDocument.createDocument(XPXLiterals.E_PRICE_LIST_HEADER_LIST);
		priceListHeaderListTemplate.getDocumentElement().setAttribute(XPXLiterals.A_TOT_NO_OF_RECORDS, "");
		YFCElement priceListHeaderElement = priceListHeaderListTemplate.createElement(XPXLiterals.E_PRICE_LIST_HEADER);
		priceListHeaderListTemplate.getDocumentElement().appendChild(priceListHeaderElement);

		env.setApiTemplate(XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API, priceListHeaderListTemplate.getDocument());
		Document outCustPriceListDoc = api.invoke(env, XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API, inputPriceListDoc.getDocument());
		env.clearApiTemplate(XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API);

		Element outCustPriceListElement = outCustPriceListDoc.getDocumentElement();
		String customerLength = outCustPriceListElement.getAttribute("TotalNumberOfRecords");
		if(customerLength.equals("0"))
		{
			//form the input document using the shipfrom (division)
			YFCDocument inputDivPriceListDoc = YFCDocument.createDocument("PricelistHeader");
			YFCElement inputDivPriceListElement = inputDivPriceListDoc.getDocumentElement();
			inputDivPriceListElement.setAttribute("PricelistNameQryType", "LIKE");
			//its hardcoded here
			inputDivPriceListElement.setAttribute("PricelistName", shipFrom);
			//invoke getPricelistHeaderList api
			env.setApiTemplate(XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API, priceListHeaderListTemplate.getDocument());
			Document outDivEntRuleDoc = api.invoke(env, XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API, inputDivPriceListDoc.getDocument());
			env.clearApiTemplate(XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API);

			Element outDivEntRuleElement = outDivEntRuleDoc.getDocumentElement();
			String divLength = outDivEntRuleElement.getAttribute("TotalNumberOfRecords");
			if(divLength.equals("0"))
			{
				//form the input document using group
				//get the group from organization table
				String group = getGroupFromOrganization(env, shipFrom);
				YFCDocument inputGroupPriceListDoc = YFCDocument.createDocument("PricelistHeader");
				YFCElement inputGroupPriceListElement = inputGroupPriceListDoc.getDocumentElement();
				inputGroupPriceListElement.setAttribute("PricelistNameQryType", "LIKE");
				//its hardcoded here
				inputGroupPriceListElement.setAttribute("PricelistName", group);
				//invoke getEntitlementRuleList api
				env.setApiTemplate(XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API, priceListHeaderListTemplate.getDocument());
				Document outGroupEntRuleDoc = api.invoke(env, XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API, inputGroupPriceListDoc.getDocument());
				env.clearApiTemplate(XPXLiterals.GET_PRICE_LIST_HEADER_LIST_API);

				Element outGroupEntRuleElement = outGroupEntRuleDoc.getDocumentElement();
				String groupLength = outGroupEntRuleElement.getAttribute("TotalNumberOfRecords");
				if(!groupLength.equals("0"))
				{
					return outGroupEntRuleDoc;
				}

			}
			else
			{
				return outDivEntRuleDoc;
			}

		}

		return outCustPriceListDoc;


	}


	/**
	 * This method creates a Entitlement for all the customers being created.
	 * The enetitlement assigned is got based on the customer name or division name or group name to which the customer belongs.
	 * @param env
	 * @param shipFrom
	 * @param sapAccountNumber 
	 * @param customerID 
	 * @param pricingWareHouse 
	 * @param processCode 
	 * @param customerID 
	 * @param organizationCode 
	 * @throws RemoteException
	 */
	/*private void createEntitlementForCustomer(YFSEnvironment env, String shipFrom, String suffixType, String envtId, 
			String pricingWareHouse, String processCode, String customerID, String organizationCode) throws RemoteException {
		//create an input doc for getting the entitlement rule

		String entitlementName = null;
		HashMap existingEntitlementsAttributes = new HashMap();

		if(pricingWareHouse == null || pricingWareHouse.trim().length() ==0)
		{
			pricingWareHouse = getPricingWareHouse(env,shipFrom);
		}

		if(pricingWareHouse!=null && pricingWareHouse.trim().length()!=0)
		{
			entitlementName="GROUP"+"_"+envtId+"_"+pricingWareHouse;

			if(processCode.equalsIgnoreCase("C"))
			{

				existingEntitlementsAttributes = checkExistingEntitlementAssignment(env,customerID,pricingWareHouse,organizationCode);

				if(!existingEntitlementsAttributes.isEmpty())
				{

					String creationRequired = (String)existingEntitlementsAttributes.get("CreationRequired");

					//log.debug("The creation Required value is: "+creationRequired);

					if(XPXLiterals.BOOLEAN_FLAG_N.equalsIgnoreCase(creationRequired))
					{


						//log.debug("No creation or deletion required as the existing entitlement name has no change");

					}

					else if(XPXLiterals.BOOLEAN_FLAG_Y.equalsIgnoreCase(creationRequired))
					{
                        //No existing Group entitlement, have to create one
                        //Create input to assign to an existing entitlement

						Document manageEntitlementRuleInputDoc = createInputDocForManageEntitlementRule(env,customerID,entitlementName);
						log.debug("The input doc for entitlement additon(processCode A) is: "+SCXmlUtil.getString(manageEntitlementRuleInputDoc));
						api.invoke(env, XPXLiterals.MANAGE_ENTITLEMENT_RULE_API, manageEntitlementRuleInputDoc);

						// Commented out as the business rule is that on a change if the customer has no existing group entitlement, 
						 //  we should not add any
					}


					else
					{

                    //Create input to delete the existing entitlement and create the new one
					Document manageEntitlementRuleInputDoc = createManageEntitlementRuleInputDoc(env,existingEntitlementsAttributes,
							XPXLiterals.DELETE,customerID,entitlementName);
					//log.debug("The input doc for entitlement deletion is: "+SCXmlUtil.getString(manageEntitlementRuleInputDoc));
					env.setApiTemplate(XPXLiterals.MANAGE_ENTITLEMENT_RULE_API, manageEntitlementRuleTemplate);
					api.invoke(env, XPXLiterals.MANAGE_ENTITLEMENT_RULE_API, manageEntitlementRuleInputDoc);
					env.clearApiTemplate(XPXLiterals.MANAGE_ENTITLEMENT_RULE_API);

					Document manageEntitlementRuleCreationInputDoc = createManageEntitlementRuleInputDoc(env,existingEntitlementsAttributes,
							XPXLiterals.MANAGE,customerID,entitlementName);
					//log.debug("The input doc for entitlement additon(processCode C) is: "+SCXmlUtil.getString(manageEntitlementRuleCreationInputDoc));
					env.setApiTemplate(XPXLiterals.MANAGE_ENTITLEMENT_RULE_API, manageEntitlementRuleTemplate);
					api.invoke(env, XPXLiterals.MANAGE_ENTITLEMENT_RULE_API, manageEntitlementRuleCreationInputDoc);
					env.clearApiTemplate(XPXLiterals.MANAGE_ENTITLEMENT_RULE_API);

					}
				}	





			}
			else if(processCode.equalsIgnoreCase("A"))
			{
				// Create input to assign a new entitlement

				Document manageEntitlementRuleInputDoc = createInputDocForManageEntitlementRule(env,customerID,entitlementName,organizationCode);
				//log.debug("The input doc for entitlement additon(processCode A) is: "+SCXmlUtil.getString(manageEntitlementRuleInputDoc));
				env.setApiTemplate(XPXLiterals.MANAGE_ENTITLEMENT_RULE_API, manageEntitlementRuleTemplate);
				api.invoke(env, XPXLiterals.MANAGE_ENTITLEMENT_RULE_API, manageEntitlementRuleInputDoc);
				env.clearApiTemplate(XPXLiterals.MANAGE_ENTITLEMENT_RULE_API);

			}

		}



		Document outEntRuleListDoc = getExistingEntitlementRuleList(env, shipFrom, sapAccountNumber);
		Element outEntRuleListElement = outEntRuleListDoc.getDocumentElement();
		String ruleLength = outEntRuleListElement.getAttribute("TotalNumberOfRecords");

		if(!ruleLength.equals("0")){
			NodeList ruleNodeList = outEntRuleListDoc.getElementsByTagName("EntitlementRule");
			Element ruleElement = (Element)ruleNodeList.item(0);
			Node detailsNode = ruleElement.getElementsByTagName(XPXLiterals.E_ENTITLEMENT_RULE_DETAILS_LIST).item(0);
			ruleElement.removeChild(detailsNode);
			//create input to create rule assignment
			Document inputAssignmentDoc= YFCDocument.createDocument().getDocument();
			inputAssignmentDoc.appendChild(inputAssignmentDoc.importNode(ruleNodeList.item(0), true));
			//create EntitlementRuleAssignmentList element

			Element entRuleAssignmentListElement = inputAssignmentDoc.createElement("EntitlementRuleAssignmentList");
			entRuleAssignmentListElement.setAttribute("Reset", "");
			inputAssignmentDoc.getDocumentElement().appendChild(entRuleAssignmentListElement);
			//create EntitlementRuleAssignment element
			Element entRuleAssignmentElement = inputAssignmentDoc.createElement("EntitlementRuleAssignment");
			entRuleAssignmentElement.setAttribute("AssignedToOrganizationCode", "xpedx");
			//customer is now hard coded.
			entRuleAssignmentElement.setAttribute("CustomerID", customerID);
			entRuleAssignmentElement.setAttribute("CustomerType", "01");
			entRuleAssignmentElement.setAttribute("CustomerLevel", "");
			entRuleAssignmentListElement.appendChild(entRuleAssignmentElement);

			//invoke manageEntitlementRule api

			log.debug(SCXmlUtil.getString(inputAssignmentDoc));
			Document outputRuleAssignmentDoc = api.invoke(env, "manageEntitlementRule", inputAssignmentDoc);

		}
	}*/

	private Document createInputDocForManageEntitlementRule(YFSEnvironment env, String customerID, String entitlementName, String organizationCode) {

		YFCDocument manageEntitlementRuleInputDoc = YFCDocument.createDocument(XPXLiterals.E_ENTITLEMENT_RULE);
		manageEntitlementRuleInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTITLEMENT_RULE_ID, entitlementName);
		manageEntitlementRuleInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE,organizationCode);


		YFCElement entitlementRuleAssignmentList = manageEntitlementRuleInputDoc.createElement(XPXLiterals.E_ENTITLEMENT_RULE_ASSIGNMENT_LIST);


		YFCElement entitlementRuleAssignment = manageEntitlementRuleInputDoc.createElement(XPXLiterals.E_ENTITLEMENT_RULE_ASSIGNMENT);
		entitlementRuleAssignment.setAttribute(XPXLiterals.A_CUSTOMER_ID,customerID);
		entitlementRuleAssignment.setAttribute(XPXLiterals.A_OPERATION, XPXLiterals.MANAGE);


		entitlementRuleAssignmentList.appendChild(entitlementRuleAssignment);
		manageEntitlementRuleInputDoc.getDocumentElement().appendChild(entitlementRuleAssignmentList);

		return manageEntitlementRuleInputDoc.getDocument();
	}

	/*private Document createManageEntitlementRuleInputDoc(YFSEnvironment env, HashMap existingEntitlementsAttributes, String operation, 
			String customerID, String entitlementName) {

        YFCDocument manageEntitlementRuleInputDoc = YFCDocument.createDocument(XPXLiterals.E_ENTITLEMENT_RULE);

        if(operation.equalsIgnoreCase(XPXLiterals.DELETE))
        {
        manageEntitlementRuleInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTITLEMENT_RULE_ID, 
        		(String)existingEntitlementsAttributes.get(XPXLiterals.A_ENTITLEMENT_RULE_ID));
        manageEntitlementRuleInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTITLEMENT_RULE_KEY,
        		(String)existingEntitlementsAttributes.get(XPXLiterals.A_ENTITLEMENT_RULE_KEY));
        }
        else if(operation.equalsIgnoreCase(XPXLiterals.MANAGE))
        {
        	manageEntitlementRuleInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTITLEMENT_RULE_ID, entitlementName);
        }
        manageEntitlementRuleInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE,
        		(String)existingEntitlementsAttributes.get(XPXLiterals.A_ORGANIZATION_CODE));


        YFCElement entitlementRuleAssignmentList = manageEntitlementRuleInputDoc.createElement(XPXLiterals.E_ENTITLEMENT_RULE_ASSIGNMENT_LIST);


        YFCElement entitlementRuleAssignment = manageEntitlementRuleInputDoc.createElement(XPXLiterals.E_ENTITLEMENT_RULE_ASSIGNMENT);

        if(operation.equalsIgnoreCase(XPXLiterals.DELETE))
        {
        entitlementRuleAssignment.setAttribute(XPXLiterals.A_ENTITLEMENT_RULE_ASSIGNMENT_KEY,
        		(String)existingEntitlementsAttributes.get(XPXLiterals.A_ENTITLEMENT_RULE_ASSIGNMENT_KEY));
        }
        else if(operation.equalsIgnoreCase(XPXLiterals.MANAGE))
        {
        	entitlementRuleAssignment.setAttribute(XPXLiterals.A_CUSTOMER_ID,customerID);
        	entitlementRuleAssignment.setAttribute(XPXLiterals.A_ENTITLEMENT_RULE_KEY,
        			(String)existingEntitlementsAttributes.get(XPXLiterals.A_ENTITLEMENT_RULE_KEY));

        }
        entitlementRuleAssignment.setAttribute(XPXLiterals.A_OPERATION, operation);

        entitlementRuleAssignmentList.appendChild(entitlementRuleAssignment);
        manageEntitlementRuleInputDoc.getDocumentElement().appendChild(entitlementRuleAssignmentList);


		return manageEntitlementRuleInputDoc.getDocument();
	}
	 */
	/*private HashMap checkExistingEntitlementAssignment(YFSEnvironment env, String customerID, String pricingWareHouse, String organizationCode) {

           boolean isEntitlementAssigned = false;
           HashMap existingEntitlementsAttributes = new HashMap();

		   YFCDocument getEnitlementAssignmentInputDoc =  YFCDocument.createDocument(XPXLiterals.E_ENTITLEMENT_RULE_ASSIGNMENT);
		   getEnitlementAssignmentInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);
		   getEnitlementAssignmentInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);
		   getEnitlementAssignmentInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_PURPOSE, XPXLiterals.BUYING);

		   try {

                // form the template
				YFCDocument outputTemplateDoc = YFCDocument.createDocument(XPXLiterals.E_ENTITLEMENT_RULE_ASSIGNMENT_LIST);
				YFCElement outputTemplateElement = outputTemplateDoc.getDocumentElement();
				outputTemplateElement.setAttribute(XPXLiterals.A_TOT_NO_OF_RECORDS, "");
				YFCElement orgTemplateElement = outputTemplateDoc.createElement(XPXLiterals.E_ENTITLEMENT_RULE_ASSIGNMENT);
				orgTemplateElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, "");
				orgTemplateElement.setAttribute(XPXLiterals.A_ENTITLEMENT_RULE_ASSIGNMENT_KEY, "");
				orgTemplateElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE,"");
				outputTemplateElement.appendChild(orgTemplateElement);
				YFCElement entitlementRuleTemplateElement = outputTemplateDoc.createElement(XPXLiterals.E_ENTITLEMENT_RULE);
				entitlementRuleTemplateElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE, "");
				entitlementRuleTemplateElement.setAttribute(XPXLiterals.A_ENTITLEMENT_RULE_ID, "");
				entitlementRuleTemplateElement.setAttribute(XPXLiterals.A_ENTITLEMENT_RULE_KEY, "");
				orgTemplateElement.appendChild(entitlementRuleTemplateElement);
				env.setApiTemplate(XPXLiterals.GET_ENTITLEMENT_ASSIGNMENT_LIST_API, outputTemplateDoc.getDocument());   


		    //log.debug("The input of getEntitlementAssgmt api is: "+SCXmlUtil.getString(getEnitlementAssignmentInputDoc.getDocument()));   
			Document getEnitlementAssignmentOutputDoc = api.invoke(env, XPXLiterals.GET_ENTITLEMENT_ASSIGNMENT_LIST_API, 
					                                            getEnitlementAssignmentInputDoc.getDocument());
			env.clearApiTemplate(XPXLiterals.GET_ENTITLEMENT_ASSIGNMENT_LIST_API);

			//log.debug("The output of getEntitlementAssgmt api is: "+SCXmlUtil.getString(getEnitlementAssignmentOutputDoc));
			NodeList entitlementRuleAssgmtElementList = getEnitlementAssignmentOutputDoc.getDocumentElement()
			                                         .getElementsByTagName(XPXLiterals.E_ENTITLEMENT_RULE_ASSIGNMENT);

			if(entitlementRuleAssgmtElementList.getLength()>0)
			{
			       for(int i=0; i<entitlementRuleAssgmtElementList.getLength();i++)
			       {
			    	   Element entitlementRuleAssgmtElement = (Element)entitlementRuleAssgmtElementList.item(i);
			    	   Element entitlementRule = (Element)entitlementRuleAssgmtElement.getElementsByTagName(XPXLiterals.E_ENTITLEMENT_RULE).item(0);


			    	     String entitlementRuleId = entitlementRule.getAttribute(XPXLiterals.A_ENTITLEMENT_RULE_ID);

					     String typeOfEntitlement = entitlementRuleId.substring(0,5);

					     String existingPricingWarehouse = entitlementRuleId.substring(entitlementRuleId.indexOf("_", 6)).replace("_", "").trim();

					     if(typeOfEntitlement.equals("GROUP") && !existingPricingWarehouse.equalsIgnoreCase(pricingWareHouse))
					     {
					    	 existingEntitlementsAttributes.put(XPXLiterals.A_ENTITLEMENT_RULE_ASSIGNMENT_KEY,
					    			 entitlementRuleAssgmtElement.getAttribute(XPXLiterals.A_ENTITLEMENT_RULE_ASSIGNMENT_KEY));
					    	 //log.debug("The entitlement rule assgmt key is: "+entitlementRuleAssgmtElement.getAttribute(XPXLiterals.A_ENTITLEMENT_RULE_ASSIGNMENT_KEY));

					    	 existingEntitlementsAttributes.put(XPXLiterals.A_ENTITLEMENT_RULE_ID, entitlementRuleId);
					    	 existingEntitlementsAttributes.put(XPXLiterals.A_ENTITLEMENT_RULE_KEY, entitlementRule.getAttribute(XPXLiterals.A_ENTITLEMENT_RULE_KEY));
					    	 existingEntitlementsAttributes.put(XPXLiterals.A_ORGANIZATION_CODE, entitlementRule.getAttribute(XPXLiterals.A_ORGANIZATION_CODE)); 
					    	 isEntitlementAssigned = true;
					    	 break;
					     }

					     else if(typeOfEntitlement.equals("GROUP") && existingPricingWarehouse.equalsIgnoreCase(pricingWareHouse))
					     {
					    	 String creationRequired = XPXLiterals.BOOLEAN_FLAG_N;

					    	 existingEntitlementsAttributes.put("CreationRequired",creationRequired);

					    	 isEntitlementAssigned = true;

					    	 break;

					     }



			       }



			}

			if(isEntitlementAssigned==false)
			{
				 String creationRequired = XPXLiterals.BOOLEAN_FLAG_Y;

		    	 existingEntitlementsAttributes.put("CreationRequired",creationRequired);
			} // Commented out as the business rule is that on a change if the customer has no existing group entitlement, we should not add any

		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return existingEntitlementsAttributes;
	}*/

	/**
	 * This method gets the existing entitlement for a customer based on Customer name or division or group to which the 
	 * customer belongs.
	 * @param env
	 * @param shipFrom
	 * @param sapAccountNumber 
	 * @return
	 * @throws RemoteException
	 */
	/*private Document getExistingEntitlementRuleList(YFSEnvironment env, String shipFrom, String sapAccountNumber)
			throws RemoteException {
		YFCDocument inputEntRuleListDoc = YFCDocument.createDocument("EntitlementRule");
		YFCElement inputEntRuleListElement = inputEntRuleListDoc.getDocumentElement();
		inputEntRuleListElement.setAttribute("EntitlementRuleIDQryType", "LIKE");
		//its hardcoded here
		inputEntRuleListElement.setAttribute("EntitlementRuleID", sapAccountNumber);
		inputEntRuleListElement.setAttribute("Purpose", "BUYING");
		//invoke getEntitlementRuleList api
		Document outCustEntRuleDoc = api.invoke(env, "getEntitlementRuleList", inputEntRuleListDoc.getDocument());
		Element outCustEntRuleElement = outCustEntRuleDoc.getDocumentElement();
		String customerLength = outCustEntRuleElement.getAttribute("TotalNumberOfRecords");
		if(customerLength.equals("0"))
		{
			//form the input document using the shipfrom (division)
			YFCDocument inputDivEntRuleListDoc = YFCDocument.createDocument("EntitlementRule");
			YFCElement inputDivEntRuleListElement = inputEntRuleListDoc.getDocumentElement();
			inputDivEntRuleListElement.setAttribute("EntitlementRuleIDQryType", "LIKE");
			//its hardcoded here
			inputDivEntRuleListElement.setAttribute("EntitlementRuleID", shipFrom);
			inputDivEntRuleListElement.setAttribute("Purpose", "BUYING");
			//invoke getEntitlementRuleList api
			Document outDivEntRuleDoc = api.invoke(env, "getEntitlementRuleList", inputDivEntRuleListDoc.getDocument());
			Element outDivEntRuleElement = outDivEntRuleDoc.getDocumentElement();
			String divLength = outDivEntRuleElement.getAttribute("TotalNumberOfRecords");
			if(divLength.equals("0"))
			{
				//form the input document using group
				//get the group from organization table
				String group = getGroupFromOrganization(env, shipFrom);
				YFCDocument inputGroupEntRuleListDoc = YFCDocument.createDocument("EntitlementRule");
				YFCElement inputGroupEntRuleListElement = inputGroupEntRuleListDoc.getDocumentElement();
				inputGroupEntRuleListElement.setAttribute("EntitlementRuleIDQryType", "LIKE");
				//its hardcoded here
				inputGroupEntRuleListElement.setAttribute("EntitlementRuleID", group);
				inputGroupEntRuleListElement.setAttribute("Purpose", "BUYING");
				//invoke getEntitlementRuleList api
				Document outGroupEntRuleDoc = api.invoke(env, "getEntitlementRuleList", inputGroupEntRuleListDoc.getDocument());
				Element outGroupEntRuleElement = outGroupEntRuleDoc.getDocumentElement();
				String groupLength = outGroupEntRuleElement.getAttribute("TotalNumberOfRecords");
				if(!groupLength.equals("0"))
				{
					return outGroupEntRuleDoc;
				}

			}
			else
			{
				return outDivEntRuleDoc;
			}

		}

			return outCustEntRuleDoc;


	}*/

	/**
	 * This method gets the group to which the customer belongs from the organization table.
	 * @param env
	 * @param shipFrom
	 * @return
	 * @throws RemoteException
	 */
	private String getGroupFromOrganization(YFSEnvironment env, String shipFrom) throws RemoteException {
		String group = "";
		//form the input Document
		YFCDocument inputOrgDoc = YFCDocument.createDocument("Organization");
		YFCElement inputOrgElement = inputOrgDoc.getDocumentElement();
		inputOrgElement.setAttribute("OrganizationCode", shipFrom);
		//form the template
		YFCDocument outputTemplateDoc = YFCDocument.createDocument("OrganizationList");
		YFCElement outputTemplateElement = outputTemplateDoc.getDocumentElement();
		outputTemplateElement.setAttribute("TotalNumberOfRecords", "");
		YFCElement orgTemplateElement = outputTemplateDoc.createElement("Organization");
		outputTemplateElement.appendChild(orgTemplateElement);
		YFCElement extnTemplateElement = outputTemplateDoc.createElement("Extn");
		extnTemplateElement.setAttribute("ExtnGroup", group);
		orgTemplateElement.appendChild(extnTemplateElement);
		env.setApiTemplate("getOrganizationList", outputTemplateDoc.getDocument());
		Document outOrgListDocument = api.invoke(env, "getOrganizationList", inputOrgDoc.getDocument());
		env.clearApiTemplate("getOrganizationList");
		Element outOrgListElement = outOrgListDocument.getDocumentElement();
		String orgLength = outOrgListElement.getAttribute("TotalNumberOfRecords");
		if(!orgLength.equals("0")){
			NodeList orgNodeList = outOrgListDocument.getElementsByTagName("Organization");
			Element orgElement = (Element)orgNodeList.item(0);
			group = orgElement.getAttribute("ExtnGroup");
		}


		return group;
	}

	/**
	 * This method forms the customerelement for the input xml to manageCustomer api
	 * @param custElement
	 * @param inputCustomerDoc
	 * @param companyCode 
	 * @param envtId 
	 * @param suffixType 
	 * @return
	 */
	//sayan changed method signature to accept customer ID START
	private YFCElement formCustomerElement(String strCustomerID,
			YFCDocument inputCustomerDoc, String organizationCode, String envtId, String companyCode,Element custElement) {
		//sayan changed method signature to accept customer ID END

		YFCElement inputCustomerElement = inputCustomerDoc.getDocumentElement();
		String processCode = custElement.getAttribute(XPXLiterals.A_PROCESS_CODE);
		String suffixType = custElement.getAttribute(XPXLiterals.E_SUFFIX_TYPE);
		/*sayan commented
		//form customerID
		String customerID = "";
		String customerDivision = custElement.getAttribute(XPXLiterals.A_CUSTOMER_DIVISION);
		String legacyCustomerNumber = custElement.getAttribute(XPXLiterals.A_LEGACY_CUSTOMER_NO);
		String processCode = custElement.getAttribute(XPXLiterals.A_PROCESS_CODE);
		String suffixType = custElement.getAttribute(XPXLiterals.A_SUFFIX_TYPE);
		String brandCode = custElement.getAttribute(XPXLiterals.A_BRAND_CODE);
		log.debug("The suffix type in customer element is: "+suffixType);
		/****************Added by Prasanth Kumar M. as per the new requirements***************************

		String shipToSuffix = custElement.getAttribute(XPXLiterals.E_SHIP_TO_SUFFIX);
		String billToSuffix = custElement.getAttribute(XPXLiterals.E_BILL_TO_SUFFIX);

		if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_B))
		{
		customerID = customerDivision+"-"+legacyCustomerNumber+"-"+billToSuffix+"-"+envtId+"-"+companyCode;
		log.debug("The customer id in customer element is: "+customerID);
		}
		else if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_S))
				{
			customerID = customerDivision+"-"+legacyCustomerNumber+"-"+shipToSuffix+"-"+envtId+"-"+companyCode;
				}

		/*************************************************************************************************
		 */
		//log.debug("The customer id in customer element is: "+strCustomerID);
		inputCustomerElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, strCustomerID);
		inputCustomerElement.setAttribute(XPXLiterals.A_CUSTOMER_TYPE, "01");
		inputCustomerElement.setAttribute(XPXLiterals.A_IGNORE_ORDERING, XPXLiterals.BOOLEAN_FLAG_Y);
		inputCustomerElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);
		
	/*	//JIRA 3740 - Start [Added as this will required if want to update BuyerOrganization element for new Organization]
		inputCustomerElement.setAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE, organizationCode);
		//JIRA 3740 - End
*/
		inputCustomerElement.setAttribute(XPXLiterals.A_OPERATION, XPXLiterals.MANAGE);

		//Begin: CR 2277
		inputCustomerElement.setAttribute(XPXLiterals.A_VERTICAL, custElement.getAttribute("BrandCode"));
		inputCustomerElement.setAttribute(XPXLiterals.A_RELATIONSHIP_TYPE, custElement.getAttribute("ShipFromBranch"));
		if(null!=processCode && processCode.equalsIgnoreCase("A")){
			if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_S)){
				inputCustomerElement.setAttribute(XPXLiterals.A_MEMBERSHIP_LEVEL, "Y");
			}
			else if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_B)){
				inputCustomerElement.setAttribute(XPXLiterals.A_MEMBERSHIP_LEVEL, "I");
			}
		}
		//End: CR 2277


		return inputCustomerElement;
	}

	//JIRA 3740 - Start
	//Modified operation defination  - Added 4 attributes to operation those are String CustomerNameType,boolean isAnExistingMSAP, String sapCustomerId.
	/**
	 * This method forms the buyerorganization element for the input to manageCustomer api
	 * @param env 
	 * @param custElement
	 * @param inputCustomerDoc
	 * @param inputCustomerElement
	 * @param sapCustomerId 
	 * @param companyCode 
	 * @param envtId 
	 * @param sapAccountNumber2 
	 * @return
	 */
	private YFCElement formBUyerOrgElement(Element custElement,
			YFCDocument inputCustomerDoc, YFCElement inputCustomerElement, String sapAccountId, String strOrganizationCode,String CustomerNameType,boolean isAnExistingMSAP, String sapCustomerId) {
		String customerID = null;
		String parentOrgCode = null;

		YFCElement buyerOrgElement = inputCustomerDoc.createElement("BuyerOrganization");
		buyerOrgElement.setAttribute("IsBuyer", "Y");
		buyerOrgElement.setAttribute("LocaleCode", "en_US_EST");
		buyerOrgElement.setAttribute("OrganizationName", custElement.getAttribute(CustomerNameType));

		String envtId = custElement.getAttribute(XPXLiterals.A_ENVIRONMENT_ID);
		String companyCode = custElement.getAttribute(XPXLiterals.A_COMPANY_CODE);

		String suffixType = custElement.getAttribute(XPXLiterals.A_SUFFIX_TYPE);
		String customerDivision = custElement.getAttribute(XPXLiterals.A_CUSTOMER_DIVISION);
		String legacyCustomerNumber = custElement.getAttribute(XPXLiterals.A_LEGACY_CUSTOMER_NO);

		String shipToSuffix = custElement.getAttribute(XPXLiterals.E_SHIP_TO_SUFFIX);
		String billToSuffix = custElement.getAttribute(XPXLiterals.E_BILL_TO_SUFFIX);
		//String sapAccountNumber  = custElement.getAttribute(XPXLiterals.A_SAP_PARENT_ACCOUNT_NO);

		/*if(sapAccountNumber == null || sapAccountNumber.trim().length() ==0)
		{
			sapAccountNumber = sapAccountNumber2;
		}*/



		if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_B))
		{
			customerID = customerDivision+"-"+legacyCustomerNumber+"-"+billToSuffix+"-"+envtId+"-"+companyCode+"-B";
			parentOrgCode = sapAccountId;
			//log.debug("The Parent OrganizationCode is: "+parentOrgCode);
		}
		else if(suffixType.equalsIgnoreCase(XPXLiterals.CHAR_S))
		{
			customerID = customerDivision+"-"+legacyCustomerNumber+"-"+shipToSuffix+"-"+envtId+"-"+companyCode+"-S";
			parentOrgCode = customerDivision+"-"+legacyCustomerNumber+"-"+billToSuffix+"-"+envtId+"-"+companyCode+"-B";
		}

		/********8Temporary****************/
		String strCustomerName = custElement.getAttribute(CustomerNameType);
		if(strCustomerName == null || strCustomerName.trim().length() == 0)
		{
			buyerOrgElement.setAttribute("OrganizationName",customerID);
		}
		else
		{
			buyerOrgElement.setAttribute("OrganizationName",strCustomerName);
		}
		/*********************************/
		//JIRA 3740 - Start
			if (!isAnExistingMSAP) {
				buyerOrgElement.setAttribute("OrganizationCode", customerID);	
			}else{
				if(null == sapCustomerId){
					buyerOrgElement.setAttribute("OrganizationCode", customerID);
				}else{
					buyerOrgElement.setAttribute("OrganizationCode", sapCustomerId);
				}
				
			}
		//JIRA 3740 - End	
		if((null!=sapCustomerId && sapCustomerId.equalsIgnoreCase(parentOrgCode)) || (null!=customerID && customerID.equalsIgnoreCase(parentOrgCode))){
			buyerOrgElement.setAttribute("ParentOrganizationCode", "");
		}else{
		buyerOrgElement.setAttribute("ParentOrganizationCode", parentOrgCode);
		}
		buyerOrgElement.setAttribute("PrimaryEnterpriseKey", strOrganizationCode);
		return buyerOrgElement;
	}
     //JIRA 3740 End
	/**
	 * This method used to create a Extn element to the input to manageCustomer api
	 * @param custElement
	 * @param inputCustomerDoc
	 * @return
	 */
	private YFCElement formExtnElement(Element custElement,
			YFCDocument inputCustomerDoc) {

		YFCElement extnElement = inputCustomerDoc.createElement(XPXLiterals.E_EXTN);
		extnElement.setAttribute("ExtnBrandCode", custElement.getAttribute("BrandCode"));
		extnElement.setAttribute("ExtnCAPSID", custElement.getAttribute("CapsId"));
		extnElement.setAttribute("ExtnCustOrderBranch", custElement.getAttribute("CustomerOrderBranch"));
		extnElement.setAttribute("ExtnCustomerClass", custElement.getAttribute("CustomerClass"));
		extnElement.setAttribute("ExtnCustomerDivision", custElement.getAttribute("CustomerDivision"));
		extnElement.setAttribute("ExtnInvoiceDistMethod", custElement.getAttribute("InvoiceDistributionMethod"));
		extnElement.setAttribute("ExtnLegacyCustNumber", custElement.getAttribute("LegacyCustomerNumber"));
		extnElement.setAttribute("ExtnNationalAccNo", custElement.getAttribute("NationalAccountNumber"));
		extnElement.setAttribute("ExtnOrderUpdateFlag", custElement.getAttribute("OrderUpdateFlag"));
		extnElement.setAttribute("ExtnSAPNumber", custElement.getAttribute("SAPNumber"));
		extnElement.setAttribute("ExtnSAPParentAccNo", custElement.getAttribute("SAPParentAccountNumber"));
		String strServiceOptCode = custElement.getAttribute("ServiceOptimizationCode");
		extnElement.setAttribute("ExtnServiceOptCode", strServiceOptCode);
		/**** Code added for Jira 1419****/
		extnElement.setAttribute("ExtnAllowDirectOrderFlag",custElement.getAttribute("AllowDirectOrdersFlag"));


		//sayan - added START
		if("K".equalsIgnoreCase(strServiceOptCode)|| "P".equalsIgnoreCase(strServiceOptCode)|| "Q".equalsIgnoreCase(strServiceOptCode))
		{
			extnElement.setAttribute("ExtnSampleRequestFlag", "Y");
		}
		else
		{
			//since default value is ' '
			extnElement.setAttribute("ExtnSampleRequestFlag", "N");
		}
		//sayan - added END
		//extnElement.setAttribute("ExtnServiceOptCode", custElement.getAttribute("ServiceOptimizationCode"));
		extnElement.setAttribute("ExtnShipComplete", custElement.getAttribute("ShipComplete"));
		extnElement.setAttribute("ExtnShipFromBranch", custElement.getAttribute("ShipFromBranch"));
		extnElement.setAttribute("ExtnSuffixType", custElement.getAttribute("SuffixType"));
		extnElement.setAttribute("ExtnBillToSuffix", custElement.getAttribute("BillToSuffix"));
		extnElement.setAttribute("ExtnShipToSuffix", custElement.getAttribute("ShipToSuffix"));
		extnElement.setAttribute("ExtnEnvironmentCode", custElement.getAttribute("EnvironmentId"));
		extnElement.setAttribute("ExtnCompanyCode", custElement.getAttribute("CompanyCode"));
		extnElement.setAttribute("ExtnCurrencyCode", custElement.getAttribute("CurrencyCode"));
		extnElement.setAttribute("ExtnCustomerName", custElement.getAttribute("CustomerName"));
		extnElement.setAttribute("ExtnCustomerStatus", custElement.getAttribute("CustomerStatus"));
		extnElement.setAttribute("ExtnSAPName", custElement.getAttribute("SAPName"));
		extnElement.setAttribute("ExtnSAPParentName", custElement.getAttribute("ParentSAPName"));
		extnElement.setAttribute("ExtnNAICSCode", custElement.getAttribute("NAICSCode"));
		extnElement.setAttribute("ExtnNAICSName", custElement.getAttribute("NAICSName"));
		extnElement.setAttribute("ExtnShipToOverrideFlag", custElement.getAttribute("ShipToOverride"));
		extnElement.setAttribute("ExtnOrigEnvironmentCode", custElement.getAttribute("LegacyEnvironmentId"));
		extnElement.setAttribute("ExtnPriceWarehouse", custElement.getAttribute("PricingWarehouse"));
		extnElement.setAttribute("ExtnCustomerStoreNumber", custElement.getAttribute("CustomerStoreNumber"));
		extnElement.setAttribute("ExtnPhone1", custElement.getAttribute("CustomerPhone"));
		extnElement.setAttribute("ExtnAttnName", custElement.getAttribute("CustomerAttentionName"));
		extnElement.setAttribute("ExtnFax1", custElement.getAttribute("CustomerFax"));

		//Added by Prasanth Kumar M as fix for JIRA defect ---> 630

		extnElement.setAttribute("ExtnBillToDunsNo", custElement.getAttribute("DUNSNumber"));

		//Added for CR 968

		extnElement.setAttribute("ExtnCustomerPORuleFlag", custElement.getAttribute("CustomerPORequiredFlag"));

		//STARTS - Fix for JIRA 1886 : adsouza

		extnElement.setAttribute("ExtnLocationID", custElement.getAttribute("LocationID"));

		//ENDS - Fix for JIRA 1886 : adsouza


		return extnElement;
	}

	/**
	 * This method is used to form the ContactPersonInfo element to the input to manageCustomer api
	 * @param custElement
	 * @param inputCustomerDoc
	 * @return
	 */
	private YFCElement formContactPersonInfoElement(Element addressNodeElement,
			YFCDocument inputCustomerDoc) {
		String addressLine1 = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@AddressLine1");
		String addressLine2 = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@AddressLine2");
		String addressLine3 = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@AddressLine3");
		String city = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@City");
		String state = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@State");
		String country = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@Country");
		String zipCode = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@ZipCode");
		YFCElement inputContactPersonInfoElement = inputCustomerDoc.createElement("ContactPersonInfo");
		inputContactPersonInfoElement.setAttribute("AddressLine1", addressLine1);
		inputContactPersonInfoElement.setAttribute("AddressLine2", addressLine2);
		inputContactPersonInfoElement.setAttribute("AddressLine3", addressLine3);
		inputContactPersonInfoElement.setAttribute("City", city);
		inputContactPersonInfoElement.setAttribute("State", state);
		inputContactPersonInfoElement.setAttribute("Country", country);
		inputContactPersonInfoElement.setAttribute("ZipCode", zipCode);
		return inputContactPersonInfoElement;
	}

	/**
	 * @param custElement
	 * @param inputCustomerDoc
	 * @return
	 */
	private YFCElement formBillingPersonInfoElement(Element addressNodeElement,
			YFCDocument inputCustomerDoc) {
		String addressLine1 = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@AddressLine1");
		String addressLine2 = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@AddressLine2");
		String addressLine3 = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@AddressLine3");
		String city = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@City");
		String state = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@State");
		String country = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@Country");
		String zipCode = SCXmlUtil.getXpathAttribute(addressNodeElement, "./Address/@ZipCode");
		YFCElement inputBillingPersonInfoElement = inputCustomerDoc.createElement("BillingPersonInfo");
		inputBillingPersonInfoElement.setAttribute("AddressLine1", addressLine1);
		inputBillingPersonInfoElement.setAttribute("AddressLine2", addressLine2);
		inputBillingPersonInfoElement.setAttribute("AddressLine3", addressLine3);
		inputBillingPersonInfoElement.setAttribute("City", city);
		inputBillingPersonInfoElement.setAttribute("State", state);
		inputBillingPersonInfoElement.setAttribute("Country", country);
		inputBillingPersonInfoElement.setAttribute("ZipCode", zipCode);

		return inputBillingPersonInfoElement;
	}

	/**
	 * @param custElement
	 * @param inputCustomerDoc
	 * @return
	 */
	private YFCElement formSalesRepListElement( Element custElement, YFCDocument inputCustomerDoc)
	{
		String processCode = custElement.getAttribute(XPXLiterals.A_PROCESS_CODE);
		YFCElement salesRepListElement = inputCustomerDoc.createElement("XPEDXSalesRepList");


		NodeList salesRepNodeList = custElement.getElementsByTagName("SalesReps");
		if(salesRepNodeList.getLength()>0)
		{
			int salesRepNodeListLength = salesRepNodeList.getLength();
			for(int salesNo=0;salesNo<salesRepNodeListLength;salesNo++)
			{
				String employeeID = "";
				Element inputSalesRepListElement = (Element)salesRepNodeList.item(salesNo);
				//log.debug(SCXmlUtil.getString(inputSalesRepListElement));
				NodeList salesRepNode = inputSalesRepListElement.getElementsByTagName("SalesRep");
				int salesRepCounter = salesRepNode.getLength();
				if(salesRepCounter>0)
				{
					for(int salesCounter=0;salesCounter<salesRepCounter;salesCounter++)
					{
						Element employeeElement = (Element)salesRepNode.item(salesCounter);
						//log.debug(SCXmlUtil.getString(employeeElement));
						employeeID = employeeElement.getAttribute("EmployeeId");
						YFCElement salesRepElement = inputCustomerDoc.createElement("XPEDXSalesRep");
						salesRepElement.setAttribute("SalesRepId", employeeID);
						salesRepElement.setAttribute("Operation", processCode);
						salesRepListElement.appendChild(salesRepElement);
					}
				}

			}
		}
		return salesRepListElement;
	}

	/**
	 * @param env
	 * @param custElement
	 * @param inputCustomerDoc
	 * @return
	 * @throws YFSException
	 * @throws RemoteException
	 */
	private YFCElement formSalesModifyRepListElement( YFSEnvironment env,Element custElement, YFCDocument inputCustomerDoc) throws YFSException, RemoteException
	{
		YFCElement salesRepListElement = inputCustomerDoc.createElement("XPEDXSalesRepList");
		HashSet<String> existingEmployeeSet = new HashSet<String>();
		existingEmployeeSet = getExistingCSRList(env);

		HashSet<String> inputEmployeeList = new HashSet<String>();
		inputEmployeeList = getInputEmployeeList(custElement);

		HashSet<String> finalEmployeeList = new HashSet<String>();
		HashSet<String> deleteEmployeeList = new HashSet<String>();
		int flag = 0;
		flag = checkIfModificationRequired(existingEmployeeSet, inputEmployeeList);
		if(flag == 1)
		{
			//form a final set
			Iterator<String> finalIterator = inputEmployeeList.iterator();
			while(finalIterator.hasNext())
			{
				String emp = finalIterator.next();
				//will not modify the already existing CSR
				//if(existingEmployeeSet.contains(emp))
				//{
				//finalEmployeeList.add(emp);
				//}
				if(!existingEmployeeSet.contains(emp))
				{
					finalEmployeeList.add(emp);
				}

			}

			Iterator<String> deleteIterator = existingEmployeeSet.iterator();
			while(deleteIterator.hasNext())
			{
				String delEmp = deleteIterator.next();
				if(!inputEmployeeList.contains(delEmp))
				{
					deleteEmployeeList.add(delEmp);
				}

			}
			//form the element

			Iterator<String> salesIterator = finalEmployeeList.iterator();
			while(salesIterator.hasNext())
			{
				String repId = salesIterator.next();
				YFCElement salesRepElement = inputCustomerDoc.createElement("XPEDXSalesRep");
				salesRepElement.setAttribute("SalesRepId", repId);
				salesRepElement.setAttribute("Operation", "Create");
				salesRepListElement.appendChild(salesRepElement);
			}
			Iterator<String> delIterator = deleteEmployeeList.iterator();
			while(delIterator.hasNext())
			{
				String delEmpId = delIterator.next();
				YFCElement salesRepElement = inputCustomerDoc.createElement("XPEDXSalesRep");
				salesRepElement.setAttribute("SalesRepId", delEmpId);
				salesRepElement.setAttribute("Operation", "Delete");
				salesRepListElement.appendChild(salesRepElement);

			}


		}






		return salesRepListElement;
	}

	/**
	 * @param existingEmployeeSet
	 * @param inputEmployeeList
	 * @return
	 */
	private int checkIfModificationRequired(HashSet<String> existingEmployeeSet,
			HashSet<String> inputEmployeeList) {
		int flag = 0;
		//check if both are same
		if(existingEmployeeSet.size() == inputEmployeeList.size())
		{
			Iterator<String> ieIterator = inputEmployeeList.iterator();
			while(ieIterator.hasNext())
			{
				String empId = ieIterator.next();
				if(!existingEmployeeSet.contains(empId))
				{
					flag=1;
					break;

				}

			}
		}
		else
		{
			flag = 1;
		}
		return flag;
	}

	/**
	 * @param custElement
	 * @return
	 */
	private HashSet<String> getInputEmployeeList(Element custElement) {
		HashSet<String> inputEmployeeList = new HashSet<String>();
		NodeList salesRepNodeList = custElement.getElementsByTagName("SalesReps");
		if(salesRepNodeList.getLength()>0)
		{
			int salesRepNodeListLength = salesRepNodeList.getLength();
			for(int salesNo=0;salesNo<salesRepNodeListLength;salesNo++)
			{
				String employeeID = "";
				Element inputSalesRepListElement = (Element)salesRepNodeList.item(salesNo);
				//log.debug(SCXmlUtil.getString(inputSalesRepListElement));
				NodeList salesRepNode = inputSalesRepListElement.getElementsByTagName("SalesRep");
				int salesRepCounter = salesRepNode.getLength();
				if(salesRepCounter>0)
				{
					for(int salesCounter=0;salesCounter<salesRepCounter;salesCounter++)
					{
						Element employeeElement = (Element)salesRepNode.item(salesCounter);
						//log.debug(SCXmlUtil.getString(employeeElement));
						employeeID = employeeElement.getAttribute("EmployeeId");
						inputEmployeeList.add(employeeID);
					}
				}
			}
		}
		return inputEmployeeList;
	}

	/**
	 * @param env
	 * @return
	 * @throws RemoteException
	 */
	private HashSet<String> getExistingCSRList(YFSEnvironment env) throws RemoteException {
		//set template to get the XPEDXSalesRepList

		YFCDocument salesRepTemplateListDoc = YFCDocument.createDocument("XPEDXSalesRepList");
		YFCElement salesRepTemplateListElement = salesRepTemplateListDoc.getDocumentElement();
		YFCElement salesRepTemplateElement = salesRepTemplateListDoc.createElement("XPEDXSalesRep");
		salesRepTemplateElement.setAttribute("SalesRepId", "");
		salesRepTemplateListElement.appendChild(salesRepTemplateElement);

		//form the input
		YFCDocument inputCSRDoc = YFCDocument.createDocument("XPEDXSalesRep"); 
		//get the exisitng list of CSRs
		env.setApiTemplate("getCSRListService", salesRepTemplateListDoc.getDocument());
		Document outputCSRListDocument = api.executeFlow(env, "getCSRListService", inputCSRDoc.getDocument());
		//Document outputCSRListDocument = api.invoke(env, "getCSRListService", inputCSRDoc.getDocument());

		env.clearApiTemplate("getCSRListService");
		//log.debug(YFCDocument.getDocumentFor(outputCSRListDocument));
		HashSet<String> existingEmployeeSet = new HashSet<String>();
		NodeList employeeList = outputCSRListDocument.getElementsByTagName("XPEDXSalesRep");
		int employeeNumber = employeeList.getLength();
		if(employeeNumber>0)
		{
			for(int employeeCounter = 0;employeeCounter<employeeNumber;employeeCounter++)
			{
				Element employeeElement = (Element)employeeList.item(employeeCounter);
				String salesRedId = employeeElement.getAttribute("SalesRepId");
				existingEmployeeSet.add(salesRedId);
			}
		}
		return existingEmployeeSet;
	}

	/**
	 * @param env
	 * @param customerElement 
	 * @return
	 * @throws YFSException
	 * @throws RemoteException
	 */
	private String getOrganizationCode(YFSEnvironment env, Element customerElement) throws YFSException, RemoteException
	{
		String organizationCode = "";
		//form the input
		YFCDocument inputDoc= YFCDocument.createDocument("CommonCode");
		YFCElement inputElement = inputDoc.getDocumentElement();
		inputElement.setAttribute("CodeType", "XPXBrandSF");
		inputElement.setAttribute("CodeValue", customerElement.getAttribute("BrandCode"));
		inputElement.setAttribute("OrganizationCode", "DEFAULT");
		//log.debug("The input to getCommonCodeList is: "+SCXmlUtil.getString(inputDoc.getDocument()));
		Document outputListDoc = api.invoke(env, "getCommonCodeList", inputDoc.getDocument());
		//log.debug("The output of getCommonCodeList is: "+SCXmlUtil.getString(outputListDoc));
		NodeList orgList = outputListDoc.getElementsByTagName("CommonCode");
		int orgLength = orgList.getLength();
		if(orgLength!=0)
		{
			Element listElement = (Element)orgList.item(0);
			organizationCode = listElement.getAttribute("CodeShortDescription");
			//log.debug("The organizationCode retruned from method to retrieve the OrgCode is: "+organizationCode);

		}
		//log.debug("The organizationCode retruned from method to retrieve the OrgCode is: "+organizationCode);
		return organizationCode;
	}

	public boolean isCustomerActive() {
		return isCustomerActive;
	}

	public void setCustomerActive(boolean isCustomerActive) {
		this.isCustomerActive = isCustomerActive;
	}

	private Element getMSAPCustomerElement(YFSEnvironment env, String customerID, String organizationCode) throws RemoteException{
		YFCDocument getCustomerListInputDoc = createGetCustomerListInput(env, customerID, organizationCode);
		getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);
		getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);

		Document templateListDoc = SCXmlUtil.createDocument(XPXLiterals.E_CUSTOMER_LIST);
		Element templateListElement = templateListDoc.getDocumentElement();
		Element templateCustElement = templateListDoc.createElement(XPXLiterals.E_CUSTOMER);
		templateCustElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, customerID);
		templateCustElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);
		Element templateExtnElement = templateListDoc.createElement(XPXLiterals.E_EXTN);
		templateExtnElement.setAttribute("ExtnSAPParentAccNo", "");
		templateExtnElement.setAttribute("ExtnSAPParentName", "");
		//JIRA 3740 - Start
		templateExtnElement.setAttribute("ExtnSAPNumber", "");
		templateExtnElement.setAttribute("ExtnSAPName", "");
		//JIRA 3740 - End
		templateCustElement.appendChild(templateExtnElement);
		templateListElement.appendChild(templateCustElement);

		log.info("getMSAPCustomerElement - Extn Template : "+SCXmlUtil.getString(templateListDoc));

		env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, templateListDoc);
		Document getCustomerListOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc.getDocument());
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);

		log.info("getMSAPCustomerElement - Output Document : "+SCXmlUtil.getString(getCustomerListOutputDoc));

		NodeList customerList = getCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_EXTN);
		Element custElement = (Element)customerList.item(0);
		
		return custElement;
		
	}	
	//JIRA 3740 Modified
	//Added four attribute to function and pass these attribute to formBUyerOrgElement operation for JIRA 3740 [custElement - Current element,existingMsapName,isAnExistingMSAP - isCustomeravaiable condtion,strSAPName - NewSAPName]
	private boolean updateCustomerWithMSAPAccountNumber(YFSEnvironment env, String organizationCode, String customerId, String masterSapCustomerId, String newMSAPAccountNumber, String newMSAPName, String suffixType, Element custElement, String existingMsapName, boolean isAnExistingMSAP, String strSAPName)
	{
		boolean result = true;
		YFCDocument updateMSAPCustomerInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
		updateMSAPCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, customerId);
		updateMSAPCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OPERATION,"Modify");
		updateMSAPCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, organizationCode);
		//JIRA 3740 - Start
		//Added Code for Updating ParentSAPName for JIRA 3740//
		updateMSAPCustomerInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE, customerId);
		//Added Code for Updating ParentSAPName for JIRA 3740//
		if (!newMSAPName.equalsIgnoreCase(existingMsapName) && suffixType.equals("C")) {
			String companyCode = custElement.getAttribute(XPXLiterals.A_COMPANY_CODE);
			String envtId = custElement.getAttribute(XPXLiterals.A_ENVIRONMENT_ID);
			YFCElement inputCustomerElement = formCustomerElement(customerId, updateMSAPCustomerInputDoc, organizationCode, envtId, companyCode, custElement);
			YFCElement buyerOrgElement = formBUyerOrgElement(custElement, updateMSAPCustomerInputDoc, inputCustomerElement, masterSapCustomerId, organizationCode, "ParentSAPName",isAnExistingMSAP,masterSapCustomerId);
			updateMSAPCustomerInputDoc.getDocumentElement().appendChild(buyerOrgElement);
		
			}
		//Completed Code for Updating ParentSAPName for JIRA 3909//
		//JIRA 3740 - End
		if(suffixType.equals("C"))
		{
			YFCElement parentCustomerElement = updateMSAPCustomerInputDoc.createElement(XPXLiterals.E_PARENT_CUSTOMER);
			parentCustomerElement.setAttribute("CustomerID", masterSapCustomerId);
			parentCustomerElement.setAttribute("OrganizationCode", organizationCode);		      
			updateMSAPCustomerInputDoc.getDocumentElement().appendChild(parentCustomerElement);

		}else {
			YFCElement extnElement = updateMSAPCustomerInputDoc.createElement(XPXLiterals.E_EXTN);
			extnElement.setAttribute("ExtnSAPParentAccNo", newMSAPAccountNumber);
			extnElement.setAttribute("ExtnSAPParentName",  newMSAPName);
			if(null!=strSAPName){
			extnElement.setAttribute("ExtnSAPName",  strSAPName);	
			}
			updateMSAPCustomerInputDoc.getDocumentElement().appendChild(extnElement);
		}
		

		log.info("updateSAPCustomerWithMSAPAccountNumber - update xml : "+SCXmlUtil.getString(updateMSAPCustomerInputDoc.getDocument()));
		try {
			api.invoke(env, XPXLiterals.MANAGE_CUSTOMER_API,  updateMSAPCustomerInputDoc.getDocument());

		} catch (YFSException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
		}
		return result;
	}



	private Document getChildCustomerList(YFSEnvironment env, String customerId, String organizationCode) throws YFSException, RemoteException{


		Document inputCustomerDoc= YFCDocument.createDocument("Customer").getDocument();
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerID", customerId);
		inputCustomerElement.setAttribute("OrganizationCode", organizationCode);
		

		Document childCustomerListDoc = api.executeFlow(env, "XPXGetChildCustomerListService", inputCustomerDoc);
		return childCustomerListDoc;
	}




	public Document getCustomerContactList(YFSEnvironment env,String mSapID) throws YFSException, RemoteException{



		Document inputCustContactDoc =YFCDocument.createDocument("CustomerContact").getDocument();

		Document custContactListDoc;

		Element inputCustContactElement = inputCustContactDoc.getDocumentElement();
		Element inputCustElement = inputCustContactDoc.createElement("Customer");
		inputCustElement.setAttribute("BuyerOrganizationCode", mSapID);
		inputCustContactElement.appendChild(inputCustElement);


		YFCDocument outputTemplateDoc = YFCDocument.createDocument("CustomerContactList");
		YFCElement outputTemplateElement = outputTemplateDoc.getDocumentElement();

		YFCElement orgTemplateElement = outputTemplateDoc.createElement("CustomerContact");
		orgTemplateElement.setAttribute("UserID","");
		outputTemplateElement.appendChild(orgTemplateElement);
		env.setApiTemplate("getCustomerContactList", outputTemplateDoc.getDocument());
		Document getCustContactListDoc = api.invoke(env, "getCustomerContactList", inputCustContactDoc);
		env.clearApiTemplate("getCustomerContactList");

		return getCustContactListDoc;
	}




	public Document getCustomerAssignmentList(YFSEnvironment env,String userId) throws YFSException, RemoteException{


		Document inputCustAssignmtDoc =YFCDocument.createDocument("CustomerAssignment").getDocument();

		Element inputCustElement =inputCustAssignmtDoc.getDocumentElement();

		inputCustElement.setAttribute("UserId", userId);




		/*             
		 * 
   	   YFCDocument outputTemplateDoc = YFCDocument.createDocument("CustomerContactList");
   		YFCElement outputTemplateElement = outputTemplateDoc.getDocumentElement();

		YFCElement orgTemplateElement = outputTemplateDoc.createElement("CustomerContact");
                		orgTemplateElement.setAttribute("UserID","");

		 */               

		YFCDocument outputTemplateDoc = YFCDocument.createDocument("CustomerAssignmentList");
		YFCElement outputTemplateElement = outputTemplateDoc.getDocumentElement();

		YFCElement assgElement = outputTemplateDoc.createElement("CustomerAssignment");
		assgElement.setAttribute("UserId","");


		YFCElement	custElement=outputTemplateDoc.createElement("Customer");
		custElement.setAttribute("CustomerID","");

		assgElement.appendChild(custElement);

		YFCElement	extnElement=outputTemplateDoc.createElement("Extn");
		extnElement.setAttribute("ExtnCustomerName","");
		extnElement.setAttribute("ExtnSuffixType","");

		custElement.appendChild(extnElement);
		assgElement.appendChild(custElement);
		outputTemplateElement.appendChild(assgElement);



		env.setApiTemplate("getCustomerAssignmentList", outputTemplateDoc.getDocument());
		Document getCustAssignDoc = api.invoke(env, "getCustomerAssignmentList", inputCustAssignmtDoc);
		env.clearApiTemplate("getCustomerAssignmentList");


		return  getCustAssignDoc;

	}

	//JIRA 3740 - Start [Modified operations defination added four attribute custElement - Current element,existingMsapName,isAnExistingMSAP - isCustomeravaiable condtion,strSAPName - NewSAPName]
	private void updateAllBillToandShipToWithMasterSAPAccountNumber(YFSEnvironment env, String organizationCode, String sapCustomerId, String newMSAPAccountNumber, String newMSAPName,Element custElement , String existingSAPName, boolean isAnExistingMSAP, String strSAPName) throws RemoteException
	{
		long updateBillShipsStartTime=System.currentTimeMillis();
		String sapCustomerKey= getCustomerKey(env, sapCustomerId,false);
		getBillToList(env, sapCustomerKey, organizationCode, newMSAPAccountNumber, newMSAPName,custElement,existingSAPName,isAnExistingMSAP,strSAPName);
		long updateBillShipsEndTime=System.currentTimeMillis();
		log.info("Extra Time taken to update ALL billTos and shipTos : ["+(updateBillShipsEndTime-updateBillShipsStartTime)+"]");
	}
	//JIRA 3740 - End
	/**
	 * Method to get all the bill to  from the level customer has logged in.
	 * @param env
	 * @param customerKey
	 * @param isAnExistingMSAP 
	 * @param strSAPName 
	 * @throws RemoteException
	 */
	//JIRA 3740 - Start [Modified operations defination added four attribute custElement - Current element,existingMsapName,isAnExistingMSAP - isCustomeravaiable condtion,strSAPName - NewSAPName
	private void getBillToList(YFSEnvironment env, String customerKey, String organizationCode, String newMSAPAccountNumber, String newMSAPName,Element custElement , String existingSAPName, boolean isAnExistingMSAP, String strSAPName) throws RemoteException {
		//form the input customer xml
		Document inputCustomerDoc = YFCDocument.createDocument("Customer").getDocument();
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("ParentCustomerKey", customerKey);
		Element extnElement = inputCustomerDoc.createElement("Extn");
		extnElement.setAttribute("ExtnSuffixTypeQryType", "LIKE");
		extnElement.setAttribute("ExtnSuffixType", "B");
		inputCustomerElement.appendChild(extnElement);
		System.out.println("getCustomerList - Extn Template : "+SCXmlUtil.getString(inputCustomerDoc));
		//get bill to customer list
		Document billtoListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		NodeList btNodeList = billtoListDoc.getElementsByTagName("Customer");
		int billtoLength = btNodeList.getLength();
		if(billtoLength != 0)
		{
			getShipToList(env, btNodeList, organizationCode, newMSAPAccountNumber, newMSAPName,custElement,existingSAPName,isAnExistingMSAP,strSAPName);
		}
	}
	//JIRA 3740 - End
	/**
	 * Method to get all the ship to for the bill to
	 * @param env
	 * @param btNodeList
	 * @param isAnExistingMSAP 
	 * @param strSAPName 
	 * @throws RemoteException
	 */
	//JIRA 3740 - Start [Modified operations defination added four attribute custElement - Current element,existingMsapName,isAnExistingMSAP - isCustomeravaiable condtion,strSAPName - NewSAPName
	private void getShipToList(YFSEnvironment env, NodeList btNodeList, String organizationCode, String newMSAPAccountNumber, String newMSAPName,Element custElement,String existingSAPName, boolean isAnExistingMSAP, String strSAPName) throws RemoteException 
	{
		int billtoLength = btNodeList.getLength();
		for(int Counter = 0;Counter < billtoLength;Counter++)
		{
			Element btElement = (Element)btNodeList.item(Counter);
			String billToCustomerID = btElement.getAttribute("CustomerID");
			arrChildCustomerIds.add(billToCustomerID);
			updateCustomerWithMSAPAccountNumber(env, organizationCode, billToCustomerID, "", newMSAPAccountNumber, newMSAPName, "B",custElement,existingSAPName,isAnExistingMSAP,strSAPName);

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
					String shipToCustomerID =  stElement.getAttribute("CustomerID");
					arrChildCustomerIds.add(shipToCustomerID);
					updateCustomerWithMSAPAccountNumber(env, organizationCode, shipToCustomerID, "", newMSAPAccountNumber, newMSAPName, "S",custElement,existingSAPName,isAnExistingMSAP,strSAPName);					
				}				
			}
		}
	}
	//JIRA 3740 - End
	
	
	private Connection getDBConnection(YFSEnvironment env,Document inputXML)
	{
		Connection m_Conn=null;
		try{
			YFSConnectionHolder connHolder     = (YFSConnectionHolder)env;
			m_Conn= connHolder.getDBConnection();
		}
		catch(Exception e){

			log.error("Exception: " + e.getStackTrace());
//			prepareErrorObject(e, "Item_Branch", XPXLiterals.E_ERROR_CLASS, env,inputXML);	

		}

		return m_Conn;
	}
}	

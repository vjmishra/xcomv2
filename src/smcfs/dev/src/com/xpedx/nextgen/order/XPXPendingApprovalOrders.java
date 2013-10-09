package com.xpedx.nextgen.order;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXEmailUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.xpedx.nextgen.customerprofilerulevalidation.api.XPXCustomerProfileRuleConstant;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCException;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXPendingApprovalOrders implements YIFCustomApi{

	private static YIFApi api = null;
	private String approverUserId = null;
	private String approverProxyUserId = null;
	private static YFCLogCategory log;
	String getItemUomMasterListTemplate = "global/template/api/getItemUomMasterList.XPXMasterUomLoad.xml";
	String getCustomerListCSREmailTemplate ="global/template/api/getCustomerList.XPXCSRFlagEmails.xml";
	static String extnCustLineAccLbl="";
    static String extnCustLinePOLbl="";
	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * Methods to check if the order needs an approval.
	 * This checks the ExtnTotalOrderValue and compares with the customer contacts Spending Limit
	 * If ExtnTotalOrderValue is GE to SpendingLimit then Hold Type ORDER_LIMIT_APPROVAL is applied to the order.
	 * To send Email to the approver and the contact you need to invoke a separate service with order Document as the input
	 */

	public Document invokeIsPendingApprovalOrder(YFSEnvironment env,Document inXML) throws Exception
	{		
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		Document customerContactOutputDoc=null;
		Double spendingLimit=null;
		String orderApprovalFlag=null;
		Document changeOrderOutput = null;
		String orderHeaderKey = inXML.getDocumentElement().getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
		if(orderHeaderKey == null || orderHeaderKey.trim().length() == 0)
		{
			YFCException exception = new YFCException();
			exception.setErrorDescription("Order Header Key is required for getting the order details");
			throw exception;
		}
		api = YIFClientFactory.getInstance().getApi();
		//Getting the Order Details - Input Doc to the getOrderList
		Document inputOrderDoc = YFCDocument.createDocument("Order").getDocument();
		Element inputOrderElement = inputOrderDoc.getDocumentElement();
		inputOrderElement.setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, orderHeaderKey);
		// Template to the getOrderList Api
		Document orderOutputTemplate = SCXmlUtil.createFromString("<Order DocumentType='' OrderNo='' OrderHeaderKey='' BillToID='' CustomerContactID='' EnterpriseCode='' DraftOrderFlag='' OrderType=''><Extn ExtnTotalOrderValue='' /><OrderHoldTypes><OrderHoldType/></OrderHoldTypes></Order>");
		env.setApiTemplate("getCompleteOrderDetails",orderOutputTemplate);
		Document orderOutputDoc = api.invoke(env, "getCompleteOrderDetails", inputOrderDoc);
		env.clearApiTemplate("getCompleteOrderDetails");
		Element orderElem = orderOutputDoc.getDocumentElement();
		String inputDraftOrderFlag =inXML.getDocumentElement().getAttribute("DraftOrderFlag");
		if(orderElem != null && "Y".equalsIgnoreCase(inputDraftOrderFlag) && "N".equalsIgnoreCase(orderElem.getAttribute("DraftOrderFlag")))
		{
			return orderOutputDoc;
		}	
		
		if(orderElem!=null) 
		{
			customerContactOutputDoc = getCustomerContactData(env, orderElem, inXML.getDocumentElement().getAttribute("CustomerContactID"));		
			if(customerContactOutputDoc!=null)
			{
				Element custContactListElement = customerContactOutputDoc.getDocumentElement();
				if(SCXmlUtil.getIntAttribute(custContactListElement, "TotalNumberOfRecords")>0)
				{
					NodeList extnNodeList = custContactListElement.getElementsByTagName("Extn");
					if(extnNodeList!=null)
					{
						Element extnElem=(Element)extnNodeList.item(0);
						if(extnElem!=null)
						{
							orderApprovalFlag = extnElem.getAttribute("ExtnOrderApprovalFlag");
							
						}
					}
					Element custContactElement = SCXmlUtil.getChildElement(custContactListElement, "CustomerContact");
					approverUserId = custContactElement.getAttribute("ApproverUserId");
					approverProxyUserId = custContactElement.getAttribute("ApproverProxyUserId");
					
					if(!"Y".equalsIgnoreCase(orderApprovalFlag))
					{
						String spndgLimitString = custContactElement.getAttribute("SpendingLimit");					
						if(!SCUtil.isVoid(spndgLimitString))
						{
							Double spLimit = Double.parseDouble(spndgLimitString);
							if(spLimit > 0)
							{
								spendingLimit = spLimit;
							}
						}
						else
						{
							spendingLimit = Double.valueOf(-1);
						}
						// checking if the order total is more than the spending limit
						if(spendingLimit!=null && orderElem!=null && spendingLimit!=-1) 
						{
							Element extnElem = SCXmlUtil.getChildElement(orderElem,"Extn");
							String ExtnTotalOrderValue = extnElem.getAttribute("ExtnTotalOrderValue");
							Double totalOrderValue = new Double(0);
							if(!SCUtil.isVoid(ExtnTotalOrderValue))
							{
								totalOrderValue= Double.parseDouble(ExtnTotalOrderValue);
							}
							
							if(totalOrderValue >= spendingLimit)
							{
								orderApprovalFlag="Y";
							
							} else {
								
								if("N".equals(orderElem.getAttribute("DraftOrderFlag")) && "Customer".equals(orderElem.getAttribute(XPXLiterals.A_ORDER_TYPE)))
								{
									Element orderHoldTypesElem = SCXmlUtil.getChildElement(orderElem, XPXLiterals.E_ORDER_HOLD_TYPES);
									if(orderHoldTypesElem!=null)
									{
										ArrayList<Element> orderHoldTypeElemList = SCXmlUtil.getChildren(orderHoldTypesElem, XPXLiterals.E_ORDER_HOLD_TYPE);
										if(orderHoldTypeElemList!=null && orderHoldTypeElemList.size()>0)
										{
											for(Element orderHoldTypeElem: orderHoldTypeElemList)
											{
												if(orderHoldTypeElem!=null)
												{
													String holdType = orderHoldTypeElem.getAttribute(XPXLiterals.A_HOLD_TYPE);
										        	String holdStatus = orderHoldTypeElem.getAttribute(XPXLiterals.A_STATUS);
													if(XPXLiterals.PENDING_APPROVAL_HOLD.equals(holdType) && XPXLiterals.PENDING_APPROVAL_ACTIVE_STATUS_ID.equals(holdStatus))
													{
														changeOrderOutput = YFCDocument.createDocument(XPXLiterals.E_ORDER).getDocument();
														Element changeOrderOutputElem = changeOrderOutput.getDocumentElement();
														changeOrderOutputElem.setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, orderElem.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY));
														Element holdTypesElem = changeOrderOutput.createElement(XPXLiterals.E_ORDER_HOLD_TYPES);
														Element holdTypeElem = changeOrderOutput.createElement(XPXLiterals.E_ORDER_HOLD_TYPE);
														holdTypeElem.setAttribute(XPXLiterals.A_HOLD_TYPE, XPXLiterals.PENDING_APPROVAL_HOLD);
														holdTypeElem.setAttribute(XPXLiterals.A_STATUS, XPXLiterals.PENDING_APPROVAL_RELEASE_STATUS_ID);
														holdTypeElem.setAttribute(XPXLiterals.HOLD_RELEASE_DESC, XPXLiterals.PENDING_APPROVAL_RELEASE_DESC);
														
														holdTypesElem.appendChild(holdTypeElem);
														changeOrderOutputElem.appendChild(holdTypesElem);
														break;
														
													}
												}
											}
											
										}
									}
								}
								
							}
						}
					}
				}
			}			
		}
		
		if("Y".equalsIgnoreCase(orderApprovalFlag))
		{
			if(approverUserId != null || approverProxyUserId != null)
			{//if at least there is one approver put the order on Hold
				String approverOnHold = null;
				if(approverUserId != null && approverUserId.trim().length()>0)
				{
					approverOnHold = approverUserId+",";//Added for EB 70
					if(approverProxyUserId != null && approverProxyUserId.trim().length()>0)
					{
						approverOnHold = approverOnHold + approverProxyUserId+",";//Added for EB 70
					}
				
				}else
				{
					approverOnHold = approverProxyUserId+",";//Added for EB 70
				}
				//end of jira 3484
				if(approverOnHold != null && approverOnHold.trim().length()>0) 
				{
					changeOrderOutput = applyHoldTypeOnOrder(env,orderElem,approverOnHold);					
				}
			}
		}
		if(changeOrderOutput != null)	
		{
			return changeOrderOutput;
		}
		else
		{
			return inXML;
		}
	}
	
	/**
	 * JIRA 4256 Start
	 * @param env
	 * @param orderElement
	 * @return
	 */
	public String getViewPricesFlag(YFSEnvironment env,Element orderElement) {
		//String customerContactId = orderElement.getAttribute("CustomerContactID");
		String organizationCode  = orderElement.getAttribute("EnterpriseCode");
		String viewPriceFlag="";
		String customerContactId=orderElement.getAttribute("CustomerContactID");
		if(customerContactId!=null && customerContactId.trim().length()>0 && organizationCode!=null && organizationCode.trim().length()>0 ) {
			//creating the document to get the customer contacts spending limit 
			Document inputCustContactDoc = YFCDocument.createDocument("CustomerContact").getDocument();
			Element inputCustContactElem = inputCustContactDoc.getDocumentElement();
			inputCustContactElem.setAttribute("CustomerContactID", customerContactId);
			inputCustContactElem.setAttribute("OrganizationCode", organizationCode);
			//setting the Api template for the getCustomerContactList Api and invoking the api
			Document Template = SCXmlUtil.createFromString("<CustomerContactList TotalNumberOfRecords=''><CustomerContact><Extn ExtnViewPricesFlag=''/></CustomerContact></CustomerContactList>");
			env.setApiTemplate("getCustomerContactList",Template);
			//invoking the api
			Document outputDoc =null;
			try {
				outputDoc = api.invoke(env, "getCustomerContactList", inputCustContactDoc);
				NodeList extnNodeList = outputDoc.getDocumentElement().getElementsByTagName("Extn");
				if(extnNodeList!=null)
				{
					Element extnElem=(Element)outputDoc.getDocumentElement().getElementsByTagName("Extn").item(0);
					if(extnElem!=null)
					{
						viewPriceFlag = extnElem.getAttribute("ExtnViewPricesFlag");
					}
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			env.clearApiTemplate("getCustomerContactList");

		}

		return viewPriceFlag;
	}
	/**
	 * JIRA 4256 End
	 * @param env
	 * @param orderElement
	 * @param customerContactId
	 * @return
	 */
	public Document getCustomerContactData(YFSEnvironment env, Element orderElement, String customerContactId) {
		String organizationCode  = orderElement.getAttribute("EnterpriseCode");
		Document outputDoc =null;
		if(customerContactId!=null && customerContactId.trim().length()>0 && organizationCode!=null && organizationCode.trim().length()>0 ) {
			//creating the document to get the customer contacts spending limit 
			Document inputCustContactDoc = YFCDocument.createDocument("CustomerContact").getDocument();
			Element inputCustContactElem = inputCustContactDoc.getDocumentElement();
			inputCustContactElem.setAttribute("CustomerContactID", customerContactId);
			inputCustContactElem.setAttribute("OrganizationCode", organizationCode);
			Element customerElem = inputCustContactDoc.createElement("Customer");
			customerElem.setAttribute("CustomerID", orderElement.getAttribute("BillToID"));
			inputCustContactElem.appendChild(customerElem);
			//setting the Api template for the getCustomerContactList Api and invoking the api
			Document Template = SCXmlUtil.createFromString("<CustomerContactList TotalNumberOfRecords=''><CustomerContact><Extn ExtnOrderApprovalFlag=''/></CustomerContact></CustomerContactList>");
			env.setApiTemplate("getCustomerContactList",Template);
			//invoking the api			
			try {
				outputDoc = api.invoke(env, "getCustomerContactList", inputCustContactDoc);
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			env.clearApiTemplate("getCustomerContactList");
			
		}
		return outputDoc;
	}

	public Document applyHoldTypeOnOrder(YFSEnvironment env, Element orderElement, String approver){
		//Document changeOrderOutput = null;
		Document changeOrderDoc = null;
		if(orderElement!=null && approver!=null) {
			/*String isEditOrder="";
			if (env instanceof ClientVersionSupport) {
				ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
				HashMap map  = clientVersionSupport.getClientProperties();
				if (map != null) {
					isEditOrder=(String) map.get("isEditOrderPendingOrderApproval");
				}
			}*/

			// Applying the Hold Type ORDER_LIMIT_APPROVAL on the order with one of the above approvers
			changeOrderDoc = YFCDocument.createDocument("Order").getDocument();
			Element changeOrderInputElem = changeOrderDoc.getDocumentElement();
			changeOrderInputElem.setAttribute("OrderHeaderKey", orderElement.getAttribute("OrderHeaderKey"));
			Element holdTypes = changeOrderDoc.createElement("OrderHoldTypes");
			Element holdType = changeOrderDoc.createElement("OrderHoldType");
			holdType.setAttribute("HoldType", "ORDER_LIMIT_APPROVAL");
			holdType.setAttribute("Status", "1100");
			holdType.setAttribute("ResolverUserId", approver);
			holdTypes.appendChild(holdType);
			changeOrderInputElem.appendChild(holdTypes);
			/*if(isEditOrder == null || !"true".equals(isEditOrder )) {
				//invoking the change Order to apply the hold type
				try {
					changeOrderOutput = api.invoke(env, "changeOrder", changeOrderInputDoc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				return changeOrderInputDoc;
			}*/
		}
		return changeOrderDoc;
		//return changeOrderOutput;
	}

	/*
	 *This is invoked when sending approval email to the customer contact. 
	 *Taking the CustomerContactID on the order and getting the Email ID 
	 *and this method appends the CustomerContactList Element to the order, so it will be easy when sending the email.
	 */

	public Document invoekApprovalOrderEmailForContact(YFSEnvironment env,Document inXML) throws Exception {


		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		String resolverUserIds[] = null;
		String orderStatusSubjectlLine=null;
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			log.error("Error getting the Api Instance, So returning the Document");
			// TODO Auto-generated catch block
			e.printStackTrace();
			appendDummyContactList(inXML);
			return inXML;
		}

		if (log.isDebugEnabled()) {
			log.debug("Inside XPXPendingApprovalOrders.invoekApprovalOrderEmailForContact - Input XML : "+SCXmlUtil.getString(inXML) );

		}
		if(inXML!=null) {

			Element orderElement = inXML.getDocumentElement();
			/*JIRA 4256 Start
			 * 
			 */
			String priceFlag = getViewPricesFlag(env,orderElement);
			orderElement.setAttribute("viewPricesFlag",priceFlag);
			/*
			 * JIRA 4256 End
			 */
			XPXUtils utilObj = new XPXUtils();
			inXML = utilObj.stampBrandLogo(env, inXML);
			String customerContactOnOrder = SCXmlUtil.getAttribute(orderElement, "CustomerContactID");
			Element inputElem = SCXmlUtil.getChildElement(orderElement, "Input");
			Element orderHoldTypeElem =null;
			if (inputElem != null) {
				orderHoldTypeElem = SCXmlUtil.getChildElement(inputElem, "OrderHoldType");
				if (orderHoldTypeElem != null) {
					String resolverUserId = null;
					String delimiter = ",";
					if (orderHoldTypeElem.hasAttribute("ResolverUserId")) {
						resolverUserId = orderHoldTypeElem.getAttribute("ResolverUserId");
					}
					if (resolverUserId != null && !resolverUserId.equalsIgnoreCase("")) {
						resolverUserId = resolverUserId + "," + customerContactOnOrder;
						resolverUserIds = resolverUserId.split(delimiter);
					} else {
						resolverUserIds[0] = customerContactOnOrder;
					}
				}
			}
			String emailType="";	        
			String readEnv = YFSSystem.getProperty("environment");
			inXML.getDocumentElement().setAttribute("EnvironmentID", readEnv);
			String organizationCode = SCXmlUtil.getAttribute(orderElement, "EnterpriseCode");
			String sellerOrgCode = inXML.getDocumentElement().getAttribute("SellerOrganizationCode");
			String holdStatus=orderHoldTypeElem.getAttribute("Status");
            String emailSubject = null;
			if(orderHoldTypeElem!=null && ("1100").equalsIgnoreCase(holdStatus))
			{   
				orderStatusSubjectlLine="Order Pending Approval Notification";
				emailType=XPXEmailUtil.ORDER_PENDING_APPROVAL_EMAIL_TYPE;
				utilObj.stampOrderChangeStatusSubjectLine(env, orderElement ,holdStatus, orderStatusSubjectlLine);
				emailSubject = utilObj.getOrderStatusSubjectLine(env, orderElement, holdStatus, orderStatusSubjectlLine);

			}
			else if (orderHoldTypeElem!=null && ("1200").equalsIgnoreCase(holdStatus))
			{   
				orderStatusSubjectlLine="Order Rejected Notification";
				emailType=XPXEmailUtil.ORDER_REJECTED_EMAIL_TYPE;
				utilObj.stampOrderChangeStatusSubjectLine(env, orderElement ,holdStatus, orderStatusSubjectlLine);
				emailSubject = utilObj.getOrderStatusSubjectLine(env, orderElement, holdStatus, orderStatusSubjectlLine);
			}
			else if(orderHoldTypeElem!=null && ("1300").equalsIgnoreCase(holdStatus))
			{
				orderStatusSubjectlLine="Order Approved Notification";
				emailType=XPXEmailUtil.ORDER_APPROVED_EMAIL_TYPE;
				Element orderExtn = SCXmlUtil.getChildElement(orderElement, "Extn");
				String formattedLegacyOrderNo = getLegacyOrderNumber(orderExtn,env);
				inXML.getDocumentElement().setAttribute("FormattedOrderNo", formattedLegacyOrderNo);
				utilObj.stampOrderChangeStatusSubjectLine(env, orderElement, holdStatus, orderStatusSubjectlLine);
				emailSubject = utilObj.getOrderStatusSubjectLine(env, orderElement, holdStatus, orderStatusSubjectlLine);
			}

			Map<String,String> getUOMListMap = getUOMList(env);

			ArrayList<Element> orderLineElementList= SCXmlUtil.getElements(orderElement, "OrderLines/OrderLine");
			if(orderLineElementList != null)
			{
				for(int i=0;i<orderLineElementList.size();i++)
				{
					Element orderLineElement=orderLineElementList.get(i);
					if(orderLineElement!=null)
					{
						if("1300".equals(holdStatus) && ("Cancelled".equals(orderLineElement.getAttribute("Status")) || "Canceled".equals(orderLineElement.getAttribute("Status"))))
						{
							Element orderLinesElement=SCXmlUtil.getChildElement(orderElement, "OrderLines");
							if(orderLinesElement!=null)
							{
								orderLinesElement.removeChild(orderLineElement);
								continue;
							}
						}
						
						Element orderLineTranQty=(Element)orderLineElement.getElementsByTagName("OrderLineTranQuantity").item(0);
						if(orderLineTranQty !=null )
						{
							String checkUOMDescription = getUOMListMap.get(orderLineTranQty.getAttribute("TransactionalUOM"));
							if(checkUOMDescription!=null && !checkUOMDescription.equals("")){
								orderLineTranQty.setAttribute("UOMDescription", getUOMListMap.get(orderLineTranQty.getAttribute("TransactionalUOM")));
							}else{
								orderLineTranQty.setAttribute("UOMDescription", orderLineTranQty.getAttribute("TransactionalUOM"));
	
							}
						}
						
						Element orderLineExtnElem=(Element)orderLineElement.getElementsByTagName("Extn").item(0);
						if(orderLineExtnElem !=null )
						{
							String checkUOMPriceDesc = getUOMListMap.get(orderLineExtnElem.getAttribute("ExtnPricingUOM"));
							if(checkUOMPriceDesc!=null && !checkUOMPriceDesc.equals("")){
								orderLineExtnElem.setAttribute("ExtnPricingUOMDescription", getUOMListMap.get(orderLineExtnElem.getAttribute("ExtnPricingUOM")));
	
							}else{
								orderLineExtnElem.setAttribute("ExtnPricingUOMDescription", orderLineExtnElem.getAttribute("ExtnPricingUOM"));
	
							}	
						}
					}
				}
			}
			String baseURL = null;
			String toApproveOrderURL =null;
			String approvedOrderURL = null;
			//stamp the approval related URLs.
            if("Saalfeld".equalsIgnoreCase(organizationCode)){ 
            	baseURL = YFSSystem.getProperty("baseSaalURL");
            	toApproveOrderURL = baseURL + "/order/approvalList.action?sfId="+ organizationCode +"&scFlag=y"; //http://stg.xpedx.com/swc/order/approvalList.action?sfId=xpedx&scFlag=y 
            	approvedOrderURL=  baseURL + "/order/orderList.action?sfId="+ organizationCode + "&scFlag=Y" ;
            }else{
            	baseURL = YFSSystem.getProperty("baseURL");
            	toApproveOrderURL = baseURL + "/order/approvalList.action?sfId="+ sellerOrgCode +"&scFlag=y"; //http://stg.xpedx.com/swc/order/approvalList.action?sfId=xpedx&scFlag=y 
            	approvedOrderURL=  baseURL + "/order/orderList.action?sfId="+ sellerOrgCode + "&scFlag=Y" ;
            }
            
			inXML.getDocumentElement().setAttribute("toApproveOrderURL",toApproveOrderURL);
			inXML.getDocumentElement().setAttribute("approvedOrderURL",approvedOrderURL);



			/* if same contact exists for different customers (multiple contacts), taking the customer contact based on the BillToID on the order,
			 * which will be the MSAP of the contact who placed the order.
			 */
			if(!SCUtil.isVoid(organizationCode) && !SCUtil.isVoid(customerContactOnOrder)) // Order will not have customer contact id stamped in case of B2B
			{
				Document custContInputDoc = YFCDocument.createDocument("CustomerContact").getDocument();
				Element inputElement = custContInputDoc.getDocumentElement();
				Element custElement = custContInputDoc.createElement("Customer");
				custElement.setAttribute("OrganizationCode", organizationCode );
				Element complexQuery = custContInputDoc.createElement("ComplexQuery");

				Element OrElem = custContInputDoc.createElement("Or"); 

				for(String contactId : resolverUserIds) {
					Element exp = custContInputDoc.createElement("Exp");
					exp.setAttribute("Name", "CustomerContactID");
					exp.setAttribute("Value", contactId);
					SCXmlUtil.importElement(OrElem, exp);
				}
				complexQuery.appendChild(OrElem);
				inputElement.appendChild(complexQuery);
				inputElement.appendChild(custElement);// appending the customer element
				// setting the Api template
				Document Template = SCXmlUtil.createFromString("<CustomerContactList TotalNumberOfRecords=''><CustomerContact/></CustomerContactList>");
				env.setApiTemplate("getCustomerContactList", Template);
				Document outputDoc =null;
				try {
					outputDoc = api.invoke(env, "getCustomerContactList", custContInputDoc);
					env.clearApiTemplate("getCustomerContactList");
				}
				catch (Exception e) {
					log.error("Error invoking the getCustomerContactList to get the EmailID of the Contact who placed the order");
					e.printStackTrace();
					env.clearApiTemplate("getCustomerContactList");
				}				
				if(outputDoc!=null) {

					SCXmlUtil.importElement(orderElement, outputDoc.getDocumentElement());//(inXML, outputDoc.getDocumentElement());

					addEmailIDToElement(orderElement);
					//order.appendChild(outputDoc.getDocumentElement());// Appends the CustomerContacList Element to the Order and returns the order Document
				}
				else {
					appendDummyContactList(inXML);
				}
			}
			else {
				appendDummyContactList(inXML);
			}
			/*XB-461 : Begin - Sending email through Java Mail API now*/
			String emailOrgCode=sellerOrgCode!=null?sellerOrgCode:"";
			
			Document inputDoc = orderElement.getOwnerDocument();
			String buyerOrgCode = orderElement.getAttribute("BuyerOrganizationCode");
			if(buyerOrgCode != null){
			Map<String,String>getCustIDMap = getCustomerID(buyerOrgCode,env);
			if(getCustIDMap.size() >0){
				String billtoID = getCustIDMap.get("BillToCustomerID");
				inputDoc = setCSREmails(env,inputDoc,billtoID);
				}
			}	
			NodeList extnElementList = inputDoc.getElementsByTagName("Extn");
            int length = extnElementList.getLength();
            if (length != 0) {
            	  Element extnElement = (Element) extnElementList.item(0);
            	  if(extnCustLinePOLbl!=null && !extnCustLinePOLbl.trim().equals(""))
            	  {
                  	extnElement.setAttribute("ExtnCustLinePOLbl", extnCustLinePOLbl); 
                  }
            	  if(extnCustLineAccLbl!=null && !extnCustLineAccLbl.trim().equals(""))
                  {
                  	extnElement.setAttribute("ExtnCustLineAccLbl", extnCustLineAccLbl); 
                  }
            }
			String inputXML=SCXmlUtil.getString(inputDoc.getDocumentElement());
			//EB-2069-As a Saalfeld product owner, I want to view the Approved Notification Email with correct Saalfeld branding 
			String emailFrom=null;
			if((inputDoc.getDocumentElement().getAttribute("EnterpriseCode")!=null && "Saalfeld".equalsIgnoreCase(inputDoc.getDocumentElement().getAttribute("EnterpriseCode"))) )// no need to check for Enterprise organization code)
			    {
	                  emailFrom=YFSSystem.getProperty("saalFeldEMailFromAddresses");  // new attribute defined in customer_overides properties….

				} else {
					emailFrom = YFSSystem.getProperty("EMailFromAddresses");
				}
			//String emailSubject = orderElement.getAttribute("Subject")!=null?orderElement.getAttribute("Subject"):"";
			String businessIdentifier = SCXmlUtil.getXpathAttribute(orderElement,"./Extn/@ExtnWebConfNum");
			XPXEmailUtil.insertEmailDetailsIntoDB(env,inputXML, emailType, emailSubject, emailFrom, emailOrgCode,businessIdentifier);
			/*XB-461 : End - Sending email through Java Mail API now*/
		}

		return inXML;		
	}
	
	public Document setCSREmails(YFSEnvironment env, Document inputDoc,String billToId)
			throws ParserConfigurationException, FactoryConfigurationError,
			YFSException, RemoteException, YIFClientCreationException {

				api = YIFClientFactory.getInstance().getApi();

				//		if(api == null){
				//			log.debug(" inside null for API ");
				//			api = YIFClientFactory.getInstance().getApi();
				//			log.debug("api  --> " + api);
				//		}
				log.debug("setCSREmails " +  SCXmlUtil.getString(inputDoc));


				YFCDocument docCustomer = YFCDocument.createDocument("Customer");
				YFCElement inputDocElement = docCustomer.getDocumentElement();

				inputDocElement.setAttribute("CustomerID",billToId); //JIRA 4093 -Changes done

				//String billToId = customerDoc.getDocumentElement().getAttribute("BillToID");


				if(billToId != null && !billToId.equalsIgnoreCase(""))
				{
					env.setApiTemplate("getCustomerList", getCustomerListCSREmailTemplate);
					log.debug("env " + env);
					log.debug("docCustomer  " + SCXmlUtil.getString(docCustomer.getDocument()));
					log.debug(" getCustomerListCSREmailTemplate " + getCustomerListCSREmailTemplate);
					//log.debug(" api " + api.toString());
					Document custListDoc = api.getCustomerList(env,docCustomer.getDocument());
					log.debug("api getCustomerList outdoc "
							+ SCXmlUtil.getString(custListDoc));
					if(log.isDebugEnabled()){
						log.debug("SCXmlUtil.getString(custListDoc) +++++++serCSREmails++++++"+SCXmlUtil.getString(custListDoc));
					}
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
							log.debug("strExtnECsr1EMailID ::"
									+ strExtnECsr1EMailID);
							inputDoc.getDocumentElement().setAttribute(
									"strExtnECsr1EMailID", strExtnECsr1EMailID);
							
						}
						strExtnECSR2EmailConfirmFlag = custExtnElement
						.getAttribute("ExtnECSR2EmailConfirmFlag");
						if (!strExtnECSR2EmailConfirmFlag.equalsIgnoreCase("N")) {
							strExtnECsr2EMailID = custExtnElement
							.getAttribute("ExtnECsr2EMailID");
							log.debug("strExtnECsr2EMailID ::"
									+ strExtnECsr2EMailID);
							inputDoc.getDocumentElement().setAttribute(
									"strExtnECsr2EMailID", strExtnECsr2EMailID);
							
						}

					}

					
				}
				System.out.println("input doc : "+ SCXmlUtil.getString(inputDoc));
				return inputDoc;
				
			}
	
	public static Map<String, String> getCustomerID(String customerId, YFSEnvironment env) throws Exception{
		if(log.isDebugEnabled()){
			log.debug("getCustomerID ++++++++++++++++++++++"+customerId);
		}

		String msapId="";
		String biltoId = "";
		Map<String,String> customerIDMAP = new HashMap<String,String>();

		Document inputDoc = SCXmlUtil.createDocument("XPXCustHierachyView");
		inputDoc.getDocumentElement().setAttribute("ShipToCustomerID", customerId);
		
		
		Document outDocument =api.executeFlow(env, "XPXCustomerHierarchyViewService", inputDoc);
		
		Element outputElement = null;
		if(outDocument != null) {
		outputElement = outDocument.getDocumentElement();
		}
		if(log.isDebugEnabled()){
			log.debug("************getCustomerID : outputElement="+SCXmlUtil.getString(outputElement));
		}
		
		NodeList customerHierView = outputElement.getElementsByTagName("XPXCustHierarchyView");
		for(int j=0;j<customerHierView.getLength();j++) {
			Element customerHierViewElem = (Element)customerHierView.item(j);
			if(log.isDebugEnabled()){
				log.debug("************customerHierViewElem : outputDoc="+SCXmlUtil.getString(customerHierViewElem));
			}

			msapId = customerHierViewElem.getAttribute("MSAPCustomerID");
			biltoId = customerHierViewElem.getAttribute("BillToCustomerID");
			extnCustLineAccLbl= customerHierViewElem.getAttribute("SAPExtnCustLineAccLbl"); // SAPExtnCustLineAccLbl
	        extnCustLinePOLbl=customerHierViewElem.getAttribute("SAPExtnCustLinePOLbl"); //SAPExtnCustLinePOLbl
	
			if(msapId!=null && !msapId.trim().equals("")){
				customerIDMAP.put("MSAPCustomerID",msapId);
			}
			if(biltoId!=null && !biltoId.trim().equals("")){
				customerIDMAP.put("BillToCustomerID",biltoId);
			}

		}	
		if(log.isDebugEnabled()){
			log.debug("customerIDMAP ++++++++++++++++++++"+customerIDMAP.size());
		}
		if(log.isDebugEnabled()){
			log.debug("************getCustomerID : msapId="+msapId);
		}
		return customerIDMAP;
	}


	private void addEmailIDToElement(Element orderElement)
	{
		String orderCustomerContactId=orderElement.getAttribute("CustomerContactID");
		Element orderElemExtn = SCXmlUtil.getChildElement(orderElement,"Extn"); //CR 4054
		ArrayList<Element> customerContactsList=SCXmlUtil.getElements(orderElement, "CustomerContactList/CustomerContact");
		String toEmailID="";
		String ccEmailId="";
		for(int i=0;i<customerContactsList.size();i++)
		{
			Element customerContact=customerContactsList.get(i);
			String custometContactId=customerContact.getAttribute("CustomerContactID");
			if(custometContactId !=null && custometContactId.equals(orderCustomerContactId))
			{
				String tempccEmailId=customerContact.getAttribute("EmailID");
				if(tempccEmailId != null && tempccEmailId.length()>0)
					ccEmailId=tempccEmailId;
			}
			else
			{
				if(toEmailID != null && toEmailID.length()>0)
					toEmailID=toEmailID+","+customerContact.getAttribute("EmailID");
				else
					toEmailID=customerContact.getAttribute("EmailID");
			}
		}
		if(customerContactsList.size() >1)
		{
			Element customerContact=customerContactsList.get(0);
			customerContact.setAttribute("ToEmailID", toEmailID);
			customerContact.setAttribute("CCEmailID", ccEmailId);
		}
		/*
		 * CR 4054 Changes start
		 * */
		String addtionalEmailAdd = orderElemExtn.getAttribute("ExtnAddnlEmailAddr");

		if(addtionalEmailAdd!=null && addtionalEmailAdd.trim().length() > 0){
			if (addtionalEmailAdd.indexOf(";") != -1) {

				addtionalEmailAdd = addtionalEmailAdd.replaceAll(";", ",");
				if(addtionalEmailAdd.substring(addtionalEmailAdd.length()-1).equals(","))
				{
					addtionalEmailAdd = addtionalEmailAdd.substring(0,
							addtionalEmailAdd.length() - 1);
				}

			}
			orderElemExtn.setAttribute("ExtnAddnlEmailAddr",
					addtionalEmailAdd);
		}
		/**
		 * CR 4054 Changes end
		 */
	}
	private void adduomDescription(ArrayList<Element> orderLinesElement,Map<String,String> uomDesriptionMap)
	{

		if(orderLinesElement != null)
		{
			for(int i=0;i<orderLinesElement.size();i++)
			{
				Element orderElement=orderLinesElement.get(i);
				Element orderLineTranQty=(Element)orderElement.getElementsByTagName("OrderLineTranQuantity").item(0);

				if(orderLineTranQty !=null )
				{
					String checkUOMDescription = uomDesriptionMap.get(orderLineTranQty.getAttribute("TransactionalUOM"));
					if(checkUOMDescription!=null && !checkUOMDescription.equals("")){
						orderLineTranQty.setAttribute("UOMDescription", uomDesriptionMap.get(orderLineTranQty.getAttribute("TransactionalUOM")));
					}else{
						orderLineTranQty.setAttribute("UOMDescription", orderLineTranQty.getAttribute("TransactionalUOM"));

					}
				}
				Element orderLineExtnElem=(Element)orderElement.getElementsByTagName("Extn").item(0);

				if(orderLineExtnElem !=null )
				{
					String checkUOMPriceDesc = uomDesriptionMap.get(orderLineExtnElem.getAttribute("ExtnPricingUOM"));
					if(checkUOMPriceDesc!=null && !checkUOMPriceDesc.equals("")){
						orderLineExtnElem.setAttribute("ExtnPricingUOMDescription", uomDesriptionMap.get(orderLineExtnElem.getAttribute("ExtnPricingUOM")));

					}else{
						orderLineExtnElem.setAttribute("ExtnPricingUOMDescription", orderLineExtnElem.getAttribute("ExtnPricingUOM"));

					}

				}
			}
		}
	}
	private Map<String,String> getUOMList(YFSEnvironment env){
		Map<String,String> UOMDesriptionMap = new HashMap<String,String>();
		try
		{
			Document getItemUomMasterListInputDoc = YFCDocument.createDocument("ItemUOMMaster").getDocument();
			getItemUomMasterListInputDoc.getDocumentElement().setAttribute("UnitOfMeasure","" );
			getItemUomMasterListInputDoc.getDocumentElement().setAttribute("OrganizationCode", "");
			env.setApiTemplate("getItemUOMMasterList", getItemUomMasterListTemplate);
			Document getItemUomMasterListOutputDoc = api.invoke(env, "getItemUOMMasterList", getItemUomMasterListInputDoc);
			env.clearApiTemplate("getItemUOMMasterList");
			env.clearApiTemplate("getItemUOMMasterList");
			NodeList itemUOMMasterList = getItemUomMasterListOutputDoc.getDocumentElement().getElementsByTagName("ItemUOMMaster");
			if (itemUOMMasterList != null) 
			{
				int itemUOMLength = itemUOMMasterList.getLength();
				if(itemUOMLength > 0) 
				{
					for(int i=0; i < itemUOMLength ;i++)
					{
						String itemUomDescriptionMaster ="";
						String itemUomMaster = "";
						Element itemUomMasterElement = (Element) itemUOMMasterList.item(i);
						if (itemUomMasterElement.hasAttribute("Description")) 
						{
							itemUomDescriptionMaster = itemUomMasterElement.getAttribute("Description");
							itemUomMaster = itemUomMasterElement.getAttribute("UnitOfMeasure");
						} 
						else 
						{
							itemUomDescriptionMaster = "";
						}
						UOMDesriptionMap.put(itemUomMaster, itemUomDescriptionMaster);
					}
				}
			}
		}
		catch(Exception e)
		{
			log.error("Error while getting UOM Description "+e.getMessage());
		}
		return UOMDesriptionMap;
	}

	private void appendDummyContactList(Document inXML) {
		Element order = inXML.getDocumentElement();
		// Creating the Dummy Element of CusotmerCotnact so that xsl wouldn't throw any error
		Document dummy = SCXmlUtil.createFromString("<CustomerContactList TotalNumberOfRecords=''><CustomerContact EmailID='' FirstName='' LastName=''" +
				"CustomerContactID='' /></CustomerContactList>");
		SCXmlUtil.importElement(order, dummy.getDocumentElement());
	}

	public static String getLegacyOrderNumber(Element orderElement,
			YFSEnvironment env) throws YFSException, RemoteException {
		HashSet<String> orderHashset = new HashSet<String>();
		String orderNos = "";
		if (orderElement != null) {
			String extnWebConfNum = orderElement.getAttribute("ExtnWebConfNum");
			if (!YFCObject.isVoid(extnWebConfNum)) {
				Document inputOrderDoc = SCXmlUtil.createDocument("Order");
				Element inputOrderElement = inputOrderDoc.getDocumentElement();
				Element extnElement = inputOrderDoc.createElement("Extn");
				extnElement.setAttribute("ExtnWebConfNum", extnWebConfNum);
				inputOrderElement.appendChild(extnElement);
				Document getOrderListTemplate = SCXmlUtil
						.createFromString("<OrderList><Order OrderHeaderKey='' OrderType=''><Extn ExtnLegacyOrderNo='' ExtnCustomerDivision='' ExtnOrderDivision='' ExtnGenerationNo='' /></Order></OrderList>");
				env.setApiTemplate("getOrderList", getOrderListTemplate);
				if (log.isDebugEnabled()) {
					log.debug("Inside XPXPendingApprovalOrders.getFormattedOrderNumber() - getOrderList_InXML : "+SCXmlUtil.getString(inputOrderDoc));				
				}
				Document orderListDoc = api.invoke(env, "getOrderList",
						inputOrderDoc);
				if (log.isDebugEnabled()) {
					log.debug("Inside XPXPendingApprovalOrders.getFormattedOrderNumber() - getOrderList_OutXML : "+SCXmlUtil.getString(orderListDoc));				
				}

				env.clearApiTemplate("getOrderList");
				Element rootElement = orderListDoc.getDocumentElement();
				NodeList orderNodeList = rootElement
						.getElementsByTagName("Order");
				int orderNodeListLength = orderNodeList.getLength();
				for (int order = 0; order < orderNodeListLength; order++) {
					Element orderEle = (Element) orderNodeList.item(order);
					String orderType = orderEle.getAttribute("OrderType");
					if (!YFCObject.isVoid(orderType) && !orderType.equalsIgnoreCase("Customer"))
					{
						if(orderEle.getElementsByTagName("Extn")!=null)
						{
							Element extnElementFO = (Element) orderEle
									.getElementsByTagName("Extn").item(0);
							String legacyOrderNum = extnElementFO
									.getAttribute("ExtnLegacyOrderNo");
							String orderBranch = extnElementFO
									.getAttribute("ExtnOrderDivision");
							String generationNum = extnElementFO
									.getAttribute("ExtnGenerationNo");
							if (null != orderBranch && !"".equalsIgnoreCase(orderBranch.trim()) && 
									null != legacyOrderNum   && !"".equalsIgnoreCase(legacyOrderNum.trim())   && 
									null != generationNum && !"".equalsIgnoreCase(generationNum.trim())) 
							{
								String orderNbr = XPXUtils.getFormattedOrderNumber(orderBranch, legacyOrderNum, generationNum);
								orderHashset.add(orderNbr);							
							}
						}
					}
				}
			}
		}

		if (orderHashset.isEmpty()) {
			orderNos = "In Progress";

		}else
		{
			if (orderHashset != null && orderHashset.size() > 0) {
				int i=1;
				Iterator<String> orderItr=orderHashset.iterator();
				while(orderItr.hasNext()){
					if(i==orderHashset.size()) {
						orderNos = orderNos+orderItr.next();

					}else {
						orderNos = orderNos+orderItr.next()+",";
					}
					++i;					
				}
			}
		}		
		return orderNos;

	}	
}

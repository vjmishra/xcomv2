package com.xpedx.nextgen.order;

import java.util.ArrayList;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCException;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXPendingApprovalOrders implements YIFCustomApi{
	
	private static YIFApi api = null;
	private String approverUserId = null;
	private String approverProxyUserId = null;
	private static YFCLogCategory log;
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
		boolean isApprovalReq = false;
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
		Document orderOutputTemplate = SCXmlUtil.createFromString("<OrderList TotalNumberOfRecords=''><Order><Extn ExtnTotalOrderValue='' /><OrderHoldTypes><OrderHoldType/></OrderHoldTypes></Order></OrderList>");
		env.setApiTemplate("getOrderList",orderOutputTemplate);
		Document orderOutputDoc = api.invoke(env, "getOrderList", inputOrderDoc);
		env.clearApiTemplate("getOrderList");
		Element orderElem = null;
		Double spendingLimit = null;
		if(orderOutputDoc!=null) {
			if(SCXmlUtil.getIntAttribute(orderOutputDoc.getDocumentElement(), "TotalNumberOfRecords") >0) {
				orderElem = SCXmlUtil.getChildElement(orderOutputDoc.getDocumentElement(),"Order");
				if(orderElem!=null) {
					spendingLimit = getSpendingLimit(env,orderElem);
				}				
			}
		}
		// checking if the order total is more than the spending limit
		if(spendingLimit!=null && orderElem!=null && spendingLimit!=-1) {
			Element extnElem = SCXmlUtil.getChildElement(orderElem,"Extn");
			String ExtnTotalOrderValue = extnElem.getAttribute("ExtnTotalOrderValue");
			Double totalOrderValue = new Double(0);
			if(!SCUtil.isVoid(ExtnTotalOrderValue))
				totalOrderValue= Double.parseDouble(ExtnTotalOrderValue);
			if(totalOrderValue >= spendingLimit)
				isApprovalReq = true;
		}
		// if approval is required
		if(isApprovalReq) {
			if(approverUserId!=null || approverProxyUserId!=null) {//if at least there is one approver put the order on Hold
				String approverOnHold = null;
//			if(approverUserId!=null && approverUserId.trim().length()>0)
//				approverOnHold = approverUserId; //approverProxyUserId;//
//			else
//				approverOnHold = approverProxyUserId;
				//for jira 3484
				if(approverUserId!=null && approverUserId.trim().length()>0){
		               approverOnHold = approverUserId;
		               if(approverProxyUserId != null && approverProxyUserId.trim().length()>0){
		                     approverOnHold = approverOnHold + ","+ approverProxyUserId;
		               }
				}else{
		              approverOnHold = approverProxyUserId;
				}
				//end of jira 3484
				if(approverOnHold!=null && approverOnHold.trim().length()>0) {
					applyHoldTypeOnOrder(env,orderElem,approverOnHold);					
				}
			}
		}
		return inXML;
		
	}
	
	public Double getSpendingLimit(YFSEnvironment env,Element orderElement) {
		Double spendingLimit = new Double(-1);
		String customerContactId = orderElement.getAttribute("CustomerContactID");
		String organizationCode  = orderElement.getAttribute("EnterpriseCode");
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
			Document Template = SCXmlUtil.createFromString("<CustomerContactList TotalNumberOfRecords=''><CustomerContact/></CustomerContactList>");
			env.setApiTemplate("getCustomerContactList",Template);
			//invoking the api
			Document outputDoc =null;
			try {
				outputDoc = api.invoke(env, "getCustomerContactList", inputCustContactDoc);
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			env.clearApiTemplate("getCustomerContactList");
			if(outputDoc!=null) {
				Element custContactList = outputDoc.getDocumentElement();
				if(SCXmlUtil.getIntAttribute(custContactList, "TotalNumberOfRecords")>0) {
					Element custContact = SCXmlUtil.getChildElement(custContactList, "CustomerContact");
					String spndgLimitString = custContact.getAttribute("SpendingLimit");
					approverUserId = custContact.getAttribute("ApproverUserId");
					approverProxyUserId = custContact.getAttribute("ApproverProxyUserId");
					if(!SCUtil.isVoid(spndgLimitString)) {
						Double spLimit = Double.parseDouble(spndgLimitString);
						if(spLimit > 0)
							spendingLimit = spLimit;
					}
					else {
						spendingLimit = Double.valueOf(-1);
					}
				}
			}
		}
		return spendingLimit;
	}
	
	public void applyHoldTypeOnOrder(YFSEnvironment env, Element orderElement, String approver){
		if(orderElement!=null && approver!=null) {
			// Applying the Hold Type ORDER_LIMIT_APPROVAL on the order with one of the above approvers
			Document changeOrderInputDoc = YFCDocument.createDocument("Order").getDocument();
			Element changeOrderInputElem = changeOrderInputDoc.getDocumentElement();
			changeOrderInputElem.setAttribute("OrderHeaderKey", orderElement.getAttribute("OrderHeaderKey"));
			Element holdTypes = changeOrderInputDoc.createElement("OrderHoldTypes");
			Element holdType = changeOrderInputDoc.createElement("OrderHoldType");
			holdType.setAttribute("HoldType", "ORDER_LIMIT_APPROVAL");
			holdType.setAttribute("Status", "1100");
			holdType.setAttribute("ResolverUserId", approver);
			holdTypes.appendChild(holdType);changeOrderInputElem.appendChild(holdTypes);
			//invoking the change Order to apply the hold type
			try {
				Document changeOrderOutput = api.invoke(env, "changeOrder", changeOrderInputDoc);
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	/*
	 *This is invoked when sending approval email to the customer contact. 
	 *Taking the CustomerContactID on the order and getting the Email ID 
	 *and this method appends the CustomerContactList Element to the order, so it will be easy when sending the email.
	 */
	
	public Document invoekApprovalOrderEmailForContact(YFSEnvironment env,Document inXML) throws Exception {

		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		String resolverUserIds[] = null;
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			log.error("Error getting the Api Instance, So returning the Document");
			// TODO Auto-generated catch block
			e.printStackTrace();
			appendDummyContactList(inXML);
			return inXML;
		}
		
		if(inXML!=null) {
			
			Element order = inXML.getDocumentElement();
			XPXUtils utilObj = new XPXUtils();
			inXML = utilObj.stampBrandLogo(env, inXML);
			String customerContactOnOrder = SCXmlUtil.getAttribute(order, "CustomerContactID");
			Element inputElem = SCXmlUtil.getChildElement(order, "Input");
			if (inputElem != null) {
				Element orderHoldTypeElem = SCXmlUtil.getChildElement(inputElem, "OrderHoldType");
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
			String organizationCode = SCXmlUtil.getAttribute(order, "EnterpriseCode");
			String sellerOrgCode = inXML.getDocumentElement().getAttribute("SellerOrganizationCode");

			
			Element orderExtn = SCXmlUtil.getChildElement(order, "Extn");
			String formattedOrderNo = getFormattedOrderNumber(orderExtn);
			inXML.getDocumentElement().setAttribute("FormattedOrderNo",formattedOrderNo);
			
			//stamp the approval related URLs.
			String baseURL = YFSSystem.getProperty("baseURL");
			String toApproveOrderURL = baseURL + "/order/approvalList.action?sfId="+ sellerOrgCode +"&scFlag=y"; //http://stg.xpedx.com/swc/order/approvalList.action?sfId=xpedx&scFlag=y 
			String approvedOrderURL =  baseURL + "/order/orderList.action?sfId="+ sellerOrgCode + "&scFlag=Y" ;  
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
					
					SCXmlUtil.importElement(order, outputDoc.getDocumentElement());//(inXML, outputDoc.getDocumentElement());
					addEmailIDToElement(order);
					//order.appendChild(outputDoc.getDocumentElement());// Appends the CustomerContacList Element to the Order and returns the order Document
				}
				else {
					appendDummyContactList(inXML);
				}
			}
			else {
				appendDummyContactList(inXML);
			}
		}
		return inXML;		
	}
	
	private void addEmailIDToElement(Element orderElement)
	{
		String orderCustomerContactId=orderElement.getAttribute("CustomerContactID");
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
	}
	
	private void appendDummyContactList(Document inXML) {
		Element order = inXML.getDocumentElement();
		// Creating the Dummy Element of CusotmerCotnact so that xsl wouldn't throw any error
		Document dummy = SCXmlUtil.createFromString("<CustomerContactList TotalNumberOfRecords=''><CustomerContact EmailID='' FirstName='' LastName=''" +
				"CustomerContactID='' /></CustomerContactList>");
		SCXmlUtil.importElement(order, dummy.getDocumentElement());
	}

	public static String getFormattedOrderNumber(Element orderElement)
	{
		StringBuffer sb = new StringBuffer();
		if(orderElement!=null)
		{
			String legacyOrderNum = orderElement.getAttribute("ExtnLegacyOrderNo");
			String orderBranch = orderElement.getAttribute("ExtnOrderDivision");
			String generationNum = orderElement.getAttribute("ExtnGenerationNo");
			
			
			
			if(orderBranch!=null && orderBranch.length()>0)
			{
				sb.append(orderBranch);
			}
			if(legacyOrderNum!=null && legacyOrderNum.length()>0)
			{				
				sb.append(legacyOrderNum);				
			}
			if(generationNum!=null && generationNum.length()>0)
			{
				if(generationNum.trim().length()==1)
				{
					generationNum="0"+generationNum;
				}				
				sb.append(generationNum);
				
			}
		}
		//as per data the order number which come from legacy will not contain any -,_ or any character .
		return sb.toString().replaceAll("_M","");
	}

	
}

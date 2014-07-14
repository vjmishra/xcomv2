package com.xpedx.nextgen.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCDateTimeUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCException;
import com.yantra.yfs.japi.YFSEnvironment;
/*
 * This Purge Agent will delete all the draft orders of the Procurement users which are unattended for over 7 days.
 * We check of the Last modified date on the order. Attribute is Modifyts on the order.
 */

public class XPXPurgePunchOutOrders extends YCPBaseAgent {
	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");

	@Override
	public void executeJob(YFSEnvironment env, Document orderToDeleteDoc)
			throws Exception {
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		try {
			if(log.isDebugEnabled()){
				log.debug("The input document to deleteOrder Api is: "+SCXmlUtil.getString(orderToDeleteDoc));
			}
			api.invoke(env, "deleteOrder", orderToDeleteDoc);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Error Purging the Order with Order Header key" + orderToDeleteDoc.getDocumentElement().getAttribute(XPXLiterals.A_ORDER_HEADER_KEY));
		}
		// TODO Auto-generated method stub

	}
	
	@Override
	public List getJobs(YFSEnvironment env, Document criteria,
			Document lastMessageCreated) throws Exception {
		/*
		 * First we are getting all the users who are procurement users using the API getUserList. 
		 * The input includes the UserGroupKey of the Procurement User Group which is PROCUREMENT-USER.
		 * In the output we get all the user who has procurement user role.
		 * 
		 * What I observed is that we can give Procurement user, other roles also. So if some buyer user is given procurement user role,
		 * that user will also be there in the output. 
		 * So to make sure that we don't delete orders of such users we check if the user is only alloted Procurement User Role.
		 */
		List listOfJobs = new ArrayList();
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		Set<String> procUserList = new HashSet<String>();
		Set<String> userEnterpriseCodeList = new HashSet<String>();
		
		Document userInputDoc = SCXmlUtil.createFromString("<User><UserGroupLists><UserGroupList UsergroupKey='PROCUREMENT-USER'/></UserGroupLists></User>");
		
		Document userOutputDoc = SCXmlUtil.createFromString("<UserList><User EnterpriseCode='' Loginid='' DisplayUserID=''><UserGroupLists><UserGroupList UserGroupListKey='' UserKey='' UsergroupKey=''/></UserGroupLists></User></UserList>");
		env.setApiTemplate("getUserList",userOutputDoc);
		if(log.isDebugEnabled())
		{
			log.debug("The input to getUserList of  XPXPurgePunchOutOrders is : "+ SCXmlUtil.getString(userInputDoc));
		}
		Document docProcUsers = api.invoke(env, "getUserList", userInputDoc);
		env.clearApiTemplate("getUserList");
		
		/*
		 * Getting all the procurement user id's for which order searches are to be made.
		 * 
		 */
		Element procUsers = docProcUsers.getDocumentElement();
		NodeList userElemsList = procUsers.getElementsByTagName("User");
		if(userElemsList.getLength()>0) {
			for(int i=0; i<userElemsList.getLength();i++) {
				Element user = (Element) userElemsList.item(i);
				if(user!=null) {
					NodeList userGroupList = user.getElementsByTagName("UserGroupList");
					if(userGroupList.getLength()==1) {
						String userId = user.getAttribute("DisplayUserID");
						String usersEnterprise = user.getAttribute("EnterpriseCode");
						procUserList.add(userId);
						userEnterpriseCodeList.add(usersEnterprise);
					}
				}
			}
		}
		
		/*
		 * Creating the document to get All the draft orders of all the userid's present in the procUserList
		 * We are getting all the draft orders using complex query
		 */
		Document orderInputDoc = SCXmlUtil.createFromString("<Order DraftOrderFlag='Y'><OrderBy><Attribute Name='OrderHeaderKey' Desc='N' /></OrderBy></Order>");
		YFCDocument orderInputYfcDoc =  YFCDocument.getDocumentFor(orderInputDoc);
		YFCElement documentElement = orderInputYfcDoc.getDocumentElement();
		
		YFCElement complexQueryElement = documentElement.createChild("ComplexQuery");
		complexQueryElement.setAttribute("Operator", "AND");
		YFCElement complexQueryOrElement = documentElement.createChild("Or");
		Iterator<String> userIterator = procUserList.iterator();
		while(userIterator.hasNext()) {
			String userId = userIterator.next();
			YFCElement expElement = documentElement.createChild("Exp");
			expElement.setAttribute("Name", "Createuserid");
			expElement.setAttribute("Value", userId);
			complexQueryOrElement.appendChild((YFCNode)expElement);
		}
		complexQueryElement.appendChild((YFCNode)complexQueryOrElement);
		documentElement.appendChild((YFCNode)complexQueryElement);
		orderInputDoc = orderInputYfcDoc.getDocument();
		String totalNumberOfRecords = getCriteriaParamValue(criteria,"NumRecordsToBuffer");
		orderInputDoc.getDocumentElement().setAttribute("MaximumRecords", totalNumberOfRecords);
		/*
		 * To get only the records after the previous Order which is deleted already, as this is a batch process
		 */
		
		if (lastMessageCreated != null) 
		{
			if(log.isDebugEnabled()){
				log.debug("In the loop for last message created");
			}
			String lastDeletedOrderHeaderKey = "";
			Element lastOrderElement = null;
			NodeList orderNodeList = lastMessageCreated.getElementsByTagName("Order");
			int orderLength = orderNodeList.getLength();
			if(orderLength != 0)
			{
				lastOrderElement = (Element)orderNodeList.item(orderLength-1);
			
				lastDeletedOrderHeaderKey = lastOrderElement.getAttribute("OrderHeaderKey");
			
				orderInputDoc.getDocumentElement().setAttribute("OrderHeaderKey", lastDeletedOrderHeaderKey);
				orderInputDoc.getDocumentElement().setAttribute("OrderHeaderKeyQryType", "GT");
			}
		}
		else {
			orderInputDoc.getDocumentElement().setAttribute("OrderHeaderKey", "");
			orderInputDoc.getDocumentElement().setAttribute("OrderHeaderKeyQryType", "GT");
		}
		if(log.isDebugEnabled()){
			log.debug("The input to getOrderList in XPXPurgePunchOutOrders  is : "+ SCXmlUtil.getString(orderInputDoc));
		}
		Document orderOutputDoc = SCXmlUtil.createFromString("<OrderList><Order/></OrderList>");
		env.setApiTemplate("getOrderList",orderOutputDoc);
		Document docProcOrdersToPurge = SCXmlUtil.createFromString("<OrderList/>");
		YIFApi api2 = YIFClientFactory.getInstance().getLocalApi();
		if(procUserList.size()>=1)
			docProcOrdersToPurge = api2.invoke(env, "getOrderList", orderInputDoc);
		env.clearApiTemplate("getOrderList");
		
		/*	calling the updateListOfJobs method to add the elements to the list of jobs to be executed
		 * as the batch process.
		 */
		
		updateListOfJobs(criteria,listOfJobs,docProcOrdersToPurge);
		
		// TODO Auto-generated method stub
		return listOfJobs;
	}
	
	private void updateListOfJobs(Document criteria, List listOfJobs,Document docOrders) {
		
		Element orderElement = null;
		List orders = SCXmlUtil.getChildrenList(docOrders.getDocumentElement());
		Date presentDate = new Date();
		Date dateToCompare= SCDateTimeUtil.addDays(presentDate, -7);
		for (int counter = 0; counter < orders.size(); counter++)
		{
			orderElement = (Element) orders.get(counter);
			String lastModifiedDateStr = orderElement.getAttribute("Modifyts");
			Date orderLastModDate = SCDateTimeUtil.getDate(lastModifiedDateStr, SCDateTimeUtil.ISO_DATE_FORMAT, TimeZone.getDefault());
			//Comparing the dates and checking whether the order should be deleted or not.
			
			int compareResult = dateToCompare.compareTo(orderLastModDate);
			
			if(compareResult == 1 || compareResult == 0) {
				
				Document docOrderNew = SCXmlUtil.createDocument("Order");
				docOrderNew.getDocumentElement().setAttribute("Action", "DELETE");
				docOrderNew.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, orderElement.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY));
				docOrderNew.getDocumentElement().setAttribute(XPXLiterals.A_ENTERPRISE_CODE, orderElement.getAttribute(XPXLiterals.A_ENTERPRISE_CODE));
				
                 //Add the Order to listOfJobs to be processed
				listOfJobs.add(docOrderNew);
			}
			
		}
		return ;
	}
	
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

}

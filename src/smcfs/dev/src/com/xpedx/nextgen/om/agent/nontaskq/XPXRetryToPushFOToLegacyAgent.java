package com.xpedx.nextgen.om.agent.nontaskq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.log.YFCLogUtil;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * @author sdodda
 * 
 * Queries the List of Fulfillment Orders in "Submitted" state, and retries processing them to Legacy system.
 *
 */
public class XPXRetryToPushFOToLegacyAgent extends YCPBaseAgent {

	private static YFCLogCategory log = YFCLogCategory
			.instance(XPXRetryToPushFOToLegacyAgent.class);
	
	//private Set<String> setOrderHeaderKeys = new HashSet<String>();

	@Override
	public List getJobs(YFSEnvironment env, Document criteria,
			Document lastMessageCreated) throws Exception {
		
		Document docInQry = null;
		Element eleInQry = null;
		Document docTemplate = null;
		Document docOrderList = null;
		
		//Log Begin Timer
		log.beginTimer("getJobs");
		
		// get YIFApi instance.
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();

		// Create getOrderList input XML with Criteria as ExtnHeaderStatusCode==ISNULL and Status==Created(1100)
		/*docInQry = SCXmlUtil
				.createFromString(new StringBuffer()
						.append("<Order Status='")
						.append(XPXLiterals.STATUS_CREATED_ID)
						.append("' EnterpriseCode='"+XPXLiterals.A_ORGANIZATIONCODE_VALUE+"' ")
						.append("><OrderBy><Attribute Name='OrderHeaderKey' Desc='N' /></OrderBy>" )
						.append("<Extn ExtnHeaderStatusCodeQryType='ISNULL'/></Order>").toString());*/
		
		/***********************Start fix as per CR # 2589 BY Prasanth Kumar M.*************************************/
		
		java.util.Date todaysDate = new java.util.Date();

		SimpleDateFormat isosdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String todaysDateStr = isosdf.format(todaysDate);
		int minutesToAdd = -30;  
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(isosdf.parse(todaysDateStr));
		cal.add(Calendar.MINUTE, minutesToAdd); 
		todaysDateStr = isosdf.format(cal.getTime());
						
		docInQry = SCXmlUtil
		.createFromString(new StringBuffer()
				.append("<Order Createts='")
				.append(todaysDateStr)
				.append("' CreatetsQryType='LT' OrderType='Customer' OrderTypeQryType='NE' DraftOrderFlag='Y' DraftOrderFlagQryType='NE' ")
				.append("><OrderBy><Attribute Name='OrderHeaderKey' Desc='N' /></OrderBy>" )
				.append("<Extn ExtnIsReprocessibleFlag='Y'/></Order>").toString());
		
		/***********************************************************************************************************/	
		
		eleInQry = docInQry.getDocumentElement();

		int iNumRecordsToBuffer = SCXmlUtil.getIntAttribute(criteria.getDocumentElement(), "NumRecordsToBuffer", 30);
		eleInQry.setAttribute("MaximumRecords", Integer.toString(iNumRecordsToBuffer));
		
		// Handle's repetitive querying of orders to retry Legacy order creation.
		if (lastMessageCreated != null) {
			Element eleCQ = SCXmlUtil.createChild(eleInQry, "ComplexQuery");
			Element eleOr = SCXmlUtil.createChild(eleCQ, "Or");
			
			//Query all failed Legacy Order posts
			/*for (String sOrderHeaderKey : setOrderHeaderKeys) {
				Element eleExp = SCXmlUtil.createChild(eleOr, "Exp");
				eleExp.setAttribute("Name", "OrderHeaderKey");
				eleExp.setAttribute("Value", sOrderHeaderKey);
			}*/
			
			//Query all Orders after the Last Message created.
			Element eleExpGT = SCXmlUtil.createChild(eleOr, "Exp");
			eleExpGT.setAttribute("Name", "OrderHeaderKey");
			eleExpGT.setAttribute("Value", SCXmlUtil.getAttribute(lastMessageCreated.getDocumentElement(), "OrderHeaderKey"));
			eleExpGT.setAttribute("QryType", "GT");
			
		}
		// Initially clear all failed OrderHeaderKeys
		//setOrderHeaderKeys.removeAll(setOrderHeaderKeys);
		
		if(log.isDebugEnabled()){
			log.debug((new StringBuilder()).append("User:" + env.getUserId()).append(" Input document[docInQry] is ").append(YFCLogUtil.toString(docInQry)).toString());
		}
		
		docTemplate = SCXmlUtil
				.createFromString(new StringBuffer()
						.append("<OrderList><Order OrderHeaderKey='' OrderNo='' DocumentType='' EnterpriseCode=''><Extn ExtnWebConfNum='' />")
						.append("<OrderHoldTypes><OrderHoldType HoldType='' Status=''/></OrderHoldTypes></Order></OrderList>")
						.toString());
		if(log.isDebugEnabled()){
			log.debug((new StringBuilder()).append(" Template document[docTemplate] is ").append( YFCLogUtil.toString(docTemplate)).toString());
		}
		if(docInQry != null){
			log.info("The input to getOrderList is: "+SCXmlUtil.getString(docInQry));
		}
		env.setApiTemplate("getOrderList", docTemplate);
		docOrderList = api.invoke(env, "getOrderList", docInQry);
		env.clearApiTemplate("getOrderList");
		
		

		if(log.isDebugEnabled()){
			log.debug((new StringBuilder())
				.append(" Output document[docOrderList] is ")
				.append(YFCLogUtil.toString(docOrderList)).toString());
		}
		
		// List<Document>: <Order OrderHeaderKey='' OrderNo='' DocumentType='' EnterpriseCode='' FromRetryAgent='Y' />
		List listOfJobs = getProcessOrderList(docOrderList);

		if(log.isDebugEnabled()){
			log.debug("The length of listOfJobs is : " + listOfJobs.size());
		}
		log.endTimer("getJobs");
		return listOfJobs;
	}

	/**
	 * Takes the List of Orders which are in 'Created' Status and their
	 * ExtnHeaderStatusCode is NULL. <br>
	 * Creates new Process Order List object from the above list, removing all
	 * the orders which are in NEEDS_ATTENTION hold
	 * 
	 * @param docOrderList
	 * @return List
	 */
	private List getProcessOrderList(Document docOrderList) {
		List listOrders = SCXmlUtil.getChildrenList(docOrderList
				.getDocumentElement());
		String  extnWebConfNum  = "";
		List listOfJobs = new ArrayList();
		for (Object objOrder : listOrders) {
			Element eleOrder = (Element) objOrder;
			if(log.isDebugEnabled()){
				log.debug((new StringBuilder())
					.append( " Order document[eleOrder] is ")
					.append( YFCLogUtil.toString(eleOrder)).toString());
			}
			if(eleOrder.getElementsByTagName("Extn")!=null)
			{
				Element extnElement = (Element)eleOrder.getElementsByTagName("Extn").item(0);
			    if(extnElement != null)
			    {
					extnWebConfNum =  extnElement.getAttribute("ExtnWebConfNum");    
					// Get List of Holds with NEEDS_ATTENTION hold in 1100(Created)
					// Status..
					
					if(!YFCObject.isVoid(extnWebConfNum))
					{
						NodeList nlOrderHoldType = SCXmlUtil.getXpathNodes(eleOrder,"./OrderHoldTypes/OrderHoldType[@HoldType='NEEDS_ATTENTION' and @Status='1100']");						
			
						if(log.isDebugEnabled()){
							log.debug((new StringBuilder()).append( " No. of ./OrderHoldTypes/OrderHoldType are ").append( nlOrderHoldType.getLength()).toString());
						}
						
						//If count of active NEEDS_ATTENTION hold is zero, then add to listOfJobs to process.
						if (nlOrderHoldType.getLength() == 0) {
							// Create a Order XML Document
							Document docOrderNew = SCXmlUtil.createDocument("Order");
							docOrderNew.getDocumentElement().setAttribute("FromRetryAgent", "Y");
							
							SCXmlUtil.mergeAttributes(eleOrder, docOrderNew .getDocumentElement(), false);
			
							// Add the Order to listOfJobs to be processed
							
							listOfJobs.add(docOrderNew);
						}
					}
			    
			    }
			}
		}
		return listOfJobs;
	}

	/** 
	 * Input document looks like this: <br>
	 * 
	 * <pre>
	 * &lt;Order OrderHeaderKey='' OrderNo='' DocumentType='' EnterpriseCode='' FromRetryAgent='Y' /&gt;
	 * </pre>
	 * Invokes PostToLegacyOrderCreateService 
	 * 
	 * @see com.yantra.ycp.japi.util.YCPBaseAgent#executeJob(com.yantra.yfs.japi.YFSEnvironment, org.w3c.dom.Document)
	 */
	@Override
	public void executeJob(YFSEnvironment env, Document docIn)
			throws Exception {
		
		 log.beginTimer("executeJob");

		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		
		if(log.isDebugEnabled()){
			log.debug((new StringBuilder("executeJobs():"))
				.append(" Order document[docIn] is ")
				.append(YFCLogUtil.toString(docIn)).toString());
		}
		
		try {
			api.executeFlow(env, XPXLiterals.SERVICE_POST_LEGACY_ORDER_CREATE, docIn);
		} catch (Exception e) {

			log.error(e);
			
			//setOrderHeaderKeys.add(SCXmlUtil.getAttribute(docIn.getDocumentElement(), "OrderHeaderKey"));
		}
		
		log.endTimer("executeJob");
	}

}
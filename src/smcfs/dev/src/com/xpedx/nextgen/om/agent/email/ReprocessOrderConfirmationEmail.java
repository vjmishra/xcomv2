package com.xpedx.nextgen.om.agent.email;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class ReprocessOrderConfirmationEmail extends YCPBaseAgent {
	private static YFCLogCategory log = YFCLogCategory.instance(XPXSendEmailAgent.class);	
	
	@Override
	public List getJobs(YFSEnvironment env, Document criteria, Document lastMessageCreated) throws Exception {
		
		Document orderListInDoc=null;
		Document orderListTemplate=null;
		Document orderListOutDoc=null;
		List orderConfirmationEmailList=null;
		log.beginTimer("getJobs() in ReprocessOrderConfirmationEmail class");
		try{
			YIFApi api=YIFClientFactory.getInstance().getLocalApi();	
			
			String strWaitTimeInMins=YFSSystem.getProperty("orderUpdateWaitTime");
			int waitTimeInMins = -15;
			if(strWaitTimeInMins!=null && strWaitTimeInMins.trim().length()>0) {
				waitTimeInMins = -(Integer.parseInt(strWaitTimeInMins));
			}			
			DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.MINUTE, waitTimeInMins);
			String sysDateTime=dateFormat.format(cal.getTime());	
			
			orderListInDoc=SCXmlUtil.createFromString(new StringBuffer().append("<Order Modifyts='")
																	.append(sysDateTime)
																	.append("' ModifytsQryType='LT' OrderType='Customer' OrderTypeQryType='EQ' DraftOrderFlag='Y' DraftOrderFlagQryType='NE'> ")																	
																	.append("<OrderBy><Attribute Name='OrderHeaderKey' Desc='N' /></OrderBy>")
																	.append("<Extn ExtnOrderConfirmationEmailSentFlag='N'/></Order>").toString());
			if (log.isDebugEnabled()) {
				log.debug((new StringBuilder()).append("Input_XML of ").append(XPXLiterals.GET_ORDER_LIST_API).append(" API: ").append(SCXmlUtil.getString(orderListInDoc)));
			}
			System.out.println((new StringBuilder()).append("Input_XML of ").append(XPXLiterals.GET_ORDER_LIST_API).append(" API: ").append(SCXmlUtil.getString(orderListInDoc)));
			orderListTemplate = SCXmlUtil.createFromString(new StringBuffer()
											              .append("<OrderList><Order OrderHeaderKey='' OrderNo='' DocumentType='' EnterpriseCode='' BillToID='' CustomerContactID='' >")
											              .append("<Extn ExtnWebConfNum='' ExtnLastOrderOperation='' ExtnOrderConfirmationEmailSentFlag='' />")
											              .append("<OrderHoldTypes><OrderHoldType HoldType='' OrderHeaderKey='' ReasonText='' ResolverUserId='' Status='' TransactionId=''/></OrderHoldTypes>")
											              .append("</Order></OrderList>").toString());
			if(log.isDebugEnabled()){
				log.debug((new StringBuilder()).append(XPXLiterals.GET_ORDER_LIST_API).append(" API Template_XML: ").append(SCXmlUtil.getString(orderListTemplate)));
			}
			System.out.println((new StringBuilder()).append(XPXLiterals.GET_ORDER_LIST_API).append(" API Template_XML: ").append(SCXmlUtil.getString(orderListTemplate)));
			env.setApiTemplate(XPXLiterals.GET_ORDER_LIST_API, orderListTemplate);
			orderListOutDoc=api.invoke(env, XPXLiterals.GET_ORDER_LIST_API, orderListInDoc);
			env.clearApiTemplate(XPXLiterals.GET_ORDER_LIST_API);
			System.out.println(new StringBuilder("Output_XML of ").append(XPXLiterals.GET_ORDER_LIST_API).append(" API: ").append(SCXmlUtil.getString(orderListOutDoc)));
			if (log.isDebugEnabled()) {
				log.debug(new StringBuilder("Output_XML of ").append(XPXLiterals.GET_ORDER_LIST_API).append(" API: ").append(SCXmlUtil.getString(orderListOutDoc)));
			}			
		
		}catch(Exception e){
			StringBuffer centException=new StringBuffer();
			if(e instanceof com.yantra.yfs.japi.YFSException) {
            	YFSException yfe = (YFSException)e;            	
            	centException.append("YFSException caught inside getJobs() of ReprocessOrderConfirmationEmail class. "+yfe.getErrorCode()).append(":").append(yfe.getErrorDescription()).append(":").append(yfe.getErrorUniqueId());            	
            } else {
            	centException.append("Exception caught inside getJobs() of ReprocessOrderConfirmationEmail class. "+e.getMessage());            	 	
            }
			log.error(centException.toString());			
		}		
		orderConfirmationEmailList=getListOfOrderDocs(orderListOutDoc);
		if(log.isDebugEnabled()){
			log.debug("OrderConfirmationEmailList size: [" +orderConfirmationEmailList.size()+"]");
		}
		System.out.println("OrderConfirmationEmailList size: [" +orderConfirmationEmailList.size()+"]");
		log.endTimer("getJobs");
		return orderConfirmationEmailList;
	}
	
	@Override
	public void executeJob(YFSEnvironment env, Document orderConfirmationEmailDocument) throws Exception {
		
		log.beginTimer("executeJob");	
		
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();		
		if(log.isDebugEnabled()){
			log.debug((new StringBuilder("executeJobs():"))
				.append(" Order document[orderConfirmationEmailDocument]: ")
				.append(SCXmlUtil.getString(orderConfirmationEmailDocument)));
		}
		String lastOrderOperation=null;
		String orderEmailConfirmationSentFlag=null;
		
		Element orderConfirmationEmailElem=orderConfirmationEmailDocument.getDocumentElement();
		Element extnOrderElem=SCXmlUtil.getChildElement(orderConfirmationEmailElem, "Extn");		
		if(extnOrderElem!=null) {
			lastOrderOperation=extnOrderElem.getAttribute(XPXLiterals.LAST_ORDER_OPERATION);
			orderEmailConfirmationSentFlag=extnOrderElem.getAttribute(XPXLiterals.ORDER_CONFIRMATION_EMAIL_SENT_FLAG);
		}
		
		if("N".equals(orderEmailConfirmationSentFlag) && lastOrderOperation != null && lastOrderOperation.trim().length()>0) {
			lastOrderOperation=lastOrderOperation.trim();			
			if ("OrderPlacement".equals(lastOrderOperation) || "OrderEdit".equals(lastOrderOperation)) {
				String cOrderHeaderKey=orderConfirmationEmailElem.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
				if(log.isDebugEnabled()) {
					log.debug("Inside ReprocessOrderConfirmationEmail class.");
					log.debug("InputXML-XPXPutOrderChangesInOrderConfirmationEmailQueue service to send Order Confirmation Email: "+SCXmlUtil.getString(orderConfirmationEmailElem));
				}
				String cOrder=SCXmlUtil.getString(orderConfirmationEmailElem);
				Document cOrderDoc=SCXmlUtil.createFromString(cOrder);
				try {
					api.executeFlow(env, "XPXPutOrderChangesInOrderConfirmationEmailQueue", cOrderDoc);
					orderEmailConfirmationSentFlag="Y";
					XPXUtils utilsObj=new XPXUtils();
					utilsObj.callChangeOrder(env, cOrderHeaderKey, orderEmailConfirmationSentFlag, this.getClass().getSimpleName());
				}catch(Exception ex) {
					log.error("Exception occured on posting order confirmation email XML to  XPXPutOrderChangesInOrderConfirmationEmailQueue service "+ex.getMessage());
				}			
			
			} else if("OrderApproved".equals(lastOrderOperation)) {
				String cOrderHeaderKey=orderConfirmationEmailElem.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
				//Forming an input document to send Order Approved Email [Input for YCD_Order_Approval_Email_8.5 service]
				XPXUtils utilsObj=new XPXUtils();				
				Document orderApprovedEmailInputDoc=createOrderApprovedEmailInputDoc(orderConfirmationEmailElem);
				if(log.isDebugEnabled()) {
					log.debug("Inside ReprocessOrderConfirmationEmail class.");
					log.debug("InputXML-YCD_Order_Approval_Email_8.5 service to send Order Approved Email: "+SCXmlUtil.getString(orderApprovedEmailInputDoc));
				}
				try{
					api.executeFlow(env, "YCD_Order_Approval_Email_8.5", orderApprovedEmailInputDoc);				
					orderEmailConfirmationSentFlag="Y";
					utilsObj.callChangeOrder(env, cOrderHeaderKey, orderEmailConfirmationSentFlag, this.getClass().getSimpleName());
				}catch(Exception ex) {
					log.error("Exception occured on posting order approved email XML to YCD_Order_Approval_Email_8.5 service: "+ex.getMessage());
				}
			}
		}		
		
		log.endTimer("executeJob");
	
	}
	
	public Document createOrderApprovedEmailInputDoc(Element cOrderElem) {
		//	<OrderHoldType FromStatus='' HoldType='' OrderHeaderKey='' ReasonText='' ResolverUserId='' Status='' TransactionId=''>
		//		<Order BillToID='' CustomerContactID='' DocumentType='' EnterpriseCode='' OrderHeaderKey='' OrderNo=''/>
		//	</OrderHoldType>		
		Element cOrderHoldTypesElem=SCXmlUtil.getChildElement(cOrderElem, XPXLiterals.E_ORDER_HOLD_TYPES);
		Element cOrderHoldTypeElem=SCXmlUtil.getChildElement(cOrderHoldTypesElem, XPXLiterals.E_ORDER_HOLD_TYPE);
		
		Document orderHoldTypeDoc=SCXmlUtil.createDocument(XPXLiterals.E_ORDER_HOLD_TYPE);
		Element orderHoldTypeEle=orderHoldTypeDoc.getDocumentElement();
		orderHoldTypeEle.setAttribute(XPXLiterals.A_HOLD_TYPE, cOrderHoldTypeElem.getAttribute(XPXLiterals.A_HOLD_TYPE));
		orderHoldTypeEle.setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, cOrderHoldTypeElem.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY));
		orderHoldTypeEle.setAttribute(XPXLiterals.HOLD_RELEASE_DESC, cOrderHoldTypeElem.getAttribute(XPXLiterals.HOLD_RELEASE_DESC));
		orderHoldTypeEle.setAttribute(XPXLiterals.RESOLVER_USER_ID, cOrderHoldTypeElem.getAttribute(XPXLiterals.RESOLVER_USER_ID));
		orderHoldTypeEle.setAttribute(XPXLiterals.A_STATUS, cOrderHoldTypeElem.getAttribute(XPXLiterals.A_STATUS));
		orderHoldTypeEle.setAttribute(XPXLiterals.A_TRANSACTION_ID, cOrderHoldTypeElem.getAttribute(XPXLiterals.A_TRANSACTION_ID));	
		
		Element orderEle=SCXmlUtil.createChild(orderHoldTypeEle, XPXLiterals.E_ORDER);
		orderEle.setAttribute(XPXLiterals.A_BILL_TO_ID, cOrderElem.getAttribute(XPXLiterals.A_BILL_TO_ID));
		orderEle.setAttribute(XPXLiterals.CUSTOMER_CONTACT_ID, cOrderElem.getAttribute(XPXLiterals.CUSTOMER_CONTACT_ID));
		orderEle.setAttribute(XPXLiterals.A_DOCUMENT_TYPE, cOrderElem.getAttribute(XPXLiterals.A_DOCUMENT_TYPE));
		orderEle.setAttribute(XPXLiterals.A_ENTERPRISE_CODE, cOrderElem.getAttribute(XPXLiterals.A_ENTERPRISE_CODE));
		orderEle.setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, cOrderElem.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY));
		orderEle.setAttribute(XPXLiterals.A_ORDER_NO, cOrderElem.getAttribute(XPXLiterals.A_ORDER_NO));
		
		return orderHoldTypeDoc;
	}
	
	private List getListOfOrderDocs(Document orderListOutDoc) throws Exception {
		NodeList nodeOrderList=orderListOutDoc.getDocumentElement().getElementsByTagName("Order");
		List listOfJobs=new ArrayList();
		int nodeOrderListLength = nodeOrderList.getLength();
		for (int i=0; i<nodeOrderListLength; i++) {
			Element orderDetailElem=(Element)nodeOrderList.item(i);
			Document orderDetailDoc=SCXmlUtil.createFromString(SCXmlUtil.getString(orderDetailElem));
			listOfJobs.add(orderDetailDoc);

		}
		return listOfJobs;
	}

}

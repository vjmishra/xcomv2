package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXOrderApprovalService implements YIFCustomApi
{
	private static YFCLogCategory log;
	private static YIFApi api = null;
	Properties props;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			e.printStackTrace();
		}
	}
	
	public Document invokeOrderApproval(YFSEnvironment env, Document inputXML) throws Exception {
		
		if (log.isDebugEnabled()) {
			log.debug("XPXOrderApprovalService-InXML: "+SCXmlUtil.getString(inputXML));
		}		
		
		Element rootElem = inputXML.getDocumentElement();
		
		try {			
			// Order Approval Flow After Hold Release.
			String orderHeaderKey = rootElem.getAttribute("OrderHeaderKey");
			if(!YFCObject.isNull(orderHeaderKey) && !YFCObject.isVoid(orderHeaderKey)) {
			   Document rulesEngineInputDoc = YFCDocument.createDocument("Order").getDocument();
			   rulesEngineInputDoc.getDocumentElement().setAttribute("OrderHeaderKey", orderHeaderKey);			
			   api.executeFlow(env, "XPXInvokeRulesEngineAndLegacyOrderCreationService", rulesEngineInputDoc);
			}			
			//Forming an input document to send an Order Approved Email [Input for YCD_Order_Approval_Email_8.5 service]
			Document orderListOutDoc=formInputForOrderApprovedEmail(env, inputXML.getDocumentElement());			
			NodeList orderHoldTypesNode = orderListOutDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_ORDER_HOLD_TYPES);	        
			Document inputForOrderApprovedEmail = null;	
			
			if(orderHoldTypesNode != null)
	        {	
	        	Element orderHoldTypesElement=(Element)orderHoldTypesNode.item(0);
	        	if(orderHoldTypesElement != null) {
	        		NodeList orderHoldTypeNode = orderHoldTypesElement.getElementsByTagName(XPXLiterals.E_ORDER_HOLD_TYPE);
	        		if(orderHoldTypeNode != null)
	    	        {
	        			Element orderHoldTypeElement=(Element)orderHoldTypeNode.item(0);
	        			if(orderHoldTypeElement!=null)
	        			{
	        				inputForOrderApprovedEmail=SCXmlUtil.createFromString(SCXmlUtil.getString(orderHoldTypeElement));
	        			}
	    	        }
	    	        
	        	}
	        }	        
	        
			if (log.isDebugEnabled()) {
				log.debug("XPXOrderApprovalService-OutXML: "+SCXmlUtil.getString(inputForOrderApprovedEmail));
			}
			return inputForOrderApprovedEmail;
			
		} catch (YFSException ye) {
			// TODO Auto-generated catch block
			ye.printStackTrace();
		} catch (RemoteException re) {
			// TODO Auto-generated catch block
			re.printStackTrace();
		}
		return inputXML;
		
	}
	
	public void setProperties(Properties props) throws Exception {
		
		this.props = props;
		
	}
	
	private Document formInputForOrderApprovedEmail(YFSEnvironment env,Element inputElement) throws YFSException, RemoteException {

		log.debug("XPXOrderApprovalService_formInputForOrderApprovedEmail()_InXML :" + SCXmlUtil.getString(inputElement));
		
		Document inputOrderDoc = SCXmlUtil.createDocument("Order");
		Element inputOrderElement = inputOrderDoc.getDocumentElement();
		
		// To Get orderHeaderKey
		String orderHeaderKey = null;
		if (inputElement != null) {
			orderHeaderKey = inputElement.getAttribute("OrderHeaderKey");			
		}		
		
		if(log.isDebugEnabled()){
			log.debug("XPXOrderApprovalService_formInputForOrderApprovedEmail_OrderHeaderKey :"+orderHeaderKey);
		}
		
		if (YFCObject.isNull(orderHeaderKey) || YFCObject.isVoid(orderHeaderKey)) {
			if(log.isDebugEnabled()){
				log.debug("XPXOrderApprovalService_formInputForOrderApprovalEmail_OrderHeaderKey :"+orderHeaderKey + " is Null or Empty");
				return inputElement.getOwnerDocument();
			}
		}
		inputOrderElement.setAttribute("OrderHeaderKey", orderHeaderKey);
		Document getOrderListTemplate = SCXmlUtil.createFromString("<OrderList><Order OrderHeaderKey=''><OrderHoldTypes><OrderHoldType FromStatus='' HoldType='' OrderHeaderKey='' ReasonText='' ResolverUserId='' Status='' TransactionId=''><Order BillToID='' CustomerContactID='' DocumentType='' EnterpriseCode='' OrderHeaderKey='' OrderNo=''/></OrderHoldType></OrderHoldTypes></Order></OrderList>");
		env.setApiTemplate("getOrderList",getOrderListTemplate);
		log.debug("XPXOrderApprovalService_formInputForOrderApprovedEmail_getOrderList_InXML :"+ SCXmlUtil.getString(inputOrderDoc));
		Document orderListDoc = api.invoke(env, "getOrderList",inputOrderDoc);
		log.debug("XPXOrderApprovalService_formInputForOrderApprovedEmail_getOrderList_OutXML :"+ SCXmlUtil.getString(orderListDoc));
		env.clearApiTemplate("getOrderList");
		
		return orderListDoc;
	}

}

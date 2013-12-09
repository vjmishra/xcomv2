package com.xpedx.nextgen.om.api;

import java.util.HashMap;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author sdodda
 * 
 * Used to create Fulfillment Order, On creation this will post the order create request to Legacy System too.
 *
 */
public class XPXCreateFulfillmentOrderAPI implements YIFCustomApi {

	private static YIFApi api = null;
	private static YFCLogCategory log;
	private Properties _properties = null;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}
	
	public static final String createOrderTemplate = "global/template/api/createOrder.XPXLegacyOrderCreationService.xml";
	
	/**
	 * Expects Fulfillment Order's Create XML.
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document invoke(YFSEnvironment env, Document inXML) throws Exception {
		
		// check if the Environment/Input document passed is null, throw exception
		if (env == null) {
			throw new YFSException("YFSEnvironment cannot be null or invalid to XPXCreateFulfillmentOrderAPI.invoke()");
		}
		if (inXML == null) {
			throw new YFSException("Input XML document cannot be null or invalid to XPXCreateFulfillmentOrderAPI.invoke()");
		}
		
		api = YIFClientFactory.getInstance().getApi();
		setProgressYFSEnvironmentVariables(env);
		
		if (log.isDebugEnabled()) {
			log.debug("XPXCreateFulfillmentOrderAPI-InXML" + SCXmlUtil.getString(inXML));
		}
		
		Element orderExtnElem = SCXmlUtil.getChildElement(inXML.getDocumentElement(), "Extn");
		if (orderExtnElem.hasAttribute("ExtnLastOrderOperation")) {
			orderExtnElem.removeAttribute("ExtnLastOrderOperation");
		}
		if (orderExtnElem.hasAttribute("ExtnOrderConfirmationEmailSentFlag")) {
			orderExtnElem.removeAttribute("ExtnOrderConfirmationEmailSentFlag");
		}			
		
		Document cOrderDoc = (Document)env.getTxnObject("CustomerOrderData");
		YFCDocument yfccOrderDoc = YFCDocument.getDocumentFor(cOrderDoc);
		YFCElement cOrderEle = yfccOrderDoc.getDocumentElement();
		String cOrderHeaderKey = cOrderEle.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
		
		YFCElement cOrderExtnEle = cOrderEle.getChildElement("Extn");
		String lastOrderOperation = null;
		String orderEmailConfirmationSentFlag = null;		
		if (cOrderExtnEle.hasAttribute("ExtnLastOrderOperation")) {
			lastOrderOperation = cOrderExtnEle.getAttribute("ExtnLastOrderOperation");
			
		}
		if (cOrderExtnEle.hasAttribute("ExtnOrderConfirmationEmailSentFlag")) {
			orderEmailConfirmationSentFlag = cOrderExtnEle.getAttribute("ExtnOrderConfirmationEmailSentFlag");			
		}
		
		// Set createOrder API Output Template
		env.setApiTemplate(XPXLiterals.CREATE_ORDER_API, createOrderTemplate);
		
		// Create Fulfillment Order
		Document docCreateOrderOutput = api.invoke(env, XPXLiterals.CREATE_ORDER_API, inXML);
		
		// Clear createOrder Template
		env.clearApiTemplate(XPXLiterals.CREATE_ORDER_API);
		
		// Post the Create Order input to Legacy via Web Methods Interface
		// In case of failure ignore the Exception and proceed with next steps.
		try {
			api.executeFlow(env, XPXLiterals.SERVICE_POST_LEGACY_ORDER_CREATE, docCreateOrderOutput);
		} catch (Exception e) {
			
			log.error("XPXCreateFulfillmentOrderAPI - Exception occured on posting XML to Legacy");			
			prepareErrorObject(e, XPXLiterals.OP_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inXML);
			
			if(e instanceof YFSException) {
				YFSException yfe = (YFSException)e;
				if(log.isDebugEnabled()) {
					log.debug("XPXCreateFulfillmentOrderAPI - YFSException error code on posting XML to Legacy :["+yfe.getErrorCode()+"]");
				}
				if("javax.xml.ws.soap.SOAPFaultException".equals(yfe.getErrorCode())) {
					if("N".equals(orderEmailConfirmationSentFlag)) {
						if(lastOrderOperation != null && lastOrderOperation.trim().length()>0) {
							lastOrderOperation=lastOrderOperation.trim();
							if ("OrderPlacement".equals(lastOrderOperation) || "OrderEdit".equals(lastOrderOperation)) {
								//createOrderEmailDocument();
								if(log.isDebugEnabled()) {
									log.debug("Inside XPXCreateFulfillmentOrderAPI class.");
									log.debug("InputXML-XPXPutOrderChangesInOrderConfirmationEmailQueue service to send Order Confirmation Email: "+SCXmlUtil.getString(cOrderEle.getOwnerDocument().getDocument()));
								}
								
								try {
									api.executeFlow(env, "XPXPutOrderChangesInOrderConfirmationEmailQueue", cOrderEle.getOwnerDocument().getDocument());
								}catch(Exception ex) {
									log.error("Exception occured on posting order confirmation email XML to  XPXPutOrderChangesInOrderConfirmationEmailQueue service "+ex.getMessage());
								}								
								orderEmailConfirmationSentFlag="Y";	
								XPXUtils utilsObj = new XPXUtils();
								utilsObj.callChangeOrder(env, cOrderHeaderKey, orderEmailConfirmationSentFlag, this.getClass().getSimpleName());
							
							} else if("OrderApproved".equals(lastOrderOperation)) {							
								//Forming an input document to send Order Approved Email [Input for YCD_Order_Approval_Email_8.5 service]
								XPXUtils utilsObj = new XPXUtils();
								YFCDocument orderApprovedEmailInputDoc = utilsObj.createOrderApprovedEmailInputDoc(cOrderEle);
								if(log.isDebugEnabled()) {
									log.debug("Inside XPXCreateFulfillmentOrderAPI class.");
									log.debug("InputXML-YCD_Order_Approval_Email_8.5 service to send Order Approved Email: "+SCXmlUtil.getString(orderApprovedEmailInputDoc.getDocument()));
								}
								try {
									api.executeFlow(env, "YCD_Order_Approval_Email_8.5", orderApprovedEmailInputDoc.getDocument());
								}catch(Exception ex) {
									log.error("Exception occured on posting order approved email XML to YCD_Order_Approval_Email_8.5 service "+ex.getMessage());
								}
								
								orderEmailConfirmationSentFlag="Y";								
								utilsObj.callChangeOrder(env, cOrderHeaderKey, orderEmailConfirmationSentFlag, this.getClass().getSimpleName());
							}
						}
					}
				}
			}			
            return inXML;
		}
		
		log.info("XPXCreateFulfillmentOrderAPI-OutXML:" + SCXmlUtil.getString(inXML));
		return  inXML;
	}
	
	private void setProgressYFSEnvironmentVariables(YFSEnvironment env) {
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				envVariablesmap.put("isDiscountCalculate", "false");
				envVariablesmap.put("isPnACall", "false");
			}
			else{
				envVariablesmap=new HashMap();
				envVariablesmap.put("isDiscountCalculate", "false");
				envVariablesmap.put("isPnACall", "false");
			}
			
			clientVersionSupport.setClientProperties(envVariablesmap);
		}
	}
	
	/**
	 * This method prepares the error object with the exception details which in
	 * turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML) {
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		errorObject.setExceptionMessage(e.getMessage());
		ErrorLogger.log(errorObject, env);
	}
	
	public void setProperties(Properties properties) throws Exception {
		_properties = properties;
	}
}

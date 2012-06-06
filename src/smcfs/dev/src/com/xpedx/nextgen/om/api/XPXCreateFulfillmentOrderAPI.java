package com.xpedx.nextgen.om.api;

import java.util.HashMap;
import java.util.Properties;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.woodstock.util.frame.log.base.ISCILogger;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategoryFactory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author sdodda
 * 
 * Used to create Fulfillment Order, On creation this will post the order create request to Legacy System too.
 *
 */
public class XPXCreateFulfillmentOrderAPI implements YIFCustomApi {

	/** API object. */
	private static YIFApi api = null;
	private static ISCILogger log = null;
	static{
		log = new YFCLogCategoryFactory().getLogger(XPXCreateFulfillmentOrderAPI.class.getCanonicalName());
	}
	private Properties _properties = null;
	public static final String createOrderTemplate = "global/template/api/createOrder.XPXLegacyOrderCreationService.xml";
	
	
	public void setProperties(Properties properties) throws Exception {
		_properties = properties;
	}
	
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
			// Throw new YFSException with the description
			throw new YFSException("YFSEnvironment cannot be null or invalid to XPXCreateFulfillmentOrderAPI.invoke()");
		}
		if (inXML == null) {
			// Throw new YFSException with the description
			throw new YFSException("Input XML document cannot be null or invalid to XPXCreateFulfillmentOrderAPI.invoke()");
		}
		
		api = YIFClientFactory.getInstance().getApi();
		
		setProgressYFSEnvironmentVariables(env);
		
		log.debug("XPXCreateFulfillmentOrderAPI.invoke()--> Input XML" + SCXmlUtil.getString(inXML));
		// Set createOrder API Output Template
		env.setApiTemplate(XPXLiterals.CREATE_ORDER_API, createOrderTemplate);
		
		// Create Fulfillment Order
		Document docCreateOrderOutput = api.invoke(env, XPXLiterals.CREATE_ORDER_API, inXML);
		
		// Clear createOrder Template
		env.clearApiTemplate(XPXLiterals.CREATE_ORDER_API);
		
		//Copying the Fulfillment OHK to a transaction object to handle ProcessCode="D" scenario in OPResponse flow
		String fulfillmentOHK = docCreateOrderOutput.getDocumentElement().getAttribute("OrderHeaderKey");
		env.setTxnObject("FOKEY", fulfillmentOHK);
		env.setTxnObject("FulFillmentOrderDetails", docCreateOrderOutput);
		
		// Post the Create Order input to Legacy via Web Methods Interface
		// In case of failure ignore the Exception and proceed with next steps.
		try{
			api.executeFlow(env, XPXLiterals.SERVICE_POST_LEGACY_ORDER_CREATE, docCreateOrderOutput);
		} catch (Exception e) {
			log.error("XPXCreateFulfillmentOrderAPI.invoke()--> Unable to Process POST XML to Legacy", e);
			
			
		      com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
			
		      errorObject.setTransType("OP");
		      errorObject.setErrorClass("Application");
		      errorObject.setInputDoc(docCreateOrderOutput);
		      		      
		      errorObject.setException(e);
		
		      ErrorLogger.log(errorObject, env);
		
            return inXML;
		}
		
		log.debug("XPXCreateFulfillmentOrderAPI.invoke()--> Output XML" + SCXmlUtil.getString(inXML));
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
	
}

/**
 *
 */
package com.sterlingcommerce.xpedx.webchannel.servlet.eprocurement;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.security.SCUISecurityResponse;
import com.sterlingcommerce.ui.web.framework.utils.SCUIContextHelper;
import com.sterlingcommerce.ui.web.platform.utils.SCUIPlatformUtils;
import com.sterlingcommerce.webchannel.common.eprocurement.AribaContextImpl;
import com.sterlingcommerce.webchannel.common.eprocurement.IProcurementContext;
import com.sterlingcommerce.webchannel.common.integration.Error;
import com.sterlingcommerce.webchannel.common.integration.IAribaConstants;
import com.sterlingcommerce.webchannel.common.integration.IWCIntegrationStatusCodes;
import com.sterlingcommerce.webchannel.common.integration.WCIntegrationResponse;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCException;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.webchannel.servlet.eprocurement.AribaIntegrationServlet;
import com.sterlingcommerce.webchannel.utilities.WCIntegrationHelper;
import com.sterlingcommerce.webchannel.utilities.WCIntegrationXMLUtils;
import com.sterlingcommerce.xpedx.webchannel.common.eprocurement.XPEDXCXMLMessageFields;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXPunchoutServlet extends AribaIntegrationServlet {
	XPEDXCXMLMessageFields cXMLFields = null;
	WCIntegrationResponse wcResponse = null;

	// This class is extending a class in the product - is there any Sterling 9.0 docs on it?
	// This overrides two base class methods though not sure how/why they get invoked.

	@Override
	protected SCUISecurityResponse authenticateRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		//SCUISecurityResponse securityResponse = super.authenticateRequest(req, res);
    	SCUISecurityResponse securityResponse =  null;
    	String startPageURL;

    	try {
    		// Initialize fields from incoming cXML
    		Document doc = (Document)req.getAttribute(IAribaConstants.ARIBA_CXML_REQUEST_ATTRIBUTE_KEY);
    		cXMLFields = new XPEDXCXMLMessageFields(doc);

    		//TODO Log in here because seem to need to be auth'ed so can call mashups - get guest from properties file?
    		Document dummyloginDoc = WCIntegrationXMLUtils.prepareLoginInputDoc("dave@perrigo.com","Password1");
    		securityResponse = SCUIPlatformUtils.login(dummyloginDoc,SCUIContextHelper.getUIContext(req, res));

    		log.info("Dummy Auth " + (securityResponse.getReturnStatus() ? "Successful" : "FAILED") + " - PayLoadID:"+cXMLFields.getPayLoadId());

    		String custIdentity	= cXMLFields.getCustomerIdentity();
			String cxmlSecret   = cXMLFields.getAuthPassword();

			// Extract credentials from incoming cXML to compare to customer in DB
    		Element custExtnElement = XPEDXWCUtils.getPunchoutConfigForCustomerIdentity(req, res, custIdentity);
    		if (custExtnElement == null) {
				log.warn("Can't get Punchout config for cust identity in cXML: " + custIdentity);
				return new SCUISecurityResponse(false, "Invalid Identity");
    		}

			String dbSecret = SCXmlUtil.getAttribute(custExtnElement, "ExtnSharedSecret");
			String dbStartPage = SCXmlUtil.getAttribute(custExtnElement, "ExtnStartPageURL");
			//String userXPath = SCXmlUtil.getAttribute(custExtnElement, "ExtnCXmlUserXPath"); might support later

    		// validate SharedSecret matches DB for cust
			if (!cxmlSecret.equals(dbSecret)) {
				log.warn("Shared Secret from cXML \'" + cxmlSecret + "\' does not match DB cust identity " + custIdentity);
				return new SCUISecurityResponse(false, "Invalid SharedSecret");
			}


			//TODO extract user from cXML and replace the one in the DB startURL
			// *if* the one specified is valid punchout user in system; otherwise keep default
			// [later: extract optional user from cxml using userXPath]

			//TODO do we need to login actual user here ?
    		String userid = obtainUserid(dbStartPage);
			String passwd = dbStartPage.substring( dbStartPage.indexOf("pwd=")+4 );

			Document loginDoc = WCIntegrationXMLUtils.prepareLoginInputDoc(userid, passwd);
    		securityResponse = SCUIPlatformUtils.login(loginDoc,SCUIContextHelper.getUIContext(req, res));
    		//From 2010: "TODO Call platform exposed method authenticate(loginDoc, request,response)once available; should return"

    		if (securityResponse.getReturnStatus())
    			log.info("Authentication successful for user " + userid + " - PayLoadID:" + cXMLFields.getPayLoadId());
    		else {
    			log.warn("Authentication failed for user " + userid + " - PayLoadID:" + cXMLFields.getPayLoadId());
    			return new SCUISecurityResponse(false, "Invalid User");
    		}

    		//TODO if userid specified in cXML, update URL with it

    		startPageURL = dbStartPage;
    	}
    	catch(Exception e){
    		logError("Exception during authentication: "+e.getMessage(),e);
    		throw new WCException("Exception during XPEDXPunchoutServlet.processRequest", e);  //TODO this doesn't seem to work right
    	}

    	//TODO securityResponse.getForwardURL() has jsessionid - any use?
    	System.out.println("------> Our start url to return: " + startPageURL); //TODO remove println's
    	System.out.println("------> securityResponse.getForwardURL(): " + securityResponse.getForwardURL());
		cXMLFields.setStartPageURL(startPageURL );

    	return securityResponse;
 	}

	private String obtainUserid(String dbStartPage) {
		// Extract user/pwd from DB configured start URL: "http://XXX?id=advance@punchout.com&pwd=punchout123"
		return dbStartPage.substring( dbStartPage.indexOf("?id=")+4, dbStartPage.indexOf("&") );
	}


	@Override
	protected WCIntegrationResponse processRequest (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
    		log.info("Punchout post Authentication process start >>>>>>>>"); //TODO keep these or make debug?

    		// NOTE: not sure what these calls to Sterling APIs do - need them all?

    		SCUISecurityResponse securityResponse = postAuthenticateUser(req, res);
    		if(YFCCommon.equals(SCUISecurityResponse.SUCCESS,securityResponse.getReturnStatus())) {

    			// Process Setup request
    			if(!YFCCommon.isVoid(cXMLFields) &&
       					YFCCommon.equals(IAribaConstants.CXML_REQUEST_SETUP_TAG,cXMLFields.getCXMLRequestType()))
    			{
    				log.info("Punchout post Authentication Successful >>>>>>>>");

    				populateAribaContext(wcContext);

    				if(YFCCommon.equals(IAribaConstants.ARIBA_OPERATION_EDIT_STRING, cXMLFields.getOperation(), true)||
       						YFCCommon.equals(IAribaConstants.ARIBA_OPERATION_INSPECT_STRING, cXMLFields.getOperation(),true))
       				{
       					if(YFCCommon.isVoid(cXMLFields.getOrderHeaderKey())) {
           	    			return sendFailureResponse("Mandatory parameter missing for EDIT/INSPECT: Order Header key is not present",
           	    					IWCIntegrationStatusCodes.MANDATORY_PARAMETER_MISSING);
       					}
       				}

    				try {
       					CommerceContextHelper.processProcurementPunchIn(wcContext); //TODO what does this do???
       				}
       		    	catch(Exception e) {
       	    			return sendFailureResponse("Failed to process the commerce context",
       	    					IWCIntegrationStatusCodes.COMMERCE_CONTEXT_FAILED);
       		    	}

    				// Shouldn't need this since we're now defining our own URL
    				// Note that it gives wrong server port - assumes 8001 regardless if running on 7002!
    				String aribaStartPageURL = WCIntegrationHelper.getAribaStartPageURL(cXMLFields, wcContext);
    		    	System.out.println("------> WCIntegrationHelper.getAribaStartPageURL: " + aribaStartPageURL);

    				//TODO "&" getting turned into "&amp;" - matter?
    		    	String startPageURL = cXMLFields.getStartPageURL(); // our custom URL from authenticateRequest

       				if(!YFCCommon.isVoid(startPageURL)) {
       		    		log.info("Start page URL being returned:" + startPageURL);

       		    		//cXMLFields.setStartPageURL(startPageURL);

       		    		wcResponse = sendSuccessResponse("Request for Auth setup is success");
       				}
       				else {
       	    			return sendFailureResponse("Cannot form start page url:",
       	    					IWCIntegrationStatusCodes.SERVER_ERROR);
       				}
       			}


    			// Process Order request
    			//TODO when is this used? Do we need story to support this?
    			else if(!YFCCommon.isVoid(cXMLFields) &&
       					YFCCommon.equals(IAribaConstants.CXML_REQUEST_ORDER_TAG,cXMLFields.getCXMLRequestType())) {

    				//Invoke the helper class which will call the service.
       				wcResponse = WCIntegrationHelper.processAribaOrderRequest(cXMLFields.getRequestDoc(), wcContext); //TODO what does?

       				if(wcResponse.getReturnStatus()) {
       					cXMLFields.setProcessStatus(true);  //TODO move this in success method so applies to setup too?
       		    		wcResponse = sendSuccessResponse("Order request processed successfully");
       				}
       				else {
       					cXMLFields.setProcessStatus(false);
       	    			return sendFailureResponse("Order request process failed:",
       	    					IWCIntegrationStatusCodes.API_ERROR);
       				}
       			}
    		}

    		else {
    			return sendFailureResponse("Post Authentication failed",
    					IWCIntegrationStatusCodes.REQUEST_AUTHENTICATION_FAILED);
    		}
    	}
    	catch(Exception e) {
    		logError("Exception in XPEDXPunchoutServlet.processRequest",e);
			return sendFailureResponse("Post Authentication failed",
					IWCIntegrationStatusCodes.REQUEST_AUTHENTICATION_FAILED);
    	}

    	log.info("Post Authentication process End >>>>>>>>");
    	return wcResponse;
    }

	private WCIntegrationResponse sendSuccessResponse(String errorMessage) {

		//TODO should setProcStatus for all successes?  cXMLFields.setProcessStatus(true);
		// [This appears to log ERROR - any way to avoid that?]
		return new WCIntegrationResponse( WCIntegrationResponse.SUCCESS,
				new Error(new Long(IWCIntegrationStatusCodes.SUCCESS), errorMessage));
	}

	private WCIntegrationResponse sendFailureResponse(String errorMessage, int errorCode) {
		logError("Error Code: "+errorCode + " & ErrorDesc: "+ errorMessage);

		//TODO should setProcStatus for all failures?  cXMLFields.setProcessStatus(false);
		return new WCIntegrationResponse(WCIntegrationResponse.FAILURE,
				new Error(new Long(errorCode),errorMessage));
	}


	private void populateAribaContext(IWCContext wcContext)
	{
		AribaContextImpl aribaContext = AribaContextImpl.getInstance();
		aribaContext.setAribaOperation(cXMLFields.getOperation());
		aribaContext.setOrderHeaderKey(cXMLFields.getOrderHeaderKey());
		aribaContext.setReturnURL(cXMLFields.getReturnURL());
		aribaContext.setBuyerCookie(cXMLFields.getBuyerCookie());
		aribaContext.setFromIdentity(cXMLFields.getFromIdentity());
		aribaContext.setToIdentity(cXMLFields.getToIdentity());
		aribaContext.setPayloadID(cXMLFields.getPayLoadId());
		wcContext.setWCAttribute(IProcurementContext.PROCUREMENT_CONTEXT_ATTRIBUTE_KEY, aribaContext, WCAttributeScope.SESSION);
		if (log.isDebugEnabled()){
			logDebug(">>>>>>>>>>Details of AribaContext created:>>>>>>>>>>>.");
			logDebug("IAribaContext.operation:"+cXMLFields.getOperation());
			logDebug("IAribaContext.getOrderHeaderKey:"+cXMLFields.getOrderHeaderKey());
			logDebug("IAribaContext.getReturnURL:"+cXMLFields.getReturnURL());
			logDebug("IAribaContext.getBuyerCookie:"+cXMLFields.getBuyerCookie());
			logDebug("IAribaContext.getFromIdentity:"+cXMLFields.getFromIdentity());
			logDebug("IAribaContext.getToIdentity:"+cXMLFields.getToIdentity());
			logDebug("IAribaContext.getPayLoadId:"+cXMLFields.getPayLoadId());

		}
	}

	@Override
	public String getResponseDoc(Error errObj,boolean resStatus, IWCContext ctx)    {
		if(resStatus)
			wcResponse = new WCIntegrationResponse(WCIntegrationResponse.SUCCESS,errObj);
		else
			wcResponse = new WCIntegrationResponse(WCIntegrationResponse.FAILURE,errObj);
		String docString = WCIntegrationXMLUtils.prepareAribaResponseDoc(cXMLFields, wcResponse, ctx);
		return docString;
	}

	private static final Logger log = Logger.getLogger(XPEDXPunchoutServlet.class);

}

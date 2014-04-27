/**
 *
 */
package com.sterlingcommerce.xpedx.webchannel.servlet.eprocurement;

import java.io.IOException;
import java.net.URLEncoder;

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
	private static final String PARAMNAME_PASSWORD = "p=";
	private static final String PARAMNAME_USERID = "?u=";
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

    		String custIdentity	= cXMLFields.getCustomerIdentity();
			String cxmlSecret   = cXMLFields.getAuthPassword();
			String buyerCookie	= cXMLFields.getBuyerCookie();
			String returnUrl   = cXMLFields.getReturnURL();
			String toIdentity	= cXMLFields.getToIdentity();
			String payLoadID	= cXMLFields.getPayLoadId();

			// Extract credentials from incoming cXML to compare to customer in DB
    		Element custExtnElement = XPEDXWCUtils.getPunchoutConfigForCustomerIdentity(custIdentity);
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


			//TODO later for Toys'R'Us: extract user from incoming cXML and use it instead
			// *if* the one specified is valid punchout user in system; otherwise keep default

			// Not sure if we need to login actual user here, could auth some other way
			// but do want to make sure valid before return URL
    		//From 2010: "TODO Call platform exposed method authenticate(loginDoc, request,response)once available; should return"
    		String userid = obtainUserid(dbStartPage);
			String passwd = obtainPassword(dbStartPage);

			Document loginDoc = WCIntegrationXMLUtils.prepareLoginInputDoc(userid, passwd);
    		securityResponse = SCUIPlatformUtils.login(loginDoc,SCUIContextHelper.getUIContext(req, res));

    		if (securityResponse.getReturnStatus())
    			log.info("Authentication successful for user " + userid + " - PayLoadID:" + cXMLFields.getPayLoadId());
    		else {
    			log.warn("Authentication failed for user " + userid + " - PayLoadID:" + cXMLFields.getPayLoadId());
    			return new SCUISecurityResponse(false, "Invalid User");
    		}

    		//TODO later for TRU: if userid specified in cXML, update URL with it

    		// Add params to start URL from DB for this customer
    		String moreParams = "&payLoadID="+payLoadID+
    							// don't need now - later? "&operation=1&orderHeaderKey=val&selectedCategory=val&selectedItem=val&selectedItemUOM=val"+
    							"&buyerCookie="+buyerCookie+"&fromIdentity="+toIdentity+"&toIdentity="+custIdentity+"&sfId=xpedx" +
    							"&returnURL="+URLEncoder.encode(returnUrl,"UTF-8");

    		startPageURL = dbStartPage + moreParams;
    	}
    	catch(Exception e){
    		logError("Exception during authentication", e);
    		throw new WCException("Exception during XPEDXPunchoutServlet.processRequest", e);  //does this work right?
    	}

    	log.info("Punchout cXML setup: start URL to return: " + startPageURL);
		cXMLFields.setStartPageURL(startPageURL );

    	return securityResponse;
 	}

	// Extract user/pwd from DB configured start URL: "http://xxx?u=advance@punchout.com&p=punchout123"
	private String obtainUserid(String dbStartPage) {
		int userStart = dbStartPage.indexOf(PARAMNAME_USERID);
		int userEnd   = dbStartPage.indexOf("&");
		return dbStartPage.substring(userStart+PARAMNAME_USERID.length(), userEnd);
	}
	private String obtainPassword(String dbStartPage) {
		// look for '&' to end pwd string in case they add more params to URL in DB
		int pwdStart = dbStartPage.indexOf(PARAMNAME_PASSWORD);
		int pwdAmp   = dbStartPage.indexOf("&", pwdStart);
		int pwdEnd = (pwdAmp != -1) ? pwdAmp : dbStartPage.length();

		return dbStartPage.substring(pwdStart+PARAMNAME_PASSWORD.length(), pwdEnd);
	}


	@Override
	protected WCIntegrationResponse processRequest (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {

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
       					CommerceContextHelper.processProcurementPunchIn(wcContext); // what does this do?
       				}
       		    	catch(Exception e) {
       	    			return sendFailureResponse("Failed to process the commerce context",
       	    					IWCIntegrationStatusCodes.COMMERCE_CONTEXT_FAILED);
       		    	}

    				// Shouldn't need this since we're now defining our own URL
    				// Note that it gives wrong server port - assumes 8001 regardless if running on 7002!
    				//String aribaStartPageURL = WCIntegrationHelper.getAribaStartPageURL(cXMLFields, wcContext);

    				//TODO "&" getting turned into "&amp;" - matter?
    		    	String startPageURL = cXMLFields.getStartPageURL(); // our custom URL from authenticateRequest

       				if(!YFCCommon.isVoid(startPageURL)) {
       		    		log.info("Start page URL being returned:" + startPageURL);

       		    		//cXMLFields.setStartPageURL(startPageURL); we've already set this earlier

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

		// should setProcStatus for all successes?  cXMLFields.setProcessStatus(true);
		// [This appears to log ERROR - any way to avoid that?]
		return new WCIntegrationResponse( WCIntegrationResponse.SUCCESS,
				new Error(new Long(IWCIntegrationStatusCodes.SUCCESS), errorMessage));
	}

	private WCIntegrationResponse sendFailureResponse(String errorMessage, int errorCode) {
		logError("Error Code: "+errorCode + " & ErrorDesc: "+ errorMessage);

		// should setProcStatus for all failures?  cXMLFields.setProcessStatus(false);
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

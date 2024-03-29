/**
 *
 */
package com.sterlingcommerce.xpedx.webchannel.servlet.eprocurement;

import java.io.IOException;
import java.util.UUID;

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
import com.sterlingcommerce.xpedx.webchannel.crypto.EncryptionUtils;
import com.sterlingcommerce.xpedx.webchannel.punchout.PunchoutCxmlLoginAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.core.YFSSystem;

/**
 * This handles the CXML authentication handshake. The customer makes a POST request to this servlet with a CXML document
 *  containing their identity and secret (and other metadata). On successful authentication, they are sent a url that
 *  will allow them access to punchout (in web channel).
 * <br>
 * The response contains a url that points to PunchoutCxmlLoginAction
 * @see PunchoutCxmlLoginAction
 */
public class XPEDXPunchoutCxmlServlet extends AribaIntegrationServlet {

	XPEDXCXMLMessageFields cXMLFields = null;
	WCIntegrationResponse wcResponse = null;

	// This class is extending a class in the product - is there any Sterling 9.0 docs on it?
	// This overrides two base class methods though not sure how/why they get invoked.

	@Override
	protected SCUISecurityResponse authenticateRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		try {
			// Initialize fields from incoming cXML
			Document doc = (Document)req.getAttribute(IAribaConstants.ARIBA_CXML_REQUEST_ATTRIBUTE_KEY);
			cXMLFields = new XPEDXCXMLMessageFields(doc);

			String custIdentity	= cXMLFields.getCustomerIdentity();
			String cxmlSecret   = cXMLFields.getAuthPassword();
			String buyerCookie	= cXMLFields.getBuyerCookie();
			String returnUrl   = cXMLFields.getReturnURL();
			String toIdentity	= cXMLFields.getToIdentity();
			String payloadId	= cXMLFields.getPayLoadId();

			// Extract credentials from incoming cXML to compare to customer in DB
			Element custExtnElement = XPEDXWCUtils.getPunchoutConfigForCustomerIdentity(custIdentity);
			if (custExtnElement == null) {
				log.warn("Can't get Punchout config for cust identity in cXML: " + custIdentity);
				return new SCUISecurityResponse(false, "Invalid Identity");
			}

			String dbSecret = SCXmlUtil.getAttribute(custExtnElement, "ExtnSharedSecret");

			// validate SharedSecret matches DB for cust
			if (!cxmlSecret.equals(dbSecret)) {
				log.warn("Shared Secret from cXML \'" + cxmlSecret + "\' does not match DB cust identity " + custIdentity);
				return new SCUISecurityResponse(false, "Invalid SharedSecret");
			}

			// Not sure if we need to login actual user here, could auth some other way
			// but do want to make sure valid before return URL
			//From 2010: "Call platform exposed method authenticate(loginDoc, request,response)once available; should return"

			String userid = SCXmlUtil.getAttribute(custExtnElement, "ExtnUsernameParam");
			String encrypted = SCXmlUtil.getAttribute(custExtnElement, "ExtnUserPwdParam");
			String passwd = "";
			if (!userid.isEmpty()) {
				passwd = EncryptionUtils.decrypt(encrypted);
			}

			Document loginDoc = WCIntegrationXMLUtils.prepareLoginInputDoc(userid, passwd);
			SCUISecurityResponse securityResponse = SCUIPlatformUtils.login(loginDoc,SCUIContextHelper.getUIContext(req, res));

			if (securityResponse.getReturnStatus()) {
				log.info("Authentication successful for user " + userid + " - PayLoadID:" + cXMLFields.getPayLoadId());
			} else {
				log.warn("Authentication failed for user " + userid + " - PayLoadID:" + cXMLFields.getPayLoadId());
				return new SCUISecurityResponse(false, "Invalid User");
			}

			// eb-6915: create a cxml session and return url with token in it
			String sessionId = persistCxmlSession(userid, encrypted, payloadId, buyerCookie, toIdentity, custIdentity, returnUrl);

			String startPageURL = YFSSystem.getProperty("punchout.cxml.login.url") + "?sfId=xpedx&sessionId=" + sessionId;

			log.info("Punchout cXML setup: start URL to return: " + startPageURL);
			cXMLFields.setStartPageURL(startPageURL );

			return securityResponse;

		} catch(Exception e){
			logError("Exception during authentication", e);
			throw new WCException("Exception during XPEDXPunchoutServlet.processRequest", e);  //does this work right?
		}
	}

	/**
	 * @param userid
	 * @param passwd Encrypted password
	 * @param payloadId
	 * @param buyerCookie
	 * @param fromIdentity
	 * @param toIdentity
	 * @param returnUrl This is the hook url provided by customer, where web channel will post cxml cart data when user has completed shopping.
	 * @return
	 */
	private String persistCxmlSession(String userid, String passwd, String payloadId, String buyerCookie, String fromIdentity, String toIdentity, String returnUrl) {
		String sessionId = UUID.randomUUID().toString().replace("-", "");

		Element punchoutCxmlSessionElem = SCXmlUtil.createDocument("XPEDXPunchoutCxmlSession").getDocumentElement();

		punchoutCxmlSessionElem.setAttribute("PunchoutCxmlSessionId", sessionId);
		punchoutCxmlSessionElem.setAttribute("Userid", userid);
		punchoutCxmlSessionElem.setAttribute("Passwd", passwd);
		punchoutCxmlSessionElem.setAttribute("PayloadId", payloadId);
		punchoutCxmlSessionElem.setAttribute("BuyerCookie", buyerCookie);
		punchoutCxmlSessionElem.setAttribute("FromIdentity", fromIdentity);
		punchoutCxmlSessionElem.setAttribute("ToIdentity", toIdentity);
		punchoutCxmlSessionElem.setAttribute("ReturnUrl", returnUrl);

		Document outputDoc = XPEDXWCUtils.handleApiRequestBeforeAuthentication("createPunchoutCxmlSession", punchoutCxmlSessionElem.getOwnerDocument(), true, null);
		logDebug("outputDoc: " + SCXmlUtil.getString(outputDoc));

		return sessionId;
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
					log.debug("Punchout post Authentication Successful");

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

					// "&" getting turned into "&amp;" - ok?
					String startPageURL = cXMLFields.getStartPageURL(); // our custom URL from authenticateRequest

					if(!YFCCommon.isVoid(startPageURL)) {

						//cXMLFields.setStartPageURL(startPageURL); we've already set this earlier

						wcResponse = sendSuccessResponse("Request for Auth setup is success");
					}
					else {
						return sendFailureResponse("Cannot form start page url:",
								IWCIntegrationStatusCodes.SERVER_ERROR);
					}
				}


				// Process Order request - TODO remove this if not going to support
				else if(!YFCCommon.isVoid(cXMLFields) &&
						YFCCommon.equals(IAribaConstants.CXML_REQUEST_ORDER_TAG,cXMLFields.getCXMLRequestType())) {

					//Invoke the helper class which will call the service.
					wcResponse = WCIntegrationHelper.processAribaOrderRequest(cXMLFields.getRequestDoc(), wcContext); // what does this do?

					if(wcResponse.getReturnStatus()) {
						cXMLFields.setProcessStatus(true);  // move this in success method so applies to setup too?
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
			logDebug("=======Details of AribaContext created:=========.");
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

	private static final Logger log = Logger.getLogger(XPEDXPunchoutCxmlServlet.class);

}

package com.sterlingcommerce.xpedx.webchannel.punchout.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.sterlingcommerce.ui.web.framework.security.SCUISecurityResponse;
import com.sterlingcommerce.ui.web.framework.utils.SCUIContextHelper;
import com.sterlingcommerce.ui.web.platform.utils.SCUIPlatformUtils;
import com.sterlingcommerce.webchannel.common.integration.Error;
import com.sterlingcommerce.webchannel.common.integration.IWCIntegrationStatusCodes;
import com.sterlingcommerce.webchannel.common.integration.WCIntegrationResponse;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCException;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.servlet.common.IntegrationServlet;
import com.sterlingcommerce.webchannel.utilities.WCIntegrationXMLUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;

/*
 * This handles OCI URL requests from external procurement systems. It was
 * initially copied from (unfinished) OciIntegrationServlet.java of 2010.
 * Changes have been made to work around issues in base Sterling code and
 * use a custom approach for authenticating and redirecting to site.
 * (Also tried to refactor some of the convoluted code from that class)
 */
public class XPEDXOciServlet extends IntegrationServlet {
	private static final Logger log = Logger.getLogger(XPEDXOciServlet.class);

	protected IWCContext wcContext = null;
	private String id="";
	private String pwd="";
	private String landingURL = "";


	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		WCIntegrationResponse wcIntegrationRes = null;

		landingURL = "/error"; //need to clear/set these class-wide vars
		wcContext = WCContextHelper.getWCContext(request);

		// Auth/login the user/pass
		SCUISecurityResponse securityResponse = authenticateRequest(request, response);

		if (YFCCommon.equals(SCUISecurityResponse.SUCCESS, securityResponse.getReturnStatus())) {

			wcIntegrationRes = processRequest(request, response); // sets landingURL which is returned by getResponseDoc

			if (wcIntegrationRes.getReturnStatus()) {
				// *** Success - redirect to catalog/site via pLogin.action
				response.sendRedirect(getResponseDoc(wcIntegrationRes.getErrorObj(),
						wcIntegrationRes.getReturnStatus(), wcContext));
				return;
			}
		}

		// is this best way to return errors?
		errorMessage = "OCI Authentication/setup failed";
		errorCode = new Long(IWCIntegrationStatusCodes.REQUEST_AUTHENTICATION_FAILED);
		logError("Error Code:" + errorCode + "& ErrorDesc:"+ errorMessage);
//			wcIntegrationRes = new WCIntegrationResponse(WCIntegrationResponse.FAILURE,
//					new Error(errorCode, errorMessage));
		response.sendError(HttpServletResponse.SC_FORBIDDEN, errorMessage);
	}

	@Override
	protected WCIntegrationResponse processRequest(HttpServletRequest req,
			HttpServletResponse res) throws ServletException, IOException {
		WCIntegrationResponse wcResponse;
		String errorMessage = null;

		try {
			// Need to pass along the URL of the procurement system for later checkout
			// [does hook that includes params pass through ok to this servlet and from here to pLogin.action ?]
			String hookUrl = wcContext.getSCUIContext().getRequest().getParameter("hook_url");
			String encodedHookUrl = URLEncoder.encode(hookUrl,"UTF-8");

			// Create custom URL and redirect to that
			// -> reusing the login action from cXML
			String loginAction = "/swc/common/pLogin.action";
			String auth = "u=" + id + "&p=" + pwd;
			String sfId = wcContext.getStorefrontId();

			landingURL = loginAction +"?"+ auth + "&sfId=" + sfId + "&returnURL=" + encodedHookUrl;

			errorCode = new Long(IWCIntegrationStatusCodes.SUCCESS);
			logInfo("OCI login page URL: " + landingURL);
			wcResponse = new WCIntegrationResponse(WCIntegrationResponse.SUCCESS,
					new Error(errorCode, errorMessage));
		}
		catch (Exception e) {
			// not sure if this would happen normally
			errorMessage = "OCI processRequest failed";
			errorCode = new Long(IWCIntegrationStatusCodes.REQUEST_AUTHENTICATION_FAILED);
			logError("Error Code:" + errorCode + "& ErrorDesc:" + errorMessage + e.getMessage(), e);
			return new WCIntegrationResponse(WCIntegrationResponse.FAILURE,
					new Error(errorCode, errorMessage));
		}

		return wcResponse;
	}

	// called directly from doPost above, and apparently called by parent on failure
	@Override
	public String getResponseDoc(Error errObj, boolean resStatus, IWCContext ctx) {

		return landingURL;
	}

	// not sure if/when this gets called by parent (called directly from this doPost)
	@Override
	protected SCUISecurityResponse authenticateRequest(HttpServletRequest req,
			HttpServletResponse res) throws ServletException, IOException {
		SCUISecurityResponse scuiSecurityResponse =  null;
		id = req.getParameter("id");
		pwd = req.getParameter("pwd");

		try {
			scuiSecurityResponse = authenticateUser(req, res);

			// Need to support this in v2 ?
			//scuiSecurityResponse = loginAlternateUser(req, res);

			if (YFCCommon.equals(SCUISecurityResponse.SUCCESS, scuiSecurityResponse.getReturnStatus())) {
				logInfo("OCI Authentication Successful for " + id);

				//scuiSecurityResponse = postAuthenticateUser(req, res); //TODO need?
			}
		}
		catch (Exception e) {
			logError("Problem authenticating OCI user: " + id, e);
			throw new WCException("Problem authenticating specified OCI user", e);
		}

		return scuiSecurityResponse;
	}

	private SCUISecurityResponse authenticateUser(HttpServletRequest req, HttpServletResponse res) {

		// is this login best way to authenticate specified user/pass? (cXML does same)
		Document loginDoc = WCIntegrationXMLUtils.prepareLoginInputDoc(id, pwd);
		SCUISecurityResponse scuiSecurityResponse = SCUIPlatformUtils.login(loginDoc,
				SCUIContextHelper.getUIContext(req, res));
		return scuiSecurityResponse;
	}

	//TODO Need to support this feature in v2?  Does this code from 2010 actually work?
	// If diff user/pass param name & domain are specified in cust config, use them instead.
	// When this applies, will incoming URL still contain id/pwd that can be used above?
	private SCUISecurityResponse loginAlternateUser(HttpServletRequest req, HttpServletResponse res) {
		SCUISecurityResponse scuiSecurityResponse = null;
		IWCContext context = WCContextHelper.getWCContext(req);

		String customerID = context.getCustomerId();
		String storefrontID = context.getStorefrontId();

		// This likely requires logged in user to invoke (so may need dummy if above doesn't work for this)
		String[] CustOCIFields = XPEDXWCUtils.getOCIFieldsForCustomer(req, res, customerID, storefrontID);

		if(CustOCIFields[0]!=null && "Y".equals(CustOCIFields[0].trim())){
			String newid = req.getParameter(CustOCIFields[1]);
			String newpwd = req.getParameter(CustOCIFields[2]);
			String emailTemplate = CustOCIFields[3];

			Document loginDoc = WCIntegrationXMLUtils.prepareLoginInputDoc(
					newid+emailTemplate, newpwd);

			scuiSecurityResponse = SCUIPlatformUtils.login(loginDoc,
					SCUIContextHelper.getUIContext(req, res));

			logInfo("OCI Authentication Successful");

			if (scuiSecurityResponse.getReturnStatus()) {
				id = newid;
				pwd = newpwd;
			}
		}
		return scuiSecurityResponse;
	}

	// not sure if/when this gets called by parent (called directly from this doPost)
	@Override
	protected boolean isValidRequest(HttpServletRequest request, HttpServletResponse response)
			throws WCException{
    	return true;
 	}

	// not sure if/when this gets called by parent (called directly from this doPost)
	@Override
	public Error getErrorObject(Long errorCode, String errorMsg) {
		return new Error(errorCode, errorMsg);
	}

	// Leftover from 2010 Sterling impl, assume don't need
//	private void populateOCIContext(IWCContext wcContext, String redirectURL) {
//		OciContextImpl ociContext = OciContextImpl.getInstance();
//		ociContext.setOciOperation("CREATE");
//		ociContext.setOrderHeaderKey(null);
//		ociContext.setReturnURL(redirectURL);
//		ociContext.setBuyerCookie(null);
//		wcContext.setWCAttribute(
//				IProcurementContext.PROCUREMENT_CONTEXT_ATTRIBUTE_KEY,
//				ociContext, WCAttributeScope.SESSION);
//	}

	public static void logError(String s, Throwable e) {
		log.error("OCI Integration Servlet:" + s, e);
	}

	public static void logError(String s) {
		log.error("OCI Integration Servlet:" + s);
	}

	public static void logInfo(String s) {
		log.info("OCI Integration Servlet:" + s);
	}

	public static void logDebug(String s) {
		log.debug("OCI Integration Servlet:" + s);
	}

}
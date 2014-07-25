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
import com.sterlingcommerce.xpedx.webchannel.common.punchout.PunchoutOciUtil;
import com.sterlingcommerce.xpedx.webchannel.common.punchout.PunchoutOciUtil.OciCredentials;
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
	private String id = "";
	private String pwd = "";
	private String landingURL = "";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WCIntegrationResponse wcIntegrationResp = null;

		landingURL = "/error"; // need to clear/set these class-wide vars
		wcContext = WCContextHelper.getWCContext(request);

		// Auth/login the user/pass
		SCUISecurityResponse securityResponse = authenticateRequest(request, response);

		if (YFCCommon.equals(SCUISecurityResponse.SUCCESS, securityResponse.getReturnStatus())) {
			wcIntegrationResp = processRequest(request, response); // sets landingURL which is returned by getResponseDoc

			if (wcIntegrationResp.getReturnStatus()) {
				// *** Success - redirect to catalog/site via pLogin.action
				response.sendRedirect(getResponseDoc(wcIntegrationResp.getErrorObj(), wcIntegrationResp.getReturnStatus(), wcContext));
				return;
			}
		}

		// is this best way to return errors?
		errorMessage = "OCI Authentication/setup failed";
		errorCode = new Long(IWCIntegrationStatusCodes.REQUEST_AUTHENTICATION_FAILED);
		logError("Error Code: " + errorCode + ", ErrorDesc: "+ errorMessage);
		response.sendError(HttpServletResponse.SC_FORBIDDEN, errorMessage);
	}

	// not sure if/when this gets called by parent (called directly from this doPost)
	@Override
	protected SCUISecurityResponse authenticateRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (req.getParameter("data") != null) {
			// eb-4834: encrypted user/pass
			try {
				OciCredentials cred = PunchoutOciUtil.decryptData(req.getParameter("data"));
				id = cred.getUserId();
				pwd = cred.getPassword();

			} catch (Exception e) {
				logError("Failed to decrypt parameter: data");
				throw new WCException("Parameter 'data' is not formatted properly", e);
			}

		} else if (req.getParameter("id") != null && req.getParameter("pwd") != null) {
			// legacy mode: unencryptd user/pass (available until R17)
			id = req.getParameter("id");
			pwd = req.getParameter("pwd");

		} else {
			// TODO return meaningful security response
			logError("Missing required parameter: data");
			throw new WCException("Missing required parameter: data");
		}

		try {
			SCUISecurityResponse scuiSecurityResponse = authenticateUser(req, res);

			if (YFCCommon.equals(SCUISecurityResponse.SUCCESS, scuiSecurityResponse.getReturnStatus())) {
				logInfo("OCI Authentication Successful for " + id);
			}

			return scuiSecurityResponse;

		} catch (Exception e) {
			logError("Problem authenticating OCI user: " + id, e);
			throw new WCException("Problem authenticating specified OCI user", e);
		}
	}

	@Override
	protected WCIntegrationResponse processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			// Need to pass along the URL of the procurement system for later checkout
			// [does hook that includes params pass through ok to this servlet and from here to pLogin.action ?]
			String hookUrl = wcContext.getSCUIContext().getRequest().getParameter("hook_url");
			String encodedHookUrl = URLEncoder.encode(hookUrl, "UTF-8");

			// Create custom URL and redirect to that
			// -> reusing the login action from cXML
			String sfId = wcContext.getStorefrontId();

			if (req.getParameter("data") != null) {
				String encodedData = URLEncoder.encode(req.getParameter("data"),"UTF-8");
				// TODO should we skip ociLogin.action and set the request attributes and display punchoutlogin.jsp directly?
				landingURL = String.format("/swc/punchout/ociLogin.action?sfId=%s&data=%s&returnURL=%s", sfId, encodedData, encodedHookUrl);

			} else {
				// legacy: this will go away in R17
				landingURL = String.format("/swc/common/pLogin.action?u=%s&p=%s&sfId=%s&returnURL=%s", id, pwd, sfId, encodedHookUrl);
			}

			errorCode = new Long(IWCIntegrationStatusCodes.SUCCESS);
			logInfo("OCI login page URL: " + landingURL);
			return new WCIntegrationResponse(WCIntegrationResponse.SUCCESS, new Error(errorCode, errorMessage));

		} catch (Exception e) {
			// not sure if this would happen normally
			String errorMessage = "OCI processRequest failed";
			errorCode = new Long(IWCIntegrationStatusCodes.REQUEST_AUTHENTICATION_FAILED);
			logError("Error Code:" + errorCode + "& ErrorDesc:" + errorMessage + e.getMessage(), e);
			return new WCIntegrationResponse(WCIntegrationResponse.FAILURE, new Error(errorCode, errorMessage));
		}
	}

	// called directly from doPost above, and apparently called by parent on failure
	@Override
	public String getResponseDoc(Error errObj, boolean resStatus, IWCContext ctx) {
		return landingURL;
	}

	private SCUISecurityResponse authenticateUser(HttpServletRequest req, HttpServletResponse res) {
		// is this login best way to authenticate specified user/pass? (cXML does same)
		Document loginDoc = WCIntegrationXMLUtils.prepareLoginInputDoc(id, pwd);
		SCUISecurityResponse scuiSecurityResponse = SCUIPlatformUtils.login(loginDoc, SCUIContextHelper.getUIContext(req, res));
		return scuiSecurityResponse;
	}

	// not sure if/when this gets called by parent (called directly from this doPost)
	@Override
	protected boolean isValidRequest(HttpServletRequest request, HttpServletResponse response) throws WCException{
		return true;
	}

	// not sure if/when this gets called by parent (called directly from this doPost)
	@Override
	public Error getErrorObject(Long errorCode, String errorMsg) {
		return new Error(errorCode, errorMsg);
	}

	public static void logError(String s, Throwable e) {
		log.error("OCI Integration Servlet:" + s, e);
	}

	public static void logError(String s) {
		log.error("OCI Integration Servlet:" + s);
	}

	public static void logInfo(String s) {
		if (log.isInfoEnabled()) {
			log.info("OCI Integration Servlet:" + s);
		}
	}

	public static void logDebug(String s) {
		if (log.isDebugEnabled()) {
			log.debug("OCI Integration Servlet:" + s);
		}
	}

}
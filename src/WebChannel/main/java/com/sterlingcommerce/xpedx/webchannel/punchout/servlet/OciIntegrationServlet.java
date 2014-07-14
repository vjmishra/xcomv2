/*
 * OciIntegrationServlet.java
 *
 * Created on July 20, 2010
 */

package com.sterlingcommerce.xpedx.webchannel.punchout.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;

import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.ui.web.framework.SCUIConstants;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.security.SCUISecurityResponse;
import com.sterlingcommerce.ui.web.framework.utils.SCUIContextHelper;
import com.sterlingcommerce.ui.web.platform.utils.SCUIPlatformUtils;
import com.sterlingcommerce.webchannel.common.eprocurement.AribaContextImpl;
import com.sterlingcommerce.webchannel.common.eprocurement.IAribaContext;
import com.sterlingcommerce.webchannel.common.eprocurement.IProcurementContext;
import com.sterlingcommerce.webchannel.common.integration.Error;
import com.sterlingcommerce.webchannel.common.integration.IAribaConstants;
import com.sterlingcommerce.webchannel.common.integration.IWCIntegrationStatusCodes;
import com.sterlingcommerce.webchannel.common.integration.WCIntegrationResponse;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCException;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.webchannel.servlet.common.IntegrationServlet;
import com.sterlingcommerce.webchannel.utilities.WCIntegrationHelper;
import com.sterlingcommerce.webchannel.utilities.WCIntegrationXMLUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;

/**
*
* @author  adsouza
* @version 1.0
*/

public class OciIntegrationServlet extends IntegrationServlet {
	private static final Logger log = Logger
			.getLogger(OciIntegrationServlet.class);

	WCIntegrationResponse wcResponse = null;
	protected IWCContext wcContext = null;
	WCIntegrationResponse wcIntegrationRes = new WCIntegrationResponse();
	protected Long errorCode = new Long(IWCIntegrationStatusCodes.SUCCESS);
	protected String errorMessage = null;
	String startPageURL = "";
	String startPageEncodedURL = "";

	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		SCUISecurityResponse securityResponse = null;
		securityResponse = authenticateRequest(request, response);

		if (YFCCommon.equals(SCUISecurityResponse.SUCCESS, securityResponse
					.getReturnStatus())) {
			wcContext = WCContextHelper.getWCContext(request);
			wcIntegrationRes = processRequest(request, response);
			if (wcIntegrationRes.getReturnStatus())
					errorMessage = "Message processed successfully";
				else
					errorMessage = "Message processing failed";
			errorCode = wcIntegrationRes.getErrorObj().getErrorCode();
			logError("Error Code:" + errorCode + "& ErrorDesc:" + errorMessage);
			response.sendRedirect(getResponseDoc(wcIntegrationRes.getErrorObj(),
					wcIntegrationRes.getReturnStatus(), wcContext));
			} else {
				errorMessage = "Authentication Error, Login failed";
				errorCode = new Long(
						IWCIntegrationStatusCodes.REQUEST_AUTHENTICATION_FAILED);
				logError("Error Code:" + errorCode + "& ErrorDesc:"
						+ errorMessage);
			}
	}

	protected SCUISecurityResponse authenticateRequest(HttpServletRequest req,
			HttpServletResponse res) throws ServletException, IOException {
		SCUISecurityResponse scuiSecurityResponse =  null;
		try {
			String id = req.getParameter("id");
			String pwd = req.getParameter("pwd");
			Document loginDoc = WCIntegrationXMLUtils.prepareLoginInputDoc(
					id, pwd);			
			scuiSecurityResponse = SCUIPlatformUtils.login(loginDoc,
					SCUIContextHelper.getUIContext(req, res));

			if (scuiSecurityResponse.getReturnStatus())
				logInfo("=====Authentication Successful =====");
			else
				logInfo("=====Authentication Failure =====");
			
			IWCContext context = WCContextHelper
			.getWCContext(req);
			String customerID = context.getCustomerId();
			String storefrontID = context.getStorefrontId();
			String[] CustOCIFields = XPEDXWCUtils.getOCIFieldsForCustomer(req, res, customerID, storefrontID);
			
			String newid = null;
			String newpwd = null;
			String emailTemplate = null;
			if(CustOCIFields[0]!=null && "Y".equals(CustOCIFields[0].trim())){
				newid = req.getParameter(CustOCIFields[1]);
				newpwd = req.getParameter(CustOCIFields[2]);
				emailTemplate = CustOCIFields[3];
				
				loginDoc = WCIntegrationXMLUtils.prepareLoginInputDoc(
						newid+emailTemplate, newpwd);
				
				scuiSecurityResponse = SCUIPlatformUtils.login(loginDoc,
						SCUIContextHelper.getUIContext(req, res));

				if (scuiSecurityResponse.getReturnStatus())
					logInfo("=====Authentication Successful =====");
				else
					logInfo("=====Authentication Failure =====");
				
			}

		} catch (Exception e) {
			logError("Exception during authentication:" + e.getMessage(), e);
			throw new WCException("Exception", e);
		}
		return scuiSecurityResponse;
	}

	protected WCIntegrationResponse processRequest(HttpServletRequest req,
			HttpServletResponse res) throws ServletException, IOException {
		try {
			SCUISecurityResponse scuiSecurityResponse = postAuthenticateUser(req, res);
			if (YFCCommon.equals(SCUISecurityResponse.SUCCESS, scuiSecurityResponse.getReturnStatus())) {
				populateOCIContext(wcContext);
				try {
					CommerceContextHelper.processProcurementPunchIn(wcContext);
				} catch (Exception e) {
					// Send failure response
					errorMessage = "Failed to process the commerce context";
					errorCode = new Long(
							IWCIntegrationStatusCodes.COMMERCE_CONTEXT_FAILED);
					logError("Error Code:" + errorCode + "& ErrorDesc:"
							+ errorMessage + "Exception:" + e.getMessage(),
							e);
					return new WCIntegrationResponse(
							WCIntegrationResponse.FAILURE, new Error(
									errorCode, errorMessage));
				}
				String startPageAction = WCIntegrationHelper
						.checkAndAppendJSessionId(
								IAribaConstants.START_PAGE_URL_DEFAULT_SEARCH,
									wcContext);
				String sfId = wcContext.getStorefrontId();
				startPageURL = startPageAction + "?sfId=" + sfId;
				startPageEncodedURL = WCIntegrationHelper
						.constructStartPageURL(startPageURL, wcContext);

				if (!YFCCommon.isVoid(startPageURL)) {
					errorMessage = "=====Request for Auth setup is success=====.";
					errorCode = new Long(IWCIntegrationStatusCodes.SUCCESS);
					logInfo("=====Start page URL:"
							+ startPageURL);
					wcResponse = new WCIntegrationResponse(
							WCIntegrationResponse.SUCCESS, new Error(
									errorCode, errorMessage));
				} else {
					// Send failure response
					errorMessage = "cannot form start page url:";
					errorCode = new Long(
							IWCIntegrationStatusCodes.COMMERCE_CONTEXT_FAILED);
					logError("Error Code:" + errorCode + "& ErrorDesc:"
							+ errorMessage);
					return new WCIntegrationResponse(
							WCIntegrationResponse.FAILURE, new Error(
									errorCode, errorMessage));
				}

			} else {
				// Send failure response
				errorMessage = "Post Authentication failed";
				errorCode = new Long(
						IWCIntegrationStatusCodes.REQUEST_AUTHENTICATION_FAILED);
				logError("Error Code:" + errorCode + "& ErrorDesc:"
						+ errorMessage);
				return new WCIntegrationResponse(WCIntegrationResponse.FAILURE,
						new Error(errorCode, errorMessage));
			}
		} catch (Exception e) {
			// Send failure response
			errorMessage = "Post Authentication failed";
			errorCode = new Long(
					IWCIntegrationStatusCodes.REQUEST_AUTHENTICATION_FAILED);
			logError("Error Code:" + errorCode + "& ErrorDesc:" + errorMessage
					+ e.getMessage(), e);
			return new WCIntegrationResponse(WCIntegrationResponse.FAILURE,
					new Error(errorCode, errorMessage));
		}
		logInfo("=====Post Authentication process End =====");
		return wcResponse;

	}

	public String getResponseDoc(Error errObj, boolean resStatus, IWCContext ctx) {

		if (resStatus)
			wcResponse = new WCIntegrationResponse(
					WCIntegrationResponse.SUCCESS, errObj);
		else
			wcResponse = new WCIntegrationResponse(
					WCIntegrationResponse.FAILURE, errObj);
		return startPageEncodedURL;
	}

	private void populateOCIContext(IWCContext wcContext) {
		String hook_url = wcContext.getSCUIContext().getRequest().getParameter("hook_url");
		OciContextImpl ociContext = OciContextImpl.getInstance();
		ociContext.setOciOperation("CREATE");
		ociContext.setOrderHeaderKey(null);
		ociContext.setReturnURL(hook_url);
		ociContext.setBuyerCookie(null);
		wcContext.setWCAttribute(
				IProcurementContext.PROCUREMENT_CONTEXT_ATTRIBUTE_KEY,
				(IOciContext) ociContext, WCAttributeScope.SESSION);
	}

    protected boolean isValidRequest(HttpServletRequest request, HttpServletResponse response)
    throws WCException{

    	boolean isValid = true;
    	return isValid;
 	}

	/**
	 * logError
	 *
	 * @param String
	 *            value
	 */
	public static void logError(String s, Throwable e) {

		log.error("OCI Integration Servlet:" + s, e);
	}

	public static void logError(String s) {

		log.error("OCI Integration Servlet:" + s);
	}

	/**
	 * logInfo
	 *
	 * @param String
	 *            value
	 */
	public static void logInfo(String s) {
		log.info("OCI Integration Servlet:" + s);
	}

	/**
	 * logVerbose
	 *
	 * @param String
	 *            value
	 */
	public static void logDebug(String s) {
		log.debug("OCI Integration Servlet:" + s);
	}

	public Error getErrorObject(Long errorCode, String errorMsg) {
		return new Error(errorCode, errorMsg);
	}

}
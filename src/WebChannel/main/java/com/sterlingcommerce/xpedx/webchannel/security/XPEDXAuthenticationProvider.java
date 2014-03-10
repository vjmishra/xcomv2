package com.sterlingcommerce.xpedx.webchannel.security;

import javax.servlet.http.HttpServletRequest;

import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.security.SCUISecurityResponse;
import com.sterlingcommerce.ui.web.framework.utils.SCUIUtils;
import com.sterlingcommerce.webchannel.core.wcaas.WCAuthenticationProvider;
import com.sterlingcommerce.webchannel.utilities.WCConstants;
import com.sterlingcommerce.xpedx.webchannel.punchout.PunchoutRequest;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXAuthenticationProvider extends WCAuthenticationProvider {
	@Override
	public SCUISecurityResponse authenticate(SCUIContext scuiCtx) {
		SCUISecurityResponse response =  super.authenticate(scuiCtx);
		setRequiredAttrbutes(scuiCtx.getRequest());
		String url = scuiCtx.getRequest().getRequestURL().toString();
		String loginPageSR = (String)scuiCtx.getRequest().getAttribute("SALES_REP_LOGIN_PAGE");
		loginPageSR = scuiCtx.getRequest().getParameter("SALES_REP_LOGIN_PAGE");
    	if (SCUIUtils.isTrue(loginPageSR)){
    		String redirectParamName = scuiCtx.getServletContext().getInitParameter(WCConstants.URL_REDIRECT_PARAM_NAME);
    		String salesRepLoginPage = scuiCtx.getServletContext().getInitParameter("xpedx_sales_rep_login_url");
    		if (!YFCCommon.isVoid(redirectParamName) && !YFCCommon.isVoid(salesRepLoginPage)){
    	        if ((response != null) && (!response.getReturnStatus())) {
    	        		response.setForwardURL(salesRepLoginPage);
    	        		if (!YFCCommon.isVoid(response.getErrorMessage())){
    	        			scuiCtx.getSession(false).setAttribute("ERROR_MESSAGE", response.getErrorMessage());
    	        		}
    	        }
    	        else{
   	        		scuiCtx.getSession(false).setAttribute("ERROR_MESSAGE", "");
  	  	        }
    		}
    	}
		String aribaFlag = scuiCtx.getRequest().getParameter("amiProcurementUser");

		if (aribaFlag!= null && aribaFlag.equals("Y")) {
			scuiCtx.getSession(false).setAttribute("aribaFlag", scuiCtx.getRequest().getParameter("amiProcurementUser"));
			scuiCtx.getSession(false).setAttribute("EnterpriseCode", scuiCtx.getRequest().getParameter("EnterpriseCode"));

			PunchoutRequest punchoutRequest = new PunchoutRequest();
			punchoutRequest.setCXML(isCXML(scuiCtx));
			punchoutRequest.setLoginID(scuiCtx.getRequest().getParameter("DisplayUserID"));
			punchoutRequest.setSfId(scuiCtx.getRequest().getParameter("EnterpriseCode"));
			punchoutRequest.setPayLoadID(scuiCtx.getRequest().getParameter("payLoadID"));
			punchoutRequest.setOperation(scuiCtx.getRequest().getParameter("operation"));
			punchoutRequest.setOrderHeaderKey(scuiCtx.getRequest().getParameter("orderHeaderKey"));
			punchoutRequest.setReturnURL(scuiCtx.getRequest().getParameter("returnURL"));
			punchoutRequest.setSelectedCategory(scuiCtx.getRequest().getParameter("selectedCategory"));
			punchoutRequest.setSelectedItem(scuiCtx.getRequest().getParameter("selectedItem"));
			punchoutRequest.setSelectedItemUOM(scuiCtx.getRequest().getParameter("selectedItemUOM"));
			punchoutRequest.setBuyerCookie(scuiCtx.getRequest().getParameter("buyerCookie"));
			punchoutRequest.setFromIdentity(scuiCtx.getRequest().getParameter("fromIdentity"));
			punchoutRequest.setToIdentity(scuiCtx.getRequest().getParameter("toIdentity"));
			punchoutRequest.setIsProcurementUser(scuiCtx.getRequest().getParameter("amiProcurementUser"));

			scuiCtx.getSession().setAttribute("PunchoutRequest", punchoutRequest);
			}
		return response;
	}

	private boolean isCXML(SCUIContext scuiCtx) {
		String identity = scuiCtx.getRequest().getParameter("toIdentity");
		return identity != null && !identity.isEmpty();
	}

	private void setRequiredAttrbutes(HttpServletRequest request){
		String sessionUserName = (String)request.getSession(false).getAttribute("loggedInUserName");

		String sessionUserId = (String)request.getSession(false).getAttribute("loggedInUserId");
		String sessionSREmailID = (String)request.getSession(false).getAttribute("SRSalesRepEmailID");
		String requestUserName = (String)request.getAttribute("loggedInUserName");
		String requestUserId = (String)request.getAttribute("loggedInUserId");
		String requestSREmailID = (String)request.getAttribute("SRSalesRepEmailID");
		if (requestUserId == null){
			requestUserName = request.getParameter("loggedInUserName");
			//request.setAttribute("DisplayUserID",(String)request.getParameter("DisplayUserID"));
			 requestUserId = request.getParameter("loggedInUserId");
			 requestSREmailID = request.getParameter("SRSalesRepEmailID");
		}
		if (requestUserId != null){
			request.getSession(false).setAttribute("IS_SALES_REP", "true");
			request.getSession(false).setAttribute("loggedInUserId",requestUserId);
			request.getSession(false).setAttribute("loggedInUserName",requestUserName);
			request.getSession(false).setAttribute("SRSalesRepEmailID",requestSREmailID);
		}
		else if (sessionUserId != null){
			request.setAttribute("IS_SALES_REP", "true");
			request.setAttribute("loggedInUserId",sessionUserId);
			request.setAttribute("loggedInUserName",sessionUserName);
			request.setAttribute("SRSalesRepEmailID",sessionSREmailID);
		}
	}
}

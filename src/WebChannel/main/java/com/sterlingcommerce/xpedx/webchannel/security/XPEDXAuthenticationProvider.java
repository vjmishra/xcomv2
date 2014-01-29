package com.sterlingcommerce.xpedx.webchannel.security;

import javax.servlet.http.HttpServletRequest;

import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.security.SCUISecurityResponse;
import com.sterlingcommerce.ui.web.framework.utils.SCUIUtils;
import com.sterlingcommerce.webchannel.core.wcaas.WCAuthenticationProvider;
import com.sterlingcommerce.webchannel.utilities.WCConstants;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXAuthenticationProvider extends WCAuthenticationProvider {
	public SCUISecurityResponse authenticate(SCUIContext scuiCtx) {
		SCUISecurityResponse response =  super.authenticate(scuiCtx);
		setRequiredAttrbutes(scuiCtx.getRequest());
		String url = scuiCtx.getRequest().getRequestURL().toString();
		String loginPageSR = (String)scuiCtx.getRequest().getAttribute("SALES_REP_LOGIN_PAGE");
		loginPageSR = (String)scuiCtx.getRequest().getParameter("SALES_REP_LOGIN_PAGE");
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
		String aribaFlag = (String) scuiCtx.getRequest().getParameter("amiProcurementUser");
		
		if (aribaFlag!= null && aribaFlag.equals("Y")) {
			scuiCtx.getSession(false).setAttribute("payLoadID", scuiCtx.getRequest().getParameter("payLoadID"));
			scuiCtx.getSession(false).setAttribute("operation", scuiCtx.getRequest().getParameter("operation"));
			scuiCtx.getSession(false).setAttribute("orderHeaderKey", scuiCtx.getRequest().getParameter("orderHeaderKey"));
			scuiCtx.getSession(false).setAttribute("returnURL", scuiCtx.getRequest().getParameter("returnURL"));
			scuiCtx.getSession(false).setAttribute("selectedCategory", scuiCtx.getRequest().getParameter("selectedCategory"));
			scuiCtx.getSession(false).setAttribute("selectedItem", scuiCtx.getRequest().getParameter("selectedItem"));
			scuiCtx.getSession(false).setAttribute("selectedItemUOM", scuiCtx.getRequest().getParameter("selectedItemUOM"));
			scuiCtx.getSession(false).setAttribute("buyerCookie", scuiCtx.getRequest().getParameter("buyerCookie"));
			scuiCtx.getSession(false).setAttribute("fromIdentity", scuiCtx.getRequest().getParameter("fromIdentity"));
			scuiCtx.getSession(false).setAttribute("toIdentity", scuiCtx.getRequest().getParameter("toIdentity"));
			scuiCtx.getSession(false).setAttribute("preferredShipTo", scuiCtx.getRequest().getParameter("preferredShipTo"));
			scuiCtx.getSession(false).setAttribute("aribaFlag", scuiCtx.getRequest().getParameter("amiProcurementUser"));
			scuiCtx.getSession(false).setAttribute("EnterpriseCode", scuiCtx.getRequest().getParameter("EnterpriseCode"));
			
			
		}
		return response;
	}
	
	private void setRequiredAttrbutes(HttpServletRequest request){
		String sessionUserName = (String)request.getSession(false).getAttribute("loggedInUserName");
		
		String sessionUserId = (String)request.getSession(false).getAttribute("loggedInUserId");
		String sessionSREmailID = (String)request.getSession(false).getAttribute("SRSalesRepEmailID");
		String requestUserName = (String)request.getAttribute("loggedInUserName");
		String requestUserId = (String)request.getAttribute("loggedInUserId");
		String requestSREmailID = (String)request.getAttribute("SRSalesRepEmailID");
		if (requestUserId == null){
			requestUserName = (String)request.getParameter("loggedInUserName");
			//request.setAttribute("DisplayUserID",(String)request.getParameter("DisplayUserID"));
			 requestUserId = (String)request.getParameter("loggedInUserId");
			 requestSREmailID = (String)request.getParameter("SRSalesRepEmailID");
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

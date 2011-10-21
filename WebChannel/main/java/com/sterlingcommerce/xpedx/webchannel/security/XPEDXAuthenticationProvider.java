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
		return response;
	}
	
	private void setRequiredAttrbutes(HttpServletRequest request){
		String sessionUserName = (String)request.getSession(false).getAttribute("loggedInUserName");
		
		String sessionUserId = (String)request.getSession(false).getAttribute("loggedInUserId");
		String requestUserName = (String)request.getAttribute("loggedInUserName");
		String requestUserId = (String)request.getAttribute("loggedInUserId");
		
		if (requestUserId == null){
			requestUserName = (String)request.getParameter("loggedInUserName");
			//request.setAttribute("DisplayUserID",(String)request.getParameter("DisplayUserID"));
			 requestUserId = (String)request.getParameter("loggedInUserId");		
		}		
		if (requestUserId != null){
			request.getSession(false).setAttribute("IS_SALES_REP", "true");
			request.getSession(false).setAttribute("loggedInUserId",requestUserId);
			request.getSession(false).setAttribute("loggedInUserName",requestUserName);			
		}
		else if (sessionUserId != null){
			request.setAttribute("IS_SALES_REP", "true");
			request.setAttribute("loggedInUserId",sessionUserId);
			request.setAttribute("loggedInUserName",sessionUserName);				
		}
	}
}

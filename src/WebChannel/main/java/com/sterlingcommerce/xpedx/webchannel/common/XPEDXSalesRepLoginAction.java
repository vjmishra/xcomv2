package com.sterlingcommerce.xpedx.webchannel.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.sterlingcommerce.webchannel.core.WCAction;

public class XPEDXSalesRepLoginAction extends WCAction implements ServletResponseAware {

	private static final long serialVersionUID = -7609560200703916048L;

	private static final Logger LOG = Logger.getLogger(XPEDXSalesRepLoginAction.class);

	private String logoutMethod = null;
	private HttpServletResponse response = null;

	public String logoutSalesRep() {
		if(LOG.isDebugEnabled()){
			LOG.debug(":: Entering logoutSalesRep() ");
			LOG.debug(":: User is currently logged out...");
		}
		if (request == null || response == null){
			if ("PAGE".equalsIgnoreCase(getLogoutMethod())){
				return WCAction.SUCCESS;
			}
			else {
				return WCAction.NONE;
			}
		}
		Cookie cookies [] = request.getCookies();
		if (cookies != null)
		{
			for (int i = 0; i < cookies.length; i++)
			{
				cookies[i].setMaxAge(0);
				cookies[i].setPath("/");
				response.addCookie(cookies[i]);
			}
		}
		request.getSession(false).invalidate();
		if ("PAGE".equalsIgnoreCase(getLogoutMethod())){
			return WCAction.SUCCESS;
		}
		else {
			return WCAction.NONE;
		}

	}

	public String getLogoutMethod() {
		return logoutMethod;
	}

	public void setLogoutMethod(String logoutMethod) {
		this.logoutMethod = logoutMethod;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}


}

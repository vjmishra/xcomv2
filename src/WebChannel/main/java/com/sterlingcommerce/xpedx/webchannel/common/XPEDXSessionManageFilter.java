package com.sterlingcommerce.xpedx.webchannel.common;


import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @author Nimesh K Bhate
 * To redirect user after session timeout to login page.
 */
public class XPEDXSessionManageFilter implements Filter {
	private String[] excludeActions;// --Actions to Exclude -- Read from Web.xml
	private StringBuffer redirectURL;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		this.excludeActions = config.getInitParameter("excludeActions").split(",");
		//forming the redirect URL - /swc/home/home.action?sfId=xpedx
		redirectURL=new StringBuffer();
		redirectURL.append(config.getServletContext().getContextPath());
		redirectURL.append(config.getServletContext().getInitParameter("wc-postlogin-account-landingpage"));
		redirectURL.append("?sfId=");
		redirectURL.append(config.getServletContext().getInitParameter("defaultSfId"));
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
			HttpServletRequest req = (HttpServletRequest)request;
			HttpServletResponse resp=(HttpServletResponse)response;
		
			 Enumeration enumeration = request.getParameterNames(); 
			 while (enumeration.hasMoreElements()) { 
				 String parameterName = (String) enumeration.nextElement(); 
				 if (parameterName.contains("scGuestUser")) { 
					 chain.doFilter(request, response);
					    return;
				 }
			}

				for(int i=0;i<this.excludeActions.length;i++){
				if (req.getRequestURL().toString().contains(excludeActions[i])) {
				    chain.doFilter(request, response);
				    return;
				}
				}
				if(req.getSession().getAttribute("loggedInFormattedCustomerIDMap") != null)
				{
					chain.doFilter(request, response);
				    return;
				}
				 if(req.getSession().getAttribute("loggedInCustomerID") == null){
					 resp.sendRedirect(redirectURL.toString());
					 return;
				    } 
			
		chain.doFilter(request, response);
	}
	
	public void destroy() {

	}

}

package com.sterlingcommerce.xpedx.webchannel.common;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Nimesh K Bhate
 * To redirect user after session timeout to login page.
 */
public class SessionManageFilter implements Filter {
	private String[] excludeActions;// --Actions to Exclude -- Read from Web.xml

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		this.excludeActions = config.getInitParameter("excludeActions").split(",");
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
			HttpServletRequest req = (HttpServletRequest)request;
			HttpServletResponse resp=(HttpServletResponse)response;
				String url = req.getRequestURL().toString();
				for(int i=0;i<this.excludeActions.length;i++){
				if (url.contains(excludeActions[i])) {
				    chain.doFilter(request, response);
				    return;
				}
				}
				
				 if(req.getSession().getAttribute("loggedInCustomerID") == null){
					 resp.sendRedirect("/swc/home/home.action");
					 return;
				    } 
			
		chain.doFilter(request, response);
	}
	
	public void destroy() {

	}

}

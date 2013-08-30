package com.sterlingcommerce.xpedx.webchannel.common;


import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @author Nimesh K Bhate
 * To redirect user after session timeout to login page.
 */
public class XPEDXSessionManageFilter implements Filter {
	private static final String SFID_REQUEST_PARAM = "sfId";
	
	
	private String[] excludeActions;// --Actions to Exclude -- Read from Web.xml
	private String timeoutUrlPattern; // use String.format(timeoutUrlPattern, sfId)
	private String defaultSfId;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		this.excludeActions = config.getInitParameter("excludeActions").split(",");
		//forming the redirect URL - /swc/home/home.action?sfId=xpedx
		StringBuilder redirectURL=new StringBuilder(128);
		redirectURL.append(config.getServletContext().getContextPath());
		redirectURL.append(config.getServletContext().getInitParameter("wc-postlogin-account-landingpage"));
		redirectURL.append("?sfId=%s");
		//Added for EB 560 - on session expire return to login page with an error msg
		redirectURL.append("&error=sessionExpired");
		
		timeoutUrlPattern = redirectURL.toString();
		
		defaultSfId = config.getServletContext().getInitParameter("defaultSfId");
	}

	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		initStorefrontCookie(req, resp);

		Enumeration enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String parameterName = (String) enumeration.nextElement();
			if (parameterName.contains("scGuestUser")) {
				chain.doFilter(request, response);
				return;
			}
		}
		
		for (int i = 0; i < this.excludeActions.length; i++) {
			if (req.getRequestURL().toString().contains(excludeActions[i])) {
				chain.doFilter(request, response);
				return;
			}
		}
		if (req.getSession().getAttribute("loggedInFormattedCustomerIDMap") != null) {
			chain.doFilter(request, response);
			return;
		}
		if (req.getSession().getAttribute("loggedInCustomerID") == null) {
			resp.sendRedirect(String.format(timeoutUrlPattern, getCurrentStorefrontId(req)));
			return;
		}

		chain.doFilter(request, response);
	}
	
	public void destroy() {

	}
	
	/**
	 * If the request parameter contains the storefront id, creates or updates the storefront cookie. If not present, does nothing.
	 * @param req
	 * @param resp
	 */
	private void initStorefrontCookie(HttpServletRequest req, HttpServletResponse resp) {
		String sfId = req.getParameter(SFID_REQUEST_PARAM);
		if (sfId != null) {
			// create or update cookie with this storefront id
			Cookie cookie = CookieUtil.getCookie(req, CookieUtil.STOREFRONT_ID);
			if (cookie == null) {
				cookie = new Cookie(CookieUtil.STOREFRONT_ID, sfId);
				cookie.setMaxAge(-1); // until user closes browser
			} else {
				cookie.setValue(sfId);
			}
			resp.addCookie(cookie);
		}
	}
	
	/**
	 * Determines the storefront id using the following order of precedence:
	 * <ol>
	 * <li>Request parameter present</li>
	 * <li>Cookie present</li>
	 * <li>Otherwise defaults to <code>defaultSfId</code></li>
	 * <li></li>
	 * </ol>
	 * @param req
	 * @return Returns the current storefront id
	 */
	private String getCurrentStorefrontId(HttpServletRequest req) {
		String sfId = req.getParameter(SFID_REQUEST_PARAM);
		if (sfId == null) {
			Cookie cookie = CookieUtil.getCookie(req, CookieUtil.STOREFRONT_ID);
			if (cookie == null) {
				return defaultSfId;
			} else {
				sfId = cookie.getValue();
			}
		}
		return sfId;
	}

}

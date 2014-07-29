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
	private String salesrepTimeoutUrlPattern; // use String.format(timeoutUrlPattern, sfId)
	private String punchoutTimeoutUrlPattern; // use String.format(timeoutUrlPattern, sfId)
	private String defaultSfId;

	/*
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		this.excludeActions = config.getInitParameter("excludeActions").split(",");

		// forming the redirect URL - /swc/home/home.action?sfId=xpedx
		StringBuilder timeoutUrl = new StringBuilder(128);
		timeoutUrl.append(config.getServletContext().getContextPath());
		timeoutUrl.append(config.getServletContext().getInitParameter("wc-postlogin-account-landingpage"));
		timeoutUrl.append("?sfId=%s&error=sessionExpired");

		timeoutUrlPattern = timeoutUrl.toString();

		defaultSfId = config.getServletContext().getInitParameter("defaultSfId");

		StringBuilder salesrepTimeoutUrl = new StringBuilder(128);
		salesrepTimeoutUrl.append(config.getServletContext().getContextPath());
		salesrepTimeoutUrl.append(config.getServletContext().getInitParameter("xpedx_sales_rep_login_url"));
		salesrepTimeoutUrl.append("?sfId=%s&error=sessionExpired");
		salesrepTimeoutUrlPattern = salesrepTimeoutUrl.toString();

		StringBuilder punchoutTimeoutUrl = new StringBuilder(128);
		punchoutTimeoutUrl.append(config.getServletContext().getContextPath());
		punchoutTimeoutUrl.append(config.getServletContext().getInitParameter("xpedx_punchout_timeout_url"));
		punchoutTimeoutUrl.append("?sfId=%s");
		punchoutTimeoutUrlPattern = punchoutTimeoutUrl.toString();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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
			boolean isSalesrepUser = isSalesrepUser(req);
			boolean isPunchoutUser = isPunchoutUser(req);
			cleanupCookies(req, resp);
			if (isSalesrepUser) {
				resp.sendRedirect(String.format(salesrepTimeoutUrlPattern, getCurrentStorefrontId(req)));
			} else if (isPunchoutUser) {
				resp.sendRedirect(String.format(punchoutTimeoutUrlPattern, getCurrentStorefrontId(req)));
			} else {
				resp.sendRedirect(String.format(timeoutUrlPattern, getCurrentStorefrontId(req)));
			}
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	private void cleanupCookies(HttpServletRequest req, HttpServletResponse resp) {
		// delete user-specific cookies (NOT CookieUtil.STOREFRONT_ID)
		CookieUtil.deleteCookie(req, resp, CookieUtil.SALESREP);
		CookieUtil.deleteCookie(req, resp, CookieUtil.PUNCHOUT);
	}

	private void initStorefrontCookie(HttpServletRequest req, HttpServletResponse resp) {
		String sfId = req.getParameter(SFID_REQUEST_PARAM);
		if (sfId != null) {
			CookieUtil.setCookie(req, resp, CookieUtil.STOREFRONT_ID, sfId);
		}
	}

	private String getCurrentStorefrontId(HttpServletRequest req) {
		// first check query string
		String sfId = req.getParameter(SFID_REQUEST_PARAM);
		if (sfId == null) {
			// next, check cookie
			Cookie cookie = CookieUtil.getCookie(req, CookieUtil.STOREFRONT_ID);
			if (cookie == null) {
				// if all else fails, use the default
				sfId = defaultSfId;
			} else {
				sfId = cookie.getValue();
			}
		}
		return sfId;
	}

	private boolean isSalesrepUser(HttpServletRequest req) {
		Cookie cookie = CookieUtil.getCookie(req, CookieUtil.SALESREP);
		return cookie != null && "true".equals(cookie.getValue());
	}

	private boolean isPunchoutUser(HttpServletRequest req) {
		Cookie cookie = CookieUtil.getCookie(req, CookieUtil.PUNCHOUT);
		return cookie != null && "true".equals(cookie.getValue());
	}

}

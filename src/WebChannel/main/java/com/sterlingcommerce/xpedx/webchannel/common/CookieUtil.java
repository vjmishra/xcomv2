package com.sterlingcommerce.xpedx.webchannel.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.yantra.yfc.log.YFCLogCategory;

/**
 * Helper classes for http cookies.
 * 
 * @author Trey Howard
 */
public class CookieUtil {

	private static YFCLogCategory log = YFCLogCategory.instance(CookieUtil.class);
	
	public static final String STOREFRONT_ID = "StorefrontId";
	public static final String SALESREP="salesrep";
	

	/**
	 * @param req
	 * @return Returns the specified cookie, or null if not present.
	 */
	public static Cookie getCookie(HttpServletRequest req, String cookieId) {
		if (req == null || cookieId == null) {
			log.warn("req or cookie is null: req=" + req + ", cookieId=" + cookieId);
			return null;
		}
		
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookieId.equals(cookie.getName())) {
					return cookie;
				}
				
			}
		}
		return null;
	}

}

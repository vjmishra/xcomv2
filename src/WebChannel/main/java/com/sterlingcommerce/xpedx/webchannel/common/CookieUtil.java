package com.sterlingcommerce.xpedx.webchannel.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yantra.yfc.log.YFCLogCategory;

/**
 * Helper classes for http cookies. All cookies use path=/swc and maxAge=-1 (expire when the user closes their browser).
 */
public class CookieUtil {

	private static YFCLogCategory log = YFCLogCategory.instance(CookieUtil.class);

	public static final String STOREFRONT_ID = "sfId";
	public static final String SALESREP = "salesrep";
	public static final String PUNCHOUT = "punchout";

	private static final String PATH_ROOT = "/swc";
	private static final int AGE_FOREVER = -1;
	private static final int AGE_DELETE = 0;

	/**
	 * @param req
	 * @param cookieId
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

	/**
	 * Updates the specified cookie, creating a new cookie if necessary.
	 * @param req
	 * @param resp
	 * @param cookieId
	 * @param value
	 */
	public static void setCookie(HttpServletRequest req, HttpServletResponse resp, String cookieId, String value) {
		if (req == null || resp == null || cookieId == null || value == null) {
			log.warn(String.format("One or more parameters is null: req=%s, resp=%s, cookieId=%s, value=%s", req, resp, cookieId, value));
			return;
		}

		Cookie cookie = CookieUtil.getCookie(req, cookieId);
		if (cookie == null) {
			cookie = new Cookie(cookieId, value);
			cookie.setMaxAge(AGE_FOREVER);
			cookie.setPath(PATH_ROOT);
		} else {
			cookie.setValue(value);
			cookie.setPath(PATH_ROOT);
		}
		resp.addCookie(cookie);
	}

	/**
	 * Deletes the specified cookie (sets max age to 0)
	 * @param req
	 * @param resp
	 * @param cookieId
	 */
	public static void deleteCookie(HttpServletRequest req, HttpServletResponse resp, String cookieId) {
		Cookie cookie = getCookie(req, cookieId);
		if (cookie != null) {
			cookie.setMaxAge(AGE_DELETE);
			cookie.setPath(PATH_ROOT);
			cookie.setValue("");
			resp.addCookie(cookie);
		}
	}

}

package com.sterlingcommerce.xpedx.webchannel.common;

import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.servlet.eprocurement.XPEDXPunchoutServlet;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

/**
 * After the CXML authentication handshake (see identity/secret in XPEDXPunchoutServlet), the user is returned a url that points to this action.
 * This action uses the seesionId parameter to fetch the CXML settings (user, password, etc).
 * @see XPEDXPunchoutServlet
 */
public class PunchoutCxmlLoginAction extends WCAction {

	private String sfId;
	private String sessionId;

	public void setSfId(String sfId) {
		this.sfId = sfId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String execute() {
		Element punchoutCxmlSessionElem = getPunchoutCxmlSession(sessionId);
		if (punchoutCxmlSessionElem == null) {
			// the cxml session has timed out. the session is only valid for a few minutes after creation
			return ERROR;
		}

		// TODO decrypt password
		request.setAttribute("selected_storefrontId", sfId);
		request.setAttribute("dum_username", punchoutCxmlSessionElem.getAttribute("Userid"));
		request.setAttribute("dum_password", punchoutCxmlSessionElem.getAttribute("Passwd"));
		request.setAttribute("payLoadID", punchoutCxmlSessionElem.getAttribute("PayloadId"));
		request.setAttribute("buyerCookie", punchoutCxmlSessionElem.getAttribute("BuyerCookie"));
		request.setAttribute("fromIdentity", punchoutCxmlSessionElem.getAttribute("FromIdentity"));
		request.setAttribute("toIdentity", punchoutCxmlSessionElem.getAttribute("ToIdentity"));
		request.setAttribute("returnURL", punchoutCxmlSessionElem.getAttribute("ReturnUrl"));

		// these fields are static
		request.setAttribute("isProcurementUser", "Y");
		request.setAttribute("operation", null);
		request.setAttribute("orderHeaderKey", null);
		request.setAttribute("selectedCategory", null);
		request.setAttribute("selectedItem", null);
		request.setAttribute("selectedItemUOM", null);

		// TODO this feels awkward: setting request attributes to populate a form that is submitted on document ready. is there a better way?
		//		look into action chaining: http://struts.apache.org/release/2.1.x/docs/action-chaining.html (chaining is discouraged)
		//		or better yet, share code with LoginAction via a helper class
		return SUCCESS;
	}

	private static Element getPunchoutCxmlSession(String sessionId) {
		Element punchoutCxmlSessionElem = SCXmlUtil.createDocument("XPEDXPunchoutCxmlSession").getDocumentElement();
		Element complexQueryInputElem = SCXmlUtil.createChild(punchoutCxmlSessionElem, "ComplexQuery");
		Element andInputElem = SCXmlUtil.createChild(complexQueryInputElem, "And");

		Element expTimestampInputElemElem = SCXmlUtil.createChild(andInputElem, "Exp");
		expTimestampInputElemElem.setAttribute("Name", "PunchoutCxmlSessionId");
		expTimestampInputElemElem.setAttribute("Value", sessionId);
		expTimestampInputElemElem.setAttribute("QryType", "EQ");

		// TODO do not provide CreateTs here, instead rely on external process to purge expired sessions. avoids timezone issues
//		Element expSessionIdInputElem = SCXmlUtil.createChild(andInputElem, "Exp");
//		expSessionIdInputElem.setAttribute("Name", "CreateTs");
//		expSessionIdInputElem.setAttribute("Value", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:Z").format(new Date()));
//		expSessionIdInputElem.setAttribute("QryType", "GE");

		Element punchoutCxmlSessionListOutputElem = XPEDXWCUtils.handleApiRequestBeforeAuthentication("getPunchoutCxmlSessionList", punchoutCxmlSessionElem.getOwnerDocument(), true, null).getDocumentElement();

		return SCXmlUtil.getChildElement(punchoutCxmlSessionListOutputElem, "XPEDXPunchoutCxmlSession");
	}

}

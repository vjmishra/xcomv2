package com.sterlingcommerce.xpedx.webchannel.punchout;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.crypto.EncryptionUtils;
import com.sterlingcommerce.xpedx.webchannel.servlet.eprocurement.XPEDXPunchoutCxmlServlet;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

/**
 * After the CXML authentication handshake (see identity/secret in XPEDXPunchoutServlet), the user is returned a url that points to this action.
 * This action uses the seesionId parameter to fetch the CXML settings (user, password, etc).
 * @see XPEDXPunchoutCxmlServlet
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

		// Password should now be encrypted in DB so unencrypt here
		String encrypted = punchoutCxmlSessionElem.getAttribute("Passwd");
		String passwd = encrypted;
		try {
			passwd = EncryptionUtils.decrypt(encrypted);
		}
		catch (Exception e) {
			log.error("problem while decrypting password for cXML login for user: " + punchoutCxmlSessionElem.getAttribute("Userid"), e);
		}

		request.setAttribute("selected_storefrontId", sfId);
		request.setAttribute("dum_username", punchoutCxmlSessionElem.getAttribute("Userid"));
		request.setAttribute("dum_password", passwd);
		request.setAttribute("payLoadID", punchoutCxmlSessionElem.getAttribute("PayloadId"));
		request.setAttribute("buyerCookie", punchoutCxmlSessionElem.getAttribute("BuyerCookie"));
		request.setAttribute("fromIdentity", punchoutCxmlSessionElem.getAttribute("FromIdentity"));
		request.setAttribute("toIdentity", punchoutCxmlSessionElem.getAttribute("ToIdentity"));
		request.setAttribute("returnURL", punchoutCxmlSessionElem.getAttribute("ReturnUrl"));

		// these fields are static for cxml
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
		punchoutCxmlSessionElem.setAttribute("PunchoutCxmlSessionId", sessionId);

		Element punchoutCxmlSessionListOutputElem = XPEDXWCUtils.handleApiRequestBeforeAuthentication("getPunchoutCxmlSessionList", punchoutCxmlSessionElem.getOwnerDocument(), true, null).getDocumentElement();

		return SCXmlUtil.getChildElement(punchoutCxmlSessionListOutputElem, "XPEDXPunchoutCxmlSession");
	}

	private static final Logger log = Logger.getLogger(PunchoutCxmlLoginAction.class);
}

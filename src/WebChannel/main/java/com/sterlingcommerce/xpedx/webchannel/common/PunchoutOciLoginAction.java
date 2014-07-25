package com.sterlingcommerce.xpedx.webchannel.common;

import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.common.punchout.PunchoutOciUtil;
import com.sterlingcommerce.xpedx.webchannel.common.punchout.PunchoutOciUtil.OciCredentials;

/**
 * XPEDXOciServlet redirects here with the encrypted data parameter (contains userId and password).
 * This action decrypts the value.
 */
public class PunchoutOciLoginAction extends WCAction {

	private String sfId;
	private String data;
	private String returnURL;

	@Override
	public String execute() {
		OciCredentials cred = PunchoutOciUtil.decryptData(data);

		request.setAttribute("dum_username", cred.getUserId());
		request.setAttribute("dum_password", cred.getPassword());
		request.setAttribute("selected_storefrontId", sfId);
		request.setAttribute("returnURL", returnURL);

		// these fields are static for oci
		request.setAttribute("isProcurementUser", "Y");
		request.setAttribute("payLoadID", null);
		request.setAttribute("buyerCookie", null);
		request.setAttribute("fromIdentity", null);
		request.setAttribute("toIdentity", null);
		request.setAttribute("operation", null);
		request.setAttribute("orderHeaderKey", null);
		request.setAttribute("selectedCategory", null);
		request.setAttribute("selectedItem", null);
		request.setAttribute("selectedItemUOM", null);

		return WCAction.SUCCESS;
	}

	// --- Inputs and Outputs

	public void setSfId(String sfId) {
		this.sfId = sfId;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

}

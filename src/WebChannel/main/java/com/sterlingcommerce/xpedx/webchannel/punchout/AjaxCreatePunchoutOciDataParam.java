package com.sterlingcommerce.xpedx.webchannel.punchout;

import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils;
import com.sterlingcommerce.xpedx.webchannel.punchout.util.PunchoutOciUtil;
import com.sterlingcommerce.xpedx.webchannel.punchout.util.PunchoutOciUtil.OciCredentials;

/*
 * Created on Oct 21, 2013
 */

/**
 * Encrypt the given userId and password to create the special 'data' parameter for OCI logins (OCIPost.jsp?...&data=3ncrypt3d)
 */
public class AjaxCreatePunchoutOciDataParam extends WCAction {

	// inputs
	private String userId;
	private String password;

	// outputs
	private String url;

	@Override
	public String execute() throws Exception {
		if (!XPEDXMyItemsUtils.isCurrentUserAdmin(wcContext)) {
			throw new IllegalAccessException("You are not an admin user.");
		}

		// per convention, data parameter is space-delimited userId and password
		String data = PunchoutOciUtil.encryptData(new OciCredentials(userId, password));

		url = String.format("https://punchoutorder.com/swc/xpedx/jsp/interop/oci/OCIPost.jsp?sfId=xpedx&data=%s", data);

		return SUCCESS;
	}

	// --- Input and Output fields

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

}

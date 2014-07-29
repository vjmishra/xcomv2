package com.sterlingcommerce.xpedx.webchannel.home;

import org.apache.log4j.Logger;

import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.webchannel.core.wcaas.Logout;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

@SuppressWarnings("serial")
public class XPEDXLogoutAction extends Logout {

	private static final Logger LOG = Logger.getLogger(XPEDXLogoutAction.class);

	@Override
	public String execute() {
		SCUIContext scuiContext = getWCContext().getSCUIContext();

		String returnType = "WebUser";
		String isSalesRep = null;
		if (scuiContext.getSession().getAttribute("IS_SALES_REP") != null) {
			isSalesRep = (String) scuiContext.getSession().getAttribute("IS_SALES_REP");
		}
		if (isSalesRep != null && isSalesRep.equalsIgnoreCase("true")) {
			returnType = "SalesRepUser";
		}
		boolean isPunchoutUser = XPEDXWCUtils.isPunchoutUser(getWCContext());
		if (isPunchoutUser) {
			returnType = "punchoutUser";
		}

		try {
			super.execute();
		} catch (Exception e) {
			LOG.error("Error during logout " + e.getMessage());
		}

		return returnType;
	}

}

package com.sterlingcommerce.xpedx.webchannel.home;

import com.sterlingcommerce.webchannel.core.WCAction;

@SuppressWarnings("serial")
public class HomeAction extends WCAction {

	public static final String RESULT_TOA_MODAL = "TOA_MODAL";

	@Override
	public String execute() throws Exception {
		String result = super.execute();

		// check if user needs to access TOA
		boolean isGuestUser = getWCContext().isGuestUser();
		String isTOAaccepted = (String) getWCContext().getWCAttribute("isTOAaccepted");
		if (!isGuestUser && (isTOAaccepted == null || "".equals(isTOAaccepted) || "N".equals(isTOAaccepted))) {
			// force user to blank screen with TOA
			return RESULT_TOA_MODAL;
		} else {
			// proceed to target action
			return result;
		}
	}

}

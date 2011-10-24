package com.sterlingcommerce.xpedx.webchannel.order;

import org.apache.log4j.Logger;

import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.order.OrderSaveBaseAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

/**
 * @author rugrani
 *
 */
public class XPEDXDraftOrderAddSpecialItemAction extends OrderSaveBaseAction {
	@Override
	public String execute() {
		try {
			prepareAndInvokeMashups();
		} catch (XMLExceptionWrapper e) {
			return "error";
		} catch (CannotBuildInputException e) {
			return "error";
		}
		return "success";
	}
	private static final Logger log = Logger.getLogger(XPEDXDraftOrderAddSpecialItemAction.class);
}



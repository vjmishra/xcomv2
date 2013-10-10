package com.sterlingcommerce.xpedx.webchannel.order;

import com.sterlingcommerce.webchannel.order.ResetPendingOrderAction;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class XPEDXResetPendingOrderAction extends ResetPendingOrderAction {	
	
	protected String resetWithError = "N"; //added for XBT-109

	public String getResetWithError() {
		return resetWithError;
	}

	public void setResetWithError(String resetWithError) {
		this.resetWithError = resetWithError;
	}
//end of XBT-109

	public String execute()
	{
		//added for jira 4306
		XPEDXWCUtils.resetEditedOrderShipTo(wcContext);
		wcContext.getSCUIContext().getSession().removeAttribute(XPEDXConstants.EDITED_ORDER_HEADER_KEY);
		XPEDXWCUtils.removeObectFromCache(XPEDXConstants.APPROVE_ORDER_FLAG);
		return super.execute();
	}
}

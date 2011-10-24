package com.sterlingcommerce.xpedx.webchannel.order;

import com.sterlingcommerce.webchannel.order.ResetPendingOrderAction;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;

public class XPEDXResetPendingOrderAction extends ResetPendingOrderAction {	
	
	public String execute()
	{
		wcContext.getSCUIContext().getSession().removeAttribute(XPEDXConstants.EDITED_ORDER_HEADER_KEY);
		return super.execute();
	}
}

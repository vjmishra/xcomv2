package com.sterlingcommerce.xpedx.webchannel.order;

import com.sterlingcommerce.webchannel.order.ResetPendingOrderAction;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class XPEDXResetPendingOrderAction extends ResetPendingOrderAction {	
	
	public String execute()
	{
		//added for jira 4306
		XPEDXWCUtils.resetEditedOrderShipTo(wcContext);
		wcContext.getSCUIContext().getSession().removeAttribute(XPEDXConstants.EDITED_ORDER_HEADER_KEY);		
		return super.execute();
	}
}

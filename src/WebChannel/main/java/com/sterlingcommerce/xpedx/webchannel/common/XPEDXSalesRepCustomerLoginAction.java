package com.sterlingcommerce.xpedx.webchannel.common;

import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.salesrep.XPEDXSalesRepUtils;

public class XPEDXSalesRepCustomerLoginAction extends WCAction {

	@Override
	public String execute()
    {
		SCUIContext scuiContext = getWCContext().getSCUIContext();
		CookieUtil.setCookie(scuiContext.getRequest(), scuiContext.getResponse(), CookieUtil.SALESREP, "true");

		(new XPEDXSalesRepUtils()).fetchCustomerLogin(request, wcContext);

		return WCAction.SUCCESS;
    }

}

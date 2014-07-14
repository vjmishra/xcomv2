package com.sterlingcommerce.xpedx.webchannel.common;

import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.salesrep.XPEDXSalesRepUtils;

public class XPEDXSalesRepCustomerLoginAction extends WCAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2929835801646530741L;

	public String execute()
    {
		(new XPEDXSalesRepUtils()).fetchCustomerLogin(request, wcContext);
	
		return WCAction.SUCCESS;
    }

}

package com.sterlingcommerce.xpedx.webchannel.order;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.sterlingcommerce.webchannel.core.WCMashupAction;

public class XPEDXEditOrderAction  extends WCMashupAction {
	
	private static final Logger LOG = Logger
	.getLogger(XPEDXEditOrderAction.class);
	
	public String execute()
	{
		try
		{
			prepareAndInvokeMashup("xpedx_changepricelock");
		}
		catch(Exception e)
		{
			
		}
		return "success";
	}

}

package com.sterlingcommerce.xpedx.webchannel.order;

import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.order.LineLevelISPUAction;

public class XPEDXLineLevelSPUAction extends LineLevelISPUAction{
	
	public String execute()
	{
		String definitionFromSession = (String)wcContext.getWCAttribute("SWC_CHECKOUT_TYPE", WCAttributeScope.LOCAL_SESSION);
		  if("Single_Step".equals(definitionFromSession))
	      {
			  isMultiStepCheckoutObj=Boolean.FALSE;
	      }
		return super.execute();
	}

}

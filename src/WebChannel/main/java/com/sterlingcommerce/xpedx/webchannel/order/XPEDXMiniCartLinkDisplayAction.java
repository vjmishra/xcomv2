package com.sterlingcommerce.xpedx.webchannel.order;

import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.order.MiniCartLinkDisplayAction;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;

@SuppressWarnings("serial")
public class XPEDXMiniCartLinkDisplayAction extends MiniCartLinkDisplayAction {

	public XPEDXMiniCartLinkDisplayAction()
    {
		 forceRefresh = false;
	     commerceContextHelper = new CommerceContextHelper();
    }
	
	public String execute()    {
       
		 	XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext(),forceRefresh);
	        return "success";
    }
	
	 public String refreshWithNextOrNewCartInContextOrderHeaderKey(){		 
		 getWCContext().setWCAttribute("CommerceContextObject", null, WCAttributeScope.SESSION);
		 XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext(),false);
		 return "success";
	 }
	 
	public void setForceRefresh(boolean forceRefresh)
    {
        this.forceRefresh = forceRefresh;
    }

    public boolean getForceRefresh()
    {
        return forceRefresh;
    }

    public CommerceContextHelper getCommerceContextHelper()
    {
        return commerceContextHelper;
    }

	 protected boolean forceRefresh;
	 protected CommerceContextHelper commerceContextHelper;
}

package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.order.CartInContextRefreshingWCMashupAction;
import com.sterlingcommerce.webchannel.order.OrderSaveBaseAction;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class XPEDXUpdateMiniCartAction extends OrderSaveBaseAction{
	
	 private static final Logger LOG = Logger.getLogger(XPEDXUpdateMiniCartAction.class);
	 protected String cicRefreshingMashupOrderHeaderKey = "";
	 private static final String UPDATE_MINI_CART_MASHUP="updateOrderForMiniCart";
	 private static final String DELETE_MINI_CART_MASHUP="deleteLineForMiniCart";
	public String execute()
	{
		XPEDXWCUtils.setYFSEnvironmentVariables(wcContext);
		 String returnValue = ERROR;
		 Element orderLelement=null;
	        try
	        {
	        	Map<String, Element> outputMap=prepareAndInvokeMashups();
	        	if(getMashupIds().contains(UPDATE_MINI_CART_MASHUP))
	        	{
	        		orderLelement=outputMap.get(UPDATE_MINI_CART_MASHUP);
	        	}
	        	else if(getMashupIds().contains(DELETE_MINI_CART_MASHUP))
	        	{
	        		orderLelement=outputMap.get(DELETE_MINI_CART_MASHUP);
	        	}
	        	XPEDXOrderUtils.refreshMiniCart(wcContext, orderLelement, true, XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
	            returnValue=SUCCESS;
	        }
	        catch(Exception e)
	        {
	            LOG.error(e);
	            return ERROR;
	        }
	        XPEDXWCUtils.releaseEnv(wcContext);
	        return returnValue;
	}
	public void setOrderHeaderKey(String orderHeaderKey)
    {
        cicRefreshingMashupOrderHeaderKey = orderHeaderKey;
    }
}

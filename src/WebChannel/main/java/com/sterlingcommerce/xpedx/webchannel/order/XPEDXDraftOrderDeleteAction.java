package com.sterlingcommerce.xpedx.webchannel.order;

import com.sterlingcommerce.webchannel.order.DraftOrderDeleteAction;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

public class XPEDXDraftOrderDeleteAction extends DraftOrderDeleteAction{

	public String execute()
	{
		String retVal="";
		try
		{
			prepareAndInvokeMashups();
			retVal=SUCCESS;
			if (retVal.equals(SUCCESS) && getSingleOrderHeaderKey().equals(CommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext()))) {
		           CommerceContextHelper.flushCartInContextCache(getWCContext());
		           XPEDXWCUtils.removeObectFromCache("OrderHeaderInContext");
					//Remove itemMap from Session, when cart change in context,  For Minicart Jira 3481
					XPEDXWCUtils.removeObectFromCache("itemMap");
					XPEDXOrderUtils.refreshMiniCart(getWCContext(),null,true,false,XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
		       }
		}
		catch(XMLExceptionWrapper e)
        {
              YFCElement errorXML=e.getXML();
              YFCElement errorElement=(YFCElement)errorXML.getElementsByTagName("Error").item(0);
              String errorDeasc=errorElement.getAttribute("ErrorDescription");
              if(errorDeasc.contains("YFS: Order cannot be modified in current status"))
              {
            	  retVal="failure";
              }
        }
		catch(Exception e)
		{
			retVal="error";
		}
		return retVal;
	}
}

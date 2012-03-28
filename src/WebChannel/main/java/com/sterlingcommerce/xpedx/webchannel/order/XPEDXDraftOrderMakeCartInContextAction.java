package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.order.DraftOrderMakeCartInContextAction;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;

public class XPEDXDraftOrderMakeCartInContextAction extends
		DraftOrderMakeCartInContextAction {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger
			.getLogger(XPEDXDraftOrderMakeCartInContextAction.class);

	private String redirectToDraftOrderDetails;
	
	public String getRedirectToDraftOrderDetails() {
		return redirectToDraftOrderDetails;
	}

	public void setRedirectToDraftOrderDetails(String redirectToDraftOrderDetails) {
		this.redirectToDraftOrderDetails = redirectToDraftOrderDetails;
	}

	public String execute() {
		String returnValue = super.execute();
		if (returnValue.equals(SUCCESS) && !(redirectToDraftOrderDetails != null && redirectToDraftOrderDetails.equals("true"))) {
			try {
				//buildNewCommerceContext
				XPEDXCommerceContextHelper.createNewCartInContext(getWCContext(),null,orderHeaderKey);
				//Commented for performance issue
				/*CommerceContextHelper.overrideCartInContext(getWCContext(),
						orderHeaderKey);*/
				/* Map<String, String> valueMap = new HashMap<String, String>();
			     valueMap.put("/Order/@OrderHeaderKey", orderHeaderKey);
			     //List<String> itemAndTotalList=new ArrayList<String>();
			     Element input = WCMashupHelper.getMashupInput("XPEDXgetOrderList", valueMap, getWCContext());
			     Element output = (Element)WCMashupHelper.invokeMashup("XPEDXgetOrderList", input, getWCContext().getSCUIContext());
			     XPEDXOrderUtils.refreshMiniCart(getWCContext(),output,false,false,XPEDXConstants.MAX_ELEMENTS_IN_MINICART);*/
			} catch (Exception ex) {
				LOG.debug(ex);
			}
		}
		return returnValue;
	}
}

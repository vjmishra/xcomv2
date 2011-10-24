package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.order.DraftOrderCreateAction;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.webchannel.order.utilities.DraftOrderCreationHelper;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

/**
 * 
 * @author Manohar Reddy
 * 
 */
public class XPEDXDraftOrderCreateAction extends DraftOrderCreateAction {

	private static final long serialVersionUID = 1L;
	private String orderName;
	private String orderHeaderKey = null;
	private String orderDescription;
	private final static String CREATE_MASHUP_ID = "draftOrderCreate";
	private final static String DEFAULT_CARRIER_SERVICE_RULE = "SWC_DEFAULT_CARRIER_SERVICE_FOR_NEW_DRAFT_ORDERS";
	private final static String DOCUMENT_TYPE = "0001";

	public String execute() {
		try {
			Element output = null;
			if (isCartOnBehalfOf()) {
				output=XPEDXOrderUtils.createNewDraftOrderOnBehalfOf(getWCContext(),getOrderName(),orderDescription);
			} else if (orderName != null) {
				output = DraftOrderCreationHelper.createNewDraftOrder(
						getWCContext(), orderName);				
			}
			if (output != null) {
				processOutput(output);
				orderHeaderKey = getOrderKeyFromOutput(outputDoc);
				CommerceContextHelper.overrideCartInContext(getWCContext(),
						orderHeaderKey);
			}
		//	Element outputDoc = prepareAndInvokeMashup("xpedx_me_changeOrderHeader");//adding cart description to the order header or cart
			/*List<String> itemAndTotalList=new ArrayList<String>();
			itemAndTotalList.add("0");
			itemAndTotalList.add("0");
			XPEDXWCUtils.setObectInCache("CommerceContextHelperOrderTotal", itemAndTotalList);*/
			XPEDXOrderUtils.refreshMiniCart(getWCContext(),null,true,false,XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
			XPEDXWCUtils.setObectInCache("OrderHeaderInContext",orderHeaderKey);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	private boolean isCartOnBehalfOf() {
		String customerInSession = XPEDXWCUtils
				.getLoggedInCustomerFromSession(getWCContext());
		if (customerInSession == null) {
			return false;
		}
		return true;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderHeaderKey() {
		return orderHeaderKey;
	}

	public String getOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}
}

package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;

public class XPEDXDraftOrderDropDownAction extends WCMashupAction {

	public XPEDXDraftOrderDropDownAction() {

		outputDoc = null;

		cartList = new LinkedHashMap();
	}

	public String execute() {
		try {
			outputDoc = (Element) prepareAndInvokeMashup("xpedxDraftOrderDropDown");

		} catch (Exception ex) {
			log.error(ex);
		}
		return "success";
	}

	public Map getCartList() throws Exception {
		Element dfOrderListElement = outputDoc.getOwnerDocument()
				.getDocumentElement();
		cartList.put("", "");
		int index = 0;
		for (Iterator itr = SCXmlUtils.getChildren(dfOrderListElement); itr
				.hasNext();) {
			Element dfOrderElem = (Element) itr.next();
			String orderName = SCXmlUtils
					.getAttribute(dfOrderElem, "OrderName");
			String orderHeaderKey = SCXmlUtils.getAttribute(dfOrderElem,
					"OrderHeaderKey");
			cartList.put(orderHeaderKey, orderName);
			if (XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(
					getWCContext(), false) == null
					&& index++ == 0) {
				CommerceContextHelper.overrideCartInContext(getWCContext(),
						orderHeaderKey);
			}
		}

		return cartList;
	}

	public Element getOutputDoc() {
		return outputDoc;
	}

	public void setOutputDoc(Element outputDocument) {
		outputDoc = outputDocument;
	}

	private static final Logger log = Logger
			.getLogger(XPEDXDraftOrderDropDownAction.class);
	protected Element outputDoc;
	protected Map cartList;

}

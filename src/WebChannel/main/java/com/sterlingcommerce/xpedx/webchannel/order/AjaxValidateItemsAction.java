package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

/**
 * This action is called via ajax and returns a json response (see struts xml).
 *
 * Inputs:
 * itemType: 1=xpedx Item Number, 2=Customer Part Number
 * itemIds: the item ids to validate
 *
 * Outputs:
 * hasItemErrors: true if one or more of the items is invalid
 * itemValidFlags: Map in the format: key = item id, value = boolean indicating if item is valid (true is valid, false is invalid)
 *
 * @author Trey Howard
 */
@SuppressWarnings("serial")
public class AjaxValidateItemsAction extends WCAction {

	// inputs
	private String itemType; // 1=xpedx #, 2=Customer Part #
	private Set<String> itemIds;

	// outputs
	private boolean hasItemErrors;
	private Map<String, Boolean> itemValidFlags = new LinkedHashMap<String, Boolean>();

	@Override
	public String execute() throws Exception {
		if (itemType == null) {
			throw new IllegalArgumentException("itemType must be defined");
		}

		for (String itemId : itemIds) {
			// there is not an api that allows multiple item ids to be checked at one time, so we have to make a call per item id
			itemValidFlags.put(itemId, isValidItem(itemId));
		}

		// check if we have at least one invalid item
		hasItemErrors = new HashSet<Boolean>(itemValidFlags.values()).contains(Boolean.FALSE);

		return SUCCESS;
	}

	/**
	 * Performs the appropriate api call(s) to determine if <code>itemId</code> is valid.
	 * @param itemId If <code>itemType</code>=1 then this is the xpedx Item Number. If <code>itemType</code>=2 then this is the Customer Part Number.
	 * @return
	 * @throws Exception API error
	 */
	private boolean isValidItem(String itemId) throws Exception {
		if (itemId.trim().length() == 0) {
			return false;
		}

		if ("2".equals(itemType)) {
			// 2 = Customer Part #
			XPEDXShipToCustomer shipToCustomer =(XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache("shipToCustomer");

			Map<String, String> valueMap = new LinkedHashMap<String, String>();
			valueMap.put("/XPXItemcustXref@EnvironmentCode", "M");
			valueMap.put("/XPXItemcustXref@CustomerDivision", shipToCustomer.getExtnCustomerDivision());
			valueMap.put("/XPXItemcustXref@CustomerNumber", shipToCustomer.getExtnLegacyCustNumber());

			Element xpedxItemCustXRefInputElem = WCMashupHelper.getMashupInput("xpedxItemCustXRef", valueMap, wcContext);
			xpedxItemCustXRefInputElem.setAttribute("CustomerItemNumber", itemId);

			Element xpedxItemCustXRefOutputElem = (Element) WCMashupHelper.invokeMashup("xpedxItemCustXRef", xpedxItemCustXRefInputElem, wcContext.getSCUIContext());

			Element xpxItemcustXrefElem = SCXmlUtils.getChildElement(xpedxItemCustXRefOutputElem, "XPXItemcustXref");
			if (xpxItemcustXrefElem == null) {
				// if output does not contain the requested item then it is invalid
				return false;
			} else {
				// if out contains the requested item then we now have the xpedx Item # for this Customer Part #
				// but we must still verify that the xpedx Item # is valid (entitled)
				itemId = xpxItemcustXrefElem.getAttribute("LegacyItemNumber");
			}
		}

		// 1 = xpedx Item #
		Map<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("/Item@CallingOrganizationCode", wcContext.getStorefrontId());
		valueMap.put("/Item@ItemID", itemId);
		valueMap.put("/Item/CustomerInformation@CustomerID", wcContext.getCustomerId());

		Element xpedxValidateItemsInputElem = WCMashupHelper.getMashupInput("xpedxValidateItems", valueMap, wcContext);

		Element xpedxValidateItemsOutputElem = (Element) WCMashupHelper.invokeMashup("xpedxValidateItems", xpedxValidateItemsInputElem, wcContext.getSCUIContext());

		// if output contains the requested item then the item is valid
		return xpedxValidateItemsOutputElem.getChildNodes().getLength() > 0;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public void setItemIds(Set<String> itemIds) {
		this.itemIds = itemIds;
	}

	public boolean isHasItemErrors() {
		return hasItemErrors;
	}

	public Map<String, Boolean> getItemValidFlags() {
		return itemValidFlags;
	}

}

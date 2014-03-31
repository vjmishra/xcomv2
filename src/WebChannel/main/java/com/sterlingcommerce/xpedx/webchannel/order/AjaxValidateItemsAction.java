package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
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
	private Map<String, String> itemUoms = new LinkedHashMap<String, String>();

	@Override
	public String execute() throws Exception {
		if (itemType == null) {
			throw new IllegalArgumentException("itemType must be defined");
		}

		for (String itemId : itemIds) {
			// there is not an api that allows multiple item ids to be checked at one time, so we have to make a call per item id
			String uom = getUomForItem(itemId);
			if (uom == null) {
				itemValidFlags.put(itemId, false);
			} else {
				itemValidFlags.put(itemId, true);
				itemUoms.put(itemId, uom);
			}
		}

		// check if we have at least one invalid item
		hasItemErrors = new HashSet<Boolean>(itemValidFlags.values()).contains(Boolean.FALSE);

		return SUCCESS;
	}

	/**
	 * Performs the appropriate api call(s) to get the default uom for <code>itemId</code>. The implicitly validates that the item is valid, since an invalid item will not be returned from the api.
	 * @param itemId If <code>itemType</code>=1 then this is the xpedx Item Number. If <code>itemType</code>=2 then this is the Customer Part Number.
	 * @return If the item is valid, returns the uom. Otherwise, returns null.
	 * @throws Exception API error
	 */
	private String getUomForItem(String itemId) throws Exception {
		if (itemId.trim().length() == 0) {
			return null;
		}

		if ("2".equals(itemType)) {
			// we must translate the customer part # to an xpedx item # before checking if it's valid
			itemId = getItemNumberForCustomerPartNumber(itemId);
			if (itemId == null) {
				// unknown customer part #
				return null;
			}
		}

		Map<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("/Item@CallingOrganizationCode", wcContext.getStorefrontId());
		valueMap.put("/Item@ItemID", itemId);
		valueMap.put("/Item/CustomerInformation@CustomerID", wcContext.getCustomerId());

		Element xpedxValidateItemsInputElem = WCMashupHelper.getMashupInput("xpedxValidateItems", valueMap, wcContext);

		Element itemListElem = (Element) WCMashupHelper.invokeMashup("xpedxValidateItems", xpedxValidateItemsInputElem, wcContext.getSCUIContext());

		// if output contains the requested item then the item is valid
		NodeList itemListNodes = itemListElem.getChildNodes();
		for (int i = 0; i < itemListNodes.getLength(); i++) {
			Element itemElem = (Element) itemListNodes.item(i);
			if (itemId.equals(itemElem.getAttribute("ItemID"))) {
				// output contains the item id, so it's valid
				return itemElem.getAttribute("UnitOfMeasure");
			}
		}

		// output did not contain the item id, so it's invalid
		return null;
	}

	/**
	 * Performs API call to get xpedx Item Number for the given Customer Part Number.
	 * @param customerPartNumber
	 * @return Returns null if customerPartNumber does not exist
	 * @throws Exception API error
	 */
	private String getItemNumberForCustomerPartNumber(String customerPartNumber) throws Exception {
		XPEDXShipToCustomer shipToCustomer =(XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache("shipToCustomer");

		Map<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("/XPXItemcustXref@EnvironmentCode", "M");
		valueMap.put("/XPXItemcustXref@CustomerDivision", shipToCustomer.getExtnCustomerDivision());
		valueMap.put("/XPXItemcustXref@CustomerNumber", shipToCustomer.getExtnLegacyCustNumber());

		Element xpedxItemCustXRefInputElem = WCMashupHelper.getMashupInput("xpedxItemCustXRef", valueMap, wcContext);
		xpedxItemCustXRefInputElem.setAttribute("CustomerItemNumber", customerPartNumber);

		Element xpxItemcustXrefListElem = (Element) WCMashupHelper.invokeMashup("xpedxItemCustXRef", xpedxItemCustXRefInputElem, wcContext.getSCUIContext());

		Element xpxItemcustXrefElem = SCXmlUtil.getChildElement(xpxItemcustXrefListElem, "XPXItemcustXref");
		if (xpxItemcustXrefElem == null) {
			// unknown customer part #
			return null;

		} else {
			// if out contains the requested item then we now have the xpedx item # for this customer part #
			// but we must still verify that the xpedx Item # is valid (entitled)
			return xpxItemcustXrefElem.getAttribute("LegacyItemNumber");
		}
	}

	// --- Input and Output fields

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

	public Map<String, String> getItemUoms() {
		return itemUoms;
	}

}

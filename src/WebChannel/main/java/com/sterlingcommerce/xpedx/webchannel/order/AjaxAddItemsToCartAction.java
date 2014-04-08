package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

/**
 * This action performs validation and adds the specified items to cart.
 * If validation fails then failure information is returned. Otherwise, all items are added to the cart.
 *
 * This action is intended to be called via ajax, since it returns a json response (see struts xml).
 *
 * Inputs:
 * itemType: 1=xpedx Item Number, 2=Customer Part Number
 * itemIds: the item ids to validate
 *
 * Outputs:
 * hasItemErrors: true if one or more of the items is invalid
 * itemValidFlags: Map in the format: key = item id, value = boolean indicating if item is valid (true is valid, false is invalid)
 */
@SuppressWarnings("serial")
public class AjaxAddItemsToCartAction extends WCAction {

	public static final String TYPE_XPEDX = "1";
	public static final String TYPE_CUSTOMER_PART_NUMBER = "2";

	// inputs
	private String itemType; // 1=xpedx #, 2=Customer Part #
	private String itemIds;

	// outputs
	private boolean hasItemErrors;
	private Map<String, Boolean> itemValidFlags = new LinkedHashMap<String, Boolean>();
	private Map<String, String> itemUoms = new LinkedHashMap<String, String>();

	@Override
	public String execute() throws Exception {
		if (itemType == null) {
			throw new IllegalArgumentException("Missing parameter: itemType");
		}
		if (itemIds == null) {
			throw new IllegalArgumentException("Missing parameter: itemIds");
		}

		Set<String> itemIdsSet = new LinkedHashSet<String>(Arrays.asList(itemIds.split("\\*")));

		initUomsForItems(itemIdsSet);

		hasItemErrors = itemIdsSet.size() != itemUoms.size();
		if (hasItemErrors) {
			// determine which item ids are not in the map
			Set<String> copyItemIds = new LinkedHashSet<String>(itemIdsSet);
			copyItemIds.removeAll(itemUoms.keySet());

			for (String itemId : copyItemIds) {
				itemValidFlags.put(itemId, false);
			}
		}

		return SUCCESS;
	}

	/**
	 * Performs API call(s) to get the default uom for all <code>itemIds</code>. This implicitly validates that the item is valid,
	 *  since an invalid item will not be returned from the api.
	 * <br>
	 * Upon completion, <code>itemUoms</code> is populated with uoms and <code>itemValidFlags</code> is populated with <code>true</code> for each item in <code>itemUoms</code>.
	 * @param itemId If <code>itemType</code>=1 then this is the xpedx Item Number. If <code>itemType</code>=2 then this is the Customer Part Number.
	 * @throws Exception API error
	 */
	private void initUomsForItems(Set<String> itemIds) throws Exception {
		Set<String> xpedxItemIds = new LinkedHashSet<String>(itemIds.size());
		if (TYPE_XPEDX.equals(itemType)) {
			// itemIds contains xpedx item ids
			xpedxItemIds.addAll(itemIds);

		} else {
			// TODO create new multi-item mashup to gather all of these at once: uncomment getItemNumbersForCustomerPartNumbers
			// we must translate each customer part # to an xpedx item # before checking if it's valid
			Map<String, String> itemIdsForCustomerPartNumbers = getItemNumbersForCustomerPartNumbers(itemIds);
			xpedxItemIds.addAll(itemIdsForCustomerPartNumbers.values());
		}

		if (xpedxItemIds.isEmpty()) {
			return;
		}

		// lookup xpedx item numbers
		Map<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("/Item@CallingOrganizationCode", wcContext.getStorefrontId());
		valueMap.put("/Item/CustomerInformation@CustomerID", wcContext.getCustomerId());

		Element xpedxGetUomsForItemsInputElem = WCMashupHelper.getMashupInput("xpedxGetUomsForItems", valueMap, wcContext);

		Element orInputElem = SCXmlUtil.getXpathElement(xpedxGetUomsForItemsInputElem, "/Item/ComplexQuery/Or");
		// append an 'Exp' tag for each item id
		for (String itemId : xpedxItemIds) {
			Element expInputElem = xpedxGetUomsForItemsInputElem.getOwnerDocument().createElement("Exp");
			expInputElem.setAttribute("Name", "ItemID");
			expInputElem.setAttribute("Value", itemId);

			orInputElem.appendChild(expInputElem);
		}

		Element itemListElem = (Element) WCMashupHelper.invokeMashup("xpedxGetUomsForItems", xpedxGetUomsForItemsInputElem, wcContext.getSCUIContext());

		// if output contains the requested item then the item is valid
		NodeList itemListNodes = itemListElem.getChildNodes();
		for (int i = 0; i < itemListNodes.getLength(); i++) {
			Element itemElem = (Element) itemListNodes.item(i);
			String itemId = itemElem.getAttribute("ItemID");
			itemValidFlags.put(itemId, true);
			itemUoms.put(itemId, itemElem.getAttribute("UnitOfMeasure"));
		}
	}

	/**
	 * Performs API call to get xpedx Item Numbers for the given Customer Part Numbers.
	 * @param customerPartNumber
	 * @return Returns a map in the form: key=customer part number, value=xpedx item number
	 * @throws Exception API error
	 */
	private Map<String, String> getItemNumbersForCustomerPartNumbers(Set<String> customerPartNumbers) throws Exception {
		XPEDXShipToCustomer shipToCustomer =(XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache("shipToCustomer");

		Map<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("/XPXItemcustXref@EnvironmentCode", "M");
		valueMap.put("/XPXItemcustXref@CustomerDivision", shipToCustomer.getExtnCustomerDivision());
		valueMap.put("/XPXItemcustXref@CustomerNumber", shipToCustomer.getExtnLegacyCustNumber());

		Element xpedxItemCustXRefInputElem = WCMashupHelper.getMashupInput("xpedxGetItemIdsForCustomerPartNumbers", valueMap, wcContext);

		Element orInputElem = SCXmlUtil.getXpathElement(xpedxItemCustXRefInputElem, "/XPXItemcustXref/ComplexQuery/Or");

		// append an 'Exp' tag for each customer part number
		for (String customerPartNumber : customerPartNumbers) {
			Element expInputElem = xpedxItemCustXRefInputElem.getOwnerDocument().createElement("Exp");
			expInputElem.setAttribute("Name", "CustomerItemNumber");
			expInputElem.setAttribute("Value", customerPartNumber);

			orInputElem.appendChild(expInputElem);
		}

		Element xpxItemcustXrefListElem = (Element) WCMashupHelper.invokeMashup("xpedxGetItemIdsForCustomerPartNumbers", xpedxItemCustXRefInputElem, wcContext.getSCUIContext());

		Map<String, String> xpedxItemIds = new LinkedHashMap<String, String>(customerPartNumbers.size());

		ArrayList<Element> xpxItemcustXrefElems = SCXmlUtil.getElements(xpxItemcustXrefListElem, "XPXItemcustXref");
		for (Element xpxItemcustXrefElem : xpxItemcustXrefElems) {
			if (xpxItemcustXrefElem != null) {
				xpedxItemIds.put(xpxItemcustXrefElem.getAttribute("CustomerItemNumber"), xpxItemcustXrefElem.getAttribute("LegacyItemNumber"));
			}
		}

		return xpedxItemIds;
	}

	// --- Input and Output fields

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public void setItemIds(String itemIds) {
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

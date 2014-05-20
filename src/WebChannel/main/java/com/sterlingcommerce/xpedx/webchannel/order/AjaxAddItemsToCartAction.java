package com.sterlingcommerce.xpedx.webchannel.order;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import au.com.bytecode.opencsv.CSVReader;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils.CartSummaryPriceStatus;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.megamenu.MegaMenuUtil;
import com.yantra.yfc.util.YFCCommon;

/**
 * This action performs validation and adds the specified items to cart.
 * If validation fails then failure information is returned. Otherwise, all items are added to the cart.
 *
 * This action is intended to be called via ajax, since it returns a json response (see struts xml).
 *
 * All input parameters except for 'itemType' are passed in as an asterisk-separated value (eg, <code>1*2*3</code>). All parameters must contain the same number of elements.
 * For customers that don't support line po/account numbers, then pass empty string for each value (eg, <code>**</code> for 3 empty strings).
 *
 * Inputs:
 * isEditOrder: flag to indicate if if editing an existing order. value must be either 'true' or 'false'
 * orderHeaderKey: order header key of order being modified
 * itemType: 1=xpedx Item Number, 2=Customer Part Number
 * items: the item ids to validate
 * qtys: the quantity
 * pos: the line po number (for customers that don't support this, use empty string)
 * accounts: the line account number
 *
 * Outputs:
 * allItemsValid: true if all items are valid
 * itemValidFlags: Map in the format: key = item id, value = boolean indicating if item is valid (true is valid, false is invalid)
 */
@SuppressWarnings("serial")
public class AjaxAddItemsToCartAction extends WCAction {

	private static final Logger log = Logger.getLogger(MegaMenuUtil.class);

	public static final String TYPE_XPEDX = "1";
	public static final String TYPE_CUSTOMER_PART_NUMBER = "2";

	// inputs
	private Boolean isEditOrder;
	private String orderHeaderKey;
	private String itemType; // 1=xpedx #, 2=Customer Part #
	private String items;
	private String qtys;
	private String pos;
	private String accounts;

	// outputs
	private boolean allItemsValid;
	private Map<String, Boolean> itemValidFlags = new LinkedHashMap<String, Boolean>();
	private String unexpectedError;

	/**
	 * @throws IllegalArgumentException If missing a parameter
	 */
	private void assertInputs() {
		if (isEditOrder == null) {
			throw new IllegalArgumentException("Missing parameter: isEditOrder");
		} else if (orderHeaderKey == null) {
			throw new IllegalArgumentException("Missing parameter: orderHeaderKey");
		} else if (itemType == null) {
			throw new IllegalArgumentException("Missing parameter: itemType");
		} else if (items == null) {
			throw new IllegalArgumentException("Missing parameter: items");
		} else if (qtys == null) {
			throw new IllegalArgumentException("Missing parameter: qtys");
		} else if (pos == null) {
			throw new IllegalArgumentException("Missing parameter: pos");
		} else if (accounts == null) {
			throw new IllegalArgumentException("Missing parameter: accounts");
		}
	}

	/**
	 * Encapsulates the CSVReader for asterisk-separated <code>param</code>
	 * @param param asterisk-separate value
	 * @return
	 * @throws RuntimeException CSV parsing error
	 */
	private static String[] parseParam(String param) {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new StringReader(param), '*');
			List<String[]> lines;
			try {
				lines = reader.readAll();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return lines.isEmpty() ? new String[] { "" } : lines.get(0);

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	/**
	 * Encapsulates the conversion of <code>items</code>, <code>qtys</code>, <code>pos</code>, and <code>accounts</code> inputs into a list of <code>InputRow</code> objects.
	 * @return
	 * @throws IllegalArgumentException If number of elements in asterisk-separated inputs do not match.
	 */
	private List<InputRow> convertInputsToRows() {
		String[] itemIds = parseParam(items);
		String[] quantities = parseParam(qtys);
		String[] linePOs = parseParam(pos);
		String[] lineAccounts = parseParam(accounts);

		if (quantities.length != itemIds.length) {
			throw new IllegalArgumentException("qtys has " + quantities.length + " elements, but items has " + itemIds.length + " elements");
		}
		if (linePOs.length != itemIds.length) {
			throw new IllegalArgumentException("pos has " + linePOs.length + " elements, but items has " + itemIds.length + " elements");
		}
		if (lineAccounts.length != itemIds.length) {
			throw new IllegalArgumentException("accounts has " + lineAccounts.length + " elements, but items has " + itemIds.length + " elements");
		}

		List<InputRow> rows = new ArrayList<InputRow>(itemIds.length);
		for (int i = 0; i < itemIds.length; i++) {
			InputRow row = new InputRow();
			row.setItem(itemIds[i]);
			row.setQty(quantities[i]);
			row.setPo(linePOs[i]);
			row.setAccount(lineAccounts[i]);
			rows.add(row);
		}
		return rows;
	}

	@Override
	public String execute() {
		try {
		assertInputs();

		List<InputRow> rows = convertInputsToRows();

		Set<String> uniqueItemIds = new LinkedHashSet<String>(rows.size());
		for (InputRow row : rows) {
			uniqueItemIds.add(row.getItem());
		}

		Map<String, ItemDetails> itemDetails = getItemsDetails(uniqueItemIds);

		// all items that have a uom are valid
		for (String itemId : itemDetails.keySet()) {
			itemValidFlags.put(itemId, true);
		}

		allItemsValid = uniqueItemIds.size() == itemDetails.size();
		if (allItemsValid) {
			// EB-5895. Get the default transaction UOMs for order lines. In Quick add User does not have option to select UOMs. So add the default UOM for transaction UOM as per rules instead of Item UOMs
			Map <String,String> defaultTransUOMMap = XPEDXOrderUtils.getDefaultTransactionalUOMsForQuickAdd(wcContext.getCustomerId(), new ArrayList<String>(uniqueItemIds), wcContext.getStorefrontId());
			String editedOrderHeaderKey = XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
			String draftOrderFlag = YFCCommon.isVoid(editedOrderHeaderKey) ? "Y" : "N";

			Map<String, String> valueMap = new LinkedHashMap<String, String>();

			valueMap.put("/Order/@OrderHeaderKey", orderHeaderKey);
			valueMap.put("/Order/@DraftOrderFlag", draftOrderFlag);

			int rowCount = 1;
			for (InputRow row : rows) {
				valueMap.put("/Order/OrderLines/OrderLine[" + rowCount + "]/@CustomerPONo", row.getPo());
				valueMap.put("/Order/OrderLines/OrderLine[" + rowCount + "]/Extn/@ExtnCustLineAccNo", row.getAccount());
				valueMap.put("/Order/OrderLines/OrderLine[" + rowCount + "]/Extn/@ExtnEditOrderFlag", isEditOrder ? "Y" : "N");
				valueMap.put("/Order/OrderLines/OrderLine[" + rowCount + "]/Item/@ItemID", row.getItem());
				valueMap.put("/Order/OrderLines/OrderLine[" + rowCount + "]/@OrderedQty", row.getQty());
				valueMap.put("/Order/OrderLines/OrderLine[" + rowCount + "]/OrderLineTranQuantity/@OrderedQty", row.getQty());
				valueMap.put("/Order/OrderLines/OrderLine[" + rowCount + "]/OrderLineTranQuantity/@TransactionalUOM", defaultTransUOMMap.get(row.getItem())!=null?defaultTransUOMMap.get(row.getItem()):itemDetails.get(row.getItem()).getUom());
				valueMap.put("/Order/OrderLines/OrderLine[" + rowCount + "]/Item/@UnitOfMeasure", itemDetails.get(row.getItem()).getUom());
				valueMap.put("/Order/OrderLines/OrderLine[" + rowCount + "]/Item/@ItemShortDesc", ""); // not needed
				valueMap.put("/Order/OrderLines/OrderLine[" + rowCount + "]/Item/@ProductClass", ""); // not needed
				valueMap.put("/Order/OrderLines/OrderLine[" + rowCount + "]/@LineType", "P"); // we always add items to the cart using xpedx item number, so this is always 'P'
				rowCount += 1;
			}

			Element xpedDraftOrderAddOrderLinesInputElem = WCMashupHelper.getMashupInput("xpedx_me_draftOrderAddOrderLines", valueMap, wcContext);
			xpedDraftOrderAddOrderLinesInputElem.setAttribute("IgnoreOrdering", "Y");

			// eb-5663: if editing order, must flag the order to record changes, otherwise item will be uneditable in the cart
			if (!"Y".equals(draftOrderFlag)) {
				Element pendingChangesElem = xpedDraftOrderAddOrderLinesInputElem.getOwnerDocument().createElement("PendingChanges");
				pendingChangesElem.setAttribute("RecordPendingChanges", "Y");
				xpedDraftOrderAddOrderLinesInputElem.appendChild(pendingChangesElem);
			}

			Element changeOrderOutput = (Element) WCMashupHelper.invokeMashup("xpedx_me_draftOrderAddOrderLines", xpedDraftOrderAddOrderLinesInputElem, wcContext.getSCUIContext());

			XPEDXWCUtils.releaseEnv(wcContext);

			getWCContext().getSCUIContext().getSession().setAttribute(XPEDXDraftOrderAddOrderLinesAction.CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ, changeOrderOutput.getOwnerDocument());
			if(YFCCommon.isVoid(editedOrderHeaderKey)) {
				XPEDXWCUtils.setMiniCartDataInToCache(changeOrderOutput, wcContext, CartSummaryPriceStatus.TBD);
			}

		} else {
			// 1+ items are invalid: determine which item ids are not in the map
			Set<String> copyItemIds = new LinkedHashSet<String>(uniqueItemIds);
			copyItemIds.removeAll(itemDetails.keySet());

			for (String itemId : copyItemIds) {
				itemValidFlags.put(itemId, false);
			}
		}

		} catch (Exception e) {
			log.error("", e);
			unexpectedError = e.getMessage();
		}

		return SUCCESS;
	}

	/**
	 * Performs API call(s) to get the default uom and short description for all <code>itemIds</code>. This implicitly validates that the item is valid,
	 *  since an invalid item will not be returned from the api.
	 * @param itemId If <code>itemType</code>=1 then this is the xpedx Item Number. If <code>itemType</code>=2 then this is the Customer Part Number.
	 * @return Returns a map in the form: key=item id, value=ItemDetails
	 * @throws Exception API error
	 */
	private Map<String, ItemDetails> getItemsDetails(Set<String> itemIds) throws Exception {
		Map<String, ItemDetails> itemDetails = new LinkedHashMap<String, ItemDetails>(itemIds.size());

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
			return itemDetails;
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

			Element primaryInformationElem = SCXmlUtil.getChildElement(itemElem, "PrimaryInformation");

			ItemDetails detail = new ItemDetails();
			detail.setUom(itemElem.getAttribute("UnitOfMeasure"));
			itemDetails.put(itemId, detail);
		}

		return itemDetails;
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

	public void setIsEditOrder(Boolean isEditOrder) {
		this.isEditOrder = isEditOrder;
	}

	public void setOrderHeaderKey(String orderHeaderKey) {
		this.orderHeaderKey = orderHeaderKey;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public void setQtys(String qtys) {
		this.qtys = qtys;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}

	public boolean isAllItemsValid() {
		return allItemsValid;
	}

	public Map<String, Boolean> getItemValidFlags() {
		return itemValidFlags;
	}

	public String getUnexpectedError() {
		return unexpectedError;
	}

	// --- Helper classes

	private static class InputRow {
		private String item;
		private String qty;
		private String po;
		private String account;

		public String getItem() {
			return item;
		}
		public void setItem(String item) {
			this.item = item;
		}
		public String getQty() {
			return qty;
		}
		public void setQty(String qty) {
			this.qty = qty;
		}
		public String getPo() {
			return po;
		}
		public void setPo(String po) {
			this.po = po;
		}
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
	}

	private static class ItemDetails {
		private String uom;

		public String getUom() {
			return uom;
		}
		public void setUom(String uom) {
			this.uom = uom;
		}
	}

}

package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

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
 * listKey: The key for the MIL being edited
 * currentItemCount: the number of items currently in the MIL being edited
 * itemType: 1=xpedx Item Number, 2=Customer Part Number
 * items: the item ids to validate
 * qtys: the quantity
 * pos: the line po number (for customers that don't support this, use empty string)
 * accounts: the line account number (for customers that don't support this, use empty string)
 *
 * Outputs:
 * allItemsValid: true if all items are valid
 * itemValidFlags: Map in the format: key = item id, value = boolean indicating if item is valid (true is valid, false is invalid)
 * unexpectedError: If an exception is thrown during call, this contains the exception's message (not formatted for user)
 */
@SuppressWarnings("serial")
public class AjaxAddItemsToMILAction extends WCAction {

	private static final Logger log = Logger.getLogger(AjaxAddItemsToMILAction.class);

	public static final String TYPE_XPEDX = "1";
	public static final String TYPE_CUSTOMER_PART_NUMBER = "2";

	// inputs
	private String listKey;
	private Integer currentItemCount;
	private String itemType; // 1=xpedx #, 2=Customer Part #
	private String items;
	private String qtys;
	private String pos;
	private String accounts;

	// outputs
	private boolean allItemsValid;
	private Map<String, Boolean> itemValidFlags = new LinkedHashMap<String, Boolean>();
	private String unexpectedError;
	/*Added for Jira 4134*/
	private String createUserName;
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
		/*End Added for Jira 4134*/
	/**
	 * @throws IllegalArgumentException If missing a parameter
	 */
	private void assertInputs() {
		if (listKey == null) {
			throw new IllegalArgumentException("Missing parameter: listKey");
		} else if (currentItemCount == null) {
			throw new IllegalArgumentException("Missing parameter: currentItemCount");
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

			Map<String, ItemDetails> itemDetails = getItemsDetails(uniqueItemIds, rows);

			allItemsValid = uniqueItemIds.size() == itemDetails.size();
			if (allItemsValid) {
				XPEDXCustomerContactInfoBean contactInfo = (XPEDXCustomerContactInfoBean) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);

				Map <String,String> defaultTransUOMMap = XPEDXOrderUtils.getDefaultTransactionalUOMsForQuickAdd(wcContext.getCustomerId(), new ArrayList<String>(itemDetails.keySet()), wcContext.getStorefrontId());
				/*Added for Jira 4134*/
				String isSalesRep = (String) getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP");
				if(isSalesRep!=null && isSalesRep.equalsIgnoreCase("true")){
					String salesreploggedInUserName = (String)getWCContext().getSCUIContext().getSession().getAttribute("loggedInUserName");
					createUserName=salesreploggedInUserName;
				}
				else{
					createUserName = wcContext.getLoggedInUserName();	
				}
					/*End Added for Jira 4134*/		

				Map<String, String> valueMap = new LinkedHashMap<String, String>();

				valueMap.put("/XPEDXMyItemsList/@MyItemsListKey", listKey);
				valueMap.put("/XPEDXMyItemsList/XPEDXMyItemsItemsList/@MyItemsListKey", listKey);
				valueMap.put("/XPEDXMyItemsList/@ModifyUserName", createUserName);

				int itemIndex = currentItemCount + 1;
				int xpathIndex = 1;
				for (InputRow row : rows) {
					String uom = defaultTransUOMMap.get(row.getItem()) != null ? defaultTransUOMMap.get(row.getItem()):itemDetails.get(row.getItem()).getUom();

					String xpathPrefix = "/XPEDXMyItemsList/XPEDXMyItemsItemsList/XPEDXMyItemsItems[" + xpathIndex + "]/";
					valueMap.put(xpathPrefix + "@Qty", row.getQty());
					valueMap.put(xpathPrefix + "@JobId", row.getAccount());
					valueMap.put(xpathPrefix + "@ItemOrder", String.valueOf(itemIndex));
					valueMap.put(xpathPrefix + "@ItemId", row.getItem());
					valueMap.put(xpathPrefix + "@ItemType", itemType);
					valueMap.put(xpathPrefix + "@UomId", uom);
					valueMap.put(xpathPrefix + "@ItemPoNumber", row.getPo());
					valueMap.put(xpathPrefix + "@MyItemsListKey", listKey);
					valueMap.put(xpathPrefix + "@Createuserid", contactInfo.getCustomerContactID());
					valueMap.put(xpathPrefix + "@ModifyUserName", contactInfo.getFirstName() + " " + contactInfo.getLastName());

					xpathIndex++;
					itemIndex++;
				}

				Element xpedxMyItemsListInputElem = WCMashupHelper.getMashupInput("XPEDXMyItemsQuickAddMulti", valueMap, wcContext);

				WCMashupHelper.invokeMashup("XPEDXMyItemsQuickAddMulti", xpedxMyItemsListInputElem, wcContext.getSCUIContext());

			} else {
				// 1+ items are invalid: determine which item ids are not in the map
				Set<String> validItems = itemValidFlags.keySet();
				for (String itemId : uniqueItemIds) {
					if (!validItems.contains(itemId)) {
						itemValidFlags.put(itemId, false);
					}
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
	 * @param itemIds If <code>itemType</code>=1 then this is the xpedx Item Number. If <code>itemType</code>=2 then this is the Customer Part Number.
	 * @param rows
	 * @return Returns a map in the form: key=item id, value=ItemDetails
	 * @throws Exception API error
	 */
	private Map<String, ItemDetails> getItemsDetails(Set<String> itemIds, List<InputRow> rows) throws Exception {
		Map<String, ItemDetails> itemDetails = new LinkedHashMap<String, ItemDetails>(itemIds.size());

		Set<String> xpedxItemIds = new LinkedHashSet<String>(itemIds.size());
		Map<String,String> itemAndCustomerNumber=new HashMap<String,String>();
		Map<String, String> itemIdsForCustomerPartNumbers =null;
		if (TYPE_XPEDX.equals(itemType)) {
			// itemIds contains xpedx item ids
			xpedxItemIds.addAll(itemIds);

		} else {
			// we must translate each customer part # to an xpedx item # before checking if it's valid
			itemIdsForCustomerPartNumbers = getItemNumbersForCustomerPartNumbers(itemIds);
			xpedxItemIds.addAll(itemIdsForCustomerPartNumbers.values());
			for (InputRow row : rows) {
				row.setCustomerItemNumber(row.getItem());
				row.setItem(itemIdsForCustomerPartNumbers.get(row.getItem()));
				itemAndCustomerNumber.put(row.getItem(), row.getCustomerItemNumber());
			}
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

			ItemDetails detail = new ItemDetails();
			detail.setUom(itemElem.getAttribute("UnitOfMeasure"));
			itemDetails.put(itemId, detail);
			if (TYPE_XPEDX.equals(itemType))
			{
				itemValidFlags.put(itemId, true);

			}
			else
			{
				itemValidFlags.put(itemAndCustomerNumber.get(itemId), true);
			}
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

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}

	public void setCurrentItemCount(Integer currentItemCount) {
		this.currentItemCount = currentItemCount;
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
		private String customerItemNumber;

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
		public String getCustomerItemNumber() {
			return customerItemNumber;
		}
		public void setCustomerItemNumber(String customerItemNumber) {
			this.customerItemNumber = customerItemNumber;
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

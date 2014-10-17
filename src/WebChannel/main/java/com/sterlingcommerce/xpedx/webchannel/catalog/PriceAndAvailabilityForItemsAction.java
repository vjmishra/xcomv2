package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItem;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItemPricingInfo;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceAndAvailability;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil;

/**
 * Performs price and availability (aka P&amp;A) for multiple items.
 *
 * @param items the item ids to validate.
 * @param qtys Calculate P&amp;A for this quantity (impacts OM and partial availability).
 * @param uoms Calculate P&amp;A for this uom (results will be scaled to this uom).
 *
 * @return priceAndAvailability Contains the quantities available and price per uom.
 * @return pricingInfo Contains more detailed pricing information for each item, including bracket pricing for all uoms. Returns null if user lacks 'View Prices' role.
 * @return divisionName The division name for the current ship-to.
 * @return uomDescriptions Contains the uom description (eg, Carton) for the uom key (eg, M_CTN)
 * @return lineErrorMessages Contains error messages for all items requested. An empty error message indicates no error.
 *                           For example, lineErrorMessages.get("123") might contain the value "Price &amp; Availability is not available at this time..."
 */
@SuppressWarnings("serial")
public class PriceAndAvailabilityForItemsAction extends WCAction {

	// input fields
	private String items;
	private String qtys;
	private String uoms;

	// output fields
	private XPEDXPriceAndAvailability priceAndAvailability;
	private Map<String, XPEDXItemPricingInfo> pricingInfo;
	private boolean userHasViewPricesRole;
	private String divisionName;
	private Map<String, String> uomDescriptions;
	private Map<String, String> itemCategories;
	private Map<String, String> lineErrorMessages;
	private Map<String, String> lineStatusCode;

	@Override
	public String execute() throws Exception {
		List<XPEDXItem> xItems = convertInputsToXpedxItems();

		List<String> itemIds = new ArrayList<String>(xItems.size());
		for (XPEDXItem xItem : xItems) {
			itemIds.add(xItem.getLegacyProductCode());
		}

		priceAndAvailability = XPEDXPriceandAvailabilityUtil.getPriceAndAvailability(xItems);


		pricingInfo = XPEDXPriceandAvailabilityUtil.getPricingInfoFromItemDetails(priceAndAvailability.getItems(), wcContext);

		XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean) XPEDXWCUtils.getObjectFromCache("XPEDX_Customer_Contact_Info_Bean");
		userHasViewPricesRole = "Y".equals(xpedxCustomerContactInfoBean.getExtnViewPricesFlag());

		lineErrorMessages = XPEDXPriceandAvailabilityUtil.getLineErrorMessageMap(priceAndAvailability.getItems());
		lineStatusCode = XPEDXPriceandAvailabilityUtil.getLineStatusCodeMap(priceAndAvailability.getItems());

		divisionName = XPEDXWCUtils.getDivisionName();

		uomDescriptions = new LinkedHashMap<String, String>();
		for (XPEDXItem xItem : priceAndAvailability.getItems()) {
			if (xItem.getRequestedQtyUOM() != null) {
				uomDescriptions.put(xItem.getRequestedQtyUOM(), XPEDXWCUtils.getUOMDescription(xItem.getRequestedQtyUOM()));
			}
			if (xItem.getOrderMultipleUOM() != null) {
				uomDescriptions.put(xItem.getOrderMultipleUOM(), XPEDXWCUtils.getUOMDescription(xItem.getOrderMultipleUOM()));
			}
		}
		

		itemCategories = new LinkedHashMap<String, String>();
		// TODO need to pull PriceInfoDoc?

		return SUCCESS;
	}

	/**
	 * @throws IllegalArgumentException If missing a required parameter
	 */
	private void assertInputs() {
		if (items == null) {
			throw new IllegalArgumentException("Missing parameter: items");
		} else if (qtys == null) {
			throw new IllegalArgumentException("Missing parameter: qtys");
		} else if (uoms == null) {
			throw new IllegalArgumentException("Missing parameter: uoms");
		}
	}

	/**
	 * @param param asterisk-separate value (eg, "1*2*3")
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
	 * @return
	 * @throws IllegalArgumentException If number of elements in asterisk-separated inputs do not match.
	 */
	private List<XPEDXItem> convertInputsToXpedxItems() {
		assertInputs();

		String[] listItems = parseParam(items);
		String[] listQtys = parseParam(qtys);
		String[] listUoms = parseParam(uoms);

		if (listQtys.length != listItems.length) {
			throw new IllegalArgumentException("qtys has " + listQtys.length + " elements, but items has " + listItems.length + " elements");
		}
		if (listUoms.length != listItems.length) {
			throw new IllegalArgumentException("uoms has " + listUoms.length + " elements, but items has " + listItems.length + " elements");
		}

		List<XPEDXItem> rows = new ArrayList<XPEDXItem>(listItems.length);
		int itemLineNumber = 1;
		for (int i = 0; i < listItems.length; i++) {
			XPEDXItem tmpItem = new XPEDXItem();
			tmpItem.setLegacyProductCode(listItems[i]);
			tmpItem.setRequestedQty(listQtys[i]);
			tmpItem.setRequestedQtyUOM(listUoms[i]);
			tmpItem.setLineNumber(String.valueOf(itemLineNumber++));
			rows.add(tmpItem);
		}
		return rows;
	}

	// --- Inputs and Outputs

	public void setItems(String items) {
		this.items = items;
	}

	public void setQtys(String qtys) {
		this.qtys = qtys;
	}

	public void setUoms(String uoms) {
		this.uoms = uoms;
	}

	public XPEDXPriceAndAvailability getPriceAndAvailability() {
		return priceAndAvailability;
	}

	public Map<String, XPEDXItemPricingInfo> getPricingInfo() {
		return pricingInfo;
	}

	public boolean isUserHasViewPricesRole() {
		return userHasViewPricesRole;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public Map<String, String> getUomDescriptions() {
		return uomDescriptions;
	}

	public Map<String, String> getItemCategories() {
		return itemCategories;
	}

	public Map<String, String> getLineErrorMessages() {
		return lineErrorMessages;
	}

	public Map<String, String> getLineStatusCode() {
		return lineStatusCode;
	}

	public void setLineStatusCode(Map<String, String> lineStatusCode) {
		this.lineStatusCode = lineStatusCode;
	}
	
	

}

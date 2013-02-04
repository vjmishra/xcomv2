/**
 * This class holds Item bean of Price and Availability
 */
package com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability;

import java.util.Vector;

/**
 * @author rugrani
 *
 */
public class XPEDXItem {
	
	private String lineNumber;
	private String legacyProductCode;
	private String requestedQtyUOM;
	private String requestedQty;
	private String purchaseOrderQty;
	private String pricingUOM;
	private String priceCurrencyCode;
	private String unitPricePerPricingUOM;
	private String unitPricePerRequestedUOM;
	private String extendedPrice;
	private String itemCost;
	private String costCurrencyCode;
	private String lineStatusCode;
	private Vector brackets;
	private Vector warehouseLocationList;
	//added for jira 2885
	private String lineStatusErrorMsg;
	private String orderMultipleQty;
	private String orderMultipleUOM;
	
	public String getLineStatusErrorMsg() {
		return lineStatusErrorMsg;
	}

	public void setLineStatusErrorMsg(String lineStatusErrorMsg) {
		this.lineStatusErrorMsg = lineStatusErrorMsg;
	}

	/**
	 * Constructor
	 */
	public XPEDXItem() {
		brackets = new Vector();
		warehouseLocationList = new Vector();
	}
	
	public void addBrackets(XPEDXBracket bracket){
		brackets.addElement(bracket);
	}
	
	public void addWarehouseLocations(XPEDXWarehouseLocation warehouseLocation){
		warehouseLocationList.addElement(warehouseLocation);
	}
	/**
	 * @return the costCurrencyCode
	 */
	public String getCostCurrencyCode() {
		return costCurrencyCode;
	}
	/**
	 * @param costCurrencyCode the costCurrencyCode to set
	 */
	public void setCostCurrencyCode(String costCurrencyCode) {
		this.costCurrencyCode = costCurrencyCode;
	}
	/**
	 * @return the extendedPrice
	 */
	public String getExtendedPrice() {
		return extendedPrice;
	}
	/**
	 * @param extendedPrice the extendedPrice to set
	 */
	public void setExtendedPrice(String extendedPrice) {
		this.extendedPrice = extendedPrice;
	}
	/**
	 * @return the itemCost
	 */
	public String getItemCost() {
		return itemCost;
	}
	/**
	 * @param itemCost the itemCost to set
	 */
	public void setItemCost(String itemCost) {
		this.itemCost = itemCost;
	}
	/**
	 * @return the legacyProductCode
	 */
	public String getLegacyProductCode() {
		return legacyProductCode;
	}
	/**
	 * @param legacyProductCode the legacyProductCode to set
	 */
	public void setLegacyProductCode(String legacyProductCode) {
		this.legacyProductCode = legacyProductCode;
	}
	/**
	 * @return the lineNumber
	 */
	public String getLineNumber() {
		return lineNumber;
	}
	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	/**
	 * @return the lineStatusCode
	 */
	public String getLineStatusCode() {
		return lineStatusCode;
	}
	/**
	 * @param lineStatusCode the lineStatusCode to set
	 */
	public void setLineStatusCode(String lineStatusCode) {
		this.lineStatusCode = lineStatusCode;
	}
	/**
	 * @return the priceCurrencyCode
	 */
	public String getPriceCurrencyCode() {
		return priceCurrencyCode;
	}
	/**
	 * @param priceCurrencyCode the priceCurrencyCode to set
	 */
	public void setPriceCurrencyCode(String priceCurrencyCode) {
		this.priceCurrencyCode = priceCurrencyCode;
	}
	/**
	 * @return the pricingUOM
	 */
	public String getPricingUOM() {
		return pricingUOM;
	}
	/**
	 * @param pricingUOM the pricingUOM to set
	 */
	public void setPricingUOM(String pricingUOM) {
		this.pricingUOM = pricingUOM;
	}
	/**
	 * @return the purchaseOrderQty
	 */
	public String getPurchaseOrderQty() {
		return purchaseOrderQty;
	}
	/**
	 * @param purchaseOrderQty the purchaseOrderQty to set
	 */
	public void setPurchaseOrderQty(String purchaseOrderQty) {
		this.purchaseOrderQty = purchaseOrderQty;
	}
	/**
	 * @return the requestedQty
	 */
	public String getRequestedQty() {
		return requestedQty;
	}
	/**
	 * @param requestedQty the requestedQty to set
	 */
	public void setRequestedQty(String requestedQty) {
		this.requestedQty = requestedQty;
	}
	/**
	 * @return the requestedQtyUOM
	 */
	public String getRequestedQtyUOM() {
		return requestedQtyUOM;
	}
	/**
	 * @param requestedQtyUOM the requestedQtyUOM to set
	 */
	public void setRequestedQtyUOM(String requestedQtyUOM) {
		this.requestedQtyUOM = requestedQtyUOM;
	}
	/**
	 * @return the unitPricePerPricingUOM
	 */
	public String getUnitPricePerPricingUOM() {
		return unitPricePerPricingUOM;
	}
	/**
	 * @param unitPricePerPricingUOM the unitPricePerPricingUOM to set
	 */
	public void setUnitPricePerPricingUOM(String unitPricePerPricingUOM) {
		this.unitPricePerPricingUOM = unitPricePerPricingUOM;
	}
	/**
	 * @return the unitPricePerRequestedUOM
	 */
	public String getUnitPricePerRequestedUOM() {
		return unitPricePerRequestedUOM;
	}
	/**
	 * @param unitPricePerRequestedUOM the unitPricePerRequestedUOM to set
	 */
	public void setUnitPricePerRequestedUOM(String unitPricePerRequestedUOM) {
		this.unitPricePerRequestedUOM = unitPricePerRequestedUOM;
	}
	/**
	 * @return the brackets
	 */
	public Vector getBrackets() {
		return brackets;
	}
	/**
	 * @return the warehouseLocationList
	 */
	public Vector getWarehouseLocationList() {
		return warehouseLocationList;
	}
	
	public String getOrderMultipleQty() {
		return orderMultipleQty;
	}

	public void setOrderMultipleQty(String orderMultipleQty) {
		this.orderMultipleQty = orderMultipleQty;
	}

	public String getOrderMultipleUOM() {
		return orderMultipleUOM;
	}

	public void setOrderMultipleUOM(String orderMultipleUOM) {
		this.orderMultipleUOM = orderMultipleUOM;
	}

}

/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.catalog;

/**
 * @author RUgrani
 *This PoJo holds information about item branch information(from the xpx_item_extn table) for a item, 
 *for the customers ship from division
 */
public class XPEDXItemBranchInfoBean {
	private String itemID = "";
	private String environmentID = "";
	private String companyCode = "";
	private String legacyPartNo = "";
	private String division = "";
	private String itemStockStatus = "";
	private String orderMultiple = "";
	private String inventoryIndicator = "";
	/**
	 * @param itemID
	 * @param environmentID
	 * @param companyCode
	 * @param legacyPartNo
	 * @param division
	 * @param itemStockStatus
	 * @param orderMultiple
	 * @param inventoryIndicator
	 */
	public XPEDXItemBranchInfoBean(String itemID, String environmentID, String companyCode, String legacyPartNo, String division, 
			String itemStockStatus, String orderMultiple, String inventoryIndicator) {
		super();
		this.itemID = itemID;
		this.environmentID = environmentID;
		this.companyCode = companyCode;
		this.legacyPartNo = legacyPartNo;
		this.division = division;
		this.itemStockStatus = itemStockStatus;
		this.orderMultiple = orderMultiple;
		this.inventoryIndicator = inventoryIndicator;
	}
	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}
	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	/**
	 * @return the division
	 */
	public String getDivision() {
		return division;
	}
	/**
	 * @param division the division to set
	 */
	public void setDivision(String division) {
		this.division = division;
	}
	/**
	 * @return the environmentID
	 */
	public String getEnvironmentID() {
		return environmentID;
	}
	/**
	 * @param environmentID the environmentID to set
	 */
	public void setEnvironmentID(String environmentID) {
		this.environmentID = environmentID;
	}
	/**
	 * @return the inventoryIndicator
	 */
	public String getInventoryIndicator() {
		return inventoryIndicator;
	}
	/**
	 * @param inventoryIndicator the inventoryIndicator to set
	 */
	public void setInventoryIndicator(String inventoryIndicator) {
		this.inventoryIndicator = inventoryIndicator;
	}
	/**
	 * @return the itemID
	 */
	public String getItemID() {
		return itemID;
	}
	/**
	 * @param itemID the itemID to set
	 */
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}
	/**
	 * @return the itemStockStatus
	 */
	public String getItemStockStatus() {
		return itemStockStatus;
	}
	/**
	 * @param itemStockStatus the itemStockStatus to set
	 */
	public void setItemStockStatus(String itemStockStatus) {
		this.itemStockStatus = itemStockStatus;
	}
	/**
	 * @return the legacyPartNo
	 */
	public String getLegacyPartNo() {
		return legacyPartNo;
	}
	/**
	 * @param legacyPartNo the legacyPartNo to set
	 */
	public void setLegacyPartNo(String legacyPartNo) {
		this.legacyPartNo = legacyPartNo;
	}
	/**
	 * @return the orderMultiple
	 */
	public String getOrderMultiple() {
		return orderMultiple;
	}
	/**
	 * @param orderMultiple the orderMultiple to set
	 */
	public void setOrderMultiple(String orderMultiple) {
		this.orderMultiple = orderMultiple;
	}

}

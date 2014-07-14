package com.xpedx.nextgen.priceAndAvailabilityService;

public class XPXWarehouseLocation {
	private String warehouse;
	private String availableQty;
	private String numberOfDays;
	/**
	 * @return the availableQty
	 */
	public String getAvailableQty() {
		return availableQty;
	}
	/**
	 * @param availableQty the availableQty to set
	 */
	public void setAvailableQty(String availableQty) {
		this.availableQty = availableQty;
	}
	/**
	 * @return the numberOfDays
	 */
	public String getNumberOfDays() {
		return numberOfDays;
	}
	/**
	 * @param numberOfDays the numberOfDays to set
	 */
	public void setNumberOfDays(String numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	/**
	 * @return the warehouse
	 */
	public String getWarehouse() {
		return warehouse;
	}
	/**
	 * @param warehouse the warehouse to set
	 */
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
}

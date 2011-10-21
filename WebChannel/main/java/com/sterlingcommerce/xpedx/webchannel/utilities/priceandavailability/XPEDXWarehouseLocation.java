/**
 * This class holds the WarehouseLocation bean of Price and Availability
 */
package com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability;

/**
 * @author rugrani
 *
 */
public class XPEDXWarehouseLocation {
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

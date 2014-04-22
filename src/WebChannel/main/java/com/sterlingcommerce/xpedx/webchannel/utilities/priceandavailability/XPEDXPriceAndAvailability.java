/**
 * This class holds all the attributes of the price and availability bean
 */
package com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability;

import java.util.Vector;

/**
 * @author rugrani
 *
 */
public class XPEDXPriceAndAvailability {
	private String transactionStatus;
	private String environmentId;
	private String company;
	private String customerBranch;
	private String customerNumber;
	private String shipToSuffix;
	private String orderBranch;
	private String headerStatusCode;
	private Vector<XPEDXItem> items;
	private String statusVerboseMsg;
	private String lineItemsVerboseMsg;


	/**
	 * Constructor
	 */
	public XPEDXPriceAndAvailability() {
		items = new Vector<XPEDXItem>();
	}

	public void addItem(XPEDXItem item){
		items.addElement(item);
	}

	public void setItems(Vector<XPEDXItem> items) {
		this.items = items;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * @return the customerBranch
	 */
	public String getCustomerBranch() {
		return customerBranch;
	}
	/**
	 * @param customerBranch the customerBranch to set
	 */
	public void setCustomerBranch(String customerBranch) {
		this.customerBranch = customerBranch;
	}
	/**
	 * @return the customerNumber
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}
	/**
	 * @param customerNumber the customerNumber to set
	 */
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	/**
	 * @return the environmentId
	 */
	public String getEnvironmentId() {
		return environmentId;
	}
	/**
	 * @param environmentId the environmentId to set
	 */
	public void setEnvironmentId(String environmentId) {
		this.environmentId = environmentId;
	}
	/**
	 * @return the headerStatusCode
	 */
	public String getHeaderStatusCode() {
		return headerStatusCode;
	}
	/**
	 * @param headerStatusCode the headerStatusCode to set
	 */
	public void setHeaderStatusCode(String headerStatusCode) {
		this.headerStatusCode = headerStatusCode;
	}
	/**
	 * @return the orderBranch
	 */
	public String getOrderBranch() {
		return orderBranch;
	}
	/**
	 * @param orderBranch the orderBranch to set
	 */
	public void setOrderBranch(String orderBranch) {
		this.orderBranch = orderBranch;
	}
	/**
	 * @return the shipToSuffix
	 */
	public String getShipToSuffix() {
		return shipToSuffix;
	}
	/**
	 * @param shipToSuffix the shipToSuffix to set
	 */
	public void setShipToSuffix(String shipToSuffix) {
		this.shipToSuffix = shipToSuffix;
	}
	/**
	 * @return the transactionStatus
	 */
	public String getTransactionStatus() {
		return transactionStatus;
	}
	/**
	 * @param transactionStatus the transactionStatus to set
	 */
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	/**
	 * @return the items
	 */
	public Vector<XPEDXItem> getItems() {
		return items;
	}

	/**
	 *
	 * @return statusVerboseMsg
	 */
	public String getStatusVerboseMsg() {
		return statusVerboseMsg;
	}
	/**
	 *
	 * @param set User Display message based on status of PriceAndAvailability WebserviceCall
	 */
	public void setStatusVerboseMsg(String statusVerboseMsg) {
		this.statusVerboseMsg = statusVerboseMsg;
	}

	/*
	 * LineItems Verbose Message
	 */
	public String getLineItemsVerboseMsg() {
		return lineItemsVerboseMsg;
	}

	public void setLineItemsVerboseMsg(String lineItemsVerboseMsg) {
		this.lineItemsVerboseMsg = lineItemsVerboseMsg;
	}

}

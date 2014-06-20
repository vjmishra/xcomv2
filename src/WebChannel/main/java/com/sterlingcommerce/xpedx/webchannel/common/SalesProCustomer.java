package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.Comparator;

public class SalesProCustomer  {

	/**
	 * Sort by customerName
	 */
	public static final Comparator<SalesProCustomer> COMPARATOR_DISPLAY = new Comparator<SalesProCustomer>() {
		@Override
		public int compare(SalesProCustomer o1, SalesProCustomer o2) {
			return o1.getCustomerName().compareToIgnoreCase(o2.getCustomerName());
		}
	};

	private String customerName;
	private String customerId;
	private String customerNo;
	private String storefrontID;
	private String legacyCustomerNo;
	private String firstName;
	private String lastName;
	private String UserName=null;
	private String SalesRepID;
	private String salesRepEmailID;

	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getStorefrontID() {
		return storefrontID;
	}
	public void setStorefrontID(String storefrontID) {
		this.storefrontID = storefrontID;
	}
	public String getLegacyCustomerNo() {
		return legacyCustomerNo;
	}
	public void setLegacyCustomerNo(String legacyCustomerNo) {
		this.legacyCustomerNo = legacyCustomerNo;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getSalesRepID() {
		return SalesRepID;
	}
	public void setSalesRepID(String salesRepID) {
		SalesRepID = salesRepID;
	}
	public String getSalesRepEmailID() {
		return salesRepEmailID;
	}
	public void setSalesRepEmailID(String salesRepEmailID) {
		this.salesRepEmailID = salesRepEmailID;
	}
}

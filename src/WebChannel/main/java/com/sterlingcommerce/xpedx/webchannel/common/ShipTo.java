package com.sterlingcommerce.xpedx.webchannel.common;


public class ShipTo implements Comparable<ShipTo>{

	private String userId;
	private String shipToCustomerID;
	private String enterpriseCode;
	private String firstName;
	private String lastName;
	private String billToCustomerID;
	private String sAPCustomerID;
	private String mSAPCustomerID;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private String shipToCustomerName;
	private String status;
	private String shipToDisplayString;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getShipToCustomerID() {
		return shipToCustomerID;
	}
	public void setShipToCustomerID(String shipToCustomerID) {
		this.shipToCustomerID = shipToCustomerID;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
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
	public String getBillToCustomerID() {
		return billToCustomerID;
	}
	public void setBillToCustomerID(String billToCustomerID) {
		this.billToCustomerID = billToCustomerID;
	}
	public String getSAPCustomerID() {
		return sAPCustomerID;
	}
	public void setSAPCustomerID(String sAPCustomerID) {
		this.sAPCustomerID = sAPCustomerID;
	}
	public String getMSAPCustomerID() {
		return mSAPCustomerID;
	}
	public void setMSAPCustomerID(String mSAPCustomerID) {
		this.mSAPCustomerID = mSAPCustomerID;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getAddressLine3() {
		return addressLine3;
	}
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getShipToCustomerName() {
		return shipToCustomerName;
	}
	public void setShipToCustomerName(String shipToCustomerName) {
		this.shipToCustomerName = shipToCustomerName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getShipToDisplayString() {
		return shipToDisplayString;
	}
	public void setShipToDisplayString(String shipToDisplayString) {
		this.shipToDisplayString = shipToDisplayString;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((shipToDisplayString == null) ? 0 : shipToDisplayString
						.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShipTo other = (ShipTo) obj;
		if (shipToDisplayString == null) {
			if (other.shipToDisplayString != null)
				return false;
		} else if (!shipToDisplayString.equals(other.shipToDisplayString))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(ShipTo obj) {
		return this.getShipToDisplayString().compareToIgnoreCase(obj.getShipToDisplayString());
	}
}

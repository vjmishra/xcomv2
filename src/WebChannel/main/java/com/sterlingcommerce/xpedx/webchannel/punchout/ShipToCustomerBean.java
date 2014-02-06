package com.sterlingcommerce.xpedx.webchannel.punchout;

public class ShipToCustomerBean implements Comparable<ShipToCustomerBean> {

	private String userId;
	private String shipToCustomerID;
	private String enterpriseCode;
	private String firstName;
	private String lastName;
	private String billToCustomerID;
	private String sAPCustomerID;
	private String mSAPCustomerID;
	private String shipToExtnCustStoreNo;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private String emailID;
	private String shipToCustomerName;
	private String billToCustomerName;
	private String shipToAddressString;
	private String status;
	private String extnCustomerDivisionID;
	private String divisionName;
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
	public String getShipToExtnCustStoreNo() {
		return shipToExtnCustStoreNo;
	}
	public void setShipToExtnCustStoreNo(String shipToExtnCustStoreNo) {
		this.shipToExtnCustStoreNo = shipToExtnCustStoreNo;
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
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getShipToCustomerName() {
		return shipToCustomerName;
	}
	public void setShipToCustomerName(String shipToCustomerName) {
		this.shipToCustomerName = shipToCustomerName;
	}
	public String getBillToCustomerName() {
		return billToCustomerName;
	}
	public void setBillToCustomerName(String billToCustomerName) {
		this.billToCustomerName = billToCustomerName;
	}
	public String getShipToAddressString() {
		return shipToAddressString;
	}
	public void setShipToAddressString(String shipToAddressString) {
		this.shipToAddressString = shipToAddressString;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExtnCustomerDivisionID() {
		return extnCustomerDivisionID;
	}
	public String getDivisionName() {
		return divisionName;
	}	
	public String getShipToDisplayString() {
		return shipToDisplayString;
	}
	public void setShipToDisplayString(String shipToDisplayString) {
		this.shipToDisplayString = shipToDisplayString;
	}
	@Override
	public int compareTo(ShipToCustomerBean obj) {
		return this.getShipToDisplayString().compareToIgnoreCase(obj.getShipToDisplayString());
	}
	@Override 
	public boolean equals(Object obj) {		
		if (!(obj instanceof ShipToCustomerBean)) return false;
		ShipToCustomerBean objShipToCustomerBean = (ShipToCustomerBean)obj;
		return this.getShipToDisplayString().equalsIgnoreCase(objShipToCustomerBean.getShipToDisplayString());
	}
}

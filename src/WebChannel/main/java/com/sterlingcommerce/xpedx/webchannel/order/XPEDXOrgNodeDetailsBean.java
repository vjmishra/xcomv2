package com.sterlingcommerce.xpedx.webchannel.order;

/**
 * Class that holds the details of the Organization Node, used in Order Review
 * to display Ship From Division details.
 * 
 * @author Sterling
 * 
 */
public class XPEDXOrgNodeDetailsBean {

	protected String orgName;

	protected String addressLineOne;

	protected String addressLineTwo;

	protected String addressLineThree;

	protected String addressLineFour;

	protected String addressLineFive;

	protected String addressLineSix;

	protected String city;

	protected String country;

	protected String emailId;

	protected String firstName;

	protected String lastName;

	protected String state;

	protected String zipCode;

	public String getAddressLineOne() {
		return addressLineOne;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getAddressLineFive() {
		return addressLineFive;
	}

	public String getAddressLineFour() {
		return addressLineFour;
	}

	public String getAddressLineSix() {
		return addressLineSix;
	}

	public String getAddressLineThree() {
		return addressLineThree;
	}

	public String getAddressLineTwo() {
		return addressLineTwo;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getState() {
		return state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setAddressLineFive(String addressLineFive) {
		this.addressLineFive = addressLineFive;
	}

	public void setAddressLineFour(String addressLineFour) {
		this.addressLineFour = addressLineFour;
	}

	public void setAddressLineSix(String addressLineSix) {
		this.addressLineSix = addressLineSix;
	}

	public void setAddressLineThree(String addressLineThree) {
		this.addressLineThree = addressLineThree;
	}

	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}

package com.sterlingcommerce.xpedx.webchannel.punchout;

public class PunchoutRequest {

	private String loginID;
	private String sfId;
	private boolean isCXML;
	private String payLoadID;
	private String operation;
	private String orderHeaderKey;
	private String returnURL;
	private String selectedCategory;
	private String selectedItem;
	private String selectedItemUOM;
	private String buyerCookie;
	private String fromIdentity;
	private String toIdentity;
	private String isProcurementUser;


	public String getLoginID() {
		return loginID;
	}
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}
	public String getSfId() {
		return sfId;
	}
	public void setSfId(String sfId) {
		this.sfId = sfId;
	}
	public boolean isCXML() {
		return isCXML;
	}
	public void setCXML(boolean isCXML) {
		this.isCXML = isCXML;
	}
	public String getPayLoadID() {
		return payLoadID;
	}
	public void setPayLoadID(String payLoadID) {
		this.payLoadID = payLoadID;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getOrderHeaderKey() {
		return orderHeaderKey;
	}
	public void setOrderHeaderKey(String orderHeaderKey) {
		this.orderHeaderKey = orderHeaderKey;
	}
	public String getReturnURL() {
		return returnURL;
	}
	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}
	public String getSelectedCategory() {
		return selectedCategory;
	}
	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}
	public String getSelectedItem() {
		return selectedItem;
	}
	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}
	public String getSelectedItemUOM() {
		return selectedItemUOM;
	}
	public void setSelectedItemUOM(String selectedItemUOM) {
		this.selectedItemUOM = selectedItemUOM;
	}
	public String getBuyerCookie() {
		return buyerCookie;
	}
	public void setBuyerCookie(String buyerCookie) {
		this.buyerCookie = buyerCookie;
	}
	public String getFromIdentity() {
		return fromIdentity;
	}
	public void setFromIdentity(String fromIdentity) {
		this.fromIdentity = fromIdentity;
	}
	public String getToIdentity() {
		return toIdentity;
	}
	public void setToIdentity(String toIdentity) {
		this.toIdentity = toIdentity;
	}
	public String getIsProcurementUser() {
		return isProcurementUser;
	}
	public void setIsProcurementUser(String isProcurementUser) {
		this.isProcurementUser = isProcurementUser;
	}

}

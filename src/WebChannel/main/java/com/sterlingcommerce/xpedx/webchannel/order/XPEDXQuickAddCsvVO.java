package com.sterlingcommerce.xpedx.webchannel.order;



public class XPEDXQuickAddCsvVO {
	private String itemId="";
	private String qty = "";
	private String itemPONumber="";
	private String itemLineNumber="";
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getItemPONumber() {
		return itemPONumber;
	}
	public void setItemPONumber(String itemPONumber) {
		this.itemPONumber = itemPONumber;
	}
	public String getItemLineNumber() {
		return itemLineNumber;
	}
	public void setItemLineNumber(String itemLineNumber) {
		this.itemLineNumber = itemLineNumber;
	}
}

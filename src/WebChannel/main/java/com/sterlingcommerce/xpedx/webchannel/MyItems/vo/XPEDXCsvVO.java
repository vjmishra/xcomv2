package com.sterlingcommerce.xpedx.webchannel.MyItems.vo;

import java.util.HashMap;

public class XPEDXCsvVO {
	
	private String CustomerPartNumber = "";
	private String SupplierPartNumber = "";
	//XB-56 Start
	private String MfgItemNumber = "";
	//XB-56 End
	private String Qty = "";
	private String UOM = "";
	private HashMap<String, String> customFields = new HashMap<String, String>();
	private String LineLevelCode = "";
	private String Description = "";
	
	//XB-56 - Start
	public String getMfgItemNumber() {
		return MfgItemNumber;
	}
	public void setMfgItemNumber(String mfgItemNumber) {
		MfgItemNumber = mfgItemNumber;
	}
	//XB-56 - End
	public String getCustomerPartNumber() {
		return CustomerPartNumber;
	}
	public void setCustomerPartNumber(String customerPartNumber) {
		CustomerPartNumber = customerPartNumber;
	}
	public String getSupplierPartNumber() {
		return SupplierPartNumber;
	}
	public void setSupplierPartNumber(String supplierPartNumber) {
		SupplierPartNumber = supplierPartNumber;
	}
	public String getQty() {
		return Qty;
	}
	public void setQty(String qty) {
		Qty = qty;
	}
	public String getUOM() {
		return UOM;
	}
	public void setUOM(String uom) {
		UOM = uom;
	}
	public String getLineLevelCode() {
		return LineLevelCode;
	}
	public void setLineLevelCode(String lineLevelCode) {
		LineLevelCode = lineLevelCode;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		try {
			res.append("Customer Part Number: ").append(getCustomerPartNumber()).append(", ");
			res.append("Supplier Part Number: ").append(getSupplierPartNumber()).append(", ");
			// XB - 56
			res.append("Manufacturer Item Number: ").append(getMfgItemNumber()).append(", ");
			//XB-56 End
			res.append("Quantity: ").append(getQty()).append(", ");
			res.append("UOM: ").append(getUOM()).append(", ");
			res.append("Line Level Code: ").append(getLineLevelCode()).append(", ");
			res.append("Description: ").append(getDescription());
			
		} catch (Exception e) {
			res = new StringBuilder();
		}
		return res.toString();
	}

	public HashMap<String, String> getCustomFields() {
		return customFields;
	}
	
	public void setCustomFields(HashMap<String, String> customFields) {
		this.customFields = customFields;
	}
}

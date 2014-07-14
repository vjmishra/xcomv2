package com.sterlingcommerce.xpedx.webchannel.punchout;

import java.util.ArrayList;

public class DivisionBean implements Comparable<DivisionBean> {
	
	private String divisionId;
	private String divisionName;
	private  ArrayList<ShipToCustomerBean> shipToCustomrs;
	
	public String getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public ArrayList<ShipToCustomerBean> getShipToCustomrs() {
		return shipToCustomrs;
	}
	public void setShipToCustomrs(ArrayList<ShipToCustomerBean> shipToCustomrs) {
		this.shipToCustomrs = shipToCustomrs;
	}	
	@Override
	public int compareTo(DivisionBean obj) {
		return this.getDivisionName().compareToIgnoreCase(obj.getDivisionName());
	}	
	@Override 
	public boolean equals(Object obj) {		
		if (!(obj instanceof DivisionBean)) return false;
		DivisionBean objDivisionBean = (DivisionBean) obj;
		return this.getDivisionName().equalsIgnoreCase(objDivisionBean.getDivisionName());
	}
}

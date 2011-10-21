package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.Comparator;

public class XPEDXShipToComparator implements Comparator{
	
	public int compare(Object shipToCustomerID1, Object shipToCustomerID2) {
		String shipToCustomerID1suffix = (String) getShipToSuffix(shipToCustomerID1);

		String shipToCustomerID2suffix = (String) getShipToSuffix(shipToCustomerID2);
		
		Double shipCust1Suffix = Double.valueOf(shipToCustomerID1suffix);
		
		Double shipCust2Suffix = Double.valueOf(shipToCustomerID2suffix);

		if( shipCust1Suffix > shipCust2Suffix )
		return 1;

		else if( shipCust1Suffix < shipCust2Suffix )
		return -1;

		else
		return 0;
	}
	
	public Object getShipToSuffix(Object shipToCustomerID){
		if (shipToCustomerID != null) {
			String customerID = (String) shipToCustomerID;
			String[] customerTokens = customerID.split("-");
			String customerSuffix = customerTokens[0]+customerTokens[1]+customerTokens[2];
			return customerSuffix;
		}
		return shipToCustomerID;
	}
}

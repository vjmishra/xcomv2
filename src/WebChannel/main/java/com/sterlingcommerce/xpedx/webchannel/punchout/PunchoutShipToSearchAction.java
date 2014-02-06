package com.sterlingcommerce.xpedx.webchannel.punchout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.util.YFCUtils;

@SuppressWarnings("serial")
public class PunchoutShipToSearchAction extends WCAction {

	@SuppressWarnings("unchecked")
	public String execute(){
		
		if (!YFCUtils.isVoid(searchString)) {
			ArrayList<DivisionBean> divisionBeanTempList = (ArrayList<DivisionBean>)XPEDXWCUtils.getObjectFromCache("divisionBeanList");
			if (divisionBeanTempList != null && divisionBeanTempList.size() >0) {
				Map<String, DivisionBean> searchDivisionBeanMap = new HashMap<String, DivisionBean>();
				for (Iterator<DivisionBean> it = divisionBeanTempList.iterator(); it.hasNext();) {
					DivisionBean divisionBean = it.next();					
					DivisionBean searchDivisionBean;
					ArrayList<ShipToCustomerBean> shipToCustomerBeanList = divisionBean.getShipToCustomrs();
						if (shipToCustomerBeanList != null && shipToCustomerBeanList.size() >0) {
							ArrayList<ShipToCustomerBean> searchShipToCustomerBeanList = null;
							
							for (Iterator<ShipToCustomerBean> it1 = shipToCustomerBeanList.iterator(); it1.hasNext();) {
								ShipToCustomerBean shipToCustomerBean = it1.next();
								String shipToDisplayString = shipToCustomerBean.getShipToDisplayString();
								 	if(containsIgnoreCase(shipToDisplayString,searchString)){	
								 		if(searchShipToCustomerBeanList == null){
								 			searchShipToCustomerBeanList = new ArrayList<ShipToCustomerBean>();
								 		}
								 		searchShipToCustomerBeanList.add(shipToCustomerBean);								 		
								 	}
								}
								 	if(searchShipToCustomerBeanList != null){
								 		searchDivisionBean = searchDivisionBeanMap.get(divisionBean.getDivisionId());
								 		if (searchDivisionBean == null) {
								 			searchDivisionBean = new DivisionBean();
								 			searchDivisionBean.setDivisionId(divisionBean.getDivisionId());
								 			searchDivisionBean.setDivisionName(divisionBean.getDivisionName());
										}
									 	Collections.sort(searchShipToCustomerBeanList);
								 		searchDivisionBean.setShipToCustomrs(searchShipToCustomerBeanList);
								 		searchDivisionBeanMap.put(divisionBean.getDivisionId(), searchDivisionBean);
								 	}
						}					
				}
				if(searchDivisionBeanMap!=null){
					divisionBeanList = new ArrayList<DivisionBean>(searchDivisionBeanMap.values());
					Collections.sort(divisionBeanList);					
				}
			}
		}else{
			divisionBeanList = (ArrayList<DivisionBean>)XPEDXWCUtils.getObjectFromCache("divisionBeanList");
		}
		
		return "punchout";
	}
	
	private String searchString;
	private ArrayList<DivisionBean> divisionBeanList;
	
	public ArrayList<DivisionBean> getDivisionBeanList() {
		return divisionBeanList;
	}
	public void setDivisionBeanList(ArrayList<DivisionBean> divisionBeanList) {
		this.divisionBeanList = divisionBeanList;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	
	public boolean containsIgnoreCase(final String shipToDisplayString, final String searchString) {
			   if (shipToDisplayString == null || searchString == null) {
			        return false;
			    }
			   if(shipToDisplayString.toUpperCase().contains(searchString.trim().toUpperCase())){
				   return true;
			   }
			   if(shipToDisplayString.toLowerCase().contains(searchString.trim().toLowerCase())){
				   return true;
			   }
		       return false;
		   }
}

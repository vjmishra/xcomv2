package com.sterlingcommerce.xpedx.webchannel.services;

import java.util.ArrayList;
import java.util.List;

import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

@SuppressWarnings("serial")
public class XPEDXServicesAction extends WCMashupAction {
	
	List<String> serviceProviderList;
	List<String> addressList;
	//Webtrends tag start
	private boolean samplePageMetaTag = false;
	
	public boolean issamplePageMetaTag() {
		return samplePageMetaTag;
	}

	public void setsamplePageMetaTag(boolean samplePageMetaTag) {
		this.samplePageMetaTag = samplePageMetaTag;
	}
	
	//Webtrends tag end
	public List<String> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<String> addressList) {
		this.addressList = addressList;
	}
	
	public void setServiceProviderList(List<String> serviceProviderList) {
		this.serviceProviderList = serviceProviderList;
	}
	
	public List<String> getServiceProviderList() {
		return this.serviceProviderList;
	}

	@Override
	public String execute() {
		
		XPEDXShipToCustomer shipToCustomer = null;
		List<String> customerAddressList = null;
		String customerId = getWCContext().getCustomerId();
		String storefrontId =  getWCContext().getStorefrontId();
		
		try {
			shipToCustomer = XPEDXWCUtils.getShipToAdress(customerId, storefrontId);
		} catch (CannotBuildInputException e) {			
		}
		
		if (shipToCustomer != null) {
			customerAddressList = shipToCustomer.getAddressList();			
		}
		
		setAddressList(customerAddressList);
		
		List<String> list = new ArrayList<String>();
		list.add("FedEx");
		list.add("UPS");
		list.add("None");
		setServiceProviderList(list);	
		//Webrtends	tag start
					setsamplePageMetaTag(true);
		//Webrtends	tag End
		return SUCCESS;
	}
}

package com.sterlingcommerce.xpedx.webchannel.profile.org;

import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCMashupAction;

public class XPEDXSaveBillToInfo extends WCMashupAction{
	
	private static final String BILL_TO_SHIP_TO_MASHUP = "xpedx-save-billToInfo";
	protected String customerId;
	protected String organizationCode;

	public String execute(){
		try
		{
			Element outputDoc = prepareAndInvokeMashup(BILL_TO_SHIP_TO_MASHUP);
		}
		catch (Exception e) {
			LOG.debug("Error while saving the Bill to cust Info");
			return(ERROR);
		}
		return(SUCCESS);
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

}

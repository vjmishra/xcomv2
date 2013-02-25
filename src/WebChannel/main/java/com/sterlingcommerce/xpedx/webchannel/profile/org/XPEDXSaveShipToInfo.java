package com.sterlingcommerce.xpedx.webchannel.profile.org;

import com.sterlingcommerce.webchannel.core.WCMashupAction;

public class XPEDXSaveShipToInfo extends WCMashupAction {

    /**
     * 
     */
    private static final long serialVersionUID = 9077298526883812055L;
    private static final String BILL_TO_SHIP_TO_MASHUP = "xpedx-save-shipToInfo";
    protected String customerId;
    protected String organizationCode;
    private boolean success;

    public boolean isSuccess() {
	return success;
    }

    public void setSuccess(boolean isSuccess) {
	this.success = isSuccess;
    }

    @Override
    public String execute() {
	try {
	    prepareAndInvokeMashup(BILL_TO_SHIP_TO_MASHUP);
	} catch (Exception e) {
	    LOG.debug("Error while saving the ship to cust Info");
	    return (ERROR);
	}
	setSuccess(true);
	return (SUCCESS);
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
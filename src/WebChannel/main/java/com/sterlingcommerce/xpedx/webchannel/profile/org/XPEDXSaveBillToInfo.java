package com.sterlingcommerce.xpedx.webchannel.profile.org;

import com.sterlingcommerce.webchannel.core.WCMashupAction;

public class XPEDXSaveBillToInfo extends WCMashupAction {

    /**
     * 
     */
    private static final long serialVersionUID = 3144565575483978951L;
    private static final String BILL_TO_SHIP_TO_MASHUP = "xpedx-save-billToInfo";
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
	    LOG.debug("Error while saving the Bill to cust Info");
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

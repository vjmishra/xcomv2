/*
 * Action Class - Save Customer Maintenance Data.
 * 
 * @author - adsouza
 */

package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCMashupAction;

public class XPEDXManageCustMaintenanceInfo extends WCMashupAction {

    /**
     * 
     */
    private static final long serialVersionUID = -8480226520265165972L;

    public XPEDXManageCustMaintenanceInfo() {
	customerOrganizationEle = null;
    }

    public String manageCustMaintenanceInfo() {
	if (log.isDebugEnabled())
	    log.debug("Class:XPEDXManageCustMaintenanceInfo Method:manageCustMaintenanceInfo STARTS");
	String returnStr = updateCustomerDetails("customer-manageCustMaintenanceInfo");
	if (!"success".equals(returnStr))
	    return returnStr;
	if (log.isDebugEnabled())
	    log.debug("Class:XPEDXManageCustMaintenanceInfo Method:manageCustMaintenanceInfo ENDS");
	return "UpdateCustomerCorporateInfo";
    }

    private String updateCustomerDetails(String mashupID) {
	if (log.isDebugEnabled())
	    log.debug("Class:XPEDXManageCustMaintenanceInfo Method:updateCustomerDetails STARTS");
	try {
	    Map outputMaps = prepareAndInvokeMashups();
	    customerOrganizationEle = (Element) outputMaps.get(mashupID);
	} catch (Exception Ex) {
	    getWCContext().getSCUIContext().replaceAttribute(
		    "SCUI_EXCEPTION_ATTR", Ex);
	    return "error";
	}
	if (log.isDebugEnabled())
	    log.debug("Class:XPEDXManageCustMaintenanceInfo Method:updateCustomerDetails END");
	return "success";
    }

    public Element getCustomerOrganizationEle() {
	return customerOrganizationEle;
    }

    public void setCustomerOrganizationEle(Element customerOrganizationEle) {
	this.customerOrganizationEle = customerOrganizationEle;
    }

    /*
     * public String getViewPrices() { return viewPrices; }
     * 
     * public void setViewPrices(String vPrices) { if("true".equals(vPrices))
     * viewPrices= "Y"; }
     * 
     * public String getCanOrder() { return canOrder; }
     * 
     * public void setCanOrder(String cOrder) { if("true".equals(cOrder))
     * canOrder= "Y"; }
     * 
     * public String getCanViewInventory() { return canViewInv; }
     * 
     * public void setCanViewInventory(String cViewInv) {
     * if("true".equals(cViewInv)) canViewInv= "Y"; }
     * 
     * public String getUseCustomerSKU() { return useCustSKU; }
     * 
     * public void setUseCustomerSKU(String uCustSKU) {
     * if("true".equals(uCustSKU)) useCustSKU= "Y"; }
     */
    /*
     * public String getUseOrderMultipleUOM() { return useOrderMulUOM; }
     * 
     * public void setUseOrderMultipleUOM(String uOrderMulUOM) {
     * if("true".equals(uOrderMulUOM)) useOrderMulUOM= "Y"; }
     */

    /*
     * public String getSampleRequestFlag() { return sampleRequestFlag; }
     * 
     * public void setSampleRequestFlag(String srFlag) {
     * if("true".equals(srFlag)) sampleRequestFlag= "Y"; }
     */

    /*
     * public String getEmailViewInvoices() { return emailViewInvoices; }
     * 
     * public void setEmailViewInvoices(String eViewInvoices) {
     * if("true".equals(eViewInvoices)) emailViewInvoices= "Y"; }
     * 
     * public String getPreviewInvoices() { return previewInvoices; }
     * 
     * public void setPreviewInvoices(String pInvoices) {
     * if("true".equals(pInvoices)) previewInvoices= "Y"; }
     * 
     * public String getAcceptBackOrders() { return acceptBackOrders; }
     * 
     * public void setAcceptBackOrders(String aBackOrders) {
     * if("true".equals(aBackOrders)) acceptBackOrders= "Y"; }
     */

    public String getCanViewInvoice() {
	return canViewInvoice;
    }

    public void setCanViewInvoice(String viewInvoice) {
	if ("true".equals(viewInvoice))
	    canViewInvoice = "Y";
    }

    public String getAcceptShipToOverrides() {
	return acceptSTOverrides;
    }

    public void setAcceptShipToOverrides(String aSTOverrides) {
	if ("true".equals(aSTOverrides))
	    acceptSTOverrides = "Y";
    }

    private static final Logger log = Logger
	    .getLogger(XPEDXManageCustMaintenanceInfo.class);
    Element customerOrganizationEle;

    // protected String viewPrices="N";
    // protected String canOrder="N";
    // protected String canViewInv="N";
    protected String canViewInvoice = "N";
    // protected String useCustSKU="N";
    // protected String useOrderMulUOM="N";
    // protected String sampleRequestFlag="N";

    // protected String emailViewInvoices="N";
    // protected String previewInvoices="N";
    // protected String acceptBackOrders="N";
    protected String acceptSTOverrides = "N";

}
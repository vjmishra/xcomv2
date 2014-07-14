package com.sterlingcommerce.xpedx.webchannel.profile.org;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class XPEDXCustomerShipTo extends WCMashupAction {
	/**
     * 
     */
	private static final long serialVersionUID = 4073171403781679345L;
	private Element outputDoc;
	private Element shipToAddress;
	private Element associatedBillToAddress;
	private Element billToCustElem;
	private boolean success;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean isSuccess) {
		this.success = isSuccess;
	}

	private static final Logger log = Logger
			.getLogger(XPEDXCustomerShipTo.class);

	@Override
	public String execute() {
		try {
			Document output = XPEDXWCUtils.getCustomerDetails(getWCContext()
					.getCustomerId(), getWCContext().getStorefrontId());
			outputDoc = output.getDocumentElement();
			String associatedBillToID = SCXmlUtil.getXpathAttribute(outputDoc,
					"//Customer/ParentCustomer/@CustomerID");
			billToCustElem = XPEDXWCUtils.getCustomerDetails(
					associatedBillToID, getWCContext().getStorefrontId())
					.getDocumentElement();
			Element custAddtnlAddresses = SCXmlUtil.getChildElement(outputDoc,
					"CustomerAdditionalAddressList");
			shipToAddress = SCXmlUtil.getFirstChildElement(custAddtnlAddresses);
			Element billToCustAddtnlAddresses = SCXmlUtil.getChildElement(
					billToCustElem, "CustomerAdditionalAddressList");
			associatedBillToAddress = SCXmlUtil
					.getFirstChildElement(billToCustAddtnlAddresses);
			setSuccess(true);
			return (SUCCESS);
		} catch (Exception e) {
			log.debug("error while getting the Ship to Customer Details");
			return (ERROR);
		}
	}

	public Element getOutputDoc() {
		return outputDoc;
	}

	public void setOutputDoc(Element outputDoc) {
		this.outputDoc = outputDoc;
	}

	public Element getShipToAddress() {
		return shipToAddress;
	}

	public void setShipToAddress(Element shipToAddress) {
		this.shipToAddress = shipToAddress;
	}

	public Element getAssociatedBillToAddress() {
		return associatedBillToAddress;
	}

	public void setAssociatedBillToAddress(Element associatedBillToAddress) {
		this.associatedBillToAddress = associatedBillToAddress;
	}

	public Element getBillToCustElem() {
		return billToCustElem;
	}

	public void setBillToCustElem(Element billToCustElem) {
		this.billToCustElem = billToCustElem;
	}

}

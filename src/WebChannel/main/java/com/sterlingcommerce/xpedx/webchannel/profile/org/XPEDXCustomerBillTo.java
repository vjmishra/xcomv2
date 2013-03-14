package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class XPEDXCustomerBillTo extends WCMashupAction {
	/**
     * 
     */
	private static final long serialVersionUID = -3597629759033015572L;
	private Element outputDoc;
	private Element billToAddress;
	private ArrayList<Element> salesRepList;
	private static final Logger log = Logger
			.getLogger(XPEDXCustomerBillTo.class);
	Element csr1UserEle = null;
	Element csr2UserEle = null;

	@Override
	public String execute() {
		if (!XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext)) {
			log.debug("Bill To Customer not in the Context");
		}
		try {
			String billToCustomerId = XPEDXWCUtils.getParentCustomer(
					wcContext.getCustomerId(), wcContext);
			Document output = XPEDXWCUtils.getCustomerDetails(billToCustomerId,
					wcContext.getStorefrontId());
			outputDoc = output.getDocumentElement();
			Element custAddtnlAddresses = SCXmlUtil.getChildElement(outputDoc,
					"CustomerAdditionalAddressList");
			billToAddress = SCXmlUtil.getElementByAttribute(outputDoc,
					"CustomerAdditionalAddressList/CustomerAdditionalAddress",
					"IsDefaultBillTo", "Y");
			if (billToAddress == null)
				billToAddress = SCXmlUtil
						.getFirstChildElement(custAddtnlAddresses);
			Element extnElem = SCXmlUtil.getChildElement(outputDoc, "Extn");
			Element salesRepListElem = SCXmlUtil.getChildElement(extnElem,
					"XPEDXSalesRepList");
			salesRepList = SCXmlUtil.getElements(salesRepListElem,
					"XPEDXSalesRep");
			String custCSR1UserId = SCXmlUtil
					.getAttribute(extnElem, "ExtnECSR");
			String custCSR2UserId = SCXmlUtil.getAttribute(extnElem,
					"ExtnECSR2");
			if (custCSR1UserId != null && custCSR1UserId.trim().length() > 0)
				csr1UserEle = XPEDXWCUtils.getUserInfo(custCSR1UserId,
						getWCContext().getStorefrontId());
			if (custCSR2UserId != null && custCSR2UserId.trim().length() > 0)
				csr2UserEle = XPEDXWCUtils.getUserInfo(custCSR2UserId,
						getWCContext().getStorefrontId());

			return (SUCCESS);
		} catch (Exception e) {
			log.debug("Error while getting the Bill To Customer Details");
			return (ERROR);
		}
	}

	public Element getOutputDoc() {
		return outputDoc;
	}

	public void setOutputDoc(Element outputDoc) {
		this.outputDoc = outputDoc;
	}

	public Element getBillToAddress() {
		return billToAddress;
	}

	public void setBillToAddress(Element billToAddress) {
		this.billToAddress = billToAddress;
	}

	public ArrayList<Element> getSalesRepList() {
		return salesRepList;
	}

	public void setSalesRepList(ArrayList<Element> salesRepList) {
		this.salesRepList = salesRepList;
	}

	public Element getCsr1UserEle() {
		return csr1UserEle;
	}

	public void setCsr1UserEle(Element csr1UserEle) {
		this.csr1UserEle = csr1UserEle;
	}

	public Element getCsr2UserEle() {
		return csr2UserEle;
	}

	public void setCsr2UserEle(Element csr2UserEle) {
		this.csr2UserEle = csr2UserEle;
	}

}

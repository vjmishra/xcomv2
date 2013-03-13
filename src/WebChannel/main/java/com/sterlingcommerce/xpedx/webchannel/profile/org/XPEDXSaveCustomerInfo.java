package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.yantra.yfc.dom.YFCDocument;

@SuppressWarnings("serial")
public class XPEDXSaveCustomerInfo extends WCMashupAction {

    // String for Action Results
    private String REDIRECT = "redirect";
    private String bodyData;
    private String CustLineAccNoLabel;
    private String CustLineField1Label;
    private String CustLineField2Label;
    private String CustLineField3Label;
    private String txtAttnName;
    private String txtCustEmailAddress;
    private String customerId;
    private String organizationCode;
    private String txtPhone2;
    private String txtFax2;
    private String txtPhone1;
    private String txtFax1;
    private String emailIDForInvoice;
    private String viewInvoices;
    private String txtAddress1;
    private String txtAddress2;
    private String txtAddress3;
    private String txtCity;
    private String txtState;
    private String selCountryCode;
    private String txtZipCode;
    private String addtnlAddressID;
    private String addtnlAddressKey;
    private boolean success;

    public boolean isSuccess() {
	return success;
    }

    public void setSuccess(boolean isSuccess) {
	this.success = isSuccess;
    }

    public String getBodyData() {
	return bodyData;
    }

    public void setBodyData(String bodyData) {
	this.bodyData = bodyData;
    }

    @Override
    public String execute() {

	Document outputDoc = null;
	Element input = prepareInputXMLforCustQuickLinks();

	String inputXml = SCXmlUtil.getString(input);

	LOG.debug("Input XML: " + inputXml);
	Object obj = WCMashupHelper.invokeMashup(
		"xpedx-saveCustomerQuickLinks", input,
		wcContext.getSCUIContext());
	outputDoc = ((Element) obj).getOwnerDocument();
	if (null != outputDoc) {
	    LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
	}
	setSuccess(true);
	return REDIRECT;
    }

    private Element prepareInputXMLforCustQuickLinks() {

	Document templateCustomerDoc = YFCDocument.createDocument("Customer")
		.getDocument();
	Element templateElement = templateCustomerDoc.getDocumentElement();
	templateElement.setAttribute("CustomerID", customerId);
	templateElement.setAttribute("OrganizationCode", organizationCode);
	Element eleExtn = templateCustomerDoc.createElement("Extn");
	if (!"Y".equals(viewInvoices)) {
	    viewInvoices = "N";
	}
	eleExtn.setAttribute("ExtnCustLineAccLbl", CustLineAccNoLabel);
	eleExtn.setAttribute("ExtnCustLineField1Label", CustLineField1Label);
	eleExtn.setAttribute("ExtnCustLineField2Label", CustLineField2Label);
	eleExtn.setAttribute("ExtnCustLineField3Label", CustLineField3Label);
	eleExtn.setAttribute("ExtnCustEmailAddress", txtCustEmailAddress);
	eleExtn.setAttribute("ExtnAttnName", txtAttnName);
	eleExtn.setAttribute("ExtnPhone2", txtPhone2);
	eleExtn.setAttribute("ExtnFax2", txtFax2);
	eleExtn.setAttribute("ExtnPhone1", txtPhone1);
	eleExtn.setAttribute("ExtnFax1", txtFax1);
	eleExtn.setAttribute("ExtnInvoiceEMailID", emailIDForInvoice);
	eleExtn.setAttribute("ExtnPreviewInvoicesFlag", viewInvoices);
	Element eleXPXQuickLinkList = templateCustomerDoc
		.createElement("XPXCustQuickLinkList");
	eleXPXQuickLinkList.setAttribute("Reset", "true");

	Element addtnlAddressList = templateCustomerDoc
		.createElement("CustomerAdditionalAddressList");
	Element addtnlAddress = templateCustomerDoc
		.createElement("CustomerAdditionalAddress");
	Element personInfo = templateCustomerDoc.createElement("PersonInfo");
	personInfo.setAttribute("AddressLine1", txtAddress1);
	personInfo.setAttribute("AddressLine2", txtAddress2);
	personInfo.setAttribute("AddressLine3", txtAddress3);
	personInfo.setAttribute("City", txtCity);
	personInfo.setAttribute("Country", selCountryCode);
	personInfo.setAttribute("State", txtState);
	personInfo.setAttribute("ZipCode", txtZipCode);

	if ((addtnlAddressID != null && addtnlAddressID.trim().length() > 0)
		|| (addtnlAddressKey != null && addtnlAddressKey
			.trim().length() > 0)) {
	    addtnlAddress.setAttribute("CustomerAdditionalAddressID",
		    addtnlAddressID);
	    addtnlAddress.setAttribute("CustomerAdditionalAddressKey",
		    addtnlAddressKey);
	    addtnlAddress.setAttribute("IsDefaultBillTo", "Y");
	    addtnlAddress.setAttribute("IsBillTo", "Y");
	} else {
	    addtnlAddress.setAttribute("IsDefaultBillTo", "Y");
	    addtnlAddress.setAttribute("IsBillTo", "Y");
	}

	Element eleXPXQuickLink = null;
	// StringTokenizer st = new StringTokenizer(getBodyData(), "+=_");
	// Tokenizer on all three parameters +=_ not the group of them
	// it causes an issue when ever the url has = or any other parameters
	// changed to work only when delimiters appear in sequence
	Pattern p = Pattern.compile("[+\\s][=\\s][_\\s]");
	int i = 1;
	String[] result = p.split(getBodyData());
	for (int j = 1; j < result.length; j++)
	// while (st.hasMoreTokens())
	{
	    // String token = st.nextToken();
	    String token = result[j];

	    if (token.equals("*#?")) {
		token = "";
	    }

	    if (i == 1) {
		eleXPXQuickLink = templateCustomerDoc
			.createElement("XPXCustQuickLink");
		eleXPXQuickLink.setAttribute("CustQuickLinkName", token);
	    }

	    if (i == 2)
		eleXPXQuickLink.setAttribute("CustQuickLinkUrl", token);

	    if (i == 3) {
		eleXPXQuickLink.setAttribute("CustShowQuickLink", token);
	    }
	    if (i == 4) {
		eleXPXQuickLink.setAttribute("CustURLOrder", token);
		eleXPXQuickLinkList.appendChild(eleXPXQuickLink);
	    }

	    i++;

	    if (i == 5) {
		i = 1;

	    }
	}

	eleExtn.appendChild(eleXPXQuickLinkList);
	templateElement.appendChild(eleExtn);
	addtnlAddress.appendChild(personInfo);
	addtnlAddressList.appendChild(addtnlAddress);
	templateElement.appendChild(addtnlAddressList);

	return templateElement;
    }

    public String getViewInvoices() {
	return viewInvoices;
    }

    public void setViewInvoices(String viewInvoices) {
	this.viewInvoices = viewInvoices;
    }

    public String getCustLineAccNoLabel() {
	return CustLineAccNoLabel;
    }

    public void setCustLineAccNoLabel(String custLineAccNoLabel) {
	CustLineAccNoLabel = custLineAccNoLabel;
    }

    public String getCustLineField1Label() {
	return CustLineField1Label;
    }

    public void setCustLineField1Label(String custLineField1Label) {
	CustLineField1Label = custLineField1Label;
    }

    public String getCustLineField2Label() {
	return CustLineField2Label;
    }

    public void setCustLineField2Label(String custLineField2Label) {
	CustLineField2Label = custLineField2Label;
    }

    public String getCustLineField3Label() {
	return CustLineField3Label;
    }

    public void setCustLineField3Label(String custLineField3Label) {
	CustLineField3Label = custLineField3Label;
    }

    public String getTxtAttnName() {
	return txtAttnName;
    }

    public void setTxtAttnName(String txtAttnName) {
	this.txtAttnName = txtAttnName;
    }

    public String getTxtCustEmailAddress() {
	return txtCustEmailAddress;
    }

    public void setTxtCustEmailAddress(String txtCustEmailAddress) {
	this.txtCustEmailAddress = txtCustEmailAddress;
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

    public String getTxtPhone2() {
	return txtPhone2;
    }

    public void setTxtPhone2(String txtPhone2) {
	this.txtPhone2 = txtPhone2;
    }

    public String getTxtFax2() {
	return txtFax2;
    }

    public void setTxtFax2(String txtFax2) {
	this.txtFax2 = txtFax2;
    }

    public String getEmailIDForInvoice() {
	return emailIDForInvoice;
    }

    public void setEmailIDForInvoice(String emailIDForInvoice) {
	this.emailIDForInvoice = emailIDForInvoice;
    }

    public String getTxtPhone1() {
	return txtPhone1;
    }

    public void setTxtPhone1(String txtPhone1) {
	this.txtPhone1 = txtPhone1;
    }

    public String getTxtFax1() {
	return txtFax1;
    }

    public void setTxtFax1(String txtFax1) {
	this.txtFax1 = txtFax1;
    }

    public String getTxtAddress1() {
	return txtAddress1;
    }

    public void setTxtAddress1(String txtAddress1) {
	this.txtAddress1 = txtAddress1;
    }

    public String getTxtAddress2() {
	return txtAddress2;
    }

    public void setTxtAddress2(String txtAddress2) {
	this.txtAddress2 = txtAddress2;
    }

    public String getTxtAddress3() {
	return txtAddress3;
    }

    public void setTxtAddress3(String txtAddress3) {
	this.txtAddress3 = txtAddress3;
    }

    public String getTxtCity() {
	return txtCity;
    }

    public void setTxtCity(String txtCity) {
	this.txtCity = txtCity;
    }

    public String getTxtState() {
	return txtState;
    }

    public void setTxtState(String txtState) {
	this.txtState = txtState;
    }

    public String getTxtZipCode() {
	return txtZipCode;
    }

    public void setTxtZipCode(String txtZipCode) {
	this.txtZipCode = txtZipCode;
    }

    public String getAddtnlAddressID() {
	return addtnlAddressID;
    }

    public void setAddtnlAddressID(String addtnlAddressID) {
	this.addtnlAddressID = addtnlAddressID;
    }

    public String getAddtnlAddressKey() {
	return addtnlAddressKey;
    }

    public void setAddtnlAddressKey(String addtnlAddressKey) {
	this.addtnlAddressKey = addtnlAddressKey;
    }

    public String getSelCountryCode() {
	return selCountryCode;
    }

    public void setSelCountryCode(String selCountryCode) {
	this.selCountryCode = selCountryCode;
    }

}

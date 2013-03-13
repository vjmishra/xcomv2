package com.sterlingcommerce.xpedx.webchannel.profile.user;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.xpedx.nextgen.common.util.XPXEmailUtil;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPEDXNewUserRegistration extends WCMashupAction {
    @SuppressWarnings("unused")
    private static final Logger log = Logger
	    .getLogger(XPEDXNewUserRegistration.class);
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static final String SUCCESS = "success";
    private String appendedCSREmailIDs = null;
    private String brandEmail = null;
    private String imageUrlPath = null; // Start -Jira 3261
    private String mailCCAddress = null;
    private String mailFromAddress = null;
    private String mailSubject = null;
    private String messageType = "NewUser";
    private String newUserAddress1 = null;
    private String newUserAddress2 = null;
    private String newUserCity = null;
    private String newUserComments = null;
    private String newUserCompanyName = null;
    private String newUserEmail = null;
    private String newUserFirstName = null;
    private String newUserLastName = null;
    private String newUserPhone = null;
    private String newUserState = null;
    private String newUserZipCode = null;

    private String templatePath = null;

    @Override
    public String execute() {

	StringBuffer sb = new StringBuffer();
	// StringBuffer sbm = new StringBuffer();
	// String suffix = "";

	String storeFrontId = wcContext.getStorefrontId();
	if (storeFrontId != null && storeFrontId.length() > 0) {
	    String userName = YFSSystem.getProperty("fromAddress.username");
	    String suffix = YFSSystem.getProperty("fromAddress.suffix");
	    sb.append(userName).append("@").append(storeFrontId).append(suffix);
	    // String marketingCC = "marketing";
	    // suffix = YFSSystem.getProperty("fromAddress.suffix");
	    // sbm.append(marketingCC).append("@").append(storeFrontId).append(suffix);
	    brandEmail = storeFrontId;

	}

	/**
	 * Start ------- JIRA 3261 for logo changes
	 * 
	 * 
	 * */

	// String imageUrl = "";
	if (storeFrontId != null && !storeFrontId.trim().isEmpty()) {
	    String imageName = getLogoName(storeFrontId);
	    final String imagesRootFolder = YFSSystem
		    .getProperty("ImagesRootFolder");
	    if (imagesRootFolder != null && !imagesRootFolder.trim().isEmpty()
		    && imageName != null && !imageName.trim().isEmpty()) {
		imageUrlPath = imagesRootFolder + imageName;
		setImageUrlPath(imageUrlPath);
	    }
	}
	/**
	 * End ------- JIRA 3261 for logo changes
	 * 
	 * 
	 * */

	// setMailCCAddress(sbm.toString());
	setMailCCAddress(sb.toString()); // JIRA 4087 -Change CC address to
					 // ebusiness@xpedx.com
	setMailFromAddress(sb.toString());
	setMailSubject("NewUserInfo");
	// setMailSubject(storeFrontId+".com Registration Request Notification");
	setTemplatePath("/global/template/email/newUser_email_CSR.xsl");
	try {
	    // JIRA 3261 Start-Code Commentd as per JIRA Requirement
	    /*
	     * Element outputElem =
	     * prepareAndInvokeMashup("xpedxGetCustomCommonCodesForCSREmailIDs"
	     * ); NodeList nl = outputElem.getElementsByTagName("CommonCode");
	     * int i =0; for (i=0;i<nl.getLength();i++) { Element commonCodeElem
	     * = (Element) nl.item(i);
	     * if(commonCodeElem.getAttribute("CodeShortDescription") != null){
	     * if (i == 0){ appendedCSREmailIDs =
	     * commonCodeElem.getAttribute("CodeShortDescription"); } else
	     * appendedCSREmailIDs =
	     * appendedCSREmailIDs.concat(","+commonCodeElem
	     * .getAttribute("CodeShortDescription")); } } //JIRA 3261 End-Code
	     * Commentd as per JIRA Requirement
	     */
	    appendedCSREmailIDs = newUserEmail;
	    // log.debug("XPEDXNewUserRegistration Before Mashup -Email was sucessfull send to "+appendedCSREmailIDs+
	    // "," +newUserEmail);
	    // prepareAndInvokeMashup("XPEDXSendNewUserInfoToCSR");
	    /* XBT-73 : Begin - Sending email through Java Mail API now */
	    Set mashupId = new HashSet();
	    mashupId.add("XPEDXSendNewUserInfoToCSR");

	    Map mashupInputs = prepareMashupInputs(mashupId);
	    Element inputXmlFromMashup = (Element) mashupInputs
		    .get("XPEDXSendNewUserInfoToCSR");
	    Element newUserElement = (Element) inputXmlFromMashup
		    .getElementsByTagName("NewUser").item(0);
	    newUserElement.setAttribute("CCEmailId", getMailCCAddress());
	    newUserElement.setAttribute("ToEmailId", appendedCSREmailIDs);
	    String emailXML = SCXmlUtil.getString(newUserElement);
	    String emailType = XPXEmailUtil.NEW_USER_REGISTRATION_EMAIL_TYPE;
	    String emailFrom = getMailFromAddress();
	    ISCUITransactionContext scuiTransactionContext = getWCContext()
		    .getSCUIContext().getTransactionContext(true);
	    YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
		    .getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
	    String emailSubject = getMailSubject();
	    XPXEmailUtil.insertEmailDetailsIntoDB(env, emailXML, emailType,
		    emailSubject, emailFrom, storeFrontId);
	    /* XBT-73 : End - Sending email through Java Mail API now */

	} catch (Exception e) {
	    log.error("Couldn't send the Mail to CSR" + e.getMessage());
	    return ERROR;
	    // TODO: handle exception
	}
	return SUCCESS;
    }

    public String getAppendedCSREmailIDs() {
	return appendedCSREmailIDs;
    }

    public String getBrandEmail() {
	return brandEmail;
    }

    public String getImageUrlPath() {
	return imageUrlPath;
    }

    /**
     * Start JIRA 3261
     * 
     * 
     * */
    private String getLogoName(String sellerOrgCode) {
	String _imageName = "";
	if (XPEDXConstants.XPEDX_LOGO.equalsIgnoreCase(sellerOrgCode)) {
	    _imageName = "/xpedx_r_rgb_lo.jpg";
	} else if (XPEDXConstants.BULKLEYDUNTON_LOGO
		.equalsIgnoreCase(sellerOrgCode)) {
	    _imageName = "/BulkleyDunton_r_rgb_lo.jpg";
	} else if (XPEDXConstants.CENTRAILEWMAR_LOG0
		.equalsIgnoreCase(sellerOrgCode)) {
	    _imageName = "/CentralLewmar_r_rgb_lo.jpg";
	} else if (XPEDXConstants.CENTRALMARQUARDT_LOGO
		.equalsIgnoreCase(sellerOrgCode)) {
	    _imageName = "/CentralMarquardt_r_rgb_lo.jpg";
	} else if (XPEDXConstants.SAALFELD_LOGO.equalsIgnoreCase(sellerOrgCode)) {
	    _imageName = "/Saalfeld_r_rgb_lo.jpg";
	} else if (XPEDXConstants.STRATEGICPAPER_LOG0
		.equalsIgnoreCase(sellerOrgCode)) {
	    _imageName = "/StrategicPaper_r_rgb_lo.jpg";
	} else if (XPEDXConstants.WESTERNPAPER_LOGO
		.equalsIgnoreCase(sellerOrgCode)) {
	    _imageName = "/WesternPaper_r_rgb_lo.jpg";
	} else if (XPEDXConstants.WHITEMANTOWER_LOGO
		.equalsIgnoreCase(sellerOrgCode)) {
	    _imageName = "/WhitemanTower_r_rgb_lo.jpg";
	} else if (XPEDXConstants.ZELLERBACH_LOGO
		.equalsIgnoreCase(sellerOrgCode)) {
	    _imageName = "/Zellerbach_r_rgb_lo.jpg";
	} else if (XPEDXConstants.XPEDXCANADA_LOGO
		.equalsIgnoreCase(sellerOrgCode)) {
	    _imageName = "/xpedx_r_rgb_lo.jpg";
	}
	return _imageName;
    }

    public String getMailCCAddress() {
	return mailCCAddress;
    }

    public String getMailFromAddress() {
	return mailFromAddress;
    }

    /*
     * public String getMailHost() { return mailHost; } public void
     * setMailHost(String mailHost) { this.mailHost = mailHost; }
     */
    public String getMailSubject() {
	return mailSubject;
    }

    public String getMessageType() {
	return messageType;
    }

    /**
     * End JIRA 3261
     * 
     * 
     * */

    public String getNewUserAddress1() {
	return newUserAddress1;
    }

    public String getNewUserAddress2() {
	return newUserAddress2;
    }

    public String getNewUserCity() {
	return newUserCity;
    }

    public String getNewUserComments() {
	return newUserComments;
    }

    public String getNewUserCompanyName() {
	return newUserCompanyName;
    }

    public String getNewUserEmail() {
	return newUserEmail;
    }

    public String getNewUserFirstName() {
	return newUserFirstName;
    }

    public String getNewUserLastName() {
	return newUserLastName;
    }

    /*
     * public String getNewUserName() { return newUserName; } public void
     * setNewUserName(String newUserName) { this.newUserName = newUserName; }
     */
    public String getNewUserPhone() {
	return newUserPhone;
    }

    public String getNewUserState() {
	return newUserState;
    }

    public String getNewUserZipCode() {
	return newUserZipCode;
    }

    public String getTemplatePath() {
	return templatePath;
    }

    public void setAppendedCSREmailIDs(String appendedCSREmailIDs) {
	this.appendedCSREmailIDs = appendedCSREmailIDs;
    }

    public void setBrandEmail(String brandEmail) {
	this.brandEmail = brandEmail;
    }

    public void setImageUrlPath(String imageUrlPath) {
	this.imageUrlPath = imageUrlPath;
    }

    public void setMailCCAddress(String mailCCAddress) {
	this.mailCCAddress = mailCCAddress;
    }

    public void setMailFromAddress(String mailFromAddress) {
	this.mailFromAddress = mailFromAddress;
    }

    public void setMailSubject(String mailSubject) {
	this.mailSubject = mailSubject;
    }

    public void setMessageType(String messageType) {
	this.messageType = messageType;
    }

    public void setNewUserAddress1(String newUserAddress1) {
	this.newUserAddress1 = newUserAddress1;
    }

    public void setNewUserAddress2(String newUserAddress2) {
	this.newUserAddress2 = newUserAddress2;
    }

    public void setNewUserCity(String newUserCity) {
	this.newUserCity = newUserCity;
    }

    public void setNewUserComments(String newUserComments) {
	this.newUserComments = newUserComments;
    }

    public void setNewUserCompanyName(String newUserCompanyName) {
	this.newUserCompanyName = newUserCompanyName;
    }

    public void setNewUserEmail(String newUserEmail) {
	this.newUserEmail = newUserEmail;
    }

    public void setNewUserFirstName(String newUserFirstName) {
	this.newUserFirstName = newUserFirstName;
    }

    public void setNewUserLastName(String newUserLastName) {
	this.newUserLastName = newUserLastName;
    }

    public void setNewUserPhone(String newUserPhone) {
	this.newUserPhone = newUserPhone;
    }

    public void setNewUserState(String newUserState) {
	this.newUserState = newUserState;
    }

    public void setNewUserZipCode(String newUserZipCode) {
	this.newUserZipCode = newUserZipCode;
    }

    public void setTemplatePath(String templatePath) {
	this.templatePath = templatePath;
    }

}

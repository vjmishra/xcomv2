package com.sterlingcommerce.xpedx.webchannel.profile.user;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.yantra.yfs.core.YFSSystem;

public class XPEDXNewUserRegistration extends WCMashupAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(XPEDXNewUserRegistration.class);
	
/*	private String newUserName = null;*/
	private String newUserEmail=null;
	private String newUserPhone = null;
	private String newUserCompanyName = null;
	private String newUserAddress1 = null;
	private String newUserAddress2 = null;
	private String newUserCity = null;
	private String newUserState = null;
	private String newUserZipCode = null;
	private String newUserComments = null;
	private String mailHost= null;
	private String mailSubject = null;
	private String appendedCSREmailIDs = null;
	private String templatePath=null;
	private String mailFromAddress = null;
	private String mailCCAddress = null;
	private String newUserFirstName = null;
	private String newUserLastName = null;
	private String messageType="NewUser";
	private String imageUrlPath = null; //Start -Jira 3261
	public String getImageUrlPath() {
		return imageUrlPath;
	}
	public void setImageUrlPath(String imageUrlPath) {
		this.imageUrlPath = imageUrlPath;
	}
	private static final String SUCCESS = "success";
	
	
		public String execute(){
			mailHost = YFSSystem.getProperty("EMailServer");	
			if(mailHost==null){
				mailHost=XPEDXConstants.MAIL_HOSTUSEREMAIL;
			}
			StringBuffer sb = new StringBuffer();
			String storeFrontId = wcContext.getStorefrontId();
			
			if(storeFrontId!=null && storeFrontId.length()>0){
				String userName = YFSSystem.getProperty("fromAddress.username");
				String suffix = YFSSystem.getProperty("fromAddress.suffix");
				sb.append(userName).append("@").append(storeFrontId).append(suffix);
			}
			
			/** Start ------- JIRA 3261 for logo changes
			 * 
			 * 
			 * */
			//String imageUrl = "";
			if(storeFrontId!=null && storeFrontId.trim().length() > 0){
			String imageName = getLogoName(storeFrontId);
			String imagesRootFolder = YFSSystem.getProperty("ImagesRootFolder");
			if(imagesRootFolder!=null && imagesRootFolder.trim().length() > 0 && imageName!=null && imageName.trim().length() > 0){
				imageUrlPath = imagesRootFolder + imageName;
			    setImageUrlPath(imageUrlPath); 	
			}
			}
			/** End ------- JIRA 3261 for logo changes
			 * 
			 * 
			 * */
			
			setMailCCAddress(newUserEmail);
			setMailFromAddress(sb.toString());
			
			setMailSubject("NewUserInfo");
			setTemplatePath("/global/template/email/newUser_email_CSR.xsl");
			try
			{
				Element outputElem = prepareAndInvokeMashup("xpedxGetCustomCommonCodesForCSREmailIDs");
				NodeList nl = outputElem.getElementsByTagName("CommonCode");
				int i =0;
				for (i=0;i<nl.getLength();i++)
				{
					Element commonCodeElem = (Element) nl.item(i);
					if(commonCodeElem.getAttribute("CodeShortDescription") != null){
						if (i == 0){
							appendedCSREmailIDs = commonCodeElem.getAttribute("CodeShortDescription");
						}
						else
						appendedCSREmailIDs = appendedCSREmailIDs.concat(","+commonCodeElem.getAttribute("CodeShortDescription"));
					}
				}
				prepareAndInvokeMashup("XPEDXSendNewUserInfoToCSR");	
				
				}catch (Exception e) {
				e.printStackTrace();
				log.error("Couldn't send the Mail to CSR"+e.getMessage());
				return ERROR;
				// TODO: handle exception
			}
			return SUCCESS;
		}
		/**
		 * Start JIRA 3261
		 * 
		 * 
		 * */
		private String getLogoName(String sellerOrgCode)
		{
			String _imageName = "";
			if (XPEDXConstants.XPEDX_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/xpedx_r_rgb_lo.jpg";
			} else if (XPEDXConstants.BULKLEYDUNTON_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/BulkleyDunton_r_rgb_lo.jpg";
			} else if (XPEDXConstants.CENTRAILEWMAR_LOG0.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/CentralLewmar_r_rgb_lo.jpg";
			} else if (XPEDXConstants.CENTRALMARQUARDT_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/CentralMarquardt_r_rgb_lo.jpg";
			} else if (XPEDXConstants.SAALFELD_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/Saalfeld_r_rgb_lo.jpg";
			} else if (XPEDXConstants.STRATEGICPAPER_LOG0.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/StrategicPaper_r_rgb_lo.jpg";
			} else if (XPEDXConstants.WESTERNPAPER_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/WesternPaper_r_rgb_lo.jpg";
			} else if (XPEDXConstants.WHITEMANTOWER_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/WhitemanTower_r_rgb_lo.jpg";
			} else if (XPEDXConstants.ZELLERBACH_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/Zellerbach_r_rgb_lo.jpg";
			} else if (XPEDXConstants.XPEDXCANADA_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/xpedx_r_rgb_lo.jpg";
			} 
			return _imageName;
		}
		/**
		 * End JIRA 3261
		 * 
		 * 
		 * */
		
		public String getNewUserAddress1() {
			return newUserAddress1;
		}
		public void setNewUserAddress1(String newUserAddress1) {
			this.newUserAddress1 = newUserAddress1;
		}
		public String getNewUserAddress2() {
			return newUserAddress2;
		}
		public void setNewUserAddress2(String newUserAddress2) {
			this.newUserAddress2 = newUserAddress2;
		}
		public String getNewUserCompanyName() {
			return newUserCompanyName;
		}
		public void setNewUserCompanyName(String newUserCompanyName) {
			this.newUserCompanyName = newUserCompanyName;
		}
		public String getNewUserEmail() {
			return newUserEmail;
		}
		public void setNewUserEmail(String newUserEmail) {
			this.newUserEmail = newUserEmail;
		}
		/*public String getNewUserName() {
			return newUserName;
		}
		public void setNewUserName(String newUserName) {
			this.newUserName = newUserName;
		}*/
		public String getNewUserPhone() {
			return newUserPhone;
		}
		public void setNewUserPhone(String newUserPhone) {
			this.newUserPhone = newUserPhone;
		}
		public String getNewUserCity() {
			return newUserCity;
		}
		public void setNewUserCity(String newUserCity) {
			this.newUserCity = newUserCity;
		}
		public String getNewUserState() {
			return newUserState;
		}
		public void setNewUserState(String newUserState) {
			this.newUserState = newUserState;
		}
		public String getNewUserZipCode() {
			return newUserZipCode;
		}
		public void setNewUserZipCode(String newUserZipCode) {
			this.newUserZipCode = newUserZipCode;
		}
		public String getNewUserComments() {
			return newUserComments;
		}
		public void setNewUserComments(String newUserComments) {
			this.newUserComments = newUserComments;
		}
		public String getMailHost() {
			return mailHost;
		}
		public void setMailHost(String mailHost) {
			this.mailHost = mailHost;
		}
		public String getMailSubject() {
			return mailSubject;
		}
		public void setMailSubject(String mailSubject) {
			this.mailSubject = mailSubject;
		}
		public String getAppendedCSREmailIDs() {
			return appendedCSREmailIDs;
		}
		public void setAppendedCSREmailIDs(String appendedCSREmailIDs) {
			this.appendedCSREmailIDs = appendedCSREmailIDs;
		}
		public String getTemplatePath() {
			return templatePath;
		}
		public void setTemplatePath(String templatePath) {
			this.templatePath = templatePath;
		}
		public String getMailFromAddress() {
			return mailFromAddress;
		}
		public void setMailFromAddress(String mailFromAddress) {
			this.mailFromAddress = mailFromAddress;
		}
		public String getMailCCAddress() {
			return mailCCAddress;
		}
		public void setMailCCAddress(String mailCCAddress) {
			this.mailCCAddress = mailCCAddress;
		}
		public String getNewUserFirstName() {
			return newUserFirstName;
		}
		public void setNewUserFirstName(String newUserFirstName) {
			this.newUserFirstName = newUserFirstName;
		}
		public String getNewUserLastName() {
			return newUserLastName;
		}
		public void setNewUserLastName(String newUserLastName) {
			this.newUserLastName = newUserLastName;
		}
		public String getMessageType() {
			return messageType;
		}
		public void setMessageType(String messageType) {
			this.messageType = messageType;
		}
		
}

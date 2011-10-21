package com.sterlingcommerce.xpedx.webchannel.profile.user;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.yantra.yfs.core.YFSSystem;

public class XPEDXNewUserRegistration extends WCMashupAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(XPEDXNewUserRegistration.class);
	
	private String newUserName = null;
	private String newUserEmail=null;
	private String newUserPhone = null;
	private String newUserCompanyName = null;
	private String mailHost= null;
	private String mailSubject = null;
	private String appendedCSREmailIDs = null;
	private String templatePath=null;
	private String mailFromAddress = null;
	private String mailCCAddress = null;
	private String newUserFirstName = null;
	private String newUserLastName = null;
	
	
		public String execute(){
			mailHost = YFSSystem.getProperty("EMailServer");	
			if(mailHost==null){
				mailHost="mail.qa.local";
			}
			StringBuffer sb = new StringBuffer();
			String storeFrontId = wcContext.getStorefrontId();
			
			if(storeFrontId!=null && storeFrontId.length()>0){
				String userName = YFSSystem.getProperty("fromAddress.username");
				String suffix = YFSSystem.getProperty("fromAddress.suffix");
				sb.append(userName).append("@").append(storeFrontId).append(suffix);
			}
			setMailCCAddress(sb.toString());
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
				log.error("Couldn't send the Mail to CSR");
				return ERROR;
				// TODO: handle exception
			}
			return SUCCESS;
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
		public String getNewUserName() {
			return newUserName;
		}
		public void setNewUserName(String newUserName) {
			this.newUserName = newUserName;
		}
		public String getNewUserPhone() {
			return newUserPhone;
		}
		public void setNewUserPhone(String newUserPhone) {
			this.newUserPhone = newUserPhone;
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
		
}

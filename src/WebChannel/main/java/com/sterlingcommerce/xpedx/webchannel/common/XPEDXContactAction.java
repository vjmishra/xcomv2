package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfs.core.YFSSystem;

public class XPEDXContactAction extends WCMashupAction {

	private static final String customerExtnInformation = "xpedx-customer-getCustomerAllExtnInformation";
	public String execute() {

		try {
			
			eBusinessPhoneNo = YFSSystem.getProperty("eBusinessPhoneNo");
			eBusinessPhoneNo = null;
			if(eBusinessPhoneNo == null){
				eBusinessPhoneNo="877 269-1784";
			}
			
			String stroeFrontName =  wcContext.getStorefrontId();
			eBusinessEmailID = "eBusiness@"+stroeFrontName+".com";
			
			emailSubjects = new HashMap();
			emailSubjects.put("Order Status", "Order Status");
			emailSubjects.put("Item Return", "Item Return");
			emailSubjects.put("Need help finding", "Need help finding");
			emailSubjects.put("Technical Support", "Technical Support");
			emailSubjects.put("How Do I", "How Do I");
			
			if(getWCContext().getCustomerId() != null && !wcContext.isGuestUser())
			{
				Document outputDoc = null;
				
				/**
				 * JIRA 243 - Only Extn information of the customer is required, so invoking
				 * a mashup xpedx-customer-getCustomerAllExtnInformation which will fetch only
				 * Extn of Customer
				 */
				outputDoc = XPEDXWCUtils.getCustomerDetails(getWCContext().getCustomerId(), getWCContext()
						.getStorefrontId(), customerExtnInformation);
				customerElement = outputDoc.getDocumentElement();
				
				Element custExtnEle = XMLUtilities.getElement(customerElement, "Extn");
				
				shipToAddress = XPEDXWCUtils.getShipToAddressOfCustomer(customerElement);
				//Changed ExtnCustomerDivision to ExtnCustOrderBranch for Jira 3251
				String custDivison = SCXmlUtil.getAttribute(custExtnEle, "ExtnCustOrderBranch");
				String customerId=getWCContext().getCustomerId();
				String envId= SCXmlUtil.getAttribute(custExtnEle, "ExtnEnvironmentCode");
				
				if(custDivison!=null && custDivison.trim().length() > 0)
				{
					if(envId!=null && envId.trim().length()>0 && !custDivison.contains("_")){
						outputDoc = XPEDXWCUtils.getOrganizationDetails(custDivison+"_"+envId);
					}else{
						outputDoc = XPEDXWCUtils.getOrganizationDetails(custDivison);
					}
					organizationElement = outputDoc.getDocumentElement();
				}
				
				String custCSR1UserId = SCXmlUtil.getAttribute(custExtnEle, "ExtnECSR");
				String custCSR2UserId = SCXmlUtil.getAttribute(custExtnEle, "ExtnECSR2");
				
				if(custCSR1UserId!=null && custCSR1UserId.trim().length() > 0)
				{
					csr1UserEle = XPEDXWCUtils.getUserInfo(custCSR1UserId, getWCContext()
						.getStorefrontId());
				}
				if(custCSR2UserId!=null && custCSR2UserId.trim().length() > 0)
				{
					csr2UserEle = XPEDXWCUtils.getUserInfo(custCSR2UserId, getWCContext()
						.getStorefrontId());
				}
			}
		}
		catch (Exception e) {
			return "error";
		}
		return "success";
	}
	
	
	public String sendEmail()
	{
		mailHost = YFSSystem.getProperty("EmailServer");	
		if(mailHost==null){
			mailHost="mail.qa.local";
		}
		setMailSubject(mailSubject);
		setTemplatePath("/global/template/email/ContactUsEmailTemplate.xsl");
		try
		{
			
			if(getWCContext().getCustomerId() != null && !wcContext.isGuestUser())
			{
				if(csr1EMailID != null)
				{
					appendedCSREmailIDs = csr1EMailID;
				}
				else
				{
					appendedCSREmailIDs = "eBusiness@"+wcContext.getStorefrontId()+".com";
				}
			}
			else
			{
				appendedCSREmailIDs = "eBusiness@"+wcContext.getStorefrontId()+".com";
			}
			prepareAndInvokeMashup("XPEDXSendMailFromContactUs");
			
		}catch (Exception e) {
			e.printStackTrace();
			contactUsResp = "Your Query/Comments couldn't be registered due to technical reasons, please try again later.";
			log.error("Couldn't send the Mail to CSR");
			return ERROR;
			// TODO: handle exception
		}
		contactUsResp = "Your Query/Comments was successfully registered";
		return SUCCESS;
	}
	
	public Element getOrganizationElement() {
		return organizationElement;
	}


	public void setOrganizationElement(Element organizationElement) {
		this.organizationElement = organizationElement;
	}


	public Element getCustomerElement() {
		return customerElement;
	}
	
	public void setCustomerElement(Element customerElement) {
		this.customerElement = customerElement;
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
	
	
	public Map getEmailSubjects() {
		return emailSubjects;
	}


	public void setEmailSubjects(Map emailSubjects) {
		this.emailSubjects = emailSubjects;
	}

	
	
	
	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getUserEmail() {
		return userEmail;
	}


	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	public String getUserPhone() {
		return userPhone;
	}


	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}


	public String getMailSubject() {
		return mailSubject;
	}


	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}


	public String getMailHost() {
		return mailHost;
	}


	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
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

	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public String getComments() {
		return comments;
	}


	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCsr1EMailID() {
		return csr1EMailID;
	}

	public void setCsr1EMailID(String csr1eMailID) {
		csr1EMailID = csr1eMailID;
	}

	public String geteBusinessPhoneNo() {
		return eBusinessPhoneNo;
	}


	public void seteBusinessPhoneNo(String eBusinessPhoneNo) {
		this.eBusinessPhoneNo = eBusinessPhoneNo;
	}

	
	public String getContactUsResp() {
		return contactUsResp;
	}


	public void setContactUsResp(String contactUsResp) {
		this.contactUsResp = contactUsResp;
	}
	
	
	
	public String getCompName() {
		return compName;
	}


	public void setCompName(String compName) {
		this.compName = compName;
	}


	public String getCcity() {
		return ccity;
	}


	public void setCcity(String ccity) {
		this.ccity = ccity;
	}


	public String getCstate() {
		return cstate;
	}


	public void setCstate(String cstate) {
		this.cstate = cstate;
	}


	public String getCzipcode() {
		return czipcode;
	}


	public void setCzipcode(String czipcode) {
		this.czipcode = czipcode;
	}



	Element customerElement = null;
	Element organizationElement = null;
	Element csr1UserEle = null;
	Element csr2UserEle = null;
	Map emailSubjects = null;
	String eBusinessPhoneNo = null;
	
	private String firstName = null;
	private String compName = null;
	private String userEmail=null;
	private String userPhone = null;
	private String orderNo = null;
	private String mailSubject = null;
	private String comments = null;
	private String csr1EMailID = null;
	private String mailHost= null;
	private String appendedCSREmailIDs = null;
	private String templatePath=null;
	private String contactUsResp=null;
	private String ccity = null;
	private String cstate = null;
	private String czipcode = null;
	private String eBusinessEmailID = null;
	private XPEDXShipToCustomer shipToAddress = null;
	
	private static final Logger log = Logger.getLogger(XPEDXContactAction.class);
	public String geteBusinessEmailID() {
		return eBusinessEmailID;
	}


	public void seteBusinessEmailID(String eBusinessEmailID) {
		this.eBusinessEmailID = eBusinessEmailID;
	}


	public XPEDXShipToCustomer getShipToAddress() {
		return shipToAddress;
	}


	public void setShipToAddress(XPEDXShipToCustomer shipToAddress) {
		this.shipToAddress = shipToAddress;
	}
}

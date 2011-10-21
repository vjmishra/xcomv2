package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class XPEDXGetShipToBillToInfoAction extends WCMashupAction {

	private final static Logger log = Logger.getLogger(XPEDXGetShipToBillToInfoAction.class);

	private String customerId;
	private String orgCode;
	private String suffixType;
	private Element csr1UserEle = null;
	private Element csr2UserEle = null;
	private ArrayList<Element> salesRepList;
	protected Document outputDoc = null;
	protected Element shipToAddress;
	protected Element billToAddress;
	protected String displayCustomerFormat;
	protected String modifiedUser;
	protected String modifiedDate;
	protected Map invoiceEmailIdList = new LinkedHashMap();
	protected String Phone1FormatChange;
	protected String Fax1FormatChange;
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getSuffixType() {
		return suffixType;
	}

	public void setSuffixType(String suffixType) {
		this.suffixType = suffixType;
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

	public ArrayList<Element> getSalesRepList() {
		return salesRepList;
	}

	public void setSalesRepList(ArrayList<Element> salesRepList) {
		this.salesRepList = salesRepList;
	}

	public String execute() {
		try {
			getCustomerDetails(customerId, orgCode);
			
		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(suffixType!=null && suffixType.trim().length()>0){
			if(suffixType.equalsIgnoreCase(XPEDXConstants.SHIP_TO_CUSTOMER_SUFFIX_TYPE)){
				Element extnElem = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "Extn");
				Phone1FormatChange = getPhoneAndFaxInMarketingDepartmentstandard(extnElem.getAttribute("ExtnPhone1"));
				Fax1FormatChange = getPhoneAndFaxInMarketingDepartmentstandard(extnElem.getAttribute("ExtnFax1"));
				Element custAddtnlAddresses = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "CustomerAdditionalAddressList");
				shipToAddress = SCXmlUtil.getElementByAttribute(custAddtnlAddresses, "CustomerAdditionalAddress", "IsDefaultShipTo", "Y");
				if(shipToAddress == null)
					shipToAddress = SCXmlUtil.getFirstChildElement(custAddtnlAddresses);
				setDisplayCustomerFormat();
				setLastModifiedFields();
				return "shipToInfo";
			}
			else if(suffixType.equalsIgnoreCase(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE)){
				Element extnElem = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "Extn");				
				Element salesRepListElem = SCXmlUtil.getChildElement(extnElem, "XPEDXSalesRepList");
				salesRepList = SCXmlUtil.getElements(salesRepListElem, "XPEDXSalesRep");
				String custCSR1UserKey = SCXmlUtil.getAttribute(extnElem, "ExtnECSR1Key");
				String custCSR2UserKey = SCXmlUtil.getAttribute(extnElem, "ExtnECSR2Key");
				String shipFromBranch = SCXmlUtil.getAttribute(extnElem, "ExtnShipFromBranch");
				String environmentCode = SCXmlUtil.getAttribute(extnElem, "ExtnEnvironmentCode");
				boolean getDivisionInfo = true;
				if(custCSR1UserKey!=null && custCSR1UserKey.trim().length()>0) {
					csr1UserEle = getUserPersonInfo(custCSR1UserKey, null);
					getDivisionInfo = false;
				}					
				if(custCSR2UserKey!=null && custCSR2UserKey.trim().length()>0) {
					csr2UserEle = getUserPersonInfo(custCSR2UserKey, null);
					getDivisionInfo = false;
				}					
				if(getDivisionInfo) {
					Document organizationDetails = null;
					if (environmentCode != null && environmentCode.trim().length() > 0) {
						try {
							organizationDetails = XPEDXWCUtils.getOrganizationDetails(shipFromBranch
									+ "_" + environmentCode);
						} catch (CannotBuildInputException e) {
							log.error("Unable to get Organization details. "+ shipFromBranch + "_" + environmentCode + " " +e.getMessage());
						}
					} else {
						try {
							organizationDetails = XPEDXWCUtils.getOrganizationDetails(shipFromBranch);
						} catch (CannotBuildInputException e) {
							log.error("Unable to get Organization details. "+ shipFromBranch +e.getMessage());
						}
					}
					Element docEle = organizationDetails.getDocumentElement();
					Element orgEle = SCXmlUtil.getChildElement(docEle, "Organization");
					Element extnEle = SCXmlUtil.getChildElement(orgEle,"Extn");
					String extnDivisionContact = extnEle.getAttribute("ExtnDivisionContact");
					
					if(extnDivisionContact != null && extnDivisionContact.trim().length()>0) {
						csr1UserEle = getUserPersonInfo(null, extnDivisionContact);
					}					
				}
				Element custAddtnlAddresses = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "CustomerAdditionalAddressList");
				billToAddress = SCXmlUtil.getElementByAttribute(custAddtnlAddresses, "CustomerAdditionalAddress", "IsDefaultBillTo", "Y");
				if(billToAddress == null)
					billToAddress = SCXmlUtil.getFirstChildElement(custAddtnlAddresses);
				setDisplayCustomerFormat();
				setLastModifiedFields();
				setInvoiceEmailIdList();
				Phone1FormatChange = getPhoneAndFaxInMarketingDepartmentstandard(extnElem.getAttribute("ExtnPhone1"));
				Fax1FormatChange = getPhoneAndFaxInMarketingDepartmentstandard(extnElem.getAttribute("ExtnFax1"));
				return "billToInfo";
			}
			else{
				return "success";
			}
		}
		else
			return "error";
		
	}
	
	private Element getUserPersonInfo(String userKey, String loginId) {
		IWCContext wcContext = WCContextHelper
		.getWCContext(ServletActionContext.getRequest());
		
		Map<String, String> valueMap = new HashMap<String, String>();
		if(userKey != null && userKey.trim().length()>0)
			valueMap.put("/User/@UserKey", userKey);
		if(loginId !=null && loginId.trim().length()>0)
			valueMap.put("/User/@Loginid", loginId);
		
		Element input = null;
		try {
			input = WCMashupHelper.getMashupInput(
					"getXpedxUserPersonInfo", valueMap, wcContext
							.getSCUIContext());
		} catch (CannotBuildInputException e) {
			log.error("Unable to get User Person Info. "+ userKey + " " + loginId +e.getMessage());
		}
		Object obj = WCMashupHelper.invokeMashup(
				"getXpedxUserPersonInfo", input, wcContext
						.getSCUIContext());
		Document outputDocPersonInfo = ((Element) obj).getOwnerDocument();
		Element wElement = outputDocPersonInfo.getDocumentElement();
		Element userEle = SCXmlUtil.getChildElement(wElement, "User");	
		return userEle;
	}

	private String getPhoneAndFaxInMarketingDepartmentstandard(String inputNumber){
		if(inputNumber!=null && inputNumber.trim().length()>0) {
			if(inputNumber.contains("-"))
				return inputNumber;
			else
				return (inputNumber.substring(0, 3) + " " + inputNumber.substring(3, 6) + "-" + inputNumber.substring(6));
		}
		else
			return inputNumber;
	}
	
	public Document getCustomerDetails(String customerId, String orgCode)
			throws CannotBuildInputException {
		if (null == customerId || customerId.length() <= 0) {
			log
					.debug("getCustomerDetails: customerId is a required field. Returning an empty document");
			return outputDoc;
		} else if (null == orgCode || orgCode.length() <= 0) {
			log
					.debug("getCustomerDetails: orgCode is a required field. Returning an empty document");
			return outputDoc;
		}
		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Customer/@CustomerID", customerId);
		valueMap.put("/Customer/@OrganizationCode", orgCode);

		Element input = WCMashupHelper.getMashupInput("xpedx-customer-getShipToBillToInfo", valueMap,
				wcContext.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);

		log.debug("Input XML: " + inputXml);

		Object obj = WCMashupHelper.invokeMashup("xpedx-customer-getShipToBillToInfo", input, wcContext
				.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		
		Element extnElem = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "Extn");
		suffixType = SCXmlUtil.getAttribute(extnElem, "ExtnSuffixType");
		
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}
		return outputDoc;
	}
	
	private void setDisplayCustomerFormat() {
		String formattedCusotmer = XPEDXWCUtils.formatBillToShipToCustomer(customerId);
		Element buyerOrgElem = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "BuyerOrganization");
		String orgName = buyerOrgElem.getAttribute("OrganizationName");
		if(formattedCusotmer!=null && orgName!=null)
			displayCustomerFormat = formattedCusotmer + " " + orgName;
	}
	
	private void setLastModifiedFields() {
		String modifyTS = SCXmlUtil.getAttribute(outputDoc.getDocumentElement(), "Modifyts");
		String modifyUserId = SCXmlUtil.getAttribute(outputDoc.getDocumentElement(), "Modifyuserid");
		if(modifyTS!=null) {
			UtilBean utilBean = new UtilBean();
			modifiedDate = utilBean.formatDate(modifyTS, wcContext, null, "M/d/yyyy");	
		}
		if(modifyUserId!=null) {
			try {
				modifiedUser = XPEDXWCUtils.getLoginUserName(modifyUserId);
			}
			catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
	}
	
	private void setInvoiceEmailIdList() {
		Element extnElement = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "Extn");
		String invoiceEmailIds = extnElement.getAttribute("ExtnInvoiceEMailID");
		if(invoiceEmailIds!=null) {
			String[] invoiceEmailIdsList = invoiceEmailIds.split(",");
			for(int i=0; i<invoiceEmailIdsList.length;i++ ) {
				invoiceEmailIdList.put(invoiceEmailIdsList[i],invoiceEmailIdsList[i]);
			}			
		}
	}

	public Document getOutputDoc() {
		return outputDoc;
	}

	public void setOutputDoc(Document outputDoc) {
		this.outputDoc = outputDoc;
	}

	public Element getShipToAddress() {
		return shipToAddress;
	}

	public void setShipToAddress(Element shipToAddress) {
		this.shipToAddress = shipToAddress;
	}

	public Element getBillToAddress() {
		return billToAddress;
	}

	public void setBillToAddress(Element billToAddress) {
		this.billToAddress = billToAddress;
	}

	public String getDisplayCustomerFormat() {
		return displayCustomerFormat;
	}

	public void setDisplayCustomerFormat(String displayCustomerFormat) {
		this.displayCustomerFormat = displayCustomerFormat;
	}

	public String getModifiedUser() {
		return modifiedUser;
	}

	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Map getInvoiceEmailIdList() {
		return invoiceEmailIdList;
	}

	public void setInvoiceEmailIdList(Map invoiceEmailIdList) {
		this.invoiceEmailIdList = invoiceEmailIdList;
	}

	public String getPhone1FormatChange() {
		return Phone1FormatChange;
	}

	public void setPhone1FormatChange(String phone1FormatChange) {
		Phone1FormatChange = phone1FormatChange;
	}

	public String getFax1FormatChange() {
		return Fax1FormatChange;
	}

	public void setFax1FormatChange(String fax1FormatChange) {
		Fax1FormatChange = fax1FormatChange;
	}
}

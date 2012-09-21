package com.sterlingcommerce.xpedx.webchannel.common;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.SCUILocalSession;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.platform.utils.SCUIPlatformUtils;
import com.sterlingcommerce.webchannel.catalog.helper.CatalogContextHelper;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.BusinessRuleUtil;
import com.sterlingcommerce.webchannel.utilities.SSLSwitchingHelper;
import com.sterlingcommerce.webchannel.utilities.UserPreferenceUtil;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXShiptoHeaderAction extends WCMashupAction {

	protected String logoURL;
	protected String customerId;
	protected String organizationCode;
	private Element customerOrganizationEle;
	protected HashMap customerFieldsMap;

	public XPEDXShiptoHeaderAction() {
		logoURL = null;
		request = null;
	}
	


	public String execute() {		
		try {
			if (!getWCContext().isGuestUser()){
				prepareLoggerInUsersCustomerName();
			}
			
		} catch (Exception e) {
			XPEDXWCUtils.logExceptionIntoCent(e); //JIRA 4289
			return "error";
		}
		
		return "success";
	}



	/**
	 * 
	 */
	protected String prepareLoggerInUsersCustomerName() {
		try {

			customerId = wcContext.getCustomerId();
			organizationCode = wcContext.getStorefrontId();
			if (customerId != null && organizationCode != null) {
				Map outputMaps = prepareAndInvokeMashups();
				customerOrganizationEle = (Element) outputMaps
						.get("xpedx-customer-getHeaderInformation");
				// set the logged in users Customer Name(Buyer Org's). This is
				// displayed in the Header - RUgrani
				Element sBuyerOrg = SCXmlUtil.getChildElement(
						customerOrganizationEle, "BuyerOrganization");
				setLoggerInUserCustomerName(sBuyerOrg
						.getAttribute("OrganizationName"));
				Element extnElement = SCXmlUtil.getChildElement(
						customerOrganizationEle, "Extn");
				Element parentElem = SCXmlUtil.getChildElement(
						customerOrganizationEle, "ParentCustomer");
				Element parentExtnElem = SCXmlUtil.getChildElement(parentElem, "Extn");
				String custPrefcategory = extnElement.getAttribute("ExtnCustomerClass");
				String shipFromDivision = extnElement.getAttribute("ExtnShipFromBranch");
				//String customerUseSKU = extnElement.getAttribute("ExtnUseCustSKU");
				String industry = extnElement.getAttribute("ExtnIndustry");
				String currencyCode = parentExtnElem.getAttribute("ExtnCurrencyCode");
				String parentCustomerID=parentElem.getAttribute("CustomerID");
				String envCode = extnElement.getAttribute("ExtnEnvironmentCode");
				String companyCode = extnElement.getAttribute("ExtnCompanyCode");
				String legacyCustomerNumber =extnElement.getAttribute("ExtnLegacyCustNumber");
				wcContext.setWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH,shipFromDivision,WCAttributeScope.LOCAL_SESSION);
				//wcContext.setWCAttribute(XPEDXConstants.CUSTOMER_USE_SKU,customerUseSKU,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.INDUSTRY,industry,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.CUSTOMER_CURRENCY_CODE,currencyCode,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.BILL_TO_CUSTOMER,parentCustomerID,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute("ShipToForOrderSummaryPage",getLoggerInUserCustomerName(), WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute("BillToForOrderSummaryPage",SCXmlUtil.getChildElement(parentElem, "BuyerOrganization").getAttribute("OrganizationName"), WCAttributeScope.LOCAL_SESSION);
				
				//customerFieldsMap = XPEDXOrderUtils.getCustomerLineFieldMap(customerOrganizationEle.getOwnerDocument());
				//Changed here : Fetching doc from getCustomerExtnFlagsDoc instead 
				//String defaultShipToChanged = (String) wcContext.getWCAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED);
				//String defaultShipToChanged = (String) wcContext.getSCUIContext().getRequest().getSession().getAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED);
				//if(YFCUtils.isVoid(defaultShipToChanged) || "true".endsWith(defaultShipToChanged)){
					// do this only when the default ship to is changed and when logging in for the first time.
//					Document SAPCustomerDoc = getCustomerExtnFlagsDoc(wcContext);
//					customerFieldsMap = XPEDXOrderUtils.getCustomerLineFieldMap(SAPCustomerDoc);
//					wcContext.getSCUIContext().getRequest().getSession().setAttribute("customerFieldsSessionMap", customerFieldsMap);
//					wcContext.getSCUIContext().getRequest().getSession().setAttribute("sapCustExtnFields", SAPCustomerDoc);
					// reset the flag once used
					//wcContext.setWCAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,"false",WCAttributeScope.LOCAL_SESSION);
				//	wcContext.getSCUIContext().getRequest().getSession().setAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED, "false");
			//	}
				
				if(custPrefcategory!=null && custPrefcategory.trim().length()>0) {
					request.setAttribute("custPrefCategory", custPrefcategory);
					wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY,custPrefcategory);
				}
				wcContext.setWCAttribute(XPEDXConstants.ENVIRONMENT_CODE,envCode,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.COMPANY_CODE,companyCode,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.LEGACY_CUST_NUMBER,legacyCustomerNumber,WCAttributeScope.LOCAL_SESSION);
			}
		} catch (Exception ex) {
			log.error("Unable to get logged in users Customer Profile. "
					+ ex.getMessage());
			return "error";
		}

		return "success";
	}

	
	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrganizationCode() {
		return this.organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	
	/**
	 * @return the loggerInUserCustomerName
	 */
	public String getLoggerInUserCustomerName() {
		return loggerInUserCustomerName;
	}

	/**
	 * @param loggerInUserCustomerName
	 *            the loggerInUserCustomerName to set
	 */
	public void setLoggerInUserCustomerName(String loggerInUserCustomerName) {
		this.loggerInUserCustomerName = loggerInUserCustomerName;
	}

	
	private static final Logger log = Logger
			.getLogger(com.sterlingcommerce.xpedx.webchannel.common.XPEDXShiptoHeaderAction.class);
	private HttpServletRequest request;
	private static final long serialVersionUID = 1L;
	// To get the Category list
	protected Document prodCategoryOutputDoc;
	
	public SCUIContext uiContext = null;
	protected String categoryDepth = "2"; // take the default
	protected String loggerInUserCustomerName = "";
	public Element getCustomerOrganizationEle() {
		return customerOrganizationEle;
	}

	public void setCustomerOrganizationEle(Element customerOrganizationEle) {
		this.customerOrganizationEle = customerOrganizationEle;
	}

}

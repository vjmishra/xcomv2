package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.profile.ProfileUtility;
import com.sterlingcommerce.webchannel.profile.org.CustomerProfileUtility;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXGetCustomerOrganizationAction extends WCMashupAction {
    /**
     * 
     */
    private static final long serialVersionUID = -5746815213502215379L;
    private static final Logger log = Logger
	    .getLogger(XPEDXGetCustomerOrganizationAction.class);

    public XPEDXGetCustomerOrganizationAction() {
	customerOrganizationEle = null;
	customerList = null;
	childCustomersMap = null;
	customerDefaultAddressListEle = new ArrayList();
	customerNonInheritedDefaultAddressListEle = new ArrayList();
	customerNonInheritedPaymentEle = new ArrayList();
	customerAddressListEle = new ArrayList();
	customerDefaultCurrency = null;
	paymentMethodsMap = null;
	customerCreditCardPayments = new ArrayList();
	customerAccountPayments = new ArrayList();
	customerOtherPayments = new ArrayList();
	expandAddressPanel = false;
    }

    public String getGeneralInfo() {
	String inputCustomerID = customerId;
	if (YFCCommon.isVoid(inputCustomerID))
	    inputCustomerID = wcContext.getCustomerId();
	if (!ProfileUtility
		.isCustInCtxCustHierarchy(inputCustomerID, wcContext))
	    return "error";
	if (log.isDebugEnabled())
	    log.debug("Class:GetCustomerOrganizationAction Method:getGeneralInfo STARTS");
	String returnStr = getCustomerDetails(
		"xpedx-customer-getGeneralInformation", false);
	CustomerProfileUtility.setCustomerAddresses(customerOrganizationEle,
		customerDefaultAddressListEle, customerAddressListEle);
	setCustomerNonInheritedDefaultAddressListEle(customerDefaultAddressListEle);
	customerDefaultCurrency = CustomerProfileUtility
		.getCustomerDefaultCurrency(customerOrganizationEle);
	isChildCustomer = ProfileUtility.setCustomerAccess(
		customerOrganizationEle.getAttribute("CustomerID"), wcContext);
	if (log.isDebugEnabled())
	    log.debug("Class:GetCustomerOrganizationAction Method:getGeneralInfo ENDS");
	return returnStr;
    }

    public String getCorporateInfo() {
	String inputCustomerID = customerId;
	if (YFCCommon.isVoid(inputCustomerID))
	    inputCustomerID = wcContext.getCustomerId();
	if (!ProfileUtility
		.isCustInCtxCustHierarchy(inputCustomerID, wcContext))
	    return "error";
	if (log.isDebugEnabled())
	    log.debug("Class:GetCustomerOrganizationAction Method:getCorporateInfo STARTS");
	String returnStr = "success";

	/* STARTS - Customer-User Profile Changes - adsouza */

	// returnStr = getCustomerDetails("cust-GetCustomerCorpInfo", false);
	returnStr = getCustomerDetails("xpedx-cust-GetCustomerCorpInfo", false);
	CustomerProfileUtility.setCustomerAddresses(customerOrganizationEle,
		customerDefaultAddressListEle, customerAddressListEle);
	setCustomerNonInheritedDefaultAddressListEle(customerDefaultAddressListEle);

	/* ENDS - Customer-User Profile Changes - adsouza */
	if (!"success".equals(returnStr))
	    return returnStr;
	returnStr = populateChildCustomerList(null);
	isChildCustomer = ProfileUtility.setCustomerAccess(
		customerOrganizationEle.getAttribute("CustomerID"), wcContext);
	if (log.isDebugEnabled())
	    log.debug("Class:GetCustomerOrganizationAction Method:getCorporateInfo ENDS");
	return returnStr;
    }

    public String getPaymentInfo() {
	String inputCustomerID = customerId;
	if (YFCCommon.isVoid(inputCustomerID))
	    inputCustomerID = wcContext.getCustomerId();
	if (!ProfileUtility
		.isCustInCtxCustHierarchy(inputCustomerID, wcContext))
	    return "error";
	if (log.isDebugEnabled())
	    log.debug("Class:GetCustomerOrganizationAction Method:getPaymentInfo STARTS");
	customerId = getWCContext().getSCUIContext().getRequest()
		.getParameter("customerId");
	organizationCode = getWCContext().getSCUIContext().getRequest()
		.getParameter("organizationCode");
	if (customerId == null || "".equals(customerId))
	    customerId = getWCContext().getCustomerId();
	if (organizationCode == null || "".equals(organizationCode))
	    organizationCode = getWCContext().getStorefrontId();
	try {
	    Map outputMaps = prepareAndInvokeMashups();
	    customerOrganizationEle = (Element) outputMaps
		    .get("customer-getPaymentInformation");
	    Element paymentTypeGroups = (Element) outputMaps
		    .get("PaymentTypeListUtil");
	    CustomerProfileUtility.setPaymentInfo(customerOrganizationEle,
		    paymentTypeGroups, customerCreditCardPayments,
		    customerAccountPayments, customerOtherPayments);
	    isChildCustomer = ProfileUtility.setCustomerAccess(
		    customerOrganizationEle.getAttribute("CustomerID"),
		    wcContext);
	    setCustomerNonInheritedPaymentEle();
	    if (log.isDebugEnabled())
		log.debug((new StringBuilder())
			.append("customerOrganizationEle: ")
			.append(customerOrganizationEle).toString());
	} catch (Exception ex) {
	    getWCContext().getSCUIContext().replaceAttribute(
		    "SCUI_EXCEPTION_ATTR", ex);
	    ex.printStackTrace();
	    return "error";
	}
	if (log.isDebugEnabled())
	    log.debug("Class:GetCustomerOrganizationAction Method:getPaymentInfo ENDS");
	return "success";
    }

    public String getNewCustomerInfo() {
	String inputCustomerID = wcContext.getSCUIContext().getRequest()
		.getParameter("parentCustomerId");
	if (YFCCommon.isVoid(inputCustomerID))
	    inputCustomerID = wcContext.getCustomerId();
	if (!ProfileUtility
		.isCustInCtxCustHierarchy(inputCustomerID, wcContext))
	    return "error";
	if (log.isDebugEnabled())
	    log.debug("Class:NewCustomerProfileAction Method:execute STARTS");
	String returnStr = "success";
	returnStr = getCustomerDetails("customer-getNewCustomer", true);
	customerDefaultCurrency = CustomerProfileUtility
		.getCustomerDefaultCurrency(customerOrganizationEle);
	if (log.isDebugEnabled())
	    log.debug("Class:NewCustomerProfileAction Method:getGeneralInfo ENDS");
	return returnStr;
    }

    private String populateChildCustomerList(Object customerObj) {
	if (customerObj == null)
	    customerObj = customerOrganizationEle;
	if (log.isDebugEnabled())
	    log.debug("Class:GetCustomerOrganizationAction Method:populateCorporateInfo STARTS");
	parentCustomerKey = SCXmlUtil.getAttribute((Element) customerObj,
		"CustomerKey");
	try {
	    this.customerList = prepareAndInvokeMashup("cust-getCustomerList",
		    "childCustomerList");
	    if (null != this.customerList) {
		List customerList = SCXmlUtil.getChildren(getCustomerList(),
			"Customer");
		childCustomersMap = new HashMap(customerList.size());
		if (null != customerList && customerList.size() > 0) {
		    Iterator itr = customerList.iterator();
		    do {
			if (!itr.hasNext())
			    break;
			Element childCust = (Element) itr.next();
			Element childCustOrgElem = SCXmlUtil.getChildElement(
				childCust, "BuyerOrganization");
			childCustomersMap.put(childCust
				.getAttribute("CustomerID"), childCustOrgElem
				.getAttribute("OrganizationName"));
			if (log.isDebugEnabled())
			    log.debug((new StringBuilder())
				    .append("Class:GetCustomerOrganizationAction Method:populateChildCustomerList")
				    .append(childCust
					    .getAttribute("CustomerID"))
				    .append(",")
				    .append(childCust
					    .getAttribute("OrganizationCode"))
				    .toString());
		    } while (true);
		}
	    }
	} catch (Exception ex) {
	    getWCContext().getSCUIContext().replaceAttribute(
		    "SCUI_EXCEPTION_ATTR", ex);
	    ex.printStackTrace();
	    return "error";
	}
	if (log.isDebugEnabled())
	    log.debug("Class:GetCustomerOrganizationAction Method:populateChildCustomerList ENDS");
	return "success";
    }

    private String getCustomerDetails(String mashupID, boolean isActionCreate) {
	if (isActionCreate)
	    customerId = getWCContext().getSCUIContext().getRequest()
		    .getParameter("parentCustomerId");
	else
	    customerId = getWCContext().getSCUIContext().getRequest()
		    .getParameter("customerId");
	organizationCode = getWCContext().getSCUIContext().getRequest()
		.getParameter("organizationCode");
	if (customerId == null || "".equals(customerId))
	    customerId = getWCContext().getCustomerId();
	if (organizationCode == null || "".equals(organizationCode))
	    organizationCode = getWCContext().getStorefrontId();
	try {
	    Map outputMaps = prepareAndInvokeMashups();
	    customerOrganizationEle = (Element) outputMaps.get(mashupID);
	    if (log.isDebugEnabled())
		log.debug((new StringBuilder())
			.append("customerOrganizationEle: ")
			.append(customerOrganizationEle).toString());
	} catch (Exception ex) {
	    getWCContext().getSCUIContext().replaceAttribute(
		    "SCUI_EXCEPTION_ATTR", ex);
	    ex.printStackTrace();
	    return "error";
	}
	return "success";
    }

    public List getCustomerDefaultAddressListEle() {
	return customerDefaultAddressListEle;
    }

    public void setCustomerDefaultAddressListEle(
	    List customerDefaultAddressListEle) {
	this.customerDefaultAddressListEle = customerDefaultAddressListEle;
    }

    public List getCustomerAddressListEle() {
	return customerAddressListEle;
    }

    public void setCustomerAddressListEle(List customerAddressListEle) {
	this.customerAddressListEle = customerAddressListEle;
    }

    public Map getPaymentMethodsMap() {
	return paymentMethodsMap;
    }

    public void setPaymentMethodsMap(Map paymentMethodsMap) {
	this.paymentMethodsMap = paymentMethodsMap;
    }

    public Element getCustomerList() {
	return customerList;
    }

    public void setCustomerList(Element customerList) {
	this.customerList = customerList;
    }

    public Element getCustomerOrganizationEle() {
	return customerOrganizationEle;
    }

    public void setCustomerOrganizationEle(Element customerOrganizationEle) {
	this.customerOrganizationEle = customerOrganizationEle;
    }

    public String getInputNs() {
	return inputNs;
    }

    public void setInputNs(String inputNs) {
	this.inputNs = inputNs;
    }

    public SCUIContext getScuicontext() {
	return getWCContext().getSCUIContext();
    }

    public Map getChildCustomersMap() {
	return childCustomersMap;
    }

    public void setChildCustomersMap(Map childCustomersMap) {
	this.childCustomersMap = childCustomersMap;
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

    public String getOtherCustomerID() {
	return OtherCustomerID;
    }

    public void setOtherCustomerID(String otherCustomerID) {
	OtherCustomerID = otherCustomerID;
    }

    public List getCustomerCreditCardPayments() {
	return customerCreditCardPayments;
    }

    public void setCustomerCreditCardPayments(List customerCreditCardPayments) {
	this.customerCreditCardPayments = customerCreditCardPayments;
    }

    public List getCustomerAccountPayments() {
	return customerAccountPayments;
    }

    public void setCustomerAccountPayments(List customerAccountPayments) {
	this.customerAccountPayments = customerAccountPayments;
    }

    public List getCustomerOtherPayments() {
	return customerOtherPayments;
    }

    public void setCustomerOtherPayments(List customerOtherPayments) {
	this.customerOtherPayments = customerOtherPayments;
    }

    public String getParentCustomerKey() {
	return parentCustomerKey;
    }

    public void setParentCustomerKey(String parentCustomerKey) {
	this.parentCustomerKey = parentCustomerKey;
    }

    public Element getCustomerDefaultCurrency() {
	return customerDefaultCurrency;
    }

    public void setCustomerDefaultCurrency(Element customerDefaultCurrency) {
	this.customerDefaultCurrency = customerDefaultCurrency;
    }

    public String getIsChildCustomer() {
	return isChildCustomer;
    }

    public void setIsChildCustomer(String isChildCustomer) {
	this.isChildCustomer = isChildCustomer;
    }

    public String getWCCustomerId() {
	return wCCustomerId;
    }

    public void setWCCustomerId(String customerId) {
	wCCustomerId = customerId;
    }

    public String getWCOrganizationCode() {
	return wCOrganizationCode;
    }

    public void setWCOrganizationCode(String organizationCode) {
	wCOrganizationCode = organizationCode;
    }

    public List getCustomerNonInheritedDefaultAddressListEle() {
	return customerNonInheritedDefaultAddressListEle;
    }

    public void setCustomerNonInheritedDefaultAddressListEle(
	    List customerDefaultAddressListEle) {
	if (null != customerDefaultAddressListEle
		&& customerDefaultAddressListEle.size() > 0) {
	    Iterator itr = customerDefaultAddressListEle.iterator();
	    do {
		if (!itr.hasNext())
		    break;
		Element addrEle = (Element) itr.next();
		String isInherited = addrEle.getAttribute("IsInherited");
		if (!"Y".equals(isInherited))
		    customerNonInheritedDefaultAddressListEle.add(addrEle);
	    } while (true);
	}
    }

    public void setCustomerNonInheritedPaymentEle() {
	List customerAllPayments = new ArrayList();
	customerAllPayments.addAll(customerCreditCardPayments);
	customerAllPayments.addAll(customerAccountPayments);
	customerAllPayments.addAll(customerOtherPayments);
	if (null != customerAllPayments && customerAllPayments.size() > 0) {
	    Iterator itr = customerAllPayments.iterator();
	    do {
		if (!itr.hasNext())
		    break;
		Element paymentEle = (Element) itr.next();
		String isInherited = paymentEle.getAttribute("IsInherited");
		if (!"Y".equals(isInherited))
		    customerNonInheritedPaymentEle.add(paymentEle);
	    } while (true);
	}
    }

    public List getCustomerNonInheritedPaymentEle() {
	return customerNonInheritedPaymentEle;
    }

    public boolean isExpandAddressPanel() {
	return expandAddressPanel;
    }

    public void setExpandAddressPanel(boolean expandAddressPanel) {
	this.expandAddressPanel = expandAddressPanel;
    }

    Element customerOrganizationEle;
    Element customerList;
    Map childCustomersMap;
    List customerDefaultAddressListEle;
    List customerNonInheritedDefaultAddressListEle;
    List customerNonInheritedPaymentEle;
    List customerAddressListEle;
    Element customerDefaultCurrency;
    Map paymentMethodsMap;
    List customerCreditCardPayments;
    List customerAccountPayments;
    List customerOtherPayments;
    private String inputNs;
    private String customerId;
    private String organizationCode;
    private boolean expandAddressPanel;
    private String parentCustomerKey;
    private String OtherCustomerID;
    private String wCCustomerId;
    private String wCOrganizationCode;
    private String isChildCustomer;
}

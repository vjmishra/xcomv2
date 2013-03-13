/*
 * Action Class - Lists Customer Locations.
 * 
 * @author - adsouza
 */

package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;

public class XPEDXGetCustLocationList extends WCMashupAction {

    /**
     * 
     */
    private static final long serialVersionUID = -300465502714694281L;

    public XPEDXGetCustLocationList() {
	customerList = null;
	childCustomersMap = null;
	organizationCode = "";
	parentCustomerKey = "";
    }

    @Override
    public String execute() {

	try {
	    this.customerList = prepareAndInvokeMashup("cust-getCustomerList");
	    if (null != this.customerList) {
		List customerList = SCXmlUtil.getChildren(getCustomerList(),
			"Customer");
		childCustomersMap = new HashMap();
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

		    } while (true);
		}
	    }
	} catch (Exception ex) {
	    getWCContext().getSCUIContext().replaceAttribute(
		    "SCUI_EXCEPTION_ATTR", ex);
	    ex.printStackTrace();
	    return "error";
	}
	return "success";
    }

    public Element getCustomerList() {
	return customerList;
    }

    public void setCustomerList(Element customerList) {
	this.customerList = customerList;
    }

    public void setParentCustomerKey(String param) {
	parentCustomerKey = param;
    }

    public String getParentCustomerKey() {
	return parentCustomerKey;
    }

    public void setOrganizationCode(String param) {
	organizationCode = param;
    }

    public String getOrganizationCode() {
	return organizationCode;
    }

    public Map getChildCustomersMap() {
	return childCustomersMap;
    }

    public void setChildCustomersMap(Map childCustomersMap) {
	this.childCustomersMap = childCustomersMap;
    }

    private static final Logger log = Logger
	    .getLogger(XPEDXGetCustLocationList.class);
    String organizationCode;
    String parentCustomerKey;
    Element customerList;
    Map childCustomersMap;

}

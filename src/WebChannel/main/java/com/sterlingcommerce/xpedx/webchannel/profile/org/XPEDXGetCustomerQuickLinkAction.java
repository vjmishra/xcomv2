package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.Arrays;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.profile.user.QuickLinkBean;
import com.sterlingcommerce.xpedx.webchannel.profile.user.QuickLinkComparator;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

@SuppressWarnings("serial")
public class XPEDXGetCustomerQuickLinkAction extends WCMashupAction {

    private QuickLinkBean quickLinkBeanArray[];
    private String createSelected = "false";
    private String customerId;
    private String organizationCode;

    public String getCreateSelected() {
	return createSelected;
    }

    public void setCreateSelected(String createSelected) {
	this.createSelected = createSelected;
    }

    public QuickLinkBean[] getQuickLinkBeanArray() {
	return quickLinkBeanArray;
    }

    public void setQuickLinkBeanArray(QuickLinkBean[] quickLinkBeanArray) {
	this.quickLinkBeanArray = quickLinkBeanArray;
    }

    @Override
    public String execute() {
	Element outputElement = null;
	try {
	    XPEDXShipToCustomer shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils
		    .getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
	    if (shipToCustomer == null || shipToCustomer.getBillTo() == null) {
		return SUCCESS;
	    }
	    XPEDXShipToCustomer billToCustomerObj = shipToCustomer.getBillTo();
	    customerId = billToCustomerObj.getCustomerID();
	    /*
	     * boolean isSAPCustomeInSession=false; if(customerId == null ||
	     * customerId.trim().length() == 0) {
	     * 
	     * if(billToCustomerObj != null) { XPEDXShipToCustomer
	     * sapCustomerObj=billToCustomerObj.getBillTo(); if(sapCustomerObj
	     * != null) { customerId=sapCustomerObj.getCustomerID();
	     * if(customerId != null) isSAPCustomeInSession=true; } }
	     * if(isSAPCustomeInSession ==false) {
	     * 
	     * Document billToCustomer =
	     * XPEDXWCUtils.getCustomerDocument(billToCustomerObj
	     * .getCustomerID(), wcContext.getStorefrontId());
	     * if(billToCustomer!=null) { Element billToCustomerElem =
	     * billToCustomer.getDocumentElement();// billTo document Element
	     * sapCustElement = SCXmlUtil.getChildElement(billToCustomerElem,
	     * "ParentCustomer");// billTo's parent - SAP customerID
	     * if(sapCustElement!=null) customerId =
	     * sapCustElement.getAttribute("CustomerID");// SAPcustomerID }
	     * XPEDXShipToCustomer sapCustomer=new XPEDXShipToCustomer();
	     * billToCustomerObj.setBillTo(sapCustomer);
	     * XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER,
	     * shipToCustomer); Document SAPCustomerDoc =
	     * XPEDXOrderUtils.getCustomerExtnFlagsDoc(wcContext);
	     * if(SAPCustomerDoc!=null) customerId =
	     * SAPCustomerDoc.getDocumentElement().getAttribute("CustomerID"); }
	     * }
	     */
	    if (organizationCode == null
		    || organizationCode.trim().length() <= 0)
		organizationCode = wcContext.getStorefrontId();
	    if (organizationCode != null && customerId != null) {
		outputElement = prepareAndInvokeMashup("xpedx-getCustomerQuickLinks");
		String inputXml = SCXmlUtil.getString(outputElement);
		LOG.debug("Input XML: " + inputXml);
	    }
	} catch (XMLExceptionWrapper e) {
	    LOG.debug("Not able to retrieve Customer Quick Link:->"
		    + e.getMessage());
	    return ERROR;
	} catch (CannotBuildInputException e) {
	    LOG.debug("Not able to build input xml for Customer Quick Link:->"
		    + e.getMessage());
	    return ERROR;
	}

	QuickLinkBean quickLinkBean[] = null;

	try {
	    quickLinkBean = getQuickLink(outputElement);
	} catch (RuntimeException re) {
	    LOG.debug("Not able to parse output xml for Customer Quick Link:->"
		    + re.getMessage());
	    if (getCreateSelected().equalsIgnoreCase("true"))
		return "createsuccess";

	    return ERROR;
	}

	setQuickLinkBeanArray(quickLinkBean);

	if (getCreateSelected().equalsIgnoreCase("true"))
	    return "createsuccess";

	return SUCCESS;
    }

    private QuickLinkBean[] getQuickLink(Element outputElement)
	    throws RuntimeException {

	if (outputElement != null) {
	    Node quickLinkNodeList = outputElement.getFirstChild()
		    .getFirstChild().getFirstChild().getFirstChild();
	    if (!quickLinkNodeList.hasChildNodes())
		throw new RuntimeException();

	    NodeList quickLinkNode = quickLinkNodeList.getChildNodes();
	    QuickLinkBean quickLinkBean[] = new QuickLinkBean[quickLinkNode
		    .getLength()];

	    for (int i = 0; i < quickLinkNode.getLength(); i++) {
		Element quickLinkElement = (Element) quickLinkNode.item(i);
		quickLinkBean[i] = new QuickLinkBean();
		quickLinkBean[i].setUrlName(quickLinkElement
			.getAttribute("CustQuickLinkName"));
		quickLinkBean[i].setQuickLinkURL(quickLinkElement
			.getAttribute("CustQuickLinkUrl"));
		quickLinkBean[i].setUrlOrder(Integer.parseInt(quickLinkElement
			.getAttribute("CustURLOrder")));
		quickLinkBean[i].setShowQuickLink(quickLinkElement
			.getAttribute("CustShowQuickLink"));
	    }

	    Arrays.sort(quickLinkBean, new QuickLinkComparator());

	    return quickLinkBean;
	} else {
	    QuickLinkBean quickLinkBean[] = new QuickLinkBean[0];
	    return quickLinkBean;
	}

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
}

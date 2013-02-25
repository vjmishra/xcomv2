package com.sterlingcommerce.xpedx.webchannel.profile.user;

import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCDocument;

@SuppressWarnings("serial")
public class XPEDXCreateCustQLAction extends WCMashupAction {

    // String for Action Results
    private String REDIRECT = "redirect";
    private String bodyData;
    private String customerContactId;
    private String customerId;
    private String frmUserProfile1Flag = "";
    private String selectedTab;
    private boolean success;

    public boolean isSuccess() {
	return success;
    }

    public void setSuccess(boolean success) {
	this.success = success;
    }

    public String getSelectedTab() {
	return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
	this.selectedTab = selectedTab;
    }

    public String getFrmUserProfile1Flag() {
	return frmUserProfile1Flag;
    }

    public void setFrmUserProfile1Flag(String frmUserProfile1Flag) {
	this.frmUserProfile1Flag = frmUserProfile1Flag;
    }

    public String getBodyData() {
	return bodyData;
    }

    public void setBodyData(String bodyData) {
	this.bodyData = bodyData;
    }

    public String getCustomerContactId() {
	return customerContactId;
    }

    public void setCustomerContactId(String customerContactId) {
	this.customerContactId = customerContactId;
    }

    public String getCustomerId() {
	return customerId;
    }

    public void setCustomerId(String customerId) {
	this.customerId = customerId;
    }

    @Override
    public String execute() {

	Document outputDoc = null;
	Element input = prepareInputXML();

	String inputXml = SCXmlUtil.getString(input);

	LOG.debug("Input XML: " + inputXml);
	Object obj = WCMashupHelper.invokeMashup("manageCustomerQuickLink",
		input, wcContext.getSCUIContext());
	outputDoc = ((Element) obj).getOwnerDocument();
	if (null != outputDoc) {
	    LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
	}
	// Added condition For Jira 3196
	if (isSuccess()) {
	    setSuccess(true);
	}

	else {
	    setSuccess(false);
	}
	// Fix End For Jira 3196
	return REDIRECT;
    }

    private Element prepareInputXML() {

	Document templateCustomerDoc = YFCDocument.createDocument("Customer")
		.getDocument();
	Element templateElement = templateCustomerDoc.getDocumentElement();
	if (!(customerId != null && customerId.trim().length() > 0)) {
	    customerId = XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext);
	    if (!(customerId != null && customerId.trim().length() > 0))
		customerId = getWCContext().getCustomerId();
	}
	if (!(customerContactId != null && customerContactId.trim().length() > 0)) {
	    customerContactId = wcContext.getCustomerContactId();
	}
	templateElement.setAttribute("CustomerID", customerId);
	templateElement.setAttribute("OrganizationCode", getWCContext()
		.getStorefrontId());
	Element eleCustomerContactList = templateCustomerDoc
		.createElement("CustomerContactList");
	Element eleCustomerContact = templateCustomerDoc
		.createElement("CustomerContact");
	eleCustomerContact.setAttribute("CustomerContactID", customerContactId);
	Element eleExtn = templateCustomerDoc.createElement("Extn");
	Element eleXPXQuickLinkList = templateCustomerDoc
		.createElement("XPXQuickLinkList");
	eleXPXQuickLinkList.setAttribute("Reset", "true");

	Element eleXPXQuickLink = null;
	StringTokenizer st = new StringTokenizer(getBodyData(), "||");
	int i = 1;
	if (!getFrmUserProfile1Flag().equalsIgnoreCase("true")) {
	    // From UserProfile.jsp
	    while (st.hasMoreTokens()) {
		String token = st.nextToken();
		if (token.equals("*#?")) {
		    token = "";
		}

		if (i == 1) {
		    eleXPXQuickLink = templateCustomerDoc
			    .createElement("XPXQuickLink");
		    String[] arrstr = token.split("%");
		    String show = arrstr[0];
		    eleXPXQuickLink.setAttribute("ShowQuickLink", show.trim());
		    eleXPXQuickLink.setAttribute("QuickLinkName",
			    arrstr[1].trim());
		}

		if (i == 2)
		    eleXPXQuickLink.setAttribute("QuickLinkUrl", token);

		if (i == 3) {
		}
		if (i == 4) {
		}

		if (i == 5) {
		    eleXPXQuickLink.setAttribute("URLOrder", token);
		    eleXPXQuickLinkList.appendChild(eleXPXQuickLink);
		}

		i++;

		if (i == 6) {
		    i = 1;
		}

	    }
	} else {
	    while (st.hasMoreTokens()) {
		// From UserProfile1.jsp
		String token = st.nextToken();
		if (token.equals("*#?")) {
		    token = "";
		}

		if (i == 1) {
		    eleXPXQuickLink = templateCustomerDoc
			    .createElement("XPXQuickLink");
		    eleXPXQuickLink.setAttribute("QuickLinkName", token);
		}

		if (i == 2)
		    eleXPXQuickLink.setAttribute("QuickLinkUrl", token);

		if (i == 3) {
		    eleXPXQuickLink.setAttribute("ShowQuickLink", token);
		}
		if (i == 4) {
		    eleXPXQuickLink.setAttribute("URLOrder", token);
		    eleXPXQuickLinkList.appendChild(eleXPXQuickLink);
		}

		i++;

		if (i == 5) {
		    i = 1;

		}

	    }
	}

	eleExtn.appendChild(eleXPXQuickLinkList);
	eleCustomerContact.appendChild(eleExtn);
	eleCustomerContactList.appendChild(eleCustomerContact);
	templateElement.appendChild(eleCustomerContactList);

	return templateElement;
    }

}

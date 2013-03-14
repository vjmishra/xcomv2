/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.profile.ProfileUtility;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfc.util.YFCLocale;
import com.yantra.yfc.util.YFCTimeZone;

/**
 * @author rugrani
 * 
 */
@SuppressWarnings("all")
public class XPEDXGetArticle extends WCMashupAction {

	private String customerId;
	private String isChildCustomer;
	private String customerContactId;

	// ResourceIds
	private static final String USER_LIST_RESOURCE_ID = "/swc/profile/ManageUserList";
	private static final String MANAGE_USER_PROFILE_RESOURCE_ID = "/swc/profile/ManageUserProfile";

	protected List<Element> articleElements = null;

	public List CustomerIDList;
	public List customerIDNameList;
	protected String startTodayDate = "";
	protected String endTodayDate = "";

	protected XPEDXWCUtils xpedxWCUtilsBean;

	public XPEDXGetArticle() {
		super();
		CustomerIDList = new ArrayList();
		customerIDNameList = new ArrayList();
	}

	/*
	 * Get the root of the Notes on the object
	 * 
	 * @return string value with the path to the root of the notes element
	 */
	protected String getArticleLocation() {
		return "/XPXArticleList/XPXArticle";
	}

	public String execute() {
		customerContactId = getWCContext().getCustomerContactId();
		String inputCustomerID = customerId;
		if (YFCCommon.isVoid(inputCustomerID)) {
			inputCustomerID = wcContext.getCustomerId();
		}
		if (!(ProfileUtility.isCustInCtxCustHierarchy(inputCustomerID,
				wcContext)))
			return ERROR;

		// Get all the CustomerID's which needs to be included in the query.
		// Only those articles belonging to the CustomerID will be
		// displayed in the UI
		getParentCustomerList();
		setArticleElements();

		return SUCCESS;

	}

	public List<String> getParentCustomerList() {
		ArrayList<String> parentCustomerList = new ArrayList<String>();
		try {

			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/Customer/@OrganizationCode", getWCContext()
					.getStorefrontId());
			valueMap.put("/Customer/@CustomerID", getWCContext()
					.getCustomerId());

			Element input = WCMashupHelper.getMashupInput(
					"XPEDXParentCustomerList", valueMap, getWCContext()
							.getSCUIContext());

			Object obj = WCMashupHelper.invokeMashup("XPEDXParentCustomerList",
					input, getWCContext().getSCUIContext());

			Document outputDoc = ((Element) obj).getOwnerDocument();

			String xml = SCXmlUtil.getString(outputDoc);

			NodeList nlCustomer = outputDoc.getElementsByTagName("Customer");
			Element customer = null;
			int length = nlCustomer.getLength();
			for (int i = 0; i < length; i++) {
				customer = (Element) nlCustomer.item(i);
				String custID = SCXmlUtil.getAttribute(customer, "CustomerID");
				CustomerIDList.add(custID);
				customerIDNameList.add("CustomerID");
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return parentCustomerList;
	}

	public List<Element> getArticleLines() {
		if (this.articleElements == null) {
			setArticleElements();
		}
		return this.articleElements;
	}

	public void setArticleElements() {
		try {
			// Element listInput =
			// WCMashupHelper.getMashupAPI("XPEDXArticleList",getWCContext().getSCUIContext());
			Map retMap = prepareAndInvokeMashups();
			Set<String> mashupSet = getMashupIds();
			String mashupId = "";
			for (String i : mashupSet) {
				// return the first mashupId out of the set
				mashupId = i;
			}
			Element e = (Element) retMap.get(mashupId);
			String xml = SCXmlUtil.getString(e.getOwnerDocument());
			this.articleElements = getArticleElements(e, getArticleLocation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Element> getArticleElements(Element xapiOutput,
			String xpathToNoteElement) {

		List<Element> articleElements = new ArrayList<Element>();

		try {
			NodeList articleLines = (NodeList) XMLUtilities.evaluate(
					xpathToNoteElement, xapiOutput, XPathConstants.NODESET);
			int length = articleLines.getLength();

			for (int i = length - 1; i >= 0; i--) {
				Element currNode = (Element) articleLines.item(i);
				// TODO: add a filter here to see if this node should be added
				// or not. Only few users will be able to see the artilces.
				articleElements.add(currNode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return articleElements;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
		setIsChildCustomer(ProfileUtility.setCustomerAccess(customerId,
				wcContext));
	}

	public String getIsChildCustomer() {
		return isChildCustomer;
	}

	public void setIsChildCustomer(String isChildCustomer) {
		this.isChildCustomer = isChildCustomer;
	}

	/**
	 * @return true: if seeing own user profile
	 */
	public boolean isSelfAdmin() {
		String loggedInCustomerContactId = getWCContext()
				.getCustomerContactId();
		if (getCustomerContactId().compareTo(loggedInCustomerContactId) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return the customerContactId
	 */
	public String getCustomerContactId() {
		return customerContactId;
	}

	/**
	 * @param customerContactId
	 *            the customerContactId to set
	 */
	public void setCustomerContactId(String customerContactId) {
		this.customerContactId = customerContactId;
	}

	/**
	 * @return the customerIDList
	 */
	public List getCustomerIDList() {
		return CustomerIDList;
	}

	/**
	 * @param customerIDList
	 *            the customerIDList to set
	 */
	public void setCustomerIDList(List customerIDList) {
		CustomerIDList = customerIDList;
	}

	/**
	 * @return the buyerOrganizationCodeList
	 */
	public List getCustomerIDNameList() {
		return customerIDNameList;
	}

	/**
	 * @param buyerOrganizationCodeList
	 *            the buyerOrganizationCodeList to set
	 */
	public void setCustomerIDNameList(List buyerOrganizationCodeList) {
		customerIDNameList = buyerOrganizationCodeList;
	}

	/**
	 * @return the todaysDate
	 */
	public String getTodayDate() {
		YFCDate date = new YFCDate();
		YFCTimeZone oTz = new YFCTimeZone(YFCLocale.getDefaultLocale(), date);
		oTz.adjustToTimeZone(
				TimeZone.getTimeZone(wcContext.getSCUIContext()
						.getUserPreferences().getLocale().getTimezone()), date);
		String dateString = date.getString(wcContext.getSCUIContext()
				.getUserPreferences().getLocale().getDateFormat());
		return dateString;
	}

	/**
	 * @return the endTodayDate
	 */
	public String getEndTodayDate() {
		return getTodayDate();
	}

	/**
	 * @return the startTodayDate
	 */
	public String getStartTodayDate() {
		return getTodayDate();
	}

	public XPEDXWCUtils getXPEDXWCUtils() {
		if (xpedxWCUtilsBean == null)
			xpedxWCUtilsBean = new XPEDXWCUtils();
		return xpedxWCUtilsBean;
	}
}

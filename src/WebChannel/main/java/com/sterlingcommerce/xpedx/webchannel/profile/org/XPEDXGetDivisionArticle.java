/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer;
import com.sterlingcommerce.webchannel.profile.ProfileUtility;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.date.YDate;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfc.util.YFCLocale;
import com.yantra.yfc.util.YFCTimeZone;

@SuppressWarnings("all")
public class XPEDXGetDivisionArticle extends WCMashupAction {

    private String customerId;
    private String customerDivisionToQry;
    private String isChildCustomer;
    private String customerContactId;
    private String orgCode;

    // ResourceIds
    private static final String USER_LIST_RESOURCE_ID = "/swc/profile/ManageUserList";
    private static final String MANAGE_USER_PROFILE_RESOURCE_ID = "/swc/profile/ManageUserProfile";
    private static final String customerDivisionMashUp = "xpedx-customer-getCustomExtnFieldsInformation";

    protected List<Element> articleElements = null;

    public List custArticleSearchName;
    public List custArticleSearchValue;
    protected String startTodayDate = "";
    protected String endTodayDate = "";

    protected XPEDXWCUtils xpedxWCUtilsBean;

    public XPEDXGetDivisionArticle() {
	super();
	custArticleSearchName = new ArrayList();
	custArticleSearchValue = new ArrayList();
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
	String inputCustomerID = customerId;
	customerContactId = getWCContext().getCustomerContactId();
	if (YFCCommon.isVoid(inputCustomerID)) {
	    inputCustomerID = wcContext.getCustomerId();
	    customerId = inputCustomerID;
	}
	if (!(ProfileUtility.isCustInCtxCustHierarchy(inputCustomerID,
		wcContext)))
	    return ERROR;
	if (MANAGE_USER_PROFILE_RESOURCE_ID.equals(resourceId)) {
	    try {
		if (!(ResourceAccessAuthorizer.getInstance().isAuthorized(
			USER_LIST_RESOURCE_ID, getWCContext()))) {
		    if (!isSelfAdmin())
			return "";
		}
	    } catch (Exception e1) {
		return ERROR;
	    }
	}
	updateLastLoginDate();
	setArticleElements();
	return SUCCESS;

    }
    
	public void updateLastLoginDate()
	{
		
		/**** Added for Jira xb 621 
		 * Below code is used to check from table xpxCustomercontactextn to see if the 
		 * user has accepted the terms of access and if yes it would update the recently
		 *  logged in date to the Database.
		 */

		String termsofAccess =(String) wcContext.getWCAttribute("isTOAaccepted");
		if((termsofAccess != null) && termsofAccess.equals("Y"))
		{
			boolean createCCExtn = false;
			Map<String, String> attributeMap = new HashMap<String, String>();
			Element xpxCustContExtnEle = (Element) XPEDXWCUtils.getObjectFromCache("CustomerContExtnEle") ;
			String lastLoginDate = (String) XPEDXWCUtils.getObjectFromCache("LastLoginDate");
			String custContRefKey = (String) XPEDXWCUtils.getObjectFromCache("CustomerContactRefKey");

			if(xpxCustContExtnEle == null)
			{
				createCCExtn = true;// added if the customer contact has to be created for the first time.
			}

			if(custContRefKey!=null && custContRefKey.length()>0)
			{
				attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_REF_ATTR, custContRefKey);
			}
			attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_LAST_LOGIN_DATE, lastLoginDate);		
			Element outDoc = (Element)XPEDXWCUtils.updateXPXCustomerContactExtn(wcContext, customerContactId,createCCExtn, attributeMap);

		}
	}


    public List<Element> getArticleLines() {
	if (this.articleElements == null) {
	    setArticleElements();
	}
	return this.articleElements;
    }

    /**
     * JIRA 243 Modified getCustomerDetails method to consider the mashup to be
     * invoked so that, we get only the required information - here
     * ExtnCustomerDivision.
     * 
     * @param inputItems
     * @return
     */
    public void setArticleElements() {
	try {
	    // Element customerInfo =
	    // XPEDXWCUtils.getCustomerDetails(customerId,
	    // wcContext.getStorefrontId(),
	    // customerDivisionMashUp).getDocumentElement();
	    XPEDXShipToCustomer shipCustomer = (XPEDXShipToCustomer) XPEDXWCUtils
		    .getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
	    customerDivisionToQry = shipCustomer.getExtnCustOrderBranch();
	    String customerID = wcContext.getCustomerId();
	    orgCode = getWCContext().getStorefrontId();
	    startTodayDate = getStartTodayDate();
	    endTodayDate = getEndTodayDate();

	    /*
	     * Comment since the article is created on order branch level not on
	     * customer division. String[] customerIdParts =
	     * customerID.split("-"); customerDivisionToQry=customerIdParts[0];
	     * String customerDivision =
	     * SCXmlUtil.getXpathAttribute(customerInfo,
	     * "/Customer/Extn/@ExtnCustomerDivision"); customerDivisionToQry =
	     * '|'+customerDivision+'|';
	     */

	    /*
	     * Commented for jira 3640.
	     * custArticleSearchName.add("XPXDivision");
	     * custArticleSearchValue.add(customerDivisionToQry);
	     * custArticleSearchName.add("ArticleType");
	     * custArticleSearchValue.add("D");
	     */

	    // Start of jira 3640
	    Element input = WCMashupHelper.getMashupInput(
		    "XPEDXDivisionArticleList", wcContext);
	    input.setAttribute("StartDate", startTodayDate);
	    input.setAttribute("EndDate", endTodayDate);
	    Element xpxArticle = (Element) input.getElementsByTagName(
		    "XPXArticle").item(0);
	    Element complexQuery = SCXmlUtil.getChildElement(input,
		    "ComplexQuery");
	    Element orElement = SCXmlUtil.getChildElement(complexQuery, "Or");

	    Element andElement = SCXmlUtil.getChildElement(orElement, "And");
	    Element expElement = SCXmlUtil.createChild(andElement, "Exp");
	    expElement.setAttribute("Name", "XPXDivision");
	    expElement.setAttribute("Value", customerDivisionToQry);
	    expElement.setAttribute("QryType", "LIKE");
	    Element expElement1 = SCXmlUtil.createChild(andElement, "Exp");
	    expElement1.setAttribute("Name", "ArticleType");
	    expElement1.setAttribute("QryType", "LIKE");
	    expElement1.setAttribute("Value", "D");

	    Element and1Element = SCXmlUtil.createChild(orElement, "And");
	    orElement.appendChild(and1Element);
	    Element expElem = SCXmlUtil.createChild(and1Element, "Exp");
	    expElem.setAttribute("Name", "OrganizationCode");
	    expElem.setAttribute("Value", orgCode);
	    Element expElem1 = SCXmlUtil.createChild(and1Element, "Exp");
	    expElem1.setAttribute("Name", "ArticleType");
	    expElem1.setAttribute("Value", "S");
	    Element e = (Element) WCMashupHelper.invokeMashup(
		    "XPEDXDivisionArticleList", input,
		    wcContext.getSCUIContext());// prepareAndInvokeMashup("XPEDXDivisionArticleList");
	    // end of jira 3640 changes
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
	// added for jira 3640
	String inputFormat = "MM/dd/yyyy";
	YDate yDate = new YDate(dateString, inputFormat, true);
	return yDate.getString("yyyy-MM-dd'T'HH:mm:ss");
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

    public List getCustArticleSearchName() {
	return custArticleSearchName;
    }

    public void setCustArticleSearchName(List custArticleSearchName) {
	this.custArticleSearchName = custArticleSearchName;
    }

    public List getCustArticleSearchValue() {
	return custArticleSearchValue;
    }

    public void setCustArticleSearchValue(List custArticleSearchValue) {
	this.custArticleSearchValue = custArticleSearchValue;
    }
}

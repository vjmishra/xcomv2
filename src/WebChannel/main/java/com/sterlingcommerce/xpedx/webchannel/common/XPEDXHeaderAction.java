package com.sterlingcommerce.xpedx.webchannel.common;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.ui.web.framework.SCUILocalSession;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.platform.utils.SCUIPlatformUtils;
import com.sterlingcommerce.webchannel.catalog.helper.CatalogContextHelper;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.IWCContextBuilder;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.BusinessRuleUtil;
import com.sterlingcommerce.webchannel.utilities.SSLSwitchingHelper;
import com.sterlingcommerce.webchannel.utilities.UserPreferenceUtil;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.core.YFSSystem;

public class XPEDXHeaderAction extends WCMashupAction {

	protected String logoURL;
	protected String customerId;
	protected String organizationCode;
	private Boolean isMultiStepCheckoutObj = null;
	protected boolean procurementMyItemsLinkFlag = false;
	private static final String SWC_CHECKOUT_TYPE = "SWC_CHECKOUT_TYPE";
	private static final String ENC_USER_KEY = "ENC_USER_KEY";
	private Element customerOrganizationEle;
	protected HashMap customerFieldsMap;
	protected boolean viewInvoices = false;
	protected boolean viewReports = false;
	protected boolean approver = false;
	protected boolean estimator = false;
	private List<String> assignedShipTos=new ArrayList<String>();
	private Boolean shipToBanner = false;
	private XPEDXShipToCustomer shipToAddress;
	//Commenting since this is not required
	//could not get key directly on jsp so added the code.
	/*private String orderHeaderKey1 = null;
	private String orderHeaderKey1 = null;

	public String getOrderHeaderKey1() {
		return orderHeaderKey1;
	}

	public void setOrderHeaderKey1(String orderHeaderKey1) {
		this.orderHeaderKey1 = orderHeaderKey1;
	}*/
	
	public XPEDXHeaderAction() {
		logoURL = null;
		request = null;
	}
	
	/*private Document getCustomerExtnFlagsDoc(IWCContext wcContext) {

		Document sapCustomer = null;
		Document billToCustomer = null;
		Document shipToCustomerDoc = null;
		String billToCustomerID = null;
		String sapCustomerID = null;
		if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext)){// if default ship to is available
			shipToCustomerDoc = XPEDXWCUtils.getCustomerDocument(wcContext.getCustomerId(), wcContext.getStorefrontId());// shipTo
			Element shipToCustomerElem = shipToCustomerDoc.getDocumentElement();
			Element billToCustElement = SCXmlUtil.getChildElement(shipToCustomerElem, "ParentCustomer");// parent of shipTo - billTo
			billToCustomerID = billToCustElement.getAttribute("CustomerID");// billTocustomerID
			if(!YFCUtils.isVoid(billToCustomerID)){
				billToCustomer = XPEDXWCUtils.getCustomerDocument(billToCustomerID, wcContext.getStorefrontId());// get billTo's parent
			}
			Element billToCustomerElem = billToCustomer.getDocumentElement();// billTo document
			Element sapCustElement = SCXmlUtil.getChildElement(billToCustomerElem, "ParentCustomer");// billTo's parent - SAP customerID
			sapCustomerID = sapCustElement.getAttribute("CustomerID");// SAPcustomerID
			if(!YFCUtils.isVoid(sapCustomerID)){
				sapCustomer = XPEDXWCUtils.getCustomerDocument(sapCustomerID, wcContext.getStorefrontId());// SAPCustomer EXTN details
			}
		}

		return sapCustomer;
	}
*/
	public String execute() {		
		try {
			//Removing  from AUTHORIZED_LOCATIONS and AVAILABLE_LOCATIONS session -Jira 4146
			Boolean sessionForUserProfile=  (Boolean) XPEDXWCUtils.getObjectFromCache("SessionForUserProfile");
			if(sessionForUserProfile == null || sessionForUserProfile != true)
			{
				XPEDXWCUtils.removeObectFromCache("AUTHORIZED_LOCATIONS");
				XPEDXWCUtils.removeObectFromCache("AVAILABLE_LOCATIONS");
				XPEDXWCUtils.removeObectFromCache("CUSTOMER2");
				
			}
			if(getWCContext().isGuestUser() && getWCContext().getCustomerId() != null && !("").equals(getWCContext().getCustomerId())) {				
				//get the builder object            
		        IWCContextBuilder builder = WCContextHelper.getBuilder(wcContext.getSCUIContext().getRequest(), wcContext.getSCUIContext().getResponse());
		        builder.setCustomerId(null);
			}	
			
			if (!getWCContext().isGuestUser()){
				handleChangeInContextForDefaultShipTo();
				prepareLoggerInUsersCustomerName();
			}
			
			XPEDXWCUtils.setObectInCache(XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT, "false");
			setupLogoURL();
			String servletPath = request.getServletPath();
			if (!getWCContext().isGuestUser()){
				prepareHeaderNavigationHighlight(servletPath);
			}
			if (!getWCContext().isGuestUser()){	
				//checkMultiStepCheckout();
				if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext))
					setSampleRequestFlagInSession();
			}
			//Commentting because this call is happening in Ajax while header page is loading
			/*if(isApprover()){
				getPendingOrdersCount();
			}
			if(XPEDXWCUtils.isCustomerSelectedIntoConext(getWCContext()))// Only getting the cart in context only if  ship to is selected - Jagadeesh
				orderHeaderKey1 = XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext());
			 */
		} catch (Exception e) {
			XPEDXWCUtils.logExceptionIntoCent(e.getMessage());
			return "error";
		}
		if (!getWCContext().isGuestUser()){
			//call to set the flags for users. Set this value only for authenticated user.
			// This is set in the handleChangeInContextForDefaultShipTo() method.
			//XPEDXWCUtils.setShowAddToCartAvaiablityAndReportsFlag(getWCContext());
			Document catObjFromCache = (Document)wcContext.getSCUIContext().getLocalSession().getAttribute("categoryCache");
			if(catObjFromCache != null)
	        {
				getCategories();
	        }
			setInvoiceDetails();
			
			setAdJuglerServerURL(XPEDXConstants.AJ_SERVER_URL );
			getWCContext().getSCUIContext().getSession().setAttribute(XPEDXConstants.AJ_SERVER_URL_KEY, getAdJuglerServerURL() );
		}
		
		/*String contextOrderHeaderKey=(String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");
		if(contextOrderHeaderKey == null && XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext) != null )
		{
			XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext());
		}*/
		return "success";
	}
	
	private void setInvoiceDetails() {
		//Getting customer suffix from customer ID and UserKey for View Invoices.
		String custId = getWCContext().getCustomerId();
		String[] custDetails = custId.split("-");
		if(custDetails!=null && custDetails.length>3)
		{
			custSuffix = custDetails[2];
			if (custSuffix != null && custSuffix.trim().length() > 0) {
				try {
					custSuffix = XPEDXWCUtils.encrypt(custSuffix);
				} catch (Exception e) {
					LOG.error("Error while encrypting customer suffix : "+ e);
				}
				custSuffix = URLEncoder.encode(custSuffix);
			}
		}
		//Getting the User Key from loginID
		String loginId = getWCContext().getLoggedInUserId();
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/User/@Loginid", loginId);
		Element inputEle;
		userKey = (String)getWCContext().getSCUIContext().getSession().getAttribute(ENC_USER_KEY);
		if (userKey == null || userKey.trim().equals("")) {
			try {
				inputEle = WCMashupHelper.getMashupInput("XPEDXUserListMashup",
						valueMap, wcContext.getSCUIContext());

				Element userEle = (Element)WCMashupHelper.invokeMashup("XPEDXUserListMashup",
						inputEle, wcContext.getSCUIContext());
				userKey = SCXmlUtil.getXpathAttribute(userEle, "//UserList/User/@UserKey");
				if(userKey != null && userKey.trim().length() > 0) {
					userKey = XPEDXWCUtils.encrypt(userKey);
					userKey = URLEncoder.encode(userKey);
					getWCContext().getSCUIContext().getSession().setAttribute(ENC_USER_KEY, userKey);
				}
				
			} catch (CannotBuildInputException e) {
				LOG.error("Error while getting user key : "+ e);
			} catch (Exception e) {
				LOG.error("Error while getting user key : "+ e);
			}
		}
				
		invoiceURL = YFSSystem.getProperty("xpedx.invoicing.url"); //to take it from properties files.
	}

	//Added by - Jira 1700
	private void setSampleRequestFlagInSession() throws CannotBuildInputException
	{
		if(null == wcContext.getSCUIContext().getSession().getAttribute("showSampleRequest")
				|| wcContext.getSCUIContext().getSession().getAttribute("showSampleRequest").equals(""))
		{
			XPEDXShipToCustomer shipToCustomer =(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache("shipToCustomer");
			//added for jira 2971
			if(shipToCustomer.getBillTo() != null)
				XPEDXWCUtils.setSampleRequestFlag(shipToCustomer.getBillTo().getExtnSampleRequestFlag(),shipToCustomer.getBillTo().getExtnServiceOptCode(),wcContext);
			// Commented for performance issue
			/*String billToCustomerId = XPEDXWCUtils.getParentCustomer(wcContext.getCustomerId(), wcContext);
			XPEDXWCUtils.setSampleRequestFlag(billToCustomerId,wcContext);*/
		}
	}
	//Ends


	private void handleChangeInContextForDefaultShipTo()
			throws CannotBuildInputException {
		//modified for jira 4285 - passing the customerContactList details to set the terms of access
		Document doc = null;
		if (getWCContext().isGuestUser()){
			return;
		}
		String msapId=wcContext.getCustomerId();
		if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext))
		{
			msapId=XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext);
		}
		
		XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
		String changeShiptOinContext = (String)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT);
		if(xpedxCustomerContactInfoBean == null || "true".equals(changeShiptOinContext) )
		{
			 doc = XPEDXWCUtils.getCustomerContactDetails(msapId);
			xpedxCustomerContactInfoBean = XPEDXWCUtils.setXPEDXCustomerContactInfoBean(doc, wcContext);
		}

		String defaultAssignedShipTo = "";
		
		if(xpedxCustomerContactInfoBean!=null) {
			welcomeUserFirstName = xpedxCustomerContactInfoBean.getFirstName();
			welcomeUserLastName = xpedxCustomerContactInfoBean.getLastName();
			welcomeLocaleId = xpedxCustomerContactInfoBean.getLocalecode();
			String viewInvoiceFlag = xpedxCustomerContactInfoBean.getExtnViewInvoices();
			if("Y".equalsIgnoreCase(viewInvoiceFlag)) {
				setViewInvoices(true);
			}
			if(xpedxCustomerContactInfoBean.isEstimator()) {
				setEstimator(true);
			}
			String viewReportFlag = xpedxCustomerContactInfoBean.getExtnViewReportsFlag();
			if("Y".equalsIgnoreCase(viewReportFlag)) {
				viewReports = Boolean.TRUE;
			}
			
			if("Y".equals(xpedxCustomerContactInfoBean.getIsApprover())) {
				setApprover(true);
			}

			String myItemsLink = xpedxCustomerContactInfoBean.getExtnMyItemsLink();
			if("Y".equalsIgnoreCase(myItemsLink)) 
				setProcurementMyItemsLinkFlag(true);		
			
			defaultAssignedShipTo = getDefaultShipTo(xpedxCustomerContactInfoBean);
		}

		if (!getWCContext().isGuestUser()
				&& !XPEDXWCUtils.isCustomerSelectedIntoConext(getWCContext())
				&&  defaultAssignedShipTo!= null) {
			XPEDXWCUtils.setCurrentCustomerIntoContext(defaultAssignedShipTo, getWCContext());
			setTermsOfAccessInRequest(doc);
		} else {
			if (!getWCContext().isGuestUser()
					&& !XPEDXWCUtils.isCustomerSelectedIntoConext(getWCContext())) {
				assignedShipTos = XPEDXWCUtils.getPaginatedAssignedCustomers(getWCContext());
				if(assignedShipTos.size() == 1) {
					XPEDXWCUtils.setCurrentCustomerIntoContext(assignedShipTos.get(0),getWCContext());
					setTermsOfAccessInRequest(doc);
				}
				else if(wcContext.getSCUIContext().getSession().getAttribute(
						XPEDXWCUtils.LOGGED_IN_FORMATTED_CUSTOMER_ID_MAP) == null){
					XPEDXWCUtils.getFormattedMasterCustomer(wcContext.getCustomerId(), wcContext.getStorefrontId());
					setTermsOfAccessInRequest(doc);
				}
			}//end of jira 4285
		}
	}
	
/*	private void handleChangeInContextForDefaultShipTo()
			throws CannotBuildInputException {
		
		// method which fetches all the customer contact details.
		if (getWCContext().isGuestUser()){
			return;
		}
		String msapId=wcContext.getCustomerId();
		if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext))
		{
			msapId=XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext);
		}
//		assignedShipTos = new ArrayList<String>();
		Document doc = XPEDXWCUtils.getCustomerContactDetails(msapId);
		
		Element contactElem = XPEDXWCUtils.getMSAPCustomerContactFromContacts(doc, wcContext);
		if(contactElem!=null) {
			welcomeUserFirstName = SCXmlUtil.getAttribute(contactElem, "FirstName");
			welcomeUserLastName = SCXmlUtil.getAttribute(contactElem, "LastName");
			welcomeLocaleId = SCXmlUtil.getXpathAttribute(contactElem, "//CustomerContact/User/@Localecode");
			Element extnElem = SCXmlUtil.getChildElement(contactElem, "Extn");
			String viewInvoiceFlag = SCXmlUtil.getAttribute(extnElem, "ExtnViewInvoices");
			String estimatorFlag = SCXmlUtil.getAttribute(extnElem, "ExtnEstimator");
			if("T".equalsIgnoreCase(viewInvoiceFlag)) {
				setViewInvoices(true);
			}
			if("T".equalsIgnoreCase(estimatorFlag)) {
				setEstimator(true);
			}
			String viewReportFlag = SCXmlUtil.getAttribute(extnElem, "ExtnViewReportsFlag");
			if("Y".equalsIgnoreCase(viewReportFlag)) {
				viewReports = Boolean.TRUE;
			}
			wcContext.setWCAttribute(XPEDXConstants.IS_ESTIMATOR,estimator,WCAttributeScope.LOCAL_SESSION);
			//Start -- Added this code and removed the call to XPEDXWCUtils.setShowAddToCartAvaiablityAndReportsFlag(wcContext)
			//Change for performance
			String viewPricesFlag = SCXmlUtil.getAttribute(extnElem, "ExtnViewPricesFlag");
			viewReportFlag = SCXmlUtil.getAttribute(extnElem, "ExtnViewReportsFlag");
			if(null != viewPricesFlag && viewPricesFlag.trim().length()>0){
				wcContext.getSCUIContext().getSession().setAttribute(
						"viewPricesFlag", viewPricesFlag);
			}
			if(null != viewReportFlag && viewReportFlag.trim().length()>0){
				wcContext.getSCUIContext().getSession().setAttribute(
						"viewReportsFlag", viewReportFlag);
			}
			//End
			Element userElem = SCXmlUtil.getChildElement(contactElem, "User");
			Element approverElem = SCXmlUtil.getElementByAttribute(userElem, "UserGroupLists/UserGroupList", "UsergroupKey", "BUYER-APPROVER");
			
			//Webtrends Tag starts
			if(wcContext.getSCUIContext().getSession().getAttribute("UsergroupKeyList") == null){
				List<Element> userGroupList = SCXmlUtil.getElements(userElem,"/UserGroupLists/UserGroupList");
				ArrayList newusergroupkey = new ArrayList(); 
				if (userGroupList != null) {
					for (Element userGroup : userGroupList) {
						String userGroupKey = userGroup.getAttribute("UsergroupKey");										
						newusergroupkey.add(userGroupKey);
					}				
					wcContext.getSCUIContext().getSession().setAttribute("UsergroupKeyList", newusergroupkey);
					wcContext.getSCUIContext().getSession().setAttribute("UsergroupKeyListActive", true);
				}	
			}		
			else {
				wcContext.getSCUIContext().getSession().setAttribute("UsergroupKeyListActive", false);
			}
			//Webtrends	tag ends
			
			if(approverElem!=null) {
				setApprover(true);
				wcContext.getSCUIContext().getSession().setAttribute(
						"isApprover", "Y");
				
			}
			Element customerElem = SCXmlUtil.getChildElement(contactElem, "Customer");
			Element customerExtnElem = SCXmlUtil.getChildElement(customerElem, "Extn");
			String myItemsLink = SCXmlUtil.getAttribute(customerExtnElem, "ExtnMyItemsLink");
			if("Y".equalsIgnoreCase(myItemsLink)) 
				setProcurementMyItemsLinkFlag(true);				
		}
		/* -- Removing this adding the getting the ohk in Line 118 in This class - Jagadeesh july 20th
		String isDefaultSet = (String) getWCContext().getWCAttribute(XPEDXConstants.IS_DEFAULT_CART_SET);
		String contextOrderHeaderKey = null;
		if(!"true".equalsIgnoreCase(isDefaultSet))
			contextOrderHeaderKey = XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext());
		if(contextOrderHeaderKey!=null && contextOrderHeaderKey.trim().length()>0)
			getWCContext().setWCAttribute(XPEDXConstants.IS_DEFAULT_CART_SET, "true", WCAttributeScope.LOCAL_SESSION);
		
		String defaultAssignedShipTo = getDefaultShipTo(doc);
		if (!getWCContext().isGuestUser()
				&& !XPEDXWCUtils.isCustomerSelectedIntoConext(getWCContext())
				&&  defaultAssignedShipTo!= null) {
			XPEDXWCUtils.setCurrentCustomerIntoContext(defaultAssignedShipTo, getWCContext());
			setTermsOfAccessInRequest();
		} else {
			if (!getWCContext().isGuestUser()
					&& !XPEDXWCUtils.isCustomerSelectedIntoConext(getWCContext())) {
				assignedShipTos = XPEDXWCUtils.getPaginatedAssignedCustomers(getWCContext());
				if(assignedShipTos.size() == 1) {
					XPEDXWCUtils.setCurrentCustomerIntoContext(assignedShipTos.get(0),getWCContext());
					setTermsOfAccessInRequest();
				}
				else if(wcContext.getSCUIContext().getSession().getAttribute(
						XPEDXWCUtils.LOGGED_IN_FORMATTED_CUSTOMER_ID_MAP) == null){
					XPEDXWCUtils.getFormattedMasterCustomer(wcContext.getCustomerId(), wcContext.getStorefrontId());
					setTermsOfAccessInRequest();
				}
			}
		}
	}*/
	
	public String getPendingOrdersCount() {
		
		/*String servletPath = request.getServletPath();
		if (!getWCContext().isGuestUser()){
			prepareHeaderNavigationHighlight(servletPath);
		}*/
			List<String> userList = getApproverUserList();
			Document outputDoc;
			if(wcContext.getCustomerId()!=null && wcContext.getLoggedInUserId() != null) 
			{
				Map<String, String> valueMap = new HashMap<String, String>();
				//valueMap.put("/Order/@BuyerOrganizationCode", wcContext.getCustomerId());
				//valueMap.put("/Order/OrderHoldType/@ResolverUserId", this.wcContext.getLoggedInUserId());
				valueMap.put("/Order/OrderHoldType/@HoldType", XPEDXConstants.HOLD_TYPE_FOR_PENDING_APPROVAL);
				valueMap.put("/Order/@EnterpriseCode", this.wcContext.getStorefrontId());
				String customerId = this.wcContext.getCustomerId();
				if(XPEDXWCUtils.isCustomerSelectedIntoConext(getWCContext()))
					customerId = XPEDXWCUtils.getLoggedInCustomerFromSession(getWCContext());
				valueMap.put("/Order/@BillToID", customerId);
				if(userList!=null && userList.size()>=0)
					userList.add(this.wcContext.getCustomerContactId());			
				for(int i=0; i<userList.size();i++) {
					int expIndexVal = i+1;
					valueMap.put("/Order/OrderHoldType/ComplexQuery/Or/Exp["+expIndexVal+"]/@Name","ResolverUserId");
					valueMap.put("/Order/OrderHoldType/ComplexQuery/Or/Exp["+expIndexVal+"]/@Value",userList.get(i));			
				}
				
				Element input;
				try 
				{
					input = WCMashupHelper.getMashupInput(
							"XPEDXPendingApprovalOrders", valueMap, wcContext
									.getSCUIContext());
				
					String inputXml = SCXmlUtil.getString(input);
					
					log.debug("Input XML for Creating a Legacy order on Approval: " + inputXml);
					
					Object obj = WCMashupHelper.invokeMashup(
						"XPEDXPendingApprovalOrders", input, wcContext
								.getSCUIContext());
					outputDoc = ((Element) obj).getOwnerDocument();
					if (null != outputDoc) {
						log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
					}
					
					String orderTotalRecords = SCXmlUtil.getAttribute(outputDoc.getDocumentElement(), "TotalNumberOfRecords");
					if(!YFCUtils.isVoid(orderTotalRecords)){
						setPendingOrderRecords(orderTotalRecords);
					}
				} catch (CannotBuildInputException e) {
					LOG.error("Error while fetching the total count of Apprvoal Pending orders for user: "+wcContext.getLoggedInUserId());
					
				}
			}
		return "success";
	}
	
	public List<String> getApproverUserList() {
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/CustomerContact/@OrganizationCode", this.wcContext.getStorefrontId());
		valueMap.put("/CustomerContact/ComplexQuery/Or/Exp[1]/@Name", "ApproverProxyUserId");
		valueMap.put("/CustomerContact/ComplexQuery/Or/Exp[1]/@Value", this.wcContext.getCustomerContactId());
		Document outputDoc;
		Element input;
		List<String> userList = new ArrayList<String>();
		try {
			input = WCMashupHelper.getMashupInput(
					"userListForApproval", valueMap, wcContext
							.getSCUIContext());
		
			String inputXml = SCXmlUtil.getString(input);
			
			log.debug("Input XML for Creating a Legacy order on Approval: " + inputXml);
			
			Object obj = WCMashupHelper.invokeMashup(
				"userListForApproval", input, wcContext
						.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			if (null != outputDoc) {
				log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
				userList = setOutputForCustomerContactList(outputDoc);
			}
		}
		catch (CannotBuildInputException e) {
			LOG.error("Error while fetching the user list to get the approval orders using mashup id 'userListForApproval'");
		}		
		return userList;
	}
	
	private List<String> setOutputForCustomerContactList(Document outDoc)
    {
        Element customerContactList = outDoc.getDocumentElement();
        ArrayList nodeCustomerList = SCXmlUtil.getChildren(customerContactList, "CustomerContact");
        Iterator iter = nodeCustomerList.iterator();
        List<String> oUserList = new ArrayList<String>();
        do
        {
            if(!iter.hasNext())
                break;
            Element oNode = (Element)iter.next();
            String proxyApprover = oNode.getAttribute("ApproverProxyUserId");
            if(this.wcContext.getCustomerContactId().equals(proxyApprover))
            {
                oUserList.add(oNode.getAttribute("CustomerContactID"));
            }
        } while(true);
        return oUserList;
    }
	
	/**
	 * This is to fetch the default ship to
	 * @param outputDoc
	 * @return
	 */
	private String getDefaultShipTo(XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean) {
		
		String defaultShipTo = xpedxCustomerContactInfoBean.getExtnDefaultShipTo();
		if (defaultShipTo != null
				&& defaultShipTo.trim().length() == 0) {
			defaultShipTo = null;
		}
		String userPrefCategory = xpedxCustomerContactInfoBean.getExtnPrefCatalog();
		// set the flag to be used in jsp
		request.setAttribute("defaultShipTo", defaultShipTo);
		if(userPrefCategory != null && userPrefCategory.trim().length()>0) {
			request.setAttribute("userPrefCategory", userPrefCategory);
		}
		return defaultShipTo;
	}

/*	private String getDefaultShipTo(Document outputDoc) {
		
		String defaultShipTo = null;
		Element wElement = outputDoc.getDocumentElement();
		wElement = XPEDXWCUtils.getMSAPCustomerContactFromContacts(outputDoc, wcContext);
		wElement = SCXmlUtil.getChildElement(wElement, "Extn");
		defaultShipTo = SCXmlUtil.getAttribute(wElement,
				"ExtnDefaultShipTo");
		if (defaultShipTo != null
				&& defaultShipTo.trim().length() == 0) {
			defaultShipTo = null;
		}
		String userPrefCategory = SCXmlUtil.getAttribute(wElement,"ExtnPrefCatalog");
		// set the flag to be used in jsp
		request.setAttribute("defaultShipTo", defaultShipTo);
		if(userPrefCategory != null && userPrefCategory.trim().length()>0) {
			request.setAttribute("userPrefCategory", userPrefCategory);
			wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.USER_PREF_CATEGORY,userPrefCategory);
		}else{ // Added for JIRA 1969
			wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.USER_PREF_CATEGORY,"");
		}
		
		// the following is used to get the toaFlag to be used in jsp
		//setTermsOfAccessInRequest(wElement);
		return defaultShipTo;
	}*/

	/**
	 * Set the terms of access in the request. 
	 * This is done to avoid an extra call of a mashup
	 * to fetch the same details
	 * @param wElement
	 */
	/*private void setTermsOfAccessInRequest(Element wElement) {
		String toaFlag = null;
		toaFlag = SCXmlUtil.getAttribute(wElement, "ExtnAcceptTAndCFlag");
		if (toaFlag == null || (toaFlag != null && toaFlag.trim().length() == 0)) {
			toaFlag = "";
		}
		request.setAttribute("isTOAaccepted", toaFlag);
	}*/
	
	private void setTermsOfAccessInRequest(Document doc) {
		String toaFlag = null;
		String addnlEmailAddrs = null;
		String addnlPOList = null;
		String lastLoginDate = null;
		
		String customerContactId = wcContext.getCustomerContactId();
		Element xpxCustContExtnEle= XPEDXWCUtils.getXPXCustomerContactExtn(wcContext, customerContactId);
		
		if(xpxCustContExtnEle!=null){
			toaFlag = xpxCustContExtnEle.getAttribute("AcceptTAndCFlag");
			addnlEmailAddrs = xpxCustContExtnEle.getAttribute("AddnlEmailAddrs");
			addnlPOList = xpxCustContExtnEle.getAttribute("POList");
			lastLoginDate = xpxCustContExtnEle.getAttribute("LastLoginDate");
		}
		
		if (toaFlag == null || (toaFlag != null && toaFlag.trim().length() == 0)) {
			toaFlag = "";
		}
		if (addnlEmailAddrs == null || (addnlEmailAddrs != null && addnlEmailAddrs.trim().length() == 0)) {
			addnlEmailAddrs = "";
		}
		if (addnlPOList == null || (addnlPOList != null && addnlPOList.trim().length() == 0)) {
			addnlPOList = "";
		}
		//JIRA 3487 start
		String isSecuityQuestionset=null;
		//modifed for jira 4285  - reducing 1 call for getCustomerContactList
		NodeList customerContactList=null;
		if(doc == null){
		customerContactList=XPEDXWCUtils.getCustomerContactDetails(XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext)).getDocumentElement().getElementsByTagName("CustomerContact");
		}
		if(doc != null){
		customerContactList=doc.getDocumentElement().getElementsByTagName("CustomerContact");
		for(int i=0;i<customerContactList.getLength();i++)
		{
			Element _customerContactElem=(Element)customerContactList.item(i);
			String contactId=_customerContactElem.getAttribute("CustomerContactID");
			if(contactId!=null&&contactId.trim().equalsIgnoreCase(wcContext.getCustomerContactId())){						
				String authQuestion=SCXmlUtil.getXpathAttribute(_customerContactElem, "//CustomerContact/User/AuthQuestionList/AuthQuestion/@AuthQuestionKey");

				if (authQuestion != null && authQuestion.trim().length() >0) {
					isSecuityQuestionset="Y";
				}				
			}
			
		}
		}
		//end of jira 4285
		if (isSecuityQuestionset == null || (isSecuityQuestionset != null && isSecuityQuestionset.trim().length() == 0)) {
			isSecuityQuestionset = "N";
		}
		//JIRA 3487 end
		//request.setAttribute("isTOAaccepted", toaFlag);
		//isTOAaccepted=toaFlag;
		getWCContext().setWCAttribute("isTOAaccepted", toaFlag, WCAttributeScope.LOCAL_SESSION);
		getWCContext().setWCAttribute("addnlEmailAddrs", addnlEmailAddrs, WCAttributeScope.LOCAL_SESSION);
		getWCContext().setWCAttribute("addnlPOList", addnlPOList, WCAttributeScope.LOCAL_SESSION);
		getWCContext().setWCAttribute("lastLoginDate", lastLoginDate, WCAttributeScope.LOCAL_SESSION);
		getWCContext().setWCAttribute("setSecretQuestion", isSecuityQuestionset, WCAttributeScope.LOCAL_SESSION);//JIRA 3487
	}

	//Commented for Home performance issue
	/*private void checkMultiStepCheckout() throws Exception{
		
    	// read the value: SWC_CHECKOUT_TYPE from session
    	// if null, then fetch it from database else return the same
		String definitionFromSession = (String)wcContext.getWCAttribute(SWC_CHECKOUT_TYPE, WCAttributeScope.LOCAL_SESSION);
		
		// if the checkout type in multi_step change it to single_step.
		if (definitionFromSession==null && isMultiStepCheckout()){
			// since the value is being set as valueMap.put("/UserUiState/@Definition", "Single_Step");
			wcContext.setWCAttribute(SWC_CHECKOUT_TYPE,"Single_Step", WCAttributeScope.LOCAL_SESSION);
	    	
			IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/UserUiState/@ComponentName", "SWC_CHECKOUT_TYPE");
			valueMap.put("/UserUiState/@Definition", "Single_Step");
			valueMap.put("/UserUiState/@Loginid", context.getLoggedInUserId());
			valueMap.put("/UserUiState/@ScreenName", "Webchannel");
			valueMap.put("/UserUiState/@ApplicationName", "swc");
			Element input = WCMashupHelper.getMashupInput("updateUserPreference", valueMap, wcContext.getSCUIContext());
			Element outDoc = (Element)WCMashupHelper.invokeMashup("updateUserPreference",input , getWCContext().getSCUIContext());
			isMultiStepCheckoutObj = null;
		}
	}
    private boolean isMultiStepCheckout()
    {
        if(isMultiStepCheckoutObj == null)
        {
            String checkoutType = UserPreferenceUtil.getUserPreference("SWC_CHECKOUT_TYPE", getWCContext());
            if("Multi_Step".equals(checkoutType))
                isMultiStepCheckoutObj = Boolean.TRUE;
            else
                isMultiStepCheckoutObj = Boolean.FALSE;
        }
        return isMultiStepCheckoutObj.booleanValue();
    }*/
    /**
	 * @param servletPath
	 */
	protected void prepareHeaderNavigationHighlight(String servletPath) {
		if (YFCCommon.isVoid(servletPath)) {
			return;
		} else if (servletPath.contains("/jsp/home/XPEDXhome")) {
			setXpedxSelectedHeaderTab("HomeTab");
		} else if (servletPath.contains("/jsp/MyItems")) {
			setXpedxSelectedHeaderTab("MyItemTab");
		} else if (servletPath.contains("/jsp/catalog")) {
			setXpedxSelectedHeaderTab("CatalogTab");
		} else if (servletPath.contains("/jsp/home/XPEDXPortalHome")) {
			setXpedxSelectedHeaderTab("OrderTab");
		} else if (servletPath.contains("/jsp/order")) {
			setXpedxSelectedHeaderTab("OrderTab");
		} else if (servletPath.contains("/jsp/services")) {
			setXpedxSelectedHeaderTab("ServicesTab");
		} else if (servletPath.contains("/jsp/tools")) {
			setXpedxSelectedHeaderTab("ToolsTab");
		} else if (servletPath.contains("jsp/profile")||servletPath.contains("jsp/admin")) {
			setXpedxSelectedHeaderTab("AdminTab");
		}
	}

	public static List<String> getMyItemList(String customerContactId) {

		List<String> listOfAssignedCustomers = new ArrayList<String>();
		SCUIContext wSCUIContext = null;

		try {
			YFCDocument inputDocument = YFCDocument
					.createDocument("CustomerAssignment");
			YFCElement documentElement = inputDocument.getDocumentElement();

			IWCContext context = WCContextHelper
					.getWCContext(ServletActionContext.getRequest());
			wSCUIContext = context.getSCUIContext();

			if (customerContactId != null
					&& customerContactId.trim().length() > 0) {
				documentElement.setAttribute("UserId", customerContactId);
			} else {
				documentElement.setAttribute("UserId", context
						.getLoggedInUserId());
			}
			documentElement.setAttribute("OrganizationCode", context
					.getBuyerOrgCode());
			YFCDocument template = YFCDocument
					.getDocumentFor("<CustomerAssignmentList>"
							+ "<CustomerAssignment><Customer/>"
							+ "</CustomerAssignment>"
							+ "</CustomerAssignmentList>");
			YFCElement yfcElement = SCUIPlatformUtils.invokeXAPI(
					"getCustomerAssignmentList", inputDocument
							.getDocumentElement(), template
							.getDocumentElement(), wSCUIContext);
			YFCIterable<YFCElement> iteartor = yfcElement.getChildren();
			YFCElement wElement = null;
			while (iteartor.hasNext()) {
				wElement = iteartor.next();
				wElement = wElement.getFirstChildElement();
				String customer = wElement.getAttribute("CustomerID");
				if (!listOfAssignedCustomers.contains(customer)) {
					listOfAssignedCustomers.add(customer);
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}		
		return listOfAssignedCustomers;
	}

	/**
	 * 
	 */
	/**
	 * 
	 */
	protected String prepareLoggerInUsersCustomerName() {
		try {

			customerId = wcContext.getCustomerId();
			organizationCode = wcContext.getStorefrontId();
			XPEDXShipToCustomer shipToCustomer=new XPEDXShipToCustomer();
			XPEDXShipToCustomer billToCustomer=new XPEDXShipToCustomer();
			if (customerId != null && organizationCode != null) {
				shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
				String changeShiptOinContext = (String)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT);
				if(shipToCustomer == null || ("true".endsWith(changeShiptOinContext) ))
				{
					Map outputMaps = prepareAndInvokeMashups();
					customerOrganizationEle = (Element) outputMaps
							.get("xpedx-customer-getHeaderInformation");
					shipToCustomer=XPEDXWCUtils.setCustomerObjectInCache(customerOrganizationEle);
					XPEDXWCUtils.getShipToAddressOfCustomer(customerOrganizationEle,shipToCustomer);
				}
				billToCustomer=shipToCustomer.getBillTo();
				
				// set the logged in users Customer Name(Buyer Org's). This is
				// displayed in the Header - RUgrani
				
				setLoggerInUserCustomerName(shipToCustomer.getOrganizationName());
				/*Element extnElement = SCXmlUtil.getChildElement(
						customerOrganizationEle, "Extn");*/
				/*Element parentElem = SCXmlUtil.getChildElement(
						customerOrganizationEle, "ParentCustomer");*/
				
				shipToAddress = shipToCustomer;//XPEDXWCUtils.getShipToAddressOfCustomer(customerOrganizationEle);
				//Element parentExtnElem = SCXmlUtil.getChildElement(parentElem, "Extn");
				String custPrefcategory = shipToCustomer.getExtnCustomerClass();
				String shipFromDivision = shipToCustomer.getExtnShipFromBranch();
				//String customerUseSKU = extnElement.getAttribute("ExtnUseCustSKU");
				String industry = shipToCustomer.getExtnIndustry();
				String currencyCode = billToCustomer.getExtnCurrencyCode();
				String customerDivision = billToCustomer.getExtnCustomerDivision();
				welcomeUserShipToName = billToCustomer.getExtnCustomerName();
				String parentCustomerID=billToCustomer.getCustomerID();
				String envCode = shipToCustomer.getExtnEnvironmentCode();
				String companyCode = shipToCustomer.getExtnCompanyCode();
				String legacyCustomerNumber =shipToCustomer.getExtnLegacyCustNumber();
				/*wcContext.setWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH,shipFromDivision,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.CUSTOMER_DIVISION,customerDivision,WCAttributeScope.LOCAL_SESSION);
				//wcContext.setWCAttribute(XPEDXConstants.CUSTOMER_USE_SKU,customerUseSKU,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.INDUSTRY,industry,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.CUSTOMER_CURRENCY_CODE,currencyCode,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.BILL_TO_CUSTOMER,parentCustomerID,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute("ShipToForOrderSummaryPage",getLoggerInUserCustomerName(), WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute("BillToForOrderSummaryPage",billToCustomer.getOrganizationName(), WCAttributeScope.LOCAL_SESSION);*/
				
				//customerFieldsMap = XPEDXOrderUtils.getCustomerLineFieldMap(customerOrganizationEle.getOwnerDocument());
				//Changed here : Fetching doc from getCustomerExtnFlagsDoc instead 
				
				//String defaultShipToChanged = (String) wcContext.getWCAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED);
				
				/*
				 * Commenting below code for perofrmance issue . Now instead of loading in to the
				 */
			/* String defaultShipToChanged = (String) wcContext.getSCUIContext().getRequest().getSession().getAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED);
				if(YFCUtils.isVoid(defaultShipToChanged) || "true".endsWith(defaultShipToChanged)){
					// do this only when the default ship to is changed and when logging in for the first time.
					Document SAPCustomerDoc = XPEDXOrderUtils.getSAPCustomerExtnFlagsDoc(wcContext);
					customerFieldsMap = XPEDXOrderUtils.getSAPCustomerLineFieldMap(SAPCustomerDoc.getDocumentElement());
					wcContext.getSCUIContext().getRequest().getSession().setAttribute("customerFieldsSessionMap", customerFieldsMap);
					wcContext.getSCUIContext().getRequest().getSession().setAttribute("sapCustExtnFields", createSAPCustomerDoc(SAPCustomerDoc.getDocumentElement(),"SAP"));
					// reset the flag once used
					wcContext.getSCUIContext().getRequest().getSession().setAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,"false");
					//wcContext.setWCAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,"false",WCAttributeScope.LOCAL_SESSION);
				}
			  */
				// jira 2890 - adjuggler keyword - category prepended with
				// TEST keyword for dev and staging
				String keywordPrefix = "";
				try {
					keywordPrefix = YFSSystem
							.getProperty(XPEDXConstants.AD_JUGGLER_KEYWORD_PREFIX_PROP);
					if (keywordPrefix == null) {
						keywordPrefix = "";
					}
					else
					{
						keywordPrefix = keywordPrefix.trim();
					}
				} catch (Exception e) {
					log.debug("AD_JUGGLER Failed to get Prefix : (Property : yfs.xpedx.adjuggler.keyword.attribute.prefix) , Message : "
							+ e.getMessage());
				}
				if(custPrefcategory!=null && custPrefcategory.trim().length()>0) {
					request.setAttribute("custPrefCategory", custPrefcategory);
					wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY,custPrefcategory);
					if(custPrefcategory.equalsIgnoreCase("CJ"))
					{	
						wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY_DESC, keywordPrefix + "FacilitySupplies");
					} else if (custPrefcategory.equals("CG"))
					{
						wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY_DESC, keywordPrefix + "Graphics");
					} else if (custPrefcategory.equals("CU")){
						wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY_DESC, keywordPrefix + "Packaging");
					} else if(custPrefcategory.equals("CA")){
						wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY_DESC, keywordPrefix + "Paper");
					} else {
						wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY_DESC, keywordPrefix + "");
					}
				}
				/*wcContext.setWCAttribute(XPEDXConstants.ENVIRONMENT_CODE,envCode,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.COMPANY_CODE,companyCode,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.LEGACY_CUST_NUMBER,legacyCustomerNumber,WCAttributeScope.LOCAL_SESSION);*/
				
				if(shipToCustomer.getOrganizationName()!=null) {
					XPEDXConstants.USER_CUSTOMER_NAME = shipToCustomer.getOrganizationName();
				}
			}
		} catch (Exception ex) {
			log.error("Unable to get logged in users Customer Profile. "
					+ ex.getMessage());
			return "error";
		}

		return "success";
	}
	
	//Commented for performance issue now every thing is being cached and using it every where.
	/*
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
				shipToAddress = XPEDXWCUtils.getShipToAddressOfCustomer(customerOrganizationEle);
				Element parentExtnElem = SCXmlUtil.getChildElement(parentElem, "Extn");
				String custPrefcategory = extnElement.getAttribute("ExtnCustomerClass");
				String shipFromDivision = extnElement.getAttribute("ExtnShipFromBranch");
				//String customerUseSKU = extnElement.getAttribute("ExtnUseCustSKU");
				String industry = extnElement.getAttribute("ExtnIndustry");
				String currencyCode = parentExtnElem.getAttribute("ExtnCurrencyCode");
				String customerDivision = parentExtnElem.getAttribute("ExtnCustomerDivision");
				welcomeUserShipToName = parentExtnElem.getAttribute("ExtnCustomerName");
				String parentCustomerID=parentElem.getAttribute("CustomerID");
				String envCode = extnElement.getAttribute("ExtnEnvironmentCode");
				String companyCode = extnElement.getAttribute("ExtnCompanyCode");
				String legacyCustomerNumber =extnElement.getAttribute("ExtnLegacyCustNumber");
				wcContext.setWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH,shipFromDivision,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.CUSTOMER_DIVISION,customerDivision,WCAttributeScope.LOCAL_SESSION);
				//wcContext.setWCAttribute(XPEDXConstants.CUSTOMER_USE_SKU,customerUseSKU,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.INDUSTRY,industry,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.CUSTOMER_CURRENCY_CODE,currencyCode,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.BILL_TO_CUSTOMER,parentCustomerID,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute("ShipToForOrderSummaryPage",getLoggerInUserCustomerName(), WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute("BillToForOrderSummaryPage",SCXmlUtil.getChildElement(parentElem, "BuyerOrganization").getAttribute("OrganizationName"), WCAttributeScope.LOCAL_SESSION);
				
				//customerFieldsMap = XPEDXOrderUtils.getCustomerLineFieldMap(customerOrganizationEle.getOwnerDocument());
				//Changed here : Fetching doc from getCustomerExtnFlagsDoc instead 
				
				//String defaultShipToChanged = (String) wcContext.getWCAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED);
				
				
				 * Commenting below code for perofrmance issue . Now instead of loading in to the
				 
			 String defaultShipToChanged = (String) wcContext.getSCUIContext().getRequest().getSession().getAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED);
				if(YFCUtils.isVoid(defaultShipToChanged) || "true".endsWith(defaultShipToChanged)){
					// do this only when the default ship to is changed and when logging in for the first time.
					Document SAPCustomerDoc = XPEDXOrderUtils.getSAPCustomerExtnFlagsDoc(wcContext);
					customerFieldsMap = XPEDXOrderUtils.getSAPCustomerLineFieldMap(SAPCustomerDoc.getDocumentElement());
					wcContext.getSCUIContext().getRequest().getSession().setAttribute("customerFieldsSessionMap", customerFieldsMap);
					wcContext.getSCUIContext().getRequest().getSession().setAttribute("sapCustExtnFields", createSAPCustomerDoc(SAPCustomerDoc.getDocumentElement(),"SAP"));
					// reset the flag once used
					wcContext.getSCUIContext().getRequest().getSession().setAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,"false");
					//wcContext.setWCAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,"false",WCAttributeScope.LOCAL_SESSION);
				}
			  
				
				if(custPrefcategory!=null && custPrefcategory.trim().length()>0) {
					request.setAttribute("custPrefCategory", custPrefcategory);
					wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY,custPrefcategory);
					if(custPrefcategory.equalsIgnoreCase("CJ"))
					{	
						wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY_DESC,"Facility Supplies");
					} else if (custPrefcategory.equals("CG"))
					{
						wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY_DESC,"Graphics");
					} else if (custPrefcategory.equals("CU")){
						wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY_DESC,"Packaging");
					} else if(custPrefcategory.equals("CA")){
						wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY_DESC,"Paper");
					} else {
						wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY_DESC,"");
					}
				}
				wcContext.setWCAttribute(XPEDXConstants.ENVIRONMENT_CODE,envCode,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.COMPANY_CODE,companyCode,WCAttributeScope.LOCAL_SESSION);
				wcContext.setWCAttribute(XPEDXConstants.LEGACY_CUST_NUMBER,legacyCustomerNumber,WCAttributeScope.LOCAL_SESSION);
				
				if(sBuyerOrg.getAttribute("OrganizationName")!=null) {
					XPEDXConstants.USER_CUSTOMER_NAME = sBuyerOrg.getAttribute("OrganizationName");
				}
			}
		} catch (Exception ex) {
			log.error("Unable to get logged in users Customer Profile. "
					+ ex.getMessage());
			return "error";
		}

		return "success";
	}*/

	public String getLogoURL() {
		return logoURL;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}

	public String getSecureLoginURL() {
		return SSLSwitchingHelper.generateURL(wcContext, "https", "home",
				"loginFullPage", null);
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	private String getSFLogoDir() {
		String sfDir = "";
		if (wcContext != null)
			sfDir = wcContext.getStorefrontId();
		return sfDir;
	}

	private String getSFTheme() {
		String sfTheme = "";
		if (wcContext != null)
			sfTheme = wcContext.getStorefrontThemeId();
		return sfTheme;
	}

	private void setupLogoURL() {
		String logo = (String) wcContext.getWCAttribute("SF_THEME_LOGO",
				WCAttributeScope.LOCAL_SESSION);
		if (logo != null && logo.length() > 0) {
			log.debug((new StringBuilder()).append(
					"Found Logo URL in the context=").append(logo).toString());
			setLogoURL(logo);
			return;
		}
		ServletContext servletCtx = ServletActionContext.getServletContext();
		String baseLogoURL = (new StringBuilder()).append(
				request.getContextPath()).append("/swc/images/logo/").append(
				getSFLogoDir()).toString();
		logo = (new StringBuilder()).append(baseLogoURL).append("/logo.gif")
				.toString();
		String theme = getSFTheme();
		if (servletCtx != null && theme != null && theme.length() > 0) {
			String themeLogoURL = (new StringBuilder()).append(
					"/swc/images/logo/").append(getSFLogoDir())
					.append("/logo-").append(theme).append(".gif").toString();
			log.debug((new StringBuilder()).append(
					"Checking for theme specific logo [").append(themeLogoURL)
					.append("]").toString());
			try {
				java.net.URL url = servletCtx.getResource(themeLogoURL);
				log.debug((new StringBuilder()).append(
						"URL from servletContext=").append(url).toString());
				if (url != null) {
					log.debug((new StringBuilder()).append(
							"Using theme based LogoURL [").append(url).append(
							"]").toString());
					logo = (new StringBuilder()).append(baseLogoURL).append(
							"/logo-").append(theme).append(".gif").toString();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		log.debug((new StringBuilder()).append("Logo URL =[").append(logo)
				.append("]").toString());
		setLogoURL(logo);
		wcContext.setWCAttribute("SF_THEME_LOGO", logo,
				WCAttributeScope.LOCAL_SESSION);
	}

	/*private Document createSAPCustomerDoc(Element sapCustomerElement,String prefix)
	{
		Document sapCustomerDocument=SCXmlUtil.createDocument("Customer");
		try
		{
			
			Element sapCustomerExtnElem=SCXmlUtil.createChild(sapCustomerDocument.getDocumentElement(), "Extn");
			Element customerOrganizationExtnEle=(Element)(sapCustomerElement.getElementsByTagName("XPXCustHierarchyView")).item(0);
			sapCustomerExtnElem.setAttribute("ExtnCustLineAccNoFlag", SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineAccNoFlag"));
			sapCustomerExtnElem.setAttribute("ExtnCustLinePONoFlag",SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLinePONoFlag"));
			//Fix for not showing Seq Number as per Pawan's mail dated 17/3/2011
			/*String custSeqNoFlag = SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineSeqNoFlag");*/
			/*sapCustomerExtnElem.setAttribute("ExtnCustLineField1Flag",SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineField1Flag"));
			sapCustomerExtnElem.setAttribute("ExtnCustLineField2Flag",SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineField2Flag"));
			sapCustomerExtnElem.setAttribute("ExtnCustLineField3Flag",SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineField3Flag"));
			sapCustomerExtnElem.setAttribute("ExtnCustLineAccLbl",SCXmlUtil.getAttribute(
						customerOrganizationExtnEle, prefix+"ExtnCustLineAccLbl"));
			sapCustomerExtnElem.setAttribute("ExtnCustLineField1Label",SCXmlUtil.getAttribute(
						customerOrganizationExtnEle, prefix+"ExtnCustLineField1Label"));
			sapCustomerExtnElem.setAttribute("ExtnCustLineField2Label",SCXmlUtil.getAttribute(
						customerOrganizationExtnEle, prefix+"ExtnCustLineField2Label"));
			sapCustomerExtnElem.setAttribute("ExtnCustLineField3Label",SCXmlUtil.getAttribute(
						customerOrganizationExtnEle, prefix+"ExtnCustLineField3Label"));
		}
		catch(Exception e)
		{
			log.error("Error while crating SAP Custom fields :: "+e.getMessage());
		}
		return sapCustomerDocument;
	}*/
	public String getCategories() {
		SCUIContext uictx = getWCContext().getSCUIContext();
		Document outDoc = null;
		// Commented for Performance - filter.action
		outDoc = CatalogContextHelper.getCategoryFromCache(wcContext,
				categoryDepth);
		
		
		Element el1;
		SCUILocalSession localSession = wcContext.getSCUIContext()
				.getLocalSession();
		//outDoc=(Document) localSession.getAttribute("categoryCache");
		if (outDoc == null) {
			try {
				customerId = wcContext.getCustomerId();
				el1= prepareAndInvokeMashup(SEARCH_CATALOG_INDEX_MASHUP_ID);
				if(el1!=null){
					outDoc = el1.getOwnerDocument();
				}
			} catch (XMLExceptionWrapper e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CannotBuildInputException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (outDoc == null) {
			log.error("Exception in reading the Categories  ");
		} else {
			localSession.setAttribute("categoryCache", outDoc);
		}
		this.prodCategoryOutputDoc = outDoc;
		this.request.setAttribute("RootCatalogDoc", outDoc);
		uiContext = uictx;

		 String numOfCat = BusinessRuleUtil.getBusinessRule(
				SWC_BUSINESS_RULE_NO_CAT_TO_DISPLAY, getWCContext());
		if (!YFCCommon.isVoid(numOfCat)) {
			setNumOfCatToDisplay(Integer.parseInt(numOfCat));
		} else {
			setNumOfCatToDisplay(NO_CAT_TO_DISPLAY);
		}

		log.debug("Currently Displayed number of Categories is *************"
				+ numOfCatToDisplay);
		return "success";
	}

	private Document callMashup(SCUIContext uictx, String mashupId) {
		Element input = null;
		Element output = null;
		try {
			output = (Element) WCMashupHelper.invokeMashup(mashupId, input,
					uictx);
		} catch (Exception e) {
			log.error("Exception during the Mashup invocation::::mashupId="
					+ mashupId, e);
		}
		if (output != null) {

			return output.getOwnerDocument();
		}
		return null;
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

	public Document getProdCategoryOutputDoc() {
		return prodCategoryOutputDoc;
	}

	public void setProdCategoryOutputDoc(Document prodCategoryOutputDoc) {
		this.prodCategoryOutputDoc = prodCategoryOutputDoc;
	}

	public int getNumOfCatToDisplay() {
		return numOfCatToDisplay;
	}

	public void setNumOfCatToDisplay(int numOfCatToDisplay) {
		this.numOfCatToDisplay = numOfCatToDisplay;
	}

	public SCUIContext getUiContext() {
		return uiContext;
	}

	public void setUiContext(SCUIContext uiContext) {
		this.uiContext = uiContext;
	}

	public String getCategoryDepth() {
		return categoryDepth;
	}

	public void setCategoryDepth(String categoryDepth) {
		this.categoryDepth = categoryDepth;
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

	/**
	 * @return the xpedxSelectedHeaderTab
	 */
	public String getXpedxSelectedHeaderTab() {
		return xpedxSelectedHeaderTab;
	}

	/**
	 * @param xpedxSelectedHeaderTab
	 *            the xpedxSelectedHeaderTab to set
	 */
	public void setXpedxSelectedHeaderTab(String xpedxSelectedHeaderTab) {
		this.xpedxSelectedHeaderTab = xpedxSelectedHeaderTab;
	}

	public Document getSapCustomerExtnDoc() {
		return sapCustomerExtnDoc;
	}

	public void setSapCustomerExtnDoc(Document sapCustomerExtnDoc) {
		this.sapCustomerExtnDoc = sapCustomerExtnDoc;
	}



	private static final Logger log = Logger
			.getLogger(com.sterlingcommerce.xpedx.webchannel.common.XPEDXHeaderAction.class);
	private static final String SF_LOGO = "SF_THEME_LOGO";
	private HttpServletRequest request;
	private static final long serialVersionUID = 1L;
	// To get the Category list
	protected Document prodCategoryOutputDoc;
	private static final String SEARCH_CATALOG_INDEX_MASHUP_ID = "xpedxCatalogNavBar";
	private static final String SWC_BUSINESS_RULE_NO_CAT_TO_DISPLAY = "SWC_NUM_OF_CATEGORIES_DISPLAY";
	private static final int NO_CAT_TO_DISPLAY = 7;
	private int numOfCatToDisplay;
	public SCUIContext uiContext = null;
	protected String categoryDepth = "2"; // take the default
	protected String loggerInUserCustomerName = "";
	protected String welcomeUserFirstName = "";
	protected String welcomeUserLastName = "";
	protected String welcomeUserShipToName = "";
	protected String welcomeLocaleId = "";
	private String xpedxSelectedHeaderTab = "";
	private Document sapCustomerExtnDoc;
	private String pendingOrderRecords = "0";
	private String custSuffix = "";
	private String userKey = "";
	private String invoiceURL = "";
	private String adJuglerServerURL = "";
	
	
	public String getAdJuglerServerURL() {
		return adJuglerServerURL;
	}

	public void setAdJuglerServerURL(String adJugURL) {
		
//		String custOverridePropertiesFile = "customer_overrides.properties";
//		XPEDXWCUtils
//				.loadXPEDXSpecficPropertiesIntoYFS(custOverridePropertiesFile);

		// JIRA 2890 - AdJuggler keyword, TEST was added in url which is wrong,
		// so removed it
		/*
		 * String adJugglerSuffix =
		 * YFSSystem.getProperty(XPEDXConstants.AD_JUGGLER_SUFFIX_PROP);
		 * XPEDXConstants
		 * .logMessage("adJugglerSuffix : yfs.xpedx.adjuggler.suffix= " +
		 * adJugglerSuffix );
		 * 
		 * if(adJugglerSuffix != null ) this.adJuglerServerURL = adJugURL +
		 * adJugglerSuffix.trim(); else this.adJuglerServerURL = adJugURL;
		 */
		this.adJuglerServerURL = adJugURL;
	}
	
	
	public String getInvoiceURL() {
		return invoiceURL;
	}

	public void setInvoiceURL(String invoiceURL) {
		this.invoiceURL = invoiceURL;
	}

	public String getCustSuffix() {
		return custSuffix;
	}

	public void setCustSuffix(String custSuffix) {
		this.custSuffix = custSuffix;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public Element getCustomerOrganizationEle() {
		return customerOrganizationEle;
	}

	public void setCustomerOrganizationEle(Element customerOrganizationEle) {
		this.customerOrganizationEle = customerOrganizationEle;
	}

	public boolean isViewInvoices() {
		return viewInvoices;
	}

	public void setViewInvoices(boolean viewInvoices) {
		this.viewInvoices = viewInvoices;
	}

	public boolean isViewReports() {
		return viewReports;
	}

	public boolean isApprover() {
		return approver;
	}

	public void setApprover(boolean approver) {
		this.approver = approver;
	}

	public boolean isProcurementMyItemsLinkFlag() {
		return procurementMyItemsLinkFlag;
	}

	public void setProcurementMyItemsLinkFlag(boolean procurementMyItemsLinkFlag) {
		this.procurementMyItemsLinkFlag = procurementMyItemsLinkFlag;
	}

	public boolean isEstimator() {
		return estimator;
	}

	public void setEstimator(boolean estimator) {
		this.estimator = estimator;
	}

	public String getWelcomeUserFirstName() {
		return welcomeUserFirstName;
	}

	public void setWelcomeUserFirstName(String welcomeUserFirstName) {
		this.welcomeUserFirstName = welcomeUserFirstName;
	}

	public String getWelcomeUserLastName() {
		if(welcomeUserLastName!=null && welcomeUserLastName.length() > 25){
			welcomeUserLastName = welcomeUserLastName.substring(0, 25);
		}
		return welcomeUserLastName;
	}

	public void setWelcomeUserLastName(String welcomeUserLastName) {
		this.welcomeUserLastName = welcomeUserLastName;
	}

	public String getWelcomeUserShipToName() {
		if(welcomeUserShipToName!=null && welcomeUserShipToName.length()> 35){
		welcomeUserShipToName = welcomeUserShipToName.substring(0, 35);
		}
		return welcomeUserShipToName;
	}

	public void setWelcomeUserShipToName(String welcomeUserShipToName) {
		this.welcomeUserShipToName = welcomeUserShipToName;
	}

	public String getWelcomeLocaleId() {
		return welcomeLocaleId;
	}

	public void setWelcomeLocaleId(String welcomeLocaleId) {
		this.welcomeLocaleId = welcomeLocaleId;
	}
	
	/**
	 * @param pendingOrderRecords the pendingOrderRecords to set
	 */
	public void setPendingOrderRecords(String pendingOrderRecords) {
		this.pendingOrderRecords = pendingOrderRecords;
	}

	public List<String> getAssignedShipTos() {
		if(assignedShipTos == null)
			assignedShipTos = new ArrayList<String>();
		return assignedShipTos;
	}

	public void setAssignedShipTos(List<String> assignedShipTos) {
		this.assignedShipTos = assignedShipTos;
	}

	/**
	 * @return the pendingOrderRecords
	 */
	public String getPendingOrderRecords() {
		return pendingOrderRecords;
	}

	public Boolean getShipToBanner() {
		return shipToBanner;
	}

	public void setShipToBanner(Boolean shipToBanner) {
		this.shipToBanner = shipToBanner;
	}

	public XPEDXShipToCustomer getShipToAddress() {
		return shipToAddress;
	}

	public void setShipToAddress(XPEDXShipToCustomer shipToAddress) {
		this.shipToAddress = shipToAddress;
	}
	
	

}

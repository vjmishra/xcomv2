/**
 * This class provides the functionality to get Buyer User General Information.
 */

package com.sterlingcommerce.xpedx.webchannel.profile.user;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer;
import com.sterlingcommerce.webchannel.profile.ProfileUtility;
import com.sterlingcommerce.webchannel.profile.user.UserProfileHelper;
import com.sterlingcommerce.webchannel.utilities.CommonCodeUtil;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCConstants;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.YfsUtils;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXAlphanumericSorting;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.ui.backend.util.Util;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;

/* ENDS - Customer-User Profile Changes - adsouza */

/**
 * @author vsriram
 *
 */
public class XPEDXUserGeneralInfo extends WCMashupAction

{

	// Logger
	private static final Logger log = Logger
			.getLogger(XPEDXUserGeneralInfo.class);

	// mashups
	private static final String GET_CUSTOMER_CONTACT_MASHUP = "getXpedxCustomerContactDetails";
	private static final String GET_ORG_QUESTION_LIST_MASHUP = "getOrgSecretQuestionList";
	private static final String GET_ADMIN_LIST_MASHUP = "getAdminList";
	private static final String DATE_FORMAT_ON_USER_PROFILE = "MM/dd/yyyy";
	
	private List<String> assignedCustomers = null;
	private HashMap<String, String> assignedCustomersMap = null;
	
	// Elements
	private QuickLinkBean quickLinkBeanArray[];
	private Element customer;
	private Element customerContactList;
	private Element contact;
	private Element user;
	private Element customerAdmin;
	private Element loc;
	private Element userList;
	private Element custContactAddtnlAddress;
	// Address Ids of default addresses of the Customer Contact
	private String defShipToAddId = "";
	private String defBillToAddId = "";
	private String defSoldToAddId = "";
	private String customerContactId;
	private String buyerOrgCode;
	private String buyerOrgName;
	private String prefCategory;
	private String selectedTab;
	private String isUserProfile = "false";
	
	//XNGTP-533 - Last Login date
	private String extnLastLoginDate;
	private String addnlEmailAddrs = "";
	private String addnlPOList = "";

	// List variables storing various lists
	private List<String> adminLoginidList = new ArrayList<String>();
	private List<String> questionListForUser = new ArrayList<String>();
	private Map<String, String> questionListForOrg = new LinkedHashMap<String, String>();
	List<Element> defaultContactAddressList = new ArrayList();
	List<Element> defaultCustomerAddressList = new ArrayList();
	List<Element> defaultAddressList = new ArrayList();
	List<Element> otherCustomerAddressList = new ArrayList();
	List<Element> otherContactAddressList = new ArrayList();
	List<Element> otherAddressList = new ArrayList();
	Map<String, String> statusList = new LinkedHashMap<String, String>();
	Map<String, String> title = new LinkedHashMap<String, String>();
	Map<String, String> localeList = new LinkedHashMap<String, String>();
	List<String> userRoles = new ArrayList();
	Map<String, Boolean> allRoles = new HashMap();

	private String userId;
	private String userPassword = "*****";
	private String confirmPassword;
	private String organizationCode;
	private String contactStatus;
	private String customerStatus;
	private String effectiveStatus;

	private static final String CUST_ADD_ID_ATTR = "CustomerAdditionalAddressID";
	private static final String CUST_ADD_ELEM_XPATH = "/CustomerAdditionalAddressList/CustomerAdditionalAddress";

	private String customerID;
	private boolean expandAddressPanel = false;
	private boolean expandPhonePanel = false;

	// Mode of operation
	private static final String CREATE = "create";
	private String operation = "";

	private String isChildCustomer;

	// Resource Ids
	private static final String USER_LIST_RESOURCE_ID = "/swc/profile/ManageUserList";

	/* STARTS - Customer-User Profile Changes - adsouza */

	protected Map AddnlEmailAddrList = new LinkedHashMap();
	protected Map POList = new LinkedHashMap();

	/* ENDS - Customer-User Profile Changes - adsouza */
	protected String showspendingLimit="";
	protected String unformattedSpendingLimit="";
	protected String spendingLimit = "";
	protected String spendingLtCurrency = "";
	protected String primaryApprover = "";
	protected String alternateApprover = "";
	protected List<String> availableApproversList = new ArrayList<String>();
	protected String lastModifyUserId = "";
	protected String lastModifiedDate = "";
	protected String userCreatedDate = "";
	protected String contactFirstName = "";
	protected String contactLastName = "";
	protected Map<String, String> countriesMap = new HashMap<String, String>();
	private String currentSelTab;
	private boolean success;
	private boolean saveAddUser;

	public boolean isSaveAddUser() {
		return saveAddUser;
	}

	public void setSaveAddUser(boolean saveAddUser) {
		this.saveAddUser = saveAddUser;
	}

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

	public String createUser() {
		String inputCustomerID = customerID;
		if (YFCCommon.isVoid(inputCustomerID)) {
			inputCustomerID = wcContext.getCustomerId();
		}
		if (!(isCustInCtxCustHierarchy(inputCustomerID,	wcContext))) {
			return ERROR;
		}
		try {
			if ((!(ResourceAccessAuthorizer.getInstance().isAuthorized(
					USER_LIST_RESOURCE_ID, getWCContext())))
					&& (!isSelfAdmin())) {
				return ERROR;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			return ERROR;
		}
		setOperation(CREATE);
		// Locale List
		this.localeList = YfsUtils.getUserLocaleList(getWCContext());
		// call commoncodeutil
		try {
			setStatusList(CommonCodeUtil.getCommonCodes(
					CommonCodeUtil.CUSTOMER_STATUS_CCT, getWCContext()));
			setTitle(CommonCodeUtil.getCommonCodes(CommonCodeUtil.TITLE_CCT,
					getWCContext()));
		} catch (XPathExpressionException e) {
			log.error("Exception in getting Status List", e.getCause());
		} catch (XMLExceptionWrapper e) {
			log.error("Exception in getting Status List", e.getCause());
		} catch (CannotBuildInputException e) {
			log.error("Exception in getting Status List", e.getCause());
		}
		return SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	
	 private boolean isCustInCtxCustHierarchy(String inputCustomerID,IWCContext wcContext)
	 {
		 boolean retVal=false;
		 try
		 {
			 String wCCustomerId = wcContext.getCustomerId();
			 if(inputCustomerID.equals(wCCustomerId))
		            return true;
			 List<String> customerIdList=new ArrayList<String>();
			 customerIdList.add(wCCustomerId);
			 customerIdList.add(inputCustomerID);
			 Element customerList=XPEDXWCUtils.getCustomerListDetalis(customerIdList ,wcContext);
			 UtilBean utilBean=new UtilBean();
			 List<Element> customerContactList =utilBean.getElements(customerList, "//CustomerList/Customer");
			 if(customerContactList !=null && customerContactList.size() >1){
				 Element loggedinCustomerDetails=customerContactList.get(0);
				 Element inputCustomerDetails=customerContactList.get(1);
				 String inputCustRootCustomerKey = inputCustomerDetails.getAttribute("RootCustomerKey");//SCXmlUtils.getAttribute(inputCustomerDetails, "RootCustomerKey");
				 String logInCustRootCustomerKey = SCXmlUtil.getAttribute(loggedinCustomerDetails, "RootCustomerKey");
				 if(inputCustRootCustomerKey != null && logInCustRootCustomerKey != null && inputCustRootCustomerKey.equals(logInCustRootCustomerKey))
					 retVal= true;
				 else
					 retVal= false;
			 }
			 else{
				 retVal= false;
			 }
		 }
		 catch(Exception e)
		 {
			 LOG.debug("error while validating user"+e.getMessage());
		 }
		 return retVal;
	 }

	public String execute() {
		boolean isCustomerIDFromContext = false;
		XPEDXWCUtils.setObectInCache("SessionForUserProfile",true);
		if (log.isDebugEnabled()) {
			log.debug("customerContactID=" + customerContactId);
		}
		
		if(selectedTab!=null && selectedTab.length()>0)
		{
			getWCContext().getSCUIContext().getRequest().setAttribute("selectedTab", selectedTab);
		}
		customerContactId = getWCContext().getSCUIContext().getRequest()
				.getParameter("customerContactId");
		customerID = wcContext.getSCUIContext().getRequest().getParameter(
				"customerId");
		if (log.isDebugEnabled()) {
			log.debug("customerContactID from Request=" + customerContactId);
		}
		if (customerContactId == null) {
			customerContactId = getWCContext().getCustomerContactId();
			
		}
		if (customerID == null) {
			isCustomerIDFromContext = true;
			customerID = wcContext.getCustomerId();
		}
		Map map = getB2bCatalogViewMap();
		String inputCustomerID = customerID;
		if (YFCCommon.isVoid(inputCustomerID)) {
			inputCustomerID = wcContext.getCustomerId();
		}
		//Commented for jira 4297 performance fix.
		/*if (inputCustomerID!=null && !(ProfileUtility.isCustInCtxCustHierarchy(inputCustomerID,
				wcContext))) {
			return ERROR;
		}*/
		try {
			if ((!(ResourceAccessAuthorizer.getInstance().isAuthorized(
					USER_LIST_RESOURCE_ID, getWCContext())))
					&& (!isSelfAdmin())) {
				return ERROR;
			}
		} catch (Exception e1) {
			return ERROR;
		}

		if (log.isDebugEnabled()) {
			log.debug("customerContactID from WCContext=" + customerContactId);
		}
		if(!customerContactId.equalsIgnoreCase(wcContext.getCustomerContactId())) {
			String CustomerIDToCheckForHierarchy;
			if(isCustomerIDFromContext) {
				CustomerIDToCheckForHierarchy = getCustomerID();
			}
			else {
				CustomerIDToCheckForHierarchy = customerID;
			}
			this.isChildCustomer = XPEDXWCUtils.setCustomerAccess(CustomerIDToCheckForHierarchy,
					wcContext);
		}else {
			this.isChildCustomer = "TRUE";
		}
		try
		{
			putAllAvailableAndAuthorizeLocationTOCache(wcContext);
		}
		catch(Exception e)
		{
			log.error("Exception while Setting Authorized and available location in to cache");
			//XPEDXWCUtils.logExceptionIntoCent(e);
		}
		//setQuickLinksForUser();		Performance Fix - Removed a mashup call for getCustomerQuickLink
		return getUserDetails();
	}
	
	public void setQuickLinksForUser(){
		Element outputElement = null;
		try {
			Map<String, String> valueMap1 = new HashMap<String, String>();
			valueMap1.put("/CustomerContact/Customer/@CustomerID", getCustomerID());
			valueMap1.put("/CustomerContact/Customer/@OrganizationCode",getWCContext().getStorefrontId());
			valueMap1.put("/CustomerContact/@CustomerContactID",getCustomerContactId());
			
			Element input1;
			input1 = WCMashupHelper.getMashupInput("getCustomerQuickLink",
					valueMap1, wcContext.getSCUIContext());
			Object obj1 = WCMashupHelper.invokeMashup("getCustomerQuickLink",
					input1, wcContext.getSCUIContext());
			outputElement = ((Element) obj1).getOwnerDocument().getDocumentElement();
			
			String inputXml = SCXmlUtil.getString(outputElement);
			LOG.debug("Input XML: " + inputXml);
		} catch (XMLExceptionWrapper e) {			
			LOG.debug("Not able to retrieve Customer Quick Link:->"
					+ e.getMessage());
		} catch (CannotBuildInputException e) {
			LOG.debug("Not able to build input xml for Customer Quick Link:->"
					+ e.getMessage());
		}
		
		QuickLinkBean quickLinkBean[] = null;
		
		try {
			quickLinkBean = getQuickLink(outputElement);
		} catch (RuntimeException re) {
			LOG.debug("Not able to parse output xml for Customer Quick Link:->"
					+ re.getMessage());			
		}
		
		setQuickLinkBeanArray(quickLinkBean);
	}
	
	private QuickLinkBean[] getQuickLink(Element outputElement)throws RuntimeException {
	  	
		Element customerContactExtn = SCXmlUtil.getChildElement(outputElement, "Extn");
		Node quickLinkNodeList = SCXmlUtil.getChildElement(customerContactExtn, "XPXQuickLinkList");
		if(!quickLinkNodeList.hasChildNodes())
			throw new RuntimeException();
		
		NodeList quickLinkNode = quickLinkNodeList.getChildNodes();
		QuickLinkBean quickLinkBean[] = new QuickLinkBean[quickLinkNode.getLength()];
		
		for (int i=0; i<quickLinkNode.getLength(); i++) {
			Element quickLinkElement = (Element)quickLinkNode.item(i);
			quickLinkBean[i] = new QuickLinkBean();
			quickLinkBean[i].setUrlName(quickLinkElement.getAttribute("QuickLinkName"));
			quickLinkBean[i].setQuickLinkURL(quickLinkElement.getAttribute("QuickLinkUrl"));
			quickLinkBean[i].setUrlOrder(Integer.parseInt(quickLinkElement.getAttribute("URLOrder")));
			quickLinkBean[i].setShowQuickLink(quickLinkElement.getAttribute("ShowQuickLink"));
		}
			
			Arrays.sort(quickLinkBean, new QuickLinkComparator());
	
			return quickLinkBean;
	}
	
	public void putAllAvailableAndAuthorizeLocationTOCache(IWCContext wcContext) throws Exception{
		Document outDoc = null;
		Object obj = null;
		//To get All Available Customers and keep it in Session
		String rootCustomerKey = (String)wcContext.getWCAttribute(XPEDXWCUtils.LOGGED_IN_CUSTOMER_KEY);
		HashMap<String,String> valueMap = new HashMap<String, String>();
		valueMap.put("/XPXCustView/@RootCustomerKey", rootCustomerKey);
		valueMap.put("/XPXCustView/@UserID", customerContactId);
		Element input = WCMashupHelper.getMashupInput("XPEDXgetAvailableLocFromService", valueMap, wcContext);			
		obj = WCMashupHelper.invokeMashup("XPEDXgetAvailableLocFromService", input, wcContext.getSCUIContext());
		//Element ele1=(Element)outDoc.getElementsByTagName("XPXCustView").item(0);
		if(obj!= null)
			outDoc = ((Element)obj).getOwnerDocument();
		
		Element ele2= (Element) outDoc.getDocumentElement();
		ArrayList<Element> availCustomerList = SCXmlUtil.getElements(ele2, "/XPXCustView");
		ArrayList<String> availableCustomerList=new ArrayList<String>();
		for(Element availCust:availCustomerList)
		{
			String customerID=availCust.getAttribute("CustomerID");
			availableCustomerList.add(customerID);
		}
		ArrayList<String> assignedCustomerList = (ArrayList<String>)XPEDXWCUtils.getAssignedCustomers(customerContactId);
		ArrayList<String> allCustomerList=new ArrayList<String>(availableCustomerList);
		allCustomerList.addAll(assignedCustomerList);		
		Map<String, String> authorizedFullAddrMap=XPEDXWCUtils.custFullAddresses(allCustomerList,wcContext.getStorefrontId(),true,true);
		List<Element> customerMap=new ArrayList<Element>();		
		for(Element availCust:availCustomerList)
		{
			String customerID=availCust.getAttribute("CustomerID");
			String customerPath=availCust.getAttribute("CustomerPath");
			createCustomerElement(customerMap,customerID,customerPath,authorizedFullAddrMap.get(customerID),-1);
		}
		XPEDXWCUtils.setObectInCache("AVAILABLE_LOCATIONS", customerMap);
		
		//To get All Authorized Customer
		//Put your mashup code here- Kubra 4146
		try {
			
				List<String> customers2 = getCustomersByPath(assignedCustomerList,authorizedFullAddrMap);
			
			} catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	private List<String> getCustomersByPath(ArrayList<String> wList,Map<String, String> customerFullAddr) throws Exception
	{
		
		List<Element> customerMap=new ArrayList<Element>();
		List<String> shipToStr = new ArrayList<String>();
		if(wList != null && wList.size()>0){
			String pageNumber="1";
			String pageSize ="10000";
			
			HashMap<String,String> valueMap = new HashMap<String, String>();
			valueMap.put("/Page/@PageNumber", pageNumber);
			valueMap.put("/Page/@PageSize", pageSize);
			
			YFCDocument inputCustomerDocument = YFCDocument
			.createDocument("XPXCustHierarchyView");
			YFCElement inputCustomerElement = inputCustomerDocument
					.getDocumentElement();
			YFCElement complexQueryElement = inputCustomerDocument
			.createElement("ComplexQuery");
			YFCElement sort = inputCustomerDocument.createElement("OrderBy");
			YFCElement attribute = inputCustomerDocument.createElement("Attribute");
			attribute.setAttribute("Name", "CustomerPath");
			attribute.setAttribute("Desc", "N");
			sort.appendChild(attribute);
			
			YFCElement orElement = inputCustomerDocument
			.createElement("Or");
			
			for(String customerID :wList) {
				createInput(customerID,complexQueryElement,orElement);
			}
			
			complexQueryElement.appendChild(orElement);
			inputCustomerElement.appendChild(complexQueryElement);
			inputCustomerElement.appendChild(sort);		
			
			Element input = WCMashupHelper.getMashupInput("xpedx-getCusotmerByCustomerPath",valueMap, wcContext);
			
			
			Element pageElement=(Element)input.getElementsByTagName("Input").item(0);
			pageElement.appendChild(input.getOwnerDocument().importNode(inputCustomerDocument.getDocument().getDocumentElement(), true));
			
			
			Object obj = WCMashupHelper.invokeMashup("xpedx-getCusotmerByCustomerPath", input, wcContext.getSCUIContext());
			
			Document outputDoc = ((Element) obj).getOwnerDocument();
					
			Element outputCustomerElement = (Element)outputDoc.getElementsByTagName("XPXCustHierarchyViewList").item(0);
			
			if (null != outputCustomerElement) {
				ArrayList<Element> xpxCustViewElems=SCXmlUtil.getElements(outputCustomerElement, "XPXCustHierarchyView");
				for(int j=0;j<xpxCustViewElems.size();j++)
				{
					if (wList.contains(xpxCustViewElems.get(j).getAttribute("MSAPCustomerID")) && !shipToStr.contains(xpxCustViewElems.get(j).getAttribute("MSAPCustomerID"))) {
							shipToStr.add(xpxCustViewElems.get(j).getAttribute("MSAPCustomerID"));
							createCustomerElement(customerMap,xpxCustViewElems.get(j).getAttribute("MSAPCustomerID"),xpxCustViewElems.get(j).getAttribute("CustomerPath"),
									customerFullAddr.get(xpxCustViewElems.get(j).getAttribute("MSAPCustomerID")),0);
						}if(wList.contains(xpxCustViewElems.get(j).getAttribute("SAPCustomerID")) && !shipToStr.contains(xpxCustViewElems.get(j).getAttribute("SAPCustomerID"))){
							shipToStr.add(xpxCustViewElems.get(j).getAttribute("SAPCustomerID"));
							createCustomerElement(customerMap,xpxCustViewElems.get(j).getAttribute("SAPCustomerID"),xpxCustViewElems.get(j).getAttribute("CustomerPath"),
									customerFullAddr.get(xpxCustViewElems.get(j).getAttribute("SAPCustomerID")),1);
						}if(wList.contains(xpxCustViewElems.get(j).getAttribute("BillToCustomerID")) && !shipToStr.contains(xpxCustViewElems.get(j).getAttribute("BillToCustomerID"))){
							shipToStr.add(xpxCustViewElems.get(j).getAttribute("BillToCustomerID"));
							createCustomerElement(customerMap,xpxCustViewElems.get(j).getAttribute("BillToCustomerID"),xpxCustViewElems.get(j).getAttribute("CustomerPath"),
									customerFullAddr.get(xpxCustViewElems.get(j).getAttribute("BillToCustomerID")),2);
						}if(wList.contains(xpxCustViewElems.get(j).getAttribute("ShipToCustomerID")) && !shipToStr.contains(xpxCustViewElems.get(j).getAttribute("ShipToCustomerID"))){
							shipToStr.add(xpxCustViewElems.get(j).getAttribute("ShipToCustomerID"));
							createCustomerElement(customerMap,xpxCustViewElems.get(j).getAttribute("ShipToCustomerID"),xpxCustViewElems.get(j).getAttribute("CustomerPath"),
									customerFullAddr.get(xpxCustViewElems.get(j).getAttribute("ShipToCustomerID")),-1);
						}
					
					} // end for  j=0 for loop
			}
		}
		XPEDXWCUtils.setObectInCache("AUTHORIZED_LOCATIONS", customerMap);
		XPEDXWCUtils.setObectInCache("CUSTOMER2", wList);
		return shipToStr;
		
	}
	private void createCustomerElement(List<Element> customerList,String customerID,String customerPath,String customerFullAddress,int index)
	{
		Element customerElemet=SCXmlUtil.createDocument("Customer").getDocumentElement();
		customerElemet.setAttribute("CustomerID", customerID);
		String customerPaths[]=customerPath.split("\\|");
		StringBuilder tempString=new StringBuilder("");
		for(int i=0;i<=index;i++)
		{
			tempString.append(customerPaths[i]);
		}
		if(index >-1)
			customerPath=tempString.toString();
		customerPath=customerPath.replaceAll("\\|", "");
		customerElemet.setAttribute("CustomerPath", customerPath);
		customerElemet.setAttribute("CustomerAddress", customerFullAddress);
		customerList.add(customerElemet);
	}
	
	private void createInput(String customerID,YFCElement complexQuery, YFCElement or)
	{
		YFCElement shipToExp = or.createChild("Exp");
		shipToExp.setAttribute("Name", "ShipToCustomerID");
		shipToExp.setAttribute("Value", customerID);
		
		YFCElement billToExp = or.createChild("Exp");
		billToExp.setAttribute("Name", "BillToCustomerID");
		billToExp.setAttribute("Value", customerID);
		or.appendChild(billToExp);

		YFCElement sapExp = or.createChild("Exp");
		sapExp.setAttribute("Name", "SAPCustomerID");
		sapExp.setAttribute("Value", customerID);
		or.appendChild(sapExp);
		
		YFCElement msapExp = or.createChild("Exp");
		msapExp.setAttribute("Name", "MSAPCustomerID");
		msapExp.setAttribute("Value", customerID);
		or.appendChild(msapExp);
		
		complexQuery.appendChild(or);
	}
	
	/**
	 * Gets the Buyer User General Information, Buyer user Addresses, Buyer User
	 * Phonebook, Buyer User Admin List
	 *
	 * @return the action restult
	 */
	public String getUserDetails() {
		//
		try {
			// call commoncodeutil
			setStatusList(CommonCodeUtil.getCommonCodes(
					CommonCodeUtil.CUSTOMER_STATUS_CCT, getWCContext()));
			statusList.remove("20");
			setTitle(CommonCodeUtil.getCommonCodes(CommonCodeUtil.TITLE_CCT,
					getWCContext()));
			Map<String, Element> outputMaps = prepareAndInvokeMashups();
			this.customerContactList = outputMaps
					.get("getXpedxCustomerContactDetailsNew");
			SCXmlUtil.getString(customerContactList);
			NodeList customerContact = customerContactList
					.getElementsByTagName("CustomerContact");
			Element contactElem = XPEDXWCUtils.getMSAPCustomerContactFromContacts(customerContactList.getOwnerDocument(), wcContext);
			
			QuickLinkBean quickLinkBean[] = null;
			
			try {
				quickLinkBean = getQuickLink(contactElem);
			} catch (RuntimeException re) {
				LOG.debug("Not able to parse output xml for Customer Quick Link:->"
						+ re.getMessage());			
			}
			
			setQuickLinkBeanArray(quickLinkBean);
			
			Element extnElem = SCXmlUtil.getChildElement(contactElem, "Extn");
			stockCheckWebservice = extnElem.getAttribute("ExtnStockCheckWS");
			viewInvoices = extnElem.getAttribute("ExtnViewInvoices");
			estimator = extnElem.getAttribute("ExtnEstimator");
			punchoutUsers = extnElem.getAttribute("ExtnPunchOutUser");
			prefCategory = extnElem.getAttribute("ExtnPrefCatalog");
			
			
			if(!customerContactId.equals(getWCContext().getCustomerContactId())){
				//Fetch the Additional Email Addresses and PO list from XPX_CUSTOMERCONTACT_EXTN
				Element xpxCustContExtnEle= XPEDXWCUtils.getXPXCustomerContactExtn(wcContext, this.customerContactId);
				if(xpxCustContExtnEle!=null){
					addnlEmailAddrs = xpxCustContExtnEle.getAttribute("AddnlEmailAddrs");
					addnlPOList = xpxCustContExtnEle.getAttribute("POList");
					extnLastLoginDate = xpxCustContExtnEle.getAttribute("LastLoginDate");
				}
			}
			else {  
				/*
				 * Changes for Jira 3382
				 */
				Element xpxCustContExtnEle= XPEDXWCUtils.getXPXCustomerContactExtn(wcContext, this.customerContactId);
				
				if(xpxCustContExtnEle!=null){
					addnlEmailAddrs = xpxCustContExtnEle.getAttribute("AddnlEmailAddrs");
					
				}
				addnlPOList = (String) getWCContext().getWCAttribute("addnlPOList");
				extnLastLoginDate = (String) getWCContext().getWCAttribute("lastLoginDate");
				XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
				xpedxCustomerContactInfoBean.setAddEmailID(addnlEmailAddrs);
				XPEDXWCUtils.setObectInCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean, xpedxCustomerContactInfoBean);
				
		
				/*addnlEmailAddrs = (String) getWCContext().getWCAttribute("addnlEmailAddrs");
				addnlPOList = (String) getWCContext().getWCAttribute("addnlPOList");
				extnLastLoginDate = (String) getWCContext().getWCAttribute("lastLoginDate");
		*/	}
			
			if (!YFCCommon.isVoid(addnlEmailAddrs)){
				String[] emailListSplit = addnlEmailAddrs.split(",");
				for (int i = 0; i < emailListSplit.length; i++) {
					if(emailListSplit[i]!= null && emailListSplit[i].trim().length()>0)
						AddnlEmailAddrList.put(emailListSplit[i], emailListSplit[i]);
				}
			}
			
			if (!YFCCommon.isVoid(addnlPOList)){
				String[] poListSplit = addnlPOList.split(",");
				Arrays.sort(poListSplit, new XPEDXAlphanumericSorting());//Added for JIRA 3645
				for (int i = 0; i < poListSplit.length; i++) {
					if(poListSplit[i]!= null && poListSplit[i].trim().length()>0)
						POList.put(poListSplit[i], poListSplit[i]);
				}
			}
			
			// get single contact
			setContact(getSingleContact(customerContactId));

			setContactStatus(this.contact.getAttribute("Status"));
			this.customer = SCXmlUtils
					.getChildElement(this.contact, "Customer");
			if (log.isDebugEnabled()) {
				log.debug("CustomerObject " + SCXmlUtils.getString(customer));
				log.debug("CustomerContactObject "
						+ SCXmlUtils.getString(this.contact));
			}

			if (log.isDebugEnabled()) {
				log.debug("CustomerObject from UserGenInfo:"
						+ SCXmlUtils.getString(customer));
				log.debug("CustomerContactObject "
						+ SCXmlUtils.getString(this.contact));
			}
			setEffectiveStatus(this.statusList.get(this.contact
					.getAttribute("AggregateStatus")));
			setBuyerOrgCode(this.customer.getAttribute("BuyerOrganizationCode"));
			if (YFCCommon.isVoid(this.buyerOrgCode)) {
				log
						.error("No buyer organization information associated with customer-contact : "
								+ customerContactId);
				return ERROR;
			}
			if (!(YFCCommon.isVoid(buyerOrgCode))) {
				setBuyerOrgName((SCXmlUtils.getChildElement(this.customer,
						"BuyerOrganization")).getAttribute("OrganizationName"));
			}
			// setdefaultaddresslist
			setEffectiveContactDefaultAddress();
			// setotheraddresslist
			setOtherAddress();
			setAddressIDField();
			setUserId(getContact().getAttribute("UserID"));
			if (log.isDebugEnabled()) {
				log.debug("********** UserID=" + getUserId());
			}
			if (YFCCommon.isVoid(getUserId())) {
				// the Contact does not have a user associated
				if (log.isDebugEnabled()) {
					log.debug("Contact does not have a user account");
				}
			} else {
				this.user = SCXmlUtils.getChildElement(getContact(), "User");
				if (log.isDebugEnabled()) {
					log.debug("User details for " + getUserId() + " ="
							+ SCXmlUtils.getString(user));
				}
				if (log.isDebugEnabled()) {
					log.debug("User details for " + getUserId() + " ="
							+ SCXmlUtils.getString(user));
				}
				getUserGroups();
				//Added for JIRA 1998
				setEmailIdInSession();
				//end of JIRA 1998
				setAuthQuestionsForUser();
				setAuthQuestionListForOrg();
				setCountryCodesList();
				setSpendingLimitAndApproversForUser(customerContact);
			}
			// Admin List for current Customer
			if (isAdminListEnabled()) {
				this.adminLoginidList = UserProfileHelper
						.getUserLoginidListWithUserRole(buyerOrgCode,
								"BUYER-ADMIN", wcContext);
				if (adminLoginidList.size() > 0) {
					this.userList = prepareAndInvokeMashup(GET_ADMIN_LIST_MASHUP);
					if (log.isDebugEnabled()) {
						log.debug("UserList=" + SCXmlUtils.getString(userList));
					}
				}
			}
			// Locale List
			this.localeList = YfsUtils.getUserLocaleList(getWCContext());
			if (log.isDebugEnabled()) {
				log.debug("localeList=" + localeList);
			}

			/* STARTS - Customer-User Profile Changes - adsouza */
			setPaper101GradeFlag();
			/* ENDS - Customer-User Profile Changes - adsouza */
			/* Removing This as this is causing Performance Issues while loading the user profile
			if(!"true".equalsIgnoreCase(isUserProfile)) {
				assignedCustomers = XPEDXWCUtils.getAllShipTosAsList(wcContext);
				setAssignedCustomersMap(XPEDXWCUtils.getHashMapFromList(assignedCustomers));
			}
			*/
		} catch (Exception e) {
			log.error(
					"Error in getting Buyer User information for Customer Contact : "
							+ customerContactId+","+ e.getMessage()+e.getCause());
			e.printStackTrace();
		}
		setUserFlags();
		setCustContactAddtnlAddressToDisplay();
		return SUCCESS;
	}
	
	private void setCountryCodesList() {
		setCountriesMap(XPEDXWCUtils.getCountryCodeList());
	}

	public boolean isViewInvoices(){
		return ("Y".equalsIgnoreCase(getViewInvoices()))?true:false;
	}
	
	public Map getUserStatusMap(){
		Map<String,String> statusMap = new TreeMap<String,String>();
		Iterator<String> iterMap = getStatusList().keySet().iterator();
		while(iterMap.hasNext()){
			String key = iterMap.next();
			statusMap.put(key, getStatusList().get(key));
		}
		return statusMap;
	}
	
	public boolean isUserStatusActive(){
		boolean active = false;
		active = ((String)getUserStatusMap().get(getContactStatus())).equalsIgnoreCase("Active");
		return active;
	}

	/**
	 * @param customerContact
	 */
	private void setSpendingLimitAndApproversForUser(NodeList customerContact) {
		/**
		 * 2. get the ApproverProxyUserId = This is the user who can approve/reject hold when the user is unavailable
		 * 3. get the ApproverUserId = This is the user who can approve/reject hold when order is beyond the buyer user's spending limit
		 * 4. ApproverProxyUserId is a customer contact so query CustomerContact to get the UserName {/CustomerContact/User/@Username}
		 * 5. Similarly ApproverUserId is also a customer contact so query CustomerContact to get the UserName {/CustomerContact/User/@Username}
		 * 6. set the field [LIST]variables for Primary and Alternate Approver
		 */
		String spendingLimit = null;
		String spendingCurrency = null;
		String approverProxyUserId = null;
		String approverUserId = null;
		String lastModifiedDate = "";
		String userCreatedDate = "";
		UtilBean utilBean = new UtilBean();
		for(int i=0; i< customerContact.getLength() ; ){
			approverProxyUserId = SCXmlUtil.getAttribute((Element)customerContact.item(i), "ApproverProxyUserId");
			approverUserId = SCXmlUtil.getAttribute((Element)customerContact.item(i), "ApproverUserId");
			spendingLimit = SCXmlUtil.getAttribute((Element)customerContact.item(i), "SpendingLimit");
			spendingCurrency = SCXmlUtil.getAttribute((Element)customerContact.item(i), "SpendingLimitCurrency");
			
			//setLastModifyUserId(SCXmlUtil.getAttribute((Element)customerContact.item(i), "Modifyuserid"));
			
			setPrimaryApprover(approverUserId);
			setAlternateApprover(approverProxyUserId);
			setSpendingLimit((spendingLimit==null)?"": spendingLimit);//default is null

			setSpendingLtCurrency((spendingCurrency==null)?"": spendingCurrency);
			
			addAvailbleCurrencies(spendingCurrency);
			addAvailableApprovers();
			setLastModifiedUser();
			break;
		}
	}

	private void setLastModifiedUser() {
		UtilBean utilBean = new UtilBean();
		Element contactElement = getContact();
		String modifyUserIdBy=contactElement.getAttribute("Modifyuserid");
		String loginID=contactElement.getAttribute("CustomerContactID");
		if(getWCContext().getCustomerContactId().equals(modifyUserIdBy))
		{
			XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
			
			setContactFirstName(xpedxCustomerContactInfoBean.getFirstName());
			setContactLastName(xpedxCustomerContactInfoBean.getLastName());
		}
		else if(loginID!= null && loginID.equals(modifyUserIdBy))
		{
			setContactFirstName(contactElement.getAttribute("FirstName"));
			setContactLastName(contactElement.getAttribute("LastName"));
		}
		else
		{
			ArrayList<Element> customerContact=new ArrayList<Element>();
			customerContact.add(contactElement);
			Map<String,String> modifiedUserMap=XPEDXWCUtils.createModifyUserNameMap(customerContact);
			String modifyFirstLastName=modifiedUserMap.get(modifyUserIdBy);
			if(modifyFirstLastName!=null){
				String name[]=modifyFirstLastName.split(", ");
				if(modifyFirstLastName !=null && modifyFirstLastName.length()>1)
					setContactFirstName(name[1]);
				if(modifyFirstLastName !=null && modifyFirstLastName.length()>1)
					setContactLastName(name[0]);
			}
		}
			
		lastModifiedDate = contactElement.getAttribute("Modifyts");
		setLastModifiedDate(utilBean.formatDate(lastModifiedDate, wcContext, null, DATE_FORMAT_ON_USER_PROFILE));
			
		userCreatedDate = contactElement.getAttribute("Createts");
		setUserCreatedDate(utilBean.formatDate(userCreatedDate, wcContext, null, DATE_FORMAT_ON_USER_PROFILE));
	}

	/**
	 * This method fetches all the available approvers for a selected customer
	 */
	private void addAvailableApprovers() {
		
		Element approverDocInput = SCXmlUtil.createDocument("CustomerContact").getDocumentElement();
		approverDocInput.setAttribute("OrganizationCode", wcContext.getStorefrontId());
		String customerKey = XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext);
		if(!YFCUtils.isVoid(customerKey)){
			SCXmlUtil.createChild(approverDocInput, "Customer");
			SCXmlUtil.getChildElement(approverDocInput, "Customer").setAttribute("CustomerID", customerKey);
		}
		// invoke Mashup
		Object contactListObj = WCMashupHelper.invokeMashup("getXpedxApproversList", approverDocInput, wcContext.getSCUIContext());
		Document contactListDoc = ((Element) contactListObj).getOwnerDocument();
		NodeList custContactNodes = contactListDoc.getElementsByTagName("CustomerContact");
		NodeList userGroupListNodes;
		Element custContactElement;
		Element customerElement;
		Element userElement;
		Element userGroupListElement;
		String buyerOrgCode;
		String displayUserID = null;
		Element userProfileElement = getUser();
		String userProfileBeingModified = userProfileElement.getAttribute("DisplayUserID");
		//Map<String, String> approversMap = new HashMap<String, String>();
		List<String> availableApproversList = new ArrayList<String>();
		for(int i=0; i< custContactNodes.getLength(); i++){
			custContactElement = (Element)custContactNodes.item(i);
			customerElement = SCXmlUtil.getChildElement(custContactElement, "Customer");
			buyerOrgCode = customerElement.getAttribute("BuyerOrganizationCode");
			if(wcContext.getBuyerOrgCode().trim().equalsIgnoreCase(buyerOrgCode==null?buyerOrgCode:buyerOrgCode.trim())){
				userElement =  SCXmlUtil.getChildElement(custContactElement, "User");
				userGroupListNodes = (userElement==null)?null:userElement.getElementsByTagName("UserGroupList");
				for(int j=0; userGroupListNodes!=null && j< userGroupListNodes.getLength(); j++){
					userGroupListElement = (Element)userGroupListNodes.item(j);
					if("BUYER-APPROVER".equalsIgnoreCase(userGroupListElement.getAttribute("UsergroupKey"))){
							displayUserID = userElement.getAttribute("DisplayUserID");
							if(displayUserID!=null && !displayUserID.equalsIgnoreCase(userProfileBeingModified)){// JIRA 1655
								//approversMap.put(displayUserID, displayUserID);
								availableApproversList.add(displayUserID);
							}
					}
				}
			}
		}
		setAvailableApproversList(availableApproversList);
	}

	/**
	 * This method fetches all the available currencies
	 * @param spendingCurrency
	 */
	private void addAvailbleCurrencies(String spendingCurrency) {

		Element currencyInput = SCXmlUtil.createDocument("Currency").getDocumentElement();
		// invoke Mashup
		Object currencyListObj = WCMashupHelper.invokeMashup("getXpedxCurrencyListForUserProfile", currencyInput, wcContext.getSCUIContext());
		Document currencyListDoc = ((Element) currencyListObj).getOwnerDocument();
		
		NodeList currencyNodes = currencyListDoc.getElementsByTagName("Currency");
		Element currencyElement;
		String currency;
		String currencyDesc;
		Map<String, String> currencyMap = new HashMap<String, String>();
		for(int i=0; i< currencyNodes.getLength(); i++){
			currencyElement = (Element)currencyNodes.item(i);
			currency = currencyElement.getAttribute("Currency");
			currencyDesc = currencyElement.getAttribute("CurrencyDescription");
			//Fix for Jira 3048 issue item 4
			if(currency != null && !currency.equalsIgnoreCase("EUR"))
			  currencyMap.put(currency, currencyDesc);
			//End Fix for Jira 3048 issue item 4
		}
		
		setCurrencyMap(currencyMap);
	}

	private void setUserFlags() {
		Element customerContact = SCXmlUtil.getChildElement( customerContactList, "CustomerContact");
		Element Extn = SCXmlUtil.getChildElement(customerContact, "Extn");

		String vPrices = SCXmlUtils.getAttribute(Extn, "ExtnViewPricesFlag");
		String vReports = SCXmlUtils.getAttribute(Extn, "ExtnViewReportsFlag");
		String orderConfEmailFlag = SCXmlUtils.getAttribute(Extn, "ExtnOrderConfEmailFlag");
		String orderCancelEmailFlag = SCXmlUtils.getAttribute(Extn, "ExtnOrderCancelEmailFlag");
		String orderShipEmailFlag = SCXmlUtils.getAttribute(Extn, "ExtnOrderShipEmailFlag");
		String backOrderEmailFlag = SCXmlUtils.getAttribute(Extn, "ExtnBackOrderEmailFlag");

		if ("Y".equals(vPrices))
			viewPrices = true;
		if ("Y".equals(vReports))
			viewReports = true;
		if ("Y".equals(orderConfEmailFlag))
			sendOrderConfEmail = true;
		if ("Y".equals(orderCancelEmailFlag))
			sendOrderCancelEmail = true;
		if ("Y".equals(orderShipEmailFlag))
			sendOrderShipEmail = true;
		if ("Y".equals(backOrderEmailFlag))
			sendBackOrderEmail = true;
		// TODO Auto-generated method stubExtnOrderShipEmailFlag

	}

	/**
	 * Method to set the Secret Question List of the Current Org/Enterprise
	 *
	 * @param
	 * @return
	 */

	private void setAuthQuestionListForOrg() {
		Element questionListForOrgElem = null;
		try {
			questionListForOrgElem = prepareAndInvokeMashup(GET_ORG_QUESTION_LIST_MASHUP);
		} catch (XMLExceptionWrapper e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.questionListForOrg = new LinkedHashMap<String, String>();
		if (null != questionListForOrgElem) {
			ArrayList<Element> nodeQuestionList = SCXmlUtils.getChildren(
					questionListForOrgElem, "AuthQuestion");
			for (Iterator<Element> iter = nodeQuestionList.iterator(); iter
					.hasNext();) {
				Element questionElem = (Element) iter.next();
				String authQuestionKey = questionElem
						.getAttribute("AuthQuestionKey");
				String questionText = questionElem.getAttribute("QuestionText");
				questionText = Util.getInstance().getDBString(
						getWCContext().getSCUIContext().getRequest(),
						questionText);
				this.questionListForOrg.put(authQuestionKey, questionText);
			}
		}

	}

	/**
	 * Method to set the Secret Question of the user
	 *
	 * @param
	 * @return
	 */

	private void setAuthQuestionsForUser() {
		Element authQuestionLiselemForUser = SCXmlUtils.getChildElement(
				this.user, "AuthQuestionList");
		ArrayList<Element> nodeQuestionList = SCXmlUtils.getChildren(
				authQuestionLiselemForUser, "AuthQuestion");
		for (Iterator<Element> iter = nodeQuestionList.iterator(); iter
				.hasNext();) {
			Element oNode = (Element) iter.next();
			String questionTextKey = oNode.getAttribute("AuthQuestionKey");
			this.questionListForUser.add(questionTextKey);
		}
	}

	/**
	 * Method to set the Address IDs
	 *
	 * @param
	 * @return
	 */

	private void setAddressIDField() {
		List<Element> defaultContactAddressList = getDefaultContactAddressList();
		List<Element> defaultCustomerAddressList = getDefaultCustomerAddressList();
		List<Element> otherCustomerAddressList = getOtherContactAddressList();
		List<Element> otherContactAddressList = getOtherCustomerAddressList();
		if (defaultContactAddressList != null) {
			setid(defaultContactAddressList.iterator());
		}
		if (defaultCustomerAddressList != null) {
			setid(defaultCustomerAddressList.iterator());
		}
		if (otherContactAddressList != null) {
			setid(otherContactAddressList.iterator());
		}
		if (otherCustomerAddressList != null) {
			setid(otherCustomerAddressList.iterator());
		}

	}

	/**
	 * Method to set the Address IDs
	 *
	 * @param
	 * @return
	 */

	private void setid(Iterator iter) {
		while (iter.hasNext()) {
			Element address = (Element) iter.next();
			Element personInfo = (Element) address.getFirstChild();
			if(personInfo!=null) {
				String addressId = personInfo.getAttribute("AddressID");
				if ((addressId == null) || (addressId.equals(""))) {
					String city = personInfo.getAttribute("City");
					String state = personInfo.getAttribute("State");
					if (!YFCCommon.isVoid(city) && !YFCCommon.isVoid(state)) {
						addressId = personInfo.getAttribute("City") + "_"
								+ personInfo.getAttribute("State");
					} else {
						if (!YFCCommon.isVoid(city)) {
							addressId = personInfo.getAttribute("City");
						}
						if (!YFCCommon.isVoid(state)) {
							addressId = personInfo.getAttribute("State");
						}
					}
					if (addressId == null || addressId.equals("")) {
						addressId = address.getAttribute(CUST_ADD_ID_ATTR);
					}
					personInfo.setAttribute("AddressID", addressId);
				}
			}
		}
	}

	/**
	 * Method to get element for single Customer Contact
	 *
	 * @param
	 * @return
	 */

	private Element getSingleContact(String contactId) {
		Element contact = null;
		try {
			ArrayList<Element> contacts = SCXmlUtil.getElementsByAttribute(
					customerContactList, "/CustomerContact",
					"CustomerContactID", contactId);
			for(int i=0;i<contacts.size();i++) {
				Element contactElem = contacts.get(i);
				Element customerElem = SCXmlUtil.getChildElement(contactElem, "Customer");
				String customerId = customerElem.getAttribute("CustomerID");
				if(customerId!=null && customerId.equalsIgnoreCase(getCustomerID()))
					contact = contactElem;
			}
		} catch (Exception e) {
			log.error("Error in getting Single contact : ", e.getCause());
		}
		return contact;
	}

	/**
	 * This method adds an address element to the address list, if the element
	 * does not already exist
	 */
	private void addToList(List<Element> list, Element address) {
		Iterator itr = list.iterator();
		boolean flag = true;
		while (itr.hasNext()) {
			Element addr1 = (Element) itr.next();
			if (addr1.getAttribute(CUST_ADD_ID_ATTR).equalsIgnoreCase(
					address.getAttribute(CUST_ADD_ID_ATTR))) {
				flag = false;
				return;
			}
		}
		if (flag) {
			list.add(address);
		}
	}

	/**
	 * This method sets the effective default/other address list
	 *
	 */
	private void setEffectiveContactDefaultAddress() {

		try {
			ArrayList<Element> customerAddressList = SCXmlUtils.getElements(
					getCustomer(), CUST_ADD_ELEM_XPATH);
			ArrayList<Element> contactAddressList = SCXmlUtils.getElements(
					getContact(), CUST_ADD_ELEM_XPATH);
			setDefaultContactAddressList(new ArrayList());
			setDefaultCustomerAddressList(new ArrayList());

			Iterator itr = contactAddressList.iterator();
			boolean isDefaultBillTo = false;
			boolean isDefaultShipTo = false;
			boolean isDefaultSoldTo = false;
			while (itr.hasNext()) {
				Element contactAddress = (Element) itr.next();
				if ((!isDefaultBillTo)
						&& ("Y".equalsIgnoreCase(contactAddress
								.getAttribute("IsDefaultBillTo")))) {
					isDefaultBillTo = true;
					addToList(getDefaultContactAddressList(), contactAddress);
					setDefBillToAddId(contactAddress
							.getAttribute(CUST_ADD_ID_ATTR));
				}
				if ((!isDefaultShipTo)
						&& ("Y".equalsIgnoreCase(contactAddress
								.getAttribute("IsDefaultShipTo")))) {
					isDefaultShipTo = true;
					addToList(getDefaultContactAddressList(), contactAddress);
					setDefShipToAddId(contactAddress
							.getAttribute(CUST_ADD_ID_ATTR));
				}

				if ((!isDefaultSoldTo)
						&& ("Y".equalsIgnoreCase(contactAddress
								.getAttribute("IsDefaultSoldTo")))) {
					isDefaultSoldTo = true;
					addToList(getDefaultContactAddressList(), contactAddress);
					setDefSoldToAddId(contactAddress
							.getAttribute(CUST_ADD_ID_ATTR));
				}
				if (isDefaultBillTo && isDefaultShipTo && isDefaultSoldTo) {
					defaultAddressList.addAll(defaultContactAddressList);
					defaultAddressList.addAll(defaultCustomerAddressList);
					return;
				}

			}
			Iterator itr1 = customerAddressList.iterator();
			while (itr1.hasNext()) {
				Element customerAddress = (Element) itr1.next();
				if ((!isDefaultBillTo)
						&& ("Y".equalsIgnoreCase(customerAddress
								.getAttribute("IsDefaultBillTo")))) {
					isDefaultBillTo = true;
					addToList(getDefaultCustomerAddressList(), customerAddress);
				}
				if ((!isDefaultShipTo)
						&& ("Y".equalsIgnoreCase(customerAddress
								.getAttribute("IsDefaultShipTo")))) {
					isDefaultShipTo = true;
					addToList(getDefaultCustomerAddressList(), customerAddress);
				}

				if ((!isDefaultSoldTo)
						&& ("Y".equalsIgnoreCase(customerAddress
								.getAttribute("IsDefaultSoldTo")))) {
					isDefaultSoldTo = true;
					addToList(getDefaultCustomerAddressList(), customerAddress);
				}
				if (isDefaultBillTo && isDefaultShipTo && isDefaultSoldTo) {
					defaultAddressList.addAll(defaultContactAddressList);
					defaultAddressList.addAll(defaultCustomerAddressList);
					return;
				}

			}

		} catch (Exception e) {
			log.error("Error in setting address List : ", e.getCause());
		}
		defaultAddressList.addAll(defaultContactAddressList);
		defaultAddressList.addAll(defaultCustomerAddressList);
	}

	/**
	 * Get other address list
	 */
	private void setOtherAddress() {
		try {
			ArrayList<Element> customerAddressList = SCXmlUtils.getElements(
					getCustomer(), CUST_ADD_ELEM_XPATH);
			ArrayList<Element> contactAddressList = SCXmlUtils.getElements(
					getContact(), CUST_ADD_ELEM_XPATH);

			setOtherCustomerAddressList(new ArrayList());
			setOtherContactAddressList(new ArrayList());
			Iterator itr = contactAddressList.iterator();
			while (itr.hasNext()) {
				Element contactAddress = (Element) itr.next();

				if (!(("Y".equalsIgnoreCase(contactAddress
						.getAttribute("IsDefaultBillTo")))
						|| ("Y".equalsIgnoreCase(contactAddress
								.getAttribute("IsDefaultShipTo"))) || ("Y"
						.equalsIgnoreCase(contactAddress
								.getAttribute("IsDefaultSoldTo"))))) {
					getOtherContactAddressList().add(contactAddress);
				}
			}
			Iterator itr1 = customerAddressList.iterator();
			while (itr1.hasNext()) {
				Element customerAddress = (Element) itr1.next();
				boolean flag = true;
				List<Element> defaultCustomerList = getDefaultCustomerAddressList();
				Iterator itr2 = defaultCustomerList.iterator();
				while (itr2.hasNext()) {
					Element defaultCustomer = (Element) itr2.next();
					if ((customerAddress.getAttribute(CUST_ADD_ID_ATTR))
							.equalsIgnoreCase(defaultCustomer
									.getAttribute(CUST_ADD_ID_ATTR))) {
						flag = false;
						break;
					}
				}
				if (flag) {
					getOtherCustomerAddressList().add(customerAddress);
				}
			}

		} catch (Exception e) {
			log.error("Error in getting address list : ", e.getCause());
		}
		otherAddressList.addAll(otherContactAddressList);
		otherAddressList.addAll(otherCustomerAddressList);
	}

	/**
	 * Get User groups' List, Current User groups existing - BUYER-ADMIN, BUYER-USER, BUYER-APPROVER. BUYER-ESTIMATOR needs to be addded for the
	 * User Type checkbox displayed in XPEDXUserProfile.jsp
	 */

	public List<String> getUserGroups() {
		if (userRoles != null && userRoles.size() > 0) {
			return userRoles;
		}
		userRoles = new ArrayList<String>();

		if (user == null) {
			return userRoles;
		}

		List<Element> userGroupList = null;
		userGroupList = SCXmlUtils.getElements(user,
				"/UserGroupLists/UserGroupList");

		if (userGroupList != null) {
			for (Element userGroup : userGroupList) {
				String userGroupKey = userGroup.getAttribute("UsergroupKey");
				userRoles.add(userGroupKey);
			}
		}
		return userRoles;
	}

//Added for JIRA 1998 - 	
	public void setEmailIdInSession() {
		String EmailId = "";

		ArrayList <Element> contactPersonInfoList = null;
		contactPersonInfoList = SCXmlUtils.getElements(user,
				"/ContactPersonInfo");

		if (contactPersonInfoList != null) {
			for (Element contactPersonInfo : contactPersonInfoList) {
				EmailId = contactPersonInfo.getAttribute("EMailID");
			}
		}
		if (!YFCCommon.isVoid(EmailId)){
			wcContext.getSCUIContext().getSession().setAttribute("emailId", EmailId);
		}
	}
	

	/**
	 * Returns the Customer member variable
	 *
	 * @return customer element
	 */
	public Element getCustomer() {
		return customer;
	}

	/**
	 * Sets the customer element
	 *
	 * @param contact
	 */
	public void setCustomer(Element contact) {
		this.customer = contact;
	}

	/**
	 * @return the user element
	 */
	public Element getUser() {
		return user;
	}

	/**
	 * Sets the user element
	 *
	 * @param user
	 */
	public void setUser(Element user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * Sets the user password to the member variable
	 *
	 * @param userPassword
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * @return the confirm password variable
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * Sets the Confirm Password field to the member variable
	 *
	 * @param confirmPassword
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	/**
	 *
	 * @return the locale member variable
	 */
	public Element getLoc() {
		return this.loc;
	}

	/**
	 * Sets the locale value to the member variable
	 *
	 * @param locale
	 */
	public void setLoc(Element locale) {
		this.loc = locale;
	}

	/**
	 * @return the UserID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the UserID
	 *
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the Organization Code
	 */
	public String getOrganizationCode() {
		return organizationCode;
	}

	public String getCustomerID() {
		String customerInSession = XPEDXWCUtils
				.getLoggedInCustomerFromSession(getWCContext());
		if (customerInSession != null && customerInSession.trim().length() > 0) {
			return customerInSession;
		} else if (customerID == null) {
			return getWCContext().getCustomerId();
		}
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	/**
	 * Sets the Organization code
	 *
	 * @param organizationCode
	 */
	public void setOrganizationCode(String orgCode) {
		organizationCode = orgCode;
	}

	/**
	 * @return the Customer Admin List Element
	 */
	public Element getCustomerAdmin() {
		return customerAdmin;
	}

	/**
	 * Sets the Customer Admin Parameter
	 *
	 * @param customerAdmin
	 */
	public void setCustomerAdmin(Element customerAdmin) {
		this.customerAdmin = customerAdmin;
	}

	/**
	 * @return the userList
	 */
	public Element getUserList() {
		return userList;
	}

	/**
	 * @param userList
	 *            the userList to set
	 */
	public void setUserList(Element userList) {
		this.userList = userList;
	}

	/**
	 * @return the contact
	 */
	public Element getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(Element contact) {
		this.contact = contact;
	}

	/**
	 * @return the defaultContactAddressList
	 */
	public List<Element> getDefaultContactAddressList() {
		return defaultContactAddressList;
	}

	/**
	 * @param defaultContactAddressList
	 *            the defaultContactAddressList to set
	 */
	public void setDefaultContactAddressList(
			List<Element> defaultContactAddressList) {
		this.defaultContactAddressList = defaultContactAddressList;
	}

	/**
	 * @return the defaultCustomerAddressList
	 */
	public List<Element> getDefaultCustomerAddressList() {
		return defaultCustomerAddressList;
	}

	/**
	 * @param defaultCustomerAddressList
	 *            the defaultCustomerAddressList to set
	 */
	public void setDefaultCustomerAddressList(
			List<Element> defaultCustomerAddressList) {
		this.defaultCustomerAddressList = defaultCustomerAddressList;
	}

	/**
	 * @return the otherCustomerAddressList
	 */
	public List<Element> getOtherCustomerAddressList() {
		return otherCustomerAddressList;
	}

	/**
	 * @param otherCustomerAddressList
	 *            the otherCustomerAddressList to set
	 */
	public void setOtherCustomerAddressList(
			List<Element> otherCustomerAddressList) {
		this.otherCustomerAddressList = otherCustomerAddressList;
	}

	/**
	 * @return the otherContactAddressList
	 */
	public List<Element> getOtherContactAddressList() {
		return otherContactAddressList;
	}

	/**
	 * @param otherContactAddressList
	 *            the otherContactAddressList to set
	 */
	public void setOtherContactAddressList(List<Element> otherContactAddressList) {
		this.otherContactAddressList = otherContactAddressList;
	}

	/**
	 * @return the statusList
	 */
	public Map<String, String> getStatusList() {
		return statusList;
	}

	/**
	 * @param statusList
	 *            the statusList to set
	 */
	public void setStatusList(Map<String, String> statusList) {
		this.statusList = statusList;
	}

	/**
	 * @return the title
	 */
	public Map<String, String> getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(Map<String, String> title) {
		this.title = title;
	}

	/**
	 * @return the localeList
	 */
	public Map<String, String> getLocaleList() {
		return localeList;
	}

	/**
	 * @param localeList
	 *            the localeList to set
	 */
	public void setLocaleList(Map<String, String> localeList) {
		this.localeList = localeList;
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
	 * @return the buyerOrgCode
	 */
	public String getBuyerOrgCode() {
		return buyerOrgCode;
	}

	/**
	 * @return the Secret question for the user
	 */
	public String getSecretQuestionForUser() {
		if (!(this.questionListForUser.isEmpty())) {
			return this.questionListForUser.get(0);
		} else {
			return "";
		}
	}

	/**
	 * @param buyerOrgCode
	 *            the buyerOrgCode to set
	 */
	public void setBuyerOrgCode(String buyerOrgCode) {
		this.buyerOrgCode = buyerOrgCode;
	}

	/**
	 * @return true: if seeing own user profile
	 */
	public boolean isSelfAdmin() {
		String loggedInCustomerContactId = getWCContext()
				.getCustomerContactId();
		if (customerContactId.compareTo(loggedInCustomerContactId) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return true: if Logged-in user is a BUYER-ADMIN
	 */
	public boolean isAdminListEnabled() {
		try {
			if (ResourceAccessAuthorizer.getInstance().isAuthorized(
					USER_LIST_RESOURCE_ID, getWCContext())) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			log.warn("Error checking user list authorization:" + e);
			return true;
		}
	}

	/**
	 * @return boolean
	 */

	public boolean isInUserGroup(String groupKey) {
		List<String> userGroups = getUserGroups();
		boolean userInGroup = false;

		if (userGroups != null) {
			for (String userGroup : userGroups) {
				if ((userGroup != null) && userGroup.equals(groupKey)) {
					userInGroup = true;
					break;
				}
			}
		}
		return userInGroup;
	}

	/**
	 * @return adminLoginidList
	 */
	public List<String> getAdminLoginidList() {
		return adminLoginidList;
	}

	/**
	 * @return questionListForOrg
	 */
	public Map<String, String> getQuestionListForOrg() {
		return questionListForOrg;
	}

	/**
	 * @return constant String
	 */
	public String getMaskedPasswordString() {
		return WCConstants.MASKED_PASSWORD_STRING;
	}

	/**
	 * @return constant String
	 */
	public String getMaskedSecretAnswerString() {
		if (YFCCommon.isVoid(getSecretQuestionForUser())) {
			return "";
		} else {
			return WCConstants.MASKED_PASSWORD_STRING;
		}
	}

	/**
	 * @return the defShipToAddId
	 */
	public String getDefShipToAddId() {
		return defShipToAddId;
	}

	/**
	 * @param defShipToAddId
	 *            the defShipToAddId to set
	 */
	public void setDefShipToAddId(String defShipToAddId) {
		this.defShipToAddId = defShipToAddId;
	}

	/**
	 * @return the defBillToAddId
	 */
	public String getDefBillToAddId() {
		return defBillToAddId;
	}

	/**
	 * @param defBillToAddId
	 *            the defBillToAddId to set
	 */
	public void setDefBillToAddId(String defBillToAddId) {
		this.defBillToAddId = defBillToAddId;
	}

	/**
	 * @return the defSoldToAddId
	 */
	public String getDefSoldToAddId() {
		return defSoldToAddId;
	}

	/**
	 * @param defSoldToAddId
	 *            the defSoldToAddId to set
	 */
	public void setDefSoldToAddId(String defSoldToAddId) {
		this.defSoldToAddId = defSoldToAddId;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the buyerOrgName
	 */
	public String getBuyerOrgName() {
		return buyerOrgName;
	}

	/**
	 * @param buyerOrgName
	 *            the buyerOrgName to set
	 */
	public void setBuyerOrgName(String buyerOrgName) {
		this.buyerOrgName = buyerOrgName;
	}

	/**
	 * @return the contactStatus
	 */

	public String getContactStatus() {
		return contactStatus;
	}

	/**
	 * @param contactStatus
	 *            the contactStatus to set
	 */
	public void setContactStatus(String contactStatus) {
		this.contactStatus = contactStatus;
	}

	/**
	 * @return the customerStatus
	 */

	public String getCustomerStatus() {
		return customerStatus;
	}

	/**
	 * @param contactStatus
	 *            the contactStatus to set
	 */
	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}

	public String getIsChildCustomer() {
		return isChildCustomer;
	}

	public void setIsChildCustomer(String isChildCustomer) {
		this.isChildCustomer = isChildCustomer;
	}

	public List<Element> getDefaultAddressList() {
		return defaultAddressList;
	}

	public void setDefaultAddressList(List<Element> defaultAddressList) {
		this.defaultAddressList = defaultAddressList;
	}

	public List<Element> getOtherAddressList() {
		return otherAddressList;
	}

	public void setOtherAddressList(List<Element> otherAddressList) {
		this.otherAddressList = otherAddressList;
	}

	public String getEffectiveStatus() {
		return effectiveStatus;
	}

	public void setEffectiveStatus(String effectiveStatus) {
		this.effectiveStatus = effectiveStatus;
	}

	public boolean getExpandAddressPanel() {
		return expandAddressPanel;
	}

	public void setExpandAddressPanel(boolean expandAddressPanel) {
		this.expandAddressPanel = expandAddressPanel;
	}

	public boolean isExpandPhonePanel() {
		return expandPhonePanel;
	}

	public void setExpandPhonePanel(boolean expandPhonePanel) {
		this.expandPhonePanel = expandPhonePanel;
	}

	public String getViewInvoices() {
		return viewInvoices;
	}

	public void setViewInvoices(String viewInvoices) {
		this.viewInvoices = viewInvoices;
	}

	public String getEstimator() {
		return estimator;
	}

	public void setEstimator(String estimator) {
		this.estimator = estimator;
	}

	public String getStockCheckWebservice() {
		return stockCheckWebservice;
	}

	public void setStockCheckWebservice(String stockCheckWebservice) {
		this.stockCheckWebservice = stockCheckWebservice;
	}

	public String getPunchoutUsers() {
		return punchoutUsers;
	}

	public void setPunchoutUsers(String punchoutUsers) {
		this.punchoutUsers = punchoutUsers;
	}

	/* STARTS - Customer-User Profile Changes - adsouza */

	/*public Map getAddnlEmailAddrList() {

		Element ccElem = XMLUtilities.getChildElementByName(
				customerContactList, "CustomerContact");
		Element extnElem = XMLUtilities.getChildElementByName(ccElem, "Extn");
		String emailList = SCXmlUtils.getAttribute(extnElem,
				"ExtnAddnlEmailAddrs");

		String[] emailListSplit = emailList.split(",");
		for (int i = 0; i < emailListSplit.length; i++) {
			if(emailListSplit[i]!= null && emailListSplit[i].trim().length()>0)
				AddnlEmailAddrList.put(emailListSplit[i], emailListSplit[i]);
		}
		
		return AddnlEmailAddrList;
	}

	public Map getPOList() {

		Element ccElem = XMLUtilities.getChildElementByName(
				customerContactList, "CustomerContact");
		Element extnElem = XMLUtilities.getChildElementByName(ccElem, "Extn");
		String poList = SCXmlUtils.getAttribute(extnElem, "ExtnPOList");

		String[] poListSplit = poList.split(",");
		for (int i = 0; i < poListSplit.length; i++) {
			if(poListSplit[i]!= null && poListSplit[i].trim().length()>0)
				POList.put(poListSplit[i], poListSplit[i]);
		}

		return POList;
	}*/
	
	
	
	public Map getEmailFormatOptions() {
		Map EmailFormatOptions = new LinkedHashMap();
		EmailFormatOptions.put("Text", "Text");
		EmailFormatOptions.put("HTML", "HTML");

		return EmailFormatOptions;
	}
	
	public Map getAddnlEmailAddrList() {
		return AddnlEmailAddrList;
	}

	public void setAddnlEmailAddrList(Map addnlEmailAddrList) {
		AddnlEmailAddrList = addnlEmailAddrList;
	}

	public Map getPOList() {
		return POList;
	}

	public void setPOList(Map pOList) {
		POList = pOList;
	}

	/**
	 * @param assignedCustomers the assignedCustomers to set
	 */
	public void setAssignedCustomers(List<String> assignedCustomers) {
		this.assignedCustomers = assignedCustomers;
	}

	public List<String> getAssignedCustomers() {
		if(assignedCustomers == null)
			assignedCustomers = new ArrayList<String>();
		return assignedCustomers;
	}
	/**
	 * @param assignedCustomersMap the assignedCustomersMap to set
	 */
	public void setAssignedCustomersMap(HashMap<String, String> assignedCustomersMap) {
		this.assignedCustomersMap = assignedCustomersMap;
	}

	public HashMap<String,String> getAssignedCustomersMap() {
		if (assignedCustomersMap == null){
			assignedCustomersMap = new HashMap<String, String>();
		}
		return assignedCustomersMap;
	}

	public Map getMainCategories() throws Exception {
		Map MainCategories = new LinkedHashMap();
		//MainCategories.put("None", "None"); removed as part of JIRA #1459

		Element oXML = null;

		Map<String, String> valueMap = new HashMap<String, String>();
		String orgCode = wcContext.getStorefrontId();
		String customerId = wcContext.getCustomerId();
		valueMap.put("/SearchCatalogIndex/@CallingOrganizationCode", orgCode);
		valueMap.put("/SearchCatalogIndex/Item/CustomerInformation/@CustomerID", customerId);

		Element input = WCMashupHelper.getMashupInput(
				"xpedx-PreferredCatalogOptions", valueMap, wcContext
						.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		if(LOG.isDebugEnabled()){
		LOG.debug("Input XML: " + inputXml);
		}
		Object obj = WCMashupHelper.invokeMashup(
				"xpedx-PreferredCatalogOptions", input, wcContext
						.getSCUIContext());
		if(obj != null){
			oXML = ((Element) obj).getOwnerDocument().getDocumentElement();
			if (null != oXML) {
				if(LOG.isDebugEnabled()){
				LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));			
				}
			Element cList = XMLUtilities
				.getChildElementByName(oXML, "CategoryList");
			if (cList == null) {
				return MainCategories;
			}
			NodeList cat = cList.getElementsByTagName("Category");
			int numCats = cat.getLength();
			for (int i = 0; i < numCats; i++) {
				Element catElem = (Element) cat.item(i);
				String catID = catElem.getAttribute("CategoryID");
				String sDesc = catElem.getAttribute("ShortDescription");
				MainCategories.put(catID, sDesc);
			}
		 }
		}

		return MainCategories;
	}

	public void setPaper101GradeFlag() {
		Element ccElem = XMLUtilities.getChildElementByName(
				customerContactList, "CustomerContact");
		Element extnElem = XMLUtilities.getChildElementByName(ccElem, "Extn");
		String p101g = SCXmlUtils.getAttribute(extnElem, "ExtnPaper101Grade");
		if ("Y".equals(p101g))
			paper101grade = true;
	}

	public boolean isPaper101Grade() {
		return paper101grade;
	}

	public void setB2BCatalogViewFlag()
    {
        Element ccElem = XMLUtilities.getChildElementByName(customerContactList, "CustomerContact");
        Element extnElem = XMLUtilities.getChildElementByName(ccElem, "Extn");
        String b2bCV = SCXmlUtils.getAttribute(extnElem, "ExtnB2BCatalogView");
        if("Y".equals(b2bCV))
        {
            b2bCatalogView = true;
        }
    }

    public boolean isB2BCatalogView()
    {
        return b2bCatalogView;
    }
    public boolean isViewPrices() {
		return viewPrices;
	}

	public boolean isViewReports() {
		return viewReports;
	}

	public boolean isSendOrderConfEmail() {
		return sendOrderConfEmail;
	}

	public boolean isSendOrderCancelEmail() {
		return sendOrderCancelEmail;
	}

	public boolean isSendOrderShipEmail() {
		return sendOrderShipEmail;
	}

	public boolean isSendBackOrderEmail() {
		return sendBackOrderEmail;
	}


	/* ENDS - Customer-User Profile Changes - adsouza */

	/* STARTS - Customer-User Profile Changes - adsouza */

	protected boolean paper101grade = false;
	protected boolean b2bCatalogView = false;
	protected boolean viewPrices = false;
	protected boolean viewReports = false;
	protected boolean sendOrderConfEmail = false;
	protected boolean sendOrderCancelEmail = false;
	protected boolean sendOrderShipEmail = false;
	protected boolean sendBackOrderEmail = false;

	/* ENDS - Customer-User Profile Changes - adsouza */

	private String estimator = "F";
	private String stockCheckWebservice = "F";
	private String punchoutUsers = "F";
	private String viewInvoices = "N";

	private Map b2bCatalogViewMap;
	private String defaultB2bCatalogView;

	public String getDefaultB2bCatalogView() {
        Element ccElem = XMLUtilities.getChildElementByName(customerContactList, "CustomerContact");
        Element extnElem = XMLUtilities.getChildElementByName(ccElem, "Extn");
        String defaultB2bCatalogView = SCXmlUtils.getAttribute(extnElem, "ExtnB2BCatalogView");
        if (YFCCommon.isVoid(defaultB2bCatalogView.trim())){
        	defaultB2bCatalogView = ""+XPEDXConstants.XPEDX_B2B_PAPER_GRID_VIEW;
        }
		return defaultB2bCatalogView;
	}

	public void setDefaultB2bCatalogView(String defaultB2bCatalogView) {
		this.defaultB2bCatalogView = defaultB2bCatalogView;
	}

	public void setB2bCatalogViewMap(Map b2bCatalogViewMap) {
		this.b2bCatalogViewMap = b2bCatalogViewMap;
	}

	public Map getB2bCatalogViewMap() {
		b2bCatalogViewMap = new HashMap();
		b2bCatalogViewMap.put(new Integer(XPEDXConstants.XPEDX_B2B_FULL_VIEW),"Full View");
		b2bCatalogViewMap.put(new Integer(XPEDXConstants.XPEDX_B2B_CONDENCED_VIEW),"Condensed View");
		b2bCatalogViewMap.put(new Integer(XPEDXConstants.XPEDX_B2B_MINI_VIEW),"Mini View");
		b2bCatalogViewMap.put(new Integer(XPEDXConstants.XPEDX_B2B_PAPER_GRID_VIEW),"Paper Grid View");

		return b2bCatalogViewMap;
	}

	private void setCustContactAddtnlAddressToDisplay(){
		Element custContact = getContact();
		Element custContactAddtnAddressList = SCXmlUtil.getChildElement(custContact, "CustomerAdditionalAddressList");
		custContactAddtnlAddress = SCXmlUtil.getFirstChildElement(custContactAddtnAddressList);
		SCXmlUtil.getString(custContactAddtnlAddress);
	}

	public Element getCustContactAddtnlAddress() {
		return custContactAddtnlAddress;
	}

	public void setCustContactAddtnlAddress(Element custContactAddtnlAddress) {
		this.custContactAddtnlAddress = custContactAddtnlAddress;
	}

	public String getPrefCategory() {
		return prefCategory;
	}

	public void setPrefCategory(String prefCategory) {
		this.prefCategory = prefCategory;
	}
	
	public String getExtnLastLoginDate() {
		if((extnLastLoginDate !=null) && (extnLastLoginDate.isEmpty()==false)){
			extnLastLoginDate = getUtilBean().formatDate(extnLastLoginDate, getWCContext(), null,DATE_FORMAT_ON_USER_PROFILE);			
		}
		return extnLastLoginDate;
	}

	public void setExtnLastLoginDate(String extnLastLoginDate) {
		this.extnLastLoginDate = extnLastLoginDate;
	}

	public boolean isAdmin(){
		List<String> userGroups = getUserGroups();
		boolean isAdmin = false;
		for(int i=0; i<userGroups.size();i++)
		if(userGroups.get(i).equalsIgnoreCase("admin"))
		{
			isAdmin = true;
		}
		return isAdmin;
	}

	/**
	 * @return the primaryApprover
	 */
	public String getPrimaryApprover() {
		return primaryApprover;
	}

	/**
	 * @param primaryApprover the primaryApprover to set
	 */
	public void setPrimaryApprover(String primaryApprover) {
		this.primaryApprover = primaryApprover;
	}

	/**
	 * @return the alternateApprover
	 */
	public String getAlternateApprover() {
		return alternateApprover;
	}

	/**
	 * @param alternateApprover the alternateApprover to set
	 */
	public void setAlternateApprover(String alternateApprover) {
		this.alternateApprover = alternateApprover;
	}
	
	/**
	 * @return the availableApproversList
	 */
	public List<String> getAvailableApproversList() {
		return availableApproversList;
	}

	/**
	 * @param availableApproversList the availableApproversList to set
	 */
	public void setAvailableApproversList(List<String> availableApproversList) {
		this.availableApproversList = availableApproversList;
	}


	protected Map<String, String> currencyMap = new HashMap<String, String>();
	
	/**
	 * @return the currencyMap
	 */
	public Map<String, String> getCurrencyMap() {
		return currencyMap;
	}
	
	/**
	 * @param currencyMap the currencyMap to set
	 */
	public void setCurrencyMap(Map<String, String> currencyMap) {
		this.currencyMap = currencyMap;
	}
	/**
	 * @return the spendingLtCurrency
	 */
	public String getSpendingLtCurrency() {
		return spendingLtCurrency;
	}
	
	/**
	 * @param spendingLtCurrency the spendingLtCurrency to set
	 */
	public void setSpendingLtCurrency(String spendingLtCurrency) {
		this.spendingLtCurrency = spendingLtCurrency;
	}

	/**
	 * @return the spendingLimit
	 */
	public String getSpendingLimit() {
		return spendingLimit;		
	}

	/**
	 * @param spendingLimit the spendingLimit to set
	 */
	public void setSpendingLimit(String spendingLimit) {
		this.spendingLimit = spendingLimit;
	}

	/**
	 * @return the lastModifyUserId
	 */
	public String getLastModifyUserId() {
		return lastModifyUserId;
	}

	/**
	 * @param lastModifyUserId the lastModifyUserId to set
	 */
	public void setLastModifyUserId(String lastModifyUserId) {
		this.lastModifyUserId = lastModifyUserId;
	}
	
	/**
	 * @return the countriesMap
	 */
	public Map<String, String> getCountriesMap() {
		return countriesMap;
	}

	/**
	 * @param countriesMap the countriesMap to set
	 */
	public void setCountriesMap(Map<String, String> countriesMap) {
		this.countriesMap = countriesMap;
	}

	/**
	 * @return the contactFirstName
	 */
	public String getContactFirstName() {
		return contactFirstName;
	}

	/**
	 * @param contactFirstName the contactFirstName to set
	 */
	public void setContactFirstName(String contactFirstName) {
		this.contactFirstName = contactFirstName;
	}

	/**
	 * @return the contactLastName
	 */
	public String getContactLastName() {
		return contactLastName;
	}

	/**
	 * @param contactLastName the contactLastName to set
	 */
	public void setContactLastName(String contactLastName) {
		this.contactLastName = contactLastName;
	}

	/**
	 * @return the lastModifiedDate
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * @return the userCreatedDate
	 */
	public String getUserCreatedDate() {
		return userCreatedDate;
	}
	
	/**
	 * @param userCreatedDate the userCreatedDate to set
	 */
	public void setUserCreatedDate(String userCreatedDate) {
		this.userCreatedDate = userCreatedDate;
	}
		
	public QuickLinkBean[] getQuickLinkBeanArray() {
		return quickLinkBeanArray;
	}

	public void setQuickLinkBeanArray(QuickLinkBean[] quickLinkBeanArray) {
		this.quickLinkBeanArray = quickLinkBeanArray;
	}

	/**
	 * @param currentSelTab the currentSelTab to set
	 */
	public void setCurrentSelTab(String currentSelTab) {
		this.currentSelTab = currentSelTab;
	}

	/**
	 * @return the currentSelTab
	 */
	public String getCurrentSelTab() {
		return currentSelTab;
	}

	public String getIsUserProfile() {
		return isUserProfile;
	}

	public void setIsUserProfile(String isUserProfile) {
		this.isUserProfile = isUserProfile;
	}

	public String getAddnlEmailAddrs() {
		return addnlEmailAddrs;
	}

	public void setAddnlEmailAddrs(String addnlEmailAddrs) {
		this.addnlEmailAddrs = addnlEmailAddrs;
	}

	public String getAddnlPOList() {
		return addnlPOList;
	}

	public void setAddnlPOList(String addnlPOList) {
		this.addnlPOList = addnlPOList;
	}	
	
	public String showSpendingLimit() {

		double spendingLimitValue = Double.parseDouble(spendingLimit);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		showspendingLimit = nf.format(spendingLimitValue);
		return showspendingLimit;
	}
	
	public String getUnformattedSpendingLimit() {
		
		double spendingLimitValue = Double.parseDouble(spendingLimit);		
		NumberFormat formatter = new DecimalFormat("#########");
		unformattedSpendingLimit = formatter.format(spendingLimitValue);
		return unformattedSpendingLimit;
	}
	
}

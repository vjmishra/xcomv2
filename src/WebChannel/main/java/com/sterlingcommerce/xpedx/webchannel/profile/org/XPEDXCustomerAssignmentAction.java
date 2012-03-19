package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPEDXCustomerAssignmentAction extends WCMashupAction {

	private static final long serialVersionUID = 4203292437195126816L;
	private List<String> customers1 = new ArrayList<String>();
	private List<String> customers2 = new ArrayList<String>();
	private String customerContactId = "";
	private String customerId = "";
	private String selectedCurrentCustomer = "";
	private String currentCustomer = "";
	private XPEDXShipToCustomer defualtShipToAddress;
	private List<XPEDXShipToCustomer> assignedShipToList;
//	Performance Fix - Removal of the mashup call of - XPEDXGetPaginatedCustomerAssignments
	private Integer assignedShipToCount = 0;
	private String comingFromSearch = "false";
	private XPEDXOverriddenShipToAddress xOverriddenShipToAddress;
	private Set<XPEDXShipToCustomer> addressSearchResult;
	private String defaultShipToCustomerId;
	private String searchTerm = "Search Criteria";
	private boolean search = false;
	private boolean setSelectedAsDefault;
	private String orderByDesc = "N";
	private String orderByAttribute = "ShipToCustomerID";
	protected Integer pageNumber = 1;
	//Updated from 25 to 6 for JIRA 2875
	protected Integer pageSize = 6;
    private Boolean isFirstPage = Boolean.FALSE;
    private Boolean isLastPage = Boolean.FALSE;
    private Boolean isValidPage = Boolean.FALSE;
    private Integer totalNumberOfPages = new Integer(1);
    private String divId = "ajax-assignedShipToCustomers" ;
    private List<String> alreadySelectedCustomers;
    
    public boolean shipToResult=true;
    private String pageSetToken;	
	
	public String getPageSetToken() {
		return pageSetToken;
	}

	public void setPageSetToken(String pageSetToken) {
		this.pageSetToken = pageSetToken;
	}
	
	public boolean isShipToResult() {
		return shipToResult;
	}

	public void setShipToResult(boolean shipToResult) {
		this.shipToResult = shipToResult;
	}

	public boolean isSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}

	public XPEDXOverriddenShipToAddress getxOverriddenShipToAddress() {
		return xOverriddenShipToAddress;
	}

	public void setxOverriddenShipToAddress(
			XPEDXOverriddenShipToAddress xOverriddenShipToAddress) {
		this.xOverriddenShipToAddress = xOverriddenShipToAddress;
	}

	public List<XPEDXShipToCustomer> getAssignedShipToList() {
		return assignedShipToList;
	}

	public void setAssignedShipToList(
			List<XPEDXShipToCustomer> assignedShipToFirstHalf) {
		this.assignedShipToList = assignedShipToFirstHalf;
	}

	public XPEDXShipToCustomer getDefualtShipToAssigned() {
		if (defualtShipToAddress == null) {
			defualtShipToAddress = new XPEDXShipToCustomer();
		}
		return defualtShipToAddress;
	}

	public void setDefualtShipToAssigned(
			XPEDXShipToCustomer defualtShipToAssigned) {
		this.defualtShipToAddress = defualtShipToAssigned;
	}

	public Set<XPEDXShipToCustomer> getAddressSearchResult() {
		return addressSearchResult;
	}

	public void setAddressSearchResult(Set<XPEDXShipToCustomer> addressSearchResult) {
		this.addressSearchResult = addressSearchResult;
	}

	public String getSearchTerm() {
		return searchTerm.toUpperCase();
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	private static final Logger log = Logger
			.getLogger(XPEDXCustomerAssignmentAction.class);

	private int listSize;

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

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public List<String> getCustomers1() {
		return customers1;
	}

	public List<String> getCustomers2() {
		return customers2;
	}

	public void setCustomers2(List<String> customers2) {
		this.customers2 = customers2;
	}

	public void setCustomers1(List<String> customers1) {
		this.customers1 = customers1;
	}

	public String getSelectedCurrentCustomer() {
		return selectedCurrentCustomer;
	}

	public void setSelectedCurrentCustomer(String selectedCurrentCustomer) {
		this.selectedCurrentCustomer = selectedCurrentCustomer;
	}

	/**
	 * <OrganizationList><Organization CustomerID="BC2"
	 * OrganizationCode="BC2__xpedx"/><Organization CustomerID="BC3"
	 * OrganizationCode="BC3__xpedx"/></OrganizationList>
	 **/
	public String getCustomersInHierarchy() throws XMLExceptionWrapper,
			CannotBuildInputException {
		Element outElem = null;
		Document document = null;
		if (XPEDXWCUtils.isCustomerSelectedIntoConext(getWCContext())) {
			document = XPEDXWCUtils.getAllChildCustomersDoc(XPEDXWCUtils
					.getLoggedInCustomerFromSession(getWCContext()));
		} else {
			document = XPEDXWCUtils.getAllChildCustomersDoc(getWCContext()
					.getCustomerId());
		}
		if (document != null) {
			outElem = document.getDocumentElement();
		}
		if (outElem != null) {
			NodeList nodeList = outElem.getChildNodes();
			int length = nodeList.getLength();
			for (int count = 0; count < length; count++) {
				Node wNode = nodeList.item(count);
				NamedNodeMap namedNodeMap = wNode.getAttributes();
				Node customerNode = namedNodeMap.getNamedItem("CustomerID");
				String customer = customerNode.getTextContent();
				if (!customers1.contains(customer)) {
					customers1.add(customer);
				}
			}
		}
		getAssignedCustomers();
		minusFromAvaialbleCustomers();
		listSize = customers1.size() + customers2.size() + 2;
		return SUCCESS;
	}
	
	public String getPaginatedCustomersInHierarchy() {
		//Element outElem = null;
		Document document = null;
		String msapCustomerId = null;
		Set<String> custIds = new HashSet<String>();
		//Set<String> sapIds = new HashSet<String>();
		if (XPEDXWCUtils.isCustomerSelectedIntoConext(getWCContext())) {
			msapCustomerId = XPEDXWCUtils.getLoggedInCustomerFromSession(getWCContext());
		} else {
			msapCustomerId = getWCContext().getCustomerId();
		}
		document = XPEDXWCUtils.getPaginatedShipTosForMIL(msapCustomerId, XPEDXConstants.MASTER_CUSTOMER_SUFFIX_TYPE, pageNumber.toString(), pageSize.toString(), pageSetToken, getWCContext());
		if(document!=null) {		
			Element customerHierarchyElem = SCXmlUtil.getChildElement(document.getDocumentElement(), "Output");
			try {
				parsePageInfo(document.getDocumentElement(), true);
			}
			catch (Exception e) {
				log.error("Error parsing the Page Element in getCustomersInHierarchyUsingView()");
				e.printStackTrace();
			}
			Element viewListElem = SCXmlUtil.getChildElement(customerHierarchyElem, "XPXCustHierarchyViewList");
			ArrayList<Element> assignedCustElems = SCXmlUtil.getElements(viewListElem, "XPXCustHierarchyView");
			for(int i=0; i<assignedCustElems.size(); i++) {
				Element custElem = assignedCustElems.get(i);
				String shipToId = SCXmlUtil.getAttribute(custElem, "ShipToCustomerID");
				String billToId = SCXmlUtil.getAttribute(custElem, "BillToCustomerID");
				String sapId = SCXmlUtil.getAttribute(custElem, "SAPCustomerID");
				custIds.add(shipToId);custIds.add(billToId);custIds.add(sapId);
				//customers1.addAll(custIds);
				//billToIds.add(billToId);
				//sapIds.add(sapId);
			}
			customers1.addAll(custIds);
		}
		if (customerContactId == null
				|| customerContactId.trim().length() == 0) {
			customerContactId = getWCContext().getLoggedInUserId();
		}
		List<String> assignedCustomerList = XPEDXWCUtils.getAssignedCustomers(customerContactId);
		for (String customer : assignedCustomerList) {
			if (!customers2.contains(customer)) {
				customers2.add(customer);
			}
		}
		if(alreadySelectedCustomers!=null && alreadySelectedCustomers.size()>0) {
			Iterator<String> iterator = alreadySelectedCustomers.iterator();
			while(iterator.hasNext()) {
				String customerID = iterator.next();
				if(customers1.contains(customerID))
					customers1.remove(customerID);
			}			
		}
		else {
			minusFromAvaialbleCustomers();			
		}		
		listSize = customers1.size() + customers2.size() + 2;
		return SUCCESS;
	}

	private void minusFromAvaialbleCustomers() {
		Iterator<String> iterator = customers1.iterator();
		while (iterator.hasNext()) {
			String customer1 = iterator.next();
			if (customers2.contains(customer1)) {
				iterator.remove();
			}
		}
	}

	public String getAssignedCustomers() {
		try {
			if (customerContactId == null
					|| customerContactId.trim().length() == 0) {
				customerContactId = getWCContext().getLoggedInUserId();
			}
			List<String> assignedCustomerList = XPEDXWCUtils
					.getAssignedCustomers(customerContactId);
			for (String customer : assignedCustomerList) {
				if (!customers2.contains(customer)) {
					customers2.add(customer);
				}
			}
			// JIRA 1457 - removed this as required by this defect.
			/*if (customers2.size() > 0
					&& !customers2.contains(XPEDXWCUtils.MY_SELF)) {
				customers2.add(0, XPEDXWCUtils.MY_SELF);
			}*/
			 updateShipToAdresses();

		} catch (Exception ex) {
			log.error("No ShipTo customers available");
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String getPaginatedAssignedCustomers() {
		if (customerContactId == null
				|| customerContactId.trim().length() == 0) {
			customerContactId = getWCContext().getLoggedInUserId();
		}
		Map<String,Element> outputMap = new HashMap<String,Element>();
		try {
			outputMap = prepareAndInvokeMashups();
			setDefaultShipTo();
			Element outputElem = outputMap.get("XPEDXGetPaginatedCustomerAssignments");
			Element customerAssignment = SCXmlUtil.getChildElement(outputElem, "Output");
//			Performance Fix - Removal of the mashup call of - XPEDXGetPaginatedCustomerAssignments			
			assignedShipToCount = Integer.parseInt(SCXmlUtil.getChildElement(customerAssignment, "XPXCustomerAssignmentViewList").getAttribute("TotalNumberOfRecords"));
			parsePageInfo(outputElem, true);
			parseForShipToAddress(customerAssignment,false);
		} catch (XMLExceptionWrapper e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		} catch (CannotBuildInputException e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		}
		catch (Exception e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		}
		return SUCCESS;
		
	}
	
	//JIRA 1878 : for setting the pre-select customer id for default ship to AssignedList
	public String getPaginatedAssignedCustomersForDefaultShipTo() {
		if (customerContactId == null
				|| customerContactId.trim().length() == 0) {
			customerContactId = getWCContext().getLoggedInUserId();
		}
		Map<String,Element> outputMap = new HashMap<String,Element>();
		try {
			outputMap = prepareAndInvokeMashups();
			setDefaultShipTo();
			Element outputElem = outputMap.get("XPEDXGetPaginatedCustomerAssignments");
			Element customerAssignment = SCXmlUtil.getChildElement(outputElem, "Output");
//			Performance Fix - Removal of the mashup call of - XPEDXGetPaginatedCustomerAssignments			
			assignedShipToCount = Integer.parseInt(SCXmlUtil.getChildElement(customerAssignment, "XPXCustomerAssignmentViewList").getAttribute("TotalNumberOfRecords"));
			parsePageInfo(outputElem, true);
			parseForShipToAddress(customerAssignment,true);
		} catch (XMLExceptionWrapper e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		} catch (CannotBuildInputException e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		}
		catch (Exception e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		}
		return SUCCESS;
		
	}
	
	private void setDefaultShipTo() {
		try {
			
			//JIRA 1878 Start
			if (customerContactId != null
					|| customerContactId.trim().length() > 0) {
				defaultShipToCustomerId = XPEDXWCUtils.getDefaultShipTo(customerContactId);
			}
			else{
				defaultShipToCustomerId = XPEDXWCUtils.getDefaultShipTo();
			}
			//JIRA 1878 END
			
			if (defaultShipToCustomerId != null
					&& defaultShipToCustomerId.trim().length() > 0) {
				defualtShipToAddress = XPEDXWCUtils.getShipToAdress(
						defaultShipToCustomerId, getWCContext().getStorefrontId());
			}
			//wcContext.setWCAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,"true",WCAttributeScope.LOCAL_SESSION);
			//Commenting to use new method to put in cache
			//wcContext.getSCUIContext().getRequest().getSession().setAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED, "true");
			XPEDXWCUtils.setObectInCache(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED, "true");
			XPEDXWCUtils.setObectInCache(XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT, "true");
			XPEDXWCUtils.removeObectFromCache("showSampleRequest");
			resetOrganizationValuesForShipToCustomer();
			boolean isCustomerSelectedIntoContext = XPEDXWCUtils
					.isCustomerSelectedIntoConext(getWCContext());
			if (!isCustomerSelectedIntoContext && defaultShipToCustomerId != null
					&& defaultShipToCustomerId.trim().length() > 0) {
				XPEDXWCUtils.setCurrentCustomerIntoContext(defaultShipToCustomerId,
						getWCContext());
			}
		}
		catch (Exception e) {
			log.error("Exception while getting the Default Ship To for the customer Contact");
			e.printStackTrace();
		}
	}

	private void updateShipToAdresses() throws CannotBuildInputException {		
		defaultShipToCustomerId = XPEDXWCUtils.getDefaultShipTo();
		if (defaultShipToCustomerId != null
				&& defaultShipToCustomerId.trim().length() > 0) {
			defualtShipToAddress = XPEDXWCUtils.getShipToAdress(
					defaultShipToCustomerId, getWCContext().getStorefrontId());
		}
		// store the information that the default ship to address has been changed.
		//wcContext.setWCAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,"true",WCAttributeScope.LOCAL_SESSION);
		//commenting to use new method to put in to cache.
		//wcContext.getSCUIContext().getRequest().getSession().setAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED, "true");
		XPEDXWCUtils.setObectInCache(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED, "true");
		XPEDXWCUtils.setObectInCache(XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT, "true");
		XPEDXWCUtils.removeObectFromCache("showSampleRequest");
		resetOrganizationValuesForShipToCustomer();
		boolean isCustomerSelectedIntoContext = XPEDXWCUtils
				.isCustomerSelectedIntoConext(getWCContext());
		if (!isCustomerSelectedIntoContext && defaultShipToCustomerId != null
				&& defaultShipToCustomerId.trim().length() > 0) {
			XPEDXWCUtils.setCurrentCustomerIntoContext(defaultShipToCustomerId,
					getWCContext());
		}
		List<String> assignedShipTos = XPEDXWCUtils.getAllAssignedShiptoCustomerIdsForAUser(getWCContext().getLoggedInUserId(),getWCContext());
		if (assignedShipTos == null) {
			assignedShipTos = new ArrayList<String>();
		}
		if (defaultShipToCustomerId != null) {
			assignedShipTos.remove(defaultShipToCustomerId);
		}
		assignedShipToList = new ArrayList<XPEDXShipToCustomer>();
		assignedShipToList = getShipToAdress(assignedShipTos, wcContext.getStorefrontId(),wcContext);
		/* --- Removing this as this calls for customer Details for each and every customer.
		 * using Complex Query and getting the customer information in one shot.
		if (assignedShipTos != null) {
			if (listSize > 0) {
				for (int index = 0; index < listSize; index++) {
					XPEDXShipToCustomer xPEDXShipToCustomer = XPEDXWCUtils
							.getShipToAdress(assignedShipTos.get(index),
									getWCContext().getStorefrontId());
					String customerId = xPEDXShipToCustomer.getCustomerID();
					xPEDXShipToCustomer
							.setHTMLValue(customerId.equals(getWCContext()
									.getCustomerId()) ? customerId : "");
					if(customerId.trim().equalsIgnoreCase(wcContext.getCustomerId()))
						currentCustomer = customerId;

					assignedShipToList.add(xPEDXShipToCustomer);
				}
			}
		}
		*/
	}
	
	private void parseForShipToAddress(Element customerAssignmentViewElem, boolean isForDefaultShipto) {
		Element viewListElem = SCXmlUtil.getChildElement(customerAssignmentViewElem, "XPXCustomerAssignmentViewList");
		ArrayList<Element> assignedCustElems = SCXmlUtil.getElements(viewListElem, "XPXCustomerAssignmentView");
		assignedShipToList = new ArrayList<XPEDXShipToCustomer>();
		if(assignedCustElems.size()>0) {
			shipToResult=false;
			for(int i=0;i<assignedCustElems.size();i++) {
				Element customer = assignedCustElems.get(i);
				XPEDXShipToCustomer defualtShipToAssigned = new XPEDXShipToCustomer();
				defualtShipToAssigned.setCustomerID(SCXmlUtil.getAttribute(customer, "ShipToCustomerID"));
				log.debug(SCXmlUtil.getString(customer));
				defualtShipToAssigned.setOrganizationName(SCXmlUtil.getAttribute(customer,"ShipToCustomerName"));
				Element element = customer;
				defualtShipToAssigned.setFirstName(SCXmlUtil.getAttribute(element,
						"FirstName"));
				defualtShipToAssigned.setMiddleName(SCXmlUtil.getAttribute(element,
						"MiddleName"));
				log.debug("************MiddleName************="
						+ SCXmlUtil.getAttribute(element, "MiddleName"));
				defualtShipToAssigned.setLastName(SCXmlUtil.getAttribute(element,
						"LastName"));
				log.debug("************LastName************="
						+ SCXmlUtil.getAttribute(element, "LastName"));
				defualtShipToAssigned.setEMailID(SCXmlUtil.getAttribute(element,
						"EMailID"));
				defualtShipToAssigned.setEMailID(SCXmlUtil.getAttribute(element,
						"EMailID"));
				defualtShipToAssigned.setLocationID(SCXmlUtil.getAttribute(element, "ShipToExtnCustStoreNo"));
				defualtShipToAssigned.setCity(SCXmlUtil.getAttribute(element, "City"));
				defualtShipToAssigned
						.setState(SCXmlUtil.getAttribute(element, "State"));
				defualtShipToAssigned.setCountry(SCXmlUtil.getAttribute(element,
						"Country"));
				defualtShipToAssigned.setZipCode(SCXmlUtil.getAttribute(element,
						"ZipCode"));
				defualtShipToAssigned.setCompany(SCXmlUtil.getAttribute(element,
						"Company"));
				defualtShipToAssigned.setDayPhone(SCXmlUtil.getAttribute(element,
				"DayPhone"));
				
				List<String> addressList = new ArrayList<String>();
				for (int index = 0; index < 6; index++) {
					if (SCXmlUtil.getAttribute(element, "AddressLine" + index) != null
							&& SCXmlUtil.getAttribute(element, "AddressLine" + index)
									.trim().length() > 0) {
						addressList.add(SCXmlUtil.getAttribute(element, "AddressLine"
								+ index));
					}
				}
				defualtShipToAssigned.setAddressList(addressList);
				String customerId = defualtShipToAssigned.getCustomerID();
				
				//Jira 1878 - for setting pre-select default ship to in assigned customerList
				if(isForDefaultShipto){
					defualtShipToAssigned
					.setHTMLValue(customerId.equals(defaultShipToCustomerId) ? customerId : "");					
				}else{
				defualtShipToAssigned
						.setHTMLValue(customerId.equals(wcContext.getCustomerId()) ? customerId : "");
				}
				assignedShipToList.add(defualtShipToAssigned);
			}
		}
	}

	/**
	 * <CustomerAssignment CustomerID="" OrganizationCode="" UserId=""
	 * Operation="" />
	 * 
	 * @return
	 */
	public String saveChanges() {
		saveChanges(customers2, "Create");
		saveChanges(customers1, "Delete");
		return SUCCESS;
	}

	private void saveChanges(List<String> wList, String operation) {
		for (int index = 0; index < wList.size(); index++) {
			try {
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("/CustomerAssignment/@CustomerID", wList
						.get(index));
				valueMap.put("/CustomerAssignment/@OrganizationCode",
						getWCContext().getBuyerOrgCode());
				valueMap.put("/CustomerAssignment/@UserId", customerContactId);
				valueMap.put("/CustomerAssignment/@Operation", operation);
				Element input = WCMashupHelper.getMashupInput(
						"xpedxSaveCustomerAssignments", valueMap, wcContext
								.getSCUIContext());
				String inputXml = SCXmlUtil.getString(input);
				LOG.debug("Input XML: " + inputXml);
				Object obj = WCMashupHelper.invokeMashup(
						"xpedxSaveCustomerAssignments", input, wcContext
								.getSCUIContext());
				Document outputDoc = null;
				if (obj != null) {
					outputDoc = ((Element) obj).getOwnerDocument();
					if (null != outputDoc) {
						String outputXml = SCXmlUtil.getString((Element) obj);
						LOG.debug("Output XML: " + outputXml);
					}
				}
			} catch (Exception ex) {
				log.debug("Record already exists");
			}
		}
	}

	public void setSelectedShipToAsDefault() throws CannotBuildInputException,
			InstantiationException, IllegalAccessException,
			YIFClientCreationException, YFSException, RemoteException {
		ISCUITransactionContext scuiTransactionContext = null;
		YFSEnvironment env = null;
		SCUIContext wSCUIContext = null;
		String selectedCustomerContactId = "";
		try {
			if (XPEDXWCUtils.isCustomerSelectedIntoConext(getWCContext())) {
				
				// Jira 1878 : Start : sending the customer contact id of selected user instead of logged in user, for input xml
				selectedCustomerContactId = getWCContext().getSCUIContext().getRequest().getParameter("selectedCustomerContactId");
				if(!(selectedCustomerContactId != null && selectedCustomerContactId.trim().length()>0)){
					selectedCustomerContactId = wcContext.getCustomerContactId();
				}
				// Jira 1878 : End
				
				String selectedCustomerID=getSelectedCurrentCustomer();
				if(SCUtil.isVoid(selectedCustomerID))
				{
					selectedCustomerID=wcContext.getCustomerId();
				}
				
				String inputXml = "<Customer CustomerID='"
						+ XPEDXWCUtils
								.getLoggedInCustomerFromSession(wcContext)
						+ "' " + "OrganizationCode='"
						+ wcContext.getStorefrontId() + "'> "
						+ "<CustomerContactList>"
						+ "<CustomerContact CustomerContactID='"
						+ selectedCustomerContactId + "'> "
						+ "<Extn ExtnDefaultShipTo='"
						+ selectedCustomerID + "' /> "
						+ "</CustomerContact> " + "</CustomerContactList>"
						+ "</Customer> ";
				Document document = getXMLUtils().createFromString(inputXml);
				wSCUIContext = wcContext.getSCUIContext();
				scuiTransactionContext = wSCUIContext
						.getTransactionContext(true);
				env = (YFSEnvironment) scuiTransactionContext
						.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
				YIFApi api = YIFClientFactory.getInstance().getApi();
				Document outputListDocument = api.invoke(env, "manageCustomer",
						document);
				LOG.debug(SCXmlUtil.getString(outputListDocument));
			}
		}
		catch(Exception ex){
			log.debug(ex);
		}
		finally {
			SCUITransactionContextHelper.releaseTransactionContext(
					scuiTransactionContext, wSCUIContext);
			scuiTransactionContext = null;
			env = null;
		}
	}

	public void setCurrentCustomerIntoContext() throws CannotBuildInputException {
		
		String selectedCustomerContactId = getWCContext().getSCUIContext().getRequest().getParameter("selectedCustomerContactId");
		if(SCUtil.isVoid(selectedCustomerContactId))
			selectedCustomerContactId = wcContext.getCustomerContactId();
		String  contaxtCustomerContactID = wcContext.getCustomerContactId();
		XPEDXWCUtils.setObectInCache(XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT, "true");
		resetOrganizationValuesForShipToCustomer();
		if(contaxtCustomerContactID.equals(selectedCustomerContactId)){
			
			XPEDXWCUtils.setCurrentCustomerIntoContext(
					getSelectedCurrentCustomer(), getWCContext());
			if (isShipingAddressOverridden()) {
				getWCContext().getSCUIContext().getSession().setAttribute(
						XPEDXWCUtils.XPEDX_SHIP_TO_ADDRESS_OVERIDDEN,
						xOverriddenShipToAddress);
			} else {
				getWCContext().getSCUIContext().getSession().removeAttribute(
						XPEDXWCUtils.XPEDX_SHIP_TO_ADDRESS_OVERIDDEN);
			}
			changeCurrentCartOwner();
			
		}
		// commenting for perofrmance issue this will be done in header action again it is not required.
		//setSampleRequestFlagInSession(); 
		if (setSelectedAsDefault) {
			try {
				setSelectedShipToAsDefault();
			} catch (Exception e) {
				log.error("Cannot set the customer as default. please try again later");
				e.printStackTrace();
			}
		}
			
	}

	public void resetOrganizationValuesForShipToCustomer(){
		XPEDXShipToCustomer shipToCustomer = (XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		shipToCustomer.setShipToOrgExtnMinOrderAmt(null);
		shipToCustomer.setShipToOrgExtnSmallOrderFee(null);
		shipToCustomer.setShipToOrgOrganizationName(null);
		shipToCustomer.setShipToOrgCorporatePersonInfoState(null);
		shipToCustomer.setShipToDivDeliveryCutOffTime(null);
		XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER, shipToCustomer);
	}

	//Added by - Jira 1700
	private void setSampleRequestFlagInSession() throws CannotBuildInputException
	{
			String billToCustomerId = XPEDXWCUtils.getParentCustomer(wcContext.getCustomerId(), wcContext);
			XPEDXWCUtils.setSampleRequestFlag(billToCustomerId,wcContext);
	}
	//Ends

	
	private boolean isShipingAddressOverridden() {
		if (xOverriddenShipToAddress != null) {
			boolean isNotVoid = !(YFCCommon
					.isStringVoid(xOverriddenShipToAddress
							.getXpedxSTAddressLine2())
					&& YFCCommon.isStringVoid(xOverriddenShipToAddress
							.getXpedxSTAddressLine3())
					&& YFCCommon.isStringVoid(xOverriddenShipToAddress
							.getXpedxSTCity())
					&& YFCCommon.isStringVoid(xOverriddenShipToAddress
							.getXpedxSTName())
					&& YFCCommon.isStringVoid(xOverriddenShipToAddress
							.getXpedxSTState()) && YFCCommon
					.isStringVoid(xOverriddenShipToAddress.getXpedxSTZip()));
			System.out
					.println("isShipingAddressOverridden is ********************"
							+ isNotVoid);

			return isNotVoid;
		}
		return false;
	}

	private void changeCurrentCartOwner() {
		String orderHeaderKey=(String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");
		/*String orderHeaderKey = XPEDXCommerceContextHelper
				.getCartInContextOrderHeaderKey(getWCContext());*/
		if (orderHeaderKey != null) {
			log.debug("Changing the owner of the order");
			XPEDXWCUtils.changeOrderOwnerToSelectedCustomer(getWCContext(),
					orderHeaderKey);
			log.debug("Changed the cart owner");
		}
	}
	
	public static ArrayList<XPEDXShipToCustomer> getShipToAdress(List<String> customerIds,
			String organizationCode, IWCContext wcContext) throws CannotBuildInputException {
		if(customerIds == null || organizationCode == null || !(customerIds.size()>0) || !(organizationCode.trim().length()>0))
			return null;
		ArrayList<XPEDXShipToCustomer> assignedShipToCustomers = new ArrayList<XPEDXShipToCustomer>();
		Document document = XPEDXWCUtils.getCustomerDetails(customerIds, organizationCode, customerShipToInformationMashUp);
		ArrayList<Element> customers = SCXmlUtil.getElements(document.getDocumentElement(), "Customer");
		if(customers.size()>0) {
			for(int i=0;i<customers.size();i++) {
				Element customer = customers.get(i);
				XPEDXShipToCustomer defualtShipToAssigned = new XPEDXShipToCustomer();
				defualtShipToAssigned.setCustomerID(SCXmlUtil.getAttribute(customer, "CustomerID"));
				log.debug(SCXmlUtil.getString(customer));
				Element buyerOrgElement = SCXmlUtil.getChildElement(customer, "BuyerOrganization");
				defualtShipToAssigned.setOrganizationName(SCXmlUtil.getAttribute(buyerOrgElement,"OrganizationName"));
				Element element = SCXmlUtil.getElementByAttribute(customer, "CustomerAdditionalAddressList/CustomerAdditionalAddress","IsDefaultShipTo","Y");
				if(element == null) {
					element = SCXmlUtil.getChildElement(customer, "CustomerAdditionalAddressList");
					element = SCXmlUtil.getChildElement(element,
							"CustomerAdditionalAddress");
				}				
				element = SCXmlUtil.getChildElement(element, "PersonInfo");
				defualtShipToAssigned.setFirstName(SCXmlUtil.getAttribute(element,
						"FirstName"));
				defualtShipToAssigned.setMiddleName(SCXmlUtil.getAttribute(element,
						"MiddleName"));
				log.debug("************MiddleName************="
						+ SCXmlUtil.getAttribute(element, "MiddleName"));
				defualtShipToAssigned.setLastName(SCXmlUtil.getAttribute(element,
						"LastName"));
				log.debug("************LastName************="
						+ SCXmlUtil.getAttribute(element, "LastName"));
				defualtShipToAssigned.setEMailID(SCXmlUtil.getAttribute(element,
						"EMailID"));
				defualtShipToAssigned.setEMailID(SCXmlUtil.getAttribute(element,
						"EMailID"));
				defualtShipToAssigned.setCity(SCXmlUtil.getAttribute(element, "City"));
				defualtShipToAssigned
						.setState(SCXmlUtil.getAttribute(element, "State"));
				defualtShipToAssigned.setCountry(SCXmlUtil.getAttribute(element,
						"Country"));
				defualtShipToAssigned.setZipCode(SCXmlUtil.getAttribute(element,
						"ZipCode"));
				defualtShipToAssigned.setCompany(SCXmlUtil.getAttribute(element,
						"Company"));
				defualtShipToAssigned.setDayPhone(SCXmlUtil.getAttribute(element,
				"DayPhone"));
				
				List<String> addressList = new ArrayList<String>();
				for (int index = 0; index < 6; index++) {
					if (SCXmlUtil.getAttribute(element, "AddressLine" + index) != null
							&& SCXmlUtil.getAttribute(element, "AddressLine" + index)
									.trim().length() > 0) {
						addressList.add(SCXmlUtil.getAttribute(element, "AddressLine"
								+ index));
					}
				}
				defualtShipToAssigned.setAddressList(addressList);
				String customerId = defualtShipToAssigned.getCustomerID();
				defualtShipToAssigned
						.setHTMLValue(customerId.equals(wcContext.getCustomerId()) ? customerId : "");
				assignedShipToCustomers.add(defualtShipToAssigned);
			}
		}
		return assignedShipToCustomers;
	}
	
	public String searchShipToAddress() {
		if (customerContactId == null
				|| customerContactId.trim().length() == 0) {
			customerContactId = getWCContext().getLoggedInUserId();
		}
		Map<String,Element> outputMap = new HashMap<String,Element>();
		String returnVal = null;
		if(!"Search Criteria".equalsIgnoreCase(searchTerm)) {
			try {
				setSearch(true);
				outputMap = prepareAndInvokeMashups();
				setDefaultShipTo();
				Element outputElem = outputMap.get("XPEDXSearchCustomerAssignmentsPaginated");
				Element customerAssignment = SCXmlUtil.getChildElement(outputElem, "Output");
//				Performance Fix - Removal of the mashup call of - XPEDXGetPaginatedCustomerAssignments
				comingFromSearch = "true";
				assignedShipToCount = Integer.parseInt(SCXmlUtil.getChildElement(customerAssignment, "XPXCustomerAssignmentViewList").getAttribute("TotalNumberOfRecords"));
				parsePageInfo(outputElem, true);
				parseForShipToAddress(customerAssignment,false);
			} catch (XMLExceptionWrapper e) {
				log.error("Error getting the Customer Assignments");
				e.printStackTrace();
			} catch (CannotBuildInputException e) {
				log.error("Error getting the Customer Assignments");
				e.printStackTrace();
			}
			catch (Exception e) {
				log.error("Error getting the Customer Assignments");
				e.printStackTrace();
			}
		}
		else
		{
			returnVal = getPaginatedAssignedCustomers();
		}
		if(returnVal!=null)
			return returnVal;
		return SUCCESS;
		/*getAssignedCustomers();
		ArrayList<String> searchStrings = new ArrayList<String>();
		if(searchTerm.indexOf(",")!= -1) {
			String[] searchStrings1 = searchTerm.split(",");
			for(int i=0;i<searchStrings1.length;i++)
			{
				searchStrings.add(searchStrings1[i]);
			}
		}
		if(searchTerm.indexOf(".")!= -1) {
			String[] searchStrings2 = searchTerm.split(".");
			for(int i=0;i<searchStrings2.length;i++)
			{
				searchStrings.add(searchStrings2[i]);
			}
		}
		if(searchStrings.size()<=0)
			searchStrings.add(searchTerm);
		if(searchStrings.size()> 0) {
			for (int i=0; i<searchStrings.size(); i++) {
				for(int j=0;j<assignedShipToList.size();j++){
					if(assignedShipToList.get(j).toString().toUpperCase().contains(searchStrings.get(i).trim().toUpperCase())) {
						if(addressSearchResult==null){
							addressSearchResult = new LinkedHashSet<XPEDXShipToCustomer>();
							}
						addressSearchResult.add(assignedShipToList.get(j));
					}
				}				
			}
		}
		*/
	}
	
	private void parsePageInfo(Element ele, boolean paginated) throws Exception {

        UtilBean util = new UtilBean();
        Element page = util.getElement(ele, "//Page");

        setIsLastPage(Boolean.FALSE);
        if ((paginated) && (page != null)) {
        	setIsLastPage(SCXmlUtil.getBooleanAttribute(page, "IsLastPage", getIsLastPage()));
        }

        setIsFirstPage(Boolean.FALSE);
        if ((paginated) && (page != null)) {
            setIsFirstPage(SCXmlUtil.getBooleanAttribute(page, "IsFirstPage", getIsFirstPage()));
        }

        setIsValidPage(Boolean.FALSE);
        if ((paginated) && (page != null)) {
        	setIsValidPage(SCXmlUtil.getBooleanAttribute(page, "IsValidPage", getIsValidPage()));
        }

        if ((paginated) && (page != null)) {
            setPageNumber(getIntegerAttribute(page, "PageNumber", getPageNumber()));
        }

        if ((paginated) && (page != null)) {
			setPageSetToken(page.getAttribute("PageSetToken"));
        }
        
        setTotalNumberOfPages(new Integer(0));
        if ((paginated) && (page != null)) {
        	setTotalNumberOfPages(getIntegerAttribute(page,
                    "TotalNumberOfPages", getTotalNumberOfPages()));
        }
    }
	
	private Integer getIntegerAttribute(Element page, String name,
            Integer defaultValue) {
        Integer value = defaultValue;
        String str = page.getAttribute(name);
        try {
            value = Integer.valueOf(str);
        } catch (NumberFormatException e) {
            value = defaultValue;
        }
        return value;
    }
	
	private static final String customerShipToInformationMashUp = "xpedx-customerlist-getCustomerAddressInformation";

	public String getDefaultShipToCustomerId() {
		return defaultShipToCustomerId;
	}

	public void setDefaultShipToCustomerId(String defaultShipToCustomerId) {
		this.defaultShipToCustomerId = defaultShipToCustomerId;
	}

	public String getCurrentCustomer() {
		if(currentCustomer.trim().length()<=0 && currentCustomer!=null)
			currentCustomer = wcContext.getCustomerId();
		return currentCustomer;
	}

	public void setCurrentCustomer(String currentCustomer) {
		this.currentCustomer = currentCustomer;
	}

	public boolean isSetSelectedAsDefault() {
		return setSelectedAsDefault;
	}

	public void setSetSelectedAsDefault(boolean setSelectedAsDefault) {
		this.setSelectedAsDefault = setSelectedAsDefault;
	}

	public String getOrderByDesc() {
		return orderByDesc;
	}

	public void setOrderByDesc(String orderByDesc) {
		this.orderByDesc = orderByDesc;
	}

	public String getOrderByAttribute() {
		return orderByAttribute;
	}

	public void setOrderByAttribute(String orderByAttribute) {
		this.orderByAttribute = orderByAttribute;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Boolean getIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(Boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public Boolean getIsLastPage() {
		return isLastPage;
	}

	public void setIsLastPage(Boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public Boolean getIsValidPage() {
		return isValidPage;
	}

	public void setIsValidPage(Boolean isValidPage) {
		this.isValidPage = isValidPage;
	}

	public Integer getTotalNumberOfPages() {
		return totalNumberOfPages;
	}

	public void setTotalNumberOfPages(Integer totalNumberOfPages) {
		this.totalNumberOfPages = totalNumberOfPages;
	}

	public String getDivId() {
		return divId;
	}

	public void setDivId(String divId) {
		this.divId = divId;
	}

	public List<String> getAlreadySelectedCustomers() {
		return alreadySelectedCustomers;
	}

	public void setAlreadySelectedCustomers(List<String> alreadySelectedCustomers) {
		this.alreadySelectedCustomers = alreadySelectedCustomers;
	}

//	Performance Fix - Removal of the mashup call of - XPEDXGetPaginatedCustomerAssignments
	public Integer getAssignedShipToCount() {
		return assignedShipToCount;
	}

	public void setAssignedShipToCount(Integer assignedShipToCount) {
		this.assignedShipToCount = assignedShipToCount;
	}

	public String getComingFromSearch() {
		return comingFromSearch;
	}

	public void setComingFromSearch(String comingFromSearch) {
		this.comingFromSearch = comingFromSearch;
	}

	
}

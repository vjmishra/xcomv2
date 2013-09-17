package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPEDXCustomerAssignmentAction extends WCMashupAction {

	private static final long serialVersionUID = 4203292437195126816L;
	private List<String> customers1 = new ArrayList<String>();
	private List<String> customers2 = new ArrayList<String>();
	private List<String> shipToStr = new ArrayList<String>();
	private String customerContactId = "";
	private String customerId = "";
	private String selectedCurrentCustomer = "";
	private String currentCustomer = "";
	private XPEDXShipToCustomer defualtShipToAddress;
	private List<XPEDXShipToCustomer> assignedShipToList;
	// Performance Fix - Removal of the mashup call of -
	// XPEDXGetPaginatedCustomerAssignments
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
	// Updated from 25 to 6 for JIRA 2875
	protected Integer pageSize = 6;
	private Boolean isFirstPage = Boolean.FALSE;
	private Boolean isLastPage = Boolean.FALSE;
	private Boolean isValidPage = Boolean.FALSE;
	private Integer totalNumberOfPages = Integer.valueOf(1);
	private String divId = "ajax-assignedShipToCustomers";
	private List<String> alreadySelectedCustomers;
	private String rootCustomerKey;
	// Added custId for Jira 4146
	private int authListSize;
	private String custID = "";
	private String existingCustId = "";
	private String addToavailable = "";
	private String removeFromavailable = "";
	private LinkedHashMap<String, String> availableLocationMap = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> authorizedLocationMap = new LinkedHashMap<String, String>();
	private String status="";
	private boolean isDefaultShipToCustSuspended= false;

	public boolean isDefaultShipToCustSuspended() {
		return isDefaultShipToCustSuspended;
	}

	public void setDefaultShipToCustSuspended(boolean isDefaultShipToCustSuspended) {
		this.isDefaultShipToCustSuspended = isDefaultShipToCustSuspended;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAuthListSize() {

		return authListSize;
	}

	public void setAuthListSize(int authListSize) {
		this.authListSize = authListSize;
	}

	public String getAddToavailable() {
		return addToavailable;
	}

	public void setAddToavailable(String addToavailable) {
		this.addToavailable = addToavailable;
	}

	public String getRemoveFromavailable() {
		return removeFromavailable;
	}

	public void setRemoveFromavailable(String removeFromavailable) {
		this.removeFromavailable = removeFromavailable;
	}

	public String getExistingCustId() {
		return existingCustId;
	}

	public void setExistingCustId(String existingCustId) {
		this.existingCustId = existingCustId;
	}

	public String getCustID() {
		return custID;
	}

	public void setCustID(String custID) {
		this.custID = custID;
	}

	public String getRootCustomerKey() {
		return rootCustomerKey;
	}

	public void setRootCustomerKey(String rootCustomerKey) {
		this.rootCustomerKey = rootCustomerKey;
	}

	public boolean shipToResult = true;
	private String pageSetToken;

	public List<String> getShipToStr() {
		return shipToStr;
	}

	public void setShipToStr(List<String> shipToStr) {
		this.shipToStr = shipToStr;
	}

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

	public void setAddressSearchResult(
			Set<XPEDXShipToCustomer> addressSearchResult) {
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

		XPEDXWCUtils.removeObectFromCache("SessionForUserProfile");
		customers2 = (List<String>) XPEDXWCUtils
				.getObjectFromCache("CUSTOMER2");
		populateAvailableLocation();
		getSortedAssignedCustomer();
		listSize = customers1.size() + customers2.size() + 2;
		return SUCCESS;
	}

	private void getSortedAssignedCustomer() {
		ArrayList<Element> assignedCustomers = (ArrayList<Element>) XPEDXWCUtils
				.getObjectFromCache("AUTHORIZED_LOCATIONS");
		if (assignedCustomers != null) {
			Collections.sort(assignedCustomers, new Comparator<Element>() {
				@Override
				public int compare(Element elem, Element elem1) {
					String customerPath1 = elem.getAttribute("CustomerPath");
					String customerPath2 = elem1.getAttribute("CustomerPath");
					return customerPath1.compareTo(customerPath2);
				}
			});
			for (Element authorizeCustomer : assignedCustomers) {
				String customerID = authorizeCustomer
						.getAttribute("CustomerID");
				String custFullAddress = authorizeCustomer
						.getAttribute("CustomerAddress");
				authorizedLocationMap.put(customerID, custFullAddress);
			}
		}
	}

	private void populateAvailableLocation() {
		ArrayList<Element> assignedCustomers = (ArrayList<Element>) XPEDXWCUtils
				.getObjectFromCache("AUTHORIZED_LOCATIONS");
		ArrayList<Element> availableCustomers = (ArrayList<Element>) XPEDXWCUtils
				.getObjectFromCache("AVAILABLE_LOCATIONS");
		if (availableCustomers != null && availableCustomers.size() <= pageSize
				&& pageNumber > 1) {
			pageNumber = 1;
		}
		if (availableCustomers != null) {
			listSize = availableCustomers.size() + assignedCustomers.size() + 2;
		}
		Document document = XPEDXWCUtils.getPaginatedCustomers(rootCustomerKey,
				getSelectedCurrentCustomer(), pageNumber.toString(),
				pageSize.toString(), pageSetToken, getWCContext(),
				assignedCustomers, availableCustomers);
		if (document != null) {
			Element customerHierarchyElem = SCXmlUtil.getChildElement(
					document.getDocumentElement(), "Output");
			try {
				parsePageInfo(document.getDocumentElement(), true);
			} catch (Exception e) {
				log.error("Error parsing the Page Element in getCustomersInHierarchyUsingView()");
				e.printStackTrace();
			}

			Element viewListElem = SCXmlUtil.getChildElement(
					customerHierarchyElem, "XPXCustViewList");
			ArrayList<Element> assignedCustElems = SCXmlUtil.getElements(
					viewListElem, "XPXCustView");
			if (assignedCustElems != null) {
				for (int i = 0; i < assignedCustElems.size(); i++) {
					Element custElem = assignedCustElems.get(i);
					String custId = SCXmlUtil.getAttribute(custElem,
							"CustomerID");
					String fullAddress = SCXmlUtil.getAttribute(custElem,
							"CustomerAddress");
					availableLocationMap.put(custId, fullAddress);
				}
			}
		}

	}

	// Jira 4146 -- Method added when Add button is clicked
	public String getCustomersForAuthorize() {
		//String newcustomers = new String(custID);
		String[] arrayIds1 = custID.split(",");
		// List<String> newcustomersList = new
		// ArrayList<String>(Arrays.asList(arrayIds1));
		ArrayList<Element> assignedCustomers = (ArrayList<Element>) XPEDXWCUtils
				.getObjectFromCache("AUTHORIZED_LOCATIONS");
		ArrayList<Element> availableCustomers = (ArrayList<Element>) XPEDXWCUtils
				.getObjectFromCache("AVAILABLE_LOCATIONS");
		ArrayList<Element> tempList = new ArrayList<Element>();
		for (int i = 0; i < arrayIds1.length; i++) {
			String newcustomer1 = arrayIds1[i];
			for (Element availableCustomer : availableCustomers) {
				String customerID = availableCustomer
						.getAttribute("CustomerID");
				if (customerID.equals(newcustomer1)) {
					tempList.add(availableCustomer);
					break;
				}
			}

		}
		for (Element availableCustomer : tempList) {
			assignedCustomers.add(availableCustomer);
			availableCustomers.remove(availableCustomer);
		}
		XPEDXWCUtils.setObectInCache("AUTHORIZED_LOCATIONS", assignedCustomers);
		XPEDXWCUtils.setObectInCache("AVAILABLE_LOCATIONS", availableCustomers);
		try {
			getSortedAssignedCustomer();
		} catch (Exception e) {
			log.error("Exception while sorting " + e.getMessage());
		}
		try {
			populateAvailableLocation();
		} catch (Exception e) {
			log.error("Exception while sorting " + e.getMessage());
		}

		listSize = availableCustomers.size() + assignedCustomers.size() + 2;
		if (assignedCustomers.size() < 7) {
			setAuthListSize(7);
		} else {
			setAuthListSize(assignedCustomers.size() + 1);
		}

		return SUCCESS;

	}

	private List<String> getCustomersByPath(List<String> wList)
			throws Exception {
		String pageNumber = "1";
		String pageSize = "5000";

		HashMap<String, String> valueMap = new HashMap<String, String>();
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

		YFCElement orElement = inputCustomerDocument.createElement("Or");

		for (int i = 0; i < wList.size(); i++) {
			createInput(wList.get(i), complexQueryElement, orElement);
		}

		complexQueryElement.appendChild(orElement);
		inputCustomerElement.appendChild(complexQueryElement);
		inputCustomerElement.appendChild(sort);

		Element input = WCMashupHelper.getMashupInput(
				"xpedx-getCusotmerByCustomerPath", valueMap, wcContext);

		Element pageElement = (Element) input.getElementsByTagName("Input")
				.item(0);
		pageElement
				.appendChild(input.getOwnerDocument().importNode(
						inputCustomerDocument.getDocument()
								.getDocumentElement(), true));

		Object obj = WCMashupHelper.invokeMashup(
				"xpedx-getCusotmerByCustomerPath", input,
				wcContext.getSCUIContext());

		Document outputDoc = ((Element) obj).getOwnerDocument();

		Element outputCustomerElement = (Element) outputDoc
				.getElementsByTagName("XPXCustHierarchyViewList").item(0);

		if (null != outputCustomerElement) {
			ArrayList<Element> xpxCustViewElems = SCXmlUtil.getElements(
					outputCustomerElement, "XPXCustHierarchyView");
			for (int j = 0; j < xpxCustViewElems.size(); j++) {
				if (wList.contains(xpxCustViewElems.get(j).getAttribute(
						"MSAPCustomerID"))
						&& !shipToStr.contains(xpxCustViewElems.get(j)
								.getAttribute("MSAPCustomerID"))) {
					shipToStr.add(xpxCustViewElems.get(j).getAttribute(
							"MSAPCustomerID"));
				}
				if (wList.contains(xpxCustViewElems.get(j).getAttribute(
						"SAPCustomerID"))
						&& !shipToStr.contains(xpxCustViewElems.get(j)
								.getAttribute("SAPCustomerID"))) {
					shipToStr.add(xpxCustViewElems.get(j).getAttribute(
							"SAPCustomerID"));
				}
				if (wList.contains(xpxCustViewElems.get(j).getAttribute(
						"BillToCustomerID"))
						&& !shipToStr.contains(xpxCustViewElems.get(j)
								.getAttribute("BillToCustomerID"))) {
					shipToStr.add(xpxCustViewElems.get(j).getAttribute(
							"BillToCustomerID"));
				}
				if (wList.contains(xpxCustViewElems.get(j).getAttribute(
						"ShipToCustomerID"))
						&& !shipToStr.contains(xpxCustViewElems.get(j)
								.getAttribute("ShipToCustomerID"))) {
					shipToStr.add(xpxCustViewElems.get(j).getAttribute(
							"ShipToCustomerID"));
				}

			} // end for j=0 for loop
		}

		return shipToStr;

	}

	private void createInput(String customerID, YFCElement complexQuery,
			YFCElement or) {
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

	// End Jira 4146
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
			/*
			 * if (customers2.size() > 0 &&
			 * !customers2.contains(XPEDXWCUtils.MY_SELF)) { customers2.add(0,
			 * XPEDXWCUtils.MY_SELF); }
			 */
			updateShipToAdresses();

		} catch (Exception ex) {
			log.error("No ShipTo customers available");
			return ERROR;
		}
		return SUCCESS;
	}

	public String getPaginatedAssignedCustomers() {
		if (customerContactId == null || customerContactId.trim().length() == 0) {
			customerContactId = getWCContext().getLoggedInUserId();
		}
		Map<String, Element> outputMap = new HashMap<String, Element>();
		try {
			outputMap = prepareAndInvokeMashups();
			setDefaultShipTo();
			Element outputElem = outputMap.get("XPEDXGetPaginatedCustomerAssignments");
			Element customerAssignment = SCXmlUtil.getChildElement(outputElem,
					"Output");
			// Performance Fix - Removal of the mashup call of -
			// XPEDXGetPaginatedCustomerAssignments
			assignedShipToCount = Integer.parseInt(SCXmlUtil.getChildElement(
					customerAssignment, "XPXCustomerAssignmentViewList")
					.getAttribute("TotalNumberOfRecords"));
			parsePageInfo(outputElem, true);
			parseForShipToAddress(customerAssignment, false);
		} catch (XMLExceptionWrapper e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		} catch (CannotBuildInputException e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		} catch (Exception e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		}
		return SUCCESS;

	}

	// JIRA 1878 : for setting the pre-select customer id for default ship to
	// AssignedList
	public String getPaginatedAssignedCustomersForDefaultShipTo() {
		if (customerContactId == null || customerContactId.trim().length() == 0) {
			customerContactId = getWCContext().getLoggedInUserId();
		}
		Map<String, Element> outputMap = new HashMap<String, Element>();
		try {
			outputMap = prepareAndInvokeMashups();
			setDefaultShipTo();
			Element outputElem = outputMap
					.get("XPEDXGetPaginatedCustomerAssignments");
			Element customerAssignment = SCXmlUtil.getChildElement(outputElem,
					"Output");
			// Performance Fix - Removal of the mashup call of -
			// XPEDXGetPaginatedCustomerAssignments
			assignedShipToCount = Integer.parseInt(SCXmlUtil.getChildElement(
					customerAssignment, "XPXCustomerAssignmentViewList")
					.getAttribute("TotalNumberOfRecords"));
			parsePageInfo(outputElem, true);
			parseForShipToAddress(customerAssignment, true);
			/*EB-76 Start Changes */
			if(defualtShipToAddress!=null && defualtShipToAddress.getCustomerStatus()!=null && defualtShipToAddress.getCustomerStatus().trim().equals("30")){
				isDefaultShipToCustSuspended = true;
			}
			/*EB-76 End Changes */
		} catch (XMLExceptionWrapper e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		} catch (CannotBuildInputException e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		} catch (Exception e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		}
		return SUCCESS;

	}

	private void setDefaultShipTo() {
		try {

			// String logedinCustomer=getWCContext().getCustomerContactId();
			// JIRA 1878 Start
			if ((customerContactId != null && customerContactId.trim().length() > 0)
					&& !customerContactId.equals(getWCContext()
							.getCustomerContactId())) {
				defaultShipToCustomerId = XPEDXWCUtils
						.getDefaultShipTo(customerContactId);
				if (defaultShipToCustomerId != null
						&& defaultShipToCustomerId.trim().length() > 0) {
					defualtShipToAddress = XPEDXWCUtils.getShipToAdress(
							defaultShipToCustomerId, getWCContext()
									.getStorefrontId());
				}
			} else {
				// defaultShipToCustomerId = XPEDXWCUtils.getDefaultShipTo();
				defualtShipToAddress = ((XPEDXShipToCustomer) XPEDXWCUtils
						.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER))
						.getDefaultShipToCustomer();
			}
			// JIRA 1878 END

			// wcContext.setWCAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,"true",WCAttributeScope.LOCAL_SESSION);
			// Commenting to use new method to put in cache
			// wcContext.getSCUIContext().getRequest().getSession().setAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,
			// "true");
			// XPEDXWCUtils.setObectInCache(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,
			// "true");
			// XPEDXWCUtils.setObectInCache(XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT,
			// "true");
			XPEDXWCUtils.removeObectFromCache("showSampleRequest");
			resetOrganizationValuesForShipToCustomer();
			boolean isCustomerSelectedIntoContext = XPEDXWCUtils
					.isCustomerSelectedIntoConext(getWCContext());
			if (!isCustomerSelectedIntoContext
					&& defaultShipToCustomerId != null
					&& defaultShipToCustomerId.trim().length() > 0) {
				XPEDXWCUtils.setCurrentCustomerIntoContext(
						defaultShipToCustomerId, getWCContext());
			}
		} catch (Exception e) {
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
		// store the information that the default ship to address has been
		// changed.
		// wcContext.setWCAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,"true",WCAttributeScope.LOCAL_SESSION);
		// commenting to use new method to put in to cache.
		// wcContext.getSCUIContext().getRequest().getSession().setAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,
		// "true");
		XPEDXWCUtils.setObectInCache(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,
				"true");
		XPEDXWCUtils.setObectInCache(
				XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT, "true");
		XPEDXWCUtils.setObectInCache(XPEDXConstants.CUSTOM_FIELD_FLAG_CHANGE,
				"true");
		XPEDXWCUtils.removeObectFromCache("showSampleRequest");
		resetOrganizationValuesForShipToCustomer();
		boolean isCustomerSelectedIntoContext = XPEDXWCUtils
				.isCustomerSelectedIntoConext(getWCContext());
		if (!isCustomerSelectedIntoContext && defaultShipToCustomerId != null
				&& defaultShipToCustomerId.trim().length() > 0) {
			XPEDXWCUtils.setCurrentCustomerIntoContext(defaultShipToCustomerId,
					getWCContext());
		}
		List<String> assignedShipTos = XPEDXWCUtils
				.getAllAssignedShiptoCustomerIdsForAUser(getWCContext()
						.getLoggedInUserId(), getWCContext());
		if (assignedShipTos == null) {
			assignedShipTos = new ArrayList<String>();
		}
		if (defaultShipToCustomerId != null) {
			assignedShipTos.remove(defaultShipToCustomerId);
		}
		assignedShipToList = new ArrayList<XPEDXShipToCustomer>();
		assignedShipToList = getShipToAdress(assignedShipTos,
				wcContext.getStorefrontId(), wcContext);
		/*
		 * --- Removing this as this calls for customer Details for each and
		 * every customer. using Complex Query and getting the customer
		 * information in one shot. if (assignedShipTos != null) { if (listSize
		 * > 0) { for (int index = 0; index < listSize; index++) {
		 * XPEDXShipToCustomer xPEDXShipToCustomer = XPEDXWCUtils
		 * .getShipToAdress(assignedShipTos.get(index),
		 * getWCContext().getStorefrontId()); String customerId =
		 * xPEDXShipToCustomer.getCustomerID(); xPEDXShipToCustomer
		 * .setHTMLValue(customerId.equals(getWCContext() .getCustomerId()) ?
		 * customerId : "");
		 * if(customerId.trim().equalsIgnoreCase(wcContext.getCustomerId()))
		 * currentCustomer = customerId;
		 * 
		 * assignedShipToList.add(xPEDXShipToCustomer); } } }
		 */}

	private void parseForShipToAddress(Element customerAssignmentViewElem,
			boolean isForDefaultShipto) {
		Element viewListElem = SCXmlUtil.getChildElement(
				customerAssignmentViewElem, "XPXCustomerAssignmentViewList");
		ArrayList<Element> assignedCustElems = SCXmlUtil.getElements(
				viewListElem, "XPXCustomerAssignmentView");
		assignedShipToList = new ArrayList<XPEDXShipToCustomer>();
		if (assignedCustElems.size() > 0) {
			
			shipToResult = false;
				String status ="";
			for (int i = 0; i < assignedCustElems.size(); i++) {
				Element customer = assignedCustElems.get(i);
				
				XPEDXShipToCustomer defualtShipToAssigned = new XPEDXShipToCustomer();
				defualtShipToAssigned.setCustomerID(SCXmlUtil.getAttribute(
						customer, "ShipToCustomerID"));
				log.debug(SCXmlUtil.getString(customer));
				
				
				defualtShipToAssigned.setOrganizationName(SCXmlUtil
						.getAttribute(customer, "ShipToCustomerName"));
				Element element = customer;
				defualtShipToAssigned.setFirstName(SCXmlUtil.getAttribute(
						element, "FirstName"));
				defualtShipToAssigned.setMiddleName(SCXmlUtil.getAttribute(
						element, "MiddleName"));
				log.debug("************MiddleName************="
						+ SCXmlUtil.getAttribute(element, "MiddleName"));
				defualtShipToAssigned.setLastName(SCXmlUtil.getAttribute(
						element, "LastName"));
				log.debug("************LastName************="
						+ SCXmlUtil.getAttribute(element, "LastName"));
				defualtShipToAssigned.setEMailID(SCXmlUtil.getAttribute(
						element, "EMailID"));
				defualtShipToAssigned.setEMailID(SCXmlUtil.getAttribute(
						element, "EMailID"));
				defualtShipToAssigned.setLocationID(SCXmlUtil.getAttribute(
						element, "ShipToExtnCustStoreNo"));
				defualtShipToAssigned.setCity(SCXmlUtil.getAttribute(element,
						"City"));
				defualtShipToAssigned.setState(SCXmlUtil.getAttribute(element,
						"State"));
				defualtShipToAssigned.setCountry(SCXmlUtil.getAttribute(
						element, "Country"));
				defualtShipToAssigned.setZipCode(SCXmlUtil.getAttribute(
						element, "ZipCode"));
				defualtShipToAssigned.setCompany(SCXmlUtil.getAttribute(
						element, "Company"));
				defualtShipToAssigned.setDayPhone(SCXmlUtil.getAttribute(
						element, "DayPhone"));

				List<String> addressList = new ArrayList<String>();
				for (int index = 0; index < 6; index++) {
					if (SCXmlUtil.getAttribute(element, "AddressLine" + index) != null
							&& SCXmlUtil
									.getAttribute(element,
											"AddressLine" + index).trim()
									.length() > 0) {
						addressList.add(SCXmlUtil.getAttribute(element,
								"AddressLine" + index));
					}
				}
				defualtShipToAssigned.setAddressList(addressList);
				String customerId = defualtShipToAssigned.getCustomerID();

				// Jira 1878 - for setting pre-select default ship to in
				// assigned customerList
				if (isForDefaultShipto) {
					defualtShipToAssigned.setHTMLValue(customerId
							.equals(defaultShipToCustomerId) ? customerId : "");
				} else {
					defualtShipToAssigned.setHTMLValue(customerId
							.equals(wcContext.getCustomerId()) ? customerId
							: "");
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

	// Jira 4146 -- Method added when Remove button is clicked
	public String removeAuthorize() {
		// removeFromavailable -- Opern =Create
		// addToavailable -- Opern = Delete
		// String customersForAuthorizedLoc = custID;
		// String[] arrayIds = customersForAuthorizedLoc.split(",");
		// List<String> columnList = new
		// ArrayList<String>(Arrays.asList(arrayIds));
		if (customerContactId == null || customerContactId.trim().length() == 0) {
			customerContactId = getWCContext().getLoggedInUserId();
		}
		// saveChanges(columnList,"Delete");
		// populateAvailableLocation();
		// saveChanges(columnList,"Create");
		String addToavailable1 = addToavailable;
		String[] addToavailablearrayIds = addToavailable1.split(",");

		List<String> newcustomersList = new ArrayList<String>(
				Arrays.asList(addToavailablearrayIds));

		ArrayList<Element> assignedCustomers = (ArrayList<Element>) XPEDXWCUtils
				.getObjectFromCache("AUTHORIZED_LOCATIONS");
		ArrayList<Element> availableCustomers = (ArrayList<Element>) XPEDXWCUtils
				.getObjectFromCache("AVAILABLE_LOCATIONS");
		ArrayList<Element> tempList = new ArrayList<Element>();
		for (String newcustomer : newcustomersList) {

			for (Element assignedCustomer : assignedCustomers) {
				String customerID = assignedCustomer.getAttribute("CustomerID");
				if (newcustomer != null
						&& newcustomer.equalsIgnoreCase(customerID)) {
					tempList.add(assignedCustomer);
					break;
				}
			}

		}
		for (Element assignedCustomer : tempList) {
			availableCustomers.add(assignedCustomer);
			assignedCustomers.remove(assignedCustomer);
		}
		setListSize(assignedCustomers.size());
		XPEDXWCUtils.setObectInCache("AUTHORIZED_LOCATIONS", assignedCustomers);
		XPEDXWCUtils.setObectInCache("AVAILABLE_LOCATIONS", availableCustomers);
		populateAvailableLocation();
		getSortedAssignedCustomer();
		/*
		 * // Set newcustomersList in session so that it is available while
		 * doing pagination - 4146 ArrayList<String>
		 * existingList=(ArrayList<String
		 * >)XPEDXWCUtils.getObjectFromCache("newcustomersList"); for(String
		 * customerId: newcustomersList) { existingList.remove(customerId); }
		 * XPEDXWCUtils.setObectInCache("newcustomersList",existingList);
		 * 
		 * Map<String, String> resultsMap1 = new HashMap<String, String>();
		 */

		/*
		 * String removeFromavailable1 = removeFromavailable; String[]
		 * removeFromavailableIds = removeFromavailable1.split(","); Map<String,
		 * String> resultsMap2 = new HashMap<String, String>();
		 * 
		 * if(addToavailable != null && addToavailable.trim().length() >0){ for
		 * (int i=0; i<addToavailablearrayIds.length; i++) {
		 * if(removeFromavailable != null && removeFromavailable.trim().length()
		 * >0 ){ if(!removeFromavailable.contains(addToavailablearrayIds[i]))
		 * resultsMap1.put(addToavailablearrayIds[i],"Delete" ); } //End of If
		 * block - removeFromavailable != null //else part -if
		 * removeFromavailable is null else
		 * resultsMap1.put(addToavailablearrayIds[i],"Delete" ); } }
		 * 
		 * if(removeFromavailable != null && removeFromavailable.trim().length()
		 * >0){ for (int i=0; i<removeFromavailableIds.length; i++) {
		 * if(addToavailable != null && addToavailable.trim().length() >0 ){
		 * if(!addToavailable.contains(removeFromavailableIds[i]))
		 * resultsMap1.put(removeFromavailableIds[i],"Create" ); }//End of If
		 * block - addToavailable != null //else part -if addToavailable is null
		 * else resultsMap1.put(removeFromavailableIds[i],"Create" ); } }
		 * 
		 * saveChanges(resultsMap1);
		 * 
		 * populateAvailableLocation();
		 * 
		 * if(addToavailable != null && addToavailable.trim().length() >0){ for
		 * (int i=0; i<addToavailablearrayIds.length; i++) {
		 * if(removeFromavailable != null && removeFromavailable.trim().length()
		 * >0 ){ if(!removeFromavailable.contains(addToavailablearrayIds[i]))
		 * resultsMap2.put(addToavailablearrayIds[i],"Create" ); } //End of If
		 * block - removeFromavailable != null //else part -if
		 * removeFromavailable is null else
		 * resultsMap2.put(addToavailablearrayIds[i],"Create" ); } }
		 * 
		 * if(removeFromavailable != null && removeFromavailable.trim().length()
		 * >0){ for (int i=0; i<removeFromavailableIds.length; i++) {
		 * if(addToavailable != null && addToavailable.trim().length() >0 ){
		 * if(!addToavailable.contains(removeFromavailableIds[i]))
		 * resultsMap2.put(removeFromavailableIds[i],"Delete" ); }//End of If
		 * block - addToavailable != null //else part -if addToavailable is null
		 * else resultsMap2.put(removeFromavailableIds[i],"Delete" ); } }
		 */
		/*
		 * if(addToavailable != null && addToavailable.trim().length() >0){ for
		 * (int i=0; i<addToavailablearrayIds.length; i++) {
		 * resultsMap2.put(addToavailablearrayIds[i],"Create" ); } }
		 * 
		 * if(removeFromavailable != null && removeFromavailable.trim().length()
		 * >0){ for (int i=0; i<removeFromavailableIds.length; i++) {
		 * resultsMap2.put(removeFromavailableIds[i],"Delete" ); } }
		 */
		// saveChanges(resultsMap2);

		return SUCCESS;
	}

	private void saveChanges(Map<String, String> resultsMap) {
		Set<String> keySet = resultsMap.keySet();
		Iterator<String> iter = keySet.iterator();
		List<String> cIds = new ArrayList<String>();
		while (iter.hasNext()) {
			String o = iter.next();
			cIds.add(o);
		}
		for (int index = 0; index < cIds.size(); index++) {
			try {
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("/CustomerAssignment/@CustomerID", cIds.get(index));
				valueMap.put("/CustomerAssignment/@OrganizationCode",
						getWCContext().getBuyerOrgCode());
				valueMap.put("/CustomerAssignment/@UserId", customerContactId);
				valueMap.put("/CustomerAssignment/@Operation",
						resultsMap.get(cIds.get(index)));
				Element input = WCMashupHelper.getMashupInput(
						"xpedxSaveCustomerAssignments", valueMap,
						wcContext.getSCUIContext());
				String inputXml = SCXmlUtil.getString(input);
				LOG.debug("Input XML: " + inputXml);
				Object obj = WCMashupHelper.invokeMashup(
						"xpedxSaveCustomerAssignments", input,
						wcContext.getSCUIContext());
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

	private void saveChanges(List<String> wList, String operation) {
		for (int index = 0; index < wList.size(); index++) {
			try {
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("/CustomerAssignment/@CustomerID",
						wList.get(index));
				valueMap.put("/CustomerAssignment/@OrganizationCode",
						getWCContext().getBuyerOrgCode());
				valueMap.put("/CustomerAssignment/@UserId", customerContactId);
				valueMap.put("/CustomerAssignment/@Operation", operation);
				Element input = WCMashupHelper.getMashupInput(
						"xpedxSaveCustomerAssignments", valueMap,
						wcContext.getSCUIContext());
				String inputXml = SCXmlUtil.getString(input);
				LOG.debug("Input XML: " + inputXml);
				Object obj = WCMashupHelper.invokeMashup(
						"xpedxSaveCustomerAssignments", input,
						wcContext.getSCUIContext());
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

				// Jira 1878 : Start : sending the customer contact id of
				// selected user instead of logged in user, for input xml
				selectedCustomerContactId = getWCContext().getSCUIContext()
						.getRequest().getParameter("selectedCustomerContactId");
				if (!(selectedCustomerContactId != null && selectedCustomerContactId
						.trim().length() > 0)) {
					selectedCustomerContactId = wcContext
							.getCustomerContactId();
				}
				// Jira 1878 : End

				String selectedCustomerID = getSelectedCurrentCustomer();
				if (SCUtil.isVoid(selectedCustomerID)) {
					selectedCustomerID = wcContext.getCustomerId();
				}

				// for JIRA 1494: when assigning a preferred ship-to for a new user, set the preferred category and category view based on the class/segment on the associated bill-to
				String initPrefCategory = null;
				Integer initPrefCategoryView = null;
				if ("true".equals(request.getParameter("initPrefs"))) {
					try {
						XPEDXShipToCustomer shipToCustomer = XPEDXWCUtils.getShipToAdress(selectedCustomerID, getWCContext().getStorefrontId());

						String billToId = XPEDXWCUtils.getParentCustomer(shipToCustomer.getCustomerID(), getWCContext());
						Document billToDetails = XPEDXWCUtils.getCustomerDetails(billToId, getWCContext().getStorefrontId());
						String billToClass = SCXmlUtil.getChildElement(billToDetails.getDocumentElement(), "Extn").getAttribute("ExtnCustomerClass");

						if (billToClass.equalsIgnoreCase("CJ")) {
							// facility supplies
							initPrefCategory = "300000";
							initPrefCategoryView = XPEDXConstants.XPEDX_B2B_FULL_VIEW;
						} else if (billToClass.equalsIgnoreCase("CG")) {
							// graphics
							initPrefCategory = "300001";
							initPrefCategoryView = XPEDXConstants.XPEDX_B2B_FULL_VIEW;
						} else if (billToClass.equalsIgnoreCase("CU")) {
							// packaging
							initPrefCategory = "300002";
							initPrefCategoryView = XPEDXConstants.XPEDX_B2B_FULL_VIEW;
						} else if (billToClass.equalsIgnoreCase("CA")) {
							// paper
							initPrefCategory = "300057";
							initPrefCategoryView = XPEDXConstants.XPEDX_B2B_PAPER_GRID_VIEW;
						}
					} catch (Exception e) {
						log.error("Failed to determine preferred category and preferred category view. Error message: " + e.getMessage());
						log.debug("", e);
					}
				}

				String inputXml = "<Customer "
						+ "CustomerID='" + XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext) + "' "
						+ "OrganizationCode='" + wcContext.getStorefrontId() + "'> "
						+ "<CustomerContactList>"
						+ "<CustomerContact CustomerContactID='" + selectedCustomerContactId + "'> "
						+ "<Extn ExtnDefaultShipTo='" + selectedCustomerID + "'";
				if (initPrefCategory != null) {
					inputXml += " ExtnPrefCatalog='" + initPrefCategory + "'";
				}
				if (initPrefCategoryView != null) {
					inputXml += " ExtnB2BCatalogView='" + initPrefCategoryView + "'";
				}
				inputXml += " /> " // close Extn tag
						+ "</CustomerContact> " + "</CustomerContactList>" + "</Customer> ";
				
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
		} catch (Exception ex) {
			log.debug(ex);
		} finally {
			SCUITransactionContextHelper.releaseTransactionContext(
					scuiTransactionContext, wSCUIContext);
			scuiTransactionContext = null;
			env = null;
		}
	}

	public void setCurrentCustomerIntoContext()
			throws CannotBuildInputException {

		String selectedCustomerContactId = getWCContext().getSCUIContext()
				.getRequest().getParameter("selectedCustomerContactId");
		// added for jira 4306
		XPEDXShipToCustomer shipToCustomer = null;
		try {
			shipToCustomer = (XPEDXShipToCustomer) ((XPEDXShipToCustomer) XPEDXWCUtils
					.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER))
					.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!setSelectedAsDefault) {
			XPEDXWCUtils.setObectInCache("DEFAULT_SHIP_TO_OBJECT",
					shipToCustomer.getDefaultShipToCustomer());
		}
		// end for jira 4306
		if (SCUtil.isVoid(selectedCustomerContactId))
			selectedCustomerContactId = wcContext.getCustomerContactId();
		String contaxtCustomerContactID = wcContext.getCustomerContactId();
		XPEDXWCUtils.setObectInCache(
				XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT, "true");
		resetOrganizationValuesForShipToCustomer();
		if (contaxtCustomerContactID.equals(selectedCustomerContactId)) {

			XPEDXWCUtils.setCurrentCustomerIntoContext(
					getSelectedCurrentCustomer(), getWCContext());
			if (isShipingAddressOverridden()) {
				getWCContext()
						.getSCUIContext()
						.getSession()
						.setAttribute(
								XPEDXWCUtils.XPEDX_SHIP_TO_ADDRESS_OVERIDDEN,
								xOverriddenShipToAddress);
			} else {
				getWCContext()
						.getSCUIContext()
						.getSession()
						.removeAttribute(
								XPEDXWCUtils.XPEDX_SHIP_TO_ADDRESS_OVERIDDEN);
			}
			XPEDXWCUtils.setObectInCache(XPEDXConstants.IS_SAP_STILL_NEED_TO_CHANGE, "Y");
			changeCurrentCartOwner();

		}
		// commenting for perofrmance issue this will be done in header action
		// again it is not required.
		// setSampleRequestFlagInSession();
		if (setSelectedAsDefault) {
			try {
				XPEDXWCUtils.setObectInCache(
						XPEDXConstants.DEFAULT_SHIP_TO_CHANGED, "true");
				setSelectedShipToAsDefault();
			} catch (Exception e) {
				log.error("Cannot set the customer as default. please try again later");
				e.printStackTrace();
			}
		}
	}

	public void resetOrganizationValuesForShipToCustomer() {
		XPEDXShipToCustomer shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils
				.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		if (null == shipToCustomer)
			shipToCustomer = new XPEDXShipToCustomer();
		shipToCustomer.setShipToOrgExtnMinOrderAmt(null);
		shipToCustomer.setShipToOrgExtnSmallOrderFee(null);
		shipToCustomer.setShipToOrgOrganizationName(null);
		shipToCustomer.setShipToOrgCorporatePersonInfoState(null);
		shipToCustomer.setShipToDivDeliveryCutOffTime(null);
		XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER,
				shipToCustomer);
	}

	// Added by - Jira 1700
	private void setSampleRequestFlagInSession()
			throws CannotBuildInputException {
		String billToCustomerId = XPEDXWCUtils.getParentCustomer(
				wcContext.getCustomerId(), wcContext);
		XPEDXWCUtils.setSampleRequestFlag(billToCustomerId, wcContext);
	}

	// Ends

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
			return isNotVoid;
		}
		return false;
	}

	private void changeCurrentCartOwner() {
		String orderHeaderKey = (String) XPEDXWCUtils
				.getObjectFromCache("OrderHeaderInContext");
		/*
		 * String orderHeaderKey = XPEDXCommerceContextHelper
		 * .getCartInContextOrderHeaderKey(getWCContext());
		 */
		if (orderHeaderKey != null) {
			log.debug("Changing the owner of the order");
			XPEDXWCUtils.changeOrderOwnerToSelectedCustomer(getWCContext(),
					orderHeaderKey);
			log.debug("Changed the cart owner");
		}
	}

	public static ArrayList<XPEDXShipToCustomer> getShipToAdress(
			List<String> customerIds, String organizationCode,
			IWCContext wcContext) throws CannotBuildInputException {
		if (customerIds == null || organizationCode == null
				|| !(customerIds.size() > 0)
				|| !(organizationCode.trim().length() > 0))
			return null;
		ArrayList<XPEDXShipToCustomer> assignedShipToCustomers = new ArrayList<XPEDXShipToCustomer>();
		Document document = XPEDXWCUtils.getCustomerDetails(customerIds,
				organizationCode, CUSTOMER_SHIPTO_INFORMATION_MASHUP);
		ArrayList<Element> customers = SCXmlUtil.getElements(
				document.getDocumentElement(), "Customer");
		if (customers.size() > 0) {
			for (int i = 0; i < customers.size(); i++) {
				Element customer = customers.get(i);
				XPEDXShipToCustomer defualtShipToAssigned = new XPEDXShipToCustomer();
				defualtShipToAssigned.setCustomerID(SCXmlUtil.getAttribute(
						customer, "CustomerID"));
				log.debug(SCXmlUtil.getString(customer));
				Element buyerOrgElement = SCXmlUtil.getChildElement(customer,
						"BuyerOrganization");
				defualtShipToAssigned.setOrganizationName(SCXmlUtil
						.getAttribute(buyerOrgElement, "OrganizationName"));
				Element element = SCXmlUtil
						.getElementByAttribute(
								customer,
								"CustomerAdditionalAddressList/CustomerAdditionalAddress",
								"IsDefaultShipTo", "Y");
				if (element == null) {
					element = SCXmlUtil.getChildElement(customer,
							"CustomerAdditionalAddressList");
					element = SCXmlUtil.getChildElement(element,
							"CustomerAdditionalAddress");
				}
				element = SCXmlUtil.getChildElement(element, "PersonInfo");
				defualtShipToAssigned.setFirstName(SCXmlUtil.getAttribute(
						element, "FirstName"));
				defualtShipToAssigned.setMiddleName(SCXmlUtil.getAttribute(
						element, "MiddleName"));
				log.debug("************MiddleName************="
						+ SCXmlUtil.getAttribute(element, "MiddleName"));
				defualtShipToAssigned.setLastName(SCXmlUtil.getAttribute(
						element, "LastName"));
				log.debug("************LastName************="
						+ SCXmlUtil.getAttribute(element, "LastName"));
				defualtShipToAssigned.setEMailID(SCXmlUtil.getAttribute(
						element, "EMailID"));
				defualtShipToAssigned.setEMailID(SCXmlUtil.getAttribute(
						element, "EMailID"));
				defualtShipToAssigned.setCity(SCXmlUtil.getAttribute(element,
						"City"));
				defualtShipToAssigned.setState(SCXmlUtil.getAttribute(element,
						"State"));
				defualtShipToAssigned.setCountry(SCXmlUtil.getAttribute(
						element, "Country"));
				defualtShipToAssigned.setZipCode(SCXmlUtil.getAttribute(
						element, "ZipCode"));
				defualtShipToAssigned.setCompany(SCXmlUtil.getAttribute(
						element, "Company"));
				defualtShipToAssigned.setDayPhone(SCXmlUtil.getAttribute(
						element, "DayPhone"));

				List<String> addressList = new ArrayList<String>();
				for (int index = 0; index < 6; index++) {
					if (SCXmlUtil.getAttribute(element, "AddressLine" + index) != null
							&& SCXmlUtil
									.getAttribute(element,
											"AddressLine" + index).trim()
									.length() > 0) {
						addressList.add(SCXmlUtil.getAttribute(element,
								"AddressLine" + index));
					}
				}
				defualtShipToAssigned.setAddressList(addressList);
				String customerId = defualtShipToAssigned.getCustomerID();
				defualtShipToAssigned.setHTMLValue(customerId.equals(wcContext
						.getCustomerId()) ? customerId : "");
				assignedShipToCustomers.add(defualtShipToAssigned);
			}
		}
		return assignedShipToCustomers;
	}

	public String searchShipToAddress() {
		if (customerContactId == null || customerContactId.trim().length() == 0) {
			customerContactId = getWCContext().getLoggedInUserId();
		}
		Map<String, Element> outputMap = new HashMap<String, Element>();
		String returnVal = null;
		if (!"Search Criteria".equalsIgnoreCase(searchTerm)) {
			try {
				setSearch(true);
				outputMap = prepareAndInvokeMashups();
				setDefaultShipTo();
				Element outputElem = outputMap
						.get("XPEDXSearchCustomerAssignmentsPaginated");
				Element customerAssignment = SCXmlUtil.getChildElement(
						outputElem, "Output");
				// Performance Fix - Removal of the mashup call of -
				// XPEDXGetPaginatedCustomerAssignments
				comingFromSearch = "true";
				assignedShipToCount = Integer.parseInt(SCXmlUtil
						.getChildElement(customerAssignment,
								"XPXCustomerAssignmentViewList").getAttribute(
								"TotalNumberOfRecords"));
				parsePageInfo(outputElem, true);
				parseForShipToAddress(customerAssignment, false);
			} catch (XMLExceptionWrapper e) {
				log.error("Error getting the Customer Assignments");
				e.printStackTrace();
			} catch (CannotBuildInputException e) {
				log.error("Error getting the Customer Assignments");
				e.printStackTrace();
			} catch (Exception e) {
				log.error("Error getting the Customer Assignments");
				e.printStackTrace();
			}
		} else {
			returnVal = getPaginatedAssignedCustomers();
		}
		if (returnVal != null)
			return returnVal;
		return SUCCESS;
		/*
		 * getAssignedCustomers(); ArrayList<String> searchStrings = new
		 * ArrayList<String>(); if(searchTerm.indexOf(",")!= -1) { String[]
		 * searchStrings1 = searchTerm.split(","); for(int
		 * i=0;i<searchStrings1.length;i++) {
		 * searchStrings.add(searchStrings1[i]); } }
		 * if(searchTerm.indexOf(".")!= -1) { String[] searchStrings2 =
		 * searchTerm.split("."); for(int i=0;i<searchStrings2.length;i++) {
		 * searchStrings.add(searchStrings2[i]); } } if(searchStrings.size()<=0)
		 * searchStrings.add(searchTerm); if(searchStrings.size()> 0) { for (int
		 * i=0; i<searchStrings.size(); i++) { for(int
		 * j=0;j<assignedShipToList.size();j++){
		 * if(assignedShipToList.get(j).toString
		 * ().toUpperCase().contains(searchStrings.get(i).trim().toUpperCase()))
		 * { if(addressSearchResult==null){ addressSearchResult = new
		 * LinkedHashSet<XPEDXShipToCustomer>(); }
		 * addressSearchResult.add(assignedShipToList.get(j)); } } } }
		 */
	}

	private void parsePageInfo(Element ele, boolean paginated) throws Exception {

		UtilBean util = new UtilBean();
		Element page = util.getElement(ele, "//Page");

		setIsLastPage(Boolean.FALSE);
		if ((paginated) && (page != null)) {
			setIsLastPage(SCXmlUtil.getBooleanAttribute(page, "IsLastPage",
					getIsLastPage()));
		}

		setIsFirstPage(Boolean.FALSE);
		if ((paginated) && (page != null)) {
			setIsFirstPage(SCXmlUtil.getBooleanAttribute(page, "IsFirstPage",
					getIsFirstPage()));
		}

		setIsValidPage(Boolean.FALSE);
		if ((paginated) && (page != null)) {
			setIsValidPage(SCXmlUtil.getBooleanAttribute(page, "IsValidPage",
					getIsValidPage()));
		}

		if ((paginated) && (page != null)) {
			setPageNumber(getIntegerAttribute(page, "PageNumber",
					getPageNumber()));
		}

		if ((paginated) && (page != null)) {
			setPageSetToken(page.getAttribute("PageSetToken"));
		}

		setTotalNumberOfPages(Integer.valueOf(0));
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

	private static final String CUSTOMER_SHIPTO_INFORMATION_MASHUP = "xpedx-customerlist-getCustomerAddressInformation";

	public String getDefaultShipToCustomerId() {
		return defaultShipToCustomerId;
	}

	public void setDefaultShipToCustomerId(String defaultShipToCustomerId) {
		this.defaultShipToCustomerId = defaultShipToCustomerId;
	}

	public String getCurrentCustomer() {
		if (currentCustomer != null && currentCustomer.trim().length() <= 0)
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

	public void setAlreadySelectedCustomers(
			List<String> alreadySelectedCustomers) {
		this.alreadySelectedCustomers = alreadySelectedCustomers;
	}

	// Performance Fix - Removal of the mashup call of -
	// XPEDXGetPaginatedCustomerAssignments
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

	public Map<String, String> getAvailableLocationMap() {
		return availableLocationMap;
	}

	public void setAvailableLocationMap(
			LinkedHashMap<String, String> availableLocationMap) {
		this.availableLocationMap = availableLocationMap;
	}

	public Map<String, String> getAuthorizedLocationMap() {
		return authorizedLocationMap;
	}

	public void setAuthorizedLocationMap(
			LinkedHashMap<String, String> authorizedLocationMap) {
		this.authorizedLocationMap = authorizedLocationMap;
	}

}

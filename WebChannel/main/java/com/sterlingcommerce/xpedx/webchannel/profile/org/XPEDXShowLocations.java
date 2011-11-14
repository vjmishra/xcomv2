package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.util.ArrayUtil;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

@SuppressWarnings("serial")
public class XPEDXShowLocations extends WCAction {
	protected String shownCustomerId;
	protected String organizationCode;
	protected Document shownCustomerDoc;
	protected Document allChildCustomersDoc;
	protected ArrayList<Element> childCustomers = new ArrayList<Element>();
	protected Map<String,String> displayChildCustomersMap = new HashMap<String, String>();
	ArrayList<Element> billToCustomers = new ArrayList<Element>();
	protected Map<String,ArrayList<String>> msapAndSapCustomersMap = new HashMap<String, ArrayList<String>>();
	protected Map<String,ArrayList<String>> sapAndBillToCustomersMap = new HashMap<String, ArrayList<String>>();
	protected Map<String,ArrayList<String>> billToAndShipToCustomersMap = new HashMap<String, ArrayList<String>>();
	private final static Logger log = Logger.getLogger(XPEDXShowLocations.class);
	String shownCustomerSuffixType;
	protected String mSapName;
	protected String buyrOrgName;
	
	public String execute() {
		if(!(shownCustomerId!=null && shownCustomerId.trim().length()>0)) { // if the shownCustomerID is not passed taking cusotmerId from the context
			if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext)) { // checking if a ship to is selected
				shownCustomerId = wcContext.getCustomerId();// if the shownCustomerId is not passed taking the selected ship to
			}
			else {	// no ship to is selected, so selecting the master customer and displaying all the child customers under it
				shownCustomerId = wcContext.getCustomerId();
			}
		}
		if(!(organizationCode!=null && organizationCode.trim().length()>0)) { // if the organization code is not passed taking org code from the context
			organizationCode = wcContext.getStorefrontId();
		}
		try {
			shownCustomerDoc = XPEDXWCUtils.getCustomerDetails(shownCustomerId, organizationCode, XPEDXConstants.MASHUP_SHOW_LOCATIONS);
			Element custElem = shownCustomerDoc.getDocumentElement();
			Element ExtnElem = SCXmlUtil.getChildElement(custElem, "Extn");
			shownCustomerSuffixType = SCXmlUtil.getAttribute(ExtnElem, "ExtnSuffixType");
			
			//added for jira 2769
			Element buyrOrgElement = SCXmlUtil.getChildElement(custElem, "BuyerOrganization");
			buyrOrgName=SCXmlUtil.getAttribute(buyrOrgElement,"OrganizationName");


			
			if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.MASTER_CUSTOMER_SUFFIX_TYPE)) {
				//shown customer is Master customer. This is not a scenario at all, but if in case, the shown customer is MSAP, we will only show him SAP
				String customerID = custElem.getAttribute("CustomerID");
				String customerKey = custElem.getAttribute("CustomerKey");
				allChildCustomersDoc = getAllChildCustomers(customerKey,customerID);
				//getting only the SAP customers
				childCustomers = SCXmlUtil.getElementsByAttribute(
						allChildCustomersDoc.getDocumentElement(),
						"Organization", "CustomerSuffixType",
						XPEDXConstants.SAP_CUSTOMER_SUFFIX_TYPE);
				prepareHierachyMapsForMSAP();
				setAllChildCustomerInfoDisplayFormat();
			}
			else if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.SAP_CUSTOMER_SUFFIX_TYPE)) {
				//shown customer is a sap customer. So we have to show all the billto's and shipto's under the sap customer
				String customerID = custElem.getAttribute("CustomerID");
				String customerKey = custElem.getAttribute("CustomerKey");
				allChildCustomersDoc = getAllChildCustomers(customerKey,customerID);
				if(allChildCustomersDoc!=null) {
					childCustomers = SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");
				}
				setChildsHierarchyMap();
				setAllChildCustomerInfoDisplayFormat();
			}
			else if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE)) {
				// shown customer is Bill to. We have to show all the billto's and ship to's under the parent of bill to(SAP) except the shown bill to customer
				//getting the parent customer. i.e., SAP and getting all the child's of SAP
				Element parentCustomer = SCXmlUtil.getChildElement(custElem, "ParentCustomer");
				String customerID = parentCustomer.getAttribute("CustomerID");
				String customerKey = parentCustomer.getAttribute("CustomerKey");
				allChildCustomersDoc = getAllChildCustomers(customerKey,customerID);
				if(allChildCustomersDoc!=null) {
					childCustomers = SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");
//					removeShownCustomer(); Instead of removing, now we are preselecting the customer
				}
				setChildsHierarchyMap();
				setAllChildCustomerInfoDisplayFormat();
			}
			else if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.SHIP_TO_CUSTOMER_SUFFIX_TYPE)) {
				// shown customer is Ship to. We have to show all the billto's and ship to's under the parent of bill to(SAP) except the shown bill to customer
				Element parentCustomer1 = SCXmlUtil.getChildElement(custElem, "ParentCustomer");// getting the bill to customer
				Element parentCustomer2= SCXmlUtil.getChildElement(parentCustomer1, "ParentCustomer");// getting the ship to customer
				String customerID = parentCustomer2.getAttribute("CustomerID");
				String customerKey = parentCustomer2.getAttribute("CustomerKey");
				allChildCustomersDoc = getAllChildCustomers(customerKey,customerID);
				if(allChildCustomersDoc!=null) {
					childCustomers = SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");
//					Changed as part of new UI that came
//					removeShownCustomer(); Instead of removing, now we are preselecting the customer
				}
				setChildsHierarchyMap();
				setAllChildCustomerInfoDisplayFormat();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	private void setAllChildCustomerInfoDisplayFormat() {
		ArrayList<String> allChildCustomerIds = new ArrayList<String>();
		ArrayList<String> sapCustomersIds = new ArrayList<String>();
		childCustomers = SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");
		Iterator<Element> iterator = childCustomers.iterator();
		while(iterator.hasNext()) {
			Element childOrgElem = iterator.next();
			String childCustomerID = childOrgElem.getAttribute("CustomerID");
			if(!allChildCustomerIds.contains(childCustomerID)) {
				allChildCustomerIds.add(childCustomerID);
			}
		}
		ArrayList<Element> allBillTos = SCXmlUtil.getElementsByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization","CustomerSuffixType",XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE);
		Iterator<Element> allBillTosIterator = allBillTos.iterator();
		while(allBillTosIterator.hasNext()) {
			Element childBillToOrgElem = allBillTosIterator.next();
			String sapCustomerID = childBillToOrgElem.getAttribute("ParentCustomerID");
			if(!sapCustomersIds.contains(sapCustomerID)) {
				sapCustomersIds.add(sapCustomerID);
			}
		}
		allChildCustomerIds.addAll(sapCustomersIds);
		if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext))
			allChildCustomerIds.add(XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext));
		else
			allChildCustomerIds.add(wcContext.getCustomerId());
		displayChildCustomersMap = XPEDXWCUtils.custFullAddresses(allChildCustomerIds, organizationCode);
		// TODO Auto-generated method stub		
	}

	private Document getAllChildCustomers(String customerKey, String customerID) throws CannotBuildInputException {
		Document allAssignedCustomerDoc = null;

		if (null == customerID && null == customerKey) {
			log.debug("getAllChildCustomersDoc: customerID is a required field. Returning a empty Document");
			return allAssignedCustomerDoc;
		}

		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Customer/@CustomerID", customerID);
		valueMap.put("/Customer/@OrganizationCode", context.getStorefrontId());
		valueMap.put("/Customer/@CustomerKey", customerKey);

		Element input = WCMashupHelper.getMashupInput(
				"xpedxGetAllChilds", valueMap, wSCUIContext);
		String inputXml = SCXmlUtil.getString(input);
		log.debug("xpedxGetAllChilds: Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxGetAllChilds",
				input, wSCUIContext);
		allAssignedCustomerDoc = ((Element) obj).getOwnerDocument();
		if (null != allAssignedCustomerDoc) {
			log.debug("xpedxGetAllChilds: Output XML: "
					+ SCXmlUtil.getString((Element) obj));
		}
		return allAssignedCustomerDoc;
	}
	
	private void removeShownCustomer() {
		childCustomers = SCXmlUtil.getElements(allChildCustomersDoc.getDocumentElement(), "Organization");
		Iterator<Element> iterator = childCustomers.iterator();
		while(iterator.hasNext()) {
			Element childOrgElem = iterator.next();
			String childCustomerID = childOrgElem.getAttribute("CustomerID");
			if(childCustomerID.equalsIgnoreCase(shownCustomerId)) {
				iterator.remove();
				break;
			}
		}
	}
	
	private void setChildsHierarchyMap() {
		if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.MASTER_CUSTOMER_SUFFIX_TYPE)) {
			prepareHierachyMapsForMSAP();
		}
		if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.SAP_CUSTOMER_SUFFIX_TYPE)) {
			billToCustomers = SCXmlUtil.getElementsByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization", "ParentCustomerID", shownCustomerId);
			prepareHierachyMaps();
			ArrayList<String> sapCustomerIDs = new ArrayList<String>();
			sapCustomerIDs.add(shownCustomerId);
			if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext))
				msapAndSapCustomersMap.put(XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext), sapCustomerIDs);
			else
				msapAndSapCustomersMap.put(wcContext.getCustomerId(), sapCustomerIDs);
		}
		if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE)) {
			Element billToCusotmerElem = SCXmlUtil.getElementByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization", "CustomerID", shownCustomerId);
			String SAPCustomerID = billToCusotmerElem.getAttribute("ParentCustomerID");
			billToCustomers = SCXmlUtil.getElementsByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization", "ParentCustomerID", SAPCustomerID);
			prepareHierachyMaps();
			ArrayList<String> sapCustomerIDs = new ArrayList<String>();
			sapCustomerIDs.add(SAPCustomerID);
			if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext))
				msapAndSapCustomersMap.put(XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext), sapCustomerIDs);
			else
				msapAndSapCustomersMap.put(wcContext.getCustomerId(), sapCustomerIDs);
		}
		if(shownCustomerSuffixType.trim().equalsIgnoreCase(XPEDXConstants.SHIP_TO_CUSTOMER_SUFFIX_TYPE)) {
			Element shipToCusotmerElem = SCXmlUtil.getElementByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization", "CustomerID", shownCustomerId);
			String billToCustomerID = shipToCusotmerElem.getAttribute("ParentCustomerID");
			Element billToCusotmerElem = SCXmlUtil.getElementByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization", "CustomerID", billToCustomerID);
			String SAPCustomerID = billToCusotmerElem.getAttribute("ParentCustomerID");
			billToCustomers = SCXmlUtil.getElementsByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization", "ParentCustomerID", SAPCustomerID);
			prepareHierachyMaps();
			ArrayList<String> sapCustomerIDs = new ArrayList<String>();
			sapCustomerIDs.add(SAPCustomerID);
			if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext))
				msapAndSapCustomersMap.put(XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext), sapCustomerIDs);
			else
				msapAndSapCustomersMap.put(wcContext.getCustomerId(), sapCustomerIDs);
		}
	}
	
	private void prepareHierachyMaps() {
		ArrayList<String> billToCustomersIDs = new ArrayList<String>();
		String sapCustomerID = null;
		if(billToCustomers!=null && billToCustomers.size()>0) {
			Iterator<Element> iterator = billToCustomers.iterator();
			while(iterator.hasNext()) {
				Element billToCustomerElem = iterator.next();
				String billToCustomerID = billToCustomerElem.getAttribute("CustomerID");
				sapCustomerID = billToCustomerElem.getAttribute("ParentCustomerID");
				if(!(billToCustomersIDs.contains(billToCustomerID))) {
					billToCustomersIDs.add(billToCustomerID);
				}
				ArrayList<Element> shipToCustomers = SCXmlUtil.getElementsByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization", "ParentCustomerID", billToCustomerID);
				Iterator<Element> shipToIterator = shipToCustomers.iterator();
				ArrayList<String> shipToCustomersIDs = new ArrayList<String>();
				while(shipToIterator.hasNext()) {
					Element shipToElement = shipToIterator.next();
					String shipToCustomerID = shipToElement.getAttribute("CustomerID");
					if(!(shipToCustomersIDs.contains(shipToCustomerID))) {
						shipToCustomersIDs.add(shipToCustomerID);
					}
				}
				billToAndShipToCustomersMap.put(billToCustomerID, shipToCustomersIDs);
			}
		}
		sapAndBillToCustomersMap.put(sapCustomerID, billToCustomersIDs);
	}
	
	private void prepareHierachyMapsForMSAP() {
		//Came here as the shown customer is MSAP customer
		ArrayList<String> sapCustomersIDs = new ArrayList<String>();
		ArrayList<Element> sapCustomers = SCXmlUtil.getElementsByAttribute(allChildCustomersDoc.getDocumentElement(),"Organization","ParentCustomerID",shownCustomerId);
		Iterator<Element> sapCustomersIterator = sapCustomers.iterator();
		while(sapCustomersIterator.hasNext()) {
			Element sapCustomerElement = sapCustomersIterator.next();
			String sapCustomerID = sapCustomerElement.getAttribute("CustomerID");
			billToCustomers = SCXmlUtil.getElementsByAttribute(allChildCustomersDoc.getDocumentElement(), "Organization", "ParentCustomerID",sapCustomerID);
			prepareHierachyMaps();
			if(!(sapCustomersIDs.contains(sapCustomerID))) {
				sapCustomersIDs.add(sapCustomerID);
			}
		}
		msapAndSapCustomersMap.put(shownCustomerId, sapCustomersIDs);		
	}

	public String getShownCustomerId() {
		return shownCustomerId;
	}

	public void setShownCustomerId(String shownCustomerId) {
		this.shownCustomerId = shownCustomerId;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public Document getShownCustomerDoc() {
		return shownCustomerDoc;
	}

	public void setShownCustomerDoc(Document shownCustomerDoc) {
		this.shownCustomerDoc = shownCustomerDoc;
	}

	public Document getAllChildCustomersDoc() {
		return allChildCustomersDoc;
	}

	public void setAllChildCustomersDoc(Document allChildCustomersDoc) {
		this.allChildCustomersDoc = allChildCustomersDoc;
	}

	public ArrayList<Element> getChildCustomers() {
		return childCustomers;
	}

	public void setChildCustomers(ArrayList<Element> childCustomers) {
		this.childCustomers = childCustomers;
	}

	public Map<String, String> getDisplayChildCustomersMap() {
		return displayChildCustomersMap;
	}

	public void setDisplayChildCustomersMap(
			Map<String, String> displayChildCustomersMap) {
		this.displayChildCustomersMap = displayChildCustomersMap;
	}

	public Map<String, ArrayList<String>> getMsapAndSapCustomersMap() {
		return msapAndSapCustomersMap;
	}

	public void setMsapAndSapCustomersMap(
			Map<String, ArrayList<String>> msapAndSapCustomersMap) {
		this.msapAndSapCustomersMap = msapAndSapCustomersMap;
	}

	public Map<String, ArrayList<String>> getSapAndBillToCustomersMap() {
		return sapAndBillToCustomersMap;
	}

	public void setSapAndBillToCustomersMap(
			Map<String, ArrayList<String>> sapAndBillToCustomersMap) {
		this.sapAndBillToCustomersMap = sapAndBillToCustomersMap;
	}

	public Map<String, ArrayList<String>> getBillToAndShipToCustomersMap() {
		return billToAndShipToCustomersMap;
	}

	public void setBillToAndShipToCustomersMap(
			Map<String, ArrayList<String>> billToAndShipToCustomersMap) {
		this.billToAndShipToCustomersMap = billToAndShipToCustomersMap;
	}
	
	public String getmSapName() {
		return mSapName;
	}

	public void setmSapName(String mSapName) {
		this.mSapName = mSapName;
	}
	
	public String getBuyrOrgName() {
		return buyrOrgName;
	}

	public void setBuyrOrgName(String buyrOrgName) {
		this.buyrOrgName = buyrOrgName;
	}


}

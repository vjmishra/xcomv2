package com.sterlingcommerce.xpedx.webchannel.profile.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
// import java.util.HashMap;
// import org.apache.struts2.ServletActionContext;
// import org.w3c.dom.Document;
// import org.w3c.dom.NodeList;
// import com.sterlingcommerce.baseutil.SCXmlUtil;
// import com.sterlingcommerce.webchannel.core.IWCContext;
// import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
// import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
// import com.yantra.yfc.dom.YFCDocument;
// import com.yantra.yfc.dom.YFCNode;
// import com.yantra.yfc.dom.YFCNodeList;

public class XPEDXAssignedCustomersLocationsDisplayAction extends
		WCMashupAction {

	private static final long serialVersionUID = 7861772398942028854L;
	// private String defaultShipTo; // ship to is the default ship to customer
	// id
	// private XPEDXShipToCustomer shipToAddress;
	// private Element customerAssignmentElem;
	private final static Logger log = Logger
			.getLogger(XPEDXAssignedCustomersLocationsDisplayAction.class);
	// private String billToParentCustomerKey;
	// private List<String> assignedCustomerIDs;
	// private Document customerHierachyDoc;
	// private YFCNodeList<YFCNode> customerAssignmentList;
	private Set<String> mapKeys;
	private List<String> billToList = new ArrayList<String>();
	private Map<String, ArrayList<Element>> assignedCustomersMap;
	private String loggedInUserId;
	private boolean locationsLoaded;
	private String billToListText = null;

	// private static final String PARENT_CUSTOMER_INFORMATION_MASHUP =
	// "xpedx-customer-getParentCustInformation";

	@Override
	public String execute() {
		/*
		 * Picking the default shipTo from the user profile and getting the ship
		 * to Address directly from the jsp This change is done for performance
		 * increase and user profile clean up
		 */
		// getDefaultShipToAddress();
		// getCustomerAssignmentsAsHierarchy();
		getCustomerAssignments();
		// if (customerHierachyDoc != null
		// && YFCDocument.getDocumentFor(customerHierachyDoc) != null) {
		// customerAssignmentList = YFCDocument.getDocumentFor(
		// customerHierachyDoc).getChildNodes();
		// }
		return SUCCESS;
	}

	/*
	 * private void getDefaultShipToAddress() { defaultShipTo =
	 * XPEDXWCUtils.getDefaultShipTo(); if (defaultShipTo != null) { IWCContext
	 * wcContext = WCContextHelper
	 * .getWCContext(ServletActionContext.getRequest()); try { shipToAddress =
	 * XPEDXWCUtils.getShipToAdress(defaultShipTo, wcContext.getBuyerOrgCode());
	 * } catch (Exception e) { LOG.debug(e.getMessage());
	 * log.debug("Error while getting the ship to Address"); } } else {
	 * log.debug("No Default Ship To is selected"); } // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 */

	private void getCustomerAssignments() {
		try {
			assignedCustomersMap = XPEDXWCUtils
					.getAssignedCustomerElementHashMap(XPEDXWCUtils
							.getLoggedInCustomerFromSession(wcContext),
							loggedInUserId);
			if (assignedCustomersMap != null && !assignedCustomersMap.isEmpty()) {
				setLocationsLoaded(true);
				mapKeys = assignedCustomersMap.keySet();
				billToList.addAll(mapKeys);
			}

			if (billToList != null) {
				for (int i = 0; i < billToList.size(); i++) {
					if (billToListText != null) {
						billToListText = billToListText + ","
								+ billToList.get(i);
					} else {
						billToListText = billToList.get(i) + ",";
					}
				}
			}
			// customerAssignmentElem =
			// prepareAndInvokeMashup("xpedx-getCustomerAssignments");

		} catch (Exception ex) {
			setLocationsLoaded(false);
			log.debug("Error getting Customer Assignment " + ex.getMessage());
		}
	}

	/**
	 * JIRA 243 Modified getCustomerDetails method to consider the mashup to be
	 * invoked so that, we get only the required information - here
	 * ParentInformation.
	 * 
	 * @param inputItems
	 * @return
	 */
	/*
	 * private void getCustomerAssignmentsAsHierarchy() { assignedCustomerIDs =
	 * XPEDXWCUtils.getAssignedCustomers(wcContext .getCustomerContactId());
	 * 
	 * // this list contains all the customer ID's of the assigned customers //
	 * for this user try { customerAssignmentElem =
	 * prepareAndInvokeMashup("xpedx-getCustomerAssignments"); // getting all
	 * the bill to and ship to hierarchy String billToCustomerId = XPEDXWCUtils
	 * .getLoggedInCustomerFromSession(wcContext); String customerId =
	 * (billToCustomerId != null && !billToCustomerId .trim().isEmpty()) ?
	 * billToCustomerId : wcContext .getCustomerId(); Element customerEle =
	 * XPEDXWCUtils.getCustomerDetails(customerId, wcContext.getStorefrontId(),
	 * PARENT_CUSTOMER_INFORMATION_MASHUP).getDocumentElement();
	 * billToParentCustomerKey = SCXmlUtil.getXpathAttribute(customerEle,
	 * "/Customer/ParentCustomer/@CustomerKey"); Element outputDoc =
	 * prepareAndInvokeMashup("XPEDXBillTOAndShipToHierarchyMashup"); NodeList
	 * billToNL = SCXmlUtil.getXpathNodes(outputDoc, "/BillToList/BillTo");
	 * prepareHierarchyToDisplay(billToNL); } catch (Exception e) {
	 * log.debug("Error while getting the Bill to and Ship to Hierarchy ... " +
	 * e.getMessage()); }
	 * 
	 * }
	 * 
	 * private void prepareHierarchyToDisplay(NodeList billToNL) {
	 * customerHierachyDoc = YFCDocument.createDocument("CustomerAssignment")
	 * .getDocument(); Element customerHierachyElem =
	 * customerHierachyDoc.getDocumentElement(); for (int i = 0; i <
	 * billToNL.getLength(); i++) { Element billToElem = (Element)
	 * billToNL.item(i); String billToID = billToElem.getAttribute("BillToID");
	 * if (assignedCustomerIDs.contains(billToID)) { Element billToCustElem =
	 * SCXmlUtil.getElementByAttribute( customerAssignmentElem,
	 * "CustomerAssignment/Customer", "CustomerID", billToID); Element
	 * assignedbillToElem = customerHierachyDoc
	 * .createElement("BillToCustomer"); if (assignedbillToElem != null) {
	 * assignedbillToElem.setAttribute("BillToID", billToID);
	 * assignedbillToElem.appendChild(billToCustElem);
	 * customerHierachyElem.appendChild(assignedbillToElem); }
	 * 
	 * NodeList shipToListOfBillTO = SCXmlUtil.getXpathNodes( billToElem,
	 * "ShipToList/ShipTo"); Element assignedbillToShipToElem =
	 * customerHierachyDoc .createElement("ShipToList"); List<String>
	 * shipToIDsList = new ArrayList<String>(); for (int j = 0; j <
	 * shipToListOfBillTO.getLength(); j++) { Element shipToElem = (Element)
	 * shipToListOfBillTO.item(j); String shipToID =
	 * shipToElem.getAttribute("ShipToID"); if
	 * (assignedCustomerIDs.contains(shipToID)) { if (shipToIDsList.isEmpty()) {
	 * shipToIDsList.add(shipToID); Element ShipToCustElem = SCXmlUtil
	 * .getElementByAttribute( customerAssignmentElem,
	 * "CustomerAssignment/Customer", "CustomerID", shipToID);
	 * assignedbillToShipToElem .appendChild(ShipToCustElem); assignedbillToElem
	 * .appendChild(assignedbillToShipToElem); } else { Element ShipToCustElem =
	 * SCXmlUtil .getElementByAttribute( customerAssignmentElem,
	 * "CustomerAssignment/Customer", "CustomerID", shipToID);
	 * assignedbillToShipToElem .appendChild(ShipToCustElem); assignedbillToElem
	 * .appendChild(assignedbillToShipToElem); } } } if (assignedbillToElem !=
	 * null) { customerHierachyElem.appendChild(assignedbillToElem); } } else {
	 * Element assignedbillToElem = customerHierachyDoc
	 * .createElement("BillToCustomer"); NodeList shipToListOfBillTO =
	 * SCXmlUtil.getXpathNodes( billToElem, "ShipToList/ShipTo"); Element
	 * assignedbillToShipToElem = customerHierachyDoc
	 * .createElement("ShipToList"); List<String> shipToIDsList = new
	 * ArrayList<String>(); for (int j = 0; j < shipToListOfBillTO.getLength();
	 * j++) { Element shipToElem = (Element) shipToListOfBillTO.item(j); String
	 * shipToID = shipToElem.getAttribute("ShipToID"); if
	 * (assignedCustomerIDs.contains(shipToID.trim())) { if
	 * (shipToIDsList.isEmpty()) { shipToIDsList.add(shipToID); Element
	 * billToCustElem = null; try { billToCustElem = XPEDXWCUtils
	 * .getCustomerDetails(billToID, wcContext.getStorefrontId())
	 * .getDocumentElement(); } catch (Exception e) {
	 * log.debug("Error while getting the Bill To customer Info " +
	 * e.getMessage()); } assignedbillToElem.setAttribute("BillToID",
	 * billToID);// if bill to is not present in // customer assignment list
	 * Element billToofShipToElem = customerHierachyDoc
	 * .createElement("Customer"); billToofShipToElem .setAttribute(
	 * "ExtnCustomerName", SCXmlUtil .getXpathAttribute( billToCustElem,
	 * "/Customer/Extn/@ExtnCustomerName"));
	 * assignedbillToElem.appendChild(billToofShipToElem); Element
	 * ShipToCustElem = SCXmlUtil .getElementByAttribute(
	 * customerAssignmentElem, "CustomerAssignment/Customer", "CustomerID",
	 * shipToID); Element hierarchyShipToElem = customerHierachyDoc
	 * .createElement("Customer"); hierarchyShipToElem .setAttribute(
	 * "ExtnCustomerName", SCXmlUtil .getXpathAttribute( ShipToCustElem,
	 * "/Customer/Extn/@ExtnCustomerName")); assignedbillToShipToElem
	 * .appendChild(hierarchyShipToElem); assignedbillToElem
	 * .appendChild(assignedbillToShipToElem); } else {
	 * shipToIDsList.add(shipToID); Element ShipToCustElem = SCXmlUtil
	 * .getElementByAttribute( customerAssignmentElem,
	 * "CustomerAssignment/Customer", "CustomerID", shipToID); if
	 * (ShipToCustElem == null) { try { ShipToCustElem = XPEDXWCUtils
	 * .getCustomerDetails(shipToID, wcContext.getStorefrontId())
	 * .getDocumentElement(); } catch (Exception e) {
	 * log.debug("Error while getting the Ship To customer Info " +
	 * e.getMessage()); } } Element hierarchyShipToElem = customerHierachyDoc
	 * .createElement("Customer"); hierarchyShipToElem.setAttribute(
	 * "ExtnCustomerName", SCXmlUtil .getXpathAttribute(ShipToCustElem,
	 * "Extn/@ExtnCustomerName")); assignedbillToShipToElem
	 * .appendChild(hierarchyShipToElem); assignedbillToElem
	 * .appendChild(assignedbillToShipToElem);
	 * 
	 * } } } if (assignedbillToElem != null) {
	 * customerHierachyElem.appendChild(assignedbillToElem); } } }
	 * 
	 * }
	 */

	// TODO Auto-generated method stub

	/*
	 * public String getDefaultShipTo() { return defaultShipTo; }
	 */

	/*
	 * public void setDefaultShipTo(String defaultShipTo) { this.defaultShipTo =
	 * defaultShipTo; }
	 */

	/*
	 * public XPEDXShipToCustomer getShipToAddress() { return shipToAddress; }
	 * 
	 * public void setShipToAddress(XPEDXShipToCustomer shipToAddress) {
	 * this.shipToAddress = shipToAddress; }
	 * 
	 * public String getBillToParentCustomerKey() { return
	 * billToParentCustomerKey; }
	 * 
	 * public void setBillToParentCustomerKey(String billToParentCustomerKey) {
	 * this.billToParentCustomerKey = billToParentCustomerKey; }
	 * 
	 * public Document getCustomerHierachyDoc() { return customerHierachyDoc; }
	 * 
	 * public void setCustomerHierachyDoc(Document customerHierachyDoc) {
	 * this.customerHierachyDoc = customerHierachyDoc; }
	 * 
	 * public Element convertIntoElement(Object wNode) { return (Element) wNode;
	 * }
	 */

	/*
	 * public YFCNodeList<YFCNode> getCustomerAssignmentList() { final int
	 * FIRST_CHILD = 0; if (customerAssignmentList != null &&
	 * customerAssignmentList.item(FIRST_CHILD) != null) { return
	 * customerAssignmentList.item(FIRST_CHILD).getChildNodes(); } return null;
	 * }
	 * 
	 * public void setCustomerAssignmentList( YFCNodeList<YFCNode>
	 * customerAssignmentList) { this.customerAssignmentList =
	 * customerAssignmentList; }
	 */

	public List<String> getBillToList() {
		return billToList;
	}

	public void setBillToList(List<String> billToList) {
		this.billToList = billToList;
	}

	public Map<String, ArrayList<Element>> getAssignedCustomersMap() {
		return assignedCustomersMap;
	}

	public void setAssignedCustomersMap(
			Map<String, ArrayList<Element>> assignedCustomersMap) {
		this.assignedCustomersMap = assignedCustomersMap;
	}

	public String getLoggedInUserId() {
		return loggedInUserId;
	}

	public void setLoggedInUserId(String loggedInUserId) {
		this.loggedInUserId = loggedInUserId;
	}

	public boolean isLocationsLoaded() {
		return locationsLoaded;
	}

	public void setLocationsLoaded(boolean locationsLoaded) {
		this.locationsLoaded = locationsLoaded;
	}

	public String getBillToListText() {
		return billToListText;
	}

	public void setBillToListText(String billToListText) {
		this.billToListText = billToListText;
	}

}

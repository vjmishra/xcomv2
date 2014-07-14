package com.sterlingcommerce.xpedx.webchannel.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.tools.datavalidator.XmlUtils;
import com.sterlingcommerce.webchannel.order.OrderDetailAction;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

@SuppressWarnings("serial")
public class XPEDXReturnItemsRequestAction extends OrderDetailAction {
	
	private static final String customerExtnMashUp= "xpedx-customer-getCustomerAllExtnInformation";
	
	private String returnsph;
	private String returnsEmail;
	private List<String> returnsqty;
	private List<String> reasonreturn;
	private List<String> othertext;
	private String notes;
	private String messageType="returnItemsRequest";

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<String> getOthertext() {
		return othertext;
	}

	public void setOthertext(List<String> othertext) {
		this.othertext = othertext;
	}

	public List<String> getReasonreturn() {
		return reasonreturn;
	}

	public void setReasonreturn(List<String> reasonreturn) {
		this.reasonreturn = reasonreturn;
	}

	public List<String> getReturnsqty() {
		return returnsqty;
	}

	public void setReturnsqty(List<String> returnsqty) {
		this.returnsqty = returnsqty;
	}

	public String getReturnsEmail() {
		return returnsEmail;
	}

	public void setReturnsEmail(String returnsEmail) {
		this.returnsEmail = returnsEmail;
	}

	public String getReturnsph() {
		return returnsph;
	}

	public void setReturnsph(String returnsph) {
		this.returnsph = returnsph;
	}

	public String execute() {
		String returnString = super.execute();
		Element eleUserInfo = XPEDXWCUtils.getUserInfo(getWCContext()
				.getCustomerContactId(), getWCContext().getStorefrontId());
		setUserName(getWCContext().getLoggedInUserName());
		setRequestedBy(getUserDetails(eleUserInfo));
		getCustomerLineDetails();
		/* Begin - Changes made by Mitesh Parikh for 2422 JIRA */
		XPEDXWCUtils.setItemDetailBackPageURLinSession();
		/* End - Changes made by Mitesh Parikh for 2422 JIRA */
		return returnString;
	}

	public String sendMailToCSR() {
		String returnString = null;
		String csrEmailID = "";
		String toMail = "";
		try {
			returnString = super.execute();
			setUserName(getWCContext().getLoggedInUserName());
			Element eleUserInfo = XPEDXWCUtils.getUserInfo(getWCContext()
					.getCustomerContactId(), getWCContext().getStorefrontId());
			setRequestedBy(getUserDetails(eleUserInfo));
			
			/*Start Jira 3162 
			 * Getting CSR EmailIds from BillTo Customer, earlier it use to be retrieved from Division's Email address .
			 * For email JIRA 2134- 'To' address should be CSR email id and 'CC' address should be Sales RP email id.
			 * 
			 * */
           /*String divisionEmail = getDivisionEmail();
			*/
			
			
			XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			String extnECsr1EmailID = shipToCustomer.getBillTo().getExtnECsr1EMailID();
			String extnECsr2EmailID = shipToCustomer.getBillTo().getExtnECsr2EMailID();
			String sapCustomerKey = shipToCustomer.getBillTo().getParentCustomerKey();
			
			csrEmailID = setCSREmails(extnECsr1EmailID,extnECsr2EmailID);
			
			String saleRepEmail = getSalesRepEmail(sapCustomerKey);
			
			/*Jira 3162
			 * If csrEmailID null or empty in that case saleRepEmail id should be tomail address 
			 *	  
			 * */
			if(csrEmailID != null && csrEmailID.trim().length() > 0){
				toMail = csrEmailID;
			}else if(saleRepEmail != null && saleRepEmail.trim().length() > 0){
				toMail = saleRepEmail;				
			}
			/*End -JIRA 3162 done changes*/
			
			/* 
			 * 
			 * 
			 * */
			/*Start Jira 3162 
			 * 
			 * Getting CSR EmailIds from BillTo Customer, earlier it use to be retrieved from Division's Email address .
			 * For email JIRA 2134- 'To' address should be CSR email id and 'CC' address should be Sales RP email id.
			 * 
			 * */
           			
			/*Element eleToSend = prepareDocumentToSend(
					getUserEmail(eleUserInfo), csrEmailIDList);
			
			*
			*/
            

			Element eleToSend = prepareDocumentToSend(
					getUserEmail(eleUserInfo), toMail);
			
			String inputXml = SCXmlUtil.getString(eleToSend);
//			Element email = SCXmlUtil.getChildElement(eleToSend, "Email");
//			email.setAttribute("ToEmail", email.getAttribute("InputEmail"));
			LOG.debug("Input XML: " + inputXml);
			Object obj = WCMashupHelper.invokeMashup(
					"SendEmailItemReturnRequest", eleToSend, wcContext
							.getSCUIContext());
			Document outputDoc = ((Element) obj).getOwnerDocument();
			if (null != outputDoc) {
				LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
			}
		} catch (Exception ex) {
			LOG.debug("Output XML: " + ex.getMessage());
		}
		return returnString;
	}

	public String returnItemComplaintRequest() {
		Element eleReturnItemComplaintRequest = prepareItemReturnComplaintRequest();
		String inputXml = SCXmlUtil.getString(eleReturnItemComplaintRequest);
		LOG.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("ItemReturnComplaintRequest",
				eleReturnItemComplaintRequest, wcContext.getSCUIContext());
		Document outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}

		return SUCCESS;
	}

	private Element prepareItemReturnComplaintRequest() {
		Element eleUserInfo = XPEDXWCUtils.getUserInfo(getWCContext()
				.getCustomerContactId(), getWCContext().getStorefrontId());
		String userEmail = getUserEmail(eleUserInfo);
		String divisionEmail = getDivisionEmail();
		Document templateEmailDoc = YFCDocument.createDocument("Emails")
				.getDocument();
		Element templateElement = templateEmailDoc.getDocumentElement();
		Element emailElement = templateEmailDoc.createElement("Email");
		emailElement.setAttribute("FromEmail", userEmail);
		emailElement.setAttribute("ToEmail", divisionEmail);
		emailElement.setAttribute("OrderDate", getOrderDate());
		emailElement.setAttribute("OrderNumber", getOrderNumber());
		emailElement.setAttribute("RequestType", getRequestType());
		emailElement.setAttribute("ServiceRequestType",
				"ItemReturnComplaintRequest");

		Element eleProductDetail = templateEmailDoc
				.createElement("ProductDetail");
		eleProductDetail.setAttribute("ItemNumber", getItemNumber());
		eleProductDetail.setAttribute("ItemDescription", getDescription());
		eleProductDetail.setAttribute("Quantity", getItemQTY());
		eleProductDetail.setAttribute("UOM", getItemUOM());
		eleProductDetail.setAttribute("ReasonType", getReasonForReturn());
		eleProductDetail.setAttribute("ReasonForReturn", getTxtReason());

		emailElement.appendChild(eleProductDetail);
		templateElement.appendChild(emailElement);

		return templateElement;
	}

	private String getDivisionEmail() {
		String divisionEmail = "";
		try {
			Element ele = prepareAndInvokeMashup("getDivisionEmail");
			divisionEmail = ele.getAttribute("EMailID");
		} catch (XMLExceptionWrapper e) {
			LOG.error("Unable to retrieve division email " + e.getMessage());
		} catch (CannotBuildInputException e) {
			LOG.error("Unable to retrieve division email " + e.getMessage());
		}
		return divisionEmail;
	}

	private Element prepareDocumentToSend(String fromEmail, String toMail) {
		Document doc = getOutputDocument();
		Element elementOrder = doc.getDocumentElement();
		Element elePaymentMethods = SCXmlUtil.getChildElement(elementOrder,
				"PaymentMethods");
		Element elePaymentMethod = SCXmlUtil.getChildElement(elePaymentMethods,
				"PaymentMethod");
		String accountNumber = SCXmlUtil.getAttribute(elePaymentMethod, "DisplayCustomerAccountNo");
		Element eleExtn = SCXmlUtil.getChildElement(elementOrder, "Extn");
		String wcNumber = eleExtn.getAttribute("ExtnWebConfNum");
		String orderNo = elementOrder.getAttribute("OrderNo");

		Document templateEmailDoc = YFCDocument.createDocument("Emails")
				.getDocument();
		Element templateElement = templateEmailDoc.getDocumentElement();
		Element emailElement = templateEmailDoc.createElement("Email");
		emailElement.setAttribute("FromEmail", fromEmail);
		emailElement.setAttribute("ToEmail", toMail);
		emailElement.setAttribute("AccountNumber", accountNumber);
		emailElement.setAttribute("WCNumber", wcNumber);
		emailElement.setAttribute("OrderNumber", orderNo);
		emailElement.setAttribute("RequestedBy", getRequestedBy());
		emailElement.setAttribute("Username", getUserName());
		emailElement.setAttribute("Phone", getReturnsph());
		emailElement.setAttribute("InputEmail", getReturnsEmail());
		emailElement.setAttribute("Note", getNotes());
		emailElement.setAttribute("ServiceRequestType", "ItemReturnRequest");

		Element eleProductDetail = null;

		ArrayList<Element> eleLineElements = getMajorLineElements();
		List selectedItems = getSelectedItems(eleLineElements.size());

		for (int i = 0; i < eleLineElements.size(); i++) {
			Element eleOrderLine = eleLineElements.get(i);
			Element elementItem = SCXmlUtil.getChildElement(eleOrderLine, "Item");
			String orderLineKey=SCXmlUtil.getAttribute(eleOrderLine, "OrderLineKey");
			if (!(selectedItems != null && selectedItems.size() > 0
					&& selectedItems.contains(orderLineKey))) {
				continue;
			}
			Element eleOrderLineTranQTY = SCXmlUtil.getChildElement(
					eleOrderLine, "OrderLineTranQuantity");
			String uom = elementItem.getAttribute("UnitOfMeasure");
			Map<String, String> uomMap = null;
			try {
				uomMap = UtilBean.getUOMDescriptions(getWCContext(), true);
			} catch (Exception e) {
				LOG.error("Unable to retrieve UOM map " + e.getMessage());
			}

			eleProductDetail = templateEmailDoc.createElement("ProductDetail");
			eleProductDetail.setAttribute("ItemId", elementItem
					.getAttribute("ItemID"));
			try {
				eleProductDetail.setAttribute("ItemShortDescription",
						getShortDescriptionForOrderLine(eleOrderLine));
			} catch (DOMException e) {
				LOG
						.error("DOMException occured while retrieving short description "
								+ e.getMessage());
			} catch (Exception e) {
				LOG.error("Unable to retrieve short description "
						+ e.getMessage());
			}
			eleProductDetail.setAttribute("QTYShipped", eleOrderLineTranQTY
					.getAttribute("OrderedQty"));
			eleProductDetail.setAttribute("QTYReturn", getReturnsqty().get(i));
			eleProductDetail.setAttribute("UOM", uomMap.get(uom));
			if (getReasonreturn().get(i).equalsIgnoreCase("Others")) {
				eleProductDetail.setAttribute("ReasontoReturn", getOthertext()
						.get(i));
			} else {
				eleProductDetail.setAttribute("ReasontoReturn",
						getReasonreturn().get(i));
			}

			emailElement.appendChild(eleProductDetail);
		}

		templateElement.appendChild(emailElement);
		return templateElement;
	}

	private List<String> getSelectedItems(int size) {
		List<String> list = new ArrayList<String>();
		for (int index = 0; index < size; index++) {
			String selectedItem = getWCContext().getSCUIContext().getRequest()
					.getParameter("selectedItems[" + index + "]");
			if (selectedItem != null && selectedItem.trim().length() > 0
					&& !selectedItem.equals("true")) {
				list.add(selectedItem);
			}
		}
		return list;
	}

	public String getUserEmail(Element eleUserInfo) {
		Element eleCustContact =XPEDXWCUtils.getMSAPCustomerContactFromContacts(eleUserInfo.getOwnerDocument(), wcContext);
		NodeList nodeList = eleCustContact.getElementsByTagName("ContactPersonInfo");
		Element elePersonInfo = (Element) nodeList.item(0);
		String userMail = elePersonInfo.getAttribute("EMailID");
		return userMail;
	}

	public String getUserDetails(Element eleUserInfo) {
		Element eleCustContact =XPEDXWCUtils.getMSAPCustomerContactFromContacts(eleUserInfo.getOwnerDocument(), wcContext);
		NodeList nodeList = eleCustContact.getElementsByTagName("ContactPersonInfo");
		Element elePersonInfo = (Element) nodeList.item(0);
		String firstName = elePersonInfo.getAttribute("FirstName");
		String lastName = elePersonInfo.getAttribute("LastName");
		String requestedName = firstName + " " + lastName;
		return requestedName;
	}

	/**
	 * JIRA 243
	 * Modified getCustomerDetails method to consider the mashup to be invoked
	 * so that, we get only the required information - here Extn field only.
	 * @param inputItems
	 * @return
	 */
	
	protected LinkedHashMap getCustomerFieldsMapfromSession(){
		/*HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
        HashMap customerFieldsSessionMap = (HashMap)localSession.getAttribute("customerFieldsSessionMap");*/
		XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
		LinkedHashMap customerFieldsSessionMap = (LinkedHashMap)XPEDXWCUtils.getObjectFromCache("customerFieldsSessionMap");
        return customerFieldsSessionMap;
	}
	
	protected void getCustomerLineDetails() {
		//get the map from the session. if null query the DB
		LinkedHashMap customerFieldsSessionMap = getCustomerFieldsMapfromSession();
        if(null != customerFieldsSessionMap && customerFieldsSessionMap.size() >= 0){
        	LOG.debug("Found customerFieldsMap in the session");
        	customerFieldsMap = customerFieldsSessionMap;
        	return;
        }

/*      String customerId = wcContext.getCustomerId();
		String orgCode = wcContext.getStorefrontId();
		wcContext.getCustomerContactId();
		Document customerDoc;
		try {
			customerDoc = XPEDXWCUtils.getCustomerDetails(customerId, orgCode, customerExtnMashUp);
			customerFieldsMap = XPEDXOrderUtils
					.getCustomerLineFieldMap(customerDoc);
		} catch (CannotBuildInputException e) {
			LOG.error("Unable to get Customer Line details for " + customerId
					+ ". " + e);
			if (null == customerFieldsMap) {
				customerFieldsMap = new HashMap<String, String>();
			}
		}
*/
	}

	@Override
	protected String getOrderDetailsMashupName() {
		return XPEDXOrderConstants.XPEDX_COMPLETE_ORDER_DETAIL_MASHUP;
	}

	public boolean isCancel() {
		if (!isUserHaveResourceAccessPermission("/swc/order/authenticatedOrdering"))
			return false;
		String supportLevel = SCXmlUtils.getAttribute(getElementOrder(),
				"SupportLevel");
		boolean bSupportLevel = false;
		bSupportLevel = supportLevel.equals("Full")
				|| supportLevel.equals("Medium");
		if (!bSupportLevel)
			return false;
		boolean containPendingChanges = SCXmlUtils.getBooleanAttribute(
				getElementOrder(), "HasPendingChanges");
		if (containPendingChanges)
			return false;
		/*
		 * if(SCXmlUtils.getAttribute(getElementOrder(),
		 * "MaxOrderStatus").equals("9000")) return false;
		 */
		String maxOrderStatus = SCXmlUtils.getAttribute(getElementOrder(),
				"MaxOrderStatus");
		if (!maxOrderStatus.equals("1100") && // Created
				!maxOrderStatus.equals("1100.0100") && // Placed
				!maxOrderStatus.equals("1100.5100") && // Legacy Open
				!maxOrderStatus.equals("1100.5200") && // Legacy web hold
				!maxOrderStatus.equals("1100.5300")) // Legacy System/Customer
														// hold
			return false;
		Element cancelModificationRuleAtOrder = SCXmlUtils
				.getElementByAttribute(getElementOrder(),
						"Modifications/Modification", "ModificationType",
						"CANCEL");
		return SCXmlUtils.getBooleanAttribute(cancelModificationRuleAtOrder,
				"ModificationAllowed");
	}
	
	private String setCSREmails(String strExtnECsr1EMailID,String strExtnECsr2EMailID) 
    {
		String strExtnECsrEMailID = "";
	   if((strExtnECsr1EMailID != null && !strExtnECsr1EMailID.equalsIgnoreCase("")) && (strExtnECsr2EMailID != null && !strExtnECsr2EMailID.equalsIgnoreCase(""))){
		   strExtnECsrEMailID = strExtnECsr1EMailID + XPEDXConstants.EMAILIDSEPARATOR + strExtnECsr2EMailID;
		}else if(strExtnECsr1EMailID != null && !strExtnECsr1EMailID.equalsIgnoreCase("")){
			strExtnECsrEMailID = strExtnECsr1EMailID;
			}
		else if(strExtnECsr2EMailID != null && !strExtnECsr2EMailID.equalsIgnoreCase("")){
			strExtnECsrEMailID  = strExtnECsr2EMailID;
		}
		
	 
	  return strExtnECsrEMailID;
	 
	}

 	public String getSalesRepEmail(String sapCustomerKey) {
 		
	 String totalSalesRepEmail = "";
	 
 	 if (sapCustomerKey != null && !sapCustomerKey.equalsIgnoreCase("")) {
 		 
 		Element xpedxSalesRep = SCXmlUtil.createDocument("XPEDXSalesRep").getDocumentElement();
 		xpedxSalesRep.setAttribute("SalesCustomerKey", sapCustomerKey);  
 		
 		Document outputDoc;
		
 		Object obj = WCMashupHelper.invokeMashup("getXpedxSalesRepList", xpedxSalesRep, wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
        
 		
 		NodeList nodeList  = outputDoc.getElementsByTagName("XPEDXSalesRep");  
		int salesRepLength = nodeList.getLength();
		List<String> salesRepUserKeysList = new ArrayList<String>();
		
		for (int counter = 0; counter < salesRepLength ; counter++) {
			Element salesRepElem = (Element) nodeList.item(counter);
			String salesUserKey = "";
			if (salesRepElem.hasAttribute("SalesUserKey")) {
				salesUserKey = salesRepElem.getAttribute("SalesUserKey");
			}
			if(salesUserKey !=null && salesUserKey.trim().length() > 0)
				salesRepUserKeysList.add(salesUserKey);
		}
		
		if(salesRepUserKeysList.size() > 0){
			try {
			Element inputElem = WCMashupHelper.getMashupInput("getUserListWithContactPersonInfo", wcContext);
			Element complexQueryElem = SCXmlUtil.getChildElement(inputElem, "ComplexQuery");
			Element OrElem = SCXmlUtil.getChildElement(complexQueryElem, "Or");
			Iterator<String> itr = salesRepUserKeysList.iterator();
			while(itr.hasNext()) {
				String userKey = (String)itr.next();
				if(userKey!=null && !userKey.equals("")){
					Element exp = inputElem.getOwnerDocument().createElement("Exp");
					exp.setAttribute("Name", "UserKey");
					exp.setAttribute("Value", userKey);
					SCXmlUtil.importElement(OrElem, exp);
				}
			}
			 
			outputDoc =((Element) WCMashupHelper.invokeMashup("getUserListWithContactPersonInfo", inputElem, wcContext.getSCUIContext())).getOwnerDocument();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			 
			NodeList personInfoList = outputDoc.getElementsByTagName("ContactPersonInfo");
	 		int contactPersonInfoLength = personInfoList.getLength();
 			for (int counter = 0; counter < contactPersonInfoLength ; counter++) {
 				Element personInfoElem = (Element) personInfoList.item(counter);
 				if (personInfoElem != null && personInfoElem.hasAttribute("EmailID")) {
 					String salesRepEmail = personInfoElem.getAttribute("EmailID");
 					if (salesRepEmail != null && !salesRepEmail.equalsIgnoreCase("")) {
 						totalSalesRepEmail = totalSalesRepEmail + XPEDXConstants.EMAILIDSEPARATOR + salesRepEmail;
 					}
 				}
 			}
	 		
	    }	
 	}
 	return totalSalesRepEmail;
 	}
 	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	private String userName;
	private String requestedBy;
	protected LinkedHashMap<String, String> customerFieldsMap;

	// Variables declaration for itemReturnComplaintRequest
	private String itemNumber;
	private String description;
	private String itemQTY;
	private String itemUOM;
	private String reasonForReturn;
	private String txtReason;
	private String orderNumber;
	private String orderDate;
	private String requestType;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getItemQTY() {
		return itemQTY;
	}

	public void setItemQTY(String itemQTY) {
		this.itemQTY = itemQTY;
	}

	public String getItemUOM() {
		return itemUOM;
	}

	public void setItemUOM(String itemUOM) {
		this.itemUOM = itemUOM;
	}

	public String getReasonForReturn() {
		return reasonForReturn;
	}

	public void setReasonForReturn(String reasonForReturn) {
		this.reasonForReturn = reasonForReturn;
	}

	public String getTxtReason() {
		return txtReason;
	}

	public void setTxtReason(String txtReason) {
		this.txtReason = txtReason;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	private static final Logger LOG = Logger
			.getLogger(XPEDXReturnItemsRequestAction.class);

	public LinkedHashMap<String, String> getCustomerFieldsMap() {
		return customerFieldsMap;
	}

	public void setCustomerFieldsMap(LinkedHashMap<String, String> customerFieldsMap) {
		this.customerFieldsMap = customerFieldsMap;
	}

}

/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.order;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.order.OrderConstants;
import com.sterlingcommerce.webchannel.order.OrderListAction;
import com.sterlingcommerce.webchannel.utilities.WCDataDeFormatHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.yfc.date.YDate;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfs.core.YFSSystem;

/**
 * @author rugrani
 *
 */
public class XPEDXOrderListAction extends OrderListAction {
	
	private static final String PLACED_STATUS = "1100.0100";
	private static final String CANCELLED_STATUS = "9000";
	private static final String INVOICED_STATUS = "1100.5700";
	private static final String PENDING_APPROVAL_STATUS = "1100.0100_Approval";
	private static final String CSR_REVIEWING_STATUS = "1100.0100_CSRReview";
	private static final String REJECTED_STATUS = "1100.0100_Rejected";
	private static final String ENC_USER_KEY = "ENC_USER_KEY";
	private boolean isPendingApprovalOrdersSearch = false;
	private boolean isCSRReviewingOrdersSearch = false;
	private boolean isRejectedOrdersSearch = false;
	private String custSuffix = null;
	private String userKey = null;
	private String invoiceURL = null;
	private Integer assignedShipToSize;
	private String MASHUP_NAME="XPEDXOrderList";
	private String rootElementName="XPEDXOrderSearchListView";
	private String pageSetToken;	
	private String isCSRReview = "N";
	
	public String getIsCSRReview() {
		return isCSRReview;
	}

	public void setIsCSRReview(String isCSRReview) {
		this.isCSRReview = isCSRReview;
	}

	public String getPageSetToken() {
		return pageSetToken;
	}

	public void setPageSetToken(String pageSetToken) {
		this.pageSetToken = pageSetToken;
	}

	public Integer getAssignedShipToSize() {
		return assignedShipToSize;
	}

	public void setAssignedShipToSize(Integer assignedShipToSize) {
		this.assignedShipToSize = assignedShipToSize;
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
	/**
	 * 
	 */
	public XPEDXOrderListAction() {
		super();
		//Orderlist customization: RUgrani
		xpedxChainedOrderListMap = new HashMap();
		xpedxParentOrderListMap = new LinkedHashMap<String, Element>();
		orderType = XPEDXOrderConstants.XPEDX_ORDER_TYPE_CUSTOMER;
		orderTypeQryType = queryTypeEq;
		statusSearchList = new LinkedHashMap();
		// Added for JIRA 2770
		shipToSearchList = new LinkedHashMap();
		orderListSearchDateRangeField = "";
	}

	@Override public String execute()
    {
		String val = YFSSystem.getProperty(ORDER_WIDGET_RECORD_PER_PAGE_PARAM);
		if (!YFCCommon.isVoid(val)){
			ORDER_WIDGET_RECORD_PER_PAGE = new Integer(val.trim());
		}
		val = YFSSystem.getProperty(ORDER_LIST_RECORD_PER_PAGE_PARAM);
		if (!YFCCommon.isVoid(val)){
			ORDER_LIST_RECORD_PER_PAGE = new Integer(val.trim());
		}

		String result = "success";
		if (getXpedxSelectedHeaderTab() != null && getXpedxSelectedHeaderTab().equals("AddToExistingOrder"))
        {
			if("ProductIdValue".equals(getSearchFieldName()))
	        {
				rootElementName="XPEDXEditableOrderLineListView";
				MASHUP_NAME="XPEDXEditableOrderLineList";
	        
	        } else if("LegacyOrderNumberValue".equals(getSearchFieldName())){
	        	rootElementName="XPEDXOrderSearchListView";
				MASHUP_NAME="XPEDXOrderList";
	        
	        } else {
	        	rootElementName="XPEDXEditableOrderListView";
	    		MASHUP_NAME="XPEDXEditableOrderList";
	    		
	        }
        
        } else {
        	if("ProductIdValue".equals(getSearchFieldName()))
	        {
				rootElementName="XPEDXOrderSearchLineListView";
				MASHUP_NAME="XPEDXOrderLineList";
	        
	        }    	
        	
        }
		
		String messageType = getMessageType();
        if(messageType != null && messageType.equals("OrderListWidget"))
        {
        	if (!(getXpedxSelectedHeaderTab() != null && getXpedxSelectedHeaderTab().equals("AddToExistingOrder")))
            {    	        
        		setOrderByAttribute("Modifyts");
            }
            setPageNumber(ORDER_WIDGET_PAGE_NUMBER);
            setRecordPerPage(ORDER_WIDGET_RECORD_PER_PAGE);
        } else
        {
        	if (!(getXpedxSelectedHeaderTab() != null && getXpedxSelectedHeaderTab().equals("AddToExistingOrder")))
            {    	        
        		if(getOrderByAttribute() == null || getOrderByAttribute().trim().length()==0)
            		setOrderByAttribute("OrderDate");
            }
            setRecordPerPage(ORDER_LIST_RECORD_PER_PAGE);
        }
        
        try
        {
        	//getAssignedCustomerListForLoggedInUser();
        	//As per discussion with Pawan, Include all the ship to's if the ship to count is less than 30.
        	XPEDXCustomerContactInfoBean custContBean=(XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache("XPEDX_Customer_Contact_Info_Bean");
        	Integer numberOfAssignedShipTo=custContBean.getNumberOfAssignedShioTos();
        	if(numberOfAssignedShipTo == null || numberOfAssignedShipTo == 0)
        	{
	        	Document assignedCustomersDoc = XPEDXWCUtils.getPaginatedAssignedCustomersDocument(getWCContext());
	        	if(assignedCustomersDoc!=null) {
	        		Element customerAssignment = SCXmlUtil.getChildElement(assignedCustomersDoc.getDocumentElement(), "Output");
	        		List<String> assignedShipToList = XPEDXWCUtils.parseForShipToCustomers(customerAssignment);
	        		Element viewListElem = SCXmlUtil.getChildElement(customerAssignment, "XPXCustomerAssignmentViewList");
	        		String totalNumOfRecords = viewListElem.getAttribute("TotalNumberOfRecords");
	        		if( totalNumOfRecords != null  && totalNumOfRecords.trim().length() > 0 )
	        			assignedShipToSize = Integer.parseInt(totalNumOfRecords);
	        		else
	        			assignedShipToSize = 0;
	        		custContBean.setNumberOfAssignedShioTos(assignedShipToSize);
	        		XPEDXWCUtils.setObectInCache("XPEDX_Customer_Contact_Info_Bean", custContBean);
	        		setShipToList(XPEDXWCUtils.getHashMapFromListWithLabel(assignedShipToList));
	        	}
        	}
        	else
        	{
        		assignedShipToSize=numberOfAssignedShipTo;
        		if(numberOfAssignedShipTo <=20)
        		{        			
	        		List<String> assignedShipToList = (List<String>)XPEDXWCUtils.getObjectFromCache("XPEDX_20_ASSIFNED_SHIPTOS");
	        		setShipToList(XPEDXWCUtils.getHashMapFromListWithLabel(assignedShipToList));
        		}
        	}
        	/*Map statusSearchListVal=(Map)wcContext.getSCUIContext().getAttribute("statusSearchListVal");
        	if(!(statusSearchListVal != null && statusSearchListVal.size()>0))
        	{
        		getStatusList();
        	}  */     	
        	getStatusList();
        	/*if (getXpedxSelectedHeaderTab() != null && getXpedxSelectedHeaderTab().equals("AddToExistingOrder")) {
        		if (getSourceTab() != null && getSourceTab().equals("Open")) {
        			//setStatusSearchFieldName("1100.5250");        			
        		}        		
        	}*/
        	
        	if(!YFCCommon.isVoid(getStatusSearchFieldName()))
        	{
        		// If the status is either 1100.5150 or 1100.5155 then they are searching for Pending Approval or CSR Reviewing orders.
        		// so Setting the status to PLACED and then setting the boolean accordingly.
        		// while retrieving we check if any of the boolean flags are set and return that status accordingly 
        		//for the drop down to pre-select that status in jsp
        		if(getStatusSearchFieldName().equalsIgnoreCase(PENDING_APPROVAL_STATUS)){
        			isPendingApprovalOrdersSearch = true;
        			setStatusSearchFieldName(PLACED_STATUS);
        		}
        		else if(getStatusSearchFieldName().equalsIgnoreCase(CSR_REVIEWING_STATUS)){
        			isCSRReviewingOrdersSearch = true;
        			setStatusSearchFieldName(PLACED_STATUS);
        		}
        		else if(getStatusSearchFieldName().equalsIgnoreCase(REJECTED_STATUS)){
        			isRejectedOrdersSearch = true;
        			setStatusSearchFieldName(PLACED_STATUS);
        		}
        		if(getStatusSearchFieldName().equalsIgnoreCase(PLACED_STATUS)){
        			//Get both Customer and Fulfillment orders for Placed, Cancelled and Invoiced Status
        			// Jira 2208 - If order search status is placed, get only customer orders
        			setOrderType(XPEDXOrderConstants.XPEDX_ORDER_TYPE_CUSTOMER);
        			setOrderTypeQryType(queryTypeEq);
        		}
        		else{
        			//Get only fulfillment orders
        			setOrderType(XPEDXOrderConstants.XPEDX_ORDER_TYPE_CUSTOMER);
        			setOrderTypeQryType(queryTypeNe);
        		}
        			
        	}
			populateOrderList();
            //outputDoc = (Element)prepareAndInvokeMashups().get("XPEDXOrderList");
            //BEGIN: Orderlist : RUgrani
			Document customerOrderDoc = outputDoc.getOwnerDocument();
			//LinkedList chainedOrderFromKeylist = getAllParentOrderHeaderKeys(outputDoc.getOwnerDocument());
			LinkedList webConfNumlist =getAllWebConfNum(outputDoc.getOwnerDocument());
			Document orderList = getXpedxChainedOrderList(webConfNumlist);
			setXpedxChainedOrderMap(orderList);
			/*if(!YFCCommon.isVoid(getStatusSearchFieldName())){
				//Prepare the Customer Order Document and create the customer info map
				customerOrderDoc = populateCustomerOrderList(chainedOrderFromKeylist);
			}*/
			if (getXpedxSelectedHeaderTab() != null && getXpedxSelectedHeaderTab().equals("AddToExistingOrder")) {
				customerOrderDoc=orderList;
			}
			parseCustomerDoc(customerOrderDoc);

			
			//Getting customer suffix from customer ID and UserKey for View Invoices.
			String custId = getWCContext().getCustomerId();
			String[] custDetails = custId.split("-");
			if(custDetails!=null && custDetails.length>3)
			{
				custSuffix = custDetails[2];
				if (custSuffix != null && custSuffix.trim().length() > 0) {
					custSuffix = XPEDXWCUtils.encrypt(custSuffix);
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
					log.error("Error in getting user key.", e);
				}
			}
			
			invoiceURL = YFSSystem.getProperty("xpedx.invoicing.url"); //to take it from properties files.
			
			//END: Orderlist : RUgrani
            processOutput(outputDoc);
            
        }
        catch(Exception ex)
        {
            log.error("Error in getting order list.", ex);
            result = "error";
        }
//        setInitialDates();
//         No need to set the initial dates for OM2 implementation
        return result;
    }

	private void getStatusList() throws Exception
	{
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/CommonCode/@CallingOrganizationCode", wcContext.getCustomerMstrOrg());
		
		Element input = WCMashupHelper.getMashupInput("XPEDXgetStatusList", valueMap,
				wcContext.getSCUIContext());
		Object obj = WCMashupHelper.invokeMashup(
				"XPEDXgetStatusList", input, wcContext
						.getSCUIContext());
		Document statusLisOutputDoc = ((Element) obj).getOwnerDocument();
		List<Element> itemList=SCXmlUtils.getElements(statusLisOutputDoc.getDocumentElement(), "/CommonCode");
		//statusSearchList.put(XPEDXConstants.DEFAULT_SELECT_ORDER_CRITERIA_LABEL, XPEDXConstants.DEFAULT_SELECT_ORDER_CRITERIA_LABEL);
		statusSearchList.put(XPEDXConstants.DEFAULT_SELECT_ORDER_CRITERIA_LABEL, "All Order Statuses");
		//statusSearchList.put("-1", "All");
		//TODO : FXD-6 : Fix the Sorting 
		// Added sorting for JIRA 2733
		Map<String, String> tempStatusList = new LinkedHashMap<String, String>();		
		for(Element elem : itemList){
			//statusSearchList.put(elem.getAttribute("CodeValue"), elem.getAttribute("CodeShortDescription"));
			tempStatusList.put(elem.getAttribute("CodeValue"), elem.getAttribute("CodeShortDescription"));
		}	
		List list = new LinkedList(tempStatusList.entrySet());
		Collections.sort(list,new StatusComparator());		
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Map.Entry entry = (Map.Entry) itr.next();
			statusSearchList.put((String) entry.getKey(), (String) entry
					.getValue());
		}			
	}
	
	//JIRA 2770 - This will populate the ShipTo search combo box
	public Map getShipToSearchList() {
		if(assignedShipToSize!=null && assignedShipToSize<=20) {
			shipToSearchList.put("",XPEDXConstants.DEFAULT_SELECT_SEARCH_SHIPTO_CRITERIA_LABEL1);
			shipToSearchList.put(1,XPEDXConstants.DEFAULT_SELECT_SEARCH_SHIPTO_CRITERIA_LABEL2);
		}
		else {
			shipToSearchList.put(wcContext.getCustomerId(), XPEDXWCUtils.formatBillToShipToCustomer(wcContext.getCustomerId()));
			shipToSearchList.put(1,XPEDXConstants.DEFAULT_SELECT_SEARCH_SHIPTO_CRITERIA_LABEL2);
		}
		
		if(!SCUtil.isVoid(getShipToSearchFieldName())){
			shipToSearchList.put(getShipToSearchFieldName(),XPEDXWCUtils.formatBillToShipToCustomer(getShipToSearchFieldName()));
		}
		return shipToSearchList;
	}
	
	//JIRA 2733 Comaparator for sorting the Order status List
	private static class StatusComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			String ccDesc1 = (String) ((Map.Entry) (o1)).getValue();
			String ccDesc2 = (String) ((Map.Entry) (o2)).getValue();
			return ccDesc1.compareTo(ccDesc2);
		}

	}
	
	public String getOrderListSearchDateRangeField() {
		return orderListSearchDateRangeField;
	}

	public void setOrderListSearchDateRangeField(
			String orderListSearchDateRangeField) {
		this.orderListSearchDateRangeField = orderListSearchDateRangeField;
	}
	
	@Override public Map getSearchList()
    {
        //searchList = super.getSearchList();
		//searchList.put("- Select Search Criteria -", "- Select Search Criteria -");
		searchList.put(XPEDXConstants.DEFAULT_SELECT_SEARCH_CRITERIA_LABEL,XPEDXConstants.DEFAULT_SELECT_SEARCH_CRITERIA_LABEL);
		searchList.put("ProductIdValue", "Item #");
		searchList.put("LegacyOrderNumberValue", "Order #");
		searchList.put("PurchaseOrderNumberValue", "PO #");
		searchList.put("WebConfNumberValue", "Web Confirmation");
        return searchList;
    }
	
	@Override public void setSearchFieldValue(String value)
    {
        if("ProductIdValue".equals(getSearchFieldName()))
        {
            setProductIdValue(value);
        }
        else
        if("OrderNameValue".equals(getSearchFieldName()))
            setOrderNameValue(value);
        else
        if("OrderNumberValue".equals(getSearchFieldName()))
            setOrderNumberValue(value);
        
        else if("PurchaseOrderNumberValue".equals(getSearchFieldName()))
            setPurchaseOrderNumberValue(value);
       
        else if("WebConfNumberValue".equals(getSearchFieldName()))
            setWebConfNumberValue(value);
        
        else if("LegacyOrderNumberValue".equals(getSearchFieldName()))
            setLegacyProductNumValue(value);
        
        searchFieldValue = value;
    }
	
	public String getXpedxSearchFieldName() {
		String newSearcFieldName = "";
		if(getSearchFieldName()== null || getSearchFieldName().length()==0 || getSearchFieldName().equalsIgnoreCase("-1")
				|| getSearchFieldName().equalsIgnoreCase(XPEDXConstants.DEFAULT_SELECT_SEARCH_CRITERIA_LABEL))
			newSearcFieldName = "";
		else
			newSearcFieldName = getSearchFieldName();
		return newSearcFieldName;
	}
	
	//This will populate the status search combo box
	public Map getStatusSearchList()
    {
		/*statusSearchList.put("", "All");
        statusSearchList.put(XPEDXOrderConstants.XPEDX_ORDER_STATUS_OPEN, "Open");
        statusSearchList.put(XPEDXOrderConstants.XPEDX_ORDER_STATUS_SHIPPED, "Shipped");
        statusSearchList.put(XPEDXOrderConstants.XPEDX_ORDER_STATUS_BACK_ORDERED, "Back Ordered");*/
        return statusSearchList;
    }
	

	/**
	 * @param draftOrderListDoc
	 * @return chainedOrderFromKeylist
	 */
	private LinkedList getAllWebConfNum(Document orderListDoc) {
		LinkedList webConfList = new LinkedList();
		if(null == orderListDoc){
			log.debug("orderListDoc is empty. Returning...");
			return webConfList;
		}
		NodeList nlOrderList = orderListDoc.getElementsByTagName(rootElementName);
		//Element orderElement = null;
		int length = nlOrderList.getLength();
		for(int i=0;i<length;i++){
			//Get each Order
			Element orderElement = (Element) nlOrderList.item(i);
			//Extract OrderHeaderKey
			//If Order status is not empty and not equal to Placed(1100.0100), then the below will be Fulfillment Order's Order Header Key
			//String chainedFromOHK = SCXmlUtil.getAttribute(orderElement, "OrderHeaderKey");	
			/*ArrayList<Element> orderExtnList=SCXmlUtil.getElements(orderElement, "Extn");
			if(orderExtnList != null && orderExtnList.size() >0)
			{*/
				String webConfNum=orderElement.getAttribute("ExtnWebConfNum");//SCXmlUtil.getXpathAttribute(orderElement, "//Order/Extn/@ExtnWebConfNum");
			
			 
			String orderName = SCXmlUtil.getAttribute(orderElement, "OrderName");
			/*if(this.legacyProductNumValue != null || !YFCCommon.isVoid(getStatusSearchFieldName())){
				//If the above order header key is a fulfillment order, fetch its respective customer order
				NodeList ordlerLineNodeList=orderElement.getElementsByTagName("OrderLine");
				if(ordlerLineNodeList != null){
					if(ordlerLineNodeList.getLength() >0){
						Element orderLineElement = (Element) ordlerLineNodeList.item(0);
						String chainedOHK = SCXmlUtil.getAttribute(orderLineElement, "ChainedFromOrderHeaderKey");
						//The above field will have value only when its a fulfillment order
						if(!YFCCommon.isVoid(chainedOHK))
							webConfNum = chainedOHK;
						
					}
				}
			}*/
			log.debug("Ordername and WebConfNum of the All order: "+orderName+" , "+webConfNum);
			if(!webConfList.contains(webConfNum))
				webConfList.add(webConfNum);
			//}
		}
		return webConfList;
	}
	
	private LinkedList getAllParentOrderHeaderKeys(Document orderListDoc) {
		LinkedList chainedOrderFromKeylist = new LinkedList();
		if(null == orderListDoc){
			log.debug("orderListDoc is empty. Returning...");
			return chainedOrderFromKeylist;
		}
		NodeList nlOrderList = orderListDoc.getElementsByTagName("Order");
		Element orderElement = null;
		int length = nlOrderList.getLength();
		for(int i=0;i<length;i++){
			//Get each Order
			orderElement = (Element) nlOrderList.item(i);
			//Extract OrderHeaderKey
			//If Order status is not empty and not equal to Placed(1100.0100), then the below will be Fulfillment Order's Order Header Key
			String chainedFromOHK = SCXmlUtil.getAttribute(orderElement, "OrderHeaderKey");	
			String orderName = SCXmlUtil.getAttribute(orderElement, "OrderName");
			if(this.legacyProductNumValue != null || !YFCCommon.isVoid(getStatusSearchFieldName())){
				//If the above order header key is a fulfillment order, fetch its respective customer order
				NodeList ordlerLineNodeList=orderElement.getElementsByTagName("OrderLine");
				if(ordlerLineNodeList != null){
					if(ordlerLineNodeList.getLength() >0){
						Element orderLineElement = (Element) ordlerLineNodeList.item(0);
						String chainedOHK = SCXmlUtil.getAttribute(orderLineElement, "ChainedFromOrderHeaderKey");
						
						//The above field will have value only when its a fulfillment order
						if(!YFCCommon.isVoid(chainedOHK))
							chainedFromOHK = chainedOHK;
						
					}
				}
			}
			log.debug("Ordername and OrderHeaderKey of the parent order: "+orderName+" , "+chainedFromOHK);
			if(!chainedOrderFromKeylist.contains(chainedFromOHK))
				chainedOrderFromKeylist.add(chainedFromOHK);
		}
		return chainedOrderFromKeylist;
	}
	
	protected Document getXpedxChainedOrderList(LinkedList extnWebConfList) throws Exception{
		Document outputDoc = null;

		if (null == extnWebConfList || extnWebConfList.isEmpty()) {
			setOrderListExist("false");
			log
					.debug("getXpedxChainedOrderLineList: Atleast one order header key is required. Use getOrderLineList API to get all the orderlines");
			return outputDoc;
		} else {
			setOrderListExist("true");
		}
		
		Element input = WCMashupHelper.getMashupInput("xpedxChainedOrderList",
				getWCContext().getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element)inputNodeList.item(0);
		for(int i=0;i<extnWebConfList.size();i++){
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			Element expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", "ExtnWebConfNum");
			expElement.setAttribute("Value", (String) extnWebConfList.get(i));
			
			inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
		}
		inputXml = SCXmlUtil.getString(input);
		log.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxChainedOrderList", input,
				getWCContext().getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}

		return outputDoc; 
	}
	
	protected Document getXpedxChainedOrderLineList(LinkedList chainedOrderFromKeylist) throws Exception{
		Document outputDoc = null;

		if (null == chainedOrderFromKeylist || chainedOrderFromKeylist.isEmpty()) {
			log
					.debug("getXpedxChainedOrderLineList: Atleast one order header key is required. Use getOrderLineList API to get all the orderlines");
			return outputDoc;
		}

		Element input = WCMashupHelper.getMashupInput("xpedxChainedOrderLineList",
				getWCContext().getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element)inputNodeList.item(0);
		for(int i=0;i<chainedOrderFromKeylist.size();i++){
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			Element expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", "ChainedFromOrderHeaderKey");
			expElement.setAttribute("Value", (String) chainedOrderFromKeylist.get(i));
			
			inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
		}
		inputXml = SCXmlUtil.getString(input);
		log.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxChainedOrderLineList", input,
				getWCContext().getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}

		return outputDoc; 
	}
	
	
	protected void setXpedxChainedOrderMap(Document chainedOrderLineList) throws Exception{
		if(null == chainedOrderLineList){
			LOG.debug("setXpedxChainedOrderMap: Empty chainedOrderLineList.... No chained orders");
			return;
		}
		//get all the Orderlines from the document
		//modified for jira 4068
		NodeList nlOrderList=chainedOrderLineList.getElementsByTagName("XPEDXOrderSearchListView");
        int length = nlOrderList.getLength();
		ArrayList chainedOrders = new ArrayList();
		ArrayList<String> alreadyAddedOrders = new ArrayList<String>();
		Element order = null;
		Map<String,String> customerOrderMap=getCustomerOrderMap(chainedOrderLineList);
		for(int i=0;i<length;i++){
			//Get each orderline
			order = (Element) nlOrderList.item(i);
			//Extract Order element from each orderline
			//order = XMLUtilities.getElement(orderLine, "Order");
			//put the Order element in the xpedxChainedOrderListMap. The key will be the "OrderLine/ChainedFromOrderHeaderKey"
			String orderType=order.getAttribute("OrderType");//SCXmlUtil.getXpathAttribute(order, "//Order/@OrderType");
			//ArrayList<Element> orderExtnList=SCXmlUtil.getElements(order, "Extn");
			String extnWebConfNum="";
			/*if(orderExtnList != null && orderExtnList.size() >0)
			{*/
				extnWebConfNum=order.getAttribute("ExtnWebConfNum");
			//}
			
			String orderHeaderKey = SCXmlUtil.getAttribute(order, "OrderHeaderKey");
			String orderStatus = order.getAttribute("Status"); //SCXmlUtil.getAttribute(order, "Status");
			if("Customer".equals(orderType))
			{
				//To get The latest hold order 
				if(xpedxParentOrderListMap.get(extnWebConfNum) != null)
				{
					Element oldOrder=xpedxParentOrderListMap.get(extnWebConfNum);
					if(oldOrder.getAttribute("HoldType") != null &&	XPEDXConstants.HOLD_TYPE_FOR_NEEDS_ATTENTION.equals(oldOrder.getAttribute("HoldType")))
						continue;
				}
				//Ends
				xpedxParentOrderListMap.put(extnWebConfNum, order);
				continue;
			}
			String headerStatusCode = order.getAttribute("ExtnHeaderStatusCode");
			String legacyOrderNumber = order.getAttribute("ExtnLegacyOrderNo");
			if(null == legacyOrderNumber || "".equals(legacyOrderNumber.trim()) ||( null != headerStatusCode
					  && !"".equals(headerStatusCode.trim()) && !headerStatusCode.equals("M0000"))) {
				isCSRReview = "Y";
				order.setAttribute("isCSRReview", isCSRReview);
			}
			String chainedFromOHK =customerOrderMap.get(extnWebConfNum);
			//xpedxChainedOrderListMap should have customer OHK as key, fullfillment orders as value
			if(!alreadyAddedOrders.contains(orderHeaderKey)){
				if(xpedxChainedOrderListMap.containsKey(chainedFromOHK)){
					ArrayList<Element> orderList = (ArrayList<Element>) xpedxChainedOrderListMap.get(chainedFromOHK);
					orderList.add(order);
					ArrayList clonedOrderList = (ArrayList<Element>)orderList.clone();
					xpedxChainedOrderListMap.remove(chainedFromOHK);
					xpedxChainedOrderListMap.put(chainedFromOHK, clonedOrderList);
				}else{
					ArrayList<Element> orderList = new ArrayList<Element>();
					//orderList.add(order);
					if(this.legacyProductNumValue != null || !YFCCommon.isVoid(getStatusSearchFieldName())){
						if(!YFCCommon.isVoid(getStatusSearchFieldName()))
						{
							//If any order status is chosen, get the fulfillment order which matches the status
							//and add it against its customer order key
							String codeDesc = (String)statusSearchList.get(getStatusSearchFieldName());
							if(orderStatus.equalsIgnoreCase(codeDesc.trim())){
								orderList.add(order);
								xpedxChainedOrderListMap.put(chainedFromOHK, orderList);
							}
						}
						else
							//xpedxChainedOrderListMap.put(orderHeaderKey, orderList);
						{
							orderList.add(order);
							xpedxChainedOrderListMap.put(chainedFromOHK, orderList);
						}
					}
					else{
						orderList.add(order);
						xpedxChainedOrderListMap.put(chainedFromOHK, orderList);
						
					}
					
				}
				alreadyAddedOrders.add(orderHeaderKey);
			}
			
		}
	}
	
	private Map<String,String>getCustomerOrderMap(Document chainedOrderLineList)
	{
		Map<String,Map> outputMap=new LinkedHashMap<String, Map>();
		Map<String,String> customerOrderMap=new LinkedHashMap<String, String>();
		//modified for jira 4068
		NodeList nlOrderList = chainedOrderLineList.getElementsByTagName("XPEDXOrderSearchListView");
		Element orderElem=null;
		for(int i=0;i<nlOrderList.getLength();i++){
			orderElem = (Element) nlOrderList.item(i);
			//ArrayList<Element> orderExtnList=SCXmlUtil.getElements(orderElem, "Extn");
			String extnWebConfNum="";
			/*if(orderExtnList != null && orderExtnList.size() >0)
			{*/
				extnWebConfNum=orderElem.getAttribute("ExtnWebConfNum");//SCXmlUtil.getXpathAttribute(orderElem, "//Order/Extn/@ExtnWebConfNum");
			
				//String extnWebConfNum=SCXmlUtil.getXpathAttribute(orderElem, "//Order/Extn/@ExtnWebConfNum"); 
				String orderType=orderElem.getAttribute("OrderType") ;//SCXmlUtil.getXpathAttribute(orderElem, "//Order/@OrderType"); 
				String orderHeaderKey=orderElem.getAttribute("OrderHeaderKey") ; // SCXmlUtil.getXpathAttribute(orderElem, "//Order/@OrderHeaderKey"); 
				if(orderType != null && "Customer".equals(orderType))
					customerOrderMap.put(extnWebConfNum, orderHeaderKey);
			//}	
		}
		
		return customerOrderMap;
	}
	/*
	 * Takes a list of orderlines(The output from getOrderLineList(Complex Query:For multiple ChainedFromOrderHeaderKey)
	 */
	/*protected void setXpedxChainedOrderMap(Document chainedOrderLineList) throws Exception{
		if(null == chainedOrderLineList){
			LOG.debug("setXpedxChainedOrderMap: Empty chainedOrderLineList.... No chained orders");
			return;
		}
		//get all the Orderlines from the document
		NodeList nlOrderLineList = chainedOrderLineList.getElementsByTagName("OrderLine");
		int length = nlOrderLineList.getLength();
		ArrayList chainedOrders = new ArrayList();
		ArrayList<String> alreadyAddedOrders = new ArrayList<String>();
		Element orderLine = null;
		
		for(int i=0;i<length;i++){
			//Get each orderline
			orderLine = (Element) nlOrderLineList.item(i);
			//Extract Order element from each orderline
			Element order = XMLUtilities.getElement(orderLine, "Order");
			//put the Order element in the xpedxChainedOrderListMap. The key will be the "OrderLine/ChainedFromOrderHeaderKey"
			String chainedFromOHK = SCXmlUtil.getAttribute(orderLine, "ChainedFromOrderHeaderKey");
			String orderHeaderKey = SCXmlUtil.getAttribute(order, "OrderHeaderKey");
			String orderStatus = SCXmlUtil.getAttribute(order, "Status");
			//xpedxChainedOrderListMap should have customer OHK as key, fullfillment orders as value
			if(!alreadyAddedOrders.contains(orderHeaderKey)){
				if(xpedxChainedOrderListMap.containsKey(chainedFromOHK)){
					ArrayList<Element> orderList = (ArrayList<Element>) xpedxChainedOrderListMap.get(chainedFromOHK);
					orderList.add(order);
					ArrayList clonedOrderList = (ArrayList<Element>)orderList.clone();
					xpedxChainedOrderListMap.remove(chainedFromOHK);
					xpedxChainedOrderListMap.put(chainedFromOHK, clonedOrderList);
				}else{
					ArrayList<Element> orderList = new ArrayList<Element>();
					//orderList.add(order);
					if(this.legacyProductNumValue != null || !YFCCommon.isVoid(getStatusSearchFieldName())){
						if(!YFCCommon.isVoid(getStatusSearchFieldName()))
						{
							//If any order status is chosen, get the fulfillment order which matches the status
							//and add it against its customer order key
							String codeDesc = (String)statusSearchList.get(getStatusSearchFieldName());
							if(orderStatus.equalsIgnoreCase(codeDesc.trim())){
								orderList.add(order);
								xpedxChainedOrderListMap.put(chainedFromOHK, orderList);
							}
						}
						else
							//xpedxChainedOrderListMap.put(orderHeaderKey, orderList);
						{
							orderList.add(order);
							xpedxChainedOrderListMap.put(chainedFromOHK, orderList);
						}
					}
					else{
						orderList.add(order);
						xpedxChainedOrderListMap.put(chainedFromOHK, orderList);
					}
					
				}
				alreadyAddedOrders.add(orderHeaderKey);
			}
			
		}
	}*/
	
	public SCXmlUtils getXmlUtils()
    {
        return SCXmlUtils.getInstance();
    }

	
	public HashMap getXpedxChainedOrderListMap() {
		return xpedxChainedOrderListMap;
	}

	public void setXpedxChainedOrderListMap(HashMap xpedxChainedOrderListMap) {
		this.xpedxChainedOrderListMap = xpedxChainedOrderListMap;
	}
	
	
	
	/**
	 * @return the xpedxParentOrderListMap
	 */
	public HashMap<String, Element> getXpedxParentOrderListMap() {
		return xpedxParentOrderListMap;
	}

	/**
	 * @param xpedxParentOrderListMap the xpedxParentOrderListMap to set
	 */
	public void setXpedxParentOrderListMap(
			HashMap<String, Element> xpedxParentOrderListMap) {
		this.xpedxParentOrderListMap = xpedxParentOrderListMap;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	
	/**
	 * @return the orderTypeQryType
	 */
	public String getOrderTypeQryType() {
		return orderTypeQryType;
	}

	/**
	 * @param orderTypeQryType the orderTypeQryType to set
	 */
	public void setOrderTypeQryType(String orderTypeQryType) {
		this.orderTypeQryType = orderTypeQryType;
	}

	public String getSearchFieldValue() {
		return searchFieldValue;
	}
	
	protected void manipulateInputs(Map mashupInputs)
    {		
		Element orderListInput = (Element)mashupInputs.get(MASHUP_NAME);
        YFCElement yorderListInput = YFCDocument.getDocumentFor(orderListInput.getOwnerDocument()).getDocumentElement();
        YFCElement apiElement = yorderListInput.getChildElement("API");
        YFCElement inpElement = apiElement.getChildElement("Input");
        YFCElement orderElem = inpElement.getChildElement(rootElementName);
        YFCElement complexQueryElement =null;
        /* Including only the logged in ship to Id as This is causing the performance Issue -- Jagadeesh
         * Including all the assigned ship to only if assignedShipToSize is less than or equal to 30 -- Discussed with pawan on call  */
        if(assignedShipToSize!=null && assignedShipToSize<=20 && SCUtil.isVoid(getShipToSearchFieldName())) {
        	complexQueryElement = orderElem.createChild("ComplexQuery");
    		YFCElement complexQueryOrElement = complexQueryElement.createChild("Or");
    		Iterator<String> itr = shipToList.keySet().iterator();
    		while (itr.hasNext()) {
    			String key = (String) itr.next();
    			if(!key.equals(XPEDXConstants.DEFAULT_SELECT_SHIP_TO_LABEL)){
    			    YFCElement expElement = complexQueryOrElement.createChild("Exp");
    				expElement.setAttribute("Name", "ShipToID");
    				expElement.setAttribute("Value", shipToList.get(key));
    				complexQueryOrElement.appendChild((YFCNode)expElement);
    			}
    		}
    		complexQueryElement.setAttribute("Operator", "AND"); 	
        }
        else // if the size is more than 30 then just getting the orders for the selected ship to
        {
        	if(SCUtil.isVoid(getShipToSearchFieldName()))//to enable the search functionality removed the buyerorgcode from the input instead passing the shiptoid
            	orderElem.setAttribute("ShipToID", wcContext.getCustomerId());
        }
        //Added condition Complex query if tab is addtoexistingorder get the all order which we can edit.
        if (getXpedxSelectedHeaderTab() != null && getXpedxSelectedHeaderTab().equals("AddToExistingOrder")) {
        	
        	if(!SCUtil.isVoid(getShipToSearchFieldName()))//added for jira 4138
            	orderElem.setAttribute("ShipToID", getShipToSearchFieldName());
        	
        	if("LegacyOrderNumberValue".equals(getSearchFieldName())){
        		if(complexQueryElement == null)
    			{
    				complexQueryElement = orderElem.createChild("ComplexQuery");
    			}
    			/*YFCElement complexQueryOrElement = complexQueryElement.createChild("Or");
    			YFCElement extnElement = orderElem;
    			extnElement.setAttribute("ExtnOrderLockFlag", "N");*/
        		YFCElement complexQueryOrElement = complexQueryElement.createChild("Or");
			    				
			    YFCElement expElementPlaced = complexQueryOrElement.createChild("Exp");
			    expElementPlaced.setAttribute("Name", "ExtnOrderStatus");
			    expElementPlaced.setAttribute("Value","1100.0100");
				complexQueryOrElement.appendChild((YFCNode)expElementPlaced);
				
				YFCElement expElementPA = complexQueryOrElement.createChild("Exp");
			    expElementPA.setAttribute("Name", "ExtnOrderStatus");
			    expElementPA.setAttribute("Value","1100.5150");
				complexQueryOrElement.appendChild((YFCNode)expElementPA);			    
				
			    YFCElement expElementOpen = complexQueryOrElement.createChild("Exp");
			    expElementOpen.setAttribute("Name", "ExtnOrderStatus");
			    expElementOpen.setAttribute("Value","1100.5250");
				complexQueryOrElement.appendChild((YFCNode)expElementOpen);
				
				YFCElement expElementCHold = complexQueryOrElement.createChild("Exp");
				expElementCHold.setAttribute("Name", "ExtnOrderStatus");
				expElementCHold.setAttribute("Value","1100.5350");
				complexQueryOrElement.appendChild((YFCNode)expElementCHold);
				
				YFCElement expElementSHold = complexQueryOrElement.createChild("Exp");
			    expElementSHold.setAttribute("Name", "ExtnOrderStatus");
			    expElementSHold.setAttribute("Value","1100.5400");
				complexQueryOrElement.appendChild((YFCNode)expElementSHold);
				
			    YFCElement expElementWHold = complexQueryOrElement.createChild("Exp");
			    expElementWHold.setAttribute("Name", "ExtnOrderStatus");
			    expElementWHold.setAttribute("Value","1100.5450");
				complexQueryOrElement.appendChild((YFCNode)expElementWHold);
				
        		complexQueryElement.setAttribute("Operator", "AND");
	        
	        } else {
	        	YFCElement isHoldType_submittedCSRReview = orderElem;
	        	isHoldType_submittedCSRReview.setAttribute("IsHoldTypeSbmtCSRReview", "N");
	        	isHoldType_submittedCSRReview.setAttribute("IsHoldTypeSbmtCSRReviewQryType", "EQ");	        	
	        	
	        }

        	YFCElement isOrderLocked = orderElem;
        	isOrderLocked.setAttribute("IsOrderLocked", "N");
        	isOrderLocked.setAttribute("IsOrderLockedQryType", "EQ");
        	//orderElem.setAttribute("ShipToID", wcContext.getCustomerId());
    		/*//if (getSourceTab() != null && getSourceTab().equals("Open")) {
        	YFCElement isHoldType_submittedCSRReview = orderElem;
        	isHoldType_submittedCSRReview.setAttribute("IsHoldTypeEqSbmtCSRReview", "N");
        	isHoldType_submittedCSRReview.setAttribute("IsHoldTypeEqSbmtCSRReviewQryType", "EQ");
    		
    			if(complexQueryElement == null)
    			{
    				complexQueryElement = orderElem.createChild("ComplexQuery");
    			}
    			YFCElement complexQueryOrElement = complexQueryElement.createChild("Or");
    			YFCElement extnElement = orderElem;
    			extnElement.setAttribute("ExtnOrderLockFlag", "N");
        		YFCElement complexQueryOrElement = complexQueryElement.createChild("Or");
			    YFCElement expElementCreated = complexQueryOrElement.createChild("Exp");
			    expElementCreated.setAttribute("Name", "ExtnOrderStatus");
			    expElementCreated.setAttribute("Value","1100");
				complexQueryOrElement.appendChild((YFCNode)expElementCreated);
				
			    YFCElement expElementPlaced = complexQueryOrElement.createChild("Exp");
			    expElementPlaced.setAttribute("Name", "ExtnOrderStatus");
			    expElementPlaced.setAttribute("Value","1100.0100");
				complexQueryOrElement.appendChild((YFCNode)expElementPlaced);
				
				YFCElement expElementPA = complexQueryOrElement.createChild("Exp");
			    expElementPA.setAttribute("Name", "ExtnOrderStatus");
			    expElementPA.setAttribute("Value","1100.5150");
				complexQueryOrElement.appendChild((YFCNode)expElementPA);			    
				
			    YFCElement expElementOpen = complexQueryOrElement.createChild("Exp");
			    expElementOpen.setAttribute("Name", "ExtnOrderStatus");
			    expElementOpen.setAttribute("Value","1100.5250");
				complexQueryOrElement.appendChild((YFCNode)expElementOpen);
				
				YFCElement expElementCHold = complexQueryOrElement.createChild("Exp");
				expElementCHold.setAttribute("Name", "ExtnOrderStatus");
				expElementCHold.setAttribute("Value","1100.5350");
				complexQueryOrElement.appendChild((YFCNode)expElementCHold);
				
				YFCElement expElementSHold = complexQueryOrElement.createChild("Exp");
			    expElementSHold.setAttribute("Name", "ExtnOrderStatus");
			    expElementSHold.setAttribute("Value","1100.5400");
				complexQueryOrElement.appendChild((YFCNode)expElementSHold);
				
			    YFCElement expElementWHold = complexQueryOrElement.createChild("Exp");
			    expElementWHold.setAttribute("Name", "ExtnOrderStatus");
			    expElementWHold.setAttribute("Value","1100.5450");
				complexQueryOrElement.appendChild((YFCNode)expElementWHold);*/
				
        		//complexQueryElement.setAttribute("Operator", "AND");			
				
    		//}   
        } else {
	        if(isPendingApprovalOrdersSearch || isCSRReviewingOrdersSearch || isRejectedOrdersSearch) {
	        	String holdTypeToSearch = null;
	        	if(isPendingApprovalOrdersSearch)
	        		holdTypeToSearch = XPEDXConstants.HOLD_TYPE_FOR_PENDING_APPROVAL;
	        	else if(isCSRReviewingOrdersSearch)
	        		holdTypeToSearch = XPEDXConstants.HOLD_TYPE_FOR_NEEDS_ATTENTION;
	        	else if(isRejectedOrdersSearch)
	        		holdTypeToSearch = XPEDXConstants.HOLD_TYPE_FOR_PENDING_APPROVAL;
	        	if(!SCUtil.isVoid(holdTypeToSearch)) {
	        		YFCElement holdTypeElement = orderElem;
	        		holdTypeElement.setAttribute(XPXLiterals.A_HOLD_TYPE, holdTypeToSearch);
	        		holdTypeElement.setAttribute("HoldTypeQryType", "FLIKE");
	        		if(isRejectedOrdersSearch){
	        			holdTypeElement.setAttribute("HoldStatus", "1200");
	        		}else {
	        			holdTypeElement.setAttribute("HoldStatus", "1100");
	        		}
	        	}        	
	        }
        }
	        
        if(!YFCCommon.isVoid(getSubmittedTSFrom()))
            orderElem.setAttribute("FromOrderDate", WCDataDeFormatHelper.getDeformattedDate(getWCContext().getSCUIContext(), getSubmittedTSFrom()));
        if(!YFCCommon.isVoid(getSubmittedTSTo()))
        	orderElem.setAttribute("ToOrderDate", WCDataDeFormatHelper.getDeformattedDate(getWCContext().getSCUIContext(), getSubmittedTSTo()));        
        
        if(!YFCCommon.isVoid(getOrderListSearchDateRangeField())){
        	YDate startDate = YDate.newDate();
        	String startDateString = startDate.getString(YDate.ISO_DATETIME_FORMAT, wcContext.getSCUIContext().getUserPreferences().getLocale().getJLocale());
        	YFCDate endDate = startDate.getNewDate(-Integer.valueOf(getOrderListSearchDateRangeField()).intValue());
        	String endDateString = endDate.getString(YDate.ISO_DATETIME_FORMAT, wcContext.getSCUIContext().getUserPreferences().getLocale().getJLocale());
        	orderElem.setAttribute("FromOrderDate", endDateString);
        	orderElem.setAttribute("ToOrderDate", startDateString);
        	orderElem.setAttribute("OrderDateQryType", "DATERANGE");
        }
        
    }
	
	protected void populateOrderList()
    throws CannotBuildInputException, XMLExceptionWrapper
	{
		Set mashupId=new HashSet();
		mashupId.add(MASHUP_NAME);
	    Map mashupInputs = prepareMashupInputs(mashupId);
	    manipulateInputs(mashupInputs);
	    Map mashupOutputs = invokeMashups(mashupInputs);
	    outputDoc = (Element)mashupOutputs.get(MASHUP_NAME);
	    pageSetToken=outputDoc.getAttribute("PageSetToken");
	}
	
	private Document populateCustomerOrderList(LinkedList chainedOrderFromKeylist) throws Exception {
		Document outputDoc = null;
		if (null == chainedOrderFromKeylist || chainedOrderFromKeylist.isEmpty()) {
			log
					.debug("getXpedxChainedOrderLineList: Atleast one order header key is required. Use getOrderLineList API to get all the orderlines");
			return outputDoc;
		}

		Element input = WCMashupHelper.getMashupInput("XPEDXCustomerOrderList",
				getWCContext().getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element)inputNodeList.item(0);
		for(int i=0;i<chainedOrderFromKeylist.size();i++){
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			Element expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", "OrderHeaderKey");
			expElement.setAttribute("Value", (String) chainedOrderFromKeylist.get(i));
			
			inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
		}
		inputXml = SCXmlUtil.getString(input);
		log.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("XPEDXCustomerOrderList", input,
				getWCContext().getSCUIContext());
		
		outputDoc = ((Element) obj).getOwnerDocument();
		
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString(outputDoc));
		}
		return outputDoc;
	}
	
	private void parseCustomerDoc(Document customerOrderDoc){
		if (null == customerOrderDoc) {
			log
					.debug("getXpedxChainedOrderLineList: Atleast one order header key is required. Use getOrderLineList API to get all the orderlines");
			return;
		}
		HashMap<String, Element> customerorderMap=new HashMap<String,Element>(xpedxParentOrderListMap);
		xpedxParentOrderListMap.clear();
		NodeList orderElems=null;
		if (getXpedxSelectedHeaderTab() != null && getXpedxSelectedHeaderTab().equals("AddToExistingOrder"))
			orderElems = customerOrderDoc.getDocumentElement().getElementsByTagName("XPEDXOrderSearchListView");
		else
			orderElems = customerOrderDoc.getDocumentElement().getElementsByTagName(rootElementName);
		
		if(orderElems!=null && orderElems.getLength()>0)
		{
			for(int i=0;i<orderElems.getLength();i++){
				Element orderElem =  (Element)orderElems.item(i);
				ArrayList<Element> orderExtnElements=SCXmlUtil.getElements(orderElem, "Extn");
				/*if(orderExtnElements != null && orderExtnElements.size()>0)
				{*/
					//Element orderExtnElement=orderExtnElements.get(0);
					String extnWebConfNum=orderElem.getAttribute("ExtnWebConfNum");
					Element orderElement=customerorderMap.get(extnWebConfNum);
					if(orderElement != null)
					{
						String orderHeaderKey = orderElement.getAttribute("OrderHeaderKey");
						xpedxParentOrderListMap.put(orderHeaderKey, orderElement);
					}
					else
					{
						String orderHeaderKey = orderElem.getAttribute("OrderHeaderKey");
						xpedxParentOrderListMap.put(orderHeaderKey, orderElem);
					}
			//	}
				
			}
		}
	}
	
	protected void getAssignedCustomerListForLoggedInUser(){
		String loggedInCustomerFromSession = XPEDXWCUtils
		.getLoggedInCustomerFromSession(wcContext);
		HashMap<String, ArrayList<String>> allMap = null;
		try{
			if (loggedInCustomerFromSession != null
					&& loggedInCustomerFromSession.trim().length() > 0) {
				allMap = XPEDXWCUtils.getAssignedCustomersMap(loggedInCustomerFromSession,
						wcContext.getLoggedInUserId());
			} else {
				allMap = XPEDXWCUtils.getAssignedCustomersMap(wcContext.getCustomerId(),
						wcContext.getLoggedInUserId());
			}
		}catch(CannotBuildInputException e){
			log.error("Unable to get Assigned BillTo and Ship to for logged in user: "+loggedInCustomerFromSession+". "+e );
		}
		populateAccountIdList(allMap);
		populateShipToList(allMap);
	}
	
	protected void populateAccountIdList(HashMap<String, ArrayList<String>> allMap){
		List<String> billToListFromDB = allMap.get(XPEDXWCUtils.XPEDX_ASSIGNED_CUSTOMER_BILL_TO);
		setAccountIdList(XPEDXWCUtils.getHashMapFromList(billToListFromDB));
		log.debug("Assigned billTos***********=" + accountIdList);
	}
	
	protected void populateShipToList(HashMap<String, ArrayList<String>> allMap){		
		List<String> shipToListFromDB = XPEDXWCUtils.getAllAssignedShiptoForAUser(getWCContext().getLoggedInUserId(),getWCContext());		
		setShipToList(XPEDXWCUtils.getHashMapFromListWithLabel(shipToListFromDB));
		log.debug("Assigned shipTos***********=" + shipToList);
	}
	
	private void setInitialDates(){
		YDate endDate = YDate.newDate();
    	initialToDateString = endDate.getString("MM/dd/yyyy", wcContext.getSCUIContext().getUserPreferences().getLocale().getJLocale());
    	YFCDate startDate = endDate.getNewDate(-30);
    	initialFromDateString = startDate.getString("MM/dd/yyyy", wcContext.getSCUIContext().getUserPreferences().getLocale().getJLocale());
	}
	
	public void setStatusSearchFieldName(String value)
    {
		//Setting the status search query type to EQ
		setStatusSearchQryType(queryTypeEq);
        this.statusSearchFieldName = value;  
        
    }
	
	/**
	 * @return the statusSearchFieldName
	 */
	public String getStatusSearchFieldName() {
		if(statusSearchFieldName== null || statusSearchFieldName.length()==0 || statusSearchFieldName.equalsIgnoreCase("-1")
				|| statusSearchFieldName.equalsIgnoreCase(XPEDXConstants.DEFAULT_SELECT_ORDER_CRITERIA_LABEL))
			statusSearchFieldName = "";
		return statusSearchFieldName;
	}
	
	public String getExactStatus() {
		String statusToSearch = null;
		if(isPendingApprovalOrdersSearch)
			statusToSearch = PENDING_APPROVAL_STATUS;
		else if(isCSRReviewingOrdersSearch)
			statusToSearch = CSR_REVIEWING_STATUS;
		else if(isRejectedOrdersSearch)
			statusToSearch = REJECTED_STATUS;
		if(SCUtil.isVoid(statusToSearch))
			return getStatusSearchFieldName();
		else
			return statusToSearch;
			
	}
	

	/**
	 * @param accountIdList the accountIdList to set
	 */
	public void setAccountIdList(HashMap<String, String> accountIdList) {
		this.accountIdList = accountIdList;
	}

	/**
	 * @param shipToList the shipToList to set
	 */
	public void setShipToList(HashMap<String, String> shipToList) {
		this.shipToList = shipToList;
	}

	/**
	 * @return the accountIdList
	 */
	public HashMap<String,String> getAccountIdList() {
		if (accountIdList == null){
			accountIdList = new HashMap<String, String>();
		}
		return accountIdList;
	}

	/**
	 * @return the shipToList
	 */
	public HashMap<String,String> getShipToList() {
		if (shipToList == null){
			shipToList = new HashMap<String, String>();
		}
		return shipToList;
	}
	
	/**
	 * @return the accountSearchFieldName
	 */
	public String getAccountSearchFieldName() {
		return accountSearchFieldName;
	}

	/**
	 * @param accountSearchFieldName the accountSearchFieldName to set
	 */
	public void setAccountSearchFieldName(String accountSearchFieldName) {
		this.accountSearchFieldName = accountSearchFieldName;
	}

	/**
	 * @return the shipToSearchFieldName
	 */
	public String getShipToSearchFieldName() {
		if(shipToSearchFieldName == null || shipToSearchFieldName.length()==0 || shipToSearchFieldName.equalsIgnoreCase(XPEDXConstants.DEFAULT_SELECT_SHIP_TO_LABEL))
			shipToSearchFieldName="";
		return shipToSearchFieldName;
	}

	/**
	 * @param shipToSearchFieldName the shipToSearchFieldName to set
	 */
	public void setShipToSearchFieldName(String shipToSearchFieldName) {
		this.shipToSearchFieldName = shipToSearchFieldName;
	}
	
	/**
	 * @return the legacyProductNumValue
	 */
	public String getLegacyProductNumValue() {
		return legacyProductNumValue;
	}

	/**
	 * @param legacyProductNumValue the legacyProductNumValue to set
	 */
	public void setLegacyProductNumValue(String legacyProductNumValue) {
		if(!legacyProductNumValue.equals(""))
        {
            if(legacyProductNumValue.endsWith("*"))
            {
            	this.legacyProductNumValue = legacyProductNumValue.substring(0, legacyProductNumValue.length() - 1);
            	setExtnLegacyOrderNoQryType(queryTypeFlike);
            } else
            {
            	this.legacyProductNumValue = legacyProductNumValue;
            	String[] arr = this.legacyProductNumValue.split("-");
            	if(arr!=null && arr.length==3)
            	{
            		this.legacyProductNumValue = arr[1];
            	}
            }
            setOrderType(null);//in order to get Fulfillment orders also
            setOrderTypeQryType(queryTypeEq);
        }
	}

	/**
	 * @return the webConfNumberValue
	 */
	public String getWebConfNumberValue() {
		return webConfNumberValue;
	}

	/**
	 * @param webConfNumberValue the webConfNumberValue to set
	 */
	public void setWebConfNumberValue(String webConfNumberValue) {
		if(!webConfNumberValue.equals(""))
        {
            if(webConfNumberValue.endsWith("*"))
            {
            	this.webConfNumberValue = webConfNumberValue.substring(0, webConfNumberValue.length() - 1);
            	setExtnWebConfNumQryType(queryTypeFlike);
            } else
            {
            	this.webConfNumberValue = webConfNumberValue;
            }
        }
	}
	
	/**
	 * @return the extnLegacyOrderNoQryType
	 */
	public String getExtnLegacyOrderNoQryType() {
		return extnLegacyOrderNoQryType;
	}

	/**
	 * @param extnLegacyOrderNoQryType the extnLegacyOrderNoQryType to set
	 */
	public void setExtnLegacyOrderNoQryType(String extnLegacyOrderNoQryType) {
		this.extnLegacyOrderNoQryType = extnLegacyOrderNoQryType;
	}

	/**
	 * @return the extnWebConfNumQryType
	 */
	public String getExtnWebConfNumQryType() {
		return extnWebConfNumQryType;
	}

	/**
	 * @param extnWebConfNumQryType the extnWebConfNumQryType to set
	 */
	public void setExtnWebConfNumQryType(String extnWebConfNumQryType) {
		this.extnWebConfNumQryType = extnWebConfNumQryType;
	}
	private static final String ORDER_WIDGET_RECORD_PER_PAGE_PARAM = "cart_widget_record_per_page";
    private static final String ORDER_LIST_RECORD_PER_PAGE_PARAM = "cart_list_record_per_page";

	private static final Integer ORDER_WIDGET_PAGE_NUMBER = Integer.valueOf(1);
    private static Integer ORDER_WIDGET_RECORD_PER_PAGE = Integer.valueOf(3);
    private static Integer ORDER_LIST_RECORD_PER_PAGE = Integer.valueOf(25);
    private static final String ORDER_LIST_WIDGET_SORT_COL = "Modifyts";
    private final Logger log = Logger.getLogger(XPEDXOrderListAction.class);
    //Orderlist customization: RUgrani
	protected HashMap xpedxChainedOrderListMap;
	protected HashMap<String, Element> xpedxParentOrderListMap;  
	/*public String orderType;
	public String searchFieldValue;
	public Map statusSearchList;
	public String statusSearchFieldName;*/
	protected String orderType;
	protected String orderTypeQryType;
	protected String searchFieldValue;
	protected Map statusSearchList;
	protected String statusSearchFieldName;
	protected String shipToSearchFieldName = XPEDXConstants.DEFAULT_SELECT_SHIP_TO_LABEL;
	protected String accountSearchFieldName;
	protected HashMap<String,String> accountIdList;
	protected HashMap<String,String> shipToList;	
	protected String webConfNumberValue;
	protected String legacyProductNumValue;
	protected String extnWebConfNumQryType;
	protected String extnLegacyOrderNoQryType;
	private String orderListSearchDateRangeField;
	protected String initialFromDateString;
	protected String initialToDateString;
	//public String sourceTab;	
	protected String orderListExist;
	protected String xpedxSelectedHeaderTab;
	//added for jira 3484
	protected String primaryApproverID;
	protected String proxyApproverID;
	public String getPrimaryApproverID() {
		return primaryApproverID;
	}

	public void setPrimaryApproverID(String primaryApproverID) {
		this.primaryApproverID = primaryApproverID;
	}

	public String getProxyApproverID() {
		return proxyApproverID;
	}

	public void setProxyApproverID(String proxyApproverID) {
		this.proxyApproverID = proxyApproverID;
	}
	
	
	protected Map shipToSearchList;

	public void setShipToSearchList(Map shipToSearchList) {
		this.shipToSearchList = shipToSearchList;
	}

	public String getXpedxSelectedHeaderTab() {
		return xpedxSelectedHeaderTab;
	}

	public void setXpedxSelectedHeaderTab(String xpedxSelectedHeaderTab) {
		this.xpedxSelectedHeaderTab = xpedxSelectedHeaderTab;
	}

	public String getOrderListExist() {
		return orderListExist;
		
	}

	public void setOrderListExist(String orderListExist) {
		this.orderListExist = orderListExist;
	}

	public String getInitialFromDateString() {
		return initialFromDateString;
	}

	public void setInitialFromDateString(String initialFromDateString) {
		this.initialFromDateString = initialFromDateString;
	}

	public String getInitialToDateString() {
		return initialToDateString;
	}

	public void setInitialToDateString(String initialToDateString) {
		this.initialToDateString = initialToDateString;
	}
	
	

public String getRootElementName() {
		return rootElementName;
	}

	public void setRootElementName(String rootElementName) {
		this.rootElementName = rootElementName;
	}

	/*	
	public void setStatusSearchList(Map statusSearchList) {
		this.statusSearchList = statusSearchList;
	}
*/
	public boolean isOrderOnHold(Element OrderElement , String holdTypeToCheck) {
		boolean isOrderOnHold = false;
		if(OrderElement!=null && holdTypeToCheck!=null && holdTypeToCheck.trim().length()>0) {
			/*Element orderHoldTypesElem = SCXmlUtil.getChildElement(OrderElement,"OrderHoldTypes");
			ArrayList<Element> orderHoldTypeList = SCXmlUtil.getElements(orderHoldTypesElem, "OrderHoldType");
			if(orderHoldTypeList!=null && orderHoldTypeList.size()>0) {
				for(int i=0; i<orderHoldTypeList.size();i++) {
					Element orderHoldTypeElem = orderHoldTypeList.get(i);*/
					String holdType = OrderElement.getAttribute("HoldType");
					String holdTypeStatus = OrderElement.getAttribute("HoldStatus");
					if(holdType.equalsIgnoreCase(holdTypeToCheck) && (holdTypeStatus.equalsIgnoreCase("1100")
							|| holdTypeStatus.equalsIgnoreCase("1200")))
						isOrderOnHold = true;
				}
			//}
		//}
		return isOrderOnHold;
	}
	
	public boolean isOrderOnRejectHold(Element OrderElement ) {
		String holdTypeToCheck = XPEDXConstants.HOLD_TYPE_FOR_PENDING_APPROVAL;;
		boolean isOrderOnHold = false;
		if(OrderElement!=null && holdTypeToCheck!=null && holdTypeToCheck.trim().length()>0) {		
					String holdType = OrderElement.getAttribute("HoldType");
					String holdTypeStatus = OrderElement.getAttribute("HoldStatus");
					if(holdType.equalsIgnoreCase(holdTypeToCheck) && holdTypeStatus.equalsIgnoreCase("1200"))
						isOrderOnHold = true;		
		}
		return isOrderOnHold;
	}
	/*public boolean isOrderOnRejectHold(Element OrderElement ) {
		String holdTypeToCheck = XPEDXConstants.HOLD_TYPE_FOR_PENDING_APPROVAL;;
		boolean isOrderOnHold = false;
		if(OrderElement!=null && holdTypeToCheck!=null && holdTypeToCheck.trim().length()>0) {
			Element orderHoldTypesElem = SCXmlUtil.getChildElement(OrderElement,"OrderHoldTypes");
			ArrayList<Element> orderHoldTypeList = SCXmlUtil.getElements(orderHoldTypesElem, "OrderHoldType");
			if(orderHoldTypeList!=null && orderHoldTypeList.size()>0) {
				for(int i=0; i<orderHoldTypeList.size();i++) {
					Element orderHoldTypeElem = orderHoldTypeList.get(i);
					String holdType = SCXmlUtil.getAttribute(orderHoldTypeElem, "HoldType");
					String holdTypeStatus = SCXmlUtil.getAttribute(orderHoldTypeElem, "Status");
					if(holdType.equalsIgnoreCase(holdTypeToCheck) && holdTypeStatus.equalsIgnoreCase("1200"))
						isOrderOnHold = true;
				}
			}
		}
		return isOrderOnHold;
	}
	*/
	
	// Check if the current order has CSR Review hold or not
	public boolean isOrderOnCSRReviewHold(Element OrderElement) {		
		String holdTypeNeedsAttention = XPEDXConstants.HOLD_TYPE_FOR_NEEDS_ATTENTION;
		String holdTypeLegacyCnclOrd = XPEDXConstants.HOLD_TYPE_FOR_LEGACY_CNCL_ORD_HOLD;
		String holdTypeOrderException = XPEDXConstants.HOLD_TYPE_FOR_ORDER_EXCEPTION_HOLD;
		//Kubra Jira 4326
		String holdTypeLegacyLine= XPEDXConstants.HOLD_TYPE_FOR_LEGACY_LINE_HOLD;
		String holdTypeLegacyLineMcf = XPEDXConstants.HOLD_TYPE_FOR_FATAL_ERR_HOLD;
		
		String openHoldStatus = OrderConstants.OPEN_HOLD_STATUS;	
		
				String orderHoldType = OrderElement.getAttribute(OrderConstants.HOLD_TYPE);
				if ((OrderElement.getAttribute("HoldStatus").equals(openHoldStatus))
					&& (orderHoldType.equals(holdTypeNeedsAttention)
							|| orderHoldType.equals(holdTypeLegacyCnclOrd) 
							|| orderHoldType.equals(holdTypeOrderException)
							|| orderHoldType.equals(holdTypeLegacyLine)
							|| orderHoldType.equals(holdTypeLegacyLineMcf)))
					return true;		
		return false;
	}
	
	//4326 - Kubra
	public boolean isFOCSRReviewHold (Element OrderElement) {
	
		String holdTypeName =OrderElement.getAttribute("HoldType");
		if(!"Customer".equals(OrderElement.getAttribute("OrderType")) )
		{
			
				if(XPEDXConstants.HOLD_TYPE_FOR_LEGACY_LINE_HOLD.equals(holdTypeName)
						|| XPEDXConstants.HOLD_TYPE_FOR_LEGACY_CNCL_ORD_HOLD.equals(holdTypeName) 
						|| XPEDXConstants.HOLD_TYPE_FOR_FATAL_ERR_HOLD.equals(holdTypeName)
						|| XPEDXConstants.HOLD_TYPE_FOR_NEEDS_ATTENTION.equals(holdTypeName)
						|| XPEDXConstants.HOLD_TYPE_FOR_ORDER_EXCEPTION_HOLD.equals(holdTypeName))
					return true;
		}
		return false;

	}
	
	// Check if the current order has CSR Review hold or not
	/*public boolean isOrderOnCSRReviewHold(Element OrderElement) {		
		String holdTypeNeedsAttention = XPEDXConstants.HOLD_TYPE_FOR_NEEDS_ATTENTION;
		String holdTypeLegacyCnclOrd = XPEDXConstants.HOLD_TYPE_FOR_LEGACY_CNCL_ORD_HOLD;
		String holdTypeOrderException = XPEDXConstants.HOLD_TYPE_FOR_ORDER_EXCEPTION_HOLD;
		String openHoldStatus = OrderConstants.OPEN_HOLD_STATUS;
		
		Element orderHoldTypesElem = SCXmlUtil.getChildElement(OrderElement,"OrderHoldTypes");
		ArrayList<Element> orderHoldTypeList = SCXmlUtil.getElements(orderHoldTypesElem, "OrderHoldType");
		if(orderHoldTypeList!=null && orderHoldTypeList.size()>0) {
			for(int i=0; i<orderHoldTypeList.size();i++) {
				Element orderHoldTypeElem = orderHoldTypeList.get(i);
				String orderHoldType = orderHoldTypeElem.getAttribute(OrderConstants.HOLD_TYPE);
				if ((orderHoldTypeElem.getAttribute(OrderConstants.STATUS).equals(openHoldStatus))
					&& (orderHoldType.equals(holdTypeNeedsAttention)
							|| orderHoldType.equals(holdTypeLegacyCnclOrd) 
							|| orderHoldType.equals(holdTypeOrderException)))
					return true;
			}
		}
		return false;
	}
	*/
	
	public String getResolverUserId(Element OrderElement, String holdTypeToCheck){
		String resolverUserId=null;
		if(OrderElement!=null && holdTypeToCheck!=null && holdTypeToCheck.trim().length()>0) {		
					String holdType = OrderElement.getAttribute("HoldType");
					String holdTypeStatus = OrderElement.getAttribute("HoldStatus");
					if(holdType.equalsIgnoreCase(holdTypeToCheck) && holdTypeStatus.equalsIgnoreCase("1100")){
						resolverUserId = OrderElement.getAttribute("ResolverUserId");
						//modified for jira 3484
						if(resolverUserId != null){
							String approverUserIDs [] = resolverUserId.split(",");
							primaryApproverID = approverUserIDs[0];
							proxyApproverID = approverUserIDs[1];
						}
					}
		}
		return resolverUserId;
	}
	
	/*public String getResolverUserId(Element OrderElement, String holdTypeToCheck){
		String resolverUserId=null;
		if(OrderElement!=null && holdTypeToCheck!=null && holdTypeToCheck.trim().length()>0) {
			Element orderHoldTypesElem = SCXmlUtil.getChildElement(OrderElement,"OrderHoldTypes");
			ArrayList<Element> orderHoldTypeList = SCXmlUtil.getElements(orderHoldTypesElem, "OrderHoldType");
			if(orderHoldTypeList!=null && orderHoldTypeList.size()>0) {
				for(int i=0; i<orderHoldTypeList.size();i++) {
					Element orderHoldTypeElem = orderHoldTypeList.get(i);
					String holdType = SCXmlUtil.getAttribute(orderHoldTypeElem, "HoldType");
					String holdTypeStatus = SCXmlUtil.getAttribute(orderHoldTypeElem, "Status");
					if(holdType.equalsIgnoreCase(holdTypeToCheck) && holdTypeStatus.equalsIgnoreCase("1100"))
						resolverUserId = orderHoldTypeElem.getAttribute("ResolverUserId");
				}
			}
		}
		return resolverUserId;
	}*/
}
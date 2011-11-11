package com.sterlingcommerce.xpedx.webchannel.order.approval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.order.approval.OrderApprovalAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.date.YDate;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCDate;

/**
 * @author RUgrani
 * 
 */
public class XPEDXOrderApprovalAction extends OrderApprovalAction {
	
	
	public XPEDXOrderApprovalAction() {
		super();
		orderApprovalSearchDateRangeField = "";
		// Added for jira 2770
		shipToSearchList = new LinkedHashMap();
		
	}
	
	

/*	public Map getSearchListNew() {
		searchListNew.put("- Select Search Criteria -","- Select Search Criteria -");
	    searchListNew.put("OrderNo", getText("approval.OrderNo"));
		searchListNew.put("OrderOwner", getText("order.Owner"));
		searchListNew.put("Approver", getText("approver"));
		return searchListNew;
	}*/
	
	public Map getSearchListNew() {
		//searchListNew.put("- Select Search Criteria -","- Select Search Criteria -");
		searchListNew.put(XPEDXConstants.DEFAULT_SELECT_SEARCH_CRITERIA_LABEL,XPEDXConstants.DEFAULT_SELECT_SEARCH_CRITERIA_LABEL);
	    searchListNew.put("OrderOwner", "Ordered By");
		searchListNew.put("PurchaseOrderNumberValue", "PO #");
		searchListNew.put("WebConfNumberValue", "Web Confirmation");
		
		return searchListNew;
	}

	@Override
	public String execute() {
		//getAssignedCustomerListForLoggedInUser();
		setInitialDates();
		String returnVal = super.execute();
		if(!ERROR.equalsIgnoreCase(returnVal))
			//call create only if the approval change is success
			if("1300".equals(getApprovalAction())){
				createFulfillmentOrder();// invoke this only on Approving the order
			}
		if(getApprovalActionRequestUrl()!=null && getApprovalActionRequestUrl().equals("orderList") && SUCCESS.equals(returnVal))
			returnVal = returnVal+"OrderList";
		return  returnVal;
	}
	
	/**
	 * This triggers a creation of fulfillment order on Approval.
	 */
	private void createFulfillmentOrder() {
		try {
			IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/Order/@OrderHeaderKey", getOrderHeaderKey());
		
			Element input = WCMashupHelper.getMashupInput(
					"xpedxCreateLegacyOrderOnApproval", valueMap, wcContext
							.getSCUIContext());
			String inputXml = SCXmlUtil.getString(input);
			
			log.debug("Input XML for Creating a Legacy order on Approval: " + inputXml);
			
			Object obj = WCMashupHelper.invokeMashup(
					"xpedxCreateLegacyOrderOnApproval", input, wcContext
							.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			if (null != outputDoc) {
				log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
			}
			
		} catch (XMLExceptionWrapper e) {
			LOG.error(" Error while creating a fulfillment order on Approval "+ e.getMessage(), e);
		} catch (CannotBuildInputException e) {
			LOG.error(" Error while creating a fulfillment order on Approval "+ e.getMessage(), e);
		}
	}



	/* (non-Javadoc)
	 * @see com.sterlingcommerce.webchannel.core.WCMashupAction#invokeMashup(java.lang.String, org.w3c.dom.Element)
	 */
	@Override
	protected Element invokeMashup(String mashupId, Element populatedMashupInput) throws CannotBuildInputException, XMLExceptionWrapper {
		if(null!=mashupId && mashupId.equals("orderApprovalList")){
			setDateRangeFieldSearch(populatedMashupInput);
		}
		return super.invokeMashup(mashupId, populatedMashupInput);
	}
	
	protected void setDateRangeFieldSearch(Element orderListInput) throws CannotBuildInputException
	{
	    Element orderElem = SCXmlUtils.getXpathElement(orderListInput, "API/Input/Order");
        if(!YFCCommon.isVoid(getOrderApprovalSearchDateRangeField())){
        	YDate startDate = YDate.newDate();
        	String startDateString = startDate.getString(YDate.ISO_DATETIME_FORMAT, wcContext.getSCUIContext().getUserPreferences().getLocale().getJLocale());
        	YFCDate endDate = startDate.getNewDate(-Integer.valueOf(getOrderApprovalSearchDateRangeField()).intValue());
        	String endDateString = endDate.getString(YDate.ISO_DATETIME_FORMAT, wcContext.getSCUIContext().getUserPreferences().getLocale().getJLocale());
        	SCXmlUtils.setAttribute(orderElem, "FromOrderDate", endDateString);
            SCXmlUtils.setAttribute(orderElem, "ToOrderDate", startDateString);
            SCXmlUtils.setAttribute(orderElem, "OrderDateQryType", "DATERANGE");
        }
	}
	
	public String getOrderApprovalSearchDateRangeField() {
		return orderApprovalSearchDateRangeField;
	}

	public void setOrderApprovalSearchDateRangeField(
			String orderApprovalSearchDateRangeField) {
		this.orderApprovalSearchDateRangeField = orderApprovalSearchDateRangeField;
	}
	
	protected void populateAccountIdList(HashMap<String, ArrayList<String>> allMap){
		List<String> billToListFromDB = allMap.get(XPEDXWCUtils.XPEDX_ASSIGNED_CUSTOMER_BILL_TO);
		setAccountIdList(XPEDXWCUtils.getHashMapFromList(billToListFromDB));
		log.debug("Assigned billTos***********=" + shipToList);
	}
	
	protected void populateShipToList(HashMap<String, ArrayList<String>> allMap){
		List<String> shipToListFromDB = allMap.get(XPEDXWCUtils.XPEDX_ASSIGNED_CUSTOMER_SHIP_TO);
		setShipToList(XPEDXWCUtils.getHashMapFromListWithLabel(shipToListFromDB));
		log.debug("Assigned shipTos***********=" + shipToList);
	}
	
	public boolean isOrderOnHold(Element OrderElement , String holdTypeToCheck) {
		boolean isOrderOnHold = false;
		if(OrderElement!=null && holdTypeToCheck!=null && holdTypeToCheck.trim().length()>0) {
			Element orderHoldTypesElem = SCXmlUtil.getChildElement(OrderElement,"OrderHoldTypes");
			ArrayList<Element> orderHoldTypeList = SCXmlUtil.getElements(orderHoldTypesElem, "OrderHoldType");
			if(orderHoldTypeList!=null && orderHoldTypeList.size()>0) {
				for(int i=0; i<orderHoldTypeList.size();i++) {
					Element orderHoldTypeElem = orderHoldTypeList.get(i);
					String holdType = SCXmlUtil.getAttribute(orderHoldTypeElem, "HoldType");
					String holdTypeStatus = SCXmlUtil.getAttribute(orderHoldTypeElem, "Status");
					if(holdType.equalsIgnoreCase(holdTypeToCheck) && holdTypeStatus.equalsIgnoreCase("1100"))
						isOrderOnHold = true;
				}
			}
		}
		return isOrderOnHold;
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
	
	@Override
	protected void manipulateMashupInputs(Map<String, Element> mashupInputs)
			throws CannotBuildInputException {
		Element orderApprovalElem = mashupInputs.get("orderApproval");
		if(orderApprovalElem!=null) {
			Element holdTypes = SCXmlUtil.getChildElement(orderApprovalElem, "OrderHoldTypes");
			Element holdType = SCXmlUtil.getChildElement(holdTypes, "OrderHoldType");
			holdType.setAttribute("HoldType", XPEDXConstants.HOLD_TYPE_FOR_PENDING_APPROVAL);
		}
		// TODO Auto-generated method stub
		super.manipulateMashupInputs(mashupInputs);
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
		return accountIdList;
	}

	/**
	 * @return the shipToList
	 */
	public HashMap<String,String> getShipToList() {
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
	private void setInitialDates(){
		YDate endDate = YDate.newDate();
    	initialToDateString = endDate.getString("MM/dd/yyyy", wcContext.getSCUIContext().getUserPreferences().getLocale().getJLocale());
    	YFCDate startDate = endDate.getNewDate(-30);
    	initialFromDateString = startDate.getString("MM/dd/yyyy", wcContext.getSCUIContext().getUserPreferences().getLocale().getJLocale());
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
	
	//JIRA 2770 - This will populate the ShipTo search combo box
	public Map getShipToSearchList() {
		
			shipToSearchList.put("",XPEDXConstants.DEFAULT_SELECT_SEARCH_SHIPTO_CRITERIA_LABEL1);
			shipToSearchList.put(1,XPEDXConstants.DEFAULT_SELECT_SEARCH_SHIPTO_CRITERIA_LABEL2);
		
		
		if(!SCUtil.isVoid(getShipToSearchFieldName())){
			shipToSearchList.put(getShipToSearchFieldName(),XPEDXWCUtils.formatBillToShipToCustomer(getShipToSearchFieldName()));
		}
		return shipToSearchList;
	}

	private String orderApprovalSearchDateRangeField;
	protected HashMap<String,String> accountIdList;
	protected HashMap<String,String> shipToList;
	protected String shipToSearchFieldName = XPEDXConstants.DEFAULT_SELECT_SHIP_TO_LABEL;
	protected String accountSearchFieldName;
	protected String initialFromDateString;
	protected String initialToDateString;
	private static final Logger log = Logger.getLogger(XPEDXOrderApprovalAction.class);
	Map searchListNew = new LinkedHashMap();
	protected String orderHeaderKey;
	protected String approvalAction;
	protected String approvalActionRequestUrl;
	
	//Added for JIRA 2770
	protected Map shipToSearchList;
	public void setShipToSearchList(Map shipToSearchList) {
		this.shipToSearchList = shipToSearchList;
	}



	/**
	 * @return the approvalAction
	 */
	public String getApprovalAction() {
		return approvalAction;
	}



	/**
	 * @param approvalAction the approvalAction to set
	 */
	public void setApprovalAction(String approvalAction) {
		this.approvalAction = approvalAction;
	}



	/**
	 * @return the orderHeaderKey
	 */
	public String getOrderHeaderKey() {
		return orderHeaderKey;
	}



	/**
	 * @param orderHeaderKey the orderHeaderKey to set
	 */
	public void setOrderHeaderKey(String orderHeaderKey) {
		this.orderHeaderKey = orderHeaderKey;
	}



	/**
	 * @return the approvalActionRequestUrl
	 */
	public String getApprovalActionRequestUrl() {
		return approvalActionRequestUrl;
	}



	/**
	 * @param approvalActionRequestUrl the approvalActionRequestUrl to set
	 */
	public void setApprovalActionRequestUrl(String approvalActionRequestUrl) {
		this.approvalActionRequestUrl = approvalActionRequestUrl;
	}
	
	
	
}

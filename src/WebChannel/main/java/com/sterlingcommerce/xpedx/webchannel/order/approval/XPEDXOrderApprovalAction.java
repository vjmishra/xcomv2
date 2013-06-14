package com.sterlingcommerce.xpedx.webchannel.order.approval;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.order.OrderConstants;
import com.sterlingcommerce.webchannel.order.OrderListAction;
import com.sterlingcommerce.webchannel.order.approval.OrderApprovalAction;
import com.sterlingcommerce.webchannel.utilities.BusinessRuleUtil;
import com.sterlingcommerce.webchannel.utilities.WCDataDeFormatHelper;
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
public class XPEDXOrderApprovalAction extends OrderListAction{
	
	private Boolean isFirstPage = Boolean.FALSE;
	private Boolean isLastPage = Boolean.FALSE;
	private Boolean isValidPage = Boolean.FALSE;
	private String orderByAttribute = "Modifyts";
	private String orderDesc = "Y";
	private String createTS="";
	private String modifyTS="";
	private String searchFieldName="";
    private String searchFieldValue="";
    private static String qryTypeFlike="FLIKE";
    private static String qryTypeLike="LIKE";
    private static String qryTypeEq="EQ";
	private Integer pageNumber = 1;
	private static Map searchList=new LinkedHashMap();
	private Map paraValueMap = new HashMap();
	private Integer totalNumberOfPages = new Integer(1);
	protected Document outputDoc = null;
	public String resourceId = "";
	public List<String> oUserList=new ArrayList<String>();
	public List<String> nameExp=new ArrayList<String>();
	public List<String> oOrderOwnerList=new ArrayList<String>();
    public List<String> oOrderOwnerNameExp=new ArrayList<String>();
    public List<String> userList=new ArrayList<String>();
    public List<String> userNameExp=new ArrayList<String>();
	public String sfId;
    private boolean isApprover = false;
    private boolean userAlwdForSrch = false;
    private String approvalHoldType = "";
    private String orderListReturnUrl = "";
    private String webConfNumberValue;
    private String extnWebConfNumQryType;
    // Added for Jira 3940
    private String returnWebConfUrl;

    public String getReturnWebConfUrl() {
		return returnWebConfUrl;
	}


	public void setReturnWebConfUrl(String returnWebConfUrl) {
		this.returnWebConfUrl = returnWebConfUrl;
	}

    private static final String XPATH_RESOLVER_USER_ID ="/Page/API/Input/Order/OrderHoldType/@ResolverUserId";
    private static final String XPATH_RESOLVER_USER_ID_QRY_TYPE ="/Page/API/Input/Order/OrderHoldType/@ResolverUserIdQryType";
    private static final String XPATH_ORDER_NO ="/Page/API/Input/Order/@OrderNo";
    private static final String XPATH_ORDER_NO_QRY_TYPE ="/Page/API/Input/Order/@OrderNoQryType";
    private static final String XPATH_CUSTOMER_CONTACT_ID="/Page/API/Input/Order/@CustomerContactID";
    private static final String XPATH_CUSTOMER_CONTACT_ID_QRY_TYPE="/Page/API/Input/Order/@CustomerContactIDQryType";
    
	private static final String APPROVAL_LIST_MASHUP_ID = "orderApprovalList";
	private static final String APPROVAL_ACTION_MASHUP_ID = "orderApproval";
	private static final String APPROVAL_LIST_WIDGET_MASHUP_ID = "orderApprovalListWidget";
	private static final String YES = "Y";
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
	    searchListNew.put("OrderNameValue", "Ordered By");
		searchListNew.put("PurchaseOrderNumberValue", "PO #");
		searchListNew.put("WebConfNumberValue", "Web Confirmation");
		
		return searchListNew;
	}

	
	public String execute() {
		//getAssignedCustomerListForLoggedInUser();
		setInitialDates();
		String returnVal = superExecute();
		if(!ERROR.equalsIgnoreCase(returnVal))
			//call create only if the approval change is success
			if("1300".equals(getApprovalAction())){
				createFulfillmentOrder();// invoke this only on Approving the order
			}
		if(getApprovalActionRequestUrl()!=null && getApprovalActionRequestUrl().equals("orderList") && SUCCESS.equals(returnVal))
			returnVal = returnVal+"OrderList";
		// Added for Jira 3940 - to display webCOnfirmation page after approve or reject, when request is from Webconfirmation page
		else if(getReturnWebConfUrl()!=null && ("true").equalsIgnoreCase(getReturnWebConfUrl())){
			returnVal = returnVal+"WebConf";
		}
		
		return  returnVal;
	}
	
	public String superExecute(){
		setApprovalHoldType(BusinessRuleUtil.getBusinessRule("HOLD_TO_BE_APPLIED_FOR_ORDER_APPROVAL" , wcContext));
		Document outDoc = null;
		//
		if(mashupIds.contains(APPROVAL_LIST_MASHUP_ID)||mashupIds.contains(APPROVAL_LIST_WIDGET_MASHUP_ID)){
			oUserList.add(wcContext.getCustomerContactId()+",");//Added for EB 70
			nameExp.add(OrderConstants.RESOLVER_USER_ID);
			if(mashupIds.contains("orderApprovalList")){
			outDoc = null;
			try {
				outDoc = prepareandCallMashupforApprovalList();
			} catch (CannotBuildInputException e) {
				// TODO Auto-generated catch block
				log.debug(e);
			}
			}
			else if(mashupIds.contains(APPROVAL_LIST_WIDGET_MASHUP_ID)){
				setIsApprover(true);
				outDoc = null;
				outDoc = callmashup(APPROVAL_LIST_WIDGET_MASHUP_ID);
				}
			}
			else if(mashupIds.contains(APPROVAL_ACTION_MASHUP_ID)){
			outDoc = null;
			outDoc = callmashup(APPROVAL_ACTION_MASHUP_ID);
			}
		if(outDoc!=null){
			try{
			if(mashupIds.contains(APPROVAL_LIST_MASHUP_ID)){
				parsePageInfo(outDoc, true);
			}
			setOutputDocument(outDoc);
			} catch (Exception e){
		          log.debug("XPathExpressionException", e);
			}
			return SUCCESS;
		}
		return ERROR;
	}
	
	/**
     * Method to set the pagination attirbutes.
     *
     * @param outDoc of type Document which is the ouput of the getCustomerList API call.
     * @param paginated of type boolean to know whether the pagination is to be invoked or not.
     * @return
     */
	private void parsePageInfo(Document outDoc, boolean paginated)
	throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        Element page = (Element) xpath.evaluate("//Page", outDoc, XPathConstants.NODE);

        setIsLastPage(Boolean.FALSE);
        if ((paginated) && (page != null)) {
        	setIsLastPage(getBooleanAttribute(page, "IsLastPage", getIsLastPage()));
        }

        setIsFirstPage( Boolean.FALSE);
        if ((paginated) && (page != null)) {
        	setIsFirstPage(getBooleanAttribute(page, "IsFirstPage", getIsFirstPage()));
         }

         setIsValidPage(Boolean.FALSE);
         if ((paginated) && (page != null)) {
        	 setIsValidPage(getBooleanAttribute(page, "IsValidPage", getIsValidPage()));
         }

         if ((paginated) && (page != null)) {
	         setPageNumber(getIntegerAttribute(page, "PageNumber", getPageNumber()));
         }

         setTotalNumberOfPages(new Integer(0));
         if ((paginated) && (page != null)) {
	         setTotalNumberOfPages(getIntegerAttribute(page, "TotalNumberOfPages", getTotalNumberOfPages()));
         }
	}
	
	
	//Method to be removed once the SCXmlUtils class provides this method.
	private static Boolean getBooleanAttribute(Element page, String name, Boolean defaultValue)
	{
		Boolean value = defaultValue;
		String str = page.getAttribute(name);
		if (str != null) {
			if (YES.equals(str)) {
				value = Boolean.TRUE;
			} else {
				value = Boolean.TRUE;
			}
		}

		return value;
	}
	
	//Method to be removed once the SCXmlUtils class provides this method.
	private Integer getIntegerAttribute(Element page, String name, Integer defaultValue)
	{
		Integer value = defaultValue;
		String str = page.getAttribute(name);
		try {
			value = Integer.valueOf(str);
		} catch (NumberFormatException e) {
			value = defaultValue;
		}

		return value;
	}

	/**
     * Common method for this action class to call any mashup by passing the mashuId.
     *
     * @param mashupCallId of the type String
     * @return outDoc of type Document which is the ouput of the mashup call.
     */


    private Document callmashup(String mashupCallId) {
        Set<String> mashupIdset = new HashSet<String> ();
        mashupIdset.add(mashupCallId);
        Map<String, Element> mashupOutput = null;
      try {
          mashupOutput = prepareAndInvokeMashups(mashupIdset);
      } catch (XMLExceptionWrapper e) {
          // TODO Auto-generated catch block
          log.debug(e);
      } catch (CannotBuildInputException e) {
          // TODO Auto-generated catch block
          log.debug(e);
      }
      if(mashupOutput!=null){
          return (mashupOutput.get(mashupCallId)).getOwnerDocument();
      }
      else{
          return null;
      }
    }
/**
 * Method to dynamically override the binding values for action approvalList.
 *
 * @param
 * @param outDoc of type Document which is the ouput of the getOrderList API call.
 * @return
 */

private Document prepareandCallMashupforApprovalList() throws CannotBuildInputException {
    Element input = WCMashupHelper.getMashupInput(APPROVAL_LIST_MASHUP_ID, wcContext);
    if ("ShowAll".equals(getSearchFieldName())){
        setSearchFieldName("");
        setSearchFieldName("");
        setPageNumber(1);
    }
    if ("Approver".equals(getSearchFieldName())){
        userAlwdForSrch  = checkUserAllowedForSearch(oUserList);
        if(!(YFCCommon.isVoid(getSearchFieldName()))){
            this.oUserList = new ArrayList<String>();
        }
    }
    Map<String, String> valueMapinput = evaluateBindingsForMashup(APPROVAL_LIST_MASHUP_ID, input);
    if ("Approver".equals(getSearchFieldName())){
        if(userAlwdForSrch){
            if(getSearchFieldName().startsWith("*")){
                valueMapinput.put(XPATH_RESOLVER_USER_ID_QRY_TYPE, qryTypeLike);
                if (getSearchFieldName().endsWith("*")){
                    valueMapinput.put(XPATH_RESOLVER_USER_ID, getSearchFieldName().substring(1, getSearchFieldName().length()-1));
                }
                else{
                    valueMapinput.put(XPATH_RESOLVER_USER_ID, getSearchFieldName().substring(1, getSearchFieldName().length()));
                }
            }else if (getSearchFieldName().endsWith("*")){
                valueMapinput.put(XPATH_RESOLVER_USER_ID_QRY_TYPE, qryTypeFlike);
                valueMapinput.put(XPATH_RESOLVER_USER_ID, getSearchFieldName().substring(0, getSearchFieldName().length()-1));
            } else {
                valueMapinput.put(XPATH_RESOLVER_USER_ID_QRY_TYPE, qryTypeEq);
                valueMapinput.put(XPATH_RESOLVER_USER_ID, getSearchFieldName());
            }
        }
        else if(!(YFCCommon.isVoid(getSearchFieldName()))){
            valueMapinput.put(XPATH_RESOLVER_USER_ID, "junk");
        }
    }
    else if ("OrderNo".equals(getSearchFieldName())){
        if(getSearchFieldName().startsWith("*")){
            valueMapinput.put(XPATH_ORDER_NO_QRY_TYPE, this.qryTypeLike);
            if (getSearchFieldName().endsWith("*")){
                valueMapinput.put(XPATH_ORDER_NO, getSearchFieldName().substring(1, getSearchFieldName().length()-1));
            }
            else{
                valueMapinput.put(XPATH_ORDER_NO, getSearchFieldName().substring(1, getSearchFieldName().length()));
            }
        }
        else if (getSearchFieldName().endsWith("*")){
            valueMapinput.put(XPATH_ORDER_NO_QRY_TYPE, this.qryTypeFlike);
            valueMapinput.put(XPATH_ORDER_NO, getSearchFieldName().substring(0, getSearchFieldName().length()-1));
        } else {
            valueMapinput.put(XPATH_ORDER_NO_QRY_TYPE, this.qryTypeEq);
            valueMapinput.put(XPATH_ORDER_NO, getSearchFieldName());
        }
    }else if ("OrderOwner".equals(getSearchFieldName())){
        if(getSearchFieldName().startsWith("*")){
            valueMapinput.put(XPATH_CUSTOMER_CONTACT_ID_QRY_TYPE, this.qryTypeLike);
            if (getSearchFieldName().endsWith("*")){
                valueMapinput.put(XPATH_CUSTOMER_CONTACT_ID, getSearchFieldName().substring(1, getSearchFieldName().length()-1));
            }
            else{
                valueMapinput.put(XPATH_CUSTOMER_CONTACT_ID, getSearchFieldName().substring(1, getSearchFieldName().length()));
            }
        }
        else if (getSearchFieldName().endsWith("*")){
            valueMapinput.put(XPATH_CUSTOMER_CONTACT_ID_QRY_TYPE, this.qryTypeFlike);
            valueMapinput.put(XPATH_CUSTOMER_CONTACT_ID, getSearchFieldName().substring(0, getSearchFieldName().length()-1));
        } else {
            valueMapinput.put(XPATH_CUSTOMER_CONTACT_ID_QRY_TYPE, this.qryTypeEq);
            valueMapinput.put(XPATH_CUSTOMER_CONTACT_ID, getSearchFieldName());
        }
    }
    populateMashupInput(APPROVAL_LIST_MASHUP_ID, valueMapinput, input);
    Document approvalListOutdoc = (invokeMashup(APPROVAL_LIST_MASHUP_ID, input)).getOwnerDocument();
    return approvalListOutdoc;
}

private boolean checkUserAllowedForSearch(List<String> list) {
    for(String s : list){
        if(getSearchFieldName().equals(s)){
            return true;
        }
        else if(getSearchFieldName().startsWith("*")&& getSearchFieldName().endsWith("*")){
            String substring =getSearchFieldName().substring(1, getSearchFieldName().length()-1);
            if(s.indexOf(substring)!=-1){
                return true;
            }
        }
        else if(getSearchFieldName().startsWith("*")){
            String substring =getSearchFieldName().substring(1, getSearchFieldName().length());
            if(s.indexOf(substring)!=-1){
                return true;
            }
        }
        else if(getSearchFieldName().endsWith("*")){
            String substring =getSearchFieldName().substring(0, getSearchFieldName().length()-1);
            if(s.indexOf(substring)!=-1){
                return true;
            }
        }
    }
    return false;
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
	
	protected Element invokeMashup(String mashupId, Element populatedMashupInput) throws CannotBuildInputException, XMLExceptionWrapper {
		if(null!=mashupId && mashupId.equals("orderApprovalList")){
			setDateRangeFieldSearch(populatedMashupInput);
		}
		return super.invokeMashup(mashupId, populatedMashupInput);
	}
	
	protected void setDateRangeFieldSearch(Element orderListInput) throws CannotBuildInputException
	{
		Element orderElem = SCXmlUtils.getXpathElement(orderListInput, "API/Input/Order");
		if(!YFCCommon.isVoid(getSubmittedTSFrom()))
            orderElem.setAttribute("FromOrderDate", WCDataDeFormatHelper.getDeformattedDate(getWCContext().getSCUIContext(), getSubmittedTSFrom()));
        if(!YFCCommon.isVoid(getSubmittedTSTo()))
        	orderElem.setAttribute("ToOrderDate", WCDataDeFormatHelper.getDeformattedDate(getWCContext().getSCUIContext(), getSubmittedTSTo()));        
        
	    
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
	
	public Document getOutputDocument()
	{
    	return this.outputDoc;
	}

	public void setOutputDocument(Document outputDocument)
	{
		this.outputDoc = outputDocument;
	}

	public void setRequestMap(Map map)
	{
			this.paraValueMap = map;
	}

	public Map getRequestMap()
	{
			return this.paraValueMap;
	}

	public String getOrderByAttribute()
	{
        return orderByAttribute;
	}

	public void setOrderByAttribute(String orderByAttribute)
	{
        this.orderByAttribute = orderByAttribute;
	}

	public Boolean getIsLastPage() {
		return isLastPage;
	}

	public void setIsLastPage(Boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Boolean getIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(Boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
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
	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public String getCreateTS(){
		return createTS;
	}
	public void setCreateTS(String createTS) {
		this.createTS=createTS;
	}
	public String getModifyTS(){
		return modifyTS;
	}
	public void setModifyTS(String modifyTS) {
		this.modifyTS=modifyTS;
	}

	public Date getDate(String dateStr){
    	return new Date(dateStr);
    }
	public void setSfId(String sfId) {
		this.sfId = sfId;
	}

	public String getSfId() {
		return sfId;
	}

	public String getSearchFieldName()
    {
        return this.searchFieldName;
    }
    public void setSearchFieldName(String searchFieldName)
    {
        this.searchFieldName=searchFieldName;
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
    public String getSearchFieldValue(){
        return this.searchFieldValue;
    }
    public void setSearchFieldValue(String searchFieldValue)
    {
        
        if("OrderNumberValue".equals(getSearchFieldName()))
        	setOrderNumberValue(searchFieldValue);
        else
        if("OrderNameValue".equals(getSearchFieldName()))
        	setOrderNameValue(searchFieldValue);
        else if("PurchaseOrderNumberValue".equals(getSearchFieldName()))
            setPurchaseOrderNumberValue(searchFieldValue);
       
        else if("WebConfNumberValue".equals(getSearchFieldName()))
            setWebConfNumberValue(searchFieldValue);
        
        this.searchFieldValue=searchFieldValue;
    }
	public Map getSearchList()
    {
        searchList.put("OrderNo", getText("approval.OrderNo"));
        searchList.put("OrderOwner", getText("order.Owner"));
        searchList.put("Approver", getText("approver"));
        return this.searchList;
    }

    public List<String> getOUserList() {
        return oUserList;
    }

    public List<String> getNameExp() {
        return nameExp;
    }

    public List<String> getOOrderOwnerList() {
        return oOrderOwnerList;
    }

    public List<String> getOOrderOwnerNameExp() {
        return oOrderOwnerNameExp;
    }

    public boolean getIsApprover() {
        return isApprover;
    }



    public void setIsApprover(boolean isApprover) {
        this.isApprover = isApprover;
    }

    public String getApprovalHoldType() {
        return approvalHoldType;
    }

    public void setApprovalHoldType(String approvalHoldType) {
        this.approvalHoldType = approvalHoldType;
    }

    public List<String> getUserList() {
        this.userList.add(wcContext.getCustomerContactId());
        return userList;
    }
    public List<String> getUserNameExp() {
        this.userNameExp.add("ApproverProxyUserId");
        return userNameExp;
    }

    public String getOrderListReturnUrl() {
        return orderListReturnUrl;
    }

    public void setOrderListReturnUrl(String orderListReturnUrl) {
        this.orderListReturnUrl = orderListReturnUrl;
    }
	
}

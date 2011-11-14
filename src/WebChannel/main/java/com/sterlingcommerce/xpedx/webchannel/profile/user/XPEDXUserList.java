package com.sterlingcommerce.xpedx.webchannel.profile.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.profile.ProfileUtility;
import com.sterlingcommerce.webchannel.profile.user.UserList;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

public class XPEDXUserList extends UserList{

	//Logger
    private static final Logger log = Logger.getLogger(UserList.class);
    
    //mashup
    private static final String GET_USER_LIST_MASHUP = "getXpedxUserList";
    private static final String GET_CUSTOMER_BUYER_ORG_MASHUP = "getBuyerOrganizationForCustomer";
    
    //Sort fields and parameters
    private static final String SORT_BY_LAST_NAME = "LastName";
    private static final String SORT_BY_FIRST_NAME = "FirstName";
    private static final String SORT_BY_EMAILID = "EmailID";
    private static final String SORT_BY_PHONENO = "DayPhone";
  //Sort fields and parameters
    private static final String SORT_BY_LAST_NAME_DESC = "Search_Last_Name";
    private static final String SORT_BY_FIRST_NAME_DESC = "Search_First_Name";
    private static final String SORT_BY_EMAILID_DESC = "Search_Email_ID";
    private static final String SORT_BY_PHONENO_DESC = "Search_Day_Phone";
 
    
    //Search fields and parameters
    private static final String SEARCH_BY_LAST_NAME = "LastName";
    private static final String SEARCH_BY_FIRST_NAME = "FirstName";
    private static final String SEARCH_BY_EMAILID = "EmailID";
    private static final String SEARCH_BY_PHONENO = "DayPhone";
    private static final String SEARCH_FIRST = "FLIKE";
    private static final String SEARCH_EXACT = "EQ";
    private static final String SEARCH_CONTAINS = "LIKE";
    private static final String SEARCH_NO_CRITERIA = "noCriteria";
    private static final String DEFAULT_PAGE_SIZE = "7";
    private static final String DEFAULT_PAGE_SIZE_PARAM = "pageSizeParam";
	
	 public String execute() {
		 String inputCustomerID = getCustomerID();
	        if(YFCCommon.isVoid(inputCustomerID)){
	            inputCustomerID = wcContext.getCustomerId();
	        }
	        if(!(isCustInCtxCustHierarchy(inputCustomerID, wcContext))){
	            return ERROR;
	        }
	        // I don't see this is getting used in jsp or in this action if any one feels it is required please customize setCustomerAccess method to call only one api
	       // setIsChildCustomer(ProfileUtility.setCustomerAccess(getCustomerID(), wcContext));
	        //getBuyerOrgCodeFromCustID();
	        if(YFCCommon.isVoid(getBuyerOrgCode())){
	            log.error("No buyer organization information associated with customer : " + getCustomerID());
	            return ERROR;
	        }
	        return searchUser();
	 }
	 
	 
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
			 if(customerContactList !=null && customerContactList.size() >1)
			 {
				 Element loggedinCustomerDetails=customerContactList.get(0);
				 Element inputCustomerDetails=customerContactList.get(1);
				 String inputCustRootCustomerKey = inputCustomerDetails.getAttribute("RootCustomerKey");//SCXmlUtils.getAttribute(inputCustomerDetails, "RootCustomerKey");
				 String inputCustomerId=inputCustomerDetails.getAttribute("CustomerID");//SCXmlUtils.getAttribute(inputCustomerDetails, "CustomerID");
				 Element buyerOrganizationElem=null;
				 if(inputCustomerID.equals(inputCustomerId))
				 {
					 buyerOrganizationElem=(Element)inputCustomerDetails.getElementsByTagName("BuyerOrganization").item(0);
					 
				 }
				 else
				 {
					 buyerOrganizationElem=(Element)loggedinCustomerDetails.getElementsByTagName("BuyerOrganization").item(0);
				 }
				 if(buyerOrganizationElem !=null)					 
				 {
					 String buyerOrgCode =buyerOrganizationElem.getAttribute("OrganizationCode");
			           setBuyerOrgCode(buyerOrgCode);
			           if(!(YFCCommon.isVoid(buyerOrgCode))){
			               setBuyerOrgName(buyerOrganizationElem.getAttribute("OrganizationName"));
			           }
				 }
				 String logInCustRootCustomerKey = SCXmlUtils.getAttribute(loggedinCustomerDetails, "RootCustomerKey");
				 if(inputCustRootCustomerKey != null && logInCustRootCustomerKey != null && inputCustRootCustomerKey.equals(logInCustRootCustomerKey))
					 retVal= true;
				 else
					 retVal= false;
			 }
			 else
			 {
				 retVal= false;
			 }
		 }
		 catch(Exception e)
		 {
			 log.info("error while validating user"+e.getMessage());
		 }
		 return retVal;
	 }
	 
	 /**
	     * Method to get the Buyer-Organization details dynamically from the CustomerID
	     *  
	     * @param 
	     * @return 
	     */ 
	    
	    private void getBuyerOrgCodeFromCustID() {
	        Element customerOutput = null;
	        try {
	            customerOutput = prepareAndInvokeMashup(GET_CUSTOMER_BUYER_ORG_MASHUP);
	        } catch (XMLExceptionWrapper e) {
	           log.error("Error in getting Buyer Organization for the customer: " + getCustomerID(), e.getCause());
	        } catch (CannotBuildInputException e) {
	            log.error("Error in getting Buyer Organization for the customer: " + getCustomerID(), e.getCause());
	        }
	        if(customerOutput!=null){
	           Element buyerOrgElem = SCXmlUtils.getChildElement(customerOutput, "BuyerOrganization");
	           String buyerOrgCode = SCXmlUtils.getAttribute(buyerOrgElem, "OrganizationCode");
	           setBuyerOrgCode(buyerOrgCode);
	           if(!(YFCCommon.isVoid(buyerOrgCode))){
	               setBuyerOrgName(buyerOrgElem.getAttribute("OrganizationName"));
	           }
	        }
	    }
	    
	    public String searchUser() {
	        setPageSize(wcContext.getSCUIContext().getRequest().getParameter(DEFAULT_PAGE_SIZE_PARAM));
	        if(YFCCommon.isVoid(getPageSize())){
	        	setPageSize(DEFAULT_PAGE_SIZE);
	        }
	        try {
	            if (getOrderByAttribute() == null) {
	                setOrderByAttribute("LastName");
	            }
	            getContactlistAPIInput();
	            getSearchAPIInput();

	            setUserList(prepareAndInvokeMashup(GET_USER_LIST_MASHUP));
	            
	           NodeList userList=SCXmlUtil.getXpathNodes(getUserList(), "Output/CustomerContactList/CustomerContact");			   
	           for(int i=0;i<userList.getLength();i++){
	        	   Element elem=(Element) userList.item(i);
	        	   String customerContactId=elem.getAttribute("CustomerContactID");
	        	   List<Element> userGroupList=SCXmlUtils.getElements(elem, "/User/UserGroupLists/UserGroupList");	
	        	   boolean isAdmin=false;
	        	   if(userGroupList != null)
	        	   {
		        	   for(Element elemUserGroupList : userGroupList){
		        		   String usergroupKey=elemUserGroupList.getAttribute("UsergroupKey");
		        		   if("BUYER-ADMIN".equals(usergroupKey))
		        		   {
		        			   isAdmin=true;
		        		   }
		        	   }
	        	   }
	        	   isAdminMap.put(customerContactId, isAdmin);
				}
	            try {
	                parsePageInfo(getUserList(), true);
	            } catch (Exception e) {
	                log.error("Error in getting the User List for Customer : " + getCustomerID(), e.getCause()); 
	            }
	        } catch (XMLExceptionWrapper e) {
	            log.error("Error in getting the User List for Customer : " + getCustomerID(), e.getCause()); 
	        } catch (CannotBuildInputException e) {
	            log.error("Error in getting the User List for Customer : " + getCustomerID(), e.getCause()); 
	        }
	        return SUCCESS;
	    }
	    
	    /**
	     * This method returns the Mashup API Input variables to get Customer
	     * Contact List
	     * 
	     * @param organizationCode
	     * @return the map of Mashup API Inputs
	     */
	    private void getContactlistAPIInput() {
	        // for sorting
	        if ((getOrderByAttribute() == null)
	                || (getOrderByAttribute().equalsIgnoreCase("null"))) {
	            setOrderByAttribute(SORT_BY_LAST_NAME);
	            setOrderDesc("N");
	        }
	        if (SORT_BY_LAST_NAME.equals(getOrderByAttribute())) {
	            setOrderByAttribute(SORT_BY_LAST_NAME);
	        } else if (SORT_BY_FIRST_NAME.equals(getOrderByAttribute())) {
	            setOrderByAttribute(SORT_BY_FIRST_NAME);
	        } else if (SORT_BY_EMAILID.equals(getOrderByAttribute())) {
	            setOrderByAttribute(SORT_BY_EMAILID);
	        } else if (SORT_BY_PHONENO.equals(getOrderByAttribute())) {
	            setOrderByAttribute(SORT_BY_PHONENO);
	        }
	    }
	    
	    /**
	     * This method returns the Mashup API Input variables to get Customer
	     * Contact List
	     * 
	     * @param 
	     * @return the map of Mashup API Inputs
	     */
	    private void getSearchAPIInput() {
	        if (!("*".equalsIgnoreCase(getSearchValue()))) {
	            // for searching
	            if (!(SEARCH_NO_CRITERIA.equals(getSearchCriteria()))) {
	                if (SEARCH_BY_LAST_NAME.equals(getSearchCriteria())) {

	                    if (getQryType(getSearchValue()).equals(SEARCH_FIRST)) {
	                        setLastNameQryType(SEARCH_FIRST);
	                        setLastName(getSearchValue().substring(0,
	                                getSearchValue().length() - 1));
	                    } else if (getQryType(getSearchValue()).equals(
	                            SEARCH_CONTAINS)) {
	                        setLastNameQryType(SEARCH_CONTAINS);
	                        setLastName(getSearchValue().substring(1,
	                                getSearchValue().length() - 1));
	                    } else {
	                        setLastNameQryType(SEARCH_EXACT);
	                        setLastName(getSearchValue());
	                    }
	                } else if (SEARCH_BY_FIRST_NAME.equals(getSearchCriteria())) {
	                    if (getQryType(getSearchValue()).equals(SEARCH_FIRST)) {
	                        setFirstNameQryType(SEARCH_FIRST);
	                        setFirstName(getSearchValue().substring(0,
	                                getSearchValue().length() - 1));
	                    } else if (getQryType(getSearchValue()).equals(
	                            SEARCH_CONTAINS)) {
	                        setFirstNameQryType(SEARCH_CONTAINS);
	                        setFirstName(getSearchValue().substring(1,
	                                getSearchValue().length() - 1));

	                    } else {
	                        setFirstNameQryType(SEARCH_EXACT);
	                        setFirstName(getSearchValue());
	                    }
	                } else if (SEARCH_BY_EMAILID.equals(getSearchCriteria())) {
	                    if (getQryType(getSearchValue()).equals(SEARCH_FIRST)) {
	                        setEmailIDQryType(SEARCH_FIRST);
	                        setEmailID(getSearchValue().substring(0,
	                                getSearchValue().length() - 1));
	                    } else if (getQryType(getSearchValue()).equals(
	                            SEARCH_CONTAINS)) {
	                        setEmailIDQryType(SEARCH_CONTAINS);
	                        setEmailID(getSearchValue().substring(1,
	                                getSearchValue().length() - 1));
	                    } else {
	                        setEmailIDQryType(SEARCH_EXACT);
	                        setEmailID(getSearchValue());
	                    }

	                } else if (SEARCH_BY_PHONENO.equals(getSearchCriteria())) {
	                    if (getQryType(getSearchValue()).equals(SEARCH_FIRST)) {
	                        setDayPhoneQryType(SEARCH_FIRST);
	                        setDayPhone(getSearchValue().substring(0,
	                                getSearchValue().length() - 1));
	                    } else if (getQryType(getSearchValue()).equals(
	                            SEARCH_CONTAINS)) {
	                        setDayPhoneQryType(SEARCH_CONTAINS);
	                        setDayPhone(getSearchValue().substring(1,
	                                getSearchValue().length() - 1));
	                    } else {
	                        setDayPhoneQryType(SEARCH_EXACT);
	                        setDayPhone(getSearchValue());
	                    }
	                }
	            }
	        }
	    }


	    
	    /**
	     * This method sets the pagination parameters
	     * 
	     * @param ele : Element of User List
	     * @param paginated 
	     * @return 
	     */
	    
	    private void parsePageInfo(Element ele, boolean paginated) throws Exception {

	        UtilBean util = new UtilBean();
	        Element page = util.getElement(ele, "//Page");

	        setIsLastPage(Boolean.FALSE);
	        if ((paginated) && (page != null)) {
	        	setIsLastPage(getBooleanAttribute(page, "IsLastPage", getIsLastPage()));
	        }

	        setIsFirstPage(Boolean.FALSE);
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
	        	setTotalNumberOfPages(getIntegerAttribute(page,
	                    "TotalNumberOfPages", getTotalNumberOfPages()));
	        }
	    }
	    
	    /**
	     * Method to get query type for the search
	     *  
	     * @param 
	     * @return qryType of type string
	     */
	    
	    private String getQryType(String qry) {
	        String qryType = SEARCH_EXACT;
	        if (qry != null) {
	            if (qry.startsWith("*")) {
	                if (qry.endsWith("*")) {

	                    qryType = SEARCH_CONTAINS;
	                }
	            } else {
	                if (qry.endsWith("*")) {
	                    qryType = SEARCH_FIRST;
	                }
	            }
	        }
	        return qryType;
	    }
	    

	    //TODO Method should be removed and SCXMLUtils method should be used
	      private static Boolean getBooleanAttribute(Element page, String name,
	              Boolean defaultValue) {
	          Boolean value = defaultValue;
	          String str = page.getAttribute(name);
	          if (str != null) {
	              if ("Y".equals(str)) {
	                  value = Boolean.TRUE;
	              } else {
	                  value = Boolean.TRUE;
	              }
	          }
	          return value;
	      }
	      
	    //TODO Method should be removed and SCXMLUtils method should be used
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
	   
	   
	      
	   public Map<String, Boolean> getIsAdminMap() {
			return isAdminMap;
		}

		public void setIsAdminMap(Map<String, Boolean> isAdminMap) {
			this.isAdminMap = isAdminMap;
		}



	Map<String ,Boolean> isAdminMap=new HashMap<String ,Boolean>();

}

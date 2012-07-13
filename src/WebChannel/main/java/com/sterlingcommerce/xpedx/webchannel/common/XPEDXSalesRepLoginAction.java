package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.salesrep.XPEDXSalesRepUtils;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

public class XPEDXSalesRepLoginAction extends WCAction implements ServletResponseAware {

	private static final long serialVersionUID = -7609560200703916048L;
	
	private static final Logger LOG = Logger.getLogger(XPEDXSalesRepLoginAction.class);
	
	private String logoutMethod = null;
	private String DisplayUserID = null;
	private String Password = null;
	private String customerSearchText;
	private String networkId;
    protected HttpServletResponse response = null;
    //added for jira 3442
    protected  Integer pageNumber = 1;
    private Boolean isFirstPage = Boolean.FALSE;
    private Boolean isLastPage = Boolean.FALSE;
    private Boolean isValidPage = Boolean.FALSE;
    private Integer totalNumberOfPages = new Integer(1);
    protected Integer pageSize = 25;
    
    public  Integer getPageNumber() {
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

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	
	@SuppressWarnings("unchecked")
	public String getCustomersForSalesRep() {
		
		LOG.debug(":: Entering execute of XPEDXSalesRepLoginAction :: ");
		LOG.debug(":: Logged In User :: " + DisplayUserID);
		Document doc=null;
		String result = WCAction.SUCCESS;
		LinkedHashMap<String, String> customersMapToSearch = new LinkedHashMap<String, String>();
		setRequiredAttrbutes();
		try {
			networkId = request.getParameter("DisplayUserID");
			String searchText=request.getParameter("searchText");
			String command=request.getParameter("command");
			if("search".equals(command)){
				if(!"".equals(searchText) && !"Search for Customer".equals(searchText)){
					//added for jira 3442 - sales rep search
					Document outputDoc = null;
					outputDoc = getPaginatedSalesRepCustomersSearch(wcContext,request);
					customersMapToSearch = (new XPEDXSalesRepUtils()).searchCustomerForSalesRep(request, wcContext,outputDoc);
					parsePageInfo(outputDoc.getDocumentElement(), true);
					//end of modification 3442
					LinkedHashMap<String, String> searchedMap = new LinkedHashMap<String, String>();
					 if(customersMapToSearch !=null){
    					for(String customerId : customersMapToSearch.keySet()){
    						String customerName = customersMapToSearch.get(customerId);
    						if(searchText.equalsIgnoreCase(customerId)
    								||searchText.equalsIgnoreCase(customerName)
    								|| customerName.toUpperCase().contains(searchText.toUpperCase())
    								|| customerId.toUpperCase().contains(searchText.toUpperCase())){
    							searchedMap.put(customerId, customerName);
    						}
    					}
    					if(searchedMap.isEmpty()){// TODO: show on UI
    						LOG.debug("Search returned no results!!!");
    					}
					 }
					wcContext.getSCUIContext().getRequest().setAttribute("SEARCHED_CUSTOMERS",  searchedMap);
					wcContext.getSCUIContext().getRequest().setAttribute("RESULT_COMMAND",  "searched");
				}
				//modified for jira 3442 - for customer search and pagination
				else{
//					wcContext.getSCUIContext().getRequest().setAttribute("SEARCHED_CUSTOMERS",  customersMap);
					doc = getPaginatedSalesRepCustomersDocument(wcContext);
					(new XPEDXSalesRepUtils()).fetchCustomerForSalesRep(request, wcContext,doc);
					parsePageInfo(doc.getDocumentElement(), true);
				}
			}
			else{
				//(new XPEDXSalesRepUtils()).fetchCustomers(request, wcContext);
				doc = getPaginatedSalesRepCustomersDocument(wcContext);
				(new XPEDXSalesRepUtils()).fetchCustomerForSalesRep(request, wcContext,doc);
				parsePageInfo(doc.getDocumentElement(), true);
				//end of changes for jira 3442
			}
		} catch (Exception e) {
			LOG.error("Error while retrieving the MSAP Customers for the Sales Rep: "+ e.getMessage(), e);
			result = WCAction.ERROR;
		}
    	return result;
	}
	
	public String logoutSalesRep() {
		System.out.println(":: Entering logoutSalesRep() ");
		System.out.println(":: User is currently logged out...");
		if (request == null || response == null){			
				return WCAction.SUCCESS;			
		}
		Cookie cookies [] = request.getCookies();
		if (cookies != null)
		{
			for (int i = 0; i < cookies.length; i++) 
			{
				cookies[i].setMaxAge(0);
				cookies[i].setPath("/");
				response.addCookie(cookies[i]);
			}
		}
		request.setAttribute("SCUI_SKIP_ADDING_ADDITIONAL_PARAMETERS", "Y");
		request.getSession(false).invalidate();
		request.getSession().invalidate();
			return WCAction.SUCCESS;
		
	}
	
	private void setRequiredAttrbutes(){
		String sessionUserName = (String)request.getSession(false).getAttribute("loggedInUserName");
		String sessionUserId = (String)request.getSession(false).getAttribute("loggedInUserId");
		String sessionSREmailID = (String)request.getSession(false).getAttribute("SRSalesRepEmailID");
		String requestUserName = (String)request.getAttribute("loggedInUserName");
		String requestUserId = (String)request.getAttribute("loggedInUserId");
		String requestSREmailID = (String)request.getAttribute("SRSalesRepEmailID");
		
		if (sessionUserName == null  && requestUserName == null){
			request.getSession(false).setAttribute("IS_SALES_REP", "true");
			request.getSession(false).setAttribute("loggedInUserId",DisplayUserID);
		}
		else if (requestUserName != null){
			request.getSession(false).setAttribute("IS_SALES_REP", "true");
			request.getSession(false).setAttribute("loggedInUserId",DisplayUserID);
			request.getSession(false).setAttribute("loggedInUserName",requestUserName);
			request.getSession(false).setAttribute("SRSalesRepEmailID",requestSREmailID);
		}

	}
	//added for jira 3442
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

	//added for jira 3442	
public Document getPaginatedSalesRepCustomersDocument(IWCContext context) {
		
		String customerContactId = context.getLoggedInUserId();
		String pageNo = getPageNumber().toString();
		String pageSize = getPageSize().toString();
		Document outputElemDoc;
		Map<String,String> valueMap = new HashMap<String,String>();
		valueMap.put("/Page/API/Input/XPXSalesRepCustomers/@UserID", customerContactId);
		valueMap.put("/Page/@PageNumber",pageNo);
		valueMap.put("/Page/@PageSize", pageSize);
		Element outputElem;
		try {
			Element input = WCMashupHelper.getMashupInput("XPXGetPaginatedSalesRepCustomersList",context.getSCUIContext());
			input.setAttribute("PageNumber", pageNo);
			input.setAttribute("PageSize", pageSize);
			Element pageElement=(Element)input.getElementsByTagName("XPXSalesRepCustomers").item(0);
			pageElement.setAttribute("UserID", customerContactId);

			outputElem = (Element) WCMashupHelper.invokeMashup("XPXGetPaginatedSalesRepCustomersList",
					input, context.getSCUIContext());
			outputElemDoc = outputElem.getOwnerDocument();
		} catch (XMLExceptionWrapper e) {
			LOG.error("Error getting the Customers Assigned to sales rep : "+e.getMessage());
			return null;
		} catch (CannotBuildInputException e) {
			LOG.error("Error getting the Customers Assigned to sales rep : "+e.getMessage());
			return null;
		}
		catch (Exception e) {
			LOG.error("Error getting the Customers Assigned to sales rep : "+e.getMessage());
			return null;
		}
		return outputElemDoc;
		
	}

public Document getPaginatedSalesRepCustomersSearch(IWCContext context, HttpServletRequest request) {
	
	String customerContactId = context.getLoggedInUserId();
	String salesRepCustomerSearchString = request.getParameter("searchText");
	String pageNo = getPageNumber().toString();
	String pageSize = getPageSize().toString();
	Document outputElemDoc;
	Element outputElem;
	try {
		Element input = WCMashupHelper.getMashupInput("XPXGetSalesRepCustomerSearchList",  context.getSCUIContext());
		input.setAttribute("PageNumber", pageNo);
		input.setAttribute("PageSize", pageSize);
		Element pageElement=(Element)input.getElementsByTagName("XPXSalesRepCustomers").item(0);
		pageElement.setAttribute("UserID", customerContactId);
		Element orElement=(Element)input.getElementsByTagName("Or").item(0);
		Element expElement=SCXmlUtil.createChild(orElement, "Exp");
		expElement.setAttribute("Name", "ExtnCustomerNo");
		expElement.setAttribute("Value", salesRepCustomerSearchString);
		expElement.setAttribute("QryType", "LIKE");
		Element exp1Element=SCXmlUtil.createChild(orElement, "Exp");
		exp1Element.setAttribute("Name", "ExtnCustomerName");
		exp1Element.setAttribute("Value", salesRepCustomerSearchString);
		exp1Element.setAttribute("QryType", "LIKE");
		outputElem = (Element) WCMashupHelper.invokeMashup("XPXGetSalesRepCustomerSearchList", input, context.getSCUIContext());
		outputElemDoc = outputElem.getOwnerDocument();
	} catch (XMLExceptionWrapper e) {
		LOG.error("Error getting the Customer Assigned to sales rep"+e.getMessage());
		return null;
	} catch (CannotBuildInputException e) {
		LOG.error("Error getting the Customer Assigned to sales rep"+e.getMessage());
		return null;
	}
	catch (Exception e) {
		LOG.error("Error getting the Customer Assigned to sales rep"+e.getMessage());
		return null;
	}
	return outputElemDoc;
	
}
	//end of jira 3442

	public String getLogoutMethod() {
		return logoutMethod;
	}

	public void setLogoutMethod(String logoutMethod) {
		this.logoutMethod = logoutMethod;
	}
	
	public String getCustomerSearchText() {
		return customerSearchText;
	}

	public void setCustomerSearchText(String customerSearchText) {
		this.customerSearchText = customerSearchText;
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	/**
	 * @return the displayUserID
	 */
	public String getDisplayUserID() {
		return DisplayUserID;
	}

	/**
	 * @param displayUserID the displayUserID to set
	 */
	public void setDisplayUserID(String displayUserID) {
		DisplayUserID = displayUserID;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return Password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		Password = password;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}
	
	
}

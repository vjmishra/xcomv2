package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.salesrep.XPEDXSalesRepUtils;

public class XPEDXSalesRepLoginAction extends WCAction implements ServletResponseAware {

	private static final long serialVersionUID = -7609560200703916048L;
	
	private static final Logger LOG = Logger.getLogger(XPEDXSalesRepLoginAction.class);
	
	private String logoutMethod = null;
	private String DisplayUserID = null;
	private String Password = null;
	private String customerSearchText;
	private String networkId;
    protected HttpServletResponse response = null;

	
	public String getCustomersForSalesRep() {
		
		LOG.info(":: Entering execute of XPEDXSalesRepLoginAction :: ");
		LOG.info(":: Logged In User :: " + DisplayUserID);
		
		String result = WCAction.SUCCESS;
		Map<String, String> customersMap = new HashMap<String, String>();
		setRequiredAttrbutes();
		try {
			networkId = request.getParameter("DisplayUserID");
			String searchText=request.getParameter("searchText");
			String command=request.getParameter("command");
			if("search".equals(command)){
				customersMap = (HashMap<String, String>)wcContext.getSCUIContext().getSession().getAttribute("ASSIGNED_CUSTOMERS");
				if(!"".equals(searchText) && !"Search for Customer".equals(searchText)){
					 Map<String, String> searchedMap = new HashMap<String, String>();
					 if(customersMap !=null){
    					for(String customerId : customersMap.keySet()){
    						String customerName = customersMap.get(customerId);
    						if(searchText.equalsIgnoreCase(customerId)
    								||searchText.equalsIgnoreCase(customerName)
    								|| customerName.toUpperCase().contains(searchText.toUpperCase())
    								|| customerId.toUpperCase().contains(searchText.toUpperCase())){
    							searchedMap.put(customerId, customerName);
    						}
    					}
    					if(searchedMap.isEmpty()){// TODO: show on UI
    						LOG.info("Search returned no results!!!");
    					}
					 }
					wcContext.getSCUIContext().getRequest().setAttribute("SEARCHED_CUSTOMERS",  searchedMap);
					wcContext.getSCUIContext().getRequest().setAttribute("RESULT_COMMAND",  "searched");
				}
				else{
					wcContext.getSCUIContext().getRequest().setAttribute("SEARCHED_CUSTOMERS",  customersMap);
				}
			}
			else{
				//(new XPEDXSalesRepUtils()).fetchCustomers(request, wcContext);
				(new XPEDXSalesRepUtils()).fetchCustomerForSalesRep(request, wcContext);
			}
		} catch (Exception e) {
			LOG.error("Error while retrieving the MSAP Customers for the Sales Rep: "+ e.getMessage(), e);
			result = WCAction.ERROR;
		}
    	return result;
	}
	
	public String logoutSalesRep() {
		LOG.info(":: Entering logoutSalesRep() ");
		LOG.info(":: User is currently logged out...");
		if (request == null || response == null){
			if ("PAGE".equalsIgnoreCase(getLogoutMethod())){
				return WCAction.SUCCESS;
			}
			else {
				return WCAction.NONE;
			}
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
		request.getSession(false).invalidate();
		if ("PAGE".equalsIgnoreCase(getLogoutMethod())){
			return WCAction.SUCCESS;
		}
		else {
			return WCAction.NONE;
		}
	}
	
	private void setRequiredAttrbutes(){
		String sessionUserName = (String)request.getSession(false).getAttribute("loggedInUserName");
		String sessionUserId = (String)request.getSession(false).getAttribute("loggedInUserId");
		String requestUserName = (String)request.getAttribute("loggedInUserName");
		String requestUserId = (String)request.getAttribute("loggedInUserId");
		
		if (sessionUserName == null  && requestUserName == null){
			request.getSession(false).setAttribute("IS_SALES_REP", "true");
			request.getSession(false).setAttribute("loggedInUserId",DisplayUserID);
		}
		else if (requestUserName != null){
			request.getSession(false).setAttribute("IS_SALES_REP", "true");
			request.getSession(false).setAttribute("loggedInUserId",DisplayUserID);
			request.getSession(false).setAttribute("loggedInUserName",requestUserName);			
		}

	}
	
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

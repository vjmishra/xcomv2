package com.sterlingcommerce.xpedx.webchannel.salesrep;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.util.YFCCommon;


public class XPEDXSalesRepUtils {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger
			.getLogger(XPEDXSalesRepUtils.class);
	
	private static boolean isSalesRepIdSetInContxt = false;
	
	private static String SR_CUSTOMER_ID_MAP = "SRCustomerIDMap";
	private static String SR_STOREFRONT_ID_MAP = "SRStorefrontIDMap";
	
	public void XPEDXSalesRepCustomer(){}
	
	/**
	 * 
	 * @param request
	 * @param wcContext
	 * @throws Exception
	 */
	public void fetchCustomerForSalesRep(HttpServletRequest request, IWCContext wcContext) throws Exception{
		LOG.info(":: Entering fetchCustomerForSalesRep of XPEDXSalesRepUtils :: ");
		
		String networkId = request.getParameter("DisplayUserID");
		if (YFCCommon.isVoid(networkId)){
			return;
		}
		
    	Map<String, String> customersMap = new HashMap<String, String>();

    	// fetch the customers of sales rep
		customersMap.putAll(getCustomersForSalesRep(networkId, wcContext));
		
		// set attributes into seession
		setAttributesInSession(wcContext, request);
		
		// set the Customer collection in the request
		wcContext.getSCUIContext().getSession().setAttribute("ASSIGNED_CUSTOMERS",  customersMap);
	}
	
	/**
	 * 
	 * @param wcContext
	 * @param request
	 */
	private void setAttributesInSession(IWCContext wcContext, HttpServletRequest request){
		if(!isSalesRepIdSetInContxt){
			request.getSession(false).setAttribute("IS_SALES_REP", new Boolean(true));
			
			String salesRepKey = (String) request.getSession(false).getAttribute("SALES_REP_KEY");
			wcContext.getSCUIContext().getRequest().getSession().setAttribute("SALES_REP_KEY", salesRepKey);
			
			String salesRepId = (String) request.getSession(false).getAttribute("SalesRepId");
			wcContext.getSCUIContext().getRequest().getSession().setAttribute("SalesRepId", salesRepId);
				    	
	    	request.getSession(false).setAttribute("scui-guest-user", salesRepId);
	    	
	    	String networkId = request.getParameter("DisplayUserID");
	    	wcContext.getSCUIContext().getRequest().getSession().setAttribute("DisplayUserID", networkId);
	    	
	    	isSalesRepIdSetInContxt = false;
		}
	}
	
	/**
	 * 
	 * @param networkId
	 * @param wcContext
	 * @return
	 */
	private Map<String, String> getCustomersForSalesRep(
			String networkId, IWCContext wcContext) {
		Document outputDoc = null;
		
		// input doc for the mashup
		Element input = SCXmlUtil.createDocument("XPEDXSalesRepCustomers").getDocumentElement();
		input.setAttribute("UserID", networkId);// customers for the logged in sales rep
		input.setAttribute("UserIDQryType", "LIKE");
		
		String inputXml = SCXmlUtil.getString(input);
		if(LOG.isDebugEnabled())LOG.debug("Input XML: " + inputXml);
		
		// invoke Mashup
		Object obj = WCMashupHelper.invokeMashup("XPEDXGetSalesRepCustomersList", input, wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		//System.out.println("Input:" + inputXml);
		//System.out.println("Output:" + SCXmlUtil.getString(outputDoc));
		if (null != outputDoc) {
			if(LOG.isDebugEnabled())LOG.debug("SalesRep Output XML: " + SCXmlUtil.getString(outputDoc));
		}
		return getCustomersSetFromDoc(outputDoc, networkId, wcContext);
	}

	/**
	 * 
	 * @param outputDoc
	 * @param networkId
	 * @param wcContext 
	 * @return
	 */
	private Map<String, String> getCustomersSetFromDoc(Document outputDoc,
			String networkId, IWCContext wcContext) {
		Map<String, String> customersMap = new HashMap<String, String>();
		Map<String, String> customerIDsMap = new HashMap<String, String>();
		Map<String, String> storefrontIDsMap = new HashMap<String, String>();
		    	
		NodeList customersElemsList = outputDoc.getElementsByTagName("XPEDXSalesRepCustomers");
		Element customerElem;
		String customerID;
		String storefrontID;
		String customerName;
		String legacyCustomerNo;
		String firstName;
		String lastName;
		String UserName=null;
		for(int i=0; customersElemsList!=null && i<customersElemsList.getLength(); i++){
			customerElem = (Element)customersElemsList.item(i);
			if(networkId.equalsIgnoreCase(customerElem.getAttribute("UserID"))){// for the logged in user ID
				firstName = customerElem.getAttribute("FirstName");
				lastName = customerElem.getAttribute("LastName");
				customerName = customerElem.getAttribute("ExtnMSAPOrganizationName");
				customerID = customerElem.getAttribute("CustomerID");
				storefrontID = customerElem.getAttribute("ExtnMSAPOrganizationCode");
				legacyCustomerNo = customerID.split("-")[1];
				customersMap.put(legacyCustomerNo, customerName);
				customerIDsMap.put(legacyCustomerNo, customerID);
				storefrontIDsMap.put(legacyCustomerNo,storefrontID);
				wcContext.setWCAttribute(SR_CUSTOMER_ID_MAP, customerIDsMap, WCAttributeScope.SESSION);
				wcContext.setWCAttribute(SR_STOREFRONT_ID_MAP, storefrontIDsMap, WCAttributeScope.SESSION);
				if((firstName!=null && !firstName.isEmpty()) && (lastName!=null && !lastName.isEmpty())){
					UserName = firstName+" "+lastName;
				}
				if(UserName!=null && !UserName.isEmpty() ){
					wcContext.setWCAttribute("loggedInUserName", UserName, WCAttributeScope.SESSION);
				}
			}
		}
		return customersMap;
	}
	
	/**
	 * Get the selected customer and construct the login Id to bypass authentication 
	 * @param request
	 * @param wcContext 
	 */
	@SuppressWarnings("unchecked")
	public void fetchCustomerLogin(HttpServletRequest request, IWCContext wcContext){
		
		String selectedCustomer = request.getParameter("selectedCustomer");
		// set the selected customer in the session
		request.getSession(false).setAttribute("selectedCustomer", selectedCustomer);
		LOG.info(" Selected Customer is:: " + selectedCustomer);
		
		String networkId = (String)wcContext.getSCUIContext().getRequest().getSession().getAttribute("DisplayUserID");
		
		// fetch the employee id for the logged in user to construct the dummyuser id
		String employeeId = getEmployeeId(networkId, wcContext);
		String customerId = null;
		String storefrontId = null;
		if(!YFCUtils.isVoid(selectedCustomer)){
			//selectedCustomer = "CD-"+selectedCustomer+"-M-XPED-CC";
			//loginId = getCustomerContactFromMSAPCustNo(wcContext, selectedCustomer);
			Map<String, String> customerIDsMap = (HashMap<String, String>)wcContext.getWCAttribute(SR_CUSTOMER_ID_MAP, WCAttributeScope.SESSION);
			customerId = customerIDsMap.get(selectedCustomer);
			Map<String, String> storefrontIDsMap = (HashMap<String, String>)wcContext.getWCAttribute(SR_STOREFRONT_ID_MAP, WCAttributeScope.SESSION);
			storefrontId = storefrontIDsMap.get(selectedCustomer);
		}
		
		// Construct the UserId or the CustomerContact - Ex: SalesRepTest@CD-101-M-XPED-CC.com
		//String loginId = salesRepId+"@"+selectedCustomer+".com";
		String loginId;
		LOG.info("--->loginId :: " + customerId);
		if(!YFCUtils.isVoid(customerId)){
			loginId = employeeId+"@"+customerId+".com";
			request.setAttribute("dum_username", loginId.trim());
			request.setAttribute("dum_password", loginId.trim());// the password remains the same as loginid - ASSUMPTION
			request.setAttribute("selected_storefrontId", storefrontId);
			request.getSession(false).setAttribute("IS_SALES_REP", new Boolean(true));
		}
	}
	
	private String getEmployeeId(String networkId, IWCContext wcContext) {
		
		Document outputDoc;
		// input doc for the mashup
		Element input = SCXmlUtil.createDocument("XPEDXSalesRep").getDocumentElement();
		input.setAttribute("NetworkID", networkId);// customers for the logged in sales rep
		
		String inputXml = SCXmlUtil.getString(input);
		if(LOG.isDebugEnabled())LOG.debug(" getEmployeeId Input XML: " + inputXml);
		
		// invoke Mashup
		Object obj = WCMashupHelper.invokeMashup("XPEDXGetSalesRepEmployeeId", input, wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		
		NodeList salesRepElemsList = outputDoc.getElementsByTagName("XPEDXSalesRep");
		String salesRepEmployeeId = null;
		
		for(int i=0; i< salesRepElemsList.getLength(); i++){
			Element salesRepElem = (Element)salesRepElemsList.item(i);
			salesRepEmployeeId = salesRepElem.getAttribute("SalesRepId");
			if("0000".equals(salesRepEmployeeId))
				continue;
			else 
				break;
			
		}
		// TODO Auto-generated method stub
		return salesRepEmployeeId;
	}
}

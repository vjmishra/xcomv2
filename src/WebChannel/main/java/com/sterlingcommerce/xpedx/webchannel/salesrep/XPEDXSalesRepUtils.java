package com.sterlingcommerce.xpedx.webchannel.salesrep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;


public class XPEDXSalesRepUtils {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger
			.getLogger(XPEDXSalesRepUtils.class);
	
	private static boolean isSalesRepIdSetInContxt = false;
	
	private static String SR_CUSTOMER_ID_MAP = "SRCustomerIDMap";
	private static String SR_STOREFRONT_ID_MAP = "SRStorefrontIDMap";
	//added for jira 3442 and 3438
	private static String SR_SALESREP_ID = "SRSalesRepID";
	private static String SR_SALESREP_EMAIL_ID = "SRSalesRepEmailID";
	
	public void XPEDXSalesRepCustomer(){}
	
	/**
	 * 
	 * @param request
	 * @param wcContext
	 * @throws Exception
	 */
	 //added an extra parameter doc in the method  for jira 3442
	public void fetchCustomerForSalesRep(HttpServletRequest request, IWCContext wcContext,Document doc) throws Exception{
		LOG.debug(":: Entering fetchCustomerForSalesRep of XPEDXSalesRepUtils :: ");
		String networkId = request.getParameter("DisplayUserID");
		if (YFCCommon.isVoid(networkId)){
			return;
		}
		LinkedHashMap<String, String> customersMap = new LinkedHashMap<String, String>();
    	// fetch the customers of sales rep
		//added an extra parameter doc for jira 3442
		customersMap.putAll(getCustomersForSalesRep(networkId, wcContext,doc));		
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
	private LinkedHashMap<String, String> getCustomersForSalesRep(
		String networkId, IWCContext wcContext,Document doc) {
		Document outputDoc = null;
		//Modified for jira 3442
		Element outputElem = null;
		if(doc == null)
		{
			// input doc for the mashup
			Element input = SCXmlUtil.createDocument("XPXSalesRepCustomers").getDocumentElement();
			input.setAttribute("UserID", networkId);// customers for the logged in sales rep
			input.setAttribute("UserIDQryType", "LIKE");
			String inputXml = SCXmlUtil.getString(input);
			if(LOG.isDebugEnabled())LOG.debug("Input XML: " + inputXml);
			Map<String,String> valueMap = new HashMap<String,String>();
			String customerContactId = wcContext.getLoggedInUserId();
			valueMap.put("/Page/API/Input/XPXSalesRepCustomers/@UserID", customerContactId);
			valueMap.put("/Page/@PageNumber", "1");
			valueMap.put("/Page/@PageSize", "25");
			Element inputElem;
		
			Document outputElemDoc = null;
			try {
				inputElem = WCMashupHelper.getMashupInput("XPXGetPaginatedSalesRepCustomersList",valueMap, wcContext.getSCUIContext());
				outputElem = (Element) WCMashupHelper.invokeMashup("XPXGetPaginatedSalesRepCustomersList",inputElem, wcContext.getSCUIContext());
				outputElemDoc = outputElem.getOwnerDocument();
				} catch (Exception e) {
					LOG.error("Error getting the Customers Assigned to sales rep : "+e.getMessage());
					return null;
				}
		}
		else{
				outputElem=doc.getDocumentElement();
			}
			Element srCustomerElem = SCXmlUtil.getChildElement(outputElem, "Output");
			Element  viewListElem=(Element)srCustomerElem.getElementsByTagName("XPXSalesRepCustomersList").item(0);
			if(viewListElem != null)
				outputDoc=viewListElem.getOwnerDocument();
				
			if (null != outputDoc) {
				if(LOG.isDebugEnabled())LOG.debug("SalesRep Output XML: " + SCXmlUtil.getString(outputDoc));
			}
			return getCustomersSetFromDoc(outputDoc, networkId, wcContext);
	}

	//search the customers for sales rep - jira 3442
	public LinkedHashMap<String, String> searchCustomerForSalesRep(HttpServletRequest request, IWCContext wcContext,Document doc) throws Exception{
		LOG.debug(":: searching Customer For SalesRep in XPEDXSalesRepUtils.searchCustomerForSalesRep :: ");
		String networkId = request.getParameter("DisplayUserID");
		if (YFCCommon.isVoid(networkId)){
			return null;
		}
		// set attributes into seession
		setAttributesInSession(wcContext, request);
		Element outputElem= doc.getDocumentElement();
		Element srCustomerElem = SCXmlUtil.getChildElement(outputElem, "Output");
		Element  viewListElem=(Element)srCustomerElem.getElementsByTagName("XPXSalesRepCustomersList").item(0);
		Document outputDoc1 = null;
		if(viewListElem != null)
			outputDoc1=viewListElem.getOwnerDocument();
		return getCustomersSetFromDoc(outputDoc1, networkId, wcContext);
		}
	//end of jira 3442

	/**
	 * 
	 * @param outputDoc
	 * @param networkId
	 * @param wcContext 
	 * @return
	 */
	private LinkedHashMap<String, String> getCustomersSetFromDoc(Document outputDoc,
			String networkId, IWCContext wcContext) {
		LinkedHashMap<String, String> customersMap = new LinkedHashMap<String, String>();
		Map<String, String> customerIDsMap = new HashMap<String, String>();
		Map<String, String> storefrontIDsMap = new HashMap<String, String>();
		Map<String, String> salesRepIDsMap = new HashMap<String, String>();		
		    	
		NodeList customersElemsList = outputDoc.getElementsByTagName("XPXSalesRepCustomers");
		Element customerElem;
		String customerID;
		String storefrontID;
		String customerName;
		String legacyCustomerNo;
		String firstName;
		String lastName;
		String UserName=null;
		String SalesRepID;
		String CustomerNo;
		
		for(int i=0; customersElemsList!=null && i<customersElemsList.getLength(); i++){
			customerElem = (Element)customersElemsList.item(i);
			if(networkId.equalsIgnoreCase(customerElem.getAttribute("UserID"))){// for the logged in user ID
				firstName = customerElem.getAttribute("FirstName");
				lastName = customerElem.getAttribute("LastName");
				//added for jira 3442
				CustomerNo = customerElem.getAttribute("ExtnCustomerNo");
				customerName = customerElem.getAttribute("ExtnCustomerName");
				//customerName = customerElem.getAttribute("ExtnMSAPOrganizationName");
				customerID = customerElem.getAttribute("CustomerID");
				storefrontID = customerElem.getAttribute("ExtnMSAPOrganizationCode");
				//added for jira 3442
				SalesRepID = customerElem.getAttribute("SalesRepID");
				String salesRepEmailID = customerElem.getAttribute("EmailID");
				//legacyCustomerNo = customerID.split("-")[1];
				customersMap.put(CustomerNo, customerName);
				customerIDsMap.put(CustomerNo, customerID);
				storefrontIDsMap.put(CustomerNo,storefrontID);
				salesRepIDsMap.put(networkId, SalesRepID);
				wcContext.setWCAttribute(SR_CUSTOMER_ID_MAP, customerIDsMap, WCAttributeScope.SESSION);
				wcContext.setWCAttribute(SR_STOREFRONT_ID_MAP, storefrontIDsMap, WCAttributeScope.SESSION);
				wcContext.setWCAttribute(SR_SALESREP_ID, SalesRepID, WCAttributeScope.SESSION);
    			//SRSalesRepEmailID added for jira 3438
				wcContext.setWCAttribute(SR_SALESREP_EMAIL_ID, salesRepEmailID, WCAttributeScope.SESSION);
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
		String customerID = request.getParameter("customerId");
		String storefrontID=request.getParameter("storeFront");
		// set the selected customer in the session
		request.getSession(false).setAttribute("selectedCustomer", selectedCustomer);
		LOG.debug(" Selected Customer is:: " + selectedCustomer);
		
		String networkId = (String)wcContext.getSCUIContext().getRequest().getSession().getAttribute("DisplayUserID");
		
	
		// fetch the employee id for the logged in user to construct the dummyuser id
		//String employeeId = getEmployeeId(networkId, wcContext);
		String employeeId = (String)wcContext.getWCAttribute(SR_SALESREP_ID, WCAttributeScope.SESSION);
		//SRSalesRepEmailID added for jira 3438
		String SREmailID  = (String)wcContext.getWCAttribute(SR_SALESREP_EMAIL_ID, WCAttributeScope.SESSION);
		String customerId = null;
		String storefrontId = null;
		if(!YFCUtils.isVoid(selectedCustomer)){
			//selectedCustomer = "CD-"+selectedCustomer+"-M-XPED-CC";
			//loginId = getCustomerContactFromMSAPCustNo(wcContext, selectedCustomer);
			Element salesRepCustomerElement=null;
			if(!YFCUtils.isVoid(customerID) && !YFCUtils.isVoid(storefrontID) )
			{
				customerId=customerID;
				storefrontId=storefrontID;
			}
			else
			{
				Map<String, String> customerIDsMap = (HashMap<String, String>)XPEDXWCUtils.getObjectFromCache(SR_CUSTOMER_ID_MAP);
				if(customerIDsMap != null)
				{
					System.out.println("*************  SalesRep Customer cout from session in class XPEDXSalesRepUtils   =====  "+customerIDsMap.size() );
					customerId = customerIDsMap.get(selectedCustomer);
					Map<String, String> storefrontIDsMap = (HashMap<String, String>)wcContext.getWCAttribute(SR_STOREFRONT_ID_MAP, WCAttributeScope.SESSION);
					storefrontId = storefrontIDsMap.get(selectedCustomer);
				}
				else
				{
					System.out.println("************* *************  SalesRep Customer cout from session in class XPEDXSalesRepUtils  is 0 ");
					try
					{
						salesRepCustomerElement=getCustoemrElementForAccount(networkId,selectedCustomer,wcContext);
						customerId=salesRepCustomerElement.getAttribute("CustomerID");
						storefrontId=salesRepCustomerElement.getAttribute("ExtnMSAPOrganizationCode");
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			
			//Map<String, String> customerIDsMap = (HashMap<String, String>)wcContext.getWCAttribute(SR_CUSTOMER_ID_MAP, WCAttributeScope.SESSION);
			
			
		}
		
		// Construct the UserId or the CustomerContact - Ex: SalesRepTest@CD-101-M-XPED-CC.com
		//String loginId = salesRepId+"@"+selectedCustomer+".com";
		String loginId;
		LOG.debug("--->loginId :: " + customerId);
		if(!YFCUtils.isVoid(customerId)){
			loginId = employeeId+"@"+customerId+".com";
			request.setAttribute("dum_username", loginId.trim());
			String saltKey = getSaltKey(loginId);
			if(!"".equalsIgnoreCase(saltKey) && saltKey !=null){
				System.out.println("Salt key is " + saltKey );
				String newPassword  = applySaltPattern( loginId, saltKey);
				
				System.out.println("New Password after Salt Pattern is : --- " + newPassword);
				request.setAttribute("dum_password", newPassword);
			}else{
				request.setAttribute("dum_password", loginId.trim());
			}
		//	System.out.println("Salt key is " + saltKey );
		//	request.setAttribute("dum_password", loginId.trim());// the saltKeypassword remains the same as loginid - ASSUMPTION
			request.setAttribute("selected_storefrontId", storefrontId);
			//SRSalesRepEmailID added for jira 3438
			request.setAttribute("SRSalesRepEmailID",SREmailID);
			request.getSession(false).setAttribute("IS_SALES_REP", new Boolean(true));
			request.getSession(false).setAttribute("SRSalesRepEmailID", SREmailID);
		}
	}
	
	private Element getCustoemrElementForAccount(String networkId,String selectedCustomer,IWCContext wcContext) throws Exception
	{
		if(YFCUtils.isVoid(networkId) || YFCUtils.isVoid(selectedCustomer))
		{
			LOG.error(" Networkid ="+networkId+"   or selectedCustomer=="+selectedCustomer+"  is void so can not get any customerid returning null");
			return null;
		}
		Map<String,String> valueMap = new HashMap<String,String>();
		valueMap.put("/XPXSalesRepCustomers/@UserID", networkId);
		valueMap.put("/XPXSalesRepCustomers/@ExtnCustomerNo", selectedCustomer);
		

		Element input = WCMashupHelper.getMashupInput("xpedxGetSRCustomersListService", valueMap, wcContext.getSCUIContext());
		return (Element) WCMashupHelper.invokeMashup("xpedxGetSRCustomersListService", input, wcContext.getSCUIContext());
		
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
	
	public static String getSaltKey(String networkId) {
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		String userName = "";
		Element userList = null;
		String saltKey = null;
		try {
			Element input = SCXmlUtil.createDocument("User").getDocumentElement();
			
			input.setAttribute("Loginid", networkId);// customers for the logged in sales rep
			
			String inputXml = SCXmlUtil.getString(input);
			
			
			
			userList =(Element) WCMashupHelper.invokeMashup("XPEDX-GetUserList-SalesRep", input, context.getSCUIContext());
		} catch (XMLExceptionWrapper e) {
			LOG.error("Unable to get user list", e);
		}

		if (userList != null) {
			Element userEle = SCXmlUtil.getChildElement(userList, "User");
			userName = userEle.getAttribute("Username");
			Element extnElem = SCXmlUtil.getChildElement(userEle, "Extn");
			System.out.println("Saltkey is------ " + extnElem.getAttribute("ExtnSaltKey"));
			saltKey = extnElem.getAttribute("ExtnSaltKey");
		}

		return saltKey;
	}
	
	public String applySaltPattern(String word,String salt) { 
		ArrayList<Character> one = new ArrayList<Character>();    
		String [] saltpattern = salt.split("@") ; 
		ArrayList swapArrayList = new ArrayList();
	
		int l=0;
		for(int j =0; j < saltpattern.length ;j++){
			
			int k = Integer.parseInt(saltpattern[j]);
		for (int i = 0; i < word.length()-1; i++) {
			
			if(i==k){			
			    swapArrayList.add(word.substring(l,k));
               l = i;				
			}else{
				continue;
			}
			
			break;
		}
		}
	    
	    swapArrayList.add(word.substring(l,word.length()));		
			
		return swapListValues(swapArrayList).toString().replace("[", "").trim().replace(","," ").trim().replace("]", "").trim().replace("-", "").trim().replace(".", "").trim().replace(" ", "");
		    
		}   
	
	public List swapListValues(ArrayList swapArrayList){
		List<String> list = new ArrayList<String>();
		//Change Position in List with 1-3, 2-4,3-5
		for(int i=0;i< swapArrayList.size();i++){
			if(i < swapArrayList.size() - 1){
				list.add((String) swapArrayList.get(i+1).toString().replace("@", "C").trim().replace("-", " "));
				list.add((String) swapArrayList.get(i).toString().replace("@", "C").trim().replace("-", " "));
				i++;
			}
			
		}
		if(list.size() < swapArrayList.size()){
			list.addAll(swapArrayList.subList(list.size(), swapArrayList.size()));
		}
		
		return list;
	}
}

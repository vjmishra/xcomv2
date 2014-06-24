package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;

@SuppressWarnings("serial")
public class AjaxGetSalesProCustomersListAction extends WCAction {

	private static String SR_CUSTOMER_ID_MAP = "SRCustomerIDMap";
	private static String SR_STOREFRONT_ID_MAP = "SRStorefrontIDMap";
	private static String SR_SALESREP_ID = "SRSalesRepID";
	private static String SR_SALESREP_EMAIL_ID = "SRSalesRepEmailID";

	// input fields
	private String networkId;
	private String DisplayUserID = null;
	protected HttpServletResponse response = null;

	// output fields
	private List<SalesProCustomer> customerList;

	@Override
	public String execute() throws Exception {
		setRequiredAttributes();
		try{
			networkId = request.getParameter("DisplayUserID");
			Document doc = getSalesRepCustomersDocument(wcContext);
			Element outputXPXSalesRepCustomersListElem = doc.getDocumentElement();
			customerList = getAllCustomerList(outputXPXSalesRepCustomersListElem);
			return WCAction.SUCCESS;

		} catch (Exception e) {
			//added error message for LDAP password validation.
			wcContext.getSCUIContext().getRequest().getSession().setAttribute("ERROR_MESSAGE", "Please enter a valid Username and Password.");
			LOG.error("Error while retrieving the MSAP Customers for the Sales Rep: "+ e.getMessage(), e);
			return WCAction.ERROR;
		}
	}

	private Document getSalesRepCustomersDocument(IWCContext context) throws Exception {
		String customerContactId = context.getLoggedInUserId();
		Map<String,String> valueMap = new HashMap<String,String>();
		valueMap.put("/XPXSalesRepCustomers/@UserID", customerContactId);

		Element outputElem;
		Element input = WCMashupHelper.getMashupInput("xpedxGetSRCustomersListService", valueMap, context.getSCUIContext());
		outputElem = (Element) WCMashupHelper.invokeMashup("xpedxGetSRCustomersListService", input, context.getSCUIContext());
		return outputElem.getOwnerDocument();
	}

	/**
	 * getting all customers for the corresponding sales professional
	 */
	private List<SalesProCustomer> getAllCustomerList(Element custAssignedEle) {
		List<SalesProCustomer> returnCustomerList=null;
		List<Element> assignedCustElems = SCXmlUtil.getElements(custAssignedEle, "XPXSalesRepCustomers");

		Map<String, String> customersMap = new LinkedHashMap<String, String>();
		Map<String, String> customerIDsMap = new HashMap<String, String>();
		Map<String, String> storefrontIDsMap = new HashMap<String, String>();
		Map<String, String> salesRepIDsMap = new HashMap<String, String>();

		if (assignedCustElems != null && assignedCustElems.size() > 0) {
			returnCustomerList =  new ArrayList<SalesProCustomer>(assignedCustElems.size());
			for (Element assignedCustElem : assignedCustElems) {
				SalesProCustomer srCustomer = new SalesProCustomer();

				srCustomer.setCustomerNo(SCXmlUtil.getAttribute(assignedCustElem, "ExtnCustomerNo"));
				srCustomer.setCustomerName(SCXmlUtil.getAttribute(assignedCustElem,"ExtnCustomerName"));
				srCustomer.setCustomerId(SCXmlUtil.getAttribute(assignedCustElem,"CustomerID"));
				srCustomer.setFirstName(SCXmlUtil.getAttribute(assignedCustElem,"FirstName"));
				srCustomer.setLastName(SCXmlUtil.getAttribute(assignedCustElem,"LastName"));
				srCustomer.setSalesRepID(SCXmlUtil.getAttribute(assignedCustElem,"SalesRepID"));
				srCustomer.setStorefrontID(SCXmlUtil.getAttribute(assignedCustElem,"ExtnMSAPOrganizationCode"));
				srCustomer.setSalesRepEmailID(SCXmlUtil.getAttribute(assignedCustElem,"EmailID"));

				customersMap.put(srCustomer.getCustomerNo(), srCustomer.getCustomerName());
				customerIDsMap.put(srCustomer.getCustomerNo(), srCustomer.getCustomerId());
				storefrontIDsMap.put(srCustomer.getCustomerNo(),srCustomer.getStorefrontID());
				salesRepIDsMap.put(networkId, srCustomer.getSalesRepID());

				wcContext.setWCAttribute(SR_CUSTOMER_ID_MAP, customerIDsMap, WCAttributeScope.SESSION);
				wcContext.setWCAttribute(SR_STOREFRONT_ID_MAP, storefrontIDsMap, WCAttributeScope.SESSION);
				wcContext.setWCAttribute(SR_SALESREP_ID, srCustomer.getSalesRepID(), WCAttributeScope.SESSION);
				wcContext.setWCAttribute(SR_SALESREP_EMAIL_ID, srCustomer.getSalesRepEmailID(), WCAttributeScope.SESSION);

				if((srCustomer.getFirstName() != null && !srCustomer.getFirstName().isEmpty()) && (srCustomer.getLastName() != null && !srCustomer.getLastName().isEmpty())) {
					srCustomer.setUserName(srCustomer.getFirstName()+" "+srCustomer.getLastName());
				}
				if(srCustomer.getUserName() != null && !srCustomer.getUserName().isEmpty() ) {
					wcContext.setWCAttribute("loggedInUserName", srCustomer.getUserName(), WCAttributeScope.SESSION);
				}

				returnCustomerList.add(srCustomer);
			}
			Collections.sort(returnCustomerList,SalesProCustomer.COMPARATOR_DISPLAY);
		}

		return returnCustomerList;
	}

	private void setRequiredAttributes() {
		String sessionUserName = (String) request.getSession(false).getAttribute("loggedInUserName");
		String requestUserName = (String) request.getAttribute("loggedInUserName");
		String requestSREmailID = (String) request.getAttribute("SRSalesRepEmailID");

		if (sessionUserName == null  && requestUserName == null) {
			request.getSession(false).setAttribute("IS_SALES_REP", "true");
			request.getSession(false).setAttribute("loggedInUserId",DisplayUserID);
		} else if (requestUserName != null) {
			request.getSession(false).setAttribute("IS_SALES_REP", "true");
			request.getSession(false).setAttribute("loggedInUserId",DisplayUserID);
			request.getSession(false).setAttribute("loggedInUserName",requestUserName);
			request.getSession(false).setAttribute("SRSalesRepEmailID",requestSREmailID);
		}
	}

	// --- Input fields

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public void setDisplayUserID(String displayUserID) {
		DisplayUserID = displayUserID;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	// --- Output fields

	public List<SalesProCustomer> getCustomerList() {
		return customerList;
	}

}

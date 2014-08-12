package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;

@SuppressWarnings("serial")
public class AjaxGetSalesProCustomersListAction extends WCAction {

	private static final Logger log = Logger.getLogger(AjaxGetSalesProCustomersListAction.class);

	private static String SR_CUSTOMER_ID_MAP = "SRCustomerIDMap";
	private static String SR_STOREFRONT_ID_MAP = "SRStorefrontIDMap";
	private static String SR_SALESREP_ID = "SRSalesRepID";
	private static String SR_SALESREP_EMAIL_ID = "SRSalesRepEmailID";

	// input fields
	private String networkId;

	// output fields
	private List<SalesProCustomer> customerList;

	@Override
	public String execute() throws Exception {
		request.getSession(false).setAttribute("IS_SALES_REP", "true");

		try{
			networkId = request.getParameter("DisplayUserID");

			Element outputXPXSalesRepCustomersListElem = invokeGetSRCustomersListService(wcContext);
			if (outputXPXSalesRepCustomersListElem != null) {
				// null doc indicates user is not currently logged in
				customerList = createCustomerList(outputXPXSalesRepCustomersListElem);
			}

			return WCAction.SUCCESS;

		} catch (Exception e) {
			//added error message for LDAP password validation.
			wcContext.getSCUIContext().getRequest().getSession().setAttribute("ERROR_MESSAGE", "Please enter a valid Username and Password.");
			LOG.error("Error while retrieving the MSAP Customers for the Sales Rep: "+ e.getMessage(), e);
			return WCAction.ERROR;
		}
	}

	/**
	 * Invokes xpedxGetSRCustomersListService mashup with networkId and returns result.
	 * Returns null if networkId is null
	 */
	private Element invokeGetSRCustomersListService(IWCContext context) throws Exception {
		if (YFCCommon.isVoid(networkId)) {
			log.error("customerContactId is null: Do not allow API call xpedxGetSRCustomersListService, since it would return all customers");
			return null;
		}

		Map<String,String> valueMap = new HashMap<String,String>();
		valueMap.put("/XPXSalesRepCustomers/@UserID", networkId);

		Element input = WCMashupHelper.getMashupInput("xpedxGetSRCustomersListService", valueMap, context.getSCUIContext());
		return (Element) WCMashupHelper.invokeMashup("xpedxGetSRCustomersListService", input, context.getSCUIContext());
	}

	/**
	 * Creates a list of SalesProCustomer from the api output.
	 */
	private List<SalesProCustomer> createCustomerList(Element custAssignedEle) {
		List<SalesProCustomer> returnCustomerList=null;
		List<Element> assignedCustElems = SCXmlUtil.getElements(custAssignedEle, "XPXSalesRepCustomers");

		Map<String, String> customersMap = new LinkedHashMap<String, String>();
		Map<String, String> customerIDsMap = new HashMap<String, String>();
		Map<String, String> storefrontIDsMap = new HashMap<String, String>();
		Map<String, String> salesRepIDsMap = new HashMap<String, String>();
		System.out.println("*************  assignedCustElems.size() = "+assignedCustElems.size() );
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
				XPEDXWCUtils.setObectInCache(SR_CUSTOMER_ID_MAP, customerIDsMap);
				XPEDXWCUtils.setObectInCache(SR_STOREFRONT_ID_MAP, storefrontIDsMap);
				XPEDXWCUtils.setObectInCache(SR_SALESREP_ID, srCustomer.getSalesRepID());
				XPEDXWCUtils.setObectInCache(SR_SALESREP_EMAIL_ID, srCustomer.getSalesRepEmailID());
				/*wcContext.setWCAttribute(SR_CUSTOMER_ID_MAP, customerIDsMap, WCAttributeScope.SESSION);
				wcContext.setWCAttribute(SR_STOREFRONT_ID_MAP, storefrontIDsMap, WCAttributeScope.SESSION);
				wcContext.setWCAttribute(SR_SALESREP_ID, srCustomer.getSalesRepID(), WCAttributeScope.SESSION);
				wcContext.setWCAttribute(SR_SALESREP_EMAIL_ID, srCustomer.getSalesRepEmailID(), WCAttributeScope.SESSION);*/

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
		Map<String, String> customersMapFromSession =(Map<String, String>)XPEDXWCUtils.getObjectFromCache(SR_CUSTOMER_ID_MAP);
		if(customersMapFromSession != null)
			System.out.println("*************  SalesRep Customer cout from session in class AjaxGetSalesProCustomersListAction   =====  "+customersMapFromSession.size() );
		else
			System.out.println("*************  SalesRep Customer cout from session in class AjaxGetSalesProCustomersListAction   is 0 ");
		return returnCustomerList;
	}

	// --- Input fields

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	// --- Output fields

	public List<SalesProCustomer> getCustomerList() {
		return customerList;
	}

}

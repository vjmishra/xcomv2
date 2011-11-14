package com.sterlingcommerce.xpedx.webchannel.salesrep;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAction;

public class XPEDXLoginManager{

	public boolean authenticate(HttpServletRequest request, IWCContext wcContext) {
		
		boolean isSalesRep = false;
		boolean authenticationNotRequired = true;
		String username = request.getParameter("sr_username");
		String password = request.getParameter("sr_password");
		
		// Will be fetched from the db / Authentication mechanism
		String salesRepKey = "salesRep1";
		request.getSession(false).setAttribute("SALES_REP_KEY", salesRepKey);
		request.getSession(false).setAttribute("NETWORK_ID", username);
		
		//String salesRepId = (String) request.getSession(false).getAttribute("SalesRepId");
		//String organizationCode = request.getParameter("sfId");
		
		// EmployeeId (username) - is what is coming in. This is mapped to extn_employee_id of YFS_USER
		// Get the LoginID for that extn_employee_id from YFS_USER
		// LoginID corresponds to user_key in the salesRep table.
		// WARNING!!!! if that user_key in XPEDX_Sales_Rep table is primary_sales_rep_flag ---> not sure about this!!!!!!
		// My UNDERSTANDING::::: using this networkID, query getCustomerDetails, to get the CustomerContact.
		// 						/Customer/CustomerContactList/CustomerContact/Extn
		// 						If ExtnUserType == EXTERNAL or INTERNAL..decide if Authentication is required!!!
		//isSalesRep = (new XPEDXSalesRepUtils()).getNetworkIdForSalesRep(wcContext, salesRepId, organizationCode);
		
		if(isSalesRep){
			// LDAP Authentication required here!!!!!!
			authenticationNotRequired = false;
		}
		return authenticationNotRequired;
	}

}

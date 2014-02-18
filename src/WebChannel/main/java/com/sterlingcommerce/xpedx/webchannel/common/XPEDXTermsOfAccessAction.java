package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCDate;

public class XPEDXTermsOfAccessAction extends WCMashupAction {

	public String execute() {

		try
		{
			if(!"Y".equals(toaChecked))
			{
				toaChecked = "N";
			}
			else
			{
				YFCDate currentDate = new YFCDate();
				toaAcceptedDate = currentDate.getString();
			}
			//getWCContext().getStorefrontId();
			/*customerContactId = getWCContext().getCustomerContactId();
			customerId = XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext);
			if(!(customerId!= null && customerId.trim().length()>0))
				customerId = wcContext.getCustomerId();
			buyerOrdCode = getWCContext().getBuyerOrgCode();
			
			Element outputEl = prepareAndInvokeMashup("UpdateUserTOA");*/
			//EB-475 update the last login date only once while logging in
			YFCDate loginDate = new YFCDate();
			String lastLoginDate = loginDate.getString();
			
			Map<String, String> attributeMap = new HashMap<String, String>();
			String custContRefKey = null;
			boolean createCCExtn = false;
			String customerContactId=  wcContext.getCustomerContactId();
			Element xpxCustContExtnEle= XPEDXWCUtils.getXPXCustomerContactExtn(wcContext, customerContactId);
			if(xpxCustContExtnEle == null)
				createCCExtn = true;
			else {
				custContRefKey = xpxCustContExtnEle.getAttribute("CustContRefKey");
			}
			if(custContRefKey!=null && custContRefKey.length()>0)
				attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_REF_ATTR, custContRefKey);
			
			attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_TC_FLAG_ATTR, toaChecked);
			attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_TC_ACCEPTED_ON_ATTR, toaAcceptedDate);
			//EB-475 update the last login while accepting terms and conditions
			attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_LAST_LOGIN_DATE,lastLoginDate);
			
			Element outDoc = (Element)XPEDXWCUtils.updateXPXCustomerContactExtn(wcContext, customerContactId,createCCExtn, attributeMap);
			wcContext.setWCAttribute("isTOAaccepted", toaChecked, WCAttributeScope.LOCAL_SESSION);
			
		}
		catch(Exception cbie)
		{
			XPEDXWCUtils.logExceptionIntoCent(cbie);  //JIRA 4289
			log.error("Couldn't save user terms of access flag", cbie);
			return "ERROR";
		}
		return SUCCESS;
	}
	
	
	public String getToaChecked() {
		return toaChecked;
	}


	public void setToaChecked(String toaChecked) {
		this.toaChecked = toaChecked;
	}
	
	public String getToaAcceptedDate() {
		return toaAcceptedDate;
	}


	public void setToaAcceptedDate(String toaAcceptedDate) {
		this.toaAcceptedDate = toaAcceptedDate;
	}
	
	public String getCustomerContactId() {
		return customerContactId;
	}


	public void setCustomerContactId(String customerContactId) {
		this.customerContactId = customerContactId;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String getBuyerOrdCode() {
		return buyerOrdCode;
	}


	public void setBuyerOrdCode(String buyerOrdCode) {
		this.buyerOrdCode = buyerOrdCode;
	}

	private String customerContactId;
	private String customerId;
	private String buyerOrdCode;
	private String toaChecked;
	private String toaAcceptedDate;
	private static final Logger log = Logger.getLogger(XPEDXTermsOfAccessAction.class);
}

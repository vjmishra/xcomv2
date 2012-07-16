package com.sterlingcommerce.xpedx.webchannel.home;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import com.yantra.yfc.util.YFCDate;
import com.sterlingcommerce.webchannel.core.wcaas.Logout;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

@SuppressWarnings("serial")
public class XPEDXLogoutAction extends Logout {
	
	private static final Logger LOG = Logger
	.getLogger(XPEDXLogoutAction.class);
	
	public String execute() {
		String lastLoginDate="";
		String returnType="WebUser";
		YFCDate loginDate = new YFCDate();
    	lastLoginDate = loginDate.getString();
    	
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
		Boolean isSalesRep = null;
		if(getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP")!=null){
			isSalesRep=Boolean.valueOf((Boolean) getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP"));
		}
		if( isSalesRep!= null && isSalesRep==true){
			returnType="SalesRepUser";
		}
		if(custContRefKey!=null && custContRefKey.length()>0)
			attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_REF_ATTR, custContRefKey);
		
		attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_LAST_LOGIN_DATE, lastLoginDate);		
		
		Element outDoc = (Element)XPEDXWCUtils.updateXPXCustomerContactExtn(wcContext, customerContactId,createCCExtn, attributeMap);    	
    	
		try {
			super.execute();
		} catch (Exception e) {
			LOG.error("Error during logout " + e.getMessage());
		}
		
		return returnType;
	}

}

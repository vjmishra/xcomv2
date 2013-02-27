package com.sterlingcommerce.xpedx.webchannel.home;

import org.apache.log4j.Logger;

import com.sterlingcommerce.webchannel.core.wcaas.Logout;

@SuppressWarnings("serial")
public class XPEDXLogoutAction extends Logout {
	
	private static final Logger LOG = Logger
	.getLogger(XPEDXLogoutAction.class);
	
	public String execute() {
		String returnType="WebUser";
		String isSalesRep = null;
		if(getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP")!=null){
			isSalesRep=(String) getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP");
		}
		if( isSalesRep!= null && isSalesRep.equalsIgnoreCase("true")){
			returnType="SalesRepUser";
		}		
		try {
			super.execute();
		} catch (Exception e) {
			LOG.error("Error during logout " + e.getMessage());
		}
		
		return returnType;
	}

}

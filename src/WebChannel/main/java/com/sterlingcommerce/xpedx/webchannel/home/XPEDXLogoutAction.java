package com.sterlingcommerce.xpedx.webchannel.home;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.reports.service.webi.ReportUtils;
import com.sterlingcommerce.webchannel.core.wcaas.Logout;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
//ML:  - EB-7221 - add imports to perform loggoff BI4 to keep sessions to a minimum.
import java.util.ArrayList;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.mozilla.javascript.Context;


import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class XPEDXLogoutAction extends Logout {
	
	private static final Logger LOG = Logger
	.getLogger(XPEDXLogoutAction.class);
	
	public String execute() {
		String returnType="WebUser";
		String isSalesRep = null;
		String result = "";
		
		
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		
		}
		if( isSalesRep!= null && isSalesRep.equalsIgnoreCase("true")){
			returnType="SalesRepUser";
		}
		boolean isPunchoutUser = XPEDXWCUtils.isPunchoutUser(getWCContext());
		if(isPunchoutUser){
			returnType="punchoutUser";
		}
		//Logoff BI4 - EB-7221
		ArrayList<String> logonTokens = null;
		logonTokens = (ArrayList) (getWCContext().getSCUIContext().getSession().getAttribute("logonTokens"));
		if ((logonTokens != null) && (logonTokens.size() > 0))
		{
			ReportUtils ru = new ReportUtils();
			//retrieve the non-encoded token in the first position of the array
			String token = logonTokens.get(0).toString();
			
			result = ru.logOffBI(token);
			System.out.println(result);
			//remove the token from session
			getWCContext().getSCUIContext().getSession().removeAttribute("logonTokens");
			
		}		
				
		try {
			super.execute();
		} catch (Exception e) {
			LOG.error("Error during logout " + e.getMessage());
		}		
		
		return returnType;
	}

}

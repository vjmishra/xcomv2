package com.sterlingcommerce.xpedx.webchannel.common;
 

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.Date;
import java.util.Map;
 
//ML: add imports to perform loggoff BI4 to keep sessions to a minimum.
import com.reports.service.webi.ReportUtils;
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
import org.apache.http.impl.client.DefaultHttpClient;

/* //Logoff BI4 - EB-7221
 * ML: 09/24/2014
 * This class is used to intercept sessions timing out/ending. 
 * Place all logic for cleanup here. Added to close BI4 connection leak. 
 * 
 */
public class XPEDXSessionListener implements HttpSessionListener
{
    public void sessionCreated(HttpSessionEvent se)
    {
        //HttpSession session = se.getSession();
        //System.out.print(getTime() + " (session) Created:");
        //System.out.println("ID=" + session.getId() + " MaxInactiveInterval=" + session.getMaxInactiveInterval());
    }
    
    
    public void sessionDestroyed(HttpSessionEvent se)
    {
        HttpSession session = se.getSession();
        String result = "";
        // session has been invalidated and all session data 
        //(except Id)is no longer available        
        
        //Logoff from reporting if already logged in             	
        //EB-7221 - Logoff BI4 and stop connection leak
		ArrayList<String> logonTokens = null;
		logonTokens = (ArrayList) (session.getAttribute("logonTokens"));
		if ((logonTokens != null) && (logonTokens.size() > 0))
		{
			ReportUtils ru = new ReportUtils();
			//retrieve the non-encoded token in the first position of the array
			String token = logonTokens.get(0).toString();
			
			result = ru.logOffBI(token);
			System.out.println(result);
			//remove the token from session
			session.removeAttribute("logonTokens");
						
		}      		
		// System.out.println(getTime() + " (session) Destroyed:ID=" + session.getId());
        
    }
     
    
    private String getTime()
    {
        return new Date(System.currentTimeMillis()).toString();
    }
}
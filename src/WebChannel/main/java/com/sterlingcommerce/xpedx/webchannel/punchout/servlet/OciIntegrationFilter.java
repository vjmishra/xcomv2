/*
 * OciIntegrationFilter.java
 *
 * Created on July 20, 2010
 */
package com.sterlingcommerce.xpedx.webchannel.punchout.servlet;

import java.io.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.yantra.yfc.util.YFCCommon;


import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.sterlingcommerce.webchannel.common.integration.IAribaConstants;
import com.sterlingcommerce.webchannel.core.WCException;
import com.sterlingcommerce.webchannel.utilities.WCIntegrationXMLUtils;

/**
*
* @author  adsouza
* @version 1.0
*/
public class OciIntegrationFilter implements Filter{

	private static final Logger log = Logger.getLogger(OciIntegrationFilter.class);

    /**
     * do nothing
     */
    public void init(FilterConfig filterCfg) throws ServletException {

    }
    /**
     *
     */
    public void destroy() {
    	// empty

    }
    /**
     * .
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,ServletException
     {

    	//System.out.println("******** > OciIntegrationFilter.doFilter - requestURL=" + ((HttpServletRequest) request).getRequestURL());
    	request.setAttribute(IAribaConstants.ARIBA_CXML_REQUEST_STATUS, IAribaConstants.SUCCESS);

        if (log.isDebugEnabled()) {
        	logDebug("******** < AribaIntegrationFilter.doFilter");
        }
        chain.doFilter(request, response);
    }

    /**
     * logError
     * @param String value
     */
   public static void logError(String s)
     {

 	  log.error("OciIntegrationFilter.doFilter :" + s);
     }
   public static void logError(String s, Throwable t)
   {

	  log.error("OciIntegrationFilter.doFilter :" + s);
   }
   /**
    * logInfo
    * @param String value
    */
   public static void logInfo(String s)
     {
         log.info("OciIntegrationFilter.doFilter :" + s);
     }
   /**
    * logVerbose
    * @param String value
    */
   public static void logDebug(String s)
     {
         log.debug("OciIntegrationFilter.doFilter :" + s);
     }


}


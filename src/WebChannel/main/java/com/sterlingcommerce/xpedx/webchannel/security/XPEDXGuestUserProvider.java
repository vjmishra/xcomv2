package com.sterlingcommerce.xpedx.webchannel.security;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;

import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.platform.SCUIImplConstants;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.IWCContextBuilder;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.core.wcaas.WCGuestUserProvider;


public class XPEDXGuestUserProvider extends WCGuestUserProvider {
	   private static final Logger log = Logger.getLogger(XPEDXGuestUserProvider.class);

	    public void init() {
	        //do nothing
	    }

	    /**
	     * This method returns the guest user in the context of a storefront. If there is no known
	     * storefront, the default guest user, given by the param value of scui-guest-user, is returned.
	     * The password is assumed to be the same as username.
	     * 
	     * @param uiContext 
	     * @return the guest username as String
	     */
	    public String getGuestUser(SCUIContext uiContext) {        
	        ServletContext sCtx = uiContext.getServletContext();
	        //get builder        
	        IWCContextBuilder builder = WCContextHelper.getBuilder(uiContext.getRequest(), uiContext.getResponse());
	        IWCContext ctx = WCContextHelper.getWCContext(uiContext.getRequest());
	        //get the storefront id from the IWCContextBuilder object  
	        String sfId = builder.getStorefrontId();
	        if(sfId == null) {
	            log.debug("Returning the guest user as specified by scui-guest-user parameter");
	            //get the default guest user specified by the value of param scui-guest-user
	            return sCtx.getInitParameter(SCUIImplConstants.GUEST_USER_ATTR_NAME);
	        }
	        if (Boolean.TRUE.equals(uiContext.getSession(false).getAttribute("IS_SALES_REP"))){
	        	String salesRepLoginId = ctx.getLoggedInUserId();
	        	uiContext.getSession(false).setAttribute("scui-guest-user", salesRepLoginId);

	        	if (salesRepLoginId != null)
	        		return salesRepLoginId;
	        }
	        //construct the guest user for the storefront using the naming convention - "guest_<sfid>"
	        StringBuilder sb = new StringBuilder("guest_");
	        sb.append(sfId);
	        if(log.isDebugEnabled()){
	            log.debug("Guest user for storefront :  " + sfId + " is : " + sb.toString());
	        }
	        return sb.toString();

	    }

	    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
	        //do nothing
	    }
}

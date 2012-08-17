package com.sterlingcommerce.xpedx.webchannel.core.context;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.IWCContextAware;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.core.context.WCContextInjectorInterceptor;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class XPEDXWCContextInjectorInterceptor  extends WCContextInjectorInterceptor {

    
    private static final long serialVersionUID = -5878430987256504735L;//for serialization
    
    private final Logger log = Logger.getLogger(XPEDXWCContextInjectorInterceptor.class);
        
    
   /**
    * Fetch the built IWCContext object and then inject it into the action. 
    * Action class must implement IWCContextAware interface. If action does not implement this
    * interface, then IWCContext is not injected into the action's value stack.
    */
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {        
        
       try
       {
    	   return super.intercept(invocation);
       }
       catch(Exception ex)
       {
    	   
    	   log.error("Exception while trying to inject IWCContext into the action's value stack.Inserting logs in to cent logs", ex);
    	   logException(invocation,ex);
         //inject IWCContext into action
           
           throw ex;
       }                  
    }
    
    private void logException(ActionInvocation invocation,Exception ex) throws Exception
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        IWCContext builder = WCContextHelper.getBuilder(request, response);
        IWCContextAware action = (IWCContextAware) invocation.getAction();
        action.setWCContext((IWCContext) builder);
        XPEDXWCUtils.logExceptionIntoCent(ex);
        response.sendRedirect("/swc/xpedx/jsp/common/XPEDXGeneralSystemError.jsp");
    }
}

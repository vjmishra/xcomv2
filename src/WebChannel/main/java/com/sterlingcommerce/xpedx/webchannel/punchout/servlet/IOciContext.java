package com.sterlingcommerce.xpedx.webchannel.punchout.servlet;

import com.sterlingcommerce.webchannel.common.eprocurement.IProcurementContext;

/**
 * Interface for object which holds Oci Punch Out specific context
 * information.
 */
public interface IOciContext extends IProcurementContext
{
   
    public String getReturnURL();
  
    public String getBuyerCookie();
    
   // public String getFromIdentity();
    
   // public String getToIdentity();

    //public String getPayloadID();

}

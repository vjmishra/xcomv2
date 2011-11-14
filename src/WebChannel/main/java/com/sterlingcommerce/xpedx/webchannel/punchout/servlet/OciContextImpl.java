package com.sterlingcommerce.xpedx.webchannel.punchout.servlet;



/**
 * @author adsouza
 * Implementation class for IOciContext interface, It basically provides the setters/getters
 * for OCI context information 
 */

import org.w3c.dom.Document;

import com.yantra.yfc.util.YFCCommon;
import com.sterlingcommerce.webchannel.common.eprocurement.IProcurementContext;
import com.sterlingcommerce.webchannel.common.integration.IAribaConstants;

public class OciContextImpl implements IOciContext {
    
   private int _opreation = 0;

   private String _returnURL= null;
   private String _buyerCookie = null;
   private String _orderHeaderKey = null;

   private static OciContextImpl _instance = null;
	
   public static OciContextImpl getInstance()  {
	      
       _instance = new OciContextImpl();
       return _instance;
}	
		

   public OciContextImpl() {
		
	}	
   public void setOperation(int operation)
   {
	   _opreation = operation;
   }

   public void setReturnURL(String returnURL)
   {
	   _returnURL = returnURL;
   }

   public void setOciOperation(String operation)
   {
	   if(!YFCCommon.isVoid(operation))
	   {
		   if(YFCCommon.equals(IAribaConstants.ARIBA_OPERATION_CREATE_STRING,operation, true))
			   setOperation(IProcurementContext.PROCUREMENT_OPERATION_CREATE);
		   if(YFCCommon.equals(IAribaConstants.ARIBA_OPERATION_EDIT_STRING,operation, true))
			   setOperation(IProcurementContext.PROCUREMENT_OPERATION_EDIT);
		   if(YFCCommon.equals(IAribaConstants.ARIBA_OPERATION_INSPECT_STRING,operation, true))
			   setOperation(IProcurementContext.PROCUREMENT_OPERATION_INSPECT);
	   }
   }   
   public int getOperation()
   {
	   return _opreation;
   }

   public String getReturnURL()
   {
	   return _returnURL;
   }
   
   public void setOrderHeaderKey(String orderHeaderKey)
   {
	   _orderHeaderKey = orderHeaderKey;
   }
   
   
   public String getOrderHeaderKey()
   {
	   return _orderHeaderKey;
   }

    public String getBuyerCookie() {
    	return _buyerCookie;
    }
    
    
    public void setBuyerCookie(String cookie) {
    	_buyerCookie = cookie;
    }
    

    





}

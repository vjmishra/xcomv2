/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.servlet.eprocurement;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.ui.web.framework.security.SCUISecurityResponse;
import com.sterlingcommerce.ui.web.framework.utils.SCUIContextHelper;
import com.sterlingcommerce.ui.web.platform.utils.SCUIPlatformUtils;
import com.sterlingcommerce.webchannel.common.eprocurement.AribaContextImpl;
import com.sterlingcommerce.webchannel.common.eprocurement.CXMLMessageFields;
import com.sterlingcommerce.webchannel.common.eprocurement.IAribaContext;
import com.sterlingcommerce.webchannel.common.eprocurement.IProcurementContext;
import com.sterlingcommerce.webchannel.common.integration.IAribaConstants;
import com.sterlingcommerce.webchannel.common.integration.IWCIntegrationStatusCodes;
import com.sterlingcommerce.webchannel.common.integration.WCIntegrationResponse;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCException;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.webchannel.servlet.eprocurement.AribaIntegrationServlet;
import com.sterlingcommerce.webchannel.utilities.WCIntegrationHelper;
import com.sterlingcommerce.webchannel.utilities.WCIntegrationXMLUtils;
import com.sterlingcommerce.xpedx.webchannel.common.eprocurement.XPEDXCXMLMessageFields;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;
import com.sterlingcommerce.webchannel.common.integration.Error;

/**
 * @author adsouza
 *
 */
public class XPEDXAribaIntegrationServlet extends AribaIntegrationServlet{
	XPEDXCXMLMessageFields cXMLFields = null;
	WCIntegrationResponse wcResponse = null;
	
	
	protected SCUISecurityResponse authenticateRequest(HttpServletRequest req,
    		HttpServletResponse res)
    throws ServletException, IOException{    	
		//SCUISecurityResponse securityResponse = super.authenticateRequest(req, res);
    	SCUISecurityResponse securityResponse =  null;
    	try
    	{   
    		Document doc = (Document)req.getAttribute(IAribaConstants.ARIBA_CXML_REQUEST_ATTRIBUTE_KEY);
    		cXMLFields = new XPEDXCXMLMessageFields(doc);	
    		
    		/*Document loginDoc = WCIntegrationXMLUtils.prepareLoginInputDoc("xpedx_cxml_dummy","xpedx_cxml_dummy");
    		securityResponse = SCUIPlatformUtils.login(loginDoc,SCUIContextHelper.getUIContext(req, res));
    		if(securityResponse.getReturnStatus())
    			log.info("<<<<<<<<<<Authentication Successful:PayLoadID:"+cXMLFields.getPayLoadId()+" >>>>>>>>");
    		else
    			log.info("<<<<<<<<<<Authentication Failure:PayLoadID:"+cXMLFields.getPayLoadId()+" >>>>>>>>");	
    		
    		logInfo("<<<<<<<<<<Authentication of incomming request >>>>>>>>");*/ 
    		
    		String custIdentity	= cXMLFields.getCustomerIdentity();
    		String AuthUserXPath = XPEDXWCUtils.getAuthUserXPathForCustomerIdentity(req, res, custIdentity);
    		
    		Document loginDoc = WCIntegrationXMLUtils.prepareLoginInputDoc(cXMLFields.getAuthUser(AuthUserXPath,custIdentity), cXMLFields.getAuthPassword());
    		if (log.isDebugEnabled())
                logDebug("Login Document:"+SCXmlUtils.getString(loginDoc));
            
    		securityResponse = SCUIPlatformUtils.login(loginDoc,SCUIContextHelper.getUIContext(req, res));
    		//TODO Call platform exposed method authenticate(loginDoc, request,response)once available; should return 
    		if(securityResponse.getReturnStatus())
    			logInfo("<<<<<<<<<<Authentication Successful:PayLoadID:"+cXMLFields.getPayLoadId()+" >>>>>>>>");
    		else
    			logInfo("<<<<<<<<<<Authentication Failure:PayLoadID:"+cXMLFields.getPayLoadId()+" >>>>>>>>");		
		
    	}
    	catch(Exception e){
    		logError("Exception during authentication:"+e.getMessage(),e);
    		throw new WCException("Exception", e);
    	}
		
    	return securityResponse;
 	} 
	
	
	
	
	protected WCIntegrationResponse processRequest (HttpServletRequest req,
    		HttpServletResponse res)
    throws ServletException, IOException{    	
    	try
    	{   
    		logInfo("<<<<<<<<<<Post Authentication process start >>>>>>>>");
    		
    		SCUISecurityResponse securityResponse = postAuthenticateUser(req, res);
    		if(YFCCommon.equals(SCUISecurityResponse.SUCCESS,securityResponse.getReturnStatus()))
    		{    			
    			if(!YFCCommon.isVoid(cXMLFields) && 
       					YFCCommon.equals(IAribaConstants.CXML_REQUEST_SETUP_TAG,cXMLFields.getCXMLRequestType()))
    			{       				
    				logInfo("<<<<<<<<<<Post Authentication Successful >>>>>>>>");
    				populateAribaContext(wcContext);
       				if(YFCCommon.equals(IAribaConstants.ARIBA_OPERATION_EDIT_STRING, cXMLFields.getOperation(), true)||
       						YFCCommon.equals(IAribaConstants.ARIBA_OPERATION_INSPECT_STRING, cXMLFields.getOperation(),true))
       				{
       					if(YFCCommon.isVoid(cXMLFields.getOrderHeaderKey()))
       					{
       						//Send failure response
           					errorMessage = "Mandatory parameter missing for EDIT/INSPECT: Order Header key is not present";
           		    		errorCode = new Long(IWCIntegrationStatusCodes.MANDATORY_PARAMETER_MISSING);
           		    		logError("Error Code:"+errorCode + "& ErrorDesc:"+ errorMessage);
           		    		return new WCIntegrationResponse(WCIntegrationResponse.FAILURE,new Error(errorCode,errorMessage));
       					}
       				}       				
    				try
       				{
       					CommerceContextHelper.processProcurementPunchIn(wcContext);
       				}
       		    	catch(Exception e)
       		    	{
       					//Send failure response
       					errorMessage = "Failed to process the commerce context";
       		    		errorCode = new Long(IWCIntegrationStatusCodes.COMMERCE_CONTEXT_FAILED);
       		    		logError("Error Code:"+errorCode + "& ErrorDesc:"+ errorMessage +"Exception:"+e.getMessage(),e);
       		    		return new WCIntegrationResponse(WCIntegrationResponse.FAILURE,new Error(errorCode,errorMessage));
       		    	}
       				String startPageURL = WCIntegrationHelper.getAribaStartPageURL(cXMLFields, wcContext);
       				if(!YFCCommon.isVoid(startPageURL))
       				{
       					//Prepare and send the response
       					cXMLFields.setStartPageURL(startPageURL);
       					//prepare and send successful response
       					errorMessage = ">>>>>>>>>>Request for Auth setup is success>>>>>>>>>>>>>.";
       		    		errorCode = new Long(IWCIntegrationStatusCodes.SUCCESS);
       		    		logInfo(">>>>>>>>>>>>>>>>Start page URL:"+startPageURL);
       		    		wcResponse = new WCIntegrationResponse(WCIntegrationResponse.SUCCESS,new Error(errorCode,errorMessage));
       				}
       				else
       				{
       					//Send failure response
       					errorMessage = "cannot form start page url:";
       		    		errorCode = new Long(IWCIntegrationStatusCodes.COMMERCE_CONTEXT_FAILED);
       		    		logError("Error Code:"+errorCode + "& ErrorDesc:"+ errorMessage);
       		    		return new WCIntegrationResponse(WCIntegrationResponse.FAILURE,new Error(errorCode,errorMessage));
       				}
       			}
       			else if(!YFCCommon.isVoid(cXMLFields) && 
       					YFCCommon.equals(IAribaConstants.CXML_REQUEST_ORDER_TAG,cXMLFields.getCXMLRequestType()))
       			{
       				//Invoke the helper class which will call the service.
       				wcResponse = WCIntegrationHelper.processAribaOrderRequest(cXMLFields.getRequestDoc(), wcContext); 
       				if(wcResponse.getReturnStatus())
       				{
       					//TODO Send success response
       					cXMLFields.setProcessStatus(true);
       				   //Send success response
       					errorMessage = "Order request processed successfully";
       		    		errorCode = new Long(IWCIntegrationStatusCodes.SUCCESS);
       		    		logInfo(">>>>>>>>>>>>>Order request processed successfully>>>>>>>>>>>>>>"); 
       		    		wcResponse = new WCIntegrationResponse(WCIntegrationResponse.SUCCESS,new Error(errorCode,errorMessage));
       		    		      		    		
       				}
       				else
       				{
       					cXMLFields.setProcessStatus(false);
       					//Send failure response
       					errorMessage = "Order request process failed:";
       		    		errorCode = new Long(IWCIntegrationStatusCodes.API_ERROR);
       		    		logError("Error Code:"+errorCode + "& ErrorDesc:"+ errorMessage);
       		    		return new WCIntegrationResponse(WCIntegrationResponse.FAILURE,new Error(errorCode,errorMessage));
       				}
       			}       			
    		}
    		else
    		{
    			//Send failure response
    			errorMessage = "Post Authentication failed";
        		errorCode = new Long(IWCIntegrationStatusCodes.REQUEST_AUTHENTICATION_FAILED);
        		logError("Error Code:"+errorCode + "& ErrorDesc:"+ errorMessage);
        		return new WCIntegrationResponse(WCIntegrationResponse.FAILURE,new Error(errorCode,errorMessage));        		
    		}    		
    	}
    	catch(Exception e)
    	{
			//Send failure response
			errorMessage = "Post Authentication failed";
    		errorCode = new Long(IWCIntegrationStatusCodes.REQUEST_AUTHENTICATION_FAILED);
    		logError("Error Code:"+errorCode + "& ErrorDesc:"+ errorMessage+e.getMessage(),e);
    		return new WCIntegrationResponse(WCIntegrationResponse.FAILURE,new Error(errorCode,errorMessage));
    	}
    	logInfo("<<<<<<<<<<Post Authentication process End >>>>>>>>");
    	return wcResponse;
 	
    }
	
	
	
	
	
	
	
	   private void populateAribaContext(IWCContext wcContext)
	    {   	
				AribaContextImpl aribaContext = AribaContextImpl.getInstance();
				aribaContext.setAribaOperation(cXMLFields.getOperation());
				aribaContext.setOrderHeaderKey(cXMLFields.getOrderHeaderKey());
				aribaContext.setReturnURL(cXMLFields.getReturnURL());
				aribaContext.setBuyerCookie(cXMLFields.getBuyerCookie());
	            aribaContext.setFromIdentity(cXMLFields.getFromIdentity());
				aribaContext.setToIdentity(cXMLFields.getToIdentity());
				aribaContext.setPayloadID(cXMLFields.getPayLoadId());
				wcContext.setWCAttribute(IProcurementContext.PROCUREMENT_CONTEXT_ATTRIBUTE_KEY, (IAribaContext)aribaContext, WCAttributeScope.SESSION);
				if (log.isDebugEnabled()){
	                logDebug(">>>>>>>>>>Details of AribaContext created:>>>>>>>>>>>.");
	                logDebug("IAribaContext.operation:"+cXMLFields.getOperation());
	                logDebug("IAribaContext.getOrderHeaderKey:"+cXMLFields.getOrderHeaderKey());
	                logDebug("IAribaContext.getReturnURL:"+cXMLFields.getReturnURL());
	                logDebug("IAribaContext.getBuyerCookie:"+cXMLFields.getBuyerCookie());
	                logDebug("IAribaContext.getFromIdentity:"+cXMLFields.getFromIdentity());
	                logDebug("IAribaContext.getToIdentity:"+cXMLFields.getToIdentity());
	                logDebug("IAribaContext.getToIdentity:"+cXMLFields.getPayLoadId());
	                               
				}		
	    }
	    
		public String getResponseDoc(Error errObj,boolean resStatus, IWCContext ctx)    {
			if(resStatus)
				wcResponse = new WCIntegrationResponse(WCIntegrationResponse.SUCCESS,errObj);
			else
				wcResponse = new WCIntegrationResponse(WCIntegrationResponse.FAILURE,errObj);
			String docString = WCIntegrationXMLUtils.prepareAribaResponseDoc(cXMLFields, wcResponse, ctx);		
			return docString;		
		}
	
	private static final Logger log = Logger.getLogger(XPEDXAribaIntegrationServlet.class);

}

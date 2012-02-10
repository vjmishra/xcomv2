package com.xpedx.nextgen.dashboard;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXInvokeOrderPlaceActions implements YIFCustomApi
{

	private static YFCLogCategory log;
	private static YIFApi api = null;
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXCreateChainedOrderService.xml";
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			log.error("API initialization error");
		}
	}
	
	public Document invokeActions(YFSEnvironment env, Document inputXML) 
	{
		Document getCustomerProfileDetailsDoc = null;
		
		log.info("The input xml in XPXInvokeOrderPlaceActions: "+ SCXmlUtil.getString(inputXML));
		
		try
		{
		   Element inputRoot = inputXML.getDocumentElement();
		
		   String buyerOrgCode = inputRoot.getAttribute("BuyerOrganizationCode");
		
		   if(buyerOrgCode!= null && buyerOrgCode.trim().length()>0)
		   {
			  //Getting the ship to customer profile details and setting in the env object which will be used throughout OP flow
			   getCustomerProfileDetailsDoc = createGetCustomerListInput(env,buyerOrgCode);
			   env.setTxnObject("ShipToCustomerProfile", getCustomerProfileDetailsDoc);
			   if(log.isDebugEnabled()){
			   		log.debug("The customer profile details are: "+SCXmlUtil.getString(getCustomerProfileDetailsDoc));
			   }
		   }
		 
		   //Invoking the order place actions which are
               
		   //1.   UpdatingWebConfNum, CreateSpecialChargeLine
		   
		   Document changeOrderOutputDocument = api.executeFlow(env,"XPXCreateSplChgLineAndWebConfNumService",inputXML);
		   
		   //2. Invoke rules engine and create and place the FO orders
		   changeOrderOutputDocument.getDocumentElement().setAttribute("OrderConfirmationFlow","Y");
		   		   
		   api.executeFlow(env, "XPXInvokeRulesEngineAndLegacyOrderCreationService", changeOrderOutputDocument);
		   
		   //3. Send Order confirmation emails(Not reqd at the moment as action component is still retained
		  // api.executeFlow(env, "XPXProcessOrdConfEmailService", inputXML);
		           
		   
		}
		
		catch(NullPointerException ne)
    	{
			
			  com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
			
		      errorObject.setTransType("OP");
		      errorObject.setErrorClass("Unexpected / Invalid");
		      errorObject.setInputDoc(inputXML);
		      		      
		      errorObject.setException(ne);
		
		      ErrorLogger.log(errorObject, env);
		
            return inputXML;
    	}
    	catch (YFSException yfe)
    	{
			
			  com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
				
			      errorObject.setTransType("OP");
			      errorObject.setErrorClass("Application");
			      errorObject.setInputDoc(inputXML);
			      		      
			      errorObject.setException(yfe);
			
			      ErrorLogger.log(errorObject, env);
			
	              return inputXML;
		} 
    	catch(Exception e)
    	{
			
			  com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
				
			      errorObject.setTransType("OP");
			      errorObject.setErrorClass("Application");
			      errorObject.setInputDoc(inputXML);
			      		      
			      errorObject.setException(e);
			
			      ErrorLogger.log(errorObject, env);
			
	              return inputXML;
		}
		
		
		return inputXML;
	}
	
	
	private Document createGetCustomerListInput(YFSEnvironment env, String buyerOrgCode) throws Exception
	{
		YFCDocument getCustomerDetailsInputDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER);
        getCustomerDetailsInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, buyerOrgCode);
                
        env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
		Document getCustomerDetailsOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerDetailsInputDoc.getDocument());
		
		if(log.isDebugEnabled()){
			log.debug("GETCustomerDetailsOutputDoc:"+SCXmlUtil.getString(getCustomerDetailsOutputDoc));
		}
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
        
        
		return getCustomerDetailsOutputDoc;
	}


	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

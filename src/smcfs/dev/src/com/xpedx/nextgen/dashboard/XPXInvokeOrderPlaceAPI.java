package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.stockcheck.api.XPXStockCheckReqRespAPI;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXInvokeOrderPlaceAPI implements YIFCustomApi
{
	private static YIFApi api = null;
	private static YFCLogCategory log;
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}
    public Document invokeOrderPlace(YFSEnvironment env, Document doc)
    {
    	try 
    	{
    		log.debug(SCXmlUtil.getString(doc));
			api.executeFlow(env, "XPXCreateSterlingOrderService", doc);
						
		} 
    	catch(NullPointerException ne)
    	{
    		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
			
		      errorObject.setTransType("B2B-PO");
		      errorObject.setErrorClass("Unexpected / Invalid");
		      errorObject.setInputDoc(doc);
		      		      
		      errorObject.setException(ne);
		
		      ErrorLogger.log(errorObject, env);
		
            return doc;
    	}
    	catch (YFSException yfe)
    	{
			//Handle the createDraftOrderException case...right now there is no logic.
			//e.printStackTrace();
			  com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
				
			      errorObject.setTransType("B2B-PO");
			      errorObject.setErrorClass("Application");
			      errorObject.setInputDoc(doc);
			      		      
			      errorObject.setException(yfe);
			
			      ErrorLogger.log(errorObject, env);
			
	              return doc;
		} 
    	catch(Exception e)
    	{
			//Handle the createDraftOrderException case...right now there is no logic.
			//e.printStackTrace();
			  com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
				
			      errorObject.setTransType("B2B-PO");
			      errorObject.setErrorClass("Application");
			      errorObject.setInputDoc(doc);
			      		      
			      errorObject.setException(e);
			
			      ErrorLogger.log(errorObject, env);
			
	              return doc;
		}
    	return doc;
    }
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

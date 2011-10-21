package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXInvokeRulesEngineServiceAPI implements YIFCustomApi
{

	private static YFCLogCategory log;
	private static YIFApi api = null;
	Properties props;
	
	static {
		log = YFCLogCategory.instance(XPXInvokeRulesEngineServiceAPI.class);
		
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Document invokeRulesEngine(YFSEnvironment env, Document inputXML) throws Exception
	{
		Document orderDocumentAfterRulesEngineInvocation = null;
		
		try 
		{			
			
			log.debug("The input to rules engine service is: "+SCXmlUtil.getString(inputXML));
			
			if("Y".equalsIgnoreCase(inputXML.getDocumentElement().getAttribute("OrderConfirmationFlow")))
			{
			orderDocumentAfterRulesEngineInvocation = api.executeFlow(env, XPXLiterals.RULES_ENGINE_SERVICE, inputXML);
			}
			else
			{
				//Create the input to getOrderList API call
				
				Document getOrderListInputDoc = YFCDocument.createDocument("Order").getDocument();
				if(inputXML.getDocumentElement().getAttribute("OrderHeaderKey")!=null && !"".equalsIgnoreCase(inputXML.getDocumentElement().getAttribute("OrderHeaderKey")))
				{
				   //If loop is added so that the getOrderList does not happen with an empty OHK, else it will do a full table scan	
				   getOrderListInputDoc.getDocumentElement().setAttribute("OrderHeaderKey", inputXML.getDocumentElement().getAttribute("OrderHeaderKey"));
				
				   orderDocumentAfterRulesEngineInvocation = api.executeFlow(env, XPXLiterals.RULES_ENGINE_SERVICE, getOrderListInputDoc);
				}
			}
			
			log.debug("The document being sent for hold check is: "+SCXmlUtil.getString(orderDocumentAfterRulesEngineInvocation));
			return orderDocumentAfterRulesEngineInvocation;
					
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return inputXML;
		
	}
	
	public void setProperties(Properties props) throws Exception {
		
		this.props = props;
		
	}

}

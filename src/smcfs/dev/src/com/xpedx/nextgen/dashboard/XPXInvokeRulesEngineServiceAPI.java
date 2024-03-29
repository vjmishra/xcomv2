package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXInvokeRulesEngineServiceAPI implements YIFCustomApi
{
	private static YFCLogCategory log;
	private static YIFApi api = null;
	Properties props;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			e.printStackTrace();
		}
	}
	
	public Document invokeRulesEngine(YFSEnvironment env, Document inputXML) throws Exception {
		
		if (log.isDebugEnabled()) {
			log.debug("XPXInvokeRulesEngineServiceAPI-InXML: "+SCXmlUtil.getString(inputXML));
		}
		
		String orderConfirmationFlow = "";
		Document rulesEngineOutDoc = null;
		Element rootElem = inputXML.getDocumentElement();
		boolean isWebHoldFlag=false;
		try
		{
			if (env instanceof ClientVersionSupport) {
				ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
				HashMap map = clientVersionSupport.getClientProperties();
				if (map != null && map.size()>0) {
					String isPlaceOnWebHold=(String)map.get("PlaceOrderOnWBHold");
					if(isPlaceOnWebHold != null && "PlaceOrderOnWBHold".equalsIgnoreCase(isPlaceOnWebHold))
					{
						isWebHoldFlag=true;
						System.out.println("**********   Order   Should go to Web Hold ********************");
					}
					else
					{
						System.out.println("**********   Order   Should not go to Web Hold ********************");
					}
				}
			}
		}
		catch(Exception e)
		{
			log.error("Error while getting webHoldFlag");
		}
		try {			
			
			if (rootElem.hasAttribute("OrderConfirmationFlow")) {
				orderConfirmationFlow = rootElem.getAttribute("OrderConfirmationFlow");
			}
			
			if("Y".equalsIgnoreCase(orderConfirmationFlow)) {
				// Order Confirmation Flow.
				rulesEngineOutDoc = api.executeFlow(env, XPXLiterals.RULES_ENGINE_SERVICE, inputXML);
			} else {
				// Order Approval Flow After Hold Release.
				String orderHeaderKey = rootElem.getAttribute("OrderHeaderKey");
				if(!YFCObject.isNull(orderHeaderKey) && !YFCObject.isVoid(orderHeaderKey)) {
				   Document getOrderListInputDoc = YFCDocument.createDocument("Order").getDocument();
				   getOrderListInputDoc.getDocumentElement().setAttribute("OrderHeaderKey", orderHeaderKey);
				
				   rulesEngineOutDoc = api.executeFlow(env, XPXLiterals.RULES_ENGINE_SERVICE, getOrderListInputDoc);
				}
			}
			if(isWebHoldFlag =true)
			{
				Element rulesEngineOutElem=rulesEngineOutDoc.getDocumentElement();
				ArrayList<Element> orderExtnList=SCXmlUtil.getElements(rulesEngineOutElem,"Extn");
				if(orderExtnList != null)
				{
					Element orderExtn=orderExtnList.get(0);
					if(orderExtn != null )
					{
						String webHoldFlag=orderExtn.getAttribute("ExtnWebHoldFlag");
						if( !"Y".equals(webHoldFlag))
							orderExtn.setAttribute("ExtnWebHoldFlag", "Y");	
						System.out.println("**********    Web Hold flag has been set to Y  ********************");
					}
				}
			}
			log.debug("XPXInvokeRulesEngineServiceAPI-OutXML: "+SCXmlUtil.getString(rulesEngineOutDoc));
			 YFCDate orderDate = new YFCDate();
			String orderPlaceDate = orderDate.getString();
			 rulesEngineOutDoc.getDocumentElement().setAttribute("OrderDate", orderPlaceDate);
			return rulesEngineOutDoc;
					
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

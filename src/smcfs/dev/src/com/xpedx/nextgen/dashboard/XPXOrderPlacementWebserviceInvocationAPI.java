package com.xpedx.nextgen.dashboard;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrder;
import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrderE;
import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrderResponseE;
import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.WsIpaperPlaceOrderStub;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXOrderPlacementWebserviceInvocationAPI implements YIFCustomApi {
		private static Properties props;
		//private static final Logger log = Logger.getLogger(XPXOrderPlacementWebserviceInvocationAPI.class);
		private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		
		public void setProperties(Properties arg0) throws Exception {
			// TODO Auto-generated method stub
			this.props = arg0;
		}

		public Document invokeWebService(YFSEnvironment env, Document inputXML)
				throws Exception {
			Document orderPlaceResponseDoc = null;
			String endPointURL = null;
			
			endPointURL = YFSSystem.getProperty("OrderPlaceWSDL");
			if(endPointURL ==  null || endPointURL.trim().length()<=0)
			{//No customer overrides entry so property value is retrieved from the SDF
			   endPointURL = props.getProperty("ENDPOINT_URL");
			}
			WsIpaperPlaceOrderStub testStub = new WsIpaperPlaceOrderStub(endPointURL);
			//Setting the time out for the Order placement to legacy also based on the email from pawan dated July 15th 2011
			Integer timeoutInMilliSecs = getTimeoutInMilliSecondsForWebService();
			testStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(timeoutInMilliSecs);
			//End of Setting the time out
			FPlaceOrderE inputOrderXml = new FPlaceOrderE();
			FPlaceOrder inputPlaceOrder = new FPlaceOrder();
			if(inputXML != null){
				log.debug("Input xml for OrderPlace to Legacy: "+SCXmlUtil.getString(inputXML));
			}
			
			String inputXMLString = SCXmlUtil.getString(inputXML);
			inputPlaceOrder.setWsIpaperPlaceOrderInput(inputXMLString);
			inputOrderXml.setFPlaceOrder(inputPlaceOrder);
			
			log.beginTimer("Calling-the-webmethod-OrderPlace-webservice");
			FPlaceOrderResponseE orderResponse = testStub.fPlaceOrder(inputOrderXml);
			log.endTimer("Calling-the-webmethod-OrderPlace-webservice");
			//Document outputXml = YFCDocument.getDocumentFor(orderResponse.getFPlaceOrderResponse().getWsIpaperPlaceOrderOutput()).getDocument();
			//YFCDocument outDoc = YFCDocument.getDocumentFor(orderResponse.getFPlaceOrderResponse().getWsIpaperPlaceOrderOutput());
			
			orderPlaceResponseDoc = YFCDocument.getDocumentFor(orderResponse.getFPlaceOrderResponse().getWsIpaperPlaceOrderOutput()).getDocument();
			
			if(null!=orderPlaceResponseDoc){
				String outputXMLString = SCXmlUtil.getString(orderPlaceResponseDoc);
				log.debug("Output xml for OrderPlace from Legacy: "+outputXMLString);
			}else{
				log.error("Output from Legcy for Order placement is empty");
			}
			/*****************************************************************************/
			/** Added by Arun Sekhar on 02-March-2011
			 * This attribute will be validated by the XPXPerformLegacyOrderUpdateAPI
			 * to distinguish between OrderPlace and OrderUpdate calls **/
			orderPlaceResponseDoc.getDocumentElement().setAttribute("IsOrderPlace", "Y");
			/*****************************************************************************/
			return orderPlaceResponseDoc;
			
			}
		
		private Integer getTimeoutInMilliSecondsForWebService(){
			//setting the time out for the order placement
			String timeout = null;
			Integer timeoutInSecs;
			
			timeout = YFSSystem.getProperty("OrderPlacementWebServiceTimeOut");
			if(timeout == null || timeout.trim().length() <= 0) 
			{// setting the default to 300 sec
				timeout = "300";
			}
			try {
				timeoutInSecs= Integer.parseInt(timeout);
			}
			catch (NumberFormatException e) {
				log.error("Exception: " + e.getStackTrace());
				timeoutInSecs = 300;
			}
			Integer timeoutInMilliSecs = timeoutInSecs*1000;
			return timeoutInMilliSecs;
		}

		
		

			

}

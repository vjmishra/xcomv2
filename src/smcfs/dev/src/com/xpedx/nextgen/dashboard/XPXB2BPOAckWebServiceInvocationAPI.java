package com.xpedx.nextgen.dashboard;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrder;
import zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderE;
import zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderResponseE;
import zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.WsIpaperSendOrderResponseStub;


import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXB2BPOAckWebServiceInvocationAPI implements YIFCustomApi
{
	private static Properties props;
	private static YIFApi api = null;
	private static YFCLogCategory log;
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}
	
	public Document invokeWebService(YFSEnvironment env, Document inputXML)
	throws Exception
	{
		
		String strExtnIsProcessedFlag = "Y";
		api = YIFClientFactory.getInstance().getApi();
		try{
			
		
			String endPointURL = null;
			
			endPointURL = YFSSystem.getProperty("B2BPOAckWSDL");
			if(endPointURL ==  null || endPointURL.trim().length()<=0)
			{//No customer overrides entry so property value is retrieved from the SDF
			   endPointURL = props.getProperty("ENDPOINT_URL");
			}
		
		WsIpaperSendOrderResponseStub orderAckStub = new WsIpaperSendOrderResponseStub(endPointURL);
		FPlaceOrderE inputOrderXml = new FPlaceOrderE();
		FPlaceOrder inputPlaceOrder = new FPlaceOrder();
		String inputXMLString = SCXmlUtil.getString(inputXML);
		if(log.isDebugEnabled()){
		log.debug("The input string is: "+inputXMLString);
		}
		inputPlaceOrder.setWsIpaperOrderResponseInput(inputXMLString);
		inputOrderXml.setFPlaceOrder(inputPlaceOrder);
		
		//Response need not be sent at all on PO-ACK
		log.beginTimer("Calling-the-webmethod-B2BPOAck-webservice");
		FPlaceOrderResponseE orderResponse = orderAckStub.fSendOrderResponse(inputOrderXml);
		log.endTimer("Calling-the-webmethod-B2BPOAck-webservice");
		
		/**
		 * catch block modified by Arun Sekhar on 28-Jan-2011 for CENT tool
		 * integration *
		 */
		
		} /*catch(Exception ex) {
			strExtnIsProcessedFlag = "N";
			
		}*/
		catch (NullPointerException ne) {
			strExtnIsProcessedFlag = "N";
			log.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.PO_ACK_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inputXML);
			throw ne;
		} catch (YFSException yfe) {
			strExtnIsProcessedFlag = "N";
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.PO_ACK_TRANS_TYPE,
					XPXLiterals.YFE_ERROR_CLASS, env, inputXML);
			throw yfe;
		} catch (Exception e) {
			strExtnIsProcessedFlag = "N";
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.PO_ACK_TRANS_TYPE,
					XPXLiterals.E_ERROR_CLASS, env, inputXML);
			throw e;
		}
		
		// Stamping the Processed flag to Yes.
		Document inputChangeOrderDoc = SCXmlUtil.createDocument("Order");
		Element inputChangeOrderElement = inputChangeOrderDoc.getDocumentElement();
		String strOrderHeaderKey = env.getTxnObject("orderHeaderKey").toString();
		if(log.isDebugEnabled()){
		log.debug("strOrderHeaderKey :" + strOrderHeaderKey);
		}
		inputChangeOrderElement.setAttribute("OrderHeaderKey",strOrderHeaderKey );
		Element extnElement = inputChangeOrderDoc.createElement("Extn");
		extnElement.setAttribute("ExtnIsProcessedFlag", strExtnIsProcessedFlag);
		inputChangeOrderElement.appendChild(extnElement);
		api.invoke(env, "changeOrder", inputChangeOrderDoc);
		
		return inputXML;
	}
	
	
	/**
	 * @This method prepares the error object with the exception details which
	 *       in turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType,
			String errorClass, YFSEnvironment env, Document inXML) {
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		this.props = arg0;
	}

}

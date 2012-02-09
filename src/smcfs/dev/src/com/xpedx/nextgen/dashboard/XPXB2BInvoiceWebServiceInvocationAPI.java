package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zwm1.com.ipaper.xpedx.wm.web.customerinvoice.wsipapersendinvoice.FPlaceOrder;
import zwm1.com.ipaper.xpedx.wm.web.customerinvoice.wsipapersendinvoice.FPlaceOrderE;
import zwm1.com.ipaper.xpedx.wm.web.customerinvoice.wsipapersendinvoice.FPlaceOrderResponseE;
import zwm1.com.ipaper.xpedx.wm.web.customerinvoice.wsipapersendinvoice.WsIpaperSendInvoiceStub;


import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXB2BInvoiceWebServiceInvocationAPI implements YIFCustomApi{

	private static Properties props;
	private static YIFApi api = null;
	private static YFCLogCategory log;
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}
	
	
	public Document invokeWebService(YFSEnvironment env, Document inputXML)
	throws Exception
	{
		String endPointURL = null;
		
		endPointURL = YFSSystem.getProperty("InvoiceWSDL");
		if(endPointURL ==  null || endPointURL.trim().length()<=0)
		{//No customer overrides entry so property value is retrieved from the SDF
		   endPointURL = props.getProperty("ENDPOINT_URL");
		}
		
		WsIpaperSendInvoiceStub invoiceStub = new WsIpaperSendInvoiceStub(endPointURL);
		FPlaceOrderE inputOrderXml = new FPlaceOrderE();
		FPlaceOrder inputPlaceOrder = new FPlaceOrder();
		String inputXMLString = SCXmlUtil.getString(inputXML);
		if(log.isDebugEnabled()){
		log.debug("The input string is: "+inputXMLString);
		}
		inputPlaceOrder.setWsIpaperInvoiceInput(inputXMLString);
		inputOrderXml.setFPlaceOrder(inputPlaceOrder);
		
        //Response need not be sent at all on B2B invoice
		if(log.isDebugEnabled()){
		log.debug(" invoiceStub.fSendInvoice inputXML : " + SCXmlUtil.getString(inputXML));
		}
		
		//comment below line for local testing only when there s no stub or webservice
		log.beginTimer("Calling-the-webmethod-B2BInvoice-webservice");
		FPlaceOrderResponseE orderResponse = invoiceStub.fSendInvoice(inputOrderXml);
		log.endTimer("Calling-the-webmethod-B2BInvoice-webservice");
		
		//call changeXPXInvoiceHdrService and update processed flag for xpxinvoicehdr table
		callchangeXPXInvoiceHdrService(env);
		
		
		return inputXML;
	}	
	
	private void callchangeXPXInvoiceHdrService(YFSEnvironment env) throws ParserConfigurationException, FactoryConfigurationError, YIFClientCreationException, YFSException, RemoteException {
		if(log.isDebugEnabled()){
		log.debug("entering callchangeXPXInvoiceHdrService in  XPXB2BInvoiceWebServiceInvocationAPI");
		}
		String strInvoiceHeaderKey = env.getTxnObject("strInvoiceHeaderKey").toString();
		if(log.isDebugEnabled()){
		log.debug(" strInvoiceHeaderKey :" + strInvoiceHeaderKey);
		}
		
		//prepare input xml for changeXPXInvoiceHdrService
		//sample <XPXInvoiceHdr InvoiceHeaderKey="20110112192944487414" ProcessedFlag="Y"/>
		Document docXPXInvoiceHdr = SCXmlUtil.getDocumentBuilder().newDocument();
		Element XPXInvoiceHdr = docXPXInvoiceHdr.createElement("XPXInvoiceHdr");
		docXPXInvoiceHdr.appendChild(XPXInvoiceHdr);
		XPXInvoiceHdr.setAttribute("InvoiceHeaderKey", strInvoiceHeaderKey);
		XPXInvoiceHdr.setAttribute("ProcessedFlag", "Y");
		api = YIFClientFactory.getInstance().getApi();
		Document changeXPXInvoiceHdrOutDoc =  api.executeFlow(env, "changeXPXInvoiceHdrService", docXPXInvoiceHdr);
		
		if(log.isDebugEnabled()){
		log.debug(" changeXPXInvoiceHdrOutDoc :" + SCXmlUtil.getString(changeXPXInvoiceHdrOutDoc));
		}	
		
	}

	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		this.props = arg0;
	}

}

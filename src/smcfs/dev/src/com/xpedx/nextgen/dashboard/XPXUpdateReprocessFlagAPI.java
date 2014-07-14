package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXUpdateReprocessFlagAPI implements YIFCustomApi {

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
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	
	public Document updateReprocessFlagForCustOrder(YFSEnvironment env,Document createdOrderDocument) throws Exception  
	{
		try{

			//Retrieve ref Order Header key from the environment object
			
			String referenceOrderHeaderKey= (String) env.getTxnObject("ReferenceOrderHeaderKey");
			log.debug("The reference order header key in XPXUpdateReprocessFlag is: "+referenceOrderHeaderKey);
			
			Document changeRefOrderInputDocument = YFCDocument.createDocument("XPXRefOrderHdr").getDocument();
			changeRefOrderInputDocument.getDocumentElement().setAttribute("RefOrderHdrKey", referenceOrderHeaderKey);
			changeRefOrderInputDocument.getDocumentElement().setAttribute("IsReprocessibleFlag", "N");
			//changeRefOrderInputDocument.getDocumentElement().setAttribute("OrderProcessedFlag", "N");
			
			log.debug("The input to change XPX Reference order header is: "+SCXmlUtil.getString(changeRefOrderInputDocument));
			api.executeFlow(env,"changeXPXRefOrderHdr",changeRefOrderInputDocument);
			
		
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return createdOrderDocument;
	}

}

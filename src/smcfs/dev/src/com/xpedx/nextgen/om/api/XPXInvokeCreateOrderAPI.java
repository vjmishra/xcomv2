package com.xpedx.nextgen.om.api;

import java.util.Properties;

import org.w3c.dom.Document;

import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * 
 * @author Administrator
 * Invokes XPXCreateSterlingOrderService
 * and returns the output as is 
 * and throws exception as is if any
 */

public class XPXInvokeCreateOrderAPI implements YIFCustomApi{
		
	private static YIFApi api = null;
	YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	
	public Document invokeCreateOrderAPI(YFSEnvironment env, Document inXML) throws Exception {
		if(log.isDebugEnabled()){
			log.debug("XPXInvokeCreateOrderAPI_InXML:" + (YFCDocument.getDocumentFor(inXML)).toString());
		}
		api = YIFClientFactory.getInstance().getApi();
		
		Document outDoc =  api.executeFlow(env, XPXLiterals.SERVICE_XPX_CREATE_CUSTOMER_ORDER_FOR_INBOUND_B2B_OP_XML, inXML);
		if(log.isDebugEnabled()){
			log.debug("XPXInvokeCreateOrderAPI_OutXML: " + (YFCDocument.getDocumentFor(outDoc)).toString());
		}
		return outDoc;	
	}

	/**
	 * not used as of now
	 */
	public void setProperties(Properties arg0) throws Exception {
		// not used as of now
		
	}

}

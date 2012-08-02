package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXOrderApprovalService implements YIFCustomApi
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
	
	public Document invokeOrderApproval(YFSEnvironment env, Document inputXML) throws Exception {
		
		if (log.isDebugEnabled()) {
			log.debug("XPXOrderApprovalService-InXML: "+SCXmlUtil.getString(inputXML));
		}
		
		Document rulesEngineOutDoc = null;
		Element rootElem = inputXML.getDocumentElement();
		
		try {			
			// Order Approval Flow After Hold Release.
			String orderHeaderKey = rootElem.getAttribute("OrderHeaderKey");
			if(!YFCObject.isNull(orderHeaderKey) && !YFCObject.isVoid(orderHeaderKey)) {
			   Document rulesEngineInputDoc = YFCDocument.createDocument("Order").getDocument();
			   rulesEngineInputDoc.getDocumentElement().setAttribute("OrderHeaderKey", orderHeaderKey);			
			   rulesEngineOutDoc = api.executeFlow(env, "XPXInvokeRulesEngineAndLegacyOrderCreationService", rulesEngineInputDoc);
			}
			
			if (log.isDebugEnabled()) {
				log.debug("XPXOrderApprovalService-OutXML: "+SCXmlUtil.getString(rulesEngineOutDoc));
			}
			return rulesEngineOutDoc;
					
		} catch (YFSException ye) {
			// TODO Auto-generated catch block
			ye.printStackTrace();
		} catch (RemoteException re) {
			// TODO Auto-generated catch block
			re.printStackTrace();
		}
		return inputXML;
		
	}
	
	public void setProperties(Properties props) throws Exception {
		
		this.props = props;
		
	}

}

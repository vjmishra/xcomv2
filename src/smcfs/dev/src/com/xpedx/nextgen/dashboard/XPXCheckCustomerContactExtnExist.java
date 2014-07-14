package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXCheckCustomerContactExtnExist implements YCPDynamicConditionEx
{
	private static YFCLogCategory log;
	private static YIFApi api = null;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean evaluateCondition(YFSEnvironment env, String arg1, Map arg2, Document inputXML)
	{	
		boolean custContactExtnExist = false;
		String custContactID = "";
		String customerKey = "";
		
		Element rootElem = inputXML.getDocumentElement();
		custContactID = rootElem.getAttribute("CustomerContactID");		
		if(!YFCObject.isNull(custContactID) && !YFCObject.isVoid(custContactID)){
			YFCDocument inputDoc = YFCDocument.createDocument("XPXCustomercontactExtn");
			inputDoc.getDocumentElement().setAttribute("CustomerContactID", custContactID);
			inputDoc.getDocumentElement().setAttribute("CustomerKey", customerKey);
			
			try {
				Document outputListDoc = api.executeFlow(env, "getXPXCustContExtnList", inputDoc.getDocument());
				if(!YFCDocument.isNull(outputListDoc)){					
					Element rootElem_ = outputListDoc.getDocumentElement();
					if(rootElem_.hasChildNodes()){
						custContactExtnExist = true;
					}
				} else {
					throw new Exception("getXPXCustContExtnList service has failed.");
				}											
			} catch (YFSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		
		log.debug("Customer Contact Extn information exist: " + custContactExtnExist);
		return custContactExtnExist;
	}

	public void setProperties(Map arg0) {
		// TODO Auto-generated method stub
		
	}

}

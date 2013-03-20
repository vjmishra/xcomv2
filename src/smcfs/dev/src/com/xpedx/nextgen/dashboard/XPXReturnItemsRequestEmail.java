package com.xpedx.nextgen.dashboard;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXEmailUtil;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXReturnItemsRequestEmail implements YIFCustomApi {
	private static YFCLogCategory yfcLog;

	static {
		yfcLog = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}
	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	
	public Document sendReturnItemsRequestEmail(YFSEnvironment env, Document inXML)	throws Exception
	{
		String inputXML=SCXmlUtil.getString(inXML);
		if(yfcLog.isDebugEnabled())
			yfcLog.debug("Begin - Inside sendReturnItemsRequestEmail method of XPXReturnItemsRequestEmail. Input XML : "+ inputXML);
		
		Element rtrnItemsReqEmailElem=inXML.getDocumentElement();
		String emailType=XPXEmailUtil.RETURN_ITEMS_REQUEST_EMAIL_TYPE;
        String emailFrom=SCXmlUtil.getXpathAttribute(rtrnItemsReqEmailElem, "/Emails/Email/@FromEmail");
        String businessIdentifier=SCXmlUtil.getXpathAttribute(rtrnItemsReqEmailElem, "/Emails/Email/@WCNumber");
        String emailSubject = XPXEmailUtil.RETURN_ITEMS_REQUEST_EMAIL_SUBJECT;
        XPXEmailUtil.insertEmailDetailsIntoDB(env, inputXML, emailType, emailSubject, emailFrom, "",businessIdentifier);
		
		if(yfcLog.isDebugEnabled())
			yfcLog.debug("End - Inside sendReturnItemsRequestEmail method of XPXReturnItemsRequestEmail.");
		
		return inXML;
	}

}

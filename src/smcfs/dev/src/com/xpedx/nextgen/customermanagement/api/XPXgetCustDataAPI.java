package com.xpedx.nextgen.customermanagement.api;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;


public class XPXgetCustDataAPI implements YIFCustomApi{
	private static YIFApi api = null;
	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");

	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	public void getCustomerData(YFSEnvironment env,Document inXML)throws Exception
	
	{
		api = YIFClientFactory.getInstance().getApi();
		if(log.isDebugEnabled()){
			log.debug("XML File for getCustomerData :"+YFCDocument.getDocumentFor(inXML));
		}
		
	}
	}
	
	


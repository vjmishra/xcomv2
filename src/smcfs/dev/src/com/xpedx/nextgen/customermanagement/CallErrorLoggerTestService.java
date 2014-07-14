package com.xpedx.nextgen.customermanagement;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.tools.datavalidator.XmlUtils;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class CallErrorLoggerTestService implements YIFCustomApi{

	private static YFCLogCategory log = YFCLogCategory
	.instance(CallErrorLoggerTestService.class);
	private static Logger log4jLogger = YFCLogCategory.getLogger("cent123");
	
	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	public Document invoke(YFSEnvironment yfsEnv, Document inXML)
	 {
		
//		inXML
//		<Error ErrorClass="" TransType=""/>
		Element rootElem = inXML.getDocumentElement();
		log.error("CallErrorLoggerTestService i/p Doc from YFCLogCategory: "+XmlUtils.getString(rootElem));
		log4jLogger.error("CallErrorLoggerTestService i/p Doc from log4jLogger : "+XmlUtils.getString(rootElem));
		
		com.xpedx.nextgen.common.cent.Error error = new com.xpedx.nextgen.common.cent.Error();
		
		error.setTransType(rootElem.getAttribute("TransType"));
		error.setErrorClass(rootElem.getAttribute("ErrorClass"));
		error.setInputDoc(inXML);
		if(rootElem.getAttribute("ErrorClass").equalsIgnoreCase("Application")){
			try{
				int i = 5/0;
			}
			catch(Exception e){
				error.setException(e);
//				ErrorLogger.log(error, yfsEnv);
			}
		}
		log.debug("Invoking the error logger");
		ErrorLogger.log(error, yfsEnv);
		log.debug("Back To Test Service's API");
		return inXML;
	}
	

}

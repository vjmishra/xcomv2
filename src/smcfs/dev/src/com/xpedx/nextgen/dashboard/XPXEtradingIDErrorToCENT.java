package com.xpedx.nextgen.dashboard;

import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author Stanislaus Joseph John.
 *  Throws the error to the CENT tool.
 */
public class XPXEtradingIDErrorToCENT implements YIFCustomApi {

	/** API object. */
	private static YIFApi api = null;

	/** YFCLogCategory object */
	private static YFCLogCategory log;

	static {
		log = (YFCLogCategory) YFCLogCategory
		.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException yifce) {
			yifce.printStackTrace();
		}
	}

	public Document throwEtradingIdError(YFSEnvironment env, Document inXML)
			throws Exception {
		
		String errorDescription = "";
		String reason = "";
		Element parentElement = inXML.getDocumentElement();
		
		reason = parentElement.getAttribute("EtradingIDReason");
		if(reason != null && !reason.isEmpty())
		{
			if(reason.equalsIgnoreCase("E"))
			{
				errorDescription = "Empty value for EtradingId...thrown from XPXCheckETradingIDCondition.java";
			}else if (reason.equalsIgnoreCase("I")){
				errorDescription = "Etrading sent in input xml is invalid...thrown from XPXCheckETradingIDCondition.java";
			}else{
				errorDescription = "Etrading element is not sent in input xml...thrown from XPXCheckETradingIDCondition.java";
			}
		}		
		// Exception has been thrown to handle at CENT tool.
		YFSException exceptionMessage = new YFSException();
		exceptionMessage.setErrorDescription(errorDescription);		
		log.error(errorDescription);				
		throw exceptionMessage;		
	}

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}

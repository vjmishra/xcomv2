package com.xpedx.nextgen.dashboard;

/**
 * @author Stanislaus Joseph John.
 *  Gets the Customer Key from transaction object.
 *  
 *  Customer key has been set in XPXSetCustomerKeyInTransaction.java
 *  
 */

import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXGetCustomerKeyFromTransaction implements YIFCustomApi
{
	private static YFCLogCategory log;
	private static YIFApi api = null;
	
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
	
	public Document getCustomerKeyFromTransation(YFSEnvironment env, Document inputXML) throws Exception
	{				
		String customerKeyStr = "";
		Object customerKeyObj = env.getTxnObject("CustomerKey");
		if(!YFCObject.isNull(customerKeyObj)){
			customerKeyStr = (String) customerKeyObj;
		} else {
			throw new Exception("Customer Key cannot be null in XPXGetCustomerKeyFromTransaction");
		}
		log.debug("XPXGetCustomerKeyFromTransaction_CustomerKey" + customerKeyStr);
		
		Element rootElem = inputXML.getDocumentElement();
		rootElem.setAttribute("CustomerKey", customerKeyStr);		
		return inputXML;		
	}

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}	

}

package com.xpedx.nextgen.dashboard;

/**
 * @author Stanislaus Joseph John.
 *  Sets the Customer Key in transaction object.
 *  
 *  Customer key will be used in XPXGetCustomerKeyFromTransaction.java
 *  
 */

import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXSetCustomerKeyInTransaction implements YIFCustomApi
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
	
	public Document setCustomerKeyInTransaction(YFSEnvironment env, Document inputXML) throws Exception
	{				
		String customerKey = "";
		Element customerElem = inputXML.getDocumentElement();
		if(customerElem.hasAttribute("CustomerKey")){
		customerKey = customerElem.getAttribute("CustomerKey");
		} else {
			throw new Exception("Customer Key cannot be null in XPXSetCustomerKeyInTransaction");
		}				
		log.debug("XPXSetCustomerKeyInTransaction_CustomerKey:" + customerKey);		
		env.setTxnObject("CustomerKey", customerKey);	
		return inputXML;		
	}

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}	

}

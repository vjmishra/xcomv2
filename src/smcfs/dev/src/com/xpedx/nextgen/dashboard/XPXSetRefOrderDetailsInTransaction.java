package com.xpedx.nextgen.dashboard;

/**
 * @author Stanislaus Joseph John.
 *  Sets the Reference Order Header Key and a map with Reference Order Line key in transaction object.
 *  
 *  RefOrdHdrKey and RefOrdLineKey in transaction object will be used to update invalid etrading Id, Item Id and UOM 
 *  in XPXB2BOrderTranslationAPI.java
 *  
 */


import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;


public class XPXSetRefOrderDetailsInTransaction implements YIFCustomApi
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
	
	
	public Document setRefDetailsInTransation(YFSEnvironment env, Document inputXML)
	{		
		if(inputXML != null){
			log.debug("Input xml for setRefDetailsInTransation in XPXSetRefOrderDetailsInTransaction is :  " + SCXmlUtil.getString(inputXML));
		}
		NodeList xpxRefOrderHdrList = inputXML.getElementsByTagName("XPXRefOrderHdr");
		Element xpxRefOrderHdrElement = (Element) xpxRefOrderHdrList.item(0);
		Document tranInputDoc = YFCDocument.createDocument().getDocument();
		tranInputDoc.appendChild(tranInputDoc.importNode(xpxRefOrderHdrElement, true));
		if(log.isDebugEnabled()){
			log.debug("Output xml for setRefDetailsInTransation in XPXSetRefOrderDetailsInTransaction is :  " + SCXmlUtil.getString(tranInputDoc));
		}
		// To set the RefOrdHdrKey and RefOdrLineKey in transaction object.
		XPXCreateReferenceOrderAPI.setRefDetailsInTransation(env, tranInputDoc);
		return inputXML;		
	}


	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}	

}

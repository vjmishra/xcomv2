package com.xpedx.nextgen.dashboard;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.YCPDynamicConditionEx;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXCheckFOAvailable implements YCPDynamicConditionEx
{
	private static YFCLogCategory log;
	private static YIFApi api = null;
	
	String getOrderListTemplate = "global/template/api/getOrderList.checkFOAvailable.xml";
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
		boolean isFOAvailable = false;
		
		try
		{
		   
		
		   Element orderRootElement = inputXML.getDocumentElement();
		
	       Element orderExtnElement =  (Element) orderRootElement.getElementsByTagName("Extn").item(0);
		
	       String webConfNumber = orderExtnElement.getAttribute("ExtnWebConfNum");
	    
	       Document getOrderListInputDoc = YFCDocument.createDocument("Order").getDocument();
	       getOrderListInputDoc.getDocumentElement().setAttribute("OrderType", "Customer");
	       getOrderListInputDoc.getDocumentElement().setAttribute("OrderTypeQryType", "NE");
	       Element getOrderListInputExtn = getOrderListInputDoc.createElement("Extn");
	       getOrderListInputExtn.setAttribute("ExtnWebConfNum", webConfNumber);
	       getOrderListInputDoc.getDocumentElement().appendChild(getOrderListInputExtn);
	    
	       env.setApiTemplate("getOrderList", getOrderListTemplate);
	       Document getOrderListOutputDoc = api.invoke(env, "getOrderList", getOrderListInputDoc);
	       env.clearApiTemplate("getOrderList");
	       
	       
	       if(getOrderListOutputDoc.getDocumentElement().getElementsByTagName("Order").getLength() > 0)
	       {
	    	   //Fulfillment orders exist
	    	   isFOAvailable = true;
	       }
	    
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return isFOAvailable;
	 }

	

	public void setProperties(Map arg0) {
		// TODO Auto-generated method stub
		
	}

}

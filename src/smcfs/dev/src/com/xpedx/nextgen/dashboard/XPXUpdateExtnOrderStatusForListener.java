package com.xpedx.nextgen.dashboard;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXUpdateExtnOrderStatusForListener implements YIFCustomApi {
	private static YIFApi api = null;
	private static YFCLogCategory log;
	private String getOrderListTemplate = "global/template/api/getOrderList.XPXUpdateExtnOrderStatus.xml";
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException yifEx) {
			yifEx.printStackTrace();
		}
	}
	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public Document updateCustomerExtnOrderStatus(YFSEnvironment env,Document inXML) throws Exception {
		String orderStatus = "";
		String orderStatusPrefix = "";
		Element rootElement = inXML.getDocumentElement();
		log.debug("updateCustomerExtnOrderStatusrootElement:" + SCXmlUtil.getString(rootElement));
		if((rootElement.getOwnerDocument().getDocumentElement().getNodeName()).equalsIgnoreCase("OrderLine")) {
			Element orderElem = SCXmlUtil.getChildElement(rootElement, "Order");
			if(orderElem!=null)
			{
				String cOrderHeaderKey =  orderElem.getAttribute("OrderHeaderKey");
				
				String maxOrderStatus = orderElem.getAttribute("MaxOrderStatus");
				String minOrderStatus = orderElem.getAttribute("MinOrderStatus");
				
				if(maxOrderStatus!=null && minOrderStatus!=null && maxOrderStatus.equals(minOrderStatus))
					orderStatus = maxOrderStatus;
				else if(!maxOrderStatus.equals(minOrderStatus)){
					orderStatus = maxOrderStatus;
					orderStatusPrefix = "Partially";
				}
				if(YFCCommon.isVoid(orderStatus))
					orderStatus = XPXLiterals.ORDER_PLACED_STATUS;
				//Update the Customer Order Status
				Document changeOrderDoc = YFCDocument.createDocument("Order").getDocument();
				Element changeOrderElement = changeOrderDoc.getDocumentElement();
				changeOrderElement.setAttribute("OrderHeaderKey", cOrderHeaderKey);
				changeOrderElement.setAttribute("Override", "Y");
				Element extnElementInCODoc = SCXmlUtil.createChild(changeOrderElement, "Extn");
				extnElementInCODoc.setAttribute("ExtnOrderStatus", orderStatus);
				extnElementInCODoc.setAttribute("ExtnOrderStatusPrefix", orderStatusPrefix);
					
				log.debug("Calling changeOrder with Input\n");
				log.debug("---------------------------------------------\n");
				log.debug(SCXmlUtil.getString(changeOrderDoc)+"\n");
				log.debug("---------------------------------------------\n");
				Document outDoc = api.invoke(env, "changeOrder", changeOrderDoc);
				
				
			}
				
		}
		return inXML;
	}

}

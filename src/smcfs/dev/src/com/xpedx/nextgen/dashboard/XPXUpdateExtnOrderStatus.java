/**
 * 
 */
package com.xpedx.nextgen.dashboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.customerprofilerulevalidation.api.XPXCustomerProfileRuleConstant;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * @author Administrator
 *
 */
public class XPXUpdateExtnOrderStatus implements YIFCustomApi{
	
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
	
	public Document updateExtnOrderStatus(YFSEnvironment env,Document inXML) throws Exception {
		
		String orderStatus = "";
		String orderStatusPrefix = "";
		String fOrderHeaderKey = "";
		String orderType = "";
		Document outDoc = null;
		boolean isCancelEventTriggered = false;
		log.debug("ExtnOrderStatusUpdate_InputXML:" + SCXmlUtil.getString(inXML));
		Element rootElement = inXML.getDocumentElement();		
		if((rootElement.getOwnerDocument().getDocumentElement().getNodeName()).equalsIgnoreCase("OrderStatusChange")){
			// For change order status.
			orderStatus = rootElement.getAttribute("BaseDropStatus");
			fOrderHeaderKey = rootElement.getAttribute("OrderHeaderKey");
			
		} else if((rootElement.getOwnerDocument().getDocumentElement().getNodeName()).equalsIgnoreCase("Order")) {
			// For change order on cancellation.
			orderType = rootElement.getAttribute("OrderType");
			// To check whether all the quantities in the line has been cancelled.
			String maxOrderStatus = rootElement.getAttribute("MaxOrderStatus");
			String minOrderStatus = rootElement.getAttribute("MinOrderStatus");
			if(!orderType.equalsIgnoreCase("Customer") && minOrderStatus.equals(XPXLiterals.ORDER_CANCELLED_STATUS) && 
					maxOrderStatus.equals(XPXLiterals.ORDER_CANCELLED_STATUS)){
				// Order cancelled status is '9000'
				isCancelEventTriggered = true;
				orderStatus = XPXLiterals.ORDER_CANCELLED_STATUS;
			}
			else {
				orderStatus = maxOrderStatus;
			}
			if(YFCCommon.isVoid(orderStatus))
				orderStatus = XPXLiterals.ORDER_PLACED_STATUS;
			
			fOrderHeaderKey = rootElement.getAttribute("OrderHeaderKey");
			if(!maxOrderStatus.equals(minOrderStatus))
					orderStatusPrefix = "Partially";
		}
		
		log.debug("Updating the status of order = " + fOrderHeaderKey + " with "+orderStatusPrefix + " " +orderStatus);
		
		Element extnRootElement = SCXmlUtil.getChildElement(rootElement, "Extn");
		if(extnRootElement!=null){
			extnRootElement.setAttribute("ExtnOrderStatus", orderStatus);
			extnRootElement.setAttribute("ExtnOrderStatusPrefix", orderStatusPrefix);
			rootElement.setAttribute("Override", "Y");
			log.debug("Calling changeOrder with Input\n");
			log.debug("---------------------------------------------\n");
			log.debug(SCXmlUtil.getString(inXML)+"\n");
			log.debug("---------------------------------------------\n");
			outDoc = api.executeFlow(env,"XPXUpdateExOrderStatus", inXML); 
		}
		else{
			Document changeOrderDoc = YFCDocument.createDocument("Order").getDocument();
			Element changeOrderElement = changeOrderDoc.getDocumentElement();
			changeOrderElement.setAttribute("OrderHeaderKey", fOrderHeaderKey);
			changeOrderElement.setAttribute("Override", "Y");
			Element extnElementInCODoc = SCXmlUtil.createChild(changeOrderElement, "Extn");
			extnElementInCODoc.setAttribute("ExtnOrderStatus", orderStatus);
			extnElementInCODoc.setAttribute("ExtnOrderStatusPrefix", orderStatusPrefix);
				
			log.debug("Calling changeOrder with Input\n");
			log.debug("---------------------------------------------\n");
			log.debug(SCXmlUtil.getString(changeOrderDoc)+"\n");
			log.debug("---------------------------------------------\n");
			Element pendignElement=changeOrderDoc.createElement("PendingChanges");
			pendignElement.setAttribute("IgnorePendingChanges", "Y");
			changeOrderDoc.getDocumentElement().appendChild(pendignElement);

			outDoc = api.executeFlow(env, "XPXUpdateExOrderStatus", changeOrderDoc);
		}
		if(outDoc!=null)
			log.debug("O/P of changeOrder is \n"+SCXmlUtil.getString(outDoc));
		
		/* If the fulfillment order status is canceled , update the customer order status.
		 * OU - Line Process Code 'C' with quantity '0'. Only fulfillment order will  be Cancelled and the customer order status should be modified accordingly. */
		if(!orderType.equalsIgnoreCase("Customer") && !YFCCommon.isVoid(orderStatus) && isCancelEventTriggered)
		{
			// Order cancelled status is '9000'
			Document fOrderOutputDoc = null;
			Document fOrderInputDoc = YFCDocument.createDocument("Order").getDocument();
			fOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, fOrderHeaderKey);		
			// To set API template
			env.setApiTemplate("getOrderList", getOrderListTemplate);
			
			fOrderOutputDoc = api.invoke(env,"getOrderList", fOrderInputDoc);
			log.debug("Fulfillment order details = " + SCXmlUtil.getString(fOrderOutputDoc));
			
			// To clear API template for getOrderList
			env.clearApiTemplate("getOrderList");
			String cOrderHeaderKey = null;
			NodeList orderLineList = fOrderOutputDoc.getElementsByTagName("OrderLine");
			if(orderLineList.getLength() > 0) {
				Element fOrderLineElement = (Element)orderLineList.item(0);
				if(fOrderLineElement!=null)
					cOrderHeaderKey = fOrderLineElement.getAttribute("ChainedFromOrderHeaderKey");
			}
			if(!YFCCommon.isVoid(cOrderHeaderKey)){
				//Update the Customer Order status by calling changeOrder
				Document cOrderOutputDoc = null;
				Document cOrderInputDoc = YFCDocument.createDocument("Order").getDocument();
				cOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, cOrderHeaderKey);		
				// To set API template
				env.setApiTemplate("getOrderList", getOrderListTemplate);
				
				cOrderOutputDoc = api.invoke(env,"getOrderList", cOrderInputDoc);
				log.debug("Customer order details = " + SCXmlUtil.getString(cOrderOutputDoc));
				
				// To clear API template for getOrderList
				env.clearApiTemplate("getOrderList");
				Element outputOrderElem = (Element)(cOrderOutputDoc.getElementsByTagName("Order")).item(0);
				String maxOrderStatus = outputOrderElem.getAttribute("MaxOrderStatus");
				String minOrderStatus = outputOrderElem.getAttribute("MinOrderStatus");
				
				if(maxOrderStatus!=null && minOrderStatus!=null && maxOrderStatus.equals(minOrderStatus))
					orderStatus = maxOrderStatus;
				else if(!maxOrderStatus.equals(minOrderStatus)){
					orderStatus = maxOrderStatus;
					orderStatusPrefix = "Partially";
				}
				if(YFCCommon.isVoid(orderStatus))
					orderStatus = XPXLiterals.ORDER_PLACED_STATUS;
				
				Document changeCustOrderDoc = YFCDocument.createDocument("Order").getDocument();
				Element changeCustOrderElement = changeCustOrderDoc.getDocumentElement();
				changeCustOrderElement.setAttribute("OrderHeaderKey", cOrderHeaderKey);
				changeCustOrderElement.setAttribute("Override", "Y");
				Element extnElementDoc = SCXmlUtil.createChild(changeCustOrderElement, "Extn");
				extnElementDoc.setAttribute("ExtnOrderStatus", orderStatus);
				extnElementDoc.setAttribute("ExtnOrderStatusPrefix", orderStatusPrefix);
					
				log.debug("Calling changeOrder with Input for updating Customer Order when FO is cancelled\n");
				log.debug("---------------------------------------------\n");
				log.debug(SCXmlUtil.getString(changeCustOrderDoc)+"\n");
				log.debug("---------------------------------------------\n");
				Element pendignElement=changeCustOrderDoc.createElement("PendingChanges");
				pendignElement.setAttribute("IgnorePendingChanges", "Y");
				changeCustOrderDoc.appendChild(pendignElement);
				outDoc = api.executeFlow(env,"XPXUpdateExOrderStatus", changeCustOrderDoc);
			}
		}
		return inXML;
	}
	
}

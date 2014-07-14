/**
 * 
 */
package com.xpedx.nextgen.dashboard;

import java.util.Properties;

import java.lang.Integer;
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
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * @author Administrator
 *
 */
public class XPXFetchUpdatedOrderAmount implements YIFCustomApi{
	private static YIFApi api = null;
	private static YFCLogCategory log;
	private String getChangeOrderTemplate = "global/template/api/changeOrder.XPXFetchUpdatedOrderAmount.xml";
	private String getOrderListTemplate = "global/template/api/getOrderList.XPXFetchUpdatedOrderAmount.xml";
	
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
	
	public Document fetchUpdatedTotalOrderValue(YFSEnvironment env, Document inXML) throws Exception {
		Document changeOrderOutDoc = null;
		String currentFOhk = null;
		String webConfNum = null;
		double totalOrderVal = 0;
		Document outputDoc = SCXmlUtil.createDocument("Order");
		try{
			if(inXML!=null && inXML.getDocumentElement()!=null && (inXML.getDocumentElement().getNodeName()).equalsIgnoreCase("Order"))
			{
				currentFOhk = inXML.getDocumentElement().getAttribute("OrderHeaderKey");
				outputDoc.getDocumentElement().setAttribute("OrderHeaderKey",currentFOhk);
				Element currentFOrderExtnElem = SCXmlUtil.getChildElement(inXML.getDocumentElement(), "Extn");
				if(currentFOrderExtnElem!=null)
					webConfNum =  currentFOrderExtnElem.getAttribute("ExtnWebConfNum");
				
				if(webConfNum!=null)
				{
					Document getOrderListDoc = YFCDocument.createDocument("Order").getDocument();
					Element orderElement = getOrderListDoc.getDocumentElement();
					
					orderElement.setAttribute("OrderType", "Customer");
					orderElement.setAttribute("OrderTypeQryType", "NE");
					
					Element extnOrderElem =  SCXmlUtil.createChild(orderElement, "Extn");
					extnOrderElem.setAttribute("ExtnWebConfNum", webConfNum);
					
					env.setApiTemplate("getOrderList", getOrderListTemplate);
					log.debug("Calling getOrderList with Input\n");
					log.debug("---------------------------------------------\n");
					log.debug(SCXmlUtil.getString(getOrderListDoc)+"\n");
					log.debug("---------------------------------------------\n");
					Document outDoc = api.invoke(env, "getOrderList", getOrderListDoc);
					env.clearApiTemplate("getOrderList");
					int noOfFOs = 0;
					if(outDoc!=null)
					{
						NodeList orderElems = outDoc.getDocumentElement().getElementsByTagName("Order");
						if(orderElems!=null)
							noOfFOs = orderElems.getLength();
						outputDoc.getDocumentElement().setAttribute("FOSize", new Integer(noOfFOs).toString());
						if(noOfFOs== 1)
						{
							Element currentFOrderElem =  (Element)orderElems.item(0);
							if(currentFOrderElem.getAttribute("OrderHeaderKey").equals(currentFOhk))
							{
								Document changeOrderInputDoc = inXML;
							
								//Call changeOrder with RecordPendingChanges="Y" to get the updated total value
								if(SCXmlUtil.getChildElement(changeOrderInputDoc.getDocumentElement(), "PendingChanges")!=null)
									changeOrderInputDoc.removeChild(SCXmlUtil.getChildElement(changeOrderInputDoc.getDocumentElement(), "PendingChanges"));
								Element pendingChanges = SCXmlUtil.createChild(changeOrderInputDoc.getDocumentElement(), "PendingChanges");
								pendingChanges.setAttribute("RecordPendingChanges", "Y");
							
								//Call changeOrder for the current fulfillment order to update its totalOrderValue
								env.setApiTemplate("changeOrder", getChangeOrderTemplate);
								changeOrderOutDoc = api.invoke(env, "changeOrder", changeOrderInputDoc);
								env.clearApiTemplate("changeOrder");
								
								//Fetch the fulfillment orders total order value
								if(changeOrderOutDoc!=null){
									 Element changeOdrExtnElem = SCXmlUtil.getChildElement(changeOrderOutDoc.getDocumentElement(), "Extn");
									 String orderVal = changeOdrExtnElem.getAttribute("ExtnTotalOrderValue");
									 if(!YFCCommon.isVoid(orderVal))
										 totalOrderVal = new Double(orderVal).doubleValue();
								
									//Iterate through the only fulfillment order of the customer order
									double penaltyChargeVal = 0;
								
									 //Iterate through all order lines and fetch all penalty charge lines
									 //Element orderlinesElem = SCXmlUtil.getChildElement(currentFOrderElem, "OrderLines");
									 NodeList orderLines =  currentFOrderElem.getElementsByTagName("OrderLine");
									 if(orderLines!=null && orderLines.getLength()>0)
									 {
										 for(int j=0;j<orderLines.getLength();j++)
										 {
											 Element orderlineElem = (Element)orderLines.item(j);
											 String lineType = orderlineElem.getAttribute("LineType");
											 Element itemElem = SCXmlUtil.getChildElement(orderlineElem, "Item");
											 String itemId = itemElem.getAttribute("ItemID");
											 String penaltyItemId = YFSSystem.getProperty("ItemID");
											 if(lineType.equals("M") && ((penaltyItemId!=null && itemId.equals(penaltyItemId)) || itemId.equals("/05")))
											 {
												 Element extnElem = SCXmlUtil.getChildElement(orderlineElem, "Extn");
												 if(extnElem!=null){
													 String lineTotal = extnElem.getAttribute("ExtnLineOrderedTotal");
													 if(!YFCCommon.isVoid(lineTotal))
														 penaltyChargeVal+= new Double(lineTotal).doubleValue();
												 }
											 }
											 
										 }
									 }
									 totalOrderVal = totalOrderVal - penaltyChargeVal;
									 outputDoc.getDocumentElement().setAttribute("UpdatedTotalOrderValue", new Double(totalOrderVal).toString());
								} 
							}
								 
							
						}
					}
				}
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return outputDoc;
		}
		return outputDoc;
	}

}

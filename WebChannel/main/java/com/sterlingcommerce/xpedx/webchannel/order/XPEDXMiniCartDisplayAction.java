package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPathConstants;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;
import com.sterlingcommerce.webchannel.utilities.OrderHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.order.MiniCartDisplayAction;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;
/**
 * XPEDXMiniCartDisplayAction is the action for displaying the major line items of the order in the mini cart window
 * 
 * @author vkaradik
 * 
 */

public class XPEDXMiniCartDisplayAction extends MiniCartDisplayAction {
	
	private static final long serialVersionUID = -367265029311230324L;
	private static final Logger log = Logger.getLogger(MiniCartDisplayAction.class);
    private static final String ORDERDETAILS_MASHUP_ID = "XPEDXOrderDetailsForMiniCart";
    private boolean readOrderLinesFromStart = false;
    
	public String execute() 
    {
        try {
            log.info("inside MiniCartDisplayAction");
            XPEDXWCUtils.checkMultiStepCheckout();
            getOrderDetails();
        } catch (Exception e){ 
            e.printStackTrace();
        }
        return SUCCESS;
    
    }
	
	public  List<String> getOrderTotal()
	{
		String OrderTotal=null;		
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());		
		String orderHeaderKey = (String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");//XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(wcContext);
		String editedOrderHeaderKey=XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
		List<String> itemAndTotalList=new ArrayList<String>();
		if(!YFCCommon.isVoid(editedOrderHeaderKey))
		{
			orderHeaderKey=editedOrderHeaderKey;
		}
		if (orderHeaderKey==null || "".equals(orderHeaderKey)){
            return itemAndTotalList;
        }
        log.info("orderHeaderKey-->"+orderHeaderKey);
        try{
        	 Map<String, String> valueMap = new HashMap<String, String>();
        	 valueMap.put("/Order/@OrderHeaderKey", orderHeaderKey);
        	 Element input = WCMashupHelper.getMashupInput("XPEDXOrderDetailsForMiniCartLink",valueMap, wcContext.getSCUIContext());
        	 String inputXml = SCXmlUtil.getString(input);
        	 Object obj = WCMashupHelper.invokeMashup("XPEDXOrderDetailsForMiniCartLink", input,wcContext.getSCUIContext());
        	 Document outputDoc = ((Element) obj).getOwnerDocument();
        	 OrderTotal= SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/Order/Extn/@ExtnTotalOrderValue");    
        	 Element orderLinesEleme=(Element)outputDoc.getDocumentElement().getElementsByTagName("OrderLines").item(0);
        	// ArrayList<Element> orderLineList= SCXmlUtil.getElements(outputDoc.getDocumentElement(),"OrderLines/OrderLine");
        	 itemAndTotalList.add(OrderTotal);
        	 itemAndTotalList.add(orderLinesEleme.getAttribute("TotalNumberOfRecords"));
        	 /*if(!YFCCommon.isVoid(orderLineList))
        	 {
        		 itemAndTotalList.add(""+orderLineList.size());
        	 }
        	 else
        	 {
        		 itemAndTotalList.add("0");
        	 }*/
            log.info("getOrderDetails end");
        } catch (Exception e) {
            e.printStackTrace();
        }		
		return itemAndTotalList;
	}
	
    
    private void getOrderDetails(){
        log.info("inside getOrderDetails");
        
        orderHeaderKey = (String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");//XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext());
        String editedOrderHeaderKey=XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
		/*if(!YFCCommon.isVoid(editedOrderHeaderKey))
		{
			orderHeaderKey=editedOrderHeaderKey;
		}*/
        if (orderHeaderKey==null || "".equals(orderHeaderKey)){
            return;
        }
        log.info("orderHeaderKey-->"+orderHeaderKey);
        try{
        	 majorLineElements=(ArrayList<Element>)XPEDXWCUtils.getObjectFromCache("OrderLinesInContext");
            /*outputDocument= prepareAndInvokeMashup(ORDERDETAILS_MASHUP_ID);
            
            NodeList orderLines = (NodeList) XMLUtilities.evaluate("//Order/OrderLines/OrderLine", outputDocument, XPathConstants.NODESET);
            int length = orderLines.getLength();
            
            if(readOrderLinesFromStart){
            	for (int i = 0; i < length; i++) {
                    Element currNode = (Element) orderLines.item(i);
                    String lineKey = currNode.getAttribute("OrderLineKey");
                    log.info("linekey-->"+lineKey);
                    NodeList bundleParentLines = currNode.getElementsByTagName("BundleParentLine");
                    if ( (bundleParentLines.getLength() == 0) &&
                         (!OrderHelper.isCancelledLine(currNode)) )
                    {
                        log.info(lineKey+ " it's major line");
                        if(majorLineElements.size() == maxElements)
                        {
                            // Already have accumulated the maximum number of line items to display.
                            // Set the "More..." indicator and break out of the loop.
                            hasMore = true;
                            break;
                        }
                        else
                        {
                            // Haven't hit the max number yet, so go ahead and add it to the list.
                            majorLineElements.add(currNode);
                        }
                    }
                }
            }
            else{
            	for (int i = length-1; i > -1; i--) {
                    Element currNode = (Element) orderLines.item(i);
                    String lineKey = currNode.getAttribute("OrderLineKey");
                    log.info("linekey-->"+lineKey);
                    NodeList bundleParentLines = currNode.getElementsByTagName("BundleParentLine");
                    if ( (bundleParentLines.getLength() == 0) &&
                         (!OrderHelper.isCancelledLine(currNode)) )
                    {
                        log.info(lineKey+ " it's major line");
                        if(majorLineElements.size() == maxElements)
                        {
                            // Already have accumulated the maximum number of line items to display.
                            // Set the "More..." indicator and break out of the loop.
                            hasMore = true;
                            break;
                        }
                        else
                        {
                            // Haven't hit the max number yet, so go ahead and add it to the list.
                            majorLineElements.add(currNode);
                        }
                    }
                }
            }*/
            
            log.info("getOrderDetails end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public boolean isReadOrderLinesFromStart() {
		return readOrderLinesFromStart;
	}

	public void setReadOrderLinesFromStart(boolean readOrderLinesFromStart) {
		this.readOrderLinesFromStart = readOrderLinesFromStart;
	}


}

package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;
import com.sterlingcommerce.webchannel.utilities.OrderHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.order.MiniCartDisplayAction;
import com.sterlingcommerce.xpedx.webchannel.catalog.XPEDXItemBranchInfoBean;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
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
	XPEDXShipToCustomer shipToCustomer;
	
	private static final long serialVersionUID = -367265029311230324L;
	private static final Logger log = Logger.getLogger(MiniCartDisplayAction.class);
    private static final String ORDERDETAILS_MASHUP_ID = "XPEDXOrderDetailsForMiniCart";
    private boolean readOrderLinesFromStart = false;
    private HashMap<String, String> itemMap = new HashMap<String, String>();
	public String customerStatus;
    
	public String getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}

	public HashMap<String, String> getItemMap() {
		return itemMap;
	}

	public void setItemMap(HashMap<String, String> itemMap) {
		this.itemMap = itemMap;
	}

	public String execute() 
    {
        try {
        	shipToCustomer = (XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
        	customerStatus = shipToCustomer.getCustomerStatus();
            LOG.debug("inside MiniCartDisplayAction");
            XPEDXWCUtils.checkMultiStepCheckout();
            ArrayList<String> itemIdsInMinicart = getOrderDetails();
            //Start for Jira 3481
            getOrderMultipleFromSession(itemIdsInMinicart);
        } catch (Exception e){ 
            e.printStackTrace();
        }
        return SUCCESS;
    
    }
	//Added this method for Jira 3481
	public void getOrderMultipleFromSession( ArrayList<String> itemIds)
	{ 	Object itemMapObj = XPEDXWCUtils.getObjectFromCache("itemMap");
		//Check if Session exist
		if(itemMapObj == null){
			//If not, we will call getXPXItemExtnList API
			Document xPXItemExtnListDoc = null;
			try {
				xPXItemExtnListDoc = XPEDXWCUtils.getXPXItemExtnList(itemIds, wcContext);
				List<Element> itemBranchElementList = null;
				if(xPXItemExtnListDoc!=null) {
					Object[] elements = itemIds.toArray();
					for(int i=0;i<elements.length;i++){
					itemBranchElementList = XMLUtilities.getElements(xPXItemExtnListDoc.getDocumentElement(),"XPXItemExtn[@ItemID='"+elements[i]+"']");
					if( itemBranchElementList!=null && itemBranchElementList.size()>0){
						Element itemBranchElement = itemBranchElementList.get(0);
		
						// get the field information
						String itemNumber = SCXmlUtil.getAttribute(itemBranchElement,"ItemID");
						String orderMultiple = SCXmlUtil.getAttribute(itemBranchElement, "OrderMultiple");
						//Created a itemMap to store itemNumber, orderMultiple
						itemMap.put(itemNumber,orderMultiple );
						}
					}
					//set in Session
					XPEDXWCUtils.setObectInCache("itemMap",itemMap);
				}
				//
			}catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//set a itemsUOMMap in Session for ConvFactor
		XPEDXWCUtils.setObectInCache("itemsUOMMap",XPEDXOrderUtils.getXpedxUOMList(wcContext.getCustomerId(), itemIds, wcContext.getStorefrontId()));
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
        LOG.debug("orderHeaderKey-->"+orderHeaderKey);
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
            LOG.debug("getOrderDetails end");
        } catch (Exception e) {
            e.printStackTrace();
        }		
		return itemAndTotalList;
	}
	
    
    private ArrayList<String> getOrderDetails(){
        LOG.debug("inside getOrderDetails");
        
        orderHeaderKey = (String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");//XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext());
        String editedOrderHeaderKey=XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
        ArrayList<String> itemIds = new ArrayList<String>();
			
		/*if(!YFCCommon.isVoid(editedOrderHeaderKey))
		{
			orderHeaderKey=editedOrderHeaderKey;
		}*/
        if (orderHeaderKey==null || "".equals(orderHeaderKey)){
            return itemIds;
        }
        LOG.debug("orderHeaderKey-->"+orderHeaderKey);
        try{
        	 majorLineElements=(ArrayList<Element>)XPEDXWCUtils.getObjectFromCache("OrderLinesInContext");
        	 for(int i=0;i<majorLineElements.size();i++)
     		{
     			Element orderLine=majorLineElements.get(i);
     			NodeList itemNodeList = orderLine.getElementsByTagName("Item");
     			for (int k = 0; k < itemNodeList.getLength(); k++) {
     				itemIds.add(SCXmlUtil.getAttribute((Element) itemNodeList.item(k), "ItemID"));
     			}
     			
     		}
        	 /*outputDocument= prepareAndInvokeMashup(ORDERDETAILS_MASHUP_ID);
            
            NodeList orderLines = (NodeList) XMLUtilities.evaluate("//Order/OrderLines/OrderLine", outputDocument, XPathConstants.NODESET);
            int length = orderLines.getLength();
            
            if(readOrderLinesFromStart){
            	for (int i = 0; i < length; i++) {
                    Element currNode = (Element) orderLines.item(i);
                    String lineKey = currNode.getAttribute("OrderLineKey");
                    LOG.debug("linekey-->"+lineKey);
                    NodeList bundleParentLines = currNode.getElementsByTagName("BundleParentLine");
                    if ( (bundleParentLines.getLength() == 0) &&
                         (!OrderHelper.isCancelledLine(currNode)) )
                    {
                        LOG.debug(lineKey+ " it's major line");
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
                    LOG.debug("linekey-->"+lineKey);
                    NodeList bundleParentLines = currNode.getElementsByTagName("BundleParentLine");
                    if ( (bundleParentLines.getLength() == 0) &&
                         (!OrderHelper.isCancelledLine(currNode)) )
                    {
                        LOG.debug(lineKey+ " it's major line");
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
            
            LOG.debug("getOrderDetails end");
            } catch (Exception e) {
            e.printStackTrace();
        }
		return itemIds;
    }

	public boolean isReadOrderLinesFromStart() {
		return readOrderLinesFromStart;
	}

	public void setReadOrderLinesFromStart(boolean readOrderLinesFromStart) {
		this.readOrderLinesFromStart = readOrderLinesFromStart;
	}


}

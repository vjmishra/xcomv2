/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.order.OrderAgainAction;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCDocument;

/**
 * @author rugrani
 *
 */
public class XPEDXOrderAgainAction extends OrderAgainAction {
	/*Added  for Webtrends Start:   */
	public static final String CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ = "changeOrderAPIOutputForOrderLinesModification";
	private boolean reorderMetaTag = false;
	
	public boolean isReorderMetaTag() {
		return reorderMetaTag;
	}
	public void setReorderMetaTag(boolean reorderMetaTag) {
		this.reorderMetaTag = reorderMetaTag;
	}
	/*Added  for Webtrends End  */
	
	public XPEDXOrderAgainAction()
    {
        formattedOrderName = "";
        reOrderRemoveItems = "";
        reOrderRemoveItemList = new ArrayList<String>();
    }
	/* (non-Javadoc)
	 * @see com.sterlingcommerce.webchannel.order.OrderAgainAction#execute()
	 */
	@Override
	public String execute() {
		String result = "success";
        try
        {
        	evalFormattedOrderName();
        	Map<String, String> valueMap = new HashMap<String, String>();
        	valueMap.put("/Order/@OrderHeaderKey", getOrderHeaderKey());
			
			Element input;
			Element orderElem = null;
			
			input = WCMashupHelper.getMashupInput("XPEDXReOrderGetOrderList",
					valueMap, wcContext.getSCUIContext());
			Object obj1 = WCMashupHelper.invokeMashup("XPEDXReOrderGetOrderList",
						input, wcContext.getSCUIContext());
			Document outputDoc = ((Element) obj1).getOwnerDocument();
			Element orderElelement=XPEDXOrderUtils.createNewDraftOrderOnBehalfOf(wcContext, getFormattedOrderName(), "");
			String orderHeaderKey=orderElelement.getAttribute("OrderHeaderKey");
			setOrderHeaderKey(orderHeaderKey);
			valueMap = new HashMap<String, String>();
        	valueMap.put("/Order/@OrderHeaderKey", getOrderHeaderKey());
			input = WCMashupHelper.getMashupInput("XPEDXReOrderChangeOrder",
					valueMap, wcContext.getSCUIContext());
			
			
			ArrayList<Element> orderLineList=SCXmlUtil.getElements(outputDoc.getDocumentElement(), "/Order/OrderLines/OrderLine");
			Element orderLines=SCXmlUtil.createChild(input, "OrderLines");
			for(Element orderLine:orderLineList)
			{
				if(!"M".equals(orderLine.getAttribute("LineType"))){
					orderLine.setAttribute("OrderLineKey", "");
					orderLine.setAttribute("Action", "CREATE");
					orderLines.appendChild(input.getOwnerDocument().importNode(orderLine, true));
				}
			}
			XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext());
			obj1 = WCMashupHelper.invokeMashup("XPEDXReOrderChangeOrder",
					input, wcContext.getSCUIContext());
			
			Element elementCopiedOrder= ((Element) obj1);
			Document elementCopiedOrderDoc=elementCopiedOrder.getOwnerDocument();
			getWCContext().getSCUIContext().getSession().setAttribute(CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ,elementCopiedOrderDoc);
           /* if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext))
            	customerId = XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext);
            else
            	customerId = wcContext.getCustomerId();
            org.w3c.dom.Element elementCopiedOrder = prepareAndInvokeMashup("xpedxOrderAgain");*/
            if(null != elementCopiedOrder)
            {
                log.debug((new StringBuilder()).append("Copy order Output XML \n").append(SCXmlUtils.getString(elementCopiedOrder)).toString());
                setOrderHeaderKey(SCXmlUtils.getAttribute(elementCopiedOrder, "OrderHeaderKey"));
                CommerceContextHelper.flushCartInContextCache(getWCContext());
				XPEDXWCUtils.removeObectFromCache("OrderHeaderInContext");
				//Remove itemMap from Session, when cart change in context,  For Minicart Jira 3481
				XPEDXWCUtils.removeObectFromCache("itemMap");
				XPEDXOrderUtils.refreshMiniCart(getWCContext(),elementCopiedOrder,true,false,XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
                //removeOrderLinesForReOrder(elementCopiedOrder);
            }
         // Webtrends meta tag starts here
    		HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
            HttpSession localSession = httpRequest.getSession();
            setReorderMetaTag(true);
    		localSession.setAttribute("reorderMetaTag", reorderMetaTag);		
    		// Webtrends meta tag end here
        }
        catch(CannotBuildInputException cbiEx)
        {
            log.error("Exception in creating input xml.", cbiEx);
            result = "error";
        }
        catch(Exception ex)
        {
            log.error("Exception in performing order again", ex);
            result = "error";
        }
        return result;
	}
	
	private void evalFormattedOrderName()
	
    {
        formattedOrderName = getOrderName();
        String copyText = getText("CopyOfPrefix");
        if(!formattedOrderName.startsWith(copyText))
        {
            String newCartName[] = {
                copyText, getOrderNo()
            };
            setFormattedOrderName(getText("CopyOfCartName", newCartName));
        }
    }
	
	/**
	 * @return the reOrderRemoveItems
	 */
	public String getReOrderRemoveItems() {
		return reOrderRemoveItems;
	}
	/**
	 * @param reOrderRemoveItems the reOrderRemoveItems to set
	 */
	public void setReOrderRemoveItems(String reOrderRemoveItems) {
		this.reOrderRemoveItems = reOrderRemoveItems;
		StringTokenizer token = new StringTokenizer(reOrderRemoveItems, ":");
		while(token.hasMoreElements()){
			reOrderRemoveItemList.add(token.nextToken().toString());
		}
	}
	
	
	public void removeOrderLinesForReOrder(Element elementCopiedOrder) throws CannotBuildInputException{
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Order/@OrderHeaderKey", elementCopiedOrder.getAttribute("OrderHeaderKey"));
		Element input = WCMashupHelper.getMashupInput("deleteLineForReOrder",valueMap,
				getWCContext().getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("OrderLines");
		Element inputNodeListElemt = (Element)inputNodeList.item(0);
		boolean hasItemstoRemove = false;
		Element orderLinesElement = (Element) elementCopiedOrder.getElementsByTagName("OrderLines").item(0);
		
		if(orderLinesElement!=null){
			NodeList orderLineElemList = orderLinesElement.getElementsByTagName("OrderLine");
			if(orderLineElemList!=null && orderLineElemList.getLength()>0)
    		{
    			for(int k =0;k<orderLineElemList.getLength();k++)
    			{
    				Element orderLineElement = (Element)orderLineElemList.item(k);
    				if("M".equals(orderLineElement.getAttribute("LineType"))){
    					Document expDoc = YFCDocument.createDocument("OrderLine").getDocument();
    					Element orderLineEle = expDoc.getDocumentElement();
    					orderLineEle.setAttribute("Action", "REMOVE");
    					orderLineEle.setAttribute("OrderLineKey", orderLineElement.getAttribute("OrderLineKey"));
    					inputNodeListElemt.appendChild(inputDoc.importNode(orderLineEle, true));
    					hasItemstoRemove = true;
    				}
    			}
			}
		}
		if(hasItemstoRemove){
			inputXml = SCXmlUtil.getString(input);
			log.debug("Input XML: " + inputXml);
			Object obj = WCMashupHelper.invokeMashup("deleteLineForReOrder", input,
					getWCContext().getSCUIContext());
			Document outputDoc = ((Element) obj).getOwnerDocument();
			if (null != outputDoc) {
				log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
			}
		}else{
			log.debug("All the item in the order has been selected for re order...");
		}
	}
	
	public String formattedOrderName;
	public final Logger log = Logger.getLogger(XPEDXOrderAgainAction.class);
	public String reOrderRemoveItems = null;
	public ArrayList<String> reOrderRemoveItemList = null;
	protected String customerId;

	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}

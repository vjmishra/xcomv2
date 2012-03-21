package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.compat.SCXmlUtils;
import com.sterlingcommerce.webchannel.order.DraftOrderModifyLineItemsAction;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCUtils;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.core.YFSSystem;

public class XPEDXDraftOrderModifyLineItemsAction extends 
DraftOrderModifyLineItemsAction
{
	public static final String CHANGE_ORDER_LINE_DETAILS_MASHUP = "xpedx_me_changeOrderLineDetails";
	//public static final String CHANGE_ORDEROUTPUT_CHECKOUT_SESSION_OBJ = "changeOrderAPIOutputForCheckout";
	//public static final String CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ = "changeOrderAPIOutputForOrderLinesModification";
	
	public String mashUpId = null; 
	
	/*Added  for Webtrends :  to check if "Save/Update Cart is hit */
	private boolean updateCartMetaTag = false;
	
	protected String validateCustomerFields = "N";
	private boolean isAnyMLine=false;
	private String orderLineKey="";
	private String modifiedOrderHeaderKey="";
	private double totalAmount;
	private Element modifiedOrderExtnForSpecailCharge;
	/*private String modifyOrderLines="false";
	public String getModifyOrderLines() {
		return modifyOrderLines;
	}

	public void setModifyOrderLines(String modifyOrderLines) {
		this.modifyOrderLines = modifyOrderLines;
	}*/

	public boolean isUpdateCartMetaTag() {
		return updateCartMetaTag;
	}

	public void setUpdateCartMetaTag(boolean updateCartMetaTag) {
		this.updateCartMetaTag = updateCartMetaTag;
	}
	
	

	/**
	 * @return the validateCustomerFields
	 */
	public String getValidateCustomerFields() {
		return validateCustomerFields;
	}

	/**
	 * @param validateCustomerFields the validateCustomerFields to set
	 */
	public void setValidateCustomerFields(String validateCustomerFields) {
		this.validateCustomerFields = validateCustomerFields;
	}

	public XPEDXDraftOrderModifyLineItemsAction()
	{
		super();
		orderLineNote = null;
	}
	 
	public String execute()
    {
		long startTime=System.currentTimeMillis();
		
		if(zeroOrderLines.equals("true")){
			mashUpId = "xpedx_me_changeOrderDetails";
		}
		else /*if (!"true".equals(isComingFromCheckout)) */{	//Removing changeOrder call for Performance improvement, while checkout
			mashUpId = CHANGE_ORDER_LINE_DETAILS_MASHUP;
		}
		
		/*  String definitionFromSession = (String)wcContext.getWCAttribute("SWC_CHECKOUT_TYPE", WCAttributeScope.LOCAL_SESSION);
		  if("Single_Step".equals(definitionFromSession))
	      {
			  isMultiStepCheckout=""+Boolean.FALSE;
	      }*/
		XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext());
		// Webtrends meta tag starts here
		HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
        setUpdateCartMetaTag(true);
        String retVal="";
		localSession.setAttribute("isUpdateCartMetaTag", "true");		
		// Webtrends meta tag end here
		Document outputDocument=null;
		try
	    {
			prepareAndInvokeMashups();
			//Map<String, Element> out = prepareAndInvokeMashups();
			/*Begin - Changes made by Mitesh Parikh for JIRA#3595*/
			//outputDocument = (Document)out.get(mashUpId).getOwnerDocument();
			/*End - Changes made by Mitesh Parikh for JIRA#3595*/
			
            retVal= SUCCESS;
	     }
	     catch(Exception e)
	     {
	            LOG.error(e.getMessage(), e);
	            WCUtils.setErrorInContext(getWCContext(), e);
	            
	            retVal= ERROR;
	     }
		if(!zeroOrderLines.equals("true"))
		{
			//Always validate customer fields after calling Update Cart
			setValidateCustomerFields("Y");
		}		
		
		XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext(),new HashMap());
		/*boolean chngOrderOutputAvailable=false;
		if("true".equals(modifyOrderLines) || "true".equals(isComingFromCheckout))
			chngOrderOutputAvailable=true;*/
		
		if(isEditOrder.contains("true"))
			processSpecialCharge(); //(outputDocument, chngOrderOutputAvailable);
		
		/*Begin - Changes made by Mitesh Parikh for JIRA#3595
		if(outputDocument!=null && retVal.equals(SUCCESS))
		{
			if("true".equals(isComingFromCheckout)) {
				getWCContext().getSCUIContext().getSession().setAttribute(CHANGE_ORDEROUTPUT_CHECKOUT_SESSION_OBJ, outputDocument);
			
			}else if ("true".equals(modifyOrderLines)) {
				getWCContext().getSCUIContext().getSession().setAttribute(CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ, outputDocument);
			}
		}
		else
			retVal= ERROR;
		End - Changes made by Mitesh Parikh for JIRA#3595*/
		XPEDXWCUtils.releaseEnv(wcContext);
		
		long endTime=System.currentTimeMillis();
		System.out.println("Time taken in milliseconds in XPEDXDraftOrderModifyLineItemsAction class : "+(endTime-startTime));
		return retVal;
    }
	
	
	private void processSpecialCharge() //Document outputDoc,  boolean chngOrderOutputAvailable)
	{
		try
		{
			LOG.info("Calling processSpecialCharge");
			UtilBean util=new UtilBean();
			
			Map<String, String> valueMap1 = new HashMap<String, String>();
			Element orderElem = null;
			Element input1=null;
			Object obj1=null;
			/*Begin - Changes made by Mitesh Parikh for JIRA#3595
			if(chngOrderOutputAvailable)
			{*/
				valueMap1.put("/Order/@OrderHeaderKey", orderHeaderKey);				
				input1 = WCMashupHelper.getMashupInput("xpedx_get_completeorderList",
							valueMap1, wcContext.getSCUIContext());
				obj1 = WCMashupHelper.invokeMashup("xpedx_get_completeorderList",
								input1, wcContext.getSCUIContext());		
				
				orderElem = ((Element) obj1).getOwnerDocument().getDocumentElement();
			
			/*} else {
				orderElem = outputDoc.getDocumentElement();
			
			}
			End - Changes made by Mitesh Parikh for JIRA#3595*/
			Element orderExtn=util.getElement(orderElem, "Extn");
			String extnWebConfNum=orderExtn.getAttribute("ExtnWebConfNum");
			validateSpecialLine(orderElem, util) ;
			if(!YFCCommon.isVoid(extnWebConfNum))
			{
				valueMap1 = new HashMap<String, String>();
				valueMap1.put("/Order/Extn/@ExtnWebConfNum", extnWebConfNum);
				
				
				input1 = WCMashupHelper.getMashupInput("xpedx_get_orderList_for_special_charge",
							valueMap1, wcContext.getSCUIContext());
				
				
				obj1 = WCMashupHelper.invokeMashup("xpedx_get_orderList_for_special_charge",
								input1, wcContext.getSCUIContext());
		
				Element orderListElem = ((Element) obj1).getOwnerDocument().getDocumentElement();
				if(orderListElem != null)
				{
					NodeList orderListNode=orderListElem.getElementsByTagName("Order");
					
					for(int i=0;i<orderListNode.getLength();i++)
					{
						Element orderListOrderElem=(Element)orderListNode.item(i);
						if(modifiedOrderHeaderKey.equals(orderHeaderKey))
						{
							continue;
						}
						validateSpecialLine(orderListOrderElem, util) ;
					}
				}
			}
			if(YFCCommon.isVoid(minOrderAmount))
			{
				minOrderAmount="0";
			}
			Document inputDocument=null;
			/*if(!isAnyMLine && Double.parseDouble(minOrderAmount) > totalAmount )
			{
				inputDocument=createOrderDocumentForSpecialLine(orderExtn,"CREATE",orderHeaderKey,null);
			}
			else*/
				if(isAnyMLine && Double.parseDouble(minOrderAmount) <=totalAmount && Double.parseDouble(minOrderAmount)!=0)
			{
				inputDocument=createOrderDocumentForSpecialLine(modifiedOrderExtnForSpecailCharge,"MODIFY",modifiedOrderHeaderKey,orderLineKey);
			}
			if(inputDocument != null)
			{
				LOG.info("Input XML = "+SCXmlUtil.getString(inputDocument) );
				WCMashupHelper.invokeMashup("xpedx_SpecialLinechangeOrder", inputDocument.getDocumentElement(), wcContext.getSCUIContext());
			}
		}
		catch(Exception e)
		{
			LOG.error("Error while calling change order to Update the price from PNA");
		}
		XPEDXWCUtils.releaseEnv(wcContext);
	}
	
	private void validateSpecialLine(Element orderListOrderElem,UtilBean util) throws Exception
	{
		modifiedOrderHeaderKey=orderListOrderElem.getAttribute("OrderHeaderKey");
		String itemID=YFSSystem.getProperty("ItemID");
		if(orderListOrderElem != null)
		{
			Element orderListOrderExtn=util.getElement(orderListOrderElem, "Extn");
			modifiedOrderExtnForSpecailCharge=orderListOrderExtn;
			totalAmount +=SCXmlUtil.getDoubleAttribute(orderListOrderExtn, "ExtnTotalOrderValue");
		}
		List<Element> orderLines=SCXmlUtil.getElements(orderListOrderElem, "/OrderLines/OrderLine");
		if(orderLines.size()>0)
		{
			for(Element orderLineEle : orderLines)
			{
				String lineType=orderLineEle.getAttribute("LineType");
				String orderedQty=orderLineEle.getAttribute("OrderedQty");
				Element itemElem=(Element)orderLineEle.getElementsByTagName("Item").item(0);
				String OrderItemID="";
				if(itemElem != null)
				{
					OrderItemID=itemElem.getAttribute("ItemID");
				}
				
				if("M".equals(lineType) && OrderItemID.equals(itemID) && Double.parseDouble(orderedQty)==1)
				{
					isAnyMLine=true;
					orderLineKey=orderLineEle.getAttribute("OrderLineKey");
					
					break;
				}
			}
		}
	}
	
	private Document createOrderDocumentForSpecialLine(Element orderExtn,String action,String orderHeaderKey,String orderLineKey)
	{
		Document inputDocument =SCXmlUtil.createDocument("Order");
		Element orderChangeElem=inputDocument.getDocumentElement();
		
		orderChangeElem.setAttribute("OrderHeaderKey", orderHeaderKey);
		Element orderLinesChanegElem=SCXmlUtil.createChild(orderChangeElem, "OrderLines");
		Element orderLineChanegElem=SCXmlUtil.createChild(orderLinesChanegElem, "OrderLine");
		Element orderLineExtnChange=SCXmlUtil.createChild(orderLineChanegElem, "Extn");
		String orderedQty="CREATE".equals(action) ? "1" : "0";
		orderLineChanegElem.setAttribute("Action", action);
		orderLineChanegElem.setAttribute("OrderedQty", orderedQty);
		Element orderLineTranChange=SCXmlUtil.createChild(orderLineChanegElem, "OrderLineTranQuantity");
		orderLineTranChange.setAttribute("OrderedQty",orderedQty);
		if("CREATE".equals(action))
		{
			orderLineChanegElem.setAttribute("ValidateItem", "N");
			orderLineChanegElem.setAttribute("LineType", "M");
			String itemID=YFSSystem.getProperty("ItemID");
			Element itemElement = SCXmlUtil.createChild(orderLineChanegElem, "Item");
			itemElement.setAttribute("ItemID", itemID);
			//itemElement.setAttribute("UnitOfMeasure", "EACH");
			orderLineExtnChange.setAttribute("ExtnLineOrderedTotal", chargeAmount);
			orderLineExtnChange.setAttribute("ExtnReqUOMUnitPrice",chargeAmount);
			orderLineExtnChange.setAttribute("ExtnUnitPriceDiscount", "0");
			orderLineExtnChange.setAttribute("ExtnUnitPrice", chargeAmount);
			orderLineExtnChange.setAttribute("ExtnExtendedPrice", chargeAmount);
			orderLineExtnChange.setAttribute("ExtnAdjUOMUnitPrice", "0");
			Element linePriceInfoElement = SCXmlUtil.createChild(orderLineChanegElem, "LinePriceInfo");
			linePriceInfoElement.setAttribute("UnitPrice", chargeAmount);
			linePriceInfoElement.setAttribute("IsPriceLocked", "Y");
			
		}
		else
		{
			orderLineChanegElem.setAttribute("OrderLineKey", orderLineKey);
		}
		createOrderExtnElem(orderChangeElem,orderExtn,action);
		return inputDocument;
	}
	
	private void createOrderExtnElem(Element orderElem,Element orderExtn,String action)
	{
		double totalOrderValue=SCXmlUtil.getDoubleAttribute(orderExtn, "ExtnTotalOrderValue");
		double totalOrderValueWithoutTax=SCXmlUtil.getDoubleAttribute(orderExtn, "ExtnTotOrdValWithoutTaxes");
		double orderSubTotal=SCXmlUtil.getDoubleAttribute(orderExtn, "ExtnOrderSubTotal");
		Element orderExtnElement =SCXmlUtil.createChild(orderElem, "Extn");
		if(YFCCommon.isVoid(chargeAmount))
		{
			chargeAmount="0";
		}
		double chargeDoubelVal=Double.parseDouble(chargeAmount);
		if("CREATE".equals(action))
		{
			totalOrderValue += chargeDoubelVal;
			totalOrderValueWithoutTax +=chargeDoubelVal;
			orderSubTotal +=chargeDoubelVal;
		}
		else
		{
			totalOrderValue -= chargeDoubelVal;
			totalOrderValueWithoutTax -=chargeDoubelVal;
			orderSubTotal -=chargeDoubelVal;
		}
		orderExtnElement.setAttribute("ExtnTotOrdValWithoutTaxes", ""+totalOrderValueWithoutTax);
		orderExtnElement.setAttribute("ExtnTotalOrderValue", ""+totalOrderValue) ;
		orderExtnElement.setAttribute("ExtnOrderSubTotal", ""+orderSubTotal) ;
	}
	/*private void setYFSEnvironmentVariables() {
		if (orderHeaderKey != null && !"".equals(orderHeaderKey))
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("CartOrderHeaderKey", orderHeaderKey);
			map.put("isPnACall", "true");
			map.put("isAddItemToCart","true");
			map.put("isDiscountCalculate", "true");
			XPEDXWCUtils.setYFSEnvironmentVariables(getWCContext(), map);
		}
	}*/
	protected void manipulateMashupInputs(Map mashupInputs)
    throws com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException
	{
		//if (!"true".equals(isComingFromCheckout))		// Removing changeOrder call for Performance improvement, while checkout
			manipulateMashupInputs(mashupInputs,zeroOrderLines);
	}
	
	protected void manipulateMashupInputs(Map mashupInputs,String isOrderLine)
    throws com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException
	{
	    Element inputXML = (Element)mashupInputs.get(mashUpId);
	    if(inputXML == null)
	        throw new com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException("Cannot locate InputXML for xpedx_me_changeOrderLineDetails mashup for manipulation");
	    
	    if("true".equals(isOrderLine))
		{
			return;
		}
	    if(selectedDeliveryMethods == null)
	        throw new com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException("No selectedDeliveryMethods were found.");
	    if(originalDeliveryMethods == null)
	        throw new com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException("No originalDeliveryMethods were found.");
	    try
	    {
	        for(int i = 0; i < originalDeliveryMethods.size(); i++)
	        {
	            String originalMethod = (String)originalDeliveryMethods.get(i);
	            if("PICK".equals(originalMethod))
	            {
	               String selectedMethod = (String)selectedDeliveryMethods.get(i);
	                if(!originalMethod.equals(selectedMethod))
	                {
	                    String orderLineKey = (String)orderLineKeys.get(i);
	                    Element orderLineEl = XMLUtilities.getElement(inputXML, (new StringBuilder()).append("/Order/OrderLines/OrderLine[@OrderLineKey='").append(orderLineKey).append("']").toString());
	                    orderLineEl.setAttribute("ShipNode", "");
	                    orderLineEl.setAttribute("ReqShipDate", "");
	                }
	            }
	        }
	        NodeList inputNodeList = inputXML.getElementsByTagName("OrderLines");
    		Element inputNodeListElemt = (Element)inputNodeList.item(0);
    		SCXmlUtils util = SCXmlUtils.getInstance();
    		
    		NodeList orderLineElemList = inputNodeListElemt.getElementsByTagName("OrderLine");
    		if(orderLineElemList!=null && orderLineElemList.getLength()>0)
    		{
    			for(int k =0;k<orderLineElemList.getLength();k++)
    			{
    				Element orderLineElement = (Element)orderLineElemList.item(k);
    				String noteKey = (String)lineNotesKey.get(k);
        			String note = (String)orderLineNote.get(k);
        			
    				/*Element orderLineNotesElement = util.createChild(orderLineElement, "Instructions");
    				Element orderLineNoteElement = util.createChild(orderLineNotesElement, "Instruction");*/
    				HashMap<String, String> attrMap =  new HashMap<String, String>();
    				if(noteKey!=null && noteKey.trim().length() > 0)
    				{
    					//orderLineNoteElement.setAttribute("InstructionDetailKey", noteKey);
    					attrMap.put("InstructionDetailKey", noteKey);
    				}
    				if(note == null || note.trim().length() == 0)
        			{
    					if(noteKey!=null && noteKey.length()>0){
    						//orderLineNoteElement.setAttribute("Action", "REMOVE");
    						attrMap.put("Action", "REMOVE");
    					}
        			}
    				else
    				{
    					//orderLineNoteElement.setAttribute("InstructionText", note);
    					attrMap.put("InstructionText", note);
    				}
    				if(attrMap!=null && !attrMap.isEmpty())
    				{
    					Element orderLineNotesElement = util.createChild(orderLineElement, "Instructions");
        				Element orderLineNoteElement = util.createChild(orderLineNotesElement, "Instruction");
        				Iterator<String> attrIter = attrMap.keySet().iterator();
        				while(attrIter.hasNext())
        				{
        					String attribute = attrIter.next();
        					String attributeVal = attrMap.get(attribute);
        					orderLineNoteElement.setAttribute(attribute, attributeVal);
        				}
        				orderLineNoteElement.setAttribute("InstructionType", "LINE");
    				}
    				
    			}
    		}
    		String inputXml = SCXmlUtil.getString(inputXML);
			log.debug("Input XML: " + inputXml);
    	}
	    catch(Exception e)
	    {
	        throw new com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException("Error encountered manipulating InputXML", e);
	    }
	}
	
	//This method is not being called owing to the latest changes in Draft Order Details jsp.
	public String updateNotes()
    {
        try
        {
        	/*mashUpId = "xpedx_me_changeOrderLineNotes";
        	prepareAndInvokeMashups();
            if(orderHeaderKey != null && orderHeaderKey.equals(XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext())))
                CommerceContextHelper.refreshCartInContextData(getWCContext());
            return "success";*/
        	
        	Map<String, String> valueMap = new HashMap<String, String>();
    		valueMap.put("/Order/@OrderHeaderKey", orderHeaderKey);
    		//RUgrani - OrderChange: Record the pending changes
    		valueMap.put("/Order/PendingChanges/@RecordPendingChanges",getRecordPendingChanges());
    		Element input = WCMashupHelper.getMashupInput("xpedx_me_changeOrderLineNotes",valueMap,
    				getWCContext().getSCUIContext());
    		NodeList inputNodeList = input.getElementsByTagName("OrderLines");
    		Element inputNodeListElemt = (Element)inputNodeList.item(0);
    		int index = -1;
    		if(orderLineKeys.contains(orderLineKeyForNote))
    		{
    			index = orderLineKeys.indexOf(orderLineKeyForNote);
    		}
    		
    		if(index!= -1)
    		{
    			String noteKey = (String)lineNotesKey.get(index);
    			String note = (String)orderLineNote.get(index);
    			
    			SCXmlUtils util = SCXmlUtils.getInstance();
    			
				Element orderLineElement = util.createChild(inputNodeListElemt, "OrderLine");
				orderLineElement.setAttribute("Action", "MODIFY");
				orderLineElement.setAttribute("OrderLineKey", orderLineKeys.get(index));
				
				Element orderLineNotesElement = util.createChild(orderLineElement, "Instructions");
				Element orderLineNoteElement = util.createChild(orderLineNotesElement, "Instruction");
				
				if(noteKey!=null && noteKey.trim().length() > 0)
				{
					orderLineNoteElement.setAttribute("InstructionDetailKey", noteKey);
				}
				if(note == null || note.trim().length() == 0)
    			{
					orderLineNoteElement.setAttribute("Action", "REMOVE");
    			}
				else
				{
					orderLineNoteElement.setAttribute("InstructionText", note);
				}
				String inputXml = SCXmlUtil.getString(input);
				log.debug("Input XML: " + inputXml);
				Object obj = WCMashupHelper.invokeMashup("xpedx_me_changeOrderLineNotes", input,
						getWCContext().getSCUIContext());
				Document outputDoc = ((Element) obj).getOwnerDocument();
				if (null != outputDoc) {
					log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
				}
    		}
    		
        	return "success";
        	
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
            WCUtils.setErrorInContext(getWCContext(), e);
            return "error";
        }
    }
	
	public List getOrderLineNote() {
		return orderLineNote;
	}

	public void setOrderLineNote(List orderLineNote) {
		this.orderLineNote = orderLineNote;
	}

	public List getLineNotesKey() {
		return lineNotesKey;
	}

	public void setLineNotesKey(List lineNotesKey) {
		this.lineNotesKey = lineNotesKey;
	}
	
	public String getOrderLineKeyForNote() {
		return orderLineKeyForNote;
	}

	public void setOrderLineKeyForNote(String orderLineKeyForNote) {
		this.orderLineKeyForNote = orderLineKeyForNote;
	}
	
	protected List orderLineNote;
	protected List lineNotesKey;
	public String orderLineKeyForNote;
	protected String zeroOrderLines;
	protected String isEditOrder;
	protected String isComingFromCheckout;
	protected String minOrderAmount;
	protected String chargeAmount;
	public String getIsEditOrder() {
		return isEditOrder;
	}
	public void setIsEditOrder(String isEditOrder) {
		this.isEditOrder = isEditOrder;
	}
	public final Logger log = Logger.getLogger(XPEDXDraftOrderModifyLineItemsAction.class);

	public String getZeroOrderLines() {
		return zeroOrderLines;
	}

	public void setZeroOrderLines(String zeroOrderLines) {
		this.zeroOrderLines = zeroOrderLines;
	}

	public String getMinOrderAmount() {
		return minOrderAmount;
	}

	public void setMinOrderAmount(String minOrderAmount) {
		this.minOrderAmount = minOrderAmount;
	}

	public String getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(String chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public String getIsComingFromCheckout() {
		return isComingFromCheckout;
	}

	public void setIsComingFromCheckout(String isComingFromCheckout) {
		this.isComingFromCheckout = isComingFromCheckout;
	}

	
}

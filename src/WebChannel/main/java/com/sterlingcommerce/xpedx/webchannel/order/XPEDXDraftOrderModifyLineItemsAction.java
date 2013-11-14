package com.sterlingcommerce.xpedx.webchannel.order;
 
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.compat.SCXmlUtils;
import com.sterlingcommerce.webchannel.order.DraftOrderModifyLineItemsAction;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCUtils;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItem;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItemPricingInfo;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceAndAvailability;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
 
public class XPEDXDraftOrderModifyLineItemsAction extends DraftOrderModifyLineItemsAction
{
    public static final String CHANGE_ORDER_LINE_DETAILS_MASHUP = "xpedx_me_changeOrderLineDetails";
    public static final String CHECKOUT_ORDER_MASHUP="xpedx_checkout_changeOrderLineDetails";
    public static final String CHANGE_ORDEROUTPUT_CHECKOUT_SESSION_OBJ = "changeOrderAPIOutputForCheckout";
    public static final String CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ = "changeOrderAPIOutputForOrderLinesModification";
     
    public String mashUpId = null;  
     
    /*Added  for Webtrends :  to check if "Save/Update Cart is hit */
    private boolean updateCartMetaTag = false;
     
    protected String validateCustomerFields = "N";
    private boolean isAnyMLine=false;
    private String orderLineKey="";
    private String modifiedOrderHeaderKey="";
    private double totalAmount;
    private Element modifiedOrderExtnForSpecailCharge;
    private String modifyOrderLines="false";
    public String draftOrderFlag;
    public String customerContactId;
	public String draftOrderError;
    private boolean isOUErrorPage=false;
 
    //Added for EB-464
    public ArrayList <String> orderedQtyForCustUom;
    public ArrayList <String> orderLineQuantities;
    public ArrayList <String> itemUOMs;
    public ArrayList <String> orderLineItemIDs;
    private ArrayList <String> customerUOMConvFactors;
    
    
    public ArrayList<String> getCustomerUOMConvFactors() {
		return customerUOMConvFactors;
	}

	public void setCustomerUOMConvFactors(ArrayList<String> customerUOMConvFactors) {
		this.customerUOMConvFactors = customerUOMConvFactors;
	}

	public String getCustomerContactId() {
		return customerContactId;
	}

	public void setCustomerContactId(String customerContactId) {
		this.customerContactId = customerContactId;
	}

     
    public ArrayList<String> getOrderLineItemIDs() {
        return orderLineItemIDs;
    }
 
    public ArrayList<String> getItemUOMs() {
        return itemUOMs;
    }
 
    public ArrayList<String> getOrderLineQuantities() {
        return orderLineQuantities;
    }
 
    public ArrayList<String> getOrderedQtyForCustUom() {
        return orderedQtyForCustUom;
    }
 
    public void setOrderedQtyForCustUom(ArrayList<String> orderedQtyForCustUom) {
        this.orderedQtyForCustUom = orderedQtyForCustUom;
    }
 
    public LinkedHashMap<String, String> itemAndCustomerUomWithConvHashMap = new LinkedHashMap<String, String>();
 
    public LinkedHashMap<String, String> getItemAndCustomerUomWithConvHashMap() {
        return itemAndCustomerUomWithConvHashMap;
    }
 
    public void setItemAndCustomerUomWithConvHashMap(
            LinkedHashMap<String, String> itemAndCustomerUomWithConvHashMap) {
        this.itemAndCustomerUomWithConvHashMap = itemAndCustomerUomWithConvHashMap;
    }
 
    public String getDraftOrderError() {
        return draftOrderError;
    }
 
    public void setDraftOrderError(String draftOrderError) {
        this.draftOrderError = draftOrderError;
    }
 
    public String getDraftOrderFlag() {
        return draftOrderFlag;
    }
 
    public void setDraftOrderFlag(String draftOrderFlag) {
        this.draftOrderFlag = draftOrderFlag;
    }
 
    public String getModifyOrderLines() {
        return modifyOrderLines;
    }
 
    public void setModifyOrderLines(String modifyOrderLines) {
        this.modifyOrderLines = modifyOrderLines;
    }
 
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
        else if ("true".equals(isComingFromCheckout)) {    //Removing changeOrder call for Performance improvement, while checkout
            mashUpId = CHECKOUT_ORDER_MASHUP;
        }
        else{
            mashUpId = CHANGE_ORDER_LINE_DETAILS_MASHUP;
             
        }
        String isSalesRep = (String) wcContext.getSCUIContext().getSession().getAttribute("IS_SALES_REP");
        if(isSalesRep!=null && isSalesRep.equalsIgnoreCase("true")){
			 customerContactId = wcContext.getLoggedInUserId();
		}
		else{
			customerContactId=wcContext.getCustomerContactId();
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
            //start of XBT 252 & 248
            String editedOrderHeaderKey = XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
            if(YFCCommon.isVoid(editedOrderHeaderKey)){
                draftOrderFlag="Y";    
            }
            else {
                draftOrderFlag="N";    
            }
            String customerUOMConvFact="";
            for(int i=0;orderLineItemIDs != null && i<orderLineItemIDs.size();i++)
            {
            	if(customerUOMConvFactors != null)
            	{
	            	customerUOMConvFact=customerUOMConvFactors.get(i);
	            	if(customerUOMConvFact != null && customerUOMConvFact.trim().length() > 0)
	            		itemAndCustomerUomWithConvHashMap.put(orderLineItemIDs.get(i), customerUOMConvFact);
            	}
            }
            //end of XBT 252 & 248
            //Added for EB-464 - if clicked on checkout button, since its calling change order, setting the value for ExtnBaseOrderQuantity for customer Uoms
	        if ("true".equals(isComingFromCheckout)){
	           // itemAndCustomerUomWithConvHashMap = (LinkedHashMap<String, String>) XPEDXWCUtils.getObjectFromCache("ItemCustomerUomWithConvFactors");
	            int i= itemUOMs.size();
	            int j= orderLineQuantities.size();
	            String itemid="";
	            orderedQtyForCustUom = new ArrayList();
	            for (int k=0; k<j;k++){
	                itemid = orderLineItemIDs.get(k);
	                BigDecimal orderlineqty = new BigDecimal(orderLineQuantities.get(k));
	                String temp = itemAndCustomerUomWithConvHashMap.get(itemid);
	                if(temp!=null && !temp.equalsIgnoreCase("")){
	                    int cnt = temp.indexOf('|');
	                    String custUom = temp.substring(0, cnt);
	                    String convFactor = temp.substring(cnt+1, temp.length());
	                    BigDecimal convFact = new BigDecimal(convFactor);
	                    if(custUom.equalsIgnoreCase(itemUOMs.get(k))){
	                        BigDecimal res = orderlineqty.multiply(convFact) ;
	                         
	                        int extnBaseOrderedQty = res.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();                          
	                         
	                        if(extnBaseOrderedQty==0)
	                        {
	                            extnBaseOrderedQty =1;
	                        }
	                        orderedQtyForCustUom.add(Integer.toString(extnBaseOrderedQty));
	                    }
	                    else{
	                        orderedQtyForCustUom.add("0");
	                    }
	                }
	                else{
	                    orderedQtyForCustUom.add("0");
	                }
	            }
	        }	        
	        
	        Object approveOrderSessionVar=XPEDXWCUtils.getObjectFromCache(XPEDXConstants.APPROVE_ORDER_FLAG);
			XPEDXWCUtils.removeObectFromCache(XPEDXConstants.APPROVE_ORDER_FLAG);
			String approveOrderFlag=null;
			if(approveOrderSessionVar!=null)
			{
				approveOrderFlag=approveOrderSessionVar.toString();
			}
	        if("false".equals(approveOrderFlag))
    		{
	        	Set<String> mashupIdSet = new HashSet<String>();
    			mashupIdSet.add(mashUpId);
    			Map<String, Element> changeOrderInputObj=prepareMashupInputs(mashupIdSet);
    			manipulateMashupInputs(changeOrderInputObj);//added for fetching the instructions element
				Document changeOrderInputDoc = changeOrderInputObj.get(mashUpId).getOwnerDocument();
				Element changeOrderInputElem = changeOrderInputDoc.getDocumentElement();
				changeOrderInputElem.setAttribute(XPXLiterals.CUSTOMER_CONTACT_ID, getCustomerContactId());
    			Element outputElement = (Element) WCMashupHelper.invokeMashup(mashUpId, changeOrderInputElem, wcContext.getSCUIContext());
    			outputDocument=outputElement.getOwnerDocument();
    		
    		} else {    			
    			Map<String, Element> out = prepareAndInvokeMashups();
	        	outputDocument = (Document)out.get(mashUpId).getOwnerDocument();
    		}
		    
	        retVal= SUCCESS;	              	        
           
            if("true".equals(isComingFromCheckout))
            {            	
            	retVal=validateMaxOrderAmountWhileCheckOut(outputDocument);
            }
            
         }
        catch(XMLExceptionWrapper e)
        {
              YFCElement errorXML=e.getXML();
              YFCElement errorElement=(YFCElement)errorXML.getElementsByTagName("Error").item(0);
              String errorDeasc=errorElement.getAttribute("ErrorDescription");
              if(errorDeasc.contains("Key Fields cannot be modified."))
              {
                    YFCNodeList listAttribute=errorElement.getElementsByTagName("Attribute");
                    for(int i=0;i<listAttribute.getLength();i++)
                    {
                          YFCElement attributeELement=(YFCElement)listAttribute.item(i);
                          String value=attributeELement.getAttribute("Value");
                          if("DraftOrderFlag".equals(value))
                          {
                                draftOrderError = "true";
                                break;
                          }
                    }
              }
              YFCNodeList<YFCElement> errorNodeList=errorXML.getElementsByTagName("Error");
              for(YFCElement errorEle:errorNodeList)
              {
                  String errorCode=errorEle.getAttribute("ErrorCode");
                  if(XPEDXConstants.UE_ERROR_CODE.equalsIgnoreCase(errorCode) || XPEDXConstants.UE_ERROR_CODE1.equalsIgnoreCase(errorCode))
                  {
                      isOUErrorPage=true;
                      break;
                  }
              }
              if(isOUErrorPage)
              {
                  return "OUErrorPage";
              }
              //XBT 248
              if ("true".equals(isComingFromCheckout))
              {
                      retVal= "checkoutError";  
              }
              else
              {
                  retVal= SUCCESS;  
              }
              
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
        boolean chngOrderOutputAvailable=false;
        if("true".equals(isComingFromCheckout) || "true".equals(modifyOrderLines))
        {
            chngOrderOutputAvailable=true;
        }
        if(isEditOrder.contains("true"))
        {
            if("Y".equalsIgnoreCase(YFSSystem.getProperty("applyMinOrderCharge")))
            {
            	// global settings is enabled
                XPEDXShipToCustomer shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
                if(shipToCustomer!=null)
                {
                    String applyMinOrderBrands_DivisionLevel=shipToCustomer.getShipToOrgExtnApplyMinOrderBrands();
                    if(applyMinOrderBrands_DivisionLevel==null)
                    {                        
                        applyMinOrderBrands_DivisionLevel=getExtnApplyMinOrderBrands(shipToCustomer.getExtnEnvironmentCode(), shipToCustomer.getExtnShipFromBranch());                    
                    }
                     
                    if (XPXUtils.isApplyMinimumOrderChargeForBrand(applyMinOrderBrands_DivisionLevel, getWCContext().getStorefrontId()))
                    {
                        processSpecialCharge(outputDocument, chngOrderOutputAvailable);
                    }
                }
            }
        }  
                
        if(outputDocument!=null && retVal.equals(SUCCESS))
        {
            if("true".equals(isComingFromCheckout))
            {
                getWCContext().getSCUIContext().getSession().setAttribute(CHANGE_ORDEROUTPUT_CHECKOUT_SESSION_OBJ, outputDocument);
             
            }else if ("true".equals(modifyOrderLines))
            {
                getWCContext().getSCUIContext().getSession().setAttribute(CHANGE_ORDEROUTPUT_MODIFYORDERLINES_SESSION_OBJ, outputDocument);
            }
        
        } 
        
        XPEDXWCUtils.releaseEnv(wcContext);
         
        long endTime=System.currentTimeMillis();
        System.out.println("Time taken in milliseconds in XPEDXDraftOrderModifyLineItemsAction class : "+(endTime-startTime));
        return retVal;
   
    }    
     
    private void processSpecialCharge(Document outputDoc,  boolean chngOrderOutputAvailable)
    {
        try
        {
            LOG.debug("Calling processSpecialCharge");
            UtilBean util=new UtilBean();
             
            Map<String, String> valueMap1 = new HashMap<String, String>();
            Element orderElem = null;
            Element input1=null;
            Object obj1=null;
            /*Begin - Changes made by Mitesh Parikh for JIRA#3595*/
            if(chngOrderOutputAvailable)
            {
                valueMap1.put("/Order/@OrderHeaderKey", orderHeaderKey);                
                input1 = WCMashupHelper.getMashupInput("xpedx_get_completeorderList",
                            valueMap1, wcContext.getSCUIContext());
                obj1 = WCMashupHelper.invokeMashup("xpedx_get_completeorderList",
                                input1, wcContext.getSCUIContext());        
                 
                orderElem = ((Element) obj1).getOwnerDocument().getDocumentElement();
             
            } else {
                orderElem = outputDoc.getDocumentElement();
             
            }
            /*End - Changes made by Mitesh Parikh for JIRA#3595*/
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
                LOG.debug("Input XML = "+SCXmlUtil.getString(inputDocument) );
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
        //if (!"true".equals(isComingFromCheckout))        // Removing changeOrder call for Performance improvement, while checkout
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
    protected String maxOrderAmount;
    private String customerFieldsValidated;
    private String ajaxLineStatusCodeMsg = "";
    private Map<String,String> pnALineErrorMessage;
    private HashMap<String, JSONObject> pnaHoverMap; 
    private HashMap<String, XPEDXItemPricingInfo> priceHoverMap;
    private HashMap orderMultipleMapFromSourcing;	
	private HashMap useOrderMultipleMapFromSourcing;
    
	
    public HashMap getOrderMultipleMapFromSourcing() {
		return orderMultipleMapFromSourcing;
	}

	public void setOrderMultipleMapFromSourcing(HashMap orderMultipleMapFromSourcing) {
		this.orderMultipleMapFromSourcing = orderMultipleMapFromSourcing;
	}

	public HashMap getUseOrderMultipleMapFromSourcing() {
		return useOrderMultipleMapFromSourcing;
	}

	public void setUseOrderMultipleMapFromSourcing(
			HashMap useOrderMultipleMapFromSourcing) {
		this.useOrderMultipleMapFromSourcing = useOrderMultipleMapFromSourcing;
	}

	public HashMap<String, XPEDXItemPricingInfo> getPriceHoverMap() {
		return priceHoverMap;
	}

	public void setPriceHoverMap(HashMap<String, XPEDXItemPricingInfo> priceHoverMap) {
		this.priceHoverMap = priceHoverMap;
	}
	
	public HashMap<String, JSONObject> getPnaHoverMap() {
		return pnaHoverMap;
	}

	public void setPnaHoverMap(HashMap<String, JSONObject> pnaHoverMap) {
		this.pnaHoverMap = pnaHoverMap;
	}

	public String getAjaxLineStatusCodeMsg() {
		return ajaxLineStatusCodeMsg;
	}

	public void setAjaxLineStatusCodeMsg(String ajaxLineStatusCodeMsg) {
		this.ajaxLineStatusCodeMsg = ajaxLineStatusCodeMsg;
	}

	public Map<String, String> getPnALineErrorMessage() {
		return pnALineErrorMessage;
	}

	public void setPnALineErrorMessage(Map<String, String> pnALineErrorMessage) {
		this.pnALineErrorMessage = pnALineErrorMessage;
	}
	
    public String getCustomerFieldsValidated() {
		return customerFieldsValidated;
	}

	public void setCustomerFieldsValidated(String customerFieldsValidated) {
		this.customerFieldsValidated = customerFieldsValidated;
	}

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
 
    public String getMaxOrderAmount() {
		return maxOrderAmount;
	}

	public void setMaxOrderAmount(String maxOrderAmount) {
		this.maxOrderAmount = maxOrderAmount;
	}
	
    private String getExtnApplyMinOrderBrands(String envCode, String shipFromBranch) {
        String extnApplyMinOrderBrands=null;
        if (shipFromBranch != null && shipFromBranch.trim().length() > 0)
        {
                 
            if (envCode != null && envCode.trim().length() > 0)
            {
                shipFromBranch = shipFromBranch    + "_" + envCode;
            }                    
             
            SCUIContext wSCUIContext = wcContext.getSCUIContext();
            ISCUITransactionContext scuiTransactionContext = wSCUIContext.getTransactionContext(true);
            YFSEnvironment env  = (YFSEnvironment) scuiTransactionContext.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
             
            YFCDocument organizationListInputDoc = YFCDocument.createDocument(XPXLiterals.E_ORGANIZATION);
            organizationListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE, shipFromBranch);
             
            env.setApiTemplate(XPXLiterals.GET_ORGANIZATION_LIST_API, SCXmlUtil.createFromString("<OrganizationList><Organization OrganizationName=\"\"><Extn ExtnApplyMinOrderBrands=\"\"/></Organization></OrganizationList>"));
            try
            {            
                Document organizationListOutDoc = YIFClientFactory.getInstance().getApi().invoke(env, XPXLiterals.GET_ORGANIZATION_LIST_API, organizationListInputDoc.getDocument());
                env.clearApiTemplate(XPXLiterals.GET_ORGANIZATION_LIST_API);
                if(organizationListOutDoc!=null)
                {
                    extnApplyMinOrderBrands=SCXmlUtil.getXpathAttribute(organizationListOutDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnApplyMinOrderBrands");
                     
                }
            }catch(Exception ex)
            {
                LOG.debug("Exception, inside getExtnApplyMinOrderBrands method of XPEDXDraftOrderModifyLineItemsAction class, while retrieving the value of ExtnApplyMinOrderBrands from yfs_organization table. Exception is : "+ ex);
             
            }finally
            {
                SCUITransactionContextHelper.releaseTransactionContext(
                        scuiTransactionContext, wSCUIContext);
                scuiTransactionContext = null;
                env = null;
            }            
        }
        return extnApplyMinOrderBrands;
         
    }
    
    /**
     * Added this validation method while doing checkout.
     * EB-1389 - As a Customer User when I click either Update or Checkout from Cart, I should be stopped from proceeding to checkout if my order total exceeds the maximum set in CC 
     * @param outputDocument
     * @return
     */
    private String validateMaxOrderAmountWhileCheckOut(Document outputDocument)    {    	
    	double totOrdValWithoutTaxes = 0;
    	 if(YFCCommon.isVoid(maxOrderAmount))
         {
    		 maxOrderAmount="0";
         }  
    	Element orderExtnElement = SCXmlUtil.getXpathElement(outputDocument.getDocumentElement(), "//Order/Extn");
    	totOrdValWithoutTaxes =  SCXmlUtil.getDoubleAttribute(orderExtnElement,"ExtnTotOrdValWithoutTaxes");
		
    	if(Double.parseDouble(maxOrderAmount) > 0 && totOrdValWithoutTaxes > Double.parseDouble(maxOrderAmount)){    
    		return "checkoutError";
    	}
    	return SUCCESS;
    	
      }
    
    private void setPnaHoverForEditOrderLine(XPEDXPriceAndAvailability pna, Document outputDocument, Document lineTpeMDoc)
	{
		try
		{
			if(outputDocument!=null && "true".equals(isEditOrder))
			{
				XPEDXItem item=null;
				Element orderLeme=(Element)outputDocument.getElementsByTagName("PriceInfo").item(0);
				String currencyCode=orderLeme.getAttribute("Currency");
				List<Element> orderLines=SCXmlUtil.getElements(outputDocument.getDocumentElement(), "OrderLines/OrderLine");
				if (orderLines != null && orderLines.size() > 0)
				{
					Iterator<Element> it=orderLines.iterator();					
					Element lineTypeElem=lineTpeMDoc.getDocumentElement();
					while(it.hasNext())
					{
						Element orderLineElem=it.next();
						Element extnElem=(Element)orderLineElem.getElementsByTagName("Extn").item(0);
						String extnEditOrderFlag=extnElem.getAttribute("ExtnEditOrderFlag");
						Element orderLineTran=(Element)orderLineElem.getElementsByTagName("OrderLineTranQuantity").item(0);
						String lineType=orderLineElem.getAttribute("LineType");
						if(!"Y".equals(extnEditOrderFlag))
						{
							Element itemElem=(Element)orderLineElem.getElementsByTagName("Item").item(0);
							item=new XPEDXItem();
							item.setLegacyProductCode(itemElem.getAttribute("ItemID"));
							item.setPriceCurrencyCode(currencyCode);
							item.setPricingUOM(extnElem.getAttribute("ExtnPricingUOM"));
							item.setUnitPricePerPricingUOM(extnElem.getAttribute("ExtnUnitPrice"));
							item.setRequestedQtyUOM(orderLineTran.getAttribute("TransactionalUOM"));
							item.setUnitPricePerRequestedUOM(extnElem.getAttribute("ExtnReqUOMUnitPrice"));
							item.setLineNumber(orderLineElem.getAttribute("PrimeLineNo"));
							if("M".equals(lineType))
							{
								Element lineTypeMElem=SCXmlUtil.createChild(lineTypeElem, "Item");
								lineTypeMElem.setAttribute("ItemID", itemElem.getAttribute("ItemID"));
								lineTypeMElem.setAttribute("UnitOfMeasure", extnElem.getAttribute("ExtnPricingUOM"));
								Element primaryInfo=SCXmlUtil.createChild(lineTypeMElem, "PrimaryInformation");
								primaryInfo.setAttribute("MinOrderQuantity", "1");
								primaryInfo.setAttribute("PricingUOM", extnElem.getAttribute("ExtnPricingUOM"));
								primaryInfo.setAttribute("PricingQuantityConvFactor", "1");
							}
							pna.getItems().add(item);
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			LOG.error("Error setting the price for existing user during edititng cart "+e.getMessage());
		}
	}
     
}


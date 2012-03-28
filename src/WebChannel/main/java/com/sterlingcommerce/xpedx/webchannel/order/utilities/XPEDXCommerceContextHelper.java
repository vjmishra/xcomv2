package com.sterlingcommerce.xpedx.webchannel.order.utilities;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.*;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContext;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.webchannel.order.utilities.DraftOrderCreationHelper;
import com.sterlingcommerce.webchannel.utilities.*;
import java.util.*;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXCommerceContextHelper
{

	public XPEDXCommerceContextHelper(){
		
	}

	 public static String getCartInContextOrderHeaderKey(IWCContext webContext)
	 {
	     return getCartInContextOrderHeaderKey(webContext, false);
	 }

	 public static String getCartInContextOrderHeaderKey(IWCContext webContext, boolean forceCreation)
	 {
	     String cartInContextOrderHeaderKey = null;
	     CommerceContext commerceContext = null;
	     try
	     {
	         commerceContext = getCommerceContext(webContext);
	         if(forceCreation && "_NONE_".equals(commerceContext.getOrderHeaderKey())){
	        	Element orderOutput = null;
	        	if (XPEDXOrderUtils.isCartOnBehalfOf(webContext)) {
	        		orderOutput=XPEDXOrderUtils.createNewDraftOrderOnBehalfOf(webContext,NEW_CART_NAME);
	 			} 
	        	else {
	 				orderOutput = DraftOrderCreationHelper.createNewDraftOrder(webContext, NEW_CART_NAME);		
	 			}
	        	if (orderOutput != null) {
	        		 Element orderEl = XMLUtilities.getElement(((Node) orderOutput.getOwnerDocument()), "//Order");
	        		 String orderHeaderKey = orderEl.getAttribute("OrderHeaderKey");
	        		 CommerceContextHelper.overrideCartInContext(webContext,orderHeaderKey);
	             }
	             commerceContext = new CommerceContext(webContext, orderOutput, "forcedCreation");
	             webContext.setWCAttribute("CommerceContextObject", commerceContext, WCAttributeScope.SESSION);
	         }
	     }
	     catch(Exception e)
	     {
	         LOG.error(e.getMessage(), e);
	         return null;
	     }
	     cartInContextOrderHeaderKey = commerceContext.getOrderHeaderKey();
	     if("_NONE_".equals(cartInContextOrderHeaderKey))
	         return null;
	     else
	         return cartInContextOrderHeaderKey;
	 }

	 public static void overrideCartInContext(IWCContext webContext, String orderHeaderKey)
	     throws Exception
	 {
	     CommerceContext origContext = (CommerceContext)webContext.getWCAttribute("CommerceContextObject");
	     CommerceContext newContext = new CommerceContext(webContext, orderHeaderKey, "manualOverride");
	     if(LOG.isDebugEnabled())
	     {
	         Exception e = new Exception("Exception created to obtain stack trace");
	         LOG.debug((new StringBuilder()).append("overrideCartInContext called : origContext=[").append(origContext).append("] : newContext=[").append(newContext).append("]\n").toString(), e);
	     }
	     webContext.setWCAttribute("CommerceContextObject", newContext, WCAttributeScope.SESSION);
	 }

/*	 public static void processProcurementPunchIn(IWCContext webContext)
	     throws Exception
	 {
	     UtilBean util = new UtilBean();
	     IProcurementContext procurementContext = util.getProcurementContext(webContext);
	     java.util.Locale locale = webContext.getSCUIContext().getUserPreferences().getLocale().getJLocale();
	     ResourceBundle rb = ResourceBundle.getBundle("com.sterlingcommerce.webchannel.order.package", locale);
	     int operation = procurementContext.getOperation();
	     if(operation != 1)
	         if(operation == 3)
	         {
	             String orderHeaderKey = procurementContext.getOrderHeaderKey();
	             try
	             {
	                 overrideCartInContext(webContext, orderHeaderKey);
	             }
	             catch(Exception e)
	             {
	                 Object params[] = {
	                     orderHeaderKey
	                 };
	                 String formatString = rb.getString("eProcurementUnableToReadOrder");
	                 MessageFormat mf = new MessageFormat(formatString);
	                 String fullError = mf.format(((Object) (params)));
	                 LOG.error(fullError, e);
	                 throw new Exception(fullError, e);
	             }
	         } else
	         if(operation == 2)
	         {
	             String origOrderHeaderKey = procurementContext.getOrderHeaderKey();
	             String newOrderHeaderKey = null;
	             try
	             {
	                 Map valueMap = new HashMap();
	                 String billToID = XPEDXWCUtils.getLoggedInCustomerFromSession(webContext);
	                 if(billToID==null || billToID.trim().length()<=0)// will be null if there is no ship to selected
	    	 			billToID = webContext.getCustomerId();
	                 valueMap.put("/Order/@BillToID", billToID);
	                 valueMap.put("/Order/@BuyerOrganizationCode", webContext.getCustomerId());
	                 valueMap.put("/Order/@CopyFromOrderHeaderKey", origOrderHeaderKey);
	                 valueMap.put("/Order/@CustomerContactID", webContext.getCustomerContactId());
	                 valueMap.put("/Order/@EnteredBy", webContext.getCustomerContactId());
	                 valueMap.put("/Order/@OrderName", rb.getString("eProcurementCartName"));
	                 Element input = WCMashupHelper.getMashupInput("draftOrderCopy", valueMap, webContext);
	                 Element output = (Element)WCMashupHelper.invokeMashup("draftOrderCopy", input, webContext.getSCUIContext());
	                 newOrderHeaderKey = output.getAttribute("OrderHeaderKey");
	                 overrideCartInContext(webContext, newOrderHeaderKey);
	             }
	             catch(Exception e)
	             {
	                 Object params[] = {
	                     origOrderHeaderKey
	                 };
	                 String formatString = rb.getString("eProcurementUnableToCopyOrder");
	                 MessageFormat mf = new MessageFormat(formatString);
	                 String fullError = mf.format(((Object) (params)));
	                 LOG.error(fullError, e);
	                 throw new Exception(fullError, e);
	             }
	         }
	 } */
	
	 private static CommerceContext getCommerceContext(IWCContext webContext)
	 {
	     CommerceContext commerceContext = (CommerceContext)webContext.getWCAttribute("CommerceContextObject");
	     if(commerceContext == null)
	     {
	         commerceContext = buildNewCommerceContext(webContext);
	         webContext.setWCAttribute("CommerceContextObject", commerceContext, WCAttributeScope.SESSION);
	     }
	     return commerceContext;
	 }
	 
	 public static CommerceContext createNewCartInContext(IWCContext webContext,Element orderElement,String orderHeaderKey)
	 {
	    /* CommerceContext commerceContext = (CommerceContext)webContext.getWCAttribute("CommerceContextObject");
	     if(commerceContext == null)
	     {*/
	    	 CommerceContext commerceContext = buildNewCommerceContext(webContext,orderElement,orderHeaderKey);
	         webContext.setWCAttribute("CommerceContextObject", commerceContext, WCAttributeScope.SESSION);
	    // }
	     return commerceContext;
	 }
	
	 private static CommerceContext buildNewCommerceContext(IWCContext webContext)
	 {
	     CommerceContext commerceContext = null;
	     Element orderElement = null;
	     try
	     {
	         if(!webContext.isProcurementUser())
	             orderElement = getCartInContextFromDB(webContext);
	         if(orderElement != null)
	             commerceContext = new CommerceContext(webContext, orderElement, "dbSearch");
	         else
	             commerceContext = new CommerceContext(webContext, "_NONE_", "dbSearch");
	     }
	     catch(Exception e)
	     {
	         LOG.error((new StringBuilder()).append("Error building CommerceContext from Cart In Context: ").append(e.getMessage()).toString(), e);
	     }
	     return commerceContext;
	 }
	 
	 private static CommerceContext buildNewCommerceContext(IWCContext webContext,Element orderElement,String orderHeaderKey)
	 {
	     CommerceContext commerceContext = null;
	     //Element orderElement = null;
	     try
	     {
	         if(!webContext.isProcurementUser())
	         {
	        	 OrderHelper.setAppSecCtxForOrder(webContext, orderHeaderKey);
	        	 if(orderElement == null)
	        		 orderElement = getDetailForOrderHeaderKey(webContext, orderHeaderKey);
	        	 else
	        		 addModificationRuleToOrderListElement(orderElement);
	         }
	         if(orderElement != null)
	             commerceContext = new CommerceContext(webContext, orderElement, "dbSearch");
	         else
	             commerceContext = new CommerceContext(webContext, "_NONE_", "dbSearch");
	     }
	     catch(Exception e)
	     {
	         LOG.error((new StringBuilder()).append("Error building CommerceContext from Cart In Context: ").append(e.getMessage()).toString(), e);
	     }
	     return commerceContext;
	 }
	
	 private static Element getCartInContextFromDB(IWCContext webContext)
	     throws Exception
	 {
	     Element orderElement = null;	     
	     orderElement = getCartInContextOrderHeaderKeyFromDB(webContext);
	     if(orderElement != null )
	     {
	     Document orderDoc=SCXmlUtil.createFromString(SCXmlUtil.getString(orderElement));
	     orderElement=orderDoc.getDocumentElement();
	     
	    	 String orderHeaderKey = orderElement.getAttribute("OrderHeaderKey");
	         OrderHelper.setAppSecCtxForOrder(webContext, orderHeaderKey);
	         //orderElement = getDetailForOrderHeaderKey(webContext, orderHeaderKey);
	     }
	     return orderElement;
	 }
	
	 private static Element getCartInContextOrderHeaderKeyFromDB(IWCContext webContext)
     throws Exception
 {
     String orderHeaderKey = null;
     Map<String, String> valueMap = new HashMap<String, String>();
     String storefrontId = webContext.getStorefrontId();
     valueMap.put("/Order/@EnterpriseCode", storefrontId);
     String billToID = XPEDXWCUtils.getLoggedInCustomerFromSession(webContext);
     if(billToID==null || billToID.trim().length()<=0)// will be null if there is no ship to selected
    	 billToID = webContext.getCustomerId();
     valueMap.put("/Order/@BillToID", billToID);
     String customerId = "";
     if(XPEDXWCUtils.isCustomerSelectedIntoConext(webContext))
    	 customerId = webContext.getCustomerId();
     valueMap.put("/Order/@BuyerOrganizationCode", customerId);
     String customerContactId = webContext.getCustomerContactId();
     valueMap.put("/Order/@CustomerContactID", customerContactId);
     if(storefrontId == null || customerId == null || customerContactId == null)
         return null;
     Element input = WCMashupHelper.getMashupInput("xpedxlistForCartInContext", valueMap, webContext);
     Element output = (Element)WCMashupHelper.invokeMashup("xpedxlistForCartInContext", input, webContext.getSCUIContext());
     Element orderListElement = XMLUtilities.getElement(output, "//OrderList");
     Element listOrderElement=null;
     if(orderListElement != null)
     {
         List<Element> orderList = XMLUtilities.getChildElements(orderListElement, "Order");
         if(orderList != null && orderList.size() > 0)
         {
             listOrderElement = (Element)orderList.get(0);
             if(listOrderElement != null)
             {
                 orderHeaderKey = listOrderElement.getAttribute("OrderHeaderKey");
                 addModificationRuleToOrderListElement(listOrderElement);
                 XPEDXOrderUtils.refreshMiniCart(webContext, listOrderElement, true, XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
             }
         }
         else
         {
        	 XPEDXOrderUtils.refreshMiniCart(webContext, null, false, XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
        	 XPEDXWCUtils.setObectInCache("OrderHeaderInContext","_CREATE_NEW_");
         }
     }
     return listOrderElement;
 }
	 private static void addModificationRuleToOrderListElement(Element orderElement)
	 {
		 Element overAllTotalElem=SCXmlUtil.createChild(orderElement, "OverallTotals");
		 Element modificationsElem=SCXmlUtil.createChild(orderElement, "Modifications");
		 Element changeCurrencyModificationElem=SCXmlUtil.createChild(modificationsElem, "Modification");
		 changeCurrencyModificationElem.setAttribute("ModificationType", "CHANGE_CURRENCY");
		 changeCurrencyModificationElem.setAttribute("ModificationAllowed", "N");
		 List<Element> orderExtn = XMLUtilities.getChildElements(orderElement, "Extn");
		 if(orderExtn != null && orderExtn.size()>0)
			 overAllTotalElem.setAttribute("AdjustedSubtotalWithoutTaxes", orderExtn.get(0).getAttribute("ExtnTotOrdValWithoutTaxes"));
			 
	 }
/*
	 private static String getCartInContextOrderHeaderKeyFromDB(IWCContext webContext)
	     throws Exception
	 {
	     String orderHeaderKey = null;
	     Map<String, String> valueMap = new HashMap<String, String>();
	     String storefrontId = webContext.getStorefrontId();
	     valueMap.put("/XPEDXOrderListView/@EnterpriseCode", storefrontId);
	     String billToID = XPEDXWCUtils.getLoggedInCustomerFromSession(webContext);
	     if(billToID==null || billToID.trim().length()<=0)// will be null if there is no ship to selected
	    	 billToID = webContext.getCustomerId();
	     valueMap.put("/XPEDXOrderListView/@BillToID", billToID);
	     String customerId = "";
	     if(XPEDXWCUtils.isCustomerSelectedIntoConext(webContext))
	    	 customerId = webContext.getCustomerId();
	     valueMap.put("/XPEDXOrderListView/@BuyerOrganizationCode", customerId);
	     String customerContactId = webContext.getCustomerContactId();
	     valueMap.put("/XPEDXOrderListView/@CustomerContactID", customerContactId);
	     if(storefrontId == null || customerId == null || customerContactId == null)
	         return null;
	     Element input = WCMashupHelper.getMashupInput("xpedxOrderListForContext", valueMap, webContext);
	     Element output = (Element)WCMashupHelper.invokeMashup("xpedxOrderListForContext", input, webContext.getSCUIContext());
	     Element orderListElement = XMLUtilities.getElement(output, "//XPXOrderListViewList");
	     if(orderListElement != null)
	     {
	         List<Element> orderList = XMLUtilities.getChildElements(orderListElement, "XPXOrderListView");
	         if(orderList != null && orderList.size() > 0)
	         {
	             Element listOrderElement = (Element)orderList.get(0);
	             if(listOrderElement  != null)
	                 orderHeaderKey = listOrderElement.getAttribute("OrderHeaderKey");
	         }
	         else
	         {
	        	 XPEDXOrderUtils.refreshMiniCart(webContext, null, false, XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
	        	 XPEDXWCUtils.setObectInCache("OrderHeaderInContext","_CREATE_NEW_");
	         }
	     }
	     return orderHeaderKey;
	 }*/
	
	 static Element getDetailForOrderHeaderKey(IWCContext webContext, String orderHeaderKey)
	     throws Exception
	 {
	     Map<String, String> valueMap = new HashMap<String, String>();
	     valueMap.put("/Order/@OrderHeaderKey", orderHeaderKey);
	     //List<String> itemAndTotalList=new ArrayList<String>();
	     if(!webContext.isGuestUser())
	         if(!orderHeaderKey.equals((String)webContext.getWCAttribute("draftOrderRepriced", WCAttributeScope.SESSION)))
	         {
	             webContext.setWCAttribute("draftOrderRepriced", orderHeaderKey, WCAttributeScope.SESSION);
	             valueMap.put("/Order/@RepriceOrder", "Y");
	         } else
	         {
	             valueMap.put("/Order/@RepriceOrder", "N");
	         }
	     Element input = WCMashupHelper.getMashupInput("detailForCartInContext", valueMap, webContext);
	     Element output = (Element)WCMashupHelper.invokeMashup("detailForCartInContext", input, webContext.getSCUIContext());
	     XPEDXOrderUtils.refreshMiniCart(webContext,output,true,false,XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
	     /*String OrderTotal= SCXmlUtil.getXpathAttribute(output, "/Order/Extn/@ExtnTotalOrderValue");    
    	 ArrayList<Element> orderLineList= SCXmlUtil.getElements(output,"OrderLines/OrderLine");
    	 itemAndTotalList.add(OrderTotal);
    	 if(!YFCCommon.isVoid(orderLineList))
    	 {
    		 itemAndTotalList.add(""+orderLineList.size());
    	 }
    	 else
    	 {
    		 itemAndTotalList.add("0");
    	 }
    	 XPEDXWCUtils.setObectInCache("CommerceContextHelperOrderTotal", itemAndTotalList);*/
	     return output;
	 }
	
	 public static final String COMMERCE_CONTEXT_LOGGING_CREATED = "CommerceContext Created";
	 public static final String COMMERCE_CONTEXT_LOGGING_SOURCE = ": source=";
	 public static final String COMMERCE_CONTEXT_LOGGING_SOURCE_DB_SEARCH = "dbSearch";
	 public static final String COMMERCE_CONTEXT_LOGGINE_SOURCE_FORCED_CREATION = "forcedCreation";
	 public static final String COMMERCE_CONTEXT_LOGGING_SOURCE_MANUAL_OVERRIDE = "manualOverride";
	 static final String NO_CART_IN_CONTEXT = "_NONE_";
	 private final static String NEW_CART_NAME = "DEFAULT_CART";
	 private static final Logger LOG = Logger.getLogger(XPEDXOrderUtils.class);
	 

}
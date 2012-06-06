package com.xpedx.nextgen.dashboard;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * Description: This class used to check the Line Types of the Customer Order and create the three kinds of chained orders, namely
 *              THIRD_PARTY, DIRECT_ORDER and STOCK_ORDER.
 *              
 * @author Prasanth Kumar M.
 *
 */
public class XPXcheckItemTypeAndCreateOrderAPI implements YIFCustomApi{

	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXCreateChainedOrderService.xml";
	
	private static YIFApi api = null;

	private static YFCLogCategory log;

	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
            //Getting the YIF api handle
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}

	Properties props;


	public void setProperties(Properties props) throws Exception {
		this.props = props;
		
	}

	/**
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document checkItemType(YFSEnvironment env,Document inDoc) throws Exception
	{

		log.beginTimer("XPXcheckItemTypeAndCreateOrderAPI.checkItemType");

		Element inputXMLRoot =null;
		Document orderLineElementDoc = null;
		Document thirdPartyOrderCreationOutputDoc = null;
		Document directOrderCreationOutputDoc = null;
		Document stockOrderCreationOutputDoc = null;
		Document getCustomerListOutputDoc = null;

		String orderLineType = "";
		String orderedQty = "";
		String transactionalUOM = ""; 
		String documentType = "";
		String enterpriseCode = "";
		String orderNo = "";
		String primeLineNo = "";
		String subLineNo = "";
		String buyerOrganizationCode = "";
		String entryType = "";
		String customerPONo = "";
		String reqDeliveryDate = "";
        String shipNodeOrder = "";
        String sourceType= "";
        String customerLinePONo = "";
        String orderLineCustomerPONo= "";
        String shipNodeOrderLine = "";
        String checkItemType = "";
        String orderLineShipNode = "";
        String allowDirectOrderFlag = "";

		ArrayList thirdPartyList = new ArrayList();
		ArrayList directOrderList = new ArrayList();
		ArrayList stockOrderList = new ArrayList();
		
		HashMap orderElementContents = new HashMap();
		HashMap orderLineElementContents = new HashMap();
		
		//Need to split the Order Level Total , discount and Subtotal
		double thirPartyExtnOrderSubTotal=0;
		double directExtnOrderSubTotal=0;
		double stockExtnOrderSubTotal=0;
		

		MultiValueMap shipNodesWithOrderLineElements = new MultiValueMap();
		
		/*if(props != null){
            Enumeration e = props.propertyNames();
            while(e.hasMoreElements()){
                String name = (String)e.nextElement();
                
                if(name.equalsIgnoreCase("ITEM_TYPE_CHECK"))
                {
                checkItemType = props.getProperty(name);
                }
                log.debug("Property Name is: " + name + " Value is: " + props.getProperty(name));
            }
		}*/
		
		//log.debug("The input doc to checkItemType&CreateOrder is: "+SCXmlUtil.getString(inDoc));
				
		orderElementContents = createOrderElementHashMap(inDoc);
		
		inputXMLRoot = inDoc.getDocumentElement();
		
		String buyerOrgCode = inputXMLRoot.getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
		
		if(buyerOrgCode != null && buyerOrgCode.trim().length() !=0)
		{
			getCustomerListOutputDoc = (Document)env.getTxnObject("ShipToCustomerProfile");
			
			if(getCustomerListOutputDoc == null)
			{
		       Document getCustomerListInputDoc = YFCDocument.createDocument("Customer").getDocument();
		       getCustomerListInputDoc.getDocumentElement().setAttribute("CustomerID", buyerOrgCode);
		
		        log.debug("The input to getCustomerList for shipTo is: "+SCXmlUtil.getString(getCustomerListInputDoc));	
		        env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
		        getCustomerListOutputDoc = api.invoke(env,XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc);
		        log.debug("The output of getCustomerList for ShipTo is: "+SCXmlUtil.getString(getCustomerListOutputDoc));
			 }
		    Element customerElement = (Element) getCustomerListOutputDoc.getDocumentElement().getElementsByTagName("Customer").item(0);
		    Element customerExtnElement = (Element) customerElement.getElementsByTagName("Extn").item(0);
		    
		    allowDirectOrderFlag = customerExtnElement.getAttribute("ExtnAllowDirectOrderFlag");
		    log.debug("The allow direct orders flag is: "+allowDirectOrderFlag);
		}

		Element orderLines = (Element)inputXMLRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);

		NodeList orderLineList = orderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);

		for(int i=0; i<orderLineList.getLength(); i++)
		{
			Element orderLine = (Element)orderLineList.item(i);
			
			String lineStatus = orderLine.getAttribute("Status");
			log.debug("The status of the line in XPXCheckItemType is: "+lineStatus);
		    /***Added for CR # 2591 by Prasanth Kumar M.**************/
		    if(!"Cancelled".equalsIgnoreCase(lineStatus))
		    {
			orderLineElementContents = createOrderLineElementHashMap(orderLine);
			
			Element orderLineExtn = (Element) orderLine.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
			
			//orderLineType = (String)orderLineElementContents.get(XPXLiterals.A_LINE_TYPE);
			//Changes done as per new Business rules
			orderLineType = orderLineExtn.getAttribute(XPXLiterals.A_EXTN_LINE_TYPE);
			log.debug("The extn line type is: "+orderLineType);
			String extnOrderLinetotatal=orderLineExtn.getAttribute(XPXLiterals.A_EXTN_LINE_ORDERED_TOTAL);
			double extnOrderLinetotatalVal=0;
			if(extnOrderLinetotatal != null)
			{
				extnOrderLinetotatalVal=extnOrderLinetotatalVal+Double.parseDouble(extnOrderLinetotatal);
			}
			
			orderLineShipNode = (String)orderLineElementContents.get(XPXLiterals.A_SHIP_NODE);
			
			
			//Check added as per JIRA # 1419 to split the orders only if AllowDirectOrderFlag="Y"
				
			if("Y".equalsIgnoreCase(allowDirectOrderFlag))
			{
			//Logic to create the respective chained orders based on the OrderLine line type
			//if(orderLineType.equalsIgnoreCase(XPXLiterals.THIRD_PARTY_ORDER))
			  if(orderLineType.equalsIgnoreCase(XPXLiterals.SPECIAL))
			  {
				orderLineElementDoc = createorderLineDoc_New(orderLineElementContents,orderElementContents);
				
				/*orderLineElementDoc = createorderLineDoc(orderedQty,transactionalUOM,itemId, unitOfMeasure,productClass,documentType,
						enterpriseCode,orderNo,primeLineNo, subLineNo, orderLineExtn,itemExtn,linePriceInfo,customerLinePONo,
						orderLineCustomerPONo,shipNodeOrderLine,customerItem,instructions);*/

				thirPartyExtnOrderSubTotal +=extnOrderLinetotatalVal;
				
				thirdPartyList.add(orderLineElementDoc);
			}

			else if(orderLineType.equalsIgnoreCase(XPXLiterals.DIRECT))

			{
				orderLineElementDoc = createorderLineDoc_New(orderLineElementContents,orderElementContents);
				
				/*orderLineElementDoc = createorderLineDoc(orderedQty,transactionalUOM,itemId, unitOfMeasure,productClass,documentType,
						enterpriseCode,orderNo,primeLineNo, subLineNo, orderLineExtn,itemExtn,linePriceInfo,customerLinePONo,
						orderLineCustomerPONo,shipNodeOrderLine,customerItem,instructions);*/
				directExtnOrderSubTotal +=extnOrderLinetotatalVal;
				directOrderList.add(orderLineElementDoc);
			}

			else if(orderLineType.equalsIgnoreCase(XPXLiterals.STOCK))
			{
				orderLineElementDoc = createorderLineDoc_New(orderLineElementContents,orderElementContents);
				
				/*orderLineElementDoc = createorderLineDoc(orderedQty,transactionalUOM,itemId, unitOfMeasure,productClass,documentType,
						enterpriseCode,orderNo,primeLineNo, subLineNo, orderLineExtn,itemExtn,linePriceInfo,customerLinePONo,
						orderLineCustomerPONo,shipNodeOrderLine,customerItem,instructions);*/
				stockExtnOrderSubTotal +=extnOrderLinetotatalVal;
				stockOrderList.add(orderLineElementDoc);
			}

		}
			else
			{
				    if(orderLineType.trim().length()>0)
				    { 	//Means the item type should be determined before this
                     orderLineElementDoc = createorderLineDoc_New(orderLineElementContents,orderElementContents);
				
				     /*orderLineElementDoc = createorderLineDoc(orderedQty,transactionalUOM,itemId, unitOfMeasure,productClass,documentType,
						enterpriseCode,orderNo,primeLineNo, subLineNo, orderLineExtn,itemExtn,linePriceInfo,customerLinePONo,
						orderLineCustomerPONo,shipNodeOrderLine,customerItem,instructions);*/
                      stockExtnOrderSubTotal +=extnOrderLinetotatalVal;
				      stockOrderList.add(orderLineElementDoc);
				    }
			}
			/*else if(checkItemType.equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_N) && orderLineShipNode != null && orderLineShipNode.trim().length()!=0)
			{
				// Logic to create chained orders based on ship node at order line level
				
				orderLineElementDoc = createorderLineDoc_New(orderLineElementContents,orderElementContents);
				
				shipNodesWithOrderLineElements.put(orderLineShipNode, orderLineElementDoc);
						
				
			}*/
			
		   }
		}

		boolean isDiscountApplied=false;
		
		if(stockOrderList.size() > 0)
		{
			Element orderExtn = (Element)orderElementContents.get(XPXLiterals.ORDER_EXTN);
			
			setOrderExtnFields(orderExtn ,isDiscountApplied,stockExtnOrderSubTotal );
			isDiscountApplied=true;
			orderExtn.setAttribute("ExtnLegacyOrderType", "S");
			
			orderElementContents.remove(XPXLiterals.ORDER_EXTN);
			//log.debug("The order extn element is: "+SCXmlUtil.getString(orderExtn));
			orderElementContents.put(XPXLiterals.ORDER_EXTN, orderExtn);
			
			Document chainedOrderInput_STOCK = createChainedOrderInput_New(stockOrderList,orderElementContents,XPXLiterals.STOCK_ORDER,orderLineShipNode);
			
			/*Document chainedOrderInput_STOCK = createChainedOrderInput(stockOrderList,documentType,enterpriseCode,
					buyerOrganizationCode,XPXLiterals.STOCK_ORDER, orderExtn,personInfoShipTo,personInfoBillTo,orderPriceInfo,
					personInfoShipToExtn,personInfoBillToExtn,entryType,customerPONo,reqDeliveryDate,shipNodeOrder,sourceType,instructionsElement);*/

			stockOrderCreationOutputDoc = api.executeFlow(env,XPXLiterals.LEGACY_ORDER_CREATION_SERVICE, chainedOrderInput_STOCK);
		}
		
		if(directOrderList.size() > 0)
		{
			
			Element orderExtn = (Element)orderElementContents.get(XPXLiterals.ORDER_EXTN);
			setOrderExtnFields(orderExtn ,isDiscountApplied,directExtnOrderSubTotal );
			isDiscountApplied=true;
			orderExtn.setAttribute("ExtnLegacyOrderType", "D");
			orderElementContents.remove(XPXLiterals.ORDER_EXTN);
			//log.debug("The order extn element is: "+SCXmlUtil.getString(orderExtn));
			orderElementContents.put(XPXLiterals.ORDER_EXTN, orderExtn);
			
			Document chainedOrderInput_DIRECT = createChainedOrderInput_New(directOrderList,orderElementContents,XPXLiterals.DIRECT_ORDER,orderLineShipNode);
			
			/*Document chainedOrderInput_DIRECT = createChainedOrderInput(directOrderList,documentType,
					enterpriseCode,buyerOrganizationCode,XPXLiterals.DIRECT_ORDER,orderExtn,personInfoShipTo,personInfoBillTo,orderPriceInfo,
					personInfoShipToExtn,personInfoBillToExtn,entryType,customerPONo,reqDeliveryDate,shipNodeOrder,sourceType,instructionsElement);*/

			//log.debug("The input to direct order FO creation is: "+SCXmlUtil.getString(chainedOrderInput_DIRECT));
			directOrderCreationOutputDoc = api.executeFlow(env,XPXLiterals.LEGACY_ORDER_CREATION_SERVICE, chainedOrderInput_DIRECT);
		}		
		
		// Continuation of create order logic by splitting based on item type
		if (thirdPartyList.size()>0)
		{
			Element orderExtn = (Element)orderElementContents.get(XPXLiterals.ORDER_EXTN);
			setOrderExtnFields(orderExtn ,isDiscountApplied,thirPartyExtnOrderSubTotal );
			isDiscountApplied=true;
			Document chainedOrderInput_SPECIAL = createChainedOrderInput_New(thirdPartyList,orderElementContents,XPXLiterals.SPECIAL_ORDER,orderLineShipNode);

			/*Document chainedOrderInput_THIRD_PARTY = createChainedOrderInput(thirdPartyList,documentType,enterpriseCode,
					buyerOrganizationCode,XPXLiterals.THIRD_PARTY_ORDER,orderExtn,personInfoShipTo,personInfoBillTo,orderPriceInfo,
					personInfoShipToExtn,personInfoBillToExtn,entryType,customerPONo,reqDeliveryDate,shipNodeOrder,sourceType,instructionsElement);*/

		    //log.debug("The input to legacy order creation is: "+SCXmlUtil.getString(chainedOrderInput_SPECIAL));     
			thirdPartyOrderCreationOutputDoc = api.executeFlow(env, XPXLiterals.LEGACY_ORDER_CREATION_SERVICE, chainedOrderInput_SPECIAL);

		}

	
		/*else if(checkItemType.equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_N))
		{
		      // Continuation of logic to create chained orders based on ship nodes.
			
			 Set keys = shipNodesWithOrderLineElements.keySet();
			 
			  for (Object k : keys) {  
				  
				  ArrayList arr = (ArrayList) shipNodesWithOrderLineElements.getCollection(k);
				  				       
				  Document chainedOrderInput_k = createChainedOrderInput_New(arr,orderElementContents,"FulfillmentOrder",orderLineShipNode);
					       
				  api.executeFlow(env,XPXLiterals.LEGACY_ORDER_CREATION_SERVICE, chainedOrderInput_k);
			      
			  } 
		}*/
		
		log.endTimer("XPXcheckItemTypeAndCreateOrderAPI.checkItemType");
		return inDoc;

	}


	private void setOrderExtnFields(Element orderExtn ,boolean isDiscountApplied, double extnOrderSubTotal )
	{
		if(!isDiscountApplied)
		{
			String extnTotAdjustments=orderExtn.getAttribute(XPXLiterals.A_EXTN_TOT_ORDER_ADJUSTMENTS);
			orderExtn.setAttribute(XPXLiterals.A_EXTN_ORDER_SUB_TOTAL, ""+extnOrderSubTotal);
			extnOrderSubTotal =extnOrderSubTotal + (extnTotAdjustments != null ? Double.parseDouble(extnTotAdjustments) : 0);
			
		}
		else
		{			
			orderExtn.setAttribute(XPXLiterals.A_EXTN_ORDER_DISCOUNT, "0");
			orderExtn.setAttribute(XPXLiterals.A_EXTN_ORDER_COUPON_DISCOUNT, "0");
			orderExtn.setAttribute(XPXLiterals.A_EXTN_TOT_ORDER_ADJUSTMENTS, "0");
			orderExtn.setAttribute(XPXLiterals.A_EXTN_ORDER_SUB_TOTAL, ""+extnOrderSubTotal);
		}
		orderExtn.setAttribute(XPXLiterals.A_EXTN_TOT_ORD_VAL_WITHOUT_TAXES, ""+extnOrderSubTotal);
		orderExtn.setAttribute(XPXLiterals.A_EXTN_TOTAL_ORDER_VALUE, ""+extnOrderSubTotal);
		
	}
	
	private Document createorderLineDoc_New(HashMap orderLineElementContents, HashMap orderElementContents) {
		
        Document orderLineElementDoc = createDocument(XPXLiterals.E_ORDER_LINE);
		
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUST_LINE_PO_NO, (String)orderLineElementContents.get(XPXLiterals.A_CUST_LINE_PO_NO));
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_PO_NO,(String)orderLineElementContents.get(XPXLiterals.A_CUSTOMER_PO_NO));
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_SHIP_NODE,(String)orderLineElementContents.get(XPXLiterals.A_SHIP_NODE));
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_LINE_TYPE,(String)orderLineElementContents.get(XPXLiterals.A_LINE_TYPE));
		
		
		importElement(orderLineElementDoc.getDocumentElement(),(Element)orderLineElementContents.get(XPXLiterals.ORDER_LINE_EXTN));
		importElement(orderLineElementDoc.getDocumentElement(),(Element)orderLineElementContents.get(XPXLiterals.E_LINE_PRICE_INFO));
		//importElement(orderLineElementDoc.getDocumentElement(),(Element)orderLineElementContents.get(XPXLiterals.E_INSTRUCTIONS));
		//importElement(orderLineElementDoc.getDocumentElement(),(Element)orderLineElementContents.get(XPXLiterals.E_LINE_CHARGES));
		//importElement(orderLineElementDoc.getDocumentElement(),(Element)orderLineElementContents.get(XPXLiterals.E_AWARDS));
		
		
		Element orderLineTranQtyElement = orderLineElementDoc.createElement(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY);
		Element orderLineTranQtyHashMap = (Element)orderLineElementContents.get(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY);
		orderLineTranQtyElement.setAttribute(XPXLiterals.A_ORDERED_QTY, (String)orderLineTranQtyHashMap.getAttribute(XPXLiterals.A_ORDERED_QTY));
		orderLineTranQtyElement.setAttribute(XPXLiterals.A_TRANSACTIONAL_UOM, (String)orderLineTranQtyHashMap.getAttribute(XPXLiterals.A_TRANSACTIONAL_UOM));
		orderLineElementDoc.getDocumentElement().appendChild(orderLineTranQtyElement);

		Element itemElement = orderLineElementDoc.createElement(XPXLiterals.E_ITEM);
		Element itemElementHashMap = (Element)orderLineElementContents.get(XPXLiterals.E_ITEM);
		itemElement.setAttribute(XPXLiterals.A_ITEM_ID, itemElementHashMap.getAttribute(XPXLiterals.A_ITEM_ID));
		itemElement.setAttribute(XPXLiterals.A_UNIT_OF_MEASURE, itemElementHashMap.getAttribute(XPXLiterals.A_UNIT_OF_MEASURE));
		itemElement.setAttribute(XPXLiterals.A_PRODUCT_CLASS, itemElementHashMap.getAttribute(XPXLiterals.A_PRODUCT_CLASS));
		itemElement.setAttribute(XPXLiterals.A_CUSTOMER_ITEM,itemElementHashMap.getAttribute(XPXLiterals.A_CUSTOMER_ITEM));
		orderLineElementDoc.getDocumentElement().appendChild(itemElement);
		
		importElement(itemElement,(Element)orderLineElementContents.get(XPXLiterals.ITEM_EXTN));
		
		
		Element chainedFromElement = orderLineElementDoc.createElement(XPXLiterals.E_CHAINED_FROM);
		
		Element orderElementHashMap = (Element)orderElementContents.get(XPXLiterals.E_ORDER);
		
		chainedFromElement.setAttribute(XPXLiterals.A_DOCUMENT_TYPE, orderElementHashMap.getAttribute(XPXLiterals.A_DOCUMENT_TYPE));
		chainedFromElement.setAttribute(XPXLiterals.A_ENTERPRISE_CODE, orderElementHashMap.getAttribute(XPXLiterals.A_ENTERPRISE_CODE));
		chainedFromElement.setAttribute(XPXLiterals.A_ORDER_NO, orderElementHashMap.getAttribute(XPXLiterals.A_ORDER_NO));
		chainedFromElement.setAttribute(XPXLiterals.A_PRIME_LINE_NO, (String)orderLineElementContents.get(XPXLiterals.A_PRIME_LINE_NO));
		chainedFromElement.setAttribute(XPXLiterals.A_SUB_LINE_NO, (String)orderLineElementContents.get(XPXLiterals.A_SUB_LINE_NO));
		orderLineElementDoc.getDocumentElement().appendChild(chainedFromElement);
		
		log.debug("The createOrderLineElement for chained order is: "+SCXmlUtil.getString(orderLineElementDoc));
		
		return orderLineElementDoc;
		
	}

	private HashMap createOrderLineElementHashMap(Element orderLine) {

         HashMap orderLineElements = new HashMap();
		
		String orderLineType = orderLine.getAttribute(XPXLiterals.A_LINE_TYPE);
		String primeLineNo = orderLine.getAttribute(XPXLiterals.A_PRIME_LINE_NO);
		String subLineNo = orderLine.getAttribute(XPXLiterals.A_SUB_LINE_NO);
		String customerLinePONo = orderLine.getAttribute(XPXLiterals.A_CUST_LINE_PO_NO);
		String orderLineCustomerPONo = orderLine.getAttribute(XPXLiterals.A_CUSTOMER_PO_NO);
		String shipNodeOrderLine = orderLine.getAttribute(XPXLiterals.A_SHIP_NODE);

		Element orderLineExtn = (Element)orderLine.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

		Element orderLineTranQuantity = (Element)orderLine.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
		Element instructions = (Element)orderLine.getElementsByTagName(XPXLiterals.E_INSTRUCTIONS).item(0);
				
		Element item = (Element)orderLine.getElementsByTagName(XPXLiterals.E_ITEM).item(0);
		Element itemExtn = (Element)item.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

		Element linePriceInfo = (Element)orderLine.getElementsByTagName(XPXLiterals.E_LINE_PRICE_INFO).item(0);
		Element lineCharges = (Element)orderLine.getElementsByTagName(XPXLiterals.E_LINE_CHARGES).item(0);
		Element awards = (Element)orderLine.getElementsByTagName(XPXLiterals.E_AWARDS).item(0);
		
		orderLineElements.put(XPXLiterals.A_LINE_TYPE, orderLineType);
		orderLineElements.put(XPXLiterals.A_PRIME_LINE_NO,primeLineNo);
		orderLineElements.put(XPXLiterals.A_SUB_LINE_NO,subLineNo);
		orderLineElements.put(XPXLiterals.A_CUST_LINE_PO_NO,customerLinePONo);
		orderLineElements.put(XPXLiterals.A_CUSTOMER_PO_NO,orderLineCustomerPONo);
		orderLineElements.put(XPXLiterals.A_SHIP_NODE, shipNodeOrderLine);
		orderLineElements.put(XPXLiterals.ORDER_LINE_EXTN,orderLineExtn);
		orderLineElements.put(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY,orderLineTranQuantity);		
        orderLineElements.put(XPXLiterals.E_INSTRUCTIONS,instructions);
        orderLineElements.put(XPXLiterals.E_ITEM,item);
        orderLineElements.put(XPXLiterals.ITEM_EXTN,itemExtn);
        orderLineElements.put(XPXLiterals.E_LINE_PRICE_INFO,linePriceInfo);
        orderLineElements.put(XPXLiterals.E_LINE_CHARGES,lineCharges);
        orderLineElements.put(XPXLiterals.E_AWARDS,awards);
        
        return orderLineElements;
		
	}

	private Document createChainedOrderInput_New(ArrayList thirdPartyList, HashMap orderElementContents, String lineType, String orderLineShipNode) {

		Document createOrderDoc = createDocument(XPXLiterals.E_ORDER);
		
		try 
		{
			copyAttributes(createOrderDoc.getDocumentElement(),(Element)orderElementContents.get(XPXLiterals.E_ORDER));
			createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, "");
			createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_NO,"");
			createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_SHIP_TO_KEY,"");
			createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_BILL_TO_KEY,"");
			createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_PAYMENT_STATUS,"");
			createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_TYPE, lineType);
			createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_SHIP_NODE,orderLineShipNode);
			
			
			importElement(createOrderDoc.getDocumentElement(),(Element)orderElementContents.get(XPXLiterals.ORDER_EXTN));
			//importElement(createOrderDoc.getDocumentElement(), orderPriceInfo);
			
			Element orderPriceInfoElement = createOrderDoc.createElement(XPXLiterals.E_PRICE_INFO);
			
			Element orderPriceInfoHashMap = (Element)orderElementContents.get(XPXLiterals.E_PRICE_INFO);
			String currency = orderPriceInfoHashMap.getAttribute(XPXLiterals.A_CURRENCY);
			
			orderPriceInfoElement.setAttribute(XPXLiterals.A_CURRENCY, currency);
			createOrderDoc.getDocumentElement().appendChild(orderPriceInfoElement);
			
			importElement(createOrderDoc.getDocumentElement(),(Element)orderElementContents.get(XPXLiterals.SHIP_TO_EXTN));
			importElement(createOrderDoc.getDocumentElement(),(Element)orderElementContents.get(XPXLiterals.BILL_TO_EXTN));
			importElement(createOrderDoc.getDocumentElement(),(Element)orderElementContents.get(XPXLiterals.E_INSTRUCTIONS));
			importElement(createOrderDoc.getDocumentElement(), (Element)orderElementContents.get(XPXLiterals.E_PERSON_INFO_BILL_TO));
			importElement(createOrderDoc.getDocumentElement(), (Element)orderElementContents.get(XPXLiterals.E_PERSON_INFO_SHIP_TO));


			Element orderLinesElement = createOrderDoc.createElement(XPXLiterals.E_ORDER_LINES);
			createOrderDoc.getDocumentElement().appendChild(orderLinesElement);

			for(int i=0; i<thirdPartyList.size(); i++)
			{
				Document orderLineDoc = (Document)thirdPartyList.get(i);			
				importElement(orderLinesElement,orderLineDoc.getDocumentElement());			

			}
			
			log.debug("The createOrderDoc for chained order is: "+SCXmlUtil.getString(createOrderDoc));
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return createOrderDoc;
		
	}

	private HashMap createOrderElementHashMap(Document inDoc) {
		
		HashMap orderElements = new HashMap();

		Element inputXMLRoot = inDoc.getDocumentElement();

		/*String documentType = inputXMLRoot.getAttribute(XPXLiterals.A_DOCUMENT_TYPE);
		String enterpriseCode = inputXMLRoot.getAttribute(XPXLiterals.A_ENTERPRISE_CODE);
		String orderNo = inputXMLRoot.getAttribute(XPXLiterals.A_ORDER_NO);
		log.debug("The order no of the Customer order is: "+orderNo);
		String buyerOrganizationCode = inputXMLRoot.getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
		String entryType = inputXMLRoot.getAttribute(XPXLiterals.A_ENTRY_TYPE);
		String customerPONo = inputXMLRoot.getAttribute(XPXLiterals.A_CUSTOMER_PO_NO);
		String reqDeliveryDate = inputXMLRoot.getAttribute(XPXLiterals.A_REQ_DELIVERY_DATE);
		String shipNodeOrder = inputXMLRoot.getAttribute(XPXLiterals.A_SHIP_NODE);
		//sourceType = inputXMLRoot.getAttribute(XPXLiterals.A_SOURCE_TYPE);
*/
		Element orderExtn = (Element) inputXMLRoot.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		Element orderPriceInfo = (Element) inputXMLRoot.getElementsByTagName(XPXLiterals.E_PRICE_INFO).item(0);
		
		Element personInfoShipTo = (Element) inputXMLRoot.getElementsByTagName(XPXLiterals.E_PERSON_INFO_SHIP_TO).item(0);
		Element personInfoShipToExtn = (Element) personInfoShipTo.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

		Element personInfoBillTo = (Element) inputXMLRoot.getElementsByTagName(XPXLiterals.E_PERSON_INFO_BILL_TO).item(0);
		Element personInfoBillToExtn = (Element) personInfoBillTo.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

		//Element notes = (Element) inputXMLRoot.getElementsByTagName(XPXLiterals.E_NOTES).item(0);
        Element instructionsElement = (Element)inputXMLRoot.getElementsByTagName(XPXLiterals.E_INSTRUCTIONS).item(0);
        
        orderElements.put(XPXLiterals.E_ORDER,inputXMLRoot);
        orderElements.put(XPXLiterals.ORDER_EXTN,orderExtn);
        orderElements.put(XPXLiterals.E_PRICE_INFO,orderPriceInfo);
        orderElements.put(XPXLiterals.E_PERSON_INFO_SHIP_TO,personInfoShipTo);
        orderElements.put(XPXLiterals.SHIP_TO_EXTN,personInfoShipToExtn);
        orderElements.put(XPXLiterals.E_PERSON_INFO_BILL_TO,personInfoBillTo);
        orderElements.put(XPXLiterals.BILL_TO_EXTN,personInfoBillToExtn);
        orderElements.put(XPXLiterals.E_INSTRUCTIONS,instructionsElement);
        
		
		return orderElements;
	}

	private Document createChainedOrderInput(ArrayList thirdPartyList, String documentType, String enterpriseCode, String buyerOrganizationCode,
			String orderType, Element orderExtn,Element personInfoShipTo,Element personInfoBillTo, Element orderPriceInfo, Element personInfoShipToExtn,
			Element personInfoBillToExtn, String entryType, String customerPONo, String reqDeliveryDate, String shipNodeOrder, String sourceType, Element instructionsElement) {

		Document createOrderDoc = createDocument(XPXLiterals.E_ORDER);
		createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_DOCUMENT_TYPE, documentType);
		createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTERPRISE_CODE, enterpriseCode);
		createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE,buyerOrganizationCode);
		createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_TYPE,orderType);
		createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTRY_TYPE, entryType);
		createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_PO_NO,customerPONo);
		createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_REQ_DELIVERY_DATE,reqDeliveryDate);
		createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_SHIP_NODE,shipNodeOrder);
		//createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_SOURCE_TYPE,sourceType);

		importElement(createOrderDoc.getDocumentElement(),orderExtn);
		//importElement(createOrderDoc.getDocumentElement(), orderPriceInfo);
		
		Element orderPriceInfoElement = createOrderDoc.createElement(XPXLiterals.E_PRICE_INFO);
		orderPriceInfoElement.setAttribute(XPXLiterals.A_CURRENCY, orderPriceInfo.getAttribute(XPXLiterals.A_CURRENCY));
		createOrderDoc.getDocumentElement().appendChild(orderPriceInfoElement);
		
		importElement(personInfoShipTo,personInfoShipToExtn);
		importElement(createOrderDoc.getDocumentElement(),personInfoShipTo);
		importElement(personInfoBillTo,personInfoBillToExtn);
		importElement(createOrderDoc.getDocumentElement(),personInfoBillTo);
		importElement(createOrderDoc.getDocumentElement(),instructionsElement);


		Element orderLinesElement = createOrderDoc.createElement(XPXLiterals.E_ORDER_LINES);
		createOrderDoc.getDocumentElement().appendChild(orderLinesElement);

		for(int i=0; i<thirdPartyList.size(); i++)
		{
			Document orderLineDoc = (Document)thirdPartyList.get(i);			
			importElement(orderLinesElement,orderLineDoc.getDocumentElement());			

		}

		return createOrderDoc;
	}

	private Document createorderLineDoc(String orderedQty, String transactionalUOM, String itemId, String unitOfMeasure, String productClass,
			String documentType, String enterpriseCode, String orderNo, String primeLineNo, String subLineNo, Element orderLineExtn,
			Element itemExtn, Element linePriceInfo, String customerLinePONo, String orderLineCustomerPONo, String shipNodeOrderLine, String customerItem, Element instructions)
	{

		Document orderLineElementDoc = createDocument(XPXLiterals.E_ORDER_LINE);
		
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUST_LINE_PO_NO, customerLinePONo);
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_PO_NO,orderLineCustomerPONo);
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_SHIP_NODE,shipNodeOrderLine);
		
		importElement(orderLineElementDoc.getDocumentElement(),orderLineExtn);
		importElement(orderLineElementDoc.getDocumentElement(),linePriceInfo);
		importElement(orderLineElementDoc.getDocumentElement(),instructions);

		Element orderLineTranQtyElement = orderLineElementDoc.createElement(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY);
		orderLineTranQtyElement.setAttribute(XPXLiterals.A_ORDERED_QTY, orderedQty);
		orderLineTranQtyElement.setAttribute(XPXLiterals.A_TRANSACTIONAL_UOM, transactionalUOM);
		orderLineElementDoc.getDocumentElement().appendChild(orderLineTranQtyElement);

		Element itemElement = orderLineElementDoc.createElement(XPXLiterals.E_ITEM);
		itemElement.setAttribute(XPXLiterals.A_ITEM_ID, itemId);
		itemElement.setAttribute(XPXLiterals.A_UNIT_OF_MEASURE, unitOfMeasure);
		itemElement.setAttribute(XPXLiterals.A_PRODUCT_CLASS, productClass);
		itemElement.setAttribute(XPXLiterals.A_CUSTOMER_ITEM,customerItem);
		orderLineElementDoc.getDocumentElement().appendChild(itemElement);
		importElement(itemElement,itemExtn);

		Element chainedFromElement = orderLineElementDoc.createElement(XPXLiterals.E_CHAINED_FROM);
		chainedFromElement.setAttribute(XPXLiterals.A_DOCUMENT_TYPE, documentType);
		chainedFromElement.setAttribute(XPXLiterals.A_ENTERPRISE_CODE, enterpriseCode);
		chainedFromElement.setAttribute(XPXLiterals.A_ORDER_NO, orderNo);
		chainedFromElement.setAttribute(XPXLiterals.A_PRIME_LINE_NO, primeLineNo);
		chainedFromElement.setAttribute(XPXLiterals.A_SUB_LINE_NO, subLineNo);
		orderLineElementDoc.getDocumentElement().appendChild(chainedFromElement);

		return orderLineElementDoc;
	}

	private Document createDocument(String docElementTag) {

		Document doc = getNewDocument();
		Element ele = doc.createElement(docElementTag);
		doc.appendChild(ele);
		return doc;			

	}

	public static Document getNewDocument() {
		return new DocumentImpl();
	}
	public static Element importElement(Element parentEle,
			Element ele2beImported) {
		Element child = null;
		if (parentEle != null && ele2beImported != null) {
			child = (Element) parentEle.getOwnerDocument().importNode(
					ele2beImported, true);
			parentEle.appendChild(child);
		}
		return child;
	}

	public static void copyAttributes(Node toNode, Node fromNode)
	throws Exception
	{
	NamedNodeMap fromAttrbMap = fromNode.getAttributes();

	Element toElement = (Element)toNode;
	if (fromAttrbMap != null) {
	int fromAttrbMapLength = fromAttrbMap.getLength();

	for (int i = 0; i < fromAttrbMapLength; i++) {
	    Node attrbNode = fromAttrbMap.item(i);

	    if ((attrbNode == null)
	            || (attrbNode.getNodeType() != Node.ATTRIBUTE_NODE)) {
	        continue;
	    }

	    String attrbName = attrbNode.getNodeName();
	    String attrbVal = attrbNode.getNodeValue();

	    String toAttrbVal =  toElement.getAttribute(attrbName);

	    if (toAttrbVal.length() == 0 &&
	    		!attrbName.equalsIgnoreCase(XPXLiterals.A_ORDER_LINE_KEY) &&
	    		!attrbName.equalsIgnoreCase(XPXLiterals.A_ORDER_HEADER_KEY) &&
	    		!attrbName.equalsIgnoreCase(XPXLiterals.A_PIPELINE_KEY)) {
	    	toElement.setAttribute(attrbName, attrbVal);
	    }
	}}

	}

}
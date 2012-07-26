package com.xpedx.nextgen.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

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
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
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
	Properties props;

	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try  {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {
			e1.printStackTrace();
		}
	}

	public void setProperties(Properties props) throws Exception {
		this.props = props;
		
	}

	/**
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document checkItemType(YFSEnvironment env, Document inDoc) throws Exception {

		if (log.isDebugEnabled()) {
			log.debug("XPXcheckItemTypeAndCreateOrderAPI-InXML:" + SCXmlUtil.getString(inDoc));
		}

		Document thirdPartyOrderCreationOutputDoc = null;
		Document directOrderCreationOutputDoc = null;
		Document stockOrderCreationOutputDoc = null;
		Document shipToCustDoc = null;

        String orderLineShipNode = "";
        String allowDirectOrderFlag = "";

		ArrayList thirdPartyList = new ArrayList();
		ArrayList directOrderList = new ArrayList();
		ArrayList stockOrderList = new ArrayList();
		
		HashMap orderElementContents = new HashMap();
		HashMap orderLineElementContents = new HashMap();
		
		//Need to split the Order Level Total , discount and Subtotal
		double thirPartyExtnOrderSubTotal = 0;
		double directExtnOrderSubTotal = 0;
		double stockExtnOrderSubTotal = 0;
				
		orderElementContents = createOrderElementHashMap(inDoc);

		Element rootElem = inDoc.getDocumentElement();
		
		shipToCustDoc = (Document) env.getTxnObject("ShipToCustomerProfile");
		if (shipToCustDoc == null) {
			String buyerOrgCode = rootElem.getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
			if(!YFCObject.isNull(buyerOrgCode) && !YFCObject.isVoid(buyerOrgCode)) {
				
		        Document getCustomerListInputDoc = YFCDocument.createDocument("Customer").getDocument();
		        getCustomerListInputDoc.getDocumentElement().setAttribute("CustomerID", buyerOrgCode);
		        
		        log.debug("The input to getCustomerList for shipTo is: "+SCXmlUtil.getString(getCustomerListInputDoc));	
		        env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);
		        shipToCustDoc = api.invoke(env,XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc);
		        log.debug("The output of getCustomerList for ShipTo is: "+SCXmlUtil.getString(shipToCustDoc));		   
			}
		}
		
		if (shipToCustDoc != null) {
			Element customerElement = (Element) shipToCustDoc.getDocumentElement().getElementsByTagName("Customer").item(0);
		    Element customerExtnElement = (Element) customerElement.getElementsByTagName("Extn").item(0);
		    
		    allowDirectOrderFlag = customerExtnElement.getAttribute("ExtnAllowDirectOrderFlag");
		}
		
	    if (log.isDebugEnabled()) {
	    	log.debug("Allow Direct Orders Flag Is: "+allowDirectOrderFlag);
	    }
	    
		Element orderLinesElem = (Element) rootElem.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);
		NodeList orderLineList = orderLinesElem.getElementsByTagName(XPXLiterals.E_ORDER_LINE);
		for(int i=0; i<orderLineList.getLength(); i++) {
			
			String orderLineType = "";
			Document orderLineElementDoc = null;
			Element orderLine = (Element)orderLineList.item(i);			
			String lineStatus = orderLine.getAttribute("Status");
		    if(!"Cancelled".equalsIgnoreCase(lineStatus)) {
		    	
		    	orderLineElementContents = createOrderLineElementHashMap(orderLine);
		    	Element orderLineExtn = (Element) orderLine.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

		    	orderLineType = orderLineExtn.getAttribute(XPXLiterals.A_EXTN_LINE_TYPE);
			
		    	String extnOrderLinetotatal = orderLineExtn.getAttribute(XPXLiterals.A_EXTN_LINE_ORDERED_TOTAL);
		    	double extnOrderLinetotatalVal=0;
		    	if(extnOrderLinetotatal != null) {
					extnOrderLinetotatalVal = extnOrderLinetotatalVal + Double.parseDouble(extnOrderLinetotatal);
		    	}
			
		    	orderLineShipNode = (String)orderLineElementContents.get(XPXLiterals.A_SHIP_NODE);
			
			
		    	// To Split Orders Only If AllowDirectOrderFlag is 'Y'
				
				if("Y".equalsIgnoreCase(allowDirectOrderFlag)) {
					// To Create Chained or Fulfillment Orders Based On Order Line Type.
					if(orderLineType.equalsIgnoreCase(XPXLiterals.STOCK)) {
						// Stock Type Line.
						orderLineElementDoc = createorderLineDoc(orderLineElementContents, orderElementContents);
						stockExtnOrderSubTotal +=extnOrderLinetotatalVal;
						stockOrderList.add(orderLineElementDoc);
					} else if(orderLineType.equalsIgnoreCase(XPXLiterals.DIRECT)) {
						// Direct Type Line.
						orderLineElementDoc = createorderLineDoc(orderLineElementContents, orderElementContents);
						directExtnOrderSubTotal +=extnOrderLinetotatalVal;
						directOrderList.add(orderLineElementDoc);
					} else if(orderLineType.equalsIgnoreCase(XPXLiterals.SPECIAL)) {
						orderLineElementDoc = createorderLineDoc(orderLineElementContents,orderElementContents);
						thirPartyExtnOrderSubTotal +=extnOrderLinetotatalVal;		
						thirdPartyList.add(orderLineElementDoc);
					} else {
						// Do Nothing.
					}
				} else {
					if(orderLineType.trim().length() > 0) { 	
					    orderLineElementDoc = createorderLineDoc(orderLineElementContents, orderElementContents);
					    stockExtnOrderSubTotal +=extnOrderLinetotatalVal;
					    stockOrderList.add(orderLineElementDoc);
					}
				}	
		    }
		}

		boolean isDiscountApplied=false;	
		if(stockOrderList.size() > 0) {
			
			Element orderExtn = (Element)orderElementContents.get(XPXLiterals.ORDER_EXTN);		
			setOrderExtnFields(orderExtn, isDiscountApplied, stockExtnOrderSubTotal);
			isDiscountApplied=true;
			orderExtn.setAttribute("ExtnLegacyOrderType", "S");
			
			Document chainedOrderInput_STOCK = createChainedOrderInput(stockOrderList,orderElementContents,XPXLiterals.STOCK_ORDER,orderLineShipNode,orderExtn);
			stockOrderCreationOutputDoc = api.executeFlow(env,XPXLiterals.LEGACY_ORDER_CREATION_SERVICE, chainedOrderInput_STOCK);
		}
		
		if(directOrderList.size() > 0) {
			
			Element orderExtn = (Element)orderElementContents.get(XPXLiterals.ORDER_EXTN);
			setOrderExtnFields(orderExtn ,isDiscountApplied,directExtnOrderSubTotal );
			isDiscountApplied=true;
			orderExtn.setAttribute("ExtnLegacyOrderType", "D");
			
			Document chainedOrderInput_DIRECT = createChainedOrderInput(directOrderList,orderElementContents,XPXLiterals.DIRECT_ORDER,orderLineShipNode,orderExtn);
			directOrderCreationOutputDoc = api.executeFlow(env,XPXLiterals.LEGACY_ORDER_CREATION_SERVICE, chainedOrderInput_DIRECT);
		}		
		
		if (thirdPartyList.size() > 0) {
			
			Element orderExtn = (Element)orderElementContents.get(XPXLiterals.ORDER_EXTN);
			setOrderExtnFields(orderExtn ,isDiscountApplied,thirPartyExtnOrderSubTotal );
			isDiscountApplied=true;
			
			Document chainedOrderInput_SPECIAL = createChainedOrderInput(thirdPartyList,orderElementContents,XPXLiterals.SPECIAL_ORDER,orderLineShipNode,orderExtn);    
			thirdPartyOrderCreationOutputDoc = api.executeFlow(env, XPXLiterals.LEGACY_ORDER_CREATION_SERVICE, chainedOrderInput_SPECIAL);
		}
		return inDoc;
	}


	private void setOrderExtnFields(Element orderExtn ,boolean isDiscountApplied, double extnOrderSubTotal )
	{
		if(!isDiscountApplied) {
			String extnTotAdjustments=orderExtn.getAttribute(XPXLiterals.A_EXTN_TOT_ORDER_ADJUSTMENTS);
			orderExtn.setAttribute(XPXLiterals.A_EXTN_ORDER_SUB_TOTAL, ""+extnOrderSubTotal);
			extnOrderSubTotal =extnOrderSubTotal + (extnTotAdjustments != null ? Double.parseDouble(extnTotAdjustments) : 0);	
		} else {			
			orderExtn.setAttribute(XPXLiterals.A_EXTN_ORDER_DISCOUNT, "0");
			orderExtn.setAttribute(XPXLiterals.A_EXTN_ORDER_COUPON_DISCOUNT, "0");
			orderExtn.setAttribute(XPXLiterals.A_EXTN_TOT_ORDER_ADJUSTMENTS, "0");
			orderExtn.setAttribute(XPXLiterals.A_EXTN_ORDER_SUB_TOTAL, ""+extnOrderSubTotal);
		}
		orderExtn.setAttribute(XPXLiterals.A_EXTN_TOT_ORD_VAL_WITHOUT_TAXES, ""+extnOrderSubTotal);
		orderExtn.setAttribute(XPXLiterals.A_EXTN_TOTAL_ORDER_VALUE, ""+extnOrderSubTotal);		
	}
	
	private Document createorderLineDoc(HashMap orderLineElementContents, HashMap orderElementContents) {
		
        Document orderLineElementDoc = createDocument(XPXLiterals.E_ORDER_LINE);
		
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUST_LINE_PO_NO, (String)orderLineElementContents.get(XPXLiterals.A_CUST_LINE_PO_NO));
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_PO_NO,(String)orderLineElementContents.get(XPXLiterals.A_CUSTOMER_PO_NO));
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_SHIP_NODE,(String)orderLineElementContents.get(XPXLiterals.A_SHIP_NODE));
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_LINE_TYPE,(String)orderLineElementContents.get(XPXLiterals.A_LINE_TYPE));
			
		importElement(orderLineElementDoc.getDocumentElement(),(Element)orderLineElementContents.get(XPXLiterals.ORDER_LINE_EXTN));
		importElement(orderLineElementDoc.getDocumentElement(),(Element)orderLineElementContents.get(XPXLiterals.E_LINE_PRICE_INFO));
			
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

	private Document createChainedOrderInput(ArrayList thirdPartyList, HashMap orderElementContents, String lineType, String orderLineShipNode, Element orderExtn) {

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
				
			importElement(createOrderDoc.getDocumentElement(),orderExtn);
			
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

			for(int i=0; i<thirdPartyList.size(); i++) {
				Document orderLineDoc = (Document)thirdPartyList.get(i);			
				importElement(orderLinesElement,orderLineDoc.getDocumentElement());			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return createOrderDoc;	
	}

	private HashMap<String,Element> createOrderElementHashMap(Document inDoc) {
		
		HashMap<String,Element> orderElements = new HashMap<String,Element>();

		Element inputXMLRoot = inDoc.getDocumentElement();
		Element orderExtn = (Element) inputXMLRoot.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		Element orderPriceInfo = (Element) inputXMLRoot.getElementsByTagName(XPXLiterals.E_PRICE_INFO).item(0);		
		Element personInfoShipTo = (Element) inputXMLRoot.getElementsByTagName(XPXLiterals.E_PERSON_INFO_SHIP_TO).item(0);
		Element personInfoShipToExtn = (Element) personInfoShipTo.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		Element personInfoBillTo = (Element) inputXMLRoot.getElementsByTagName(XPXLiterals.E_PERSON_INFO_BILL_TO).item(0);
		Element personInfoBillToExtn = (Element) personInfoBillTo.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
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
			}
		}
	}

}
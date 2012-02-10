package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import java.util.Properties;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import com.yantra.ycp.core.YCPContext;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/** Desription: This class is invoked when there is any updation on an existing Customer order. If there are existing chained lines for this
 *              order, then these will also be updated accordingly or new chained orders will be created.
 * 
 * @author Prasanth Kumar M.
 *
 */

public class XPXUpdateOrderWithNewItemsAPI implements YIFCustomApi{


	private static YIFApi api = null;

	private static YFCLogCategory log;
	private static int newPrimeLineNo = 0;
	public static final String orderLineExtn_LT = "ORDER_LINE_EXTN";
	public static final String orderExtn_LT = "ORDER_EXTN";
	public static final String personInfoBillToExtn_LT = "PERSON_INFO_BILL_TO_EXTN";
	public static final String personInfoShipToExtn_LT = "PERSON_INFO_SHIP_TO_EXTN";

	String itemTemplate = "global/template/api/getItemList.CreateCustomerOrderService.xml";
	String orderLineListTemplate = "global/template/api/getOrderLineList.XPXUpdateCustomerOrderService.xml";
	String changeOrderTemplate = "global/template/api/changeOrder.XPXUpdateCustomerOrderService.xml";
	String getOrderListTemplate = "global/template/api/getOrderList.XPXUpdateCustomerOrderService.xml";
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXCreateChainedOrderService.xml";
	
	String customerItem = null;

	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");

        try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Document updateExistingOrder(YFSEnvironment env,Document changeOrderInputDoc) throws Exception
	{
		log.beginTimer("XPXUpdateOrderWithNewItemsAPI.updateExistingOrder");

		Document changeOrderOutputDoc = null;
		Document OrderLineListInputDoc = null;
		Document OrderLineListOutputDoc = null;
		Document chainedOrderInput_THIRD_PARTY = null;
		Document chainedOrderInput_DIRECT = null;
		Document chainedOrderInput_STOCK =null;
	    Document chainedOrderHeader_THIRDPARTY = null;
	    Document chainedOrderHeader_DIRECTORDER = null;
	    Document chainedOrderHeader_STOCKORDER = null;



		Element changeOrderOutputDocRoot = null;	
		Element orderLines = null;
		Element changeOrderInputDocExtn = null;
		Element orderPriceInfo = null;
		Element personInfoShipTo = null;
		Element personInfoShipToExtn = null;
		Element personInfoBillTo = null;
		Element personInfoBillToExtn = null;
		Element changeOrderInputDocOrderLineTranQuantity = null;
		Element lineTypeUpdateItemExtn = null;



		NodeList orderLineList = null; 

		ArrayList thirdPartyList = new ArrayList();
		ArrayList directOrderList = new ArrayList();
		ArrayList stockOrderList = new ArrayList(); 
		ArrayList orderLineElementsForChangeOrder = new ArrayList();
		ArrayList ordLineElemsForThirdPartyOrderQtyChange = new ArrayList();
		ArrayList ordLineElemsForDirectOrderQtyChange = new ArrayList();
		ArrayList ordLineElemsForStockOrderQtyChange = new ArrayList();
		ArrayList orderLineElementsForThirdPartyOrder = new ArrayList();
		ArrayList orderLineElementsForDirectOrder = new ArrayList();
		ArrayList orderLineElementsForStockOrder = new ArrayList();
		ArrayList orderLineInputOfExistingLineThirdParty = new ArrayList();
		ArrayList orderLineInputOfExistingLineDirectOrder = new ArrayList();
		ArrayList orderLineInputOfExistingLineStockOrder = new ArrayList();
		ArrayList changeOrderStatusOrderLineInput = new ArrayList();
		ArrayList lineTypeUpdateOrderLineElementsForThirdParty = new ArrayList();
		ArrayList lineTypeUpdateOrderLineElementsForDirectOrder = new ArrayList();
		ArrayList lineTypeUpdateOrderLineElementsForStockOrder = new ArrayList();
		ArrayList orderLineElementsForThirdPartyOrderLineTypeUpdate = new ArrayList();
		ArrayList orderLineElementsForDirectOrderLineTypeUpdate = new ArrayList();
		ArrayList orderLineElementsForStockOrdeLineTypeUpdate = new ArrayList();
        ArrayList orderLineInputOfExistingLineThirdPartyForOtherChanges =  new ArrayList();
        ArrayList orderLineInputOfExistingLineDirectOrderForOtherChanges = new ArrayList();
		ArrayList orderLineInputOfExistingLineStockOrderForOtherChanges = new ArrayList();
		

		String orderLineType = null;
		String changeOrderInputDocOHK = null;
		String changeOrderInputDocType = null;
		String changeOrderInputDocEntCode = null;
		String orderNo = null;
		String existingOrderedQty = null;
		String existingSubLineNo = null;
		String existingPrimeLineNo = null;
		String existingLineType = null;
		String buyerOrganizationCode = null;
		String transactionalUOM = null;
		String chainedOrderOLK = null;
		String lineTypeUpdateprimeLineNo = null;
		String lineTypeUpdateItemId = null;
		String lineTypeUpdateUOM = null;
		String lineTypeUpdateProductClass = null;
		String chainedOrderLineType = null;	
		String quantity = null;
		String transactionalOrderedQty = null;
		String entryType = null;
		String customer_branch = null;
        String environment_id = null;
        String company_code = null;
        String billToId = null;
        String legacyCustomerNo = null;
        

		MultiValueMap existingOrderQty = new MultiValueMap();
		HashMap chainedOrderLineKeys = new HashMap();
		HashMap chainedOHKAndType = new HashMap();
		HashMap createOrderInputForChainedOrders = new HashMap();
		HashMap changeOrderInputForChainedOrders = new HashMap();
		HashMap orderLineAttributes = new HashMap();

		int noOfExistingOrderLines = 0;


		
		

		//log.debug("The customer change order input doc is: "+TrialClass.getXMLString(changeOrderInputDoc));
		Element changeOrderInputDocRoot = changeOrderInputDoc.getDocumentElement();
		changeOrderInputDocOHK = changeOrderInputDocRoot.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
		buyerOrganizationCode = changeOrderInputDocRoot.getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
		entryType = changeOrderInputDocRoot.getAttribute(XPXLiterals.A_ENTRY_TYPE);
		customer_branch = changeOrderInputDocRoot.getAttribute(XPXLiterals.A_SHIP_NODE);
		billToId = changeOrderInputDocRoot.getAttribute(XPXLiterals.A_BILL_TO_ID);
		
		changeOrderInputDocExtn = (Element)changeOrderInputDocRoot.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		environment_id = changeOrderInputDocExtn.getAttribute(XPXLiterals.A_EXTN_ENVT_ID);
		company_code = changeOrderInputDocExtn.getAttribute(XPXLiterals.A_EXTN_COMPANY_CODE);
				
		orderPriceInfo = (Element) changeOrderInputDocRoot.getElementsByTagName(XPXLiterals.E_PRICE_INFO).item(0);

		personInfoShipTo = (Element) changeOrderInputDocRoot.getElementsByTagName(XPXLiterals.E_PERSON_INFO_SHIP_TO).item(0);
		if(personInfoShipTo!=null)
		{
			personInfoShipToExtn = (Element) personInfoShipTo.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		}
		personInfoBillTo = (Element) changeOrderInputDocRoot.getElementsByTagName(XPXLiterals.E_PERSON_INFO_BILL_TO).item(0);
		if(personInfoBillTo!=null)
		{
			personInfoBillToExtn = (Element) personInfoBillTo.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		}
		//Get the order details of the existing customer order to store the existing ordered qty of the lines
		Document getOrderListInputDoc = createGetOrderListInputDoc(changeOrderInputDocOHK);
		Document getOrderListOutputDoc = invokeGetOrderList(env,getOrderListInputDoc);
		existingOrderQty = getExistingOrderQty(getOrderListOutputDoc);
		noOfExistingOrderLines = getNoOfExistingOrderLines(getOrderListOutputDoc);
		log.debug("The number of existing orderlines in customer order is: "+noOfExistingOrderLines);
		newPrimeLineNo=noOfExistingOrderLines;

         

		//Invoking getOrderLineList to get the number of existing orderlines(chained order lines) before Customer order is updated
		Document getOrderLineListInputDoc = createOrderLineListInputDoc(changeOrderInputDocOHK);
		env.setApiTemplate(XPXLiterals.GET_ORDER_LINE_LIST_API, orderLineListTemplate);
		Document outputOrderLineListDoc = api.invoke(env,XPXLiterals.GET_ORDER_LINE_LIST_API, getOrderLineListInputDoc);
		env.clearApiTemplate(XPXLiterals.GET_ORDER_LINE_LIST_API);

		chainedOrderLineKeys = getChainedOrderLineKeys(outputOrderLineListDoc);
		chainedOHKAndType = getchainedOHKAndType(outputOrderLineListDoc);

		

		//Following code used to print out the hashmap contents above
		Set set1 = chainedOHKAndType.entrySet();

		    Iterator itr = set1.iterator();

		    while(itr.hasNext()){
		      Map.Entry me = (Map.Entry)itr.next();
		      //log.debug(me.getKey() + " : " + TrialClass.getXMLString((Document)me.getValue()) );
		    }
		 




		// Iterating through the Customer Change Order input doc
		changeOrderInputDocType = changeOrderInputDocRoot.getAttribute(XPXLiterals.A_DOCUMENT_TYPE);
		changeOrderInputDocEntCode = changeOrderInputDocRoot.getAttribute(XPXLiterals.A_ENTERPRISE_CODE);
		Element changeOrderInputDocOrderLines = (Element)changeOrderInputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);
		NodeList changeOrderInputDocOrderLineList = changeOrderInputDocOrderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);


		log.debug("The no of lines in the customer change order input doc is: "+changeOrderInputDocOrderLineList.getLength());



		//Stamping weblinenumber and linetype to the new lines 
		for(int i=0;i<changeOrderInputDocOrderLineList.getLength(); i++)
		{


			log.debug("Entered the For loop");
			Element changeOrderInputDocOrderLine = (Element)changeOrderInputDocOrderLineList.item(i);

			String changeOrderInputDocOrderLineKey = changeOrderInputDocOrderLine.getAttribute(XPXLiterals.A_ORDER_LINE_KEY);				
			String changeOrderInputDocOrderedQty = changeOrderInputDocOrderLine.getAttribute(XPXLiterals.A_ORDERED_QTY);
			String changeOrderInputDocOrderLineType = changeOrderInputDocOrderLine.getAttribute(XPXLiterals.A_LINE_TYPE);

			String  changeOrderInputDocPrimeLineNo = changeOrderInputDocOrderLine.getAttribute(XPXLiterals.A_PRIME_LINE_NO);

			Element orderLineExtn = (Element)changeOrderInputDocOrderLine.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

			Element linePriceInfo = (Element)changeOrderInputDocOrderLine.getElementsByTagName(XPXLiterals.E_LINE_PRICE_INFO).item(0);

			//Getting the OrderLineTranQuantity for retrieving Transactional UOM
			changeOrderInputDocOrderLineTranQuantity = (Element)changeOrderInputDocOrderLine.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
			if(changeOrderInputDocOrderLineTranQuantity!=null)
			{
				transactionalUOM = changeOrderInputDocOrderLineTranQuantity.getAttribute(XPXLiterals.A_TRANSACTIONAL_UOM);
				quantity = changeOrderInputDocOrderLineTranQuantity.getAttribute(XPXLiterals.A_QUANTITY);
				transactionalOrderedQty = changeOrderInputDocOrderLineTranQuantity.getAttribute(XPXLiterals.A_ORDERED_QTY);
			}

			if(changeOrderInputDocOrderLineKey != null && changeOrderInputDocOrderLineKey.trim().length() != 0)
			{
				orderLineAttributes = getOrderLineAttributes(getOrderListOutputDoc,changeOrderInputDocOrderLineKey);				
			}

			if(changeOrderInputDocOrderLineKey == null || changeOrderInputDocOrderLineKey.trim().length() == 0)
			{  // This means that this line is a new line

				log.debug("Inside the orderLineKey = null");
				
				newPrimeLineNo++;
				log.debug("The newPrimeLineNo is: "+newPrimeLineNo);
				//Get the item type from the orderline to get the line type
				Element item = (Element) changeOrderInputDocOrderLine.getElementsByTagName(XPXLiterals.E_ITEM).item(0);
				String itemId = item.getAttribute(XPXLiterals.A_ITEM_ID);
				String unitOfMeasure = item.getAttribute(XPXLiterals.A_UNIT_OF_MEASURE);
				String productClass = item.getAttribute(XPXLiterals.A_PRODUCT_CLASS);
				Element itemExtn = (Element) item.getElementsByTagName(XPXLiterals.E_EXTN).item(0);	    			

				Document getItemListInputDoc = createItemListInputDoc(itemId);
				
				Document getCustomerListInputDoc = createGetCustomerListInput(env,billToId);
				
				try 
				{
					env.setApiTemplate(XPXLiterals.GET_ITEM_LIST_API, itemTemplate);
					Document outputItemDoc = api.invoke(env,XPXLiterals.GET_ITEM_LIST_API, getItemListInputDoc);
					env.clearApiTemplate(XPXLiterals.GET_ITEM_LIST_API);
					
					
					env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate);					
					Document getCustomerListOutputDoc = api.invoke(env,XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc);				
				    env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
				    
				    
				    Element getCustomerListOutputRoot = getCustomerListOutputDoc.getDocumentElement();					
					Element customerElement = (Element)getCustomerListOutputRoot.getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);					
					Element customerExtnElement = (Element)customerElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);						
					legacyCustomerNo = customerExtnElement.getAttribute(XPXLiterals.A_EXTN_LEGACY_CUST_NO);					
					log.debug("The legacy customer no is: "+legacyCustomerNo);
					
                    //Querying XPX_ITEMCUST_XREF to get the Customer Part No.
					
					Document XREFOutputDoc = invokeXREF(env,itemId,legacyCustomerNo,environment_id,company_code,customer_branch);
					Element XREFOutputDocRoot = XREFOutputDoc.getDocumentElement();
					NodeList XREFCustElementList = XREFOutputDocRoot.getElementsByTagName(XPXLiterals.E_XPX_ITEM_CUST_XREF);
					if(XREFCustElementList.getLength() > 0)
					{
						for(int k=0;k<XREFCustElementList.getLength();k++)
						{
						        Element XREFElement = (Element) XREFCustElementList.item(k);
						        customerItem = XREFElement.getAttribute(XPXLiterals.A_CUSTOMER_PART_NO);
						        						        
						}
					}

					//Storing item type as line type
					orderLineType = getOrderLineType(outputItemDoc);
					changeOrderInputDocOrderLine.setAttribute(XPXLiterals.A_LINE_TYPE, orderLineType);

					//Getting the next unique sequence number from the custom sequence created
					long uniqueSequenceNo = getNextDBSequenceNo(env, XPXLiterals.WEB_LINE_SEQUENCE);

					String webLineNumber = generateWebLineNumber(uniqueSequenceNo);
					if(orderLineExtn != null)
					{
						orderLineExtn.setAttribute(XPXLiterals.A_WEB_LINE_NUMBER,webLineNumber);
					}
					else
					{
						//Adding the Extn element at LineLevel to store the Web Line number    
						orderLineExtn = changeOrderInputDoc.createElement(XPXLiterals.E_EXTN);
						orderLineExtn.setAttribute(XPXLiterals.A_WEB_LINE_NUMBER,webLineNumber);
						changeOrderInputDocOrderLine.appendChild(orderLineExtn);
					}

					boolean doesOrderTypeExist = checkOrderTypeExistsBeforeChangeOrder(outputOrderLineListDoc,orderLineType);
					log.debug("The line type "+orderLineType+"existing is: "+doesOrderTypeExist);
					if(doesOrderTypeExist)
					{	    
						//Line type already exists indicates that chained order already exists hence appending the new line
						Document updateChainedOrderLineDoc = createOrderLineInputForChangeOrder(orderLineType,transactionalOrderedQty,
								orderLineExtn, itemId, unitOfMeasure, productClass, changeOrderInputDocType, changeOrderInputDocEntCode,
								changeOrderInputDocOHK, String.valueOf(newPrimeLineNo), XPXLiterals.CONSTANT_1, transactionalUOM);

						if(orderLineType.equalsIgnoreCase(XPXLiterals.THIRD_PARTY_ORDER))
						{	    					
							orderLineElementsForThirdPartyOrder.add(updateChainedOrderLineDoc);
						}
						if(orderLineType.equalsIgnoreCase(XPXLiterals.DIRECT_ORDER))
						{
							orderLineElementsForDirectOrder.add(updateChainedOrderLineDoc);	
						}
						if(orderLineType.equalsIgnoreCase(XPXLiterals.STOCK_ORDER))
						{
							orderLineElementsForStockOrder.add(updateChainedOrderLineDoc);	
						}

					}
					else
					{
						//Here there is no chained order so forming the createOrder xml for the same and storing them in a hashmap

						if(orderLineType.equalsIgnoreCase(XPXLiterals.THIRD_PARTY_ORDER))
						{
							Document orderLineElementDoc = createorderLineDoc(transactionalOrderedQty,transactionalUOM,itemId,
									unitOfMeasure,productClass,changeOrderInputDocType,changeOrderInputDocEntCode,changeOrderInputDocOHK,String.valueOf(newPrimeLineNo),
									XPXLiterals.CONSTANT_1,orderLineExtn,itemExtn,linePriceInfo,XPXLiterals.THIRD_PARTY_ORDER);

							thirdPartyList.add(orderLineElementDoc);
						}
						else if(orderLineType.equalsIgnoreCase(XPXLiterals.DIRECT_ORDER))

						{
							log.debug("Inside else if at line 307");
							Document orderLineElementDoc = createorderLineDoc(transactionalOrderedQty,transactionalUOM,itemId,
									unitOfMeasure,productClass,changeOrderInputDocType,changeOrderInputDocEntCode,changeOrderInputDocOHK,String.valueOf(newPrimeLineNo),
									XPXLiterals.CONSTANT_1,orderLineExtn,itemExtn,linePriceInfo,XPXLiterals.DIRECT_ORDER);

							directOrderList.add(orderLineElementDoc);
						}

						else 
						{
							Document orderLineElementDoc = createorderLineDoc(transactionalOrderedQty,transactionalUOM,itemId,
									unitOfMeasure,productClass,changeOrderInputDocType,changeOrderInputDocEntCode,changeOrderInputDocOHK,String.valueOf(newPrimeLineNo),
									XPXLiterals.CONSTANT_1,orderLineExtn,itemExtn,linePriceInfo,XPXLiterals.STOCK_ORDER);

							stockOrderList.add(orderLineElementDoc);
						}

						if (thirdPartyList.size()>0 && orderLineType.equalsIgnoreCase(XPXLiterals.THIRD_PARTY_ORDER))
						{

							chainedOrderInput_THIRD_PARTY = createChainedOrderInput(thirdPartyList,changeOrderInputDocType,
									changeOrderInputDocEntCode,	buyerOrganizationCode,XPXLiterals.THIRD_PARTY_ORDER,changeOrderInputDocExtn,personInfoShipTo,personInfoBillTo,orderPriceInfo,
									personInfoShipToExtn,personInfoBillToExtn);

							if(!createOrderInputForChainedOrders.containsKey(XPXLiterals.THIRD_PARTY_ORDER))
							{
								createOrderInputForChainedOrders.put(XPXLiterals.THIRD_PARTY_ORDER,chainedOrderInput_THIRD_PARTY);
							}
							else if (createOrderInputForChainedOrders.containsKey(XPXLiterals.THIRD_PARTY_ORDER))
							{
								Document createOrderExistingDoc = (Document)createOrderInputForChainedOrders.get(XPXLiterals.THIRD_PARTY_ORDER);

								Document createOrderUpdatedDoc = updateCreateOrderExistingDoc(createOrderExistingDoc,thirdPartyList);

								createOrderInputForChainedOrders.remove(XPXLiterals.THIRD_PARTY_ORDER);//Remove the existing entry to add the updated one

								createOrderInputForChainedOrders.put(XPXLiterals.THIRD_PARTY_ORDER, createOrderUpdatedDoc);

							}
							/*log.debug("The Third party Chained Order create Order xml: "+TrialClass.getXMLString(chainedOrderInput_THIRD_PARTY));
    	    						api.invoke(env,XPXLiterals.CREATE_ORDER_API, chainedOrderInput_THIRD_PARTY);*/

						}

						if(directOrderList.size() > 0 && orderLineType.equalsIgnoreCase(XPXLiterals.DIRECT_ORDER))
						{
							for(int j=0; j<directOrderList.size(); j++)
							{
								Document orderLineDoc = (Document)directOrderList.get(j);	
								//log.debug("The direct order line input doc is: "+TrialClass.getXMLString(orderLineDoc));
							}
							chainedOrderInput_DIRECT = createChainedOrderInput(directOrderList,changeOrderInputDocType,
									changeOrderInputDocEntCode,buyerOrganizationCode,XPXLiterals.DIRECT_ORDER,changeOrderInputDocExtn,personInfoShipTo,personInfoBillTo,orderPriceInfo,
									personInfoShipToExtn,personInfoBillToExtn);

							//log.debug("The chaineOrderInput_DIRECT doc is: "+TrialClass.getXMLString(chainedOrderInput_DIRECT));

							if(!createOrderInputForChainedOrders.containsKey(XPXLiterals.DIRECT_ORDER))
							{
								log.debug("Inside if");
								createOrderInputForChainedOrders.put(XPXLiterals.DIRECT_ORDER,chainedOrderInput_DIRECT);
							}
							else if(createOrderInputForChainedOrders.containsKey(XPXLiterals.DIRECT_ORDER))
							{
								Document createOrderExistingDoc = (Document)createOrderInputForChainedOrders.get(XPXLiterals.DIRECT_ORDER);

								Document createOrderUpdatedDoc = updateCreateOrderExistingDoc(createOrderExistingDoc,directOrderList);

								createOrderInputForChainedOrders.remove(XPXLiterals.DIRECT_ORDER);//Remove the existing entry to add the updated one

								createOrderInputForChainedOrders.put(XPXLiterals.DIRECT_ORDER, createOrderUpdatedDoc);

							}   

							/*log.debug("The direct order Chained Order create Order xml: "+TrialClass.getXMLString(chainedOrderInput_DIRECT));
    	    					api.invoke(env,XPXLiterals.CREATE_ORDER_API, chainedOrderInput_DIRECT);*/
						}

						if(stockOrderList.size() > 0 && orderLineType.equalsIgnoreCase(XPXLiterals.STOCK_ORDER))
						{
							chainedOrderInput_STOCK = createChainedOrderInput(stockOrderList,changeOrderInputDocType,changeOrderInputDocEntCode,
									buyerOrganizationCode,XPXLiterals.STOCK_ORDER, changeOrderInputDocExtn,personInfoShipTo,personInfoBillTo,orderPriceInfo,
									personInfoShipToExtn,personInfoBillToExtn);

							if(!createOrderInputForChainedOrders.containsKey(XPXLiterals.STOCK_ORDER))
							{
								createOrderInputForChainedOrders.put(XPXLiterals.STOCK_ORDER,chainedOrderInput_STOCK);
							}
							else if (createOrderInputForChainedOrders.containsKey(XPXLiterals.STOCK_ORDER))
							{
								Document createOrderExistingDoc = (Document)createOrderInputForChainedOrders.get(XPXLiterals.STOCK_ORDER);

								Document createOrderUpdatedDoc = updateCreateOrderExistingDoc(createOrderExistingDoc,stockOrderList);

								createOrderInputForChainedOrders.remove(XPXLiterals.STOCK_ORDER);//Remove the existing entry to add the updated one

								createOrderInputForChainedOrders.put(XPXLiterals.STOCK_ORDER, createOrderUpdatedDoc);

							}

							//api.invoke(env,XPXLiterals.CREATE_ORDER_API, chainedOrderInput_STOCK);
						}
					}


				}

				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else// These are existing lines for which we check the orderedQty and LineType change as well
			{

				if (existingOrderQty.containsKey(changeOrderInputDocOrderLineKey))
				{
					ArrayList arr = (ArrayList) existingOrderQty.getCollection(changeOrderInputDocOrderLineKey);

					for(int j=0; j <arr.size();j++)
					{
						if( j==0)
						{
							existingOrderedQty = (String)arr.get(j);
						}
						if( j==1 )
						{
							existingPrimeLineNo = (String)arr.get(j);
						}
						if(j ==2)
						{
							existingSubLineNo = (String)arr.get(j);
						}
						if( j==3 )
						{
							existingLineType = (String)arr.get(j);	
							log.debug("The existing line type in the arraylist is: "+ existingLineType);
						}
					}    					
				}

				//Checking if there is a difference in the order quantities of the existing lines or if there is a line type change
				if(changeOrderInputDocOrderedQty != null && changeOrderInputDocOrderedQty.trim().length() !=0)
				{
					if(Float.parseFloat(changeOrderInputDocOrderedQty) > Float.parseFloat(existingOrderedQty))


					{
						log.debug("Inside the OrderedQty increase");

						if(chainedOrderLineKeys.containsKey(changeOrderInputDocOrderLineKey))
						{

							Element getOrderLineDocElement = (Element) chainedOrderLineKeys.get(changeOrderInputDocOrderLineKey);

							chainedOrderOLK = getOrderLineDocElement.getAttribute(XPXLiterals.A_ORDER_LINE_KEY);

							chainedOrderLineType = getOrderLineDocElement.getAttribute(XPXLiterals.A_LINE_TYPE);

							log.debug("The chained order line key is: "+chainedOrderOLK);
						}

						//Create changeOrder input to stamp the new orderedQty on Chained Line[Product case created]   						
						Document orderLineInputForChangeOrder = createOrderLineInput(changeOrderInputDocType,
								changeOrderInputDocEntCode,chainedOrderOLK,changeOrderInputDocOrderedQty,existingPrimeLineNo,
								existingSubLineNo,changeOrderInputDocOHK);

						if(chainedOrderLineType.equalsIgnoreCase(XPXLiterals.THIRD_PARTY_ORDER))
						{
							ordLineElemsForThirdPartyOrderQtyChange.add(orderLineInputForChangeOrder);
						}
						if(chainedOrderLineType.equalsIgnoreCase(XPXLiterals.DIRECT_ORDER))
						{
							ordLineElemsForDirectOrderQtyChange.add(orderLineInputForChangeOrder);
						}
						if(chainedOrderLineType.equalsIgnoreCase(XPXLiterals.STOCK_ORDER))
						{
							ordLineElemsForStockOrderQtyChange.add(orderLineInputForChangeOrder);
						}
					}
				}
				else if(!changeOrderInputDocOrderLineType.equalsIgnoreCase(existingLineType) 
						&& changeOrderInputDocOrderLineType != null && changeOrderInputDocOrderLineType.trim().length() != 0)	
				{
					
					log.debug("Inside the Line type change loop");
					// Line type change on the orderline

					if(chainedOrderLineKeys.containsKey(changeOrderInputDocOrderLineKey))
					{

						Element getOrderLineDocElement = (Element) chainedOrderLineKeys.get(changeOrderInputDocOrderLineKey);

						chainedOrderOLK = getOrderLineDocElement.getAttribute(XPXLiterals.A_ORDER_LINE_KEY);

						chainedOrderLineType = getOrderLineDocElement.getAttribute(XPXLiterals.A_LINE_TYPE);


						log.debug("The chained order line key is: "+chainedOrderOLK);
					}


					//Creating the change order xml to nullify qty on chained order so as to cancel the line

					Document orderLineInputForChangeOrder = createOrderLineInputToNullifyOrderQty(changeOrderInputDocType,
							changeOrderInputDocEntCode,chainedOrderOLK,XPXLiterals.ORDERED_QTY_ZERO);
					
				

					log.debug("The existing line type is: "+existingLineType);			

					if(existingLineType.equalsIgnoreCase(XPXLiterals.THIRD_PARTY_ORDER))
					{
						orderLineInputOfExistingLineThirdParty.add(orderLineInputForChangeOrder);
					}
					if(existingLineType.equalsIgnoreCase(XPXLiterals.DIRECT_ORDER))
					{
						orderLineInputOfExistingLineDirectOrder.add(orderLineInputForChangeOrder); 
					}
					if(existingLineType.equalsIgnoreCase(XPXLiterals.STOCK_ORDER))
					{
						orderLineInputOfExistingLineStockOrder.add(orderLineInputForChangeOrder);   
					}



					// Creating changeOrder status i/p xml's OrderLine element to move from Unscheduled to Created status

					Document orderLineInputForChangeOrderStatus = createOrderLineInputForChangeOrderStatus(changeOrderInputDocOrderLineKey,
							existingPrimeLineNo,transactionalUOM,transactionalOrderedQty);

					changeOrderStatusOrderLineInput.add(orderLineInputForChangeOrderStatus);



					/* for(int j=0; j<orderLineAttributes.size(); j++)
							   {
								   if(j == 0)
								   {
								   lineTypeUpdateprimeLineNo =  (String)orderLineAttributes.get(j);
								   }
								   if(j == 1)
								   {
								   lineTypeUpdateItemId =  (String)orderLineAttributes.get(j);
								   }
								   if(j == 2)
								   {
								   lineTypeUpdateUOM   = (String)orderLineAttributes.get(j);
								   }
								   if(j == 3)
								   {
								   lineTypeUpdateProductClass  = (String)orderLineAttributes.get(j);
								   }
								   if(j == 4)
								   {
								   lineTypeUpdateItemExtn = (Element)orderLineAttributes.get(j);
								   }
							   }*/

					if(createOrderInputForChainedOrders.containsKey(changeOrderInputDocOrderLineType) &&
							!chainedOHKAndType.containsKey(changeOrderInputDocOrderLineType))
					{
						// Update the existing chained order creation xml and update the hashmap with the new create order xml								   

                        log.debug("Updating the existing chained order creation xml");
						Document orderLineElementDoc = createOrderLineInputForChangeOrder(changeOrderInputDocOrderLineType,transactionalOrderedQty,
								orderLineExtn,(String)orderLineAttributes.get(XPXLiterals.A_ITEM_ID), transactionalUOM,
								(String)orderLineAttributes.get(XPXLiterals.A_PRODUCT_CLASS), changeOrderInputDocType, changeOrderInputDocEntCode,
								changeOrderInputDocOHK, (String)orderLineAttributes.get(XPXLiterals.A_PRIME_LINE_NO), XPXLiterals.CONSTANT_1, transactionalUOM);

						Document existingCreateOrderDocument = (Document)createOrderInputForChainedOrders.get(changeOrderInputDocOrderLineType);

						Element existingOrderLinesElement = (Element)existingCreateOrderDocument.getDocumentElement()
						.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);

						importElement(existingOrderLinesElement,orderLineElementDoc.getDocumentElement());

						createOrderInputForChainedOrders.remove(changeOrderInputDocOrderLineType);

						createOrderInputForChainedOrders.put(changeOrderInputDocOrderLineType, existingCreateOrderDocument);

					}

					else if(!createOrderInputForChainedOrders.containsKey(changeOrderInputDocOrderLineType)
							&& !chainedOHKAndType.containsKey(changeOrderInputDocOrderLineType))
					{
						// Creation of a new chained order
                        log.debug("The new chained order has to be created");
						
						if(changeOrderInputDocOrderLineType.equalsIgnoreCase(XPXLiterals.THIRD_PARTY_ORDER))

						{//Assuming that the order line extn from change order i/p xml will have the existing and the new extn attributes
						//So not retrieving it from orderLineAttributes hashmap and also these will be populated if they are not changed
							Document orderLineElementDoc = createorderLineDoc(transactionalOrderedQty,
									transactionalUOM,(String)orderLineAttributes.get(XPXLiterals.A_ITEM_ID),
									(String)orderLineAttributes.get(XPXLiterals.A_UNIT_OF_MEASURE),
									(String)orderLineAttributes.get(XPXLiterals.A_PRODUCT_CLASS),changeOrderInputDocType,
									changeOrderInputDocEntCode,changeOrderInputDocOHK,(String)orderLineAttributes.get(XPXLiterals.A_PRIME_LINE_NO),
									XPXLiterals.CONSTANT_1,orderLineExtn,
									(Element)orderLineAttributes.get(XPXLiterals.E_EXTN),linePriceInfo,
									XPXLiterals.THIRD_PARTY_ORDER);

							lineTypeUpdateOrderLineElementsForThirdParty.add(orderLineElementDoc);
						}

						if(changeOrderInputDocOrderLineType.equalsIgnoreCase(XPXLiterals.DIRECT_ORDER))

						{
							Document orderLineElementDoc = createorderLineDoc(transactionalOrderedQty,
									transactionalUOM,(String)orderLineAttributes.get(XPXLiterals.A_ITEM_ID),
									(String)orderLineAttributes.get(XPXLiterals.A_UNIT_OF_MEASURE),
									(String)orderLineAttributes.get(XPXLiterals.A_PRODUCT_CLASS),changeOrderInputDocType,
									changeOrderInputDocEntCode,changeOrderInputDocOHK,(String)orderLineAttributes.get(XPXLiterals.A_PRIME_LINE_NO),
									XPXLiterals.CONSTANT_1,orderLineExtn,
									(Element)orderLineAttributes.get(XPXLiterals.E_EXTN),linePriceInfo,XPXLiterals.DIRECT_ORDER);

							lineTypeUpdateOrderLineElementsForDirectOrder.add(orderLineElementDoc);
						}

						if(changeOrderInputDocOrderLineType.equalsIgnoreCase(XPXLiterals.STOCK_ORDER))

						{
							Document orderLineElementDoc = createorderLineDoc(transactionalOrderedQty,
									transactionalUOM,(String)orderLineAttributes.get(XPXLiterals.A_ITEM_ID),
									(String)orderLineAttributes.get(XPXLiterals.A_UNIT_OF_MEASURE),
									(String)orderLineAttributes.get(XPXLiterals.A_PRODUCT_CLASS),changeOrderInputDocType,
									changeOrderInputDocEntCode,changeOrderInputDocOHK,(String)orderLineAttributes.get(XPXLiterals.A_PRIME_LINE_NO),
									XPXLiterals.CONSTANT_1,orderLineExtn,
									(Element)orderLineAttributes.get(XPXLiterals.E_EXTN),linePriceInfo,XPXLiterals.STOCK_ORDER);

							lineTypeUpdateOrderLineElementsForStockOrder.add(orderLineElementDoc);
						}


						if (lineTypeUpdateOrderLineElementsForThirdParty.size() > 0 && changeOrderInputDocOrderLineType.equalsIgnoreCase(XPXLiterals.THIRD_PARTY_ORDER))
						{
                            //Not changing method signature as the elements might need to be added at a later stage. As of now, we are going
							// with logic of stamping the order level elements after the change order on the Customer order
							
							chainedOrderInput_THIRD_PARTY = createChainedOrderInput(lineTypeUpdateOrderLineElementsForThirdParty,
									changeOrderInputDocType,changeOrderInputDocEntCode,buyerOrganizationCode,XPXLiterals.THIRD_PARTY_ORDER,
									(Element)orderLineAttributes.get(orderExtn_LT),(Element)orderLineAttributes.get(XPXLiterals.E_PERSON_INFO_SHIP_TO)
									,(Element)orderLineAttributes.get(XPXLiterals.E_PERSON_INFO_BILL_TO),
									(Element)orderLineAttributes.get(XPXLiterals.E_PRICE_INFO),
									(Element)orderLineAttributes.get(personInfoShipToExtn_LT),(Element)orderLineAttributes.get(personInfoBillToExtn_LT));

							createOrderInputForChainedOrders.put(XPXLiterals.THIRD_PARTY_ORDER,chainedOrderInput_THIRD_PARTY);

						}

						if (lineTypeUpdateOrderLineElementsForDirectOrder.size() > 0 && changeOrderInputDocOrderLineType.equalsIgnoreCase(XPXLiterals.DIRECT_ORDER))
						{
							chainedOrderInput_DIRECT = createChainedOrderInput(lineTypeUpdateOrderLineElementsForDirectOrder,
									changeOrderInputDocType,changeOrderInputDocEntCode,buyerOrganizationCode,XPXLiterals.DIRECT_ORDER,
									(Element)orderLineAttributes.get(orderExtn_LT),(Element)orderLineAttributes.get(XPXLiterals.E_PERSON_INFO_SHIP_TO)
									,(Element)orderLineAttributes.get(XPXLiterals.E_PERSON_INFO_BILL_TO),
									(Element)orderLineAttributes.get(XPXLiterals.E_PRICE_INFO),
									(Element)orderLineAttributes.get(personInfoShipToExtn_LT),(Element)orderLineAttributes.get(personInfoBillToExtn_LT));

							createOrderInputForChainedOrders.put(XPXLiterals.DIRECT_ORDER,chainedOrderInput_DIRECT);

						}

						if (lineTypeUpdateOrderLineElementsForStockOrder.size() > 0 && changeOrderInputDocOrderLineType.equalsIgnoreCase(XPXLiterals.STOCK_ORDER))
						{
							chainedOrderInput_STOCK = createChainedOrderInput(lineTypeUpdateOrderLineElementsForStockOrder,
									changeOrderInputDocType,changeOrderInputDocEntCode,buyerOrganizationCode,XPXLiterals.STOCK_ORDER,
									(Element)orderLineAttributes.get(orderExtn_LT),(Element)orderLineAttributes.get(XPXLiterals.E_PERSON_INFO_SHIP_TO)
									,(Element)orderLineAttributes.get(XPXLiterals.E_PERSON_INFO_BILL_TO),
									(Element)orderLineAttributes.get(XPXLiterals.E_PRICE_INFO),
									(Element)orderLineAttributes.get(personInfoShipToExtn_LT),(Element)orderLineAttributes.get(personInfoBillToExtn_LT));

							createOrderInputForChainedOrders.put(XPXLiterals.STOCK_ORDER,chainedOrderInput_STOCK);

						}

					}

					else if(chainedOHKAndType.containsKey(changeOrderInputDocOrderLineType))
					{
						// Updating the change order xml for the chained orders with the new line type

						log.debug("The existing prime line no is: "+(String)orderLineAttributes.get(XPXLiterals.A_PRIME_LINE_NO));

						Document updateChainedOrderLineDoc = createOrderLineInputForChangeOrder(changeOrderInputDocOrderLineType,transactionalOrderedQty,
								orderLineExtn,(String)orderLineAttributes.get(XPXLiterals.A_ITEM_ID), (String)orderLineAttributes.get(XPXLiterals.A_UNIT_OF_MEASURE),
								(String)orderLineAttributes.get(XPXLiterals.A_PRODUCT_CLASS), changeOrderInputDocType, changeOrderInputDocEntCode,
								changeOrderInputDocOHK, (String)orderLineAttributes.get(XPXLiterals.A_PRIME_LINE_NO), XPXLiterals.CONSTANT_1, transactionalUOM);

						//log.debug("The updateChainedOrderLine Doc is: "+TrialClass.getXMLString(updateChainedOrderLineDoc));

						if(changeOrderInputDocOrderLineType.equalsIgnoreCase(XPXLiterals.THIRD_PARTY_ORDER))
						{	    					
							orderLineElementsForThirdPartyOrderLineTypeUpdate.add(updateChainedOrderLineDoc);
						}
						if(changeOrderInputDocOrderLineType.equalsIgnoreCase(XPXLiterals.DIRECT_ORDER))
						{
							orderLineElementsForDirectOrderLineTypeUpdate.add(updateChainedOrderLineDoc);	
						}
						if(changeOrderInputDocOrderLineType.equalsIgnoreCase(XPXLiterals.STOCK_ORDER))
						{
							orderLineElementsForStockOrdeLineTypeUpdate.add(updateChainedOrderLineDoc);	
						}
					}

				}

				else// No line type and ordered qty change, its someother fields that change
				{
					log.debug("Inside the default change loop");
					// Retrieving the chained order lien keys
					
					if(chainedOrderLineKeys.containsKey(changeOrderInputDocOrderLineKey))
					{

						Element getOrderLineDocElement = (Element) chainedOrderLineKeys.get(changeOrderInputDocOrderLineKey);

						chainedOrderOLK = getOrderLineDocElement.getAttribute(XPXLiterals.A_ORDER_LINE_KEY);

						chainedOrderLineType = getOrderLineDocElement.getAttribute(XPXLiterals.A_LINE_TYPE);

						log.debug("The chained order line key is: "+chainedOrderOLK);
					}
					
					
					/*Document changeOrderLineInputDocument = createorderLineDoc(transactionalOrderedQty,
							transactionalUOM,(String)orderLineAttributes.get(XPXLiterals.A_ITEM_ID),
							(String)orderLineAttributes.get(XPXLiterals.A_UNIT_OF_MEASURE),
							(String)orderLineAttributes.get(XPXLiterals.A_PRODUCT_CLASS),changeOrderInputDocType,
							changeOrderInputDocEntCode,changeOrderInputDocOHK,(String)orderLineAttributes.get(XPXLiterals.A_PRIME_LINE_NO),
							XPXLiterals.CONSTANT_1,orderLineExtn,
							(Element)orderLineAttributes.get(XPXLiterals.E_EXTN),linePriceInfo,chainedOrderLineType);*///Line type is not reqd here
					
					/*Element chainedFromElement = (Element)changeOrderLineInputDocument.getDocumentElement().getElementsByTagName(XPXLiterals.E_CHAINED_FROM).item(0);
					changeOrderLineInputDocument.getDocumentElement().removeChild(chainedFromElement);*/
					
					
					
					
					Document changeOrderLineInputDocument = createDefaultChangeOrderLineDoc(changeOrderInputDocOrderLine,chainedOrderOLK);
					
					
					if(existingLineType.equalsIgnoreCase(XPXLiterals.THIRD_PARTY_ORDER))
					{
						orderLineInputOfExistingLineThirdPartyForOtherChanges.add(changeOrderLineInputDocument);
					}
					if(existingLineType.equalsIgnoreCase(XPXLiterals.DIRECT_ORDER))
					{
						orderLineInputOfExistingLineDirectOrderForOtherChanges.add(changeOrderLineInputDocument); 
					}
					if(existingLineType.equalsIgnoreCase(XPXLiterals.STOCK_ORDER))
					{
						orderLineInputOfExistingLineStockOrderForOtherChanges.add(changeOrderLineInputDocument);   
					}
					
					
					
				}




			}

		}	

		
		//Printing the hashMap
		
		
		Set set2 = chainedOHKAndType.entrySet();

	    Iterator itr2 = set2.iterator();

	    while(itr2.hasNext()){
	      Map.Entry me = (Map.Entry)itr2.next();
	      //log.debug(me.getKey() + " : " + TrialClass.getXMLString((Document)me.getValue()) );
	    }
		
		
		/************************ Invoking changeOrder on the Customer order***********************************************/

		env.setApiTemplate(XPXLiterals.CHANGE_ORDER_API, changeOrderTemplate);
		changeOrderOutputDoc = api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderInputDoc);
		env.clearApiTemplate(XPXLiterals.CHANGE_ORDER_API);
		/******************************************************************************************************************/

		
		Set set3 = chainedOHKAndType.entrySet();

	    Iterator itr3 = set3.iterator();

	    while(itr3.hasNext()){
	      Map.Entry me = (Map.Entry)itr3.next();
	      //log.debug(me.getKey() + " : " + TrialClass.getXMLString((Document)me.getValue()) );
	    }
		
		
		
		changeOrderOutputDocRoot = changeOrderOutputDoc.getDocumentElement();
		Element changeOrderOutputDocRootOrderExtn = (Element)changeOrderOutputDocRoot.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		Element changeOrderOutputDocPersonInfoBillTo = (Element)changeOrderOutputDocRoot.getElementsByTagName(XPXLiterals.E_PERSON_INFO_BILL_TO).item(0);
		Element changeOrderOutputDocPersonInfoShipTo = (Element)changeOrderOutputDocRoot.getElementsByTagName(XPXLiterals.E_PERSON_INFO_SHIP_TO).item(0);
		Element changeOrderOutputDocPersonInfoBillToExtn = (Element)changeOrderOutputDocPersonInfoBillTo.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		Element changeOrderOutputDocPersonInfoShipToExtn = (Element)changeOrderOutputDocPersonInfoShipTo.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		Element changeOrderOutputDocOrderPriceInfo = (Element)changeOrderOutputDocRoot.getElementsByTagName(XPXLiterals.E_PRICE_INFO).item(0);
		
        /**********Consolidating all the change order xmls from all the arraylist available************************************/
		
		if(orderLineElementsForThirdPartyOrder.size()>0 || orderLineInputOfExistingLineThirdPartyForOtherChanges.size() > 0)
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.THIRD_PARTY_ORDER);
			
			//log.debug("The change order header in the second change order is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			
			for(int i=0; i<orderLineElementsForThirdPartyOrder.size();i++)
			{
				Document orderLineInput = (Document)orderLineElementsForThirdPartyOrder.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}
			/*for(int a=0; a<orderLineInputOfExistingLineThirdParty.size();a++)
			{
				Document orderLineInput = (Document)orderLineInputOfExistingLineThirdParty.get(a);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}*/
			/*for(int b=0; b<orderLineElementsForThirdPartyOrderLineTypeUpdate.size();b++)
			{
				Document orderLineInput = (Document)orderLineElementsForThirdPartyOrderLineTypeUpdate.get(b);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}*/
			for(int c=0; c<orderLineInputOfExistingLineThirdPartyForOtherChanges.size(); c++)
			{
				Document orderLineInput = (Document)orderLineInputOfExistingLineThirdPartyForOtherChanges.get(c);
				
				//Element orderLineInput = (Element)orderLineInputOfExistingLineThirdPartyForOtherChanges.get(c);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}
			if(changeOrderOutputDocRootOrderExtn!=null && changeOrderOutputDocOrderPriceInfo!=null)
			{
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocRootOrderExtn);
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocOrderPriceInfo);
				//importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocPersonInfoBillTo);
				//importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocPersonInfoShipTo);
			}
			
			//log.debug("The changeOrder input document is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);
			
			
			
		}
		
		if(orderLineElementsForDirectOrder.size()>0 || orderLineInputOfExistingLineDirectOrderForOtherChanges.size() > 0)
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.DIRECT_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			
			for(int i=0; i<orderLineElementsForDirectOrder.size();i++)
			{
				Document orderLineInput = (Document)orderLineElementsForDirectOrder.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}
			/*for(int a=0; a<orderLineInputOfExistingLineDirectOrder.size();a++)
			{
				Document orderLineInput = (Document)orderLineInputOfExistingLineDirectOrder.get(a);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}*/
			/*for(int b=0; b<orderLineElementsForDirectOrderLineTypeUpdate.size();b++)
			{
				Document orderLineInput = (Document)orderLineElementsForDirectOrderLineTypeUpdate.get(b);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}*/
			for(int c=0; c<orderLineInputOfExistingLineDirectOrderForOtherChanges.size();c++)
			{
				Document orderLineInput = (Document)orderLineInputOfExistingLineDirectOrderForOtherChanges.get(c);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}
			if(changeOrderOutputDocRootOrderExtn!=null && changeOrderOutputDocOrderPriceInfo!=null)
			{
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocRootOrderExtn);
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocOrderPriceInfo);
				//importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocPersonInfoBillTo);
				//importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocPersonInfoShipTo);
			}
			
			//log.debug("The changeOrder input document is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);
			
		}
		
		if(orderLineElementsForStockOrder.size()>0 || orderLineInputOfExistingLineStockOrderForOtherChanges.size() > 0)
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.STOCK_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			
			for(int i=0; i<orderLineElementsForStockOrder.size();i++)
			{
				Document orderLineInput = (Document)orderLineElementsForStockOrder.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}
			/*for(int a=0; a<orderLineInputOfExistingLineStockOrder.size();a++)
			{
				Document orderLineInput = (Document)orderLineInputOfExistingLineStockOrder.get(a);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}*/
			/*for(int b=0; b<orderLineElementsForStockOrdeLineTypeUpdate.size();b++)
			{
				Document orderLineInput = (Document)orderLineElementsForStockOrdeLineTypeUpdate.get(b);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}*/
			for(int c=0; c<orderLineInputOfExistingLineStockOrderForOtherChanges.size();c++)
			{
				Document orderLineInput = (Document)orderLineInputOfExistingLineStockOrderForOtherChanges.get(c);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}
			if(changeOrderOutputDocRootOrderExtn!=null && changeOrderOutputDocOrderPriceInfo!=null)
			{
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocRootOrderExtn);
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocOrderPriceInfo);
				//importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocPersonInfoBillTo);
				//importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocPersonInfoShipTo);
			}
			
			//log.debug("The changeOrder input document is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);
			
		}
		
		
		
		
		
		
		
		
		
		/************************Updating the chained orders if new lines are added*****************************************//*
		

		if(orderLineElementsForThirdPartyOrder.size()>0)// Means chained line type updates exist
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.THIRD_PARTY_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			for(int i=0; i<orderLineElementsForThirdPartyOrder.size();i++)
			{
				Document orderLineInput = (Document)orderLineElementsForThirdPartyOrder.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}
			if(changeOrderInputDocExtn!=null && orderPriceInfo!=null && personInfoBillTo!=null && personInfoShipTo!=null)
			{
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderInputDocExtn);
				importElement(changeOrderForChainedOrderInputDocRoot,orderPriceInfo);
				importElement(changeOrderForChainedOrderInputDocRoot,personInfoBillTo);
				importElement(changeOrderForChainedOrderInputDocRoot,personInfoShipTo);
			}

			//changeOrderInputForChainedOrders.put(XPXLiterals.THIRD_PARTY_ORDER,changeOrderForChainedOrderInputDoc); 
			log.debug("The changeOrder input document is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);

		}

		if(orderLineElementsForDirectOrder.size()>0)// Means chained line type updates exist
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.DIRECT_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			for(int i=0; i<orderLineElementsForDirectOrder.size();i++)
			{
				Document orderLineInput = (Document)orderLineElementsForDirectOrder.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}

			if(changeOrderInputDocExtn!=null && orderPriceInfo!=null && personInfoBillTo!=null && personInfoShipTo!=null)
			{
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderInputDocExtn);
				importElement(changeOrderForChainedOrderInputDocRoot,orderPriceInfo);
				importElement(changeOrderForChainedOrderInputDocRoot,personInfoBillTo);
				importElement(changeOrderForChainedOrderInputDocRoot,personInfoShipTo);
			}

			//changeOrderInputForChainedOrders.put(XPXLiterals.DIRECT_ORDER,changeOrderForChainedOrderInputDoc);
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);

		}

		if(orderLineElementsForStockOrder.size()>0)// Means chained line type updates exist
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.STOCK_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			for(int i=0; i<orderLineElementsForStockOrder.size();i++)
			{
				Document orderLineInput = (Document)orderLineElementsForStockOrder.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}

			if(changeOrderInputDocExtn!=null && orderPriceInfo!=null && personInfoBillTo!=null && personInfoShipTo!=null)
			{
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderInputDocExtn);
				importElement(changeOrderForChainedOrderInputDocRoot,orderPriceInfo);
				importElement(changeOrderForChainedOrderInputDocRoot,personInfoBillTo);
				importElement(changeOrderForChainedOrderInputDocRoot,personInfoShipTo);
			}
			//changeOrderInputForChainedOrders.put(XPXLiterals.STOCK_ORDER,changeOrderForChainedOrderInputDoc);
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);

		}
		*//******************************************************************************************************************/


		/**********************************Order qty nullification on Chained orders for line type change*********************/


		if(orderLineInputOfExistingLineThirdParty.size() > 0)
		{
			log.debug("The change order for ordered qty modification is in THird party loop");
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.THIRD_PARTY_ORDER);
			//log.debug("The changeOrder input document before adding the arraylist entries is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			Element changeOrderForChainedOrderInputDocRoot =changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			for(int i=0; i<orderLineInputOfExistingLineThirdParty.size();i++)
			{
				Document orderLineInput = (Document)orderLineInputOfExistingLineThirdParty.get(i);
				//log.debug("The orderLine input is: "+TrialClass.getXMLString(orderLineInput));
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}

			//log.debug("The changeOrder input document before extn stamping is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			if(!changeOrderInputForChainedOrders.containsKey(XPXLiterals.THIRD_PARTY_ORDER))
			{//Means there was no earlier change Order and so we need to ad these elements
				if(changeOrderOutputDocRootOrderExtn!=null && changeOrderOutputDocOrderPriceInfo!=null)
				{
					importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocRootOrderExtn);
					importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocOrderPriceInfo);
					//importElement(changeOrderForChainedOrderInputDocRoot,personInfoBillTo);
					//importElement(changeOrderForChainedOrderInputDocRoot,personInfoShipTo);
					changeOrderInputForChainedOrders.put(XPXLiterals.THIRD_PARTY_ORDER,changeOrderForChainedOrderInputDoc);
				}
			//}
			//log.debug("The changeOrder input document is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);
		}
		}
		if(orderLineInputOfExistingLineDirectOrder.size() > 0)
		{
			log.debug("The change order for ordered qty modification is in direct order loop");
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.DIRECT_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			for(int i=0; i<orderLineInputOfExistingLineDirectOrder.size();i++)
			{
				Document orderLineInput = (Document)orderLineInputOfExistingLineDirectOrder.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}

			if(!changeOrderInputForChainedOrders.containsKey(XPXLiterals.DIRECT_ORDER))
			{//Means there was no earlier change Order and so we need to ad these elements
				if(changeOrderOutputDocRootOrderExtn!=null && changeOrderOutputDocOrderPriceInfo!=null)
				{
					importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocRootOrderExtn);
					importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocOrderPriceInfo);
					//importElement(changeOrderForChainedOrderInputDocRoot,personInfoBillTo);
					//importElement(changeOrderForChainedOrderInputDocRoot,personInfoShipTo);
					changeOrderInputForChainedOrders.put(XPXLiterals.DIRECT_ORDER,changeOrderForChainedOrderInputDoc);
				}
			}
			//log.debug("The changeOrder input document is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);
		}

		if(orderLineInputOfExistingLineStockOrder.size() > 0)
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.STOCK_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			for(int i=0; i<orderLineInputOfExistingLineStockOrder.size();i++)
			{
				Document orderLineInput = (Document)orderLineInputOfExistingLineStockOrder.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}

			if(!changeOrderInputForChainedOrders.containsKey(XPXLiterals.STOCK_ORDER))
			{//Means there was no earlier change Order and so we need to ad these elements
				if(changeOrderOutputDocRootOrderExtn!=null && changeOrderOutputDocOrderPriceInfo!=null)
				{
					importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocRootOrderExtn);
					importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocOrderPriceInfo);
					//importElement(changeOrderForChainedOrderInputDocRoot,personInfoBillTo);
					//importElement(changeOrderForChainedOrderInputDocRoot,personInfoShipTo);
					changeOrderInputForChainedOrders.put(XPXLiterals.STOCK_ORDER,changeOrderForChainedOrderInputDoc);
				}
			}
			//log.debug("The changeOrder input document is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);
		}

		/*********************************************************************************************************************/

		/***************************************Order status change of Customer order on line type change*********************/


		if(orderLineInputOfExistingLineThirdParty.size() > 0 || orderLineInputOfExistingLineDirectOrder.size() > 0
				|| orderLineInputOfExistingLineStockOrder.size() > 0)
		{
			Document changeOrderStatusInputDoc = createChangeOrderStatusInputDoc(changeOrderStatusOrderLineInput,changeOrderInputDocOHK);

			//log.debug("The change Order status xml is: "+TrialClass.getXMLString(changeOrderStatusInputDoc));

			api.invoke(env,XPXLiterals.CHANGE_ORDER_STATUS_API,changeOrderStatusInputDoc);
		}
		/*********************************************************************************************************************/

	   
		
	
		
		
		
		/************************Updating the chained orders if new lines are added on a line type change****************************/

		if(orderLineElementsForThirdPartyOrderLineTypeUpdate.size()>0)// Means chained line type updates exist
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.THIRD_PARTY_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			for(int i=0; i<orderLineElementsForThirdPartyOrderLineTypeUpdate.size();i++)
			{
				Document orderLineInput = (Document)orderLineElementsForThirdPartyOrderLineTypeUpdate.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}
			if(changeOrderOutputDocRootOrderExtn!=null && changeOrderOutputDocOrderPriceInfo!=null)
			{
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocRootOrderExtn);
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocOrderPriceInfo);
				//importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocPersonInfoBillTo);
				//importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocPersonInfoShipTo);
			}

			//changeOrderInputForChainedOrders.put(XPXLiterals.THIRD_PARTY_ORDER,changeOrderForChainedOrderInputDoc); 
			//log.debug("The changeOrder input document is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);

		}

		if(orderLineElementsForDirectOrderLineTypeUpdate.size()>0)// Means chained line type updates exist
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.DIRECT_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			for(int i=0; i<orderLineElementsForDirectOrderLineTypeUpdate.size();i++)
			{
				Document orderLineInput = (Document)orderLineElementsForDirectOrderLineTypeUpdate.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}

			if(changeOrderOutputDocRootOrderExtn!=null && changeOrderOutputDocOrderPriceInfo!=null)
			{
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocRootOrderExtn);
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocOrderPriceInfo);
				//importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocPersonInfoBillTo);
				//importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocPersonInfoShipTo);
			}

			changeOrderInputForChainedOrders.put(XPXLiterals.DIRECT_ORDER,changeOrderForChainedOrderInputDoc);
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);

		}

		if(orderLineElementsForStockOrdeLineTypeUpdate.size()>0)// Means chained line type updates exist
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.STOCK_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			for(int i=0; i<orderLineElementsForStockOrdeLineTypeUpdate.size();i++)
			{
				Document orderLineInput = (Document)orderLineElementsForStockOrdeLineTypeUpdate.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}

			if(changeOrderOutputDocRootOrderExtn!=null && changeOrderOutputDocOrderPriceInfo!=null)
			{
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocRootOrderExtn);
				importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocOrderPriceInfo);
				//importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocPersonInfoBillTo);
				//importElement(changeOrderForChainedOrderInputDocRoot,changeOrderOutputDocPersonInfoShipTo);
			}
			changeOrderInputForChainedOrders.put(XPXLiterals.STOCK_ORDER,changeOrderForChainedOrderInputDoc);
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);

		}
		/******************************************************************************************************************/




		/*************************************Order Qty Change orders on Chained Orders*********************************/
		if(ordLineElemsForThirdPartyOrderQtyChange.size() > 0)
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.THIRD_PARTY_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			for(int i=0; i<ordLineElemsForThirdPartyOrderQtyChange.size();i++)
			{
				Document orderLineInput = (Document)ordLineElemsForThirdPartyOrderQtyChange.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}

			if(!changeOrderInputForChainedOrders.containsKey(XPXLiterals.THIRD_PARTY_ORDER))
			{//Means there was no earlier change Order and so we need to ad these elements
				if(changeOrderInputDocExtn!=null && orderPriceInfo!=null && personInfoBillTo!=null && personInfoShipTo!=null)
				{
					importElement(changeOrderForChainedOrderInputDocRoot,changeOrderInputDocExtn);
					importElement(changeOrderForChainedOrderInputDocRoot,orderPriceInfo);
					importElement(changeOrderForChainedOrderInputDocRoot,personInfoBillTo);
					importElement(changeOrderForChainedOrderInputDocRoot,personInfoShipTo);
					changeOrderInputForChainedOrders.put(XPXLiterals.THIRD_PARTY_ORDER,changeOrderForChainedOrderInputDoc);
				}
			}
			//log.debug("The changeOrder input document is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);
		}

		if(ordLineElemsForDirectOrderQtyChange.size() > 0)
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.DIRECT_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			for(int i=0; i<ordLineElemsForDirectOrderQtyChange.size();i++)
			{
				Document orderLineInput = (Document)ordLineElemsForDirectOrderQtyChange.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}

			if(!changeOrderInputForChainedOrders.containsKey(XPXLiterals.DIRECT_ORDER))
			{//Means there was no earlier change Order and so we need to ad these elements
				if(changeOrderInputDocExtn!=null && orderPriceInfo!=null && personInfoBillTo!=null && personInfoShipTo!=null)
				{
					importElement(changeOrderForChainedOrderInputDocRoot,changeOrderInputDocExtn);
					importElement(changeOrderForChainedOrderInputDocRoot,orderPriceInfo);
					importElement(changeOrderForChainedOrderInputDocRoot,personInfoBillTo);
					importElement(changeOrderForChainedOrderInputDocRoot,personInfoShipTo);
					changeOrderInputForChainedOrders.put(XPXLiterals.DIRECT_ORDER,changeOrderForChainedOrderInputDoc);
				}
			}
			//log.debug("The changeOrder input document is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);
		}

		if(ordLineElemsForStockOrderQtyChange.size() > 0)
		{
			Document changeOrderForChainedOrderInputDoc = (Document)chainedOHKAndType.get(XPXLiterals.STOCK_ORDER);
			Element changeOrderForChainedOrderInputDocRoot = changeOrderForChainedOrderInputDoc.getDocumentElement();
			Element changeOrderForChainedOrderInputDocOrderLines = (Element)changeOrderForChainedOrderInputDocRoot.
			getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0); 
			for(int i=0; i<ordLineElemsForStockOrderQtyChange.size();i++)
			{
				Document orderLineInput = (Document)ordLineElemsForStockOrderQtyChange.get(i);
				importElement(changeOrderForChainedOrderInputDocOrderLines,orderLineInput.getDocumentElement());
			}

			if(!changeOrderInputForChainedOrders.containsKey(XPXLiterals.STOCK_ORDER))
			{//Means there was no earlier change Order and so we need to ad these elements
				if(changeOrderInputDocExtn!=null && orderPriceInfo!=null && personInfoBillTo!=null && personInfoShipTo!=null)
				{
					importElement(changeOrderForChainedOrderInputDocRoot,changeOrderInputDocExtn);
					importElement(changeOrderForChainedOrderInputDocRoot,orderPriceInfo);
					importElement(changeOrderForChainedOrderInputDocRoot,personInfoBillTo);
					importElement(changeOrderForChainedOrderInputDocRoot,personInfoShipTo);
					changeOrderInputForChainedOrders.put(XPXLiterals.STOCK_ORDER,changeOrderForChainedOrderInputDoc);
				}
			}
			//log.debug("The changeOrder input document is: "+TrialClass.getXMLString(changeOrderForChainedOrderInputDoc));
			api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderForChainedOrderInputDoc);
		}
		/*********************************************************************************************************************/


		/**********************Create Chained Order with new line type if else update existing Chained Order******************/

		/*****************************Creating the chained orders if the new lines are added*******************************/

		Set set = createOrderInputForChainedOrders.entrySet();

		Iterator itr1 = set.iterator();

		while(itr1.hasNext()){


			Map.Entry me = (Map.Entry)itr1.next();
			//log.debug(me.getKey() + " : " + TrialClass.getXMLString((Document)me.getValue()) );
		}

		if(createOrderInputForChainedOrders.containsKey(XPXLiterals.THIRD_PARTY_ORDER))
		{
			Document chainedOrderCreateOrderInput_THIRD_PARTY = (Document)createOrderInputForChainedOrders.get(XPXLiterals.THIRD_PARTY_ORDER);

			/*if(checkElementPresent(XPXLiterals.E_PERSON_INFO_BILL_TO,chainedOrderCreateOrderInput_THIRD_PARTY))
			{
				importElement(chainedOrderCreateOrderInput_THIRD_PARTY.getDocumentElement(), changeOrderOutputDocPersonInfoBillTo);
			}
			if(checkElementPresent(XPXLiterals.E_PERSON_INFO_SHIP_TO,chainedOrderCreateOrderInput_THIRD_PARTY))
			{
				importElement(chainedOrderCreateOrderInput_THIRD_PARTY.getDocumentElement(), changeOrderOutputDocPersonInfoShipTo);
			}
			if(checkElementPresent(XPXLiterals.E_PRICE_INFO,chainedOrderCreateOrderInput_THIRD_PARTY))
			{
				importElement(chainedOrderCreateOrderInput_THIRD_PARTY.getDocumentElement(), changeOrderOutputDocOrderPriceInfo);
			}*/
			if(changeOrderOutputDocRootOrderExtn!=null && changeOrderOutputDocOrderPriceInfo!=null 
					&& changeOrderOutputDocPersonInfoBillTo!=null && changeOrderOutputDocPersonInfoShipTo!=null)
			{
				importElement(chainedOrderCreateOrderInput_THIRD_PARTY.getDocumentElement(),changeOrderOutputDocRootOrderExtn);
				importElement(chainedOrderCreateOrderInput_THIRD_PARTY.getDocumentElement(),changeOrderOutputDocOrderPriceInfo);
				importElement(chainedOrderCreateOrderInput_THIRD_PARTY.getDocumentElement(),changeOrderOutputDocPersonInfoBillTo);
				importElement(chainedOrderCreateOrderInput_THIRD_PARTY.getDocumentElement(),changeOrderOutputDocPersonInfoShipTo);
			}
			//log.debug("the create order xml is: "+TrialClass.getXMLString(chainedOrderCreateOrderInput_THIRD_PARTY));
			api.invoke(env,XPXLiterals.CREATE_ORDER_API, chainedOrderCreateOrderInput_THIRD_PARTY);

		}



		if(createOrderInputForChainedOrders.containsKey(XPXLiterals.DIRECT_ORDER))
		{
			Document chainedOrderCreateOrderInput_DIRECT = (Document)createOrderInputForChainedOrders.get(XPXLiterals.DIRECT_ORDER);

			/*if(checkElementPresent(XPXLiterals.E_PERSON_INFO_BILL_TO,chainedOrderCreateOrderInput_DIRECT))
			{
				importElement(chainedOrderCreateOrderInput_DIRECT.getDocumentElement(), changeOrderOutputDocPersonInfoBillTo);
			}
			if(checkElementPresent(XPXLiterals.E_PERSON_INFO_SHIP_TO,chainedOrderCreateOrderInput_DIRECT))
			{
				importElement(chainedOrderCreateOrderInput_DIRECT.getDocumentElement(), changeOrderOutputDocPersonInfoShipTo);
			}
			if(checkElementPresent(XPXLiterals.E_PRICE_INFO,chainedOrderCreateOrderInput_DIRECT))
			{
				importElement(chainedOrderCreateOrderInput_DIRECT.getDocumentElement(), changeOrderOutputDocOrderPriceInfo);
			}*/
			if(changeOrderOutputDocRootOrderExtn!=null && changeOrderOutputDocOrderPriceInfo!=null 
					&& changeOrderOutputDocPersonInfoBillTo!=null && changeOrderOutputDocPersonInfoShipTo!=null)
			{
				importElement(chainedOrderCreateOrderInput_DIRECT.getDocumentElement(),changeOrderOutputDocRootOrderExtn);
				importElement(chainedOrderCreateOrderInput_DIRECT.getDocumentElement(),changeOrderOutputDocOrderPriceInfo);
				importElement(chainedOrderCreateOrderInput_DIRECT.getDocumentElement(),changeOrderOutputDocPersonInfoBillTo);
				importElement(chainedOrderCreateOrderInput_DIRECT.getDocumentElement(),changeOrderOutputDocPersonInfoShipTo);
			}
			
			//log.debug("the direct order create order xml is: "+TrialClass.getXMLString(chainedOrderCreateOrderInput_DIRECT));
			api.invoke(env,XPXLiterals.CREATE_ORDER_API, chainedOrderCreateOrderInput_DIRECT);
		}



		if(createOrderInputForChainedOrders.containsKey(XPXLiterals.STOCK_ORDER))
		{
			Document chainedOrderCreateOrderInput_STOCK = (Document)createOrderInputForChainedOrders.get(XPXLiterals.STOCK_ORDER);

			/*if(checkElementPresent(XPXLiterals.E_PERSON_INFO_BILL_TO,chainedOrderCreateOrderInput_STOCK))
			{
				importElement(chainedOrderCreateOrderInput_STOCK.getDocumentElement(), changeOrderOutputDocPersonInfoBillTo);
			}
			if(checkElementPresent(XPXLiterals.E_PERSON_INFO_SHIP_TO,chainedOrderCreateOrderInput_STOCK))
			{
				importElement(chainedOrderCreateOrderInput_STOCK.getDocumentElement(), changeOrderOutputDocPersonInfoShipTo);
			}
			if(checkElementPresent(XPXLiterals.E_PRICE_INFO,chainedOrderCreateOrderInput_STOCK))
			{
				importElement(chainedOrderCreateOrderInput_STOCK.getDocumentElement(), changeOrderOutputDocOrderPriceInfo);
			}*/
			if(changeOrderOutputDocRootOrderExtn!=null && changeOrderOutputDocOrderPriceInfo!=null 
					&& changeOrderOutputDocPersonInfoBillTo!=null && changeOrderOutputDocPersonInfoShipTo!=null)
			{
				importElement(chainedOrderCreateOrderInput_STOCK.getDocumentElement(),changeOrderOutputDocRootOrderExtn);
				importElement(chainedOrderCreateOrderInput_STOCK.getDocumentElement(),changeOrderOutputDocOrderPriceInfo);
				importElement(chainedOrderCreateOrderInput_STOCK.getDocumentElement(),changeOrderOutputDocPersonInfoBillTo);
				importElement(chainedOrderCreateOrderInput_STOCK.getDocumentElement(),changeOrderOutputDocPersonInfoShipTo);
			}
			
			api.invoke(env,XPXLiterals.CREATE_ORDER_API, chainedOrderCreateOrderInput_STOCK);
		}

		/*************************************************************************************************************/




		/*// Chaining the existing chained orders if there is an increase in the existing orderedQty

	    	for(int i=0; i<changeOrderInputDocOrderLineList.getLength(); i++)
			{

				Element changeOrderInputDocOrderLine = (Element)changeOrderInputDocOrderLineList.item(i);

				String changeOrderInputDocOrderLineKey = changeOrderInputDocOrderLine.getAttribute(XPXLiterals.A_ORDER_LINE_KEY);				
				String changeOrderInputDocOrderedQty = changeOrderInputDocOrderLine.getAttribute(XPXLiterals.A_ORDERED_QTY);
		        log.debug("The input order qty is: "+changeOrderInputDocOrderedQty);					

				if(changeOrderInputDocOrderLineKey != null && changeOrderInputDocOrderLineKey.trim().length() != 0)
				{					
					//Changing an existing line

					Document getOrderLineListInputDocument = createGetOrderLineListInputDoc(changeOrderInputDocOrderLineKey);
					env.setApiTemplate(XPXLiterals.GET_ORDER_LINE_LIST_API, getOrderLineListForOrderedQtyTemplate);
    				Document getOrderLineListOutputDoc = api.invoke(env,XPXLiterals.GET_ORDER_LINE_LIST_API, getOrderLineListInputDocument);
    				env.clearApiTemplate(XPXLiterals.GET_ORDER_LINE_LIST_API);
    				String existingOrderedQty= retrieveOrderedQty(getOrderLineListOutputDoc);


					// Iterating to get the speciific OrderLineKey's orderedQty,primeLineNo and subLineNo
    				if (existingOrderQty.containsKey(changeOrderInputDocOrderLineKey))
    				{
    					ArrayList arr = (ArrayList) existingOrderQty.getCollection(changeOrderInputDocOrderLineKey);

    					for(int j=0; j <arr.size();j++)
    					   {
    						   if( j==0)
    						   {
    						existingOrderedQty = (String)arr.get(j);
    						   }
    						   if( j==1 )
    						   {
    						existingPrimeLineNo = (String)arr.get(j);
    						   }
    						   if(j ==2)
    						   {
    						existingSubLineNo = (String)arr.get(j);
    						   }
    						   if( j==3 )
    						   {
    					    existingLineType = (String)arr.get(j);		   
    						   }
    					   }    					
    				}

    				log.debug("The current orderLineKey is: "+changeOrderInputDocOrderLineKey);
    				log.debug("The existing order qty is: "+existingOrderedQty);
    				log.debug("The existing prime line no is: "+existingPrimeLineNo);
    				log.debug("The existing sub line no: "+existingSubLineNo);
    				log.debug("The existing line type is: "+existingLineType);

    				//Retrieving the chained order

    				OrderLineListInputDoc = createOrderLineListInputDocWithChainedOLK(changeOrderInputDocOrderLineKey,
    						changeOrderInputDocOHK);

    				env.setApiTemplate(XPXLiterals.GET_ORDER_LINE_LIST_API, orderLineListTemplate);
    				OrderLineListOutputDoc = api.invoke(env,XPXLiterals.GET_ORDER_LINE_LIST_API, OrderLineListInputDoc);
    				env.clearApiTemplate(XPXLiterals.GET_ORDER_LINE_LIST_API);


    				String chainedFromOHK = retrieveChainedOrderHeaderKey(OrderLineListOutputDoc);
    				log.debug("The chained order OHK is :"+chainedFromOHK);

    				String chainedOrderOLK = retrieveChainedOrderLineKey(OrderLineListOutputDoc);
    				log.debug("The chained order OLK is :"+chainedOrderOLK);


    				    //Checking if there is a difference in the order quantities of the existing lines
    					if(Float.parseFloat(changeOrderInputDocOrderedQty) > Float.parseFloat(existingOrderedQty))
    					{

    						//Create changeOrder input to stamp the new orderedQty on Chained Line[Product case created]   						
    						Document orderLineInputForChangeOrder = createOrderLineInput(chainedFromOHK,changeOrderInputDocType,
    								changeOrderInputDocEntCode,chainedOrderOLK,changeOrderInputDocOrderedQty,existingPrimeLineNo,
    								existingSubLineNo,changeOrderInputDocOHK);

    						api.invoke(env,XPXLiterals.CHANGE_ORDER_API, orderLineInputForChangeOrder);				    						

    					}    				

				}
			}



                  //Adding a new order line to the customer order and creating its corresponding chained order if the order type 
	    	      //already exists else appending the new line to the existing chained order type



	    			changeOrderOutputDocRoot = changeOrderOutputDoc.getDocumentElement();
	    			orderNo = changeOrderOutputDocRoot.getAttribute(XPXLiterals.A_ORDER_NO); 
	    			String buyerOrganizationCode = changeOrderOutputDocRoot.getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);*/
		/*Element orderExtn = (Element) changeOrderOutputDocRoot.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
	    			Element orderPriceInfo = (Element) changeOrderOutputDocRoot.getElementsByTagName(XPXLiterals.E_PRICE_INFO).item(0);

	    			Element personInfoShipTo = (Element) changeOrderOutputDocRoot.getElementsByTagName(XPXLiterals.E_PERSON_INFO_SHIP_TO).item(0);
	    			Element personInfoShipToExtn = (Element) personInfoShipTo.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

	    			Element personInfoBillTo = (Element) changeOrderOutputDocRoot.getElementsByTagName(XPXLiterals.E_PERSON_INFO_BILL_TO).item(0);
	    			Element personInfoBillToExtn = (Element) personInfoBillTo.getElementsByTagName(XPXLiterals.E_EXTN).item(0);

	    			Element changeOrderOutputDocOrderLines = (Element)changeOrderOutputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);
		 */
		//NodeList changeOrderOutputDocOrderLineList = changeOrderOutputDocOrderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);

		/*for(int j=0; j< changeOrderOutputDocOrderLineList.getLength(); j++)
	    			{
	    				Element changeOrderOutputDocOrderLine = (Element)changeOrderOutputDocOrderLineList.item(j);	    				
	    				String changeOrderOutputPrimeLineNo = changeOrderOutputDocOrderLine.getAttribute(XPXLiterals.A_PRIME_LINE_NO);
	    				String changeOrderOutputSubLineNo = changeOrderOutputDocOrderLine.getAttribute(XPXLiterals.A_SUB_LINE_NO);
	    				String changeOrderOutputLineType = changeOrderOutputDocOrderLine.getAttribute(XPXLiterals.A_LINE_TYPE);
	    				String orderedQty = changeOrderOutputDocOrderLine.getAttribute(XPXLiterals.A_ORDERED_QTY);

	    				Element orderLineTranQuantity = (Element)changeOrderOutputDocOrderLine.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);				
	    				String transactionalUOM = orderLineTranQuantity.getAttribute(XPXLiterals.A_TRANSACTIONAL_UOM);					    				

	    				Element linePriceInfo = (Element)changeOrderOutputDocOrderLine.getElementsByTagName(XPXLiterals.E_LINE_PRICE_INFO).item(0);

	    				//Checking if primeLineNo is a greater number than the total number of existing lines which indictaes that this is a new line
	    				if(Integer.parseInt(changeOrderOutputPrimeLineNo) > noOfExistingOrderLines)
	    				{

	    					Element changeOrderOutputItem = (Element) changeOrderOutputDocOrderLine.getElementsByTagName(XPXLiterals.E_ITEM).item(0);
	    					String changeOrderOutputItemId = changeOrderOutputItem.getAttribute(XPXLiterals.A_ITEM_ID);
	    					String unitOfMeasure = changeOrderOutputItem.getAttribute(XPXLiterals.A_UNIT_OF_MEASURE);
		    				String productClass = changeOrderOutputItem.getAttribute(XPXLiterals.A_PRODUCT_CLASS);

		    				Element changeOrderOutputItemExtn = (Element)changeOrderOutputItem.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
	    					Element  changeOrderOutputExtn =  (Element) changeOrderOutputDocOrderLine.getElementsByTagName(XPXLiterals.E_EXTN).item(0);


	    					boolean doesLineTypeExist = checkLineTypeExistsBeforeChangeOrder(outputOrderLineListDoc,changeOrderOutputLineType);

	    					if(doesLineTypeExist)
		    				{	    						
		    					Document changeOrderInputDocument = createOrderLineInputForChangeOrder(outputOrderLineListDoc,changeOrderOutputDoc,
		    							changeOrderInputDocType,changeOrderInputDocEntCode,orderNo,changeOrderOutputExtn,orderedQty,transactionalUOM,
		    							changeOrderOutputItemId,unitOfMeasure,productClass,changeOrderOutputPrimeLineNo,changeOrderOutputSubLineNo,
		    							changeOrderOutputLineType);

		    					orderLineElementsForChangeOrder.add(changeOrderInputDocument);
		    				}
	    					else
	    					{
	    						if(orderLineType.equalsIgnoreCase(XPXLiterals.THIRD_PARTY_ORDER))
	    	    				{
	    	    					Document orderLineElementDoc = createorderLineDoc(orderedQty,transactionalUOM,changeOrderOutputItemId,
	    	    							unitOfMeasure,productClass,changeOrderInputDocType,changeOrderInputDocEntCode,orderNo,changeOrderOutputPrimeLineNo,
	    	    							changeOrderOutputSubLineNo,changeOrderOutputExtn,changeOrderOutputItemExtn,linePriceInfo,changeOrderOutputLineType);

	    	    					thirdPartyList.add(orderLineElementDoc);
	    	    				}
	    	    				else if(orderLineType.equalsIgnoreCase(XPXLiterals.DIRECT_ORDER))

	    	    				{
	    	    					Document orderLineElementDoc = createorderLineDoc(orderedQty,transactionalUOM,changeOrderOutputItemId,
	    	    							unitOfMeasure,productClass,changeOrderInputDocType,changeOrderInputDocEntCode,orderNo,changeOrderOutputPrimeLineNo,
	    	    							changeOrderOutputSubLineNo,changeOrderOutputExtn,changeOrderOutputItemExtn,linePriceInfo,changeOrderOutputLineType);

	    	    					directOrderList.add(orderLineElementDoc);
	    	    				}

	    	    				else 
	    	    				{
	    	    					Document orderLineElementDoc = createorderLineDoc(orderedQty,transactionalUOM,changeOrderOutputItemId,
	    	    							unitOfMeasure,productClass,changeOrderInputDocType,changeOrderInputDocEntCode,orderNo,changeOrderOutputPrimeLineNo,
	    	    							changeOrderOutputSubLineNo,changeOrderOutputExtn,changeOrderOutputItemExtn,linePriceInfo,changeOrderOutputLineType);

	    	    					stockOrderList.add(orderLineElementDoc);
	    	    				}

	    	    				if (thirdPartyList.size()>0)
	    	    				{

	    	    						Document chainedOrderInput_THIRD_PARTY = createChainedOrderInput(thirdPartyList,changeOrderInputDocType,
	    	    								changeOrderInputDocEntCode,	buyerOrganizationCode,XPXLiterals.THIRD_PARTY_ORDER,orderExtn,personInfoShipTo,personInfoBillTo,orderPriceInfo,
	    	    								personInfoShipToExtn,personInfoBillToExtn);

	    	    						api.invoke(env,XPXLiterals.CREATE_ORDER_API, chainedOrderInput_THIRD_PARTY);

	    	    				}

	    	    				if(directOrderList.size() > 0)
	    	    				{
	    	    					Document chainedOrderInput_DIRECT = createChainedOrderInput(directOrderList,changeOrderInputDocType,
	    	    							changeOrderInputDocEntCode,buyerOrganizationCode,XPXLiterals.DIRECT_ORDER,orderExtn,personInfoShipTo,personInfoBillTo,orderPriceInfo,
	    	    							personInfoShipToExtn,personInfoBillToExtn);

	    	    					api.invoke(env,XPXLiterals.CREATE_ORDER_API, chainedOrderInput_DIRECT);
	    	    				}

	    	    				if(stockOrderList.size() > 0)
	    	    				{
	    	    					Document chainedOrderInput_STOCK = createChainedOrderInput(stockOrderList,changeOrderInputDocType,changeOrderInputDocEntCode,
	    	    							buyerOrganizationCode,XPXLiterals.STOCK_ORDER, orderExtn,personInfoShipTo,personInfoBillTo,orderPriceInfo,
	    	    							personInfoShipToExtn,personInfoBillToExtn);

	    	    					api.invoke(env,XPXLiterals.CREATE_ORDER_API, chainedOrderInput_STOCK);
	    	    				}
	    	    				}



	    					if(orderLineElementsForChangeOrder.size() > 0)
		    				{
		    					for(int k=0; k < orderLineElementsForChangeOrder.size(); k++)

		    					{
		    						Document changeOrderInputDocument = (Document)orderLineElementsForChangeOrder.get(k);
		    						api.invoke(env,XPXLiterals.CHANGE_ORDER_API, changeOrderInputDocument);
		    					}
		    				}
	    				}
	    				else// These are existing lines where we check for line type and ordered qty change
	    				{

	    				}

	    			}*/



		

		log.endTimer("XPXUpdateOrderWithNewItemsAPI.updateExistingOrder");	
		return changeOrderInputDoc;
	}

	private Document invokeXREF(YFSEnvironment env, String itemId, String legacyCustomerNo, String environment_id, String company_code, String customer_branch) {


		Document XREFOutputDoc = null; 
		
        Document XREFInputDoc = createDocument(XPXLiterals.E_XPX_ITEM_CUST_XREF);
        XREFInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENVT_CODE, environment_id);
        XREFInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_COMPANY_CODE, company_code);
        XREFInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_NO,legacyCustomerNo);
        XREFInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_BRANCH,customer_branch);
        XREFInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_LEGACY_ITEM_NO,itemId);
	
	      log.debug("The input to getXrefList is: "+SCXmlUtil.getString(XREFInputDoc));
        //<XPXItemcustXrefList />
        
        try {
			XREFOutputDoc = api.executeFlow(env, XPXLiterals.GET_XREF_LIST, XREFInputDoc);
			
			log.debug("The output of getXrefList is: "+SCXmlUtil.getString(XREFOutputDoc));
		} catch (YFSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	return XREFOutputDoc;
		
	}

	private Document createGetCustomerListInput(YFSEnvironment env, String billToId) {
		
		Document getCustomerListInputDoc = createDocument(XPXLiterals.E_CUSTOMER);
        
        getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, billToId);
	
        return getCustomerListInputDoc;
	}

	private Document createDefaultChangeOrderLineDoc(Element changeOrderInputDocOrderLine, String chainedOrderOLK) {


		Document orderLineDoc = createDocument(XPXLiterals.E_ORDER_LINE);
		
		Element orderLineExtnElement = orderLineDoc.createElement(XPXLiterals.E_EXTN);
		Element changeOrderInputDocOrderLineExtn = (Element)changeOrderInputDocOrderLine.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		
		Element orderLineItemElement = orderLineDoc.createElement(XPXLiterals.E_ITEM);
		Element changeOrderInputDocOrderLineItem = (Element)changeOrderInputDocOrderLine.getElementsByTagName(XPXLiterals.E_ITEM).item(0);
		
		Element orderLineItemExtnElement = orderLineDoc.createElement(XPXLiterals.E_EXTN);
		Element changeOrderInputDocOrderLineItemExtn = (Element)orderLineItemElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		
		Element orderLineTranQuantityElement = orderLineDoc.createElement(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY);
		Element changeOrderInputDocOrderLineOrdTranQty = (Element)changeOrderInputDocOrderLine.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
		
		Element orderLinePriceInfoElement = orderLineDoc.createElement(XPXLiterals.E_LINE_PRICE_INFO);
		Element changeOrderInputDocOrderLinePriceInfo = (Element)changeOrderInputDocOrderLine.getElementsByTagName(XPXLiterals.E_LINE_PRICE_INFO).item(0);
		
		try {
			copyAttributes(orderLineDoc.getDocumentElement(),changeOrderInputDocOrderLine);
			orderLineDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_LINE_KEY, chainedOrderOLK);
			orderLineDoc.getDocumentElement().setAttribute(XPXLiterals.A_ACTION,XPXLiterals.MODIFY);
			if(changeOrderInputDocOrderLineExtn!=null)
			{
			copyAttributes(orderLineExtnElement,changeOrderInputDocOrderLineExtn);
			orderLineDoc.getDocumentElement().appendChild(orderLineExtnElement);
			}
			if(changeOrderInputDocOrderLineItem!=null)
			{
				copyAttributes(orderLineItemElement,changeOrderInputDocOrderLineItem);
				orderLineDoc.getDocumentElement().appendChild(orderLineItemElement);
			}
			if(changeOrderInputDocOrderLineItemExtn!=null)
			{
				copyAttributes(orderLineItemExtnElement,changeOrderInputDocOrderLineItemExtn);
				orderLineItemElement.appendChild(orderLineItemExtnElement);
			}
			if(changeOrderInputDocOrderLineOrdTranQty!=null)
			{
				copyAttributes(orderLineTranQuantityElement,changeOrderInputDocOrderLineOrdTranQty);
				orderLineDoc.getDocumentElement().appendChild(orderLineTranQuantityElement);
			}
			if(changeOrderInputDocOrderLinePriceInfo!=null)
			{
				copyAttributes(orderLinePriceInfoElement,changeOrderInputDocOrderLinePriceInfo);
				orderLineDoc.getDocumentElement().appendChild(orderLinePriceInfoElement);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			//log.debug("The default changeOrderLine doc is: "+TrialClass.getXMLString(orderLineDoc));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return orderLineDoc;
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
	
	private long getNextDBSequenceNo(YFSEnvironment env, String web_line_sequence) {

		return ((YCPContext)env).getNextDBSeqNo(web_line_sequence);

	}

	private boolean checkElementPresent(String element_name, Document chainedOrderCreateOrderInput_THIRD_PARTY) {

		boolean isElementNotPresent = true;
		Element elementNameBeingChecked = null;

		elementNameBeingChecked = (Element) chainedOrderCreateOrderInput_THIRD_PARTY.getDocumentElement()
		.getElementsByTagName(element_name).item(0);

		if(elementNameBeingChecked != null)
		{
			isElementNotPresent = false;
		}

		return isElementNotPresent;
	}

	private HashMap getOrderLineAttributes(Document getOrderListOutputDoc, String changeOrderInputDocOrderLineKey) {

		HashMap getOrderLineAttributes = new HashMap();         


		Element getOrderListOutputDocOrderElement = (Element)getOrderListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_ORDER).item(0);
		Element getOrderListOutputDocOrderExtn = (Element)getOrderListOutputDocOrderElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		getOrderLineAttributes.put(orderExtn_LT,getOrderListOutputDocOrderExtn);

		Element getOrderListOutputDocOrderLinesElement = (Element)getOrderListOutputDocOrderElement.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);

		NodeList getOrderListOutputDocorderLineElement = getOrderListOutputDocOrderElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE);

		for(int i=0;i<getOrderListOutputDocorderLineElement.getLength();i++)
		{
			Element   getOrderListOutputDocOrderLineElement = (Element) getOrderListOutputDocorderLineElement.item(i);

			if(getOrderListOutputDocOrderLineElement.getAttribute(XPXLiterals.A_ORDER_LINE_KEY).equalsIgnoreCase(changeOrderInputDocOrderLineKey))
			{
				getOrderLineAttributes.put(XPXLiterals.A_PRIME_LINE_NO,getOrderListOutputDocOrderLineElement.getAttribute(XPXLiterals.A_PRIME_LINE_NO));
				getOrderLineAttributes.put(XPXLiterals.A_ORDERED_QTY,getOrderListOutputDocOrderLineElement.getAttribute(XPXLiterals.A_ORDERED_QTY));

				Element getOrderListOutputDocItemElement = (Element)getOrderListOutputDocOrderLineElement.getElementsByTagName(XPXLiterals.E_ITEM).item(0);

				getOrderLineAttributes.put(XPXLiterals.A_ITEM_ID,getOrderListOutputDocItemElement.getAttribute(XPXLiterals.A_ITEM_ID));
				getOrderLineAttributes.put(XPXLiterals.A_UNIT_OF_MEASURE,getOrderListOutputDocItemElement.getAttribute(XPXLiterals.A_UNIT_OF_MEASURE));
				getOrderLineAttributes.put(XPXLiterals.A_PRODUCT_CLASS,getOrderListOutputDocItemElement.getAttribute(XPXLiterals.A_PRODUCT_CLASS));

				Element getOrderListOutputDocItemExtnElement = (Element)getOrderListOutputDocItemElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
				getOrderLineAttributes.put(orderLineExtn_LT,getOrderListOutputDocItemExtnElement);

				Element getOrderListOutputDocLinePriceInfoElement = (Element)getOrderListOutputDocOrderLineElement.getElementsByTagName(XPXLiterals.E_LINE_PRICE_INFO).item(0);
				getOrderLineAttributes.put(XPXLiterals.E_LINE_PRICE_INFO,getOrderListOutputDocLinePriceInfoElement);

				Element getOrderListOutputDocOrderLineTran = (Element)getOrderListOutputDocOrderLineElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY).item(0);
				getOrderLineAttributes.put(XPXLiterals.A_TRANSACTIONAL_UOM,getOrderListOutputDocOrderLineTran.getAttribute(XPXLiterals.A_TRANSACTIONAL_UOM)); 
			}
		}

		Element getOrderListOutputDocPersonInfoShipToElement = (Element)getOrderListOutputDocOrderElement.getElementsByTagName(XPXLiterals.E_PERSON_INFO_SHIP_TO).item(0);
		getOrderLineAttributes.put(XPXLiterals.E_PERSON_INFO_SHIP_TO,getOrderListOutputDocPersonInfoShipToElement);

		Element getOrderListOutputDocPersonInfoBillToElement = (Element)getOrderListOutputDocOrderElement.getElementsByTagName(XPXLiterals.E_PERSON_INFO_BILL_TO).item(0);
		getOrderLineAttributes.put(XPXLiterals.E_PERSON_INFO_BILL_TO,getOrderListOutputDocPersonInfoShipToElement);

		Element personInfoBillToExtnElement = (Element)getOrderListOutputDocPersonInfoBillToElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		getOrderLineAttributes.put(personInfoBillToExtn_LT,personInfoBillToExtnElement);

		Element personInfoShipToExtnElement = (Element)getOrderListOutputDocPersonInfoShipToElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		getOrderLineAttributes.put(personInfoShipToExtn_LT,personInfoShipToExtnElement);

		Element orderPriceInfoElement = (Element)getOrderListOutputDocOrderElement.getElementsByTagName(XPXLiterals.E_PRICE_INFO).item(0);
		getOrderLineAttributes.put(XPXLiterals.E_PRICE_INFO,orderPriceInfoElement);

		return getOrderLineAttributes;         
	}

	private Document updateCreateOrderExistingDoc(Document createOrderExistingDoc, ArrayList thirdPartyList) {


		Element createOrderExistingDocOrderLines = (Element)createOrderExistingDoc.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);

		/* for(int i=0; i<thirdPartyList.size(); i++)
                       {*/
			Document orderLineInput = (Document)thirdPartyList.get(thirdPartyList.size()-1);
                       importElement(createOrderExistingDocOrderLines,orderLineInput.getDocumentElement());
                       // }

		return createOrderExistingDoc;
	}

	private Document createChangeOrderStatusInputDoc(ArrayList changeOrderStatusOrderLineInput,String orderHeaderKey)
	{

		Document changeOrderStatusInputDoc = createDocument(XPXLiterals.E_ORDER_STATUS_CHANGE);
		changeOrderStatusInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, orderHeaderKey);
		changeOrderStatusInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_IGNORE_TRANS_DEPENDENCIES, XPXLiterals.BOOLEAN_FLAG_Y);
		changeOrderStatusInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_MODIFICATION_REASON_CODE,XPXLiterals.CHANGE_FULFILLMENT_TYPE);
		changeOrderStatusInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_MODIFICATION_REASON_TEXT,XPXLiterals.REASON_TEXT);
		changeOrderStatusInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_TRANSACTION_ID,XPXLiterals.UPDATE_LEGACY_TRANS_ID);

		Element orderLinesElement = changeOrderStatusInputDoc.createElement(XPXLiterals.E_ORDER_LINES);

		for(int i=0; i < changeOrderStatusOrderLineInput.size(); i++)
		{
			Document orderLineElement = (Document) changeOrderStatusOrderLineInput.get(i);
			importElement(orderLinesElement,orderLineElement.getDocumentElement());
		}

		/*Element orderLineElement = (Element)orderLinesElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE).item(0);
          orderLineElement.setAttribute(XPXLiterals.A_BASE_DROP_STATUS, XPXLiterals.STATUS_CREATED_ID);*/

		changeOrderStatusInputDoc.getDocumentElement().appendChild(orderLinesElement);


		return changeOrderStatusInputDoc;

	}

	private Document createOrderLineInputForChangeOrderStatus(String changeOrderInputDocOrderLineKey, String existingPrimeLineNo, String transactionalUOM, String quantity) {

		Document orderLineInputDoc = createDocument(XPXLiterals.E_ORDER_LINE);
		//orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_LINE_KEY, changeOrderInputDocOrderLineKey);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_PRIME_LINE_NO, existingPrimeLineNo);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_SUB_LINE_NO, XPXLiterals.CONSTANT_1);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_BASE_DROP_STATUS,XPXLiterals.STATUS_CREATED_ID);
		//orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CHANGE_FOR_ALL_AVAIL_QTY,XPXLiterals.BOOLEAN_FLAG_Y);

		Element orderLineTranQuantity = orderLineInputDoc.createElement(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY);
		orderLineTranQuantity.setAttribute(XPXLiterals.A_QUANTITY, quantity);
		orderLineTranQuantity.setAttribute(XPXLiterals.A_TRANSACTIONAL_UOM,transactionalUOM);
		orderLineInputDoc.getDocumentElement().appendChild(orderLineTranQuantity);

		return orderLineInputDoc;

	}

	private Document createOrderLineInputToNullifyOrderQty(String changeOrderInputDocType, String changeOrderInputDocEntCode,
			String chainedOrderOLK, String nullifiedOrderQty) {

		Document  orderLineInputDoc = createDocument(XPXLiterals.E_ORDER_LINE);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ACTION,XPXLiterals.MODIFY);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_LINE_KEY,chainedOrderOLK);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDERED_QTY, nullifiedOrderQty);

		return orderLineInputDoc;

	}

	private HashMap getchainedOHKAndType(Document outputOrderLineListDoc) {

		HashMap chainedOHKAndType = new HashMap();


		Element getOrderLineListOutputDocRoot = outputOrderLineListDoc.getDocumentElement();
		//Element getOrderLineListOutputDocOrder = (Element)getOrderLineListOutputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER).item(0);
		//Element getOrderDetailsDocOrderLinesElement = (Element)getOrderLineListOutputDocOrder.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);
		NodeList getOrderDetailsDocOrderLineList = getOrderLineListOutputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINE);
		for(int i=0; i<getOrderDetailsDocOrderLineList.getLength();i++)
		{
			Element getOrderDetailsDocOrderLine = (Element)getOrderDetailsDocOrderLineList.item(i);

			Element getOrderDetailsOrderLineOrder =  (Element)getOrderDetailsDocOrderLine.getElementsByTagName(XPXLiterals.E_ORDER).item(0);
			String documentType =   getOrderDetailsOrderLineOrder.getAttribute(XPXLiterals.A_DOCUMENT_TYPE);
			String enterpriseCode = getOrderDetailsOrderLineOrder.getAttribute(XPXLiterals.A_ENTERPRISE_CODE);
			String orderType = getOrderDetailsOrderLineOrder.getAttribute(XPXLiterals.A_ORDER_TYPE);

			if(orderType.equalsIgnoreCase(XPXLiterals.THIRD_PARTY_ORDER)
					&& !chainedOHKAndType.containsKey(XPXLiterals.THIRD_PARTY_ORDER))
				//&& !getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_STATUS).equalsIgnoreCase(XPXLiterals.STATUS_CANCELLED))
				{
				String chainedOrderHeaderKey = getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);

				Document changeOrderInputOrderHeaderDoc = getchangeOrderInputOrderHeaderDoc(chainedOrderHeaderKey,documentType,enterpriseCode);

				chainedOHKAndType.put(XPXLiterals.THIRD_PARTY_ORDER, changeOrderInputOrderHeaderDoc);

				}

			if(orderType.equalsIgnoreCase(XPXLiterals.DIRECT_ORDER)
					&& !chainedOHKAndType.containsKey(XPXLiterals.DIRECT_ORDER))
				//&& !getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_STATUS).equalsIgnoreCase(XPXLiterals.STATUS_CANCELLED))
			{
				String chainedOrderHeaderKey = getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
				/*Element getOrderDetailsOrderLineOrder =  (Element)getOrderDetailsDocOrderLine.getElementsByTagName(XPXLiterals.E_ORDER).item(0);
            		  String documentType =   getOrderDetailsOrderLineOrder.getAttribute(XPXLiterals.A_DOCUMENT_TYPE);
            		  String enterpriseCode = getOrderDetailsOrderLineOrder.getAttribute(XPXLiterals.A_ENTERPRISE_CODE);*/
				Document changeOrderInputOrderHeaderDoc = getchangeOrderInputOrderHeaderDoc(chainedOrderHeaderKey,documentType,enterpriseCode);

				chainedOHKAndType.put(XPXLiterals.DIRECT_ORDER, changeOrderInputOrderHeaderDoc);

			}

			if(orderType.equalsIgnoreCase(XPXLiterals.STOCK_ORDER)
					&& !chainedOHKAndType.containsKey(XPXLiterals.STOCK_ORDER))
				//&& !getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_STATUS).equalsIgnoreCase(XPXLiterals.STATUS_CANCELLED))
			{
				String chainedOrderHeaderKey = getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
				/* Element getOrderDetailsOrderLineOrder =  (Element)getOrderDetailsDocOrderLine.getElementsByTagName(XPXLiterals.E_ORDER).item(0);
            		  String documentType =   getOrderDetailsOrderLineOrder.getAttribute(XPXLiterals.A_DOCUMENT_TYPE);
            		  String enterpriseCode = getOrderDetailsOrderLineOrder.getAttribute(XPXLiterals.A_ENTERPRISE_CODE);*/
				Document changeOrderInputOrderHeaderDoc = getchangeOrderInputOrderHeaderDoc(chainedOrderHeaderKey,documentType,enterpriseCode);

				chainedOHKAndType.put(XPXLiterals.STOCK_ORDER, changeOrderInputOrderHeaderDoc);

			}

		}



		return chainedOHKAndType;
	}

	private Document getchangeOrderInputOrderHeaderDoc(String chainedOrderHeaderKey, String documentType, String enterpriseCode) {


		Document changeOrderInputDoc = createDocument(XPXLiterals.E_ORDER);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY,chainedOrderHeaderKey);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_DOCUMENT_TYPE, documentType);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTERPRISE_CODE, enterpriseCode);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ACTION, XPXLiterals.MODIFY);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OVERRIDE,XPXLiterals.BOOLEAN_FLAG_Y);

		Element OrderLines = changeOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINES);
		changeOrderInputDoc.getDocumentElement().appendChild(OrderLines);

		return changeOrderInputDoc;
	}

	private HashMap getChainedOrderLineKeys(Document outputOrderLineListDoc) {

		HashMap chainedOrderLines = new HashMap();

		Element getOrderLineListOutputDocRoot = outputOrderLineListDoc.getDocumentElement();
		//Element getOrderLineListOutputDocOrder = (Element)getOrderLineListOutputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER).item(0);
		//Element getOrderDetailsDocOrderLinesElement = (Element)getOrderLineListOutputDocOrder.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);
		NodeList getOrderDetailsDocOrderLineList = getOrderLineListOutputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINE);
		for(int i=0; i<getOrderDetailsDocOrderLineList.getLength();i++)
		{
			Element getOrderDetailsDocOrderLine = (Element)getOrderDetailsDocOrderLineList.item(i);
			if(!getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_STATUS).equalsIgnoreCase(XPXLiterals.STATUS_CANCELLED))
			{
				chainedOrderLines.put(getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.CHAINED_FROM_ORDER_LINE_KEY), getOrderDetailsDocOrderLine);  
			}
		}
		return chainedOrderLines;
	}

	private MultiValueMap getExistingOrderQty(Document getOrderListOutputDoc) {

		MultiValueMap orderedQty = new MultiValueMap();

		Element getOrderListOutputDocRoot = getOrderListOutputDoc.getDocumentElement();
		Element getOrderListOutputDocOrder = (Element)getOrderListOutputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER).item(0);
		Element getOrderDetailsDocOrderLinesElement = (Element)getOrderListOutputDocOrder.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);
		NodeList getOrderDetailsDocOrderLineList = getOrderDetailsDocOrderLinesElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE);
		for(int i=0; i<getOrderDetailsDocOrderLineList.getLength();i++)
		{
			Element getOrderDetailsDocOrderLine = (Element)getOrderDetailsDocOrderLineList.item(i);


			orderedQty.put(getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_ORDER_LINE_KEY),
					getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_ORDERED_QTY));
			orderedQty.put(getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_ORDER_LINE_KEY),
					getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_PRIME_LINE_NO));
			orderedQty.put(getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_ORDER_LINE_KEY),
					getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_SUB_LINE_NO));
			orderedQty.put(getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_ORDER_LINE_KEY),
					getOrderDetailsDocOrderLine.getAttribute(XPXLiterals.A_LINE_TYPE));

		}

		return orderedQty;
	}

	private Document createOrderLineListInputDocWithChainedOLK(String changeOrderInputDocOLK, String changeOrderInputDocOHK) {

		Document getOrderLineListInputDoc = createDocument(XPXLiterals.E_ORDER_LINE);

		Element getItemListInputDocRoot = getOrderLineListInputDoc.getDocumentElement();

		getItemListInputDocRoot.setAttribute(XPXLiterals.CHAINED_FROM_ORDER_LINE_KEY, changeOrderInputDocOLK);
		getItemListInputDocRoot.setAttribute(XPXLiterals.CHAINED_FROM_ORDER_HEADER_KEY, changeOrderInputDocOHK);

		return getOrderLineListInputDoc;
	}

	private String retrieveChainedOrderLineKey(Document orderLineListOutputDoc) {


		Element orderLineListOutputDocRoot = orderLineListOutputDoc.getDocumentElement();
		Element orderLine = (Element)orderLineListOutputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINE).item(0);

		String orderLineKey = orderLine.getAttribute(XPXLiterals.A_ORDER_LINE_KEY);

		return orderLineKey;

	}

	private boolean checkOrderTypeExistsBeforeChangeOrder(Document outputOrderLineListDoc, String lineType) {

		boolean doesOrderTypesExists = false;

		NodeList orderLineList = outputOrderLineListDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_ORDER_LINE);

		for(int i=0; i<orderLineList.getLength() ; i++)
		{
			Element orderLine = (Element)orderLineList.item(i);

			Element orderElement = (Element)orderLine.getElementsByTagName(XPXLiterals.E_ORDER).item(0);
			if(orderElement.getAttribute(XPXLiterals.A_ORDER_TYPE).equalsIgnoreCase(lineType))
			{
				doesOrderTypesExists = true;
				break;
			}
		}

		return doesOrderTypesExists;
	}

	private int getNoOfExistingOrderLines(Document getOrderListDoc) {

		int highestPrimeLineNo = 0;

		Element getOrderListDocRoot = getOrderListDoc.getDocumentElement();
		Element getOrderListDocOrder = (Element)getOrderListDocRoot.getElementsByTagName(XPXLiterals.E_ORDER).item(0);
		Element getOrderListOrderLines = (Element)getOrderListDocOrder.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);

		NodeList orderLineList = getOrderListOrderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);
		for (int i=0;i < orderLineList.getLength(); i++)
		{
			Element orderLine = (Element) orderLineList.item(i);

			int primeLineNo = Integer.parseInt(orderLine.getAttribute(XPXLiterals.A_PRIME_LINE_NO));

			if(primeLineNo > highestPrimeLineNo)
			{
				highestPrimeLineNo = primeLineNo;
			}
		}

		return highestPrimeLineNo;

	}

	private String retrieveSubLineNo(Document outputOrderLineListDoc) {

		String subLineNo = null;

		Element  outputOrderLineListDocRoot = outputOrderLineListDoc.getDocumentElement();

		Element outputOrderLine = (Element)outputOrderLineListDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINE).item(0);

		subLineNo = outputOrderLine.getAttribute(XPXLiterals.A_SUB_LINE_NO);

		return subLineNo;

	}

	private String retrievePrimeLineNo(Document outputOrderLineListDoc) {

		String primeLineNo = null;

		Element  outputOrderLineListDocRoot = outputOrderLineListDoc.getDocumentElement();

		Element outputOrderLine = (Element)outputOrderLineListDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINE).item(0);

		primeLineNo = outputOrderLine.getAttribute(XPXLiterals.A_PRIME_LINE_NO);

		return primeLineNo;

	}

	private Document invokeGetOrderList(YFSEnvironment env, Document getOrderListInputDoc) {

		Document getOrderListOutputDoc = null;
		Document getOrderDetailsDoc = null;

		try
		{
			env.setApiTemplate(XPXLiterals.GET_ORDER_LIST_API, getOrderListTemplate);
			getOrderListOutputDoc = api.invoke(env,XPXLiterals.GET_ORDER_LIST_API, getOrderListInputDoc);
			env.clearApiTemplate(XPXLiterals.GET_ORDER_LIST_API);

			//getOrderDetailsDoc = getDocument((Element) getOrderListOutputDoc.getElementsByTagName(
					//		    XPXLiterals.E_ORDER).item(0), true);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return getOrderListOutputDoc;
	}

	private Document getDocument(Element inputElement, boolean deep)
	throws IllegalArgumentException, Exception{

//		Validate input element
if (inputElement == null) {
	throw new IllegalArgumentException(
			"Input element cannot be null in "
			+ "XmlUtils.getDocument method");
}

// Create a new document
Document outputDocument = getDocument();

// Import data from input element and
// set as root element for output document
outputDocument.appendChild(outputDocument
		.importNode(inputElement, deep));

// return output document
return outputDocument;
	}



	private Document getDocument() throws ParserConfigurationException {
		// Create a new Document Bilder Factory instance
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
		.newInstance();

		// Create new document builder
		DocumentBuilder documentBuilder = documentBuilderFactory
		.newDocumentBuilder();

		// Create and return document object
		return documentBuilder.newDocument();
	}


	private Document createGetOrderListInputDoc(String changeOrderInputDocOHK) {

		Document getOrderListInputDoc = createDocument(XPXLiterals.E_ORDER);

		getOrderListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, changeOrderInputDocOHK);

		return getOrderListInputDoc;
	}

	private Document createOrderLineInput(String changeOrderInputDocDocType, String changeOrderInputDocDocEntCode,
			String orderLineKey, String orderedQty, String primeLineNumber, String subLineNo, String changeOrderInputDocOHK) {

		/*Document changeOrderInputDoc = createDocument(XPXLiterals.E_ORDER);
     changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY,existingChainedFromOHK);
     changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_DOCUMENT_TYPE,changeOrderInputDocDocType);
     changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTERPRISE_CODE, changeOrderInputDocDocEntCode);
     changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ACTION, XPXLiterals.MODIFY);
     changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OVERRIDE,XPXLiterals.BOOLEAN_FLAG_Y);

     Element OrderLines = changeOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINES);
     changeOrderInputDoc.getDocumentElement().appendChild(OrderLines);*/

		Document  orderLineInputDoc = createDocument(XPXLiterals.E_ORDER_LINE);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ACTION,XPXLiterals.MODIFY);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_LINE_KEY,orderLineKey);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDERED_QTY, orderedQty);

		Element orderLineTranQuantity = orderLineInputDoc.createElement(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY);
		orderLineTranQuantity.setAttribute(XPXLiterals.A_ORDERED_QTY, orderedQty);
		orderLineInputDoc.getDocumentElement().appendChild(orderLineTranQuantity); 

		Element chainedFromElement = orderLineInputDoc.createElement(XPXLiterals.E_CHAINED_FROM);
		chainedFromElement.setAttribute(XPXLiterals.A_DOCUMENT_TYPE, changeOrderInputDocDocType);
		chainedFromElement.setAttribute(XPXLiterals.A_ENTERPRISE_CODE, changeOrderInputDocDocEntCode);
		chainedFromElement.setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, changeOrderInputDocOHK);
		chainedFromElement.setAttribute(XPXLiterals.A_PRIME_LINE_NO, primeLineNumber);
		chainedFromElement.setAttribute(XPXLiterals.A_SUB_LINE_NO, subLineNo);
		orderLineInputDoc.getDocumentElement().appendChild(chainedFromElement);

		/* OrderLines.appendChild(orderLineInputDoc);*/

		return orderLineInputDoc;

	}

	private String retrieveChainedOrderHeaderKey(Document outputOrderLineListDoc) {

		String chainedOrderHeaderKey = null;

		/*try {
		log.debug(TrialClass.getXMLString(outputOrderLineListDoc));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/

		Element  outputOrderLineListDocRoot = outputOrderLineListDoc.getDocumentElement();
		Element outputOrderLine = (Element)outputOrderLineListDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINE).item(0);
		chainedOrderHeaderKey = outputOrderLine.getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
		return chainedOrderHeaderKey;

	}

	private String retrieveOrderedQty(Document outputOrderLineListDoc) {

		String orderedQty = null;

		Element  outputOrderLineListDocRoot = outputOrderLineListDoc.getDocumentElement();

		Element outputOrderLine = (Element)outputOrderLineListDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINE).item(0);

		orderedQty = outputOrderLine.getAttribute(XPXLiterals.A_ORDERED_QTY);

		return orderedQty;

	}

	private Document createGetOrderLineListInputDoc(String changeOrderInputDocOrderLineKey) {


		Document getOrderLineListInputDoc = createDocument(XPXLiterals.E_ORDER_LINE);

		Element getItemListInputDocRoot = getOrderLineListInputDoc.getDocumentElement();

		getItemListInputDocRoot.setAttribute(XPXLiterals.A_ORDER_LINE_KEY, changeOrderInputDocOrderLineKey);

		return getOrderLineListInputDoc;
	}

	private String generateWebLineNumber(long uniqueSequenceNo) {
		String webLineNumber = "";
		String uniqueSequence = "";
		String formatted = "";

		int uniqueSequenceLength = 8;
		int keyLength = String.valueOf(uniqueSequenceNo).length();
		log.debug("KeyLength = "+keyLength);
		formatted = String.format("%08d", uniqueSequenceNo); 

		log.debug("Number with leading zeros: " + formatted); 

		webLineNumber = "A"+formatted;

		return webLineNumber;
	}

	private Element stampLineTypeOfCustomerOrderLine(YFSEnvironment env, String orderLineType, String orderHeaderKey, String orderLineKey, 
			String documentType, String enterpriseCode, String webLineNumber) {

		Document changeOrderOutDoc = null;
		Element orderLineExtn = null;

		Document changeOrderInputDoc = createDocument(XPXLiterals.E_ORDER);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY,orderHeaderKey);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_DOCUMENT_TYPE, documentType);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTERPRISE_CODE, enterpriseCode);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ACTION, XPXLiterals.MODIFY);
		changeOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_OVERRIDE,XPXLiterals.BOOLEAN_FLAG_Y);

		Element OrderLines = changeOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINES);
		changeOrderInputDoc.getDocumentElement().appendChild(OrderLines);

		Element  orderLineInputDoc = changeOrderInputDoc.createElement(XPXLiterals.E_ORDER_LINE);
		orderLineInputDoc.setAttribute(XPXLiterals.A_ORDER_LINE_KEY, orderLineKey);
		orderLineInputDoc.setAttribute(XPXLiterals.A_LINE_TYPE,orderLineType);

		Element orderLineInputExtn = changeOrderInputDoc.createElement(XPXLiterals.E_EXTN);
		orderLineInputExtn.setAttribute(XPXLiterals.A_WEB_LINE_NUMBER, webLineNumber);
		orderLineInputDoc.appendChild(orderLineInputExtn);

		OrderLines.appendChild(orderLineInputDoc);

		try
		{
			env.setApiTemplate(XPXLiterals.CHANGE_ORDER_API, changeOrderTemplate);	
			changeOrderOutDoc = api.invoke(env,XPXLiterals.CHANGE_ORDER_API,changeOrderInputDoc);
			env.clearApiTemplate(XPXLiterals.CHANGE_ORDER_API);

			Element changeOrderOutDocRoot = changeOrderOutDoc.getDocumentElement();
			Element changeOrderOutDocOrderLines = (Element)changeOrderOutDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);
			NodeList changeOrderOutDocOrderLinesList = changeOrderOutDocOrderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);
			for(int b=0; b<changeOrderOutDocOrderLinesList.getLength();b++)
			{
				Element changeOrderOutDocOrderLine = (Element)changeOrderOutDocOrderLinesList.item(b);

				if(changeOrderOutDocOrderLine.getAttribute(XPXLiterals.A_ORDER_LINE_KEY).equalsIgnoreCase(orderLineKey))
				{
					orderLineExtn = (Element)changeOrderOutDocOrderLine.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
					break;
				}

			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return orderLineExtn;

	}

	private Document createOrderLineInputForChangeOrder(String orderLineType,String orderedQty,Element orderLineExtn,
			String itemId, String unitOfMeasure, String productClass, String documentType, String enterpriseCode,
			String changeOrderInputDocOHK, String primeLineNo, String subLineNo, String transactionalUOM) {


		Document orderLineInputDoc = createDocument(XPXLiterals.E_ORDER_LINE);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ACTION, XPXLiterals.CREATE);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_LINE_TYPE,orderLineType);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDERED_QTY, orderedQty);
		orderLineInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_UNIT_OF_MEASURE,transactionalUOM);

		importElement(orderLineInputDoc.getDocumentElement(),orderLineExtn);


		Element orderLineTranQuantity = orderLineInputDoc.createElement(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY);
		orderLineTranQuantity.setAttribute(XPXLiterals.A_ORDERED_QTY, orderedQty);
		orderLineTranQuantity.setAttribute(XPXLiterals.A_TRANSACTIONAL_UOM, transactionalUOM);
		orderLineInputDoc.getDocumentElement().appendChild(orderLineTranQuantity);


		Element item = orderLineInputDoc.createElement(XPXLiterals.E_ITEM);
		item.setAttribute(XPXLiterals.A_ITEM_ID, itemId);
		item.setAttribute(XPXLiterals.A_UNIT_OF_MEASURE, unitOfMeasure);
		item.setAttribute(XPXLiterals.A_PRODUCT_CLASS, productClass);
		if(customerItem != null && customerItem.trim().length() != 0)
		{
		item.setAttribute(XPXLiterals.A_CUSTOMER_ITEM, customerItem);
		}
		orderLineInputDoc.getDocumentElement().appendChild(item);

		Element chainedFromElement = orderLineInputDoc.createElement(XPXLiterals.E_CHAINED_FROM);
		chainedFromElement.setAttribute(XPXLiterals.A_DOCUMENT_TYPE, documentType);
		chainedFromElement.setAttribute(XPXLiterals.A_ENTERPRISE_CODE, enterpriseCode);
		chainedFromElement.setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, changeOrderInputDocOHK);
		chainedFromElement.setAttribute(XPXLiterals.A_PRIME_LINE_NO, primeLineNo);
		chainedFromElement.setAttribute(XPXLiterals.A_SUB_LINE_NO, subLineNo);
		orderLineInputDoc.getDocumentElement().appendChild(chainedFromElement);



		return orderLineInputDoc;

	}

	private boolean checkOrderLineType(Document outputOrderLineListDoc, String orderLineType) {

		boolean orderLineTypeExists = false;

		Element orderLineListRoot = outputOrderLineListDoc.getDocumentElement();

		NodeList orderLines = orderLineListRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINE);

		for(int i=0;i<orderLines.getLength();i++)
		{
			Element orderLine = (Element)orderLines.item(i);

			String lineType = orderLine.getAttribute(XPXLiterals.A_LINE_TYPE);

			if(lineType.equalsIgnoreCase(orderLineType))
			{
				orderLineTypeExists = true;
				break;
			}
		}

		return orderLineTypeExists;
	}

	private Document createOrderLineListInputDoc(String chainedFromOrderHeaderKey) {

		Document getOrderLineListInputDoc = createDocument(XPXLiterals.E_ORDER_LINE);

		Element getItemListInputDocRoot = getOrderLineListInputDoc.getDocumentElement();

		getItemListInputDocRoot.setAttribute(XPXLiterals.CHAINED_FROM_ORDER_HEADER_KEY, chainedFromOrderHeaderKey);

		return getOrderLineListInputDoc;

	}

	private String getOrderLineType(Document outputItemDoc) {

		String orderLineType = null;

		Element itemListDocRoot = outputItemDoc.getDocumentElement();
		Element itemElement = (Element)itemListDocRoot.getElementsByTagName(XPXLiterals.E_ITEM).item(0);
		Element itemExtnElement = (Element)itemElement.getElementsByTagName(XPXLiterals.E_EXTN).item(0);
		String itemTypeValue = itemExtnElement.getAttribute(XPXLiterals.A_EXTN_ITEM_TYPE);

		//Stamping the item type as the line type. If not present, the default value set is STOCK_ORDER

		if(itemTypeValue!=null && itemTypeValue.trim().length()!=0)
		{
			orderLineType = itemTypeValue;
		}
		else
		{
			orderLineType = XPXLiterals.STOCK_ORDER;
		}
		log.debug("The value of the line type going to be returned is: "+orderLineType);
		return orderLineType;
	}

	private Document createChainedOrderInput(ArrayList thirdPartyList, String documentType, String enterpriseCode,
			String buyerOrganizationCode, String orderType, Element orderExtn, Element personInfoShipTo, Element personInfoBillTo,
			Element orderPriceInfo, Element personInfoShipToExtn, Element personInfoBillToExtn) {

		Document createOrderDoc = createDocument(XPXLiterals.E_ORDER);
		createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_DOCUMENT_TYPE, documentType);
		createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_ENTERPRISE_CODE, enterpriseCode);
		createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE,buyerOrganizationCode);
		createOrderDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_TYPE,orderType);

		//importElement(createOrderDoc.getDocumentElement(),orderExtn);
		//importElement(createOrderDoc.getDocumentElement(), orderPriceInfo);
		//importElement(personInfoShipTo,personInfoShipToExtn);
		//importElement(createOrderDoc.getDocumentElement(),personInfoShipTo);
		//importElement(personInfoBillTo,personInfoBillToExtn);
		//importElement(createOrderDoc.getDocumentElement(),personInfoBillTo);


		Element orderLinesElement = createOrderDoc.createElement(XPXLiterals.E_ORDER_LINES);
		createOrderDoc.getDocumentElement().appendChild(orderLinesElement);

		for(int i=0; i<thirdPartyList.size(); i++)
		{
			Document orderLineDoc = (Document)thirdPartyList.get(i);			
			importElement(orderLinesElement,orderLineDoc.getDocumentElement());			

		}

		return createOrderDoc;

	}

	private Document createorderLineDoc(String orderedQty, String transactionalUOM, String itemId, String unitOfMeasure,
			String productClass, String documentType, String enterpriseCode, String orderHeaderKey, String primeLineNo, String subLineNo,
			Element orderLineExtn, Element itemExtn, Element linePriceInfo, String orderLineType) {

		Document orderLineElementDoc = createDocument(XPXLiterals.E_ORDER_LINE);
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_LINE_TYPE, orderLineType);
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDERED_QTY, orderedQty);
		orderLineElementDoc.getDocumentElement().setAttribute(XPXLiterals.A_UNIT_OF_MEASURE,transactionalUOM);
		importElement(orderLineElementDoc.getDocumentElement(),orderLineExtn);
		importElement(orderLineElementDoc.getDocumentElement(),linePriceInfo);

		Element orderLineTranQtyElement = orderLineElementDoc.createElement(XPXLiterals.E_ORDER_LINE_TRAN_QUANTITY);
		orderLineTranQtyElement.setAttribute(XPXLiterals.A_ORDERED_QTY, orderedQty);
		orderLineTranQtyElement.setAttribute(XPXLiterals.A_TRANSACTIONAL_UOM, transactionalUOM);
		orderLineElementDoc.getDocumentElement().appendChild(orderLineTranQtyElement);

		Element itemElement = orderLineElementDoc.createElement(XPXLiterals.E_ITEM);
		itemElement.setAttribute(XPXLiterals.A_ITEM_ID, itemId);
		itemElement.setAttribute(XPXLiterals.A_UNIT_OF_MEASURE, unitOfMeasure);
		itemElement.setAttribute(XPXLiterals.A_PRODUCT_CLASS, productClass);
		if(customerItem != null && customerItem.trim().length() != 0)
		{
			itemElement.setAttribute(XPXLiterals.A_CUSTOMER_ITEM, customerItem);
		}
		orderLineElementDoc.getDocumentElement().appendChild(itemElement);
		importElement(itemElement,itemExtn);

		Element chainedFromElement = orderLineElementDoc.createElement(XPXLiterals.E_CHAINED_FROM);
		chainedFromElement.setAttribute(XPXLiterals.A_DOCUMENT_TYPE, documentType);
		chainedFromElement.setAttribute(XPXLiterals.A_ENTERPRISE_CODE, enterpriseCode);
		chainedFromElement.setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, orderHeaderKey);
		chainedFromElement.setAttribute(XPXLiterals.A_PRIME_LINE_NO, primeLineNo);
		chainedFromElement.setAttribute(XPXLiterals.A_SUB_LINE_NO, subLineNo);
		orderLineElementDoc.getDocumentElement().appendChild(chainedFromElement);

		return orderLineElementDoc;
	}

	private Document createItemListInputDoc(String itemId) {

		Document getItemListInputDoc = createDocument(XPXLiterals.E_ITEM);

		Element getItemListInputDocRoot = getItemListInputDoc.getDocumentElement();

		getItemListInputDocRoot.setAttribute(XPXLiterals.A_ITEM_ID, itemId);

		return getItemListInputDoc;
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
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
}


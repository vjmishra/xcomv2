package com.xpedx.nextgen.item.price;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.priceAndAvailabilityService.XPXItem;
import com.xpedx.nextgen.priceAndAvailabilityService.XPXPriceAndAvailability;
import com.xpedx.nextgen.priceAndAvailabilityService.XPXPriceandAvailabilityUtil;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfs.japi.YFSUserExitException;
import com.yantra.ypm.japi.ue.YPMOverrideGetItemPriceUE;

public class XPEDXOverrideGetItemPriceUE implements YPMOverrideGetItemPriceUE {

	@Override
	public Document overrideGetItemPrice(YFSEnvironment env, Document arg1)
			throws YFSUserExitException {
		SCXmlUtil.getString(arg1);
		log.debug("overrideGetItemPrice method in XPEDXOverrideGetItemPriceUE : "+SCXmlUtil.getString(arg1));
		
		Element itemPriceElement = arg1.getDocumentElement();
		//Fetching CustomerID for PNA call
		String customerId = itemPriceElement.getAttribute("CustomerID");
		Node lineItemsList = itemPriceElement.getFirstChild();
		NodeList lineItems = lineItemsList.getChildNodes();
		try {
			String orderHeaderKey = itemPriceElement.getAttribute("OrderReference");
			String isPnACall=null;
			String isSpecialItem =null;
			
			// <OrderLineKey, LineNumber>
			HashMap<String,String> orderlineKeyNumberMap=new HashMap<String,String>();
			// <OrderLineKey, UnitPrice>
			HashMap<String,String> unitPricePricingUOMMap=new HashMap<String,String>();
			// <OrderLineKey, ExtendedPrice>
			HashMap<String,String> extendedPriceMap=new HashMap<String,String>();
			// <OrderLineKey, UOM>
			HashMap<String,String> pricingUOMMap=new HashMap<String,String>();
			HashMap<String,String> reqUnitPrice=new HashMap<String,String>();
			HashMap<String,String> isPriceLockMap=new HashMap<String,String>();
			// default values set
			String draftOrderFlag="Y";
			String hasPendingChanges="N";
			HashMap<String,String> primeLineNumMap = new HashMap<String,String>();
			String isDiscountCalculate=null;
			//String isDiscountCalculate=null;
			String ischangeOrderInprogress=null;
			if (env instanceof ClientVersionSupport) {
				ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
				HashMap map = clientVersionSupport.getClientProperties();
				if (map != null && map.size()>0) {
					// This flag is set if the order has penalty charges and they come as special item
					isSpecialItem=(String)map.get("isSpecialItem");
					// This flag is set if PNA call is needed. For catalog page's getCompleteOrderDetails() this flag is not needed.
					isPnACall=(String)map.get("isPnACall");
					isDiscountCalculate=(String)map.get("isDiscountCalculate");
					//isDiscountCalculate=(String)map.get("isDiscountCalculate");
					ischangeOrderInprogress=(String)map.get("ischangeOrderInprogress");
					draftOrderFlag=(String)map.get("draftOrderFlag");
					hasPendingChanges=(String)map.get("hasPendingChanges");
					primeLineNumMap = (HashMap<String,String>)map.get("primeLineNoMap");
					reqUnitPrice = (HashMap<String,String>)map.get("reqUnitPrice");
					isPriceLockMap=(HashMap<String,String>)map.get("isPriceLockMap");
				}
			}

			// The below section calls PNA and sets in the OOTB fields (needed for discount calculation) and maps
			if ((isPnACall !=null && "true".equalsIgnoreCase(isPnACall)) &&
					!(isSpecialItem != null && "true".equals(isSpecialItem))  &&
					!(YFCCommon.isVoid(orderHeaderKey))
					 && ischangeOrderInprogress == null)
			{
				ArrayList<XPXItem> inputItems = new ArrayList<XPXItem>();
				YFCDocument inputDocument = YFCDocument.createDocument("Order");
				YFCElement inputElement = inputDocument.getDocumentElement();
				inputElement.setAttribute("OrderHeaderKey", orderHeaderKey);
				
				// the below getOrderList(...) is made for fetching some additional field needed like 
				// Buyer Org (shipTo), DraftOrderFlag, hasPendingChanges, transactional UOM and Qty
				// hasPendingChanges will be N if there are no changes in order or if it is a draftOrder
				/*env.setApiTemplate(
								"getOrderList",
								SCXmlUtil
								.createFromString(""
										+ "<OrderList><Order HasPendingChanges='' BuyerOrganizationCode='' DraftOrderFlag='' ><OrderLines><OrderLine>"
										+ "<OrderLineTranQuantity OrderedQty='' TransactionalUOM=''></OrderLineTranQuantity>"
										+ "</OrderLine></OrderLines></Order></OrderList>"));
				api = YIFClientFactory.getInstance().getApi();

				Document orderListDocument = api.invoke(env, "getOrderList",
						inputDocument.getDocument());*/
				/*Element orderListElement = orderListDocument
						.getDocumentElement();
				NodeList orderListNodes = orderListElement.getChildNodes();*/
				//int length = orderListNodes.getLength();
				int lineNumber=1;
				/*for (int i = 0; i < length; i++) {
					Node orderNode = orderListNodes.item(i);
					Element orderElement=(Element)orderNode;
					
					// If customerID is BillTo fetch the ShipTo customer from BuyerOrganization
				//	String tempCustomerId=orderElement.getAttribute("BuyerOrganizationCode");
					draftOrderFlag=orderElement.getAttribute("DraftOrderFlag");
					hasPendingChanges=orderElement.getAttribute("HasPendingChanges");
					//if(tempCustomerId !=null && tempCustomerId.length() >0)
					//{
				//		customerId=tempCustomerId;
				//	}
					Node orderLinesNode = orderNode.getFirstChild();
					NodeList orderLineNodes = orderLinesNode.getChildNodes();*/
					//int length1 = orderLineNodes.getLength();
					int length1=lineItems.getLength();
					int lineID=0;
					for (int j = 0; j < length1; j++) {
						//Node orderLineNode = orderLineNodes.item(j);
						Element lineItemEle = (Element) lineItems.item(j);
						if(lineItemEle != null)
						{
							//Element orderLineTranQtyEle = (Element) orderLineNode
							//		.getFirstChild();
							String orderLineKey=lineItemEle.getAttribute("LineID");		
							//String orderQty="";
							//String transactionalUOM="";
							//if(qtyUOMMap != null)
							//{
							//	Element orderLineTranQtyEleMap=qtyUOMMap.get(orderLineKey);
							//	orderQty=orderLineTranQtyEleMap.getAttribute("OrderedQty");
							//	transactionalUOM=orderLineTranQtyEleMap.getAttribute("TransactionalUOM");
							//}
							//if(YFCCommon.isVoid(orderQty))
							//{
							//	orderQty=lineItemEle
							//	.getAttribute("Quantity");
							//	if(YFCCommon.isVoid(orderQty))
							//	{
							//		orderQty=orderLineTranQtyEle.getAttribute("OrderedQty");
							//	}
								
							//}
							/*if(YFCCommon.isVoid(transactionalUOM))
							{
								transactionalUOM=orderLineTranQtyEle
								.getAttribute("TransactionalUOM");
								
							}*/
							//We are reading j+1 as primeLineNumber starts with 1
							if(YFCCommon.isVoid(orderLineKey))
							{
								orderLineKey=""+lineID;
								lineID +=1;
							}
							String mapVal = (String)primeLineNumMap.get(orderLineKey);
							if(mapVal!=null){
								int primeLineNum = Integer.parseInt(mapVal);
								lineNumber = primeLineNum;
							}
							
							XPXItem item1 = new XPXItem();
							item1.setLegacyProductCode(lineItemEle
									.getAttribute("ItemID"));
							item1.setRequestedQtyUOM(lineItemEle
									.getAttribute("UnitOfMeasure"));
							item1.setRequestedQty(lineItemEle
									.getAttribute("Quantity"));
							item1.setLineNumber(""+lineNumber);
							lineItemEle.setAttribute("LineNumber", ""+lineNumber);
							//the inputs added are needed for genertating PNA call input XML
							inputItems.add(item1);
							
							
							orderlineKeyNumberMap.put(""+lineNumber+lineItemEle
									.getAttribute("ItemID"),orderLineKey );
							lineNumber +=lineID;
						}
					}
				//}
				// PNA call
				XPXPriceAndAvailability pna = XPXPriceandAvailabilityUtil.getPriceAndAvailability(env, inputItems, customerId);
				//added for jira 2885
					if(pna != null){
					String responseXML=pna.getResponseXml();
					storeResponseString(env,responseXML,orderHeaderKey);
					}
					//end of jira 2885
					if (pna != null && (!"F".equals(pna.getTransactionStatus()))) {
					
					Vector<XPXItem> pAndAitems = pna.getItems();
					for(int idx=0;idx<lineItems.getLength();idx++){
						Element lineItemEle = (Element)lineItems.item(idx);
						String lineNumberNew=lineItemEle.getAttribute("LineNumber");
						int lineNum=Integer.parseInt(lineNumberNew);
						String itemId=lineItemEle.getAttribute("ItemID");
						lineItemEle.removeAttribute("LineNumber");
						boolean isPriceApplied=false;
						String isPriceLock=isPriceLockMap.get(orderlineKeyNumberMap.get(""+lineNum+itemId));
						if("Y".equals(isPriceLock))
						{
							String unitPrice=reqUnitPrice.get(orderlineKeyNumberMap.get(""+lineNum+itemId));
							lineItemEle.setAttribute("ListPrice", unitPrice);
							lineItemEle.setAttribute("UnitPrice", unitPrice);
							lineItemEle.setAttribute("LinePrice", unitPrice);
							BigDecimal extendedPriceDB=new BigDecimal(unitPrice);
							extendedPriceDB =extendedPriceDB.multiply(new BigDecimal(lineItemEle.getAttribute("Quantity")));
							extendedPriceMap.put(orderlineKeyNumberMap.get(""+lineNum+itemId), extendedPriceDB.toString());
							continue;
						}
						for (int i = 0; i < pAndAitems.size(); i++) {							
							XPXItem paitem = pAndAitems.get(i);
							int pnAlineNum=Integer.parseInt(paitem.getLineNumber());
								if(lineNumberNew !=null && itemId !=null)
								{
									if(lineNum == pnAlineNum && itemId.equals(paitem.getLegacyProductCode()))
									{  
										//Based on ItemID and LineNum UnitPrice, extendedPrice and UOM are set in a map
										unitPricePricingUOMMap.put(orderlineKeyNumberMap.get(""+lineNum+itemId), paitem.getUnitPricePerPricingUOM());
										extendedPriceMap.put(orderlineKeyNumberMap.get(""+lineNum+itemId), paitem.getExtendedPrice());
										pricingUOMMap.put(orderlineKeyNumberMap.get(""+lineNum+itemId), paitem.getPricingUOM());
										
										//Based on ItemID and LineNum ListPrice, UnitPrice and LinePrice are set in the xml
										lineItemEle.setAttribute("ListPrice", paitem
												.getUnitPricePerRequestedUOM());
										lineItemEle.setAttribute("UnitPrice", paitem
												.getUnitPricePerRequestedUOM());
										lineItemEle.setAttribute("LinePrice", paitem
												.getUnitPricePerRequestedUOM());
										isPriceApplied=true;
										break;
									}
								}
						}
						if(isPriceApplied == false)
						{
							lineItemEle.setAttribute("ListPrice", "0");
							lineItemEle.setAttribute("UnitPrice", "0");
							lineItemEle.setAttribute("LinePrice","0");
						}
					}
				}//Delete the PNA Response from UE attributes table if PNA fails
				else
				{
					for(int idx=0;idx<lineItems.getLength();idx++)
					{
						Element lineItemEle = (Element)lineItems.item(idx);	
						lineItemEle.setAttribute("ListPrice", "0");
						lineItemEle.setAttribute("UnitPrice", "0");
						lineItemEle.setAttribute("LinePrice","0");
					}
					//added for jira 2885
					if(pna == null){
						deleteResponseFromUEAttrTable( env, orderHeaderKey);
					}
					//end for jira 2885
				}
					
			}
			else if(isSpecialItem != null && "true".equals(isSpecialItem))
			{
				if(!YFCCommon.isVoid(orderHeaderKey))
				{
						Document inputDocument = SCXmlUtil.createDocument("Order");
						Element inputElement = inputDocument.getDocumentElement();				
						inputElement.setAttribute("OrderHeaderKey", orderHeaderKey);
						
						String inputXML = SCXmlUtil.getString(inputDocument);
				
						env.setApiTemplate(
										"getOrderList",
										SCXmlUtil.createFromString(""
														+"<OrderList>"
														+ "<Order OrderHeaderKey=\"\"> "
														+ "<OrderLines TotalNumberOfRecords=\"\">"
														+ "<OrderLine OrderedQty=\"\"  OrderLineKey=\"\" >"
														+"<LineOverallTotals UnitPrice=\"\" />"
														+"</OrderLine>"
														+"</OrderLines>"
														+"</Order>"
														+"</OrderList>"));
		
						api = YIFClientFactory.getInstance().getApi();
						Document orderDocument =api.invoke(env, "getOrderList", inputDocument);
						HashMap<String,String> unitPriceMap=new HashMap<String,String>();
						if(orderDocument != null){
							
							Element orderElement = orderDocument.getDocumentElement();
							NodeList orderLinesList=orderElement.getChildNodes();
							if(orderLinesList != null)
							{
								Node orderLineNode= orderElement.getChildNodes().item(0);
								if(orderLineNode != null)
								{
									NodeList orderLinesNodes = orderLineNode.getChildNodes();
									if(orderLinesNodes != null)
									{
										Node orderlineNode=orderLinesNodes.item(0);
										if(orderlineNode != null)
										{
											NodeList orderLineNodes =orderlineNode.getChildNodes();
											int length = orderLineNodes.getLength();
											for (int i = 0; i < length; i++) {
												Element orderLineElement=(Element)orderLineNodes.item(i);
												String lineKey=orderLineElement.getAttribute("OrderLineKey");
												Element linePriceElem=(Element)orderLineElement.getElementsByTagName("LineOverallTotals").item(0);
												String unitPrice=linePriceElem.getAttribute("UnitPrice");
												unitPriceMap.put(lineKey, unitPrice);
											}
										}
								}
							}
						}
							for(int idx=0;idx<lineItems.getLength();idx++)
							{
								Element lineItemEle = (Element)lineItems.item(idx);
								String orderLineKey=lineItemEle.getAttribute("LineID");
								if(unitPriceMap.get(orderLineKey) != null ){
											lineItemEle.setAttribute("ListPrice", unitPriceMap.get(orderLineKey));
											lineItemEle.setAttribute("UnitPrice", unitPriceMap.get(orderLineKey));
											lineItemEle.setAttribute("LinePrice", unitPriceMap.get(orderLineKey));
								}
							}
						}
					}
				}
			else if((isPnACall ==null || "false".equalsIgnoreCase(isPnACall)) && 
					(isDiscountCalculate !=null  && "true".equals(isDiscountCalculate)))
			{
				int lineID=0;
				for (int j = 0; j < lineItems.getLength(); j++) {
					//Node orderLineNode = orderLineNodes.item(j);
					Element lineItemEle = (Element) lineItems.item(j);
					if(lineItemEle != null)
					{
						//Element orderLineTranQtyEle = (Element) orderLineNode
						//		.getFirstChild();
						String orderLineKey=lineItemEle.getAttribute("LineID");		
						
						if(YFCCommon.isVoid(orderLineKey))
						{
							orderLineKey=""+lineID;
							lineID +=1;
						}
						String unitPrice=reqUnitPrice.get(orderLineKey);
						lineItemEle.setAttribute("ListPrice", unitPrice);
						lineItemEle.setAttribute("UnitPrice", unitPrice);
						lineItemEle.setAttribute("LinePrice", unitPrice);
						BigDecimal extendedPriceDB=new BigDecimal(unitPrice);
						extendedPriceDB =extendedPriceDB.multiply(new BigDecimal(lineItemEle.getAttribute("Quantity")));
						extendedPriceMap.put(orderLineKey, extendedPriceDB.toString());
					}
				}
			}
			setProgressYFSEnvironmentVariables(env,unitPricePricingUOMMap,extendedPriceMap,pricingUOMMap,draftOrderFlag,hasPendingChanges);
			
		} catch (RemoteException re) {
			log.error(re.getMessage());
		} catch (YIFClientCreationException cce) {
			log.error(cce.getMessage());
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		SCXmlUtil.getString(arg1);
		return arg1;
	}
	
	// Storing PNA response xml into the table 
	private void storeResponseString(YFSEnvironment env,String responseXML,String orderHeaderKey)
	{
		Document inputDocument = SCXmlUtil.createDocument("XPXUeAdditionalAttrXml");
		try
		{
			
			Element inputElement = inputDocument.getDocumentElement();	
			inputElement.setAttribute("OrderHeaderKey", orderHeaderKey);
			inputElement.setAttribute("ResponseXML", responseXML);
			inputElement.setAttribute("XMLType", "PNA");
			api = YIFClientFactory.getInstance().getApi();
			api.executeFlow(env, "XPXChangeUEAdditionalAttributesXML", inputDocument);
		}
		catch(YFSException e)
		{
			try
			{
				if(e.getErrorDescription() !=null  && e.getErrorDescription().contains("Record does not exist in the database."))
				{
					api.executeFlow(env, "XPXCreateUEAdditionalAttributesXML", inputDocument);
				}
				else
				{
					log.error("Exception while changing the PnARespXML table : "+e.getMessage());
				}
			}
			catch(Exception exp)
			{
				log.error("Exception while changing the PnARespXML table : "+exp.getMessage());
			}
		}
		catch(Exception e)
		{
			log.error("Exception while changing the PnARespXML table : "+e.getMessage());
			
		}
		
		
	}
	
	
	// Storing PNA response xml into the table 
	private void deleteResponseFromUEAttrTable(YFSEnvironment env,String orderHeaderKey){
		Document inputDocument = SCXmlUtil.createDocument("XPXUeAdditionalAttrXml");
		try
		{
			
			Element inputElement = inputDocument.getDocumentElement();	
			inputElement.setAttribute("OrderHeaderKey", orderHeaderKey);
			inputElement.setAttribute("XMLType", "PNA");
			api = YIFClientFactory.getInstance().getApi();
			api.executeFlow(env, "XPXDeleteUEAdditionalAttributesXML", inputDocument);
		}
		catch(YFSException exp)
		{
			log.error("Exception while changing the PnARespXML table : "+exp.getMessage());
		}
		catch(Exception e)
		{
			log.error("Exception while changing the PnARespXML table : "+e.getMessage());
			
		}
	}
	
	private void setProgressYFSEnvironmentVariables(YFSEnvironment env,HashMap<String,String> unitPricePricingUOMMap,HashMap<String,String> extendedPriceMap,
			HashMap<String,String> pricingUOMMap,String draftOrderFlag,String hasPendingChanges) {
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				envVariablesmap.put("unitPricePricingUOMMap", unitPricePricingUOMMap);
				envVariablesmap.put("ExtendedPrice", extendedPriceMap);
				envVariablesmap.put("PricingUOMMap", pricingUOMMap);
				envVariablesmap.put("draftOrderFlag", draftOrderFlag);	
				envVariablesmap.put("hasPendingChanges", hasPendingChanges);
			}
			else{
				envVariablesmap = new HashMap();
				envVariablesmap.put("unitPricePricingUOMMap", unitPricePricingUOMMap);
				envVariablesmap.put("ExtendedPrice", extendedPriceMap);
				envVariablesmap.put("PricingUOMMap", pricingUOMMap);
				envVariablesmap.put("draftOrderFlag", draftOrderFlag);
				envVariablesmap.put("hasPendingChanges", hasPendingChanges);
			}
			
			clientVersionSupport.setClientProperties(envVariablesmap);
		}
	}
	
	private static void addXMLTag(Document doc, String tagName, String tagValue) {
		Element eleRoot = doc.getDocumentElement();
		Element textNode = SCXmlUtil.createChild(eleRoot, tagName);
		Text txt = doc.createTextNode(tagName);
		txt.setTextContent(tagValue);
		textNode.appendChild(txt);
	}

	private static YIFApi api = null;
	private static final Logger log = Logger
			.getLogger(XPEDXOverrideGetItemPriceUE.class);
}

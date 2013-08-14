package com.xpedx.nextgen.order;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.priceAndAvailabilityService.XPXItem;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.date.YTimestamp;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSUserExitException;
import com.yantra.yfs.japi.ue.YFSBeforeChangeOrderUE;

public class XPXBeforeChangeOrderUE implements YFSBeforeChangeOrderUE
{
	private static final Logger log = Logger
	.getLogger(XPXBeforeChangeOrderUE.class);
	private String draftOrderFlag;
	private String hasPendingChanges;
	private HashMap<String, String> primeLineNumMap = new LinkedHashMap<String, String>();
	private boolean isDeleteLine=false;
	private boolean isCreateLine=false;
	String isCom=null;
	private static YIFApi api = null;
	
	private boolean isOrderUpdateDone( YFSEnvironment env, Document outputDoc) 
	{
		try
		{
			Element _orderElement=outputDoc.getDocumentElement();
			String orderHeaderKey=_orderElement.getAttribute("OrderHeaderKey");
			YFCDocument inputDocument = YFCDocument.createDocument("Order");
			YFCElement inputElement = inputDocument.getDocumentElement();
			inputElement.setAttribute("OrderHeaderKey", orderHeaderKey);
			Date changedDate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.ENGLISH).parse(outputDoc.getDocumentElement().getAttribute("Modifyts"));
			String draftOrderFlag=outputDoc.getDocumentElement().getAttribute("DraftOrderFlag");
			api = YIFClientFactory.getInstance().getApi();
			
			 if("N".equalsIgnoreCase(draftOrderFlag))
			 {
					env.setApiTemplate(
							"getOrderList",
							SCXmlUtil
							.createFromString(""+"<OrderList>"
									+ "<Order  SellerOrganizationCode='' BuyerOrganizationCode='' DraftOrderFlag='' ShipToID='' OrderedQty='' Modifyts ='' >"
									+ "</Order></OrderList>"));
					Document orderListDocument = api.invoke(env, "getOrderList",
							inputDocument.getDocument());
					Element orderElementWithoutPending=(Element)orderListDocument.getDocumentElement().getElementsByTagName("Order").item(0);
					Date dbDate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.ENGLISH).parse(orderElementWithoutPending.getAttribute("Modifyts"));
					
				 
					return dbDate.after(changedDate);
			}
		}
		catch(Exception e)
		{
			log.error("Error while getting OrderList and order Document for pendign changes");
		}
		return false;
		}
		public Document beforeChangeOrder(YFSEnvironment env, Document outputDoc) throws YFSUserExitException			
			{
			if(log.isDebugEnabled()){
			log.debug("Inside Before Change Order UE");
			}
			
			Document inputDoc=null;
			String ischangeOrderInprogress=null;
			String isDiscountCalculate = null;
			String isCouponApply=null;
			String isPriceLock=null;
			String isPnACall = null;
			String orderWithoutLineToProcess=null;
			
			HashMap map =null;
			Element adjustmentInfoElem=null;			
			Element outputOrderElement = outputDoc.getDocumentElement();
			List<Element> oldAdjustmentInfoList=new ArrayList<Element>();
			Map<String,Map<String,String>> uomListMap=new HashMap<String,Map<String,String>>();
			Map<String,String> pricingUOMMap=new HashMap<String,String>();
			Map<String,String> unitPricePricingUOMMap=new HashMap<String,String>();
			Map<String,String> reqUnitPrice=new HashMap<String,String>();
			Map<String,String>isPriceLockMap=new  HashMap<String,String>();
			if (env instanceof ClientVersionSupport) {
				ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
				map = clientVersionSupport.getClientProperties();
				if (map != null) {
					inputDoc=(Document) map.get("changeOrderDocument");
					ischangeOrderInprogress=(String) map.get("isChangeOrderInProgress");
					isDiscountCalculate=(String)map.get("isDiscountCalculate");
					isCouponApply=(String)map.get("isCouponApply");
					isPriceLock =(String)map.get("isPriceLocking");
					isPnACall=(String)map.get("isPnACall");
					orderWithoutLineToProcess=(String)map.get("OrderWithoutLineToProcess");	
					if("COM".equals(map.get("applicationcode")) && SCUtil.isVoid(isDiscountCalculate))
					{
						isDiscountCalculate="true";
						isPnACall="true";
						isCom="true";
					}
				}
			}
			NodeList promotions=outputOrderElement.getElementsByTagName("Promotion");
			boolean isPromotion=(promotions !=null && promotions.getLength()>0);
			ArrayList<Element> orderLineList=SCXmlUtil.getElements(outputOrderElement,"OrderLines/OrderLine");
			if(orderLineList ==null && !(isPromotion) && (!"true".equals(orderWithoutLineToProcess)))
			{
				return outputDoc;
			}
			if(isDiscountCalculate !=null  && "true".equals(isDiscountCalculate) 
					|| isPnACall !=null && "true".equalsIgnoreCase(isPnACall))
			{
				
				if(	isOrderUpdateDone(env,outputDoc))
				{
					throw new YFSUserExitException("Exception While Applying cheanges .Order Update was finished before you update");
				}
				Element pendingChangesElem=(Element)outputDoc.getDocumentElement().getElementsByTagName("PendingChanges").item(0);
				if(pendingChangesElem != null )
				{
					String recordChanges=pendingChangesElem.getAttribute("RecordPendingChanges");
					if("Y".equalsIgnoreCase(recordChanges))
					{
						YTimestamp todayDate=YTimestamp.newTimestamp();
						//Date d=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").;
						outputDoc.getDocumentElement().setAttribute("Modifyts", todayDate.getString());
						outputDoc.getDocumentElement().setAttribute("DraftOrderFlag", "N");
					}
				}
				try
				{
					populatePrimeLineNum(outputDoc.getDocumentElement());
					Document orderPriceInputDoc=createOrderPriceDocument(env,outputOrderElement.getAttribute("OrderHeaderKey"),outputDoc,
							isPnACall,isCom,oldAdjustmentInfoList,uomListMap,pricingUOMMap,unitPricePricingUOMMap,reqUnitPrice,isPriceLockMap);
					if(createOrderExtnIfNoLine(orderPriceInputDoc,outputDoc,isPromotion,orderWithoutLineToProcess))
					{
						return outputDoc;
					}
					if(log.isDebugEnabled()){
					log.debug("Input Doc for getOrderPeice is : "+SCXmlUtil.getString(orderPriceInputDoc));
					}
					setProgressYFSEnvironmentVariables(env,map,draftOrderFlag,hasPendingChanges, primeLineNumMap,uomListMap,pricingUOMMap,unitPricePricingUOMMap,reqUnitPrice,isPriceLockMap); 
					
					env.setApiTemplate(
							"getOrderPrice",
							SCXmlUtil
							.createFromString("<Order CustomerID='' EnterpriseCode='' />"));
					api = YIFClientFactory.getInstance().getApi();
					setProgressYFSEnvironmentVariables(env,isDiscountCalculate);
					api.invoke(env, "getOrderPrice", orderPriceInputDoc);
					ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
					HashMap clientVersionMap = clientVersionSupport.getClientProperties();
					if (clientVersionMap != null) {
						inputDoc=(Document) clientVersionMap.get("changeOrderDocument");
						Object adjustmentInfoObj= clientVersionMap.get("adjustmentInfoElem");
						if(!YFCCommon.isVoid(adjustmentInfoObj))
						{
							adjustmentInfoElem=(Element)adjustmentInfoObj;
						}
					}
				}catch(Exception e)
				{
					e.printStackTrace();
					log.error(e.getMessage());
				}
			}
			if(ischangeOrderInprogress == null && inputDoc != null && 
					(isDiscountCalculate !=null  && "true".equals(isDiscountCalculate)))
			{
					try
					{
						
						
						// the below getOrderList(...) is made for fetching some additional field needed like 
						// Buyer Org (shipTo), DraftOrderFlag, hasPendingChanges, transactional UOM and Qty
						// hasPendingChanges will be N if there are no changes in order or if it is a draftOrder
						
						
						String inputXML = SCXmlUtil.getString(inputDoc);						
						Element orderElement = inputDoc.getDocumentElement();
						ArrayList<Element> orderExtnNode=SCXmlUtil.getElements(orderElement,"Extn");//orderElement.getElementsByTagName("Extn").item(0);
						Element orderExtn=null;
						if(orderExtnNode != null && orderExtnNode.size()>0)
						{
							orderExtn=orderExtnNode.get(0);
						}
						Element outputOrderExtnNode=null;
						ArrayList<Element> orderExtnList=SCXmlUtil.getElements(outputOrderElement, "Extn");
						if(orderExtnList != null && orderExtnList.size() > 0)
						{
							outputOrderExtnNode=orderExtnList.get(0);//outputOrderElement.getElementsByTagName("Extn").item(0);
						
						}						
						if(!YFCCommon.isVoid(adjustmentInfoElem))
						{
							
							if(!YFCCommon.isVoid(oldAdjustmentInfoList ))
							{
								for(int i=0;i<oldAdjustmentInfoList.size();i++)
								{
									Element adjustmentInfo=oldAdjustmentInfoList.get(i);
									adjustmentInfo.setAttribute("Operation", "Delete");
								//	SCXmlUtil.importElement(adjustmentInfoListElem, (Element)adjustmentInfo.cloneNode(true));
									SCXmlUtil.importElement(adjustmentInfoElem, adjustmentInfo);
								}
							}
							
						}
						if(outputOrderExtnNode == null)
						{
							Element outputOrderExtn=SCXmlUtil.createChild(
									outputOrderElement, "Extn");
							outputOrderExtn.setAttribute("ExtnOrderCouponDiscount", orderExtn.getAttribute("ExtnOrderCouponDiscount"));
							outputOrderExtn.setAttribute("ExtnOrderDiscount", orderExtn.getAttribute("ExtnOrderDiscount"));
							outputOrderExtn.setAttribute("ExtnOrderSubTotal", orderExtn.getAttribute("ExtnOrderSubTotal"));
							outputOrderExtn.setAttribute("ExtnTotOrdValWithoutTaxes", orderExtn.getAttribute("ExtnTotOrdValWithoutTaxes"));
							outputOrderExtn.setAttribute("ExtnTotOrderAdjustments", orderExtn.getAttribute("ExtnTotOrderAdjustments"));
							outputOrderExtn.setAttribute("ExtnTotalOrderValue", orderExtn.getAttribute("ExtnTotalOrderValue"));
							outputOrderExtn.setAttribute("ExtnLegTotOrderAdjustments", orderExtn.getAttribute("ExtnLegTotOrderAdjustments"));
							
							if(!YFCCommon.isVoid(adjustmentInfoElem))
								SCXmlUtil.importElement(outputOrderExtn, adjustmentInfoElem);
							
						}
						else
						{
							Element outputOrderExtn =(Element)outputOrderExtnNode;
							outputOrderExtn.setAttribute("ExtnOrderCouponDiscount", orderExtn.getAttribute("ExtnOrderCouponDiscount"));
							outputOrderExtn.setAttribute("ExtnOrderDiscount", orderExtn.getAttribute("ExtnOrderDiscount"));
							outputOrderExtn.setAttribute("ExtnOrderSubTotal", orderExtn.getAttribute("ExtnOrderSubTotal"));
							outputOrderExtn.setAttribute("ExtnTotOrdValWithoutTaxes", orderExtn.getAttribute("ExtnTotOrdValWithoutTaxes"));
							outputOrderExtn.setAttribute("ExtnTotOrderAdjustments", orderExtn.getAttribute("ExtnTotOrderAdjustments"));
							outputOrderExtn.setAttribute("ExtnTotalOrderValue", orderExtn.getAttribute("ExtnTotalOrderValue"));
							outputOrderExtn.setAttribute("ExtnLegTotOrderAdjustments", orderExtn.getAttribute("ExtnLegTotOrderAdjustments"));
							if(!YFCCommon.isVoid(adjustmentInfoElem))
								SCXmlUtil.importElement(outputOrderExtn, adjustmentInfoElem);
						}
						
						Map<String,Object> orederlineExtnMap= getOrderLineExtnMap(orderElement,outputOrderElement,isPriceLock);
						
						//Node orderLinesElem = outputOrderElement.getElementsByTagName("OrderLines").item(0);
						//NodeList orderLineNodeList =  orderLinesElem.getChildNodes();
						//ArrayList<Element> outputOrderLineNodeList=SCXmlUtil.getElements(outputOrderElement,"OrderLines/OrderLine");
						
						ArrayList<Element> orderLineNodeList=(ArrayList<Element>)orederlineExtnMap.get("OrderLine");
						/*if(outputOrderLineNodeList == null || outputOrderLineNodeList.size()==0)
						{
							Element outputOrderLinesExtn=SCXmlUtil.createChild(
									outputOrderElement, "OrderLines");
							for(int i=0;i<orderLineNodeList.size();i++)
							{
								Element orderLineElem = (Element) orderLineNodeList.get(i);
								SCXmlUtil.importElement(outputOrderLinesExtn, orderLineElem);
							}
						}
							*//*Element outputOrderLinesExtn=SCXmlUtil.createChild(
									outputOrderElement, "OrderLines");*/
							/*for(int i=0;i<orderLineNodeList.size();i++)
							{
								Element orderLineElem = (Element) orderLineNodeList.get(i);
								SCXmlUtil.importElement(outputOrderLinesExtn, orderLineElem);
							}*/
						//}
						/*else
						{*/
							boolean isAllRemoved=true;
							int lineID=0;
							for(int i=0;i<orderLineNodeList.size();i++)
							{
								Element orderLineElem = (Element) orderLineNodeList.get(i);
								String orderLineKey= orderLineElem.getAttribute("OrderLineKey");
								String orderLineAction=orderLineElem.getAttribute("Action");
								
								if(!(YFCCommon.isVoid(isPriceLock)) && "true".equals(isPriceLock.trim()))
								{
									Element linePriceInfoElem=(Element)orderLineElem.getElementsByTagName("LinePriceInfo").item(0);
									if(linePriceInfoElem ==null)
									{
										linePriceInfoElem=SCXmlUtil.createChild(
											orderLineElem, "LinePriceInfo");
										
									}
									linePriceInfoElem.setAttribute("IsPriceLocked", "Y");
								}
								if(YFCCommon.isVoid(orderLineKey))
								{
									orderLineKey=""+lineID;
									lineID +=1;
								}
								Element transactionLineElem= (Element)orderLineElem.getElementsByTagName("OrderLineTranQuantity").item(0);
								if(transactionLineElem != null )
								{
									String orderedQty=transactionLineElem.getAttribute("OrderedQty");
									if(YFCCommon.isVoid(orderedQty))
									{
										isAllRemoved=false;
									}
									else
									{
										double qty=Double.valueOf(orderedQty);
										if(qty!=0)
										{
											isAllRemoved=false;
										}
										else if(orderLineAction != null && !"REMOVE".equals(orderLineAction))
										{
											isAllRemoved=false;
										}
											
									}
								}
								else if(orderLineAction != null && !"REMOVE".equals(orderLineAction))
								{
									isAllRemoved=false;
								}
								Node orderLineExtnElem=(Node)orederlineExtnMap.get(orderLineKey);
								
								Node orderLineExtnNode=orderLineElem.getElementsByTagName("Extn").item(0);
								Element orderLineExtn=null;
								if(orderLineExtnElem != null)
								{
									 orderLineExtn=(Element)orderLineExtnElem;
								}
								if( orderLineExtn != null)
								{
								
									if(orderLineExtnNode == null)
									{
										
										//orderLineElem.appendChild(orderLineExtnElem);
										Element outputOrderLineExtn=SCXmlUtil.createChild(
												orderLineElem, "Extn");
										outputOrderLineExtn.setAttribute("ExtnAdjDollarAmt", orderLineExtn.getAttribute("ExtnAdjDollarAmt"));
										outputOrderLineExtn.setAttribute("ExtnAdjUOMUnitPrice", orderLineExtn.getAttribute("ExtnAdjUOMUnitPrice"));
										outputOrderLineExtn.setAttribute("ExtnExtendedPrice", orderLineExtn.getAttribute("ExtnExtendedPrice"));
										outputOrderLineExtn.setAttribute("ExtnLineCouponDiscount", orderLineExtn.getAttribute("ExtnLineCouponDiscount"));
										outputOrderLineExtn.setAttribute("ExtnLineOrderedTotal", orderLineExtn.getAttribute("ExtnLineOrderedTotal"));
										outputOrderLineExtn.setAttribute("ExtnPricingUOM", orderLineExtn.getAttribute("ExtnPricingUOM"));
										outputOrderLineExtn.setAttribute("ExtnReqUOMUnitPrice", orderLineExtn.getAttribute("ExtnReqUOMUnitPrice"));
										outputOrderLineExtn.setAttribute("ExtnUnitPrice", orderLineExtn.getAttribute("ExtnUnitPrice"));
										outputOrderLineExtn.setAttribute("ExtnUnitPriceDiscount", orderLineExtn.getAttribute("ExtnUnitPriceDiscount"));
										outputOrderLineExtn.setAttribute("ExtnLegOrderLineAdjustments", orderLineExtn.getAttribute("ExtnLegOrderLineAdjustments"));
									}
									else
									{
										Element outputOrderLineExtn =(Element)orderLineExtnNode;
										outputOrderLineExtn.setAttribute("ExtnAdjDollarAmt", orderLineExtn.getAttribute("ExtnAdjDollarAmt"));
										outputOrderLineExtn.setAttribute("ExtnAdjUOMUnitPrice", orderLineExtn.getAttribute("ExtnAdjUOMUnitPrice"));
										outputOrderLineExtn.setAttribute("ExtnUnitPriceDiscount", orderLineExtn.getAttribute("ExtnUnitPriceDiscount"));
										outputOrderLineExtn.setAttribute("ExtnLineCouponDiscount", orderLineExtn.getAttribute("ExtnLineCouponDiscount"));
										outputOrderLineExtn.setAttribute("ExtnLineOrderedTotal", orderLineExtn.getAttribute("ExtnLineOrderedTotal"));
										outputOrderLineExtn.setAttribute("ExtnReqUOMUnitPrice", orderLineExtn.getAttribute("ExtnReqUOMUnitPrice"));
										outputOrderLineExtn.setAttribute("ExtnLegOrderLineAdjustments", orderLineExtn.getAttribute("ExtnLegOrderLineAdjustments"));
										if("true".equals(isCom))
										{
											if(!outputOrderLineExtn.hasAttribute("ExtnExtendedPrice"))
											{
												outputOrderLineExtn.setAttribute("ExtnExtendedPrice", orderLineExtn.getAttribute("ExtnExtendedPrice"));
											}
											if(!outputOrderLineExtn.hasAttribute("ExtnPricingUOM"))
											{
												outputOrderLineExtn.setAttribute("ExtnPricingUOM", orderLineExtn.getAttribute("ExtnPricingUOM"));
											}									
											if(!outputOrderLineExtn.hasAttribute("ExtnUnitPrice"))
											{
												outputOrderLineExtn.setAttribute("ExtnUnitPrice", orderLineExtn.getAttribute("ExtnUnitPrice"));
											}
											else
											{
												try
												{
												BigDecimal extnUnitPrice=new BigDecimal(outputOrderLineExtn.getAttribute("ExtnUnitPrice"));
												BigDecimal extnAdjustedUnitPrice=new BigDecimal(orderLineExtn.getAttribute("ExtnAdjustmentOfUnitPrice"));
												outputOrderLineExtn.setAttribute("ExtnAdjUnitPrice", extnUnitPrice.subtract(extnAdjustedUnitPrice).toString());
												}
												catch(Exception e)
												{
													
												}
											}
											
										}
										else
										{
											outputOrderLineExtn.setAttribute("ExtnExtendedPrice", orderLineExtn.getAttribute("ExtnExtendedPrice"));
											if(!"".equals(orderLineExtn.getAttribute("ExtnPricingUOM")) && orderLineExtn.getAttribute("ExtnPricingUOM") != null)
											{
												outputOrderLineExtn.setAttribute("ExtnPricingUOM", orderLineExtn.getAttribute("ExtnPricingUOM"));
											}
											if(!"".equals(orderLineExtn.getAttribute("ExtnUnitPrice")) && orderLineExtn.getAttribute("ExtnUnitPrice") != null)
											{
												outputOrderLineExtn.setAttribute("ExtnUnitPrice", orderLineExtn.getAttribute("ExtnUnitPrice"));
												outputOrderLineExtn.setAttribute("ExtnAdjUnitPrice", orderLineExtn.getAttribute("ExtnAdjUnitPrice"));
											}
											//outputOrderLineExtn.setAttribute("ExtnPricingUOM", orderLineExtn.getAttribute("ExtnPricingUOM"));
											//outputOrderLineExtn.setAttribute("ExtnUnitPrice", orderLineExtn.getAttribute("ExtnUnitPrice"));
										}
									}
								}
							}
							if(isAllRemoved && !isPromotion &&  (!"true".equals(orderWithoutLineToProcess)))
							{
								createOrderExtnIfNoLine(outputDoc);
							}
						}
					
					//}
					catch(Exception e)
					{
						log.debug("Error while updating the order "+e );
					}
					
			}
			setProgressYFSEnvironmentVariables(env);
			
			
			NodeList linePriceInfoEleList = outputOrderElement.getElementsByTagName("LinePriceInfo");
			int linePriceInfoLength = linePriceInfoEleList.getLength();
			for(int elemCounter = 0;elemCounter < linePriceInfoLength;elemCounter++){
				Element linePriceElement = (Element)linePriceInfoEleList.item(elemCounter);
				if(linePriceElement.hasAttribute("LineTotal")){
					linePriceElement.removeAttribute("LineTotal");
				}
			}
			
			if(log.isDebugEnabled()){
			log.debug("Output XML of Before Change Order UE "+SCXmlUtil.getString(outputDoc));
			}
			return outputDoc;
            }
		
		private void populatePrimeLineNum(Element outputDoc)
		{
			/*String isDraftOrder = outputDoc.getAttribute("DraftOrderFlag");
			if("N".equals(isDraftOrder ))
			{*/
				ArrayList<Element> orderLinesElem=SCXmlUtil.getElements(outputDoc, "OrderLines/OrderLine");
				if(orderLinesElem!=null && orderLinesElem.size()>0 )
				{
					Iterator<Element> outputLinesIter = orderLinesElem.iterator();
					
					while(outputLinesIter.hasNext())
					{
						Element orderLineItem = outputLinesIter.next();
						Element newOrderLineElem = SCXmlUtil.createDocument("OrderLine").getDocumentElement();
						String orderLineKey = orderLineItem.getAttribute("OrderLineKey");
						String primeLineNum = orderLineItem.getAttribute("PrimeLineNo");
						primeLineNumMap.put(orderLineKey, primeLineNum);
					}
				}
		//	}
		}
		private boolean createOrderExtnIfNoLine(Document orderPriceInputDoc,Document outputDoc,boolean isCoupon,String orderWithoutLineToProcess)
		{
			if(isCoupon )
			{
				return false;
			}
			Element orderElem=orderPriceInputDoc.getDocumentElement();
			Element outputOrderElem=outputDoc.getDocumentElement();
			ArrayList<Element> orderLineNodeList=SCXmlUtil.getElements(orderElem,"OrderLines/OrderLine");
			setTranQuantityZero(outputDoc);
			if(orderLineNodeList == null || orderLineNodeList.size()==0)
			{
				Element orderExtnElem=(Element)outputOrderElem.getElementsByTagName("Extn").item(0);
				if(orderExtnElem == null)
				{
					orderExtnElem=SCXmlUtil.createChild(
							outputOrderElem, "Extn");
				}
				orderExtnElem.setAttribute("ExtnOrderCouponDiscount","0");
				orderExtnElem.setAttribute("ExtnOrderDiscount", "0");
				orderExtnElem.setAttribute("ExtnOrderSubTotal", "0");
				orderExtnElem.setAttribute("ExtnTotOrdValWithoutTaxes", "0");
				orderExtnElem.setAttribute("ExtnTotOrderAdjustments", "0");
				orderExtnElem.setAttribute("ExtnTotalOrderValue", "0");
				return true;
			}
			return false;
		}
		private void setTranQuantityZero(Document outputDoc )
		{
			Element orderElem=outputDoc.getDocumentElement();
			ArrayList<Element> orderLineNodeList=SCXmlUtil.getElements(orderElem,"OrderLines/OrderLine");
			if(orderLineNodeList == null)
			{
				return;
			}
			for(int i=0;i<orderLineNodeList.size();i++)
			{
				Element orderLineElem =orderLineNodeList.get(i);
				String orderQty=orderLineElem.getAttribute("OrderedQty");
				double orderVal=0;
				if(!SCUtil.isVoid(orderQty) )
				{
					orderVal=Double.parseDouble(orderQty);
				}
				if(orderVal ==0 && !SCUtil.isVoid(orderQty))
				{
					Element orderLineTran=(Element)orderLineElem.getElementsByTagName("OrderLineTranQuantity").item(0);
					if(orderLineTran == null)
					{
						orderLineTran=SCXmlUtil.createChild(
								orderLineElem, "OrderLineTranQuantity");
					}
					orderLineTran.setAttribute("OrderedQty", "0");
				}
			}
		}
		private void createOrderExtnIfNoLine(Document outputDoc)
		{
			Element orderElem=outputDoc.getDocumentElement();
			ArrayList<Element> orderExtnList=SCXmlUtil.getElements(orderElem, "Extn");
			Element orderExtnElem=orderExtnList.get(0);
			if(orderExtnList == null)
			{
				orderExtnElem=SCXmlUtil.createChild(
						orderElem, "Extn");
			}
			orderExtnElem.setAttribute("ExtnOrderCouponDiscount","0");
			orderExtnElem.setAttribute("ExtnOrderDiscount", "0");
			orderExtnElem.setAttribute("ExtnOrderSubTotal", "0");
			orderExtnElem.setAttribute("ExtnTotOrdValWithoutTaxes", "0");
			orderExtnElem.setAttribute("ExtnTotOrderAdjustments", "0");
			orderExtnElem.setAttribute("ExtnTotalOrderValue", "0");
		}
		
		private Document createOrderPriceDocument(YFSEnvironment env,String orderHeaderKey,Document outputDoc,
				String isPnACall,String isCom,List<Element> oldAdjustmentInfoList,Map<String,Map<String,String>> uomListMap,
				Map<String,String> pricingUOMMap,Map<String,String> unitPricePricingUOMMap,Map<String,String> reqUnitPrice,
				Map<String,String>isPriceLockMap) throws Exception
		{
			/*<Order Currency="USD" CustomerID="60-6806858-000002-M-60" OrganizationCode="xpedx" OrderReference="201105201254521473136" >
			<OrderLines>
			  <OrderLine ItemID="2254088" Quantity="1" />
			</OrderLines>
			</Order>*/
			ArrayList<XPXItem> inputItems = new ArrayList<XPXItem>();
			YFCDocument inputDocument = YFCDocument.createDocument("Order");
			YFCElement inputElement = inputDocument.getDocumentElement();
			inputElement.setAttribute("OrderHeaderKey", orderHeaderKey);
			
			//api = YIFClientFactory.getInstance().getApi();
			setProgressYFSEnvironmentForCompleteOrderDetailsVariables(env);
			/*Document orderListDocument = api.invoke(env, "getOrderList",
					inputDocument.getDocument());	*/
			
			//Element orderListElem=(Element)orderListDocument.getElementsByTagName("Order").item(0);
			Element orderListElem=getOrderElement(env,outputDoc.getDocumentElement().getAttribute("DraftOrderFlag"),inputDocument);
			Element orderPriceInfoElem=(Element)orderListElem.getElementsByTagName("PriceInfo").item(0);
			Document orderPriceInputDoc=SCXmlUtil.createDocument("Order");
			Element orderPriceInputElem=orderPriceInputDoc.getDocumentElement();
			draftOrderFlag= orderListElem.getAttribute("DraftOrderFlag");
			hasPendingChanges=orderListElem.getAttribute("HasPendingChanges");
			orderPriceInputElem.setAttribute("Currency", orderPriceInfoElem.getAttribute("Currency"));
			orderPriceInputElem.setAttribute("CustomerID", orderListElem.getAttribute("ShipToID"));
			orderPriceInputElem.setAttribute("OrganizationCode", orderListElem.getAttribute("EnterpriseCode"));
			orderPriceInputElem.setAttribute("OrderReference", orderHeaderKey);
			applayCoupon(outputDoc.getDocumentElement(),orderPriceInputElem);
			applayCoupon(orderListElem,orderPriceInputElem);
			Element orderPriceLinesElem=SCXmlUtil.createChild(orderPriceInputElem, "OrderLines");
			oldAdjustmentInfoList.addAll(SCXmlUtil.getElements(orderListElem, "Extn/XPXOrderAdjustmentsInfoList/XPXOrderAdjustmentsInfo"));
			//Map<String,Element> orderQtyMap=createQtyUOMMap(outputDoc.getDocumentElement());
			
			//if(!(orderQtyMap !=null && orderQtyMap.size()>0))
			//{
			//	orderQtyMap=createQtyUOMMap(orderListElem);
			//}
			Map<String,Element> outputMap=new LinkedHashMap<String,Element>();
			//Method to form the price input document with latest qty/uom changes, including the existing items.
			updateOrderLinesElem(orderListElem, outputMap,isPnACall,1,uomListMap,pricingUOMMap,unitPricePricingUOMMap,reqUnitPrice,isPriceLockMap);
			updateOrderLinesElem(outputDoc.getDocumentElement(), outputMap,isPnACall,2,uomListMap,pricingUOMMap,unitPricePricingUOMMap,reqUnitPrice,isPriceLockMap);
			
			ArrayList<Element> orderLinesElem= null;
			if(outputMap!=null && !outputMap.isEmpty())
			{
				Collection<Element> allOrderLineElems= outputMap.values();
				orderLinesElem = new ArrayList<Element>(allOrderLineElems);
			}
			/*ArrayList<Element> orderLinesElem=SCXmlUtil.getElements(orderListElem, "OrderLines/OrderLine");
			if(orderLinesElem == null || orderLinesElem.size() == 0)
			{
				orderLinesElem=SCXmlUtil.getElements(outputDoc.getDocumentElement(), "OrderLines/OrderLine");
			}*/		
			int primeLineNo=1;
			ArrayList<Element> isPriceLockList=new ArrayList<Element>();  
			if(orderLinesElem != null && orderLinesElem.size() > 0)
			{
				/*if(orderLinesElem.size() == 0)
				{
					orderLinesElem=SCXmlUtil.getElements(outputDoc.getDocumentElement(), "OrderLines/OrderLine");
				}*/
				Iterator<Element> it=orderLinesElem.iterator();
				while(it.hasNext())
				{
					Element orderLineElem=it.next();
					if(orderLineElem.hasAttribute("isDeletedLine"))
						continue;
					Element orderLineItemElem=(Element)orderLineElem.getElementsByTagName("Item").item(0);
					Element orderListLineElem=SCXmlUtil.createChild(orderPriceLinesElem, "OrderLine");
					String orderLineKey=orderLineElem.getAttribute("OrderLineKey");
					//Element transactionLineElem=orderQtyMap.get(orderLineKey);
					String primeLineNum=orderLineElem.getAttribute("PrimeLineNo");	
					if(YFCCommon.isVoid(orderLineKey))
					{
						primeLineNumMap.put(new Integer(primeLineNo).toString(), primeLineNum);
						primeLineNo++;
					}
					else
					{
						primeLineNumMap.put(orderLineKey, primeLineNum);
					}
					
					Element transactionLineElem= (Element)orderLineElem.getElementsByTagName("OrderLineTranQuantity").item(0);
					String orderedQty=transactionLineElem.getAttribute("OrderedQty");
					String tranUOM=transactionLineElem.getAttribute("TransactionalUOM");
					if((YFCCommon.isVoid(orderedQty)))
					{
						orderedQty=orderLineElem.getAttribute("OrderedQty");
					}
					orderListLineElem.setAttribute("ItemID", orderLineItemElem.getAttribute("ItemID"));
					orderListLineElem.setAttribute("Quantity",orderedQty);
					orderListLineElem.setAttribute("LineID",orderLineKey );
					orderListLineElem.setAttribute("UnitOfMeasure",tranUOM );
					Element linePriceInfoElem= (Element)orderLineElem.getElementsByTagName("LinePriceInfo").item(0);
					if(linePriceInfoElem != null)
					{
						orderListLineElem.setAttribute("UnitPrice", linePriceInfoElem.getAttribute("UnitPrice"));
						orderListLineElem.setAttribute("IsPriceLocked", "N");
						
					}
					isPriceLockList.add(linePriceInfoElem);
					/*if(!"true".equals(isCom))
					{
						orderListLineElem.setAttribute("IsPriceLocked","N");
						
					}*/
					
				}
				//primeLineNumMap.put("isPriceLockList", isPriceLockList);
			}
			
			return orderPriceInputDoc;
			
		}
		private Element getOrderElement(YFSEnvironment env,String isDraftOrder,YFCDocument inputDocument) throws Exception 
		{
			api = YIFClientFactory.getInstance().getApi();
			if( "N".equals(isDraftOrder))
			{
				env.setApiTemplate(
						"getCompleteOrderDetails",
						SCXmlUtil
						.createFromString("<Order HasPendingChanges='' EnterpriseCode='' SellerOrganizationCode='' BuyerOrganizationCode='' DraftOrderFlag='' ShipToID='' OrderedQty=''> <PriceInfo Currency=''></PriceInfo>" 
								+"<Promotions/>"
								+"<OrderLines><OrderLine OrderedQty='' OrderLineKey='' PrimeLineNo=''>"
								+"<ItemDetails ItemID='' ItemKey='' UnitOfMeasure=''><AlternateUOMList><AlternateUOM Quantity='' UnitOfMeasure=''/></AlternateUOMList></ItemDetails>"
								+"<LinePriceInfo IsPriceLocked='' UnitPrice='' /><Extn ExtnLineOrderedTotal='' ExtnReqUOMUnitPrice='' ExtnAdjUOMUnitPrice='' ExtnPriceOverrideFlag='' ExtnEditOrderFlag='' ExtnPricingUOM='' ExtnUnitPrice=''/> <Item ItemID='' ></Item>"
								+ "<OrderLineTranQuantity OrderedQty='' TransactionalUOM=''></OrderLineTranQuantity>"
								+ "</OrderLine></OrderLines>"
								+ "<Extn ><XPXOrderAdjustmentsInfoList><XPXOrderAdjustmentsInfo OrderAdjustmentInfoKey='' OrderHeaderKey=''/>"
								+ "</XPXOrderAdjustmentsInfoList></Extn>"
								+ "</Order>"));
				Document orderListDocument = api.invoke(env, "getCompleteOrderDetails",
						inputDocument.getDocument());
				return orderListDocument.getDocumentElement();
			}
			else
			{
				env.setApiTemplate(
						"getOrderList",
						SCXmlUtil
						.createFromString(""+"<OrderList>"
								+ "<Order HasPendingChanges='' EnterpriseCode='' SellerOrganizationCode='' BuyerOrganizationCode='' DraftOrderFlag='' ShipToID='' OrderedQty=''> <PriceInfo Currency=''></PriceInfo>" 
								+"<Promotions/>"
								+"<OrderLines><OrderLine OrderedQty='' OrderLineKey='' PrimeLineNo=''>"
								+"<ItemDetails ItemID='' ItemKey='' UnitOfMeasure=''><AlternateUOMList><AlternateUOM Quantity='' UnitOfMeasure=''/></AlternateUOMList></ItemDetails>"
								+"<LinePriceInfo IsPriceLocked='' UnitPrice='' /><Extn ExtnLineOrderedTotal='' ExtnReqUOMUnitPrice='' ExtnAdjUOMUnitPrice='' ExtnPriceOverrideFlag='' ExtnEditOrderFlag='' ExtnPricingUOM='' ExtnUnitPrice=''/> <Item ItemID='' ></Item>"
								+ "<OrderLineTranQuantity OrderedQty='' TransactionalUOM=''></OrderLineTranQuantity>"
								+ "</OrderLine></OrderLines>"
								+ "<Extn ><XPXOrderAdjustmentsInfoList><XPXOrderAdjustmentsInfo OrderAdjustmentInfoKey='' OrderHeaderKey=''/>"
								+ "</XPXOrderAdjustmentsInfoList></Extn>"
								+ "</Order></OrderList>"));
				Document orderListDocument = api.invoke(env, "getOrderList",
						inputDocument.getDocument());
				return (Element)orderListDocument.getElementsByTagName("Order").item(0);
			}
						
		}
		
		private void applayCoupon(Element outputDoc,Element orderPriceInputElem)
		{
			NodeList promotions=outputDoc.getElementsByTagName("Promotion");
			for(int i=0;i<promotions.getLength();i++)
			{
				Element couponsElem=SCXmlUtil.createChild(orderPriceInputElem, "Coupons");
				Element couponElem=SCXmlUtil.createChild(couponsElem, "Coupon");
				Element promotion=(Element)promotions.item(i);
				couponElem.setAttribute("CouponID", promotion.getAttribute("PromotionId"));
				
			}
		}
		private void setProgressYFSEnvironmentVariables(YFSEnvironment env) {
			if (env instanceof ClientVersionSupport) {
				ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
				HashMap envVariablesmap = clientVersionSupport.getClientProperties();
				if (envVariablesmap != null) {
					envVariablesmap.remove("changeOrderDocument");
					envVariablesmap.remove("isCouponApply");
					envVariablesmap.remove("isDiscountCalculate");
					envVariablesmap.remove("PricingUOMMap");
					envVariablesmap.remove("qtyUOMMap");
					envVariablesmap.remove("ExtendedPrice");
					envVariablesmap.remove("isPnACall");
					envVariablesmap.remove("unitPricePricingUOMMap");
					envVariablesmap.remove("hasPendingChanges");					
					envVariablesmap.put("ischangeOrderInprogress", "true");
				}
				/*else
				{
					envVariablesmap=new HashMap();
					envVariablesmap.put("ischangeOrderInprogress", "true");
				}*/
				clientVersionSupport.setClientProperties(envVariablesmap);
			}
		}
		private void setProgressYFSEnvironmentVariables(YFSEnvironment env,String isDiscountCalculate) {
			if (env instanceof ClientVersionSupport) {
				ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
				HashMap envVariablesmap = clientVersionSupport.getClientProperties();
				if (envVariablesmap != null) {
					envVariablesmap.put("isDiscountCalculate", isDiscountCalculate);
				}
				clientVersionSupport.setClientProperties(envVariablesmap);
			}
		}
		
		private void setProgressYFSEnvironmentVariables(YFSEnvironment env,HashMap map,String hasPendingChanges,String draftOrderFlag, HashMap<String, String> primeLineNumMap,
				Map<String,Map<String,String>> uomListMap,Map<String,String> pricingUOMMap,Map<String,String> unitPricePricingUOMMap,Map<String,String> reqUnitPrice
				,Map<String,String> isPriceLockMap) {
			if (env instanceof ClientVersionSupport) {
				ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
				//HashMap envVariablesmap = clientVersionSupport.getClientProperties();
				
				if (map != null) {
					map.put("hasPendingChanges", hasPendingChanges);
					map.put("draftOrderFlag", draftOrderFlag);
					map.put("primeLineNoMap", primeLineNumMap);
					map.put("uomListMap", uomListMap);
					map.put("PricingUOMMap", pricingUOMMap);
					map.put("unitPricePricingUOMMap", unitPricePricingUOMMap);
					map.put("reqUnitPrice", reqUnitPrice);
					map.put("isPriceLockMap", isPriceLockMap);
					if(isCreateLine == true)
					{
						
						map.put("isPnACall", "true");
					}
					else if(isDeleteLine == true)
					{						
						map.put("isDiscountCalculate", "false");
					}
					
				}
				clientVersionSupport.setClientProperties(map);
			}
		}
		
		private void setProgressYFSEnvironmentForCompleteOrderDetailsVariables(YFSEnvironment env) {
			if (env instanceof ClientVersionSupport) {
				ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
				clientVersionSupport.setClientProperties(new HashMap());
			}
		}
		
		private Map<String,Object> getOrderLineExtnMap(Element orderElement,Element outputOrderElement,String isPriceLock)
		{
			Map<String,Object> outputMap=new HashMap<String,Object>();
			Node orderLinesElem = orderElement.getElementsByTagName("OrderLines").item(0);
			NodeList orderLineNodeList =  orderLinesElem.getChildNodes();
			
			
			if(SCXmlUtil.getElements(outputOrderElement,"OrderLines/OrderLine") == null)
			{
				SCXmlUtil.createChild(outputOrderElement, "OrderLines");
			}
			ArrayList<Element> outputOrderLineNodeList=SCXmlUtil.getElements(outputOrderElement,"OrderLines/OrderLine");
			Element orderLines=(Element)outputOrderElement.getElementsByTagName("OrderLines").item(0);
			ArrayList<Element> orderLineElemList=new ArrayList<Element>();
			Map<String,Element> priceInfoElem=new HashMap<String,Element>();
			int lineId=0;
			//String tempStr="";
			for(int i=0;i<outputOrderLineNodeList.size();i++)
			{
				Element orderLineElem = (Element) outputOrderLineNodeList.get(i);
				String orderLineKey= orderLineElem.getAttribute("OrderLineKey");
				Element linePriceInfoElem=(Element)orderLineElem.getElementsByTagName("LinePriceInfo").item(0);
				
				if(YFCCommon.isVoid(orderLineKey))
				{
					orderLineKey=""+lineId;
					lineId=lineId+1;
				}
				changeTranQuantity(orderLineElem);
				orderLineElemList.add(orderLineElem);
				//tempStr=tempStr+","+orderLineKey;
				priceInfoElem.put(orderLineKey, orderLineElem);
			}
			
			
			
			for(int i=0;i<orderLineNodeList.getLength();i++)
			{
				Element orderLineElem = (Element) orderLineNodeList.item(i);
				Node orederLineExtnNode=orderLineElem.getElementsByTagName("Extn").item(0);
				String orderLineKey= orderLineElem.getAttribute("OrderLineKey");
				
				if(YFCCommon.isVoid(priceInfoElem.get(orderLineKey)))
				{
					SCXmlUtil.importElement(orderLines, orderLineElem);
					orderLineElemList.add(orderLineElem);
				}
				/*else
				{
					Element orderLinePriceLineInfoElem=priceInfoElem.get(orderLineKey);
					Element linePriceInfoOutPut= (Element)orderLinePriceLineInfoElem.getElementsByTagName("LinePriceInfo").item(0);
					Element linePriceInfo= (Element)orderLineElem.getElementsByTagName("LinePriceInfo").item(0);
					if(linePriceInfoOutPut == null)
					{
						linePriceInfoOutPut=SCXmlUtil.createChild(orderLinePriceLineInfoElem, "LinePriceInfo");
					}
					String isPriceLockedOutput=linePriceInfoOutPut.getAttribute("IsPriceLocked");
					
					String isPriceLocked=linePriceInfo.getAttribute("IsPriceLocked");
					if("".equals(isPriceLockedOutput) || isPriceLockedOutput == null)
					{
						linePriceInfoOutPut.setAttribute("IsPriceLocked", isPriceLocked);
					}
				}*/
				outputMap.put(orderLineKey,orederLineExtnNode);
				
				
			}
			
			outputMap.put("OrderLine", orderLineElemList);
			return outputMap;
		}
		
		private void changeTranQuantity(Element orderLineElem)
		{
			NodeList tranNodeList=orderLineElem.getElementsByTagName("OrderLineTranQuantity");
			String orderQty=orderLineElem.getAttribute("OrderedQty");
			double orderVal=0;
			if(!SCUtil.isVoid(orderQty) )
			{
				orderVal=Double.parseDouble(orderQty);
			}
			if(tranNodeList != null && tranNodeList.getLength() >0 && 
					(orderVal ==0 && !SCUtil.isVoid(orderQty)))
			{
				Element orderLineTran=(Element)tranNodeList.item(0);
				orderLineTran.setAttribute("OrderedQty", orderQty);
			}
			
			
		}
		/*private Map<String,String> getOrderQtyMap(Element orderElement )
		{
			Map<String,String> outputMap=new HashMap<String,String>();
			ArrayList<Element> orderLineList=SCXmlUtil.getElements(orderElement,"OrderLines/OrderLine");
			if(orderLineList != null)
			{
				for(int i=0;i<orderLineList.size();i++)
				{
					Element orderLineElem = orderLineList.get(i);
					String orderLineKey= orderLineElem.getAttribute("OrderLineKey");
					Element order
					outputMap.put(orderLineKey,orderLineElem.getAttribute("OrderedQty"));
					
					
				}
			}
			return outputMap;
		}*/
		
		/*private void createQtyUOMMap(Element orderElement, Map<String,Element> outputMap)
		{
			//Map<String,Element> outputMap=new HashMap<String,Element>();
			
			ArrayList<Element> orderLineList=SCXmlUtil.getElements(orderElement,"OrderLines/OrderLine");
			if(orderLineList != null)
			{
				for(int i=0;i<orderLineList.size();i++)
				{
					Element outputElem=SCXmlUtil.createDocument("OrderLineTranQuantity").getDocumentElement();
					Element orderLineElem = orderLineList.get(i);
					NodeList tranNodeList=orderLineElem.getElementsByTagName("OrderLineTranQuantity");
					if(tranNodeList != null && tranNodeList.getLength() >0)
					{
						Element orderLineTran=(Element)tranNodeList.item(0);
						outputElem.setAttribute("OrderedQty", orderLineTran.getAttribute("OrderedQty"));
						outputElem.setAttribute("TransactionalUOM",  orderLineTran.getAttribute("TransactionalUOM"));
					}
					else
					{
						outputElem.setAttribute("OrderedQty", orderLineElem.getAttribute("OrderedQty"));
						NodeList itemNodeList=orderLineElem.getElementsByTagName("Item");
						if(itemNodeList != null && itemNodeList.getLength() >0)
						{
							Element orderLineItem=(Element)itemNodeList.item(0);
							outputElem.setAttribute("TransactionalUOM",  orderLineItem.getAttribute("UnitOfMeasure"));
						}
					}
					String orderLineKey= orderLineElem.getAttribute("OrderLineKey");
					outputMap.put(orderLineKey,outputElem);
					
					
				}
			}
			
		}*/
		
		private void updateOrderLinesElem(Element orderListDoc,Map<String,Element> outputMap,String isPnACall,int callNumber,
				Map<String,Map<String,String>> uomListMap,Map<String,String> pricingUOMMap,Map<String,String> unitPricePricingUOMMap,
				Map<String,String>reqUnitPrice,Map<String,String>isPriceLockMap){
			
			String isDraftOrder = orderListDoc.getAttribute("DraftOrderFlag");
			ArrayList<Element> orderLinesElem=SCXmlUtil.getElements(orderListDoc, "OrderLines/OrderLine");
			int mapSize= outputMap.size();
			if(orderLinesElem!=null && orderLinesElem.size()>0 )
			{
				Iterator<Element> outputLinesIter = orderLinesElem.iterator();
				int lineId=0;
				int primeLineNo = 1;
				
				while(outputLinesIter.hasNext())
				{
					Element orderLineItem = outputLinesIter.next();
					Element orderLineExtnElem = (Element) orderLineItem.getElementsByTagName("Extn").item(0);
					Element newOrderLineElem = SCXmlUtil.createDocument("OrderLine").getDocumentElement();
					String orderLineKey = orderLineItem.getAttribute("OrderLineKey");
					String primeLineNum = orderLineItem.getAttribute("PrimeLineNo");
					String action = orderLineItem.getAttribute("Action");
					String orderQty=orderLineItem.getAttribute("OrderedQty");
					double orderVal=0;
					if(callNumber == 1)
					{
						createUOMConversionFactorMap(orderLineItem,uomListMap);
					}
					if(!SCUtil.isVoid(orderQty) )
					{
						orderVal=Double.parseDouble(orderQty);
					}
					if("CREATE".equals(action))
					{
						isCreateLine=true;
					}
					if("REMOVE".equals(action)  && outputMap.containsKey(orderLineKey))
					{						
							outputMap.remove(orderLineKey);
							isDeleteLine=true;
							continue;
						
					}
					else if(orderVal ==0 && !SCUtil.isVoid(orderQty))
					{
						if(outputMap != null)
						{
							outputMap.remove(orderLineKey);
							isDeleteLine=true;
						}
						outputMap.put(orderLineKey,createdDeletedLine(primeLineNumMap.get(orderLineKey)));
						continue;
					}
					else
					{
						NodeList tranNodeList=orderLineItem.getElementsByTagName("OrderLineTranQuantity");
						if(tranNodeList != null && tranNodeList.getLength() >0)
						{
							Element orderLineTran=(Element)tranNodeList.item(0);
							if(!SCUtil.isVoid(orderLineTran))
							{
								if(SCXmlUtil.hasAttribute(orderLineTran, "OrderedQty"))
								{
									orderVal=Double.parseDouble(orderLineTran.getAttribute("OrderedQty"));
									if(orderVal ==0)
									{
										if(outputMap != null)
										{
											outputMap.remove(orderLineKey);
											isDeleteLine=true;
										}
										outputMap.put(orderLineKey,createdDeletedLine(primeLineNumMap.get(orderLineKey)));
										continue;
									}
								}
							}
						}
					}
						/*else if( outputMap.containsKey(orderLineKey) )
						{
							Element linetypeOrderLineElem=outputMap.get(orderLineKey);
							if("M".equals(linetypeOrderLineElem.getAttribute("LineType")) && mapSize>0)
							{
								outputMap.remove(orderLineKey);
								continue;
							}
						}	*/	
					
					if(!outputMap.containsKey(orderLineKey))
					{
						
							if(YFCCommon.isVoid(orderLineKey))
							{
								orderLineKey=""+lineId;
								lineId=lineId+1;
							}
							if(YFCCommon.isVoid(primeLineNum))
							{
								//When primeLineNum is empty, it is a new line item and hence the primeLineNum of the new item
								if(outputMap!=null && !outputMap.isEmpty())
								{
									//if there are other items in cart, it will be highest primeLineNo among existing items + 1.
									//eg. If there are 5 existing items, the newly added item's primeLineNum will be 6.
									Collection<Element> allOrderLineElems = outputMap.values();
									Element[] allOrderLineElemsArr = allOrderLineElems.toArray(new Element[allOrderLineElems.size()]);
									Element lastOrderLineElem = allOrderLineElemsArr[allOrderLineElems.size()-1];
									if(lastOrderLineElem!=null)								
										primeLineNum = ""+(Integer.parseInt(lastOrderLineElem.getAttribute("PrimeLineNo"))+1);
										
								}
								else {
									//it is the first item in cart, so primeLineNumber starts with 1.
									primeLineNum = new Integer(primeLineNo).toString();
									primeLineNo++;
								}
							 }
							if(YFCCommon.isVoid(primeLineNum))
							{
								//When primeLineNum is empty, it is a new line item and hence the primeLineNum of the new item
								if(outputMap!=null && !outputMap.isEmpty())
								{
									//if there are other items in cart, it will be highest primeLineNo among existing items + 1.
									//eg. If there are 5 existing items, the newly added item's primeLineNum will be 6.
									Collection<Element> allOrderLineElems = outputMap.values();
									Element[] allOrderLineElemsArr = allOrderLineElems.toArray(new Element[allOrderLineElems.size()]);
									Element lastOrderLineElem = allOrderLineElemsArr[allOrderLineElems.size()-1];
									if(lastOrderLineElem!=null)
										
										primeLineNum = ""+(Integer.parseInt(lastOrderLineElem.getAttribute("PrimeLineNo"))+1);
								}
								else {
									//it is the first item in cart, so primeLineNumber starts with 1.
									primeLineNum = new Integer(primeLineNo).toString();
									primeLineNo++;
								}
							}
							newOrderLineElem.setAttribute("OrderLineKey",orderLineKey);
							newOrderLineElem.setAttribute("PrimeLineNo", primeLineNum);
							newOrderLineElem.setAttribute("OrderLineKey",orderLineKey);
							newOrderLineElem.setAttribute("LineType", orderLineItem.getAttribute("LineType"));
							/*if("M".equals(orderLineItem.getAttribute("LineType")))
							{
								
								if(orderLineExtnElem != null)
								{
									additionalAmountForSpecialCharge += Double.parseDouble(orderLineExtnElem.getAttribute("ExtnLineOrderedTotal"));
								}
							}*/
	
							
							NodeList itemNodeList = orderLineItem.getElementsByTagName("ItemDetails");
							
							
							//Element itemElem=(Element)orderLineItem.getElementsByTagName("Item").item(0);
							Element newItemElem= SCXmlUtil.createChild(newOrderLineElem, "Item");
							Element itemElem = null;
							if(itemNodeList != null && itemNodeList.getLength() >0)
							{
								itemElem = (Element)itemNodeList.item(0);
								newItemElem.setAttribute("ItemID", itemElem.getAttribute("ItemID"));
								
							}
							else
							{
								NodeList itemNodeListTemp = orderLineItem.getElementsByTagName("Item");
								if(itemNodeListTemp != null && itemNodeListTemp.getLength() >0)
								{
									itemElem = (Element)itemNodeListTemp.item(0);
									newItemElem.setAttribute("ItemID", itemElem.getAttribute("ItemID"));
									
								}
							}
							newOrderLineElem.setAttribute("OrderedQty",orderLineItem.getAttribute("OrderedQty"));
		
							Element orderTranElem= SCXmlUtil.createChild(newOrderLineElem, "OrderLineTranQuantity");
							NodeList tranNodeList=orderLineItem.getElementsByTagName("OrderLineTranQuantity");
							if(tranNodeList != null && tranNodeList.getLength() >0)
							{
								Element orderLineTran=(Element)tranNodeList.item(0);
								orderTranElem.setAttribute("OrderedQty", orderLineTran.getAttribute("OrderedQty"));
								orderTranElem.setAttribute("TransactionalUOM",  orderLineTran.getAttribute("TransactionalUOM"));
							}
							else
							{
								orderTranElem.setAttribute("OrderedQty", orderLineItem.getAttribute("OrderedQty"));
								orderTranElem.setAttribute("TransactionalUOM", itemElem!=null?itemElem.getAttribute("UnitOfMeasure"):"");
							}							
							if(orderLineExtnElem != null )
							{
								NodeList linePriceInfoList=orderLineItem.getElementsByTagName("LinePriceInfo");
								Element newLinePriceInfoElem= SCXmlUtil.createChild(newOrderLineElem, "LinePriceInfo");
								if(callNumber==1)
								{
									newLinePriceInfoElem.setAttribute("UnitPrice", orderLineExtnElem.getAttribute("ExtnAdjUOMUnitPrice"));
									
								}
								else
								{
									if(linePriceInfoList != null && linePriceInfoList.getLength() >0)
									{
										Element linePriceInfo=(Element)linePriceInfoList.item(0);
										newLinePriceInfoElem.setAttribute("UnitPrice", linePriceInfo.getAttribute("UnitPrice"));
									}
								}
								if(linePriceInfoList != null && linePriceInfoList.getLength() >0)
								{
										
									Element linePriceInfo=(Element)linePriceInfoList.item(0);
										newLinePriceInfoElem.setAttribute("IsPriceLocked", linePriceInfo.getAttribute("IsPriceLocked"));
										
								}
								reqUnitPrice.put(orderLineKey, orderLineExtnElem.getAttribute("ExtnReqUOMUnitPrice"));
								if((!"Y".equals(orderLineExtnElem.getAttribute("ExtnPriceOverrideFlag")))) {
									
										newLinePriceInfoElem.setAttribute("IsPriceLocked", "N");
									isPriceLockMap.put(orderLineKey, "N");
								}
								else
								{
									if("true".equals(isCom))
										newLinePriceInfoElem.setAttribute("IsPriceLocked", "Y");
									isPriceLockMap.put(orderLineKey, "Y");
								}
								/*if(!"true".equals(isPnACall) || ("N".equals(draftOrderFlag) && !"Y".equals(orderLineExtnElem.getAttribute("ExtnEditOrderFlag")) ))
								{
									newLinePriceInfoElem.setAttribute("IsPriceLocked", "Y");
								}*/
								
							}
								
							outputMap.put(orderLineKey,newOrderLineElem);
								
						}
						else
						{
							Element orderOutputElem=outputMap.get(orderLineKey);
							Element orderTranElem=(Element)orderOutputElem.getElementsByTagName("OrderLineTranQuantity").item(0);
							NodeList tranNodeList=orderLineItem.getElementsByTagName("OrderLineTranQuantity");
							if(tranNodeList != null && tranNodeList.getLength() >0)
							{
								Element orderLineTran=(Element)tranNodeList.item(0);
								orderTranElem.setAttribute("OrderedQty", orderLineTran.getAttribute("OrderedQty"));
								orderTranElem.setAttribute("TransactionalUOM",  orderLineTran.getAttribute("TransactionalUOM"));
							}
							Element linePriceInfoElem=(Element)orderOutputElem.getElementsByTagName("LinePriceInfo").item(0);
							NodeList linePriceInfoList=orderLineItem.getElementsByTagName("LinePriceInfo");
							if(linePriceInfoList != null && linePriceInfoList.getLength() >0)
							{
								Element linePriceInfo=(Element)linePriceInfoList.item(0);
								if(linePriceInfoElem == null)
								{
									linePriceInfoElem=SCXmlUtil.createChild(orderOutputElem, "LinePriceInfo");
									if("true".equals(isCom))
										linePriceInfoElem.setAttribute("IsPriceLocked",  linePriceInfo.getAttribute("IsPriceLocked"));
									else
										linePriceInfoElem.setAttribute("IsPriceLocked",  "N");
									isPriceLockMap.put(orderLineKey, linePriceInfo.getAttribute("IsPriceLocked"));
								}
								else
								{
									if(!"".equals(linePriceInfo.getAttribute("IsPriceLocked")))
									{
										if("true".equals(isCom))
											linePriceInfoElem.setAttribute("IsPriceLocked",  linePriceInfo.getAttribute("IsPriceLocked"));
										else
											linePriceInfoElem.setAttribute("IsPriceLocked",  "N");
										isPriceLockMap.put(orderLineKey, linePriceInfo.getAttribute("IsPriceLocked"));
										
									}
								}
								if(("Y".equals(orderLineExtnElem.getAttribute("ExtnPriceOverrideFlag")))) {	
									if("true".equals(isCom))
										linePriceInfoElem.setAttribute("IsPriceLocked", "Y");
								}
								if(!"".equals(linePriceInfo.getAttribute("UnitPrice")))
								{
									linePriceInfoElem.setAttribute("UnitPrice", linePriceInfo.getAttribute("UnitPrice"));
									reqUnitPrice.put(orderLineKey, linePriceInfo.getAttribute("UnitPrice"));
								}
								
							}
						}
						createPricingUOMMap(orderLineKey,orderLineExtnElem,pricingUOMMap,unitPricePricingUOMMap);
					}
				
			}
		}
		
	private Element createdDeletedLine(String primeLineNo)
	{
		Element deletedOrderLine = SCXmlUtil.createDocument("OrderLine").getDocumentElement();
		deletedOrderLine.setAttribute("isDeletedLine", "true");
		deletedOrderLine.setAttribute("PrimeLineNo", primeLineNo);
		return deletedOrderLine;
	}
	private void createUOMConversionFactorMap(Element orderLineElem,Map<String,Map<String,String>> uomListMap)	
	{
		ArrayList<Element> orderLinesElem=SCXmlUtil.getElements(orderLineElem, "ItemDetails");
		for(int i=0;i<orderLinesElem.size();i++)
		{
			Element orderLineItemDetailsElem=orderLinesElem.get(i);
			ArrayList<Element>  alternateUOMList=SCXmlUtil.getElements(orderLineItemDetailsElem, "AlternateUOMList/AlternateUOM");
			if(SCUtil.isVoid(alternateUOMList))
				return;
			Map<String,String> uomMap=new HashMap<String,String>();
			uomMap.put(orderLineItemDetailsElem.getAttribute("UnitOfMeasure"),"1");
			for(int j=0;j<alternateUOMList.size();j++)
			{
				Element alternateUOMElem=alternateUOMList.get(j);
				uomMap.put(alternateUOMElem.getAttribute("UnitOfMeasure"), alternateUOMElem.getAttribute("Quantity"));
			}
			uomListMap.put(orderLineItemDetailsElem.getAttribute("ItemID"), uomMap);
		}
	}
	private void createPricingUOMMap(String orderLineKey,Element orderLineExtn,Map<String,String> pricingUOMMap,Map<String,String> unitPricePricingUOMMap)
	{
		if(SCUtil.isVoid(orderLineExtn) || SCUtil.isVoid(pricingUOMMap))
			return;
		String pricingUOM=orderLineExtn.getAttribute("ExtnPricingUOM");
		if(!SCUtil.isVoid(pricingUOM))
			pricingUOMMap.put(orderLineKey, pricingUOM);
		
		if(SCUtil.isVoid(unitPricePricingUOMMap))
			return;
		String extnUnitPrice=orderLineExtn.getAttribute("ExtnUnitPrice");
		if(!SCUtil.isVoid(extnUnitPrice))
			unitPricePricingUOMMap.put(orderLineKey, extnUnitPrice);
	}
}

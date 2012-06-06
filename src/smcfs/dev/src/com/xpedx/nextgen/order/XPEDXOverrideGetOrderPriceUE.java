package com.xpedx.nextgen.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.shared.dbi.YFS_Order_Header;
import com.yantra.shared.omp.OMPFactory;
import com.yantra.shared.ycp.YFSContext;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfs.japi.YFSUserExitException;
import com.yantra.ypm.japi.ue.YPMOverrideGetOrderPriceUE;

public class XPEDXOverrideGetOrderPriceUE implements YPMOverrideGetOrderPriceUE {

	@Override
	public Document overrideGetOrderPrice(YFSEnvironment env, Document outputDoc)
			throws YFSUserExitException {
		try {
			String isPriceLock=null;
			String isSpecialItem=null;
			String specailItemID=null;
			String isDiscountCalculate=null;
			String isCouponApply=null;
			BigDecimal extnOrderTotalDiscount = new BigDecimal(0);
			BigDecimal extnOrderTotal = new BigDecimal(0);
			BigDecimal extnOrderSubTotal = new BigDecimal(0);
			BigDecimal orderTotalDiscountOnPrice = new BigDecimal(0);
			BigDecimal orderCouponDiscountOnPrice = new BigDecimal(0);
			BigDecimal linCouponDiscountOnPrice = new BigDecimal(0);
			HashMap<String,Element> couponMap=new HashMap<String,Element>();
			HashMap<String,Element> adjustmentActionMap=new HashMap<String,Element>();
			HashMap<String,String> unitPricePricingUOMMap=new HashMap<String,String>();
			HashMap<String,String> extendedPriceMap=new HashMap<String,String>();
			HashMap<String,String> pricingUOMMap=new HashMap<String,String>();
			Map<String,Map<String,String>> uomListMap=new HashMap<String,Map<String,String>>();
			HashMap<String,String> isPriceLockMap=new HashMap<String,String>();
			String ischangeOrderInprogress=null;
			// default values set
			String draftOrderFlag="Y";
			String hasPendingChanges="N";
			
			if (env instanceof ClientVersionSupport) {
				ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
				HashMap map = clientVersionSupport.getClientProperties();
				if (map != null) {
					isPriceLock =(String)map.get("isPriceLocking");
					isSpecialItem=(String)map.get("isSpecialItem");
					specailItemID=(String)map.get("specialItemID");
					isDiscountCalculate=(String)map.get("isDiscountCalculate");
					unitPricePricingUOMMap=(HashMap<String,String>)map.get("unitPricePricingUOMMap");
					//String isPnACall=(String)map.get("isPnACall");
					extendedPriceMap=(HashMap<String,String>)map.get("ExtendedPrice");
					pricingUOMMap=(HashMap<String,String>)map.get("PricingUOMMap");
					draftOrderFlag=(String)map.get("draftOrderFlag");
					hasPendingChanges=(String)map.get("hasPendingChanges");
					ischangeOrderInprogress=(String)map.get("ischangeOrderInprogress");
					uomListMap=(Map<String,Map<String,String>>)map.get("uomListMap");
					isPriceLockMap=(HashMap<String,String>)map.get("isPriceLockMap");
				}
			}
			
			//START adding AdjustmentAction into Adjustments from AdjustmentSummary
			Element orderElement = outputDoc.getDocumentElement();
			String orderHeaderKey=  orderElement.getAttribute("OrderReference");
			Element adjustmentActionElem=null;
			Node adjustSummaList = orderElement.getElementsByTagName("AdjustmentSummary").item(0);
			NodeList orderAdjustList =  adjustSummaList.getChildNodes();
			
			for(int i=0;i<orderAdjustList.getLength();i++)
			{
				Element orderAdjust = (Element) orderAdjustList.item(i);
				String ruleKey=orderAdjust.getAttribute("PricingRuleKey");
				adjustmentActionElem=(Element)orderAdjust.getElementsByTagName("AdjustmentAction").item(0);
				adjustmentActionMap.put(ruleKey, adjustmentActionElem);
			}
			
			Node couponListNode = orderElement.getElementsByTagName("Coupons").item(0);
			NodeList couponsList =  couponListNode.getChildNodes();
			
			for(int i=0;i<couponsList.getLength();i++)
			{
				Element couponElem = (Element) couponsList.item(i);
				String ruleKey=couponElem.getAttribute("CouponID");
				couponMap.put(ruleKey, couponElem);
			}
			
			Node adjusts = orderElement.getElementsByTagName("Adjustments").item(0);
			NodeList adjustList =  adjusts.getChildNodes();
			for(int i=0;i<adjustList.getLength();i++)
			{
				Element adjustElem = (Element) adjustList.item(i);
				String ruleKey=adjustElem.getAttribute("PricingRuleKey");
				extnOrderTotalDiscount=extnOrderTotalDiscount.add(new BigDecimal(adjustElem.getAttribute("AdjustmentApplied")));
				String couponDiscount=adjustElem.getAttribute("AdjustmentApplied");
				if(adjustElem.getElementsByTagName("AdjustmentAction").getLength()<=0)
				{
					Element orderadjustsAction=adjustmentActionMap.get(ruleKey);
					adjustElem.appendChild(orderadjustsAction.cloneNode(true));
					orderTotalDiscountOnPrice=orderTotalDiscountOnPrice.subtract(new BigDecimal(orderadjustsAction.getAttribute("OrderTotal")));
					
				}
				String adjustmentId=adjustElem.getAttribute("AdjustmentID");
				String isCoupon=adjustElem.getAttribute("IsCouponRule");
				if(adjustElem.getElementsByTagName("Coupon").getLength()<=0 && "Y".equals(isCoupon))
				{
					Element couponElem=couponMap.get(adjustmentId);
					adjustElem.appendChild(couponElem.cloneNode(true));
					isCouponApply="true";
					orderCouponDiscountOnPrice=orderCouponDiscountOnPrice.subtract(new BigDecimal(couponDiscount));
				}
			}
			
			Node orderLinesElem = orderElement.getElementsByTagName("OrderLines").item(0);
			NodeList orderLineNodeList =  orderLinesElem.getChildNodes();
			for(int i=0;i<orderLineNodeList.getLength();i++)
			{
				Element orderLineElem = (Element) orderLineNodeList.item(i);
				Node orderLineAdjustments = orderLineElem.getElementsByTagName("LineAdjustments").item(0);
				NodeList orderLineAdjustmentList = orderLineAdjustments.getChildNodes();
				
				for(int j=0; j<orderLineAdjustmentList.getLength();j++) {
					Element orderLineAdjustment = (Element) orderLineAdjustmentList.item(j);
					String lineAdjustmentId = orderLineAdjustment.getAttribute("AdjustmentID");
					String isCoupon=orderLineAdjustment.getAttribute("IsCouponRule");
					
					if(orderLineAdjustment.getElementsByTagName("Coupon").getLength()<=0 && "Y".equals(isCoupon))
					{
						Element couponElem=couponMap.get(lineAdjustmentId);
						orderLineAdjustment.appendChild(couponElem.cloneNode(true));
						linCouponDiscountOnPrice=linCouponDiscountOnPrice.subtract(new BigDecimal(orderLineAdjustment.getAttribute("AdjustmentApplied")));
					}
				}
			}
			//END adding AdjustmentAction into Adjustments from AdjustmentSummary
		
			
			// Discount calculation is done below
			if ((isDiscountCalculate !=null  && "true".equals(isDiscountCalculate)) && ischangeOrderInprogress == null
					&& !(YFCCommon.isVoid(orderHeaderKey))){
				SCXmlUtil.getString(outputDoc);
				BigDecimal orderTotalWithoutTaxes = new BigDecimal(0);
				BigDecimal extnLineTotal = new BigDecimal(0);
				BigDecimal specaiItemPrice=new BigDecimal(0);
				
				
				Node orderLinesList = orderElement.getElementsByTagName(
						"OrderLines").item(0);
				NodeList orderLines = orderLinesList.getChildNodes();

				String orderReference = orderElement
						.getAttribute("OrderReference");
				Document inputDocument = SCXmlUtil.createDocument("Order");
				SCXmlUtil.getString(inputDocument);
				
				// creating input document to stamp extn fields in OrderHeader and OrderLine tables.
				Element inputElement = inputDocument.getDocumentElement();
				inputElement.setAttribute("OrderHeaderKey", orderReference);
				/*if("N".equals(draftOrderFlag) && "Y".equals(hasPendingChanges))
				{
					Element pendingChangeElem = SCXmlUtil.createChild(inputElement,
					"PendingChanges");
					pendingChangeElem.setAttribute("RecordPendingChanges", "Y");
					inputElement.setAttribute("Override", "Y");
				}
				*/
				Element orderLinesEle = SCXmlUtil.createChild(inputElement,"OrderLines");
				int lineID=0;
				int length = orderLines.getLength();
				for (int i = 0; i < length; i++)
				{
					Element orderLineEle = (Element) orderLines.item(i);
					String lineTotPrice = orderLineEle
							.getAttribute("LineTotal");
					String orderLineKey=orderLineEle.getAttribute("LineID");
					String unitPrice = orderLineEle.getAttribute("UnitPrice");
					String quantity = orderLineEle.getAttribute("Quantity");
					BigDecimal qty = new BigDecimal(quantity);
					if (unitPrice == null || unitPrice.trim().length() == 0) {
						unitPrice = "0.0";
					}
					BigDecimal price = new BigDecimal(unitPrice);
					BigDecimal discount = new BigDecimal("0");
					String isPriceLockMapLock=isPriceLockMap.get(orderLineKey);
					
						Node orderLineAdjs = orderLineEle.getElementsByTagName(
								"LineAdjustments").item(0);
						NodeList lineAdjList = orderLineAdjs.getChildNodes();
						String adj ="0.0";
						double adjDouble=0;
						double lockPriceDiscount=0;
						for (int j = 0; j < lineAdjList.getLength(); j++) {
							Element orderLineAdjEle = (Element) lineAdjList.item(j);
							adj = orderLineAdjEle
									.getAttribute("AdjustmentPerLine"); 
							// AdjustmentPerLine, we don't get every time even if we have discount so checking 
							// AdjustmentAvailable if line is not present
							if (adj == null || adj.trim().length() == 0) {
								adj = orderLineAdjEle.getAttribute("AdjustmentAvailable");
							}
							if(adj == null || adj.trim().length() == 0) {
								adj = "0.0";
							}
								
							if(!"Y".equals(isPriceLockMapLock))
							{
								adjDouble += Double.parseDouble(adj);
							}
							else
							{
								lockPriceDiscount +=Double.parseDouble(adj);
							}
							
							
						}
						BigDecimal adjPrice = new BigDecimal(adjDouble);						
						discount = discount.subtract(adjPrice);
					BigDecimal unitDiscount = new BigDecimal(discount.doubleValue() / qty.doubleValue() );
					BigDecimal disUnitPrice = price.subtract(unitDiscount);
					Element changedOrderLineEle = SCXmlUtil.createChild(
							orderLinesEle, "OrderLine");
					
					if(YFCCommon.isVoid(orderLineKey))
					{
						orderLineKey=""+lineID;
						lineID +=1;
					}
					changedOrderLineEle.setAttribute("OrderLineKey",
							orderLineKey);
					changedOrderLineEle.setAttribute("Action", "MODIFY");
					/*if( isPriceLock !=null && ("true".equals(isPriceLock.trim())))
					{
						*/
							/*Element linePriceInfoElem= SCXmlUtil.createChild(
									changedOrderLineEle, "LinePriceInfo");
							linePriceInfoElem.setAttribute("IsPriceLocked", orderLineEle.getAttribute("IsPriceLocked"));*/
					//}
					Element changedOrderLineExtnEle = SCXmlUtil.createChild(
							changedOrderLineEle, "Extn");
					
					discount = discount.intValue() >0 ? discount.multiply(new BigDecimal("-1")):discount;
					linCouponDiscountOnPrice = linCouponDiscountOnPrice.intValue() >0 ? linCouponDiscountOnPrice.multiply(new BigDecimal("-1")):linCouponDiscountOnPrice;
					BigDecimal unitLinediscount=discount.subtract(linCouponDiscountOnPrice);
					unitLinediscount = unitLinediscount.intValue() >0 ? unitLinediscount.multiply(new BigDecimal("-1")):unitLinediscount;
					changedOrderLineExtnEle.setAttribute("ExtnReqUOMUnitPrice",	unitPrice);
					changedOrderLineExtnEle.setAttribute("ExtnAdjDollarAmt", discount.toString());
					changedOrderLineExtnEle.setAttribute("ExtnLegOrderLineAdjustments", discount.toString());
					changedOrderLineExtnEle.setAttribute("ExtnAdjUOMUnitPrice",disUnitPrice.toString());					
					changedOrderLineExtnEle.setAttribute("ExtnLineCouponDiscount",linCouponDiscountOnPrice.toString());
					changedOrderLineExtnEle.setAttribute("ExtnUnitPriceDiscount",(unitLinediscount).toString());
					if("Y".equals(isPriceLockMapLock))
					{
						changedOrderLineExtnEle.setAttribute("ExtnExtendedPrice", "".equals(orderLineEle.getAttribute("ExtendedPrice")) ?  "0" :orderLineEle.getAttribute("ExtendedPrice"));
					}
					if(unitPricePricingUOMMap != null && unitPricePricingUOMMap.size()>0)
					{
						String extnUnitPrice=unitPricePricingUOMMap.get(orderLineKey);
						changedOrderLineExtnEle.setAttribute("ExtnUnitPrice",extnUnitPrice );
						if(unitDiscount.doubleValue() >0)
						{
							BigDecimal extnUnitPriceB=new BigDecimal(extnUnitPrice);
							BigDecimal adjusteAmountOfExtnUnitPrice=getAdjustetedPriceInPricingCUOM(pricingUOMMap,uomListMap,orderLineEle,unitDiscount);
							extnUnitPriceB=extnUnitPriceB.subtract(adjusteAmountOfExtnUnitPrice);
							changedOrderLineExtnEle.setAttribute("ExtnAdjUnitPrice",extnUnitPriceB.toString() );
							changedOrderLineExtnEle.setAttribute("ExtnAdjustmentOfUnitPrice",adjusteAmountOfExtnUnitPrice.toString());
						}
						else
						{
							changedOrderLineExtnEle.setAttribute("ExtnAdjUnitPrice",extnUnitPrice);
							changedOrderLineExtnEle.setAttribute("ExtnAdjustmentOfUnitPrice","0");
						}
					}
					
					if(extendedPriceMap != null && extendedPriceMap.size()>0)
					{
						if(!(YFCCommon.isVoid(extendedPriceMap.get(orderLineKey))))
							changedOrderLineExtnEle.setAttribute("ExtnExtendedPrice", extendedPriceMap.get(orderLineKey));
					}
					if(pricingUOMMap != null && pricingUOMMap.size()>0)
					{
						changedOrderLineExtnEle.setAttribute("ExtnPricingUOM", pricingUOMMap.get(orderLineKey));
					}
					
					if(lineTotPrice==null || lineTotPrice.trim().length()==0){
						lineTotPrice="0.0";
					}
					
					extnLineTotal=new BigDecimal(lineTotPrice);
					extnLineTotal=extnLineTotal.subtract(new BigDecimal(lockPriceDiscount));
					extnOrderSubTotal=extnOrderSubTotal.add(extnLineTotal);
					changedOrderLineExtnEle.setAttribute(
							"ExtnLineOrderedTotal", extnLineTotal.toString());
					/*Element changedOrderLineItemEle = SCXmlUtil.createChild(
							changedOrderLineEle, "Item");
					
					changedOrderLineItemEle.setAttribute("ItemID", orderLineEle.getAttribute("ItemID"));*/
					if(isSpecialItem != null || !"".equals(isSpecialItem))
					{
						if(orderLineEle.getAttribute("ItemID").equals(specailItemID))
						{
							specaiItemPrice=specaiItemPrice.add(extnLineTotal);
						}
					}
					//changedOrderLineItemEle.setAttribute("UnitOfMeasure", orderLineEle.getAttribute("UnitOfMeasure"));
					
					orderTotalWithoutTaxes = orderTotalWithoutTaxes
					.add(new BigDecimal(lineTotPrice));
					extnOrderTotal=extnOrderTotal.add(extnLineTotal);
					
				}
				BigDecimal orderTotalWithDiscount=new BigDecimal(extnOrderTotal.toString());
				extnOrderTotal=extnOrderTotal.add(extnOrderTotalDiscount);
				if(isSpecialItem != null || !"".equals(isSpecialItem))
				{
					if(specaiItemPrice.doubleValue() >0 && (orderTotalWithDiscount.subtract(specaiItemPrice)).doubleValue() < orderTotalDiscountOnPrice.doubleValue())
					{
						extnOrderTotal=extnOrderTotal.subtract(extnOrderTotalDiscount);
					}
				}
				
				if(orderCouponDiscountOnPrice.intValue() >0)
				{
					orderCouponDiscountOnPrice=orderCouponDiscountOnPrice.multiply(new BigDecimal("-1"));
				}
				if(extnOrderTotalDiscount.intValue() >0)
				{
					extnOrderTotalDiscount=extnOrderTotalDiscount.multiply(new BigDecimal("-1"));
				}
				BigDecimal totalOrderAdjustment=(extnOrderTotalDiscount.subtract(orderCouponDiscountOnPrice));
				if(totalOrderAdjustment.intValue() >0)
				{
					totalOrderAdjustment=totalOrderAdjustment.multiply(new BigDecimal("-1"));
				}
				Element orderExtn= SCXmlUtil.createChild(
						inputElement, "Extn");
				orderExtn.setAttribute("ExtnOrderSubTotal", extnOrderSubTotal.toString());
				orderExtn.setAttribute("ExtnOrderCouponDiscount", orderCouponDiscountOnPrice.toString());
				orderExtn.setAttribute("ExtnOrderDiscount", totalOrderAdjustment.toString());
				orderExtn.setAttribute("ExtnTotOrderAdjustments", extnOrderTotalDiscount.toString());
				orderExtn.setAttribute("ExtnLegTotOrderAdjustments", extnOrderTotalDiscount.toString());
				orderExtn.setAttribute("ExtnTotOrdValWithoutTaxes", extnOrderTotal.toString());
				orderExtn.setAttribute("ExtnTotalOrderValue", extnOrderTotal.toString());
				/*if("N".equals(draftOrderFlag) && "Y".equals(hasPendingChanges))
				{
					storeResponseString(env,SCXmlUtil.getString(inputDocument),orderReference);
				}
				else
				{*/
				if(log.isDebugEnabled()){
				log.debug("Input doc in Price UE : "+SCXmlUtil.getString(inputDocument));
				}
					setProgressYFSEnvironmentVariables(env,inputDocument,isDiscountCalculate,isCouponApply,orderReference,
							createAdjustmentInfoDocument(env,outputDoc,orderReference));
				//}
			}
		} catch (NullPointerException cce) {
			log.debug(cce.getMessage());
		}
		catch (ArrayIndexOutOfBoundsException cce) {
			log.debug(cce.getMessage());
		}
		catch (Exception e) {
			log.error("Exception while changing the OrderPrice in User Exit...\n");
		}

		return outputDoc;
	}
	
	private BigDecimal getAdjustetedPriceInPricingCUOM(HashMap<String,String> pricingUOMMap,Map<String,Map<String,String>> uomListMap,
			Element orderLineElem,BigDecimal unitDiscount )
	{
		BigDecimal adjustedPrice=new BigDecimal(0);
		String itemID=orderLineElem.getAttribute("ItemID");
		String orderLineKey=orderLineElem.getAttribute("LineID");
		String requestedUOM=orderLineElem.getAttribute("UnitOfMeasure");
		if(SCUtil.isVoid(uomListMap) || SCUtil.isVoid(pricingUOMMap))
			return adjustedPrice;
		String pricingUOM=pricingUOMMap.get(orderLineKey);
		Map<String,String> uomsList=uomListMap.get(itemID);
		if(SCUtil.isVoid(uomsList))
			return adjustedPrice;
		String requestedUOMConvF=uomsList.get(requestedUOM);
		String pricingUOMConvF = uomsList.get(pricingUOM);
		if(SCUtil.isVoid(requestedUOMConvF) || SCUtil.isVoid(pricingUOMConvF))
		{
			return adjustedPrice;
		}
		else
		{
			double requestedUOMConvFDouble=Double.parseDouble(requestedUOMConvF);
			double pricingUOMConvFDouble=Double.parseDouble(pricingUOMConvF);
			double prcingDiscountConvF=requestedUOMConvFDouble/pricingUOMConvFDouble;
			double adjustedPriceD = unitDiscount.doubleValue()/prcingDiscountConvF ;
			adjustedPrice=new BigDecimal(adjustedPriceD);
		}
		return adjustedPrice;
	}
	private Element createAdjustmentInfoDocument(YFSEnvironment env,Document inputDoc,String orderHeaderKey)
	{
		Document outputDocument = SCXmlUtil.createDocument("XPXOrderAdjustmentsInfoList");
		Element adjustmentSummary=(Element)inputDoc.getElementsByTagName("AdjustmentSummary").item(0);
		
		if(!YFCCommon.isVoid(adjustmentSummary))
		{
			ArrayList<Element> adjustmentElemList=SCXmlUtil.getElements(adjustmentSummary, "Adjustment");
			if(!YFCCommon.isVoid(adjustmentElemList))
			{
				for(int i=0;i<adjustmentElemList.size();i++)
				{
					Element adjustmentElem=adjustmentElemList.get(i);
					Element orderAdjustmentInfoElem=SCXmlUtil.createChild(outputDocument.getDocumentElement(), "XPXOrderAdjustmentsInfo");
					String orderlineKey="";
					Element orderLineAdjustmentElem=(Element)adjustmentElem.getElementsByTagName("OrderLine").item(0);
					if(!YFCCommon.isVoid(orderLineAdjustmentElem))
					{
						orderlineKey=orderLineAdjustmentElem.getAttribute("LineID");
					}
					String adjustment="";
					String adjustmentType="";
					String chargeCategory="";
					String chargeName="";
					String orderTotal="";
					String quantity="";
					String whenToApply="";
					String pricingRuleActionKey="";
					Element adjustmentAction=(Element)adjustmentElem.getElementsByTagName("AdjustmentAction").item(0);
					if(!YFCCommon.isVoid(adjustmentAction))
					{
						adjustment=adjustmentAction.getAttribute("Adjustment");
						adjustmentType=adjustmentAction.getAttribute("AdjustmentType");
						chargeCategory=adjustmentAction.getAttribute("ChargeCategory");
						chargeName=adjustmentAction.getAttribute("ChargeName");
						orderTotal=adjustmentAction.getAttribute("OrderTotal");
						quantity=adjustmentAction.getAttribute("Quantity");
						whenToApply=adjustmentAction.getAttribute("WhenToApply");
						pricingRuleActionKey=adjustmentAction.getAttribute("PricingRuleActionKey");
					}
					orderAdjustmentInfoElem.setAttribute("OrderHeaderKey", orderHeaderKey);
					orderAdjustmentInfoElem.setAttribute("OrderLineKey", orderlineKey);
					orderAdjustmentInfoElem.setAttribute("AdjustmentID", adjustmentElem.getAttribute("AdjustmentID"));
					orderAdjustmentInfoElem.setAttribute("AdjustmentApplied", adjustmentElem.getAttribute("AdjustmentApplied"));
					orderAdjustmentInfoElem.setAttribute("AdjustmentAvailable", adjustmentElem.getAttribute("AdjustmentAvailable"));
					orderAdjustmentInfoElem.setAttribute("Currency",adjustmentElem.getAttribute("Currency"));
					orderAdjustmentInfoElem.setAttribute("Description",adjustmentElem.getAttribute("Description"));
					orderAdjustmentInfoElem.setAttribute("DistributedAdjustment", adjustmentElem.getAttribute("DistributeAdjustment"));
					orderAdjustmentInfoElem.setAttribute("EndDateActive", adjustmentElem.getAttribute("EndDateActive"));
					orderAdjustmentInfoElem.setAttribute("OrganizationCode", adjustmentElem.getAttribute("OrganizationCode"));
					orderAdjustmentInfoElem.setAttribute("PricingRuleKey",  adjustmentElem.getAttribute("PricingRuleKey"));
					orderAdjustmentInfoElem.setAttribute("PricingRuleName",  adjustmentElem.getAttribute("PricingRuleName"));
					orderAdjustmentInfoElem.setAttribute("PricingStatus",  adjustmentElem.getAttribute("PricingStatus"));
					orderAdjustmentInfoElem.setAttribute("RuleCategory", adjustmentElem.getAttribute("RuleCategory"));
					orderAdjustmentInfoElem.setAttribute("RuleType", adjustmentElem.getAttribute("RuleType"));
					orderAdjustmentInfoElem.setAttribute("StartDateActive",adjustmentElem.getAttribute("StartDateActive"));
					orderAdjustmentInfoElem.setAttribute("Adjustment", adjustment);
					orderAdjustmentInfoElem.setAttribute("AdjustmentType", adjustmentType);
					orderAdjustmentInfoElem.setAttribute("ChargeCategory", chargeCategory);
					orderAdjustmentInfoElem.setAttribute("ChargeName", chargeName);
					orderAdjustmentInfoElem.setAttribute("OrderTotal", orderTotal);					
					orderAdjustmentInfoElem.setAttribute("Quantity", quantity);
					orderAdjustmentInfoElem.setAttribute("WhenToApply", whenToApply);
					orderAdjustmentInfoElem.setAttribute("PricingRuleActionKey", pricingRuleActionKey);
					orderAdjustmentInfoElem.setAttribute("IsItemRule", adjustmentElem.getAttribute("IsItemRule"));
					orderAdjustmentInfoElem.setAttribute("IsCouponRule", adjustmentElem.getAttribute("IsCouponRule"));
					orderAdjustmentInfoElem.setAttribute("IgnoreRuleOnPriceLock", adjustmentElem.getAttribute("IgnoreRuleOnPriceLock"));					
					orderAdjustmentInfoElem.setAttribute("IsItemAttributeValueRule",adjustmentElem.getAttribute("IsItemAttributeValueRule"));
					
				}
			}
			
		}
		return outputDocument.getDocumentElement();
	}
	
	private void setProgressYFSEnvironmentVariables(YFSEnvironment env,Document inputDoc,String isDiscountCalculate,String isCouponApply,String orderHeaderKey,Element adjustmentInfo) {
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				envVariablesmap.put("changeOrderDocument", inputDoc);
				envVariablesmap.put("isCouponApply",isCouponApply);
				envVariablesmap.put("isDiscountCalculate", isDiscountCalculate);
				envVariablesmap.put("ischangeOrderInprogress", "true");
				envVariablesmap.put("adjustmentInfoElem",adjustmentInfo);
			}
			else
			{
				envVariablesmap=new HashMap();
				envVariablesmap.put("changeOrderDocument", inputDoc);
				envVariablesmap.put("isDiscountCalculate", isDiscountCalculate);
				envVariablesmap.put("isCouponApply",isCouponApply);
				envVariablesmap.put("ischangeOrderInprogress", "true");
				envVariablesmap.put("adjustmentInfoElem",adjustmentInfo);
			}
			clientVersionSupport.setClientProperties(envVariablesmap);
		}
	}
	private YFSContext  getContext(YFSEnvironment env) {
		if ( env instanceof YFSContext )
			return (YFSContext)env;
		else
			return null;
	}
	
	private static YIFApi api = null;
	private static final Logger log = Logger
			.getLogger(XPEDXOverrideGetOrderPriceUE.class);
}

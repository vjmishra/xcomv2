/*
 *ajindal
 *$Id: XPEDXOrderPlaceAction.java,v 1.29 2011/11/17 08:23:07 rghare Exp $
 *
 */
package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.order.OrderConstants;
import com.sterlingcommerce.webchannel.order.OrderSaveBaseAction;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;
import com.sterlingcommerce.webchannel.order.utilities.CreditCardCVVStorageHelper;
import com.sterlingcommerce.webchannel.order.utilities.PaymentTypeHelper;
import com.sterlingcommerce.webchannel.utilities.BusinessRuleUtil;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXPaymentMethodHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfs.japi.YFSException;

public class XPEDXOrderPlaceAction extends OrderSaveBaseAction {
	/** Logger. */
	protected final Logger log = Logger.getLogger(XPEDXOrderPlaceAction.class);

	protected static final String VALIDATE_ITEM_FOR_ORDERING_MASHUP_ID = "me_validateItemForOrdering";
	
	protected static final String CHANGE_ORDER_DATE_MASHUP_ID = "xpedx_me_changeOrderDate";

//	protected static final String ORDER_BILL_TO_ADDRESS_ID = "me_getBillToAddressForValidation";
	protected static final String ORDER_BILL_TO_ADDRESS_ID = "me_getBillToAddressForValidationAndSourceForMaxChargeAmounts";
	
	protected static final String PROCESS_ORDER_PAYMENT_MASHUP_ID = "me_processOrderPayment";

	protected static final String CONFIRM_DRAFT_ORDER_MASHUP_ID = "xpedxConfirmDraftOrder";

	protected static final String GET_CONFIRMATION_PAGE_DETAILS = "XPEDXGetConfirmationPageDetails";

	protected static final String SUB_ITEM_DETAILS_MASHUP_ID = "subItemDetails";

	protected static final String ZERO_CHARGE_AMOUNT = "0.00";

	protected static final String ORDER = "Order";

	/** Result type for order place API failure. */
	protected static final String FAILURE = "failure";

	/** Output doucment for order place result. */
	protected Element outDoc = null;

	protected String orderHeaderKey = "";

	protected String parentOrderHeaderKeyForFO = "";

	protected Element orderEl;

	protected List<String> paymentKeys = new ArrayList<String>();

	protected List<String> cvvList = new ArrayList<String>();

	protected List<String> maxChargeLimits = new ArrayList<String>();

	protected List<String> unlimitedCharges = new ArrayList<String>();

	protected String hasItemValidationException = "N";

	protected Element processOrderPaymentElem = null;

	protected String hasProcessOrderPaymentException = "N";

	protected String hasPaymentErrorMessage = "N";

	protected Element confirmDraftOrderElem = null;

	protected List itemValidationExceptionList = new ArrayList();

	protected List<String> ftcOrderLineKeyList = new ArrayList<String>();

	protected List<String> ftcActualDateList = new ArrayList<String>();

	protected String itemID = "";

	protected String unitOfMeasure = "";

	protected String generatedErrorMessage = null;

	protected String customerAccountBalanceCheckError = null;

	protected UtilBean utilBean = new UtilBean();

	/**
	 * The currency received from the caller and used as inputs to the mashups.
	 */
	protected String currency;

	private String approvalHoldStatus;
	
	private String orderPlaceDate;

	private String resolverUserID;
	//added for jira 3484
	private String primaryApproverID;
	private String proxyApproverID;
	private String primaryApprovalEmailId;
	private String proxyApprovalEmailId;
	private static final String CHANGE_ORDEROUTPUT_ORDER_UPDATE_SESSION_OBJ = "changeOrderAPIOutputForOU";
	protected boolean isCSRReview = false;
	public boolean isCSRReview() {
		return isCSRReview;
	}

	public void setCSRReview(boolean isCSRReview) {
		this.isCSRReview = isCSRReview;
	}

	public String getPrimaryApprovalEmailId() {
		return primaryApprovalEmailId;
	}

	public void setPrimaryApprovalEmailId(String primaryApprovalEmailId) {
		this.primaryApprovalEmailId = primaryApprovalEmailId;
	}

	public String getProxyApprovalEmailId() {
		return proxyApprovalEmailId;
	}

	public void setProxyApprovalEmailId(String proxyApprovalEmailId) {
		this.proxyApprovalEmailId = proxyApprovalEmailId;
	}
	
	public String getPrimaryApproverID() {
		return primaryApproverID;
	}

	public void setPrimaryApproverID(String primaryApproverID) {
		this.primaryApproverID = primaryApproverID;
	}

	public String getProxyApproverID() {
		return proxyApproverID;
	}

	public void setProxyApproverID(String proxyApproverID) {
		this.proxyApproverID = proxyApproverID;
	}
	//end of jira 3484
	
	private boolean  isOrderOnApprovalHoldStatus;
	
	protected HashMap xpedxChainedOrderListMap=new HashMap();
	
	public HashMap getXpedxChainedOrderListMap() {
		return xpedxChainedOrderListMap;
	}
	
	public void setXpedxChainedOrderListMap(HashMap xpedxChainedOrderListMap) {
		this.xpedxChainedOrderListMap = xpedxChainedOrderListMap;
	}
	

	public boolean isOrderOnApprovalHoldStatus() {
		return isOrderOnApprovalHoldStatus;
	}

	public void setOrderOnApprovalHoldStatus(boolean isOrderOnApprovalHoldStatus) {
		this.isOrderOnApprovalHoldStatus = isOrderOnApprovalHoldStatus;
	}

	public String execute() {
		try {
			YFCDate orderDate = new YFCDate();
			orderPlaceDate = orderDate.getString();
			
			//Commented this line as it is causing exception. Looks like the mashup is not defined.
			if (isDraftOrder()) {
//				prepareAndInvokeMashup(CHANGE_ORDER_DATE_MASHUP_ID);
				prepareAndInvokeMashup("XPXIsPendingApprovalOrder");
				confirmDraftOrderElem = prepareAndInvokeMashup(CONFIRM_DRAFT_ORDER_MASHUP_ID);
				String cartInContextOrderHeaderKey=(String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");
				if (confirmDraftOrderElem != null&& orderHeaderKey.equals(cartInContextOrderHeaderKey)) {
					// Flush the cart in context if it is the one we've been working with.
					CommerceContextHelper.flushCartInContextCache(getWCContext());
					XPEDXWCUtils.removeObectFromCache("OrderHeaderInContext");
					//Remove itemMap from Session, when cart change in context,  For Minicart Jira 3481
					XPEDXWCUtils.removeObectFromCache("itemMap");
					XPEDXOrderUtils.refreshMiniCart(getWCContext(),null,true,false,XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
				}
			} else {//order update flow
				/*Begin - Changes made by Mitesh Parikh for JIRA#3594*/
				Document orderDetailDocument = (Document)getWCContext().getSCUIContext().getSession().getAttribute(CHANGE_ORDEROUTPUT_ORDER_UPDATE_SESSION_OBJ);
				getWCContext().getSCUIContext().getSession().removeAttribute(CHANGE_ORDEROUTPUT_ORDER_UPDATE_SESSION_OBJ);
				setOrderHeaderKey(SCXmlUtil.getAttribute(orderDetailDocument.getDocumentElement(), "OrderHeaderKey"));
				/*End - Changes made by Mitesh Parikh for JIRA#3594*/
				if(YFCCommon.isVoid(orderHeaderKey)){
					generatedErrorMessage = "Could not process the order with no header key.Please contact system admin.";
					log.error("OrderHeaderKey is empty. Cannot process OrderChange.");
					return FAILURE;
				}
				
				prepareAndInvokeMashup("XPXIsPendingApprovalOrder");
				
				/*Begin - Changes made by Mitesh Parikh for JIRA#3594*/
				//get the order details
				/*Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("/Order/@OrderHeaderKey", orderHeaderKey);
				Element input = WCMashupHelper.getMashupInput("XPEDXOrderDetailForOrderUpdate", valueMap, wcContext.getSCUIContext());
				Object obj;
				Document orderDetailDocument = null;				
				
				try {
					obj = WCMashupHelper.invokeMashup("XPEDXOrderDetailForOrderUpdate", input, wcContext.getSCUIContext());
					if(!YFCCommon.isVoid(obj)){
						orderDetailDocument = ((Element) obj).getOwnerDocument();
					}
				} catch (Exception e) {
					generatedErrorMessage = "Error getting the order details from database.Please contact system admin.";
					log.error("Exception getting order detail xml for order update..\n",e);
					XPEDXWCUtils.logExceptionIntoCent(e.getMessage());
					return FAILURE;
					
				}*/
				/*End - Changes made by Mitesh Parikh for JIRA#3594*/
				if(orderDetailDocument != null)
				{
					changeOutputDocToOrderUpdateDoc(orderDetailDocument.getDocumentElement());
					//LOG.debug("Order Input to xpedxOrderUpdateToLegacyFlow : "+SCXmlUtil.getString(orderDetailDocument));
				}
				
				/*call the customized service XPXUpdateChainedOrder with the order XML which propagates the Fulfillment 
				order changes to Customer Order and sends the new order to Legacy*/
				try 
				{
					Object orderUpdateObj = WCMashupHelper.invokeMashup("xpedxOrderUpdateToLegacyFlow", orderDetailDocument.getDocumentElement(), wcContext.getSCUIContext());
					
					if(orderUpdateObj != null)
					{
						Element orderUpdateElem=(Element)orderUpdateObj;
						if(orderUpdateElem.hasAttribute("TransactionMessage"))
						{
							String transactionMessage=orderUpdateElem.getAttribute("TransactionMessage");
							if(!YFCCommon.isVoid(transactionMessage))
							{
									try
									{
										Document error=SCXmlUtil.createFromString(transactionMessage);
										if(error == null)
										{
											generatedErrorMessage = transactionMessage;
											return FAILURE;
										}
										generatedErrorMessage = "Error posting the order update to Legacy system.Please contact system admin.";
										log.error("Exception posting the order update to Legacy system..\n");
										return FAILURE;
									}
									catch(Exception e)
									{
										generatedErrorMessage = transactionMessage;
										XPEDXWCUtils.logExceptionIntoCent(e.getMessage());
										return FAILURE;
									}
							}
								
						}
					}
					
				} catch (Exception e) {
					generatedErrorMessage = "Error posting the order update to Legacy system.Please contact system admin.";
					log.error("Exception posting the order update to Legacy system..\n",e);
					XPEDXWCUtils.logExceptionIntoCent(e.getMessage());
					return FAILURE;
				}
				
				// CR 2997 - Updated for Removing the EditOrderHeaderKey from session after placing an order(Success/Failure) in Edit Order Flow
				if(YFCCommon.isVoid(generatedErrorMessage))
				{
					wcContext.getSCUIContext().getSession().removeAttribute(XPEDXConstants.EDITED_ORDER_HEADER_KEY);
				}
				
				//the output required for the confirmation page; do the necessary getCompleteOrderDetails call.
				confirmDraftOrderElem = prepareAndInvokeMashup(GET_CONFIRMATION_PAGE_DETAILS);				
				ArrayList<Element> orderLineNodeList=SCXmlUtil.getElements(confirmDraftOrderElem,"OrderLines/OrderLine");
				parentOrderHeaderKeyForFO = orderLineNodeList.get(0).getAttribute("ChainedFromOrderHeaderKey"); 
			}
			isOrderOnApprovalHoldStatus=isOrderOnApprovalHold();
			//added for jira 3484 for display of approvers EMailID
			if(isOrderOnApprovalHoldStatus)
			{
				if(primaryApproverID != null){
					resolverUserID = primaryApproverID;
					if(primaryApproverID != null && proxyApproverID != null){
						resolverUserID = primaryApproverID+","+proxyApproverID;
					}
				}
				else{
					resolverUserID = proxyApproverID;
				}
				if(resolverUserID != null){
					Map<String,Element> usersInfoMap=XPEDXWCUtils.getUsersInfoMap(resolverUserID, wcContext.getStorefrontId());
					if(usersInfoMap.get(primaryApproverID) != null)
						primaryApprovalEmailId=usersInfoMap.get(primaryApproverID).getAttribute("EmailID");
					if(usersInfoMap.get(proxyApproverID) != null)
						proxyApprovalEmailId=usersInfoMap.get(proxyApproverID).getAttribute("EmailID");
				}
//end of jira 3484
			}
			ArrayList<String> chainedOrderFromKeylist = new ArrayList<String>();
			chainedOrderFromKeylist.add(orderHeaderKey);
			Document chainedOrderLineListDoc = getXpedxChainedOrderLineList(chainedOrderFromKeylist);
			setXpedxChainedOrderMap(chainedOrderLineListDoc);
			return SUCCESS;
		} catch (Exception ex) {
			log.error("Unexpected error while placing the order. "+ex.getMessage(), ex);
			generatedErrorMessage = "There was an error processing your last request. Please contact the Customer Support desk at 877 269-1784, eBusiness@ipaper.com";//Message changed - JIRA 3221
			XPEDXWCUtils.logExceptionIntoCent(ex.getMessage());
			return FAILURE;
		}
	}

	private void changeOutputDocToOrderUpdateDoc(Element outputDoc)
	{
		ArrayList<Element> outputOrderLineNodeList=SCXmlUtil.getElements(outputDoc,"OrderLines/OrderLine");
		Iterator<Element> outputIt=outputOrderLineNodeList.iterator();
		
		//Keeping only latest instructions
		ArrayList<Element> instructionsNodeList=SCXmlUtil.getElements(outputDoc,"Instructions/Instruction");
		if(!YFCCommon.isVoid(instructionsNodeList))
		{
			double maxInstructionDetailsKey=0;
			Element instructionsElem=(Element)outputDoc.getElementsByTagName("Instructions").item(0);
			instructionsElem.setAttribute("NumberOfInstructions", "1");
			for(int i=0;i<instructionsNodeList.size();i++)
			{
				Element instuctionElem=instructionsNodeList.get(i);
				String instructionKey=instuctionElem.getAttribute("InstructionDetailKey");
				
				if(!YFCCommon.isVoid(instructionKey))
				{
					double _instructionKey=Double.parseDouble(instructionKey);
					if(_instructionKey < maxInstructionDetailsKey)
					{
						instructionsElem.removeChild(instuctionElem);
					}
					else
					{
						maxInstructionDetailsKey=_instructionKey;
					}
					
				}
				else if(maxInstructionDetailsKey != 0)
				{
					
					instructionsElem.removeChild(instuctionElem);
				}
				
			}
		}
		
		
		Element pendingChangeElem=(Element)outputDoc.getElementsByTagName("PendingChanges").item(0);
		if(YFCCommon.isVoid(pendingChangeElem))
		{
			pendingChangeElem=SCXmlUtil.createChild(outputDoc, "PendingChanges");
		}
		else
		{
			pendingChangeElem.setAttribute("RecordPendingChanges", "N");
		}
		
		pendingChangeElem.setAttribute("ResetPendingChanges", "Y");
		while(outputIt.hasNext())
		{
			Element orderLine=outputIt.next();
			Element orderLineExtn=(Element)orderLine.getElementsByTagName("Extn").item(0);
			String editOrderflag=orderLineExtn.getAttribute("ExtnEditOrderFlag");
			if("Y".equals(editOrderflag))
			{
				orderLine.setAttribute("Action", "CREATE");
				orderLine.setAttribute("OrderLineKey", "");
				orderLine.setAttribute("PrimeLineNo", "");
				orderLine.setAttribute("SubLineNo", "");
				
			}
			orderLineExtn.setAttribute("ExtnEditOrderFlag", "N");
		}
	}
	
	public String getOrderPlaceDate() {
		return orderPlaceDate;
	}

	public void setOrderPlaceDate(String orderPlaceDate) {
		this.orderPlaceDate = orderPlaceDate;
	}

	public Element getOutDoc() {
		return outDoc;
	}

	public void setOutDoc(Element outDoc) {
		this.outDoc = outDoc;
	}

	public String getOrderHeaderKey() {
		return orderHeaderKey;
	}

	public void setOrderHeaderKey(String orderHeaderKey) {
		this.orderHeaderKey = orderHeaderKey;
	}

	public List getPaymentKeys() {
		return paymentKeys;
	}

	public List getMaxChargeLimits() {
		return maxChargeLimits;
	}

	public List getUnlimitedCharges() {
		return unlimitedCharges;
	}

	public String getHasItemValidationException() {
		return hasItemValidationException;
	}

	public Element getProcessOrderPaymentElem() {
		return processOrderPaymentElem;
	}

	public String getHasProcessOrderPaymentException() {
		return hasProcessOrderPaymentException;
	}

	public Element getConfirmDraftOrderElem() {
		return confirmDraftOrderElem;
	}

	public List getItemValidationExceptionList() {
		return itemValidationExceptionList;
	}

	public String getGeneratedErrorMessage() {
		return generatedErrorMessage;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public String getItemID() {
		return itemID;
	}

	public List<String> getFtcOrderLineKeyList() {
		return ftcOrderLineKeyList;
	}

	public List<String> getFtcActualDateList() {
		return ftcActualDateList;
	}

	public String getSkipBOMValidationsValue() {
		String ruleValue = BusinessRuleUtil.getBusinessRule(
				"SWC_VALIDATE_CART_ITEMS_ON_CHECKOUT", "0001", getWCContext());
		if ("Y".equals(ruleValue)) {
			return "N";
		} else {
			return "Y";
		}
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getHasPaymentErrorMessage() {
		return hasPaymentErrorMessage;
	}

	public void setHasPaymentErrorMessage(String hasPaymentErrorMessage) {
		this.hasPaymentErrorMessage = hasPaymentErrorMessage;
	}

	public List<String> getCvvList() {
		return cvvList;
	}

	public void setCvvList(List<String> cvvList) {
		this.cvvList = cvvList;
	}

	public String getCustomerAccountBalanceCheckError() {
		return customerAccountBalanceCheckError;
	}

	public void setCustomerAccountBalanceCheckError(
			String customerAccountBalanceCheckError) {
		this.customerAccountBalanceCheckError = customerAccountBalanceCheckError;
	}

	// Check if the current order has approval hold or not (any status) to show
	// the approval-history link
	public boolean isOrderOnApprovalHold() {
		String holdTypeForApproval = XPEDXConstants.HOLD_TYPE_FOR_PENDING_APPROVAL;
		if (holdTypeForApproval != null) {
			Element orderholdtypeselem = SCXmlUtils.getInstance()
					.getChildElement(this.confirmDraftOrderElem,
							OrderConstants.ORDER_HOLD_TYPES);
			ArrayList<Element> orderholdtypeelemlist = SCXmlUtils.getInstance()
					.getChildren(orderholdtypeselem,
							OrderConstants.ORDER_HOLD_TYPE);
			for (Iterator<Element> iter = orderholdtypeelemlist.iterator(); iter
					.hasNext();) {
				Element orderholdtypeelem = (Element) iter.next();
				if ((orderholdtypeelem.getAttribute(OrderConstants.HOLD_TYPE))
						.trim().equals(holdTypeForApproval)) {
					String holdstatus = orderholdtypeelem.getAttribute(
							OrderConstants.STATUS).trim();
					this.approvalHoldStatus = holdstatus;
					this.resolverUserID = orderholdtypeelem
							.getAttribute(OrderConstants.RESOLVER_USER_ID);
				    //modified for jira 3484
					if(this.resolverUserID != null ){
						String approverUserIDs [] = this.resolverUserID.split(",");
						if(approverUserIDs[0] != null)
							primaryApproverID = approverUserIDs[0];
						if(approverUserIDs.length > 1){
							if(approverUserIDs[1] != null)
								proxyApproverID = approverUserIDs[1];	
						}
					}
//end of jira 3484
					return true;
				}
			}
		}
		return false;
	}
	
	// modified for jira 4092 to Check if the current order has CSR Review hold or not
	public boolean isOrderOnNeedsAttentionHold() {
		String holdTypeNeedsAttention = XPEDXConstants.HOLD_TYPE_FOR_NEEDS_ATTENTION;
		String holdTypeLegacyCnclOrd = XPEDXConstants.HOLD_TYPE_FOR_LEGACY_CNCL_ORD_HOLD;
		String holdTypeOrderException = XPEDXConstants.HOLD_TYPE_FOR_ORDER_EXCEPTION_HOLD;
		String openHoldStatus = OrderConstants.OPEN_HOLD_STATUS;
		
		Element orderholdtypeselem = SCXmlUtil.getChildElement(this.confirmDraftOrderElem, OrderConstants.ORDER_HOLD_TYPES);
		ArrayList<Element> orderholdtypeelemlist = SCXmlUtil.getElements(orderholdtypeselem, OrderConstants.ORDER_HOLD_TYPE);
		for (Iterator<Element> iter = orderholdtypeelemlist.iterator(); iter.hasNext();) {
			Element orderholdtypeelem = (Element) iter.next();
			String orderHoldType = orderholdtypeelem.getAttribute(OrderConstants.HOLD_TYPE);
			if ((orderholdtypeelem.getAttribute(OrderConstants.STATUS).equals(openHoldStatus))
					&& (orderHoldType.equals(holdTypeNeedsAttention)
							|| orderHoldType.equals(holdTypeLegacyCnclOrd) 
							|| orderHoldType.equals(holdTypeOrderException)))
					return true;
		}
		return false;
	}

	public String getResolverUserID() {
		return resolverUserID;
	}

	public void setResolverUserID(String resolverUserID) {
		this.resolverUserID = resolverUserID;
	}	
	
	protected Document getXpedxChainedOrderLineList(ArrayList<String> chainedOrderFromKeylist) throws Exception{
		Document outputDoc = null;

		if (null == chainedOrderFromKeylist || chainedOrderFromKeylist.isEmpty()) {
			log
					.debug("getXpedxChainedOrderLineList: Atleast one order header key is required. Use getOrderLineList API to get all the orderlines");
			return outputDoc;
		}

		Element input = WCMashupHelper.getMashupInput("xpedxChainedOrderLineList",getWCContext().getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element)inputNodeList.item(0);
		for(int i=0;i<chainedOrderFromKeylist.size();i++){
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			Element expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", "ChainedFromOrderHeaderKey");
			expElement.setAttribute("Value", chainedOrderFromKeylist.get(i));
			
			inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
		}
		inputXml = SCXmlUtil.getString(input);
		log.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxChainedOrderLineList", input,
				getWCContext().getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}

		return outputDoc; 
	}
	
	protected void setXpedxChainedOrderMap(Document chainedOrderLineList) throws Exception{
		if(null == chainedOrderLineList){
			LOG.debug("setXpedxChainedOrderMap: Empty chainedOrderLineList.... No chained orders");
			return;
		}
		//get all the Orderlines from the document
		NodeList nlOrderLineList = chainedOrderLineList.getElementsByTagName("OrderLine");
		int length = nlOrderLineList.getLength();
		ArrayList chainedOrders = new ArrayList();
		ArrayList<String> alreadyAddedOrders = new ArrayList<String>();
		Element orderLine = null;
		
		for(int i=0;i<length;i++){
			//Get each orderline
			orderLine = (Element) nlOrderLineList.item(i);
			//Extract Order element from each orderline
			Element order = XMLUtilities.getElement(orderLine, "Order");
			//put the Order element in the xpedxChainedOrderListMap. The key will be the "OrderLine/ChainedFromOrderHeaderKey"
			String chainedFromOHK = SCXmlUtil.getAttribute(orderLine, "ChainedFromOrderHeaderKey");
			String orderHeaderKey = SCXmlUtil.getAttribute(order, "OrderHeaderKey");
			//added for jira 4092 - display of Submitted CSR reviewing in the order confirmation page
			Element orderExtn = SCXmlUtil.getChildElement(order, "Extn");
			String legacyOrderNumber = orderExtn.getAttribute("ExtnLegacyOrderNo");
			String headerStatusCode = orderExtn.getAttribute("ExtnHeaderStatusCode");
			if(null == legacyOrderNumber || "".equals(legacyOrderNumber.trim()) ||( null != headerStatusCode && !"".equals(headerStatusCode.trim()) && !headerStatusCode.equals("M0000"))) {
				isCSRReview = true;
			}
			//end of jira 4092
			if(!alreadyAddedOrders.contains(orderHeaderKey)){
				if(xpedxChainedOrderListMap.containsKey(chainedFromOHK)){
					ArrayList<Element> orderList = (ArrayList<Element>) xpedxChainedOrderListMap.get(chainedFromOHK);
					orderList.add(order);
					ArrayList clonedOrderList = (ArrayList<Element>)orderList.clone();
					xpedxChainedOrderListMap.remove(chainedFromOHK);
					xpedxChainedOrderListMap.put(chainedFromOHK, clonedOrderList);
				}else{
					ArrayList<Element> orderList = new ArrayList<Element>();
					orderList.add(order);
					xpedxChainedOrderListMap.put(chainedFromOHK, orderList);
				}
				alreadyAddedOrders.add(orderHeaderKey);
			}
			
		}
	}

	public String getParentOrderHeaderKeyForFO() {
		return parentOrderHeaderKeyForFO;
	}

	public void setParentOrderHeaderKeyForFO(String parentOrderHeaderKeyForFO) {
		this.parentOrderHeaderKeyForFO = parentOrderHeaderKeyForFO;
	}
}

/*
 *ajindal
 *$Id: XPEDXOrderPlaceAction.java,v 1.29 2011/11/17 08:23:07 rghare Exp $
 *
 */
package com.sterlingcommerce.xpedx.webchannel.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.order.OrderConstants;
import com.sterlingcommerce.webchannel.order.OrderSaveBaseAction;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContextHelper;
import com.sterlingcommerce.webchannel.utilities.BusinessRuleUtil;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfs.japi.YFSEnvironment;
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
	private String orderType;
	XPEDXShipToCustomer shipToCustomer;
	private String customerContactID;
	public String draftOrderflagOrderSubmit;
	public String draftErrorFlagOrderSummary = "draftErrorFlagOrderSummary";
	public String draftErrorOrderSummary = "false";

	public String getDraftOrderflagOrderSubmit() {
		return draftOrderflagOrderSubmit;
	}

	public void setDraftOrderflagOrderSubmit(String draftOrderflagOrderSubmit) {
		this.draftOrderflagOrderSubmit = draftOrderflagOrderSubmit;
	}

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
		String retFail=FAILURE;
		try {			
			YFCDate orderDate = new YFCDate();
			orderPlaceDate = orderDate.getString();
			Element isPendingApproval = null;
			customerContactID=wcContext.getLoggedInUserId();
			String approveOrderFlag="false";
			//Commented this line as it is causing exception. Looks like the mashup is not defined.
			if (isDraftOrder()) {
//				prepareAndInvokeMashup(CHANGE_ORDER_DATE_MASHUP_ID);
				Document shipToCustomerDoc=createCustomerDocument();
				String editedOrderHeaderKey=XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
				if(YFCCommon.isVoid(editedOrderHeaderKey)){
		 			draftOrderflagOrderSubmit="Y";
		 			retFail="error";
		 		}
		 		else{
		 			draftOrderflagOrderSubmit="N";
		 		}
				isPendingApproval=prepareAndInvokeMashup("XPXIsPendingApprovalOrder");
				if(isPendingApproval != null && isPendingApproval.getAttribute("DraftOrderFlag").equals("N"))
				{
					return draftErrorFlagOrderSummary;
				}
				if(isPendingApproval != null)
				{
					Element holdType = SCXmlUtil.getChildElement(isPendingApproval, "OrderHoldTypes");
					if(holdType != null)
					{
						//applyHoldOnOrder=true;//orderDetailDocument.getDocumentElement().appendChild(orderDetailDocument.importNode(holdType, true));
						setOrderPlaceYFSEnvironmentVariables(getWCContext(),shipToCustomerDoc, holdType);
					
					} else {
						setOrderPlaceYFSEnvironmentVariables(getWCContext(),shipToCustomerDoc, null);
						
					}
				}
				//setOrderPlaceYFSEnvironmentVariables(getWCContext(),shipToCustomerDoc);
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
				setOrderType("Customer");
			} else {//order update flow
				/*Begin - Changes made by Mitesh Parikh for JIRA#3594*/
				String editedOrderHeaderKey=XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
				if(YFCCommon.isVoid(editedOrderHeaderKey)) {
					return draftErrorFlagOrderSummary;

		 		}
				
				Document orderDetailDocument=(Document)getWCContext().getSCUIContext().getSession().getAttribute(CHANGE_ORDEROUTPUT_ORDER_UPDATE_SESSION_OBJ);
				if(isOrderUpdateDone(orderDetailDocument))
					return "OUErrorPage";
				getWCContext().getSCUIContext().getSession().removeAttribute(CHANGE_ORDEROUTPUT_ORDER_UPDATE_SESSION_OBJ);			
				
				setOrderHeaderKey(SCXmlUtil.getAttribute(orderDetailDocument.getDocumentElement(), "OrderHeaderKey"));
				/*End - Changes made by Mitesh Parikh for JIRA#3594*/
				if(YFCCommon.isVoid(orderHeaderKey)){
					generatedErrorMessage = "Could not process the order with no header key.Please contact system admin.";
					log.error("OrderHeaderKey is empty. Cannot process OrderChange.");
					return retFail;
				}
				setOrderType(orderDetailDocument.getDocumentElement().getAttribute("OrderType"));
				
				Object approveOrderSessionVar=XPEDXWCUtils.getObjectFromCache(XPEDXConstants.APPROVE_ORDER_FLAG);
				XPEDXWCUtils.removeObectFromCache(XPEDXConstants.APPROVE_ORDER_FLAG);
				if(approveOrderSessionVar!=null)
				{
					approveOrderFlag=approveOrderSessionVar.toString();
				}
				
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
				
				/*call the customized service XPXUpdateChainedOrder with the order XML which propagates the Fulfillment 
				order changes to Customer Order and sends the new order to Legacy*/
				try 
				{
					Object orderUpdateObj = null;
	
					if("false".equals(approveOrderFlag))
					{
						if("Customer".equals(orderDetailDocument.getDocumentElement().getAttribute("OrderType")))
						{
							isPendingApproval = (Element) prepareAndInvokeMashup("XPXIsPendingApprovalOrder");
							if(isPendingApproval != null)
							{
								Element holdTypes = SCXmlUtil.getChildElement(isPendingApproval, "OrderHoldTypes");
								if(holdTypes != null)
								{
									orderDetailDocument.getDocumentElement().appendChild(orderDetailDocument.importNode(holdTypes, true));
									
								}
							}
						}
						
						if(orderDetailDocument != null)
						{
							// Updated for Jira 4304
							orderDetailDocument.getDocumentElement().removeAttribute("OrderName");
							changeOutputDocToOrderUpdateDoc(orderDetailDocument.getDocumentElement());
							//LOG.debug("Order Input to xpedxOrderUpdateToLegacyFlow : "+SCXmlUtil.getString(orderDetailDocument));
						}
						
						orderUpdateObj = WCMashupHelper.invokeMashup("xpedxOrderUpdateToLegacyFlow", orderDetailDocument.getDocumentElement(), wcContext.getSCUIContext());

						if(orderUpdateObj != null)
						{
							
							Element orderUpdateElem=(Element)orderUpdateObj;
							//Added for JIRA 4326 to get the FO holds because below getOrderLineList will be called for CO .and icase of FO editing there will not be any chained order so need to ge torder details
							if(orderUpdateElem != null && !orderUpdateElem.getAttribute("OrderType").equals("Customer"))
							{
								Map<String, String> valueMap1 = new HashMap<String, String>();
								valueMap1.put("/Order/@OrderHeaderKey", orderUpdateElem.getAttribute("OrderHeaderKey"));
								try {
									Element input1 = WCMashupHelper.getMashupInput("xpedxOrderEditOrderList",
											valueMap1, wcContext.getSCUIContext());

									Object obj1 = WCMashupHelper.invokeMashup(
											"xpedxOrderEditOrderList", input1,
											wcContext.getSCUIContext());

									Document outputDoc1 = ((Element) obj1).getOwnerDocument();
									isFOCSRReview(outputDoc1.getDocumentElement());

								} catch (CannotBuildInputException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
								
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
											return retFail;
										}
										generatedErrorMessage = "Error posting the order update to Legacy system.Please contact system admin.";
										log.error("Exception posting the order update to Legacy system..\n");
										return retFail;
									}
									catch(Exception e)
									{
										generatedErrorMessage = transactionMessage;
										XPEDXWCUtils.logExceptionIntoCent(e.getMessage());
										return retFail;
									}
								}									
							}
						}
						
					} else
					{						
						orderUpdateObj = WCMashupHelper.invokeMashup("xpedxCreateLegacyOrderOnApproval", orderDetailDocument.getDocumentElement(), wcContext.getSCUIContext());
					}					
					
				} catch (Exception e) {
					if("false".equals(approveOrderFlag))
					{
						generatedErrorMessage = "Error posting the order update to Legacy system.Please contact system admin.";
						log.error("Exception posting the order update to Legacy system..\n",e);
						XPEDXWCUtils.logExceptionIntoCent(e.getMessage());
						return retFail;
					
					} else {
						generatedErrorMessage = "Exception while approving an order and posting it to the Legacy system. Please contact system admin.";
						log.error("Exception while approving an order and posting it to the Legacy system..\n",e);
						XPEDXWCUtils.logExceptionIntoCent(e.getMessage());
						return retFail;
					}
				}
				XPEDXWCUtils.resetEditedOrderShipTo(wcContext); //added for jira 4306
				// CR 2997 - Updated for Removing the EditOrderHeaderKey from session after placing an order(Success/Failure) in Edit Order Flow
				if(YFCCommon.isVoid(generatedErrorMessage))
				{
					wcContext.getSCUIContext().getSession().removeAttribute(XPEDXConstants.EDITED_ORDER_HEADER_KEY);
				}
				
				//the output required for the confirmation page; do the necessary getCompleteOrderDetails call.
				confirmDraftOrderElem = prepareAndInvokeMashup(GET_CONFIRMATION_PAGE_DETAILS);
				
				String orderType = SCXmlUtil.getAttribute(confirmDraftOrderElem,"OrderType");
				if(!"Customer".equals(orderType))
				{
					ArrayList<Element> orderLineNodeList=SCXmlUtil.getElements(confirmDraftOrderElem,"OrderLines/OrderLine");
					parentOrderHeaderKeyForFO = orderLineNodeList.get(0).getAttribute("ChainedFromOrderHeaderKey");
				
				} else {
					parentOrderHeaderKeyForFO = getOrderHeaderKey();
				}
			}
			
			ArrayList<String> chainedOrderFromKeylist = new ArrayList<String>();
			chainedOrderFromKeylist.add(orderHeaderKey);
			Document chainedOrderLineListDoc = getXpedxChainedOrderLineList(chainedOrderFromKeylist);
			setXpedxChainedOrderMap(chainedOrderLineListDoc);
			if("false".equals(approveOrderFlag))
			{
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
			}
			
			return SUCCESS;
		}catch(XMLExceptionWrapper e)
		 {
			 YFCElement errorXML=e.getXML();
			 YFCElement errorElement=(YFCElement)errorXML.getElementsByTagName("Error").item(0);
			 String errorDeasc=errorElement.getAttribute("ErrorDescription");
			 XPEDXWCUtils.logExceptionIntoCent(e);
			 if(errorDeasc.contains("Order is not Draft Order"))
			 {
				 
				 return draftErrorFlagOrderSummary;
			 }
			 else{
				 return retFail;
			 }
		 } 
		
		catch (Exception ex) {
			log.error("Unexpected error while placing the order. "+ex.getMessage(), ex);
			generatedErrorMessage = "There was an error processing your last request. Please contact the Customer Support desk at 877 269-1784, eBusiness@ipaper.com";//Message changed - JIRA 3221
			XPEDXWCUtils.logExceptionIntoCent(ex);   //JIRA 4289
			return retFail;
		}
	}
	
	private boolean isOrderUpdateDone( Document outputDoc) 
	{
		try
		{
			Element _orderElement=outputDoc.getDocumentElement();
			String orderHeaderKey=_orderElement.getAttribute("OrderHeaderKey");
			YFCDocument inputDocument = YFCDocument.createDocument("Order");
			YFCElement inputElement = inputDocument.getDocumentElement();
			inputElement.setAttribute("OrderHeaderKey", orderHeaderKey);
			Date changedDate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.ENGLISH).parse(outputDoc.getDocumentElement().getAttribute("Modifyts"));
			YIFApi api = YIFClientFactory.getInstance().getApi();
			ISCUITransactionContext scuiTransactionContext = wcContext.getSCUIContext().getTransactionContext(true);
			YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
			.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
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
		catch(Exception e)
		{
			log.error("Error while getting OrderList and order Document for pendign changes");
		}
		return false;
		}
	private Document createCustomerDocument()
	{
		shipToCustomer = (XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		Document customerListDoc=SCXmlUtil.createDocument("CustomerList");
		if(shipToCustomer != null)
		{
			
			Element customerElement=SCXmlUtil.createChild(customerListDoc.getDocumentElement(), "Customer");
			//<Customer CustomerID="" RootCustomerKey="" ParentCustomerKey="" OrganizationCode="" CustomerKey="">
			customerElement.setAttribute("CustomerID", shipToCustomer.getCustomerID());
			customerElement.setAttribute("RootCustomerKey", shipToCustomer.getRootCustomerKey());
			customerElement.setAttribute("ParentCustomerKey", shipToCustomer.getParentCustomerKey());
			customerElement.setAttribute("OrganizationCode", getWCContext().getStorefrontId());
			customerElement.setAttribute("CustomerKey", shipToCustomer.getCustomerKey());
			Element cusomerExtnElem=SCXmlUtil.createChild(customerElement, "Extn");
			cusomerExtnElem.setAttribute("ExtnAllowDirectOrderFlag", shipToCustomer.getExtnAllowDirectOrderFlag());
			Element buyerOrgElement=SCXmlUtil.createChild(customerListDoc.getDocumentElement(), "BuyerOrganization");
			//<BuyerOrganization OrganizationName=""/>
			buyerOrgElement.setAttribute("OrganizationName", shipToCustomer.getOrganizationName());
			Element parentCustomerElement=SCXmlUtil.createChild(customerListDoc.getDocumentElement(), "ParentCustomer");
			//<ParentCustomer ParentCustomerKey="" BuyerOrganizationCode="" CustomerID="" OrganizationCode="">
			XPEDXShipToCustomer billToCustomer=shipToCustomer.getBillTo();
			Element parentExtnElem=SCXmlUtil.createChild(parentCustomerElement, "Extn");
			if(billToCustomer != null)
			{
				parentCustomerElement.setAttribute("ParentCustomerKey", billToCustomer.getParentCustomerKey());
				parentCustomerElement.setAttribute("BuyerOrganizationCode", billToCustomer.getBuyerOrganizationCode());
				parentCustomerElement.setAttribute("CustomerID", billToCustomer.getCustomerID());
				parentCustomerElement.setAttribute("OrganizationCode", getWCContext().getStorefrontId());
				//<Extn ExtnMinOrderAmount="" ExtnMinChargeAmount="" ExtnMaxOrderAmount=""/>
				parentExtnElem.setAttribute("ExtnMinOrderAmount", billToCustomer.getExtnMinOrderAmount());
				parentExtnElem.setAttribute("ExtnMinChargeAmount", billToCustomer.getExtnMinChargeAmount());
				parentExtnElem.setAttribute("ExtnMinChargeAmount", billToCustomer.getExtnMaxOrderAmount());
			}
			
			
		}
		return customerListDoc;
	}
	public void setOrderPlaceYFSEnvironmentVariables(IWCContext wcContext,Document customerListDoc, Element orderHoldTypeElement ) 
	{
			Document rulesDoc = (Document) wcContext.getWCAttribute("rulesDoc");
			HashMap<String, Object> map = new HashMap<String, Object>();
			if(orderHoldTypeElement!=null){
				map.put("ApplyHoldonOrderDoc",orderHoldTypeElement);
				
			}
			XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean=(XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
			if(xpedxCustomerContactInfoBean != null)
			{
				map.put("OrderedByName",xpedxCustomerContactInfoBean.getFirstName() +""+
						xpedxCustomerContactInfoBean.getLastName());
				map.put("OrderLimitAmount",xpedxCustomerContactInfoBean.getSpendingLimit());
				if(shipToCustomer != null)
				{
					XPEDXShipToCustomer billToCustomer=shipToCustomer.getBillTo();
					if(billToCustomer != null)
					{
						map.put("BillToName",billToCustomer.getOrganizationName());
					}
				}
			}
			map.put("CustomerRulesDoc",rulesDoc);
			map.put("ShipToCustomerProfile",customerListDoc);
			map.put("OPOrderDetailsMap", XPEDXWCUtils.getObjectFromCache("ORDER_PLACE_CHNAGE_ORDER_DOC_MAP"));
			map.put("ItemTypeMap",XPEDXWCUtils.getObjectFromCache("INVENTORY_INDICATOR_MAP"));
			ArrayList<String> minAmount=getMinAndChargeAmount();
			if(minAmount != null)
			{
				map.put("ExtnMaxOrderAmount",minAmount.get(0));
				map.put("ExtnMinOrderAmount",minAmount.get(1));
				map.put("ExtnChargeAmount",minAmount.get(2));
				map.put("ExtnApplyMinOrderBrands", minAmount.get(3));
			}
			XPEDXWCUtils.removeObectFromCache("ORDER_PLACE_CHNAGE_ORDER_DOC_MAP");
			XPEDXWCUtils.removeObectFromCache("INVENTORY_INDICATOR_MAP");
			SCUIContext uictx = wcContext.getSCUIContext();
			ISCUITransactionContext iSCUITransactionContext = uictx.getTransactionContext(true);
			Object env = iSCUITransactionContext.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
			if (env instanceof ClientVersionSupport) {
				ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
				clientVersionSupport.setClientProperties(map);
			}
	}
	
	private ArrayList<String> getMinAndChargeAmount()
	{
		float maxOrderAmountFloat=0;
		float minOrderAmount=0;
		float chargeAmount=0;
		String shipFromDivision="";
		String applyMinOrderBrands=null;
		
		ArrayList<String> retVal=new ArrayList<String>();
		
		XPEDXShipToCustomer shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		if(shipToCustomer!=null)
		{
			applyMinOrderBrands=shipToCustomer.getShipToOrgExtnApplyMinOrderBrands();
		}
		
		XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean=(XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
		
		try
		{
			//Form the input
			if(wcContext.getCustomerId()!=null) {

				String maxOrderAmountStr=xpedxCustomerContactInfoBean.getExtnmaxOrderAmount();
				if(maxOrderAmountStr != null && (!("".equals(maxOrderAmountStr)))  &&
						Float.parseFloat(maxOrderAmountStr)>0)
				{
					maxOrderAmountFloat=Float.parseFloat(maxOrderAmountStr);	
				
				}
				else {
					XPEDXShipToCustomer billToElement = shipToCustomer.getBillTo();
					if(billToElement != null )
					{
						maxOrderAmountStr=billToElement.getExtnMaxOrderAmount();
						if(maxOrderAmountStr != null && (!("".equals(maxOrderAmountStr))) &&
								Float.parseFloat(maxOrderAmountStr)>0)
						{
							maxOrderAmountFloat=Float.parseFloat(maxOrderAmountStr);	
				}
					}
				}
				//JIRA 3488 end
						if(shipToCustomer== null){
							LOG.error("shipToCustomer object from session is null... Creating the Object and Putting it in the session");
							
							XPEDXWCUtils.setCustomerObjectInCache(XPEDXWCUtils
									.getCustomerDetails(getWCContext().getCustomerId(),
											getWCContext().getStorefrontId())
									.getDocumentElement());
							shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
						}
						
						String minOrderAmountStr=shipToCustomer.getExtnMinOrderAmount();
						String chargeAmountStr=shipToCustomer.getExtnMinChargeAmount();;
						
						if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr)))  &&
								(!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) ))
						{
							minOrderAmount = Float.parseFloat(minOrderAmountStr);	
							if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
							{
								chargeAmount = Float.parseFloat(chargeAmountStr);
							}
						}
						else
						{
							if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
							{
								chargeAmount = Float.parseFloat(chargeAmountStr);
							}
							XPEDXShipToCustomer parentElement = shipToCustomer.getBillTo();
							if(parentElement != null )
							{
								minOrderAmountStr=parentElement.getExtnMinOrderAmount();
								chargeAmountStr=parentElement.getExtnMinChargeAmount();
								if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr))) &&
										(!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) ))
								{
									minOrderAmount = Float.parseFloat(minOrderAmountStr);	
									if(chargeAmount <=0 && chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
									{
											chargeAmount = Float.parseFloat(chargeAmountStr);
									}
								}
								else
								{
									if(chargeAmountStr !=null && (!"".equals(chargeAmountStr)) && chargeAmount <=0)
									{
										chargeAmount = Float.parseFloat(chargeAmountStr);
									}
									//String shipFromDivision =(String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH,WCAttributeScope.LOCAL_SESSION);
									//String envCode =(String)wcContext.getWCAttribute(XPEDXConstants.ENVIRONMENT_CODE,WCAttributeScope.LOCAL_SESSION);
									if(shipToCustomer.getShipToOrgExtnMinOrderAmt() == null && shipToCustomer.getShipToOrgExtnSmallOrderFee() == null &&
											shipToCustomer.getShipToOrgOrganizationName() == null && shipToCustomer.getShipToOrgCorporatePersonInfoState() == null)
									{
										shipFromDivision = shipToCustomer.getExtnShipFromBranch();
										String envCode =shipToCustomer.getExtnEnvironmentCode();
										Map<String, String> valueMap = new HashMap<String, String>();
										if(envCode!=null && envCode.trim().length()>0){
											valueMap.put("/Organization/@OrganizationCode", shipFromDivision+"_"+envCode);
										}else{
											LOG.error("EnvCode is NULL. Returning back to the caller.");
											return retVal;
										}
										try {
											Element input = WCMashupHelper.getMashupInput("XPEDXGetShipOrgNodeDetails", valueMap, getWCContext().getSCUIContext());
											Object obj = WCMashupHelper.invokeMashup("XPEDXGetShipOrgNodeDetails", input, getWCContext().getSCUIContext());
											Document outputDoc = ((Element) obj).getOwnerDocument();
											
											if(YFCCommon.isVoid(outputDoc)){
												LOG.error("No DB record exist for Node "+ shipFromDivision+"_"+envCode+". ");
												return retVal;
											}
											shipToCustomer.setShipToOrgExtnMinOrderAmt(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnMinOrderAmt"));
											shipToCustomer.setShipToOrgExtnSmallOrderFee(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnSmallOrderFee"));
											shipToCustomer.setShipToOrgOrganizationName(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/@OrganizationName"));
											shipToCustomer.setShipToOrgCorporatePersonInfoState(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/CorporatePersonInfo/@State"));
											shipToCustomer.setShipToDivDeliveryCutOffTime(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnDeliveryCutOffTime"));
											//Added For Jira 3465
											shipToCustomer.setShipToDivdeliveryInfo(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnDeliveryInfo"));
											shipToCustomer.setShipToDivdeliveryInfoSaal(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnDeliveryInfoSaal"));//EB-3624
											shipToCustomer.setShipToOrgExtnApplyMinOrderBrands(SCXmlUtil.getXpathAttribute(outputDoc.getDocumentElement(), "/OrganizationList/Organization/Extn/@ExtnApplyMinOrderBrands"));
											XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER, shipToCustomer);
										} catch (CannotBuildInputException e) {
											LOG.error("Unable to get XPEDXGetShipOrgNodeDetails for "+ shipFromDivision+"_"+envCode+". ",e);
											return retVal;
										}
									}
									minOrderAmountStr = shipToCustomer.getShipToOrgExtnMinOrderAmt();
									chargeAmountStr= shipToCustomer.getShipToOrgExtnSmallOrderFee();
									if(minOrderAmountStr != null && (!("".equals(minOrderAmountStr))) &&
											(!"0".equals(minOrderAmountStr) ) && (!"0.00".equals(minOrderAmountStr) ))
									{
										minOrderAmount = Float.parseFloat(minOrderAmountStr);				
										if( chargeAmount <=0 && chargeAmountStr !=null && (!"".equals(chargeAmountStr)))
										{
												chargeAmount = Float.parseFloat(chargeAmountStr);
										}
									}
									applyMinOrderBrands=(applyMinOrderBrands!=null && applyMinOrderBrands.trim().length()>0 ? applyMinOrderBrands : shipToCustomer.getShipToOrgExtnApplyMinOrderBrands());
									
							}
						}
				}
			}// if customerId is not null

		}
		catch(Exception e)
		{
			log.error("Error While getting Min Order amount");
		}
		retVal.add(""+maxOrderAmountFloat);
		retVal.add(""+minOrderAmount);
		retVal.add(""+chargeAmount);
		retVal.add(applyMinOrderBrands!=null?applyMinOrderBrands:"");
		
		return retVal;
		
	}
	/*public static void setYFSEnvironmentVariables(IWCContext wcContext) 
	{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("isEditOrderPendingOrderApproval", "true");
			SCUIContext uictx = wcContext.getSCUIContext();
			ISCUITransactionContext iSCUITransactionContext = uictx.getTransactionContext(true);
			Object env = iSCUITransactionContext.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
			if (env instanceof ClientVersionSupport) {
				ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
				clientVersionSupport.setClientProperties(map);
			}
			
	}
	*/
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
			if(("0".equals(orderLine.getAttribute("OrderedQty")) || "0.0".equals(orderLine.getAttribute("OrderedQty")) || "0.00".equals(orderLine.getAttribute("OrderedQty")))
					&& "Y".equals(editOrderflag))
			{
				Element orderLines=(Element)outputDoc.getElementsByTagName("OrderLines").item(0);
				orderLines.removeChild(orderLine);
			}
			else if("Y".equals(editOrderflag))
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
		ArrayList<Element> orderList =(ArrayList<Element> )xpedxChainedOrderListMap.get(orderHeaderKey);
		if (holdTypeForApproval != null && (orderList ==null || orderList.size()==0)) {
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
		ArrayList<Element> orderList =(ArrayList<Element> )xpedxChainedOrderListMap.get(orderHeaderKey);
		if ((orderList !=null && orderList.size()>0)) {
			return false;
		}
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
			//Kubra Jira 4326
			isFOCSRReview(order);
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

	private void isFOCSRReview(Element order)
	{
		NodeList holdtypes =order.getElementsByTagName("OrderHoldType");//SCXmlUtil.getElements(order, "OrderHoldTypes/OrderHoldType");

		
		if(holdtypes != null )
		{
			for(int j=0;j<holdtypes.getLength();j++ )
			{
				Element orderHoldType=(Element)holdtypes.item(j);
				String holdTypeName=orderHoldType.getAttribute("HoldType");
				if(XPEDXConstants.HOLD_TYPE_FOR_LEGACY_LINE_HOLD.equals(holdTypeName)
						|| XPEDXConstants.HOLD_TYPE_FOR_LEGACY_CNCL_ORD_HOLD.equals(holdTypeName) 
						|| XPEDXConstants.HOLD_TYPE_FOR_FATAL_ERR_HOLD.equals(holdTypeName)
						|| XPEDXConstants.HOLD_TYPE_FOR_NEEDS_ATTENTION.equals(holdTypeName)
						|| XPEDXConstants.HOLD_TYPE_FOR_ORDER_EXCEPTION_HOLD.equals(holdTypeName))
				{
					isCSRReview = true;
					break;
				}
			}
		}
	}
	public String getParentOrderHeaderKeyForFO() {
		return parentOrderHeaderKeyForFO;
	}

	public void setParentOrderHeaderKeyForFO(String parentOrderHeaderKeyForFO) {
		this.parentOrderHeaderKeyForFO = parentOrderHeaderKeyForFO;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCustomerContactID() {
		return customerContactID;
	}

	public void setCustomerContactID(String customerContactID) {
		this.customerContactID = customerContactID;
	}
	
	
}

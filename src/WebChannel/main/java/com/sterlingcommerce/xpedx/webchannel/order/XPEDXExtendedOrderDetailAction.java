package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer;
import com.sterlingcommerce.webchannel.order.AuthorizedClientMismatchException;
import com.sterlingcommerce.webchannel.order.DraftOrderFlagMismatchException;
import com.sterlingcommerce.webchannel.order.OrderConstants;
import com.sterlingcommerce.webchannel.order.OrderLineProcessingBaseAction;
import com.sterlingcommerce.webchannel.profile.user.UserProfileHelper;
import com.sterlingcommerce.webchannel.utilities.BusinessRuleUtil;
import com.sterlingcommerce.webchannel.utilities.OrderHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCUtils;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXPaymentMethodHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXExtendedOrderDetailAction extends
		OrderLineProcessingBaseAction {

	/** Logger. */
	private static final Logger log = Logger.getLogger(XPEDXExtendedOrderDetailAction.class);
	private static final String RESULT_SHOW_ORDER_ERROR_PAGE = "orderDetailError";
	private static final String RESOURCE_ID_FOR_ORDER_AGAIN = "/swc/order/authenticatedOrdering";
	private static final String RESOURCE_ID_FOR_ORDER_CANCEL = "/swc/order/authenticatedOrdering";
	private static final String ORDER_DETAIL_MASHUP_ID = "orderDetailMashup";
	private static final String MASHUP_ID_IS_USER_EXIT_IMPL_AVAILABLE_FOR_GETTRACKINGNUMERBERURL = "isUserExitImplAvailableForgetTrackingNumberURL";
	private static final String MASHUP_ID_GET_TRACKING_URL = "getTrackingNumberURL";
	private static final String DUPLICATE_ORDER_HOLD_TYPE = "YCD_DUPLICATE_ORDER";

	protected XPEDXPaymentMethodHelper xpedxPaymentMethodHelper = null;

	private String orderHeaderKey = null;
	private String parentOHK = null;
	private String enterpriseCode = null;
	private Element elementOrder = null;
	private String userKey = "";
	private boolean enableChangeOrder = false;
	private String approvalHoldStatus = "";
	private String resolverUserID = "";
	private String orderListReturnUrl = "";

	private List<String> trackingNoList = new ArrayList<String>();
	private List<String> requestNoList = new ArrayList<String>();
	private List<String> carrierServiceList = new ArrayList<String>();
	private List<String> scacList = new ArrayList<String>();
	private List<String> scacAndServiceList = new ArrayList<String>();
	
	protected String orderBillToID = "";
	protected String orderShipToID = "";
	
	protected HashMap<String, String> inventoryMap = new HashMap<String, String>();

	/*
	 * protected HashMap<String,String> majorLineMap = new
	 * HashMap<String,String>();
	 * 
	 * protected ArrayList<Element> majorLineElements = new
	 * ArrayList<Element>();
	 * 
	 * protected HashMap<String,List<Element>> minorLineElementsMap = new
	 * HashMap<String,List<Element>>();
	 */

	/**
	 * @return the inventoryMap
	 */
	public HashMap<String, String> getInventoryMap() {
		return inventoryMap;
	}

	/**
	 * @param inventoryMap the inventoryMap to set
	 */
	public void setInventoryMap(HashMap<String, String> inventoryMap) {
		this.inventoryMap = inventoryMap;
	}

	/**
	 * Presence of an entry in this map indicates the line has sub-lines (either
	 * bundle or kit).
	 */
	/*
	 * protected HashMap<String, String> hasSublineMap = new HashMap<String,
	 * String>();
	 */

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public String getOrderHeaderKey() {
		return orderHeaderKey;
	}

	public void setOrderHeaderKey(String orderHeaderKey) {
		this.orderHeaderKey = orderHeaderKey;
	}

	public String execute() {
		String result = SUCCESS;
		XPEDXWCUtils xPEDXWCUtils = new XPEDXWCUtils();
		XPEDXOrderUtils orderUtils = new XPEDXOrderUtils();
		try {
			setDraft(DRAFT_N);
			getCompleteOrderDetailsDoc();
			Document outputDoc = getOutputDocument();
			XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			//String shipFromBranch = (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH,WCAttributeScope.LOCAL_SESSION);
			String shipFromBranch=shipToCustomer.getExtnShipFromBranch();
			setInventoryMap(xPEDXWCUtils.getInventoryCheckMap(outputDoc, shipFromBranch, getWCContext()));
			if (outputDoc != null) {
				Element outputDocElement = outputDoc.getDocumentElement();
				
				String loginId = outputDocElement.getAttribute("Createuserid");
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("/User/@Loginid", loginId);
				
				if(!"Customer".equals(outputDocElement.getAttribute("OrderType"))){
					parentOHK = getParentOrderHeaderKey((Element)outputDocElement.getElementsByTagName("OrderLines").item(0));
				}
				
				Element inputEle;
				try {
					inputEle = WCMashupHelper.getMashupInput("XPEDXUserListMashup",
							valueMap, wcContext.getSCUIContext());

					Element userEle = (Element)WCMashupHelper.invokeMashup("XPEDXUserListMashup",
							inputEle, wcContext.getSCUIContext());
					userKey = SCXmlUtil.getXpathAttribute(userEle, "//UserList/User/@UserKey");
				} catch (CannotBuildInputException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String actualDraftOrderFlag = outputDocElement
						.getAttribute("DraftOrderFlag");
				if (!DRAFT_N.equals(actualDraftOrderFlag)) {
					throw new DraftOrderFlagMismatchException(
							getText("OrderDraftOrderFlagMismatch"));
				}
				String supportLevel = outputDocElement
						.getAttribute("SupportLevel");
				setElementOrder(outputDocElement);
				if (supportLevel.equals(OrderConstants.SUPPORT_LEVEL_MINIMUM)) {
					result = RESULT_SHOW_ORDER_ERROR_PAGE;
				} else {
					if (SCXmlUtils.getInstance().getAttribute(elementOrder,
							"MaxOrderStatus").equals(
							OrderConstants.ORDER_CANCEL_STATUS)) {
						setIncludeCancelledLines(true);
					}
					processOrderLines();
					xpedxPaymentMethodHelper = new XPEDXPaymentMethodHelper(
							getWCContext(), outputDocElement);
					String hasPendingChanges = outputDocElement
							.getAttribute("HasPendingChanges");
					if ("Y".equals(hasPendingChanges)) {
						xpedxPaymentMethodHelper.processRemainingAmountToAuth();
					}
					setEnableChangeOrder();
				}
			}
			// TASK 30 OP
			HashMap<String, String> billToShipToMap = orderUtils.getBillToShipToFromOrder(outputDoc,getOrderHeaderKey(), wcContext);
			setOrderBillToID(billToShipToMap.get("OrderBillToID"));
			setOrderShipToID(billToShipToMap.get("OrderShipToID"));
			
			log.debug("Output xml : \n"
					+ SCXmlUtils.getInstance().getString(getElementOrder()));
		} catch (DraftOrderFlagMismatchException dofme) {
			handleValidityCheckExceptions(dofme, log);
			result = DRAFT_ORDER_FLAG_MISMATCH_ERROR;
		} catch (AuthorizedClientMismatchException acme) {
			handleValidityCheckExceptions(acme, log);
			result = AUTHORIZED_CLIENT_MISMATCH_ERROR;
		} catch (Exception e) {
			log.error("Error while getting order detail", e);
			WCUtils.setErrorInContext(getWCContext(), e);

			result = ERROR;
		}
		
		return result;

	}

	private void setEnableChangeOrder() {
		enableChangeOrder = false;
		if (elementOrder != null) {
			Element eleOrderModificationRules = SCXmlUtils.getInstance()
					.getChildElement(elementOrder, "Modifications");
			if (eleOrderModificationRules != null) {
				Iterator<Element> eleOrderModification = SCXmlUtils
						.getInstance().getChildren(eleOrderModificationRules);
				while (eleOrderModification.hasNext()) {
					Element rule = eleOrderModification.next();
					boolean mValue = SCXmlUtils.getInstance()
							.getBooleanAttribute(rule, "ModificationAllowed");
					if (mValue == true) {
						enableChangeOrder = mValue;
						return;
					}
				}
			}

			Element eleOrderLines = SCXmlUtils.getInstance().getInstance()
					.getChildElement(elementOrder, "OrderLines");
			Iterator<Element> iterOrderLines = SCXmlUtils.getInstance()
					.getInstance().getChildren(eleOrderLines);
			while (iterOrderLines.hasNext()) {
				Element eleOrderLine = iterOrderLines.next();
				Element eleLineModificationRules = SCXmlUtils.getInstance()
						.getInstance().getChildElement(eleOrderLine,
								"Modifications");
				if (eleLineModificationRules != null) {
					Iterator<Element> eleLineModification = SCXmlUtils
							.getInstance().getInstance().getChildren(
									eleLineModificationRules);
					while (eleLineModification.hasNext()) {
						Element lineRule = eleLineModification.next();
						boolean mLineValue = SCXmlUtils.getInstance()
								.getInstance().getBooleanAttribute(lineRule,
										"ModificationAllowed");
						if (mLineValue == true) {
							enableChangeOrder = mLineValue;
							return;
						}
					}
				}
			}

		}
	}

	public boolean isShowZipCode() {
		boolean show = false;
		if (elementOrder != null) {
			Element eleOrderLines = SCXmlUtils.getInstance().getInstance()
					.getChildElement(elementOrder, "OrderLines");
			Iterator<Element> iterOrderLines = SCXmlUtils.getInstance()
					.getInstance().getChildren(eleOrderLines);
			while (!show && iterOrderLines.hasNext()) {
				Element eleOrderLine = iterOrderLines.next();
				Element eleLineModificationRules = SCXmlUtils.getInstance()
						.getInstance().getChildElement(eleOrderLine,
								"Modifications");
				if (eleLineModificationRules != null) {
					Iterator<Element> eleLineModification = SCXmlUtils
							.getInstance()
							.getChildren(eleLineModificationRules);
					while (eleLineModification.hasNext()) {
						Element rule = eleLineModification.next();
						if (SCXmlUtils.getInstance().getAttribute(rule,
								"ModificationType").equals("SHIP_NODE")) {
							boolean mValue = SCXmlUtils.getInstance()
									.getBooleanAttribute(rule,
											"ModificationAllowed");
							if (mValue) {
								show = mValue;
								break;
							}
						}
					}
				}
			}

		}
		return show;
	}

	public boolean getModificationRuleValue(Element eleModificationRule,
			String modificationType) {
		boolean mValue = false;
		if (eleModificationRule != null) {
			Iterator<Element> eleOrderModification = SCXmlUtils.getInstance()
					.getChildren(eleModificationRule);
			while (eleOrderModification.hasNext()) {
				Element rule = eleOrderModification.next();
				if (SCXmlUtils.getInstance().getAttribute(rule,
						"ModificationType").equals(modificationType)) {
					mValue = SCXmlUtils.getInstance().getBooleanAttribute(rule,
							"ModificationAllowed");
					break;
				}
			}
		}
		return mValue;
	}

	public Element getOrderModificationRule() {
		return SCXmlUtils.getInstance().getChildElement(elementOrder,
				"Modifications");
	}

	public Element getElementOrder() {
		return elementOrder;
	}

	public void setElementOrder(Element elementOrder) {
		this.elementOrder = elementOrder;
	}

	/**
	 * check for the duplicate order hold was opened or rejected on the order.
	 * 
	 * @return true if YCD_DUPLICATE_ORDER was in the open or reject status.
	 */

	public boolean isOrderOnDuplicateOrderHold() {
		boolean result = false;
		Element orderholdtypeselem = SCXmlUtils.getInstance().getChildElement(
				this.elementOrder, OrderConstants.ORDER_HOLD_TYPES);
		if (orderholdtypeselem != null) {
			List<Element> holdTypeList = SCXmlUtils
					.getInstance()
					.getElementsByAttribute(orderholdtypeselem,
							OrderConstants.ORDER_HOLD_TYPE,
							OrderConstants.HOLD_TYPE, DUPLICATE_ORDER_HOLD_TYPE);
			for (Element element : holdTypeList) {
				String status = element.getAttribute(OrderConstants.STATUS)
						.trim();
				if (status.equals(OrderConstants.OPEN_HOLD_STATUS)
						|| status.equals(OrderConstants.REJECT_HOLD_STATUS)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	// Check if the current order has approval hold or not (any status) to show
	// the approval-history link
	public boolean isOrderOnApprovalHold() {
		/*String holdTypeForApproval = (BusinessRuleUtil.getBusinessRule(
				OrderConstants.HOLD_TO_BE_APPLIED_FOR_ORDER_APPROVAL_RULE,
				wcContext)).trim();*/
		String holdTypeForApproval = XPEDXConstants.HOLD_TYPE_FOR_PENDING_APPROVAL;
		
		if (holdTypeForApproval != null) {
			Element orderholdtypeselem = SCXmlUtils.getInstance()
					.getChildElement(this.elementOrder,
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
					return true;
				}
			}
		}
		return false;
	}

	// Check if the current order has CSR Review hold or not
	public boolean isOrderOnCSRReviewHold() {
		String holdTypeNeedsAttention = XPEDXConstants.HOLD_TYPE_FOR_NEEDS_ATTENTION;
		String holdTypeLegacyCnclOrd = XPEDXConstants.HOLD_TYPE_FOR_LEGACY_CNCL_ORD_HOLD;
		String holdTypeOrderException = XPEDXConstants.HOLD_TYPE_FOR_ORDER_EXCEPTION_HOLD;
		String openHoldStatus = OrderConstants.OPEN_HOLD_STATUS;
		
		Element orderholdtypeselem = SCXmlUtil.getChildElement(this.elementOrder, OrderConstants.ORDER_HOLD_TYPES);
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

	// Return OrderHoldTypeLogs element for the order approval history details
	public Element getHoldTypeLogElem() {
		String holdTypeForApproval = (BusinessRuleUtil.getBusinessRule(
				OrderConstants.HOLD_TO_BE_APPLIED_FOR_ORDER_APPROVAL_RULE,
				wcContext)).trim();
		Element holdtypelogelem = null;
		Element orderholdtypeselem = SCXmlUtils.getInstance().getChildElement(
				this.elementOrder, OrderConstants.ORDER_HOLD_TYPES);
		ArrayList<Element> orderholdtypeelemlist = SCXmlUtils
				.getInstance()
				.getChildren(orderholdtypeselem, OrderConstants.ORDER_HOLD_TYPE);
		for (Iterator<Element> iter = orderholdtypeelemlist.iterator(); iter
				.hasNext();) {
			Element orderholdtypeelem = (Element) iter.next();
			if (orderholdtypeelem.getAttribute(OrderConstants.HOLD_TYPE).trim()
					.equals(holdTypeForApproval)) {
				holdtypelogelem = SCXmlUtils.getInstance().getChildElement(
						orderholdtypeelem, OrderConstants.ORDER_HOLD_TYPE_LOGS);
				holdtypelogelem = OrderHelper.sortChildren(holdtypelogelem,
						"Createts", true, false);
				Element lastApprovalActionElem = SCXmlUtils.getInstance()
						.createChild(holdtypelogelem, "OrderHoldTypeLog");
				String displayUserIdForlastApprovalAction = UserProfileHelper
						.getDisplayUserIDForContact(orderholdtypeelem
								.getAttribute("Modifyuserid"), wcContext,
								this.elementOrder.getAttribute("BillToID"));
				lastApprovalActionElem.setAttribute("UserId",
						displayUserIdForlastApprovalAction);

				lastApprovalActionElem.setAttribute("StatusDescription",
						orderholdtypeelem.getAttribute("StatusDescription"));
				lastApprovalActionElem.setAttribute("Status", orderholdtypeelem
						.getAttribute("Status"));
				if (orderholdtypeelem.getAttribute("Status").equals(
						OrderConstants.RESOLVE_HOLD_STATUS)) {
					lastApprovalActionElem.setAttribute("ResolverUserId",
							orderholdtypeelem.getAttribute("ResolverUserId"));
				} else {
					lastApprovalActionElem.setAttribute("ResolverUserId",
							displayUserIdForlastApprovalAction);
				}
				lastApprovalActionElem.setAttribute("ReasonText",
						orderholdtypeelem
								.getAttribute(OrderConstants.REASON_TEXT));
				lastApprovalActionElem.setAttribute("Createts",
						orderholdtypeelem.getAttribute("LastHoldTypeDate"));
				cleanUpHoldTypeLogForApproval(holdtypelogelem);
				if (this.approvalHoldStatus
						.equals(OrderConstants.OPEN_HOLD_STATUS)) {
					Element awaitingApprovalElem = SCXmlUtils.getInstance()
							.createChild(holdtypelogelem, "OrderHoldTypeLog");
					String displayUserIdForPendingApproval = UserProfileHelper
							.getDisplayUserIDForContact(
									orderholdtypeelem
											.getAttribute(OrderConstants.RESOLVER_USER_ID),
									wcContext, this.elementOrder
											.getAttribute("BillToID"));
					awaitingApprovalElem.setAttribute("UserId",
							displayUserIdForPendingApproval);
					awaitingApprovalElem.setAttribute("StatusDescription",
							getText("approval.Pending"));
				}
				return holdtypelogelem;
			}
		}
		return holdtypelogelem;

	}

	private void cleanUpHoldTypeLogForApproval(Element holdtypelogelem) {
		log.debug(SCXmlUtils.getInstance().getString(holdtypelogelem));
		ArrayList<Element> orderholdtypeelemlist = SCXmlUtils.getInstance()
				.getChildren(holdtypelogelem, "OrderHoldTypeLog");
		for (Iterator<Element> iter = orderholdtypeelemlist.iterator(); iter
				.hasNext();) {
			Element orderholdtypelogelem = (Element) iter.next();
			String status = orderholdtypelogelem.getAttribute("Status");
			if (YFCCommon.isVoid(status)
					|| status.equals(OrderConstants.OPEN_HOLD_STATUS)) {
				holdtypelogelem.removeChild(orderholdtypelogelem);
			} else if (status.equals(OrderConstants.REJECT_HOLD_STATUS)
					|| status.equals(OrderConstants.RESOLVE_HOLD_STATUS)) {
				orderholdtypelogelem.setAttribute("UserId",
						orderholdtypelogelem.getAttribute("ResolverUserId"));
			}
		}
		log.debug(SCXmlUtils.getInstance().getString(holdtypelogelem));

	}

	// Call getCustomerContactList to check if the logged-in user is Approver or
	// Proxy-Approver for the order owner
	public boolean approvalAllowed() throws CannotBuildInputException {
		if (isSupportedFunction(true)) {
			if (this.approvalHoldStatus.equals(OrderConstants.OPEN_HOLD_STATUS)) {
				if (this.resolverUserID
						.equals(wcContext.getCustomerContactId())) {
					return true;
				} else if (UserProfileHelper.isContactProxyForResolver(
						wcContext.getCustomerContactId(), this.resolverUserID,
						wcContext)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isOrderInPendingChageState() {
		boolean result = false;
		boolean containPendingChanges = SCXmlUtils.getInstance()
				.getBooleanAttribute(elementOrder,
						OrderConstants.CONTAIN_PENDING_CHANGES);
		if (containPendingChanges && isSupportedFunction(false)) {
			String otherUserId = SCXmlUtils.getInstance().getAttribute(
					elementOrder, OrderConstants.PENDING_CHANGES_USERID);
			if (null != otherUserId
					&& otherUserId.equals(getWCContext().getLoggedInUserId())) {
				result = true;
			}
		}
		return result;

	}

	/**
	 * This function is to used for check whether logged in user is owner of
	 * pending order change.
	 * 
	 * @return boolean true if pending user is not logged in user.
	 */
	public boolean isEditableOrder() {
		boolean result = true;
		if (xpedxPaymentMethodHelper.isPresentOtherPaymentGroupMethod()) {
			return false;
		}
		if (!isSupportedFunction(false)) {
			return false;
		}

		if (SCXmlUtils.getInstance().getAttribute(elementOrder,
				"MaxOrderStatus").equals(OrderConstants.ORDER_CANCEL_STATUS)) {
			return false;
		}

		if (isOrderOnDuplicateOrderHold()) {
			return false;
		}
		//Condition to check if order is locked then user will not be able to see edit order and cancel order button
		ArrayList<Element> orderExtnNode=SCXmlUtil.getElements(elementOrder,"Extn");//orderElement.getElementsByTagName("Extn").item(0);
		Element orderExtn=null;
		if(orderExtnNode != null && orderExtnNode.size()>0)
		{
			orderExtn=orderExtnNode.get(0);
			String extnOrderLockFlag=orderExtn.getAttribute("ExtnOrderLockFlag");
			String OrderType=elementOrder.getAttribute("OrderType");
			if("Y".equals(extnOrderLockFlag) && !"Customer".equals(OrderType))
			{
				return false;
			}
		}
		
		boolean containPendingChanges = SCXmlUtils.getInstance()
				.getBooleanAttribute(elementOrder,
						OrderConstants.CONTAIN_PENDING_CHANGES);
		if (containPendingChanges) {
			String otherUserId = SCXmlUtils.getInstance().getAttribute(
					elementOrder, OrderConstants.PENDING_CHANGES_USERID);
			if (null != otherUserId
					&& otherUserId.equals(getWCContext().getLoggedInUserId())) {
				result = true;
			} else {
				result = false;
			}
		}
		//for jira 2248 - for cancel button in system hold status
		String  maxOrderStatus=SCXmlUtils.getInstance().getAttribute(elementOrder,
        "MaxOrderStatus");
        if (result && (maxOrderStatus.equals("1100") || // Created
                        maxOrderStatus.equals("1100.0100") || // Placed
                        maxOrderStatus.equals("1100.5150") || // Hold Pending Approval
                        maxOrderStatus.equals("1100.5250") || // Legacy Open
                        maxOrderStatus.equals("1100.5350") || // Customer Hold
                        maxOrderStatus.equals("1100.5450") || // Legacy web hold
                        maxOrderStatus.equals("1100.5400")) // System hold
                        
        )
        {
        		return true;
        }
        //end of jira 2248
        //For Jira 2378
        else if (result && (maxOrderStatus.equals("1100.6000") || // Blanket
                maxOrderStatus.equals("1100.5500") || // Released For Fullfillment
                maxOrderStatus.equals("1100.5550") || // Shipped JIRA #2607
                maxOrderStatus.equals("3700.0100") || // Delivered
                maxOrderStatus.equals("1100.5700") || // Invoiced JIRA #2607
                maxOrderStatus.equals("1100.5750") || // Return
                maxOrderStatus.equals("1100.5900") || // Quote
                maxOrderStatus.equals("1100.5800")) // Credit                
        	)
        {
        	return false;
        }
        //end of jira 2378
		if (result) {
			result = enableChangeOrder;
		}
		return result;
	}

	
	/**
	 * This function is to used for check whether logged in user is owner of
	 * pending order change.
	 * METHOD ADDED FOR CANCEL BUTTON DISPLAY, JIRA 3042. Specifically for determining display on a customer order.
	 * @return boolean true if pending user is not logged in user.
	 */
	public boolean isEditableCustomerOrder() {
		boolean result = true;
		boolean resultForCustomer = false;
     System.out.println("INSIDE XPEDXExtendedOrderDetail.isEditableCustomerOrder, IS A CUSTOMER ORDER");
		if (xpedxPaymentMethodHelper.isPresentOtherPaymentGroupMethod()) {
			System.out.println("INSIDE XPEDXExtendedOrderDetail.isEditableCustomerOrder, IS A CUSTOMER ORDER, ALL GOOD! RETURNING FALSE! 1");
			return false;
		}
		if (!isSupportedFunction(false)) {
			System.out.println("INSIDE XPEDXExtendedOrderDetail.isEditableCustomerOrder, IS A CUSTOMER ORDER, ALL GOOD! RETURNING FALSE! 2");
			return false;
		}

		if (SCXmlUtils.getInstance().getAttribute(elementOrder,
				"MaxOrderStatus").equals(OrderConstants.ORDER_CANCEL_STATUS)) {
			System.out.println("INSIDE XPEDXExtendedOrderDetail.isEditableCustomerOrder, IS A CUSTOMER ORDER, ALL GOOD! RETURNING FALSE! 3");
			return false;
		}

		if (isOrderOnDuplicateOrderHold()) {
			System.out.println("INSIDE XPEDXExtendedOrderDetail.isEditableCustomerOrder, IS A CUSTOMER ORDER, ALL GOOD! RETURNING FALSE! 4");
			return false;
		}
		//Condition to check if order is locked then user will not be able to see edit order and cancel order button
		ArrayList<Element> orderExtnNode=SCXmlUtil.getElements(elementOrder,"Extn");//orderElement.getElementsByTagName("Extn").item(0);
		Element orderExtn=null;
		if(orderExtnNode != null && orderExtnNode.size()>0)
		{
			orderExtn=orderExtnNode.get(0);
			String extnOrderLockFlag=orderExtn.getAttribute("ExtnOrderLockFlag");
			String OrderType=elementOrder.getAttribute("OrderType");
			if("Y".equals(extnOrderLockFlag) && !"Customer".equals(OrderType))
			{
				System.out.println("INSIDE XPEDXExtendedOrderDetail.isEditableCustomerOrder, IS A CUSTOMER ORDER, ALL GOOD! RETURNING FALSE! 5");
				return false;
			}
		}
		
		boolean containPendingChanges = SCXmlUtils.getInstance()
				.getBooleanAttribute(elementOrder,
						OrderConstants.CONTAIN_PENDING_CHANGES);
		if (containPendingChanges) {
			String otherUserId = SCXmlUtils.getInstance().getAttribute(
					elementOrder, OrderConstants.PENDING_CHANGES_USERID);
			if (null != otherUserId
					&& otherUserId.equals(getWCContext().getLoggedInUserId())) {
				result = true;
			} else {
				result = false;
			}
		}
		//for jira 2248 - for cancel button in system hold status
		String  maxOrderStatus=SCXmlUtils.getInstance().getAttribute(elementOrder,
        "MaxOrderStatus");
		System.out.println("INSIDE XPEDXExtendedOrderDetail.isEditableCustomerOrder, MAX ORDER STATUS IS: " + maxOrderStatus);
        if (result && (maxOrderStatus.equals("1100.0100"))) // Placed
        {
        	System.out.println("INSIDE XPEDXExtendedOrderDetail.isEditableCustomerOrder, MAX ORDER STATUS IS: " + maxOrderStatus);
        	System.out.println("INSIDE XPEDXExtendedOrderDetail.isEditableCustomerOrder, IS A CUSTOMER ORDER, ALL GOOD! RETURNING TRUE!");
        		return true;
        }
        else                 
        {
        	System.out.println("INSIDE XPEDXExtendedOrderDetail.isEditableCustomerOrder, IS A CUSTOMER ORDER, ALL GOOD! RETURNING FALSE!");
        	return false;
        }
        //end of jira 2378
		
		//System.out.println("INSIDE XPEDXExtendedOrderDetail.isEditableCustomerOrder, IS A CUSTOMER ORDER, ALL GOOD! RETURNING RESULT! " + result);
		//	 return result;
	}	
	
	
	
	
	public boolean isCancel() {
		if (!isUserHaveResourceAccessPermission(RESOURCE_ID_FOR_ORDER_CANCEL)) {
			return false;
		}
		if (!isSupportedFunction(true)) {
			return false;
		}
		boolean containPendingChanges = SCXmlUtils.getInstance()
				.getBooleanAttribute(elementOrder,
						OrderConstants.CONTAIN_PENDING_CHANGES);
		if (containPendingChanges) {
			return false;
		}

		if (SCXmlUtils.getInstance().getAttribute(elementOrder,
				"MaxOrderStatus").equals(OrderConstants.ORDER_CANCEL_STATUS)) {
			return false;
		}

		Element cancelModificationRuleAtOrder = SCXmlUtils.getInstance()
				.getElementByAttribute(elementOrder,
						"Modifications/Modification", "ModificationType",
						"CANCEL");

		if (!SCXmlUtils.getInstance().getBooleanAttribute(
				cancelModificationRuleAtOrder, "ModificationAllowed")) {
			return false;
		}
		return true;
	}

	private boolean isSupportedFunction(boolean opCancel) {
		String supportLevel = SCXmlUtils.getInstance().getAttribute(
				elementOrder, "SupportLevel");
		boolean bSupportLevel = false;
		if (opCancel) {
			bSupportLevel = (supportLevel
					.equals(OrderConstants.SUPPORT_LEVEL_FULL) || supportLevel
					.equals(OrderConstants.SUPPORT_LEVEL_MEDIUM)) ? true
					: false;
		} else {
			bSupportLevel = (supportLevel
					.equals(OrderConstants.SUPPORT_LEVEL_FULL)) ? true : false;
		}
		return bSupportLevel;

	}

	public boolean isUserHaveResourceAccessPermission(String resId) {
		boolean result = false;
		try {
			result = ResourceAccessAuthorizer.getInstance().isAuthorized(resId,
					getWCContext());
		} catch (Exception ex) {
			log.error("Error while checking resource permission.  " + ex);
		}

		return result;
	}

	public boolean canOrderAgain() {
		if (!isUserHaveResourceAccessPermission(RESOURCE_ID_FOR_ORDER_AGAIN)) {
			return false;
		}
		if (!isSupportedFunction(false)) {
			return false;
		}
		if (SCXmlUtils.getInstance().getAttribute(elementOrder,
				"MaxOrderStatus").equals(OrderConstants.ORDER_CANCEL_STATUS)) {
			return false;
		}

		return true;
	}

	public SCXmlUtils getXmlUtils() {
		return SCXmlUtils.getInstance();
	}

	/*
	 * public ArrayList<Element> getMajorLineElements() { return
	 * majorLineElements; }
	 * 
	 * public void setMajorLineElements(ArrayList<Element> majorLineElements) {
	 * this.majorLineElements = majorLineElements; }
	 */
	public boolean isUserExitImplAvailableForgetTrackingNumberURL() {
		boolean result = false;
		try {
			Element element = prepareAndInvokeMashup(MASHUP_ID_IS_USER_EXIT_IMPL_AVAILABLE_FOR_GETTRACKINGNUMERBERURL);
			if (element != null) {
				result = SCXmlUtils.getInstance().getBooleanAttribute(element,
						"IsUserExitImplemented");
			}
		} catch (Exception ex) {
			log.error("Exception in checking User exit implementation." + ex);
		}
		return result;
	}

	public List<String> getCarrierServiceList() {
		return carrierServiceList;
	}

	public void setCarrierServiceList(List<String> carrierServiceList) {
		this.carrierServiceList = carrierServiceList;
	}

	public List<String> getRequestNoList() {
		return requestNoList;
	}

	public void setRequestNoList(List<String> requestNoList) {
		this.requestNoList = requestNoList;
	}

	public List<String> getTrackingNoList() {
		return trackingNoList;
	}

	public void setTrackingNoList(List<String> trackingNoList) {
		this.trackingNoList = trackingNoList;
	}

	public Element getTrackingURL(List<Element> shipment) {
		Element elementTrackingURL = null;
		trackingNoList.clear();
		carrierServiceList.clear();
		requestNoList.clear();
		scacList.clear();
		scacAndServiceList.clear();
		for (Element shipmentElement : shipment) {
			String trackingNumber = SCXmlUtils.getInstance().getAttribute(
					shipmentElement, "TrackingNo");
			String carrierServiceCode = SCXmlUtils.getInstance().getAttribute(
					shipmentElement, "CarrierServiceCode");
			String scac = SCXmlUtils.getInstance().getAttribute(
					shipmentElement, "SCAC");
			Element containerShipmentEle = SCXmlUtils.getInstance()
					.getChildElement(shipmentElement, "Shipment");
			String scacAndService = SCXmlUtils.getInstance().getAttribute(
					containerShipmentEle, "ScacAndService");
			trackingNoList.add(trackingNumber);
			carrierServiceList.add(carrierServiceCode);
			requestNoList.add(trackingNumber);
			scacList.add(scac);
			scacAndServiceList.add(scacAndService);
		}
		try {
			elementTrackingURL = prepareAndInvokeMashup(MASHUP_ID_GET_TRACKING_URL);
		} catch (Exception ex) {
			log.error("Exception in getting Tracking URL." + ex);
		}
		return elementTrackingURL;
	}

	@Override
	protected String getOrderDetailsMashupName() {
		return ORDER_DETAIL_MASHUP_ID;
	}

	public XPEDXPaymentMethodHelper getPaymentMethodHelper() {
		return xpedxPaymentMethodHelper;
	}

	public void setPaymentMethodHelper(
			XPEDXPaymentMethodHelper paymentMethodHelper) {
		this.xpedxPaymentMethodHelper = paymentMethodHelper;
	}

	public Element getHoldTypesElem() {
		Element orderHolds = SCXmlUtils.getInstance().getChildElement(
				this.elementOrder, "OrderHoldTypes");
		return orderHolds;
	}

	public Element getLineHoldTypesElem() throws Exception {
		Element orderlineElemList = SCXmlUtils.getInstance().getChildElement(
				this.elementOrder, "OrderLines");
		Element orderLineHoldTypes = SCXmlUtils.getInstance().createDocument(
				"OrderHoldTypes").getDocumentElement();
		ArrayList<Element> orderlineList = SCXmlUtils.getInstance()
				.getChildren(orderlineElemList, "OrderLine");
		for (Iterator<Element> iter = orderlineList.iterator(); iter.hasNext();) {
			Element oNode = (Element) iter.next();
			String itemShortDesc = getShortDescriptionForOrderLine(oNode);
			Element orderLineHoldTypesElem = SCXmlUtils.getInstance()
					.getChildElement(oNode, "OrderHoldTypes");
			ArrayList<Element> orderLineHoldTypeList = SCXmlUtils.getInstance()
					.getChildren(orderLineHoldTypesElem, "OrderHoldType");
			if (orderLineHoldTypeList != null
					&& orderLineHoldTypeList.size() > 0) {
				for (Iterator<Element> iter1 = orderLineHoldTypeList.iterator(); iter1
						.hasNext();) {
					Element oNode1 = (Element) iter1.next();
					oNode1.setAttribute("ItemDescription", itemShortDesc);
					SCXmlUtils.getInstance().importElement(orderLineHoldTypes,
							oNode1);
				}
			}
		}
		return orderLineHoldTypes;
	}
	
	public String getParentOrderHeaderKey(Element orderLinesElem){
		ArrayList<Element> orderLineElemList = SCXmlUtil.getElements(orderLinesElem, "OrderLine");
		if(orderLineElemList != null){
			for (int i = 0; i < orderLineElemList.size(); i++) {
				Element orderLineElement = (Element)orderLineElemList.get(i);
				if(!"".equals(orderLineElement.getAttribute("ChainedFromOrderHeaderKey"))){
					return orderLineElement.getAttribute("ChainedFromOrderHeaderKey");
				}
			}
		}
		return null;
	}

	public Map<String, String> getHoldTypeDescription() {
		Map<String, String> holdTypeDescMap = new HashMap<String, String>();
		Element holdTypeList = null;
		try {
			holdTypeList = prepareAndInvokeMashup("getHoldTypeList");
		} catch (XMLExceptionWrapper ex) {
			log.error("Exception in getting Hold type List. " + ex);
		} catch (CannotBuildInputException cbe) {
			log.error("Exception in getting Hold type List. " + cbe);
		}
		if (holdTypeList != null) {
			ArrayList<Element> holdTypesElem = SCXmlUtils.getInstance()
					.getChildren(holdTypeList, "HoldType");
			for (Iterator<Element> iter1 = holdTypesElem.iterator(); iter1
					.hasNext();) {
				Element oNode1 = (Element) iter1.next();
				String holdType = oNode1.getAttribute("HoldType");
				String holdTypeDesc = oNode1
						.getAttribute("HoldTypeDescription");
				holdTypeDescMap.put(holdType, holdTypeDesc);
			}
		}
		return holdTypeDescMap;
	}

	public boolean isOrderHeld() {
		String holdflag = elementOrder.getAttribute("HoldFlag");
		if (holdflag.equals("Y")) {
			return true;
		} else {
			return false;
		}
	}

	public String getOrderListReturnUrl() {
		return orderListReturnUrl;
	}

	public void setOrderListReturnUrl(String orderListReturnUrl) {
		this.orderListReturnUrl = orderListReturnUrl;
	}

	public List<String> getScacAndServiceList() {
		return scacAndServiceList;
	}

	public void setScacAndServiceList(List<String> scacAndServiceList) {
		this.scacAndServiceList = scacAndServiceList;
	}

	public List<String> getScacList() {
		return scacList;
	}

	public void setScacList(List<String> scacList) {
		this.scacList = scacList;
	}
	
	/**
	 * @return the orderBillToID
	 */
	public String getOrderBillToID() {
		return orderBillToID;
	}

	/**
	 * @param orderBillToID the orderBillToID to set
	 */
	public void setOrderBillToID(String orderBillToID) {
		this.orderBillToID = orderBillToID;
	}

	/**
	 * @return the orderShipToID
	 */
	public String getOrderShipToID() {
		return orderShipToID;
	}

	/**
	 * @param orderShipToID the orderShipToID to set
	 */
	public void setOrderShipToID(String orderShipToID) {
		this.orderShipToID = orderShipToID;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getParentOHK() {
		return parentOHK;
	}

	public void setParentOHK(String parentOHK) {
		this.parentOHK = parentOHK;
	}
}

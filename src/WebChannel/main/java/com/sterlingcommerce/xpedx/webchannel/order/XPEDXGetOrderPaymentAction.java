/*
 *ajindal
 *$Id: XPEDXGetOrderPaymentAction.java,v 1.2 2011/09/15 14:22:32 amakumar-tw Exp $
 *
 */
package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.CommonCodeDescriptionType;
import com.sterlingcommerce.webchannel.order.AuthorizedClientMismatchException;
import com.sterlingcommerce.webchannel.order.DraftOrderFlagMismatchException;
import com.sterlingcommerce.webchannel.order.OrderConstants;
import com.sterlingcommerce.webchannel.order.OrderGetBaseAction;
import com.sterlingcommerce.webchannel.order.utilities.CreditCardCVVStorageHelper;
import com.sterlingcommerce.webchannel.order.utilities.PaymentTypeHelper;
import com.sterlingcommerce.webchannel.utilities.CommonCodeUtil;
import com.sterlingcommerce.webchannel.utilities.WCUtils;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXPaymentMethodHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class XPEDXGetOrderPaymentAction extends OrderGetBaseAction {

	private static final String YCD_CREDIT_CARD_TYPE = "YCD_CREDIT_CARD_TYPE";

	private static final String SINGLE_STEP_CHECKOUT_BACK_BUTTON_VIEW = "OrderSummary.action";

	private static final String MULTI_STEP_CHECKOUT_BACK_BUTTON_VIEW = "getOrderBillToAddress.action";

	private String orderHeaderKey;

	private static final String ORDER_PAYMENT_MASHUP = "me_OrderPayment";

	private static final String ORDER_BILL_TO_ADDRESS_MASHUP = "me_getOrderBillToAddress";

	@SuppressWarnings("deprecation")
	private SCXmlUtils scXmlUtils = SCXmlUtils.getInstance();

	private static final String RESULT_SINGLE_NON_GIFT_PAYMENT = "successSingleNonGiftPayment";

	private static final String RESULT_MULTIPLE_NON_GIFT_PAYMENT = "successMultipleNonGiftPayment";

	private final static String GET_PAYMENT_BOOK_MASHUP = "GetPaymentBook";

	/** Logger. */
	private final Logger log = Logger
			.getLogger(XPEDXGetOrderPaymentAction.class);

	private List<Element> customerCreditCardPayments = new ArrayList<Element>();

	private List<Element> customerAccountPayments = new ArrayList<Element>();

	private String paymentArrayName = "";

	private Element elementPaymentBook = null;

	private boolean onlyGiftPayment = false;

	private String svcNo = null;

	private String paymentType = "";

	private boolean multiNonGiftPaymentMethod = false;

	private boolean singleNonGiftPaymentMethod = false;

	private Element orderBillToAddress;

	private Double orderTotal = 0.0;

	private boolean useMorePayment = false;

	private Double remainChargeableAmount = 0.00;

	private Double remainingAmountToAuth = 0.00;

	private Element paymentMethodOnOrder = null;

	private String paymentKey = "";

	private boolean paymentMethodEditable = false;

	private String returnURL;

	protected CreditCardCVVStorageHelper creditCardCVVStorageHelper = null;

	protected XPEDXPaymentMethodHelper paymentMethodHelper = null;

	protected PaymentTypeHelper paymentTypeHelper = null;

	protected boolean paymentBookForSingleNonGiftPayment = true;

	private Map<String, String> creditCardType = new LinkedHashMap<String, String>();

	public boolean isPaymentMethodEditable() {
		return paymentMethodEditable;
	}

	public void setPaymentMethodEditable(boolean editable) {
		paymentMethodEditable = editable;
	}

	public String getPaymentKey() {
		return paymentKey;
	}

	public void setPaymentKey(String pKey) {
		paymentKey = pKey;
	}

	public boolean isMultiNonGiftPaymentMethod() {
		return multiNonGiftPaymentMethod;
	}

	public void setMultiNonGiftPaymentMethod(boolean multiNonGiftPaymentMethod) {
		this.multiNonGiftPaymentMethod = multiNonGiftPaymentMethod;
	}

	public boolean isSingleNonGiftPaymentMethod() {
		return singleNonGiftPaymentMethod;
	}

	public void setSingleNonGiftPaymentMethod(boolean singleNonGiftPaymentMethod) {
		this.singleNonGiftPaymentMethod = singleNonGiftPaymentMethod;
	}

	public String getSvcNo() {
		return svcNo;
	}

	public void setSvcNo(String svcNo) {
		this.svcNo = svcNo;
	}

	public String getOrderHeaderKey() {
		return orderHeaderKey;
	}

	public void setOrderHeaderKey(String orderHeaderKey) {
		this.orderHeaderKey = orderHeaderKey;
	}

	public String getReturnURL() {
		return returnURL;
	}

	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	@Override
	public String execute() {
		creditCardCVVStorageHelper = new CreditCardCVVStorageHelper(
				getWCContext());
		XPEDXWCUtils.checkMultiStepCheckout();
		String result = RESULT_SINGLE_NON_GIFT_PAYMENT;
		Set<String> mashupId = new TreeSet<String>();
		mashupId.add(ORDER_PAYMENT_MASHUP);
		try {
			getCreditCardList();
			getCompleteOrderDetailsDoc();
			paymentMethodHelper = new XPEDXPaymentMethodHelper(getWCContext(),
					this.getOutputDocument().getDocumentElement());
			paymentTypeHelper = paymentMethodHelper.getPaymentTypeHelper();
			paymentMethodHelper.processRemainingAmountToAuth();
			processPaymentForGiftAndNonGift();
			setPaymentBook();
			orderBillToAddress = scXmlUtils.getChildElement(getOutputDocument()
					.getDocumentElement(), "PersonInfoBillTo");
			setOrdersPaymentRemainingToPay();
			if (onlyGiftPayment
					|| (singleNonGiftPaymentMethod && !useMorePayment)) {
				result = RESULT_SINGLE_NON_GIFT_PAYMENT;
			} else if (multiNonGiftPaymentMethod
					|| (singleNonGiftPaymentMethod && useMorePayment)) {
				result = RESULT_MULTIPLE_NON_GIFT_PAYMENT;
			}
		} catch (CannotBuildInputException cbiEx) {
			log
					.error(
							"Exception in creating input xml for getting order payment.",
							cbiEx);
			result = ERROR;
		} catch (DraftOrderFlagMismatchException dofme) {
			handleValidityCheckExceptions(dofme, log);
			result = DRAFT_ORDER_FLAG_MISMATCH_ERROR;
		} catch (AuthorizedClientMismatchException acme) {
			handleValidityCheckExceptions(acme, log);
			result = AUTHORIZED_CLIENT_MISMATCH_ERROR;
		} catch (Exception ex) {
			log.error("Exception in getting order payment.", ex);
			WCUtils.setErrorInContext(getWCContext(), ex);
			result = ERROR;
		}
		return result;
	}

	public void setOrdersPaymentRemainingToPay() {
		Element overallTotal = scXmlUtils.getChildElement(this
				.getOutputDocument().getDocumentElement(), "OverallTotals");
		Double amountToPay = scXmlUtils.getDoubleAttribute(overallTotal,
				"GrandTotal");
		setOrderTotal(amountToPay);
		Element chargeTransDetailsElement = scXmlUtils.getChildElement(this
				.getOutputDocument().getDocumentElement(),
				"ChargeTransactionDetails");
		remainingAmountToAuth = scXmlUtils.getDoubleAttribute(
				chargeTransDetailsElement, "RemainingAmountToAuth");
		if (remainingAmountToAuth < OrderConstants.ZERO_REMAINING_AMOUNT_TO_AUTH) {
			remainChargeableAmount = OrderConstants.ZERO_REMAINING_AMOUNT_TO_AUTH;
		} else {
			remainChargeableAmount = remainingAmountToAuth;
		}

	}

	/**
	 * This method is to be use for setting the max charge sequence defined in available order payment
	 * as well as figure out how many gift card payments are available, whehter sigle non gift payment
	 * or multiple non gift payment available.
	 */
	private void processPaymentForGiftAndNonGift() {
		int numberOfSVCPaymentMethods = paymentMethodHelper
				.getListOfStoredValueCardElements().size();
		int numberOfNonSVCPaymentMethods = paymentMethodHelper
				.getListOfNonStoredValueCardElements().size();
		int noPayments = numberOfSVCPaymentMethods
				+ numberOfNonSVCPaymentMethods;

		onlyGiftPayment = (noPayments == numberOfSVCPaymentMethods) ? true
				: false;

		singleNonGiftPaymentMethod = (numberOfNonSVCPaymentMethods == 1) ? true
				: false;
		multiNonGiftPaymentMethod = (numberOfNonSVCPaymentMethods > 1) ? true
				: false;
	}

	public String setPaymentBook() {
		String result = SUCCESS;
		try {
			paymentTypeHelper = new PaymentTypeHelper(getWCContext());
			elementPaymentBook = prepareAndInvokeMashup(GET_PAYMENT_BOOK_MASHUP);
			customerCreditCardPayments.clear();
			customerAccountPayments.clear();
			if (elementPaymentBook != null) {
				Element customerContact = scXmlUtils.getElementByAttribute(
						elementPaymentBook,
						"CustomerContactList/CustomerContact",
						"CustomerContactID", getWCContext()
								.getCustomerContactId());
				ArrayList<Element> elePayment = scXmlUtils.getElements(
						customerContact,
						"CustomerPaymentMethodList/CustomerPaymentMethod");
				elePayment.addAll(scXmlUtils.getElements(elementPaymentBook,
						"CustomerPaymentMethodList/CustomerPaymentMethod"));

				for (Element elePaymentMethod : elePayment) {
					String pType = elePaymentMethod.getAttribute("PaymentType");
					String paymentTypeGroup = paymentTypeHelper
							.getPaymentTypeGroupForPaymentType(pType);
					boolean paymentBookForMultiNonGiftPayment = (paymentBookForSingleNonGiftPayment == false && pType
							.equals(paymentType));
					if (OrderConstants.PAYMENT_TYPE_GROUP_CREDIT_CARD
							.equals(paymentTypeGroup)) {
						if (paymentBookForMultiNonGiftPayment) {
							customerCreditCardPayments.add(elePaymentMethod);
						} else if (paymentBookForSingleNonGiftPayment) {
							customerCreditCardPayments.add(elePaymentMethod);
						}
					} else if (OrderConstants.PAYMENT_TYPE_GROUP_CUSTOMER_ACCOUNT
							.equals(paymentTypeGroup)) {
						if (paymentBookForMultiNonGiftPayment) {
							customerAccountPayments.add(elePaymentMethod);
						} else if (paymentBookForSingleNonGiftPayment) {
							customerAccountPayments.add(elePaymentMethod);
						}
					}

				}
			}
		} catch (CannotBuildInputException cbiEx) {
			log
					.error(
							"Exception in creating input xml for getting payment book.",
							cbiEx);
		} catch (Exception ex) {
			log.error("Exception in getting payment book.", ex);
		}
		return result;

	}

	public List<Element> getCustomerAccountPayments() {
		return customerAccountPayments;
	}

	public void setCustomerAccountPayments(List<Element> customerAccountPayments) {
		this.customerAccountPayments = customerAccountPayments;
	}

	public List<Element> getCustomerCreditCardPayments() {
		return customerCreditCardPayments;
	}

	public void setCustomerCreditCardPayments(
			List<Element> customerCreditCardPayments) {
		this.customerCreditCardPayments = customerCreditCardPayments;
	}

	public boolean isOnlyGiftPayment() {
		return onlyGiftPayment;
	}

	public void setOnlyGiftPayment(boolean onlyGiftPayment) {
		this.onlyGiftPayment = onlyGiftPayment;
	}

	public Map getPaymentTypesForSingleNonGiftPayment() {
		Map map = new TreeMap<String, String>();
		List<String> nonStotePaymentTypeList = paymentMethodHelper
				.getPaymentTypeHelper().getListOfNonStoredValueCardTypes();
		for (String paymentType : nonStotePaymentTypeList) {
			if (paymentMethodHelper.getPaymentTypeHelper()
					.getPaymentTypeGroupForPaymentType(paymentType).equals(
							OrderConstants.PAYMENT_TYPE_GROUP_CREDIT_CARD)) {
				map.put(paymentType, paymentMethodHelper.getPaymentTypeHelper()
						.getDescriptionForPaymentType(paymentType));
			}
		}
		return map;
	}

	public Element getOrderBillToAddress() {
		return orderBillToAddress;
	}

	public void setOrderBillToAddress(Element orderBillToAddress) {
		this.orderBillToAddress = orderBillToAddress;
	}

	public Double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public boolean isUseMorePayment() {
		return useMorePayment;
	}

	public void setUseMorePayment(boolean useMorePayment) {
		this.useMorePayment = useMorePayment;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Double getRemainChargeableAmount() {
		return remainChargeableAmount;
	}

	public void setRemainChargeableAmount(Double remainChargeableAmount) {
		this.remainChargeableAmount = remainChargeableAmount;
	}

	public Double getRemainingAmountToAuth() {
		return remainingAmountToAuth;
	}

	public void setRemainingAmountToAuth(Double remainingAmountToAuth) {
		this.remainingAmountToAuth = remainingAmountToAuth;
	}

	public String getPaymentArrayName() {
		return paymentArrayName;
	}

	public void setPaymentArrayName(String paymentArrayName) {
		this.paymentArrayName = paymentArrayName;
	}

	public Map getCustomerAccountFromPB(int counter,
			List<Element> customerAccount) {
		Map<Integer, String> mapCustomerAccount = new TreeMap<Integer, String>();
		int size = customerAccount.size();
		for (int i = 0; i < size; i++) {
			mapCustomerAccount.put(counter + i, scXmlUtils.getAttribute(
					customerAccount.get(i), "DisplayCustomerAccountNo"));
		}
		return mapCustomerAccount;
	}

	public String executePaymentMethodOnOrder() {
		String result = SUCCESS;
		Set<String> mashupIdBillTo = new TreeSet<String>();
		mashupIdBillTo.add(ORDER_BILL_TO_ADDRESS_MASHUP);
		creditCardCVVStorageHelper = new CreditCardCVVStorageHelper(
				getWCContext());

		try {
			getCreditCardList();
			paymentTypeHelper = new PaymentTypeHelper(getWCContext());
			String pTypeGroup = paymentTypeHelper
					.getPaymentTypeGroupForPaymentType(getPaymentType());
			if (!isPaymentMethodEditable()) {
				if (pTypeGroup
						.equals(OrderConstants.PAYMENT_TYPE_GROUP_CREDIT_CARD)) {
					Element elementAddress = prepareAndInvokeMashups(
							mashupIdBillTo).get(ORDER_BILL_TO_ADDRESS_MASHUP);
					setOrderBillToAddress(scXmlUtils.getChildElement(
							elementAddress, "PersonInfoBillTo"));
				}
				result = INPUT;
			} else {
				getCompleteOrderDetailsDoc();
				Element element = getOutputDocument().getDocumentElement();
				paymentMethodOnOrder = scXmlUtils.getElementByAttribute(
						element, "PaymentMethods/PaymentMethod", "PaymentKey",
						getPaymentKey());

				if (paymentMethodOnOrder != null
						&& pTypeGroup
								.equals(OrderConstants.PAYMENT_TYPE_GROUP_CREDIT_CARD)) {
					orderBillToAddress = scXmlUtils.getChildElement(
							paymentMethodOnOrder, "PersonInfoBillTo");
				}
			}
		} catch (CannotBuildInputException cbiEx) {
			log
					.error(
							"Exception in creating input xml for getting order payment.",
							cbiEx);
			result = ERROR;
		} catch (DraftOrderFlagMismatchException dofme) {
			handleValidityCheckExceptions(dofme, log);
			result = DRAFT_ORDER_FLAG_MISMATCH_ERROR;
		} catch (AuthorizedClientMismatchException acme) {
			handleValidityCheckExceptions(acme, log);
			result = AUTHORIZED_CLIENT_MISMATCH_ERROR;
		} catch (Exception e) {
			log.error("Exception in getting order payment.", e);
			WCUtils.setErrorInContext(getWCContext(), e);

			result = ERROR;
		}

		return result;

	}

	public Element getPaymentMethodOnOrder() {
		return paymentMethodOnOrder;
	}

	public void setPaymentMethodOnOrder(Element element) {
		paymentMethodOnOrder = element;
	}

	public boolean isGiftPaymentAllowed() {
		return paymentMethodHelper.getPaymentTypeHelper()
				.getListOfStoredValueCardTypes().size() > 0 ? true : false;
	}

	public String getBackButtonView() {
		boolean multiStepCheckoutReference = true;
		String backButtonView = "";

		/* Place the logic for evaluating multiStepCheckoutReference flag based
		 * on the order business rule.
		 */
		backButtonView = multiStepCheckoutReference ? MULTI_STEP_CHECKOUT_BACK_BUTTON_VIEW
				: SINGLE_STEP_CHECKOUT_BACK_BUTTON_VIEW;

		return backButtonView;
	}

	public boolean isReturnURLExist() {
		return (returnURL != null && !returnURL.trim().equals("")) ? true
				: false;
	}

	public CreditCardCVVStorageHelper getCreditCardCVVStorageHelper() {
		return creditCardCVVStorageHelper;
	}

	public XPEDXPaymentMethodHelper getPaymentMethodHelper() {
		return paymentMethodHelper;
	}

	public PaymentTypeHelper getPaymentTypeHelper() {
		return paymentTypeHelper;
	}

	public boolean isPaymentBookForSingleNonGiftPayment() {
		return paymentBookForSingleNonGiftPayment;
	}

	public void setPaymentBookForSingleNonGiftPayment(
			boolean paymentBookForSingleNonGiftPayment) {
		this.paymentBookForSingleNonGiftPayment = paymentBookForSingleNonGiftPayment;
	}

	public void setPaymentTypeHelper(PaymentTypeHelper paymentTypeHelper) {
		this.paymentTypeHelper = paymentTypeHelper;
	}

	@Override
	protected String getOrderDetailsMashupName() {
		return ORDER_PAYMENT_MASHUP;
	}

	private void getCreditCardList() {
		try {
			Map<String, String> cMap = CommonCodeUtil.getCommonCodes(
					YCD_CREDIT_CARD_TYPE, CommonCodeDescriptionType.SHORT,
					getWCContext(), true);
			/*
			 * Sort YCD_CREDIT_CARD_TYPE common codes.
			 */
			List list = new LinkedList(cMap.entrySet());
			Collections.sort(list, new CreditCardDescriptionComparator());
			creditCardType.clear();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Map.Entry entry = (Map.Entry) itr.next();
				creditCardType.put((String) entry.getKey(), (String) entry
						.getValue());
			}
		} catch (Exception ex) {
			log.error("Exception in sorting credit card.", ex);
		}
	}

	/**
	 * This class compares two common code element object for common code description type.
	 * @author AJindal
	 *
	 */
	private static class CreditCardDescriptionComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			String ccDesc1 = (String) ((Map.Entry) (o1)).getValue();
			String ccDesc2 = (String) ((Map.Entry) (o2)).getValue();
			return ccDesc1.compareTo(ccDesc2);
		}

	}

	public Map<String, String> getCreditCardType() {
		return creditCardType;
	}

	public void setCreditCardType(Map<String, String> creditCardType) {
		this.creditCardType = creditCardType;
	}
}
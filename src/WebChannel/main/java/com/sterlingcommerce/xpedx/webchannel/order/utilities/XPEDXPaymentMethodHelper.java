package com.sterlingcommerce.xpedx.webchannel.order.utilities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.order.OrderConstants;
import com.sterlingcommerce.webchannel.order.utilities.PaymentTypeHelper;
import com.sterlingcommerce.webchannel.utilities.PaymentUtil;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

/**
 * Helper class for dealing with Order Payment Methods.
 */
public class XPEDXPaymentMethodHelper {

	private final static String GET_CUSTOMER_ACCOUNT_BALANCE = "GetCustomerAccountBalance";

	/** Charge sequence incrementor. */
	private static final Integer CHARGE_SEQUENCE_INCREMENTOR = 1;

	public static final String ZERO_AMOUNT_STRING = "0.00";

	private static final String STRING_OF_ZEROS = "00000000000000000000";

	private static double ZERO_VALUE_PLUS_EPSILON_FOR_COMPARISONS = 0.0000001;

	private static final Integer MAX_INTEGER = new Integer(Integer.MAX_VALUE);

	protected List<Element> svcPaymentMethodList = new ArrayList<Element>();

	protected List<Element> nonSVCCardPaymentMethodList = new ArrayList<Element>();

	protected int indexOfLastCCOrAcct = -1;

	protected String remainingAmountToAuthStr = null;

	protected Integer maxChargeSequence = -1;

	protected boolean updateFlag = false;

	protected boolean presentOtherPaymentGroupMethod = false;

	protected boolean presentCustomerAccountPGMethod = false;

	protected PaymentTypeHelper paymentTypeHelper = null;

	protected Element orderEl = null;

	private static final Logger LOG = Logger
			.getLogger(XPEDXPaymentMethodHelper.class);

	private List<Element> customerAccountListOnPB = new ArrayList<Element>();

	private List<String> customerAccountList = new ArrayList<String>();

	/**
	 * Constructs a new PaymentMethodHelper.
	 * 
	 * @param webContext
	 *            the web context.
	 * @param orderEl
	 *            the Order element from an order XAPI call.
	 */
	public XPEDXPaymentMethodHelper(IWCContext webContext, Element orderEl)
			throws Exception {
		this.orderEl = orderEl;
		paymentTypeHelper = new PaymentTypeHelper(webContext);

		List<Element> rawPaymentMethodList = XMLUtilities.getElements(orderEl,
				"/Order/PaymentMethods/PaymentMethod");
		List<Element> paymentMethodList = getElementsSortedByChargeSequence(rawPaymentMethodList); 
		Iterator<Element> iter = paymentMethodList.iterator();
		while (iter.hasNext()) {
			Element paymentMethodEl = iter.next();

			/* Get the next available max charge sequence number. */
			int chargeSeq = SCXmlUtils.getIntAttribute(paymentMethodEl,
					"ChargeSequence");
			maxChargeSequence = chargeSeq > maxChargeSequence ? chargeSeq
					: maxChargeSequence;

			String paymentType = paymentMethodEl.getAttribute("PaymentType");
			String paymentTypeGroup = paymentTypeHelper
					.getPaymentTypeGroupForPaymentType(paymentType);

			if (!presentOtherPaymentGroupMethod
					&& paymentTypeGroup.equals(OrderConstants.OTHER_PAYMENT)) {
				presentOtherPaymentGroupMethod = true;
			}

			/*
			 * Filter out the Remove payment method during pending order change
			 * process.
			 */
			String action = paymentMethodEl.getAttribute("Action");
			if (action != null
					&& action
							.equals(OrderConstants.ORDER_PAYMENT_REMOVE_ACTION))
				continue;

			if (paymentTypeHelper.isStoredValueCardType(paymentType)) {
				svcPaymentMethodList.add(paymentMethodEl);
			} else {
				nonSVCCardPaymentMethodList.add(paymentMethodEl);
				if (((OrderConstants.PAYMENT_TYPE_GROUP_CREDIT_CARD
						.equals(paymentTypeGroup)) || (OrderConstants.PAYMENT_TYPE_GROUP_CUSTOMER_ACCOUNT
						.equals(paymentTypeGroup)))
						&& !isSuspendedPayment(paymentMethodEl)) {
					indexOfLastCCOrAcct = nonSVCCardPaymentMethodList.size() - 1;
					if (!presentCustomerAccountPGMethod
							&& OrderConstants.PAYMENT_TYPE_GROUP_CUSTOMER_ACCOUNT
									.equals(paymentTypeGroup)) {
						presentCustomerAccountPGMethod = true;
					}
				}
			}
		}
		Element chargeTransactionDetails = XMLUtilities.getElement(orderEl,
				"/Order/ChargeTransactionDetails");
		if (chargeTransactionDetails != null) {
			remainingAmountToAuthStr = chargeTransactionDetails
					.getAttribute("RemainingAmountToAuth");
		}
		// changed for order-on-behalf of functionality
		String billToCustomerId = XPEDXWCUtils
				.getLoggedInCustomerFromSession(webContext);
		String customerId = (billToCustomerId != null && billToCustomerId
				.trim().length() > 0) ? billToCustomerId : webContext
				.getCustomerId();
		if (presentCustomerAccountPGMethod) {
			Element ePaymentBook = PaymentUtil.getPaymentElement(
					GET_CUSTOMER_ACCOUNT_BALANCE, customerId, webContext
							.getCustomerContactId(), webContext);
			Element elementPaymentBook = SCXmlUtils.getElementByAttribute(
					ePaymentBook, "CustomerContactList/CustomerContact",
					"CustomerContactID", webContext.getCustomerContactId());
			customerAccountListOnPB.clear();
			customerAccountListOnPB.addAll(SCXmlUtils.getElements(
					elementPaymentBook,
					"CustomerPaymentMethodList/CustomerPaymentMethod"));
			customerAccountListOnPB.addAll(SCXmlUtils.getElements(ePaymentBook,
					"CustomerPaymentMethodList/CustomerPaymentMethod"));

			for (Iterator iterator = customerAccountListOnPB.iterator(); iterator
					.hasNext();) {
				Element element = (Element) iterator.next();
				if (!(paymentTypeHelper
						.getPaymentTypeGroupForPaymentType(element
								.getAttribute("PaymentType"))
						.equals(OrderConstants.PAYMENT_TYPE_GROUP_CUSTOMER_ACCOUNT))) {
					iterator.remove();
				}
			}
		}
	}

	/**
	 * Returns the inputList of elements sorted by their ChargeSequence
	 * (interpreted as integers) values.
	 * 
	 * @param inputList
	 *            the list of Elements to sort.
	 * @return list of input elements sorted numerically by ChargeSequence
	 *         values.
	 */
	protected List<Element> getElementsSortedByChargeSequence(
			List<Element> inputList) {
		SortedMap<Integer, List<Element>> sortedElementMap = new TreeMap<Integer, List<Element>>();
		Iterator<Element> elementIter = inputList.iterator();
		// First, sort all the elements into "buckets" based on their
		// ChargeSequence.
		while (elementIter.hasNext()) {
			Element element = elementIter.next();
			String chargeSequenceStr = element.getAttribute("ChargeSequence");
			Integer chargeSequence = MAX_INTEGER;
			try {
				chargeSequence = new Integer(chargeSequenceStr);
			} catch (NumberFormatException nfe) {
				LOG
						.error("getElementsSortedByChargeSequence found an invalid ChargeSequence of "
								+ chargeSequenceStr);
			}
			List<Element> elementList = sortedElementMap.get(chargeSequence);
			if (elementList == null) {
				elementList = new ArrayList<Element>();
				sortedElementMap.put(chargeSequence, elementList);
			}
			elementList.add(element);
		}

		// Then move all of the elements, in sorted order, to the
		// orderedPaymentMethods list.
		ArrayList<Element> returnList = new ArrayList<Element>();
		Iterator<List<Element>> elementListIter = sortedElementMap.values()
				.iterator();
		while (elementListIter.hasNext()) {
			returnList.addAll(elementListIter.next());
		}

		return returnList;
	}

	/**
	 * Processes the RemainingAmountToAuth value by distributing it against the
	 * available payment methods by modifying the MaxChargeLimit value(s) as
	 * necessary. Charge distribution is based on the draft order charge
	 * distribution logic used by COM. Any outstanding balance is added to the
	 * last active payment method of payment type group CREDIT_CARD or
	 * CUSTOMER_ACCOUNT, if one exists, with a floor of 0.00.
	 * 
	 * @return true if the distribution was successful and all of the
	 *         outstanding balance could be distributed across the payment
	 *         methods, or false if there was a (positive) outstanding balance
	 *         that could not be applied to any payment methods.
	 */
	public boolean processRemainingAmountToAuth() throws Exception {
		// To calculate the "real" balance, start with RemainingAmountToAuth
		double realBalance = getDoubleValueFromString(remainingAmountToAuthStr);

		// Increase balance by MaxChargeLimit of all non-SVC payments (that
		// aren't suspended).
		Iterator<Element> elementIter = nonSVCCardPaymentMethodList.iterator();
		while (elementIter.hasNext()) {
			Element paymentMethodEl = elementIter.next();
			if (!isSuspendedPayment(paymentMethodEl)) {
				double maxChargeLimit = getDoubleValueFromString(paymentMethodEl
						.getAttribute("MaxChargeLimit"));
				realBalance = realBalance + maxChargeLimit;
			}
		}

		// Increase balance by MaxChargeLimit (if non-zero) or FundsAvailable of
		// all SVC payments that aren't suspended
		// (accounting for all the "special case handling" used in the
		// RemainingAmountToAuth when values are set illogically),
		// or the TotalCharged for those that are.
		elementIter = svcPaymentMethodList.iterator();
		while (elementIter.hasNext()) {
			Element paymentMethodEl = elementIter.next();
			if (!isSuspendedPayment(paymentMethodEl)) {
				String totalChargedStr = paymentMethodEl
						.getAttribute("TotalCharged");
				double totalCharged = getDoubleValueFromString(totalChargedStr);
				String maxChargeLimitStr = paymentMethodEl
						.getAttribute("MaxChargeLimit");
				double maxChargeLimit = getDoubleValueFromString(maxChargeLimitStr);

				if (totalCharged > maxChargeLimit) {
					// This is a pathalogical case which should never happen,
					// since MaxChargeLimit should always be >= TotalCharged.
					maxChargeLimitStr = totalChargedStr;
					maxChargeLimit = totalCharged;
				}

				double fundsAvailable = getDoubleValueFromString(paymentMethodEl
						.getAttribute("FundsAvailable"));
				double relevantAmount = fundsAvailable; // Assume the relevant
														// amount used by
														// RemainingAmountToAuth
														// was FundsAvailable.

				// If there is a MaxChargeLimit, need to re-evaluate.
				if (!((ZERO_AMOUNT_STRING.equals(maxChargeLimitStr))
						|| ("".equals(maxChargeLimitStr)) || (maxChargeLimitStr == null))) {
					relevantAmount = maxChargeLimit;

					double deltaForPayment = maxChargeLimit - totalCharged;
					if (deltaForPayment > fundsAvailable) {
						// This is pathalogical too. RemainingAmountToAuth
						// would've been calculated based on the charged amount
						// + funds available in this case.
						relevantAmount = totalCharged + fundsAvailable;
					}
				}

				realBalance = realBalance + relevantAmount;
			} else {
				realBalance = realBalance
						+ getDoubleValueFromString(paymentMethodEl
								.getAttribute("TotalCharged"));
			}
		}

		// Now we have the "true" outstanding balance for the order. Now we put
		// apply the payment methods against the balance.

		// Start by applying any amounts that have already been charged, since
		// we can't undo the charges.
		realBalance = applyChargesToBalance(svcPaymentMethodList, realBalance);
		// realBalance = applyChargesToBalance(nonSVCCardPaymentMethodList,
		// realBalance); - don't do non-SVC, since user can manually manipulate
		// MaxChargeLimit still

		// Now apply any available SVC funds (for non-suspended methods), until
		// we have either run out of funds or have a zero balance remaining.
		elementIter = svcPaymentMethodList.iterator();
		while ((elementIter.hasNext())
				&& (realBalance > ZERO_VALUE_PLUS_EPSILON_FOR_COMPARISONS)) {
			Element paymentMethodEl = elementIter.next();
			if (!isSuspendedPayment(paymentMethodEl)) {
				double fundsAvailable = getDoubleValueFromString(paymentMethodEl
						.getAttribute("FundsAvailable"));
				double fundsToUse = fundsAvailable;
				if (realBalance < fundsAvailable) {
					fundsToUse = realBalance;
				}
				realBalance = realBalance - fundsToUse;
				double newMaxChargeLimit = getDoubleValueFromString(paymentMethodEl
						.getAttribute("TotalCharged"))
						+ fundsToUse;

				paymentMethodEl
						.setAttribute(
								"MaxChargeLimit",
								makePrecisionMatchRemainingAmountToAuth(newMaxChargeLimit));
				paymentMethodEl.setAttribute("DeltaMaxChargeLimit",
						makePrecisionMatchRemainingAmountToAuth(fundsToUse));
			} else {
				// If SVC is suspended, just copy the TotalCharged value into
				// the MaxChargeLimit field for display.
				paymentMethodEl.setAttribute("MaxChargeLimit", paymentMethodEl
						.getAttribute("TotalCharged"));
			}
		}
		// Finish processing the list if there are unvisited elements (resolves
		// issue with bad MaxChargeLimit on SVC(s) below an SVC which has just
		// been un-suspended)
		while (elementIter.hasNext()) {
			Element paymentMethodEl = elementIter.next();
			paymentMethodEl.setAttribute("MaxChargeLimit", paymentMethodEl
					.getAttribute("TotalCharged"));
		}

		// Now apply the MaxChargeLimit of all the non-SVC cards (that aren't
		// suspended) to the balance
		elementIter = nonSVCCardPaymentMethodList.iterator();
		while (elementIter.hasNext()) {
			Element paymentMethodEl = elementIter.next();
			if (!isSuspendedPayment(paymentMethodEl)) {
				double maxChargeLimit = getDoubleValueFromString(paymentMethodEl
						.getAttribute("MaxChargeLimit"));
				realBalance = realBalance - maxChargeLimit;
			}
		}

		// Last but not least, apply the realBalance to the last (non-suspended)
		// non-SVC card.
		boolean success = false;

		if (indexOfLastCCOrAcct > -1) {
			// We have a CC or Acct payment method to fix-up.
			Element lastCCOrAccount = nonSVCCardPaymentMethodList
					.get(indexOfLastCCOrAcct);
			double oldMaxChargeLimit = getDoubleValueFromString(lastCCOrAccount
					.getAttribute("MaxChargeLimit"));
			String paymentTypeGroup = paymentTypeHelper
					.getPaymentTypeGroupForPaymentType(lastCCOrAccount
							.getAttribute("PaymentType"));
			double newMaxChargeLimit = oldMaxChargeLimit + realBalance;
			realBalance = ZERO_VALUE_PLUS_EPSILON_FOR_COMPARISONS;
			if (newMaxChargeLimit > ZERO_VALUE_PLUS_EPSILON_FOR_COMPARISONS) {
				if (paymentTypeGroup
						.equals(OrderConstants.PAYMENT_TYPE_GROUP_CUSTOMER_ACCOUNT)) {
					double adjMaxChargeLimit = getAdjustedMaxChargeLimitForCA(
							newMaxChargeLimit, lastCCOrAccount);
					if (newMaxChargeLimit > adjMaxChargeLimit) {
						realBalance = newMaxChargeLimit - adjMaxChargeLimit;
					}
					newMaxChargeLimit = adjMaxChargeLimit;
				}

				lastCCOrAccount
						.setAttribute(
								"MaxChargeLimit",
								makePrecisionMatchRemainingAmountToAuth(newMaxChargeLimit));
			} else {
				lastCCOrAccount.setAttribute("MaxChargeLimit",
						ZERO_AMOUNT_STRING);
			}

			/*
			 * realBalance would be greater than zero if last payment method
			 * belongs to customer account payment group and available account
			 * balance would be less then the amount applied against the
			 * customer account.
			 */
			if (realBalance > ZERO_VALUE_PLUS_EPSILON_FOR_COMPARISONS) {
				success = false;
				remainingAmountToAuthStr = makePrecisionMatchRemainingAmountToAuth(realBalance);
			} else {
				success = true;
			}
			setUpdateFlag(success);
		} else {
			// No CC or Acct to fix up, which is still okay as long as the
			// realBalance is <= 0
			if (realBalance <= ZERO_VALUE_PLUS_EPSILON_FOR_COMPARISONS) {
				success = true;
				setUpdateFlag(success);
			}
		}

		return success;
	}

	/**
	 * This method would be used for getting the adjusted maxcharge limit for
	 * customer account payment type group method.
	 * 
	 * @param newMaxChargeLimit
	 *            max charge limit applied on the customer account payment
	 *            method.
	 * @param custAccPaymentOnOrder
	 *            existing record of customer account that would be want to
	 *            update with new maxcharge limit.
	 * @return Adjusted amount of the max charge limit of customer account.
	 */
	public double getAdjustedMaxChargeLimitForCA(double newMaxChargeLimit,
			Element custAccPaymentOnOrder) {
		double totalAuthAmount = getDoubleValueFromString(custAccPaymentOnOrder
				.getAttribute("TotalAuthorized"));
		String displayCustomerAccountNo = custAccPaymentOnOrder
				.getAttribute("DisplayCustomerAccountNo");

		/*
		 * For customer account that is already authorized and authorized amount
		 * greater than of max charge limit, do nothing.
		 */
		if (totalAuthAmount >= newMaxChargeLimit) {
			return newMaxChargeLimit;
		}

		if (newMaxChargeLimit > ZERO_VALUE_PLUS_EPSILON_FOR_COMPARISONS) {
			int caPBsize = customerAccountListOnPB.size();
			Element elePayment = null;
			/*
			 * Get the delta amount of max charge limit if customer account
			 * already authorized.
			 */
			if (totalAuthAmount > ZERO_VALUE_PLUS_EPSILON_FOR_COMPARISONS
					&& newMaxChargeLimit > totalAuthAmount) {
				newMaxChargeLimit = newMaxChargeLimit - totalAuthAmount;
			}

			if (caPBsize > 0) {
				for (int i = 0; i < caPBsize; i++) {
					Element caPaymentOnPB = customerAccountListOnPB.get(i);
					if (caPaymentOnPB.getAttribute("DisplayCustomerAccountNo")
							.equals(displayCustomerAccountNo)) {
						elePayment = caPaymentOnPB;
						break;
					}
				}
				/*
				 * Perform the adjustment with customer account's max charge
				 * limit. if available account balance would be less than
				 * newMaxChargeLimit then availabe account balance become the
				 * new max charge limit.
				 * 
				 */
				if (elePayment != null) {
					double availableAccountBal = getDoubleValueFromString(elePayment
							.getAttribute("AvailableAccountBalance"));

					if (availableAccountBal > ZERO_VALUE_PLUS_EPSILON_FOR_COMPARISONS) {
						if (availableAccountBal < newMaxChargeLimit) {
							newMaxChargeLimit = availableAccountBal;
						}
					} else {
						newMaxChargeLimit = getDoubleValueFromString(makePrecisionMatchRemainingAmountToAuth(0.00));
					}
				} else {
					newMaxChargeLimit = getDoubleValueFromString(makePrecisionMatchRemainingAmountToAuth(0.00));
					customerAccountList.add(displayCustomerAccountNo);
					LOG
							.error("Unable to check customer account available balance for the customer account "
									+ displayCustomerAccountNo);
				}
			} else {
				newMaxChargeLimit = getDoubleValueFromString(makePrecisionMatchRemainingAmountToAuth(0.00));
				customerAccountList.add(displayCustomerAccountNo);
				LOG
						.error("Unable to get customer account payment book, hence available account balance check for the customer account "
								+ displayCustomerAccountNo + " failed.");
			}
			/*
			 * Reverse the total authorized amount to new max charge limit if
			 * customer account already authorized.
			 */
			if (totalAuthAmount > ZERO_VALUE_PLUS_EPSILON_FOR_COMPARISONS) {
				newMaxChargeLimit = newMaxChargeLimit + totalAuthAmount;
			}

		}
		return newMaxChargeLimit;
	}

	/**
	 * Decreases the given balance by the TotalCharged attributes of the given
	 * elements, and returns the modified balance. Note that this method does
	 * NOT check to see whether the payment method is suspended or not, since
	 * suspension only affects additional charges, not ones already made.
	 * 
	 * @param elements
	 *            the paymentMethodElements to evaluate.
	 * @param balance
	 *            the balance to modify.
	 * @return the modified balance after all TotalCharged attribute values have
	 *         been subtracted off.
	 */
	protected double applyChargesToBalance(List<Element> elements,
			double balance) {
		Iterator<Element> elementIter = elements.iterator();
		while (elementIter.hasNext()) {
			Element paymentMethodEl = elementIter.next();
			balance = balance
					- getDoubleValueFromString(paymentMethodEl
							.getAttribute("TotalCharged"));
		}

		return balance;
	}

	/**
	 * This function is to be use to check payment method is supended for more
	 * charges.
	 * 
	 * @param elePayment
	 *            Element of payment method.
	 * @return boolean true if payment method is supended for more charges
	 *         otherwise false.
	 */
	public boolean isSuspendedPayment(Element elePayment) {
		boolean result = false;
		String suspendedFlag = elePayment.getAttribute("SuspendAnyMoreCharges");
		if (!suspendedFlag.equals(OrderConstants.NOT_SUPENDED_PAYMENT_FLAG)) {
			result = true;
		}
		return result;
	}

	/**
	 * Fixes up the charge sequences in the PaymentMethod elements so that they
	 * follow "COM charging order". This ordering is: all payments of
	 * PaymentTypeGroup STORED_VALUE_CARD in ascending order of FundsAvailable
	 * followed by all remaining methods in PaymentType ChargeSequence order
	 * (with sub-ordering in the order they were entered when multiple
	 * PaymentType's have the same ChargeSequence).
	 * 
	 * @return true if any ChargeSequence was changed as a result of this
	 *         operation, false all of the ChargeSequence's ended up being the
	 *         same in the end.
	 */
	public boolean fixUpChargeSequences() throws Exception {
		ArrayList<Element> orderedPaymentMethods = new ArrayList();
		List<Element> sourceList = XMLUtilities.getElements(orderEl,
				"PaymentMethods/PaymentMethod");

		// First, process all payment methods of group STORED_VALUE_CARD. These
		// are to be added in ascending order of FundsAvailable.

		// Transfer all STORED_VALUE_CARD payment methods to a sorted map
		// (FundsAvailable -> List of PaymentMethods with those FundsAvailable).
		SortedMap<Double, List<Element>> sortedSVCMap = new TreeMap<Double, List<Element>>();
		Iterator<Element> elementIter = sourceList.iterator();
		while (elementIter.hasNext()) {
			Element paymentMethodEl = elementIter.next();
			String paymentKey = paymentMethodEl.getAttribute("PaymentKey");
			String paymentType = paymentMethodEl.getAttribute("PaymentType");
			String paymentTypeGroup = paymentTypeHelper
					.getPaymentTypeGroupForPaymentType(paymentType);
			if (OrderConstants.PAYMENT_TYPE_GROUP_STORE_VALUE_CARD
					.equals(paymentTypeGroup)) {
				elementIter.remove(); // Remove it from the list, since we're
										// processing it now.
				String fundsAvailableStr = paymentMethodEl
						.getAttribute("FundsAvailable");
				Double fundsAvailable = null;
				try {
					fundsAvailable = new Double(paymentMethodEl
							.getAttribute("FundsAvailable"));
				} catch (NumberFormatException nfe) {
					LOG.error("Invalid FundsAvailable value of "
							+ fundsAvailableStr + " for PaymentKey "
							+ paymentKey, nfe);
					fundsAvailable = new Double(ZERO_AMOUNT_STRING);
				}

				List<Element> listForFundsAvailable = sortedSVCMap
						.get(fundsAvailable);
				if (listForFundsAvailable == null) {
					listForFundsAvailable = new ArrayList<Element>();
					sortedSVCMap.put(fundsAvailable, listForFundsAvailable);
				}
				listForFundsAvailable.add(paymentMethodEl);
			}
		}
		// Then move all of the elements, in sorted order, to the
		// orderedPaymentMethods list.
		Iterator<List<Element>> elementListIter = sortedSVCMap.values()
				.iterator();
		while (elementListIter.hasNext()) {
			orderedPaymentMethods.addAll(elementListIter.next());
		}

		// Then process the remaining payment methods: primary sort by
		// PaymentType ChargeSequence, and within a ChargeSequence the order
		// they were added.

		SortedMap<Integer, List<String>> chargeSequenceToPaymentTypeList = paymentTypeHelper
				.getSortedMapOfChargeSequenceToPaymentTypeList();
		Iterator<List<String>> stringListIter = chargeSequenceToPaymentTypeList
				.values().iterator();
		while (stringListIter.hasNext()) {
			List<String> paymentTypeListForChargeSequence = stringListIter
					.next();
			elementIter = sourceList.iterator();
			while (elementIter.hasNext()) {
				Element paymentMethodEl = elementIter.next();
				String paymentType = paymentMethodEl
						.getAttribute("PaymentType");
				if (paymentTypeListForChargeSequence.contains(paymentType)) {
					elementIter.remove(); // Remove it from the list
					orderedPaymentMethods.add(paymentMethodEl);
				}
			}
		}

		// We shouldn't have any payment methods left in the source list by now.
		// If we do, then it's an error.
		if (!sourceList.isEmpty()) {
			throw new Exception("sourceList for PaymentMethods still contains "
					+ sourceList.size() + " entries after processing");
		}

		// Finally, do the actual ChargeSequence fix-up based on the ordering in
		// the orderedPaymentMethods list.
		boolean sequenceChanged = false;
		for (int i = 0; i < orderedPaymentMethods.size(); i++) {
			Element paymentMethodEl = orderedPaymentMethods.get(i);
			String oldChargeSequence = paymentMethodEl
					.getAttribute("ChargeSequence");
			String newChargeSequence = String.valueOf(i);
			if (!newChargeSequence.equals(oldChargeSequence)) {
				sequenceChanged = true;
				paymentMethodEl.setAttribute("ChargeSequence",
						newChargeSequence);
			}
		}

		return sequenceChanged;
	}

	/**
	 * Turns the given string into it's double equivalent. If the string is null
	 * or empty, ZERO_AMOUNT_STRING will be evaluated instead.
	 * 
	 * @param stringValue
	 *            the string version of the value to be converted.
	 * @return the double equivalent of stringValue (or "0.00" if stringValue is
	 *         null or empty).
	 * @throws NumberFormatException
	 */
	public double getDoubleValueFromString(String stringValue)
			throws NumberFormatException {
		if ((stringValue == null) || ("".equals(stringValue))) {
			stringValue = ZERO_AMOUNT_STRING;
		}

		return Double.parseDouble(stringValue);
	}

	/**
	 * Returns a string version of the source formatted to the same number of
	 * decimal places as the remainingAmountToAuthStr has.
	 * 
	 * @param source
	 *            the value to format.
	 * @return a string version of the source formatted to the same number of
	 *         decimal places as the remainingAmountToAuthStr has.
	 */
	private String makePrecisionMatchRemainingAmountToAuth(double source) {
		int precision = 0;
		int indexOfDecimal = remainingAmountToAuthStr.lastIndexOf(".");
		if (indexOfDecimal > -1) {
			precision = remainingAmountToAuthStr.length()
					- (indexOfDecimal + 1);
		}

		DecimalFormat format = null;
		if (precision > 0) {
			format = new DecimalFormat("0."
					+ STRING_OF_ZEROS.substring(0, precision));
		} else {
			format = new DecimalFormat("0");
		}

		String returnValue = format.format(source);

		return returnValue;
	}

	/**
	 * Returns a list of the order PaymentMethods which are of the payment type
	 * group STORED_VALUE_CARD.
	 * 
	 * @return list of the order PaymentMethods which are of the payment type
	 *         group STORED_VALUE_CARD.
	 */
	public List<Element> getListOfStoredValueCardElements() {
		return svcPaymentMethodList;
	}

	/**
	 * Returns a list of the order PaymentMethods which are not of the payment
	 * type group STORED_VALUE_CARD.
	 * 
	 * @return list of the order PaymentMethods which are not of the payment
	 *         type group STORED_VALUE_CARD.
	 */
	public List<Element> getListOfNonStoredValueCardElements() {
		return nonSVCCardPaymentMethodList;
	}

	/**
	 * Returns the index of the last CREDIT_CARD or CUSTOMER_ACCOUNT payment
	 * type group payment method in the list returned by
	 * getListOfNonStoredValueCardElements().
	 * 
	 * @return the index of the last CREDIT_CARD or CUSTOMER_ACCOUNT payment
	 *         type group payment method in the list returned by
	 *         getListOfNonStoredValueCardElements() if one exists, otherwise
	 *         returns -1.
	 */
	public int getIndexOfLastCreditCardOrCustomerAccount() {
		return indexOfLastCCOrAcct;
	}

	/**
	 * Convenience method to return the PaymentTypeHelper instantiated by this
	 * class.
	 * 
	 * @return the PaymentTypeHelper instantiated by this class.
	 */
	public PaymentTypeHelper getPaymentTypeHelper() {
		return paymentTypeHelper;
	}

	public Integer getMaxChargeSequence() {
		return (maxChargeSequence + CHARGE_SEQUENCE_INCREMENTOR);
	}

	public String getRemainingAmountToAuthString() {
		return remainingAmountToAuthStr;
	}

	public boolean isRemoveAllowedOnPaymentMethod(Element paymentMethodElement) {
		boolean result = true;
		String awaitingAuthInterfaceAmount = paymentMethodElement
				.getAttribute("AwaitingAuthInterfaceAmount");
		String awaitingChargeInterfaceAmount = paymentMethodElement
				.getAttribute("AwaitingChargeInterfaceAmount");
		String requestedAuthAmount = paymentMethodElement
				.getAttribute("RequestedAuthAmount");
		String requestedChargeAmount = paymentMethodElement
				.getAttribute("RequestedChargeAmount");
		String totalAuthorized = paymentMethodElement
				.getAttribute("TotalAuthorized");
		String totalCharged = paymentMethodElement.getAttribute("TotalCharged");
		String totalRefundedAmount = paymentMethodElement
				.getAttribute("TotalRefundedAmount");

		result = (awaitingAuthInterfaceAmount.equals(ZERO_AMOUNT_STRING)
				&& awaitingChargeInterfaceAmount.equals(ZERO_AMOUNT_STRING)
				&& requestedAuthAmount.equals(ZERO_AMOUNT_STRING)
				&& requestedChargeAmount.equals(ZERO_AMOUNT_STRING)
				&& totalAuthorized.equals(ZERO_AMOUNT_STRING)
				&& totalCharged.equals(ZERO_AMOUNT_STRING) && totalRefundedAmount
				.equals(ZERO_AMOUNT_STRING)) ? true : false;
		return result;

	}

	public boolean isEditAllowedOnPaymentMethod(Element paymentMethodElement) {
		String totalAuthorized = paymentMethodElement
				.getAttribute("TotalAuthorized");
		return totalAuthorized.equals(ZERO_AMOUNT_STRING) ? true : false;
	}

	public boolean isUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(boolean updateFlag) {
		this.updateFlag = updateFlag;
	}

	public boolean isPresentOtherPaymentGroupMethod() {
		return presentOtherPaymentGroupMethod;
	}

	public void setPresentOtherPaymentGroupMethod(
			boolean presentOtherPaymentGroupMethod) {
		this.presentOtherPaymentGroupMethod = presentOtherPaymentGroupMethod;
	}

	public boolean isPresentCustomerAccountPGMethod() {
		return presentCustomerAccountPGMethod;
	}

	public void setPresentCustomerAccountPGMethod(
			boolean presentCustomerAccountPGMethod) {
		this.presentCustomerAccountPGMethod = presentCustomerAccountPGMethod;
	}

	public List<String> getCustomerAccountList() {
		return customerAccountList;
	}

	public void setCustomerAccountList(List<String> customerAccountList) {
		this.customerAccountList = customerAccountList;
	}
}

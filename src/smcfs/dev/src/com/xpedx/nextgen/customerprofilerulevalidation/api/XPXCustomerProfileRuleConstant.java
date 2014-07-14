package com.xpedx.nextgen.customerprofilerulevalidation.api;

public interface XPXCustomerProfileRuleConstant {
	
	String ERROR_NAME_SUFFIX = "ErrorText";
	
	String CHARGE_TYPE = "M";
	
	String STANDARD = "STANDARD";
	
	//This is changed to B2B as per prasanth nair comments	
	String XPX_EDI_ORDER = "B2B";
	
	String NEEDS_ATTENTION_HOLD_TYPE = "NEEDS_ATTENTION";
		
	String NEEDS_ATTENTION_HOLD_DESC = "Needs Attention";
	
	String NEEDS_ATTENTION_ACTIVE_STATUS = "1100";
	
	String NEEDS_ATTENTION_INACTIVE_STATUS = "1300";
	/* error Element Starts */
	String CUSTOMER_COMMENTS_ERROR_TEXT = "CustomerCommentsErrorText";

	String PRICE_OVERRIDE_ERROR_TEXT = "PriceOverrideErrorText";

	String CUSTOMER_PONO_ERROR_TEXT ="CustomerPONoErrorText";

	String SHIP_COMPLETE_ERROR_TEXT = "ShipCompleteErrorText";

	String ACCEPT_NO_NEXTBUSINESSDAY_ERROR_TEXT = "AcceptNoNextBusinessDayErrorText";

	String PREVENT_AUTO_ORDERPLACE_ERROR_TEXT = "PreventAutoOrdPlacementErrorText";

	//String SHIP_DATE_ERROR_TEXT = "ShipDateErrorText";

	String SHIP_METHOD_ERROR_TEXT = "ShipMethodErrorText";

	//String ORDER_CHARGES_ERROR_TEXT = "OrderChargesErrorText";

	//String INTERAL_COMMENTS_ERROR_TEXT = "InternalCommentsErrorText";

	//String OTHER_COMMENTS_ERROR_TEXT = "OtherCommentsErrorText";	 

	String ITEM_ID_ERROR_TEXT = "ItemIDErrorText";
	
	String PREVENT_BACKORDER_ERROR_TEXT = "PreventBackOrderErrorText";

	String ALLOW_BACKORDER_ERROR_TEXT = "AllowBackOrderErrorText";

	String LINE_COMMENTS_ERROR_TEXT = "LineCommentsErrorText";

	String CUSTOMER_LINE_PONO_ERROR_TEXT = "CustomerLinePONoErrorText";

	String REQUESTED_DELIVERY_DATE_ERROR_TEXT= "ReqDeliveryDateErrorText";

	String CUST_LINE1_ERROR_TEXT = "CustLine1ErrorText";

	String CUST_LINE2_ERROR_TEXT = "CustLine2ErrorText";

	String CUST_LINE3_ERROR_TEXT = "CustLine3ErrorText";
	
	String VALIDATE_SHIPTO_ZIP_ERROR_TEXT = "ShipToZipCodeErrorText";
	
	String PRICE_DISCREPENCY_ERROR_TEXT = "PriceDiscrepencyErrorText";
	
	String INCORRECT_ETRADING_ID_ERROR_TEXT = "IncorrectETradingErrorText";
	
	String MANDATORY_CUST_LINE_SEQ_NO = "CustomerLineSeqNoErrorText";
	
	String MANDATORY_CUST_LINE_ACCT_NO = "CustomerLineAcctNoErrorText";
	
	/* error Element Ends */
	
	/*Rule Id starts */
	String ACCEPT_PRICE_OVERRIDE = "AcceptPriceOverRide";
	
	String WEB_HOLD_FLAG = "PlaceOrderOnWBHold";

	String ACCEPT_PRICE_OVERRIDE_ERROR_TEXT = "AcceptPriceOverRideErrorText";

	String PREVENT_AUTO_PLACE = "PreventAutoPlace";

	String HEADER_COMMENT_BY_CUSTOMER = "HeaderCommentByCustomer";

	String DUPLICATE_PO = "DuplicatePO";

	String NON_STANDARD_SHIP_METHOD = "NonStandardShipMethod";

	String CUSTOMER_SELECTED_SHIP_COMPLETE = "CustomerSelectedShipComplete";

	String VALIDATE_SHIPTO_ZIP_CODE = "ValidShiptoZipCode";

	String SHIPDATE_NOT_NEXTBUSINESS_DAY = "ShipDateNotNextBusinessDay";

	//String INCORRECT_BUYER_ID = "IncorrectBuyerID";

	String INCORRECT_ETRADING_ID = "IncorrectETradingID";

	String LINE_COMMENTS_BY_CUSTOMER = "LineCommentsByCustomer";

	String REQUIRED_CUSTOMER_LINE_PO = "RequiredCustomerLinePO";

	String REQUIRED_CUSTOMER_LINE_FIELD1 = "RequireCustomerLineField1";
	
	String REQUIRED_CUSTOMER_LINE_FIELD2 = "RequireCustomerLineField2";
	
	String REQUIRED_CUSTOMER_LINE_FIELD3 = "RequireCustomerLineField3";

	String ALL_DELIVERY_DATES_DONOT_MATCH = "AllDeliveryDatesDoNotMatch";

	String PREVENT_BACK_ORDER = "PreventBackOrder";

	String ITEM_NOT_ALLOWED_FOR_NEXT_DAY_SHIPMENT = "ItemNotAvailableForNextDayShipment";

	//String LINE_LEVEL_CODE_BY_CUSTOMER = "LineLevelCodeByCustomer";

	//String PREVENT_PRICE_BELOEW_COST = "PreventPriceBelowCost";

	String PRICE_DISCREPENCY = "PriceDiscrepency";
	
	String REQUIRED_CUSTOMER_LINE_SEQUENCE_NO = "RequiredCustomerLineSequenceNo";
	
	String REQUIRED_CUSTOMER_LINE_ACCOUNT_NO = "RequiredCustomerLineAccountNo";	
	
	String REQUIRE_CUSTOMER_PO = "RequireCustomerPO";

	String REQ_CUSTOMER_PONO_ERROR_TEXT = "ReqCustomerPONoErrorText";
	
	String GROSS_TRADING_MARGIN = "GrossTradingMargin";
	
	String GROSS_TRADING_MARGIN_ERROR = "Gross Trading Margin beyond the tolerance" ;
	
	String GROSS_TRADING_MARGIN_ERROR_TEXT = "GrossTradingMarginErrorText";
	//String PART_NUMBER_VALIDATION = "PartNumberValidation";
	/*Rule Id Ends */
	enum CustomerProfileRuleID {
		PlaceOrderOnWBHold,AcceptPriceOverRide, PreventAutoPlace, HeaderCommentByCustomer, DuplicatePO, NonStandardShipMethod, CustomerSelectedShipComplete, ValidShiptoZipCode, ShipDateNotNextBusinessDay, /*IncorrectBuyerID,*/ IncorrectETradingID, LineCommentsByCustomer, RequiredCustomerLinePO, RequireCustomerLineField1, RequireCustomerLineField2, RequireCustomerLineField3, AllDeliveryDatesDoNotMatch, PreventBackOrder, ItemNotAvailableForNextDayShipment, /*LineLevelCodeByCustomer, PreventPriceBelowCost,*/ PriceDiscrepency/*, PartNumberValidation*/,RequiredCustomerLineSequenceNo,RequiredCustomerLineAccountNo, RequireCustomerPO,GrossTradingMargin
	};
}

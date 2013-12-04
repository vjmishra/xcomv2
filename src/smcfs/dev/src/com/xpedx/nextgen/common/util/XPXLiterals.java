package com.xpedx.nextgen.common.util;


/**
 * Description: This class is used to declare the Literal values which will also include those that are used in the input/output xmls
 * 
 * @author Prasanth Kumar M.
 *
 */

public class XPXLiterals {
	
	/**@author asekhar-tw on 10-Jan-2011
	 * Literals for B2B-PO-Ack CENT tool logging
	 */
	public static final String ATTRB_TRANS_TYPE = "Attrb";
	public static final String PO_ACK_TRANS_TYPE = "B2B-PO-Ack";
	public static final String B2B_INV_TRANS_TYPE = "B2B-Inv";
	public static final String B2B_PO_TRANS_TYPE = "B2B-PO";
	public static final String OP_TRANS_TYPE = "OP";
	public static final String OU_TRANS_TYPE = "OU";
	public static final String B2B_PA_TRANS_TYPE = "B2B-PA";
	public static final String PA_TRANS_TYPE = "PA";
	public static final String B2B_PUNCH_TRANS_TYPE = "B2B-Punch";
	public static final String CAT_TRANS_TYPE = "Cat";
	public static final String CUST_B_TRANS_TYPE = "Cust-B";
	public static final String CUST_XREF_B_TRANS_TYPE = "Custxref-B";
	public static final String DIV_B_TRANS_TYPE = "Div-B";
	public static final String ENT_B_TRANS_TYPE = "Ent-B";
	public static final String INV_B_TRANS_TYPE = "Inv-B";
	public static final String CD_ITEM_TRANS_TYPE = "CD-Item";
	public static final String LDAP_TRANS_TYPE = "LDAP";
	public static final String PB_B_TRANS_TYPE = "PB-B";
	public static final String UOM_B_TRANS_TYPE = "UOM-B";
	public static final String ITEM_DIV_B_TRANS_TYPE = "ItemDiv-B";
	public static final String CUSTOMER_TRANS_TYPE="Customer Maintenance";
	public static final String YFE_ERROR_VALUE_TOO_LARGE_CLASS = "Column Value Too Large";
	public static final String YFE_ERROR_INVALID_PRODUCT_CODE_CLASS = "Invalid Legacy Product Number";
	public static final String IOBE_ERROR_CLASS = "Unexpected / Invalid";
	public static final String NE_ERROR_CLASS = "Unexpected / Invalid";
	public static final String YFE_ERROR_CLASS = "Application";
	public static final String E_ERROR_CLASS = "Unknown Error";
	public static final String EOF_ERROR_CLASS = "EOF";
	public static final String SOF_ERROR_CLASS = "SOF";
	public static final String CUSTOMER_ERROR_CLASS = "Customer OU failure";
	public static final String ITEM_ERROR_CLASS = "Item OU failure";
	
	public static final String EOF_ERROR_DESC = "This is an EOF message of the";
	public static final String SOF_ERROR_DESC = "This is an SOF of the";

	
/*	public static final String IOBE_ERROR_DESC = "IndexOutOfBoundsException : This order does not exist...!";
	public static final String NE_ERROR_DESC = "NullPointerException : Operations are being attempted on a null object..!";
	public static final String YFE_ERROR_DESC = "YFSException : changeOrder API failed to execute..! Certain attributes might be missing in the input XML..";
	public static final String E_ERROR_DESC = "Exception : Unknown exception..!";*/
	
	
	// Added by Prasanth Kumar M.
	public static final String A_ORDER_TYPE = "OrderType";
	
	public static final String CUSTOMER ="Customer";
	
	public static final String E_ORDER_LINES = "OrderLines";

	public static final String E_ORDER_LINE = "OrderLine";

	public static final String A_LINE_TYPE = "LineType";

	public static final String E_ITEM = "Item";

	public static final String A_ITEM_ID = "ItemID";

	public static final String GET_ITEM_LIST_API = "getItemList";

	public static final String A_EXTN_ITEM_TYPE = "ExtnItemType";

	public static final String E_ORDER = "Order";

	public static final String A_ORDER_LINE_KEY = "OrderLineKey";

	public static final String A_ORDER_HEADER_KEY = "OrderHeaderKey";

	public static final String A_ORGANIZATIONCODE_VALUE = "xpedx";

	public static final String A_ENTERPRISE_CODE = "EnterpriseCode";

	public static final String A_ACTION = "Action";

	public static final String MODIFY = "MODIFY";

	public static final String A_OVERRIDE = "Override";

	public static final String BOOLEAN_FLAG_Y = "Y";

	public static final String E_EXTN = "Extn";

	public static final String CHANGE_ORDER_API = "changeOrder";

	public static final String A_WEB_CONF_NUMBER = "ExtnWebConfNum";

	public static final String A_WEB_LINE_NUMBER = "ExtnWebLineNumber";

	public static final String THIRD_PARTY_ORDER = "THIRD_PARTY";
	
	public static final String E_ORDER_LINE_TRAN_QUANTITY = "OrderLineTranQuantity";

	public static final String A_ORDERED_QTY = "OrderedQty";

	public static final String A_TRANSACTIONAL_UOM = "TransactionalUOM";

	public static final String A_UNIT_OF_MEASURE = "UnitOfMeasure";

	public static final String A_PRODUCT_CLASS = "ProductClass";

	public static final String E_CHAINED_FROM = "ChainedFrom";

	public static final String A_DOCUMENT_TYPE = "DocumentType";

	public static final String A_ORDER_NO = "OrderNo";

	public static final String A_PRIME_LINE_NO = "PrimeLineNo";

	public static final String A_SUB_LINE_NO = "SubLineNo";

	public static final String DIRECT_ORDER = "DIRECT_ORDER";

	public static final String STOCK_ORDER = "STOCK_ORDER";

	public static final String A_BUYER_ORGANIZATION_CODE = "BuyerOrganizationCode";

	public static final String CREATE_ORDER_API = "createOrder";

	public static final String E_PERSON_INFO_SHIP_TO = "PersonInfoShipTo";

	public static final String E_PERSON_INFO_BILL_TO = "PersonInfoBillTo";

	public static final String E_PRICE_INFO = "PriceInfo";

	public static final String E_LINE_PRICE_INFO = "LinePriceInfo";

	public static final String A_TEMP_ORDER_SOURCE = "OrderSourcePlaceHolder";

	public static final String A_EXTN_ORDER_SOURCE = "ExtnOrderSource";

	public static final String CHAINED_FROM_ORDER_HEADER_KEY = "ChainedFromOrderHeaderKey";

	public static final String GET_ORDER_LINE_LIST_API = "getOrderLineList";

	public static final String CREATE = "CREATE";

	public static final String WEB_LINE_SEQUENCE = "SEQ_XPEDX_WEBLINE";

	public static final String CHAINED_FROM_ORDER_LINE_KEY = "ChainedFromOrderLineKey";

	public static final String CONSTANT_1 = "1";

	public static final String ORDERED_QTY_ZERO = "0";

	public static final String E_ORDER_STATUS_CHANGE = "OrderStatusChange";

	public static final String A_IGNORE_TRANS_DEPENDENCIES = "IgnoreTransactionDependencies";

	public static final String CHANGE_FULFILLMENT_TYPE = "CHANGE_FULFILLMENT_TYPE";

	public static final String A_MODIFICATION_REASON_CODE = "ModificationReasonCode";

	public static final String A_MODIFICATION_REASON_TEXT = "ModificationReasonText";

	public static final String REASON_TEXT = "Changed Fulfillment Type";

	public static final String A_TRANSACTION_ID = "TransactionId";

	public static final String UPDATE_LEGACY_TRANS_ID = "XPX_UPDT_LEGACY_QTY.0001.ex";

	public static final String CHANGE_ORDER_STATUS_API = "changeOrderStatus";

	public static final String A_BASE_DROP_STATUS = "BaseDropStatus";

	public static final String STATUS_CREATED_ID = "1100";

	public static final String A_CHANGE_FOR_ALL_AVAIL_QTY = "ChangeForAllAvailableQty";

	public static final String A_QUANTITY = "Quantity";

	public static final String STATUS_CANCELLED = "Cancelled";

	public static final String A_STATUS = "Status";

	public static final String A_PIPELINE_KEY = "PipeLineKey";

	public static final String E_SOURCE_INDICATOR = "SourceIndicator";

	public static final String E_ENVT_ID = "EnvironmentId";

	public static final String E_COMPANY = "Company";

	public static final String E_CUSTOMER_BRANCH = "CustomerBranch";

	public static final String E_CUSTOMER_NUMBER = "CustomerNumber";

	public static final String E_SHIP_TO_SUFFIX = "ShipToSuffix";

	public static final String E_ORDER_BRANCH = "OrderBranch";

	public static final String E_LINE_NUMBER ="LineNumber";

	public static final String E_LEGACY_PRODUCT_CODE = "LegacyProductCode";

	public static final String E_REQ_QTY_UOM = "RequestedQtyUOM";

	public static final String E_REQ_QTY = "RequestedQty";

	public static final String E_ITEMS = "Items";

	public static final Object ITEMS_ARRAY = "ITEMSARRAY";

	public static final String E_ORDER_HOLD_TYPES ="OrderHoldTypes";

	public static final String E_ORDER_HOLD_TYPE = "OrderHoldType";

	public static final String A_HOLD_TYPE = "HoldType";

	public static final String HOLD_CREATED_STATUS_ID = "1100";

	public static final String A_HAS_ERROR = "HasError";

	public static final String PAYMENT_APPROVAL_HOLD = "PAYMENT_APPROVAL";

	public static final String NEEDS_ATTENTION_HOLD = "NEEDS_ATTENTION";
	
	public static final String LEG_ERR_CODE_HOLD = "LEG_ERR_CODE_HOLD";

	public static final String A_EXTN_WILL_CALL_FLAG = "ExtnWillCall";

	public static final String CONSTANT_CHAR_P = "P";

	public static final String A_EXTN_WEB_HOLD_FLAG = "ExtnWebHoldFlag";

	public static final String A_BILL_TO_ID = "BillToID";

	public static final String E_CUSTOMER = "Customer";

	public static final String A_CUSTOMER_ID = "CustomerID";

	public static final String GET_CUSTOMER_LIST_API = "getCustomerList";

	public static final String A_EXTN_ORDER_UPDATE_FLAG = "ExtnOrderUpdateFlag";

	public static final String BOOLEAN_FLAG_N = "N";

	public static final String WEB_HOLD_FLAG_REASON_1 = "Will Call requested";
	
	public static final String WEB_HOLD_FLAG_REASON_2 = "New Customer Setup";

	public static final String A_EXTN_WEB_HOLD_REASON = "ExtnWebHoldReason";
	
	public static final String WEB_HOLD_FLAG_REASON_1_AND_2 = "Will Call requested & New Customer Setup";

	public static final String LEGACY_ORDER_CREATION_SERVICE = "XPXLegacyOrderCreationService";

	public static final String A_ENTRY_TYPE = "EntryType";

	public static final String A_CUSTOMER_PO_NO = "CustomerPONo";

	public static final String A_REQ_DELIVERY_DATE = "ReqDeliveryDate";

	public static final String A_SHIP_NODE = "ShipNode";

	public static final String A_SOURCE_TYPE = "SourceType";

	public static final String A_CUST_LINE_PO_NO = "CustomerLinePONo";

	public static final String A_CUSTOMER_ITEM = "CustomerItem";

	public static final String E_NOTES = "Notes";

	public static final String E_INSTRUCTIONS = "Instructions";

	public static final String CHAR_B = "B";

	public static final String CHAR_S = "S";

	public static final String E_BILL_TO_SUFFIX = "BillToSuffix";

	public static final String E_SUFFIX_TYPE = "SuffixType";

	public static final String E_CUSTOMER_DIVISION = "CustomerDivision";

	public static final String E_LEGACY_CUSTOMER_NO = "LegacyCustomerNumber";

	public static final String E_PARENT_CUSTOMER = "ParentCustomer";

	public static final String A_ORGANIZATION_CODE = "OrganizationCode";

	public static final String ALERT_CREATION_FOR_NO_PARENT_CUSTOMER_SERVICE = "XPXAlertCreationForNonExistentParentCustomerService";

	public static final String E_INBOX = "Inbox";

	public static final String CUSTOMER_BATCH_FEED_API = "XPXCustomerBatchProcess";

	public static final String A_API_NAME = "ApiName";

	public static final String A_DESCRIPTION = "Description";

	public static final String A_ERROR_REASON = "ErrorReason";

	public static final String A_EXCEPTION_TYPE = "ExceptionType";

	public static final String QUEUE_ID_DEFAULT = "DEFAULT";

	public static final String A_QUEUE_ID = "QueueId";

	public static final String E_INBOX_REFERENCES_LIST = "InboxReferencesList";

	public static final String E_INBOX_REFERENCES = "InboxReferences";

	public static final String A_NAME = "Name";

	public static final String A_SHIP_FROM_BRANCH = "ShipFromBranch";

	public static final String A_VALUE = "Value";

	public static final String A_REFERENCE_TYPE = "ReferenceType";

	public static final String CREATE_EXCEPTION_API = "createException";

	public static final String A_PROCESS_CODE = "ProcessCode";

	public static final String A_CUSTOMER_DIVISION = "CustomerDivision";

	public static final String A_LEGACY_CUSTOMER_NO = "LegacyCustomerNumber";

	public static final String A_SUFFIX_TYPE = "SuffixType";

	public static final String A_CUSTOMER_TYPE = "CustomerType";

	public static final String A_IGNORE_ORDERING = "IgnoreOrdering";

	public static final String A_SAP_PARENT_ACCOUNT_NO = "SAPParentAccountNumber";

	public static final String E_PRICE_LIST_HEADER_LIST = "PricelistHeaderList";

	public static final String A_TOT_NO_OF_RECORDS = "TotalNumberOfRecords";

	public static final String GET_PRICE_LIST_HEADER_LIST_API = "getPricelistHeaderList";

	public static final String E_PRICE_LIST_HEADER = "PricelistHeader";

	public static final String E_ENTITLEMENT_RULE_DETAILS_LIST = "EntitlementRuleDetailList";

	public static final String A_ENVIRONMENT_ID = "EnvironmentId";

	public static final String A_COMPANY_CODE = "CompanyCode";

	public static final String CUSTOMER_TYPE_BUSINESS = "01";

	public static final String A_OPERATION = "Operation";

	public static final String E_BUYER_ORGANIZATION = "BuyerOrganization";

	public static final String A_IS_BUYER = "IsBuyer";

	public static final String A_LOCALE_CODE = "LocaleCode";

	public static final String A_ORGANIZATION_NAME = "OrganizationName";

	public static final String A_PRIMARY_ENTERPRISE_KEY = "PrimaryEnterpriseKey";

	public static final String E_CUSTOMER_CURRENCY_LIST = "CustomerCurrencyList";

	public static final String A_RESET = "Reset";

	public static final String E_CUSTOMER_CURRENCY = "CustomerCurrency";

	public static final String A_CURRENCY = "Currency";

	public static final String A_IS_DEFAULT_CURRENCY = "IsDefaultCurrency";

	public static final String MANAGE_CUSTOMER_API = "manageCustomer";

	public static final String E_CUSTOMER_LIST = "CustomerList";

	public static final String A_SHIP_TO_ID = "ShipToID";

	public static final String A_EXTN_LEGACY_CUST_NO = "ExtnLegacyCustNumber";

	public static final String A_EXTN_CUSTOMER_NO = "ExtnCustomerNo";

	public static final String SOURCE_EDI = "EDI";

	public static final String SOURCE_TYPE_B2B = "B2B";

	public static final String SOURCE_WEB = "Web";

	public static final String SOURCE_TYPE_EXTERNAL = "External";

	public static final String SOURCE_COM = "Call Center";

	public static final String SOURCE_TYPE_INTERNAL = "Internal";

	public static final String A_EXTN_SOURCE_TYPE = "ExtnSourceType";

	public static final String E_OVERALL_TOTALS ="OverallTotals";

	public static final String A_LINE_SUB_TOTAL = "LineSubTotal";

	public static final String A_EXTN_TOTAL_ORDER_VALUE = "ExtnTotalOrderValue";

	public static final String E_LINE_OVERALL_TOTALS = "LineOverallTotals";

	public static final String A_EXTENDED_PRICE = "ExtendedPrice";

	public static final String A_EXTN_LINE_ORDERED_TOTAL = "ExtnLineOrderedTotal";

	public static final String ORDER_EXTN = "ORDER_EXTN";

	public static final String SHIP_TO_EXTN = "SHIP_TO_EXTN";

	public static final String BILL_TO_EXTN = "BILL_TO_EXTN";
	
	public static final String GET_ORDER_LIST_API = "getOrderList";

	public static final String RULES_ENGINE_SERVICE= "XPXGetOrderDetailsService";

	public static final String A_SHIP_TO_KEY = "ShipToKey";
	
	public static final String A_BILL_TO_KEY = "BillToKey";

	public static final String E_LINE_CHARGES = "LineCharges";

	public static final String E_AWARDS = "Awards";

	public static final String ORDER_LINE_EXTN = "ORDER_LINE_EXTN";

	public static final String ITEM_EXTN = "ITEM_EXTN";

	public static final String A_PAYMENT_STATUS = "PaymentStatus";

	public static final String A_EXTN_ENVT_ID = "ExtnEnvtId";

	public static final String A_EXTN_COMPANY_CODE = "ExtnCompanyId";

	public static final String E_XPX_ITEM_CUST_XREF = "XPXItemcustXref";

	public static final String A_ENVT_CODE = "EnvironmentCode";

	public static final String A_CUSTOMER_NO = "CustomerNumber";

	public static final String A_CUSTOMER_BRANCH = "CustomerBranch";

	public static final String A_LEGACY_ITEM_NO = "LegacyItemNumber";

	public static final String GET_XREF_LIST = "getXrefList";

	public static final String A_CUSTOMER_PART_NO = "CustomerPartNumber";

	public static final String A_LINE_PROCESS_CODE = "LineProcessCode";
	
	public static final String SEQ_XPEDX_UOMSEQ = "SEQ_XPEDX_UOMSEQ";

	public static final String SEND_CSR_EMAIL_SERVICE = "XPXSendCSREmailService";

	public static final String A_EXTN_DIVISION_EMAIL_ID = "ExtnDivisionEmailId";

	public static final String A_PARENT_ORGANIZATION_CODE = "ParentOrganizationCode";

	public static final String E_ORGANIZATION_LIST = "OrganizationList";

	public static final String E_ORGANIZATION = "Organization";

	public static final String GET_ORGANIZATION_LIST_API = "getOrganizationList";

	public static final String GET_XPX_ITEM_BRANCH_LIST_SERVICE = "getXPXItemBranchListService";

	public static final String E_XPX_ITEM_EXTN = "XPXItemExtn";

	public static final String A_INVENTORY_INDICATOR = "InventoryIndicator";

	public static final String STOCK = "STOCK";

	public static final String DIRECT = "DIRECT";

	public static final String A_XPX_DIVISION = "XPXDivision";

	public static final String A_SAP_PARENT_NAME = "ExtnSAPParentName";
	
	public static final String SERVICE_POST_LEGACY_ORDER_CREATE = "XPXPostToLegacyOrderCreateService";

	public static final String E_PRICE_BOOK = "PriceBook";

	public static final String A_CURRENCY_CODE = "CurrencyCode";

	public static final String A_WAREHOUSE = "Warehouse";

	public static final String A_PRICE_LIST_NAME = "PricelistName";

	public static final String A_PRICING_STATUS = "PricingStatus";

	public static final String ACTIVE = "ACTIVE";

	public static final String A_START_DATE_ACTIVE = "StartDateActive";

	public static final String A_END_DATE_ACTIVE = "EndDateActive";

	public static final String A_EXTN_PRICE_WAREHOUSE = "ExtnPricingWareHouse";

	public static final String A_EXTN_LEGACY_PROD_CODE = "ExtnLegacyProductCode";

	public static final String E_PRICE_LIST_LINE_LIST = "PricelistLineList";

	public static final String E_PRICE_LIST_LINE = "PricelistLine";

	public static final String MANAGE = "Manage";

	public static final String A_PRICING_UOM = "PricingUoM";

	public static final String E_PRICE_BRACKETS = "PriceBrackets";

	public static final String E_PRICE_BRACKET = "PriceBracket";

	public static final String UOM = "UOM";

	public static final String QTY = "Qty";

	public static final String PRICE = "Price";

	public static final String A_EXTN_PRICING_UOM = "ExtnPricingUom";

	public static final String A_EXTN_TIER_UOM = "ExtnTierUom";

	public static final String E_PRICE_LIST_LINE_QTY_TIER_LIST = "PricelistLineQuantityTierList";

	public static final String E_PRICE_LIST_LINE_QTY_TIER = "PricelistLineQuantityTier";

	public static final String A_FROM_QUANTITY = "FromQuantity";

	public static final String A_LIST_PRICE = "ListPrice";

	public static final String E_MULTI_API = "MultiApi";

	public static final String MANAGE_PRICE_LIST_HEADER_API = "managePricelistHeader";

	public static final String MANAGE_PRICE_LIST_LINE_API = "managePricelistLine";

	public static final String DELETE = "Delete";

	public static final String GET_PRICE_LIST_LINE_LIST_API = "getPricelistLineList";

	public static final String A_PRICE_LIST_LINE_KEY = "PricelistLineKey";

	public static final String A_PRICING_WAREHOUSE = "PricingWarehouse";

	public static final String E_PRICE_LIST_ASSIGNMENT = "PricelistAssignment";

	public static final String A_SHAREABLE = "Shareable";

	public static final String MANAGE_PRICE_LIST_ASSIGNMENT_API = "managePricelistAssignment";

	public static final String E_XPEDX_SALES_REP_LIST = "XPEDXSalesRepList";

	public static final String E_SALES_REPS = "SalesReps";
	
	public static final String E_SALES_REPS_SAP = "SalesRepsSAP";


	public static final String E_SALES_REP = "SalesRep";

	public static final String A_EMPLOYEE_ID = "EmployeeId";

	public static final String E_XPEDX_SALES_REP = "XPEDXSalesRep";

	public static final String A_SALES_REP_ID ="SalesRepId";

	public static final String A_NETWORK_ID = "NetworkID";

	public static final String A_PRIMARY_SALES_REP_FLAG = "PrimarySalesRepFlag";

	public static final String E_CUSTOMER_ASSIGNMENT = "CustomerAssignment";

	public static final String A_TEAM_CODE = "TeamCode";

	public static final String A_TEAM_NAME = "XPXSalesRep";

	public static final String A_USER_ID = "UserId";
	
	public static final String A_SALES_ID = "SalesId";

	public static final String MANAGE_CUSTOMER_ASSIGNMENT_API = "manageCustomerAssignment";

	public static final String E_EMPLOYEE_ID = "EmployeeId";

	public static final String E_TEAM = "Team";

	public static final String A_TEAM_ID = "TeamId";

	public static final String GET_TEAM_LIST_API = "getTeamList";

	public static final String A_TEAM_KEY = "TeamKey";

	public static final String E_API = "API";

	public static final String A_FLOW_NAME = "FlowName";

	public static final String GET_CUSTOMER_ASSIGNMENT_API = "getCustomerAssignmentList";

	public static final String A_CUSTOMER_ASSIGNMENT_KEY = "CustomerAssignmentKey";

	public static final String E_INPUT = "Input";

	public static final String MULTI_API = "multiApi";

	public static final String E_ENTITLEMENT_RULE_ASSIGNMENT = "EntitlementRuleAssignment";

	public static final String GET_ENTITLEMENT_ASSIGNMENT_LIST_API = "getEntitlementAssignmentList";

	public static final String A_PURPOSE = "Purpose";

	public static final String BUYING = "BUYING";

	public static final String E_ENTITLEMENT_RULE = "EntitlementRule";

	public static final String A_ENTITLEMENT_RULE_ID = "EntitlementRuleID";

	public static final String A_ENTITLEMENT_RULE_KEY = "EntitlementRuleKey";

	public static final String E_ENTITLEMENT_RULE_ASSIGNMENT_LIST = "EntitlementRuleAssignmentList";

	public static final String A_ENTITLEMENT_RULE_ASSIGNMENT_KEY = "EntitlementRuleAssignmentKey";

	public static final String MANAGE_ENTITLEMENT_RULE_API = "manageEntitlementRule";

	public static final String E_CORPROATE_PERSON_INFO = "CorporatePersonInfo";

	public static final String A_EMAIL_ID = "EMailID";

	public static final String E_UOM = "Uom";

	public static final String GET_UOM_LIST_API = "getUomList";

	public static final String A_UOM_DESCRIPTION = "UomDescription";

	public static final String E_USER = "User";
	
	public static final String E_SALES = "SalesRep";
	public static final String E_USER_SAP = "UserSAP";
	
	public static final String E_SALES_SAP = "SalesRepSAP";

	public static final String A_EXTN_EMPLOYEE_ID = "ExtnEmployeeId";

	public static final String GET_USER_LIST_API = "getUserList";

	public static final String A_LOGIN_ID = "Loginid";

	public static final String E_AWARD = "Award";

	public static final String A_AWARD_AMOUNT = "AwardAmount";

	public static final String A_EXTN_ADJUST_DOLLAR_AMOUNT = "ExtnAdjDollarAmt";

	public static final String E_TEMPLATE = "Template";

	public static final String A_EXTN_SAP_PARENT_NAME = "ExtnSAPParentName";

	public static final String E_OUTPUT = "Output";

	public static final String A_EXTN_SHIP_TO_NAME = "ExtnShipToName";

	public static final String A_EXTN_BILL_TO_NAME = "ExtnBillToName";

	public static final String A_EXTN_BILL_TO_SUFFIX = "ExtnBillToSuffix";

	public static final String A_EXTN_SHIP_TO_SUFFIX = "ExtnShipToSuffix";

	public static final String A_MASTER_PRODUCT_CODE = "MasterProductCode";

	public static final String A_STOCK_INDICATOR = "StockIndicator";

	public static final String A_EXTN_MASTER_PRODUCT_CODE = "ExtnMasterProductCode";

	public static final String A_EXTN_STOCK_INDICATOR = "ExtnStockIndicator";

	public static final String A_EXTN_SERVICE_DIVISION = "ExtnServiceDivision";

	public static final String A_EXTN_CUSTOMER_ORDER_BRANCH = "ExtnCustOrderBranch";

	public static final String A_EXTN_LINE_TYPE = "ExtnLineType";

	public static final String SPECIAL = "Special";

	public static final String SPECIAL_ORDER = "SPECIAL_ORDER";

	public static final String A_SAP_NUMBER = "SAPNumber";

	public static final String A_ASSIGNED_TO_USERID = "AssignedToUserId";

	public static final String ADMIN = "admin";

	public static final String A_EXTN_UNIT_PRICE_DISCOUNT = "ExtnUnitPriceDiscount";

	public static final String A_UNIT_PRICE = "UnitPrice";

	public static final String A_IS_PRICE_LOCKED = "IsPriceLocked";
	
	public static final String DB_SERVICE_GET_ARTICLE_DETAILS = "getXPXArticleDetailsService";
	
	public static final String A_ORDER_DATE = "OrderDate";

	public static final String A_DATA_SECURITY_GROUP = "DataSecurityGroupId";

	public static final String A_BRAND_CODE = "BrandCode";
	
	public static final String A_CUSTOMER_KEY = "CustomerKey";
	
	public static final String A_PARENT_CUSTOMER_KEY = "ParentCustomerKey";
	
	public static final String A_EXTN_SUFFIX_TYPE = "ExtnSuffixType";
	public static final String E_EXP = "Exp";
	public static final String E_Or = "Or";
	public static final String E_COMPLEX_QUERY = "ComplexQuery";
	
	public static final String SERVICE_XPX_CREATE_CUSTOMER_ORDER_FOR_INBOUND_B2B_OP_XML = "XPXCreateSterlingOrderService";
	
	public static final String A_EXTN_ENVIRONMENT_CODE = "ExtnEnvironmentCode";
	
	// Added to fix issue 926
	public static final String SYSTEM_SPECIFIER_WEB = "E";
	
	// Changes made on 15/02/2011
	public static final String MSAP_CUSTOMER_NAME = "MSAPCustomerName";
	public static final String MSAP_CUSTOMER_NUMBER = "MSAPCustomerNumber";
	// Changes made on 16/02/2011
	public static final String SOURCE_INDICATOR_B2B = "2";

	/** Added by Arun Sekhar 0n 14-March-2011 for XPXExternalUserProfileMigration **/
	//public static final String GET_USER_LIST_API = "getUserList";
	public static final String USER = "User";
	public static final String MODIFY_USER_HIERARCHY_API = "modifyUserHierarchy";
	public static final String CREATE_USER_HIERARCHY_API = "createUserHierarchy";
	public static final String USER_PROFILE_MIGRATION = "InternalUserProfileMigration";
	/*******************************************************************************/
	
	/** Added by Arun Sekhar 0n 14-April-2011 for User Update E-mail template **/
	public static final String USER_UPDATE_EMAIL = "XPXSendUserUpdateEmailService";
	/***************************************************************************/
	
	public static final String ORGANIZATION = "Organization";
	public static final String DIVISION_PROFILE_MIGRATION = "DivisionProfileMigration";
	
	// Changes made for Order update email templates.
	public static final String ORDER_SHIPPED_STATUS = "1100.5550";
	public static final String ORDER_BACKORDERED_STATUS = "1100.5100";
	public static final String ORDER_CANCELLED_STATUS = "9000";	
	public static final String ORDER_REJECTED_STATUS = "1100.5900";
	public static final String ORDER_INVOICED_STATUS = "1100.5700";
	public static final String ORDER_PLACED_STATUS = "1100.0100";

	public static final String MANAGE_ORGANIZATION_HIERARCHY = "manageOrganizationHierarchy";

	/** Added by Arun Sekhar 0n 25-April-2011 for Customer Profile Data MIgration **/
	public static final String RULE_DEFINITION= "XPXRuleDefn";
	public static final String CUSTOMER_PROFILE_MIGRATION = "CustomerProfileMigration";
	/********************************************************************************/
	
	/** Added by Amar 0n 09-May-2011  **/
	public static final String A_EXTN_ORDER_SUB_TOTAL= "ExtnOrderSubTotal";
	public static final String A_EXTN_ORDER_DISCOUNT = "ExtnOrderDiscount";
	public static final String A_EXTN_ORDER_COUPON_DISCOUNT= "ExtnOrderCouponDiscount";
	public static final String A_EXTN_TOT_ORDER_ADJUSTMENTS = "ExtnLegTotOrderAdjustments"; 
	public static final String A_EXTN_TOT_ORD_VAL_WITHOUT_TAXES= "ExtnTotOrdValWithoutTaxes";
	
	
	/********************************************************************************/
	//Begin: CR 2277
	public static final String A_VERTICAL = "Vertical";
	public static final String A_RELATIONSHIP_TYPE = "RelationshipType";
	public static final String A_MEMBERSHIP_LEVEL = "CustomerLevel";
	//End: CR 2277
	
	/*Begin - Changes made by Mitesh Parikh for JIRA 3002*/
	public static final String A_CUSTOMER_RECORD_TYPE="CustomerRecordType";
	public static final String A_USERS = "Users";
	public static final String A_SALES_REP = "SalesRep";
	public static final String A_USERS_SAP = "UsersSAP";
	public static final String A_SALES_REP_SAP = "SalesRepSAP";
	public static final String OLD_SAP_PARENT_ACCOUNT_NO = "OldSAPParentAccountNumber";
	public static final String OLD_SAP_PARENT_NAME = "OldSAPParentName";
	public static final String NEW_SAP_PARENT_ACCOUNT_NO = "NewSAPParentAccountNumber";
	public static final String NEW_SAP_PARENT_NAME = "NewSAPParentName";
	public static final String OLD_SAP_ACCOUNT_NO = "OldSAPAccountNumber";
	public static final String OLD_SAP_NAME = "OldSAPName";
	public static final String NEW_SAP_ACCOUNT_NO = "NewSAPAccountNumber";
	public static final String NEW_SAP_NAME = "NewSAPName";
	public static final String A_SELLER_ORGANIZATION_CODE="SellerOrganizationCode";
	/*End - Changes made by Mitesh Parikh for JIRA 3002*/
	public static final String SWC_ORDER_TRANS_TYPE = "Order";
	public static final String MSAP_SUFFIX_TYPE = "MC";
	
	public static final String HOLD_REASON_LEGACY_ERROR_CODE = "Header Error Code Or Line Error Code Received From Legacy.";
	public static final String NFE_M0000 = "M0000";
	public static final String NFE_M0020 = "M0020";
	public static final String NFE_M0036 = "M0036";
	public static final String NFE_M0041 = "M0041";
	
	public static final String PENDING_APPROVAL_HOLD = "ORDER_LIMIT_APPROVAL";
	public static final String PENDING_APPROVAL_ACTIVE_STATUS_ID = "1100";
	public static final String PENDING_APPROVAL_RELEASE_STATUS_ID = "1300";
	public static final String PENDING_APPROVAL_RELEASE_DESC="Released during Order Edit";
	
	public static final String HOLD_RELEASE_DESC="ReasonText";
	public static final String CUSTOMER_CONTACT_ID="CustomerContactID";
	public static final String RESOLVER_USER_ID="ResolverUserId";
}
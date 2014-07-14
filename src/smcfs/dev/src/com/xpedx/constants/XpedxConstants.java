package com.xpedx.constants;

//import com.labcorp.api.common.LCPropertyValueHelper;

/**
 * Interface Name : XpedxConstants
 *
 * @version 1.0
 */
public interface XpedxConstants {

	// Service Arguments - changes in EA branch
	public static final String LIST_API_SERVICE = "LIST_API_SERVICE";
	public static final String UPDATE_SERVICE = "UPDATE_SERVICE";
	public static final String INSERT_SERVICE = "INSERT_SERVICE";
	public static final String PK_NAME = "PK_NAME";
	public static final String LABCORP_KEYS = "LABCORP_KEYS";
	public static final String KEY_GETDOC_ENV_KEY = "labcorp.sts.getdoc.env.key";
	public static final String TAG_INTERNAL_ENVELOPE = "EnvironmentObjectEnvelope";
	public static final String TAG_MERGE_ROOT_DOC = "LabCorpMergedDocument";
	public static final String TAG_MERGE_INPUT_DOC = "InputDocument";
	public static final String KEY_MERGEDOC_ENV_KEY = "labcorp.sts.mergedoc.env.key";
	public static final String TAG_MERGE_ENVIRONMENT_DOC = "EnvironmentDocument";
	public static final String KEY_SETDOC_ENV_KEY = "labcorp.sts.setdoc.env.key";
	public static final String DELETE_SERVICE_NAME = "DELETE_SERVICE_NAME";
	public static final String CREATE_SERVICE_NAME = "CREATE_SERVICE_NAME";
	public static final String LIST_SERVICE_NAME = "LIST_SERVICE_NAME";
	public static final String UNIQUE_KEY = "UNIQUE_KEY";
	public static final String STERLING_KEY = "STERLING_KEY";
	public static final String CHILD_ELEMENT = "CHILD_ELEMENT";
	public static final String ID_ELEMENT = "ID_ELEMENT";
	public static final String CHANGE_SERVICE_NAME = "CHANGE_SERVICE_NAME";
	public static final String TABLE_PRIMARY_KEY = "TABLE_PRIMARY_KEY";
	public static final String DEFAULT_STATUS = "Draft";
	//public static final String GET_LCLOC_PERFLAB_VIEW_LIST = "GetLCLocPerfLabViewList";
	// Exception Code and Messages
	public static final String EXTN001 = "EXTN001";
	public static final String EXTN002 = "EXTN002";
	public static final String EXTN001_ERROR_DESCRIPTION = " API argument name is missing!!!";
	public static final String EXTN002_ERROR_DESCRIPTION = " API argument value is missing!!!";
	public static final String DC_ERR_CODE_COUNT_EXCEEDS_LIMIT = "ERR_CODE_COUNT_EXCEEDS_LIMIT";
	public static final String DC_ERR_MSG_COUNT_EXCEEDS_LIMIT = "The number of records returned are {0}. This exceeds the allowable limit of {1} records. Please refine your search.";
	public static final String DC_ERR_CODE_NO_RESULTS_FOUND = "ERR_CODE_NO_RESULTS_FOUND";
	public static final String DC_ERR_MSG_NO_RESULTS_FOUND = "No valid diagnosis codes were found that match the search criteria entered. Please modify your search.";
	public static final String SPEC_LINE_WARN_INVALID_DATE = "Invalid Format Entered, Setting current Date and Time";
	public static final String TC_ERR_CODE_NO_RESULTS_FOUND = "ERR_CODE_NO_RESULTS_FOUND";
	public static final String TC_ERR_MSG_NO_RESULTS_FOUND = "No records were found matching your search criteria. Please modify your search criteria and try again.";
	public static final String DC_ERR_CODE_INVALID_DIAGNOSIS_CODE = "ERR_CODE_INVALID_DIAGNOSIS_CODE";

	// XML Attributes
	public static final String TOTAL_NO_OF_RECORDS = "TotalNumberOfRecords";
	public static final String CATEGORY_EXIST = "CategoryExist";
	public static final String ITEM_ID = "ItemID";
	public static final String ENTERPRISE_CODE = "EnterpriseCode";
	public static final String PRICELIST_HEADER = "PricelistHeader";
	public static final String PRICELIST_NAME = "PricelistName";
	public static final String PRICELIST_LINE = "PricelistLine";
	public static final String ORGANIZATION_CODE = "OrganizationCode";
	public static final String CALLING_ORGANIZATION_CODE = "CallingOrganizationCode";
	public static final String ADDNL_ATTR_LST = "AdditionalAttributeList";
	public static final String ADDNL_ATTR = "AdditionalAttribute";
	public static final String SPEC_LINE_ATTR = "SpecimenLineKey";
	public static final String SPEC_LINE_ATTR_VALUE = "@SpecimenLineKey";
	public static final String QUANTITY = "Quantity";
	public static final String QUANTITY_VALUE = "@Quantity";
	public static final String PROC_INSTR = "ProcessingInstruction";
	public static final String PROC_INSTR_VALUE = "@ProcessingInstruction";
	public static final String HEADER_INSTR_VALUE = "@HeaderSpecialInstruction";
	public static final String COL_DATE_TIME = "CollectionDateTime";
	public static final String COL_DATE_TIME_VALUE = "@CollectionDateTime";
	public static final String ATTR_LOCATION_ID = "LocationID";
	public static final String ATTR_TEST_CATEGORY = "TestCategory";
	public static final String ATTR_PREFERRED_ACCOUNT = "PreferredAccount";
	public static final String ATTR_VERTICAL = "Vertical";
	public static final String ATTR_CUSTOMER_ID = "CustomerID";
	public static final String ATTR_OPERATOR = "Operator";
	public static final String ATTR_STATUS = "Status";
	public static final String ITEM_KEY = "ItemKey";
	public static final String ATTR_COMPLEXQUERY_VALUE = "Value";
	public static final String ATTR_COMPLEXQUERY_NAME = "Name";
	public static final String ATTR_LAST_NAME = "LastName";
	public static final String ATTR_FIRST_NAME = "FirstName";
	public static final String ATTR_PARTY_ID = "PartyID";
	public static final String ATTR_TOTAL_NUMBER_OF_PAGES = "TotalNumberOfPages";
	public static final String ATTR_PAGE_NUMBER = "PageNumber";
	public static final String ATTR_IS_VALID_PAGE = "IsValidPage";
	public static final String ATTR_IS_FIRST_PAGE = "IsFirstPage";
	public static final String ATTR_IS_LAST_PAGE = "IsLastPage";
	public static final String ATTR_ERROR_DESCRIPTION = "@ErrorDescription";
	public static final String ATTR_ERROR_CODE = "@ErrorCode";
	public static final String ATTR_ORDER_NAME = "OrderName";
	public static final String ATTR_ORDER_NXT_ITER_DATE = "NextIterationDate";
	public static final String ATTR_ORDER_NXT_ITER_SEQ_NO = "NextIterationSeqNo";
	public static final String ATTR_ORDER_EXTN_PHYS_ID = "ExtnPhysicianId";
	public static final String ATTR_ORDER_EXTN_SO_FREQUENCY = "ExtnStandingOrderFrequency";
	public static final String ATTR_ORDER_NXT_SO_FREQ_INTERVAL = "ExtnStandingOrderFrequencyInterval";
	public static final String ATTR_TOTAL_ORDER_LIST = "TotalOrderList";
	public static final String ATTR_EXTN_PATIENT_NICKNAME = "ExtnPatientNickname";
	public static final String ATTR_EXTN_PATIENT_BIRTHDAY = "ExtnPatientBirthday";
	public static final String ATTR_EXTN_PATIENT_M_R_N = "ExtnPatientMRN";
	public static final String ATTR_EXTN_PATIENT_ETHNICITY = "ExtnPatientEthnicity";
	public static final String ATTR_EXTN_PATIENT_RACE = "ExtnPatientRace";
	public static final String ATTR_EXTN_PATIENT_GENDER = "ExtnPatientGender";
	public static final String ATTR_EXTN_PATIENT_ALTERNATE_PHONE1 = "ExtnPatientAlternatePhone1";
	public static final String ATTR_EXTN_PATIENT_EMAIL = "ExtnPatientEmail";
	public static final String ATTR_EXTN_PATIENT_PHONE_NUMBER = "ExtnPatientPhoneNumber";
	public static final String ATTR_EXTN_PATIENT_ZIP_CODE = "ExtnPatientZipCode";
	public static final String ATTR_EXTN_PATIENT_STATE_CODE = "ExtnPatientStateCode";
	public static final String ATTR_EXTN_PATIENT_CITY = "ExtnPatientCity";
	public static final String ATTR_EXTN_PATIENT_ADDRESS_LINE2 = "ExtnPatientStreetAddressLine2";
	public static final String ATTR_EXTN_PATIENT_ADDRESS_LINE1 = "ExtnPatientStreetAddressLine1";
	public static final String ATTR_EXTN_PATIENT_MIDDLE_INITIAL = "ExtnPatientMiddleInitial";
	public static final String ATTR_EXTN_PATIENT_LAST_NAME = "ExtnPatientLastName";
	public static final String ATTR_EXTN_PATIENT_FIRST_NAME = "ExtnPatientFirstName";
	public static final String ATTR_EXTN_PATIENT_VISIT_DATE = "ExtnPatientVisitDate";
	public static final String ATTR_EXTN_RP_RELATIONSHIP = "ExtnResponsiblePartyRelationship";
	public static final String ATTR_EXTN_RP_PHONE_NUMBER = "ExtnResponsiblePartyPhoneNumber";
	public static final String ATTR_EXTN_RP_ZIP_CODE = "ExtnResponsiblePartyZipCode";
	public static final String ATTR_EXTN_RP_STATE_CODE = "ExtnResponsiblePartyStateCode";
	public static final String ATTR_EXTN_RP_CITY = "ExtnResponsiblePartyCity";
	public static final String ATTR_EXTN_RP_ADDRESS_LINE2 = "ExtnResponsiblePartyStreetAddressLine2";
	public static final String ATTR_EXTN_RP_ADDRESS_LINE1 = "ExtnResponsiblePartyStreetAddressLine1";
	public static final String ATTR_EXTN_RP_MIDDLE_INITIAL = "ExtnResponsiblePartyMiddleInitial";
	public static final String ATTR_EXTN_RP_LAST_NAME = "ExtnResponsiblePartyLastName";
	public static final String ATTR_EXTN_RP_FIRST_NAME = "ExtnResponsiblePartyFirstName";
	public static final String ATTR_PARENT_CUSTOMER_KEY = "ParentCustomerKey";
	public static final String ATTR_CUSTOMER_KEY = "CustomerKey";
	public static final String ATTR_EXTN_STANDING_ORDER_END_DATE = "ExtnStandingOrderEndDate";
	public static final String ATTR_OVERRIDE = "Override";
	public static final String ATTR_EXTN_LAST_EXECUTION_DATE = "ExtnLastExecutionDate";
	public static final String ATTR_MASTER_ORDER_HEADER_KEY = "MasterOrderHeaderKey";

	public static final String PRICELIST_LINE_KEY = "PricelistLineKey";
	public static final String PRICELIST_LINE_LIST = "PricelistLineList";
	public static final String LIST_PRICE = "ListPrice";
	public static final String PRICING_STATUS = "PricingStatus";
	public static final String ACTIVE = "ACTIVE";
	public static final String UNIT_OF_MEASURE = "UnitOfMeasure";
	public static final String CURRENCY = "Currency";
	public static final String USD = "USD";
	public static final String DESCRIPTION = "Description";
	public static final String END_DATE_ACTIVE = "EndDateActive";
	public static final String START_DATE_ACTIVE = "StartDateActive";

	// Date format attributes
	public static final String DEFAULT_DATETIME_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String MANIFEST_BATCH_DATETIME_FORMAT_MDYYYYHMM = "MdyyyyHmm";
	public static final String DEFAULT_TIMESTAMP_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	public static final String UI_DATE_FORMAT_MMDDYYYY = "MM/dd/yyyy";

	// String constants
	public static final String ID = "Id";
	public static final String SRCH_FOR_DESCRIPTION = "FullDesc";
	public static final String SRCH_FOR_FULL_DESCRIPTION = "FullDesc";
	public static final String SRCH_FOR_SHORT_DESCRIPTION = "ShortDesc";
	public static final String SRCH_FOR_LONG_DESCRIPTION = "LongDesc";
	public static final String SRCH_FOR_CODE = "Code";
	public static final String SRCH_FOR_ALL = "all";
	public static final String MAX_DIAG_CODE_COUNT_ATTRIBUTE_NAME = "MAX_DIAG_CODE_COUNT";
	public static final String DFLT_MAX_DIAG_CODE_COUNT = "10";
	public static final String YES = "Y";
	public static final String NO = "N";
	public static final String OPERATION = "Operation";
	public static final String DELETE = "Delete";
	public static final String PHYSICIAN_ID_SESSION_ATTR = "PHYSICIAN_ID";
	public static final String PATIENT_DTLS_SESSION_ATTR = "PATIENT_DETAILS";
	public static final String ORDER_STATUS_SESSION_ATTR = "ORDER_STATUS";
	public static final String EXTN_STANDING_ORDER_FLAG_SESSION_ATTR = "STANDING_ORDER_FLAG";
	public static final String IS_NEW_ORDER_SESSION_ATTR = "IS_NEW_ORDER";
	public static final String IS_SAVED_SO_CHILD_SESSION_ATTR = "IS_SAVED_SO_CHILD";
	public static final String IS_CHILD_SO = "IS_SO_CHILD";
	public static final String ORDER_NO_SESSION_ATTR = "ORDER_NO";
	public static final String REPORT_BYTE_ARRAY_EXISTS_SESSION_ATTR = "REPORT_BYTE_ARRAY_EXISTS";
	public static final String ORDER_HEADER_KEY_SESSION_ATTR = "ORDER_HEADER_KEY";
	public static final String STANDING_ORDER_STATUS = "STD_ORDER_STATUS";
	public static final String STDORD_STATUS = "Status";
	public static final String SO_END_DATE_SESSION_ATTR = "SO_END_DATE";
	public static final String NEWLINE = System.getProperty("line.separator");
	public static final String ORDER_HDR_KEY = "OrderHeaderKey";
	public static final String API_SUCCESS = "ApiSuccess";
	public static final int CACHE_TIME_IN_MINUTES = 10;
	public static final int MAX_LENGTH_FOR_SPECIAL_INSTRUCTIONS = 255;
	public static final String JSON = "json";
	public static final String MAP_KEY_PREFERRED_TEST_CATEGORIES = "PreferredTestCategories";
	public static final String MAP_KEY_PREFERRED_ITEMS = "PreferredItems";
	public static final String MAP_KEY_CACHE_TIMESTAMP = "CacheTimestamp";
	public static final Long DEFAULT_PREFERENCE_CACHE_LIFETIME = new Long(24);
	public static final String OPERATOR_AND = "AND";
	public static final String STATUS_VALUE_ACTIVE = "10";
	public static final String ACTIVE_STANDING_ORDER_FLAG = "IS_STANDING_ORDER_ACTIVE";
	public static final String STANDING_ORDER_IS_MANAGE_FLAG = "IsManage";
	public static final String ZERO = "0";
	public static final String SPACE_STRING = " ";
	public static final String ADDRESS_SEPARATOR_STRING = ", ";
	public static final String EMPTY_STRING = "";
	public static final String DELIMETER_COMMA = ",";
	public static final String IS_ORDER_FROM_MASTER_ORDER = "OrderFromMasterOrder";
	public static final String STR_MASTER_ORDER_DETAILS = "MasterOrderDetails";
	public static final String PSC_LINE_TYPE = "PSC";
	public static final String ALR_LINE_TYPE = "ALR";
	public static final String LINE_TYPE = "LineType";
	public static final String EXTN_QUESTION_SET_ID = "ExtnQuestionSetId";
	public static final String EXTN_CATALOG_KEY ="ExtnCatalogKey";

	// Customer overrides property key names
	public static final String PROP_KEY_ENTERPRISE_CODE = "labcorp.enterpriseCode";
	public static final String PROP_KEY_BUYER_ORG = "labcorp.buyerOrganizationCode";
	public static final String PROP_KEY_SELLER_ORG = "labcorp.SellerOrganizationCode";
	public static final String PROP_KEY_PAT_DOB_FORMAT = "labcorp.patDOBFormat";
	public static final String PROP_KEY_PREFERENCE_CACHE_LIFE = "labcorp.TestPreferenceCacheLife";
	public static final String PROP_PAGINATION_PAGE_SIZE = "PAGINATION_PAGE_SIZE";

	// Service Name
	public static final String Request_To_Be_Cancelled="Request To Be Cancelled";
	public static final String GET_ITEM_LIST_DATA_LOAD = "GetItemListDataLoad";
	public static final String DROP_DIAGNOSIS_CODE_IN_Q = "DropDiagnosisCodeInQueue";
	public static final String GET_PRICE_LIST_LINE_LIST_SERVICE = "GetPricelistLineListService";
	public static final String DELETE_PRICE_LIST_LINE_LIST_SERVICE = "DeletePricelistLineListService";
	public static final String GET_ITEM_LIST_ADDNL_ATTRS = "GetItemListAdditionalAttributes";
	//public static final String SERVICE_DELETE_DIAG_LINES = "deleteLCDiagnosisLinesService";
	//public static final String SERVICE_DELETE_DIAG_LINE_DB = "deleteLCDiagnosisLineDBService";
	//public static final String SERVICE_SEARCH_DIAG_CODES = "getLCDiagnosisCodeListService";
	//public static final String SERVICE_SEARCH_DIAG_CODES_DB = "getLCDiagnosisCodeListDBService";
	//public static final String SERVICE_UPDATE_DIAG_LINES = "updateLCDiagnosisLinesService";
	//public static final String SERVICE_UPDATE_DIAG_LINE_DB = "updateLCDiagnosisLineDBService";
	public static final String SERVICE_ADD_TESTCODES = "AddTestCodesService";
	public static final String SERVICE_SPECIMEN_INSTRUCTIONS = "GetSpecimenInstructionsService";
	public static final String SERVICE_UPDATE_SPEC_LINE_DB = "UpdateSpecimenLineListDBService";
	public static final String API_CHANGE_ORDER = "changeOrder";
	public static final String API_GET_ORDER_LIST = "getOrderList";
	public static final String SERVICE_MANAGE_REQ = "ManageRequisitionService";
	public static final String API_GET_ORDER_LINE_LIST = "getOrderLineList";
	public static final String API_CHECK_FOR_EIXTSING_CATEGORY_ITEM = "CheckForExistingCategoryItem";
	public static final String API_MANAGE_DATA_LOAD = "LabCorpManageDataLoad";
	public static final String API_MANAGE_DELETE_AND_RECREATE = "ManageDeleteAndRecreate";
	public static final String API_MANAGE_PRICE_LIST_LINE = "ManagePriceListLine";
	public static final String API_PARSE_DIAGNOSIS_CODE = "ParseDiagnosisCodeList";
	public static final String API_UPDATE_ADDNL_ATTR = "UpdateAdditionalAttribute";
	public static final String API_UPDATE_SPEC_LINE = "UpdateSpecimenLineApi";
	public static final String SERVICE_COLLECTION_RESPONSE = "GetCollectionResponseDBService";
	public static final String SERVICE_INSERT_SPECIMEN_LINE = "insertIntoSpecimenLineService";
	//public static final String SERVICE_GET_SPECIMEN_LINE_LIST = "getLCSpecimenLineListService";
	public static final String SERVICE_GENERATE_BARCODE = "GenerateBarcodeService";
	public static final String API_CONFIRM_DRAFT_ORDER = "confirmDraftOrder";
	public static final String SERVICE_UPDATE_REQUISITION = "UpdateRequisitionService";
	public static final String API_GET_PHYSICIAN_DETAILS = "SelectPhysicianDetails";
	public static final String SERVICE_GET_PREFERENCE_LIST = "getPreferenceListDBService";
	public static final String API_GETCUSTOMERLIST_COMPLEX = "getCustomerListForCustomerIDs";
	public static final String API_UPDATE_MANAGE_AND_DELETE_UTIL = "UpdateManageAndDeleteUtil";
	public static final String SERVICE_EXTENDED_DB_WRAPPER = "XpedxExtendedDb";
	public static final String SERVICE_CREATE_ORDER_FROM_STANDING_ORDER = "CreateOrderFromStandingOrder";
	public static final String SERVICE_COPY_ORDER_TO_CREATE_STANDING_ORDER = "CreateStandingOrderFromOrder";
	public static final String API_CREATE_ORDER_FROM_MASTER_ORDER = "createOrderFromMasterOrder";
	public static final String API_PRICE_LIST_ASSGNMNT_BUYER_ORG = "PriceListAssgnmntToBuyerOrg";
	public static final String SERVICE_GET_CUSTOMER_LIST = "GetCustomerListService";
	public static final String SERVICE_PRICE_LIST_ASSIGNMENT = "PriceListAssignmentService";
	public static final String SERVICE_GET_CHILD_CUSTS = "GetChildCustsService";
	public static final String API_FIND_ACCNTS_ASSIGN_TO_ORG = "FindAccntsOfLocAndAssignToOrg";

	public static final String SERVICE_QUESTION_LIST = "getQuestionList";

	public static final String API_DISASOCIATE_PRICELISTS_PARENT_ORG = "DisasociatePriceListIfParentOrgsAreDiff";
	public static final String API_DELETE_RECORDS_UTIL = "DeleteRecordsUtilAPI";
	public static final String API_FIND_ACCNTS_N_ASSIGN_PRICELISTS = "FindAccountsAndAssignPricelistsAPI";

	public static final String API_DISASOCIATE_PRICELIST_PARENT_LOCS = "DisasociatePriceListIfParentLocsAreDiff";
	public static final String API_GET_PRICELIST_ASSIGNMENT_LIST = "getPricelistAssignmentList";

	public static final String SERVICE_GET_PROCEDURE_GROUPS = "getProcedureGroups";
	public static final String SERVICE_PEFFORMING_LAB_BY_LOCATION_DB = "GetPerformingLabCodeByLocationDBService";
	//public static final String LC_SESSION_ORDER_WRAPPER_SERVICE = "LCSessionOrderWrapper";

	public static final String SERVICE_DATALOAD_FILES_STORAGE_LIST = "GetDataLoadFilesStorageList";
	public static final String SERVICE_CHANGE_DATALOAD_FILES_STORAGE = "ChangeDataLoadFilesStorageService";
	public static final String SERVICE_CREATE_DATALOAD_FILES_STORAGE = "CreateDataLoadFilesStorageService";
	public static final String SERVICE_MANAGE_PRICELIST_LINE_LIST = "ManagePricelistLineListService";
	public static final String SERVICE_DELETE_LOC_ACCT_PREF = "DeleteLocAcctPrefService";
	public static final String SERVICE_CREATE_LOC_ACCNT_PREF = "CreateLocAccntPrefService";
	public static final String SERVICE_GET_LOC_ACCNT_PREF_LIST = "GetLocAccntPrefListService";
	public static final String SERVICE_CHANGE_LOC_ACCNT_PREF = "ChangeLocAccntPrefService";
	public static final String SERVICE_MANAGE_DIAGNOSIS_CODE_LOAD = "ManageDiagnosisCodeLoadService";

	// Element Name
	public static final String ITEM = "Item";
	public static final String CATEGORY_LIST = "CategoryList";
	public static final String CATEGORY = "Category";
	//public static final String LC_DIAGNOSIS_CODE = "LCDiagnosisCode";
	//public static final String LC_SPECIMEN_LINE = "LCSpecimenLine";
	public static final String ELEMENT_CUSTOMER = "Customer";
	//public static final String PREF_DOCUMENT = "LCLocAcctPref";
	public static final String ELEMENT_EXP = "Exp";
	public static final String ELEMENT_COMPLEX_QUERY = "ComplexQuery";
	public static final String ELEMENT_OR = "Or";
	public static final String ELEMENT_AND = "And";
	public static final String ELEMENT_ORDERLINE = "OrderLine";
	public static final String ELEMENT_ORDERLINES = "OrderLines";
	public static final String ELEMENT_EXTN = "Extn";
	public static final String ELEMENT_ORDER = "Order";
	public static final String ELEMENT_ORDERLIST = "OrderList";
	public static final String ELEMENT_OUTPUT = "Output";
	public static final String ATTRIBUTE_QRY_TYPE = "QryType";
	public static final String PARENT_CUSTOMER_ID = "ParentCustomerID";
	public static final String ACCOUNT_ID = "AccountID";
	public static final String LOCATION_ID = "LocationID";
	public static final String CUSTOMER_TYPE = "CustomerType";
	//public static final String ATTR_EXTN_LC_NODE_TYPE = "ExtnLCNodeType";
	public static final String NODE_TYPE_LOC = "LOC";
	public static final String PRICE_LIST_ASSIGNMENT = "PricelistAssignment";
	public static final String PARENT_CUSTOMER = "ParentCustomer";

	//public static final String DATALOAD_FILES_STORAGE = "LCDataloadFilesStorage";
	public static final String PRIMARY_KEY_VALUE = "PrimaryKeyValue";
	public static final String LOAD_NAME = "LoadName";
	public static final String SERVICE_NAME = "ServiceName";
	public static final String RELOAD_REQUIRED = "ReloadRequired";
	public static final String PROCESSED = "Processed";
	public static final String DATA_LOAD_FILE = "DataLoadFile";
	public static final String DIAGNOSIS_LOAD = "DiagnosisLoad";
	public static final String PRIMARY_KEY = "PrimaryKey";
	public static final String EXTN_STALE_FLAG = "ExtnStaleFlag";
	public static final String STALE_FLAG = "StaleFlag";
	public static final String PREFERENCE_TYPE = "PreferenceType";
	public static final String PREFERRED_ACCOUNT = "PreferredAccount";
	public static final String MAX_RECORDS = "MaximumRecords";

	public static final String MANAGE_REQUISITION_SERVICE = "ManaageRequisitionService";
	public static final String GET_ORDER_DETAILS_API = "getOrderDetails";
	public static final String GET_ORDERLINE_LIST_API = "getOrderLineList";
	public static final String GET_CUSTOMER_LIST_API = "getCustomerList";
	public static final String SELECT_REQ_LIST_DBSERVICE = "SelectRequisitionListDBService";
	public static final String DELETE_REQ_DBSERVICE = "DeleteRequisitionDBService";
	public static final String INSERT_REQ_DBSERVICE = "InsertRequisitionDBService";

	public static final String ORDERLINE_INPUT_TEMPLATE = "<OrderLine><Order OrderHeaderKey=''/><OrderBy><Extn><Attribute Name='ExtnReqKey' /></Extn></OrderBy></OrderLine>";
	// mashups
//	public static final String LC_UPDATE_SPEC_LINE_MASHUP = "labcorpUpdateSpecimenLineMashUp";
//	public static final String LC_GET_ACCU_DRAW_INSTR_MASHUP = "labcorpGetAccuDrawInstructionsMashup";
//	public static final String LC_SPEC_LINE_INFO_MASHUP = "labcorpSpecimenLineInfoMashUp";
	public static final String LABCORP_REFRESH_DIAGNOSIS_CODES_MASH_UP = "labcorpRefreshDiagnosisWrapperMashUp";

	public static final String TEMPLATE_ORDERLINE_INPUT = "<OrderLine><Order OrderHeaderKey=''/><OrderBy><Extn><Attribute Name='ExtnReqKey' /></Extn></OrderBy></OrderLine>";

	// UI Section names (Should match with the portlet names from
	// LCPortalDefinition.xml)
	public static final String PATIENT_SECTION = "Patient";
	public static final String DIAGNOSISCODES_SECTION = "Diagnosis";
	public static final String TESTCODES_SECTION = "Test";
	public static final String SPECIMEN_SECTION = "Specimen";

	public static final String LINE_SEPARATOR = System
	.getProperty("line.separator");

	// XPath
	//public static final String XPATH_LCSPEC_LINE = "/LCSpecimenLineList/LCSpecimenLine";
	public static final String XPATH_ERRORS_ERROR = "/Errors/Error";
	public static final String ELEM_PAGE = "/Page";
	public static final String PAGE_OUTPUT_XPATH = "/Page/Output";
	//public static final String LC_LOC_X_PHYS_VIEW = "//LCLocXPhysView/LCPhysician";
	public static final String XPATH_CUSTLIST_CUSTID = "/CustomerList/Customer/ParentCustomer/@CustomerID";
	public static final String XPATH_ORDHDR_CATALOGKEY = "/Order/Extn/@ExtnCatalogKey";
	public static final String XPATH_ORDHDR_PAYORCODE = "/Order/Extn/@ExtnPayorID";
	public static final String XPATH_ORDHDR_PATIENT_VISITDATE = "/Order/Extn/@ExtnPatientVisitDate";
	public static final String XPATH_ORDHDR_PARENT_MO_KEY = "/Order/Extn/@ExtnParentMasterOrderKey";
	public static final String XPATH_ORDHDR_CLASSIFICATION_CODE="/Order/Extn/@ExtnClassificationCode";

	// PL Level
	public static final String PICK_LIST_LEVEL_ORGANIZATION = "Organization";
	public static final String PICK_LIST_LEVEL_USER = "User";

	// Tab constants
	public static final String TAB_SECURITY_SETTINGS = "SecuritySettings";
	public static final String TAB_GENERAL_SETTINGS = "GeneralSettings";
	public static final String TAB_NOTE_LIBRARY = "NoteLibrary";
	public static final String TAB_QUICK_ORDER = "QuickOrder";
	public static final String TAB_PICK_LIST_LIBRARY = "PickList";
	public static final String TAB_PRINTER_URL_LIBRARY = "PrintSettings";

	// session constants
	public static final String SELECTED_QUICKORDER_LOCATION_NAME_SESSION_ATTR = "SessionLocationName";
	public static final String QUICK_ORDER_NAME_SESSION_ATTR = "SesionQuickOrderName";
	public static final String QUICK_ORDER_CREATE_TIME_SESSION_ATTR = "SessionQuickOrderCreateTime";
	public static final String SELECTED_QUICKORDER_LOCATION_ID_SESSION_ATTR = "SessionLocationID";
	public static final String NAVIGATION_GUIDE_GROUPS = "GroupList";

	// IsQuick Order
	public static final String IS_QUICK_ORDER = "Y";
	public static final String IS_QUICK_ORDER_SHARED_TO_ORG = "Y";
	public static final String IS_QUICK_ORDER_NOT_SHARED_TO_ORG = "N";

	// Misc
	public static final String LABCORP = "LABCORP";
	public static final String STRING_01 = "01";
	public static final String COL_SPECIAL_INSTRUCTION = "C";
	public static final String PROCESSING_INSTRUCTION = "P";
	public static final String HEADER_SPECIAL_INSTRUCTION = "H";
	public static final String INDIGENT_TESTING="INDIGENT";
	// ABN Constants
	public static final String OPER_GET_ABN_DETAIL = "getABNDetail";
	public static final String OPER_BROWSE_ABN_DETAIL = "browseABNDetail";
	public static final String OPER_UPDATE_ABN_DETAIL = "updateABNDetail";

	public static final String ABN_DETAIL_SERVICE_MASHUP = "abnDetailMashUp";
	public static final String XPATH_CHECK_ABN_RESPONSE = "/CheckABNResponse";
	public static final String CHECK_ABN_RETURN = "CheckABNReturn";

	public static final String SHIP_QUANTITY_ATTRIBUTE = "ShipQty";
	public static final String SHIP_QUANTITY_VALUE = "@ShipQty";

	// Requisition Types
	public static final String REQUISITION_REGULAR = "REG";
	public static final String REQUISITION_PSC = "PSC";
	public static final String REQUISITION_ALR = "ALR";
	public static final String ACTION_CREATE = "CREATE";
	public static final String ACTION_REGENERATE = "REGENERATE";

	public static final String PROCEDURE_TYPE_PAP = "PAP";
	public static final String PROCEDURE_TYPE_PAN = "PAN";
	//public static final String TXN_ID_MANIFESTED = "LC_COS_MANIFEST.0001.ex";

	//public static final String LC_REQUISITON_KEY = "ReqKey";
	public static final Integer MINIMUM_TEST_CODE_COUNT = 2;

	public static final String POST_MANIFEST_ACCUDRAW_CHECK_SERVICE = "PostManifestAccudrawService";
	public static final String POST_MANIFEST_SERVICE = "PostManifestService";

	public static final String ACCUDRAW_SPEICMEN_NOT_AVAILABLE_ERROR_CODE = "ItemIDSpecimentNotMatchError";
	public static final String ACCUDRAW_USAGE_FLAG_ERROR_CODE = "ItemIDsAccudrawUsageError";
	public static final String ITEM_IDS_PROC_TYPE_ERROR_CODE = "ItemIDProcTypeError";
	public static final String REQUISTION_NOT_FOUND_ERRROR = "ItemIDRequistionError";
	public static final String ACCUDRAW_ADD_ON_ERROR_MESSAGE = "The test(s) being added require a new specimen to be submitted. "
		+ "Please initiate a new order, with an 'OK' option to continue";
	public static final String ACCUDRAW_ERROR = "ItemIDAccudrawError";

	public static final String IS_POST_MANIFEST_FLOW = "IsPostManifestFlow";
	public static final String SESSION_CURRENT_FLOW_NAME = "SessionCurrentFlowName";

	public static final String POST_MANIFEST_MANAGE_ADD_ON_EXPIRED = "PostManifestIsExpired";
	public static final int POST_MANIFEST_NO_DAYS_CHECK = 7;
	public static final String POST_MANIFEST_STATUS_MANIFESTED = "Manifested";
	public static final String MANIFEST_STATUS_CODE = "1100.200";
	public static final String READY_TO_MANIFEST_STATUS_CODE = "1100.100";
	public static final String MANIFEST_STATUS_DESCRIPTION = "Manifested";
	public static final String STATUS_DESC_READY_TO_MANIFEST = "Ready to Manifest";
	//public static final String MANIFEST_TRANSACTION_ID = "LC_COS_MANIFEST.0001.ex";
	public static final String STANDING_ORDER_ON_HOLD_STATUS = "StandingOrderOnHold";
	public static final String STANDING_ORDER_CANCELLED_STATUS = "StandingOrderCancelled";
	public static final String STANDING_ORDER_COMPLETED_STATUS = "StandingOrderCompleted";
	public static final String STANDING_ORDER_ON_HOLD_STATUS_CODE = "1100.902";
	public static final String STANDING_ORDER_CANCELLED_STATUS_CODE = "1100.901";
	public static final String STANDING_ORDER_COMPLETED_STATUS_CODE = "1100.903";
	public static final String STANDING_ORDER_STATUS_CHANGE_TXN_ID = "ChangeStandOrderStatus.0001.ex";
	public static final String TXN_ID_REVERT_MANIFEST = "ReverseManifestTrans.0001.ex";
	public static final String TXN_ID_REVERT_PSC = "ReversePSCTransaction.0001.ex";
	public static final String STATUS_SENT_TO_PSC = "1100.120";
	public static final String STATUS_DESC_SENT_TO_PSC = "Sent to PSC";
	public static final String STATUS_READY_FOR_PSC = "1100.10";
	public static final String STATUS_DESC_READY_FOR_PSC = "Ready for PSC";
	public static final String TXN_ID_DROP_TO_PSC_STATUS = "ScheduledPSC.0001.ex";

	public static final String ORDER_TRACKING_SELECTED_TAB_STANDING = "Standing";
//	public static final String IS_BEACON_REQUIRED = LCPropertyValueHelper
//	.getPropertyValue("labcorp.IS_BEACON_REQUIRED");
//	public static final String beaconHostName = LCPropertyValueHelper
//	.getPropertyValue("BEACON_HOST_NAME");
	public static final String IS_BEACON_REQUIRED = "true";
	public static final String beaconHostName = "localhost";
	// bugilla #9831
	public static final String ORDER_STATUS_DRAFT_SAVE = "Saved Order";
	public static final String RESPONSE_ERROR_FRAGEMENT = "RespErrorFragment";

	// ABN related properties from customeroverrides.property file
	public static final String ACCOUNT_NO_FOR_ABN = "labcorp.accountNumberForABN";
	public static final String SOURCE_NAME_FOR_ABN = "labcorp.sourcecNameForABN";
	public static final String BILLINGS_FOR_ABN = "labcorp.billingsForABN";
	public static final String SOURCE_VERSION_FOR_ABN = "labcorp.sourceVersionForABN";
	public static final String ABN_END_POINT_URL = "labcorp.ABN_END_POINT";
	public static final String ABN_UTILITY_END_POINT_URL = "labcorp.ABN_UTILITY_END_POINT";
	public static final String USE_REAL_ACCNT_FOR_ABN = "labcorp.useRealAccountForABN_Flag";
	public static final String ABN_DETAILS_GRID = "ABNDetailsGrid";
	public static final String ABN_ERROR_CODES = "ERR0005,ERR0001,ERR0002,ERR0004,ERR0014,ERR0021,ERR0015,ERR0031,ERROR0000";
	public static final String ABN_SERVICE_UNAVILABLE_CODE = "ERROR0000";
	public static final String ABN_SERVICE_UNAVILABLE_DESC = "An ABN check cannot be completed at this time. For ABN determination, refer to your Medicare Medical Necessity booklet.";

	public static final String REQUISITIONS_AVAILABLE = "Requisitions";
	public static final String ACCUDRAW_SERVICE_DONE = "Accudrawdone";

	public static final String BARCODE_BILL_TYPE_CM = "Account Billing";
	public static final String BARCODE_BILL_TYPE_PI = "Third Party Insurance";
	public static final String BARCODE_BILL_TYPE_IN = "Indigent Billing";
	public static final String BARCODE_BILL_TYPE_MC = "Medicare";
	public static final String BARCODE_BILL_TYPE_MD = "Medicaid";
	public static final String BARCODE_BILL_TYPE_HMO = "HMO";
	public static final String BARCODE_BILL_TYPE_CS = "Cash Sales";
	public static final String BARCODE_BILL_TYPE_PT = "Patient Billing";
	// ALR constants
	public static final String ALR_EC_EXCEPTION = "ERR_CODE_EXCEPTION";
	public static final String ALR_ED_EXCEPTION = "Error description not available";
	public static final String ALR_EC_INVALID_BLANK_INPUT = "ALR_INVALID_BLANK_INPUT";
	public static final String ALR_EC_INVALID_NULL_INPUT = "ALR_INVALID_NULL_INPUT";
	public static final String ALR_ED_INVALID_BLANK_INPUT = "One or more of the input fields are blank";
	public static final String ALR_ED_INVALID_NULL_INPUT = "One or more of the input fields are null";
	public static final String ALR_EC_QUESTIONSETID_NULL_OR_BLANK = "ALR_QUESTIONSETID_NULL_OR_BLANK";
	public static final String ALR_ED_QUESTIONSETID_NULL_OR_BLANK = "Question set id is either null or blank";
	public static final String GET_ITEM_LIST_API = "getItemList";
	public static final String MANAGE_ALR_TEST_CODES_API = "ManageALRTestCodesLoadService";
	public static final String ALR_EC_NO_CAT_RECORD_FOUND = "ALR_EC_NO_CATLOG_RECORD_FOUND";
	//public static final String ALR_ED_NO_CAT_RECORD_FOUND = "No record found in LC ALR CATALOG table for input catalog key";
	public static final String ALR_EC_CAT_RECORD_INSERTION_FAILED = "ALR_EC_CAT_RECORD_INSERTION_FAILED";
	//public static final String ALR_ED_CAT_RECORD_INSERTION_FAILED = "Record insertion in LC ALR CATALOG and child table failed";
	public static final String ALR_EC_CLONED_CATALOG_KEY_NOT_PRESENT = "ALR_EC_CLONED_CATALOG_KEY_NOT_PRESENT";
	//public static final String ALR_ED_CLONED_CATALOG_KEY_NOT_PRESENT = "Cloned catalog key from LC ALR CATALOG table is not available";
	public static final String ALR_EC_PAYCODE_CATALOG_UNIQUE = "ErrorUniqueExceptionId";
	public static final String ALR_ED_PAYCODE_CATALOG_UNIQUE = "The specified payor code is already assigned to a System catalog";
	public static final String ALR_EC_PAYCODE_ORGID_UNIQUE = "ErrorUniqueExceptionId";
	public static final String ALR_ED_PAYCODE_ORGID_UNIQUE = "The specified payor code is already assigned to an Organization";
//	public static final String CREATE_LC_ALR_PAYORCODE_INFO_ID = "createLCAlrPayorCodeInfoId";
//	public static final String GET_LC_ALR_PAYORCODE_INFO_ID = "getLCAlrPayorCodeInfoId";
//	public static final String GET_LC_ALR_AOE_QUES_GRPLIST_ID1="getLCAlrAoeQuestionGroupListId1";
//	public static final String GET_LC_ALR_AOE_QUES_GRPLIST_MULTIPLE="getLCAlrAoeQuestionGroupListByMultipleGroups";
//	public static final String CREATE_LC_ALR_AOE_QUESTION_ID = "createLCAlrAoeQuestionId";
//	public static final String GET_LC_ALR_AOE_QUES_LIST="getLCAlrAoeQuestionList";
//	public static final String GET_LC_ALR_AOE_QUES_LIST_MULTIPLE="getLCAlrAoeQuestionListByMultipleQuestionID";
//	public static final String CREATE_LC_ALR_AOE_QUESTION_GROUP_ID = "createLCAlrAoeQuestionGroupId";
//	public static final String CHANGE_LC_ALR_PAYOR_CODE_INFO_ID = "changeLCAlrPayorCodeInfoId";
	public static final String ALR_EC_NO_RECORD_EXISTS = "ALR_NO_RECORD_EXISTS";
	public static final String ALR_ED_NO_RECORD_EXISTS = "No record exists for the specified primary key";
	public static final String ALR_ED_NO_SUCH_RECORD ="No Record exists for the specified input";
	public static final String ALR_EC_CATKEY_TESTCODE_UNIQ="ALR_ED_CATKEY_TESTCODE_UNIQ";
	public static final String ALR_ED_CATKEY_TESTCODE_UNIQ="The specified test code is already assigned to a Catalog";
	public static final String ALR_EC_ACCOUNTKEY_NULL_OR_BLANK = "ALR_ACCOUNTKEY_NULL_OR_BLANK"; // added
	// by
	// vkaradik
	public static final String ALR_ED_ACCOUNTKEY_NULL_OR_BLANK = "AccountKey is either null or blank"; // added
	// by
	// vkaradik
	public static final String ALR_EC_ACCOUNTKEY_ASSOCIATED_WITH_LOCATIONID = "ALR_ACCOUNTKEY_ASSOCIATED_WITH_LOCATIONID"; // added
	// by
	// vkaradik
	public static final String ALR_ED_ACCOUNTKEY_ASSOCIATED_WITH_LOCATIONID = "AccountKey is associated with LocationId"; // added
	// by
	// vkaradik
	public static final String ALR_EC_CATALOGKEY_NOT_PRESENT = "ALR_EC_CATALOGKEY_NOT_PRESENT";
	public static final String ALR_ED_CATALOGKEY_NOT_PRESENT = "The specified CatalogKey was not found";

	public static final String ALR_EC_DUPLICATE_TC_DRIVEN_CATALOG = "ALR_EC_DUPLICATE_TC_DRIVEN_CATALOG"; // added by swetha
	public static final String ALR_ED_DUPLICATE_TC_DRIVEN_CATALOG = "Cannot create a duplicate test code driven catalog for the same organization"; // added by swetha
	//ALR BY TEST
	public static final String DRIVEN_BY_TESTCODE = "T";
	//ALR BY PAYOR
	public static final String DRIVEN_BY_PAYOR = "P";
	// POST MANIFEST SERVICE
	public static final String POST_MANIFEST_CONFIG_SERVICE_NAME = "PostManifestService";

	public static final String PROP_POST_MANIFEST_NO_DAYS_CHECK = "labcorp.postmanifestnodayscheck";
	public static final String API_DELETE_ORDER = "deleteOrder";
	public static final String EXTN_POST_MANIFESTED_STATE = "ExtnPostManifestedState";
	// represent the new test code addition state
	public static final String EXTN_POST_MANIFESTED_STATE_NEW_ADDITION = "New";
//	public static final String ELEMENT_LC_REQUISITION_LIST = "LCRequisitionList";
//	public static final String ELEMENT_LC_REQUISITION = "LCRequisition";
	public static final String ATT_ORDER_LINE_KEY = "OrderLineKey";
	public static final String ATT_REQ_KEY = "ReqKey";
	public static final String ATT_REQ_TYPE = "ReqType";
	public static final String ATT_REQ_NO = "ReqNo";
	public static final String ATT_VENDOR = "Vendor";
	public static final String ATT_VERSION = "Version";
	public static final String ATT_PROCESSING_TYPE = "ProcessingType";
	public static final String ATT_ORDERED_QTY = "OrderedQty";
	public static final String ATT_EXTN_TEST_NUMBER = "ExtnTestNumber";

	//public static final String LC_POST_MANIFEST_WOODSTOCK_SERVICE = "PostManifestWoodstockService";
	public static final String ACTION_WOODSTOCK_CHG = "CHANGE";
	public static final String GET_ORDER_LIST_INPUT_FOR_WOODSTOCK_POST_MANIFEST = "getWoodStockPostManifestOrderList.xml";
	public static final String GET_ORDER_LIST = "getOrderList";
	public static final String POST_MANIFEST_TEST_CODE_ADD_ON = "PostManifestTestCodeAddition";
	public static final String POST_MANIFEST_TEST_CODE_CANCEL = "PostManifestTestCodeCancellation";
	// for getLocationDetails function defined in PostManifestWoodStockAPI
	//public static final String LC_LOC_XPERF_LAB_VIEW = "LCLocXPerfLabView";
	public static final String YFS_ORDER_HEADER = "YFSOrderHeader";
//	public static final String LC_PERF_LAB = "LCPerfLab";
	public static final String PERF_LAB_CODE = "PerformingLabCode";
	public static final String PERF_LAB_NAME = "PerformingLabName";
	public static final String DIVISION_NAME = "DivisionName";
	public static final String FAX_NUMBER = "FaxNumber";
	public static final String CONTACT_EMAIL = "ContactEmail";
	public static final String EXTN = "Extn";
	public static final String EXTN_LOCATION_ID = "ExtnLocationID";
//	public static final String LC_LOC_XPERF_LAB = "LCLocXperfLab";
	public static final String LOCATION_ID_ = "LocationID";

	public static final String GET_WOODSTOCK_REQUISITION_SERVICE = "getWoodstockRequisition";
	public static final String SKIP = "skip";
	public static final String MANIFESTED_DATE_TIME_ERROR_CODE = "ManifestedDateTimeError";
	public static final String MANIFESTED_DATE_TIME_ERROR = "ManifestedDateTimeError";
	public static final String MANIFESTED_TIME_TO_CHECK = "yfs.labcorp.ManifestedTimeToCheck";



	// Order tracking related properties from customeroverrides.property file
//	public static final String SHOW_ALR_TAB = LCPropertyValueHelper
//	.getPropertyValue("OrderTracking.ShowALRTab", "N");
//	public static final String SHOW_ALR_TAB = LCPropertyValueHelper
//	.getPropertyValue("OrderTracking.ShowALRTab", "N");
	public static final String SHOW_ALR_TAB =  "N";
	public static final long DEFAULT_PURGE_AFTER_NO_OF_DAYS = 50;
	//public static final String XPATH_REQ_LINE = "/LCRequisitionLineList/LCRequisitionLine";
	public static final String PAGE_NOT_EXIST ="ErrorOnRequestedPage";
	public static final String PAGE_NOT_EXIST_DESC ="The Requested Page Does not Exist";
	public static final String MANIFEST_ERROR_MSG = "Your orders could not be manifested at this time, Please try Again";

	/*** Start of Code For JIra 3552***/
	public static final String CUST_SKU_FLAG_FOR_CUSTOMER_ITEM = "1";
	public static final String CUST_SKU_FLAG_FOR_MANUFACTURER_ITEM = "2";
	public static final String CUST_SKU_FLAG_FOR_MPC_ITEM = "3";
	/***End of  CODE For Jira 3552***/

	/***Start of Code for Jira 3838 ****/
	public static final String SalesRep_Password_Policy="XPXSalesRepPwdPolicy";
	/***End of Code for Jira 3838***/
	

}


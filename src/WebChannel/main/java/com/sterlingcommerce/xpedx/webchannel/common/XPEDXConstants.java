package com.sterlingcommerce.xpedx.webchannel.common;

import com.yantra.yfs.core.YFSSystem;


/**
 * XPEDXConstants class will hold all common constants used internally by 
 * XPEDX application. 
 *
 */
public class XPEDXConstants {
	
	// Types for B2B catalog view
	public static final int XPEDX_B2B_FULL_VIEW = 0;
	public static final int XPEDX_B2B_CONDENCED_VIEW = 1;
	public static final int XPEDX_B2B_MINI_VIEW = 2;
	public static final int XPEDX_B2B_PAPER_GRID_VIEW = 3;
	
	// CallerPage Type
	public static final String XPEDX_CALLER_HOME_PAGE="HomePage";
	public static final String XPEDX_CALLER_CATALOG_PAGE="Catalog";
	public static final String XPEDX_CALLER_CATLANDING_PAGE="CatLanding";
	public static final String XPEDX_CALLER_SIGNIN_PAGE="SignInPage";
	
	// promo page URL, SharedPath
	public static final String REQUEST_ATTR_PROMO_PAGE_URL = "PROMO_PAGE_URL";
	public static final String DYN_PROMO_SHARED_PATH_PROP = "promotions_shared_path";
	public static final String AD_JUGGLER_KEYWORD_PREFIX_PROP = "xpedx.adjuggler.keyword.attribute.prefix";
	
	public static final String CONTENT_LOCATION_CODE = "CONTENT_LOCATION";
	public static final String IMAGE_SERVER_LOCATION = "IMAGE_SERVER_LOCATION";
	public static final String IMAGE_SERVER = "IMAGE_SERVER";
	public static final String CONTENT_SERVER_FSC_LOCATION = "CONTENT_SERVER_FSC_LOCATION";
	public static final String CONTENT_SERVER_FSC = "CONTENT_SERVER_FSC";
	public static final String CONTENT_SERVER_PEFC_LOCATION = "CONTENT_SERVER_PEFC_LOCATION";
	public static final String CONTENT_SERVER_PEFC = "CONTENT_SERVER_PEFC";
	public static final String CONTENT_SERVER_SFI_LOCATION = "CONTENT_SERVER_SFI_LOCATION";
	public static final String CONTENT_SERVER_SFI = "CONTENT_SERVER_SFI";
	public static final String CONTENT_SERVER_LOCATION = "CONTENT_SERVER_LOCATION";
	public static final String CONTENT_SERVER = "CONTENT_SERVER";
	public static final String CONTENT_SERVER_MSDS_LOCATION = "CONTENT_SERVER_MSDS_LOCATION";
	public static final String CONTENT_SERVER_MSDS = "CONTENT_SERVER_MSDS";
	public static final String USER_PREF_CATEGORY = "USER_PREF_CATEGORY";
	public static final String DEFAULT_USER_PREF_CATEGORY = "4000000"; /*Added for JIRA 1969*/
	public static final String CUST_PREF_CATEGORY = "CUST_PREF_CATEGORY";
	public static final String CUST_PREF_CATEGORY_DESC = "CUST_PREF_CATEGORY_DESC";
	public static final String SHIP_FROM_BRANCH = "SHIP_FROM_BRANCH";
	public static final String INDUSTRY = "INDUSTRY";
	public static final String CUSTOMER_CURRENCY_CODE="CUSTOMER_CURRENCY_CODE";
	public static final String XPEDX_Customer_Contact_Info_Bean = "XPEDX_Customer_Contact_Info_Bean";
	public static final String CUSTOMER_DIVISION="CUSTOMER_DIVISION";
	public static final String BILL_TO_CUSTOMER="BILL_TO_CUSTOMER";
	public static final String CUSTOMER_USE_SKU="customerUseSKU";
	public static final String ENVIRONMENT_CODE="ENVIRONMENT_CODE";
	public static final String COMPANY_CODE="COMPANY_CODE";
	public static final String LEGACY_CUST_NUMBER="LEGACY_CUST_NUMBER";
	public static final String MSDS_URL_ASSET_ID="MSDS";
	public static final String MSDS_URL_DISPLAY="MSDS";
	public static final String MSDS_ASSET_TYPE_URL="URL";
	public static final String MSDS_ASSET_TYPE_DATA_SHEET="ITEM_DATA_SHEET";
	public static final String IS_ESTIMATOR = "isEstimator";
	public static final String IS_DEFAULT_CART_SET = "isDefaultCartSet";
	
	public static final String XPEDX_ITEM_LABEL = " Item #";
	public static final String CUSTOMER_ITEM_LABEL = "My Item #"; /* <!-- -FXD-4 --> */
	public static final String MANUFACTURER_ITEM_LABEL = "Mfg. Item #";
	public static final String MPC_ITEM_LABEL = "MPC Code";
	public static final String DEFAULT_SELECT_SHIP_TO_LABEL = "- Select Ship-To -";
	public static final String DEFAULT_SELECT_ORDER_CRITERIA_LABEL = "- Select Order Criteria -";
	public static final String DEFAULT_SELECT_SEARCH_CRITERIA_LABEL = "- Select Search Criteria -";
	//Added for JIRA 2770
	public static final String DEFAULT_SELECT_SEARCH_SHIPTO_CRITERIA_LABEL1 = "All Ship-Tos";
	public static final String DEFAULT_SELECT_SEARCH_SHIPTO_CRITERIA_LABEL2 = "- Select a Ship-To -";	
	
	public static final String CUST_SKU_FLAG_FOR_CUSTOMER_ITEM = "1";
	public static final String CUST_SKU_FLAG_FOR_MANUFACTURER_ITEM = "2";
	public static final String CUST_SKU_FLAG_FOR_MPC_ITEM = "3";
	
	public static final String SHIP_COMPLETE_Y = "Y";
	public static final String SHIP_COMPLETE_C = "C";
	public static final String SHIP_COMPLETE_N = "N";
	
	public static final String XPX_CUSTCONTACT_EXTN_ADDLN_EMAIL_ATTR = "AddnlEmailAddrs";
	public static final String XPX_CUSTCONTACT_EXTN_PO_LIST_ATTR = "POList";
	public static final String XPX_CUSTCONTACT_EXTN_REF_ATTR = "CustContRefKey";
	public static final String XPX_CUSTCONTACT_EXTN_TC_FLAG_ATTR = "AcceptTAndCFlag";
	public static final String XPX_CUSTCONTACT_EXTN_TC_ACCEPTED_ON_ATTR = "TAndCAcceptedOn";
	public static final String XPX_CUSTCONTACT_EXTN_LAST_LOGIN_DATE = "LastLoginDate";
	
	public static final String DEFAULT_SHIP_TO_CHANGED = "DEFAULT_SHIP_TO_CHANGED";
	public static final String MASHUP_SHOW_LOCATIONS = "xpedx-customer-showLocations";
	
	public static String MASTER_CUSTOMER_SUFFIX_TYPE;
	public static String SAP_CUSTOMER_SUFFIX_TYPE;
	public static String BILL_TO_CUSTOMER_SUFFIX_TYPE;
	public static String SHIP_TO_CUSTOMER_SUFFIX_TYPE;
	
	public static String USER_CUSTOMER_NAME;
	
	public static String XPEDX_STORE_FRONT = "xpedx";
	public static String SAALFELD_STORE_FRONT = "Saalfeld";
	public static String STRAITEGIC_PPR_STORE_FRONT = "StrategicPaper";
	public static String BULKLEY_DUNTON_STORE_FRONT = "BulkleyDunton";
	public static String CANADA_STORE_FRONT = "xpedxCanada"; 

	
	public static String EDITED_ORDER_HEADER_KEY="Edited_OrderHeaderKey";
	
	public static String HOLD_TYPE_FOR_PENDING_APPROVAL = "ORDER_LIMIT_APPROVAL";
	public static String HOLD_TYPE_FOR_NEEDS_ATTENTION = "NEEDS_ATTENTION";
	public static String HOLD_TYPE_FOR_LEGACY_CNCL_ORD_HOLD = "LEGACY_CNCL_ORD_HOLD";
	public static String HOLD_TYPE_FOR_ORDER_EXCEPTION_HOLD = "ORDER_EXCEPTION_HOLD";
	public static String REQUIRE_CUSTOMER_PO_NO_RULE="RequireCustomerPO";
	
	// Added for JIRA 1998
	public static final String ENTRY_TYPE_EMAIL_UPDATE = "EmailChange";
	public static final String SHIP_TO_CUSTOMER = "shipToCustomer";
	public static final String CHANGE_SHIP_TO_IN_TO_CONTEXT = "changeShipToContext";
	public static final int MAX_ELEMENTS_IN_MINICART=5;
	static{
		
		MASTER_CUSTOMER_SUFFIX_TYPE = YFSSystem.getProperty("xpedx.master.customer.suffix");
		if(MASTER_CUSTOMER_SUFFIX_TYPE == null)
			MASTER_CUSTOMER_SUFFIX_TYPE = "MC";
		
		SAP_CUSTOMER_SUFFIX_TYPE = YFSSystem.getProperty("xpedx.sap.customer.suffix");
		if(SAP_CUSTOMER_SUFFIX_TYPE == null)
			SAP_CUSTOMER_SUFFIX_TYPE = "C";
		
		BILL_TO_CUSTOMER_SUFFIX_TYPE = YFSSystem.getProperty("xpedx.billto.customer.suffix");
		if(BILL_TO_CUSTOMER_SUFFIX_TYPE == null)
			BILL_TO_CUSTOMER_SUFFIX_TYPE = "B";
		
		SHIP_TO_CUSTOMER_SUFFIX_TYPE = YFSSystem.getProperty("xpedx.shipto.customer.suffix");
		if(SHIP_TO_CUSTOMER_SUFFIX_TYPE == null)
			SHIP_TO_CUSTOMER_SUFFIX_TYPE = "S";
	}
}

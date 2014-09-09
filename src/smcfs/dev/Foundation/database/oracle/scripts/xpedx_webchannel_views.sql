CREATE OR REPLACE FORCE VIEW "XPEDX_ORDER_LIST_VIEW" ("ORDERED_BY_NAME", "LAST_MODIFYTS", "ORDER_NAME", "ORDER_HEADER_KEY", "ORDER_NO", "DRAFT_ORDER_FLAG", "DOCUMENT_TYPE", "AUTHORIZED_CLIENT", "BUYER_ORGANIZATION_CODE", "EXTN_WEB_CONF_NUM", "EXTN_ORDER_DESCRIPTION", "CUSTOMER_CONTACT_ID", "BILL_TO_ID", "ENTERPRISE_KEY", "TOTAL_NUMBER_OF_ITEM")
AS
 SELECT
    CASE
    
      WHEN yus.username = ' '
      OR yus.username   = ''
      OR yus.username  IS NULL     
      THEN yus1.username
       WHEN OHR.MODIFYUSERID = 'guest_xpedx' then 
      YUS2.username
      ELSE yus.username
    END          AS ORDERED_BY_NAME,
    OHR.MODIFYTS AS LAST_MODIFYTS,
    OHR.ORDER_NAME ,
    OHR.ORDER_HEADER_KEY ,
    OHR.ORDER_NO,
    OHR.DRAFT_ORDER_FLAG,
    OHR.DOCUMENT_TYPE ,
    OHR.AUTHORIZED_CLIENT,
    OHR.BUYER_ORGANIZATION_CODE ,
    OHR.EXTN_WEB_CONF_NUM,
    OHR.EXTN_ORDER_DESCRIPTION AS EXTN_ORDER_DESCRIPTION,
    OHR.CUSTOMER_CONTACT_ID,
    OHR.BILL_TO_ID,
    trim(OHR.ENTERPRISE_KEY)  AS ENTERPRISE_KEY,
    COUNT(YOR.ORDER_LINE_KEY) AS TOTAL_NUMBER_OF_ITEM
  FROM YFS_ORDER_HEADER OHR
  LEFT JOIN YFS_ORDER_LINE YOR
  ON YOR.ORDER_HEADER_KEY=OHR.ORDER_HEADER_KEY
  LEFT JOIN YFS_USER YUS
  ON (OHR.MODIFYUSERID = TRIM(YUS.LOGINID)) 
  LEFT JOIN YFS_USER YUS1
  ON (yus1.extn_employee_id = SUBSTR(OHR.MODIFYUSERID, 0, INSTR(OHR.MODIFYUSERID, '@')-1))
  LEFT JOIN YFS_USER YUS2
  ON (TRIM(OHR.CREATEUSERID) = TRIM(YUS2.LOGINID))
  GROUP BY OHR.ORDER_HEADER_KEY,
    yus.username,
    yus1.username ,
    OHR.MODIFYTS,
    OHR.ORDER_NAME,
    OHR.ORDER_NO,
    OHR.DRAFT_ORDER_FLAG,
    OHR.DOCUMENT_TYPE,
    OHR.AUTHORIZED_CLIENT,
    OHR.BUYER_ORGANIZATION_CODE,
    OHR.EXTN_WEB_CONF_NUM,
    OHR.EXTN_ORDER_DESCRIPTION,
    OHR.CUSTOMER_CONTACT_ID,
    OHR.BILL_TO_ID,
    trim(OHR.ENTERPRISE_KEY),
    OHR.MODIFYUSERID,
    YUS2.username;

CREATE OR REPLACE VIEW XPEDX_SALES_REP_CUSTOMERS 
AS 
SELECT DISTINCT 
  yc2.customer_id AS SALES_RP_CUST_KEY,
  yc2.customer_id AS CUSTOMER_ID,
  sr.msap_customer_number AS MSAP_NO,
  sr.msap_customer_name AS MSAP_NAME,
  yo.organization_name AS MSAP_ORGANIZATION_NAME,
  yc2.organization_code AS MSAP_ORGANIZATION_CODE,
  yu.loginid AS USER_ID,
  p.first_name AS SR_FIRST_NAME,
  p.last_name AS SR_LAST_NAME 
FROM xpedx_sales_rep sr
JOIN yfs_user yu ON (trim(yu.loginid) = sr.network_id)
JOIN yfs_data_security_group ydsg ON (yu.data_security_group_id = ydsg.data_security_group_id)
JOIN yfs_customer_assignment yca ON (yca.team_key = ydsg.data_security_group_key)
JOIN yfs_customer yc1 ON (yca.customer_key = yc1.customer_key)
JOIN yfs_customer yc2 ON (yc2.customer_key = CAST(yc1.root_customer_key AS CHAR(50)))
JOIN yfs_organization yo ON (yc2.customer_id = yo.ORGANIZATION_CODE)
LEFT OUTER JOIN yfs_person_info p ON (yu.contactaddress_key = p.person_info_key)
AND (yc1.root_customer_key IS NOT NULL)
AND (sr.sales_customer_key = yc1.customer_key);

CREATE OR REPLACE VIEW XPX_CUST_HIERARCHY_VIEW AS 
SELECT 
ShipTo.Customer_Id AS SHIP_TO_CUSTOMER_ID,
ShipTo.EXTN_SHIP_FROM_BRANCH   AS EXTN_SHIP_FROM_BRANCH,
CAST (TRIM(ShipTo.EXTN_SHIP_FROM_BRANCH)||'_'||TRIM(ShipTo.EXTN_ENVIRONMENT_CODE) AS CHAR(50)) AS SHIP_TO_DIVISION_ID,
ShipTo.Extn_Customer_Name AS SHIP_TO_CUSTOMER_NAME, 
ShipTo.CUSTOMER_KEY AS SHIP_TO_CUSTOMER_KEY, 
ShipTo.ORGANIZATION_CODE AS ENTERPRISE_CODE, 
ShipTo.Extn_Cust_Store_No AS SHIP_TO_EXTN_CUST_STORE_NO, 
ShipTo.EXTN_SAP_PARENT_ACC_NO AS SHIP_TO_SAP_PARENT_ACC_NO, 
ShipTo.EXTN_SAP_NUMBER AS SHIP_TO_SAP_NUMBER,
ShipTo.STATUS AS STATUS,
PersonInfo.FIRST_NAME AS FIRST_NAME, 
PersonInfo.LAST_NAME AS LAST_NAME, 
PersonInfo.ADDRESS_LINE1 AS SHIP_TO_ADDR_LINE_1, 
PersonInfo.ADDRESS_LINE2 AS SHIP_TO_ADDR_LINE_2, 
PersonInfo.ADDRESS_LINE3 AS SHIP_TO_ADDR_LINE_3, 
PersonInfo.CITY AS SHIP_TO_CITY, 
PersonInfo.STATE AS SHIP_TO_STATE, 
PersonInfo.ZIP_CODE AS SHIP_TO_ZIP_CODE, 
PersonInfo.EXTN_ZIP4 AS EXTN_ZIP4, 
PersonInfo.COUNTRY AS SHIP_TO_COUNTRY, 
PersonInfo.EMAILID AS EMAILID, 
BillTo.Customer_Id AS BILL_TO_CUSTOMER_ID, 
BillTo.Extn_Customer_Name AS BILL_TO_CUSTOMER_NAME, 
BillTo.CUSTOMER_KEY AS BILL_TO_CUSTOMER_KEY, 
BillTo.EXTN_SAP_PARENT_ACC_NO AS BILL_TO_PARENT_ACC_NO, 
BillTo.EXTN_SAP_NUMBER AS BILL_TO_SAP_NUMBER, 
SAP.Customer_Id AS SAP_CUSTOMER_ID, 
SAP.CUSTOMER_KEY AS SAP_CUSTOMER_KEY, 
MSAP.customer_id AS MSAP_CUSTOMER_ID, 
MSAP.CUSTOMER_KEY AS MSAP_CUSTOMER_KEY, 
MSAP.Extn_Customer_Name AS MSAP_CUSTOMER_NAME, 
MSAP.EXTN_SAP_PARENT_ACC_NO AS MSAP_SAP_PARENT_ACC_NO, 
MSAP.EXTN_SAP_NUMBER AS MSAP_SAP_NUMBER, 
SAP.EXTN_CUST_LINE_ACCNO_FLAG AS SAP_EXTN_CUST_LINE_ACCNO_FLAG, 
SAP.EXTN_CUST_LINE_COMM_FLAG AS SAP_EXTN_CUST_LINE_COMM_FLAG, 
SAP.EXTN_CUST_LINE_ACC_LBL AS SAP_EXTN_CUST_LINE_ACC_LBL, 
SAP.EXTN_CUST_LINE_PO_LBL AS SAP_EXTN_CUST_LINE_PO_LBL,
SAP.EXTN_CUST_LINE_SEQNO_FLAG AS SAP_EXTN_CUST_LINE_SEQNO_FLAG, 
SAP.EXTN_CUST_LINE_PONO_FLAG AS SAP_EXTN_CUST_LINE_PONO_FLAG, 
SAP.EXTN_CUST_LINE_FIELD1_FLAG AS SAP_EXTN_CUST_LINE_FIELD1_FLAG, 
SAP.EXTN_CUST_LINE_FIELD1_LBL AS SAP_EXTN_CUST_LINE_FIELD1_LBL, 
SAP.EXTN_CUST_LINE_FIELD2_FLAG AS SAP_EXTN_CUST_LINE_FIELD2_FLAG, 
SAP.EXTN_CUST_LINE_FIELD2_LBL AS SAP_EXTN_CUST_LINE_FIELD2_LBL, 
SAP.EXTN_CUST_LINE_FIELD3_FLAG AS SAP_EXTN_CUST_LINE_FIELD3_FLAG, 
SAP.EXTN_CUST_LINE_FIELD3_LBL AS SAP_EXTN_CUST_LINE_FIELD3_LBL, 
SAP.Extn_Customer_Name AS SAP_CUSTOMER_NAME, 
SAP.EXTN_SAP_PARENT_ACC_NO AS SAP_SAP_PARENT_ACC_NO, 
SAP.EXTN_SAP_NUMBER AS SAP_SAP_NUMBER ,
trim(MSAP.customer_id) || '|'||trim(SAP.Customer_Id) || '|' || trim(BillTo.Customer_Id) || '|' || trim(ShipTo.Customer_Id) as CUSTOMER_PATH
FROM 
yfs_customer ShipTo, 
yfs_customer BillTo, 
yfs_customer SAP, 
yfs_customer MSAP, 
Yfs_Customer_Addnl_Address Address, 
yfs_person_info PersonInfo
WHERE 
ShipTo.PARENT_CUSTOMER_KEY = BillTo.CUSTOMER_KEY AND 
BillTo.PARENT_CUSTOMER_KEY = SAP.CUSTOMER_KEY AND 
SAP.PARENT_CUSTOMER_KEY = MSAP.CUSTOMER_KEY AND
ShipTo.ROOT_CUSTOMER_KEY = MSAP.CUSTOMER_KEY AND BillTo.ROOT_CUSTOMER_KEY = MSAP.CUSTOMER_KEY AND SAP.ROOT_CUSTOMER_KEY = MSAP.CUSTOMER_KEY AND
ShipTo.CUSTOMER_KEY = Address.CUSTOMER_KEY AND 
Address.IS_DEFAULT_SHIP_TO = 'Y' AND 
Address.Person_Info_Key = Personinfo.Person_Info_Key And 
ShipTo.EXTN_SUFFIX_TYPE='S';

CREATE OR REPLACE VIEW XPX_CUSTOMER_ASSIGNMENT_VIEW AS 
SELECT distinct CA.USER_ID, VW.*,ORG.ORGANIZATION_NAME AS DIVISION_NAME,
UPPER (trim(VW.SHIP_TO_CUSTOMER_ID)||','||trim(VW.SHIP_TO_CUSTOMER_NAME)||','||trim(VW.SHIP_TO_EXTN_CUST_STORE_NO)||','||trim(VW.SHIP_TO_ADDR_LINE_1) ||','|| trim(VW.SHIP_TO_ADDR_LINE_2) ||','|| trim(VW.SHIP_TO_ADDR_LINE_3) ||','|| trim(VW.SHIP_TO_CITY) ||','||trim(VW.SHIP_TO_STATE) ||','|| trim(VW.SHIP_TO_COUNTRY) ||','|| trim(VW.SHIP_TO_ZIP_CODE) ||'-'|| trim(VW.EXTN_ZIP4) ||','|| trim(VW.EMAILID)) AS SHIP_TO_ADDRESS_STRING
FROM YFS_CUSTOMER_ASSIGNMENT CA, XPX_CUST_HIERARCHY_VIEW VW,YFS_ORGANIZATION ORG
WHERE (CA.customer_key = VW.ship_to_customer_key or
CA.customer_key = VW.bill_to_customer_key or
CA.customer_key = VW.sap_customer_key or
CA.customer_key = VW.msap_customer_key)
AND VW.SHIP_TO_DIVISION_ID = ORG.ORGANIZATION_KEY;

CREATE OR REPLACE VIEW XPX_CUSTOMER_SAP_NUMBER_VIEW AS 
SELECT DISTINCT VW.SAP_CUSTOMER_ID,
BILL.EXTN_SAP_NUMBER AS SAP_NUMBER,
BILL.EXTN_SAP_PARENT_ACC_NO AS SAP_PARENT_ACC_NO,
SAP.EXTN_CUST_LINE_ACCNO_FLAG AS CUST_LINE_ACCNO_FLAG,
SAP.EXTN_CUST_LINE_ACC_LBL AS CUST_LINE_ACC_LBL,
SAP.EXTN_CUST_LINE_PO_LBL AS CUST_LINE_PO_LBL,
SAP.EXTN_CUST_LINE_FIELD1_FLAG AS CUST_LINE_FIELD1_FLAG,
SAP.EXTN_CUST_LINE_FIELD1_LBL AS CUST_LINE_FIELD1_LBL,
SAP.EXTN_CUST_LINE_FIELD2_FLAG AS CUST_LINE_FIELD2_FLAG,
SAP.EXTN_CUST_LINE_FIELD2_LBL AS CUST_LINE_FIELD2_LBL,
SAP.EXTN_CUST_LINE_FIELD3_FLAG AS CUST_LINE_FIELD3_FLAG,
SAP.EXTN_CUST_LINE_FIELD3_LBL AS CUST_LINE_FIELD3_LBL,
SAP.EXTN_CUST_LINE_PONO_FLAG AS CUST_LINE_PONO_FLAG,
SAP.EXTN_CUST_LINE_SEQNO_FLAG AS CUST_LINE_SEQNO_FLAG,
VW.MSAP_CUSTOMER_ID,MSAP.EXTN_USE_CUST_SKU AS USE_CUST_SKU
FROM 
XPX_CUST_HIERARCHY_VIEW VW,
YFS_CUSTOMER MSAP, 
YFS_CUSTOMER SAP, 
YFS_CUSTOMER BILL
WHERE 
(VW.SAP_CUSTOMER_KEY= SAP.CUSTOMER_KEY and
VW.BILL_TO_CUSTOMER_KEY = BILL.CUSTOMER_KEY and
VW.MSAP_CUSTOMER_KEY = MSAP.CUSTOMER_KEY);

CREATE OR REPLACE FORCE VIEW XPEDX_LANDING_MIL ("MY_ITEMS_LIST_KEY", "NUMBER_OF_ITEMS", "USERNAME", "CUSTOMER_ID", "LIST_DESC", "LIST_NAME", "CREATEUSERNAME", "SHARE_ADMIN_ONLY", "SHARE_PRIVATE","CREATEUSERID", "MODIFYUSERID",  "MODIFYTS" , "MODIFYUSERNAME")
AS
SELECT DISTINCT(list.my_items_list_key) AS MY_ITEMS_LIST_KEY,
COUNT(litem.my_items_key) AS number_of_items,
yu.username AS USERNAME,
ls.customer_id AS CUSTOMER_ID,
LIST.LIST_DESC,
LIST.LIST_NAME,
LIST.CREATEUSERNAME AS CREATEUSERNAME,
LIST.SHARE_ADMIN_ONLY AS SHARE_ADMIN_ONLY,
LIST.SHARE_PRIVATE AS SHARE_PRIVATE,
LIST.createuserid     AS createUserid, 
list.modifyuserid     AS modifyuserid, 
list.modifyts AS modifyts,
list.MODIFYUSERNAME as MODIFYUSERNAME
FROM XPEDX_MY_ITEMS_LIST list
LEFT JOIN XPEDX_MY_ITEMS_ITEMS litem
ON litem.my_items_list_key=list.my_items_list_key
LEFT JOIN XPEDX_MY_ITEMS_LIST_SHARE ls
ON ls.my_items_list_key = list.my_items_list_key
LEFT JOIN yfs_user yu
ON trim(yu.loginid)=list.modifyuserid
group By list.my_items_list_key, yu.username, ls.customer_id, LIST.LIST_DESC, LIST.LIST_NAME, LIST.CREATEUSERNAME, LIST.SHARE_ADMIN_ONLY, LIST.SHARE_PRIVATE, LIST.createuserid, list.modifyuserid, list.modifyts, list.MODIFYUSERNAME ;


CREATE OR REPLACE FORCE VIEW XPX_ORDER_LINE_SEARCH_VIEW (
ORDER_HEADER_KEY,DOCUMENT_TYPE,ENTERPRISE_KEY,ORDER_NO,BUYER_ORGANIZATION_CODE,SELLER_ORGANIZATION_CODE,BILL_TO_KEY,BILL_TO_ID,SHIP_TO_KEY,
SHIP_TO_ID,REQ_SHIP_DATE,ORDER_TYPE,DRAFT_ORDER_FLAG,CUSTOMER_PO_NO,CUSTOMER_LINE_PO_NO,ORDER_NAME,CURRENCY,CUSTOMER_CONTACT_ID,EXTN_WEB_CONF_NUM,EXTN_ORDER_DIVISION,
EXTN_LEGACY_ORDER_NO,EXTN_LEGACY_ORDER_TYPE,EXTN_SHIP_TO_NAME,EXTN_ORDERED_BY_NAME,EXTN_CURRENCY_CODE,EXTN_TOTAL_ORDER_VALUE,EXTN_TOT_ORD_VAL_WITHOUT_TAXES,
EXTN_ORDER_SUB_TOTAL,EXTN_ORDER_STATUS,ORDER_STATUS,STATUS_QUANTITY,HOLD_TYPE,HOLD_STATUS,MODIFYTS,MODIFYUSERID, ORDER_DATE,ADDRESS_LINE1,ADDRESS_LINE2
,ADDRESS_LINE3,ADDRESS_LINE4,ADDRESS_LINE5,ADDRESS_LINE6,CITY,STATE,ZIP_CODE,COUNTRY ,EXTN_INVOICED_DATE ,EXTN_INVOICE_NO,EXTN_GENERATION_NO
, STATUS,RESOLVER_USER_ID,ITEM_ID
)AS
Select ORDER_HEADER_KEY,DOCUMENT_TYPE,ENTERPRISE_KEY,ORDER_NO,BUYER_ORGANIZATION_CODE,SELLER_ORGANIZATION_CODE,BILL_TO_KEY,BILL_TO_ID,SHIP_TO_KEY,
SHIP_TO_ID,REQ_SHIP_DATE,ORDER_TYPE,DRAFT_ORDER_FLAG,CUSTOMER_PO_NO, CUSTOMER_LINE_PO_NO,ORDER_NAME,CURRENCY,CUSTOMER_CONTACT_ID,EXTN_WEB_CONF_NUM,EXTN_ORDER_DIVISION,
EXTN_LEGACY_ORDER_NO,EXTN_LEGACY_ORDER_TYPE,EXTN_SHIP_TO_NAME,EXTN_ORDERED_BY_NAME,EXTN_CURRENCY_CODE,EXTN_TOTAL_ORDER_VALUE,EXTN_TOT_ORD_VAL_WITHOUT_TAXES,
EXTN_ORDER_SUB_TOTAL,EXTN_ORDER_STATUS,ORDER_STATUS,STATUS_QUANTITY,HOLD_TYPE,HOLD_STATUS,MODIFYTS,MODIFYUSERID, ORDER_DATE,ADDRESS_LINE1,ADDRESS_LINE2
,ADDRESS_LINE3,ADDRESS_LINE4,ADDRESS_LINE5,ADDRESS_LINE6,CITY,STATE,ZIP_CODE,COUNTRY ,EXTN_INVOICED_DATE ,EXTN_INVOICE_NO,EXTN_GENERATION_NO
, STATUS,RESOLVER_USER_ID,ITEM_ID
FROM (
Select Distinct(OH.ORDER_HEADER_KEY),OH.DOCUMENT_TYPE,Trim(OH.ENTERPRISE_KEY) As ENTERPRISE_KEY,Oh.ORDER_NO,Trim(OH.BUYER_ORGANIZATION_CODE) As BUYER_ORGANIZATION_CODE,OH.SELLER_ORGANIZATION_CODE,OH.BILL_TO_KEY,OH.BILL_TO_ID,OH.SHIP_TO_KEY,
OH.SHIP_TO_ID,OH.REQ_SHIP_DATE,OH.ORDER_TYPE,OH.DRAFT_ORDER_FLAG,OH.CUSTOMER_PO_NO, YOL.CUSTOMER_PO_NO CUSTOMER_LINE_PO_NO, OH.ORDER_NAME,OH.CURRENCY,OH.CUSTOMER_CONTACT_ID,OH.EXTN_WEB_CONF_NUM,OH.EXTN_ORDER_DIVISION,
OH.EXTN_LEGACY_ORDER_NO,OH.EXTN_LEGACY_ORDER_TYPE,OH.EXTN_SHIP_TO_NAME,OH.EXTN_ORDERED_BY_NAME,OH.EXTN_CURRENCY_CODE,OH.EXTN_TOTAL_ORDER_VALUE,OH.EXTN_TOT_ORD_VAL_WITHOUT_TAXES,
OH.EXTN_ORDER_SUB_TOTAL,TRIM(OH.EXTN_ORDER_STATUS) AS EXTN_ORDER_STATUS,TRIM(OH.EXTN_ORDER_STATUS) AS ORDER_STATUS,1 AS STATUS_QUANTITY,TRIM(OHT.HOLD_TYPE) AS HOLD_TYPE,TRIM(OHT.STATUS) AS HOLD_STATUS,OH.MODIFYTS,OH.MODIFYUSERID,OH.ORDER_DATE AS ORDER_DATE,YPI.ADDRESS_LINE1,YPI.ADDRESS_LINE2
,YPI.ADDRESS_LINE3,YPI.ADDRESS_LINE4,YPI.ADDRESS_LINE5,YPI.ADDRESS_LINE6,YPI.CITY,YPI.STATE,YPI.ZIP_CODE,YPI.COUNTRY ,OH.EXTN_INVOICED_DATE ,OH.EXTN_INVOICE_NO,OH.EXTN_GENERATION_NO
, OH.extn_order_status_prefix || ' ' || YCC.CODE_SHORT_DESCRIPTION AS STATUS,OHT.RESOLVER_USER_ID,YOL.ITEM_ID
FROM YFS_ORDER_HEADER OH 
JOIN YFS_ORDER_LINE YOL ON YOL.ORDER_HEADER_KEY= OH.ORDER_HEADER_KEY
LEFT JOIN YFS_ORDER_HOLD_TYPE OHT ON OHT.ORDER_HEADER_KEY=OH.ORDER_HEADER_KEY
LEFT JOIN YFS_PERSON_INFO YPI ON YPI.PERSON_INFO_KEY=OH.SHIP_TO_KEY
Join Yfs_Common_Code Ycc   On Trim(Code_Value)=Trim(Extn_Order_Status)
GROUP BY OH.ORDER_HEADER_KEY, OH.DOCUMENT_TYPE, OH.ENTERPRISE_KEY, OH.ORDER_NO, TRIM(OH.BUYER_ORGANIZATION_CODE), OH.SELLER_ORGANIZATION_CODE, OH.BILL_TO_KEY, OH.BILL_TO_ID, OH.SHIP_TO_KEY, OH.SHIP_TO_ID, OH.REQ_SHIP_DATE, OH.ORDER_TYPE, OH.DRAFT_ORDER_FLAG, OH.CUSTOMER_PO_NO, yol.customer_po_no, OH.ORDER_NAME, OH.CURRENCY, OH.CUSTOMER_CONTACT_ID, OH.EXTN_WEB_CONF_NUM, OH.EXTN_ORDER_DIVISION, OH.EXTN_LEGACY_ORDER_NO, OH.EXTN_LEGACY_ORDER_TYPE, OH.EXTN_SHIP_TO_NAME, OH.EXTN_ORDERED_BY_NAME, OH.EXTN_CURRENCY_CODE, OH.EXTN_TOTAL_ORDER_VALUE, OH.EXTN_TOT_ORD_VAL_WITHOUT_TAXES, OH.EXTN_ORDER_SUB_TOTAL, TRIM(OH.EXTN_ORDER_STATUS), OHT.HOLD_TYPE, TRIM(OHT.STATUS), OH.MODIFYTS, OH.MODIFYUSERID, OH.ORDER_DATE, YPI.ADDRESS_LINE1, YPI.ADDRESS_LINE2, YPI.ADDRESS_LINE3, YPI.ADDRESS_LINE4, YPI.ADDRESS_LINE5, YPI.ADDRESS_LINE6, YPI.CITY, YPI.STATE, YPI.ZIP_CODE, YPI.COUNTRY, OH.EXTN_INVOICED_DATE, OH.EXTN_INVOICE_NO,OH.EXTN_GENERATION_NO,YOL.ITEM_ID,OHT.RESOLVER_USER_ID,YCC.CODE_SHORT_DESCRIPTION,OH.extn_order_status_prefix
Order By Oh.Modifyts Desc);

CREATE OR REPLACE FORCE VIEW XPX_ORDER_SEARCH_LIST_VIEW
(
    ORDER_HEADER_KEY,
    DOCUMENT_TYPE,
    ENTERPRISE_KEY,
    ORDER_NO,
    BUYER_ORGANIZATION_CODE,
    SELLER_ORGANIZATION_CODE,
    BILL_TO_KEY,
    BILL_TO_ID,
    SHIP_TO_KEY,
    SHIP_TO_ID,
    REQ_SHIP_DATE,
    ORDER_TYPE,
    DRAFT_ORDER_FLAG,
    CUSTOMER_PO_NO,
    ORDER_NAME,
    CURRENCY,
    CUSTOMER_CONTACT_ID,
    EXTN_WEB_CONF_NUM,
    EXTN_ORDER_DIVISION,
    EXTN_LEGACY_ORDER_NO,
    EXTN_LEGACY_ORDER_TYPE,
    EXTN_SHIP_TO_NAME,
    EXTN_ORDERED_BY_NAME,
    EXTN_CURRENCY_CODE,
    EXTN_TOTAL_ORDER_VALUE,
    EXTN_TOT_ORD_VAL_WITHOUT_TAXES,
    EXTN_ORDER_SUB_TOTAL,
    EXTN_ORDER_STATUS,
    ORDER_STATUS,
    STATUS_QUANTITY,
    HOLD_TYPE,
    HOLD_STATUS,
    MODIFYTS,
    MODIFYUSERID,
    ORDER_DATE,
    ADDRESS_LINE1,
    ADDRESS_LINE2,
    ADDRESS_LINE3,
    ADDRESS_LINE4,
    ADDRESS_LINE5,
    ADDRESS_LINE6,
    CITY,
    STATE,
    ZIP_CODE,
    COUNTRY ,
    EXTN_INVOICED_DATE ,
    EXTN_INVOICE_NO,
    EXTN_GENERATION_NO,
    status,
    EXTN_ORDER_LOCK_FLAG,
    RESOLVER_USER_ID)
AS
   SELECT DISTINCT(OH.ORDER_HEADER_KEY),
      OH.DOCUMENT_TYPE,
      trim(OH.ENTERPRISE_KEY) AS ENTERPRISE_KEY,
      OH.ORDER_NO,
      TRIM(OH.BUYER_ORGANIZATION_CODE) AS BUYER_ORGANIZATION_CODE,
      OH.SELLER_ORGANIZATION_CODE,
      OH.BILL_TO_KEY,
      OH.BILL_TO_ID,
      OH.SHIP_TO_KEY,
      OH.SHIP_TO_ID,
      OH.REQ_SHIP_DATE,
      OH.ORDER_TYPE,
      OH.DRAFT_ORDER_FLAG,
      OH.CUSTOMER_PO_NO,
      OH.ORDER_NAME,
      OH.CURRENCY,
      OH.CUSTOMER_CONTACT_ID,
      OH.EXTN_WEB_CONF_NUM,
      OH.EXTN_ORDER_DIVISION,
      OH.EXTN_LEGACY_ORDER_NO,
      OH.EXTN_LEGACY_ORDER_TYPE,
      OH.EXTN_SHIP_TO_NAME,
      OH.EXTN_ORDERED_BY_NAME,
      OH.EXTN_CURRENCY_CODE,
      OH.EXTN_TOTAL_ORDER_VALUE,
      OH.EXTN_TOT_ORD_VAL_WITHOUT_TAXES,
      OH.EXTN_ORDER_SUB_TOTAL,
      TRIM(OH.EXTN_ORDER_STATUS) AS EXTN_ORDER_STATUS,
      TRIM(OH.EXTN_ORDER_STATUS)           AS ORDER_STATUS,
      1 AS STATUS_QUANTITY,
      TRIM(OHT.HOLD_TYPE) AS HOLD_TYPE,
      TRIM(OHT.STATUS)    AS HOLD_STATUS,
      OH.MODIFYTS,
      OH.MODIFYUSERID,
      OH.ORDER_DATE AS ORDER_DATE,
      YPI.ADDRESS_LINE1,
      YPI.ADDRESS_LINE2 ,
      YPI.ADDRESS_LINE3,
      YPI.ADDRESS_LINE4,
      YPI.ADDRESS_LINE5,
      YPI.ADDRESS_LINE6,
      YPI.CITY,
      YPI.STATE,
      YPI.ZIP_CODE,
      YPI.COUNTRY ,
      OH.EXTN_INVOICED_DATE ,
      OH.EXTN_INVOICE_NO,
      OH.EXTN_GENERATION_NO,
      OH.extn_order_status_prefix || ' ' || YCC.CODE_SHORT_DESCRIPTION as STATUS,
	  OH.EXTN_ORDER_LOCK_FLAG,
      OHT.RESOLVER_USER_ID
    FROM YFS_ORDER_HEADER OH

    LEFT JOIN YFS_ORDER_HOLD_TYPE OHT
    ON OHT.ORDER_HEADER_KEY=OH.ORDER_HEADER_KEY
    LEFT JOIN YFS_PERSON_INFO YPI
    ON YPI.PERSON_INFO_KEY=OH.SHIP_TO_KEY
    JOIN YFS_COMMON_CODE YCC ON TRIM(CODE_VALUE)=TRIM(EXTN_ORDER_STATUS)
    WHERE EXTN_LEGACY_ORDER_TYPE||EXTN_ORDER_STATUS !='F9000'  and YCC.code_type='XpedxWCStatus'
    GROUP BY OH.ORDER_HEADER_KEY,
      OH.DOCUMENT_TYPE,
      OH.ENTERPRISE_KEY,
      OH.ORDER_NO,
      TRIM(OH.BUYER_ORGANIZATION_CODE),
      OH.SELLER_ORGANIZATION_CODE,
      OH.BILL_TO_KEY,
      OH.BILL_TO_ID,
      OH.SHIP_TO_KEY,
      OH.SHIP_TO_ID,
      OH.REQ_SHIP_DATE,
      OH.ORDER_TYPE,
      OH.DRAFT_ORDER_FLAG,
      OH.CUSTOMER_PO_NO,
      OH.ORDER_NAME,
      OH.CURRENCY,
      OH.CUSTOMER_CONTACT_ID,
      OH.EXTN_WEB_CONF_NUM,
      OH.EXTN_ORDER_DIVISION,
      OH.EXTN_LEGACY_ORDER_NO,
      OH.EXTN_LEGACY_ORDER_TYPE,
      OH.EXTN_SHIP_TO_NAME,
      OH.EXTN_ORDERED_BY_NAME,
      OH.EXTN_CURRENCY_CODE,
      OH.EXTN_TOTAL_ORDER_VALUE,
      OH.EXTN_TOT_ORD_VAL_WITHOUT_TAXES,
      OH.EXTN_ORDER_SUB_TOTAL,
      TRIM(OH.EXTN_ORDER_STATUS),
      OHT.HOLD_TYPE,
      TRIM(OHT.STATUS),
      OH.MODIFYTS,
      OH.MODIFYUSERID,
      OH.ORDER_DATE,
      YPI.ADDRESS_LINE1,
      YPI.ADDRESS_LINE2,
      YPI.ADDRESS_LINE3,
      YPI.ADDRESS_LINE4,
      YPI.ADDRESS_LINE5,
      YPI.ADDRESS_LINE6,
      YPI.CITY,
      YPI.STATE,
      YPI.ZIP_CODE,
      YPI.COUNTRY,
      OH.EXTN_INVOICED_DATE,
      OH.EXTN_INVOICE_NO,
      OH.EXTN_GENERATION_NO,
      OH.EXTN_ORDER_LOCK_FLAG,
      OHT.RESOLVER_USER_ID,
      YCC.CODE_SHORT_DESCRIPTION,
      OH.extn_order_status_prefix
    ORDER BY OH.ORDER_DATE DESC;

CREATE OR REPLACE FORCE VIEW  XPX_CUST_VIEW_CUST_PATH
                                  AS
  SELECT C.Customer_Id            AS CUSTOMER_ID,
      C.parent_customer_key       AS parent_customer_key,
    C.Extn_Customer_Name          AS CUSTOMER_NAME,
    C.CUSTOMER_KEY                AS CUSTOMER_KEY,
    C.ORGANIZATION_CODE           AS ENTERPRISE_CODE,
    C.Extn_Cust_Store_No          AS EXTN_CUST_STORE_NO,
    c.root_customer_key           AS ROOT_CUSTOMER_KEY,
    cc.user_id                    AS USER_ID,
    MIN(PersonInfo.FIRST_NAME)    AS FIRST_NAME,
    MIN(PersonInfo.LAST_NAME)     AS LAST_NAME,
    MIN(PersonInfo.ADDRESS_LINE1) AS SHIP_TO_ADDR_LINE_1,
    MIN(PersonInfo.ADDRESS_LINE2) AS SHIP_TO_ADDR_LINE_2,
    MIN(PersonInfo.ADDRESS_LINE3) AS SHIP_TO_ADDR_LINE_3,
    MIN(PersonInfo.CITY)          AS SHIP_TO_CITY,
    MIN(PersonInfo.STATE)         AS SHIP_TO_STATE,
    MIN(PersonInfo.ZIP_CODE)      AS SHIP_TO_ZIP_CODE,
    MIN(PersonInfo.EXTN_ZIP4)     AS EXTN_ZIP4,
    MIN(PersonInfo.COUNTRY)       AS SHIP_TO_COUNTRY,
    MIN(PersonInfo.EMAILID)       AS EMAILID,
    C.EXTN_CUST_LINE_ACCNO_FLAG   AS SAP_EXTN_CUST_LINE_ACCNO_FLAG,
    C.EXTN_CUST_LINE_COMM_FLAG    AS SAP_EXTN_CUST_LINE_COMM_FLAG,
    C.EXTN_CUST_LINE_ACC_LBL      AS SAP_EXTN_CUST_LINE_ACC_LBL,
    C.EXTN_CUST_LINE_PO_LBL       AS SAP_EXTN_CUST_LINE_PO_LBL,
    C.EXTN_CUST_LINE_SEQNO_FLAG   AS SAP_EXTN_CUST_LINE_SEQNO_FLAG,
    C.EXTN_CUST_LINE_PONO_FLAG    AS SAP_EXTN_CUST_LINE_PONO_FLAG,
    C.EXTN_CUST_LINE_FIELD1_FLAG  AS SAP_EXTN_CUST_LINE_FIELD1_FLAG,
    C.EXTN_CUST_LINE_FIELD1_LBL   AS SAP_EXTN_CUST_LINE_FIELD1_LBL,
    C.EXTN_CUST_LINE_FIELD2_FLAG  AS SAP_EXTN_CUST_LINE_FIELD2_FLAG,
    C.EXTN_CUST_LINE_FIELD2_LBL   AS SAP_EXTN_CUST_LINE_FIELD2_LBL,
    C.EXTN_CUST_LINE_FIELD3_FLAG  AS SAP_EXTN_CUST_LINE_FIELD3_FLAG,
    C.EXTN_CUST_LINE_FIELD3_LBL   AS SAP_EXTN_CUST_LINE_FIELD3_LBL
  FROM yfs_customer_contact cc
  INNER JOIN yfs_customer c
  ON RTRIM (c.root_customer_key) = TRIM (cc.customer_key)
  LEFT JOIN yfs_customer_addnl_address address
  ON ( c.customer_key              = address.customer_key
  AND address.customer_contact_key = ' ' )
  LEFT JOIN yfs_person_info personinfo
  ON ( personinfo.person_info_key = address.person_info_key )
  GROUP BY C.Customer_Id, C.parent_customer_key, C.Extn_Customer_Name, C.CUSTOMER_KEY, C.ORGANIZATION_CODE, C.Extn_Cust_Store_No, c.root_customer_key, cc.user_id, C.EXTN_CUST_LINE_ACCNO_FLAG, C.EXTN_CUST_LINE_COMM_FLAG, C.EXTN_CUST_LINE_ACC_LBL, C.EXTN_CUST_LINE_PO_LBL, C.EXTN_CUST_LINE_SEQNO_FLAG, C.EXTN_CUST_LINE_PONO_FLAG, C.EXTN_CUST_LINE_FIELD1_FLAG, C.EXTN_CUST_LINE_FIELD1_LBL, C.EXTN_CUST_LINE_FIELD2_FLAG, C.EXTN_CUST_LINE_FIELD2_LBL, C.EXTN_CUST_LINE_FIELD3_FLAG, C.EXTN_CUST_LINE_FIELD3_LBL,parent_customer_key
  MINUS
  SELECT C.Customer_Id            AS CUSTOMER_ID,
  C.parent_customer_key       AS parent_customer_key,
    C.Extn_Customer_Name          AS CUSTOMER_NAME,
    C.CUSTOMER_KEY                AS CUSTOMER_KEY,
    C.ORGANIZATION_CODE           AS ENTERPRISE_CODE,
    C.Extn_Cust_Store_No          AS EXTN_CUST_STORE_NO,
    c.root_customer_key           AS ROOT_CUSTOMER_KEY,
    cc.user_id                    AS USER_ID,
    MIN(PersonInfo.FIRST_NAME)    AS FIRST_NAME,
    MIN(PersonInfo.LAST_NAME)     AS LAST_NAME,
    MIN(PersonInfo.ADDRESS_LINE1) AS SHIP_TO_ADDR_LINE_1,
    MIN(PersonInfo.ADDRESS_LINE2) AS SHIP_TO_ADDR_LINE_2,
    MIN(PersonInfo.ADDRESS_LINE3) AS SHIP_TO_ADDR_LINE_3,
    MIN(PersonInfo.CITY)          AS SHIP_TO_CITY,
    MIN(PersonInfo.STATE)         AS SHIP_TO_STATE,
    MIN(PersonInfo.ZIP_CODE)      AS SHIP_TO_ZIP_CODE,
    MIN(PersonInfo.EXTN_ZIP4)     AS EXTN_ZIP4,
    MIN(PersonInfo.COUNTRY)       AS SHIP_TO_COUNTRY,
    MIN(PersonInfo.EMAILID)       AS EMAILID,
    C.EXTN_CUST_LINE_ACCNO_FLAG   AS SAP_EXTN_CUST_LINE_ACCNO_FLAG,
    C.EXTN_CUST_LINE_COMM_FLAG    AS SAP_EXTN_CUST_LINE_COMM_FLAG,
    C.EXTN_CUST_LINE_ACC_LBL      AS SAP_EXTN_CUST_LINE_ACC_LBL,
    C.EXTN_CUST_LINE_PO_LBL       AS SAP_EXTN_CUST_LINE_PO_LBL,
    C.EXTN_CUST_LINE_SEQNO_FLAG   AS SAP_EXTN_CUST_LINE_SEQNO_FLAG,
    C.EXTN_CUST_LINE_PONO_FLAG    AS SAP_EXTN_CUST_LINE_PONO_FLAG,
    C.EXTN_CUST_LINE_FIELD1_FLAG  AS SAP_EXTN_CUST_LINE_FIELD1_FLAG,
    C.EXTN_CUST_LINE_FIELD1_LBL   AS SAP_EXTN_CUST_LINE_FIELD1_LBL,
    C.EXTN_CUST_LINE_FIELD2_FLAG  AS SAP_EXTN_CUST_LINE_FIELD2_FLAG,
    C.EXTN_CUST_LINE_FIELD2_LBL   AS SAP_EXTN_CUST_LINE_FIELD2_LBL,
    C.EXTN_CUST_LINE_FIELD3_FLAG  AS SAP_EXTN_CUST_LINE_FIELD3_FLAG,
    C.EXTN_CUST_LINE_FIELD3_LBL   AS SAP_EXTN_CUST_LINE_FIELD3_LBL
  FROM yfs_customer_contact cc
  INNER JOIN yfs_customer c
  ON RTRIM (c.root_customer_key) = TRIM (cc.customer_key)
  LEFT JOIN yfs_customer_addnl_address address
  ON ( c.customer_key              = address.customer_key
  AND address.customer_contact_key = ' ' )
  LEFT JOIN yfs_person_info personinfo
  ON ( personinfo.person_info_key = address.person_info_key )
  JOIN yfs_customer_assignment ca
  ON ( ca.customer_key = c.customer_key
  AND cc.user_id       = ca.user_id )
  GROUP BY C.Customer_Id ,
    C.Extn_Customer_Name ,
    C.CUSTOMER_KEY ,
    C.ORGANIZATION_CODE ,
    C.Extn_Cust_Store_No ,
    c.root_customer_key ,
    cc.user_id ,
    C.EXTN_CUST_LINE_ACCNO_FLAG ,
    C.EXTN_CUST_LINE_COMM_FLAG ,
    C.EXTN_CUST_LINE_ACC_LBL ,
    C.EXTN_CUST_LINE_PO_LBL ,
    C.EXTN_CUST_LINE_SEQNO_FLAG ,
    C.EXTN_CUST_LINE_PONO_FLAG,
    C.EXTN_CUST_LINE_FIELD1_FLAG,
    C.EXTN_CUST_LINE_FIELD1_LBL,
    C.EXTN_CUST_LINE_FIELD2_FLAG,
    C.EXTN_CUST_LINE_FIELD2_LBL,
    C.EXTN_CUST_LINE_FIELD3_FLAG,
    C.EXTN_CUST_LINE_FIELD3_LBL,
    parent_customer_key;
    
    
    
    CREATE OR REPLACE FORCE VIEW XPX_CUST_VIEW ("CUSTOMER_ID", "CUSTOMER_NAME", "CUSTOMER_KEY", "ENTERPRISE_CODE", "EXTN_CUST_STORE_NO", "ROOT_CUSTOMER_KEY", "USER_ID", "FIRST_NAME", "LAST_NAME", "SHIP_TO_ADDR_LINE_1", "SHIP_TO_ADDR_LINE_2", "SHIP_TO_ADDR_LINE_3", "SHIP_TO_CITY", "SHIP_TO_STATE", "SHIP_TO_ZIP_CODE", "EXTN_ZIP4", "SHIP_TO_COUNTRY", "EMAILID", "SAP_EXTN_CUST_LINE_ACCNO_FLAG", "SAP_EXTN_CUST_LINE_COMM_FLAG", "SAP_EXTN_CUST_LINE_ACC_LBL", "SAP_EXTN_CUST_LINE_PO_LBL","SAP_EXTN_CUST_LINE_SEQNO_FLAG", "SAP_EXTN_CUST_LINE_PONO_FLAG", "SAP_EXTN_CUST_LINE_FIELD1_FLAG", "SAP_EXTN_CUST_LINE_FIELD1_LBL", "SAP_EXTN_CUST_LINE_FIELD2_FLAG", "SAP_EXTN_CUST_LINE_FIELD2_LBL", "SAP_EXTN_CUST_LINE_FIELD3_FLAG", "SAP_EXTN_CUST_LINE_FIELD3_LBL", "CUSTOMER_PATH")
AS
  SELECT xView.CUSTOMER_ID,
    xView.CUSTOMER_NAME,
    xView.CUSTOMER_KEY,
    xView.ENTERPRISE_CODE,
    xView.EXTN_CUST_STORE_NO,
    xView.ROOT_CUSTOMER_KEY,
    xView.USER_ID,
    xView.FIRST_NAME,
    xView.LAST_NAME,
    xView.SHIP_TO_ADDR_LINE_1,
    xView.SHIP_TO_ADDR_LINE_2,
    xView.SHIP_TO_ADDR_LINE_3,
    xView.SHIP_TO_CITY,
    xView.SHIP_TO_STATE,
    xView.SHIP_TO_ZIP_CODE,
    xView.EXTN_ZIP4,
    xView.SHIP_TO_COUNTRY,
    xView.EMAILID,
    xView.SAP_EXTN_CUST_LINE_ACCNO_FLAG,
    xView.SAP_EXTN_CUST_LINE_COMM_FLAG,
    xView.SAP_EXTN_CUST_LINE_ACC_LBL,
    xView.SAP_EXTN_CUST_LINE_PO_LBL,
    xView.SAP_EXTN_CUST_LINE_SEQNO_FLAG,
    xView.SAP_EXTN_CUST_LINE_PONO_FLAG,
    xView.SAP_EXTN_CUST_LINE_FIELD1_FLAG,
    xView.SAP_EXTN_CUST_LINE_FIELD1_LBL,
    xView.SAP_EXTN_CUST_LINE_FIELD2_FLAG,
    xView.SAP_EXTN_CUST_LINE_FIELD2_LBL,
    xView.SAP_EXTN_CUST_LINE_FIELD3_FLAG,
    xView.SAP_EXTN_CUST_LINE_FIELD3_LBL,
    trim(customer3.customer_id)
    ||trim(customer2.customer_id)
    || trim(current_customer.Customer_Id)
    || xView.customer_id AS CUSTOMER_PATH
  FROM XPX_CUST_VIEW_CUST_PATH xView
  LEFT JOIN YFS_CUSTOMER current_customer
  ON current_customer.customer_key = xview.parent_customer_key
  LEFT JOIN YFS_CUSTOMER customer2
  ON customer2.customer_key=current_customer.parent_customer_key
  LEFT JOIN YFS_CUSTOMER customer3
  ON customer3.customer_key=customer2.parent_customer_key;


    
CREATE OR REPLACE VIEW XPX_SALES_REP_CUSTOMERS AS
  SELECT DISTINCT YC1.EXTN_LEGACY_CUST_NUMBER AS CUSTOMER_NO,
    YC1.EXTN_CUSTOMER_NAME                   AS CUSTOMER_NAME,
    YC2.CUSTOMER_ID                          AS CUSTOMER_ID,
    YO.ORGANIZATION_NAME                     AS MSAP_ORGANIZATION_NAME,
    YC2.ORGANIZATION_CODE                    AS MSAP_ORGANIZATION_CODE,
    SR.NETWORK_ID                            AS USER_ID,
    YU.EXTN_EMPLOYEE_ID                      AS SALES_REP_ID,
    YPI.FIRST_NAME                           AS SR_FIRST_NAME,
    YPI.LAST_NAME                            AS SR_LAST_NAME,
    YPI.EMAILID                              AS SR_EMAILID,
    YU.EXTN_SALT_KEY			     AS EXTN_SALT_KEY
  FROM XPEDX_SALES_REP SR
  INNER JOIN YFS_CUSTOMER YC1
  ON YC1.CUSTOMER_key = SR.SALES_CUSTOMER_KEY
  AND YC1.EXTN_SUFFIX_TYPE = 'B'
  INNER JOIN YFS_CUSTOMER YC2
  ON (YC2.CUSTOMER_KEY = CAST(YC1.ROOT_CUSTOMER_KEY AS CHAR(50)))
  INNER JOIN YFS_ORGANIZATION YO
  ON (YC2.CUSTOMER_ID = YO.ORGANIZATION_CODE)
  INNER JOIN YFS_USER YU
  ON YU.loginid = Cast(SR.network_id AS CHAR(100)) 
  LEFT OUTER JOIN YFS_PERSON_INFO YPI
  ON YU.CONTACTADDRESS_KEY   = YPI.PERSON_INFO_KEY
  AND (YC1.ROOT_CUSTOMER_KEY IS NOT NULL)
  AND (SR.SALES_CUSTOMER_KEY = YC1.CUSTOMER_KEY);
  
CREATE OR REPLACE FORCE VIEW XPX_EDITABLE_ORDER_LIST_VIEW AS
  SELECT DISTINCT(OH.EXTN_WEB_CONF_NUM), OH.DOCUMENT_TYPE, TRIM(OH.ENTERPRISE_KEY) AS ENTERPRISE_KEY, OH.SHIP_TO_ID, OH.CUSTOMER_PO_NO,
         (CASE WHEN TRIM(OH.EXTN_ORDER_STATUS)='1100.0100' AND OHT.HOLD_TYPE in ('NEEDS_ATTENTION','LEGACY_CNCL_ORD_HOLD','ORDER_EXCEPTION_HOLD','LEGACY_CNCL_LNE_HOLD') THEN 'Y'
	             ELSE 'N' END) as IS_HLD_TYP_SBMT_CSR_REVIEW,
		     (CASE WHEN OH.ORDER_TYPE !='Customer' and OH.EXTN_ORDER_LOCK_FLAG='Y' THEN 'Y' 
	    	       ELSE 'N' END) as IS_ORDER_LOCKED 
  FROM YFS_ORDER_HEADER OH 
  LEFT JOIN YFS_ORDER_HOLD_TYPE OHT ON OHT.ORDER_HEADER_KEY=OH.ORDER_HEADER_KEY 
  WHERE TRIM(OH.EXTN_ORDER_STATUS) IN ('1100.0100','1100.5150','1100.5250','1100.5350','1100.5400','1100.5450');
    
    
CREATE  OR REPLACE VIEW XPX_EDIT_ORDER_LINE_LIST_VIEW AS
  SELECT DISTINCT(OH.EXTN_WEB_CONF_NUM), OH.DOCUMENT_TYPE, TRIM(OH.ENTERPRISE_KEY) AS ENTERPRISE_KEY, OH.SHIP_TO_ID, OL.ITEM_ID,
         (CASE WHEN TRIM(OH.EXTN_ORDER_STATUS)='1100.0100' AND OHT.HOLD_TYPE in ('NEEDS_ATTENTION','LEGACY_CNCL_ORD_HOLD','ORDER_EXCEPTION_HOLD','LEGACY_CNCL_LNE_HOLD') THEN 'Y'
	             ELSE 'N' END) as IS_HLD_TYP_SBMT_CSR_REVIEW,
		     (CASE WHEN OH.ORDER_TYPE !='Customer' and OH.EXTN_ORDER_LOCK_FLAG='Y' THEN 'Y' 
	    	       ELSE 'N' END) as IS_ORDER_LOCKED 
  FROM YFS_ORDER_HEADER OH
  JOIN YFS_ORDER_LINE OL ON OL.ORDER_HEADER_KEY=OH.ORDER_HEADER_KEY
  LEFT JOIN YFS_ORDER_HOLD_TYPE OHT ON OHT.ORDER_HEADER_KEY=OH.ORDER_HEADER_KEY 
  WHERE TRIM(OH.EXTN_ORDER_STATUS) IN ('1100.0100','1100.5150','1100.5250','1100.5350','1100.5400','1100.5450');



   CREATE OR REPLACE FORCE VIEW "XPX_CHILD_CUSTOMERS_VIEW" ("ORGANIZATION_NAME", "ORGANIZATION_CODE", "ROOT_CUSTOMER_KEY", "PARENT_CUSTOMER_ID", "STATUS", "ENTERPRISE_CODE", "PARENT_CUSTOMER_KEY", "CUSTOMER_ID", "CUSTOMER_KEY", "CHILD_EXTN_SUFFIX_TYPE", "FIRST_NAME", "LAST_NAME", "ADDR_LINE_1", "ADDR_LINE_2", "ADDR_LINE_3", "CITY", "STATE", "ZIP_CODE", "EXTN_ZIP4", "COUNTRY", "EMAILID", "ADDRESS_CUSTOMER_KEY") AS 
  SELECT "ORGANIZATION_NAME","ORGANIZATION_CODE","ROOT_CUSTOMER_KEY","PARENT_CUSTOMER_ID","STATUS","ENTERPRISE_CODE","PARENT_CUSTOMER_KEY","CUSTOMER_ID","CUSTOMER_KEY","CHILD_EXTN_SUFFIX_TYPE","FIRST_NAME","LAST_NAME","ADDR_LINE_1","ADDR_LINE_2","ADDR_LINE_3","CITY","STATE","ZIP_CODE","EXTN_ZIP4","COUNTRY","EMAILID","ADDRESS_CUSTOMER_KEY" FROM (
(SELECT ORG.ORGANIZATION_NAME,ORG.ORGANIZATION_CODE, PARENT_CUT.ROOT_CUSTOMER_KEY,PARENT_CUT.CUSTOMER_ID PARENT_CUSTOMER_ID,CHILD_CUST.status,CHILD_CUST.organization_code ENTERPRISE_CODE,CHILD_CUST.PARENT_CUSTOMER_KEY,CHILD_CUST.CUSTOMER_ID,CHILD_CUST.CUSTOMER_KEY,CHILD_CUST.EXTN_SUFFIX_TYPE AS CHILD_EXTN_SUFFIX_TYPE FROM YFS_CUSTOMER PARENT_CUT ,YFS_CUSTOMER CHILD_CUST,YFS_ORGANIZATION ORG  WHERE CHILD_CUST.PARENT_CUSTOMER_KEY=PARENT_CUT.CUSTOMER_KEY AND ORG.ORGANIZATION_KEY=CHILD_CUST.CUSTOMER_ID  ) A
LEFT JOIN (SELECT PERSONINFO.FIRST_NAME AS FIRST_NAME, 
PERSONINFO.LAST_NAME AS LAST_NAME, PERSONINFO.ADDRESS_LINE1 AS ADDR_LINE_1, PERSONINFO.ADDRESS_LINE2 AS ADDR_LINE_2,PERSONINFO.ADDRESS_LINE3 AS ADDR_LINE_3, 
PERSONINFO.CITY AS CITY, 
PERSONINFO.STATE AS STATE, 
PERSONINFO.ZIP_CODE AS ZIP_CODE, 
PERSONINFO.EXTN_ZIP4 AS EXTN_ZIP4, 
PERSONINFO.COUNTRY AS COUNTRY, 
PERSONINFO.EMAILID AS EMAILID, ADDRESS.CUSTOMER_KEY AS ADDRESS_CUSTOMER_KEY  FROM YFS_CUSTOMER_ADDNL_ADDRESS ADDRESS , YFS_PERSON_INFO PERSONINFO WHERE  ADDRESS.PERSON_INFO_KEY = PERSONINFO.PERSON_INFO_KEY  ) B
ON B.ADDRESS_CUSTOMER_KEY=A.CUSTOMER_KEY ) 
 ;
 


COMMIT;
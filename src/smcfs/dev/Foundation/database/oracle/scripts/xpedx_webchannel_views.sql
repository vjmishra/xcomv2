CREATE OR REPLACE VIEW XPEDX_ORDER_LIST_VIEW
AS
SELECT
yus.username AS ORDERED_BY_NAME,
ohr.modifyts AS LAST_MODIFYTS,
ohr.order_name ,
ohr.order_header_key ,
ohr.order_no,
ohr.draft_order_flag,
ohr.document_type ,
ohr.authorized_client,
ohr.buyer_organization_code ,
ohr.extn_web_conf_num,
ohr.extn_order_description AS extn_order_description,
count(yor.order_line_key) as total_number_of_item
FROM yfs_order_header ohr
LEFT JOIN yfs_order_line yor ON yor.order_header_key=ohr.order_header_key
JOIN yfs_user yus ON (ohr.modifyuserid = trim(yus.loginid))
GROUP BY ohr.order_header_key ,ohr.order_no, yus.username,ohr.modifyts,ohr.order_name,ohr.draft_order_flag,ohr.document_type,ohr.AUTHORIZED_CLIENT,ohr.BUYER_ORGANIZATION_CODE,extn_web_conf_num,ohr.extn_order_description,yus.username;


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
ShipTo.Extn_Customer_Name AS SHIP_TO_CUSTOMER_NAME,
ShipTo.CUSTOMER_KEY AS SHIP_TO_CUSTOMER_KEY,
ShipTo.ORGANIZATION_CODE AS ENTERPRISE_CODE,
ShipTo.Extn_Cust_Store_No AS SHIP_TO_EXTN_CUST_STORE_NO,
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
SAP.Customer_Id AS SAP_CUSTOMER_ID,
SAP.CUSTOMER_KEY AS SAP_CUSTOMER_KEY,
MSAP.customer_id AS MSAP_CUSTOMER_ID,
MSAP.CUSTOMER_KEY AS MSAP_CUSTOMER_KEY,
SAP.EXTN_CUST_LINE_ACCNO_FLAG AS SAP_EXTN_CUST_LINE_ACCNO_FLAG,
SAP.EXTN_CUST_LINE_COMM_FLAG AS SAP_EXTN_CUST_LINE_COMM_FLAG,
SAP.EXTN_CUST_LINE_ACC_LBL AS SAP_EXTN_CUST_LINE_ACC_LBL,
SAP.EXTN_CUST_LINE_SEQNO_FLAG AS SAP_EXTN_CUST_LINE_SEQNO_FLAG,
SAP.EXTN_CUST_LINE_PONO_FLAG AS SAP_EXTN_CUST_LINE_PONO_FLAG,
SAP.EXTN_CUST_LINE_FIELD1_FLAG AS SAP_EXTN_CUST_LINE_FIELD1_FLAG,
SAP.EXTN_CUST_LINE_FIELD1_LBL AS SAP_EXTN_CUST_LINE_FIELD1_LBL,
SAP.EXTN_CUST_LINE_FIELD2_FLAG AS SAP_EXTN_CUST_LINE_FIELD2_FLAG,
SAP.EXTN_CUST_LINE_FIELD2_LBL AS SAP_EXTN_CUST_LINE_FIELD2_LBL,
SAP.EXTN_CUST_LINE_FIELD3_FLAG AS SAP_EXTN_CUST_LINE_FIELD3_FLAG,
SAP.EXTN_CUST_LINE_FIELD3_LBL AS SAP_EXTN_CUST_LINE_FIELD3_LBL
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
ShipTo.CUSTOMER_KEY = Address.CUSTOMER_KEY AND
Address.IS_DEFAULT_SHIP_TO = 'Y' AND
Address.Person_Info_Key = Personinfo.Person_Info_Key And
ShipTo.EXTN_SUFFIX_TYPE='S';

CREATE OR REPLACE VIEW XPX_CUSTOMER_ASSIGNMENT_VIEW AS 
SELECT distinct CA.USER_ID, VW.*,
UPPER (trim(VW.SHIP_TO_CUSTOMER_ID)||','||trim(VW.SHIP_TO_CUSTOMER_NAME)||','||trim(VW.SHIP_TO_EXTN_CUST_STORE_NO)||','||trim(VW.SHIP_TO_ADDR_LINE_1) ||','|| trim(VW.SHIP_TO_ADDR_LINE_2) ||','|| trim(VW.SHIP_TO_ADDR_LINE_3) ||','|| trim(VW.SHIP_TO_CITY) ||','||trim(VW.SHIP_TO_STATE) ||','|| trim(VW.SHIP_TO_COUNTRY) ||','|| trim(VW.SHIP_TO_ZIP_CODE) ||'-'|| trim(VW.EXTN_ZIP4) ||','|| trim(VW.EMAILID)) AS SHIP_TO_ADDRESS_STRING
FROM YFS_CUSTOMER_ASSIGNMENT CA, XPX_CUST_HIERARCHY_VIEW VW
WHERE (CA.customer_key = VW.ship_to_customer_key or
CA.customer_key = VW.bill_to_customer_key or
CA.customer_key = VW.sap_customer_key or
CA.customer_key = VW.msap_customer_key);

CREATE OR REPLACE VIEW XPX_CUSTOMER_SAP_NUMBER_VIEW AS 
SELECT DISTINCT VW.SAP_CUSTOMER_ID,
BILL.EXTN_SAP_NUMBER AS SAP_NUMBER,
BILL.EXTN_SAP_PARENT_ACC_NO AS SAP_PARENT_ACC_NO,
SAP.EXTN_CUST_LINE_ACCNO_FLAG AS CUST_LINE_ACCNO_FLAG,
SAP.EXTN_CUST_LINE_ACC_LBL AS CUST_LINE_ACC_LBL,
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

CREATE OR REPLACE VIEW XPEDX_LANDING_MIL AS
SELECT
distinct(list.my_items_list_key)  AS MY_ITEMS_LIST_KEY,
count(litem.my_items_key) as number_of_items,
yu.username AS USERNAME,
ls.customer_id AS CUSTOMER_ID,
LIST.LIST_DESC,
LIST.LIST_NAME,
LIST.CREATEUSERNAME AS CREATEUSERNAME,
LIST.SHARE_ADMIN_ONLY 
AS SHARE_ADMIN_ONLY,
LIST.SHARE_PRIVATE AS SHARE_PRIVATE,
NVL(max(litem.modifyts),list.modifyts) as modifyts
FROM
XPEDX_MY_ITEMS_LIST list
LEFT JOIN XPEDX_MY_ITEMS_ITEMS litem  ON  litem.my_items_list_key=list.my_items_list_key
LEFT JOIN XPEDX_MY_ITEMS_LIST_SHARE 
ls ON ls.my_items_list_key = list.my_items_list_key
LEFT JOIN yfs_user yu ON trim(yu.loginid)=list.modifyuserid
GROUP BY list.my_items_list_key, yu.username, ls.customer_id, LIST.LIST_DESC, LIST.LIST_NAME, LIST.CREATEUSERNAME, LIST.SHARE_ADMIN_ONLY, LIST.SHARE_PRIVATE,litem.modifyts,list.modifyts;


COMMIT;
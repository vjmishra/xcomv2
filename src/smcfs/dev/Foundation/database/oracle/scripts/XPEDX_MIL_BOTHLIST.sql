CREATE OR REPLACE FORCE VIEW "NG"."XPEDX_MIL_BOTHLIST" ("MY_ITEMS_LIST_KEY", "CUSTOMER_ID", "LIST_NAME", "LIST_DESC", "CREATEUSERNAME", "CREATEUSERID", "MODIFYUSERID", "MODIFYUSERNAME", "SHARE_ADMIN_ONLY", "SHARE_PRIVATE", "MODIFYTS", "LIST_ORDER", "CUSTOMER_KEY", "PARENT_CUSTOMER_KEY", "ROOT_CUSTOMER_KEY", "EXTN_SUFFIX_TYPE", "EXTN_CUSTOMER_DIVISION", "ORGANIZATION_NAME", "ORGANIZATION_CODE", "SHARE_CUSTOMER_ID", "MSAPNAME", "MSAPCUSTOMERID", "TOTALITEMS", "CREATED_IS_SALES_REP", "MODIFIED_IS_SALES_REP") 
AS
  SELECT DISTINCT mil.my_items_list_key ,
    mil.customer_id ,
    mil.list_name,
    mil.list_desc ,
    mil.createusername ,
    mil.createuserid,
    mil.modifyuserid ,
    mil.modifyusername ,
    mil.share_admin_only,
    mil.share_private,
    mil.modifyts,
    mil.list_order,
    cust.customer_key,
    cust.parent_customer_key,
    cust.root_customer_key,
    cust.extn_suffix_type,
    cust.extn_customer_division,
    org.organization_name,
    org.organization_code,
    mils.customer_id AS share_customer_id,
    msaporg.organization_name
    || ' ('
    || SUBSTR (msapcust.customer_id, 4, 10)
    || ')' AS msapname,  msapcust.customer_id,
    (SELECT COUNT (*)
    FROM xpedx_my_items_items item
    WHERE item.my_items_list_key = mil.my_items_list_key
    )                             AS totalitems,
    
    nvl(CreatedUser.EXTN_IS_SALES_REP,'N') AS CREATED_IS_SALES_REP,
    nvl(ModifyUser.EXTN_IS_SALES_REP,'N')  AS MODIFIED_IS_SALES_REP
  FROM xpedx_my_items_list mil
  INNER JOIN yfs_customer cust
  ON (mil.customer_id) = TRIM (cust.customer_id)
  INNER JOIN yfs_customer msapcust
  ON TRIM (cust.root_customer_key) = TRIM (msapcust.customer_key)
  INNER JOIN yfs_organization msaporg
  ON TRIM (msapcust.customer_id) = TRIM (msaporg.organization_key)
  LEFT OUTER JOIN xpedx_my_items_list_share mils
  ON mil.my_items_list_key = mils.my_items_list_key
  LEFT OUTER JOIN yfs_organization org
  ON TRIM (mils.division_id) = TRIM (org.organization_code)
 LEFT JOIN yfs_customer_contact CreatedUser
  ON mil.CREATEUSERID = CreatedUser.USER_ID
 LEFT JOIN yfs_customer_contact ModifyUser
  ON mil.MODIFYUSERID = ModifyUser.USER_ID
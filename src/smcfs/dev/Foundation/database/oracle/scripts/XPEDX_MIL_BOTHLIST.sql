CREATE OR REPLACE FORCE VIEW ng.xpedx_mil_bothlist (my_items_list_key,
                                                    customer_id,
                                                    list_name,
                                                    list_desc,
                                                    createusername,
                                                    createuserid,
                                                    modifyuserid,
                                                    modifyusername,
                                                    share_admin_only,
                                                    share_private,
                                                   modifyts,
                                                    list_order,
                                                    customer_key,
                                                    parent_customer_key,
                                                    root_customer_key,
                                                    extn_suffix_type,
                                                    extn_customer_division,
                                                    organization_name,
                                                    organization_code,
                                                    share_customer_id,
                                                    msapname,
                                                    totalitems,
                                                    CREATED_IS_SALES_REP,
                                                    MODIFIED_IS_SALES_REP
                                                   ) as
   SELECT DISTINCT mil.my_items_list_key
   , mil.customer_id
   , mil.list_name,
                   mil.list_desc
                   ,mil.createusername
                   ,mil.createuserid,
                   mil.modifyuserid
                   ,mil.modifyusername
                   , mil.share_admin_only,
                   mil.share_private, mil.modifyts, mil.list_order,
                   cust.customer_key, cust.parent_customer_key,
                   cust.root_customer_key, cust.extn_suffix_type,
                   cust.extn_customer_division, org.organization_name,
                   org.organization_code,
                   mils.customer_id AS share_customer_id,
                      msaporg.organization_name
                   || ' ('
                   || SUBSTR (msapcust.customer_id, 4, 10)
                   || ')' AS msapname,
                   (SELECT COUNT (*)
                      FROM xpedx_my_items_items item
                     WHERE item.my_items_list_key =
                                          mil.my_items_list_key)
                                                                AS totalitems,
                   CreatedUser.EXTN_IS_SALES_REP AS CREATED_IS_SALES_REP,
                   ModifyUser.EXTN_IS_SALES_REP AS MODIFIED_IS_SALES_REP
              FROM xpedx_my_items_list mil INNER JOIN yfs_customer cust
                   ON (mil.customer_id) = TRIM (cust.customer_id)
                   INNER JOIN yfs_customer msapcust
                   ON TRIM (cust.root_customer_key) =
                                                  TRIM (msapcust.customer_key)
                   INNER JOIN yfs_organization msaporg
                   ON TRIM (msapcust.customer_id) =
                                               TRIM (msaporg.organization_key)
                   LEFT OUTER JOIN xpedx_my_items_list_share mils
                   ON mil.my_items_list_key = mils.my_items_list_key
                   LEFT OUTER JOIN yfs_organization org
                   ON TRIM (mils.division_id) = TRIM (org.organization_code)
                   inner join yfs_customer_contact CreatedUser on mil.CREATEUSERID = CreatedUser.USER_ID
                   inner join yfs_customer_contact ModifyUser on mil.MODIFYUSERID = ModifyUser.USER_ID;  

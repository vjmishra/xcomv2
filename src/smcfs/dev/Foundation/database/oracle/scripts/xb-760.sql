/* Updating Mfg Item for all customer execept Packaging customer  */

update yfs_customer set extn_mfg_item_flag = 'Y' where customer_id like '%M-XX-B%' and extn_customer_class != 'CU' ;
commit;


/* Update Customer Item flag by verifying if Master SAP of Bill-To customer had customer item number set on it. */

update yfs_customer set extn_customer_item_flag = 'Y' where customer_id like '%M-XX-B%' and root_customer_key in (select customer_key from yfs_customer where customer_id like '%M-XPED-CC%' and extn_use_cust_sku = '1') ;
commit;

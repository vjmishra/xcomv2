update yfs_customer set EXTN_MIN_ORDER_AMOUNT = '300' ,EXTN_MIN_CHARGE_AMOUNT='25' where EXTN_SERVICE_OPT_CODE ='V'  and extn_ship_from_branch in('81', '82', '83', '84', '86') ;
update yfs_customer set EXTN_MIN_ORDER_AMOUNT = '400' ,EXTN_MIN_CHARGE_AMOUNT='50' where EXTN_SERVICE_OPT_CODE ='Q'  and extn_ship_from_branch in('81', '82', '83', '84', '86') ;

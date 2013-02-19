/* 
Script created: 10/1/2012
Created by: Mahmoud Lamriben
FUNCTION: This script is to be used during the production support database refresh from any other environment(

Date Modified: 
12/17/2012: by ML: Mod#1 - added "where not (loginid like 'guest_%' or loginid = 'admin');" clause to line 44 to avoide resetting pwd on the admin and guest_* users. 
12/17/2012: by ML: Mod#2 added new update for yfs_organization 
--12/17/2012 - Mod#3 -- excluded salesrep logins from the pwd update .
 */

--Update Legacy regions
--select EXTN_ORIG_ENVIRONMENT_CODE from yfs_customer where rownum < 100;
-- select distinct(EXTN_ORIG_ENVIRONMENT_CODE) from yfs_customer;

update yfs_customer set EXTN_ORIG_ENVIRONMENT_CODE = 'T3' where (EXTN_ORIG_ENVIRONMENT_CODE = 'D3' OR EXTN_ORIG_ENVIRONMENT_CODE = 'N3');
commit;

update yfs_customer set EXTN_ORIG_ENVIRONMENT_CODE = 'T4' where (EXTN_ORIG_ENVIRONMENT_CODE = 'D4' OR EXTN_ORIG_ENVIRONMENT_CODE = 'N4');
commit;

update yfs_customer_contact set emailid = 'xpedx.com@gmail.com'; 
commit;

update yfs_person_info set emailid = 'xpedx.com@gmail.com' where PERSON_INFO_KEY in(
select userpersoninfo.PERSON_INFO_KEY
from yfs_customer_contact cc 
inner join yfs_user  u on trim(cc.USER_ID) = trim(u.LOGINID) 
inner join yfs_person_info userpersoninfo on u.CONTACTADDRESS_KEY = userpersoninfo.PERSON_INFO_KEY); 
commit;

update yfs_person_info set emailid = 'xpedx.com@gmail.com' where PERSON_INFO_KEY in(
select contactpersoninfo.PERSON_INFO_KEY
from yfs_customer_contact cc 
inner join YFS_CUSTOMER_ADDNL_ADDRESS addr on cc.CUSTOMER_CONTACT_KEY = addr.CUSTOMER_CONTACT_KEY 
inner join yfs_person_info contactpersoninfo on addr.PERSON_INFO_KEY = contactpersoninfo.PERSON_INFO_KEY);
commit;

update yfs_customer_contact set extn_addnl_email_addrs='xpedx.com@gmail.com' where trim(extn_addnl_email_addrs) is not null;
commit;

--12/17/2012 - MOD#2 -- added new update for yfs_organization 
update yfs_organization set EXTN_DIV_EMAIL_PAPER = 'xpedx.com@gmail.com', EXTN_DIV_EMAIL_NON_PAPER = 'xpedx.com@gmail.com';
commit;
--02/15/2013-ML: added new email columns to be updated to generic test email(xpedx.com@gmail.com). 
update yfs_customer set EXTN_CUST_EMAIL_ADDRESS='xpedx.com@gmail.com' WHERE EXTN_CUST_EMAIL_ADDRESS IS NOT NULL;
commit;
update yfs_customer set EXTN_SAMPLE_ROOM_EMAIL_ADDRESS='xpedx.com@gmail.com' WHERE EXTN_SAMPLE_ROOM_EMAIL_ADDRESS IS NOT NULL;
commit;
update yfs_customer set EXTN_ECSR1_EMAIL_ID='xpedx.com@gmail.com'  WHERE EXTN_ECSR1_EMAIL_ID IS NOT NULL;
commit;
update yfs_customer set EXTN_ECSR2_EMAIL_ID='xpedx.com@gmail.com'  WHERE EXTN_ECSR2_EMAIL_ID IS NOT NULL;
commit;
update yfs_customer set EXTN_INVOICE_EMAIL_ID='xpedx.com@gmail.com'  WHERE EXTN_INVOICE_EMAIL_ID IS NOT NULL;
commit;
update yfs_customer set EXTN_CSR_EMAIL_ID='xpedx.com@gmail.com' WHERE EXTN_CSR_EMAIL_ID IS NOT NULL; 
commit;
update yfs_customer set EXTN_SUPPORT_EMAIL_ADDRESS='xpedx.com@gmail.com'  WHERE EXTN_SUPPORT_EMAIL_ADDRESS IS NOT NULL;
commit;
update yfs_customer set EXTN_EDI_EMAIL_ADDRESS='xpedx.com@gmail.com'  WHERE EXTN_EDI_EMAIL_ADDRESS IS NOT NULL;
commit;

--note: must not change pwd on guest user or webchannel will fail to load
-- select * from yfs_user where loginid like 'guest_%' or loginid = 'admin';
--12/17/2012 - Mod#3 -- excluded salesrep logins from the update below.
update yfs_user set IS_PASSWORD_ENCRYPTED='N', password='Password1' where not (loginid like 'guest_%' or loginid = 'admin') and trim(loginid) not in (select trim(user_id) from yfs_customer_contact where EXTN_IS_SALES_REP = 'Y') ; -- THIS MIGHT REQUIRE A SERVER RESTART OF BOTH SMCFS/SWC
commit;

--select * from yfs_user where loginid like '%linemark.com%';  

-- select * from yfs_user where loginid like 'guest%';  
--select IS_PASSWORD_ENCRYPTED, password from yfs_user  where not (loginid like 'guest_%' or loginid = 'admin') and rownum < 100;
-- This query updates the admin and guest_xpedx user's pwd to "password" so that swc works. 

-- update yfs_user set IS_PASSWORD_ENCRYPTED='Y', password='Db+TV4B4UGqj7/GYcCuvsqVL4/6LzuFx3m+mASkT5as=' where loginid like 'guest_%'-- THIS MIGHT REQUIRE A SERVER RESTART OF BOTH SMCFS/SWC
-- commit;

-- get then encrypted pwd and salt from either stg or prod and update below.
-- update yfs_user set IS_PASSWORD_ENCRYPTED='Y', password='Db+TV4B4UGqj7/GYcCuvsqVL4/6LzuFx3m+mASkT5as=', salt='IurFXWZJDGityoBHLgoitA=='  where loginid='admin'; -- THIS MIGHT REQUIRE A SERVER RESTART OF BOTH SMCFS/SWC
-- commit;

-- select IS_PASSWORD_ENCRYPTED, password, salt from yfs_user  where ( loginid = 'admin') and rownum < 100;
-- The below query is used to sync up the order_no sequence after a db restore. 
--it's used as part of the main refresh db script also. but can be used by itself if needed. 
set serveroutput on
declare MaxOrderNo PLS_INTEGER;
MaxOrderNoSequence PLS_INTEGER;
Diff PLS_INTEGER;
begin
  select MAX(replace(ORDER_NO, 'Y', '')) into MaxOrderNo from yfs_order_header;
  dbms_output.put_line ('The value of MaxOrderNo is: ['||MaxOrderNo||']');
  select SEQ_YFS_ORDER_NO.NextVal INTO MaxOrderNoSequence from dual; 
  dbms_output.put_line ('The value of MaxOrderNoSequence is: ['||MaxOrderNoSequence||']');
  dbms_output.put_line ('The value of MaxOrderNo - MaxOrderNoSequence is ['||TO_CHAR(MaxOrderNo - MaxOrderNoSequence)||']');
  Diff := MaxOrderNo - MaxOrderNoSequence;
  
  --IF Diff < 0 THEN
      dbms_output.put_line ('The value of Diff is greater than 0');
      dbms_output.put_line ('The value of Diff is ['||TO_CHAR(Diff)||']');
      EXECUTE IMMEDIATE 'alter sequence SEQ_YFS_ORDER_NO increment by ' || Diff;  
      select SEQ_YFS_ORDER_NO.NextVal INTO MaxOrderNoSequence from dual;
      dbms_output.put_line ('New value of MaxOrderNoSequence is: ['||MaxOrderNoSequence||']');
      EXECUTE IMMEDIATE 'alter sequence SEQ_YFS_ORDER_NO increment by 1' ;
      select SEQ_YFS_ORDER_NO.NextVal INTO MaxOrderNoSequence from dual;
      dbms_output.put_line ('New value of MaxOrderNoSequence is: ['||MaxOrderNoSequence||']');
      
  --ELSE
  --    dbms_output.put_line ('The value of Diff is less than 0');
  -- END IF;
end ;

/

 

--CREATE SEQUENCE SEQ_XPEDX_WEBLINE

-- START WITH     1

--INCREMENT BY   1

-- NOCACHE

-- NOCYCLE;

--commit;


--CREATE SEQUENCE SEQ_XPEDX_UOMSEQ

--START WITH 1

--INCREMENT BY 1

--NOCACHE

--NOCYCLE;

--commit;

 


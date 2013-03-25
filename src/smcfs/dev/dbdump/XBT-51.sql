
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

  CREATE OR REPLACE PROCEDURE INSERT_INVOICEGROUP_LIST AS

 USERKEY YFS_USER.USER_KEY%TYPE;
 USERGROUPKEY YFS_USER_GROUP.USERGROUP_KEY%TYPE;
 CREATEDATE  YFS_USER_GROUP.CREATETS%TYPE := SYSDATE;
 MODIFYDATE  YFS_USER_GROUP.MODIFYTS%TYPE := SYSDATE;
 CREATEUSERID YFS_USER_GROUP.CREATEUSERID%TYPE := 'admin'; 
 MODIFYUSERID YFS_USER_GROUP.MODIFYUSERID%TYPE := 'admin';
 CREATEPROGID YFS_USER_GROUP.CREATEPROGID%TYPE := 'dbscript'; 
 MODIFYPROGID YFS_USER_GROUP.MODIFYPROGID%TYPE := 'dbscript'; 
 LOCKID YFS_USER_GROUP.LOCKID%TYPE :=0;

 
 cursor cur is 
     SELECT trim(USER_KEY) as USERKEY FROM YFS_USER where EXTN_USER_TYPE ='INTERNAL' AND ORGANIZATION_KEY='xpedx' AND DATA_SECURITY_GROUP_ID LIKE 'Team%'
     AND USER_KEY  NOT IN (SELECT UGL.USER_KEY FROM YFS_USER_GROUP_LIST UGL, YFS_USER_GROUP UG 
     where UGL.USERGROUP_KEY = UG.USERGROUP_KEY AND UG.USERGROUP_ID='XPXViewInvoicesGroup');
        
       
    BEGIN
       SELECT USERGROUP_KEY  INTO USERGROUPKEY   FROM YFS_USER_GROUP  WHERE USERGROUP_ID = 'XPXViewInvoicesGroup';
      open cur;
          loop
           fetch cur into USERKEY;
           exit when cur%notfound;
             INSERT INTO YFS_USER_GROUP_LIST VALUES ('2005'||to_char(systimestamp, 'yyyyddmmhh24missFF')||TRUNC(DBMS_RANDOM.VALUE (11, 99)),trim(USERKEY),
             trim(USERGROUPKEY),LOCKID,CREATEDATE,MODIFYDATE,CREATEUSERID,MODIFYUSERID,CREATEPROGID,MODIFYPROGID,null);
          end loop;
      commit;
     CLOSE cur;
   END INSERT_INVOICEGROUP_LIST;
/
 

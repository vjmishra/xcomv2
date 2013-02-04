Insert into XPX_EMAIL_DEFN (EMAIL_XSL_PATH,EMAIL_TYPE,EMAIL_TO_XPATH,EMAIL_CC_XPATH,EMAIL_BCC_XPATH) values ('../extensions/global/template/email/XPX_Order_Confirmation.xsl','ORDER_CONFIRMATION_EMAIL','/Order/@strToEmailid','/Order/Extn/@ExtnAddnlEmailAddr,/Order/@strExtnECsr1EMailID,/Order/@strExtnECsr2EMailID,/Order/@strCustomerAdminEmailList','');

Insert into XPX_EMAIL_DEFN (EMAIL_XSL_PATH,EMAIL_TYPE,EMAIL_TO_XPATH,EMAIL_CC_XPATH,EMAIL_BCC_XPATH) values ('../extensions/global/template/email/YCD_Password_Reset_Success_Email.xsl','USER_NOTIFICATION_EMAIL','/User/ContactPersonInfo/@EMailID','','');

Insert into XPX_EMAIL_DEFN (EMAIL_XSL_PATH,EMAIL_TYPE,EMAIL_TO_XPATH,EMAIL_CC_XPATH,EMAIL_BCC_XPATH) values ('../extensions/global/template/email/YCD_Password_Reset_Success_Email.xsl','USER_RESET_PASSWORD_EMAIL','/User/ContactPersonInfo/@EMailID','','');

Insert into XPX_EMAIL_DEFN (EMAIL_XSL_PATH,EMAIL_TYPE,EMAIL_TO_XPATH,EMAIL_CC_XPATH,EMAIL_BCC_XPATH) values ('../extensions/global/template/email/YCD_Password_Reset_Success_Email.xsl','USER_CHANGE_PASSWORD_EMAIL','/User/ContactPersonInfo/@EMailID','','');

Insert into XPX_EMAIL_DEFN (EMAIL_XSL_PATH,EMAIL_TYPE,EMAIL_TO_XPATH,EMAIL_CC_XPATH,EMAIL_BCC_XPATH) values ('../extensions/global/template/email/newUser_email_CSR.xsl','NEW_USER_REGISTRATION','/NewUser/@ToEmailId','/NewUser/@CCEmailId','');

Insert into XPX_EMAIL_DEFN (EMAIL_XSL_PATH,EMAIL_TYPE,EMAIL_TO_XPATH,EMAIL_CC_XPATH,EMAIL_BCC_XPATH) values ('../extensions/global/template/email/XPX_Order_NeedsApproval.xsl','ORDER_PENDING_APPROVAL_EMAIL','/Order/CustomerContactList/CustomerContact/@ToEmailID','/Order/CustomerContactList/CustomerContact/@CCEmailID,/Order/Extn/@ExtnAddnlEmailAddr','');

Insert into XPX_EMAIL_DEFN (EMAIL_XSL_PATH,EMAIL_TYPE,EMAIL_TO_XPATH,EMAIL_CC_XPATH,EMAIL_BCC_XPATH) values ('../extensions/global/template/email/XPX_Order_Approved.xsl','ORDER_APPROVED_EMAIL','/Order/CustomerContactList/CustomerContact/@EmailID','/Order/CustomerContactList/CustomerContact/@CCEmailID,/Order/Extn/@ExtnAddnlEmailAddr','');

Insert into XPX_EMAIL_DEFN (EMAIL_XSL_PATH,EMAIL_TYPE,EMAIL_TO_XPATH,EMAIL_CC_XPATH,EMAIL_BCC_XPATH) values ('../extensions/global/template/email/XPX_Order_ApprovedRejected.xsl','ORDER_REJECTED_EMAIL','/Order/CustomerContactList/CustomerContact/@ToEmailID','/Order/CustomerContactList/CustomerContact/@CCEmailID','');

Insert into XPX_EMAIL_DEFN (EMAIL_XSL_PATH,EMAIL_TYPE,EMAIL_TO_XPATH,EMAIL_CC_XPATH,EMAIL_BCC_XPATH) values ('../extensions/global/template/email/ItemReturnRequest.xsl','ITEM_RETURN_REQUEST_EMAIL','/Emails/Email/@ToEmail','/Emails/Email/@InputEmail','');
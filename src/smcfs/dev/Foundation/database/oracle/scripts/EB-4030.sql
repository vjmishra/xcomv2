UPDATE XPX_EMAIL_DEFN SET 
EMAIL_CC_XPATH='/Order/CustomerContactList/CustomerContact/@CCEmailID,/Order/Extn/@ExtnAddnlEmailAddr,/Order/@strExtnECsr1EMailID,/Order/@strExtnECsr2EMailID,/Order/Extn/@ExtnSalesRepEmail' WHERE 
EMAIL_TYPE='ORDER_APPROVED_EMAIL';
COMMIT;

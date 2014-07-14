package com.xpedx.nextgen.common.util;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.order.XPXBeforeChangeOrderUE;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXEmailUtil {
	private static YIFApi api = null;
	private static final Logger log = Logger.getLogger(XPXEmailUtil.class);
	public static final String ORDER_CONFIRMATION_EMAIL_TYPE="ORDER_CONFIRMATION_EMAIL";
	public static final String ORDER_PENDING_APPROVAL_EMAIL_TYPE="ORDER_PENDING_APPROVAL_EMAIL";
	public static final String ORDER_APPROVED_EMAIL_TYPE="ORDER_APPROVED_EMAIL";
	public static final String ORDER_REJECTED_EMAIL_TYPE="ORDER_REJECTED_EMAIL";
    public static final String USER_NOTIFICATION_EMAIL_TYPE="USER_NOTIFICATION_EMAIL";
    public static final String USER_RESET_PASSWORD_EMAIL_TYPE="USER_RESET_PASSWORD_EMAIL";
    public static final String USER_CHANGE_PASSWORD_EMAIL_TYPE="USER_CHANGE_PASSWORD_EMAIL";
    public static final String NEW_USER_REGISTRATION_EMAIL_TYPE="NEW_USER_REGISTRATION";
    public static final String RETURN_ITEMS_REQUEST_EMAIL_TYPE="RETURN_ITEMS_REQUEST_EMAIL";
    public static final String USER_PROFILE_UPDATED_NOTIFICAON="USER_PROFILE_UPDATED_NOTIFICAON";
    public static final String REGISTRATION_REQUEST_NOTIFICATION="Registration Request Notification";
    
   // public static final String ORDER_CONFIRMATION_EMAIL_SUBJECT=".com Order Submitted Notification";
    public static final String RETURN_ITEMS_REQUEST_EMAIL_SUBJECT="Item Return Request";
    
    public static final String EMAIL_TRANSPORT_PROTOCOL="mail.transport.protocol";
    public static final String SMTP_EMAIL_HOST="mail.smtp.host";
    public static final int SMTP_SUCCESS_RETURN_CODE=250;
    

	public static void insertEmailDetailsIntoDB(YFSEnvironment env,  String emailXML,  String emailType,
											    String emailSubject, String emailFrom, String emailOrgCode , String businessIdentifier) throws Exception
	{
		if(log.isDebugEnabled()){
			log.debug("Begin : XPXEmailUtil.insertEmailDetailsIntoDB() method. Email Type is ["+emailType+"]. Input XML is "+emailXML);
		}
		YFCDocument emailInputDoc=YFCDocument.createDocument("XPXEmailDetails");		
		YFCElement emailDetail=emailInputDoc.getDocumentElement();
		emailDetail.setAttribute("EmailType", emailType);
		emailDetail.setAttribute("EmailSubject", emailSubject);
		emailDetail.setAttribute("EmailXML", emailXML);
		emailDetail.setAttribute("EmailOrgCode", emailOrgCode);
		emailDetail.setAttribute("EmailFrom", emailFrom);	
		emailDetail.setAttribute("BusinessIdentifier", businessIdentifier);
		
		api = YIFClientFactory.getInstance().getApi();
		api.executeFlow(env,"createXPXEmailDetails", emailInputDoc.getDocument());
		
		if(log.isDebugEnabled()){
			log.debug("End : XPXEmailUtil.insertEmailDetailsIntoDB() method. "+emailType+ "Email XML inserted successfully in DB");
		}
	}
}

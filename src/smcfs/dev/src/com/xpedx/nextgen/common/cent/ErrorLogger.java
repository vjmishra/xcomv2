package com.xpedx.nextgen.common.cent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.tools.datavalidator.XmlUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.xpedx.nextgen.common.util.XPXLiterals;

public class ErrorLogger {

	

	/**
	 * Instance of YIFApi used to invoke Yantra APIs or services.
	 */
	private static YIFApi api = null;
	private static YFCLogCategory yfcLogCatlog;
	
	static {
		/* Please do not change anything here this is very specific to cent logging  and if the getlogger path going to change this will be conflict with log4jconfig.custom.xml
		   cent_appender entry for logging */
		yfcLogCatlog = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.cent");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (Exception e) {
			// TO DO
		}
	}

	public static void log(Error errorObj, YFSEnvironment yfsEnv) {
		/* get a pojo with error class,trans type, exception object, input xml in case, error class is EoF or SoF, log to the log file
		   else, if error class is Application or UnknownError orUnexpectedInvalid, log to the file as well as the new table with i/p xml and exception

		   InXML to XPXGetErrorLookupListService :
		   <XPXErrorLookup ErrorClass="Unknown Error" SourceSystem="Sterling" TransType="App"/>  */
		try {
			
			Random random = new Random(); 
			long fraction = (long)(1 * Math.abs(random.nextLong())); 
			String randomNumber=""+fraction;
			String userID = yfsEnv.getUserId();
			String progID = yfsEnv.getProgId();
			
			YFCDocument envDoc = YFCDocument.createDocument("YFSEnvironment");	
			YFCElement eleEnv = envDoc.getDocumentElement();
			eleEnv.setAttribute("userId", userID);
			eleEnv.setAttribute("progId", progID);
			
			YIFApi api = YIFClientFactory.getInstance().getApi();
			YFSEnvironment newEnv = api.createEnvironment(envDoc.getDocument());
			
			Document getErrorLookupInDoc = XmlUtils.createDocument("XPXErrorLookup");
			Element xpxErrorLookupRootElem = getErrorLookupInDoc.getDocumentElement();
			if (errorObj != null) {
				xpxErrorLookupRootElem.setAttribute("ErrorClass", errorObj.getErrorClass());
				xpxErrorLookupRootElem.setAttribute("SourceSystem", "Sterling");
				xpxErrorLookupRootElem.setAttribute("TransType", errorObj.getTransType());
				
				Document getErrorLookupOutputDoc = api.executeFlow(yfsEnv,"XPXGetErrorLookupListService", getErrorLookupInDoc);
				Element outputRootElement = getErrorLookupOutputDoc.getDocumentElement();
	
				// Field to hold value of the total number of Elements in response
				String strTotalRecords = null;
				if (outputRootElement != null) {
					strTotalRecords = outputRootElement.getAttribute("TotalNumberOfRecords");
					int intTotalRecords = 0;
					if (strTotalRecords != null && !strTotalRecords.equalsIgnoreCase("")) {
						intTotalRecords = Integer.parseInt(strTotalRecords);
					}
	
					// Check if record exists
					if (intTotalRecords > 0) {
						Element xpxErrorElem = (Element) outputRootElement.getElementsByTagName("XPXErrorLookup").item(0);
						if (xpxErrorElem != null) {
							
							StringBuffer logString = new StringBuffer(xpxErrorElem.getAttribute("TargetSystem"));
							logString.append("|");
							logString.append(xpxErrorElem.getAttribute("SourceSystem"));
							logString.append("|");
							logString.append(xpxErrorElem.getAttribute("TransType"));
							logString.append("|");						
							logString.append(checkDateTimeCommMethod(xpxErrorElem.getAttribute("CommMethod"),xpxErrorElem.getAttribute("StartDownTime"),xpxErrorElem.getAttribute("EndDownTime"),xpxErrorElem.getAttribute("DownTimeNotification")));
							logString.append("|");
							logString.append(xpxErrorElem.getAttribute("QueueName"));
							logString.append("|");
							logString.append(xpxErrorElem.getAttribute("ErrorClass"));
							logString.append("|");
							logString.append("ExceptionID="+randomNumber);
							logString.append(" "+errorObj.getException());											
							yfcLogCatlog.error(logString.toString());
						}
					}
				}
				
				// To Populate The Exception Table
				if (errorObj.getErrorClass() != null && !errorObj.getErrorClass().equalsIgnoreCase("")) {
					if (errorObj.getErrorClass().equalsIgnoreCase("Unknown Error")
							|| errorObj.getErrorClass().equalsIgnoreCase("Unexpected / Invalid")
							|| errorObj.getErrorClass().equalsIgnoreCase("Application") 
							|| errorObj.getErrorClass().equalsIgnoreCase("EOF") 
							|| errorObj.getErrorClass().equalsIgnoreCase("SOF")||errorObj.getErrorClass().equalsIgnoreCase(XPXLiterals.CUSTOMER_ERROR_CLASS)||errorObj.getErrorClass().equalsIgnoreCase(XPXLiterals.ITEM_ERROR_CLASS)) {
						populateNotificationTable(errorObj, newEnv,randomNumber);
					}
				}
			}
		} catch (Exception e) {
			yfcLogCatlog.error(e.getMessage());
		}
	}
    


	private static String checkDateTimeCommMethod(String theCommMethodVal,String startDownTime , String endDownTime , String downtimeNotification) {
		
		String commMethod = theCommMethodVal;
		try {
			Calendar calendar = Calendar.getInstance();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date nowdate = new Date();
			Date startoutage = dateFormat.parse(startDownTime);
			Date endoutage = dateFormat.parse(endDownTime);
			int theHour = calendar.get(calendar.HOUR_OF_DAY) ;
			if(startoutage!=null && (startoutage.after(nowdate)&& endoutage.after(nowdate))) {
				// Execute Code of Block for Planned Downtime
				commMethod=downtimeNotification;
			} else {
				// Execute Code of Block for UnPlanned Downtime
				if(calendar.get(calendar.DAY_OF_WEEK) == 7) {
					if(theHour >= 18 && theHour <= 23) {
						commMethod = "P3";
					}
				}				
			}
		} catch (Exception e){
			yfcLogCatlog.error(e.getMessage());			
		}
		return commMethod;		
	}
	
	public static void populateNotificationTable(Error errorObj,YFSEnvironment yfsEnv,String randomNumber) {
		
		try {
			Document createErrorNotificatnInDoc = XmlUtils.createDocument("XPXErrorNotification");
			Element xpxErrorNotifyRootElem = createErrorNotificatnInDoc.getDocumentElement();
			String exceptionMessage = null;
			if (errorObj != null) {
				if (errorObj.getException() != null && errorObj.getException().getMessage() != null) {
					exceptionMessage = errorObj.getExceptionMessage();
					if (YFCObject.isNull(exceptionMessage) || YFCObject.isVoid(exceptionMessage)) {
						// Handle the case where stack reaches more than 3800 char
						if(errorObj.getException().getMessage().length() > 3800) {
							String StackTrace=((String)errorObj.getException().getMessage());
							StackTrace=StackTrace.substring(0, 3800);					
							xpxErrorNotifyRootElem.setAttribute("StackTrace",StackTrace);
						} else {
							xpxErrorNotifyRootElem.setAttribute("StackTrace",errorObj.getException().getMessage());
						}
					} else {
						// To Set Only Exception Message For Order Update/Order Edit Interface.
						xpxErrorNotifyRootElem.setAttribute("StackTrace",exceptionMessage);
					}
				} else if(null != errorObj.getErrorDesc()) {
					xpxErrorNotifyRootElem.setAttribute("StackTrace",errorObj.getErrorDesc());
				} else {
					xpxErrorNotifyRootElem.setAttribute("StackTrace","");
				}
				
				if(errorObj.getInputDoc() != null){	
					if(XmlUtils.getString(errorObj.getInputDoc()).length()>3800) {
						String XmlFile=XmlUtils.getString(errorObj.getInputDoc()).substring(0, 3800);
						xpxErrorNotifyRootElem.setAttribute("XmlFile",XmlFile);
					} else {
						xpxErrorNotifyRootElem.setAttribute("XmlFile",XmlUtils.getString(errorObj.getInputDoc()));
					}
				} else {
					xpxErrorNotifyRootElem.setAttribute("XmlFile","");
				}
				
				xpxErrorNotifyRootElem.setAttribute("ExceptionID",randomNumber);
				Document createErrorNotificatnOutputDoc = api.executeFlow(yfsEnv, "XPXCreateErrorNotificatnService",createErrorNotificatnInDoc);
			}
		} catch (Exception e) {
			yfcLogCatlog.error(e.getMessage());
		}
	}
}

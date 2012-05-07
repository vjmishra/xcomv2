package com.xpedx.nextgen.common.cent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.lucene.util.ToStringUtils;
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

public class ErrorLogger {

	/**
	 * Instance of YIFApi used to invoke Yantra APIs or services.
	 */
	private static YIFApi api = null;
	private static YFCLogCategory yfcLogCatlog;
	//Commenting this line as one logger is enough and appenders can be controlled in config file.
	//private static Logger log4jLogger;
	static {
		
		//Please do not change anything here this is very specific to cent logging  and if the getlogger path going to change this will be conflict with log4jconfig.custom.xml
		//cent_appender entry for logging
				yfcLogCatlog = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.cent");
		//Commenting this line as one logger is enough and appenders can be controlled in config file.
		//log4jLogger = YFCLogCategory.getLogger("cent");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (Exception e) {
			// TO DO
			// logger.error("IOM_UTIL_0001", e);
		}
	}

	public static void log(Error errorObj, YFSEnvironment yfsEnv)
			 {
		// get a pojo with error class,trans type, exception object, input xml
		// in case, error class is EoF or SoF, log to the log file
		// else, if error class is Application or UnknownError or
		// UnexpectedInvalid, log to the file as well as the new table with i/p
		// xml and exception

		// i/p XML to XPXGetErrorLookupListService

		// <XPXErrorLookup ErrorClass="Unknown Error" SourceSystem="Sterling"
		// TransType="App"/>
		try{
			
			//SAYAN - START
			
			String userID = yfsEnv.getUserId();
			String progID = yfsEnv.getProgId();
			
			YFCDocument envDoc = YFCDocument.createDocument("YFSEnvironment");	
			YFCElement eleEnv = envDoc.getDocumentElement();
			eleEnv.setAttribute("userId", userID);
			eleEnv.setAttribute("progId", progID);
			
			YIFApi api = YIFClientFactory.getInstance().getApi();
			YFSEnvironment newEnv = api.createEnvironment(envDoc.getDocument());
			
			
		Document getErrorLookupInDoc = XmlUtils
				.createDocument("XPXErrorLookup");
		Element xpxErrorLookupRootElem = getErrorLookupInDoc
				.getDocumentElement();
		if (errorObj != null) {
			xpxErrorLookupRootElem.setAttribute("ErrorClass", errorObj
					.getErrorClass());
			xpxErrorLookupRootElem.setAttribute("SourceSystem", "Sterling");
			xpxErrorLookupRootElem.setAttribute("TransType", errorObj
					.getTransType());

			/*yfcLogCatlog.debug("XPXGetErrorLookupListService i/p Doc"
					+ XmlUtils.getString(getErrorLookupInDoc));
			log4jLogger.debug("XPXGetErrorLookupListService i/p Doc"
					+ XmlUtils.getString(getErrorLookupInDoc));*/
			
			Document getErrorLookupOutputDoc = api.executeFlow(yfsEnv,
					"XPXGetErrorLookupListService", getErrorLookupInDoc);
			/*yfcLogCatlog.debug("XPXGetErrorLookupListService o/p Doc"
					+ XmlUtils.getString(getErrorLookupOutputDoc));
			log4jLogger.debug("XPXGetErrorLookupListService o/p Doc"
					+ XmlUtils.getString(getErrorLookupOutputDoc));*/
			
			Element outputRootElement = getErrorLookupOutputDoc
					.getDocumentElement();

			// Field to hold value of the total number of Elements in response
			String strTotalRecords = null;

			if (outputRootElement != null) {
				strTotalRecords = outputRootElement
						.getAttribute("TotalNumberOfRecords");
				int intTotalRecords = 0;
				if (strTotalRecords != null
						&& !strTotalRecords.equalsIgnoreCase("")) {
					intTotalRecords = Integer.parseInt(strTotalRecords);
				}

				// Check if record exists
				if (intTotalRecords > 0) {
					Element xpxErrorElem = (Element) outputRootElement
							.getElementsByTagName("XPXErrorLookup").item(0);
					/*yfcLogCatlog.debug("Total no of records > 0");
					log4jLogger.debug("Total no of records > 0");*/
					if (xpxErrorElem != null) {
						/*yfcLogCatlog.debug(XmlUtils.getString(xpxErrorElem));
						log4jLogger.debug(XmlUtils.getString(xpxErrorElem));*/
						/*String logString = xpxErrorElem
								.getAttribute("TargetSystem")
								+ "|"
								+ xpxErrorElem.getAttribute("SourceSystem")
								+ "|"
								+ xpxErrorElem.getAttribute("TransType")
								+ "|"
								+ xpxErrorElem.getAttribute("CommMethod")
								+ "|"
								+ xpxErrorElem.getAttribute("QueueName")
								+ "|"
								+ xpxErrorElem.getAttribute("ErrorClass")
						        + "|"
						        + errorObj.getErrorDesc()
						        + "|"
						        + errorObj.getException(); */
						        
						
						StringBuffer logString = new StringBuffer(xpxErrorElem.getAttribute("TargetSystem"));
						logString.append("|");
						logString.append(xpxErrorElem.getAttribute("SourceSystem"));
						logString.append("|");
						logString.append(xpxErrorElem.getAttribute("TransType"));
						logString.append("|");						
						//logString.append(xpxErrorElem.getAttribute("CommMethod"));
						logString.append(checkDateTimeCommMethod(xpxErrorElem.getAttribute("CommMethod")));
						logString.append("|");
						logString.append(xpxErrorElem.getAttribute("QueueName"));
						logString.append("|");
						logString.append(xpxErrorElem.getAttribute("ErrorClass"));
						logString.append("|");
						logString.append(errorObj.getException());
						
						     
						/*yfcLogCatlog.debug("yfcLogCatalog.debug");
						yfcLogCatlog.info("yfcLogCatlog.info");
						yfcLogCatlog.error("yfcLogCatlog.error");
						yfcLogCatlog.verbose("yfcLogCatlog.verbose");
						log4jLogger.info("log4jLogger.info");
						log4jLogger.debug("log4jLogger.debug");
						log4jLogger.error("log4jLogger.error");*/

						/*yfcLogCatlog.debug(logString);
						yfcLogCatlog.info(logString);*/
						yfcLogCatlog.error(logString.toString());
						/*yfcLogCatlog.verbose(logString);
						log4jLogger.info(logString);
						log4jLogger.debug(logString);
						log4jLogger.error(logString);*/

					}
				}

			}
			// populate the Exception table
			if (errorObj.getErrorClass() != null
					/** EOF/SOF conditions added by Arun Sekhar on 31-Jan-2011 for CENT tool integration **/
					&& !errorObj.getErrorClass().equalsIgnoreCase("")) {
				if (errorObj.getErrorClass().equalsIgnoreCase("Unknown Error")
						|| errorObj.getErrorClass().equalsIgnoreCase(
								"Unexpected / Invalid")
						|| errorObj.getErrorClass().equalsIgnoreCase(
								"Application") || errorObj.getErrorClass().equalsIgnoreCase(
								"EOF") || errorObj.getErrorClass().equalsIgnoreCase(
								"SOF")) {
					populateNotificationTable(errorObj, newEnv);
				}
			}

		}
		}catch(Exception e){
			//Commenting this line as one logger is enough and appenders can be controlled in config file.
			//log4jLogger.error(e.getMessage());
			yfcLogCatlog.error(e.getMessage());
		}
	}
    
	//Product Support Readiness, Fix for Jira 2691.
	private static String checkDateTimeCommMethod(String theCommMethodVal)
	{
		String commMethod = theCommMethodVal;
		try
		{
			Calendar calendar = Calendar.getInstance();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String dateNow = dateFormat.format(calendar.getTime());
			
			int theHour = calendar.get(calendar.HOUR_OF_DAY) ;
			
			if(commMethod != null && commMethod.equalsIgnoreCase("P1"))
			{
				if(calendar.get(calendar.DAY_OF_WEEK) == 7)
				{
					if(theHour >= 18 && theHour <= 23)
					{
						commMethod = "P3";
					}
				}				
			}
		}
		catch (Exception e){
			//Commenting this line as one logger is enough and appenders can be controlled in config file.
			//log4jLogger.error(e.getMessage());
			yfcLogCatlog.error(e.getMessage());			
		}
		return commMethod;		
	}
	
	public static void populateNotificationTable(Error errorObj,
			YFSEnvironment yfsEnv) {
		try {
			Document createErrorNotificatnInDoc = XmlUtils.createDocument("XPXErrorNotification");
			Element xpxErrorNotifyRootElem = createErrorNotificatnInDoc
					.getDocumentElement();
			String exceptionMessage = null;
			if (errorObj != null) {
				if (errorObj.getException() != null
						&& errorObj.getException().getMessage() != null) {
					exceptionMessage = errorObj.getExceptionMessage();
					if (YFCObject.isNull(exceptionMessage) || YFCObject.isVoid(exceptionMessage)) {
						//handle the case where stack reaches more than 3800 char
						if(errorObj.getException().getMessage().length()>3800) {
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
					 
					 /** Added by Arun Sekhar on 31-Jan-20100 for CENT tool logging of EOF/SOF messages **/
				}else if(null != errorObj.getErrorDesc()){
					xpxErrorNotifyRootElem.setAttribute("StackTrace",errorObj.getErrorDesc());
				}
				else{
					xpxErrorNotifyRootElem.setAttribute("StackTrace","");
				}
				
				if(errorObj.getInputDoc() != null){	
					if(XmlUtils.getString(errorObj.getInputDoc()).length()>3800)
					{
						String XmlFile=XmlUtils.getString(errorObj.getInputDoc()).substring(0, 3800);
						xpxErrorNotifyRootElem.setAttribute("XmlFile",XmlFile);
					}
					else
					{
					xpxErrorNotifyRootElem.setAttribute("XmlFile",XmlUtils.getString(errorObj.getInputDoc()));
					}
				}
				else{
					xpxErrorNotifyRootElem.setAttribute("XmlFile","");
				}
				Document createErrorNotificatnOutputDoc = api.executeFlow(
						yfsEnv, "XPXCreateErrorNotificatnService",
						createErrorNotificatnInDoc);
				
			}
		} catch (Exception e) {
			yfcLogCatlog.error(e.getMessage());
			//Commenting this line as one logger is enough and appenders can be controlled in config file.
			//log4jLogger.error(e.getMessage());
		}
	}

	// public static void main (String a[]){
	// ErrorLogger errorLogger = new ErrorLogger();
	// errorLogger.loger();
	// }
	//	
	// public void loger(){
	// yfcLogCatlog.debug("yfcLogCatalog.debug");
	// yfcLogCatlog.info("yfcLogCatlog.info");
	// yfcLogCatlog.error("yfcLogCatlog.error");
	// yfcLogCatlog.verbose("yfcLogCatlog.verbose");
	// log4jLogger.info("log4jLogger.info");
	// log4jLogger.debug("log4jLogger.debug");
	// log4jLogger.error("log4jLogger.error");
	// }
}

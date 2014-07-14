<%@ page language="java" errorPage="errorPage4Download.jsp" import="com.businessobjects.rebean.wi.*,com.businessobjects.adv_ivcdzview.*,java.util.*"%><jsp:useBean id="objUtils" class="com.businessobjects.adv_ivcdzview.Utils" scope="application" /><jsp:useBean id="objUserSettings" class="com.businessobjects.adv_ivcdzview.UserSettings" scope="session" /><jsp:useBean id="requestWrapper" class="com.businessobjects.adv_ivcdzview.RequestWrapper" scope="page" /><%
response.reset();
response.setDateHeader("expires", 0);
requestWrapper.onStart(request);
requestWrapper.setCharacterEncoding("UTF-8");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strDocType = requestWrapper.getQueryParameter("doctype", true);
String strViewType = requestWrapper.getQueryParameter("viewType", false, "C");
boolean blnSafari = false;
String strUserAgent = request.getHeader("User-Agent");
strUserAgent = strUserAgent.toLowerCase();
if (strUserAgent.indexOf("safari") > -1) blnSafari = true;
String eol = "\r\n";
String vd = "\"";
String cs = ",";
String strCharset = null;
DHTMLLogger _logger = Utils.getLogger("com.businessobjects.dhtml." + requestWrapper.getCurrentPageName());
if (strViewType.equalsIgnoreCase("COp"))
{
_logger.info("strViewType.equalsIgnoreCase(\"COp\")");
String strParams = (String) session.getAttribute("CDZ." + strViewerID + ".CSV");
_logger.info("strParams:" + strParams);
String[] arrParams = strParams.split(" ");
vd = arrParams[0];
cs = arrParams[1];
cs = cs.equals("Tab")?"\t":cs;
if (arrParams.length > 2) {
strCharset = arrParams[2];
}
}
else if (objUserSettings.getUserProfile("CDZ_VIEW_CSV_DefaultValues", "").equals("true"))
{
_logger.info("CDZ_VIEW_CSV_DefaultValues=true");
vd = objUserSettings.getUserProfile("CDZ_VIEW_CSV_CharDelimiter");
cs = objUserSettings.getUserProfile("CDZ_VIEW_CSV_ColumnSeparator");
cs = cs.equals("Tab")?"\t":cs;
strCharset = objUserSettings.getUserProfile("CDZ_VIEW_CSV_Charset");
}
else
{
_logger.info("Get Charset according to the current Locale and Operating System");
String[] arrOS = ViewerTools.split(objUtils.objConfig.getProperty("locale", ""), ",");
String strCurOS = objUtils.objConfig.getProperty("os.name", "windows");
int iIndex = 0;
for (int i=0; i<arrOS.length; i++)
{
if (strCurOS.equalsIgnoreCase(arrOS[i]))
{
iIndex = i;
break;
}
}
String strLocal = (String)session.getAttribute(ViewerTools.SessionLanguage);
strCharset = objUtils.objConfig.getProperty(strLocal, "UTF-8");
String[] arrCharset = ViewerTools.split(strCharset, ",");
if (iIndex < arrCharset.length)
strCharset = arrCharset[iIndex];
if (strCharset.equals("")) strCharset = "UTF-8";
}
String strCsvContentType = "text/csv;charset=";
if (blnSafari) strCsvContentType = "application/csv;charset=";
ReportEngines reportEngines = (ReportEngines)session.getAttribute(ViewerTools.SessionReportEngines);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
String strDocName = doc.getProperties().getProperty(PropertiesType.NAME, "");
String strLanguage = (String)session.getAttribute(ViewerTools.SessionLanguage);
if (strLanguage.equals("ja") && strDocName.length() > 12)
strDocName = strDocName.substring(0,11);
CSVView objCSVView = null;
DataProviders arrDataProviders = doc.getDataProviders();
out.clearBuffer();
if (strDocType.equalsIgnoreCase("wid"))
{
objCSVView = (CSVView)arrDataProviders.getView(OutputFormatType.CSV);
    objCSVView.setCharDelimiter(vd);
    objCSVView.setColumnSeparator(cs);
    objCSVView.setEndOfLine(eol);
try
{
objCSVView.setCharset(strCharset);
response.setContentType(strCsvContentType + strCharset);
response.setHeader("Content-Disposition", "attachment;filename=\"" + ViewerTools.encodeDocName4Download(strDocName) + ".csv\"");
objCSVView.getContent(out);
}
catch(Exception e)
{
response.reset();
response.setContentType("text/html;charset=UTF-8");
throw e;
}
}
else
{
if (strDocType.equalsIgnoreCase("rep"))
strCharset = "UTF-8";
boolean blnStartTransfer = false;
int iNbDP = arrDataProviders.getCount();
for (int i=0; i<iNbDP; i++)
{
DataProvider objDP = arrDataProviders.getItem(i);
for (int j=0; j<objDP.getFlowCount(); j++)
{
objCSVView = objDP.getResultAsCSV(j);
objCSVView.setCharDelimiter(vd);
objCSVView.setColumnSeparator(cs);
objCSVView.setEndOfLine(eol);
objCSVView.setCharset(strCharset);
if (!blnStartTransfer)
{
response.setContentType(strCsvContentType + strCharset);
response.setHeader("Content-Disposition", "attachment;filename=\"" + ViewerTools.encodeDocName4Download(strDocName) + ".csv\"");
blnStartTransfer = true;
}
objCSVView.getContent(out);
out.flush();
}
}
}
objCSVView = null;
doc = null;
%>

<%@ page language="java" contentType="application/pdf" errorPage="errorPage4Download.jsp" import="com.businessobjects.rebean.wi.*,com.businessobjects.adv_ivcdzview.*,java.util.*"%><jsp:useBean id="objUtils" class="com.businessobjects.adv_ivcdzview.Utils" scope="application" /><jsp:useBean id="objUserSettings" class="com.businessobjects.adv_ivcdzview.UserSettings" scope="session" /><jsp:useBean id="requestWrapper" class="com.businessobjects.adv_ivcdzview.RequestWrapper" scope="page" /><%
response.reset();
response.setDateHeader("expires", 0);
requestWrapper.onStart(request);
requestWrapper.setCharacterEncoding("UTF-8");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewType = requestWrapper.getQueryParameter("viewType", true);
String strSaveReport = requestWrapper.getQueryParameter("saveReport", false, "N");
DHTMLLogger _logger = Utils.getLogger("com.businessobjects.dhtml." + requestWrapper.getCurrentPageName());
_logger.info("viewType=" + strViewType);
if (strViewType.equals("P"))
{
String theAgent = request.getHeader("user-agent");
if (null == theAgent) theAgent = "";
if (theAgent.equalsIgnoreCase("contype"))
{
response.setHeader("Content-Type", "application/pdf");
response.setStatus(HttpServletResponse.SC_OK);
return;
}
}
ReportEngines reportEngines = (ReportEngines)session.getAttribute(ViewerTools.SessionReportEngines);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
BinaryView objBinaryView = null;
String strContentType = "";
String strDocExt = "";
Reports arrReports = null;
Report objReport = null;
if(strSaveReport.equalsIgnoreCase("Y"))
{
String iReport = requestWrapper.getQueryParameter("iReport", true);
int nReportIndex = Integer.parseInt(iReport);
arrReports = doc.getReports();
objReport = arrReports.getItem(nReportIndex);
objReport.setPaginationMode(PaginationMode.Listing);
}
if (strViewType.equalsIgnoreCase("P"))
{
strDocExt = ".pdf";
strContentType = "application/pdf";
if (request.getHeader("User-Agent").equalsIgnoreCase("contype"))
{
response.setHeader("Content-Type", strContentType);
return;
}
if(strSaveReport.equalsIgnoreCase("Y"))
{
objBinaryView = (BinaryView)objReport.getView(OutputFormatType.PDF);
_logger.info(" download report as PDF");
}
else
{
objBinaryView = (BinaryView)doc.getView(OutputFormatType.PDF);
_logger.info(" download document as PDF");
}
}
else if (strViewType.equalsIgnoreCase("X"))
{
strDocExt = ".xls";
strContentType = "application/vnd.ms-excel";
if(strSaveReport.equalsIgnoreCase("Y"))
{
objBinaryView = (BinaryView)objReport.getView(OutputFormatType.XLS);
_logger.info(" download report as Excel");
}
else
{
objBinaryView = (BinaryView)doc.getView(OutputFormatType.XLS);
_logger.info(" download document as Excel");
}
}
else if (strViewType.equalsIgnoreCase("O"))
{
strDocExt = ".xls";
strContentType = "application/vnd.ms-excel";
if(strSaveReport.equalsIgnoreCase("Y"))
{
objBinaryView = (BinaryView)objReport.getView(OutputFormatType.XLSDataCentric);
_logger.info(" download report as Optimized Excel");
}
else
{
objBinaryView = (BinaryView)doc.getView(OutputFormatType.XLSDataCentric);
_logger.info(" download document as Optimized Excel");
}
}
else
{
String strMsg = "Unknown document view type: " + strViewType;
throw new Exception(strMsg);
}
String strDocName = doc.getProperties().getProperty(PropertiesType.NAME, "");
String strLanguage = (String)session.getAttribute(ViewerTools.SessionLanguage);
if (strLanguage.equals("ja") && strDocName.length() > 12)
strDocName = strDocName.substring(0,11);
int iLength = objBinaryView.getContentLength();
response.setContentLength(iLength);
response.setContentType(strContentType);
response.setHeader("Content-Disposition", "attachment;filename=\"" + ViewerTools.encodeDocName4Download(strDocName) + strDocExt + "\"");
ServletOutputStream Output = response.getOutputStream();
objBinaryView.getContent(Output);
Output.close();
objBinaryView = null;
doc = null;
arrReports = null;
objReport = null;
%>

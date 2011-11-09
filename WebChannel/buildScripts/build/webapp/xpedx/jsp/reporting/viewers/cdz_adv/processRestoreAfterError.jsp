<!--
=============================================================
WebIntelligence(r) Report Panel
Copyright(c) 2001-2005 Business Objects S.A.
All rights reserved

Use and support of this software is governed by the terms
and conditions of the software license agreement and support
policy of Business Objects S.A. and/or its subsidiaries. 
The Business Objects products and technology are protected
by the US patent number 5,555,403 and 6,247,008

=============================================================
--><%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive)
{
objUtils.invalidSessionDialog(out);
return;
}
try
{
_logger.info("--> processRestoreAfterError.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("sEntry IN: " + strEntry);
String strQueryString = requestWrapper.getQueryString();
boolean bRestoreState = false;
try
{
ReportEngine objReportEngine = reportEngines.getServiceFromStorageToken(strEntry);
bRestoreState = objReportEngine.isStorageTokenValid(strEntry);
if (bRestoreState)
{
DocumentInstance doc = objReportEngine.getDocumentFromStorageToken(strEntry);
Report objReport = doc.getReports().getItem(0);
Object obj = objReport.getView(OutputFormatType.DHTML);
bRestoreState = true;
obj = null;
objReport = null;
doc = null;
}
}
catch (Exception e)
{
bRestoreState = false;
_logger.error("Error occured when trying to restore previous state: " + e.getLocalizedMessage());
}
String strRestoreState = (bRestoreState) ? "true" : "false";
%>
<html>
<head>
<script language="javascript">
var restorePreviousState = <%=strRestoreState%>;
var qs = "<%=ViewerTools.escapeQuotes(strQueryString)%>";
if (restorePreviousState)
{
parent.frameNav("Report","report"+parent._appExt+"?"+qs);
}
else
{
parent._dontCloseDoc = true;
parent.backToParent();
}
</script>
</head>
<body></body>
</html>
<%
}
catch (Exception e)
{
out.clearBuffer();
out.println("<html><head><script language=\"javascript\">");
out.println("function okCB() {");
out.println(" parent.backToParent();");
out.println("}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_DEFAULT", false, "okCB", out, session);
}
%>

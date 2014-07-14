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
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strRefId = requestWrapper.getQueryParameter("refId", true);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
String strDocID = doc.getProperties().getProperty(PropertiesType.DOCUMENT_ID);
ReportPart reportPart = doc.setReportPart(strRefId);
String strUIRef = reportPart.getUniqueReference();
%>
<html>
<head>
<script language="javascript">
    if (parent.blnAllowPicker)
{
var uiref = "<%=ViewerTools.escapeQuotes(strUIRef)%>";
parent.goSelectReportPart(uiref, "<%=strDocID%>", "<%=strEntry%>");
parent.blnAllowPicker=false;
}
</script>
</head>
<body></body>
</html>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_REPORT_PART_PICKER", false, out, session);
}
%>

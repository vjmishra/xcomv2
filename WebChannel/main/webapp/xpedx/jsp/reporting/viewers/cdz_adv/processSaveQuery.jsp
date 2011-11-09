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
--><%@ include file="wistartpage.jsp" %>
<jsp:useBean id="objApplyQueries" class="com.businessobjects.adv_ivcdzview.ApplyQueries" scope="page" />
<%
response.setDateHeader("expires", 0);
try
{
_logger.info("-->processSaveQuery.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strWOMqueries = requestWrapper.getQueryParameter("WOMqueries", false, "");
String strAllUserEditQuery = requestWrapper.getQueryParameter("sEditQuery", false, "");
String strNewDoc = requestWrapper.getQueryParameter("sNewDoc", false, "");
boolean bNewDoc = Boolean.valueOf(strNewDoc).booleanValue();
String sbQuickSave = requestWrapper.getQueryParameter("quickSave",false,"false");
boolean bQuickSave = Boolean.valueOf(sbQuickSave).booleanValue();
_logger.info("sbQuickSave = " + sbQuickSave );
String strCreateDefaultReportBody = requestWrapper.getQueryParameter("bCreateDefaultReportBody", false, "false");
boolean bCreateDefaultReportBody = Boolean.valueOf(strCreateDefaultReportBody).booleanValue();
_logger.info("strCreateDefaultReportBody="+strCreateDefaultReportBody);
_logger.info("strNewDoc=" + strNewDoc);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
DataProviders dps = doc.getDataProviders();
boolean bModified = false;
String strRedirect = "";
String strQueryString="";
Properties props = doc.getProperties();
if(!strAllUserEditQuery.equals(""))
{
props.setProperty("enablealternateusertoeditdoc", strAllUserEditQuery);
doc.setProperties(props);
}
bModified = objApplyQueries.applyModifications(strWOMqueries, dps);
dps.generateQueries();
_logger.info("Has generated queries.");
if (doc.getMustFillContexts())
{
_logger.info("Doc has contexts.");
strRedirect = "getContextsInfos.jsp";
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
requestWrapper.setQueryParameter("bSaveQuery", "true");
out.clearBuffer();
%>
<jsp:forward page="<%=strRedirect%>"/>
<%
}
if (bCreateDefaultReportBody) 
{
String sDefaultRepTitle= requestWrapper.getQueryParameter("defaultRepTitle",false,"");
objApplyQueries.createDefaultReportBody(doc,-1, sDefaultRepTitle);
_logger.info("createDefaultReportBody with title=" + sDefaultRepTitle);
}
if (bModified) {
doc.applyFormat();
}
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
if (bQuickSave)
{
_logger.info("Quick save.");
strRedirect = "processSave.jsp";
out.clearBuffer();
%>
<jsp:forward page="<%=strRedirect%>"/>
<%
} else {
%>
<html>
<head>
<script language="javascript">
function loadCB()
{
parent.changeEntry("<%=strEntry %>");
parent.saveAs(true); 
}
</script>
</head>
<body onload="loadCB()">
</body>
</html>
<%
}
_logger.info("<--processSaveQuery.jsp");
}
catch(Exception e) {
objUtils.displayErrorMsg(e, "_ERR_SAVE_QUERY", true, out, session);
}
%>
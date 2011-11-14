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
<jsp:useBean id="objApplyQueries" class="com.businessobjects.adv_ivcdzview.ApplyQueries" scope="page" />
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->processGenerateQuery.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    _logger.info("IN strEntry = " + strEntry );
String strViewerID = requestWrapper.getQueryParameter("iViewerID",true);
    DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
String strWOMqueries = requestWrapper.getQueryParameter("WOMqueries", false, "");
_logger.info("strWOMqueries="+strWOMqueries);
DataProviders dps = doc.getDataProviders();
if ( !strWOMqueries.equals("") )
{
objApplyQueries.applyModifications( strWOMqueries, dps);
}           
    session.setAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"), doc);
_logger.info("<--processGenerateQuery.jsp");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script language="javascript">
function loadCB()
{
var f=parent.getTopFrameset()
if (f)
f=f.getFrame("DlgFrame")
if (f)
{
if (f.updateQueryPropertiesDialog)
    f.updateQueryPropertiesDialog(0,null); 
}
}
</script>
</head>
<body>
</body>
<script language="javascript">setTimeout("loadCB()",1)</script>
</html>
<%
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>

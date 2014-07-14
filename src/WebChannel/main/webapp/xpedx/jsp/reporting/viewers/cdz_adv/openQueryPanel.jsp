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
<%
response.setDateHeader("expires", 0);
_logger.info("-->openQueryPanel.jsp");
session.setAttribute("isSessionAlive", "yes");
String sUnvID = requestWrapper.getQueryParameter("unvid", false);
_logger.info("sUnvID=" + sUnvID);
ReportEngine objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
objReportEngine.setLocale(requestWrapper.getUserLocale());
DocumentInstance doc = objReportEngine.newDocument(sUnvID);
String strEntry = doc.getStorageToken();
String sWIViewType="I";
objUserSettings.setDocKind("Webi"); 
session.setAttribute("CDZ.Skin", "skin_standard");
String sRedirect = "viewDocument.jsp";
String sUrl="viewDocument.jsp?";
sUrl += "sEntry=" + ViewerTools.URLEncodeUTF8( ViewerTools.escapeQuotes(strEntry) );
sUrl += "&bEmptyDoc=true";
sUrl += "&isNew=true";
sUrl += "&bLaunchQP=true";
sUrl += "&ViewType="+sWIViewType;
sUrl += "&sUndoEnabled=false";
out.clearBuffer();
_logger.info("<--openQueryPanel.jsp");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript">
_skinName="<%=(String)session.getAttribute("CDZ.Skin")%>"
_img="images/main/"
initDom("lib/images/"+_skinName+"/","<%=(String)session.getAttribute("CDZ.Language")%>")
</script>
<script language="javascript" src="language/<%=(String)session.getAttribute("CDZ.Language")%>/scripts/labels.js"></script>
<style type="text/css">
BODY {cursor:wait}
</style>
<script language="javascript">
function loadCB()
{
var url="<%=sUrl%>&defaultDocName="+convURL(_defaultDocName)+"&defaultRepName="+convURL(_defaultRepName)+"&defaultRepTitle="+convURL(_defaultRepTitle)
var _UDZ_useCustomPrompts = "N";
if ("undefined" != typeof(parent.goPrompts))
_UDZ_useCustomPrompts = "Y";
var _UDZ_customParameters = "&useCustomPrompts="+_UDZ_useCustomPrompts;
url+=_UDZ_customParameters;
document.location.replace(url);
}
</script>
</head>
<body onload="loadCB()">
</body>
</html>

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
String strNewDoc = null;
String strNEV = null;
String strNoEvent = null;
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String iReport = requestWrapper.getQueryParameter("iReport", true);
strNoEvent = requestWrapper.getQueryParameter("noEvent", false, "");
strNEV = requestWrapper.getQueryParameter("sNEV", false, "no");
strNewDoc = requestWrapper.getQueryParameter("sNewDoc", false, "false");
String strQueryString = requestWrapper.getQueryString();
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
String strLastRefreshDate = doc.getProperties().getProperty("lastrefreshdate");
if(strLastRefreshDate == null) strLastRefreshDate="";
String strLastRefreshTime = doc.getProperties().getProperty(PropertiesType.LAST_REFRESH_DATE, "0");
long lLastRefreshMilliSecDate = Long.parseLong(strLastRefreshTime) * 1000;
String strLastRefreshDuration = doc.getProperties().getProperty(PropertiesType.LAST_REFRESH_DURATION, "0");
String sbPartialResult = doc.getProperties().getProperty(PropertiesType.IS_PARTIALLY_REFRESHED, "false");
sbPartialResult = sbPartialResult.toLowerCase();
doc = null;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="scripts/Utils.js"></script>
<script language="javascript">
var p=parent;
var strQueryString = "<%=ViewerTools.escapeQuotes(strQueryString)%>";
if (strQueryString.indexOf("iViewerID=")<0)
strQueryString = "iViewerID=" + p.iViewerID + "&" + strQueryString;
var docName = encodeDocName(p.strDocName);
strQueryString += "&name=/" + docName + ".pdf";
function encodeDocName(name)
{
s = "";
for (var i=0; i<name.length; i++)
{
var c = name.charAt(i);
switch (c)
{
case ' ':
s += "%20";
break;
case '&':
s += "%26";
break;
case '/':
s += "_";
break;
default:
s += c;
}
}
return s;
}
var strNEV = "<%=strNEV%>";
var strNoEvent = "<%=strNoEvent%>";
var strEntry = "<%=ViewerTools.escapeQuotes(strEntry)%>";
if (strEntry!="")
p.changeEntry(strEntry);
var iReportID = "<%=iReport%>";
p.lastRefreshDuration = "<%=strLastRefreshDuration%>";
p.dtDate = new Date(<%=lLastRefreshMilliSecDate%>);
p.strLastRefreshDate = "<%=ViewerTools.escapeQuotes(strLastRefreshDate)%>";
if(p.strLastRefreshDate == "")
{
if(<%=lLastRefreshMilliSecDate%>!=0)
p.strLastRefreshDate = p.formatDate(p.dtDate, p.strDateFormat);
else
p.strLastRefreshDate = p._noRefreshYet;
}
p.blnPartialResult = <%=sbPartialResult%>;
if (p.hideWaitDlg)
p.hideWaitDlg();
if (!p._doNotHideOnLoadReport)
p.hideBlockWhileWaitWidget();
function yesCB()
{
strQueryString=updateQueryParameter(strQueryString, "download", "yes") + "&name=/*.pdf";
if (strQueryString.indexOf("viewType=")<0)
strQueryString = "viewType=P" + "&" + strQueryString;
self.location.replace("downloadDoc.jsp?" + strQueryString);
}
function noCB()
{
if (strNEV=="yes")
p.backToParent();
else
{
p.setClickCBID("htmlMode");
p.clickCB();
}
}
function loadCB()
{
var blnViewPDF=true;
if (p._ie)
{
if (typeof(document.all.PDFNotKnown)!="undefined")
blnViewPDF=false;
}
else if (typeof(navigator.mimeTypes["application/pdf"])=="undefined")
blnViewPDF=false;
if (blnViewPDF)
{
p.showWaitDlg(p._view_in_pdf_format);
p.eventManager.notify(p._EVT_PAGE_LOADED);
if (p._propShowTabs4PDF)
{
strQueryString+="&sShowTabs=true";
p.iReportID=iReportID;
}
self.location.replace("viewAsPDF.jsp?" + strQueryString);
}
else
{
p.showPromptDialog(p._ERR_PLUGIN_PDF_NOT_INSTALLED,p._view_in_pdf_format,2,yesCB,noCB);
}
}
function unLoadCB()
{
if (parent.hideWaitDlg)
parent.hideWaitDlg();
}
</script>
</head>
<body onload="loadCB()" onunload="unLoadCB()">
<script language="javascript">
if (p._ie)
{
document.write('<object type="application/pdf" width="0" height="0" style="visibility:hidden" id="pdfobj">');
document.write('<div id="PDFNotKnown" style="visibility:hidden">&nbsp;</div>');
document.write('</object>');
}
</script>
</body>
</html>
<%
}
catch(Exception e)
{
out.println("<html><head><script language=\"javascript\">");
out.println("function okCB() {");
if (strNEV.equals("yes") || strNewDoc.equals("true"))
out.println("parent.backToParent();");
else
{
out.println("parent.setClickCBID(\"htmlMode\");");
out.println("parent.clickCB();");
}
out.println("}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_VIEWER_PDFMODE",false, "okCB", out, session);
}
%>
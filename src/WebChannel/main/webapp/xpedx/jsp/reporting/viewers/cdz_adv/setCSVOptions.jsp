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
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript" src="lib/dialog.js"></script>
<script language="javascript">
p = parent;
initDom(p._root + "lib/images/"+p._skinName+"/", p._lang, p, "csvOptions");
</script>
<script language="javascript" src="scripts/Utils.js"></script>
<script language="javascript" src="scripts/ViewerState.js"></script>
<script language="javascript" src="language/<%=(String)session.getAttribute("CDZ.Language")%>/scripts/csvOptions.js"></script>
<script language="javascript">
var charsetLabels=newHashTable();
charsetLabels.put("UTF-8", _charset_UTF8);
charsetLabels.put("ar", _charset_ar);
charsetLabels.put("cs", _charset_cs);
charsetLabels.put("de", _charset_de);
charsetLabels.put("el", _charset_el);
charsetLabels.put("en", _charset_en);
charsetLabels.put("es", _charset_es);
charsetLabels.put("fr", _charset_fr);
charsetLabels.put("he", _charset_he);
charsetLabels.put("it", _charset_it);
charsetLabels.put("ja", _charset_ja);
charsetLabels.put("ko", _charset_ko);
charsetLabels.put("nl", _charset_nl);
charsetLabels.put("pl", _charset_pl);
charsetLabels.put("pt", _charset_pt);
charsetLabels.put("ru", _charset_ru);
charsetLabels.put("sv", _charset_sv);
charsetLabels.put("th", _charset_th);
charsetLabels.put("tr", _charset_tr);
charsetLabels.put("zh_CN", _charset_zh_CN);
charsetLabels.put("zh_TW", _charset_zh_TW);
<%
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strQueryString = requestWrapper.getQueryString();
String strCharDelimiter = "\"";
String strColumnSeparator = ",";
String strOptCharSet = "";
String strDefaultCharSet = "";
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
String strLocale = (String)session.getAttribute("CDZ.Language");
strDefaultCharSet = objUtils.objConfig.getProperty(strLocale, "UTF-8");
String[] arr = ViewerTools.split(strDefaultCharSet, ",");
if (iIndex >= arr.length) iIndex = 0;
strDefaultCharSet = arr[iIndex];
String strDefaultValues = objUserSettings.getUserProfile("CDZ_VIEW_CSV_DefaultValues", "false");
if (strDefaultValues.equals("true"))
{
strCharDelimiter = objUserSettings.getUserProfile("CDZ_VIEW_CSV_CharDelimiter", strCharDelimiter);
strColumnSeparator = objUserSettings.getUserProfile("CDZ_VIEW_CSV_ColumnSeparator", strColumnSeparator);
strOptCharSet = objUserSettings.getUserProfile("CDZ_VIEW_CSV_Charset", "");
}
session.setAttribute("CDZ." + strViewerID + ".CSV", null);
out.println("delimiters=\"" + ViewerTools.escapeQuotes(objUtils.objConfig.getProperty("CSV_CharDelimiter")) + "\";");
out.println("separators=\"" + ViewerTools.escapeQuotes(objUtils.objConfig.getProperty("CSV_ColumnSeparator")) + "\";");
String[] charsets =objUtils.objConfig.getProperty("CSV_Charset").split(" ");
out.println("var c=arrCharsets=new Array();");
int j = 0;
for (j=0; j<charsets.length; j++)
{
out.println("c[" + Integer.toString(j) + "]=new Array(2);");
out.println("c[" + Integer.toString(j) + "][0]=\"" + ViewerTools.escapeQuotes(charsets[j]) + "\";");
out.println("c[" + Integer.toString(j) + "][1]=\"" + ViewerTools.escapeQuotes(charsets[j]) + "\";");
}
Enumeration objEnum = objUtils.objChaset.propertyNames();
while (objEnum.hasMoreElements())
{
String strKey = (String)objEnum.nextElement();
String[] values = objUtils.objChaset.getProperty(strKey, "").split(",");
out.println("c[" + Integer.toString(j) + "]=new Array(2);");
out.println("c[" + Integer.toString(j) + "][0]=\"" + ViewerTools.escapeQuotes(values[iIndex]) + "\";");
out.println("c[" + Integer.toString(j) + "][1]=\"" + ViewerTools.escapeQuotes(strKey) + "\";");
j++;
}
out.println("strQueryString=\"" + ViewerTools.escapeQuotes(strQueryString) + "\";");
out.println("strCharDelimiter=\"" + ViewerTools.escapeQuotes(strCharDelimiter) + "\";");
out.println("strColumnSeparator=\"" + strColumnSeparator + "\";");
out.println("strDefaultCharSet=\"" + strDefaultCharSet + "\";");
out.println("strOptCharSet=\"" + strOptCharSet + "\";");
out.println("useDefaultValues=" + strDefaultValues + ";");
%>
function loadCB()
{
setTimeout('loadDialog()',1);
}
</script>
</head>
<body class="dialogzone" onload="loadCB()">
</body>
</html>
<%
}
catch(Exception e)
{
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_DOWNLOAD_AS", false, out, session);
}
%>
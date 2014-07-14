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
String strSetDefault = requestWrapper.getQueryParameter("check_ckSetDefault", false, "");
String strCharDelimiter = requestWrapper.getQueryParameter("cbCharDelimiter");
String strColumnSeparator = requestWrapper.getQueryParameter("cbColSep");
String strCbCharset = requestWrapper.getQueryParameter("cbCharset");
String strOptCharSet = requestWrapper.getQueryParameter("txtCharset");
String strCkOptCharset = requestWrapper.getQueryParameter("check_ckNewCharset", false, "");
_logger.info("--> processCSVOptions");
String strCharset = "";
_logger.info("strCkOptCharset:" + strCkOptCharset);
if (strCkOptCharset.equalsIgnoreCase("on"))
strCharset = strOptCharSet;
else
strCharset = strCbCharset;
_logger.info("strCharset:" + strCharset);
String strParams = strCharDelimiter + " " + strColumnSeparator + " " + strCharset;
session.setAttribute("CDZ." + strViewerID + ".CSV", strParams);
_logger.info("strParams:" + strParams);
if (strSetDefault.equalsIgnoreCase("on"))
{
objUserSettings.setUserProfile("CDZ_VIEW_CSV_DefaultValues", "true");
objUserSettings.setUserProfile("CDZ_VIEW_CSV_CharDelimiter", strCharDelimiter);
objUserSettings.setUserProfile("CDZ_VIEW_CSV_ColumnSeparator", strColumnSeparator);
objUserSettings.setUserProfile("CDZ_VIEW_CSV_Charset", strCharset);
} else {
objUserSettings.setUserProfile("CDZ_VIEW_CSV_DefaultValues", "false");
}
_logger.info("<-- processCSVOptions");
out.clearBuffer();
%>
<jsp:forward page="downloadCSV.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_DOWNLOAD_AS", true, out, session);
}
%>
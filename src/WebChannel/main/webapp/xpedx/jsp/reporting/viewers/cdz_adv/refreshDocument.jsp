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
String strNewDoc = requestWrapper.getQueryParameter("sNewDoc", false, "false");
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
String strRedirect = "";
String strAction = requestWrapper.getQueryParameter("sAction", false, "");
String strPromptOnRefresh = doc.getProperties().getProperty(PropertiesType.PROMPT_KEYDATE_ONREFRESH, "");
String strRefreshAction = "Refresh";
if (strPromptOnRefresh.equals("true") && !strAction.equals(strRefreshAction))
{
strRedirect = "getKeydateProperties.jsp";
requestWrapper.setQueryParameter("sAction", strRefreshAction);
}
else
{
String strKey = "CDZ." + strViewerID + ".DocInstance";
session.setAttribute(strKey, doc);
if(strDPIndex.equals("")) 
doc.refresh();
else
{
_logger.info("dp to refresh => " + strDPIndex);
int iDPIndex = Integer.parseInt(strDPIndex); 
DataProviders dps = doc.getDataProviders();
DataProvider dp = dps.getItem(iDPIndex);
if(dp!=null) dp.runQuery();
}
if (doc.getMustFillContexts())
strRedirect = "getContextsInfos.jsp";
else if (doc.getMustFillPrompts())
strRedirect = "getPrompts.jsp";
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
}
if (!strRedirect.equals(""))
{
requestWrapper.setQueryParameter("sEntry", strEntry);
out.clearBuffer();
%>
<jsp:forward page="<%=strRedirect%>"/>
<%
}
else
{
String strQueryString = requestWrapper.getQueryString();
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
out.println("<html><head><script language=\"javascript\">");
out.println("parent.Report.location=\"report.jsp?" + ViewerTools.escapeQuotes(strQueryString) + "\";");
out.println("</script></head><body></body></html>");
}
}
catch(Exception e) {
objUtils.displayErrorMsg(e, "_ERR_REFRESH_DATA", true, out, session);
}
%>
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
out.println("<html><head><script language='javascript'>parent.alertSessionInvalid();");
out.println("</script></head><body></body></html>");
return;
}
try
{
_logger.info("-->processUserPref.jsp");
String sFrom = requestWrapper.getQueryParameter("sFrom", false, "");
if (sFrom.equals(""))
{
String strKey   = requestWrapper.getQueryParameter("sKey", true);
String strValue = requestWrapper.getQueryParameter("sVal", true);
objUserSettings.setUserProfile(strKey, strValue);
_logger.info("processUserPref FROM is EMPTY\n strKey:" + strKey + ", strValue:" + strValue);
} else if (sFrom.equals("userPrefDlg")) {
String sValue = requestWrapper.getQueryParameter("framework", false, "");
_logger.info("framework = " + sValue );
String[] frmk = ViewerTools.split(sValue,";");
objUserSettings.setUserProfile( "CADENZA_MeasurementUnit", frmk[0]);
objUserSettings.setUserProfile( "CDZ_VIEW_snapToGrid", frmk[1]);
objUserSettings.setUserProfile( "CDZ_VIEW_showGrid", frmk[2]);
objUserSettings.setUserProfile( "CDZ_VIEW_gridSpacing", frmk[3]);
objUserSettings.setUserProfile( "CDZ_VIEW_showDesc", frmk[4]);
objUserSettings.setUserProfile( "CDZ_VIEW_dispMenuHeaders", frmk[5]);
objUserSettings.setUserProfile( "CDZ_VIEW_ctxMenu", frmk[6]);
sValue = requestWrapper.getQueryParameter("repRules", false, "");
_logger.info("repRules = " + sValue );
String[] rep = ViewerTools.split(sValue,";");
int nbProps = rep.length;
objUserSettings.setUserProfile( "CDZ_VIEW_appFmt", rep[0]);
objUserSettings.setUserProfile( "CDZ_VIEW_addTableHeader", nbProps>=2?rep[1]:"true");
objUserSettings.setUserProfile( "CDZ_VIEW_sortPriority", nbProps>=3?rep[2]:"highest");
objUserSettings.setUserProfile( "CDZ_VIEW_drillCustomFmt", nbProps>=4?rep[3]:"false");
objUserSettings.setUserProfile( "CDZ_VIEW_drillUnderlined", nbProps>=5?rep[4]:"false");
objUserSettings.setUserProfile( "CDZ_VIEW_drillBgColor", nbProps>=6?rep[5]:"");
objUserSettings.setUserProfile( "CDZ_VIEW_drillFgColor", nbProps>=7?rep[6]:"");
sValue = requestWrapper.getQueryParameter("queryRules", false, "");
String[] query = ViewerTools.split(sValue,";");
objUserSettings.setUserProfile( "CDZ_VIEW_defaultQueryFilter", query[0]);
String strRedirect = "report.jsp";
out.clearBuffer();
%><jsp:forward page="<%=strRedirect%>"/><%
}
_logger.info("<--processUserPref.jsp");
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_DEFAULT", true, out, session);
}
%>

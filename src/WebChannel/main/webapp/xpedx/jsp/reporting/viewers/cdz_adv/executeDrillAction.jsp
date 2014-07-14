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
String strCancel = "";
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String iReport = requestWrapper.getQueryParameter("iReport", true);
int iReportIndex = Integer.parseInt(iReport);
String strAction = requestWrapper.getQueryParameter("action", true);
strCancel = requestWrapper.getQueryParameter("sCancel", false, "");
String strBlock = requestWrapper.getQueryParameter("block", false, "0");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
Report objReport = doc.getReports().getItem(iReportIndex);
DrillInfo objDrillInfo = (DrillInfo) objReport.getNamedInterface("DrillInfo");
DrillPath objDrillPath = objDrillInfo.getDrillPath();
if (strAction.equals("up")) objDrillPath.setAction(DrillActionType.UP);
else if (strAction.equals("down")) objDrillPath.setAction(DrillActionType.DOWN);
else if (strAction.equals("by")) objDrillPath.setAction(DrillActionType.BY);
objDrillPath.setBlockID(strBlock);
int nIndex;
String[] arrFromIDs = requestWrapper.getQueryParameterValues("from");
int ncFromIDs = 0;
if (arrFromIDs != null) ncFromIDs = arrFromIDs.length;
String[] arrFilterIDs = requestWrapper.getQueryParameterValues("filter");
int ncFilterIDs = 0;
if (arrFilterIDs != null) ncFilterIDs = arrFilterIDs.length;
String[] arrToIDs  = requestWrapper.getQueryParameterValues("to");
int ncToIDs = 0;
if (arrToIDs != null) ncToIDs = arrToIDs.length;
String[] arrHierIDs = requestWrapper.getQueryParameterValues("hier");
DrillFromElement objFromDrillElement = null;
if (ncFromIDs>=ncFilterIDs)
{
for (nIndex = 0; nIndex < ncFromIDs; nIndex++)
{
objFromDrillElement = (DrillFromElement)objDrillPath.getFrom().add();
objFromDrillElement.setObjectID(arrFromIDs[nIndex]);
if (nIndex < ncFilterIDs && !strAction.equals("up")) objFromDrillElement.setFilter(arrFilterIDs[nIndex]);
}
}
else
{
for (int nIndexFilter = 0; nIndexFilter < ncFilterIDs; nIndexFilter++)
{
for (nIndex = 0; nIndex < ncFromIDs; nIndex++)
{
objFromDrillElement = (DrillFromElement)objDrillPath.getFrom().add();
objFromDrillElement.setObjectID(arrFromIDs[nIndex]);
objFromDrillElement.setFilter(arrFilterIDs[nIndexFilter]);
}
}
}
DrillToElement objToDrillElement = null;
for (nIndex = 0; nIndex < ncToIDs; nIndex++)
{
objToDrillElement = (DrillToElement)objDrillPath.getTo().add ();
objToDrillElement.setObjectID(arrToIDs[nIndex]);
objToDrillElement.setHierarchyID(arrHierIDs[nIndex]);
}
boolean blnOutOfScope = objDrillInfo.willGoOutOfScope();
String strPromptDrillOutScope = objUserSettings.getUserPreference("DOCUMENT_WIPromptDrillOutScope");
String strDrillOnline = doc.getProperties().getProperty(PropertiesType.DATABASE_CONNECTED_DRILL);
strDrillOnline = (strDrillOnline == null)?"false":strDrillOnline.toLowerCase();
boolean blnDrillOnline = (strDrillOnline.equals("true"))?true:false;
if (blnOutOfScope || blnDrillOnline)
{
String strExtScope = objUserSettings.getUserRight("EXTEND_SCOPE_OF_ANALYSIS");
if (!strExtScope.equals("full"))
{
strCancel = "";
throw new Exception("VIEWER:_ERR_DRILL_OUTOF_SCOPE_RIGHTS");
}
}
String strQueryString = requestWrapper.getQueryString();
String strRedirectTo = "report.jsp";
String strRelativeURL = "";
if (strPromptDrillOutScope.equals("Y") && blnOutOfScope && !blnDrillOnline)
{
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
strRedirectTo = "getExtendScopeOfAnalysis.jsp";
strRelativeURL = "sEntry=" + strEntry + "&" + strQueryString;
strRelativeURL += "&action=" + strAction;
strRelativeURL += "&iFromID=";
for (nIndex = 0; nIndex < ncFromIDs; nIndex++)
{
if (nIndex > 0) strRelativeURL += ";";
strRelativeURL += arrFromIDs[nIndex];
}
strRelativeURL += "&iToID=";
for (nIndex = 0; nIndex < ncToIDs; nIndex++)
{
if (nIndex > 0) strRelativeURL += ";";
strRelativeURL += arrToIDs[nIndex];
}
request.setAttribute("CDZ.DrillOutOfScope.Filters", arrFilterIDs);
}
else
{
objDrillInfo.executeDrill();
strEntry = doc.getStorageToken();
strQueryString = "sEntry=" + strEntry + "&" + strQueryString;
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
strRelativeURL = strQueryString;
if (doc.getMustFillContexts())
strRedirectTo = "getContextsInfos.jsp";
else if (doc.getMustFillPrompts())
strRedirectTo = "getPrompts.jsp";
}
requestWrapper.setQueryString(strRelativeURL);
out.clearBuffer();
%>
<jsp:forward page="<%=strRedirectTo%>"/>
<%
}
catch(Exception e)
{
if (!strCancel.equals(""))
{
out.println("<html><head><script language=\"javascript\">");
out.println("var previousURL=parent.allUseDictionary.get(\"" + strCancel + "\");");
out.println("function okCB() {parent.frameNav(\"Report\",previousURL);}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_DRILL", false, "okCB",out, session);
}
else
objUtils.displayErrorMsg(e, "_ERR_DRILL", true, out, session);
}
%>
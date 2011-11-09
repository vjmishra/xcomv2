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
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int iReportIndex = Integer.parseInt(iReport);
String strReportMode = requestWrapper.getQueryParameter("sReportMode", false, "");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
Report objReport = doc.getReports().getItem(iReportIndex);
String strReportID = objReport.getID();
DrillInfo objDrillInfo = (DrillInfo)objReport.getNamedInterface ("DrillInfo");
String strDrillReportID = "";
String strRelativeURL = "";
if (objDrillInfo.isActive())
{
objDrillInfo.endDrill();
strRelativeURL = "viewReport.jsp";
}
else
{
if (strReportMode.equals("Analysis"))
{
Report objDrillReport = objDrillInfo.beginDrill();
strDrillReportID = objDrillReport.getID();
if (!strDrillReportID.equals(strReportID))
{
iReportIndex = doc.getReports().getCount() - 1;
iReport = Integer.toString(iReportIndex);
requestWrapper.setQueryParameter("sRequestNewReport", "true");
}
strRelativeURL = "report.jsp";
}
else
{
requestWrapper.setQueryParameter("sReportMode", "Viewing");
strRelativeURL = "viewReport.jsp";
}
}
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
requestWrapper.setQueryParameter("iReport", iReport);
out.clearBuffer();
%>
<jsp:forward page="<%=strRelativeURL%>"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_DRILL", true, out, session);
}
%>
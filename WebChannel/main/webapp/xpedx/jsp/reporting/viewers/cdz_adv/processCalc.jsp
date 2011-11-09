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
String bid = requestWrapper.getQueryParameter("sBid", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
String calcAction = requestWrapper.getQueryParameter("sCalcAction", false);
String bottomCalc = requestWrapper.getQueryParameter("iBottomCalc", false, "0");
DocumentInstance objDocumentInstance = reportEngines.getDocumentFromStorageToken(strEntry);
CurrentCellInfo cellInfo = new CurrentCellInfo(objDocumentInstance, nReportIndex, bid);
cellInfo.toggleCalc(calcAction, (bottomCalc.equals("0"))?false:true);
objDocumentInstance.applyFormat();
strEntry = objDocumentInstance.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strRedirect = "report.jsp";
requestWrapper.setQueryParameter("sEntry", strEntry);
cellInfo = null;
objDocumentInstance = null;
out.clearBuffer();
%>
<jsp:forward page="<%=strRedirect%>"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_INSERT_CALCULATION", true, out, session);
}
%>

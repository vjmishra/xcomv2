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
_logger.info("-->processFilterBy.jsp");
String strEntry      = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
String bid           = requestWrapper.getQueryParameter("sBid", true);
String sOperator    = requestWrapper.getQueryParameter("sOperator", true);
String sOperands    = requestWrapper.getQueryParameter("sOperands", true);
String filtBid       = requestWrapper.getQueryParameter("filtBid", true);
String iReport     = requestWrapper.getQueryParameter("iReport", false, "0");
int      nReportIndex = Integer.parseInt(iReport);
DocumentInstance objDocumentInstance = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("objDocumentInstance:" + objDocumentInstance);
CurrentCellInfo cellInfo = new CurrentCellInfo(objDocumentInstance, nReportIndex, bid);
ReportExpression expr = cellInfo.m_expr;
_logger.info("expr:" + expr);
Operator operator = Operator.fromInt(Integer.parseInt(sOperator));
String[] operands = ViewerTools.convertStringToArray(sOperands);
_logger.info("operator:" + operator);
for (int i = 0; i < operands.length; i++)
{
String tmp = operands[i].substring(1, operands[i].length()-1);
operands[i] = tmp;
_logger.info("operands[" + i + "]:" + operands[i]);
}
ReportElement reFilter = cellInfo.getReportElementByID(filtBid);
_logger.info("filtBid:" + filtBid);
_logger.info("reFilter:" + reFilter);
if (!cellInfo.isChart())
{
cellInfo.setFilter(reFilter, operator, operands);
}
else
{
}
objDocumentInstance.applyFormat();
strEntry = objDocumentInstance.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strQueryString = requestWrapper.getQueryString();
int iPos = strQueryString.indexOf("&sBid");
if (iPos>=0) {
strQueryString = strQueryString.substring(0, iPos);
}
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
requestWrapper.setQueryString(strQueryString);
requestWrapper.setQueryParameter("sFollowBid", bid);
_logger.info("<--processFilterBy.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_FILTER_BY", true, out, session);
}
%>
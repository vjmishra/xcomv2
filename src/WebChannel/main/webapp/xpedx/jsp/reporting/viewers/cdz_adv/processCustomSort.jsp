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
_logger.info("-->processCustomSort.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
DocumentInstance doc     = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("strEntry = " + strEntry );
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(strReport);
String bid = requestWrapper.getQueryParameter("sBid", false, "");
String ID = requestWrapper.getQueryParameter("sID", false, "");
ReportExpression re = null;
if (!ID.equals("")) {
_logger.info("use ID = " + ID );
ReportDictionary rd = doc.getDictionary();
re = rd.getChild(ID);
} else {
_logger.info("use bid = " + bid );
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
re = cellInfo.m_expr;
}
String customSortLOV = requestWrapper.getQueryParameter("customSortLOV", false, "");
String delCS = requestWrapper.getQueryParameter("delCS", false, "false");
boolean bDeleteCS = Boolean.valueOf(delCS).booleanValue();
SortInfo sortInfo = null;
if (re instanceof DPExpression) {
sortInfo = ((DPExpression) re).getSortInfo();
} else if (re instanceof VariableExpression) {
sortInfo = ((VariableExpression) re).getSortInfo();
} else if (re instanceof Link) {
sortInfo = ((Link) re).getSortInfo();
}
boolean getNewToken = false;
if (bDeleteCS) {
_logger.info("delete Custom Sort");
sortInfo.deleteCustomSort();
getNewToken = true;
} else if (sortInfo.canApplyCustomSort() && !customSortLOV.equals("")){
String[] arrCustomSortLOV = ViewerTools.convertStringToArray(customSortLOV);
StringBuffer tmp = new StringBuffer("customSortLOV= ");
int len = arrCustomSortLOV.length;
String sItem;
for (int i=0; i < len; i++) {
sItem = arrCustomSortLOV[i]; 
arrCustomSortLOV[i] = sItem.substring(1, sItem.length() -1); 
tmp.append(arrCustomSortLOV[i]);
if (i < (len-1)) {
tmp.append(",");
}
}
_logger.info(tmp);
sortInfo.setCustomSortLov(arrCustomSortLOV);
getNewToken = true;
}
if (getNewToken) {
strEntry = doc.getStorageToken();
_logger.info("new strEntry = " + strEntry );
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strQueryString = requestWrapper.getQueryString();
int iPos = strQueryString.indexOf("&sBid");
if (iPos>=0)
  strQueryString = strQueryString.substring(0, iPos);
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
requestWrapper.setQueryString(strQueryString);
} else {
_logger.info("No modification has been done to the custom sort.");
}
_logger.info("<--processCustomSort.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_CUSTOM_SORT", true, out, session);
}
%>
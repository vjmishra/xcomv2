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
String bid = requestWrapper.getQueryParameter("sBid", true);
String strDuplicateBottom = requestWrapper.getQueryParameter("isBottom",false,"0");
boolean isBottom=strDuplicateBottom.equals("1");
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
String strAction = requestWrapper.getQueryParameter("sAction", false, "apply");
String[] arrFilterValues = request.getParameterValues("FV");
if (null == arrFilterValues) arrFilterValues = new String [0];
String axisName = requestWrapper.getQueryParameter("sAxisName", false, "x");
String indexInAxis = requestWrapper.getQueryParameter("sAxisIndex", false, "-1");
int nIndexInAxis = Integer.parseInt(indexInAxis);
String followBid="";
DocumentInstance objDocumentInstance = reportEngines.getDocumentFromStorageToken(strEntry);
CurrentCellInfo cellInfo = new CurrentCellInfo(objDocumentInstance, nReportIndex, bid);
ReportElement sourceBlock = cellInfo.m_block;
if (sourceBlock == null)
sourceBlock = cellInfo.m_cell;
if (sourceBlock != null)
{
ReportElementContainer container = sourceBlock.getFather();
ReportElement destBlock = container.copyReportElement(sourceBlock);
if (destBlock instanceof Attachable)
{
Attachable attachable = (Attachable)destBlock;
UnitType oldUnit = attachable.getUnit();
attachable.setUnit(UnitType.MILLIMETER);
if (isBottom)
{
attachable.setAttachTo(sourceBlock, VAnchorType.BOTTOM, HAnchorType.LEFT);
attachable.setY(10);
attachable.setX(0);
}
else
{
attachable.setAttachTo(sourceBlock, VAnchorType.TOP, HAnchorType.RIGHT);
attachable.setX(10);
attachable.setY(0);
}
attachable.setUnit(oldUnit);
}
followBid=destBlock.getID();
}
objDocumentInstance.applyFormat();
strEntry = objDocumentInstance.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strQueryString = requestWrapper.getQueryString();
int iPos = strQueryString.indexOf("&sBid");
if (iPos>=0)
strQueryString = strQueryString.substring(0, iPos);
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
requestWrapper.setQueryString(strQueryString);
requestWrapper.setQueryParameter("sFollowBid",followBid);
 out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_DUPLICATE", true, out, session);
}
%>

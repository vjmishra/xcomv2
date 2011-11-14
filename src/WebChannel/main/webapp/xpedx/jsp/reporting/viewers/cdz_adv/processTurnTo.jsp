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
String strEntry      = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
String bid           = requestWrapper.getQueryParameter("sBid", true);
String sNewBlockType = requestWrapper.getQueryParameter("sNewBlockType", true);
String sNewBlockSubType = requestWrapper.getQueryParameter("sNewBlockSubType", true);
String iReport     = requestWrapper.getQueryParameter("iReport", false, "0");
String axisName    = requestWrapper.getQueryParameter("sAxisName", false, "x");
String indexInAxis = requestWrapper.getQueryParameter("sAxisIndex", false, "-1");
int      nReportIndex = Integer.parseInt(iReport);
DocumentInstance doc           = reportEngines.getDocumentFromStorageToken(strEntry);
CurrentCellInfo  cellInfo      = new CurrentCellInfo(doc, nReportIndex, bid);
int              nNewBlockType = Integer.parseInt(sNewBlockType);
int              nNewBlockSubType = Integer.parseInt(sNewBlockSubType);
Representation   repr          = cellInfo.m_block.getRepresentation();
BlockType        oldType       = repr.getType();
repr.setType(CurrentCellInfo.convertToTypeSubType( nNewBlockType, nNewBlockSubType));
if (((oldType == TableType.HTABLE) || (oldType == TableType.VTABLE) || (oldType == TableType.XTABLE) || (oldType == TableType.FORM)) && (nNewBlockType >= 11))
{
Graph graph = (Graph) repr;
graph.setUnit(UnitType.MILLIMETER);
graph.setWidth(120);
graph.setHeight(80);
}
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strQueryString = requestWrapper.getQueryString();
int iPos = strQueryString.indexOf("&sBid");
if (iPos >= 0)
  strQueryString = strQueryString.substring(0, iPos);
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
requestWrapper.setQueryString(strQueryString);
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_TURN_TO", true, out, session);
}
%>

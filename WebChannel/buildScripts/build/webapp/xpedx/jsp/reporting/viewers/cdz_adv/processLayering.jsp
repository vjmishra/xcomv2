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
_logger.info("-->processLayering.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("strEntry = " + strEntry );
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
String bid = requestWrapper.getQueryParameter("sBid", true);
_logger.info("bid = " + bid);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
ReportElementContainer container = null;
int fromIndex = 0;
if ( cellInfo.m_block != null )
{
container = cellInfo.m_block.getFather();
fromIndex = container.getIndex(cellInfo.m_block);
}
else
{
if ( cellInfo.m_cell == null )
throw new Exception("VIEWER:_ERR_LAYERING_NO_BLOCK");
container = cellInfo.m_cell.getFather();
fromIndex = container.getIndex(cellInfo.m_cell);
}
int toIndex = fromIndex;
int nbLevel = container.getReportElementCount();
String sLevel = requestWrapper.getQueryParameter("level", false);
if ( sLevel.equals("front") )
toIndex=nbLevel-1;
else if ( sLevel.equals("back") )
toIndex=0;
else if ( sLevel.equals("forward") )
toIndex=fromIndex+1;
else if ( sLevel.equals("backwards") )
toIndex=fromIndex-1;
if (toIndex<0)
toIndex=0;
if (toIndex>=nbLevel)
toIndex=nbLevel-1;
_logger.info("Moving from index <"+fromIndex+"> to index <"+toIndex+">");
container.move(fromIndex,toIndex);
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
_logger.info("strEntry = " + strEntry );
requestWrapper.setQueryParameter("sEntry", strEntry);
_logger.info("<--processLayering.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_LAYERING", true, out, session);
}
%>
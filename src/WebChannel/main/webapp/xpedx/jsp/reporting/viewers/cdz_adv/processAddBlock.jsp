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
_logger.info("-->processAddBlock.jsp");
String strEntry    = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strReport = requestWrapper.getQueryParameter("iReport", false, "0");
String bid         = requestWrapper.getQueryParameter("sBid", true);
String sNewBlockType = requestWrapper.getQueryParameter("sNewBlockType", true);
String sNewBlockSubType = requestWrapper.getQueryParameter("sNewBlockSubType", true);
String sUnitIsInch = requestWrapper.getQueryParameter("sUnitIsInch", true);
String sX = requestWrapper.getQueryParameter("sX", true);
String sY = requestWrapper.getQueryParameter("sY", true);
String sRow = requestWrapper.getQueryParameter("sRow", false, "0");
String strTarget = requestWrapper.getQueryParameter("sTarget", false, "");
int     nReportIndex = Integer.parseInt(strReport);
int     row = Integer.parseInt(sRow);
_logger.info("bid=" + bid + ", sUnitIsInch=" + sUnitIsInch + ", sX=" + sX + ", sY=" + sY + ", row=" + row);
DocumentInstance doc     = reportEngines.getDocumentFromStorageToken(strEntry);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
ReportContainer rc = cellInfo.m_report;
ReportElement container = cellInfo.getReportElementByID(bid);
ReportBlock block = null;
int nNewBlockType = Integer.parseInt(sNewBlockType);
int nNewBlockSubType = Integer.parseInt(sNewBlockSubType);
BlockType blockType = CurrentCellInfo.convertToTypeSubType( nNewBlockType, nNewBlockSubType);
_logger.info("blockType = " + blockType);
boolean isTable = (nNewBlockType == 0);
boolean prefUnitIsInch =  Boolean.valueOf(sUnitIsInch).booleanValue();
UnitType prefUnit = prefUnitIsInch ? UnitType.INCH : UnitType.MILLIMETER;
_logger.info("prefUnit=" + prefUnit);
double cellX = 0;
double cellY = 0;
if (container instanceof Cell)
{
_logger.info("get Cell parent = " + container + container.getID());
((Cell) container).setUnit(prefUnit);
cellX = ((Cell) container).getX();
cellY = ((Cell) container).getY();
_logger.info("cellX = " + cellX + ", cellY=" + cellY);
container = container.getFather();
}
if (container instanceof ReportBody)
{
block = ((ReportBody) container).createBlock(blockType);
_logger.info("add to ReportBody = " + container + container.getID());
}
else if (container instanceof SectionContainer)
{
block = ((SectionContainer) container).createBlock(blockType);
_logger.info("add to SectionContainer = " + container + container.getID());
}
if (block != null) 
{
String newBid = block.getID();
_logger.info("new Block bid = " + newBid + ", isTable=" + isTable);
double adjY = CurrentCellInfo.getYPositionFromRow((ReportElement) container, prefUnit, row, _logger);
double finalY = ViewerTools.toServerUnit(sY, prefUnit) + adjY;
block.deleteAttachment();
block.setUnit(prefUnit);
block.setX(ViewerTools.toServerUnit(sX, prefUnit) + cellX);
block.setY(finalY + cellY);
_logger.info("ViewerTools.toServerUnit(sX, prefUnit) = " + ViewerTools.toServerUnit(sX, prefUnit) + cellX + ", ViewerTools.toServerUnit(sY, prefUnit)="+ViewerTools.toServerUnit(sY, prefUnit) + ", adjY= " + adjY);
_logger.info("finalY = " + finalY + cellY) ;
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
_logger.info("new strEntry = " + strEntry);
if (!isTable) requestWrapper.setQueryParameter("sNewChartBid", newBid);
requestWrapper.setQueryParameter("sEntry", strEntry);
requestWrapper.setQueryParameter("sFollowBid", newBid);
}
_logger.info("<--processAddBlock.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
out.clearBuffer();
_logger.info(e.toString());
objUtils.displayErrorMsg(e, "_ERR_ADDING_BLOCK", true, out, session);
}
%>

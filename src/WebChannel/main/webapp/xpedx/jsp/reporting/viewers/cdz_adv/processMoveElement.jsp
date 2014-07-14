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
_logger.info("--> processMoveElement.jsp");
String strEntry    = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String bid         = requestWrapper.getQueryParameter("sBid", true);
String sTargetBid  = requestWrapper.getQueryParameter("sTargetBid", true);
String sUnitIsInch = requestWrapper.getQueryParameter("sUnitIsInch", true);
String sX          = requestWrapper.getQueryParameter("sX", true);
String sY          = requestWrapper.getQueryParameter("sY", true);
String sDx         = requestWrapper.getQueryParameter("sDx", true);
String sDy         = requestWrapper.getQueryParameter("sDy", true);
String iReport   = requestWrapper.getQueryParameter("iReport", false, "0");
String sDup      = requestWrapper.getQueryParameter("sDup", false, "false");
String sRow      = requestWrapper.getQueryParameter("sRow", false, "0");
String sStartRow = requestWrapper.getQueryParameter("sStartRow", false, "0");
  _logger.info("bid=" + bid + ", sTargetBid=" + sTargetBid + ", sUnitIsInch=" + sUnitIsInch + ", sX=" + sX + ", sY=" + sY);
int nReportIndex = Integer.parseInt(iReport);
int row = Integer.parseInt(sRow);
int startRow = Integer.parseInt(sStartRow);
_logger.info("bid=" + bid + ", sTargetBid=" + sTargetBid + ", sUnitIsInch=" + sUnitIsInch + ", sX=" + sX + ", sY=" + sY + ", row=" + row);
DocumentInstance       doc             = reportEngines.getDocumentFromStorageToken(strEntry);
ReportElementContainer reportStructure = doc.getStructure();
ReportContainer        report          = (ReportContainer) reportStructure.getChildAt(nReportIndex);
boolean                prefUnitIsInch  = Boolean.valueOf(sUnitIsInch).booleanValue();
UnitType               prefUnit        = prefUnitIsInch ? UnitType.INCH : UnitType.MILLIMETER;
boolean                toDuplicate  = Boolean.valueOf(sDup).booleanValue();
ReportElement movedElem          = report.getReportElement(bid);
ReportElement targetElem         = report.getReportElement(sTargetBid);
ReportElement toRemoveElemFather = null;
Position movedPos = null;
boolean startOnNewpage = false;
boolean isSameContainer = movedElem.getFather().getID().equals(targetElem.getID()) && (row == startRow);
if (isSameContainer) {
if (movedElem instanceof Attachable)
{
_logger.info( "detach : " +movedElem);
((Attachable) movedElem).deleteAttachment();
}
if (toDuplicate ) {
ReportElement copiedElem = ((ReportElementContainer) movedElem.getFather()).copyReportElement(movedElem);
movedPos = ((Position) copiedElem);
startOnNewpage = ((copiedElem instanceof PageLayout) && ((PageLayout) copiedElem).startOnNewPage());
} else {
movedPos = ((Position) movedElem);
startOnNewpage = ((movedElem instanceof PageLayout) && ((PageLayout) movedElem).startOnNewPage());
}
}
else
{
_logger.info( "move to an other container. duplicate=" + toDuplicate);
 ReportElement copiedElem = ((ReportElementContainer) targetElem).copyReportElement(movedElem);
if (!toDuplicate)
{
toRemoveElemFather = movedElem.getFather();
}
        movedPos = ((Position) copiedElem);
        startOnNewpage = ((copiedElem instanceof PageLayout) && ((PageLayout) copiedElem).startOnNewPage());
}
if (toRemoveElemFather != null)
{
((ReportElementContainer) toRemoveElemFather).removeReportElement(movedElem);
}
double adjY = CurrentCellInfo.getYPositionFromRow(targetElem, prefUnit, row, _logger);
movedPos.setUnit(prefUnit);
boolean useDelta = isSameContainer && !toDuplicate;
if (useDelta)
{
movedPos.setX(Math.max(0, movedPos.getX() + ViewerTools.toServerUnit(sDx, prefUnit)));
}
else
{
movedPos.setX(ViewerTools.toServerUnit(sX, prefUnit));
}
if (!startOnNewpage)
{
if (useDelta)
{
movedPos.setY(Math.max(0, movedPos.getY() + ViewerTools.toServerUnit(sDy, prefUnit)));
}
else
{
movedPos.setY(ViewerTools.toServerUnit(sY, prefUnit) + adjY);
}
_logger.info("adjY=" + adjY);
}
else
{
_logger.info("start on new page, so don't change Y pos");
}
doc.applyFormat();
strEntry = doc.getStorageToken();
_logger.info("new strEntry = " + strEntry);
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strQueryString = requestWrapper.getQueryString();
int iPos = strQueryString.indexOf("&sBid");
if (iPos>=0)
  strQueryString = strQueryString.substring(0, iPos);
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
requestWrapper.setQueryString(strQueryString);
_logger.info(" <-- processMoveElement.jsp\n");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_MOVE_ELEMENT", true, out, session);
}
%>

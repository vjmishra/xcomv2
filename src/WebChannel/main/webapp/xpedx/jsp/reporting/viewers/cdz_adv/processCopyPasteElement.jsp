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
String strEntry = null;
String followBid="";
try {
_logger.info("--> processCopyPasteElement.jsp");
strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
    String sBid        = requestWrapper.getQueryParameter("sBid", false);
String iReport     = requestWrapper.getQueryParameter("iReport", false, "0");
String sAction= requestWrapper.getQueryParameter("sAction", false, "copy");
String sTargetBid  = requestWrapper.getQueryParameter("sTargetBid", false);
String sUnitIsInch = requestWrapper.getQueryParameter("sUnitIsInch", false);
String sX          = requestWrapper.getQueryParameter("sX", false);
String sY          = requestWrapper.getQueryParameter("sY", false);
String sRow = requestWrapper.getQueryParameter("sRow", false, "0");
int      nReportIndex = Integer.parseInt(iReport);
boolean prefUnitIsInch  = Boolean.valueOf(sUnitIsInch).booleanValue();
UnitType prefUnit       = prefUnitIsInch ? UnitType.INCH : UnitType.MILLIMETER;
int row = Integer.parseInt(sRow);
DocumentInstance       doc             = reportEngines.getDocumentFromStorageToken(strEntry);
ReportElementContainer reportStructure = doc.getStructure();
ReportContainer        report          = (ReportContainer) reportStructure.getChildAt(nReportIndex);
_logger.info("strEntry = " + strEntry);
boolean _isDocModified = false;
if (sAction.equals("copy")) {
ReportElement toCopyElem = report.getReportElement(sBid);
_logger.info( "sAction=" + sAction + ", sBid=" + sBid + ", toCopyElem=" + toCopyElem);
if (toCopyElem != null) {
ReportElement copiedElem = ((ReportElementContainer) toCopyElem.getFather()).copyReportElement(toCopyElem);
session.setAttribute("CDZ.copyReportElement", copiedElem);
((ReportElementContainer) toCopyElem.getFather()).removeReportElement(copiedElem);
_logger.info("COPIED copiedElem=" + copiedElem);
_isDocModified = true;
}
} else if (sAction.equals("cut")) {
ReportElement toCutElem = report.getReportElement(sBid);
_logger.info( "sAction=" + sAction + ", sBid=" + sBid + ", toCutElem=" + toCutElem);
if (toCutElem != null) {
ReportElement cuttedElem = ((ReportElementContainer) toCutElem.getFather()).copyReportElement(toCutElem);
session.setAttribute("CDZ.copyReportElement", cuttedElem);
((ReportElementContainer) toCutElem.getFather()).removeReportElement(cuttedElem); 
((ReportElementContainer) toCutElem.getFather()).removeReportElement(toCutElem); 
_logger.info( "toCutElem=" + toCutElem);
_logger.info("CUTTED cuttedElem=" + cuttedElem);
_isDocModified = true;
}
} else if (sAction.equals("paste")) {
_logger.info("sAction=" + sAction + ", sTargetBid=" + sTargetBid +  ", sX=" + sX + ", sY=" + sY);
ReportElement toCopyElem = (ReportElement) session.getAttribute("CDZ.copyReportElement");
ReportElement targetElem = (ReportElement) report.getReportElement(sTargetBid);
if ((toCopyElem != null) && (targetElem != null)) {
_logger.info("PASTED toCopyElem=" + toCopyElem + " at x=" + ViewerTools.toServerUnit(sX, prefUnit) + ", y=" + ViewerTools.toServerUnit(sY, prefUnit) + " in targetElem=" + targetElem);
ReportElement copiedElem = ((ReportElementContainer) targetElem).copyReportElement(toCopyElem);
double adjY = CurrentCellInfo.getYPositionFromRow((ReportElement) targetElem, prefUnit, row, _logger);
_logger.info("adjY=" + adjY);
_logger.info("PASTED copiedElem=" + copiedElem);
if (copiedElem != null) {
((Position) copiedElem).setUnit(prefUnit);
((Position) copiedElem).setX(ViewerTools.toServerUnit(sX, prefUnit));
((Position) copiedElem).setY(ViewerTools.toServerUnit(sY, prefUnit) + adjY);
_isDocModified = true;
followBid=copiedElem.getID();
}
}
}
if (_isDocModified) {
doc.applyFormat();
strEntry = doc.getStorageToken();
_logger.info("new strEntry = " + strEntry);
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
} else {
_logger.info("Doc is not modified, keep old storage token.");
}
} catch(IllegalArgumentException e) {
_logger.warn("Paste Exception: element cannot be added to this element ");
_logger.warn(e.toString());
} catch(Exception e) {
objUtils.displayErrorMsg(e, "_ERR_COPY_PASTE_ELEMENT", true, out, session);
} finally {
requestWrapper.setQueryParameter("sEntry", strEntry);
requestWrapper.setQueryParameter("sFollowBid",followBid);
_logger.info("<-- processCopyPasteElement.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
%>

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
_logger.info("--> processResize");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String bid = requestWrapper.getQueryParameter("sBid", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
String sElemZone = requestWrapper.getQueryParameter("sElemZone", false, "");
String strW = requestWrapper.getQueryParameter("iW", false, null);
String strH = requestWrapper.getQueryParameter("iH", false, null);
String strKeepAttach = requestWrapper.getQueryParameter("bKeepAttach", false, "false");
String strOldW = requestWrapper.getQueryParameter("iOldW", false, null);
String strOldH = requestWrapper.getQueryParameter("iOldH", false, null);
boolean bKeepAttach = Boolean.valueOf(strKeepAttach).booleanValue();
DocumentInstance       objDocumentInstance = reportEngines.getDocumentFromStorageToken(strEntry);
ReportElementContainer reportStructure     = objDocumentInstance.getStructure();
ReportContainer        report              = (ReportContainer) reportStructure.getChildAt(nReportIndex);
ReportElement          re                  = report.getReportElement(bid);
if (re == null)
{
CurrentCellInfo cellInfo = new CurrentCellInfo(objDocumentInstance, nReportIndex, bid);
if ((cellInfo != null) && (cellInfo.m_tableCell != null))
{
TableCell cell = cellInfo.m_tableCell;
UnitType unit = cell.getUnit(); 
 _logger.info("Resize cell:");
if (strW != null)
{
boolean autofit = strW.equals("0");
if (autofit) {
_logger.info("Autofit width.");
cell.setColumnAutoFitWidth(true);
if( cell.getColSpan() == 1 )
{
double width = 0.04; 
if (unit != UnitType.INCH) 
{ 
width = (width * 25.4) ;
}
cell.setWidth(width); 
}
} else {
cell.setColumnAutoFitWidth(false);
double width = Double.parseDouble(strW) ; 
if (unit != UnitType.INCH) { 
width = (width * 25.4) ;
}
if (width > 0) { 
cell.setWidth(width);
_logger.info("width=" + width + unit);
} else {
double delta = cell.getWidth() + width;
double newX = Math.max(0.0, (((Position) cell).getX() - delta));
((Position) cell).setX(newX);
cell.setWidth(-width + delta);
_logger.info("width=" + width + unit + " ,delta=" + delta + unit + ", newX=" + newX);
}
}
}
if (strH != null)
{
boolean autofit = strH.equals("0");
if (autofit) {
_logger.info("Autofit height.");
cell.setRowAutoFitHeight(true);
cell.setHeight(cell.estimateMinimalHeight());
} else {
cell.setRowAutoFitHeight(false);
double height = Double.parseDouble(strH) ; 
if (unit != UnitType.INCH) { 
height = (height * 25.4) ;
}
if (height > 0) { 
cell.setHeight(height);
_logger.info("height=" + height + unit);
} else {
double delta = cell.getHeight() + height;
double newY = Math.max(0.0, (((Position) cell).getY() + delta));
((Position) cell).setY(newY);
cell.setHeight(Math.abs(height));
_logger.info("width=" + height + unit + " ,delta=" + delta + unit + ", newY=" + newY);
}
}
}
}
}
else if (re instanceof Cell)
{
_logger.info("Resize free cell:");
Cell cell = (Cell) re;
UnitType unit = cell.getUnit(); 
if (strW != null)
{ 
boolean autofit = strW.equals("0");
if (autofit) {
_logger.info("Autofit width.");
cell.setAutoFitWidth(true);
double width = 0.04; 
if (unit != UnitType.INCH) { 
width = (width * 25.4) ;
}
cell.setWidth(width); 
}
else
{
boolean bAutofitWBefore=cell.isAutoFitWidth();
cell.setAutoFitWidth(false);
double width = Double.parseDouble(strW) ; 
if (unit != UnitType.INCH) { 
width = (width * 25.4) ;
}
if (width > 0) { 
cell.setWidth(width);
_logger.info("width=" + width + unit);
}
else
{
if (cell instanceof Attachable) 
{
if (!bKeepAttach)
{
_logger.info( "detach : " +cell);
((Attachable) cell).deleteAttachment();
}
}
double delta = 0;
if (bAutofitWBefore)
{
if (strOldW!=null)
{
double oldWidth = Double.parseDouble(strOldW) ; 
if (unit != UnitType.INCH)
{ 
oldWidth = (oldWidth * 25.4) ;
}
delta = oldWidth + width;
}
}
else
delta = cell.getWidth() + width;
double newX = (((Position) cell).getX() + delta);
if (newX < 0)
{
width -= newX;
newX = 0;
}
_logger.info("width=" + width + unit + " ,delta=" + delta + unit + ", X= " + ((Position) cell).getX() + ", newX=" + newX );
((Position) cell).setX(newX);
cell.setWidth(Math.abs(width));
}
}
}
if (strH!=null)
{
boolean autofit = strH.equals("0");
if (autofit) {
_logger.info("Autofit height.");
cell.setAutoFitHeight(true);
cell.setHeight(cell.estimateMinimalHeight());
} 
else
{
boolean bAutofitHBefore=cell.isAutoFitHeight(); 
cell.setAutoFitHeight(false);
double height = Double.parseDouble(strH) ; 
if (unit != UnitType.INCH) { 
height = (height * 25.4) ;
}
if (height > 0) { 
cell.setHeight(height);
_logger.info("height=" + height + unit);
} 
else
{
if (cell instanceof Attachable) 
{
if (!bKeepAttach)
{
_logger.info( "detach : " +cell);
((Attachable) cell).deleteAttachment();
}
}
double delta = 0;
if (bAutofitHBefore)
{
if (strOldH!=null)
{
double oldHeight = Double.parseDouble(strOldH) ; 
if (unit != UnitType.INCH)
{ 
oldHeight = (oldHeight * 25.4) ;
}
delta = oldHeight + height;
}
}
else
delta = cell.getHeight() + height;
double newY  = (((Position) cell).getY() + delta);
if (newY < 0)
{
height -= newY;
newY = 0;
}
((Position) cell).setY(newY);
cell.setHeight(Math.abs(height));
_logger.info("width=" + height + unit + " ,delta=" + delta + unit + ", newY=" + newY);
}
}
}
}
else if (re instanceof ReportBlock)
{
_logger.info("Resize chart:");
ReportBlock block = (ReportBlock) re; 
Graph graph = (Graph) block.getRepresentation();
UnitType unit = graph.getUnit(); 
((Unit) block).setUnit(unit); 
if (strW != null) {
double width = Double.parseDouble(strW) ; 
if (unit != UnitType.INCH) { 
width = (width * 25.4) ;
}
if (width > 0) { 
graph.setWidth(width);
_logger.info("width=" + width + unit);
} else {
if (!bKeepAttach)
{
_logger.info( "detach : " +block);
((Attachable) block).deleteAttachment();
}
double delta = graph.getWidth() + width;
double newX = block.getX() + delta;
if (newX < 0)
{
width -= newX;
newX = 0;
}
block.setX(newX);
graph.setWidth(Math.abs(width));
_logger.info("width=" + width + unit + " ,delta=" + delta + unit + ", newX=" + newX);
}
}
if (strH != null) {
double height = Double.parseDouble(strH); 
if (unit != UnitType.INCH) { 
height = (height * 25.4) ;
}
if (height > 0) { 
graph.setHeight(height);
_logger.info("height=" + height + unit);
} else {
if (!bKeepAttach)
{
_logger.info( "detach : " +block);
((Attachable) block).deleteAttachment();
}
double delta = graph.getHeight() + height;
double newY  = block.getY() + delta;
if (newY < 0)
{
height -= newY;
newY = 0;
}
block.setY(newY);
graph.setHeight(Math.abs(height));
_logger.info("height=" + height + unit + " ,delta=" + delta + unit + ", newY=" + newY);
}
}
}
else if (re instanceof SectionContainer)
{
_logger.info("Resize section:");
SectionContainer sc = (SectionContainer) re;
UnitType unit = sc.getUnit();
if (strH!=null)
{
double height = Double.parseDouble(strH);
if (unit != UnitType.INCH) { 
height = (height * 25.4) ;
}
_logger.info("height=" + height + unit);
_logger.info("resize=" + sElemZone + " zone");
if (sElemZone.equals("top"))
{
double top = sc.getY() + height;
_logger.info("top=" + top + unit);
sc.setY(Math.max(0.0,top));
}
else
{
double bottomPadding = sc.getBottomPadding() + height;
_logger.info("bottomPadding=" + bottomPadding + unit);
sc.setBottomPadding(Math.max(0.0, bottomPadding));
}
}
}
else if (re instanceof PageHeaderFooter)
{
_logger.info("Resize header or footer:");
PageHeaderFooter cap = (PageHeaderFooter)re;
cap.setUnit(UnitType.INCH);
if (strH!=null)
{
PageHeaderFooter otherCap = cap.isHeader() ? report.getPageFooter() : report.getPageHeader();
otherCap.setUnit(UnitType.INCH);
PageInfo info = report.getPageInfo();
info.setUnit(UnitType.INCH);
Margins margins = info.getMargins();
margins.setUnit(UnitType.INCH);
double maxH = (info.getOrientation() == Orientation.PORTRAIT ? info.getPaperSizeHeight() : info.getPaperSizeWidth()) - otherCap.getHeight() - margins.getBottom() - margins.getTop();
double h = cap.getHeight() + Double.parseDouble(strH);
maxH = maxH-0.39; 
if (h > maxH) {
h = maxH;
}
cap.setHeight(Math.max(0.0197, h)); 
}
}
objDocumentInstance.applyFormat();
strEntry = objDocumentInstance.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strRedirect = "report.jsp";
requestWrapper.setQueryParameter("sEntry", strEntry);
re = null;
report = null;
reportStructure = null;
objDocumentInstance = null;
_logger.info("<-- processResize");
out.clearBuffer();
%>
<jsp:forward page="<%=strRedirect%>"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_RESIZE", true, out, session);
}
%>

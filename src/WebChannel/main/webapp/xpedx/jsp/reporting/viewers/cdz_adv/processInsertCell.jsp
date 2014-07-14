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
_logger.info("-->processInsertCell.jsp");
String strEntry    = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String bid         = requestWrapper.getQueryParameter("sBid", true);
String newVarIds    = requestWrapper.getQueryParameter("sNewVarIds", true);
String sUnitIsInch = requestWrapper.getQueryParameter("sUnitIsInch", true);
String sX          = requestWrapper.getQueryParameter("sX", true);
String sY          = requestWrapper.getQueryParameter("sY", true);
String sRow = requestWrapper.getQueryParameter("sRow", false, "0");
String iReport     = requestWrapper.getQueryParameter("iReport", false, "0");
String[] IDs = ViewerTools.convertStringToArray(newVarIds);
boolean isSingleSelection = (IDs.length == 1);
int      nReportIndex = Integer.parseInt(iReport);
int row = Integer.parseInt(sRow);
DocumentInstance       doc             = reportEngines.getDocumentFromStorageToken(strEntry);
ReportElementContainer reportStructure = doc.getStructure();
ReportContainer        report          = (ReportContainer) reportStructure.getChildAt(nReportIndex);
ReportElement          re              = report.getReportElement(bid);
ReportDictionary       dict            = doc.getDictionary();
ReportExpression       expr            = getExpression(dict, IDs[0]);
boolean                prefUnitIsInch  =  Boolean.valueOf(sUnitIsInch).booleanValue();
UnitType               prefUnit        = prefUnitIsInch ? UnitType.INCH : UnitType.MILLIMETER;
_logger.info("bid=" + bid + ", sUnitIsInch=" + sUnitIsInch + ", sX=" + sX + ", sY=" + sY + ", newVarIds=" + newVarIds + ", row=" + row);
boolean insertCell = true;
if (isSingleSelection) {
if ((re instanceof ReportBody) || (re instanceof SectionContainer))
{
if (expr.getQualification() != ObjectQualification.MEASURE)
insertCell = false; 
}
} else {
insertCell = false;
}
double adjY = CurrentCellInfo.getYPositionFromRow((ReportElement) re, prefUnit, row, _logger);
_logger.info("adjY=" + adjY);
String followBid="";
if (insertCell)
{
_logger.info("insert Free Cell");
ReportCell cell = null;
if (re instanceof ReportBody)
{
cell = ((ReportBody) re).createReportCell(expr);
}
else if (re instanceof SectionContainer)
{
cell = ((SectionContainer) re).createReportCell(expr);
}
else if (re instanceof PageHeaderFooter)
{
cell = ((PageHeaderFooter) re).createReportCell(expr);
}
String newBid = cell.getID();
_logger.info("new Cell bid = " + newBid);
cell.deleteAttachment();
cell.setUnit(prefUnit);
cell.setX(ViewerTools.toServerUnit(sX, prefUnit));
cell.setY(ViewerTools.toServerUnit(sY, prefUnit) + adjY);
followBid = cell.getID();
}
else
{
int         childCount   = re.getChildCount();
boolean     mustAddBlock = true;
if (isSingleSelection) {
for (int i = 0; i < childCount; i++)
{
 ReportElement child = re.getReportElement(i) ;
 if ((child instanceof SectionContainer) || (child instanceof ReportBlock))
 {
mustAddBlock = false;
break;
 }
}
}
_logger.info("insert " + (mustAddBlock? "Table" : " section+Section Cell") + ", isSingleSelection:" + isSingleSelection);
ReportBlock newBlock  = null;
SectionContainer newSection=null;
if (re instanceof ReportBody)
{
if (mustAddBlock) {
newBlock = ((ReportBody) re).createBlock(TableType.VTABLE);
} else {
((ReportBody) re).setUnit(prefUnit);
newSection=((ReportBody) re).insertSection(expr,ViewerTools.toServerUnit(sY, prefUnit) + adjY); 
_logger.info("insertSection in ReportBody: " + expr + ", ViewerTools.toServerUnit(sY, prefUnit):" + ViewerTools.toServerUnit(sY, prefUnit));
}
}
else if (re instanceof SectionContainer)
{
if (mustAddBlock) {
newBlock = ((SectionContainer) re).createBlock(TableType.VTABLE);
} else {
((Position) re).setUnit(prefUnit);
newSection=((SectionContainer) re).insertSection(expr,ViewerTools.toServerUnit(sY, prefUnit) + adjY); 
_logger.info("insertSection in SectionContainer: " + expr + ", ViewerTools.toServerUnit(sY, prefUnit):" + ViewerTools.toServerUnit(sY, prefUnit));
}
}
if (newBlock != null)
{
_logger.info("newBlock: " + newBlock);
BlockAxis vertAxis = newBlock.getAxis(TableAxis.HORIZONTAL);
if (isSingleSelection) {
vertAxis.addExpr(expr);
} else {
ReportExpression exp;
for (int i=0; i < IDs.length; i++) {
exp = getExpression(dict, IDs[i]);
if (exp != null) {
vertAxis.addExpr(exp);
}
}
}
newBlock.deleteAttachment();
newBlock.setUnit(prefUnit);
newBlock.setX(ViewerTools.toServerUnit(sX, prefUnit));
newBlock.setY(ViewerTools.toServerUnit(sY, prefUnit) + adjY);
followBid = newBlock.getID();
}
else if(newSection!=null)
{
followBid = newSection.getID();
}
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
requestWrapper.setQueryParameter("sFollowBid", followBid);
_logger.info("<--processInsertCell.jsp\n");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_INSERT_CELL", true, out, session);
}
%>
<%!
public synchronized ReportExpression getExpression(ReportDictionary dict, String idOrFormula)
{
ReportExpression expr = null;
try
{
expr = dict.getChild(idOrFormula);
}
catch (Exception e)
{
expr = null;
}
if ((expr == null) && (idOrFormula!=null) && (idOrFormula.startsWith("=")))
{
expr = dict.createFormula(idOrFormula);
}
return expr;
}
%>

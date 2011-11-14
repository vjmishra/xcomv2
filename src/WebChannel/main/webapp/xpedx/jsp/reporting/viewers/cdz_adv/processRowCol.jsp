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
try
{
_logger.info("-->processRowCol.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry = " + strEntry );
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
_logger.info("strViewerID = " + strViewerID );
String bid = requestWrapper.getQueryParameter("sBid", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nRepIndex = Integer.parseInt(iReport);
_logger.info("iReport = " + iReport );
_logger.info("nRepIndex = " + nRepIndex );
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nRepIndex, bid);    
TableCell tc = (TableCell) cellInfo.m_tableCell;
String sVariableID = null;
String sVariableBID = null;
if (tc == null) {
_logger.error("TableCell is null");
} else {
CellMatrix matrix = tc.getCellMatrix();
if (matrix == null)
_logger.error("CellMatrix is null");
else
{
String sAction = requestWrapper.getQueryParameter("iAction", false, "0");
int iAction = Integer.parseInt(sAction);
_logger.info("sAction = " + sAction);
String sDirection = requestWrapper.getQueryParameter("sDir", false);
_logger.info("sDirection = " + sDirection);
int nbCol = matrix.getColumnCount();
int nbRow = matrix.getRowCount();
_logger.info("nbRow = "+nbRow+" nbCol = "+nbCol);
int nRow = 0;
int nCol = 0;
switch (iAction)
{
case 0 :
{
int iWhere = tc.getRow();
int refRow = iWhere;
if (sDirection.equals("below")) {
int rowspan = tc.getRowSpan();
if (rowspan > 1)
iWhere=iWhere+rowspan-1;
iWhere=iWhere+1;
}
nRow = iWhere;
nCol = Integer.parseInt(requestWrapper.getQueryParameter("nCol", false, "0"));
_logger.info("' insert a row with nRow=" + nRow);
if (( iWhere >= 0) && ( iWhere <= nbRow )) {
matrix.insertRow( iWhere, refRow );
}
break;
}
case 2 :
{
int iWhere = tc.getColumn();
int refCol = iWhere;
if (sDirection.equals("right")) {
int colspan = tc.getColSpan();
if (colspan > 1)
iWhere=iWhere+colspan-1;
iWhere=iWhere+1;
}
nCol = iWhere;
nRow=Integer.parseInt(requestWrapper.getQueryParameter("nRow", false, "0"));
_logger.info("' insert a col with nCol=" + nCol);
if ((iWhere >= 0) && (iWhere <= nbCol)) {
matrix.insertColumn( iWhere, refCol );
}
break;
}
}
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
_logger.info("strEntry = " + strEntry );
requestWrapper.setQueryParameter("sEntry", strEntry);
sVariableID = requestWrapper.getQueryParameter("sVarID", false, null);
if (sVariableID != null)
{
_logger.info("variable ID =" + sVariableID);
sVariableBID = matrix.getCell(nRow,nCol).getID();
_logger.info("sVariableBID=" + sVariableBID);
}
requestWrapper.setQueryParameter("bids", sVariableBID);
requestWrapper.setQueryParameter("sFollowBid", matrix.getCell(nRow,nCol).getID());
}
}
_logger.info("<--processRowCol.jsp");
String strRedirect = "report.jsp";
if (sVariableBID != null)
strRedirect = "processFormula.jsp";
out.clearBuffer();
%>
<jsp:forward page="<%=strRedirect%>"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_DEFAULT", true, out, session);
}
%>

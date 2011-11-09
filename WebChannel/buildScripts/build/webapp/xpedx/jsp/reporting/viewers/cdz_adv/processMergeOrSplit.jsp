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
_logger.info("-->processMergeOrSplit.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry = " + strEntry );
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
_logger.info("strViewerID = " + strViewerID );
    String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
    int nRepIndex = Integer.parseInt(iReport);
_logger.info("iReport = " + iReport );
_logger.info("nRepIndex = " + nRepIndex );
String sMerge = requestWrapper.getQueryParameter("bMerge",true);
boolean bMerge=(new Boolean(sMerge)).booleanValue();
_logger.info("sMerge = " + sMerge );
    String sBids = requestWrapper.getQueryParameter("bids", false);
    String[] bids = ViewerTools.split(sBids, ",");
_logger.info("bids.length="+bids.length);
DocumentInstance doc = null;
doc = (DocumentInstance)session.getAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"));
if (doc == null)
{
    doc = reportEngines.getDocumentFromStorageToken(strEntry);
    }
if ( bMerge && (bids.length > 1) )
{
_logger.info("Merging cells");
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nRepIndex, bids[0]);
TableCell topLeftCell = cellInfo.m_tableCell;
TableCell rightBottomCell = cellInfo.m_tableCell;
for (int i = 1; i < bids.length; i++)
{
        CurrentCellInfo nextCellInfo = new CurrentCellInfo(doc, nRepIndex, bids[i]);
        TableCell nextCell = nextCellInfo.m_tableCell;
        _logger.info("nextCell :" + nextCell);
        if (nextCell == null) continue;
        if (( nextCell.getRow() <= topLeftCell.getRow()) && (nextCell.getColumn() <= topLeftCell.getColumn()))
topLeftCell = nextCell;
else if ( ( nextCell.getRow() >= rightBottomCell.getRow() ) && ( nextCell.getColumn() >= rightBottomCell.getColumn() ) )
rightBottomCell = nextCell;
}
int topLeftRow=topLeftCell.getRow();
int topLeftCol=topLeftCell.getColumn();
int rightBotRow=rightBottomCell.getRow();
int rightBotCol=rightBottomCell.getColumn();
_logger.info("Top left cell " + topLeftCell.getText() + "(" + topLeftRow + "," + topLeftCol + ")");
_logger.info("Right bottom cell " + rightBottomCell.getText() + "(" + rightBotRow + "," + rightBotCol + ")");
CellMatrix matrix = topLeftCell.getCellMatrix();
int colspan=0;
for (int column = topLeftCol; column <= rightBotCol; column++)
{
    TableCell cell = matrix.getCell(topLeftRow, column);
    if (cell.getColumn() == column)
colspan += cell.getColSpan();
}
int rowspan=0;
for (int row = topLeftRow; row <= rightBotRow; row++)
{
    TableCell cell = matrix.getCell(row, topLeftCol);
    if (cell.getRow() == row)
rowspan += cell.getRowSpan();
}
_logger.info("Calculated rowspan=" + rowspan + " - Calculated colspan=" + colspan);
int maxRowSpan = matrix.getRowCount() + topLeftRow;
int maxColSpan = matrix.getColumnCount() +topLeftCol;
_logger.info("Max authorized rowspan = " + maxRowSpan + " - Max authorized colSpan = " + maxColSpan);
TableCell cellToUse=topLeftCell;
for (int i = 0; i < bids.length; i++)
{
        CurrentCellInfo nextCellInfo = new CurrentCellInfo(doc, nRepIndex, bids[i]);
        if ( nextCellInfo.m_expr != null )
        {
cellToUse=nextCellInfo.m_tableCell;
break;
        }
}
if (cellToUse!=topLeftCell)
{
topLeftCell.setExpr(cellToUse.getExpr());
topLeftCell.setAlignment(cellToUse.getAlignment());
topLeftCell.setFont(cellToUse.getFont());
topLeftCell.setAttributes(cellToUse.getAttributes());
}
if ( (colspan > 1) && (colspan <= maxColSpan) )
{
_logger.info("Setting colspan to " + colspan);
topLeftCell.setColSpan( colspan );
}
if ( (rowspan > 1) && (rowspan <= maxRowSpan) )
{
_logger.info("Setting rowspan to " + rowspan);
topLeftCell.setRowSpan( rowspan );
}
requestWrapper.setQueryParameter("sFollowBid", topLeftCell.getID());
}
if ( !bMerge && (bids.length == 1) )
{
_logger.info("Splitting cell");
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nRepIndex, bids[0]);
TableCell cell = cellInfo.m_tableCell;
int colspan = cell.getColSpan();
int rowspan = cell.getRowSpan();
if (colspan > 1)
cell.setColSpan( 1 );
if (rowspan > 1)
cell.setRowSpan( 1 );
}
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
_logger.info("<--processMergeOrSplit.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_MERGEORSPLIT", true, out, session);
}
%>

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
_logger.info("-->processRemoveReportElements.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("\t strEntry = " + strEntry );
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
_logger.info("\t strViewerID = " + strViewerID );
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
_logger.info("\t iReport = " + iReport );
_logger.info("\t nReportIndex = " + nReportIndex );
String sBids = requestWrapper.getQueryParameter("bids", false);
String[] arrBids = ViewerTools.split(sBids, ",");
String bid = requestWrapper.getQueryParameter("sBid", false);
if ( (arrBids.length==0) && (bid!=null) )
{
arrBids=new String[1];
arrBids[0]=bid;
}
String strObjects = requestWrapper.getQueryParameter("objects",false,"");
String[] arrObjects = ViewerTools.split(strObjects,",");
_logger.info("\t strObjects = " + strObjects);
if ( arrObjects.length != arrBids.length )
{
_logger.error("List of objects not compatible with selection");
_logger.error("arrBids.length="+arrBids.length+" arrObjects.length="+arrObjects.length);
}
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
ReportElementContainer reportStructure = doc.getStructure();
ReportContainer report = (ReportContainer) reportStructure.getChildAt(nReportIndex);
for (int i = 0; i < arrObjects.length; i++)
{
String strObject = arrObjects[i];
if ( strObject.equals("Row") || strObject.equals("Col") )
{
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, arrBids[i]);
if ( cellInfo.m_tableCell == null )
continue;
CellMatrix matrix = cellInfo.m_tableCell.getCellMatrix();
if ( matrix == null )
continue;
_logger.info("Removing " + strObject + " bid = " + arrBids[i] + " m_expr = <" + cellInfo.m_expr + ">");
int nbCol = matrix.getColumnCount();
int nbRow = matrix.getRowCount();
_logger.info("nbRow = "+nbRow+" nbCol = "+nbCol);
try
{
if ( strObject.equals("Row") )
{
int startRow = cellInfo.m_tableCell.getRow();
int endRow = startRow + cellInfo.m_tableCell.getRowSpan() - 1;
for ( int row = endRow; row >= startRow; row-- )
{
if (( row >= 0) && ( row < nbRow ))
{
_logger.info("Removing row in position ="+row);
matrix.removeRow( row );
}
}
}
if ( strObject.equals("Col") )
{
int startCol = cellInfo.m_tableCell.getColumn();
int endCol = startCol + cellInfo.m_tableCell.getColSpan() - 1;
for ( int col = endCol; col >= startCol; col-- )
{
if (( col >= 0) && ( col < nbCol ))
{
_logger.info("Removing col in position ="+col);
matrix.removeColumn( col );
}
}
}
}
catch (ReportException e)
{
if ( e.getErrorCode()==ReportException.CANNOT_REMOVE_BODY )
{
HZoneType htype = matrix.getHZoneType();
VZoneType vtype = matrix.getVZoneType();
_logger.info("htype = "+htype+" vtype = "+vtype);
_logger.info("Removing entire table");
ReportBlock rpBlk = cellInfo.m_block;
ReportElementContainer father = rpBlk.getFather();
father.removeReportElement( rpBlk );
}
}
}
else
{
String sObject = "";
ReportElement re = report.getReportElement(arrBids[i]);
if (re != null)
{
_logger.info("Get the ReportElement to remove");
if (strObject.equals(""))
{
if ((re!=null)&&(re instanceof ReportBlock))
{
ReportBlock blk = (ReportBlock)re;
if ((blk.getRepresentation().getType() instanceof GraphType))
sObject = "Chart";
else
sObject = "Table";
}
else if (re!=null)
sObject = "Cell";
}
else
sObject = strObject;
}
else
{
_logger.info("No ReportElement found with BID");
if (strObject.equals("Table"))
{
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, arrBids[i]);
re = cellInfo.m_block;
sObject = strObject;
}
}
_logger.info("Removing <"+sObject+">");
if (re == null)
continue;
ReportElementContainer father = re.getFather();
if (sObject.equals(""))
continue;
if ( sObject.equals("Section") )
{
if (father instanceof SectionContainer)
((SectionContainer)father).deleteSection((SectionContainer)re);
else if (father instanceof ReportBody)
((ReportBody)father).deleteSection((SectionContainer)re);
}
else
father.removeReportElement(re);
}
}
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
_logger.info("<--processRemoveReportElements.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_REMOVE", true, out, session);
}
%>

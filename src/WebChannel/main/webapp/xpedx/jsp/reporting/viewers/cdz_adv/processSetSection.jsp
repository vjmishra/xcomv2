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
_logger.info("-->processSetSection.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry : " + strEntry);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String bid = requestWrapper.getQueryParameter("sBid", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
DocumentInstance objDocumentInstance = reportEngines.getDocumentFromStorageToken(strEntry);
CurrentCellInfo cellInfo = new CurrentCellInfo(objDocumentInstance, nReportIndex, bid);
ReportElementContainer container = cellInfo.m_block.getFather();
CellContentType originalContType =  cellInfo.m_tableCell.getContentType();
_logger.info("originalContType : " + originalContType);
_logger.info("cellInfo.m_block : " + cellInfo.m_block + ", cellInfo.m_axis:" + cellInfo.m_axis);
SectionContainer theNewSection = null;
if (container instanceof ReportBody)
theNewSection = ((ReportBody) container).insertSection(cellInfo.m_expr, cellInfo.m_block);
else if (container instanceof SectionContainer)
theNewSection = ((SectionContainer) container).insertSection(cellInfo.m_expr, cellInfo.m_block);
SortElement sortElement = cellInfo.m_axis.getBlockSort().findSort(cellInfo.m_expr);
_logger.info("sortElement : " + sortElement);
if (sortElement != null)
theNewSection.getAxis().getBlockSort().createSortElement(cellInfo.m_expr, sortElement.getType());
ReportCell cells[] = theNewSection.getSectionCells();
for (int i = 0; i < cells.length; i++)
{
if (cells[i].isSection())
{
cells[i].setContentType(originalContType);
break;
}
}
cellInfo.remove();
objDocumentInstance.applyFormat();
strEntry = objDocumentInstance.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strRedirect = "report.jsp";
requestWrapper.setQueryParameter("sEntry", strEntry);
requestWrapper.setQueryParameter("sFollowBid", (theNewSection!=null)?theNewSection.getID():"");
originalContType = null;
container = null;
cellInfo = null;
objDocumentInstance = null;
_logger.info("<--processSetSection.jsp : " + strEntry);
out.clearBuffer();
%>
<jsp:forward page="<%=strRedirect%>"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_SETSECTION", true, out, session);
}
%>

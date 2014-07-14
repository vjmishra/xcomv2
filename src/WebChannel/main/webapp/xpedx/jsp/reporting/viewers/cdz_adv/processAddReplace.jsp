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
_logger.info("-->processAddReplace.jsp");
String strEntry    = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String bid         = requestWrapper.getQueryParameter("sBid", true);
String newVarIds    = requestWrapper.getQueryParameter("sNewVarIds", true);
String strReason   = requestWrapper.getQueryParameter("sReason", true);
String iReport     = requestWrapper.getQueryParameter("iReport", false, "0");
String axisName    = requestWrapper.getQueryParameter("sAxisName", false, "x");
String indexInAxis = requestWrapper.getQueryParameter("sAxisIndex", false, "-1");
String strTarget   = requestWrapper.getQueryParameter("sTarget", false, "");
String strZone     = requestWrapper.getQueryParameter("nZone", false, "0");
boolean  isAdd        = strReason.equals("add");
int      nIndexInAxis = Integer.parseInt(indexInAxis);
int      nReportIndex = Integer.parseInt(iReport);
int      nZone        = Integer.parseInt(strZone);
String[] IDs = ViewerTools.convertStringToArray(newVarIds);
DocumentInstance doc  = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("processAddReplace bid=" + bid+ ", newVarIds=" + newVarIds + ", IDs[0]:" + IDs[0] + ", nZone:" + nZone + ", nIndexInAxis:" + nIndexInAxis);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
_logger.info(" cellInfo=" + cellInfo);
int delta=0;
String followBid="";
switch(nZone)
{
case 1: 
case 2: 
delta=1;
break;
case 4: 
case 5: 
delta=-1;
break;
}
_logger.info(" isAdd=" + isAdd + ", delta=" + delta);
if (isAdd || (delta != 0)){
_logger.info(" isAdd||(delta!=0))=true");
TableCell tc = cellInfo.m_tableCell;
CellMatrix matrix = tc.getCellMatrix();
int nRow= tc.getRow(),nCol=tc.getColumn();
if (cellInfo.isChart()) {
_logger.info(" isChart");
cellInfo.add(IDs[0], axisName, nIndexInAxis);
} else {
_logger.info(" not isChart");
cellInfo.add(IDs[0],delta>=0);
}
_logger.info(" add is done.");
switch(nZone)
{
case 1: 
nCol=nCol+1+tc.getColSpan()-1;
break;
case 2: 
nRow=nRow+1+tc.getRowSpan()-1;
break;
case 4: 
break;
case 5: 
break;
}
matrix = tc.getCellMatrix();
if(nRow>=0 && nCol>=0)
followBid=matrix.getCell(nRow,nCol).getID();
} else {
_logger.info(" isAdd||(delta!=0))=false");
if (cellInfo.isChart()) {
_logger.info(" isChart");
cellInfo.replace(IDs[0], axisName, nIndexInAxis);
} else {
_logger.info(" not isChart");
cellInfo.replace(IDs[0]);
}
_logger.info(" replace is done.");
followBid=bid;
}
doc.applyFormat();
_logger.info(" doc.applyFormat()");
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strQueryString = requestWrapper.getQueryString();
int iPos = strQueryString.indexOf("&sBid");
if (iPos>=0)
  strQueryString = strQueryString.substring(0, iPos);
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
requestWrapper.setQueryString(strQueryString);
requestWrapper.setQueryParameter("sFollowBid", followBid);
_logger.info("<-- processAddReplace.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_ADD_OR_REPLACE", true, out, session);
}
%>

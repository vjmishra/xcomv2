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
if (!isAlive) return;
try
{
_logger.info("-->processSorts.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String bid = requestWrapper.getQueryParameter("sBid", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
String actionList = requestWrapper.getQueryParameter("iSortAction", false, "-2");
_logger.info("actionList="+actionList);
String[] arrAction = ViewerTools.split(actionList,",");
String sortPriority = requestWrapper.getQueryParameter("sSortPriority", false, "highest");
_logger.info("sortPriority="+sortPriority);
String axisName = requestWrapper.getQueryParameter("sAxisName", false);
_logger.info("axisName="+axisName);
String indexInAxis = requestWrapper.getQueryParameter("sAxisIndex", false, "-1");
int nIndexInAxis = Integer.parseInt(indexInAxis);
_logger.info("indexInAxis="+indexInAxis);
String sbSortHAxisList = requestWrapper.getQueryParameter("bSortHAxis", false,"");
_logger.info("sbSortHAxisList="+sbSortHAxisList);
String[] arrbSortHAxis = ViewerTools.split(sbSortHAxisList,",");
String sSortIdxList = requestWrapper.getQueryParameter("sSortIdx", false,""); 
_logger.info("sSortIdxList="+sSortIdxList);
String[] arrSortIdx = ViewerTools.split(sSortIdxList,",");
String sStepList = requestWrapper.getQueryParameter("sStep", false,"");
_logger.info("sStepList="+sStepList);
String[] arrsStep = ViewerTools.split(sStepList,",");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
boolean bNothingWasDone = false;
for (int i=0; i<arrAction.length; i++)
{
String sortAction = arrAction[i];
int action = Integer.parseInt( sortAction );
switch(action)
{
case -2:
if ( arrAction.length == 1 )
bNothingWasDone=true;
break;
case -1:
{
if (cellInfo != null)
cellInfo.removeAllSorts();
break;
}
case 0:
case 1:
case 2:
{
if (cellInfo.isChart()) {
cellInfo.applyGraphSort(sortAction, axisName, nIndexInAxis, sortPriority);
} else {
cellInfo.applySort(sortAction, sortPriority);
}
break;
}
case 3:
{
boolean bHAxis = false;
if ( arrbSortHAxis[i]!=null )
bHAxis = (new Boolean(arrbSortHAxis[i])).booleanValue();
int sortIndex=0;
if ( arrSortIdx[i]!=null )
sortIndex=Integer.parseInt( arrSortIdx[i] );
int istep=0;
if ( arrsStep[i]!=null )
istep=Integer.parseInt( arrsStep[i] );
ReportBlock repBlk = cellInfo.m_block;
BlockSort blkSort = repBlk.getBlockSort(bHAxis);
int toIndex=sortIndex+istep;
try
{
blkSort.move(sortIndex,toIndex);
}
catch (Exception e)
{
_logger.error("Could not move sort");
}
break;
}
case 4:
{
BlockSort blkSort = null;
if ( cellInfo.m_isSection )
{
BlockAxis blkAxis = cellInfo.m_axis;
blkSort = blkAxis.getBlockSort();
}
else
{
boolean bHAxis = false;
if ( arrbSortHAxis[i]!=null )
bHAxis = (new Boolean(arrbSortHAxis[i])).booleanValue();
ReportBlock repBlk = cellInfo.m_block;
blkSort = repBlk.getBlockSort(bHAxis);
}
int sortIndex=0;
if ( arrSortIdx[i]!=null )
sortIndex=Integer.parseInt( arrSortIdx[i] );
try
{
blkSort.removeSort(sortIndex);
}
catch (Exception e)
{
_logger.error("Could not remove sort");
}
break;
}
}
}
if (!bNothingWasDone)
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
_logger.info("<--processSorts.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_SORT", true, out, session);
}
%>
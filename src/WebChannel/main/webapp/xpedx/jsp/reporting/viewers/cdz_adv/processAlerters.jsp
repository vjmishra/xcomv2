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
_logger.info("-->processAlerters.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("strEntry = " + strEntry );
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
String sBids = requestWrapper.getQueryParameter("bids", false);
    String[] bids = ViewerTools.split(sBids, ",");
String sAlerterIDS = requestWrapper.getQueryParameter("alrtIDS", false);
_logger.info( "sAlerterIDS = " + sAlerterIDS );
if ( sAlerterIDS != null )
{
String[] arrAlrtIDs = ViewerTools.split(sAlerterIDS, ";");
for (int k = 0; k < bids.length; k++) 
{
    CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bids[k]);
Alerters cellAlerters = cellInfo.m_tableCell.getAlerters();
_logger.info( "Cell BID = " + bids[k] + " - This cell has <" + cellAlerters.getCount() + "> Alerters" );
for ( int i = 0; i < arrAlrtIDs.length; i++ )
{
String[] arrVal = ViewerTools.split(arrAlrtIDs[i],",");
String sID = arrVal[0];
boolean bActivate = (new Boolean(arrVal[1])).booleanValue();
_logger.info( "Alerter ID = " + sID + " bActivate = " + bActivate );
Alerter cellAlerter = cellAlerters.getAlerter( sID );
if ( cellAlerter == null )
_logger.info( "CellAlerter does not exist" );
else
{
_logger.info( "CellAlerter exists " + cellAlerter.getID() + " - " + cellAlerter.getName() );
if ( !cellAlerter.getID().equals( sID ) )
_logger.error("SDK did not returned the corresponding alerter");
}
if ( bActivate && (cellAlerter==null) )
{
cellAlerters.addAlerter( sID );
_logger.info( "Alerter sID = " + sID + " added" );
}
if ( !bActivate && (cellAlerter!=null) )
{
cellAlerters.removeAlerter( sID );
_logger.info( "Alerter sID = " + sID + " removed" );
}
}
}
doc.applyFormat();
}
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
_logger.info("strEntry = " + strEntry );
requestWrapper.setQueryParameter("sEntry", strEntry);
_logger.info("<--processAlerters.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_ALERTER", true, out, session);
}
%>
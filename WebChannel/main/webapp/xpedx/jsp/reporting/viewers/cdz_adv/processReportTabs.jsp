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
_logger.info("-->processReportTabs.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry = " + strEntry );
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
_logger.info("strViewerID = " + strViewerID );
    String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
    int nRepIndex = Integer.parseInt(iReport);
_logger.info("iReport = " + iReport );
_logger.info("nRepIndex = " + nRepIndex );
String sAction = requestWrapper.getQueryParameter("iAction", false, "0");
int iAction = Integer.parseInt(sAction);
_logger.info("sAction = " + sAction );
String sRepName = requestWrapper.getQueryParameter("sParam1", false);
_logger.info("sRepName = " + sRepName );
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
String sNewRepName = "";
int nDestIndex = nRepIndex;
String strUpdateArrayAction="";
String sPageMode = "";
String sRepMode = "";
int errCode = 0;
ReportStructure rs = doc.getStructure();
int nbReport = rs.getReportElementCount();
    switch (iAction)
    {
case 0 :
{
ReportContainer rc = (ReportContainer)rs.getReportElement( nRepIndex );
rc.setName(sRepName);
doc.applyFormat();
nDestIndex = nRepIndex;
break;
}
case 1 :
{
if ( nbReport > 1 )
{
rs.removeReportElementAt( nRepIndex );
doc.applyFormat();
}
else
errCode=-1;
if ( nRepIndex == 0 )
nDestIndex = 0;
else if ( nRepIndex == (nbReport-1) )
nDestIndex = rs.getReportElementCount()-1;
else
nDestIndex = nRepIndex-1;
break;
}
case 2 :
{
int numReport = nbReport + 1;
if ( sRepName.endsWith("1") )
{
String tmp = sRepName.substring( 0, sRepName.length()-1 ) ;
sRepName = tmp;
}
sNewRepName = sRepName + numReport;
nDestIndex=rs.getReportElementCount();
ReportContainer rc = doc.createReport( sNewRepName );
doc.applyFormat();
Report rep = doc.getReports().getItem( sNewRepName );
sPageMode = rep.getPaginationMode().toString();
sRepMode = rep.getReportMode().toString();
break;
}
case 3 :
{
nDestIndex=nbReport;
ReportElement re = rs.getReportElement( nRepIndex );
ReportContainer newRep = (ReportContainer)rs.copyReportElement( re );
sNewRepName = buildNewReportName( _logger, doc, newRep );
newRep.setName( sNewRepName );
doc.applyFormat();
Report repDup = doc.getReports().getItem( sNewRepName );  
 ReportEngine objReportEngine = reportEngines.getServiceFromStorageToken(strEntry);   
if (objReportEngine.getCanTrackData()) {
Report repOrigin = doc.getReports().getItem(nRepIndex);
TrackDataInfo tdi = repOrigin.getTrackDataInfo();
repDup.getTrackDataInfo().showChanges(tdi.isActive());
}
sPageMode = repDup.getPaginationMode().toString();
sRepMode = repDup.getReportMode().toString();
break;
}
case 4 :
{
String sTo = requestWrapper.getQueryParameter("sTo", false);
nDestIndex = Integer.parseInt(sTo);
_logger.info( "fromIndex = " + nRepIndex + " nDestIndex = " + nDestIndex );
if (( nDestIndex >= 0 ) && ( nDestIndex <= nbReport ))
{
rs.move( nRepIndex, nDestIndex );
doc.applyFormat();
}
else
errCode=-1;
break;
}
    }
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
_logger.info("<--processReportTabs.jsp");
%>
<script language="javascript">
var p=parent;
var errCode=<%=errCode%>;
var iAction=<%=iAction%>;
switch (iAction)
{
case 0 :
p.arrReports[<%=nRepIndex%>].name='<%=ViewerTools.escapeQuotes(sRepName)%>';
break;
case 1 :
p.arrayRemove(p, "arrReports", <%=nRepIndex%>);
break;
case 2 :
p.arrReports[p.arrReports.length]=p.newReportInfo('<%=ViewerTools.escapeQuotes(sNewRepName)%>', '<%=sPageMode%>', '<%=sRepMode%>', 'NaN');
break;
case 3 :
p.arrReports[p.arrReports.length]=p.newReportInfo('<%=ViewerTools.escapeQuotes(sNewRepName)%>', '<%=sPageMode%>', '<%=sRepMode%>', 'NaN');
break;
case 4 :
if (errCode==0)
p.arrayMove(p, 'arrReports',<%=nRepIndex%>, <%=nDestIndex%>);
break;
}
p.eventManager.notify(p._EVT_REP_DATAOK);
p.selectReport(<%=nDestIndex%>,true,true,'<%=strEntry%>',false);
</script>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_REPORT", true, out, session);
}
%>
<%!
boolean existsName( DHTMLLogger _logger, DocumentInstance doc, String s )
throws IOException
{
Reports reports = doc.getReports();
int count = reports.getCount();
for ( int i = 0; i < count; i++ )
{
if ( reports.getItem(i).getName().equals(s) )
return true;
}
return false;
}
String buildNewReportName( DHTMLLogger _logger, DocumentInstance doc, ReportContainer repToCopy )
throws IOException
{
String oldName=repToCopy.getName();
String newName="";
int endIdx=-1;
if ( (oldName.length()>0) && ( oldName.charAt(oldName.length()-1) == ')') )
{
endIdx=oldName.length()-2;
while (endIdx>=0)
{
char c=oldName.charAt(endIdx);
if ((c=='0')||(c=='1')||(c=='2')||(c=='3')||(c=='4')||(c=='5')||(c=='6')||(c=='7')||(c=='8')||(c=='9'))
endIdx--;
else if (c=='(')
{
break;
}
else
{
endIdx=-1;
break;
}
}
}
if (endIdx>0)
newName=oldName.substring(0,endIdx);
else
newName=oldName;
int i=1;
String common=newName+"("+i+")";
while( existsName( _logger, doc, common ) )
{
i++;
common=newName+"("+i+")";
}
newName=common;
return newName;
}
%>

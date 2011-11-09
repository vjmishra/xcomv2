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
<jsp:useBean id="oBreaks" class="com.businessobjects.adv_ivcdzview.Breaks" scope="page" />
<%
response.setDateHeader("expires", 0);
try
{
_logger.info("-->processBreaks.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry IN = " + strEntry );
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(strReport);
String bid = requestWrapper.getQueryParameter("sBid", true);
_logger.info("bid = " + bid);
DocumentInstance doc = null;
doc = (DocumentInstance)session.getAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"));
if (doc == null)
{
    doc = reportEngines.getDocumentFromStorageToken(strEntry);
    }
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
BreakElement cellBrkElt = null;
if ( cellInfo.m_expr != null )
cellBrkElt = cellInfo.m_axis.getBlockBreak().findBreak( cellInfo.m_expr );
String blockID = requestWrapper.getQueryParameter("sBlockID", false);
ReportBlock repBlk = null;
if ( blockID != null )
{
_logger.info("blockID = " + blockID);
ReportElementContainer rs = doc.getStructure();
ReportElement re = rs.getReportElement( blockID );
repBlk = (ReportBlock)re;
}
else
repBlk = cellInfo.m_block;
String sId = requestWrapper.getQueryParameter("iId", false);
String sAction = requestWrapper.getQueryParameter("iAction", false, "2");
int action = Integer.parseInt( sAction );
String followBid="";
switch(action)
{
case 0:
case 1:
{
String sAxis = requestWrapper.getQueryParameter("iAxis", false);
AxisType axisType;
if ( sAxis == null )
axisType = cellInfo.m_axisType;
else
{
int val = Integer.parseInt( sAxis );
axisType = getAxis( val );
}
BlockBreak blockBrk = repBlk.getAxis( axisType ).getBlockBreak();
int id;
if (sId == null)
id = getBreakID( blockBrk, cellInfo.m_expr );
else
id = Integer.parseInt( sId );
_logger.info("id = " + id);
String sAddTableHeader = requestWrapper.getQueryParameter("bAddTableHeader",
false,"true");
sId = String.valueOf( addOrRemoveBreak( _logger, repBlk, doc, cellInfo.m_expr, action, axisType,
id, sAddTableHeader ) );
followBid=bid; 
break;
}
case 2:
{
String strBrkProps = requestWrapper.getQueryParameter("sBreaksProps", false, "");
applyBreakProps( _logger, repBlk, out, doc, strBrkProps );
break;
}
case 3:
{
String sAxis = requestWrapper.getQueryParameter("iAxis", false);
_logger.info("sAxis = " + sAxis );
String sMove = requestWrapper.getQueryParameter("iMove", false);
_logger.info("sMove = " + sMove );
sId = String.valueOf( changeBreakOrder( _logger, repBlk, doc, sAxis, sId, sMove ) );
break;
}
}
doc.applyFormat();
requestWrapper.setQueryParameter("sFollowBid", followBid);
String sbWriteInHiddenFrame = requestWrapper.getQueryParameter("bWriteInHiddenFrame", false, "false");
boolean bWriteInHiddenFrame = Boolean.valueOf( sbWriteInHiddenFrame ).booleanValue();
if ( !bWriteInHiddenFrame )
{
    strEntry = doc.getStorageToken();
    objUtils.setSessionStorageToken(strEntry, strViewerID, session);
    requestWrapper.setQueryParameter("sEntry", strEntry);
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
else
{
    session.setAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"), doc);
%>
<script language="javascript" src="language/<%=(String)session.getAttribute("CDZ.Language")%>/scripts/breaks.js"></script>
<script language="javascript">
<%
oBreaks.printBreaksProperties( out, repBlk, cellBrkElt );
%>
parent.updateBrkDlgWidgets(blockHBrk,blockVBrk,"<%=strEntry%>",<%=sId%>)
</script>
<%
}
_logger.info("strEntry OUT = " + strEntry );
_logger.info("<--processBreaks.jsp");
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_BREAK", true, out, session);
}
%>
<%!
int addOrRemoveBreak( DHTMLLogger _logger, ReportBlock repBlk, DocumentInstance doc,
ReportExpression expr, int action, AxisType axisType, int id, String sAddTableHeader )
throws IOException, java.text.ParseException
{
int selBrk = -1;
BlockBreak blockBrk = repBlk.getAxis( axisType ).getBlockBreak();
if ( action == 0 )
{
_logger.info("Removing break");
if ( ( id >= 0 ) && ( id < blockBrk.getCount() ) )
{
blockBrk.removeBreak( id );
selBrk = (id > 0) ? (id-1) : 0;
int nbBreak = blockBrk.getCount();
_logger.info("nbBreak="+nbBreak+", sAddTableHeader="+sAddTableHeader);
if ( nbBreak == 0 )
{
Representation repr = repBlk.getRepresentation();
switch (repr.getType().value())
{
case TableType._VTABLE:
case TableType._HTABLE:
case TableType._XTABLE:
Table table = (Table) repr;
boolean bHeader = (new Boolean(sAddTableHeader)).booleanValue();
table.setHeaderVisible(bHeader);
break;
}
}
}
}
if ( action == 1 )
{
_logger.info("Inserting break");
blockBrk.createBreakElement( expr );
selBrk = getBreakID( blockBrk, expr );
}
return selBrk;
}
void applyBreakProps( DHTMLLogger _logger, ReportBlock repBlk, JspWriter out, DocumentInstance doc,
String strBrkProps )
throws IOException, java.text.ParseException
{
_logger.info("strBrkProps="+strBrkProps);
    String[] arrBrksProps = ViewerTools.split(strBrkProps,";");
_logger.info("arrBrksProps.length="+arrBrksProps.length);
if ( arrBrksProps.length == 0 )
return ;
for ( int i = 0; i < arrBrksProps.length; i++ )
{
String[] arrBrkProps = ViewerTools.split(arrBrksProps[i], ",");
_logger.info("arrBrkProps.length="+arrBrkProps.length);
if ( arrBrkProps.length != 13 )
return ;
int val = Integer.parseInt( arrBrkProps[1] );
_logger.info("val = "+val);
AxisType brkAxis = getAxis( val );
_logger.info("brkAxis = "+brkAxis);
BlockBreak blockBrk = repBlk.getAxis( brkAxis ).getBlockBreak();
val = Integer.parseInt(arrBrkProps[2]);
BreakElement brk = blockBrk.getBreakElement( val );
if ( brk == null )
continue;
_logger.info("brk.getExpression().getName()="+brk.getExpression().getName()+" / arrBrkProps[0] = " + arrBrkProps[0]);
boolean bVal = Boolean.valueOf( arrBrkProps[3] ).booleanValue();
brk.setHeaderVisible( bVal );
_logger.info("setHeaderVisible = " + bVal );
bVal = Boolean.valueOf( arrBrkProps[4] ).booleanValue();
brk.setFooterVisible( bVal );
_logger.info("setFooterVisible = " + bVal );
bVal = Boolean.valueOf( arrBrkProps[5] ).booleanValue();
brk.setValueRepeated( !bVal );
_logger.info("setValueRepeated = " + bVal );
bVal = Boolean.valueOf( arrBrkProps[6] ).booleanValue();
brk.setValueCentered( bVal );
_logger.info("setValueCentered = " + bVal );
bVal = Boolean.valueOf( arrBrkProps[7] ).booleanValue();
brk.setImplicateSort( bVal );
_logger.info("setImplicateSort = " + bVal );
bVal = Boolean.valueOf( arrBrkProps[8] ).booleanValue();
brk.setStartOnNewPage( bVal );
_logger.info("setStartOnNewPage = " + bVal );
bVal = Boolean.valueOf( arrBrkProps[9] ).booleanValue();
brk.setAvoidPageBreak( bVal );
_logger.info("avoidPageBreak = " + bVal );
bVal = Boolean.valueOf( arrBrkProps[10] ).booleanValue();
brk.setHeaderOnEveryPage( bVal );
_logger.info("setHeaderOnEveryPage = " + bVal );
bVal = Boolean.valueOf( arrBrkProps[11] ).booleanValue();
brk.setValueOnNewPage( bVal );
_logger.info("setValueOnNewPage = " + bVal );
}
}
int changeBreakOrder( DHTMLLogger _logger, ReportBlock repBlk,
DocumentInstance doc, String sAxis,
String sId, String sMove )
throws IOException, java.text.ParseException
{
int axis = Integer.parseInt( sAxis );
AxisType brkAxis = getAxis( axis );
BlockBreak blockBrk = repBlk.getAxis( brkAxis ).getBlockBreak();
int id = Integer.parseInt( sId );
int newPos;
int order = Integer.parseInt( sMove );
try
{
newPos = id+order;
blockBrk.move( id, newPos );
}
catch (java.lang.IndexOutOfBoundsException e)
{
newPos = id;
_logger.error("<-- invalid move");
}
_logger.info("oldPos="+id+"newPos="+newPos);
return newPos;
}
AxisType getAxis( int axis )
{
TableAxis brkAxis;
if ( axis == 0 )
return TableAxis.HORIZONTAL;
else if ( axis  == 1 )
return TableAxis.VERTICAL;
else
return TableAxis.CONTENT;
}
int getBreakID( BlockBreak blockBrk, ReportExpression expr )
{
BreakElement brk = blockBrk.findBreak( expr );
if ( brk == null )
return -1;
for (int i=0; i < blockBrk.getCount(); i++)
{
if ( brk.equals(blockBrk.getBreakElement(i)) )
return i;
}
return -1;
}
%>
<%@ include file="incStartpage.jsp" %>
<jsp:useBean id="oBreaks" class="com.businessobjects.adv_ivcdzview.Breaks" scope="page" />
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incBreaks.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("strEntry = " + strEntry );
String bid = requestWrapper.getQueryParameter("sBid", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
String blockID = requestWrapper.getQueryParameter("blockID", false);
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
if ( repBlk != null )
{
BreakElement cellBrkElt = null;
if ( cellInfo.m_expr != null )
cellBrkElt = cellInfo.m_axis.getBlockBreak().findBreak( cellInfo.m_expr );
int tableType = repBlk.getRepresentation().getType().value();
String sTableType="";
String sSelTab="";
switch(tableType)
{
case TableType._HTABLE:
sTableType="HTABLE";
sSelTab="HORIZ_TAB";
break;
case TableType._VTABLE:
sTableType="VTABLE";
sSelTab="VERTI_TAB";
break;
case TableType._XTABLE:
sTableType="XTABLE";
if ( cellInfo.m_axisType == TableAxis.HORIZONTAL )
sSelTab="HORIZ_TAB";
else
sSelTab="VERTI_TAB";
break;
case TableType._FORM:
sTableType="FORM";
sSelTab="VERTI_TAB";
break;
}
out.println( "var sTableType = \"" + sTableType + "\";" );
_logger.info( "sTableType = " + sTableType );
out.println( "selTab = " + sSelTab + ";" );
_logger.info( "sSelTab = " + sSelTab );
oBreaks.printBreaksProperties(out, repBlk, cellBrkElt);
}
_logger.info("<--incBreaks.jsp");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>
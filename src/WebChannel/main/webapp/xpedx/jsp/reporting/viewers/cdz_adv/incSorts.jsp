<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incSorts.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("strEntry = " + strEntry );
String bid = requestWrapper.getQueryParameter("sBid", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
if (cellInfo==null)
_logger.error("No CurrentCellInfo found");
String sBlockType="";
String sSelTab="";
if ( cellInfo.isChart() )
{
sBlockType="GRAPH";
if ( cellInfo.m_block.getAxis(GraphAxis.X) == cellInfo.m_axisType )
sSelTab="HORIZ_TAB";
else
sSelTab="VERTI_TAB";
}
else if ( cellInfo.m_isSection )
{
sBlockType="SECTION";
sSelTab="HORIZ_TAB";
}
else
{
if (cellInfo.m_block==null)
_logger.error("Cell has no reportBlock");
int tableType = cellInfo.m_block.getRepresentation().getType().value();
switch(tableType)
{
case TableType._HTABLE:
sBlockType="HTABLE";
sSelTab="HORIZ_TAB";
break;
case TableType._VTABLE:
sBlockType="VTABLE";
sSelTab="VERTI_TAB";
break;
case TableType._XTABLE:
sBlockType="XTABLE";
if ( cellInfo.m_axisType == TableAxis.HORIZONTAL )
sSelTab="HORIZ_TAB";
else
sSelTab="VERTI_TAB";
break;
case TableType._FORM:
sBlockType="FORM";
sSelTab="VERTI_TAB";
break;
}
}
out.println( "var sBlockType = \"" + sBlockType + "\";" );
out.println( "var selTab = " + sSelTab + ";" );
out.println( "var arrH = new Array;" );
out.println( "var arrV = new Array;" );
if ( cellInfo.m_isSection )
{
BlockAxis blkAxis = cellInfo.m_axis;
BlockSort blkSortH = blkAxis.getBlockSort();
printSortProperties(out,"arrH",blkSortH,true);
}
else
{
ReportBlock repBlk = cellInfo.m_block;
BlockSort blkSortH = repBlk.getBlockSort(true);
BlockSort blkSortV = repBlk.getBlockSort(false);
if (sBlockType.equals("FORM"))
printSortProperties(out,"arrV",blkSortH,true);
else
printSortProperties(out,"arrH",blkSortH,true);
printSortProperties(out,"arrV",blkSortV,false);
}
_logger.info("<--incSorts.jsp");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>
<%!
    public static void printSortProperties(JspWriter out, String arrName, BlockSort blkSort,boolean bHAxis)
throws IOException, java.text.ParseException 
    {
if ( blkSort!=null )
{
int sCount = blkSort.getCount();
    for (int j = 0; j < sCount; j++)
    {
        SortElement sortE = blkSort.getSortElement(j);
        SortType sType = sortE.getType(); 
        ReportExpression rexpr = sortE.getExpression();
        boolean canHaveCustom = !((rexpr instanceof FormulaExpression) || (rexpr.getQualification() == ObjectQualification.MEASURE)) ;
        if (sType != SortType.NONE)
            out.println( arrName+"[" + j + "] = newSortProps(" + j + ",\"" + ViewerTools.escapeQuotes(rexpr.getName()) + "\",\"" + ViewerTools.escapeQuotes(rexpr.getID()) + "\"," + (sType == SortType.ASCENDING ? 1 : 2) + "," + bHAxis  + ","  + canHaveCustom + ");" );
    }        
}
    }
%>
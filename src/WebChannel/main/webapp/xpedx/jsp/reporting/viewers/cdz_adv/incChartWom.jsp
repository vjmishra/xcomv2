<%@ include file="incStartpage.jsp" %>
<%@ include file="incWomFunctions.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
    _logger.info("--> incChartWom");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
    String sBid = requestWrapper.getQueryParameter("sBid", true);
    String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
    int nReportIndex = Integer.parseInt(iReport);
    _logger.info("bid=" + sBid);
    ReportElementContainer rs= doc.getStructure();
    ReportContainer     report= (ReportContainer) rs.getChildAt(nReportIndex);
    CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, sBid);
    ReportBlock block = cellInfo.m_block;
    if (block != null) {
    _logger.info("block: " + block);
    StringBuffer sbToOut = new StringBuffer();
    printReportBlockForChart(sbToOut, block, _logger);
    out.println(sbToOut.toString());
    }
    _logger.info("<-- incChartWom");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>
<%!
void printReportBlockForChart(StringBuffer out, ReportBlock block, DHTMLLogger _logger)
throws IOException
{
    Representation repr = block.getRepresentation();
    BlockType blockType = repr.getType();
    printBlock(out, block, repr, _logger);
    printGraph(out, block);
}
void printGraph(StringBuffer out, ReportBlock block)
throws IOException
{
    Graph graph = (Graph) block.getRepresentation();
    StringBuffer s = new StringBuffer("var varGraph = graph(");
    s.append(((graph.getUnit().value()== UnitType._INCH )? "1" : "0") + ","); 
    s.append( graph.getWidth() + ",");
    s.append( graph.getHeight() + ",");
    s.append( (graph.getTitle().isVisible()?"1":"0")  + ",");
    s.append( "\"" +  ViewerTools.escapeQuotes(graph.getTitle().getTitle()) + "\",");
    s.append( (graph.getLegend().isVisible()?"1":"0")  + ",");
    s.append( graph.getLegend().getPosition().value() +  ",");
    s.append( (graph.getData().isVisible()?"1":"0") + ",");
    s.append( (graph.is3DLook()?"1":"0"));
    s.append(");");
    out.append(s.toString());
    s = new StringBuffer("var varGraphApp = graphApp(");
    s.append((graph.is3D()? "1":"0")  + ",");
    s.append((graph.isBottomWallVisible()? "1":"0")  + ",");
    s.append(getColorString(graph.getWallColor()) + ",");
    s.append((graph.isLeftWallVisible()? "1":"0")  + ",");
    s.append((graph.isRightWallVisible()? "1":"0")  + ",");
    s.append(getColorString(graph.getPrimaryDataColor()) + ",");
    s.append((graph.isValueInPercentage()? "1":"0")  + ",");
    BlockAxis axis = null;
    GraphAxisProperties graphXAxisProp = graph.getAxis(GraphAxis.X);
    s.append((graphXAxisProp.getLabel().isVisible()? "1":"0")   + ",");
    s.append(graphXAxisProp.getValues().getFrequency() + ",");
    s.append(graphXAxisProp.getValues().getOrientation().value() + ",");
    s.append((graphXAxisProp.getGrid().isVisible()? "1":"0")  + ",");
    s.append(getColorString(graphXAxisProp.getGrid().getColor()) + ",");
    axis = null;
    try
    {
        axis = block.getAxis(GraphAxis.X);
    }
    catch (Exception e)  { }
    FormatNumber formatNumber =  (((axis != null) && (axis.getCount() > 0)) ? graphXAxisProp.getFormatNumber(0): null); 
    s.append("\"" + ((formatNumber != null) ? formatNumber.getSample():"") +  "\",");
    s.append("\"" + ((formatNumber != null) ? (String.valueOf(formatNumber.getType().value())):"")  + "\",");
    GraphAxisProperties graphYAxisProp = graph.getAxis(GraphAxis.Y);
    s.append((graphYAxisProp.getLabel().isVisible()? "1":"0")   + ",");
    s.append(graphYAxisProp.getValues().getFrequency() + ","); 
    s.append((graphYAxisProp.hasMinValue()? "1":"0") + ",");
    s.append(graphYAxisProp.getMinValueGraph() + ",");
    s.append((graphYAxisProp.hasMaxValue()? "1":"0") + ",");
    s.append(graphYAxisProp.getMaxValueGraph() + ",");
    s.append((graphYAxisProp.isLogarithmic()? "1":"0") + ",");
    s.append(graphYAxisProp.getValues().getOrientation().value() + ",");
    s.append((graphYAxisProp.getGrid().isVisible()? "1":"0")  + ",");;
    s.append(getColorString(graphYAxisProp.getGrid().getColor()) + ",");
    axis = null;
    try
    {
        axis = block.getAxis(GraphAxis.Y);
    }
    catch (Exception e)  { }
    formatNumber =  (((axis != null) && (axis.getCount() > 0))  ? graphYAxisProp.getFormatNumber(0) : null); 
    s.append("\"" + ((formatNumber != null) ? formatNumber.getSample():"") +  "\",");
    s.append("\"" + ((formatNumber != null) ? (String.valueOf(formatNumber.getType().value())):"")  + "\",");
    GraphAxisProperties graphZAxisProp = graph.getAxis(GraphAxis.Z);
    s.append((graphZAxisProp.getLabel().isVisible()? "1":"0")   + ",");
    s.append(graphZAxisProp.getValues().getFrequency() + ",");
    s.append((graphZAxisProp.hasMinValue()? "1":"0") + ",");
    s.append(graphZAxisProp.getMinValueGraph() + ",");
    s.append((graphZAxisProp.hasMaxValue()? "1":"0") + ",");
    s.append(graphZAxisProp.getMaxValueGraph() + ",");
    s.append((graphZAxisProp.isLogarithmic()? "1":"0") + ",");
    s.append(graphZAxisProp.getValues().getOrientation().value() + ",");
    s.append((graphZAxisProp.getGrid().isVisible()? "1":"0")  + ",");;
    s.append(getColorString(graphZAxisProp.getGrid().getColor()) + ",");
    axis = null;
    try
    {
        axis = block.getAxis(GraphAxis.Z);
    }
    catch (Exception e)  { }
    formatNumber = (((axis != null) && (axis.getCount() > 0)) ? graphZAxisProp.getFormatNumber(0) : null);
    s.append("\"" + ((formatNumber != null) ? formatNumber.getSample():"") +  "\",");
    s.append("\"" + ((formatNumber != null) ? (String.valueOf(formatNumber.getType().value())):"")  + "\"");
    s.append(");");
    out.append(s.toString());
    printDecoration(out, (Decoration) graph.getData(), "chartData");
    printDecoration(out, (Decoration) graphXAxisProp.getValues(), "XAxisValues");
    printDecoration(out, (Decoration) graphYAxisProp.getValues(), "YAxisValues");
    printDecoration(out, (Decoration) graphZAxisProp.getValues(), "ZAxisValues");
    printDecoration(out, (Decoration) graph.getTitle(), "chartTitle");
    printDecoration(out, (Decoration) graph.getLegend(), "chartLegend");
    printDecoration(out, (Decoration) graphXAxisProp.getLabel(), "XAxisLabel");
    printDecoration(out, (Decoration) graphYAxisProp.getLabel(), "YAxisLabel");
    printDecoration(out, (Decoration) graphZAxisProp.getLabel(), "ZAxisLabel");
    printAttributes(out, ((Decoration) graph).getAttributes(), "varGraph", true);
}
void printDecoration(StringBuffer out, Decoration deco, String varName)
throws IOException
{
    out.append( varName + " = new Object();");
    printFont(out, deco.getFont(), varName);
    printAlign(out, deco.getAlignment(), varName);
    printAttributes(out, deco.getAttributes(), varName, false);
}
%>
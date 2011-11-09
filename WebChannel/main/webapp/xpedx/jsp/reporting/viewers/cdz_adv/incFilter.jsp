<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try {
    _logger.info("-->incFilter.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
    String filtBid       = requestWrapper.getQueryParameter("filtBid", false);
    String bid           = requestWrapper.getQueryParameter("sBid", false);
    String viewer        = requestWrapper.getQueryParameter("viewer", false, "false");
String isBlock       = requestWrapper.getQueryParameter("isBlock", false, "false");
    String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
    int nReportIndex = Integer.parseInt(iReport);
    DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
    locale = ViewerTools.convStrToLocale(requestWrapper.getUserLocale());
String strDocID = doc.getProperties().getProperty(PropertiesType.DOCUMENT_ID);
    boolean bEditQueryFilter = objUserSettings.getUserDocRight("EDIT_QUERY", strDocID).equals("full");
    boolean bViewer = viewer.equals("true"); 
    boolean isFilterBy = (filtBid != null);
boolean bIsBlock = isBlock.equals("true"); 
    if (bViewer && !bIsBlock) { 
    printReportFilter(out, _logger, doc, nReportIndex, bid);
    } else if (bViewer && bIsBlock) { 
    printBlockFilter(out, _logger, doc, nReportIndex, bid);
    } else if (isFilterBy) { 
    printFilterBy(out, _logger, doc, nReportIndex, bid, filtBid);
    } else { 
    printDocument(out, _logger, doc, nReportIndex, bEditQueryFilter);
    }
out.println("var incFilterOK = true;");
_logger.info("<--incFilter.jsp");
}
catch(ServerException e)
{
out.println("var incFilterOK = false;");
out.println("var bIncServerException = true;");
}
catch(Exception e)
{
out.println("var incFilterOK = false;");
objUtils.incErrorMsg(e, out);
}
%>
<%!
Locale locale = null;
void printReportFilter(JspWriter out, DHTMLLogger _logger, DocumentInstance doc, int nReportIndex, String selBid)
throws IOException
{
    _logger.info("-->printReportFilter");
    _logger.info("selBid:" + selBid);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, selBid);
    ReportContainer report = cellInfo.m_report;
    if ((report instanceof Filterable) && ((Filterable) report).hasFilter()) {
        String reportName = "report" + nReportIndex;
        out.println(reportName + " = newTreeWidgetElem( _reportIconID, \"" + ViewerTools.escapeQuotes(report.getName())  + "\", " + ViewerTools.escapeQuotes(report.getID()) + ",null,null,null,null,'treeFilter','treeFilterSelected')");
        printFilter(out, report, reportName, true, nReportIndex, _logger);
out.println("currFil= " + reportName + ".sub[0].obj");
    }
    _logger.info("<--printReportFilter");
}
void printBlockFilter(JspWriter out, DHTMLLogger _logger, DocumentInstance doc, int nReportIndex, String selBid)
throws IOException
{
    _logger.info("-->printBlockFilter");
    _logger.info("selBid:" + selBid);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, selBid);
ReportElement re = cellInfo.getReportElementByID(selBid);
    if ((re instanceof Filterable) && ((Filterable) re).hasFilter()) {
    FilterContainer filters = ((Filterable) re).getFilter();
    String blockName = "blockFilter";
    ReportFilterMaker filter = new ReportFilterMaker( blockName, filters, nReportIndex);
        _logger.info("filter generated : " + filter.toString());
    out.println(filter.toString());
out.println("currFil= " + blockName + "0");
    }
    _logger.info("<--printBlockFilter");
}
void printFilterBy(JspWriter out, DHTMLLogger _logger, DocumentInstance doc, int nReportIndex, String selBid, String filtBid)
throws IOException
{
    _logger.info("-->printFilterBy");
    _logger.info("selBid:" + selBid);
    _logger.info("filtBid:" + filtBid);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, selBid);
    ReportElement reFilter = cellInfo.getReportElementByID(filtBid);
    _logger.info("reFilter:" + reFilter);
    ReportExpression expr = cellInfo.m_expr;
    _logger.info("expr:" + expr);
    FilterCondition filter = cellInfo.getFilter(reFilter, expr, false);
    _logger.info("Filter:" + filter);
    ReportFilterMaker mkFilt = new ReportFilterMaker(filter, expr);
    _logger.info("mkFilt:" + mkFilt);
    out.println(mkFilt.toString());
    _logger.info("<--printFilterBy");
}
boolean printErrorMsg(JspWriter out, String scriptFilterName, String DPName, String errorMsg)
throws IOException, java.text.ParseException
{
out.println(scriptFilterName + "0 = newBOFilterNode(true);\n");
out.println("currFil = newBOFilter(_filterAdvanced, null);\n");
out.println("currFil.advancedName='" + DPName + ": '+" +  errorMsg + ";\n");
out.println(scriptFilterName + "0.add(currFil);\n");
return true;
}
void printDocument(JspWriter out, DHTMLLogger _logger, DocumentInstance doc, int nReportIndex, boolean bEditQueryFilter)
throws IOException, java.text.ParseException
{
    _logger.info("-->printDocument");
    Properties props = doc.getProperties();
    String docName = props.getProperty("name");
    out.println("filtersTreeRoot = newTreeWidgetElem( _documentIconID, \"" + ViewerTools.escapeQuotes(docName) + "\", 0,null,null,null,null,'treeFilter','treeFilterSelected')"); 
    out.println("filtersTree.add(filtersTreeRoot)");
    out.println("filtersTreeRoot.expanded=" + true + ";");
    if (bEditQueryFilter) {
        DataProviders dataProviders = doc.getDataProviders();
        int count = dataProviders.getCount();
String filterLabel = "QF"; 
        for (int i = 0; i < count; i++) {
        DataProvider dp = dataProviders.getItem(i);
        String DPName = dp.getName();
        String queryWidgName = "query" + i;
        String scriptFilterName = "flt" + i;
        boolean displayQueryFilter = false ;
        _logger.info("Universe:" + DPName + " try to load Query Filter");
        try {        
if (!dp.hasCombinedQueries())
{
ConditionContainer conditions = dp.getQuery().getCondition();
if (conditions != null) {
QueryFilterMaker filt = new QueryFilterMaker( scriptFilterName, conditions, false); 
out.println(filt.toString());
displayQueryFilter = true;
}
            }
            else
            {
displayQueryFilter=printErrorMsg(out, scriptFilterName, DPName, "s_combinedQueryMsg");
            }
}
catch(REException e)
{
        displayQueryFilter=printErrorMsg(out, scriptFilterName, DPName, "s_noUniverseErrorMsg");
} finally {
if (displayQueryFilter) {
String dpWidgName = "dp" + i;
String strKeydateMsg = "";
String keydateValue = DataSourceTools.getKeydateValue(dp, locale);
        if (keydateValue != null) {
        strKeydateMsg = "+\" (\"+_keydateUsedLabel+\" \"+";
        if (keydateValue.equals(Keydate.LAST_AVAILABLE)) {
        strKeydateMsg += "_lastAvailableLabel+\")\"";
        }
        else {
        strKeydateMsg += "formatSerializedDate(\""+keydateValue+"\")+\")\"";
        }
        }
out.println(dpWidgName + " = newTreeWidgetElem(_queyIconID, \"" + DPName + "\"" + strKeydateMsg + ", \"" + dpWidgName + "\");");
out.println("filtersTreeRoot.add(" + dpWidgName + ");");
out.println(dpWidgName + ".expanded=true;");
out.println("_filterID=\"" + filterLabel + i + "\""); 
                out.println(queryWidgName + " = newTreeWidgetHTMLElem( _filterIconID, \"" + DPName + "\", " + scriptFilterName + "0, _filterID," + scriptFilterName + "0.getInlineFilter())");
                out.println(dpWidgName + ".add(" + queryWidgName + ")");
            }
}
        }
    }
    ReportElementContainer reportStructure = doc.getStructure();
Reports objReports = doc.getReports();
Report objReport = null;    
    if (reportStructure!=null){
        boolean expand = true;
        for (int i = 0; i < reportStructure.getChildCount(); i++) {
            if (i == nReportIndex) { 
                expand = true;
            } else {
                expand = false;
            }
            objReport = objReports.getItem(i);
            String pageMode = objReport.getPaginationMode().toString();
            boolean bIsPageModeReport = (pageMode.equals("Page") || pageMode.equals("QuickDisplay") );
            printReport(out,(ReportContainer) reportStructure.getChildAt(i), i, expand, _logger, bIsPageModeReport);
        }
    }
    _logger.info("<--printDocument");
}
void printReport(JspWriter out, ReportContainer report, int reportIndex, boolean expand, DHTMLLogger _logger, boolean bIsPageMode)
throws IOException
{
    if (report == null) return;
_logger.info("-->printReport");
    String reportName = "report" + reportIndex;
    out.println(reportName + " = newTreeWidgetElem( _reportIconID, \"" + ViewerTools.escapeQuotes(report.getName())  + "\", " + ViewerTools.escapeQuotes(report.getID()) + ",null,null,null,null,'treeFilter','treeFilterSelected')");
    out.println("filtersTreeRoot.add(" + reportName + ")");
out.println(reportName+".reportIndex="+reportIndex+";");
    if ((report instanceof Filterable) && ((Filterable) report).hasFilter()) {
    printFilter(out, report , reportName,  expand, reportIndex, _logger);
    }
if (bIsPageMode) {
        printHeaderFooterBody(out, report, "header", 0, reportName, expand, reportIndex, _logger, bIsPageMode);
}
    printHeaderFooterBody(out, report, "body", 0, reportName, expand, reportIndex, _logger, bIsPageMode);
if (bIsPageMode) {
        printHeaderFooterBody(out, report, "footer", 0, reportName, expand, reportIndex, _logger, bIsPageMode);
}
_logger.info("<--printReport");
}
void printHeaderFooterBody(JspWriter out, ReportContainer report, String className, int varIndex, String parentVarName, boolean expand, int nReportIndex, DHTMLLogger _logger, boolean bIsPageMode)
throws IOException
{
    String iconID = "";
    String partName = "";
    String varName = "el";
ReportElement re = null;
    if (className.equals("header")) {
    re = report.getPageHeader();
    if (re == null) return;
        iconID = "_headerIconID";
        partName = "_pageHeader";
        varName += re.getID();
    } else if (className.equals("body")) {
    re = report.getReportBody();
        iconID = "_bodyIconID";
        partName = "_pageBody";
        varName += re.getID();
    } else if (className.equals("footer")) {
    re = report.getPageFooter();
    if (re == null) return;
        iconID = "_footerIconID";
        partName = "_pageFooter";
        varName += re.getID();
    }
     _logger.info("-->printHeaderFooterBody: " + re + ", cn=" + className + ", parentVarName=" + parentVarName );
    int childVarIndex = varIndex;
if (bIsPageMode) {
    String widget = "newTreeWidgetElem( " + iconID + " , p." + partName + ", " + ViewerTools.escapeQuotes(re.getID()) + ",null,null,null,null,'treeFilter','treeFilterSelected')";
    out.println( varName + " = " + widget);
    out.println( parentVarName + ".add(" + varName + ")");
    out.println( parentVarName + ".expanded=" + expand);
    childVarIndex++;
    } else {
    varName = parentVarName;
    }
    printReportElement(out, re, className, childVarIndex, varName, expand, nReportIndex, _logger, bIsPageMode);
    _logger.info("<--printHeaderFooterBody");
}
void printReportElement(JspWriter out, ReportElement re, String className, int varIndex, String parentVarName, boolean expand, int nReportIndex, DHTMLLogger _logger, boolean bIsPageMode)
throws IOException
{
    if (re == null) return;
_logger.info("-->printReportElement.getID(): " + re.getID() + ", cn=" + className + ", parentVarName=" + parentVarName );
    String varName = parentVarName;
    int childVarIndex = varIndex + 1;
    ReportElement recChildRE = null;
    String recChildClassName = null;
if (bIsPageMode || !className.equals("body")) {
    parentVarName = "el" +  re.getID();
    }
    int count = re.getChildCount();
    for (int i = 0; i < count; i++)
    {
        ReportElement childRE = (ReportElement) re.getChildAt(i);
        String childClassName = CurrentCellInfo.getREClassName(childRE);
        if (childClassName.equals("cell") || childClassName.equals("reportCell")) {
           printCell(out, (Cell) childRE, parentVarName, _logger);
        }
        if (childClassName.equals("block")){
            String blockName = printReportBlock(out, childRE, childClassName, varIndex, parentVarName, expand);
            if ((childRE instanceof Filterable) && ((Filterable) childRE).hasFilter()) {
               printFilter(out, childRE , blockName,  expand, nReportIndex, _logger);
            }
        }
    }
    for (int i = 0; i < count; i++)
    {
        ReportElement childRE = (ReportElement) re.getChildAt(i);
        String childClassName = CurrentCellInfo.getREClassName(childRE);
        if (childClassName.equals("section")) {
            String sectionName = printSection(out, childRE, childClassName, varIndex, parentVarName, expand);
            if ((childRE instanceof Filterable) && ((Filterable) childRE).hasFilter()) {
               printFilter(out, childRE , sectionName,  expand, nReportIndex, _logger);
            }
            printReportElement(out, childRE, childClassName, childVarIndex, sectionName, expand, nReportIndex, _logger, bIsPageMode);
        }
    }
_logger.info("<--printReportElement");
}
void printCell(JspWriter out, Cell cell, String parentVarName, DHTMLLogger _logger)
throws IOException
{
    _logger.info("-->printCell: " + cell);
    String cellName = "";
boolean isSectCell = false;
    if (cell instanceof FreeCell)
    {
        cellName = "s_cell + \" " + ViewerTools.escapeQuotes(((FreeCell) cell).getValue()) + "\"";
        _logger.info("-->FreeCell: " + cellName);
    } else if (cell instanceof ReportCell) {
    isSectCell = ((ReportCell) cell).isSection();
        cellName = isSectCell ? "s_sectioncell":"s_cell";
        ReportExpression re = ((ReportCell) cell).getNestedExpr();
        if (re == null) {
        re = ((ReportCell) cell).getExpr();
        }
        String name = (re != null) ? re.getName() : "";
        _logger.info("-->ReportCell: " + name);
        cellName += " + \" " + ViewerTools.escapeQuotes(name) + "\"";
    }
    if (!cellName.equals("")) {
        String id = ((ReportElement) cell).getID();
        String varName = "el" + id;
        String widget = "newTreeWidgetElem( _cellIconID, " + cellName + ", " + id + ",null,null,null,null,'treeFilter','treeFilterSelected')";
        out.println( varName + " = " + widget);
        if (isSectCell) out.println( varName + ".isSectCell = true");
        out.println( parentVarName + ".add(" + varName + ")");
    }
    _logger.info("<--printCell");
}
void printFilter(JspWriter out, ReportElement re, String parentVarName, boolean expand, int nReportIndex, DHTMLLogger _logger)
throws IOException
{
_logger.info("-->printFilter");
    FilterContainer filters = ((Filterable) re).getFilter();
_logger.info("FilterContainer : " + filters);
    String scriptFilterName = parentVarName + "Flt";
    ReportFilterMaker filt = new ReportFilterMaker( scriptFilterName, filters, nReportIndex);
    out.println(filt.toString());
_logger.info("filter generated : " + filt.toString());
out.println("_filterID=\""+ re.getID() + "\"");
    String widget = "newTreeWidgetHTMLElem( _filterIconID, \"" + re.getID() + "\", " + scriptFilterName + "0, _filterID, " + scriptFilterName + "0.getInlineFilter(),null,null,'treeFilter','treeFilterSelected')";
    out.println( parentVarName + ".add(" +  widget + ")");
    out.println( parentVarName + ".expanded=" + expand);
    out.println( parentVarName + ".reportIndex=" + nReportIndex);
_logger.info("<--printFilter");
}
String  printReportBlock(JspWriter out, ReportElement re, String className, int varIndex, String parentVarName, boolean expand)
throws IOException
{
    String varName = "el" + re.getID();
    int blockType  = CurrentCellInfo.getBlockType(((ReportBlock) re).getRepresentation());
    String widget = "newTreeWidgetElem( " + blockType + ", " +
                                    "getBlockSubType(" + blockType + ") + \": " +
                                    ViewerTools.escapeQuotes(((ReportBlock) re).getName()) + "\"," +
                                    ViewerTools.escapeQuotes(re.getID()) + ",null,null,null,null,'treeFilter','treeFilterSelected')";
    out.println( varName + " = " + widget);
    out.println( parentVarName + ".add(" + varName + ")");
    out.println( parentVarName + ".expanded=" + expand);
    return varName;
}
String printSection(JspWriter out, ReportElement re, String className, int varIndex, String parentVarName, boolean expand)
throws IOException
{
    String varName = "el" + re.getID();
    String sectionName = "";
    BlockAxis axis = ((SectionContainer) re).getAxis();
    if ((axis != null) && (axis.getCount() > 0)) {
    ReportExpression exp = axis.getExpr(0);
    if (exp != null) {
    sectionName = exp.getName();
    }
}
    String widget = "newTreeWidgetElem( _sectionIconID, s_section + \" " + ViewerTools.escapeQuotes(sectionName) + "\", " + ViewerTools.escapeQuotes(re.getID()) + ",null,null,null,null,'treeFilter','treeFilterSelected')";
    out.println( varName + " = " + widget);
    out.println( parentVarName + ".add(" + varName + ")");
    out.println( parentVarName + ".expanded=" + expand);
    return varName;
}
%>

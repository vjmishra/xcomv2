<%@ page import="com.businessobjects.rebean.wi.*,com.businessobjects.adv_ivcdzview.*,java.io.IOException" %>
<%@ include file="incWomFunctions.jsp" %>
<%! 
void printDocument(StringBuffer out, DocumentInstance doc, int nReportIndex)
throws IOException, java.text.ParseException
{
DHTMLLogger _logger = Utils.getLogger("com.businessobjects.dhtml.wom");
_logger.info("-->wom.jsp");
out.append("function printWOM(p){");
        out.append("doc=new Object;");
        out.append("doc.children=new Array;");
        out.append("doc.currentReport=" + nReportIndex + ";");
printCustomOrders(out, doc, _logger);
printLinks(out, doc, _logger);
        ReportElementContainer reportStructure = doc.getStructure();
        Reports reports = doc.getReports();
        if ( (reportStructure != null) && (reports != null))
        {
printReportAlerters(out, (ReportStructure) reportStructure, _logger);   
        int count = reportStructure.getChildCount();
            for (int i = 0; i < count; i++) {
            if (i == nReportIndex) {
                printReport(out, (ReportContainer) reportStructure.getChildAt(i), reports.getItem(i), _logger);
                } else {
                    printOtherReport(out, (ReportContainer) reportStructure.getChildAt(i), reports.getItem(i), _logger);
                }
            }
        }
        out.append("\n p.doc=doc;}\n");
        _logger.info("WOM is LOADED.");
        _logger.info("<--wom.jsp");
}
void printCustomOrders(StringBuffer out, DocumentInstance doc, DHTMLLogger _logger)
throws IOException, java.text.ParseException
{
_logger.info("printCustomOrders");
    ReportDictionary rd = doc.getDictionary();
    StringBuffer customSorts = new StringBuffer("doc._CSArr = new Array();");
    int count = rd.getChildCount();
    for (int i = 0; i < count; i++) {
    ReportExpression re = rd.getChildAt(i);
    _logger.info("ReportExpression re :" + re);
SortInfo sortInfo = null;
if (re instanceof DPExpression) {
sortInfo = ((DPExpression) re).getSortInfo();
} else if (re instanceof VariableExpression) {
sortInfo = ((VariableExpression) re).getSortInfo();
} else if (re instanceof Link) {
sortInfo = ((Link) re).getSortInfo();
}
if (sortInfo != null) {
String id = re.getID();
if (sortInfo.isCustomSortDefined()) {
customSorts.append("doc._CSArr['" + id + "'] = true;");
_logger.info("_CSArr['" + id + "'] = true\n");
}
}
  }
  out.append(customSorts + "\n");
}
void printLinks(StringBuffer out, DocumentInstance doc, DHTMLLogger _logger)
throws IOException, java.text.ParseException
{
_logger.info("printLinks");
    ReportDictionary rd = doc.getDictionary();
    StringBuffer linksArr = new StringBuffer("doc._links = new Array(");
SynchroManager syncManager = rd.getSynchroManager();
Link link = null;
int expCount;
boolean isFirst=true;
ReportExpression linkRe;
int linkCount = syncManager.getLinkCount();
for (int i=0; i < linkCount; i++) 
{
  link = syncManager.getLink(i);
_logger.info("printLinks : link("+ i +")="+ link );
expCount = link.getExpressionCount();
for (int j=0; j < expCount; j++)
{
linkRe = link.getExpression(j);
_logger.info("printLinks : linkRe("+ j +")="+ linkRe );
if (linkRe instanceof DPExpression)
{
if(!isFirst)
linksArr.append(",");
linksArr.append("'"+ViewerTools.escapeQuotes(linkRe.getID())+"'");
isFirst = false;
}
}
}
linksArr.append(");");
  out.append( linksArr );
}
void printOtherReport(StringBuffer out, ReportContainer rc, Report rp, DHTMLLogger _logger)
throws IOException
{
        if ((rc != null) && (rp != null))
        {
            String varName = "report";
            printElementInfo(out, varName, varName, rc.getID(), "doc");
        }
  }
void printReport(StringBuffer out, ReportContainer rc, Report rp, DHTMLLogger _logger)
throws IOException
{
        if ((rc != null) && (rp != null))
        {
            String varName = "report";
            printElementInfo(out, varName, varName, rc.getID(), "doc");
    if ((rc instanceof Filterable) && ((Filterable) rc).hasFilter()) {
    out.append(varName + ".hf=true;") ;
    }
            StringBuffer s = new StringBuffer("fReport(" + varName + ",");
            PageHeaderFooter header = rc.getPageHeader();
            PageHeaderFooter footer = rc.getPageFooter();
        s.append( "\"" + ViewerTools.escapeQuotes(rc.getName()) + "\",");
            if (header != null) {
               s.append(true + ",");
               s.append(((header.getUnit().value() == UnitType._INCH )? "1" : "0") + ","); 
               s.append(header.getHeight() + ",");
               s.append(getColorString(header.getAttributes().getBackground()) + ",");
            } else {
               s.append(false + ",");
               s.append("0,");
               s.append("0,");
               s.append("cl(-1,-1,-1),");
            }
            if (footer != null) {
               s.append(true + ",");
               s.append(((footer.getUnit().value() == UnitType._INCH )? "1" : "0") + ","); 
               s.append(footer.getHeight() + ",");
               s.append(getColorString(footer.getAttributes().getBackground()) + ",");
            } else {
               s.append(false + ",");
               s.append("0,");
               s.append("0,");
               s.append("cl(-1,-1,-1),");
            }
            Attributes atts = rc.getReportBody().getAttributes();
            s.append(getColorString(atts.getBackground()) + ",");
            String skin = (atts.getSkin() != null) ? atts.getSkin().getName() :"";
            s.append("\"" + skin + "\",");
            String imgURL = (atts.getBackgroundImageURL() != null)? atts.getBackgroundImageURL() : "";
            s.append("\"" + imgURL + "\",");
            s.append(atts.getBackgroundImageDisplayMode().value() + ",");
            Alignment align = rc.getReportBody().getBackgroundAlignment();
            s.append(align.getHorizontal().value() + ",");
            s.append(align.getVertical().value() + ",");
            s.append(getColorString(rc.getHyperlinkColor()) + ",");
            s.append(getColorString(rc.getVisitedHyperlinkColor()) + ",");
PageInfo pInfo=rc.getPageInfo();
            s.append(pInfo.getPaperSize().value() + ",");
            s.append(pInfo.getOrientation().value() + ",");
            s.append(((pInfo.getUnit().value() == UnitType._INCH )? "1" : "0") + ",");
            s.append(pInfo.getPaperSizeHeight() + ",");
            s.append(pInfo.getPaperSizeWidth() + ",");
            Margins margins = pInfo.getMargins();
            s.append(((margins.getUnit().value() == UnitType._INCH )? "1" : "0") + ","); 
            s.append(margins.getTop() + ",");
            s.append(margins.getLeft() + ",");
            s.append(margins.getBottom() + ",");
            s.append(margins.getRight() + ",");
            s.append("\"" + rp.getPaginationMode().toString() + "\"" + ",");
s.append("\"" + rp.getReportMode().toString() + "\"" + ",");
s.append("\"" + "NaN" + "\"" + ",");
s.append(rp.getPageNavigation().getCurrent() + ",");
s.append("false" + ",");
String embimg = (atts.getBackgroundImageResource() != null) ? "true" : "false";
s.append( embimg );
if ( rc.getPageHeader() != null )
{
s.append(",");
Attributes attshd = header.getAttributes();
String hdskin = (attshd.getSkin() != null) ? attshd.getSkin().getName() :"";
s.append("\"" + hdskin + "\",");
String hdimgURL = (attshd.getBackgroundImageURL() != null)? attshd.getBackgroundImageURL() : "";
s.append("\"" + hdimgURL + "\",");
String hdembimg = (attshd.getBackgroundImageResource() != null)? "true" : "false";
s.append( hdembimg + ",");
s.append(attshd.getBackgroundImageDisplayMode().value() + ",");
Alignment hdalign = header.getBackgroundAlignment();
s.append(hdalign.getHorizontal().value() + ",");
s.append(hdalign.getVertical().value() );
}
if ( rc.getPageFooter() != null )
{
s.append(",");
Attributes attsfr = footer.getAttributes();
String hdskin = (attsfr.getSkin() != null) ? attsfr.getSkin().getName() :"";
s.append("\"" + hdskin + "\",");
String hdimgURL = (attsfr.getBackgroundImageURL() != null)? attsfr.getBackgroundImageURL() : "";
s.append("\"" + hdimgURL + "\",");
String ftembimg =(attsfr.getBackgroundImageResource() != null)? "true" : "false";
s.append( ftembimg + ",");
s.append(attsfr.getBackgroundImageDisplayMode().value() + ",");
Alignment hdalign = footer.getBackgroundAlignment();
s.append(hdalign.getHorizontal().value() + ",");
s.append(hdalign.getVertical().value() );
}
s.append( ");");
s.append(varName +".report.vertRecords="+pInfo.getVerticalRecords() + ";");
s.append(varName +".report.horiRecords="+pInfo.getHorizontalRecords() + ";");
            out.append(s);
            printReportElement(out, header, "pageHeader", 0, "report", _logger);
            printReportElement(out, rc.getReportBody(), "body", 0, "report", _logger);
            printReportElement(out, footer, "pageFooter", 0, "report", _logger);
        }
}
void printReportElement(StringBuffer out, ReportElement re, String className, int varIndex, String parentVarName, DHTMLLogger _logger)
throws IOException
{
        if (re != null)
        {
            String varName= "r" + varIndex;
            printElementInfo(out, varName, className,re.getID(), parentVarName);
    if ((re instanceof Filterable) && ((Filterable) re).hasFilter()) {
    if (className.equals("block")) {
    out.append(varName + ".hf=" + getBlockFilterExpressions(re, _logger));
    } else {
    out.append(varName + ".hf=true;") ; 
    }    
    }
            if (className.equals("reportCell")) {
                printReportCell(out, (ReportCell) re, varName, _logger);                
            }
            if (className.equals("cell")) {
                printCell(out, (Cell) re, varName, _logger);                
            }
            if (re instanceof Attachable) {
                  printAttached(out, (Attachable) re, varName, _logger);
            }
if ( (className.equals("block")) || (className.equals("reportCell")) || (className.equals("cell")) )
printLayerLevel(out, className, re, varName, _logger);
            int childVarIndex = varIndex+1;
            int count = re.getChildCount();
            for (int i = 0; i < count; i++)
            {
                ReportElement childRE= (ReportElement) re.getChildAt(i);
                String childClassName = CurrentCellInfo.getREClassName(childRE);
                printReportElement(out, childRE, childClassName, childVarIndex, varName, _logger);
                if (childClassName.equals("block")) {
                    printReportBlock(out, (ReportBlock) childRE, childVarIndex, _logger);
                }
                if (childClassName.equals("section")) {
                    printSection(out, (SectionContainer) childRE, childVarIndex, _logger);
                }
            }
        }
        }
void printSection(StringBuffer out, SectionContainer section, int varIndex, DHTMLLogger _logger)
throws IOException
{
            String varName   = "r" + varIndex;
            Attributes atts = section.getAttributes();
            out.append( varName + ".bgColor=" +  getColorString(atts.getBackground()) + ";");
            BlockAxis axis = section.getAxis();
            printAxis(out, varName, axis, 0, false, _logger);
}
void printReportBlock(StringBuffer out, ReportBlock block, int varIndex, DHTMLLogger _logger)
throws IOException
{
            String         varName   = "r" + varIndex;
            Representation repr      = block.getRepresentation();
            BlockType      blockType = repr.getType();
            int nDimDetails = countBlockVars(block, false);
            int nMeasures   = countBlockVars(block, true);
            StringBuffer s = new StringBuffer("block(" + varName + ",");
            s.append( "\"" + ViewerTools.escapeQuotes(block.getName()) + "\",");
            s.append(CurrentCellInfo.getBlockType(repr) + ",");
            s.append(nDimDetails + ",");
            s.append(nMeasures + ",");
s.append((block.getDuplicateRowAggregation()? "1" : "0") + ",");
s.append((block.startOnNewPage()? "1" : "0") + ",");
s.append((block.repeatOnEveryPage()? "1" : "0") + ",");
s.append((block.avoidPageBreak()? "1" : "0") + ",");
s.append(((((Position) block).getUnit().value() == UnitType._INCH )? "1" : "0") + ","); 
s.append(((Position) block).getX() + ",");
s.append(((Position) block).getY() + ",");
s.append((block.isShowWhenEmpty()? "1" : "0") + ");");
            out.append(s);
            printAllBlockAxis(out, varName, block, blockType instanceof GraphType, _logger);
            if (blockType instanceof TableType) {
                if ((blockType == TableType.VTABLE) || (blockType == TableType.HTABLE)) {
                    printSimpleTable(out, block, (SimpleTable) block.getRepresentation(), varIndex + 1, varName, _logger);
                }
                if (blockType == TableType.FORM) {
                    printForm(out, block, (Form) block.getRepresentation(), varIndex + 1, varName, _logger);
                }
                if (blockType == TableType.XTABLE) {
                   printCrossTable(out, block, (CrossTable) block.getRepresentation(), varIndex + 1, varName, _logger);
                }
                if (repr instanceof TableFormBase)
{
Decoration deco = ((TableFormBase)repr).getBodyTableDecoration();
printBorders(out, deco.getAttributes().getBorder(), varName);
}
            } else if (blockType instanceof GraphType) {
            printAttributes(out, ((Decoration) block.getRepresentation()).getAttributes(), varName, true);
                printGraph(out, block, (Graph) block.getRepresentation(), varName);
            }
}
void printGraph(StringBuffer out, ReportBlock block, Graph graph, String varName)
    throws IOException
    {
        StringBuffer s = new StringBuffer("graph(" + varName + ",");
        s.append(((graph.getUnit().value()== UnitType._INCH )? "1" : "0") + ","); 
        s.append( graph.getWidth() + ",");
        s.append( graph.getHeight() + ");");
        out.append(s);
    }
void printAllBlockAxis(StringBuffer out, String varName, ReportBlock block, boolean printVarNames, DHTMLLogger _logger)
throws IOException
{
            printBlockAxis(out, varName, block, TableAxis.CONTENT, 0, printVarNames, _logger);
            printBlockAxis(out, varName, block, TableAxis.HORIZONTAL, 1, printVarNames, _logger);
            printBlockAxis(out, varName, block, TableAxis.VERTICAL, 2, printVarNames, _logger);
            printBlockAxis(out, varName, block, GraphAxis.X, 3, printVarNames, _logger);
            printBlockAxis(out, varName, block, GraphAxis.Y, 4, printVarNames, _logger);
            printBlockAxis(out, varName, block, GraphAxis.Z, 5, printVarNames, _logger);
}
void printBlockAxis(StringBuffer out, String varName, ReportBlock block, AxisType axisType, int intAxisType, boolean printVarNames, DHTMLLogger _logger)
throws IOException
{
            BlockAxis axis = null;
            try
            {
                axis = block.getAxis(axisType);
            }
            catch (Exception e)
            {
            }
            printAxis(out, varName, axis, intAxisType, printVarNames, _logger);
}
void printAxis(StringBuffer out, String varName, BlockAxis axis, int intAxisType, boolean printVarNames, DHTMLLogger _logger)
throws IOException
{
            if (axis != null)
            {
                StringBuffer s = new StringBuffer();
                StringBuffer crdr = new StringBuffer();
                int count = axis.getCount();
                String id = null;
                ReportExpression re;
                StringBuffer sNames = new StringBuffer();
                if (printVarNames)
                {
                sNames.append("axn(" + varName + "," + intAxisType + ",new Array(");
                }
crdr.append("crdr(" + varName + "," + intAxisType + ",new Array(");
                int k = 0;
                for (int i = 0; i < count; i++)
                {
                    s.append(",");
                    re = axis.getExpr(i);
boolean canCustomSort = true;
                    if (re instanceof FormulaExpression) {
                      id = "\""+ ViewerTools.escapeQuotes(re.getName()) +"\"";
                      canCustomSort = false;
                    } else {
                      id = "\""+ ViewerTools.escapeQuotes(re.getID()) +"\"";
                    }
                    s.append(id);
                    if (printVarNames)
                    {
                    if (i > 0) {
sNames.append(",");
}
sNames.append("\""+ ViewerTools.escapeQuotes(re.getName())+"\"");
                    }
                    if (re.getQualification() == ObjectQualification.MEASURE) {
                    canCustomSort = false;
                    }
                    if (canCustomSort) {
                    if (k++ > 0) {
crdr.append(",");
}
crdr.append(id);                    
                    } 
                }
                out.append("ax(" + varName + "," + intAxisType + s.toString() + ");");
                crdr.append("));");
out.append(crdr);
                BlockSort sort = axis.getBlockSort();
                if (sort!=null)
                {
                    s = new StringBuffer();
                    int sCount = sort.getCount();
                    for (int j = 0; j < sCount; j++)
                    {
                        SortElement sortE = sort.getSortElement(j);
                        SortType sType = sortE.getType();
                        _logger.info(sortE.getExpression().getID() + "," + sType);
                        if (j > 0) {
                            s.append(",");
                        }
                        if (sType != SortType.NONE) 
                        {
ReportExpression sExpr = sortE.getExpression();
                            s.append(((sExpr instanceof FormulaExpression) ? ((FormulaExpression)sExpr).getValue() : sExpr.getID()) + "," + (sType == SortType.ASCENDING ? 1 : 2));
                        }
                    }
                    out.append("srt(" + varName + "," + intAxisType + ",\"" + ViewerTools.escapeQuotes( s.toString()) + "\");");                    
                }
                if (printVarNames)
                {                
                    sNames.append("));");
out.append(sNames);
                }
                BlockBreak brk = axis.getBlockBreak();
                if (brk!=null)
                {
                    int sCount = brk.getCount();
                    s = new StringBuffer();
                    for (int j = 0; j < sCount; j++)
                    {
                        BreakElement brkE = brk.getBreakElement(j);
                        if (j > 0) {
                            s.append(",");
                        }
                        ReportExpression brkExpr = brkE.getExpression();
                        s.append((brkExpr instanceof FormulaExpression) ? ((FormulaExpression)brkExpr).getValue() : brkExpr.getID());
                    }
                    out.append("brk(" + varName + "," + intAxisType + ",\"" + ViewerTools.escapeQuotes(s.toString()) + "\");");
                }
             }
}
void printCrossTable(StringBuffer out, ReportBlock block, CrossTable xtable, int varIndex, String parentVarName, DHTMLLogger _logger)
throws IOException
{    
        String varName  = "r" + varIndex;
        BlockAxis hAxis = block.getAxis(TableAxis.HORIZONTAL);
        BlockAxis vAxis = block.getAxis(TableAxis.VERTICAL);
        printElementInfo(out, varName, "crossTable", "", parentVarName);
        BlockBreak hBlockBreak = hAxis.getBlockBreak();
        BlockBreak vBlockBreak = vAxis.getBlockBreak();
        int hBreakCount = hBlockBreak.getCount();
        int vBreakCount = vBlockBreak.getCount();
        int childVarIndex = varIndex + 1;
        for (int h = -1; h < hBreakCount; h++)
        {
            for (int v = -1; v < vBreakCount; v++)
            {
                BreakElement hbreakElem = (h == -1 ? null : hBlockBreak.getBreakElement(h));
                BreakElement vbreakElem = (v == -1 ? null : vBlockBreak.getBreakElement(v));
                printCellMatrix(out, xtable.getCellMatrix(VZoneType.TOP,    HZoneType.LEFT,  vbreakElem, hbreakElem), childVarIndex, varName, "h" + v + ",h" + h, -1, _logger);
                printCellMatrix(out, xtable.getCellMatrix(VZoneType.TOP,    HZoneType.RIGHT, vbreakElem, hbreakElem), childVarIndex, varName, "h" + v + ",f" + h, -1, _logger);
                if (h == -1)
                {
                    printCellMatrix(out, xtable.getCellMatrix(VZoneType.TOP,    HZoneType.BODY, vbreakElem, hbreakElem), childVarIndex, varName,  "h" + v + ",b", 1, _logger);
                    printCellMatrix(out, xtable.getCellMatrix(VZoneType.BOTTOM, HZoneType.BODY,  vbreakElem, hbreakElem), childVarIndex, varName, "f" + v + ",b",-1, _logger);
                }
                if (v == -1)
                {
                    printCellMatrix(out, xtable.getCellMatrix(VZoneType.BODY,   HZoneType.RIGHT, vbreakElem, hbreakElem), childVarIndex, varName, "b" + ",f" + h, -1, _logger);
                    printCellMatrix(out, xtable.getCellMatrix(VZoneType.BODY,   HZoneType.LEFT,  vbreakElem, hbreakElem), childVarIndex, varName, "b" + ",h" + h, 2, _logger);
                }
                printCellMatrix(out, xtable.getCellMatrix(VZoneType.BOTTOM, HZoneType.LEFT,  vbreakElem, hbreakElem), childVarIndex, varName, "f" + v + ",h" + h, -1, _logger);
                printCellMatrix(out, xtable.getCellMatrix(VZoneType.BOTTOM, HZoneType.RIGHT, vbreakElem, hbreakElem), childVarIndex, varName, "f" + v + ",f" + h, -1, _logger);
            }
        }
printCellMatrix(out, xtable.getCellMatrix(VZoneType.BODY, HZoneType.BODY), childVarIndex, varName, "b", 0, _logger);
}
void printForm(StringBuffer out, ReportBlock block, Form form, int varIndex, String parentVarName, DHTMLLogger _logger)
throws IOException
{
          String varName  = "r" + varIndex;
          printElementInfo(out, varName, "form", "", parentVarName);
          printCellMatrix(out, form.getCells(), varIndex + 1, varName, "b", 2, _logger);
}
void printSimpleTable(StringBuffer out, ReportBlock block, SimpleTable table, int varIndex, String parentVarName, DHTMLLogger _logger)
throws IOException
{
          String varName  = "r" + varIndex;
          boolean bVTable = (table.getType() == TableType.VTABLE);
          BlockAxis axis = block.getAxis(bVTable ? TableAxis.HORIZONTAL : TableAxis.VERTICAL);
          printElementInfo(out, varName, (bVTable ? "vTable" : "hTable"), "", parentVarName);
          BlockBreak blockBreak = axis.getBlockBreak();
          int breakCount = blockBreak.getCount();
          int childVarIndex = varIndex + 1;
          for (int i = -1; i < breakCount; i++)
          {
              BreakElement breakElem = (i == -1 ? null : blockBreak.getBreakElement(i));
              printCellMatrix(out, table.getHeader(breakElem), childVarIndex, varName, "h" + i, bVTable?1:2, _logger);
              printCellMatrix(out, table.getFooter(breakElem), childVarIndex, varName, "f" + i, -1, _logger);
          }
          printCellMatrix(out, table.getBody(), childVarIndex, varName, "b", bVTable?1:2, _logger);
}
void printCellMatrix(StringBuffer out, CellMatrix matrix, int varIndex, String parentVarName, String className, int nRelatedAxis, DHTMLLogger _logger)
throws IOException
{
            if (matrix!=null)
            {
                String varName = "r" + varIndex;
                String childVarName = "r" + (varIndex + 1);
                printElementInfo(out, varName, className, "", parentVarName);
                out.append(varName + ".axis=" + nRelatedAxis + ";");
                int rowCount = matrix.getRowCount();
                int colCount = matrix.getColumnCount();
                out.append(varName + ".rowCount=" + rowCount + ";");
                out.append(varName + ".colCount=" + colCount + ";");
                for (int row = 0; row < rowCount; row++)
                {
                    for (int col = 0; col < colCount; col++)
                    {
                    TableCell cell = matrix.getCell(row, col);
if (( cell.getRow() == row ) && ( cell.getColumn() == col )) {
printTableCell(out, cell, childVarName, varName, row, col, _logger);
}
                    }
                }
            }
}
void printFormula(StringBuffer out, ReportExpression expr, String varName, ReportExpression nestedExpr, DHTMLLogger _logger)
throws IOException
{
        int nbParam=9;
        String[] params = new String[nbParam];
        for (int i = 0; i < nbParam; i++)
            params[i] = null;
        int objFormula       = 0;
        int objQualification = 1;
        int objDType         = 2;
        int objType          = 3;
        int objDefinition    = 4;
        int objAggregation   = 5;
        int objNestedDType   = 6;
        int objLinkedDim = 7;
        int objAggregateFct = 8;
        if (expr != null)
        {
            if (expr instanceof FormulaExpression)
            {
                FormulaExpression fexpr = (FormulaExpression)expr;
                params[objFormula] = "\"" +  ViewerTools.escapeQuotes(fexpr.getValue()) + "\"";
                if (fexpr.isAggregation())
                   params[objAggregation] = "1";
            }            
            params[objQualification] = "\"" +  ViewerTools.getWomType(expr.getQualification()) + "\"";
            int v = expr.getType().value();
            int dataType = 0;
            switch(v)
            {
                case(ObjectType._DATE):
                    dataType=0;
                break;
                case(ObjectType._NUMERIC):
                    dataType=1;
                break;
                case(ObjectType._TEXT):
                    dataType=2;
                break;
            }
            params[objDType] = "\"" + dataType + "\"";
params[objAggregateFct] =  "\"\""; 
            int oType = 2;
            int nestedDataType = 0;
            if (nestedExpr != null)
            {               
                if (nestedExpr instanceof DPExpression) 
                {                
                    oType = 0;
                    params[objAggregateFct] =  "\""+ ((DPExpression)nestedExpr).getAggregationFunction().toString()+"\"";
                } 
                else if (nestedExpr instanceof VariableExpression) 
                {
                    oType = 1;
                    FormulaExpression nestedFExpr = ((VariableExpression) nestedExpr).getFormula();
                    params[objDefinition] =  "\"" +  ViewerTools.escapeQuotes(nestedFExpr.getValue()) + "\"";
                    if( nestedExpr.getQualification().value() == ObjectQualification._DETAIL)
{
ReportExpression dpExpr = ((VariableExpression)nestedExpr).getAssociatedDimension();
if(dpExpr!=null)
params[objLinkedDim] =  "\"" +  dpExpr.getID() + "\"";
else
params[objLinkedDim] =  "\"\"";
}
                }
ObjectType lObjType =  nestedExpr.getType();
if (lObjType != null) {
                int nv = lObjType.value();
                switch(nv)
                {
case(ObjectType._DATE):
nestedDataType=0;
break;
                    case(ObjectType._NUMERIC):
nestedDataType=1;
break;
                    case(ObjectType._TEXT):
nestedDataType=2;
break;
                }
            } else {
            nestedDataType=2;
            }
            }
            params[objType] = "" + oType;
            params[objNestedDType] = "" + nestedDataType;
        }
        else
            params[objFormula] = "\"\"";
        int lastParam=0;
        for (int i=(nbParam-1); i>=0;i--)
        {
            if (params[i] != null)
            {
                lastParam=i;
                break;
            }
        }
        StringBuffer s = new StringBuffer();
        s.append("fm("+varName);
        for (int i=0; i <= lastParam; i++)
        {
        s.append(","+params[i]);
        }
        s.append(");");
        out.append(s);
    }
void printTableCell(StringBuffer out, TableCell cell, String varName, String parentVarName, int row, int col, DHTMLLogger _logger)
throws IOException
{
            printElementInfo(out, varName, "tableCell", cell.getID(), parentVarName);
            printDecoration(out, cell, varName, _logger);
            printPadding(out, cell, varName, _logger);
            printTableSizeCell(out, cell, varName, _logger);
printCellAlerters(out, cell, varName, _logger);
            StringBuffer sb = new StringBuffer(varName + ".contType=" + cell.getContentType().value() + ";");
            sb.append(varName + ".row=" + row + ";");
            sb.append(varName + ".col=" + col + ";");
boolean isAggregate = false;
ReportExpression plainExpr = cell.getExpr();
if ((plainExpr != null) && (plainExpr  instanceof FormulaExpression))
{
FormulaExpression formulaExpr = (FormulaExpression) plainExpr;
if (formulaExpr.isAggregation())
isAggregate = true;
}
            ReportExpression expr = isAggregate ? null : cell.getNestedExpr();
            if (expr != null) {
               sb.append(varName + ".id=\"" + expr.getID() + "\";");
               sb.append(varName + ".name=\"" +  ViewerTools.escapeQuotes(expr.getName()) + "\";");
            }
            int rowSpan = cell.getRowSpan();
            int colSpan = cell.getColSpan();
            sb.append(varName + ".rowSpan=" + rowSpan + ";");
            sb.append(varName + ".colSpan=" + colSpan + ";");
            FormatNumber formatNumber = cell.getFormatNumber();
            if (formatNumber != null)
            {
                sb.append(varName + ".formatNb=\"" + formatNumber.getSample() +  "\";");
                sb.append(varName + ".nbType=\"" + formatNumber.getType().value() + "\";");
            }
            out.append(sb);
            printFormula(out, cell.getExpr(), varName, expr , _logger);
            Representation repr = cell.getCellMatrix().getRepresentation();
            if (repr instanceof Table)
            {
Table table = (Table) repr;
for (int k = 0; k < 2; k++)
{
BlockCalculation calc   = (k == 0) ? table.getHBlockCalculation(cell) : table.getVBlockCalculation(cell);
String           prefix = (k == 0) ? "h" : "v";
if (calc != null)
{
CalculationElement[] calcs = calc.findCalculations(null);
int                  len   = calcs.length;
if (len > 0)
{
StringBuffer s = new StringBuffer();
s.append(varName + "." + prefix + "calc=\"");
for (int j=0; j < len; j++)
{
if (j > 0)
s.append(",");
int v=0;
switch(calcs[j].getType().value())
{
case Calculation._SUM     : v = 0; break;
case Calculation._COUNT   : v = 1; break;
case Calculation._AVERAGE : v = 2; break;
case Calculation._MIN     : v = 3; break;
case Calculation._MAX     : v = 4; break;
case Calculation._PERCENT : v = 5; break;
case Calculation._TOTAL  : v = 6; break;
}
s.append(v);
}
_logger.info( "<" +cell.getExpr() + ">\tNb Calc="+ len +"\t s="+s);
s.append("\";");
out.append(s);
}
}
}
            }
        }
void printReportCell(StringBuffer out, ReportCell cell, String varName, DHTMLLogger _logger)
throws IOException
{
            printCell(out, cell, varName, _logger);
            ReportExpression expr = cell.getNestedExpr();
            if (expr != null){
                out.append(varName + ".id=\"" + expr.getID() + "\";");
            }
            FormatNumber formatNumber = cell.getFormatNumber();
            if (formatNumber != null)
            {
                out.append(varName + ".formatNb=\"" + formatNumber.getSample() +  "\";");
                out.append(varName + ".nbType=\"" + formatNumber.getType().value() + "\";");
            }
            printFormula(out, cell.getExpr(), varName, cell.getNestedExpr(), _logger);
            if (cell.isSection())
               out.append(varName + ".isSect=true;");
        }
void printCell(StringBuffer out, Cell cell, String varName, DHTMLLogger _logger)
throws IOException
{
StringBuffer s = new StringBuffer();
            s.append(varName + ".contType=" + cell.getContentType().value() + ";");
            s.append(varName + ".showWhenEmpty=" + (cell.isShowWhenEmpty()? "1" : "0") + ";");
            if (cell instanceof FreeCell)
            {
                s.append("fcell(" + varName + ",");
                s.append( "\"" + ViewerTools.escapeQuotes(((FreeCell) cell).getValue())  + "\");");                                
            }
            s.append("cell(" + varName + ",");
            s.append((cell.repeatOnEveryPage()? "1" : "0") + ",");
            s.append(((((Position) cell).getUnit().value() == UnitType._INCH )? "1" : "0")+ ",");
            s.append(((Position) cell).getX() + ",");
            s.append(((Position) cell).getY() + ",");
            s.append((cell.avoidPageBreak()? "1" : "0") + ");");
            out.append(s);
            printDecoration(out, cell, varName, _logger);
            printSizeCell(out, cell, varName, _logger);
}
void printSizeCell(StringBuffer out, Cell cell, String varName, DHTMLLogger _logger)
throws IOException
{
            StringBuffer s = new StringBuffer("cs(" + varName + ",");
            s.append(((cell.getUnit().value()== UnitType._INCH )? "1" : "0") + ","); 
            s.append((cell.isAutoFitWidth() ? "1" : "0") + ",");
            s.append((cell.isAutoFitHeight() ? "1" : "0") + ",");
            s.append(cell.getWidth() + ",");
            s.append(cell.getHeight() + ");");
            s.append(varName+".cellsize.estimateH="+cell.estimateMinimalHeight() + ";");
            out.append(s);
}
void printTableSizeCell(StringBuffer out, TableCell cell, String varName, DHTMLLogger _logger)
throws IOException
{
            StringBuffer s = new StringBuffer("cs(" + varName + ",");
            s.append(((cell.getUnit().value()== UnitType._INCH )? "1" : "0") + ","); 
            s.append((cell.isAutoFitWidth() ? "1" : "0") + ",");
            s.append((cell.isAutoFitHeight() ? "1" : "0") + ",");
            s.append(cell.getWidth() + ",");
            s.append(cell.getHeight() + ");");
            s.append(varName+".cellsize.estimateH="+cell.estimateMinimalHeight() + ";");
            out.append(s);
}
void printDecoration(StringBuffer out, Decoration deco, String varName, DHTMLLogger _logger)
throws IOException
{
        printFont(out, deco.getFont(), varName);
        printAlign(out, deco.getAlignment(), varName);
        printAllAttributes(out, deco.getAttributes(), varName, false, deco.getBackgroundAlignment());        
}
void printPadding(StringBuffer out, TableCell cell, String varName, DHTMLLogger _logger)
throws IOException
{
        StringBuffer s = new StringBuffer("pad(" + varName + ",");
        s.append( cell.getHorizontalPadding() + ",");
        s.append( cell.getVerticalPadding() + ");");
        out.append(s.toString());
}
void printAttached(StringBuffer out, Attachable re, String varName, DHTMLLogger _logger)
throws IOException
{
        ReportElement attachedH = re.getHAttachTo();
        ReportElement attachedV = re.getVAttachTo();
        out.append("attach(" + varName + "," + ((attachedH != null) ? attachedH.getID() : "\"\"") + "," + re.getHorizontalAnchor().value()+ "," + ((attachedV != null) ? attachedV.getID() : "\"\"") + "," + re.getVerticalAnchor().value() + ");");
}
    void printAllPaperSize(DocumentInstance doc, DHTMLLogger _logger)
    throws IOException
    {
        ReportContainer report = (ReportContainer) doc.getStructure().getChildAt(0);
        ((Unit) report.getPageInfo()).setUnit(UnitType.MILLIMETER);
        _logger.info(report.getPageInfo().getOrientation() + " " + ((Unit) report.getPageInfo()).getUnit());
    if (report != null) {
           for (int i=1; i < 25; i++) {
              report.getPageInfo().setPaperSize(PaperSize.fromInt(i));
              _logger.info( "wh(" + (report.getPageInfo().getPaperSizeWidth()/10.0) + ",");
              _logger.info( (report.getPageInfo().getPaperSizeHeight()/10.0) + "),");
        }
}
    }
String getBlockFilterExpressions(ReportElement re, DHTMLLogger _logger) {
FilterContainer filters = ((Filterable) re).getFilter();
_logger.info("filters:" + filters);
StringBuffer sb = new StringBuffer("\"");
if (filters.getOperator() == LogicalOperator.AND)
{
    int count = filters.getChildCount();
    for (int i = 0; i < count; i++)
    {
        FilterConditionNode node = filters.getFilterConditionNode(i);
        if (node instanceof FilterObject)
        {
            FilterObject filter = (FilterObject) node;
            sb.append(filter.getExpr().getID());
            if (i< (count-1)) {
            sb.append(",");
            }
        }
    }
}
sb.append("\";");
return sb.toString();
}
void printLayerLevel(StringBuffer out, String className, ReportElement re, String varName, DHTMLLogger _logger)
    throws IOException
    {
int nbChild = re.getFather().getReportElementCount();
int childLevel = re.getFather().getIndex(re);
String layering=null;
if ( nbChild > 1 )
{
if ( childLevel == 0 )
layering="back-most";
else if ( childLevel == (nbChild-1) )
layering="front-most";
else
layering="middle";
out.append(varName + ".layering='" + layering + "';");
}
    }
void printReportAlerters(StringBuffer out, ReportStructure reportStructure, DHTMLLogger _logger)
    throws IOException
    {
AlertersDictionary docAlerters = reportStructure.getAlerters();
int nbDocAlerter = docAlerters.getCount();
_logger.info("nbDocAlerter = " + nbDocAlerter );
if (nbDocAlerter > 0) {
out.append("doc.arrAlerter = [");
for (int i = 0; i < nbDocAlerter; i++)
{
Alerter docAlerter = docAlerters.getAlerter(i);
out.append("new AlerterElement(");
out.append(docAlerter.getID());
out.append(", '" + ViewerTools.escapeQuotes(docAlerter.getName()) + "')");
if (i < (nbDocAlerter-1)) {
out.append(",");
}
_logger.info("Alerter id <"+ docAlerter.getID() +"> - name <"+ docAlerter.getName() +">");
}
out.append("];");
}
}
void printCellAlerters(StringBuffer out, TableCell cell, String varName, DHTMLLogger _logger)
    throws IOException
    {
Alerters cellAlerters = cell.getAlerters();
int nbCellAlerter = cellAlerters.getCount();
if (nbCellAlerter > 0) {
out.append(varName + ".alerts= [");
for (int i = 0; i < nbCellAlerter; i++)
{
out.append(cellAlerters.getAlerter(i).getID());
if (i < (nbCellAlerter-1)) {
out.append(",");
}
}
out.append("];");
}
}    
%>
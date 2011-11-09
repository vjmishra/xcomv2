<%@ include file="wistartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
try
{
    String strEntry    = requestWrapper.getQueryParameter("sEntry", true);
    String iReport     = requestWrapper.getQueryParameter("iReport", false, "0");
    int              nReportIndex = Integer.parseInt(iReport);
    ReportEngine objReportEngine  = reportEngines.getServiceFromStorageToken(strEntry);
    DocumentInstance doc          = objReportEngine.getDocumentFromStorageToken(strEntry);
%>
<html>
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script language="javascript" src="scripts/ReportWom.js"></script>
        <script language="javascript">
        <%        
            printDocument(out, doc, objReportEngine, nReportIndex);
        %>
        parent.doc=doc
        parent.womLoadCB()
        </script>
</head>
<body></body>
</html>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_DEFAULT", true, out, session);
}
%>
<%! 
void printDocument(JspWriter out, DocumentInstance doc, ReportEngine objReportEngine, int nReportIndex)
throws IOException, java.text.ParseException
{
            out.println("doc=new Object;");
            out.println("doc.children=new Array;");
            out.println("doc.currentReport=" + nReportIndex);
            StringBuffer skins = new StringBuffer("doc.reportSkins = new Array;");
            Skin[] reportSkins = objReportEngine.getBackgroundSkins(SkinReportElementType.REPORT) ;
            for (int i = 0; i < reportSkins.length; i++)
            {
                skins.append("doc.reportSkins[doc.reportSkins.length] = \"" + ViewerTools.escapeQuotes(((Skin) reportSkins[i]).getName()) + "\";");
            }
            Skin[] sectionSkins = objReportEngine.getBackgroundSkins(SkinReportElementType.SECTION) ;
            skins.append("doc.sectionSkins = new Array;");
            for (int i = 0; i < sectionSkins.length; i++)
            {
                skins.append("doc.sectionSkins[doc.sectionSkins.length] = \"" + ViewerTools.escapeQuotes(((Skin) sectionSkins[i]).getName()) + "\";");
            }
            Skin[] blockSkins = objReportEngine.getBackgroundSkins(SkinReportElementType.BLOCK) ;
            skins.append("doc.blockSkins = new Array;");
            for (int i = 0; i < blockSkins.length; i++)
            {
                skins.append("doc.blockSkins[doc.blockSkins.length] = \"" + ViewerTools.escapeQuotes(((Skin) blockSkins[i]).getName()) + "\";");
            }
            Skin[] cellSkins = objReportEngine.getBackgroundSkins(SkinReportElementType.CELL) ;
            skins.append("doc.cellSkins = new Array;");
            for (int i = 0; i < cellSkins.length; i++)
            {
                skins.append("doc.cellSkins[doc.cellSkins.length] = \"" + ViewerTools.escapeQuotes(((Skin) cellSkins[i]).getName()) + "\";");
            }
            out.println(skins);
            ReportElementContainer reportStructure = doc.getStructure();
            Reports reports = doc.getReports();
            if ( (reportStructure != null) && (reports != null))
            {
                int count = reportStructure.getChildCount();
                for (int i = 0; i < count; i++) {
                if (i == nReportIndex) {
printReport(out, (ReportContainer) reportStructure.getChildAt(i), reports.getItem(i));
                } else {
printOtherReport(out, (ReportContainer) reportStructure.getChildAt(i), reports.getItem(i));
                }
                }
            }
}
void printElementInfo(JspWriter out, String varName, String className, String bid, String parentVarName)
throws IOException
{
out.println();
out.println(varName+"=ac("+parentVarName+",\""+className+"\",\""+bid+"\")");
}
void printOtherReport(JspWriter out, ReportContainer rc, Report rp)
throws IOException
{
        if ((rc != null) && (rp != null))
        {
            String varName = "report";
            printElementInfo(out, varName, varName, rc.getID(), "doc");
        }
  }
void printReport(JspWriter out, ReportContainer rc, Report rp)
throws IOException
{
        if ((rc != null) && (rp != null))
        {
            String varName = "report";
            printElementInfo(out, varName, varName, rc.getID(), "doc");
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
            s.append(rc.getPageInfo().getPaperSize().value() + ",");
            s.append(rc.getPageInfo().getOrientation().value() + ",");
            s.append(((rc.getPageInfo().getUnit().value() == UnitType._INCH )? "1" : "0") + ",");
            s.append(rc.getPageInfo().getPaperSizeHeight() + ",");
            s.append(rc.getPageInfo().getPaperSizeWidth() + ",");
            Margins margins = rc.getPageInfo().getMargins();
            s.append(((margins.getUnit().value() == UnitType._INCH )? "1" : "0") + ","); 
            s.append(margins.getTop() + ",");
            s.append(margins.getLeft() + ",");
            s.append(margins.getBottom() + ",");
            s.append(margins.getRight() + ",");
            s.append("\"" + rp.getPaginationMode().toString() + "\"" + ",");
s.append("\"" + rp.getReportMode().toString() + "\"" + ",");
s.append("\"" + "NaN" + "\"" + ",");
s.append(rp.getPageNavigation().getCurrent() + ",");
s.append("false" + ")");
            out.println(s);
            printReportElement(out, header, "pageHeader", 0, "report");
            printReportElement(out, rc.getReportBody(), "body", 0, "report");
            printReportElement(out, footer, "pageFooter", 0, "report");
        }
}
void printReportElement(JspWriter out, ReportElement re, String className, int varIndex, String parentVarName)
throws IOException
{
        if (re!=null)
        {
            String varName= "r" + varIndex;
            printElementInfo(out,varName,className,re.getID(), parentVarName);
            if (className.equals("reportCell")) {
                printReportCell(out, (ReportCell) re, varName);
            }
            if (className.equals("cell")) {
                printCell(out, (Cell) re, varName);
            }
            if(re instanceof Attachable) {
                  printAttached(out, (Attachable) re, varName);
            }
            int childVarIndex = varIndex+1;
            int count = re.getChildCount();
            for (int i = 0; i < count; i++)
            {
                ReportElement childRE= (ReportElement) re.getChildAt(i);
                String childClassName = CurrentCellInfo.getREClassName(childRE);
                printReportElement(out, childRE, childClassName, childVarIndex, varName);
                if (childClassName.equals("block")) {
                    printReportBlock(out, (ReportBlock) childRE, childVarIndex);
                }
                if (childClassName.equals("section")) {
                    printSection(out, (SectionContainer) childRE, childVarIndex);
                }
            }
        }
        }
void printSection(JspWriter out, SectionContainer section, int varIndex)
throws IOException
{
            String varName   = "r" + varIndex;
            Attributes atts = section.getAttributes();
            out.println( varName + ".bgColor=" +  getColorString(atts.getBackground()));
            BlockAxis axis = section.getAxis();
            printAxis(out, varName, axis, 0, false);
}
void printAllBlockAxis(JspWriter out, String varName, ReportBlock block, boolean printVarNames)
throws IOException
{
            printBlockAxis(out, varName, block, TableAxis.CONTENT, 0, printVarNames);
            printBlockAxis(out, varName, block, TableAxis.HORIZONTAL, 1, printVarNames);
            printBlockAxis(out, varName, block, TableAxis.VERTICAL, 2, printVarNames);
            printBlockAxis(out, varName, block, GraphAxis.X, 3, printVarNames);
            printBlockAxis(out, varName, block, GraphAxis.Y, 4, printVarNames);
            printBlockAxis(out, varName, block, GraphAxis.Z, 5, printVarNames);
}
void printBlockAxis(JspWriter out, String varName, ReportBlock block, AxisType axisType, int womType, boolean printVarNames)
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
            printAxis(out, varName, axis, womType, printVarNames);
}
void printAxis(JspWriter out, String varName, BlockAxis axis, int womType, boolean printVarNames)
throws IOException
{
            if (axis != null)
            {
                StringBuffer s = new StringBuffer();
                int count = axis.getCount();
                String id = null;
                ReportExpression re;
                StringBuffer sNames= new StringBuffer();
                if (printVarNames)
                {
                sNames.append("axn(" + varName + "," + womType + ",new Array(");
                }
                for (int i = 0; i < count; i++)
                {
                    if (i > 0) {
                        s.append(",");
                    }
                    re = axis.getExpr(i);
                    if (re instanceof FormulaExpression) {
                      id = ViewerTools.escapeQuotes(re.getName());
                    } else {
                      id = ViewerTools.escapeQuotes(re.getID());
                    }
                    s.append(id);
                    if (printVarNames)
                    {
                    if (i > 0) {
    sNames.append(",");
}
sNames.append("\""+ ViewerTools.escapeQuotes(re.getName())+"\"");
                    }
                }
                out.println("ax(" + varName + "," + womType + ",\"" + s.toString() + "\")");
                BlockSort sort = axis.getBlockSort();
                if (sort!=null)
                {
                    s = new StringBuffer();
                    int sCount = sort.getCount();
                    for (int j = 0; j < sCount; j++)
                    {
                        SortElement sortE = sort.getSortElement(j);
                        SortType sType = sortE.getType();
                        if (j > 0) {
                            s.append(",");
                        }
                        if (sType != SortType.NONE) {
                            s.append(sortE.getExpression().getID() + "," + (sType == SortType.ASCENDING ? 1 : 2));
                        }
                    }
                    out.println("srt(" + varName + "," + womType + ",\"" + s.toString() + "\")");
                }
                if (printVarNames)
                {
sNames.append("))\n");
out.println(sNames);
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
                        s.append(brkE.getExpression().getID());
                    }
                    out.println("brk(" + varName + "," + womType + ",\"" + s.toString() + "\")");
                }
             }
}
void printReportBlock(JspWriter out, ReportBlock block, int varIndex)
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
            s.append((block.isShowWhenEmpty()? "1" : "0") + ")");
            out.println(s);
            printAllBlockAxis(out, varName, block, blockType instanceof GraphType);
            if (blockType instanceof TableType) {
                printTableForm(out, (TableFormBase) block.getRepresentation(), varName);
                if ((blockType == TableType.VTABLE) || (blockType == TableType.HTABLE)) {
                    printTable(out, (Table) block.getRepresentation(), varName);
                    printSimpleTable(out, block, (SimpleTable) block.getRepresentation(), varIndex + 1, varName);
                }
                if (blockType == TableType.FORM) {
                    printForm(out, block, (Form) block.getRepresentation(), varIndex + 1, varName);
                }
                if (blockType == TableType.XTABLE) {
                   printTable(out, (Table) block.getRepresentation(), varName);
                   printCrossTable(out, block, (CrossTable) block.getRepresentation(), varIndex + 1, varName);
                }
            } else if (blockType instanceof GraphType) {
                printGraph(out, block, (Graph) block.getRepresentation(), varName);
            }
}
    void printTableForm(JspWriter out, TableFormBase tableForm, String varName)
    throws IOException
    {
        StringBuffer s = new StringBuffer("tableForm(" + varName + ",");
        s.append(((tableForm.getUnit().value()== UnitType._INCH )? "1" : "0") + ","); 
        s.append(tableForm.getCellSpacing() + ",");
        s.append(tableForm.getCellSpacing() + ",");  
        s.append(tableForm.getCellPadding() + ",");
        s.append(tableForm.getAlternateColorFrequency() + ",");
        s.append(getColorString(tableForm.getAlternateColor()) + ")");
        out.println(s);
    }
    void printTable(JspWriter out, Table table, String varName)
    throws IOException
    {
        StringBuffer s = new StringBuffer("table(" + varName + ",");
        s.append((table.isHeaderVisible()? "1" : "0") + ",");
        s.append((table.isFooterVisible()? "1" : "0") + ",");
        Attributes atts = table.getBodyTableDecoration().getAttributes();
        s.append(getColorString(atts.getBackground()) + ",");
        String skin = (atts.getSkin() != null) ? atts.getSkin().getName() :"";
        s.append("\"" + skin + "\",");
        String imgURL = (atts.getBackgroundImageURL() != null)? atts.getBackgroundImageURL() : "";
        s.append("\"" + imgURL + "\",");
        s.append(((atts.getSkin() != null) ? atts.getSkin().getDisplayMode().value() : atts.getBackgroundImageDisplayMode().value()) + ",");
        Alignment align = table.getBodyTableDecoration().getBackgroundAlignment();
        s.append(align.getHorizontal().value() + ",");
        s.append(align.getVertical().value() + ",");
        s.append((table.isHeaderOnEveryPage()? "1" : "0") + ",");
        s.append((table.isFooterOnEveryPage()? "1" : "0") + ")");
        out.println(s);
      }
void printCrossTable(JspWriter out, ReportBlock block, CrossTable xtable, int varIndex, String parentVarName)
throws IOException
{
        out.println(parentVarName + ".table.showEmptyRows=" + (xtable.isShowEmptyRows()? "1" : "0"));
        out.println(parentVarName + ".table.showEmptyCols=" + (xtable.isShowEmptyColumns()? "1" : "0"));
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
                printCellMatrix(out, xtable.getCellMatrix(VZoneType.TOP,    HZoneType.LEFT,  vbreakElem, hbreakElem), childVarIndex, varName, "h" + v + ",h" + h, -1);
                printCellMatrix(out, xtable.getCellMatrix(VZoneType.TOP,    HZoneType.RIGHT, vbreakElem, hbreakElem), childVarIndex, varName, "h" + v + ",f" + h, -1);
                if (h == -1)
                {
                    printCellMatrix(out, xtable.getCellMatrix(VZoneType.TOP,    HZoneType.BODY, vbreakElem, hbreakElem), childVarIndex, varName,  "h" + v + ",b", 1);
                    printCellMatrix(out, xtable.getCellMatrix(VZoneType.BOTTOM, HZoneType.BODY,  vbreakElem, hbreakElem), childVarIndex, varName, "f" + v + ",b",-1);
                }
                if (v == -1)
                {
                    printCellMatrix(out, xtable.getCellMatrix(VZoneType.BODY,   HZoneType.RIGHT, vbreakElem, hbreakElem), childVarIndex, varName, "b" + ",f" + h, -1);
                    printCellMatrix(out, xtable.getCellMatrix(VZoneType.BODY,   HZoneType.LEFT,  vbreakElem, hbreakElem), childVarIndex, varName, "b" + ",h" + h, 2);
                }
                printCellMatrix(out, xtable.getCellMatrix(VZoneType.BOTTOM, HZoneType.LEFT,  vbreakElem, hbreakElem), childVarIndex, varName, "f" + v + ",h" + h, -1);
                printCellMatrix(out, xtable.getCellMatrix(VZoneType.BOTTOM, HZoneType.RIGHT, vbreakElem, hbreakElem), childVarIndex, varName, "f" + v + ",f" + h, -1);
            }
        }
printCellMatrix(out, xtable.getCellMatrix(VZoneType.BODY, HZoneType.BODY), childVarIndex, varName, "b", 0);
}
void printForm(JspWriter out, ReportBlock block, Form form, int varIndex, String parentVarName)
throws IOException
{
          out.println(parentVarName + ".tableForm.showEmptyFields=" + (form.isShowEmptyFields()? "1" : "0"));
          String varName  = "r" + varIndex;
          printElementInfo(out, varName, "form", "", parentVarName);
          printCellMatrix(out, form.getCells(), varIndex + 1, varName, "b", 2);
}
void printSimpleTable(JspWriter out, ReportBlock block, SimpleTable table, int varIndex, String parentVarName)
throws IOException
{
          String varName  = "r" + varIndex;
          boolean bVTable = (table.getType() == TableType.VTABLE);
          if (bVTable)
          { 
            out.println(parentVarName + ".table.showEmptyRows=" + (table.isShowEmptyRows()? "1" : "0"));
          } else {
            out.println(parentVarName + ".table.showEmptyCols=" + (table.isShowEmptyRows()? "1" : "0"));
          }
          BlockAxis axis = block.getAxis(bVTable ? TableAxis.HORIZONTAL : TableAxis.VERTICAL);
          printElementInfo(out, varName, (bVTable ? "vTable" : "hTable"), "", parentVarName);
          BlockBreak blockBreak = axis.getBlockBreak();
          int breakCount = blockBreak.getCount();
          int childVarIndex = varIndex + 1;
          for (int i = -1; i < breakCount; i++)
          {
              BreakElement breakElem = (i == -1 ? null : blockBreak.getBreakElement(i));
              printCellMatrix(out, table.getHeader(breakElem), childVarIndex, varName, "h" + i, bVTable?1:2);
              printCellMatrix(out, table.getFooter(breakElem), childVarIndex, varName, "f" + i, -1);
          }
          printCellMatrix(out, table.getBody(), childVarIndex, varName, "b", bVTable?1:2);
}
void printCellMatrix(JspWriter out, CellMatrix matrix, int varIndex, String parentVarName, String className, int nRelatedAxis)
throws IOException
{
            if (matrix!=null)
            {
                String varName = "r" + varIndex;
                String childVarName = "r" + (varIndex + 1);
                printElementInfo(out, varName, className, "", parentVarName);
                out.println(varName + ".axis=" + nRelatedAxis);
                int rowCount = matrix.getRowCount();
                int colCount = matrix.getColumnCount();
                out.println(varName + ".rowCount=" + rowCount);
                out.println(varName + ".colCount=" + colCount);
                for (int row = 0; row < rowCount; row++)
                {
                    for (int col = 0; col < colCount; col++)
                    {
TableCell cell = matrix.getCell(row, col);
if ( ( cell.getRow() == row ) && ( cell.getColumn() == col ) )
printTableCell(out, cell, childVarName, varName, row, col);
else
{
System.out.println("-----> Not Good row=" + row + "(cell row=" + cell.getRow() +")");
System.out.println("-----> Not Good col=" + col + "(cell col=" + cell.getColumn() +")\n\n");
}
                    }
                }
            }
}
void printFormula(JspWriter out, ReportExpression expr, String varName, ReportExpression nestedExpr)
throws IOException
{
int nbParam=7;
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
                int oType = 2;
                int nestedDataType = 0;
                if (nestedExpr != null)
                {
                    if (nestedExpr instanceof DPExpression)
                        oType = 0;
                    else if (nestedExpr instanceof VariableExpression)
                    {
                        oType = 1;
                        FormulaExpression nestedFExpr = ((VariableExpression) nestedExpr).getFormula();
                        params[objDefinition] =  "\"" +  ViewerTools.escapeQuotes(nestedFExpr.getValue()) + "\"";
                    }
                    int nv = nestedExpr.getType().value();
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
s.append(")");
out.println(s);
    }
void printTableCell(JspWriter out, TableCell cell, String varName, String parentVarName, int row, int col)
throws IOException
{
            printElementInfo(out, varName, "tableCell", cell.getID(), parentVarName);
            printDecoration(out, cell, varName);
            printPadding(out, cell, varName);
            printTableSizeCell(out, cell, varName);
            ReportExpression expr = cell.getNestedExpr();
            if (expr != null) {
               out.println(varName + ".id=\"" + expr.getID() + "\"");
            }
            out.println(varName + ".contType=" + cell.getContentType().value());
            out.println(varName + ".row=" + row);
            out.println(varName + ".col=" + col);
            int rowSpan = cell.getRowSpan();
int colSpan = cell.getColSpan();
out.println(varName + ".rowSpan=" + rowSpan);
            out.println(varName + ".colSpan=" + colSpan);
            FormatNumber formatNumber = cell.getFormatNumber();
            if (formatNumber != null)
            {
                out.println(varName + ".formatNb=\"" + formatNumber.getSample() +  "\",");
                out.println(varName + ".nbType=\"" + formatNumber.getType().value() + "\",");
            }
            printFormula(out, cell.getExpr(), varName, cell.getNestedExpr());
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
                            }
                            s.append(v);
                        }
                        s.append("\"");
                        out.println(s);
                    }
                }
            }
            }
        }
void printReportCell(JspWriter out, ReportCell cell, String varName)
throws IOException
{
            printCell(out, cell, varName);
            ReportExpression expr = cell.getNestedExpr();
            if (expr != null){
                out.println(varName + ".id=\"" + expr.getID() + "\"");
            }
            FormatNumber formatNumber = cell.getFormatNumber();
            if (formatNumber != null)
            {
                out.println(varName + ".formatNb=\"" + formatNumber.getSample() +  "\",");
                out.println(varName + ".nbType=\"" + formatNumber.getType().value() + "\",");
            }
            printFormula(out, cell.getExpr(), varName, cell.getNestedExpr());
            if (cell.isSection())
               out.println(varName + ".isSect=true");
        }
void printCell(JspWriter out, Cell cell, String varName)
throws IOException
{
            out.println(varName + ".contType=" + cell.getContentType().value());
            StringBuffer s;
            if (cell instanceof FreeCell)
            {
                s = new StringBuffer("fcell(" + varName + ",");
                s.append( "\"" + ViewerTools.escapeQuotes(((FreeCell) cell).getValue())  + "\",");
                s.append((((FreeCell) cell).isShowWhenEmpty() ? "1" : "0") + ")");
                out.println(s);
            }
            s = new StringBuffer("cell(" + varName + ",");
            s.append((cell.repeatOnEveryPage()? "1" : "0") + ",");
            s.append(((((Position) cell).getUnit().value() == UnitType._INCH )? "1" : "0")+ ",");
            s.append(((Position) cell).getX() + ",");
            s.append(((Position) cell).getY() + ",");
            s.append((cell.avoidPageBreak()? "1" : "0") + ");");
            out.println(s);
            printDecoration(out, cell, varName);
            printSizeCell(out, cell, varName);
}
void printSizeCell(JspWriter out, Cell cell, String varName)
throws IOException
{
            StringBuffer s = new StringBuffer("cs(" + varName + ",");
            s.append(((cell.getUnit().value()== UnitType._INCH )? "1" : "0") + ","); 
            s.append((cell.isAutoFitWidth() ? "1" : "0") + ",");
            s.append((cell.isAutoFitHeight() ? "1" : "0") + ",");
            s.append(cell.getWidth() + ",");
            s.append(cell.getHeight() + ")");
            out.println(s);
}
void printTableSizeCell(JspWriter out, TableCell cell, String varName)
throws IOException
{
            StringBuffer s = new StringBuffer("cs(" + varName + ",");
            s.append(((cell.getUnit().value()== UnitType._INCH )? "1" : "0") + ","); 
            s.append((cell.isAutoFitWidth() ? "1" : "0") + ",");
            s.append((cell.isAutoFitHeight() ? "1" : "0") + ",");
            s.append(cell.getWidth() + ",");
            s.append(cell.getHeight() + ")");
            out.println(s);
}
void printDecoration(JspWriter out, Decoration deco, String varName)
throws IOException
{
            printFont(out, deco.getFont(), varName);
            printAlign(out, deco.getAlignment(), varName);
            printAttributes(out, deco.getAttributes(), varName, false);
}
void printDecoration(JspWriter out, Decoration deco, String parentVarName, String varName)
throws IOException
{
            out.print(parentVarName + "." + varName + " = new Object();");
            printFont(out, deco.getFont(), parentVarName + "." + varName);
            printAlign(out, deco.getAlignment(), parentVarName + "." + varName);
            printAttributes(out, deco.getAttributes(), parentVarName + "." + varName, false);
}
void printAttributes(JspWriter out, Attributes att, String varName, boolean bDoNoPrintFGColor)
throws IOException
{
            out.print(varName + ".bgColor=");
            printColor(out, att.getBackground());
            out.println();
            String imgURL = (att.getBackgroundImageURL() == null)? "" : att.getBackgroundImageURL();
            out.println(varName + ".imgURL=\"" + imgURL + "\"");
            if (!bDoNoPrintFGColor)
            {
                out.print(varName + ".fgColor=");
                printColor(out, att.getForeground());
                out.println();
            }
            printBorders(out, att.getBorder(), varName);
}
void printColor(JspWriter out, java.awt.Color color)
throws IOException
{
            if (color != null) {
                out.print( "cl(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")");
            } else {
                out.print( "cl(-1,-1,-1)");
            }
}
void printAlign(JspWriter out, Alignment align, String varName)
throws IOException
        {
            StringBuffer s = new StringBuffer("align(" + varName + ",");
            HAlignmentType hAlign = align.getHorizontal();
            VAlignmentType vAlign = align.getVertical();
            if (hAlign==HAlignmentType.LEFT) {
                s.append("0");
            } else if (hAlign==HAlignmentType.CENTER) {
                s.append("1");
            } else {
                s.append("2");
            }
            s.append(",");
            if (vAlign==VAlignmentType.TOP) {
                s.append("0");
            } else if (vAlign==VAlignmentType.CENTER) {
                s.append("1");
            }else {
                s.append("2");
            }
            s.append(",");
            s.append(align.getWrapText() ? "1" : "0");
            s.append(")");
            out.println(s.toString());
}
void printPadding(JspWriter out, TableCell cell, String varName)
throws IOException
{
            StringBuffer s = new StringBuffer("padding(" + varName + ",");
            s.append( cell.getPadding() + ",");
            s.append( cell.getPadding() + ",");
            s.append( cell.getPadding() + ",");
            s.append( cell.getPadding());
            s.append(")");
            out.println(s.toString());
}
void printBorders(JspWriter out, Border border, String varName)
throws IOException
{
            if (border instanceof SimpleBorder) {
               out.print("bd(" + varName + ",");
               SimpleBorder sb = (SimpleBorder) border;
               out.print( sb.getSize().value() + ",");
               printColor(out, sb.getColor());
            } else if (border instanceof ComplexBorder) {
               out.print("bs(" + varName + ",");
               ComplexBorder cb = (ComplexBorder) border;
               SimpleBorder sbTop = cb.getTop();
               out.print( sbTop.getSize().value() + ",");
               printColor(out, sbTop.getColor());
               SimpleBorder sbBottom = cb.getBottom();
               out.print( "," + sbBottom.getSize().value() + ",");
               printColor(out, sbBottom.getColor());
               SimpleBorder sbLeft = cb.getLeft();
               out.print( "," + sbLeft.getSize().value() + ",");
               printColor(out, sbLeft.getColor());
               SimpleBorder sbRight = cb.getRight();
               out.print( "," + sbRight.getSize().value() + ",");
               printColor(out, sbRight.getColor());
            }
            out.println(");");
}
void printFont(JspWriter out, Font font, String varName)
throws IOException
{
            StringBuffer s = new StringBuffer((((font.getStyle() & StyleType.BOLD)==StyleType.BOLD)?"1":"0"));
            s.append("," + (((font.getStyle() & StyleType.ITALIC)==StyleType.ITALIC)?"1":"0"));
            s.append("," + (((font.getStyle() & StyleType.UNDERLINE)==StyleType.UNDERLINE)?"1":"0"));
            s.append("," + (((font.getStyle() & StyleType.STRIKETHROUGH)==StyleType.STRIKETHROUGH)?"1":"0"));
            out.println("fnt(" + varName + ",\"" +  ViewerTools.escapeQuotes(font.getName()) + "\"," + font.getSize() + "," + s.toString() +");");
}
void printAttached(JspWriter out, Attachable re, String varName)
throws IOException
{
            ReportElement attached = re.getAttachTo();
            if (attached != null && !((re.getHorizontalAnchor() == HAnchorType.NONE) && (re.getVerticalAnchor() == VAnchorType.NONE))) {
               out.println("attach(" + varName + "," + attached.getID() + "," + re.getHorizontalAnchor().value() + "," + re.getVerticalAnchor().value() + ")");
            }
}
    void printGraph(JspWriter out, ReportBlock block, Graph graph, String varName)
    throws IOException
    {
        StringBuffer s = new StringBuffer("graph(" + varName + ",");
        s.append(((graph.getUnit().value()== UnitType._INCH )? "1" : "0") + ","); 
        s.append( graph.getWidth() + ",");
        s.append( graph.getHeight() + ",");
        s.append( (graph.getTitle().isVisible()?"1":"0")  + ",");
        s.append( "\"" +  ViewerTools.escapeQuotes(graph.getTitle().getTitle()) + "\",");
        s.append( (graph.getLegend().isVisible()?"1":"0")  + ",");
        s.append( graph.getLegend().getPosition().value() +  ",");
        s.append( (graph.getData().isVisible()?"1":"0") + ",");
        s.append( (graph.is3DLook()?"1":"0"));
        s.append(")");
        out.println(s.toString());
        s = new StringBuffer("graphApp(" + varName + ",");
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
        formatNumber =  (((axis != null) && (axis.getCount() > 0))  ? graphZAxisProp.getFormatNumber(0) : null); 
        s.append("\"" + ((formatNumber != null) ? formatNumber.getSample():"") +  "\",");
        s.append("\"" + ((formatNumber != null) ? (String.valueOf(formatNumber.getType().value())):"")  + "\"");
        s.append(")");
        out.println(s.toString());
        printDecoration(out, (Decoration) graph.getData(), varName, "chartData");
        printDecoration(out, (Decoration) graphXAxisProp.getValues(), varName, "XAxisValues");
        printDecoration(out, (Decoration) graphYAxisProp.getValues(), varName, "YAxisValues");
        printDecoration(out, (Decoration) graphZAxisProp.getValues(), varName, "ZAxisValues");
        printDecoration(out, (Decoration) graph.getTitle(), varName,  "chartTitle");
        printDecoration(out, (Decoration) graph.getLegend(), varName, "chartLegend");
printDecoration(out, (Decoration) graphXAxisProp.getLabel(), varName, "XAxisLabel");
printDecoration(out, (Decoration) graphYAxisProp.getLabel(), varName, "YAxisLabel");
printDecoration(out, (Decoration) graphZAxisProp.getLabel(), varName, "ZAxisLabel");
        printAttributes(out, ((Decoration) graph).getAttributes(), varName, true);
        }
void printDict(JspWriter out, DocumentInstance doc)
throws IOException
{
}
String getColorString(java.awt.Color color)
{
        if (color != null) {
            return "cl(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
        } else {
            return "cl(-1,-1,-1)";
        }
}
    int countAxisVars(ReportBlock block, AxisType axisType, boolean bMeasures)
    {
        int nVarCount = 0;
        BlockAxis axis = block.getAxis(axisType);
        if (axis != null)
        {
            int count = axis.getCount();
            for (int i = 0; i < count; i++)
            {
                ReportExpression expr = axis.getExpr(i);
                if (bMeasures)
                {
                    if (expr.getQualification() == ObjectQualification.MEASURE)
                        nVarCount++;
                }
                else
                {
                    if (expr.getQualification() != ObjectQualification.MEASURE)
                        nVarCount++;
                }
            }
        }
        return nVarCount;
    }
    int countBlockVars(ReportBlock block, boolean bMeasures)
    {
        int            nVarCount = 0;
        Representation repr      = block.getRepresentation();
        BlockType      bType     = repr.getType();
        if (repr instanceof GraphType)
        {
            nVarCount += countAxisVars(block, GraphAxis.X, bMeasures);
            nVarCount += countAxisVars(block, GraphAxis.Y, bMeasures);
            nVarCount += countAxisVars(block, GraphAxis.Z, bMeasures);
        }
        else
        {
            nVarCount += countAxisVars(block, TableAxis.CONTENT, bMeasures);
            nVarCount += countAxisVars(block, TableAxis.VERTICAL, bMeasures);
            nVarCount += countAxisVars(block, TableAxis.HORIZONTAL, bMeasures);
        }
        return nVarCount;
    }
    void printAllPaperSize(DocumentInstance doc)
    throws IOException
    {
        ReportContainer report = (ReportContainer) doc.getStructure().getChildAt(0);
        ((Unit) report.getPageInfo()).setUnit(UnitType.MILLIMETER);
        System.out.println(report.getPageInfo().getOrientation() + " " + ((Unit) report.getPageInfo()).getUnit());
        if (report != null) {
           for (int i=1; i < 25; i++) {
               report.getPageInfo().setPaperSize(PaperSize.fromInt(i));
               System.out.print( "wh(" + (report.getPageInfo().getPaperSizeWidth()/10.0) + ",");
               System.out.print( (report.getPageInfo().getPaperSizeHeight()/10.0) + "),");
            }
        }
    }
%>
<%@ include file="incStartpage.jsp" %>
<%@ page import="java.util.Set,java.util.TreeSet" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("--> incObjects.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
    String sBid = requestWrapper.getQueryParameter("sBid", false);
    String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
    String strForFilter = requestWrapper.getQueryParameter("forFilter", false, "false");
    DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
    int nReportIndex = Integer.parseInt(iReport);
    boolean isForFilter = Boolean.valueOf(strForFilter).booleanValue();
    _logger.info("isForFilter: " + isForFilter);
    printReportObjects(out, doc, sBid, nReportIndex, isForFilter, _logger);
Set fSet = new TreeSet();
    if (!isForFilter) {
       printFormulas(fSet, doc, _logger);
    }
    StringBuffer sOut = new StringBuffer(fSet.size()*20);    
Iterator it = fSet.iterator();
while (it.hasNext()) {
sOut.append((String) it.next());
}
out.println(sOut.toString());
out.println("var incObjectsOK = true;");
_logger.info("<-- incObjects.jsp");
}
catch(Exception e)
{
out.println("var incObjectsOK = false;");
objUtils.incErrorMsg(e, out);
}
%>
<%!
void printReportObjects(JspWriter out, DocumentInstance doc, String sBid, int nReportIndex, boolean isForFilter, DHTMLLogger _logger)
throws IOException, java.text.ParseException
{
    ReportDictionary rd = doc.getDictionary();
    boolean isMultiDP = ((DataProviders) doc.getDataProviders()).getCount() > 1;
    _logger.info("printReportObjects : sBid = "+ sBid );
    boolean allObjects = true;
    String blockDPName = "";
    if (sBid != null) {
    CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, sBid);
            DataProvider blockDP = getDPFromBlock(cellInfo.m_block);
            if (blockDP != null) {
            blockDPName = getDPFromBlock(cellInfo.m_block).getName();
              allObjects = false;
            _logger.info("printReportObjects : blockDP = "+ blockDP );
            }
    }
    DataProvider objDP;
    StringBuffer objects = new StringBuffer("_docObjects = new Array();");
    StringBuffer variables = new StringBuffer("_variableObjects = new Array();");
    int count = rd.getChildCount();
    for (int i = 0; i < count; i++) {
    ReportExpression re = rd.getChildAt(i);
      if (re instanceof DPExpression) {
        objDP = ((DPExpression) re).getDP();
        String desc = null;
        try {
        if (re.getDataSourceObject() != null) {
        desc = re.getDataSourceObject().getDescription();
        }
        } catch (REException ree) {
        _logger.info("Problem getting dataSource Object by accessing Universe.");
        } finally {
        if (desc == null) {
        desc = "";
    }
    }
    _logger.info("printReportObjects : DPExpression >> re.getName()="+ re.getName()  );
        if (isForFilter || allObjects || objDP.getName().equals(blockDPName)) { 
            _logger.info("printReportObjects : DPExpression >> re.getName()="+ re.getName() + ", objDP.getName()=" + objDP.getName());
objects.append("len = _docObjects.length;");
            objects.append("_docObjects[len] = " +
            "p.newBOObj('" + ViewerTools.escapeQuotes(re.getName()) + "','" +
                    re.getID() + "'," +
                               "p." + ViewerTools.getWomType(re.getQualification()) + "," +
                    "p." + ViewerTools.getWomDataType(re.getType()) + ",'" +
                                 ViewerTools.escapeQuotes(desc) + "');");
            objects.append("_docObjects[len].DPName='" +ViewerTools.escapeQuotes(objDP.getName())+ "';");
            objects.append("_docObjects[len].snapName='" +ViewerTools.escapeQuotes(re.getFormulaLanguageID())+ "';"); 
            objects.append("_docObjects[len].setFormula('=" + ViewerTools.escapeQuotes(re.getFormulaLanguageID())+ "');");
            if ((re instanceof DPExpression) && (re.getQualification() == ObjectQualification.MEASURE))
            objects.append("_docObjects[len].aggregateFct='" +((DPExpression)re).getAggregationFunction().toString()+ "';"); 
            }
      }
    if (re instanceof VariableExpression) {
        _logger.info("printReportObjects : VariableExpression >> re.getName()="+ re.getName());
    variables.append("len = _variableObjects.length;");
       variables.append("_variableObjects[len] = " +
                "p.newBOObj('" + ViewerTools.escapeQuotes(re.getName()) + "','" +
                        re.getID() + "'," +
                        "p." + ViewerTools.getWomType(re.getQualification()) + "," +
                        "p." + ViewerTools.getWomDataType(re.getType()) + ");");
        FormulaExpression nestedFExpr = ((VariableExpression) re).getFormula();
        variables.append("_variableObjects[len].setFormula('" + ViewerTools.escapeQuotes(nestedFExpr.getValue())+ "');");
        variables.append("_variableObjects[len].snapName='" +ViewerTools.escapeQuotes(nestedFExpr.getFormulaLanguageID())+ "';"); 
        String linkedDim="";
        if(re.getQualification().value() == ObjectQualification._DETAIL)
{
ReportExpression dpExpr = ((VariableExpression)re).getAssociatedDimension();
if(dpExpr!=null)
linkedDim = ViewerTools.escapeQuotes(dpExpr.getID());
else
linkedDim = "";
}
        variables.append("_variableObjects[len].setlinkedDim('" +linkedDim+ "');");
    }
  }
  SynchroManager syncManager = rd.getSynchroManager();
  Link link = null;
  int expCount;
  String dPNameLinkList;
  ReportExpression linkRe;
  int linkCount = syncManager.getLinkCount();
  String [] dPNames;
  String [] linkedIDs;
  boolean matchDPName;
  for (int i=0; i < linkCount; i++) {
  link = syncManager.getLink(i);
    _logger.info("printReportObjects : link("+ i +")="+ link );
    expCount = link.getExpressionCount();
    dPNames = new String[expCount];
    linkedIDs = new String[expCount];
    matchDPName = false;
for (int j=0; j < expCount; j++)
    {
linkRe = link.getExpression(j);
    _logger.info("printReportObjects : linkRe("+ j +")="+ linkRe );
if (linkRe instanceof DPExpression)
{
    dPNames[j] =  ViewerTools.escapeQuotes(((DPExpression) linkRe).getDP().getName());
    linkedIDs[j] =  ViewerTools.escapeQuotes(linkRe.getID());
        matchDPName = dPNames[j].equals(blockDPName);
        if (matchDPName) break;
}
    }
if (!allObjects && !matchDPName) { 
continue;
}
    String desc = null;
try {
    if (link.getDataSourceObject() != null) {
    desc = link.getDataSourceObject().getDescription();
    }
    } catch (REException ree) {
    _logger.info("Problem getting dataSource Object by accessing Universe.");
    } finally {
    if (desc == null) {
    desc = "";
}
}
  objects.append("len = _docObjects.length;");
objects.append("_docObjects[len] = " +
        "p.newBOObj('" +
                        ViewerTools.escapeQuotes(link.getName()) + "','" +
                        link.getID() + "'," +
                        "p." + ViewerTools.getWomType(link.getQualification()) + "," +
                        "p." + ViewerTools.getWomDataType(link.getType()) + ",'" +
                            ViewerTools.escapeQuotes(desc) + "');");
  objects.append("_docObjects[len].DPNames = new Array();");
  objects.append("_docObjects[len].linkedIDs = new Array();");
  for (int k=0; k < dPNames.length; k++) {
    objects.append("_docObjects[len].DPNames[" + k + "]='" + dPNames[k] + "';");
    objects.append("_docObjects[len].linkedIDs[" + k + "]='" + linkedIDs[k] + "';");        
  }
  objects.append("_docObjects[len].snapName='" +ViewerTools.escapeQuotes(link.getFormulaLanguageID())+ "';"); 
  objects.append("_docObjects[len].setFormula('=" + ViewerTools.escapeQuotes(link.getFormulaLanguageID())+ "');");
}
    out.println("_reportName=\"" + ViewerTools.escapeQuotes(((ReportContainer) doc.getStructure().getChildAt(nReportIndex)).getName()) + "\";");
    out.println( objects );
    out.println( variables );
}
DataProvider getDPFromExpression(ReportExpression expr)
{
if (expr instanceof DPExpression)
{
DPExpression dpExpr = (DPExpression) expr;
return dpExpr.getDP();
}
else
return null;
}
DataProvider getDPFromBlock(ReportBlock block)
{
if (block == null)
return null;
DataProvider dp = null;
dp = getDPFromAxis(block.getAxis(TableAxis.HORIZONTAL));
if (dp != null)
return dp;
dp = getDPFromAxis(block.getAxis(TableAxis.VERTICAL));
if (dp != null)
return dp;
dp = getDPFromAxis(block.getAxis(TableAxis.CONTENT));
if (dp != null)
return dp;
dp = getDPFromAxis(block.getAxis(GraphAxis.X));
if (dp != null)
return dp;
dp = getDPFromAxis(block.getAxis(GraphAxis.Y));
if (dp != null)
return dp;
dp = getDPFromAxis(block.getAxis(GraphAxis.Z));
if (dp != null)
return dp;
return null;
}
DataProvider getDPFromAxis(BlockAxis axis)
{
int count = axis.getCount();
for (int i = 0; i < count; i++)
{
DataProvider dp = getDPFromExpression(axis.getExpr(i));
if (dp != null)
return dp;
}
return null;
}
void printFormulas(Set fSet, DocumentInstance doc, DHTMLLogger _logger)
throws IOException, java.text.ParseException
{
_logger.info("printFormulas");
    ReportElementContainer reportStructure = doc.getStructure();
    if (reportStructure != null)
    {
        int count = reportStructure.getChildCount();
        fSet.add( new String("_formulaObjects = new Array();"));
        for (int i = 0; i < count; i++){
            printReportElement(fSet, ((ReportContainer) reportStructure.getChildAt(i)).getReportBody(), "body", _logger);
        }
    }
}
void printReportElement(Set fSet, ReportElement re, String className, DHTMLLogger _logger)
throws IOException
{
_logger.info("printReportElement");
    if (re != null)
    {
        if (className.equals("reportCell") && ((ReportCell) re).isCustomFormula()) {
           printFormula(fSet, ((ReportCell) re).getExpr(), ((ReportCell) re).getNestedExpr(), _logger);
        }
        for (int i = 0; i < re.getChildCount(); i++)
        {
            ReportElement childRE = (ReportElement) re.getChildAt(i);
            String childClassName = getREClassName(childRE);
            printReportElement(fSet, childRE, childClassName, _logger);
            if (childClassName.equals("block")) {
               printReportBlock(fSet, (ReportBlock) childRE, _logger);
            }
        }
    }
}
void printReportBlock(Set fSet, ReportBlock block, DHTMLLogger _logger)
throws IOException
{
    Representation repr = block.getRepresentation();
    BlockType blockType = repr.getType();
    if ((blockType == TableType.VTABLE)||(blockType == TableType.HTABLE)) {
        printSimpleTable(fSet, block, (SimpleTable) repr, _logger);
    } else if (blockType == TableType.FORM) {
        printForm(fSet, block, (Form) repr, _logger);
    } else if (blockType == TableType.XTABLE) {
        printCrossTable(fSet, block, (CrossTable) repr, _logger);
    } else if (blockType instanceof GraphType) { 
_logger.info("printGraph:" + block);
        printAxis(fSet, block.getAxis(GraphAxis.X), _logger);
        printAxis(fSet, block.getAxis(GraphAxis.Y), _logger);
        printAxis(fSet, block.getAxis(GraphAxis.Z), _logger);
    }
}
void printSimpleTable(Set fSet, ReportBlock block, SimpleTable table, DHTMLLogger _logger)
throws IOException
{
    boolean bVTable = (table.getType() == TableType.VTABLE);
    BlockAxis axis = block.getAxis(bVTable ? TableAxis.HORIZONTAL : TableAxis.VERTICAL);
    BlockBreak blockBreak = axis.getBlockBreak();
    int breakCount = blockBreak.getCount();
    int childVarIndex = 1;
    for (int i = -1; i < breakCount; i++)
    {
        BreakElement breakElem = (i == -1 ? null : blockBreak.getBreakElement(i));
        printCellMatrix(fSet, table.getHeader(breakElem), 1, _logger);
        printCellMatrix(fSet, table.getFooter(breakElem), -1, _logger);
    }
    printCellMatrix(fSet, table.getBody(), 1, _logger);
}
void printForm(Set fSet, ReportBlock block, Form form, DHTMLLogger _logger)
throws IOException
{
    printCellMatrix(fSet, form.getCells(), 1, _logger);
}
void printCrossTable(Set fSet, ReportBlock block, CrossTable xtable, DHTMLLogger _logger)
throws IOException
{
    BlockAxis hAxis = block.getAxis(TableAxis.HORIZONTAL);
    BlockAxis vAxis = block.getAxis(TableAxis.VERTICAL);
    BlockBreak hBlockBreak = hAxis.getBlockBreak();
    BlockBreak vBlockBreak = vAxis.getBlockBreak();
    int hBreakCount = hBlockBreak.getCount();
    int vBreakCount = vBlockBreak.getCount();
    for (int h = -1; h < hBreakCount; h++)
    {
        for (int v = -1; v < vBreakCount; v++)
        {
            BreakElement hbreakElem = (h == -1 ? null : hBlockBreak.getBreakElement(h));
            BreakElement vbreakElem = (v == -1 ? null : vBlockBreak.getBreakElement(v));
            printCellMatrix(fSet, xtable.getCellMatrix(VZoneType.TOP, HZoneType.LEFT,  vbreakElem, hbreakElem), -1, _logger);
            printCellMatrix(fSet, xtable.getCellMatrix(VZoneType.TOP, HZoneType.RIGHT, vbreakElem, hbreakElem), -1, _logger);
            if (h == -1)
            {
                printCellMatrix(fSet, xtable.getCellMatrix(VZoneType.TOP, HZoneType.BODY, vbreakElem, hbreakElem), 1, _logger);
                printCellMatrix(fSet, xtable.getCellMatrix(VZoneType.BOTTOM, HZoneType.BODY,  vbreakElem, hbreakElem), -1, _logger);
            }
            if (v == -1)
            {
                printCellMatrix(fSet, xtable.getCellMatrix(VZoneType.BODY,   HZoneType.RIGHT, vbreakElem, hbreakElem), -1, _logger);
                printCellMatrix(fSet, xtable.getCellMatrix(VZoneType.BODY,   HZoneType.LEFT,  vbreakElem, hbreakElem), 2, _logger);
            }
            printCellMatrix(fSet, xtable.getCellMatrix(VZoneType.BOTTOM, HZoneType.LEFT,  vbreakElem, hbreakElem), -1, _logger);
            printCellMatrix(fSet, xtable.getCellMatrix(VZoneType.BOTTOM, HZoneType.RIGHT, vbreakElem, hbreakElem), -1, _logger);
        }
    }
    printCellMatrix(fSet, xtable.getCellMatrix(VZoneType.BODY, HZoneType.BODY), 0, _logger);
}
void printAxis(Set fSet, BlockAxis axis, DHTMLLogger _logger)
throws IOException
{
_logger.info("printAxis:" + axis);
        if (axis != null)
        {            
            int count = axis.getCount();         
            for (int i = 0; i < count; i++)
            {
            _logger.info("printAxis i:" + i);
printFormula(fSet, axis.getExpr(i), null, _logger);
            }                        
} 
}
void printFormula(Set fSet, ReportExpression expr, ReportExpression nestedExpr, DHTMLLogger _logger)
throws IOException
{
    _logger.info("printFormula");
    if (expr != null && (expr instanceof FormulaExpression)) {
           FormulaExpression fexpr = (FormulaExpression) expr;
           _logger.info("printFormula: fexpr.getValue()" + fexpr.getValue());
           String formula = ViewerTools.escapeQuotes(fexpr.getValue());
           fSet.add(new String("len = _formulaObjects.length;_formulaObjects[len] = p.newBOObj('" +
                          formula + "','" +
                          formula + "'," +
                          "p." + ViewerTools.getWomType(fexpr.getQualification()) + "," +
                          "p." + ViewerTools.getWomDataType(fexpr.getType()) + ",'" +
                          formula + "',false,true);_formulaObjects[len].setFormula('"+formula+"');"));
    }
}
void printCellMatrix(Set fSet, CellMatrix matrix, int nRelatedAxis, DHTMLLogger _logger)
throws IOException
{
    if (matrix != null)
    {
        int rowCount = matrix.getRowCount();
        int colCount = matrix.getColumnCount();
        for (int row = 0; row < rowCount; row++)
        {
            for (int col = 0; col < colCount; col++) {
               TableCell cell = matrix.getCell(row, col);
               if (cell.isCustomFormula()) {
               printFormula(fSet, cell.getExpr(), cell.getNestedExpr(), _logger);
               }
            }
        }
    }
}
String getREClassName(ReportElement re)
{
    if (re instanceof SectionContainer)
        return "section";
    else if (re instanceof ReportBlock)
        return "block";
    else if (re instanceof ReportCell)
        return "reportCell";
    else if (re instanceof Cell)
        return "cell";
    return "";
}
%>
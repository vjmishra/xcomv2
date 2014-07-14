<%@ include file="incStartpage.jsp" %>
<%@ page import="com.crystaldecisions.sdk.properties.*" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->DataSummary.jsp");
    String strEntry      = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
    String iReport     = requestWrapper.getQueryParameter("iReport", false, "0");
    int              nReportIndex = Integer.parseInt(iReport);
DocumentInstance doc          = reportEngines.getDocumentFromStorageToken(strEntry);
StringBuffer sBuffToOut = new StringBuffer();
    boolean bBommServer = printBOMMParameters(sBuffToOut,entSession );
_logger.info("printQuerySources");
    printQuerySources(sBuffToOut, doc, nReportIndex, requestWrapper,bBommServer);
_logger.info("printVariables");
    printVariables(sBuffToOut, doc);
_logger.info("printFormulas");    
Set fSet = new TreeSet();
    printFormulas(fSet, doc, _logger);
    StringBuffer sOut = new StringBuffer(fSet.size()*20);    
Iterator it = fSet.iterator();
while (it.hasNext()) {
sOut.append((String) it.next());
}
out.println(sBuffToOut.toString());
out.println("var formulaObjects = new Array();");
out.println(sOut.toString());
   out.println("var incDataSummaryOK = true;");
_logger.info("<--DataSummary.jsp");
}
catch(ServerException e)
{
out.println("var incDataSummaryOK = false;");
out.println("var errMessage = "+"\""+ViewerTools.escapeQuotes(e.getLocalizedMessage())+"\""+" ;");
_logger.error("Error: " + e.getLocalizedMessage());
}
catch(Exception e)
{
out.println("var incDataSummaryOK = false;");
objUtils.incErrorMsg(e, out);
_logger.error("Error: " + e.getLocalizedMessage());
}
%>
<%!
void printQuerySources(StringBuffer sbToOut, DocumentInstance doc, int nReportIndex, RequestWrapper requestWrapper, boolean bBommServer)
throws IOException, java.text.ParseException
{
    DataProviders dataProviders = doc.getDataProviders();
    StringBuffer dps =  new StringBuffer("var dps = new Array(");
    StringBuffer univers = new StringBuffer("var universeObjects = new Array(");
    String dpVarName;
    int count = dataProviders.getCount();
    for (int i = 0; i < count; i++) {
        DataProvider dp = dataProviders.getItem(i);
        Query query = null;
        dpVarName = "dp" + String.valueOf(i) ;
        sbToOut.append("var " + dpVarName + " = new Array(");
sbToOut.append("\"" + ViewerTools.escapeQuotes(dp.getName()) + "\" ");
        if (!dp.hasCombinedQueries())
        { 
query = dp.getQuery();
sbToOut.append(  ", ");
sbToOut.append( "\"" + ViewerTools.escapeQuotes(dp.getDataSource().getLongName()) + "\", ");
sbToOut.append( "\"" + dp.getDuration() + "\", ");
sbToOut.append( "" + query.getMaxRowsRetrieved()+ ", ");
sbToOut.append( "" + dp.getDataSource().getMaxRowsRetrieved()+ ", ");
sbToOut.append( "" + dp.getRowCount() + ", ");
sbToOut.append( "" + dp.isSampledData());
Locale loc = ViewerTools.convStrToLocale(requestWrapper.getUserLocale());
String keydateValue = DataSourceTools.getKeydateValue(dp, loc);
if (keydateValue != null) {
        keydateValue = ViewerTools.replace(keydateValue, ",", "-", true);
        sbToOut.append( ",\"" + keydateValue + "\"");
}
}   
                sbToOut.append(");  \n");
dps.append(dpVarName);
printQueryUniverse(sbToOut, (!dp.hasCombinedQueries())?query:null, univers, bBommServer);
if (count > 0 && i < (count - 1)) {
dps.append(",");
univers.append(",");
}
    }
    dps.append(");");
    sbToOut.append(dps + "\n");
    univers.append(");");
    sbToOut.append(univers + "\n");
}
void printQueryUniverse(StringBuffer sbToOut, Query query, StringBuffer univers,boolean bBommServer)
throws IOException
{
    int count = (query!=null)?query.getResultObjectCount():0;
    univers.append( "new Array("); 
    for (int i = 0; i < count; i++) {
         DataSourceObject dso = query.getResultObject(i);
         univers.append( printObject(sbToOut, dso,bBommServer));
         if (count > 0 && i < (count-1)){
            univers.append(",");
        }
    }
    univers.append(")");
}
String printObject(StringBuffer sbToOut, DataSourceObject dso,boolean bBommServer)
throws IOException
{
String desc = dso.getDescription();
if (desc == null){
desc = "";
}
String techInfo = "";
String mapping = "";
String lineage = "";
String tmp = null;
Properties props = dso.getProperties();
if (props != null) {
tmp = props.getProperty(DataSourceObjectPropertiesType.TECHNICALDESCRIPTION);
if (tmp != null)
{
techInfo = tmp;
}
tmp = props.getProperty(DataSourceObjectPropertiesType.MAPPING);
if (tmp != null)
{
mapping = tmp;
}
tmp = props.getProperty(DataSourceObjectPropertiesType.LINEAGE);
if (tmp != null)
{
lineage = tmp;
}
}
if(bBommServer) {
DataSourceObject universeClass = (DataSourceObject)dso.getParent();
while (universeClass != null && universeClass.getQualification() != ObjectQualification.CLASS) {
     universeClass = (DataSourceObject)universeClass.getParent();
        }
    if(universeClass != null) {
lineage = universeClass.getName();
    }
}
return " newBOObj('"  + ViewerTools.escapeQuotes(dso.getName()) + "','" +
                        dso.getID() + "', " +
                        ViewerTools.getWomType(dso.getQualification()) + "," +
                        "_txt" + ",'" +
                        ViewerTools.escapeQuotes(desc) +
                        "',null,null,'" + 
                        ViewerTools.escapeQuotes(techInfo) + "','" +
                        ViewerTools.escapeQuotes(lineage) + "','" +
ViewerTools.escapeQuotes(mapping) + "')";
}
void printVariables(StringBuffer sbToOut, DocumentInstance doc)
throws IOException
{
  ReportDictionary rd  = doc.getDictionary();
  VariableExpression[] vars = rd.getVariables();
  VariableExpression ve = null;
  StringBuffer variables = new StringBuffer("var variableObjects = new Array(");
  for(int j = 0; j < vars.length; j++)
  {
ve = vars[j];
        variables.append(" newBOObj('" + ViewerTools.escapeQuotes(ve.getName()) + "','" + ve.getID() + "'," + ViewerTools.getWomType(ve.getQualification())+ "," + "_txt" + ",'" + ViewerTools.escapeQuotes(ve.getFormula().getValue()) + "')") ;
        if ( vars.length > 0 && j < (vars.length-1)){
           variables.append(",");
        }
  }
  variables.append(");");
  sbToOut.append(variables +"\n");
}
void printFormulas(Set fSet, DocumentInstance doc, DHTMLLogger _logger)
throws IOException, java.text.ParseException
{
_logger.info("printFormulas");
    ReportElementContainer reportStructure = doc.getStructure();
    if (reportStructure != null)
    {
        int count = reportStructure.getChildCount();        
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
            String childClassName =  CurrentCellInfo.getREClassName(childRE);
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
void printFormula(Set fSet, ReportExpression expr, ReportExpression nestedExpr, DHTMLLogger _logger)
throws IOException
{
    _logger.info("printFormula");
    if (expr != null && (expr instanceof FormulaExpression)) {
           FormulaExpression fexpr = (FormulaExpression) expr;
           _logger.info("printFormula: fexpr.getValue()" + fexpr.getValue());
           fSet.add(new String("formulaObjects[formulaObjects.length] = p.newBOObj('" +
                          ViewerTools.escapeQuotes(fexpr.getValue()) + "','" +
                          ViewerTools.escapeQuotes(fexpr.getValue()) + "'," +
                          ViewerTools.getWomType(fexpr.getQualification()) + "," +
                          ViewerTools.getWomDataType(fexpr.getType()) + ",'" +
                          ViewerTools.escapeQuotes(fexpr.getValue()) + "',false,true);"));
    }
}
boolean printBOMMParameters(StringBuffer sBuffToOut, IEnterpriseSession entSession )
throws IOException
{
    String SERVER_QUERY = "select SI_BOMM_INTEGRATION_SERVICE from CI_APPOBJECTS where SI_PROGID='CrystalEnterprise.WebIntelligence'";
    String bommServer = null;
    String bommUser = "";
    String bommPwd = "";
    String bommToken="";
    boolean bBommServer=false;
     try {
     IInfoStore iStore = (IInfoStore) entSession.getService("InfoStore");
IInfoObjects results=iStore.query(SERVER_QUERY );
if(results.getResultSize() >0) {
IInfoObject webiApplication = (IInfoObject) results.get(0);
IProperty bommPropertyBag= webiApplication.properties().getProperty("SI_BOMM_INTEGRATION_SERVICE");
if(bommPropertyBag != null) {
IProperty servicePoint=((IProperties) bommPropertyBag.getValue()).getProperty("SI_BOMM_SERVICE_POINT");
IProperty userName=((IProperties) bommPropertyBag.getValue()).getProperty("SI_BOMM_USER");
IProperty pwd=((IProperties) bommPropertyBag.getValue()).getProperty("SI_BOMM_PWD");
IProperty token=((IProperties) bommPropertyBag.getValue()).getProperty("SI_BOMM_TOKEN");
if( servicePoint!= null) {
bommServer = servicePoint.toString();
}
if(userName != null && pwd !=null) {
bommUser = userName.toString();
bommPwd =  pwd.toString();
}
if(token != null) {
bommToken = token.toString();
}
}
}
           } catch(Exception bommException){
bommServer =null;
bBommServer =false;
         }
         if(bommServer ==null){
sBuffToOut.append("var BOMM_SERVER = null;\n");
bBommServer =false;
          } else {
sBuffToOut.append("var BOMM_SERVER = '"+ViewerTools.escapeQuotes(bommServer)+"';\n");
sBuffToOut.append("var BOMM_USER = '"+ViewerTools.escapeQuotes(bommUser)+"';\n");
sBuffToOut.append("var BOMM_PWD = '"+ViewerTools.escapeQuotes(bommPwd) +"';\n");
sBuffToOut.append("var BOMM_TOKEN = '"+ViewerTools.escapeQuotes(bommToken) +"';\n");
bBommServer =true;
       }
return bBommServer;
}
%>

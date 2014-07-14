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
_logger.info("-->processFormula.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
String bids = requestWrapper.getQueryParameter("bids", false,"");
String sDefinition = requestWrapper.getQueryParameter("sFormula", false, "");
String action = requestWrapper.getQueryParameter("nAction", false, "0");
int nAction = Integer.parseInt(action);
String sName = requestWrapper.getQueryParameter("sName", false, "");
String sVarID = requestWrapper.getQueryParameter("sVarID", false, "");
String sQualif = requestWrapper.getQueryParameter("sQualif", false, "");
ObjectQualification qualif = ObjectQualification.DIMENSION;
if (sQualif.length()>0)
{
if (sQualif.equals("detail"))
qualif = ObjectQualification.DETAIL;
else if (sQualif.equals("measure"))
qualif = ObjectQualification.MEASURE;
}
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
    ReportDictionary rd  = doc.getDictionary();
String sDimID = "";
ReportExpression dpExpr = null;
if(sQualif.equals("detail"))
{
sDimID = requestWrapper.getQueryParameter("sAssociatedDim", false, "");
dpExpr = rd.getChild(sDimID);
_logger.info("--> associate to detail the dimension "+sDimID);
}
ReportExpression formula = null;
boolean bIsFormula = true;
if(sDefinition.startsWith("=")) 
{
if (nAction == 3)
{
_logger.info("--> replace variable ");  
_logger.info(sName+" "+sQualif+" "+sDefinition);
VariableExpression[] vars = rd.getVariables();
VariableExpression var=null;
String varName;
for(int j=0; j<vars.length; j++)
{
var = vars[j];
varName = vars[j].getName();
if(sName.equals(varName))
{
if (! var.getFormula().getValue().equals (sDefinition))
                        var.setValue(sDefinition);
if (! var.getQualification().toString().equals (qualif.toString()))
    var.setQualification(qualif);
if (sQualif.equals("detail")) {
    if (var.getAssociatedDimension()==null || dpExpr==null || !var.getAssociatedDimension().getName().equals(dpExpr.getName()))
    var.setAssociatedDimension(dpExpr);
}
formula = var;
break;
}
}
}
else if (nAction == 2) 
{
_logger.info("--> create new variable"); 
_logger.info("sName:" + sName + ", sQualif:" + sQualif + ", sDefinition:" + sDefinition);   
if (dpExpr!=null) {
_logger.info("dpExpr:" + dpExpr); 
formula = rd.createVariable(sName,qualif,sDefinition,dpExpr);
} else {
formula = rd.createVariable(sName,qualif,sDefinition);
}
}
else 
{
_logger.info("--> replace formula");  
formula = rd.createFormula(sDefinition);
}
}
else if(!sVarID.equals(""))
{
_logger.info("--> drag and drop variable ID= "+sVarID); 
formula = rd.getChild(sVarID);
if (formula == null) {
formula = rd.createFormula(sVarID);
} else if (sName.equals("")) {
sName = formula.getFormulaLanguageID();
}
}
else
bIsFormula = false;
if (formula != null) {
_logger.info("formula= " + formula); 
} else {
_logger.info("formula is null.");
}
String[] bid = ViewerTools.split(bids,",");
for (int i = 0; i < bid.length; i++)
{
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid[i]);
TableCell tc = cellInfo.m_tableCell;
if (tc != null)
{
_logger.info("cellInfo.m_tableCell.getID()=" + tc.getID());
if((nAction == 4) && (!sName.equals(""))) 
{
_logger.info("drag and drop object in table, insert header  if necessary");
CellMatrix cm = tc.getCellMatrix();
Representation repr = cm.getRepresentation();
boolean isHTable = (repr.getType() == TableType.HTABLE);
boolean isVTable = (repr.getType() == TableType.VTABLE);
if (isHTable || isVTable)
{
if((cm.getHZoneType()==HZoneType.BODY) && (cm.getVZoneType()==VZoneType.BODY))
{
int idxCol = tc.getColumn();
int idxRow = tc.getRow();
_logger.info("TableCell : idxCol = "+idxCol +" idxRow = "+idxRow);
ReportExpression NameOf = null;
ReportStructure rs = doc.getStructure();
Function funct = rs.getFunctionByID(291);
if (funct != null)
{
String sHeaderDef="="+funct.getName()+"("+sName+")";
_logger.info("sHeaderDef : "+sHeaderDef);
NameOf=rd.createFormula(sHeaderDef);
}
CellMatrix headers = ((SimpleTable) repr).getHeader(null);
cellInfo.setHeaderExpr(headers,idxRow,idxCol,NameOf);
BlockAxis axis = cellInfo.m_axis;
if (axis != null)
{
BlockBreak bBreak=axis.getBlockBreak();
BreakElement bElem=null;
CellMatrix bHeaders=null;
int nbBreaks=bBreak.getCount();
for (int j=0; j < nbBreaks; j++)
{
bElem = bBreak.getBreakElement(j);
bHeaders = ((SimpleTable) repr).getHeader(bElem);
cellInfo.setHeaderExpr(bHeaders,idxRow,idxCol,NameOf);
}
}
}
}
}
if (bIsFormula)
{
if (formula != null)
{
boolean isInitiallyAggregation = false;
ReportExpression initialExpr = tc.getExpr();
if (initialExpr != null)
{
if (initialExpr instanceof FormulaExpression)
{
FormulaExpression initialFormula = (FormulaExpression) initialExpr;
isInitiallyAggregation = initialFormula.isAggregation();
}
}
tc.setExpr(formula);
if (isInitiallyAggregation && (formula.getType() != ObjectType.TEXT))
{
tc.setFormatNumber(null);
}
}
} else {
tc.setText(sDefinition);
}
}
else if (cellInfo.m_cell != null)
{
if (cellInfo.m_cell instanceof FreeCell)
{
FreeCell fc = (FreeCell)cellInfo.m_cell;
if (bIsFormula)
{
if (formula!=null) (fc.toReportCell()).setExpr(formula);
}
else
fc.setValue(sDefinition);
}
else 
{
ReportCell rc = (ReportCell)cellInfo.m_cell;
if (bIsFormula)
{
if(formula!=null) rc.setExpr(formula);
}
else
(rc.toFreeCell()).setValue(sDefinition);
}
}
}
String followBid = (bid.length>1?"":bid[0]);
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
requestWrapper.setQueryParameter("sFollowBid", followBid);
_logger.info("<--processFormula.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_FORMULA_VARIABLE", true, out, session);
}
%>
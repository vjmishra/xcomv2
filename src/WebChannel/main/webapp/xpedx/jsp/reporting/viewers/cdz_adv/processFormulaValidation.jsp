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
<%
response.setDateHeader("expires", 0);
try {
_logger.info("-->processFormulaValidation.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
    String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
    int nReportIndex = Integer.parseInt(iReport);
    String sDefinition = requestWrapper.getQueryParameter("sFormula", false, "");
    String sDefFromViewer = requestWrapper.getQueryParameter("sParam1", false, "");
    if(sDefFromViewer.length()>0) 
sDefinition = sDefFromViewer;
boolean bIsFormula= true;
    if(sDefinition.startsWith("=")==false)
bIsFormula = false;
    String action = requestWrapper.getQueryParameter("nAction", false, "0");
    int nAction = Integer.parseInt(action);
_logger.info("nAction: " + nAction + "'0=validation 1=replaceFormula 2=createVariable 3=replaceVariable");
    String sEditInPlace = requestWrapper.getQueryParameter("bEditInPlace", false, "false");
    String sName = requestWrapper.getQueryParameter("sName", false, "");
    String sQualif = requestWrapper.getQueryParameter("sQualif", false, "");
    ObjectQualification qualif = ObjectQualification.DIMENSION;
    if(sQualif.length()>0)
    {
if (sQualif.equals("detail"))
qualif = ObjectQualification.DETAIL;
else if (sQualif.equals("measure"))
qualif = ObjectQualification.MEASURE;
    }     
    DocumentInstance objDocumentInstance = reportEngines.getDocumentFromStorageToken(strEntry);
    ReportDictionary rd  = objDocumentInstance.getDictionary();
    String sDimID = "";    
    ReportExpression dpExpr = null;    
    if(sQualif.equals("detail")) 
    {       
sDimID = requestWrapper.getQueryParameter("sAssociatedDim", false, "");  
dpExpr = rd.getChild(sDimID);  
}
_logger.info(" --> new formula : " + sDefinition);
  ReportExpression formula=null;
    String sType = "";
%>
<html>
<head>
<script language="javascript">
var msg=""
var par = parent.parent
var displayDlg= false, bError=true
var fromHyperlink =false
<%
try
{
if (nAction == 0)
  {
  if (bIsFormula) {
  formula = rd.createFormula(sDefinition);
  _logger.info(" validate formula:" + formula);
  }
  out.println("displayDlg = true;");
  out.println("bError = false;");
  out.println("parent.setValidateFormula(true);"); 
}
else if (nAction == 1)
{
out.println("if(parent._isFormulaEditor){par=parent.parent;}else{par=parent;}"); 
if (bIsFormula) {
formula = rd.createFormula(sDefinition);
_logger.info(" replace formula:" + formula);
}
out.println("if(parent._isFormulaEditor){parent.closeDlg(parent._isApplyMode);}"); 
}
  else if (nAction == 2) 
{
if( bIsFormula )
{
if (dpExpr != null) {
_logger.info(" sName:" + sName + ", qualif:" + qualif + ", sDefinition:" + sDefinition + ", dpExpr:" + dpExpr);
formula = rd.createVariable(sName,qualif,sDefinition,dpExpr);
} else {
_logger.info(" sName:" + sName + ", qualif:" + qualif + ", sDefinition:" + sDefinition + ", dpExpr:" + dpExpr);
formula = rd.createVariable(sName,qualif,sDefinition);
}
_logger.info(" create variable:" + formula);
}
out.println("parent.closeDlg(parent._isApplyMode)");
}
else if (nAction == 4) 
{
if (bIsFormula)
{
formula = rd.createFormula(sDefinition);
_logger.info(" validate formula:" + formula);
}
out.println("bError = false;");
out.println("fromHyperlink  = true;");
}
else 
  {  
  if( bIsFormula )
  {
  VariableExpression[] vars = rd.getVariables();
  VariableExpression var=null;
  String varName;
  for(int j=0; j<vars.length; j++)
  {
  var = vars[j];
  varName = vars[j].getName();
  if(sName.equals(varName))
  { 
  var.setValue(sDefinition);
  var.setQualification(qualif);  
  if (sQualif.equals("detail")) { 
  var.setAssociatedDimension(dpExpr);
  }
  formula = var;
  _logger.info(" replace variable:" + formula); 
  break;
  }
  }  
  }
  out.println("parent.closeDlg(parent._isApplyMode)");
  }
                if (formula != null)
                    sType = Integer.toString(formula.getType().value());
  } catch (IllegalArgumentException e) {
out.println("msg=parent._changeVariableAssociateDim;");
out.println("displayDlg = true;");
} catch (Exception e) {
e.printStackTrace();
out.println("msg=\""+ViewerTools.escapeQuotes(e.getLocalizedMessage())+"\";");
out.println("displayDlg = true;");
}
%>
function loadCB()
{
if(displayDlg)
{
if(bError)
par.showAlertDialog(msg,null,2);
else
par.showAlertDialog(parent.validationDlgMsg,null,0);
}
else if(fromHyperlink)
parent.setHyperlinkFormula();
else
{
par.eventManager.notify(par._EVT_VARS_UPDATE)
var p=""
if(parent.getURLParams) 
p=parent.getURLParams()
else
{
if(<%=sEditInPlace%>==true && par._paramsEditInPlace) 
p=par._paramsEditInPlace
else
p=par.urlParams(false)
}
var url = par._root + "processFormula.jsp" + p
var definition="<%=ViewerTools.escapeQuotes(sDefinition)%>"
var name="<%=ViewerTools.escapeQuotes(sName)%>"
var qualification="<%=ViewerTools.escapeQuotes(sQualif)%>"
var associatedDim="<%=ViewerTools.escapeQuotes(sDimID)%>"
var action =<%=nAction%>
self.document.submitForm.action = url;
self.document.submitForm.target = "Report"
self.document.submitForm.sName.value = name
self.document.submitForm.sFormula.value = definition
self.document.submitForm.sQualif.value = qualification
self.document.submitForm.sAssociatedDim.value = associatedDim
self.document.submitForm.nAction.value = action;
self.document.submitForm.submit();
}
if (parent.textTypeValue)
    parent.textTypeValue ("<%=sType%>")
}
</script>
</head>
<body onload="loadCB()">
<!-- Hidden form -->
<form target="Report" style="display:none" name="submitForm" method="post" action="">
<input type="hidden" name="sName" id="sName">
<input type="hidden" name="sFormula" id="sFormula">
<input type="hidden" name="sQualif" id="sQualif">
<input type="hidden" name="sAssociatedDim" id="sAssociatedDim">
<input type="hidden" name="nAction" id="nAction">
<input type="hidden" name="bidon" id="bidon">
</form>
</body>
</html>
<%
}
catch(Exception e) {
objUtils.displayErrorMsg(e, "_ERR_FORMULA_VARIABLE", true, out, session);
}
%>
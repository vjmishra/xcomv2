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
try
{
_logger.info("-->processGrouping.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true); 
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
    _logger.info("strEntry = " + strEntry );
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
    String strReport = requestWrapper.getQueryParameter("iReport", false, "0");
    int nReportIndex = Integer.parseInt(strReport);
    String bid = requestWrapper.getQueryParameter("sBid", true);
_logger.info("bid = " + bid);    
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
String expr = cellInfo.m_expr.getName();
String sVarName = requestWrapper.getQueryParameter("sVarName",false,"");
if (sVarName.equals(""))
sVarName = expr+"+";
_logger.info("sVarName = " + sVarName);
String sGroups = requestWrapper.getQueryParameter("sGroups",false,"");
_logger.info("sGroups = " + sGroups);
String sFormula = buildFormula( _logger, doc, sGroups, expr );
_logger.info("sFormula="+sFormula);
ReportDictionary rd  = doc.getDictionary();
ObjectQualification qualif = ObjectQualification.DIMENSION;
ReportExpression variable = rd.createVariable(sVarName,qualif,sFormula);
cellInfo.replace(variable.getID());
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
_logger.info("<--processGrouping.jsp");
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
<%!
String buildFormula( DHTMLLogger _logger, DocumentInstance doc, String sGroups, String expr )
throws Exception, IOException, java.text.ParseException
{
String sFormula="=";
int nbIF=0;
ReportStructure rs = doc.getStructure();
Function funct = rs.getFunctionByID(AllAvailableFunctionsIdentifiers.ID_IF);
if ( funct == null )
throw new Exception();
String funcIF = funct.getName();
String[] arrGroups = ViewerTools.convertStringToArray(sGroups);
for (int i = 0; i < arrGroups.length; i++)
{
_logger.info("arrGroups["+i+"]="+arrGroups[i]);
String[] arrGroup = ViewerTools.convertStringToArray(arrGroups[i]);
String gpName = arrGroup[0];
_logger.info("gpName="+gpName);
String[] values = ViewerTools.convertStringToArray( arrGroup[1] );
nbIF+=values.length;
for (int j = 0; j < values.length; j++)
{
_logger.info("values["+j+"]="+values[j]);
sFormula+=beginIFFormula( funcIF, expr, values[j], gpName );
}
}
sFormula+=endIFFormula( expr );
for (int k=0; k<(nbIF-1); k++)
{
sFormula+=endIFFormula( null );
}
return sFormula;
}
String beginIFFormula( String funcIF, String expr, String val, String gpName )
throws IOException, java.text.ParseException
{
String tmp=funcIF;
tmp+="(";
tmp+="["+ViewerTools.escapeQuotes(expr)+"]";
tmp+="=";
tmp+="\""+ViewerTools.escapeQuotes(val)+"\"";
tmp+=";";
tmp+="\""+ViewerTools.escapeQuotes(gpName)+"\"";
tmp+=";";
return tmp;
}
String endIFFormula( String expr )
throws IOException, java.text.ParseException
{
String tmp="";
if (expr!=null)
tmp+="["+ViewerTools.escapeQuotes(expr)+"]";
tmp+=")";
return tmp;
}
%>
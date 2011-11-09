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
if (!isAlive)
{
objUtils.invalidSessionDialog(out);
return;
}
%>
<%!
String strEntry = null;
String strViewerID = null;
String strBid = null;
String strAction = null;
String strPrivateAttrib = null;
String strNavType = null;
String strURI = null;
String strQS = null;
String strCellContent = null;
String strToolTip =null;
String strTargetFrame = null;
String strNewFormula = null;
boolean isCellHasLink = false;
String strFreeCell = null;
boolean isFreeCell = false;
String strURLEncode = null;
boolean isHyperlink = false;
%>
<%
try
{
strEntry = requestWrapper.getQueryParameter("sEntry", true);
strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
strBid = requestWrapper.getQueryParameter("sBid", true);
strAction = requestWrapper.getQueryParameter("sAction", true);
if (strAction.equals("U"))
{
strURI = requestWrapper.getQueryParameter("sURI", true);
strPrivateAttrib = requestWrapper.getQueryParameter("sPrivateAttrib", true);
strNavType = requestWrapper.getQueryParameter("sNavType", true);
strQS = requestWrapper.getQueryParameter("sQueryString", false, "");
strCellContent = requestWrapper.getQueryParameter("sCellContent", false, "");
strToolTip = requestWrapper.getQueryParameter("sTooltip", false, "");
strTargetFrame = requestWrapper.getQueryParameter("sWinTarget", false, "_blank");
strFreeCell = requestWrapper.getQueryParameter("sFreeCell", false, "false");
isFreeCell = (strFreeCell.equals("true"))? true : false;
}
else if (strAction.equals("S"))
{
String strHyperlink = requestWrapper.getQueryParameter("sHyperlink", true);
isHyperlink = (strHyperlink.equals("true"))? true : false;
}
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
if (strAction.equals("U"))
{
String sep = "\\\"";
strURLEncode = getLocaleURLEnocde(doc);
String strURL = strURI;
String strQueryString = formatParameters(strQS);
if (strQueryString != null && !strQueryString.equals(""))
strURL += "?" + strQueryString;
strNewFormula = "=\"<a href=" + sep + strURL + sep;
strNewFormula += " title=" + sep + formatTooltip(strToolTip) + sep;
strNewFormula += " target=" + sep + strTargetFrame + sep;
strNewFormula += " " + strPrivateAttrib + "=" + sep + strNavType + sep + ">";
strNewFormula += formatContent(strCellContent) + "</a>\"";
strNewFormula = parseNewFormula(strNewFormula);
}
ReportDictionary rd = doc.getDictionary();
String iReport = requestWrapper.getQueryParameter("iReport", true, "0");
int nReportIndex = Integer.parseInt(iReport);
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, strBid);
if (cellInfo.m_tableCell != null)
{
isCellHasLink = (cellInfo.m_tableCell.getContentType() == CellContentType.HYPERLINK)? true : isCellHasLink;
if (cellInfo.m_tableCell instanceof TableCell)
{
TableCell tabcell = (TableCell)cellInfo.m_tableCell;
ReportExpression tabExpr = tabcell.getExpr();
if (tabExpr != null)
{
ReportExpression nExpr = tabcell.getNestedExpr();
if (nExpr != null && nExpr instanceof VariableExpression)
{
updateVariableDefinition(tabcell, nExpr, rd, strAction);
}
else if (nExpr != null && nExpr instanceof DPExpression)
{
updateDPObjectValue(tabcell, rd, strAction);
}
else if (tabExpr instanceof FormulaExpression)
{
FormulaExpression fmExpr = (FormulaExpression)tabExpr;
updateFormula(tabcell, fmExpr, rd, strAction);
}
}
}
else if (cellInfo.m_tableCell instanceof FreeCell)
{
FreeCell fcell = (FreeCell)cellInfo.m_tableCell;
updateFreeCellText(fcell, rd, strAction);
}
}
else if(cellInfo.m_cell  != null)
{
isCellHasLink = (cellInfo.m_cell.getContentType() == CellContentType.HYPERLINK)? true : isCellHasLink;
if (cellInfo.m_cell instanceof ReportCell)
{
ReportCell repcell = (ReportCell)cellInfo.m_cell;
ReportExpression repExpr = repcell.getExpr();
if (repExpr != null)
{
ReportExpression nExpr = repcell.getNestedExpr();
if (nExpr != null && nExpr instanceof VariableExpression)
{
updateVariableDefinition(repcell, nExpr, rd, strAction);
}
else if (nExpr != null && nExpr instanceof DPExpression)
{
updateDPObjectValue(repcell, rd, strAction);
}
else if (repExpr instanceof FormulaExpression)
{
FormulaExpression fmExpr = (FormulaExpression)repExpr;
updateFormula(repcell, fmExpr, rd, strAction);
}
}
}
else if (cellInfo.m_cell instanceof FreeCell)
{
FreeCell fcell = (FreeCell)cellInfo.m_cell;
updateFreeCellText(fcell, rd, strAction);
}
}
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
out.println("<html><head><script language=\"javascript\">");
out.println("function okCB() {parent.restoreAfterError()};");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_HYPERLINK", false, "okCB", out, session);
}
%>
<%!
private int _tokenLimit = 500;
private int _partLength = 450;
private void updateFormula(ReportCell cell, FormulaExpression fmExpr, ReportDictionary rd, String strAction)
{
if (strAction.equalsIgnoreCase("R"))
{
if (fmExpr != null)
{
String strValue = fmExpr.getValue();
if (strValue != null)
{
strValue = removeATagFromFormula(strValue);
if (strValue.startsWith("="))
{
FormulaExpression formula = rd.createFormula(strValue);
cell.setExpr(formula);
}
cell.setContentType(CellContentType.TEXT);
}
}
}
else if (strAction.equalsIgnoreCase("U"))
{
setFormulaAsHyperlink(cell, rd);
}
else if (strAction.equalsIgnoreCase("S"))
{
setContentTypeAs(cell);
}
}
private void updateVariableDefinition(ReportCell cell, ReportExpression nestedExpr, ReportDictionary rd, String strAction)
{
if (strAction.equalsIgnoreCase("R"))
{
if (nestedExpr != null)
{
VariableExpression varExpr = (VariableExpression)nestedExpr;
FormulaExpression nestedFExpr = varExpr.getFormula();
String strValue = nestedFExpr.getValue();
if (strValue != null)
{
strValue = removeATagFromFormula(strValue);
if (strValue.startsWith("="))
{
varExpr.setValue(strValue);
}
cell.setContentType(CellContentType.TEXT);
}
}
}
else if (strAction.equalsIgnoreCase("U"))
{
setFormulaAsHyperlink(cell, rd);
}
else if (strAction.equalsIgnoreCase("S"))
{
setContentTypeAs(cell);
}
}
private void updateDPObjectValue(ReportCell cell, ReportDictionary rd, String strAction)
{
if (strAction.equalsIgnoreCase("R"))
{
cell.setContentType(CellContentType.TEXT);
}
else if (strAction.equalsIgnoreCase("U"))
{
setFormulaAsHyperlink(cell, rd);
}
else if (strAction.equalsIgnoreCase("S"))
{
setContentTypeAs(cell);
}
}
private void setFormulaAsHyperlink(ReportCell cell, ReportDictionary rd)
{
if (strNewFormula!=null)
{
FormulaExpression formula = rd.createFormula(strNewFormula);
cell.setExpr(formula);
}
cell.setContentType(CellContentType.HYPERLINK);
}
private void setContentTypeAs(ReportCell cell)
{
if (isHyperlink)
cell.setContentType(CellContentType.TEXT);
else
cell.setContentType(CellContentType.HYPERLINK);
}
private void updateFormula(TableCell cell, FormulaExpression fmExpr, ReportDictionary rd, String strAction)
{
if (strAction.equalsIgnoreCase("R"))
{
if (fmExpr != null)
{
String strValue = fmExpr.getValue();
if (strValue != null)
{
strValue = removeATagFromFormula(strValue);
if (strValue.startsWith("="))
{
FormulaExpression formula = rd.createFormula(strValue);
cell.setExpr(formula);
}
else {
cell.setText(strValue);
}
cell.setContentType(CellContentType.TEXT);
}
}
}
else if (strAction.equalsIgnoreCase("U"))
{
setFormulaAsHyperlink(cell, rd);
}
else if (strAction.equalsIgnoreCase("S"))
{
setContentTypeAs(cell);
}
}
private void updateVariableDefinition(TableCell cell, ReportExpression nestedExpr, ReportDictionary rd, String strAction)
{
if (strAction.equalsIgnoreCase("R"))
{
if (nestedExpr != null)
{
VariableExpression varExpr = (VariableExpression)nestedExpr;
FormulaExpression nestedFExpr = varExpr.getFormula();
String strValue = nestedFExpr.getValue();
if (strValue != null)
{
strValue = removeATagFromFormula(strValue);
if (strValue.startsWith("="))
{
varExpr.setValue(strValue);
}
cell.setContentType(CellContentType.TEXT);
}
}
}
if (strAction.equalsIgnoreCase("U"))
{
setFormulaAsHyperlink(cell, rd);
}
else if (strAction.equalsIgnoreCase("S"))
{
setContentTypeAs(cell);
}
}
private void updateDPObjectValue(TableCell cell, ReportDictionary rd, String strAction)
{
if (strAction.equalsIgnoreCase("R"))
{
cell.setContentType(CellContentType.TEXT);
}
else if (strAction.equalsIgnoreCase("U"))
{
setFormulaAsHyperlink(cell, rd);
}
else if (strAction.equalsIgnoreCase("S"))
{
setContentTypeAs(cell);
}
}
private void setFormulaAsHyperlink(TableCell cell, ReportDictionary rd)
{
if (strNewFormula!=null)
{
FormulaExpression formula = rd.createFormula(strNewFormula);
cell.setExpr(formula);
}
cell.setContentType(CellContentType.HYPERLINK);
}
private void setContentTypeAs(TableCell cell)
{
if (isHyperlink)
cell.setContentType(CellContentType.TEXT);
else
cell.setContentType(CellContentType.HYPERLINK);
}
private void updateFreeCellText(FreeCell fc, ReportDictionary rd, String strAction)
{
if (strAction.equalsIgnoreCase("R"))
{
String strValue = fc.getValue();
if (strValue != null)
{
strValue = removeATagFromText(strValue);
fc.setValue(strValue);
fc.setContentType(CellContentType.TEXT);
}
}
else if (strAction.equalsIgnoreCase("U"))
{
if (strNewFormula!=null)
{
if (isFormulaHasVariable(strNewFormula))
{
FormulaExpression formula = rd.createFormula(strNewFormula);
ReportCell rc = fc.toReportCell();
rc.setExpr(formula);
rc.setContentType(CellContentType.HYPERLINK);
}
else
{
String strText = cnvFormula2Text(strNewFormula);
fc.setValue(strText);
fc.setContentType(CellContentType.HYPERLINK);
}
}
else
fc.setContentType(CellContentType.HYPERLINK);
}
else if (strAction.equalsIgnoreCase("S"))
{
if (isHyperlink)
fc.setContentType(CellContentType.TEXT);
else
fc.setContentType(CellContentType.HYPERLINK);
}
}
private String removeATagFromText(String strValue)
{
String strRet = strValue;
try
{
String strStartTagB = "<a";
String strStartTagE = ">";
String strEndTag = "</a>";
int iCp = 0;
int iPos = 0;
while (iPos > -1 && iCp < 100)
{
iPos = strRet.indexOf(strStartTagB, iPos);
if (iPos < 0) iPos = strRet.indexOf(strStartTagB.toUpperCase());
if (iPos > -1)
{
int iPosA = strRet.indexOf(strStartTagE, iPos + 1);
if (iPosA > -1)
{
String strLeft = strRet;
String strRight = strRet;
strRet = strLeft.substring(0, iPos) + strRight.substring(iPosA + 1);
iPos = strRet.indexOf(strEndTag);
if (iPos < 0) iPos = strRet.indexOf(strEndTag.toUpperCase());
if (iPos > -1)
{
iPosA = iPos + strEndTag.length();
if (iPosA > -1)
{
strLeft = strRet;
strRight = strRet;
strRet = strLeft.substring(0, iPos) + strRight.substring(iPosA);
}
}
}
else
iPos = -1;
}
iCp++;
}
}
catch (Exception e)
{
e.printStackTrace();
strRet = null;
}
return strRet;
}
private String removeATagFromFormula(String strValue)
{
String strRet = strValue;
try
{
String strStartTagB = "\"" + "<a";
String strStartTagE = ">";
String strEndTag = "</a>" + "\"";
int iCp = 0;
int iPos = 0;
while (iPos > -1 && iCp < 100)
{
iPos = strRet.indexOf(strStartTagB, iPos);
if (iPos < 0) iPos = strRet.indexOf(strStartTagB.toUpperCase());
if (iPos > -1)
{
int iPosA = strRet.indexOf(strStartTagE, iPos + 1);
if (iPosA > -1)
{
iPosA++;
char c = strRet.charAt(iPosA);
if (strRet.charAt(iPosA) == '"')
{
//Formula contains variable 
iPosA++;
strEndTag = "\"" + strEndTag;
//Remove +
for (int i=iPosA; i<strRet.length(); i++)
{
c = strRet.charAt(i);
if (c != ' ' && c != '+')
{
iPosA = i;
break;
}
}
String strLeft = strRet;
String strRight = strRet;
strRet = strLeft.substring(0, iPos) + strRight.substring(iPosA);
iPos = strRet.indexOf(strEndTag);
if (iPos < 0) iPos = strRet.indexOf(strEndTag.toUpperCase());
if (iPos > -1)
{
iPosA = iPos + strEndTag.length();
//Remove +
iPos--;
for (int i=iPos; i>=0; i--)
{
c = strRet.charAt(i);
if (c != ' ' && c != '+')
{
iPos = i;
break;
}
}
strLeft = strRet;
strRight = strRet;
strRet = strLeft.substring(0, iPos + 1) + strRight.substring(iPosA);
}
}
else {
iPos = strRet.indexOf(strEndTag);
if (iPos < 0) iPos = strRet.indexOf(strEndTag.toUpperCase());
if (iPos > -1)
{
strRet = strRet.substring(iPosA, iPos);
}
}
}
else
iPos = -1;
}
iCp++;
}
}
catch (Exception e)
{
e.printStackTrace();
strRet = null;
}
return strRet;
}
private String getLocaleURLEnocde(DocumentInstance doc)
{
ReportStructure reportStruct = doc.getStructure();
Function func = reportStruct.getFunctionByID(516);
return func.getName();
}
private String formatParameters(String strParams)
{
String strRet = strParams;
try
{
strRet += "&";
StringBuffer sb = new StringBuffer();
int n = 0;
int iLevel = 0;
for (int i=0; i<strRet.length(); i++)
{
if (strRet.charAt(i) == '(')
iLevel++;
else if (strRet.charAt(i) == ')')
iLevel--;
else if (iLevel == 0 && strRet.charAt(i) == '&')
{
String v = strRet.substring(n, i);
n = i + 1;
int iPos = v.indexOf('=');
if (iPos > -1)
{
if (sb.length() > 0)
sb.append("&");
sb.append(v.substring(0, iPos + 1));
String r = v.substring(iPos + 1);
if (!r.equals(""))
{
if (r.charAt(0) == '(')
{
r=r.substring(2,r.length()-1);
if (r.indexOf('"') > -1)
r = "\"+" + strURLEncode + "(" + r + ")+\"";
else
r = "\"+" + strURLEncode + "(\"\"+" + r + ")+\"";
}
}
sb.append(r);
}
}
}
strRet = sb.toString();
}
catch(Exception e)
{
e.printStackTrace();
strRet = null;
}
strRet = (strRet == null)? strParams : strRet;
return strRet;
}
private String formatContent(String strContent)
{
String strRet = strContent;
try
{
strRet = strRet.trim();
if (!strRet.equals(""))
{
if (strRet.charAt(0) == '=')
strRet = "\"+" + strRet.substring(1) + "+\"";
}
}
catch(Exception e)
{
e.printStackTrace();
strRet = null;
}
strRet = (strRet == null)? strContent : strRet;
return strRet;
}
private String formatTooltip(String strTooltip)
{
String strRet = strTooltip;
try
{
strRet = strRet.trim();
if (!strRet.equals(""))
{
if (strRet.charAt(0) == '=')
{
strRet = ViewerTools.replace(strToolTip, "\\\"", "&quot;", true);
strRet = strRet.substring(1);
if (!strRet.startsWith("(") || !strRet.endsWith(")"))
strRet = "(" + strRet + ")";
strRet = "\"+" + strRet + "+\"";
}
else
strRet = ViewerTools.replace(strToolTip, "\"", "&quot;", true);
}
}
catch(Exception e)
{
e.printStackTrace();
strRet = null;
}
strRet = (strRet == null)? strTooltip : strRet;
return strRet;
}
private boolean isFormulaHasVariable(String strFormula)
{
boolean bRet = false;
if (strFormula != null)
{
int iPos = strFormula.indexOf("+[");
if (iPos >= 0)
{
    iPos = strFormula.indexOf("]+", iPos);
if (iPos >= 0)
bRet = true;
}
if (!bRet)
{
iPos = strFormula.indexOf("+URLEncode(");
if (iPos >= 0)
bRet = true;
}
}
return bRet;
}
private String cnvFormula2Text(String strFormula)
{
String strRet = strFormula;
if (strRet != null)
{
if (strRet.startsWith("=\"") && strRet.endsWith("\""))
{
strRet = strRet.substring(2, strRet.length()-1);
strRet = ViewerTools.replace(strRet, "\\\"", "\"", true);
}
}
return strRet;
}
private String parseNewFormula(String formula)
{
if (formula == null)
return null;
int iLen = strNewFormula.length();
StringBuffer sbOut = new StringBuffer(iLen);
int iLevel = 0;
int iPos = 0;
for (int i = 0; i < iLen; i++)
{
char c = strNewFormula.charAt(i);
if (c == '"')
{
char pc = (i > 0)? strNewFormula.charAt(i - 1) : ' ';
if (pc != '\\')
{
iLevel++;
if (iLevel == 1)
{
iPos = i;
continue;
}
}
if (iLevel == 2)
{
// String found
String strToken = strNewFormula.substring(iPos+1, i);
if (strToken.length() >= _tokenLimit)
strToken = splitString(strToken);
sbOut.append("\"" + strToken + "\"");
iLevel = 0;
iPos = 0;
continue;
}
}
if (iLevel == 0)
sbOut.append(c);
}
return sbOut.toString();
}
private String splitString(String str)
{
String strRet = str;
if (str.length() >= _tokenLimit)
{
// Split string
strRet = "";
while (str.length() > _partLength)
{
int iPartLen = _partLength;
String part = str.substring(0, iPartLen);
char c = part.charAt(iPartLen - 1);
int iCpt = 3;
while (c == '\\' && iCpt > 0)
{
iCpt--;
iPartLen--;
if (iPartLen > 0)
c = part.charAt(iPartLen - 1);
}
strRet += str.substring(0, iPartLen) + "\"+\"";
str = str.substring(iPartLen);
}
strRet += str;
}
return strRet;
}
%>

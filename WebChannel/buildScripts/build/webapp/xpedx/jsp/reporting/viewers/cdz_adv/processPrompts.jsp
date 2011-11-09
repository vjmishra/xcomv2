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
String strSRC = requestWrapper.getQueryParameter("src", false, "");
String strCancel = requestWrapper.getQueryParameter("sCancel", false, "");
String strAdvPrompts = requestWrapper.getQueryParameter("advPrompts", false, "no");
String strParentFrame = (strAdvPrompts.equals("yes") && !strSRC.equals("BCA"))?"parent":"self";
try
{
_logger.info("--> processPrompts.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strNEV = requestWrapper.getQueryParameter("sNEV", false, "no");
String strNewDoc = requestWrapper.getQueryParameter("sNewDoc", false, "false");
String strCreateDefaultReportBody = requestWrapper.getQueryParameter("bCreateDefaultReportBody", false, "false");
boolean bCreateDefaultReportBody = Boolean.valueOf(strCreateDefaultReportBody).booleanValue();
_logger.info("strCreateDefaultReportBody="+strCreateDefaultReportBody);
String strApplyFormat = requestWrapper.getQueryParameter("sApplyFormat", false, ""); 
String strValidateSQL = requestWrapper.getQueryParameter("bValidateSQL", false, "false");
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "");
String action = requestWrapper.getQueryParameter("nAction", false, "");
String strEmptyLab = requestWrapper.getQueryParameter("sEmptyLab",false,"");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
session.setAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"), doc);
if (!doc.getMustFillPrompts() && !strSRC.equals("BCA"))
{
doc.refresh();
if (doc.getMustFillContexts())
{
Contexts arrContexts = doc.getContextPrompts();
for (int i=0; i<arrContexts.getCount(); i++)
{
Context objContext = arrContexts.getItem(i);
String[] arrSelContexts = null;
if (null != objContext.getCurrentValues() && objContext.getCurrentValues().length > 0)
arrSelContexts = objContext.getCurrentValues();
else if (null != objContext.getPreviousValues() && objContext.getPreviousValues().length > 0)
arrSelContexts = objContext.getPreviousValues();
if (null != arrSelContexts)
objContext.enterValues(arrSelContexts);
}
doc.setContexts();
arrContexts = null;
}
}
Prompts arrPrompts = doc.getPrompts();
int iNbPrompts = arrPrompts.getCount();
for (int i=1; i<=iNbPrompts; i++)
{
Prompt objPrompt = arrPrompts.getItem(i-1);
String strParamName = "PV" + Integer.toString(i);
String strParamIndex = "PI" + Integer.toString(i);
String[] arrSelValues = requestWrapper.getQueryParameterValues(strParamName);
String[] arrSelIndexes = requestWrapper.getQueryParameterValues(strParamIndex);
if (arrSelValues != null && arrSelIndexes != null)
{
ValueFromLov[] valueFromLovArray = null;
ValueFromLov objValueFromLov = null;
if (objPrompt.getType() == PromptType.Mono)
{
if(!arrSelValues[0].equals(""))
{
if(arrSelValues[0].equals(strEmptyLab))
arrSelValues[0]="";
valueFromLovArray = new ValueFromLov[1];
objValueFromLov = new ValueFromLov(ViewerTools.decodeIndex(arrSelIndexes[0]), arrSelValues[0]);
valueFromLovArray[0] = objValueFromLov;
removeCurrentValues(objPrompt);
objPrompt.enterValues(valueFromLovArray);
}
}
else if (objPrompt.getType() == PromptType.Multi)
{
if (arrSelValues.length == 1)
arrSelValues = ViewerTools.split(arrSelValues[0], ";");
if (arrSelIndexes.length == 1)
arrSelIndexes = ViewerTools.split(arrSelIndexes[0], ";");
if(!arrSelValues[0].equals(""))
{
valueFromLovArray = new ValueFromLov[arrSelValues.length];
for (int j=0; j<arrSelValues.length; j++)
{
if(arrSelValues[j].equals(strEmptyLab))
arrSelValues[j]="";
if (arrSelIndexes.length > j)
objValueFromLov = new ValueFromLov(ViewerTools.decodeIndex(arrSelIndexes[j]), arrSelValues[j]);
else
objValueFromLov = new ValueFromLov("", arrSelValues[j]);
valueFromLovArray[j] = objValueFromLov;
}
removeCurrentValues(objPrompt);
objPrompt.enterValues(valueFromLovArray);
}
}
}
}
if (!strSRC.equals("BCA"))
{
doc.setPrompts();
}
if(strValidateSQL.equals("true"))
{
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
_logger.info("ValidateSQL");
out.println("<html><head><script language=\"javascript\">");
out.println("p=" + strParentFrame + ";");
out.println("pp=p.parent;");
out.println("pp.hideWaitDlg();");
out.println("pp.changeEntry('"+strEntry+"');");
out.println("params=pp.urlParamsNoBID();");
out.println("params += '&iDPIndex="+strDPIndex+"&nAction="+action+"';");
out.println("fr = pp.getFrame('DlgFrame',pp);");
out.println("function loadCB(){ pp.frameNav('validateSQL',pp._root+'processSetSQL'+pp._appExt+params,null,fr);}");
out.println("</script></head><body onload='loadCB()'></body></html>");
}
else
{
if (bCreateDefaultReportBody) 
{
_logger.info("bCreateDefaultReportBody="+bCreateDefaultReportBody);
int indexDP = -1;
if(!strDPIndex.equals(""))
indexDP = Integer.parseInt(strDPIndex);
ApplyQueries objApplyQueries = new ApplyQueries();
String sDefaultRepTitle= requestWrapper.getQueryParameter("defaultRepTitle",false,"");
objApplyQueries.createDefaultReportBody(doc,indexDP,sDefaultRepTitle);
_logger.info("createDefaultReportBody with title=" + sDefaultRepTitle);
bCreateDefaultReportBody = false;
strApplyFormat="true";
}
if(strApplyFormat.equals("true"))
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
out.println("<html><head><script language=\"javascript\">");
out.println("p=" + strParentFrame + ";");
out.println("pp=p.parent;");
out.println("pp.bCreateDefaultReportBody=" + bCreateDefaultReportBody + ";");
if (strSRC.equals("BCA"))
{
_logger.info("SRC = BCA");
session.setAttribute("CDZ." + strViewerID + ".Prompts", arrPrompts);
out.println("pp.hideWaitDlg();");
out.println("if (pp.advPromptsFrame)");
out.println("pp.advPromptsFrame.location.replace(\"lib/empty.html\");");
out.println("pp.goFinishSchedule(\"" + strEntry + "\");");
}
else if (strSRC.equals("RPV"))
{
_logger.info("SRC = RPV");
out.println("pp.hideWaitDlg();");
out.println("pp.goReportPartViewer(\"" + strEntry + "\");");
}
else
{
_logger.info("refresh document");
String strQueryString = requestWrapper.getQueryString();
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
_logger.info("strQueryString="+strQueryString);
if (strNEV.equals("yes"))
out.println("pp.Report.location.replace(\"report.jsp?" + ViewerTools.escapeQuotes(strQueryString) + "\");");
else
out.println("pp.Report.location=\"report.jsp?" + ViewerTools.escapeQuotes(strQueryString) + "\";");
out.println("if (typeof(pp.blnNeedAnswer)!=\"undefined\")");
out.println("  pp.blnNeedAnswerCP=true;");
out.println("if (p.name!=\"Report\")");
out.println("  p.location.replace(\"lib/empty.html\");");
}
out.println("</script></head><body></body></html>");
}
_logger.info("<-- processPrompts.jsp");
}
catch(Exception e)
{
if (strSRC.equals("BCA") || strSRC.equals("RPV"))
{
out.println("<html><head><script language=\"javascript\">");
out.println("p=" + strParentFrame + ";");
out.println("pp=p.parent;");
if (strSRC.equals("BCA"))
out.println("function okCB() {pp.goBackSchedule();}");
else
out.println("function okCB() {pp.goBackDashboard();}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_PROMPTS", false, "okCB",out, session);
}
else if (!strCancel.equals(""))
{
out.println("<html><head><script language=\"javascript\">");
out.println("p=" + strParentFrame + ";");
out.println("pp=p.parent;");
out.println("previousURL=pp.allUseDictionary.get(\"" + strCancel + "\");");
out.println("function okCB() {pp.frameNav(\"Report\",previousURL);}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_PROMPTS", false, "okCB",out, session);
}
else
objUtils.displayErrorMsg(e, "_ERR_PROMPTS", true, out, session);
}
%>
<%!
private void removeCurrentValues(Prompt objPrompt)
{
ValueFromLov[] valueFromLovArray = objPrompt.getCurrentValuesFromLov();
if (objPrompt.getCurrentValuesFromLov() != null && objPrompt.getCurrentValuesFromLov().length > 0)
{
objPrompt.removeValuesFromLov(valueFromLovArray);
}
}
%>

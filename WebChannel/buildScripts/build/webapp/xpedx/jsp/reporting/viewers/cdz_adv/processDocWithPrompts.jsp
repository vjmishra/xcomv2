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
String strTarget = "";
try
{
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
strTarget = requestWrapper.getQueryParameter("sTarget", false, "");
String strNaii = requestWrapper.getQueryParameter("NAII", false, "");
String strNotAnsweredPrompts = "";
DocumentInstance doc = null;
String strEntry = requestWrapper.getQueryParameter("sEntry", false, "");
if (strEntry.equals(""))
{
String strDocID = requestWrapper.getQueryParameter("id", false, "");
if (strDocID.equals(""))
strDocID = requestWrapper.getQueryParameter("docid", true);
String strKind = requestWrapper.getQueryParameter("kind");
ReportEngine objReportEngine = null;
if (strKind.equalsIgnoreCase("webi"))
objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
else
objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.FC_REPORT_ENGINE);
objReportEngine.setLocale(requestWrapper.getUserLocale());
doc = objReportEngine.openDocument(Integer.parseInt(strDocID));
String strRefreshOnOpen = doc.getProperties().getProperty(PropertiesType.REFRESH_ON_OPEN, "false");
if (strRefreshOnOpen.equals("false"))
doc.refresh();
}
else
{
doc = reportEngines.getDocumentFromStorageToken(strEntry);
doc.refresh();
}
if (doc.getMustFillContexts())
{
String strParamValue = requestWrapper.getQueryParameter("lsC");
if (strParamValue != null)
{
Contexts arrContexts = doc.getContextPrompts();
Context objContext = arrContexts.getItem(0);
String[] arrSelContexts = new String[0];
arrSelContexts = ViewerTools.split(strParamValue, ";");
objContext.enterValues(arrSelContexts);
doc.setContexts();
}
}
boolean blnSetPrompts = true;
if (doc.getMustFillPrompts())
{
Prompts arrPrompts = doc.getPrompts();
int iNbPrompts = arrPrompts.getCount();
for (int i=1; i<=iNbPrompts; i++)
{
Prompt objPrompt = arrPrompts.getItem(i-1);
String strParamName = objPrompt.getID();
String strRefreshLOV = requestWrapper.getQueryParameter("lsCBR" + strParamName, false, "");
if (strRefreshLOV.equals("Y"))
{
if (objPrompt.hasLOV())
{
Lov objLOV = objPrompt.getLOV();
objLOV.refresh();
}
}
String strValueType = "lsS";
String strParamValue = requestWrapper.getQueryParameter(strValueType + strParamName);
if (strParamValue == null)
{
strValueType = "lsM";
strParamValue = requestWrapper.getQueryParameter(strValueType + strParamName);
}
String[] arrPromptValues = null;
String[] arrPromptIndexes = null;
if (strParamValue != null)
{
String strParamValueIndex = requestWrapper.getQueryParameter("lsI" + strParamName, false, "");
strParamValue = java.net.URLDecoder.decode(strParamValue, "UTF-8");
if (strParamValue.equals("?"))
{
blnSetPrompts = false;
if (!strNotAnsweredPrompts.equals("")) strNotAnsweredPrompts += "-";
strNotAnsweredPrompts += Integer.toString(i);
continue;
}
if (objPrompt.getType() == PromptType.Mono || strValueType.equals("lsS"))
{
arrPromptValues = new String[1];
arrPromptValues[0] = strParamValue;
if (!strParamValueIndex.equals(""))
{
arrPromptIndexes = new String[1];
arrPromptIndexes[0] = strParamValueIndex;
}
else
arrPromptIndexes = new String[0];
}
else
{
arrPromptValues = ViewerTools.split(strParamValue, ";");
if (!strParamValueIndex.equals(""))
arrPromptIndexes = ViewerTools.split(strParamValueIndex, ";");
else
arrPromptIndexes = new String[0];
}
ValueFromLov[] valueFromLovArray = new ValueFromLov[arrPromptValues.length];
for (int j=0; j<arrPromptValues.length; j++)
{
ValueFromLov objValueFromLov = null;
if (arrPromptIndexes.length > j)
objValueFromLov = new ValueFromLov(arrPromptIndexes[j], arrPromptValues[j]);
else
objValueFromLov = new ValueFromLov("", arrPromptValues[j]);
valueFromLovArray[j] = objValueFromLov;
}
objPrompt.enterValues(valueFromLovArray);
}
else if (!objPrompt.isOptional())
{
if (strNaii.equals("Y"))
{
if (!strNotAnsweredPrompts.equals("")) strNotAnsweredPrompts += "-";
strNotAnsweredPrompts += Integer.toString(i);
blnSetPrompts = false;
}
else
{
ValueFromLov[] arrTemp = null;
if (objPrompt.getCurrentValuesFromLov() != null && objPrompt.getCurrentValuesFromLov().length > 0)
arrTemp = objPrompt.getCurrentValuesFromLov();
else if (objPrompt.getPreviousValuesFromLov() != null && objPrompt.getPreviousValuesFromLov().length > 0)
arrTemp = objPrompt.getPreviousValuesFromLov();
else if (objPrompt.getDefaultValuesFromLov() != null && objPrompt.getDefaultValuesFromLov().length > 0)
arrTemp = objPrompt.getDefaultValuesFromLov();
if (arrTemp != null)
objPrompt.enterValues(arrTemp);
else
blnSetPrompts = false;
}
}
}
if (blnSetPrompts)
{
doc.setPrompts();
}
}
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
requestWrapper.removeQueryParameter("sRefresh");
if (!strTarget.equals(""))
{
    strTarget += ".jsp";
requestWrapper.removeQueryParameter("sTarget");
}
else
strTarget = (blnSetPrompts)? "report.jsp" : "getPrompts.jsp";
if (blnSetPrompts)
{
requestWrapper.setQueryParameter("sSetPrompts", "true");
}
else
{
requestWrapper.setQueryParameter("sNotSetPrompts", strNotAnsweredPrompts);
if (strNaii.equals(""))
requestWrapper.setQueryParameter("NAII", "Y");
}
out.clearBuffer();
%>
<jsp:forward page="<%=strTarget%>"/>
<%
}
catch(Exception e)
{
if (strTarget.equals("viewPart"))
{
String strMsg2Display = "Internal error occured: " + e.getMessage();
out.println("<html><body><p>" + strMsg2Display + "</p></body></html>");
}
else
objUtils.displayErrorMsg(e, "_ERR_PROMPTS", true, out, session);
}
%>

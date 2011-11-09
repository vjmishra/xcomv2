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
<jsp:useBean id="objPromptsBean" class="com.businessobjects.adv_ivcdzview.Prompts" scope="page" />
<%
response.setDateHeader("expires", 0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="includes/Utils.js"></script>
<script language="javascript">
<%
if (!isAlive)
{
out.println("parent.parent.alertSessionInvalid();");
out.println("</script></head><body></body></html>");
return;
}
try
{
_logger.info("--> updateQuickPrompts.jsp");
objPromptsBean.onStart(requestWrapper);
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("sEntry IN: " + strEntry);
String strReloadLov = requestWrapper.getQueryParameter("sReloadLOV", true);
String strReloadPrompts = requestWrapper.getQueryParameter("sReloadPrompts", false, "");
String[] arrReloadPrompts = (strReloadPrompts.equals(""))? new String[0]:ViewerTools.split(strReloadPrompts, ";");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
Prompts arrPrompts = doc.getPrompts();
int iNbPrompts = arrPrompts.getCount();
String strDocID = doc.getProperties().getProperty(PropertiesType.DOCUMENT_ID);
String strUseLov = objUserSettings.getUserDocRight("USE_LOV", strDocID);
out.println("strReloadLov=\"" + strReloadLov + "\";");
out.println("iNbPrompts=" + Integer.toString(iNbPrompts) + ";");
out.println("arrPrompts=new Array();");
out.println("arrPromptValues=new Array();");
out.println("arrNbLovItems=new Array();");
out.println("arrSelectedValues=new Array();");
out.println("arrLovTexts=new Array();");
out.println("arrLovValues=new Array();");
out.println("arrLovIndexes=new Array();");
out.println("arrNbBatchLovItems=new Array();");
out.println("arrLovTitle=new Array();");
out.println("iLovSortType=new Array();");
out.println("iLovSortedColumnIndex=new Array();");
for (int i=1; i<=iNbPrompts; i++)
{
Prompt objPrompt = arrPrompts.getItem(i-1);
String strIndex = Integer.toString(i-1);
String strParamValue = "";
ValueFromLov[] arrTemp = null;
if (objPrompt.getCurrentValuesFromLov() != null && objPrompt.getCurrentValuesFromLov().length > 0)
{
arrTemp = objPrompt.getCurrentValuesFromLov();
}
else if (objPrompt.getPreviousValuesFromLov() != null && objPrompt.getPreviousValuesFromLov().length > 0)
{
arrTemp = objPrompt.getPreviousValuesFromLov();
}
if (arrTemp != null)
{
for (int j=0; j<arrTemp.length; j++)
{
String value = arrTemp[j].getValue();
if (value == null) value = "";
String index = arrTemp[j].getRowIndex();
if (index == null) index = "";
if (j > 0) strParamValue += ";";
String strValueIndex = ViewerTools.encodeIndex(index);
if (strValueIndex == null) strValueIndex = "";
strParamValue += strValueIndex + "_" + value;
}
}
Lov objLOV = null;
boolean blnLOV = false;
if (strUseLov.equals("full") && strReloadLov.equals("yes"))
{
blnLOV = objPrompt.hasLOV();
if (blnLOV)
{
try
{
objLOV = objPrompt.getLOV();
}
catch(Exception e)
{
_logger.error("Internal error: Cannot get LOV object");
e.printStackTrace();
blnLOV = false;
}
}
}
strParamValue = ViewerTools.escapeQuotes(strParamValue);
out.println("arrPromptValues[" + strIndex + "]=parent.encodeEmptyValue(\"" + strParamValue + "\",true);");
out.println("arrLovTexts[" + strIndex + "]=new Array();");
out.println("arrLovValues[" + strIndex + "]=new Array();");
out.println("arrLovIndexes[" + strIndex + "]=new Array();");
out.println("p=arrPrompts[" + strIndex + "]=new Array(13);");
out.println("p[7]=false;");
boolean blnReloadLOV = false;
for (int j=0; j<arrReloadPrompts.length; j++)
{
if (Integer.parseInt(arrReloadPrompts[j]) == (i-1))
{
blnReloadLOV = true;
break;
}
}
if (blnReloadLOV & blnLOV && objPrompt.isConstrained())
{
String[] arrBatchNames = objLOV.getBatchNames();
if (arrBatchNames != null)
out.println("arrNbBatchLovItems[" + strIndex + "]=" + Integer.toString(objLOV.getBatchNames().length) + ";");
boolean blnHasLovItems = objPromptsBean.getLovValues(i, objLOV, out);
if (!blnHasLovItems)
out.println("arrNbLovItems[" + strIndex + "]=0;");
}
else
{
out.println("arrNbBatchLovItems[" + strIndex + "]=0;");
out.println("arrNbLovItems[" + strIndex + "]=0;");
out.println("arrLovTitle[" + strIndex + "]=\"\";");
}
}
%>
if (parent.updatePrompts)
{
parent.updatePrompts(self);
}
</script>
</head>
<body></body>
</html>
<%
arrPrompts = null;
doc = null;
_logger.info("<-- updateQuickPrompts.jsp");
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_QUERY_QUICK_FILTER", true, out, session);
}
%>

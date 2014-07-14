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
<jsp:useBean id="objPromptsBean" class="com.businessobjects.adv_ivcdzview.Prompts" scope="page">
<%
objPromptsBean.onStart(requestWrapper);
%>
</jsp:useBean>
<%
response.setDateHeader("expires", 0);
if (!isAlive)
{
objUtils.invalidSessionDialog(out);
return;
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript">
var par=parent;
_emptyValueLab=par._emptyValueLab;
<%
try
{
_logger.info("--> processRefreshLOV.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strSrcLovID = requestWrapper.getQueryParameter("sLovID", true);
String strAction = requestWrapper.getQueryParameter("sAction", true);
String strGlobalIndex = requestWrapper.getQueryParameter("gIndex", true);
int iPrompt = 0;
String strPromptIndex = requestWrapper.getQueryParameter("iPrompt", false);
if (strPromptIndex!=null)
iPrompt = Integer.parseInt(strPromptIndex);
String strParamName = requestWrapper.getQueryParameter("sParamName", true);
String strSetNP = requestWrapper.getQueryParameter("setNP", false, "no");
String strLovType = requestWrapper.getQueryParameter("sLovType", false, "");
LovType lovType = null;
if (strLovType.equals("DataSource"))
lovType = LovType.DATA_SOURCE;
else if (strLovType.equals("Cube"))
lovType = LovType.CUBE;
else
lovType = LovType.LOV_OBJECT;
String strSearchSensitive = requestWrapper.getQueryParameter("sCaseSensitive", false, "no");
boolean bMatchCase=false;
if ( strSearchSensitive.equals("yes") )
bMatchCase=true;
String strEmptyLab = requestWrapper.getQueryParameter("sEmptyLab",false,"");
objPromptsBean.strEmptyLab=strEmptyLab;
String strAdvPrompts = requestWrapper.getQueryParameter("advPrompts", false, "no");
DocumentInstance doc = null;
if (strAdvPrompts.equals("yes"))
{
doc = (DocumentInstance)session.getAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"));
}
if (doc == null)
{
doc = reportEngines.getDocumentFromStorageToken(strEntry);
}
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "");
DataProvider dp = null;
if (lovType != LovType.CUBE)
{
if(!strDPIndex.equals(""))
{
dp=doc.getDataProviders().getItem(Integer.parseInt(strDPIndex));
}
else if (iPrompt > 0)
{
int iPromptIndex = iPrompt;
String strNestedPromptParam = requestWrapper.getQueryParameter("sNested", false, "");
if (!strNestedPromptParam.equals(""))
{
String[] arrIndexes = ViewerTools.split(strNestedPromptParam, ".");
iPromptIndex = Integer.parseInt(arrIndexes[0]);
}
Prompts prompts=doc.getPrompts();
if (prompts.getCount() > 0)
{
Prompt prompt = prompts.getItem(iPromptIndex - 1);
DataProvider[] arrDp = prompt.getDataProviders();
if (arrDp != null && arrDp.length > 0)
dp = prompt.getDataProviders()[0];
}
}
}
Lov objLOV = doc.getLOV(strSrcLovID, lovType, dp);
out.println("arrPrompts=new Array(1);");
out.println("arrNbLovItems=new Array(1);");
out.println("arrSelectedValues=new Array(1);");
out.println("arrLovTexts=new Array(1);");
out.println("arrLovValues=new Array(1);");
out.println("arrLovIndexes=new Array(1);");
out.println("arrNbBatchLovItems=new Array(1);");
out.println("arrBatchLovTexts=new Array(1);");
out.println("arrBatchLovSelValue=new Array(1);");
out.println("arrLovTitle=new Array();");
out.println("iLovSortType=new Array();");
out.println("iLovSortedColumnIndex=new Array();");
String strTxtSearch = requestWrapper.getQueryParameter("L" + strParamName + "_searchVal", false, "");
String mustFillNestedPrompts = "false";
_logger.info("Action: " + strAction);
if (!strAction.equals("B"))
{
if (strAction.equals("R"))
{
if (strSetNP.equals("yes"))
objLOV = objPromptsBean.setNestedPrompts(objLOV,iPrompt);
else
{
objLOV.refresh();
if (objLOV.mustFillNestedPrompts())
mustFillNestedPrompts = "true";
}
}
else if (strAction.equals("F"))
{
if (strSetNP.equals("yes"))
objLOV = objPromptsBean.setNestedPrompts(objLOV,iPrompt);
if (objLOV.mustFillNestedPrompts())
mustFillNestedPrompts = "true";
}
}
if (strAction.equals("R"))
strTxtSearch="";
if (!strTxtSearch.equals(""))
objLOV.setSearchMode(true);
else
objLOV.setSearchMode(false);
objLOV.setSearchPattern(strTxtSearch);
objLOV.setSearchMatchCase( bMatchCase );
String strColIndex = requestWrapper.getQueryParameter("colIndex", false, "");
if (!strColIndex.equals(""))
{
String strSortType = requestWrapper.getQueryParameter("sortType", true);
int iSortType = Integer.parseInt(strSortType);
objLOV.setSortedColumnIndex(Integer.parseInt(strColIndex));
switch(iSortType)
{
case -1:
objLOV.setSortType(SortType.NONE);
break;
case 0:
objLOV.setSortType(SortType.ASCENDING);
break;
case 1:
objLOV.setSortType(SortType.DESCENDING);
break;
}
}
out.println("p=arrPrompts[0]=new Array(19);");
out.println("p[7]=false;");
if (!strAction.equals("B"))
{
boolean blnHasBatchList = objPromptsBean.getBatchLOV(1, objLOV, out);
if (!blnHasBatchList)
out.println("arrNbBatchLovItems[0]=0;");
}
boolean blnHasLovItems = objPromptsBean.getLovValues(1, objLOV, out);
if (!blnHasLovItems)
out.println("arrNbLovItems[0]=0;");
out.println("p[18]=" + ((objLOV.isPartialResult())?"true":"false") + ";");
%>
</script>
<script language="javascript">
par.strEntry="<%=strEntry%>";
var strAction="<%=strAction%>";
var bNestedPrompt=<%=mustFillNestedPrompts%>;
function reloadLOV(gIndex)
{
if (par.updateLovAfterSubmitAction)
{
par.updateLovAfterSubmitAction(gIndex,self);
}
else if (par.updateReportFltLOV)
{
par.updateReportFltLOV(strAction, self);
}
else if (par.updateQueryFltLOV)
{
if (bNestedPrompt)
par.frameReload(par)
else
par.updateQueryFltLOV(gIndex,self);
par.hideBlockWhileWaitWidget();
}
else if (par.advPromptsFrame)
{
par=par.advPromptsFrame;
if (par.updateLovAfterSubmitAction)
par.updateLovAfterSubmitAction(gIndex,self);
}
else
par.hideBlockWhileWaitWidget();
}
</script>
</head>
<body onLoad="reloadLOV(<%=strGlobalIndex%>)">
</body>
</html>
<%
_logger.info("<-- processRefreshLOV.jsp");
}
catch(Exception e)
{
out.println("if (typeof(parent.displayWaitCursor)!=\"undefined\")");
out.println("    parent.displayWaitCursor.show(false);");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_LOV", false, out, session);
}
%>

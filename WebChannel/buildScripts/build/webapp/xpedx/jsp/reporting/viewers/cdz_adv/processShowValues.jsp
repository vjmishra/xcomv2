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
_emptyValueLab=parent._emptyValueLab;
<%
try
{
_logger.info("--> processShowValues.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strSrcLovID = requestWrapper.getQueryParameter("sLovID", true);
String strGlobalIndex = requestWrapper.getQueryParameter("gIndex", true);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
Lov objLOV = doc.getLOV(strSrcLovID, LovType.LOV_OBJECT);
out.println("arrPrompts=new Array(1);");
out.println("arrNbLovItems=new Array(1);");
out.println("arrSelectedValues=new Array(1);");
out.println("arrLovTexts=new Array(1);");
out.println("arrLovValues=new Array(1);");
out.println("arrLovIndexes=new Array(1);");
out.println("arrNbBatchLovItems=new Array(1);");
out.println("arrBatchLovTexts=new Array(1);");
out.println("arrBatchLovSelValue=new Array(1);");
out.println("arrLovTitle=new Array(1);");
out.println("iLovSortType=new Array(1);");
out.println("iLovSortedColumnIndex=new Array(1);");
out.println("p=arrPrompts[0]=new Array(13);");
out.println("p[7]=false;");
String mustFillNestedPrompts = (objLOV.mustFillNestedPrompts())? "true" : "false";
if (mustFillNestedPrompts.equals("false"))
{
boolean blnHasBatchList = objPromptsBean.getBatchLOV(1, objLOV, out);
if (!blnHasBatchList)
out.println("arrNbBatchLovItems[0]=0;");
boolean blnHasLovItems = objPromptsBean.getLovValues(1, objLOV, out);
if (!blnHasLovItems)
out.println("arrNbLovItems[0]=0;");
}
%>
</script>
<script language="javascript">
var p=parent;
var hasNestedPrompts=<%=mustFillNestedPrompts%>;
function reloadLOV(gIndex)
{
if (p.updatePromptLov)
{
p.updatePromptLov(gIndex,self);
}
}
</script>
</head>
<body onLoad="reloadLOV(<%=strGlobalIndex%>)">
</body>
</html>
<%
_logger.info("<-- processShowValues.jsp");
}
catch(Exception e)
{
out.println("if (typeof(parent.displayWaitCursor)!=\"undefined\")");
out.println("    parent.displayWaitCursor.show(false);");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_LOV", false, out, session);
}
%>
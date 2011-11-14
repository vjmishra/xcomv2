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
<%
Properties customProperties = (Properties) session.getAttribute("BO_CUSTOM_PROPS");
String useCustomPrompts = customProperties.getProperty("useCustomPrompts");
if (useCustomPrompts.equals("Y"))
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String callbackQS = ViewerTools.removeQueryParameter(requestWrapper.getQueryString(),"sEntry");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="scripts/Utils.js"></script>
<script language="javascript">
function goCustomPrompts()
{
var viewerFrame = getTopViewerFrameset();
if ((viewerFrame != null) && (typeof(viewerFrame.parent.goPrompts) != "undefined"))
{
var callbackScript="viewers/cdz_adv/viewDocument.jsp?";
var callbackQS="<%=callbackQS%>";
viewerFrame.parent.goPrompts('<%=strEntry%>',callbackScript,callbackQS);
}
}
</script>
</head>
<body onload="goCustomPrompts()">
</body>
</html>
<%
return;
}
%>
<html>
<head>
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript" src="lib/dialog.js"></script>
<script language="javascript" src="lib/treeview.js"></script>
<script language="javascript" src="language/<%=(String)session.getAttribute("CDZ.Language")%>/scripts/contexts.js"></script>
<script language="javascript">
topfs=getTopFrameset();
if(topfs == null) topfs=parent;
initDom(topfs._skin, topfs._lang, topfs, "contextDlg");
<%
String strSRC = "";
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strDPName = requestWrapper.getQueryParameter("sDPName", false, "");
String strViewSQL = requestWrapper.getQueryParameter("bViewSQL", false, "false");
boolean bViewSQL =strViewSQL.equals("true");
String strAction = requestWrapper.getQueryParameter("iAction", false, "");
String strNEV = requestWrapper.getQueryParameter("sNEV", false, "no");
String strNewDoc = requestWrapper.getQueryParameter("sNewDoc", false, "false");
String strApplyFormat = requestWrapper.getQueryParameter("sApplyFormat", false, ""); 
String strViewType = requestWrapper.getQueryParameter("viewType", false, "");
String strCancel = requestWrapper.getQueryParameter("sCancel", false, "");
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "");
String strSaveQuery = requestWrapper.getQueryParameter("bSaveQuery", false, "false");
strSRC = requestWrapper.getQueryParameter("src", false, "");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
if (doc.getMustFillContexts())
{
Contexts contexts = doc.getContextPrompts();
Context context;
DataProvider[] dps;
String dpName;
int cpt=0, count = contexts.getCount();
Properties props = doc.getProperties();
String docName = props.getProperty( PropertiesType.NAME);
out.println("_docName='"+ViewerTools.escapeQuotes(docName)+"'");
out.println("_contexts.length=0;");
for (int i = 0; i < count; ++i)
{
context = contexts.getItem(i);
if (context.requireAnswer())
{
dps =context.getDataProviders();
dpName=dps[0].getName();
if(!bViewSQL || (bViewSQL && dpName.equals(strDPName)))
{
out.println("ctx=newBOContext('"+ViewerTools.escapeQuotes(dpName)+"',"+context.getType().value()+");");
if (context.getType() == PromptType.Multi)
out.println("ctx.isMulti=true;");
String[] allValues =context.getAllValues();
for (int j = 0; j < allValues.length ; j++)
{
out.println("ctx.addValue('"+ViewerTools.escapeQuotes(allValues[j])+"');");
out.println("ctx.addDescr('"+ViewerTools.escapeQuotes(context.getDescription(j))+"');");
}
String[] previousValues =context.getPreviousValues();
for (int j = 0; j < previousValues.length ; j++)
out.println("ctx.selectValue('"+ViewerTools.escapeQuotes(previousValues[j])+"');");
out.println("_contexts["+cpt+"]=ctx;");
cpt++;
}
}
}
}
%>
function fillContextsForm()
{
var len = _contexts.length
with(self.document.contextsForm)
{
for(var i=0;i<len;i++)
{
elements[i].value=_contexts[i].selectedvalues.join(';')
}
}
}
function createContextsForm()
{
var s=''
s='<form style="display:none" name="contextsForm" target="_self" method="post" action="">'
var len = _contexts.length
for(var i=0;i<len;i++)
{
s+='<input type="hidden" name="context'+i+'" id="context'+i+'" value="">'
}
s+='</form>'
document.write(s)
}
function loadCB()
{
setTimeout('loadDialog()',1)
}
var strEntry = "<%=strEntry%>";
var strNEV = "<%=strNEV%>";
var strViewSQL = "<%=strViewSQL%>";
var strNewDoc = "<%=strNewDoc%>";
var strApplyFormat = "<%=strApplyFormat%>";
var strDPName = "<%=ViewerTools.escapeQuotes(strDPName)%>";
var strViewType = "<%=strViewType%>";
var strCancel = "<%=strCancel%>";
var strDPIndex = "<%=strDPIndex%>";
var strSaveQuery = "<%=strSaveQuery%>";
var strSRC = "<%=strSRC%>";
var strAction = "<%=strAction%>";
</script>
</head>
<body onload="loadCB()">
<!-- Hidden form -->
<script language="javascript">createContextsForm()</script>
</body>
</html>
<%
}
catch(Exception e)
{
if (strSRC.equals("RPV"))
{
out.println("function okCB() {parent.goBackDashboard();}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_CONTEXTS", false, "okCB", out, session);
}
else
{
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_CONTEXTS", false, out, session);
}
}
%>
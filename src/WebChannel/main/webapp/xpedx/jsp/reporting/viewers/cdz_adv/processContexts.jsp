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
<jsp:useBean id="objApplyQueries" class="com.businessobjects.adv_ivcdzview.ApplyQueries" scope="page" />
<%
response.setDateHeader("expires", 0);
if (!isAlive)
{
objUtils.invalidSessionDialog(out);
return;
}
String strSRC = "";
String strCancel = "";
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strDPName = requestWrapper.getQueryParameter("sDPName", false, "");
String strViewSQL = requestWrapper.getQueryParameter("bViewSQL", false, "false");
boolean bViewSQL = strViewSQL.equals("true");
String strAction = requestWrapper.getQueryParameter("iAction", false, "");
boolean bChangeSrc = strAction.equals("3");
String strNEV = requestWrapper.getQueryParameter("sNEV", false, "no");
String strNewDoc = requestWrapper.getQueryParameter("sNewDoc", false, "false");
String strCreateDefaultReportBody = requestWrapper.getQueryParameter("bCreateDefaultReportBody", false, "false");
boolean bCreateDefaultReportBody = Boolean.valueOf(strCreateDefaultReportBody).booleanValue();
_logger.info("strCreateDefaultReportBody="+strCreateDefaultReportBody);
String strApplyFormat = requestWrapper.getQueryParameter("sApplyFormat", false, ""); 
strCancel = requestWrapper.getQueryParameter("sCancel", false, "");
String strSaveQuery = requestWrapper.getQueryParameter("bSaveQuery", false, "false");
boolean bSaveQuery = Boolean.valueOf(strSaveQuery).booleanValue();
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "");
strSRC = requestWrapper.getQueryParameter("src", false, "");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
String strKey = "CDZ." + strViewerID + ".DocInstance";
session.setAttribute(strKey, doc);
Contexts arrContexts = doc.getContextPrompts();
int iNbContexts = arrContexts.getCount(), cpt=0;
Context context;
DataProvider[] contextOfDP;
String dpName,strParamName,strParamValue;
for (int i=0; i<iNbContexts; i++)
{
context = arrContexts.getItem(i);
if (context.requireAnswer())
{
contextOfDP =context.getDataProviders();
dpName=contextOfDP[0].getName();
if(!bViewSQL || (bViewSQL && dpName.equals(strDPName)))
{
strParamName = "context" + Integer.toString(cpt); cpt++;
strParamValue = requestWrapper.getQueryParameter(strParamName);
if (strParamValue != null)
{
String[] arrSelContexts = ViewerTools.split(strParamValue, ";");
context.enterValues(arrSelContexts);
}
}
}
}
doc.setContexts();
if (bSaveQuery)
{
if(bCreateDefaultReportBody)
{
String sDefaultRepTitle= requestWrapper.getQueryParameter("defaultRepTitle",false,"");
objApplyQueries.createDefaultReportBody(doc,-1, sDefaultRepTitle);
_logger.info("createDefaultReportBody with title=" + sDefaultRepTitle);
doc.applyFormat();
}
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
%>
<html>
<head>
<script language="javascript">
function loadCB()
{
parent.changeEntry("<%=strEntry %>");
parent.saveAs(true); 
}
</script>
</head>
<body onload="loadCB()">
</body>
</html>
<%
} 
else if(bViewSQL) 
{
DataProviders dps = doc.getDataProviders();
DataProvider  dp = dps.getItem(strDPName);
if (!doc.getMustFillContexts())
{
%>
<html>
<head>
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript" src="lib/dialog.js"></script>
<script language="javascript" src="lib/treeview.js"></script>
<script language="javascript" src="language/<%=(String)session.getAttribute("CDZ.Language")%>/scripts/sqlViewer.js"></script>
<script language="javascript">
initDom(parent._skin, parent._lang, parent, "sqlViewerDlg");
<%
if((dp!=null) && (dp instanceof SQLDataProvider) )
{
objApplyQueries.printIterSQL(out,((SQLDataProvider)dp).getSQLContainer(),0);
out.println("isCustomSQL=" + (((SQLDataProvider)dp).isCustomSQL() ? true : false) + ";");
out.println("if(curDP) curDP.isCustomSQL=isCustomSQL;");
out.println("dpName='"+ViewerTools.escapeQuotes(strDPName)+"';");
}
strEntry = doc.getStorageToken();
out.println("updateAfterViewSQL('"+strEntry+"');");
%>
</script>
</head>
<body onload="loadDialog()">
<script language="javascript">writeBody()</script>
</body>
</html>
<%
}
}
else if (bChangeSrc) 
{
strEntry = doc.getStorageToken();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script language="javascript" src="language/<%=(String)session.getAttribute("CDZ.Language")%>/scripts/dataSourceMapping.js"></script>
<script language="javascript">
<%
out.println("strDPIndex = "+strDPIndex +";");
int iDPIndex = -1;
if(!strDPIndex.equals(""))
iDPIndex =  Integer.parseInt(strDPIndex);
objApplyQueries.printContexts(out,doc,iDPIndex);
%>
function loadCB()
{
var f=parent.getTopFrameset()
parent.changeSrcDlg.show(false);
if (f)
f=f.getFrame("DlgFrame")
if (f)
f.updateQueryPropertiesDialog(1,'<%=strEntry%>',null,arrContexts)
}
</script>
</head>
<body>
</body>
<script language="javascript">setTimeout("loadCB()",1)</script>
</html>
<%
}
else 
{
if (doc.getMustFillPrompts())
{
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
out.clearBuffer();
%>
<jsp:forward page="getPrompts.jsp"/>
<%
}
else if (strSRC.equals("RPV"))
{
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
out.println("<html><head><script language=\"javascript\">");
out.println("parent.hideWaitDlg();");
out.println("parent.goReportPartViewer(\"" + strEntry + "\");");
out.println("</script></head><body></body></html>");
}
else
{
if(bCreateDefaultReportBody)
{
_logger.info("bCreateDefaultReportBody=true");
int indexDP = -1;
if(!strDPIndex.equals(""))
indexDP =  Integer.parseInt(strDPIndex);
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
String strQueryString = requestWrapper.getQueryString();
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
out.println("<html><head><script language=\"javascript\">");
out.println("parent.bCreateDefaultReportBody=" + bCreateDefaultReportBody + ";");
if (strNEV.equals("yes"))
out.println("parent.Report.location.replace(\"report.jsp?" + ViewerTools.escapeQuotes(strQueryString) + "\");");
else
out.println("parent.Report.location=\"report.jsp?" + ViewerTools.escapeQuotes(strQueryString) + "\";");
out.println("</script></head><body></body></html>");
}
}
}
catch(Exception e)
{
if (strSRC.equals("RPV"))
{
out.println("function okCB() {parent.goBackDashboard();}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_CONTEXTS", false, "okCB", out, session);
}
else if (!strCancel.equals(""))
{
out.println("<html><head><script language=\"javascript\">");
out.println("var previousURL=parent.allUseDictionary.get(\"" + strCancel + "\");");
out.println("function okCB() {parent.frameNav(\"Report\",previousURL);}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_CONTEXTS", false, "okCB",out, session);
}
else
objUtils.displayErrorMsg(e, "_ERR_CONTEXTS", true, out, session);
}
%>

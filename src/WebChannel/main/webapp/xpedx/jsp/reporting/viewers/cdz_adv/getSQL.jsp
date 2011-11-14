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
<jsp:useBean id="objApplyQueries" class="com.businessobjects.adv_ivcdzview.ApplyQueries" scope="page" />
<%
response.setDateHeader("expires", 0);
try
{
_logger.info("-->getSQL.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strWOMqueries = requestWrapper.getQueryParameter("WOMqueries", false, "");
_logger.info(strWOMqueries);
String strDPName = requestWrapper.getQueryParameter("sDPName", false, "");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
DataProviders dps = doc.getDataProviders();
DataProvider dp= dps.getItem(strDPName);
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
boolean bModified = objApplyQueries.applyModifications(strWOMqueries, dps);
_logger.info(strDPName + " is modified ?"+bModified);
if((dp!=null) && (dp instanceof SQLDataProvider) )
{
objApplyQueries.printIterSQL(out,((SQLDataProvider)dp).getSQLContainer(),0);
out.println("isCustomSQL=" + (((SQLDataProvider)dp).isCustomSQL() ? true : false) + ";");
out.println("if(curDP) curDP.isCustomSQL=isCustomSQL;");
out.println("dpName='"+ViewerTools.escapeQuotes(strDPName)+"';");
}
if(bModified)
{
strEntry = doc.getStorageToken();
out.println("updateAfterViewSQL('"+strEntry+"');");
}
if(doc.getMustFillContexts())
{
requestWrapper.setQueryParameter("sEntry", strEntry);
requestWrapper.setQueryParameter("sDPName", strDPName);
requestWrapper.setQueryParameter("bViewSQL", "true");
out.clearBuffer();
%>
<jsp:forward page="getContextsInfos.jsp"/>
<%
}
%>
</script>
</head>
<body onload="loadDialog()">
<script language="javascript">writeBody()</script>
</body>
</html>
<%
_logger.info("<-- getSQL.jsp");
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_VIEW_SQL", true, out, session);
}
%>

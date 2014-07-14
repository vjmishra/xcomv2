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
_logger.info("---> processRunQuery");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strWOMqueries = requestWrapper.getQueryParameter("WOMqueries", false, "");
String strAllUserEditQuery = requestWrapper.getQueryParameter("sEditQuery", false, "");
String strNewDoc = requestWrapper.getQueryParameter("sNewDoc", false, "");
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, ""); 
String strNoData = requestWrapper.getQueryParameter("bNoData", false, "false");
String strCreateDefaultReportBody = requestWrapper.getQueryParameter("bCreateDefaultReportBody", false, "false");
boolean bCreateDefaultReportBody = Boolean.valueOf(strCreateDefaultReportBody).booleanValue();
_logger.info("strCreateDefaultReportBody="+strCreateDefaultReportBody);
boolean bCancel=false; 
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
DataProviders dps = doc.getDataProviders();
DataProvider dp = null;
int indexDP = -1;
boolean blnNeedAnswerCP = false;
boolean bModified = false;
String strRedirect = "";
String strQueryString="";
String strAction = requestWrapper.getQueryParameter("sAction", false, "");
String strPromptKeydateOnRefresh = doc.getProperties().getProperty(PropertiesType.PROMPT_KEYDATE_ONREFRESH, "");
    String strRunAction = "RunQuery";
String strKey = "CDZ." + strViewerID + ".DocInstance";
session.setAttribute(strKey, doc);
bModified = objApplyQueries.applyModifications(strWOMqueries, dps);
if (strPromptKeydateOnRefresh.equals("true") && !strAction.equals(strRunAction))
{
strRedirect = "getKeydateProperties.jsp";
requestWrapper.setQueryParameter("sAction", strRunAction);
}
else
{
if(!strAllUserEditQuery.equals(""))
{
Properties props = new Properties();
props.setProperty("enablealternateusertoeditdoc", strAllUserEditQuery);
doc.setProperties(props);
}
if(strNoData.equals("true"))
{
dps.generateQueries();
}
else if(strDPIndex.equals("")) 
{
dps.runQueries();
}
else
{
indexDP = Integer.parseInt(strDPIndex);
_logger.info("run query index:" + indexDP);
dp= dps.getItem(indexDP);
if(dp!=null) dp.runQuery();
}
if (doc.getMustFillContexts())
{
strRedirect = "getContextsInfos.jsp";
blnNeedAnswerCP=true;
}
else if (doc.getMustFillPrompts())
{
strRedirect = "getPrompts.jsp";
blnNeedAnswerCP=true;
}
}
if (!strRedirect.equals(""))
{
requestWrapper.setQueryParameter("sApplyFormat", "true");
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
_logger.info("<--- processRunQuery");
out.clearBuffer();
%>
<jsp:forward page="<%=strRedirect%>"/>
<%
}
else
{
_logger.info("newDoc" + strNewDoc);
_logger.info("bModified" + bModified);
    ViewerInstance vi= instanceManager.getViewerInstance(Integer.parseInt(strViewerID));
    String action = vi.getActionState();    
    if(action!=null  && action.equals("cancel"))
        bCancel= true;
    if(!bCancel)    
    {
    if (bCreateDefaultReportBody)
    {
    String sDefaultRepTitle= requestWrapper.getQueryParameter("defaultRepTitle",false,"");
    objApplyQueries.createDefaultReportBody(doc,indexDP,sDefaultRepTitle);
    _logger.info("createDefaultReportBody with title=" + sDefaultRepTitle);
    bCreateDefaultReportBody = false;
    }
    doc.applyFormat();
    strEntry = doc.getStorageToken();
    objUtils.setSessionStorageToken(strEntry, strViewerID, session);
    strQueryString = requestWrapper.getQueryString();
    strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
}
_logger.info("<--- processRunQuery");
}
%>
<html>
<head>
<script language="javascript">
function loadCB()
{
    var bCancel = <%=(bCancel)?"true":"false"%> 
    if(bCancel) 
        return;
    var bModified = <%=(bModified)?"true":"false"%>
    if(bModified)
    {
    parent.changeEntry("<%=strEntry%>")
    parent.blnNeedAnswerCP =<%=(blnNeedAnswerCP)?"true":"false"%>; 
    }
    parent.bCreateDefaultReportBody = <%=bCreateDefaultReportBody%>;
    parent.Report.location="report.jsp?"+"<%=ViewerTools.escapeQuotes(strQueryString)%>"
}
</script>
</head>
<body onload="loadCB()">
</body>
</html>
<%
}
catch(Exception e) {
objUtils.displayErrorMsg(e, "_ERR_RUN_QUERY", true, out, session);
}
%>
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
<jsp:useBean id="oDS" class="com.businessobjects.adv_ivcdzview.DataSourceTools" scope="page" />
<%
response.setDateHeader("expires", 0);
try
{
_logger.info("-->processQueryPanel.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "0");
int iDPIndex = Integer.parseInt(strDPIndex);
String strDataSource = requestWrapper.getQueryParameter("sDataSource", false, "");
String strMoveToIndex = requestWrapper.getQueryParameter("iMoveTo", false, "-1");
int iMoveTo = Integer.parseInt(strMoveToIndex);
    String sName = requestWrapper.getQueryParameter("sParam1", false, "");
    String sRemoveDP = requestWrapper.getQueryParameter("bRemoveDP", false, "false");
    String sDuplicateDP = requestWrapper.getQueryParameter("bDuplicateDP", false, "false");
    String sClearContexts = requestWrapper.getQueryParameter("bClearCtxs", false, "false");
    DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
    DataProviders dps = doc.getDataProviders();
    DataProvider dp  = null;
%>
<html>
<head>
<script language="javascript">
var msg="",sToken=""
var displayDlg= false, bError=true, bClearCtx=false
var _p = parent;
if(!_p.arrDPs) _p=parent.parent;
<%
try
{
if(strDataSource!="") 
{
dp=dps.createDP(strDataSource,-1);
updateStorageToken(out,doc);
oDS.updateArrayOfDPInfo(out,dps);
out.println("_p.updateIsQPModified(true);");
out.println("_p.addDPTab('"+ ViewerTools.escapeQuotes(dp.getName())+"',false)");
Properties props = doc.getProperties();
out.println("_p.updateUseQueryDrill("+ props.getProperty(PropertiesType.DATABASE_CONNECTED_DRILL) +")");
}
if(sName!="")
{
dp  = dps.getItem(iDPIndex);
dp.setName(sName);
updateStorageToken(out,doc);
oDS.updateArrayOfDPInfo(out,dps);
out.println("_p.updateIsQPModified(true);");
out.println("_p.renameDP('"+ViewerTools.escapeQuotes( dp.getName())+"')");
}
if(iMoveTo>-1) 
{
dps.move(iDPIndex,iMoveTo);
updateStorageToken(out,doc);
oDS.updateArrayOfDPInfo(out,dps);
out.println("_p.updateIsQPModified(true);");
out.println("_p.updateDPTab("+iMoveTo+")");
}
if(sRemoveDP.equals("true"))
{
dps.deleteDP(iDPIndex);
updateStorageToken(out,doc);
oDS.updateArrayOfDPInfo(out,dps);
out.println("_p.updateIsQPModified(true);");
out.println("_p.deleteDPTab("+iDPIndex+")");
}
if(sDuplicateDP.equals("true"))
{
dp  = dps.duplicate(iDPIndex);
updateStorageToken(out,doc);
oDS.updateArrayOfDPInfo(out,dps);
out.println("_p.updateIsQPModified(true);");
out.println("_p.addDPTab('"+ ViewerTools.escapeQuotes(dp.getName())+"',true);");
}
if(sClearContexts.equals("true"))
{
dp  = dps.getItem(iDPIndex);
dp.clearContexts();
out.println("bClearCtx = true;");
out.println("sToken='"+doc.getStorageToken()+"';");
}
}
catch (Exception e)
{
out.println("msg=\""+e.getLocalizedMessage()+"\";");
out.println("displayDlg = true;");
}
%>
function loadCB()
{
if(displayDlg)
{
if (_p.showAlertDialog)
_p.showAlertDialog(msg,null,2)
}
else
{
if(_p.hideBlockWhileWaitWidget)
_p.hideBlockWhileWaitWidget()
if(bClearCtx && parent.updateAfterClearContexts)
parent.updateAfterClearContexts(sToken);
else if(parent.endQProperties)
parent.endQProperties();
}
}
</script>
</head>
<body onload="loadCB()">
</body>
</html>
<%
_logger.info("<--processQueryPanel.jsp");
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_DEFAULT",true, out, session);
}
%>
<%!
void updateStorageToken(JspWriter out,DocumentInstance doc)
throws IOException
{
String sEntry = doc.getStorageToken();
out.println("_p.changeEntry('"+sEntry+"')");
}
%>
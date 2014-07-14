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
_logger.info("-->processSetSQL.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strWOMquery = requestWrapper.getQueryParameter("WOMquery", false, "");
_logger.info(strWOMquery);
String strCustomSQL = requestWrapper.getQueryParameter("inputSQL", false, "");
String strDPName = requestWrapper.getQueryParameter("sDPName", false, "");
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "");
    String action = requestWrapper.getQueryParameter("nAction", false, "0");    
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
DataProviders dps = doc.getDataProviders();
DataProvider dp = null;
%>
<html>
<head>
<script language="javascript">
var msg="";
var par = parent.parent;
var displayDlg = (<%=action%>=="0") ? true:false;
var action =<%=action%>;
var isError=false;
var mustFillPrompt=false;
var bRestoreSQL=false;
var bForceCustom=false;
<%
if(!strDPName.equals(""))
{ 
dp = dps.getItem(strDPName);
int i =0,ndp=dps.getCount();
for(i=0;i<ndp;i++)
{
if(dps.getItem(i).getName().equals(strDPName))
{
strDPIndex = Integer.toString(i);
break;
}
}
}
else if(!strDPIndex.equals(""))
{ 
dp = dps.getItem(Integer.parseInt(strDPIndex));
}
if( (dp!=null) && (dp instanceof SQLDataProvider))
{
    if(action.equals("3")) 
{
out.println("with(parent){");
objApplyQueries.printIterSQL(out,((SQLDataProvider)dp).getSQLContainer(false),0);
out.println("isCustomSQL=" + (((SQLDataProvider)dp).isCustomSQL() ? true : false) + ";");
out.println("}");
out.println("bRestoreSQL=true;bForceCustom=true;");
strEntry = doc.getStorageToken();
out.println("par.changeEntry('"+strEntry+"');");
}
    else if(action.equals("2")) 
{
boolean bModified = objApplyQueries.applyModifications(strWOMquery, dps);
_logger.info(strDPName + " is modified ?"+bModified);
((SQLDataProvider)dp).resetSQL();
out.println("with(parent){");
objApplyQueries.printIterSQL(out,((SQLDataProvider)dp).getSQLContainer(),0);
out.println("isCustomSQL=" + (((SQLDataProvider)dp).isCustomSQL() ? true : false) + ";");
out.println("}");
out.println("bRestoreSQL=true;");
strEntry = doc.getStorageToken();
out.println("par.changeEntry('"+strEntry+"');");
}
else 
{
applySQL((SQLDataProvider)dp,strCustomSQL,_logger);
try
{
boolean bValidate = ((SQLDataProvider)dp).validateSQL();
if(!bValidate && doc.getMustFillPrompts())
{
strEntry = doc.getStorageToken();
out.println("par.strEntry='"+strEntry+"'");
out.println("var params = par.urlParamsNoBID();");
out.println("params += '&iDPIndex="+strDPIndex+"&bValidateSQL=true&nAction="+action+"';");
out.println("mustFillPrompt=true;");
}
if(action.equals("1"))
{
((SQLDataProvider)dp).changeSQL();
strEntry = doc.getStorageToken();
out.println("par.changeEntry('"+strEntry+"');");
}
}
catch (Exception e) 
{
e.printStackTrace();
out.println("msg=\""+ViewerTools.escapeQuotes(e.getLocalizedMessage())+"\";");
out.println("displayDlg = true; isError = true;");
}
}
}
%>
function loadCB()
{
if(bRestoreSQL)
{
parent.updateSQL(parent.curSql0,parent.isCustomSQL,bForceCustom);
}
else if(mustFillPrompt)
{
par.frameNav('SecondDlgFrame',par._root+'getPrompts'+par._appExt+params);
}
else if(displayDlg)
{
if(isError)
par.showAlertDialog(msg,null,2);
else
par.showAlertDialog(parent.validSQLMsg,null,0);
}
else
{
parent.updateSQL(null,true);
parent.closeCB();
}
}
</script>
</head>
<body onload="loadCB()">
</body>
</html>
<%
_logger.info("<--processSetSQL.jsp");
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_VIEW_SQL", true, out, session);
}
%>
<%!
void applySQL(SQLDataProvider dp,String encSQL,DHTMLLogger _logger)
throws IOException
{
if(encSQL.equals("")) return;
String[] arrSQL = ViewerTools.convertStringToArray(encSQL);
_logger.info(arrSQL[1]);
decodeSubSQL(ViewerTools.convertStringToArray(arrSQL[1]),dp.getSQLContainer(),_logger);
}
void decodeSubSQL(String[] arrSQL, SQLContainer cont,DHTMLLogger _logger)
throws IOException
{
if(cont == null)
{
_logger.error("SQLContainer is null");
return;
}
int lenCont =  cont.getChildCount();
int lenSQL = arrSQL.length;
if(lenCont != lenSQL)
{
_logger.error("old and new SQL containers do not match");
return;
}
if(arrSQL.length==1 && arrSQL[0].equals("")) return; 
String[] subArrSQL;
String subSQL;
SQLNode subNode;
for(int k=0;k<lenSQL ;k++)
{
subSQL = arrSQL[k];
subNode = (SQLNode)cont.getChildAt(k);
if(subNode instanceof SQLContainer)
{
subArrSQL=ViewerTools.convertStringToArray(subSQL);
decodeSubSQL(ViewerTools.convertStringToArray(subArrSQL[1]),(SQLContainer)subNode,_logger);
}
else
{
_logger.info(subSQL);
((SQLSelectStatement)subNode).setSQL(subSQL);
}
}
}
boolean isOperator(String s)
throws IOException
{
String lowerCase = s.toLowerCase();
if(lowerCase.equals("minus")
||lowerCase.equals("union")
||lowerCase.equals("joint")
||lowerCase.equals("intersection")
||lowerCase.equals("synchronisation")
)
return true;
else 
return false;
}
%>

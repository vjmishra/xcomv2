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
<jsp:useBean id="objDrillBean" class="com.businessobjects.adv_ivcdzview.Drill" scope="page" />
<%
response.setDateHeader("expires", 0);
response.setContentType("text/html;charset=UTF-8");
try
{
String strQueryString = requestWrapper.getQueryString();
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strAction = request.getParameter("action");
String iReport = requestWrapper.getQueryParameter("iReport", true);
int iReportIndex = Integer.parseInt(iReport);
String strFromID = requestWrapper.getQueryParameter("iFromID", true);
String[] arrFromIDs = ViewerTools.split(strFromID, ";");
String strToID = requestWrapper.getQueryParameter("iToID", true);
String[] arrToIDs = ViewerTools.split(strToID, ";");
String strFilterID = requestWrapper.getQueryParameter("iFilters", false, "");
String[] arrFilterIDs = (String[])request.getAttribute("CDZ.DrillOutOfScope.Filters");
if (arrFilterIDs == null)
arrFilterIDs = ViewerTools.split(strFilterID, ";");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
Report objReport = doc.getReports().getItem(iReportIndex);
DrillInfo objDrillInfo = (DrillInfo) objReport.getNamedInterface("DrillInfo");
int nbEltDrillBar = 0;
DrillBar objDrillBar = objDrillInfo.getDrillBar();
if (objDrillBar != null) nbEltDrillBar = objDrillBar.getCount();
String[] arrDimDrillBar = new String[nbEltDrillBar];
for (int i=0; i<nbEltDrillBar; i++)
{
arrDimDrillBar[i] = objDrillBar.getItem(i).getID();
}
objDrillBean.onStart(objDrillInfo);
objDrillBean.setHierarchies4ExtendScope();
String[] arrHierarchy = objDrillBean.getParentHierarchy(arrToIDs[0]);
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript" src="lib/dialog.js"></script>
<script language="javascript" src="language/<%=(String)session.getAttribute("CDZ.Language")%>/scripts/drillExtendScope.js"></script>
<script language="javascript">
initDom(parent._skin,parent._lang,parent,"scopedrill")
_scope=new Array
fillScope()
arrScope=_scope
lenScope=arrScope.length
arrCheck=new Array
function fillScope()
{
<%
int cpt=0;
boolean outScope=false;
boolean blnFilter=false;
boolean blnCheckDim=false;
if (strAction.equals("up"))  blnCheckDim = true;
for (Enumeration e = objDrillBean.getReportDimension().elements();e.hasMoreElements();)
{
String[] arrDimension = (String[])e.nextElement();
if (arrDimension[3].equals(arrHierarchy[1]))
{
out.println("_scope["+cpt+"]=new Object");
out.println("_scope["+cpt+"].action='"+strAction+"'");
out.println("_scope["+cpt+"].hName='"+ViewerTools.escapeQuotes(arrHierarchy[0])+"'");
out.println("_scope["+cpt+"].hID='"+arrHierarchy[1]+"'");
out.println("_scope["+cpt+"].dimName='"+ViewerTools.escapeQuotes(arrDimension[0])+"'");
out.println("_scope["+cpt+"].dimID='"+arrDimension[1]+"'");
if (strAction.equals("down")) 
{
out.println("_scope["+cpt+"].outScope="+blnCheckDim+"");
if (arrDimension[1].equals(arrFromIDs[0]))
{
String strName = arrDimension[0];
String strInfoCheckFilter = strName + " = " + arrFilterIDs[0];
if (!arrFilterIDs[0].equals(""))
{
out.println("_scope["+cpt+"].filter='"+ViewerTools.escapeQuotes(strInfoCheckFilter)+"'");
out.println("_scope["+cpt+"].filterValue='"+ViewerTools.escapeQuotes(arrFilterIDs[0])+"'");
out.println("_scope["+cpt+"].isQFilter=false");
}
blnFilter = true;
}
if (!blnFilter)
{
DrillBarObject objDrillBarObj = null;
for (int k=0; k<nbEltDrillBar; k++)
{
if (arrDimension[1].equals(arrDimDrillBar[k]))
{
objDrillBarObj = objDrillBar.getItem(arrDimDrillBar[k]);
break;
}
}
if (objDrillBarObj != null)
{
String strName = objDrillBarObj.getName();
String strFilter = objDrillBarObj.getFilter();
if (!strFilter.equals(""))
{
String strInfoCheckFilter = strName + " = " + strFilter;
out.println("_scope["+cpt+"].filter='"+ViewerTools.escapeQuotes(strInfoCheckFilter)+"'");
out.println("_scope["+cpt+"].filterValue='"+ViewerTools.escapeQuotes(strFilter)+"'");
out.println("_scope["+cpt+"].isQFilter="+objDrillBarObj.isQueryFilter()+"");
}
}
}
if (!blnCheckDim && arrDimension[1].equals(arrToIDs[0]))
{
blnCheckDim = true;
}
}
else if (strAction.equals("up"))
{
if (blnCheckDim && arrDimension[1].equals(arrToIDs[0]))
blnCheckDim = false;
out.println("_scope["+cpt+"].outScope="+blnCheckDim+"");
} 
cpt++;
}
}
%>
}
strQueryString="<%=ViewerTools.escapeQuotes(strQueryString)%>";
</script>
</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" onLoad="setTimeout('loadDialog()',1)" >
<!-- Hidden form -->
<script language="javascript">createScopeForm()</script>
</body>
</html>
<%
}
catch(Exception e)
{
  objUtils.displayErrorMsg(e, "_ERR_DRILL", true, out, session);
}
%>
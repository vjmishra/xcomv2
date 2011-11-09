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
<%!
public String[] arrExpNodes;
public int gLevel;
public int gIndex;
public void getChildNodes(ReportMapNode objNode, JspWriter out, String strNodeID)
throws Exception
{
try
{
TreeNode objTreeNode = (TreeNode)objNode;
int nbNodes = objTreeNode.getChildCount();
for (int i=0; i<nbNodes; i++)
{
ReportMapNode objNodeChildren = (ReportMapNode) objTreeNode.getChildAt(i);
boolean blnIsLeaf = objNodeChildren.isLeaf();
out.println("rm=pp.arrReportMap[" + Integer.toString(gIndex) + "]=new Array(nb);");
String strIndex = strNodeID + "-" + Integer.toString(i);
gIndex++;
out.println("rm[0]=\"" + strIndex + "\";");
out.println("rm[1]=\"" + ViewerTools.escapeQuotes(objNodeChildren.getName()) + "\";");
out.println("rm[2]=" + ((blnIsLeaf)?"true":"false") + ";");
out.println("rm[3]=" + Integer.toString(gLevel) + ";");
out.println("rm[4]=\"" + strNodeID + "\";");
out.println("rm[5]=\"" + objNodeChildren.getPath() + "\";");
out.println("rm[6]=\"" + objNodeChildren.getHTMLAnchorName() + "\";");
boolean blnToExpand = false;
if (!blnIsLeaf)
{
for (int j=0; j<arrExpNodes.length; j++)
{
if (arrExpNodes[j].equals(strIndex))
{
blnToExpand = true;
break;
}
}
}
out.println("rm[7]=" + ((blnToExpand)?"true":"false") + ";");
out.println("rm[8]=" + ((blnToExpand)?"true":"false") + ";");
out.flush();
if (blnToExpand)
{
gLevel++;
getChildNodes(objNodeChildren, out, strIndex);
}
}
gLevel--;
}
catch (Exception e)
{
throw e;
}
}
%>
<%
response.setDateHeader("expires", 0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript">
<%
if (!isAlive)
{
%>
if (typeof(parent.alertSessionInvalid)!="undefined")
parent.alertSessionInvalid();
else
parent.parent.alertSessionInvalid();
</script>
</head><body></body></html>
<%
return;
}
try
{
arrExpNodes = new String[0];
gLevel = 0;
gIndex = 0;
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strListNodes = requestWrapper.getQueryParameter("lstNodes", false, "");
if (!strListNodes.equals(""))
arrExpNodes = ViewerTools.split(strListNodes, ";");
String strTarget = requestWrapper.getQueryParameter("sTarget", false, "");
String strCollapse = requestWrapper.getQueryParameter("sCollapse", false, "no");
String strAppendRoot = requestWrapper.getQueryParameter("sAppendRoot", false, "no");
if (!strTarget.equals(""))
{
String strQueryString = requestWrapper.getQueryString();
strQueryString = ViewerTools.removeQueryParameter(strQueryString, "sTarget");
String strRedirectTo = "updateReportMap.jsp?" + ViewerTools.escapeQuotes(strQueryString);
%>
trueParent= parent.leftPane.getFrame();
function submitForm()
{
document.frmReportMap.lstNodes.value=trueParent.document.frmReportMap.lstNodes.value;
document.frmReportMap.action="<%=strRedirectTo%>";
document.frmReportMap.target="subHiddenRepMap";
document.frmReportMap.submit();
}
</script>
<body onload="submitForm()">
<iframe name="subHiddenRepMap" style="position:absolute;left:-100px;top:-100px;width:10px;height:10px;" src="lib/empty.html"></iframe>
<form name="frmReportMap" method="post">
<input type="hidden" name="lstNodes" value="">
</form>
</body>
</html>
<%
return;
}
%>
var bIndirection=(typeof(parent.trueParent)!="undefined")?true:false;
p=(bIndirection)?parent.trueParent:parent;
if (p.blnAllowRefreshRM)
{
var pp=p.parent;
var nb=pp.nbRepMapFields;
<%
DocumentInstance objDocumentInstance = reportEngines.getDocumentFromStorageToken(strEntry);
ReportMap objReportMap = objDocumentInstance.getReportMap();
objReportMap.refresh();
ReportMapNodes objNodes = objReportMap.getStructure();
ReportMapNode objNode = null;
int nbNodes = objNodes.getChildCount();
if (strAppendRoot.equals("yes"))
{
objNode = objNodes.getChildAt(nbNodes - 1);
out.println("rm=pp.arrReportMap[pp.arrReportMap.length]=new Array(nb);");
out.println("rm[0]=\"" + Integer.toString(nbNodes - 1) + "\";");
out.println("rm[1]=\"" + ViewerTools.escapeQuotes(objNode.getName()) + "\";");
out.println("rm[2]=" + ((objNode.isLeaf())?"true":"false") + ";");
out.println("rm[3]=0;");
out.println("rm[4]=\"\";");
out.println("rm[5]=\"" + ViewerTools.escapeQuotes(objNode.getPath()) + "\";");
out.println("rm[6]=\"" + ViewerTools.escapeQuotes(objNode.getHTMLAnchorName()) + "\";");
out.println("rm[7]=false;");
out.println("rm[8]=false;");
out.flush();
}
else
{
out.println("pp.arrReportMap=new Array();");
for (int i=0; i<nbNodes; i++)
{
objNode = objNodes.getChildAt(i);
boolean blnIsLeaf = objNode.isLeaf();
String strIndex = Integer.toString(i);
out.println("rm=pp.arrReportMap[" + Integer.toString(gIndex) + "]=new Array(nb);");
gIndex++;
out.println("rm[0]=\"" + strIndex + "\";");
out.println("rm[1]=\"" + ViewerTools.escapeQuotes(objNode.getName()) + "\";");
out.println("rm[2]=" + ((blnIsLeaf)?"true":"false") + ";");
out.println("rm[3]=" + Integer.toString(gLevel) + ";");
out.println("rm[4]=\"\";");
out.println("rm[5]=\"" + ViewerTools.escapeQuotes(objNode.getPath()) + "\";");
out.println("rm[6]=\"" + ViewerTools.escapeQuotes(objNode.getHTMLAnchorName()) + "\";");
if (!strCollapse.equals("yes"))
{
boolean blnToExpand = false;
if (!blnIsLeaf)
{
for (int j=0; j<arrExpNodes.length; j++)
{
if (arrExpNodes[j].equals(strIndex))
{
blnToExpand = true;
break;
}
}
}
out.println("rm[7]=" + ((blnToExpand)?"true":"false") + ";");
out.println("rm[8]=" + ((blnToExpand)?"true":"false") + ";");
out.flush();
if (blnToExpand)
{
gLevel++;
getChildNodes(objNode, out, strIndex);
}
}
else
{
out.println("rm[7]=false;");
out.println("rm[8]=false;");
out.flush();
}
}
}
%>
p.blnAllowRefreshRM=false;
p.buildTree();
p.selItemAfterSync();
if (bIndirection)
{
parent.location.replace("lib/empty.html");
}
}
</script>
</head>
<body></body>
</html>
<%
objNode = null;
objNodes = null;
objReportMap = null;
objDocumentInstance = null;
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_REPORT_MAP", true, out, session);
}
%>

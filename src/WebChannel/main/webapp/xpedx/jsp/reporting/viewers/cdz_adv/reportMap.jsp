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
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript">
<%
if (!isAlive)
{
out.println("parent.parent.alertSessionInvalid();");
out.println("</script></head><body></body></html>");
return;
}
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strPath = requestWrapper.getQueryParameter("sPath", true);
String strNodeID = requestWrapper.getQueryParameter("sNodeID", true);
String strLevel = requestWrapper.getQueryParameter("iLevel", true);
int iLevel = Integer.parseInt(strLevel);
DocumentInstance objDocumentInstance = reportEngines.getDocumentFromStorageToken(strEntry);
ReportMap objReportMap = objDocumentInstance.getReportMap();
ReportMapNode objNode = objReportMap.getNodeFromPath(strPath);
TreeNode objTreeNode = (TreeNode) objNode;
int nbNodes = objTreeNode.getChildCount();
out.println("var strNodeID=\"" + strNodeID + "\";");
out.println("var nb=parent.parent.nbRepMapFields;");
out.println("var arrRepMapItems=new Array();");
for (int i=0; i<nbNodes; i++)
{
ReportMapNode objNodeChildren = (ReportMapNode) objTreeNode.getChildAt(i);
out.println("rm=arrRepMapItems[" + Integer.toString(i) + "]=new Array(nb);");
out.println("rm[0]=\"" + strNodeID + "-" + Integer.toString(i) + "\";");
out.println("rm[1]=\"" + ViewerTools.escapeQuotes(objNodeChildren.getName()) + "\";");
if (objNodeChildren.isLeaf())
out.println("rm[2]=true;");
else
out.println("rm[2]=false;");
out.println("rm[3]=" + Integer.toString(iLevel + 1) + ";");
out.println("rm[4]=\"" + strNodeID + "\";");
out.println("rm[5]=\"" + objNodeChildren.getPath() + "\";");
out.println("rm[6]=\"" + objNodeChildren.getHTMLAnchorName() + "\";");
out.println("rm[7]=false;\n");
out.println("rm[8]=false;\n");
out.flush();
}
%>
parent.appendNodes(strNodeID);
</script>
</head>
<body></body>
</html>
<%
objTreeNode = null;
objNode = null;
objReportMap = null;
objDocumentInstance = null;
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_REPORT_MAP", true, out, session);
}
%>
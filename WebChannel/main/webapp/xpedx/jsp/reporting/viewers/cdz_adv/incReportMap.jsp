<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive)
{
return;
}
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
ReportMap objReportMap = doc.getReportMap();
ReportMapNodes objNodes = objReportMap.getStructure();
int nbReports = objNodes.getChildCount();
for (int i=0; i<nbReports; i++)
{
ReportMapNode objNode = objNodes.getChildAt(i);
String strIndex = Integer.toString(i);
out.println("rm=parent.arrReportMap[" + strIndex + "]=new Array(parent.nbRepMapFields);");
out.println("rm[0]=\"" + strIndex + "\";");
out.println("rm[1]=\"" + ViewerTools.escapeQuotes(objNode.getName()) + "\";");
out.println("rm[2]=" + ((objNode.isLeaf())?"true":"false") + ";");
out.println("rm[3]=0;");       
out.println("rm[4]=\"\";");    
out.println("rm[5]=\"" + ViewerTools.escapeQuotes(objNode.getPath()) + "\";");
out.println("rm[6]=\"" + ViewerTools.escapeQuotes(objNode.getHTMLAnchorName()) + "\";");
out.println("rm[7]=false;");
out.println("rm[8]=false;");
}
objReportMap = null;
objNodes = null;
doc = null;
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>

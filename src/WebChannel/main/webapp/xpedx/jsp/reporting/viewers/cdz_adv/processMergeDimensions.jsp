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
try 
{
_logger.info("--> processMergeDimensions.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);    
String mergeAction = requestWrapper.getQueryParameter("sMergeAction", true);
String sBid = requestWrapper.getQueryParameter("sBid", false);
    String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
    String sDimensionIDs = requestWrapper.getQueryParameter("sDimensionIDs", false, "");
    String sName = requestWrapper.getQueryParameter("name", false, "");
    String sDescription = requestWrapper.getQueryParameter("description", false, "");
    String sourceID = requestWrapper.getQueryParameter("sourceID", false, "");
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
int nReportIndex = Integer.parseInt(iReport);
String [] dimIDs = sDimensionIDs.split(",");
int IDsCount = dimIDs.length;
   _logger.info("sDimensionIDs=" +  sDimensionIDs + ", mergeAction: " + mergeAction);
ReportDictionary rd = doc.getDictionary();
SynchroManager syncManager = rd.getSynchroManager();
if (mergeAction.equals("merge") && (IDsCount > 1)) {
ReportExpression re1 = rd.getChild(dimIDs[0]);
_logger.info("first re=" +  re1);
String name = null;
    String desc = null;
    if (re1.getDataSourceObject() != null) {
    name = re1.getDataSourceObject().getName();
    desc = re1.getDataSourceObject().getDescription();
    }
    if (name == null) {
    name = "";
}    
    if (desc == null) {
    desc = "";
}
ReportExpression re2 = rd.getChild(dimIDs[1]);
_logger.info("second re=" +  re2);
_logger.info("create first link with name ='" + name + "' and desc='"+ desc + "'");
Link link = syncManager.createLink(name, desc, re1, re2);
_logger.info("link from first two re=" +  link);
for (int i = 2; i < IDsCount; i++) {
ReportExpression re = rd.getChild(dimIDs[i]);
link.addExpression(re);
_logger.info("add " + re + " to link.");
}
} else if (mergeAction.equals("unmerge")&& (IDsCount == 1)) {
_logger.info("unmerge link.");
syncManager.removeLink(dimIDs[0]);
} else if (mergeAction.equals("chgSource")&& (IDsCount == 1)) {
_logger.info("change Sourge Dimension of link ID=" + dimIDs[0]);
Link link = (Link) rd.getChild(dimIDs[0]);
if (link != null) 
{
_logger.info("set link=" + link + " with name=" + sName + " and desc=" + sDescription + " // change source id= "+ sourceID);
if (!sourceID.equals("")) 
link.setSourceExpression(sourceID);
link.setName(sName);
link.setDescription(sDescription);
}
}
_logger.info("<-- processMergeDimensions.jsp");
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
} catch(DSObjectSynchroException e) {
_logger.warn("Already existing name for the merged dimension!!!");
out.clearBuffer();
response.setContentType("text/html;charset=UTF-8");
out.println("<html><head><script language=\"javascript\">");
out.println("function okCB() {");
out.println("   var topf=getTopViewerFrameset();");
out.println("   if (topf!=null) topf.restoreAfterError();");
out.println("}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_MERGE_DIM_CREATION", false, "okCB", out, session);
} catch(Exception e) {
objUtils.displayErrorMsg(e, "_ERR_MERGE_DIM_CREATION", true, out, session);
} 
%>
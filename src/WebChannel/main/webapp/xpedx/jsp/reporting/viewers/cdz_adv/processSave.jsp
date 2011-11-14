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
_logger.info("-->processSave.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry = " + strEntry );
String sbQuickSave = requestWrapper.getQueryParameter("quickSave",false,"false");
boolean bQuickSave = Boolean.valueOf(sbQuickSave).booleanValue();
_logger.info("sbQuickSave = " + sbQuickSave );
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
_logger.info("iReport = " + iReport);
int nReportIndex = Integer.parseInt(iReport);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
doc.setSelectedReport(nReportIndex);
out.println("<html><head><script language='javascript'>var p=parent;");
Properties props = doc.getProperties();
if (bQuickSave)
{
doc.save();
_logger.info("Quick save.");
String strKey = "ctx" + requestWrapper.getQueryParameter("sDocID", false, "") + "_docToken";
Enumeration enumSession = session.getAttributeNames();
while (enumSession.hasMoreElements())
{
String strName = (String) enumSession.nextElement();
if (strName != null)
{
if (strName.startsWith(strKey))
session.setAttribute(strName, null);
}
}
} else {
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
_logger.info("strViewerID = " + strViewerID );
String strDocType = requestWrapper.getQueryParameter("doctype", false, "wid");
_logger.info("strDocType = " + strDocType );
String sbOverwrite = requestWrapper.getQueryParameter("overwrite",false,"false");
boolean bOverwrite = Boolean.valueOf(sbOverwrite).booleanValue();
_logger.info("sbOverwrite = " + sbOverwrite );
String strDocName = requestWrapper.getQueryParameter("name", false, "");
if (strDocName.equals("")) {
strDocName = requestWrapper.getQueryParameter("title", false, "");
}
_logger.info("strDocName = " + strDocName );
String strDesc = requestWrapper.getQueryParameter("description", false, "");
_logger.info("strDesc = " + strDesc );
String strKeywords = requestWrapper.getQueryParameter("keywords", false, "");
_logger.info("strKeywords = " + strKeywords );
String folderID = requestWrapper.getQueryParameter("folderId", false, "");
_logger.info("folderID = " + folderID );
String corpCategories = requestWrapper.getQueryParameter("categories", false, "");
_logger.info("corpCategories = " + corpCategories );
String persCategories = requestWrapper.getQueryParameter("personalCategories", false, "");
_logger.info("persCategories = " + persCategories );
String sRefreshOnOpen = requestWrapper.getQueryParameter("refreshOnOpen", false, "false");
_logger.info("sRefreshOnOpen = " + sRefreshOnOpen );
String sRegionalFormatting = requestWrapper.getQueryParameter("permanentRegionalFormatting", false, "false");
_logger.info("sRegionalFormatting = " + sRegionalFormatting );
_logger.info("set document name property: " + strDocName);
props.setProperty(PropertiesType.NAME, strDocName);
_logger.info("strDesc = " + strDesc );
_logger.info("strKeywords = " + strKeywords );
props.setProperty(PropertiesType.DESCRIPTION, strDesc);
props.setProperty(PropertiesType.KEYWORDS, strKeywords);
    List corpList = new ArrayList();
if (!corpCategories.equals(""))
{
String[] corpCategoriesArr = corpCategories.split(",");
for (int i = 0; i < corpCategoriesArr.length; i++)
{
Integer cid = Integer.valueOf(corpCategoriesArr[i]);
corpList.add(cid);
}
}
    _logger.info("Corporate Categories:" + corpList + ", size:" + corpList.size());
    List persList = new ArrayList();
if (!persCategories.equals(""))
{
String[] persCategoriesArr = persCategories.split(",");
for (int i = 0; i < persCategoriesArr.length; i++)
{
Integer cid = Integer.valueOf(persCategoriesArr[i]);
persList.add(cid);
}
}
_logger.info("Personal Categories:" + persList + ", size:" + persList.size());
if ((sRefreshOnOpen.equals("true")) || (sRefreshOnOpen.equals("on"))) {
props.setProperty(PropertiesType.REFRESH_ON_OPEN, "true" );
} else {
props.setProperty(PropertiesType.REFRESH_ON_OPEN, "false" );
}
doc.setProperties(props);
boolean bRegionalFormatting = false;
if ((sRegionalFormatting.equals("true")) || (sRegionalFormatting.equals("on"))) {
bRegionalFormatting = true;
}
if (bRegionalFormatting || strDocType.equalsIgnoreCase("rep")) {
doc.setFormattingLocale(DocumentLocaleType.BASELOCALE);
} else {
doc.setFormattingLocale(DocumentLocaleType.LOCALE);
}
_logger.info("saveAs:" + strDocName + ", folderID:" + Integer.parseInt(folderID) + ", bOverwrite=" + bOverwrite);
if (bOverwrite) {
try {
doc.saveAs(strDocName, Integer.parseInt(folderID), corpList, persList, true);
} catch(ServerException se) {
String errCode = se.getCode().trim();
_logger.info("errCode:" + errCode);
objUtils.displayErrorMsg(se, "_ERR_SAVE", true, out, session);
return;
}
} else {
try {
doc.saveAs(strDocName, Integer.parseInt(folderID), corpList, persList, false);
} catch(ServerException se) {
String errCode = se.getCode().trim();
_logger.info("errCode:" + errCode);
if ("1".equals(errCode) || "WIS 30552".equals(errCode) || "RFC 00325".equals(errCode)) {
out.println("p.eventManager.notify(p._EVT_SAVE_DUPLICATE_DOCNAME);");
out.println("</script></head><html>");
return;
} else if ("WIS 30567".equals(errCode)) {
out.println("p.eventManager.notify(p._EVT_SAVE_NONAUTH_DIR);");
out.println("</script></head><html>");
return;
} else {
objUtils.displayErrorMsg(se, "_ERR_SAVE", true, out, session);
return;
}
}
}
out.println("p._isDuplicateDocName=false;");
String strPropDocID = props.getProperty("docrepoid");
String sKind = null;
if (strDocType.equals("rep")) {
sKind = "FullClient";
} else {
sKind = "Webi";
}
String strDocID = null;
if (folderID != null) {
strDocID = getDocId(entSession, strDocName, folderID, sKind);
}
_logger.info("doc ID from CMS:" + strDocID + ", from prop:" + strPropDocID);
if (strDocType.equals("rep")) {
doc.closeDocument();
ReportEngine objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.FC_REPORT_ENGINE);
doc = objReportEngine.openDocument(Integer.parseInt(strDocID));
}
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
out.println("p.changeEntry(\"" + strEntry + "\")");
out.println("p.strDocID=\"" + strDocID + "\"");
out.println("p.strDocName=\"" + ViewerTools.escapeQuotes(strDocName) + "\"");
out.println("p.setDocTitle(\"" + ViewerTools.escapeQuotes(strDocName) + "\")");
String strLastSavedBy = doc.getProperties().getProperty("lastsavedby", "");
_logger.info("LastSavedBy:" + strLastSavedBy);
out.println("p.strLastSavedBy=\"" + strLastSavedBy + "\"");
_logger.info("The new strEntry for the saved doc is:" + strEntry);
out.println("p.resetStatesCB(); p.saveStateCB();");
}
if (bQuickSave) {
out.println("p.eventManager.notify(p._EVT_PROCESS_QUICK_SAVE);");
} else {
out.println("p.eventManager.notify(p._EVT_PROCESS_SAVE);");
}
out.println("</script></head><html>");
_logger.info("<--processSave.jsp");
}
catch(Exception e)
{
out.clearBuffer();
out.println("<html><body>" + e.getLocalizedMessage() + "</body></html>");
}
%>
<%!
private String getDocId(IEnterpriseSession entSession, String name, String folderID, String kind)
throws SDKException
{
if (entSession == null)
return null;
IInfoStore infoStore = (IInfoStore) entSession.getService("InfoStore");
IInfoObjects objs = infoStore.query("SELECT SI_ID FROM CI_INFOOBJECTS WHERE SI_NAME='" + ViewerTools.doubleSingleQuotes(name) + "' and SI_KIND='" + kind + "' and SI_PARENT_FOLDER='" + folderID + "'");
if (objs.size() <= 0) 
return null;
IInfoObject idObj = (IInfoObject)objs.get(0);
return String.valueOf(idObj.getID());
}
%>

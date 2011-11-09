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
<%@ page import="com.crystaldecisions.sdk.framework.CrystalEnterprise,
                 com.crystaldecisions.sdk.properties.IProperties,
com.crystaldecisions.sdk.properties.IProperty, 
                 com.crystaldecisions.sdk.occa.infostore.*,                 
                 com.crystaldecisions.sdk.framework.ISessionMgr,
                 com.crystaldecisions.sdk.framework.IEnterpriseSession,
                 com.crystaldecisions.sdk.plugin.CeKind,
                 com.crystaldecisions.celib.properties.*,                 
                 com.crystaldecisions.sdk.exception.SDKException,
                 com.crystaldecisions.client.logic.*,
                 com.crystaldecisions.client.logic.Query,
                 com.crystaldecisions.client.logic.util.holder.IntHolder,
 com.crystaldecisions.client.logic.util.conversion.LogicUtils, 
com.crystaldecisions.sdk.occa.pluginmgr.IPluginInfo,                                            
                 com.businessobjects.rebean.wi.DocumentInstance,
                 com.businessobjects.rebean.wi.ReportEngine,
                 java.util.*,                 
                 com.businessobjects.rebean.wi.PropertiesType,
                 com.businessobjects.sdk.plugin.desktop.webi.IWebi"
%>
<%
response.setDateHeader("expires", 0);
if (!isAlive)
{
objUtils.invalidSessionDialog(out);
return;
}
try 
{
_logger.info("-->updateSave.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry = " + strEntry );
    String folderID = requestWrapper.getQueryParameter("folderID", false);
String catID = requestWrapper.getQueryParameter("catID", false);
String persCatID = requestWrapper.getQueryParameter("persCatID", false);
String filesFolderID = requestWrapper.getQueryParameter("filesFolderID", false);
String filesCatID = requestWrapper.getQueryParameter("filesCatID", false);
String strPageListNb = requestWrapper.getQueryParameter("pageListNb", false);
String strIndexListNb = requestWrapper.getQueryParameter("indexListNb", false);
String sHyperlink = requestWrapper.getQueryParameter("sHyperlink", false ,"false");
String sSearchName = requestWrapper.getQueryParameter("searchName", false);
boolean isFromHyperlinkEditor = Boolean.valueOf(sHyperlink).booleanValue();
String strDocID = requestWrapper.getQueryParameter("id", false);
_logger.info("strDocID = " + strDocID );
String sIsNew = requestWrapper.getQueryParameter("isNew", false ,"false");
boolean isNew = Boolean.valueOf(sIsNew).booleanValue();
_logger.info("sIsNew = " + sIsNew );
IInfoStore infoStore = (IInfoStore) entSession.getService("InfoStore");
long d = new java.util.Date().getTime();
_logger.info("sub date 1=" + ((new java.util.Date().getTime()) - d));
int parentID = 0;
String kind = "";
boolean isCheckboxTree = false;
boolean isPersonal = false;
String cbMethod = "loadSubFolderCB";
String strOut = "";
if (folderID != null) {
_logger.info("folderID = " + folderID);
parentID = Integer.valueOf(folderID).intValue();
kind = "'" + CeKind.FOLDER + "'";
strOut = generateFolderTreeFromFilteredQuery(_logger, infoStore, session, parentID, kind, cbMethod);
} else if (filesFolderID != null) {
_logger.info("filesFolderID = " + filesFolderID);
parentID = Integer.valueOf(filesFolderID).intValue();
cbMethod = "null";
strOut = generateListFromQuery(_logger, infoStore, session, parentID, cbMethod, "", "", "", false);
} else if (strPageListNb != null) {
_logger.info("strPageNb = " + strPageListNb);
strOut = generateListFromQuery(_logger, infoStore, session, parentID, cbMethod, strPageListNb, "", "", false);
} else if (strIndexListNb != null) {
_logger.info("strIndexListNb = " + strIndexListNb);
strOut = generateListFromQuery(_logger, infoStore, session, parentID, cbMethod, "", strIndexListNb, "", false);
} else if (catID != null) {
_logger.info("catID = " + catID);
parentID = Integer.valueOf(catID).intValue();
kind = "'" + CeKind.CATEGORY + "'";
isCheckboxTree = !isFromHyperlinkEditor;
cbMethod = "loadSubCatCB";
strOut = generateCatTreeFromQuery(_logger, infoStore, parentID, kind, isCheckboxTree, isPersonal, cbMethod);
} else if (persCatID != null) {
_logger.info("persCatID = " + persCatID);
parentID = Integer.valueOf(persCatID).intValue();
kind = "'" + CeKind.PERSONALCAT + "'";
isCheckboxTree = !isFromHyperlinkEditor;
isPersonal = true;
cbMethod = "loadSubPersCatCB";
strOut = generateCatTreeFromQuery(_logger, infoStore, parentID, kind, isCheckboxTree, isPersonal, cbMethod);
} else if (filesCatID != null) {
_logger.info("filesCatID = " + filesCatID);
parentID = Integer.valueOf(filesCatID).intValue();
cbMethod = "null";
strOut = generateListFromQuery(_logger, infoStore, session, parentID, cbMethod, "", "", "", true);
} else if (sSearchName != null) {
_logger.info("sSearchName = " + sSearchName);
cbMethod = "null";
strOut = generateListFromQuery(_logger, infoStore, session, parentID, cbMethod, "", "", sSearchName, false);
}
_logger.info("sub date 2=" + ((new java.util.Date().getTime()) - d));
_logger.info("<--updateSave.jsp");
infoStore = null;
strOut += "var updateSaveOK=true;";
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript">
p = parent;
    <%=strOut%>
function callParentFillTreeCB()
{
if (window.updateSaveOK && p._updateFromIframeCB)
{
p._updateFromIframeCB()
}
}
</script>
</head>
<body onLoad="callParentFillTreeCB()"></body>
</html>
<%
}
catch(Exception e)
{
out.println("if (typeof(parent.displayWaitCursor)!=\"undefined\")");
out.println("    parent.displayWaitCursor.show(false);");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_SAVE", false, out, session);
}
%>
<%!
private String generateListFromQuery(DHTMLLogger _logger, IInfoStore iStore, HttpSession session, int parentID, String cbMethod, String strPageNb, String strIndexListNb, String searchName, boolean isCategory)
throws SDKException
{
_logger.info("Method generateListFromQuery");
Index index = null;
IInfoObjects objects = null;
IntHolder newPageNb = new IntHolder(0);
   int objsPerPage = 100;
int pagesPerIndex = 5; 
String props = "SI_ID, SI_NAME, SI_KIND, SI_DESCRIPTION";
String whereAnd = " AND SI_KIND NOT IN ('" + CeKind.OBJECTPACKAGE + "','" + CeKind.FOLDER + "','" + CeKind.FAVORITESF + "','" + CeKind.PERSONALCAT + "','" + CeKind.CATEGORY + "')";   
if (!strIndexListNb.equals("")) {
index = (Index) session.getAttribute( "CDZ_VIEWER_SaveAs_Page_Index");        
if (index != null) {
if ("-1".equals(strIndexListNb)) { 
while (index.getHasNext()) {
    _logger.info("has next.");
    index = index.getNext(iStore);
    }    
    session.setAttribute( "CDZ_VIEWER_SaveAs_Page_Index", index);
    objects = index.getObjectsByKey(iStore, 0, newPageNb);
} else {    
int indexListNb = -1;
try {
indexListNb = Integer.parseInt(strIndexListNb);
_logger.info("parsed indexListNb: " + indexListNb); 
} catch(NumberFormatException nfe) {
indexListNb = 1;
}
    int curIdxNb = index.getIndexNum();
    _logger.info("curIdxNb: " + curIdxNb); 
    if ((curIdxNb - indexListNb) < 0) {
    _logger.info("look for next."); 
    if (index.getHasNext()) {
    _logger.info("has next."); 
    index = index.getNext(iStore);    
    objects = index.getObjectsByKey(iStore, 0, newPageNb);
    } else {
    index = null;
    }
    } else {
    _logger.info("look for previous."); 
    if (index.getHasPrev()) {
    _logger.info("has previous.");
    index = index.getPrev(iStore);    
    objects = index.getObjectsByKey(iStore, 0, newPageNb);
    } else {
    index = null;
    }        
}
    session.setAttribute( "CDZ_VIEWER_SaveAs_Page_Index", index);
}
if (index != null) {
newPageNb = new IntHolder((((index.getIndexNum()-1) * pagesPerIndex) + 1));
}
    }
} else if (!strPageNb.equals("")) {
int pageNb = 0;
try {
pageNb = Integer.parseInt(strPageNb);
_logger.info("parsed pageNb: " + pageNb); 
} catch(NumberFormatException nfe) {
pageNb = 1;
}
if (pageNb > 0) {
    index = (Index) session.getAttribute( "CDZ_VIEWER_SaveAs_Page_Index");        
    if ((index != null) && (index.getSize() > 0)) {
    objects = index.getObjectsByPage(iStore, pageNb, newPageNb);
    } else {
    index= null;
    }
} 
} else if (!searchName.equals("")) {
searchName = searchName.trim().substring(0,(searchName.length() > 200?200:searchName.length()));
searchName = searchName.replaceAll(",\\^;/!'<\\]\\[>%\\*&\"$", "");
if (!searchName.equals("")) {
ITraversalLogic traversal = TraversalLogic.getInstance();
    String strWhere = " SI_NAME LIKE '%" + searchName + "%' ";    
    strWhere += whereAnd;
Query query = new Query(props, "CI_INFOOBJECTS", strWhere, "SI_KIND, SI_NAME ASC");
_logger.info("search query:" + props + "CI_INFOOBJECTS" + strWhere + "SI_KIND, SI_NAME ASC");
index = traversal.getQueryIndex(iStore, query, objsPerPage, pagesPerIndex);
_logger.info("index size: " + index.getSize());
traversal = null;
query = null;
if ((index != null) && (index.getSize() > 0)) {
objects = index.getObjectsByKey( iStore, 0, newPageNb); 
_logger.info("objects results size: " + objects.getResultSize());
} else {
index = null;
}
}
session.setAttribute( "CDZ_VIEWER_SaveAs_Page_Index", index);
} else {
ITraversalLogic traversal = TraversalLogic.getInstance();
    String strWhere = isCategory?"MEMBERS (\"SI_NAME='Category-Document'\", \"SI_ID=" + parentID + "\")" : " SI_PARENTID=" + parentID;    
    strWhere += whereAnd;
Query query = new Query(props, "CI_INFOOBJECTS", strWhere, "SI_NAME ASC");
_logger.info("FIRST QUERY:" + props + "CI_INFOOBJECTS" + strWhere + "SI_KIND, SI_NAME ASC");
index = traversal.getQueryIndex(iStore, query, objsPerPage, pagesPerIndex);
traversal = null;
if ((index != null) || (index.getSize() > 0)) {
objects = index.getObjectsByKey( iStore, 0, newPageNb); 
} else {
_logger.info("No Result.");
index = null;
}
session.setAttribute( "CDZ_VIEWER_SaveAs_Page_Index", index);
}
int nPgNb = 0;
int currentIdxNb = 0;
int maxPage = 0;
boolean hasPrevIndex = false;
boolean hasNextIndex = false;
StringBuffer sb = new StringBuffer(1024);
if (index != null) {
nPgNb = newPageNb.get();
currentIdxNb = index.getIndexNum();
maxPage = index.getSize();
hasPrevIndex = index.getHasPrev();
hasNextIndex = index.getHasNext();
} else {
sb.append("p._maxIdxNb=-1;");
}
sb.append("p._pageNb=" + nPgNb + ";");     
sb.append("p._currentIdxNb=" + currentIdxNb + ";");
sb.append("p._maxPage=" + maxPage + ";");
sb.append("p._hasPrevIndex=" + hasPrevIndex + ";");
sb.append("p._hasNextIndex=" + hasNextIndex + ";"); 
if ("-1".equals(strIndexListNb)) {
sb.append("p._maxIdxNb=" + currentIdxNb + ";");
}
if ((index != null) && (objects != null)) {
   printTreeObjects(_logger, sb, objects, cbMethod);
}
index = null;
    objects = null;    
  _logger.info(" generateListFromQuery: " + sb);
  return sb.toString();
} 
private String generateFolderTreeFromFilteredQuery(DHTMLLogger _logger, IInfoStore iStore, HttpSession session, int parentID, String kindFilter, String cbMethod)
throws SDKException
{
_logger.info("Method generateFolderTreeFromFilteredQuery");
ITraversalLogic traversal = TraversalLogic.getInstance();
String props = "SI_ID, SI_NAME, SI_KIND, SI_DESCRIPTION";    
    String strWhere = " SI_KIND IN(" + kindFilter + ")  AND ";      
strWhere += " SI_PARENTID=" + parentID;
    Query query = new Query(props, "CI_INFOOBJECTS", strWhere, "SI_NAME ASC");
    _logger.info("SQL: " + query);
IInfoObjects objects = traversal.getInfoObjects(iStore, query);    
StringBuffer sb = new StringBuffer(1024);
   printTreeObjects(_logger, sb, objects, cbMethod);
    objects = null;
    return sb.toString();
}    
private void printTreeObjects(DHTMLLogger _logger, StringBuffer sb, IInfoObjects objects, String cbMethod)
throws SDKException 
{
    if (objects == null) {
    _logger.info("objects is null in printTreeObjects");
    return; 
    } 
    _logger.info("Found: " + objects.size() + " objects.");
    for (int i = 0; i < objects.size(); i++) {
        IInfoObject obj = (IInfoObject) objects.get(i);
        String objID = "" + obj.getID();           
        String title = ViewerTools.escapeQuotes(obj.getTitle());
        String description = obj.getDescription();
        if ("".equals(description)) {
            description = title;
        } else {
        description = ViewerTools.replace(description, "\n", "<br>", true);
        description = ViewerTools.escapeQuotes(description);
        }
        String kind = obj.getKind();
String varName = "p.fold" + objID;
String iconUrl = "";
try {
String progID = obj.getProgID();
 iconUrl = "p._root + 'getKindIcon' + p._appExt + '?progID=" + progID + "'"; 
} catch (SDKException e) {
_logger.info("getProgID:" + e.getMessage());
}
_logger.info("iconUrl:" + iconUrl);
   sb.append( varName + "= p.newTreeWidgetElem(0,\"" + title + "\",\"" + objID + "\",\"" + description + "\",null,\"" + description + "\",null,null,null," + iconUrl + ");\n");      
        sb.append("p._currentTreeElem.add(" + varName + ");\n");
        if (kind.equals(CeKind.FAVORITESF) || kind.equals(CeKind.FOLDER)) {                
sb.append(varName + ".expanded=false;\n");
sb.append(varName + ".setIncomplete(p." + cbMethod + ");\n");                
        }
        obj = null;
  }
    _logger.info(" return printTreeObjects: " + sb);
}
private String generateCatTreeFromQuery(DHTMLLogger _logger, IInfoStore iStore, int parentID, String kindFilter, boolean isCheckboxTree, boolean isPersonal, String cbMethod)
throws SDKException
{
_logger.info("Method generateCatTreeFromQuery.");
    IInfoObjects objects = queryCatsCMS(_logger, iStore, parentID, kindFilter);
    if (objects == null) return "";
    StringBuffer sb = new StringBuffer(1024);
    for (int i = 0; i < objects.size(); i++) {
        IInfoObject obj = (IInfoObject) objects.get(i);
        String objID = "" + obj.getID();
        String kind = obj.getKind();
String title = ViewerTools.escapeQuotes(obj.getTitle());
String varName = "p.fold" + objID;
String iconUrl = "";
try {
String progID = obj.getProgID();
 iconUrl = "p._root + 'getKindIcon' + p._appExt + '?progID=" + progID + "'"; 
} catch (SDKException e) {
_logger.info("getProgID:" + e.getMessage());
}
_logger.info("iconUrl:" + iconUrl);
sb.append( varName + "= p.newTreeWidgetElem(0,\"" + title + "\",\"" + objID + "\",null,null,null,null,null,null," + iconUrl + ");\n");      
        if (isCheckboxTree) {
         sb.append(varName + ".isCheck=true;\n");
         sb.append(varName + ".checkCB=p.checkTreeCB;\n");
        }
        if (isPersonal) {
         sb.append(varName + ".isPersonal=true;\n");
        }            
        sb.append("p._currentTreeElem.add(" + varName + ");\n");
        if (kind.equals(CeKind.PERSONALCAT) || kind.equals(CeKind.CATEGORY)) {                
sb.append(varName + ".expanded=false;\n");
sb.append(varName + ".setIncomplete(p." + cbMethod + ");\n");                
        }
        obj = null;
   }
    objects = null;
    return sb.toString();
}    
private IInfoObjects queryCatsCMS(DHTMLLogger _logger, IInfoStore iStore, int parentID, String kind)
throws SDKException 
{   
_logger.info("Method queryCatsCMS.");
    String query = "SELECT SI_ID, SI_NAME, SI_KIND, SI_DOCUMENTS FROM CI_INFOOBJECTS WHERE ";
    if (!kind.trim().equals("")) {
        query += " SI_KIND IN(" + kind + ")  AND ";
    }
    query += " SI_PARENTID=" + parentID;
    query += " ORDER BY SI_NAME ASC";
    _logger.info("SQL: " + query);
    IInfoObjects objects = iStore.query(query);
    _logger.info("Found: "+objects.size()+" objects.");
    return objects;
}
private String documentsListFromCatCMS(DHTMLLogger _logger, IInfoObject obj)
throws SDKException 
{
_logger.info("Method documentsListFromCatCMS.");
Property prop = (Property) obj.properties().getProperty("SI_DOCUMENTS");
_logger.info("prop:" +  prop);
PropertyBag propBag = (PropertyBag) prop.getPropertyBag();
_logger.info("propBag:" +  propBag);
    int count = propBag.size() - 1; 
    _logger.info("count:" + count);
   StringBuffer docIds = new StringBuffer();    
    for (int i = 0; i < count; i++) {
Integer docId = new Integer(i+1);
    _logger.info("docId:" + docId);
    int propValue = propBag.getInt(docId);
        _logger.info("propValue:" + propValue);
        docIds.append(propValue);
        if (i < (count - 1)) { 
        docIds.append(",");
        }
   } 
    return docIds.toString();
}
private String generateFilesListFromDocsIdQuery(DHTMLLogger _logger, IInfoStore iStore, String filesCatID)
throws SDKException
{
_logger.info("Method generateFilesListFromDocsIdQuery.");
    IInfoObjects objects = queryDocFromIdsCMS(_logger, iStore, filesCatID);
    if (objects == null) return "";
    StringBuffer sb = new StringBuffer(1024);
    for (int i = 0; i < objects.size(); i++) {
        IInfoObject obj = (IInfoObject) objects.get(i);
        String objID = "" + obj.getID();           
        String title = ViewerTools.escapeQuotes(obj.getTitle());
        String description = obj.getDescription();
        if ("".equals(description)) {
            description = title;
        } else {
        description = ViewerTools.replace(description, "\n", "<br>", true);
        description = ViewerTools.escapeQuotes(description);            
        }
String varName = "p.fold" + objID;
String iconUrl = "";
try {
String progID = obj.getProgID();
 iconUrl = "p._root + 'getKindIcon' + p._appExt + '?progID=" + progID + "'"; 
} catch (SDKException e) {
_logger.info("getProgID:" + e.getMessage());
}
_logger.info("iconUrl:" + iconUrl);
        sb.append( varName + "= p.newTreeWidgetElem(0,\"" + title + "\",\"" + objID + "\",\"" + description + "\",null,\"" + description + "\",null,null,null," + iconUrl + ");\n");      
        sb.append("p._currentTreeElem.add(" + varName + ");\n");
        obj = null;
  }
  objects = null;
  return sb.toString();
}    
private IInfoObjects queryDocFromIdsCMS(DHTMLLogger _logger, IInfoStore iStore, String filesCatID)
throws SDKException 
{   
_logger.info("Method queryDocFromIdsCMS.");
ITraversalLogic traversal = TraversalLogic.getInstance();
    String strQuery = "SELECT SI_ID, SI_NAME, SI_KIND, SI_DESCRIPTION FROM CI_INFOOBJECTS WHERE ";        
    strQuery += " SI_ID IN(" + filesCatID + ")";    
strQuery += " ORDER BY SI_NAME ASC";
    _logger.info("SQL: " + strQuery);
    Query query = new Query("SI_ID, SI_NAME, SI_KIND, SI_DESCRIPTION", "CI_INFOOBJECTS", "SI_ID IN(" + filesCatID + ")", "SI_NAME ASC");
    IInfoObjects objects = traversal.getInfoObjects(iStore, query);
    _logger.info("Found: "+objects.size()+" objects.");
    return objects;
}
%>
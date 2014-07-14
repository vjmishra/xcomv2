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
<%
response.setDateHeader("expires", 0);

_logger.info("-->viewDocument.jsp");
boolean blnEnteringViewer = false;
ViewerInstance viewerInstance = null;
String strViewerID = requestWrapper.getQueryParameter("iViewerID", false, "");
if (strViewerID.equals(""))
{
blnEnteringViewer = true;
viewerInstance = instanceManager.newViewerInstance();
strViewerID = Integer.toString(viewerInstance.getViewerID());
_logger.info("New viewer instance: " + strViewerID);
}
else
{
viewerInstance = instanceManager.getViewerInstance(Integer.parseInt(strViewerID));
if (viewerInstance == null)
throw new Exception("Critical error: viewer instance " + strViewerID + " does't exist.");
_logger.info("Opening viewer instance with existing viewer ID: " + strViewerID);
}
String strSkin = requestWrapper.getQueryParameter("skin", false, "skin_standard");
if (!strSkin.equals(""))
session.setAttribute(ViewerTools.SessionSkin, strSkin);
else
{
strSkin = (String)session.getAttribute(ViewerTools.SessionSkin);
if (strSkin == null || strSkin.equals(""))
{
strSkin = "skin_standard";
session.setAttribute(ViewerTools.SessionSkin, strSkin);
}
}
_logger.info("lang=" + strLanguage + ", skin=" +  strSkin);
String strTarget = requestWrapper.getQueryParameter("sTarget", false, "");
if (strTarget.equals(""))
{
if (objUtils.hasRequestPromptsParam(request))
{
strTarget = "processDocWithPrompts";
requestWrapper.setQueryParameter("sTarget", strTarget);
}
}
boolean redirectNeeded = strTarget.equals("")? false : true;
String strDisplayMode = requestWrapper.getQueryParameter("mode", false, "");
if (strDisplayMode.equals(""))
strDisplayMode = requestWrapper.getQueryParameter("sReportMode", false, "full");
strDisplayMode = strDisplayMode.toLowerCase();
if (strDisplayMode.equals("part"))
{
requestWrapper.setQueryParameter("iViewerID", strViewerID);
out.clearBuffer();
%>
<jsp:forward page="viewPart.jsp" />
<%
}
boolean bFullScreen = (strDisplayMode.equals("win")); 
boolean bIsPicker = (strDisplayMode.equals("picker"));
String strCallbackParam = requestWrapper.getQueryParameter("sCallbackParam");
if (strCallbackParam == null) strCallbackParam = "null";
else strCallbackParam = "\"" + ViewerTools.escapeQuotes(strCallbackParam) + "\"";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="imagetoolbar" content="no">
<style type="text/css">
#formulaText{width:490px}
#expandLeft{position:absolute;left:-100px;top:0px;}
</style>
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript" src="lib/dialog.js"></script>
<script language="javascript" src="lib/waitdialog.js"></script>
<script language="javascript">
_skinName="<%=strSkin%>";
_img="images/main/";
initDom("lib/images/"+_skinName+"/","<%=strLanguage%>");
</script>
<script language="javascript">styleSheet()</script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/labels.js"></script>
<script language="javascript">
_appExt=".jsp";
_appSecExt=".jsp";
</script>
</head>
<body onunload="viewerUnloadCB()" onselectstart="return false" ondragstart="return false" style="cursor:default;overflow:hidden" class="bgzone" onresize="setTimeout('if (window.resizeCB) resizeCB()',1)" onload="setTimeout('if (window.resizeCB) resizeCB()',1)" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<script language="javascript" src="scripts/IVIntegration.js"></script>
<script language="javascript">
iViewerID=<%=strViewerID%>;
function viewerCloseCB()
{
var url='processClose.jsp?iViewerID='+iViewerID;
sendAjaxRequest(url, backToParent);
}
function viewerUnloadCB()
{
if (typeof(_dontCloseDoc)=="undefined" || !_dontCloseDoc)
unload();
}
function safeBeforeUnloadCB(e)
{
if (window.beforeUnloadCB) beforeUnloadCB(e);
}
window.onbeforeunload=safeBeforeUnloadCB;
waitDlgBegin=newWaitDialogBoxWidget("waitDlgBegin",250,150,_wait_opendoc_title,true,viewerCloseCB,false,null,true,_wait_opendoc_msg,false);
waitDlgBegin.write();
</script>
<script language="javascript">
waitDlgBegin.init();
waitDlgBegin.show(true);
</script>
<script language="javascript" src="lib/palette.js"></script>
<script language="javascript" src="lib/psheet.js"></script>
<script language="javascript" src="lib/menu.js"></script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/errorManager.js"></script>
<script language="javascript" src="scripts/Utils.js"></script>
<script language="javascript">
listViewerEvents();
<%
try
{
out.flush();
String strDocType = "";
String strKind = requestWrapper.getQueryParameter("kind", false, "");
if (!strKind.equals(""))
strDocType = ViewerTools.convertKindToDocType(strKind);
else
    strDocType = requestWrapper.getQueryParameter("doctype", false, "");
String strEntry = requestWrapper.getQueryParameter("sEntry", false, "");
if (strEntry.equals(""))
strEntry = requestWrapper.getQueryParameter("entry", false, "");
String strDocID = requestWrapper.getQueryParameter("id", false, "");
if (strDocID.equals(""))
strDocID = requestWrapper.getQueryParameter("docid", false, "");
String strDocName = requestWrapper.getQueryParameter("name", false, "");
if (strDocName.equals(""))
strDocName = requestWrapper.getQueryParameter("docname", false, "");
String strViewType = requestWrapper.getQueryParameter("viewType", false, "");
if (strViewType.equals(""))
{
strViewType = requestWrapper.getQueryParameter("ViewType", false, "");
if (!strViewType.equals(""))
requestWrapper.setQueryParameter("viewType", strViewType);
}
_logger.info("strViewType = "+strViewType);
String strforceViewType = requestWrapper.getQueryParameter("forceViewType", false, "H");
_logger.info("strforceViewType = "+strforceViewType);
String strUndoEnabled = requestWrapper.getQueryParameter("sUndoEnabled", false, "true");
String sIsNew = requestWrapper.getQueryParameter("isNew",false,"false");
boolean bIsNewDoc = Boolean.valueOf(sIsNew).booleanValue();
_logger.info("sIsNew = "+sIsNew);
String strNEV = requestWrapper.getQueryParameter("sNEV", false, "no");
String sbLaunchQP = requestWrapper.getQueryParameter("bLaunchQP", false, "false");
boolean bLaunchQP =  Boolean.valueOf(sbLaunchQP).booleanValue();
String sbEmptyDoc = requestWrapper.getQueryParameter("bEmptyDoc",false,"false");
boolean bEmptyDoc = Boolean.valueOf(sbEmptyDoc).booleanValue();
String strNewDoc = requestWrapper.getQueryParameter("sNewDoc", false, "");
if (blnEnteringViewer)
{
strNEV = bLaunchQP?"no":"yes";
objUserSettings.init(entSession, strDocID);
if (strDocType.equals(""))
{
    strKind = objUserSettings.getDocKind();
if (strKind != null)
strDocType = ViewerTools.convertKindToDocType(strKind);
}
viewerInstance.setDocType(strDocType);
    if (strViewType.equals(""))
{
if (strDocType.equals("rep"))
strViewType = objUserSettings.getUserPreference("DOCUMENT_DIViewTechno");
else
strViewType = objUserSettings.getUserPreference("DOCUMENT_WIViewTechno");
}
}
ReportEngine objReportEngine;
if (strDocType.equals("rep"))
objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.FC_REPORT_ENGINE);
else
objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
objReportEngine.setLocale(requestWrapper.getUserLocale());
int storageTokenStackSize = objReportEngine.getStorageTokenStackSize();
DocumentInstance doc = null;
if (strEntry.equals(""))
{
_logger.info("Opening document from DocID");
doc = objReportEngine.openDocument(Integer.parseInt(strDocID));
}
else
{
try
{
_logger.info("Opening document from storage token");
doc = reportEngines.getDocumentFromStorageToken(strEntry);
}
catch(Exception docE)
{
_logger.info("Could not open document from StorageToken => trying from its DocID");
doc = objReportEngine.openDocument(Integer.parseInt(strDocID));
}
}
boolean  isInteractive = true;
boolean  isPDF = false;
if (strforceViewType.equals("I"))
{
isPDF = false;
isInteractive = true;
}
else if (strforceViewType.equals("P")) 
{
isPDF = true;
if (strViewType.equals("I"))
isInteractive = true;
else
isInteractive = false;
}
else if (strViewType.equals("H"))
{
	
isInteractive = false;
isPDF = false;
}
else if (strViewType.equals("I"))
{
isInteractive = true;
isPDF = false;
}
else
{
isInteractive = false;
isPDF = true;
}
if (bFullScreen && isPDF)
{
isInteractive = false;
isPDF = false;
}
if (bIsPicker)
{
isInteractive = true;
    isPDF = false;
}
Properties docProps = doc.getProperties();
if (bEmptyDoc && objReportEngine.getCanEditDocument())
{
String sDefaultDocName = requestWrapper.getQueryParameter("defaultDocName",false,"");
docProps.setProperty(PropertiesType.NAME, sDefaultDocName);
doc.setProperties(docProps);
String sDefaultRepName= requestWrapper.getQueryParameter("defaultRepName",false,"");
if ( !sDefaultRepName.endsWith("1") )
sDefaultRepName += "1";
ReportContainer report= doc.createReport(sDefaultRepName);
doc.applyFormat();
strEntry = doc.getStorageToken();
}
String strRedirectTo = null;
boolean blnNeedAnswerCP = false;
String strRefreshOnOpen = docProps.getProperty(PropertiesType.REFRESH_ON_OPEN, "false");
strRefreshOnOpen = strRefreshOnOpen.toLowerCase();
String strRefresh = requestWrapper.getQueryParameter("sRefresh", false, "no");
if (strRefresh.equalsIgnoreCase("y")) strRefresh = "yes";
if (strRefresh.equals("yes"))
 {
if (!strRefreshOnOpen.equals("true"))
{
_logger.info("Refresh document");
doc.refresh();
}

String strCtxset = requestWrapper.getQueryParameter("sCtxset", false, "no");
if (strCtxset.equalsIgnoreCase("yes") && doc.getMustFillContexts())
{
Contexts arrContexts = doc.getContextPrompts();
for (int i=1; i<=arrContexts.getCount(); i++)
{
Context objContext = arrContexts.getItem(i-1);
String[] arrSelContexts = null;
if (null != objContext.getCurrentValues() && objContext.getCurrentValues().length > 0)
{
arrSelContexts = objContext.getCurrentValues();
}
else if (null != objContext.getPreviousValues() && objContext.getPreviousValues().length > 0)
{
arrSelContexts = objContext.getPreviousValues();
}
if (null != arrSelContexts)
{
objContext.enterValues(arrSelContexts);
}
}
doc.setContexts();
}
} 
if (strRefresh.equals("yes") || strRefreshOnOpen.equals("true"))
	
/* Commented For Reporting.. Jira 2810 
{ 
if (doc.getMustFillContexts())
{
strRedirectTo = "sForwardTo=getContextsInfos";
blnNeedAnswerCP = true;
}
else if (doc.getMustFillPrompts())
{
strRedirectTo = "sForwardTo=getPrompts";
String strPreviousURL = requestWrapper.getQueryParameter("backUrl", false, "");
if (!strPreviousURL.equals(""))
session.setAttribute("CDZ." + strViewerID + ".PreviousURL", strPreviousURL);
blnNeedAnswerCP = false;
}
if (strRedirectTo != null && !strRedirectTo.equals(""))
strRedirectTo += "&iViewerID=" + strViewerID + "&sNEV=" + strNEV + "&viewType=" + strViewType;
} */
 

blnNeedAnswerCP = objUserSettings.getHasPrompts()? true : blnNeedAnswerCP;
int nbReports = 0;
Reports arrReports = null;
try
{
arrReports = doc.getReports();
nbReports = arrReports.getCount();
if (nbReports == 0) 
throw new Exception();
}
catch(Exception e)
{
if (nbReports == 0)
throw new Exception("VIEWER:_ERR_DOCUMENT_HAS_NO_REPORT");
else
throw e;
}
_logger.info("Document contains <" + nbReports + "> reports");
String strSavedReportIndex = "0";
try
{
strSavedReportIndex = Integer.toString(doc.getSelectedReport());
}
catch(Exception e)
{
_logger.info("Cannot get selected report from document instance: " + e.getLocalizedMessage());
}
String strLastRefreshDate = docProps.getProperty("lastrefreshdate");
if(strLastRefreshDate == null) strLastRefreshDate="";
long lLastRefreshMilliSecDate = 0;
String strLastRefreshTime = docProps.getProperty(PropertiesType.LAST_REFRESH_DATE);
if (strLastRefreshTime != null)
lLastRefreshMilliSecDate = Long.parseLong(strLastRefreshTime)*1000;
String strCreationDate = docProps.getProperty("creationdate");
if(strCreationDate == null) strCreationDate="";
long lCreationMilliSecTime = 0;
String strCreationTime = docProps.getProperty("creationtime");
if (strCreationTime != null)
lCreationMilliSecTime = Long.parseLong(strCreationTime)*1000;
String strLastRefreshDuration = docProps.getProperty(PropertiesType.LAST_REFRESH_DURATION, "0");
String strPartialResult = docProps.getProperty(PropertiesType.IS_PARTIALLY_REFRESHED, "false");
strPartialResult = strPartialResult.toLowerCase();
String strSampleResult = docProps.getProperty(PropertiesType.HAS_SAMPLING_RESULTS, "false");
strSampleResult = strSampleResult.toLowerCase();
strDocName = docProps.getProperty(PropertiesType.NAME, "");
String strAuthor = docProps.getProperty(PropertiesType.AUTHOR, "");
String strLastSavedBy = docProps.getProperty("lastsavedby", "");
String sbUseQueryDrill = docProps.getProperty(PropertiesType.DATABASE_CONNECTED_DRILL, "false");
sbUseQueryDrill = sbUseQueryDrill.toLowerCase();
_logger.info("sbUseQueryDrill="+sbUseQueryDrill);
String strDateFormat = "";
String strAllUserEditQuery = docProps.getProperty("enablealternateusertoeditdoc");
String strDisableQueryPanel = docProps.getProperty("disablequerypanel");
String strURLRoot = objUtils.getURLRoot(request);
strURLRoot += "/viewDocument.jsp";
String strUserName = entSession.getUserInfo().getUserName();
int    iReportIndex  = 0;
String iReport       = "";
Report objReport = null;
String strReportName = requestWrapper.getQueryParameter("sReportName", false, "");
String strReportPart = requestWrapper.getQueryParameter("sReportPart", false, "");
if (!strReportPart.equals(""))
{
try
{
ReportPart part = doc.setReportPart(strReportPart);
objReport = part.getReport();
for (int i=0; i<nbReports; i++)
{
if (objReport.getID().equals(arrReports.getItem(i).getID()))
{
iReportIndex = i;
iReport = Integer.toString(iReportIndex);
break;
}
}
}
catch(Exception e)
{
_logger.error("Report Part ID is no longer valid: " + e.getMessage());
}
}
else if (!strReportName.equals(""))
{
strReportName = strReportName.toLowerCase();
for (int i=0; i<nbReports; i++)
{
if (strReportName.equals(arrReports.getItem(i).getName().toLowerCase()))
{
iReport = Integer.toString(i);
break;
}
}
}
else
iReport = requestWrapper.getQueryParameter("iReport", false, "");
if (iReport.equals(""))
{
iReport = strSavedReportIndex;
}
iReportIndex = Integer.parseInt(iReport);
objReport = arrReports.getItem(iReportIndex);
String strPageMode = requestWrapper.getQueryParameter("sPageMode", false, "");
if (strPageMode.equals(""))
strPageMode = objReport.getPaginationMode().toString();
String strReportMode = requestWrapper.getQueryParameter("sReportMode", false, "");
if (strReportMode.equals(""))
strReportMode = objReport.getReportMode().toString();
String iPage = requestWrapper.getQueryParameter("iPage", false, "");
if (iPage.equals(""))
{
try
 {
int n = objReport.getPageNavigation().getCurrent();
iPage = Integer.toString(n);
 }
 catch(Exception e)
 {
 e.printStackTrace();
 iPage = "1";
 }
}
String strPageParam = "&iPage=" + iPage;
String nbPage = requestWrapper.getQueryParameter("nbPage", false, "NaN");
strPageParam += "&nbPage=" + nbPage;
int nbDocAlerters = 0;
if (isInteractive)
{
ReportStructure reportStruct = doc.getStructure();
AlertersDictionary docAlerters = reportStruct.getAlerters();
nbDocAlerters = docAlerters.getCount();
}
String strReportSrc = "report.jsp?";
String reportSrcQS = "iReport=" + iReport + "&iViewerID=" + strViewerID + strPageParam + "&sPageMode=" + strPageMode + "&sReportMode=" + strReportMode + "&sNEV=" + strNEV + "&sUndoEnabled=" + strUndoEnabled + "&isInteractive=" + Boolean.toString(isInteractive);
if (isPDF)
{
reportSrcQS = "sForwardTo=getPDFView&" + reportSrcQS + "&download=no";
}
if (strRedirectTo != null)
{
reportSrcQS = strRedirectTo;
}
if (strNewDoc.equals("true"))
{
reportSrcQS = "sForwardTo=processNewDocument&viewType=" + strViewType + "&" + reportSrcQS;
}
if (redirectNeeded)
{
String strQS = requestWrapper.getQueryString();
strQS = ViewerTools.removeQueryParameter(strQS, "sTarget");
strQS = ViewerTools.setQueryParameter(strQS, "iViewerID", strViewerID);
strQS = ViewerTools.setQueryParameter(strQS, "iReport", iReport);
strQS = ViewerTools.setQueryParameter(strQS, "sReportMode", strReportMode);
strQS = ViewerTools.setQueryParameter(strQS, "isInteractive", Boolean.toString(isInteractive));
strQS = ViewerTools.setQueryParameter(strQS, "sNEV", strNEV);
reportSrcQS = "sForwardTo=" + strTarget + "&" + ViewerTools.escapeQuotes(strQS);
}
strReportSrc += reportSrcQS;
String strLeftPane     = objUserSettings.getUserProfile("CDZ_VIEW_leftPane"); 
String strLeftPaneW    = objUserSettings.getUserProfile("CDZ_VIEW_leftPaneW");
String strLeftPaneSel  = objUserSettings.getUserProfile("CDZ_VIEW_leftPaneSel");
String strStandardPal  = objUserSettings.getUserProfile("CDZ_VIEW_standardPal");
String strFormatPal    = objUserSettings.getUserProfile("CDZ_VIEW_formatPal");
String strReportPal    = objUserSettings.getUserProfile("CDZ_VIEW_reportPal");
String strFormulaPal   = objUserSettings.getUserProfile("CDZ_VIEW_formulaPal");
String strTdcAutoMode  = objUserSettings.getUserProfile("CDZ_VIEW_tdcAutoMode");
String strShowStatus   = objUserSettings.getUserProfile("CDZ_VIEW_showStatus");
String strShowSaveOptions   = objUserSettings.getUserProfile("CDZ_VIEW_showSaveOptions");
String strUnit    = objUserSettings.getUserProfile("CADENZA_MeasurementUnit");
boolean isInch = strUnit.equals("centimeter") ? false : true;
String strSnapToGrid    = objUserSettings.getUserProfile("CADENZA_ResultSnapToGrid");
String strShowGrid      = objUserSettings.getUserProfile("CADENZA_ResultShowGrid");
String strGridSpacing   = objUserSettings.getUserProfile("CADENZA_ResultGridSpacing");
String strShowDesc      = objUserSettings.getUserProfile("CDZ_VIEW_showDesc");
String strDispMenu      = objUserSettings.getUserProfile("CDZ_VIEW_dispMenuHeaders");
String strCtxMenu       = objUserSettings.getUserProfile("CDZ_VIEW_ctxMenu");
String strAppFmt        = objUserSettings.getUserProfile("CDZ_VIEW_appFmt");
String strAddTableHeader= objUserSettings.getUserProfile("CDZ_VIEW_addTableHeader");
String strSortPriority  = objUserSettings.getUserProfile("CDZ_VIEW_sortPriority");
String strDrillCustFmt  = objUserSettings.getUserProfile("CDZ_VIEW_drillCustomFmt");
String strDrillUnderLine= objUserSettings.getUserProfile("CDZ_VIEW_drillUnderlined");
String strDrillBgColor  = objUserSettings.getUserProfile("CDZ_VIEW_drillBgColor");
String strDrillFgColor  = objUserSettings.getUserProfile("CDZ_VIEW_drillFgColor");
String strLastUsedColors  = objUserSettings.getUserProfile("CDZ_VIEW_lastUsedColors");
String strUniversePaneW = objUserSettings.getUserProfile("CDZ_QP_universePaneW");
String strQueryPaneH   = objUserSettings.getUserProfile("CDZ_QP_queryPaneH");
String strUniverseSel= objUserSettings.getUserProfile("CDZ_QP_universeSel");
String strQueryFilter   = objUserSettings.getUserProfile("CDZ_VIEW_defaultQueryFilter");
String strWebEditor     = objUserSettings.getUserPreference("DOCUMENT_WICreateTechno");
if (strDocType.equals("rep")) strWebEditor=""; 
String strViewMode =objUserSettings.getUserPreference("DOCUMENT_WIViewTechno");
String strPromptDrillOutScope = objUserSettings.getUserPreference("DOCUMENT_WIPromptDrillOutScope");
String strStartNewDrill= objUserSettings.getUserPreference("DOCUMENT_WIStartNewDrill");
String strDrillBar= objUserSettings.getUserPreference("DOCUMENT_WIDrillBar");
String strSaveAsXLSOptimized = strDocType.equals("rep")?"N":objUserSettings.getUserPreference("DOCUMENT_WISaveAsXLSOptimized");
String strRefreshDoc = objUserSettings.getUserDocRight("REFRESH_DOCUMENT", strDocID);
String strEditQuery = objUserSettings.getUserDocRight("EDIT_QUERY", strDocID);
String strViewSQL = objUserSettings.getUserDocRight("VIEW_QUERY_SQL_ANALYSIS", strDocID);
String strDownload = objUserSettings.getUserDocRight("DOWNLOAD", strDocID);
String strDownloadPDF = objUserSettings.getUserDocRight("DOWNLOAD_AS_PDF", strDocID);
String strDownloadExcel = objUserSettings.getUserDocRight("DOWNLOAD_AS_EXCEL", strDocID);
String strDownloadCSV = objUserSettings.getUserDocRight("DOWNLOAD_AS_CSV", strDocID);
String strSaveDoc = objUserSettings.getUserDocRight("EDIT_DOCUMENT", strDocID);
String strSaveAsDoc = objUserSettings.getUserDocRight("SAVE_AS_DOCUMENT", strDocID);
String strEditDoc =  objUserSettings.getUserDocRight("EDIT_DOCUMENT", strDocID);
String strEditSQL = objUserSettings.getUserRight("EDIT_QUERY_SQL");
String strPublishDoc = objUserSettings.getUserRight("PUBLISH_DOCUMENT ");
String strWorkInDrillMode = objReportEngine.getCanDrillInDocument()?objUserSettings.getUserRight("WORK_IN_DRILLMODE"):"none";
String strExtendScope = objUserSettings.getUserRight("EXTEND_SCOPE_OF_ANALYSIS");
String strDrillOutScope = objUserSettings.getUserRight("DRILL_OUTSIDE_OF_CUBE");
String strUseFormula = objUserSettings.getUserRight("USE_FORMULA_CREATE_VARIABLE");
String strUseWebiHTMLReportPanel = objUserSettings.getUserRight("USE_WIHTML_REPORTPANEL");
String strUseFormatting = objUserSettings.getUserRight("USE_FORMATTING_TOOLBAR_MENUS");
String strUseUseReporting = objUserSettings.getUserRight("USER_REPORTING_TOOLBAR_MENUS");
String strUseContextMenu = objUserSettings.getUserRight("USE_CONTEXTUAL_MENUS");
String strShowDocSummary = objUserSettings.getUserRight("SHOW_DOCUMENT_SUMMARY");
String strShowFilterMap = objUserSettings.getUserRight("DISPLAY_APPLIED_FILTERS");
String strShowResultObj = objUserSettings.getUserRight("DISPLAY_AVAILABLE_OBJECTS");
String strCreateMergeDimension = objUserSettings.getUserRight("CREATE_MERGE_DIMENSION");
if ((strCreateMergeDimension != null) && (!strCreateMergeDimension.equals("full")) )
{
docProps.setProperty(PropertiesType.MERGE_DIMENSION, "false" );
doc.setProperties(docProps);
}
String sbMergeDimension = docProps.getProperty(PropertiesType.MERGE_DIMENSION, "false");
sbMergeDimension = sbMergeDimension.toLowerCase();
String strUseQueryHTML = objUserSettings.getUserRight("USE_QUERY_HTML");
String strUseHTMLReportPanel = objUserSettings.getUserRight("USE_HTML_REPORT_PANEL");
String strUseJavaReportPanel = objUserSettings.getUserRight("USE_JAVA_REPORT_PANEL");
String strAllowShowHideToolbar = objUserSettings.getUserRight("ALLOW_SHOWHIDE_TOOLBAR");
String strShowRightClickMenu = objUserSettings.getUserRight("SHOW_RIGHT_CLICK_MENU");
String strEditPreferences = objUserSettings.getUserRight("EDIT_PREFERENCES");
String strShowDataSummary = objUserSettings.getUserRight("SHOW_DATA_SUMMARY");
String strCreateEditReportFilter = objUserSettings.getUserRight("CREATE_EDIT_REPORT_FILTER");
String strCreateEditSort = objUserSettings.getUserRight("CREATE_EDIT_SORT");
String strCreateEditBreak = objUserSettings.getUserRight("CREATE_EDIT_BREAK");
String strCreateEditCalculation = objUserSettings.getUserRight("CREATE_EDIT_CALCULATION");
String strCreateEditAlerter = objUserSettings.getUserRight("CREATE_EDIT_ALERTER");
String strCreateEditRank = objUserSettings.getUserRight("CREATE_EDIT_RANK");
String strInsertDuplicate = objUserSettings.getUserRight("INSERT_DUPLICATE");
String strEnableTDC = objUserSettings.getUserRight("TDC_ENABLE");
String strTDCChangeFormat = objUserSettings.getUserRight("TDC_CHANGE_FORMAT");
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
if (!bLaunchQP)
{
int iPos = strReportSrc.indexOf("?") + 1;
String strQueryStringTmp = strReportSrc.substring(iPos);
strReportSrc = strReportSrc.substring(0, iPos);
if (strQueryStringTmp.indexOf("sEntry=") > -1)
strReportSrc += ViewerTools.updateQueryParameter(strQueryStringTmp, "sEntry", strEntry);
else
strReportSrc += "sEntry=" + strEntry + "&" + strQueryStringTmp;
}
else
strReportSrc = "lib/empty.html";
%>
_root=getBasePath();
_showQueryPanel=false;
_appExt=".jsp";
_appSecExt=".jsp";
_showLeftPane=<%=strLeftPane%>;
_leftPaneWidth=<%=strLeftPaneW%>;
_leftPaneSel="<%=strLeftPaneSel%>";
_showStandard=<%=strStandardPal%>;
_showFormats=<%=strFormatPal%>;
_showReporting=<%=strReportPal%>;
_showFormula=<%=strFormulaPal%>;
_tdcAutoMode=<%="1".equals(strTdcAutoMode)%>;
_showStatus=<%=strShowStatus%>;
_unitIsInch=<%=isInch%>;
_snapToGrid=<%=strSnapToGrid%>;
_showGrid=<%=strShowGrid%>;
_gridSpacing="<%=strGridSpacing%>";
_showDesc=<%=strShowDesc%>;
_dispMenuHeaders=<%=strDispMenu%>;
_ctxMenu="<%=strCtxMenu%>";
_showSaveOptions="<%=strShowSaveOptions%>";
_appFmt=<%=strAppFmt%>;
_addTableHeader=<%=strAddTableHeader%>;
_sortPriority="<%=strSortPriority%>";
_defaultQueryFilter="<%=strQueryFilter%>";
_drillCustomFmt=<%=strDrillCustFmt%>;
_drillUnderlined=<%=strDrillUnderLine%>;
_drillBgColor="<%=strDrillBgColor%>";
_drillFgColor="<%=strDrillFgColor%>";
    <%
if ((strLastUsedColors != null) && (!strLastUsedColors.equals(""))) {
%>
_lastUsedColorsAr = "<%=strLastUsedColors%>".replace(/\./g, ",").split(";");
<% } else { %>
_lastUsedColorsAr = new Array;
<% } %>
_curLastUsedColor=_lastUsedColorsAr.length
_defaultWebEditor="<%=strWebEditor%>";
_promptDrillOutScope="<%=strPromptDrillOutScope%>";
_startNewDrill="<%=strStartNewDrill%>";
_showDrillBar=<%=strDrillBar.equals("Y")%>;
_unvPaneWidth=<%=strUniversePaneW%>;
_queryPaneHeight=<%=strQueryPaneH%>;
_unvDisplayMode="<%=strUniverseSel%>";
_saveAsXLSOptimized="<%=strSaveAsXLSOptimized%>";
_usrWorkInDrillMode="<%=strWorkInDrillMode%>";
strWorkInDrillMode=_usrWorkInDrillMode
_usrEditQuery="<%=strEditQuery%>";
_usrViewSQL="<%=strViewSQL%>";
_usrEditSQL="<%=strEditSQL%>";
_usrUseFormula="<%=strUseFormula%>";
_usrExtendScope="<%=strExtendScope%>";
_usrDrillOutScope="<%=strDrillOutScope%>";
_usrRefreshDoc="<%=strRefreshDoc%>";
_usrSaveDoc="<%=strSaveDoc%>";
_usrSaveAsDoc="<%=strSaveAsDoc%>";
_usrPublishDoc="<%=strPublishDoc%>";
_usrDownload="<%=strDownload%>";
<%
if (strDocType.equals("rep"))
{
%>
_usrDownloadExcel=_usrDownload;
_usrDownloadPDF=_usrDownload;
_usrDownloadCSV=_usrDownload;
<%
}
else
{
%>
_usrDownloadExcel=(_usrDownload=="full")?"full":"<%=strDownloadExcel%>";
_usrDownloadPDF=(_usrDownload=="full")?"full":"<%=strDownloadPDF%>";
_usrDownloadCSV=(_usrDownload=="full")?"full":"<%=strDownloadCSV%>";
<%
}
%>
_usrDownloadDocMenu=(_usrDownloadExcel=="full" || _usrDownloadPDF=="full" || _usrDownloadCSV==="full")?"full":"none";
_usrDownloadExcelOrPDF = (_usrDownloadExcel=="full" || _usrDownloadPDF=="full" )?"full":"none";
_usrEditDoc="<%=strEditDoc%>";
_usrUseFormatting="<%=strUseFormatting%>";
_usrUseReporting="<%=strUseUseReporting%>";
_usrUseContextMenu="<%=strUseContextMenu%>";
_usrShowDocInfo="<%=strShowDocSummary%>";
_usrShowFilterMap="<%=strShowFilterMap%>";
_usrShowResultObj="<%=strShowResultObj%>";
_usrCreateMergeDimension="<%=strCreateMergeDimension%>";
_usrUseQueryHTML="<%=strUseQueryHTML%>";
_usrUseHTMLReportPanel="<%=strUseHTMLReportPanel%>";
_usrUseJavaReportPanel="<%=strUseJavaReportPanel%>";
_usrAllowShowHideToolbar="<%=strAllowShowHideToolbar%>";
_usrShowRightClickMenu="<%=strShowRightClickMenu%>";
_usrEditPreferences="<%=strEditPreferences%>";
_usrShowDataSummary="<%=strShowDataSummary%>";
_usrCreateEditReportFilter="<%=strCreateEditReportFilter%>";
_usrCreateEditSort="<%=strCreateEditSort%>";
_usrCreateEditBreak="<%=strCreateEditBreak%>";
_usrCreateEditCalculation="<%=strCreateEditCalculation%>";
_usrCreateEditAlerter="<%=strCreateEditAlerter%>";
_usrCreateEditRank="<%=strCreateEditRank%>";
_usrInsertDuplicate="<%=strInsertDuplicate%>";
_usrEnableTrackDataChanges="<%=strEnableTDC%>";
_usrTDCChangeFormat="<%=strTDCChangeFormat%>";
if ( parent.overrideUserSettings )
parent.overrideUserSettings(window.self);
strDocName="<%=ViewerTools.escapeQuotes(strDocName)%>";
strDocID="<%=strDocID%>";
strDocType="<%=strDocType%>";
strRepoType="";
strViewType="<%=strViewType%>";
strSavedReportIndex="<%=strSavedReportIndex%>";
strURLRoot="<%=strURLRoot%>";
strEntry="<%=strEntry%>";
strAuthor="<%=strAuthor%>";
strUserName="<%=strUserName%>";
strLastSavedBy="<%=strLastSavedBy%>";
bUndoableAction=<%=strUndoEnabled%>;
isNew=<%=bIsNewDoc%>;
strViewType="<%=strViewType%>";
bUseQueryDrill=<%=sbUseQueryDrill%>;
bMergeDimensions=<%=sbMergeDimension%>;
iStorageTokenStackSize=<%=storageTokenStackSize%>;
blnPartialResult=<%=strPartialResult%>;
hasSampleResult=<%=strSampleResult%>;
setDocTitle(strDocName);
strDateFormat="<%=strDateFormat%>";
if (strDateFormat == "") strDateFormat=_localeDateFormat;
var strLastRefreshDate="<%=ViewerTools.escapeQuotes(strLastRefreshDate)%>";
 if ((strLastRefreshDate == "") && (<%=lLastRefreshMilliSecDate%>!=0) )
  {
  dtDate=new Date(<%=lLastRefreshMilliSecDate%>);
  strLastRefreshDate = formatDate(dtDate,strDateFormat);
  }
  if(strLastRefreshDate == "") strLastRefreshDate=_noRefreshYet; 
var strCreationDate="<%=ViewerTools.escapeQuotes(strCreationDate)%>";
if ((strCreationDate == "") && (<%=lCreationMilliSecTime%>!=0) )
{
strCreationDate=formatDate(new Date(<%=lCreationMilliSecTime%>),strDateFormat);
}
arr_AM_PM_LocaleString=new Array(2);
arr_AM_PM_LocaleString[0]=_localeTimeAM;
arr_AM_PM_LocaleString[1]=_localeTimePM;
allUsersEditQueries=<%=strAllUserEditQuery%>;
disableQueryPanel=<%=strDisableQueryPanel%>;
_propShowTabs4PDF=<%=(objUtils.objConfig.getProperty("SHOW_TABS_WHEN_VIEWING_PDF", "no").equals("yes")?"true":"false")%>;
<%
FormatNumber localeFormat = null;
FormatNumber defaultFormat = doc.getDefaultFormatNumber(FormatNumberType.DATE_TIME);
if((defaultFormat!= null) && (defaultFormat instanceof DefaultDateTimeFormatNumber)) 
localeFormat = ((DefaultDateTimeFormatNumber) defaultFormat).getDateFormatting();   
if (localeFormat != null)
out.println("_inputFormatDate=\""+ViewerTools.escapeQuotes(localeFormat.getPositive())+"\";");
else
out.println("_inputFormatDate=strDateFormat;");
%>
allUseDictionary=newHashTable();
iReportID="<%=iReport%>";
strPageMode="<%=strPageMode%>";
strReportMode="<%=strReportMode%>";
iPage="<%=iPage%>";
nbPage="<%=nbPage%>";
bFullScreen=<%=bFullScreen%>;
iDataProviderID=0;
blnNeedAnswerCP=<%=blnNeedAnswerCP%>;
isInteractive=<%=isInteractive%>;
isPicker=<%=bIsPicker%>;
sCallbackParam=<%=strCallbackParam%>;
blnAllowPicker=false;
isPDF=<%=isPDF%>;
allowSaveAs=true;
allowMyInfoView=true;
if (typeof(parent.allowSaveAs) != "undefined")
allowSaveAs=parent.allowSaveAs;
if (typeof(parent.allowMyInfoView) != "undefined")
allowMyInfoView=parent.allowMyInfoView;
bLaunchQP=<%=bLaunchQP%>;
bCreateDefaultReportBody=<%=bLaunchQP%>;
arrState=new Array;
curState=-1;
lastRefreshDuration=<%=strLastRefreshDuration%>;
isFirstPage=true;
isLastPage=true;
_listSeparator=';'
_keydateLastAvailable="<%=Keydate.LAST_AVAILABLE%>";
eventManager=newEventManager();
_UDZ_bNoSaveas=(typeof(parent.goSave)=="undefined")?true:false;
_UDZ_useCustomPrompts=(typeof(parent.goPrompts)!="undefined")?true:false;
arrReports=new Array();
arrReportMap=new Array();
nbRepMapFields=9;
blnAllowRefreshMap=true;
iNbReports=<%=nbReports%>;
<%
StringBuffer sbReports = new StringBuffer(1024);
for (int i=0; i<nbReports; i++)
{
String strIndex = Integer.toString(i);
objReport = arrReports.getItem(i);
String name = ViewerTools.escapeQuotes(objReport.getName());
String pageMode = (iReportIndex == i)?strPageMode:objReport.getPaginationMode().toString();
String repMode = (iReportIndex == i)?strReportMode:objReport.getReportMode().toString();
String sNbPage = (iReportIndex == i)?nbPage:"NaN";
String selPage = (iReportIndex == i)?iPage:"1";
String sIsLeaf = "true";
boolean tdcActivate = false;
if (!strDocType.equals("rep")) {
TrackDataInfo tdi = objReport.getTrackDataInfo();
if (tdi != null) {
tdcActivate = tdi.isActive();
}
}
sbReports.append("arrReports[" + strIndex + "]=newReportInfo(\"" + name + "\",\"" + pageMode + "\",\"" + repMode + "\"," + sNbPage + "," + selPage + ","+ sIsLeaf + ",null,null,null," + tdcActivate + ");\n");
}
out.println(sbReports.toString());
printDPs(out,doc);
Properties customProperties = (Properties) session.getAttribute("BO_CUSTOM_PROPS");
if (customProperties == null)
{
customProperties = new Properties();
session.setAttribute("BO_CUSTOM_PROPS",customProperties);
}
String useCustomPrompts = requestWrapper.getQueryParameter("useCustomPrompts", false, "");
if (!useCustomPrompts.equals(""))
{
customProperties.setProperty("useCustomPrompts", useCustomPrompts);
session.setAttribute("BO_CUSTOM_PROPS", customProperties);
requestWrapper.removeQueryParameter("useCustomPrompts");
}
%>
</script>
<%
if (isInteractive)
{
%>
    <script language="javascript">
allFonts=new Array;
platformFonts = new Array;
<%
_logger.info("Retrieve platorm & server fonts.");
FontMapping[] allFonts = objReportEngine.getInstalledFontNames(PlatformType.HTML);
int count = allFonts.length;
for (int i = 0; i < count; i++) {
FontMapping fontMap = allFonts[i];
out.println("allFonts[" + i + "]=\"" + fontMap.getServerFontName().trim() +"\";");
out.println("platformFonts[" + i + "]=\"" + fontMap.getPlatformName().trim() + "\";");
}
printSkins(out, objReportEngine);
%>
  _nbDocAlerters = <%=nbDocAlerters%>;
</script>
<script language="javascript" src="scripts/CommonWom.js"></script>
<script language="javascript" src="scripts/QueryWom.js"></script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/editor.js"></script>
<%
}
if (bIsPicker)
{
%>
<script language="javascript" src="language/<%=strLanguage%>/scripts/reportPicker.js"></script>
<%
}
%>
<script language="javascript" src="language/<%=strLanguage%>/scripts/viewer.js"></script>
<script language="javascript">
loadCBObs=newObserverOneEvent(_EVT_HTML_WRITTEN,loadCB);
<%
if (strRefreshOnOpen.equals("true") || strRefresh.equals("yes"))
out.println("eventManager.notify(_EVT_REFRESH_DATA);");
%>
    wt();
    customPromptsParam = "&useCustomPrompts="+(_UDZ_useCustomPrompts?"Y":"N");
</script>
<script language="javascript">writeBody("<%=strReportSrc%>"+customPromptsParam)</script>
</body>
</html>
<%
viewerInstance.setViewerState(InstanceManager.ViewerStateAlive);
_logger.info("<--viewDocument.jsp");
}
catch(Throwable e)
{
viewerInstance.setViewerState(InstanceManager.ViewerStateFailed);
e.printStackTrace();
String strMsg = ViewerTools.escapeQuotes(objUtils.getFormattedErrorMsg(e.getLocalizedMessage(), ""));
String strHtmlTrace = ViewerTools.escapeQuotes(objUtils.getHTMLElement4Trace(e.getLocalizedMessage(), ""));
%>
</script>
<script language="javascript">
var msg="<%=strMsg%>";
var htmlTrace="<%=strHtmlTrace%>";
alertDlg=newAlertDialog("ViewerAlertDlg","","",_ERR_OK_BUTTON_CAPTION,_promptDlgCritical);
alertDlg.write();
</script>
<script language="javascript">
   alertDlg.init();
document.write(htmlTrace);
function viewerErrCB()
{
backToParent();
}
function hideWaitDlg()
{
   waitDlgBegin.show(false);
}
function showAlertDialog(msg,title,iPromptType,okCB)
{
alertDlg.setText(msg)
alertDlg.setTitle(title)
alertDlg.setPromptType(iPromptType)
alertDlg.yesCB=(okCB)?okCB:null
alertDlg.show(true)
}
setTopFrameset();
advDisplayViewerErrorMsgDlg(msg,_ERR_OPEN_DOCUMENT,viewerErrCB);
</script>
</body>
</html>
<%
}
%>
<%! 
void printSkins(JspWriter out, ReportEngine objReportEngine)
throws IOException
{
    StringBuffer skins = new StringBuffer("_reportSkins = new Array;\n");
    Skin[] reportSkins = objReportEngine.getBackgroundSkins(SkinReportElementType.REPORT) ;
    for (int i = 0; i < reportSkins.length; i++)
    {
        skins.append("_reportSkins[_reportSkins.length] = \"" + ViewerTools.escapeQuotes(((Skin) reportSkins[i]).getName()) + "\";\n");
    }
    Skin[] sectionSkins = objReportEngine.getBackgroundSkins(SkinReportElementType.SECTION) ;
    skins.append("_sectionSkins = new Array;\n");
    for (int i = 0; i < sectionSkins.length; i++)
    {
        skins.append("_sectionSkins[_sectionSkins.length] = \"" + ViewerTools.escapeQuotes(((Skin) sectionSkins[i]).getName()) + "\";\n");
    }
    Skin[] blockSkins = objReportEngine.getBackgroundSkins(SkinReportElementType.BLOCK) ;
    skins.append("_blockSkins = new Array;\n");
    for (int i = 0; i < blockSkins.length; i++)
    {
        skins.append("_blockSkins[_blockSkins.length] = \"" + ViewerTools.escapeQuotes(((Skin) blockSkins[i]).getName()) + "\";\n");
    }
    Skin[] cellSkins = objReportEngine.getBackgroundSkins(SkinReportElementType.CELL) ;
    skins.append("_cellSkins = new Array;\n");
    for (int i = 0; i < cellSkins.length; i++)
    {
        skins.append("_cellSkins[_cellSkins.length] = \"" + ViewerTools.escapeQuotes(((Skin) cellSkins[i]).getName()) + "\";\n");
    }
    out.println(skins);
}
void printDPs(JspWriter out, DocumentInstance doc)
throws IOException
{
DataProviders dps = doc.getDataProviders();
DataProvider dp = null;
int count  = dps.getCount(), nRows = 0;
String dpName="";
boolean refreshable=true;
int dpType=0; 
StringBuffer buffer = new StringBuffer("arrDPs = new Array;\n arrDPs.length="+count+";\n");
for(int i=0;i<count;i++)
{
dp = dps.getItem(i);
dpName = dp.getName();
nRows = dp.getRowCount();
if(dp instanceof PersonalDataProvider)
{
refreshable = false;
dpType =1;
}
else
{
refreshable = true;
dpType =0;
}
buffer.append("arrDPs["+i+"]=new Object; arrDPs["+i+"].name=\""+ViewerTools.escapeQuotes(dpName)+"\"; arrDPs["+i+"].rows="+nRows+";arrDPs["+i+"].refreshable="+refreshable+";arrDPs["+i+"].dpType="+dpType+";");
}
out.println(buffer);
}
%>

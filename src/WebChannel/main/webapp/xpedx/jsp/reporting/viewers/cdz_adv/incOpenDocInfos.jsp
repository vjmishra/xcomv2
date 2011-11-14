<%@ include file="incStartpage.jsp" %>
<%@ page import="com.crystaldecisions.sdk.properties.*,
 com.crystaldecisions.celib.properties.*" %>
<%
response.setDateHeader("expires", 0);
String strNewDoc = null;
String strParent = "";
try
{
strNewDoc = requestWrapper.getQueryParameter("sNewDoc", false, "true");
if (!isAlive)
{
if (strNewDoc.equals("true"))
objUtils.invalidSessionDialog(out);
return;
}
if (strNewDoc.equals("true"))
{
%>
<html>
<head>
<script language="javascript">
var p=parent;
p.doc=p.openDocInfo();
<%
strParent = "p.";
}
requestWrapper.setQueryParameter("sParent", strParent);
String query = "SELECT SI_CUID, SI_ID, SI_NAME, SI_KIND, SI_FILES, SI_IS_SCHEDULABLE FROM CI_INFOOBJECTS WHERE ";
String strDocID = requestWrapper.getQueryParameter("id", false, "");
if (strDocID.equals(""))
{
strDocID = requestWrapper.getQueryParameter("cuid", true);
query += "SI_CUID = '" + strDocID + "'";
}
else
query += "SI_ID = " + strDocID;
IInfoStore store = (IInfoStore) entSession.getService("InfoStore");
IInfoObjects infoObjs = store.query(query);
if (infoObjs.size() > 0)
{
IInfoObject infoObj = (IInfoObject) infoObjs.get(0);
IProperties props = infoObj.properties();
Property prop = (Property) props.getProperty("SI_CUID");
out.println(strParent + "doc.CUID=\"" + ((prop != null) ? prop.toString() : "null") + "\";");
prop = (Property) props.getProperty("SI_ID");
String strID = (prop != null) ? prop.toString() : "";
out.println(strParent + "doc.ID=\"" + strID + "\";");
requestWrapper.setQueryParameter("id", strID);
prop = (Property) props.getProperty("SI_NAME");
String strName = (prop != null) ? prop.toString() : "";
out.println(strParent + "doc.name=\"" + ViewerTools.escapeQuotes(strName) + "\";");
prop = (Property) props.getProperty("SI_KIND");
String strKind = (prop != null) ? prop.toString() : "null";
out.println(strParent + "doc.kind=\"" + strKind + "\";");
prop = (Property) props.getProperty("SI_FILES");
if (prop != null)
{
PropertyBag propBag = prop.getPropertyBag();
String strFileName = propBag.getString("SI_FILE1");
if (strFileName != null)
{
int iPos = strFileName.lastIndexOf('.');
if (iPos > -1)
{
String strFileType = strFileName.substring(iPos + 1);
out.println(strParent + "doc.type=\"" + strFileType + "\";");
}
}
}
prop = (Property) props.getProperty("SI_IS_SCHEDULABLE");
boolean isSchedulable = (prop != null) ? ((Boolean)prop.getValue()).booleanValue() : false;
out.println(strParent + "doc.isSchedulable=" + ((isSchedulable) ? "true" : "false") + ";");
if (strKind.equalsIgnoreCase("Webi") || strKind.equalsIgnoreCase("FullClient"))
{
out.println(strParent + "doc.isAgnostic=false;");
ReportEngine objReportEngine;
if (strKind.equalsIgnoreCase("FullClient"))
objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.FC_REPORT_ENGINE);
else
objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
DocumentInstance doc = objReportEngine.openDocument(Integer.parseInt(strID));
String strRefreshOnOpen = doc.getProperties().getProperty(PropertiesType.REFRESH_ON_OPEN, "false");
out.println(strParent + "doc.refreshOnOpen=" + (strRefreshOnOpen.equalsIgnoreCase("true") ? "true" : "false") + ";");
try
{
int iSelectedReport = doc.getSelectedReport();
Reports arrReports = doc.getReports();
for (int i=0; i<arrReports.getCount(); i++)
{
out.println(strParent + "doc.arrReports[" + Integer.toString(i) + "]=new Array(2);");
out.println(strParent + "doc.arrReports[" + Integer.toString(i) + "][0]=\"" +  ViewerTools.escapeQuotes(arrReports.getItem(i).getName()) + "\"");
out.println(strParent + "doc.arrReports[" + Integer.toString(i) + "][1]=" + ((i==iSelectedReport)?"true":"false") + ";");
}
}
catch(Exception e)
{
e.printStackTrace();
}
Prompts arrPrompts = doc.getPrompts();
for (int i=0; i<arrPrompts.getCount(); i++)
{
Prompt objPrompt = arrPrompts.getItem(i);
String strIndex = Integer.toString(i);
out.println(strParent + "doc.arrPrompts[" + strIndex + "]=new "+ strParent + "newPromptInfo();");
out.println(strParent + "doc.arrPrompts[" + strIndex + "].name=\"" + ViewerTools.escapeQuotes(objPrompt.getName()) + "\";");
out.println(strParent + "doc.arrPrompts[" + strIndex + "].id=\"" + ViewerTools.escapeQuotes(objPrompt.getID()) + "\";");
out.println(strParent + "doc.arrPrompts[" + strIndex + "].type='" + ((objPrompt.getType() == PromptType.Mono) ? "S" : "M") + "';");
out.println(strParent + "doc.arrPrompts[" + strIndex + "].isOptional=" + (objPrompt.isOptional()? "true" : "false") + ";");
out.println(strParent + "doc.arrPrompts[" + strIndex + "].isSelected=false;");
}
}
else if (strKind.equalsIgnoreCase("CrystalReport"))
{
out.println(strParent + "doc.isAgnostic=false;");
try
{
%>
<jsp:include flush="true" page="incCrystalParams.jsp" />
<%
}
catch(Throwable e)
{
_logger.info("Internal error occured when trying to access Crystal Report libraries: " + e.getLocalizedMessage());
e.printStackTrace();
}
}
query = "SELECT SI_ID FROM CI_INFOOBJECTS WHERE SI_PARENTID = " + strID;
infoObjs = store.query(query);
out.println(strParent + "doc.hasInstances=" + ((infoObjs.size() > 0)? "true": "false") + ";");
if (strNewDoc.equals("true"))
{
%>
p.parent.hideBlockWhileWaitWidget();
p.fillopenDocForm();
</script>
</head>
<body></body>
</html>
<%
}
}
else
{
if (strNewDoc.equals("true"))
throw new Exception("VIEWER:_ERR_HYPERLINK_DOC_NOT_FOUND");
else
out.println("_displayWarningMessage=_ERR_HYPERLINK_DOC_NOT_FOUND_RECOVERY;");
}
}
catch(Exception e)
{
if (strNewDoc.equals("true"))
{
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_HYPERLINK", false, out, session);
}
else
{
out.clearBuffer();
objUtils.incErrorMsg(e, out);
}
}
%>

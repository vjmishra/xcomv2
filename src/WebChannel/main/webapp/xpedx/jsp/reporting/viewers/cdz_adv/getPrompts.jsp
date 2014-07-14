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
<jsp:useBean id="objPromptsBean" class="com.businessobjects.adv_ivcdzview.Prompts" scope="page">
<% 
objPromptsBean.onStart(requestWrapper);
%>
</jsp:useBean>
<%
_logger.info("--> getPrompts");
response.setDateHeader("expires", 0);
String strSRC = requestWrapper.getQueryParameter("src", false, "");
if (!isAlive)
{
if (strSRC.equals("BCA"))
{
session.setAttribute(ViewerTools.SessionAlive, "yes");
out.println("_isSessionInvalid=false;");
}
else
{
objUtils.invalidSessionDialog(out);
return;
}
}
Properties customProperties = (Properties) session.getAttribute("BO_CUSTOM_PROPS");
if (customProperties != null)
{
String useCustomPrompts = customProperties.getProperty("useCustomPrompts", "");
if (useCustomPrompts.equals("Y"))
{
_logger.info("Custom prompts required");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String callbackQS = ViewerTools.removeQueryParameter(requestWrapper.getQueryString(),"sEntry");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="scripts/Utils.js"></script>
<script language="javascript">
function goCustomPrompts()
{
var viewerFrame = getTopViewerFrameset();
if ((viewerFrame != null) && (typeof(viewerFrame.parent.goPrompts) != "undefined"))
{
var callbackScript="viewers/cdz_adv/viewDocument.jsp?";
var callbackQS="<%=callbackQS%>";
viewerFrame.parent.goPrompts('<%=strEntry%>',callbackScript,callbackQS);
}
}
</script>
</head>
<body onload="goCustomPrompts()">
</body>
</html>
<%
return;
}
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="scripts/Utils.js"></script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/prompts.js"></script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/promptsUI.js"></script>
<%
String strCancel = "";
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry IN = " + strEntry);
String strViewerID = null;
if (strSRC.equals("BCA"))
strViewerID = requestWrapper.getQueryParameter("iViewerID", false, "0");
else
strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strNEV = requestWrapper.getQueryParameter("sNEV", false, "no");
String strSelPrompt = requestWrapper.getQueryParameter("iSelPrompt", false, "0");
String strNewDoc = requestWrapper.getQueryParameter("sNewDoc", false, "false");
String strApplyFormat = requestWrapper.getQueryParameter("sApplyFormat", false, "");
String strViewType = requestWrapper.getQueryParameter("viewType", false, "");
strCancel = requestWrapper.getQueryParameter("sCancel", false, "");
String strWebApp = requestWrapper.getQueryParameter("webApp", false, "");
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "");
String strValidateSQL = requestWrapper.getQueryParameter("bValidateSQL", false, "false");
String action = requestWrapper.getQueryParameter("nAction", false, "");
String domHome = requestWrapper.getQueryParameter("domHome", false, "");
String[] arrNotAnsweredPrompts = null;
String strNaii = requestWrapper.getQueryParameter("NAII", false, "");
if (strNaii.equals("Y"))
{
String strNotAnsweredPrompts = requestWrapper.getQueryParameter("sNotSetPrompts", false, "");
if (!strNotAnsweredPrompts.equals(""))
arrNotAnsweredPrompts = ViewerTools.split(strNotAnsweredPrompts, "-");
}
%>
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript" src="lib/dialog.js"></script>
<script language="javascript" src="lib/palette.js"></script>
<script language="javascript" src="lib/psheet.js"></script>
<script language="javascript" src="lib/treeview.js"></script>
<script language="javascript" src="lib/prompttree.js"></script>
<script language="javascript" src="lib/menu.js"></script>
<script language="javascript" src="lib/multilist.js"></script>
<script language="javascript" src="lib/lov.js"></script>
<script language="javascript" src="lib/calendar.js"></script>
<script language="javascript">
p = parent;
initDom( p._root + "lib/images/"+p._skinName+"/", "<%=strLanguage%>", p<%=domHome%>, "prompts");
</script>
<script language="javascript">styleSheet();</script>
<script language="javascript">
<%
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
Prompts arrPrompts = doc.getPrompts();
int iNbPrompts = arrPrompts.getCount();
String[][] arrPromptNames = new String[iNbPrompts][2];
for (int i=0; i<iNbPrompts; i++)
{
arrPromptNames[i][0] = arrPrompts.getItem(i).getName();
arrPromptNames[i][1] = "false";
for (int j=0; j<i; j++)
{
if (arrPromptNames[i][0].equalsIgnoreCase(arrPromptNames[j][0]))
{
arrPromptNames[i][1] = "true";  
arrPromptNames[j][1] = "true";
break;
}
}
}
String strDefaultInputFormat = null;
String strDocType = doc.getProperties().getProperty(PropertiesType.DOCUMENT_TYPE) ;
if (strDocType.equalsIgnoreCase("WID"))
{
ReportEngine objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
if(objReportEngine!=null)
{
FormatNumber defaultFormat = doc.getDefaultFormatNumber(FormatNumberType.DATE_TIME);
if((defaultFormat!= null) && (defaultFormat instanceof DefaultDateTimeFormatNumber)) 
strDefaultInputFormat = ((DefaultDateTimeFormatNumber) defaultFormat).getDateFormatting().getPositive(); 
}
}
objPromptsBean.setDateFormat(strDefaultInputFormat);
objPromptsBean.setDocType(strDocType) ;
String strDocID = doc.getProperties().getProperty(PropertiesType.DOCUMENT_ID);
String strUserRefreshLov = objUserSettings.getUserDocRight("REFRESH_LISTS_OF_VALUES", strDocID);
out.println("strUserRefreshLov=\"" + strUserRefreshLov.toLowerCase() + "\";");
String strUseLov = objUserSettings.getUserDocRight("USE_LOV", strDocID);
out.println("strUseLov=\"" + strUseLov.toLowerCase() + "\";");
out.println("iNbPrompts=0;");
out.println("arrPrompts=new Array();");
out.println("arrNbLovItems=new Array();");
out.println("arrSelectedValues=new Array();");
out.println("arrLovTexts=new Array();");
out.println("arrLovValues=new Array();");
out.println("arrLovIndexes=new Array();");
out.println("arrNbBatchLovItems=new Array();");
out.println("arrBatchLovTexts=new Array();");
out.println("arrBatchLovSelValue=new Array();");
out.println("arrLovTitle=new Array();");
out.println("iLovSortType=new Array();");
out.println("iLovSortedColumnIndex=new Array();");
int iDPPrompts = 0;
DataProvider dp = null;
if (!strDPIndex.equals("")) 
{
dp = doc.getDataProviders().getItem(Integer.parseInt(strDPIndex));
_logger.info("display prompts for DP " + strDPIndex);
}
for (int i=1; i<=iNbPrompts; i++)
{
if (arrNotAnsweredPrompts != null)
{
boolean blnFoundIndex = false;
String strIndex = Integer.toString(i);
for (int j=0; j<arrNotAnsweredPrompts.length; j++)
{
if (arrNotAnsweredPrompts[j].equals(strIndex))
{
blnFoundIndex = true;
break;
}
}
if (!blnFoundIndex)
continue;
}
Prompt objPrompt = arrPrompts.getItem(i-1);
if (!strDPIndex.equals("") && dp != null)
{
boolean dpFound = false;
DataProvider[] dps = objPrompt.getDataProviders();
for (int k=0; k<dps.length; k++)
{
if (dps[k] == dp) {
dpFound = true;
break;
}
}
if (!dpFound)
continue;
}
iDPPrompts++;
objPromptsBean.gIndex++;
int iLocalIndex = objPromptsBean.gIndex;
String strLocalIndex = Integer.toString(iLocalIndex);
if (arrPromptNames[i-1][1].equals("true"))
{
try
{
DataProvider[] arrDataProviders = objPrompt.getDataProviders();
String strDPName = " (";
for (int j=0; j<arrDataProviders.length; j++)
{
if (j == 0) strDPName += arrDataProviders[j].getName();
else strDPName += ", " + arrDataProviders[j].getName();
}
strDPName += ")";
arrPromptNames[i-1][0] += strDPName;
}
catch (Exception e1)
{
System.out.println("Cannot get Dataproviders from prompts");
e1.printStackTrace();
}
}
String strEscapedPromptName = ViewerTools.convStr(arrPromptNames[i-1][0]);
String strInputFormat = objPrompt.getInputFormat();
if (null == strInputFormat) strInputFormat = "";
boolean blnRequired = objPrompt.requireAnswer();
boolean blnDateType = false;
if (objPrompt.getObjectType() == ObjectType.DATE)
{
blnDateType = true;
if(strDefaultInputFormat != null)
strInputFormat = strDefaultInputFormat;
}
String strParamName = "PV" + Integer.toString(i);
String strParamIndexName = "PI" + Integer.toString(i);
String strParamValue = "";
String[] arrSeletedValues = requestWrapper.getQueryParameterValues(strParamName);
if (arrSeletedValues == null)
{
ValueFromLov[] arrTemp = null;
if (objPrompt.getCurrentValuesFromLov() != null && objPrompt.getCurrentValuesFromLov().length > 0)
{
arrTemp = objPrompt.getCurrentValuesFromLov();
}
else if (objPrompt.getPreviousValuesFromLov() != null && objPrompt.getPreviousValuesFromLov().length > 0)
{
arrTemp = objPrompt.getPreviousValuesFromLov();
}
else if (objPrompt.getDefaultValuesFromLov() != null && objPrompt.getDefaultValuesFromLov().length > 0)
{
arrTemp = objPrompt.getDefaultValuesFromLov();
}
if (arrTemp != null)
{
for (int j=0; j<arrTemp.length; j++)
{
String value = arrTemp[j].getValue();
String index = arrTemp[j].getRowIndex();
if (index == null) index = "";
if (j > 0) strParamValue += ";";
String strIndex = ViewerTools.encodeIndex(index);
if (strIndex == null) strIndex = "";
strParamValue += strIndex + "_" + value;
}
}
}
else
{
if (!(arrSeletedValues.length==1 && arrSeletedValues[0].equals("")))
{
if (arrSeletedValues.length == 1)
arrSeletedValues = ViewerTools.split(arrSeletedValues[0], ";");
String[] arrSeletedIndexes = requestWrapper.getQueryParameterValues(strParamIndexName);
if (arrSeletedIndexes == null || arrSeletedIndexes[0].equals(""))
arrSeletedIndexes = new String[0];
else if (arrSeletedIndexes.length == 1)
arrSeletedIndexes = ViewerTools.split(arrSeletedIndexes[0], ";");
for (int j=0; j<arrSeletedValues.length; j++)
{
if (j > 0) strParamValue += ";";
if (arrSeletedIndexes.length > j)
strParamValue += arrSeletedIndexes[j] + "_" + arrSeletedValues[j];
else
strParamValue += "_" + arrSeletedValues[j];
}
}
}
String strLovID = "";
boolean blnDelegatedSearch = false;
Lov objLOV = null;
boolean blnLOV = false;
if (strUseLov.equals("full"))
{
blnLOV = objPrompt.hasLOV();
if (blnLOV)
{
try
{
objLOV = objPrompt.getLOV();
if (objLOV != null)
{
strLovID = objLOV.getID();
blnDelegatedSearch = objLOV.isDelegated();
}
}
catch(Exception e)
{
_logger.warn("Cannot get LOV object: " + e.getLocalizedMessage());
e.printStackTrace();
blnLOV = false;
}
}
}
out.println("p=arrPrompts[" + strLocalIndex + "]=new Array(19);");
out.println("p[0]=\"" + ViewerTools.escapeQuotes(strEscapedPromptName) + "\";");
out.println("p[1]=\"" + strParamName + "\";");
out.println("p[2]=\"" + strInputFormat + "\";");
out.println("p[3]=" + ((blnDateType)?"true":"false") + ";");
if (objPrompt.getType() == PromptType.Mono)
out.println("p[4]=\"Mono\";");
else if (objPrompt.getType() == PromptType.Multi)
out.println("p[4]=\"Multi\";");
out.println("p[5]=" + ((blnLOV)?"true":"false") + ";");
out.println("p[6]=" + ((objPrompt.isConstrained())?"true":"false") + ";");
out.println("p[7]=false;");
out.println("p[8]=encodeEmptyValue(\"" + ViewerTools.escapeQuotes(strParamValue) + "\",true);");
out.println("p[9]=" + ((blnRequired)?"true":"false") + ";");
out.println("p[10]=\"" + ViewerTools.escapeQuotes(arrPromptNames[i-1][0]) + "\";");
out.println("p[11]=\"" + strLovID + "\";");
out.println("p[12]=false;");
out.println("p[13]=false;");
out.println("p[14]=0;");
out.println("p[15]=true;");
out.println("p[16]=" + ((objPrompt.isOptional())?"true":"false") + ";");
out.println("p[17]=" + ((blnDelegatedSearch)?"true":"false") + ";");
out.println("p[18]=false;");
boolean blnHasNP = false;
if (blnLOV)
{
if (objLOV.mustFillNestedPrompts() || objLOV.getNestedPrompts().getCount()>0)
blnHasNP = true;
boolean blnAny_Level = (objLOV.getLovDisplayType() == LovDisplayType.HIERARCHICAL_ANY_LEVEL);
boolean blnHierar = (blnAny_Level || objLOV.getLovDisplayType() == LovDisplayType.HIERARCHICAL);
objPromptsBean.blnGroupPrompts = (blnHierar && blnHasNP);
if (blnHasNP && strUserRefreshLov.equals("full"))
{
out.println("p[12]=true;");
objPromptsBean.getNestedPrompts(objLOV, Integer.toString(i), 1, blnAny_Level, out, session);
out.println("p=arrPrompts[" + strLocalIndex + "];");
}
if (objPromptsBean.blnGroupPrompts)
out.println("p[13]=true;");
if (i == 1)
{
if (!blnDelegatedSearch && (!strUserRefreshLov.equals("full") || (strParamValue.equals("") && !objPrompt.isOptional()) ))
{
boolean blnHasBatchList = objPromptsBean.getBatchLOV((iLocalIndex+1), objLOV, out);
if (!blnHasBatchList)
out.println("arrNbBatchLovItems[" + strLocalIndex + "]=0;");
boolean blnHasLovItems = false;
if (objPromptsBean.blnGroupPrompts)
{
out.println("p[13]=true;");
blnHasLovItems = objPromptsBean.getLovValues((iLocalIndex+1), -1, objLOV, out);
}
else
blnHasLovItems = objPromptsBean.getLovValues((iLocalIndex+1), objLOV, out);
if (!blnHasLovItems)
out.println("arrNbLovItems[" + strLocalIndex + "]=0;");
out.println("p[18]=" + ((objLOV.isPartialResult())?"true":"false") + ";");
}
}
}
else
{
out.println("arrNbBatchLovItems[" + strLocalIndex + "]=0;");
out.println("arrNbLovItems[" + strLocalIndex + "]=0;");
}
out.flush();
}
session.setAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"), doc);
String strBackURL = (String)session.getAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".PreviousURL"));
if (strBackURL == null)
strBackURL = "";
else
session.setAttribute("CDZ." + strViewerID + ".PreviousURL", null);
out.println("iNbPrompts +=" + Integer.toString(iDPPrompts) + ";");
out.println("strEntry=\"" + strEntry + "\";");
out.println("iViewerID=\"" + strViewerID + "\";");
out.println("strNEV=\"" + strNEV + "\";");
out.println("strNewDoc=\"" + strNewDoc + "\";");
out.println("strApplyFormat=\"" + strApplyFormat + "\";");
out.println("strDPIndex=\"" + strDPIndex + "\";");
out.println("iSelPrompt=" + strSelPrompt + ";");
out.println("strSRC=\"" + strSRC + "\";");
out.println("strViewType=\"" + strViewType + "\";");
out.println("strCancel=\"" + strCancel + "\";");
out.println("backURL=\"" + ViewerTools.escapeQuotes(strBackURL) +"\";");
out.println("strWebApp=\"" + strWebApp + "\";");
out.println("strValidateSQL=\"" + strValidateSQL + "\";");
out.println("nAction=\"" + action + "\";");
%>
function loadCB()
{
setTimeout('loadDialog()',1);
}
</script>
</head>
<body class="dialogzone" onload="loadCB()">
<%
if (!strSRC.equals("BCA"))
out.println("<iframe name='loadLOV' style='position:absolute;left:-100px;top:-100px;width:50px;height:50px' src='lib/empty.html'></iframe>");
%>
</body>
</html>
<%
arrPromptNames = null;
arrPrompts = null;
doc = null;
_logger.info("<-- getPrompts");
}
catch(Exception e)
{
if (strSRC.equals("BCA") || strSRC.equals("RPV"))
{
if (strSRC.equals("BCA"))
out.println("function okCB() {parent.goBackSchedule();}");
else
out.println("function okCB() {parent.goBackDashboard();}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_PROMPTS", false, "okCB", out, session);
}
else if (!strCancel.equals(""))
{
out.println("var previousURL=parent.allUseDictionary.get(\"" + strCancel + "\");");
out.println("function okCB() {parent.frameNav(\"Report\",previousURL);}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_PROMPTS", false, "okCB", out, session);
}
else
{
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_PROMPTS", false, out, session);
}
}
%>

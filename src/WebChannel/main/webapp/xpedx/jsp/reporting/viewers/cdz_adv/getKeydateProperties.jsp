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
_logger.info("--> getKeydateProperties");
response.setDateHeader("expires", 0);
if (!isAlive)
{
objUtils.invalidSessionDialog(out);
return;
}
boolean blnOkKeydate = false;
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry IN = " + strEntry);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
_logger.info("viewerID: " + strViewerID);
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "");
int iDPIndex = (!strDPIndex.equals(""))? Integer.parseInt(strDPIndex) : -1;
String strNEV = requestWrapper.getQueryParameter("sNEV", false, "no");
String strAction = requestWrapper.getQueryParameter("sAction", false, "");
String strQueryString = requestWrapper.getQueryString();
Locale locale = ViewerTools.convStrToLocale(requestWrapper.getUserLocale());
Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), locale);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
ArrayList arrKDs = new ArrayList();
DataProviders dps = doc.getDataProviders();
int count = dps.getCount();
for (int i=0; i<count; i++)
{
if (strDPIndex.equals("") || iDPIndex == i)
{
DataProvider dp = dps.getItem(i);
DataSourceParameterValues dsParamValues = dp.getDataSourceParameterValues();
if (dsParamValues != null)
{
int nbValues = 0;
try
{
nbValues = dsParamValues.getCount();
}
catch(Exception e)
{
_logger.info("Cannot get nombre of datasource parameter values: " + e.getLocalizedMessage());
}
for (int j=0; j<nbValues; j++)
{
DataSourceParameterValue dsParamValue = dsParamValues.getValue(j);
if (dsParamValue != null)
{
DataSourceParameter dsParam = dsParamValue.getParameter();
if (dsParam != null && dsParam.getClassName().equals(DataSourceParameterClassNames.SAPKeyDate) && dsParam.getType() == DataSourceParameterType.DATE)
{
Keydate keydate = new Keydate();
keydate.setCaption(dsParam.getName());
keydate.setQueryName(dp.getName());
keydate.isRequired = dsParam.isRequired();
if (dsParam.getDefaultValue() != null)
{
Date paramDate = (Date)dsParam.getDefaultValue();   
keydate.setDefaultValue(ViewerTools.serializeDate(paramDate, calendar));
}
else
keydate.setDefaultValue(Keydate.LAST_AVAILABLE);
if (dsParamValue.getValue() != null)
{
Date paramDateValue = (Date)dsParamValue.getValue();
keydate.setValue(ViewerTools.serializeDate(paramDateValue, calendar));
keydate.isDefaultValue = dsParamValue.isDefault();
}
else
{
keydate.setValue("");
keydate.isDefaultValue = true;
}
arrKDs.add(keydate);
break;
}
}
}
}
}
}
if (arrKDs.size() == 0)
{
_logger.warn("This document has no keydate.");
if (strAction.equals("Refresh"))
{
out.clearBuffer();
%>
<jsp:forward page="refreshDocument.jsp" />
<%
}
else if (strAction.equals("RunQuery"))
{
out.clearBuffer();
%>
<jsp:forward page="processRunQuery.jsp" />
<%
}
else
return;
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript" src="lib/dialog.js"></script>
<script language="javascript" src="lib/palette.js"></script>
<script language="javascript" src="lib/menu.js"></script>
<script language="javascript" src="lib/calendar.js"></script>
<script language="javascript">
var p=parent;
initDom(p._root+"lib/images/"+p._skinName+"/",p._lang,p,"keydateProperties");
</script>
<script language="javascript" src="scripts/Utils.js"></script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/errorManager.js"></script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/keydateGrid.js"></script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/keydateProperties.js"></script>
<script language="javascript">
<%
blnOkKeydate = true;
String strKeydateCaption = "";
out.println("arrData = new Array();");
for (int i=0; i<arrKDs.size(); i++)
{
Keydate keydate = (Keydate)arrKDs.get(i);
String strIndex = Integer.toString(i);
out.println("arrData[" + strIndex + "]=new Array(6);");
out.println("arrData[" + strIndex + "][0]=\"" + ViewerTools.escapeQuotes(keydate.getQueryName()) + "\";");
out.println("arrData[" + strIndex + "][1]=" + ((keydate.isDefaultValue)? "0" : "1") + ";");
out.println("arrData[" + strIndex + "][2]=\"" + keydate.getDefaultValue() + "\";");
out.println("arrData[" + strIndex + "][3]=\"" + keydate.getValue() + "\";");
out.println("arrData[" + strIndex + "][4]=" + ((keydate.isRequired)? "true" : "false") + ";");
out.println("arrData[" + strIndex + "][5]=\"" + ViewerTools.escapeQuotes(keydate.getCaption()) + "\";");
if (i == 0)
strKeydateCaption = keydate.getCaption();
}
out.println("strQueryString=\"" + ViewerTools.escapeQuotes(strQueryString) + "\";");
out.println("strAction=\"" + strAction + "\";");
out.println("keyDateCaption=\"" + ViewerTools.escapeQuotes(strKeydateCaption) + "\";");
out.println("strNEV=\"" + strNEV + "\";");
Properties props = doc.getProperties();
String strPromptOnRefresh = props.getProperty(PropertiesType.PROMPT_KEYDATE_ONREFRESH, "true");
if (strPromptOnRefresh.equals("")) strPromptOnRefresh = "true";
out.println("blnPromptOnRefresh=" + strPromptOnRefresh + ";");
String strPreviousState = props.getProperty(PropertiesType.KEYDATE_INPUT_MODE, "");
if (!strPreviousState.equals(""))
{
if (strPreviousState.equals(Keydate.INPUT_MODE_LASTFORALL))
strPreviousState = "1";
else if (strPreviousState.equals(Keydate.INPUT_MODE_PARAMTFORALL))
strPreviousState = "2";
else if (strPreviousState.equals(Keydate.INPUT_MODE_PARAMFOREACH))
strPreviousState = "3";
else
strPreviousState = "";
}
if (strPreviousState.equals(""))
strPreviousState = "1";
out.println("previousState=" + strPreviousState + ";");
%>
function loadCB()
{
setTimeout('loadDialog()',1);
}
</script>
</head>
<body class="dialogzone" onload="loadCB()">
<form name="frmKeyDateProperties" id="frmKeyDateProperties" method="post" onSubmit="return false;"  style="display:none">
<script language="javascript">
for (var i=1; i<=arrData.length; i++)
{
document.write('<input type="hidden" name="KDV'+i+'" value="">');
}
document.write('<input type="hidden" name="sPOR" value="">');
document.write('<input type="hidden" name="sCurState" value="">');
</script>
</form>
</body>
</html>
<%
props = null;
dps = null;
doc = null;
_logger.info("<-- getKeydateProperties");
}
catch(Exception e)
{
if (blnOkKeydate)
{
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_KEYDATE_PROPERTIES", false, out, session);
}
else
objUtils.displayErrorMsg(e, "_ERR_KEYDATE_PROPERTIES", true, out, session);
}
%>

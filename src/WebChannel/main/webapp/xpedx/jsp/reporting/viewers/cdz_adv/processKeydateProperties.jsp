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
String strCancel = requestWrapper.getQueryParameter("sCancel", false, "");
try
{
_logger.info("--> processKeydateProperties.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry IN = " + strEntry);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
_logger.info("viewerID: " + strViewerID);
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "");
int iDPIndex = (!strDPIndex.equals(""))? Integer.parseInt(strDPIndex) : -1;
String strAction = requestWrapper.getQueryParameter("sAction", false, "");
Locale locale = ViewerTools.convStrToLocale(requestWrapper.getUserLocale());
Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), locale);
calendar.set(Calendar.HOUR_OF_DAY, 12);
calendar.set(Calendar.MINUTE, 0);
calendar.set(Calendar.SECOND, 0);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
DataProviders dps = doc.getDataProviders();
int count = dps.getCount();
int k=1;
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
String strParamName = "KDV" + Integer.toString(k);
String strValue = requestWrapper.getQueryParameter(strParamName, false, "");
if (!strValue.equals(""))
{
Date dtValue = ViewerTools.deserializeDate(strValue, calendar);
dsParamValue.setValue(dtValue);
}
else
dsParamValue.useDefault();
k++;
break;
}
}
}
}
}
}
boolean propsUpdated = false;
Properties props = doc.getProperties();
String strPromptOnRefresh = requestWrapper.getQueryParameter("sPOR", false, "");
if (!strPromptOnRefresh.equals(""))
{
String strPreviousValue = props.getProperty(PropertiesType.PROMPT_KEYDATE_ONREFRESH, "");
if (!strPromptOnRefresh.equals(strPreviousValue))
{
props.setProperty(PropertiesType.PROMPT_KEYDATE_ONREFRESH, strPromptOnRefresh);
propsUpdated = true;
}
}
String strCurrentState = requestWrapper.getQueryParameter("sCurState", false, "");
if (!strCurrentState.equals(""))
{
String strPreviousValue = props.getProperty(PropertiesType.KEYDATE_INPUT_MODE, "");
int iCurrentState = Integer.parseInt(strCurrentState);
switch(iCurrentState)
{
case 1:
strCurrentState = Keydate.INPUT_MODE_LASTFORALL;
break;
case 2:
strCurrentState = Keydate.INPUT_MODE_PARAMTFORALL;
break;
case 3:
strCurrentState = Keydate.INPUT_MODE_PARAMFOREACH;
break;
}
if (!strCurrentState.equals(strPreviousValue))
{
props.setProperty(PropertiesType.KEYDATE_INPUT_MODE, strCurrentState);
propsUpdated = true;
}
}
if (propsUpdated)
doc.setProperties(props);
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
props = null;
dps = null;
doc = null;
if (strAction.equals("Refresh"))
{
_logger.info("refresh document");
requestWrapper.setQueryParameter("sEntry", strEntry);
out.clearBuffer();
%>
<jsp:forward page="refreshDocument.jsp" />
<%
}
else if (strAction.equals("RunQuery"))
{
_logger.info("run query");
requestWrapper.setQueryParameter("sEntry", strEntry);
out.clearBuffer();
%>
<jsp:forward page="processRunQuery.jsp" />
<%
}
else 
{
_logger.info("update keydate properties in query panel");
%>
<html>
<head>
<script language="javascript">
function loadCB()
{
var qframe = parent.getQueryFrame();
var token='<%=strEntry%>';
if(qframe && qframe.updateKeyDateUI)
qframe.updateKeyDateUI(token);
}
</script>
</head>
<body onload="loadCB()">
</body>
</html>
<%
}
_logger.info("<-- processKeydateProperties.jsp");
}
catch(Exception e)
{
if (!strCancel.equals(""))
{
out.println("<html><head><script language=\"javascript\">");
out.println("p=parent;");
out.println("previousURL=p.allUseDictionary.get(\"" + strCancel + "\");");
out.println("function okCB() {p.frameNav(\"Report\",previousURL);}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_KEYDATE_PROPERTIES", false, "okCB",out, session);
}
else
objUtils.displayErrorMsg(e, "_ERR_KEYDATE_PROPERTIES", true, out, session);
}
%>

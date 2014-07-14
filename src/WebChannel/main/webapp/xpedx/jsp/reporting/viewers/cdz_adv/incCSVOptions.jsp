<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strCharDelimiter = "\"";
String strColumnSeparator = ",";
String strOptCharSet = "";
String strDefaultCharSet = "";
String[] arrOS = ViewerTools.split(objUtils.objConfig.getProperty("locale", ""), ",");
String strCurOS = objUtils.objConfig.getProperty("os.name", "windows");
_logger.info("--> incCSVOptions");
int iIndex = 0;
for (int i=0; i<arrOS.length; i++)
{
if (strCurOS.equalsIgnoreCase(arrOS[i]))
{
iIndex = i;
break;
}
}
String strLocale = (String)session.getAttribute("CDZ.Language");
strDefaultCharSet = objUtils.objConfig.getProperty(strLocale, "UTF-8");
String[] arr = ViewerTools.split(strDefaultCharSet, ",");
if (iIndex >= arr.length) iIndex = 0;
strDefaultCharSet = arr[iIndex];
String strDefaultValues = objUserSettings.getUserProfile("CDZ_VIEW_CSV_DefaultValues", "false");
if (strDefaultValues.equals("true"))
{
strCharDelimiter = objUserSettings.getUserProfile("CDZ_VIEW_CSV_CharDelimiter", strCharDelimiter);
strColumnSeparator = objUserSettings.getUserProfile("CDZ_VIEW_CSV_ColumnSeparator", strColumnSeparator);
strOptCharSet = objUserSettings.getUserProfile("CDZ_VIEW_CSV_Charset", "");
}
session.setAttribute("CDZ." + strViewerID + ".CSV", null);
out.println("delimiters=\"" + ViewerTools.escapeQuotes(objUtils.objConfig.getProperty("CSV_CharDelimiter")) + "\";");
out.println("separators=\"" + ViewerTools.escapeQuotes(objUtils.objConfig.getProperty("CSV_ColumnSeparator")) + "\";");
String[] charsets =objUtils.objConfig.getProperty("CSV_Charset").split(" ");
out.println("var c=arrCharsets=new Array();");
int j = 0;
for (j=0; j<charsets.length; j++)
{
out.println("c[" + Integer.toString(j) + "]=new Array(2);");
out.println("c[" + Integer.toString(j) + "][0]=\"" + ViewerTools.escapeQuotes(charsets[j]) + "\";");
out.println("c[" + Integer.toString(j) + "][1]=\"" + ViewerTools.escapeQuotes(charsets[j]) + "\";");
}
Enumeration objEnum = objUtils.objChaset.propertyNames();
while (objEnum.hasMoreElements())
{
String strKey = (String)objEnum.nextElement();
String[] values = objUtils.objChaset.getProperty(strKey, "").split(",");
out.println("c[" + Integer.toString(j) + "]=new Array(2);");
out.println("c[" + Integer.toString(j) + "][0]=\"" + ViewerTools.escapeQuotes(values[iIndex]) + "\";");
out.println("c[" + Integer.toString(j) + "][1]=\"" + ViewerTools.escapeQuotes(strKey) + "\";");
j++;
}
out.println("strCharDelimiter=\"" + ViewerTools.escapeQuotes(strCharDelimiter) + "\";");
out.println("strColumnSeparator=\"" + strColumnSeparator + "\";");
out.println("strDefaultCharSet=\"" + strDefaultCharSet + "\";");
out.println("strOptCharSet=\"" + strOptCharSet + "\";");
out.println("useDefaultValues=" + strDefaultValues + ";");
_logger.info("<-- incCSVOptions");
}
catch(Exception e)
{
out.clearBuffer();
objUtils.incErrorMsg(e, out);
}
%>
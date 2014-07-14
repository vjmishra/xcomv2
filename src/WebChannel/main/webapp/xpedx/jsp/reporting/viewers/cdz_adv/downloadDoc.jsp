<%@ include file="wistartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
try
{
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewType = requestWrapper.getQueryParameter("viewType", true);
String strQueryString = requestWrapper.getQueryString();
String strURL = "";
if (strViewType.equalsIgnoreCase("C"))
strURL = "downloadCSV.jsp?";
else if (strViewType.equalsIgnoreCase("COp"))
strURL = "setCSVOptions.jsp?";
else
strURL = "downloadPDForXLS.jsp?";
strURL += ViewerTools.escapeQuotes(strQueryString);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript">
function onInit()
{
self.location.replace("<%=strURL%>");
}
</script>
</head>
<body onload="onInit()">
</body>
</html>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_DOWNLOAD_AS", true, out, session);
}
%>
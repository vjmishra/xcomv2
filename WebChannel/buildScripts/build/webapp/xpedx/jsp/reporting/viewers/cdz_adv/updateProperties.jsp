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
String strEntry = requestWrapper.getQueryParameter("sEntry", false, "");
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
doc.setSelectedReport(nReportIndex);
strEntry = doc.getStorageToken();
%>
<html>
<head>
<script language="javascript">
var strToken = "<%=strEntry%>";
function loadCB() 
{
if(parent.changeEntry)
{ 
parent.changeEntry(strToken)
parent.edit("H")
}
}
</script>
</head>
<body onLoad="loadCB()"></body>
</html>
<%
}
catch(Exception e)
{
System.out.println("Cannot set selected report");
e.printStackTrace();
}
%>
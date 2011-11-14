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
--><%@ page language="java" contentType="text/html;charset=UTF-8" import="com.businessobjects.adv_ivcdzview.*" %>
<%
response.setDateHeader("expires", 0);
try
{
String strQueryString = request.getQueryString();
strQueryString += "&entSession=" + (String)session.getAttribute(ViewerTools.SessionEntSessionName);
%>
<html>
<head>
<script language="javascript">
    var strEntry=null;
function goBack()
{
opener.setReportPartCB();
}
function goInvalidSession()
{
}
function setHelpSystemSection(helpObj, section)
{
}
function initHelpSystem(basePath)
{
return null;
}
function goSelectReportPart(uiref,docId,token)
{
strEntry=token;
opener.setReportPartCB(uiref);
}
</script>
</head>
<frameset rows="100%">
<frame name="picker" src="viewDocument.jsp?<%=strQueryString%>" frameborder="0"  scrolling="auto"/>
</frameset>
</html>
<%
}
catch(Throwable e)
{
out.clearBuffer();
out.println("<html><head><body>");
out.println("<p>" + e.getLocalizedMessage() + "</p>");
out.println("</body></head></html>");
}
%>

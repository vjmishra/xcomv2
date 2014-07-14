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
try
{
_logger.info("-->processClose.jsp");
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String strEntry = requestWrapper.getQueryParameter("sEntry");
if (instanceManager != null)
instanceManager.closeInstance(Integer.parseInt(strViewerID), reportEngines, strEntry, session);
_logger.info("<--processClose.jsp");
}
catch(Exception e)
{
_logger.info("Error when trying to close document: " + e.getLocalizedMessage());
}
%>

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
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
ViewerInstance vi= instanceManager.getViewerInstance(Integer.parseInt(strViewerID));
vi.setActionState("cancel");
String strKey = "CDZ." + strViewerID + ".DocInstance";
if (session.getAttribute(strKey) !=  null)
{
DocumentInstance doc = (DocumentInstance)session.getAttribute(strKey);
doc.cancelQueries();
doc = null;
session.setAttribute(strKey, null);
}
}
catch(Exception e)
{
System.out.println("Cannot cancel query.");
e.printStackTrace();
}
out.println("<html><head><script language=\"javascript\">");
out.println("parent.hideWaitDlg();");
out.println("</script></head><body></body></html>");
%>

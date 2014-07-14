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
--><%@ page isErrorPage="true" language="java" contentType="text/html;charset=UTF-8" import="com.businessobjects.adv_ivcdzview.ViewerTools,java.util.*"%><jsp:useBean id="objUtils" class="com.businessobjects.adv_ivcdzview.Utils" scope="application" /><%
response.setDateHeader("expires", 0);
response.setContentType("text/html;charset=UTF-8");
String strErrMsg = exception.getLocalizedMessage();
String strErrorMsg = objUtils.getFormattedErrorMsg(strErrMsg, "");
String strCdzCatchError = objUtils.getHTMLElement4Trace(strErrMsg, "");
out.println("<html><head>");
out.println("<script language=\"javascript\" src=\"scripts/Utils.js\"></script>");
out.println("<script language=\"javascript\" src=\"lib/dom.js\"></script>");
out.println("<script language=\"javascript\" src=\"language/" + (String)session.getAttribute(ViewerTools.SessionLanguage) + "/scripts/errorManager.js\"></script>");
out.println("<script language=\"javascript\">");
out.println("setTimeout('delayedDisplayDlg()',100);");
out.println("function delayedDisplayDlg()");
out.println("{");
out.println("advDisplayViewerErrorMsgDlg(\"" + ViewerTools.escapeQuotes(strErrorMsg) + "\", _ERR_DOWNLOAD_AS);");
out.println("}");
out.println("</script></head>");
out.println("<body>" + strCdzCatchError + "</body>");
out.println("</html>");
%>
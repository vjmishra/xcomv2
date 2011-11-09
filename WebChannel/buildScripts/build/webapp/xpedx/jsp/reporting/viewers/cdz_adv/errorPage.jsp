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
--><%@ page language="java" contentType="text/html;charset=UTF-8" isErrorPage="true" import="com.businessobjects.adv_ivcdzview.*" %>
<jsp:useBean id="objUtils" class="com.businessobjects.adv_ivcdzview.Utils" scope="application" />
<%
response.setDateHeader("expires", 0);
String strViewerErrorMsgPrefix="VIEWER:";
exception.printStackTrace();
String strErrMsg = "";
try
{
strErrMsg = exception.getLocalizedMessage();
if (strErrMsg.startsWith(strViewerErrorMsgPrefix))
strErrMsg = strErrMsg.substring(strViewerErrorMsgPrefix.length());
}
catch(Exception e) 
{
strErrMsg = "Internal error occured: no error message available.";
}
String strKin = (String)session.getAttribute(ViewerTools.SessionSkin);
if (strKin == null) strKin = "skin_standard";
String strLanguage = (String)session.getAttribute(ViewerTools.SessionLanguage);
if (strLanguage == null) strLanguage = "en";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript" src="lib/dialog.js"></script>
<script language="javascript">
_skinName="<%=strKin%>";
_img="images/main/";
initDom("lib/images/"+_skinName+"/","<%=strLanguage%>");
</script>
<script language="javascript">styleSheet()</script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/errorManager.js"></script>
<script language="javascript">
var strMsg = "<%=ViewerTools.escapeQuotes(objUtils.getFormattedErrorMsg(exception.getLocalizedMessage(), ""))%>";
alertDlg=newAlertDialog("ViewerAlertDlg","","",_ERR_OK_BUTTON_CAPTION,_promptDlgCritical);
function loadCB()
{
alertDlg.init();
advDisplayViewerErrorMsgDlg(strMsg,_ERR_OPEN_DOCUMENT,viewerErrCB,true);
}
function showAlertDialog(msg,title,iPromptType,okCB)
{
alertDlg.setText(msg);
alertDlg.setTitle(title);
alertDlg.setPromptType(iPromptType);
alertDlg.yesCB=(okCB)?okCB:null;
alertDlg.show(true);
}
function wr(s)
{
document.write(s);
}
function viewerErrCB()
{
}
function writeBody()
{
wr('<body onselectstart="return false" ondragstart="return false" style="cursor:default;overflow:hidden" class="bgzone" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">');
alertDlg.write();
wr('<%=objUtils.getHTMLElement4Trace(exception.getLocalizedMessage(), "")%>');
wr('</body>');
}
</script>
</head>
<script language="javascript">
writeBody();
</script>
<script language="javascript">
setTimeout("loadCB()",1);
</script>
</html>

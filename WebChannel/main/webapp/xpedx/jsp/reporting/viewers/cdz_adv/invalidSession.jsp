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
--><%@ page language="java" contentType="text/html;charset=UTF-8"%>
<html>
<head>
<script language="javascript" src="scripts/Utils.js"></script>
<script language="javascript">
topFrame=getTopViewerFrameset();
curLocale=(topFrame!=null)?topFrame._lang:"en";
</script>
<script language="javascript" src="scripts/IVIntegration.js"></script>
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript">
if(topFrame==null)
{
_skinName="skin_standard";
_img="images/main/";
initDom("lib/images/"+_skinName+"/",curLocale);
}
</script>
<script language="javascript" src="lib/dialog.js"></script>
<script language="javascript">
document.write('<sc'+'ript language="javascript" src="language/'+curLocale+'/scripts/errorManager.js\"></sc'+'ript>');
</script>
<script language="javascript">
if(topFrame==null)
styleSheet();
</script>
<script language="javascript">
if(topFrame==null)
{
// Alert dialog box
invalidSessionDlg=newAlertDialog("invSesDlg",_ERR_INVALID_SESSION_TITLE,_ERR_INVALID_SESSION_MSG,_ERR_OK_BUTTON_CAPTION,_promptDlgWarning, invalidSession);
function unloadCB()
{
if (invalidSessionDlg.layer)
invalidSessionDlg.show(false)
}
function loadCB()
{
invalidSessionDlg.init()
invalidSessionDlg.show(true)
}
function wr(s)
{
document.write(s)
}
function writeBody()
{
wr('<body onselectstart="return false" ondragstart="return false" onunload="unloadCB()" style="cursor:default;overflow:hidden" class="bgzone" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">')
// Alert dialog box
invalidSessionDlg.write()
wr('</body>')
}
writeBody();
setTimeout("loadCB()",100);
}
else
topFrame.alertSessionInvalid();
</script>
</script>
</head>
</html>

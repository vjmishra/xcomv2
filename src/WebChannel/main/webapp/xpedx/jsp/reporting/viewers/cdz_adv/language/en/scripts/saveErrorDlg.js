/*
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
*/var promptDlg=newPromptDialog("promptDlg","Save","This document name already exists. Do you want to overwrite it ?","Yes","No",_promptDlgInfo,yesCB,noCB)
var alertDlg=newAlertDialog("alertDlg","Save","","OK",_promptDlgCritical,returnToDocCB)
var queryString=null
var errCode=null
var errMsg=null
if (typeof(_appExt)=="undefined")
_appExt=".jsp";
function setErrCode(p)
{
errCode=p
}
function setErrMsg(p)
{
errMsg=p
}
function setQueryString(p)
{
queryString=p
}
function showPromptDialog()
{
if (errCode == "WIS 30552" || errCode == "RFC 00325")
{
promptDlg.show(true)
return;
}
alertDlg.setText(errMsg)
alertDlg.show(true)
return;
}
function yesCB()
{
document.SaveAsForm.action = "processSave"+_appExt+"?"+queryString+"&overwrite=true";
document.SaveAsForm.submit();
}
function noCB()
{
window.history.back()
}
function returnToDocCB()
{
document.location.replace("viewDocument"+_appExt+"?"+queryString)
}
function unloadCB()
{
if (promptDlg.layer)
promptDlg.show(false)
}
function loadCB()
{
promptDlg.init()
alertDlg.init()
}
function wr(s)
{
document.write(s)
}
function writeDPI()
{
var layer = document.getElementById("DPI");
var dpi = layer.offsetWidth/100;
document.cookie = "DPI=" + dpi.toString();
}
function writeBody()
{
wr('<body onselectstart="return false" ondragstart="return false" onunload="unloadCB()" style="cursor:default;overflow:hidden" class="bgzone" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">')
wr('<div style="top:0px;left:0px;width:100in;height:10px" id="DPI">')
promptDlg.write()
alertDlg.write()
wr('<form name="SaveAsForm" method="post">')
wr('<input type="hidden" name="name" value="">')
wr('<input type="hidden" name="description" value="">')
wr('<input type="hidden" name="keywords" value="">')
wr('<input type="hidden" name="folderId" value="">')
wr('<input type="hidden" name="categories" value="">')
wr('<input type="hidden" name="personalCategories" value="">')
wr('<input type="hidden" name="refreshOnOpen" value="">')
wr('<input type="hidden" name="permanentRegionalFormatting" value="">')
wr('</form>')
wr('</body>')
}
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
*/waitDlg=newWaitDialogBoxWidget("waitDlg",250,150,"Opening Document",false,
null,false,null,true,"Please wait while the document is being processed",true)
function unloadCB()
{
if (waitDlg.layer)
waitDlg.show(false)
}
function loadCB()
{
waitDlg.init()
waitDlg.show(true)
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
waitDlg.write()
wr('</body>')
}
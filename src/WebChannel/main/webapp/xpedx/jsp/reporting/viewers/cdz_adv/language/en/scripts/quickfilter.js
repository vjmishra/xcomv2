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
*/initialized=false
filterDlg=null
cc=new Array 
icc=new Array 
function loadDialog()
{
initDom("lib/images/"+parent._skinName+"/",parent._lang,parent,"quickFilters")
if (!initialized)
{
initialized=true
filterDlg = newDialogBoxWidget("quickfilterDlg","Filter Editor",600,null,okCB,cancelCB,false)
filterDlg.setResize(resizeCB,400,320,false,true)
textFil    = newTextFieldWidget("quickfilterTextFil", null, null, null,applyTextCB,true,"Restrict the available values")
filterList = newListWidget("quickfilterFilterList",filterChangeCB,true,250,10,"List of filters")
addButton  = newButtonWidget("quickfilterAddButton",">>","addCB()",24)
delButton  = newButtonWidget("quickfilterDelButton","<<","delCB()",24)
valueList  = newListWidget("quickfilterValueList",valueChangeCB,true,250,10,"Selected values")
infozone   = newInfoWidget("quickfilterInfozone","","","To select the value(s) you want to display on the report, click the Add button or double-click the value.<br>The list on the right displays the values that filter the report.")
OKButton=newButtonWidget("quickfilterOKButton","OK","okCB()",60)
CancelButton=newButtonWidget("quickfilterCancelButton","Cancel","cancelCB()",60)
RemoveButton=newButtonWidget("quickfilterRemoveButton","Remove","removeCB()",60)
}
if (_curWin.filterInitialized!=true)
{
_curWin.filterInitialized=true
targetApp
(
filterDlg.beginHTML()+
'<table cellspacing="5" cellpadding="0" border="0" width="100%"><tbody>'+
'<tr>'+
'<td align="left" colspan="3">'+
textFil.getHTML()+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
filterList.getHTML()+
'</td>'+
'<td valign="middle" align="center" width="24">'+
addButton.getHTML()+
getSpace(24,5)+
delButton.getHTML()+
'</td>'+
'<td>'+
valueList.beginHTML()+
valueList.endHTML()+
'</td>'+
'</tr>'+
'<tr>'+
'<td align="left" colspan="3">'+
infozone.getHTML()+
'</td>'+
'</tr>'+
'<tr>'+
'<td align="right" colspan="3">'+
'<table cellspacing="0" cellpadding="0" border="0"><tbody><tr>'+
'<td>'+
OKButton.getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'<td>'+
RemoveButton.getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'<td>'+
CancelButton.getHTML()+
'</td>'+
'</tr></tbody></table>'+
'</td>'+
'</tr>'+
'</tbody></table>'+ 
filterDlg.endHTML()
)
}
filterDlg.init()
filterList.init()
addButton.init()
delButton.init()
valueList.init()
infozone.init()
textFil.init()
OKButton.init()
filterList.del()
valueList.del()
for (var i in cc)
{
var s=cc[i]
filterList.add(s,s)
}
for (var i in icc)
{
var s=icc[i]
valueList.add(s,s)
}
grayButtons()
filterDlg.show(true)
}
function isEntryValid(strActionType)
{
if (strActionType == "apply")
{
var oldDoc=_curDoc
_curDoc=document
var l=newListWidget("FV",null,true)
l.init()
for (var i in icc)
l.add("",icc[i],true)
_curDoc=oldDoc
}
var  s="processFilters.jsp"+parent.urlParams(true)
document.forms.theForm.action=s+"&sAction="+strActionType
setTimeout("document.forms.theForm.submit()",1)
}
function fillFilterList(restrict)
{
restrict=restrict.toLowerCase()
filterList.del()
for (var i in cc)
{
var s=cc[i]
if ((restrict=="")||(s.toLowerCase().indexOf(restrict)>=0))
filterList.add(s,s)
}
}
function existsValue(s)
{
var count=icc.length
for (var i=0;i<count;i++)
{
if (icc[i]==s)
return true
}
return false
}
function applyTextCB()
{
fillFilterList(textFil.getValue())
}
function okCB()
{
filterDlg.show(false)
isEntryValid("apply")
}
function removeCB()
{
filterDlg.show(false)
isEntryValid("remove")
}
function cancelCB()
{
filterDlg.show(false)
}
function addCB()
{
var sel=filterList.getMultiSelection()
for (var i in sel)
{
var v=sel[i].value
if (!existsValue(v))
{
valueList.add(v,v)
icc[icc.length]=v
}
}
grayButtons()
}
function grayButtons()
{
OKButton.setDisabled(icc.length==0)
}
function delCB()
{
var sel=valueList.getMultiSelection(),len=sel.length
for (var i=len-1;i>=0;i--)
{
var j=parseInt(sel[i].value)
valueList.del(i)
arrayRemove(self,"icc",i)
}
grayButtons()
}
function resizeCB(w,h)
{
filterList.resize((w-58)/2)
valueList.resize((w-58)/2)
}
function filterChangeCB()
{
}
function valueChangeCB()
{
}

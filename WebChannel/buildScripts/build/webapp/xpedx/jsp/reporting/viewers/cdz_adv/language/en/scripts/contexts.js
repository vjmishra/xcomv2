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
*/function displayErrorMsg(strMsg)
{
topfs.hideWaitDlg();
topfs.showAlertDialog(strMsg,"Contexts",2);
}
var contextDlg = null;
var info = null;
var backButton = null;
var nextButton = null;
var okButton = null;
var cancelButton = null;
var helpButton = null;
var ctxList = null;
var ctxGroup = null;
var ctxGroupMulti = null;
var curCtxGroup = null;
var ctxDesc = null;
var ctxZone = null;
var ctxGroupZone = null;
var ctxDescZone = null;
var dlgWidth= 500;
var dlgHeight= 400;
var initialized= false;
var _contextIndex= 0
var _contexts = new Array
function loadDialog()
{
if (!initialized)
{
initialized=true;
contextDlg = newDialogBoxWidget("contextDlg","Contexts",570,400,okCB,cancelCB,false);
ctxZone=newFrameZoneWidget("ctxZone",300,210)
ctxGroupZone=newFrameZoneWidget("ctxGroupZone",300,200)
ctxDescZone=newFrameZoneWidget("ctxDescZone",300,80,true)
ctxList= newIconListWidget("ctxList",200,200,topfs._img+"formula.gif",ctxListCB,null);
ctxList.iconOrientVertical=false
ctxGroup= newListWidget("ctxGroup",ctxGroupCB,false,300,7,null);
ctxGroupMulti= newListWidget("ctxGroupMulti",ctxGroupCB,true,300,7,null);
ctxDesc= newWidget("ctxDesc");
ctxAction= newWidget("ctxAction");
info = newInfoWidget("infoContext","More Information","","To avoid ambiguities in a query, contexts may be used to implement restrictions on how the objects can be combined. You need to select which context corresponds to the type of information you want to return to reports before you can run the query.")
okButton = newButtonWidget("okButton", "Apply Context", "okCB()", 50)
cancelButton = newButtonWidget("cancelButton", "Cancel","cancelCB()", 50)
helpButton = newButtonWidget("helpButton", "Help", "helpCB()", 50)
backButton = newButtonWidget("backButton", "&lt;&nbsp;Back", "backCB()", 50)
nextButton = newButtonWidget("nextButton", "Next&nbsp;&gt;", "nextCB()", 50)
}
if ( !_curWin.contextDlgInitialized )
{
_curWin.contextDlgInitialized = true;
targetApp(
contextDlg.beginHTML() +
'<table cellspacing="0" cellpadding="5" border="0" class="dialogzone"><tbody>'+
'<tr><td id="ctxAction" tabIndex="0">Select context(s) for the queries.</td></tr>'+
'<tr>'+
'<td width="100%">'+
ctxZone.beginHTML()+
'<table cellspacing="0" cellpadding="5" border="0" class="dialogzone"><tbody>'+
'<tr><td>'+
ctxList.getHTML()+
'</td><td valign="top" height="100%">'+
ctxGroupZone.beginHTML()+
'<table cellspacing="0" cellpadding="5" border="0" class="dialogzone"><tbody>'+
'<tr><td>'+
'<div id="ctxTab">'+
ctxGroup.getHTML()+
'</div>'+
'<div id="ctxMultiTab" style="display:none">'+
ctxGroupMulti.getHTML()+
'</div>'+
'</td></tr><tr><td>'+
ctxDescZone.beginHTML()+
'<div id="ctxDesc" tabIndex="0">Context description: </div>'+
ctxDescZone.endHTML()+
'</td></tr>'+
'</tbody></table>'+
ctxGroupZone.endHTML()+
'</td></tr>'+
'</tbody></table>'+
ctxZone.endHTML()+
'</td>' +
'</tr>' +
'<tr>' +
'<td align="center" valign="right">' +
info.getHTML() +
'</td>' +
'</tr>' +
'<tr>' +
'<td align="right" valign="center">' +
'<table cellspacing="0" cellpadding="0" border="0"><tbody><tr>' +
'<td>' +
backButton.getHTML() +
'</td>' +
'<td>' + getSpace(5,1)+ '</td>' +
'<td>' +
nextButton.getHTML() +
'</td>' +
'<td>' + getSpace(5,1)+ '</td>' +
'<td>' +
okButton.getHTML() +
'</td>' +
'<td>' + getSpace(5,1)+ '</td>' +
'<td>' +
cancelButton.getHTML() +
'</td>' +
'<td>' + getSpace(5,1)+ '</td>' +
'<td>' +
helpButton.getHTML() +
'</td>' +
'</tr></tbody></table>'+
'</td>' +
'</tr>' +
'</tbody></table>' +
contextDlg.endHTML()
);
}
contextDlg.init();
info.init();
okButton.init();
contextDlg.attachDefaultButton(okButton);
cancelButton.init();
helpButton.init();
helpButton.setDisplay(false)
backButton.init();
nextButton.init();
backButton.setDisplay(false)
nextButton.setDisplay(false)
ctxList.init();
ctxGroup.init();
ctxGroupMulti.init();
ctxDesc.init();
ctxAction.init();
ctxZone.init();
ctxGroupZone.init();
ctxDescZone.init();
fillContexts();
var s="Select context(s) for the queries."
if(_docName!="") s+="'"+"<b>"+convStr(_docName)+"</b>'"
ctxAction.setHTML(s);
topfs.hideWaitDlg();
contextDlg.show(true);
}
function fillContexts()
{
ctxList.deleteAll();
var len = _contexts.length, cpt=0, prevCtx=(len>0)?_contexts[0].name:null
for(var i=0;i<len;i++)
{
var ctx=_contexts[i]
if(prevCtx == ctx.name )
cpt++
else
{
cpt=1
prevCtx = ctx.name
}
var id=ctx.name+".Context "+cpt
var elem=newTreeWidgetElem(ctx.isFilled()?2:1,id,i,null,null,null,topfs.getIconAlt(topfs._FORMULA_IMG,ctx.isFilled()?2:1))
elem.advTooltip=getCtxAdvTooltip(ctx);
ctxList.add(elem)
}
ctxList.rebuildHTML();
selectCtx()
updateBtn()
}
function getCtxAdvTooltip(ctx)
{
var sel=ctx.selectedvalues
var advTooltip=""
if(sel.length>0)
{
var elem=ctxList.getSelectedItem()
if (elem!=null)
{
advTooltip="Context equal to  "
for(var j=0;j<sel.length;j++)
{ 
if(j>0) elem.advTooltip+=';'
advTooltip+=sel[j]
}
}
}
else
advTooltip="No selected context "
return advTooltip;
}
function changeIcon()
{
var ctx=_contexts[_contextIndex]
if (ctx.isFilled())
ctxList.getSelectedItem().change(2)
else
ctxList.getSelectedItem().change(1)
updateBtn()
}
function updateContextDesc(desc)
{
if(!desc)
desc="Context description: "
else
desc="Context description:  \n" + desc
ctxDesc.setHTML(convStr( desc))
}
function ctxListCB()
{
var o=this
_contextIndex = o.getSelection().index
var ctx=_contexts[_contextIndex]
var len= ctx.allvalues.length
curCtxGroup=(ctx.isMulti)?ctxGroupMulti:ctxGroup
_curDoc.getElementById("ctxTab").style.display=(ctx.isMulti)?'none':''
_curDoc.getElementById("ctxMultiTab").style.display=(ctx.isMulti)?'':'none'
curCtxGroup.del()
for(var i=0;i <len; i++)
{
curCtxGroup.add(ctx.allvalues[i],ctx.allvalues[i])
}
setCtxValues(_contextIndex)
updateBtn()
}
function ctxGroupCB()
{
saveCtxValues()
var ctx=_contexts[_contextIndex], isFilled = ctx.isFilled()
var o=this
changeIcon()
if(isFilled)
{
index = o.getSelection().index
updateContextDesc(ctx.alldescr[index])
}
}
function saveCtxValues()
{
var ctx=_contexts[_contextIndex]
ctx.selectedvalues.length=0 
var valuesSelected = curCtxGroup.getMultiSelection()
for(var j=0; j< valuesSelected.length;j++)
{
ctx.selectValue(valuesSelected[j].value);
}
var selItem = ctxList.getSelectedItem();
selItem.advTooltip=getCtxAdvTooltip(ctx);
}
function setCtxValues(index)
{
if(index!= null)  _contextIndex=index;
var ctx=_contexts[_contextIndex]
var selectedvalues =ctx.selectedvalues, len=selectedvalues.length
if(len>0)
{
for(var j=0; j<len;j++)
{
curCtxGroup.valueSelect(selectedvalues[j]);
}
}
if(len == 1 )
{
var index = curCtxGroup.getSelection().index
updateContextDesc(ctx.alldescr[index])
}
else
updateContextDesc()
}
function selectCtx(index)
{
if(index!= null)  _contextIndex=index;
ctxList.select(_contextIndex)
}
function updateBtn()
{
if(_contextIndex == 0) backButton.setDisabled(true)
else backButton.setDisabled(false)
if(_contextIndex == _contexts.length -1 ) nextButton.setDisabled(true)
else nextButton.setDisabled(false)
var len = _contexts.length
var finish= true
for(var i=0;i<len;i++)
{
if (!_contexts[i].isFilled())
{
finish = false
break;
}
}
if(finish) okButton.setDisabled(false)
else okButton.setDisabled(true)
}
function okCB()
{
fillContextsForm();
contextDlg.show(false);
var url="processContexts"+topfs._appExt;
if(strSRC!="RPV")
{
url+=topfs.urlParamsNoBID(strEntry,null,null,null,null,1)+"&sNEV="+strNEV;
url+="&bViewSQL="+strViewSQL+"&sDPName="+topfs.convURL(strDPName); 
url+="&sNewDoc="+strNewDoc+"&viewType="+strViewType+"&sApplyFormat="+strApplyFormat+"&iDPIndex="+strDPIndex;
url+= "&bSaveQuery=" + strSaveQuery +"&iAction="+strAction
}
else
url+="?sEntry="+strEntry+"&iViewerID="+topfs.iViewerID+"&src="+strSRC+"&sNEV="+strNEV;
url +="&bCreateDefaultReportBody="+topfs.bCreateDefaultReportBody
url += "&defaultRepTitle="+convURL(topfs._defaultRepTitle)
self.document.contextsForm.action = url;
document.contextsForm.submit();
if(strSRC!="RPV")
{
if(topfs._showQueryPanel)
topfs.eventManager.notify(topfs._EVT_DOC_UPDATE);
else
topfs.eventManager.notify(topfs._EVT_REFRESH_DATA);
var bChangeSrc = (strAction == "3");
if ((strViewSQL == "false") && (strSaveQuery != "true") && (!bChangeSrc))
topfs.launchRefreshWaitDlg("Contexts, please wait",true,true,true);
}
else
parent.showWaitDlg();
}
function cancelCB()
{
contextDlg.show(false);
if (strNEV=="yes")
{
parent.backToParent();
}
else if (strCancel!="")
{
var previousURL=parent.allUseDictionary.get(strCancel);
parent.frameNav("Report",previousURL);
}
}
function backCB()
{
selectCtx(_contextIndex-1);
updateBtn();
}
function nextCB()
{
selectCtx(_contextIndex+1);
updateBtn();
}
function helpCB()
{
contextDlg.show(false);
}
function newBOContext(name, selection)
{
var o=new Object;
o.name=name;
o.selection=selection;
o.allvalues=new Array;
o.alldescr=new Array;
o.selectedvalues=new Array;
o.isMulti=false;
o.addValue=newBOContext_addValue
o.addDescr=newBOContext_addDescr
o.selectValue=newBOContext_selectValue
o.isFilled=newBOContext_isFilled
return o;
}
function newBOContext_addValue(v,i){arrayAdd(this,'allvalues',v,i)}
function newBOContext_addDescr(v,i){arrayAdd(this,'alldescr',v,i)}
function newBOContext_selectValue(v,i){arrayAdd(this,'selectedvalues',v,i)}
function newBOContext_isFilled( )
{
var o=this
if(o.selectedvalues.length > 0 )
return true
else
return false
}

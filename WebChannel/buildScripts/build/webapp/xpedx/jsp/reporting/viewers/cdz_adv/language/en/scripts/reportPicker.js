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
*/isInteractive=false;
blnAllowPicker=false;
_selectedInstance=null;
_selectedIdRef=null;
_reportPickerSaveBefore=false;
_reportPartMenu=null;
function pickerContextMenu(e)
{
if (_saf)
this.onmousedown(e)
if (_reportPartMenu==null)
{
_reportPartMenu=newMenu("reportPartMenu");
_reportPartMenu.add("selectReportPart","Select this report part");
}
var o=this,bid=o.getAttribute("bid"),fr=getReportFrame();
var elt=findByBID(doc,bid);
var cn=elt?elt.className:"";
if ((cn=="cell")||(cn=="reportCell")||(cn=="block")||(cn=="tableCell")||(cn=="section"))
{
if (selectedBid[bid]!=null && selectedBid[bid]==1)
{
incReportPickerContextMenu(o,e);
}
}
return false;
}
function incReportPickerContextMenu(l,e)
{
if (!isEnableUserRight(_usrShowRightClickMenu))
return
var w=getReportFrame();
if (w==null) return;
if (_ie)
e=w.event;
var bid=l.getAttribute("bid");
setTimeout('showReportpickerContextMenu("'+bid+'",'+ (_saf? (absxpos(e) - getScrollX(w)):absxpos(e)) + ',' + (_saf? (absypos(e) - getScrollY(w)):absypos(e)) + ')',1);
e.cancelBubble=true;
return false;
}
function showReportpickerContextMenu(bid,x,y)
{
var pos=getPos(reportBorder.layer)
x=x+pos.x;
y=y+pos.y;
_reportPartMenu.show(true,x,y);
_currContextMenu=_reportPartMenu;
}
function partContextMenu(e)
{
if (_reportPartMenu==null)
{
_reportPartMenu=newMenu("reportPartMenu");
_reportPartMenu.add("unSelectReportPart","Unselect this report part");
}
var o=this;
incReportPartContextMenu(o,e);
return false;
}
function incReportPartContextMenu(l,e)
{
if (_ie)
e=self.event;
setTimeout('showReportPartContextMenu('+ (_saf? (absxpos(e) - getScrollX(w)):absxpos(e)) + ',' + (_saf? (absypos(e) - getScrollY(w)):absypos(e)) + ')',1);
e.cancelBubble=true;
return false;
}
function showReportPartContextMenu(x,y)
{
x=x+getScrollX(window);
y=y+getScrollY(window);
_reportPartMenu.show(true,x,y);
_currContextMenu=_reportPartMenu;
}
function reportPickerClickCB(o,id)
{
switch(id)
{
case "selectReportPart":
var elts=getSelectedElts();
if (elts.length==1)
{
if (!_dontCloseDoc && isDocModified())
{
var msg="The document has changed. The changes must be saved before selecting this report part. Do you want to continue?";
showPromptDialog(msg,"Save",_promptDlgInfo,reportPartYesCB,reportPartNoCB);
}
else
goSelectReportPart();
}
break;
default:
return false;
}
return true;
}
function goSelectReportPart(uiref,docId,token)
{
if (typeof(uiref)!="undefined")
{
if (!_doNotHideOnLoadReport)
hideBlockWhileWaitWidget();
if (uiref=='')
{
var msg="Internal error: cannot select this report part. The server has returned an empty Report Part ID.";
showAlertDialog(msg,_ERR_DEFAULT,_promptDlgCritical);
return;
}
var myFrame=self;
    var i=0,iMaxLoop=100;
while (i<iMaxLoop)
{
if (myFrame.parent && myFrame!=myFrame.parent)
{
myFrame=myFrame.parent;
if (myFrame.goSelectReportPart)
{
var sCBParam=(typeof(sCallbackParam)!="undefined")?sCallbackParam:null;
myFrame.goSelectReportPart(uiref,docId,token,sCBParam);
break;
}
    }
else
break;
i++;
}
}
else
{
var p=urlParamsNoBID();
if (p!="" && _selectedIdRef!=null)
{
wt();
blnAllowPicker=true;
frameNav("DlgFrame","getReportPart"+_appExt+p+"&refId="+_selectedIdRef);
}
}
}
function reportPartYesCB()
{
if (_usrSaveDoc=="none")
{
var msg="You do not have sufficient rights to save this document.";
showAlertDialog(msg,"Save",_promptDlgCritical);
}
else
{
_reportPickerSaveBefore=true;
_dontCloseDoc=true;
saveOrSaveAs();
}
}
function reportPartNoCB()
{
}
reportPickerSaveEvent=newObserverOneEvent(_EVT_PROCESS_SAVE,reportPickerSaveEventCB);
reportPickerQuickSaveEvent=newObserverOneEvent(_EVT_PROCESS_QUICK_SAVE,reportPickerSaveEventCB);
function reportPickerSaveEventCB()
{
if (_reportPickerSaveBefore)
{
if (!_doNotHideOnLoadReport)
hideBlockWhileWaitWidget();
goSelectReportPart();
_reportPickerSaveBefore=false;
}
}
reportPickerPageLoadedEvent=newObserverOneEvent(_EVT_PAGE_LOADED,reportPickerPageLoadedCB);
function reportPickerPageLoadedCB()
{
_selectedInstance=null;
_selectedIdRef=null;
}
function getIdRef4ReportPicker(o)
{
return o.getAttribute("idref");
}
function pickerClickCB(e)
{
globResizeObj=null;
globMoveObj=null;
if (_moz)
{
tabEdit_lostFocus();
fEdit_lostFocus();
}
_tableBid=null;
var o=this,bid=o.getAttribute("bid"),fr=getReportFrame();
if (fr==null) return;
var d=fr.document;
if (_ie)
e=fr.event;
var ctrl=e.ctrlKey||e.shiftKey;
var elt=findByBID(doc,bid);
var cn=elt?elt.className:"";
if ((cn=="cell")||(cn=="reportCell")||(cn=="block")||(cn=="section"))
{
var isSelected=(selectedBid[bid]!=null && selectedBid[bid]==1)?true:false;
deselectAll(bid);
pickerUnselect();
if (isSelected && ctrl)
selectedBid[bid]=null;
else
{
selectedBid[bid]=1;
pickerSelect(o);
}
e.cancelBubble=true;
}
else if (cn!="tableCell")
{
pickerUnselect();
}
simulateClick(e);
return false;
}
function pickerSelectObjInstance(o,sel,bid,elt)
{
selectObjInstance(o,sel,bid,false,elt);
var cn=(elt)?elt.className:"";
if (cn=="block")
{
var n=o.childNodes,len=n.length;
for (var i=0;i<len;i++)
{
pickerSelectTD(n[i],sel);
}
}
}
function pickerSelectTD(o,sel)
{
if (o.tagName=="TD")
{
var bid=o.getAttribute("bid");
if (bid!=null)
{
var elt=findByBID(doc,bid);
selectObjInstance(o,sel,bid,false,elt);
}
}
else
{
var n=o.childNodes,len=n.length;
for (var i=0;i<len;i++)
{
pickerSelectTD(n[i],sel);
}
}
}
function pickerSelect(o)
{
var bid=o.getAttribute("bid"),elt=findByBID(doc,bid);
_selectedIdRef=getIdRef4ReportPicker(o);
_selectedInstance=o;
pickerSelectObjInstance(o,true,bid,elt);
}
function pickerUnselect(exceptLayer)
{
if (_selectedInstance)
{
if ((exceptLayer==null)||(getIdRef4ReportPicker(exceptLayer)!=_selectedIdRef))
{
var bid=_selectedInstance.getAttribute("bid"),elt=findByBID(doc,bid);
if (bid!=null && elt!=null)
pickerSelectObjInstance(_selectedInstance,false,bid,elt);
}
}
_selectedInstance=null;
_selectedIdRef=null;
}
function pickerOverCB(e)
{
var o=this,bid=o.getAttribute("bid"),fr=getReportFrame();
var elt=findByBID(doc,bid);
var cn=elt?elt.className:"";
if ((cn=="cell")||(cn=="reportCell")||(cn=="block")||(cn=="tableCell")||(cn=="section"))
{
o.oldTitle=o.title
o.title="Right click to select this block as linked report part"
}
o.tempMover=mover
o.tempMover(e)
o.tempMover=null
}
function pickerOutCB(e)
{
var o=this
if (o.oldTitle)
o.title=o.oldTitle
}
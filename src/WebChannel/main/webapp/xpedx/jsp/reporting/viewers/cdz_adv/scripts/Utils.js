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
*/function getQueryParamValue(strQueryString, strParamName)
{
var strRet = "";
if (strQueryString != null)
{
var arrQueryParam = strQueryString.split('&');
for (var i=0;i<arrQueryParam.length;i++)
{
var strName = arrQueryParam[i];
var strValue = "";
var iPos = arrQueryParam[i].indexOf('=');
if (iPos>=0)
{
strName = arrQueryParam[i].substring(0, iPos);
strValue = arrQueryParam[i].substring(iPos + 1);
}
if (strParamName == strName)
{
strRet = strValue;
break;
}
}
}
return strRet;
}
function updateQueryParameter(strQueryString, strParamName, strParamValue)
{
if (strQueryString != null)
{
var arrQueryParam = strQueryString.split('&');
for (var i=0;i<arrQueryParam.length;i++)
{
var strName = arrQueryParam[i];
var iPos = arrQueryParam[i].indexOf('=');
if (iPos>=0) strName = arrQueryParam[i].substring(0, iPos);
if (strParamName == strName)
{
arrQueryParam[i] = strParamName + "=" + strParamValue;
strQueryString = arrQueryParam.join('&');
break;
}
}
}
return strQueryString;
}
function removeQueryParameter(strQueryString, strParamName)
{
var i=0;
if (strQueryString != null)
{
var arrQueryParam = strQueryString.split('&');
for (i=0;i<arrQueryParam.length;i++)
{
var strName = arrQueryParam[i];
var iPos = arrQueryParam[i].indexOf('=');
if (iPos>=0) strName = arrQueryParam[i].substring(0, iPos);
if (strParamName == strName)
{
arrQueryParam[i] = "";
break;
}
}
i=0;
var arrFinalQueryParam = new Array();
for (var j=0;j<arrQueryParam.length;j++)
{
if (arrQueryParam[j] != "")
{
arrFinalQueryParam[i]=arrQueryParam[j];
i++;
}
}
strQueryString = arrFinalQueryParam.join('&');
}
return strQueryString;
}
function startsWithIgnoreCase(strString, strToFind)
{
var blnRet = false;
if (strToFind != null)
{
var strVar = strString.substring(0, strToFind.length);
if (strVar.toLowerCase() == strToFind.toLowerCase())
blnRet = true;
}
return blnRet;
}
function endsWithIgnoreCase(strString, strToFind)
{
var blnRet = false;
if (strToFind != null)
{
var iRight = strString.length- strToFind.length;
if (iRight >= 0)
{
var strVar = strString.substring(iRight);
if (strVar.toLowerCase() == strToFind.toLowerCase())
blnRet = true;
}
}
return blnRet;
}
function trim(strValue)
{
if (strValue != null)
{
var i=0;
for (i=0; i<strValue.length; i++)
{
if (strValue.charAt(i)!=' ')
break;
}
strValue=strValue.substring(i);
for (i=strValue.length-1; i>=0; i--)
{
if (strValue.charAt(i)!=' ')
break;
}
return strValue.substring(0, i+1);
}
}
function getTopViewerFrameset(f)
{
if(f == null)
f = self
if(f._topfs=="topfs")
{
return f;
}
else
{
if(f!= top)
return getTopViewerFrameset(f.parent)
else
return null;
}
}
function setKeyCB(win, isReportFr)
{
var locWin = win?win:_curWin
theCB = docKeyCB
if (isReportFr) {
theCB = reportKeyCB
} else if (win) {
theCB = paneKeyCB
}
if (_ie) 
{
locWin.document.onkeydown= theCB;
} 
else 
{
locWin.document.addEventListener('keydown', theCB, false);
}
}
function docKeyCB(e)
{
if (_ie) {
e = _curWin.event
if (e == null) {
return true
}
}
else
{
return reportKeyCB(e)
}
if (isTextInput(e)) return true;
e.cancelBubble=true
return processKeyEvent(e, false)
}
function reportKeyCB(e)
{
if (_ie) {
e = getReportFrame().event
if (e == null) {                             
return true
}           
}
var oldCurWin = _curWin
_curWin=getReportFrame()
if (isTextInput(e)) 
{
_curWin = oldCurWin
return true;
}
_curWin = oldCurWin                 
e.cancelBubble=true      
return processKeyEvent(e, true)
}
function paneKeyCB(e)
{
if (_ie) {
e = leftPane.getFrame().event
if (e == null) {
return true
}
}
if (isTextInput(e)) return true;
e.cancelBubble=true
return processKeyEvent(e, false)
}
function preventDef(e)
{
if (e.preventDefault)
e.preventDefault()
if (e.stopPropagation)
e.stopPropagation()
e.cancelBubble=true
e.returnValue=false
}
function processKeyEvent(e, isReportFr)
{
var key = e.keyCode
if ((!_mac&&e.ctrlKey)||(_mac&&e.metaKey)) {
if (isReportFr&&isInteractive) {
switch(key) {
case 67 : 
copyCutPasteCB("copy")
preventDef(e)
return false
case 86 : 
copyCutPasteCB("paste")
preventDef(e)
return false
case 88 : 
copyCutPasteCB("cut")
preventDef(e)
return false
case 68 : 
removeReportElement()
preventDef(e)
return false
}
}
switch(key) {
case 90 : 
setClickCBID("undo")
clickCB()
preventDef(e)
return false
case 89 : 
setClickCBID("redo")
clickCB()
preventDef(e)
return false
case 83 : 
if (window.saveDocIcn) {
setClickCBID("saveDoc")
clickCB()
preventDef(e)
return false
}
break
}
} 
switch(key){
case 8: 
e.returnValue=false
preventDef(e)
return false
case 27:
eventManager.notify(_EVT_ESC_KEY)
preventDef(e)
return false
case 46 : 
if (isInteractive&&isReportFr && window.removeBut && !removeBut.isDisabled())
{
if (isInteractive&&window._fEditIsShown!=true)
{
removeReportElement()
preventDef(e)
return false
}
else
return true
}
default:
return true
}
}
function newReportInfo(name,pageMode,repMode,nbPage,page,isLeaf,zoom,scrX,scrY,tdcActivate)
{
var o=new Object
o.name=name
o.pageMode=pageMode
o.repMode=repMode
o.nbPage=nbPage
o.curPage=page
o.isLeaf=isLeaf
o.zoom=(zoom!=null)?zoom:100
o.scrollX=(scrX!=null)?scrX:0
o.scrollY=(scrY!=null)?scrY:0
o.tdcActivate=tdcActivate?tdcActivate:false
return o
}
function newViewerState(entry,viewerID,selRep,arrRep)
{
var o=new Object
o.sEntry=entry
o.iViewerID=viewerID
o.selRep=selRep
o.arr=new Array
var rp=null
for(var i in arrRep)
{
rp=arrRep[i]
o.arr[i]=newReportInfo(rp.name,rp.pageMode,rp.repMode,rp.nbPage,
rp.curPage,rp.isLeaf,rp.zoom,rp.scrollX,rp.scrollY,rp.tdcActivate);
}
return o
}
function newHashTable()
{
var o=new Object
o.arrKeys=new Array();
o.arrValues=new Array();
o.clear=newHashTable_clear
o.containsKey=newHashTable_containsKey
o.get=newHashTable_get
o.isEmpty=newHashTable_isEmpty
o.keys=newHashTable_keys
o.put=newHashTable_put
o.remove=newHashTable_remove
o.size=newHashTable_size
o.values=newHashTable_values
return o
}
function newHashTable_clear()
{
var o=this
o.arrKeys=new Array()
o.arrValues=new Array()
}
function newHashTable_containsKey(sKey)
{
var o=this
var blnRet=false
for (var i=0; i<o.arrKeys.length; i++)
{
if (o.arrKeys[i]==sKey)
{
blnRet=true
break
}
}
return blnRet
}
function newHashTable_get(sKey)
{
var o=this
var sRet=null
for (var i=0; i<o.arrKeys.length; i++)
{
if (o.arrKeys[i]==sKey)
{
sRet=o.arrValues[i]
break
}
}
return sRet
}
function newHashTable_isEmpty()
{
var o=this
var blnRet=(o.arrKeys.length==0)?true:false
return blnRet
}
function newHashTable_keys()
{
var o=this
var arr=new Array()
for (var i=0; i<o.arrKeys.length; i++)
{
arr[i]=o.arrKeys[i]
}
return arr
}
function newHashTable_put(sKey,value)
{
var o=this
var nb=o.arrKeys.length
var iIndex=-1
for (var i=0; i<nb; i++)
{
if (o.arrKeys[i]==sKey)
{
iIndex=i
break
}
}
if (iIndex>-1)
    o.arrValues[iIndex]=value
else
{
if (sKey!=null)
{
o.arrKeys[nb]=sKey
o.arrValues[nb]=value
}
}
}
function newHashTable_remove(sKey)
{
var o=this
for (var i=0; i<o.arrKeys.length; i++)
{
if (o.arrKeys[i]==sKey)
{
o.arrKeys.splice(i,1)
o.arrValues.splice(i,1)
}
}
}
function newHashTable_size()
{
return this.arrKeys.length
}
function newHashTable_values()
{
var o=this
var arr=new Array()
for (var i=0; i<o.arrValues.length; i++)
{
arr[i]=o.arrValues[i]
}
return arr
}
function decodeEmptyValue(s)
{
while(s.indexOf(_emptyValueLab)!=-1)
{
s=s.replace(_emptyValueLab,"");
}
return s;
}
function encodeEmptyValue(s,isIndexAware)
{
if(s==null) return "";
var separator=";";
var arr=s.split(separator),len=arr.length;
var ret=new Array,cpt=0,pos=-1,value,idx;
for(var i=0;i<len;i++)
{
if(isIndexAware)
{
pos = arr[i].indexOf('_');
if(pos>-1)
{
idx=arr[i].substring(0,pos+1);
value=arr[i].substring(pos+1);
}
if(value=="")
{
arr[i]=idx+_emptyValueLab;
}
ret[cpt++]=arr[i];
}
else
{
ret[cpt++]=((arr[i]=="")?_emptyValueLab:arr[i]);
}
}
return ret.join(separator);
}
function processFreeValues(s)
{
var separator=";";
if(s=="" || s==separator) return "";
s=s.replace(/;;/g,";"+_emptyValueLab+";");
var arr1 = s.split(separator), len = arr1.length; 
var arr2=new Array,cpt=0;
for(var i=0;i<len;i++)
{
if(arr1[i]!="")
arr2[cpt++]=arr1[i]
}
return arr2.join(separator);
}
function newEventManager()
{
var o=new Object
o.arrObs=new Array
o.attach=EventManager_attach
o.notify=EventManager_notify
o.detach=EventManager_detach
return o
}
function EventManager_attach(obs)
{
var ar=this.arrObs
for (var i=0;i<ar.length;i++)
{
if (ar[i]==obs)
return
}
ar[ar.length]=obs
}
function EventManager_notify(evt,data)
{
var ar=this.arrObs
for (var i=0;i<ar.length;i++)
ar[i].update(evt,data)
}
function EventManager_detach(obs)
{
var ar=this.arrObs
var i
var bFound=false
for (i=0;i<ar.length;i++)
{
if (ar[i]==obs)
{
bFound=true
break;
}
}
if (bFound)
arrayRemove(this,"arrObs",i)
}
function newObserverOneEvent(evt,cb)
{
var o=new Object
o.evt=evt
o.cb=cb
o.update=ObserverOneEvent_update
eventManager.attach(o)
return o
}
function ObserverOneEvent_update(evt,data)
{
var o=this
if (evt==o.evt)
{
if ( typeof(o.cb) != 'undefined' )
o.cb(evt,data);
}
}
function newObserverTwoEvents(evt1,evt2,cb)
{
var o=new Object
o.evt1=evt1
o.evt2=evt2
o.data1=null
o.evt1Complete=false
o.cb=cb
o.update=ObserverTwoEvents_update
eventManager.attach(o)
return o
}
function ObserverTwoEvents_update(evt,data)
{
var o=this
if (evt==o.evt1)
{
o.data1=data
o.evt1Complete=true
return
}
if ((evt==o.evt2) && (o.evt1Complete))
{
o.evt1Complete=false
if ( typeof(o.cb) != 'undefined' )
o.cb(o.evt1,o.data1);
}
}
function listViewerEvents()
{
var t=0
_EVT_HTML_WRITTEN= t++
_EVT_DOC_UPDATE= t++
_EVT_WOM_LOADED= t++
_EVT_SELECTION_CHANGES= t++
_EVT_PAGE_LOADED= t++
_EVT_REFRESH_DATA= t++
_EVT_PURGE_DATA= t++
_EVT_FETCH_DATA= t++
_EVT_LEFTCOMBO_CHANGED= t++
_EVT_BEFORE_TOKEN_UPDATE= t++
_EVT_PDF_VIEW= t++
_EVT_VARS_UPDATE= t++
_EVT_REP_ADDED= t++
_EVT_REP_RENAMED= t++
_EVT_REP_DELETED= t++
_EVT_REP_MOVED= t++
_EVT_REP_DUPLICATED= t++
_EVT_REP_DATAOK= t++
_EVT_REP_SELECTED= t++
_EVT_UNDO_REDO= t++
_EVT_DRILL_ON= t++
_EVT_DRILL_OFF= t++
_EVT_DRILL_ACTION= t++
_EVT_PROMPTS_LOV_REFRESHED= t++
_EVT_PROMPTS_VALUES_CHANGED= t++
_EVT_REINIT_REPORTMAP_NODE= t++
_EVT_DOC_CLOSED= t++
_EVT_ZOOM_CHANGED= t++
_EVT_CUSTOMSORT_ACTION= t++
_EVT_SAVE_DUPLICATE_DOCNAME= t++
_EVT_PROCESS_SAVE= t++
_EVT_PROCESS_QUICK_SAVE= t++
_EVT_ESC_KEY= t++
_EVT_REP_TDC_ACTIVATED= t++
_EVT_TDC_CHANGED= t++
_EVT_SAVE_NONAUTH_DIR= t++
_EVT_DOC_PROPERTIES= t++
_EVT_RESTORE_AFTER_ERR= t++
}

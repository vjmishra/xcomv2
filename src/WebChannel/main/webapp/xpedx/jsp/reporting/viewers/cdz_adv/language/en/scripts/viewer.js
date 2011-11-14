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
*/setTopFrameset()
_curIdRef=null
_curIdRefBid=null
_dpi=96
subHelpObj=null
_initialTabMaxL=null
_bDrillMode = false;
_panesInitalized=false
isPageLoaded=false
isWOMLoaded=false
leftPaneX=0
leftPaneY=0
_unitLabel = _unitIsInch ? 'inches':'cm'
_findAvailable=_ie||window.find
if (bFullScreen)
_showLeftPane=false
_displayedModeIsPDF=isPDF
function createBorderElement(id,x,y,w,h,url,border)
{
return '<div id="'+id+'" style="overflow:hidden;position:absolute;' +
(border ? ("border:" + border  + ";") : "") +
"width:" +(Math.max(0, Math.round(w)))+"px;" +
"height:" +(Math.max(0, Math.round(h)))+"px;" +
"left:" +Math.max(0, (x))+"px;" +
"top:" +Math.max(0, (y))+"px;" +
(url ? backImgOffset(url,0,0) : "") +
'"></div>'
}
function moveBorderElement(id,x,y,w,h)
{
var fr = getReportFrame()
if (fr)
{
if (eval("fr.__" + id) == null)
{
eval("fr.__" + id + "=fr.document.getElementById('"+id+"');" )
}
var lyr = eval("fr.__" + id)
if (lyr)
{
var st=lyr.style
if (w!=null) st.width  = "" +(Math.max(0, Math.round(w)))+"px"
if (h!=null) st.height = "" +(Math.max(0, Math.round(h)))+"px"
if (x!=null) st.left   = "" +(Math.round(x))+"px"
if (y!=null) st.top    = "" +(Math.round(y))+"px"
}
}
}
function displayPageBorders(z)
{
var pageContainer = getPageContainer()
var pageFrame = getPageFrame()
if (pageFrame)
{
var x=pageContainer.offsetLeft
var y=pageContainer.offsetTop
var w=pageContainer.offsetWidth
var h=pageContainer.offsetHeight
pageFrame.innerHTML=createBorderElement("borderSpacer",x+w*z+14,y+h*z+14,1,1) +
createBorderElement("borderRight",x+w*z+1,y+1+7,7,h*z-7+2,_img+"pageright.gif") +
createBorderElement("borderBottom",x+1+7,y+h*z+1,w*z-7+2,7,_img+"pagebottom.gif") +
createBorderElement("borderTopRight",x+w*z+1,y+1,7,7,_img+"pagetopright.gif") +
createBorderElement("borderBottomLeft",x+1,y+h*z+1,7,7,_img+"pagebottomleft.gif") +
createBorderElement("borderBottomRight",x+w*z+1,y+h*z+1,7,7,_img+"pagebottomright.gif") +
createBorderElement("borderContainer",x-1,y-1,w*z+(_ie?2:0),h*z+(_ie?2:0),null,"1px solid #808090")
}
}
function adjustPageBorders()
{
var repInfo=arrReports[iReportID]
if (repInfo.pageMode == "Page")
{
var fr = getReportFrame()
if (fr.document.getElementById("borderContainer")==null)
{
displayPageBorders(getZoom())
}
else
{
var pageContainer = getPageContainer()
var pageFrame = getPageFrame()
var z = getZoom()/100
var x=pageContainer.offsetLeft
var y=pageContainer.offsetTop
var w=pageContainer.offsetWidth
var h=pageContainer.offsetHeight
moveBorderElement("borderSpacer",x+w*z+14,y+h*z+14)
moveBorderElement("borderRight",x+w*z+1,y+1+7,7,h*z-7+2)
moveBorderElement("borderBottom",x+1+7,y+h*z+1,w*z-7+2,7)
moveBorderElement("borderTopRight",x+w*z+1,y+1)
moveBorderElement("borderBottomLeft",x+1,y+h*z+1)
moveBorderElement("borderBottomRight",x+w*z+1,y+h*z+1)
moveBorderElement("borderContainer",x-1,y-1,w*z+(_ie?2:0),h*z+(_ie?2:0))
}
}
}
function getPageFrame()
{
var fr = getReportFrame()
if (fr)
{
if (fr.__pageFrame == null)
{
fr.__pageFrame = fr.document.getElementById("pageFrame")
}
return fr.__pageFrame
}
else
return null
}
function getPageContainer()
{
var fr = getReportFrame()
if (fr)
{
if (fr.__pageContainer == null)
{
fr.__pageContainer = fr.document.getElementById("pageContainer")
if (fr.__pageContainer == null)
fr.__pageContainer = fr.document.body
}
return fr.__pageContainer
}
else
return null
}
function convertX(x)
{
return (getPageContainer().offsetLeft * (1 - getZoom()/100)) + Math.round(x * (getZoom() / 100))
}
function convertY(y)
{
return (getPageContainer().offsetTop * (1 - getZoom()/100)) +  + Math.round(y * (getZoom() / 100))
}
function convertWidth(w)
{
return Math.round(w * (getZoom() / 100))
}
function convertHeight(h)
{
return Math.round(h * (getZoom() / 100))
}
function newMenu(id,beforeShowCB)
{
var o=newMenuWidget(id,null,beforeShowCB)
o.oAdd=o.add
o.oAddCheck=o.addCheck
o.add=newMenu_add
o.addSub=newMenu_addSub
o.addCheck=newMenu_addCheck
return o
}
function newMenu_add(id,txt,icon,dx,cb)
{
var o=this,r=o[id]=o.oAdd(id,txt,cb?cb:clickCB,icon?(_img+icon+".gif"):null,16*dx,0,false,16*dx,16)
return r
}
function newMenu_addSub(sub,id,txt,icon,dx)
{
var r=this.add(id,txt,icon,dx)
this[id].attachSubMenu(sub)
return r
}
function newMenu_addCheck(id,txt,icon,dx,cb)
{
var o=this
o[id]=o.oAddCheck(id,txt,cb?cb:clickCB,icon?(_img+icon+".gif"):null,16*dx,0,false,16*dx,16)
}
function addAccelerators(m)
{
var im=m.add("cut","Cut","copycut", 0, copyCutPasteCB)
im.setAccelerator("X",_mac?_meta:_ctrl)
im=m.add("copy","Copy","copycut", 1, copyCutPasteCB)
im.setAccelerator("C",_mac?_meta:_ctrl)
im=m.add("paste","Paste","copycut", 2, copyCutPasteCB)
im.setAccelerator("V",_mac?_meta:_ctrl)
}
drillByHMenu=newMenu("drillByHMenu",initdrillHierarchyMenu)
drillDimMenu=newMenu("drillDimMenu",initdrillDimensionMenu)
drillByHMenu.addSub(drillDimMenu,"dbH0","")
drillDimMenu.add('dDim0','',null,null,drillCB)
function addDrillMenu(m,nosep)
{
m.add("drillUp","Drill Up to",null,null,drillCB)
m.add("drillDown","Drill Down to",null,null,drillCB)
m.addSub(drillDimMenu,"drillUpSubMenu","Drill Up to")
m.addSub(drillDimMenu,"drillDownSubMenu","Drill Down to")
m.drillsep1=m.addSeparator()
m.addSub(drillByHMenu,"drillBy","Drill By...")
m.drillsep2=m.addSeparator()
}
drillContextMenu=m=newMenu("drillContextMenu",initdrillContextMenu)
addDrillMenu(m)
_drillInfo=new Object
_drillOverInfo=null;
_bodrill=''
_drillPathInfo=null
_drillPathInfo=null
idxAction=0
idxHID=1
idxHName=2
idxDimID=3
idxDimName=4
idxScopeFlag=5
_arrAmbiguousDim=null
drillObs1=newObserverTwoEvents(_EVT_DRILL_ON,_EVT_WOM_LOADED,drillIcnCB)
drillObs2=newObserverTwoEvents(_EVT_DRILL_OFF,_EVT_WOM_LOADED,drillIcnCB)
function fillDrillInfo(drillAction, drillPathID,parentBlockID,dimFromID,drillFilter,blockID,isDrillOver)
{
var infoObj,pathObj = null;
if(isDrillOver) 
{
if(!_drillOverInfo) _drillOverInfo=new Object
infoObj =_drillOverInfo;
}
else
{
if(!_drillInfo) _drillInfo=new Object
infoObj =_drillInfo;
}
infoObj.action=drillAction
infoObj.pathId=drillPathID
infoObj.parBlockID=parentBlockID
infoObj.dimFromID=dimFromID
infoObj.drillFilter=drillFilter
infoObj.bID=blockID
var fr=getReportFrame()
if(fr && fr.DrillPathInfoAr && drillPathID)
pathObj=findDrillPath(drillPathID,fr.DrillPathInfoAr)
if(isDrillOver)
_drillOverPathInfo=pathObj;
else
_drillPathInfo=pathObj;
}
function findDrillPath(drillPathID,arr)
{
if(!drillPathID) return null;
for(i = 0 ;i < arr.length; i++) 
{
if(arr[i].id== drillPathID)
return arr[i];
}
return null;
}
function initdrillContextMenu(cm)
{
if(!isInteractive) cm=this
if(_bDrillMode && _drillPathInfo)
{
cm.drillsep1.show(true)
if(isInteractive)
cm.drillsep2.show(true)
else 
cm.drillsep2.show(false)
generateDrillByMenu(cm)
generateDrillMenu(cm,'up')
generateDrillMenu(cm,'down')
}
else
{
cm.drillsep1.show(false)
cm.drillsep2.show(false)
cm.drillUp.show(false)
cm.drillDown.show(false)
cm.drillUpSubMenu.show(false)
cm.drillDownSubMenu.show(false)
cm.drillBy.show(false)
}
}
function generateDrillMenu(cm,action)
{
var text,actionArr,menuItem,menuSubItem
if(action == 'up')
{
text="Drill Up to"
actionArr=_drillPathInfo.du
menuItem=cm.drillUp
menuSubItem=cm.drillUpSubMenu
}
else if(action == 'down')
{
text="Drill Down to"
actionArr=_drillPathInfo.dd
menuItem=cm.drillDown
menuSubItem=cm.drillDownSubMenu
}
var len=actionArr?actionArr.length:0
if(len==0)
{
menuItem.setText(text)
menuItem.setDisabled(true)
menuSubItem.show(false)
menuItem.show(true)
}
else if(len==1) 
{
menuItem.setDisabled(false)
menuItem.show(true)
menuSubItem.show(false)
if(actionArr[0][idxScopeFlag]=='out')
menuItem.setText(text+' '+actionArr[0][idxDimName]+ ' (New Query)')
else
menuItem.setText(text+' '+actionArr[0][idxDimName])
}
else 
{
menuItem.show(false)
menuSubItem.show(true)
menuSubItem.setText(text)
var lenMenu=drillDimMenu.items.length
if(len>lenMenu)
{
for(var i=lenMenu;i<len;i++)
drillDimMenu.add('dDim'+i,'',null,null,drillCB)
}
}
}
function initdrillHierarchyMenu()
{
var m=this, mlen=m.items.length
var db=_drillPathInfo.db,len=db.length
if(len==0) return;
var HName='',InitialHName=db[0][idxHName]
var HID='',InitialHID=db[0][idxHID] 
var idxH=1
m.getItem(0).setText(InitialHName)
m.getItem(0).Hid=InitialHID
for(var i=1;i<len;i++)
{
HName=db[i][idxHName]
HID=db[i][idxHID]
if(HID!=InitialHID)
{
InitialHID=HID
m.getItem(idxH).setText(HName)
m.getItem(idxH).Hid=HID
idxH++
}
}
if(idxH<mlen) 
{
for(var i=idxH;i<mlen;i++) 
m.getItem(i).show(false)
}
}
function initdrillDimensionMenu()
{
var m=this,mlen=m.items.length,da 
var action=m.par.id
if (_bodrillMenuInfo != null) { 
eval("fillDrillInfo("+_bodrillMenuInfo+")")
}
if (action=='drillUpSubMenu')
{
da=_drillPathInfo.du
}
else if (action=='drillDownSubMenu')
{
da=_drillPathInfo.dd
}
else
{
da=_drillPathInfo.db 
}
var len=da.length
if (len==0) return;
var idxD=0,InitialHName=m.par.text,InitialHID=m.par.Hid ,HID='',HName='',DName='',DimID=''
for(var i=0;i<len;i++)
{
HName=da[i][idxHName]
HID=da[i][idxHID]
DName=da[i][idxDimName]
DimID=da[i][idxDimID]
if(action=='drillDownSubMenu' || action=='drillUpSubMenu' )
{
var scope=''
if(da[i][idxScopeFlag]=='out') scope=' (New Query)'
m.getItem(idxD).show(true)
m.getItem(idxD).setText(HName +' - '+DName + scope)
m.getItem(idxD).drillvalue=DimID
m.getItem(idxD).drillHvalue=HID
idxD++
}
else
{
if(HID == InitialHID)
{
m.getItem(idxD).show(true)
m.getItem(idxD).setText(DName)
m.getItem(idxD).drillvalue=DimID
m.getItem(idxD).drillHvalue=HID
idxD++
}
}
}
if(idxD<mlen) 
{
for(var i=idxD;i<mlen;i++)
m.getItem(i).show(false)
}
}
function generateDrillByMenu(cm)
{
var db=_drillPathInfo.db,len=db.length
if(len==0)
{
cm.drillBy.show(true)
cm.drillBy.setDisabled(true)
return;
}
var HName='',InitialHName=db[0][idxHName] 
var HID='',InitialHID=db[0][idxHID] 
var mHlen=drillByHMenu.items.length
var mDlen=drillDimMenu.items.length
var countH=1,countD=countDmax=1
for(var i=1;i<db.length;i++)
{
HName=db[i][idxHName]
HID=db[i][idxHID]
if(HID!=InitialHID)
{
InitialHName=HName
InitialHID=HID
countH++
if(countD>countDmax) 
countDmax=countD
countD=0
}
countD++
}
if(countD>countDmax) 
countDmax=countD
if(countH>mHlen) 
{
for(var i=mHlen;i<countH;i++)
drillByHMenu.addSub(drillDimMenu,'dbH'+i,'')
}
if(countDmax>mDlen) 
{
for(var i=mDlen;i<countDmax;i++)
drillDimMenu.add('dDim'+i,'',null,null,drillCB)
}
cm.drillBy.show(true)
cm.drillBy.setDisabled(false)
}
_firstOutOfScopeDrillTDC=true
function drillCB(id, checkTDC)
{
var action=id?id:this.id
var checkTDC=checkTDC?true:false
if (!checkTDC) {
if(action == 'null') return; 
var fr=getReportFrame()
if (fr==null) return
_drillSDKURL=fr.DrillSDKURL
_drillTargetFrame=fr.DrillTargetFrame
_drillHierToHolder=fr.DrillHierToHolder
_drillDimFromHolder=fr.DrillDimFromHolder
_drillDimToHolder=fr.DrillDimToHolder
_drillActionHolder=fr.DrillActionHolder
_drillFilterHolder=fr.DrillFilterHolder
_blockHolder=fr.BlockHolder
_storageHolder=fr.StorageHolder
_storageKey=fr.StorageKey
}
switch(action)
{
case "up":
case "drillUp":
var du=_drillPathInfo.du
if(du.length>1)
{
_arrAmbiguousDim=du
frameNav("DlgFrame","language/"+_lang+"/html/ambiguousDrillDialog.html")
}
else
{
if (_firstOutOfScopeDrillTDC && _bTdcActivate && !checkTDC && (du[0][idxScopeFlag]=='out'))
{
showPromptDialog("This action will modify the query. Web Intelligence can track data changes only when the query remains the same. If you modify the query, the reference data will be lost and changed data will not be visible until the next refresh. Do you want to continue?","Deactivate Data Tracking",1, "_firstOutOfScopeDrillTDC=false;drillCB(\""+id+"\", true);")
return
}
generateDrill('up',du[0][idxHID],du[0][idxDimID],du[0][idxScopeFlag])
}
break;
case "down":
case "drillDown":
var dd=_drillPathInfo.dd
if(dd.length>1)
{
_arrAmbiguousDim=dd
frameNav("DlgFrame","language/"+_lang+"/html/ambiguousDrillDialog.html")
}
else
{
if (_firstOutOfScopeDrillTDC && _bTdcActivate && !checkTDC && (dd[0][idxScopeFlag]=='out'))
{
showPromptDialog("This action will modify the query. Web Intelligence can track data changes only when the query remains the same. If you modify the query, the reference data will be lost and changed data will not be visible until the next refresh. Do you want to continue?","Deactivate Data Tracking",1, "_firstOutOfScopeDrillTDC=false;drillCB(\""+id+"\", true);")
return
}
generateDrill('down',dd[0][idxHID],dd[0][idxDimID],dd[0][idxScopeFlag])
}
break;
default:
if(id)  
return showAlertDialog("Drill action is not possible","Drill",2,null);
var dimID = this.drillvalue, hID=this.drillHvalue, sDrillAction , action=this.par.par.id 
if(action=='drillDownSubMenu') 
{
sDrillAction='down'
}
else if( action=='drillUpSubMenu') 
{
sDrillAction='up'
}
else  
{
sDrillAction='by'
}
generateDrill(sDrillAction,hID,dimID,null)
break
}
}
function generateDrill(sDrillAction,hID,dimID,outOfScope)
{
var action= new Object;
action.valid= true;
action.DrillAction = sDrillAction;
action.BID = _drillInfo.bID;
action.PBID = _drillInfo.parBlockID;
action.DimFrom = _drillInfo.dimFromID;
action.HierTo = hID;
action.DimTo = dimID;
action.DrillFilter = _drillInfo.drillFilter;
if((outOfScope =='out') && (_promptDrillOutScope=='Y') && !bUseQueryDrill)
    _drillTargetFrame="DlgFrame"
var newDrillURL=_drillSDKURL+"&zoom="+getZoom() + "&isInteractive=" + isInteractive
if(isInteractive) 
_curIdRef=getReportBodyIdRef();
else
_curIdRef=null;
newDrillURL+=(_curIdRef?"&idRef="+escape(_curIdRef):"")+"&sFollowBid="+action.BID
var cancelKey="DL"+allUseDictionary.size()
var previousURL=(self.Report.Reportbloc)?self.Report.Reportbloc.location.href:self.Report.location.href
var iPos=previousURL.indexOf('?')
if (iPos>-1)
{
var urlBase=previousURL.substring(0,iPos)
var queryString=previousURL.substring(iPos+1)
queryString=removeQueryParameter(queryString, "sRequestNewReport")
iPos=urlBase.lastIndexOf('/')
urlBase=(iPos>-1)?urlBase.substring(0,iPos+1):""
previousURL=urlBase+"report"+_appExt+"?"+queryString
}
allUseDictionary.put(cancelKey,previousURL)
newDrillURL+="&sCancel="+cancelKey
var FormStr = '<FORM ID="Form2SDKDRILL" NAME="Form2SDKDRILL" ACTION="' + newDrillURL +'" METHOD="POST" TARGET="' + _drillTargetFrame + '">' + 
'<INPUT TYPE="hidden" NAME= "' + _drillActionHolder + '" VALUE="'+ action.DrillAction + '"></INPUT>' + 
'<INPUT TYPE="hidden" NAME="' + _blockHolder + '" VALUE="' + action.PBID + '"></INPUT>' +
'<INPUT TYPE="hidden" NAME="' + _storageHolder + '" VALUE="' + _storageKey + '"></INPUT>';
switch(typeof(action.DimFrom))
{
case 'object':
for(i = 0; i < action.DimFrom.length; i++)
FormStr += '<INPUT TYPE="hidden" NAME="' + _drillDimFromHolder + '" VALUE="' + action.DimFrom[i] + '"></INPUT>';
break;
case 'string':
case 'number':
FormStr += '<INPUT TYPE="hidden" NAME="' + _drillDimFromHolder + '" VALUE="' + action.DimFrom + '"></INPUT>';
break;
}
switch(typeof(action.DrillFilter))
{
case 'object':
for(i = 0; i < action.DrillFilter.length; i++)
FormStr += '<INPUT TYPE="hidden" NAME="' + _drillFilterHolder + '" VALUE="' + convStr(action.DrillFilter[i]) + '"></INPUT>';
break;
case 'string':
case 'number':
FormStr += '<INPUT TYPE="hidden" NAME="' + _drillFilterHolder + '" VALUE="' + convStr(action.DrillFilter) + '"></INPUT>';
break;
}
switch(typeof(action.DimTo))
{
case 'object':
for(i = 0; i < action.DimTo.length; i++)
FormStr += '<INPUT TYPE="hidden" NAME="' + _drillDimToHolder + '" VALUE="' + action.DimTo[i] + '"></INPUT>';
break;
case 'string':
case 'number':
FormStr += '<INPUT TYPE="hidden" NAME="' + _drillDimToHolder + '" VALUE="' + action.DimTo + '"></INPUT>';
break;
}
switch(typeof(action.HierTo))
{
case 'object':
for(i = 0; i < action.HierTo.length; i++)
FormStr +='<INPUT TYPE="hidden" NAME="' + _drillHierToHolder + '" VALUE="' + action.HierTo[i] + '"></INPUT>';
break;
case 'string':
case 'number':
FormStr +='<INPUT TYPE="hidden" NAME="' + _drillHierToHolder + '" VALUE="' + action.HierTo + '"></INPUT>';
break;
}
FormStr +='</FORM>';
var fr=getReportFrame()
if (fr==null) return
var drillForm = fr.document.getElementById("Form2SDKDRILL");
if(drillForm!=null)
fr.document.body.removeChild(drillForm)
append(fr.document.body,FormStr,fr.document)
eventManager.notify(_EVT_DRILL_ACTION)
wt()
drillForm = fr.document.getElementById("Form2SDKDRILL");
drillForm.submit();
}
function drillNav()
{
if (strReportMode == "Viewing")
{
strReportMode = "Analysis";
eventManager.notify(_EVT_DRILL_ON)
wt()
}
else
{
strReportMode = "Viewing"
eventManager.notify(_EVT_DRILL_OFF)
wt()
}
setDrillMode()
var p=urlParams(false,true)
if (p!="")
{
frameNav("Report","processDrill"+_appExt+p)
}
}
function getSnapshot() 
{
var p=urlParams(false,true);
if (p!="")
{
p+= "&nbPage=" + nbPage
frameNav("Report","processSnapShot"+_appExt+p)
}
}
function setDrillMode()
{
if(strReportMode == "Analysis")
{
setDrillIconState(true)
_bDrillMode = true
}
else
{
setDrillIconState(false)
_bDrillMode = false
}
}
function setDrillIconState(bDrillMode)
{
if (!isPageLoaded)
setTimeout('setDrillIconState('+bDrillMode+')',100)
else
{
var tooltip = bDrillMode?"Deactivate drill mode":"Start drill mode"
//drillMainIcon.check(false) // bDrillMode to false Done For Reporting
//drillMainIcon.changeTooltip(tooltip)
snapshotIcon.setDisplay(false) // bDrillMode to Done For Reporting
}
}
function drillMoveCB(e)
{
if (_ie)
{
var fr=getReportFrame()
if (fr==null) return
e=fr.event
if (this.style.cursor==_hand)
this.parentNode.parentNode.style.cursor=_hand
}
e.cancelBubble=true
}
function drillOverCB()
{
var tooltip='',actionArr=null
var directDrill=true
_bodrillOver = this.getAttribute("bodrill")
if(_bodrillOver)
{
_bodrillOver=escapeCR(_bodrillOver);
eval("fillDrillInfo("+_bodrillOver+",true)")
if(_drillOverInfo.action == 'up')
{
tooltip="Drill Up to "
actionArr=_drillOverPathInfo.du
}
else if(_drillOverInfo.action == 'down')
{
tooltip="Drill Down to "
actionArr=_drillOverPathInfo.dd
}
var len=actionArr?actionArr.length:0
if(len==0)
{
directDrill = false
}
else if(len==1) 
{
if(actionArr[0][idxScopeFlag]=='out')
tooltip+=' '+actionArr[0][idxDimName]+ ' (New Query)'
else
tooltip+=' '+actionArr[0][idxDimName]
}
if(directDrill)
{
this.style.cursor=_hand
if(_drillCustomFmt)
{
this.directDrill=true
_defaultdrillBgColor=this.style.backgroundColor
_defaultdrillFgColor=this.style.color
_defaultdrillUnderlined=this.style.textDecorationUnderline
this.style.textDecorationUnderline=_drillUnderlined
if(_drillBgColor!="")
this.style.backgroundColor="rgb("+_drillBgColor+")"
else
this.style.backgroundColor=_defaultdrillBgColor
if(_drillFgColor!="")
this.style.color="rgb("+_drillFgColor+")"
else
this.style.color=_defaultdrillFgColor
}
}
this.title=tooltip
}
}
function drillOutCB()
{
if(this.directDrill && _drillCustomFmt && (_defaultdrillBgColor!=null) && (_defaultdrillFgColor!=null))
{
this.style.textDecorationUnderline=(_defaultdrillUnderlined?_defaultdrillUnderlined:false)
this.style.backgroundColor=_defaultdrillBgColor
this.style.color=_defaultdrillFgColor
}
}
function requestNewReport(name,sPageMode,sReportMode)
{
arrReports[arrReports.length]=newReportInfo(name,sPageMode, sReportMode,"NaN")
var last=arrReports.length-1
eventManager.notify(_EVT_REP_DUPLICATED)
eventManager.notify(_EVT_REP_DATAOK)
}
function updateUseQueryDrill(b)
{
bUseQueryDrill = b;
}
drillFilterMenu=newMenu("drillFilterMenu",initdrillFilterMenu)
drillFilterDimMenu=newMenu("drillFilterDimMenu",initdrillFilterDimMenu)
function filldrillFilterMenu()
{
var p=getFrame("Report") 
if (p==null) return
var arrH=p.arrHierarchy,arrD=p.arrDimension,arrFreeDim=p.arrFreeDimension,arrFreeDtl=p.arrFreeDetail
var FHLen=drillFilterMenu.items.length,len=arrH.length,FDLen=drillFilterDimMenu.items.length
 for (var i=0; i<len; i++)
 {
var cptD=0
FDLen=drillFilterDimMenu.items.length
for (var j=0; j<arrD.length; j++) 
{
            if (i == arrD[j][4]) 
            {
if(cptD>=FDLen)
{
drillFilterDimMenu.add('dfd'+cptD,arrD[j][0],null,null,filterDrillCB)
}
cptD++
            }            
         }        
         if(i>=FHLen)
         {
drillFilterMenu.addSub(drillFilterDimMenu,'dfh'+i,arrH[i][0])
         }         
 }
var lenFreeDimDtl=arrFreeDim.length+arrFreeDtl.length;
if(lenFreeDimDtl>0)
{
FHLen=drillFilterMenu.items.length 
if(len>=FHLen)
drillFilterMenu.addSub(drillFilterDimMenu,'dfh'+len,'others')
FDLen=drillFilterDimMenu.items.length;
if(lenFreeDimDtl>FDLen)
{
for (var j=FDLen; j<lenFreeDimDtl; j++) 
{
drillFilterDimMenu.add('dfd'+j,"",null,null,filterDrillCB)
}
}
}
}
function initdrillFilterMenu()
{
var p=getFrame("Report") 
if (p==null) return
var arrH=p.arrHierarchy,arrD=p.arrDimension
var FHLen=drillFilterMenu.items.length, FDLen=drillFilterDimMenu.items.length
var len=arrH.length
for (var i=0; i<len; i++)
{     
drillFilterMenu.getItem(i).show(true)
drillFilterMenu.getItem(i).setText(arrH[i][0])
drillFilterMenu.getItem(i).hierID=arrH[i][1]
     }    
    var arrFreeDim=p.arrFreeDimension,arrFreeDtl=p.arrFreeDetail
    if (arrFreeDim.length>0 || arrFreeDtl.length>0) 
    {
drillFilterMenu.getItem(len).show(true)
drillFilterMenu.getItem(len).setText("Others")
drillFilterMenu.getItem(len).hierID='others'
len++
    }
for (var i=len; i<FHLen; i++)
{
drillFilterMenu.getItem(i).show(false)
    }   
}
function initdrillFilterDimMenu()
{    
    var fr=getReportFrame()
    if (fr==null) return
    var p=fr.parent,arrH=p.arrHierarchy,arrD=p.arrDimension
var FHLen=drillFilterMenu.items.length, FDLen=drillFilterDimMenu.items.length
var arrFreeDim=p.arrFreeDimension,arrFreeDtl=p.arrFreeDetail
var HID=this.par.hierID, cptD=0
if(HID=='others')
{
for (var j=0; j<arrFreeDim.length; j++) 
{
drillFilterDimMenu.getItem(cptD).show(true)
drillFilterDimMenu.getItem(cptD).setText(arrFreeDim[j][0])
drillFilterDimMenu.getItem(cptD).dimID=arrFreeDim[j][1]
cptD++
     }
    for (var j=0; j<arrFreeDtl.length; j++) 
{
drillFilterDimMenu.getItem(cptD).show(true)
drillFilterDimMenu.getItem(cptD).setText(arrFreeDtl[j][0])
drillFilterDimMenu.getItem(cptD).dimID=arrFreeDtl[j][1]   
cptD++   
    }
}
else
{
for (var j=0; j<arrD.length; j++) 
{
    if (HID == arrD[j][3]) 
    {
drillFilterDimMenu.getItem(cptD).show(true)
drillFilterDimMenu.getItem(cptD).setText(arrD[j][0])
drillFilterDimMenu.getItem(cptD).dimID=arrD[j][1]
cptD++
    }            
 } 
     }    
     for (var j=cptD; j<FDLen; j++) 
{
drillFilterDimMenu.getItem(j).show(false)
}
}
function getDrillBarFrame()
{
var f=getFrame("Report")
if (f==null) return null
if (_bDrillMode) 
f=f?f.frames[0]:null
else
f=null
return f
}
function filterDrillCB()
{
var DimID=this.dimID
getDrillBarFrame().AddFilter(DimID)
}
function showDrillFilterMenu(x,y)
{
drillFilterMenu.show(true,x,y)
_currContextMenu=drillFilterMenu
}
function debugdrillfilter()
{
var fr=getReportFrame(),p=fr.parent,arrH=p.arrHierarchy,arrD=p.arrDimension
var s=''
for (var i=0; i<arrH.length; i++)
{
s+='H = '+arrH[i][0]+ arrH[i][1] +' ('
for (var j=0; j<arrD.length; j++) 
{
            if (i == arrD[j][4]) 
            {
s+=arrD[j][0]+ ' , '
            }                      
         }   
         s+=' )'+'\n'             
 }
 var arrFreeDim=p.arrFreeDimension,arrFreeDtl=p.arrFreeDetail
if((arrFreeDim.length >0) && (arrFreeDtl.length>0) )
s+='arrFreeDim= '+arrFreeDim.length +' arrFreeDtl= '+arrFreeDtl.length
 alert(s)
}
function showDrillContextMenu(bid,x,y)
{
var pos=getPos(reportBorder.layer)
x=x+pos.x
y=y+pos.y
m=drillContextMenu
m.show(true,x,y)
_currContextMenu=m
}
function drillIcnCB(evt,data)
{
var tab=tabsZone.items[iReportID]
var rep=arrReports[iReportID]
switch(evt)
{
case _EVT_DRILL_ON:
tab.change(rep.name,tab.cb,tab.value,tab.icon,16,16,(rep.sc?16:0),16,tab.dblclick,"Drilled Report")
rep.repMode='Analysis'
break
case _EVT_DRILL_OFF:
tab.change(rep.name,tab.cb,tab.value,tab.icon,16,16,(rep.sc?16:0),0,tab.dblclick,"Report")
rep.repMode='Viewing'
break
default:
break
}
}
toolbarsMenu=m=newMenu("toolbars",initToolbarsMenu)
m.addCheck("formatTool","Formatting")
m.addCheck("reportTool","Report")
m.addCheck("formulaTool","Formula")
//m.addCheck("drillTool","Drill") //Done For Reporting
m.addSeparator()
m.add("hideTool","Remove All Toolbars")
function initToolbarsMenu()
{
var o=this
if (_showQueryPanel || !isInteractive || !isEnableUserRight(_usrAllowShowHideToolbar))
{
o.formatTool.show(false)
o.reportTool.show(false)
o.formulaTool.show(false)
o.hideTool.show(false)
if( !isInteractive)
{
o.drillTool.show(_bDrillMode)
o.drillTool.check(_showDrillBar)
repairMenu(o)
}
else
o.drillTool.show(false)
}
else
{
o.formatTool.show(false) // from 'true' to false Done For Reporting
o.reportTool.show(false) // from 'true' to false Done For Reporting
o.formulaTool.show(false) // from 'true' to false Done For Reporting
o.hideTool.show(true)
o.formatTool.check(_showFormats)
o.reportTool.check(_showReporting)
o.formulaTool.check(_showFormula)
o.hideTool.setDisabled( !_showFormats&&!_showReporting&&!_showFormula)
initUserRight(o.formulaTool,_usrUseFormula);
initUserRight(o.formatTool,_usrUseFormatting);
if (!isEnableUserRight(_usrCreateEditSort) && !isEnableUserRight(_usrCreateEditBreak) && !isEnableUserRight(_usrCreateEditCalculation) &&
!isEnableUserRight(_usrCreateEditReportFilter) && !isEnableUserRight(_usrInsertDuplicate) && !isEnableUserRight(_usrCreateEditAlerter) &&
!isEnableUserRight(_usrCreateEditRank) ) 
o.reportTool.show(false);
o.drillTool.show(_bDrillMode)
o.drillTool.check(_showDrillBar)
}
}
function addPageNavigationIcn(id,text,tooltip,offset7)
{
var ic=pal.add(newIconWidget(id,_img+curIcon,clickCB,text,tooltip,7,16,offset7*7,0,offset7*7,16))
ic.setDisabled(true)
return ic
}
function addIcn(a,b,c,d)
{
return pal.add(newIconWidget(a,(b!=null)?_img+curIcon:null,clickCB,c,d,16,16,b?b*16:0,0,b?b*16:0,16))
}
function addIcnM(a,b,c,d,e,beforeShowCB)
{
return pal.add(newIconMenuWidget(a,(b!=null)?_img+curIcon:null,e,c,d,(b!=null)?16:1,16,b?b*16:0,0,b?b*16:0,16,null,beforeShowCB))
}
function addRadIcnM(a,b,c,d,e,attachMenuCB)
{
return pal.add(newRadioIconMenuWidget(a,(b!=null)?_img+curIcon:null,e,c,d,(b!=null)?16:1,16,b?b*16:0,0,b?b*16:0,16,null,attachMenuCB))
}
function sp()
{
return pal.add()
}
function addCheck(a,b,c,d,e)
{
return pal.add(newIconCheckWidget(a,(b!=null)?_img+curIcon:null,clickCB,c,d,16,16,b?b*16:0,0,b?b*16:0,16,e))
}
function addRad(a,b,c,d,e)
{
return pal.add(newIconRadioWidget(a,(b!=null)?_img+curIcon:null,clickCB,c,d,e,16,16,b?b*16:0,0,b?b*16:0,16))
}
function addLabel(a,b,c)
{
return pal.add(NewLabelWidget(a,b,c))
}
function addCombo(a,b,c,d)
{
return pal.add(newCustomCombo(a,clickCB,b,c,d))
}
function addTextCombo(a,b,c,d,e,f)
{
return pal.add(newTextComboWidget(a,b,c,d,clickCB,e,f))
}
function addIntCombo(a,b,c,d)
{
return pal.add(newIntComboWidget(a,b,c,d,clickCB))
}
function newJITPalette(id,hasSep)
{
var o=newWidget(id)
o.isDisp=true
o.htmlWritten=false
o.palWritten=false
o.pal=newPaletteWidget("pal_"+id)
o.hasSep=hasSep
o.sep=hasSep?newPaletteSepWidget("sep_"+id):null
o.getHTML=JITPalette_getHTML
o.oldInit=o.init
o.init=JITPalette_init
o.oldSetDisplay=o.setDisplay
o.setDisplay=JITPalette_setDisplay
o.getPalette=JITPalette_getPalette
return o
}
function JITPalette_getHTML()
{
var o=this
o.htmlWritten=true
var s= '<div id="'+o.id+'" style="display:'+(o.isDisp?'block':'none')+'">'
if (o.isDisp)
{
s+=o.pal.getHTML()+(o.hasSep?o.sep.getHTML():'')
o.palWritten=true
}
return s+'</div>'
}
function JITPalette_init()
{
var o=this
o.oldInit()
if (o.palWritten)
{
o.pal.init()
if (o.hasSep)
o.sep.init()
}
}
function JITPalette_setDisplay(d)
{
var o=this
if (o.isDisp!=d)
{
o.isDisp=d
if (o.htmlWritten)
{
o.oldSetDisplay(d)
}
}
if (!o.palWritten&&d)
{
o.setHTML(o.pal.getHTML()+(o.hasSep?o.sep.getHTML():''))
o.palWritten=true
o.pal.init()
if (o.hasSep)
o.sep.init()
}
}
function JITPalette_getPalette()
{
return this.pal
}
mainPal = newPaletteContainerWidget("mainPal",toolbarsMenu,0)
pal=pal1=newPaletteWidget("pal1")
if (bFullScreen)
{
curIcon="togglepane.gif"
togglepane=addCheck("togglepane",0,null,"Left Panel")
sp()
}
curIcon="standard.gif"
mainContainer=newWidget("mainContainer")
saveDocComputerMenu=m=newMenu("saveDocComputerMenu",initsaveDocComputerMenu)
m.add("saveXLS","Excel")
m.add("savePDF","PDF")
m.add("savePDFOp","PDF (with options)...")
//m.add("saveCSV","CSV")
//m.add("saveCSVOp","CSV (with options)...")
subRepMenu=newMenu("saveReportComputerAs",initsaveReportComputerMenu)
subRepMenu.add("saveReportXLS","Excel")
subRepMenu.add("saveReportPDF","PDF")
function initsaveDocComputerMenu()
{
var o=this
initUserRight(o.saveXLS,_usrDownloadExcel);
initUserRight(o.saveCSV,_usrDownloadCSV);
initUserRight(o.saveCSVOp,(strDocType=='rep')?'none':_usrDownloadCSV);
initUserRight(o.savePDF,_usrDownloadPDF);
initUserRight(o.savePDFOp,"none");
}
docMenuIcon=addIcnM("docMenu",(strDocType=='rep')?17:0,"Document","Document actions",null,initDocMenu)
docMenu=docMenuIcon.getMenu()
// COMMEDTED BELOW LINES. Done For Reporting
//closeDoc=docMenu.add("closeDoc","Close",clickCB)
//docMenu.addSeparator()
//if(!(isInteractive && _defaultWebEditor=='I'))
//editDocMenu=docMenu.add("editDoc","Edit",clickCB)
//else if(isInteractive && _defaultWebEditor=='I')
//editQueryMenu=docMenu.add("editQueryMenu","Edit Query",clickCB,_img+"standard.gif",8*16,0,false,8*16,16,"Edit the query to change the data definition of reports")
//docMenu.addSeparator()
//saveDoc=docMenu.add("saveDoc","Save",clickCB,_img+"standard.gif",16,0,false,32,16,"Save document")
//saveDoc.setAccelerator("S",_mac?_meta:_ctrl)
saveDocAs=docMenu.add("saveDocAs","Save as",clickCB)
saveDocComputerAs=docMenu.add("saveDocComputerAs","Save to my computer as")
saveDocComputerAs.attachSubMenu(saveDocComputerMenu)
docMenu.addSeparator()
saveReportComputerAs=docMenu.add("saveReportComputerAs","Save Report to my Computer As")
saveReportComputerAs.attachSubMenu(subRepMenu)
function initsaveReportComputerMenu()
{
var o=this;
initUserRight(o.saveReportXLS,_usrDownloadExcel);
initUserRight(o.saveReportPDF,_usrDownloadPDF);
}
if(isInteractive)
{
docMenu.addSeparator()
docProps=docMenu.add("docProps","Properties",clickCB)
}
function initDocMenu()
{
if (isNew && _UDZ_bNoSaveas)
_usrSaveDoc="none"
//initUserRight(saveDoc,_usrSaveDoc) Done For Reporting
if (_UDZ_bNoSaveas)
_usrSaveAsDoc="none"
initUserRight(saveDocAs,_usrSaveAsDoc)
if ((_defaultWebEditor!='I') && !canGoEdit())
_usrEditDoc="none";
if(!(isInteractive && _defaultWebEditor=='I')) 
{
var enableEditDoc=isEnableUserRight(_usrUseQueryHTML)||
isEnableUserRight(_usrUseHTMLReportPanel)||
isEnableUserRight(_usrUseJavaReportPanel);
if(! enableEditDoc) 
_usrEditDoc="none";
// initUserRight(editDocMenu,_usrEditDoc);//Done For Reporting
}
else if(isInteractive && _defaultWebEditor=='I') 
{
initUserRight(editQueryMenu,_usrEditQuery)
editQueryMenu.setDisabled(disableQueryPanel);
}
initUserRight(saveDocComputerAs,_usrDownloadDocMenu);
initUserRight(saveReportComputerAs,_usrDownloadExcelOrPDF);
repairMenu(docMenu)
}
viewMenuIcon=addIcnM("viewMenu",null,"View","View",null,initViewMenu)
viewMenu=viewMenuIcon.getMenu()
quickMode=viewMenu.addCheck("quickMode","Quick Display mode",clickCB,_img+"menu.gif",16*19,0,null,null,null,"Quick Display mode")
pageMode=viewMenu.addCheck("pageMode","Page mode",clickCB,_img+"menu.gif",64,0,null,null,null,"Page mode")
webMode=viewMenu.addCheck("webMode","Draft mode",clickCB,_img+"menu.gif",48,0,null,null,null,"Draft mode")
pdfMode=viewMenu.addCheck("pdfMode","PDF mode",clickCB,_img+"menu.gif",272,0,null,null,null,"PDF mode")
viewMenu.addSeparator();
leftPaneButton=viewMenu.addCheck("leftPaneButton","Left Panel",clickCB);
if (bFullScreen)
leftPaneButton=togglepane
viewMenu.addSeparator();
showStatus=viewMenu.addCheck("showStatus","Status Bar",clickCB);
if(isInteractive)
{
viewMenu.addSeparator()
if (isEnableUserRight(_usrAllowShowHideToolbar)) {
toolbars=viewMenu.add("toolbars","Toolbars")
toolbars.attachSubMenu(toolbarsMenu)
}
preferences=viewMenu.add("preferences","Preferences",clickCB)
}
function initViewMenu()
{
initUserRight(pdfMode,_usrDownloadPDF)
if (isInteractive)
{
if ( !isEnableUserRight(_usrEditPreferences) || !isEnableUserRight(_usrEditDoc) )
preferences.show(false)
}
quickMode.show(strDocType!="rep")
updatePageModeMenu(strPageMode);
repairMenu(viewMenu)
}
if (isInteractive)
createEditorContextMenus()
if (isInteractive)
createInsertMenu()
sp()
//saveDocIcn=addIcn("saveDocIcn",1,null,"Save document") //Done For Reporting
printDocIcn=addIcn("printDocIcn",19,null,"Export to PDF for printing")
sp()
if (_findAvailable)
//Commented For JIRA 2547	At Present uncommenting it!
findInDoc=addIcn("findInDoc",2,null,"Find text in tables and cells on this page")
sp();
//undo=addIcn("undo",4,null,"Undo previous actions") // commented four lines, Done For Reporting
//undo.setDisabled(true)
//redo=addIcn("redo",5,null,"Redo previous actions")
//redo.setDisabled(true)
sp();
zoomCombo=addTextCombo("zoomCombo",4,"Zoom",70,checkZoomCB)
sp()
curIcon="navigation.gif"
pageLab=addLabel("pageLab","")
firstIcn=addPageNavigationIcn("firstIcn","","Go to first index",0)
previousIcn=addPageNavigationIcn("previousIcn","","Go to previous index",1)
pageNumber=pal.add(newIntFieldWidget("pageNumber",null,null,null,clickCB,true,"Page number",30))
maxPage=addLabel("maxPage","")
nextIcn=addPageNavigationIcn("nextIcn","","Go to next index",2)
lastIcn=addPageNavigationIcn("lastIcn","","Go to last index",3)
pal1.beginRightZone()
curIcon="standard.gif"
editQueryIcon=editDocIcon=null;
if((_defaultWebEditor=='I') && isInteractive)
editQueryIcon=addIcn("editQuery",8,"Edit Query","Edit the query to change the data definition of reports")
//else
//editDocIcon=addIcn("editDocIcon",null,"Edit","Modify the document using your selected Web Intelligence report panel")
//above else and editDocIcon commented by ritesh.
sp()
//below  lines commented. Done For Reporting
refreshDocIcon=addIcn("refresh",7,bFullScreen?null:"Refresh Data","Refresh data to return latest results from the database")
refreshAllIcon=addIcnM("refreshAll",7,"Refresh All","Refresh data to return latest results from the database",clickCB,initRefreshAllMenu);
refreshAllMenu=refreshAllIcon.getMenu();
if (isInteractive)
{
purgeIcn=addIcn("purge",18,null,"Purge data");
purgeAll=addIcnM("purgeAll",18,null,"Purge data",clickCB,initPurgeAllMenu);
}
if (isEnableUserRight(_usrEnableTrackDataChanges)) sp()
curIcon="tdcIcons.gif"
tdcActivateIcon=addCheck("tdcActivate",2,"Track","Activate Data Tracking")
tdcShowChangesIcon=addCheck("tdcShowChanges",0,null,"Show changes")
tdcOptionsIcon=addIcn("tdcOptions",1,null,"Data Tracking Options")
sp()
curIcon="report.gif"
// drillMainIcon=addCheck("drill",9,null,"Start drill mode") // Done For Reporting for drill on right top on webi
snapshotIcon=addIcn("snapshot",10,null,"Snapshot")
if (isInteractive)
createFormatPalette()
if (isInteractive)
createReportingPalette()
if (isInteractive)
createFormulaPalette()
if (isInteractive)
createQueryPalette()
reportBorder=newWidget("reportBorder")
ReportWidget=newWidget("Report")
leftPane=newPaneWidget("leftPane","Show Document Properties",50,50,-1000,-1000,clickCB)
lPaneWidget=leftPane.iframe
var c=leftPane,dfc=_leftPaneSel
if (isInteractive)
addLeftComboEditItems(c,dfc)
c.add("reportmap","Navigation Map",_img+"panes.gif",48,0,_root+"language/"+_lang+"/html/reportMap.html")
if (!_UDZ_useCustomPrompts && isEnableUserRight(_usrRefreshDoc))
c.add("quickFilter","User Prompt Input",_img+"panes.gif",64,0,_root+"language/"+_lang+"/html/quickPrompts.html")
if (_findAvailable)
c.add("advFind","Find",_img+"standard.gif",2*16,0,_root+"language/"+_lang+"/html/advancedFind.html")
tabsZone=newTabBarWidget("tabsZone",false,clickCB,"position:absolute;top:-500px",window.dblClickTabWidget,isInteractive?beforeShowTabsZoneWidgetMenu:null,true)
statusBar=newWidget("statusBar");
statusBarContent=newWidget("statusBarContent");
statusPartial=newWidget("statusPartial");
statusPartialText=newWidget("statusPartialText");
statusRefDate=newWidget("statusRefDate");
statusBarTDC=newWidget("statusBarTDC");
statusBarTDCRef=newWidget("statusBarTDCRef");
statusBarTDCRefDate=newWidget("statusBarTDCRefDate");
leftZoneGrab = newGrabberWidget("leftZoneGrab",grabCB,-100,100,5,10,true,clickCB)
pdfContainer=newJITPalette("pal6",false)
pal=pal6=pdfContainer.getPalette("pal6")
curIcon="standard.gif"
saveDocPDFIcn=addIcn("saveDocPDF",1,"Save","Save")
curIcon="other.gif"
sp()
curIcon="menu.gif"
addIcn("htmlMode",4,"View in HTML format","View in HTML format")
sp()
showStatusPDF=addCheck("showStatusPDF",null,"Status Bar");
sp();
pal6.beginRightZone()
curIcon="standard.gif"
editDocPDFIcn=addIcn("editDocPDF",null,"Edit",(strDocType=='rep')?null:"Modify the document using your selected Web Intelligence report panel")
sp();
refreshDocPDFIcn=addIcn("refreshPDF",7,"Refresh Data","Refresh data to return latest results from the database")
alertDlg=null
function showAlertDialog(msg,title,iPromptType,okCB)
{
if (alertDlg==null)
{
alertDlg=newAlertDialog("alertDlg","","","OK",_promptDlgInfo)
targetApp(alertDlg.getHTML());
alertDlg.init()
}
if (iPromptType ==null) iPromptType=0
if (title==null)
title= "Web Intelligence"
if (startsWithIgnoreCase(msg, "VIEWER:"))
msg=getLocalizedMessageString(msg);
alertDlg.setText(msg)
alertDlg.setTitle(title)
alertDlg.setPromptType(iPromptType)
alertDlg.yesCB=(okCB)?okCB:null
alertDlg.show(true)
}
promptDlg=null
function showPromptDialog(msg,title,iPromptType,yesCB,noCB, yesText, noText)
{
if (promptDlg==null)
{
var yes=(typeof(yesText)=="undefined")?"Yes":yesText;
var no=(typeof(noText)=="undefined")?"No":noText;
promptDlg=newPromptDialog("promptDlg","","",yes,no,_promptDlgInfo)
targetApp(promptDlg.getHTML());
promptDlg.init()
}
if (iPromptType ==null) iPromptType=0
if (title==null)
{
title= "Web Intelligence"
}
promptDlg.setText(msg)
promptDlg.setTitle(title)
promptDlg.setPromptType(iPromptType)
promptDlg.yesCB=yesCB
promptDlg.noCB=noCB
promptDlg.show(true)
}
invalidSessionDlg=null
function alertSessionInvalid()
{
if (invalidSessionDlg==null)
{
invalidSessionDlg=newAlertDialog("invalidSessionDlg","Web Intelligence","Invalid session. Please close your browser and log on again. (WIH 00013)","OK",_promptDlgWarning, invalidSession)
targetApp(invalidSessionDlg.getHTML());
invalidSessionDlg.init()
}
hideWaitDlg();
invalidSessionDlg.show(true)
}
waitDlg=null
waitTimeoutID=null
function showWaitDlg(title,showCancel,cancelCB,showUPB,duration,showLabel,text)
{
if (waitDlg==null)
{
waitDlg=newWaitDialogBoxWidget("waitDlg",250,150)
targetApp(waitDlg.getHTML());
waitDlg.init()
}
waitDlg.setTitle(title)
waitDlg.setShowCancel(showCancel,cancelCB)
waitDlg.setShowUPB(showUPB,duration)
waitDlg.setShowLabel(showLabel,text)
if (waitTimeoutID!=null)
clearTimeout(waitTimeoutID)
waitTimeoutID=setTimeout('waitDlg.show(true)',1)
}
function hideWaitDlg()
{
if ((waitDlg!=null)&&(waitTimeoutID!=null))
{
clearTimeout(waitTimeoutID)
waitTimeoutID=null
waitDlg.show(false)
}
}
if ((allowSaveAs!=null)&&(allowSaveAs==false))
{
_usrSaveAsDoc="none"
}
function isEnableUserRight(right)
{
return (right=="none")?false:true
}
function initUserRight(widget,right,bDisable)
{
if(widget==null)return;
if(!isEnableUserRight(right))
{  
if (bDisable)
widget.setDisabled(true)
else
{
if(widget.setDisplay) 
widget.setDisplay(false)
else if(widget.show) 
widget.show(false)
}
}
}
function setUserRights()
{
var w=window
if ((_defaultWebEditor!='I') && !canGoEdit())
_usrEditDoc="none";
var enableEditDoc=isEnableUserRight(_usrUseQueryHTML)||
isEnableUserRight(_usrUseHTMLReportPanel)||
isEnableUserRight(_usrUseJavaReportPanel);
if(! enableEditDoc) 
_usrEditDoc="none";
if (isNew && _UDZ_bNoSaveas)
_usrSaveDoc="none"
if (isInteractive) 
editSetUserRights()
//initUserRight(drillMainIcon,false) ////modified as false from _usrWorkInDrillMode. Done For Reporting
initUserRight(snapshotIcon,false) //modified as false from _usrWorkInDrillMode. Done For Reporting
initUserRight(refreshDocIcon,_usrRefreshDoc)
initUserRight(refreshAllIcon,_usrRefreshDoc)
//initUserRight(saveDocIcn,_usrSaveDoc) //Done For Reporting
initUserRight(printDocIcn,_usrDownloadPDF)
initUserRight(editDocIcon,_usrEditDoc);
initUserRight(editDocPDFIcn,_usrEditDoc)
initUserRight(refreshDocPDFIcn,_usrRefreshDoc)
initUserRight(saveDocPDFIcn,_usrSaveDoc)
if (bFullScreen)
{
if (w.undo) undo.setDisplay(false)
if (w.redo) redo.setDisplay(false)
if (w.viewMenuIcon) viewMenuIcon.setDisplay(false)
if (w.docMenuIcon) docMenuIcon.setDisplay(false)
if (w.editQueryIcon) editQueryIcon.setDisplay(false)
if (w.editDocIcon) editDocIcon.setDisplay(false)
}
repairToolbar(w.pal1)
repairToolbar(w.pal3)
repairToolbar(w.pal5)
repairToolbar(w.pal6)
}
function repairMenu(menu)
{
if (menu==null)
return
var lastIsSep=true 
var index=-1
var items=menu.items
for (var i in items)
{
var item=items[i]
if(item)
{
if (item.isSeparator==true)
{
if(lastIsSep)
{
item.show(false)
}
else
{
index=i
item.show(true)
}
lastIsSep=true
}
else if(item.isShown) 
{
lastIsSep=false
index=-1
}
}
}
if(lastIsSep && index >-1) 
items[index].show(false)
}
function repairToolbar(toolbar)
{
if (toolbar==null)
return
var lastIsSep=true 
var index=-1
var items=toolbar.items
for (var i in items)
{
var item=items[i]
if(item)
{
if (item.isSeparator==true)
{
if(lastIsSep)
{
item.setDisplay(false)
}
else
{
index=i
item.setDisplay(true)
}
lastIsSep=true
}
else if(item.isDisplayed()) 
{
lastIsSep=false
index=-1
}
}
}
if(lastIsSep && index >-1) 
{
items[index].setDisplay(false)
}
}
function simulateClick(e)
{
fr=getReportFrame()
if (fr&&fr.document&&fr.document.onmousedown&&(fr.document.onmousedown!=releaseSelection))
fr.document.onmousedown(e)
}
function releaseSelection()
{
if (_currContextMenu)
_currContextMenu.show(false)
}
function bodyDragStart()
{
if (_currContextMenu)
_currContextMenu.show(false)
return false
}
function getReportFrame()
{
var f=getFrame("Report")
if (f==null) return null
if (_bDrillMode&&f&&(f.frames)&&(f.frames.Reportbloc))
{
f=f?f.frames.Reportbloc:null
try
{
var dummy=f.name
}
catch(ex)
{
return null
} 
}
return f
}
function getparentBID(o)
{
var bid=o.getAttribute("bid")
if(bid) 
{
return o
}
else
{
if(o.parentNode)
return getparentBID(o.parentNode)
else return null
}
}
function getReportBodyIdRef()
{
var body = findByClassName(doc,"body")
if (body)
{
var layers = bidTable[body.bid]
if (layers.length == 1)
return layers[0].getAttribute("idref")
}
return null;
}
DDActionMethod=null
_isSelectStartEnabled=true
function CancelSelectStartCB()
{
return _isSelectStartEnabled
}
function viewClickCB(e) 
{
globResizeObj = null
globMoveObj = null
if (_moz&&isInteractive)
{
tabEdit_lostFocus()
fEdit_lostFocus()
}
_tableBid=null
var o=this,bid=o.getAttribute("bid"),fr=getReportFrame()
_curIdRef=null
_curIdRefBid=bid
if (fr==null) return
var d=fr.document
if (_ie)
e =fr.event
var ctrl= _mac?e.metaKey :e.ctrlKey, shift = e.shiftKey 
e.cancelBubble=true
if (!_webKit)
fr.focus();
_bodrill=null
_drillPathInfo=null
if (_bDrillMode)
{
if (eventIsLeftButton(e,fr))
{
_bodrill=o.getAttribute("bodrill")
if (_bodrill)
{
escapeFormatPainter()
_bodrill=escapeCR(_bodrill);
eval("fillDrillInfo("+_bodrill+")")
simulateClick(e)
drillCB(_drillInfo.action)
return _webKit?true:false
}
}
else
{
if(this.tagName=='AREA')
{
_bodrill=o.getAttribute("bodrill")
_useParentContextMenu=true
}
o=getparentBID(o)
bid=o.getAttribute("bid")
}
}
if(!isInteractive)
{
simulateClick(e)
return;
}
var elt=findByBID(doc,bid)
var cn=elt?elt.className:""
var isSection = (cn=="section")
var isHeader  = (cn=="pageHeader")
var isFooter  = (cn=="pageFooter")
var isBody    = (cn=="body")
if (!(isHeader||isFooter||isBody))
{
_curIdRef=o.getAttribute("idref");
_curIdRefBid=bid
}
var z=getZoom()/100
var pageContainer = getPageContainer()
_lastX= ((- getPos(o,pageContainer).x*z - pageContainer.offsetLeft + getScrollX(fr) + eventGetX(e,fr)) / z)
_lastY= ((- getPos(o,pageContainer).y*z - pageContainer.offsetTop  + getScrollY(fr) + eventGetY(e,fr)) / z)
_lastLayer=o
_lastElt=elt
if (isInteractive && eventIsLeftButton(e,fr) && window._isDDEnabled && DDActionMethod)
{
DDActionMethod(o,cn)
simulateClick(e)
return _webKit?true:false
}
if (isSection||isHeader||isFooter||isBody)
{
var oldSel=selectedBid[bid]
deselectAll(bid)
if ((oldSel!=1)||(!ctrl))
{
selectedBid[bid]=1
selectObj(bidTable[bid],true,bid)
} else { 
selectedBid[bid]=null
selectObj(bidTable[bid],false,bid)
}
if (eventIsLeftButton(e,fr)&&!isBody)
{
var isFooter=isFooter,z=getZoom()/100
var zone=getCellZone(o,e,d,true,isFooter,isHeader)
o.downZone=zone
o.resizeZoneX=absxpos(e,z,fr)
o.resizeZoneY=absypos(e,z,fr)
o.mouseMoved=false
o.isSectionOrHeaderOrFooter=true
o.isFooter=isFooter
o.isSection=isSection
globResizeObj=o
if (!drawResize(e)) {
globResizeObj = null
}
var rWin=getReportFrame()
if (rWin == null) return
var rDoc=rWin.document
rDoc.body.onmousedown=releaseSelection
rDoc.body.onmousemove=globmmove
rDoc.body.onmouseup=globmup
}
selectionChanged(true)
}
else
{
deselectAll(bid,"section")
deselectAll(bid,"pageHeader")
deselectAll(bid,"pageFooter")
deselectAll(bid,"body")
if (ctrl)
{
_isSelectStartEnabled=false
escapeFormatPainter()
if (selectedBid[bid])
{
if (eventIsLeftButton(e,fr))
{
selectedBid[bid]=null
selectObj(bidTable[bid],false,bid)
}
}
else
{
selectedBid[bid]=1
selectObj(bidTable[bid],true,bid)
}
selectionChanged(true)
setTimeout("_isSelectStartEnabled=true")
if (_ie) e.returnValue=false
} else if (shift) {
_isSelectStartEnabled=false
escapeFormatPainter()
setTimeout("_isSelectStartEnabled=true")
} else {
var z=getZoom()/100
if ((selectedBid[bid]!=1)||eventIsLeftButton(e,fr))
deselectAll(bid)
if (selectedBid[bid]==null) 
selectSingle(bid)
if (eventIsLeftButton(e,fr))
{
if (cn=="tableCell") 
{
var zone=getCellZone(o,e,d,true,false,true)
o.downZone=zone
o.resizeZoneX=absxpos(e,z,fr)
o.resizeZoneY=absypos(e,z,fr)
o.mouseMoved=false
globResizeObj=o
if (!drawResize(e)) {
globResizeObj = null
}
} 
else if ((cn != "block")||(elt.block.blockType > 10)) 
{
var zone=getCellZone(o,e,d)
o.downZone=zone
o.resizeZoneX=absxpos(e,z,fr)
o.resizeZoneY=absypos(e,z,fr)
o.mouseMoved=false
globResizeObj=o
if (!drawResize(e)) {
globResizeObj = null
}
}
var rWin=getReportFrame()
if (rWin== null) return
var rDoc=rWin.document
rDoc.body.onmousedown=releaseSelection
rDoc.body.ondragstart=bodyDragStart
rDoc.body.onmousemove=globmmove
rDoc.body.onmouseup=globmup
}
}
selectionChanged(true)
}
if (isInteractive && eventIsLeftButton(e,fr) && (window._formatPainterBid != null) && (_formatPainterBid != bid))
{
var p=urlParamsNoBID()
p += "&sTargetBid="+ bid + "&sBid=" + _formatPainterBid
if ((_reportIdxSrc != null) && (_reportIdxSrc != iReportID)){
p += "&reportIdxSrc="+ _reportIdxSrc
}
wt()
setTimeout('frameNav("Report","' + _root + "processFormatPainter"+_appExt + p + '")',1);
formatPainterStopIfSingleAction(e)
}
if (_ie&&globResizeObj)
this.setCapture(true)
simulateClick(e)
if (!_webKit)
return false
}
_useParentContextMenu=false
function contextMenu(e)
{
if (_saf)
this.onmousedown(e)
incContextMenu(this,e)
return false
}
_bodrillMenuInfo=null
function incContextMenu(l,e)
{
if (!isEnableUserRight(_usrShowRightClickMenu))
return
var w= getReportFrame()
if (w==null) return
if (_ie) {
e=w.event
}
escapeFormatPainter()
var bid=l.getAttribute?l.getAttribute("bid"):null
if(_bDrillMode)
{
if(_useParentContextMenu)
_useParentContextMenu=false
else 
_bodrill=l.getAttribute?l.getAttribute("bodrill"):null
if(_bodrill)
{
_bodrill=escapeCR(_bodrill);
_bodrillMenuInfo=_bodrill
eval("fillDrillInfo("+_bodrill+")")
bid=_drillInfo.bID
}
else
{
_drillPathInfo=null
}
}
else
{
_bodrill=null
_drillPathInfo=null
}
if(isInteractive) {
setTimeout('showContextMenu("'+bid+'",'+ (_saf? (absxpos(e) - getScrollX(w)):absxpos(e)) + ',' + (_saf? (absypos(e) - getScrollY(w)):absypos(e)) + ')',1)
} else {
setTimeout('showDrillContextMenu("'+bid+'",'+ (_saf? (absxpos(e) - getScrollX(w)):absxpos(e)) + ',' + (_saf? (absypos(e) - getScrollY(w)):absypos(e)) + ')',1)
}
e.cancelBubble=true
return false
}
_emptyURL="url("+_skin+"../transp.gif)"
function registerBid(bid,l)
{
var t=bidTable[bid]
if (t==null) t=bidTable[bid]=new Array
t[t.length]=l
}
function makeSectionSensitive(l)
{
if (_ie&&(l.getAttribute("isSection")=="0"))
{
var st=l.parentNode.style
st.backgroundImage=_emptyURL
st.backgroundRepeat="no-repeat"
}
}
function ddDragStartCB()
{
return false;
}
_elemRect=newRect(0,0,0,0)
_viewPortRect=newRect(0,0,0,0)
function intersectRect(r1,r2)
{
return !( ((r1.x+r1.w) < r2.x) || (r1.x > (r2.x+r2.w)) || ((r1.y+r1.h) < r2.y) || (r1.y > (r2.y+r2.h)))
}
function newRect(x,y,w,h)
{
var o=new Object
o.x=x
o.y=y
o.w=w
o.h=h
return o
}
function setViewPortRect()
{
var fr=getReportFrame()
_viewPortRect.x=getScrollX(fr)
_viewPortRect.y=getScrollY(fr)
_viewPortRect.w=winWidth(fr)
_viewPortRect.h=winHeight(fr)
return _viewPortRect
}
function addBOEvents(l,winRect)
{
if (l.nodeType==3)
return
var bid=l.getAttribute?l.getAttribute("bid"):null,n=l.childNodes
if (isPicker && bid!=null)
{
registerBid(bid,l)
l.oncontextmenu=pickerContextMenu
l.onmousedown=pickerClickCB
l.onmouseover=pickerOverCB
l.onmousemove=mmove
l.onmouseout=pickerOutCB
makeSectionSensitive(l)
}
else
{
if (isInteractive && bid!=null)
{
makeSectionSensitive(l)
if (_ie&&(l.tagName=="TD"))
{
if (l.innerHTML=="&nbsp;")
l.innerHTML="<div>&nbsp;</div>"
var lst=l.style
var lclst=l.currentStyle
if (lclst.backgroundImage||lclst.backgroundColor)
{
}
else
{
lst.backgroundImage=_emptyURL
lst.backgroundRepeat="no-repeat"
}
}
registerBid(bid,l)
addDblClickCB(l,viewDblClickCB)
l.onmousedown=viewClickCB
l.oncontextmenu=contextMenu
l.onmousemove=mmove
l.onmouseover=mover
l.onmouseup=elemup
if (_ie)
{
l.ondragover=dragOverCB
l.ondrop=dropCB
}
}
if(_bDrillMode)
{
var bodrill=l.getAttribute?l.getAttribute("bodrill"):null
if (bodrill!=null)
{
l.onmousedown=viewClickCB
l.onmouseover=drillOverCB
l.onmouseout=drillOutCB
if (l.tagName!="AREA")
{
l.oncontextmenu=contextMenu
}
else
{
if (l.tagName=="IMG")
{
l.ondragstart=ddDragStartCB
}
l.onmousemove=drillMoveCB
if(!isInteractive)
{
var divl=l.parentNode.parentNode
divl.oncontextmenu=contextMenu
}
}
}
}
}
if ((bid != null) && (l.tagName == "DIV")) {
var tdc = l.getAttribute?l.getAttribute("trackdata"):""
if (tdc && (tdc != "")) {
var rWin=getReportFrame()
if (rWin == null) return
var rDoc=rWin.document
var divSt = l.style
divSt.borderWidth="2px"
divSt.borderColor="#FFB49D"
var newImg=rDoc.createElement('img')
newImg.id= "chartTdcIcon_" + bid
newImg.src=_img+'tdc.gif'
newImg.onclick=clickIconChartCB
newImg.alt="The data in this chart has changed.\nClick on the icon to turn the chart into a table and view data changes."
var imgSt=newImg.style
imgSt.position="absolute"
imgSt.zIndex=100
imgSt.top=0
imgSt.left=0
imgSt.width="24px"
imgSt.height="24px"
l.appendChild(newImg)
}
}
if (n!=null)
{
var count=n.length
for (var i=0;i<count;i++) addBOEvents(n[i],winRect)
}
}
function clickIconChartCB()
{
var o=this, id = o.getAttribute?o.getAttribute("id"):"", tmpAr = id.split("_")
if (tmpAr.length <= 0) return
bid = tmpAr[1]
var url = _root + "processTurnTo" + _appExt
url += urlParamsNoBID()
url += "&sBid="+ bid
url += "&sNewBlockType=0&sNewBlockSubType=1"
wt()
setTimeout('frameNav("Report","' + url + '")',1);
}
function reportBodyMouseDown()
{
return false
}
function goToAnchor(rWin)
{
var anchor=getQueryParamValue(rWin.location.href, "sAnchor");
if (anchor!='')
{
var theLinks = rWin.document.getElementsByName(anchor)
if (theLinks&&(theLinks.length>0))
{
for (var i=0; i < theLinks.length; i++ )
{
if (theLinks[i].name == anchor)
{
theLinks[i].scrollIntoView();
break;
}
}
}
}
}
function delayedWomLoadCB()
{
var rWin=getReportFrame()
if (rWin == null) return
var rDoc=rWin.document
goToAnchor(rWin);
if (!_ie)
rDoc.body.addEventListener("mousedown",reportBodyMouseDown,true)
addBOEvents(rDoc)
if (isInteractive)
{
if (_webKit)
{
rDoc.body.onselectstart=reportBodyMouseDown
}
rWin.cm=rWin.oldCm
rDoc.onmousedown=releaseSelection
rDoc.body.onmouseout=mouseOut
append(rDoc.body,'<div id="boresizediv" style="overflow:hidden;z-index:10000;position:absolute;display:none;background-image:url('+_img+'resizepattern.gif);"></div>',rDoc)
boresizediv=rDoc.getElementById("boresizediv")
rDoc.onmousemove=globmmove
rDoc.onmouseup=globmup
createMoveLayers(rWin,rDoc)
isWOMLoaded=true
restoreSelection()
if (_globalWOMCallback!=null)
_globalWOMCallback()
if (_setFocusToViewer)
{
_setFocusToViewer=false
rDoc.focus()
}
rDoc.oncontextmenu=contextMenu
}
eventManager.notify(_EVT_WOM_LOADED)
}
function womLoadCB()
{
tabsZone.select(iReportID)
var frm=getReportFrame()
try
{
if ((frm==null)||(frm.document==null)||(frm.document.body==null))
setTimeout("womLoadCB()",100)
else
delayedWomLoadCB()
}
catch(ex)
{
setTimeout("womLoadCB()",100)
} 
}
function getSelectedReportIndex()
{
return tabsZone.getSelection().index
}
function getSelectedReportName()
{
return tabsZone.getSelection().name
}
function selectReport(index,bForceReDraw,bUndoEnabled,otherSEntry,bNotifyEvent)
{
if(index<0) return; 
if (bForceReDraw==null)
bForceReDraw=false;
if (bUndoEnabled==null)
bUndoEnabled=false;
if (bNotifyEvent==null)
bNotifyEvent=true;
var repInfo=arrReports[index]
if (index!=iReportID || bForceReDraw )
{
if (bNotifyEvent)
eventManager.notify(_EVT_REP_SELECTED,index)
var p=urlParams(true,true,otherSEntry,null,index,repInfo.pageMode,repInfo.repMode,repInfo.curPage,true) 
frameNav("Report",_root+"report"+_appExt+p+"&sUndoEnabled="+bUndoEnabled+((isPDF && _propShowTabs4PDF)?"&viewType=P":""));
if (isInteractive)
{
for (var i in selectedBid)
selectedBid[i]=null
}
}
showPageNavigIcons((repInfo.pageMode=='Page'||repInfo.pageMode=='QuickDisplay')?true:false)
updatePageModeMenu(repInfo.pageMode);
if (tdcShowChangesIcon.isDisplayed())
tdcShowChangesIcon.check(repInfo.sc)
}
rtabsObs1=newObserverTwoEvents(_EVT_REP_RENAMED,_EVT_REP_DATAOK,reportTabsCB)
rtabsObs2=newObserverTwoEvents(_EVT_REP_DELETED,_EVT_REP_DATAOK,reportTabsCB)
rtabsObs3=newObserverTwoEvents(_EVT_REP_ADDED,_EVT_REP_DATAOK,reportTabsCB)
rtabsObs4=newObserverTwoEvents(_EVT_REP_DUPLICATED,_EVT_REP_DATAOK,reportTabsCB)
rtabsObs5=newObserverTwoEvents(_EVT_REP_MOVED,_EVT_REP_DATAOK,reportTabsCB)
rtabsObs6=newObserverTwoEvents(_EVT_UNDO_REDO,_EVT_REP_DATAOK,reportTabsCB)
rtabsObs7=newObserverOneEvent(_EVT_REP_TDC_ACTIVATED,reportTabsCB)
function reportTabsCB(evt,data)
{
switch(evt)
{
case _EVT_REP_TDC_ACTIVATED:
for (var i=0; i < tabsZone.items.length; i++) {
var tab=tabsZone.items[i], icnIdx=0, alt="Report", report = arrReports[i]
if (report.repMode=='Analysis') 
{
icnIdx=1
alt="Drilled Report"
}
tab.change(report.name,tab.cb,tab.value,tab.icon,16,16,(report.sc?16:0),16*icnIdx,tab.dblclick,alt)
}
break
case _EVT_REP_RENAMED:
var index=parseInt(data)
var tab=tabsZone.items[index]
var icnIdx=0
var alt="Report"
var report = arrReports[index]
if (report.repMode=='Analysis') 
{
icnIdx=1
alt="Drilled Report"
}
tab.change(report.name,tab.cb,tab.value,tab.icon,16,16,(report.sc?16:0),16*icnIdx,tab.dblclick,alt)
tabsZone.select(index)
break
case _EVT_REP_DELETED:
var index=parseInt(data)
tabsZone.remove(index)
break;
case _EVT_REP_ADDED:
case _EVT_REP_DUPLICATED:
var last=arrReports.length-1
var icnIdx=0
var alt="Report"
var report = arrReports[last]
if (report.repMode=='Analysis') 
{
icnIdx=1
alt="Drilled Report"
}
tabsZone.add(report.name,null,-1,_img+"framework.gif",16,16,(report.sc?16:0),16*icnIdx,alt)
tabsZone.select(-1)
break
case _EVT_REP_MOVED:
var from=parseInt(data.from)
var dest=parseInt(data.dest)
initTabsZone(dest)
break
case _EVT_UNDO_REDO:
var index=parseInt(data)
initTabsZone(index)
break;
}
}
function initTabsZone(repIdx)
{
tabsZone.removeAll()
for (var i in arrReports)
{
rep=arrReports[i]
var icnIdx=0
var alt="Report"
if(rep.repMode=='Analysis') 
{
icnIdx=1
alt="Drilled Report"
}
tabsZone.add(rep.name,null,-1,_img+"framework.gif",16,16,(rep.sc?16:0),16*icnIdx,alt)
}
tabsZone.select(repIdx)
}
fetchDataObv1=newObserverTwoEvents(_EVT_DOC_UPDATE,_EVT_PAGE_LOADED,fetchDataCB)
fetchDataObv2=newObserverTwoEvents(_EVT_REFRESH_DATA,_EVT_PAGE_LOADED,fetchDataCB)
_runQueryIdx = null;
_bRetrieveData = true ;
function fetchDataCB()
{
if (!isPDF)
{
var otherParams ="";
var p=urlParams(false,true)
if (p!="")
{
if(_bRetrieveData)
{
otherParams = "&bNoFetchDataMsg=true";
if(_runQueryIdx!=null) 
    otherParams="&iDPIndex="+_runQueryIdx;
}
else 
{
_bRetrieveData = true; 
}
frameNav("DlgFrame","processDataFetch"+_appExt+p+otherParams)
}
}
}
fetchDataObv3=newObserverTwoEvents(_EVT_UNDO_REDO,_EVT_PAGE_LOADED,updateDPsRowsCB)
function updateDPsRowsCB()
{
if (strDocType!="rep" || !isPDF)
{
var p=urlParams(false,true)
if (p!="")
{
frameNav("DlgFrame","processDataFetch"+_appExt+p)
}
}
}
function displayNoDataToFetch(s,cb)
{
showAlertDialog("No data to retrieve in "+s,"Retrieving Data",0,cb);
}
function updateDPsRows(arr)
{
if(arr.length != arrDPs.length) return;
for (var i in arrDPs)
{
arrDPs[i].rows = parseInt(arr[i]);
}
eventManager.notify(_EVT_FETCH_DATA);
}
sbarDocUpdateObv=newObserverTwoEvents(_EVT_DOC_UPDATE,_EVT_PAGE_LOADED,resizeCB)
sbarRefreshDataObv=newObserverTwoEvents(_EVT_REFRESH_DATA,_EVT_PAGE_LOADED,resizeCB)
sbarUndoRedoObv=newObserverTwoEvents(_EVT_UNDO_REDO,_EVT_PAGE_LOADED,resizeCB)
function setHelp()
{
var section="context.htm?contextid=on_report_analysis_root"
var subPath="webintelligence/html_report/html/default.htm"
if (!isInteractive)
{
section="context.htm?contextid=viewing_drill_root"
subPath="webintelligence/viewing/html/default.htm"
}
if (_showQueryPanel)
{
section="context.htm?contextid=html_query_root"
subPath="webintelligence/html_query/html/default.htm"
}
subHelpObj = initHelpSection(subPath);
setHelpSection(subHelpObj, section);
}
function switchToolbars()
{
mainContainer.setDisplay(bFullScreen?false:(!_showQueryPanel&&!isPDF))
pdfContainer.setDisplay(!_showQueryPanel&&isPDF)
if (isInteractive)
editSwitchToolbars()
if (!_showQueryPanel)
{
var c=leftPane
c.valueShow("reportelts",!isPDF)
c.valueShow("resultObj",!isPDF)
c.valueShow("filtmap",!isPDF)
c.valueShow("reportmap",!isPDF)
if (_findAvailable)
c.valueShow("advFind",!isPDF)
if (isPDF)
{
var sel=c.getSelection()
var value=_leftPaneSel
if ((value=="reportelts")||
(value=="resultObj")||
(value=="filtmap")||
(value=="reportmap")||
(value=="advFind"))
{
var selPane=isInteractive?"docinfo":(isEnableUserRight(_usrRefreshDoc)?"quickFilter":"");
if (selPane != "")
{
_leftPaneSel = selPane; 
if (_showLeftPane&&(c.valueSelect(_leftPaneSel)))
{
var key=c.getSelection().value
recordProp("CDZ_VIEW_leftPaneSel",key)
}
}
else
c.valueSelect('');
}
}
else
{
if (_leftPaneSel == "quickFilter")
_leftPaneSel = isEnableUserRight(_usrRefreshDoc)?"quickFilter":"reportmap";
c.valueSelect(_leftPaneSel);
}
}
setHelp()
setTimeout("resizeCB()",1)
}
function changeEntry(s)
{
eventManager.notify(_EVT_BEFORE_TOKEN_UPDATE)
strEntry=s
}
function updateWOM()
{
_moutTimer=null
_currentMoveLayer=null
globMoveObj=null
_isMoving=false
setDrillMode();
isWOMLoaded=false
womLoadCB()
}
_currContextMenu=null
function showContextMenu(bid,x,y)
{
var pos=getPos(reportBorder.layer),m
x=x+pos.x
y=y+pos.y
var isComposite=false
var elts = getSelectedElts();
var firstCN=null
for (var i in elts)
{
var elt=elts[i],curCN=elt.className+(((elt.block != null) && (elt.block.blockType > 10))?"chart":"")
if (firstCN==null)
firstCN=curCN
else
{
if (curCN!=firstCN)
{
isComposite=true
break
}
}
}
if (isComposite)
{
m=compositeSelContextMenu
m.show(true,x,y)
_currContextMenu=m
}
else
{
var elt=findByBID(doc,bid),cn=elt?elt.className:""
if (elt!=null)
{
if (cn=="section")
m=sectContextMenu
else if (cn=="tableCell")
m=cellContextMenu
else if (cn=="reportCell")
{
if (elt.isSect)
m=reportSectCellContextMenu
else
m=freeCellContextMenu
}
else if (cn=="cell")
m=freeCellContextMenu
else if (cn=="block")
m=blockContextMenu
else
m=reportContextMenu
m.show(true,x,y)
_currContextMenu=m
}
else
{
for (var i in selectedBid)
{
selectedBid[i]=null
if (bidTable[i]!=null)
selectObj(bidTable[i],false,i)
}
selectionChanged(true)
m=reportContextMenu
m.show(true,x,y)
_currContextMenu=m
}
}
}
zoomObs=newObserverOneEvent(_EVT_WOM_LOADED,setZoomCB)
function setZoomCB(evt,data)
{
var idx=(data!=null)?parseInt(data):null
if (idx==null)
idx=iReportID;
var zoom = arrReports[idx].zoom;
zoomCombo.valueSelect(zoom);
}
function checkZoomCB()
{
var usrVal=zoomCombo.getSelection().value
var len=usrVal.length
if (usrVal[len-1]=='%')
usrVal=usrVal.substring(0,len-1)
var val=parseFloat(usrVal)/100
if (isNaN(val) || (val < 0) || (val > 5))
{
var zoom=arrReports[iReportID].zoom
zoomCombo.valueSelect(zoom)
return false
}
return true
}
function getZoom(idx)
{
if (_ie)
{
if (idx==null)
idx=iReportID;
return arrReports[idx].zoom
}
else
return 100
}
function applyZoom()
{
if (_ie)
{
var st=getPageContainer()
if (st == null) return
st=st.style
var zoom = (zoomCombo&&zoomCombo.layer) ? (parseFloat(zoomCombo.getSelection().value)) : 100
st.zoom=zoom/100
if (isInteractive)
{
if (_moutTimer!=null)
{
clearTimeout(_moutTimer)
_moutTimer=null;
}
showMoveZone(false)
}
eventManager.notify(_EVT_ZOOM_CHANGED,zoom)
}
}
function initZoomCombo()
{
if (!_ie)
{
if (window.zoomCombo)
zoomCombo.setDisplay(false)
repairToolbar(pal1)
return
}
zoomCombo.add("10%","10")
zoomCombo.add("25%","25")
zoomCombo.add("50%","50")
zoomCombo.add("75%","75")
zoomCombo.add("100%","100",true)
zoomCombo.add("150%","150")
zoomCombo.add("200%","200")
zoomCombo.add("500%","500")
}
function findByBID(elt,bid)
{
if (elt.bid==bid)
return elt
if (elt.children)
{
var children=elt.children,len=children.length
for (var i=0;i<len;i++)
{
var ret=findByBID(children[i],bid)
if (ret)
return ret
}
}
return null
}
function findByClassName(elt,className)
{
if (elt.className==className)
return elt
if (elt.children)
{
var children=elt.children,len=children.length
for (var i=0;i<len;i++)
{
var ret=findByClassName(children[i],className)
if (ret)
return ret
}
}
return null
}
function getBids(singleBID) {
var b=""
if (isInteractive)
{
for (var i in selectedBid)
{
if (selectedBid[i]!=null)
{
if (b!="")
b+=","
b+=i
if (singleBID)
break
}
}
}
return b
}
function urlParams(singleBID,acceptNoBID,otherSEntry,otherViewerID,otherReportID,otherPageMode,otherReportMode,otherPage, dontKeepScrollInfo)
{
var b = getBids(singleBID)
if ((b=="")&&(!acceptNoBID))
return ""
return urlParamsNoBID(otherSEntry,otherViewerID,otherReportID,otherPageMode,otherReportMode,otherPage,dontKeepScrollInfo) + (singleBID?("&sBid="+escape(b)):("&bids="+escape(b)))
}
function urlParamsNoBID(otherSEntry,otherViewerID,otherReportID,otherPageMode,otherReportMode,otherPage,dontKeepScrollInfo)
{
if (otherSEntry==null)
otherSEntry=strEntry
if (otherViewerID==null)
otherViewerID=iViewerID
if (otherReportID==null)
otherReportID=iReportID
if (otherPageMode==null)
otherPageMode=strPageMode
if (otherReportMode==null)
otherReportMode=strReportMode
if (otherPage==null)
otherPage=iPage
if (dontKeepScrollInfo) {
var rep=arrReports[iReportID]
if (rep!=null)  {
rep.scrollX=0
rep.scrollY=0
}
 } else  {
saveScrollingInfo()
}
return "?iViewerID="+escape(otherViewerID)+"&sEntry="+escape(otherSEntry)+"&iReport="+otherReportID+"&sPageMode="+otherPageMode+"&sReportMode="+otherReportMode+"&iPage="+otherPage+"&zoom="+getZoom(otherReportID)+"&isInteractive="+((isInteractive || isPicker)?"true":"false")+(_curIdRef?"&idRef="+escape(_curIdRef):"")
}
function recordProp(key,val)
{
frameNav("SecondDlgFrame",_root+"processUserPref"+_appExt+"?sKey="+key+"&sVal="+val)
}
function moveToPage(dir)
{
_curIdRef=null
var p=urlParams(true,true,null,null,null,null,null,dir,true)
if (p!="")
{
p+="&nbPage="+nbPage
p+="&sUndoEnabled=false"
if (strReportMode == "Viewing")
frameNav("Report","report"+_appExt+p,true)
else
frameNav("Reportbloc","viewReport"+_appExt+p,true,getFrame("Report"))
}
}
pageObs1=newObserverOneEvent(_EVT_WOM_LOADED,updatePageWidgetsCB)
function updatePageWidgetsCB()
{
firstIcn.setDisabled(isFirstPage)
previousIcn.setDisabled(isFirstPage)
nextIcn.setDisabled(isLastPage)
lastIcn.setDisabled(isLastPage)
var s=''
if ( !isNaN(nbPage) )
s="/"+nbPage
maxPage.setHTML(s)
pageNumber.setValue(iPage)
}
function showPageNavigIcons(show)
{
pageLab.setDisplay(show)
firstIcn.setDisplay(show)
previousIcn.setDisplay(show)
pageNumber.setDisplay(show)
maxPage.setDisplay(show)
nextIcn.setDisplay(show)
lastIcn.setDisplay(show)
}
function updatePageModeMenu(strPageMode)
{
quickMode.check((strPageMode=="QuickDisplay")&&!isPDF)
pageMode.check((strPageMode=="Page")&&!isPDF)
webMode.check((strPageMode=="Listing")&&!isPDF)
pdfMode.check(isPDF)
}
function saveScrollingInfo()
{
if ( isPDF || _showQueryPanel || _displayedModeIsPDF )
return
var w=getReportFrame()
if (w==null) return
if (w.document.body==null) return
var rep=arrReports[iReportID]
if (rep==null)
return
rep.scrollX=getScrollX(w)
rep.scrollY=getScrollY(w)
}
function restoreScrollingInfo()
{
if ( isPDF || _showQueryPanel || !isInteractive )
return
var w=getReportFrame()
if (w == null) return
if (w.document.body==null)
return
var rep=arrReports[iReportID]
if (rep==null)
return
if (rep.curPage==iPage)
winScrollTo(rep.scrollX,rep.scrollY,w)
}
newObserverOneEvent(_EVT_DOC_UPDATE,resetStatesCB);
function resetStatesCB()
{
arrState.length=0
curState=-1
}
stateObs1=newObserverOneEvent(_EVT_WOM_LOADED,saveStateCB)
function saveStateCB(evt,data)
{
if (!bUndoableAction)
return
var len=arrState.length;
if ((curState>=0) && ((curState+1) <= len) && (arrState[curState].sEntry==strEntry))
return;
if ( (iStorageTokenStackSize > 0) && (len >= iStorageTokenStackSize) )
{
if ((curState+1)< len)
{
curState+=1;
}
else
{
arrayRemove(window,'arrState',0)
}
}
else
{
curState+=1
}
arrState[curState]=newViewerState(strEntry,iViewerID,iReportID,arrReports)
arrState.length=curState+1
updateUndoRedoIcn()
}
stateObs2=newObserverTwoEvents(_EVT_REP_SELECTED,_EVT_PAGE_LOADED,saveSelectedReportCB)
function saveSelectedReportCB()
{
var index=parseInt(iReportID)
arrState[curState].selRep=index
}
stateObs3=newObserverOneEvent(_EVT_ZOOM_CHANGED,saveZoomCB)
function saveZoomCB(evt,data)
{
var zoom=parseFloat(data)
arrReports[iReportID].zoom=zoom
arrState[curState].arr[iReportID].zoom=zoom
adjustPageBorders()
}
stateObs4=newObserverOneEvent(_EVT_WOM_LOADED,savePageCB)
function savePageCB(evt,data)
{
arrReports[iReportID].curPage=iPage
arrState[curState].arr[iReportID].curPage=iPage
}
function updateUndoRedoIcn()
{
var l=arrState.length
if (curState<1)
undo.setDisabled(true)
else
undo.setDisabled(false)
if (curState==(l-1))
redo.setDisabled(true)
else
redo.setDisabled(false)
}
function undoRedo(step)
{
if (getReportFrame()) { 
curState=curState+step
}
if (curState < 0) curState=0
var st=arrState[curState]
eventManager.notify(_EVT_UNDO_REDO,st.selRep)
arrReports.length=0
for(var i in st.arr)
arrReports[i]=newReportInfo(st.arr[i].name,st.arr[i].pageMode,
st.arr[i].repMode,st.arr[i].nbPage,st.arr[i].curPage,st.arr[i].isLeaf,st.arr[i].zoom, st.arr[i].sc);
eventManager.notify(_EVT_REP_DATAOK)
updateUndoRedoIcn()
_curIdRef=null
var p=urlParams(true,true,st.sEntry,st.iViewerID,st.selRep,
st.arr[st.selRep].pageMode,st.arr[st.selRep].repMode,st.arr[st.selRep].curPage)
if (p!="")
{
p+="&sUndoEnabled=false"
frameNav("Report",_root+"report"+_appExt+p,true)
}
}
function restoreAfterError()
{
var st=arrState[curState]
if (st!=null)
{
var p=urlParamsNoBID(st.sEntry,st.iViewerID,st.selRep,
st.arr[st.selRep].pageMode,st.arr[st.selRep].repMode,st.arr[st.selRep].curPage)
if (p!="")
{
p+="&sUndoEnabled=false";
frameNav("ThirdDlgFrame","processRestoreAfterError"+_appExt+p,false);
}
eventManager.notify(_EVT_RESTORE_AFTER_ERR)
}
}
reportMapStates=newHashTable();
function getRepMapState(sKey)
{
return reportMapStates.get(sKey);
}
function setRepMapState(sKey)
{
var arrExp=new Array();
for (var i=0; i<arrReportMap.length; i++)
{
if (arrReportMap[i][8])
arrExp[arrExp.length]=arrReportMap[i][0].valueOf();
}
reportMapStates.put(sKey,arrExp);
}
function switchToInteractive()
{
var p=urlParams(false,true)
if (p!="")
{
p+="&name="+convURL(strDocName)+"&docid="+strDocID+"&sRepoType="+strRepoType+"&doctype="+strDocType+"&forceViewType=I"
window.location="viewDocument"+_appExt+p
}
}
function launchReportPanel()
{
if(_defaultWebEditor=='R' || strDocType=="rep")
{
if (isDocModified())
{
var msg="The document has changed. The changes will be visible in your desktop application only after you have saved them. Do you want to save the document now?"
showPromptDialog(msg,"Edit",_promptDlgWarning,editWithRCYesCB,editWithRCNoCB)
}
else
edit("R")
}
else if(_defaultWebEditor=='I')
{
switchToInteractive()
}
else
{
if((_defaultWebEditor=='J') && (iReportID != strSavedReportIndex))
{
var p=urlParams(false,true)
if (p!="")
frameNav("DlgFrame","updateProperties"+_appExt+p)
return;
}
edit("H")
}
}
_editWithRCSaveBefore=false
if (_defaultWebEditor=='R' || strDocType=="rep")
{
editWithRCQuickSaveEventCB=function()
{
if (_editWithRCSaveBefore)
{
if (!_doNotHideOnLoadReport)
hideBlockWhileWaitWidget()
_editWithRCSaveBefore=false
edit('R')
}
}
editWithRCQuickSaveEvent=newObserverOneEvent(_EVT_PROCESS_QUICK_SAVE,editWithRCQuickSaveEventCB)
editWithRCSaveEvent=newObserverOneEvent(_EVT_PROCESS_SAVE,editWithRCQuickSaveEventCB)
editWithRCYesCB=function()
{
_editWithRCSaveBefore=true
saveOrSaveAs()
}
editWithRCNoCB=function()
{
edit('R')
}
}
processSaveEvent = newObserverOneEvent(_EVT_PROCESS_SAVE,processSaveEventCB)
processQuickSaveEvent = newObserverOneEvent(_EVT_PROCESS_QUICK_SAVE,processSaveEventCB)
function processSaveEventCB() 
{
    if (isInteractive)
    {
updateIsQPModified(false); 
    if (_showQueryPanel) 
    {
    storeReportQueryState(); 
    }   
}
isNew=false;
hideBlockWhileWaitWidget()
}
function saveOrSaveAs(fromQuery)
{
wt()
if (isNew) {
saveAs(fromQuery)
} else {
if (strLastSavedBy!='' && strLastSavedBy!=strUserName) {
saveAs(fromQuery)
} else {
save(fromQuery)
}
}
}
_doNotHideOnLoadReport=false
_disableBlockFrame=false
function wt(doNotHideOnLoadReport)
{
if (isInteractive)
{
clearMoutTimer()
_disableBlockFrame=true
}
newBlockWhileWaitWidget().show(true)
_doNotHideOnLoadReport=doNotHideOnLoadReport?true:false
}
_forcedID=null
function setClickCBID(forcedID)
{
_forcedID=forcedID
}
_reportIdxSrc=null
function clickCB()
{
var id=_forcedID!=null?_forcedID:this.id
_forcedID=null
if (isPicker && reportPickerClickCB(this, id))
return
_dontCloseDoc = false
if ((id != "formatPainter") && (id != "tabsZone") && (window.escapeFormatPainter)) {
escapeFormatPainter()
}
if (isInteractive && editClickCB(this, id))
return
switch(id)
{
case "findInDoc":
setPane("advFind",false,true)
break;
case "saveXLS":
var p = urlParamsNoBID() + "&doctype=" + strDocType 
p +=  "&viewType=" + (_saveAsXLSOptimized=="Y"? "O":"X")+"&saveReport=N"
frameNav("DlgFrame", _root + "downloadPDForXLS" + _appExt + p);
break
case "saveReportXLS":
var p = urlParamsNoBID() + "&doctype=" + strDocType 
p +=  "&viewType=" + (_saveAsXLSOptimized=="Y"? "O":"X")+"&saveReport=Y"
frameNav("DlgFrame", _root + "downloadPDForXLS" + _appExt + p);
    break
case "printDocIcn":
case "savePDF":
var p=urlParamsNoBID() + "&doctype=" + strDocType + "&viewType=P"+"&saveReport=N"
frameNav("DlgFrame", _root + "downloadPDForXLS" + _appExt + p);
break
case "saveReportPDF":
var p=urlParamsNoBID() + "&doctype=" + strDocType + "&viewType=P"+"&saveReport=Y"
frameNav("DlgFrame", _root + "downloadPDForXLS" + _appExt + p);
break    
case "savePDFOp":
    wt()
frameNav("DlgFrame", "language/"+_lang+"/html/PDFOptionsDialog.html");
break
case "saveCSV":
var p=urlParamsNoBID() + "&doctype=" + strDocType + "&viewType=C"
frameNav("DlgFrame", _root + "downloadCSV" + _appExt + p);
break
case "saveCSVOp":
    wt()
frameNav("DlgFrame", "language/"+_lang+"/html/CSVOptionsDialog.html");
break
case "quickMode":
var repInfo=arrReports[iReportID]
if ((repInfo.pageMode!="QuickDisplay")||isPDF)
{
wt()
pageMode.check(false)
webMode.check(false)
pdfMode.check(false)
repInfo.pageMode="QuickDisplay"
_reportPage="report"+_appExt
_displayedModeIsPDF=isPDF
isPDF=false
resizeCB()
showPageNavigIcons(true)
switchToolbars()
var p=urlParams(true,true,null,null,null,repInfo.pageMode,repInfo.repMode,repInfo.curPage)
frameNav("Report",_reportPage+p)
}
quickMode.check(true)
break;
case "pageMode":
var repInfo=arrReports[iReportID]
if ((repInfo.pageMode!="Page")||isPDF)
{
wt()
quickMode.check(false)
webMode.check(false)
pdfMode.check(false)
repInfo.pageMode="Page"
_reportPage="report"+_appExt
_displayedModeIsPDF=isPDF
isPDF=false
resizeCB()
showPageNavigIcons(true)
switchToolbars()
var p=urlParams(true,true,null,null,null,repInfo.pageMode,repInfo.repMode,repInfo.curPage)
frameNav("Report",_reportPage+p)
}
pageMode.check(true)
break
case "webMode":
var repInfo=arrReports[iReportID]
if ((repInfo.pageMode!="Listing")||isPDF)
{
wt()
quickMode.check(false)
pageMode.check(false)
pdfMode.check(false)
repInfo.pageMode="Listing"
_reportPage="report"+_appExt
_displayedModeIsPDF=isPDF
isPDF=false
resizeCB()
showPageNavigIcons(false)
switchToolbars()
var p=urlParams(true,true,null,null,null,repInfo.pageMode,repInfo.repMode,repInfo.curPage)
p+="&sUndoEnabled=false"
frameNav("Report",_reportPage+p)
}
webMode.check(true)
break
case "htmlMode":
var repInfo=arrReports[iReportID]
if (isPDF)
{
wt()
pdfMode.check(false)
var blnPageMode=(repInfo.pageMode=="Page"||repInfo.pageMode=="QuickDisplay")?true:false;
quickMode.check(repInfo.pageMode=="QuickDisplay")
pageMode.check(repInfo.pageMode=="Page")
webMode.check(!blnPageMode)
_reportPage="report"+_appExt
_displayedModeIsPDF=isPDF
isPDF=false
resizeCB()
showPageNavigIcons(blnPageMode)
switchToolbars()
var p=urlParams(true,true,null,null,null,repInfo.pageMode,repInfo.repMode,repInfo.curPage)
p+="&sUndoEnabled=false"
frameNav("Report",_reportPage+p)
}
break
case "pdfMode":
if (!isPDF)
{
quickMode.check(false)
pageMode.check(false)
pdfMode.check(false)
strPageMode="Listing"
_reportPage="getPDFView"+_appExt
_displayedModeIsPDF=isPDF
isPDF=true
switchToolbars()
resizeCB()
var p=urlParamsNoBID() + "&viewType=P&download=no";
frameNav("Report",_reportPage+p)
}
break
case "calcButton":
applyDefaultCalculation()
break
case "calcTotal":
case "calcSum":
case "calcCount":
case "calcAvg":
case "calcMin":
case "calcMax":
case "calcPercent":
wt()
var p=urlParams(true)
if (p!="")
frameNav("Report","processCalc"+_appExt+p+"&sCalcAction="+id+(this.par.id=="extracalcMenu"?"&iBottomCalc=1":""),true)
break;
case "firstIcn":
wt()
moveToPage("first");
break;
case "previousIcn":
var val = parseInt(pageNumber.getValue());
if (val > 1)
{
val--;
wt();
moveToPage(val.toString());
}
break;
case "pageNumber":
var val = pageNumber.getValue();
if ( (val < 1) || (!isNaN(nbPage) && val > nbPage) )
{
showAlertDialog("Invalid page number.","Error",0);
pageNumber.setValue(iPage);
}
else if ( val != iPage )
{
wt()
moveToPage(val)
}
break;
case "nextIcn":
var val = parseInt(pageNumber.getValue());
if (isNaN(nbPage) || val < nbPage)
{
val++;
wt();
moveToPage(val.toString());
}
break;
case "lastIcn":
wt()
moveToPage("last");
break;
case "saveDocAs":
_dontCloseDoc = true
wt()
saveAs();
break;
case "saveDocQuery":
_dontCloseDoc = true
wt()
var f = getQueryFrame();
if (f != null) f.submitQueries("savequery")
break;
case "saveDocQueryAs":
_dontCloseDoc = true
wt()
var f = getQueryFrame();
if (f != null) f.submitQueries("savequeryas")
break;
case "saveDoc":
case "saveDocPDF":
case "saveDocIcn":
//_dontCloseDoc = true 
//saveOrSaveAs() //Done For Reporting
break;
case "refresh":
case "refreshAll":
wt()
case "refreshPDF":
refreshDocument(null,true);
break;
case "purge":
case "purgeAll":
case "purgeQ":
case "purgeQAll":
purgeDocument(null,true,true);
break;
case "editDoc":
case "editDocPDF":
case "editDocIcon":
_dontCloseDoc=true
launchReportPanel()
break;
case "drillTool":
_showDrillBar=this.isChecked();
_showDrillBarChanged=true;
var p=urlParams(false,true)
if (p!="")
frameNav("Report","report"+_appExt+p,true);
break;
case "leftPaneButton":
case "togglepane":
case "leftZoneGrab":
//if (_showLeftPane) //Done For Reporting
//collapsePane(false,true)
//else
//expandPane(false,true)
break;
case "showStatus":
case "showStatusPDF":
_showStatus=this.isChecked();
showStatusPDF.check(_showStatus);
showStatus.check(_showStatus);
resizeCB();
recordProp("CDZ_VIEW_showStatus",_showStatus?"1":"0");
break;
case "undo":
if (curState>=1)
{
wt()
undoRedo(-1)
}
break;
case "redo":
if (curState<(arrState.length-1))
{
wt()
undoRedo(+1)
}
break;
case "zoomCombo":
applyZoom()
break;
case "tabsZone":
var index=tabsZone.getSelection().index
if (index!=iReportID)
wt()
selectReport(index)
break;
case "leftPane":
var key=this.getSelection().value
eventManager.notify(_EVT_LEFTCOMBO_CHANGED,key)
setPane(key,false,false)
recordProp("CDZ_VIEW_leftPaneSel",key)
_leftPaneSel=key
break
case "drill":
wt()
drillNav()
break;
case "snapshot":
wt()
getSnapshot();
break;
case "closeDoc":
backToParent()
break;
case "tdcActivate":
if (this.isChecked()) {
frameNav("DlgFrame","language/"+_lang+"/html/applyTDCAutoUpdate.html")
} else {
showPromptDialog("If you deactivate data tracking, you will not be able to compare your current data with the data before the last refresh. Do you want to continue?","Deactivate Data Tracking",1, deactivateTDC, cancelDeactivateTDC)
}
break;
case "showCurrentReportChangesTabsZone":
wt()
var p=urlParamsNoBID()
if (p!="")
frameNav("Report","processTrackDataChanges"+_appExt+p+"&sShowCurrentReportChanges="+tabsZoneMenu.showCurrentReportChangesTabsZone.isChecked(),true)
break;
case "showCurrentReportChangesFromContext":
wt()
var p=urlParamsNoBID()
if (p!="")
frameNav("Report","processTrackDataChanges"+_appExt+p+"&sShowCurrentReportChanges="+reportContextMenu.showCurrentReportChangesFromContext.isChecked(),true)
if (tdcShowChangesIcon.isDisplayed())
tdcShowChangesIcon.check(reportContextMenu.showCurrentReportChangesFromContext.isChecked())
break;
case "tdcShowChanges":
{
wt()
var p=urlParamsNoBID()
if (p!="")
frameNav("Report","processTrackDataChanges"+_appExt+p+"&sShowCurrentReportChanges="+tdcShowChangesIcon.isChecked(),true)
break;
}
case "tdcOptions": 
invokeTDCOptionDlgViaStatusBar(0)
break;
default:
showAlertDialog("Not implemented yet !","Development version",0)
}
}
function loadCB()
{
setHelp()
setKeyCB();
isPageLoaded=true
tabsZone.init()
leftPane.init()
mainPal.init()
pal1.init()
updateRefreshIcon();
if(isInteractive)
updatePurgeIcn();
if (isInteractive)
initEditWidgets()
updatePageModeMenu(strPageMode);
toolbarsMenu.init()
mainContainer.init()
pdfContainer.init()
leftPaneButton.check(_showLeftPane);
showStatus.check(_showStatus);
showStatusPDF.check(_showStatus);
initZoomCombo();
reportBorder.init()
ReportWidget.init()
statusBarContent.init();
statusBar.init();
statusPartial.init();
statusPartialText.init();
statusRefDate.init();
statusBarTDC.init();
statusBarTDCRef.init();
statusBarTDCRefDate.init();
leftZoneGrab.init()
setTimeout("if (isPDF) {updateWOM(); switchToolbars();}resizeCB()",1)
if(isInteractive && (_defaultWebEditor=='I') && disableQueryPanel)
editQueryIcon.setDisabled(true);
setUserRights()
if (bLaunchQP)
launchQueryPanel()
waitDlgBegin.show(false)
}
function addC(c,label,id,dfc)
{
return c.add(label,id,dfc==id)
}
function setPanes()
{
if (_showLeftPane) 
{
setPane(_leftPaneSel,false,false)
_leftPaneSel=leftPane.getSelection().value
}
}
function grabCB(x,y)
{
var newW=Math.max(50,x-6)
if (_leftPaneWidth!=newW)
{
_leftPaneWidth=newW
resizeCB()
recordProp("CDZ_VIEW_leftPaneW",""+newW)
}
}
function resizeCB()
{
if (isPageLoaded)
{
var tabsH=(isPDF && !_propShowTabs4PDF)?(_showStatus?18:1):24;
var lpw=_leftPaneWidth
var w=winWidth(),h=winHeight(),toolH=mainPal.getHeight()-4
var leftOff=_showLeftPane?10+lpw:(_ie?4:5)
var rightOff=6
var statusHeight=_showStatus?22:0
var iFramesH=Math.max(0,h-toolH-14-tabsH)
var minReportW=50
if(_showQueryPanel)
{
leftPane.move(-1000,0)
leftPane.resize(100,100)
tabsZone.resize(100,100)
tabsZone.move(-1000,0)
reportBorder.move(-1000,0)
ReportWidget.resize(100,100)
leftZoneGrab.setDisplay(false);
statusBar.move(0,-1000);
querypanelWidget.resize(Math.max(0,w-10),iFramesH)
querypanelBorder.move(4,toolH+8)
tabsDP.resize(w,tabsH)
tabsDP.move(4,toolH+iFramesH+9)
}
else if (bFullScreen)
{
mainPal.setDisplay(true);
tabsH=0
toolH=27
iFramesH=Math.max(h-toolH+1,0)
if ((w-leftOff-rightOff)<minReportW)
{
if (_showLeftPane)
{
leftOff=Math.max(25,w-(minReportW+25))
lpw=leftOff-10
}
}
if (!_showLeftPane)
leftOff=-1
leftZoneGrab.enableGrab(_showLeftPane)
if (_showLeftPane)
leftZoneGrab.setMinMax(80,w-50)
leftZoneGrab.move(_showLeftPane?5+lpw:-1000,toolH-1)
leftZoneGrab.resize(5,Math.max(0,iFramesH+tabsH))
//leftZoneGrab.setDisplay(_showLeftPane) // Done For Reporting
leftPaneX=_showLeftPane?0:-1000
leftPaneY=toolH-1
leftPane.resize(lpw+(_ie?7:6),Math.max(0,iFramesH+tabsH+1))
leftPane.move(leftPaneX+(_ie?-1:0),leftPaneY)
leftZoneGrab.setCollapsed(false,"Click here to hide the left panel, or drag to resize it")
tabsZone.resize(100,100);
tabsZone.move(-1000,0);
reportBorder.move(leftOff,toolH-1);
reportBorder.css.borderTopWidth=0
var repZoneW=Math.max(minReportW,w-leftOff-1);
ReportWidget.resize(repZoneW,iFramesH);
statusBar.move(0,-1000);
if (isInteractive)
hideQueryPanelZones()
}
else
{
iFramesH=Math.max(0,iFramesH-statusHeight)
leftZoneGrab.setDisplay(true)
leftZoneGrab.enableGrab(_showLeftPane)
if ((w-leftOff-rightOff)<minReportW)
{
if (_showLeftPane)
{
leftOff=Math.max(25,w-(minReportW+25))
lpw=leftOff-10
}
}
leftZoneGrab.setCollapsed(!_showLeftPane,_showLeftPane?"Click here to hide the left panel, or drag to resize it":"Click here to show the left panel")
if (_showLeftPane)
{
leftZoneGrab.setMinMax(80,w-50)
}
leftZoneGrab.move(_showLeftPane?5+lpw:(_ie?-1:0),toolH+8)
leftZoneGrab.resize(6,Math.max(0,iFramesH+tabsH+2))
leftPaneX=_showLeftPane?4:-1000
leftPaneY=toolH+8
leftPane.move(leftPaneX,leftPaneY)
leftPane.resize(lpw+2,Math.max(0,iFramesH+tabsH+2))
statusRefDate.setHTML(convStr(strLastRefreshDate));
statusPartial.setDisplay(blnPartialResult || hasSampleResult);
statusPartialText.setHTML('<nobr>'+(hasSampleResult?'Sampled Results':'Partial Results')+'<nobr>');
updateTDCInfoOnStatusBar()
var minTabW=isPDF?0:150
if (!isPDF || _propShowTabs4PDF)
{
tabsZone.resize(Math.max(minTabW,w-leftOff-rightOff),tabsH);
tabsZone.move(leftOff,toolH+iFramesH+9);
}
else
{
tabsZone.resize(100,100);
tabsZone.move(-1000,0);
}
reportBorder.move(leftOff,toolH+8);
var repZoneW=Math.max(minReportW,w-leftOff-rightOff);
ReportWidget.resize(repZoneW,iFramesH+(_ie?-1:0));
if (_showStatus)
{
statusBar.resize(Math.max(0,w+(_dtd4?-2:0)));
statusBar.move(0, toolH+iFramesH+tabsH+14);
}
else
{
statusBar.move(0,-1000);
}
if (isInteractive)
{
hideQueryPanelZones()
if (formulaContainer&&(formulaContainer.css)&&(formulaContainer.css.display!="none"))
formulaText.resize(Math.max(w-getPos(formulaText.layer).x-10))
}
}
}
}
function setPane(key,isRight,expand)
{
var s=_root+"lib/empty.html"
if (!isInteractive&&((key=="docinfo")||(key=="dataSum")||(key=="reportelts")||(key=="resultObj")||(key=="filtmap")))
key="reportmap"
if (!isEnableUserRight(_usrShowDocInfo) && (key=="docinfo")) key="reportmap"
if (!isEnableUserRight(_usrShowDataSummary) && (key=="dataSum")) key="reportmap"
if (!isEnableUserRight(_usrShowResultObj) && (key=="resultObj")) key="reportmap"
if (!isEnableUserRight(_usrShowFilterMap) && (key=="filtmap")) key="reportmap"
if ((!isEnableUserRight(_usrInsertDuplicate) || !isEnableUserRight(_usrShowResultObj)) && (key=="reportelts")) key="reportmap"
if ( (_UDZ_useCustomPrompts) && (key == "quickFilter")) key="reportmap"
if ((key == "advFind")&&(!_findAvailable))
key="reportmap"
if (isPDF)
key=isInteractive?"docinfo":"quickFilter"
leftPane.valueSelect(key)
if (expand)
expandPane(isRight)
}
function expandPane(isRight,saveProp)
{
_showLeftPane=true
leftPaneButton.check(true)
var sel=leftPane.getSelection()
setPane(sel?sel.value:_leftPaneSel,isRight,false)
resizeCB()
if (isPDF && _ie) frameReload(getFrame("Report").window);
if (saveProp)
recordProp(isRight?"CDZ_VIEW_rightPane":"CDZ_VIEW_leftPane","1")
leftZoneGrab.allowGrab=true
_dontCloseDoc = false
}
function collapsePane(isRight,saveProp)
{
_showLeftPane=false
leftPaneButton.check(false)
resizeCB()
var sel=leftPane.getSelection()
if (sel)
_leftPaneSel=sel.value
leftPane.valueSelect()
if (isPDF && _ie) frameReload(getFrame("Report").window);
if (saveProp)
recordProp(isRight?"CDZ_VIEW_rightPane":"CDZ_VIEW_leftPane","0")
leftZoneGrab.allowGrab=false
_dontCloseDoc = false
}
_bTdcActivate=false;
_bTDCAutoUpdateDataOnRefresh=false;
_refTDCDate=''; 
_refTDCDateInMs=0;
_lLastRefreshMilliSecDate=0;
tdcObs=newObserverOneEvent(_EVT_REP_TDC_ACTIVATED,updateTDCToolbar)
function invokeTDCOptionDlgViaStatusBar(tabIndex)
{
wt()
frameNav("DlgFrame","language/"+_lang+"/html/applyTDCOptions.html?tabIndex="+tabIndex)
}
function updateTDCInfoOnStatusBar()
{
statusBarTDC.setDisplay(_bTdcActivate)
if (_bTdcActivate)
{
var link=lnk(convStr(_bTDCAutoUpdateDataOnRefresh ? 'Auto update' : 'Fixed data'),"invokeTDCOptionDlgViaStatusBar(1);event.cancelBubble=true;return false")
statusBarTDCRef.setHTML('<nobr>'+ link +'<nobr>')
statusBarTDCRef.layer.style.cursor=_hand
statusBarTDCRefDate.setHTML(convStr(_refTDCDate));
}
}
function updateTDCToolbar(evt,data)
{
if (!isEnableUserRight(_usrEnableTrackDataChanges)) {
tdcActivateIcon.setDisplay(false)
tdcShowChangesIcon.setDisplay(false)
tdcOptionsIcon.setDisplay(false)
return
}
var show=true
if (_showQueryPanel || (strDocType=="rep")) {
show=false
}
tdcActivateIcon.setDisplay(false) //added as false from 'show'.Done For Reporting
tdcShowChangesIcon.setDisplay(false)  //added as false from 'show'. Done For Reporting
tdcOptionsIcon.setDisplay(false) //added as false from 'show'. Done For Reporting
if (show)
{
var tooltip=_bTdcActivate?"Deactivate Data Tracking":"Activate Data Tracking";
tdcActivateIcon.changeTooltip(tooltip);
}
tdcActivateIcon.check(false) // Done For Reporting _bTdcActivate to false
tdcShowChangesIcon.setDisabled(true); //Done For Reporting "!_bTdcActivate" to true
tdcOptionsIcon.setDisabled(true);//Done For Reporting "!_bTdcActivate" to true
var idx=parseInt(data), rep = arrReports[idx], show = rep.sc
if (window.reportContextMenu && reportContextMenu.showCurrentReportChangesFromContext) {
reportContextMenu.showCurrentReportChangesFromContext.check(show)
}
if (window.tabsZoneMenu && tabsZoneMenu.showCurrentReportChangesTabsZone) {
tabsZoneMenu.showCurrentReportChangesTabsZone.check(show)
}
tdcShowChangesIcon.check(show);
var tooltip= (!_bTdcActivate || !show || !rep.sc)?"Show changes":"Hide changes";
tdcShowChangesIcon.changeTooltip(tooltip);
updateTDCInfoOnStatusBar();
}
function arrowRollover(lyr,isLeft, isActive)
{
changeOffset(lyr,0,(isLeft?32:48)+(isActive?32:0))
}
function toUserUnit(value, isInch)
{
if (isInch){
if (_unitIsInch) return value;
else return (value * 2.54);
} else {
if (_unitIsInch)  return (value / 25.4);
else return (value / 10.0);
}
}
_longTextAllowed = false
_longTextAr = new Array()
_longTextID = 0
_curLongTextObj = null
function newLongTextObj(dlgTitle,dlgText,header)
{
    var o=new Object
    o.dlgTitle=dlgTitle
    o.dlgText=dlgText
    o.header=header    
    return o
}
function longText(title,value,header,par,frName)
{
header = (header)? header:''
frName = (frName)? frName:''
par= (par) ? par : ''
var descToolTip = value
var maxCharsLine = 60
var maxLines = 3
var maxTextLen = maxLines * maxCharsLine
var txtLen = descToolTip.length
var moreStr = "more..."
var text = ''
if (!_longTextAllowed && (txtLen > maxCharsLine))
{
_longTextAr[_longTextID] = newLongTextObj(title, value, header)
var iter = txtLen / maxCharsLine
var iter = (iter > 3) ? 3 : iter
for (var i = 0; i < iter; i++) 
{
if (i < iter-1)
text += convStr(descToolTip.substring( i*maxCharsLine, (i+1) * maxCharsLine)) + '<br>'
else 
{
var subText = descToolTip.substring( i*maxCharsLine, maxTextLen)
var subTextLen = subText.length
var moreTextLen = moreStr.length + 3 
text += (subTextLen < maxCharsLine) ? convStr(subText) : convStr(subText.substring(0, maxCharsLine-moreTextLen)) + 
' (<span onclick=\''+convStr(_codeWinName)+convStr(par)+'.clickMoreCB(' + (_longTextID++) + ',"'+convStr(frName)+'");return false\'><a class="dialogzone" href="javascript:void(0)"  >' +
convStr(moreStr) + '</a></span>)'
}
}
} 
else 
text = convStr(descToolTip)
text = '<span title="' + convStr(descToolTip, false, true) + '">' + text + '</span>'
return text
}
function clickMoreCB(txtID,frName)
{    
_curLongTextObj = _longTextAr[txtID]
wt()
var frameName=(frName)?frName:'DlgFrame'
var url=(frameName=='DlgFrame')?'':'language/'+_lang+'/html/'
url+='longTextDialog.html'
frameNav(frameName,url)
}
function getForceViewType()
{
var forceViewType=""
if(isPDF) 
forceViewType="P"
else if(isInteractive)
forceViewType="I"
return forceViewType;
}
function wr(s)
{
document.write(s)
}
_dontCloseDoc = false
function beforeUnloadCB(e)
{
if (_ie)
e=event
if (!_dontCloseDoc && (isEnableUserRight(_usrSaveAsDoc) || isEnableUserRight(_usrSaveDoc)) && isDocModified()) {
e.returnValue = "You will lose any unsaved modifications to this document.";
}
}
function isDocModified() 
{
    var ret = ((arrState && (arrState.length > 1)) || (isInteractive && _isQPModified));      
return ret;
}
function openDocumentCB(targetURL)
{
frameNav("DlgFrame", getBasePath()+"lib/empty.html?sEntry="+strEntry, true);
Report.history.back();
setTimeout("delayedOpenDocument('"+targetURL.replace(/\'/g,"%27")+"')", 10);
}
function delayedOpenDocument(url)
{
frameNav("DlgFrame",url,true);
}
function writeBody(reportSrc)
{
mainPal.begin()
var show=bLaunchQP?false:true
wr('<div id="mainContainer" style="display:'+(show?'block':'none')+'">')
pal1.write()
wr('</div>')
if (isInteractive)
writeEditToolbars()
show=bLaunchQP?false:_showQueryPanel
if (bFullScreen)
show=false
pdfContainer.setDisplay(show)
pdfContainer.write()
show=true
if (!isEnableUserRight(_usrEnableTrackDataChanges) || _showQueryPanel || (strDocType=="rep")) {show=false
}
tdcActivateIcon.setDisplay(show)
mainPal.end()
wr('<div id="statusBar" class="palette" style="overflow:hidden;height:'+(_dtd4?20:22)+'px;display:block;position:absolute;left:0px;top:-100px;">');
wr('<table width="100%" class="dialogzone" cellpadding="0" cellspacing="0" border="0" id="statusBarContent" style="height:19px"><tr valign="middle"><td width="100%"></td>');
wr('<td class="dialogzone" id="statusPartial"><table class="dialogzone" cellpadding="0" cellspacing="0" border="0"><tr valign="middle">');
wr('<td style="padding-left:4px">'+simpleImgOffset(_img+"framework.gif",16,16,0,95)+'</td><td class="panelzone" style="padding-left:4px" id="statusPartialText"><nobr>Partial Results</nobr></td><td style="padding-left:4px">'+simpleImgOffset(_skin+"iconsep.gif",6,20,0,1)+'</td></tr></table></td>');
wr('<td class="dialogzone" id="statusBarTDC"><table class="dialogzone" cellpadding="0" cellspacing="0" border="0" ><tr valign="middle" class="panelzone">');
wr('<td style="padding-left:5px"><nobr>Data Tracking Reference:</nobr></td>')
wr('<td style="padding-left:10px" id="statusBarTDCRef"></td>')
wr('<td style="padding-left:10px;padding-right:10px" ><nobr id="statusBarTDCRefDate"></nobr></td>')
wr('<td style="padding-left:4px">'+simpleImgOffset(_skin+"iconsep.gif",6,20,0,1)+'</td>')
wr('</tr></table></td>');
wr('<td class="panelzone" style="padding-left:10px;"><nobr>Refresh Date:</nobr></td><td class="panelzone" style="padding-left:4px;padding-right:4px"><nobr id="statusRefDate"></nobr></td>');
wr('</tr></table>');
wr('</div>');
wr('<div class="treeZone" id="reportBorder" style="display:block;position:absolute;left:-1000px;top:100px;">')
wr('<iframe id="Report" name="Report" frameborder="0" src="' + reportSrc + '"></iframe>')
wr('</div>')
initTabsZone(iReportID)
tabsZone.write()
wr('<iframe style="position:absolute;left:-100px;top:-100px;width:10px;height:10px;" id="dlg" name="DlgFrame" frameborder="0" src="lib/empty.html"></iframe>')
wr('<iframe style="position:absolute;left:-100px;top:-100px;width:10px;height:10px;" id="dlg2" name="SecondDlgFrame" frameborder="0" src="lib/empty.html"></iframe>')
wr('<iframe style="position:absolute;left:-100px;top:-100px;width:10px;height:10px;" id="dlg3" name="ThirdDlgFrame" frameborder="0" src="lib/empty.html"></iframe>')
wr('<form target="Report" style="display:none" name="viewerForm" method="post" action="">')
wr('<input type="hidden" name="sParam1" id="sParam1">')
wr('<input type="hidden" name="sParam2" id="sParam2">')
wr('<input type="hidden" name="sParam3" id="sParam3">')
wr('</form>')
docMenu.write()
viewMenu.write()
toolbarsMenu.write()
if (isInteractive)
writeEditHTML();
leftZoneGrab.write() // Done For Reporting
leftPane.write() // Done For Reporting
eventManager.notify(_EVT_HTML_WRITTEN)
}
function refreshDocument(iDataProvider,bCheckPDP)
{
if(bCheckPDP && iDataProvider==null )
checkContextPDP("refresh");
else
{
strDlgTitle="Refreshing Data";
iDataProvider=iDataProvider?iDataProvider:null;
_runQueryIdx = iDataProvider; 
_curIdRef=null
var strLocation = "refreshDocument"+_appExt+urlParamsNoBID(null,null,null,null,null,1)+"&nbPage=NaN";
if(_runQueryIdx!=null)
    strLocation+="&iDPIndex="+_runQueryIdx;
if (bLaunchQP==true)
{
strLocation+="&sNewDoc=true";
self.DlgFrame.location=strLocation;
}
else
{
if (blnNeedAnswerCP)
{
if (isPDF)
{
var cancelKey="CP"+allUseDictionary.size();
var cancelValue=getFrame("Report").location.href;
allUseDictionary.put(cancelKey,cancelValue);
strLocation+="&sCancel="+cancelKey;
}
launchRefreshWaitDlg("Refreshing Data",false,false,false);
}
else
{
eventManager.notify(_EVT_REFRESH_DATA);
launchRefreshWaitDlg("Refreshing Data",true,true,true);
}
if (isPDF)
strLocation+="&viewType=P";
self.DlgFrame.location.replace(strLocation);
}
}
}
function launchRefreshWaitDlg(strDlgTitle,blnCanelQuery,blnShowProgress,blnShowLabel)
{
var strLabel="Last refresh time: %h%h %m%m %s%s";
var ret=formatDuration(lastRefreshDuration.toString())
strLabel=strLabel.replace(/%h%/g,ret.hh);
strLabel=strLabel.replace(/%m%/g,ret.mm);
strLabel=strLabel.replace(/%s%/g,ret.ss);
strLabel=strLabel.replace(/"/g,"\\"+"\"");
strDlgTitle=strDlgTitle.replace(/"/g,"\\"+"\"");
var lastRDms = (lastRefreshDuration * 1000).toString();
var strFuncPrototype="showWaitDlg(\""+strDlgTitle+"\","+((blnCanelQuery)?"true":"false")+",cancelQueryCB,"+((blnShowProgress)?"true":"false")+","+lastRDms+','+((blnShowProgress)?"true":"false")+",\""+strLabel+"\")";
eval(strFuncPrototype);
}
function cancelQueryCB()
{
if (typeof(cancelQueryInDotNet)!="undefined")
cancelQueryInDotNet();
else
self.DlgFrame.location="cancelQuery"+_appExt+"?iViewerID="+iViewerID;
}
function formatDuration(dur)
{
var ret=new Object
ret.hh=0
ret.mm=0
ret.ss=0
if (dur==null)
return ret
var nbsec=parseInt(dur)
var h,m,s
h=Math.floor(nbsec/3600)
m=Math.floor((nbsec-h*3600)/60)
s=nbsec-h*3600-m*60
ret.hh=h
ret.mm=m
ret.ss=s
return ret
}
function initRefreshAllMenu()
{
var m = refreshAllMenu;
var mlen=m.items.length;
var dplen =arrDPs.length;
var dp,isPDP;
if(mlen < dplen+1)
{
for(var i=mlen;i<dplen+1;i++)
m.add('refresh'+i,'',refreshQueriesCB,_img+"standard.gif",8*16,0,false,8*16,1*16)
}
for(var i=0;i<dplen;i++)
{
dp=arrDPs[i];
m.getItem(i).setText(dp.name);
isPDP= (dp.dpType == 1);
m.getItem(i).setIcon((isPDP?21:8)*16,0,(isPDP?21:8)*16,1*16,_img+"standard.gif");
m.getItem(i).show(true);
m.getItem(i).setDisabled(!dp.refreshable);
}
m.getItem(dplen).setText("Refresh all queries");
m.getItem(dplen).setIcon(7*16,0,7*16,1*16,_img+"standard.gif");
m.getItem(dplen).show(true);
if(dplen+1<mlen) 
{
for(var i=dplen+1;i<mlen;i++)
m.getItem(i).show(false);
}
}
newObserverOneEvent(_EVT_DOC_UPDATE,updateRefreshIcon);
function updateRefreshIcon()
{
if (strDocType=="rep")
{
refreshDocIcon.setDisplay(false); //Done  false, For Reporting
refreshAllIcon.setDisplay(false);
}
else
{
var len =arrDPs.length;
refreshDocIcon.setDisplay(false); // from len  == 1 to false.Done For Reporting
refreshAllIcon.setDisplay(false); // from len > 1 to false. Done For Reporting
}
}
function refreshQueriesCB()
{
var index = this.id.slice(7); 
var dplen =arrDPs.length; 
if(index == dplen)
 index = null; 
refreshDocument(index,true);
}
newObserverOneEvent(_EVT_FETCH_DATA,updatePurgeIcn)
newObserverTwoEvents(_EVT_PURGE_DATA,_EVT_PAGE_LOADED,updateDPsRowsCB)
function initPurgeAllMenu()
{
var m = this;
var mlen=m.items.length;
var dplen =arrDPs.length;
var disableAll=true, isPDP;
if(mlen < dplen+1)
{
for(var i=mlen;i<dplen+1;i++)
m.add('purge'+i,'',purgeCB,_img+"standard.gif",8*16,0,false,8*16,1*16)
}
for(var i=0;i<dplen;i++)
{
var dp=arrDPs[i];
isPDP= (dp.dpType == 1);
m.getItem(i).setText(dp.name);
m.getItem(i).setIcon((isPDP?21:8)*16,0,(isPDP?21:8)*16,1*16,_img+"standard.gif");
m.getItem(i).show(true);
if((dp.rows!=null) && (dp.rows<1))
m.getItem(i).setDisabled(true);
else
{
if(!dp.refreshable) 
m.getItem(i).setDisabled(true);
else
{
m.getItem(i).setDisabled(false);
}
disableAll=false;
}
}
m.getItem(dplen).setText("Purge all queries");
m.getItem(dplen).setIcon(18*16,0,18*16,1*16,_img+"standard.gif");
m.getItem(dplen).show(true);
m.setDisabled(disableAll);
if(dplen+1<mlen) 
{
for(var i=dplen+1;i<mlen;i++)
m.getItem(i).show(false);
}
}
function updatePurgeIcn()
{
if(isInteractive)
{
var len =arrDPs.length;
purgeIcn.setDisplay(len  == 1);
purgeAll.setDisplay(len > 1);
purgeQIcn.setDisplay(len  == 1);
purgeQAll.setDisplay(len > 1);
if(len == 1)
{
var disable = true;
if ((arrDPs[0].rows != null) && (arrDPs[0].rows>0))
disable = false;
purgeIcn.setDisabled(disable);
purgeQIcn.setDisabled(disable);
}
else if(len > 1)
{
var disableAll = true;
for(var i=0;i<len;i++)
{
var dp=arrDPs[i];
if((dp.rows!=null) && (dp.rows>0))
disableAll=false;
}
purgeAll.setDisabled(disableAll);
purgeQAll.setDisabled(disableAll);
}
}
}
function purgeCB()
{
var id=this.id;
var index = null;
if(id == "tabsDP_purge")
{
index = getSelectedDPIndex(); 
}
else
{
index =id.slice(5); 
}
var dplen =arrDPs.length; 
if(index == dplen) index = null; 
purgeDocument(index,true,true);
}
function purgeDocument(iDataProvider,bAskConfirmation, bCheckPDP)
{
if(bCheckPDP && iDataProvider == null)
checkContextPDP("purge");
else
{
if(bAskConfirmation) 
showPromptDialog("Are you sure you want to purge the data from the document ?","Purge data",1,'purgeDocument('+iDataProvider+')',null);
else
{
_curIdref=null
var p=urlParams(false,true)
if (p!="")
{
if(iDataProvider != null)
p+="&iDPIndex="+iDataProvider;
eventManager.notify(_EVT_PURGE_DATA);
wt()
frameNav("Report","processPurge"+_appExt+p);
}
}
}
}
function checkContextPDP(action)
{
var len = arrDPs.length;
var showAlert = true;
for(var i=0;i<len;i++)
{
var dp=arrDPs[i];
if(dp.refreshable)
{
showAlert=false;
break;
}
}
if(showAlert) 
{
if (action == "runquery")
showAlertDialog("This document contains one or more local data sources that cannot be refreshed",null,0,'getQueryFrame().submitQueries("runquery")');
else if (action == "purge")
showAlertDialog("This document contains one or more local data sources that cannot be purged",null,0,null);
else
showAlertDialog("This document contains one or more local data sources that cannot be refreshed",null,0,null);
}
else
{
if(action == "refresh")
refreshDocument();
else if (action == "purge")
showPromptDialog("Are you sure you want to purge the data from the document ?","Purge data",1,purgeDocument,null);
else if (action == "runquery")
{
var f = getQueryFrame();
if(f != null) f.submitQueries("runquery");
}
}
}
function deactivateTDC()
{
eventManager.notify(_EVT_TDC_CHANGED);
setTDC(false);
}
function cancelDeactivateTDC()
{
tdcActivateIcon.check(true);
}
function setTDC(bTDCRefreshNow) {
_bTdcActivate=tdcActivateIcon.isChecked()
if (_bTdcActivate) {
_tdcAutoMode = _bTDCAutoUpdateDataOnRefresh;
}
tdcShowChangesIcon.setDisabled(!_bTdcActivate);
tdcOptionsIcon.setDisabled(!_bTdcActivate);
tdcShowChangesIcon.check(_bTdcActivate);
var showAllChanges="&sShowChanges=";
showAllChanges+=(_bTdcActivate)?"true":"false";
wt()
var p=urlParamsNoBID()
if (p!="")
frameNav("Report",_root+"processTrackDataChanges"+_appExt+p+"&sEnableTdc="+_bTdcActivate+showAllChanges+"&bAutoUpdateChanges="+_bTDCAutoUpdateDataOnRefresh+"&bRefreshNow="+bTDCRefreshNow,true)
}
function escapeFormatPainter()
{
if (window.formatPainter) {
formatPainter.check(false)
if (_formatPainterBid != null)
newTooltipWidget().show(false)
_formatPainterBid = null
}
}
obsLP = new Array;
obsLP[obsLP.length] = newObserverTwoEvents(_EVT_DOC_UPDATE,_EVT_WOM_LOADED, refreshLeftPanelCB)
obsLP[obsLP.length] = newObserverTwoEvents(_EVT_REFRESH_DATA,_EVT_WOM_LOADED, refreshLeftPanelCB)
obsLP[obsLP.length] = newObserverTwoEvents(_EVT_PROMPTS_VALUES_CHANGED,_EVT_WOM_LOADED, refreshLeftPanelCB)
obsLP[obsLP.length] = newObserverTwoEvents(_EVT_UNDO_REDO,_EVT_WOM_LOADED, refreshLeftPanelCB)
obsLP[obsLP.length] = newObserverOneEvent(_EVT_PROCESS_SAVE, refreshLeftPanelCB)
obsLP[obsLP.length] = newObserverTwoEvents(_EVT_TDC_CHANGED,_EVT_WOM_LOADED, refreshLeftPanelCB)
obsLP[obsLP.length] = newObserverTwoEvents(_EVT_DOC_PROPERTIES,_EVT_WOM_LOADED, refreshLeftPanelCB)
obsLP[obsLP.length] = newObserverTwoEvents(_EVT_VARS_UPDATE,_EVT_WOM_LOADED, refreshLeftPanelCB)
obsLP[obsLP.length] = newObserverTwoEvents(_EVT_DRILL_ACTION,_EVT_WOM_LOADED, refreshLeftPanelCB)
paneObs=newObserverOneEvent(_EVT_WOM_LOADED,refreshLeftPanelCB)
function refreshLeftPanelCB(evt1,data) 
{
if (! _showLeftPane) return;
var lf=leftPane.getFrame();
if(lf == null ) return;
if (!_panesInitalized) 
{
_panesInitalized=true
setPanes()
return;
}
var pane = leftPane.selection>=0?leftPane.panes[leftPane.selection]:null;
if(pane == null )
return;
if (_leftPaneSel=="filtmap")
{
if ((typeof(fromTreeReportChange)!="undefined") && fromTreeReportChange)
{
fromTreeReportChange = false
return;
}
switch(evt1)
{
case _EVT_WOM_LOADED:
wt(true);
lf.window.location.replace(pane.url)
break;
}
}
else if (_leftPaneSel=="docinfo")
{
switch(evt1)
{
case _EVT_DOC_UPDATE:
case _EVT_REFRESH_DATA:
case _EVT_PROMPTS_VALUES_CHANGED:
case _EVT_UNDO_REDO:
case _EVT_PROCESS_SAVE:
case _EVT_TDC_CHANGED:
case _EVT_DOC_PROPERTIES:
wt(true);
lf.window.location.replace(pane.url)
break;
}
}
else if (_leftPaneSel=="dataSum")
{
switch(evt1)
{
case _EVT_DOC_UPDATE:
case _EVT_REFRESH_DATA:
case _EVT_UNDO_REDO:
case _EVT_VARS_UPDATE:
wt(true);
lf.window.location.replace(pane.url)
break;
}
}
else if (_leftPaneSel=="resultObj")
{
switch(evt1)
{
case _EVT_DOC_UPDATE:
case _EVT_REFRESH_DATA:
case _EVT_UNDO_REDO:
case _EVT_VARS_UPDATE:
case _EVT_DRILL_ACTION:
wt(true);
lf.window.location.replace(pane.url)
break;
}
}
}

_trIndent=18
function newIconListWidget(id,w,h,icns,clickCB,doubleClickCB,bgClass)
{
var o=newTreeWidget(id,w,h,icns,clickCB,doubleClickCB,bgClass)
o.select=IconListWidget_select
o.getSelection=IconListWidget_getSelection
o.initialIndent=-_trIndent+3
return o
}
function IconListWidget_select(pos,setFocus,ev,noSendClickCB,isFromKeyArrow)
{
if ((pos>=0) && (pos<this.sub.length)) {
this.sub[pos].select(setFocus,ev,noSendClickCB,isFromKeyArrow)
}
}
function IconListWidget_getSelection()
{
var sel=this.getSelectedItem()
if (sel)
{
if (sel.elemPos==-1) this.buildElems()
var selection=new Object
selection.index=sel.elemPos
selection.value=sel.userData
return selection
}
return null
}
function newIconListPopupWidget(id,w,h,icns,clickCB,doubleClickCB)
{
var o=newWidget(id)
o.iconList=newIconListWidget("list_"+id,w,h,icns,clickCB,doubleClickCB,"treeNoBorder")
o.getList=IconListPopupWidget_getList
o.getHTML=IconListPopupWidget_getHTML
o.justInTimeInit=IconListPopupWidget_justInTimeInit
o.getShadowHTML=IconListPopupWidget_getShadowHTML
o.show=IconListPopupWidget_show
o.captureClicks=IconListPopupWidget_captureClicks
o.releaseClicks=IconListPopupWidget_releaseClicks
o.getSelection=IconListPopupWidget_getSelection
o.clickCB=new Array
o.clickCBDocs=new Array
o.cancelCB=null;
return o
}
function IconListPopupWidget_init()
{
var o=this
o.iconList.init();
o.init();
}
function IconListPopupWidget_getList()
{
return this.iconList;
}
function IconListPopupWidget_getSelection()
{
}
function IconListPopupWidget_getShadowHTML()
{
return getBGIframe('menuIframe_'+this.id)
}
function IconListPopupWidget_getHTML()
{
var o=this
var keyCB=' onkeydown="'+_codeWinName+'.IconListPopupWidget_keyDown(\''+o.id+'\',event)" onkeypress=" return '+_codeWinName+'.IconListPopupWidget_keyPress(\''+o.id+'\',event)" onkeyup="'+_codeWinName+'.IconListPopupWidget_keyUp(\''+o.id+'\',event)"';
var s=''
s+=o.getShadowHTML()
s+='<table id="'+o.id+'" style="display:none;" class="menuFrame" cellspacing="0" cellpadding="0" border="0" '+keyCB+'><tbody>'
s+='<tr><td align="center">'+o.iconList.getHTML()+'</td></tr>'
s+='</tbody></table>'
return s
}
function IconListPopupWidget_justInTimeInit()
{
var o=this
o.layer=getLayer(o.id)
if (o.layer==null)
{
targetApp(o.getHTML())
o.layer=getLayer(o.id)
}
o.layer._widget=o.widx
o.css=o.layer.style
o.css.visibility="hidden"
o.iframeLyr=getLayer("menuIframe_"+o.id)
o.iframeCss=o.iframeLyr.style
o.iconList.init()
o.iconList.layer.onmousedown=IconListPopupWidget_clickNoBubble
}
function IconListPopupWidget_show(show,x,y)
{
var o=this
if (o.layer==null)
o.justInTimeInit()
var css=o.css,iCss=o.iframeCss
if (show)
{
o.captureClicks()
css.display='block'
css.zIndex=4000
css.visibility="hidden"
css.left="-1000px"
css.top="-1000px"
var w=o.getWidth()
var h=o.getHeight()
if (o.alignLeft)
x-=w
var x2=x+w+4,y2=y+h+4
if (x2>winWidth())
x=Math.max(0,x-4-w)
if (y2>winHeight())
y=Math.max(0,y-4-h)
css.left=""+x+"px"
css.top=""+y+"px"
css.visibility="visible"
iCss.left=""+x+"px"
iCss.top=""+y+"px"
iCss.width=""+w+"px"
iCss.height=""+h+"px"
iCss.zIndex=3998
iCss.display='block'
if (!_dtd4)
{
y-=2
x-=2
}
}
else
{
css.display='none'
iCss.display='none'
o.releaseClicks()
}
}
function IconListPopupWidget_captureClicks(w)
{
var o=this
if (o.par==null)
{
if (w==null)
{
_globMenuCaptured=o
o.clickCB.length=0
o.clickCBDocs.length=0
w=_curWin
}
if (canScanFrames(w))
{
if (_moz)
{
_oldErrHandler=window.onerror
window.onerror=localErrHandler
}
try
{
d=w.document
o.clickCB[o.clickCB.length]=d.onmousedown
o.clickCBDocs[o.clickCBDocs.length]=d
d.onmousedown=IconListPopupWidget_globalClick
var fr=w.frames,len=fr.length
for (var i=0;i<len;i++)
o.captureClicks(fr[i])
}
catch(expt)
{
}
if (_moz)
window.onerror=_oldErrHandler
}
}
}
function IconListPopupWidget_globalClick()
{
var o=_globMenuCaptured
if (o!=null)
{
_globMenuCaptured=null
o.releaseClicks()
o.show(false)
}
}
function IconListPopupWidget_releaseClicks()
{
var o=this
if (o.par==null)
{
var len=o.clickCB.length
for (var i=0;i<len;i++)
{
o.clickCBDocs[i].onmousedown=o.clickCB[i]
o.clickCB[i]=null
o.clickCBDocs[i]=null
}
o.clickCB.length=0
o.clickCBDocs.length=0
}
}
function IconListPopupWidget_clickNoBubble(e)
{
eventCancelBubble(e)
}
function IconListPopupWidget_keyDown(id,e)
{
var o=getWidget(getLayer(id))
var key=eventGetKey(e)
if(key == 27)
{
if(o && o.cancelCB)
o.cancelCB(e)
}
if(key == 13)
{
eventCancelBubble(e)
return false
}
}
function IconListPopupWidget_keyPress(id,e)
{
var o=getWidget(getLayer(id))
var key=eventGetKey(e)
if(key == 13 || key == 27)
{
eventCancelBubble(e)
return false
}
}
function IconListPopupWidget_keyUp(id,e)
{
var key=eventGetKey(e)
if(key == 13 || key == 27)
{
eventCancelBubble(e)
return false
}
}
function newTreeWidget(id,w,h,icns,clickCB,doubleClickCB,bgClass,expandCB,collapseCB,deleteCB)
{
var o=newScrolledZoneWidget(id,2,4,w,h,bgClass)
o.icns=icns
o.sub = new Array
o.clickCB=clickCB
o.doubleClickCB=doubleClickCB
o.expandCB=expandCB
o.collapseCB=collapseCB
o.deleteCB=deleteCB
o.mouseOverCB=null
o.customTooltip=null
o.rightClickMenuCB=null 
o.mouseOverTooltip=false
o.dragDrop=null
o.oldInit=o.init
o.init=TreeWidget_init
o.getHTML=TreeWidget_getHTML
o.getSelections=TreeWidget_getSelections
o.getSelectedItem=TreeWidget_getSelectedItem
o.getSelectedItems=TreeWidget_getSelectedItems
o.getCheckedItems=TreeWidget_getCheckedItems
o.setDragDrop=TreeWidget_setDragDrop
o.setDDTooltip=TreeWidget_setDDTooltip
o.setFocus=TreeWidget_setFocus
o.add=TreeWidget_add
o.setRightClickMenuCB=TreeWidget_setRightClickMenuCB
o.findByData=TreeWidget_findByData
o.findById=TreeWidget_findById
o.findInName=TreeWidget_findInName
o.selectByData=TreeWidget_selectByData
o.selectById=TreeWidget_selectById
o.unselect=TreeWidget_unselect
o.search=TreeWidget_search
o.treeLyr=null
o.elems=new Array
o.elemCount=0
o.selId=-1;
o.selIds=new Array; 
o.multiSelection=false;
o.hlPath=false; 
o.hlElems=new Array; 
o.iconOrientVertical=true
o.deleteAll=TreeWidget_deleteAll
o.rebuildHTML=TreeWidget_rebuildHTML
    o.clearLayers=TreeWidget_clearLayers
o.iconW=16
o.iconH=16
o.initialIndent=0
o.buildElems=TreeWidget_buildElems
o.getCount=TreeWidget_getCount
if (window._TreeWidgetElemInstances==null)
window._TreeWidgetElemInstances=new Array
o.dispIcnFuncName="dispIcn"
o.setTooltipOnMouseOver=TreeWidget_setTooltipOnMouseOver
o.setMouseOverCB=TreeWidget_setMouseOverCB
o.setMultiSelection=TreeWidget_setMultiSelection
o.setHighlightPath=TreeWidget_setHighlightPath
o.highlightPath=TreeWidget_highlightPath
o.unhlPath=TreeWidget_unhlPath
return o
}
function TreeWidget_unselect()
{
var o=this
if (o.selId>=0)
{
var prev=_TreeWidgetElemInstances[o.selId]
prev.unselect()
o.selId=-1
}
if(o.multiSelection)
{
var len=o.selIds.length, id;
for(var i=len-1;i>=0;i--)
{
var prev=_TreeWidgetElemInstances[o.selIds[i]]
if(prev) prev.unselect()
}
o.selIds.length=0;
o.layer._BOselIds="";
}
o.unhlPath()
}
function TreeWidget_selectByData(data,setFocus)
{
var o=this,item=o.findByData(data)
if (item)
{
item.select(setFocus)
}
}
function TreeWidget_selectById(id,setFocus)
{
var o=this,item=o.findById(id)
if (item)
{
item.select(setFocus)
}
}
function TreeWidget_findByData(data)
{
var o=this,sub=o.sub,item=null
for (var i in sub)
{
item=sub[i].findByData(data)
if (item)
    return item
}
return null
}
function TreeWidget_findById(id)
{
var o=this,sub=o.sub,item=null
for (var i in sub)
{
item=sub[i].findById(id)
if (item)
    return item
}
return null
}
function TreeWidget_findInName(text,matchCase,matchWholeW,startFrom,next,starWith,visible)
{
if(text=="" || text==null) return null; 
var o=this,item=null,elem=null,hidden=false;
var startPos=0,newPos=0;
var bMatchCase=matchCase?matchCase:false;
var bMatchWW=matchWholeW?matchWholeW:false;
var bNext=(!next)?next:true;
var bVisible=visible?visible:false;
var len=o.elems.length;
if(len == 0)
{ 
o.buildElems();
len=o.elems.length;
if( len == 0) return;
}
var arr = o.getSelections();
if(arr.length>0) 
{
startPos=arr[0].elemPos+(bNext?1:-1);
if((startPos<0) &&! bNext )
startPos=len-1;
if((startPos==len) && bNext )
startPos=0;
}
else if(startFrom=="begin")
{
startPos=0;
}
else if(startFrom=="end")
{
startPos=len-1;
}
newPos=startPos;
while ((newPos>=0)&&(newPos<len))
{
elem=o.elems[newPos];
hidden=elem.getHiddenParent();
if((bVisible && !hidden) || (!bVisible))
item=elem.findInName(text,bMatchCase,bMatchWW,bNext,starWith);
if(item!=null) break;
newPos=newPos+(bNext?1:-1);
if((newPos<0) && !bNext )
newPos=len-1;
if((newPos==len) && bNext )
newPos=0;
if(newPos==startPos) break;
}
return item;
}
function TreeWidget_search(text,matchCase,matchWholeW,startFrom,next,notFoundCB,starWith,visible,setFocus)
{
var o=this,item=null;
if(text=="" || text==null) return ; 
item = o.findInName(text,matchCase,matchWholeW,startFrom,next,starWith,visible);
if(item)
{
o.unselect();
item.select(setFocus);
}
else if(notFoundCB)
{
notFoundCB();
}
}
function TreeWidget_add(elem,extraIndent)
{
var o=this,sub=o.sub,len=sub.length
elem.treeView=o
sub[len]=elem
elem.expanded=(len==0)
if (extraIndent)
elem.extraIndent=extraIndent
return elem
}
function TreeWidget_getHTML()
{
var o=this,sub=o.sub,len=sub.length,a=new Array(len+3),j=0
a[j++]= o.beginHTML()+'<span id="treeCont_'+o.id+'" onkeydown="return '+_codeWinName+'.TreeWidget_keyDownCB(this,event)" onkeypress="return '+_codeWinName+'.TreeWidget_keyPressCB(this,event)">'
for (var i in sub)
a[j++]=sub[i].getHTML(o.initialIndent,i==0)
a[j++]='</span>'+o.endHTML()
return a.join("")
}
function TreeWidget_deleteAll()
{
var sub=this.sub
for (var i in sub)
{
sub[i].deleteAll()
sub[i]=null
}
sub.length=0
if (this.elems)
this.elems.length=0
}
function TreeWidget_rebuildHTML()
{
var o=this,sub=o.sub,len=sub.length,a=new Array(len),j=0,idt=o.initialIndent
for (var i in sub)
a[j++]=sub[i].getHTML(idt,i==0)
o.treeLyr.innerHTML=a.join("")
o.selId=-1
o.layer._BOselId=-1
o.selIds.length=0
o.layer._BOselIds=""
this.buildElems()
this.clearLayers();
this.init();
}
function TreeWidget_init()
{
this.oldInit();
var l=this.treeLyr=getLayer('treeCont_'+this.id);
if (this.dragDrop)
this.dragDrop.attachCallbacks(this.layer)
var oldSel = this.layer._BOselId
if (oldSel!=null)
this.selId=oldSel
var oldArraySel = this.layer._BOselIds; 
if (oldArraySel!=null && oldArraySel!="")
{
this.selIds.length=0;
this.selIds=oldArraySel.split(";");
}
var sub=this.sub
}
function TreeWidget_buildElems(elem)
{
with (this)
{
if (elem==null)
elem=this;
else
{
var pos=elems.length;
elems[pos]=elem;
elem.elemPos=pos;
}
var subArr=elem.sub,len=subArr.length;
for (var i=0;i<len;i++)
buildElems(subArr[i]);
}
}
function TreeWidget_clearLayers(elem)
{
with (this)
{
if (elem==null)
elem=this;
        elem.layer=null
var subArr=elem.sub,len=subArr.length;
for (var i=0;i<len;i++)
clearLayers(subArr[i]);
}
}
function TreeWidget_getSelectedItem()
{
var id=this.selId
return (id>=0)?_TreeWidgetElemInstances[id]:null
}
function TreeWidget_getSelections()
{
var o=this;
if(o.multiSelection)
{
return o.getSelectedItems();
}
else
{
var sel=o.getSelectedItem(),arrSel=new Array;
if(sel!=null) arrSel[0]=sel;
return arrSel;
}
}
function TreeWidget_setFocus(index)
{
var elem=_TreeWidgetElemInstances[index]
if(elem!=null)
{
elem.init()
safeSetFocus(elem.domElem)
}
}
function TreeWidget_keyPressCB(lay,e)
{
if(getWidget(lay).multiSelection)
{ 
return TreeWidget_multiSelKeyPress(lay,e);
}
var id=getWidget(lay).selId;
if (id>=0)
{
var elem=_TreeWidgetElemInstances[id]
var treeView=elem.treeView
var source =TreeIdToIdx(_ie?_curWin.event.srcElement:e.target)
var k=eventGetKey(e) , ctrl=eventIsCtrl(e)
if( k==13 )
{
if(source!=id)
{
TreeWidget_clickCB(source,false,null);
TreeWidgetElem_UpdateTooltip(source,true);
}
else if ((source==id)&&(treeView.doubleClickCB))
treeView.doubleClickCB(elem.userData);
}
if((k==10) && ctrl &&(source==_codeWinName+"trLstElt"+id))
{
if(elem.sub.length>0)
{
TreeWidget_toggleCB(id);
TreeWidgetElem_UpdateTooltip(source);
}
if (elem.isIncomplete&&elem.querycompleteCB)
{
elem.querycompleteCB()
TreeWidgetElem_UpdateTooltip(source);
}
return false
}
}
else 
{
var source =TreeIdToIdx(_ie?_curWin.event.srcElement:e.target)
var k=eventGetKey(e);
if( k==13 )
{
TreeWidget_clickCB(source,false,null);
TreeWidgetElem_UpdateTooltip(source,true);
}
}
}
function TreeWidget_multiSelKeyPress(o,e)
{
var treeView = getWidget(o);
var len = treeView.selIds.length;
if (len>0) 
{
var source =TreeIdToIdx(_ie?_curWin.event.srcElement:e.target)
var k=eventGetKey(e) , ctrl=eventIsCtrl(e)
var elem = null;
for(var i=0; i<len;i++)
{
var id = treeView.selIds[i];
if (source==id)
{
elem = _TreeWidgetElemInstances[id];
break;
}
}
if( k==13 )
{
if(elem == null)
{
TreeWidget_clickCB(source,false,_ie?_curWin.event:e);
TreeWidgetElem_UpdateTooltip(source,true);
}
else if (elem &&(treeView.doubleClickCB))
treeView.doubleClickCB(elem.userData);
}
if((k==10) && ctrl && elem) 
{
if(elem.sub.length>0)
{
TreeWidget_toggleCB(id);
TreeWidgetElem_UpdateTooltip(source);
}
if (elem.isIncomplete&&elem.querycompleteCB)
{
elem.querycompleteCB()
TreeWidgetElem_UpdateTooltip(source);
}
return false
}
}
else 
{
var source =TreeIdToIdx(_ie?_curWin.event.srcElement:e.target)
var k=eventGetKey(e);
if( k==13 )
{
TreeWidget_clickCB(source,false,null);
TreeWidgetElem_UpdateTooltip(source,true);
}
}
return true;
}
t=0
function TreeWidget_keyDownCB(lay,e)
{
if(getWidget(lay).multiSelection)
{ 
return TreeWidget_multiSelKeyDown(lay,e);
}
var id=getWidget(lay).selId;
var k=eventGetKey(e);
if (id>=0)
{
var elem=_TreeWidgetElemInstances[id]
if (elem!=null)
{
var treeView=elem.treeView
var source=TreeIdToIdx(_ie?_curWin.event.srcElement:e.target)
switch(k)
{
case 107:
case 39:
if ((elem.sub.length>0)&&(!elem.expanded))
{
TreeWidget_toggleCB(id);
TreeWidgetElem_UpdateTooltip(source);
}
if (elem.isIncomplete&&elem.querycompleteCB)
{
elem.querycompleteCB()
TreeWidgetElem_UpdateTooltip(source);
}
break;
case 109:
case 37:
if ((elem.sub.length>0)&&(elem.expanded))
{
TreeWidget_toggleCB(id);
TreeWidgetElem_UpdateTooltip(source);
}
break;
case 40:
case 38:
var nElt=elem.getNextPrev(k==40?1:-1);if (nElt!=null){nElt.select(null,null,null,true);safeSetFocus(nElt.domElem)} 
return false
break;
case 46: 
if(treeView.deleteCB)
treeView.deleteCB(elem.userData)
break;
default:
var c = String.fromCharCode(k);
if(c)
{
if ((e.altKey!=true)&&(e.ctrlKey!=true)&&(e.metaKey!=true))
treeView.search(c,false,false,null,true,null,true,true,true);
}
break
}
}
}
if(k == 13)
{
eventCancelBubble(e);
}
}
function TreeWidget_multiSelKeyDown(o,e)
{
var treeView = getWidget(o);
var len = treeView.selIds.length;
var k=eventGetKey(e);
if (len>0) 
{
var ctrl=eventIsCtrl(e)
var shift=_ie?_curWin.event.shiftKey:e.shiftKey;
var source=TreeIdToIdx(_ie?_curWin.event.srcElement:e.target)
var elem = null, id;
for(var i=0; i<len;i++)
{
id = treeView.selIds[i];
if (source==id)
{
elem = _TreeWidgetElemInstances[id];
break;
}
}
if(elem)
{
switch(k)
{
case 107: 
case 39:
if ((elem.sub.length>0)&&(!elem.expanded))
{
TreeWidget_toggleCB(id);
TreeWidgetElem_UpdateTooltip(source);
}
if (elem.isIncomplete&&elem.querycompleteCB)
{
elem.querycompleteCB()
TreeWidgetElem_UpdateTooltip(source);
}
break;
case 109: 
case 37:
if ((elem.sub.length>0)&&(elem.expanded))
{
TreeWidget_toggleCB(id);
TreeWidgetElem_UpdateTooltip(source);
}
break;
case 40: 
case 38:
var nElt=elem.getNextPrev(k==40?1:-1);
if (nElt!=null)
{
nElt.select(null,_ie?_curWin.event:e,null,true);
safeSetFocus(nElt.domElem)
} 
return false
break;
case 46: 
if(treeView.deleteCB)
treeView.deleteCB(elem.userData)
break;
default:
var c = String.fromCharCode(k);
if(c)
{
treeView.search(c,false,false,null,true,null,true,true,true);
}
break
}
}
}
if(k == 13)
{
eventCancelBubble(e);
}
}
function TreeWidget_setDragDrop(dragCB,acceptDropCB,dropCB,dragEndCB)
{
this.dragCB=dragCB
this.acceptDropCB=acceptDropCB
this.dropCB=dropCB
this.dragEndCB=dragEndCB
this.dragDrop=newDragDropData(this,TreeWidget_dragStartCB,TreeWidget_dragCB,TreeWidget_dragEndCB,TreeWidget_acceptDropCB,TreeWidget_leaveDropCB,TreeWidget_dropCB)
}
function TreeWidget_dragStartCB(src)
{
var items=src.getSelections(),vert=src.iconOrientVertical
src.dragCB(src)
    var tw= newTooltipWidget();
if (items && items.length==1 )
{
var item=items[0]; 
var idx=item.iconId
var icons=item.icns?item.icns:src.icns
tw.show(
true,
item.getDragTooltip(),
idx>=0?icons:null,
src.iconW,
src.iconH,
vert?0:src.iconW*idx,
vert?src.iconH*idx:0)
}
else
    tw.show(false);
}
function TreeWidget_setDDTooltip(src,tooltip)
{
    var items=src.getSelections(),vert=src.iconOrientVertical
    var tw= newTooltipWidget();
    if (items && items.length==1 )
{
if(tooltip)
    tw.show(true,tooltip)
    else
    {
        var item=items[0]; 
    var idx=item.iconId
    var icons=item.icns?item.icns:src.icns
    tw.show(
true,
item.getDragTooltip(),
idx>=0?icons:null,
src.iconW,
src.iconH,
vert?0:src.iconW*idx,
vert?src.iconH*idx:0)
}
}
else 
    tw.show(false);    
}
function TreeWidget_setRightClickMenuCB(rightClickMenuCB)
{
this.rightClickMenuCB=rightClickMenuCB
}
function TreeWidget_getCount()     
{
var o=this
if (o.sub != null)
return o.sub.length
else
return 0;
}
function TreeWidget_setTooltipOnMouseOver(catchMouseOver)
{
this.mouseOverTooltip=catchMouseOver
}
function TreeWidget_setMouseOverCB(mouseOverCB)
{
this.mouseOverCB=mouseOverCB
}
function TreeWidget_dragCB(src)     
{
newTooltipWidget().setPos();
}
function TreeWidget_dragEndCB(src)  {
newTooltipWidget().show(false);
if (src.dragEndCB) src.dragEndCB()
}
function TreeWidget_dragOverEnterCB(lyr,elemId)
{
var e=_TreeWidgetElemInstances[elemId]
if (lyr.ondrop==null)
{
e.treeView.dragDrop.attachCallbacks(lyr,true)
lyr.domEltID=elemId
}
var o=_ddData[lyr._dragDropData],e=_curWin.event
e.dataTransfer.dropEffect=e.ctrlKey?'copy':'move'
if (o.acceptDropCB(window._globalDDD,o.widget,e.ctrlKey,e.ctrlKey?false:e.shiftKey,lyr,false))
e.returnValue=false
e.cancelBubble=true
}
function TreeWidget_acceptDropCB(src,target,ctrl,shift,layer) 
{
return target.acceptDropCB(src,target,ctrl,shift,layer)
}
function TreeWidget_leaveDropCB(src,target,ctrl,shift) 
{
if (target.dropWidget && target.dropWidget.layer) {
if (target.dropWidget.layer.className != target.dropWidget.nonselectedClass) {
target.dropWidget.layer.className = target.dropWidget.nonselectedClass
}
}
}
function TreeWidget_dropCB(src,target,ctrl,shift,layer,enter)       
{
newTooltipWidget().show(false);
target.dropCB(src,target,ctrl,shift);
}
function TreeWidget_setMultiSelection(multi)
{
if((!this.multiSelection && multi)||(this.multiSelection && !multi))
this.unselect();
this.multiSelection = multi;
}
function TreeWidget_getSelectedItems()
{
var arrSel=new Array;
var len = this.selIds.length, id, cpt=0;
for(var i=0; i< len; i++)
{
id = this.selIds[i];
if(id>=0)
{
arrSel[cpt]=_TreeWidgetElemInstances[id];
cpt++;
}
}
return arrSel;
}
function TreeWidget_getCheckedItems()
{
var arrChecked=new Array;
var len = _TreeWidgetElemInstances.length, cpt=0;
for (var i=0; i < len; i++)
{
elem = _TreeWidgetElemInstances[i]
if (elem.isChecked())
{
arrChecked[cpt]=elem;
cpt++;
}
}
return arrChecked;
}
function TreeWidget_setHighlightPath(hl)
{
this.hlPath=hl;
if(!hl)
this.unhlPath();
}
function TreeWidget_unhlPath()
{
var o=this, len = o.hlElems.length;
var elem, de;
if(len>0)
{
for(var i=0;i<len;i++)
{
elem = o.hlElems[i];
elem.init();
de =elem.domElem;
if( de == null) return;
if(elem.isSelected())
de.className=elem.selectedClass;
else
de.className=elem.nonselectedClass;
}
o.hlElems.length=0;
}
}
function TreeWidget_highlightPath(elemId)
{
var o=this;
if(!o.hlPath) return ; 
o.unhlPath();
var elem = _TreeWidgetElemInstances[elemId];
o.hlElems[o.hlElems.length]=elem;
elem.domElem.className=elem.selectedClass;
if(elem.elemPos == -1) o.buildElems();
var papa = elem.par;
while(papa)
{
papa.init();
papa.domElem.className=papa.hlClass;
o.hlElems[o.hlElems.length]=papa;
papa = papa.par;
}
if(elem.isNode())
hlVisibleChildren(elem,o.hlElems);
}
function hlVisibleChildren(node,arr)
{
if(node.expanded && !node.isIncomplete)
{
var len = node.sub.length;
for(var i=0;i<len;i++)
{
var sub = node.sub[i];
arr[arr.length]=sub;
sub.init();
sub.domElem.className=sub.hlClass;
if(sub.isNode()) 
hlVisibleChildren(sub,arr);
}
}
}
function newTreeWidgetElem(iconId,name,userData,help,iconSelId,tooltip,iconAlt,textClass,textSelectedClass,icns)
{
var o=new Object
o.elemPos=-1
if (window._TreeWidgetElemInstances==null)
window._TreeWidgetElemInstances=new Array
o.expanded=false
o.generated=false
o.iconId=iconId
o.iconSelId=iconSelId?iconSelId:iconId
o.tooltip=tooltip
o.iconAlt=iconAlt
o.isHTML=false
o.isCheck=false
o.checked=false
o.check=TreeWidgetElem_check
o.isChecked=TreeWidgetElem_isChecked
o.checkCB=null
o.name=name
o.par=null
o.userData=userData
o.sub=new Array
o.treeView=null
o.id=_TreeWidgetElemInstances.length
o.icns=icns
o.layer=null
o.plusLyr=null
o.icnLyr=null
o.checkElem=null
o.domElem=null
o.toggleLyr=null
o.blackTxt=(textClass)?textClass:'treeNormal'
o.grayTxt='treeGray'
o.selectedClass=(textSelectedClass)?textSelectedClass:'treeSelected'
o.nonselectedClass=o.blackTxt
o.feedbackDDClass='treeFeedbackDD'
o.hlClass='treeHL' 
o.help=help
_TreeWidgetElemInstances[o.id]=o
o.getHTML=TreeWidgetElem_getHTML
o.init=TreeWidgetElem_init
o.add=TreeWidgetElem_add;
o.select=TreeWidgetElem_select
o.unselect=TreeWidgetElem_unselect
o.getNextPrev=TreeWidgetElem_getNextPrev
o.getHiddenParent=TreeWidgetElem_getHiddenParent
o.nodeIndent=0
o.getTooltip=TreeWidgetElem_getTooltip
o.getDragTooltip=TreeWidgetElem_getDragTooltip
o.change=TreeWidgetElem_change
o.deleteAll=TreeWidget_deleteAll
o.setGrayStyle=TreeWidgetElem_setGrayStyle
o.isGrayStyle=TreeWidgetElem_isGrayStyle
o.findByData=TreeWidgetElem_findByData
o.findSimpleByData=TreeWidgetElem_findSimpleByData
o.findById=TreeWidgetElem_findById
o.findInName=TreeWidgetElem_findInName
o.isIncomplete=false
o.querycompleteCB=null
o.setIncomplete=TreeWidgetElem_setIncomplete
o.finishComplete=TreeWidgetElem_finishComplete
o.setEditable=TreeWidgetElem_setEditable
o.showEditInput=TreeWidgetElem_showEditInput
o.isLeaf=TreeWidgetElem_isLeaf
o.isNode=TreeWidgetElem_isNode
o.isSelected=TreeWidgetElem_isSelected
o.htmlWritten=false
return o
}
function TreeWidgetElem_checkCB(elem, id)
{
var o=_TreeWidgetElemInstances[id]
o.checked=elem.checked
if (o.checkCB)
o.checkCB(o, id)
}
function TreeWidgetElem_isChecked()
{
var o=this
return (o.isCheck ? o.checked : false)
}
function TreeWidgetElem_check(checked)
{
var o=this
if (o.isCheck)
{
o.checked = checked
if (o.htmlWritten)
{
o.init()
o.checkElem.checked = checked
}
}
}
function TreeWidgetElem_EditNormalBehaviour(e)
{
eventCancelBubble(e)
return true
}
function TreeWidgetElem_EditBlurCB()
{
setTimeout("TreeWidgetElem_EditKeyCancel("+this.widID+")",1)
}
_globTreeTxtvalue=""
function TreeWidgetElem_EditKeyDown(e)
{
eventCancelBubble(e); 
var k=eventGetKey(e),o=_TreeWidgetElemInstances[this.widID]
if (k==27) 
{
setTimeout("TreeWidgetElem_EditKeyCancel("+this.widID+")",1)
}
else if (k==13) 
{
_globTreeTxtvalue=this.value
setTimeout("TreeWidgetElem_EditKeyAccept("+this.widID+")",1)
}
}
function TreeWidgetElem_EditKeyCancel(id)
{
var o=_TreeWidgetElemInstances[id]
o.showEditInput(false)
}
function TreeWidgetElem_EditKeyAccept(id)
{
var o=_TreeWidgetElemInstances[id]
if (o.validChangeNameCB)
{
if (o.validChangeNameCB(_globTreeTxtvalue)==false)
return
}
o.change(null,_globTreeTxtvalue)
o.showEditInput(false)
if (o.changeNameCB)
o.changeNameCB()
}
_globTreeTxt=null
function TreeWidgetElem_showEditInput(show)
{
var o=this
o.init()
var lyr=o.domElem,css=lyr.style
if (show&&(css.display!="none"))
{
var par=lyr.parentNode,w=lyr.offsetWidth,h=lyr.offsetHeight
css.display="none"
var tl=_globTreeTxt=_curDoc.createElement("INPUT");
tl.type="text"
tl.className="textinputs"
tl.value=o.name
tl.ondragstart=TreeWidgetElem_EditNormalBehaviour
tl.onselectstart=TreeWidgetElem_EditNormalBehaviour
tl.onblur=TreeWidgetElem_EditBlurCB
tl.onkeydown=TreeWidgetElem_EditKeyDown
tl.widID=o.id
var tc=tl.style
tc.width=""+(w+20)+"px"
par.appendChild(tl);
tl.focus()
tl.select()
}
if ((show!=true)&&(css.display=="none"))
{
var tl=_globTreeTxt
if (tl)
{
tl.parentNode.removeChild(tl)
css.display=""
_globTreeTxt=null
}
}
}
function TreeWidgetElem_setEditable(isEditable, changeNameCB,validChangeNameCB)
{
var o=this
if (isEditable)
{
o.changeNameCB=changeNameCB
o.validChangeNameCB=validChangeNameCB
}
o.isEditable=isEditable
}
function TreeWidgetElem_triggerDD()
{
var o=_treeWClickedW,e=_curWin.event
if (o&&(o.clicked)&&(e.button==_leftBtn))
{
if (o.initialX!=null)
{
var x=eventGetX(e),y=eventGetY(e),threshold=3
if ((x<(o.initialX-threshold))||(x>(o.initialX+threshold))||(y<(o.initialY-threshold))||(y>(o.initialY+threshold)))
{
this.dragDrop()
o.clicked=false
}
}
}
}
function TreeWidgetElem_mouseUp()
{
var o=_treeWClickedW,ev=_curWin.event
o.select(null,ev)
o.domElem.onmouseup=null
}
function TreeWidgetElem_init()
{
var o=this
if (o.layer==null)
{
var sub=o.sub,len=sub.length,exp=(len>0)||o.isIncomplete
o.layer=getLayer(_codeWinName+"TWe_"+o.id);
if (o.layer==null)
return;
var cNodes=o.layer.childNodes,cLen=cNodes.length
o.plusLyr=exp?cNodes[0]:null
o.icnLyr=(o.iconId>-1)?cNodes[exp?1:0]:null
o.checkElem=o.isCheck?cNodes[cLen-2]:null
o.domElem=cNodes[cLen-1]
o.toggleLyr=getLayer(_codeWinName+"trTog"+o.id)
if(o.treeView.mouseOverTooltip||o.treeView.mouseOverCB)
o.domElem.onmouseout=TreeFuncMouseout
if (exp)
{
addDblClickCB(o.plusLyr,_tpdb)
}
if (exp&&o.generated)
{
for (var i in sub)
sub[i].init()
}
addDblClickCB(o.domElem,_tpdb)
}
}
function TreeIdToIdx(l)
{
if (l)
{
var id=l.id
if (id)
{
var idx=id.lastIndexOf("TWe_")
if (idx>=0)
return parseInt(id.slice(idx+4))
else
return -1
}
else
return TreeIdToIdx(l.parentNode)
}
return -1
}
function TreeFuncMouseout(e)
{
_tmoc(this,TreeIdToIdx(this),false,e)
}
function _tmvc(l,ev)
{
_tmoc(l,TreeIdToIdx(l),true,ev)
}
function _tpl(l,event)
{
TreeWidget_clickCB(TreeIdToIdx(l),true,event,true);
return _webKit ? true : false
}
function _tpt(l,event)
{
TreeWidget_clickCB(TreeIdToIdx(l),false,event,true);
return _webKit ? true : false
}
function _tpdb(e)
{
treeDblClickCB(TreeIdToIdx(this),_ie?_curWin.event:e)
return false
}
function _tfcc(l,e)
{
treeFCCB(l,TreeIdToIdx(l),true,e)
l.onblur=_tblc
}
function _tblc(e)
{
treeFCCB(this,TreeIdToIdx(this),false,e)
}
function TreeWidgetElem_getHTML(indent,isFirst)
{
var s='';
with (this)
{
htmlWritten=true,isRoot=(par==null)
var len=sub.length,exp=(len>0)||isIncomplete,a=new Array,i=0
if (this.extraIndent)
    indent+=_trIndent*extraIndent
var mouseCB='onfocus="'+_codeWinName+'._tfcc(this,event)" onmousedown="return '+_codeWinName+'._tpt(this,event)" '
if(treeView.mouseOverTooltip || treeView.mouseOverCB)
mouseCB+='onmouseover="'+_codeWinName+'._tmvc(this,event)" '
var contextMenu=''
if (treeView.rightClickMenuCB != null)
{
contextMenu= ' oncontextmenu="' + _codeWinName + '.treeContextMenuCB(\''+ id + '\', event);return false" '
}
var acceptDD=''
if ((treeView.acceptDropCB != null) && (_ie))
{
acceptDD= ' ondragenter="' + _codeWinName + '.TreeWidget_dragOverEnterCB(this,\''+id+'\');" '
acceptDD += ' ondragover="' + _codeWinName + '.TreeWidget_dragOverEnterCB(this,\''+id+'\');" '
}
a[i++]='<div id="'+_codeWinName+'TWe_'+id+'"'+contextMenu+' class=trElt>'
var mouseDown='onmousedown="'+_codeWinName+'._tpl(this,event)" '
if (exp)
a[i++]='<img '+mouseDown+'class=trPlus src="'+_skin+'../'+(expanded?'min':'plus')+(_mac?'mac':'')+'.gif"/>'
var icons=icns?icns:treeView.icns
if (iconId>-1)
a[i++]='<img '+mouseDown+'class=trIcn'+(exp||isRoot?'Plus':'')+' '+attr('src', _skin+'../transp.gif')+attr('title',iconAlt)+' align=top style="'+backImgOffset(icons,(treeView.iconOrientVertical?0:treeView.iconW*(expanded?iconSelId:iconId)),(treeView.iconOrientVertical?treeView.iconH*(expanded?iconSelId:iconId):0))+'" />'
else if (!exp&&!isRoot)
a[i++]='<img class=trSep '+attr('src', _skin+'../transp.gif')+'/>'
if (isCheck)
a[i++]='<input type=checkbox style="margin:0px;" onclick="'+_codeWinName+'.TreeWidgetElem_checkCB(this,\''+id+'\')"'+(this.checked?' checked':'')+'>'
a[i++]='<span '+mouseCB+'tabindex=1'+acceptDD+' class='+nonselectedClass+'>'
a[i++]=(isHTML?name:convStr(name))
a[i++]='</span>'
a[i++]='</div>'
if (exp) a[i++]='<div id="'+_codeWinName+'trTog'+id+'" style="margin-left:18px;display:'+(expanded?'':'none')+'">'
if (expanded)
{
generated=true
for (var j=0;j<len;j++) a[i++]=sub[j].getHTML(0,j==0);
}
if (exp)
{
nodeIndent=indent
a[i++]="</div>"
}
}
return a.join("");
}
function TreeWidgetElem_setGrayStyle(isGray)
{
var o=this,cls=isGray?o.grayTxt:o.blackTxt
if (cls!=o.nonselectedClass)
{
o.nonselectedClass=cls
o.init()
}
if (o.domElem&&(o.domElem.className!=o.selectedClass))
o.domElem.className=cls
}
function TreeWidgetElem_isGrayStyle()
{
return this.nonselectedClass==this.grayTxt
}
function TreeWidgetElem_setIncomplete(querycompleteCB)
{
this.isIncomplete=true
this.querycompleteCB=querycompleteCB
}
function TreeWidgetElem_finishComplete()
{
this.isIncomplete=false
TreeWidget_toggleCB(this.id)
this.treeView.buildElems()
}
function TreeWidgetElem_findByData(data)
{
var o=this
if (o.userData==data)
return o
var sub=o.sub
for (var i in sub)
{
var item=sub[i].findByData(data)
if (item!=null)
    return item
}
return null
}
function TreeWidgetElem_findSimpleByData(data)
{
      var o=this
      var sub=o.sub
      for (var i in sub)
      {
            var item=sub[i]
            if (item.userData==data)
                return item
      }
      return null
}
function TreeWidgetElem_findInName(text,matchCase,matchWholeW,next,starWith)
{
var o=this, name=o.name
if(text=="" || text==null) return; 
if(!matchCase || (matchCase == null))
{
name=name.toLowerCase();
text=text.toLowerCase();
}
if(matchWholeW)
{
var arrWords = name.split(" ");
for(var i = 0; i<arrWords.length; i++)
{
if(arrWords[i] == text)
return o;
}
}
else
{
var idx = name.indexOf(text); 
if (starWith == true ) 
{
if(idx == 0) return o;
}
else
{
if(idx>-1) return o;
}
}
return null
}
function TreeWidgetElem_findById(id)
{
var o=this
if (o.id==id)
return o
var sub=o.sub
for (var i in sub)
{
var item=sub[i].findById(id)
if (item!=null)
    return item
}
return null
}
function TreeWidgetElem_change(iconId, name, userData, help,iconSelId,tooltip)
{
var o=this,treeView=o.treeView
if (iconId!=null) o.iconId=iconId
if (name!=null) o.name=name
o.userData=userData
if (help!=null) o.help=help
o.iconSelId=(iconSelId!=null)?iconSelId:o.iconId
if (tooltip!=null) o.tooltip=tooltip
o.init()
if (o.domElem)
o.domElem.innerHTML=(o.isHTML?o.name:convStr(o.name))
if (o.icnLyr)
{
changeOffset(o.icnLyr,
             treeView.iconOrientVertical?0:o.treeView.iconW*(o.expanded?o.iconSelId:o.iconId),
             treeView.iconOrientVertical?o.treeView.iconH*(o.expanded?o.iconSelId:o.iconId):0)
}
}
function treeInitDropFunc(lyr,elemId)
{
var e=_TreeWidgetElemInstances[elemId]
if (lyr.ondrop==null)
{
e.treeView.dragDrop.attachCallbacks(lyr,true)
lyr.domEltID=elemId
}
}
function TreeWidget_toggleCB(elemId,noTimeOut)
{
var elem=_TreeWidgetElemInstances[elemId]
if (elem.sub.length==0) {
    if(elem.plusLyr)
elem.plusLyr.style.visibility='hidden'
return
}
elem.expanded=!elem.expanded
elem.init()
if (noTimeOut) {
dispIcn(elemId);
} else {
setTimeout(elem.treeView.dispIcnFuncName+'('+elemId+')',1)
}
var tree=elem.treeView
if (elem.expanded&&tree.expandCB)
tree.expandCB(elem.userData)
if (!elem.expanded&&tree.collapseCB)
tree.collapseCB(elem.userData)
}
function dispIcn(eId)
{
var e=_TreeWidgetElemInstances[eId]
with (e)
{
        if (toggleLyr) 
        {
if (expanded&&!generated)
{
generated=true;
var a=new Array,i=0,len=sub.length,newInd=nodeIndent+_trIndent
for (var j=0;j<len;j++) a[i++]=sub[j].getHTML(newInd,j==0);
toggleLyr.innerHTML=a.join('');
}
toggleLyr.style.display=expanded?'block':'none'
plusLyr.src=_skin+'../'+(expanded?'min':'plus')+(_mac?'mac':'')+'.gif'
plusLyr.title=expanded?_expandedLab:_collapsedLab
    if(icnLyr)
{
    changeOffset(icnLyr,
treeView.iconOrientVertical?0:treeView.iconW*(expanded?iconSelId:iconId),
treeView.iconOrientVertical?treeView.iconH*(expanded?iconSelId:iconId):0)
}
}
}
}
function TreeWidgetElem_add(elem)
{
with (this)
{
elem.treeView=treeView;
elem.par=this;
sub[sub.length]=elem;
}
return elem
}
function TreeWidgetElem_getHiddenParent()
{
var par=this.par
if (par==null) return null
if (!par.expanded)
return par
return
par.getHiddenParent()
}
function TreeWidgetElem_getNextPrev(delta)
{
with (this)
{
if (elemPos==-1) treeView.buildElems()
var newPos=elemPos+delta
if ((newPos>=0)&&(newPos<treeView.elems.length))
{
var ret=treeView.elems[newPos]
var hidden=ret.getHiddenParent()
if (hidden!=null) return ret.getNextPrev(delta)
else return ret
}
else return null;
}
}
function TreeWidgetElem_scroll(elemLyr,treeLyr)
{
var scrollH = Math.max(0,treeLyr.offsetHeight-20), scrollY = treeLyr.scrollTop
var elPos = getPos(elemLyr,treeLyr)
var y = elPos.offsetTop, h = elemLyr.offsetHeight
if ((y-scrollY+h) > scrollH ) {
treeLyr.scrollTop=y+h-scrollH
}
if ((y-scrollY) < 0) {
treeLyr.scrollTop= y
}
}
function TreeWidgetElem_unselect()
{
var o=this
with(o)
{
init()
if (domElem) {
domElem.className=o.nonselectedClass
}
treeView.selId=-1
if(treeView.multiSelection)
{
var idx = arrayFind(treeView,'selIds',id)
if(idx>-1)
{
arrayRemove(treeView,'selIds',idx);
treeView.layer._BOselIds=""
var len = treeView.selIds.length;
for(var i=0;i<len;i++)
{
if(treeView.layer._BOselIds == "")
treeView.layer._BOselIds=""+treeView.selIds[i];
else
treeView.layer._BOselIds+=";"+treeView.selIds[i];
}
}
}
}
}
function TreeWidgetElem_select(setFocus,ev,noSendClickCB,isFromKeybArrow)
{
var coll=new Array
var par=this.par
while (par)
{
if (!par.expanded)
coll[coll.length]=par
par=par.par
}
var cLen=coll.length
if (cLen>0)
{
    for (var i=cLen-1;i>=0;i--)
    {
    TreeWidget_toggleCB(coll[i].id,true)
    }
this.select(setFocus,ev,noSendClickCB,isFromKeybArrow);
return
}
if(this.treeView.multiSelection)
{ 
TreeWidgetElem_multiSelect(this,setFocus,ev,noSendClickCB,isFromKeybArrow);
return;
}
if (noSendClickCB==null)
noSendClickCB=false
with (this)
{
if (treeView.selId!=id)
{
if (treeView.selId>=0)
{
var prev=_TreeWidgetElemInstances[treeView.selId]
prev.init()
if (prev.domElem) {
prev.domElem.className=prev.nonselectedClass
}
}
treeView.selId=id;
init()
treeView.layer._BOselId=id
var de=domElem
if (de == null) return
if(treeView.hlPath)
treeView.highlightPath(id);
else
de.className=selectedClass
if (setFocus) {
safeSetFocus(de)
}
TreeWidgetElem_scroll(de,treeView.layer)
}
if ((treeView.clickCB)&&(!noSendClickCB)) treeView.clickCB(userData,isFromKeybArrow!=null?isFromKeybArrow:false)
}
}
_startShift=null;
function TreeWidgetElem_multiSelect(o,setFocus,ev,noSendClickCB,isFromKeybArrow)
{
if (noSendClickCB==null)
noSendClickCB=false
with (o) 
{
init();
var de=domElem
if (de == null) return
if(treeView.hlPath) treeView.unhlPath();
if(ev == null)
{
var idx = arrayFind(treeView,'selIds',id);
if(idx == -1)
{
treeView.selIds[treeView.selIds.length]=id;
if(treeView.layer._BOselIds == "")
treeView.layer._BOselIds=""+id;
else
treeView.layer._BOselIds+=";"+id;
de.className=selectedClass
}
_startShift=null;
}
else 
{
var idx = arrayFind(treeView,'selIds',id);
var ctrl=eventIsCtrl(ev)
var shift=_ie?_curWin.event.shiftKey:ev.shiftKey
var typeEvt=_ie?_curWin.event.type:ev.type
if(ctrl && !shift) 
{
if(idx == -1)
{   
treeView.selIds[treeView.selIds.length]=id;
if(treeView.layer._BOselIds == "")
treeView.layer._BOselIds=""+id;
else
treeView.layer._BOselIds+=";"+id;
de.className=selectedClass
}
else 
{
unselect();
}
_startShift= o;
}
if(shift) 
{
var lastSelId=-1,lastSel=null;
if(treeView.selIds.length>0)
{
lastSelId = treeView.selIds[treeView.selIds.length-1];
lastSel = _TreeWidgetElemInstances[lastSelId];
if(_startShift == null)
_startShift = lastSel;
if(!ctrl) 
treeView.unselect();
TreeWidgetElem_multiSelectShift(_startShift.id,id);
}
else 
{
treeView.unselect();
treeView.selIds[0]=id;
treeView.layer._BOselIds=""+id;
if(treeView.hlPath)
treeView.highlightPath(id);
else
de.className=selectedClass;
_startShift=null;
}
}
if(!ctrl && !shift) 
{
var idx = arrayFind(treeView,'selIds',id);
if( _ie &&typeEvt=="mousedown" && idx>-1)
{
window._treeWClickedW=o
de.onmouseup=TreeWidgetElem_mouseUp
}
else
{
treeView.unselect();
treeView.selIds[0]=id;
treeView.layer._BOselIds=""+id;
if(treeView.hlPath)
treeView.highlightPath(id);
else
de.className=selectedClass
_startShift=null;
}
}
}
if (setFocus) {
safeSetFocus(de)
}
TreeWidgetElem_scroll(de,treeView.layer)
if ((treeView.clickCB)&&(!noSendClickCB)) treeView.clickCB(userData,isFromKeybArrow!=null?isFromKeybArrow:false)
}
}
function TreeWidgetElem_multiSelectCtrl()
{
}
function TreeWidgetElem_multiSelectShift(id1,id2)
{
var elem1=_TreeWidgetElemInstances[id1];
var elem2=_TreeWidgetElemInstances[id2];
var treeView = elem1?elem1.treeView:null;
if(treeView == null) return;
if (elem1.elemPos==-1 || elem2.elemPos==-1 ) treeView.buildElems()
var startPos= (elem1.elemPos<elem2.elemPos)?elem1.elemPos:elem2.elemPos
var endPos= (elem1.elemPos>elem2.elemPos)?elem1.elemPos:elem2.elemPos
if ((startPos>=0)&&(endPos<treeView.elems.length))
{
for(var j=startPos;j<=endPos;j++)
{
var elem = treeView.elems[j];
var hidden=elem.getHiddenParent();
if((hidden == null)&&(arrayFind(treeView,'selIds',elem.id) == -1))
{
treeView.selIds[treeView.selIds.length]=elem.id;
if(treeView.layer._BOselIds == "")
treeView.layer._BOselIds=""+elem.id;
else
treeView.layer._BOselIds+=";"+elem.id;
elem.init();
if(elem.domElem)
elem.domElem.className=elem.selectedClass;
}
}
}
}
function TreeWidget_clickCB(elemId,isIcon,ev,isDown)
{
eventCancelBubble(ev)
var e=_TreeWidgetElemInstances[elemId]
if (e==null)
return
if (e.treeView.layer == null)
return
e.init()
if (isIcon&&(e.sub.length>0)) TreeWidget_toggleCB(elemId)
else if (isIcon&&e.isIncomplete&&e.querycompleteCB)
e.querycompleteCB()
else e.select(null,ev)
if (_curDoc.onmousedown)
_curDoc.onmousedown(ev)
if (isDown&&_ie)
{
window._treeWClickedW=e
e.init()
e.clicked=true
e.initialX=eventGetX(ev)
e.initialY=eventGetY(ev)
if (_ie&&e.domElem)
e.domElem.onmousemove=TreeWidgetElem_triggerDD
}
if (_moz&&e.domElem)
setTimeout("_TreeWidgetElemInstances["+elemId+"].domElem.focus()",1)
return false
}
function treeDblClickCB(elemId,ev)
{
eventCancelBubble(ev)
var e=_TreeWidgetElemInstances[elemId],treeView=e.treeView;
if (e.sub.length>0) TreeWidget_toggleCB(elemId)
else if (e.isIncomplete&&e.querycompleteCB)
{
e.querycompleteCB()
return
}
if (e.isEditable)
e.showEditInput(true)
else
{
if (treeView.doubleClickCB) 
if (_webKit)
setTimeout("delayedTreeDblClickCB("+elemId+")",1)
else
treeView.doubleClickCB(e.userData);
}
}
function delayedTreeDblClickCB(elemId)
{
var e=_TreeWidgetElemInstances[elemId],treeView=e.treeView;
treeView.doubleClickCB(e.userData);
}
function TreeWidgetElem_UpdateTooltip(newId,forceSelect)
{
var elem=_TreeWidgetElemInstances[newId];
if(elem)
{
elem.init();
if(elem.domElem != null)
elem.domElem.title = elem.getTooltip(forceSelect);
}
}
function TreeWidgetElem_getDragTooltip()
{
var o=this
if (o.obj && o.obj.getDragTooltip) return o.obj.getDragTooltip()
return o.name
}
function TreeWidgetElem_getTooltip(forceSelect)
{
var tooltip='',o=this
var itemSelected=false;
if(o.treeView.multiSelection)
{
itemSelected = (arrayFind(o.treeView,'selIds',o.id) > -1);
}
else
{
itemSelected = (o.treeView.selId == o.id);
}
if (forceSelect || itemSelected)
tooltip = _selectedLab + ' ';
tooltip+=o.name;
if((o.sub.length > 0) || (o.isIncomplete))
{
if(o.expanded)
tooltip += ' '+ _expandedLab + ' ';
else
tooltip += ' '+ _collapsedLab + ' ';
}
if(o.advTooltip)
{
tooltip+=' ('+o.advTooltip+')'
}
return tooltip;
}
function treeFCCB(e,elemId,focus,ev)
{
var elem =_TreeWidgetElemInstances[elemId];
if ((elem==null) || elem.treeView.mouseOverTooltip)
return
if(focus)
{
if (elem.treeView.customTooltip != null)
{
elem.init()
elem.treeView.customTooltip.showCustomTooltip(elem.userData,ev)
}
else
e.title = elem.getTooltip();
}
else
{
if (elem.treeView.customTooltip != null)
elem.treeView.customTooltip.hideCustomTooltip() 
else 
e.title = ""; 
}
}
function _tmoc(e,elemId,over,ev)
{
var elem =_TreeWidgetElemInstances[elemId];
if(elem==null)
return
if(elem.treeView.mouseOverTooltip)
{
if(over)
{
if (elem.treeView.customTooltip != null)
{
elem.init()
elem.treeView.customTooltip.showCustomTooltip(elem.userData,ev)
}
else
e.title = elem.tooltip?elem.tooltip:''
}
else
{
if (elem.treeView.customTooltip != null)
elem.treeView.customTooltip.hideCustomTooltip()
else
e.title =''
}
}
if(elem.treeView.mouseOverCB)
elem.treeView.mouseOverCB(elem)
}
function treeContextMenuCB(elemId,ev)
{
var elem =_TreeWidgetElemInstances[elemId];
if(elem)
{
elem.treeView.rightClickMenuCB(elemId, _ie?_curWin.event:ev)
}
}
function TreeWidgetElem_isLeaf()
{
return (this.sub.length==0 && !this.isIncomplete);
}
function TreeWidgetElem_isNode()
{
return (!this.isLeaf());
}
function TreeWidgetElem_isSelected()
{
var o=this;
if(o.treeView.multi)
{
var idx = arrayFind(o.treeView,'selIds',o.id);
return (idx>=0);
}
else
{
return (o.id == o.treeView.selId);
}
}
function newTreeWidgetHTMLElem(iconID, name, obj, userData, help)
{
var o=newTreeWidgetElem(iconID, name, userData, help);
o.obj=obj
o.getHTML=TreeWidgetHTMLElem_getHTML;
o.selectedClass='filterBoxSelected';
o.nonselectedClass='filterBox';
o.feedbackDDClass='filterBoxFeedbackDD'
o.init=TreeWidgetHTMLElem_init
return o;
}
function TreeWidgetHTMLElem_init()
{
this.domElem=getLayer(_codeWinName+'trLstElt' + this.id)
}
function TreeWidgetHTMLElem_getHTML(indent,isFirst)
{
with (this)
{
len=sub.length,exp=(len>0),a=new Array,i=0
var mouseDownCB='onmousedown="'+_codeWinName+'.TreeWidget_clickCB(\''+ id + '\', false, event, true);return false" '
var contextMenu=''
if (treeView.rightClickMenuCB != null)
{
contextMenu= ' oncontextmenu="' + _codeWinName + '.treeContextMenuCB(\''+ id + '\', event);return false" '
}
var acceptDD=''
if ((treeView.acceptDropCB != null) && (_ie))
{
acceptDD= ' ondragenter="' + _codeWinName + '.TreeWidget_dragOverEnterCB(this,\''+id+'\');" '
acceptDD += ' ondragover="' + _codeWinName + '.TreeWidget_dragOverEnterCB(this,\''+id+'\');" '
}
var icons=icns?icns:treeView.icns
a[i++] = '<table border="0" cellspacing="0" cellpadding="0"><tbody><tr>'
a[i++] = '<td>' + getSpace(indent + 16, 16) + '</td>'
a[i++] = '<td>'
a[i++] ='<table id="'+_codeWinName+'trLstElt' + id + '" class="filterBox" ondblclick="'+_codeWinName+'.treeDblClickCB('+id+',event);if(_saf||_ie) return false" ' + contextMenu + acceptDD + mouseDownCB + ' border="0" cellspacing="0" cellpadding="0">'
a[i++] ='<tbody><tr>'
a[i++] ='<td width="20">'
a[i++] ='<span style="padding:2px">'
a[i++] =(iconId>-1?imgOffset(icons,treeView.iconW,treeView.iconH, treeView.iconW * iconId, treeView.iconH * iconId,null,null,null,'','top'):'')
a[i++] ='</span>'
a[i++] ='</td>'
a[i++] ='<td>' + obj.getHTML(treeView, userData) + '</td>'
a[i++] ='</tr></tbody></table>'
a[i++] = '</td></tr></tbody></table>'
}
return  a.join("");
}
function newTreeWidgetVoidElem(iconID, name, obj, userData, help)
{
var o=newTreeWidgetElem(iconID, name, userData, help);
o.obj=obj
o.getHTML=TreeWidgetVoidElem_getHTML;
o.selectedClass='filterTextSelected';
o.nonselectedClass='filterText';
o.feedbackDDClass='filterTextFeedbackDD '
o.init=TreeWidgetVoidElem_init
return o;
}
function TreeWidgetVoidElem_init()
{
this.domElem=getLayer(_codeWinName+'trLstElt' + this.id)
}
function TreeWidgetVoidElem_getHTML(indent,isFirst)
{
return  ""
}
function newStaticTreeWidget(id, originalTree)
{
var o = newWidget(id);
o.sub = originalTree.sub;
return o;
}
function newFolderWidget(id, exp)
{
var o=newWidget(id)
o.expanded= (exp)? exp : false
o.getCross=FolderWidget_getCross
return o;
}
function FolderWidget_getCross()
{
if (_printDoc)
    return imgOffset(_skin+'../transp.gif', 1, 1, null, null, null, null, null,null,null,'top')
with(this)
{
return '<span style="cursor:'+_hand+'" id="icn'+id+'" '+(_moz?'onclick':'onmousedown')+'="'+_codeWinName+'.clickCrossCB('+id+'); if (_ie) return false" ondblclick="'+_codeWinName+'.clickCrossCB('+id+'); if (_ie) return false">' +
imgOffset(_skin +(_mac?'../treemac.gif':'../tree.gif'), 13, 9, expanded?0:13, _mac?2:3, 'img'+ id, null, expanded?_expandedLab:_collapsedLab,null,null,'top') +
'</span>'
}
}

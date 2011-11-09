if (window._DHTML_LIB_MENU_JS_LOADED==null)
{
_DHTML_LIB_MENU_JS_LOADED=true
_menusZIndex=2000
_menusItems=new Array
_globMenuCaptured=null
_isColor=0
_isLastUsedColor=1
_isNotColor=2
_currentFocus=null
_mitemH=22
}
function newMenuWidget(id,hideCB,beforeShowCB)
{
var o=newWidget(id)
o.items=new Array
o.par=null
o.container=null
o.currentSub=-1
o.nextSub=-1
o.zIndex=_menusZIndex
o.hideCB=hideCB
o.beforeShowCB=beforeShowCB
o.className="menuFrame"
o.leftZoneClass="menuLeftPart"
o.leftZoneSelClass="menuLeftPartSel"
o.menuIconClass="menuIcon"
o.menuIconCheckClass="menuIconCheck"
o.menuTextPartClass="menuTextPart"
o.menuTextPartClassDis="menuTextPartDisabled"
o.menuTextPartClassSel="menuTextPartSel"
o.menuRightPartClass="menuRightPart"
o.menuRightPartClassSel="menuRightPartSel"
o.init=MenuWidget_init
o.justInTimeInit=MenuWidget_justInTimeInit
o.getHTML=MenuWidget_getHTML
o.show=MenuWidget_show
o.internalAdd=o.add=MenuWidget_add
o.addCheck=MenuWidget_addCheck
o.addSeparator=MenuWidget_addSeparator
o.insert=MenuWidget_insert
o.insertCheck=MenuWidget_insertCheck
o.insertSeparator=MenuWidget_insertSeparator
o.getItem=MenuWidget_getItem
o.getItemByID=MenuWidget_getItemByID
o.isShown=MenuWidget_isShown
o.remove=MenuWidget_remove
o.removeByID=MenuWidget_removeByID
o.setClasses=MenuWidget_setClasses
o.updateArrows=MenuWidget_updateArrows
o.showSub=MenuWidget_showSub
o.captureClicks=MenuWidget_captureClicks
o.releaseClicks=MenuWidget_releaseClicks
o.focus=MenuWidget_focus
o.restoreFocus=MenuWidget_restoreFocus
o.hasVisibleItem=MenuWidget_hasVisibleItem
o.updateIndex=MenuWidget_updateIndex
o.removeAll=MenuWidget_removeAll
o.isScrollable=false
o.getMenuTBody=MenuWidget_getMenuTBody
o.clickCB=new Array
o.clickCBDocs=new Array
o.write=MenuWidget_write
o.alignLeft=false
o.sepCount=0
return o
}
function MenuWidget_captureClicks(w)
{
var o=this
if (o.par==null)
{
if (w==null)
{
_globMenuCaptured=o
o.clickCB.length=0
o.clickCBDocs.length=0
w=getTopAccessibleFrame(_curWin)
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
d.onmousedown=MenuWidget_globalClick
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
function MenuWidget_releaseClicks()
{
var o=this
if (o.par==null)
{
var len=o.clickCB.length
for (var i=0;i<len;i++)
{
try
{
o.clickCBDocs[i].onmousedown=o.clickCB[i]
}
catch(expt)
{
}
o.clickCB[i]=null
o.clickCBDocs[i]=null
}
o.clickCB.length=0
o.clickCBDocs.length=0
}
}
_menuItem=null;
function MenuWidget_focus()
{
var o=this, items=o.items, len=items.length
for(var i=0; i<len;i++)
{
if(items[i].isShown && !items[i].isSeparator)
{
_menuItem=items[i];
setTimeout("_menuItem.focus()",1);
if(o.endLink) o.endLink.show(true)
if(o.startLink) o.startLink.show(true)
break;
}
}
}
function MenuWidget_keepFocus(id)
{
var o=getWidget(getLayer(id))
if (o) o.focus();
}
function MenuWidget_restoreFocus()
{
var o=this
if(o.endLink) o.endLink.show(false)
if(o.startLink) o.startLink.show(false)
if(o.parIcon) o.parIcon.focus()
else 
if (o.par)o.par.focus()
else if(o.parCalendar) o.parCalendar.focus()
}
function MenuWidget_keyDown(id,e)
{
var o=getWidget(getLayer(id))
var key=eventGetKey(e)
if(key==27 && o)
{
o.restoreFocus()
o.show(false)
if (o.par && o.par.par)
{
o.par.par.currentSub=-1
}
o.currentSub=-1
eventCancelBubble(e);
}
else if(o && (key==109 || key==37))
{
if (o.par && o.par.par)  
{
o.restoreFocus()
o.show(false)
o.par.par.currentSub=-1
o.currentSub=-1
}
}
else if(key==13)
{
eventCancelBubble(e);
}
}
function MenuWidget_globalClick()
{
var o=_globMenuCaptured
if (o!=null)
{
_globMenuToHide=o
_globMenuCaptured=null
o.releaseClicks()
setTimeout("_globMenuToHide.show(false)",1)
}
}
function MenuWidget_add(id,text,cb,icon,dx,dy,disabled,disDx,disDy,alt)
{
var o=this,i=o.items.length
var ret=o.items[i]=newMenuItem(o,id,text,cb,icon,dx,dy,disabled,disDx,disDy,false,alt)
ret.menuIndex=i
ret.dynHTML()
return ret
}
function MenuWidget_addCheck(id,text,cb,icon,dx,dy,disabled,disDx,disDy,alt)
{
var o=this,i=o.items.length
var ret=o.items[i]=newMenuItem(o,id,text,cb,icon,dx,dy,disabled,disDx,disDy,true,alt)
ret.menuIndex=i
ret.dynHTML()
return ret
}
function MenuWidget_addSeparator()
{
var s=this.internalAdd("_menusep_"+(this.sepCount++))
s.isSeparator=true
return s
}
function MenuWidget_insert(index,id,text,cb,icon,dx,dy,disabled,disDx,disDy,alt)
{
var o=this, item = newMenuItem(o,id,text,cb,icon,dx,dy,disabled,disDx,disDy,false,alt);
arrayAdd(o,'items',item,index);
o.updateIndex();
item.dynHTML()
return item
}
function MenuWidget_insertCheck(index,id,text,cb,icon,dx,dy,disabled,disDx,disDy,alt)
{
var o=this, item = newMenuItem(o,id,text,cb,icon,dx,dy,disabled,disDx,disDy,true,alt);
arrayAdd(o,'items',item,index);
o.updateIndex();
item.dynHTML()
return item
}
function MenuWidget_insertSeparator(index)
{
var item = newMenuItem(this,"_menusep_"+(this.sepCount++));
item.isSeparator=true;
arrayAdd(this,'items',item,index);
this.updateIndex();
item.dynHTML()
return item
}
function MenuWidget_init()
{
}
function MenuWidget_getItem(index)
{
var o=this,items=o.items
if ((index>=0)&&(index<items.length))
return items[index]
return null
}
function MenuWidget_getItemByID(id)
{
var o=this,items=o.items
for(var i in items)
{
if(items[i].id == id)
return items[i];
}
return null
}
function MenuWidget_getMenuTBody()
{
return this.layer.childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0]
}
function MenuWidget_removeByID(id)
{
var o=this;
var item = o.getItemByID(id);
if(item)
{
arrayRemove(o,'items',item.menuIndex);
o.updateIndex();
if (o.layer==null)
return
var tbody=o.getMenuTBody()
tbody.deleteRow(item.menuIndex);
}
}
function MenuWidget_remove(index)
{
var o=this;
if(index != null)
{
arrayRemove(o,'items',index);
}
else 
{
o.items.length = 0;
}
o.updateIndex();
if (o.layer==null)
return
if (index != null)
{
var tbody=o.getMenuTBody()
tbody.deleteRow(index);
}
else
{
        o.layer.parentNode.removeChild(o.layer);
        o.layer=null
}
}
function MenuWidget_removeAll()
{
this.remove()
}
function MenuWidget_updateIndex()
{
var items = this.items,len = items.length
for(var i=0; i<len;i++)
{
items[i].menuIndex=i;
}
}
function MenuWidget_showSub()
{
var o=this
if (o.nextSub!=-1)
{
if (o.nextSub!=o.currentSub)
{
var currentItem=o.items[o.currentSub]
if (currentItem&&currentItem.sub)
{
currentItem.sub.show(false)
o.currentSub=-1
}
var nextItem=o.items[o.nextSub]
if (nextItem&&nextItem.sub)
{
var lyr=nextItem.layer
var x=parseInt(o.css.left)
var y=parseInt(o.css.top)
for (var i=0;i<o.nextSub;i++)
{
var item=o.items[i]
if (item.isShown)
{
if ((item.icon!=null)||(item.text!=null))
y+=_mitemH
else
y+=3
}
}
var w=o.getWidth()
x=x+w-4
if (o.isScrollable)
{
var frameTBody=o.layer.childNodes[0]
var menuDiv=o.menuDiv?o.menuDiv:getLayer("menuDiv_"+o.id)
var topScr=frameTBody.childNodes[0]
y-=menuDiv.scrollTop
if (topScr.style.display!="none")
y+=13
}
nextItem.attachSubMenu(nextItem.sub)
nextItem.sub.show(true,x,y,false,w)
o.currentSub=o.nextSub
}
}
}
else if (o.currentSub!=-1)
{
var currentItem=o.items[o.currentSub]
if (currentItem&&currentItem.sub)
{
currentItem.sub.show(false)
o.currentSub=-1
}
}
}
function MenuWidget_write()
{
}
function MenuWidget_justInTimeInit()
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
o.endLink=newWidget("endLink_"+o.id)
o.endLink.init()
o.startLink=newWidget("startLink_"+o.id)
o.startLink.init()
var items=o.items
for (var i in items)
items[i].init()
}
function MenuWidget_setClasses(className,
                               leftZoneClass,
                               leftZoneSelClass,
                               menuIconClass,
                               menuIconCheckClass,
                               menuTextPartClass,
                               menuTextPartClassDis,
                               menuTextPartClassSel,
                               menuRightPartClass,
                               menuRightPartClassSel)
{
var o=this
if (className)             o.className             = className
if (leftZoneClass)         o.leftZoneClass         = leftZoneClass
if (leftZoneSelClass)      o.leftZoneSelClass      = leftZoneSelClass
if (menuIconClass)         o.menuIconClass         = menuIconClass
if (menuIconCheckClass)    o.menuIconCheckClass    = menuIconCheckClass
if (menuTextPartClass)     o.menuTextPartClass     = menuTextPartClass
if (menuTextPartClassDis)  o.menuTextPartClassDis  = menuTextPartClassDis
if (menuTextPartClassSel)  o.menuTextPartClassSel  = menuTextPartClassSel
if (menuRightPartClass)    o.menuRightPartClass    = menuRightPartClass
if (menuRightPartClassSel) o.menuRightPartClassSel = menuRightPartClassSel
for (var i in o.items)
{
var item=o.items[i]
item.leftZoneClass=leftZoneClass
item.leftZoneSelClass=leftZoneSelClass
if (item.sub)
{
item.sub.setClasses(className,
leftZoneClass,
leftZoneSelClass,
menuIconClass,
menuIconCheckClass,
menuTextPartClass,
menuTextPartClassDis,
menuTextPartClassSel,
menuRightPartClass,
menuRightPartClassSel)
}
}
if (o.layer)
{
        o.layer.parentNode.removeChild(o.layer);
        o.layer=null
}
}
function MenuWidget_getHTML()
{
var o=this,items=o.items
var keysCbs=' onkeydown="'+_codeWinName+'.MenuWidget_keyDown(\''+o.id+'\',event);return true" '
o.isScrollable=true
var s='<a style="position:absolute;left:-30px;top:-30px; visibility:hidden" id="startLink_'+o.id+'" href="javascript:void(0)" onfocus="'+_codeWinName+'.MenuWidget_keepFocus(\''+o.id+'\');return false;" ></a>'+
'<table style="display:none;" class="'+o.className+'" id="'+o.id+'" cellspacing="0" cellpadding="0" border="0"><tbody>'+
'<tr><td align="center" class="menuScrollTop">'+simpleImgOffset(_skin+'menuscroll.gif',5,12,0,12,"scrTop_"+o.id)+'</td></tr>'+
'<tr><td><div id="menuDiv_'+o.id+'">'+
'<table cellspacing="0" cellpadding="0" border="0" '+keysCbs+'><tbody>'
for (var i in items)
s+=items[i].getHTML()
s+='</tbody></table></div></td></tr>'+
'<tr ><td align="center" class="menuScrollBottom">'+simpleImgOffset(_skin+'menuscroll.gif',5,12,0,0,"scrBottom_"+o.id)+'</td></tr></tbody></table><a style="position:absolute;left:-30px;top:-30px; visibility:hidden" id="endLink_'+o.id+'" href="javascript:void(0)" onfocus="'+_codeWinName+'.MenuWidget_keepFocus(\''+o.id+'\');return false;" ></a>'
return s
}
function MenuWidget_updateArrows()
{
var o=this
}
_menuCurrScrollWidget=null
_menuCurrScrollTimer=null
function MenuWidget_doScroll(up)
{
var o=_menuCurrScrollWidget
if (o)
{
var frameTBody=o.layer.childNodes[0]
var menuDiv=frameTBody.childNodes[1].childNodes[0].childNodes[0]
var menu=menuDiv.childNodes[0]
var scroll=menuDiv.scrollTop
if (up)
scroll-=4
else
scroll+=4
scroll=Math.max(0,scroll)
var maxScr=menu.offsetHeight-menuDiv.offsetHeight
scroll=Math.min(maxScr,scroll)
menuDiv.scrollTop=scroll
if ((scroll==maxScr)&&_menuCurrScrollTimer)
{
clearInterval(_menuCurrScrollTimer)
_menuCurrScrollTimer=null
}
}
else if (_menuCurrScrollTimer)
{
clearInterval(_menuCurrScrollTimer)
_menuCurrScrollTimer=null
}
}
function MenuWidget_scrollOver()
{
var o=getWidget(this)
_menuCurrScrollWidget=o
o.nextSub=-1
o.showSub()
MenuWidget_doScroll(this.webiIsTop==1)
this.childNodes[0].className=this.webiIsTop==1?"menuScrollTopHover":"menuScrollBottomHover"
_menuCurrScrollTimer=setInterval('MenuWidget_doScroll('+(this.webiIsTop==1?'true':'false')+')',30)
}
function MenuWidget_scrollDown(e)
{
eventCancelBubble(e)
return false
}
function MenuWidget_scrollOut()
{
if (_menuCurrScrollTimer)
{
clearInterval(_menuCurrScrollTimer)
_menuCurrScrollTimer=null
}
this.childNodes[0].className=this.webiIsTop==1?"menuScrollTop":"menuScrollBottom"
}
function MenuWidget_show(show,x,y,parentPropagate,parentMenuW,buttonFrom)
{
var o=this
if (o.layer==null)
o.justInTimeInit()
var css=o.css 
if (_menuCurrScrollTimer)
{
clearInterval(_menuCurrScrollTimer)
_menuCurrScrollTimer=null
}
if (show)
{
var winH=winHeight()
if (o.iframeLyr==null)
{
o.iframeLyr=getDynamicBGIFrameLayer()
o.iframeCss=o.iframeLyr.style
}
if (o.beforeShowCB)
o.beforeShowCB()
if(!o.hasVisibleItem()) return;
o.captureClicks()
css.display='block'
css.zIndex=(o.zIndex+1)
css.visibility="hidden"
css.left="-1000px"
css.top="-1000px"
if (o.isScrollable)
{
var frameTBody=o.layer.childNodes[0]
var topScr=frameTBody.childNodes[0]
var bottomScr=frameTBody.childNodes[2]
topScr.style.display="none"
bottomScr.style.display="none"
var menuDiv=frameTBody.childNodes[1].childNodes[0].childNodes[0]
menuDiv.style.overflow=""
menuDiv.style.height=""
menuDiv.scrollTop=0
}
var w=o.getWidth()
var h=o.getHeight()
if (o.alignLeft)
x-=w
if (buttonFrom)
{
var buttonW=buttonFrom.getWidth()
if (buttonW>w)
x=x+buttonW-w
}
var x2=x+w+4,y2=y+h+4
if (x2-getScrollX()>winWidth())
{
if (buttonFrom)
{
x=Math.max(0,winWidth()-w)
}
else
x=Math.max(0,x-4-(w+((parentMenuW!=null)?parentMenuW-12:0)))
}
if (y2-getScrollY()>winH)
y=Math.max(0,y-4-h+(parentMenuW!=null?30:0))
css.left=""+x+"px"
css.top=""+y+"px"
css.visibility="visible"
if (o.isScrollable)
{
var frameTBody=o.layer.childNodes[0]
var topScr=frameTBody.childNodes[0]
var bottomScr=frameTBody.childNodes[2]
var menuDiv=o.menuDiv?o.menuDiv:getLayer("menuDiv_"+o.id)
if (h>winH)
{
topScr.style.display=""
bottomScr.style.display=""
var topScrTD=topScr.childNodes[0]
var bottomScrTD=bottomScr.childNodes[0]
topScrTD.style.width=""+w+"px"
bottomScrTD.style.width=""+w+"px"
topScrTD.className="menuScrollTop"
bottomScrTD.className="menuScrollBottom"
topScr.onmouseover=MenuWidget_scrollOver
topScr.onmousedown=MenuWidget_scrollDown
topScr.onmouseup=MenuWidget_scrollDown
topScr.onmouseout=MenuWidget_scrollOut
topScr.webiIsTop=1
bottomScr.onmouseover=MenuWidget_scrollOver
bottomScr.onmousedown=MenuWidget_scrollDown
bottomScr.onmouseup=MenuWidget_scrollDown
bottomScr.onmouseout=MenuWidget_scrollOut
menuDiv.style.overflow="hidden"
menuDiv.style.height="" + (Math.max(10,winH-26-2)) + "px"
menuDiv.scrollTop=0
o.updateArrows()
}
else
{
menuDiv.style.overflow=""
menuDiv.style.height=""
menuDiv.scrollTop=0
}
}
w=o.getWidth()
h=o.getHeight()
iCss=o.iframeCss
iCss.left=""+x+"px"
iCss.top=""+y+"px"
iCss.width=""+w+"px"
iCss.height=""+h+"px"
iCss.zIndex=o.zIndex-1
iCss.display='block'
if (_ie)
{
y-=2
x-=2
}
o.nextSub=-1
o.showSub()
o.focus()
}
else
{
if (parentPropagate && o.par && o.par.par)
{
o.par.par.show(show,x,y,parentPropagate)
} 
if (o.iframeLyr)
{
releaseBGIFrame(o.iframeLyr.id)
o.iframeLyr=null
o.iframeCss=null
}
css.display='none'
o.nextSub=-1
o.showSub()
if (o.hideCB)
o.hideCB()
o.releaseClicks()
}
}
function MenuWidget_isShown()
{
var o=this
if (o.layer==null)
return false
else
return (o.css.display=='block')
}
function MenuWidget_hasVisibleItem()
{
var o=this
if(o.isMenuColor || o.isCalendar) return true;
var items=o.items
for (var i in items)
{
var item=items[i]
if (item && !(item.isSeparator==true) && item.isShown)
return true;
}
return false
}
function newMenuItem(par,id,text,cb,icon,dx,dy,disabled,disDx,disDy,isCheck,alt)
{
var o=new Object
o.par=par
o.id=id
o.text=text
o.cb=cb
o.icon=icon
o.dx=(dx==null)?0:dx
o.dy=(dy==null)?0:dy
o.disDx=(disDx==null)?o.dx:disDx
o.disDy=(disDy==null)?o.dy:disDy
o.sub=null
o.layer=null
o.iconTDLayer=null
o.iconLayer=null
o.textLayer=null
o.textOnlyLayer=null
o.accel=null
o.accelLayer=null
o.hasNoLayer=false
o.isSeparator=false
o.disabled=(disabled!=null)?disabled:false
o.isShown=true
o.alt=alt 
o.index=_menusItems.length
_menusItems[o.index]=o
o.menuIndex=-1
o.isCheck=isCheck
o.checked=false
o.menuItemType=_isNotColor
o.init=MenuItem_init
o.leftZoneClass=o.par.leftZoneClass
o.leftZoneSelClass=o.par.leftZoneSelClass
o.attachSubMenu=MenuItem_attachSubMenu
o.getHTML=MenuItem_getHTML
o.getHTMLPart=MenuItem_getHTMLPart
o.dynHTML=MenuItem_dynHTML
o.setDisabled=MenuItem_setDisabled
o.check=MenuItem_check
o.isChecked=MenuItem_isChecked
o.show=MenuItem_show
o.setText=MenuItem_setText
o.setIcon=MenuItem_setIcon
o.setAccelerator=MenuItem_setAccelerator
o.focus=MenuItem_focus
return o
}
function MenuItem_init()
{
if (!this.hasNoLayer)
{
var o=this,id=o.par.id
o.layer=getLayer(id+'_item_'+o.id)
o.layer._boIndex=o.index
if (!o.isSeparator)
{
if ((o.icon!=null)||(o.isCheck))
{
o.iconLayer=getLayer(id+'_item_icon_'+o.id)
o.iconTDLayer=getLayer(id+'_item_td_'+o.id)
}
o.textLayer=getLayer(id+'_text_'+o.id)
o.textOnlyLayer=getLayer(id+'_span_text_'+o.id)
o.accelLayer=getLayer(id+'_accel_'+o.id)
if(o.textOnlyLayer) 
{
o.textOnlyLayer.title=o.checked?o.textOnlyLayer.innerText+" "+_menuCheckLab:""
if (o.disabled) o.textOnlyLayer.title +=" "+_menuDisableLab
}
}
if (o.isCheck)
{
o.check(o.checked,true)
}
}
}
function MenuItem_attachSubMenu(menu)
{
var o=this
o.sub=menu
menu.par=o
menu.zIndex=o.par.zIndex+2
if (o.layer)
{
if (o.arrowLayer==null)
o.arrowLayer=getLayer(o.par.id+'_item_arrow_'+o.id)
var dis=o.disabled
changeSimpleOffset(o.arrowLayer,dis?7:0,dis?81:64)
}
return menu
}
function MenuItem_check(check,force)
{
var o=this
if ((o.checked!=check)||force)
{
o.checked=check
if (o.par.layer)
{
var lyr=o.layer
if (lyr)
{
if (o.icon==null)
changeSimpleOffset(o.iconLayer,0,(o.checked?48:0),null,(o.checked?_menuCheckLab:""))
changeOffset(o.iconTDLayer,0,(o.checked?96:0))
if (o.checkFrame==null)
o.checkFrame=getLayer(o.par.id+'_item_check_'+o.id)
o.checkFrame.className=o.checked?o.par.menuIconCheckClass:o.par.menuIconClass
if(o.textOnlyLayer) 
o.textOnlyLayer.title=o.checked?o.textOnlyLayer.innerText+" "+_menuCheckLab:""
}
}
}
}
function MenuItem_setDisabled(dis)
{
var o=this
if (o.disabled!=dis)
{
o.disabled=dis
if (o.par.layer)
{
var lyr=o.layer
if (lyr)
{
lyr.style.cursor=dis?'default':_hand
if (o.icon)
changeSimpleOffset(o.iconLayer,dis?o.disDx:o.dx,dis?o.disDy:o.dy)
var cn=o.disabled?o.par.menuTextPartClassDis:o.par.menuTextPartClass
if (cn!=o.textLayer.className)
o.textLayer.className=cn
if (o.accel && (cn!=o.accelLayer.className))
o.accelLayer.className=cn
if (o.sub)
{
if (o.arrowLayer==null)
o.arrowLayer=getLayer(o.par.id+'_item_arrow_'+o.id)
changeSimpleOffset(o.arrowLayer,dis?7:0,dis?81:64)
}
if(o.textOnlyLayer) 
o.textOnlyLayer.title=o.disabled?o.textOnlyLayer.innerText+" "+_menuDisableLab:""
}
}
}
}
function _mii(lyr,inv)
{
var c=lyr.childNodes,y=0,len=c.length,idx=lyr._boIndex
var o=_menusItems[idx]
if (o.disabled)
inv=0
else
{
if (inv)
{
o.par.nextSub=o.menuIndex
MenuItem_callShowSub(idx,true)
if (o.par.par)
{
if (o.par.par.par)
{
o.par.par.par.nextSub=o.par.par.menuIndex
}
}
}
}
 var realPart=0
 for (var i=0;i<len;i++)
 {
 var ce=c[i]
 if (ce.tagName!=null)
 {
 if (realPart==0)
 ce.className=inv?o.leftZoneSelClass:o.leftZoneClass
 else if (realPart==1)
 ce.className=o.disabled?o.par.menuTextPartClassDis:(inv?o.par.menuTextPartClassSel:o.par.menuTextPartClass)
 else if (o.accel && (realPart==2)) 
 {
 ce.className=o.disabled?o.par.menuTextPartClassDis:(inv?o.par.menuTextPartClassSel:o.par.menuTextPartClass)
 break
 }
 else
 ce.className=inv?o.par.menuRightPartClassSel:o.par.menuRightPartClass
  realPart++
 }
 }
}
function MenuItem_getHTMLPart(part)
{
var o=this
switch(part)
{
case 0: 
var im=null,className=' class="'+(o.checked?o.par.menuIconCheckClass:o.par.menuIconClass)+'"'
if (o.isCheck&&(o.icon==null))
im=simpleImgOffset(_skin+"menus.gif",16,16,0,o.checked?48:0,(o.par.id+'_item_icon_'+o.id),null,(o.checked?_menuCheckLab:""))
else
im=o.icon?simpleImgOffset(o.icon,16,16,o.disabled?o.disDx:o.dx,o.disabled?o.disDy:o.dy,(o.par.id+'_item_icon_'+o.id),null,o.alt?o.alt:''):(getSpace(16,16))
if (o.isCheck)
{
var size=_ie?18:16
im='<div id="'+o.par.id+'_item_check_'+o.id+'" class="'+(o.checked?o.par.menuIconCheckClass:o.par.menuIconClass)+'" style="width:'+size+'px;height:'+size+'px;padding:1px">'+im+'</div>'
}
return im
case 1: 
return '<span id="'+(o.par.id+'_span_text_'+o.id)+'" tabIndex="0">'+convStr(o.text)+'</span>'
case 2:
return simpleImgOffset(_skin+"menus.gif",16,16,o.sub?(o.disabled?7:0):0,o.sub?(o.disabled?81:64):0,o.par.id+'_item_arrow_'+o.id, null, null, null,"right")
case 3:
return '<table width="100%" height="3" cellpadding="0" cellspacing="0" border="0" style="'+backImgOffset(_skin+"menus.gif",0,80)+';"><tbody><tr><td></td></tr></tbody></table>'
case 4:
return convStr(o.accel)
}
}
function MenuItem_getHTML()
{
var o=this
if ((o.icon!=null)||(o.text!=null))
{
var invertCbs=' onclick="'+_codeWinName+'._micl(this,event);return true" oncontextmenu="'+_codeWinName+'._micl(this,event);return false" onmouseover="'+_codeWinName+'._mii(this,1)" onmouseout="'+_codeWinName+'._mii(this,0);" '
var keysCbs=' onkeydown="'+_codeWinName+'._mikd(this,event);return true" '
var ar=new Array(), i=0
ar[i++] = '<tr onmousedown="'+_codeWinName+'._minb(event)" onmouseup="'+_codeWinName+'._minb(event)" id="'+(o.par.id+'_item_'+o.id)+'" style="'+(!o.isShown?'display:none;':'')+'height:'+_mitemH+'px;width:24px;cursor:'+(o.disabled?'default':_hand)+'" '+invertCbs+keysCbs+' valign="middle">'
ar[i++] ='<td id="'+(o.par.id+'_item_td_'+o.id)+'" style="width:23px;height:'+_mitemH+'px;" align="center" class="'+o.leftZoneClass+'">'
ar[i++] =o.getHTMLPart(0)
ar[i++] ='</td>'
ar[i++] ='<td ' +(o.centered?' align="center" ':'')+' style="height:'+_mitemH+'px" id="'+(o.par.id+'_text_'+o.id)+'" class="'+(o.disabled?o.par.menuTextPartClassDis:o.par.menuTextPartClass)+'">'
ar[i++] =o.getHTMLPart(1)
ar[i++] ='</td>'
if (o.accel!=null)
{
ar[i++] = '<td class="'+(o.disabled?o.par.menuTextPartClassDis:o.par.menuTextPartClass)+'" id="'+(o.par.id+'_accel_'+o.id)+'" align="right"' +' style="height:'+_mitemH+'px"  tabIndex="-1">'
ar[i++] =o.getHTMLPart(4)
ar[i++] = '</td>'
} else {
ar[i++] = '<td class="menuRightPart" align="right" style="width:40px;height:'+_mitemH+'px;" >'
ar[i++] =o.getHTMLPart(2)
ar[i++] = '</td>'
}
ar[i++] = '</tr>'
return ar.join('')
}
else
{
return '<tr onmousedown="'+_codeWinName+'._minb(event)" onclick="'+_codeWinName+'._minb(event)" id="'+(o.par.id+'_item_'+o.id)+'" onmouseup="'+_codeWinName+'._minb(event)" style="height:3px">'+
'<td class="'+o.leftZoneClass+'" style="width:24px;height:3px;border:0px"></td>'+
'<td colspan="2" style="padding-left:5px;padding-right:5px;border:0px">'+
o.getHTMLPart(3)+
'</td></tr>'
}
}
function MenuItem_dynHTML()
{
var o=this
if (o.par.layer==null)
return
var tbody=o.par.getMenuTBody()
var tr=tbody.insertRow(o.menuIndex),st=tr.style
tr.onmousedown=_minb
tr.onmouseup=_minb
tr.id=(o.par.id+'_item_'+o.id)
if ((o.icon!=null)||(o.text!=null))
{
var td1=tr.insertCell(0),td2=tr.insertCell(1),td3=tr.insertCell(2),st1=td1.style,st2=td2.style,st3=td3.style
tr.onclick=MenuItem_clickCallTrue
tr.oncontextmenu=MenuItem_clickCallFalse
tr.onmouseover=MenuItem_invertCall1
tr.onmouseout=MenuItem_invertCall0
st.height=""+_mitemH+"px"
st.width="24px"
st.cursor=(o.disabled?'default':_hand)
td1.id=(o.par.id+'_item_td_'+o.id)
st1.width="23px"
st1.height=""+_mitemH+"px"
td1.innerHTML=o.getHTMLPart(0)
td1.align="center"
td1.className=o.leftZoneClass
if (o.centered)
td2.align="center"
st2.height=""+_mitemH+"px"
td2.id=(o.par.id+'_text_'+o.id)
td2.className="menuTextPart"+(o.disabled?'Disabled':'')
td2.innerHTML=o.getHTMLPart(1)
if (o.accel) 
{
td3.className="menuTextPart"+(o.disabled?'Disabled':'')
td3.align="right"
st3.height=""+_mitemH+"px"
td3.innerHTML=o.getHTMLPart(4)
} else {
td3.className="menuRightPart"
td3.align="right"
st3.width="40px"
st3.height=""+_mitemH+"px"
changeOffset(td3,0,0,_skin+"menus.gif")
td3.innerHTML=o.getHTMLPart(2)
}
o.init()
}
else
{
tr.onclick=_minb
tr.style.height="3px"
var td1=tr.insertCell(0),td2=tr.insertCell(1),st1=td1.style,st2=td2.style
td1.className=o.leftZoneClass
st1.width="24px"
st1.height="3px"
st1.border="0px"
td2.colSpan="2"
st2.paddingLeft="5px"
st2.paddingRight="5px"
td2.innerHTML=o.getHTMLPart(3)
}
}
function MenuItem_isChecked()
{
return this.checked
}
function MenuItem_setText(s)
{
var o=this,id=o.par.id
o.text=s
if (o.textLayer)
{
o.textLayer.innerHTML=o.getHTMLPart(1)
o.textOnlyLayer=getLayer(id+'_span_text_'+o.id)
}
}
function MenuItem_setAccelerator(keystroke, modifier)
{
var o=this,id=o.par.id
o.accel= ((modifier != null)?_modifiers[modifier]:"") + keystroke
if (o.accelLayer)
{
o.accelLayer.innerHTML=o.getHTMLPart(4)
}
}
function MenuItem_setIcon(dx,dy,disDx,disDy,url)
{
var o=this
o.url = url ? url : o.url
o.dx = (dx != null) ? dx : o.dx
o.dy = (dy != null) ? dy : o.dy
o.disDx = (disDx != null) ? disDx : o.disDx
o.disDy = (disDy != null) ? disDy : o.disDy
if (o.icon && o.iconLayer)
changeSimpleOffset(o.iconLayer,o.disabled?o.disDx:o.dx, o.disabled?o.disDy:o.dy,o.url)
}
function MenuItem_show(sh)
{
var o=this
o.isShown=sh
if (o.layer!=null)
o.layer.style.display=sh?'':'none'
}
function _micl(lyr,e)
{
eventCancelBubble(e)
var idx=lyr._boIndex,o=_menusItems[idx]
o.layer=lyr
if (!o.disabled)
{
if (o.sub)
{
o.par.nextSub=o.menuIndex
MenuItem_callShowSub(idx)
}
else
{
o.par.show(false,0,0,true)
if (o.isCheck)
{
if (o.par.uncheckAll)
o.par.uncheckAll()
o.check(!o.checked)
}
var m = o.par.container
if (m && m.updateButton)
{
if (typeof(m.autoUpdateMenuIconFromItemIcon) != 'undefined' && m.autoUpdateMenuIconFromItemIcon)
m.updateButton(o.dx/16)
}
_mii(lyr,0,idx)
o.par.nextSub=-1
if (o.cb)
setTimeout("MenuItem_delayedClick("+idx+")",1)
}
}
}
function _mikd(lyr,e)
{
var idx=lyr._boIndex,o=_menusItems[idx]
o.layer=lyr
var k=eventGetKey(e)
switch(k)
{
case 13:
_micl(lyr,e)
break;
case 107:
case 39:
if (!o.disabled && o.sub )
{
_micl(lyr,e)
}
break;
case 109:
case 37:
break;
case 40:
var items=o.par.items, len = items.length
for(var i=o.menuIndex+1;i<len;i++)
{
if(items[i].isShown && !items[i].isSeparator)
{
items[i].focus()
break;
}
}
break;
case 38:
var items=o.par.items, len = items.length
for(var i=o.menuIndex-1;i>=0;i--)
{
if(items[i].isShown && !items[i].isSeparator)
{
items[i].focus()
break;
}
}
break;
}
}
function MenuItem_callShowSub(idx,delayed)
{
var o=_menusItems[idx]
if (delayed)
setTimeout('MenuItem_delayedShowSub('+idx+')',500)
else
MenuItem_delayedShowSub(idx)
}
function MenuItem_delayedShowSub(idx)
{
var o=_menusItems[idx]
o.par.showSub()
}
function _minb(e)
{
eventCancelBubble(e)
}
function MenuItem_delayedClick(idx)
{
var item=_menusItems[idx]
if (item.cb)
item.cb()
}
function MenuItem_clickCallTrue(event)
{
_micl(this,event)
return true
}
function MenuItem_clickCallFalse(event)
{
_micl(this,event)
return false
}
function MenuItem_invertCall0(event)
{
_mii(this,0)
}
function MenuItem_invertCall1(event)
{
_mii(this,1)
}
function MenuItem_focus()
{
var o=this
if(isLayerDisplayed(o.layer) && o.textOnlyLayer && o.textOnlyLayer.focus)
{
safeSetFocus(o.textOnlyLayer)
}
}
function newMenuColorWidget(id,hideCB)
{
var o=newMenuWidget(id,hideCB)
o.addSeparator=null
o.lastUsedTxt=""
o.lastUsedColorsAr=null
o.addColor=MenuColorWidget_addColor
o.addLastUsed=MenuColorWidget_addLastUsed
o.getHTML=MenuColorWidget_getHTML
o.uncheckAll=MenuColorWidget_uncheckAll
o.isMenuColor=true
return o
}
function MenuColorWidget_addColor(tooltip,color,cb)
{
var o=this,i=o.items.length
var ret=o.items[i]=newColorMenuItem(o,color,tooltip,cb)
ret.menuIndex=i
return ret
}
function MenuColorWidget_addLastUsed(text,lastUsedColorsAr,cb, beforeShowCB)
{
var o=this
o.lastUsedTxt = text
o.lastUsedColorsAr = lastUsedColorsAr
o.beforeShowCB = MenuColorWidget_beforeShowCB
colorsMax = 8
len = o.items.length
var it = null
for (var c = 0; c < colorsMax; c++)
{
it = newLastUsedColorMenuItem(o,c,lastUsedColorsAr[c],"",cb)
it.isLast = (c == colorsMax-1) ? true : false 
o.items[len + c] = it
}
}
function MenuColorWidget_getHTML()
{
var o=this,items=o.items
var j = 0
var keysCbs=' onkeydown="'+_codeWinName+'.MenuWidget_keyDown(\''+o.id+'\',event);return true" '
var s = new Array
s[j++] = '<a style="position:absolute;left:-30px;top:-30px; visibility:hidden" id="startLink_'+o.id+'" href="javascript:void(0)" onfocus="'+_codeWinName+'.MenuWidget_keepFocus(\''+o.id+'\');return false;" ></a>'
s[j++] = '<table style="display:none;" class="menuFrame" id="'+o.id+'" cellspacing="0" cellpadding="0" border="0"'+keysCbs+'><tbody>'
var sep = '<tr style="height:3px"><td colspan="8" style="padding-left:5px;padding-right:5px;"><table width="100%" height="3" cellpadding="0" cellspacing="0" border="0" style="'+backImgOffset(_skin+"menus.gif",0,80)+';"><tbody><tr><td></td></tr></tbody></table></td></tr>'
var len = items.length
lastUsedCol=""
lastUsedColIconsNb = 0
lastUsedColIconsMaxLine = 3
for (var i in items)
{
var item=items[i]
switch (item.menuItemType)
{
case _isColor:
s[j++] = item.getHTML()
break;
case _isLastUsedColor:
lastUsedCol += item.getHTML()
lastUsedCol += (lastUsedColIconsNb++ == lastUsedColIconsMaxLine)? "</tr><tr>":""
if (item.isLast)
{
s[j++] = sep
s[j++] = '<tr><td colspan="8">'
s[j++] ='<table border="0" cellspacing="0" cellpadding="0" width="100%"><tbody><tr>'
s[j++] ='<td width="50%" class="menuTextPart">' + convStr(o.lastUsedTxt) + '</td>'
s[j++] ='<td><table border="0" cellspacing="0" cellpadding="0"><tbody><tr>'
s[j++] =lastUsedCol
s[j++] ='</tr></tbody></table></td>'
s[j++] ='</tr></tbody></table>'
s[j++] = '</td></tr>'
s[j++] = sep
}
break;
case _isNotColor:
item.leftZoneClass="menuLeftPartColor"
item.leftZoneSelClass="menuLeftPartSel"
item.centered=true
s[j++] ='<tr><td colspan="8"><table border="0" cellspacing="0" cellpadding="0" width="100%"><tbody><tr>'+item.getHTML()+'</tr></tbody></table></td></tr>'
s[j++] = (i == 0 )? sep:""
}
}
s[j++] ='</tbody></table><a style="position:absolute;left:-30px;top:-30px; visibility:hidden" id="endLink_'+o.id+'" href="javascript:void(0)" onfocus="'+_codeWinName+'.MenuWidget_keepFocus(\''+o.id+'\');return false;" ></a>'
return s.join("")
}
function MenuColorWidget_beforeShowCB()
{
var o=this, j=0
lenLastUsed = o.lastUsedColorsAr.length
if ((lenLastUsed == 1) && ((o.lastUsedColorsAr[0].length == 0) || (o.lastUsedColorsAr[0] == "null"))) {
lenLastUsed = 0
o.lastUsedColorsAr.length = 0
}
for (var i in o.items)
{
var item=o.items[i]
if (item.menuItemType == _isLastUsedColor) 
{
if (j < lenLastUsed)
{
item.init()
var c = o.lastUsedColorsAr[j++]
item.color = c
item.layer.childNodes[0].childNodes[0].style.backgroundColor = 'rgb(' + c + ')'
var t = _colorsArr[""+c+""]
item.text = (t)? t:(_RGBTxtBegin + c + _RGBTxtEnd)
item.layer.childNodes[0].childNodes[0].childNodes[0].title = item.text
item.show(true)
} else {
item.show(false)
}
}
else
{
if (item.menuItemType!=_isNotColor)
{
if (item.layer==null)
item.init();
item.check(item.checked,true)
}
} 
}
}
function MenuColor_invert(lyr,inv)
{
var o=_menusItems[lyr._boIndex]
if (o && o.checked)
inv=1
var border=lyr.childNodes[0],newClassName="menuColor"+(inv?"sel":"")
if (border.className!=newClassName)
border.className=newClassName
}
function MenuColor_out()
{
MenuColor_invert(this,0);
}
function _Mcov(l)
{
l.onmouseout=MenuColor_out
MenuColor_invert(l,1);
}
function MenuColorWidget_uncheckAll()
{
var o=this,items=o.items
for (var i in items)
{
var item=items[i]
if (item.checked)
item.check(false)
}
}
function _mcc(lyr,e)
{
eventCancelBubble(e)
var idx=lyr._boIndex,o=_menusItems[idx]
o.par.uncheckAll()
MenuColor_invert(lyr,1,idx)
o.checked=true
o.par.show(false,0,0,true)
if (o.cb)
setTimeout("MenuItem_delayedClick("+idx+")",1)
}
function newColorMenuItem(par,color,text,cb)
{
var o=newMenuItem(par,"color_"+color,text,cb)
o.color=color
o.attachSubMenu=null
o.getHTML=ColorMenuItem_getHTML
o.check=ColorMenuItem_check
o.menuItemType=_isColor
return o
}
function ColorMenuItem_check(check,force)
{
var o=this
if (force||(o.checked!=check))
{
o.checked=check
if (o.layer)
MenuColor_invert(o.layer,o.checked?1:0)
}
}
function ColorMenuItem_getHTML()
{
var o=this,s="",d=_moz?10:12,lenTotal=o.par.items.length,index=o.menuIndex - 1;col=index%8
var len=0
for (var i = 0; i <lenTotal; i++)
{
if (o.par.items[i].menuItemType == _isColor) len++
}
var first=(col==0)
var last=(col==7)
var firstL=(index<8)
var lastL=(index>=(Math.floor((len-1)/8)*8))
var cbs=' onclick="'+_codeWinName+'._mcc(this,event);return true" oncontextmenu="'+_codeWinName+'._mcc(this,event);return false" onmousedown="'+_codeWinName+'._minb(event)" onmouseup="'+_codeWinName+'._minb(event)" onmouseover="'+_codeWinName+'._Mcov(this)" '
if (first)
s+='<tr valign="middle" align="center">'
s+='<td id="'+(o.par.id+'_item_'+o.id)+'" '+cbs+' style="padding-top:'+(firstL?2:0)+'px;padding-bottom:'+(lastL?2:0)+'px;padding-left:'+(first?3:1)+'px;padding-right:'+(last?3:1)+'px"><div class="menuColor'+(o.checked?'Sel':'')+'"><div style="cursor:'+_hand+';border:1px solid #4A657B;width:'+d+'px;height:'+d+'px;background-color:rgb('+o.color+');">'+img(_skin+'../transp.gif',10,10,null,null,o.text)+'</div></div></td>'
if (last)
s+='</tr>'
return s
}
function newLastUsedColorMenuItem(par,idx,color,text,cb)
{
var o=newMenuItem(par,"color_"+idx,text,cb)
o.idx=idx
o.color=color
o.menuItemType = _isLastUsedColor
o.attachSubMenu=null
o.check=ColorMenuItem_check
o.getHTML=LastUsedColorMenuItem_getHTML
o.init=LastUsedColorMenuItem_init
return o
}
function LastUsedColorMenuItem_getHTML()
{
var o=this,s="",d=_moz?10:12
var cbs=' onclick="'+_codeWinName+'._mcc(this,event);return true" oncontextmenu="'+_codeWinName+'._mcc(this,event);return false" onmousedown="'+_codeWinName+'._minb(event)" onmouseup="'+_codeWinName+'._minb(event)" onmouseover="'+_codeWinName+'._Mcov(this)" '
s+='<td id="'+(o.par.id+'_item_'+o.id)+'" width="18" '+cbs+' style="padding-top:0px;padding-bottom:0px;padding-left:1px;padding-right:1px"><div class="menuColor'+(o.checked?'sel':'')+'"><div style="cursor:'+_hand+';border:1px solid #4A657B;width:'+d+'px;height:'+d+'px;background-color:rgb('+o.color+');">'+img(_skin+'../transp.gif',10,10,null,null,o.text)+'</div></div></td>'
return s
}
function LastUsedColorMenuItem_init()
{
if (!this.hasNoLayer)
{
var o=this,id=o.par.id
o.layer=getLayer(id+'_item_'+o.id)
o.layer._boIndex=o.index
if (o.isCheck)
{
o.check(o.checked,true)
}
}
}
function newScrollMenuWidget(id,changeCB,multi,width,lines,tooltip,dblClickCB,keyUpCB,
showLabel,label,convBlanks,beforeShowCB)
{
var o=newWidget(id)
o.list=newListWidget("list_"+id,ScrollMenuWidget_changeCB,multi,width,lines,tooltip,ScrollMenuWidget_dblClickCB,ScrollMenuWidget_keyUpCB)
o.list.par=o
o.label=NewLabelWidget("label_"+id,label,convBlanks)
o.showLabel=showLabel
o.changeCB=changeCB
o.dblClickCB=dblClickCB
o.keyUpCB=keyUpCB
o.beforeShowCB=beforeShowCB
o.zIndex=_menusZIndex
o.init=ScrollMenuWidget_init
o.justInTimeInit=ScrollMenuWidget_justInTimeInit
o.setDisabled=ScrollMenuWidget_setDisabled
o.write=ScrollMenuWidget_write
o.getHTML=ScrollMenuWidget_getHTML
o.show=ScrollMenuWidget_show
o.add=ScrollMenuWidget_add
o.del=ScrollMenuWidget_del
o.getSelection=ScrollMenuWidget_getSelection
o.select=ScrollMenuWidget_select
o.valueSelect=ScrollMenuWidget_valueSelect
o.getCount=ScrollMenuWidget_getCount
o.isShown=MenuWidget_isShown
o.captureClicks=MenuWidget_captureClicks
o.releaseClicks=MenuWidget_releaseClicks
o.clickCB=new Array
o.clickCBDocs=new Array
return o
}
function ScrollMenuWidget_init()
{
}
function ScrollMenuWidget_justInTimeInit()
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
o.list.init()
o.label.init()
}
function ScrollMenuWidget_setDisabled()
{
}
function ScrollMenuWidget_write()
{
}
function ScrollMenuWidget_getHTML()
{
var o=this
var s=''
s+='<table onmousedown="event.cancelBubble=true" id="'+o.id+'" style="display:none;" class="menuFrame" cellspacing="0" cellpadding="0" border="0"><tbody>'
s+='<tr><td align="center">'+o.list.getHTML()+'</td></tr>'
s+='<tr><td align="center">'+o.label.getHTML()+'</td></tr>'
s+='</tbody></table>'
return s
}
function ScrollMenuWidget_show(show,x,y)
{
var o=this
if (o.layer==null)
o.justInTimeInit()
var css=o.css
if (show)
{
if (o.beforeShowCB)
o.beforeShowCB()
o.captureClicks()
css.display='block'
css.zIndex=(o.zIndex+1)
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
if (o.iframeLyr==null)
{
o.iframeLyr=getDynamicBGIFrameLayer()
o.iframeCss=o.iframeLyr.style
}
iCss=o.iframeCss
iCss.left=""+x+"px"
iCss.top=""+y+"px"
iCss.width=""+w+"px"
iCss.height=""+h+"px"
iCss.zIndex=o.zIndex-1
iCss.display='block'
if (_ie)
{
y-=2
x-=2
}
}
else
{
if (o.iframeLyr)
{
releaseBGIFrame(o.iframeLyr.id)
o.iframeLyr=null
o.iframeCss=null
}
css.display='none'
iCss.display='none'
o.releaseClicks()
}
}
function ScrollMenuWidget_add(s,val,sel,id)
{
var o=this
if (o.layer==null)
o.justInTimeInit()
o.list.add(s,val,sel,id)
}
function ScrollMenuWidget_del(i)
{
var o=this
if (o.layer==null)
o.justInTimeInit()
o.list.del(i)
}
function ScrollMenuWidget_getSelection()
{
var o=this
if (o.layer==null)
o.justInTimeInit()
return o.list.getSelection()
}
function ScrollMenuWidget_select(i)
{
var o=this
if (o.layer==null)
o.justInTimeInit()
o.list.select(i)
}
function ScrollMenuWidget_valueSelect(v)
{
var o=this
if (o.layer==null)
o.justInTimeInit()
o.list.valueSelect(v)
}
function ScrollMenuWidget_getCount()
{
var o=this
if (o.layer==null)
o.justInTimeInit()
return o.list.getCount()
}
function ScrollMenuWidget_changeCB()
{
var o=this
o.par.show(false)
if (o.par.changeCB)
o.par.changeCB()
}
function ScrollMenuWidget_dblClickCB()
{
var o=this
o.par.show(false)
if (o.par.dblClickCB)
o.par.dblClickCB()
}
function ScrollMenuWidget_keyUpCB()
{
var o=this
o.par.show(false)
if (o.par.keyUpCB)
o.par.keyUpCB()
}
function newButtonScrollMenuWidget(id,label,buttonWidth,buttonTooltip,beforeShowCB,tabIndex,
            changeCB,multi,menuWidth,lines,menuTooltip,dblClickCB,keyUpCB,showMenuLabel,menuLabel,convBlanks,beforeShowCB)
{
var o=newButtonWidget(id,label,ButtonScrollMenuWidget_clickCB,buttonWidth,null,buttonTooltip,tabIndex,0,_skin+"menus.gif",7,16,0,81,true,0,97)
o.menu=newScrollMenuWidget("scrollMenu_menu_"+id,changeCB,multi,menuWidth,lines,menuTooltip,dblClickCB,keyUpCB,showMenuLabel,menuLabel,convBlanks,beforeShowCB)
o.getMenu=IconMenuWidget_getMenu
o.add = ButtonScrollMenuWidget_add
return o;
}
function ButtonScrollMenuWidget_clickCB()
{
var o=this,l=o.layer;
o.menu.show(!o.menu.isShown(),getPosScrolled(l).x,getPosScrolled(l).y+o.getHeight(),null,null,o)
}
function ButtonScrollMenuWidget_add(s,val,sel,id)
{
this.menu.add(s,val,sel,id)
}
function newBorderMenuItem(par,idx,cb,isLabel,label)
{
var o=newMenuItem(par,"border_"+idx,null,cb)
o.idx=idx
o.isLabel=isLabel?isLabel:false
o.label=label?label:null
o.attachSubMenu=null
o.getHTML=BorderMenuItem_getHTML
o.check=BorderMenuItem_check
o.menuItemType=_isNotColor
return o
}
function BorderMenuItem_check(check)
{
var o=this
if (o.checked!=check)
{
o.checked=check
if (o.layer)
BorderMenuItem_invert(o.layer,o.checked?1:0)
}
}
function BorderMenuItem_getHTML()
{
var o=this,s="",d=_moz?10:12,lenTotal=o.par.items.length,index=o.menuIndex - 1;col=index%8
var cbs=' onclick="'+_codeWinName+'.MenuBordersWidget_onclickCB(this,event);return true" oncontextmenu="'+_codeWinName+'.MenuBordersWidget_onclickCB(this,event);return false" onmousedown="'+_codeWinName+'._minb(event)" onmouseup="'+_codeWinName+'._minb(event)" onmouseover="'+_codeWinName+'.MenuBordersWidget_onmouseOverCB(this)" '
var cspan=(o.isLabel?' colspan="4"':'')
var cls="menuiconborders"+(o.checked?"Sel":"")
s+='<td '+cspan+' id="'+(o.par.id+'_item_'+o.id)+'" '+cbs+' align="center"><div class="'+cls+'">'
s+=o.isLabel?convStr(o.label):simpleImgOffset(_skin+'../borders.gif',16,16,16*o.idx,0,'IconImg_'+o.id,null,_bordersTooltip[o.idx],'margin:2px;cursor:default')
s+='</div></td>'
return s
}
function BorderMenuItem_invert(lyr,inv)
{
var o=_menusItems[lyr._boIndex]
if (o && o.checked)
inv=1
lyr.childNodes[0].className="menuiconborders"+(inv?"Sel":"")
}
function newMenuBordersWidget(id,hideCB,beforeShowCB,clickCB)
{
var o=newMenuWidget(id,hideCB,beforeShowCB)
o.items=new Array
for (var i=0; i < 12; i++)
o.items[i]=newBorderMenuItem(o,i,clickCB)
var len=o.items.length
o.items[len]=newBorderMenuItem(o,12,clickCB,true,_bordersMoreColorsLabel)
o.clickCB=clickCB
o.getHTML=MenuBordersWidget_getHTML
o.hasVisibleItem=MenuBordersWidget_hasVisibleItem
o.uncheckAll=MenuBordersWidget_uncheckAll
return o
}
function MenuBordersWidget_getHTML()
{
var o=this,items=o.items
var keysCbs=' onkeydown="'+_codeWinName+'.MenuWidget_keyDown(\''+o.id+'\',event);return true" '
var s='<a style="position:absolute;left:-30px;top:-30px; visibility:hidden" id="startLink_'+o.id+'" href="javascript:void(0)" onfocus="'+_codeWinName+'.MenuWidget_keepFocus(\''+o.id+'\');return false;" ></a>'
s+='<table style="display:none;" class="menuFrame" id="'+o.id+'" cellspacing="0" cellpadding="0" border="0" '+keysCbs+'><tbody>'
s+='<tr>'
for (var i=0; i<=3; i++)
s+=items[i].getHTML()
s+='</tr>'
s+='<tr>'
for (var i=4; i<=7; i++)
s+=items[i].getHTML()
s+='</tr>'
s+='<tr>'
for (var i=8; i<=11; i++)
s+=items[i].getHTML()
s+='</tr>'
s+='<tr>'+items[12].getHTML()+'</tr>'
s+='</tbody></table><a style="position:absolute;left:-30px;top:-30px; visibility:hidden" id="endLink_'+o.id+'" href="javascript:void(0)" onfocus="'+_codeWinName+'.MenuWidget_keepFocus(\''+o.id+'\');return false;" ></a>'
return s
}
function MenuBordersWidget_hasVisibleItem()
{
return true
}
function MenuBordersWidget_uncheckAll()
{
var o=this,items=o.items
for (var i in items)
{
var item=items[i]
if (item.checked)
item.check(false)
}
}
function MenuBordersWidget_onclickCB(lyr,e)
{
eventCancelBubble(e)
var idx=lyr._boIndex,o=_menusItems[idx]
o.par.uncheckAll()
BorderMenuItem_invert(lyr,1,idx)
o.checked=true
o.par.show(false,0,0,true)
if (o.cb)
setTimeout("MenuItem_delayedClick("+idx+")",1)
}
function MenuBordersWidget_out()
{
BorderMenuItem_invert(this,0);
}
function MenuBordersWidget_onmouseOverCB(l)
{
l.onmouseout=MenuBordersWidget_out
BorderMenuItem_invert(l,1)
}

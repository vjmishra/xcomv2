_menusZIndex=2000
_menusItems=new Array
_globMenuCaptured=null
_isColor=0
_isLastUsedColor=1
_isNotColor=2
function newMenuWidget(id,hideCB,beforeShowCB)
{
var o=newWidget(id)
o.shadowLyr=null
o.items=new Array
o.par=null
o.currentSub=-1
o.nextSub=-1
o.zIndex=_menusZIndex
o.hideCB=hideCB
o.beforeShowCB=beforeShowCB
o.init=MenuWidget_init
o.justInTimeInit=MenuWidget_justInTimeInit
o.getHTML=MenuWidget_getHTML
o.getShadowHTML=MenuWidget_getShadowHTML
o.show=MenuWidget_show
o.internalAdd=o.add=MenuWidget_add
o.addCheck=MenuWidget_addCheck
o.addSeparator=MenuWidget_addSeparator
o.getItem=MenuWidget_getItem
o.isShown=MenuWidget_isShown
o.showSub=MenuWidget_showSub
o.captureClicks=MenuWidget_captureClicks
o.releaseClicks=MenuWidget_releaseClicks
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
w=_curWin
}
var d=w.document
o.clickCB[o.clickCB.length]=d.onmouseup
o.clickCBDocs[o.clickCBDocs.length]=d
d.onmouseup=MenuWidget_globalClick
var fr=w.frames,len=fr.length
for (var i=0;i<len;i++)
o.captureClicks(fr[i])
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
o.clickCBDocs[i].onmouseup=o.clickCB[i]
o.clickCB[i]=null
o.clickCBDocs[i]=null
}
o.clickCB.length=0
o.clickCBDocs.length=0
}
}
function MenuWidget_globalClick()
{
var o=_globMenuCaptured
if (o!=null)
{
_globMenuCaptured=null
o.releaseClicks()
o.show(false)
}
}
function MenuWidget_add(id,text,cb,icon,dx,dy,disabled,disDx,disDy)
{
var o=this,i=o.items.length
var ret=o.items[i]=newMenuItem(o,id,text,cb,icon,dx,dy,disabled,disDx,disDy,false)
ret.menuIndex=i
ret.dynHTML()
return ret
}
function MenuWidget_addCheck(id,text,cb,icon,dx,dy,disabled,disDx,disDy)
{
var o=this,i=o.items.length
var ret=o.items[i]=newMenuItem(o,id,text,cb,icon,dx,dy,disabled,disDx,disDy,true)
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
y+=24
else
y+=3
}
}
var w=o.getWidth()
x=x+w-4
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
o.shadowLyr=getLayer("menuShadow_"+o.id)
o.shadowCss=o.shadowLyr.style
var items=o.items
for (var i in items)
items[i].init()
}
function MenuWidget_getShadowHTML()
{
return '<div style="display:none;filter:progid:DXImageTransform.Microsoft.Blur(PixelRadius=\'2\', MakeShadow=\'false\', ShadowOpacity=\'0.75\')" class="menuShadow" id="menuShadow_'+this.id+'"></div>'
}
function MenuWidget_getHTML()
{
var o=this,items=o.items
var s=o.getShadowHTML()+'<table style="display:none;" class="menuFrame" id="'+o.id+'" cellspacing="0" cellpadding="0" border="0"><tbody>'
for (var i in items)
s+=items[i].getHTML()
s+='</tbody></table></div>'
return s
}
function MenuWidget_show(show,x,y,parentPropagate,parentMenuW)
{
var o=this
if (o.layer==null)
o.justInTimeInit()
var css=o.css,sCss=o.shadowCss
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
x=Math.max(0,x-4-(w+((parentMenuW!=null)?parentMenuW-12:0)))
if (y2>winHeight())
y=Math.max(0,y-4-h+(parentMenuW!=null?30:0))
css.left=""+x+"px"
css.top=""+y+"px"
hideAllInputs(x,y,w+4,h+4)
css.visibility="visible"
if (_ie)
{
y-=2
x-=2
}
sCss.left=""+(x+2)+"px"
sCss.top=""+(y+2)+"px"
sCss.width=""+w+"px"
sCss.height=""+h+"px"
sCss.zIndex=o.zIndex
sCss.display='block'
o.nextSub=-1
o.showSub()
}
else
{
if (parentPropagate&&o.par)
{
if (o.par.par)
o.par.par.show(show,x,y,parentPropagate)
}
sCss.display='none'
css.display='none'
o.nextSub=-1
o.showSub()
if (o.hideCB)
o.hideCB()
o.releaseClicks()
restoreAllInputs()
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
function newMenuItem(par,id,text,cb,icon,dx,dy,disabled,disDx,disDy,isCheck)
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
o.hasNoLayer=false
o.isSeparator=false
o.disabled=(disabled!=null)?disabled:false
o.isShown=true
o.index=_menusItems.length
_menusItems[o.index]=o
o.menuIndex=-1
o.isCheck=isCheck
o.checked=false
o.menuItemType=_isNotColor
o.init=MenuItem_init
o.attachSubMenu=MenuItem_attachSubMenu
o.getHTML=MenuItem_getHTML
o.getHTMLPart=MenuItem_getHTMLPart
o.dynHTML=MenuItem_dynHTML
o.setDisabled=MenuItem_setDisabled
o.check=MenuItem_check
o.isChecked=MenuItem_isChecked
o.show=MenuItem_show
o.setText=MenuItem_setText
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
}
}
}
function MenuItem_attachSubMenu(menu)
{
var o=this
o.sub=menu
menu.par=o
menu.zIndex=o.par.zIndex+2
return menu
}
function MenuItem_check(check)
{
var o=this
if (o.checked!=check)
{
o.checked=check
if (o.par.layer)
{
var lyr=o.layer
if (lyr)
{
if (o.icon==null)
changeOffset(o.iconLayer,0,(o.checked?144:0))
changeOffset(o.iconTDLayer,0,(o.checked?96:0))
}
}
}
}
function MenuItem_isChecked()
{
return this.checked
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
if (o.icon)
changeOffset(o.iconLayer,dis?o.disDx:o.dx,dis?o.disDy:o.dy)
o.textLayer.className='menuTextPart'+(o.disabled?'Disabled':'')
if (o.sub)
{
if (o.arrowLayer==null)
o.arrowLayer=getLayer(o.par.id+'_item_arrow_'+o.id)
changeOffset(o.arrowLayer,dis?7:0,dis?179:160)
}
}
}
}
}
function MenuItem_setText(s)
{
var o=this
o.text=s
o.textLayer.innerHTML=convStr(o.text)
}
function MenuItem_show(sh)
{
var o=this
o.isShown=sh
if (o.layer!=null)
o.layer.style.display=sh?'':'none'
}
function MenuItem_invert(lyr,inv)
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
 var isLeft=true
 for (var i=0;i<len;i++)
 {
 var ce=c[i]
 if (ce.tagName!=null)
 {
 if (inv)
 y+=24
 changeOffset(ce,0,y+((o.checked&&isLeft)?96:0))
 isLeft=false
 }
 }
}
function MenuItem_click(lyr,e)
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
MenuItem_invert(lyr,0,idx)
o.par.nextSub=-1
if (o.cb)
setTimeout("MenuItem_delayedClick("+idx+")",1)
}
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
function MenuItem_noBubble(e)
{
eventCancelBubble(e)
}
function MenuItem_delayedClick(idx)
{
var item=_menusItems[idx]
if (item.cb)
item.cb()
}
function MenuItem_getHTMLPart(part)
{
var o=this
switch(part)
{
case 0:
var im=null
if (o.isCheck&&(o.icon==null))
im=imgOffset(_skin+"menus.gif",16,16,0,o.checked?144:0,(o.par.id+'_item_icon_'+o.id))
else
im=o.icon?imgOffset(o.icon,16,16,o.disabled?o.disDx:o.dx,o.disabled?o.disDy:o.dy,(o.par.id+'_item_icon_'+o.id)):(_saf?getSpace(16,24):'')
return im
case 1:
var div1=_saf?'<div style="height:19px;padding-top:5px">':'',div2=_saf?'</div>':''
return div1+convStr(o.text)+div2
case 2:
return imgOffset(_skin+"menus.gif",16,16,0,o.sub?160:0,o.par.id+'_item_arrow_'+o.id)
case 3:
return '<table width="100%" height="3" cellpadding="0" cellspacing="0" border="0" style="'+backImgOffset(_skin+"menus.gif",0,176)+';"><tbody><tr><td></td></tr></tbody></table>'
}
}
function MenuItem_getHTML()
{
var o=this
if ((o.icon!=null)||(o.text!=null))
{
var invertCbs=' onclick="'+_codeWinName+'.MenuItem_click(this,event);return true" oncontextmenu="'+_codeWinName+'.MenuItem_click(this,event);return false" onmouseover="'+_codeWinName+'.MenuItem_invert(this,1)" onmouseout="'+_codeWinName+'.MenuItem_invert(this,0);" '
return '<tr onmousedown="'+_codeWinName+'.MenuItem_noBubble(event)" onmouseup="'+_codeWinName+'.MenuItem_noBubble(event)" id="'+(o.par.id+'_item_'+o.id)+'" style="'+(!o.isShown?'display:none;':'')+'height:24px;width:24px;cursor:'+(o.disabled?'default':_hand)+'" '+invertCbs+' valign="middle" align="left">'+
'<td id="'+(o.par.id+'_item_td_'+o.id)+'" style="width:24px;height:24px;'+backImgOffset(_skin+"menus.gif",0,o.checked?96:0)+'" align="center" class="menuLeftPart">'+
o.getHTMLPart(0)+
'</td>'+
'<td '+(o.centered?' align="center" ':'')+' style="height:24px;'+backImgOffset(_skin+"menus.gif",0,0)+'" id="'+(o.par.id+'_text_'+o.id)+'" class="menuTextPart'+(o.disabled?'Disabled':'')+'">'+
o.getHTMLPart(1)+
'</td>'+
'<td align="center" style="width:24px;height:24px;'+backImgOffset(_skin+"menus.gif",0,0)+'">'+
o.getHTMLPart(2)+
'</td>'+
'</tr>'
}
else
{
return '<tr onmousedown="'+_codeWinName+'.MenuItem_noBubble(event)" onclick="'+_codeWinName+'.MenuItem_noBubble(event)" id="'+(o.par.id+'_item_'+o.id)+'" onmouseup="'+_codeWinName+'.MenuItem_noBubble(event)" style="height:3px">'+
'<td class="menuLeftPart" style="width:24px;height:3px"></td>'+
'<td colspan="2" style="padding-left:5px;padding-right:5px;">'+
o.getHTMLPart(3)+
'</td></tr>'
}
}
function MenuItem_clickCallTrue(event)
{
MenuItem_click(this,event)
return true
}
function MenuItem_clickCallFalse(event)
{
MenuItem_click(this,event)
return false
}
function MenuItem_invertCall0(event)
{
MenuItem_invert(this,0)
}
function MenuItem_invertCall1(event)
{
MenuItem_invert(this,1)
}
function MenuItem_dynHTML()
{
var o=this
if (o.par.layer==null)
return
var tbody=o.par.layer.childNodes[0],tr=tbody.insertRow(tbody.rows.length),st=tr.style
tr.onmousedown=MenuItem_noBubble
tr.onmouseup=MenuItem_noBubble
tr.id=(o.par.id+'_item_'+o.id)
if ((o.icon!=null)||(o.text!=null))
{
var td1=tr.insertCell(0),td2=tr.insertCell(1),td3=tr.insertCell(2),st1=td1.style,st2=td2.style,st3=td3.style
tr.onclick=MenuItem_clickCallTrue
tr.oncontextmenu=MenuItem_clickCallFalse
tr.onmouseover=MenuItem_invertCall1
tr.onmouseout=MenuItem_invertCall0
st.height="24px"
st.width="24px"
st.cursor=(o.disabled?'default':_hand)
td1.id=(o.par.id+'_item_td_'+o.id)
st1.width="24px"
st1.height="24px"
changeOffset(td1,0,o.checked?96:0,_skin+"menus.gif")
td1.innerHTML=o.getHTMLPart(0)
td1.align="center"
td1.className="menuLeftPart"
if (o.centered)
td2.align="center"
st2.height="24px"
changeOffset(td2,0,0,_skin+"menus.gif")
td2.id=(o.par.id+'_text_'+o.id)
td2.className="menuTextPart"+(o.disabled?'Disabled':'')
td2.innerHTML=o.getHTMLPart(1)
td3.align="center"
st3.width="24px"
st3.height="24px"
changeOffset(td3,0,0,_skin+"menus.gif")
td3.innerHTML=o.getHTMLPart(2)
o.init()
}
else
{
tr.onclick=MenuItem_noBubble
tr.style.height="3px"
var td1=tr.insertCell(0),td2=tr.insertCell(1),st1=td1.style,st2=td2.style
td1.className="menuLeftPart"
st1.width="24px"
st1.height="3px"
td2.colSpan="2"
st2.paddingLeft="5px"
st2.paddingRight="5px"
td2.innerHTML=o.getHTMLPart(3)
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
it = newLastUsedColorMenuItem(o,c,"",cb)
it.isLast = (c == colorsMax-1) ? true : false 
o.items[len + c] = it
}
}
function MenuColorWidget_getHTML()
{
var o=this,items=o.items
var j = 0
var s = new Array
s[j++] = '<div style="display:none;filter:progid:DXImageTransform.Microsoft.Blur(PixelRadius=\'2\', MakeShadow=\'false\', ShadowOpacity=\'0.75\')" class="menuShadow" id="menuShadow_'+o.id+'"></div>' 
s[j++] = '<table style="display:none;" class="menuFrame" id="'+o.id+'" cellspacing="0" cellpadding="0" border="0"><tbody>'
var sep = '<tr class="menuLeftPart" style="height:3px"><td colspan="8" style="padding-left:5px;padding-right:5px;"><table width="100%" height="3" cellpadding="0" cellspacing="0" border="0" style="'+backImgOffset(_skin+"menus.gif",0,176)+';"><tbody><tr><td></td></tr></tbody></table></td></tr>'
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
s[j++] = '<tr class="menuLeftPart"><td colspan="8">'
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
item.centered=true
s[j++] ='<tr><td colspan="8" class="menuLeftPart"><table border="0" cellspacing="0" cellpadding="0" width="100%"><tbody><tr>'+item.getHTML()+'</tr></tbody></table></td></tr>'
s[j++] = (i == 0 )? sep:""
}
}
s[j++] ='</tbody></table></div>'
return s.join("")
}
function MenuColorWidget_beforeShowCB()
{
var o=this, j=0
lenLastUsed = o.lastUsedColorsAr.length
for (var i in o.items)
{
var item=o.items[i]
if (item.menuItemType == _isLastUsedColor) 
{
if (j < lenLastUsed)
{
item.color = o.lastUsedColorsAr[j++]
item.layer.childNodes[0].childNodes[0].style.backgroundColor = 'rgb(' + item.color + ')'
item.show(true)
} else {
item.show(false)
}
} 
}
}
function MenuColor_invert(lyr,inv)
{
var o=_menusItems[lyr._boIndex]
if (o && o.checked)
inv=1
lyr.childNodes[0].className="menuColor"+(inv?"sel":"")
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
function MenuColor_click(lyr,e)
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
function ColorMenuItem_check(check)
{
var o=this
if (o.checked!=check)
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
var cbs=' onclick="'+_codeWinName+'.MenuColor_click(this,event);return true" oncontextmenu="'+_codeWinName+'.MenuColor_click(this,event);return false" onmousedown="'+_codeWinName+'.MenuItem_noBubble(event)" onmouseup="'+_codeWinName+'.MenuItem_noBubble(event)" onmouseover="'+_codeWinName+'.MenuColor_invert(this,1)" onmouseout="'+_codeWinName+'.MenuColor_invert(this,0);" '
if (first)
s+='<tr class="menuLeftPart" valign="middle" align="center">'
s+='<td id="'+(o.par.id+'_item_'+o.id)+'" class="menuLeftPart" '+cbs+' style="padding-top:'+(firstL?2:0)+'px;padding-bottom:'+(lastL?2:0)+'px;padding-left:'+(first?3:1)+'px;padding-right:'+(last?3:1)+'px"><div class="menuColor'+(o.checked?'sel':'')+'"><div style="border:1px solid #4A657B;width:'+d+'px;height:'+d+'px;background-color:rgb('+o.color+');">'+img(_skin+'../transp.gif',10,10,null,null,o.text)+'</div></div></td>'
if (last)
s+='</tr>'
return s
}
function newLastUsedColorMenuItem(par,color,text,cb)
{
var o=newMenuItem(par,"lastUsed_color_"+color,text,cb)
o.color=color
o.menuItemType = _isLastUsedColor
o.attachSubMenu=null
o.check=ColorMenuItem_check
o.getHTML=LastUsedColorMenuItem_getHTML
return o
}
function LastUsedColorMenuItem_getHTML()
{
var o=this,s="",d=_moz?10:12
var cbs=' onclick="'+_codeWinName+'.MenuColor_click(this,event);return true" oncontextmenu="'+_codeWinName+'.MenuColor_click(this,event);return false" onmousedown="'+_codeWinName+'.MenuItem_noBubble(event)" onmouseup="'+_codeWinName+'.MenuItem_noBubble(event)" onmouseover="'+_codeWinName+'.MenuColor_invert(this,1)" onmouseout="'+_codeWinName+'.MenuColor_invert(this,0);" '
s+='<td id="'+(o.par.id+'_item_'+o.id)+'" width="18" class="menuLeftPart" '+cbs+' style="padding-top:0px;padding-bottom:0px;padding-left:1px;padding-right:1px"><div class="menuColor'+(o.checked?'sel':'')+'"><div style="border:1px solid #4A657B;width:'+d+'px;height:'+d+'px;background-color:rgb('+o.color+');">'+img(_skin+'../transp.gif',10,10,null,null,o.text)+'</div></div></td>'
return s
}

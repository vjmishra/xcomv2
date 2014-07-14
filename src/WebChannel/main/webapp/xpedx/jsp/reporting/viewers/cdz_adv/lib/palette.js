if (window._DHTML_LIB_PALETTE_JS_LOADED==null)
{
_DHTML_LIB_PALETTE_JS_LOADED=true
_allBOIcons=new Array
_allBOIconsMenus=new Array
_menuType_simple=0
_menuType_color=1
_menuType_border=2
}
function NewLabelWidget(id,text,convBlanks, wrapSpace)
{
var o=newWidget(id)
o.text=text
o.convBlanks=convBlanks
o.getHTML=LabelWidget_getHTML
o.setDisabled=LabelWidget_setDisabled
o.setClasses=LabelWidget_setClasses
o.setText=LabelWidget_setText
o.dis=false
o.wrapSpace=wrapSpace
o.normalCSS="iconText"
o.disabledCSS="iconTextDis"
return o;
}
function LabelWidget_setDisabled(dis)
{
var o=this
o.dis=dis
if (o.layer)
{
o.layer.className=(o.dis?o.disabledCSS:o.normalCSS)
}
}
function LabelWidget_getHTML()
{
var o=this
var ws=o.wrapSpace?'':'white-space:nowrap;';
return '<div id="'+o.id+'" class="'+(o.dis?o.disabledCSS:o.normalCSS)+'" style='+ws+'margin-right:4px;margin-left:4px;cursor:default">'+convStr(o.text,o.convBlanks)+'</div>'
}
function LabelWidget_setClasses(normalCSS,disabledCSS)
{
var o=this
if (normalCSS)
o.normalCSS=normalCSS
if (disabledCSS)
o.disabledCSS=disabledCSS
if (o.layer)
{
o.layer.className=(o.dis?o.disabledCSS:o.normalCSS)
}
}
function LabelWidget_setText(s)
{
var o=this
if (o.layer) 
{
o.layer.innerHTML=convStr(s)
}
}
function newIconWidget(id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy,dblclickCB)
{
var o=newWidget(id)
o.src=src
o.clickCB=clickCB
o.dblclickCB=dblclickCB
o.text=text
o.alt=alt
o.width=null
o.txtAlign="left"
o.txtPosition="rigth" //to the left or rigth of the icon
o.border=4
o.txtNoPadding=false
o.allowDblClick=false // If true, a double click is regarded as a single click
if (src)
{
o.w=(w!=null)?w:16
o.h=(h!=null)?h:16
o.dx=(dx!=null)?dx:0
o.dy=(dy!=null)?dy:0
o.disDx=(disDx!=null)?disDx:0
o.disDy=(disDy!=null)?disDy:0
}
else
{
o.w=1
o.h=16
}
o.dis=false
o.disp=true
o.margin=1
o.extraHTML=''
o.imgLayer=null
o.txtLayer=null
o.overCB="IconWidget_overCB"
o.outCB="IconWidget_outCB"
o.isDisplayed=IconWidget_isDisplayed
o.setDisplay=IconWidget_setDisplay
o.getHTML=IconWidget_getHTML
o.getTxtWidth=IconWidget_getTxtWidth
o.index=_allBOIcons.length++
o.nocheckClass="iconnocheck"
o.hoverClass="iconhover"
o.checkClass="iconcheck"
o.checkhoverClass="iconcheckhover"
o.currentClass=o.nocheckClass
o.currentHoverClass=o.hoverClass
o.setClasses=IconWidget_setClasses
o.internalUpCB=null
o.internalDownCB=IconWidget_internalDownCB
o.internalUpCB=IconWidget_internalUpCB
o.isHover=false
o.changeTooltip=IconWidget_changeTooltip
o.changeText=IconWidget_changeText
o.changeImg=IconWidget_changeImg
o.setDisabled=IconWidget_setDisabled
o.isDisabled=IconWidget_isDisabled
o.acceptClick=IconWidget_acceptClick
_allBOIcons[o.index]=o
o.outEnable=true
o.setCrs=IconWidget_setCrs
o.oldRes=o.resize
o.resize=IconWidget_resize
o.iconOldInit=o.init
o.init=IconWidget_init
return o
}
function newIconMenuWidget(id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy,isColor,beforeShowCB,menuType)
{
var o=newWidget(id)
if ( typeof(menuType) == 'undefined' )
menuType=isColor?_menuType_color:_menuType_simple
o.menuItemType = isColor? _isColor:_isNotColor
o.icon=newIconWidget("iconMenu_icon_"+id,src,IconMenuWidget_iconClickCB,text,alt,w,h,dx,dy,disDx,disDy)
o.arrow=newIconWidget("iconMenu_arrow_"+id,_skin+"menus.gif",IconMenuWidget_arrowClickCB,null,(_openMenuPart1+(text?text:(alt?alt:""))+_openMenuPart2),7,16,0,81,0,97)
switch (menuType)
{
case _menuType_color:
o.menu=newMenuColorWidget("iconMenu_menu_"+id,IconMenuWidget_hideCB)
break
case _menuType_border:
o.menu=newMenuBordersWidget("iconMenu_menu_"+id,IconMenuWidget_hideCB,beforeShowCB,IconBordersMenuWidget_internalClickCB)
break
default:
case _menuType_simple:
o.menu=newMenuWidget("iconMenu_menu_"+id,IconMenuWidget_hideCB,beforeShowCB)
break
}
o.icon.par=o
o.arrow.par=o
o.menu.parIcon=o
o.icon.margin=0
o.arrow.margin=0
o.icon.overCB="IconWidget_none"
o.icon.outCB="IconWidget_none"
o.arrow.overCB="IconWidget_none"
o.arrow.outCB="IconWidget_none"
o.margin=1
o.spc=0
o.getHTML=IconMenuWidget_getHTML
o.clickCB=clickCB
o.getMenu=IconMenuWidget_getMenu
o.menIcnOldInit=o.init
o.init=IconMenuWidget_init
o.index=_allBOIconsMenus.length++
_allBOIconsMenus[o.index]=o
o.setDisabled=IconMenuWidget_setDisabled
o.isDisabled=IconMenuWidget_isDisabled
o.disableMenu=IconMenuWidget_disableMenu
o.changeText=IconMenuWidget_changeText
o.imwpResize=o.resize
o.resize=IconMenuWidget_resize
o.focus=IconMenuWidget_focus
o.changeArrowTooltip=IconMenuWidget_changeArrowTooltip
o.setClickCallback=IconMenuWidget_setClickCallback
o.disp=true
o.isDisplayed=IconWidget_isDisplayed
o.setDisplay=IconWidget_setDisplay
return o
}
function IconMenuWidget_setClickCallback(clickCB)
{
this.clickCB=clickCB
}
function IconMenuWidget_changeText(s)
{
this.icon.changeText(s)
}
function IconMenuWidget_changeArrowTooltip(tooltip)
{
this.arrow.changeTooltip(tooltip,false);
}
function IconMenuWidget_resize(w,h)
{
var o=this
if (w!=null)
w=Math.max(0,w-2*o.margin)
var d=o.layer.display!="none"
if (d&_moz&&!_saf)
o.setDisplay(false)
o.imwpResize(w,h)
if (w!=null)
o.icon.resize(Math.max(0,w-13-o.spc))
if (d&_moz&&!_saf)
o.setDisplay(true)
}
function IconMenuWidget_setDisabled(dis)
{
var o=this
if (dis) {
if (o.menu.isShown()) {
o.menu.show(false)
}
IconMenuWidgetOutCB(o.index)
}
o.icon.setDisabled(dis)
o.arrow.setDisabled(dis)
}
function IconMenuWidget_isDisabled()
{
return (this.icon.dis == true)
}
function IconMenuWidget_internalCB()
{
var o=this,col=null
if (o.id!=null)
{
col = (o.menuItemType != _isLastUsedColor)? o.id.slice(6) : o.color // "color".length=6
}
var icon=o.par.parIcon
icon.oldColor=icon.curColor
icon.curColor=col
icon.defaultActionColor=col
if (icon.defaultActionColor!=null)
icon.showSample()
if (icon.clickColor)
icon.clickColor()
}
function IconMenuWidget_focus()
{
var o=this
o.arrow.focus()
}
function IconMenuWidget_disableMenu(b)
{
var o=this
o.arrow.setDisabled(b)
o.menu.setDisabled(b)
}
function IconMenuWidget_getMenu()
{
return this.menu
}
function IconWidget_none()
{
}
function IconMenuWidget_init()
{
var o=this
o.menIcnOldInit()
o.icon.init()
o.arrow.init()
o.menu.init()
var l=o.layer
l.onmouseover=IconMenuWidget_OverCB
l.onmouseout=IconMenuWidget_OutCB
}
function IconMenuWidget_getHTML()
{
var o=this,d=o.disp?"":"display:none;"
return '<table id="'+o.id+'" cellspacing="0" cellpadding="0" border="0" style="'+d+'margin:'+o.margin+'px"><tr><td>'+o.icon.getHTML()+'</td><td style="padding-left:'+o.spc+'px" width="'+(13+o.spc)+'">'+o.arrow.getHTML()+'</td></table>'
}
function IconMenuWidget_OverCB()
{
IconMenuWidgetOverCB(getWidget(this).index)
return true
}
function IconMenuWidget_OutCB()
{
IconMenuWidgetOutCB(getWidget(this).index)
}
function IconMenuWidgetOverCB(i)
{
o=_allBOIconsMenus[i]
IconWidget_overCB(o.icon.index)
IconWidget_overCB(o.arrow.index)
}
function IconMenuWidgetOutCB(i)
{
o=_allBOIconsMenus[i]
if (!o.menu.isShown())
{
IconWidget_outCB(o.icon.index)
IconWidget_outCB(o.arrow.index)
}
else
{
IconWidget_overCB(o.icon.index)
IconWidget_overCB(o.arrow.index)
}
}
function IconMenuWidget_iconClickCB()
{
var o=this.par
if (o.clickCB==null)
{
var l=o.layer
o.menu.show(!o.menu.isShown(),getPosScrolled(l).x,getPosScrolled(l).y+o.getHeight()+1,null,null,o)
IconMenuWidgetOverCB(o.index)
}
else
o.clickCB()
}
function IconMenuWidget_arrowClickCB()
{
var o=this.par,l=o.layer
o.menu.show(!o.menu.isShown(),getPosScrolled(l).x,getPosScrolled(l).y+o.getHeight()+1,null,null,o)
IconMenuWidgetOverCB(o.index)
}
function IconMenuWidget_hideCB()
{
var o=this.parIcon
IconMenuWidgetOutCB(o.index)
}
function newRadioIconMenuWidget(id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy,radioIdx,attachMenuCB)
{
var o=newIconMenuWidget(id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy,false)
o.defDx=dx
o.defDy=dy
o.attachMenuCB=null
if (attachMenuCB) {
o.attachMenuCB=attachMenuCB
o.arrow.clickCB=RadioIconMenuWidget_arrowClickCB
o.icon.clickCB=RadioIconMenuWidget_iconClickCB
}
o.attachMenu=RadioIconMenuWidget_attachMenu
o.updateButton=RadioIconMenuWidget_updateButton
o.setAutoUpdateMenuIconFromItemIcon=RadioIconMenuWidget_SetAutoUpdateMenuIconFromItemIcon
o.radioIdx=radioIdx?radioIdx:-1
o.checked=radioIdx?true:false
o.autoUpdateMenuIconFromItemIcon=true
return o
}
function RadioIconMenuWidget_arrowClickCB()
{
var o=this.par,l=o.layer
if (o.attachMenuCB)
o.attachMenuCB()
o.menu.show(!o.menu.isShown(),getPosScrolled(l).x,getPosScrolled(l).y+o.getHeight()+1,null,null,o)
IconMenuWidgetOverCB(o.index)
}
function RadioIconMenuWidget_iconClickCB()
{
var o=this.par
if (o.clickCB==null)
{
var l=o.layer
if (o.attachMenuCB)
o.attachMenuCB()
o.menu.show(!o.menu.isShown(),getPosScrolled(l).x,getPosScrolled(l).y+o.getHeight()+1,null,null,o)
IconMenuWidgetOverCB(o.index)
}
else
o.clickCB()
}
function RadioIconMenuWidget_updateButton(idx, checked)
{
var o=this
o.icon.dis=false
if ((idx != null) &&  (idx > 0)) {
if (checked==null) checked = true
if (o.radioIdx == idx && o.checked==checked) return
o.radioIdx=idx
o.checked=checked
dx=idx*16
dy=0
cn=o.checked?o.icon.checkClass:o.icon.nocheckClass
} else {
if (checked==null) checked = false
o.radioIdx=0
o.checked=checked
dx=16
dy=0
cn=o.checked?o.icon.checkClass:o.icon.nocheckClass
}
if (o.icon)
{
var lyr=o.icon.layer
if (lyr)
{
o.icon.changeImg(dx,dy)
lyr.className=cn
/*if(o.textOnlyLayer) //508
o.textOnlyLayer.title=o.checked?o.textOnlyLayer.innerText+" "+_menuCheckLab:""*/
}
}
if (idx==null) o.setDisabled(true)
//o.icon.
}
function RadioIconMenuWidget_attachMenu(menu)
{
var o=this
o.par=null
o.menu=menu
menu.par=null
menu.container=o
//menu.par=o
//menu.zIndex=o.par.zIndex+2
/*
if (o.layer)
{
if (o.arrowLayer==null)
o.arrowLayer=getLayer(o.par.id+'_item_arrow_'+o.id)
var dis=o.disabled
changeSimpleOffset(o.arrowLayer,dis?7:0,dis?81:64)
}
*/
return menu
}
function RadioIconMenuWidget_SetAutoUpdateMenuIconFromItemIcon (_autoUpdateMenuIconFromItemIcon)
{
this.autoUpdateMenuIconFromItemIcon = _autoUpdateMenuIconFromItemIcon
}
function newIconColorMenuWidget(id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy, lastUsedColorsAr, curColorAsActionColor)
{
var o=newIconMenuWidget(id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy,true)
o.setColor=IconColorMenuWidget_setColor
o.getColor=IconColorMenuWidget_getColor
o.getDefaultActionColor=IconColorMenuWidget_getDefaultActionColor;
o.setDefaultActionColor=IconColorMenuWidget_setDefaultActionColor;
if (typeof(curColorAsActionColor)=="undefined")
o.curColorAsActionColor=true;
else
o.curColorAsActionColor=curColorAsActionColor;
o.icon.extraHTML='<div id="colSample_'+o.id+'" style="position:relative;top:-6px;left:2px;width:16px;height:4px;overflow:hidden;margin:0px"></div>'
o.curColor=null
o.sampleLayer=null
o.hasDefaultActionOnIcon=false
o.clickColor=clickCB
o.clickCB=(o.hasDefaultActionOnIcon?clickCB:null)
o.defaultActionColor=null
o.colOldSetDis=o.setDisabled
o.setDisabled=IconColorMenuWidget_setDisabled
o.showSample=IconColorMenuWidget_showSample
o.changeTooltip=IconColorMenuWidget_changeTooltip
o.setDefaultActionOnIcon=IconColorMenuWidget_setDefaultActionOnIcon
var m=o.menu
m.ac=m.addColor
var cb=IconMenuWidget_internalCB
var c1=m.addCheck("color_-1,-1,-1",_default,cb)
c1.leftZoneClass="menuLeftPartColor"
c1.leftZoneSelClass="menuLeftPartSel"
_colorsArr = new Array()
_colorsArr["0,0,0"]=_black
_colorsArr["148,52,0"]=_brown
_colorsArr["49,52,0"]=_oliveGreen
_colorsArr["0,52,0"]=_darkGreen
_colorsArr["0,52,99"]=_darkTeal
_colorsArr["0,0,132"]=_navyBlue
_colorsArr["49,52,148"]=_indigo
_colorsArr["66,65,66"]=_darkGray
_colorsArr["132,4,0"]=_darkRed
_colorsArr["255,101,0"]=_orange
_colorsArr["123,125,0"]=_darkYellow
_colorsArr["0,125,0"]=_green
_colorsArr["0,125,123"]=_teal
_colorsArr["0,0,255"]=_blue
_colorsArr["99,101,148"]=_blueGray
_colorsArr["132,130,132"]=_mediumGray
_colorsArr["255,0,0"]=_red
_colorsArr["255,195,66"]=_lightOrange
_colorsArr["148,199,0"]=_lime
_colorsArr["49,150,99"]=_seaGreen
_colorsArr["49,199,198"]=_aqua
_colorsArr["49,101,255"]=_lightBlue
_colorsArr["132,0,132"]=_violet
_colorsArr["148,150,148"]=_gray
_colorsArr["255,0,255"]=_magenta
_colorsArr["255,199,0"]=_gold
_colorsArr["255,255,0"]=_yellow
_colorsArr["0,255,0"]=_brightGreen
_colorsArr["0,255,255"]=_cyan
_colorsArr["0,199,255"]=_skyBlue
_colorsArr["148,52,99"]=_plum
_colorsArr["198,195,198"]=_lightGray
_colorsArr["255,178,181"]=_pink
_colorsArr["255,199,148"]=_tan
_colorsArr["255,255,206"]=_lightYellow
_colorsArr["206,255,206"]=_lightGreen
_colorsArr["206,255,255"]=_lightTurquoise
_colorsArr["148,199,255"]=_paleBlue
_colorsArr["198,150,255"]=_lavender
_colorsArr["255,255,255"]=_white
with (m)
{
for (var i in _colorsArr) {
ac(_colorsArr[i],i,cb)
}
}
if (lastUsedColorsAr) 
{
m.addLastUsed(_lastUsed,lastUsedColorsAr, cb)
}
c1=m.add(null,_moreColors,cb)
c1.leftZoneClass="menuLeftPartColor"
c1.leftZoneSelClass="menuLeftPartSel"
return o
}
function IconColorMenuWidget_setColor(color)
{
var o=this,menu=o.menu
menu.uncheckAll()
o.curColor=color
if (o.curColorAsActionColor)
o.defaultActionColor=o.curColor;
if (color!=null)
{
var id="color_"+color
var items=menu.items
for (var i in items)
{
var item=items[i]
if ((item.menuItemType && (item.menuItemType == _isLastUsedColor) && (color == menu.lastUsedColorsAr[item.idx])) || (item.id==id)){
item.check(true)
break
}
}
}
o.showSample()
}
function IconColorMenuWidget_showSample()
{
var o=this
if (o.layer)
{
if (o.sampleLayer==null)
o.sampleLayer=getLayer('colSample_'+o.id)
var color=o.defaultActionColor;
o.sampleLayer.style.backgroundColor=((color!='-1,-1,-1')&&(color!=null)) ? 'rgb('+color+')' : ''
}
}
function IconColorMenuWidget_setDisabled(dis)
{
this.colOldSetDis(dis)
if (this.layer)
this.showSample()
}
function IconColorMenuWidget_getColor()
{
return this.curColor;
}
function IconColorMenuWidget_getDefaultActionColor()
{
return this.defaultActionColor;
}
function IconColorMenuWidget_setDefaultActionColor(c)
{
this.defaultActionColor=c;
}
function IconColorMenuWidget_changeTooltip(s)
{
var o=this
if (s==null) return;
if (o.icon && o.arrow) {
o.icon.alt=s;
o.icon.changeTooltip(s)
o.arrow.changeTooltip(_openMenuPart1+ s +_openMenuPart2)
}
}
function IconColorMenuWidget_setDefaultActionOnIcon(b)
{
var o=this
o.hasDefaultActionOnIcon=b
o.clickCB=(o.hasDefaultActionOnIcon?o.clickColor:null)
}
function newIconCheckWidget(id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy,dblclickCB)
{
var o=newIconWidget(id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy,dblclickCB)
o.checked=false
o.internalUpCB=IconCheckWidget_internalUpCB
o.internalDownCB=IconCheckWidget_internalDownCB
o.check=IconCheckWidget_check
o.isChecked=IconCheckWidget_isChecked
o.oldInit=o.init
o.init=IconCheckWidget_init
o.isRadio=false
return o
}
function newIconToggleWidget(id,src,clickCB,text,alt,w,h,dx,dy,togX,togY,disDx,disDy,dblclickCB)
{
var o=newIconWidget(id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy,dblclickCB)
o.togX = togX
o.togY = togY
o.checked=false
o.internalUpCB=IconToggleWidget_internalUpCB
o.internalDownCB=IconToggleWidget_internalDownCB
o.check=IconToggleWidget_check
o.isChecked=IconCheckWidget_isChecked
o.oldInit=o.init
o.init=IconCheckWidget_init
o.isRadio=false
return o
}
function newIconRadioWidget(id,src,clickCB,text,alt,group,w,h,dx,dy,disDx,disDy)
{
var o=newIconCheckWidget(id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy)
o.group=group
o.beforeClickCB=IconRadioWidget_uncheckOthers
o.isRadio=true
if (_RadioWidget_groups[group]==null)
_RadioWidget_groups[group]=new Array
o.groupInstance=_RadioWidget_groups[group]
var g=o.groupInstance
o.groupIdx=g.length
g[g.length]=o
return o
}
function newPaletteContainerWidget(id,contextMenu,margin,clip)
{
var o=newWidget(id)
o.beginHTML=PaletteContainerWidget_beginHTML
o.endHTML=PaletteContainerWidget_endHTML
o.add=PaletteContainerWidget_add
o.palettes=new Array
o.contextMenu=contextMenu
o.margin=(margin!=null)?margin:4
o.clip=clip==null?true:clip
return o
}
function newPaletteWidget(id,height)
{
var o=newWidget(id)
o.getHTML=PaletteWidget_getHTML
o.add=PaletteWidget_add
o.disableChildren=PaletteWidget_disableChildren
o.items=new Array
o.oldInit=o.init
o.init=PaletteWidget_init
o.beginRightZone=PaletteWidget_beginRightZone
o.height=height
o.rightZoneIndex=-1
o.sepCount=0
o.vertPadding=4
return o
}
function newPaletteSepWidget(id)
{
var o=newWidget(id)
o.getHTML=PaletteSepWidget_getHTML
o.isSeparator=true
return o
}
function newPaletteVerticalSepWidget(id)
{
var o=newWidget(id)
o.getHTML=PaletteVerticalSepWidget_getHTML
o.isSeparator=true
return o
}
function PaletteVerticalSepWidget_getHTML()
{
return img(_skin+"iconsep.gif",6,22,null,' id="'+this.id+'" ')
}
function getPaletteSep()
{
return img(_skin+"iconsep.gif",6,22)
}
function IconRadioWidget_uncheckOthers()
{
var g=this.groupInstance,idx=this.groupIdx,len=g.length;
for (var i=0;i<len;i++)
{
if (i!=idx)
{
var c=g[i];
if(c)
c.check(false);
}
}
}
function PaletteWidget_beginRightZone()
{
this.rightZoneIndex=this.items.length
}
function PaletteSepWidget_getHTML()
{
return '<div style="background-image:url('+_skin+'sep.gif);height:2px;padding:0px;margin-top:0px;margin-bottom:0px;margin-left:4px;margin-right:4px">'+getSpace(1,2)+'</div>'
}
function PaletteContainerWidget_beginHTML()
{
var o=this
var cm=o.contextMenu?('oncontextmenu="'+_codeWinName+'.PaletteContainerWidget_contextMenu(this,event);return false"'):''
var ov=o.clip?"overflow:hidden;":""
if (o.clip)
return '<div '+cm+ ' class="palette" style="width:100%;overflow:hidden;margin:'+o.margin+'px;" id="'+o.id+'">'
else
return '<table width="100%" cellspacing="0" cellpadding="0" '+cm+ ' class="palette" style="margin:'+o.margin+'px;" id="'+o.id+'"><tr><td>'
}
function PaletteContainerWidget_endHTML()
{
if (this.clip)
return '</div>'
else
return '</td></tr></table>'
}
_delayedMenu=null
function PaletteContainerWidget_contextMenu(o,e)
{
if (_ie)
e=_curWin.event
_delayedMenu=getWidget(o).contextMenu
setTimeout('_delayedMenu.par=null;_delayedMenu.show(true,'+absxpos(e)+','+absypos(e)+')',1)
}
function PaletteContainerWidget_add(palette)
{
this.palettes[this.palettes.length]=palette
return palette
}
function PaletteWidget_getHTML()
{
var o=this,items=o.items,len=items.length,fields=new Array;j=0
fields[j++]='<table id="'+o.id+'" '+attr("height",o.height)+' cellspacing="0" cellpadding="0" '+(_ie?"":'width="100%"')+'><tbody><tr valign="middle">'
fields[j++]='<td align="left" style="padding-left:'+o.vertPadding+'px;padding-right:4px"><table cellspacing="0" cellpadding="0"><tbody><tr valign="middle">'
var haveRightZone=false
for (var i=0;i<len;i++)
{
if (i==o.rightZoneIndex)
{
fields[j++]='</tr></tbody></table></td><td width="100%" align="right" style="padding-right:'+o.vertPadding+'px"><table cellspacing="0" cellpadding="0"><tbody><tr valign="middle">'
haveRightZone=true
}
var it=items[i]
//fields[j++]='<td>'+(it?it.getHTML():getPaletteSep())+'</td>'
fields[j++]='<td>'+it.getHTML()+'</td>'
}
if (!haveRightZone)
fields[j++]='</tr></tbody></table></td><td width="100%" align="right" style="padding-right:4px"><table cellspacing="0" cellpadding="0"><tbody><tr valign="middle"><td></td>'
fields[j++]='</tr></tbody></table></td></tr></tbody></table>'
return fields.join("")
}
function PaletteWidget_add(item)
{
if(item==null)
{
item=newPaletteVerticalSepWidget(this.id+"_palettesep_"+(this.sepCount++))
}
this.items[this.items.length]=item
return item
}
function PaletteWidget_disableChildren(dis)
{
var items=this.items
for (var i in items)
{
var item=items[i]
if (item&&(item.isSep!=true))
item.setDisabled(dis)
}
}
function PaletteWidget_init()
{
this.oldInit()
var items=this.items
for (var i=0; i<items.length; i++)
{
var item=items[i]
if (item)
item.init()
}
}
function IconWidget_isDisplayed()
{
return this.disp
}
function IconWidget_setDisplay(d)
{
var o=this
if (o.css)
{
var ds=d?"block":"none"
if (o.css.display!=ds)
o.css.display=ds
}
o.disp=d
}
function IconWidget_getTxtWidth()
{
var o=this,w=o.width
if (w!=null)
{
w=w-(o.margin*2) // remove table margin
w=w-(o.src?o.w+o.border:1) // Image size
w=w-(o.txtNoPadding?0:((o.src?4:2)+2))
if (_ie)
w-=2
else
w-=2
return Math.max(0,w)
}
else
return -1
}
function IconWidget_init()
{
var o=this,dblClick=false
o.iconOldInit()
var l=o.layer
//manage focus for 508
l.tabIndex=o.dis?-1:0
//manage tooltip for 508
//l.title=(o.alt?o.alt:(o.text?o.text:""))
l.title=(o.alt?o.alt:"")
// attach callbacks
if (o.clickCB)
{
l.onclick=IconWidget_upCB
l.onmousedown=IconWidget_downCB
if (o.allowDblClick&&(_ie||_saf)&&(o.dblclickCB==null))
{
dblClick=true
addDblClickCB(l,IconWidget_upCB)
}
l.onkeydown=IconWidget_keydownCB
l.onmouseover=IconWidget_realOverCB
l.onmouseout=IconWidget_realOutCB
}
if (o.dblclickCB)
{
addDblClickCB(l,IconWidget_dblClickCB)
}
else if (!dblClick)
addDblClickCB(l,IconWidget_retFalse)
l.onselectstart=IconWidget_retFalse
var d=o.disp?"block":"none"
if (o.css.display!=d)
o.css.display=d
}
function IconWidget_getHTML()
{
var o=this,imgCode=o.src?'<div style="overflow:hidden;height:'+(o.h+o.border)+'px;width:'+(o.w+o.border)+'px;">'+simpleImgOffset(o.src,o.w,o.h,o.dis?o.disDx:o.dx,o.dis?o.disDy:o.dy,'IconImg_'+o.id,null,o.alt,'margin:2px;cursor:'+(o.clickCB ? (!o.acceptClick() ? 'default' : _hand):'default'))+o.extraHTML+'</div>':'<div class="iconText" style="width:1px;height:'+(o.h+o.border)+'px"></div>'
var txtAtt='style="white-space:nowrap;',txtW=o.getTxtWidth()
if (txtW>=0)
txtAtt+='text-overflow:ellipsis;overflow:hidden;width:'+txtW+'px'
txtAtt+='"'
var iconHTML = '<td>'+ ((o.clickCB&&_ie)?lnk(imgCode,null,null,null, ' tabIndex="-1"' ):imgCode)+'</td>';
var d=o.disp?"":"display:none;"
return '<table style="'+d+'height:'+(o.h+o.border+(_dtd4?2:0))+'px;'+(o.width!=null?"width:"+o.width+"px;":"")+'margin:'+o.margin+'px" id="'+o.id+'" class="' + o.nocheckClass + '" cellspacing="0" cellpadding="0" border="0"><tr valign="middle">'+
((o.txtPosition=="rigth")?iconHTML:"")+
(o.text?'<td style="padding-'+ (o.txtPosition=="rigth"?'left':'right') +':'+(o.txtNoPadding?0:(o.src?4:2))+'px;padding-'+ (o.txtPosition=="rigth"?'right':'left') +':'+(o.txtNoPadding?0:2)+'px"><span id="IconImg_Txt_'+o.id+'" class="iconText'+(o.dis?"Dis":"")+'" '+txtAtt+'>'+convStr(o.text)+'</span></td>':'')+
((o.txtPosition=="left")?iconHTML:"")+
'</tr></table>'
}
__globIconWidget_realOutCBobj=null
function IconWidget_realOutCB()
{
var o=getWidget(this)
if (o&&o.outCB)
{
__globIconWidget_realOutCBobj=o
eval(__globIconWidget_realOutCBobj.outCB+'('+__globIconWidget_realOutCBobj.index+')')
}
}
function IconWidget_realOverCB()
{
var o=getWidget(this)
if (o&&o.overCB)
{
__globIconWidget_realOutCBobj=o
eval(__globIconWidget_realOutCBobj.overCB+'('+__globIconWidget_realOutCBobj.index+')')
}
return true
}
function IconWidget_retFalse()
{
return false
}
function IconWidget_resize(w,h)
{
var o=this
if (o.layer)
o.oldRes(w,h)
if (o.txtLayer==null)
o.txtLayer=getLayer("IconImg_Txt_"+o.id)
if (w!=null)
{
o.width=w
var txtW=o.getTxtWidth()
if (o.txtLayer&&(txtW>=0))
{
o.txtLayer.style.width=''+txtW+'px'
}
}
if (h!=null)
{
o.h=h?(h-o.border):o.h
if (o.txtLayer&&(o.h>=0))
{
o.txtLayer.style.height=''+o.h+'px'
}
}
}
function IconWidget_changeTooltip(s,isTemporary)
{
var o=this
if (s==null) return;
if(!isTemporary)
o.alt=s;
if(o.layer)
o.layer.title=s
if (o.imgLayer==null)
o.imgLayer = getLayer('IconImg_'+this.id);
if (o.imgLayer)
changeSimpleOffset(o.imgLayer,null,null,null,s)
}
function IconWidget_changeText(s)
{
var o=this
o.text=s
if (o.layer)
{
if (o.txtLayer==null)
o.txtLayer=getLayer("IconImg_Txt_"+o.id)
o.txtLayer.innerHTML=convStr(s)
}
}
function IconWidget_changeImg(dx,dy,src)
{
var o=this
if (src) o.src=src
if (dx!=null) o.dx=dx
if (dy!=null) o.dy=dy
if (o.layer&&(o.imgLayer==null))
o.imgLayer = getLayer('IconImg_'+this.id);
if (o.imgLayer)
changeSimpleOffset(o.imgLayer,dx,dy,o.src)
}
function IconWidget_internalDownCB()
{
if (!this.dis)
this.currentHoverClass=this.checkhoverClass
}
function IconWidget_internalUpCB()
{
if (!this.dis)
this.currentHoverClass=this.hoverClass
}
function IconWidget_setCrs()
{
var o=this,crs=(o.clickCB ? (!o.acceptClick() ? 'default' : _hand) : 'default')
o.css.cursor=crs
if (o.src)
{
if (o.imgLayer==null)
o.imgLayer = getLayer('IconImg_'+o.id);
if (o.imgLayer) o.imgLayer.style.cursor=crs
}
}
function IconWidget_downCB(e)
{
var o=getWidget(this)
if ((o.layer)&&(o.acceptClick()))
{
if (_ie||(_moz&&(!o.dblclickCB||e.detail!=2)))
{
o.internalDownCB()
o.layer.className=o.currentHoverClass
}
}
return _webKit ? true : false
}
function IconWidget_upCB(e)
{
var o=getWidget(this)
if (_moz)
{
if (o.dblclickCB&&e.detail==2)
{
setTimeout("delayedDblClickCB("+o.index+")",1)
return false;
}
}
if ((o.layer)&&(o.acceptClick()))
{
o.internalUpCB()
o.layer.className=o.isHover?o.currentHoverClass:o.currentClass
o.setCrs()
setTimeout("delayedClickCB("+o.index+")",1)
}
}
function IconWidget_dblClickCB()
{
var o=getWidget(this)
if (_ie&&(o.layer)&&(o.acceptClick()))
{
//o.internalUpCB()
setTimeout("delayedDblClickCB("+o.index+")",1)
}
return false
}
function IconWidget_keydownCB(e)
{
if(eventGetKey(e)==13) //enter
{
var o=getWidget(this)
setTimeout("delayedClickCB("+o.index+")",1)
eventCancelBubble(e);//be careful ! usefull for dialog box close by Enter keypressed
}
}
function delayedClickCB(index)
{
var o=_allBOIcons[index]
if (o.beforeClickCB)
o.beforeClickCB()
if (o.clickCB)
o.clickCB()
}
function delayedDblClickCB(index)
{
var o=_allBOIcons[index]
if (o.dblclickCB)
o.dblclickCB()
}
function IconWidget_overCB(index)
{
var o=_allBOIcons[index]
o.setCrs()
if ((o.layer)&&(!o.dis)&&!(o.par && o.par.checked)) // if not button container and it is checked
{
o.isHover=true
o.layer.className=o.currentHoverClass
}
}
function IconWidget_outCB(index)
{
var o=_allBOIcons[index]
if ((o.layer)&&(o.outEnable)&&!(o.par && o.par.checked)) // if not button container and it is checked
{
o.isHover=false
o.layer.className=o.currentClass
}
}
function IconCheckWidget_init()
{
var o=this
o.oldInit()
o.check(o.checked,true)
}
function IconCheckWidget_internalDownCB()
{
var o=this
if (o.acceptClick())
o.currentHoverClass=o.checked?o.hoverClass:o.checkhoverClass
}
function IconCheckWidget_internalUpCB()
{
var o=this
if (o.acceptClick())
{
o.checked=o.isRadio?true:!o.checked
o.currentClass=o.checked?this.checkClass:this.nocheckClass
o.currentHoverClass=o.checked?this.checkhoverClass:this.hoverClass
}
}
function IconToggleWidget_internalDownCB()
{
var o=this
if (o.acceptClick()) {
//o.currentHoverClass=o.checked?o.hoverClass:o.checkhoverClass
}
}
function IconToggleWidget_internalUpCB()
{
var o=this
if (o.acceptClick())
{
if (o.layer&&(o.imgLayer==null))
o.imgLayer = getLayer('IconImg_'+this.id);
if (!o.imgLayer) return
o.checked=!o.checked
if (o.checked)
{
changeSimpleOffset(o.imgLayer,o.togX,o.togY,o.src)
} else {
changeSimpleOffset(o.imgLayer,o.dx,o.dy,o.src)
}
}
}
function IconCheckWidget_check(checked,force)
{
var o=this
if ((o.checked!=checked)||force)
{
o.checked=checked
if (o.layer)
{
o.layer.className=o.currentClass=o.checked?this.checkClass:this.nocheckClass
o.currentHoverClass=o.checked?this.checkhoverClass:this.hoverClass
}
}
if (o.checked&&o.beforeClickCB)
{
if (o.layer)
o.beforeClickCB()
}
}
function IconToggleWidget_check(checked,force)
{
var o=this
if ((o.checked!=checked)||force)
{
o.checked=checked
if (o.layer&&(o.imgLayer==null))
o.imgLayer = getLayer('IconImg_'+this.id);
if (!o.imgLayer) return
if (o.checked)
{
changeSimpleOffset(o.imgLayer,o.togX,o.togY,o.src)
} else {
changeSimpleOffset(o.imgLayer,o.dx,o.dy,o.src)
}
}
if (o.checked&&o.beforeClickCB)
{
if (o.layer)
o.beforeClickCB()
}
}
function IconCheckWidget_isChecked()
{
return this.checked
}
function IconWidget_setClasses(nocheck, check, hover, checkhover)
{
var o=this
o.nocheckClass=nocheck
o.checkClass=check
o.hoverClass=hover
o.checkhoverClass=checkhover
o.currentClass=o.nocheckClass
o.currentHoverClass=o.hoverClass
}
function IconWidget_setDisabled(dis)
{
 var o=this
{
o.dis=dis
if (o.layer)
{
o.setCrs()
if (o.src)
{
if (o.imgLayer==null)
o.imgLayer = getLayer('IconImg_'+this.id);
changeSimpleOffset(o.imgLayer,dis?o.disDx:o.dx,dis?o.disDy:o.dy)
}
if (o.text)
{
if (o.txtLayer==null)
o.txtLayer=getLayer("IconImg_Txt_"+o.id)
var newClassName="iconText"+(dis?"Dis":"")
if (o.txtLayer.className!=newClassName)
o.txtLayer.className=newClassName
if (dis)
{
if (o.layer.className!=o.currentClass)
o.layer.className=o.currentClass
}
}
//manage focus for 508
o.layer.tabIndex=o.dis?-1:0
}
}
}
function IconWidget_isDisabled()
{
return this.dis?this.dis:false
}
function IconWidget_acceptClick()
{
var o=this
if (o.isDisabled()) return false
if (o.isRadio&&o.checked) return false
return true
}
function newCustomCombo(id,changeCB,noMargin,width,tooltip,url,w,h,dx,dy,disDx,disDy)
{
var o=newIconMenuWidget(id,url,null," ",tooltip,w,h,dx,dy,disDx,disDy)
o.icon.width=width!=null?Math.max(0,width-13):50-(2*o.margin)
o.icon.setClasses("combonocheck", "combocheck", "combohover", "combocheck")
o.icon.clip
o.arrow.setClasses("iconnocheck", "combobtnhover", "combobtnhover", "combobtnhover")
o.spc=0
o.margin=2
if (url==null)
{
o.icon.h=12
o.arrow.h=12
o.arrow.dy+=2
o.arrow.disDy+=2
}
// Private attributes
o.counter=0
o.changeCB=changeCB
o.selectedItem=null
o.setOldDid=o.setDisabled
o.disabled=false
o.ccomboOldInit=o.init
// Public methods
o.init=CustomCombo_init
o.add=CustomCombo_add
o.addSeparator=CustomCombo_addSeparator
o.addMenuItem=CustomCombo_addMenuItem
o.select=CustomCombo_select
o.getSelection=CustomCombo_getSelection
o.valueShow=CustomCombo_valueShow
o.valueSelect=CustomCombo_valueSelect
o.setUndefined=CustomCombo_setUndefined
o.setDisabled=CustomCombo_setDisabled
o.getVisibleItemsCount=CustomCombo_getVisibleItemsCount
o.del=CustomCombo_del
// Private methods
o.selectItem=CustomCombo_selectItem
o.getItemByIndex=CustomCombo_getItemByIndex
o.getItemIndex=CustomCombo_getItemIndex
o.setItemDisabled=CustomCombo_setItemDisabled
return o
}
function CustomCombo_init()
{
var o=this
o.ccomboOldInit()
if(o.disabled) o.icon.changeTooltip(o.icon.alt?o.icon.alt:"",true)
o.arrow.changeTooltip(_openMenuPart1+(o.icon.alt?o.icon.alt:"")+_openMenuPart2)
}
function CustomCombo_add(s,val,selected)
{
var o=this
var item=o.menu.addCheck(o.id+"_it_"+(o.counter++),s,CustomCombo_internalCB)
item.val=""+val
item.parCombo=o
item.isComboVal=true
if ((o.selectedItem==null)||selected)
o.selectItem(item)
}
function CustomCombo_addSeparator()
{
this.menu.addSeparator()
}
function CustomCombo_addMenuItem(id,text,cb,icon,dx,dy,disabled,disDx,disDy)
{
this.menu.add(id,text,cb,icon,dx,dy,disabled,disDx,disDy)
}
function CustomCombo_internalCB()
{
var o=this,c=o.parCombo
c.selectItem(o)
if (c.changeCB)
c.changeCB()
}
function CustomCombo_getItemByIndex(idx)
{
var items=this.menu.items
return ((idx>=0)&&(idx<items.length))?items[idx]:null
}
function CustomCombo_getItemIndex(item)
{
var items=this.menu.items,len=items.length,j=0
for (var i=0;i<len;i++)
{
var it=items[i]
if (it.isComboVal)
{
if (it.id==item.id)
return j
j++
}
}
return -1
}
function CustomCombo_selectItem(item)
{
var o=this
if (o.selectedItem)
o.selectedItem.check(false)
if (item)
{
o.val=item.val
o.icon.changeText(o.disabled?"":item.text)
o.selectedItem=item
item.check(true)
if(o.disabled)
o.icon.changeTooltip(o.icon.alt?o.icon.alt:"",true)
else
o.icon.changeTooltip(o.icon.alt?(o.icon.alt+' ('+item.text)+')':(item.text),true)
}
else
{
o.val=null
o.icon.changeText("")
o.icon.changeTooltip(o.icon.alt?o.icon.alt:"",true)
o.selectedItem=null
}
}
function CustomCombo_setDisabled(d)
{
var o=this
if (o.selectedItem)
o.icon.changeText(d?"":o.selectedItem.text)
o.disabled=d
o.setOldDid(d)
if(d) o.icon.changeTooltip(o.icon.alt?o.icon.alt:"",true)
}
function CustomCombo_select(idx)
{
var o=this,item=o.getItemByIndex(idx)
if (item)
o.selectItem(item)
}
function CustomCombo_setItemDisabled(idx,disabled)
{
var o=this,item=o.getItemByIndex(idx)
if (item)
item.setDisabled(disabled)
}
function CustomCombo_getSelection()
{
var o=this,it=o.selectedItem
if (it)
return {index:o.getItemIndex(it),value:it.val}
else
return null
}
function CustomCombo_valueSelect(v)
{
v=""+v
var o=this,items=o.menu.items,len=items.length
for (var i=0;i<len;i++)
{
var it=items[i]
if ((it.isComboVal)&&(it.val==v)&&(it.isShown) )
{
o.selectItem(it)
return true
}
}
return false
}
function CustomCombo_valueShow(v,show)
{
v=""+v
var o=this,items=o.menu.items,len=items.length
for (var i=0;i<len;i++)
{
var it=items[i]
if ((it.isComboVal)&&(it.val==v))
{
it.show(show)
return
}
}
}
function CustomCombo_setUndefined(u)
{
var o=this
if (u)
o.selectItem(null)
}
function CustomCombo_getVisibleItemsCount()
{
var o=this,items=o.menu.items,len=items.length,n=0
for (var i=0;i<len;i++)
{
var it=items[i]
if ((it.isComboVal)&&(it.isShown))
{
n++
}
}
return n;
}
function CustomCombo_del(i)
{
if (this.menu)
this.menu.remove (i)
}
function newComboTextFieldWidget(id,changeCB,maxChar,keyUpCB,enterCB,noMargin,tooltip,width,dataType)
{
var o=null
if (dataType == "int")
o=newIntFieldWidget(id,changeCB,maxChar,keyUpCB,enterCB,noMargin,tooltip,width) 
else
o=newTextFieldWidget(id,changeCB,maxChar,keyUpCB,enterCB,noMargin,tooltip,width)
o.par=null
o.oldInit=o.init
o.init=ComboTextFieldWidget_init
o.setContentEditable=ComboTextFieldWidget_setContentEditable
o.isContentEditable=ComboTextFieldWidget_isContentEditable
o.getHTML=ComboTextFieldWidget_getHTML
o.oldSetDisabled=o.setDisabled
o.setDisabled=ComboTextFieldWidget_setDisabled
return o
}
function ComboTextFieldWidget_init()
{
var o=this
o.oldInit()
var l=o.layer
if (l!=null)
{
o.setContentEditable(true)
l.onclick=ComboTextFieldWidget_onClick
}
}
function ComboTextFieldWidget_setContentEditable(d)
{
var o=this, l=o.layer
o.contentEditable=d
if (l)
{
l.contentEditable=d
l.style.cursor=d?'text':_hand
l.className=d?'comboEditable':'combo'
}
}
function ComboTextFieldWidget_isContentEditable()
{
var o=this
return o.contentEditable
}
function ComboTextFieldWidget_onClick()
{
var o=getWidget(this)
if (o.contentEditable)
return
if (o.par!=null)
o.par.clickCB()
}
function ComboTextFieldWidget_getHTML()
{
var o=this
return '<input'+(o.disabled?' disabled':'')+' oncontextmenu="event.cancelBubble=true;return true" style="'+sty("width",this.width)+(_dtd4?'margin-top:1px;margin-bottom:1px;padding-left:2px;padding-right:2px':'')+'height:18px;margin-left:'+(this.noMargin?0:10)+'px" onfocus="'+_codeWinName+'.TextFieldWidget_focus(this)" onblur="'+_codeWinName+'.TextFieldWidget_blur(this)" onchange="'+_codeWinName+'.TextFieldWidget_changeCB(event,this)" onkeydown=" return '+_codeWinName+'.TextFieldWidget_keyDownCB(event,this);" onkeyup=" return '+_codeWinName+'.TextFieldWidget_keyUpCB(event,this);" type="text" '+attr('maxLength',this.maxChar)+' ondragstart="event.cancelBubble=true;return true" onselectstart="event.cancelBubble=true;return true" class="combo" id="'+this.id+'" name="'+this.id+'"'+attr('title',this.tooltip)+' value="">'
}
function ComboTextFieldWidget_setDisabled(d)
{
var o=this
o.oldSetDisabled(d)
//o.layer.className=d?'comboDisabled':'combo'
}
function newTextComboWidget(id,maxChar,tooltip,w,changeCB,checkCB,beforeShowCB,formName,dataType)
{
var o=newWidget(id)
// basic widgets
o.text=newComboTextFieldWidget((formName?formName:"text_"+id),TextComboWidget_checkCB,maxChar,null/*keyUpCB*/,TextComboWidget_enterCB,true,tooltip,w-13,dataType)
o.arrow=newIconWidget("arrow_"+id,_skin+"menus.gif",TextComboWidget_arrowClickCB,null,(_openMenuPart1+(tooltip?tooltip:"")+_openMenuPart2),7,16,0,81,0,97)
o.menu=newMenuWidget("menu_"+id,TextComboWidget_hideCB,beforeShowCB)
o.arrow.setClasses("iconnocheck", "combobtnhover", "combobtnhover", "combobtnhover")
// set the parent
o.text.par=o
o.arrow.par=o
o.menu.parIcon=o
// properties
o.arrow.margin=0
o.arrow.overCB="IconWidget_none"
o.arrow.outCB="IconWidget_none"
o.margin=0
o.spc=0
o.counter=0
// to align the text field and arrow icon
o.arrow.h=12
o.arrow.dy+=2
o.arrow.disDy+=2
o.index=_allBOIconsMenus.length++
_allBOIconsMenus[o.index]=o
// public methods
o.menIcnOldInit=o.init
o.init=TextComboWidget_init
o.imwpResize=o.resize
o.resize=TextComboWidget_resize
o.getHTML=TextComboWidget_getHTML
o.setDisabled=TextComboWidget_setDisabled
o.isDisabled=TextComboWidget_isDisabled
o.add=TextComboWidget_add
o.addSeparator=TextComboWidget_addSeparator
o.addMenuItem=TextComboWidget_addMenuItem
o.select=TextComboWidget_select
o.getSelection=TextComboWidget_getSelection
o.valueShow=TextComboWidget_valueShow
o.valueSelect=TextComboWidget_valueSelect
o.setUndefined=TextComboWidget_setUndefined
o.setContentEditable=TextComboWidget_setContentEditable
o.isContentEditable=TextComboWidget_isContentEditable
o.del=TextComboWidget_del
o.getTextValue=TextComboWidget_getTextValue
// private methods
o.changeCB=changeCB
o.checkCB=checkCB
o.clickCB=TextComboWidget_clickCB
o.selectItem=TextComboWidget_selectItem
o.getItemByIndex=TextComboWidget_getItemByIndex
o.getItemIndex=TextComboWidget_getItemIndex
o.setItemDisabled=TextComboWidget_setItemDisabled
o.selectTextValue=TextComboWidget_SelectTextValue
o.text.enterCancelBubble=false
return o
}
function TextComboWidget_init()
{
var o=this
o.menIcnOldInit()
o.text.init()
o.arrow.init()
o.menu.init()
var l=o.layer
l.onmouseover=TextCombo_OverCB
l.onmouseout=TextCombo_OutCB
}
function TextComboWidget_getHTML()
{
var o=this, s=''
s+='<table id="'+o.id+'" cellspacing="0" cellpadding="0" border="0" style="cursor:default;margin:'+o.margin+'px"><tbody><tr>'
s+='<td>'+o.text.getHTML()+'</td>'
s+='<td style="padding-left:'+o.spc+'px" width="'+(13+o.spc)+'">'+o.arrow.getHTML()+'</td>'
s+='</tr></tbody></table>'
return s
}
function TextComboWidget_resize(w,h)
{
var o=this
if (w!=null)
w=Math.max(0,w-2*o.margin)
var d=o.layer.display!="none"
if (d&_moz&&!_saf)
o.setDisplay(false)
o.imwpResize(w,h)
if (d&_moz&&!_saf)
o.setDisplay(true)
}
function TextComboWidget_add(s,val,selected)
{
var o=this
var item=o.menu.addCheck(o.id+"_it_"+(o.counter++),s,TextComboWidget_internalCB)
item.val=""+val
item.parCombo=o
item.isComboVal=true
if ((o.selectedItem==null)||selected)
o.selectItem(item)
}
function TextComboWidget_addSeparator()
{
this.menu.addSeparator()
}
function TextComboWidget_addMenuItem(id,text,cb,icon,dx,dy,disabled,disDx,disDy)
{
this.menu.add(id,text,cb,icon,dx,dy,disabled,disDx,disDy)
}
function TextComboWidget_setDisabled(d)
{
var o=this
o.text.setDisabled(d)
o.arrow.setDisabled(d)
o.menu.setDisabled(d)
o.disabled=d
}
function TextComboWidget_isDisabled()
{
var o=this
return o.disabled
}
function TextComboWidget_select(idx)
{
var o=this,item=o.getItemByIndex(idx)
if (item)
o.selectItem(item)
}
function TextComboWidget_setItemDisabled(idx,disabled)
{
var o=this,item=o.getItemByIndex(idx)
if (item)
item.setDisabled(disabled)
}
function TextComboWidget_getSelection()
{
var o=this,it=o.selectedItem
var txt=o.text.getValue()
if (it)
return {index:o.getItemIndex(it),value:it.val}
else
return {index:-1,value:txt}
}
function TextComboWidget_valueSelect(v)
{
v=""+v
var o=this,items=o.menu.items,len=items.length
for (var i=0;i<len;i++)
{
var it=items[i]
if ((it.isComboVal)&&(it.val==v))
{
o.selectItem(it)
return
}
}
o.text.setValue(v)
}
function TextComboWidget_valueShow(v,show)
{
v=""+v
var o=this,items=o.menu.items,len=items.length
for (var i=0;i<len;i++)
{
var it=items[i]
if ((it.isComboVal)&&(it.val==v))
{
it.show(show)
return
}
}
o.text.setValue(v)
o.text.show(show)
}
function TextComboWidget_setUndefined(u)
{
var o=this
if (u)
o.selectItem(null)
}
function TextComboWidget_setContentEditable(d)
{
var o=this
o.text.setContentEditable(d)
}
function TextComboWidget_isContentEditable()
{
var o=this
return o.text.isContentEditable()
}
function TextComboWidget_del(i)
{
if (this.menu)
this.menu.remove (i)
}
function TextComboWidget_selectItem(item)
{
var o=this
if (o.selectedItem)
o.selectedItem.check(false)
if (item)
{
o.val=item.val
o.text.setValue(/*o.disabled?"":*/item.text)
o.selectedItem=item
item.check(true)
}
else
{
o.val=null
o.text.setValue("")
o.selectedItem=null
}
}
function TextComboWidget_getItemByIndex(idx)
{
var items=this.menu.items
return ((idx>=0)&&(idx<items.length))?items[idx]:null
}
function TextComboWidget_getItemIndex(item)
{
var items=this.menu.items,len=items.length,j=0
for (var i=0;i<len;i++)
{
var it=items[i]
if (it.isComboVal)
{
if (it.id==item.id)
return j
j++
}
}
return -1
}
function TextComboWidget_changeCB()
{
var p=this.par
var b=true
if (p.checkCB)
b=p.checkCB()
if (!b)
return
if (p.changeCB)
p.changeCB()
}
function TextComboWidget_enterCB()
{
var p=this.par
// 1st : Unselect any selected element from the menu
if (p.selectedItem)
{
p.selectedItem.check(false)
p.selectedItem=null
}
//2nd : Call the callBack function
var b=true
if (p.checkCB)
b=p.checkCB()
if (!b)
return
if (p.changeCB) 
p.changeCB()
}
function TextComboWidget_checkCB()
{
var p=this.par
p.selectTextValue ()
if (p.checkCB) 
p.checkCB()
}
function TextComboWidget_hideCB()
{
var o=this.parIcon
TextComboOutCB(o.index)
}
function TextComboWidget_arrowClickCB()
{
this.par.clickCB()
}
function TextComboWidget_clickCB()
{
var o=this,l=o.layer
o.menu.show(!o.menu.isShown(),getPosScrolled(l).x,getPosScrolled(l).y+o.getHeight()+1,null,null,o)
TextComboOverCB(o.index)
}
function TextComboWidget_getTextValue()
{
return this.text.getValue()
}
function TextCombo_OverCB()
{
TextComboOverCB(getWidget(this).index)
return true
}
function TextComboOverCB(i)
{
var o=_allBOIconsMenus[i]
IconWidget_overCB(o.arrow.index)// Re-use method from IconWidget class
}
function TextCombo_OutCB(i)
{
TextComboOutCB(getWidget(this).index)
}
function TextComboOutCB(i)
{
var o=_allBOIconsMenus[i]
if (!o.menu.isShown())
IconWidget_outCB(o.arrow.index)// Re-use method from IconWidget class
else
IconWidget_overCB(o.arrow.index)
}
function TextComboWidget_internalCB()
{
var o=this,c=o.parCombo
c.selectItem(o)
if (c.changeCB)
c.changeCB()
}
function TextComboWidget_keyUpCB()
{
}
function TextComboWidget_SelectTextValue ()
{
var o=this,l=""+o.text.getValue(),items=o.menu.items,len=items.length
// Find and select a matching item if any
for (var i=0;i<len;i++)
{
var it=items[i]
if ((it.isComboVal)&&(""+it.text==l))
{
o.selectItem(it)
return
}
}
// If we arrive here, we did'nt found any matching item
// Deselect if any item is already selected
// Don't setUndefined because it update the TextComboWidget text
if (o.selectedItem)
o.selectedItem.check(false)
}
function newIconScrollMenuWidget(id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy,
changeCB,multi,width,lines,tooltip,dblClickCB,keyUpCB,showLabel,label,convBlanks,beforeShowCB)
{
var o=newIconMenuWidget(id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy)
o.menu=newScrollMenuWidget("iconMenu_menu_"+id,changeCB,multi,width,lines,tooltip,dblClickCB,keyUpCB,showLabel,label,convBlanks,beforeShowCB)
o.add=IconScrollMenu_add
return o
}
function IconScrollMenu_add(s,val,sel,id)
{
this.menu.add(s,val,sel,id)
}
function newPaneWidget(id,tooltip,w,h,x,y,changeCB)
{
var o=newWidget(id)
// Private Fields
o.items=new Array
o.w=(w==null?100:w)
o.h=(w==null?400:h)
o.x=x
o.y=y
o.changeCB=changeCB
o.pal = newPaletteWidget("palette_"+id);
o.pal.vertPadding=0
o.panes = new Array;
o.panemenu = null
o.selection=-1
o.topH=_dtd4?17:18
o.bottomH=24+(_dtd4?2:0)
o.tooltip=tooltip
o.iframe=newWidget("_iframe"+o.id)
o.title=newWidget("paneTitleB_"+o.id)
// Methods
o.add = PaneWidget_add
o.valueSelect = PaneWidget_valueSelect
o.valueShow = PaneWidget_valueShow
o.getSelection = PaneWidget_getSelection
// Standard widget methods
o.getHTML=PaneWidget_getHTML
o.oldInit=o.init
o.init=PaneWidget_init
o.oldResize=o.resize
o.resize=PaneWidget_resize
o.getFrame=PaneWidget_getFrame
o.resizePalette=PaneWidget_resizePalette
return o
}
function PaneWidget_getSelection()
{
var o=this,i=o.selection
if (i==-1)
return null
else
return {index:i,value:o.panes[i].value}
}
function PaneWidget_resize(w,h)
{
var o=this
o.w=w
o.h=h
o.oldResize(Math.max(0,w+(_dtd4?-2:0)),Math.max(0,h+(_dtd4?-2:0)))
o.pal.resize(Math.max(0,w-2))
o.title.resize(Math.max(0,w-2))
var parCss=o.iframe.layer.parentNode.style
if (w!=null)
parCss.width=""+Math.max(0,o.w-2)+"px"
if (h!=null)
parCss.height=""+Math.max(0,o.h-(o.topH+o.bottomH)-2)+"px"
o.iframe.resize(Math.max(0,o.w-2),Math.max(0,o.h-(o.topH+o.bottomH)-(_dtd4?2:3)))
o.resizePalette()
}
function PaneWidget_resizePalette()
{
var o=this,nbButton=0,panes=o.panes,len=panes.length;maxVisible=Math.floor((o.w-22)/24),j=0,hasMenu=false
for (var i=0;i<len;i++)
{
var pane=panes[i],isVis=j<maxVisible,menu=o.menu
pane.button.setDisplay(pane.shown&&(isVis))
if (menu)
{
var menuItem=menu.getItem(i),show=pane.shown&&(!isVis)
menuItem.show(show)
if (show)
hasMenu=true
}
if (pane.shown) j++
}
o.panemenu.setDisplay(hasMenu)
}
function PaneWidget_init()
{
var o=this
o.oldInit()
o.pal.init()
o.panemenu.init()
o.iframe.init()
o.title.init()
}
function PaneWidget_getFrame()
{
return eval("_curWin.frames._iframe"+this.id)
}
function PaneWidget_valueShow(value,show)
{
var o=this,panes=o.panes,index=-1,pane=null
for (i=0;i<panes.length;i++)
{
pane=panes[i]
if (pane.value==value)
{
index=i
break
}
}
if (pane)
{
o.resizePalette()
pane.menuItem.show(show)
pane.shown=show
}
}
function PaneWidget_valueSelect(value)
{
var o=this,panes=o.panes,index=-1,pane=null,oldSel=o.selection
for (i=0;i<panes.length;i++)
{
pane=panes[i]
if (pane.value==value)
{
index=i
break
}
}
// Deselect prev
if (o.selection>=0)
{
var oldPane=panes[o.selection]
if (oldPane)
{
oldPane.menuItem.check(false)
oldPane.button.check(false)
}
}
if (index>=0)
{
pane.menuItem.check(true)
pane.button.check(true)
o.title.setHTML(convStr(pane.name))
var hasChanged=(o.selection!=index)
o.selection=index
if (hasChanged)
{
if ((oldSel!=-1)&&(o.changeCB))
o.changeCB()
frameNav("_iframe"+o.id,pane.url,false,_curWin)
}
}
else
{
if (o.selection!=-1)
{
o.selection=-1
frameNav("_iframe"+o.id,_skin+"../../empty.html",false,_curWin)
o.title.setHTML('')
}
}
}
function PaneWidget_getHTML()
{
var o=this,panes=o.panes,pal=o.pal
if (o.panemenu == null)
{
pal.beginRightZone()
var pm=o.panemenu = newIconWidget("__panemenu_"+o.id,_skin+'panemenu.gif',PaneWidget_buttonCB,null,null,9,16);
pm.isPaneMenu=true
pm.parPane=o
pal.add(pm)
var menu=o.menu = newMenuWidget("panemenu_"+o.id);
for (var i=0; i<panes.length; i++)
{
var pane=panes[i]
var m=menu.addCheck("m__"+pane.button.id,pane.name,PaneWidget_buttonCB,pane.icon,pane.button.dx,pane.button.dy)
m.paneElement=pane
pane.menuItem=m
}
}
o.resizePalette()
var absSty = ((o.x!=null)&&(o.y!=null)) ? "position:absolute;left:"+o.x+"px;top:"+o.y+"px;" : ""
return '<div class="treeZone" id="'+o.id+'" style="'+absSty+'overflow:hidden;width:'+(o.w+(_dtd4?-2:0))+'px;height:'+(o.h+(_dtd4?-2:0))+'px">' +
'<div class="titlepane" id="paneTitleB_'+o.id+'" style="overflow:hidden;background-image:url('+_skin+'panetitle.gif);height:'+(o.topH)+'px"></div>'+
'<div class="treeZone" style="border-width:0px;border-bottom-width:1px;height:'+(Math.max(0,o.h-(o.topH+o.bottomH)-2))+'px"><iframe style="margin:0px;width:'+Math.max(0,o.w-2)+'px;height:'+(Math.max(0,o.h-(o.topH+o.bottomH)-(_dtd4?2:3)))+'px" id="_iframe'+o.id+'" name="_iframe'+o.id+'" title="'+convStr(o.tooltip)+'" frameborder="0" src="'+_skin+'../../empty.html"></iframe></div>'+
'<div class="panelzone" id="paneTools_'+o.id+'" style="height:'+o.bottomH+'px">'+pal.getHTML()+'</div>' +
'</div>'
}
function PaneWidget_add(value,name,icon,dx,dy,url)
{
newPaneElement(this,this.id+"_item_"+value,value,name,icon,dx,dy,url)
}
function PaneWidget_buttonCB()
{
var o=this
if (o.isPaneMenu)
{
var m = o.parPane.panemenu,l = m.layer
o.parPane.menu.show(true,getPosScrolled(l).x+m.getWidth()+1,getPosScrolled(l).y+16)
}
else
{
var paneE = o.paneElement
paneE.par.valueSelect(paneE.value)
}
}
function newPaneElement(par,id,value,name,icon,dx,dy,url)
{
var o=new Object
o.par=par
par.panes[par.panes.length]=o
o.value=value
o.name=name
o.icon=icon
o.button=newIconCheckWidget(id,icon,PaneWidget_buttonCB,null,name,16,16,dx,dy);
o.button.paneElement=o
o.url=url
par.pal.add(o.button)
o.shown=true
}
function newButtonMenuWidget(id,label,width,tooltip,beforeShowCB,tabIndex)
{
    var o=newButtonWidget(id,label,ButtonMenuWidget_clickCB,width,null,tooltip,tabIndex,0,_skin+"menus.gif",7,16,0,81,true,0,97)
    o.menu = newMenuWidget("buttonMenu_menu_"+id,null,beforeShowCB);
    o.getMenu=IconMenuWidget_getMenu
    return o;
}
function ButtonMenuWidget_clickCB()
{
      var o=this,l=o.layer;
      o.menu.show(!o.menu.isShown(),getPosScrolled(l).x,getPosScrolled(l).y+o.getHeight(),null,null,o)
}
_scrollW=20
_scrollH=20
_margin=2
function newIconTableWidgetElem(parid,id,src,clickCB,text,alt,w,h,dx,dy,disDx,disDy,maxW,maxH)
{
var o=newIconRadioWidget(id,src,clickCB,text,alt,"grp_"+parid,w,h,dx,dy,disDx,disDy)
// Properties
o.txtNoPadding=true
o.txtAlign='center'
o.margin=0
o.maxW=maxW
o.maxH=maxH
o.imgW=w+o.border
o.imgH=h+o.border
o.txtW=maxW-2*_margin
// Methods
o.oldGetHTML=o.getHTML
o.getHTML=IconTableWidgetElem_getHTML
o.oldResize=o.resize
o.resize=IconTableWidgetElem_resize
return o
}
function IconTableWidgetElem_getHTML()
{
var o=this,imgCode=o.src?'<div style="overflow:hidden;width:'+o.imgW+'px;height:'+o.imgH+'">'+simpleImgOffset(o.src,o.w,o.h,o.dis?o.disDx:o.dx,o.dis?o.disDy:o.dy,'IconImg_'+o.id,null,o.alt,'margin:2px;cursor:'+(o.clickCB ? (!o.acceptClick() ? 'default' : _hand):'default'))+o.extraHTML+'</div>':'<div class="iconText" style="width:1px;"></div>'
var txtAtt='style="white-space:normal;'
txtAtt+='text-overflow:ellipsis;overflow:hidden;width:'+o.txtW+'px;'
txtAtt+='"'
var d=o.disp?"":"display:none;"
return '<table style="'+d+'width:'+o.maxW+'px;height:'+o.maxH+'margin:0px" id="'+o.id+'" class="' + o.nocheckClass + '" cellspacing="0" cellpadding="0" border="0"><tr valign="middle">'+
'<td align="center">'+ ((o.clickCB&&_ie)?lnk(imgCode,null,null,null, ' tabIndex="-1"' ):imgCode)+'</td></tr><tr>'+
(o.text?'<td align="center"><div id="IconImg_Txt_'+o.id+'" class="iconTableText'+(o.dis?"Dis":"")+'" '+txtAtt+'>'+convStr(o.text)+'</div></td>':'')+
'</tr></table>'
}
function IconTableWidgetElem_resize(w,h)
{
var o=this
if (w!=null)
o.oldResize(w-2*_margin,null)
if (h!=null)
o.oldResize(null,h-2*_margin)
}
function newIconTableWidget(id,horiz,w,h)
{
var o=newScrolledZoneWidget(id,0,0,w,h/*,'iconTableZone'*/)
// Properties
o.horiz=horiz
o.padding=2
o.icnTblLayer=null
o.contCSS=null
o.elems=new Array
// Methods
o.oldInit=o.init
o.init=IconTableWidget_init
o.oldResize2=o.resize
o.resize=IconTableWidget_resize
o.getHTML=IconTableWidget_getHTML
o.add=IconTableWidget_add
o.select=IconTableWidget_select
return o
}
function IconTableWidget_init()
{
var o=this, len=o.elems.length
//o.oldInit()
for (var i=0;i<len;i++)
o.elems[i].init()
o.icnTblLayer=getLayer(_codeWinName+'icnTbl'+o.id)
o.contCSS=o.icnTblLayer.style
}
function IconTableWidget_resize(w,h)
{
var o=this
//o.oldResize2(w,h)
if (w!=null)
{
if (o.contCSS!=null)
o.contCSS.width=''+w+'px'
}
if (h!=null)
{
if (o.contCSS!=null)
o.contCSS.height=''+h+'px'
}
}
function IconTableWidget_getHTML()
{
var o=this, s=''
s+='<table id="'+_codeWinName+'icnTbl'+o.id+'" width="'+o.w+'" height="'+o.h+'px" class="iconTableZone" cellspacing="0" cellpadding="0"><tbody>'
s+= '<tr valign="top"><td>'
s+='<table cellspacing="0" cellpadding="'+o.padding+'px"><tbody>'
if (o.horiz)
{
s+= '<tr>'
for (var i=0;i<o.elems.length;i++)
{
s+= '<td align="center">'
s+= o.elems[i].getHTML()
s+= '</td>'
}
s+= '</tr>'
}
else
{
for (var i=0;i<o.elems.length;i++)
{
s+= '<tr><td align="center">'
s+= o.elems[i].getHTML()
s+= '</td></tr>'
}
}
s+='</tbody></table>'
s+= '</td><tr>'
s+='</tbody></table>'
return s
}
function IconTableWidget_add(src,clickCB,text,alt,w,h,dx,dy,disDx,disDy)
{
var o=this, len=o.elems.length, icn=null
var maxW=o.horiz?o.h:o.w
maxW-=2*_margin
var maxH=maxW
icn=newIconTableWidgetElem(o.id,"iconTable_icon_"+len+o.id,src,clickCB,text,alt,
w,h,dx,dy,disDx,disDy,maxW,maxH)
icn.par=this
o.elems[len]=icn
}
function IconTableWidget_select(index)
{
var o=this
var e=o.elems[index]
if ( e==null )
return
e.check(true)
}
function newIconBordersMenuWidget(id,clickCB,beforeShowCB, alt)
{
var o=newIconMenuWidget(id,_skin+'../borders.gif',IconBordersMenuWidget_iconInternalClickCB,null,alt,16,16,0,0,0,16,false,beforeShowCB,_menuType_border)
// properties
o.selectedBorder=new Object
o.selectedBorder.top=0
o.selectedBorder.bot=0
o.selectedBorder.left=0
o.selectedBorder.right=0
o.lastSelectedBorder=new Object
o.lastSelectedBorder.top=0
o.lastSelectedBorder.bot=0
o.lastSelectedBorder.left=0
o.lastSelectedBorder.right=0
o.bordersClickCB=clickCB
// methods
o.getSelectedBorder=IconBordersMenuWidget_getSelectedBorder
o.getLastSelectedBorder=IconBordersMenuWidget_getLastSelectedBorder
return o
}
function IconBordersMenuWidget_getSelectedBorder()
{
var o=this
return o.selectedBorder
}
function IconBordersMenuWidget_getLastSelectedBorder()
{
var o=this
return o.lastSelectedBorder
}
function IconBordersMenuWidget_iconInternalClickCB()
{
var o=this
o.selectedBorder.top=o.lastSelectedBorder.top
o.selectedBorder.bot=o.lastSelectedBorder.bot
o.selectedBorder.left=o.lastSelectedBorder.left
o.selectedBorder.right=o.lastSelectedBorder.right
if (o.bordersClickCB)
o.bordersClickCB()
}
function IconBordersMenuWidget_internalClickCB()
{
var o=this
var top=bot=left=right=-1
switch (o.id)
{
case "border_0":
top=bot=left=right=0
break;
case "border_1":
bot=1
break;
case "border_2":
left=1
break;
case "border_3":
right=1
break;
case "border_4":
bot=2
break;
case "border_5":
bot=3
break;
case "border_6":
top=bot=1
break;
case "border_7":
top=1;bot=2
break;
case "border_8":
top=1;bot=3
break;
case "border_9":
top=bot=left=right=1
break;
case "border_10":
top=bot=left=right=2
break;
case "border_11":
top=bot=left=right=3
break;
case "border_12":
top=bot=left=right=-2
break;
}
// Set the selection
var pp=o.par.parIcon
pp.selectedBorder.top=top
pp.selectedBorder.bot=bot
pp.selectedBorder.left=left
pp.selectedBorder.right=right
if (o.id!="border_12")
{
// Update the menu icon
pp.icon.changeImg(16*o.idx,0)
// Save the previous border
pp.lastSelectedBorder.top=top
pp.lastSelectedBorder.bot=bot
pp.lastSelectedBorder.left=left
pp.lastSelectedBorder.right=right
}
// Call the menu callback
if (pp.bordersClickCB)
pp.bordersClickCB()
}
function newIntComboWidget(id,maxChar,tooltip,w,changeCB,checkCB,beforeShowCB,formName)
{
var o=newTextComboWidget(id,maxChar,tooltip,w,changeCB,checkCB,beforeShowCB,formName,"int")
o.setMin=IntComboWidget_setMin
o.setMax=IntComboWidget_setMax
return o
}
function IntComboWidget_setMin(min)
{
if (!isNaN(min))
this.text.setMin(min)
}
function IntComboWidget_setMax(max)
{
if (!isNaN(max))
this.text.setMax(max)
}

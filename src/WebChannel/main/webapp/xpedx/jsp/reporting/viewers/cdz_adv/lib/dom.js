if (window._DHTML_LIB_DOM_JS_LOADED==null)
{
    _DHTML_LIB_DOM_JS_LOADED=true
    _ie=(document.all!=null)?true:false 
    _dtd4=!_ie||(document.compatMode!='BackCompat') 
    _dom=(document.getElementById!=null)?true:false 
    _moz=_dom&&!_ie 
    _show='visible'
    _hide='hidden'
    _hand=_ie?"hand":"pointer"
    _appVer=navigator.appVersion.toLowerCase();
    _mac=(_appVer.indexOf('macintosh')>=0)||(_appVer.indexOf('macos')>=0);
    _userAgent=navigator.userAgent?navigator.userAgent.toLowerCase():null
    _webKit=(_userAgent.indexOf("safari")>=0)||(_userAgent.indexOf("applewebkit")>=0)
    _saf=_moz&&_mac&&_webKit 
    _saf2=_saf&&(_userAgent.indexOf("version")<0) 
    _winSaf=_moz&&_webKit    
    _opera = (_userAgent.indexOf('opera') != -1);     
    if (_opera)
    {
    _moz=true
    _dtd4=true
    }
    _ctrl=0
    _shift=1
    _alt=2
    _meta=3
    _ie6 = _ie&&(_appVer.indexOf("msie 5")<0)
    _ie7 = _ie6&&(_appVer.indexOf("msie 6")<0)
    _curDoc=document
    _curWin=self
    _tooltipWin=self
    _tooltipDx=0
    _tooltipDy=0
    _codeWinName="_CW"
    _leftBtn=(_ie||_saf2)?1:0
    _preloadArr=new Array
    _widgets=new Array
    _resizeW=_ie6?"col-resize":(_webKit?"col-resize":"EW-resize")
    _resizeH=_ie6?"row-resize":(_webKit?"row-resize":"NS-resize")
    _ddData=new Array
    _dontNeedEncoding=null;
    _thex=null;
    _small=false; 
    ButtonWidget_inst=new Array
ButtonWidget_currInst=0
}
function debuggingLogger(s,x,y,w,h)
{
var cons=_curDoc.getElementById("debuggingConsoleLyr")
if (cons==null)
{
x = (x==null?0:x)
y = (y==null?0:y)
w = (w==null?300:w)
h = (h==null?100:h)
append(_curDoc.body,'<textarea id="debuggingConsoleLyr" style="font-family:arial;font-size:10px;z-index:5000;position:absolute;top:'+y+'px;left:'+x+'px;width:'+w+'px;height:'+h+'px;overflow:auto;border:solid 1px black"></textarea>')
cons=_curDoc.getElementById("debuggingConsoleLyr")
}
cons.value=cons.value+"" + s + "\n"
}
function initDomWithLangPath(skinPath,lang,langPath,curWin,codeUniqueName)
{
if (window._InitDomCalled==null)
{
_InitDomCalled=true
_skin=skinPath;
_lang=lang;
if (curWin)
{
_curWin=curWin
_curDoc=curWin.document
}
_tooltipWin=_curWin
if (codeUniqueName)
_codeWinName="_CW"+codeUniqueName
_curWin[_codeWinName]=self
_dtd4=!_ie||(_curDoc.compatMode!='BackCompat') 
includeScript(langPath + "labels.js")
}
}
function initDom(skinPath,lang,curWin,codeUniqueName)
{
if (lang.length > 2)
{
var langShort = lang.indexOf("zh")
if (langShort == 0)
{
if (lang == "zh_SG")
lang = "zh_CN";
else if (lang == "zh_HK")
lang = "zh_TW";
else if (lang == "zh_MO")
lang = "zh_TW";
}
else
lang = lang.slice(0,2)
}
var langPath = skinPath + "../../language/" + lang + "/"
initDomWithLangPath(skinPath,lang,langPath,curWin,codeUniqueName)
}
function styleSheetName()
{
switch(_lang)
{
case 'ja':
case 'ko':
case 'zh':
return 'style_fe'
default:
return 'style'
break;
}
}
function styleSheet()
{
if (_curWin.location.href!=window.location.href)
dynStyleSheet(_curWin)
else
includeCSS(styleSheetName())
}
function includeCSS(css,noskin)
{
var url=""
if( noskin )
url=_skin+'../'+css
else
url=_skin+css
url+='.css'
_curDoc.write('<li'+'nk id="dhtmlLibCurrCss" rel="stylesheet" type="text/css" href="'+url+'">')
}
function dynStyleSheet(win)
{
if (win)
{
var doc  = win.document
var link = doc.getElementById("dhtmlLibCurrCss")
var href = _skin+styleSheetName()+".css"
if (link)
{
if (link.href!=href)
link.href=href
}
else
{
var arrHead = doc.getElementsByTagName("HEAD")
if (arrHead.length > 0)
{
var head = arrHead[0]
link = doc.createElement('link')
link.href=href
link.id="dhtmlLibCurrCss"
link.rel="stylesheet"
link.type="text/css"
head.appendChild(link)
}
}
}
}
function isSmallScreen()
{
var isSmall = false;
try
{
isSmall = screen.height<=768
}
catch(exp)
{
}
return isSmall;
}
function isLayerDisplayed(lyr)
{
var css=lyr?lyr.style:null
if(css)
{
if(css.display == "none" || css.visibility=="hidden")
return false
else
{
var par=lyr.parentNode
if(par!=null)
return isLayerDisplayed(par)
else
return true
}
}
else
return true;
}
function safeSetFocus(lyr)
{
if (lyr && lyr.focus && isLayerDisplayed(lyr))
{
try
{
lyr.focus()
}
catch (exc)
{
}
}
}
function newWidget(id)
{
var o=new Object
o.id=id
o.layer=null
o.css=null
o.getHTML=Widget_getHTML
o.beginHTML=Widget_getHTML
o.endHTML=Widget_getHTML
o.write=Widget_write
o.begin=Widget_begin
o.end=Widget_end
o.init=Widget_init
o.move=Widget_move
o.resize=Widget_resize
o.setBgColor=Widget_setBgColor
o.show=Widget_show
o.getWidth=Widget_getWidth
o.getHeight=Widget_getHeight
o.setHTML=Widget_setHTML
o.setDisabled=Widget_setDisabled
o.focus=Widget_focus
o.setDisplay=Widget_setDisplay
o.isDisplayed=Widget_isDisplayed
o.appendHTML=Widget_appendHTML
o.setTooltip=Widget_setTooltip
o.initialized=Widget_initialized
o.widx=_widgets.length
_widgets[o.widx]=o
return o
}
function Widget_appendHTML()
{
append(_curDoc.body,this.getHTML())
}
function Widget_getHTML()
{
return ''
}
function Widget_write(i)
{
_curDoc.write(this.getHTML(i))
}
function Widget_begin()
{
_curDoc.write(this.beginHTML())
}
function Widget_end()
{
_curDoc.write(this.endHTML())
}
function Widget_init()
{
var o=this
o.layer=getLayer(o.id)
o.css=o.layer.style
o.layer._widget=o.widx
if (o.initialHTML)
o.setHTML(o.initialHTML)
}
function Widget_move(x,y)
{
    c=this.css;
if(c!=null)
{
    if (x!=null){if (_moz) c.left=""+x+"px";else c.pixelLeft=x}if (y!=null){if (_moz) c.top=""+y+"px";else c.pixelTop=y}
    }    
}
function Widget_focus()
{
safeSetFocus(this.layer)
}
function Widget_setBgColor(c)
{
this.css.backgroundColor=c
}
function Widget_show(show)
{
this.css.visibility=show?_show:_hide
}
function Widget_getWidth()
{
return this.layer.offsetWidth
}
function Widget_getHeight()
{
return this.layer.offsetHeight
}
function Widget_setHTML(s)
{
var o=this
if (o.layer)
o.layer.innerHTML=s
else
o.initialHTML=s
}
function Widget_setDisplay(d)
{
if (this.css)
this.css.display=d?"":"none"
}
function Widget_isDisplayed()
{
if(this.css.display == "none")
return false
else
return true
}
function Widget_setDisabled(d)
{
if (this.layer)
this.layer.disabled=d
}
function Widget_resize(w,h)
{
var o=this
if (o.css)
{
if (w!=null)
o.css.width=''+(Math.max(0,w))+'px';
if (h!=null)
o.css.height=''+(Math.max(0,h))+'px';
}
}
function Widget_setTooltip(tooltip)
{
this.layer.title=tooltip
}
function Widget_initialized()
{
return this.layer!=null
}
function newGrabberWidget(id,resizeCB,x,y,w,h,isHori,buttonCB,tooltip)
{
o=newWidget(id)
o.resizeCB=resizeCB
o.x=x
o.y=y
o.w=w
o.h=h
o.dx=0
o.dy=0
o.min=null
o.max=null
o.isHori=isHori
o.preloaded=new Image
o.preloaded.src=_skin+"../resizepattern.gif"
o.buttonCB=buttonCB
o.allowGrab=true
o.collapsed=false
o.isFromButton=false
o.showGrab=GrabberWidget_showGrab
o.setCollapsed=GrabberWidget_setCollapsed
o.tooltipButton=tooltip
o.getHTML=GrabberWidget_getHTML
o.enableGrab=GrabberWidget_enableGrab
o.setMinMax=GrabberWidget_setMinMax
if (window._allGrabbers==null)
window._allGrabbers=new Array
o.index=_allGrabbers.length
_allGrabbers[o.index]=o
o.buttonLyr=null
o.setButtonImage=GrabberWidget_setButtonImage
o.getImgOffset=GrabberWidget_getImgOffset
return o
}
function GrabberWidget_setCollapsed(collapsed,tooltip)
{
this.collapsed=collapsed
this.setButtonImage(false,tooltip)
}
function GrabberWidget_getImgOffset(isRollover)
{
var o=this
if (o.isHori)
{
o.dx=(o.collapsed?12:0)+(isRollover?6:0)
o.dy=0
}
else
{
o.dy=(o.collapsed?12:0)+(isRollover?6:0)
o.dx=0
}
}
function GrabberWidget_setButtonImage(isRollover,tooltip)
{
var o=this
o.getImgOffset(isRollover)
o.tooltipButton=tooltip
if (o.layer)
{
if (o.buttonLyr==null)
o.buttonLyr=getLayer("grabImg_"+o.id)
if (o.buttonLyr)
{
changeSimpleOffset(o.buttonLyr,o.dx,o.dy,null,tooltip)
}
}
}
function GrabberWidget_enableGrab(bEnable)
{
var o=this
o.allowGrab=bEnable
if (o.css)
o.css.cursor=o.allowGrab?(o.isHori?_resizeW:_resizeH):"default"
}
function GrabberWidget_getHTML()
{
var o=this,ho=o.isHori
var cr=o.allowGrab?(ho?_resizeW:_resizeH):"default"
var moveableCb='onselectstart="return false" ondragstart="return false" onmousedown="'+_codeWinName+'.GrabberWidget_down(event,\''+o.index+'\',this);return false;"'
var imgG=_ie?('<img onselectstart="return false" ondragstart="return false" onmousedown="'+_codeWinName+'.eventCancelBubble(event)" border="0" hspace="0" vspace="0" src="'+_skin+'../transp.gif" id="modal_'+o.id+'" style="z-index:10000;display:none;position:absolute;top:0px;left:0px;width:1px;height:1px;cursor:'+cr+'">'):('<div onselectstart="return false" ondragstart="return false" onmousedown="'+_codeWinName+'.eventCancelBubble(event)" border="0" hspace="0" vspace="0" id="modal_'+o.id+'" style="z-index:10000;display:none;position:absolute;top:0px;left:0px;width:1px;height:1px;cursor:'+cr+'"></div>')
var button=o.buttonCB?(_skin+(ho?"h":"v")+"grab.gif"):""
if (button)
{
button=simpleImgOffset(button,ho?6:50,ho?50:6,o.dx,o.dy,"grabImg_"+o.id,'onmouseover="'+_codeWinName+'.GrabberWidget_buttonover(event,\''+o.index+'\',this);return false;" onmouseout="'+_codeWinName+'.GrabberWidget_buttonout(event,\''+o.index+'\',this);return false;" onmousedown="'+_codeWinName+'.GrabberWidget_button(event,\''+o.index+'\',this);return false;"',o.tooltipButton,'cursor:'+_hand+';')
}
return getBGIframe('grabIframe_'+o.id)+imgG+'<table cellpadding="0" cellspacing="0" border="0" '+moveableCb+' id="'+o.id+'" style="overflow:hidden;position:absolute;left:'+o.x+'px;top:'+o.y+'px;width:'+o.w+'px;height:'+o.h+'px;cursor:'+cr+'"><tr><td align="center" valign="middle">'+button+'</td></table>'
}
function GrabberWidget_setMinMax(min,max)
{
this.min=min
this.max=max
}
function GrabberWidget_button(e,index,lyr)
{
var o=_allGrabbers[index]
o.isFromButton=true
lyr.onmouseup=eval('_curWin.'+_codeWinName+'.GrabberWidget_buttonup')
}
function GrabberWidget_buttonover(e,index,lyr)
{
var o=_allGrabbers[index]
o.setButtonImage(true)
}
function GrabberWidget_buttonout(e,index,lyr)
{
var o=_allGrabbers[index]
o.setButtonImage(false)
}
function GrabberWidget_buttonup(e)
{
GrabberWidget_up(e)
}
function GrabberWidget_showGrab()
{
var o=this,mod=o.mod,ifr=o.iframe,stl=o.layer.style,st=mod.style
ifr.setDisplay(true)
}
function GrabberWidget_down(e,index,lyr)
{
var o=_allGrabbers[index]
window._theGrabber=o
if (o.mod==null)
{
o.mod=getLayer('modal_'+o.id)
o.iframe=newWidget('grabIframe_'+o.id)
o.iframe.init()
}
o.mod.onmousemove=eval('_curWin.'+_codeWinName+'.GrabberWidget_move')
o.mod.onmouseup=eval('_curWin.'+_codeWinName+'.GrabberWidget_up')
o.grabStartPosx=parseInt(lyr.style.left)
o.grabStartPosy=parseInt(lyr.style.top)
o.grabStartx=eventGetX(e)
o.grabStarty=eventGetY(e)
var mod=o.mod,ifr=o.iframe,stl=o.layer.style,st=mod.style
stl.backgroundImage='url(\''+_skin+'../resizepattern.gif\')'
o.prevZ=stl.zIndex
stl.zIndex=9999
ifr.css.zIndex=9998
st.width='100%'
st.height='100%'
mod.style.display="block"
var p=getPos(o.layer)
ifr.move(p.x,p.y)
ifr.resize(o.getWidth(),o.getHeight())
if (!o.isFromButton)
o.showGrab()
return false
}
function GrabberWidget_move(e)
{
var o=_theGrabber,lyr=o.layer,mod=o.mod
if (o.isFromButton)
{
if (o.isHori)
{
var x = eventGetX(e), ox=o.grabStartx
if ((x < ox - 3) || (x > ox + 3))
o.isFromButton = false
}
else
{
var Y = eventGetY(e), oy=o.grabStarty
if ((y < oy - 3) || (y > oy + 3))
o.isFromButton = false
}
if (!o.isFromButton)
o.showGrab()
}
if (!o.isFromButton)
{
if (o.allowGrab)
{
var x=o.isHori?Math.max(0,o.grabStartPosx-o.grabStartx+eventGetX(e)):null
var y=o.isHori?null:Math.max(0,o.grabStartPosy-o.grabStarty+eventGetY(e))
if (o.isHori)
{
if (o.min!=null) x=Math.max(x,o.min)
if (o.max!=null) x=Math.min(x,o.max)
}
else
{
if (o.min!=null) y=Math.max(y,o.min)
if (o.max!=null) y=Math.min(y,o.max)
}
eventCancelBubble(e)
o.move(x,y)
getPos(o.layer)
if (o.buttonCB)
{
var bCss=o.buttonLyr.style
if (bCss.display!="none")
bCss.display="none"
}
o.iframe.move(x,y)
}
}
}
function GrabberWidget_up(e)
{
var o=_theGrabber,lyr=o.layer,mod=o.mod,stl=lyr.style
stl.backgroundImage=''
stl.zIndex=o.prevZ
var ifr=o.iframe
ifr.move(-100,-100)
ifr.resize(1,1)
ifr.setDisplay(false)
eventCancelBubble(e)
var st=mod.style
st.display="none"
st.width='0px'
st.height='0px'
if (o.buttonCB)
o.buttonLyr.style.display=""
if (o&&(o.isFromButton))
{
if (o.buttonCB)
o.buttonCB()
o.isFromButton=false
}
if (o.allowGrab&&(!o.isFromButton))
{
if (o.resizeCB)
o.resizeCB(parseInt(lyr.style.left),parseInt(lyr.style.top))
}
}
function newButtonWidget(id,label,cb,width,hlp,tooltip,tabIndex,margin,url,w,h,dx,dy,imgRight,disDx,disDy)
{
var o=newWidget(id)
o.label=label
o.cb=cb
o.width=width
o.hlp=hlp
o.tooltip=tooltip
o.tabIndex=tabIndex
o.isGray=false
o.isDefault=false
o.txt=null
o.icn=null
o.btn=null
o.margin=margin?margin:0
o.extraStyle=""
if (url)
{
o.url=url
o.w=w
o.h=h
o.dx=dx
o.dy=dy
o.disDx=(disDx!=null)?disDx:dx
o.disDy=(disDy!=null)?disDy:dy
o.imgRight=imgRight?true:false
}
o.getHTML=ButtonWidget_getHTML
o.setDisabled=ButtonWidget_setDisabled
o.setText=ButtonWidget_setText
o.changeImg=ButtonWidget_changeImg
o.oldInit=o.init
o.init=ButtonWidget_init
o.isDisabled=ButtonWidget_isDisabled
o.setDefaultButton=ButtonWidget_setDefaultButton
o.executeCB=ButtonWidget_executeCB
o.setTooltip=ButtonWidget_setTooltip
o.instIndex=ButtonWidget_currInst
ButtonWidget_inst[ButtonWidget_currInst++]=o
return o;
}
function ButtonWidget_getHTML()
{
with (this)
{
var clk=_codeWinName+'.ButtonWidget_clickCB('+this.instIndex+');return ' + (_webKit ? 'true': 'false') + ';"'
var clcbs= 'onclick="'+clk+'" '
if (_ie)  clcbs+= 'ondblclick="'+clk+'" '
//
var isDefaultSty=(this.isDefault && !this.isGray);
//add onkeydown for dialog default action (Enter)
clcbs+= 'onkeydown=" return '+_codeWinName+'.ButtonWidget_keydownCB(event,'+this.instIndex+');" '
var url1=_skin+"button.gif",addPar=' style="'+extraStyle+'cursor:'+_hand+';margin-left:'+margin+'px; margin-right:'+margin+'px; "'+clcbs+' ',tip=attr('title', tooltip),idText="theBttn"+id, idIcon="theBttnIcon"+id;
var bg=backImgOffset(url1,0,isDefaultSty?105:42);
var lnkB='<a '+attr('id',idText)+' '+tip+' '+attr('tabindex',tabIndex)+' href="javascript:void(0)" class="wizbutton">'
var l=(label!=null)
var im=(this.url?('<td align="'+(l?(this.imgRight?'right':'left'):'center')+'" style="'+bg+'" width="'+(!l&&(width!=null)?width+6:w+6)+'">'+(l?'':lnkB)+simpleImgOffset(url,w,h,this.isGray?disDs:dx,this.isGray?disDy:dy,idIcon,null,(l?'':tooltip),'cursor:'+_hand)+(l?'':'</a>')+'</td>'):'')
return '<table onmouseover="return true" '+attr('id',id)+' '+addPar+' border="0" cellspacing="0" cellpadding="0"><tr valign="middle">'+
'<td width="5">'+simpleImgOffset(url1,5,21,0,isDefaultSty?63:0,"theBttnLeftImg"+id)+'</td>'+
(this.imgRight?'':im)+
(l ?('<td '+attr("width",width)+ attr('id',"theBttnCenterImg"+id)+' align="center" class="'+(this.isGray?'wizbuttongray':'wizbutton')+'" style="padding-left:3px;padding-right:3px;'+bg+'">'+
'<button class="wizbuttoninner" '+attr('id','theRealBttn'+id)+' style="cursor:'+_hand+';padding:0px;margin:0px;border:none;background-color:transparent;font-size:1em;"'+(this.isGray?'disabled="disabled" ':' ')+tip+' ' + attr('tabindex',tabIndex)+'>'+
'<nobr '+attr('id',idText)+'>'+convStr(label)+'</nobr>'+
'</button>'+
'</td>'):'')+
(this.imgRight?im:'')+
'<td width="5">'+simpleImgOffset(url1,5,21,0,isDefaultSty?84:21,"theBttnRightImg"+id)+'</td></tr></table>';
}
}
function ButtonWidget_setDisabled(d)
{
var o=this,newCur=d?'default':_hand
o.isGray=d
if (o.layer)
{
var newClassName=d?'wizbuttongray':'wizbutton'
// Ensure the button state hasn't changed to avoid unnecessary processing
// The text className is a safe way to do the test
if (o.txt.className!=newClassName)
{
o.txt.className=newClassName
o.txt.style.cursor=newCur
o.css.cursor=newCur
if (o.btn)
{
o.btn.className=d?'wizbuttongray':'wizbutton'
o.btn.disabled=d?true:false
o.btn.style.cursor=newCur
}
if(o.icn)
{
changeSimpleOffset(o.icn,o.isGray?o.disDx:o.dx,o.isGray?o.disDy:o.dy)
o.icn.style.cursor=newCur
}
if(o.isDefault) //default button style
{
var isDefaultSty=!d,url=_skin+"button.gif";
changeSimpleOffset(o.leftImg,0,isDefaultSty?63:0,url);
changeOffset(o.centerImg,0,isDefaultSty?105:42,url);
changeSimpleOffset(o.rightImg,0,isDefaultSty?84:21,url);
}
}
}
}
function ButtonWidget_setDefaultButton()
{
var o=this;
if (o.layer)
{
var isDefaultSty = !o.isGray,url=_skin+"button.gif";
changeSimpleOffset(o.leftImg,0,isDefaultSty?63:0,url);
changeOffset(o.centerImg,0,isDefaultSty?105:42,url);
changeSimpleOffset(o.rightImg,0,isDefaultSty?84:21,url);
}
o.isDefault=true;
}
function ButtonWidget_isDisabled()
{
return this.isGray
}
function ButtonWidget_setText(str)
{
this.txt.innerHTML=convStr(str)
}
function ButtonWidget_setTooltip(tooltip)
{
var o=this
o.tooltip=tooltip
o.layer.title=tooltip
if (o.txt)
o.txt.title=tooltip
if (o.icn)
o.icn.title=tooltip
}
function ButtonWidget_init()
{
var o=this
o.oldInit()
o.txt=getLayer('theBttn'+this.id)
o.icn=getLayer('theBttnIcon'+this.id)
o.btn=getLayer('theRealBttn'+this.id)
o.leftImg=getLayer('theBttnLeftImg'+this.id)
o.centerImg=getLayer('theBttnCenterImg'+this.id)
o.rightImg=getLayer('theBttnRightImg'+this.id)
// reset if already initialized
var newClassName=o.isGray?'wizbuttongray':'wizbutton'
if (o.txt.className!=newClassName)
{
o.setDisabled(o.isGray)
}
}
function ButtonWidget_changeImg(dx,dy,disDx,disDy,url,tooltip)
{
var o=this
if (url) o.url=url
if (dx!=null) o.dx=dx
if (dy!=null) o.dy=dy
if (disDx!=null) o.disDx=disDx
if (disDy!=null) o.disDy=disDy
if (tooltip!=null) o.tooltip=tooltip
if (o.icn)
changeSimpleOffset(o.icn,o.isGray?o.disDx:o.dx,o.isGray?o.disDy:o.dy, o.url, o.tooltip)
}
function ButtonWidget_clickCB(index)
{
var btn=ButtonWidget_inst[index]
if (btn && !btn.isGray)
setTimeout("ButtonWidget_delayClickCB("+index+")",1)
}
function ButtonWidget_delayClickCB(index)
{
var btn=ButtonWidget_inst[index]
btn.executeCB();
/*
if (btn.cb)
{
if (typeof btn.cb!="string")
btn.cb()
else
eval(btn.cb)
}*/
}
function ButtonWidget_executeCB()
{
var o=this
if (o.cb)
{
if (typeof o.cb!="string")
o.cb()
else
eval(o.cb)
}
}
function ButtonWidget_keydownCB(e,index)
{
var k=eventGetKey(e);
var btn=ButtonWidget_inst[index]
if(k == 13 && btn.cb )//enter
{
eventCancelBubble(e);
}
return true;
}
function newScrolledZoneWidget(id,borderW,padding,w,h,bgClass)
{
var o=newWidget(id)
o.borderW=borderW
o.padding=padding
o.w=w
o.h=h
o.oldResize=o.resize
o.beginHTML=ScrolledZoneWidget_beginHTML
o.endHTML=ScrolledZoneWidget_endHTML
o.resize=ScrolledZoneWidget_resize
o.bgClass = (bgClass)? bgClass : 'insetBorder'
return o;
}
function ScrolledZoneWidget_beginHTML()
{
var w=this.w,h=this.h;
var ofs=_dtd4?2*(this.borderW+this.padding):0
if (typeof(w)=="number")
{
if (_dtd4)
w=Math.max(0,w-ofs)
w=""+w+"px"
}
if (typeof(h)=="number")
{
if (_moz)
h=Math.max(0,h-ofs)
h=""+h+"px"
}
return '<div align="left" onselectstart="return false" class="' + this.bgClass + '" id="'+this.id+'" style="border-width:'+this.borderW+'px;padding:'+this.padding+'px;'+sty("width",w)+sty("height",h)+'overflow:auto">'
}
function ScrolledZoneWidget_endHTML()
{
return '</div>'
}
function ScrolledZoneWidget_resize(w,h)
{
var o=this
if (w!=null) o.w=w
if (h!=null) o.h=h
if (_dtd4)
{
var ofs=2*(o.borderW+o.padding)
if (w!=null) w=Math.max(0,w-ofs)
if (h!=null) h=Math.max(0,h-ofs)
}
o.oldResize(w,h)
}
function newTooltipWidget(maxw,maxh)
{
if (window._theGlobalTooltip!=null)
return window._theGlobalTooltip
var o=newWidget('theGlobalTooltip')
o.maxw=maxw
o.maxh=maxh
o.getPrivateHTML=TooltipWidget_getPrivateHTML
o.init=TooltipWidget_init
o.oldShow=o.show
o.show=TooltipWidget_show
o.hide=TooltipWidget_hide
o.setPos=TooltipWidget_setPos
o.inputs=null
window._theGlobalTooltip=o
o.eventWin=_curWin
preloadImg(_skin+'../swap.gif')
return o;
}
function TooltipWidget_init()
{
//cancels the default init behaviour
}
function TooltipWidget_getPrivateHTML()
{
var o=this
return getBGIframe('tipIframe_'+o.id)+'<div class="dragTooltip" id="'+o.id+'" style="visibility:hidden;z-index:10000;position:absolute;top:0px;left:0px,visibility:'+_hide+'"'+attr("width",o.w)+attr("height",o.h)+'></div>'+
'<img width="11" height="11" border="0" hspace="0" vspace="0" src="'+_skin+'../swap.gif" id="swap_'+o.id+'" style="position:absolute;top:0px;left:0px;display:none;z-index:10000;">'
}
function TooltipWidget_show(show,str,url,w,h,dx,dy,isHTML, e)
{
var o=this
oldWin=_curWin
_curWin=_tooltipWin
_curDoc=_tooltipWin.document
// object not init yet, 2 cases
if (o.layer==null)
{
o.layer=getLayer(o.id)
// another instance hasn't written it's HTML yet
if (o.layer==null)
{
targetApp(o.getPrivateHTML())
o.layer=getLayer(o.id)
}
o.css=o.layer.style
o.swapLayer=getLayer("swap_"+o.id)
o.iframe=newWidget('tipIframe_'+o.id)
o.iframe.init()
}
// hides combo boxes or restore
//if (show) o.inputs=hideAllInputs()
//else restoreAllInputs(o.inputs)
dx=dx!=null?dx:0
dy=dy!=null?dy:0
if (show)
{
var s=null
if (url)
//s='<span style="margin-right:2px;width:'+w+'px;height:'+h+'px;overflow:hidden">'+img(url,null,null,'top','style="position:relative;top:-'+dy+'px;left:-'+dx+'px"')+'</span>'
s=simpleImgOffset(url,w,h,dx,dy,null,null,null,"margin-right:4px;margin-left:0px;",'top')
//o.resize(0,0)
o.css.width=''
o.css.height=''
o.setHTML('<table border="0" cellspacing="0" cellpadding="0"><tr valign="middle">'+(s?'<td align="center">'+s+'</td>':'')+'<td class="dragTxt"><nobr>'+(isHTML?str:convStr(str))+'</nobr></td></tr></table>')
o.setPos(null,e)
if (o.getWidth()>o.maxw)
{
o.resize(o.maxw)
o.iframe.resize(o.maxw)
}
/*
if (o.getHeight()>o.maxh)
o.resize(null,o.maxh)*/
o.oldShow(show)
o.iframe.setDisplay(true)
}
else
{
o.oldShow(false)
o.setHTML('')
o.move(0,0)
o.swapLayer.style.display="none"
o.iframe.setDisplay(false)
}
_curWin=oldWin
_curDoc=_curWin.document
}
function TooltipWidget_hide()
{
var o=this
o.show(false)
}
function TooltipWidget_setPos(shift,e)
{
var o=this
if (o.layer==null)
return
/*
var x=0,y=0;
if (_ie)
{
var ew=o.eventWin;
x=ew.event.x+_curDoc.body.scrollLeft
y=ew.event.y+_curDoc.body.scrollTop
}
else
{
x=absxpos(e)
y=absypos(e)
}
*/
if (e) {
var x=absxpos(e), y=absypos(e)
} else {
var ew=o.eventWin,x=ew.event.x+_curDoc.body.scrollLeft,y=ew.event.y+_curDoc.body.scrollTop
}
x+=_tooltipDx
y+=_tooltipDy
o.move(x+27,y+10);
o.iframe.move(x+27,y+10);
o.iframe.resize(o.getWidth(),o.getHeight())
var c=o.swapLayer.style;
c.display=shift?"":"none"
if (shift)
{
y+=18
x+=14
if (_moz)
{
c.left=""+x+"px"
c.top=""+y+"px"
}
else
{
c.pixelLeft=x
c.pixelTop=y
}
}
}
function initTooltipWin(tooltipWin)
{
_tooltipWin=tooltipWin
}
function setTooltipOffset(dx,dy)
{
_tooltipDx=dx
_tooltipDy=dy
}
function newComboWidget(id,changeCB,noMargin,width,tooltip)
{
var o=newWidget(id)
o.tooltip=tooltip
o.size=1
o.getHTML=ComboWidget_getHTML
o.beginHTML=ComboWidget_beginHTML
o.endHTML=ComboWidget_endHTML
o.changeCB=changeCB
o.noMargin=noMargin
o.width=width==null?null:''+width+'px'
o.add=ComboWidget_add
o.del=ComboWidget_del
o.getSelection=ComboWidget_getSelection
o.select=ComboWidget_select
o.valueSelect=ComboWidget_valueSelect
o.getCount=ComboWidget_getCount
o.oldSetDisabled=o.setDisabled
o.setDisabled=ComboWidget_setDisabled
o.setUndefined=ComboWidget_setUndefined
o.delByID=ComboWidget_delByID
o.move=ComboWidget_move
o.findByValue=ComboWidget_findByValue
o.findByText=ComboWidget_findByText
o.getValue=ComboWidget_getValue
o.isGrayed=ComboWidget_isGrayed
o.isDisabled=false
o.multi=false
o.undef=false
o.isCombo=true
o.selValBeforDisabled=null
o.undefId=o.id+"__undef"
o.disabledId=o.id+"__disabled"
return o;
}
_extrCmbS=(_moz?'font-size:12px;':'')
function ComboWidget_beginHTML()
{
var o=this,_extrCmbS=((_moz&&!o.isCombo)?'font-size:12px;':'')
return '<select '+(o.multi?'multiple':'')+' '+(o.noMargin?'style="'+sty("width",o.width)+_extrCmbS+'"':'style="'+sty("width",o.width)+'margin-left:10px;'+_extrCmbS+'"')+' class="listinputs" '+attr('onchange',_codeWinName+'.ComboWidget_changeCB(event,this)')+attr('ondblclick',_codeWinName+'.ComboWidget_dblClickCB(event,this)')+attr('onkeyup',_codeWinName+'.ComboWidget_keyUpCB(event,this)')+attr('onkeydown',_codeWinName+'.ComboWidget_keyDownCB(event,this)')+attr('id',o.id)+attr('name',o.id)+attr('title',o.tooltip)+'size="'+o.size+'">'
}
function ComboWidget_endHTML()
{
return '</select>'
}
function ComboWidget_getHTML(inner)
{
return this.beginHTML()+(inner?inner:'')+this.endHTML()
}
function ComboWidget_add(s,val,sel,id,grayed, tooltip, pos)
{
var e=this.layer,opt=_curDoc.createElement('option');
// Add item
if (pos != null && pos!= -1 && pos != e.options.length)
{
if ((typeof pos !="number") || pos < 0 || pos > e.options.length || ""+pos=="NaN")
return false
if (_ie)
{
e.options.add(opt, pos)
}
else
{
var posOpt = e.options[pos]
e.insertBefore(opt, posOpt);
}
}
else
{
if (_ie)
e.options.add(opt);
else
e.appendChild(opt);
}
// Set text content
if (opt.innerText!=null)
opt.innerText=s;
else
opt.innerHTML=convStr(s);
// Set other attributes
opt.value=val;
if(id!=null)
opt.id=id;
if (sel)
opt.selected=true;
if (grayed) 
{
opt.style.color='gray'
}
if (tooltip)
opt.title=tooltip;
return opt;
}
function ComboWidget_move(delta)
{
    var e=this.layer,i=e.selectedIndex,len=e.options.length-1,newI=i+delta
    if ((i==-1)||(newI<0)||(newI>len))
        return false
    var oldOpt = e.options[i],newOpt = e.options[newI]
    var oldVal=oldOpt.value,oldHTML=oldOpt.innerHTML,oldID=oldOpt.id,newID=newOpt.id,oldColor=oldOpt.style.color,newColor=newOpt.style.color
    oldOpt.value=newOpt.value
    oldOpt.innerHTML=newOpt.innerHTML
    newOpt.id=null
    oldOpt.id=newOpt.id
oldOpt.style.color=newColor
    newOpt.value=oldVal
    newOpt.innerHTML=oldHTML
    newOpt.id=oldID
newOpt.style.color=oldColor
    e.selectedIndex=newI
    return true
}
function ComboWidget_getSelection()
{
var e=this.layer,i=e.selectedIndex;if (i<0) return null;var ret=new Object;ret.index=i;ret.value=e.options[i].value;ret.text=e.options[i].text;return ret
}
function ComboWidget_select(i)
{
var o=this,e=o.layer,len=e.options.length
if (i==null) e.selectedIndex=-1
if ((i<0)||(i>=len))
i=len-1
if (i>=0)
e.selectedIndex=i
o.setUndefined(false)
}
function ComboWidget_valueSelect(v)
{
var o=this,e=o.layer,opts=e.options,len=opts.length
for (var i=0;i<len;i++)
{
if (opts[i].value==v)
{
//e.selectedIndex=i
    opts[i].selected=true
o.setUndefined(false)
break;
}
}
}
function ComboWidget_del(i)
{
var e=this.layer
if (i==null)
e.options.length=0
else
{
if (_ie) e.remove(i)
else e.options[i]=null
this.select(i)
}
}
function ComboWidget_changeCB(e,l)
{
var o=getWidget(l);if(o.changeCB) o.changeCB(e)
}
function ComboWidget_dblClickCB(e,l)
{
var o=getWidget(l);if(o.dblClickCB) o.dblClickCB(e)
}
function ComboWidget_keyUpCB(e,l)
{
var o=getWidget(l);if(o.keyUpCB) o.keyUpCB(e)
}
function ComboWidget_keyDownCB(e,l)
{
var k=eventGetKey(e);
var o=getWidget(l);
//be careful ! usefull for dialog box close by Enter ou Escape keypressed
if(o.isCombo && (k==27 || k==13))//Escape ou Enter
{
eventCancelBubble(e);
}
else if(k==13 && o.keyUpCB) //Enter can be attached to an action in listwidget
{
eventCancelBubble(e);
}
}
function ComboWidget_getCount()
{
return this.layer.options.length
}
function ComboWidget_delByID(id)
{
var opt=getLayer(id)
if (opt!=null)
this.del(opt.index)
opt=null
}
function ComboWidget_setDisabled(d,addEmptyElt)
{
var o=this
o.oldSetDisabled(d);
var alreadyDisabled = o.isDisabled;
o.isDisabled=d;
addEmptyElt = (addEmptyElt == null ? true : addEmptyElt)
if (d==true)
{
if (!alreadyDisabled)
o.selValBeforDisabled = o.getSelection()
if (addEmptyElt)
{
var old=getLayer(o.disabledId)
if (old==null)
o.add('','',true,o.disabledId);
else
o.layer.selectedIndex=old.index
}
}
else
{
o.delByID(o.disabledId)
if (o.selValBeforDisabled != null && o.selValBeforDisabled.value != null)
o.valueSelect (o.selValBeforDisabled.value)
}
}
function ComboWidget_setUndefined(u)
{
var o=this
o.undef=u;
if (u==true)
{
var old=getLayer(o.undefId)
if (old==null)
o.add('','',true,o.undefId);
else
o.layer.selectedIndex=old.index
}
else
o.delByID(o.undefId)
}
function ComboWidget_findByValue(val)
{
var o=this,e=o.layer,opts=e.options,len=opts.length
for (var i=0;i<len;i++)
{
if (opts[i].value==val)
{
var ret=new Object;
ret.index=i;
ret.value=e.options[i].value;
return ret
}
}
return null
}
function ComboWidget_findByText(txt)
{
var o=this,e=o.layer,opts=e.options,len=opts.length
for (var i=0;i<len;i++)
{
if (opts[i].text==txt)
{
var ret=new Object;
ret.index=i;
ret.value=e.options[i].value;
ret.txt=e.options[i].txt;
return ret
}
}
return null
}
function ComboWidget_getValue(i)
{
var o=this,e=o.layer,opts=e.options,len=opts.length
if(i==null || i<0 || i>len)  return null;
var ret=new Object;
ret.index=i;
ret.value=e.options[i].value;
return ret
}
function ComboWidget_isGrayed(i)
{
var o=this,e=o.layer,opts=e.options,len=opts.length
if(i==null || i<0 || i>len)  return false;
return (e.options[i].style.color=="gray")
}
function newListWidget(id,changeCB,multi,width,lines,tooltip,dblClickCB,keyUpCB)
{
var o=newComboWidget(id,changeCB,true,width,tooltip)
o.dblClickCB=dblClickCB
o.keyUpCB=keyUpCB
o.size=lines
o.multi=multi
o.getMultiSelection=ListWidget_getMultiSelection
o.setUndefined=ListWidget_setUndefined
o.isUndefined=ListWidget_isUndefined
o.change=ListWidget_change
o.isCombo=false
return o;
}
function ListWidget_setUndefined(u)
{
var o=this
o.undef=u;
if (u==true)
{
o.layer.selectedIndex = -1
}
}
function ListWidget_isUndefined()
{
return (this.layer.selectedIndex == -1)
}
function ListWidget_getMultiSelection()
{
var e=this.layer,rets=new Array,len=e.options.length
for (var i=0;i<len;i++)
{
var opt=e.options[i]
if (opt.selected)
{
var ret=new Object;
ret.index=i;ret.value=opt.value;ret.text=opt.text;rets[rets.length]=ret
}
}
return rets
}
function ListWidget_change(multi,lines)
{
var o=this
if(multi!=null)
{
o.multi=multi
o.layer.multiple=multi
}
if(lines!=null)
{
o.size=lines
o.layer.size=lines
}
}
function newInfoWidget(id,title,boldTitle,text,height)
{
var o=newWidget(id)
o.title=title?title:""
o.boldTitle=boldTitle?boldTitle:""
o.text=text?text:""
o.height=(height!=null)?height:55
o.getHTML=InfoWidget_getHTML
o.setText=InfoWidget_setText
o.setTitle=InfoWidget_setTitle
o.setTitleBold=InfoWidget_setTitleBold
o.oldResize=o.resize
o.resize=InfoWidget_resize
o.textLayer=null
return o
}
function InfoWidget_setText(text,isHTML)
{
var o=this
text=text?text:""
o.text=text
if (o.layer)
{
var l=o.textLayer
if (l==null)
l=o.textLayer=getLayer('infozone_'+o.id)
if (l) l.innerHTML=isHTML?text:convStr(text,false,true)
}
}
function InfoWidget_setTitle(text)
{
var o=this
text=text?text:""
o.title=text
if (o.layer)
{
var l=o.titleLayer
if (l==null)
l=o.titleLayer=getLayer('infotitle_'+o.id)
if (l) l.innerHTML=convStr(text)
}
}
function InfoWidget_setTitleBold(text)
{
var o=this
text=text?text:""
o.boldTitle=text
if (o.layer)
{
var l=o.titleLayerBold
if (l==null)
l=o.titleLayerBold=getLayer('infotitlebold_'+o.id)
if (l) l.innerHTML=convStr(text)
}
}
function InfoWidget_getHTML()
{
var o=this
return '<div class="dialogzone" align="left" style="overflow:hidden;'+sty("width",o.width)+sty("height",""+o.height+"px")+'" id="'+o.id+'">'+
'<div style="white-space:nowrap;">'+img(_skin+'../help.gif',16,16,'top',null,_helpLab)+
'<span class="dialogzone" style="padding-left:5px" id="infotitle_'+o.id+'">'+convStr(o.title)+'</span>'+
'<span style="padding-left:5px" class="dialogzonebold" id="infotitlebold_'+o.id+'">'+convStr(o.boldTitle)+'</span></div>'+
'<div class="infozone" align="left" id="infozone_'+o.id+'" style="margin-top:2px;height:'+(o.height-18-(_dtd4?10:0))+'px;overflow'+(_ie?'-y':'')+':auto">'+convStr(o.text,false,true)+'</div>'+
'</div>'
}
function InfoWidget_resize(w,h)
{
var o=this;
if (w!=null) o.w=w
if (h!=null) o.h=h
o.oldResize(w,h)
if (o.layer)
{
var l=o.textLayer
if (l==null)
l=o.textLayer=getLayer('infozone_'+o.id)
if (l)
{
if (o.h!=null) l.style.height=""+Math.max(0, o.h-(_dtd4?28:18))+"px"
}
}
}
function newCheckWidget(id,text,changeCB,bold,imgUrl,imgW,imgH,bconvtext)
{
var o=newWidget(id)
o.text=text
o.convText=bconvtext
o.changeCB=changeCB
o.idCheckbox='check_'+id
o.checkbox=null
o.kind='checkbox'
o.name=o.idCheckbox
o.bold=bold
o.imgUrl=imgUrl
o.imgW=imgW
o.imgH=imgH
o.getHTML=CheckWidget_getHTML
o.setText=CheckWidget_setText
o.parentInit=Widget_init
o.init=CheckWidget_init
o.check=CheckWidget_check
o.isChecked=CheckWidget_isChecked
o.setDisabled=CheckWidget_setDisabled
o.isDisabled=CheckWidget_isDisabled
o.uncheckOthers=CheckWidget_uncheckOthers
o.isIndeterminate=CheckWidget_isIndeterminate
o.setIndeterminate=CheckWidget_setIndeterminate
o.layerClass=('dialogzone'+(o.bold?'bold':''))
o.nobr=true
return o
}
function CheckWidget_getHTML()
{
var o=this,cls=o.layerClass;
return '<table border="0" onselectstart="return false" cellspacing="0" cellpadding="0" class="'+cls+'"'+attr('id',o.id)+'>' +
'<tr valign="middle">'+
'<td style="height:20px;width:21px"><input style="margin:'+(_moz?3:0)+'px" onclick="'+_codeWinName+'.CheckWidget_changeCB(event,this)" ' +
'type="'+o.kind+'"'+attr('id',o.idCheckbox)+attr('name',o.name)+'>' +
'</td>'+
(o.imgUrl?'<td><label style="padding-left:2px" for="'+o.idCheckbox+'">'+img(o.imgUrl,o.imgW,o.imgH)+'</label></td>':'')+
'<td>'+(o.nobr?'<nobr>':'')+'<label style="padding-left:'+ (o.imgUrl?4:2) +'px" id="label_'+o.id+'" for="'+o.idCheckbox+'">'+(o.convText?convStr(o.text):o.text)+'</label>'+(o.nobr?'</nobr>':'')+'</td>'+
'</tr></table>'
}
function CheckWidget_setText(s)
{
var o=this
o.text=s
if (o.layer)
{
if (o.labelLyr==null)
o.labelLyr=getLayer('label_'+o.id)
o.labelLyr.innerHTML=o.convText?convStr(s):s
}
}
function CheckWidget_init()
{
this.parentInit()
this.checkbox=getLayer(this.idCheckbox)
}
function CheckWidget_check(c) {this.checkbox.checked=c;if(c)this.uncheckOthers()}
function CheckWidget_isChecked() {return this.checkbox.checked}
function CheckWidget_changeCB(e,l) {var o=getWidget(l);o.uncheckOthers();if(o.changeCB) o.changeCB(e)}
function CheckWidget_setDisabled(d) {this.checkbox.disabled=d;if(_moz) this.checkbox.className=(d?'dialogzone':'')}
function CheckWidget_isDisabled(){ return this.checkbox.disabled }
function CheckWidget_uncheckOthers() {}
function CheckWidget_isIndeterminate() {return this.checkbox.indeterminate}
function CheckWidget_setIndeterminate(b) {this.checkbox.indeterminate=b}
function newRadioWidget(id,group,text,changeCB,bold,imgUrl,imgW,imgH,bconvtext)
{
var o=newCheckWidget(id,text,changeCB,bold,imgUrl,imgW,imgH,bconvtext)
o.kind='radio'
o.name=group
if (_RadioWidget_groups[group]==null)
_RadioWidget_groups[group]=new Array
o.groupInstance=_RadioWidget_groups[group]
var g=o.groupInstance
o.groupIdx=g.length
g[g.length]=o
o.uncheckOthers=RadioWidget_uncheckOthers
return o;
}
var _RadioWidget_groups=new Array
function RadioWidget_uncheckOthers()
{
var g=this.groupInstance,idx=this.groupIdx,len=g.length
for (var i=0;i<len;i++)
{
if (i!=idx)
{
var c=g[i].checkbox
if(c)
c.checked=false
}
}
}
function newIconNoTextCheckWidget(id,changeCB,imgUrl,imgW,imgH,tooltip)
{
var o=newWidget(id)
o.changeCB=changeCB
o.idCheckbox='check_'+id
o.checkbox=null
o.kind='checkbox'
o.name=o.idCheckbox
o.imgUrl=imgUrl
o.imgW=imgW
o.imgH=imgH
o.tooltip=(tooltip?tooltip:"")
o.getHTML=IconNoTextCheckWidget_getHTML
o.parentInit=Widget_init
o.init=CheckWidget_init
o.check=CheckWidget_check
o.isChecked=CheckWidget_isChecked
o.setDisabled=CheckWidget_setDisabled
o.uncheckOthers=CheckWidget_uncheckOthers
return o
}
function IconNoTextCheckWidget_getHTML()
{
var o=this
return '<table border="0" onselectstart="return false" cellspacing="0" cellpadding="0" title= "'+ convStr(o.tooltip) + '" ' + attr('id',o.id) +'>' +
'<tr>'+
'<td align="center"><label style="padding-left:2px" for="'+o.idCheckbox+'">'+img(o.imgUrl,o.imgW,o.imgH, null, null, convStr(o.tooltip))+'</label></td>'+
'</tr>'+
'<tr>'+
'<td align="center"><input style="margin:'+(_moz?3:0)+'px" onclick="'+_codeWinName+'.CheckWidget_changeCB(event,this)" ' +
'type="'+o.kind+'"'+attr('id',o.idCheckbox)+attr('name',o.name)+'>' +
'</td>'+
'</tr></table>'
}
function newIconNoTextRadioWidget(id,group,changeCB,imgUrl,imgW,imgH,tooltip)
{
var tip = tooltip?tooltip:""
var o=newIconNoTextCheckWidget(id,changeCB,imgUrl,imgW,imgH,tip)
o.kind='radio'
o.name=group
if (_RadioWidget_groups[group]==null)
_RadioWidget_groups[group]=new Array
o.groupInstance=_RadioWidget_groups[group]
var g=o.groupInstance
o.groupIdx=g.length
g[g.length]=o
o.uncheckOthers=RadioWidget_uncheckOthers
return o;
}
function newTextFieldWidget(id,changeCB,maxChar,keyUpCB,enterCB,noMargin,tooltip,width,focusCB,blurCB)
{
var o=newWidget(id)
o.tooltip=tooltip
o.changeCB=changeCB
o.maxChar=maxChar
o.keyUpCB=keyUpCB
o.enterCB=enterCB
o.noMargin=noMargin
o.width=width==null?null:''+width+'px'
o.focusCB=focusCB
o.blurCB=blurCB
o.disabled=false
o.readOnly=false
o.getHTML=TextFieldWidget_getHTML
o.getValue=TextFieldWidget_getValue
o.setValue=TextFieldWidget_setValue
o.intValue=TextFieldWidget_intValue
o.floatValue=TextFieldWidget_floatValue
o.intPosValue=TextFieldWidget_intPosValue
o.select=TextFieldWidget_select
o.setDisabled=TextFieldWidget_setDisabled
o.setReadOnly=TextFieldWidget_setReadOnly
o.setClasses=TextFieldWidget_setClasses
o.beforeChange=null
o.wInit=o.init
o.init=TextFieldWidget_init
o.oldValue=""
o.helpTxt=''
o.isHelpTxt=false
o.setHelpTxt=TextFieldWidget_setHelpTxt
o.eraseHelpTxt=TextFieldWidget_eraseHelpTxt
o.enterCancelBubble=true
o.className="textinputs"
return o
}
function TextFieldWidget_setDisabled(d)
{
var o=this
o.disabled=d
if (o.layer)
o.layer.disabled=d
}
function TextFieldWidget_setReadOnly(r)
{
var o=this
o.readOnly=r
if (o.layer)
o.layer.readOnly=r
}
function TextFieldWidget_init()
{
var o=this
o.wInit()
o.layer.value=""+ (o.oldValue != "")?o.oldValue : "";
if(o.helpTxt && !o.oldValue)
o.setHelpTxt(o.helpTxt);
}
function TextFieldWidget_getHTML()
{
var o=this
return '<input'+(o.disabled?' disabled':'')+(o.readOnly?' readonly':'')+' oncontextmenu="event.cancelBubble=true;return true" style="'+sty("width",o.width)+(_dtd4?'margin-top:1px;margin-bottom:1px;padding-left:2px;padding-right:2px':'')+'height:18px;margin-left:'+(o.noMargin?0:10)+'px" onfocus="'+_codeWinName+'.TextFieldWidget_focus(this)" onblur="'+_codeWinName+'.TextFieldWidget_blur(this)" onchange="'+_codeWinName+'.TextFieldWidget_changeCB(event,this)" onkeydown=" return '+_codeWinName+'.TextFieldWidget_keyDownCB(event,this);" onkeyup=" return '+_codeWinName+'.TextFieldWidget_keyUpCB(event,this);" type="text" '+attr('maxLength',o.maxChar)+' ondragstart="event.cancelBubble=true;return true" onselectstart="event.cancelBubble=true;return true" class="'+o.className+'" id="'+o.id+'" name="'+o.id+'"'+attr('title',o.tooltip)+' value="">'
}
function TextFieldWidget_setClasses(className)
{
if (className)
{
var o=this
o.className=className
if (o.layer)
o.layer.className=className
}
}
function TextFieldWidget_getValue()
{
var o=this
if (o.isHelpTxt) { 
return ''
}
else
{
return o.layer ? o.layer.value : o.oldValue
}
}
function TextFieldWidget_setValue(s)
{
var o=this
if (o.layer) {
o.eraseHelpTxt()
o.layer.value=''+s
} else {
o.oldValue=s
}
}
function TextFieldWidget_changeCB(e,l)
{
var o=getWidget(l)
o.eraseHelpTxt()
if(o.beforeChange)
o.beforeChange()
if(o.changeCB)
o.changeCB(e)
}
function TextFieldWidget_keyUpCB(e,l)
{
var o=getWidget(l)
o.eraseHelpTxt()
if (eventGetKey(e)==13)
{
if (o.beforeChange)
o.beforeChange()
if (o.enterCB)
{
if (o.enterCancelBubble)
eventCancelBubble(e)
o.enterCB(e)
}
return false
}
else if(o.keyUpCB)
{
o.keyUpCB(e)
}
return true
}
function TextFieldWidget_keyDownCB(e,l)
{
var o=getWidget(l)
var key=eventGetKey(e)
o.eraseHelpTxt()
if (key==13)
{
if (o.enterCB)
{
if (o.enterCancelBubble)
eventCancelBubble(e)
}
return false
}
else if (key==8 || key==46)//back space
{
eventCancelBubble(e);
}
return true;
}
function TextFieldWidget_eraseHelpTxt()
{
var o=this
if (o.isHelpTxt) o.layer.value= ""
o.isHelpTxt = false
o.layer.style.color="black"
}
function TextFieldWidget_focus(l)
{
var o=getWidget(l)
o.eraseHelpTxt()
if (o.focusCB)
o.focusCB()
}
function TextFieldWidget_blur(l)
{
var o=getWidget(l)
if(o.beforeChange)
o.beforeChange()
if (o.blurCB)
o.blurCB()
}
function TextFieldWidget_intValue(nanValue)
{
var n=parseInt(this.getValue())
return isNaN(n)?nanValue:n
}
function TextFieldWidget_floatValue(nanValue)
{
var n=parseFloat(this.getValue())
return isNaN(n)?nanValue:n
}
function TextFieldWidget_intPosValue(nanValue)
{
var n=this.intValue(nanValue)
return (n<0)?nanValue:n
}
function TextFieldWidget_select()
{
this.layer.select()
}
function TextFieldWidget_setHelpTxt(h)
{
var o=this
o.helpTxt=h
if (o.layer && (o.layer.value == "")) 
{
o.isHelpTxt=true
o.layer.value=h
o.layer.style.color="#808080"
}
}
function newIntFieldWidget(id,changeCB,maxChar,keyUpCB,enterCB,noMargin,tooltip,width,customCheckCB,focusCB,blurCB)
{
var o=newTextFieldWidget(id,changeCB,maxChar,keyUpCB,enterCB,noMargin,tooltip,width,focusCB,blurCB)
o.min=-Number.MAX_VALUE
o.max=Number.MAX_VALUE
o.customCheckCB=customCheckCB // Returns a boolean
o.setMin=IntFieldWidget_setMin
o.setMax=IntFieldWidget_setMax
o.setValue=IntFieldWidget_setValue
o.beforeChange=IntFieldWidget_checkChangeCB
o.value=''
return o
}
function IntFieldWidget_setMin(min)
{
if (!isNaN(min))
this.min=min
}
function IntFieldWidget_setMax(max)
{
if (!isNaN(max))
this.max=max
}
function IntFieldWidget_setValue(s)
{
var o=this,l=o.layer
s = '' + s
if (s =='')
{
if (l)
l.value= ''
o.oldValue = ''
return
}
var n=parseInt(s)
var value = ''
if (!isNaN(n) && (n >= o.min) && (n <= o.max) && ((o.customCheckCB==null) || o.customCheckCB(n))) {
 value = n
 o.oldValue = value
} else {
if (o.oldValue)
value = o.oldValue
}
if (l)
l.value= '' + value
}
function IntFieldWidget_checkChangeCB()
{
var o=this
o.setValue(o.layer.value)
}
function newFloatFieldWidget(id,changeCB,maxChar,keyUpCB,enterCB,noMargin,tooltip,width,customCheckCB,focusCB,blurCB)
{
var o=newIntFieldWidget(id,changeCB,maxChar,keyUpCB,enterCB,noMargin,tooltip,width,customCheckCB,focusCB,blurCB)
o.setValue=FloatFieldWidget_setValue
o.setPrecision=FloatFieldWidget_setPrecision
o.toPrecision=FloatFieldWidget_toPrecision
o.setSeparator=FloatFieldWidget_setSeparator
o.beforeChange=FloatFieldWidget_checkChangeCB
o.precision=10 // default precision !!is it enough?
o.sep='.' // default separator
return o
}
function FloatFieldWidget_setValue(s)
{
var o=this,l=o.layer
s = '' + s
if (s =='')
{
if (l)
l.value= ''
o.oldValue = ''
return
}
var n=parseFloat(s)
value = ''
if (!isNaN(n) && (n >= o.min) && (n <= o.max) && ((o.customCheckCB==null) || o.customCheckCB(n))) {
value = '' + o.toPrecision(n)
o.oldValue = value
} else {
if (o.oldValue)
value = o.oldValue
}
if (l)
l.value = value
}
function FloatFieldWidget_toPrecision(n)
{
var o=this
n = '' + n
var nAr = n.split(o.sep)
if (nAr.length == 1) return nAr[0]
var dec = (nAr[1].length >= o.precision)? nAr[1].substr(0, o.precision) : nAr[1]
return nAr[0] + o.sep + dec
}
function FloatFieldWidget_checkChangeCB()
{
var o=this
o.setValue(o.layer.value)
}
function FloatFieldWidget_setPrecision(p)
{
this.precision=p
}
function FloatFieldWidget_setSeparator(s)
{
this.sep=s
}
function newTextAreaWidget(id,rows,cols,tooltip,changeCB,enterCB,cancelCB)
{
var o=newWidget(id)
o.rows=rows
o.cols=cols
o.allowCR=true
o.tooltip=tooltip
o.changeCB=changeCB
o.enterCB=enterCB
o.cancelCB=cancelCB
o.getHTML=TextAreaWidget_getHTML
o.getValue=TextAreaWidget_getValue
o.setValue=TextAreaWidget_setValue
o.resize=TextAreaWidget_resize
o.wInit=o.init
o.init=TextAreaWidget_init
o.oldValue=""
if ((o.rows!=null)&&!_ie&&!_saf)
o.rows--
return o
}
function TextAreaWidget_init()
{
var o=this
o.wInit()
o.layer.value=""+o.oldValue
}
function TextAreaWidget_getHTML()
{
return '<textarea oncontextmenu="event.cancelBubble=true;return true" id="'+this.id+'" '+attr('title',this.tooltip)+ 'rows="'+this.rows+'" cols="'+this.cols+'" class="textareainputs" value="" onkeydown="return '+_codeWinName+'.TextAreaWidget_keyDownCB(event,this)" ondragstart="event.cancelBubble=true;return true" onselectstart="event.cancelBubble=true;return true" ></textarea>'
}
function TextAreaWidget_getValue()
{
return this.layer.value
}
function TextAreaWidget_setValue(s)
{
if (this.layer)
this.layer.value=''+s
else
this.oldValue=s
}
function TextAreaWidget_resize(lines,cols)
{
var o=this
if(lines && lines >0) o.layer.rows=lines
if(cols && cols>0) o.layer.cols=cols
}
function TextAreaWidget_keyDownCB(e,l)
{
var key = eventGetKey(e),o=getWidget(l)
if (key==13)//enter
{
if (o.enterCB)
{
eventCancelBubble(e)
o.enterCB(e)
}
else if (o.allowCR)
{
eventCancelBubble(e);
//change in textarea
setTimeout("TextAreaWidget_delayedChangeCB("+key+","+o.widx+")",1)
}
return o.allowCR;
}
else if(key==27)//escape
{
if(o.cancelCB) 
return o.cancelCB(e)
else 
return true;
}
else if(key == 8)// back space
{
eventCancelBubble(e);
//change in textarea
setTimeout("TextAreaWidget_delayedChangeCB("+key+","+o.widx+")",1)
return true;
}
else 
{
//setTimeout to be sure that the key is writen in the textarea
//we do not use the keyup event because of rapid keyboard pression
setTimeout("TextAreaWidget_delayedChangeCB("+key+","+o.widx+")",1)
return true;
}
}
function TextAreaWidget_delayedChangeCB(key,widx)
{
var o=_widgets[widx]
if (o.changeCB)
o.changeCB(key)
}
function newFrameZoneWidget(id,w,h,reverse)
{
var o=newWidget(id)
o.w=(w!=null)?""+Math.max(0,w-10)+"px":null
o.h=(h!=null)?""+Math.max(0,h-10)+"px":null
o.reverse=(reverse!=null)?reverse:false
o.cont=null
o.beginHTML=FrameZoneWidget_beginHTML
o.endHTML=FrameZoneWidget_endHTML
o.oldResize=o.resize
o.resize=FrameZoneWidget_resize
return o
}
function FrameZoneWidget_resize(w,h)
{
var o=this
var d=o.layer.display!="none"
if (d&_moz&&!_saf)
o.setDisplay(false)
o.oldResize(w,h)
if (d&_moz&&!_saf)
o.setDisplay(true)
}
function FrameZoneWidget_beginHTML()
{
var o=this
return '<table  style="'+sty("width",o.w)+sty("height",o.h)+'" id="'+o.id+'" cellspacing="0" cellpadding="0" border="0"><tbody>'+
'<tr height="5">'+
'<td width="5">'+imgOffset(_skin+'dialogframe.gif',5,5,o.reverse?10:0,0)+'</td>'+
'<td style="'+backImgOffset(_skin+"dialogframetopbottom.gif",0,o.reverse?10:0)+'"></td>'+
'<td width="5">'+imgOffset(_skin+'dialogframe.gif',5,5,o.reverse?15:5,0)+'</td>'+
'</tr>'+
'<tr><td style="'+backImgOffset(_skin+"dialogframeleftright.gif",o.reverse?10:0,0)+'"></td><td valign="top" class="dialogzone" id="frame_cont_'+o.id+'">'
}
function FrameZoneWidget_endHTML()
{
var o=this
return '</td><td style="'+backImgOffset(_skin+"dialogframeleftright.gif",o.reverse?15:5,0)+'"></td></tr>'+
'<tr height="5">'+
'<td>'+imgOffset(_skin+'dialogframe.gif',5,5,o.reverse?10:0,5)+'</td>'+
'<td style="'+backImgOffset(_skin+"dialogframetopbottom.gif",0,o.reverse?15:5)+'"></td>'+
'<td>'+imgOffset(_skin+'dialogframe.gif',5,5,o.reverse?15:5,5)+'</td>'+
'</tr>'+
'</tbody></table>'
}
function newBOColor(r,g,b)
{
var o=new Object;
if (r && (g==null) && (b==null))
{
s = r.split(",")
o.r=parseInt(s[0])
o.g=parseInt(s[1])
o.b=parseInt(s[2])
}
else
{
o.r=r
o.g=g
o.b=b
}
o.set=BOColor_set
o.getCopy=BOColor_getCopy
o.getStringDef=BOColor_getStringDef
o.setStringDef=BOColor_setStringDef
o.getStyleColor=BOColor_getStyleColor
return o;
}
function BOColor_getStringDef()
{
var o=this
return ""+o.r+","+o.g+","+o.b
}
function BOColor_setStringDef(s)
{
var rgb=s?s.split(","):null
var o=this
if (rgb&&(rgb.length==3))
{
o.r = isNaN(rgb[0]) ? 255 : parseInt(rgb[0],10)
o.g = isNaN(rgb[1]) ? 255 : parseInt(rgb[1],10)
o.b = isNaN(rgb[2]) ? 255 : parseInt(rgb[2],10)
}
}
function BOColor_getCopy()
{
var f=this
var o=newBOColor(f.r,f.g,f.b)
return o;
}
function BOColor_set(r,g,b)
{
this.r=r
this.g=g
this.b=b
}
function BOColor_getStyleColor()
{
var o=this
// if undefined
if (o.r == -1 || o.r == null) return null;
if (o.g == -1 || o.g == null) return null;
if (o.b == -1 || o.b == null) return null;
return "rgb(" + o.r + "," + o.g + "," + o.b + ")"
}
function newDragDropData(widget,dragStartCB,dragCB,dragEndCB,acceptDropCB,leaveDropCB,dropCB)
{
var o=new Object
o.widget=widget
o.dragStartCB=dragStartCB
o.dragCB=dragCB
o.dragEndCB=dragEndCB
o.acceptDropCB=acceptDropCB
o.leaveDropCB=leaveDropCB
o.dropCB=dropCB
o.attachCallbacks=DragDropData_attachCallbacks
o.id=_ddData.length
_ddData[o.id]=o
return o
}
function DragDropData_attachCallbacks(lyr,onlyDrop)
{
if (_ie)
{
onlyDrop=(onlyDrop==null)?false:onlyDrop
if (!onlyDrop)
{
lyr.ondragstart=DDD_dragStart
lyr.ondragend=DDD_dragEnd
}
lyr.ondrop=DDD_drop
lyr.ondragleave=DDD_dragLeave
lyr.ondragover=DDD_dragOver
lyr.ondrag=DDD_drag
lyr._dragDropData=this.id
}
}
function DDD_dragStart()
{
var e=_curWin.event,dt=e.dataTransfer
dt.effectAllowed='copyMove'
dt.dropEffect=_curWin.event.ctrlKey?'copy':'move'
var o=_ddData[this._dragDropData]
o.dragStartCB(o.widget,this)
window._globalDDD=o.widget
e.cancelBubble=true
}
function DDD_drag()
{
var e=_curWin.event,dt=e.dataTransfer
dt.dropEffect=e.ctrlKey?'copy':'move'
var o=_ddData[this._dragDropData]
o.dragCB(o.widget,this,e.ctrlKey?false:e.shiftKey)
e.cancelBubble=true
}
function DDD_dragEnd()
{
var o=_ddData[this._dragDropData]
o.dragEndCB(o.widget,this)
window._globalDDD=null
}
function DDD_dragEnter()
{
DDD_dragOverEnter(this,true)
}
function DDD_dragOver()
{
DDD_dragOverEnter(this,false)
}
function DDD_dragOverEnter(layer,isEnter)
{
var o=_ddData[layer._dragDropData],e=_curWin.event
e.dataTransfer.dropEffect=e.ctrlKey?'copy':'move'
var o=_ddData[layer._dragDropData];
if (o.acceptDropCB(window._globalDDD,o.widget,e.ctrlKey,e.ctrlKey?false:e.shiftKey,layer,isEnter))
e.returnValue=false
e.cancelBubble=true
}
function DDD_dragLeave()
{
var o=_ddData[this._dragDropData],e=_curWin.event
o.leaveDropCB(window._globalDDD,o.widget,e.ctrlKey,e.ctrlKey?false:e.shiftKey,this)
}
function DDD_drop()
{
var o=_ddData[this._dragDropData],e=_curWin.event
o.dropCB(window._globalDDD,o.widget,e.ctrlKey,e.ctrlKey?false:e.shiftKey,this)
window._globalDDD=null
e.cancelBubble=true
}
function arrayAdd(obj,fieldName,item,idx)
{
var array=obj[fieldName],len=array.length
if ((idx==null)||(typeof idx!="number")) idx=-1
if ((idx<0)||(idx>len)) idx=len
if  (idx!=len)
{
var end=array.slice(idx)
array.length=idx+1
array[idx]=item
array=array.concat(end)
}
else array[idx]=item
obj[fieldName]=array
return idx
}
function arrayRemove(obj,fieldName,idx)
{
var array=obj[fieldName],last=array.length-1
if (idx==null)
{
array.length=0
obj[fieldName]=array
return -1
}
if ((idx<0)||(idx>last)) return -1
if (idx==last) array.length=last
else
{
var end=array.slice(idx+1)
array.length=idx
array=array.concat(end)
}
obj[fieldName]=array
return idx
}
function arrayMove(obj,fieldName,i,j)
{
var array=obj[fieldName],len=array.length
if ((i<0)||(i>=len)||(j<0)||(j>=len)) return false
var old=array[i]
arrayRemove(obj,fieldName,i)
arrayAdd(obj,fieldName,old,j)
return true;
}
function arrayGetCopy(arr)
{
var o=new Array,len=arr.length;
for (var i=0;i<len;i++)
o[i]=arr[i]
return o;
}
function arrayFind(obj,fieldName,v,subfield)
{
var array=obj[fieldName],len=array.length;
for (var i=0;i<len;i++)
{
if(subfield)
{
if (array[i][subfield] == v) return i;
}
else 
if(array[i]==v) return i;
}
return -1;
}
function getFrame(name,par)
{
if (par==null) par=self
var frames=par.frames,w=eval("frames."+name)
if (w==null) return w
var l=frames.length
for (var i=0;i<l;i++)
{
w=frames[i]
try {
if (w.name==name)
return w
} catch (exc) {
// keep on
}
}
return null
}
function frameNav(name,url,fillHistory,par,noRefreshDrillBar)
{
var fr=null
if (noRefreshDrillBar & name=="Report")
{
var topfs=getTopFrameset();
fr=topfs.getReportFrame()
} else {
fr=getFrame(name,par)
}
if (fr) {
var l=fr.location
if (fillHistory)
l.href=url
else
l.replace(url)
} else {
var lay = document.getElementById(name)
if (lay)
lay.src=url;
}
}
/*
function genericIframeNav(url,fillHistory)
{
var l = getDynamicBGIFrameLayer()
if (fillHistory)
{
l.href=url
} else {
l.replace(url)
} 
}
*/
function frameGetUrl(win)
{
return win.location.href
}
function frameReload(win)
{
var loc=win.location
loc.replace(loc.href)
}
function setTopFrameset()
{
_curWin._topfs="topfs"
}
function getTopFrameset(f)
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
return getTopFrameset(f.parent)
else
return null;
}
}
function convStr(s,nbsp,br)
{
s=""+s
var ret=s.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/"/g,"&quot;")
if (nbsp)
ret=ret.replace(/ /g,"&nbsp;")
if (br)
ret=ret.replace(/\n/g,"<br>")
return ret
}
function escapeCR(s)
{
s=""+s
var ret=s.replace(/\r/g,"").replace(/\n/g,"\\n");
return ret
}
function addDblClickCB(l,cb)
{
if (l.addEventListener && !_saf) {
  l.addEventListener("dblclick",cb,false)  
} else {
  l.ondblclick=cb
} 
}
function img(src,w,h,align,att,alt)
{
att=(att?att:'')
if (alt==null) alt=''
return '<img'+attr('width',w)+attr('height',h)+attr('src', src)+attr('title',alt)+attr("align", align)+' border="0" hspace="0" vspace="0" '+(att?att:'')+'>'
}
function imgOffset(url,w,h,dx,dy,id,att,alt,st,align)
{
return '<div '+attr('id',id)+attr('title',(alt?convStr(alt):null))+'class="imo" style="width:'+w+'px;height:'+h+'px;'+backImgOffset(url,dx,dy)+(st?st:'')+'"'+(att?' '+att:'')+ '></div>'
}
function simpleImgOffset(url,w,h,dx,dy,id,att,alt,st,align)
{
if (_dtd4||_ie7)
return imgOffset(url,w,h,dx,dy,id,att,alt,st,align)
else
{
if (dx==null) dx=0
if (dy==null) dy=0
return '<span '+(att?att:'')+' '+attr("id",id)+' style="padding:0px;width:'+w+'px;height:'+h+'px;overflow:hidden;'+(st?st:'')+'">'+img(url,null,null,(align?align:'top'),'style="margin:0px;position:relative;top:'+(-dy)+'px;left:'+(-dx)+'px"',alt)+'</span>'
}
}
function changeSimpleOffset(lyr,dx,dy,url,alt)
{
if (_dtd4||_ie7)
changeOffset(lyr,dx,dy,url,alt)
else
{
lyr=lyr.childNodes[0]
var st=lyr.style
if ((url!=null)&&(url!=lyr.src))
lyr.src=url
if (dx!=null)
st.left=""+(-dx)+"px"
if (dy!=null)
st.top=""+(-dy)+"px"
if (alt!=null)
lyr.title=alt
}
}
function backImgOffset(url,dx,dy)
{
return 'background-image:url(\''+url+'\');background-position:'+(-dx)+'px '+(-dy)+'px;'
}
function changeOffset(lyr,dx,dy,url,alt)
{
var st=lyr.style
if (st)
{
if ((dx!=null)&&(dy!=null))
st.backgroundPosition=''+(-dx)+'px '+(-dy)+'px'
if (url)
st.backgroundImage='url(\''+url+'\')'
}
if(alt) lyr.title=alt
}
function includeScript(url)
{
document.write('<scr'+'ipt language="javascript" charset="UTF-8" src="'+url+'"><\/scr'+'ipt>')
}
function getLayer(id)
{
return _curDoc.getElementById(id)
}
function setLayerTransp(lyr,percent)
{
if (_ie)
lyr.style.filter=(percent==null) ? "" :  "progid:DXImageTransform.Microsoft.Alpha( style=0,opacity="+percent+")"
else
{
if (_saf||_winSaf)
lyr.style.opacity=(percent==null) ? 1 : percent/100
else
lyr.style.MozOpacity=(percent==null) ? 1 : percent/100
}
}
function getPos(el,relTo)
{
relTo = relTo?relTo:null
for (var lx=0,ly=0;(el!=null)&&(el!=relTo);
lx+=el.offsetLeft,ly+=el.offsetTop,el=el.offsetParent);
return {x:lx,y:ly}
}
function getPosScrolled(el,relTo)
{
relTo = relTo?relTo:null
if (_ie)
{
for (var lx=0,ly=0;(el!=null)&&(el!=relTo);
lx+=el.offsetLeft-el.scrollLeft,ly+=el.offsetTop-el.scrollTop,el=el.offsetParent);
}
else
{
var oldEl=el
for (var lx=0,ly=0;(el!=null)&&(el!=relTo);
lx+=el.offsetLeft,ly+=el.offsetTop,el=el.offsetParent);
for (el=oldEl;(el!=null)&&(el!=relTo);el=el.parentNode)
{
if (el.scrollLeft!=null)
{
lx-=el.scrollLeft
ly-=el.scrollTop
}
}
}
lx+=getScrollX()
ly+=getScrollY()
return {x:lx,y:ly}
}
function getWidget(layer)
{
if (layer==null)
return null
var w=layer._widget
if (w!=null)
return _widgets[w]
else
return getWidget(layer.parentNode)
}
function getWidgetFromID(id)
{
if (id==null)
return null
var l=getLayer(id)
return getWidget(l)
}
function attr(key,val)
{
return (val!=null?' '+key+'="'+val+'" ':'')
}
function sty(key,val)
{
return (val!=null?key+':'+val+';' :'')
}
function getSep(marg,solid)
{
if (marg==null)marg=0;var spc=marg>0?'<td width="'+marg+'">'+getSpace(marg,1)+'</td>':''; return '<table style="margin-top:5px;margin-bottom:5px;" width="100%" cellspacing="0" cellpadding="0"><tr>'+spc+'<td background="'+_skin+'sep'+(solid?'_solid':'')+'.gif" class="smalltxt"><img alt="" src="'+_skin+'../transp.gif" width="10" height="2"></td>'+spc+'</tr></table>'
}
function writeSep(marg,solid)
{
_curDoc.write(getSep(marg,solid))
}
function getSpace(w,h)
{
return '<table height="'+h+'" border="0" cellspacing="0" cellpadding="0"><tr><td>'+img(_skin+'../transp.gif',w,h)+'</td></tr></table>'
}
function writeSpace(w,h)
{
_curDoc.write(getSpace(w,h))
}
function winWidth(win)
{
win=win?win:_curWin
return _ie?win.document.body.clientWidth:win.innerWidth
}
function winHeight(win)
{
win=win?win:_curWin
return _ie?win.document.body.clientHeight:win.innerHeight
}
function getScrollX(win)
{
win=win?win:_curWin
return _ie?win.document.body.scrollLeft:win.scrollX
}
function getScrollY(win)
{
win=win?win:_curWin
return _ie?win.document.body.scrollTop:win.scrollY
}
function getEvent(e,w)
{
if (_ie&&(e==null))
e = w ? w.event : _curWin.event
return e
}
function eventIsCtrl(e,w)
{
e=getEvent(e,w)
return _mac ? e.metaKey : e.ctrlKey
}
function winScrollTo(x, y, win)
{
win=win?win:_curWin
win.scrollTo(x,y)
}
function eventIsLeftButton(e,w)
{
e=getEvent(e,w)
return (e.button == _leftBtn)||(_mac&&!e.ctrlKey)
}
function eventGetKey(e,win)
{
win=win?win:_curWin
return _ie?win.event.keyCode:e.keyCode
}
function eventGetX(e,w)
{
e=getEvent(e,w)
return _saf ? (e.pageX - getScrollX(w)) : e.clientX
}
function eventGetY(e,w)
{
e=getEvent(e,w)
return _saf ? (e.pageY - getScrollY(w)) : e.clientY
}
function xpos(o,e,doc,zoom)
{
if ((zoom==null)||(!_ie))
zoom=1
return (((eventGetX(e) + getScrollX())/zoom)-getPosScrolled(o).x)
}
function ypos(o,e,doc,zoom)
{
if ((zoom==null)||(!_ie))
zoom=1
return (((eventGetY(e) + getScrollY())/zoom)-getPosScrolled(o).y)
}
function absxpos(e,zoom,w)
{
var clientX=eventGetX(e,w)
if ((zoom==null)||(!_ie))
{
return clientX
}
else
{
return clientX/zoom
}
}
function absypos(e,zoom,w)
{
var clientY=eventGetY(e,w)
if ((zoom==null)||(!_ie))
{
return clientY
}
else
{
return clientY/zoom
}
}
function eventCancelBubble(e,win)
{
win=win?win:_curWin
_ie?win.event.cancelBubble=true:e.cancelBubble=true
}
function isHidden(lyr)
{
if ((lyr==null)||(lyr.tagName=="BODY")) return false;var sty=lyr.style;if ((sty==null)||(sty.visibility==_hide)||(sty.display=='none')) return true;return isHidden(lyr.parentNode)
}
function opt(val,txt,sel)
{
return '<option value="'+val+'" '+(sel?'selected':'')+'>'+convStr(''+txt)+'</option>'
}
function lnk(inner,clickCB,cls,id,att,dblClickCB)
{
if (clickCB==null)
clickCB="return false"
att=att?att:'';
return '<a'+attr('class',cls)+attr('id',id)+attr('href','#'+id?id:"")+  
attr('onclick',clickCB)+attr('ondblclick',dblClickCB)+att+'>'+inner +'</a>'
}
_oldErrHandler=null
function localErrHandler()
{
return true
}
function canScanFrames(w)
{
var ex=true,d=null
if (_moz)
{
_oldErrHandler=window.onerror
window.onerror=localErrHandler
}
try
{
d=w.document
ex=false
}
catch(expt)
{
}
if (_moz)
window.onerror=_oldErrHandler
return (!ex&&(d!=null))
}
function getTopAccessibleFrame(w)
{
var maxFrame=50;
while (true)
{
var p=w.parent
if (!canScanFrames(p))
return w
if (w==top)
return w
maxFrame--
if (maxFrame == 0)
return w
w=p
}
}
function restoreAllInputs(win,level)
{
if (_ie&&_curWin._inptStackLevel!=null)
{
win=win?win:_curWin
if (canScanFrames(win))
{
if (level==null)
level=--_curWin._inptStackLevel
var b=win.document.body,arr=b?b.getElementsByTagName("SELECT"):null,len=arr?arr.length:0
for (var i=0;i<len;i++)
{
var inpt=arr[i]
if (inpt._boHideLevel==level)
{
inpt.style.visibility=inpt._boOldVis
inpt._boHideLevel=null
}
}
var frames=win.frames,flen=frames.length
for (var k=0;k<flen;k++)
restoreAllInputs(frames[k],level)
}
}
}
function hideAllInputs(x,y,w,h,win,level)
{
if (_ie)
{
win=win?win:_curWin
if (canScanFrames(win))
{
var b=win.document.body,arr=b?b.getElementsByTagName("SELECT"):null,len=arr?arr.length:0
if (level==null)
{
if (_curWin._inptStackLevel==null)
_curWin._inptStackLevel=0
level=_curWin._inptStackLevel++
}
for (var i=0;i<len;i++)
{
var inpt=arr[i],css=inpt.style;
var inter=(x==null)||isLayerIntersectRect(inpt,x,y,w,h)
if (!isHidden(inpt)&&inter)
{
inpt._boHideLevel=level
inpt._boOldVis=css.visibility
css.visibility=_hide
}
}
var frames=win.frames,flen=frames.length
for (var k=0;k<flen;k++)
hideAllInputs(null,null,null,null,frames[k],level)
}
}
}
function getBGIframe(id)
{
return '<iframe id="'+id+'" name="'+id+'" style="display:none;left:0px;position:absolute;top:0px" src="' + _skin + '../../empty.html' + '" frameBorder="0" scrolling="no"></iframe>'
}
function getDynamicBGIFrameLayer()
{
var recycle=false
if (_curWin.BGIFramePool) 
{
BGIFrames = _curWin.BGIFramePool.split(",")
BGIFCount = BGIFrames.length
for (var id = 0; id < BGIFCount; id++) {
if (BGIFrames[id] != "1") {
recycle=true
break
}
}
} else {
id = 0
BGIFrames = new Array
}
BGIFrames[id] = "1"
_curWin.BGIFramePool = BGIFrames.join(",")
if (!recycle) {
targetApp(getBGIframe("BGIFramePool_" + id))
}
return getLayer("BGIFramePool_" + id)
}
function releaseBGIFrame(layerId) {
var l = getLayer(layerId)
if (l) {
l.style.display="none"
}
id = parseInt(layerId.split('_')[1])
BGIFrames = _curWin.BGIFramePool.split(",")
BGIFrames[id]=0
_curWin.BGIFramePool = BGIFrames.join(",")
}
function append(e,s,c)
{
if (_ie)
e.insertAdjacentHTML("BeforeEnd",s)
else
{
var curDoc = c?c:_curDoc
var r=curDoc.createRange()
r.setStartBefore(e)
var frag=r.createContextualFragment(s)
e.appendChild(frag)
}
}
function insBefore(e,s,c)
{
if (_ie)
e.insertAdjacentHTML("BeforeBegin",s)
else
{
var curDoc = c?c:_curDoc
var r=_curDoc.createRange()
r.setEndBefore(e)
var frag=r.createContextualFragment(s)
e.parentNode.insertBefore(frag,e)
}
}
function targetApp(s)
{
append(_curDoc.body,s)
}
function getBasePath()
{
var url=document.location.href,last1= url.lastIndexOf('?');if (last1>=0) url=url.slice(0,last1);var last = url.lastIndexOf('/');return (last>=0)?url.slice(0,last+1):url
}
function isLayerIntersectRect(l,x1,y1,w,h)
{
var xl1=getPos(l).x,yl1=getPos(l).y,xl2=xl1+l.offsetWidth,yl2=yl1+l.offsetHeight,x2=x1+w,y2=y1+h
return ((x1>xl1)||(x2>xl1))&&((x1<xl2)||(x2<xl2)) && ((y1>yl1)||(y2>yl1))&&((y1<yl2)||(y2<yl2))
}
function preloadImg(url)
{
var img=_preloadArr[_preloadArr.length]=new Image;img.src=url
}
function convURL(str)
{
if (_dontNeedEncoding == null)
{
_dontNeedEncoding = new Array(256);
for (var i = 0 ; i < 256 ; i++) _dontNeedEncoding[i] = false;
for (var i = (new String('a')).charCodeAt(0); i <= (new String('z')).charCodeAt(0); i++) _dontNeedEncoding[i] = true;
for (var i = (new String('A')).charCodeAt(0); i <= (new String('Z')).charCodeAt(0); i++) _dontNeedEncoding[i] = true;
for (var i = (new String('0')).charCodeAt(0); i <= (new String('9')).charCodeAt(0); i++) _dontNeedEncoding[i] = true;
_dontNeedEncoding[(new String(' ')).charCodeAt(0)] = true; 
_dontNeedEncoding[(new String('-')).charCodeAt(0)] = true;
_dontNeedEncoding[(new String('_')).charCodeAt(0)] = true;
_dontNeedEncoding[(new String('.')).charCodeAt(0)] = true;
_dontNeedEncoding[(new String('*')).charCodeAt(0)] = true;
_thex = new Array("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F");
}
var encstr = "";
for (var i = 0 ; i < str.length ; i++) encstr += URLEncodeUTF8Char(str.charAt(i));
return encstr;
}
function URLEncodeUTF8Char(c)
{
var unicodeval = c.charCodeAt(0);
if (unicodeval < 128)
{
if (_dontNeedEncoding[unicodeval]) return (c == ' ' ? '+' : c);
else return ("%" + _thex[unicodeval >> 4] + _thex[unicodeval & 15]);
}
else if (unicodeval < 2048)
{
return ("%" + _thex[(unicodeval >> 10) | 12]
+ _thex[(unicodeval >> 6) & 15]
+ "%"+ _thex[(unicodeval >> 4) & 3 | 8]
+ _thex[unicodeval & 15]);
}
else
{
return ("%"+ _thex[14]
+ _thex[unicodeval >> 12]
+ "%" + _thex[(unicodeval >> 10) & 3 | 8]
+ _thex[(unicodeval >> 6) & 15]
+ "%"+ _thex[(unicodeval >> 4) & 3 | 8]
+ _thex[unicodeval & 15]);
}
}
function encString(s)
{
var res=''
if (s!=null)
{
var len=s.length
for (var i=0;i<len;i++)
{
var c=s.charAt(i)
switch (c)
{
case '$': res+='$3'; break
case ',': res+='$4'; break
case '[': res+='$5'; break
case ']': res+='$6'; break
default: res+=c; break
}
}
}
return res
}
function enc()
{
var args=enc.arguments,len=args.length,s='['
if (len>0) s+=args[0]
for (var i=1;i<len;i++) s+=','+args[i]
return s+']'
}
function remSpaceAround(s)
{
var len = s.length;
if(len<=0) return "";
var start=0,end=len
var c=s.substr(start,1);
while (c==' ' && start<len)
{
start++
c=s.substr(start,1);
}
if(start<len)
{
c=s.substr(end-1,1);
while (c==' ')
{
end--
c=s.substr(end-1,1);
}
}
var sub = s.substring(start,end);
return sub
}
function getArrows(upCb,downCB,hori,newNode,newNodeCB)
{
if (hori==null) hori=false;
var s=''
if (hori) s+='<nobr>'
s+=lnk(img(_skin+(hori?'left.gif':'up.gif'),12,12,'top',null,hori?"_LEFT ARROW":"_UP ARROW"),upCb,null,null,null,(_moz?null:upCb))
s+=(hori?'':img(_skin+'../transp.gif',1,5)+'<br>')
s+=lnk(img(_skin+(hori?'right.gif':'down.gif'),hori?11:12,hori?12:11,'top',null,hori?"_RIGHT ARROW":"_LEFT ARROW"),downCB,null,null,null,(_moz?null:downCB))
if (newNode)
{
s+=img(_skin+'../transp.gif',1,5)+'<br>'
s+=lnk(img(_skin+('node.gif'),12,12,'top',null,"_NEW NODE"),newNodeCB,null,null,null,(_moz?null:newNodeCB))
}
if (hori) s+='</nobr>'
return s
}
_staticUnicBlockWhileWaitWidgetID = "staticUnicBlockWhileWaitWidgetID"
function hideBlockWhileWaitWidget()
{
var lyr=getLayer(_staticUnicBlockWhileWaitWidgetID)
if (lyr)
lyr.style.display="none"
}
function newBlockWhileWaitWidget(urlImg)
{
if (window._BlockWhileWaitWidget!=null)
return window._BlockWhileWaitWidget
var o=newWidget(_staticUnicBlockWhileWaitWidgetID)
o.getPrivateHTML=BlockWhileWaitWidget_getPrivateHTML
o.init=BlockWhileWaitWidget_init
o.show=BlockWhileWaitWidget_show
window._BlockWhileWaitWidget=o
return o
}
function BlockWhileWaitWidget_init()
{
}
function BlockWhileWaitWidget_getPrivateHTML()
{
return '<div id="'+ this.id+'" onselectstart="return false" ondragstart="return false" onmousedown="'+_codeWinName+'.eventCancelBubble(event)" border="0" hspace="0" vspace="0"  style="background-image:url('+_skin+'../transp.gif);z-index:5000;cursor:wait;position:absolute;top:0px;left:0px;width:100%;height:100%"></div>'
}
function BlockWhileWaitWidget_show(show)
{
var o=this
if (o.layer==null)
{
o.layer=getLayer(o.id)
if (o.layer==null)
{
targetApp(o.getPrivateHTML())
o.layer=getLayer(o.id)
o.css=o.layer.style
}
else
{
o.css=o.layer.style
}
}
o.setDisplay(show)
}
var regLang= /^[a-zA-Z]{2}$|^[a-zA-Z]{2}_[a-zA-Z]{2}$/, regIntPos=/^\d+$/, regYes=/^yes$/, regPath=/^[\w|\/|:|.|-]+$/, regAlphanumDot=/^\w+\.+\w+$/, regAlphanumDotEx=/^\w+\.*\w+\.*\w+$/, regBoolean=/[true||false]/, regAlphanumComma=/^\w+\,+\w+$/, regColor = /rgb([\\d,.-]*)/, regNumPlusComma = /[\\d,]*/, regAplhaNumOptionalDot = /^\w+\.*\w*/;;
var paramRegs = new Array()
paramRegs["ID"]=regAplhaNumOptionalDot
paramRegs["allTableCells"]=regYes
paramRegs["gotoPivot"]=regYes
paramRegs["reportIndex"]=regIntPos
paramRegs["fromLPane"]=regYes
paramRegs["skin"]=regPath
paramRegs["lang"]=regLang
paramRegs["noGrabber"]=regYes
paramRegs["isFormulaEdit"]=regYes
paramRegs["fromQuery"]=regYes
paramRegs["isFromHyperlinkEditor"]=regYes
paramRegs["iAction"]=regIntPos
paramRegs["callback"]=regAlphanumDotEx
paramRegs["callbackin"]=regAlphanumDotEx
paramRegs["callbackout"]=regAlphanumDotEx
paramRegs["fntSt"]=regNumPlusComma
paramRegs["fgColor"]=regColor
paramRegs["bgColor"]=regColor
paramRegs["isNum"]=regBoolean
paramRegs["formatName"]=regAlphanumDotEx
paramRegs["index"]=regIntPos
function requestQueryString(win, par){
params = win.location.search.substr(1).split("&")
for(i=0;i<params.length;i++){
var param = params[i].split("="), key = param[0], val = param[1]
if (key == par){
var reg = new RegExp(paramRegs[key])
if ((paramRegs[key]==null) || (val == "") || (reg.test(val))) {
return val
} else {
var tpfs = getTopFrameset()
if (tpfs != null) {
tpfs._dontCloseDoc=true
tpfs.document.location.replace(tpfs._root+"html/badparamserror.html");
} else {
tpfs=getTopFrameset(window.opener)
if (tpfs != null)
{
document.location.replace(tpfs._skin+"../../../html/badparamserror.html");
}
}
}
}
}
}
function trim(strString) {
    if (strString != null) {
        var iLength = strString.length;
        var i;
        for (i=0; i<iLength; i++) {
            if (strString.charAt(i) != " ") {
                break;
            }
        }
        strString = strString.substring(i);
        iLength = strString.length;
        for (i=iLength; i>0; i--) {
            if (strString.charAt(i-1) != " ") {
                break;
            }
        }
        strString = strString.substring(0,i);
    }
    return strString;
}
function startsWithIgnoreCase(strString, strToFind) {
    var blnRet = false;
    if (strToFind != null) {
        var strVar = strString.substring(0, strToFind.length);
        if (strVar.toLowerCase() == strToFind.toLowerCase()) {
            blnRet = true;
        }
    }
    return blnRet;
}
function endsWithIgnoreCase(strString, strToFind) {
    var blnRet = false;
    if (strToFind != null) {
        var iRight = strString.length- strToFind.length;
        if (iRight >= 0) {
            var strVar = strString.substring(iRight);
            if (strVar.toLowerCase() == strToFind.toLowerCase()) {
                blnRet = true;
            }
        }
    }
    return blnRet;
}
function isTextInput(ev)
{
if (_ie)
ev = ev ? ev : _curWin.event;
var source = _ie?ev.srcElement:ev.target;
var isText=false;
if(source.tagName=="TEXTAREA")
isText=true
if((source.tagName=="INPUT") && (source.type.toLowerCase()=="text"))
isText=true
return isText;
}
function isTextArea(ev)
{
var source = _ie?ev.srcElement:ev.target;
if(source.tagName=="TEXTAREA")
return true;
else 
return false;
}
function shrinkTooltip(t,n)
{ 
var n = n?n:360
return (t.length < n)? t : (t.substring(0,n) + "...")
}
function setDateValue(strDateValue, strInputFormat)
{
var strRet = ",,";
var strYear = "";
var strMonth = "";
var strDay = "";
var length = strInputFormat.length;
var sep = "";
for (var i=0; i<length; i++) 
{
var c = strInputFormat.charAt(i);
switch(c) 
{
case "/":
case "-":
case ".":
case ",":
case "\"": sep = c; break;
}
if (sep != "") break;
}
if (sep != "") 
{
var arrInputFormat = strInputFormat.split(sep);
var arrDateValue = strDateValue.split(sep);
for (var i=0; i<arrDateValue.length; i++) 
{
if (arrInputFormat[i] != null && typeof(arrInputFormat[i]) != "undefined") 
{
if (arrInputFormat[i].indexOf('y')>=0) 
{
var iPosA = arrInputFormat[i].indexOf('y');
var iPosB = arrInputFormat[i].lastIndexOf('y');
if (iPosB>=0) 
{
strYear = arrInputFormat[i].substring(iPosA, iPosB + 1);
if (strYear.length >= arrDateValue[i].length) strYear = arrDateValue[i];
else 
{
iPosB = iPosA;
for (var j=iPosA; j<arrDateValue[i].length; j++) 
{
var c = arrDateValue[i].charAt(j);
if (c < '0' || c > '9') break;
else iPosB = j + 1;
}
strYear = arrDateValue[i].substring(iPosA, iPosB);
if (strYear.length <= 2) 
{
var iYear = parseInt(strYear);
if (iYear>=70) iYear += 1900;
else iYear += 2000;
strYear = iYear.toString();
}
}
}
else 
{
return strRet;
}
}
else if (arrInputFormat[i].indexOf('M')>=0) 
{
var iPosA = arrInputFormat[i].indexOf('M');
var iPosB = arrInputFormat[i].lastIndexOf('M');
if (iPosB>=0) 
{
strMonth = arrInputFormat[i].substring(iPosA, iPosB + 1);
if (strMonth.length >= arrDateValue[i].length) strMonth = arrDateValue[i];
else 
{
iPosB = iPosA;
for (var j=iPosA; j<arrDateValue[i].length; j++) 
{
var c = arrDateValue[i].charAt(j);
if (c < '0' || c > '9') break;
else iPosB = j + 1;
}
strMonth = arrDateValue[i].substring(iPosA, iPosB);
}
}
else 
{
return strRet;
}
}
else if (arrInputFormat[i].indexOf('d')>=0) 
{
var iPosA = arrInputFormat[i].indexOf('d');
var iPosB = arrInputFormat[i].lastIndexOf('d');
if (iPosB>=0) 
{
strDay = arrInputFormat[i].substring(iPosA, iPosB + 1);
if (strDay.length >= arrDateValue[i].length) strDay = arrDateValue[i];
else {
iPosB = iPosA;
for (var j=iPosA; j<arrDateValue[i].length; j++) 
{
var c = arrDateValue[i].charAt(j);
if (c < '0' || c > '9') break;
else iPosB = j + 1;
}
strDay = arrDateValue[i].substring(iPosA, iPosB);
}
}
else 
{
return strRet;
}
}
}
}
if (strMonth != "" && strDay != "" && strYear != "" && !(isNaN(strMonth) || isNaN(strDay) || isNaN(strYear))) 
{
strRet = strMonth + ',' + strDay + ',' + strYear;
}
}
return strRet;
}
function LZ(x) {
    return(x<0||x>9?"":"0")+x
}
function formatDate(date,format)
{
var format=format+"";
    var result="";
    var i_format=0;
    var c="";
    var token="";
    var y=date.getFullYear()+"";
    var M=date.getMonth()+1;
    var d=date.getDate();
    var E=date.getDay();
    var H=date.getHours();
    var m=date.getMinutes();
    var s=date.getSeconds();
    var yyyy,yy,MMM,MM,dd,hh,h,mm,ss,ampm,HH,H,KK,K,kk,k;
    var value=new Object();
    if (y.length==2) {
if (y-0>=70) y=""+(y-0+1900);
else y=""+(y-0+2000);
    }
    value["y"]=""+y;
    value["yyyy"]=y;
    value["yy"]=y.substring(2,4);
    value["M"]=M;
    value["MM"]=LZ(M);
    value["MMM"]=_month[M-1];
    value["NNN"]=_month[M+11];
    value["d"]=d;
    value["dd"]=LZ(d);
    value["E"]=_day[E+7];
    value["EE"]=_day[E];
    value["H"]=H;
    value["HH"]=LZ(H);
    if (H==0){value["h"]=12;}
    else if (H>12){value["h"]=H-12;}
    else {value["h"]=H;}
    value["hh"]=LZ(value["h"]);
    if (H>11){value["K"]=H-12;} else {value["K"]=H;}
    value["k"]=H+1;
    value["KK"]=LZ(value["K"]);
    value["kk"]=LZ(value["k"]);
    if (H > 11) { value["a"]=_PM; value["aa"]=_PM;; }
    else { value["a"]=_AM; value["aa"]=_AM; }
    value["m"]=m;
    value["mm"]=LZ(m);
    value["s"]=s;
    value["ss"]=LZ(s);
    while (i_format < format.length) {
        c=format.charAt(i_format); 
        token="";
        while ((format.charAt(i_format)==c) && (i_format < format.length)) {
            token += format.charAt(i_format++);
        }
        if (value[token] != null) { result=result + value[token]; }
        else { result=result + token; }
     }
   return result;
}
function newSearchWidget(id,w,searchCB,helpText)
{
var o=newWidget(id)
o.bMatchCase= false
o.bSearchIconDisabled= false
o.searchField= newTextFieldWidget(id+"Txt",null,50,SearchWidget_keyUpCB,SearchWidget_searchCB,true,_lovSearchFieldLab,w?(w-35):null);
o.searchField.par= o;
o.searchField.setHelpTxt(helpText?helpText:_lovSearchFieldLab);
o.searchIcn= newIconMenuWidget(id+"Icn",_skin+'../lov.gif',SearchWidget_searchCB,null,_lovSearchLab,16,16,0,0,0,0)
o.searchIcn.par= o
o.searchMenu= o.searchIcn.getMenu()
o.normal= o.searchMenu.addCheck(id+"normal",_lovNormalLab,SearchWidget_normalClickCB)
o.matchCase= o.searchMenu.addCheck(id+"matchCase",_lovMatchCase,SearchWidget_matchCaseClickCB)
o.oldInit= o.init
o.searchCB= searchCB
o.init= SearchWidget_init
o.getHTML= SearchWidget_getHTML
o.setCaseSensitive= SearchWidget_setCaseSensitive
o.isCaseSensitive= SearchWidget_isCaseSensitive
o.updateMatchCase= SearchWidget_updateMatchCase
o.getSearchValue= SearchWidget_getSearchValue;
o.setSearchValue= SearchWidget_setSearchValue;
o.resize= SearchWidget_resize;
o.setSearchIconDisabled= SearchWidget_setSearchIconDisabled;
return o;
}
function SearchWidget_getHTML()
{
var o=this, s =''
s='<table id="'+o.id+'" border="0" cellspacing="0" cellpadding="0"><tbody>' +
'<tr>' +
'<td>' + o.searchField.getHTML() + '</td>' +
'<td>' + o.searchIcn.getHTML() + '</td>' +
'</tr>' +
'</tbody></table>';
return s
}
function SearchWidget_resize(w,h)
{
var o = this
o.searchField.resize(w-35,h);
}
function SearchWidget_init()
{
var o=this
o.oldInit();
o.searchField.init()
o.searchIcn.init()
o.searchIcn.setDisabled(o.bSearchIconDisabled || o.searchField.getValue()=='')
o.updateMatchCase(o.bMatchCase)
}
function SearchWidget_isCaseSensitive()
{
return this.bMatchCase;
}
function SearchWidget_setCaseSensitive(b)
{
var o=this
if(o.bMatchCase!=b)
{
o.updateMatchCase(b);
o.bMatchCase=b;
}
}
function SearchWidget_updateMatchCase(b)
{
var o=this
o.normal.check(!b)
o.matchCase.check(b)
if (b)
o.searchIcn.icon.changeImg(55,0)
else
o.searchIcn.icon.changeImg(0,0)
}
function SearchWidget_normalClickCB()
{
var o=this.par.parIcon.par
if(o.bMatchCase) 
o.bMatchCase=false;
o.updateMatchCase(o.bMatchCase);
}
function SearchWidget_matchCaseClickCB()
{
var o=this.par.parIcon.par
if(!o.bMatchCase) 
o.bMatchCase=true;
o.updateMatchCase(o.bMatchCase);
}
function SearchWidget_keyUpCB()
{
var o=this
var p=o.par
p.searchIcn.setDisabled(p.bSearchIconDisabled || o.getValue()=='')
}
function SearchWidget_searchCB()
{
var p=this.par;
if (p.searchCB != null)
p.searchCB();
}
function SearchWidget_getSearchValue()
{
var o=this;
return o.searchField.getValue();
}
function SearchWidget_setSearchValue(s)
{
var o=this;
o.searchField.setValue(s);
}
function SearchWidget_setSearchIconDisabled(b)
{
var o=this;
o.bSearchIconDisabled=(b)?true:false;
}
function newToggleButtonWidget(id,label,cb,width,hlp,tooltip,tabIndex,margin,url,w,h,dx,dy,imgRight,disDx,disDy,togX,togY)
{
var o=newButtonWidget(id,label,cb,width,hlp,tooltip,tabIndex,margin,url,w,h,dx,dy,imgRight,disDx,disDy)
o.togX = togX
o.togY = togY
o.checked=false
o.executeCB=ToggleButtonWidget_executeCB
o.check=ToggleButtonWidget_check
o.isChecked=ToggleButtonWidget_isChecked
o.init=ButtonWidget_init
return o
}
function ToggleButtonWidget_executeCB()
{
var o=this
o.check(!o.checked) 
if (o.cb)
{
if (typeof o.cb!="string")
o.cb()
else
eval(o.cb)
}
}
function ToggleButtonWidget_check(checked)
{
var o=this
if (o.checked != checked)
{
o.checked=checked
if (o.checked)
{
changeSimpleOffset(o.icn,o.togX,o.togY,o.url)
} else {
changeSimpleOffset(o.icn,o.dx,o.dy,o.url);
}
}
if (o.checked&&o.beforeClickCB)
{
if (o.layer)
o.beforeClickCB()
}
}
function ToggleButtonWidget_isChecked()
{
return this.checked
}
function newBrowseWidget(id,tooltip,editable,w, keyDownCB, changeCB)
{
var o=newWidget(id)
o.disabled=false
o.tooltip=tooltip!=null?tooltip:''
o.contentEditable=editable!=null?editable:true
o.width=w
o.oldInit=o.init
o.init=BrowseWidget_init
o.getHTML=BrowseWidget_getHTML
o.getValue=BrowseWidget_getValue
o.setDisabled=TextFieldWidget_setDisabled
o.instIndex=ButtonWidget_currInst
ButtonWidget_inst[ButtonWidget_currInst++]=o
o.changeCB=changeCB
o.keyDownCB=keyDownCB
return o
}
function BrowseWidget_init()
{
var o=this
o.oldInit()
var l=o.layer
if (l==null)
return
l.contentEditable=o.contentEditable
l.style.cursor='default'
}
function BrowseWidget_keydownCB(e)
{
var key=eventGetKey(e);
if (key == 46) return;
if (key == 8) 
e.cancelBubble=true
var o=this
if (o.keyDownCB) o.keyDownCB(key);
}
function BrowseWidget_getHTML()
{
var o=this, s=''
s='<input' + (o.disabled?' disabled':'') + ' onkeydown=" return '+_codeWinName+'.BrowseWidget_keydownCB(event,'+this.instIndex+');"'+ ' onfocus=" return '+_codeWinName+'.BrowseWidget_onfocusCB(event);"'+' onchange=" return '+_codeWinName+'.BrowseWidget_onchangeCB(event);"'+ ' onkeyup=" return '+_codeWinName+'.BrowseWidget_onkeyupCB(event);"'+' type="file" id="' + o.id + '" name="' + o.id + '" class="textinputs"' + attr('title',o.tooltip)+ ' size="' + o.width + '" ondragstart="event.cancelBubble=true" onselectstart="event.cancelBubble=true" \>'
return s
}
function BrowseWidget_getValue()
{
var o=this, l=o.layer
if (l)
return l.value
return null
}
function BrowseWidget_onfocusCB(e)
{
BrowseWidget_onchangeCB(e)
}
function BrowseWidget_onchangeCB(e)
{
var win=win?win:_curWin
var value= _ie?win.event.srcElement.value:e.target.value;
var o=this
if (o.changeCB) o.changeCB((value=="")?true:false);
}
function BrowseWidget_onkeyupCB(e)
{
BrowseWidget_onchangeCB(e)
}

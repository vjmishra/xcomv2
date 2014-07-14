if (window._DHTML_LIB_DIALOG_JS_LOADED==null)
{
_DHTML_LIB_DIALOG_JS_LOADED=true
DialogBoxWidget_modals=new Array;
DialogBoxWidget_instances=new Array
_promptDlgInfo=0
_promptDlgWarning=1
_promptDlgCritical=2
}
function newDialogBoxWidget(id,title,width,height,defaultCB,cancelCB,noCloseButton,isFullScreen)
{
var o=newWidget(id)
o.title=title
o.width=width
o.height=height
o.defaultCB=defaultCB
o.cancelCB=cancelCB
o.noCloseButton=noCloseButton?noCloseButton:false
o.isFullScreen=isFullScreen?isFullScreen:false
o.resizeable=false
o.oldMouseDown=null
o.modal=null
o.hiddenVis=new Array
o.lastLink=null
o.firstLink=null
o.titleLayer = null
o.defaultBtn=null
o.divLayer=null
if (o.isFullScreen)
{
o.otherLayer=new Array
o.oldDisplay=new Array
}
o.oldInit=o.init
o.oldShow=o.show
o.init=DialogBoxWidget_init
o.setResize=DialogBoxWidget_setResize
o.beginHTML=DialogBoxWidget_beginHTML
o.endHTML=DialogBoxWidget_endHTML
o.show=DialogBoxWidget_Show
o.center=DialogBoxWidget_center
o.focus=DialogBoxWidget_focus
o.setTitle=DialogBoxWidget_setTitle
o.getContainerWidth=DialogBoxWidget_getContainerWidth
o.getContainerHeight=DialogBoxWidget_getContainerHeight
DialogBoxWidget_instances[id]=o
o.modal=newWidget('modal_'+id)
o.placeIframe=DialogBoxWidget_placeIframe
o.oldResize=o.resize
o.resize=DialogBoxWidget_resize
o.attachDefaultButton=DialogBoxWidget_attachDefaultButton
o.unload=DialogBoxWidget_unload
if (!_ie)
{
if (o.width!=null) o.width=Math.max(0,width+4)
if (o.height!=null) o.height=Math.max(0,height+4)
}
return o
}
function DialogBoxWidget_setResize(resizeCB,minWidth,minHeight,noResizeW,noResizeH)
{
var o=this;
o.resizeable=true
o.resizeCB=resizeCB
o.minWidth=minWidth?minWidth:50
o.minHeight=minHeight?minHeight:50
o.noResizeW=noResizeW
o.noResizeH=noResizeH
}
function DialogBoxWidget_setTitle(title)
{
var o=this
o.title=title
if (!o.isFullScreen)
{
if (o.titleLayer == null)
o.titleLayer = getLayer('titledialog_'+this.id);
o.titleLayer.innerHTML=convStr(title)
}
}
function DialogBoxWidget_setCloseIcon(lyr,isActive)
{
changeOffset(lyr,0,(isActive==1?0:18))
}
function DialogBoxWidget_beginHTML()
{
with (this)
{
var moveableCb=' onselectstart="return false" ondragstart="return false" onmousedown="'+_codeWinName+'.DialogBoxWidget_down(event,\''+id+'\',this,false);return false;" '
var titleBG="background-image:url("+_skin+"dialogtitle.gif)"
if (isFullScreen)
{
return '<a style="position:absolute;left:-30px;top:-30px; visibility:hidden" id="firstLink_'+this.id+'" href="javascript:void(0)" onfocus="'+_codeWinName+'.DialogBoxWidget_keepFocus(\''+this.id+'\');return false;" ></a>'+
'<table class="dialogzone" border="0" cellspacing="0" cellpadding="0" width="100%" height="100%" cellspacing="0" cellpadding="2" id="'+id+'" style="display:none;padding:0px;visibility:'+_hide+';position:absolute;top:0px;left:0px;"><tr><td valign="top" align="left">'+
'<table border="0"><tr><td id="td_dialog_'+id+'" class="dialogzone" valign="top">'+
'<div class="dialogzone" id="div_dialog_'+id+'">'
}
else
{
var mdl='<div onselectstart="return false" onmouseup="'+_codeWinName+'.DialogBoxWidget_keepFocus(\''+this.id+'\');" onmousedown="'+_codeWinName+'.eventCancelBubble(event)" border="0" hspace="0" vspace="0" src="'+_skin+'../transp.gif" id="modal_'+id+'" style="position:absolute;top:0px;left:0px;width:1px;height:1px">'+(_ie?img(_skin+'../transp.gif','100%','100%',null):'')+'</div>'
return mdl+
'<a style="position:absolute;left:-30px;top:-30px; visibility:hidden" id="firstLink_'+this.id+'" href="javascript:void(0)" onfocus="'+_codeWinName+'.DialogBoxWidget_keepFocus(\''+this.id+'\');return false;" ></a>'+
'<table border="0" cellspacing="0" cellpadding="2" id="'+id+'" style="display:none;padding:0px;visibility:'+_hide+';position:absolute;top:-2000px;left:-2000px;'+sty("width",width?(""+width+"px"):null)+sty("height",height?(""+height+"px"):null)+'"><tr><td id="td_dialog_'+id+'" onresize="'+_codeWinName+'.DialogBoxWidget_resizeIframeCB(\''+id+'\',this)" class="dialogbox" valign="top">'+
'<table width="100%" style="margin-bottom:4px" border="0" cellspacing="0" cellpadding="0"><tr valign="top">'+
'<td '+moveableCb+' style="cursor:move;'+titleBG+'" class="titlezone">'+getSpace(5,18)+'</td>'+
'<td '+moveableCb+' style="cursor:move;'+titleBG+'" class="titlezone" width="100%" valign="middle" align="left"><nobr><span id="titledialog_'+id+'" tabIndex="0" class="titlezone">'+convStr(title)+'</span></nobr></td>'+
'<td class="titlezone" style="'+titleBG+'">'+
(noCloseButton?'':'<a href="javascript:void(0)" onclick="'+_codeWinName+'.DialogBoxWidget_close(\''+id+'\');return false;" title="'+ _closeDialog +'">'+imgOffset(_skin+'dialogelements.gif',18,18,0,18,'dialogClose_'+this.id,'onmouseover="'+_codeWinName+'.DialogBoxWidget_setCloseIcon(this,1)" onmouseout="'+_codeWinName+'.DialogBoxWidget_setCloseIcon(this,0)" ',_closeDialog,'cursor:'+_hand)+'</a>')+
'</td>'+
'</tr></table><div id="div_dialog_'+id+'">'
}
}
}
function DialogBoxWidget_endHTML()
{
return '</div>'+(this.isFullScreen?'</td></tr></table>':"")+'</td></tr></table><a style="position:absolute;left:-30px;top:-30px; visibility:hidden" id="lastLink_'+this.id+'" href="javascript:void(0)" onfocus="'+_codeWinName+'.DialogBoxWidget_keepFocus(\''+this.id+'\');return false;" ></a>' 
}
function DialogBoxWidget_getContainerWidth()
{
var o=this
return o.width-(2+2)
}
function DialogBoxWidget_getContainerHeight() 
{
var o=this
return o.height-(2+18+2+2+2)
}
function DialogBoxWidget_close(id) 
{
var o=DialogBoxWidget_instances[id]
if (o)
{
o.show(false)
if(o.cancelCB!=null) o.cancelCB()
}
}
function DialogBoxWidget_resizeIframeCB(id,lyr)
{
DialogBoxWidget_instances[id].placeIframe()
}
function DialogBoxWidget_placeIframe()
{
var o=this
if (o.iframe)
{
var lyr=o.td_lyr
if (lyr==null)
{
o.td_lyr=lyr=getLayer("td_dialog_"+o.id)
}
o.iframe.resize(lyr.offsetWidth,lyr.offsetHeight)
o.iframe.move(o.layer.offsetLeft,o.layer.offsetTop)
}
}
function DialogBoxWidget_resize(w,h)
{
var o=this;
if (!o.isFullScreen)
{
o.oldResize(w,h);
if (o.iframe)
{
o.iframe.resize(w,h);
}
}
}
function DialogBoxWidget_init()
{
if (this.layer!=null)
return
var o=this
o.oldInit();
if (!o.isFullScreen)
o.modal.init();
o.lastLink=newWidget("lastLink_"+o.id)
o.firstLink=newWidget("firstLink_"+o.id)
o.lastLink.init()
o.firstLink.init()
if (!o.noCloseButton)
{
o.closeButton=getLayer('dialogClose_'+o.id)
DialogBoxWidget_setCloseIcon(o.closeButton,false)
}
}
function DialogBoxWidget_attachDefaultButton(btn)
{
this.defaultBtn=btn;
this.defaultBtn.setDefaultButton();
}
_theLYR=null
_dlgResize=null
function DialogBoxWidget_down(e,id,obj,isResize)
{
_dlgResize=isResize
var o=DialogBoxWidget_instances[id],lyr=o.layer,mod=o.modal.layer
lyr.onmousemove=mod.onmousemove=eval('_curWin.'+_codeWinName+'.DialogBoxWidget_move')
lyr.onmouseup=mod.onmouseup=eval('_curWin.'+_codeWinName+'.DialogBoxWidget_up')
lyr.dlgStartPosx=mod.dlgStartPosx=parseInt(lyr.style.left)
lyr.dlgStartPosy=mod.dlgStartPosy=parseInt(lyr.style.top)
lyr.dlgStartx=mod.dlgStartx=eventGetX(e)
lyr.dlgStarty=mod.dlgStarty=eventGetY(e)
lyr.dlgStartw=mod.dlgStartw=o.getWidth()
lyr.dlgStarth=mod.dlgStarth=o.getHeight()
lyr._widget=mod._widget=o.widx
_theLYR=lyr
eventCancelBubble(e)
if (lyr.setCapture)
lyr.setCapture(true)
}
function DialogBoxWidget_move(e)
{
var o=_theLYR,dlg=getWidget(o)
if (_dlgResize)
{
var newW=Math.max(dlg.minWidth,o.dlgStartw+eventGetX(e)-o.dlgStartx)
var newH=Math.max(dlg.minHeight,o.dlgStarth+eventGetY(e)-o.dlgStarty)
dlg.resize(dlg.noResizeW?null:newW,dlg.noResizeH?null:newH)
if (dlg.firstTR)
{
if (!dlg.noResizeW)
dlg.firstTR.style.width=newW-4
if (!dlg.noResizeH)
dlg.secondTR.style.height=newH-44
}
if (dlg.resizeCB)
dlg.resizeCB(newW,newH)
}
else
{
var x=Math.max(0,o.dlgStartPosx-o.dlgStartx+eventGetX(e))
var y=Math.max(0,o.dlgStartPosy-o.dlgStarty+eventGetY(e))
x = Math.min( Math.max(10,winWidth()-10), x)
y = Math.min( Math.max(10,winHeight()-18), y)
if (dlg.iframe)
dlg.iframe.move(x,y)
dlg.move(x,y)
}
eventCancelBubble(e)
return false
}
function DialogBoxWidget_up(e)
{
var o=getWidget(_theLYR),lyr=o.layer,mod=o.modal.layer;
lyr.onmousemove=mod.onmousemove=null;
lyr.onmouseup=mod.onmouseup=null;
if (lyr.releaseCapture)
lyr.releaseCapture();
_theLYR=null
}
function DialogBoxWidget_keypress(e)
{
eventCancelBubble(e)
var dlg=currntDlgGet()
if (dlg!=null)
{
switch( eventGetKey(e))
{
case 13:
if(dlg.yes && !dlg.no) 
{ 
if (dlg.defaultCB) dlg.defaultCB();return false; 
} 
if(isTextArea(_ie?_curWin.event:e)) return true;
if(dlg.defaultBtn!=null && !dlg.defaultBtn.isDisabled())
{
dlg.defaultBtn.executeCB(); return false;
}
break;
case 27:
hideBlockWhileWaitWidget()
if (dlg.cancelCB!=null) 
    dlg.cancelCB()
else
    dlg.show(false)    
return false;
break;
case 8: 
return isTextInput(_ie?_curWin.event:e);
break;
}
}
}
function DialogBoxWidgetResizeModals(e)
{
for (var i in DialogBoxWidget_modals)
{
m_sty=DialogBoxWidget_modals[i]
m_sty.width='100%'
m_sty.height='100%'
}
}
function DialogBoxWidget_center()
{
var o=this, scrY=getScrollY(),scrX=getScrollX()
if (!o.isFullScreen)
{
o.height=o.layer.offsetHeight;
o.width=o.layer.offsetWidth;
o.move(Math.max(0,scrX+(winWidth()-o.width)/2),Math.max(0,scrY+(winHeight()-o.height)/2));
o.placeIframe()
}
}
function DialogBoxWidget_Show(sh)
{
with (this)
{
if (!isFullScreen)
m_sty=modal.css
l_sty=css
if (sh)
{
if (isFullScreen)
{
var body=_curDoc.body
this.oldClassName=body.className
if (_ie)
{
this.oldBodyLeftMargin=body.leftMargin
this.oldBodyTopMargin=body.topMargin
body.leftMargin=0
body.topMargin=0
}
body.className="dialogzone"
var nodes=body.childNodes,nodesLen=nodes.length
otherLayer.length=0
oldDisplay.length=0
var j=0
for (var i=0;i<nodesLen;i++)
{
var node=nodes[i]
if (node.nodeType!=3)
{
if ((node.id!=id)&&(node.id!=('firstLink_'+id))&&(node.id!=('lastLink_'+id))&&node.style)
{
otherLayer[j]=node
oldDisplay[j]=node.style.display
node.style.display="none"
j++
}
}
}
}
if (!this.isFullScreen&&!this.iframe)
{
this.iframe=newWidget(getDynamicBGIFrameLayer().id)
this.iframe.init()
}
currntDlgUnregister(this)
currntDlgRegister(this)
oldMouseDown=_curDoc.onmousedown
_curDoc.onmousedown=null
hideBlockWhileWaitWidget()
}
else
{
if (this.isFullScreen)
{
var body = _curDoc.body
body.className=this.oldClassName
if (_ie)
{
body.leftMargin=this.oldBodyLeftMargin
body.topMargin=this.oldBodyTopMargin
}
var j=0,nodesLen=otherLayer.length
for (var j=0;j<nodesLen;j++)
{
otherLayer[j].style.display=oldDisplay[j]
}
otherLayer.length=0
oldDisplay.length=0
}
currntDlgUnregister(this)
_curDoc.onmousedown=oldMouseDown
}
var sameState=(layer.isShown==sh)
if (sameState)
return
layer.isShown=sh
if (sh)
{
if (_curWin.DialogBoxWidget_zindex==null)
_curWin.DialogBoxWidget_zindex=1000
if (this.iframe)
this.iframe.css.zIndex=_curWin.DialogBoxWidget_zindex++;
if (!this.isFullScreen)
{
m_sty.zIndex=_curWin.DialogBoxWidget_zindex++;
DialogBoxWidget_modals[DialogBoxWidget_modals.length]=m_sty
m_sty.display=''
}
l_sty.zIndex=_curWin.DialogBoxWidget_zindex++;
l_sty.display='block'
if (this.iframe)
this.iframe.setDisplay(true)
DialogBoxWidgetResizeModals()
this.height=layer.offsetHeight;
this.width=layer.offsetWidth;
var _small=isSmallScreen()
if(_small && height)
{
if(divLayer == null)
divLayer=getLayer("div_dialog_"+id);
if(divLayer)
{
divLayer.style.overflow="auto";
divLayer.style.height = (winHeight()< height)? (winHeight()-40) : getContainerHeight();
divLayer.style.width = (_moz?width+20:getContainerWidth());
}
resize(null,((winHeight()< height)? (winHeight()-10):null));
}
if (isHidden(layer))
{
this.center()
}
if (!_small && this.resizeCB)
this.resizeCB(width,height)
}
else
{
var l=DialogBoxWidget_modals.length=Math.max(0,DialogBoxWidget_modals.length-1)
if (!this.isFullScreen)
{
m_sty.width='1px'
m_sty.height='1px'
m_sty.display='none'
}
l_sty.display='none'
move(-2000,-2000);
if (this.iframe != null) {
this.iframe.move(-2000,-2000)
this.iframe.setDisplay(false)
releaseBGIFrame(this.iframe.id)
}
}
if (!this.isFullScreen)
modal.show(sh);
firstLink.show(sh)
lastLink.show(sh)
oldShow(sh);
var prevDlg=currntDlgGet()
if (prevDlg)
prevDlg.focus()
}
}
function DialogBoxWidget_unload()
{
if (this.iframe) {
releaseBGIFrame(this.iframe.id)
}
currntDlgUnregister(this)
}
function DialogBoxWidget_keepFocus(id)
{
var o=DialogBoxWidget_instances[id];
if (o)
safeSetFocus(o.layer)
}
function DialogBoxWidget_focus()
{
with (this)
{
if (!isFullScreen)
{
if (titleLayer == null)
titleLayer = getLayer('titledialog_'+id);
if (titleLayer.focus)
safeSetFocus(titleLayer)
}
}
}
function newAlertDialog(id,title,text,okLabel,promptType,yesCB)
{
return newPromptDialog(id,title,text,okLabel,null,promptType,yesCB,null,true)
}
function newPromptDialog(id,title,text,okLabel,cancelLabel,promptType,yesCB,noCB,noCloseButton, width, height)
{
var o=newDialogBoxWidget(id,title,(width)?width:300,(height)?height:null,PromptDialog_defaultCB,PromptDialog_cancelCB,noCloseButton)
o.text=text
o.getHTML=PromptDialog_getHTML
o.yes=okLabel?newButtonWidget(id+"_yesBtn",okLabel,'PromptDialog_yesCB(\"'+o.id+'\")',70):null
o.no=cancelLabel?newButtonWidget(id+"_noBtn",cancelLabel,'PromptDialog_noCB(\"'+o.id+'\")',70):null
o.yesCB=yesCB
o.noCB=noCB
o.promptType=promptType
o.txtLayer = null
o.imgLayer = null
o.setPromptType=PromptDialog_setPromptType
o.setText=PromptDialog_setText
if(o.yes) o.attachDefaultButton(o.yes)
else if(o.no) o.attachDefaultButton(o.no)
return o
}
function PromptDialog_getimgPath(promptType)
{
var imgPath=_skin
switch(promptType)
{
case _promptDlgInfo: imgPath+='information_icon.gif';break
case _promptDlgWarning: imgPath+='warning_icon.gif';break
default: imgPath+='critical_icon.gif';break
}
return imgPath
}
function PromptDialog_getimgAlt(promptType)
{
var imgAlt=''
return imgAlt
}
function PromptDialog_setPromptType(promptType)
{
var o=this
if (o.imgLayer == null)
o.imgLayer = getLayer("dlg_img_"+o.id)
o.imgLayer.src=PromptDialog_getimgPath(promptType)
o.imgLayer.alt=PromptDialog_getimgAlt(promptType)
}
function PromptDialog_setText(text)
{
var o=this
o.text=text
if (o.txtLayer == null)
o.txtLayer = getLayer("dlg_txt_"+o.id)
o.txtLayer.innerHTML=convStr(text,false,true)
}
function PromptDialog_getHTML()
{
var o=this
var imgPath=PromptDialog_getimgPath(o.promptType)
var imgAlt=PromptDialog_getimgAlt(o.promptType)
return o.beginHTML()+
'<table class="dialogzone" width="' + o.width + '"'+' cellpadding="0" cellspacing="5" border="0">'+
'<tr><td>'+
'<table class="dialogzone" cellpadding="5" cellspacing="0" border="0">'+
'<tr><td align="right" width="32" >'+img(imgPath,32,32,null,'id="dlg_img_'+o.id+'"',imgAlt)+'</td><td></td><td id="dlg_txt_'+o.id+'" align="left" tabindex="0">'+convStr(o.text,false,true)+'</td></tr>'+
'</table>'+
'</td></tr>'+
'<tr><td>'+getSep()+'</td></tr>'+
'<tr><td align="right">'+
'<table cellpadding="5" cellspacing="0" border="0">'+
'<tr>'+
(o.yes?'<td>'+o.yes.getHTML()+'</td>':'')+
(o.no?'<td>'+o.no.getHTML()+'</td>':'')+
'</tr>'+
'</table>'+
'</td></tr>'+
'</table>'+
o.endHTML()
}
function PromptDialog_defaultCB()
{
var o=this
if (o.yesCB)
{
if (typeof o.yesCB!="string")
o.yesCB()
else
eval(o.yesCB)
}
this.show(false)
}
function PromptDialog_cancelCB()
{
var o=this
if (o.noCB)
{
if (typeof o.noCB!="string")
o.noCB()
else
eval(o.noCB)
}
this.show(false)
}
function PromptDialog_yesCB(id)
{
DialogBoxWidget_instances[id].defaultCB()
}
function PromptDialog_noCB(id) 
{
DialogBoxWidget_instances[id].cancelCB()
}
function debugInst(step)
{
return 
var s=""
if (_curWin.DialogBoxWidget_currentCount)
{
s="count=" + _curWin.DialogBoxWidget_currentCount+"\n"
for (var i=0;i<_curWin.DialogBoxWidget_currentCount;i++)
{
s+="    " + i +"--> " + eval("_curWin.DialogBoxWidget_inst_" + i + ".id") + "\n"
}
}
else
{
s="no instances cached"
}
alert(step + "\n"+s)
}
function currntDlgRegister(dlg)
{
if (dlg==null)
return
if (_curWin.DialogBoxWidget_currentCount==null)
_curWin.DialogBoxWidget_currentCount=0
if (_curWin.DialogBoxWidget_currentCount==0)
_curWin.DialogBoxWidget_oldKeyCB=_curDoc.body.onkeydown
eval("_curWin.DialogBoxWidget_inst_" + (_curWin.DialogBoxWidget_currentCount) + "=dlg;")
eval("_curWin.DialogBoxWidget_keyCB_" + (_curWin.DialogBoxWidget_currentCount) + "=DialogBoxWidget_keypress;")
_curWin.DialogBoxWidget_currentCount++
_curDoc.body.onkeydown=DialogBoxWidget_keypress
debugInst("register")
}
function currntDlgUnregister(dlg)
{
if (dlg==null)
return
debugInst("BEFORE unregister")
var count = _curWin.DialogBoxWidget_currentCount
if (count)
{
var idx=-1,curDlg=null
for (var i=0;i<count;i++)
{
eval("curDlg=_curWin.DialogBoxWidget_inst_" + i + ";")
if (curDlg.id==dlg.id)
{
idx=i
break;
}
}
if (idx>=0)
{
eval("curDlg=_curWin.DialogBoxWidget_inst_" + idx + "=null;")
eval("curDlg=_curWin.DialogBoxWidget_keyCB_" + idx + "=null;")
for (var i=(idx+1);i<count;i++)
{
eval("_curWin.DialogBoxWidget_inst_" + (i-1) + "=_curWin.DialogBoxWidget_inst_" +i+";")
eval("_curWin.DialogBoxWidget_keyCB_" + (i-1) + "=_curWin.DialogBoxWidget_keyCB_" +i+";")
if (i==(count-1))
{
eval("_curWin.DialogBoxWidget_inst_" + i + "=null;")
eval("_curWin.DialogBoxWidget_keyCB_" + i + "=null;")
}
}
_curWin.DialogBoxWidget_currentCount--
if (_curWin.DialogBoxWidget_currentCount == 0)
_curDoc.body.onkeydown=_curWin.DialogBoxWidget_oldKeyCB
else
eval("_curDoc.body.onkeydown=_curWin.DialogBoxWidget_keyCB_" + (_curWin.DialogBoxWidget_currentCount-1) + ";")
}
}
debugInst("AFTER unregister")
}
function currntDlgGet()
{
if ((_curWin.DialogBoxWidget_currentCount!=null)&&(_curWin.DialogBoxWidget_currentCount>0))
return  eval("_curWin.DialogBoxWidget_inst_" + (_curWin.DialogBoxWidget_currentCount-1) + ";")
else
return null
}

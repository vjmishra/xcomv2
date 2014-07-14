function newTreePromptWidget(id,w,h,clickCB,doubleClickCB,bgClass,expandCB,collapseCB)
{
var o=newTreeWidget(id,w,h,_skin+'../prompt.gif',clickCB,doubleClickCB,bgClass,expandCB,collapseCB)
o.padding=0
o.iconW=15
o.iconH=17
o.dispIcnFuncName="dispIcnPrompt"
return o
}
function newTreePromptElem(name,values,userData,isOptional)
{
var o=newTreeWidgetElem(3,name,userData,null,2,null)
o.isPrompt=true
o.values=values
o.isOptional=isOptional?isOptional:false;
o.getHTML=TreePromptElem_getHTML
o.isChecked=TreePromptElem_isChecked 
o.isAnswered=TreePromptElem_isAnswered
o.change=TreePromptElem_change
o.selectedClass='promptSelected'
o.nonselectedClass='promptNormal'
o.select=TreePromptElem_select
o.selected=false
o.checkIcn=null
o.init=TreePromptElem_init
return o
}
function TreePromptElem_init()
{
var o=this
o.domElem=getLayer(_codeWinName+'trLstElt' + o.id)
o.icnLyr=getLayer(_codeWinName+'icn' + o.id)
o.toggleLyr=getLayer(_codeWinName+'trTog' + o.id)
}
function TreePromptElem_change(name,values,isOptional)
{
var o=this
o.name=name
o.values=values
o.isOptional=(isOptional==null)?o.isOptional:isOptional;
if (o.domElem==null)
o.domElem=getLayer(_codeWinName+"trLstElt"+o.id);
if (o.domElem!=null)
{
o.domElem.innerHTML=convStr(o.name)+(o.isOptional?'<span style="color:#808080">&nbsp;'+_promptOptional+'&nbsp;</span>':'')+(o.values!=null?('<b>&nbsp;'+convStr(o.values)+'</b>'):(o.isOptional?'<span style="color:#808080">&nbsp;'+_promptIgnored+'&nbsp;</span>':''))
if (o.par==null)
{
if (o.checkIcn==null)
o.checkIcn=getLayer(_codeWinName+o.id+"_checkIcn")
var deltaX,icnAlt;
if(o.isAnswered())
{
deltaX=0;
icnAlt=_checkedPromptLab;
}
else if(o.isOptional)
{
deltaX=68; 
icnAlt="";
}
else
{
deltaX=17;
icnAlt=_nocheckedPromptLab;
}
changeSimpleOffset(o.checkIcn,0,deltaX,null,icnAlt)
}
if(o.values)
o.advTooltip=_selectionPromptLab +' '+ o.values
else
o.advTooltip=_noselectionPromptLab
}
}
function TreePromptElem_select(setFocus,ev)
{
with (this)
{
if (treeView.selId!=id)
{
if (treeView.selId>=0)
{
var prev=_TreeWidgetElemInstances[treeView.selId]
prev.init()
var e=prev.domElem
e.className=prev.nonselectedClass
e.parentNode.parentNode.className=prev.nonselectedClass
var prevIcn=prev.icnLyr
if (prevIcn==null)
prevIcn=prev.icnLyr=getLayer(_codeWinName+"icn"+prev.id)
prevIcn.parentNode.className=prev.nonselectedClass
prev.selected=false
var arrowL=prevIcn.childNodes[0]
changeSimpleOffset(arrowL,prev.selected?15:0,prev.sub.length==0?17*4:(prev.expanded?17*2:17*3))
}
treeView.selId=id;
init()
treeView.layer._BOselId=id
var de=domElem
de.className=selectedClass
de.parentNode.parentNode.className=selectedClass
if (this.icnLyr==null)
this.icnLyr=getLayer(_codeWinName+"icn"+id)
this.icnLyr.parentNode.className=selectedClass
this.selected=true
var arrowL=this.icnLyr.childNodes[0]
changeSimpleOffset(arrowL,this.selected?15:0,this.sub.length==0?17*4:(this.expanded?17*2:17*3))
if (setFocus)
de.focus()
if (treeView.clickCB) treeView.clickCB(userData)
}
}
if (ev)
eventCancelBubble(ev) 
}
function TreePromptElem_isChecked()
{
return (this.values!=null || this.isOptional)
}
function TreePromptElem_isAnswered()
{
return this.values!=null
}
function TreePromptElem_getHTML(indent,isFirst)
{
var o=this,a=new Array,i=0,len=o.sub.length,exp=(len>0)||o.isIncomplete,icns=o.treeView.icns
var bord="border-top:0px;"
if (isFirst)
{
o.treeView.isFirst=false
bord=""
}
var isLast=false
if (o.par)
{
var psub=o.par.sub,plen=psub.length
if (plen>0)
isLast = (psub[plen-1].id==o.id)
}
if ((o.par!=null)&&isLast)
bord+="border-bottom:0px;"
var cliEvt=' onclick="return '+_codeWinName+'.TreeWidget_clickCB('+o.id+',false,event)" onmousedown="return '+_codeWinName+'.TreeWidget_clickCB('+o.id+',false,event);" ondblclick="'+_codeWinName+'.treeDblClickCB('+o.id+',event);return false" '
var valuesPart=(o.values!=null?('<b>&nbsp;'+convStr(o.values)+'</b>'):(o.isOptional?'<span style="color:#808080">'+_promptIgnored+'</span>':''))
a[i++]='<table style="border:1px solid #E2E2E2;border-right:0px;'+bord+'" width="100%" border="0" cellspacing="0" cellpadding="0">'
a[i++]='<tr valign="top">'
if (o.par==null)
{
var deltaX,icnAlt;
if(o.isAnswered())
{
deltaX=0;
icnAlt=_checkedPromptLab;
}
else if(o.isOptional)
{
deltaX=68; 
icnAlt="";
}
else
{
deltaX=17;
icnAlt=_nocheckedPromptLab;
}
a[i++]='<td width="15" style="border-right:1px solid #E2E2E2;">'+simpleImgOffset(icns,15,17,0,deltaX,_codeWinName+o.id+"_checkIcn",null,icnAlt)+'</td>'
}
a[i++]='<td '+cliEvt+' class="promptNormal" width="15"><span id="'+_codeWinName+'icn'+o.id+'" '+(_moz?'onclick':'onmousedown')+'="return '+_codeWinName+'.TreeWidget_clickCB('+o.id+',true,event);" ondblclick="'+_codeWinName+'.treeDblClickCB('+o.id+',event);return false">'+simpleImgOffset(icns,15,17,0,exp?(o.expanded?34:51):68)+'</span></td>'
a[i++]='<td  '+cliEvt+' class="promptNormal" width="100%" style="padding-top:2px;padding-right:0px;cursor:hand"><nobr>'
a[i++]='&nbsp;<a class="promptNormal" id="'+_codeWinName+'trLstElt'+o.id+'" href="javascript:void(0)" onfocus="'+_codeWinName+'.treeFCCB(this,'+o.id+',true)" onblur="'+_codeWinName+'.treeFCCB(this,'+o.id+',false)">'
a[i++]=''+convStr(o.name)+(o.isOptional?'<span style="color:#808080">&nbsp;'+_promptOptional+'&nbsp;</span>':"")+valuesPart+'</a>'
a[i++]='</nobr>'
if (exp)
a[i++]='<table width="100%" style="display:'+(o.expanded?'block':'none')+' " border="0" cellspacing="0" cellpadding="0"><tr><td id="'+_codeWinName+'trTog'+o.id+'" style="padding:0px;padding-left:20px;padding-top:3px;">'
if (o.expanded)
{
o.generated=true
var idt=indent+_trIndent
for (var j=0;j<len;j++) a[i++]=o.sub[j].getHTML(idt,j==0);
o.treeView.isFirst=false
}
if (exp)
{
nodeIndent=indent
a[i++]="</td></tr></table>"
}
a[i++]='</td></tr></table>'
if(o.values)
o.advTooltip=_selectionPromptLab +' '+ o.values
else
o.advTooltip=_noselectionPromptLab
return a.join("");
}
function dispIcnPrompt(eId)
{
var e=_TreeWidgetElemInstances[eId]
with (e)
{
select()
if (expanded&&!generated)
{
e.generated=true;
var s='',len=sub.length,newInd=nodeIndent+_trIndent
for (var j=0;j<len;j++) s+=sub[j].getHTML(newInd,j==0);
toggleLyr.innerHTML=s;
}
var icn=treeView.icns[iconId]
toggleLyr.parentNode.parentNode.parentNode.style.display=expanded?'block':'none'
var arrowL=icnLyr.childNodes[0]
changeSimpleOffset(arrowL,selected?15:0,expanded?17*2:17*3)
}
}

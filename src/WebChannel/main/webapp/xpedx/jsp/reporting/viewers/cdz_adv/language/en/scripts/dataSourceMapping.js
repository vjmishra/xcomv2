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
*/_p=parent
_img=_p._img
_DSMappingDPNameType=0
_DSMappingRowType=1
_AMBIGUOUS=0
_CUSTOM=1
_NOT_FOUND=2
_OK=3
_mapIcnW=16
_qualifIcnW=16
_editBtnW=16
_columnW=0
_borderW=1
_padding=1
_scrollbarW=16
function getMappingIcnIdx(mapping)
{
var icnIdx=0
switch (mapping)
{
case _OK:
icnIdx=0
break
case _NOT_FOUND:
icnIdx=2
break
case _AMBIGUOUS:
icnIdx=1
break
case _CUSTOM:
icnIdx=0
break
}
return icnIdx
}
function newDPMapping(name,idx)
{
var o=new Object
o.idx=idx
o.name=name
o.arr=new Array
o.add=DPMapping_add
return o
}
function DPMapping_add(dpIdx,mapIdx,mapping,sName,sID,sQualif,sType,sDesc,tName,tID,
tQualif,tType,tDesc)
{
var o=this
o.arr[o.arr.length]=newMapping(dpIdx,mapIdx,mapping,sName,sID,sQualif,sType,sDesc,
tName,tID,tQualif,tType,tDesc)
}
function newMapping(dpIdx,mapIdx,mapping,sName,sID,sQualif,sType,sDesc,tName,tID,
tQualif,tType,tDesc)
{
var o=new Object
o.dpIdx=dpIdx
o.mapIdx=mapIdx
o.mapping=mapping
o.src=newBOObj(sName,sID,sQualif,sType,sDesc)
if (typeof(tName)=="undefined")
tName=null
if (typeof(tID)=="undefined")
tID=null
if (typeof(tName)=="undefined")
tName=null
if (typeof(tQualif)=="undefined")
tQualif=-1
if (typeof(tType)=="undefined")
tType=null
if (typeof(tDesc)=="undefined")
tDesc=null
o.trg=newBOObj(tName,tID,tQualif,tType,tDesc)
return o
}
function newDSMappingWidget(id,w,h)
{
var o=newScrolledZoneWidget(id,_borderW,_padding,w,h)
o.rows=new Array
o.tblW=o.w-2*o.borderW-2*o.padding-_scrollbarW
o.colW=(o.tblW-_mapIcnW-_qualifIcnW*2-_editBtnW-5*_borderW)/2
o.tblLayer=null
o.selId=null
if (window._DSMappingInstances==null)
window._DSMappingInstances=new Array
o.oldInit=o.init
o.init=DSMappingWidget_init
o.getTableHTML=DSMappingWidget_getTableHTML
o.getHTML=DSMappingWidget_getHTML
o.addDSMappingRow=DSMappingWidget_addDSMappingRow
o.addDPNameRow=DSMappingWidget_addDPNameRow
o.deleteAll=DSMappingWidget_deleteAll
o.rebuildHTML=DSMappingWidget_rebuildHTML
o.selectDSMappingRow=DSMappingWidget_selectDSMappingRow
o.unSelectDSMappingRow=DSMappingWidget_unSelectDSMappingRow
o.changeDSMappingRow=DSMappingWidget_changeDSMappingRow
o.getSelection=DSMappingWidget_getSelection
return o
}
function DSMappingWidget_init()
{
var o=this
o.oldInit()
o.tblLayer=getLayer(_codeWinName+'tblCont_'+o.id)
}
function DSMappingWidget_getTableHTML()
{
var o=this, s=''
var borRig='border-right:1px solid #E2E2E2'
var borBot='border-bottom:1px solid #E2E2E2'
var borTop='border-top:1px solid #E2E2E2'
s+='<table align="center" cellspacing="0" cellpadding="0" width="'+o.tblW+'"><tbody>'
for (var i=0;i<o.rows.length;i++)
{
var e=o.rows[i]
switch (e.type)
{
case _DSMappingDPNameType:
{
s+= '<tr><td class="treeNormal" colspan="6" style="'+borBot+';">'+e.name+'</td></tr>'
break
}
case _DSMappingRowType:
{
s+='<tr id="'+_codeWinName+'row_'+e.id+'" class="treeNormal" onclick="eventCancelBubble(event);return false" onmousedown="'+_codeWinName+'.DSMappingWidget_clickCB('+e.id+',false,event,true);return false">'
s+='<td align="center" style="'+borRig+';'+borBot+';" width="'+_mapIcnW+'">'
s+=simpleImgOffset(_img+'changeSource.gif',16,16,getMappingIcnIdx(e.mapping)*16,0,_codeWinName+'map_icn_'+e.id)
s+='</td>'
s+='<td align="center" style="'+borBot+';" width="'+_qualifIcnW+'">'
s+=simpleImgOffset(_img+'qualificationIcons.gif',16,16,0,e.srcQualif*16,_codeWinName+'src_icn_'+e.id)
s+='</td>'
s+='<td style="'+borRig+';'+borBot+';" width="'+o.colW+'">'
s+='<div style="width:'+(o.colW-2)+'px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;" id="'+_codeWinName+'src_'+e.id+'" title="'+e.srcName+'">'+e.srcName+'</div>'
s+='</td>'
s+='<td align="center" style="'+borBot+';" width="'+_qualifIcnW+'">'
s+=simpleImgOffset(_img+'qualificationIcons.gif',16,16,0,e.trgQualif*16,_codeWinName+'trg_icn_'+e.id)
s+='</td>'
s+='<td style="'+borRig+';'+borBot+';" width="'+o.colW+'">'
s+='<div style="width:'+(o.colW-2)+'px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;" id="'+_codeWinName+'trg_'+e.id+'" title="'+e.trgName+e.trgComment+'" >'+e.trgName+e.trgComment+'</div>'
s+='</td>'
s+='<td align="center" style="'+borBot+';" width="'+_editBtnW+'">'
s+='<div onclick="'+_codeWinName+'.DSMappingWidget_editBtnClickCB(\''+e.id+'\')";>'+simpleImgOffset(_img+'editButton.gif',17,16,0,0,null,null,null,'cursor:'+_hand+';')+'</div>'
s+='</td>'
s+='</tr>'
break
}
}
}
s+='</tbody></table>'
return s
}
function DSMappingWidget_getHTML()
{
var o=this, s=''
s+= o.beginHTML()
s+= '<div id="'+_codeWinName+'tblCont_'+o.id+'">'
s+= o.getTableHTML()
s+= '</div>'
s+= o.endHTML()
return s
}
function DSMappingWidget_addDSMappingRow(mapping,srcQualif,srcName,trgQualif,trgName,clickCB,editCB,usrData)
{
var o=this
var e=newDSMappingRow(_DSMappingInstances.length,o,mapping,srcQualif,srcName,trgQualif,trgName,clickCB,editCB,usrData)
o.rows[o.rows.length]=e
_DSMappingInstances[_DSMappingInstances.length]=e
}
function DSMappingWidget_addDPNameRow(name)
{
this.rows[this.rows.length]=newDPNameRow(name)
}
function DSMappingWidget_deleteAll()
{
var o=this
o.rows.length=0
}
function DSMappingWidget_rebuildHTML()
{
var o=this
if (o.tblLayer)
o.tblLayer.innerHTML=o.getTableHTML()
}
function DSMappingWidget_selectDSMappingRow(e)
{
var o=this
if (e==null)
return
o.unSelectDSMappingRow(_DSMappingInstances[o.selId])
var l=getLayer(_codeWinName+'row_'+e.id)
if (l)
l.className='treeSelected'
o.selId=e.id
}
function DSMappingWidget_unSelectDSMappingRow(e)
{
var o=this
if (e==null)
return
var l=getLayer(_codeWinName+'row_'+e.id)
if (l)
l.className='treeNormal'
o.selId=null
}
function DSMappingWidget_getSelection()
{
var o=this
if (o.selId!=null)
return _DSMappingInstances[o.selId]
else
return null
}
function DSMappingWidget_changeDSMappingRow(id,map,qualif,s)
{
var o=this
var e=_DSMappingInstances[id]
if (e==null)
return
e.mapping=map
var icnl=getLayer(_codeWinName+'map_icn_'+e.id)
if (icnl)
changeSimpleOffset(icnl,getMappingIcnIdx(e.mapping)*16,0,_img+'changeSource.gif')
e.trgQualif=qualif
var icnl=getLayer(_codeWinName+'trg_icn_'+e.id)
if (icnl)
changeSimpleOffset(icnl,0,e.trgQualif*16,_img+'qualificationIcons.gif')
var title="";
if (s==null)
s='&nbsp;'
else
title=s;
if (e.mapping==_NOT_FOUND)
e.trgComment='This object will be removed'
else if (e.mapping==_AMBIGUOUS)
e.trgComment='(ambiguous)'
else
e.trgComment='&nbsp;'
e.trgName=s
var txtl=getLayer(_codeWinName+'trg_'+e.id)
if (txtl)
{
txtl.innerHTML=e.trgName+e.trgComment
txtl.title=title
}
}
function DSMappingWidget_clickCB(id)
{
var e=_DSMappingInstances[id]
if (e==null)
return
var p=e.par
if (p.selId!=id)
{
var oldSel=_DSMappingInstances[p.selId]
p.unSelectDSMappingRow(oldSel)
p.selectDSMappingRow(e)
if (e.clickCB)
e.clickCB(id,e.usrData)
}
}
function DSMappingWidget_editBtnClickCB(id)
{
var e=_DSMappingInstances[id]
if (e==null)
return
if (e.editCB)
e.editCB(id,e.usrData)
}
function newDSMappingRow(id,par,mapping,srcQualif,srcName,trgQualif,trgName,clickCB,editCB,usrData)
{
var e=new Object
e.type=_DSMappingRowType
e.id=id
e.par=par
e.mapping=mapping
e.srcQualif=srcQualif
e.srcName=srcName
e.trgQualif=trgQualif
if (trgName==null)
trgName='&nbsp;'
e.trgName=trgName
var comment='&nbsp;'
if ( mapping==_NOT_FOUND )
comment='This object will be removed'
else if ( mapping ==_AMBIGUOUS)
comment='(ambiguous)'
e.trgComment=comment
e.clickCB=clickCB
e.editCB=editCB
e.usrData=usrData
return e
}
function newDPNameRow(name)
{
var e=new Object
e.type=_DSMappingDPNameType
e.name=name
return e
}
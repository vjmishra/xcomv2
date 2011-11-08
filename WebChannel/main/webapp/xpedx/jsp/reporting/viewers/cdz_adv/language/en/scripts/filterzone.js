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
*/_topfs=getTopFrameset()
_img = _topfs._img
_skin= _topfs._skin
_labFil=_topfs._labFil
_idxFilter=-1
_refreshOpList=false
function newFilterZoneWidget(id,title,w,h,isQuery,changeCB,helpText,hasEdit)
{
var o=newWidget(id)
o.title = title
o.root = null
o.selCopy = null
o.changeCB = changeCB 
o.isQuery = ((isQuery!=null) ? isQuery: true)
o.hasEditBtn=hasEdit?hasEdit:false;
o.filterZone    = newFrameZoneWidget(id+"_FilterZoneContainer",w,h)
o.flistCont= newAndOrContainerWidget(id+"_flistCont",500,280,_img+"qualificationIcons.gif",changeFilterCB,editFilterCB,andOrFilterCB,moveFilterCB,newFilterNodeCB,delCurrentFilterCB,true,helpText,removeAllFiltersCB)
o.flist= o.flistCont.getList()
o.flist.mycounter=0;
o.editBtn  = newButtonWidget(id+"_editFilterBtn","Edit Filter",editFilterCB,null,null,"Edit selected filter");
o.oldInit=o.init
o.init=FilterZoneWidget_init
o.getHTML=FilterZoneWidget_getHTML
o.findFilter=FilterZoneWidget_findFilter
o.addFilter=FilterZoneWidget_addFilter
o.resize=FilterZoneWidget_resize
o.buildFilters=FilterZoneWidget_buildFilters
o.getList=FilterZoneWidget_getList
o.updateBOListIcons=FilterZoneWidget_updateBOListIcons
o.getCurrentFilterWidget=FilterZoneWidget_getCurrentFilterWidget 
o.getCurrentFilter=FilterZoneWidget_getCurrentFilter 
o.removeCurrentFilter=FilterZoneWidget_removeCurrentFilter
o.moveFilter=FilterZoneWidget_moveFilter
o.switchFilter=FilterZoneWidget_switchFilter
o.duplicateFilter=FilterZoneWidget_duplicateFilter
o.flist.parentWidget=o
o.editBtn.parentWidget=o
return o;
}
function FilterZoneWidget_getHTML()
{
var o=this, s =''
s='<table id="'+o.id+'" class="dialogzone" border="0" cellspacing="0" cellpadding="0">'+
'<tr><td>'+
o.filterZone.beginHTML()+
'<table><tr>'+
'<td colspan="2" class="dialogzonebold">'+o.title+'</td>'+
'</tr><tr>'+
'<td colspan="2">'+o.flistCont.getHTML()+'</td>'+
'</tr><tr>'+
'<td align="right" style="padding-right:32px" colspan="2">'+o.editBtn.getHTML()+'</td>'+
'</tr></table>'+
o.filterZone.endHTML()+
'</td></tr>'+
'</table>'
return s
}
function FilterZoneWidget_init()
{
this.oldInit()
var o=this;
o.filterZone.init()
o.flistCont.init()
o.editBtn.init()
o.editBtn.setDisplay(o.hasEditBtn)
}
function FilterZoneWidget_getList(){ return this.flist }
function FilterZoneWidget_resize(w,h)
{
var o=this
var marge = 5
o.flistCont.resize(Math.max(0,w-marge*2),h?Math.max(35,h-(o.hasEditBtn?57:40)):null)
o.filterZone.resize(Math.max(0,w),h?Math.max(0,h):null)
}
function FilterZoneWidget_buildFilters(filterRoot)
{
var o= this
o.root = filterRoot
var widgetRoot=o.flist.getRoot()
if(filterRoot)
{
widgetRoot.setIsAnd(filterRoot.filterAnd)
buildFilters(widgetRoot,filterRoot)
}
}
function FilterZoneWidget_findFilter(idx)
{
var o= this, root = o.root
if (root== null)return null;
if (idx=="" )return root;
return root.find(idx)
}
function FilterZoneWidget_addFilter(newFilter, node, idx, noselect)
{
var o=this, newItem = null, parFilter = o.findFilter(node.value);
var iconIdx=-1, newValidId='advFlt'+(o.flist.mycounter++);
idx=(idx==null?-1:idx)
parFilter.add(newFilter,idx)
switch (newFilter.kind)
{
case _topfs._filterPredefObj:
iconIdx=newFilter.obj.kind;
newItem = node.addItem(newFilter.getLabel(),iconIdx,newFilter.idx,newFilter.getLabel(),idx)
break;
case _topfs._filterAdvanced:
iconIdx=10;
newItem = node.addItem(newFilter.getLabel(),iconIdx,newFilter.idx,newFilter.getLabel(),idx)
break;
case _topfs._filterTopBottom:
iconIdx=11;
newItem = node.addItem(newFilter.getLabel(),iconIdx,newFilter.idx,newFilter.getLabel(),idx)
break;
default:
iconIdx =newFilter.obj.kind;
if(o.isQuery) 
newItem = node.addCustomFilterItem(newAndOrFilter(newValidId,newFilter.obj.name,iconIdx,newFilter.idx,newFilter.obj.name,newFilter),idx);
else 
newItem = node.addItem(newFilter.getLabel(),iconIdx,newFilter.idx,newFilter.getLabel(),idx)
break;
}
if(o.changeCB) o.changeCB('add',newFilter);
if(!noselect)
{
o.flist.select(newItem);
o.updateBOListIcons()
}
return newItem;
}
function FilterZoneWidget_moveFilter(sourceItem, targetItem, index)
{
var target = this.flist
if (target.moveCB)target.moveCB(sourceItem, targetItem, index)
}
function FilterZoneWidget_switchFilter(sourceItem, targetItemParent, index)
{
var o=this, target = this.flist
var targetItem= targetItemParent.getItem(index)
var newItem = o.duplicateFilter(sourceItem, targetItemParent, index)
var sourceItemParent = sourceItem.getParentNode()
var sourceItemIdx = sourceItem.getIndexInParent()
o.duplicateFilter(targetItem, sourceItemParent, sourceItemIdx)
var sourceFilter = o.findFilter(sourceItem.value)
var targetFilter = o.findFilter(targetItem.value)
sourceItem.remove()
sourceFilter.remove()
targetItem.remove()
targetFilter.remove()
target.select(newItem)
o.updateBOListIcons()
}
function FilterZoneWidget_duplicateFilter(sourceItem, targetItem, index)
{
var o=this, target = o.flist
var sourceFilter = o.findFilter(sourceItem.value)
var targetFilter = o.findFilter(targetItem.value)
var newFilter = null, newItem = null
if(sourceFilter.isNode())
{
newFilter = targetFilter.addCopyNode(sourceFilter,index)
newItem =targetItem.addNode(newFilter.filterAnd,newFilter.idx,index)
buildFilters(newItem,newFilter,index)
}
else
{
newFilter = sourceFilter.getCopy()
newItem = o.addFilter(newFilter, targetItem, index)
}
target.select(newItem);
return newItem;
}
function FilterZoneWidget_updateBOListIcons()
{
var o=this,flistCont=o.flistCont,flist = this.flist
var sel=flist.getSelection()
if(sel)
{
if(flistCont.canBeMoved(sel,true))
flistCont.up.setDisabled(false)
else
flistCont.up.setDisabled(true)
if(flistCont.canBeMoved(sel,false))
flistCont.down.setDisabled(false)
else
flistCont.down.setDisabled(true)
flistCont.newNode.setDisabled(false)
if(flistCont.removeAll)
    flistCont.removeAll.setDisabled(flist.root.items.length == 0)
if(sel.isNode())
o.editBtn.setDisabled(true)
else
o.editBtn.setDisabled(false)
}
else
{
flistCont.up.setDisabled(true)
flistCont.down.setDisabled(true)
flistCont.newNode.setDisabled(true)
if(flistCont.removeAll)
    flistCont.removeAll.setDisabled(true)
o.editBtn.setDisabled(true)
}
}
function FilterZoneWidget_getCurrentFilterWidget(id)
{
var sel=this.flist.getSelection();
if((sel.id == id) || (id == null)) 
return sel;
else 
return null;
}
function FilterZoneWidget_getCurrentFilter()
{
var sel=this.flist.getSelection();
if(sel == null) 
return null;
return this.findFilter(sel.value);
}
function FilterZoneWidget_removeCurrentFilter()
{
delCurrentFilter(this);
}
function changeFilterCB()
{
var o = this.parentWidget
o.updateBOListIcons()
}
function dbClickFilterCB()
{
var o=this.parentWidget;
if(!o.isQuery)
{
_topfs.wt();
frameNav("SecondDlgFrame",_topfs._root+"language/"+_topfs._lang+"/html/partialFilterDialog.html?"+o.id,null,_topfs);
}
}
function editFilterCB()
{
var o=this.parentWidget;
if(!o.isQuery)
{
_topfs.wt();
frameNav("SecondDlgFrame",_topfs._root+"language/"+_topfs._lang+"/html/partialFilterDialog.html?"+o.id,null,_topfs);
}
}
function andOrFilterCB()
{
var o = this.parentWidget, sel=o.flist.getSelection() 
var selFilter = o.findFilter(sel.value)
if(selFilter.isNode())
selFilter.filterAnd=sel.isAnd
if(o.changeCB) o.changeCB()
}
function moveFilterCB(elem,node,idx)
{
var o= this.parentWidget
var newItem = o.duplicateFilter(elem, node, idx)
var sourceFilter = o.findFilter(elem.value)
elem.remove()
sourceFilter.remove()
o.flist.select(newItem)
o.updateBOListIcons()
return false;
}
function newFilterNodeCB()
{
var o= this.parentWidget,sel, newParent,index,newIndex, newFilterParent, newfilter
sel=o.flist.getSelection()
newParent=sel.par
newIndex=sel.getIndexInParent()
newFilterParent=o.findFilter(newParent.value)
newfilter = newFilterParent.addNode(sel.isAnd,newIndex) 
sel.value = newfilter.idx
o.updateBOListIcons()
if(o.changeCB) o.changeCB()
}
function updateFromButton()
{
var o= this.parentWidget
o.modifyFilter();
}
function delCurrentFilterCB()
{
var o = this.parentWidget
delCurrentFilter(o)
}
function removeAllFiltersCB()
{
    _topfs._filterZoneWidget=this.lst
    _topfs.showPromptDialog("Are you sure you want to delete all objects from the filter panel ?",null,_topfs._promptDlgWarning,doRemoveAllFiltersCB,null)   
}
function doRemoveAllFiltersCB()
{
    if(_topfs._filterZoneWidget)
    {
        var list = _topfs._filterZoneWidget.andOrList 
        var o = list.parentWidget   
        var sel=list.root, selFilter=o.findFilter(sel.value)
    if(sel)
    {
    sel.remove()
    selFilter.remove()
    o.updateBOListIcons()
    if(o.changeCB) o.changeCB('delete',selFilter)
    }     
}
}
function buildFilters(AndOrNode,fnode,index)
{
var len=fnode.items.length;
for (var k=0;k<len;k++)
{
var fitem=fnode.items[k]
if(fitem.isNode())
{
subNode=AndOrNode.addNode(fitem.filterAnd,fitem.idx,index?index:-1)
buildFilters(subNode,fitem)
}
else
{
var iconIdx=-1;
switch (fitem.kind)
{
case _topfs._filterAdvanced:
iconIdx=10;
break;
case _topfs._filterTopBottom:
iconIdx=11;
break;
default:
if(fitem.obj ) iconIdx =fitem.obj.kind;
else iconIdx =7; 
break;
}
if(fitem.kind == _topfs._filterEditable)
{
var newValidId='advFlt'+(AndOrNode.list.mycounter++);
AndOrNode.addCustomFilterItem(newAndOrFilter(newValidId,fitem.obj.name,iconIdx,fitem.idx,fitem.obj.name,fitem),index?index:-1);
}
else
AndOrNode.addItem(fitem.getLabel(),iconIdx,fitem.idx,fitem.getLabel())
}
}
}
function setDefaultAdvFilter(f,isQuery,isQuickFlt)
{
var fPart0=f.getOperandPart(0),fPart1=f.getOperandPart(1);
var useListOfValues=true;
var type=_topfs._filterConst;
var msg0="",msg1="";
if(isQuery)
{
switch(_topfs._defaultQueryFilter)
{
case "constant":
type=_topfs._filterConst
break
case "promptNoLOV":
type=_topfs._filterPrompt
useListOfValues=false
break
default:
type=_topfs._filterPrompt
useListOfValues=true
break
}
if(isQuickFlt)
type=_topfs._filterLov 
if(!f.obj.hasLov)
useListOfValues=false
if(type==_topfs._filterPrompt)
{
fPart0.operand=getPromptMessageValue(f,0);
}
if(_topfs.usrPromptKeepValues.toLowerCase()=="no") 
{
fPart0.keepLastValues=false;
fPart1.keepLastValues=false;
}
fPart0.useListOfValues=useListOfValues;
fPart0.type=type;
fPart1.useListOfValues=useListOfValues;
fPart1.type=type;
}
}
function addCurrentFilter(fZoneWidget,obj,node, idx, isQuickFlt)
{
var o = fZoneWidget, sel=null , newFilter =null, newItem=null;
if(node)
sel = node
else
{
sel=o.flist.getSelection()
if (sel.isNode())
idx = -1
else
idx = sel.getIndexInParent()+1 
}
if ((obj==null) || (obj.kind== _topfs._unv)|| (obj.kind== _topfs._cls) || (obj.kind== _topfs._hchy)|| (!obj.allowInFilter)) 
return;
if (obj.kind==_topfs._fil)
{
newFilter=_topfs.newBOFilter(_topfs._filterPredefObj,obj)
}
else if(!o.isQuery) 
{
_idxFilter=idx;
newFilter=_topfs.newBOFilter(_topfs._filterConst,obj);
newFilter.isIndexAware=false;
newFilter.operator=_topfs.IN_LIST;
newFilter.kind=_filterConst;
newFilter.operands[0]=newFilter.operands[1]="";
newFilter.isQuickFilter=true;
}
else  
{
_idxFilter=idx
newFilter=_topfs.newAdvBOFilter(_topfs._filterEditable,obj)
newFilter.isIndexAware=o.isQuery;
newFilter.isQuery=o.isQuery;
setDefaultAdvFilter(newFilter,o.isQuery,isQuickFlt)
newFilter.isQuickFilter=isQuickFlt?isQuickFlt:false;
}
if(!sel.isNode()) 
sel=sel.getParentNode()
newItem=o.addFilter(newFilter, sel, idx)
if(newItem && newFilter.isQuickFilter) 
{
_topfs.wt();
if(!o.isQuery)
frameNav("SecondDlgFrame",_topfs._root+"language/"+_topfs._lang+"/html/partialFilterDialog.html?"+o.id,null,_topfs);
else
frameNav("SecondDlgFrame",_topfs._root+"language/"+_topfs._lang+"/html/filterLOVDialog.html?ID="+newItem.id+"&index=0",null,_topfs)
}
}
function addCurrentFilters(fZoneWidget,arrObj,node, idx)
{
var o = fZoneWidget, sel=null, item=null
if(node)
sel = node
else
{
sel=o.flist.getSelection()
if (sel.isNode())
idx = -1
else
idx = sel.getIndexInParent()+1 
}
var lenObjs = arrObj.length, obj=null;
if(lenObjs == 0) return;
for(var i=0;i<lenObjs;i++)
{
obj=arrObj[i];
if ((obj==null) || (obj.kind== _topfs._unv)|| (obj.kind== _topfs._cls) || (obj.kind== _topfs._hchy) || (!obj.allowInFilter)) continue;
var newFilter =null
if (obj.kind==_topfs._fil)
{
newFilter=_topfs.newBOFilter(_topfs._filterPredefObj,obj);
}
else if(!o.isQuery) 
{
_idxFilter=idx;
newFilter=_topfs.newBOFilter(_topfs._filterConst,obj);
newFilter.isIndexAware=false;
newFilter.operator=_topfs.IN_LIST;
newFilter.kind=_filterConst;
newFilter.operands[0]=newFilter.operands[1]="";
}
else 
{
_idxFilter=idx;
newFilter=_topfs.newAdvBOFilter(_topfs._filterEditable,obj);
newFilter.isIndexAware=o.isQuery;
newFilter.isQuery=o.isQuery;
setDefaultAdvFilter(newFilter,o.isQuery);
}
if(!sel.isNode()) sel=sel.getParentNode()
var noSelectItem=(i == lenObjs-1)?false:true;
o.addFilter(newFilter, sel, idx, noSelectItem)
if(idx>-1) idx++;
}
}
function delCurrentFilter( fZoneWidget)
{
var o= fZoneWidget
var sel=o.flist.getSelection(), selFilter=o.findFilter(sel.value)
if(sel)
{
sel.remove()
selFilter.remove()
o.updateBOListIcons()
if(o.changeCB) o.changeCB('delete',selFilter)
}
}
function isCustom(f)
{
return ((f!=null)&&(f.kind!=_topfs._filterPredefObj)&&(f.kind!=_topfs._filterAdvanced)&&(f.kind!=_topfs._filterTopBottom) && (!f.isNode()))
}
function isPromptType(o)
{
return (o.type==_topfs._filterPrompt);
}
function isCustomPromptMessage(f,pos)
{
    var fPart=f.operands[pos]
if (isPromptType(fPart))
{
return !(getPromptMessageValue(f,pos)==fPart.operand);
}
return false
}
function getPromptMessageValue(f,pos)
{   
if (isCustom(f))
{
with (f)
{
var nbVis=(f==null)?-1:getOperandCount();
var isInList=(f==null)?false:isListOperator();
if (pos==0)
{
    if(isInList)
    {    
        return "Enter value(s) for " + convStr(obj.name)+":";
    }
    else
    { 
        if(nbVis==1)
            return "Enter " + convStr(obj.name)+":";
        if(nbVis==2)
            return "Enter " + convStr(obj.name)+" (Start):";
    }
}
if (pos==1 && nbVis==2)
{
return "Enter " + convStr(obj.name)+" (End):";
}
}
}
return "";
}
function getIconIdx(idx)
{
var a=new Array;
a[0]=0
a[1]=0
a[2]=0
a[3]=1
a[4]=2
a[5]=3
a[6]=3
a[7]=4
return a[idx];
}
function debugTree(root)
{
s="Root "+ (root.filterAnd?"AND":"OR")  +"\n"
var inden=""
buildFiltersDebug(root,inden)
alert(s)
}
function buildFiltersDebug(fnode,inden)
{
var len=fnode.items.length;
for (var k=0;k<len;k++)
{
var fitem=fnode.items[k]
if(fitem.isNode())
{
s+=inden+"node "+ (fitem.filterAnd?"AND":"OR") +  " (idx="+fitem.idx +")\n"
buildFiltersDebug(fitem,inden+"--")
}
else
{
s+=inden+fitem.getLabel()+ " (idx="+ fitem.idx +")\n"
}
}
}
function getOnlyValues(s,doNothing)
{
if(doNothing) return s;
var arrIdxValues = s.split(_topfs._listSeparator), len = arrIdxValues.length;
var arrValues = new Array;
for(var i=0;i<len;i++)
{
arrValues[i]=getOnlyValue(arrIdxValues[i]);
}
return arrValues.join(_topfs._listSeparator);
}
function getOnlyValue(s,doNothing)
{
var ret=s;
if(!doNothing)
{
var pos = s.indexOf('_');
if(pos>-1)
ret=s.substring(pos+1);
}
return ret
}
function setFreeValues(s,doNothing)
{
s=processFreeValues(s);
if(doNothing || s=="") return s;
var arrValues = s.split(_topfs._listSeparator), len = arrValues.length;
var arrIdxValues= new Array;
for(var i=0;i<len;i++)
{
arrIdxValues[i]="_"+arrValues[i];
}
return arrIdxValues.join(_topfs._listSeparator);
}
function newAndOrFilter(id,text,imgIndex,value,tooltip,filter)
{
var o=newWidget(id)
o.text=text
o.filter=filter;
o.isQuery = (filter.isQuery != null)?filter.isQuery:true;
o.value=value
o.tooltip=tooltip?tooltip:''
o.imgIndex=imgIndex?imgIndex:0
o.par=null
o.txtLayer=null
o.operatorCombo=newCustomCombo('AndOrFilterOperator_'+id,operator_changeCB,true,null);
o.operatorCombo.parentWidget=o
AndOrFilter_initOperatorList(o.operatorCombo,filter);
o.defZone0=newAdvFilterDef('AndOrFilterDef0_'+id);
o.defZone0.parentWidget=o
o.defZone0.pos=0;
o.defZone1=newAdvFilterDef('AndOrFilterDef1_'+id);
o.defZone1.parentWidget=o
o.defZone1.pos=1;
o.getHTML=AndOrFilter_getHTML
o.getExtraHTML=AndOrFilter_getExtraHTML
o.getDynamicHTML=AndOrFilter_getDynamicHTML
o.oldAOItemInit=o.init
o.init=AndOrFilter_init
o.select=AndOrFilter_select
o.isNode=AndOrFilter_isNode
o.getParentNode=AndOrFilter_getParentNode
o.getIndexInParent=AndOrFilter_getIndexInParent
o.remove=AndOrFilter_remove
o.setText=AndOrFilter_setText
o.setInsertFeedback=AndOrFilter_setInsertFeedback
o.isChildOf=AndOrFilter_isChildOf
o.getAdvFilterDef=AndOrFilter_getAdvFilterDef
o.getCopy=AndOrFilter_getCopy
o.updateUI=AndOrFilter_updateUI
o.modifyFilter=AndOrFilter_modifyFilter
o.getFilter=AndOrFilter_getFilter
o.setLovSelection=AndOrFilter_setLovSelection
o.setCompareObj=AndOrFilter_setCompareObj
o.setPromptInfo=AndOrFilter_setPromptInfo
o.cancelAction=AndOrFilter_cancelAction
o.fullW=false
return o
}
function AndOrFilter_init()
{
var o=this
o.oldAOItemInit()
o.operatorCombo.init();
o.defZone0.init();
o.defZone1.init();
o.layer.onmousedown=AndOrFilter_clickCB
o.layer.onmouseup=AndOrFilter_mouseupCB
if (_ie)
o.layer.onmousemove=AndOrNodeWidget_triggerDD
o.layer.onkeydown=AndOrFilter_keyDownCB
addDblClickCB(o.layer,AndOrFilter_dblClickCB)
o.layer.title=o.tooltip
var dd=o.list.dragDrop
if (_ie&&dd)
{
dd.attachCallbacks(o.layer)
o.layer.BODDType="item"
}
var tdOpCombo=o.operatorCombo.layer
tdOpCombo.onmousedown=AdvFilterDef_cancelBubbleAndSelect
o.updateUI()
}
function AndOrFilter_getExtraHTML()
{
var o=this,s="";
s+='<table class="treeNormal" border="0" cellspacing="0" cellpadding="0"><tr valign="middle"><td>';
s+='<nobr>'+o.text+'</nobr></td><td>';
s+=o.operatorCombo.getHTML();
s+='</td><td>';
s+=o.defZone0.getHTML();
s+='</td><td>';
s+=o.defZone1.getHTML();
s+='</td></tr></table>';
return s;
}
function AndOrFilter_getHTML()
{
var o=this, list=o.list;
var topBorder=((list.layout==_vertBOList)||(list.layout==_andOrBOList))
var bottomBorder=(o.par.getLastItem().id==o.id)?topBorder:null
var text=o.text
if(list.layout!=_andOrBOList && !list.autofit)
{
if(text.length>10)text=text.slice(0,10)+"..."
}
return elasticZone(text,_skin+'bolist.gif',3,28,o.selected?84:0,o.id,'treeNormal',o.list.image,0,o.imgIndex*16,null,'AndOrFilterTxt_'+o.id,o.fullW,topBorder,bottomBorder,null,o.getExtraHTML())
}
function AndOrFilter_getDynamicHTML()
{
var o=this
var topBorder=((this.list.layout==_vertBOList)||(this.list.layout==_andOrBOList))
var bottomBorder=(o.par.getLastItem().id==o.id)?topBorder:null
var text=o.text
if(this.list.layout!=_andOrBOList)
{
if(text.length>10)text=text.slice(0,10)+"..."
}
return dynamicElasticZone(text,_skin+'bolist.gif',3,28,o.selected?84:0,o.id,'treeNormal',o.list.image,0,o.imgIndex*16,null,'AndOrFilterTxt_'+o.id,o.fullW,topBorder,bottomBorder,null,o.getExtraHTML())
}
function AndOrFilter_setInsertFeedback(show,isAll)
{
var o=this,isLeft=(_dropPosition!=-1),st=o.css,b=show?"#C00000":"#FFFFFF"
var node=o.isNode()
if (isAll)
{
st.borderTopColor=st.borderLeftColor=st.borderBottomColor=st.borderRightColor="#FFFFFF"
if (node)
{
var dy=show?15:(6+(o.selected?3:0))
elasticZoneSetImg(o.operLayer,28,dy,dy+1,dy+2)
}
else
{
var dy=show?12:(o.selected?3:0)
elasticZoneSetImg(o.layer,28,dy,dy+1,dy+1,dy+2)
}
}
else 
{
var isVert=((o.list.layout==_vertBOList)||(o.list.layout==_andOrBOList))
if (_dropParentWidget&&(_dropFeebackWidget.id==_dropParentWidget.id))
{
var feedLayer=o.layer.childNodes[0].childNodes[0].childNodes[1],feedCSS=feedLayer.style
feedCSS.borderLeft=show?_lstBord:_lstNullbord
feedLayer.innerHTML=show?'&nbsp;':''
}
else
{
if (isLeft)
{
if (isVert)
st.borderTopColor=b
else
st.borderLeftColor=b
}
else
{
if (isVert)
st.borderBottomColor=b
else
st.borderRightColor=b
}
if (node)
{
var dy=6+(o.selected?3:0)
elasticZoneSetImg(o.operLayer,28,dy,dy+1,dy+2)
}
else
{
var dy=(o.selected?3:0)
elasticZoneSetImg(o.layer,28,dy,dy+1,dy+1,dy+2)
}
}
}
}
function AndOrFilter_getParentNode()
{
return this.par
}
function AndOrFilter_isNode()
{
return false
}
function AndOrFilter_clickCB(e)
{
var o=getWidget(this),list=o.list
o.select(true)
o.clicked=true
o.initialX=eventGetX(e)
o.initialY=eventGetY(e)
if (list.changeCB)
list.changeCB()
return false
}
function AndOrFilter_mouseupCB()
{
var o=getWidget(this)
o.clicked=false
}
function AndOrFilter_keyDownCB(e)
{
var o=getWidget(this),list=o.list
var key=eventGetKey(e);
switch(key)
{
case 46:
if (list.deleteCB)
list.deleteCB()
break;
case 13:
eventCancelBubble(e);
o.select(true)
if (list.changeCB)
list.changeCB()
break;
case 37:
case 38:
case 39:
case 40:
if(list.selection == o)
{
var pos=null,updown=null,l=list.layout;
if(l==_andOrBOList&& (key==37 || key==38))
{
pos=-1
updown=(key==37)?false:true;
}
if(l==_andOrBOList && (key==39 || key==40))
{
pos=1
updown=(key==39)?false:true;
}
if(pos!=null)
{
elem = list.getNextPrev(pos,updown);
if(elem!=null)
{
elem.select(true);
if (list.changeCB)
list.changeCB()
}
}
}
break;
}
}
function AndOrFilter_dblClickCB()
{
var o=getWidget(this),list=o.list
if (list.dblClickCB)
list.dblClickCB()
return false
}
function AndOrFilter_select(sel,noForceFocus)
{
noForceFocus=(noForceFocus!=null)?noForceFocus:false
var o=this,list=o.list
if (sel)
list.unselect()
list.selection=sel?o:null
o.selected=sel
if (o.layer)
{
var dy=sel?3:0
elasticZoneSetImg(o.layer,28,dy,dy+1,dy+1,dy+2)
if(sel)
o.layer.title=_lstSelectedLabel+" "+o.tooltip
else
o.layer.title=o.tooltip
if(!noForceFocus&&sel&&o.layer.focus&&(!isHidden(o.layer)))
o.layer.focus()
}
}
function AndOrFilter_remove()
{
var o=this
if (o.par)
o.par.removeChild(o)
}
function AndOrFilter_getIndexInParent()
{
var o=this,par=o.par
return par?par.getChildIndex(o):-1
}
function AndOrFilter_setText(text)
{
var o=this
if (o.txtLayer==null)
o.txtLayer=getLayer("AndOrFilterTxt_"+o.id)
o.text=text
o.txtLayer.innerHTML=convStr(o.text,true)
o.tooltip=text
o.layer.title=o.tooltip
}
function AndOrFilter_isChildOf(node)
{
var p=this.par
if (p==null)
return false
if (p.id==node.id)
return true
else
return p.isChildOf(node)
}
function AndOrFilter_getCopy()
{
var o=this, list=o.list;
var newId='advFlt'+(list.mycounter++);
return newAndOrFilter(newId,o.text,o.imgIndex,o.value,o.tooltip,o.filter);
}
function AndOrFilter_getFilter()
{
return this.filter;
}
function AndOrFilter_getAdvFilterDef(pos)
{
if(pos == 0) return this.defZone0;
else if(pos == 1) return this.defZone1;
else return null;
}
function AndOrFilter_setLovSelection(v,pos)
{
var o=this;
o.modifyFilter(2,v,pos);
o.updateUI(pos);
}
function AndOrFilter_setCompareObj(obj,pos)
{
var o=this;
o.modifyFilter(2,obj,pos);
o.updateUI(pos);
}
function AndOrFilter_setPromptInfo(v,pos)
{
var o=this;
o.modifyFilter(2,v,pos);
o.updateUI(pos);
}
function AndOrFilter_cancelAction(action,pos)
{
var o=this,filter=o.filter, advdef=o.getAdvFilterDef(pos),fpart=filter.getOperandPart(pos);
if(action == "removefilter")
{
var fltzone = o.list.parentWidget;
fltzone.removeCurrentFilter();
}
else if(action == "resetmenu")
{
var menu=advdef.typeMenu, items=menu.items
for (var i in items)
{
var item=items[i];
item.check(false);
}
var previousMenu= getTypeMenuEntry(advdef.previousType);
if(previousMenu)
menu.getItemByID(previousMenu).check(true);
o.modifyFilter(1,advdef.previousType,o.pos);
}
}
function AndOrFilter_updateUI(pos)
{
var o=this, advdef0=o.defZone0,advdef1=o.defZone1;
var filter=o.filter, fPart0=filter.getOperandPart(0),fPart1=filter.getOperandPart(1);
if(pos == null)
{
var nbOpe = filter.getOperandCount();
advdef0.setDisplay(nbOpe>=1);
advdef1.setDisplay(nbOpe==2);
o.operatorCombo.valueSelect(filter.operator);
if(nbOpe>=1)
{
advdef0.updateUI(fPart0);
if(nbOpe==2)
{
advdef1.updateUI(fPart1);
}
}
}
else
{
if(pos == 0)
advdef0.updateUI(fPart0);
else if(pos == 1)
advdef1.updateUI(fPart1);
else
return;
}
}
function AndOrFilter_modifyFilter(action,value,pos)
{
var filter = this.filter;
if((pos==null) || (pos>1) || (pos<0)) pos=0;
if(action==0)
{
    var isCustomMessage1 = isCustomPromptMessage(filter,0)
var isCustomMessage2 = isCustomPromptMessage(filter,1)
filter.setOperator(value);
var fPart0= filter.operands[0], fPart1= filter.operands[1];
if(isPromptType(fPart0) && !isCustomMessage1)
fPart0.operand=getPromptMessageValue(filter,0);
if(isPromptType(fPart1) && !isCustomMessage2)
fPart1.operand=getPromptMessageValue(filter,1);
}
else if(action==1) 
{
var fPart= filter.operands[pos];
fPart.type=value;
if(isPromptType(fPart))
fPart.operand=getPromptMessageValue(filter,pos);
}
else if(action==2) 
{
filter.operands[pos].operand=value
}
var fzone=this.list.parentWidget;
if(fzone && fzone.changeCB) fzone.changeCB('modify',filter);
}
function AndOrFilter_getFilter()   
{
return this.filter;
}
function AndOrFilter_initOperatorList(fop,filter)
{
var o=this,isQuery=filter.isQuery;
fop.icon.setClasses("iconnochecknobg", "iconchecknobg", "iconhovernobg", "iconcheckhovernobg")
fop.arrow.setClasses("iconnochecknobg", "iconchecknobg", "iconhovernobg", "iconcheckhovernobg")
fop.arrow.overCB="IconWidget_overCB"
fop.arrow.outCB="IconWidget_outCB"
fop.icon.overCB="IconWidget_overCB"
fop.icon.outCB="IconWidget_outCB"
fop.icon.width=null;
var obj=filter?filter.obj:null;
if (obj!=null)
{
var isString=obj.dataType==_topfs._txt;
for (var i=_topfs._firstFilter;i<=_topfs._lastFilter;i++)
{
var bAdd = true;
switch (i)
{
case _topfs.LIKE:
case _topfs.NOT_LIKE:
if((!isQuery) || (isQuery && !isString) )
bAdd = false;
break;
case _topfs.BOTH:
if((!isQuery) || (isQuery && _currentDP && !_currentDP.allowBoth) )
bAdd = false;
break;
case _topfs.EXCEPT:
if((!isQuery) || (isQuery && _currentDP && !_currentDP.allowExcept) )
bAdd = false;
break;
case _topfs.IS_NULL:
if(isQuery && _currentDP && !_currentDP.allowIsNull)
bAdd = false;
break;
case _topfs.NOT_IS_NULL:
if(isQuery && _currentDP &&!_currentDP.allowNotIsNull) 
bAdd = false;
break;
default:
break;
}
if (bAdd)
fop.add(_labFil[i],i,(i==filter.operator));
}
}
}
function operator_changeCB()
{
var o=this, sel = o.getSelection()
var advflt= o.parentWidget;
advflt.modifyFilter(0,sel.value);
advflt.updateUI();
}
function newAdvFilterDef(id)
{
var o=newWidget(id);
o.type=_filterConst;
o.input="";
o.objLabel= newWidget(id+"_objlabel");
o.inputTxt= newTextFieldWidget(id+"_input",textModifiedCB,null,null,textModifiedCB);
o.calendar=newCalendarButton(id+"_calendar",setStringFromDateCB,setDateFromStringCB);
o.propertyIcn=newIconWidget(id+"_property",_img+"promptProp.gif",clickPromptPropCB,null,"Show prompt properties",16,16,0,0);
o.typeIcn=newIconWidget(id+"_type",_img+"filterType.gif",clickFilterTypeCB,null,"Define filter type",16,16,0,0);
o.propertyIcn.setClasses("iconnochecknobg", "iconchecknobg", "iconhovernobg", "iconcheckhovernobg");
o.typeIcn.setClasses("iconnochecknobg", "iconchecknobg", "iconhovernobg", "iconcheckhovernobg");
o.typeMenu=newMenuWidget(id+"_type_menu",null,beforeShowTypeMenu);
o.inputTxt.parentWidget=o;
o.calendar.parentWidget= o;
o.propertyIcn.parentWidget=o;
o.typeIcn.parentWidget=o;
o.typeMenu.parentWidget=o;
o.oldInit=o.init
o.init=AdvFilterDef_init
o.getHTML=AdvFilterDef_getHTML
o.updateUI=AdvFilterDef_updateUI
o.pos=0; 
return o;
}
function AdvFilterDef_getHTML()
{
var o=this;
return(
'<table id="'+o.id+'" border="0" cellspacing="0" cellpadding="0"><tbody>'+
'<tr>'+
((o.pos>0)?('<td class="treeNormal" >  '+_topfs._labOperand[AND]+'</td>'):'')+
'<td id="'+o.id+'_objlabel">'+
'</td><td><div>'+
o.inputTxt.getHTML()+
'</div></td><td>'+
o.calendar.getHTML()+
'</td><td>'+
o.propertyIcn.getHTML()+
'</td><td>'+
o.typeIcn.getHTML()+
'</td>'+
'</tr>'+
'</tbody></table>');
}
function AdvFilterDef_cancelBubbleAndSelect(e)
{
eventCancelBubble(e)
var o=getWidget(this)
o.parentWidget.select(true,true)
_topfs.simulateClick(e)
return true
}
function AdvFilterDef_cancelBubble(e)
{
eventCancelBubble(e)
}
function AdvFilterDef_init()
{
var o=this;
var filter=o.parentWidget.filter;
o.oldInit();
o.objLabel.init();
o.inputTxt.init();
o.calendar.init();
o.propertyIcn.init();
o.typeIcn.init();
initTypeList(o);
var txtDiv=o.inputTxt.layer.parentNode
txtDiv.onmousedown=AdvFilterDef_cancelBubbleAndSelect
txtDiv.onmouseup=AdvFilterDef_cancelBubble
txtDiv.onmousemove=AdvFilterDef_cancelBubble
txtDiv.onselectstart=AdvFilterDef_cancelBubble
txtDiv.onkeydown=AdvFilterDef_cancelBubble
txtDiv.onkeyup=AdvFilterDef_cancelBubble
var tdTypeIcn=o.typeIcn.layer.parentNode
tdTypeIcn.onmousedown=AdvFilterDef_cancelBubbleAndSelect
}
function initTypeList(o)
{
var m = o.typeMenu;
var filter = o.parentWidget.filter;
var fPart=getBOFilterPart(o);
var isQuery=filter.isQuery;
var item=null;
m.addCheck("advf_constant","Constant",changeFTypeCB);
item = m.addCheck("advf_fromList","Value(s) from list",changeFTypeCB);
item.setDisabled(!filter.obj.hasLov);
if(isQuery)
{
m.addCheck("advf_prompt","Prompt",changeFTypeCB);
item = m.addCheck("advf_object","Object",changeFTypeCB);
item.setDisabled(filter.isListOperator());
}
var selMenu = getTypeMenuEntry(fPart.type);
o.typeMenu.getItemByID(selMenu).check(true);
}
function beforeShowTypeMenu()
{
var o=this.parentWidget; 
var filter = getAdvBOFilter(o);
var item = this.getItemByID("advf_object");
item.setDisabled(filter.isListOperator());
o.previousType=o.type;
}
function changeFTypeCB()
{
var id = this.id;
var menu=this.par,items=menu.items
var o=advdef=menu.parentWidget;
var advflt=o.parentWidget;
var fPart=advflt.filter.operands[o.pos];
for (var i in items)
{
var item=items[i]
item.check(item.id==id)
}
switch(id)
{
case "advf_constant" :
if(fPart.type == _filterPrompt || fPart.type == _filterObj )
advflt.modifyFilter(2,"",o.pos);
advflt.modifyFilter(1,_filterConst,o.pos);
o.updateUI(fPart);
break;
case "advf_fromList" :
if(fPart.type == _filterPrompt || fPart.type == _filterObj )
advflt.modifyFilter(2,"",o.pos);
advflt.modifyFilter(1,_filterLov,o.pos);
openLOVDlgCB(o);
break;
case "advf_prompt" :
advflt.modifyFilter(1,_filterPrompt,o.pos);
o.updateUI(fPart);
break;
case "advf_object" :
advflt.modifyFilter(1,_filterObj,o.pos);
frameNav("SecondDlgFrame",_topfs._root+"language/"+_topfs._lang+"/html/compareObjectDialog.html?ID="+advflt.id+"&index="+o.pos,null,_topfs);
break;
}
}
function AdvFilterDef_updateUI(fPart)
{
var o=this;
var isDate=(fPart.obj.dataType==_topfs._date);
o.type=fPart.type;
o.input=fPart.operand; 
switch(o.type)
{
case _filterConst :
o.objLabel.setDisplay(false);
o.inputTxt.setDisplay(true);
o.inputTxt.setValue(getOnlyValues(o.input));
o.inputTxt.setDisabled(false);
o.calendar.setDisplay(isDate);
if(o.input=="")
{
if(isDate)
{ 
var today=formatDate(new Date(),_topfs._inputFormatDate);
o.input=setFreeValues(today)
o.inputTxt.setValue(today)
o.parentWidget.modifyFilter(2,o.input,o.pos);
}
else
o.inputTxt.setHelpTxt("Type a constant");
}
o.propertyIcn.setDisplay(false);
o.typeMenu.getItemByID("advf_constant").check(true);
break;
case _filterLov :
o.objLabel.setDisplay(false);
o.inputTxt.setDisplay(true);
o.inputTxt.setValue(getOnlyValues(o.input));
o.inputTxt.setDisabled(true);
o.calendar.setDisplay(false);
o.propertyIcn.setDisplay(false);
o.typeMenu.getItemByID("advf_fromList").check(true);
break;
case _filterPrompt :
o.objLabel.setDisplay(false);
o.inputTxt.setDisplay(true);
o.inputTxt.setValue(o.input);
o.inputTxt.setDisabled(false);
o.calendar.setDisplay(false);
o.propertyIcn.setDisplay(true);
o.typeMenu.getItemByID("advf_prompt").check(true);
break;
case _filterObj :
var obj=fPart.operand;
var iconIdx =obj.kind;
var s='<table class="treeNormal" border="0" cellspacing="0" cellpadding="0"><tr><td>'+imgOffset(_img+"qualificationIcons.gif",16,16,0,16*iconIdx)+'</td><td><nobr>'+convStr(obj.name)+'</nobr></td></tr></table>'
o.objLabel.layer.innerHTML=s;
o.objLabel.setDisplay(true);
o.inputTxt.setDisplay(false);
o.calendar.setDisplay(false);
o.propertyIcn.setDisplay(false);
o.typeMenu.getItemByID("advf_object").check(true);
break;
}
}
function clickPromptPropCB()
{
var o=advflt=this.parentWidget.parentWidget; 
var pos = this.parentWidget.pos
if(o)
{
_topfs.wt();
frameNav("SecondDlgFrame",_topfs._root+"language/"+_topfs._lang+"/html/promptPropertiesDialog.html?ID="+o.id+"&index="+pos,null,_topfs)
}
}
function clickFilterTypeCB()
{
var o=advdef=this.parentWidget, icn = this, l=icn.layer,menu=o.typeMenu;
menu.show(true,getPosScrolled(l).x  + (menu.alignLeft?icn.getWidth():0) ,getPosScrolled(l).y+icn.getHeight()+1);
IconWidget_overCB(icn.index)
}
function openLOVDlgCB(o)
{
if(o==null) return;
_topfs.wt()
var advflt=o.parentWidget; 
var pos = o.pos
var id = advflt.id;
var isQuery=advflt.filter.isQuery;
if (isQuery)
{
frameNav("SecondDlgFrame",_topfs._root+"language/"+_topfs._lang+"/html/filterLOVDialog.html?ID="+advflt.id+"&index="+pos,null,_topfs)
}
}
function textModifiedCB()
{
var o=this.parentWidget;
var advflt=o.parentWidget;
var text=this.getValue(),s="";
if(o.type==_filterConst)
{
s=processFreeValues(text)
if(s!="")
this.setValue(s);
text=setFreeValues(text);
}
advflt.modifyFilter(2,text,o.pos);
}
function getBOFilterPart(o)
{
var filter = o.parentWidget.filter;
var fPart = filter.operands[o.pos];
return fPart;
}
function getAdvBOFilter(o)
{
var filter = o.parentWidget.filter;
return filter;
}
function getTypeMenuEntry(type)
{
var s="";
switch(type)
{
case _filterConst :
s="advf_constant";
break;
case _filterLov :
s="advf_fromList";
break;
case _filterPrompt :
s="advf_prompt";
break;
case _filterObj :
s="advf_object";
break;
}
return s;
}
function setDateFromStringCB()
{
var o=this.parentWidget;
var strDateValue=o.inputTxt.getValue();
var strInputFormat=_topfs._inputFormatDate;
var strRet = setDateValue(strDateValue, strInputFormat);
if(strRet == ",,")
o.calendar.menu.setToday(true);
else
{
var arr = strRet.split(",");
var strDay=arr[1],strMonth=arr[0],strYear=arr[2];
o.calendar.set(parseInt(strDay), parseInt(strMonth), parseInt(strYear))
o.calendar.menu.update()
}
}
function setStringFromDateCB() 
{
var o=this.parentWidget, advflt= o.parentWidget;
var format=""+_topfs._inputFormatDate
var date=new Date;
var menu=o.calendar.menu
date.setYear(menu.year)
date.setMonth(menu.month)
date.setDate(menu.day+1)
date.setHours(0)
date.setMinutes(0)
date.setSeconds(0)
var result=formatDate(date,format);
o.inputTxt.setValue(result);
advflt.modifyFilter(2,result,o.pos);
}

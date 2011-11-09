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
*/_womInstances=new Array
function AddInstance(obj) 
{
obj.idx=_womInstances.length;
_womInstances[obj.idx]=obj
}
_documentIconID = 0
_reportIconID = 1
_sectionIconID = 2
_filterIconID = 3
_headerIconID = 4
_bodyIconID = 5
_footerIconID = 6
_cellIconID = 53
_unv=0;
_cls=1;
_dim=2;
_msr=3;
_dtl=4;
_fil=5;
_variable=6;
_formula=7;
_hchy=8
_linkDim=14 
_date=0;
_num=1;
_txt=2;
_fntSMin = 1;
_fntSMax = 409;
function newBOObj(name,id,kind,dataType,desc,hasLov,isFormula,techInfo,lineage,mapping,dsName,hasIndexAware,aggregateFct)
{
var o=new Object;
o.name=name;
o.id=id;
o.kind=kind;
o.dataType=dataType;
o.par=null
AddInstance(o);
o.sub=new Array;
o.add=BOObj_add
o.getById=BOObj_getById;
o.isResultObj=BOObj_isResultObj;
o.getVariableID=BOObj_getVariableID
o.setFormula=BOObj_setFormula
o.getFormula=BOObj_getFormula
o.formula=""
o.linkedDim=null;
o.setlinkedDim=BOObj_setlinkedDim
o.getlinkedDim=BOObj_getlinkedDim
if (desc==null) desc="";
o.desc=desc;
o.hasLov=hasLov;
o.isFormula=isFormula?true:false
o.techInfo=techInfo?techInfo:""
o.lineage=lineage?lineage:""
o.mapping=mapping?mapping:""
o.dsName=dsName?dsName:""
o.hasIndexAware=hasIndexAware?hasIndexAware:false; 
o.aggregateFct=aggregateFct?aggregateFct:"";
o.allowInResult=true
o.allowInFilter=true
return o
}
function BOObj_getById(id) {if (this.id==id) return this;var sub=this.sub;for (var i=0;i<sub.length;i++){var result=sub[i].getById(id);if (result) return result;}return null;}
function BOObj_add(name,id,kind,dataType,desc,hasLov,isFormula,techInfo,lineage,mapping,dsName,hasIndexAware,aggregateFct) {var obj=newBOObj(name,id,kind,dataType,desc,hasLov,isFormula,techInfo,lineage,mapping,dsName,hasIndexAware,aggregateFct);arrayAdd(this,'sub',obj);obj.par=this;return obj}
function BOObj_isResultObj() {with (this) return (((kind==_dim)||(kind==_msr)||(kind==_dtl))&& allowInResult)}
function BOObj_sortFunc(v1,v2){if ((v1.kind!=v2.kind))return (v1.kind<v2.kind?-1:1);return (v1.name<v2.name?-1:1)}
function BOObj_getVariableID(){var o=this;return o.isFormula?o.name:o.id}
function BOObj_setFormula(s){var o=this;o.formula=s}
function BOObj_getFormula(){var o=this; return o.formula}
function BOObj_setlinkedDim(id){var o=this;o.linkedDim=id;}
function BOObj_getlinkedDim(){var o=this; return o.linkedDim}
function setListOrder()
{
var len = _docObjects.length
for (var i=0; i < len; i++)
{
kind = _docObjects[i].kind
switch(kind) 
{
case p._dim :
_docObjects[i].listOrder = 1
break;
case p._linkDim :
_docObjects[i].listOrder = 1
break;
case p._msr :
_docObjects[i].listOrder = 2
break;
default:
_docObjects[i].listOrder = 3
}
}
}
function sortFuncUsingListOrder(v1,v2)
{
if ((v1.listOrder!=v2.listOrder))return (v1.listOrder<v2.listOrder?-1:1);return (v1.name<v2.name?-1:1)
}
function renameSameNameDifferentDPObj()
{
var len = _docObjects.length
for (var i=0; i < len; i++)
{
itemName = _docObjects[i].name
if (!_docObjects[i].shortName) _docObjects[i].shortName = itemName
var isTheSame = false
var cpt = 0;
for (var j = i + 1; j < len; j++)
{
var itemName2 = _docObjects[j].name
if (!_docObjects[j].shortName) _docObjects[j].shortName = itemName2
if (itemName == itemName2)
{
if (_docObjects[j].DPName)
{
_docObjects[j].name = itemName2 + "(" + _docObjects[j].DPName + ")"
}
isTheSame = true
cpt++
}
else
break;
}
if (isTheSame)
{
if (_docObjects[i].DPName)
{
_docObjects[i].shortName = itemName
_docObjects[i].name = itemName + "(" + _docObjects[i].DPName + ")" 
}
isTheSame = false
i+=cpt
}
}
}
_filterPredefObj=0
_filterConst=1 
_filterPrompt=2
_filterLov=3 
_filterObj=4
_filterAdvanced=5 
_filterTopBottom=6
_filterEditable=7
_firstFilter=0
EQUAL=0
NOT_EQUAL=1
GREATER=2
GREATER_OR_EQUAL=3
LESS=4
LESS_OR_EQUAL=5
BETWEEN=6
NOT_BETWEEN=7
IN_LIST=8
NOT_IN_LIST=9
IS_NULL=10
NOT_IS_NULL=11
LIKE=12
NOT_LIKE=13
BOTH=14
EXCEPT=15
_lastFilter=15
AND=0
OR=1
BEGINPROMPT=2
ENDPROMPT=3
OBJECT=4
BASEON=5
FOREACH=6
ANY=7
ALL=8
function newBOFilterNode(filterAnd)
{
var o=new Object;
o.filterAnd=filterAnd;
o.par=null
o.init=BOFilterNode_init
o.items=new Array;
o.add=BOFilterNode_add
o.addNode=BOFilterNode_addNode
o.isNode=BOFilterNode_isNode
o.removeChild=BOFilterNode_removeChild
o.remove=BOFilterNode_remove
o.find=BOFilterNode_find
o.getChildIndex=BOFilterNode_getChildIndex
o.getInlineFilter=BOFilterNode_getInlineFilter
o.getDragTooltip=BOFilterNode_getInlineFilter
o.getHTML=BOFilterNode_getHTML
o.isOnlyNodeAdvanced=BOFilterNode_isOnlyNodeAdvanced
o.addCopyNode=BOFilterNode_addCopyNode
o.iterFilterChild=BOFilterNode_iterFilterChild
o.findFromQuestion=BOFilterNode_findFromQuestion
AddInstance(o);
return o
}
function BOFilterNode_init()
{
var o=this, l = getLayer(o.id)
l.onmousedown=BOFilterNode_clickCB
if (_ie)
l.onmouseup=BOFilterNode_triggerDD
}
function BOFilterNode_find(idx)
{
var node=this, items=node.items, len = items.length, ret= null
if(node.idx == idx) return node;
for (var i=0;i<len ;i++) 
{ 
var item = items[i]
if (item.idx==idx) return item;
if(item.isNode())
{
ret = item.find(idx)
if (ret!=null) return ret;
}
} 
return null
}
function BOFilterNode_findFromQuestion(q)
{
var node=this, items=node.items, len = items.length, ret= null
for (var i=0;i<len ;i++) 
{ 
var item = items[i]
if (item.isAdvFilter)
{
if(item.getOperandCount() > 0)
{
var fPart=item.getOperandPart(0);
if(fPart.type == _filterPrompt && fPart.operand==q)
{
return item;
}
if(item.getOperandCount() > 1)
{
fPart=item.getOperandPart(1);
if(fPart.type == _filterPrompt && fPart.operand==q)
{
return item;
}
}
}
}
if(item.isNode())
{
ret = item.findFromQuestion(q);
if (ret!=null) return ret;
}
} 
return null
}
function BOFilterNode_remove()
{
var o=this
if (o.par)
o.par.removeChild(o)
else 
{
var items=o.items, len = items.length
for (var i=len-1;i>=0;i--)
{
o.removeChild(items[i])
}
}
}
function BOFilterNode_removeChild(elem)
{
var o=this,items=o.items,len=items.length
for (var i=0;i<len;i++)
{
var item=items[i]
if (item.idx==elem.idx)
{
arrayRemove(o,"items",i)
return true
}
}
return false;
}
function BOFilterNode_isNode(){return true;} 
function BOFilterNode_addNode(filterAnd,idx) 
{
var obj=newBOFilterNode(filterAnd);
arrayAdd(this,'items',obj,idx);
obj.par=this;
return obj
}
function BOFilterNode_add(obj,idx)
{
arrayAdd(this,'items',obj,idx)
obj.par=this;
return obj
}
function BOFilterNode_getChildIndex(elem)
{
var items=this.items, len = items.length
for (var i=0;i<len ;i++) 
{ 
if (items[i].idx==elem.idx) return i;
} 
return -1
}
function BOFilterNode_addCopyNode(node,idx)
{
var o=this,obj=o.addNode(node.filterAnd,idx),nodeItems=node.items
for (var i in nodeItems)
{
var item=nodeItems[i]
if (item.isNode())
obj.addCopyNode(item,-1)
else
{
item = item.getCopy()
obj.add(item,-1)
}
}
return obj
}
function BOFilterNode_getInlineFilter() 
{
if (window._labFil==null) _labFil = parent._labFil
if (window._labOperand==null) _labOperand = parent._labOperand
var o=this,items=o.items,len=items.length, str=''
for (var i=0; i<len; i++)
{
var item=items[i]
str += "("
str += (item.isNode() ? item.getInlineFilter() : item.getLabel())
str += ")"
if (i < (len - 1)) {
str += " " + (item.par.filterAnd ? _labOperand[AND] : _labOperand[OR]) + " " 
}
}
return str
}
function BOFilterNode_isOnlyNodeAdvanced() 
{
var o=this,len=o.items.length
if (len==1) {
return ((o.items[0].kind ==_filterAdvanced)||(o.items[0].kind ==_filterTopBottom))
}
return false
}
function BOFilterNode_getHTML(treeView, filterID) 
{
if (window._labFil==null) _labFil = parent._labFil
if (window._labOperand==null) _labOperand = parent._labOperand
if (window._TreeWidgetElemInstances==null)
window._TreeWidgetElemInstances=new Array;
var o=this, a=new Array,i=0
a[i++] = '<table cellspacing="2" cellpadding="1"><tbody><tr>'
a[i++] ='<td>'
a[i++] =o.iterFilterChild(treeView, filterID)
a[i++] ='</td>'
a[i++] = '</tr></tbody></table>'
return a.join("");
}
function BOFilterNode_iterFilterChild(treeView, filterID) 
{
if (window._labFil==null) _labFil = parent._labFil
if (window._labOperand==null) _labOperand = parent._labOperand
if (window._TreeWidgetElemInstances==null)
window._TreeWidgetElemInstances=new Array;
var o=this,items=o.items,len=items.length, a=new Array,i=0
a[i++] = '<table cellspacing="2" cellpadding="1"><tbody>'
a[i++] = '<tr>'
a[i++] = '<td width="25" align="right" rowspan ="' + (len+1) +'" class="treeNormal filterOp" style="font-variant:small-caps;border-right:1px solid #A0A0A0;">' 
+ (o.filterAnd ? _labOperand[AND] : _labOperand[OR]) + '</td>'
a[i++] = '</tr>'
var sHTML=""
for (var j=0; j<len; j++)
{
a[i++] = '<tr><td>'
var item=items[j]
if (item.isNode()) 
{
a[i++] = item.iterFilterChild(treeView, filterID)
} 
else 
{
id = _TreeWidgetElemInstances.length;
var contextMenu=''
if (treeView.rightClickMenuCB != null)
{
contextMenu= ' oncontextmenu="' + _codeWinName + '.treeContextMenuCB(\''+ id + '\', event);  return false" '
}
a[i++] = '<a class="filterText" id="'+_codeWinName+'trLstElt' + id + '"'
a[i++] =' href="javascript:void(0)"' 
a[i++] =' onmousedown="'+_codeWinName+'.TreeWidget_clickCB(\''+ id +'\', false, event); return false"'
a[i++] =' ondblclick="'+_codeWinName+'.treeDblClickCB(\''+ id +'\', event); if(_saf||_ie) return false"'
a[i++] =contextMenu
a[i++] = '>'
a[i++] = convStr(item.getLabel())
a[i++] ='</a>'
treeView.add( newTreeWidgetVoidElem(_filterIconID, "\"" + item.getLabel() + "\"", item, filterID))
}
a[i++] ='</td></tr>'
}
a[i++] = '</tbody></table>'
return a.join("");
}
function BOFilterNode_triggerDD(e)
{
var o=getWidget(this)
if ((o.clicked) && (_curWin.event.button==_leftBtn))
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
function BOFilterNode_clickCB(e)
{
var o=getWidget(this)
o.clicked=true
o.initialX=eventGetX(e)
o.initialY=eventGetY(e)
return false
}
function newBOFilter(kind,obj)
{
var o=new Object;
o.kind=kind;
o.operator=EQUAL
o.par=null
o.obj=obj
o.operands=new Array('','')
o.advancedName=''
o.advancedIndex=-1
o.separator=';'
o.isIndexAware=false;
o.useListOfValues=obj?obj.hasLov:true
o.keepLastValues=true
o.selectFromList=obj?obj.hasIndexAware:false
o.optionalPrompt=false
o.useDefaultValues=false
o.defaultValues=new Array('','')
o.getLabel=BOFilter_getLabel;
o.isNode=BOFilter_isNode
o.getOperandCount=BOFilter_getOperandCount
o.getOperandValue=BOFilter_getOperandValue
o.isListOperator=BOFilter_isListOperator
o.getCopy=BOFilter_getCopy
o.copy=BOFilter_copy
o.remove=BOFilter_remove
o.setSeparator=BOFilter_setSeparator
AddInstance(o);
return o;
}
function BOFilter_isNode(){return false;} 
function BOFilter_remove()
{
var o=this
if (o.par)
o.par.removeChild(o)
} 
function BOFilter_getCopy()
{
var obj=newBOFilter();
obj.copy(this);
return obj
}
function BOFilter_copy(f)
{
var o=this
o.kind=f.kind;
o.par=f.par;
o.obj=f.obj;
o.operator=f.operator;
o.operands=new Array(f.operands[0],f.operands[1])
o.advancedName=f.advancedName
o.advancedIndex=f.advancedIndex
o.isIndexAware=f.isIndexAware
o.useListOfValues=f.useListOfValues
o.keepLastValues=f.keepLastValues
o.selectFromList=f.selectFromList
o.optionalPrompt=f.optionalPrompt
o.useDefaultValues=f.useDefaultValues
o.defaultValues=new Array(f.defaultValues[0],f.defaultValues[1])
AddInstance(o);
}
function BOFilter_setSeparator(s){this.separator=s;}
function BOFilter_getOperandValue(index)
{
with (this)
{
var values = operands[index].split(separator);
var s='';
for(var i=0; i< values.length;i++)
{
if(i>0) s+=separator;
var pos = values[i].indexOf('_');
if(pos>-1)
s+= values[i].substring(pos+1);
else
s+= values[i];
}
return s;
}
}
function BOFilter_getLabel(labelIdx) 
{
var s=''
with (this)
{
if (kind==_filterPredefObj) 
s = obj.name; 
else if ((kind==_filterAdvanced) || (kind==_filterTopBottom))
s =  advancedName; 
else if ((kind==_filterPrompt)&&(getOperandCount()>0)) 
{
if (getOperandCount()==1) 
s =  operands[0]; 
else 
{
if(labelIdx == 0)
s =  obj.name +' '+ _labFil[operator]
else if (labelIdx == 1)
s = _labOperand[AND] 
else
s =  obj.name +' '+ _labFil[operator] +' '+ _labOperand[BEGINPROMPT] + operands[0] + _labOperand[ENDPROMPT] +' ' + _labOperand[AND] +' '+ _labOperand[BEGINPROMPT] + operands[1] + _labOperand[ENDPROMPT];
}
}
else 
{
if(labelIdx == 0)
s =  obj.name +' '+ _labFil[operator]
else if (labelIdx == 1)
s = (getOperandCount()==2?' '+ _labOperand[AND]:'')
else
{
if(isIndexAware && (kind==_filterConst))
s =  obj.name +' '+ _labFil[operator] + ((getOperandCount()>0) ? (' '+ getOperandValue(0) + (getOperandCount()==2?' '+ _labOperand[AND] +' '+getOperandValue(1):'')):'');
else 
s =  obj.name +' '+ _labFil[operator] + ((getOperandCount()>0) ? (' '+ operands[0] + (getOperandCount()==2?' '+ _labOperand[AND] +' '+operands[1]:'')):'');
}
}
}
return s;
}
function BOFilter_getOperandCount() {
with (this)
{
if (kind==_filterPredefObj) return -1;
if ((kind==_filterAdvanced) || (kind==_filterTopBottom)) 
return -1;
else if ((operator==BETWEEN) || (operator==NOT_BETWEEN) || (operator==BOTH)) 
return 2;
else if ((operator==IS_NULL) || (operator==NOT_IS_NULL)) 
return 0;
else return 1;
}
}
function BOFilter_isListOperator()
{
with (this)
{
if ((operator==IN_LIST) || (operator==NOT_IN_LIST))
return true
else
return false
}
}
function newAdvBOFilter(kind,obj)
{
var o=new Object;
o.isAdvFilter=true;
o.kind=kind;
o.operator=IN_LIST
o.par=null
o.obj=obj
o.operands=new Array(2)
o.operands[0]=newBOFilterPart(_filterConst,obj,o)
o.operands[1]=newBOFilterPart(_filterConst,obj,o)
o.advancedName=''
o.advancedIndex=-1
o.separator=';'
o.isIndexAware=false;
o.isQuery=true;
o.getLabel=AdvBOFilter_getLabel;
o.isNode=AdvBOFilter_isNode
o.getOperandCount=AdvBOFilter_getOperandCount
o.getOperandValue=AdvBOFilter_getOperandValue
o.getOperandPart=AdvBOFilter_getOperandPart
o.isListOperator=AdvBOFilter_isListOperator
o.getCopy=AdvBOFilter_getCopy
o.copy=AdvBOFilter_copy
o.remove=AdvBOFilter_remove
o.setSeparator=AdvBOFilter_setSeparator
o.setOperator=AdvBOFilter_setOperator
AddInstance(o);
return o;
}
function AdvBOFilter_isNode(){return false;} 
function AdvBOFilter_remove()
{
var o=this
if (o.par)
o.par.removeChild(o)
} 
function AdvBOFilter_getCopy()
{
var obj=newAdvBOFilter();
obj.copy(this);
return obj
}
function AdvBOFilter_copy(f)
{
var o=this
o.kind=f.kind;
o.par=f.par;
o.obj=f.obj;
o.operator=f.operator;
o.operands=new Array(f.operands[0].getCopy(o),f.operands[1].getCopy(o))
o.advancedName=f.advancedName
o.advancedIndex=f.advancedIndex
o.separator=f.separator
o.isIndexAware=f.isIndexAware
AddInstance(o);
}
function AdvBOFilter_setSeparator(s){this.separator=s;}
function AdvBOFilter_setOperator(op)
{
var o=this, fPart = null;
o.operator=op; 
for(var i=0;i<2;i++)
{
fPart= o.getOperandPart(i);
fPart.operator=op;
}
}
function AdvBOFilter_getOperandValue(index)
{
if((index>1) || (index<0) ) return null;
var s='';
var fPart= this.operands[index];
if(fPart)
{
var values = fPart.operand.split(separator);
for(var i=0; i< values.length;i++)
{
if(i>0) s+=separator;
var pos = values[i].indexOf('_');
if(pos>-1)
s+= values[i].substring(pos+1);
else
s+= values[i];
}
}
return s;
}
function AdvBOFilter_getOperandPart(index)
{
if((index>1) || (index<0) ) return null;
else return this.operands[index];
}
function AdvBOFilter_getLabel(labelIdx) 
{
var s=''
with (this)
{
if (kind==_filterPredefObj) 
s = obj.name; 
else if ((kind==_filterAdvanced) || (kind==_filterTopBottom))
s =  advancedName; 
else if (kind==_filterEditable)
{
if(labelIdx!=null && labelIdx<getOperandCount() && labelIdx>-1)
{
s=getOperandPart(labelIdx).getLabel(labelIdx);
}
else
{
s= obj.name +' '+ _labFil[operator];
for(var i=0;i<getOperandCount();i++)
{
var fPart = getOperandPart(i);
if(i>0)
s+= ' ' +_labOperand[AND];
if (fPart.type==_filterConst || fPart.type==_filterLov)
{
if(isIndexAware)
s+=' '+fPart.getOperandValue();
else
s+= ' '+fPart.operand; 
}
else if (fPart.type==_filterPrompt)
{ 
s+=' '+ _labOperand[BEGINPROMPT]+fPart.operand+_labOperand[ENDPROMPT]; 
}
else if(fPart.type == _filterObj)
{
s+=' '+ _labOperand[OBJECT] + ' '+fPart.operand.name;
}
}
}
}
}
return s;
}
function AdvBOFilter_getOperandCount() {
with (this)
{
if (kind==_filterPredefObj) return -1;
if ((kind==_filterAdvanced) || (kind==_filterTopBottom)) 
return -1;
else if ((operator==BETWEEN) || (operator==NOT_BETWEEN) || (operator==BOTH)) 
return 2;
else if ((operator==IS_NULL) || (operator==NOT_IS_NULL)) 
return 0;
else return 1;
}
}
function AdvBOFilter_isListOperator()
{
return ((this.operator==IN_LIST) || (this.operator==NOT_IN_LIST))
}
function newBOFilterPart(type,obj,advFilter)
{
var o=new Object;
o.type=type;
o.operator=IN_LIST 
o.advFilter=advFilter
o.obj=obj
o.operand='' 
o.useListOfValues=obj?obj.hasLov:true
o.keepLastValues=true
o.selectFromList=obj?obj.hasIndexAware:false
o.optionalPrompt=false
o.useDefaultValues=false
o.defaultValues=''
o.getLabel=BOFilterPart_getLabel;
o.getOperandValue=BOFilterPart_getOperandValue
o.isListOperator=BOFilterPart_isListOperator
o.getCopy=BOFilterPart_getCopy
AddInstance(o);
return o;
}
function BOFilterPart_getOperandValue()
{
with (this)
{
var separator = advFilter.separator;
var values = operand.split(separator);
var s='';
for(var i=0; i< values.length;i++)
{
if(i>0) s+=separator;
var pos = values[i].indexOf('_');
if(pos>-1)
s+= values[i].substring(pos+1);
else
s+= values[i];
}
return s;
}
}
function BOFilterPart_getLabel( labelIdx , maxNameLength) 
{
var o=this,s="";
if(o.type == _filterPrompt)
{
s=o.operand;
}
else
{
    var name = o.obj.name;
    if(maxNameLength>0 && name.length>maxNameLength)
        name=name.substring(0,maxNameLength)+'...';
s =  name+' '+ _labFil[o.operator];
if(labelIdx == 1)
s += ' '+_labOperand[AND] 
}
return s;
}
function BOFilterPart_isListOperator()
{
return ((this.operator==IN_LIST) || (this.operator==NOT_IN_LIST))
}
function BOFilterPart_getCopy(par)
{
var o=this;
var obj=newBOFilterPart();
obj.type=o.type;
obj.operator=o.operator;
obj.advFilter=par;
obj.obj=o.obj;
obj.operand=o.operand;
obj.useListOfValues=o.useListOfValues;
obj.keepLastValues=o.keepLastValues;
obj.selectFromList=o.selectFromList;
obj.optionalPrompt=o.optionalPrompt;
obj.useDefaultValues=o.useDefaultValues;
obj.defaultValues=o.defaultValues;
AddInstance(obj);
return obj
}
function encodeSubAdvFilter(fil)
{
var s='',sOperand='';
var countOp = fil.getOperandCount();
if(countOp >0) 
{
var fpart=fil.getOperandPart(0);
sOperand=enc(
fpart.type,
encString(fpart.operand.id ?fpart.operand.id:decodeEmptyValue(fpart.operand)), 
encString(decodeEmptyValue(fpart.defaultValues)),
fpart.useListOfValues?1:0,
fpart.keepLastValues?1:0,
fpart.selectFromList?1:0,
fpart.optionalPrompt?1:0);
if(countOp >1) 
{
fpart=fil.getOperandPart(1);
sOperand+=","
sOperand+=enc(
fpart.type,
encString(fpart.operand.id ?fpart.operand.id:decodeEmptyValue(fpart.operand)),
encString(decodeEmptyValue(fpart.defaultValues)),
fpart.useListOfValues?1:0,
fpart.keepLastValues?1:0,
fpart.selectFromList?1:0,
fpart.optionalPrompt?1:0);
}
}
s+=enc(
fil.kind,
fil.operator,
encString(fil.obj?fil.obj.id:""),
sOperand)
return s;
}
function encodeSubFilters(fnode)
{
if (fnode.items)
{
var len=fnode.items.length;
var s='['
for (var k=0;k<len;k++)
{
if (k>0) s+=','
var fil=fnode.items[k]
if(fil.isNode())
{
s+=enc(fil.filterAnd,encodeSubFilters(fil))
}
else
{
if(fil.isAdvFilter)
{
s+=encodeSubAdvFilter(fil);
}
else
{
  s+=enc(
  fil.kind,
fil.operator,
encString(fil.obj?fil.obj.id:""),
encString(decodeEmptyValue(fil.operands[0])),
encString(decodeEmptyValue(fil.operands[1])),
encString(fil.advancedName),
fil.advancedIndex,    
fil.useListOfValues?1:0,
fil.keepLastValues?1:0,
fil.selectFromList?1:0,
fil.optionalPrompt?1:0,
encString(decodeEmptyValue(fil.defaultValues[0])),
encString(decodeEmptyValue(fil.defaultValues[1]))
)
}    
}
}
return s+']';
}
else
{
return '[]'
}
}
function encodeFilters(filtersRoot)
{
var s=''
cleanFilters(filtersRoot)
if(filtersRoot)
{
s+=enc(filtersRoot.filterAnd,encodeSubFilters(filtersRoot))
return s;
}
else return '[]';
}
function filtersGetCopy(root)
{
var o = newBOFilterNode(root.filterAnd)
treeGetCopy(root,o)
return o;
}
function treeGetCopy(oldNode, newNode)
{
var o, items= oldNode.items, len = items.length
for (var i=0;i<len;i++) 
{
var item=items[i]
if(item.isNode())
{
o=newNode.addNode(item.filterAnd,i)
treeGetCopy(item,o)
}
else
o=newNode.add(item.getCopy(),i)
}
return o;
}
function cleanFilters(fnode)
{
if (fnode.items)
{
var len=fnode.items.length;
for (var i=len-1;i>=0;i--)
{
var fil=fnode.items[i]
if(fil.isNode())
cleanFilters(fil)
}
}
if(fnode.items==null || fnode.items.length==0)
{
fnode.remove()
}
}
function newFormulaFunction(name,id,cat,type,hint,syntax,data)
{
var o=new Object;
o.name=name;
o.id=id;
o.category=cat;
o.returntype=type;
o.hint=hint;
o.syntax=syntax;
o.data=data;
return o; 
}
function newff(o,name,id,cat,type,hint,syntax,data)
{
o[o.length]=newFormulaFunction(name,id,cat,type,hint,syntax,data);
}
function newFormulaOperator(name,id)
{
var o=new Object;
o.name=name;
o.id=id;
return o; 
}
function newfo(o,name,id)
{
o[o.length]=newFormulaOperator(name,id);
}

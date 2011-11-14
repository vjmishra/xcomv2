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
_skin=_topfs._skin
_lang=_topfs._lang
_root=_topfs._root
function getOnlyIndexes(s) 
{
var arrIdxValues = s.split(_topfs._listSeparator), len = arrIdxValues.length;
var arrIndexes = new Array;
for(var i=0;i<len;i++)
{
var pos = arrIdxValues[i].indexOf('_');
if(pos>-1)
arrIndexes[i]=arrIdxValues[i].substring(0,pos);
else
arrIndexes[i]="";
}
return arrIndexes.join(_topfs._listSeparator);
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
return ret;
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
function newSelectedValuesWidget(id,operandCount,isList,nbLinesList,width,getSelectionCB,editableOperator, hasLov, isDocFilter,changeSelectionCB,opChangedCB,isMeasure)
{
var o=newWidget(id)
o.operandCount=operandCount
o.operands=new Array("","") 
o.isList=isList
o.getSelectionCB=getSelectionCB 
o.changeSelectionCB=changeSelectionCB 
o.values1Edit= newTextFieldWidget(id+"_v1Edit",null,null,null,null,true,null,width?width:200)
o.values2Edit= newTextFieldWidget(id+"_v2Edit",null,null,null,null,true,null,width?width:200)
o.valuesList= newMultiColumnList(id+"_v1list",null,true,width?width:200,null,selValues_dblClickCB,null,true,selValues_keyUpCB)
o.valuesList.setLines(nbLinesList?nbLinesList:12)
o.addValues1Btn= newButtonWidget(id+'_addV1Btn',null,addValuesCB,null,null,"Add selected value(s)",null,null,_skin+'buttonIcons.gif',16,16,0,0)
o.delValues1Btn= newButtonWidget(id+'_delV1Btn',null,removeValuesCB,null,null,"Remove selected value(s)",null,null,_skin+'buttonIcons.gif',16,16,0,16)
o.delValues1Btn.extraStyle="margin-top:2px;"
o.addValues2Btn= newButtonWidget(id+'_addV2Btn',null,addValuesCB,null,null,"Add selected value(s)",null,null,_skin+'buttonIcons.gif',16,16,0,0)
o.delValues2Btn= newButtonWidget(id+'_delV2Btn',null,removeValuesCB,null,null,"Remove selected value(s)",null,null,_skin+'buttonIcons.gif',16,16,0,16)
o.delValues2Btn.extraStyle="margin-top:2px;"
o.buttonsArea = newWidget(id+"_ButtonsArea")
o.operand2Values  =newWidget(id+"_Operand2Values")
o.operand2Buttons  = newWidget(id+"_Operand2Buttons")
o.label1  =newWidget(id+"_label1")
o.label2  =newWidget(id+"_label2")
o.editOperator= newWidget(id+"_editOperator")
o.selObj=newWidget(id+"_selObj")
o.sepBeforValues =newWidget(id+"_SepBeforValues")
o.opChangedCB = opChangedCB
o.operator=newComboWidget(id+"_operator",SelectedValuesWidget_opChangedCB,true,null,"Filter Operators");
o.operator.parentSV = o 
o.editableOperator = editableOperator?editableOperator:false
o.hasLov = (hasLov!=null)?hasLov:true
o.isDocFilter = isDocFilter?isDocFilter:false
o.oldInit=o.init
o.init=SelectedValuesWidget_init
o.getHTML=SelectedValuesWidget_getHTML
o.getTarget=SelectedValuesWidget_getTarget
o.getSource=SelectedValuesWidget_getSource
o.getValues=SelectedValuesWidget_getValues
o.setValues=SelectedValuesWidget_setValues
o.setLabels=SelectedValuesWidget_setLabels
o.fillOperatorList=SelectedValuesWidget_fillOperatorList
o.update=SelectedValuesWidget_update
o.updateOperands=SelectedValuesWidget_updateOperands
o.values1Edit.parentWidget=o
o.values2Edit.parentWidget=o
o.valuesList.parentWidget=o
o.addValues1Btn.parentWidget=o
o.delValues1Btn.parentWidget=o
o.addValues2Btn.parentWidget=o
o.delValues2Btn.parentWidget=o
return o;
}
function SelectedValuesWidget_init()
{
var o = this
o.label1.init()
o.label2.init()
o.values1Edit.init()
o.values2Edit.init()
o.values1Edit.setValue("")
o.values2Edit.setValue("")
o.values1Edit.setDisabled(true)
o.values2Edit.setDisabled(true)
o.valuesList.init()
o.addValues1Btn.init()
o.delValues1Btn.init()
o.addValues2Btn.init()
o.delValues2Btn.init()
o.buttonsArea.init()
o.operand2Buttons.init()
o.operand2Values.init()
o.editOperator.init()
o.selObj.init()
o.sepBeforValues.init()
o.operator.init()
if(o.isList) 
{
o.valuesList.setDisplay(true) 
o.values1Edit.setDisplay(false)
var tr = o.valuesList.layer.parentNode.parentNode
tr.vAlign="top"
}
else
{
o.values1Edit.setDisplay(o.operandCount>0)
o.valuesList.setDisplay(false) 
var tr = o.values1Edit.layer.parentNode.parentNode
tr.vAlign="middle"
}
o.editOperator.setDisplay(o.editableOperator)
o.label1.setDisplay(!o.editableOperator)
o.label2.setDisplay(o.operandCount==2)
o.buttonsArea.layer.vAlign=(o.isList?"center":"bottom")
o.operand2Buttons.setDisplay(o.operandCount==2)
o.operand2Values.setDisplay(o.operandCount==2)
o.addValues1Btn.setDisplay(o.operandCount>0)
o.delValues1Btn.setDisplay(o.operandCount>0)
o.sepBeforValues.setDisplay(o.operandCount>0 && !o.isList)
}
function SelectedValuesWidget_update(opCount,isList,lab1,lab2,editOp)
{
var o=this
o.operandCount = (opCount!=null)?opCount:o.operandCount
o.isList = (isList!=null)?isList:o.isList
o.setLabels(lab1,lab2)
if(o.isList) 
{
o.valuesList.setDisplay(true) 
o.values1Edit.setDisplay(false)
var tr = o.valuesList.layer.parentNode.parentNode
tr.vAlign="top"
}
else
{
o.values1Edit.setDisplay(o.operandCount>0)
o.valuesList.setDisplay(false) 
var tr = o.values1Edit.layer.parentNode.parentNode
tr.vAlign="middle"
}
o.label2.setDisplay(o.operandCount==2)
o.buttonsArea.layer.vAlign=(o.isList?"center":"bottom")
o.operand2Buttons.setDisplay(o.operandCount==2)
o.operand2Values.setDisplay(o.operandCount==2)
o.addValues1Btn.setDisplay(o.operandCount>0)
o.delValues1Btn.setDisplay(o.operandCount>0)
o.sepBeforValues.setDisplay(o.operandCount>0 && !o.isList)
o.valuesList.del()
o.values1Edit.setValue("")
o.values2Edit.setValue("")
if(editOp!=null)
{
o.editableOperator=editOp;
o.editOperator.setDisplay(o.editableOperator)
o.label1.setDisplay(!( o.editableOperator))
}
}
function SelectedValuesWidget_opChangedCB()
{
var o=this.parentSV 
var op = this.getSelection().value
var count = 1
count  = ((op==_topfs.BETWEEN) || (op==_topfs.NOT_BETWEEN) || (op==_topfs.BOTH)) ? 2 : count
count  = ((op==_topfs.IS_NULL) || (op==_topfs.NOT_IS_NULL)) ? 0 : count
o.operandCount = count
o.isList=((op==_topfs.IN_LIST) || (op==_topfs.NOT_IN_LIST))
if(o.isList) 
{
o.valuesList.freeze()
o.valuesList.del()
var tr = o.valuesList.layer.parentNode.parentNode
tr.vAlign="top"
if(o.isDocFilter && o.operands[0]!="") 
{
var arr=o.operands[0].split(_topfs._listSeparator);
for(var i=0; i<arr.length;i++)
{
o.valuesList.add(arr[i],arr[i]);
}
}
else 
{
o.operands[0]="";
}
o.valuesList.rebuildHTML()
o.valuesList.setDisplay(true) 
o.values1Edit.setDisplay(false)
}
else
{
o.values1Edit.setDisplay(count > 0)
o.valuesList.setDisplay(false) 
var tr = o.values1Edit.layer.parentNode.parentNode
tr.vAlign="middle"
if(o.isDocFilter) 
{
var s1="", s2="", arr;
if(count>0)
{
arr=o.operands[0].split(_topfs._listSeparator);
s1=arr[0];
if(count == 2)
{
arr=o.operands[1].split(_topfs._listSeparator);
s2=arr[0];
}
}
o.operands[0]=s1;
o.operands[1]=s2;
o.values1Edit.setValue(s1);
o.values2Edit.setValue(s2);
}
else 
{
o.operands[0]="";
o.operands[1]="";
o.values1Edit.setValue("");
o.values2Edit.setValue("");
}
}
o.buttonsArea.layer.vAlign=(o.isList?"center":"bottom")
o.operand2Buttons.setDisplay(count == 2)
o.operand2Values.setDisplay(count == 2)
o.label2.setDisplay(count == 2)
o.addValues1Btn.setDisplay(count >0)
o.delValues1Btn.setDisplay(count >0)
o.sepBeforValues.setDisplay(count>0 && !o.isList)
if(o.opChangedCB) o.opChangedCB(o.isList,count)
}
function SelectedValuesWidget_getHTML()
{
var o=this
return ('<table border="0" cellspacing="0" cellpadding="2"><tbody><tr>'+
'<td id="'+o.id+'_ButtonsArea"><table border="0" cellspacing="0" cellpadding="4"><tbody>'+
'<tr><td valign="middle">'+
o.addValues1Btn.getHTML()+
o.delValues1Btn.getHTML()+
'</td></tr>'+
'<tr><td id="'+o.id+'_Operand2Buttons" valign="bottom">'+
o.addValues2Btn.getHTML()+
o.delValues2Btn.getHTML()+
'</td></tr>'+
'</tbody></table></td>'+
'<td valign="top"><table border="0" cellspacing="0" cellpadding="4" class="LOVZone" ><tbody>'+
'<tr><td width="200" height="100%" align="center">'+
'<div id="'+o.id+'_label1"></div>'+
'<table id="'+o.id+'_editOperator" border="0"><tbody><tr>'+
'<td><span class="dialogzone" id="'+o.id+'_selObj"></span></td>'+
'<td><span style="padding-left:5px">' + o.operator.getHTML()+'</span></td>'+
'</tr></tbody></table>'+
'</td>'+
'</tr>'+
'<tr id="'+o.id+'_SepBeforValues"><td>&nbsp;<td><tr>'+
'<tr><td width="200">'+ 
o.values1Edit.getHTML()+
o.valuesList.getHTML()+
'</td></tr>'+
'<tr><td><div id="'+o.id+'_label2"></div></td><tr>'+
'<tr><td id="'+o.id+'_Operand2Values" width="200">'+
o.values2Edit.getHTML()+
'</td></tr>'+
'</tbody></table></td>'+
'</tr></tbody></table>')
}
function SelectedValuesWidget_getTarget(id)
{
var o=this, target=null
if(id==o.id+'_addV1Btn' || id==null)
{
if(o.isList)
target = o.valuesList
else
target = o.values1Edit
}
if(id==o.id+'_addV2Btn')
target=o.values2Edit
return target
}
function SelectedValuesWidget_getSource(id)
{
var o=this, source=null
if(id==o.id+'_delV1Btn')
{
if(o.isList)
source = o.valuesList
else
source = o.values1Edit
}
else if(id==o.id+'_delV2Btn')
source=o.values2Edit
else if(id==o.valuesList.id)
source = o.valuesList
return source
}
function SelectedValuesWidget_updateOperands(arr,widget)
{
var o = this;
if(widget.id == o.valuesList.id)
{
o.operands[0]="";
var len=o.valuesList.getCount();
if(len > 0)
{
var arrValues=new Array;
for (var i=0;i<len;i++)
{
arrValues[arrValues.length]=o.valuesList.getValue(i)
}
o.operands[0]=arrValues.join(_topfs._listSeparator)
}
}
else if(widget.id == o.values1Edit.id)
{
o.operands[0]="";
if(arr && arr.length>0) o.operands[0]=arr[0];
}
else
{
o.operands[1]="";
if(arr && arr.length>0) o.operands[1]=arr[0];
}
}
function SelectedValuesWidget_getValues(textIndex)
{
return this.operands[textIndex-1];
}
function SelectedValuesWidget_setValues(s1,s2,exp,kind,obj)
{
var o=this,s=''
o.operands[0]=s1;
o.operands[1]=s2;
if(o.operandCount==1)
{
if(o.isList)
{
o.valuesList.freeze()
o.valuesList.del()
if(s1!="")
{
var arr=s1.split(_topfs._listSeparator), len=arr.length
for(var i=0;i<len;i++)
{
o.valuesList.add(getOnlyValues(arr[i],o.isDocFilter),arr[i])
}
}
o.valuesList.rebuildHTML()
}
else
{
o.values1Edit.setValue(getOnlyValues(s1,o.isDocFilter))
}
o.values2Edit.setValue("")
}
else
{
o.values1Edit.setValue(getOnlyValues(s1,o.isDocFilter))
o.values2Edit.setValue(getOnlyValues(s2,o.isDocFilter))
}
if (o.editableOperator)
{
var s=exp;
var len=12;
if (s.length > len)
{ 
s=s.substring(0, len)
s+="..."
}
o.selObj.setHTML(elasticZone(s,_skin+'bolist.gif',3,28,0,null,'treeNormal',_img+"qualificationIcons.gif",0,kind*16,null,null,null,null,null,"default"))
o.selObj.layer.title=exp
o.fillOperatorList(obj)
}
}
function SelectedValuesWidget_fillOperatorList(obj)
{
var o=this
o.operator.show(false)
o.operator.del();
 var labFil=_topfs._labFil?_topfs._labFil:_labFil;
if (obj!=null)
{
var isString = (obj.dataType==_topfs._txt)
var isMes = !o.hasLov
for (var i= _topfs._firstFilter; i <= _topfs._lastFilter; i++)
{
if (o.isDocFilter) { 
if ((i != _topfs.BOTH) && (i !=_topfs.EXCEPT) && (i != _topfs.LIKE) && (i !=_topfs.NOT_LIKE)) {
o.operator.add(labFil[i],""+i,(i==obj.operator));
}
} else {
if (isString) { 
o.operator.add(labFil[i],""+i,(i==obj.operator));
} else if ((i != _topfs.LIKE) && (i !=_topfs.NOT_LIKE)) {
o.operator.add(labFil[i],""+i,(i==obj.operator));
}
}
}
}
o.operator.show(true)
}
function SelectedValuesWidget_setLabels(l1,l2)
{
var o = this
if(l1!=null)
o.label1.setHTML(l1)
if(l2!=null)
o.label2.setHTML(l2)
}
function addValuesCB()
{
var o=this.parentWidget
var arrSel=new Array
if(o.getSelectionCB) 
arrSel=o.getSelectionCB()
if(arrSel == null || arrSel.length == 0) return;
var target= o.getTarget(this.id)
var len=arrSel.length
if(len>0)
{
for(var i=0;i<len;i++)
{
var v=arrSel[i];
var s=getOnlyValue(v,o.isDocFilter);
if(o.isList)
{
if(canAddValue(target,v,!o.isDocFilter))
target.add(s,v);
}
else
{
target.setValue(s);
break;
}
}
o.updateOperands(arrSel,target)
if(o.changeSelectionCB)
o.changeSelectionCB()
}
}
function removeValuesCB(layer)
{
var l=layer?layer:this
var o=l.parentWidget
var source= o.getSource(l.id)
if(o.isList)
{
var arrSel=source.getMultiSelection(), len=arrSel.length
for(var i=len;i>0;i--)
{
source.del(arrSel[i-1].index)
}
}
else
{
source.setValue("")
}
o.updateOperands(null,source)
if(o.changeSelectionCB)
o.changeSelectionCB()
}
function selValues_dblClickCB()
{
removeValuesCB(this) 
}
function selValues_keyUpCB(e)
{
var o=this.parentWidget
var key=eventGetKey(e);
if (key==46)
{
removeValuesCB(this)
}
}
function newSelectedLOVWidget(id,operandCount,isList,nbLinesList,width,getSelectionCB)
{
var o=newWidget(id)
o.operandCount=operandCount
o.operand=""; 
o.isList=isList
o.getSelectionCB=getSelectionCB 
o.changeSelectionCB=changeSelectionCB 
o.values1Edit= newTextFieldWidget(id+"_v1Edit",null,null,null,null,true,null,width?width:200)
o.valuesList= newMultiColumnList(id+"_v1list",null,true,width?width:200,null,selValues_dblClickCB,null,true,selValues_keyUpCB)
o.valuesList.setLines(nbLinesList?nbLinesList:12)
o.addValues1Btn= newButtonWidget(id+'_addV1Btn',null,addValuesCB,null,null,"Add selected value(s)",null,null,_skin+'buttonIcons.gif',16,16,0,0)
o.delValues1Btn= newButtonWidget(id+'_delV1Btn',null,removeValuesCB,null,null,"Remove selected value(s)",null,null,_skin+'buttonIcons.gif',16,16,0,16)
o.delValues1Btn.extraStyle="margin-top:2px;"
o.label1  =newWidget(id+"_label1")
o.oldInit=o.init
o.init=SelectedLOVWidget_init
o.getHTML=SelectedLOVWidget_getHTML
o.getTarget=SelectedLOVWidget_getTarget
o.getSource=SelectedLOVWidget_getSource
o.getValues=SelectedLOVWidget_getValues
o.setValues=SelectedLOVWidget_setValues
o.setLabels=SelectedLOVWidget_setLabels
o.update=SelectedLOVWidget_update
o.updateOperands=SelectedLOVWidget_updateOperands
o.values1Edit.parentWidget=o
o.valuesList.parentWidget=o
o.addValues1Btn.parentWidget=o
o.delValues1Btn.parentWidget=o
return o;
}
function SelectedLOVWidget_init()
{
var o = this
o.label1.init()
o.values1Edit.init()
o.values1Edit.setValue("")
o.values1Edit.setDisabled(true)
o.valuesList.init()
o.addValues1Btn.init()
o.delValues1Btn.init()
if(o.isList) 
{
o.valuesList.setDisplay(true) 
o.values1Edit.setDisplay(false)
var tr = o.valuesList.layer.parentNode.parentNode
tr.vAlign="top"
}
else
{
o.values1Edit.setDisplay(o.operandCount>0)
o.valuesList.setDisplay(false) 
var tr = o.values1Edit.layer.parentNode.parentNode
tr.vAlign="middle"
}
o.addValues1Btn.setDisplay(o.operandCount>0)
o.delValues1Btn.setDisplay(o.operandCount>0)
}
function SelectedLOVWidget_update(opCount,isList,lab1)
{
var o=this
o.operandCount = (opCount!=null)?opCount:o.operandCount
o.isList = (isList!=null)?isList:o.isList
o.setLabels(lab1)
if(o.isList) 
{
o.valuesList.setDisplay(true) 
o.values1Edit.setDisplay(false)
var tr = o.valuesList.layer.parentNode.parentNode
tr.vAlign="top"
}
else
{
o.values1Edit.setDisplay(o.operandCount>0)
o.valuesList.setDisplay(false) 
var tr = o.values1Edit.layer.parentNode.parentNode
tr.vAlign="middle"
}
o.addValues1Btn.setDisplay(o.operandCount>0)
o.delValues1Btn.setDisplay(o.operandCount>0)
o.valuesList.del()
o.values1Edit.setValue("")
}
function SelectedLOVWidget_getHTML()
{
var o=this
return ('<table border="0" cellspacing="0" cellpadding="2"><tbody><tr>'+
'<td><table border="0" cellspacing="0" cellpadding="4"><tbody>'+
'<tr><td valign="middle">'+
o.addValues1Btn.getHTML()+
o.delValues1Btn.getHTML()+
'</td></tr>'+
'</tbody></table></td>'+
'<td valign="top"><table border="0" cellspacing="0" cellpadding="4" class="LOVZone" ><tbody>'+
'<tr><td width="200" height="100%" align="center">'+
'<div id="'+o.id+'_label1"></div>'+
'</td>'+
'</tr>'+
'<tr><td width="200">'+ 
o.values1Edit.getHTML()+
o.valuesList.getHTML()+
'</td></tr>'+
'</tbody></table></td>'+
'</tr></tbody></table>')
}
function SelectedLOVWidget_getTarget(id)
{
var o=this, target=null
if(id==o.id+'_addV1Btn' || id==null)
{
if(o.isList)
target = o.valuesList
else
target = o.values1Edit
}
return target
}
function SelectedLOVWidget_getSource(id)
{
var o=this, source=null
if(id==o.id+'_delV1Btn')
{
if(o.isList)
source = o.valuesList
else
source = o.values1Edit
}
else if(id==o.valuesList.id)
source = o.valuesList
return source
}
function SelectedLOVWidget_updateOperands(arr,widget)
{
var o = this;
if(widget.id == o.valuesList.id)
{
o.operand="";
var len=o.valuesList.getCount();
if(len > 0)
{
var arrValues=new Array;
for (var i=0;i<len;i++)
{
arrValues[arrValues.length]=o.valuesList.getValue(i)
}
o.operand=arrValues.join(_topfs._listSeparator)
}
}
else if(widget.id == o.values1Edit.id)
{
o.operand="";
if(arr && arr.length>0) o.operand=arr[0];
}
}
function SelectedLOVWidget_getValues()
{
return this.operand;
}
function SelectedLOVWidget_setValues(s1,exp,kind,obj)
{
var o=this,s=''
o.operand=s1;
if(o.operandCount==1)
{
if(o.isList)
{
o.valuesList.freeze()
o.valuesList.del()
if(s1!="")
{
var arr=s1.split(_topfs._listSeparator), len=arr.length
for(var i=0;i<len;i++)
{
o.valuesList.add(getOnlyValues(arr[i]),arr[i])
}
}
o.valuesList.rebuildHTML()
}
else
{
o.values1Edit.setValue(getOnlyValues(s1))
}
}
else
{
o.values1Edit.setValue(getOnlyValues(s1))
}
}
function SelectedLOVWidget_setLabels(l1)
{
var o = this
if(l1!=null)
o.label1.setHTML(l1)
}
function newDefaultValuesWidget(id,filter,pos)
{
var o=newWidget(id)
o.filter=filter; 
o.pos=pos;
o.values1Edit= newTextFieldWidget(id+"_v1Edit",null,null,defaultTextChangedCB,addDefaultValuesCB,true,null,150)
o.valuesList= newMultiColumnList(id+"_v1list",null,true,200,null,dftValues_dblClickCB,null,true,dftValues_keyUpCB)
o.valuesList.setLines(5)
o.addValuesBtn= newButtonWidget(id+'_addValuesBtn',null,addDefaultValuesCB,null,null,"Add selected value(s)",null,null,_skin+'buttonIcons.gif',16,16,0,0)
o.delValuesBtn= newButtonWidget(id+'_delValuesBtn',null,removeDefaultValuesCB,null,null,"Remove selected value(s)",null,null,_skin+'buttonIcons.gif',16,16,0,16)
o.delValuesBtn.extraStyle="margin-top:2px;"
o.showValuesBtn= newButtonWidget(id+'_showValuesBtn',"Values",showValuesCB,null,null,null,null,null,null)
o.oldInit=o.init
o.init=DefaultValuesWidget_init
o.getHTML=DefaultValuesWidget_getHTML
o.setEditDisabled=DefaultValuesWidget_setEditDisabled
o.setShowLOVDisabled=DefaultValuesWidget_setShowLOVDisabled
o.getFilter=DefaultValuesWidget_getFilter
o.getText=DefaultValuesWidget_getText
o.setLovSelection=DefaultValuesWidget_setLovSelection
o.addValuesBtn.parentWidget=o
o.delValuesBtn.parentWidget=o
o.showValuesBtn.parentWidget=o
o.values1Edit.parentWidget=o
o.valuesList.parentWidget=o
o.isDefaultValues=true; 
return o;
}
function DefaultValuesWidget_init()
{
var o=this
o.oldInit()
o.layer._codeWinName=_codeWinName
var fPart = o.filter.getOperandPart(o.pos)
o.isList=fPart?fPart.isListOperator():true
o.values1Edit.init()
o.values1Edit.setValue("")
o.values1Edit.setDisplay(true);
o.values1Edit.hasChanged=false;
o.valuesList.init()
o.addValuesBtn.init()
o.delValuesBtn.init()
o.showValuesBtn.init()
if(o.isList) 
{
o.addValuesBtn.setDisplay(true)
o.delValuesBtn.setDisplay(true)
o.valuesList.setDisplay(true)
}
else
{
o.addValuesBtn.setDisplay(false)
o.delValuesBtn.setDisplay(false)
o.valuesList.setDisplay(false) 
}
}
function DefaultValuesWidget_getHTML()
{
var o=this
return ('<table id="'+o.id+'"><tr><td><div class="dialogzone">Type value(s) in the text box(s) or click Values</div></td></tr><tr>'+
'<td>'+
'<table><tr><td>'+
o.values1Edit.getHTML()+
'</td><td>'+
o.addValuesBtn.getHTML()+
o.delValuesBtn.getHTML()+
'</td><td>'+
o.valuesList.getHTML()+
'</td></tr>'+
'</table>'+
'</td>'+
'<td>'+
o.showValuesBtn.getHTML()+
'</td>'+
'<td height="1"><iframe name="'+o.id+'_DefaultValuesDlgFrame" style="width:1px;height:1px;visibility:hidden" frameborder="0" src="lib/empty.html"></iframe></td>'+
'</tr></table>')
} 
function DefaultValuesWidget_setEditDisabled(b)
{
var o=this
o.values1Edit.setDisabled(b)
if(o.isList) 
{
o.values1Edit.setDisplay(!b)
o.addValuesBtn.setDisplay(!b)
o.delValuesBtn.setDisplay(!b)
}
}
function DefaultValuesWidget_setShowLOVDisabled(b)
{
var o=this
o.showValuesBtn.setDisabled(b)
_topfs.initUserRight(o.showValuesBtn,_topfs._usrUseLOV);
}
function DefaultValuesWidget_getFilter()
{ 
return this.filter;
}
function DefaultValuesWidget_getText()
{
var o=this,s ='';
if(o.isList) 
{
var len=o.valuesList.getCount();
if(len > 0)
{
var arrValues=new Array;
for (var i=0;i<len;i++)
{
arrValues[arrValues.length]=o.valuesList.getValue(i);
}
s=arrValues.join(_topfs._listSeparator)
}
}
else
{
s=setFreeValues(o.values1Edit.getValue());
}
return s;
}
function DefaultValuesWidget_setLovSelection(s1,pos)
{
var o=this
if(o.isList) 
{
o.valuesList.freeze()
o.valuesList.del()
if(s1!="")
{
var arr=s1.split(_topfs._listSeparator), len=arr.length
for(var i=0;i<len;i++)
{
o.valuesList.add(getOnlyValues(arr[i]),arr[i])
}
}
o.valuesList.rebuildHTML()
}
else
{
o.values1Edit.setValue(getOnlyValues(s1))
}
}
function showValuesCB()
{
var o=this.parentWidget; 
var id=o.id; 
var pos=o.pos;
var fPart = o.filter.getOperandPart(o.pos);
fPart.defaultValues=o.getText();
frameNav(id+"_DefaultValuesDlgFrame",_root+"language/"+_lang+"/html/filterLOVDialog.html?ID="+id+"&index="+pos,null,_curWin);
}
function addDefaultValuesCB() 
{
var o=this.parentWidget
var v = setFreeValues(o.values1Edit.getValue())
if(o.valuesList.isDisplayed() && v!="")
{
var arr=v.split(_topfs._listSeparator), len=arr.length,s="";
for(var i=0;i<len;i++)
{
s=getOnlyValue(arr[i]);
if(canAddValue(o.valuesList,arr[i],true))
o.valuesList.add(s,arr[i])
}
}
o.values1Edit.setValue("");
}
function removeDefaultValuesCB(layer)
{
var o=layer?layer.parentWidget:this.parentWidget
var arrSel=o.valuesList.getMultiSelection(), len=arrSel.length
for(var i=len;i>0;i--)
{
o.valuesList.del(arrSel[i-1].index)
}
}
function dftValues_dblClickCB()
{
removeDefaultValuesCB(this)
}
function dftValues_keyUpCB(e)
{
var o=this.parentWidget
var key=eventGetKey(e);
if (key==46)
{
removeDefaultValuesCB(this)
}
}
function defaultTextChangedCB()
{
}
_sNeedAnswer ="(--Answer the related prompts in order to view available Values--)"
_sNoValue= "(--No value--)"
function getLovChoices(lovWidget,isList,onlyText,bRowIndex,onlyLov)
{
var arrSel=new Array
if(onlyLov == true)
{
var sel = lovWidget.getLOVSelection()
var len=sel.length
for(var i=0;i<len;i++) 
{
if(sel[i].value != _sNeedAnswer && sel[i].value != _sNoValue)
arrSel[arrSel.length]=sel[i].value
}
}
else
{
var text = processFreeValues(lovWidget.getTextValue())
if(text!="") 
{
if(isList)
{
var arr=text.split(_topfs._listSeparator), arrlen=arr.length
for(var i=0;i<arrlen;i++)
{
if(bRowIndex)
arrSel[arrSel.length]="_"+arr[i]
else
arrSel[arrSel.length]=arr[i]
}
}
else
{
if(bRowIndex)
arrSel[arrSel.length]="_"+text
else
arrSel[arrSel.length]=text
}
}
lovWidget.setTextValue("")
text= processFreeValues(lovWidget.getDateValue())
if(text!="") 
{
if(isList)
{
var arr=text.split(_topfs.listSeparator), arrlen=arr.length
for(var i=0;i<arrlen;i++)
{
if(bRowIndex)
arrSel[arrSel.length]="_"+arr[i]
else
arrSel[arrSel.length]=arr[i]
}
}
else
{
if(bRowIndex)
arrSel[arrSel.length]="_"+text
else
arrSel[arrSel.length]=text
}
}
lovWidget.setDateValue("")
if(onlyText == true) return arrSel; 
var sel = lovWidget.getLOVSelection()
var len=sel.length
for(var i=0;i<len;i++)
{
if(sel[i].value != _sNeedAnswer && sel[i].value != _sNoValue)
arrSel[arrSel.length]=sel[i].value
}
}
return arrSel;
}
function canAddValue(list,str,isIndexAware)
{
var l=list.getCount();
if(isIndexAware)
{
var pos,idx,val,row,v,idxtmp,valTmp;
pos=str.indexOf('_');
idx=str.substring(0,pos);
val=str.substring(pos+1);
for (j=0;j<l;j++)
{
row=list.getRow(j);
v=row.value;
if (v==str)
return false
pos=v.indexOf('_');
idxTmp=v.substring(0,pos);
valTmp=v.substring(pos+1);
if((valTmp == val)&& (idxTmp=="" || idx=="") )
{
return false;
}
}
}
else
{
for (j=0;j<l;j++)
{
if(list.getRow(j).value==str)
{
return false;
}
}
}
return true;
}

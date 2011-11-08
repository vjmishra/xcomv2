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
*/_universes=new Array
_universesHrc=new Array 
_queries=new Array
function newBOUnv(name,id)
{
var o=new Object;
o.name=name;
o.id=id;
o.root=newBOObj(name,id,_unv,"",false);
o.getById=BOUnv_getById;
o.kind=_unv;
AddInstance(o);
return o
}
function BOUnv_getById(id) {return this.root.getById(id)}
function newBOProvider(name,scope)
{
var o=new Object;
o.name=name
o.scope=scope
o.haveMaxRows=false
o.haveMaxTime=false
o.maxRows=2000
o.maxTime=10
o.duplicateRows=true
o.refreshContexts=true
o.objs=new Array
o.filtersTree=null
o.contexts=new Array 
o.prompts=new Array 
o.isModified=false;
o.allowViewSQL=true;
o.allowDuplicateRows=true;
o.allowBoth=true;
o.allowExcept=true;
o.allowIsNull=true;
o.allowNotIsNull=true;
o.allowSampling=true;
o.allowSeededSampling=true;
o.samplingMode="none"; 
o.samplingSize=500;
o.isCustomSQL=false;
o.unvID="";
o.setModified=BOProvider_setModified
o.addObj=BOProvider_addObj
o.remObj=BOProvider_remObj
o.moveObj=BOProvider_moveObj
o.findObj=BOProvider_findObj
o.findFilter=BOProvider_findFilter
o.addContext=BOProvider_addContext
o.resetContexts=BOProvider_resetContexts
o.addPrompt=BOProvider_addPrompt
o.resetPrompt=BOProvider_resetPrompt
o.getCopy=BOProvider_getCopy
AddInstance(o);
return o
}
function BOProvider_setModified() {this.isModified=true;}
function BOProvider_addObj(obj,i){arrayAdd(this,'objs',obj,i)}
function BOProvider_remObj(i) {arrayRemove(this,'objs',i)}
function BOProvider_moveObj(i,j) {return arrayMove(this,'objs',i,j)}
function BOProvider_findObj(id) {var objs=this.objs; for (var i=0;i<objs.length;i++) {if (objs[i].id==id) return i;} return -1}
function BOProvider_addContext(ctx,i) {arrayAdd(this,'contexts',ctx,i)}
function BOProvider_resetContexts() {arrayRemove(this,'contexts')}
function BOProvider_addPrompt(v,i) {arrayAdd(this,'prompts',v,i)}
function BOProvider_resetPrompt(i) {arrayRemove(this,'prompts',i)}
function BOProvider_findFilter(idx) 
{
var root = this.filtersTree
if (root == null) return null;
if (idx=="") return root;
return root.find(idx)
}
function BOProvider_getCopy()
{
var o=newBOProvider();
o.name=this.name
o.scope=this.scope
o.haveMaxRows=this.haveMaxRows
o.haveMaxTime=this.haveMaxTime
o.maxRows=this.maxRows
o.maxTime=this.maxTime
o.duplicateRows=this.duplicateRows
o.refreshContexts=this.refreshContexts
o.objs=arrayGetCopy(this.objs)
o.filtersTree=filtersGetCopy(this.filtersTree)
o.prompts=arrayGetCopy(this.prompts)
o.isModified=this.isModified;
o.allowViewSQL=this.allowViewSQL;
o.allowDuplicateRows=this.allowDuplicateRows;
o.allowBoth=this.allowBoth;
o.allowExcept=this.allowExcept;
o.allowIsNull=this.allowIsNull;
o.allowNotIsNull=this.allowNotIsNull;
o.isCustomSQL=false;
o.unvID=this.unvID
return o
}
function newDPInfo(name,editable,rows,refreshable,dpType)
{
var o=new Object
o.name=name
o.editable=editable
o.rows=rows
o.refreshable=refreshable
o.dpType=dpType 
return o
}
function newValueRowIndex(value,rowIdx)
{
var o=new Object
o.value=value
o.rowIdx=rowIdx
return o
}
function newBOSQLNode(operator)
{
var o=new Object;
o.operator=operator;
o.par=null
o.init=BOSQLNode_init
o.items=new Array;
o.add=BOSQLNode_add
o.addNode=BOSQLNode_addNode
AddInstance(o);
return o
}
function BOSQLNode_init()
{
this.init();
}
function BOSQLNode_add(obj)
{
arrayAdd(this,'items',obj)
obj.par=this;
return obj;
}
function BOSQLNode_addNode(op)
{
var obj=newBOSQLNode(op);
arrayAdd(this,'items',obj);
obj.par=this;
return obj;
}
function newBOSQLStatement(sql)
{
var o=new Object;
o.value=sql;
o.isModified=false;
o.par=null
AddInstance(o);
return o
}

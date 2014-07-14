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
_root=_p._root
_appExt=_p._appExt
_prevTooltip=''
if(!stopLoadingDP)
{
_isPageLoaded=false
_expanded=false;
_currentObj=null;
_currentObjects=new Array;
_resultObj=null;
_allowDPViewSQL=_currentDP.allowViewSQL
_allowDPProperties=true
HgrabX=_p._unvPaneWidth
VgrabY=_p._queryPaneHeight
marge=5
buttonsw=50 
rzwmin=200 
qzhmin=100 
universeContainer=newWidget("universeContainer")
queryContainer=newWidget("queryContainer")
filterContainer=newWidget("filterContainer")
universeZone=newFrameZoneWidget("qpUniverseZone",200,200)
queryZone=newFrameZoneWidget("qpQueryZone",200,200)
addObjButton  = newButtonWidget('addObjButton',null,'addFromButton()',null,null,"Add selected object to Result Objects pane",null,null,_skin+'buttonIcons.gif',16,16,0,0,null,1*16,0)
delObjButton  = newButtonWidget('delObjButton',null,'removeFromButton()',null,null,"Remove selected object from Result Objects pane",null,null,_skin+'buttonIcons.gif',16,16,0,16)
delObjButton.extraStyle="margin-top:2px;"
addFltButton  = newButtonWidget('addFltButton',null,'addFilterFromButton()',null,null,"Add the selected object to the Query Filters pane",null,null,_skin+'buttonIcons.gif',16,16,0,0,null,1*16,0)
delFltButton  = newButtonWidget('delFltButton',null,'removeFilterFromButton()',null,null,"Remove the selected object from the Query Filters pane",null,null,_skin+'buttonIcons.gif',16,16,0,16)
delFltButton.extraStyle="margin-top:2px;"
objectDescZone= newInfoWidget("objectDescZone","Description:")
objectSearchZone= newSearchWidget("objectSearchZone",null,searchObjectCB,"");
disObjRadio= newRadioWidget("disObjRadio","displayObjGrp", 'Display by objects', changeUnvDisplayMode)
disHrcRadio= newRadioWidget("disHrcRadio","displayObjGrp", 'Display by hierarchies', changeUnvDisplayMode)
disObjDiv= newWidget("disObjDiv");
disKeyDateSpan= newWidget("keydateInfo");
queryListContainer=newBOListContainerWidget("queryList",10,80,_img+'qualificationIcons.gif',_wrapBOList,queryChangeCB,null,queryMoveCB,removeFromButton,true,null,addQuickFilter,_ie?"To include data in reports, select objects on the Universe pane and add them here by clicking the arrow or using drag-and-drop. Click Run Query to return the results.":"To include data in reports, select objects on the Universe pane and add them here by clicking the arrow. Click Run Query to return the results.",removeAllObjectsCB)
queryList=queryListContainer.getList()
queryList.setDragDrop(treeDragCB,treeAcceptDropCB,treeDropCB)
unvRootObj = newTreeWidgetElem(0, _currentUnv.name, _currentUnv,null,null,null,_p.getIconAlt(_p._QUALIF_IMG,0))
objTree    = newTreeWidget('objTree',200,300,_img+'qualificationIcons.gif',clickCB,dclickCB)
unvRootHrc = newTreeWidgetElem(0, _currentUnvHrc.name, _currentUnvHrc,null,null,null,_p.getIconAlt(_p._QUALIF_IMG,0))
hrcTree    = newTreeWidget('hrcTree',200,300,_img+'qualificationIcons.gif',clickCB,dclickCB)
objTree.setDragDrop(treeDragCB,treeAcceptDropCB,treeDropCB)
hrcTree.setDragDrop(treeDragCB,treeAcceptDropCB,treeDropCB)
objTree.setMultiSelection(true);
hrcTree.setMultiSelection(true);
objTree.setTooltipOnMouseOver(true)
qpTooltip=newUnvObjTreeTooltipWidget(300,50,true,5000)
objTree.customTooltip=qpTooltip
if(_p._unvDisplayMode=="disObj")
{
objTree.add(unvRootObj)
buildTree(_currentUnv.root,unvRootObj,0);
}
else
{
hrcTree.add(unvRootHrc)
buildTree(_currentUnvHrc.root,unvRootHrc,0);
}
filtersZone = newFilterZoneWidget('QueryFilterZone','Query Filters',null,null,true,changeCB,_ie?'To filter the query, select predefined filters or objects on the Universe pane and add them here by clicking the arrow or using drag-and-drop. Select Filter to specify the values you want returned to reports or select Prompt to define a message so users can select values of their choice.':'To filter the query, select predefined filters or objects on the Universe pane and add them here by clicking the arrow. Select Filter to specify the values you want returned to reports or select Prompt to define a message so users can select values of their choice.')
filtersList= filtersZone.getList()
filtersList.setDragDrop(treeDragCB,treeAcceptDropCB,treeDropCB)
horigrab=newGrabberWidget("horigrab",grabCB,HgrabX,marge,5,300,true)
vertgrab=newGrabberWidget("vertgrab",grabCB,HgrabX,VgrabY,300,5,false)
horigrab.setMinMax(100)
vertgrab.setMinMax(130)
transpgrab=newGrabberWidget("transpgrab",grabCB,marge,marge,1,300,true)
}
function wr(s)
{
document.write(s)
}
function writeEmptyBody(s,reason)
{
var msg;
if(reason == 1)
msg="This data provider cannot be displayed because it contains combined queries";
else if(reason == 2)
msg="This data provider cannot be displayed because it contains a personal data provider";
else if(reason == 3) 
msg="This data provider cannot be displayed because the query based on this universe cannot be modified";
else 
msg=s?s:"";
wr('<body style="cursor:default;overflow:hidden" class="dialogzone" onselectstart="return false" ondragstart="return false" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">')
wr('<table width="100%" height="100%" class="dialogzonebold"><tr><td align="center"><i>'+msg+'</i></td></tr></table>')
wr('<form target="DlgFrame" style="display:none" name="submitForm" method="post" action="">')
wr('<input type="hidden" name="WOMqueries" id="WOMqueries">')
wr('</form>')
wr('</body>')
}
function writeBody()
{
wr('<body style="cursor:default;overflow:hidden" class="dialogzone"  onselectstart="return false" ondragstart="return false" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">')
wr('<div id="universeContainer" style="display:block;position:absolute;left:-1000px;top:100px;width:15px;height:15px">')
universeZone.begin()
wr('<table width="100%" cellspacing="0" cellpadding="0" border="0" >')
wr('<tr>')
wr('<td style="padding-bottom:5px;padding-left:5px;overflow:hidden" class="dialogzonebold">')
wr('Universe')
wr('</td>')
wr('</tr>')
wr('<tr>')
wr('<td style="padding-bottom:5px;padding-left:5px">')
objectSearchZone.write()
wr('</td>')
wr('</tr>')
wr('<tr>')
wr('<td style="padding-bottom:5px;padding-left:5px">')
objTree.write()
hrcTree.write()
wr('</td>')
wr('</tr>')
wr('<tr>')
wr('<td style="padding-left:5px"><div id="disObjDiv" style="overflow:hidden;text-overflow:ellipsis">')
disObjRadio.write()+disHrcRadio.write();
wr('</div></td>')
wr('</tr>')
wr('</table>')
universeZone.end()
wr('</div>')
wr('<div id="queryContainer" style="display:block;position:absolute;left:-1000px;top:100px;width:15px;height:15px">')
wr('<table width="100%" border="0" cellspacing="0" cellpadding="0">')
wr('<tr>')
wr('<td width="50" align="center" valign="top">')
writeSpace(50,28)
addObjButton.write()
delObjButton.write()
wr('</td>')
wr('<td>')
queryZone.begin()
wr('<table class="dialogzone" border="0" cellspacing="0" cellpadding="0">')
wr('<tr>')
wr('<td style="padding-bottom:5px;padding-left:3px" class="dialogzonebold" align="left" ><nobr>')
wr('Result Objects')
wr('</nobr></td>')
wr('<td id="keydateInfo" style="padding-bottom:5px;padding-left:3px;" class="dialogzonebold" align="right"><nobr>')
wr('<span style="text-overflow:ellipsis;overflow:hidden"></span>')
wr('</nobr></td>')
wr('</tr>')
wr('<tr>')
wr('<td style="padding-bottom:5px;padding-left:3px;padding-right:3px" colspan="2">')
queryListContainer.write()
wr('</td>')
wr('</tr>')
wr('</table>')
queryZone.end()
wr('</td>')
wr('</tr>')
wr('</table>')
wr('</div>')
wr('<div id="filterContainer" style="display:block;position:absolute;left:-1000px;top:100px;width:15px;height:15px">')
wr('<table width="100%" border="0" cellspacing="0" cellpadding="0">')
wr('<tr>')
wr('<td width="50" align="center" valign="top">')
writeSpace(50,28)
addFltButton.write()
delFltButton.write()
wr('</td>')
wr('<td>')
filtersZone.write()
wr('</td>')
wr('</tr>')
wr('</table>')
wr('</div>')
horigrab.write()
vertgrab.write()
transpgrab.write()
wr('<iframe style="position:absolute;left:-100px;top:-100px;width:10px;height:10px;" name="queryDlgFrame" frameborder="0" src="lib/empty.html"></iframe>')
wr('<form target="DlgFrame" style="display:none" name="submitForm" method="post" action="">')
wr('<input type="hidden" name="WOMqueries" id="WOMqueries">')
wr('</form>')
wr('</body>')
}
function loadEmptyDPCB()
{
_isPageLoaded= true 
_allowDPViewSQL=false;
_allowDPProperties=false;
updateQPToolbar()
setTimeout("_p.hideWaitDlg()",100)
}
function loadCB()
{
    _isPageLoaded= true 
universeContainer.init()
queryContainer.init()
filterContainer.init()
universeZone.init()
queryZone.init()
addObjButton.init()
delObjButton.init()
addFltButton.init()
delFltButton.init()
disObjRadio.init()
disHrcRadio.init()
disObjRadio.check(!_p._unvDisplayMode || _p._unvDisplayMode == "disObj")
disHrcRadio.check(_p._unvDisplayMode == "disHrc")
disObjDiv.init()
disKeyDateSpan.init()
objTree.init()
hrcTree.init()
objectSearchZone.init();
queryListContainer.init()
filtersZone.init()
filtersZone.buildFilters(_currentDP.filtersTree)
filtersZone.updateBOListIcons()
horigrab.init()
vertgrab.init()
transpgrab.init()
qpTooltip.init()
fillWidgets()
updateQPToolbar()
resizeCB()
setTimeout("_p.hideWaitDlg()",100)
if(!_currentDP.alreadyLoaded && _currentDP.isCustomSQL)
{
_p.showAlertDialog("The query contains custom SQL. If you modify it you will lose the custom SQL.",null,1);
}
if (_moz)  
    window.onresize=delayResize; 
else
   window.document.body.onresize=delayResize;
}
function grabCB(x,y)
{
if (this.id=="horigrab")
{
HgrabX=x
_p.recordProp("CDZ_QP_universePaneW",HgrabX)
_p._unvPaneWidth = HgrabX 
}
if (this.id=="vertgrab")
{
VgrabY=y
_p.recordProp("CDZ_QP_queryPaneH",VgrabY)
_p._queryPaneHeight=VgrabY 
}
resizeCB()
}
function delayResize()
{   
    setTimeout('resizeCB()',1);
}
function resizeCB()
{
if (_isPageLoaded && !stopLoadingDP)
{
var w=winWidth(),h=winHeight()
if (w<=0 || h<=0) return;
if(HgrabX > w)HgrabX =w-marge
if(VgrabY > h)VgrabY =h-marge
var uzw, rzw, qzh, fzh, uzh
uzh=h-marge*2
uzw=HgrabX-marge*2
qzh=VgrabY-marge
rzw=w-HgrabX-marge*3-buttonsw 
universeContainer.move(marge,marge)
disObjDiv.resize(Math.max(0,uzw-marge*2),null)
if(_p._unvDisplayMode=="disObj")
{
objTree.setDisplay(true)
objTree.resize(uzw-marge*2,Math.max(0,h-120))
hrcTree.setDisplay(false)
hrcTree.move(-100,-100)
}
else
{
hrcTree.setDisplay(true)
hrcTree.resize(uzw-marge*2,Math.max(0,h-120))
objTree.setDisplay(false)
objTree.move(-100,-100)
}
objectSearchZone.resize(Math.max(0,uzw-marge*2),null)
universeZone.resize(uzw,Math.max(0,uzh))
if(VgrabY < qzhmin+ marge )VgrabY = qzhmin + marge
fzh=h-VgrabY-marge*2
resizeFilterZone(rzw,fzh)
resizeQueryZone(rzw,qzh)
horigrab.move(HgrabX,null)
horigrab.resize(null,uzh)
vertgrab.move(HgrabX+marge+buttonsw,VgrabY)
vertgrab.resize(Math.max(0,queryZone.getWidth()),null)
transpgrab.show(false)    
}
}
function resizeFilterZone(rzw,fzh)
{
filterContainer.setDisplay(true)
filterContainer.move(HgrabX+marge,Math.max(0,VgrabY+marge))
filtersZone.resize(rzw,fzh)
}
function resizeQueryZone(rzw,qzh)
{
queryContainer.move(HgrabX+marge,marge)
queryListContainer.resize(Math.max(rzwmin,rzw-marge*2),qzh?Math.max(30,qzh-33):null)
queryZone.resize(Math.max(rzwmin,rzw),qzh?Math.max(qzhmin,qzh):null)
}
function fillWidgets()
{
var objs=_currentDP.objs,len=objs.length
for (var k=0;k<len;k++)
{
var obj=objs[k];
queryList.add(obj.name,obj.kind,obj.id,obj.name,-1)
}
if (len>0) queryList.select(0)
changeObjSelCB();
fillKeydate()
}
function fillKeydate(newKeydate)
{
var keydateTooltip="";
if(newKeydate!=null)
{
_currentDP.keydateValue = newKeydate;
}
if(_currentDP.keydateValue !=null)
{
keydateTooltip=_currentDP.keydateValue[5]+' :  '
if(!_currentDP.keydateValue[1])
{
if(_currentDP.keydateValue[2]!=_p._keydateLastAvailable)
keydateTooltip+=formatSerializedDate(_currentDP.keydateValue[2])+' (default)';
else
keydateTooltip+=_p._lastAvailableLabel;
}
else
{
keydateTooltip+=formatSerializedDate(_currentDP.keydateValue[3]);
}
}
var cNodes=disKeyDateSpan.layer.childNodes,cLen=cNodes.length;
if(cLen>0)
{ 
cNodes[0].innerHTML=keydateTooltip;
cNodes[0].title=keydateTooltip;
}
}
function isFilter(elt)
{
var sub=elt.sub,len=sub.length
if ((len==0)&&(elt.kind==_p._fil)) return true
if (elt.kind==_p._cls)
{
for (var i=0;i<len;i++)
if (!isFilter(sub[i])) return false
return true
}
return false
}
function buildTree(elt, treeElt,level)
{
for (var i in elt.sub)
{
var subElt=elt.sub[i]
{
var newTreeElt=newTreeWidgetElem(subElt.kind, subElt.name, subElt,subElt.desc, subElt.kind==1?9:null,null,_p.getIconAlt(_p._QUALIF_IMG,subElt.kind))
if (!_expanded)
{
if (level==0) _expanded=true
newTreeElt.expanded=(level<1)
}
treeElt.add(newTreeElt);
if (subElt.sub.length > 0) buildTree(subElt, newTreeElt,level+1)
}
}
}
function getSelUnvObjects()
{
var arr = ((_p._unvDisplayMode=="disObj")? objTree.getSelections():hrcTree.getSelections()),len=arr.length;
_currentObjects.length=0;
for(var i=0;i<len;i++)
{
_currentObjects[i]=arr[i].userData;
}
}
function clickCB(data)
{
getSelUnvObjects();
_currentObj=data
updateAddButtons();
}
function dclickCB(data)
{
getSelUnvObjects();
_currentObj=data
addCurrent(false)
}
function updateDescZone()
{
var s='<b>'+convStr(_currentObj.name)+'</b>';
if(_currentObj.desc) 
s+='<br>'+convStr(_currentObj.desc)
}
function updateAddButtons()
{  
    addObjButton.setDisabled(!canAddToResult());
    addFltButton.setDisabled(!canAddToFilter());   
}
function canAddToResult()
{
    var len = _currentObjects.length 
    var bInResult=false
    for(var i=0;i<len;i++)
    {
        var obj = _currentObjects[i]        
        if(obj.allowInResult &&  ((obj.kind != _topfs._unv)&& (obj.kind!= _topfs._hchy) && (obj.kind!=_topfs._fil))) 
        {
            bInResult=true;
            break;
        }
    }      
    return bInResult;
}
function canAddToFilter()
{   
    var len = _currentObjects.length
    var bInFilter=false
    for(var i=0;i<len;i++)
    {
        var obj = _currentObjects[i]
        if(obj.allowInFilter &&  ((obj.kind != _topfs._unv)&& (obj.kind!= _topfs._cls) && (obj.kind!= _topfs._hchy))) 
        {
            bInFilter=true;
            break;
         }
    }   
    return bInFilter; 
}
function getTooltipMessage(panel)
{
    var s="";
    var len = _currentObjects.length       
    if(panel=="result")
    {
        s='You cannot use this object in the Result pane.'
        for(var i=0;i<len;i++)
        {        
            if(_currentObjects[i].allowInResult)             
                s="";break;                                      
         }       
    }
    else if(panel=="filter")
    {
        s='You cannot use this object in the Filter pane.'
        for(var i=0;i<len;i++)
        {        
            if(_currentObjects[i].allowInFilter)             
                s="";break;            
         }
    }
    return s;
}
function treeDragCB(source) {}
function treeAcceptDropCB(source,target,ctrl,shift)
{
    var tooltip=''
    var bAccept=false
if ((target.id==queryList.id)&&(source&&(source.id=='objTree' || source.id=='hrcTree')))
{
    if(canAddToResult())
    bAccept=true
    else    
        tooltip=getTooltipMessage('result');
}
if ((target.id=='objTree' || target.id=='hrcTree')&&(source&&(source.id==queryList.id)))
bAccept=true
if ((target.id==queryList.id)&&(source&&(source.id==queryList.id)))
{
if (ctrl)
bAccept=false
else
bAccept=true
}
if ((target.id==filtersList.id)&&(source&&(source.id=='objTree' || source.id=='hrcTree')) )
{
    if(canAddToFilter())
    bAccept=true
else
   tooltip=getTooltipMessage('filter');
    }
if ((target.id=='objTree' || target.id=='hrcTree')&&(source&&(source.id==filtersList.id)))
bAccept=true
if ((target.id==filtersList.id)&&(source&&(source.id==filtersList.id)))
bAccept=true
if ((target.id==filtersList.id)&&(source&&(source.id==queryList.id)))
{
    var obj=null,objIdx=_currentDP.findObj(source.selection.value)
    if(objIdx>-1) 
        obj=_currentDP.objs[objIdx];
    if (obj && !obj.allowInFilter)
    tooltip='You cannot use this object in the Filter pane.'
else 
    bAccept=true     
    }
    if(source && (_prevTooltip!=tooltip))
    {
        source.setDDTooltip(source,tooltip?tooltip:null);        
        _prevTooltip = tooltip;
    }
return bAccept
}
function treeDropCB(source,target,ctrl,shift)
{
    _prevTooltip = '';
if ((target.id==queryList.id)&&(source&&(source.id=='objTree' || source.id=='hrcTree')))
{
var lenObjs = _currentObjects.length;
if(lenObjs == 0) return;
for(var i=0;i<lenObjs;i++)
{
_currentObj = _currentObjects[i];
var idx=target.dropIndex;
if (idx<0)
idx=target.getLength()
if (_currentObj.kind==_p._cls )
AddClassObj(idx);
else
{
if (_currentObj.kind==_p._dtl )
{
AddDetailParentObj(idx)
idx++
}
AddQueryObj(idx);
}
}
}
if ((target.id=='objTree' || target.id=='hrcTree')&&(source&&(source.id==queryList.id)))
removeFromButton()
if ((target.id==queryList.id)&&(source&&(source.id==queryList.id)))
{
var sourceItem=source.selection
var val=sourceItem.value
var idx=target.dropIndex
var targetItem = idx>=0?target.getItem(idx):null
if (shift)
{
val=sourceItem.value
_currentObj=_currentUnv.getById(val)
AddQueryObj(idx, true)
var sourceIdx = sourceItem?sourceItem.getIndexInParent():-1
val=targetItem.value
_currentObj=_currentUnv.getById(val)
AddQueryObj(sourceIdx, true)
sourceItem.select(true)
delCurrent()
targetItem.select(true)
delCurrent()
target.select(idx)
}
else
{
delCurrent()
_currentObj=_currentUnv.getById(val)
AddQueryObj(targetItem?targetItem.getIndexInParent():-1)
}
}
if ((target.id==filtersList.id)&&(source&&(source.id=='objTree' || source.id=='hrcTree')))
{
var targetItem = target.dropWidget
var index      = target.dropIndex
var lenObjs = _currentObjects.length;
if(lenObjs == 0) return;
addCurrentFilters(filtersZone,_currentObjects,targetItem,index)
}
if ((target.id=='objTree' || target.id=='hrcTree')&&(source&&(source.id==filtersList.id)))
removeFilterFromButton()
if ((target.id==filtersList.id)&&(source&&(source.id==filtersList.id)))
{
var sourceItem = source.selection
var targetItem = target.dropWidget
var index      = target.dropIndex
if(ctrl) 
{
filtersZone.duplicateFilter(sourceItem,targetItem,index)
}
else if (shift) 
{
filtersZone.switchFilter(sourceItem,targetItem,index)
}
else 
{
filtersZone.moveFilter(sourceItem,targetItem,index)
}
}
if ((target.id==filtersList.id)&&(source&&(source.id==queryList.id)))
{
var targetItem = target.dropWidget
var index      = target.dropIndex
var sourceItem=source.selection
var arrObjs=new Array(_currentUnv.getById(sourceItem.value));
addCurrentFilters(filtersZone,arrObjs,targetItem,index)
}
}
function searchObjectNotFoundCB()
{
_p.showAlertDialog("Not Found",null,0);
}
function searchObjectCB()
{
var text = objectSearchZone.getSearchValue();
var matchCase = objectSearchZone.isCaseSensitive();
if(_p._unvDisplayMode=="disObj")
{
objTree.search(text,matchCase,null,null,true,searchObjectNotFoundCB);
}
else
{
hrcTree.search(text,matchCase,null,null,true,searchObjectNotFoundCB);
}
}
function changeUnvDisplayMode()
{
var unvDisplayMode = disObjRadio.isChecked()?"disObj":"disHrc"
if(unvDisplayMode=="disObj" && objTree.sub.length==0)
{
_expanded=false
objTree.add(unvRootObj)
buildTree(_currentUnv.root,unvRootObj,0);
objTree.rebuildHTML()
}
if(unvDisplayMode=="disHrc" && hrcTree.sub.length==0)
{
_expanded=false;
hrcTree.add(unvRootHrc)
buildTree(_currentUnvHrc.root,unvRootHrc,0);
hrcTree.rebuildHTML()
}
_p.recordProp("CDZ_QP_universeSel",unvDisplayMode)
_p._unvDisplayMode = unvDisplayMode 
resizeCB();
}
function setProviderModified()
{
_currentDP.setModified();
_p.updateIsQPModified(true);
}
function addCurrent(addClass)
{
var lenObjs = _currentObjects.length;
if(lenObjs == 0) return;
for(var i=0;i<lenObjs;i++)
{
_currentObj = _currentObjects[i];
if (_currentObj.kind==_p._fil )
addCurrentFilter(filtersZone,_currentObj)
if (_currentObj.kind==_p._cls )
{
if (addClass)
AddClassObj();
}
else
{
if (_currentObj.kind==_p._dtl )
AddDetailParentObj()
AddQueryObj();
}
}
}
function AddClassObj(idx)
{
var oldObj=_currentObj,sub=_currentObj.sub,count=sub.length;
var theIdx=idx
for (var i=0;i<count;i++)
{
if(idx==null)theIdx=-1
_currentObj=sub[i]
if (AddQueryObj(theIdx)) theIdx++;
if (_currentObj.kind==_p._dim && _currentObj.sub.length>0)
theIdx = AddClassObj((idx==null? null: theIdx))
}
_currentObj=oldObj;
return theIdx;
}
function AddDetailParentObj(idx)
{
_curDetailObj=_currentObj
var dim = _currentObj.par
if (_currentDP.findObj(dim.id)==-1)
{
_currentObj=dim
AddQueryObj(idx)
_currentObj=_curDetailObj
}
}
function AddQueryObj(idx, bforce)
{
var bAdd=false
if ((_currentObj.isResultObj!=null)&&(_currentObj.isResultObj()))
{
if ((_currentDP.findObj(_currentObj.id)==-1)||bforce)
{
_currentDP.addObj(_currentObj,idx)
queryList.add(_currentObj.name,_currentObj.kind,_currentObj.id,_currentObj.name,idx)
queryList.select(idx==null?-1:idx);
setProviderModified();
bAdd=true
}
}
if(bAdd) changeObjSelCB();
return bAdd
}
function queryMoveCB(elem,node,idx)
{
var sourceItem=elem
var oldCurr=_currentObj
var val=sourceItem.value
var targetItem = idx>=0?node.getItem(idx):null
delCurrent()
_currentObj=_currentUnv.getById(val)
AddQueryObj(targetItem?targetItem.getIndexInParent():-1)
_currentObj=oldCurr
return false
}
function delCurrent()
{
var sel=queryList.getSelection()
if (sel)
{
var idx=sel.index
queryList.remove(idx)
_currentDP.remObj(idx)
queryList.select(Math.min(idx,queryList.getLength()-1))
setProviderModified()
}
}
function removeFromButton()
{
setTimeout('delCurrent()',1)
setTimeout('changeObjSelCB();selectDSFromResultObject()',2)
}
function addFromButton()
{
setTimeout('addCurrent(true)',1)
}
function removeAllObjectsCB()
{
    _topfs.showPromptDialog("Are you sure you want to delete all objects from the result panel?",null,_topfs._promptDlgWarning,doRemoveAllObjectsCB,null)   
}
function doRemoveAllObjectsCB()
{
    var len=queryList.items.length;
    if(len >0)
    {
        queryList.remove(null)
        _currentDP.remObj(null) 
        setProviderModified()
        changeObjSelCB()
    }
}
function addQuickFilter()
{
var sel=queryList.getSelection()
if (sel)
{
_resultObj =_currentUnv.getById(sel.value)
if (_resultObj && _resultObj.hasLov && (_topfs.isEnableUserRight(_topfs._usrUseLOV)))
{
setTimeout('addCurrentFilter(filtersZone,_resultObj,null,null,true)',1);
}
}
}
function removeQuickFilter()
{
delCurrentFilter( filtersZone);
}
function queryChangeCB()
{
    changeObjSelCB()
    selectDSFromResultObject()
}
function changeObjSelCB()
{
var sel=queryList.getSelection()
if(sel)
{
_resultObj =_currentUnv.getById(sel.value)
var qfBtn = queryListContainer.quickFilter
if(qfBtn)
{
if (_resultObj && _resultObj.hasLov && (_topfs.isEnableUserRight(_topfs._usrUseLOV)) && _resultObj.allowInFilter)
qfBtn.setDisabled(false)
else
qfBtn.setDisabled(true)
}
if(sel.index == 0)
queryListContainer.up.setDisabled(true)
else
queryListContainer.up.setDisabled(false)
if(sel.index == queryList.items.length-1)
queryListContainer.down.setDisabled(true)
else
queryListContainer.down.setDisabled(false)
if(queryListContainer.removeAll) 
    queryListContainer.removeAll.setDisabled(false)
}
else
{
queryListContainer.up.setDisabled(true)
queryListContainer.down.setDisabled(true)
if(queryListContainer.quickFilter) 
queryListContainer.quickFilter.setDisabled(true)
if(queryListContainer.removeAll) 
queryListContainer.removeAll.setDisabled(true)
}
}
function selectDSFromResultObject()
{
    var sel=queryList.getSelection()
    if(sel)
    {
        var ds =_currentUnv.getById(sel.value)
        if(_p._unvDisplayMode=="disObj" && ds)     
        {  
            objTree.unselect() 
            objTree.selectByData(ds,false)
        }
        else 
        {
            ds =_currentUnvHrc.getById(sel.value)
            if(_resultObj)
            {
                hrcTree.unselect()
                hrcTree.selectByData(ds,false)
            }
        }
    } 
}
function addFilterFromButton()
{
setTimeout('addCurrentFilters(filtersZone,_currentObjects)',1);
}
function removeFilterFromButton()
{
setTimeout('delCurrentFilter(filtersZone)',1);
}
function changeCB(action, flt)
{
setProviderModified()
recordPrompts(action, flt)
}
function recordPrompts(action,flt)
{
if(action == null || flt == null) return;
if(action=='delete')
{
if(flt.isNode())
{
cleanDPPrompt();
}
else
{
removePrompt(flt)
}
}
else if(action=='add')
{
addPrompt(flt)
}
else if(action=='modify')
{
addPrompt(flt);
cleanDPPrompt();
}
}
function addPrompt(flt)
{
var opCount= flt.getOperandCount();
for(var i= 0; i<opCount; i++)
{
var fPart= flt.getOperandPart(i);
if(fPart.type == _topfs._filterPrompt)
{
if(findIndexFromValue(fPart.operand)==-1)
_currentDP.addPrompt(fPart.operand);
}
}
}
function removePrompt(flt)
{
var opCount= flt.getOperandCount();
for(var i= 0; i<opCount; i++)
{
var fPart= flt.getOperandPart(i);
if(fPart.type == _topfs._filterPrompt)
{
cleanDPPrompt(fPart.operand);
}
}
}
function findIndexFromValue(v)
{
var len = _currentDP.prompts.length
for(var i=0; i<len; i++)
{
if(_currentDP.prompts[i] == v)
return i
}
return -1
}
function cleanDPPrompt(q)
{
if(_currentDP.filtersTree == null) return;
var root=_currentDP.filtersTree;
if(q!=null && q!="") 
{
if(root.findFromQuestion(q) == null)
arrayRemove(_currentDP,'prompts',findIndexFromValue(q)); 
}
else
{
var len = _currentDP.prompts.length
for(var i=len-1; i>-1; i--)
{
if(root.findFromQuestion(_currentDP.prompts[i]) == null)
arrayRemove(_currentDP,'prompts',i); 
}
}
}
var dpList = _p._queries
var dpCount =dpList.length
function hasEmptyDP(isRunQuery)
{
var ret=false;
if(isRunQuery)
{
for(var i=0;i<dpCount;i++)
{
var dp=dpList[i];
ret =(dp.objs.length>0)?false:true;
}
}
else if(_currentDP)
{
ret =(_currentDP.objs.length>0)?false:true;
}
return ret;
}
function encodeCurrentQuery()
{
var dp =_currentDP;
if(dp && dp.isModified)
return '['+enc(encString(dp.name),encodeQuery(dp),encodeQueryAttr(dp),encodeFilters(dp.filtersTree),encodePromptOrder(null))+']'
else
return "[]"
}
function encodeAllQueries()
{
var sWOMqueries='['
for(var i=0;i<dpCount;i++)
{
var dp=dpList[i]
if(dp.isModified)
{
if(i>0 && sWOMqueries!='[') sWOMqueries+=','
var prompts = null
if(mustSendPromptOrder(dp.filtersTree,dp.prompts))
prompts = dp.prompts
sWOMqueries+=enc(encString(dp.name),encodeQuery(dp),encodeQueryAttr(dp),encodeFilters(dp.filtersTree),encodePromptOrder(prompts))
}
}
return sWOMqueries+=']'
}
function encodeQuery(prov)
{
var s='[',objslen=prov.objs.length
for (var i=0;i<objslen;i++)
{
if (i>0) s+=','
s+=encString(prov.objs[i].id)
}
s+="]"
return s
}
function encodeQueryAttr(prov)
{
var maxRows=prov.haveMaxRows?''+prov.maxRows:'-1'
var maxTime=prov.haveMaxTime?''+prov.maxTime:'-1'
var samplingSize=(prov.samplingMode!="none")?''+prov.samplingSize:'-1'
return enc(maxRows,maxTime,prov.duplicateRows,prov.scope,prov.refreshContexts,samplingSize,prov.samplingMode)
}
function encodePromptOrder(prompts)
{
if(!prompts) return '[]';
var s='[',len = prompts.length
for (var i=0;i<len;i++)
{
if (i>0) s+=','
s+=encString(prompts[i])
}
s+=']'
return s
}
function mustSendPromptOrder(filterRoot, promptOrder)
{
var arrPrompts = new Array()
fillPromptList(filterRoot, arrPrompts)
var lenPrompt = arrPrompts.length, lenPOrder = promptOrder.length
if(lenPrompt != lenPOrder) return true;
for(var i=0; i<lenPOrder ; i++)
{
if(arrPrompts[i]!=promptOrder[i])
return true;
}
return false;
}
function fillPromptList(node,arr)
{
var items = node.items,len = items.length
for (var i=0;i<len ;i++) 
{ 
var item = items[i]
if (item.kind==_p._filterPrompt)
{
arr[arr.length]=item.operands[0]
if(item.getOperandCount()== 2)
arr[arr.length]=item.operands[1]
}
if(item.isNode())
fillPromptList(item,arr)
} 
}
function submitQueries(action,idx)
{
if (hasEmptyDP((action != "viewsql")))
{
_p.showAlertDialog("At least one query in this document is empty. Check that the Result Objects pane on each query tab includes objects before running the queries. (Error: WIJ 30001)","Run Query",2)
return ;
}
if (action == "runquery")
{
document.submitForm.WOMqueries.value = encodeAllQueries();
_p.eventManager.notify(_p._EVT_DOC_UPDATE)
_p.launchRefreshWaitDlg('Retrieving Data',true,false,false);
}
else if (action == "generatequery")
{
document.submitForm.WOMqueries.value = encodeAllQueries();
_p.eventManager.notify(_p._EVT_DOC_UPDATE)
_p.wt();
}
else if ((action == "savequery") || (action == "savequeryas"))
{
document.submitForm.WOMqueries.value = encodeAllQueries();
_p.eventManager.notify(_p._EVT_DOC_UPDATE)
}
else if (action == "viewsql")
{
document.submitForm.WOMqueries.value = encodeCurrentQuery();
_p.wt();
}
var url=_root
var p = _p.urlParams(true, true);
if (action == "savequeryas")
{
url += "processSaveQuery"+_appExt+p;
url += "&sEditQuery="+_p.allUsersEditQueries 
if(_topfs.bLaunchQP) {
url += "&sNewDoc=true"
url += "&defaultRepTitle="+convURL(_p._defaultRepTitle)
}
}
else if (action == "savequery")
{
url += "processSaveQuery"+_appExt+p;
url += "&sEditQuery="+_p.allUsersEditQueries 
if (_topfs.isNew) {
url += "&sNewDoc=true"
url += "&defaultRepTitle="+convURL(_p._defaultRepTitle)
} 
if (_p.strDocName != _p._defaultDocName) {
url += "&quickSave=true"
}
}
else if (action == "runquery")
{
url += "processRunQuery"+_appExt+p;
url += "&sEditQuery="+_p.allUsersEditQueries 
if(_topfs.isNew) {
url += "&sNewDoc=true"
url += "&defaultRepTitle="+convURL(_p._defaultRepTitle)
}
if(idx!=null)
{
url += "&iDPIndex="+idx
}
_p._runQueryIdx = idx;
}
else if (action == "generatequery")
{
url += "processRunQuery"+_appExt+p;
url += "&sEditQuery="+_p.allUsersEditQueries 
url += "&bNoData=true";
if(_topfs.isNew) {
url += "&sNewDoc=true"
url += "&defaultRepTitle="+convURL(_p._defaultRepTitle)
}
_p._bRetrieveData=false;
}
else if (action == "viewsql")
{
url += "getSQL"+_appExt+p+"&sDPName="+_p.convURL(_p.getSelectedDPName());
}
url +="&bCreateDefaultReportBody="+_topfs.bCreateDefaultReportBody
document.submitForm.action = url;
setTimeout("document.submitForm.submit()",1);
}
function hideContainers()
{
if(!stopLoadingDP)
{
universeContainer.move(-1000,0)
queryContainer.move(-1000,0)
filterContainer.move(-1000,0)
}
}
function changeSQLIcon()
{
if(_allowDPViewSQL && _currentDP)
{
if( _currentDP.isCustomSQL)
_p.viewSQLIcon.changeImg(16*1,0,_img+'sqlicons.gif');
else
_p.viewSQLIcon.changeImg(16*0,0,_img+'sqlicons.gif');
}
}
function updateQPToolbar()
{
changeSQLIcon();
var bViewSQL = (_p.isEnableUserRight(_p._usrViewSQL) && _allowDPViewSQL) ;
_p.viewSQLIcon.setDisplay(bViewSQL)
_p.querySep1.setDisplay(bViewSQL)
_p.queryPropIcon.setDisplay(_allowDPProperties)
_p.querySep2.setDisplay(_allowDPProperties)
_p.updateRunQueryIcn();
_p.pal5.disableChildren(false);
_p.updatePurgeIcn()
_p.updateIsQPModified(_p._isQPModified);
var hasKeydate=false;
for(var i=0;i<_p.arrDPs.length;i++)
{
if(_p.arrDPs[i].hasKeydate)
{
hasKeydate=true;
break;
}
}
_p.keyDateIcon.setDisplay(hasKeydate)
_p.querySep4.setDisplay(hasKeydate)
}
function updateKeyDateUI(newEntry)
{
if(newEntry)
_p.changeEntry(newEntry);
fillKeydate(_currentKeydate);
}
function getCurrentFilter()
{
return filtersZone.getCurrentFilter()
}
function getCurrentFilterWidget(AndOrFilterID)
{
return filtersZone.getCurrentFilterWidget(AndOrFilterID)
}
function formatSerializedDate(s)
{
var dt=new Date();
dt.setDate(1);
var arr=s.split(',');
dt.setYear(arr[2]);
dt.setMonth(arr[0]-1);
dt.setDate(arr[1]);
return formatDate(dt, _p._inputFormatDate);
}
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
*/preloadImg(_img+"resizepattern.gif")
bidTable=new Array
selectedBid=new Array
applyBid= null
_targetCopyBid = null
_formatPainterBid = null
_isSingleActionFormatPainter=true
_setFocusToViewer=false
globResizeObj=null
oldSelCell=null
_minResWidth=4
_minResHeight=4
_fcDlgSelectedPane=0 
_ftDlgSelectedPane=0 
_fchartDlgSelectedPane=0  
_fchartDlgSelectedAxis='c'  
_fsectDlgSelectedPane=0 
_frDlgSelectedPane=0 
_sortDlgSelectedPane=null
_breakBlockBID=null
_brkCurTab=null
_docSummaryBranchExp = true
_docPropBranchExp = true
_promptsBranchExp = true
_alertersBranchExp = true
_queriesBranch = true
_arrQueriesBranch = new Array
_objectsBranch = true
_arrObjectsBranch = new Array
_varsDocBranchExp = false
_formulaeDocBranchExp = false
_elemDDBranchExp = true
_tableElemsBranchExp = true
_chartElemsBranchExp = true
_arrChartElemsBranchExp = new Array(false,false,false,false,false)
_cellsElemBranchExp = true
_formCellsElemBranchExp = true
_pnCellsElemBranchExp = false
_resultObjsBranchExp = true
_varsBranchExp = false
_formulaeBranchExp = true
editFilterID=null
editFilterReload=false
_currentFilter=null
_isQPModified=false;
_formatDlgBackgroundImg=new Array;
querypanelBorder=newWidget("querypanelBorder");
querypanelWidget=newWidget("querypanel");
_cellNone=0
_cellRight=1
_cellBottom=2
_cellContent=3
_cellTop=4
_cellLeft=5
BD_NONE = 0
BD_THIN = 1
BD_MEDIUM = 2
BD_THICK = 3
AL_LEFT = 0
AL_CENTER = 1
AL_RIGHT = 2
AL_TOP = 0
AL_BOTTOM = 2
_currColorCombo=null
_currColorCB=null
_labFil=new Array;
_labFil[EQUAL]="Equal to";
_labFil[NOT_EQUAL]="Not Equal to";
_labFil[GREATER]="Greater than";
_labFil[GREATER_OR_EQUAL]="Greater than or Equal to";
_labFil[LESS]="Less than";
_labFil[LESS_OR_EQUAL]="Less than or Equal to";
_labFil[BETWEEN]="Between";
_labFil[NOT_BETWEEN]="Not Between";
_labFil[IN_LIST]="In List";
_labFil[NOT_IN_LIST]="Not In List"
_labFil[IS_NULL]="Is Null"
_labFil[NOT_IS_NULL]="Is not Null"
_labFil[LIKE]="Matches pattern"
_labFil[NOT_LIKE]="Different from pattern"
_labFil[BOTH]="Both"
_labFil[EXCEPT]="Except"
_labOperand=new Array;
_labOperand[AND]="And";
_labOperand[OR]="Or";
_labOperand[BEGINPROMPT]="prompt(\'";
_labOperand[ENDPROMPT]="\')";
_labOperand[OBJECT]="Object";
_labOperand[BASEON]="Based on";
_labOperand[FOREACH]="For Each";
_labOperand[ANY]="Any";
_labOperand[ALL]="All";
_labRankTop="TOP";
_labRankBottom="BOTTOM";
_labRankPercTop="% TOP";
_labRankPercBottom="% BOTTOM";
_labDataType=new Array;
_labDataType[_date]="Date";
_labDataType[_num]="Number";
_labDataType[_txt]="Text";
function getAggregationLabel(aggFct)
{
var label="";
switch (aggFct)
{
case 'AVERAGE':
label='Average'
break
case 'COUNT':
label='Count'
break
case 'DELEGATED':
label='Database delegated'
break
case 'MAX':
label='Max'
break
case 'MIN':
label='Min'
break
case 'NONE':
label='None'
break
case 'SUM':
label='Sum'
break
}
return label;
}
_thickness = new Array("None", "Thin", "Medium", "Thick")
_attachH = new Array("None", "Left side of ...", "Right side of ...")
_attachV = new Array("None", "Top side of ...", "Bottom side of ...")
_posH = new Array('Left', 'Center', 'Right')
_posV = new Array('Top', 'Middle', 'Bottom')
_IMG_NORMAL = 0
_IMG_STRETCH = 1
_IMG_TILE = 2
_IMG_HORIZONTAL_TILE = 3
_IMG_VERTICAL_TILE = 4
_dispImg = new Array('Normal', 'Stretch (not supported in HTML)', 'Tile', 'Horizontal Tile', 'Vertical Tile')
function searchClassName(elt,cn)
{
if (elt==null)
return null
if (elt.className==cn)
return elt
return searchClassName(elt.parent,cn)
}
function getLayerByIdRef(idref, l)
{
if (l==null)
{
var rwin = getReportFrame()
if (rwin==null)
return null
else l=rwin.document.body
}
if (l==null) return null;
var currIdRef=l.getAttribute?l.getAttribute("idref"):null,n=l.childNodes
if (currIdRef==idref)
return l
if (n!=null)
{
var count=n.length
for (var i=0;i<count;i++)
{
var subL = getLayerByIdRef(idref, n[i])
if (subL)
return subL
}
}
return null;
}
function getParentLayerFromBid(l,bid,isSect)
{
if (l==null || bid==null)
return null
var currBid=l.getAttribute?l.getAttribute("bid"):null
if (currBid==bid)
{
if(isSect)
{
var firstL=l
l = getParentLayerFromBid(l.parentNode,bid, false)
if (l!=null)
return l
else firstL
}
return l
}
else
{
l = getParentLayerFromBid(l.parentNode,bid, isSect)
if (l!=null)
return l
}
return null
}
function updateParentIdRefBid(bResetIDRef,bGetSectIDRef)
{
if(_curIdRef == null) return;
bGetSectIDRef=bGetSectIDRef?bGetSectIDRef:false;
var elt=findByBID(doc,_curIdRefBid)
if (elt)
{
var cn=elt.className
if ((cn=="tableCell") || (cn=="reportCell" && elt.isSect))
{
var isSect=false
var parentBid=getTableCellParentBID(elt)
if (parentBid==null)
{
parentBid=getSectionCellParentBID(elt)
isSect=true
}
var curL=getLayerByIdRef(_curIdRef)
var containerL= getParentLayerFromBid(curL,parentBid,isSect && bGetSectIDRef)
if (containerL)
_curIdRef=containerL.getAttribute("idref")
else
_curIdRef=null
}
else if(bResetIDRef)
{
_curIdRef=null
}
}
}
function updateContainerIdRefBid(bGetSectIDRef)
{
if(_curIdRef == null) return;
bGetSectIDRef=bGetSectIDRef?bGetSectIDRef:false;
var parentBid=null,elts=getSelectedElts(),isSect=false
if (elts&&elts[0])
{
var parentElt=searchClassName(elts[0],"section")
if (parentElt==null)
parentElt=searchClassName(elts[0],"body")
else
isSect=true
if (parentElt)parentBid=parentElt.bid
}
var curL=getLayerByIdRef(_curIdRef)
var containerL= getParentLayerFromBid(curL,parentBid,isSect && bGetSectIDRef)
if (containerL)
_curIdRef=containerL.getAttribute("idref")
else
_curIdRef=null
}
function updateParentIdRefBidForFilter(filterBid)
{
var elts=getSelectedElts()
if (elts && elts[0])
{
var elt=elts[0], cn=elt.className
if(filterBid && filterBid!=elt.bid) 
return
else
{
if(cn=="body")
{
_curIdRef = getReportBodyIdRef()
}
else if (cn=="section")
{
updateContainerIdRefBid(true)
}
else
{
updateParentIdRefBid(false,true)
}
}
}
}
function getSortValue(elts)
{
if ((elts.length==1)&&(getVariableQualification()!=1))
{
var elt=elts[0],ax=getTableCellAxis(elt,false)
if (ax!=-1)
{
var ppp=elt.parent.parent.parent,axis=ppp.axis[ax]
if (isInAxis(axis,elt))
{
var srt = flatFindbyId(ppp.axis[ax].sorts, getCellFormulaOrId(elt))
return srt!=null?srt.kind:0
}
}
else
{
var sect=elt.parent
if ((elt.className=="reportCell")&&(sect.className=="section"))
{
var srt=flatFindbyId(sect.axis[0].sorts,getCellFormulaOrId(elt))
return srt!=null?srt.kind:0
}
}
}
return -1
}
function createInsertRowMenu()
{
insertRowMenu=m=newMenu("insertRowMenu",initRowMenu)
m.add("insertRowAbove","Above","menu",13)
m.add("insertRowBelow","Below","menu",14)
}
function createFilterMenu()
{
filterMenu=m=newMenu("filterMenu")
filterMenuItem = m.add("filter","Add Filter","report",0)
removeFilterMenuItem = m.add("removeFilter","Remove Filter","report",12)
}
function initRowMenu()
{
var o=this,elts=getSelectedElts()
if (elts.length==1)
{
o.insertRowAbove.setDisabled(false)
o.insertRowBelow.setDisabled(false)
}
else
{
o.insertRowAbove.setDisabled(true)
o.insertRowBelow.setDisabled(true)
}
}
function createInsertColMenu()
{
insertColMenu=m=newMenu("insertColMenu",initColMenu)
m.add("insertColLeft","Left","menu",11)
m.add("insertColRight","Right","menu",12)
}
function initColMenu()
{
var o=this,elts=getSelectedElts()
if (elts.length==1)
{
o.insertColLeft.setDisabled(false)
o.insertColRight.setDisabled(false)
}
else
{
o.insertColLeft.setDisabled(true)
o.insertColRight.setDisabled(true)
}
}
function createHyperlinkMenu()
{
hyperlinkMenu=m=newMenu("hyperlinkMenu",initHyperlinkMenu)
m.add("newHyperlink","New...")
m.addSeparator()
m.add("editHyperlink","Edit...")
m.add("removeHyperlink","Remove")
m.addSeparator()
m.addCheck("readAsHyperlink","Read content as hyperlink")
}
function initHyperlinkMenu()
{
var o=this,elts=getSelectedElts()
if (elts==null)
return
if (elts.length==1)
{
var elt=elts[0]
if ((elt.className == "tableCell") || (elt.className == "reportCell") || (elt.className == "cell"))
{
var isLink=(elt.contType==1)?true:false;
o.newHyperlink.setDisabled(isLink)
o.editHyperlink.setDisabled(!isLink)
o.removeHyperlink.setDisabled(!isLink)
o.readAsHyperlink.check(isLink)
}
}
}
function setHyperlinkMenu(elts,w)
{
if (elts==null)
return
var e=(elts.length==1)
if (e)
{
var elt=elts[0]
e=((elt.className == "tableCell") || (elt.className == "reportCell") || (elt.className == "cell"))
}
w.setDisabled(!e)
}
function showHyperlinkDialog(id)
{
var elts=getSelectedElts()
if (elts.length==1)
{
if (id=="removeHyperlink")
{
eventManager.notify(_EVT_VARS_UPDATE);
var url=_root+"processHyperlink"+_appExt
url+=urlParamsNoBID()+"&sBid="+escape(elts[0].bid)+"&sAction=R"
frameNav("Report",url,true)
}
else if (id=="readAsHyperlink")
{
var url=_root+"processHyperlink"+_appExt
url+=urlParamsNoBID()+"&sBid="+escape(elts[0].bid)+"&sAction=S&sHyperlink="+((elts[0].contType==1)?"true":"false");
frameNav("Report",url,true)
}
else
frameNav("DlgFrame","language/"+_lang+"/html/hyperlinkDialog.html")
}
}
function createFormatMenu()
{
formatMenu=m=newMenu("formatMenu",initFormatMenu)
m.add("cellFormatCell","Cell")
m.add("cellFormatAllCells","All table cells")
m.add("cellFormatTable","Table")
m.add("cellFormatChart","Chart")
m.add("cellFormatSection","Section")
m.add("cellFormatReport","Report")
}
function initFormatMenu()
{
var o=this,elts=getSelectedElts()
setFormatCellMenu(elts,o.cellFormatCell)
setFormatTableMenu(elts,o.cellFormatTable)
setFormatTableMenu(elts,o.cellFormatAllCells)
setFormatChartMenu(elts,o.cellFormatChart)
setFormatSectionMenu(elts,o.cellFormatSection)
}
function setFormatCellMenu(elts,w)
{
for (var i in elts)
{
var elt=elts[i]
if ((elt.className != "tableCell") && (elt.className != "reportCell") && (elt.className != "cell"))
{
w.show(false)
return
}
}
w.show(true)
}
function setFormatTableMenu(elts,w)
{
for (var i in elts)
{
var elt=elts[i]
if ((elt.className != "tableCell")&&(!((elt.className=="block"))))
{
w.show(false)
return
}
}
if (isMultiTableCellInSameTable(elts))
w.show(true)
else
w.show(false)
}
function setFormatChartMenu(elts,w)
{
var nCount=0
for (var i in elts)
{
var elt=elts[i]
if ((elt.className != "block")|| !(elt.block.blockType > 10))
{
w.show(false)
return
}
if (elt.block.blockType > 10)
nCount++
}
w.show(nCount==1)
}
function setFormatSectionMenu(elts,w)
{
for (var i in elts)
{
var elt=elts[i]
if (searchClassName(elt,"section")==null)
{
w.show(false)
return
}
}
w.show(true)
}
function createCellInsertMenu()
{
cellInsertMenu=m=newMenu("cellInsertMenu",initInsertMenu)
m.addSub(insertRowMenu,"insertRow","New row")
m.addSub(insertColMenu,"insertCol","New column")
}
function initInsertMenu()
{
var o=this,elts=getSelectedElts()
if (elts.length==1)
{
o.insertRow.setDisabled(false)
o.insertCol.setDisabled(false)
}
else
{
o.insertRow.setDisabled(true)
o.insertCol.setDisabled(true)
}
}
function createCellLayeringMenu()
{
cellLayeringMenu=m=newMenu("cellLayeringMenu",initLayeringMenu)
m.add("frontMenuItem","Bring to Front","layering",1)
m.add("backMenuItem","Send to Back","layering",2)
m.add("forwardMenuItem","Bring Forward","layering",3)
m.add("backwardsMenuItem","Send Backward","layering",4)
}
function initLayeringMenu()
{
var o=this
var res=getLayeringValue()
o.getItem(0).setDisabled(res.toFront)
o.getItem(1).setDisabled(res.toBack)
o.getItem(2).setDisabled(res.toForward)
o.getItem(3).setDisabled(res.toBackward)
}
function getLayeringValue()
{
var toFront=toBack=toForward=toBackward=true
var elts=getSelectedElts()
var elt=elts[0], res=null
if (elt.className=="tableCell")
{
var arrElt=new Array
arrElt[0]=elt.parent.parent.parent
res=getAttFromElts(".layering",arrElt)
}
else
res=getAttFromElts(".layering",elts)
if (res.defined && res.ret!=null)
{
var level = res.ret
switch (level)
{
case 'back-most':
toFront=toForward=false
break
case 'front-most':
toBack=toBackward=false
break
case 'middle':
toFront=toBack=toForward=toBackward=false
break
}
}
var ret=new Object
ret.toFront=toFront
ret.toBack=toBack
ret.toForward=toForward
ret.toBackward=toBackward
return ret
}
function createAlignmentMenu()
{
alignmentMenu=m=newMenu("alignmentMenu",initAlignmentMenu)
m.add("alignLeftItem","Align Left","alignment",1)
m.add("alignCenter","Align Center","alignment",2)
m.add("alignRight","Align Right","alignment",3)
m.addSeparator()
m.add("alignTop","Align Top","alignment",4)
m.add("alignMiddle","Align Middle","alignment",5)
m.add("alignBottom","Align Bottom","alignment",6)
m.addSeparator()
m.add("relativePos","Relative Position...","alignment",7)
}
function initAlignmentMenu()
{
var o=this, elts=getSelectedElts()
var len=(elts!=null)?elts.length:0
var oneEltSelected=(len>1)?false:true
if (isMultiTableCellInSameTable(elts))
oneEltSelected=true
var allInSameSection=areAllEltsInSameSection(elts)
var selectedTableClassName=new Object;
getTableClassNameIfTableSelected(elts, selectedTableClassName)
var oneHTableSelected=(selectedTableClassName.hTable!=undefined)
var oneVTableSelected=(selectedTableClassName.vTable!=undefined)
var oneXTableSelected=(selectedTableClassName.crossTable!=undefined)
var oneFormSelected=(selectedTableClassName.form!=undefined)
o.getItem(0).setDisabled(oneEltSelected)
o.getItem(1).setDisabled(oneEltSelected||oneXTableSelected || oneHTableSelected )
o.getItem(2).setDisabled(oneEltSelected||oneXTableSelected || oneHTableSelected )
o.getItem(4).setDisabled(oneEltSelected||(!allInSameSection))
o.getItem(5).setDisabled(oneEltSelected||(!allInSameSection)|| oneXTableSelected || oneVTableSelected || oneFormSelected)
o.getItem(6).setDisabled(oneEltSelected||(!allInSameSection)|| oneXTableSelected || oneVTableSelected || oneFormSelected)
o.getItem(8).setDisabled(!oneEltSelected)
}
function createCellRemoveMenu()
{
cellRemoveMenu=m=newMenu("cellRemoveMenu",initRemoveMenu)
m.add("removeRow","Row")
m.add("removeCol","Column")
m.add("removeTable","Table")
}
function determineRemoveRowCol(elt)
{
var bEnableRow=true
var bEnableCol=true
var zone=elt.parent,block=zone.parent
var nbRow=zone.rowCount
var nbCol=zone.colCount
switch(block.className)
{
case "vTable":
        if(elt.rowSpan == nbRow && zone.className=="b")
    bEnableRow=false
if (elt.colSpan == nbCol)
bEnableCol=false
break
case "hTable":
if(elt.rowSpan == nbRow)
    bEnableRow=false
if (elt.colSpan == nbCol && zone.className=="b")
bEnableCol=false
break
case "crossTable":
switch(zone.className)
{
case "b,h-1":
case "b,f-1":
if(elt.rowSpan == nbRow)
bEnableRow=false
break
case "b":
if(elt.rowSpan == nbRow)
bEnableRow=false
if (elt.colSpan == nbCol)
bEnableCol=false
break
case "h-1,b":
case "f-1,b":
if (elt.colSpan == nbCol)
bEnableCol=false
break
}
break
}
var rc=new Object
rc.bRow=!bEnableRow
rc.bCol=!bEnableCol
return rc
}
function determineRemoveTable(elts)
{
var tabBID=null
for (var i in elts)
{
var p=elts[i].parent,pp=p?p.parent:null,ppp=pp?pp.parent:null
var parBID=ppp?ppp.bid:null
if (i==0)
tabBID=parBID
else if (tabBID!=parBID)
{
tabBID=null
break
}
}
return tabBID
}
function showMenuRemove(menuItemRemove)
{
var elts=getSelectedElts();
var rc=new Object;
rc.bEnableRow=true;
rc.bEnableCol=true;
for (var i=0; i<elts.length; i++)
{
var r=determineRemoveRowCol(elts[i]);
rc.bEnableRow = rc.bEnableRow && !r.bRow 
rc.bEnableCol = rc.bEnableCol && !r.bCol 
}
return rc;
}
function initRemoveMenu()
{
var o=this;
var rc=new Object;
rc.bEnableRow=true;
rc.bEnableCol=true;
if ((getVariableQualification()!=1))
{
rc=showMenuRemove(o);
}
o.removeRow.setDisabled(!rc.bEnableRow);
o.removeCol.setDisabled(!rc.bEnableCol);
rc=determineRemoveTable(getSelectedElts());
o.removeTable.setDisabled(rc==null);
}
function createCalcMenu()
{
calcMenu=m=newMenu("calcMenu",initCalcMenu)
fillCalcMenu(m,false)
extracalcMenu=m=newMenu("extracalcMenu",initCalcMenu)
fillCalcMenu(m,true)
matrixCalc=m=newMenu("matrixCalc")
s=m.addSub(insertRowMenu,"calcRight","To the right")
s.attachSubMenu(calcMenu)
s=m.addSub(insertRowMenu,"calcBottom","At the bottom")
s.attachSubMenu(extracalcMenu)
}
function fillCalcMenu(m,extra)
{
m.addCheck("calcSum","Sum","menu",5)
m.addCheck("calcCount","Count","menu",6)
m.addCheck("calcAvg","Average","menu",7)
m.addCheck("calcMin","Min","menu",8)
m.addCheck("calcMax","Max","menu",9)
m.addCheck("calcPercent","Percentage","menu",10)
m.addSeparator();
m.addCheck("calcTotal","Default")
}
function initCalcMenu()
{
initCalculations(this)
}
function initCalculations(o)
{
var elts=getSelectedElts()
if (elts.length==1)
{
var elt=elts[0],d=((elt.dType!=1)&&(elt.nDType!=1)),zone=elt.parent,block=zone.parent,calc=null,isMeasure=(elt.qualification=="_msr")
var isSmartMsr=( elt.qualification=="_msr" && elt.aggregateFct=="DELEGATED")
o.calcSum.setDisabled(d || isSmartMsr)
o.calcAvg.setDisabled(d || isSmartMsr)
o.calcPercent.setDisabled(d || isSmartMsr)
o.calcCount.setDisabled(isSmartMsr)
o.calcMin.setDisabled(isSmartMsr)
o.calcMax.setDisabled(isSmartMsr)
o.calcTotal.setDisabled(!isMeasure)
switch(block.className)
{
case "vTable": calc=elt.vcalc; break
case "hTable": calc=elt.hcalc; break
case "crossTable":
{
switch(zone.className)
{
case "b,h-1": 
calc=elt.vcalc;break
case "h-1,b": 
calc=elt.hcalc;break
case "b": 
calc=o.id=="extracalcMenu"?elt.vcalc:elt.hcalc;break
}
}
break
}
setCalcFromString(o,calc)
}
}
function setCalcFromString(o,s)
{
var c=new Array(0,0,0,0,0,0,0)
if (s)
{
var vals=s.split(",")
for (var i in vals)
c[parseInt(vals[i])]=1
}
o.calcSum.check(c[0])
o.calcCount.check(c[1])
o.calcAvg.check(c[2])
o.calcMin.check(c[3])
o.calcMax.check(c[4])
o.calcPercent.check(c[5])
o.calcTotal.check(c[6])
}
function createBreakMenu()
{
breakMenu=m=newMenu("breakMenu", initBreakMenu)
m.add("insertBreak","Insert");
m.add("removeBreak","Remove");
m.add("breakProperties","Properties");
}
function initBreakMenu()
{
var o=this,elts=getSelectedElts()
o.breakProperties.setDisabled(false)
var ppp=elts[0].parent.parent.parent
_breakBlockBID=ppp.bid
var rc=getBreakStatus(elts)
if (rc!=null)
{
o.insertBreak.show(!rc.hasBreak)
o.insertBreak.setDisabled(!rc.brkAllowed)
o.removeBreak.show(rc.hasBreak)
o.removeBreak.setDisabled(!rc.brkAllowed)
}
else
{
o.insertBreak.setDisabled(true)
o.removeBreak.setDisabled(true)
}
}
function getBreakStatus(elts)
{
var hasBrk=false,isbrkAllowed=false
var qualif=0;
if ( (elts!=null) && (elts.length==1) && ((qualif=getVariableQualification())!=1) )
{
var elt=elts[0]
if (elt.className != "tableCell")
return {hasBreak:false, brkAllowed:false}
var ax=getTableCellAxis(elt,true)
var type=elt.type
var zone=elt.parent
if (elt.parent.parent)
var ppp=elt.parent.parent.parent
else
return {hasBreak:false, brkAllowed:false}
if (ppp==null)
return {hasBreak:false, brkAllowed:false}
var cn=elt.className
var isInForm=(ppp.block)?(ppp.block.blockType==10):false
if ( (ax!=-1) && (ax!=0) && isInAxis(ppp.axis[ax], elt) && !isInForm && (cn=='tableCell') && ((qualif!=_msr) || (qualif==_msr && elt.parent.className=='b')))
{
isbrkAllowed=true
var brkelt = flatFindbyId(ppp.axis[ax].brks,getCellFormulaOrId(elt))
if (brkelt)
hasBrk=true
else
hasBrk=false
}
else
{
hasBrk=false
isbrkAllowed=false
}
}
else
{
hasBrk=false
isbrkAllowed=false
}
return {hasBreak:hasBrk, brkAllowed:isbrkAllowed}
}
function createCellContextMenu()
{
cellContextMenu=m=newMenu("cellContextMenu",initcellContextMenu)
addDrillMenu(m)
m.add("setSection","Set as section")
m.addSeparator()
m.addSub(cellInsertMenu,"cellInsert","Insert")
m.addSeparator()
m.addSub(formatMenu,"format","Format")
m.addSeparator()
m.addSub(filterMenu,"filterMenu","Filter")
m.add("turnto","Turn table to...")
m.add("swap","Swap axis")
m.addSub(breakMenu,"breakMenu","Break")
m.addSub(sortSubMenu,"sort","Sort")
m.addSub(calcMenu,"calcMenu","Calculation")
m.addSeparator()
m.addSub(hyperlinkMenu,"cellHyperlinkMenu","Hyperlink")
m.addSeparator()
m.addSub(cellLayeringMenu,"cellLayering","Order")
m.addSeparator()
m.addSub(alignmentMenu,"alignment","Align")
m.addSeparator()
m.addSub(cellRemoveMenu,"cellRemove","Remove")
m.add("removeComposite","Remove");
}
function initcellContextMenu()
{
var o=this,elts=getSelectedElts()
setSwapMenu(elts,o.swap)
o.turnto.setDisabled(!isMultiTableCellInSameTable(elts));
setBreakMenu(elts,o.breakMenu)
setSetSectionMenu(elts,o.setSection)
setCalcMenu(elts,o.calcMenu)
o.sort.setDisabled(getSortValue(elts)==-1)
initdrillContextMenu(o)
setInsert(elts,o.cellInsert)
setHyperlinkMenu(elts,o.cellHyperlinkMenu)
setLayering(elts,o.cellLayering)
initUserRight(o.setSection,_usrInsertDuplicate);
initUserRight(o.cellInsert,_usrInsertDuplicate)
initUserRight(o.format,_usrUseFormatting);
initUserRight(o.filterMenu,_usrCreateEditReportFilter);
initUserRight(o.turnto,_usrInsertDuplicate);
initUserRight(o.swap,_usrInsertDuplicate);
initUserRight(o.breakMenu,_usrCreateEditBreak);
initUserRight(o.sort,_usrCreateEditSort)
initUserRight(o.calcMenu,_usrCreateEditCalculation);
initUserRight(o.cellRemove,_usrInsertDuplicate);
initUserRight(o.cellHyperlinkMenu,_usrUseFormula);
var rc=showMenuRemove(o.cellRemove);
var showComposite= !rc.bEnableRow && !rc.bEnableCol;
o.cellRemove.show(!showComposite);
o.removeComposite.show(showComposite);
repairMenu(o);
}
function setInsert(elts,w)
{
if (elts==null)
return
var e=(elts.length==1)
if (e)
e=(elts[0].className=="tableCell")
w.setDisabled(!e)
}
function getCalcStatus(elts)
{
if ( (elts==null) || (elts.length!=1) )
return false
var e=false
if (elts.length==1)
{
var elt=elts[0]
var ax=getTableCellAxis(elt,true)
if (ax!=-1)
{
var ppp=elt.parent.parent.parent,axis=ppp.axis[ax]
if(isInAxis(axis,elt))
e=true
}
}
return e
}
function setCalcMenu(elts,w)
{
var e=getCalcStatus(elts)
w.setDisabled(!e)
if (e)
{
var zone=elts[0].parent,block=zone.parent
w.attachSubMenu((zone.className=="b")&&(block.className=="crossTable")?matrixCalc:calcMenu)
}
}
function setCalc(elts)
{
var e=getCalcStatus(elts)
calcButton.setDisabled(!e)
if (e)
{
var zone=elts[0].parent,block=zone.parent
if ((zone.className=="b")&&(block.className=="crossTable"))
calcButton.menu=matrixCalc
else
{
calcButton.menu=calcBtnMenu
initCalculations(calcBtnMenu)
}
}
}
function setSetSectionMenu(elts,w)
{
var isDisabled=true
if ((elts.length==1)&&(elts[0].qualification!="_msr")&&(getTableCellAxis(elts[0],false)!=-1))
{
var elt=elts[0],ax=getTableCellAxis(elt,false)
var pb=elt.parent.parent.parent,axis=pb.axis[ax]
if (isInAxis(axis,elt)&& (pb.block.nDimDetails > 0)) 
isDisabled=false
}
w.setDisabled(isDisabled)
}
function setBreakMenu(elts,w)
{
if (!isMultiTableCellInSameTable(elts))
{
w.setDisabled(true);
return
}
for (var i in elts)
{
var elt=elts[i]
if (elt.className!="tableCell")
{
w.setDisabled(true)
return
}
if (elt.parent.parent.parent.block.blockType==10) 
{
w.setDisabled(true)
return
}
}
w.setDisabled(false)
}
function setSwapMenu(elts,w)
{
var bid=null
if (elts.length==0)
{
w.setDisabled(true)
return
}
if (elts.length==1)
{
var elt=elts[0]
if ((elt!=null)&&(elt.className=="block"))
{
switch(elt.block.blockType)
{
case 9:
case 17:
case 26:
case 27:
case 34:
case 35:
w.setDisabled(false)
break
default:
w.setDisabled(true)
break
}
return
}
}
for (var i in elts)
{
var elt=elts[i]
if (elt.className=="tableCell")
{
var block=elt.parent.parent
if (block.className!="crossTable")
{
w.setDisabled(true)
return
}
if (bid==null)
bid=block.bid
else if (bid!=block.bid)
{
w.setDisabled(true)
return
}
}
else
{
w.setDisabled(true)
return
}
}
w.setDisabled(false)
}
function createSortMenu()
{
chartVarsMenu=m=newMenu("chartVarsMenu",initChartVarsMenu)
sortSubMenu=m=newMenu("sortSubMenu",initSortSubMenu)
m.addCheck("sort0","None")
m.sortAsc=m.addCheck("sort1","Ascending","report",1)
m.sortDsc=m.addCheck("sort2","Descending","report",2)
m.addSeparator()
customSort=m.add("customSort","Custom sort...","report",13)
m.addSeparator()
removeSorts=m.add("removeSorts","Remove Sorts")
sortProps=m.add("sortProps","Properties")
}
function attachSortMenu()
{
var o=this,elts=getSelectedElts()
if (elts.length==1)
{
var elt=elts[0]
var menuId = o.menu?o.menu.id:""
if ((elt.className=="block") && (elt.block.blockType > 10)) {
if (menuId != "chartVarsMenu") {
o.attachMenu(chartVarsMenu)
}
} else {
if (menuId != "sortSubMenu") {
o.attachMenu(sortSubMenu)
}
}
}
}
function initSortSubMenu()
{
var o=this,elts=getSelectedElts(),srt=null,srtArr=null,canCustOrder=false,iconDelta=0,par=o.par,expID=null,isChart=false
isChart=(par && par.isChart)
if (elts.length==1)
{
var elt=elts[0], sect = null
if (isChart) 
{
srt=flatFindbyId(elt.axis[par.sortAxisIndex].sorts,par.exprID)
srt=srt?srt.kind:0
crdr=elt.axis[par.sortAxisIndex].crdr
if (crdr) {
for (var i=0; i < crdr.length; i++) {
if (crdr[i] == par.exprID) {
canCustOrder = true
break
}
}
}
expID=par.exprID
} else {
expID=getCellFormulaOrId(elt)
var type = elt.type
var qualif = getVariableQualification()
canCustOrder = !((qualif == _msr) || ((type == 2) && (qualif != _linkDim)))
if (qualif != _cls) {
srt=getSortValue(elts)
ax=getTableCellAxis(elt,false)
if (ax != -1) {
var ppp=elt.parent.parent.parent,axis=ppp.axis[ax]
srtArr=axis.sorts
} else { 
sect = (elt.className=="section")?elt : elt.parent
axis=sect.axis[0]
if (elt.className=="section")
expID=axis.vars[0]
srtArr=axis.sorts
if (srtArr)
{
var firstSrt=srtArr[0]
if (firstSrt&&firstSrt.id == expID)
srt=firstSrt.kind
else
srt=0
}
else
srt=0
}
}
}
iconDelta = isCustomSort(expID)? 13 : 0
}
for (var i=0;i<3;i++) 
{
if (i>0) 
{
o["sort"+i].setIcon((i + iconDelta) * 16, null, (i + iconDelta) * 16) 
}
o["sort"+i].check(i==srt)
}
o.removeSorts.show(!isChart)
o.sortProps.show(!isChart)
o.customSort.setDisabled(!(canCustOrder && !isLinkedMember(expID)))
o.removeSorts.setDisabled(srtArr==null)
o.sortProps.setDisabled(srt==null || (sect != null))
repairMenu(o)
}
function isLinkedMember(id)
{
if (id && doc._links)
{
var len=doc._links.length;
for (var i=0; i<len; i++)
{
if (doc._links[i]==id)
return true;
}
}
return false;
}
function initChartVarsMenu()
{
var o=this,elts=getSelectedElts()
if (elts.length <= 0)
return
var elt=elts[0]
if (!elt||(elt.className!="block"))
return
var mlen=o.items.length,axis=elt.axis,k=0,hasSorts=false
for (var i=3;i<6;i++)
{
var ax=axis[i].vars
var names=axis[i].names
if (axis[i].sorts!=null)
hasSorts=true
for (var j in ax)
{
var exprID=ax[j],item=null
if (k < mlen) {
item=o.getItem(k)
} else {
item=o.addSub(sortSubMenu,"sort"+k,"a")
}
item.show(true)
item.setText(names[j])
item.sortAxisIndex=i
item.sortIndexInAxis=j
item.exprID=ax[j]
item.isChart=true
if (k==0)
item.setDisabled(false)
k++
}
}
mlen=o.items.length
for (var i=k;i<(mlen-3);i++)
o.getItem(i).show(false)
if (k==0)
{
var item=o.getItem(0)
item.setDisabled(true)
item.setText("Empty")
}
var rmItem=o.items[mlen-1]
if (rmItem.id != "sortProps")
{
o.addSeparator()
o.add("removeSorts","Remove Sorts")
o.add("sortProps","Properties")
}
o.removeSorts.setDisabled(!hasSorts)
}
function createBlockContextMenu()
{
blockContextMenu=m=newMenu("chart",initblockContextMenu)
addAccelerators(m)
addDrillMenu(m)
m.addSub(formatMenu,"format","Format")
m.addSeparator()
m.addSub(filterMenu,"filterMenu","Filter")
m.add("turnto","Turn chart to...")
m.add("swap","Swap axis")
m.addSub(chartVarsMenu,"chartSort","Sort")
m.addSeparator()
m.addSub(cellLayeringMenu,"cellLayering","Order")
m.addSeparator()
m.addSub(alignmentMenu,"alignment","Align")
m.addSeparator()
im=m.add("removeBlock","Remove Chart","formula",1)
im.setAccelerator("Del")
}
function initblockContextMenu()
{
var o=this,elts=getSelectedElts()
o.paste.setDisabled((_copyElemCN == null) || !_canPaste || (elts.length>1))
o.copy.setDisabled(elts.length>1)
o.cut.setDisabled(elts.length>1)
setSwapMenu(elts,o.swap)
initdrillContextMenu(o)
var isChart=(elts.length>0)&&(elts[0]!=null)&&(elts[0].block.blockType > 10)
o.chartSort.show(isChart)
if (isChart)
o.chartSort.setDisabled(elts.length!=1)
o.turnto.setText(isChart?"Turn chart to...":"Turn table to...")
o.turnto.setDisabled(elts.length!=1)
setLayering(elts,o.cellLayering)
o.removeBlock.setText(isChart?"Remove Chart":"Remove Table")
initUserRight(o.cut,_usrInsertDuplicate)
initUserRight(o.copy,_usrInsertDuplicate)
initUserRight(o.paste,_usrInsertDuplicate)
initUserRight(o.format,_usrUseFormatting)
initUserRight(o.filterMenu,_usrCreateEditReportFilter);
initUserRight(o.swap,_usrInsertDuplicate);
initUserRight(o.chartSort,_usrCreateEditSort)
initUserRight(o.removeBlock,_usrInsertDuplicate)
repairMenu(o)
}
function createSectformatMenu()
{
sectformatMenu=m=newMenu("sectformatMenu")
m.add("cellFormatSection","Section")
m.add("cellFormatReport","Report")
sectContextMenu=m=newMenu("sectContextMenu",initSectContextMenu)
addAccelerators(m)
addDrillMenu(m)
m.addSeparator()
m.addSub(filterMenu,"filterMenu","Filter")
m.addSeparator()
m.addSub(sectformatMenu,"format","Format")
m.addSeparator()
m.addSub(sortSubMenu,"sort","Sort")
m.addSeparator()
var im=m.add("removeSect","Remove Section","formula",1)
im.setAccelerator("Del")
}
function initSectContextMenu()
{
var o=this
o.cut.setDisabled(true)
o.copy.setDisabled(true)
o.paste.setDisabled((_copyElemCN == null) || !_canPaste)
initdrillContextMenu(o)
initUserRight(o.cut,_usrInsertDuplicate)
initUserRight(o.copy,_usrInsertDuplicate)
initUserRight(o.paste,_usrInsertDuplicate)
initUserRight(o.format,_usrUseFormatting)
initUserRight(o.sort,_usrCreateEditSort)
initUserRight(o.filterMenu,_usrCreateEditReportFilter);
repairMenu(o)
}
function createReportSectCellContextMenu()
{
reportSectCellContextMenu=m=newMenu("reportSectCellContextMenu",initReportSectCellContextMenu)
addAccelerators(m)
addDrillMenu(m)
m.addSeparator()
m.addSub(filterMenu,"filterMenu","Filter")
m.filterSep=m.addSeparator()
m.addSub(formatMenu,"format","Format")
m.sortSep=m.addSeparator()
m.addSub(sortSubMenu,"sort","Sort")
m.addSeparator()
m.addSub(hyperlinkMenu,"cellHyperlinkMenu","Hyperlink")
m.addSeparator()
m.addSub(cellLayeringMenu,"cellLayering","Order")
m.addSeparator()
m.addSub(alignmentMenu,"alignment","Align")
m.addSeparator()
im=m.add("removeCell","Remove","formula",1)
im.setAccelerator("Del")
}
function initReportSectCellContextMenu()
{
var o=this,elts=getSelectedElts()
o.paste.setDisabled((_copyElemCN == null) || !_canPaste || (elts.length>1))
o.copy.setDisabled(elts.length>1)
o.cut.setDisabled(elts.length>1)
o.sort.setDisabled(getSortValue(elts)==-1)
setHyperlinkMenu(elts,o.cellHyperlinkMenu)
initdrillContextMenu(o)
setLayering(elts,o.cellLayering)
initUserRight(o.cut,_usrInsertDuplicate)
initUserRight(o.copy,_usrInsertDuplicate)
initUserRight(o.paste,_usrInsertDuplicate)
initUserRight(o.format,_usrUseFormatting)
initUserRight(o.filterMenu,_usrCreateEditReportFilter)
initUserRight(o.sort,_usrCreateEditSort)
initUserRight(o.cellHyperlinkMenu,_usrUseFormula)
repairMenu(o)
}
function createFreeCellContextMenu()
{
freeCellContextMenu=m=newMenu("freeCellContextMenu",initFreeCellContext)
addAccelerators(m)
addDrillMenu(m)
m.addSub(formatMenu,"format","Format")
m.addSeparator()
m.addSub(hyperlinkMenu,"cellHyperlinkMenu","Hyperlink")
m.addSeparator()
m.addSub(cellLayeringMenu,"cellLayering","Order")
m.addSeparator()
m.addSub(alignmentMenu,"alignment","Align")
m.addSeparator()
im=m.add("removeCell","Remove Cell","formula",1)
im.setAccelerator("Del")
}
function initFreeCellContext()
{
var o=this,elts=getSelectedElts()
o.paste.setDisabled((_copyElemCN == null) || !_canPaste || (elts.length>1))
o.copy.setDisabled(elts.length>1)
o.cut.setDisabled(elts.length>1)
setHyperlinkMenu(elts,o.cellHyperlinkMenu)
setLayering(elts,o.cellLayering)
setAlignment(elts,o.alignment)
initdrillContextMenu(o)
initUserRight(o.cut,_usrInsertDuplicate)
initUserRight(o.copy,_usrInsertDuplicate)
initUserRight(o.paste,_usrInsertDuplicate)
initUserRight(o.format,_usrUseFormatting)
initUserRight(o.removeCell,_usrInsertDuplicate)
initUserRight(o.cellHyperlinkMenu,_usrUseFormula)
repairMenu(o)
}
function createReportContextMenu()
{
reportContextMenu=m=newMenu("reportContextMenu",initReportContextMenu)
addAccelerators(m)
addDrillMenu(m)
m.add("tabsZone_rename","Rename Report",null,null,dblClickTabWidget)
m.addSeparator()
m.add("tabsZone_delete","Remove Report","formula",1)
m.add("tabsZone_add","Add Report")
m.add("tabsZone_duplicate","Duplicate Report")
m.addSeparator()
m.addSub(filterMenu,"filterMenu","Filter")
m.addSeparator()
m.addCheck("showCurrentReportChangesFromContext","Show changes")
m.addSeparator()
m.addCheck("drill","Drill Mode","report",9)
m.addSeparator()
m.add("cellFormatReport","Format Report")
}
function initReportContextMenu()
{
var o=this
o.tabsZone_delete.setDisabled(arrReports.length==1)
initdrillContextMenu(o)
o.cut.setDisabled(true)
o.copy.setDisabled(true)
o.paste.setDisabled((_copyElemCN == null) || !_canPaste)
rep = arrReports[iReportID]
o.showCurrentReportChangesFromContext.setDisabled(!_bTdcActivate)
o.showCurrentReportChangesFromContext.check(rep.sc)
o.drill.check(_bDrillMode)
initUserRight(o.cut,_usrInsertDuplicate)
initUserRight(o.copy,_usrInsertDuplicate)
initUserRight(o.paste,_usrInsertDuplicate)
initUserRight(o.drill,_usrWorkInDrillMode)
initUserRight(o.cellFormatReport,_usrUseFormatting)
initUserRight(o.filterMenu,_usrCreateEditReportFilter)
initUserRight(o.tabsZone_delete,_usrInsertDuplicate)
initUserRight(o.tabsZone_duplicate,_usrInsertDuplicate)
initUserRight(o.showCurrentReportChangesFromContext,_usrEnableTrackDataChanges)
repairMenu(o)
}
function createCompositeSelContextMenu()
{
compositeSelContextMenu=m=newMenu("compositeSelContextMenu",initCompositeSelContextMenu)
m.addSub(formatMenu,"format","Format")
m.addSeparator()
m.addSub(alignmentMenu,"alignment","Align")
m.addSeparator()
m.add("removeComposite","Remove","formula",1)
}
function initCompositeSelContextMenu()
{
var o=this, elts=getSelectedElts()
setAlignment(elts,o.alignment)
initUserRight(o.format,_usrUseFormatting)
initUserRight(o.removeComposite,_usrInsertDuplicate)
repairMenu(o)
}
_globalWOMCallback=null
function breakDlg_globalWOMCallback()
{
frameNav("DlgFrame","language/"+_lang+"/html/breakDialog.html")
}
function formatChart_globalWOMCallback()
{
frameNav("DlgFrame","language/"+_lang+"/html/applyFormatChartDialog.html")
}
function formatCell_globalWOMCallback()
{
frameNav("DlgFrame","language/"+_lang+"/html/applyFormatCellDialog.html")
}
function formatTable_globalWOMCallback()
{
frameNav("DlgFrame","language/"+_lang+"/html/applyFormatTableDialog.html")
}
function formatSection_globalWOMCallback()
{
frameNav("DlgFrame","language/"+_lang+"/html/applyFormatSectionDialog.html")
}
function formatReport_globalWOMCallback()
{
frameNav("DlgFrame","language/"+_lang+"/html/applyFormatReportDialog.html")
}
function formatTurnTo_globalWOMCallback()
{
frameNav("DlgFrame","language/"+_lang+"/html/applyTurnToDialog.html")
}
function editFilterFromViewer_globalWOMCallback()
{
frameNav("DlgFrame","language/"+_lang+"/html/editFilterDialog.html?viewer")
}
function editFilterFromPane_globalWOMCallback()
{
frameNav("DlgFrame","language/"+_lang+"/html/editFilterDialog.html")
}
function alerter_globalWOMCallback()
{
frameNav("DlgFrame","language/"+_lang+"/html/alerterDialog.html")
}
function sortProperties_globalWOMCallback()
{
frameNav("DlgFrame","language/"+_lang+"/html/sortDialog.html")
}
function customSort_globalWOMCallback()
{
frameNav("SecondDlgFrame",_root+"language/"+_lang+"/html/applyCustomSortDialog.html")
}
_maxLastUsedColors = 8
function addLastUsedColor(color)
{
if ((color == null) || (color == "-1,-1,-1")) return
alreadyExists = false
len = (_lastUsedColorsAr.length > _maxLastUsedColors) ? _maxLastUsedColors : _lastUsedColorsAr.length
for (var i = 0; i < len; i++)
{
if (_lastUsedColorsAr[i] == color) return
}
limit = (len > _maxLastUsedColors) ? _maxLastUsedColors : len++
for (i = limit; i > 0; i--)
{
_lastUsedColorsAr[i] = _lastUsedColorsAr[i-1]
}
_lastUsedColorsAr[0] = color
recordProp("CDZ_VIEW_lastUsedColors", _lastUsedColorsAr.join(";").replace(/,/g, "."))
}
function updateToolbarFromElt(elts)
{
var z=null
_singleSel = null
if (elts&&(elts.length==1)&&elts[0])
{
switch (elts[0].className)
{
case "section": z=".bgColor.rgb";break
case "pageHeader": z=".parent.report.headerBGColor.rgb";break
case "pageFooter": z=".parent.report.footerBGColor.rgb";break
case "body": z=".parent.report.bgColor.rgb";break
}
_singleSel = elts[0]
}
if (z)
{
var elt=elts[0]
setAlign(null)
setWrapText(null)
setFont(null)
setBorders(null)
setColors(elts,z)
setBreak(null)
setCalc(null)
setDup(null)
setFormula(null)
setAlerter(null)
setMergeOrSplit(null)
}
else
{
setAlign(_containsChart?null:elts)
setWrapText(_containsChart?null:elts)
setFont(_containsChart?null:elts)
setBorders(elts)
setColors(elts,".bgColor.rgb",_containsChart?"":".fgColor.rgb")
setBreak(_containsChart?null:elts)
setCalc(_containsChart?null:elts)
setDup(elts)
setFormula(_containsChart?null:elts)
setAlerter(_containsChart?null:elts)
setMergeOrSplit(_containsChart?null:elts)
}
setSort(elts)
setLayering(elts,layeringBtn)
setAlignment(elts,alignBtn)
setFormatPainter(elts)
setRemove(elts)
setInsert(elts,insertMenuIcon2)
setFilter(elts)
setBackgroundImage(elts)
applyFormatBt.setDisplay(!_appFmt)
applyFormatBt.setTooltip("")
applyFormatBt.setDisabled(true)
setDrillMode()
}
function setAlerter(elts)
{ 
if ((_nbDocAlerters == 0) || (elts==null))
{
alerterBtn.setDisabled(true)
return
}
var canAlrt=false
var len=elts.length,cn
if ((len==1)&&(elts[0]))
{
cn=elts[0].className
canAlrt=(cn=="tableCell")
}
else if (len>1)
{
canAlrt=isMultiTableCellInSameTable(elts)
}
alerterBtn.setDisabled(!canAlrt)
}
function setGrouping(elts)
{
if (elts==null)
{
groupingBtn.setDisabled(true)
return
}
var len=elts.length
var canGroup=false
if ((len==1)&&(elts[0]))
{
var elt=elts[0], cn=elt.className
var zone=elt.parent,block=zone.parent,bcn=block.className
if ( (bcn=='vTable') || (bcn=='hTable') || (bcn=='crossTable') || (bcn=='vTable') || 
((cn=='reportCell') && (elt.isSect )) )
canGroup=true
}
groupingBtn.setDisabled(!canGroup)
}
function setBreak(elts)
{
var rc=getBreakStatus(elts)
if (rc!=null) 
{
brkCheck1.check(rc.hasBreak)
brkCheck1.setDisabled(!rc.brkAllowed)
brkCheck2.check(rc.hasBreak)
brkCheck2.setDisabled(!rc.brkAllowed)
}
else
{
brkCheck1.setDisabled(true)
brkCheck2.setDisabled(true)
}
}
function getTableCellParentBID(elt)
{
if (elt&&(elt.className=="tableCell"))
return elt.parent.parent.parent.bid
else if ((elt.className=="block")&& !(elt.block.blockType > 10))
return elt.bid
else
return null
}
function getSectionCellParentBID(elt)
{
if (elt&&(elt.className=="reportCell")&& elt.isSect)
return elt.parent.bid
else if ((elt.className=="section"))
return elt.bid
else
return null
}
function isMultiTableCellInSameTable(elts)
{
isOK=true,len=elts.length,tableBID = null
for (var i=0;i<len;i++)
{
var currBID=getTableCellParentBID(elts[i])
if (currBID==null){
tableBID = null
return false
}
if (tableBID==null) 
{
tableBID=currBID
} else if (tableBID != currBID)
{
tableBID = null
return false
}
}
return true
}
function areAllEltsInSameSection(elts)
{
var len=elts.length,sectionBID=null,elt=null,par=null
for (var i=0;i<len;i++)
{
elt=elts[i]
switch (elt.className)
{
case 'cell':
case 'reportCell':
case 'block':
par=elt.parent
break
case 'tableCell':
par=elt.parent.parent.parent.parent
break
}
if (sectionBID==null)
sectionBID=par.bid
if (sectionBID!=par.bid)
return false
}
return true
}
function getTableClassNameIfTableSelected(elts, selectedTableClassName)
{
var len=elts.length,elt=null
var className=null;
for (var i=0;i<len;i++)
{
elt=elts[i]
if (elt.className == 'tableCell' ) 
{
var zone=elt.parent,block=zone.parent
className= block.className
}
else if (elt.className == 'block' && elt.children[0])
{ 
className= elt.children[0].className;
}
switch (className)
{
case "vTable": 
selectedTableClassName.vTable=1; 
break;
case "hTable": 
selectedTableClassName.hTable=1; 
break;
case "crossTable":
selectedTableClassName.crossTable=1;
break;
case "form": 
selectedTableClassName.form=1;
break;
}
}
}
function setDup(elts)
{
var canDup=false
if (elts!=null)
{
var len=elts.length,cn
if ((len==1)&&(elts[0]))
{
cn=elts[0].className
canDup=((cn=="block")||(cn=="reportCell")||(cn=="cell")||(cn=="tableCell"))
}
else if (len>1)
{
canDup=isMultiTableCellInSameTable(elts)
}
}
dupBtn.setDisabled(!canDup)
}
function isCustomSort(id)
{
return doc._CSArr[id] ? true : false
}
function setSort(elts)
{
var selSort=null, ax=-1, iconDelta=0
if ((elts!=null)&&(elts.length==1))
{
var elt=elts[0]
if (elt!=null)
{
var id=getCellFormulaOrId(elt), pp=null,cn=elt.className
if (cn=="tableCell")
{
var p=elt.parent
pp=p.parent.parent
ax=p.axis
if (ax!=-1)
{
axis=pp.axis[ax]
if (isInAxis(axis,elt))
{
var srt = flatFindbyId(axis.sorts,id)
selSort=srt!=null?srt.kind:0
var idx = isCustomSort(elt.id)?(selSort+13):selSort
m = sortSubMenu
m.sort0.check(idx==0||idx==13)
m.sort1.check(idx==1||idx==14)
m.sort2.check(idx==2||idx==15)
}
}
sortBut.setClickCallback(clickCB)
sortBut.setDisabled(false)
if (idx==0)
sortBut.updateButton(1, false)
else if (idx==13)
sortBut.updateButton(14, false)
else
sortBut.updateButton(idx)
insertSort.setDisabled(false)
} else if ((cn=="reportCell")&&(elt.parent.className=="section")) {
sortBut.setClickCallback(clickCB)
pp=elt.parent
axis=pp.axis[0] 
var srt = flatFindbyId(axis.sorts,id)
selSort=srt!=null?srt.kind:0
var idx = isCustomSort(elt.id)?(selSort+13):selSort
if (idx==0)
sortBut.updateButton(1, false)
else if (idx==13)
sortBut.updateButton(14, false)
else
sortBut.updateButton(idx)
var secId=null
if (elt.parent.axis[0])
{
secId=elt.parent.axis[0].vars[0]
}
sortBut.setDisabled(!((elt.qualification=="_msr") || (elt.id==secId)))
insertSort.setDisabled(false)
} else if (cn=="section") {
sortBut.setClickCallback(clickCB)
axis=elt.axis[0], id = axis.vars[0]
var srt = flatFindbyId(axis.sorts,id)
selSort=srt!=null?srt.kind:0
var idx = isCustomSort(id)?(selSort+13):selSort
if (idx==0)
sortBut.updateButton(1, false)
else if (idx==13)
sortBut.updateButton(14, false)
else
sortBut.updateButton(idx)
sortBut.setDisabled(false)
insertSort.setDisabled(false)
} else if ((cn=="block")&&(elt.block.blockType > 10)) { 
sortBut.setClickCallback(null)
sortBut.setDisabled(false)
insertSort.setDisabled(false)
} else {
sortBut.setClickCallback(clickCB)
sortBut.updateButton(selSort)
insertSort.setDisabled(true)
}
}
} else {
sortBut.updateButton(selSort)
insertSort.setDisabled(true)
sortBut.setClickCallback(null)
}
}
function createFormatPalette()
{
curIcon="format.gif"
formatContainer=newWidget("formatContainer")
formatSep=newPaletteSepWidget("formatSep")
pal=pal2=newPaletteWidget("pal2")
fontCombo=addCombo("fontCombo",true,150,"Font name")
sizeCombo=addIntCombo("sizeCombo", 3, "Font size", 50)
sizeCombo.setMin (_fntSMin) 
sizeCombo.setMax (_fntSMax) 
sp()
bold=addCheck("bold",3,null,"Bold")
italic=addCheck("italic",4,null,"Italic")
under=addCheck("under",5,null,"Underlined")
sp()
fgCombo=pal2.add(newIconColorMenuWidget("fgCombo",_img+"format.gif",clickCB,null,"Text color",16,16,7*16,0,7*16,16,_lastUsedColorsAr, false))
fgCombo.setDefaultActionOnIcon(true)
sp()
bgCombo=pal2.add(newIconColorMenuWidget("bgCombo",_img+"format.gif",clickCB,null,"Background color",16,16,6*16,0,6*16,16,_lastUsedColorsAr, false))
bgCombo.setDefaultActionOnIcon(true)
sp()
bgimgBtn=addIcn("bgimgBtn",16,null,"Background image")
sp()
bordersBtn=pal2.add(newIconBordersMenuWidget('bordersBtn',clickCB, null, "Borders"))
borderColor=pal2.add(newIconColorMenuWidget("borderColor",_img+"format.gif",clickCB,null,"Border color",16,16,8*16,0,8*16,16,_lastUsedColorsAr, false))
borderColor.setDefaultActionOnIcon(true)
sp()
hAlign=new Array
hAlign[0]=addRad("leftAlign",0,null,"Align Left","halignGrp")
hAlign[1]=addRad("centerAlign",1,null,"Align Center","halignGrp")
hAlign[2]=addRad("rightAlign",2,null,"Align Right","halignGrp")
sp()
vAlign=new Array
vAlign[0]=addRad("topAlign",12,null,"Align Top","valignGrp")
vAlign[1]=addRad("middleAlign",13,null,"Align Middle","valignGrp")
vAlign[2]=addRad("bottomAlign",14,null,"Align Bottom","valignGrp")
sp()
wrapText=addCheck("wrapText",15,null,"Wrap text")
sp()
mergeOrSplit=addCheck("mergeOrSplit",10,null,"Merge or split cells")
sp()
applyFormatBt = newButtonWidget("applyFormat","Apply","applyClickCB()",null,null," Click Apply to change format settings on selection: ",null,4)
pal2.add(applyFormatBt)
formatPainter=addCheck("formatPainter",11,null,"Format Painter\nDouble click this button to apply the same formatting to multiple places in the document",formatPainterDblClickCB)
formatPainterObs=newObserverOneEvent(_EVT_ESC_KEY,escKeyCB)
pal2.disableChildren(true)
}
function createReportingPalette()
{
curIcon="report.gif"
reportContainer=newWidget("reportContainer")
propSep=newPaletteSepWidget("propSep")
pal=pal3=newPaletteWidget("pal3")
alerterBtn=addIcnM("alerterBtn",7,null,"Activate or deactivate an alerter",clickCB,alerterMenuInit)
alerterMenu=alerterBtn.getMenu()
sp()
sortBut=addRadIcnM("sortBut",1,null,"Sort",clickCB,attachSortMenu)
sortBut.setAutoUpdateMenuIconFromItemIcon(false)
sp()
filterBut=addIcnM("filter",0,null,"Apply filter",clickCB)
filterButMenu=filterBut.getMenu()
butFilterMenuItem = filterButMenu.add("filter","Add Filter",clickCB,_img+"report.gif",0,0,0,0,16)
butRemoveFilterMenuItem =filterButMenu.add("removeFilter","Remove Filter",clickCB,_img+"report.gif",12*16,0,0,12*16,16)
sp()
brkCheck1=addCheck("brkCheck1",3,null,"Insert break to group results on a table")
sp()
calcButton=addIcnM("calcButton",4,null,"Add or remove calculations",clickCB)
m=calcBtnMenu=calcButton.getMenu()
m.calcSum=m.addCheck("calcSum","Sum",clickCB,_img+"menu.gif",5*16,0,null,5*16,16)
m.calcCount=m.addCheck("calcCount","Count",clickCB,_img+"menu.gif",6*16,0,null,6*16,16)
m.calcAvg=m.addCheck("calcAvg","Average",clickCB,_img+"menu.gif",7*16,0,null,7*16,16)
m.calcMin=m.addCheck("calcMin","Min",clickCB,_img+"menu.gif",8*16,0,null,8*16,16)
m.calcMax=m.addCheck("calcMax","Max",clickCB,_img+"menu.gif",9*16,0,null,6*16,16)
m.calcPercent=m.addCheck("calcPercent","Percentage",clickCB,_img+"menu.gif",10*16,0,null,10*16,16)
m.addSeparator();
m.calcTotal=m.addCheck("calcTotal","Default",clickCB)
sp()
insertMenuIcon2=addIcnM("insertMenuIcon2",16,null,"Insert",clickCB)
insertMenu2=insertMenuIcon2.getMenu()
insertMenu2.beforeShowCB=insertMenuInit
insertRow2=insertMenu2.add("insertRow2","New row",clickCB)
insertRow2.attachSubMenu(insertRowMenu)
insertCol2=insertMenu2.add("insertCol2","New column",clickCB)
insertCol2.attachSubMenu(insertColMenu)
sp()
removeBut=addIcnM("removeBut",12,null,"Remove",clickCB)
removeBtnMenu=removeBut.getMenu()
removeBtnMenu.add("removeRow","Row",clickCB)
removeBtnMenu.add("removeCol","Column",clickCB)
sp()
dupBtn=addIcnM("duplicate",5,null,"Duplicate this table or chart",clickCB)
dupBtnMenu=dupBtn.getMenu()
dupBtnMenu.add("duplicate","Duplicate To Right",clickCB)
dupBtnMenu.add("duplicateBottom","Duplicate Below",clickCB)
sp()
curIcon="layering.gif"
layeringBtn=addIcnM("layeringBtn",0,null,"Order")
curIcon="format.gif"
layeringBtnMenu=layeringBtn.getMenu()
layeringBtnMenu.beforeShowCB=initLayeringMenu
layeringBtnMenu.add("frontMenuItem","Bring to Front",clickCB,_img+"layering.gif",0,0,0,0,16)
layeringBtnMenu.add("backMenuItem","Send to Back",clickCB,_img+"layering.gif",1*16,0,0,1*16,16)
layeringBtnMenu.add("forwardMenuItem","Bring Forward",clickCB,_img+"layering.gif",2*16,0,0,2*16,16)
layeringBtnMenu.add("backwardsMenuItem","Send Backward",clickCB,_img+"layering.gif",3*16,0,0,3*16,16)
sp()
curIcon="alignment.gif"
alignBtn=addIcnM("alignBtn",0,null,"Align blocks and cells")
curIcon="format.gif"
alignBtnMenu=alignBtn.getMenu()
alignBtnMenu.beforeShowCB=initAlignmentMenu
alignBtnMenu.add("alignLeftItem","Align Left",clickCB,_img+"alignment.gif",1*16,0,0,1*16,16)
alignBtnMenu.add("alignCenter","Align Center",clickCB,_img+"alignment.gif",2*16,0,0,2*16,16)
alignBtnMenu.add("alignRight","Align Right",clickCB,_img+"alignment.gif",3*16,0,0,3*16,16)
alignBtnMenu.addSeparator()
alignBtnMenu.add("alignTop","Align Top",clickCB,_img+"alignment.gif",4*16,0,0,4*16,16)
alignBtnMenu.add("alignMiddle","Align Middle",clickCB,_img+"alignment.gif",5*16,0,0,5*16,16)
alignBtnMenu.add("alignBottom","Align Bottom",clickCB,_img+"alignment.gif",6*16,0,0,6*16,16)
alignBtnMenu.addSeparator()
alignBtnMenu.add("relativePos","Relative Position...",clickCB,_img+"alignment.gif",7*16,0,0,7*16,16)
pal3.disableChildren(true)
}
function createFormulaPalette()
{
curIcon="formula.gif"
formulaContainer=newWidget("formulaContainer")
formulaSep=newPaletteSepWidget("propSep")
pal=pal4=newPaletteWidget("pal4")
creationIcon=addIcn("variableCreation",5,null,"Create a variable")
formulaIcon=addIcn("formulaEditor",3,null,"Formula Editor") 
fOKBtn=addIcn("formulaOK",2,null,"Validate formula")
fCancelBtn=addIcn("formulaCancel",1,null,"Cancel formula")
formulaText=pal4.add(newTextFieldWidget("formulaText",null,null,null,clickCB,false,"Modify formula"))
formulaEdition=false
pal4.disableChildren(true)
creationIcon.setDisabled(false)
}
function createQueryPalette()
{
queryContainer=newJITPalette("pal5",false)
pal=pal5=queryContainer.getPalette()
curIcon="standard.gif"
saveQueryDoc=addIcnM("saveDocQuery",1, null,"Save document",clickCB)
saveQueryMenu = saveQueryDoc.getMenu()
saveQuery=saveQueryMenu.add("saveDocQuery","Save",clickCB,_img+curIcon,16,0,false,32,16,"Save document")
saveQuery.setAccelerator("S",_ctrl)
saveQueryAs=saveQueryMenu.add("saveDocQueryAs","Save as",clickCB)
querySep3=sp()
addIcn("addQuery",8,"Add Query","Add Query")
querySep1=sp()
curIcon="sqlicons.gif"
viewSQLIcon=addIcn("viewSQL",0,"View SQL","View SQL")
curIcon="standard.gif"
querySep2=sp()
queryPropIcon=addIcn("propertiesQuery",9,"Query Properties","Query Properties")
pal5.beginRightZone()
keyDateIcon=addIcn("propertiesKeyDate",20,"Keydate Properties","Keydate Properties")
querySep4=sp()
runQueryIcon=addIcn("runquery",7,"Run Query","Run query and return results to reports")
runQueriesIcon=addIcnM("runqueries",7,"Run Queries","Run all queries",clickCB,initRunQueriesMenu);
runQueriesMenu=runQueriesIcon.getMenu();
purgeQIcn=addIcn("purgeQ",18,"","Purge data");
purgeQAll=addIcnM("purgeQAll",18,null,"Purge data",clickCB,initPurgeAllMenu);
backToReport=addIcnM("closeQP",11,"Close","Close the query panel",clickCB)
closeQPMenu = backToReport.getMenu();
closeQPMenu.add("applyClose","Apply changes and close",clickCB);
closeQPMenu.add("revertClose","Discard changes and close",clickCB);
tabsDP=newTabBarWidget("tabsDP",false,clickCB,"position:absolute;top:-500px",dblClickTabWidget,beforeShowTabWidgetMenu,true)
}
function createEditorContextMenus()
{
createSortMenu()
createInsertRowMenu()
createInsertColMenu()
createFormatMenu()
createFilterMenu()
createCellInsertMenu()
createCellLayeringMenu()
createAlignmentMenu()
createCellRemoveMenu()
createCalcMenu()
createBreakMenu()
createHyperlinkMenu()
createCellContextMenu()
createBlockContextMenu()
createSectformatMenu()
createReportSectCellContextMenu()
createFreeCellContextMenu()
createReportContextMenu()
createCompositeSelContextMenu()
}
function createInsertMenu()
{
insertMenu1=m=newMenuWidget("insertMenu1",IconMenuWidget_hideCB,insertMenuInit)
insertRow1=insertMenu1.add("insertRow1","New row",clickCB)
insertRow1.attachSubMenu(insertRowMenu)
insertCol1=insertMenu1.add("insertCol1","New column",clickCB)
insertCol1.attachSubMenu(insertColMenu)
insertMenu1.addSeparator()
brkCheck2=insertMenu1.addCheck("brkCheck2","Break",clickCB,_img+"report.gif",3*16,0,true,3*16,16)
insertFilter=insertMenu1.add("insertFilter","Filter",clickCB,_img+"report.gif",0,0,false,0,16)
insertSort=insertMenu1.add("insertSort","Sort")
insertSort.attachSubMenu(sortSubMenu)
insertCalc=insertMenu1.add("insertCalc","Calculation")
insertCalc.attachSubMenu(calcMenu)
}
function insertMenuInit()
{
var o=this,elts=getSelectedElts()
var insRowCol=false,insCalc=false
if (elts.length==1)
{
var elt=elts[0]
if (elt!=null)
{
if (elt.className=="tableCell")
{
insRowCol=true
if (elt.isAggregate!=1)
{
var ax=getTableCellAxis(elt,true)
if (ax!=-1)
{
var ppp=elt.parent.parent.parent,axis=ppp.axis[ax]
if(isInAxis(axis,elt))
{
insCalc=true
}
}
}
}
}
}
insertRow1.setDisabled(!insRowCol)
insertRow2.setDisabled(!insRowCol)
insertCol1.setDisabled(!insRowCol)
insertCol2.setDisabled(!insRowCol)
insertCalc.setDisabled(!insCalc)
if (insCalc)
{
var zone=elts[0].parent,block=zone.parent
insertCalc.attachSubMenu((zone.className=="b")&&(block.className=="crossTable")?matrixCalc:calcMenu)
}
setSort(elts)
setBreak(elts)
setCalc(elts)
initUserRight(insertSort,_usrCreateEditSort)
initUserRight(brkCheck2,_usrCreateEditBreak)
initUserRight(insertCalc,_usrCreateEditCalculation)
initUserRight(insertFilter,_usrCreateEditReportFilter)
initUserRight(insertRow1,_usrInsertDuplicate)
initUserRight(insertCol1,_usrInsertDuplicate)
repairMenu(o)
}
function writeEditToolbars()
{
var show=bLaunchQP?false:(_showFormats&&isInteractive&&(_usrUseFormatting=="full"))
if (bFullScreen)
show=false
wr('<div id="formatContainer" style="display:'+(show?'block':'none')+'">')
formatSep.write()
pal2.write()
wr('</div>')
show=bLaunchQP?false:_showReporting&&isInteractive
if (bFullScreen)
show=false
wr('<div id="reportContainer" style="display:'+(show?'block':'none')+'">')
propSep.write()
pal3.write()
wr('</div>')
show=bLaunchQP?false:(_showFormula&&isInteractive&&(_usrUseFormula=="full"))
if (bFullScreen)
show=false
wr('<div id="formulaContainer" style="display:'+(show?'block':'none')+'">')
formulaSep.write()
pal4.write()
wr('</div>')
show=bLaunchQP?false:_showQueryPanel
if (bFullScreen)
show=false
queryContainer.setDisplay(show)
queryContainer.write()
}
function writeEditHTML()
{
insertMenu1.write()
insertMenu2.write()
fgCombo.getMenu().write()
bgCombo.getMenu().write()
wr('<div class="treeZone" id="querypanelBorder" style="display:block;position:absolute;left:-1000px;top:100px;">')
wr('<iframe id="querypanel" name="querypanel" frameborder="0" src="lib/empty.html"></iframe>')
wr('</div>');
tabsDP.write()
}
function initTabsDPMenu()
{
var menu=tabsDP.getMenu();
if(menu == null ) return;
menu.add("tabsDP_rename","Rename",dblClickTabWidget)
menu.addSeparator()
menu.add("tabsDP_remove","Remove",clickCB)
menu.add("tabsDP_add","Insert",clickCB)
menu.add("tabsDP_duplicate","Duplicate",clickCB)
menu.addSeparator()
var subMove=menu.add("tabsDP_move","Move",null)
submenu=newMenuWidget("tabsDP_submenu")
submenu.add("tabsDP_first","First",clickCB)
submenu.add("tabsDP_previous","Previous",clickCB)
submenu.add("tabsDP_next","Next",clickCB)
submenu.add("tabsDP_last","Last",clickCB)
subMove.attachSubMenu(submenu)
menu.addSeparator()
menu.add("tabsDP_run","Run",runQueriesCB)
menu.add("tabsDP_purge","Purge",purgeCB)
}
function initTabsZoneMenu()
{
tabsZoneMenu =tabsZone.getMenu()
if (tabsZoneMenu==null)
return;
var menu=tabsZoneMenu
menu.add("tabsZone_rename","Rename Report",dblClickTabWidget)
menu.addSeparator()
menu.add("tabsZone_add","Insert Report",clickCB)
menu.add("tabsZone_duplicate","Duplicate Report",clickCB)
menu.add("tabsZone_delete","Delete Report",clickCB,_img+"formula.gif",16,0,false,16,16)
menu.addSeparator()
var subMove=menu.add("tabsZone_move","Move Report",null)
submenu=newMenuWidget("tabsZone_move")
submenu.add("tabsZone_first","First",clickCB)
submenu.add("tabsZone_previous","Previous",clickCB)
submenu.add("tabsZone_next","Next",clickCB)
submenu.add("tabsZone_last","Last",clickCB)
subMove.attachSubMenu(submenu)
menu.addSeparator()
menu.addCheck("drill","Drill Mode",clickCB,_img+"report.gif",9*16,0,false,9*16,16)
menu.addSeparator()
menu.showCurrentReportChangesTabsZone = menu.addCheck("showCurrentReportChangesTabsZone","Show changes",clickCB)
menu.addSeparator()
menu.add("cellFormatReport","Format Report",clickCB)
menu.addSeparator()
subSaveComputerAS=menu.add("tabsZone_saveComputerAS","Save to my computer as",null)
submenu=newMenu("tabsZone_saveComputerAS",initTabZoneSaveReportComputerMenu)
submenu.add("tabsZone_saveReportXLS","Excel")
submenu.add("tabsZone_saveReportPDF","PDF")
subSaveComputerAS.attachSubMenu(submenu)
}
function initTabZoneSaveReportComputerMenu()
{
var o=this;
initUserRight(o.tabsZone_saveReportXLS,_usrDownloadExcel);
initUserRight(o.tabsZone_saveReportPDF,_usrDownloadPDF);
}
function initEditWidgets()
{
pal2.init()
pal3.init()
pal4.init()
tabsDP.init()
initTabsDPMenu()
initTabsZoneMenu()
insertMenu1.init()
insertMenu2.init()
formatContainer.init()
reportContainer.init()
formulaContainer.init()
queryContainer.init()
sizes=new Array("6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "20", "22", "24", "30", "36", "44")
for (var i in sizes)
sizeCombo.add(sizes[i],sizes[i])
var count=allFonts.length
for (var i=0;i<count;i++)
fontCombo.add(allFonts[i],""+allFonts[i])
querypanelBorder.init();
querypanelWidget.init();
}
function addLeftComboEditItems(c,dfc)
{
if (isEnableUserRight(_usrShowDocInfo))
c.add("docinfo","Document Summary",_img+"panes.gif",0,0,_root+"language/"+_lang+"/html/overview.html")
if (isEnableUserRight(_usrShowDataSummary))
c.add("dataSum","Data Summary",_img+"panes.gif",16,0,_root+"language/"+_lang+"/html/dataSummary.html")
if (isEnableUserRight(_usrShowResultObj) && isEnableUserRight(_usrInsertDuplicate))
c.add("reportelts","Chart and Table Types",_img+"standard.gif",15*16,0,_root+"language/"+_lang+"/html/reportElements.html")
if (isEnableUserRight(_usrShowResultObj))
c.add("resultObj","Available Objects",_img+"formula.gif",4*16,0,_root+"language/"+_lang+"/html/resultObjects.html")
if (isEnableUserRight(_usrShowFilterMap))
c.add("filtmap","Document Structure and Filters",_img+"panes.gif",32,0,_root+"language/"+_lang+"/html/filterMap.html")
}
_firstEditQueryTDC=true
function launchQueryPanel()
{
_showQueryPanel=true
if (_firstEditQueryTDC) {
_firstEditQueryTDC = _bTdcActivate?false:true
}
storeReportQueryState()
resetQueries()
showWaitDlg('Launching Query Panel',true,cancelLaunchQueryPanel)
_curIdRef=null
var url = "queryPanel"+_appExt;
url += urlParams(true,true)
url += "&iDPIndex="+iDataProviderID
setUnvDPParams() 
document.viewerForm.target="querypanel"
document.viewerForm.action = url;
document.viewerForm.submit();
switchToolbars();
updateIsQPModified(false);
}
function cancelLaunchQueryPanel()
{
_showQueryPanel=false
if(bLaunchQP)
{
if(parent.goBack) 
parent.goBack()
}
else 
{
var f = getQueryFrame();
if(f != null)
{
f.stopLoadingDP=true;
switchToolbars()
}
}
hideWaitDlg();
}
function revertChangesAndCloseQP()
{
if(bLaunchQP)
{
if(parent.goBack) 
parent.goBack()
}
else
{
strEntry=saveSEntry
bUseQueryDrill = savebUseQueryDrill;
arrDPs.length=0;
for(var i in saveDPTab)
arrDPs[i]=newDPInfo(saveDPTab[i].name,saveDPTab[i].editable,saveDPTab[i].rows,saveDPTab[i].refreshable,saveDPTab[i].dpType);
iDataProviderID=0
resetDPTab(arrDPs,iDataProviderID)
resetQueries()
_showQueryPanel=false
switchToolbars()
}
}
function updateIsQPModified(b)
{
_isQPModified = b;
backToReport.disableMenu(!_isQPModified);
}
function initRunQueriesMenu()
{
var m = runQueriesMenu;
var mlen = m.items.length;
var dplen = arrDPs.length;
var isPDP = false;
if(mlen < dplen+1)
{
for(var i=mlen;i<dplen+1;i++)
m.add('runquery'+i,'',runQueriesCB,_img+"standard.gif",8*16,0,false,8*16,1*16)
}
for(var i=0;i<dplen;i++)
{
var dp=arrDPs[i];
isPDP= (dp.dpType == 1);
m.getItem(i).setText(dp.name);
m.getItem(i).setIcon((isPDP?21:8)*16,0,(isPDP?21:8)*16,1*16,_img+"standard.gif");
m.getItem(i).show(true);
m.getItem(i).setDisabled(!dp.refreshable);
}
m.getItem(dplen).setText("Run all queries");
m.getItem(dplen).setIcon(7*16,0,7*16,1*16,_img+"standard.gif");
m.getItem(dplen).show(true);
if(dplen+1<mlen) 
{
for(var i=dplen+1;i<mlen;i++)
m.getItem(i).show(false);
}
}
function runQueriesCB()
{
var id=this.id;
var index = null;
if(id == "tabsDP_run")
{
index = getSelectedDPIndex(); 
}
else
{
index = this.id.slice(8); 
}
var dplen =arrDPs.length; 
if(index == dplen)
index = null; 
runDocument(index,true);
}
function runDocument(iDataProvider, bCheckPDP)
{
if(bCheckPDP && iDataProvider==null )
checkContextPDP("runquery");
else
{
var f = getQueryFrame();
if(f != null) 
f.submitQueries("runquery",iDataProvider);
}
}
function updateRunQueryIcn()
{
var len =arrDPs.length;
runQueryIcon.setDisplay(len  == 1);
runQueriesIcon.setDisplay(len > 1);
}
function editSetUserRights()
{
if (_defaultWebEditor=='I')
initUserRight(editQueryIcon,_usrEditQuery)
initUserRight(filterBut,_usrCreateEditReportFilter)
initUserRight(insertMenuIcon2,_usrInsertDuplicate)
initUserRight(dupBtn,_usrInsertDuplicate)
initUserRight(brkCheck2,_usrCreateEditBreak)
tabsZone.setShowContextMenuAllowed(isEnableUserRight(_usrShowRightClickMenu))
tabsDP.setShowContextMenuAllowed(isEnableUserRight(_usrShowRightClickMenu))
initUserRight(removeBut,_usrInsertDuplicate)
initUserRight(runQueryIcon,_usrRefreshDoc)
initUserRight(runQueriesIcon,_usrRefreshDoc)
initUserRight(viewSQLIcon,_usrViewSQL)
initUserRight(sortBut,_usrCreateEditSort)
initUserRight(brkCheck1,_usrCreateEditBreak)
initUserRight(calcButton,_usrCreateEditCalculation)
initUserRight(alerterBtn,_usrCreateEditAlerter)
initUserRight(formatContainer,_usrUseFormatting)
initUserRight(formulaContainer,_usrUseFormula)
}
function hideQueryPanelZones()
{
querypanelBorder.move(-20000,0)
tabsDP.resize(100,100)
tabsDP.move(-1000,0)
}
function restoreSelection()
{
var old_curIdRef=_curIdRef
var old_curIdRefBid=_curIdRefBid
_curIdRef=null
_curIdRefBid=null
for (var i in selectedBid)
{
if (selectedBid[i]!=null)
{
var obj=bidTable[i]
if (obj!=null)
{
selectObj(obj,true,i)
if (i==old_curIdRefBid)
restoreIdRef(i,obj,old_curIdRef)
}
else
{
arrayRemove(this,"selectedBid",i)
}
}
}
selectionChanged(false) 
}
function restoreIdRef(bid,arrLayers,old_curIdRef)
{
if (arrLayers==null)
return
var count=arrLayers.length,elt=findByBID(doc,bid)
if (elt)
{
for (var i=0;i<count;i++)
{
var layer=arrLayers[i]
if (layer.getAttribute("idref")==old_curIdRef)
{
_curIdRef=old_curIdRef
_curIdRefBid=bid
return
}
}
if (count>0)
{
_curIdRef=arrLayers[0].getAttribute("idref")
_curIdRefBid=bid
}
}
}
function editSwitchToolbars()
{
var f = getQueryFrame();
if(f && f.hideContainers && !_showQueryPanel) f.hideContainers()
if(_showQueryPanel)
{
updateRunQueryIcn();
updatePurgeIcn();
}
queryContainer.setDisplay(_showQueryPanel)
formatContainer.setDisplay(!isPDF && !_showQueryPanel && _showFormats && isInteractive)
reportContainer.setDisplay(!isPDF && !_showQueryPanel && _showReporting  && isInteractive)
formulaContainer.setDisplay(!isPDF && !_showQueryPanel && _showFormula  && isInteractive)
initUserRight(formatContainer,_usrUseFormatting);
initUserRight(formulaContainer,_usrUseFormula);
if (_showQueryPanel)
{
pal5.disableChildren(true)
}
}
_accepDDfunc=null
_dropDDfunc=null
_ddMargH=10
_ddMargV=5
function dropCellZone(o,e,doc)
{
var z=getZoom()/100
var fr=getReportFrame()
var pos=getPos(o)
var x= (eventGetX(e,fr)-convertX(pos.x) + getScrollX(fr)) / z
var y= (eventGetY(e,fr)-convertY(pos.y) + getScrollY(fr)) / z
var w=o.offsetWidth,h=o.offsetHeight
if (x>(w-_ddMargH))
return _cellRight
if (x<(_ddMargH))
return _cellLeft
if (y>(h-_ddMargV))
return _cellBottom
if (y<(_ddMargV))
return _cellTop
return _cellContent
}
function dragOverCB()
{
var fr=getReportFrame()
if (fr==null) return
var e=fr.event,cn=getClassNameByLayer(this)
e.returnValue=_accepDDfunc?!_accepDDfunc(this,cn,(cn=="tableCell"?dropCellZone(this,e,fr.document):null)):true
e.cancelBubble=true
}
function dropCB()
{
var o=this
var fr=getReportFrame()
if (fr==null) return
var e=fr.event,cn=getClassNameByLayer(o),d=fr.document
var z=getZoom()/100
var pageContainer = getPageContainer()
_lastX= ((- getPos(o,pageContainer).x*z - pageContainer.offsetLeft + getScrollX(fr) + eventGetX(e,fr)) / z)
_lastY= ((- getPos(o,pageContainer).y*z - pageContainer.offsetTop  + getScrollY(fr) + eventGetY(e,fr)) / z)
_lastLayer=o
if (_dropDDfunc)
_dropDDfunc(o,cn,(cn=="tableCell"?dropCellZone(o,e,fr.document):null))
e.cancelBubble=true
}
function getYRowInContainer(o,yRel)
{
var rinfo = o.getAttribute?o.getAttribute("rinfo"):null
var rh = o.getAttribute?o.getAttribute("rh"):null
var yRet = yRel
var rowID =0
if (rinfo != null)
{
var bags = rinfo.split(';')
if (bags != null)
{
var start = parseInt(bags.length -1)
start -= ((bags[start] == "")?1:0)
for (var i=start; i >= 0; i--)
{
var pair = bags[i].split(':')
if ((pair != null) && (pair.length == 2)) 
{
rowID = parseInt(pair[0])
var rowOffset = parseInt(pair[1])
if ((rowID % 2) == 1) {
var sectNB = parseInt(rowID / 2) - 1, c=o.childNodes
isSect = (hasSectionInChildNodes(c[(sectNB > 0)?sectNB:0])!= null)
if (!isSect) continue
}
if (rowOffset <= yRel) {
if (rowOffset < 0) {
var yAdj = (rh != null)?(yRel-parseInt(rh)):yRel
yRet = yAdj-rowOffset
} else {
yRet = yRel-rowOffset
}
break
}
}
}
}
}
return {y:yRet,row:rowID}
}
function getXinContainer(o,xRel)
{
var cinfo = o.getAttribute?o.getAttribute("cinfo"):null
var xRet = xRel
if (cinfo != null)
{
var bags = cinfo.split(';')
if ((bags != null) && (bags.length > 0))
{
var pair = bags[0].split(':')
if ((pair != null) && (pair.length == 2))
{
xRet = xRel - parseInt(pair[1])
}
}
}
return xRet
}
_lastX=0
_lastY=0
_lastLayer=null
_lastElt=null
_row=0
_baseColor=null
_containsChart=false
toolbarObs=newObserverOneEvent(_EVT_SELECTION_CHANGES,selectionChangeCB)
function getSelectedElts()
{
var elts = new Array
_containsChart = false
for (var i in selectedBid)
{
if (selectedBid[i]!=null)
{
var elt=findByBID(doc,i)
if (elt) {
elts[elts.length]=elt
if (elt.className == "block") {
_containsChart = true
}
}
}
}
return elts;
}
function selectionChanged(fromClick)
{
eventManager.notify(_EVT_SELECTION_CHANGES, fromClick)
}
function delayedSelectionChanged()
{
updateToolbarFromElt(getSelectedElts())
}
function selectionChangeCB(evt,data)
{
if (!isWOMLoaded)
setTimeout("selectionChangeCB()",100)
else
setTimeout("delayedSelectionChanged()",1)
}
function resetFormula()
{
var elts = getSelectedElts();
setFormula(elts)
}
function getClickCoordinates()
{
var l=_lastLayer
if (l==null) return null
var x=getXinContainer(l,_lastX),yRow=getYRowInContainer(l,_lastY),y=yRow.y, subMaxY=-1, subBID=null
_row=yRow.row
return {x:toUserUnit(Math.max(0,x/_dpi),true),y:toUserUnit(Math.max(0,y/_dpi),true)}
}
function hasSectionInChildNodes(o)
{
var bid=o.getAttribute?o.getAttribute("bid"):null
if ((bid!=null)&&(getClassNameByLayer(o)=="section"))
return bid
else
{
var c=o.childNodes,len=c.length
for (var i=0;i<len;i++)
{
bid=hasSectionInChildNodes(c[i])
if (bid!=null)
return bid
}
return null
}
}
function getInvertedColor(s)
{
_baseColor=s
if (s==null)
return null
s=s.toLowerCase()
var r=0,g=0,b=0
if (s.charAt(0)=="#")
{
r=parseInt(s.slice(1,3),16)
g=parseInt(s.slice(3,5),16)
b=parseInt(s.slice(5,7),16)
}
else if (s.indexOf("rgb")==0)
{
s=s.slice(s.indexOf("(")+1)
s=s.slice(0,s.indexOf(")"))
s=s.split(",")
r=parseInt(s[0])
g=parseInt(s[1])
b=parseInt(s[2])
}
else
return null
r=(r > 60) ? r-40 : r+60
g=(g > 60) ? g-40 : g+60
b=(b > 60) ? b-40 : b+60
return "rgb("+r+","+g+","+b+")"
}
function getTableCellAxis(elt,allowBreakCalc)
{
if (elt.className!="tableCell") return -1
var zone=elt.parent,block=zone.parent
if (allowBreakCalc)
{
if (block.className=="form") return -1
}
var cls=zone.className.slice(0,1)
if ((cls!="b")&&(cls!="h")) return -1
return zone.axis
}
function getComputedBG(o)
{
var f=_ie?"backgroundColor":"background-color"
if (_ie)
return o.currentStyle[f]
else
{
var df=o.ownerDocument.defaultView
if (df==null)
return null
var st=df.getComputedStyle(o,'')
if (st==null)
return null
return st.getPropertyValue(f)
}
}
function getCurrentBG(o)
{
if (o.tagName=="BODY")
return ("rgb(255,255,255)")
var r=getComputedBG(o)
if ((r==null)||(r=="")||(r=="transparent")||(r=="rgba(0,0,0,0)")||(r=="rgba(0, 0, 0, 0)"))
{
var p=o.parentNode
if (p)
r=getCurrentBG(p)
}
return r
}
function invertObj(o,keepChilden)
{
var invBg=o.oldInverted
var parentBG=o.parentBG
if (invBg==null)
{
parentBG=o.parentBG=getCurrentBG(o)
invBg=o.oldInverted=getInvertedColor(parentBG)
}
if (keepChilden)
{
var n=o.childNodes,count=n.length
for (var i=0;i<count;i++)
{
var w=n[i],st=w.style
if (w.tagName&&(w.tagName!="A")&&st)
{
var r=getComputedBG(w)
if ((r==null)||(r=="")||(r=="transparent")||(r=="rgba(0,0,0,0)")||(r=="rgba(0, 0, 0, 0)"))
{
w.oldBGP=st.backgroundColor
st.backgroundColor=parentBG
}
}
}
}
var st=o.style
if (invBg!=null)
st.backgroundColor=invBg
else
{
st.backgroundColor="#000000"
o.oldFg=st.color
o.hasOldFG=true
st.color="#FFFFFF"
return true
}
}
function selectLayer(o,sel,elt,keepChilden)
{
if (o==null) return
var s=o.style
if (sel)
{
if (o.isBOSelected==false)
{
o.isBOSelected=true
invertObj(o,keepChilden)
}
else if (o.isBOSelected==null)
{
o.oldBg=s.backgroundColor
o.isBOSelected=true
invertObj(o,keepChilden)
}
}
else
{
if (o.oldBg!=null)
{
s.backgroundColor=o.oldBg
if (o.hasOldFG)
s.color=o.oldFg
o.isBOSelected=false
if (keepChilden)
{
var n=o.childNodes,count=n.length
for (var i=0;i<count;i++)
{
var w=n[i],st=w.style
if (w.tagName&&(w.tagName!="A")&&st)
st.backgroundColor=w.oldBGP
}
}
}
}
}
function blockFeedback_mouseDown(e)
{
if (_ie)
e=getReportFrame().event
e.cancelBubble=true
simulateClick(e)
}
function blockFeedback_mouseOver(e)
{
if (_ie)
e=getReportFrame().event
e.cancelBubble=true
}
function blockFeedback_ContextMenu(e)
{
if (_saf)
this.onmousedown(e)
if (_ie)
e=getReportFrame().event
e.cancelBubble=true
incContextMenu(this,e)
return false;
}
function selectObjInstance(o,sel,bid,forceScroll,elt)
{
var selW=4
var dselW=selW*2
var cn=elt.className
if (forceScroll && (i==0)) { 
var w = getReportFrame()
if (w==null) return
var absPos =  getPos(o)
var absPosX = absPos.x, absPosY = absPos.y, scrX = getScrollX(w), scrY = getScrollY(w)
var scrToX = -1, scrToY = -1
if (((absPosX - scrX) < 0) || ((absPosX - scrX) > winWidth(w))) {
scrToX = absPosX
}
if (((absPosY - scrY) < 0) || ((absPosY - scrY) > winHeight(w))) {
scrToY = absPosY
}
winScrollTo(((scrToX != -1)? scrToX : scrX), ((scrToY != -1)? scrToY : scrY), w)
}
var isTable = (cn=="block")&& !(elt.block.blockType > 10)
if ((cn=="block")&&!isTable)
{
var st=o.style
st.backgroundColor=sel?"black":""
var im=o.childNodes[_bDrillMode?1:0]
if (im&&im.style)
{
setLayerTransp(im,sel?80:null)
}
}
else if ((cn=="block")&&isTable)
{
var fr=getReportFrame()
var bid = o.getAttribute("bid"),idref=o.getAttribute("idref")
selectLayer(o,sel,elt,sel)
if ((o.selLayIndex==null)&&(sel))
{
if (fr.__arrSelTab==null)
fr.newArrSelTab()
o.selLayIndex=fr.__arrSelTab.length
var newNode = fr.document.createElement('div'),newSt=newNode.style
newNode.onmousedown   = blockFeedback_mouseDown
newNode.onmouseover   = blockFeedback_mouseOver
newNode.oncontextmenu = blockFeedback_ContextMenu
newSt.position = "absolute";
newSt.overflow = "hidden";
newSt.top    = "" + (o.offsetTop    - 8)  + "px"
newSt.left   = "" + (o.offsetLeft   - 8)  + "px"
newSt.width  = "" + (o.offsetWidth  + 16) + "px"
newSt.height = "" + (o.offsetHeight + 16) + "px"
newSt.backgroundColor = getCurrentBG(o)
fr.__arrSelTab[o.selLayIndex] = o.parentNode.insertBefore(newNode,o);
newNode.setAttribute("bid", bid)
newNode.setAttribute("idref", idref)
}
if (o.selLayIndex != null)
{
fr.__arrSelTab[o.selLayIndex].style.display= sel ? "block" : "none"
fr.__arrSelTab[o.selLayIndex].setAttribute("bid", sel? bid : null)
}
}
else if ((cn=="section")||(cn=="pageHeader")||(cn=="pageFooter")||(cn=="body")) 
{
var l=o,st=l.style
var bd=(cn=="pageFooter")?"borderTop":"borderBottom"
if (cn!="body")
{
if (_moz)
{
if (o.oldHeight==null)
{
var cStyle = o.ownerDocument.defaultView.getComputedStyle(o,'')
o.oldPadding=parseInt(cStyle.getPropertyValue("padding-bottom"))
o.oldHeight=parseInt(cStyle.getPropertyValue("height"))
}
if (sel)
{
st.height="" + (o.oldHeight-1) + "px"
st.paddingBottom="0px"
}
else
{
st.height="" + (o.oldHeight) + "px"
st.paddingBottom="" + (o.oldPadding) + "px"
}
}
st[bd]=sel?"1px dotted black":"0px solid black"
}
selectLayer(l,sel,elt,true)
}
else if (isTable||(elt.className=="tableCell")||(elt.className=="reportCell")||(elt.className=="cell"))
{
selectLayer(o,sel,elt)
}
}
function selectObj(objs,sel,bid,forceScroll)
{
if (objs==null)
return
var count=objs.length,elt=findByBID(doc,bid)
if (elt)
{
for (var i=0;i<count;i++)
{
selectObjInstance(objs[i],sel,bid,forceScroll,elt)
}
}
}
function deselectAll(bid,restricTo)
{
for (var i in selectedBid)
{
if ((i!=bid)&&(selectedBid[i]!=null))
{
var deselect=true
if (restricTo)
{
var elt=findByBID(doc,i)
var className=elt?elt.className:""
if (className!=restricTo)
deselect=false
}
if (deselect)
{
selectedBid[i]=null
selectObj(bidTable[i],false,i)
}
}
}
if (!_useParentContextMenu)
{
_bodrill=null
_drillPathInfo=null
}
}
function selectSingle(bid,forceScroll)
{
selectObj(bidTable[bid],true,bid,forceScroll)
selectedBid[bid]=1
}
_moutTimer=null
_currentMoveLayer=null
globMoveObj=null
_isMoving=false
function clearMoutTimer()
{
if (_moutTimer!=null)
{
clearTimeout(_moutTimer)
_moutTimer=null
}
}
function mout()
{
if (!_isMoving)
{
if ((_moutTimer==null)&&(_disableBlockFrame==false))
{
_moutTimer=setTimeout("_moutTimer=null;showMoveZone(false)",800)
}
}
}
function mouseOut()
{
if (_isDDEnabled || _formatPainterBid != null) {
newTooltipWidget().show(false)
}
}
function moveLayOver()
{
this.onmouseout=mout
clearMoutTimer()
}
function beginMoveCB(e)
{
if (!_isMoving)
{
this.onmouseout=null
globMoveObj=_currentMoveLayer
var fr=getReportFrame()
if (fr == null) return
if (_ie)
e=fr.event
var o=this,bid=o.getAttribute?o.getAttribute("bid"):""
var isLeftButton=false
if (_saf)
isLeftButton=eventIsLeftButton(e,fr)&&(e.button == _leftBtn)
else
isLeftButton=eventIsLeftButton(e,fr)
if (isLeftButton && (window._formatPainterBid != null) && (_formatPainterBid != bid))
{
var p=urlParamsNoBID()
p += "&sTargetBid="+ bid + "&sBid=" + _formatPainterBid
if ((_reportIdxSrc != null) && (_reportIdxSrc != iReportID)){
p += "&reportIdxSrc="+ _reportIdxSrc
}
wt()
setTimeout('frameNav("Report","' + _root + "processFormatPainter"+_appExt + p + '")',1);
formatPainterStopIfSingleAction(e)
return false
}
if (_isDDEnabled && DDActionMethod)
{
DDActionMethod(null)
}
var z=getZoom()/100
var rDoc=fr.document
if (isLeftButton)
{
rDoc.body.ondragstart=bodyDragStart
rDoc.body.onmousemove=globmmove
globMoveObj.resizeZoneX=-getPos(globMoveObj).x+absxpos(e,z,fr)
globMoveObj.resizeZoneY=-getPos(globMoveObj).y+absypos(e,z,fr)
_isMoving=true
var pos=getPos(_currentMoveLayer)
_moveStartX=convertX(pos.x)/z
_moveStartY=convertY(pos.y)/z
var elt=findByBID(doc,bid)
var parentElt=searchClassName(elt,"section")
if (parentElt==null)
parentElt=searchClassName(elt,"body")
if (parentElt==null)
parentElt=searchClassName(elt,"pageHeader")
if (parentElt==null)
parentElt=searchClassName(elt,"pageFooter")
parentBid=parentElt.bid
var containerL= getParentLayerFromBid(globMoveObj,parentBid,false)
_MoveStartRow=getYRowInContainer(containerL,ypos(containerL,e,rDoc,z)).row
}
_curIdRef=o.getAttribute("idref");
_curIdRefBid=bid
moveLayContextSelect(e,this)
simulateClick(e)
e.cancelBubble=true
if (!_webKit)
return false
}
}
function elemup(e)
{
var o=this
var className=getClassNameByLayer(o)
if (_ie)
{
try
{
this.releaseCapture()
}
catch(expt) {}
}
_targetCopyBid = null
_canPaste=false
switch (className)
{
case "section":
case "pageHeader":
case "pageFooter":
case "body":
var fr=getReportFrame()
if (fr == null) return
var d=fr.document, targetBID=o.getAttribute("bid")
if (globMoveObj != null) {
var bid=globMoveObj.getAttribute("bid")
if ((className=="pageHeader")||(className=="pageFooter"))
{
if (getClassNameByLayer(globMoveObj)=="block")
{
return
}
}
} else {
if ((_copyElemCN == null) || (((className=="pageHeader")||(className=="pageFooter")) && (_copyElemCN == "block"))) {
_canPaste=false
return
} else {
_canPaste=true
_targetCopyBid = targetBID
}
}
var fr=getReportFrame()
if (fr == null) return
e=_ie?fr.event:e
if ((globResizeObj==null)&&(globMoveObj==null))
e.cancelBubble=true
showMoveZone(false)
globMoveObj=null
var z=getZoom()/100,pixPerfectX = 1,pixPerfectY = 1
var pageContainer = getPageContainer()
_lastX= ((- getPos(o,pageContainer).x*z - pageContainer.offsetLeft + getScrollX(fr) + eventGetX(e,fr)) / z) - pixPerfectX
_lastY= ((- getPos(o,pageContainer).y*z - pageContainer.offsetTop  + getScrollY(fr) + eventGetY(e,fr)) / z) - pixPerfectY
_lastLayer=o
_lastElt=findByBID(doc,targetBID)
var xybid=getClickCoordinates()
_copyX = xybid.x 
_copyY = xybid.y
_copyRow = _row
if (_isMoving)
{
_isMoving=false
var dX = toUserUnit(( (eventGetX(e,fr) + getScrollX(fr))/z - _moveStartX)/_dpi,true);
var dY = toUserUnit(( (eventGetY(e,fr) + getScrollY(fr))/z - _moveStartY)/_dpi,true);
var url = _root + "processMoveElement"+_appExt
url += urlParamsNoBID()+"&sBid="+escape(bid) + "&sTargetBid="+escape(targetBID) + "&sX=" + xybid.x + "&sY=" + xybid.y + "&sUnitIsInch=" + _unitIsInch + "&sRow=" + _row + "&sStartRow=" + _MoveStartRow + "&sDx="+dX+"&sDy="+dY
if (isEnableUserRight(_usrInsertDuplicate))
url += "&sDup=" + (_mac?(e.metaKey) : (e.ctrlKey))
wt()
frameNav("Report",url,true)
}
break
}
}
function mover()
{
if (!isEnableUserRight(_usrUseFormatting))
return
if (this.getAttribute)
{
if (this.getAttribute("ismovelayer")=="y")
return
}
if (!_isMoving)
{
if (globResizeObj!=null)
{
clearMoutTimer()
showMoveZone(false)
}
var className=getClassNameByLayer(this)
switch (className)
{
case "tableCell":
case "reportCell":
case "block":
case "cell":
clearMoutTimer()
break
}
switch (className)
{
case "reportCell":
case "block":
case "cell":
showMoveZone(true,this)
this.onmouseout=mout
break
case "tableCell":
var node=this.parentNode.parentNode.parentNode.parentNode
showMoveZone(true,node)
this.onmouseout=mout
break
}
}
}
moveLayer_d0=null
moveLayer_d1=null
moveLayer_d2=null
moveLayer_d3=null
moveLayer_d4=null
function createMoveLayers(fr,d)
{
var w=1
append(d.body,'<div ismovelayer="y" onmouseover="event.cancelBubble=true" id="bomovediv0" style="overflow:hidden;z-index:9000;position:absolute;display:none;width:13px;height:13px;background-image:url('+_img+'moveobject.gif);"></div>',d)
for (var i=1;i<5;i++)
append(d.body,'<div ismovelayer="y" id="bomovediv'+i+'" style="overflow:hidden;z-index:8999;position:absolute;display:none;width:'+w+'px;height:'+w+'px;background-image:url('+_img+'resizepattern.gif);"></div>',d)
for (var i=0;i<5;i++)
{
eval('fr.d'+i+'=d.getElementById("bomovediv'+i+'")')
eval('fr.d'+i+'.onmouseover=moveLayOver')
}
moveLayer_d0=fr.document.getElementById("bomovediv0");
moveLayer_d1=fr.document.getElementById("bomovediv1");
moveLayer_d2=fr.document.getElementById("bomovediv2");
moveLayer_d3=fr.document.getElementById("bomovediv3");
moveLayer_d4=fr.document.getElementById("bomovediv4");
moveLayer_d0.style.cursor="move"
moveLayer_d0.onmousedown=beginMoveCB
addDblClickCB(moveLayer_d0,moveLayDblClickCB)
}
function moveLayContextMenu(e)
{
if (_saf)
this.onmousedown(e)
incContextMenu(this,e)
return false
}
_tableBid=null
function moveLayContextSelect(e,lyr)
{
e.cancelBubble=true
var bid=lyr.getAttribute("bid")
_tableBid=null
var ctrlIsPressed = (_mac ? e.altKey : e.ctrlKey) || e.shiftKey
if (!ctrlIsPressed)
{
if ((eventIsLeftButton(e,getReportFrame()))||(selectedBid[bid]==null))
deselectAll()
} 
else
{
deselectAll(bid,"section")
deselectAll(bid,"pageHeader")
deselectAll(bid,"pageFooter")
deselectAll(bid,"body")
}
var elt = findByBID(doc, bid)
selectSingle(bid)
selectionChanged(true)
delayedSelectionChanged()
}
function showMoveZone(show,layer,x,y,hideArrow)
{
var _w=1
if (_moutTimer!=null)
return
var fr=getReportFrame()
if (fr==null)
return
var d=fr.document
_currentMoveLayer=layer
if (isWOMLoaded!=true)
{
return
}
var d0=moveLayer_d0,d1=moveLayer_d1,d2=moveLayer_d2,d3=moveLayer_d3,d4=moveLayer_d4
var st0=d0.style,st1=d1.style,st2=d2.style,st3=d3.style,st4=d4.style
if (show)
{
if (layer.isSection) return ; 
var bid = layer.getAttribute("bid"),idref=layer.getAttribute("idref")
d0.setAttribute("bid", bid)
d0.setAttribute("idref", idref)
var x1=getPos(layer).x,y1=getPos(layer).y
var w=layer.offsetWidth,h=layer.offsetHeight
if (x==null)
{
x=convertX(x1)
y=convertY(y1)
}
w = convertWidth(w)
h = convertHeight(h)
st0.left = "" + Math.max(0, x-13) + "px"
st0.top  = "" + Math.max(0, y-13) + "px"
st1.left=st3.left=st4.left=""+(x-_w)+"px"
st2.left=""+(x+w)+"px"
st1.top=st2.top=st3.top=""+(y-_w)+"px"
st4.top=""+(y+h)+"px"
st1.height=st2.height=""+(h+(2*_w))+"px"
st3.width=st4.width=""+(w+(2*_w))+"px"
d0.oncontextmenu=moveLayContextMenu
}
var ds=show?"block":"none"
st0.display=isPicker?"none":(show?(hideArrow?"none":"block"):"none")
st1.display=st2.display=st3.display=st4.display=ds
}
function getContainerFromBid(lyr, bid) {
var currentRe = findByBID(doc,bid)
var isChart = false
if (currentRe.className == "block") {
isChart = (currentRe.block.blockType > 10)
}
while ((currentRe.className != "body") && (currentRe.className != "section")) {
currentRe = currentRe.parent
}
var isSection = (currentRe.className == "section"), contBid = currentRe.bid
while ((lyr.getAttribute("bid")) && (lyr.getAttribute("bid") != contBid)) {
lyr = lyr.parentElement
}
return (isSection && isChart && lyr.parentElement && (lyr.style.pixelTop <= 0) && (lyr.parentElement.tagName == "DIV"))?lyr.parentElement:lyr
}
function getPageHeaderHeight(lyr, bid) {
var currentRe = findByBID(doc,bid)
while ((currentRe.className != "body")) {
currentRe = currentRe.parent
}
contBid = currentRe.parent.children[0].bid
while ((lyr.getAttribute("bid")) && (lyr.getAttribute("bid") != contBid)) {
lyr = lyr.parentElement
}
return lyr.style.pixelHeight
}
function moveLayDblClickCB()
{
escapeFormatPainter()
var bid=this.getAttribute("bid")
var e = findByBID(doc, bid), cn = e.className, clickID =""
switch(cn) {
case "cell":
case "reportCell": 
clickID = "cellFormatCell"
break
case "block": 
clickID = (e.children[0])?"cellFormatTable":"cellFormatChart"
break
}
if (clickID != "") {
setClickCBID(clickID)
clickCB()
}
}
function mup(e)
{
var o=this
var fr =getReportFrame() 
if (fr == null) return true
var ee=_ie?fr.event:e
if (eventIsLeftButton(ee,fr))
{
triggerResize(o,e)
o.downZone=null
drawResize(e)
return true
}
else
globResizeObj=null
return true
}
_editInPlace="_BOFormulaEditInPlace"
_editTabWidget="_TabWidgetEditInPlace"
_submitEditInPlace=true
_paramsEditInPlace=null
function viewDblClickCB(e)
{
escapeFormatPainter()
var reportFrame=getReportFrame()
if (reportFrame == null) return
var d=reportFrame.document
if (_ie)
e=reportFrame.event
var o=this
if (o!=null)
{
var bid=o.getAttribute("bid"),elt=findByBID(window.doc,bid),cn=elt?elt.className:""
if ((cn=="tableCell")||(cn=="reportCell")||(cn=="cell"))
{
var zone=getCellZone(o,e,d)
if (zone==_cellRight)
{
var p=urlParams(true)
if (p!="")
{
wt()
frameNav("Report","processResize"+_appExt+p+"&iW=0&sElemZone=right",true)
}
}
else if (zone==_cellLeft)
{
var p=urlParams(true)
if (p!="")
{
wt()
frameNav("Report","processResize"+_appExt+p+"&iW=0&sElemZone=left",true)
}
}
else if (zone==_cellBottom)
{
var p=urlParams(true)
if (p!="")
{
wt()
frameNav("Report","processResize"+_appExt+p+"&iH=0&sElemZone=bottom",true)
}
}
else if (zone==_cellTop)
{
var p=urlParams(true)
if (p!="")
{
wt()
frameNav("Report","processResize"+_appExt+p+"&iH=0&sElemZone=top",true)
}
}
else if (zone==_cellContent)
{
var size=(elt.font?elt.font.size:9)
if (size<=0)
size=9
fEdit_show(o,size,elt.fcell?elt.fcell.formula:elt.formula,bid)
}
e.cancelBubble=true
return false
}
}
return true
}
_fEditIsShown=false
function fEdit_show(o,size,formula,bid)
{
if(isEnableUserRight(_usrUseFormula))
{
var fr = getReportFrame()
if (fr == null) return
var doc=fr.document
var fEdit=doc.getElementById(_editInPlace)
if (fEdit==null)
{
append(doc.body,'<input onselectstart="event.cancelBubble=true;return true" type="text" class="textinputs" id="'+_editInPlace+'" style="display:none;zindex:200;position:absolute">',doc)
fEdit=doc.getElementById(_editInPlace)
fEdit.onkeydown=fEdit_keyDown
fEdit.onblur=fEdit_lostFocus
fEdit.onmousedown=fEdit_mouseDown
fEdit.onmousemove=fEdit_mouseMove
fEdit.onmouseup=fEdit_mouseUp
}
fEdit.value=formula
fEdit.oldValue=fEdit.value
fEdit.isDisplayed=true
var st=fEdit.style
st.left=""+ convertX(getPos(o).x)+"px"
st.top=""+ convertY(getPos(o).y)+"px"
st.width=""+convertWidth(o.offsetWidth<80?80:o.offsetWidth )+"px"
st.height=""+ convertHeight(o.offsetHeight)+"px"
st.fontSize=""+((size * getZoom()) / 100) +"pt"
st.display=""
updateParentIdRefBid(false,true)
_paramsEditInPlace=urlParamsNoBID()+"&bids="+bid;
_submitEditInPlace=true
_fEditIsShown=true
setTimeout("delayed_fEdit_show()",1);
}
}
function delayed_fEdit_show()
{
var fr = getReportFrame()
if (fr == null)
return
var fEdit=fr.document.getElementById(_editInPlace)
fEdit.select()
}
function fEdit_hide()
{
_fEditIsShown=false
var fr = getReportFrame()
if (fr == null) return
var fEdit=fr.document.getElementById(_editInPlace)
if (fEdit)
{
fEdit.style.display="none"
fEdit.isDisplayed=false
}
}
function fEdit_lostFocus()
{
_fEditIsShown=false
var fr = getReportFrame()
if (fr == null) return
var fEdit=fr.document.getElementById(_editInPlace)
if(fEdit && fEdit.isDisplayed)
{
if(_submitEditInPlace)
{
setTimeout("delayedValidateFormulaInPlace()",1)
}
fEdit_hide()
}
}
function fEdit_mouseDown(e)
{
if (_ie) {
var reportFrame=getReportFrame()
if (reportFrame == null) return
e=reportFrame.event
}
e.cancelBubble=true
}
function fEdit_mouseMove(e)
{
if (_ie)
{
var reportFrame=getReportFrame()
if (reportFrame == null) return
e=reportFrame.event
}
e.cancelBubble=true
return true
}
function fEdit_mouseUp(e)
{
if (_ie)
{
var reportFrame=getReportFrame()
if (reportFrame == null) return
e=reportFrame.event
}
e.cancelBubble=true
simulateClick(e)
return true
}
function fEdit_keyDown(e)
{
if (_ie) {
var reportFrame=getReportFrame()
if (reportFrame == null) return
e=reportFrame.event
}
var key=_ie?e.keyCode:e.which
if (key==13) 
{
fEdit_lostFocus()
}
else if (key==27) 
{
_submitEditInPlace=false
fEdit_hide()
}
e.cancelBubble=true
return true
}
function delayedValidateFormulaInPlace()
{
var reportFrame=getReportFrame()
if (reportFrame == null) return
var fEdit=reportFrame.document.getElementById(_editInPlace)
if(fEdit.oldValue==fEdit.value) return;
var p = _paramsEditInPlace 
if(p == null)
p=urlParams(false)
if (p!="")
{
self.document.viewerForm.action="processFormulaValidation"+_appExt+p+"&nAction=1&bEditInPlace=true"
self.document.viewerForm.target="DlgFrame"
self.document.viewerForm.sParam1.value=fEdit.value;
self.document.viewerForm.submit();
}
wt()
}
function dblClickTabWidget()
{
var id=this.id, tab = null, tabs=null
var fromDP=false, fromTabsZone=false
switch(id)
{
case "tabsDP":
case "tabsDP_rename":
fromDP=true
tabs=tabsDP
tab=tabsDP.items[tabsDP.getSelection().index]
break;
case "tabsZone":
case "tabsZone_rename":
if (!isInteractive)
return
fromTabsZone=true
tabs=tabsZone
tab=tabsZone.items[tabsZone.getSelection().index]
break;
default:
}
if(tab == null) return;
if( fromTabsZone || fromDP)
{
var tabEdit=document.getElementById(_editTabWidget)
if (tabEdit==null)
{
append(document.body,'<input ondragstart="return true" onselectstart="event.cancelBubble=true;return true" class="textinputs" type="text" id="'+_editTabWidget+'" style="display:none;zindex:200;position:absolute" maxlength="50">',document)
tabEdit=document.getElementById(_editTabWidget)
tabEdit.onkeydown=tabEdit_keyDown
tabEdit.onblur=tabEdit_lostFocus
tabEdit.onmousedown=tabEdit_mouseDown
}
var l = tab.midImgLayer
var st=tabEdit.style
st.left=""+(getPos(l).x-tabs.tabsLayer.scrollLeft)+"px"
st.top=""+(getPos(l).y + (_saf ? -3 : 0))+"px"
st.width=""+l.offsetWidth+"px"
st.height=""+(l.offsetHeight+(_saf ? 3 : -3))+"px"
st.display=""
___oldTabEdirValue=tab.name
tabEdit.value=tab.name
tabEdit.isDisplayed=true
_submitEditInPlace=true;
setTimeout("delayed_dblClickTabWidget()",1)
}
}
function delayed_dblClickTabWidget()
{
var tabEdit=document.getElementById(_editTabWidget)
tabEdit.select()
}
function tabEdit_hide()
{
var tabEdit=document.getElementById(_editTabWidget)
if (tabEdit)
{
tabEdit.style.display="none"
tabEdit.isDisplayed=false
}
}
function tabEdit_lostFocus()
{
var tabEdit=document.getElementById(_editTabWidget)
if(tabEdit && tabEdit.isDisplayed)
{
if(_submitEditInPlace)
tabEdit_submit()
tabEdit_hide()
}
}
function tabEdit_mouseDown(e)
{
if (_ie)
e=event
e.cancelBubble=true
}
function tabEdit_submit()
{
var tabEdit=document.getElementById(_editTabWidget)
var newName=tabEdit.value
if (newName!=___oldTabEdirValue) 
{
if (newName.length==0)
{
setTimeout('showAlertDialog("The name must contain at least one character.","Web Intelligence",0)',1);
return
}
if ((trim(newName)).length==0)
{
setTimeout('showAlertDialog("Report names of more than 31 characters or with : \\ / ? * [ ] characters or blanks are invalid in Microsoft Excel. This may cause an error if you save this document as Excel.","Report name",0)',1);
}
if(_showQueryPanel)
setTimeout("delayedRenameDPTabWidget()",1)
else
setTimeout("renameReport()",1)
}
}
function tabEdit_keyDown(e)
{
if (_ie)
e=event
var key=_ie?e.keyCode:e.which
if (key==13) 
{
tabEdit_lostFocus()
}
else if (key==27) 
{
_submitEditInPlace= false;
tabEdit_hide()
}
}
function goToProcessQueryPanel(url_params, DPName, frameName, entry)
{
var p=urlParams(true,true, entry?entry:null);
wt();
if (p!="")
{
var frm=self.document.viewerForm;
frm.action=_root+"processQueryPanel"+_appExt+p+url_params;
frm.target=frameName?frameName:"DlgFrame";
frm.sParam1.value=DPName ? DPName : "";
frm.submit();
}
}
function delayedRenameDPTabWidget()
{
goToProcessQueryPanel("&iDPIndex="+getSelectedDPIndex(), document.getElementById(_editTabWidget).value);
}
function beforeShowTabWidgetMenu()
{
var menu=tabsDP.getMenu()
var removemenu = menu.getItemByID("tabsDP_remove")
var movemenu = menu.getItemByID("tabsDP_move")
var first = false, last = false, oneDP = false
if(arrDPs.length == 1)
oneDP = true
movemenu.setDisabled(oneDP)
if(iDataProviderID==0)
first = true
else if(iDataProviderID==arrDPs.length-1)
last = true
var submenu = movemenu.sub 
submenu.getItem(0).setDisabled(first)
submenu.getItem(1).setDisabled(first)
submenu.getItem(2).setDisabled(last)
submenu.getItem(3).setDisabled(last)
var purgemenu = menu.getItemByID("tabsDP_purge");
var curDP=arrDPs[getSelectedDPIndex()];
if((curDP) && (((curDP.rows!=null) && (curDP.rows<1)) || (curDP.dpType == 1)))
purgemenu.setDisabled(true);
else
purgemenu.setDisabled(false);
var runmenu = menu.getItemByID("tabsDP_run");
var duplicatemenu=menu.getItemByID("tabsDP_duplicate");
if((curDP) && (curDP.dpType == 1))
{
runmenu.setDisabled(true);
duplicatemenu.setDisabled(true);
removemenu.setDisabled(true);
}
else
{
runmenu.setDisabled(false);
duplicatemenu.setDisabled(false);
removemenu.setDisabled(oneDP);
}
}
classNamesArr=new Array
codeArr=new Array
function getClassNameByLayer(l)
{
if (l.cdzClassNameCode!=null)
return classNamesArr[l.cdzClassNameCode]
else
{
var bid=l.getAttribute("bid"),elt=findByBID(window.doc,bid),className=elt?elt.className:""
var code=codeArr[className]
if (code==null)
{
code=classNamesArr.length
classNamesArr[code]=className
codeArr[className]=code
}
l.cdzClassNameCode=code
return className
}
}
function getValFromString(rot,att)
{
if (rot==null)
return null
var isNull=false
var subFields=att.split(".")
var subStr="rot"
var len=subFields.length
if ((subFields!=null)&&(len>0))
{
for (var j=1;j<len;j++)
{
subStr=subStr+"."+subFields[j]
if (eval(subStr)==null)
{
isNull=true
break
}
}
}
return isNull?null:(eval("rot"+att))
}
function getAttFromElts(att,elt,show)
{
var ret=null
var defined=true
var firstVal=null
var allNull=true
if (elt==null)
return {ret:ret,firstVal:firstVal,defined:defined,allNull:allNull}
for (var i in elt)
{
var e=elt[i]
firstVal=getValFromString(e,att)
if (e&&att&&(firstVal!=null))
{
allNull=false
break
}
}
if (allNull)
return {ret:ret,firstVal:firstVal,defined:defined,allNull:allNull}
allNull=false
for (var i in elt)
{
var isNull=false,val=null
if ((elt==null)||(elt[i]==null)||(att==null))
{
isNull=true
}
else
{
var oldAtt=att
if (elt.className=="block")
att=".block"+att
val=getValFromString(elt[i],att)
att=oldAtt
}
if ((val!=null)&&(ret==null)&&i==0)
ret=val
else
{
if (ret!=val)
{
ret=null
defined=false
return {ret:ret,firstVal:firstVal,defined:defined,allNull:allNull}
}
}
}
defined=true
return {ret:ret,firstVal:firstVal,defined:defined,allNull:allNull}
}
function get2AttsFromElts(att1, att2, elt,show)
{
var ret=null
var defined=true
var firstVal=null
var allNull=true
if (elt==null)
return {ret:ret,firstVal:firstVal,defined:defined,allNull:allNull}
for (var i in elt)
{
var e=elt[i]
firstVal=getValFromString(e,att1)
if (e && att1 && (firstVal!=null))
{
allNull=false
break
}
firstVal=getValFromString(e,att2)
if (e && att2 && (firstVal!=null))
{
allNull=false
break
}
}
if (allNull)
return {ret:ret,firstVal:firstVal,defined:defined,allNull:allNull}
allNull=false
for (var i in elt)
{
var isNull=false,val=null
if ((elt==null) || (elt[i]==null) || ((att1==null) && (att2==null)))
{
isNull=true
}
else
{
var oldAtt=att1
if (elt.className=="block")
att1=".block"+att1
val1=getValFromString(elt[i],att1)
att1=oldAtt
var oldAtt=att2
if (elt.className=="block")
att2=".block"+att2
val2=getValFromString(elt[i],att2)
att2=oldAtt
val = (val1!=null)? val1 : val2
}
if ((ret==null) && (val!=null))
ret=val
else
{
if ((ret!=null) && (ret != val))
{
ret=null
defined=false
return {ret:ret,firstVal:firstVal,defined:defined,allNull:allNull}
}
}
}
defined=true
return {ret:ret,firstVal:firstVal,defined:defined,allNull:allNull}
}
function setAlign(elt)
{
var res=getAttFromElts(".align.h",elt)
if (res.defined)
{
for (var i=0;i<3;i++)
{
var b=hAlign[i]
b.hasChanged=false
b.setDisabled(res.ret==null)
b.check(res.ret==i)
}
}
else
{
for (var i=0;i<3;i++)
{
var b=hAlign[i]
b.hasChanged=false
b.setDisabled(false)
b.check(false)
}
}
var res=getAttFromElts(".align.v",elt)
if (res.defined)
{
for (var i=0;i<3;i++)
{
var b=vAlign[i]
b.hasChanged=false
b.setDisabled(res.ret==null)
b.check(res.ret==i)
}
}
else
{
for (var i=0;i<3;i++)
{
var b=vAlign[i]
b.hasChanged=false
b.setDisabled(false)
b.check(false)
}
}
}
function setWrapText(elt)
{
var res=getAttFromElts(".align.wrap",elt)
if (res.defined)
{
wrapText.hasChanged=false
wrapText.setDisabled(res.ret==null)
wrapText.check(res.ret==1)
}
else
{
wrapText.hasChanged=false
wrapText.setDisabled(false)
wrapText.check(false)
}
}
function setFont(elt)
{
var res=getAttFromElts(".font.name",elt)
fontCombo.hasChanged=false
if (res.defined)
{
fontCombo.setDisabled(res.ret==null)
fontCombo.setUndefined(false)
if (res.ret!=null)
fontCombo.valueSelect(res.ret?res.ret:"")
}
else
{
fontCombo.setDisabled(false)
fontCombo.setUndefined(true)
}
res=getAttFromElts(".font.size",elt)
sizeCombo.hasChanged=false
if (res.defined)
{
sizeCombo.setDisabled(res.ret==null)
sizeCombo.setUndefined(false)
sizeCombo.valueSelect(res.ret?(""+res.ret):"")
}
else
{
sizeCombo.setDisabled(false)
sizeCombo.setUndefined(true)
}
res=getAttFromElts(".font.bold",elt)
bold.hasChanged=false
if (res.defined)
{
bold.setDisabled(res.ret==null)
bold.check(res.ret==1)
}
else
{
bold.setDisabled(false)
bold.check(false)
}
res=getAttFromElts(".font.italic",elt)
italic.hasChanged=false
if (res.defined)
{
italic.setDisabled(res.ret==null)
italic.check(res.ret==1)
}
else
{
italic.setDisabled(false)
italic.check(false)
}
res=getAttFromElts(".font.under",elt)
under.hasChanged=false
if (res.defined)
{
under.setDisabled(res.ret==null)
under.check(res.ret==1)
}
else
{
under.setDisabled(false)
under.check(false)
}
}
function setBorders(elts)
{
if ((elts == null)||(elts.length==0))
{
borderColor.setDisabled(true)
bordersBtn.setDisabled(true)
return
}
borderColor.hasChanged=false
borderColor.setDisabled(false)
var res=get2AttsFromElts(".border.color.rgb",".borders.topColor.rgb",elts);
var retTopColor = (res.defined)? res.ret:null;
res=get2AttsFromElts(".border.color.rgb",".borders.bottomColor.rgb",elts);
var retBottomColor = (res.defined)? res.ret:null;
res=get2AttsFromElts(".border.color.rgb",".borders.leftColor.rgb",elts);
var retLeftColor = (res.defined)? res.ret:null;
res=get2AttsFromElts(".border.color.rgb",".borders.rightColor.rgb",elts)
var retRightColor = (res.defined)? res.ret:null
if ((retTopColor==retBottomColor) && (retBottomColor==retLeftColor) && (retLeftColor==retRightColor))
borderColor.setColor(retTopColor);
else
borderColor.setColor(null);
var cn=elts[0].className
if ( (cn=='pageHeader') || (cn=='body') || (cn=='pageFooter') || (cn=='report') || (cn=='section') )
bordersBtn.setDisabled(true)
bordersBtn.setDisabled(false)
}
function setBackgroundImage(elts)
{
if ((elts == null)||(elts.length==0))
{
bgimgBtn.setDisabled(true)
return
}
bgimgBtn.setDisabled(false)
}
function flatFindbyId(arr,id)
{
if (arr!=null)
{
var len=arr.length
for (var i=0;i<len;i++)
{
var elem=arr[i]
if (elem.id==id)
return elem
}
}
return null
}
function getCellFormulaOrId(elt)
{
return elt ? ((elt.id&&(elt.id!="null"))?elt.id:elt.formula) : null
}
function isInAxis(axis,elt)
{
var arr=axis.vars
var id=getCellFormulaOrId(elt)
if (arr!=null)
{
var len=arr.length
for (var i=0;i<len;i++)
{
if (arr[i]==id)
return true
}
}
id=elt.name
if (arr!=null)
{
var len=arr.length
for (var i=0;i<len;i++)
{
if (arr[i]==id)
return true
}
}
return false
}
function getExtCells(elts)
{
if (elts.length==0)
return null
var ret = new Object
ret.tl = elts[0]
ret.rb = elts[0]
for (var i=1; i<elts.length; i++)
{
var e=elts[i];
if ((e.row <= ret.tl.row) && (e.col <= ret.tl.col))
ret.tl=e;
else 
{
var emaxRow=(e.rowSpan!=1)?(e.row+e.rowSpan-1):e.row;
var emaxCol=(e.colSpan!=1)?(e.col+e.colSpan-1):e.col;
var rbmaxRow=(ret.rb.rowSpan!=1)?(ret.rb.row+ret.rb.rowSpan-1):ret.rb.row;
var rbmaxCol=(ret.rb.colSpan!=1)?(ret.rb.col+ret.rb.colSpan-1):ret.rb.col;
if ((emaxRow >= rbmaxRow) && (emaxCol >= rbmaxCol))
ret.rb=e;
}
}
return ret;
}
function isInSelection(elts, rowBegin, rowEnd, colBegin, colEnd)
{
for (var i in elts)
{
var e = elts[i]
var minRow=e.row,maxRow=(e.rowSpan!=1)?(e.row+e.rowSpan-1):e.row
var minCol=e.col,maxCol=(e.colSpan!=1)?(e.col+e.colSpan-1):e.col
if ((minRow >= rowBegin && maxRow<=rowEnd) && (minCol>=colBegin && maxCol<=colEnd))
{}
else
return false;
}
return true;
}
function isContiguous(elts)
{
var ret=getExtCells(elts);
if (ret.rb==ret.tl) return false;
var rowEnd=(ret.rb.rowSpan!=1)?(ret.rb.row+ret.rb.rowSpan-1):ret.rb.row;
var colEnd=(ret.rb.colSpan!=1)?(ret.rb.col+ret.rb.colSpan-1):ret.rb.col;
var nSelElts=elts.length;
var nbrElts=(rowEnd - ret.tl.row +1)*(colEnd - ret.tl.col +1);
for (var i in elts)
{
var e = elts[i]
var minRow=e.row,maxRow=(e.rowSpan!=1)?(e.row+e.rowSpan-1):e.row
var minCol=e.col,maxCol=(e.colSpan!=1)?(e.col+e.colSpan-1):e.col
var nOccupiedCells=(maxRow-minRow+1)*(maxCol-minCol+1);
nbrElts=nbrElts-nOccupiedCells+1;
}
if (nSelElts!=nbrElts) return false;
return isInSelection(elts,ret.tl.row, rowEnd, ret.tl.col, colEnd)?true:false;
}
function setMergeOrSplit(elts)
{
if (elts==null)
{
mergeOrSplit.check(false)
mergeOrSplit.setDisabled(true)
return
}
var canMerge=false
var canSplit=false
var canMergeOrSplit=false
var len=elts.length
if ((len==1)&&(elts[0]))
{
var rowSpan=elts[0].rowSpan
var colSpan=elts[0].colSpan
canMerge=false
canSplit=( (rowSpan>1) || (colSpan>1) )
}
else if (len>1)
{
canSplit=false
var ref=elts[0]
var refZone=ref.parent,refBlock=refZone.parent
for (var i=0;i<elts.length;i++)
{
var e=elts[i]
var eZ=e.parent
var eB=eZ.parent
var cn=e.className
if ((cn!="tableCell") || ((e.block) && (e.block.blockType > 10)))
{
canMerge=false
break
}
if ( ( eZ != refZone ) || ( eB != refBlock ) )
{
canMerge = false
break
}
else
{
canMerge=true
continue
}
}
canMerge=canMerge&&isContiguous(elts)
}
canMergeOrSplit=canMerge||canSplit
mergeOrSplit.check(canSplit)
mergeOrSplit.setDisabled(!canMergeOrSplit)
var ret=new Object()
ret.canMergeOrSplit=canMergeOrSplit
ret.canSplit=canSplit
return ret
}
function setRemove(elts)
{
if ( (elts==null) || ((elts!=null)&&(elts.length==0)))
{
removeBut.setDisabled(true)
return
}
var len=elts.length
var bRemBut=false
var bMenu=false
if (len==1)
{
var cn=elts[0].className
if ( (cn=='pageHeader') || (cn=='body') || (cn=='pageFooter') || (cn=='report') )
bRemBut=true
if (cn!='tableCell')
bMenu=true
else
{
var rc=determineRemoveRowCol(elts[0])
removeBtnMenu.getItem(0).setDisabled(rc.bRow)
removeBtnMenu.getItem(1).setDisabled(rc.bCol)
bMenu=rc.bRow&&rc.bCol
}
}
else
{
bMenu=true
}
removeBut.setDisabled(bRemBut)
removeBut.disableMenu(bMenu)
}
function setFilter(elts)
{
_isQuickFilter=false
var hf=false, ok=false
if (_singleSel != null)
{
cn = _singleSel.className
switch (cn)
{
case "body":
hf = (_singleSel.parent && (_singleSel.parent.hf != null)) 
ok = true
break
case "report":
hf = (_singleSel.hf != null)
ok = true
break
case "block":
hf = (_singleSel.hf != null) 
ok = true
break
case "section":
hf = (_singleSel.hf != null) 
ok = true
break
case "tableCell":
_isQuickFilter=true
ok =(_singleSel.id!=null)&&(_singleSel.id!="null") 
if (ok && (elts[0].parent.parent.parent.hf != null))
{
var filteredExps = elts[0].parent.parent.parent.hf.split(","), id = _singleSel.id
for (var i=0; i < filteredExps.length; i++)
{
if (filteredExps[i] == id)
{
hf = true
break
}
}
}
break
case "reportCell":
_isQuickFilter=true
ok = _singleSel.isSect
hf = (_singleSel.parent.hf != null) 
break
}
} else if (elts && (elts.length>1)) {
if (isMultiTableCellInSameTable(elts)) {
ok=true
hf = (elts[0].parent.parent.parent.hf != null)
}
}
filterBut.setDisabled(!ok)
filterMenuItem.setDisabled(!ok)
butFilterMenuItem.setDisabled(!ok) 
filterMenuItem.setText(hf?"Edit Filter":"Add Filter")
butFilterMenuItem.setText(hf?"Edit Filter":"Add Filter")
removeFilterMenuItem.setDisabled(!hf)
butRemoveFilterMenuItem.setDisabled(!hf)
}
function updateCreationIcon(elt)
{
var res=getAttFromElts(".type",elt);
var src=_img+"formula.gif";
if (res.defined && res.ret!=null && res.ret==1)
{
if(!formulaEdition)
{
formulaEdition= true;
creationIcon.changeTooltip("Edit a variable");
creationIcon.changeImg(16*6,0,src);
}
}
else if(formulaEdition)
{
formulaEdition = false;
creationIcon.changeTooltip("Create a variable");
creationIcon.changeImg(16*5,0,src);
}
}
function setFormula(elts)
{
var elt=(elts&&(elts.length==1))?elts[0]:null
var res=(elt!=null)?((elt.className=="cell")?elt.fcell.formula:elt.formula):null
if (res!=null)
{
formulaText.setValue(res!=null?res:"")
formulaText.setDisabled(false)
creationIcon.setDisabled(false)
formulaIcon.setDisabled(false)
fOKBtn.setDisabled(false)
fCancelBtn.setDisabled(false)
updateCreationIcon(elts)
}
else
{
formulaText.setValue("")
formulaText.setDisabled(true)
fOKBtn.setDisabled(true)
fCancelBtn.setDisabled(true)
formulaIcon.setDisabled(true)
updateCreationIcon(elts)
}
}
function setColors(elts,bg,fg)
{
if (fg)
{
var res=getAttFromElts(fg,elts)
fgCombo.hasChanged=false
if (res.defined)
{
fgCombo.setDisabled(res.ret==null);
fgCombo.setColor(res.ret);
}
else
{
fgCombo.setDisabled(false)
fgCombo.setColor(null);
}
}
else
{
fgCombo.setDisabled(true)
}
if (bg)
{
if ((elts == null) || (elts.length == 0)){
bgCombo.setDisabled(true)
} else {
var res=getAttFromElts(bg,elts)
bgCombo.hasChanged=false
if (res.defined)
{
bgCombo.setDisabled(false);
bgCombo.setColor(res.ret);
} else {
bgCombo.setDisabled(false)
bgCombo.setColor(null);
}
}
} else {
bgCombo.setDisabled(true)
}
}
function setFormatPainter(elts)
{
if ((_singleSel != null) || (_tableBid != null) || (_formatPainterBid != null))
{
formatPainter.setDisabled(false)
}
else
{
formatPainter.setDisabled(true)
escapeFormatPainter()
}
}
function escKeyCB() 
{
escapeFormatPainter()
mouseOut()
}
function setLayering(elts,w)
{
if ( (elts == null) || (elts.length==0) || 
((elts != null) && (elts.length > 1) && !isMultiTableCellInSameTable(elts)) || 
((elts != null) && (elts.length ==1) && (elts[0].className!='tableCell') && (elts[0].className!='cell') && (elts[0].className!='block') && (elts[0].className!='reportCell')) )
{
w.setDisabled(true)
return
}
w.setDisabled(false)
}
function setAlignment(elts,w)
{
if ( (elts == null) || (elts.length==0) || ((elts.length==1)&&(elts[0].className!='cell')&&(elts[0].className!='reportCell')&&(elts[0].className!='tableCell')&&(elts[0].className!='block')) )
{
w.setDisabled(true)
return
}
w.setDisabled(false)
}
function getVariableQualification()
{
var elts = getSelectedElts(),qualif=""
var res=getAttFromElts(".qualification",elts)
if (res.defined && res.ret!=null)
qualif =  eval(res.ret)
return qualif
}
function getAssociatedDim()
{
var elts = getSelectedElts(),linkedDim=""
var res=getAttFromElts(".linkedDim",elts)
if (res.defined && res.ret!=null)
linkedDim =  res.ret
return linkedDim;
}
function getVariableDefinition()
{
var elts = getSelectedElts(),def=""
var res=getAttFromElts(".definition",elts)
if (res.defined && res.ret!=null)
def = res.ret
return def
}
function getVariableName()
{
var elts = getSelectedElts(),def="",endIdx=0
var res=getAttFromElts(".formula",elts)
if (res.defined && res.ret!=null)
{
def = res.ret
endIdx= def.length-1
if (endIdx>0) def=def.slice(2,endIdx)
}
return def
}
function getVariableDataType()
{
var elts = getSelectedElts(),def=""
var res=getAttFromElts(".dType",elts)
if (res.defined && res.ret!=null)
def = res.ret
return def
}
function getRow(elt)
{
var elts=null,def=""
if (elt!=null)
{
elts=new Array
elts[0]=elt
}
else 
elts=getSelectedElts()
var res=getAttFromElts(".row",elts)
if (res.defined && res.ret!=null)
def = res.ret
return def
}
function getCol(elt)
{
var elts=null,def=""
if (elt!=null)
{
elts=new Array
elts[0]=elt
}
else 
elts=getSelectedElts()
var res=getAttFromElts(".col",elts)
if (res.defined && res.ret!=null)
def = res.ret
return def
}
function getRowSpan()
{
var elts = getSelectedElts(),def=""
var res=getAttFromElts(".rowSpan",elts)
if (res.defined && res.ret!=null)
def = res.ret
return def
}
function getColSpan()
{
var elts = getSelectedElts(),def=""
var res=getAttFromElts(".colSpan",elts)
if (res.defined && res.ret!=null)
def = res.ret
return def
}
saveSEntry="",saveDPTab=null, savebUseQueryDrill=false;
mustFillTabsDP=true;
function getSelectedDPIndex()
{ 
var idx = tabsDP.getSelection().index; 
if(idx>-1 ) return idx  
else return 0
}
function setUnvDPParams()
{
self.document.viewerForm.sParam1.value="";
self.document.viewerForm.sParam2.value="";
    self.document.viewerForm.sParam3.value="";
var count= _queries.length
if (count>0)
{
var s2='['
for(var i=0;i<count;i++)
{
if(i>0)
s2+=","
s2+=encString(_queries[i].name)
}
s2+=']'
self.document.viewerForm.sParam2.value=s2
}
}
function getSelectedDPName()
{
return tabsDP.getSelection().name
}
function selectDP(bRefresh,DPDuplicated)
{
var index= getSelectedDPIndex()
if (index!=iDataProviderID || bRefresh==true)
{
iDataProviderID=index
var url = "queryPanel"+_appExt;
url += urlParams(true,true)
url += "&iDPIndex="+iDataProviderID
setUnvDPParams() 
document.viewerForm.target="querypanel"
document.viewerForm.action = url;
document.viewerForm.sParam3.value=DPDuplicated?DPDuplicated:"";
setTimeout("document.viewerForm.submit()",1);
pal5.disableChildren(true);
}
}
function renameDP(name,index)
{
if(index==null) index = iDataProviderID
var tab=tabsDP.items[index]
var oldName= tab.name
tab.change(name,tab.cb,null,_img+"standard.gif",16,16,16*(arrDPs[index].dpType?21:8),0,tab.dblclick)
for(var i in _queries )
{
if(_queries[i].name == oldName )
{
_queries[i].name = name
if(_queries[i].keydateValue != null)  
_queries[i].keydateValue[0]= name;
break;
}
}
}
function addDPTab(name,bDuplicate)
{
var obj = tabsDP.add(name,null,-1,_img+"standard.gif",16,16,16*(arrDPs[arrDPs.length-1].dpType?21:8),0)
var DPDuplicated = getSelectedDPName(); 
tabsDP.select(-1)
selectDP(null, bDuplicate?DPDuplicated:null)
}
function createDP(id)
{
goToProcessQueryPanel("&sDataSource="+convURL(id));
}
function moveDP(moveToIndex)
{
goToProcessQueryPanel("&iDPIndex="+getSelectedDPIndex()+"&iMoveTo="+moveToIndex);
}
function updateDPTab(selectIndex)
{
var items = tabsDP.items
for (var i in arrDPs)
{
var name = items[i].name
if(arrDPs[i].name != name)
{
var tab=tabsDP.items[i]
tab.change(arrDPs[i].name,tab.cb,null,_img+"standard.gif",16,16,16*(arrDPs[i].dpType?21:8),0,tab.dblclick)
}
}
tabsDP.select(selectIndex)
iDataProviderID = selectIndex
}
function deleteDP()
{
goToProcessQueryPanel("&iDPIndex="+iDataProviderID+"&bRemoveDP=true");
}
function deleteDPTab(indexToDelete)
{
var oldDPName = tabsDP.items[indexToDelete].name
var len=_queries.length
for(var i=0; i<len; i++ )
{
if(_queries[i].name == oldDPName )
{
if (i==len-1) _queries.length=len-1
else
{
var end=_queries.slice(i+1)
_queries.length=i
_queries=_queries.concat(end)
}
break;
}
}
tabsDP.remove(indexToDelete)
selectDP(true)
}
function writeTabsDP()
{
for (var i in arrDPs)
{
tabsDP.add(arrDPs[i].name,null,-1,_img+"standard.gif",16,16,16*(arrDPs[i].dpType?21:8),0)
}
tabsDP.select(0);
mustFillTabsDP=false;
storeReportQueryState();
}
function storeReportQueryState()
{
if(mustFillTabsDP) return;
if(saveDPTab==null) 
saveDPTab=new Array
else 
saveDPTab.length=0
var dp=null
for(var i in arrDPs)
{
dp=arrDPs[i]
saveDPTab[i]=newDPInfo(dp.name,dp.editable,dp.rows,dp.refreshable,dp.dpType)
}
saveSEntry= strEntry
savebUseQueryDrill = bUseQueryDrill;
}
closeQPObv=newObserverTwoEvents(_EVT_DOC_UPDATE,_EVT_PAGE_LOADED,closeQPCB)
function closeQPCB()
{
_showQueryPanel=false
switchToolbars()
bLaunchQP = false; 
}
function resetDPTab(arr,index)
{
tabsDP.removeAll()
for (var i in arr)
{
tabsDP.add(arr[i].name,null,-1,_img+"standard.gif",16,16,16*(arr[i].dpType?21:8),0)
}
tabsDP.select(index)
}
function resetQueries()
{
_queries.length=0
}
function resetQueriesFromUnv(id)
{
    var arr = getDPListFromUnv(id)
    var len1 = arr.length 
    if(len1 == 0) return
    for(var i=0;i<len1;i++)
    {        
        var len2 = _queries.length;
        for(var j=0;j<len2;j++)
        {
            if(arr[i].unvID == _queries[j].unvID )
            {
                arrayRemove(window,"_queries",j)
                break;
            }
        }
    }
}
function getDPListFromUnv(id)
{
    var arr = new Array
    if(id == null)  return arr; 
    var len = _queries.length
    for(var i=0;i<len;i++)
    {
        if(_queries[i].unvID == id)
            arr[arr.length]=_queries[i]        
    }
    return arr;
}
_globMoveTarget=null
function getCellZone(o,e,doc,vertOnly,topOnly,bottomOnly)
{
if (!isEnableUserRight(_usrUseFormatting))
return _cellContent
var marg=4
var z=getZoom()/100
var fr=getReportFrame()
var pos=getPos(o)
var x= (eventGetX(e,fr)-convertX(pos.x) + getScrollX(fr)) / z
var y= (eventGetY(e,fr)-convertY(pos.y) + getScrollY(fr)) / z
var w=o.offsetWidth,h=o.offsetHeight
vertOnly=vertOnly?true:false
bottomOnly=bottomOnly?true:false
if (vertOnly)
{
if (topOnly) {
if (y<(marg)) return _cellTop
} else if (bottomOnly) {
if (x>(w-marg))return _cellRight
if (y>(h-marg)) return _cellBottom
} else {
if (y>(h-marg)) return _cellBottom
if (y<(marg)) return _cellTop
}
}
else
{
if (x>(w-marg))return _cellRight
if (x<(marg)) return _cellLeft
if (y<(marg)) return _cellTop
if (y>(h-marg)) return _cellBottom
}
return _cellContent
}
function setCrs(o,zone)
{
var s=o.style
if (isPicker||_isDDEnabled||globResizeObj)
{
s.cursor="default";
return
}
if (globResizeObj==null)
{
switch(zone)
{
case _cellContent:
s.cursor="default";
break
case _cellRight:
s.cursor=_resizeW;
break
case _cellLeft:
s.cursor=_resizeW;
break
case _cellBottom:
s.cursor=_resizeH;
break
case _cellTop:
s.cursor=_resizeH;
break
}
} else {
s.cursor="default"
}
}
function drawResize(e)
{
var rWin=getReportFrame()
if (rWin == null) return
var rDoc=rWin.document, hasResized=false
var oldCurDoc=_curDoc
var oldCurWin=_curWin
_curDoc=rDoc
_curWin=rWin
var s=boresizediv.style
if (globResizeObj==null)
{
s.display="none"
}
else
{
var o=globResizeObj
var zone=o.downZone
var x=convertX(getPos(o).x)
var y=convertY(getPos(o).y)
var w=convertWidth(o.offsetWidth)+1
var h=convertHeight(o.offsetHeight)+1
var cn=getClassNameByLayer(o),z=getZoom()/100
if ((zone==_cellRight)||(zone==_cellLeft)||(zone==_cellTop)||(zone==_cellBottom))
{
if (zone==_cellRight) {
w=Math.max(_minResWidth*z,w+ (absxpos(e,z,rWin)-o.resizeZoneX)*z)
var pixPerfectW = 2
w = w - pixPerfectW
s.left=""+ (x + w) +"px"
s.top=""+getScrollY()+"px"
s.width="1px"
s.height=""+winHeight()+"px"
}
else if (zone==_cellLeft)
{
w = Math.max(_minResWidth*z, Math.min(absxpos(e,z,rWin)*z+getScrollX(),x+o.offsetWidth*z))
var pixPerfectW = 1
w = w - pixPerfectW
s.left=""+ w + "px"
s.top=""+getScrollY()+"px"
s.width="1px"
s.height=""+winHeight()+"px"
}
else if (zone==_cellBottom)
{
h=h+(absypos(e,z,rWin)-o.resizeZoneY)*z
if (o.isSection!=true)
h=Math.max(_minResHeight*z,h)
var pixPerfectH= 1
h = h - pixPerfectH
s.left=""+getScrollX()+"px"
s.top=""+(y+h + (o.isFooter?0:(o.isSectionOrHeaderOrFooter ? -2 : -1)))+"px"
s.width=""+winWidth()+"px"
s.height="1px"
}
else if (zone==_cellTop)
{
h=absypos(e,z,rWin)*z+getScrollY()
if (o.isSection!=true)
h = Math.max(_minResHeight*z, Math.min(h,y+o.offsetHeight*z))
var pixPerfectH= 1
h = h - pixPerfectH
s.left=""+getScrollX()+"px"
s.top=""+ h +"px" 
s.width=""+winWidth()+"px"
s.height="1px"
}
hasResized=true
s.display="block"
}
}
_curDoc=oldCurDoc
_curWin=oldCurWin
return hasResized
}
_isDDEnabled=false
_DDIconUrl=null
_DDIdx=0
_DDAction=null
_DDActionAdd=1
_DDActionTurnTo=2
function globmmove(e)
{
var fr=getReportFrame()
if (fr==null)
return true
if (_ie)
e=fr.event
if (e)
{
var z=getZoom()/100
if (!_isMoving) 
{
var o=globResizeObj
if (o)
{
if (o.mouseMoved==false)
{
var delta=0
if ((o.downZone==_cellRight) || (o.downZone==_cellLeft)) {
delta=Math.abs(absxpos(e,z,fr)-o.resizeZoneX)
} else if ((o.downZone==_cellTop) || (o.downZone==_cellBottom)) {
delta=Math.abs(absypos(e,z,fr)-o.resizeZoneY)
}
if (delta>2) {
o.mouseMoved=true
}
}
}
drawResize(e)
}
else 
{
if (globMoveObj!=null)
{
var o=globMoveObj
var oldWin=_curWin
var oldDoc=_curDoc
_curWin=fr
_curDoc=fr.document
var sX=getScrollX(fr),sY=getScrollY(fr)
_curWin=oldWin
_curDoc=oldDoc
showMoveZone(true, globMoveObj, eventGetX(e,fr) + sX,  eventGetY(e,fr) + sY, true)
if (_globMoveTarget!=null)
{
bid=_globMoveTarget.getAttribute("bid")
deselectAll(bid)
var elt = findByBID(doc, bid)
if (elt && elt.className == "block" && elt.children[0]) 
{            
var c= elt.children[0].children
for (var i in c)
{
var cc=c[i].children
for (var j in cc)
selectSingle(cc[j].bid)
}
} else {
selectSingle(bid)
}
_globMoveTarget=null
}
}
}
return false
}
return true
}
function globmup(e)
{
if (_ie) {
var fr = getReportFrame()
if (fr == null) return
e=fr.event
}
if (e && eventIsLeftButton(e,fr))
{
if (_isMoving)
{
_isMoving=false
globMoveObj=null
showMoveZone(false)
}
else
{
var o=globResizeObj
globResizeObj=null
drawResize(e)
triggerResize(o,e)
}
return _webKit ? true : false
}
else
globResizeObj=null
return true
}
function mmove(e)
{
var fr=getReportFrame()
if (fr==null)
return true
var o=this,e=_ie?fr.event:e,d=fr.document,cn=getClassNameByLayer(o)
if (_isDDEnabled && DDInitMethod)
{
DDInitMethod(o,cn,getCellZone(o,e,d))
var l= reportBorder.layer
setTooltipOffset(l.offsetLeft - (_saf ? getScrollX(fr) : 0) ,l.offsetTop - (_saf ? getScrollY(fr) : 0) )
newTooltipWidget().show(true, _DDTxt,_DDIconUrl,16,16,0,16*_DDIdx,false,e)
e.cancelBubble=true
o.style.cursor="default";
return false
}
else
{
newTooltipWidget().show(false)
}
if ((_isMoving)&&(globMoveObj!=null))
{
o.style.cursor="default";
if (_globMoveTarget==null)
{
switch (cn)
{
case "section":
case "pageHeader":
case "pageFooter":
case "body":
_globMoveTarget=this
break;
}
}
return true
}
if (_formatPainterBid != null) {
o.style.cursor="default";
var l= reportBorder.layer
setTooltipOffset(l.offsetLeft - (_saf ? getScrollX(fr) : 0),l.offsetTop - (_saf ? getScrollY(fr) : 0))
var iconUrl = _root + "/images/main/" + "formatPainter.gif"
newTooltipWidget().show(true, "(Presse Esc  to cancel this action)",iconUrl,16,16,0,16*_DDIdx,false,e)
setCrs(o,_cellContent)
return false
}
else
{
newTooltipWidget().show(false)
var w=null,onlyW=false,topOnly=false,bottomOnly=false
switch (cn)
{
case "section":
w=o
onlyW=true
bottomOnly=false
break
case "pageHeader":
case "pageFooter":
w=o
onlyW=true
topOnly=(cn=="pageFooter")
bottomOnly=(cn=="pageHeader")
break
case "tableCell":
onlyW=true
bottomOnly=true
w=o
break
case "reportCell":
case "cell":
w=o
break
case "block":
var bid=this.getAttribute("bid"),elt=findByBID(window.doc,bid)
if (elt&&(elt.block)&&(elt.block.blockType > 10)) {
w=o
}
break
}
if (w)
{
zone=getCellZone(w,e,d,onlyW,topOnly,bottomOnly)
if (cn=="section")
{
switch (zone)
{
case _cellBottom:
w.title="Resize bottom of section";
break;
case _cellTop:
w.title="Resize top of section";
break;
default:
w.title="";
break;
}
}
setCrs(w,zone)
return false
}
else
{
o.style.cursor="default";
return true
}
}
}
_triggerResizeURL=""
function triggerResizeYesCB()
{
frameNav("Report",_triggerResizeURL,true,null,true) 
}
function triggerLeftResize(o,delta,w,baseUrl)
{
if (baseUrl=="")
return
var bid=o.getAttribute("bid"),elt=findByBID(window.doc,bid),className=elt?elt.className:""
wt()
if (className != "tableCell")
{
var attach = elt.attach
if (attach)
{
if (attach.toH)
{
var cell=elt.cell?elt.cell:elt.block
var posInch = cell.unitIsInch ? cell.posH : (cell.posH / 25.4)
_triggerResizeURL="processResize"+_appExt+baseUrl+"&iW="+(w/_dpi)+"&sElemZone=left&iOldW="+(o.offsetWidth/_dpi)
if ((posInch + (delta/_dpi)) < 0)
{
_triggerResizeURL+="&bKeepAttach=false"
showPromptDialog("The table or chart that you have moved is positioned relative to another block, cell or section in the report. By moving it, you will disable the default relative positioning. Do you want to continue?","Confirm resize",_promptDlgWarning,triggerResizeYesCB,null)
return
}
else
{
_triggerResizeURL+="&bKeepAttach=true"
frameNav("Report",_triggerResizeURL,true,null,true) 
return
}
}
}
}
frameNav("Report","processResize"+_appExt+baseUrl+"&iW="+(w/_dpi)+"&bKeepAttach=true"+"&iOldW="+(o.offsetWidth/_dpi)+"&sElemZone=left",true,null,true) 
}
function triggerTopResize(o,delta,h,baseUrl)
{
if (baseUrl=="")
return
var bid=o.getAttribute("bid"),elt=findByBID(window.doc,bid),className=elt?elt.className:""
wt()
if (className != "tableCell")
{
var attach = elt.attach
if (attach)
{
if (attach.toV)
{
_triggerResizeURL="processResize"+_appExt+baseUrl+"&iH="+(h/_dpi)+"&sElemZone=top&iOldH="+(o.offsetHeight/_dpi)
var breakAttach=false
if (o.isSection)
{
breakAttach=false
}
else
{
var cell=elt.cell?elt.cell:elt.block
var posInch = cell.unitIsInch ? cell.posV : (cell.posV / 25.4)
breakAttach=(posInch + (delta/_dpi)) < 0;
}
if (breakAttach)
{
_triggerResizeURL+="&bKeepAttach=false"
showPromptDialog("The table or chart that you have moved is positioned relative to another block, cell or section in the report. By moving it, you will disable the default relative positioning. Do you want to continue?","Confirm resize",_promptDlgWarning,triggerResizeYesCB,null)
return
}
else
{
_triggerResizeURL+="&bKeepAttach=true"
frameNav("Report",_triggerResizeURL,true,null,true) 
return
}
}
}
}
frameNav("Report","processResize"+_appExt+baseUrl+"&iH="+(h/_dpi)+"&bKeepAttach=true"+"&iOldH="+(o.offsetHeight/_dpi)+"&sElemZone=top",true,null,true) 
}
function triggerResize(o,e)
{
if ((o!=null)&&(o.mouseMoved))
{
var z=getZoom()/100, p=urlParams(true)
var fr=getReportFrame()
if (o.downZone==_cellRight)
{
var w=o.offsetWidth, delta=absxpos(e,z,fr)-o.resizeZoneX
w=Math.max(_minResWidth, w + delta)
wt()
if (p!="")
frameNav("Report","processResize"+_appExt+p+"&iW="+(w/_dpi)+"&sElemZone=right",true,null,true)
}
else if (o.downZone==_cellLeft)
{
var w=o.offsetWidth, delta=absxpos(e,z,fr)-o.resizeZoneX, w=Math.min(-_minResWidth, delta - w)
triggerLeftResize(o,delta,w,p)
}
else if (o.downZone==_cellBottom)
{
var h=o.offsetHeight, delta=absypos(e,z,fr)-o.resizeZoneY
if (o.isFooter)
delta = -delta
var h=o.isSectionOrHeaderOrFooter?delta:Math.max(_minResHeight, h + delta)
wt()
if (p!="")
frameNav("Report","processResize"+_appExt+p+"&iH="+(h/_dpi)+"&sElemZone=bottom",true,null,true) 
}
else if (o.downZone==_cellTop)
{
var h=o.offsetHeight, delta=absypos(e,z,fr)-o.resizeZoneY
if (o.isFooter)
delta = -delta
var h=o.isSectionOrHeaderOrFooter?delta:Math.min(-_minResHeight, delta - h)
triggerTopResize(o,delta,h,p)
}
}
}
function goToProcessReportTab(iAction, notify, data, name, sTo)
{
if (notify!=null)
eventManager.notify(notify,data)
var p=urlParams(true,true)
wt()
if (p!="")
{
var frm=self.document.viewerForm
frm.action=_root+"processReportTabs"+_appExt+p+"&iAction="+iAction+(sTo!=null ? ("&sTo="+sTo) : "")
frm.target="DlgFrame"
frm.sParam1.value=name ? name : ""
frm.submit();
}
}
function renameReport()
{
goToProcessReportTab(0, _EVT_REP_RENAMED, iReportID, document.getElementById(_editTabWidget).value)
}
function deleteReport()
{
goToProcessReportTab(1, _EVT_REP_DELETED, iReportID)
}
function addReport()
{
goToProcessReportTab(2, _EVT_REP_ADDED, null, _defaultRepName)
}
function duplicateReport()
{
goToProcessReportTab(3, _EVT_REP_DUPLICATED, iReportID)
}
function moveReport(dir)
{
var dest=0;
if (dir=="first")
dest = 0;
else if ( dir=="previous" )
dest = parseInt(iReportID)-1;
else if ( dir=="next" )
dest = parseInt(iReportID)+1;
else if ( dir=="last" )
dest = arrReports.length-1;
var data = new Object()
data.from=iReportID
data.dest=dest
goToProcessReportTab(4, _EVT_REP_MOVED, data, null, dest)
}
function beforeShowTabsZoneWidgetMenu()
{
var menu=tabsZoneMenu
var removemenu = menu.getItemByID("tabsZone_delete")
var movemenu = menu.getItemByID("tabsZone_move")
var submenu = movemenu.sub
var m0=submenu.getItem(0)
var m1=submenu.getItem(1)
var m2=submenu.getItem(2)
var m3=submenu.getItem(3)
var selRep=tabsZone.getSelection().index
var rep=arrReports[selRep]
var drillmenu = menu.getItemByID("drill")
drillmenu.check((rep.repMode=='Analysis'))
initUserRight(drillmenu,_usrWorkInDrillMode)
initUserRight(subSaveComputerAS,_usrDownloadExcelOrPDF)
var insertmenu  = menu.getItemByID("tabsZone_add")
initUserRight(insertmenu,_usrInsertDuplicate)
var dupmenu  = menu.getItemByID("tabsZone_duplicate")
initUserRight(dupmenu,_usrInsertDuplicate)
initUserRight(removemenu,_usrInsertDuplicate)
var fmt=menu.getItemByID("cellFormatReport")
initUserRight(fmt,_usrUseFormatting)
menu.showCurrentReportChangesTabsZone.setDisabled(!_bTdcActivate)
menu.showCurrentReportChangesTabsZone.check(rep.sc)
initUserRight(menu.showCurrentReportChangesTabsZone,_usrEnableTrackDataChanges)
repairMenu(menu)
var nbRep=tabsZone.getCount()
if (nbRep==1)
{
removemenu.setDisabled(true)
movemenu.setDisabled(true)
m0.setDisabled(true)
m1.setDisabled(true)
m2.setDisabled(true)
m3.setDisabled(true)
return
}
removemenu.setDisabled(false)
movemenu.setDisabled(false)
if (selRep==0)
{
m0.setDisabled(true)
m1.setDisabled(true)
m2.setDisabled(false)
m3.setDisabled(false)
return
}
if (selRep==(nbRep-1))
{
m0.setDisabled(false)
m1.setDisabled(false)
m2.setDisabled(true)
m3.setDisabled(true)
return
}
m0.setDisabled(false)
m1.setDisabled(false)
m2.setDisabled(false)
m3.setDisabled(false)
}
_copyElemCN = null
function copyCutPasteCB(action)
{
if (!isEnableUserRight(_usrInsertDuplicate))
return
action = action?action:this.id
switch (action) {
case "copy" :
case "cut" :
var elts = getSelectedElts()
if (elts.length < 1) return
switch (elts[0].className) {
case "cell" :
case "block" :
case "reportCell" :
var elt = elts[0]
break
case "tableCell" :
var elt = elts[0].parent.parent.parent
break
default :
return
}
_targetCopyBid=elt.parent.bid
_canPaste=true
selectSingle(_targetCopyBid)
if ((window._copyX==null)||(window._copyY==null)||(window._copyRow==null))
{
window._copyX = toUserUnit(1, true)
window._copyY = toUserUnit(1, true)
window._copyRow = 0;
}
_setFocusToViewer=true
_copyElemCN = elt.className
var p=urlParamsNoBID()
p += "&sBid="+elt.bid
break
case "paste" :
if ((_copyElemCN != null) && (_targetCopyBid != null) && _canPaste) {
_setFocusToViewer=true
updateContainerIdRefBid(); 
var p=urlParamsNoBID()
p += "&sTargetBid="+escape(_targetCopyBid) + "&sX=" + _copyX + "&sY=" + _copyY + "&sUnitIsInch=" + _unitIsInch + "&sRow=" + _copyRow
}
break
default:
return
}
if (p) {
var url = _root + "processCopyPasteElement"+_appExt
url += p + "&sAction=" + action
wt()
setTimeout('frameNav("Report","' +  url + '")',1);
}
}
function addRow(dir)
{
var iWhere=getRow()
updateParentIdRefBid() 
var p=urlParams(true,false)
if (p!="")
frameNav("Report","processRowCol"+_appExt+p+"&iAction=0&sDir="+dir,true)
}
function addCol(dir)
{
var iWhere=getCol()
updateParentIdRefBid() 
var p=urlParams(true,false)
if (p!="")
frameNav("Report","processRowCol"+_appExt+p+"&iAction=2&sDir="+dir,true)
}
function insertRowCol()
{
var o=this,elts=getSelectedElts()
if (elts.length!=1)
return
var elt=elts[0]
if (elt==null)
return
var zone=elt.parent,block=zone.parent
switch(block.className)
{
case "vTable": addCol('right'); break
case "hTable": addRow('below'); break
case "crossTable":
{
switch(zone.className)
{
case "b,h-1": 
{
addCol('right');break
}
case "h-1,b": 
{
addRow('below');break
}
case "b": 
{
addRow('below');break
}
}
break
}
case "form": addRow('below'); break
}
}
function rmSect()
{
updateContainerIdRefBid(false);
var p=urlParams(false)
if (p!="")
frameNav("Report", _root +"processRemoveReportElements"+_appExt+p+"&objects=Section&sRefreshRMNode=yes",true)
}
var _usrRMSect=null;
var arrRMSecElts=new Array();
var iRMSectTimeoutID=null;
function sectYesCB()
{
_usrRMSect=true
}
function sectNoCB()
{
_usrRMSect=false
}
function waitRMSectionsAnswer(i, show)
{
if (i>=arrRMSecElts.length) 
{
arrRMSecElts.length=0;
doRemoveReportElement();
return;
}
var func;
if ((_usrRMSect==null) && !show)
{
var e=arrRMSecElts[i];
var sectionName="";
var msg="";
var title="";
var isSection=(e.className=="reportCell")?false:true;
if (!isSection)
{
sectionName=e.formula;
}
else
if (e.children && e.children[0] && e.children[0].className=="reportCell" && e.children[0].formula)
{
sectionName=e.children[0].formula;
}
if (sectionName!="")
{
var idxBegin=sectionName.indexOf("=[");
var idxEnd=sectionName.indexOf("]");
if ((idxBegin >=0) && (idxEnd>0) && (idxEnd>idxBegin))
sectionName=sectionName.substring(idxBegin+2, idxEnd);
}
if (!isSection)
{
msg="You are deleting a section master cell. Do you want to delete the associated section on " + " "+sectionName+"?";
title="Confirm Delete";
}
else
{
msg="Are you sure you want to delete this section?";
title="Confirm Delete"+" "+sectionName;
}
showPromptDialog(msg,title,1,sectYesCB,sectNoCB);
func="waitRMSectionsAnswer("+i+",true)";
iRMSectTimeoutID=setTimeout(func,1);
return;
}
else
if (_usrRMSect!=null && show)
{
clearTimeout(iRMSectTimeoutID);
var e=arrRMSecElts[i];
var bid=(e.className=="reportCell")?e.parent.bid:e.bid;
if (_usrRMSect==true)
{
selectedBid[bid]=1;
}
else
if (selectedBid[bid])
{
selectedBid[bid]=null;
}
i++;
_usrRMSect=null;
if (i>=arrRMSecElts.length) 
{
arrRMSecElts.length=0;
doRemoveReportElement();
return;
}
func="waitRMSectionsAnswer("+i+",false)";
iRMSectTimeoutID=setTimeout(func,1);
return;
}
func="waitRMSectionsAnswer("+i+",true)";
iRMSectTimeoutID=setTimeout(func,1);
return;
}
function removeReportElement()
{
var elts=getSelectedElts();
if (elts.length==0) return;
var arrSections=new Array();
arrRMSecElts.length=0;
var k=0;
for (var i in elts)
{
var e=elts[i];
if (e.className=="section")
{
if (typeof(arrSections[e.bid])=="undefined")
{
arrRMSecElts[k]=e;
arrSections[e.bid]=1;
k++;
}
}
else
if ((e.className=="reportCell") && e.isSect && e.parent && (e.parent.className=="section"))
{
if (typeof(arrSections[e.parent.bid])=="undefined")
{
arrSections[e.parent.bid]=1;
arrRMSecElts[k]=e;
k++;
}
}
}
if (arrRMSecElts.length==0)
doRemoveReportElement();
else
{
_usrRMSect=null;
waitRMSectionsAnswer(0, false);
}
}
function doRemoveReportElement(i,sObjects)
{
if (i==null)
{
i=0
}
if (sObjects==null)
sObjects=''
var elts=getSelectedElts()
if (elts.length==0)
return
var e=elts[i],cn=e.className
var objType='',index=-1
switch (cn)
{
case "section":
objType='Section'
break
case "block":
objType='Table'
if ((e.block) && (e.block.blockType > 10) )
objType='Chart'
break
case "reportCell":
case "cell":
objType='Cell'
break
case "tableCell":
var zone=e.parent,block=zone.parent
switch(block.className)
{
case "crossTable":
{
objType='Col';
if (zone.className)
{
switch (zone.className.charAt(0))
{
case 'h':
case 'f':
objType='Row';
break;
case 'b':
objType='Col';
break;
}
}
break;
}
case "vTable":
objType='Col'
break
case "form":
case "hTable":
objType='Row'
break
}
break
}
if (objType!='')
{
if (sObjects!='')
sObjects+=','
sObjects+=objType
}
else
selectedBid[e.bid]=null
if (i<(elts.length-1))
{
i++
doRemoveReportElement(i,sObjects)
}
else
{
if ( sObjects=='' )
return
if(elts.length > 1 ) 
{
if(isMultiTableCellInSameTable(elts))
{
updateParentIdRefBid() 
}
else
{
_curIdRef = null; 
}
}
else
{
if(objType == 'Section')
updateContainerIdRefBid(false);
else
updateParentIdRefBid(true) 
}
var p=urlParams(false,false)
if (p!="")
frameNav("Report", _root +"processRemoveReportElements"+_appExt+p+"&objects="+sObjects,true)
}
}
function applyDefaultCalculation()
{
var elts=getSelectedElts()
if ( (elts==null) || ((elts!=null)&& (elts.length!=1)) )
return
var elt=elts[0]
if ( elt.dType==1 )
defaultAction = "sum"
else
defaultAction = "count"
if ( elt.qualification=="_msr" && elt.aggregateFct=="DELEGATED")
defaultAction = "total"
var zone=elt.parent,block=zone.parent,calc=null
switch(block.className)
{
case "vTable": calc=elt.vcalc; break
case "hTable": calc=elt.hcalc; break
case "crossTable":
{
switch(zone.className)
{
case "b,h-1": 
calc=elt.vcalc;break
case "h-1,b": 
calc=elt.hcalc;break
case "b": 
calc=o.id=="extracalcMenu"?elt.vcalc:elt.hcalc;break
}
}
break
}
var c=new Array(0,0,0,0,0,0,0)
if (calc)
{
var vals=calc.split(",")
for (var i in vals)
c[parseInt(vals[i])]=1
}
var sCalcAction=null
if ( ( defaultAction =="sum" ) && ( !c[0] ) )
sCalcAction="calcSum"
if ( ( defaultAction =="count" ) && ( !c[1] ) )
sCalcAction="calcCount"
if ( ( defaultAction =="total" ) && ( !c[6] ) )
sCalcAction="calcTotal"
if (sCalcAction != null)
{
wt()
var p=urlParams(true)
if (p!="")
frameNav("Report","processCalc"+_appExt+p+"&sCalcAction="+sCalcAction,true)
}
}
_globColorObj=newBOColor(0,0,0)
function toolBarColorPickCB(c)
{
_currColorCombo.setColor(c);
_currColorCombo.setDefaultActionColor(c);
_currColorCombo.hasChanged=true;
addLastUsedColor(c);
if (_appFmt)
{
applyClickCB()
wt()
}
else
setApplyToolTip()
}
function setApplyToolTip()
{
var applyTooltip = "\n"
var tab = "    "
if (fontCombo.hasChanged)
{
applyTooltip += tab + "Name:\t\t"+ fontCombo.getSelection().value + "\n"
}
if (sizeCombo.hasChanged)
{
applyTooltip += tab + "Size:\t\t\t"+ sizeCombo.getSelection().value + "\n"
}
if (bold.hasChanged || italic.hasChanged || under.hasChanged)
{
applyTooltip += tab + "Style:\t\t"
applyTooltip += (bold.hasChanged)? (bold.isChecked()? "Bold ":"") : ""
applyTooltip += (italic.hasChanged)? (italic.isChecked()? "Italic ":"") : ""
applyTooltip += (under.hasChanged)? (under.isChecked()?"Underline":"") : ""
applyTooltip += "\n"
}
if (fgCombo.hasChanged)
{
applyTooltip += tab + "Font color:\t\t"+ fgCombo.getDefaultActionColor() + "\n"
}
if ((hAlign[0].hasChanged)||(hAlign[1].hasChanged)||(hAlign[2].hasChanged))
{
applyTooltip += tab + "Alignment:\t\t"+ (hAlign[0].isChecked()? "Left":(hAlign[1].isChecked()? "Center":"Right")) + "\n"
}
if ((vAlign[0].hasChanged)||(vAlign[1].hasChanged)||(vAlign[2].hasChanged))
{
applyTooltip += tab + "Alignment:\t\t"+ (vAlign[0].isChecked()? "Top":(vAlign[1].isChecked()? "Middle":"Bottom")) + "\n"
}
if (wrapText.hasChanged)
{
applyTooltip += tab + "Wrap text\t"+ (wrapText.isChecked()? "Wrap text":"Do not wrap text")  + "\n"
}
if (bgCombo.hasChanged)
{
applyTooltip += tab + "Background color:\t"+ bgCombo.getDefaultActionColor() + "\n"
}
if (borderColor.hasChanged)
{
applyTooltip += tab + "Border color:\t\t"+ borderColor.getDefaultActionColor() + "\n"
}
if (mergeOrSplit.hasChanged)
{
applyTooltip += tab + (mergeOrSplit.isChecked()?"Merge\t\t":"Split\t\t\n")
}
applyFormatBt.setTooltip(" Click Apply to change format settings on selection: " + applyTooltip)
applyFormatBt.setDisabled(applyTooltip == "")
}
function applyClickCB()
{
var s=""
if (fontCombo.hasChanged)
{
s+="&f="+escape(fontCombo.getSelection().value) 
fontCombo.hasChanged=false
}
if (sizeCombo.hasChanged)
{
s+="&s="+sizeCombo.getSelection().value
sizeCombo.hasChanged=false
}
if (bold.hasChanged)
{
s+="&b="+(bold.isChecked()?1:0)
bold.hasChanged=false
}
if (italic.hasChanged)
{
s+="&i="+(italic.isChecked()?1:0)
italic.hasChanged=false
}
if (under.hasChanged)
{
s+="&u="+(under.isChecked()?1:0)
under.hasChanged=false
}
if ((hAlign[0].hasChanged)||(hAlign[1].hasChanged)||(hAlign[2].hasChanged))
{
s+="&a="+(hAlign[0].isChecked()?"0":(hAlign[1].isChecked()?"1":"2"))
for (var i=0;i<3;i++)
hAlign[i].hasChanged=false
}
if ((vAlign[0].hasChanged)||(vAlign[1].hasChanged)||(vAlign[2].hasChanged))
{
s+="&va="+(vAlign[0].isChecked()?"0":(vAlign[1].isChecked()?"1":"2"))
for (var i=0;i<3;i++)
vAlign[i].hasChanged=false
}
if (wrapText.hasChanged)
{
s+="&wt="+(wrapText.isChecked()?"1":"0")
wrapText.hasChanged=false
}
if (bgCombo.hasChanged)
{
var color=bgCombo.getDefaultActionColor()
addLastUsedColor(color)
if (color==null)
color="-2,-2,-2"
s+="&bg="+convURL(color)
bgCombo.hasChanged=false
}
if (fgCombo.hasChanged)
{
var color=fgCombo.getDefaultActionColor()
addLastUsedColor(color)
if (color==null)
color="-2,-2,-2"
s+="&fg="+convURL(color)
fgCombo.hasChanged=false
}
if (borderColor.hasChanged)
{
var color=borderColor.getDefaultActionColor()
addLastUsedColor(color)
if (color==null)
color="-2,-2,-2"
s+="&bc="+convURL(color)
borderColor.hasChanged=false
}
if (mergeOrSplit.hasChanged)
{
s+="&bMerge="+mergeOrSplit.isChecked()
mergeOrSplit.hasChanged=false
if(mergeOrSplit.isChecked()) 
updateParentIdRefBid();
}
if (s=="") return
var p=urlParams(false)
wt()
if (p!="")
frameNav("Report", _root + "processFormatToolbar"+_appExt+p+s,true)
}
function initCurrentBlockType(intTypeToXYType, sBid) 
{
var blockElt=null
var blockType=-1
if (doc&&(sBid==null))
{
if (selectedBid)
{
for (var i in selectedBid)
{
if (selectedBid[i]!=null)
{
sBid=""+i
break
}
}
}
}
if (sBid!="")
{
var elt = findByBID(doc,sBid)
if (elt)
{
if (elt.className=="block") {
blockElt=elt
} else if (elt.className=="tableCell") {
blockElt=elt.parent.parent.parent
}
if (blockElt) {
applyBid = blockElt.bid
blockType=blockElt.block.blockType
nDimDetails=blockElt.block.nDimDetails
nMeasures=blockElt.block.nMeasures
}
}
}
var a = new Array
a = intTypeToXYType[blockType]
if (a)
{
curBlockType = a[0]
curBlockSubType = a[1]
}
}
function canApplyTurnTo(selBlockType, selBlockSubType, nDimDetails, nMeasures)
{
if (selBlockType == -1)
return false
if (selBlockSubType == -1) 
return false
if ((selBlockType == curBlockType) && (selBlockSubType == curBlockSubType)) 
return false
if (selBlockType == 0) 
return true;
if (selBlockType == 4) 
return ((nDimDetails >= 1) && (nMeasures == 1))
if ((selBlockType == 5) && (selBlockSubType >= 2)) 
return ((nDimDetails >= 0) && (nMeasures >= 2))
return ((nDimDetails >= 1) && (nMeasures >= 1))
}
function selectNewBlock()
{
_globalWOMCallback = selectNewBlock_globalWOMCallback
oldEntry = strEntry
}
function selectNewBlock_globalWOMCallback()
{
_globalWOMCallback = null
if (sNewChartBID == "") return
changeEntry(strEntry)
_newBlockBid = parseInt(sNewChartBID)
    deselectAll(_newBlockBid)
    selectSingle(_newBlockBid)
    var url = "language/"+_lang+"/html/applyFormatChartDialog.html?gotoPivot=yes&sBid=" + _newBlockBid
frameNav('DlgFrame', url)
}
function setLayeringLevelTo(level)
{
if (level==null)
return
var p=urlParams(true)
if (p!="")
frameNav("Report","processLayering"+_appExt+p+"&level="+level,true)
}
function align_getEltWidthFromHTMLContentInPx(bid)
{
var obj=bidTable[bid];
return obj?obj[0].offsetWidth:0;
}
function align_getEltHeightFromHTMLContentInPx(bid)
{
var obj=bidTable[bid];
return obj?obj[0].offsetHeight:0;;
}
function align_getTableWidthInMM(elt)
{
if (!elt) return 0;
var type=elt.children[0].className;
var w=0;
if (type=='vTable')
{
var v_table=elt.children[0].children[2];
for (var k=0; k<v_table.colCount; k++)
{
var cell=v_table.children[k];
if (cell.className=='tableCell')
{
w+=align_getEltWidthFromHTMLContentInPx(cell.bid);
}
}
}
else
if (type=='hTable')
{
}
else
if (type=='form')
{
var form=elt.children[0].children[0];
for (var k=0; k<form.colCount; k++)
{
var cell=form.children[k];
if (cell.className=='tableCell')
{
w+=align_getEltWidthFromHTMLContentInPx(cell.bid);
}
}
var obj=bidTable[elt.bid];
if (obj)
{
w=w+2*Number(obj[0].cellSpacing);
}
}
return w*25.4/_dpi;
}
function align_getTableHeightInMM(elt)
{
if (!elt) return 0;
var type=elt.children[0].className;
var h=0;
if (type=='vTable')
{
}
else
if (type=='hTable')
{
var h_table=elt.children[0].children[2];
for (var k=0; k<h_table.rowCount; k++)
{
var cell=h_table.children[k];
if (cell.className=='tableCell')
{
h+=align_getEltHeightFromHTMLContentInPx(cell.bid);
}
}
}
else
if (type=='form')
{
}
return h*25.4/_dpi;
}
function align_getReportEltWidthInMM(elt)
{
if (!elt) return 0;
var w=0;
switch (elt.className)
{
case 'reportCell':
case 'cell':
{
{
w=align_getEltWidthFromHTMLContentInPx(elt.bid);
w*=25.4/_dpi;
}
break;
}
case 'block':
{
if ( elt.block.blockType > 10) 
{
if (elt.graph)
{
w=elt.graph.width;
if (elt.graph.unitIsInch)
w=w*25.4;
}
}
else
w=align_getTableWidthInMM(elt);
break;
}
case 'tableCell':
{
w=align_getTableWidthInMM(elt.parent.parent.parent);
break;
}
}
return w;
}
function align_getReportEltHeightInMM(elt)
{
if (!elt) return 0;
var h=0;
switch (elt.className)
{
case 'reportCell':
case 'cell':
{
{
h=align_getEltHeightFromHTMLContentInPx(elt.bid);
h*=25.4/_dpi;
}
break;
}
case 'block':
{
if ( elt.block.blockType > 10) 
{
if (elt.graph)
{
h=elt.graph.height;
if (elt.graph.unitIsInch)
h*=25.4;
}
}
else
h=align_getTableHeightInMM(elt);
break;
}
case 'tableCell':
{
h=align_getTableHeightInMM(elt.parent.parent.parent);
break;
}
}
return h;
}
function align_getAbosoluteXinMM(elt)
{
if (!elt) return 0;
var x =0;
switch (elt.className)
{
case 'reportCell':
case 'cell':
{
x=elt.cell.posH;
if (elt.cellsize.unitIsInch)
x=x*25.4;
break;
}
case 'block':
{
x=elt.block.posH;
if (elt.block.unitIsInch)
x=x*25.4;
break;
}
}
if (elt.attach)
{
var attachBid=elt.attach.toH;
if (attachBid!='')
{
var attachElt = findByBID(doc, attachBid);
switch (elt.attach.h)
{
case 1:
x+=align_getAbosoluteXinMM(attachElt);
break;
case 2:
x+=align_getAbosoluteXinMM(attachElt)+align_getReportEltWidthInMM(attachElt);
break;
}
}
}
return x;
}
function align_getAbosoluteYinMM(elt)
{
if (!elt) return 0;
var y =0;
switch (elt.className)
{
case 'reportCell':
case 'cell':
{
y=elt.cell.posV;
if (elt.cellsize.unitIsInch)
y=y*25.4;
break;
}
case 'block':
{
y=elt.block.posV;
if (elt.block.unitIsInch)
y=y*25.4;
break;
}
}
if (elt.attach)
{
var attachBid=elt.attach.toV;
if (attachBid!='')
{
var attachElt = findByBID(doc, attachBid);
switch (elt.attach.v)
{
case 1:
y+=align_getAbosoluteYinMM(attachElt);
break;
case 2:
y+=align_getAbosoluteYinMM(attachElt)+align_getReportEltHeightInMM(attachElt);
break;
}
}
}
return y;
}
_alignURL=''
function setAlignmentTo(alignment)
{
_alignURL=''
if (alignment==null)
return
var elts=getSelectedElts()
if (alignment=='relativePos')
{
var elt=elts[0]
switch (elt.className)
{
case 'reportCell':
case 'cell':
_fcDlgSelectedPane = 6
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatCellDialog.html")
break
case 'block':
if (elt.block.blockType>10)
{
_fchartDlgSelectedPane = 2
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatChartDialog.html")
}
else
{
_ftDlgSelectedPane = 3
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatTableDialog.html")
}
break
case 'tableCell':
var pp=elt.parent.parent
if (pp==null)
return
if ( (pp.className=='hTable')||(pp.className=='vTable')||(pp.className=='crossTable') ||(pp.className=='form'))
{
_ftDlgSelectedPane = 3
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatTableDialog.html")
}
break
}
return
}
var s='', arr=new Array, attach=false
var arrSelected=new Array;
var HoriDirection=false;
switch(alignment)
{
case "left":
case "center":
case "right":
HoriDirection=true;
break;
case "top":
case "middle":
case "bottom":
HoriDirection=false;
break;
}
for (var i=0;i<elts.length;i++)
{
var found=false,elt=elts[i]
if (elt.className=='tableCell')
elt=elt.parent.parent.parent
var bid=elt.bid
for (var j=0;j<arr.length;j++)
{
if (arr[j]==bid)
{
found=true
break
}
}
if (!found)
{
arr[arr.length]=bid
if (s!='')
s+=','
s+=bid
arrSelected[arrSelected.length]=elt;
}
if (elt.attach)
{
var isAttached=((HoriDirection&&elt.attach.toH) || (!HoriDirection&&elt.attach.toV))
attach=attach||isAttached
}
}
var p=urlParamsNoBID()
if (p=="")
return
p+="&bids="+escape(s)+"&alignment="+alignment
var sWidth='';
var sHeight='';
var arrWidth=new Array;
var arrHeight=new Array;
if (alignment=='center' || alignment=='right')
{
for (var i=0;i<arrSelected.length;i++)
{
var elt=arrSelected[i];
var w=align_getReportEltWidthInMM(elt);
if (w>0)
{
if (sWidth!='')
sWidth+=',';
sWidth+=w;
arrWidth[arrWidth.length]=w;
}
}
p+="&sAlignW="+escape(sWidth);
}
else
if (alignment=='middle' || alignment=='bottom')
{
for (var i=0;i<arrSelected.length;i++)
{
var elt=arrSelected[i];
var h=align_getReportEltHeightInMM(elt);
if (h>0)
{
if (sHeight!='')
sHeight+=',';
sHeight+=h;
arrHeight[arrHeight.length]=h;
}
}
p+="&sAlignH="+escape(sHeight)
}
var leftMost=0;
if ( alignment=='left' || alignment=='center')
{
var left=0;
for (var i=0;i<arrSelected.length;i++)
{
left=align_getAbosoluteXinMM(arrSelected[i]);
if (leftMost==0)
leftMost=left;
else
{
if (left<leftMost)
leftMost=left;
}
}
p+="&align_leftMost="+leftMost;
}
var rightMost=0;
if ( alignment=='right' || alignment=='center')
{
var right=0;
for (var i=0;i<arrSelected.length;i++)
{
right=align_getAbosoluteXinMM(arrSelected[i])+arrWidth[i];
if (right>rightMost)
rightMost=right;
}
p+="&align_rightMost="+rightMost;
}
var topMost=0;
if ( alignment=='top' || alignment=='middle')
{
var top=0;
for (var i=0;i<arrSelected.length;i++)
{
top=align_getAbosoluteYinMM(arrSelected[i]);
if (topMost==0)
topMost=top;
else
{
if (top<topMost)
topMost=top;
}
}
p+="&align_topMost="+topMost;
}
var bottomMost=0;
if ( alignment=='middle' || alignment=='bottom')
{
var bottom=0;
for (var i=0;i<arrSelected.length;i++)
{
bottom=align_getAbosoluteYinMM(arrSelected[i])+arrHeight[i];
if (bottom>bottomMost)
bottomMost=bottom;
}
p+="&align_bottomMost="+bottomMost;
}
_alignURL=p
if (attach)
showPromptDialog("One selected table or chart selectioned is positioned relative to another block, cell, or section. By moving it you will disable this relative positioning. Do you want to continue ?","Align",1,processAlignment)
else
frameNav("Report","processAlignment"+_appExt+_alignURL,true)
}
function processAlignment()
{
frameNav("Report","processAlignment"+_appExt+_alignURL+"&bdeleteAttach=true",true)
}
function processBorders()
{
var ret=bordersBtn.getSelectedBorder()
if ( ret==null )
return
var elts=getSelectedElts()
if ( ret.top == -2 )
{
var elt=elts[0]
switch (elt.className)
{
case 'reportCell':
case 'cell':
_fcDlgSelectedPane = 4
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatCellDialog.html")
break
case 'block':
if (elt.block.blockType>10)
{
_fchartDlgSelectedPane = 0
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatChartDialog.html")
}
else
{
var type=elt.children[0].className;
if ( (type=='hTable')||(type=='vTable')||(type=='crossTable')||(type=='form') )
{
_ftDlgSelectedPane = 1
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatTableDialog.html")
}
}
break
case 'tableCell':
var pp=elt.parent.parent
if (pp==null)
return
if ( (pp.className=='hTable')||(pp.className=='vTable')||(pp.className=='crossTable') ||(pp.className=='form'))
{
_fcDlgSelectedPane = 4
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatCellDialog.html")
}
break
}
return
}
var s=getBIDsWithoutEmbeddedTableCells(elts)
if (s=='')
return
var p=urlParamsNoBID()
if (p=="")
return
p+="&bids="+escape(s)
p+='&tb='+ret.top
p+='&bb='+ret.bot
p+='&lb='+ret.left
p+='&rb='+ret.right
frameNav("Report","processFormatToolbar"+_appExt+p,true)
}
function getBIDsWithoutEmbeddedTableCells(elts)
{
var arrTableBID=new Array
for (var i=0;i<elts.length;i++)
{
var elt=elts[i]
if (elt.className=='block')
{
var c0=elt.children[0]
if ( c0 && ((c0.className=='hTable') || (c0.className=='vTable') || (c0.className=='crossTable') || (c0.className=='form')) )
arrTableBID[arrTableBID.length]=elt.bid
}
}
var s=''
for (var i=0;i<elts.length;i++)
{
var elt=elts[i], found=false
if (elt.className=='tableCell') 
{
var pp=elt.parent.parent.parent
for (var j=0; j<arrTableBID.length;j++)
{
if ( pp.bid == arrTableBID[j] )
found=true
}
if (!found)
{
if (s!='')
s+=','
s+=elt.bid
}
}
else
{
if (s!='')
s+=','
s+=elt.bid
}
}
return s
}
function processBackgroundImage()
{
var elts=getSelectedElts()
var elt=elts[0]
switch (elt.className)
{
case 'tableCell':
case 'reportCell':
case 'cell':
_fcDlgSelectedPane = 5
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatCellDialog.html")
break
case 'section':
_fsectDlgSelectedPane = 1
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatSectionDialog.html")
break
case 'block':
var c0=elt.children[0]
if (c0==null)
return
if ( (c0.className=='hTable')||(c0.className=='vTable')||(c0.className=='crossTable') )
{
_ftDlgSelectedPane = 2
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatTableDialog.html")
}
break
case 'pageHeader':
_frDlgSelectedPane = 2
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatReportDialog.html")
break
case 'pageFooter':
_frDlgSelectedPane = 3
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatReportDialog.html")
break
case 'body':
_frDlgSelectedPane = 1
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatReportDialog.html")
break
}
return
}
function editClickCB(o,id)
{
switch(id)
{
case "docProps":
wt()
frameNav("DlgFrame", _root + "language/"+_lang+"/html/documentPropertiesDialog.html")
break;
case "closeQP":
if(_isQPModified)
{
showPromptDialog("Are you sure you want to close without saving the last modifications ?",null,1,revertChangesAndCloseQP);
}
else
{
revertChangesAndCloseQP();
}
break;
case "applyClose":
var f = getQueryFrame();
if(f != null) f.submitQueries("generatequery")
break;
case "revertClose": 
revertChangesAndCloseQP();
break;
case "runquery":
case "runqueries":
runDocument(null,true);
break;
case "editQuery":
case "editQueryMenu":
if (_firstEditQueryTDC && _bTdcActivate) {
showPromptDialog("This action will modify the query. Web Intelligence can track data changes only when the query remains the same. If you modify the query, the reference data will be lost and changed data will not be visible until the next refresh. Do you want to continue?","Deactivate Data Tracking",1, launchQueryPanel)
} else {
launchQueryPanel()
}
break;
case "formatTool":
_showFormats=o.isChecked()
formatContainer.setDisplay(_showFormats)
resizeCB()
recordProp("CDZ_VIEW_formatPal",_showFormats?"1":"0")
break;
case "reportTool":
_showReporting=o.isChecked()
reportContainer.setDisplay(_showReporting)
resizeCB()
recordProp("CDZ_VIEW_reportPal",_showReporting?"1":"0")
break;
case "formulaTool":
_showFormula=o.isChecked()
formulaContainer.setDisplay(_showFormula)
resizeCB()
recordProp("CDZ_VIEW_formulaPal",_showFormula?"1":"0")
break;
case "hideTool":
_showFormats=_showReporting=_showFormula=false
formatContainer.setDisplay(_showFormats)
reportContainer.setDisplay(_showReporting)
formulaContainer.setDisplay(_showFormula)
resizeCB()
recordProp("CDZ_VIEW_formatPal",_showFormats?"1":"0")
recordProp("CDZ_VIEW_reportPal",_showReporting?"1":"0")
recordProp("CDZ_VIEW_formulaPal",_showFormula?"1":"0")
_showDrillBar=false;
_showDrillBarChanged=true;
var p=urlParams(false,true);
if (p!="")
frameNav("Report","report"+_appExt+p,true);
break;
case "preferences":
wt()
frameNav("DlgFrame","language/"+_lang+"/html/userPrefDialog.html")
break;
case "fgCombo":
case "bgCombo":
case "borderColor":
if (o.getDefaultActionColor()==null)
{
_currColorCombo=o
_currColorCB=toolBarColorPickCB
frameNav("DlgFrame","language/"+_lang+"/html/colorPickerDialog.html")
} else {
o.hasChanged=true
}
if (_appFmt)
{
wt()
addLastUsedColor(o.getDefaultActionColor())
 applyClickCB()
 }
 else
 setApplyToolTip()
break;
case "bold":
case "italic":
case "under":
case "bold":
case "fontCombo":
case "sizeCombo":
case "leftAlign":
case "centerAlign":
case "rightAlign":
case "topAlign":
case "middleAlign":
case "bottomAlign":
case "wrapText":
case "borders":
var elts=getSelectedElts(), l = elts.length
if (l>0) {
o.hasChanged=true
if (_appFmt)
{
wt()
 applyClickCB()
 }
 else
 setApplyToolTip()
 }
break;
case "tabsZone_delete": 
if (arrReports.length==1) return 
wt()
showPromptDialog("Are you sure you want to delete this report?","Deleting Report",1,deleteReport)
break;
case "tabsZone_add":
wt()
addReport();
break;
case "tabsZone_duplicate":
wt()
duplicateReport()
break;
case "tabsZone_first":
wt()
moveReport('first')
break;
case "tabsZone_previous":
wt()
moveReport('previous')
break;
case "tabsZone_next":
wt()
moveReport('next')
break;
case "tabsZone_last":
wt()
moveReport('last')
break;
        case "tabsZone_saveReportXLS":
var p = urlParamsNoBID() + "&doctype=" + strDocType 
p +=  "&viewType=" + (_saveAsXLSOptimized=="Y"? "O":"X")+"&saveReport=Y"
frameNav("DlgFrame", _root + "downloadPDForXLS" + _appExt + p);
break
case "tabsZone_saveReportPDF":
var p=urlParamsNoBID() + "&doctype=" + strDocType + "&viewType=P"+"&saveReport=Y"
frameNav("DlgFrame", _root + "downloadPDForXLS" + _appExt + p);
break
case "tabsDP":
selectDP()
break;
case "groupingBtn":
frameNav("DlgFrame","language/"+_lang+"/html/groupingDialog.html")
break;
case "filter":
case "insertFilter":
case "cellFilter":
wt()
updateParentIdRefBid(false,true)
file = _isQuickFilter ? "partialFilterDialog":"editFilterDialog"
frameNav("DlgFrame","language/"+_lang+"/html/" + file + ".html?viewer")
break;
case "removeFilter":
            showPromptDialog("Are you sure you want to delete this filter?" , "Web Intelligence", _promptDlgWarning, doRmFilter)        
break
case "duplicate":
case "duplicateBottom":
wt()
updateContainerIdRefBid()
var p=urlParams(true)
if (p!="")
frameNav("Report","processDuplicate"+_appExt+p+"&isBottom="+(id=="duplicateBottom"?1:0),true)
break;
case "brkCheck1":
case "brkCheck2":
wt()
var p=urlParams(true)
if (p!="")
frameNav("Report","processBreaks"+_appExt+p+"&iAction="+(o.isChecked()?1:0)+"&sFrom=Report&sRedirect=report"+"&bAddTableHeader="+_addTableHeader,true)
break;
case "insertBreak":
wt()
var p=urlParams(true)
if (p!="")
frameNav("Report","processBreaks"+_appExt+p+"&iAction=1&sFrom=Report&sRedirect=report&sBlockID="+_breakBlockBID+"&bAddTableHeader="+_addTableHeader,true)
break;
case "removeBreak":
wt()
var p=urlParams(true)
if (p!="")
frameNav("Report","processBreaks"+_appExt+p+"&iAction=0&sFrom=Report&sRedirect=report&sBlockID="+_breakBlockBID+"&bAddTableHeader="+_addTableHeader,true)
break;
case "breakProperties":
wt()
frameNav("DlgFrame","language/"+_lang+"/html/breakDialog.html")
break;
case "removeSect":
wt()
showPromptDialog("Are you sure you want to delete this section?","Confirm Delete",1,rmSect)
break;
case "removeTable":
wt()
_curIdRef = null; 
var p=urlParams(false)
if (p!="")
frameNav("Report", _root +"processRemoveReportElements"+_appExt+p+"&objects=Table",true)
break;
case "removeRow":
case "removeCol":
var elts=getSelectedElts();
if (elts.length==1) 
{
wt();
updateParentIdRefBid(); 
var p=urlParams(true,false);
if (p!="")
{
var param=_root +"processRemoveReportElements"+_appExt+p;
param+=("removeRow"==id)?"&objects=Row":"&objects=Col";
frameNav("Report", param,true);
}
}
else
{
wt();
removeReportElement();
}
break;
case "removeCell":
_curIdRef = null; 
case "removeBlock":
case "removeComposite":
case "removeBut":
wt();
removeReportElement();
break;
case "sortBut":
m = sortSubMenu
if (m.sort2.isChecked()) {
m.sort2.check(false)
m.sort0.check(true)
} else if (m.sort1.isChecked()) {
m.sort1.check(false)
m.sort0.check(true)
} else {
m.sort1.check(true)
m.sort0.check(false)
}
var p=urlParams(true)
if (p!="")
{
wt()
eventManager.notify(_EVT_REINIT_REPORTMAP_NODE)
frameNav("Report","processSorts"+_appExt+p+"&iSortAction="+(m.sort1.isChecked()?1:(m.sort2.isChecked()?2:0))+"&sSortPriority="+_sortPriority,true)
}
break;
case "sortAsc":
wt()
var p=urlParams(true)
if (sortDsc.isChecked())
sortDsc.check(false)
if (p!="")
{
eventManager.notify(_EVT_REINIT_REPORTMAP_NODE)
frameNav("Report","processSorts"+_appExt+p+"&iSortAction="+(sortAsc.isChecked()?1:0)+"&sSortPriority="+_sortPriority,true)
}
break;
case "sortDsc":
wt()
if (sortAsc.isChecked())
sortAsc.check(false)
var p=urlParams(true)
if (p!="")
{
eventManager.notify(_EVT_REINIT_REPORTMAP_NODE)
frameNav("Report","processSorts"+_appExt+p+"&iSortAction="+(sortDsc.isChecked()?2:0)+"&sSortPriority="+_sortPriority,true)
}
break;
case "sort0":
case "sort1":
case "sort2":
var val=id.slice(4)
var p=urlParams(true)
if (p!="")
{
if (o.par&&o.par.par&&o.par.par.isChart)
{
var axName="x",par=o.par.par
switch(par.sortAxisIndex)
{
case 3: axName="x"; break
case 4: axName="y"; break
case 5: axName="z"; break
}
p+="&sAxisName="+axName+"&sAxisIndex="+par.sortIndexInAxis
}
wt()
eventManager.notify(_EVT_REINIT_REPORTMAP_NODE)
frameNav("Report","processSorts"+_appExt+p+"&iSortAction="+val+"&sSortPriority="+_sortPriority,true)
}
break;
case "removeSorts":
var p=urlParams(true,true)
if (p!="")
{
if (o.par&&o.par.par&&o.par.par.isChart)
{
var axName="x",par=o.par.par
switch(par.sortAxisIndex)
{
case 3: axName="x"; break
case 4: axName="y"; break
case 5: axName="z"; break
}
p+="&sAxisName="+axName+"&sAxisIndex="+par.sortIndexInAxis
}
wt()
eventManager.notify(_EVT_REINIT_REPORTMAP_NODE)
frameNav("Report","processSorts"+_appExt+p+"&iSortAction=-1",true)
}
break;
case "sortProps":
wt()
frameNav("DlgFrame","language/"+_lang+"/html/sortDialog.html")
setSort(getSelectedElts());
break;
case "customSort":
wt()
if (o.par&&o.par.par&&o.par.par.isChart)
{
frameNav("SecondDlgFrame","language/"+_lang+"/html/applyCustomSortDialog.html?ID=" + o.par.par.exprID)
} else {
frameNav("SecondDlgFrame","language/"+_lang+"/html/applyCustomSortDialog.html")
}
setSort(getSelectedElts());
break;
case "turnto":
wt()
frameNav("DlgFrame","language/"+_lang+"/html/applyTurnToDialog.html")
break;
case "setSection":
wt()
updateContainerIdRefBid(); 
var p=urlParams(true)
if (p!="")
frameNav("Report","processSetSection"+_appExt+p+"&sRefreshRMNode=yes",true)
break;
case "swap":
wt()
var p=urlParams(true)
if (p!="")
frameNav("Report","processSwap"+_appExt+p,true)
break;
case "formulaOK":
case "formulaText":
wt()
updateParentIdRefBid(false,true) 
var p=urlParams(false)
if (p!="")
{
self.document.viewerForm.action="processFormulaValidation"+_appExt+p+"&nAction=1"
self.document.viewerForm.target="DlgFrame"
self.document.viewerForm.sParam1.value=formulaText.getValue();
self.document.viewerForm.submit();
}
break;
case "formulaCancel":
resetFormula();
break;
case "variableCreation":
wt()
updateParentIdRefBid(false,true) 
frameNav("DlgFrame","language/"+_lang+"/html/variableEditorDialog.html")
break;
case "formulaEditor":
wt()
updateParentIdRefBid(false,true) 
frameNav("DlgFrame","language/"+_lang+"/html/variableEditorDialog.html?isFormulaEdit=yes")
break;
case "cellFormatTable":
wt()
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatTableDialog.html")
break;
case "cellFormatAllCells":
wt()
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatCellDialog.html?allTableCells=yes")
break;
case "cellFormatChart":
wt()
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatChartDialog.html")
break;
case "cellFormatCell":
wt()
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatCellDialog.html")
break;
case "cellFormatSection":
wt()
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatSectionDialog.html")
break;
case "cellFormatReport":
wt()
frameNav("DlgFrame", _root + "language/"+_lang+"/html/applyFormatReportDialog.html")
break;
case "propertiesQuery":
wt()
frameNav("DlgFrame","language/"+_lang+"/html/queryPropertiesDialog.html")
break;
case "viewSQL":
var f = getQueryFrame();
if(f != null) f.submitQueries("viewsql")
break;
case "propertiesKeyDate":
var p=urlParams(false,true)
wt()
if (p!="")
frameNav("DlgFrame","getKeydateProperties"+_appExt+p)
break;
break;
case "addQuery":
case "tabsDP_add":
wt()
frameNav("DlgFrame","language/"+_lang+"/html/addQueryDialog.html")
break;
case "tabsDP_first":
wt()
moveDP(0)
break;
case "tabsDP_previous":
wt()
moveDP(iDataProviderID-1)
break;
case "tabsDP_next":
wt()
moveDP(iDataProviderID+1)
break;
case "tabsDP_last":
wt()
moveDP(arrDPs.length-1)
break;
case "tabsDP_remove":
showPromptDialog("Are you sure you want to delete this query?",null,1,deleteDP)
break;
case "tabsDP_duplicate":
goToProcessQueryPanel("&iDPIndex="+iDataProviderID+"&bDuplicateDP=true");
break;
case "insertMenuIcon2":
wt()
insertRowCol()
break;
case "insertRowAbove":
wt()
addRow('above')
break;
case "insertRowBelow":
wt()
addRow('below')
break;
case "insertColLeft":
wt()
addCol('left')
break;
case "insertColRight":
wt()
addCol('right')
break;
case "alerterBtn":
wt()
frameNav("DlgFrame","language/"+_lang+"/html/alerterDialog.html")
break;
case "mergeOrSplit":
processMergeOrSplit (mergeOrSplit.isChecked(), processMergeOrSplitEnd, processMergeOrSplitCancel)
break;
case "newHyperlink":
case "editHyperlink":
case "removeHyperlink":
case "readAsHyperlink":
    wt()
showHyperlinkDialog(id)
break;
case "frontMenuItem":
setLayeringLevelTo("front")
break;
case "backMenuItem":
setLayeringLevelTo("back")
break;
case "forwardMenuItem":
setLayeringLevelTo("forward")
break;
case "backwardsMenuItem":
setLayeringLevelTo("backwards")
break;
case "alignLeftItem":
setAlignmentTo("left")
break;
case "alignCenter":
setAlignmentTo("center")
break;
case "alignRight":
setAlignmentTo("right")
break;
case "alignTop":
setAlignmentTo("top")
break;
case "alignMiddle":
setAlignmentTo("middle")
break;
case "alignBottom":
setAlignmentTo("bottom")
break;
case "relativePos":
setAlignmentTo("relativePos")
break;
case "formatPainter":
var bid = null
if (_singleSel != null) 
{
bid = _singleSel.bid
} else if (_tableBid != null) {
bid =_tableBid
}
_formatPainterBid = formatPainter.isChecked()? bid:null
_reportIdxSrc = (_formatPainterBid != null)?iReportID:null
_isSingleActionFormatPainter = true
break;
case "bordersBtn":
processBorders()
break
case "bgimgBtn":
processBackgroundImage()
break
default:
return false
}
return true
}
function formatPainterDblClickCB()
{
if (_formatPainterBid!=null)
_isSingleActionFormatPainter = false
}
function formatPainterStopIfSingleAction(e)
{
if (_isSingleActionFormatPainter&&(_formatPainterBid!=null))
{
_formatPainterBid = null
_reportIdxSrc = null
formatPainter.check(false)
}
}
function doRmFilter() {
var url = _root + "processFilterMap" + _appExt
updateParentIdRefBidForFilter()
if (_isQuickFilter) {
elt = searchClassName(_singleSel,"block")
if (elt==null)
{
elt = searchClassName(_singleSel,"section")
}
url += urlParamsNoBID() 
url += "&sBid=" + _singleSel.bid
url += "&filtBid=" + elt.bid
url += "&command=" + 4
} else {
url += urlParams(true) 
url += "&command=" + 0
}
frameNav("Report", url)
}
function getQueryFrame()
{
var f=getFrame("querypanel")
return f;
}
function alerterMenuInit()
{
var o=this,elts=getSelectedElts(),k=0 
if ((elts.length <= 0) || !(doc.arrAlerter)) return
var elt=elts[0]
var mlen=o.items.length, nbAlerters=doc.arrAlerter.length, it=null
for (var i=0; i < nbAlerters; i++) 
{
var alerter = doc.arrAlerter[i]
if (k < mlen) {
it=o.getItem(k)
it.setText(alerter.name)
} else {
it=o.insertCheck(-1, "alerter" + i, alerter.name, alerterCheckCB)
}
it.alertID = alerter.id
if (elt.alerts) {
nbAlerts = elt.alerts.length
for (var j=0; j < nbAlerts; j++) {
if (elt.alerts[j] == alerter.id) {
it.check(true)
break
} else {
it.check(false)
}
}
}
it.show(true)
k++
}
mlen=o.items.length
for (var i=k; i < mlen; i++) {
o.getItem(i).show(false)
}
}
function alerterCheckCB()
{
var its = alerterMenu.items, mlen = its.length, ids='', it = null
for (var i=0; i < mlen; i++) 
{
it = its[i]
if (!it.isShown) break
ids += it.alertID
ids += ','
ids += it.isChecked()
ids += (i<(mlen-1))?';':''
}
var url= _root + 'processAlerters' + _appExt
url += urlParams(false,false) + "&alrtIDS=" + ids
frameNav("Report", url)
}
function findReportBid(elt)
{
var reportElt=searchClassName(elt,"report")
return (reportElt!=null)?reportElt.bid:null
}
function processMergeOrSplit (mergeProcess, fctMergeOrSplitEndOk, fctMergeOrSplitCancel)
{
elts=getSelectedElts()
var tmpValue = ""
if (mergeProcess)
{
for (var i=0;i<elts.length;i++)
{
var e=elts[i]
if (! e.formula)
break;
if (tmpValue=="" && e.formula != "")
tmpValue=e.formula
if (tmpValue!="" && e.formula != "" && tmpValue != e.formula)
{
showPromptDialog("The selected cells contain multiple values. Merging will keep the contents of the first selected cell only. Are you sure you want to merge the cells?","Confirm Cell Merge",1,fctMergeOrSplitEndOk,fctMergeOrSplitCancel)
return
}
}
}
if (fctMergeOrSplitEndOk)
fctMergeOrSplitEndOk ()
}
function processMergeOrSplitEnd ()
{
mergeOrSplit.hasChanged=true
if (_appFmt)
applyClickCB()
else
setApplyToolTip()
}
function processMergeOrSplitCancel ()
{
selectionChanged(false)
}

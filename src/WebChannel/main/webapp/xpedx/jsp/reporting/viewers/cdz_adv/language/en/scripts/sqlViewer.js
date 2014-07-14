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
*/function newBOSQLNode(operator)
{
var o=new Object;
o.operator=operator;
o.par=null;
o.isNode=true;
o.init=BOSQLNode_init;
o.items=new Array;
o.add=BOSQLNode_add;
o.addNode=BOSQLNode_addNode;
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
return o
}
var sqlViewerDlg = null;
var info = null;
var saveButton = null;
var closeButton = null;
var undoButton = null;
var validateButton = null;
var printButton = null;
var dlgWidth= 500;
var dlgHeight= 400;
var zoneWidth= 480;
var zoneHeight= 250;
var sqlTreeWidth= 150;
var sqlTreeHeight= 220;
var lines= 15;
var cols= 70;
var initialized= false;
var _p= parent; 
var urlImg= _p._img;
var qpFrame= _p.getQueryFrame();
var curDP= (qpFrame?qpFrame._currentDP:null);
var curDPName= (curDP?curDP.name:_p.getSelectedDPName());
var isCustomSQL= false;
var showTreeSQL= true;
var enableEditSQL= _p.isEnableUserRight(_p._usrEditSQL);
var root= null;
var curSelect= null;
var strPrintSQL= "";
var curSql0= null;
var validSQLMsg= "The SQL contains no errors"
function loadDialog()
{
if (!initialized)
{
initialized=true;
sqlViewerDlg = newDialogBoxWidget("sqlViewerDlg","SQL Viewer",dlgWidth,dlgHeight,closeCB,closeCB,false);
sqlZone= newFrameZoneWidget("sqlZone",zoneWidth,zoneHeight);
sqlTxt= newTextAreaWidget("sqlTxtArea",lines,cols,null,SQLchangeCB,null,null);
sqlTree= newTreeWidget('sqlTreeView', sqlTreeWidth, sqlTreeHeight, urlImg + "sqlTree.gif", clickCB, dclickCB);
generateSqlRadio = newRadioWidget("keepSqlRadio","sqlGrp", 'Use generated SQL', radioBtnCB);
customSqlRadio = newRadioWidget("customSqlRadio","sqlGrp", 'Use custom SQL', radioBtnCB);
info = newInfoWidget("infoSQLViewer","More Information","","View, print, or edit the query SQL here.");
saveButton = newButtonWidget("saveButton", "Save", "saveCB()", 50);
closeButton = newButtonWidget("closeButton", "Close", "closeCB()", 50);
undoButton = newButtonWidget("undoButton", "Undo", "undoCB()", null, null,null,null,null,urlImg+'standard.gif',16,16,16*4,0,null,16*4,16);
validateButton = newButtonWidget("validateButton", "Validate", "validateCB()", null,null,null,null,null,urlImg+'sqlicons.gif',16,16,16*2,0,null,16*2,16);
printButton = newButtonWidget("printButton", "Print", "printCB()", null,null,null,null,null,urlImg+'menu.gif',16,16,16*2,0,null,16*2,16);
sqlViewerDlg.attachDefaultButton(closeButton);
}
if ( !_curWin.sqlViewerDlgInitialized )
{
_curWin.sqlViewerDlgInitialized = true;
targetApp(
sqlViewerDlg.beginHTML() +
'<table cellspacing="0" cellpadding="5" border="0" class="dialogzone"><tbody>'+
'<tr><td width="100%">'+
sqlZone.beginHTML()+
'<table cellspacing="0" cellpadding="5" border="0" class="dialogzone"><tbody>'+
'<tr><td colspan="2" >'+generateSqlRadio.getHTML()+customSqlRadio.getHTML()+'</td></tr>'+
'<tr>'+
'<td>'+sqlTree.getHTML()+'</td>' +
'<td>'+sqlTxt.getHTML()+'</td>' +
'</tr>' +
'<tr>' +
'<td align="right" valign="center" colspan="2">' +
'<table cellspacing="0" cellpadding="0" border="0"><tbody><tr>' +
'<td>' +
undoButton.getHTML() +
'</td>' +
'<td>' + getSpace(5,1)+ '</td>' +
'<td>' +
validateButton.getHTML() +
'</td>' +
'<td>' + getSpace(5,1)+ '</td>' +
'<td>' +
printButton.getHTML() +
'</td>' +
'</tr></tbody></table>'+
'</td>' +
'</tr>' +
'</tbody></table>'+
sqlZone.endHTML()+
'</td></tr>' +
'<tr>' +
'<td align="center" valign="right">' +
info.getHTML() +
'</td>' +
'</tr>' +
'<tr>' +
'<td align="right" valign="center">' +
'<table cellspacing="0" cellpadding="0" border="0"><tbody><tr>' +
'<td>' +
saveButton.getHTML() +
'</td>' +
'<td>' + getSpace(5,1)+ '</td>' +
'<td>' +
closeButton.getHTML() +
'</td>' +
'</tr></tbody></table>'+
'</td>' +
'</tr>' +
'</tbody></table>' +
sqlViewerDlg.endHTML()
);
}
sqlViewerDlg.init();
sqlTxt.init();
sqlTree.init();
generateSqlRadio.init();
customSqlRadio.init();
info.init();
saveButton.init();
closeButton.init();
undoButton.init();
validateButton.init(); 
printButton.init();
fillWidgets();
_p.hideBlockWhileWaitWidget();
sqlViewerDlg.show(true);
}
function closeCB()
{
qpFrame.changeSQLIcon();
_p.hideBlockWhileWaitWidget();
sqlViewerDlg.show(false); 
}
function undoCB()
{
sqlTxt.setValue(curSelect.oldValue);
curSelect.value = curSelect.oldValue;
undoButton.setDisabled(true);
}
function validateCB()
{
saveCustomSQL()
var params=_p.urlParamsNoBID();
if(params)
{
var url = _p._root+"processSetSQL"+_p._appExt+params;
self.document.submitSQLForm.action = url;
self.document.submitSQLForm.target = "validateSQL";
self.document.submitSQLForm.WOMquery.value ="";
self.document.submitSQLForm.inputSQL.value = encodeSQLTree();
self.document.submitSQLForm.sDPName.value = curDPName;
self.document.submitSQLForm.nAction.value = 0;
_p.wt();
self.document.submitSQLForm.submit();
}
}
function saveCB()
{
saveCustomSQL();
var sWOMquery="";
if(curDP && curDP.refreshContexts)
{
curDP.refreshContexts = false;
}
var params=_p.urlParamsNoBID();
if(params)
{
var url = _p._root+"processSetSQL"+_p._appExt+params;
self.document.submitSQLForm.action = url;
self.document.submitSQLForm.target = "validateSQL";
self.document.submitSQLForm.WOMquery.value =sWOMquery;
self.document.submitSQLForm.inputSQL.value = encodeSQLTree();
self.document.submitSQLForm.sDPName.value = curDPName;
self.document.submitSQLForm.nAction.value = 1;
_p.wt();
self.document.submitSQLForm.submit();
}
}
function printCB()
{
saveCustomSQL();
strPrintSQL="";
printSQLFormatHTML();
self.document.submitPrintForm.title.value = _p._viewSQLLabel+ ' - ' + dpName;
self.document.submitPrintForm.textToPrint.value = "<div class='sqlText'>"+strPrintSQL+"</div>";
var features = "width=640,height=480,menubar,resizable,scrollbars";
var url =parent._root+"language/"+parent._lang+"/html/printWindow.html";
url += "?noGrabber=yes&skin=" +parent._root + "lib/images/" + parent._skinName + "/&lang=" + parent._lang;
var newWindow = window.open(url, "printSQLWindow", features);
if(newWindow) newWindow.focus();
}
function clickCB(data)
{
saveCustomSQL();
if(data.isNode)
sqlTxt.setValue("");
else
sqlTxt.setValue(data.value);
curSelect=data;
undoButton.setDisabled(!curSelect.isModified || !enableEditSQL || !isCustomSQL);
if(enableEditSQL && isCustomSQL)
disableTextArea(curSelect.isNode);
else
disableTextArea(true);
}
function dclickCB(){}
function radioBtnCB()
{
if(isCustomSQL == customSqlRadio.isChecked()) return;
isCustomSQL = customSqlRadio.isChecked();
if(!isCustomSQL)
_p.showPromptDialog("Do you want to restore the SQL generated by your query ?","View SQL",1,restoreSQLCB,keepCustomSQLCB);
else
{
var params=_p.urlParamsNoBID();
    if(params)
    {
        var url = _p._root+"processSetSQL"+_p._appExt+params;
        self.document.submitSQLForm.action = url;
        self.document.submitSQLForm.target = "validateSQL";
        self.document.submitSQLForm.WOMquery.value = qpFrame.encodeCurrentQuery();
        self.document.submitSQLForm.inputSQL.value = "";
        self.document.submitSQLForm.sDPName.value = curDPName;
        self.document.submitSQLForm.nAction.value = 3;
        _p.wt();
        self.document.submitSQLForm.submit();
        }
}
}
function restoreSQLCB()
{
var params=_p.urlParamsNoBID();
if(params)
{
var url = _p._root+"processSetSQL"+_p._appExt+params;
self.document.submitSQLForm.action = url;
self.document.submitSQLForm.target = "validateSQL";
self.document.submitSQLForm.WOMquery.value = qpFrame.encodeCurrentQuery();
self.document.submitSQLForm.inputSQL.value = "";
self.document.submitSQLForm.sDPName.value = curDPName;
self.document.submitSQLForm.nAction.value = 2;
_p.wt();
self.document.submitSQLForm.submit();
}
}
function keepCustomSQLCB()
{
customSqlRadio.check(true);
isCustomSQL=true;
}
function SQLchangeCB()
{
if(!curSelect.isNode)
{
curSelect.isModified=true;
undoButton.setDisabled(!enableEditSQL || !isCustomSQL);
}
}
function fillWidgets()
{
if(curSql0 == null) return; 
if(isCustomSQL)
customSqlRadio.check(true);
else
generateSqlRadio.check(true);
backupSQL();
fillSQLTree();
if(curSql0.items.length<=1)
{ 
showTreeSQL=false;
if(root.sub.length == 1) root.sub[0].select();
}
else
{
root.select();
}
if(showTreeSQL)
{
sqlTree.setDisplay(true);
sqlTxt.resize(null,cols);
}
else
{
sqlTree.setDisplay(false);
sqlTxt.resize(null,100);
}
updateWidgets();
}
var indexSQL=1;
function fillSQLTree()
{
indexSQL=1;
sqlTree.deleteAll();
root=newTreeWidgetElem(0,curSql0.operator,curSql0,null,1,null,null);
sqlTree.add(root);
buildSQLTree(curSql0,root,0);
sqlTree.rebuildHTML();
}
function buildSQLTree(elt, treeElt,level)
{
var name = "";
for (var i in elt.items)
{
var subElt=elt.items[i];
if(subElt.isNode )
{ 
name=subElt.operator;
icnIdx=0;
icnIdxSel=1;
}
else
{
name="Select"+indexSQL; 
indexSQL++;
icnIdx=2;
icnIdxSel=2;
}
var newTreeElt=newTreeWidgetElem(icnIdx, name, subElt,null,icnIdxSel);
if (level==0) newTreeElt.expanded=true;
treeElt.add(newTreeElt);
if (subElt.isNode) buildSQLTree(subElt, newTreeElt,level+1)
}
}
function updateWidgets()
{
customSqlRadio.setDisabled(!enableEditSQL);
generateSqlRadio.setDisabled(!enableEditSQL);
saveButton.setDisabled(!enableEditSQL || !isCustomSQL);
undoButton.setDisabled(true);
validateButton.setDisabled(!enableEditSQL || !isCustomSQL);
disableTextArea(!enableEditSQL || !isCustomSQL || curSelect.isNode);
}
function disableTextArea(d)
{
if(_ie) 
{
sqlTxt.layer.readOnly=d;
}
else
{
sqlTxt.setDisabled(d);
}
}
function saveCustomSQL()
{
if(curSelect && !curSelect.isNode && curSelect.isModified)
{
curSelect.value = sqlTxt.getValue();
}
}
function updateSQL(rootSQL,isCustom,bForceCustom)
{
if(isCustom != null)
{
if(curDP) curDP.isCustomSQL=isCustom;
}
if(rootSQL != null)
{
curSql0=rootSQL;
}
isCustomSQL = bForceCustom?bForceCustom:(isCustom?isCustom:false);
fillWidgets();
_p.hideBlockWhileWaitWidget();
}
function updateAfterViewSQL(newEntry)
{
_p.strEntry = newEntry;
if(curDP) curDP.isModified = false;
}
function encodeSubSQL(node)
{
if (node.items)
{
var len=node.items.length;
var s='['
for (var k=0;k<len;k++)
{
if (k>0) s+=','
var item=node.items[k]
if(item.isNode)
{
s+=enc(item.operator,encodeSubSQL(item));
}
else
{
  s+=encString(item.value);    
}
}
return s+']';
}
else
{
return '[]'
}
}
function encodeSQLTree()
{
var s=''
if(curSql0)
{
s+=enc(curSql0.operator,encodeSubSQL(curSql0))
return s;
}
else return '[]';
}
function backupSQL()
{
iterBackUpSQL(curSql0);
}
function iterBackUpSQL(node)
{
var items=node.items,len=items.length;
for(var i=0;i<len;i++)
{
if(items[i].isNode) 
iterSQLTree(items[i]);
else
items[i].oldValue=items[i].value;
}
}
function printSQLFormatHTML()
{
indexSQL=1;
strPrintSQL="";
if (curSql0.items.length ==1 )
{
var item=curSql0.items[0];
if(!item.isNode)
strPrintSQL=_p.convStr(item.value,true,true);
}
else
{
iterSQLFormatHTML(curSql0,0);
}
}
function iterSQLFormatHTML(node,level)
{
strPrintSQL+='<div style="margin-left:'+ level*30+'">'+node.operator.toUpperCase();
var items=node.items,len=items.length;
for(var i=0;i<len;i++)
{
if(items[i].isNode) 
iterSQLFormatHTML(items[i],level+1);
else
{
strPrintSQL+='<div style="margin-left:20">Select'+indexSQL++;
strPrintSQL+='<div style="margin-left:20">';
strPrintSQL+=_p.convStr(items[i].value,true,true);
strPrintSQL+='</div></div>';
strPrintSQL+='<br>';
}
}
strPrintSQL+='</div>';
}
function writeBody()
{
var s='';
s='<form target="printWindow" style="display:none" name="submitPrintForm" method="post" action="">';
s+='<input type="hidden" name="title" id="title" value="">';
s+='<input type="hidden" name="textToPrint" id="textToPrint" value="">';
s+='</form>';
s+='<iframe name="validateSQL" src="lib/empty.html"></iframe>';
s+='<form target="validateSQL" style="display:none" name="submitSQLForm" method="post" action="">';
s+='<input type="hidden" name="WOMquery" id="WOMquery">';
s+='<input type="hidden" name="inputSQL" id="inputSQL">';
s+='<input type="hidden" name="sDPName" id="sDPName">';
s+='<input type="hidden" name="nAction" id="nAction">';
s+='</form>';
document.write(s)
}
function debugSQLTree()
{
strPrintSQL="";
iterSQLTree(curSql0);
alert(strPrintSQL);
}
function iterSQLTree(node)
{
strPrintSQL+=node.operator+"\n";
var items=node.items,len=items.length;
for(var i=0;i<len;i++)
{
if(items[i].isNode) 
iterSQLTree(items[i]);
else
strPrintSQL+=items[i].value+"\n";
}
}
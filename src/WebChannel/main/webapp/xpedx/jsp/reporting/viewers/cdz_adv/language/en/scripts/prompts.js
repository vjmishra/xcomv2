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
*/strPromptsDlgTitle="Prompts";
iItemValueMaxLength=50;
initialized=false;
promptsDlg=null;
arrTreeViewMap=null;
prompts=new Array();
promptUI=null;
rangeValuesParams=null;
_this=null
function loadDialog()
{
if (!initialized)
{
initialized=true;
var strOkButtonLabel=(strSRC=="BCA")?((strWebApp=="cmc")?parent._BtnLabelUpdate:"Apply"):"Run Query";
if(strValidateSQL == "true") strOkButtonLabel="Apply";
promptsDlg = newDialogBoxWidget("promptsDlg",strPromptsDlgTitle,null,null,submitCB,cancelCB,false);
displayWaitCursor=newBlockWhileWaitWidget();
pListWidget=newTreePromptWidget("tree",607,102,pListWidgetCB,pListWidgetCB,null,expandTreeNodeCB,collapseTreeNodeCB);
frameZone=newFrameZoneWidget("frameZone1",616,244,true);
infozone=newInfoWidget("promptsInfozone","More Information","","Select or type the values you want to return to reports for each prompt displayed here.",55);
infozone.isTitleChanged=false;
OKButton=newButtonWidget("promptsOKButton",strOkButtonLabel,"submitCB()",60);
CancelButton=newButtonWidget("promptsCancelButton","Cancel","cancelCB()",60);
}
prompts=new Array();
for (var i=0;i<iNbPrompts;i++)
{
prompts[i]=newBOPrompt(arrPrompts[i]);
prompts[i].index=i;
}
buildTreeViewMap();
promptsUI=new getPromptsUI(arrTreeViewMap);
promptsUI.getWidgets();
_curWin.promptsInitialized=true;
targetApp
(
promptsDlg.beginHTML()+
'<form style="padding:0px;margin:0px" name="frmPrompt" id="frmPrompt" method="post" onSubmit="return false;">'+
'<table border="0" style="margin-left:5px;margin-right:5px;" cellspacing="0" cellpadding="0" class="dialogzone"><tbody>'+
'<tr>'+
'<td>'+
'<table border="0" cellspacing="0" cellpadding="0">'+
'<tr>'+
'<td class="dialogzone">Reply to prompts before running the query.</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
'<table border="0" style="margin-top:5px" cellspacing="0" cellpadding="0">'+
'<tr>'+
'<td>'+
pListWidget.getHTML()+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
'<table border="0" style="margin-top:5px" cellspacing="0" cellpadding="0">'+
'<tr>'+
'<td>'+
frameZone.beginHTML()+
'<table id="pDetailContainerID" border="0" cellpadding="0" cellspacing="0" width="580" height="180">'+
'<tr>'+
'<td align="center" valign="middle">'+
promptsUI.getHTML()+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
'<input type="hidden" name="sLovID" value="">'+
'<input type="hidden" name="sEmptyLab" value="'+_emptyValueLab+'">'+
'</td>'+
'</tr>'+
'</table>'+
frameZone.endHTML()+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
'<table border="0" style="margin-top:5px" cellspacing="0" cellpadding="0" width="607">'+
'<tr>'+
'<td>'+
infozone.getHTML()+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'<tr>'+
'<td align="right">'+
'<table border="0" style="margin-top:10px;margin-bottom:5px" cellspacing="0" cellpadding="0">'+
'<tr>'+
'<td>'+
OKButton.getHTML()+
'</td>'+
'<td>'+
getSpace(10,1)+
'</td>'+
'<td>'+
CancelButton.getHTML()+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</tbody></table>'+
'</form>'+
promptsDlg.endHTML()
)
promptsDlg.init();
displayWaitCursor.init();
frameZone.init();
pListWidget.init();
infozone.init();
OKButton.init();
CancelButton.init();
promptsDlg.attachDefaultButton(OKButton);
promptsUI.init();
fillPromptsList();
parent.hideWaitDlg();
promptsDlg.show(true);
promptsDlg.layer.style.visibility=_hide;
resize();
promptsDlg.center();
promptsDlg.layer.style.visibility=_show;
}
function resize()
{
if (isSmallScreen()) return;
var wWindow=winWidth();
var hWindow=winHeight();
var wZoom=wWindow/promptsDlg.width;
var hZoom=hWindow/promptsDlg.height;
var zoom=(wZoom>hZoom?hZoom:wZoom)*0.7;
if (zoom<1 || zoom<1.1)
    zoom=1;
else if (zoom>1.5)
zoom=1.5;
var dwDlg=Math.round((promptsDlg.getWidth()*zoom)-promptsDlg.getWidth());
var dhDlg=Math.round((promptsDlg.getHeight()*zoom)-promptsDlg.getHeight());
var wPL=pListWidget.getWidth()+dwDlg;
var dhPL=Math.round(dhDlg/40)*20;
var hPL=pListWidget.getHeight()+dhPL;
pListWidget.resize(wPL,hPL);
var wPD=frameZone.getWidth()+dwDlg;
var hPD=frameZone.getHeight()+(dhDlg-dhPL);
frameZone.resize(wPD,hPD);
_curDoc.getElementById("pDetailContainerID").width=parseInt(_curDoc.getElementById("pDetailContainerID").width)+dwDlg;
_curDoc.getElementById("pDetailContainerID").height=parseInt(_curDoc.getElementById("pDetailContainerID").height)+dhDlg-dhPL;
promptsUI.resize(zoom);
var wIZ=infozone.getWidth()+dwDlg;
var hIZ=infozone.getHeight();
infozone.resize(wIZ,hIZ);
}
function buildTreeViewMap()
{
arrTreeViewMap=new Array();
var nb=arrPrompts.length;
var k=0;
for (var i=0;i<nb;i++)
{
arrTreeViewMap[k]=new Array(2);
arrTreeViewMap[k][0]=i;
arrTreeViewMap[k][1]=prompts[i].level;
k++;
if (!prompts[i].isNestedPrompt() && prompts[i].isGrouped)
{
var blnFound=false;
var j=0;
for (j=i+1;j<nb;j++)
{
if (!prompts[j].isNestedPrompt())
{
blnFound=true;
break;
}
}
if (blnFound)
i=j-1;
else
break;
}
}
}
function fillPromptsList()
{
pListWidget.deleteAll();
var arrParentNodes=new Array();
var blnDisableOKButton=false;
arrParentNodes[0]=pListWidget;
for (var i=0;i<promptsUI.length;i++)
{
var pt=promptsUI.getPrompt(i);
var strValue=(pt.value=="")?null:pt.value;
if (strValue!=null)
{
}
else if (!pt.isNestedPrompt() && pt.isRequired)
blnDisableOKButton=true;
arrParentNodes[pt.level+1]=arrParentNodes[pt.level].add(newTreePromptElem(pt.name,stringOverflowEllipsis(strValue),i,pt.isOptional));
arrParentNodes[pt.level+1].expanded=true;
}
pListWidget.rebuildHTML();
pListWidget.selectByData(0);
OKButton.setDisabled(blnDisableOKButton);
}
function stringOverflowEllipsis(strValueToDisplay)
{
if (strValueToDisplay!=null && strValueToDisplay.length>iItemValueMaxLength) strValueToDisplay=strValueToDisplay.substring(0,iItemValueMaxLength)+'...';
return strValueToDisplay;
}
function updateLovAfterSubmitAction(iPrompt,s)
{
if (typeof(s)=="undefined") var s=loadLOV;
var pt=prompts[iPrompt];
pt.isMultiColumn=s.arrPrompts[0][7];
pt.isLovPartialResult=s.arrPrompts[0][18];
var blnGroupedPrompts=true;
for (var i=0;i<promptsUI.length;i++)
{
if (promptsUI.arrPromptTree[i][0]==iPrompt)
{
blnGroupedPrompts=(promptsUI.getPrompt(i).isGrouped)?true:false;
break;
}
}
if (blnGroupedPrompts)
{
if(_this==null) 
{ 
_this=treeLov[iPrompt];
_this.cleanTree=true;
}
if(_this.cleanTree) 
_this.deleteAll();
var selChunk =null;
arrBatchLovSelValue[iPrompt]=s.arrBatchLovSelValue[0];
arrNbBatchLovItems[iPrompt]=s.arrNbBatchLovItems[0];
arrNbLovItems[iPrompt]=s.arrNbLovItems[0];
if (s.strAction!="B")
{
var iNbBatchLovItems=arrNbBatchLovItems[iPrompt];
if(iNbBatchLovItems>1)
{
arrBatchLovTexts[iPrompt]=new Array(iNbBatchLovItems);
for (var j=0;j<iNbBatchLovItems;j++)
{
arrBatchLovTexts[iPrompt][j]=s.arrBatchLovTexts[0][j];
var blnSelected=(j==arrBatchLovSelValue[iPrompt]);
var itemChunk=newTreeWidgetElem(-1,arrBatchLovTexts[iPrompt][j],new Array(pt.paramName,j),"",null,"",null);
itemChunk.selectableItem=false;
if (blnSelected)
selChunk = _this.add(itemChunk);
else
{
var subChunk=_this.add(itemChunk);
subChunk.setIncomplete(treeLov_CompleteChunkCB);
subChunk.expanded=false;
}
}
}
}
if(arrNbLovItems[iPrompt]>0)
{
for (var i=0;i<arrNbLovItems[iPrompt];i++)
{
var t=s.arrLovTexts[0][i];
var v=t;
if (pt.isMultiColumn)
{
v=''+iPrompt+'_'+s.arrLovIndexes[0][i]+'_'+s.arrLovValues[0][i];
t=t.join(" | ");
}
else
v=''+iPrompt+'_'+s.arrLovIndexes[0][i];
var itemLov=newTreeWidgetElem(-1,t,v,"",null,"",null);
itemLov.selectableItem=pt.lovSelectableItems;
var sub = null;
if(selChunk)
{
sub = selChunk.add(itemLov);
selChunk.expanded=true;
}
else
{
sub = _this.add(itemLov);
}
if (pt.isGrouped)
{
}
else
{
sub.setIncomplete(treeLov_CompleteLOVCB);
sub.expanded=false;
}
}
}
else 
{
var itemLov=newTreeWidgetElem(-1,promptsUI.strLovNoValue,"","",null,"",null);
itemLov.selectableItem=false;
_this.add(itemLov);
}
if(_this.cleanTree) 
_this.rebuildHTML();
else
{
_this.finishComplete();
_this.expanded=true;
}
}
else 
{
var lv=objLov[iPrompt];
lv.showLovMessage(false);
lv.showPartialResult(pt.isLovPartialResult);
if (!s.bNestedPrompt)
{
lv.lovList.freeze();
lv.lovList.del();
lv.lovList.resetColums();
arrNbLovItems[iPrompt]=s.arrNbLovItems[0];
var hh=(s.arrLovTitle.length>0) ? s.arrLovTitle[0] : new Array;
var colwidth=lv.lovList.w;
if (hh.length>1) colwidth=Math.floor(lv.lovList.w/2);
colwidth-=1;
if (hh.length>0)
{
for (var l=0;l<hh.length;l++)
lv.lovList.addColumn(hh[l],colwidth,true,(l==s.iLovSortedColumnIndex[0]?s.iLovSortType[0]:-1),hh[l]);
}
else
lv.lovList.addColumn("",colwidth,false);
if (s.arrNbLovItems[0]>0)
{
for (var i=0;i<s.arrNbLovItems[0];i++)
{
var t=s.arrLovTexts[0][i];
var v=t;
if (pt.isMultiColumn) v=s.arrLovValues[0][i];
v=s.arrLovIndexes[0][i]+'_'+v;
var blnSelected=(i==s.arrSelectedValues[0])?true:false;
lv.lovList.add(t,v,blnSelected);
}
}
else
lv.lovList.add(promptsUI.strLovNoValue,"",false);
lv.lovList.rebuildHTML();
if (s.strAction!="B")
{
lv.prevChunkIcn.setDisabled(true);
lv.chunkCombo.del();
arrNbBatchLovItems[iPrompt]=s.arrNbBatchLovItems[0];
var iNbBatchLovItems=s.arrNbBatchLovItems[0];
for (var j=0;j<iNbBatchLovItems;j++)
{
var blnSelected=(j==s.arrBatchLovSelValue[0])?true:false;
lv.chunkCombo.add(s.arrBatchLovTexts[0][j],j.toString(),blnSelected);
}
lv.nextChunkIcn.setDisabled((iNbBatchLovItems>1)?false:true);
lv.updateWidget();
}
}
else
{
lv.prevChunkIcn.setDisabled(true);
lv.chunkCombo.del();
lv.lovList.freeze();
lv.lovList.del();
mustFillNP(iPrompt);
lv.lovList.rebuildHTML();
}
}
var iTreeIndex=promptsUI.selectedIndex;
promptsUI.isLovFilled[iTreeIndex]=true;
hideBlockWhileWaitWidget();
}
function updatePValueDiplay(iPrompt, strValueToDisplay)
{
var iTreeIndex=promptsUI.getTreeIndex(iPrompt);
if (iTreeIndex!=null)
{
var pt=promptsUI.getPrompt(iTreeIndex)
pListWidget.findByData(iTreeIndex).change(pt.name,strValueToDisplay,pt.isOptional);
}
}
function selectRangeValues(iPrompt)
{
rangeValuesParams=iPrompt.toString();
frameNav("SecondDlgFrame",parent._root+"language/"+parent._lang+"/html/rangeValuesDialog.html",null,_curWin);
}
function expandTreeNodeCB()
{
}
function collapseTreeNodeCB()
{
}
function pListWidgetCB()
{
var iTreeIndex=parseInt(this.getSelectedItem().userData);
if (promptsUI.selectedIndex!=iTreeIndex)
{
promptsUI.selectedIndex=iTreeIndex;
var pt=promptsUI.getPrompt(iTreeIndex);
var iPrompt=pt.index;
var blnGetLov=false;
if (pt.hasLOV && !promptsUI.isLovFilled[iTreeIndex])
{
displayWaitCursor.show(true);
if (typeof(arrNbBatchLovItems[iPrompt])!="undefined")
{
if (pt.isGrouped)
{
treeLov[iPrompt].deleteAll();
var arrNP=promptsUI.getGroupedNP(iPrompt);
var nb=arrNP.length;
var i=arrNP[nb-1];
var np=prompts[i];
var selChunk = null;
var iNbBatchLovItems=arrNbBatchLovItems[i];
if(iNbBatchLovItems>1)
{
for (var j=0;j<iNbBatchLovItems;j++)
{
var blnSelected=(j==arrBatchLovSelValue[i]);
var itemChunk=newTreeWidgetElem(-1,arrBatchLovTexts[i][j],new Array(np.paramName,j),"",null,"",null);
itemChunk.selectableItem=false;
if (blnSelected)
selChunk = treeLov[iPrompt].add(itemChunk);
else
{
var subChunk=treeLov[iPrompt].add(itemChunk);
subChunk.setIncomplete(treeLov_CompleteChunkCB);
subChunk.expanded=false;
}
}
}
if(arrNbLovItems[i]>0)
{
for (var k=0;k<arrNbLovItems[i];k++)
{
var t=arrLovTexts[i][k];
var v;
if (np.isMultiColumn)
{
v=''+np.index+'_'+arrLovIndexes[i][k]+'_'+arrLovValues[i][k];
t=t.join(" | ");
}
else
v=''+np.index+'_'+arrLovIndexes[i][k];
var itemLov=newTreeWidgetElem(-1,t,v,"",null,"",null);
itemLov.selectableItem=np.lovSelectableItems;
var sub = null;
if(selChunk) 
{
sub = selChunk.add(itemLov);
selChunk.expanded =true;
}
else 
{
sub=treeLov[iPrompt].add(itemLov);
}
sub.expanded=false;
sub.setIncomplete(treeLov_CompleteLOVCB);
}
}
else 
{
var itemLov=newTreeWidgetElem(-1,promptsUI.strLovNoValue,"","",null,"",null);
itemLov.selectableItem=false;
treeLov[iPrompt].add(itemLov);
}
treeLov[iPrompt].rebuildHTML();
}
else 
{
var lv=objLov[iPrompt];
lv.showLovMessage(false);
lv.chunkCombo.del();
lv.prevChunkIcn.setDisabled(true);
lv.lovList.freeze();
lv.lovList.del();
var hh=(arrLovTitle.length>0) ? arrLovTitle[0] : new Array;
lv.lovList.resetColums();
if (hh.length>0)
{
for (var l=0;l<hh.length;l++)
lv.lovList.addColumn(hh[l],_LOV_MULTI_COLH,true,(l==iLovSortedColumnIndex[0]?iLovSortType[0]:-1))
}
else
lv.lovList.addColumn("",_LOV_MULTI_COLH,false)
if (arrNbLovItems[iPrompt]>0)
{
for (var i=0;i<arrNbLovItems[iPrompt];i++)
{
var t=arrLovTexts[iPrompt][i];
var v=t;
if (pt.isMultiColumn) v=arrLovValues[iPrompt][i];
v=arrLovIndexes[iPrompt][i]+'_'+v;
lv.lovList.add(t,v,false);
}
}
else if (pt.hasNP)
mustFillNP(iPrompt);
else
lv.lovList.add(promptsUI.strLovNoValue,"",false);
lv.lovList.rebuildHTML();
var iNbBatchLovItems=arrNbBatchLovItems[iPrompt];
for (var j=0;j<iNbBatchLovItems;j++)
{
var blnSelected=(j==arrBatchLovSelValue[iPrompt]);
lv.chunkCombo.add(arrBatchLovTexts[iPrompt][j],j.toString(),blnSelected);
}
lv.nextChunkIcn.setDisabled((iNbBatchLovItems>1)?false:true);
lv.updateWidget();
lv.showPartialResult(pt.isLovPartialResult);
}
}
else
{
blnGetLov=true;
}
promptsUI.isLovFilled[iTreeIndex]=!blnGetLov;
displayWaitCursor.show(false);
}
for (var i=0;i<promptsUI.length;i++)
{
_curDoc.getElementById("tp"+i).style.display=(i==iTreeIndex)?'':'none';
}
if (infozone.isTitleChanged)
displaySelectedItems(iPrompt);
if (pt.hasNP)
setNestedPrompts();
else if (blnGetLov)
{
if (!pt.isLovDelegatedSearch && (strUserRefreshLov!="full" || (pt.value=="" && pt.isRequired) ))
getLovValues(iPrompt);
}
}
}
function mustFillNP(iPrompt)
{
objLov[iPrompt].lovList.add("(--Answer the related prompts in order to view available Values--)","",false);
}
function cancelQuery()
{
parent.cancelQueryCB();
}
function submitCB()
{
if (isAnsweredPrompts())
{
promptsDlg.show(false);
var url="processPrompts"+parent._appExt;
if (strSRC=="BCA")
{
url+="?sEntry="+strEntry+"&iViewerID="+iViewerID+"&src="+strSRC;
if (parent._viewerContext) url=parent._viewerContext+'/'+url;
}
else if (strSRC=="RPV")
url+="?sEntry="+strEntry+"&iViewerID="+iViewerID+"&src="+strSRC;
else
url+=parent.urlParamsNoBID(strEntry,null,null,null,null,1)+"&sNEV="+strNEV+"&sNewDoc="+strNewDoc+"&viewType="+strViewType+"&sApplyFormat="+strApplyFormat+"&iDPIndex="+strDPIndex+"&bValidateSQL="+strValidateSQL+"&nAction="+nAction;
url+="&advPrompts=yes";
url +="&bCreateDefaultReportBody="+parent.bCreateDefaultReportBody
url += "&defaultRepTitle="+convURL(parent._defaultRepTitle)
_curDoc.frmPrompt.action=url;
_curDoc.frmPrompt.target="loadLOV";
_curDoc.frmPrompt.submit();
var blnLovRefreshed=false;
var blnValueChanged=false;
for (var i=0; i<prompts.length; i++)
{
blnLovRefreshed=(blnLovRefreshed || prompts[i].lovRefreshed);
blnValueChanged=(blnValueChanged || prompts[i].valueChanged);
}
if (strSRC!="BCA" && strSRC!="RPV")
{
var n=0;
var pHasNP=false;
for (var i=0; i<iNbPrompts; i++)
{
if (prompts[i].parentLovID=="")
{
n++;
pHasNP=(pHasNP||prompts[i].hasNP);
}
}
if (blnLovRefreshed)
{
if (pHasNP)
parent.eventManager.notify(parent._EVT_PROMPTS_LOV_REFRESHED);
else
parent.eventManager.notify(parent._EVT_PROMPTS_LOV_REFRESHED,n);
}
else
parent.eventManager.notify(parent._EVT_PROMPTS_VALUES_CHANGED,n);
if(parent._showQueryPanel)
parent.eventManager.notify(parent._EVT_DOC_UPDATE);
else
parent.eventManager.notify(parent._EVT_REFRESH_DATA);
parent.launchRefreshWaitDlg("Retrieving Data",true,true,true);
}
else
parent.showWaitDlg();
finalize();
}
else
parent.showAlertDialog("You must enter or select one or more values to answer the prompts before you can run the query.","Prompts",1);
}
function isAnsweredPrompts()
{
var blnRet=true;
for (var i=0;i<promptsUI.length;i++)
{
var pt=promptsUI.getPrompt(i);
if (!pt.isNestedPrompt())
{
if (pt.isRequired && _curDoc.frmPrompt.elements[pt.paramName].value=="")
{
blnRet=false;
pListWidget.selectByData(i);
break;
}
}
}
return blnRet;
}
function cancelCB()
{
promptsDlg.show(false);
finalize();
if (strSRC=="BCA")
parent.goBackSchedule();
else if (strNEV=="yes")
{
if (backURL!="")
{
parent._dontCloseDoc=true;
parent.location=backURL;
}
else
parent.backToParent();
}
else if (strCancel!="")
{
var previousURL=parent.allUseDictionary.get(strCancel);
parent.frameNav("Report",previousURL);
}
}
function resizeCB(w,h)
{
}
function displayErrorMsg(strMsg)
{
parent.showAlertDialog(strMsg,strPromptsDlgTitle,2);
}
function finalize()
{
var l=promptsDlg.layer;
for (var i in _widgets)
{
var w=_widgets[i];
w.layer=null;
_widgets[i]=null;
}
f=_curDoc.frmPrompt;
f.parentNode.removeChild(f);
l.parentNode.removeChild(l);
prompts=null;
promptsUI=null;
}

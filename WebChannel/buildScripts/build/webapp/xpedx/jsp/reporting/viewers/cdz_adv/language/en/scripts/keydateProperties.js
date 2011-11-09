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
*/var labelDlgTitle="Keydate Properties";
var labelUseDefaultDate="Use the default date";
var labelSetDate="Set a date";
var labelUseDefaultDateAllQueries="Use the default date for all queries";
var labelSetDateForAllQueries="Set date for all queries";
var labelSetDateForEachQuery="Set a date for each query";
var labelLastAvailable="Last available";
var labelPromptRefreshData="Prompt on data refresh";
var labelOKButton="OK";
var labelCancelButton="Cancel";
var labelInfoZone="More Information";
var labelParseDateFailed="You must enter a valid date.";
var arrTypes=new Array(2);
arrTypes[0]="Default";
arrTypes[1]="Specific";
var arrTitles=new Array(2);
arrTitles[0]="Query name";
arrTitles[1]="Keydate";
function newDPKeyDate()
{
var o=new Object;
o.name=null;
o.caption=null;
o.type=0;
o.defaultValue=null;
o.specifiedValue=null;
o.required=false;
return o;
}
var dateFormat=p._inputFormatDate;
var dtCurrentDate=formatDate(new Date(),dateFormat);
var isNotRefreshAction=false;
var isMultiKeyDates=false;
var initialized=false;
function loadDialog()
{
var arrDPKeyDates=new Array();
if (typeof(arrData)=="undefined")
arrData=new Array();
for (var i=0; i<arrData.length; i++)
{
var data=arrData[i];
var dp=arrDPKeyDates[i]=newDPKeyDate();
dp.name=data[0];
dp.caption=data[5];
dp.type=data[1];
dp.defaultValue=(data[2]==p._keydateLastAvailable)?labelLastAvailable:formatSerializedDate(data[2]);
dp.specifiedValue=formatSerializedDate(data[3]);
dp.required=data[4];
}
isMultiKeyDates=(arrData.length>1)?true:false;
isNotRefreshAction=(strAction!="Refresh" && strAction!="RunQuery")?true:false;
var gridW=486;
var gridH=100;
var dateValueW=136;
var frameZoneW=(isMultiKeyDates)?500:380;
var frameZoneH=(isMultiKeyDates)?220:72;
frameZoneH+=(isNotRefreshAction)?30:0;
if (!initialized)
{
initialized=true;
keydatePropDlg=newDialogBoxWidget("keydatePropDlg",labelDlgTitle,null,null,okCB,cancelCB,false);
frameZone=newFrameZoneWidget("keydatePropFrameZone",frameZoneW,frameZoneH,false);
optUseDefaultDateAll=newRadioWidget("keydatePropOpt_UseDefaultDateAll","keydatePropOptGroup","",optionChangeCB,false,"",0,0);
optSetDateAll=newRadioWidget("keydatePropOpt_SetDateAll","keydatePropOptGroup","",optionChangeCB,false,"",0,0);
txtSetDateAll=newCalendarTextFieldButton("keydatePropSetDateAll_Calendar","keydatePropSetDateAll_Text",onDateChangedCB,null,null,null,true,null,dateValueW,null,null,dateFormat);
optSetDateEach=newRadioWidget("keydatePropOpt_SetDateEach","keydatePropOptGroup",labelSetDateForEachQuery,optionChangeCB,false,"",0,0);
gridSetDateEach=newGrid("keydatePropGrid",gridW,gridH,arrTitles,dateFormat,onDateChangedCB);
ckPromptOnRefresh=newCheckWidget("keydatePropRefreshData",labelPromptRefreshData);
infozone=newInfoWidget("keydatePropInfozone",labelInfoZone,"","");
okButton=newButtonWidget("keydatePropOKButton",labelOKButton,"okCB()",60);
cancelButton=newButtonWidget("keydatePropCancelButton",labelCancelButton,"cancelCB()",60);
keydatePropDlg.attachDefaultButton(okButton);
}
if (!_curWin.hLinkDlgInitialized)
{
_curWin.hLinkDlgInitialized=true;
targetApp
(
keydatePropDlg.beginHTML()+
'<table style="margin-left:3px;margin-right:3px" border="0" cellspacing="0" cellpadding="0" class="dialogzone"><tbody>'+
'<tr>'+
'<td>'+
'<table style="margin-left:3px;margin-bottom:3px" border="0" cellspacing="0" cellpadding="0" class="dialogzonebold">'+
'<tr>'+
'<td>'+
'<div id="'+keydatePropDlg.id+'_caption" style="width:'+(frameZoneW-15)+'px">&nbsp;</div>'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
frameZone.beginHTML()+
'<table border="0" cellspacing="0" cellpadding="2" width="100%">'+
'<tr>'+
'<td>'+
'<table border="0" cellspacing="0" cellpadding="0">'+
'<tr>'+
'<td>'+
optUseDefaultDateAll.getHTML()+
'</td>'+
'<td>'+
getSpace(10,1)+
'</td>'+
'<td id="'+optUseDefaultDateAll.id+'_txtValue" class="dialogzone">&nbsp;</td>'+
'</tr>'+
'<tr>'+
'<td colspan="3">'+
getSpace(1,2)+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
optSetDateAll.getHTML()+
'</td>'+
'<td>'+
getSpace(10,1)+
'</td>'+
'<td>'+
txtSetDateAll.getHTML()+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'<tr id="'+optSetDateEach.id+'_container" style="display:none">'+
'<td>'+
optSetDateEach.getHTML()+
'</td>'+
'</tr>'+
'<tr id="'+gridSetDateEach.id+'_container" style="display:none">'+
'<td>'+
gridSetDateEach.getHTML()+
'</td>'+
'</tr>'+
'<tr id="PromptOnRefresh_separator" style="display:none">'+
'<td>'+
getSep(0,false)+
'</td>'+
'</tr>'+
'<tr id="'+ckPromptOnRefresh.id+'_container" style="display:none">'+
'<td>'+
ckPromptOnRefresh.getHTML()+
'</td>'+
'</tr>'+
'</table>'+
frameZone.endHTML()+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
getSpace(1,10)+
'</td>'+
'</tr>'+
'<tr>'+
'<td width="100%">'+
infozone.getHTML()+
'</td>'+
'</tr>'+
'<tr>'+
'<td align="right">'+
'<table style="margin-top:10px" border="0" cellspacing="0" cellpadding="0">'+
'<tr>'+
'<td>'+
'<table style="margin-bottom:4px" border="0" cellspacing="0" cellpadding="0">'+
'<tr>'+
'<td>'+
okButton.getHTML()+
'</td>'+
'<td>'+
getSpace(10,1)+
'</td>'+
'<td>'+
cancelButton.getHTML()+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</tbody></table>'+
keydatePropDlg.endHTML()
)
}
keydatePropDlg.init();
frameZone.init();
optUseDefaultDateAll.init();
optSetDateAll.init();
txtSetDateAll.init();
optSetDateEach.init();
gridSetDateEach.init();
ckPromptOnRefresh.init();
infozone.init();
okButton.init();
cancelButton.init();
optUseDefaultDateAll.setText((isMultiKeyDates)?labelUseDefaultDateAllQueries:labelUseDefaultDate);
optSetDateAll.setText((isMultiKeyDates)?labelSetDateForAllQueries:labelSetDate);
if (arrDPKeyDates.length>0)
{
var layer=getLayer(keydatePropDlg.id+"_caption");
layer.innerHTML=(isMultiKeyDates)?'&nbsp;':arrDPKeyDates[0].caption;
layer=getLayer(optUseDefaultDateAll.id+"_txtValue");
layer.innerHTML=(isMultiKeyDates)?'&nbsp;':'('+arrDPKeyDates[0].defaultValue+')';
}
var layer=getLayer("PromptOnRefresh_separator");
layer.style.display=(isNotRefreshAction)?'':'none';
layer=getLayer(ckPromptOnRefresh.id+"_container");
layer.style.display=(isNotRefreshAction)?'':'none';
getLayer(keydatePropDlg.id+"_caption").style.width=''+(frameZoneW-15)+'px';
frameZone.css.width=''+frameZoneW+'px';
frameZone.css.height=''+frameZoneH+'px';
if (arrDPKeyDates.length>0)
{
layer=getLayer(optSetDateEach.id+"_container");
layer.style.display=(isMultiKeyDates)?'':'none';
layer=getLayer(gridSetDateEach.id+"_container");
layer.style.display=(isMultiKeyDates)?'':'none';
}
gridSetDateEach.addRows(arrDPKeyDates);
ckPromptOnRefresh.check(blnPromptOnRefresh);
if (isMultiKeyDates)
{
var ps=(typeof(previousState)!="undefined")?previousState:1;
var cps=getCalculatedPreviousState(arrDPKeyDates);
if (ps!=cps)
{
if (cps==3 || ps==3)
ps=3;
else
ps=cps;
}
}
else
ps=getCalculatedPreviousState(arrDPKeyDates);
var id="";
switch(ps)
{
case 1:
optUseDefaultDateAll.check(true);
id=optUseDefaultDateAll.id;
break;
case 2:
optSetDateAll.check(true);
id=optSetDateAll.id;
txtSetDateAll.setValue(arrDPKeyDates[0].specifiedValue);
break;
case 3:
optSetDateEach.check(true);
id=optSetDateEach.id;
break;
}
if (id=="")
{
optUseDefaultDateAll.check(true);
id=optUseDefaultDateAll.id;
}
var iPos=id.indexOf('_');
if (iPos>-1)
{
var opt=id.substring(iPos+1);
selectOption(opt);
}
p.hideWaitDlg();
keydatePropDlg.show(true);
}
function onDateChangedCB()
{
var o=this;
var v=trim(o.getValue());
if (v!='')
{
var sd=parseDate(o);
if (sd=='')
p.advDisplayViewerErrorMsgDlg(labelParseDateFailed,labelDlgTitle);
}
else
o.setValue(v);
}
function optionChangeCB()
{
var o=this;
var iPos=o.id.indexOf('_');
if (iPos>-1)
{
var opt=o.id.substring(iPos+1);
selectOption(opt);
}
}
function selectOption(opt)
{
switch(opt)
{
case "UseDefaultDateAll":
txtSetDateAll.setDisabled(true);
gridSetDateEach.setDisabled(true);
break;
case "SetDateAll":
gridSetDateEach.setDisabled(true);
txtSetDateAll.setDisabled(false);
if (txtSetDateAll.getValue()=="")
txtSetDateAll.setValue(dtCurrentDate);
break;
case "SetDateEach":
txtSetDateAll.setDisabled(true);
gridSetDateEach.setDisabled(false);
break;
default:
txtSetDateAll.setDisabled(true);
gridSetDateEach.setDisabled(true);
}
}
function formatSerializedDate(sdt)
{
var ret=sdt;
if (sdt!='' && sdt!=p._keydateLastAvailable)
{
var dt=new Date();
dt.setDate(1);
var arr=sdt.split(',');
dt.setYear(arr[2]);
dt.setMonth(arr[0]-1);
dt.setDate(arr[1]);
ret=formatDate(dt,dateFormat);
}
return ret;
}
function parseDate(obj)
{
var dt=obj.getValue();
var r=setDateValue(dt,dateFormat);
r=(r==',,')?'':r;
return r;
}
var txtDateToParse=null;
function parseErrorCB()
{
if (txtDateToParse!=null)
{
txtDateToParse.text.layer.focus();
txtDateToParse=null;
}
}
function isAnsweredKeydates(frm)
{
var blnRet=true;
if (optUseDefaultDateAll.isChecked())
{
for (var i=1;i<=arrData.length;i++)
{
frm.elements["KDV"+i].value='';
}
}
else if (optSetDateAll.isChecked())
{
var v=parseDate(txtSetDateAll);
if (v!='')
{
for (var i=1;i<=arrData.length;i++)
{
var elementName="KDV"+i;
frm.elements[elementName].value=v;
}
}
else
{
txtDateToParse=txtSetDateAll;
p.advDisplayViewerErrorMsgDlg(labelParseDateFailed,labelDlgTitle,parseErrorCB);
blnRet=false;
}
}
else if (optSetDateEach.isChecked())
{
var cbType=gridSetDateEach.cboDateType;
var txtDate=gridSetDateEach.txtDateValue;
for (var i=0; i<cbType.length; i++)
{
var elementName="KDV"+(i+1);
var sel=cbType[i].getSelection();
if (sel.value=="1")
{
var v=parseDate(txtDate[i]);
if (v!='')
frm.elements[elementName].value=v;
else
{
txtDateToParse=txtDate[i];
p.advDisplayViewerErrorMsgDlg(labelParseDateFailed,labelDlgTitle,parseErrorCB);
blnRet=false;
break;
}
}
else
frm.elements[elementName].value='';
}
}
return blnRet;
}
function getCurrentSelectionState()
{
var ret=1;
if (optUseDefaultDateAll.isChecked())
ret=1;
else if (optSetDateAll.isChecked())
ret=2;
else if (optSetDateEach.isChecked())
ret=3;
return ret;
}
function setCurrentDPKeydateInfo()
{
var frm=document.frmKeyDateProperties;
var qf=p.getQueryFrame();
var dpName = p.getSelectedDPName();
if(qf)
{
qf._currentKeydate = (qf._currentDP!=null)?qf._currentDP.keydateValue:null; 
if(qf._currentKeydate!=null)
{
for (var i=1;i<=arrData.length;i++)
{
if(arrData[i-1][0] == qf._currentDP.name)
{
if(frm.elements["KDV"+i].value == '') 
{
qf._currentKeydate[1]=0;
}
else
{
qf._currentKeydate[1]=1;
qf._currentKeydate[3]=frm.elements["KDV"+i].value;
}
break;
}
}
}
}
}
function getCalculatedPreviousState(arrDPKeyDates)
{
var cps=1;
var nbKeyDates=arrDPKeyDates.length;
var value=0;
for (var i=0;i<nbKeyDates;i++)
{
value+=arrDPKeyDates[i].type;
}
if (value==0)
{
cps=1;
}
else if (value==nbKeyDates)
{
var blnSameDate=true;
for (var i=1;i<nbKeyDates;i++)
{
if (arrDPKeyDates[i-1].specifiedValue!=arrDPKeyDates[i].specifiedValue)
{
blnSameDate=false;
break;
}
}
cps=(blnSameDate)?2:3;
}
else
{
cps=3
}
return cps;
}
function okCB()
{
var frm=document.frmKeyDateProperties;
if (isAnsweredKeydates(frm))
{
keydatePropDlg.show(false);
if (isNotRefreshAction)
{
frm.elements["sPOR"].value=(ckPromptOnRefresh.isChecked())?"true":"false";
setCurrentDPKeydateInfo();
}
else
frm.elements["sPOR"].value='';
frm.elements["sCurState"].value=''+((isMultiKeyDates)?getCurrentSelectionState():'');
var url="processKeydateProperties"+p._appExt+"?"+strQueryString;
frm.action=url;
frm.submit();
}
}
function cancelCB()
{
keydatePropDlg.show(false);
if (strNEV=="yes")
parent.backToParent();
}

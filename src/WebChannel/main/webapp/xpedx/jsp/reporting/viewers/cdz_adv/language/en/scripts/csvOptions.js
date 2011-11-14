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
*/_charset_UTF8="Unicode";
_charset_ar="Arabic";
_charset_cs="Central European";
_charset_de="Western European";
_charset_el="Greek";
_charset_en="Western European";
_charset_es="Western European";
_charset_fr="Western European";
_charset_he="Hebrew";
_charset_it="Western European";
_charset_ja="Japanese";
_charset_ko="Korean";
_charset_nl="Western European";
_charset_pl="Central European";
_charset_pt="Western European";
_charset_ru="Cyrillic";
_charset_sv="Nordic";
_charset_th="Thai";
_charset_tr="Turkish";
_charset_zh_CN="Traditional Chinese";
_charset_zh_TW="Modern Chinese";
strCSVOptionsDlgTitle="Save as CSV - Options";
initialized=false;
function loadDialog()
{
if (!initialized)
{
initialized=true;
csvOpDlg=newDialogBoxWidget("csvOpDlg",strCSVOptionsDlgTitle,400,null,submitCB,closeCB,false);
cbCharDelimiter=newComboWidget("cbCharDelimiter",null,true,50,"");
cbColSep=newComboWidget("cbColSep",null,true,50,"");
cbCharset=newComboWidget("cbCharset",null,true,240,"");
txtCharset=newTextFieldWidget("txtCharset",null,null,null,null,true,"Enter a new charset",90);
ckNewCharset=newCheckWidget("ckNewCharset","Enter a new charset",ckCharsetCB);
ckSetDefault=newCheckWidget("ckSetDefault","Set as default values",null);
infozone=newInfoWidget("csvOpInfozone","More Information","","Use this dialog box to set the options for export to a text file. You can specify the text qualifier, column delimiter and character set of the file. To make your choices the default settings, click Set as default values.");
frameZone=newFrameZoneWidget("csvOpframeZone",394,150,false);
OKButton=newButtonWidget("csvopOKButton","OK","submitCB()",60);
CancelButton=newButtonWidget("csvopCancelButton","Cancel","closeCB()",60);
}
targetApp
(
csvOpDlg.beginHTML()+
'<form style="padding:0px;margin:0px" name="frmCSVOp" id="frmCSVOp" method="post" onSubmit="return false;">'+
'<table border="0" cellspacing="3" cellpadding="0" class="dialogzone"><tbody>'+
'<tr>'+
'<td class="dialogzone">&nbsp;'+'When saving as CSV, use the following values:'+'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
frameZone.beginHTML()+
'<table border="0" cellspacing="0" width="100%" cellpadding="0">'+
'<tr>'+
'<td>'+getSpace(5,5)+'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
'<table border="0" cellspacing="0" cellpadding="0">'+
'<tr>'+
'<td class="dialogzone"><label for="'+cbCharDelimiter.id+'">'+'Text qualifier'+'</label></td>'+
'<td>'+getSpace(5,1)+'</td>'+
'<td>'+
cbCharDelimiter.getHTML()+
'</td>'+
'</tr>'+
'<tr>'+
'<td colspan="3">'+getSpace(5,5)+'</td>'+
'</tr>'+
'<tr>'+
'<td class="dialogzone"><label for="'+cbColSep.id+'">'+'Column delimiter'+'</label></td>'+
'<td>'+getSpace(5,1)+'</td>'+
'<td>'+
cbColSep.getHTML()+
'</td>'+
'</tr>'+
'<tr>'+
'<td colspan="3">'+getSpace(5,5)+'</td>'+
'</tr>'+
'<tr>'+
'<td class="dialogzone"><label for="'+cbCharset.id+'">'+'Charset:'+'</label></td>'+
'<td>'+getSpace(5,1)+'</td>'+
'<td>'+
cbCharset.getHTML()+
'</td>'+
'</tr>'+
'<tr>'+
'<td colspan="3">'+getSpace(5,5)+'</td>'+
'</tr>'+
'<tr>'+
'<td colspan="3">'+
'<table border="0" cellspacing="0" cellpadding="0">'+
'<tr>'+
'<td>'+
ckNewCharset.getHTML()+
'</td>'+
'<td>'+getSpace(5,1)+'</td>'+
'<td id="idEditCharset" style="visibility:hidden">'+
txtCharset.getHTML()+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+getSep()+'</td>'+
'</tr>'+
'<tr>'+
'<td align="right">'+
ckSetDefault.getHTML()+
'</td>'+
'</tr>'+
'</table>'+
frameZone.endHTML()+
'</td>'+
'<tr>'+
'<td>'+getSpace(5,5)+'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
infozone.getHTML()+
'</td>'+
'</tr>'+
'<tr>'+
'<td align="right" valign="center">'+
'<table cellspacing="0" cellpadding="0" border="0"><tbody>'+
'<tr>'+
'<td>'+
OKButton.getHTML()+
'</td>'+
'<td>'+getSpace(5,1)+'</td>'+
'<td>'+
CancelButton.getHTML()+
'</td>'+
'</tr>'+
'</tbody></table>'+
'</td>'+
'</tr>'+
'</tbody></table>'+
'</form>'+
csvOpDlg.endHTML()
)
csvOpDlg.init();
cbCharDelimiter.init();
cbColSep.init();
cbCharset.init();
txtCharset.init();
ckNewCharset.init();
ckSetDefault.init();
infozone.init();
frameZone.init();
OKButton.init();
CancelButton.init();
csvOpDlg.attachDefaultButton(OKButton);
var blnSelect=false;
var arr=delimiters.split(' ');
cbCharDelimiter.del();
for (var i=0;i<arr.length;i++)
{
blnSelect=(arr[i]==strCharDelimiter)?true:false;
cbCharDelimiter.add(arr[i],arr[i],blnSelect);
}
arr=separators.split(' ');
cbColSep.del();
for (var i=0;i<arr.length;i++)
{
blnSelect=(arr[i]==strColumnSeparator)?true:false;
cbColSep.add(arr[i],arr[i],blnSelect);
}
arrCharsets.sort();
var blnSelected=false;
var strSelCharset=(useDefaultValues)?strOptCharSet:strDefaultCharSet;
cbCharset.del();
for (var i=0;i<arrCharsets.length;i++)
{
var blnFound=false;
for (var j=0;j<i;j++)
{
if (arrCharsets[j][0]==arrCharsets[i][0])
{
blnFound=true;
break;
}
}
if (!blnFound)
{
var strLabel="";
if (typeof(charsetLabels)!="undefined")
{
var strLabel=charsetLabels.get(arrCharsets[i][1],"");
if (strLabel!="") strLabel=' ('+strLabel+')';
}
arrCharsets[i][1]=arrCharsets[i][0]+strLabel;
if (!blnSelected && arrCharsets[i][0]==strSelCharset)
{
blnSelected=true;
cbCharset.add(arrCharsets[i][1],arrCharsets[i][0],true);
}
else
cbCharset.add(arrCharsets[i][1],arrCharsets[i][0],false);
}
}
if (useDefaultValues && !blnSelected)
{
txtCharset.setValue(strOptCharSet);
ckNewCharset.check(true);
_curDoc.getElementById("idEditCharset").style.visibility="visible";
cbCharset.valueSelect(strDefaultCharSet);
}
ckSetDefault.check(useDefaultValues);
p.hideWaitDlg();
csvOpDlg.show(true);
}
function ckCharsetCB()
{
var obj=_curDoc.getElementById("idEditCharset");
if (this.isChecked())
{
obj.style.visibility="visible";
_curDoc.getElementById(txtCharset.id).focus();
}
else
obj.style.visibility="hidden";
}
function submitCB()
{
csvOpDlg.show(false);
setTimeout("delayedSubmit()",1);
}
function delayedSubmit()
{
if (ckNewCharset.isChecked())
{
var strTxtCharset=trim(txtCharset.getValue());
if (strTxtCharset=="")
ckNewCharset.check(false);
}
var url="processCSVOptions"+p._appExt+"?"+strQueryString;
_curDoc.frmCSVOp.action=url;
_curDoc.frmCSVOp.target=self.name;
_curDoc.frmCSVOp.submit();
finalize();
}
function closeCB()
{
csvOpDlg.show(false);
finalize();
}
function helpCB()
{
}
function finalize()
{
var l=csvOpDlg.layer;
for (var i in _widgets)
{
var w=_widgets[i];
w.layer=null;
_widgets[i]=null;
}
l.parentNode.removeChild(l);
}
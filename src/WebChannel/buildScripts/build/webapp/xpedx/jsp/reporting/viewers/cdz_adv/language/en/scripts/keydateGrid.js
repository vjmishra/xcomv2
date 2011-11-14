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
*/_rowKeydateGridPrefix="QR";
function newGrid(id,w,h,titles,format,changeCB)
{
var o=newWidget(id);
o.w=(w==null?100:w);
o.h=(h==null?400:h);
o.borderW=1;
o.bgClass='solidBorder';
o.innerW=Math.max(0,o.w-(2*(o.borderW)));
o.innerH=Math.max(0,o.h-(2*(o.borderW)));
o.format=format;
o.changeCB=changeCB;
o.getHTML=Grid_getHTML;
o.oldInit=o.init;
o.init=Grid_init;
o.oldResize=o.resize;
o.resize=Grid_resize;
o.fillHeader=Grid_fillHeader;
o.setDisabled=Grid_setDisabled;
o.addRows=Grid_addRows;
o.headH=19;
o.colW=0;
o.data=new Array;
o.titles=(titles)?titles:new Array;
return o;
}
function Grid_getHTML()
{
var o=this,s=new Array,i=0;
var scrollbarW=15;
var w=""+(_dtd4?o.innerW:o.w)+"px";
var h=""+(_dtd4?o.innerH:o.h)+"px";
s[i++]='<div align="left" onselectstart="return false" class="'+o.bgClass+'" id="'+o.id+'" style="border-width:'+o.borderW+'px;padding:0px;'+sty("width",w)+'">';
s[i++]='<div id="'+o.id+'_header" style="'+backImgOffset(_skin+'multih.gif',0,0)+sty("width",""+o.innerW+"px")+'overflow:hidden;height:'+o.headH+'px">'
o.fillHeader(s);
i=s.length;
s[i++]='</div>';
s[i++]='<div id="'+o.id+'_body" align="left" onselectstart="return false" style="overflow:auto;'+sty("width",w-scrollbarW)+sty("height",h)+'">';
s[i++]='</div>';
s[i++]='</div>';
return s.join("");
}
function Grid_fillHeader(s)
{
var o=this,i=s.length;
var tableW="100%";
if (o.titles!=null)
{
var len=o.titles.length;
o.colW=(len>0)?Math.floor(o.w/len):0;
s[i++]='<table onmousedown="return false" height="'+o.headH+'" width="'+tableW+'" cellpadding="0" cellspacing="0" border="0"><tbody>'+
   '<tr valign="middle" align="left">';
for (var j=0; j<len;j++)
{
var tW=0;
if (len==1)
tW=(o.w-(_dtd4?10:6));
else
tW=(o.colW-(_dtd4?10:6));
s[i++]='<td><div align="center" class="mclH" style="cursor:default;width:'+tW+'px">'+convStr(arrTitles[j])+'</div></td><td width="0"></td>';
if (len>1 && j<len-1)
s[i++]='<td align="right" valign="middle" style="cursor:default" width="6">'+simpleImgOffset(_skin+"iconsep.gif",2,o.headH-7,2,3,' id="'+o.id+'" ')+'</td>';
}
s[i++]='</tr></tbody></table>';
}
}
function Grid_init()
{
var o=this;
o.oldInit();
}
function Grid_addRows(data)
{
var o=this;
o.data=(data)?data:new Array;
o.comboW=70;
o.dateValueW=136;
if (o.data.length>0)
{
for (var i=0; i<o.data.length; i++)
{
var rowId=_rowKeydateGridPrefix+i+"_"+o.id+"_";
if (typeof(o.cboDateType)=="undefined") o.cboDateType=new Array(o.data.length);
o.cboDateType[i]=newComboWidget(rowId+"type",onDateTypeChangedCB,true,o.comboW);
o.cboDateType[i].rowId=rowId;
o.cboDateType[i].par=o;
if (typeof(o.txtDateValue)=="undefined") o.txtDateValue=new Array(o.data.length);
o.txtDateValue[i]=newCalendarTextFieldButton(rowId+"calendar",rowId+"text",o.changeCB,null,null,null,true,null,o.dateValueW,null,null,o.format);
}
var tableW="100%";
var paddingW=2;
if (o.titles!=null)
{
var len=o.titles.length;
o.colW=(len>0)?Math.floor(o.w/len):0;
}
var s=new Array();
s[i++]='<table border="0" class="bgzone" cellpadding="0" cellspacing="0"><tbody>';
for (var j=0; j<o.data.length;j++)
{
var rowId=_rowKeydateGridPrefix+j+"_"+o.id+"_";
var label=o.data[j].name;
var defaultValue=o.data[j].defaultValue;
s[i++]='<tr valign="middle" align="left" style="height:24px">'+
'<td>'+
'<div style="overflow:hidden;text-overflow:ellipsis;width:'+o.colW+'px;padding-left:'+paddingW+'px">'+
'<label style="white-space:nowrap;" title="'+label+'">'+convStr(label)+'</label>'+
'</div>'+
'</td>'+
'<td>'+
'<table border="0" style="margin-left:'+paddingW+'px" cellspacing="0" cellpadding="0">'+
'<tr>'+
'<td>'+o.cboDateType[j].getHTML()+'</td>'+
'<td>'+getSpace(2,1)+'</td>'+
'<td>'+
'<table border="0" cellspacing="0" cellpadding="0" style="width:'+o.dateValueW+'px">'+
'<tr id="'+rowId+'TextField">'+
'<td align="left" class="calendarTextPart">'+
'<label style="white-space:nowrap;" title="'+defaultValue+'"><i>'+convStr(defaultValue)+'</i></label>'+
'</td>'+
'</tr>'+
'<tr id="'+rowId+'CalendarField" style="display:none">'+
'<td>'+o.txtDateValue[j].getHTML()+'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>';
}
s[i++]='</tbody></table>';
getLayer(o.id+"_body").innerHTML=s.join('');
if (typeof(o.cboDateType)!="undefined")
{
for (var i=0; i<o.cboDateType.length; i++)
{
if (o.cboDateType[i]!=null)
{
o.cboDateType[i].init();
o.cboDateType[i].del();
for (var j=0; j<arrTypes.length; j++)
{
var bSel=(o.data[i].type==j)?true:false;
o.cboDateType[i].add(arrTypes[j],j,bSel);
}
}
}
}
if (typeof(o.txtDateValue)!="undefined")
{
for (var i=0; i<o.txtDateValue.length; i++)
{
if (o.txtDateValue[i]!=null)
{
o.txtDateValue[i].init();
o.txtDateValue[i].setValue(o.data[i].specifiedValue);
var rowId=_rowKeydateGridPrefix+i+"_"+o.id+"_";
var df=(o.data[i].type==0)?true:false;
getLayer(rowId+"TextField").style.display=df?'':'none';
getLayer(rowId+"CalendarField").style.display=df?'none':'';
}
}
}
}
}
function Grid_delRows()
{
var o=this;
getLayer(o.id+"_body").innerHTML='';
o.cboDateType=new Array();
o.txtDateValue=new Array();
o.init();
}
function Grid_setDisabled(b)
{
var o=this;
for (var i=0; i<o.data.length; i++)
{
if (o.cboDateType[i]!=null)
o.cboDateType[i].setDisabled(b,false);
if (o.txtDateValue[i]!=null)
o.txtDateValue[i].setDisabled(b);
}
if (typeof(o.cboDateType)!="undefined")
{
for (var i=0; i<o.cboDateType.length; i++)
{
if (o.cboDateType[i]!=null)
{
var sel=o.cboDateType[i].getSelection();
o.cboDateType[i].init();
o.cboDateType[i].del();
for (var j=0; j<arrTypes.length; j++)
{
var bSel=(sel.index==j)?true:false;
o.cboDateType[i].add(arrTypes[j],j,bSel);
}
}
}
}
}
function Grid_resize()
{
}
function onDateTypeChangedCB()
{
var o=this;
var v=parseInt(o.getSelection().value);
var df=(v==0)?true:false;
getLayer(o.rowId+"TextField").style.display=df?'':'none';
getLayer(o.rowId+"CalendarField").style.display=df?'none':'';
if (!df)
{
var i=getRowIndex(o.rowId);
if (i>-1)
{
var par=o.par;
if (par.txtDateValue[i].getValue()=="" && typeof(dtCurrentDate)!="undefined")
par.txtDateValue[i].setValue(dtCurrentDate);
}
}
}
function getRowIndex(row)
{
var rIndex=-1;
var iPos=row.indexOf('_');
if (iPos>-1)
{
var r=row.substring(0,iPos);
rIndex=parseInt(r.substring(_rowKeydateGridPrefix.length))
}
return rIndex;
}

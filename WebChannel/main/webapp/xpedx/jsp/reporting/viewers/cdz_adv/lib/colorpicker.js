if (window._DHTML_LIB_COLORPICKER_JS_LOADED==null)
{
_DHTML_LIB_COLORPICKER_JS_LOADED = true;
_colorPickerLosangeColor = new Array(
new Array("", "", "", "0.49.99", "49.99.156", "49.99.206", "0.49.156", "0.0.156", "0.0.206", "0.0.99", "", "", ""),
new Array("", "", "0.99.99", "0.99.156", "0.156.206", "0.99.206", "0.49.206", "0.0.255", "49.49.255", "49.49.156", "", "", ""),
new Array("", "", "0.132.132", "0.156.156", "49.206.206", "0.206.255", "0.156.255", "0.99.255", "49.99.255", "49.49.206", "99.99.156", "", ""),
new Array("", "49.156.99", "0.206.156", "0.255.206", "0.255.255", "49.206.255", "49.156.255", "99.156.255", "99.99.255", "99.0.255", "99.0.206", "", ""),
new Array("", "49.156.49", "0.206.99", "0.255.156", "99.255.206", "99.255.255", "99.206.255", "156.206.255", "156.156.255", "156.99.255", "156.49.255", "156.0.255", ""),
new Array("0.99.0", "0.206.0", "0.255.00", "99.255.156", "156.255.206", "206.255.255", "206.239.255", "206.206.255", "206.156.255", "206.99.255", "206.0.255", "156.0.206", ""),
new Array("0.49.0", "0.132.0", "49.206.49", "99.255.99", "156.255.156", "206.255.206", "255.255.255", "255.206.255", "255.156.255", "255.99.255", "255.0.255", "206.0.206", "99.0.9"),
new Array("49.99.0", "0.156.0", "99.255.49", "156.255.99", "206.255.156", "255.255.206", "255.206.206", "255.156.206", "255.99.206", "255.49.206", "206.0.156", "132.0.132", ""),
new Array("", "49.49.0", "99.156.0", "156.255.49", "206.255.99", "255.255.156", "255.206.156", "255.156.156", "255.99.156", "255.49.156", "206.49.156", "156.0.156", ""),
new Array("", "99.99.49", "156.206.0", "206.255.49", "255.255.99", "255.206.99", "255.156.99", "255.123.132", "255.0.99", "214.0.148", "156.49.99", "", ""),
new Array("", "", "132.132.0", "206.206.0", "255.255.0", "255.206.0", "255.156.49", "255.99.0", "255.82.82", "206.0.99", "99.0.49", "", ""),
new Array("", "", "156.99.49", "206.156.0", "255.156.0", "206.99.0", "255.49.0", "255.0.0", "206.0.0", "156.0.49", "206.0.99", "", "", ""),
new Array("", "", "", "99.49.0", "156.99.0", "206.49.0", "156.49.0", "156.0.0", "132.0.0", "165.0.33", "", "", "")
);
_colorPickerLinesBW = new Array(
new Array("255.255.255", "222.222.222", "181.181.181", "132.132.132", "90.90.90", "49.49.49", "24.24.24", "8.8.8"),
new Array("239.239.239", "198.198.198", "148.148.148", "115.115.115", "74.74.74", "41.41.41", "16.16.16", "")
);
}
function newColorPickerWidget(id, title, okCB)
{
if (title == null)
{
title = _colorPickerDlgTitle;
}
var o= newDialogBoxWidget(id, title, null, null, ColorPickerWidget_okCB, ColorPickerWidget_cancelDialogCB);
o.setColor = ColorPickerWidget_setColor;
o.getColor = ColorPickerWidget_getColor;
o.getHTML = ColorPickerWidget_getHTML;
o.oldPickerInit = o.init;
o.init = ColorPickerWidget_init;
o.oldPickerShow = o.show;
o.show = ColorPickerWidget_show;
o.okCB = okCB;
o.initialColor = newBOColor(255,255,255);
o.currentColor = newBOColor(255,255,255);
o.getColorInZone = ColorPickerWidget_getColorInZone;
o.updateNewColor = ColorPickerWidget_updateNewColor;
o.titleColorFrame = newWidget(o.id+"titleColorFrame");
o.colorFrame =  newFrameZoneWidget(o.id+"colorFrame", 330, 177);
o.colorMap = newWidget(o.id+"colorMap");
o.bwMap = newWidget(o.id+"bwMap");
o.redWidget = newIntFieldWidget(o.id+"redWidget", ColorPickerWidget_redCB, 3, ColorPickerWidget_redCB, ColorPickerWidget_redCB,null,null,30);
o.greenWidget = newIntFieldWidget(o.id+"greenWidget", ColorPickerWidget_greenCB, 3, ColorPickerWidget_greenCB, ColorPickerWidget_greenCB,null,null,30);
o.blueWidget = newIntFieldWidget(o.id+"blueWidget", ColorPickerWidget_blueCB, 3, ColorPickerWidget_blueCB, ColorPickerWidget_blueCB,null,null,30);
o.oldColorWidget = newWidget(o.id+"oldColorWidget");
o.newColorWidget = newWidget(o.id+"newColorWidget");
o.info = newInfoWidget(o.id+"pickerInfo",_colorPickerInfoTitle,"",_colorPickerInfoText);
o.OKButton = newButtonWidget(o.id+"pickerOKButton",_libOkButton,ColorPickerWidget_okCB,60);
o.CancelButton = newButtonWidget(o.id+"pickerCancelButton",_libCancelButton,ColorPickerWidget_cancelCB,60);
o.OKButton.parentDialog = o;
o.CancelButton.parentDialog = o;
o.redWidget.parentDialog = o;
o.greenWidget.parentDialog = o;
o.blueWidget.parentDialog = o;
o.colorMap.parentDialog = o;
o.bwMap.parentDialog = o;
o.attachDefaultButton(o.OKButton);
return o;
}
function ColorPickerWidget_init()
{
var o = this;
o.oldPickerInit();
o.titleColorFrame.init();
o.colorFrame.init();
o.redWidget.init();
o.redWidget.setMin(0);
o.redWidget.setMax(255);
o.greenWidget.init();
o.greenWidget.setMin(0);
o.greenWidget.setMax(255);
o.blueWidget.init();
o.blueWidget.setMin(0);
o.blueWidget.setMax(255);
o.colorMap.init();
o.bwMap.init();
o.oldColorWidget.init();
o.newColorWidget.init();
o.info.init();
o.OKButton.init();
o.CancelButton.init();
}
function ColorPickerWidget_show(sh)
{
var o =this;
if (sh)
{
o.oldColorWidget.setBgColor(o.initialColor.getStyleColor());
o.updateNewColor(o.initialColor);
}
o.oldPickerShow(sh);
}
function ColorPickerWidget_setColor(color)
{
if ((color == null) || (color == "-1,-1,-1"))
{
color="255,255,255";
}
this.initialColor.setStringDef(color);
this.currentColor.setStringDef(color);
}
function ColorPickerWidget_getColor()
{
return this.currentColor.getStringDef();
}
function ColorPickerWidget_getHTML()
{
var o=this;
return o.beginHTML() +
'<table cellspacing="10" cellpadding="0" border="0" class="dialogzone"><tbody>'+
'<tr>' +
'<td align="left" valign="top" colspan="2">' +
'<div id="'+o.id+'titleColorFrame">'+_colorPickerFrameTitle+'</div>' +
o.colorFrame.beginHTML() +
'<table cellspacing="0" cellpadding="0" border="0" style="margin:3px"><tbody>' +
'<tr>'+
'<td align="center" valign="top" rowspan="2">'+
img(_skin + "../colorPicker.gif",183,161,null,' style="cursor:' + _hand + '" id="'+o.id+'colorMap" onclick="'+_codeWinName+'.ColorPickerWidget_clickCB(this,event)" ')+
'</td>'+
'<td align="left" valign="top">'+
img(_skin + "../BWPicker.gif",113,29,null,' style="cursor:' + _hand + '" id="'+o.id+'bwMap" onclick="'+_codeWinName+'.ColorPickerWidget_clickCB(this,event)" ')+
'</td>'+
'</tr>' +
'<tr>'+
'<td align="right" valign="bottom">'+
'<table cellspacing="0" cellpadding="0" border="0"><tbody>' +
'<tr>' +
'<td align="right" class="dialogzone">'+
_colorPickerWidgRed +
'</td>'+
'<td align="left">' +
o.redWidget.getHTML() +
'</td>'+
'</tr>' +
'<tr>' +
'<td align="right" class="dialogzone">'+
_colorPickerWidgGreen +
'</td>'+
'<td align="left">' +
o.greenWidget.getHTML() +
'</td>'+
'</tr>' +
'<tr>' +
'<td align="right" class="dialogzone">'+
_colorPickerWidgBlue +
'</td>'+
'<td align="left">' +
o.blueWidget.getHTML() +
'</td>'+
'</tr>' +
'</tbody></table>'+ 
'</td>'+
'</tr>' +
'</tbody></table>'+
o.colorFrame.endHTML() +
'</td>'+
'<td align="center" valign="middle" colspan="1">'+
'<table cellspacing="0" cellpadding="20" border="0"><tbody>' +
'<tr><td align="center" valign="middle" class="dialogzone">'+
'<div id="'+o.id+'oldColorWidget" style="width:35px;height:20px"></div>' +
_colorPickerWidgCurrent +
'</td></tr>' + 
'<tr>' +
'<td align="center" valign="middle" class="dialogzone">' +
'<div id="'+o.id+'newColorWidget" style="width:35px;height:20px"></div>' +
_colorPickerWidgNew +
'</td>'+
'</tr>' +
'</tbody></table>'+ 
'</td>'+
'</tr>'+
'<tr>'+
'<td align="left" colspan="3">'+
o.info.getHTML() +
'</td>'+
'</tr>'+
'<tr>'+
'<td align="right" colspan="3">'+
'<table cellspacing="0" cellpadding="0" border="0"><tbody><tr>'+
'<td>'+o.OKButton.getHTML()+'</td>'+
'<td>'+getSpace(5,1)+'</td>'+
'<td>'+o.CancelButton.getHTML()+'</td>'+
'<td>'+getSpace(5,1)+'</td>'+
'</tr></tbody></table>'+
'</td>'+
'</tr>'+
'</tbody></table>'+ 
o.endHTML();
}
function ColorPickerWidget_updateNewColor(newColor)
{
var o = this;
o.newColorWidget.setBgColor(newColor.getStyleColor());
o.redWidget.setValue(newColor.r);
o.greenWidget.setValue(newColor.g);
o.blueWidget.setValue(newColor.b);
o.currentColor.set(newColor.r, newColor.g, newColor.b);
o.OKButton.setDisabled(o.oldColorWidget.css.backgroundColor == o.newColorWidget.css.backgroundColor);
}
function ColorPickerWidget_getColorInZone(x, y, cellW, cellH, matrix)
{
var i =  y / cellH;
i = Math.max(0, Math.round(i));
var lineOffset = 0;
if (i % 2)
{
lineOffset = Math.round(cellW/2);
}
var j = (x - lineOffset) / cellW;
if (j<0) return ""; 
j = Math.max(0, Math.floor(j));
if ( i >= matrix.length) i = matrix.length - 1;
if ( j >= matrix[0].length) j = matrix[0].length - 1;
var color = matrix[i][j];
if (color == "") return "";
var colors = color.split('.');
return newBOColor(colors[0], colors[1], colors[2]);
}
function ColorPickerWidget_clickCB(layer, e)
{
var o=getWidget(layer).parentDialog;
var id=layer.id;
var lW = 14;
var lH = 12.75;
var x,y;
y = _ie ? e.offsetY : (e.layerY -55);
if (layer.id == o.id+"colorMap")
{
x = _ie ? e.offsetX : (e.layerX -20);
var hexColor = o.getColorInZone( x, y, lW, lH, _colorPickerLosangeColor);
if (hexColor != '')
{
newColor = hexColor.getCopy();
o.updateNewColor(newColor);
}
}
else if (layer.id == o.id+"bwMap")
{
x = _ie ? e.offsetX : (e.layerX -20 -182);
var hexBW = o.getColorInZone( x, y, lW, lH, _colorPickerLinesBW);
if (hexBW != '')
{
newColor = hexBW.getCopy();
o.updateNewColor(newColor);
}
}
}
function ColorPickerWidget_cancelCB()
{
this.parentDialog.show(false);
}
function ColorPickerWidget_cancelDialogCB()
{
this.show(false);
}
function ColorPickerWidget_okCB()
{
var o=this.parentDialog;
o.OKButton.setDisabled(true);
o.show(false);
o.OKButton.setDisabled(false);
if (o.okCB)
o.okCB();
}
function ColorPickerWidget_redCB()
{
var o = this.parentDialog;
var red = o.redWidget.getValue();
if (red == "")
{
red=0;
o.redWidget.setValue(0);
}
o.currentColor.r = red;
o.updateNewColor(o.currentColor);
}
function ColorPickerWidget_greenCB()
{
var o = this.parentDialog;
var green = o.greenWidget.getValue();
if (green == "")
{
green=0;
o.greenWidget.setValue(0);
}
o.currentColor.g = green;
o.updateNewColor(o.currentColor);
}
function ColorPickerWidget_blueCB()
{
var o = this.parentDialog;
var blue = o.blueWidget.getValue();
if (blue == "")
{
blue=0;
o.blueWidget.setValue(0);
}
o.currentColor.b = blue;
o.updateNewColor(o.currentColor);
}
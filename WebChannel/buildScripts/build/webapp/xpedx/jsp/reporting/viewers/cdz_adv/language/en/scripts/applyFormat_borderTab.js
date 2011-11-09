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
*/function createWidgetsForBorderTab(prefix)
{
genBorderThickCombo = newCustomCombo(prefix+"DlgGenBorderThickCombo", applyGenThicknessCB, true, 90)
genBorderColorMenuWidget = newIconColorMenuWidget(prefix+"DlgGenBorderColorMenuWidget",p._img+"format.gif",chgBorderColorCB,null,'Border color',16,16,8*16,0,8*16,16,p._lastUsedColorsAr)
topBorderThickCombo = newCustomCombo(prefix+"DlgTopBorderThickCombo", changeComplexBordersCB, true, 90)
topBorderColorMenuWidget = newIconColorMenuWidget(prefix+"DlgTopBorderColorMenuWidget",p._img+"format.gif",changeColorComplexBordersCB,null,'Top border color',16,16,8*16,0,8*16,16,p._lastUsedColorsAr)
leftBorderThickCombo = newCustomCombo(prefix+"DlgLeftBorderThickCombo", changeComplexBordersCB, true, 90)
leftBorderColorMenuWidget = newIconColorMenuWidget(prefix+"DlgLeftBorderColorMenuWidget",p._img+"format.gif",changeColorComplexBordersCB,null,'Left border color',16,16,8*16,0,8*16,16,p._lastUsedColorsAr)
rightBorderThickCombo = newCustomCombo(prefix+"DlgRightBorderThickCombo", changeComplexBordersCB, true, 90)
rightBorderColorMenuWidget = newIconColorMenuWidget(prefix+"DlgRightBorderColorMenuWidget",p._img+"format.gif",changeColorComplexBordersCB,null,'Right border color',16,16,8*16,0,8*16,16,p._lastUsedColorsAr)
bottomBorderThickCombo = newCustomCombo(prefix+"DlgBottomBorderThickCombo", changeComplexBordersCB, true, 90)
bottomBorderColorMenuWidget = newIconColorMenuWidget(prefix+"DlgBottomBorderColorMenuWidget",p._img+"format.gif",changeColorComplexBordersCB,null,'Bottom border color',16,16,8*16,0,8*16,16,p._lastUsedColorsAr)
for (var i=0; i < p._thickness.length; i++)
{
var t = p._thickness[i]
genBorderThickCombo.add(t,""+t)
topBorderThickCombo.add(t,""+t)
leftBorderThickCombo.add(t,""+t)
rightBorderThickCombo.add(t,""+t)
bottomBorderThickCombo.add(t,""+t)
}
previewBorders = newWidget(prefix+"DlgPreviewBorders")
}
function getHTMLForBorderTab(prefix)
{
var arHTML = new Array()
var i=0
arHTML[i++] = '<table border="0" class="dialogzone" width=100%"><tbody>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td colspan="2" valign="top">' + 'Settings applied to all borders:' + '</td>'
arHTML[i++] = '<td align="center"><table class="dialogzone" border="0" cellspacing="0" cellpadding="0"><tbody><tr>'
arHTML[i++] = '<td>' + genBorderThickCombo.getHTML() + '</td>'
arHTML[i++] = '<td>' + genBorderColorMenuWidget.getHTML() + '</td></tr></tbody></table></td>'
arHTML[i++] = '<td></td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '<tr><td colspan="4">' + getSep() + '</td></tr>'
arHTML[i++] = '<tr><td colspan="4" valign="top">' + 'Settings per border:' + '</td></tr>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td colspan="2">' + ' ' + '</td>'
arHTML[i++] = '<td align="center"><table class="dialogzone" border="0" cellspacing="0" cellpadding="0"><tbody><tr>'
arHTML[i++] = '<td>' + topBorderThickCombo.getHTML() + '</td>'
arHTML[i++] = '<td>' + topBorderColorMenuWidget.getHTML() + '</td></tr></tbody></table></td>'
arHTML[i++] = '<td></td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td>' + ' ' + '</td>'
arHTML[i++] = '<td align="right"><table class="dialogzone" border="0" cellspacing="0" cellpadding="0"><tbody><tr>'
arHTML[i++] = '<td>' + leftBorderThickCombo.getHTML() + '</td>'
arHTML[i++] = '<td>' + leftBorderColorMenuWidget.getHTML() + '</td></tr></tbody></table></td>'
arHTML[i++] = '<td width="180" height="60" valign="middle" align="center" bgcolor="white" style="height:60px"">'
arHTML[i++] = '<table border="0" cellspacing="0" cellpadding="8"><tbody><tr>'
arHTML[i++] = '<td><div id ='+'"'+prefix+'DlgPreviewBorders">' + getSpace(140,36) + '</div></td>'
arHTML[i++] = '</tr></tbody></table>'
arHTML[i++] = '</td>'
arHTML[i++] = '<td><table class="dialogzone" border="0" cellspacing="0" cellpadding="0"><tbody><tr>'
arHTML[i++] = '<td>' + rightBorderThickCombo.getHTML() + '</td>'
arHTML[i++] = '<td>' + rightBorderColorMenuWidget.getHTML() + '</td></tr></tbody></table></td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '<tr>' 
arHTML[i++] = '<td colspan="2">' + ' ' + '</td>' 
arHTML[i++] = '<td align="center"><table class="dialogzone" border="0" cellspacing="0" cellpadding="0"><tbody><tr>' 
arHTML[i++] = '<td>' + bottomBorderThickCombo.getHTML() + '</td>' 
arHTML[i++] = '<td>' + bottomBorderColorMenuWidget.getHTML() + '</td></tr></tbody></table></td>' 
arHTML[i++] = '<td></td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '</tbody></table>'
return arHTML.join('')
}
function setBorders(elts)
{
if (!initBorders)
{
initBorders =  true
previewBorders.init()
genBorderThickCombo.init()
topBorderThickCombo.init()
leftBorderThickCombo.init()
rightBorderThickCombo.init()
bottomBorderThickCombo.init()
genBorderColorMenuWidget.init()
topBorderColorMenuWidget.init()
leftBorderColorMenuWidget.init()
rightBorderColorMenuWidget.init()
bottomBorderColorMenuWidget.init()
res = p.get2AttsFromElts(".border.size",".borders.topSize",elts)
retTopSize = (res.defined)? res.ret:null
if (res.defined)
{
topBorderThickCombo.select(retTopSize)
} else {
topBorderThickCombo.setUndefined(true)
}
res=p.get2AttsFromElts(".border.color.rgb",".borders.topColor.rgb",elts)
retTopColor = (res.defined)? res.ret:null
topBorderColorMenuWidget.setColor(retTopColor)
res=p.get2AttsFromElts(".border.size",".borders.bottomSize",elts)
retBottomSize = (res.defined)? res.ret:null
if (res.defined)
{
bottomBorderThickCombo.select(retBottomSize)
} else {
bottomBorderThickCombo.setUndefined(true)
}
res=p.get2AttsFromElts(".border.color.rgb",".borders.bottomColor.rgb",elts)
retBottomColor = (res.defined)? res.ret:null
bottomBorderColorMenuWidget.setColor(retBottomColor)
res=p.get2AttsFromElts(".border.size",".borders.leftSize",elts)
retLeftSize = (res.defined)? res.ret:null
if (res.defined)
{
leftBorderThickCombo.select(retLeftSize)
} else {
leftBorderThickCombo.setUndefined(true)
} 
res=p.get2AttsFromElts(".border.color.rgb",".borders.leftColor.rgb",elts)
retLeftColor = (res.defined)? res.ret:null
leftBorderColorMenuWidget.setColor(retLeftColor)
res=p.get2AttsFromElts(".border.size",".borders.rightSize",elts)
retRightSize = (res.defined)? res.ret:null
if (res.defined)
{
rightBorderThickCombo.select(retRightSize)
} else {
rightBorderThickCombo.setUndefined(true)
} 
res=p.get2AttsFromElts(".border.color.rgb",".borders.rightColor.rgb",elts)
retRightColor = (res.defined)? res.ret:null
rightBorderColorMenuWidget.setColor(retRightColor)
if ((retTopSize != null) && (retTopSize == retBottomSize) && (retBottomSize == retLeftSize) && (retLeftSize == retRightSize))
{
genBorderThickCombo.select(retTopSize)
} else {
genBorderThickCombo.setUndefined(true)
}
if ((retBottomColor == retLeftColor) && (retLeftColor == retTopColor) && (retTopColor == retRightColor)) 
{
genBorderColorMenuWidget.setColor(retBottomColor)
} else {
genBorderColorMenuWidget.setColor(null)
}
_simpleBorder = (genBorderThickCombo.getSelection()!=null) && (genBorderColorMenuWidget.getColor() != null)
updatePreviewBorders()
}
}
function chgBorderColorCB()
{
if (this.getColor()==null)
{
p._currColorCombo= this
p._currColorCB= changeColorPreviewBorderCB
p.frameNav("SecondDlgFrame","colorPickerDialog.html")
} else {
p.addLastUsedColor(this.getColor())
}
applyGenThicknessCB()
}
function changeColorPreviewBorderCB(c)
{
p._currColorCombo.setColor(c)
p._currColorCombo.hasChanged=true
p.addLastUsedColor(c)
applyGenThicknessCB()
updatePreviewBorders()
}
function applyGenThicknessCB()
{
var thicknessSelection = genBorderThickCombo.getSelection()
var thicknessColor = genBorderColorMenuWidget.getColor()
_simpleBorder = (thicknessSelection!=null) && (thicknessColor != null)
if (thicknessSelection) 
{
var thicknessIndex = thicknessSelection.index
var thicknessValue = thicknessSelection.value
topBorderThickCombo.select(thicknessIndex)
bottomBorderThickCombo.select(thicknessIndex)
leftBorderThickCombo.select(thicknessIndex)
rightBorderThickCombo.select(thicknessIndex)
}
if (thicknessColor)
{
topBorderColorMenuWidget.setColor(thicknessColor)
bottomBorderColorMenuWidget.setColor(thicknessColor)
leftBorderColorMenuWidget.setColor(thicknessColor)
rightBorderColorMenuWidget.setColor(thicknessColor)
}
updatePreviewBorders()
}
function changeColorComplexBordersCB()
{
if (this.getColor()==null)
{
p._currColorCombo= this
p._currColorCB= changeColorComplexBordersCBProcess
p.frameNav("SecondDlgFrame","colorPickerDialog.html")
} else {
p.addLastUsedColor(this.getColor())
}
changeComplexBordersCB()
}
function changeColorComplexBordersCBProcess(c)
{
p._currColorCombo.setColor(c)
p._currColorCombo.hasChanged=true
p.addLastUsedColor(c)
}
function changeComplexBordersCB()
{
_simpleBorder = false
genBorderThickCombo.setUndefined(true)
genBorderColorMenuWidget.setColor(null)
updatePreviewBorders()
}
function updatePreviewBorders()
{
var css = previewBorders.css
if (topBorderThickCombo.getSelection() && topBorderColorMenuWidget.getColor() && (topBorderColorMenuWidget.getColor() != "-1,-1,-1"))
{
var cssBorderTop = 'solid '
cssBorderTop += ((topBorderThickCombo.getSelection().value != "") ? topBorderThickCombo.getSelection().index : 0) + 'px '
var s= newBOColor(topBorderColorMenuWidget.getColor()).getStyleColor()
if (s) cssBorderTop += s
css.borderTop = cssBorderTop
} else {
css.borderTop = ""
}
if (bottomBorderThickCombo.getSelection() && bottomBorderColorMenuWidget.getColor() && (bottomBorderColorMenuWidget.getColor() != "-1,-1,-1"))
{
var cssBorderBottom = 'solid '
cssBorderBottom += ((bottomBorderThickCombo.getSelection().value != "") ? bottomBorderThickCombo.getSelection().index : 0) + 'px '
s= newBOColor(bottomBorderColorMenuWidget.getColor()).getStyleColor()
if (s) cssBorderBottom += s
css.borderBottom = cssBorderBottom
} else {
css.borderBottom = ""
}
if (leftBorderThickCombo.getSelection() && leftBorderColorMenuWidget.getColor() && (leftBorderColorMenuWidget.getColor() != "-1,-1,-1"))
{
var cssBorderLeft = 'solid '
cssBorderLeft += ((leftBorderThickCombo.getSelection().value != "") ? leftBorderThickCombo.getSelection().index : 0) + 'px ' 
s= newBOColor(leftBorderColorMenuWidget.getColor()).getStyleColor()
if (s) cssBorderLeft += s
css.borderLeft = cssBorderLeft
} else {
css.borderLeft = ""
}
if (rightBorderThickCombo.getSelection() && rightBorderColorMenuWidget.getColor() && (rightBorderColorMenuWidget.getColor() != "-1,-1,-1"))
{
var cssBorderRight = 'solid ' 
cssBorderRight += ((rightBorderThickCombo.getSelection().value != "") ? rightBorderThickCombo.getSelection().index : 0) + 'px ' 
s= newBOColor(rightBorderColorMenuWidget.getColor()).getStyleColor()
if (s) cssBorderRight += s
css.borderRight = cssBorderRight
} else {
css.borderRight = ""
}
}
function doFormatForBorders(f)
{
if (genBorderThickCombo.initialized())
{
if (_simpleBorder)
{
f.borderSize.value="" + genBorderThickCombo.getSelection().index
if (genBorderColorMenuWidget.getColor()) 
{
f.borderColor.value="" + genBorderColorMenuWidget.getColor()
}
} else {
f.topBorderSize.value="" + (topBorderThickCombo.getSelection()?topBorderThickCombo.getSelection().index:"")
if (topBorderColorMenuWidget.getColor()) 
{
f.topBorderColor.value="" + topBorderColorMenuWidget.getColor()
}
f.bottomBorderSize.value="" + (bottomBorderThickCombo.getSelection()?bottomBorderThickCombo.getSelection().index:"")
if (bottomBorderColorMenuWidget.getColor())
{
f.bottomBorderColor.value="" + bottomBorderColorMenuWidget.getColor()
}
f.leftBorderSize.value="" + (leftBorderThickCombo.getSelection()?leftBorderThickCombo.getSelection().index:"")
if (leftBorderColorMenuWidget.getColor()) 
{
f.leftBorderColor.value="" + leftBorderColorMenuWidget.getColor()
}
f.rightBorderSize.value="" + (rightBorderThickCombo.getSelection()?rightBorderThickCombo.getSelection().index:"")
if (rightBorderColorMenuWidget.getColor()) 
{
f.rightBorderColor.value="" + rightBorderColorMenuWidget.getColor()
}
}
}
}

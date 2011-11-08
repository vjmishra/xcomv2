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
*/bgColor=null
noneRadio=null
imageRadio=null
skinRadio=null
imageFromFileRadio=null
addImageButton=null
urlTxt=null
skinsCombo=null
testButton=null
displayCombo=null
alignHorizontalCombo=null
alignVerticalCombo=null
curEntry=null
function createWidgetsForAppearenceTab(prefix)
{
bgColor = newIconColorMenuWidget(prefix+"DlgBgColor",p._img+"format.gif",changeColorCB,null,'Background color',16,16,6*16,0,6*16,16,p._lastUsedColorsAr)
noneRadio = newRadioWidget(prefix+"DlgNoneRadio","imgSkinGrp", 'None', skinOrImageCB)
imageRadio = newRadioWidget(prefix+"DlgImageRadio","imgSkinGrp", 'Image from URL', skinOrImageCB)
skinRadio = newRadioWidget(prefix+"DlgSkinRadio","imgSkinGrp", 'Skin:', skinOrImageCB)
imageFromFileRadio = newRadioWidget(prefix+"DlgImageFromFileRadio","imgSkinGrp", 'Image from file', skinOrImageCB)
addImageButton = newButtonWidget(prefix+"DlgAddButton","Add","addImageCB()",60)
urlTxt = newTextFieldWidget(prefix+"DlgURLTxt", null, 255, null, null, true, 'Type a valid URL here.', 200)
skinsCombo = newComboWidget(prefix+"DlgSkinsCombo", null, true, 120)
testButton = newButtonWidget(prefix+"DlgTestButton",'Test',"testBtCB()",60)
displayCombo = newComboWidget(prefix+"DlgDisplayCombo", displayComboCB, true, 163)
alignHorizontalCombo = newComboWidget(prefix+"DlgAlignHorizontalCombo", null, true, 80)
alignVerticalCombo = newComboWidget(prefix+"DlgAlignVerticalCombo", null, true, 80)
}
function getHTMLForAppearenceTab()
{
var arHTML = new Array()
var i=0
arHTML[i++] = '<table border="0" class="dialogzone" cellpadding="1" cellspacing="0" width=100%"><tbody>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td width="80">' +'Background' + '</td>'
arHTML[i++] = '<td width="80">' +'Color:' + '</td>'
arHTML[i++] = '<td colspan="4">' + bgColor.getHTML() + '</td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td width="80">' + ''  + '</td>'
arHTML[i++] = '<td width="80">' + 'Pattern:' + '</td>'
arHTML[i++] = '<td colspan="4">' + noneRadio.getHTML() + '</td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td colspan="2">' + ''  + '</td>'
arHTML[i++] = '<td colspan="4">' + getSep() + '</td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td colspan="2">' + ''  + '</td>'
arHTML[i++] = '<td colspan="4">'
arHTML[i++] ='<table border="0" class="dialogzone" cellpadding="0" cellspacing="0"><tbody>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td>' + skinRadio.getHTML() +  '</td>'
arHTML[i++] = '<td>' + getSpace(5,1) +  '</td>'
arHTML[i++] = '<td>' + skinsCombo.getHTML() + '</td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '</tbody></table>'
arHTML[i++] = '</td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td colspan="2">' + ''  + '</td>'
arHTML[i++] = '<td colspan="4">' + getSep() + '</td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td colspan="2">' + ''  + '</td>'
arHTML[i++] = '<td colspan="3">' + imageRadio.getHTML() + '</td>'
arHTML[i++] = '<td>' + urlTxt.getHTML() + '</td>'
//'<td>' + testButton.getHTML() + '</td>'+
arHTML[i++] = '</tr>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td colspan="2">' + ''  + '</td>'
arHTML[i++] = '<td colspan="3">' + imageFromFileRadio.getHTML() + '</td>'
arHTML[i++] = '<td>' + addImageButton.getHTML() + '</td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td colspan="2">' + ''  + '</td>'
arHTML[i++] = '<td colspan="2">' + 'Display:'  + '</td>'
arHTML[i++] = '<td colspan="2">' + displayCombo.getHTML() + '</td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '<tr>'
arHTML[i++] = '<td colspan="2">' + ''  + '</td>'
arHTML[i++] = '<td colspan="2">' + 'Position:'  + '</td>'
arHTML[i++] = '<td colspan="2">' + alignHorizontalCombo.getHTML() + '&nbsp;'
arHTML[i++] = alignVerticalCombo.getHTML() + '</td>'
arHTML[i++] = '</tr>'
arHTML[i++] = '</tbody></table>'
return arHTML.join('')
}
function skinOrImageCB()
{
var isSkin = skinRadio.isChecked()
var isImgUrl = imageRadio.isChecked()
var isNone = noneRadio.isChecked()
var isImgFromFile = imageFromFileRadio.isChecked()
skinsCombo.setDisabled(!isSkin)
urlTxt.setDisabled(!isImgUrl)
addImageButton.setDisabled(!isImgFromFile)
displayCombo.setDisabled(! (isImgUrl||isImgFromFile))
displayComboCB()
}
function displayComboCB()
{
var showH = false
var showV = false
switch (displayCombo.getSelection().index)
{
case p._IMG_NORMAL :
showH = true
showV = true
break;
case p._IMG_STRETCH :
showH = false
showV = false
break;
case p._IMG_TILE :
showH = false
showV = false
break;
case p._IMG_HORIZONTAL_TILE :
showH = false
showV = true
break;
case p._IMG_VERTICAL_TILE :
showH = true
showV = false
break
}
alignHorizontalCombo.setDisabled(!showH)
alignVerticalCombo.setDisabled(!showV)
}
function addImageCB()
{
var s=p._root + "language/"+p._lang+"/html/uploadImageDialog.html?curEntry="+curEntry;
var backgrdImg=getImgFileFromApplyFormatDlg()
if (backgrdImg!=null)
s+="&backgrdImg="+backgrdImg;
p.frameNav("ThirdDlgFrame", s)
}
function getBackgroundImgFile(elts)
{
var report_bid=reportBID?reportBID:0;
for (var i in elts)
{
for (var k in p._formatDlgBackgroundImg[report_bid])
{
if (elts[i].bid==k)
return p._formatDlgBackgroundImg[report_bid][k];
}
}
return null;
}
function setBackgroundImgFile(elts, imagFile)
{
var report_bid=reportBID?reportBID:0;
for (var i in elts)
{
var bid=elts[i].bid;
if (typeof(p._formatDlgBackgroundImg[report_bid])=="undefined")
{
p._formatDlgBackgroundImg[report_bid]=new Array;
}
p._formatDlgBackgroundImg[report_bid][bid]=imagFile;
}
}

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
*/function newBOPrompt(arrPrompts)
{
if (arrPrompts==null) return null;
var o=new Object;
o.index=-1;
o.name=arrPrompts[10];
o.escapedName=arrPrompts[0];
o.paramName=arrPrompts[1];
o.paramIndexName=arrPrompts[1].replace('V','I');
o.inputFormat=arrPrompts[2];
o.isDate=arrPrompts[3];
o.type=arrPrompts[4];
o.hasLOV=arrPrompts[5];
o.lovID=arrPrompts[11];
o.isConstrained=arrPrompts[6];
o.isMultiColumn=arrPrompts[7];
var v=getValuesFromStringTokenizer(arrPrompts[8]);
o.value=v[1];
o.valueIndex=v[0];
o.isRequired=arrPrompts[9];
o.hasNP=arrPrompts[12];
o.level=arrPrompts[14];
o.isGrouped=startsWithIgnoreCase(o.paramName, "PV")?arrPrompts[13]:false;
o.parentLovID=startsWithIgnoreCase(o.paramName, "NPV")?arrPrompts[13]:"";
o.isFilled=newBOPrompt_isFilled;
o.isNestedPrompt=newBOPrompt_isNestedPrompt;
o.valueChanged=false;
o.lovRefreshed=false;
o.lovSelectableItems=arrPrompts[15];
o.isOptional=arrPrompts[16];
o.isLovDelegatedSearch=arrPrompts[17];
o.isLovPartialResult=arrPrompts[18];
return o;
}
function newBOPrompt_isFilled( )
{
return (this.value!="")?true:false;
}
function newBOPrompt_isNestedPrompt()
{
return (this.parentLovID!="")?true:false;
}
function getValuesFromStringTokenizer(s)
{
var ret=new Array(2);
ret[0]="";
ret[1]="";
if (s!=null)
{
var arr=s.split(';');
for (var i=0;i<arr.length;i++)
{
if (i>0)
{
ret[0]+=";";
ret[1]+=";";
}
var idx=arr[i].indexOf('_');
if (idx>=0)
{
ret[0]+=arr[i].substring(0,idx);
ret[1]+=arr[i].substring(idx+1);
}
else
ret[1]+=arr[i];
}
}
return ret;
}
function getPromptsUI(arrTree)
{
var o=new Object;
o.strLovNoValue="(--No value--)";
o.strPreviousChunk="(--Select values from %P%--)";
o.strNextChunk="(--Select values from %P%--)";
o.strSelectChunk="(--Select a range of values--)";
o.strDelegateSearchMessage="Use search criteria to retreive values.\n\nThe search is case sensitive. Here are examples of search criteria:\nSearch = a* -> retrieves all values starting by \"a\".\nSearch = *a -> retrieves all values finishing by \"a\".\nSearch = a*a -> retrieves all values starting and finishing by \"a\".\nSearch = a*a* -> retrieves all values starting by \"a\" and containing one more \"a\".";
o.strRefreshMessage="To see the content of the list, please click the refresh/load button.";
o.strPromptWidth="274";
o.iPromptWidth=parseInt(o.strPromptWidth);
o.iListBoxSize=10;
o.iTreeLovHeight=215;
o.arrPromptTree=arrTree;
o.length=(arrTree)?arrTree.length:0;
o.selectedIndex=-1;
o.isLovFilled=new Array();for (var i=0;i<o.length;i++) {o.isLovFilled[i]=false};
o.resize=promptUI_resize;
o.getPrompt=promptUI_getPrompt;
o.getTreeIndex=promptUI_getTreeIndex;
o.getGroupedNP=promptUI_getGroupedNP;
o.getHTML=promptUI_getPromptsHTML;
o.getPLovMonoHTML=promptUI_getPLovMonoHTML;
o.getPMonoHTML=promptUI_getPMonoHTML;
o.getPLovMultiHTML=promptUI_getPLovMultiHTML;
o.getPLovMultiConstHTML=promptUI_getPLovMultiConstHTML;
o.getPMultiConstHTML=promptUI_getPMultiConstHTML;
o.getPMultiHTML=promptUI_getPMultiHTML
o.getGroupedPromptHTML=promptUI_getGroupedPromptHTML;
o.getWidgets=promptUI_getPromptsWidgets;
o.getPLovMonoWidgets=promptUI_getPLovMonoWidgets;
o.getPMonoWidgets=promptUI_getPMonoWidgets;
o.getPLovMultiWidgets=promptUI_getPLovMultiWidgets;
o.getPMultiConstWidgets=promptUI_getPMultiConstWidgets;
o.getPMultiWidgets=promptUI_getPMultiWidgets;
o.getGroupedPromptWidgets=promptUI_getGroupedPromptWidgets;
o.init=promptUI_initPromptsUI;
o.initPLovMonoUI=promptUI_initPLovMonoUI;
o.initPMonoUI=promptUI_initPMonoUI;
o.initPLovMultiUI=promptUI_initPLovMultiUI;
o.initPMultiConstUI=promptUI_initPMultiConstUI;
o.initPMultiUI=promptUI_initPMultiUI;
o.initGroupedPromptUI=promptUI_initGroupedPromptUI;
return o;
}
function promptUI_getTreeIndex(pIndex)
{
var iTreeIndex=null;
if (pIndex!=null)
{
var o=this;
for (var i=0;i<o.length;i++)
{
if (o.arrPromptTree[i][0]==pIndex)
{
iTreeIndex=i;
break;
}
}
}
return iTreeIndex;
}
function promptUI_getPrompt(iTreeIndex)
{
var pt=null;
if (iTreeIndex!=null)
{
var o=this;
if (o.length>0)
{
var i=o.arrPromptTree[iTreeIndex][0];
pt=prompts[i];
}
}
return pt;
}
function promptUI_getGroupedNP(iPrompt)
{
var arr=new Array()
if (iPrompt!=null)
{
if (prompts[iPrompt].isGrouped)
{
for (var i=iPrompt+1;i<prompts.length;i++)
{
if (prompts[i].isNestedPrompt())
arr[arr.length]=i;
else
break;
}
}
}
return arr;
}
function promptUI_resize(zoom)
{
var getIntSize=function(w)
{
w=""+w
var v='';
for (var j=0;j<w.length;j++)
{
if (isNaN(w.charAt(j)))
    break;
else
    v+=w.charAt(j);
}
if (v!='')
return parseInt(v);
else
return 0;
}
var o=this;
for (var i=0;i<prompts.length;i++)
{
if (typeof(objLov)!="undefined" && objLov[i]!=null)
{
var wLov=Math.round(objLov[i].lovList.w*zoom);
var size=Math.round(objLov[i].lovList.getLines()*zoom);
objLov[i].resize(wLov,size);
}
var dSize=null;
if (typeof(lstPromptValues)!="undefined" && lstPromptValues[i]!=null)
{
var wLPV=Math.round(lstPromptValues[i].w*zoom);
lstPromptValues[i].resize(wLPV);
dSize=lstPromptValues[i].getLines()
lstPromptValues[i].setLines(Math.round(lstPromptValues[i].getLines()*zoom));
dSize=lstPromptValues[i].getLines()-dSize;
}
if (typeof(treeLov)!="undefined" && treeLov[i]!=null)
{
var wTLov=Math.round(treeLov[i].w*zoom);
var hTLov=null;
if (dSize!=null)
    hTLov=treeLov[i].h+(dSize*15);
else
hTLov=Math.round(treeLov[i].h*zoom);
treeLov[i].resize(wTLov,hTLov);
}
if (typeof(txtEnterValue)!="undefined" && txtEnterValue[i]!=null)
{
var wETX=Math.round(getIntSize(txtEnterValue[i].width)*zoom);
txtEnterValue[i].resize(wETX);
}
if (typeof(txtPromptValue)!="undefined" && txtPromptValue[i]!=null)
{
var wTPV=Math.round(getIntSize(txtPromptValue[i].width)*zoom);
txtPromptValue[i].resize(wTPV);
}
}
}
function promptUI_getPromptsHTML()
{
var o=this;
var strPromptsHTML = '';
for (var j=0;j<o.length;j++)
{
if (j==0)
strPromptsHTML+='<div id="tp'+j+'">\n';
else
strPromptsHTML+='<div id="tp'+j+'" style="display:none">\n';
var pt=o.getPrompt(j);
var i=pt.index;
if (pt.isGrouped)
strPromptsHTML+=o.getGroupedPromptHTML(i);
else if (pt.type=="Mono")
{
if (pt.hasLOV)
strPromptsHTML+=o.getPLovMonoHTML(i);
else
strPromptsHTML+=o.getPMonoHTML(i);
}
else if (pt.type=="Multi")
{
if (pt.hasLOV && pt.isConstrained)
strPromptsHTML+=o.getPLovMultiConstHTML(i);
else if (pt.hasLOV && !pt.isConstrained)
strPromptsHTML+=o.getPLovMultiHTML(i);
else if (pt.isConstrained)
strPromptsHTML+=o.getPMultiConstHTML(i);
else
strPromptsHTML+=o.getPMultiHTML(i);
}
strPromptsHTML+='</div>';
}
return strPromptsHTML;
}
function promptUI_getPLovMonoHTML(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
var strPromptOutput=''+
'<table border="0" cellpadding="0" cellspacing="0"><tbody>'+
'<tr>'+
'<td rowspan="2" align="left">'+
objLov[iPrompt].getHTML()+
'<input type="hidden" id="L'+pt.paramName+'_searchVal" name="L'+pt.paramName+'_searchVal" value="">'+
'</td>'+
'<td height="100%">'+
'<table border="0" cellpadding="0" cellspacing="0" height="100%">'+
'<tr>'+
'<td valign="top">'+
'<table border="0" cellpadding="0" cellspacing="3">'+
'<tr>'+
'<td colspan="2"></td>'+
'</tr>'+
'<tr>'+
'<td>&nbsp;</td>'+
'<td class="dialogzonebold"><label style="overflow:hidden;text-overflow:ellipsis;width:'+o.strPromptWidth+'px" for="'+pt.paramName+'">'+pt.escapedName+'</label></td>'+
'</tr>'+
'<tr>'+
'<td align="center">'+
'<table border="0" cellpadding="0" cellspacing="0">'+
'<tr>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'<td>'+
addButton[iPrompt].getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'</tr>'+
'<tr>'+
'<td colspan="3">'+
getSpace(8,8)+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'<td>'+
delButton[iPrompt].getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'<td valign="top">';
if (pt.isConstrained)
{
strPromptOutput+=txtPromptValue[iPrompt].getHTML()+
'<input type="hidden" id="'+pt.paramName+'" name="'+pt.paramName+'" value="">';
}
else
{
strPromptOutput+=''+
'<table border="0" cellspacing="0" cellpadding="0">'+
'<tr>'+
'<td>'+
txtPromptValue[iPrompt].getHTML()+
'</td>'+
'</tr>'+
'<tr>'+
'<td align="center" class="dialogzone">'+pt.inputFormat+'</td>'+
'</tr>'+
'</table>';
}
strPromptOutput+='<input type="hidden" id="'+pt.paramIndexName+'" name="'+pt.paramIndexName+'" value="">'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</tbody></table>';
return strPromptOutput;
}
function promptUI_getPMonoHTML(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
var strPromptOutput=''+
'<table border="0" cellpadding="0" cellspacing="0" width="100%"><tbody>'+
'<tr>'+
'<td align="center">'+
'<table border="0" cellpadding="0" cellspacing="5">'+
'<tr>'+
'<td class="dialogzonebold"><label style="overflow:hidden;text-overflow:ellipsis;width:'+o.strPromptWidth+'px" for="'+pt.paramName+'Static">'+pt.escapedName+'</label></td>'+
'</tr>'+
'<tr>'+
'<td align="left">';
if (pt.isConstrained)
{
strPromptOutput+=txtPromptValue[iPrompt].getHTML()+
'<input type="hidden" id="'+pt.paramName+'" name="'+pt.paramName+'" value="">';
}
else
{
strPromptOutput+=txtPromptValue[iPrompt].getHTML();
}
strPromptOutput+='<input type="hidden" id="'+pt.paramIndexName+'" name="'+pt.paramIndexName+'" value="">'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</tbody></table>';
return strPromptOutput;
}
function promptUI_getPLovMultiHTML(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
var strPromptOutput=''+
'<table border="0" cellpadding="0" cellspacing="0"><tbody>'+
'<tr>'+
'<td align="center" class="dialogzone">'+
((pt.inputFormat!="")?pt.inputFormat:"&nbsp;")+
'</td>'+
'<td>&nbsp;</td>'+
'<td class="dialogzonebold"><label style="overflow:hidden;text-overflow:ellipsis;width:'+o.strPromptWidth+'px" for="'+pt.paramName+'List">'+pt.escapedName+'</label></td>'+
'</tr>'+
'<tr>'+
'<td align="left" valign="top">'+
objLov[iPrompt].getHTML()+
'<input type="hidden" id="L'+pt.paramName+'_searchVal" name="L'+pt.paramName+'_searchVal" value="">'+
'</td>'+
'<td valign="top">'+
'<table border="0" cellpadding="0" cellspacing="0" height="100%">'+
'<tr>'+
'<td align="center" valign="top">'+
'<table border="0" style="margin-left:3px;margin-right:3px" cellpadding="0" cellspacing="0">'+
'<tr>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'<td>'+
addButton[iPrompt].getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'</tr>'+
'<tr>'+
'<td colspan="3">'+
getSpace(8,8)+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'<td>'+
delButton[iPrompt].getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'<td valign="top">'+
lstPromptValues[iPrompt].getHTML()+
'<input type="hidden" id="'+pt.paramName+'" name="'+pt.paramName+'" value="">'+
'<input type="hidden" id="'+pt.paramIndexName+'" name="'+pt.paramIndexName+'" value="">'+
'</td>'+
'</tr>'+
'</tbody></table>';
return strPromptOutput;
}
function promptUI_getPLovMultiConstHTML(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
var strPromptOutput=''+
'<table border="0" cellpadding="0" cellspacing="0"><tbody>'+
'<tr>'+
'<td align="left">'+
objLov[iPrompt].getHTML()+
'<input type="hidden" id="L'+pt.paramName+'_searchVal" name="L'+pt.paramName+'_searchVal" value="">'+
'</td>'+
'<td valign="top">'+
'<table style="margin-top:8px" border="0" cellpadding="0" cellspacing="0">'+
'<tr>'+
'<td>&nbsp;</td>'+
'<td class="dialogzonebold"><label style="overflow:hidden;text-overflow:ellipsis;width:'+o.strPromptWidth+'px" for="'+pt.paramName+'List">'+pt.escapedName+'</label></td>'+
'</tr>'+
'<tr>'+
'<td height="100%">'+
'<table border="0" cellpadding="0" cellspacing="0" height="100%">'+
'<tr>'+
'<td align="center" valign="top">'+
'<table border="0" style="margin-left:3px;margin-right:3px" cellpadding="0" cellspacing="0">'+
'<tr>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'<td>'+
addButton[iPrompt].getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'</tr>'+
'<tr>'+
'<td colspan="3">'+
getSpace(8,8)+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'<td>'+
delButton[iPrompt].getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'<td valign="top">'+
lstPromptValues[iPrompt].getHTML()+
'<input type="hidden" id="'+pt.paramName+'" name="'+pt.paramName+'" value="">'+
'<input type="hidden" id="'+pt.paramIndexName+'" name="'+pt.paramIndexName+'" value="">'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</tbody></table>';
return strPromptOutput;
}
function promptUI_getPMultiConstHTML(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
var strPromptOutput=''+
'<table border="0" cellpadding="0" cellspacing="3"><tbody>'+
'<tr>'+
'<td>&nbsp;</td>'+
'<td class="dialogzonebold"><label style="overflow:hidden;text-overflow:ellipsis;width:'+o.strPromptWidth+'px" for="'+pt.paramName+'List">'+pt.escapedName+'</label></td>'+
'</tr>'+
'<tr>'+
'<td valign="top">'+
'<table border="0" cellpadding="0" cellspacing="0">'+
'<tr>'+
'<td>'+
delButton[iPrompt].getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'<td valign="top">'+
lstPromptValues[iPrompt].getHTML()+
'<input type="hidden" id="'+pt.paramName+'" name="'+pt.paramName+'" value="">'+
'<input type="hidden" id="'+pt.paramIndexName+'" name="'+pt.paramIndexName+'" value="">'+
'</td>'+
'<tr>'+
'</tbody></table>';
return strPromptOutput;
}
function promptUI_getPMultiHTML(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
var strPromptOutput=''+
'<table border="0" cellpadding="0" cellspacing="0"><tbody>'+
'<tr>'+
'<td align="center" class="dialogzone">'+
((pt.inputFormat!="")?pt.inputFormat:"&nbsp;")+
'</td>'+
'<td>&nbsp;</td>'+
'<td class="dialogzonebold">'+
'<label style="overflow:hidden;text-overflow:ellipsis;width:'+o.strPromptWidth+'px" for="'+pt.paramName+'List">'+pt.escapedName+'</label>'+
'</td>'+
'</tr>'+
'<tr>'+
'<td valign="top">'+
txtEnterValue[iPrompt].getHTML()+
'</td>'+
'<td valign="top" align="center">'+
'<table border="0" style="margin-top:1px" cellpadding="0" cellspacing="0">'+
'<tr>'+
'<td>'+
'<table border="0" style="margin-left:7px;margin-right:3px" cellpadding="0" cellspacing="0">'+
'<tr>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'<td>'+
addButton[iPrompt].getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'</tr>'+
'<tr>'+
'<td colspan="3">'+
getSpace(8,8)+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'<td>'+
delButton[iPrompt].getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'<td valign="top">'+
'<table border="0" style="margin-top:1px" cellpadding="0" cellspacing="0">'+
'<tr>'+
'<td>'+
lstPromptValues[iPrompt].getHTML()+
'<input type="hidden" id="'+pt.paramName+'" name="'+pt.paramName+'" value="">'+
'<input type="hidden" id="'+pt.paramIndexName+'" name="'+pt.paramIndexName+'" value="">'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</tbody></table>';
return strPromptOutput;
}
function promptUI_getGroupedPromptHTML(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
var arrNP=o.getGroupedNP(iPrompt);
var nb=arrNP.length;
var strPromptOutput=''+
'<table id="toto2" border="0" cellpadding="0" cellspacing="3"><tbody>';
strPromptOutput+=''+
'<tr>'+
'<td valign="top" align="left">'+
'<table border="0" cellpadding="0" cellspacing="0">';
if(!pt.isConstrained && pt.type=="Multi")
{
strPromptOutput+=''+
'<tr><td align="center" class="dialogzone">'+
pt.inputFormat+
'</td></tr>'+
'<tr><td  valign="top">'+
txtEnterValue[iPrompt].getHTML()+
'</td></tr>';
}
strPromptOutput+=''+
'<tr><td align="right">'+
'<table class="dialogzone" border="0" cellspacing="0" cellpadding="0"><tbody><tr><td>' +
treeRefreshBtn[iPrompt].getHTML()+
'</td></tr></tbody></table>' +
'</td></tr>'+
'<tr><td>'+
treeLov[iPrompt].getHTML();
for (var i=nb-1;i>=0;i--)
{
var np=prompts[arrNP[i]];
strPromptOutput+=''+
'<input type="hidden" id="'+np.paramName+'" name="'+np.paramName+'" value="">'+
'<input type="hidden" id="'+np.paramIndexName+'" name="'+np.paramIndexName+'" value="">';
}
strPromptOutput+=''+
'</td></tr>'+
'</table>';
strPromptOutput+=''+
'</td>'+
'<td valign="top" align="center">'+
'<table>'+
'<tr>'+
'<td>&nbsp;</td>'+
'<td class="dialogzonebold"><span style="overflow:hidden;text-overflow:ellipsis;width:'+o.strPromptWidth+'px">'+pt.escapedName+'</span></td>'+
'</tr>'+
'<tr>'+
'<td valign="top" align="center">'+
'<table border="0" cellpadding="0" cellspacing="0">'+
'<tr>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'<td>'+
addButton[iPrompt].getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'</tr>'+
'<tr>'+
'<td colspan="3">'+
getSpace(8,8)+
'</td>'+
'</tr>'+
'<tr>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'<td>'+
delButton[iPrompt].getHTML()+
'</td>'+
'<td>'+
getSpace(5,1)+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'<td valign="top">'+
'<table border="0" cellpadding="0" cellspacing="0">'+
'<tr>'+
'<td>';
if (pt.type=="Mono")
{
if (pt.isConstrained)
{
strPromptOutput+=txtPromptValue[iPrompt].getHTML()+
'<input type="hidden" id="'+pt.paramName+'" name="'+pt.paramName+'" value="">';
}
else
{
strPromptOutput+=''+
'<table border="0" cellspacing="0" cellpadding="0">'+
'<tr>'+
'<td>'+
txtPromptValue[iPrompt].getHTML()+
'</td>'+
'</tr>'+
'<tr>'+
'<td align="center" class="dialogzone">'+pt.inputFormat+'</td>'+
'</tr>'+
'</table>';
}
}
else if (pt.type=="Multi")
{
strPromptOutput+=lstPromptValues[iPrompt].getHTML()+
'<input type="hidden" id="'+pt.paramName+'" name="'+pt.paramName+'" value="">';
}
strPromptOutput+='<input type="hidden" id="'+pt.paramIndexName+'" name="'+pt.paramIndexName+'" value="">'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</table>'+
'</td>'+
'</tr>'+
'</tbody></table>';
return strPromptOutput;
}
function promptUI_getPromptsWidgets()
{
var o=this;
for (var j=0;j<o.length;j++)
{
var pt=o.getPrompt(j);
var i=pt.index;
if (pt.isGrouped)
o.getGroupedPromptWidgets(i);
else if (pt.type=="Mono")
{
if (pt.hasLOV)
o.getPLovMonoWidgets(i);
else
o.getPMonoWidgets(i);
}
else if (pt.type=="Multi")
{
if (pt.hasLOV)
o.getPLovMultiWidgets(i);
else if (pt.isConstrained)
o.getPMultiConstWidgets(i);
else
o.getPMultiWidgets(i);
}
}
}
function promptUI_getPLovMonoWidgets(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
if (typeof(objLov)=="undefined") objLov=new Array();
objLov[iPrompt]=newLovWidget("L"+pt.paramName,"",o.iPromptWidth,o.iListBoxSize,false,true,changeBatchLOV,refreshLOV,searchValue,dlbClickCB,false);
objLov[iPrompt].lovList.changeCB=listChangeCB;
objLov[iPrompt].lovList.sortCB=sortColumnValuesCB;
if (typeof(addButton)=="undefined") addButton=new Array();
addButton[iPrompt]=newButtonWidget(pt.paramName+"AddButton",null,"addMono(\'"+pt.paramName+"\'"+(pt.isConstrained?",true":"")+")",0,null,"Add from List of Values",null,null,parent._skin+'buttonIcons.gif',16,16,0,0,true);
if (typeof(delButton)=="undefined") delButton=new Array();
delButton[iPrompt]=newButtonWidget(pt.paramName+"DelButton",null,"removeMono(\'"+pt.paramName+"\'"+(pt.isConstrained?",true":"")+")",0,null,"Remove selected value(s)",null,null,parent._skin+'buttonIcons.gif',16,16,0,16,false);
if (typeof(txtPromptValue)=="undefined") txtPromptValue=new Array();
if (pt.isConstrained)
txtPromptValue[iPrompt]=newTextFieldWidget(pt.paramName+"Static",null,null,null,null,true,"",o.iPromptWidth);
else if (pt.isDate)
{
objLov[iPrompt].setFormatDate(pt.inputFormat);
txtPromptValue[iPrompt]=newCalendarTextFieldButton(pt.paramName+"Calendar",pt.paramName,onTxtValueChanged,null,onTxtChangeCB,submitCB,true,null,o.iPromptWidth,null,null,(pt.inputFormat!="")?(pt.inputFormat):null);
}
else
txtPromptValue[iPrompt]=newTextFieldWidget(pt.paramName,onTxtValueChanged,null,onTxtChangeCB,submitCB,true,"",o.iPromptWidth);
}
function promptUI_getPMonoWidgets(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
if (typeof(txtPromptValue)=="undefined") txtPromptValue=new Array();
if (pt.isConstrained)
txtPromptValue[iPrompt]=newTextFieldWidget(pt.paramName+"Static",null,null,null,null,true,"",o.iPromptWidth);
else if (pt.isDate)
txtPromptValue[iPrompt]=newCalendarTextFieldButton(pt.paramName+"Calendar",pt.paramName,onTxtValueChanged,null,onTxtChangeCB,submitCB,true,null,o.iPromptWidth,null,null,(pt.inputFormat!="")?(pt.inputFormat):null);
else
txtPromptValue[iPrompt]=newTextFieldWidget(pt.paramName,onTxtValueChanged,null,onTxtChangeCB,submitCB,true,"",o.iPromptWidth);
}
function promptUI_getPLovMultiWidgets(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
var lstSelectedItemsSize=o.iListBoxSize+2;
var lstLovWidgetSize=(pt.isConstrained)?o.iListBoxSize:o.iListBoxSize-3;
var blnDate=pt.isDate;
if (typeof(objLov)=="undefined") objLov=new Array();
if (pt.isConstrained)
objLov[iPrompt]=newLovWidget("L"+pt.paramName,"",o.iPromptWidth,lstLovWidgetSize,false,true,changeBatchLOV,refreshLOV,searchValue,dlbClickCB,true);
else
{
objLov[iPrompt]=newLovWidget("L"+pt.paramName,"",o.iPromptWidth,(blnDate?lstLovWidgetSize-2:lstLovWidgetSize),!blnDate,true,changeBatchLOV,refreshLOV,searchValue,dlbClickCB,true,onLovEnterCB,blnDate);
if(blnDate)
objLov[iPrompt].setFormatDate(pt.inputFormat);
}
objLov[iPrompt].lovList.changeCB=listChangeCB;
objLov[iPrompt].lovList.sortCB=sortColumnValuesCB;
if (typeof(addButton)=="undefined") addButton=new Array();
if (pt.isConstrained)
addButton[iPrompt]=newButtonWidget(pt.paramName+"AddButton",null,"addMulti(\'"+pt.paramName+"\')",0,null,"Add from List of Values",null,null,parent._skin+'buttonIcons.gif',16,16,0,0,true);
else
addButton[iPrompt]=newButtonWidget(pt.paramName+"AddButton",null,"addMultiMix(\'"+pt.paramName+"\')",0,null,"Add from List of Values or from text box",null,null,parent._skin+'buttonIcons.gif',16,16,0,0,true);
if (typeof(delButton)=="undefined") delButton=new Array();
delButton[iPrompt]=newButtonWidget(pt.paramName+"DelButton",null,"removeMulti(\'"+pt.paramName+"\')",0,null,"Remove selected value(s)",null,null,parent._skin+'buttonIcons.gif',16,16,0,16,false);
if (typeof(lstPromptValues)=="undefined") lstPromptValues=new Array();
lstPromptValues[iPrompt]=newMultiColumnList(pt.paramName+"List",null,true,o.iPromptWidth,0,dblClickRemove,null,true);
lstPromptValues[iPrompt].setLines(lstSelectedItemsSize);
lstPromptValues[iPrompt].changeCB=listChangeCB;
}
function promptUI_getPMultiConstWidgets(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
if (typeof(delButton)=="undefined") delButton=new Array();
delButton[iPrompt]=newButtonWidget(pt.paramName+"DelButton",null,"removeMulti(\'"+pt.paramName+"\')",0,null,"Remove selected value(s)",null,null,parent._skin+'buttonIcons.gif',16,16,0,16,false);
if (typeof(lstPromptValues)=="undefined") lstPromptValues=new Array();
lstPromptValues[iPrompt]=newMultiColumnList(pt.paramName+"List",null,true,o.iPromptWidth,0,dblClickRemove,null,true);
lstPromptValues[iPrompt].setLines(o.iListBoxSize+2);
lstPromptValues[iPrompt].changeCB=listChangeCB;
}
function promptUI_getPMultiWidgets(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
var txtFieldWidth=o.iPromptWidth-3;
if (typeof(txtEnterValue)=="undefined") txtEnterValue=new Array();
if (pt.isDate)
txtEnterValue[iPrompt]=newCalendarTextFieldButton(pt.paramName+"Calendar",pt.paramName+"EnterValue",null,null,null,onDateEnterCB,true,null,txtFieldWidth,null,null,(pt.inputFormat!="")?(pt.inputFormat):null);
else
txtEnterValue[iPrompt]=newTextFieldWidget(pt.paramName+"EnterValue", null, null, null,onTxtEnterCB,true,"Type a value",txtFieldWidth);
txtEnterValue[iPrompt].setHelpTxt(_lovTextFieldLab);
if (typeof(addButton)=="undefined") addButton=new Array();
addButton[iPrompt]=addButton[iPrompt]=newButtonWidget(pt.paramName+"AddButton",null,"addTxtToList(\'"+pt.paramName+"EnterValue\')",0,null,"Add from text box",null,null,parent._skin+'buttonIcons.gif',16,16,0,0,true);
if (typeof(delButton)=="undefined") delButton=new Array();
delButton[iPrompt]=newButtonWidget(pt.paramName+"DelButton",null,"removeMulti(\'"+pt.paramName+"\')",0,null,"Remove selected value(s)",null,null,parent._skin+'buttonIcons.gif',16,16,0,16,false);
if (typeof(lstPromptValues)=="undefined") lstPromptValues=new Array();
lstPromptValues[iPrompt]=newMultiColumnList(pt.paramName+"List",null,true,o.iPromptWidth,0,dblClickRemove,null,true);
lstPromptValues[iPrompt].setLines(o.iListBoxSize+2);
lstPromptValues[iPrompt].changeCB=listChangeCB;
}
function promptUI_getGroupedPromptWidgets(iPrompt)
{
var urlImg = parent._root + parent._img
var o=this;
var pt=prompts[iPrompt];
var hTreeLov=o.iTreeLovHeight;
var wTreeLov=o.iPromptWidth-10;
if(!pt.isConstrained && pt.type=="Multi")
{
if (typeof(txtEnterValue)=="undefined") txtEnterValue=new Array();
if (pt.isDate)
{
txtEnterValue[iPrompt]=newCalendarTextFieldButton(pt.paramName+"Calendar",pt.paramName+"EnterValue",null,null,null,onDateEnterCB,true,null,wTreeLov,null,null,(pt.inputFormat!="")?(pt.inputFormat):null);
hTreeLov-=50;
}
else
{
txtEnterValue[iPrompt]=newTextFieldWidget(pt.paramName+"EnterValue", null, null, null,onTxtEnterCB,true,"Type a value",wTreeLov);
hTreeLov-=25;
}
}
if (typeof(treeRefreshBtn)=="undefined") treeRefreshBtn=new Array();
treeRefreshBtn[iPrompt]=newIconWidget("treeRefreshL"+pt.paramName,parent._img+'standard.gif',refreshLOV,"Refresh Values","Refresh Values",16,16,16*7,0,16*7,16);
treeRefreshBtn[iPrompt].txtPosition="left";
if (typeof(treeLov)=="undefined") treeLov=new Array();
treeLov[iPrompt] = newTreeWidget("treeL"+pt.paramName,wTreeLov,hTreeLov,urlImg,treeLov_clickCB,treeLov_dclickCB,null,null,null);
if (typeof(addButton)=="undefined") addButton=new Array();
if (typeof(delButton)=="undefined") delButton=new Array();
if (pt.type=="Mono")
{
addButton[iPrompt]=newButtonWidget(pt.paramName+"AddButton",null,"addMonoFromTreeLov(\'"+pt.paramName+"\'"+(pt.isConstrained?",true":"")+")",0,null,"Add from List of Values",null,null,parent._skin+'buttonIcons.gif',16,16,0,0,true);
delButton[iPrompt]=newButtonWidget(pt.paramName+"DelButton",null,"removeMono(\'"+pt.paramName+"\'"+(pt.isConstrained?",true":"")+")",0,null,"Remove selected value(s)",null,null,parent._skin+'buttonIcons.gif',16,16,0,16,false);
if (typeof(txtPromptValue)=="undefined") txtPromptValue=new Array();
if (pt.isConstrained)
txtPromptValue[iPrompt]=newTextFieldWidget(pt.paramName+"Static",null,null,null,null,true,"",o.iPromptWidth);
else if (pt.isDate)
txtPromptValue[iPrompt]=newCalendarTextFieldButton(pt.paramName+"Calendar",pt.paramName,onTxtValueChanged,null,onTxtChangeCB,null,true,null,o.iPromptWidth,null,null,(pt.inputFormat!="")?(pt.inputFormat):null);
else
txtPromptValue[iPrompt]=newTextFieldWidget(pt.paramName,onTxtValueChanged,null,onTxtChangeCB,null,true,"",o.iPromptWidth);
}
else if (pt.type=="Multi")
{
addButton[iPrompt]=newButtonWidget(pt.paramName+"AddButton",null,"addMultiFromGroupedLov(\'"+pt.paramName+"\')",0,null,"Add from List of Values",null,null,parent._skin+'buttonIcons.gif',16,16,0,0,true);
delButton[iPrompt]=newButtonWidget(pt.paramName+"DelButton",null,"removeMulti(\'"+pt.paramName+"\')",0,null,"Remove selected value(s)",null,null,parent._skin+'buttonIcons.gif',16,16,0,16,false);
if (typeof(lstPromptValues)=="undefined") lstPromptValues=new Array();
lstPromptValues[iPrompt]=newListWidget(pt.paramName+"List",null,true,o.iPromptWidth,o.iListBoxSize+(_moz?2:3),"",dblClickRemove);
lstPromptValues[iPrompt]=newMultiColumnList(pt.paramName+"List",null,true,o.iPromptWidth,0,dblClickRemove,null,true);
lstPromptValues[iPrompt].setLines(o.iListBoxSize+3);
lstPromptValues[iPrompt].changeCB=listChangeCB;
}
}
function promptUI_initPromptsUI()
{
var o=this;
for (var j=0;j<o.length;j++)
{
var pt=o.getPrompt(j);
var i=pt.index;
if (pt.isGrouped)
o.initGroupedPromptUI(i);
else if (pt.type=="Mono")
{
if (pt.hasLOV)
o.initPLovMonoUI(i);
else
o.initPMonoUI(i);
}
else if (pt.type=="Multi")
{
if (pt.hasLOV)
o.initPLovMultiUI(i);
else if (pt.isConstrained)
o.initPMultiConstUI(i);
else
o.initPMultiUI(i);
}
}
}
function promptUI_initPLovMonoUI(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
objLov[iPrompt].init();
objLov[iPrompt].showRefreshButton(strUserRefreshLov=="full" && !pt.isLovDelegatedSearch);
if (pt.isLovDelegatedSearch)
{
objLov[iPrompt].setLovMessage(convStr(o.strDelegateSearchMessage,false,true));
objLov[iPrompt].showDelegateSearch(true);
}
else
{
objLov[iPrompt].setLovMessage(convStr(o.strRefreshMessage,false,true));
objLov[iPrompt].showLovMessage(true);
}
addButton[iPrompt].init();
delButton[iPrompt].init();
txtPromptValue[iPrompt].init();
txtPromptValue[iPrompt].setValue(prompts[iPrompt].value);
if (pt.isConstrained)
{
txtPromptValue[iPrompt].setDisabled(true);
_curDoc.getElementById(pt.paramName).value=pt.value;
}
_curDoc.getElementById(pt.paramIndexName).value=pt.valueIndex;
}
function promptUI_initPMonoUI(iPrompt)
{
var pt=prompts[iPrompt];
txtPromptValue[iPrompt].init();
txtPromptValue[iPrompt].setValue(prompts[iPrompt].value);
if (pt.isConstrained)
{
txtPromptValue[iPrompt].setDisabled(true);
_curDoc.getElementById(pt.paramName).value=pt.value;
}
_curDoc.getElementById(pt.paramIndexName).value=pt.valueIndex;
}
function promptUI_initPLovMultiUI(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
objLov[iPrompt].init();
objLov[iPrompt].showRefreshButton(strUserRefreshLov=="full" && !pt.isLovDelegatedSearch);
if (pt.isLovDelegatedSearch)
{
objLov[iPrompt].setLovMessage(convStr(o.strDelegateSearchMessage,false,true));
objLov[iPrompt].showDelegateSearch(true);
}
else
{
objLov[iPrompt].setLovMessage(convStr(o.strRefreshMessage,false,true));
objLov[iPrompt].showLovMessage(true);
}
addButton[iPrompt].init();
delButton[iPrompt].init();
lstPromptValues[iPrompt].init();
lstPromptValues[iPrompt].freeze();
lstPromptValues[iPrompt].del();
if (pt.value!="")
{
var arrSelectedValues=pt.value.split(';');
var arrSelectedIndexes=pt.valueIndex.split(';');
for (var i=0;i<arrSelectedValues.length;i++)
{
var t=arrSelectedValues[i];
var v=arrSelectedIndexes[i]+'_'+t;
lstPromptValues[iPrompt].add(t,v,false);
}
}
lstPromptValues[iPrompt].rebuildHTML();
_curDoc.getElementById(pt.paramName).value=pt.value;
_curDoc.getElementById(pt.paramIndexName).value=pt.valueIndex;
}
function promptUI_initPMultiConstUI(iPrompt)
{
var pt=prompts[iPrompt];
delButton[iPrompt].init();
lstPromptValues[iPrompt].init();
lstPromptValues[iPrompt].freeze();
lstPromptValues[iPrompt].del();
if (pt.value!="")
{
var arrSelectedValues=pt.value.split(';');
var arrSelectedIndexes=pt.valueIndex.split(';');
for (var i=0;i<arrSelectedValues.length;i++)
{
var t=arrSelectedValues[i];
var v=arrSelectedIndexes[i]+'_'+t;
lstPromptValues[iPrompt].add(t,v,false);
}
}
lstPromptValues[iPrompt].rebuildHTML();
_curDoc.getElementById(pt.paramName).value=pt.value;
_curDoc.getElementById(pt.paramIndexName).value=pt.valueIndex;
}
function promptUI_initPMultiUI(iPrompt)
{
var pt=prompts[iPrompt];
txtEnterValue[iPrompt].init();
addButton[iPrompt].init();
delButton[iPrompt].init();
lstPromptValues[iPrompt].init();
lstPromptValues[iPrompt].freeze();
lstPromptValues[iPrompt].del();
if (pt.value!="")
{
var arrSelectedValues=pt.value.split(';');
var arrSelectedIndexes=pt.valueIndex.split(';');
for (var i=0;i<arrSelectedValues.length;i++)
{
var t=arrSelectedValues[i];
var v=arrSelectedIndexes[i]+'_'+t;
lstPromptValues[iPrompt].add(t,v,false);
}
}
lstPromptValues[iPrompt].rebuildHTML();
_curDoc.getElementById(pt.paramName).value=pt.value;
_curDoc.getElementById(pt.paramIndexName).value=pt.valueIndex;
}
function promptUI_initGroupedPromptUI(iPrompt)
{
var o=this;
var pt=prompts[iPrompt];
if(!pt.isConstrained && pt.type=="Multi")
{
txtEnterValue[iPrompt].init();
}
treeRefreshBtn[iPrompt].init();
treeRefreshBtn[iPrompt].setDisplay(strUserRefreshLov=="full");
treeLov[iPrompt].init();
if (pt.type=="Multi")
treeLov[iPrompt].setMultiSelection(true);
addButton[iPrompt].init();
delButton[iPrompt].init();
if (pt.type=="Mono")
{
txtPromptValue[iPrompt].init();
txtPromptValue[iPrompt].setValue(prompts[iPrompt].value);
if (pt.isConstrained)
{
txtPromptValue[iPrompt].setDisabled(true);
_curDoc.getElementById(pt.paramName).value=pt.value;
}
}
else if (pt.type=="Multi")
{
lstPromptValues[iPrompt].init();
lstPromptValues[iPrompt].freeze();
lstPromptValues[iPrompt].del();
if (pt.value!="")
{
var arrSelectedValues=pt.value.split(';');
var arrSelectedIndexes=pt.valueIndex.split(';');
for (var i=0;i<arrSelectedValues.length;i++)
{
var t=arrSelectedValues[i];
var v=arrSelectedIndexes[i]+'_'+t;
lstPromptValues[iPrompt].add(t,v,false);
}
}
lstPromptValues[iPrompt].rebuildHTML();
_curDoc.getElementById(pt.paramName).value=pt.value;
}
_curDoc.getElementById(pt.paramIndexName).value=pt.valueIndex;
}
function changeBatchLOV()
{
var o=this;
var iChunk=o.chunkCombo.getSelection().index;
submitLovAction(o.id.substring(1),"B",iChunk);
}
function listChangeCB()
{
displayItemsInInfozone(this);
}
function displayItemsInInfozone(list)
{
infozone.setTitle("Currently-selected values in listbox");
var sel = list.getMultiSelection()
var arrTxt=new Array
if (sel)
{
{
var selLen = sel.length
for (var i=0;i<selLen;i++)
{
if (list.getColumsCount&&(list.getColumsCount()>1))
{
var arrSelTxt=sel[i].text,arrSelTxtLen=arrSelTxt.length
for (var j=0;j<arrSelTxtLen;j++)
{
arrTxt[arrTxt.length]=arrSelTxt[j]
if (i<arrSelTxtLen-1)
arrTxt[arrTxt.length]=" "
}
}
else
arrTxt[arrTxt.length]=sel[i].text
if (i<selLen-1)
arrTxt[arrTxt.length]="\n"
}
}
}
if (infozone)
{
infozone.setText(arrTxt.join(""));
infozone.isTitleChanged=true;
}
return (arrTxt.length>0);
}
function displaySelectedItems(iPrompt)
{
infozone.setText('');
var hasSelItems=false;
if (typeof(objLov)!="undefined" && objLov[iPrompt]!=null)
hasSelItems=displayItemsInInfozone(objLov[iPrompt].lovList);
if (!hasSelItems && typeof(lstPromptValues)!="undefined" && lstPromptValues[iPrompt]!=null)
hasSelItems=displayItemsInInfozone(lstPromptValues[iPrompt]);
}
function searchValue()
{
var iTreeIndex=promptsUI.selectedIndex;
var pt=promptsUI.getPrompt(iTreeIndex);
var lv=objLov[iTreeIndex];
if (pt.isLovDelegatedSearch && lv.getSearchValue()=="") {
lv.showLovMessage(true);
lv.chunkCombo.del();
lv.updateWidget();
lv.showPartialResult(false);
return;
}
submitLovAction(pt.paramName,"S",0);
}
function refreshLOV()
{
var iTreeIndex=promptsUI.selectedIndex;
var pt=promptsUI.getPrompt(iTreeIndex);
if( !promptsUI.isLovFilled[iTreeIndex])
{
getLovValues(pt.index);
}
else if(pt.isGrouped) 
{
_this=treeLov[pt.index];
_this.cleanTree=true;
var arrNP=promptsUI.getGroupedNP(pt.index);
var i=arrNP[arrNP.length-1];
if(i>=0 && i<prompts.length)
submitLovAction(prompts[i].paramName,"R",0);
}
else 
{
if (pt.hasNP)
setNestedPrompts("R");
else
submitLovAction(pt.paramName,"R",0);
}
}
function sortColumnValuesCB(colIndex)
{
var iTreeIndex=promptsUI.selectedIndex;
var pt=promptsUI.getPrompt(iTreeIndex);
this.sortedColumn=colIndex;
var sortType=this.cols[colIndex].sortIcn;
sortType++;
if (sortType>1) sortType=-1;
this.sortType=sortType;
submitLovAction(pt.paramName,"SRT",0);
}
function setNestedPrompts(strAction)
{
strAction=(typeof(strAction)=="undefined")?"":strAction;
var iTreeIndex=promptsUI.selectedIndex;
var pt=promptsUI.getPrompt(iTreeIndex);
var blnRefresh=(strAction=="R")?true:false;
var strChildren="";
var pLovID=pt.lovID;
var pLevel=pt.level+1;
for (var i=pt.index+1;i<prompts.length;i++)
{
if (prompts[i].level>0)
{
if (prompts[i].level==pLevel && prompts[i].isNestedPrompt() && prompts[i].parentLovID==pLovID)
{
if (strAction=="")
blnRefresh=(prompts[i].valueChanged && prompts[i].value!="")?true:blnRefresh;
blnRefresh=(prompts[i].value=="")?false:blnRefresh;
strChildren=(strChildren=="")?strChildren+i.toString():strChildren+","+i.toString();
}
}
else
break;
}
if (blnRefresh)
{
var arrChildren=strChildren.split(',');
for (var i=0;i<arrChildren.length;i++)
{
var j=parseInt(arrChildren[i]);
prompts[j].valueChanged=false;
}
submitLovAction(pt.paramName,"R",0,"yes");
}
}
function getLovValues(iPrompt,setNP)
{
var pt=prompts[iPrompt];
var lv=(typeof(objLov)!="undefined" && objLov[iPrompt]!=null)?objLov[iPrompt]:null;
if (lv!=null)
lv.showLovMessage(false);
var isAutoSetNP=(setNP==null)?true:false;
var setNP=(isAutoSetNP)?"no":setNP;
if(pt.isGrouped)
{
_this=treeLov[pt.index];
_this.cleanTree=true;
var arrNP=promptsUI.getGroupedNP(iPrompt);
iPrompt=arrNP[arrNP.length-1];
}
else if (pt.hasNP)
{
var pLevel=pt.level+1;
if (isAutoSetNP)
{
var isValueChanged=false;
var isAnswered=true;
for (var i=iPrompt+1;i<prompts.length;i++)
{
if (prompts[i].level==pLevel && prompts[i].isNestedPrompt() && prompts[i].parentLovID==pt.lovID)
{
isAnswered=(prompts[i].value=="")?false:isAnswered;
isValueChanged=(prompts[i].valueChanged)?true:isValueChanged;
}
}
setNP=(isValueChanged && isAnswered)?"yes":setNP;
}
for (var i=iPrompt+1;i<prompts.length;i++)
{
if (prompts[i].level==pLevel && prompts[i].isNestedPrompt() && prompts[i].parentLovID==pt.lovID)
prompts[i].valueChanged=false;
}
}
submitLovAction(prompts[iPrompt].paramName,"F",0,setNP);
}
function submitLovAction(strPromptName,strAction,iChunk,strSetNP)
{
var url="processRefreshLOV"+parent._appExt+"?";
var iPrompt=getPromptIndex(strPromptName);
if (typeof(strSetNP)!="undefined" && strSetNP!=null)
strSetNP="&setNP="+strSetNP;
else
strSetNP="";
var strQueryParams="sEntry="+strEntry+"&iViewerID="+iViewerID;
strQueryParams+="&sAction="+strAction+"&sParamName="+strPromptName+"&gIndex="+iPrompt.toString()+"&iChunk="+iChunk+"&advPrompts=yes";
var promptIndex="";
var strNested="";
if (prompts[iPrompt].isNestedPrompt())
{
strNested=strPromptName.substring("NPV".length);
var iPos=strNested.lastIndexOf('.');
if (iPos>=0)
{
promptIndex=strNested.substring(iPos+1);
strNested=strNested.substring(0,iPos);
}
strQueryParams+="&iPrompt="+promptIndex+"&sNested="+strNested;
}
else
{
promptIndex=strPromptName.substring("PV".length);
strQueryParams+="&iPrompt="+promptIndex;
}
_curDoc.frmPrompt.elements["sLovID"].value=prompts[iPrompt].lovID;
var lov=(typeof(objLov)!="undefined" && objLov[iPrompt]!=null)?objLov[iPrompt]:null;
if (lov!=null)
{
if (strAction=="S")
{
objLov[iPrompt].keepCaseSensitive=lov.isCaseSensitive()?"yes":"no";
objLov[iPrompt].keepSearchValue=lov.getSearchValue();
}
_curDoc.frmPrompt.elements["L"+strPromptName+"_searchVal"].value=objLov[iPrompt].keepSearchValue?objLov[iPrompt].keepSearchValue:"";
strQueryParams+="&sCaseSensitive="+(objLov[iPrompt].keepCaseSensitive?objLov[iPrompt].keepCaseSensitive:(lov.isCaseSensitive()?"yes":"no"));
if (strAction=="R")
{
lov.setSearchValue("");
objLov[iPrompt].keepSearchValue="";
}
var colIndex=(typeof(lov.lovList.sortedColumn)!="undefined")?lov.lovList.sortedColumn:0;
var sortType=(typeof(lov.lovList.sortType)!="undefined")?lov.lovList.sortType:lov.lovList.cols[colIndex].sortIcn;
strQueryParams+="&colIndex="+colIndex+"&sortType="+sortType;
}
prompts[iPrompt].lovRefreshed=(strAction=="R")?true:prompts[iPrompt].lovRefreshed;
strQueryParams+=strSetNP;
if (typeof(parent.strDocType)!="undefined" && parent.strDocType!=null)
strQueryParams+="&doctype="+parent.strDocType;
url+=strQueryParams;
if (strSRC=="BCA" && typeof(parent._viewerContext)!="undefined")
url=parent._viewerContext+'/'+url;
_curDoc.frmPrompt.action=url;
_curDoc.frmPrompt.target = "loadLOV";
_curDoc.frmPrompt.submit();
displayWaitCursor.show(true);
}
function dlbClickCB()
{
var iPrompt=null;
var strPromptName=this.id;
if (typeof(strPromptName)!="undefined")
{
var iPos=strPromptName.indexOf('_');
if (iPos>=0) strPromptName=strPromptName.substring(0,iPos);
strPromptName=strPromptName.substring(1);
iPrompt=getPromptIndex(strPromptName);
if (prompts[iPrompt].type=="Mono")
addMono(strPromptName, prompts[iPrompt].isConstrained);
else
addMulti(strPromptName);
}
}
function onLovEnterCB()
{
addLovTxtToList(this);
}
function addLovTxtToList(obj)
{
var strValue="";
if (obj.showDate)
strValue=obj.getDateValue();
else
strValue=obj.getTextValue();
strValue=processFreeValues(strValue);
if (strValue!="")
{
var strPromptName=obj.id.substring(1);
addMultiToList(strPromptName,strValue.split(';'));
}
if (obj.showDate)
obj.setDateValue('');
else
obj.setTextValue('');
}
function addMultiMix(strPromptName)
{
var iPrompt=getPromptIndex(strPromptName);
addLovTxtToList(objLov[iPrompt]);
addMulti(strPromptName);
}
function onDateEnterCB()
{
if (this.getValue()!="")
addTxtToList(this.text.id);
}
function onTxtEnterCB()
{
if (this.getValue()!="")
addTxtToList(this.id);
}
function onValueChange(strPromptName)
{
var iPrompt=getPromptIndex(strPromptName);
var strValue=_curDoc.getElementById(strPromptName).value;
var strValueToDisplay=stringOverflowEllipsis(strValue);
if (!prompts[iPrompt].isNestedPrompt())
{
if (prompts[iPrompt].isRequired)
{
var blnDisableOKButton=false;
if (strValueToDisplay=="")
{
strValueToDisplay=null;
blnDisableOKButton=true;
}
else
{
for (var i=0;i<prompts.length;i++)
{
if (!prompts[i].isNestedPrompt() && i!=iPrompt && prompts[i].value=="" && prompts[i].isRequired)
{
blnDisableOKButton=true;
break;
}
}
}
OKButton.setDisabled(blnDisableOKButton);
}
else
{
if (strValueToDisplay=="")
strValueToDisplay=null;
}
if (updatePValueDiplay)
updatePValueDiplay(iPrompt, strValueToDisplay);
}
else
{
if (updatePValueDiplay)
updatePValueDiplay(iPrompt, strValueToDisplay);
}
prompts[iPrompt].valueChanged=(prompts[iPrompt].value==strValue)?false:true;
prompts[iPrompt].value=strValue;
}
function onTxtChangeCB()
{
var strPromptName=(typeof(this.text)!="undefined")?this.text.id:this.id;
onValueChange(strPromptName);
}
function onTxtValueChanged()
{
var strPromptName=(typeof(this.text)!="undefined")?this.text.id:this.id;
var iPrompt=getPromptIndex(strPromptName);
_curDoc.getElementById(prompts[iPrompt].paramIndexName).value="";
onValueChange(strPromptName);
prompts[iPrompt].valueChanged=true;
}
function addMono(strPromptName, blnStaticBox)
{
var iPrompt=getPromptIndex(strPromptName);
var sel=objLov[iPrompt].lovList.getSelection();
if (sel!=null)
{
if (sel.value!="")
{
var val=sel.value;
var idx=val.indexOf('_');
var strIndex=val.substring(0,idx);
var strValue=val.substring(idx+1);
if (typeof(blnStaticBox)=="undefined") blnStaticBox=false;
if (blnStaticBox)
{
_curDoc.frmPrompt.elements[strPromptName+"Static"].value=strValue;
_curDoc.getElementById(strPromptName).value=strValue;
}
else
_curDoc.frmPrompt.elements[strPromptName].value=strValue;
_curDoc.getElementById(prompts[iPrompt].paramIndexName).value=strIndex;
onValueChange(strPromptName);
}
}
}
function addMulti(strPromptName)
{
var iPrompt=getPromptIndex(strPromptName);
var arrSel=objLov[iPrompt].getLOVSelection();
if (arrSel!=null && arrSel.length>0)
{
var as=new Array();
var j=0;
var l=arrSel.length;
for (var i=0;i<l;i++)
{
if (arrSel[i].value!="")
{
as[j]=new Array(3);
var val=arrSel[i].value;
var idx=val.indexOf('_');
as[j][0]=val.substring(idx+1);
as[j][1]=i;
as[j][2]=val;
j++;
}
}
var at=new Array();
var tg=lstPromptValues[iPrompt];
j=0;
l=tg.getCount();
for (var i=0;i<l;i++)
{
if (tg.getValue(i)!="")
{
    at[j]=tg.getValue(i);
j++;
}
}
var r=getItems2Insert(as,at);
insertNewItemsInList(tg,r);
updatePromptValue(strPromptName);
onValueChange(strPromptName);
}
}
function addTxtToList(strID)
{
var strValue=_curDoc.getElementById(strID).value; 
var newValue=processFreeValues(strValue);
if ( strValue=='' )
return
var strPromptName = strID;
var iPos=strID.indexOf("EnterValue");
if (iPos>=0) strPromptName=strID.substring(0,iPos);
addMultiToList(strPromptName,newValue.split(';'));
_curDoc.getElementById(strID).value='';
}
function addMultiToList(strPromptName,arrValues)
{
var l=arrValues.length;
if (l>0)
{
var cpt=0;
var as=new Array();
for (var i=0;i<l;i++)
{
var bExist=false;
for(var j=0;j<as.length;j++)
{
if(as[j][0] == arrValues[i])
{
bExist=true;
break;
}
}
if(!bExist)
{
as[cpt]=new Array(3);
as[cpt][0]=arrValues[i];
as[cpt][1]=cpt;
as[cpt][2]="_"+arrValues[i];
cpt++;
}
}
var at=new Array();
var iPrompt=getPromptIndex(strPromptName);
var tg=lstPromptValues[iPrompt];
var j=0;
l=tg.getCount();
for (var i=0;i<l;i++)
{
if (tg.getValue(i)!="")
{
at[j]=tg.getValue(i);
j++;
}
}
var r=getItems2Insert(as,at);
insertNewItemsInList(tg,r);
updatePromptValue(strPromptName);
onValueChange(strPromptName);
}
}
function removeMono(strPromptName, blnStaticBox)
{
if (typeof(blnStaticBox)=="undefined") blnStaticBox=false;
if (blnStaticBox)
{
_curDoc.frmPrompt.elements[strPromptName+"Static"].value="";
_curDoc.getElementById(strPromptName).value="";
}
else
_curDoc.frmPrompt.elements[strPromptName].value="";
onValueChange(strPromptName);
}
function dblClickRemove()
{
var strPromptName=this.id;
if (typeof(strPromptName)!="undefined")
{
var iPos = strPromptName.indexOf("List");
if (iPos>=0) strPromptName = strPromptName.substring(0, iPos);
removeMulti(strPromptName);
}
}
function removeMulti(strPromptName)
{
var iPrompt=getPromptIndex(strPromptName);
var tg=lstPromptValues[iPrompt];
var arrSel=tg.getMultiSelection();
if (arrSel!=null)
{
for (var i=arrSel.length-1;i>=0;i--)
{
tg.del(arrSel[i].index);
}
if (tg.getCount()==0)
{
var strPromptNoSelValue="(--No selected value--)";
tg.add(strPromptNoSelValue,'',false);
}
}
updatePromptValue(strPromptName);
onValueChange(strPromptName);
}
function updatePromptValue(strPromptName)
{
var iPrompt=getPromptIndex(strPromptName);
var strPromptValue="";
var strPromptIndex="";
var tg=lstPromptValues[iPrompt];
for (var i=0;i<tg.getCount();i++)
{
var val=tg.getValue(i);
if (val!="")
{
var arr=new Array(2);
var idx=val.indexOf('_');
arr[0]=val.substring(0,idx);
arr[1]=val.substring(idx+1);
if (i>0)
{
strPromptIndex+=";";
strPromptValue+=";";
}
strPromptIndex+=arr[0];
strPromptValue+=arr[1];
}
}
_curDoc.getElementById(prompts[iPrompt].paramIndexName).value=strPromptIndex;
_curDoc.getElementById(strPromptName).value=strPromptValue;
}
function addMonoFromTreeLov(strPromptName,blnStaticBox)
{
var iPrompt=getPromptIndex(strPromptName);
var objSource=treeLov[iPrompt];
if (objSource == null) return; 
if (this.multiSelection) 
return; 
var sel =objSource.getSelectedItem();
if (sel && sel.selectableItem)
{
var arr=getIndexValueFromTreeLov(sel);
var strIndex=arr[0];
var strValue=arr[1];
if (typeof(blnStaticBox)=="undefined") blnStaticBox=false;
if (blnStaticBox)
{
_curDoc.frmPrompt.elements[strPromptName+"Static"].value=strValue;
_curDoc.getElementById(strPromptName).value=strValue;
}
else
_curDoc.frmPrompt.elements[strPromptName].value=strValue;
_curDoc.getElementById(prompts[iPrompt].paramIndexName).value=strIndex;
onValueChange(strPromptName);
}
}
function addMultiFromGroupedLov(strPromptName)
{
var iPrompt=getPromptIndex(strPromptName);
var objSource=null;
if (typeof(txtEnterValue)!="undefined")
{
objSource=txtEnterValue[iPrompt];
if((objSource!=null) && (objSource.getValue()!=""))
{
addTxtToList(objSource.id);
}
}
addMultiFromTreeLov(strPromptName);
}
function addMultiFromTreeLov(strPromptName)
{
var iPrompt=getPromptIndex(strPromptName);
var objSource=treeLov[iPrompt];
if (objSource==null) return; 
var arraySel = objSource.getSelectedItems(),len = arraySel.length;
if (len==0) return; 
for (var i=0;i<len;i++)
{
var sel=arraySel[i];
if (sel && sel.selectableItem)
{
var tg=lstPromptValues[iPrompt];
var arr=getIndexValueFromTreeLov(sel);
var strIndex=arr[0];
var strValue=arr[1];
var strIdxVal=strIndex+'_'+strValue;
var blnExist=false;
for (var j=0;j<tg.getCount();j++)
{
if (cannotAddValue(tg.getValue(j),strIdxVal))
{
blnExist=true;
break;
}
}
if (!blnExist)
{
if (tg.getCount()==1 && tg.getValue(0)=="")
tg.del(0);
tg.add(strValue,strIdxVal,false);
}
}
}
updatePromptValue(strPromptName);
onValueChange(strPromptName);
}
function treeLov_clickCB()
{
}
function treeLov_dclickCB()
{
var strUserData="";
if(this.multiSelection)
{
var arraySel = this.getSelectedItems(), len = arraySel.length;
if(len == 0) return; 
for(var i=0;i<len;i++)
{
if (arraySel[i].selectableItem)
{
strUserData=arraySel[i].userData;
break;
}
}
}
else 
{
var sel = this.getSelectedItem();
if (sel && sel.selectableItem)
strUserData=sel.userData;
}
if (strUserData!="")
{
var arr=strUserData.split('_');
var pt=prompts[parseInt(arr[0])];
if (pt.isNestedPrompt())
pt=getGroupedPrompt(pt);
if (pt.type=="Mono")
addMonoFromTreeLov(pt.paramName,pt.isConstrained);
else
addMultiFromTreeLov(pt.paramName);
}
}
function treeLov_CompleteLOVCB()
{
_this=this;
var val=_this.userData;
var arrData=new Array(3);
var iPos=val.indexOf('_');
if (iPos>-1)
{
arrData[0]=val.substring(0,iPos);
val=val.substring(iPos+1);
iPos=val.indexOf('_');
if (iPos>-1)
{
arrData[1]=val.substring(0,iPos);
arrData[2]=val.substring(iPos+1);
}
else
arrData[1]=val;
}
var iPrompt=arrData[0];
var pt=prompts[iPrompt];
_curDoc.getElementById(pt.paramIndexName).value=arrData[1];
if (typeof(arrData[2])=="undefined")
_curDoc.getElementById(pt.paramName).value=_this.name;
else
_curDoc.getElementById(pt.paramName).value=arrData[2];
if (iPrompt>0)
{
iPrompt--;
submitLovAction(prompts[iPrompt].paramName,"R",0,"yes");
}
}
function treeLov_CompleteChunkCB()
{
_this=this;
var strPromptName=this.userData[0];
var iChunk=this.userData[1];
submitLovAction(strPromptName,"B",iChunk);
}
function getPromptIndex(strPromptName)
{
var iPrompt=null;
for (var i=0;i<arrPrompts.length;i++)
{
if (prompts[i].paramName==strPromptName)
{
iPrompt=prompts[i].index;
break;
}
}
return iPrompt;
}
function getIndexValueFromTreeLov(sel)
{
var arrRet=new Array(2);
if (sel!=null)
{
var userData=sel.userData;
var idx=userData.indexOf('_');
var val=userData.substring(idx+1);
var idx=val.indexOf('_');
if (idx<0)
{
arrRet[0]=val;
arrRet[1]=sel.name;
}
else
{
arrRet[0]=val.substring(0,idx);
arrRet[1]=val.substring(idx+1);
}
}
return arrRet;
}
function getGroupedPrompt(np)
{
var iPrompt=np.index;
for (var i=iPrompt-1;i>=0;i--)
{
if (prompts[i].isGrouped)
{
iPrompt=i;
break;
}
}
return prompts[iPrompt];
}
function getItems2Insert(as,at)
{
var arrRet=new Array();
if (as && at)
{
var l=as.length;
for (var i=0;i<at.length;i++)
{
as[l]=new Array(3);
as[l][0]=at[i].substring(at[i].indexOf('_')+1);
as[l][1]=-1;
as[l][2]=at[i];
l++;
}
as.sort();
l=as.length-1;
var j=0;
for (var i=0;i<l;i++)
{
if (as[i]!=null && !as[i].rejected)
{
for (var k=i+1;k<l+1;k++)
{
if(as[i][0]==as[k][0]) 
{
if(cannotAddValue(as[i][2],as[k][2])) 
{
as[i].rejected=true;
as[k].rejected=true;
}
}
else
break;
}
if (!as[i].rejected)
{
arrRet[j]=as[i];
j++;
}
}
}
if (as[l]!=null && !as[l].rejected)
{
arrRet[j]=as[l];
}
var str;
for (var i=0;i<arrRet.length;i++)
{
str=arrRet[i][0];
arrRet[i][0]=arrRet[i][1];
arrRet[i][1]=str;
}
arrRet.sort(sortNumber);
for (var i=0;i<arrRet.length;i++)
{
str=arrRet[i][0];
arrRet[i][0]=arrRet[i][1];
arrRet[i][1]=str;
}
}
return arrRet;
}
function sortNumber(a,b)
{
return a[0]-b[0];
}
function insertNewItemsInList(tg,r)
{
if (tg.getCount()==1 && tg.getValue(0)=="")
tg.del(0);
var l=r.length;
for (var i=0;i<l;i++)
{
var k=r[i][1];
if (k>=0)
{
tg.add(r[i][0],r[i][2],false);
}
}
}
function cannotAddValue(idxVal1,idxVal2) 
{
var ret=false;
if(idxVal1 == idxVal2)
ret=true;
else
{
var pos,idx1,val1,idx2,val2;
pos =idxVal1.indexOf('_');
idx1=idxVal1.substring(0,pos); 
val1=idxVal1.substring(pos+1); 
pos=idxVal2.indexOf('_');
idx2=idxVal2.substring(0,pos); 
val2=idxVal2.substring(pos+1); 
if((val1 == val2) && (idx1=="" || idx2==""))
ret=true;
}
return ret;
}

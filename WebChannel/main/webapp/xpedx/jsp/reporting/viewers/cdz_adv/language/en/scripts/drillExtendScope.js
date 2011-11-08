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
*/initialized=false
scopeDrillDlg=null
function loadDialog()
{
if (!initialized)
{
initialized=true
urlImg = parent._root+parent._img
scopeDrillDlg = newDialogBoxWidget("scopeDrillDlg","Extend the Scope of Analysis",410,440,okCB,closeDlg,false)
scopeDrillZone=newFrameZoneWidget("scopeDrillZone",400,null,true)
scopeDrillScroll=newScrolledZoneWidget("ambDrillScroll",0,0,400,200,"dialogzone")
var cpt=0;
for(var i=0;i<lenScope;i++)
{
if(arrScope[i].outScope)
{
arrCheck[cpt]=newCheckWidget("dimCheck"+i,arrScope[i].dimName,updateCheckScope,false,urlImg+"dim_o.gif",14,16,false);
arrCheck[cpt].paramName='idDim'+i
cpt++
}
else if(arrScope[i].filter)
{
arrCheck[cpt]=newCheckWidget("fltCheck"+i,arrScope[i].filter,updateCheckScope,false,urlImg+"filter_o.gif",14,16,false);
arrCheck[cpt].paramName='idFilter'+i
if(arrScope[i].isQFilter) arrCheck[cpt].isQFilter=true
cpt++
}
}
OKButton=newButtonWidget("scopeDrillOK","OK","okCB()",60)
CancelButton=newButtonWidget("scopeDrillCancel","Cancel","closeDlg()",60)
HelpButton=newButtonWidget("scopeDrillHelp","Help",null,60)
scopeDrillInfo= newInfoWidget("scopeDrillInfo","More Information","","Select the dimensions and filters you want to return to the document in order to continue your drill analysis.")
}
var sCheck=''
if (lenScope>0)
{
var sCheck='<table class="dialogzone"><tr><td><b>'+arrScope[0].hName+'</b>'+'</td></tr>'
cpt=0;
for(var i=0;i<lenScope;i++)
{
if(arrScope[i].outScope)
{
sCheck+='<tr valign="middle"><td style="padding-left:30px">'
sCheck+=arrCheck[cpt].getHTML()
sCheck+='</td></tr>'
cpt++
}
else
{
sCheck+='<tr valign="bottom"><td style="padding-left:52px">'
sCheck+=img(urlImg+'dim_o.gif',14,16,'bottom')+arrScope[i].dimName
sCheck+='</td></tr>'
}
if(arrScope[i].filter)
{
sCheck+='<tr valign="middle"><td style="padding-left:80px">'
sCheck+=arrCheck[cpt].getHTML()
sCheck+='</td></tr>'
cpt++
}
}
sCheck+='</table>'
}
if (_curWin.scopeDrillInitialized!=true)
{
_curWin.scopeDrillInitialized=true
targetApp
(
scopeDrillDlg.beginHTML()+
'<table class="dialogzone" width="100%" cellpadding="0" cellspacing="5" border="0"><tbody>'+
'<tr><td><IMG src="'+urlImg+'../DrillOutsideScopeDialog.gif'+'">This drill action requires additional data.<br></td></tr>'+
'<tr><td>Select dimensions and filters to include for further analysis.<br></td></tr>'+
'<tr width="100%"><td style="padding-top:5px;padding-bottom:5px">'+
scopeDrillZone.beginHTML()+
scopeDrillScroll.beginHTML()+
'<table class="dialogzone" cellpadding="0" cellspacing="5" border="0"><tbody>'+
'<tr><td><div id="scopeDrillOptions">'+
sCheck+
'</div></td></tr>'+
'</tbody></table>'+
scopeDrillScroll.endHTML()+
scopeDrillZone.endHTML()+
'</td></tr>'+
'<tr><td>'+
scopeDrillInfo.getHTML()+
'</td></tr>'+
'<tr><td align="right">'+
'<table cellpadding="0" cellspacing="5" border="0"><tbody>'+
'<tr>'+
'<td>'+OKButton.getHTML()+'</td>'+
'<td>'+CancelButton.getHTML()+'</td>'+
'<td>'+HelpButton.getHTML()+'</td>'+
'</tr>'+
'</tbody></table>'+
'</td></tr>'+
'</tbody></table>'+
scopeDrillDlg.endHTML()
)
}
else
{
var l = getLayer('scopeDrillOptions')
if(l) l.innerHTML=sCheck
}
scopeDrillDlg.init();
scopeDrillZone.init();
scopeDrillInfo.init();
OKButton.init();
CancelButton.init();
HelpButton.init();
HelpButton.setDisplay(false)
scopeDrillDlg.attachDefaultButton(OKButton);
for(var i=0;i<arrCheck.length;i++)
{
arrCheck[i].init()
if(arrCheck[i].isQFilter)
{
arrCheck[i].check(true)
}
}
scopeDrillDlg.show(true)
}
function createScopeForm()
{
var s=''
s='<form style="display:none" name="ScopeForm" method="post" target="Report" action="processDrillOutOfScope'+parent._appExt+'?'+strQueryString+'">'
for(var i=0;i<lenScope;i++)
{
if(arrScope[i].outScope)
{
s+='<input name="dimensions" id="idDim'+i+'" type="checkbox" value="'+arrScope[i].dimID+'">'+'\n'
}
if(arrScope[i].filter)
{
if(arrScope[i].isQFilter)
{
s+='<input name="rem_filtres_id_ck" id="idFilter'+i+'" type="checkbox" value="'+arrScope[i].dimID+'" checked>'+'\n'
s+='<input type="hidden" name="rem_filtres_val" value="'+arrScope[i].filterValue+'">'+'\n'
s+='<input type="hidden" name="rem_filtres_id" value="'+arrScope[i].dimID+'">'+'\n'
}
else
{
s+='<input name="filtres_id" id="idFilter'+i+'" type="checkbox" value="'+arrScope[i].dimID+'" >'+'\n'
s+='<input type="hidden" name="filtres_val" value="'+arrScope[i].filterValue+'">'+'\n'
}
}
}
s+='</form>'
document.write(s)
}
function okCB()
{
updateFilterValArray();
setTimeout("document.ScopeForm.submit()",1);
parent.eventManager.notify(parent._EVT_REFRESH_DATA);
scopeDrillDlg.show(false);
parent.wt();
}
function closeDlg()
{
scopeDrillDlg.show(false)
}
function updateCheckScope()
{
var name=this.paramName
eval("document.ScopeForm."+name+".checked="+this.isChecked()+"")
}
function updateFilterValArray()
{
var filtres_id=document.ScopeForm.filtres_id
var len=filtres_id?filtres_id.length:0
if(len>0)
{
var nbFilter = 0;
for (var i=0; i<len; i++)
    {
        if(filtres_id[i].checked == true) nbFilter++;
    }
    if(nbFilter > 0)
    {
        var arrFilter = new Array(nbFilter);
        var nbIndex = 0;
        var filtres_val=document.ScopeForm.filtres_val
        for (var i=0; i<len; i++)
        {
            if(filtres_id[i].checked == true)
            {
                arrFilter[nbIndex] = filtres_val[i].value;
                nbIndex++;
            }
            filtres_val[i].value = "";
        }
        for (var i=0; i<nbFilter; i++)
        {
            filtres_val[i].value = arrFilter[i];
        }
    }
}
return true;
}
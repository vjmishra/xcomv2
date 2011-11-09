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
*/function newUnvObjTreeTooltipWidget(maxw,maxh,autohide,delay)
{
var o=newTooltipWidget(maxw,maxh)
o.autohide=(autohide!=null)?autohide:true
o.delay=(delay!=null)?delay:5000
o.showCustomTooltip=newUnvObjTreeTooltipWidget_showCustomTooltip
o.hideCustomTooltip=o.hide
return o
}
function newUnvObjTreeTooltipWidget_showCustomTooltip(data,e)
{
var o=this
var s=''
s+='<table class="dragTxt"><tbody>'
s+='<tr><td><b>Object:</b>&nbsp;'+data.name+'</td></tr>'
s+='<tr><td><b>Type:</b>&nbsp;'
switch (data.dataType)
{
case 0:
s+='Date'
break
case 1:
s+='Number'
break
case 2:
s+='Text'
break
}
if(data.kind == 3)
{
s+='</td></tr>'
s+='<tr><td><b>Aggregation:</b>&nbsp;'
switch (data.aggregateFct)
{
case 'AVERAGE':
s+='Average'
break
case 'COUNT':
s+='Count'
break
case 'DELEGATED':
s+='Database delegated'
break
case 'MAX':
s+='Max'
break
case 'MIN':
s+='Min'
break
case 'NONE':
s+='None'
break
case 'SUM':
s+='Sum'
break
}
}
s+='</td></tr>'
s+='<tr><td><b>Based on:</b>&nbsp;'+data.dsName+'</td></tr>'
s+='<tr><td><b>Description:</b>&nbsp;'+data.desc+'</td></tr>'
s+='<tr><td><div style="overflow:hidden;height:4px;border-top:1px solid black;margin-top:4px"></div></td></tr>'
s+='<tr><td><b>Technical Information:</b>&nbsp;'+data.techInfo+'</td></tr>'
s+='<tr><td><b>Mapping:</b>&nbsp;'+data.mapping+'</td></tr>'
s+='<tr><td><b>Lineage:</b>&nbsp;'+data.lineage+'</td></tr>'
s+='</table></tbody>'
o.show(true,s,null,null,null,null,null,true,e)
if ( o.autohide )
setTimeout("autoHideTooltip()",o.delay)
}
function autoHideTooltip()
{
var o=window._theGlobalTooltip
if (o==null)
return
if (o.hideCustomTooltip!=null)
o.hideCustomTooltip()
}

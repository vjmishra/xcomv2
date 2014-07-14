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
*/function BreakElement(expr, axis, id, header, footer, removeDupl, centerVal,
applySort, newPage, noBrkIncBlk, repHeader, repBrkVal, selected)
{
var o=this;
o.expr=expr;
o.axis=axis;
o.id=id;
o.header=header;
o.footer=footer;
o.removeDupl=removeDupl;
o.centerVal=centerVal;
o.applySort=applySort;
o.newPage=newPage;
o.noBrkIncBlk=noBrkIncBlk;
o.repHeader=repHeader;
o.repBrkVal=repBrkVal;
o.selected=selected;
o.toString=BreakElement_toString;
return o;
}
function BreakElement_toString()
{ 
var tmp =  this.expr + "," + this.axis + "," + this.id; 
tmp += "," + this.header + "," + this.footer; 
tmp += "," + this.removeDupl + "," + this.centerVal; 
tmp += "," + this.applySort + "," + this.newPage;
tmp += "," + this.noBrkIncBlk + "," + this.repHeader;
tmp += "," + this.repBrkVal + "," + this.selected;
return tmp;
}

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
*/function formatMultiFluxSQL(s)
{
s=""+s
var cpt=1
var str="",substr=""
var start=0,index=-1
index=s.indexOf("  SELECT\n",start)
if(index > -1)
{
while(index>-1)
{
substr=s.substring(start,index)
str=str.concat(substr)
str=str.concat(((cpt==1)?"\n":"")+_selectSQLLabel+cpt+"\n")
cpt++
start=index
index=s.indexOf("  SELECT\n",index+3)
}
substr=s.substring(start,s.length)
str=str.concat(substr)
return str;
}
else
return s;
}
function formatSQL(s)
{
var res=''
var start=false
if (s!=null)
{
s = formatMultiFluxSQL(s)
var len=s.length
for (var i=0;i<len;i++)
{
var c=s.charAt(i)
switch (c)
{
case '\n': 
res+=c;
start=true
break
case ' ': 
res+=c
if(start)
res+='  ';
break
default: 
res+=c; 
start=false
break
}
}
}
return res
}
function displaySQL(sql, dpName )
{
self.document.submitForm.title.value = parent._viewSQLLabel+ ' - ' + dpName
sql =parent.convStr(formatSQL(sql),true,true)
self.document.submitForm.textToPrint.value = "<div class='sqlText'>"+sql+"</div>"
var features = "width=640,height=480,menubar,resizable,scrollbars"
var url =parent._root+"language/"+parent._lang+"/html/printWindow.html"
url += "?noGrabber=yes&skin=" +parent._root + "lib/images/" + parent._skinName + "/&lang=" + parent._lang
var newWindow = window.open(url, "printSQLWindow", features)
if(newWindow) newWindow.focus()
}

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
*/function formatDate(date,format)
{
    format=format+"";
    var result="";
    var i_format=0;
    var c="";
    var token="";
    var y=date.getYear()+"";
    var M=date.getMonth()+1;
    var d=date.getDate();
    var E=date.getDay();
    var H=date.getHours();
    var m=date.getMinutes();
    var s=date.getSeconds();
    var yyyy,yy,MMM,MM,dd,hh,h,mm,ss,ampm,HH,H,KK,K,kk,k;
    var value=new Object();
    if (y.length < 4) {y=""+(y-0+1900);}
    value["y"]=""+y;
    value["yyyy"]=y;
    value["yy"]=y.substring(2,4);
    value["M"]=M;
    value["MM"]=LZ(M);
    value["MMM"]=arrMonthNames[M-1];
    value["NNN"]=arrMonthNames[M+11];
    value["d"]=d;
    value["dd"]=LZ(d);
    value["E"]=arrDayNames[E+7];
    value["EE"]=arrDayNames[E];
    value["H"]=H;
    value["HH"]=LZ(H);
    if (H==0){value["h"]=12;}
    else if (H>12){value["h"]=H-12;}
    else {value["h"]=H;}
    value["hh"]=LZ(value["h"]);
    if (H>11){value["K"]=H-12;} else {value["K"]=H;}
    value["k"]=H+1;
    value["KK"]=LZ(value["K"]);
    value["kk"]=LZ(value["k"]);
    if (H > 11) { value["a"]=get_AM_PM_LocaleString("PM"); }
    else { value["a"]=get_AM_PM_LocaleString("AM"); }
    value["m"]=m;
    value["mm"]=LZ(m);
    value["s"]=s;
    value["ss"]=LZ(s);
    while (i_format < format.length) {
        c=format.charAt(i_format);
        token="";
        while ((format.charAt(i_format)==c) && (i_format < format.length)) {
            token += format.charAt(i_format++);
        }
        if (value[token] != null) { result=result + value[token]; }
        else { result=result + token; }
     }
     return result;
}
function LZ(x)
{
    return(x<0||x>9?"":"0")+x
}
function get_AM_PM_LocaleString(str)
{
strRet = str;
if (arr_AM_PM_LocaleString)
{
if (str == "AM")
strRet = arr_AM_PM_LocaleString[0];
else if (str == "PM")
strRet = arr_AM_PM_LocaleString[1];
}
return strRet;
}
function setDateValue(strDateValue, strInputFormat)
{
var strRet = ",,";
var strYear = "";
var strMonth = "";
var strDay = "";
var length = strInputFormat.length;
var sep = "";
for (var i=0; i<length; i++)
{
         var c = strInputFormat.charAt(i);
         switch(c)
 {
             case "/":
             case "-":
             case ".":
             case ",":
             case "\"": sep = c; break;
          }
          if (sep != "") break;
}
if (sep != "")
{
var arrInputFormat = strInputFormat.split(sep);
var arrDateValue = strDateValue.split(sep);
for (var i=0; i<arrDateValue.length; i++)
{
if (arrInputFormat[i] != null && typeof(arrInputFormat[i]) != "undefined")
{
if (arrInputFormat[i].indexOf('y')>=0)
{
var iPosA = arrInputFormat[i].indexOf('y');
var iPosB = arrInputFormat[i].lastIndexOf('y');
if (iPosB>=0)
{
strYear = arrInputFormat[i].substring(iPosA, iPosB + 1);
if (strYear.length >= arrDateValue[i].length)
strYear = arrDateValue[i];
else
{
iPosB = iPosA;
for (var j=iPosA; j<arrDateValue[i].length; j++)
{
var c = arrDateValue[i].charAt(j);
if (c < '0' || c > '9') break;
else iPosB = j + 1;
}
strYear = arrDateValue[i].substring(iPosA, iPosB);
}
}
else
return strRet;
}
else if (arrInputFormat[i].indexOf('M')>=0)
{
var iPosA = arrInputFormat[i].indexOf('M');
var iPosB = arrInputFormat[i].lastIndexOf('M');
if (iPosB>=0)
{
strMonth = arrInputFormat[i].substring(iPosA, iPosB + 1);
if (strMonth.length >= arrDateValue[i].length)
strMonth = arrDateValue[i];
else
{
iPosB = iPosA;
for (var j=iPosA; j<arrDateValue[i].length; j++)
{
var c = arrDateValue[i].charAt(j);
if (c < '0' || c > '9') break;
else iPosB = j + 1;
}
strMonth = arrDateValue[i].substring(iPosA, iPosB);
}
}
else
return strRet;
}
else if (arrInputFormat[i].indexOf('d')>=0)
{
var iPosA = arrInputFormat[i].indexOf('d');
var iPosB = arrInputFormat[i].lastIndexOf('d');
if (iPosB>=0)
{
strDay = arrInputFormat[i].substring(iPosA, iPosB + 1);
if (strDay.length >= arrDateValue[i].length)
strDay = arrDateValue[i];
else
{
iPosB = iPosA;
for (var j=iPosA; j<arrDateValue[i].length; j++)
{
var c = arrDateValue[i].charAt(j);
if (c < '0' || c > '9') break;
else iPosB = j + 1;
}
strDay = arrDateValue[i].substring(iPosA, iPosB);
}
}
else
return strRet;
}
}
}
if (strMonth != "" && strDay != "" && strYear != "" && !(isNaN(strMonth) || isNaN(strDay) || isNaN(strYear)))
{
strRet = strMonth + ',' + strDay + ',' + strYear;
}
}
return strRet;
}

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
*/_strServerInstance="";
function cancelQueryInDotNet()
{
var arrTokens=_strServerInstance.split(',');
var strQueryString="cancelRequest"+"&SessionID="+arrTokens[0]+"&DocumentToken=&CancelMode=2"+"&CdzSession="+_strServerInstance;
var strURL=self.location.href;
var iPos=strURL.indexOf('?');
if (iPos>-1) strURL=strURL.substring(0, iPos);
iPos=strURL.lastIndexOf('/');
if (iPos>-1) strURL=strURL.substring(0, iPos + 1);
var strURL=strURL+"cdzisapi.wis?"+strQueryString;
self.SecondDlgFrame.location.replace(strURL);
}

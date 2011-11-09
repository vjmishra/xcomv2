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
*/function refresh()
{
refreshDocument();
}
function publish()
{
var iRepoType = getIntRepoType(strRepoType);
var strToken = getToken();
parent.goPublish(strDocName, strDocID, iRepoType, strDocType, strToken);
}
function save(isQuery) 
{
var strQuery=isQuery?"&querySave=true":""
var p=urlParams(true,true)
if (p!="")
{
p += "&quickSave=true" + "&name="+convURL(strDocName)+"&id="+strDocID+"&repotype="+strRepoType+"&doctype="+strDocType+"&ViewType="+strViewType+"&forceViewType="+getForceViewType()
p += strQuery
if (window.bFullScreen)
p+="&mode=win";
frameNav("DlgFrame", "processSave" + _appExt + p)
}
}
function saveAs(isQuery) 
{
var strQuery=isQuery?"?querySave=yes":""
frameNav("DlgFrame","language/"+_lang+"/html/saveAsDialog.html" + strQuery)
}
function send()
{
var iRepoType = getIntRepoType(strRepoType);
var strToken = getToken();
parent.goSend(strDocName, strDocID, iRepoType, strDocType, strToken);
}
function edit(strEditor)
{
var iRepoType = getIntRepoType(strRepoType);
var strToken = getToken();
if (parent.goEdit)
parent.goEdit("", strDocID, "", strDocType, strToken, strEditor);
else if (window.opener)
{
var topfs=getOpenerTopFrame();
if (topfs!=null && topfs.goEdit)
topfs.goEdit("", strDocID, "", strDocType, strToken, strEditor);
}
}
function canGoEdit()
{
if (parent.goEdit) return true;
if (window.opener)
{
var topfs=getOpenerTopFrame();
if (topfs!=null && topfs.goEdit)
return true;
}
return false;
}
function addToMyInfoView()
{
if ( parent.addTo )
parent.addTo()
}
function setDocTitle(strDocName)
{
var isInPopupWindow = false;
if (typeof(parent.isPopupWindow) != "undefined")
{
isInPopupWindow=parent.isPopupWindow();
var docType = (typeof(strDocType) != "undefined")? strDocType : 'wid';
var docKind = (docType == "rep") ? "Desktop Intelligence" : "Web Intelligence";
if (!isInPopupWindow)
{
if (typeof(parent.displayHeaderInfo) != "undefined")
{
parent.displayHeaderInfo(docKind, 1, strDocName);
}
}
else
{
parent.document.title = docKind + ' - ' + strDocName; 
}
}
}
function getToken()
{
return strEntry;
}
function getIntRepoType(strRepoType)
{
var iRepoType = "";
if (strRepoType == "corporate")
iRepoType = "0";
else if (strRepoType == "inbox")
iRepoType = "1";
else if (strRepoType == "personal")
iRepoType = "2";
else
iRepoType = "-1";
return iRepoType;
}
function updateLastRefreshDate(strLastRefreshDate)
{
if (parent.setLastRefreshDate)
parent.setLastRefreshDate(strLastRefreshDate);
}
function invalidSession()
{
_dontCloseDoc=true;
if (parent.goInvalidSession)
parent.goInvalidSession()
else if (parent.parent && parent.parent.goInvalidSession)
parent.parent.goInvalidSession();
else if (window.opener)
{
var topfs=getOpenerTopFrame();
if (topfs!=null && topfs.goInvalidSession)
topfs.goInvalidSession();
}
}
function backToParent()
{
if (parent.goBack)
parent.goBack();
else if (window.opener)
{
var topfs=getOpenerTopFrame();
if (topfs!=null && topfs.goBack)
topfs.goBack();
}
}
function initHelpSection(subPath)
{
    var subHelpObj = null;
if (parent.initHelpSystem)
subHelpObj = parent.initHelpSystem(subPath);
if (parent.helpURL)
{
    var i=parent.helpURL.indexOf("webintelligence");
    if (i>0)
    { 
        var help=parent.helpURL.substr(0, i);
        parent.helpURL= help+subPath;
    }  
}
    return subHelpObj;
}
function setHelpSection(subHelpObj, section)
{
if (parent.setHelpSystemSection)
parent.setHelpSystemSection(subHelpObj, section);
}
function getOpenerTopFrame()
{
var wop=window.opener;
var topfs=null;
var iMaxLoop=100;
var i=0;
while (i<iMaxLoop)
{
if (wop!=wop.parent)
wop=wop.parent;
else
{
topfs=wop;
break;
}
i++;
}
return topfs;
}
function getTopFrame()
{
var wop=self;
var topfs=null;
var iMaxLoop=100;
var i=0;
while (i<iMaxLoop)
{
if (wop!=wop.parent)
wop=wop.parent;
else
{
topfs=wop;
break;
}
i++;
}
return topfs;
}
function getTopIVFrame()
{
var wop=self;
var topIVFr=null;
var iMaxLoop=100;
var i=0;
if (_moz)
{
_oldErrHandler=window.onerror
window.onerror=localErrHandler
}
try
{
while (i<iMaxLoop)
{
if (wop!=wop.parent) {
wop=wop.parent;
} else if (wop.thisIsTheIVTopFrame == "wishYouAreHere") {
topIVFr=wop;
break;
} else if (wop.window.opener) {
wop=wop.window.opener
} 
i++;
}
} catch(e) {
topIVFr=null
}
if (_moz)
window.onerror=_oldErrHandler
return topIVFr;
}
function unload()
{
var url='processClose'+_appExt+'?iViewerID='+iViewerID;
url +=(typeof(strEntry)!="undefined")?'&sEntry='+strEntry:'';
sendAjaxRequest(url);
}
function sendAjaxRequest(url, callBackFunction)
{
try
{
if (window.XMLHttpRequest)
{
var xmlHttp=new XMLHttpRequest()
}
else if (window.ActiveXObject)
{
var xmlHttp=new ActiveXObject("Microsoft.XMLHTTP"); 
if (xmlHttp==null)
return;
}
xmlHttp.open('GET', url, false);
xmlHttp.onreadystatechange=onCloseResponse;
xmlHttp.send(null);
}
catch(err)
{
}
function onCloseResponse()
{
if (xmlHttp.readyState!=4)
{
return;
}
else if (callBackFunction != null)
{
callBackFunction(xmlHttp.responseText);
}
}
}

<!--
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
--><%@ include file="wistartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
_logger.info("-->viewPart.jsp");
System.out.println("::::::::inside viewPart.jsp::::::::::::::");
String strSkin = null;
String strViewerID = null;
String strDocID = null;
try
{
strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
strSkin = (String)session.getAttribute(ViewerTools.SessionSkin);
if (strSkin == null) strSkin = "skin_standard";
String strTarget = requestWrapper.getQueryParameter("sTarget", false, "");
if (!strTarget.equals("") && !strTarget.equals("viewPart"))
{
requestWrapper.setQueryParameter("sTarget", "viewPart");
requestWrapper.setQueryParameter("iViewerID", strViewerID);
requestWrapper.setQueryParameter("kind", "webi");
strTarget += ".jsp";
out.clearBuffer();
%>
<jsp:forward page="<%=strTarget%>"/>
<%
}
String strReportPart = requestWrapper.getQueryParameter("sReportPart", true);
String[] arrReportParts = new String[1];
arrReportParts[0] = strReportPart;
String strRefresh = requestWrapper.getQueryParameter("sRefresh", false, "no");
if (strRefresh.equalsIgnoreCase("y")) strRefresh = "yes";
String strPicker = requestWrapper.getQueryParameter("sPicker", false, "false");
String strCallbackParam = requestWrapper.getQueryParameter("sCallbackParam");
if (strCallbackParam == null) strCallbackParam = "null";
else strCallbackParam = "\"" + ViewerTools.escapeQuotes(strCallbackParam) + "\"";
DocumentInstance doc = null;
String strEntry = requestWrapper.getQueryParameter("sEntry", false, "");
if (strEntry.equals(""))
{
strDocID = requestWrapper.getQueryParameter("id", false, "");
if (strDocID.equals(""))
strDocID = requestWrapper.getQueryParameter("docid", true);
ReportEngine objReportEngine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
objReportEngine.setLocale(requestWrapper.getUserLocale());
doc = objReportEngine.openDocument(Integer.parseInt(strDocID));
_logger.info("open doc from doc id: " + strDocID);
String strRefreshOnOpen = doc.getProperties().getProperty(PropertiesType.REFRESH_ON_OPEN, "false");
strRefreshOnOpen = strRefreshOnOpen.toLowerCase();
_logger.info("Refresh on open: " + strRefreshOnOpen);
if (strRefresh.equals("yes") && !strRefreshOnOpen.equals("true"))
doc.refresh();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
}
else
{
doc = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("open doc from storage token: " + strEntry);
strDocID = doc.getProperties().getProperty(PropertiesType.DOCUMENT_ID);
if (strRefresh.equals("yes"))
{
doc.refresh();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
}
}
boolean blnNeedAnswerCP = false;
String strRedirectTo = "";
if (doc.getMustFillContexts())
{
strRedirectTo = "getContextsInfos.jsp";
blnNeedAnswerCP = true;
_logger.info("doc has contexts");
}
else if (doc.getMustFillPrompts())
{
strRedirectTo = "getPrompts.jsp";
blnNeedAnswerCP = true;
_logger.info("doc has prompts");
}
if (blnNeedAnswerCP)
{
objUserSettings.init(entSession, strDocID);
String strQS = requestWrapper.getQueryString();
strQS = ViewerTools.setQueryParameter(strQS, "sEntry", strEntry);
strQS = ViewerTools.setQueryParameter(strQS, "sNEV", "yes");
strQS = ViewerTools.setQueryParameter(strQS, "src", "RPV");
strRedirectTo += "?" + strQS;
%>
<html>
<head>
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript">
_root=getBasePath();
_lang="<%=strLanguage%>";
_skinName="<%=strSkin%>";
_img="images/main/";
initDom(_root + "lib/images/"+_skinName+"/", _lang);
</script>
<script language="javascript">styleSheet();</script>
<script language="javascript" src="lib/dialog.js"></script>
<script language="javascript" src="lib/waitdialog.js"></script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/labels.js"></script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/errorManager.js"></script>
<script language="javascript" src="scripts/IVIntegration.js"></script>
<script language="javascript" src="scripts/Utils.js"></script>
<script language="javascript">
var uiref="<%=ViewerTools.escapeQuotes(strReportPart)%>";
var docId="<%=ViewerTools.escapeQuotes(strDocID)%>";
var sCallbackParam=<%=strCallbackParam%>;
var blnPicker=<%=strPicker%>;
var iViewerID="<%=strViewerID%>";
var redirectTo="<%=ViewerTools.escapeQuotes(strRedirectTo)%>";
</script>
<script language="javascript">
_appExt=".jsp";
_appSecExt=".jsp";
setTopFrameset();
waitDlg=newWaitDialogBoxWidget("waitDlg",250,100,_prompts_title);
alertDlg=newAlertDialog("alertDlg","","",_ERR_OK_BUTTON_CAPTION,_promptDlgInfo);
function showAlertDialog(msg,title,iPromptType,okCB)
{
if (iPromptType==null) iPromptType=0
if (title==null) title=_prompts_title;
alertDlg.setText(msg)
alertDlg.setTitle(title)
alertDlg.setPromptType(iPromptType)
alertDlg.yesCB=(okCB)?okCB:null
alertDlg.show(true)
}
function hideWaitDlg()
{
waitDlg.show(false);
}
function showWaitDlg()
{
waitDlg.show(true);
}
function initDlgs()
{
alertDlg.init();
waitDlg.init();
}
function goReportPartViewer(strEntry)
{
var strURL="viewPart.jsp?sEntry="+strEntry+"&id="+docId+"&iViewerID="+iViewerID+"&sReportPart="+encodeURIComponent(uiref);
strURL+=((sCallbackParam!=null)?("&sCallbackParam="+sCallbackParam):"")+((blnPicker)?"&sPicker=true":"");
self.location.replace(strURL);
}
function goBackDashboard()
{
backToParent();
}
</script>
</head>
<body>
<script language="javascript">
alertDlg.write();
waitDlg.write();
</script>
<script language="javascript">
initDlgs();
showWaitDlg();
</script>
<script language="javascript">
document.write('<iframe name="DlgFrame" style="position:absolute;left:-100px;top:-100px;width:50px;height:50px" src="' + redirectTo + '"></iframe>');
</script>
</body>
</html>
<%
return;
}
ImageOption objImageOption = doc.getImageOption();
objImageOption.setImageCallback("getImage.jsp");
objImageOption.setImageNameHolder("name");
objImageOption.setStorageTokenHolder("sEntry");
ReportParts reportParts = null;
try
{
reportParts = doc.getReportParts(arrReportParts, OutputFormatType.DHTML);
}
catch(Exception e)
{
throw new Exception("VIEWER:_ERR_REPORT_PART_NOT_VALID");
}
HTMLView objHTMLView = (HTMLView) reportParts.getView();
objHTMLView.setUserAgent(request.getHeader("User-Agent"));
%>
<html>
<head>
<%
if (strPicker.equals("true"))
{
%>
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript" src="lib/menu.js"></script>
<script language="javascript">
_root=getBasePath();
_lang="<%=strLanguage%>";
_skinName="<%=strSkin%>";
_img="images/main/";
initDom(_root + "lib/images/"+_skinName+"/", _lang);
</script>
<script language="javascript">styleSheet()</script>
<script language="javascript" src="scripts/Utils.js"></script>
<script language="javascript">
listViewerEvents();
eventManager=newEventManager();
</script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/reportPicker.js"></script>
<script language="javascript">
var uiref="<%=ViewerTools.escapeQuotes(strReportPart)%>";
var docId="<%=ViewerTools.escapeQuotes(strDocID)%>";
var token="<%=ViewerTools.escapeQuotes(strEntry)%>";
var sCallbackParam=<%=strCallbackParam%>;
</script>
<%
}
%>
<script language="javascript" src="scripts/IVIntegration.js"></script>
<script language="javascript">
_ie=(document.all!=null)?true:false;
_dom=(document.getElementById!=null)?true:false;
_moz=_dom&&!_ie;
_appExt=".jsp";
_appSecExt=".jsp";
var isLoaded=false;
var isVisible=false;
var isPicker=<%=strPicker%>;
var strEntry="<%=strEntry%>";
var iViewerID="<%=strViewerID%>";
function moveTo(x,y)
{
var c=containerS;
if (x!=null)
{
if (_moz) c.left=""+x+"px";
else c.pixelLeft=x;
}
if (y!=null)
{
if (_moz) c.top=""+y+"px";
else c.pixelTop=y;
}
}
function resizeCB()
{
if (isLoaded)
{
var ww=_ie?window.document.body.clientWidth:window.innerWidth,wh=_ie?window.document.body.clientHeight:window.innerHeight;
var w=container.offsetWidth,h=container.offsetHeight;
var x=(w<ww)?((ww-w)/2):0;
var y=(h<wh)?((wh-h)/2):0;
moveTo(x,y);
if (!isVisible)
{
containerS.visibility = "visible";
isVisible=true;
}
}
}
function onloadCB()
{
if (isPicker)
addEvents(self.document);
}
function unloadCB()
{
unload();
}
<%
if (strPicker.equals("true"))
{
%>
function addEvents(l)
{
var idref=l.getAttribute?l.getAttribute("idref"):null,n=l.childNodes;
if (idref!=null)
{
l.onmousemove=mmove;
l.oncontextmenu=partContextMenu;
}
else if (n!=null)
{
var count=n.length;
for (var i=0;i<count;i++) addEvents(n[i]);
}
}
function newMenu(id,beforeShowCB)
{
var o=newMenuWidget(id,null,beforeShowCB)
o.oAdd=o.add
o.add=newMenu_add
return o
}
function newMenu_add(id,txt,icon,dx,cb)
{
var o=this,r=o[id]=o.oAdd(id,txt,cb?cb:clickCB,icon?(_img+icon+".gif"):null,16*dx,0,false,16*dx,16)
return r
}
function clickCB()
{
var myFrame=self;
var i=0,iMaxLoop=100;
while (i<iMaxLoop)
{
if (myFrame.parent && myFrame!=myFrame.parent)
{
myFrame=myFrame.parent;
if (myFrame.goSelectFullReport)
myFrame.goSelectFullReport(uiref,docId,token,sCallbackParam);
}
else
break;
i++;
}
}
function mmove()
{
var o=this;
var s=o.style;
s.cursor="default";
return;
}
<%
}
%>
</script>
</head>
<body onload="onloadCB()" onresize="resizeCB()" onunload="unloadCB()">
<%
objHTMLView.getContent(out, "", "");
%>
<script language="javascript">
container=document.body;
var c=container.childNodes;
for (var i=0;i<c.length;i++)
{
var t=c[i];
if (t&&t.tagName&&(t.tagName=="DIV"))
{
container=t;
break;
}
}
containerS=container.style;
isLoaded=true;
setTimeout("resizeCB()",1);
</script>
</body>
</html>
<%
}
catch(Exception e)
{
out.clearBuffer();
String strMsg = e.getLocalizedMessage();
String strCdzCatchError = "";
String strViewerMsgPrefix = "VIEWER:";
if (strMsg.startsWith(strViewerMsgPrefix))
strMsg = strMsg.substring(strViewerMsgPrefix.length());
else
{
strMsg = "\"" + ViewerTools.escapeQuotes(objUtils.getFormattedErrorMsg(strMsg, "")) + "\"";
strCdzCatchError = objUtils.getHTMLElement4Trace(strMsg, "");
e.printStackTrace();
}
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript" src="lib/dialog.js"></script>
<script language="javascript" src="scripts/IVIntegration.js"></script>
<script language="javascript">
_skinName="<%=strSkin%>"
_img="images/main/"
initDom("lib/images/"+_skinName+"/","<%=strLanguage%>")
</script>
<script language="javascript">styleSheet()</script>
<script language="javascript" src="language/<%=strLanguage%>/scripts/errorManager.js"></script>
<script language="javascript">
var strMsg = <%=strMsg%>;
alertDlg=newAlertDialog("ViewerAlertDlg","","",_ERR_OK_BUTTON_CAPTION,_promptDlgCritical);
function unloadCB()
{
if (alertDlg.layer)
alertDlg.show(false);
}
function loadCB()
{
alertDlg.init();
advDisplayViewerErrorMsgDlg(strMsg,_ERR_DEFAULT,viewerErrCB,true);
}
function showAlertDialog(msg,title,iPromptType,okCB)
{
alertDlg.setText(msg);
alertDlg.setTitle(title);
alertDlg.setPromptType(iPromptType);
alertDlg.yesCB=(okCB)?okCB:null;
alertDlg.show(true);
}
function wr(s)
{
document.write(s);
}
function viewerErrCB()
{
backToParent();
}
function writeBody()
{
wr('<body onselectstart="return false" ondragstart="return false" onunload="unloadCB()" style="cursor:default;overflow:hidden" class="bgzone" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">');
wr('<div style="top:0px;left:0px;width:100in;height:10px" id="DPI">');
alertDlg.write();
wr('<%=strCdzCatchError%>');
wr('</body>');
}
writeBody();
setTimeout("loadCB()",100);
</script>
</head>
</html>
<%
}
%>

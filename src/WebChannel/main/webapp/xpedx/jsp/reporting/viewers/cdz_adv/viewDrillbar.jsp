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
response.setContentType("text/html;charset=UTF-8");
try {
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int iReportIndex = Integer.parseInt(iReport);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
Report objReport = doc.getReports().getItem(iReportIndex);
DrillInfo objDrillInfo = (DrillInfo)objReport.getNamedInterface("DrillInfo");
DrillBar objDrillBar = null;
try {
objDrillBar = objDrillInfo.getDrillBar();
}
catch(Exception e1) {}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="lib/dom.js"></script>
<script language="javascript" src="lib/palette.js"></script>
<script language="javascript" src="lib/palette.js"></script>
<script language="javascript">initDom(parent.parent._skin,parent.parent._lang)</script>
<script language="javascript">styleSheet()</script>
<script language="javascript">
var urlLabels = "language/" + parent.parent._lang + "/" + "scripts/labels.js"
includeScript(urlLabels)
</script>
<script language="javascript">
objParent= parent.parent
var blnLoaded = false;
var iBlocHeight = null;
var arrFilters = new Array();
<%
String[][] arrFilters = new String[0][2];
DrillBarObject objDrillBarObject = null;
if (null != objDrillBar && objDrillBar.getCount()>0) {
int nbDrillBarElt = objDrillBar.getCount();
String strScriptCode = "";
arrFilters = new String[nbDrillBarElt][2];
for (int i=0; i<nbDrillBarElt; i++) {
objDrillBarObject = objDrillBar.getItem(i);
arrFilters[i][0] = objDrillBarObject.getName();
arrFilters[i][1] = objDrillBarObject.getID();
strScriptCode +=" arrFilters[" + Integer.toString(i) + "] = new Array(2);\n";
strScriptCode +=" arrFilters[" + Integer.toString(i) + "][0] = \"" + ViewerTools.escapeQuotes(arrFilters[i][0]) + "\";\n";
strScriptCode +=" arrFilters[" + Integer.toString(i) + "][1] = \"" + arrFilters[i][1] + "\";\n";
}
out.println(strScriptCode);
}
%>
function AddFilter(strID) {
var blnFound = false;
for (var i=0;i<arrFilters.length;i++) {
if (strID == arrFilters[i][1]) {
    blnFound = true;
    break;
    }
}
if (!blnFound) {
var strAction = "processDrillbar.jsp" + parent.parent.urlParamsNoBID() + "&addFilter=" + strID;
self.document.frmDrillbar.target = "_parent";
self.document.frmDrillbar.action = strAction;
self.document.frmDrillbar.submit();
}
}
document.onmousemove = mouseTracker;
if (!document.all) {
window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
}
function mouseTracker(e) {
e = e || window.Event || window.event;
window.pageX = e.pageX || e.clientX;
window.pageY = e.pageY || e.clientY;
}
function PopupMenu(e) {
if(objParent.showDrillFilterMenu)
objParent.showDrillFilterMenu(20, 100);
return true;
}
function MoveButtonAddIE(){
dyn_button.style.pixelLeft = document.body.scrollLeft;
dyn_button.style.pixelTop = document.body.scrollTop + document.body.clientHeight -20;
}
function MoveButtonAddNT(){
document.getElementById("dyn_button").style.left = window.pageXOffset;
document.getElementById("dyn_button").style.top = window.pageYOffset + window.innerHeight - document.getElementById("imgDyn_button").height - 6;
document.getElementById("divBody").style.height = window.pageYOffset + window.innerHeight - 2;
self.document.getElementById("divBody").style.width = (window.pageXOffset + window.innerWidth - 2);
}
function drillFilter(strID, objDrillFilter){
var strAction = "";
var strValue = "";
var iUBound = objDrillFilter.length - 1;
if (objDrillFilter.selectedIndex > 0) {
strValue = objDrillFilter.value;
}
if (iUBound == objDrillFilter.selectedIndex && strValue == "Remove") {
var strAction = "processDrillbar.jsp" + parent.parent.urlParamsNoBID(null,null,null,null,null,"") + "&sID=" + strID;
strAction += "&removeFilter=" + strID + "&sDrillAction=yes";
}
else {
var strAction = "processDrillFilters.jsp" + parent.parent.urlParamsNoBID(null,null,null,null,null,"") + "&sID=" + strID;
strAction += "&sFilter=" + convURL(strValue) + "&sDrillAction=yes";
}
parent.parent.Report.location = strAction;
}
function drillIcn()
{
var s = imgOffset(objParent._img+'report.gif',16,16,9*16,0)
document.write(s)
}
//Done For Reporting, modified for Drill
fltIcn = newIconWidget('addDrillFilter',objParent._img+'report.gif',addFilterCB,null,_drillbar_icon_tooltip,16,16,16*11,0)
divBody=newWidget('divBody')
drillLeftTitle=newWidget('drillLeftTitle')
function addFilterCB()
{
var pos = getPos(objParent.reportBorder.layer)
var posIcn= getPos(fltIcn.layer)
var icnHeight=fltIcn.getHeight();
var w = document.body.offsetWidth
if(objParent.showDrillFilterMenu)
objParent.showDrillFilterMenu(pos.x+w, pos.y+posIcn.y+icnHeight+2);
}
function resizeDrillbar()
{
var drillbarH=divBody.getHeight()
var drillbarW=winWidth()
var maxComboW=Math.max(0,drillbarW-drillLeftTitle.getWidth()-50)
var combos=document.getElementsByTagName("select")
if (combos)
{
var count = combos.length
for (var i=0;i<count;i++)
{
var combo=combos[i]
combo.style.width=""
var w=combo.offsetWidth
if (w>maxComboW)
combo.style.width=""+maxComboW+"px"
}
}
setTimeout("parent.resizeCB("+drillbarH+")",100)
}
function onInit()
{
fltIcn.init();
divBody.init();
drillLeftTitle.init();
resizeDrillbar();
blnLoaded = true;
if(objParent.filldrillFilterMenu)
objParent.filldrillFilterMenu()
}
arrParamName=new Array
function fctMouseOver(idx)
{
window.status=_drillbar_combo_tooltip+" "+arrParamName[idx];
return true;
}
</script>
</head>
<body style="overflow:hidden" marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" onLoad="onInit()" onResize="resizeDrillbar()" onselectstart="return false" ondragstart="return false" oncontextmenu="return false" >
<table border="0" cellspacing="0" cellpadding="0" height="100%" width="100%"><tr>
<td style="padding-left:4px;padding-top:4px;" width="20" class="dialogzone" valign='top' ><script language="javascript">drillIcn()</script></td>
<td id="drillLeftTitle" style="padding-left:4px;padding-right:4px;padding-top:6px;" class="dialogzone" valign='top' width="100"  ><nobr><script language="javascript">document.write(_drillbar_title)</script></nobr></td>
<td class="dialogzone" valign="top" align="left" width="100%">
<div id="divBody" class="dialogzone">
<form name="frmDrillbar" method="post" action="viewDrillbar.jsp" style="padding:0px;margin:0px">
    <table border="0" cellspacing="3" cellpadding="0" class="dialogzone">
<tr>
    <td>
        <%
if (null != objDrillBar) {
int nbEltDrillBar = objDrillBar.getCount();
for (int i=0; i<nbEltDrillBar; i++) {
String strFilterName = "FV" + Integer.toString(i+1);
objDrillBarObject = objDrillBar.getItem(i);
String strParamID = objDrillBarObject.getID();
String strParamName = objDrillBarObject.getName();
String strSelected = "";
%>
<script language="javascript">
arrParamName[<%=i%>]="<%=ViewerTools.escapeQuotes(strParamName)%>"
var s='<select name="<%=strFilterName%>" size="1" onChange="javascript:drillFilter(\'<%=strParamID%>\',this)" onMouseOver="javascript:return fctMouseOver(<%=i%>)" onMouseOut="window.status = \'\'" class="listinputs" title= "'+convStr(_drillbar_combo_tooltip+' <%=ViewerTools.escapeQuotes(strParamName)%>',true) +'">'
document.write(s);
</script>
<%
String strParamValue = request.getParameter(strFilterName);
if (null == strParamValue || strParamValue.equals("")) {
strParamValue = objDrillBarObject.getFilter();
}
int nbEltLOV = 0;
Lov objLOV = null;
Values objLOValues = null;
try {
    objLOV = objDrillBarObject.getLOV();
    objLOValues = objLOV.getAllValues();
    nbEltLOV = objLOValues.getCount();
}
catch(Exception e) {
}
if(nbEltLOV > 0) {
%>
<script language="javascript">document.write(opt("All","<%=ViewerTools.escapeQuotes(strParamName)%> (" + _drillbar_allValues + ")"));</script>
<%
for (int j=0; j<nbEltLOV; j++) {
String strOpValue = objLOValues.getValue(j);
strSelected = "";
if (null != strParamValue && null != strOpValue && strParamValue.equals(strOpValue)) {strSelected = " selected";}
%>
<option value="<%=ViewerTools.convQuotes(strOpValue)%>" <%=strSelected%>><%=ViewerTools.convStr(strOpValue)%></option>
<%
}
}
else {
%>    
    <script language="javascript">document.write(opt("None","<%=ViewerTools.escapeQuotes(strParamName)%> (" + _drillbar_noValue + ")"));</script>
    <%
}
%>
<script language="javascript">document.write(opt("Remove","(" + _drillbar_remove + ")"));</script>
</select>
<%
}
}
                %>
</td>
</tr>
</table>
</form>
</div>
</td>
<td class="dialogzone" valign='top' align='right' style="padding-top:1px;padding-right:4px" width="20">
</td>
</tr></table>
<script language="javascript">
<%
String[] arrHierarchy = null;
String[] arrDimension = null;
String[] arrDetail = null;
String[] arrFreeDimension = null;
String[] arrFreeDetail = null;
Vector vHierarchy = new Vector();
Vector vDimension = new Vector();
Vector vFreeDimension = new Vector();
Vector vFreeDetail = new Vector();
DrillHierarchies objDrillHierarchies = objDrillInfo.getDrillHierarchies();
if (null != objDrillHierarchies) {
int nbDrillHierarchies = objDrillHierarchies.getCount();
String[][] arrDHNames = new String[nbDrillHierarchies][2];
for (int j=0; j<nbDrillHierarchies; j++) {
    arrDHNames[j][0] = objDrillHierarchies.getItem(j).getName();
    arrDHNames[j][1] = "false";
    for (int k=0; k<j; k++) {
if (arrDHNames[j][0].equalsIgnoreCase(arrDHNames[k][0])) {
    arrDHNames[j][1] = "true";                               
    arrDHNames[k][1] = "true";
break;
}
}
}
for (int i=0; i<nbDrillHierarchies; i++) {
boolean blnDim = false;
DrillHierarchy objDrillHierarchy = objDrillHierarchies.getItem(i);
DrillDimensions objDrillDimensions = objDrillHierarchy.getDrillDimensions();
int nbDrillDimensions = objDrillDimensions.getCount();
if (nbDrillDimensions>0) {
for (int j=0; j<nbDrillDimensions; j++) {
DrillDimension objDrillDimension = objDrillDimensions.getItem(j);
boolean isInDrillBar = false;
for (int k=0; k<arrFilters.length; k++) {
if (arrFilters[k][1].equals(objDrillDimension.getID())) {
isInDrillBar = true;
break;
}
}
if(!isInDrillBar && objDrillDimension.isInScope()) {
arrDimension = new String[6];
arrDimension[0] = objDrillDimension.getName();          
arrDimension[1] = objDrillDimension.getID();            
arrDimension[2] = "false";                              
arrDimension[3] = objDrillHierarchy.getID();            
arrDimension[4] = Integer.toString(vHierarchy.size());  
vDimension.addElement(arrDimension);
blnDim = true;
}
}
}
if(blnDim) {
arrHierarchy = new String[2];
if (arrDHNames[i][1].equals("true")) {
arrHierarchy[0] = objDrillHierarchy.getName() + " (" + objDrillHierarchy.getDataProvider().getName() + ")";
}
else {
arrHierarchy[0] = objDrillHierarchy.getName();          
}
arrHierarchy[1] = objDrillHierarchy.getID();                
vHierarchy.addElement(arrHierarchy);
blnDim = false;
}
}
}
DrillDimensions objFreeDimensions = objDrillInfo.getFreeDrillDimensions();
int nbFreeDimensions = objFreeDimensions.getCount();
if (null != objFreeDimensions && nbFreeDimensions > 0) {
for (int i=0; i<nbFreeDimensions; i++) {
DrillDimension objFreeDrillDimension = objFreeDimensions.getItem(i);
boolean isInDrillBar = false;
for (int k=0; k<arrFilters.length; k++) {
if (arrFilters[k][1].equals(objFreeDrillDimension.getID())) {
isInDrillBar = true;
break;
}
}
if(!isInDrillBar && objFreeDrillDimension.isInScope()){
arrFreeDimension = new String[3];
arrFreeDimension[0] = objFreeDrillDimension.getName();      
arrFreeDimension[1] = objFreeDrillDimension.getID();        
arrFreeDimension[2] = "false";                              
vFreeDimension.addElement(arrFreeDimension);
}
}
}
DrillDetails objFreeDetails = objDrillInfo.getFreeDrillDetails();
int nbFreeDetails = objFreeDetails.getCount();
if (null != objFreeDetails && nbFreeDetails > 0) {
for (int i=0; i<nbFreeDetails; i++) {
DrillDetail objFreeDrillDetail = objFreeDetails.getItem(i);
boolean isInDrillBar = false;
for (int k=0; k<arrFilters.length; k++) {
if (arrFilters[k][1].equals(objFreeDrillDetail.getID())) {
isInDrillBar = true;
break;
}
}
if(!isInDrillBar) {
arrFreeDetail = new String[2];
arrFreeDetail[0] = objFreeDrillDetail.getName();            
arrFreeDetail[1] = objFreeDrillDetail.getID();              
vFreeDetail.addElement(arrFreeDetail);
}
}
}
StringBuffer sbOutput = new StringBuffer(1024);
sbOutput.append("parent.arrHierarchy = new Array();\n");
for (int i=0; i<vHierarchy.size(); i++) {
arrHierarchy = (String[])vHierarchy.elementAt(i);
sbOutput.append("parent.arrHierarchy[" + Integer.toString(i) + "] = new Array(2);\n");
sbOutput.append("parent.arrHierarchy[" + Integer.toString(i) + "][0] = \"" + ViewerTools.escapeQuotes(arrHierarchy[0]) + "\";\n");
sbOutput.append("parent.arrHierarchy[" + Integer.toString(i) + "][1] = \"" + arrHierarchy[1] + "\";\n");
}
sbOutput.append("parent.arrDimension = new Array();\n");
for (int i=0; i<vDimension.size(); i++) {
arrDimension = (String[])vDimension.elementAt(i);
sbOutput.append("parent.arrDimension[" + Integer.toString(i) + "] = new Array(5);\n");
sbOutput.append("parent.arrDimension[" + Integer.toString(i) + "][0] = \"" + ViewerTools.escapeQuotes(arrDimension[0]) + "\";\n");
sbOutput.append("parent.arrDimension[" + Integer.toString(i) + "][1] = \"" + arrDimension[1] + "\";\n");
sbOutput.append("parent.arrDimension[" + Integer.toString(i) + "][2] = " + arrDimension[2] + ";\n");
sbOutput.append("parent.arrDimension[" + Integer.toString(i) + "][3] = \"" + arrDimension[3] + "\";\n");
sbOutput.append("parent.arrDimension[" + Integer.toString(i) + "][4] = " + arrDimension[4] + ";\n");
}
sbOutput.append("parent.arrFreeDimension = new Array();\n");
for (int i=0; i<vFreeDimension.size(); i++) {
arrFreeDimension = (String[])vFreeDimension.elementAt(i);
sbOutput.append("parent.arrFreeDimension[" + Integer.toString(i) + "] = new Array(3);\n");
sbOutput.append("parent.arrFreeDimension[" + Integer.toString(i) + "][0] = \"" + ViewerTools.escapeQuotes(arrFreeDimension[0]) + "\";\n");
sbOutput.append("parent.arrFreeDimension[" + Integer.toString(i) + "][1] = \"" + arrFreeDimension[1] + "\";\n");
sbOutput.append("parent.arrFreeDimension[" + Integer.toString(i) + "][2] = " + arrFreeDimension[2] + ";\n");
}
sbOutput.append("parent.arrFreeDetail = new Array();\n");
for (int i=0; i<vFreeDetail.size(); i++) {
arrFreeDetail = (String[])vFreeDetail.elementAt(i);
sbOutput.append("parent.arrFreeDetail[" + Integer.toString(i) + "] = new Array(2);\n");
sbOutput.append("parent.arrFreeDetail[" + Integer.toString(i) + "][0] = \"" + ViewerTools.escapeQuotes(arrFreeDetail[0]) + "\";\n");
sbOutput.append("parent.arrFreeDetail[" + Integer.toString(i) + "][1] = \"" + arrFreeDetail[1] + "\";\n");
}
out.print(sbOutput.toString());
sbOutput = null;
arrHierarchy = null;
arrDimension = null;
arrDetail = null;
arrFreeDimension = null;
arrFreeDetail = null;
vHierarchy = null;
vDimension = null;
vFreeDimension = null;
vFreeDetail = null;
%>
</script>
</body>
<html>
<%
objDrillInfo = null;
objDrillBar = null;
objDrillHierarchies = null;
objFreeDimensions = null;
objFreeDetails = null;
objReport = null;
doc = null;
}
catch(Exception e) {
objUtils.displayErrorMsg(e, "_ERR_DRILL", true, out, session);
}
%>
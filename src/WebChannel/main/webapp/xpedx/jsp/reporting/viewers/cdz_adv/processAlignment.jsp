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
try
{
_logger.info("-->processAlignment.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry = " + strEntry );
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
String sBids = requestWrapper.getQueryParameter("bids", false);
    String[] arrBids = ViewerTools.split(sBids, ",");
_logger.info( "arrBids.length = " + arrBids.length );
if ( arrBids.length < 2 )
throw new Exception ("VIEWER:_ERR_ALIGNMENT_ERR01");
String sWidth = requestWrapper.getQueryParameter("sAlignW", false);
    String[] arrWidth = ViewerTools.split(sWidth, ",");
    String sHeight = requestWrapper.getQueryParameter("sAlignH", false);
    String[] arrHeight = ViewerTools.split(sHeight, ",");
ReportElementContainer reportStructure = doc.getStructure();
ReportContainer report = (ReportContainer) reportStructure.getChildAt(nReportIndex);
String sbdeleteAttach = requestWrapper.getQueryParameter("bdeleteAttach",false,"true");
boolean bdeleteAttach = Boolean.valueOf(sbdeleteAttach).booleanValue();
_logger.info("sbdeleteAttach="+sbdeleteAttach);
String sAlignment = requestWrapper.getQueryParameter("alignment", false);
_logger.info("sAlignment="+sAlignment);
if ( sAlignment.equals("center") || sAlignment.equals("right"))
{
if (arrWidth.length !=arrBids.length)
throw new Exception ("VIEWER:_ERR_ALIGNMENT_ERR01");
}
else
if ( sAlignment.equals("middle") || sAlignment.equals("bottom"))
{
if (arrHeight.length !=arrBids.length)
throw new Exception ("VIEWER:_ERR_ALIGNMENT_ERR01");
}
double rightMostInMM = 0;
double bottomMostInMM = 0;
double leftMostInMM = 0; 
double topMostInMM = 0;
String sMost;
if ( sAlignment.equals("left") || sAlignment.equals("center"))
{
sMost = requestWrapper.getQueryParameter("align_leftMost", true);
leftMostInMM=Double.parseDouble(sMost);
}
if ( sAlignment.equals("center") || sAlignment.equals("right"))
{
sMost = requestWrapper.getQueryParameter("align_rightMost", true);
rightMostInMM=Double.parseDouble(sMost);
}
if ( sAlignment.equals("top") || sAlignment.equals("middle") )
{
sMost = requestWrapper.getQueryParameter("align_topMost", true);
topMostInMM=Double.parseDouble(sMost);
}
if ( sAlignment.equals("middle") || sAlignment.equals("bottom"))
{
sMost = requestWrapper.getQueryParameter("align_bottomMost", true);
bottomMostInMM=Double.parseDouble(sMost);
}
_logger.info( "within report: leftMostInMM="+leftMostInMM+" - rightMostInMM="+rightMostInMM+" - topMostInMM="+topMostInMM+" - bottomMostInMM="+bottomMostInMM );
for ( int i = 0; i < arrBids.length; i++) 
{
ReportElement re = report.getReportElement(arrBids[i]);
Attachable at = (Attachable)re;
ReportElement attachHElt=at.getHAttachTo();
ReportElement attachVElt=at.getVAttachTo();
HAnchorType ht=HAnchorType.NONE;
VAnchorType vt=VAnchorType.NONE;
if ( (( attachHElt != null ) || ( attachVElt != null )) && !bdeleteAttach )
throw new Exception ("VIEWER:_ERR_ALIGNMENT_ERR02");
if (bdeleteAttach)
{
if (attachHElt!=null)
ht=at.getHorizontalAnchor(); 
if (attachVElt!=null)
vt=at.getVerticalAnchor(); 
if ( (attachHElt!=null) && (sAlignment.equals("left") || sAlignment.equals("center") || sAlignment.equals("right")))
at.setAttachTo(attachVElt, vt, null, HAnchorType.NONE);
if ( (attachVElt!=null) && (sAlignment.equals("top") || sAlignment.equals("middle") || sAlignment.equals("bottom")))
at.setAttachTo(null, VAnchorType.NONE, attachHElt, ht);
}
Position position = (Position)re;
Unit un = (Unit)re;
if ( sAlignment.equals("left") )
{
double val = leftMostInMM;
if ( un.getUnit() == UnitType.INCH )
val = mmToinch( val );
position.setX( val );
}
else if ( sAlignment.equals("right") )
{
double w=Double.parseDouble(arrWidth[i]);
double val = rightMostInMM - w;
if ( un.getUnit() == UnitType.INCH )
val = mmToinch( val );
position.setX( val );
}
else if ( sAlignment.equals("center") )
{
double w=Double.parseDouble(arrWidth[i]);
double val = (leftMostInMM + rightMostInMM)/2 - w/2; 
if ( un.getUnit() == UnitType.INCH )
val = mmToinch( val );
position.setX( val );
}
else if ( sAlignment.equals("top") )
{
double val = topMostInMM;
if ( un.getUnit() == UnitType.INCH )
val = mmToinch( val );
position.setY( val );
}
else if ( sAlignment.equals("middle") )
{
double h=Double.parseDouble(arrHeight[i]);
double val = (topMostInMM + bottomMostInMM)/2 -  h/2;
if ( un.getUnit() == UnitType.INCH )
val = mmToinch( val );
position.setY( val );
}
else if ( sAlignment.equals("bottom") )
{
double h=Double.parseDouble(arrHeight[i]);
double val = bottomMostInMM -  h ;
if ( un.getUnit() == UnitType.INCH )
val = mmToinch( val );
position.setY( val );
}
}
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
_logger.info("strEntry = " + strEntry );
requestWrapper.setQueryParameter("sEntry", strEntry);
_logger.info("<--processAlignment.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_ALIGNMENT_00", true, out, session);
}
%>
<%!
double mmToinch( double mmValue )
{
return mmValue/25.4;
}
%>
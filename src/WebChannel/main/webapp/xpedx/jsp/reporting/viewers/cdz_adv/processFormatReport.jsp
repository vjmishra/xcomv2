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
--><%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive)
{
objUtils.invalidSessionDialog(out);
return;
}
try
{
_logger.info("-->processFormatReport.jsp");
    String strEntry      = requestWrapper.getQueryParameter("sEntry", true);
   _logger.info("strEntry IN = " + strEntry );
    String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
    String sBid          = requestWrapper.getQueryParameter("sBid", false);
    String bgColor = requestWrapper.getQueryParameter("bgColor", false);
    String isSkin = requestWrapper.getQueryParameter("isSkin", false);
    String urlImg = requestWrapper.getQueryParameter("urlImg", false);
    String noneBG = requestWrapper.getQueryParameter("noneBG", false);
    String alignH = requestWrapper.getQueryParameter("alignH", false);
    String alignV = requestWrapper.getQueryParameter("alignV", false);
    String dispMode = requestWrapper.getQueryParameter("dispMode", false);
    String isImgUrl = requestWrapper.getQueryParameter("isImgUrl", false);
    String sHasEmbeddedImg = requestWrapper.getQueryParameter("hasEmbeddedImg", false, "false");
    String sBackGrdImgResourceID = requestWrapper.getQueryParameter("sBackGrdImgResourceID", false, "");
boolean fromFormatPainter = Boolean.valueOf(requestWrapper.getQueryParameter("fromFormatPainter", false, "false")).booleanValue();
    String iReport     = requestWrapper.getQueryParameter("iReport", true, "0");
    int nReportIndex = Integer.parseInt(iReport);
ReportEngine objReportEngine = reportEngines.getServiceFromStorageToken(strEntry);
    DocumentInstance doc = objReportEngine.getDocumentFromStorageToken(strEntry);
    CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, sBid);
    ReportContainer report = cellInfo.m_report;
if (fromFormatPainter) 
{
_logger.info("From FormatPainter");
Decoration deco = null;
if ((cellInfo.m_report_header != null) && (cellInfo.m_report.getPageHeader().getID().equals(sBid))) {         
 deco = (Decoration) report.getPageHeader(); 
 } else if ((cellInfo.m_report_footer != null) && (cellInfo.m_report.getPageFooter().getID().equals(sBid))) {
 deco = (Decoration) report.getPageFooter(); 
 } else if (cellInfo.m_report_body != null) {
 deco = (Decoration) report.getReportBody();  
 }    
    Attributes atts = deco.getAttributes();
Alignment align = report.getReportBody().getBackgroundAlignment();
boolean bHasEmbeddedImg = Boolean.valueOf(sHasEmbeddedImg).booleanValue();
applyAttributes( _logger, objReportEngine, atts, align, bgColor, noneBG, 
isSkin, isImgUrl, urlImg, dispMode, alignH, alignV, bHasEmbeddedImg, sBackGrdImgResourceID);
} 
else 
{
_logger.info("Not from FormatPainter");
    String name = requestWrapper.getQueryParameter("name", false);
   report.setName(name);
 String sPrefUnitIsInch = requestWrapper.getQueryParameter("unitIsInch", false);    
    boolean prefUnitIsInch =  Boolean.valueOf(sPrefUnitIsInch).booleanValue();
    UnitType prefUnit = prefUnitIsInch ? UnitType.INCH : UnitType.MILLIMETER;
    String showHeader = requestWrapper.getQueryParameter("showHeader", false);
    PageHeaderFooter header = report.getPageHeader();
if (!showHeader.equals(""))
{
String headerHeight = requestWrapper.getQueryParameter("headerHeight", false, "");
String headerBgColor = requestWrapper.getQueryParameter("headerBgColor", false);
String hdbgColor = requestWrapper.getQueryParameter("hdbgColor", false);
String hdisSkin = requestWrapper.getQueryParameter("hdisSkin", false);
String hdurlImg = requestWrapper.getQueryParameter("hdurlImg", false);
String hdnoneBG = requestWrapper.getQueryParameter("hdnoneBG", false);
String hdalignH = requestWrapper.getQueryParameter("hdalignH", false);
String hdalignV = requestWrapper.getQueryParameter("hdalignV", false);
String hddispMode = requestWrapper.getQueryParameter("hddispMode", false);
String hdisImgUrl = requestWrapper.getQueryParameter("hdisImgUrl", false);
String shdHasEmbeddedImg = requestWrapper.getQueryParameter("hdhasEmbeddedImg", false, "false");
_logger.info("shdHasEmbeddedImg = " + shdHasEmbeddedImg );   
    if (Boolean.valueOf(showHeader).booleanValue()) 
    {
        if (header == null) 
        header = report.createPageHeader();
        if (!headerHeight.equals("")) 
        {
            header.setUnit(prefUnit);
            header.setHeight(ViewerTools.toServerUnit(headerHeight, prefUnit));
        }
        Attributes atts = header.getAttributes();
Alignment hdalign = report.getPageHeader().getBackgroundAlignment();
        applyAttributes( _logger, objReportEngine, atts, hdalign, headerBgColor, hdnoneBG, 
hdisSkin, hdisImgUrl, hdurlImg, hddispMode, hdalignH, hdalignV, false, "");
    } 
    else 
    {
        if (header != null) 
        report.removeReportElement(header);
    }
}
String showFooter = requestWrapper.getQueryParameter("showFooter", false);
    String footerHeight = requestWrapper.getQueryParameter("footerHeight", false, "");
    String footerBgColor = requestWrapper.getQueryParameter("footerBgColor", false);
    PageHeaderFooter footer = report.getPageFooter();
if (!showFooter.equals(""))
{
String ftbgColor = requestWrapper.getQueryParameter("ftbgColor", false);
String ftisSkin = requestWrapper.getQueryParameter("ftisSkin", false);
String fturlImg = requestWrapper.getQueryParameter("fturlImg", false);
String ftnoneBG = requestWrapper.getQueryParameter("ftnoneBG", false);
String ftalignH = requestWrapper.getQueryParameter("ftalignH", false);
String ftalignV = requestWrapper.getQueryParameter("ftalignV", false);
String ftdispMode = requestWrapper.getQueryParameter("ftdispMode", false);
String ftisImgUrl = requestWrapper.getQueryParameter("ftisImgUrl", false);
String sftHasEmbeddedImg = requestWrapper.getQueryParameter("fthasEmbeddedImg", false, "false");
    if (Boolean.valueOf(showFooter).booleanValue()) 
    {
if (footer == null) 
footer = report.createPageFooter();
if (!footerHeight.equals("")) 
{
footer.setUnit(prefUnit);
footer.setHeight(ViewerTools.toServerUnit(footerHeight, prefUnit));
}
Attributes atts = footer.getAttributes();
Alignment ftalign = footer.getBackgroundAlignment();
applyAttributes( _logger, objReportEngine, atts, ftalign, footerBgColor, ftnoneBG, 
ftisSkin, ftisImgUrl, fturlImg, ftdispMode, ftalignH, ftalignV, false, "" );
} 
else 
{
if (footer != null) 
report.removeReportElement(footer);
}
}
    Attributes atts = report.getReportBody().getAttributes();
Alignment align = report.getReportBody().getBackgroundAlignment();
applyAttributes( _logger, objReportEngine, atts, align, bgColor, noneBG, 
isSkin, isImgUrl, urlImg, dispMode, alignH, alignV, false, "");
    String hyperLinkVisitedColor = requestWrapper.getQueryParameter("hyperLinkVisitedColor", false);
    String hyperLinkUnvisitedColor = requestWrapper.getQueryParameter("hyperLinkUnvisitedColor", false);
    if (!hyperLinkVisitedColor.equals("")) {
    report.setVisitedHyperlinkColor(ViewerTools.toColor(ViewerTools.split(hyperLinkVisitedColor, ",")));
    }
    if (!hyperLinkUnvisitedColor.equals("")) {
    report.setHyperlinkColor(ViewerTools.toColor(ViewerTools.split(hyperLinkUnvisitedColor, ",")));
    }
String verticalRecords = requestWrapper.getQueryParameter("vertRecords", false);
    String horizontalRecords = requestWrapper.getQueryParameter("horiRecords", false);
if (!verticalRecords.equals("")) {
report.getPageInfo().setVerticalRecords(Integer.parseInt(verticalRecords));
    }
    if (!horizontalRecords.equals("")) {
    report.getPageInfo().setHorizontalRecords(Integer.parseInt(horizontalRecords));
    }        
    String orientation = requestWrapper.getQueryParameter("orientation", false);
    String pageSize = requestWrapper.getQueryParameter("pageSize", false);
    if (!orientation.equals("")) {
    report.getPageInfo().setOrientation(Orientation.fromInt(Integer.parseInt(orientation)));
    }
    if (!pageSize.equals("")) {
    report.getPageInfo().setPaperSize(PaperSize.fromInt(Integer.parseInt(pageSize)));
    }
    String marginTop = requestWrapper.getQueryParameter("marginTop", false);
    String marginLeft = requestWrapper.getQueryParameter("marginLeft", false);
    String marginBottom = requestWrapper.getQueryParameter("marginBottom", false);
    String marginRight = requestWrapper.getQueryParameter("marginRight", false);
    Margins margins = report.getPageInfo().getMargins();
    if (!marginTop.equals("")) {
margins.setUnit(prefUnit); 
    margins.setTop(ViewerTools.toServerUnit(marginTop, prefUnit));
    }
    if (!marginLeft.equals("")) {
    margins.setLeft(ViewerTools.toServerUnit(marginLeft, prefUnit));
    }
    if (!marginBottom.equals("")) {
    margins.setBottom(ViewerTools.toServerUnit(marginBottom, prefUnit));
    }
    if (!marginRight.equals("")) {
    margins.setRight(ViewerTools.toServerUnit(marginRight, prefUnit));
    }
}
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
_logger.info("strEntry OUT = " + strEntry );
String strQueryString = requestWrapper.getQueryString();
int iPos = strQueryString.indexOf("&sBid");
if (iPos>=0)
  strQueryString = strQueryString.substring(0, iPos);
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
requestWrapper.setQueryString(strQueryString);
_logger.info("<--processFormatReport.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_FORMAT", true, out, session);
}
%>
<%!
void applyAttributes(DHTMLLogger _logger, ReportEngine objReportEngine, Attributes atts, Alignment align, String bgColor, String isNone, 
String isSkin, String isImgUrl, String imgurl, String dispMode, String alignH, String alignV, boolean bHasEmbeddedImg, String sBackGrdImgResourceID)
{
_logger.info("bgColor="+bgColor);
if (!bgColor.equals("")) 
atts.setBackground(ViewerTools.toColor(ViewerTools.split(bgColor, ",")));
boolean hasNoBG = Boolean.valueOf(isNone).booleanValue();
boolean hasSkin = Boolean.valueOf(isSkin).booleanValue();
boolean hasImgUrl = Boolean.valueOf(isImgUrl).booleanValue();
if (hasNoBG)
{
_logger.info("No bg.");
atts.setSkin(null);
atts.setBackgroundImageURL(null);
}
else if (hasSkin)
{
_logger.info("Skin + imgurl = " + imgurl);
Skin[] skinsAr = objReportEngine.getBackgroundSkins(SkinReportElementType.REPORT);
for (int i = 0; i < skinsAr.length; i++)
{
if (((Skin) skinsAr[i]).getName().equals(imgurl))
{
_logger.info("Applying skin " + imgurl);
atts.setSkin(skinsAr[i]);
atts.setBackground(null);                    
break;                
}
}
} 
else
{
_logger.info("hasImgUrl="+hasImgUrl);
if ( hasImgUrl )
{
atts.setBackground(null); 
       atts.setSkin(null);
_logger.info("imgurl=" + imgurl);
atts.setBackgroundImageURL(imgurl);
}
else
if (bHasEmbeddedImg)
{
atts.setBackground(null); 
       atts.setSkin(null);
atts.setBackgroundImageResource(sBackGrdImgResourceID);
_logger.info("EmbeddedImg resource ID=" + sBackGrdImgResourceID);
}
_logger.info("dispMode=" + dispMode);
if ( !dispMode.equals("") )
{
_logger.info("dispMode=" + ImageDisplayMode.fromInt(Integer.parseInt(dispMode)));
atts.setBackgroundImageDisplayMode(ImageDisplayMode.fromInt(Integer.parseInt(dispMode)));
}
_logger.info("alignH=" + alignH);
if (!alignH.equals("")) 
{
_logger.info("alignH=" + HAlignmentType.fromInt(Integer.parseInt(alignH)));
align.setHorizontal(HAlignmentType.fromInt(Integer.parseInt(alignH)));
}
_logger.info("alignV=" + alignV);
if (!alignV.equals("")) 
{
_logger.info("alignV=" + VAlignmentType.fromInt(Integer.parseInt(alignV)));
align.setVertical(VAlignmentType.fromInt(Integer.parseInt(alignV)));
}
}
}
%>
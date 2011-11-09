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
_logger.info("-->processFormatSection.jsp");
    String strEntry      = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
    String sBid         = requestWrapper.getQueryParameter("sBid", false);
    String sPrefUnitIsInch = requestWrapper.getQueryParameter("unitIsInch", false);
    String inMap = requestWrapper.getQueryParameter("inMap", false);
    String bgColor = requestWrapper.getQueryParameter("bgColor", false);
    String isSkin = requestWrapper.getQueryParameter("isSkin", false);
    String urlImg = requestWrapper.getQueryParameter("urlImg", false);
    String noneBG = requestWrapper.getQueryParameter("noneBG", false);
    String dispMode = requestWrapper.getQueryParameter("dispMode", false);
    String alignH = requestWrapper.getQueryParameter("alignH", false);
    String alignV = requestWrapper.getQueryParameter("alignV", false);
    String sHasEmbeddedImg = requestWrapper.getQueryParameter("hasEmbeddedImg", false, "false");
    String sBackGrdImgResourceID = requestWrapper.getQueryParameter("sBackGrdImgResourceID", false, "");
boolean fromFormatPainter = Boolean.valueOf(requestWrapper.getQueryParameter("fromFormatPainter", false, "false")).booleanValue();
    String iReport     = requestWrapper.getQueryParameter("iReport", true, "0");
    int nReportIndex = Integer.parseInt(iReport);
    ReportEngine objReportEngine = reportEngines.getServiceFromStorageToken(strEntry);
    DocumentInstance doc = objReportEngine.getDocumentFromStorageToken(strEntry);
    _logger.info("bid=" + sBid);
    ReportElementContainer rs              = doc.getStructure();
    ReportContainer        report          = (ReportContainer) rs.getChildAt(nReportIndex);
    SectionContainer       section         = (SectionContainer) report.getReportElement(sBid);
    boolean prefUnitIsInch =  Boolean.valueOf(sPrefUnitIsInch).booleanValue();
    UnitType prefUnit = prefUnitIsInch ? UnitType.INCH : UnitType.MILLIMETER;
    _logger.info("section=" + section);
    _logger.info("inMap=" + inMap);
    Attributes atts = section.getAttributes();
if (!bgColor.equals("")) 
{
atts.setBackground(ViewerTools.toColor(ViewerTools.split(bgColor, ",")));
} 
    if (!isSkin.equals("")) 
    {
       _logger.info("isSkin=" + isSkin);
       boolean hasSkin = Boolean.valueOf(isSkin).booleanValue();
       boolean hasNoBG = Boolean.valueOf(noneBG).booleanValue();
       boolean bHasEmbeddedImg = Boolean.valueOf(sHasEmbeddedImg).booleanValue();
       if (hasSkin)
       {
        _logger.info("Skin=" + urlImg);
        Skin[] skinsAr = objReportEngine.getBackgroundSkins(SkinReportElementType.SECTION);
        for (int i = 0; i < skinsAr.length; i++)
        {
            if (((Skin) skinsAr[i]).getName().equals(urlImg))
            {
                atts.setSkin(skinsAr[i]);
                atts.setBackground(null);                    
                break;
            }
        }
       } 
       else if (!hasNoBG) 
       {
if ( !bHasEmbeddedImg )
{
atts.setBackground(null); 
       atts.setSkin(null);
_logger.info("urlImg=" + urlImg);
atts.setBackgroundImageURL(urlImg);
            }
else
if (fromFormatPainter)
{
atts.setBackground(null); 
       atts.setSkin(null);
atts.setBackgroundImageResource(sBackGrdImgResourceID);
_logger.info("EmbeddedImg resource ID=" + sBackGrdImgResourceID);
}
            _logger.info("dispMode=" + ImageDisplayMode.fromInt(Integer.parseInt(dispMode)));
            atts.setBackgroundImageDisplayMode(ImageDisplayMode.fromInt(Integer.parseInt(dispMode)));
            Alignment align = section.getBackgroundAlignment();
            _logger.info("alignH=" + alignH);
            if (!alignH.equals("")) {
               _logger.info("alignH=" + HAlignmentType.fromInt(Integer.parseInt(alignH)));
               align.setHorizontal(HAlignmentType.fromInt(Integer.parseInt(alignH)));
            }
            _logger.info("alignV=" + alignV);
            if (!alignV.equals("")) {
               _logger.info("alignV=" + VAlignmentType.fromInt(Integer.parseInt(alignV)));
               align.setVertical(VAlignmentType.fromInt(Integer.parseInt(alignV)));
            }
       } 
       else 
       {
_logger.info("no bg.");
atts.setSkin(null);
atts.setBackgroundImageURL(null);         
       }
    }
if (!fromFormatPainter) 
{
    if (!inMap.equals("")) {
    section.setInIndex(Boolean.valueOf(inMap).booleanValue());
    }
    String startNewPage = requestWrapper.getQueryParameter("startNewPage", false);
    String avoidPageBreak = requestWrapper.getQueryParameter("avoidPageBreak", false);
    String posV = requestWrapper.getQueryParameter("posV", false);
    String attachToV = requestWrapper.getQueryParameter("attachToV", false);
    String attachV = requestWrapper.getQueryParameter("attachV", false);
    String condDisplay = requestWrapper.getQueryParameter("condDisplay", false);
    String condDisplayTxt = requestWrapper.getQueryParameter("condDisplayTxt", false);
    if (!startNewPage.equals("")) {
        section.startOnNewPage(Boolean.valueOf(startNewPage).booleanValue());
    }
    if (!avoidPageBreak.equals("")) {
        section.avoidPageBreak(Boolean.valueOf(avoidPageBreak).booleanValue());
    }
    if (!attachToV.equals("") && !attachV.equals("")) {
       _logger.info("attachToV=" +attachToV);
       _logger.info("attachV=" +attachV);
       ReportElement elem = report.getReportElement(attachToV); 
       _logger.info(elem);
       section.setAttachTo(elem, VAnchorType.fromInt(Integer.parseInt(attachV)), HAnchorType.NONE);
    }
    if (!posV.equals("")) {
       section.setUnit(prefUnit);
       section.setY(ViewerTools.toServerUnit(posV, prefUnit));
    }
}    
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strQueryString = requestWrapper.getQueryString();
int iPos = strQueryString.indexOf("&sBid");
if (iPos>=0)
  strQueryString = strQueryString.substring(0, iPos);
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
requestWrapper.setQueryString(strQueryString);
    _logger.info("<--processFormatSection.jsp");
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
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
--><%@include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive)
{
objUtils.invalidSessionDialog(out);
return;
}
String strEntry = null;
try {
_logger.info("--> processFormatPainter.jsp");
strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
    String sBid        = requestWrapper.getQueryParameter("sBid", false).trim();
String iReport     = requestWrapper.getQueryParameter("iReport", false, "0");
String reportIdxSrc= requestWrapper.getQueryParameter("reportIdxSrc", false);
String sTargetBid  = requestWrapper.getQueryParameter("sTargetBid", false).trim();
_logger.info( " sBid=" + sBid + ", sTargetBid=" + sTargetBid);
int nReportIndex = Integer.parseInt(iReport);
int nReportIdxSrc = nReportIndex;
if (reportIdxSrc != null) {
nReportIdxSrc = Integer.parseInt(reportIdxSrc);
}
_logger.info("strEntry = " + strEntry);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
CurrentCellInfo templateElem = new CurrentCellInfo(doc, nReportIdxSrc, sBid);
CurrentCellInfo toFormatElem = new CurrentCellInfo(doc, nReportIndex, sTargetBid);
HashMap formatProperties = null;
if (templateElem.m_cell != null) {
formatProperties = readCellFormat(templateElem.m_cell);
_logger.info("Read cell format:");
} 
else if (templateElem.m_tableCell != null) {
formatProperties = readTableCellFormat(templateElem.m_tableCell);
_logger.info("Read table cell format:");
} 
else if (templateElem.m_block != null) {
if (templateElem.m_block.getRepresentation().getType() instanceof TableType) {
formatProperties = readTableFormat(templateElem.m_block);
_logger.info("Read table format:");
} else {
formatProperties = readChartFormat(templateElem.m_block);
_logger.info("Read chart format:");
}
} 
else
if (templateElem.m_sectionContainer != null) 
{
formatProperties = readSectionFormat(templateElem.m_sectionContainer);
_logger.info("Read section format:");
}
else 
if (templateElem.m_report != null) {
Decoration deco = null;
 if ((templateElem.m_report_header != null) && (templateElem.m_report.getPageHeader().getID().equals(sBid))) {         
 deco = (Decoration) templateElem.m_report.getPageHeader();
 _logger.info("Read report header format:");
 } else if ((templateElem.m_report_footer != null) && (templateElem.m_report.getPageFooter().getID().equals(sBid))) {
 deco = (Decoration) templateElem.m_report.getPageFooter();
 _logger.info("Read report footer format:");
 } else if (templateElem.m_report_body != null) {
 deco = (Decoration) templateElem.m_report.getReportBody(); 
 _logger.info("Read report body format:");
 }
 formatProperties = readReportFormat(deco.getAttributes(), deco.getBackgroundAlignment());
}
_logger.info(formatProperties.toString());
int CELL = 0, BLOCK = 1, SECTION = 2, REPORT = 3;
int toFormatElemType = -1;
String [][] toFormatElemMatrix = null;
String redirectUrl = null;
if (toFormatElem.m_cell != null) {
toFormatElemType = CELL;
toFormatElemMatrix = getCellApplyMatrix();
redirectUrl = "processFormatCell.jsp";
requestWrapper.setQueryParameter("bids", sTargetBid);
} else if (toFormatElem.m_tableCell != null) {
toFormatElemType = CELL;
toFormatElemMatrix = getCellApplyMatrix(); 
redirectUrl = "processFormatCell.jsp";
requestWrapper.setQueryParameter("bids", sTargetBid);
} else if (toFormatElem.m_block != null) {
toFormatElemType = BLOCK;
toFormatElemMatrix = getBlockApplyMatrix(); 
if (toFormatElem.m_block.getRepresentation().getType() instanceof TableType) {
redirectUrl = "processFormatTable.jsp";
} else {
redirectUrl = "processFormatChart.jsp";
}
} 
else
if (toFormatElem.m_sectionContainer != null) {
toFormatElemType = SECTION;
toFormatElemMatrix = getSectionApplyMatrix();
redirectUrl = "processFormatSection.jsp";
}
else 
if (toFormatElem.m_report != null) {
toFormatElemType = REPORT;
toFormatElemMatrix = getReportApplyMatrix();
redirectUrl = "processFormatReport.jsp";
}
int len = toFormatElemMatrix.length;
StringBuffer params = new StringBuffer();
for (int i = 0; i < len; i++)
{
if (toFormatElemMatrix[i][toFormatElemType+1].equals("y")) {
String keyParam = toFormatElemMatrix[i][0];
String valParam = (String) formatProperties.get(keyParam);
if (valParam != null) {
requestWrapper.setQueryParameter(keyParam, valParam);
}
}
}
requestWrapper.setQueryParameter("sBid", sTargetBid);
requestWrapper.setQueryParameter("fromFormatPainter", "true");
_logger.info("Redirect to :" + redirectUrl);
_logger.info("<-- processFormatPainter.jsp");
%>
<jsp:forward page="<%=redirectUrl%>"/>
<%
} catch(Exception e) {
objUtils.displayErrorMsg(e, "_ERR_COPY_PASTE_ELEMENT", true, out, session);
} 
%>
<%!
HashMap readCellFormat(Cell cell)
{
HashMap formatMap = new HashMap();
    Font font = ((Decoration) cell).getFont();
    formatMap.put("fntName", font.getName());
    formatMap.put("fntSize",  String.valueOf(font.getSize()));
    formatMap.put("bold", String.valueOf((((font.getStyle() & StyleType.BOLD)==StyleType.BOLD))));
    formatMap.put("italic", String.valueOf((((font.getStyle() & StyleType.ITALIC)==StyleType.ITALIC))));
    formatMap.put("underline", String.valueOf((((font.getStyle() & StyleType.UNDERLINE)==StyleType.UNDERLINE))));
    formatMap.put("strike", String.valueOf((((font.getStyle() & StyleType.STRIKETHROUGH)==StyleType.STRIKETHROUGH))));
    Attributes atts = ((Decoration) cell).getAttributes();
    formatMap.put("fgColor", getColorString(atts.getForeground()));
    boolean hasSkin = (atts.getSkin() != null);            
    String urlImg = hasSkin ? (atts.getSkin().getName()) : ((atts.getBackgroundImageURL() == null)? "" : atts.getBackgroundImageURL());
String sBackGrdImgResourceID="";
if (!hasSkin && urlImg.equals(""))
{
sBackGrdImgResourceID=atts.getBackgroundImageResource();
}
    boolean hasEmbedImg=((sBackGrdImgResourceID!=null) && !sBackGrdImgResourceID.equals(""));
    String noneBG = String.valueOf(urlImg.equals("") && !hasEmbedImg);
    formatMap.put("hasEmbeddedImg", String.valueOf(hasEmbedImg));
    if (hasEmbedImg) 
formatMap.put("sBackGrdImgResourceID", sBackGrdImgResourceID); 
formatMap.put("isSkin", String.valueOf(hasSkin)); 
formatMap.put("noneBG", noneBG); 
    formatMap.put("urlImg", urlImg);      
    formatMap.put("bgColor", getColorString(atts.getBackground()));
    formatMap.put("dispMode", String.valueOf(atts.getBackgroundImageDisplayMode().value()));    
    Alignment align = ((Decoration) cell).getBackgroundAlignment();
formatMap.put("alignV", String.valueOf(align.getVertical().value()));
formatMap.put("alignH", String.valueOf(align.getHorizontal().value()));        
    Alignment txtAlign = ((Decoration) cell).getAlignment();    
    formatMap.put("wrapText", String.valueOf(txtAlign.getWrapText()));
    HAlignmentType hAlign = txtAlign.getHorizontal();
    VAlignmentType vAlign = txtAlign.getVertical();
String sHAlign;
    if (hAlign == HAlignmentType.LEFT) {
        sHAlign = "0";
    } else if (hAlign == HAlignmentType.CENTER) {
        sHAlign = "1";
    } else {
        sHAlign = "2";
    }
    formatMap.put("txtAlignH", sHAlign);    
    String sVAlign;
    if (vAlign == VAlignmentType.TOP) {
        sVAlign = "0";
    } else if (vAlign == VAlignmentType.CENTER) {
        sVAlign = "1";
    }else {
        sVAlign = "2";
    }       
    formatMap.put("txtAlignV", sVAlign);
    Border border = atts.getBorder();
    if (border instanceof SimpleBorder) {       
SimpleBorder sb = (SimpleBorder) border;
String size = String.valueOf(sb.getSize().value());
String color = getColorString(sb.getColor());
formatMap.put("topBorderSize", size);
formatMap.put("topBorderColor", color);
formatMap.put("bottomBorderSize", size);
formatMap.put("bottomBorderColor", color);
formatMap.put("leftBorderSize", size);
formatMap.put("leftBorderColor", color);
formatMap.put("rightBorderSize", size);
formatMap.put("rightBorderColor", color);
    } else if (border instanceof ComplexBorder) {
   ComplexBorder cb = (ComplexBorder) border;
   SimpleBorder sbTop = cb.getTop();
    formatMap.put("topBorderSize", String.valueOf(sbTop.getSize().value()));
    formatMap.put("topBorderColor", getColorString(sbTop.getColor()));
    SimpleBorder sbBottom = cb.getBottom();
    formatMap.put("bottomBorderSize", String.valueOf(sbBottom.getSize().value()));
    formatMap.put("bottomBorderColor", getColorString(sbBottom.getColor()));
    SimpleBorder sbLeft = cb.getLeft();
    formatMap.put("leftBorderSize", String.valueOf(sbLeft.getSize().value()));
    formatMap.put("leftBorderColor", getColorString(sbLeft.getColor()));
    SimpleBorder sbRight = cb.getRight();
    formatMap.put("rightBorderSize", String.valueOf(sbRight.getSize().value()));
    formatMap.put("rightBorderColor", getColorString(sbRight.getColor()));
}
return formatMap;
}
HashMap readTableCellFormat(TableCell cell)
{
HashMap formatMap = new HashMap();
boolean unitIsInch = (((Unit) cell).getUnit() == UnitType.INCH);
    formatMap.put("unitIsInch",  String.valueOf(unitIsInch));
    if (unitIsInch) {
    formatMap.put("padLeft", String.valueOf(cell.getHorizontalPadding()));
    formatMap.put("padTop",  String.valueOf(cell.getVerticalPadding()));
    } else {
    formatMap.put("padLeft", String.valueOf(cell.getHorizontalPadding()/10.0));
    formatMap.put("padTop",  String.valueOf(cell.getVerticalPadding()/10.0));    
    }
    Font font = ((Decoration) cell).getFont();
    formatMap.put("fntName", font.getName());
    formatMap.put("fntSize",  String.valueOf(font.getSize()));
    formatMap.put("bold", String.valueOf((((font.getStyle() & StyleType.BOLD)==StyleType.BOLD))));
    formatMap.put("italic", String.valueOf((((font.getStyle() & StyleType.ITALIC)==StyleType.ITALIC))));
    formatMap.put("underline", String.valueOf((((font.getStyle() & StyleType.UNDERLINE)==StyleType.UNDERLINE))));
    formatMap.put("strike", String.valueOf((((font.getStyle() & StyleType.STRIKETHROUGH)==StyleType.STRIKETHROUGH))));
    Attributes atts = ((Decoration) cell).getAttributes();
    formatMap.put("fgColor", getColorString(atts.getForeground()));
    boolean hasSkin = (atts.getSkin() != null);            
    String urlImg = hasSkin ? (atts.getSkin().getName()) : ((atts.getBackgroundImageURL() == null)? "" : atts.getBackgroundImageURL());
    String sBackGrdImgResourceID="";
if (!hasSkin && urlImg.equals(""))
{
sBackGrdImgResourceID=atts.getBackgroundImageResource();
}
    boolean hasEmbedImg=((sBackGrdImgResourceID!=null) && !sBackGrdImgResourceID.equals(""));
    String noneBG = String.valueOf(urlImg.equals("") && !hasEmbedImg);
    formatMap.put("hasEmbeddedImg", String.valueOf(hasEmbedImg));
    if (hasEmbedImg) 
formatMap.put("sBackGrdImgResourceID", sBackGrdImgResourceID); 
formatMap.put("isSkin", String.valueOf(hasSkin)); 
formatMap.put("noneBG", noneBG); 
    formatMap.put("urlImg", urlImg);      
    formatMap.put("bgColor", getColorString(atts.getBackground()));
    formatMap.put("dispMode", String.valueOf(hasSkin ? atts.getSkin().getDisplayMode().value() : atts.getBackgroundImageDisplayMode().value()));
    Alignment align = ((Decoration) cell).getBackgroundAlignment();
formatMap.put("alignV", String.valueOf(align.getVertical().value()));
formatMap.put("alignH", String.valueOf(align.getHorizontal().value()));   
    Alignment txtAlign = ((Decoration) cell).getAlignment();
formatMap.put("wrapText", String.valueOf(txtAlign.getWrapText()));
    HAlignmentType hAlign = txtAlign.getHorizontal();
    VAlignmentType vAlign = txtAlign.getVertical();
String sHAlign;
    if (hAlign == HAlignmentType.LEFT) {
        sHAlign = "0";
    } else if (hAlign == HAlignmentType.CENTER) {
        sHAlign = "1";
    } else {
        sHAlign = "2";
    }
    formatMap.put("txtAlignH", sHAlign);    
    String sVAlign;
    if (vAlign == VAlignmentType.TOP) {
        sVAlign = "0";
    } else if (vAlign == VAlignmentType.CENTER) {
        sVAlign = "1";
    }else {
        sVAlign = "2";
    }       
    formatMap.put("txtAlignV", sVAlign);
    Border border = atts.getBorder();
    if (border instanceof SimpleBorder) {       
SimpleBorder sb = (SimpleBorder) border;
String size = String.valueOf(sb.getSize().value());
String color = getColorString(sb.getColor());
formatMap.put("topBorderSize", size);
formatMap.put("topBorderColor", color);
formatMap.put("bottomBorderSize", size);
formatMap.put("bottomBorderColor", color);
formatMap.put("leftBorderSize", size);
formatMap.put("leftBorderColor", color);
formatMap.put("rightBorderSize", size);
formatMap.put("rightBorderColor", color);
    } else if (border instanceof ComplexBorder) {
   ComplexBorder cb = (ComplexBorder) border;
   SimpleBorder sbTop = cb.getTop();
    formatMap.put("topBorderSize", String.valueOf(sbTop.getSize().value()));
    formatMap.put("topBorderColor", getColorString(sbTop.getColor()));
    SimpleBorder sbBottom = cb.getBottom();
    formatMap.put("bottomBorderSize", String.valueOf(sbBottom.getSize().value()));
    formatMap.put("bottomBorderColor", getColorString(sbBottom.getColor()));
    SimpleBorder sbLeft = cb.getLeft();
    formatMap.put("leftBorderSize", String.valueOf(sbLeft.getSize().value()));
    formatMap.put("leftBorderColor", getColorString(sbLeft.getColor()));
    SimpleBorder sbRight = cb.getRight();
    formatMap.put("rightBorderSize", String.valueOf(sbRight.getSize().value()));
    formatMap.put("rightBorderColor", getColorString(sbRight.getColor()));
}
return formatMap;
}
HashMap readTableFormat(ReportBlock block)
{
HashMap formatMap = new HashMap();
 Decoration deco = ((TableFormBase) block.getRepresentation()).getBodyTableDecoration();
    Attributes atts = deco.getAttributes();
    boolean hasSkin = (atts.getSkin() != null);    
    String urlImg = hasSkin ? (atts.getSkin().getName()) : ((atts.getBackgroundImageURL() == null)? "" : atts.getBackgroundImageURL());
    String sBackGrdImgResourceID="";
if (!hasSkin && urlImg.equals(""))
{
sBackGrdImgResourceID=atts.getBackgroundImageResource();
}
    boolean hasEmbedImg=((sBackGrdImgResourceID!=null) && !sBackGrdImgResourceID.equals(""));
    String noneBG = String.valueOf(urlImg.equals("") && !hasEmbedImg);
    formatMap.put("hasEmbeddedImg", String.valueOf(hasEmbedImg));
    if (hasEmbedImg) 
formatMap.put("sBackGrdImgResourceID", sBackGrdImgResourceID); 
formatMap.put("isSkin", String.valueOf(hasSkin)); 
formatMap.put("noneBG", noneBG); 
    formatMap.put("urlImg", urlImg);      
    formatMap.put("bgColor", getColorString(atts.getBackground()));
formatMap.put("dispMode", String.valueOf(hasSkin ? atts.getSkin().getDisplayMode().value() : atts.getBackgroundImageDisplayMode().value()));
    Alignment align = deco.getBackgroundAlignment();
formatMap.put("alignV", String.valueOf(align.getVertical().value()));
formatMap.put("alignH", String.valueOf(align.getHorizontal().value()));
    Border border = atts.getBorder();
    if (border instanceof SimpleBorder) {       
SimpleBorder sb = (SimpleBorder) border;
String size = String.valueOf(sb.getSize().value());
String color = getColorString(sb.getColor());
formatMap.put("topBorderSize", size);
formatMap.put("topBorderColor", color);
formatMap.put("bottomBorderSize", size);
formatMap.put("bottomBorderColor", color);
formatMap.put("leftBorderSize", size);
formatMap.put("leftBorderColor", color);
formatMap.put("rightBorderSize", size);
formatMap.put("rightBorderColor", color);
    } else if (border instanceof ComplexBorder) {
   ComplexBorder cb = (ComplexBorder) border;
   SimpleBorder sbTop = cb.getTop();
    formatMap.put("topBorderSize", String.valueOf(sbTop.getSize().value()));
    formatMap.put("topBorderColor", getColorString(sbTop.getColor()));
    SimpleBorder sbBottom = cb.getBottom();
    formatMap.put("bottomBorderSize", String.valueOf(sbBottom.getSize().value()));
    formatMap.put("bottomBorderColor", getColorString(sbBottom.getColor()));
    SimpleBorder sbLeft = cb.getLeft();
    formatMap.put("leftBorderSize", String.valueOf(sbLeft.getSize().value()));
    formatMap.put("leftBorderColor", getColorString(sbLeft.getColor()));
    SimpleBorder sbRight = cb.getRight();
    formatMap.put("rightBorderSize", String.valueOf(sbRight.getSize().value()));
    formatMap.put("rightBorderColor", getColorString(sbRight.getColor()));
}
formatMap.put("freqVal", String.valueOf(((TableFormBase) block.getRepresentation()).getAlternateColorFrequency()));
formatMap.put("freqColor", getColorString(((TableFormBase) block.getRepresentation()).getAlternateColor()));
boolean unitIsInch = (UnitType.INCH  == ((TableFormBase) block.getRepresentation()).getUnit()) ;
    formatMap.put("unitIsInch", String.valueOf(unitIsInch));
    if (unitIsInch) {
formatMap.put("HSpacing", String.valueOf(((TableFormBase) block.getRepresentation()).getCellSpacing()));
    } else {
formatMap.put("HSpacing", String.valueOf((((TableFormBase) block.getRepresentation()).getCellSpacing()) / 10.0));
    }
return formatMap;
}
HashMap readChartFormat(ReportBlock block)
{
HashMap formatMap = new HashMap();
 Decoration deco = (Decoration) block.getRepresentation();
    Attributes atts = deco.getAttributes();
    boolean hasSkin = (atts.getSkin() != null);    
    String urlImg = hasSkin ? (atts.getSkin().getName()) : ((atts.getBackgroundImageURL() == null)? "" : atts.getBackgroundImageURL());
    String sBackGrdImgResourceID="";
if (!hasSkin && urlImg.equals(""))
{
sBackGrdImgResourceID=atts.getBackgroundImageResource();
}
    boolean hasEmbedImg=((sBackGrdImgResourceID!=null) && !sBackGrdImgResourceID.equals(""));
    String noneBG = String.valueOf(urlImg.equals("") && !hasEmbedImg);
    formatMap.put("hasEmbeddedImg", String.valueOf(hasEmbedImg));
    if (hasEmbedImg) 
formatMap.put("sBackGrdImgResourceID", sBackGrdImgResourceID); 
formatMap.put("isSkin", String.valueOf(hasSkin)); 
formatMap.put("noneBG", noneBG); 
    formatMap.put("urlImg", urlImg);      
    formatMap.put("bgColor", getColorString(atts.getBackground()));
formatMap.put("dispMode", String.valueOf(hasSkin ? atts.getSkin().getDisplayMode().value() : atts.getBackgroundImageDisplayMode().value()));
    Alignment align = deco.getBackgroundAlignment();
formatMap.put("alignV", String.valueOf(align.getVertical().value()));
formatMap.put("alignH", String.valueOf(align.getHorizontal().value()));
    Border border = atts.getBorder();
    if (border instanceof SimpleBorder) {       
SimpleBorder sb = (SimpleBorder) border;
String size = String.valueOf(sb.getSize().value());
String color = getColorString(sb.getColor());
formatMap.put("topBorderSize", size);
formatMap.put("topBorderColor", color);
formatMap.put("bottomBorderSize", size);
formatMap.put("bottomBorderColor", color);
formatMap.put("leftBorderSize", size);
formatMap.put("leftBorderColor", color);
formatMap.put("rightBorderSize", size);
formatMap.put("rightBorderColor", color);
    } else if (border instanceof ComplexBorder) {
   ComplexBorder cb = (ComplexBorder) border;
   SimpleBorder sbTop = cb.getTop();
    formatMap.put("topBorderSize", String.valueOf(sbTop.getSize().value()));
    formatMap.put("topBorderColor", getColorString(sbTop.getColor()));
    SimpleBorder sbBottom = cb.getBottom();
    formatMap.put("bottomBorderSize", String.valueOf(sbBottom.getSize().value()));
    formatMap.put("bottomBorderColor", getColorString(sbBottom.getColor()));
    SimpleBorder sbLeft = cb.getLeft();
    formatMap.put("leftBorderSize", String.valueOf(sbLeft.getSize().value()));
    formatMap.put("leftBorderColor", getColorString(sbLeft.getColor()));
    SimpleBorder sbRight = cb.getRight();
    formatMap.put("rightBorderSize", String.valueOf(sbRight.getSize().value()));
    formatMap.put("rightBorderColor", getColorString(sbRight.getColor()));
}
return formatMap;
}
HashMap readSectionFormat(SectionContainer section)
{
HashMap formatMap = new HashMap();
    Attributes atts = ((Decoration) section).getAttributes();
    boolean hasSkin = (atts.getSkin() != null);            
    String urlImg = hasSkin ? (atts.getSkin().getName()) : ((atts.getBackgroundImageURL() == null)? "" : atts.getBackgroundImageURL());
    String sBackGrdImgResourceID="";
if (!hasSkin && urlImg.equals(""))
{
sBackGrdImgResourceID=atts.getBackgroundImageResource();
}
    boolean hasEmbedImg=((sBackGrdImgResourceID!=null) && !sBackGrdImgResourceID.equals(""));
    String noneBG = String.valueOf(urlImg.equals("") && !hasEmbedImg);
    formatMap.put("hasEmbeddedImg", String.valueOf(hasEmbedImg));
    if (hasEmbedImg) 
formatMap.put("sBackGrdImgResourceID", sBackGrdImgResourceID); 
formatMap.put("isSkin", String.valueOf(hasSkin)); 
formatMap.put("noneBG", noneBG); 
    formatMap.put("urlImg", urlImg);      
    formatMap.put("bgColor", getColorString(atts.getBackground()));
    formatMap.put("dispMode", String.valueOf(hasSkin ? atts.getSkin().getDisplayMode().value() : atts.getBackgroundImageDisplayMode().value()));
    Alignment align = section.getBackgroundAlignment();
formatMap.put("alignV", String.valueOf(align.getVertical().value()));
formatMap.put("alignH", String.valueOf(align.getHorizontal().value()));
return formatMap;
}
HashMap readReportFormat(Attributes atts, Alignment align)
{
HashMap formatMap = new HashMap();
    boolean hasSkin = (atts.getSkin() != null);    
    String urlImg = hasSkin ? (atts.getSkin().getName()) : ((atts.getBackgroundImageURL() == null)? "" : atts.getBackgroundImageURL());
    String sBackGrdImgResourceID="";
if (!hasSkin && urlImg.equals(""))
{
sBackGrdImgResourceID=atts.getBackgroundImageResource();
}
    boolean hasEmbedImg=((sBackGrdImgResourceID!=null) && !sBackGrdImgResourceID.equals(""));
    String noneBG = String.valueOf(urlImg.equals("") && !hasEmbedImg);
    formatMap.put("hasEmbeddedImg", String.valueOf(hasEmbedImg));
    if (hasEmbedImg) 
formatMap.put("sBackGrdImgResourceID", sBackGrdImgResourceID); 
formatMap.put("isSkin", String.valueOf(hasSkin)); 
formatMap.put("noneBG", noneBG); 
    formatMap.put("urlImg", urlImg);      
    formatMap.put("bgColor", getColorString(atts.getBackground()));
    formatMap.put("dispMode", String.valueOf(hasSkin ? atts.getSkin().getDisplayMode().value() : atts.getBackgroundImageDisplayMode().value()));
formatMap.put("alignV", String.valueOf(align.getVertical().value()));
formatMap.put("alignH", String.valueOf(align.getHorizontal().value()));
return formatMap;
}
final String[][] getCellApplyMatrix() 
{
return new String[][] {
{"padTop", "y", "n", "n", "n"},
{"padLeft", "y", "n", "n", "n"},
{"unitIsInch", "y", "n", "n", "n"},
{"fntName", "y", "n", "n", "n"},
{"fntSize", "y", "n", "n", "n"},
{"bold", "y", "n", "n", "n"},
{"italic", "y", "n", "n", "n"},
{"underline", "y", "n", "n", "n"},
{"strike", "y", "n", "n", "n"},
{"fgColor", "y", "n", "n", "n"},
{"alignV", "y", "n", "n", "n"},
{"alignH", "y", "n", "n", "n"},
{"wrapText", "y", "n", "n", "n"},
{"bgColor", "y", "y", "y", "y"},
{"urlImg", "y", "y", "y", "y"},
{"isSkin", "y", "y", "y", "y"},
{"noneBG", "y", "y", "y", "y"},
{"dispMode", "y", "y", "y", "y"},
{"alignH", "y", "y", "y", "y"},
{"alignV", "y", "y", "y", "y"},
{"txtAlignH", "y", "n", "n", "n"},
{"txtAlignV", "y", "n", "n", "n"},
{"topBorderSize", "y", "y", "n", "n"},
{"topBorderColor", "y", "y", "n", "n"},
{"bottomBorderSize", "y", "y", "n", "n"},
{"bottomBorderColor", "y", "y", "n", "n"},
{"leftBorderSize", "y", "y", "n", "n"},
{"leftBorderColor", "y", "y", "n", "n"},
{"rightBorderSize", "y", "y", "n", "n"},
{"rightBorderColor", "y", "y", "n", "n"},
{"sBackGrdImgResourceID", "y", "y", "y", "y"},
{"hasEmbeddedImg", "y", "y", "y", "y"}};
}
final String[][] getBlockApplyMatrix() 
{
return new String[][] {
{"HSpacing", "n", "y", "n", "n"},
{"unitIsInch", "n", "y", "n", "n"},
{"bgColor", "y", "y", "y", "y"},
{"urlImg", "y", "y", "y", "y"},
{"isSkin", "y", "y", "y", "y"},
{"noneBG", "y", "y", "y", "y"},
{"dispMode", "y", "y", "y", "y"},
{"alignH", "y", "y", "y", "y"},
{"alignV", "y", "y", "y", "y"},
{"topBorderSize", "y", "y", "n", "n"},
{"topBorderColor", "y", "y", "n", "n"},
{"bottomBorderSize", "y", "y", "n", "n"},
{"bottomBorderColor", "y", "y", "n", "n"},
{"leftBorderSize", "y", "y", "n", "n"},
{"leftBorderColor", "y", "y", "n", "n"},
{"rightBorderSize", "y", "y", "n", "n"},
{"rightBorderColor", "y", "y", "n", "n"},
{"freqVal", "n", "y", "n", "n"},
{"freqColor", "n", "y", "n", "n"},
{"sBackGrdImgResourceID", "y", "y", "y", "y"},
{"hasEmbeddedImg", "y", "y", "y", "y"}};
}
final String[][] getSectionApplyMatrix() 
{
return new String[][] {
{"bgColor", "y", "y", "y", "y"},
{"urlImg", "y", "y", "y", "y"},
{"isSkin", "y", "y", "y", "y"},
{"noneBG", "y", "y", "y", "y"},
{"dispMode", "y", "y", "y", "y"},
{"alignH", "y", "y", "y", "y"},
{"alignV", "y", "y", "y", "y"},
{"sBackGrdImgResourceID", "y", "y", "y", "y"},
{"hasEmbeddedImg", "y", "y", "y", "y"}};
}
final String[][] getReportApplyMatrix() 
{
return new String[][] {
{"bgColor", "y", "y", "y", "y"},
{"urlImg", "y", "y", "y", "y"},
{"isSkin", "y", "y", "y", "y"},
{"noneBG", "y", "y", "y", "y"},
{"dispMode", "y", "y", "y", "y"},
{"alignH", "y", "y", "y", "y"},
{"alignV", "y", "y", "y", "y"},
{"sBackGrdImgResourceID", "y", "y", "y", "y"},
{"hasEmbeddedImg", "y", "y", "y", "y"}};
}
String getColorString(java.awt.Color color)
{
if (color != null) {
            return "" + color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    } else {
            return "-1,-1,-1";
    }
}
%>
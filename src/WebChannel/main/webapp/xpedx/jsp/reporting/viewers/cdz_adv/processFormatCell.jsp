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
_logger.info("-->processFormatCell.jsp");
    String strEntry      = requestWrapper.getQueryParameter("sEntry", true);
    _logger.info("strEntry IN = " + strEntry );
    String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
    String sBids         = requestWrapper.getQueryParameter("bids", false);
    String[] bids        = ViewerTools.split(sBids, ",");
    boolean oneSelect = (bids.length == 1)? true : false;
    String sPrefUnitIsInch = requestWrapper.getQueryParameter("unitIsInch", false, "");
    String nameCell = requestWrapper.getQueryParameter("nameCell", false);
    String showWhenEmpty = requestWrapper.getQueryParameter("showWhenEmpty",false, "");
    String wrapText = requestWrapper.getQueryParameter("wrapText",false, "");
    String widthCheck = requestWrapper.getQueryParameter("widthCheck",false, "");
    String widthField = requestWrapper.getQueryParameter("widthField", false, "");
    String heightCheck = requestWrapper.getQueryParameter("heightCheck",false, "");
    String heightField = requestWrapper.getQueryParameter("heightField", false, "");
    String contentAs = requestWrapper.getQueryParameter("contentAs", false, "");
    String repeat = requestWrapper.getQueryParameter("repeat",  false, "");
    String avoidPageBreak = requestWrapper.getQueryParameter("avoidPageBreak",  false, "");
    String posH = requestWrapper.getQueryParameter("posH", false, "");
    String posV = requestWrapper.getQueryParameter("posV", false, "");
    String attachToH = requestWrapper.getQueryParameter("attachToH",  false, "");
    String attachH = requestWrapper.getQueryParameter("attachH", false, "");
    String attachToV = requestWrapper.getQueryParameter("attachToV", false, "");
    String attachV = requestWrapper.getQueryParameter("attachV", false, "");
    String condDisplay = requestWrapper.getQueryParameter("condDisplay", false, "");
    String condDisplayTxt = requestWrapper.getQueryParameter("condDisplayTxt", false, "");
    String formatNb = requestWrapper.getQueryParameter("formatNb", false, "");
    String txtAlignH = requestWrapper.getQueryParameter("txtAlignH", false, "");
    String txtAlignV = requestWrapper.getQueryParameter("txtAlignV", false, "");
    String padTop = requestWrapper.getQueryParameter("padTop", false, "");
    String padLeft = requestWrapper.getQueryParameter("padLeft", false, "");
    String fntName = requestWrapper.getQueryParameter("fntName", false, "");
    String fntSize = requestWrapper.getQueryParameter("fntSize", false, "");
    String bold = requestWrapper.getQueryParameter("bold", false, "");
    String italic = requestWrapper.getQueryParameter("italic", false, "");
    String underline = requestWrapper.getQueryParameter("underline", false, "");
    String strike = requestWrapper.getQueryParameter("strike", false, "");
    String fgColor = requestWrapper.getQueryParameter("fgColor", false, "");
    String bgColor = requestWrapper.getQueryParameter("bgColor", false, "");
    String isSkin = requestWrapper.getQueryParameter("isSkin", false);
    String urlImg = requestWrapper.getQueryParameter("urlImg", false);
    String noneBG = requestWrapper.getQueryParameter("noneBG", false);
    String dispMode = requestWrapper.getQueryParameter("dispMode", false);
    String alignH = requestWrapper.getQueryParameter("alignH", false);
    String alignV = requestWrapper.getQueryParameter("alignV", false);
String sHasEmbeddedImg = requestWrapper.getQueryParameter("hasEmbeddedImg", false, "false");
String sBackGrdImgResourceID = requestWrapper.getQueryParameter("sBackGrdImgResourceID", false, "");
    String borderSize = requestWrapper.getQueryParameter("borderSize",  false, "");
    String borderColor = requestWrapper.getQueryParameter("borderColor", false, "");
    String topBorderSize = requestWrapper.getQueryParameter("topBorderSize",  false, "");
    String topBorderColor = requestWrapper.getQueryParameter("topBorderColor", false, "");
    String bottomBorderSize = requestWrapper.getQueryParameter("bottomBorderSize", false, "");
    String bottomBorderColor = requestWrapper.getQueryParameter("bottomBorderColor",  false, "");
    String leftBorderSize = requestWrapper.getQueryParameter("leftBorderSize",  false, "");
    String leftBorderColor = requestWrapper.getQueryParameter("leftBorderColor",  false, "");
    String rightBorderSize = requestWrapper.getQueryParameter("rightBorderSize",  false, "");
    String rightBorderColor = requestWrapper.getQueryParameter("rightBorderColor", false, "");
    boolean fromFormatPainter = Boolean.valueOf(requestWrapper.getQueryParameter("fromFormatPainter", false, "false")).booleanValue();
    String iReport     = requestWrapper.getQueryParameter("iReport", true, "0");
String sMerge = requestWrapper.getQueryParameter("bMerge",false);
_logger.info("sMerge = " + sMerge );
    int nReportIndex = Integer.parseInt(iReport);
    boolean prefUnitIsInch =  Boolean.valueOf(sPrefUnitIsInch).booleanValue();
    UnitType prefUnit = prefUnitIsInch ? UnitType.INCH : UnitType.MILLIMETER;
    ReportEngine objReportEngine = reportEngines.getServiceFromStorageToken(strEntry);
    DocumentInstance doc = objReportEngine.getDocumentFromStorageToken(strEntry);
    _logger.info("nameCell=>" + nameCell + "<");
    for (int i = 0; i < bids.length; i++) {
        CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bids[i]);
        Decoration deco = ((cellInfo.m_tableCell != null) ? (Decoration) cellInfo.m_tableCell : (Decoration) cellInfo.m_cell);
        _logger.info("deco:" + deco);
        if (deco == null) continue;
        Alignment txtAlign = deco.getAlignment();
 Alignment align = deco.getBackgroundAlignment();
        if (!wrapText.equals("")) {
            txtAlign.setWrapText(Boolean.valueOf(wrapText).booleanValue());
        }
        FormatNumber format = null;
        boolean isDefaultFormat = false;
        if (!formatNb.equals("")) { 
        _logger.info("FormatNumber received:" + formatNb);
        isDefaultFormat = formatNb.equals("null"); 
        format = getFormatNumber(objReportEngine, formatNb);
        _logger.info("FormatNumber:" + format);
        }
        if (deco instanceof TableCell) {
        _logger.info("is TableCell");
            TableCell cell = (TableCell) deco;
            if (!widthCheck.equals("")) {
if (fromFormatPainter) 
{
        if (Boolean.valueOf(widthCheck).booleanValue())
        {
        cell.setAutoFitWidth(true);
        }           
           }
           else
cell.setAutoFitWidth(Boolean.valueOf(widthCheck).booleanValue());
            }
            if (!heightCheck.equals("")) {
if (fromFormatPainter) 
{
if (Boolean.valueOf(heightCheck).booleanValue())
cell.setAutoFitHeight(true);
}
else
cell.setAutoFitHeight(Boolean.valueOf(heightCheck).booleanValue());
            }
            if (!widthField.equals("")) {
               cell.setUnit(prefUnit);
               cell.setWidth(ViewerTools.toServerUnit(widthField, prefUnit));
            }
            if (!heightField.equals("")) {
               cell.setUnit(prefUnit);
               cell.setHeight(ViewerTools.toServerUnit(heightField, prefUnit));
            }
            if (!contentAs.equals("")) {
               cell.setContentType(CellContentType.fromInt(Integer.parseInt(contentAs)));
            } 
            if (!padTop.equals("")) {
                cell.setUnit(prefUnit);
                cell.setVerticalPadding(ViewerTools.toServerUnit(padTop, prefUnit));
                _logger.info("set verticalPadding:" + ViewerTools.toServerUnit(padTop, prefUnit) + prefUnit);
            }
            if (!padLeft.equals("")) {
                cell.setUnit(prefUnit);
                cell.setHorizontalPadding(ViewerTools.toServerUnit(padLeft, prefUnit));
                _logger.info("set horizontalPadding:" + ViewerTools.toServerUnit(padLeft, prefUnit) + prefUnit);
            }
            if (format != null) {
               cell.setFormatNumber(format);
               _logger.info("set FormatNumber.");
            } else if (isDefaultFormat) {
               cell.setFormatNumber(null);
            }
      } else {
        Cell cell = (Cell) deco;
        if ((showWhenEmpty != null) && (!showWhenEmpty.equals(""))) {
        cell.setShowWhenEmpty(Boolean.valueOf(showWhenEmpty).booleanValue());
        }
        if (cell instanceof FreeCell) {
        _logger.info("is FreeCell");
        if (oneSelect && (nameCell != null)) {
            ((FreeCell) cell).setValue(nameCell); 
}
        } else if (cell instanceof ReportCell) {
           _logger.info("is ReportCell");
        if (format != null) {
            ((ReportCell) cell).setFormatNumber(format);
            } else if (isDefaultFormat) {
((ReportCell) cell).setFormatNumber(null);
      }
}
        if (!widthCheck.equals("")) {
        if (fromFormatPainter) {
        if (Boolean.valueOf(widthCheck).booleanValue())
        {
        cell.setAutoFitWidth(true);
        }           
           } else {
           cell.setAutoFitWidth(Boolean.valueOf(widthCheck).booleanValue());
           }           
        }
        if (!heightCheck.equals("")) {
        if (fromFormatPainter) {
        if (Boolean.valueOf(heightCheck).booleanValue())
        {
        cell.setAutoFitHeight(true);
        } 
           } else {
           cell.setAutoFitHeight(Boolean.valueOf(heightCheck).booleanValue());
           }                   
        }
        if (!widthField.equals("")) {
           cell.setUnit(prefUnit);
           cell.setWidth(ViewerTools.toServerUnit(widthField, prefUnit));
        }
        if (!heightField.equals("")) {
           cell.setUnit(prefUnit);
           cell.setHeight(ViewerTools.toServerUnit(heightField, prefUnit));
        }
        if (!contentAs.equals("")) {
           cell.setContentType(CellContentType.fromInt(Integer.parseInt(contentAs)));
        } 
        if (!avoidPageBreak.equals("")) {
            cell.avoidPageBreak(Boolean.valueOf(avoidPageBreak).booleanValue());
        }
        if (!repeat.equals("")) {
            cell.repeatOnEveryPage(Boolean.valueOf(repeat).booleanValue());
        }
        if (oneSelect) {
    if (!attachToH.equals("") && !attachToV.equals("") && !attachV.equals("") && !attachH.equals("")) {
         _logger.info("attachToH=" + attachToH + ", VAnchorType= " + VAnchorType.fromInt(Integer.parseInt(attachV))+ ", attachToV=" + attachToV + ", HAnchorType="+  HAnchorType.fromInt(Integer.parseInt(attachH)));
         VAnchorType vAnchor = VAnchorType.fromInt(Integer.parseInt(attachV));
         ReportElement elemV = null;                  
         if (vAnchor != VAnchorType.NONE) {
             elemV = doc.getStructure().getReportElement(attachToV);
         }
         HAnchorType hAnchor = HAnchorType.fromInt(Integer.parseInt(attachH));
         ReportElement elemH = null;
         if (hAnchor != HAnchorType.NONE) {
             elemH = doc.getStructure().getReportElement(attachToH);
         }
         _logger.info("elemV=" + elemV + ", elemH= " + elemH);
         _logger.info("detach ALL;");
         cell.deleteAttachment();
         if ((elemV != null) && (elemH != null)) {
        if (attachToV.equals(attachToH)) {
                 _logger.info("dbl attach V&H to same elem =" + elemV);
                 cell.setAttachTo(elemV, vAnchor, hAnchor);
            } else {     
          _logger.info("dbl attach to" + elemV + " and " + elemH);
             cell.setAttachTo(elemV, vAnchor, elemH, hAnchor);
            }
         } else if (elemV != null) {             
             _logger.info("single attach V to " + elemV);
             cell.setAttachTo(elemV, vAnchor, hAnchor);             
         } else if (elemH != null) {
             _logger.info("single attach H to" + elemH);
             cell.setAttachTo(elemH, vAnchor, hAnchor);
         }
    }
            if (!posH.equals("")) {
            cell.setUnit(prefUnit);
               cell.setX(ViewerTools.toServerUnit(posH, prefUnit));
            }
            if (!posV.equals("")) {
cell.setUnit(prefUnit);
               cell.setY(ViewerTools.toServerUnit(posV, prefUnit));
            }
        }
    }
    if (!txtAlignH.equals("")) {
       txtAlign.setHorizontal(HAlignmentType.fromInt(Integer.parseInt(txtAlignH)));
    }
    if (!txtAlignV.equals("")) {
       txtAlign.setVertical(VAlignmentType.fromInt(Integer.parseInt(txtAlignV)));
    }
    Font font = deco.getFont();
    if (!fntName.equals("")) {
           font.setName(fntName);
    }
    if (!fntSize.equals("")) {
           font.setSize(Integer.parseInt(fntSize));
    }
   int style = 0;
    if (!bold.equals(""))
    {
        if (Boolean.valueOf(bold).booleanValue()) style |= StyleType.BOLD;
    } else {
        style |= (font.getStyle() & StyleType.BOLD);
    }
    if (!italic.equals(""))
    {
        if (Boolean.valueOf(italic).booleanValue()) style |= StyleType.ITALIC;
    } else {
        style |= (font.getStyle() & StyleType.ITALIC);
    }
    if (!underline.equals(""))
    {
        if (Boolean.valueOf(underline).booleanValue()) style |= StyleType.UNDERLINE;
    } else {
        style |= (font.getStyle() & StyleType.UNDERLINE);
    }
    if (!strike.equals(""))
    {
        if (Boolean.valueOf(strike).booleanValue()) style |= StyleType.STRIKETHROUGH;
    } else {
        style |= (font.getStyle() & StyleType.STRIKETHROUGH);
    }
    font.setStyle(style);
    Attributes atts = deco.getAttributes();
    _logger.info("fgColor=" + fgColor);
    if (!fgColor.equals("")) 
    {
    atts.setForeground( ViewerTools.toColor(ViewerTools.split(fgColor, ",")));
    }
_logger.info("bgColor=" + bgColor);
    if (!bgColor.equals("")) 
    {
    atts.setBackground( ViewerTools.toColor(ViewerTools.split(bgColor, ",")));
    }
    if (!isSkin.equals("")) 
    {
       boolean hasSkin = Boolean.valueOf(isSkin).booleanValue();
       boolean hasNoBG = Boolean.valueOf(noneBG).booleanValue();
       boolean bHasEmbeddedImg = Boolean.valueOf(sHasEmbeddedImg).booleanValue();
       if (hasSkin)
       {            
       _logger.info("hasSkin=" + hasSkin + ", skin=>" + urlImg + "<");
            Skin[] skinsAr = objReportEngine.getBackgroundSkins(SkinReportElementType.CELL);
            for (int j = 0; j < skinsAr.length; j++)
            {            
                if (((Skin) skinsAr[j]).getName().equals(urlImg))
                {
                _logger.info("set Skin=" + urlImg);
                    atts.setSkin(skinsAr[j]);
                    break;
                }
            }                                    
       }        
       else if (!hasNoBG) 
       {
if ( !bHasEmbeddedImg )
{
       atts.setSkin(null);
_logger.info("urlImg=" + urlImg);
atts.setBackgroundImageURL(urlImg);
}
else
if (fromFormatPainter)
{
       atts.setSkin(null);
atts.setBackgroundImageResource(sBackGrdImgResourceID);
_logger.info("EmbeddedImg resource ID=" + sBackGrdImgResourceID);
}
_logger.info("dispMode=" + ImageDisplayMode.fromInt(Integer.parseInt(dispMode)));
atts.setBackgroundImageDisplayMode(ImageDisplayMode.fromInt(Integer.parseInt(dispMode)));
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
       else 
       {
         _logger.info("no bg.");
         atts.setSkin(null);
         atts.setBackgroundImageURL(null);
       }
}
_logger.info("getBackgroundImageResource=" + atts.getBackgroundImageResource());
_logger.info("getBackgroundImageURL=" + atts.getBackgroundImageURL());
_logger.info("getBackground=" + atts.getBackground());
_logger.info("getSkin=" + atts.getSkin());
    boolean isSimpleBorder = !borderSize.equals("");
    boolean chgBorder = isSimpleBorder || !topBorderSize.equals("");
    if (chgBorder)
    {
        Border border = (Border) atts.getBorder();
        SimpleBorder sb = null;
        ComplexBorder cb = null;
        if (border instanceof SimpleBorder) {
            sb = (SimpleBorder) border;
            if (!isSimpleBorder) {
               cb = sb.toComplex();
            }
        } else if (border instanceof ComplexBorder) {
            cb = (ComplexBorder) border;
            if (isSimpleBorder) {
               sb = cb.toSimple();
            }
        }
        if (isSimpleBorder) {
            if (!borderSize.equals("")) {
               sb.setSize(BorderSize.fromInt(Integer.parseInt(borderSize)));
            }
            if (!borderColor.equals("")) {
               sb.setColor( ViewerTools.toColor(ViewerTools.split(borderColor, ",")));
            }
        } else {
           SimpleBorder sbTop = cb.getTop();
           if (!topBorderSize.equals("")) {
               sbTop.setSize(BorderSize.fromInt(Integer.parseInt(topBorderSize)));
           }
           if (!topBorderColor.equals("")) {
               sbTop.setColor( ViewerTools.toColor(ViewerTools.split(topBorderColor, ",")));
           }
           SimpleBorder sbBottom = cb.getBottom();
           if (!bottomBorderSize.equals("")) {
               sbBottom.setSize(BorderSize.fromInt(Integer.parseInt(bottomBorderSize)));
           }
           if (!bottomBorderColor.equals("")) {
               sbBottom.setColor( ViewerTools.toColor(ViewerTools.split(bottomBorderColor, ",")));
           }
           SimpleBorder sbLeft = cb.getLeft();
           if (!leftBorderSize.equals("")) {
              sbLeft.setSize(BorderSize.fromInt(Integer.parseInt(leftBorderSize)));
           }
           if (!leftBorderColor.equals("")) {
              sbLeft.setColor( ViewerTools.toColor(ViewerTools.split(leftBorderColor, ",")));
           }
           SimpleBorder sbRight = cb.getRight();
           if (!rightBorderSize.equals("")) {
              sbRight.setSize(BorderSize.fromInt(Integer.parseInt(rightBorderSize)));
           }
           if (!rightBorderColor.equals("")) {
              sbRight.setColor( ViewerTools.toColor(ViewerTools.split(rightBorderColor, ",")));
           }
        }
    }
  }
doc.applyFormat();
String strRedirectTo="";
if ( (sMerge == null) || (sMerge.equals("")) )
{
    strEntry = doc.getStorageToken();
    objUtils.setSessionStorageToken(strEntry, strViewerID, session);
    String strQueryString = requestWrapper.getQueryString();
    int iPos = strQueryString.indexOf("&sBid");
    if (iPos>=0)
      strQueryString = strQueryString.substring(0, iPos);
    strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
    requestWrapper.setQueryString(strQueryString);
strRedirectTo = "report.jsp";
}
else
{
    session.setAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"), doc);
strRedirectTo = "processMergeOrSplit.jsp";
requestWrapper.setQueryParameter("iViewerID", strViewerID);
requestWrapper.setQueryParameter("iReport", iReport);
requestWrapper.setQueryParameter("bMerge", sMerge);
requestWrapper.setQueryParameter("bids", sBids);
}
_logger.info("<--processFormatCell.jsp");
out.clearBuffer();
%>
<jsp:forward page="<%=strRedirectTo%>"/>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_FORMAT", true, out, session);
}
%>
<%!
FormatNumber getFormatNumber(ReportEngine reEngine, String formatNb)
{
    for (int j = 0; j < 4; j++)
    {
        FormatNumberType formatType = FormatNumberType.CURRENCY;
        switch (j)
        {
            case 0: formatType = FormatNumberType.CURRENCY;  break;
            case 1: formatType = FormatNumberType.DATE_TIME; break;
            case 2: formatType = FormatNumberType.NUMBER;    break;
            case 3: formatType = FormatNumberType.CUSTOM;    break;
        }
        FormatNumber[] allFormats = reEngine.getFormatNumbers(formatType);
        int count = allFormats.length;
        for (int k = 0; k < count; k++)
        {
            FormatNumber format = allFormats[k];
            if (format != null)
            {
String sample = format.getSample();
if (formatNb.trim().equals(sample))
{
return format;
}
}
        }
    }
    return null;
}
%>
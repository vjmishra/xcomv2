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
_logger.info("-->processFormatTable.jsp");
try
{
    String strEntry      = requestWrapper.getQueryParameter("sEntry", true);
    _logger.info("strEntry IN = " + strEntry );
    String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
    String sBid         = requestWrapper.getQueryParameter("sBid", false);
boolean fromFormatPainter = Boolean.valueOf(requestWrapper.getQueryParameter("fromFormatPainter", false, "false")).booleanValue();
    String sPrefUnitIsInch = requestWrapper.getQueryParameter("unitIsInch", false);
    String bgColor = requestWrapper.getQueryParameter("bgColor", false);
    String isSkin = requestWrapper.getQueryParameter("isSkin", false);
    String urlImg = requestWrapper.getQueryParameter("urlImg", false);
    String noneBG = requestWrapper.getQueryParameter("noneBG", false);
    String dispMode = requestWrapper.getQueryParameter("dispMode", false);
    String alignH = requestWrapper.getQueryParameter("alignH", false);
    String alignV = requestWrapper.getQueryParameter("alignV", false);
   String sHasEmbeddedImg = requestWrapper.getQueryParameter("hasEmbeddedImg", false, "false");
   String sBackGrdImgResourceID = requestWrapper.getQueryParameter("sBackGrdImgResourceID", false, "");
    String HSpacing = requestWrapper.getQueryParameter("HSpacing", false, "");
    String freqVal = requestWrapper.getQueryParameter("freqVal", false, "");
    String freqColor = requestWrapper.getQueryParameter("freqColor", false, "");
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
    String iReport     = requestWrapper.getQueryParameter("iReport", true, "0");
    int nReportIndex = Integer.parseInt(iReport);
    ReportEngine objReportEngine = reportEngines.getServiceFromStorageToken(strEntry);
    DocumentInstance doc = objReportEngine.getDocumentFromStorageToken(strEntry);
    CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, sBid);
    ReportBlock block = cellInfo.m_block;
    TableFormBase tableForm = (TableFormBase) block.getRepresentation();
    boolean prefUnitIsInch =  Boolean.valueOf(sPrefUnitIsInch).booleanValue();
    UnitType prefUnit = prefUnitIsInch ? UnitType.INCH : UnitType.MILLIMETER;
    Attributes atts = null;
    Alignment align = null;
    atts = tableForm.getBodyTableDecoration().getAttributes();
    align = tableForm.getBodyTableDecoration().getBackgroundAlignment();
    if (!bgColor.equals("")) {
    atts.setBackground( ViewerTools.toColor(ViewerTools.split(bgColor, ",")));
    }
    _logger.info("isSkin=" + isSkin);
    if (!isSkin.equals("")) {
       boolean hasSkin = Boolean.valueOf(isSkin).booleanValue();
       boolean hasNoBG = Boolean.valueOf(noneBG).booleanValue();
       boolean bHasEmbeddedImg = Boolean.valueOf(sHasEmbeddedImg).booleanValue();
       _logger.info("bHasEmbeddedImg=" + bHasEmbeddedImg);
       if (hasSkin)
       {
            _logger.info("Skin=" + urlImg);
            Skin[] skinsAr = objReportEngine.getBackgroundSkins(SkinReportElementType.BLOCK);
            for (int i = 0; i < skinsAr.length; i++)
            {
                if (((Skin) skinsAr[i]).getName().equals(urlImg))
                {
                    atts.setSkin(skinsAr[i]);
                    atts.setBackground(null);                    
                    break;
                }
            }
       } else if (!hasNoBG) {
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
       } else {
_logger.info("no bg.");
atts.setSkin(null);
atts.setBackgroundImageURL(null);        
       }
    }
    if (!HSpacing.equals("")) {
        tableForm.setUnit(prefUnit);
        tableForm.setCellSpacing(ViewerTools.toServerUnit(HSpacing, prefUnit));
    }
    if (!freqVal.equals("") && !freqColor.equals("")) {
       _logger.info("freqVal:"+ freqVal + ", freqColor:"  + ViewerTools.toColor(ViewerTools.split(freqColor, ",")));
       tableForm.setAlternateColor(Integer.parseInt(freqVal), ViewerTools.toColor(ViewerTools.split(freqColor, ",")));
    }
if (!fromFormatPainter) 
{
    String nameTable = requestWrapper.getQueryParameter("nameTable", false);
    String dispHead = requestWrapper.getQueryParameter("dispHead", false);
    String dispFoot = requestWrapper.getQueryParameter("dispFoot", false);
String showLeftHeader = requestWrapper.getQueryParameter("showLeftHeader", false);
String showTopHeader = requestWrapper.getQueryParameter("showTopHeader", false);
String showObjectNames = requestWrapper.getQueryParameter("showObjectNames", false);    
    String dupRow = requestWrapper.getQueryParameter("dupRow", false);
    String showWhenEmpty = requestWrapper.getQueryParameter("showWhenEmpty", false);
    String showEmptyRows = requestWrapper.getQueryParameter("showEmptyRows", false);
    String showEmptyCols = requestWrapper.getQueryParameter("showEmptyCols", false);
    String showEmptyFields = requestWrapper.getQueryParameter("showEmptyFields", false);
    block.setName(nameTable);
    if (!dupRow.equals("")) {
        block.setDuplicateRowAggregation(Boolean.valueOf(dupRow).booleanValue());
    }
    if (!showWhenEmpty.equals("")) {
        block.setShowWhenEmpty(Boolean.valueOf(showWhenEmpty).booleanValue());
    }
    String startNewPage = requestWrapper.getQueryParameter("startNewPage", false);
    String repeatOnEveryPage = requestWrapper.getQueryParameter("repeatOnEveryPage", false);
    String avoidPageBreak = requestWrapper.getQueryParameter("avoidPageBreak", false);
    String repeatHead = requestWrapper.getQueryParameter("repeatHead", false);
    String repeatFoot = requestWrapper.getQueryParameter("repeatFoot", false);
    if ((block.getRepresentation().getType() == TableType.HTABLE) || (block.getRepresentation().getType() == TableType.VTABLE)){
_logger.info("-->HTABLE or VTABLE");
Table table = (Table) block.getRepresentation();
        if (!showEmptyRows.equals("")) {
          ((SimpleTable) table).setShowEmptyRows(Boolean.valueOf(showEmptyRows).booleanValue());
        }
        if (!dispHead.equals("")) {
          table.setHeaderVisible(Boolean.valueOf(dispHead).booleanValue());
        }
        if (!dispFoot.equals("")) {
          table.setFooterVisible(Boolean.valueOf(dispFoot).booleanValue());
        }
        if (!repeatHead.equals("")) {
          table.setHeaderOnEveryPage(Boolean.valueOf(repeatHead).booleanValue());
        }
        if (!repeatFoot.equals("")) {
          table.setFooterOnEveryPage(Boolean.valueOf(repeatFoot).booleanValue());
        }
    } else if (block.getRepresentation().getType() == TableType.XTABLE) {
      CrossTable crossTable = (CrossTable) block.getRepresentation();
   _logger.info("-->XTABLE");
        if (!showLeftHeader.equals("")) {
          crossTable.setLeftHeaderVisible(Boolean.valueOf(showLeftHeader).booleanValue());
          _logger.info("-->LeftHeaderVisible=" + showLeftHeader);
        }
        if (!showTopHeader.equals("")) {
          crossTable.setTopHeaderVisible(Boolean.valueOf(showTopHeader).booleanValue());
          _logger.info("-->TopHeaderVisible=" + showTopHeader);
        }
        if (!showObjectNames.equals("")) {
          crossTable.setExtraHeaderVisible(Boolean.valueOf(showObjectNames).booleanValue());
          _logger.info("-->ExtraHeaderVisible=" + showObjectNames);
        }                        
        if (!dispFoot.equals("")) {
          crossTable.setFooterVisible(Boolean.valueOf(dispFoot).booleanValue());
        }
        if (!showEmptyRows.equals("")) {
           crossTable.setShowEmptyRows(Boolean.valueOf(showEmptyRows).booleanValue());
        }
        if (!showEmptyCols.equals("")) {
           crossTable.setShowEmptyColumns(Boolean.valueOf(showEmptyCols).booleanValue());
        }
        if (!repeatHead.equals("")) {
          crossTable.setHeaderOnEveryPage(Boolean.valueOf(repeatHead).booleanValue());
        }
        if (!repeatFoot.equals("")) {
          crossTable.setFooterOnEveryPage(Boolean.valueOf(repeatFoot).booleanValue());
        }
    } else if (block.getRepresentation().getType() == TableType.FORM) {
   Form form = (Form) block.getRepresentation();
   _logger.info("-->FORM");
        if (!showEmptyFields.equals("")) {
           form.setShowEmptyFields(Boolean.valueOf(showEmptyFields).booleanValue());
        }
    }
    String posH = requestWrapper.getQueryParameter("posH", false);
    String posV = requestWrapper.getQueryParameter("posV", false);
    String attachToH = requestWrapper.getQueryParameter("attachToH", false);
    String attachH = requestWrapper.getQueryParameter("attachH", false);
    String attachToV = requestWrapper.getQueryParameter("attachToV", false);
    String attachV = requestWrapper.getQueryParameter("attachV", false);
String condDisplay = requestWrapper.getQueryParameter("condDisplay", false);
    String condDisplayTxt = requestWrapper.getQueryParameter("condDisplayTxt", false);
    if (!startNewPage.equals("")) {
        block.startOnNewPage(Boolean.valueOf(startNewPage).booleanValue());
    }
    if (!repeatOnEveryPage.equals("")) {
        block.repeatOnEveryPage(Boolean.valueOf(repeatOnEveryPage).booleanValue());
    }
    if (!avoidPageBreak.equals("")) {
        block.avoidPageBreak(Boolean.valueOf(avoidPageBreak).booleanValue());
    }
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
          _logger.info("detach ALL;");
          block.deleteAttachment();
         _logger.info("elemV=" + elemV + ", elemH= " + elemH);
         if ((elemV != null) && (elemH != null)) {
        if (attachToV.equals(attachToH)) {
                 _logger.info("dbl attach V&H to same elem =" + elemV);
                 block.setAttachTo(elemV, vAnchor, hAnchor);
            } else {     
          _logger.info("dbl attach to" + elemV + " and " + elemH);
             block.setAttachTo(elemV, vAnchor, elemH, hAnchor);
            }
         } else if (elemV != null) {             
             _logger.info("single attach V to " + elemV);
             block.setAttachTo(elemV, vAnchor, hAnchor);             
         } else if (elemH != null) {
             _logger.info("single attach H to" + elemH);
             block.setAttachTo(elemH, vAnchor, hAnchor);
         }
    }
    if (!posH.equals("")) {
        block.setUnit(prefUnit);
        block.setX(ViewerTools.toServerUnit(posH, prefUnit));
    }
    if (!posV.equals("")) {
        block.setUnit(prefUnit);
        block.setY(ViewerTools.toServerUnit(posV, prefUnit));
    }
    String axisCommands = requestWrapper.getQueryParameter("axisCommands", false);
    _logger.info("axisCommands="+axisCommands);
    if (!axisCommands.equals("")) {
       String[] axisCommandsList = ViewerTools.convertStringToArray(axisCommands);
       String command = null;
       int axisTypeSrc;
       int axisTypeTarg;
       int idxSrc;
       int idxTarg;
       String id;
       BlockAxis blockAxis;
       ReportExpression re;
       for (int i = 0; i < axisCommandsList.length; i++){
           command = axisCommandsList[i];
           command = command.substring(1, command.length() -1);
           String[] arrayCommands = ViewerTools.split(command, ",");
           char action = arrayCommands[0].charAt(0);
           switch(action)
           {
              case 'a': 
                   axisTypeSrc = Integer.parseInt(arrayCommands[1]);
                   idxSrc = Integer.parseInt(arrayCommands[2]);
                   id = arrayCommands[3];
                   blockAxis = block.getAxis(AxisType.fromInt(axisTypeSrc));
                   re = cellInfo.getExpression(id);
                   _logger.info("Add Expression "+ re + " in axis type = " + AxisType.fromInt(axisTypeSrc) + "->" + blockAxis + " at idx=" + idxSrc + " with ID=" + id);
                   if (idxSrc == -1 ) {
                     blockAxis.addExpr(re) ;
                   } else {
                     blockAxis.insertExpr(re, idxSrc);
                   }
                   _logger.info("Add done.");
                   break;
              case 'd': 
                   axisTypeSrc = Integer.parseInt(arrayCommands[1]);
                   idxSrc = Integer.parseInt(arrayCommands[2]);
                   blockAxis = block.getAxis(AxisType.fromInt(axisTypeSrc));
                   _logger.info("Delete Expression in axis type = " + AxisType.fromInt(axisTypeSrc) + " (" + axisTypeSrc + ")" + " at idx=" + idxSrc);
                   blockAxis.removeExprAt(idxSrc);
                  _logger.info("Delete done.");
                   break;
            }
       }
    }
}    
    boolean isSimpleBorder = !borderSize.equals("");
    boolean chgBorder = isSimpleBorder || !topBorderSize.equals("");
    if (chgBorder)
    {
        Border border = (Border) atts.getBorder();
        SimpleBorder sb = null;
        ComplexBorder cb = null;
        if (border instanceof SimpleBorder) 
        {
            sb = (SimpleBorder) border;
            if (!isSimpleBorder) {
               cb = sb.toComplex();
            }
        } 
        else 
        if (border instanceof ComplexBorder) 
        {
            cb = (ComplexBorder) border;
            if (isSimpleBorder) {
               sb = cb.toSimple();
            }
        }
        if (isSimpleBorder) 
        {
            if (!borderSize.equals("")) {
               sb.setSize(BorderSize.fromInt(Integer.parseInt(borderSize)));
            }
            if (!borderColor.equals("")) {
               sb.setColor( ViewerTools.toColor(ViewerTools.split(borderColor, ",")));
            }
        } 
        else 
        {
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
    _logger.info("<--processFormatTable.jsp");
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
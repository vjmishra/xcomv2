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
_logger.info("-->processFormatChart.jsp");
    String strEntry     = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID  = requestWrapper.getQueryParameter("iViewerID", true);
    String sBid         = requestWrapper.getQueryParameter("sBid", false);
boolean fromFormatPainter = Boolean.valueOf(requestWrapper.getQueryParameter("fromFormatPainter", false, "false")).booleanValue();
    String sPrefUnitIsInch = requestWrapper.getQueryParameter("unitIsInch", false);
    String name = requestWrapper.getQueryParameter("name", false, "");
    String width = requestWrapper.getQueryParameter("width", false, "");
    String height = requestWrapper.getQueryParameter("height", false, "");
    String showWhenEmpty = requestWrapper.getQueryParameter("showWhenEmpty", false, "");
    String chartTitleShow = requestWrapper.getQueryParameter("chartTitleShow", false, "");
    String chartTitle = requestWrapper.getQueryParameter("chartTitle", false, "");
    String x_label = requestWrapper.getQueryParameter("x_label", false, "");
    String y_label = requestWrapper.getQueryParameter("y_label", false, "");
    String z_label = requestWrapper.getQueryParameter("z_label", false, "");
    String legend = requestWrapper.getQueryParameter("legend", false, "");
    String posLegend = requestWrapper.getQueryParameter("posLegend", false, "");
    String dataValues = requestWrapper.getQueryParameter("dataValues", false, "");
    String threeDLook = requestWrapper.getQueryParameter("threeDLook", false, "");
    String bgColor = requestWrapper.getQueryParameter("bgColor", false, "");
String isPie = requestWrapper.getQueryParameter("isPie", false, "");
    String borderSize = requestWrapper.getQueryParameter("borderSize", false, "");
    String borderColor = requestWrapper.getQueryParameter("borderColor", false, "");
String topBorderSize = requestWrapper.getQueryParameter("topBorderSize", false, "");
String bottomBorderSize = requestWrapper.getQueryParameter("bottomBorderSize", false, "");
String leftBorderSize = requestWrapper.getQueryParameter("leftBorderSize", false, "");
String rightBorderSize = requestWrapper.getQueryParameter("rightBorderSize", false, "");
String topBorderColor = requestWrapper.getQueryParameter("topBorderColor", false, "");
String bottomBorderColor = requestWrapper.getQueryParameter("bottomBorderColor", false, "");
String leftBorderColor = requestWrapper.getQueryParameter("leftBorderColor", false, "");
String rightBorderColor = requestWrapper.getQueryParameter("rightBorderColor", false, "");
    String floor = requestWrapper.getQueryParameter("floor", false, "");
    String floorColor = requestWrapper.getQueryParameter("floorColor", false, "");
    String leftWall = requestWrapper.getQueryParameter("leftWall", false, "");
    String rightWall = requestWrapper.getQueryParameter("rightWall", false, "");
    String primColor = requestWrapper.getQueryParameter("primColor", false, "");
    String asPercent = requestWrapper.getQueryParameter("asPercent", false, "");
    String x_frequency = requestWrapper.getQueryParameter("x_frequency", false, "");
    String x_orientation = requestWrapper.getQueryParameter("x_orientation", false, "");
    String x_grid = requestWrapper.getQueryParameter("x_grid", false, "");
    String x_gridColor = requestWrapper.getQueryParameter("x_gridColor", false, "");
    String x_formatNb = requestWrapper.getQueryParameter("x_formatNb", false, "");
    String x_max_idx = requestWrapper.getQueryParameter("x_max_idx", false, "");
    String y_frequency = requestWrapper.getQueryParameter("y_frequency", false, "");
    String hasMinValue = requestWrapper.getQueryParameter("hasMinValue", false, "");
    String minValue= requestWrapper.getQueryParameter("minValue", false, "");
    String hasMaxValue = requestWrapper.getQueryParameter("hasMaxValue", false, "");
    String maxValue = requestWrapper.getQueryParameter("maxValue", false, "");
    String logScale = requestWrapper.getQueryParameter("logScale", false, "");
    String y_orientation = requestWrapper.getQueryParameter("y_orientation", false, "");
    String y_grid = requestWrapper.getQueryParameter("y_grid", false, "");
    String y_gridColor = requestWrapper.getQueryParameter("y_gridColor", false, "");
    String y_formatNb = requestWrapper.getQueryParameter("y_formatNb", false, "");
    String y_max_idx = requestWrapper.getQueryParameter("y_max_idx", false, "");
    String z_frequency = requestWrapper.getQueryParameter("z_frequency", false, "");
    String z_hasMinValue = requestWrapper.getQueryParameter("z_hasMinValue", false, "");
    String z_minValue= requestWrapper.getQueryParameter("z_minValue", false, "");
    String z_hasMaxValue = requestWrapper.getQueryParameter("z_hasMaxValue", false, "");
    String z_maxValue = requestWrapper.getQueryParameter("z_maxValue", false, "");
    String z_logScale = requestWrapper.getQueryParameter("z_logScale", false, "");
    String z_orientation = requestWrapper.getQueryParameter("z_orientation", false, "");
    String z_grid = requestWrapper.getQueryParameter("z_grid", false, "");
    String z_gridColor = requestWrapper.getQueryParameter("z_gridColor", false, "");
    String z_formatNb = requestWrapper.getQueryParameter("z_formatNb", false, "");
    String z_max_idx = requestWrapper.getQueryParameter("z_max_idx", false, "");
    String title_fntName = requestWrapper.getQueryParameter("title_fntName", false, "");
    String title_fntSize = requestWrapper.getQueryParameter("title_fntSize", false, "");
    String title_bold = requestWrapper.getQueryParameter("title_bold", false, "");
    String title_italic = requestWrapper.getQueryParameter("title_italic", false, "");
    String title_under = requestWrapper.getQueryParameter("title_underline", false, "");
    String title_strike = requestWrapper.getQueryParameter("title_strike", false, "");
    String title_align = requestWrapper.getQueryParameter("title_alignH", false, "");
    String title_fgColor = requestWrapper.getQueryParameter("title_fgColor", false, "");
    String title_bgColor = requestWrapper.getQueryParameter("title_bgColor", false, "");
    String title_borderSize = requestWrapper.getQueryParameter("title_borderSize", false, "");
    String title_borderColor = requestWrapper.getQueryParameter("title_borderColor", false, "");
    String chartData_fntName = requestWrapper.getQueryParameter("chartData_fntName", false, "");
    String chartData_fntSize = requestWrapper.getQueryParameter("chartData_fntSize", false, "");
    String chartData_bold = requestWrapper.getQueryParameter("chartData_bold", false, "");
    String chartData_italic = requestWrapper.getQueryParameter("chartData_italic", false, "");
    String chartData_under = requestWrapper.getQueryParameter("chartData_underline", false, "");
    String chartData_strike = requestWrapper.getQueryParameter("chartData_strike", false, "");
    String chartData_align = requestWrapper.getQueryParameter("chartData_alignH", false, "");
    String chartData_fgColor = requestWrapper.getQueryParameter("chartData_fgColor", false, "");
    String chartData_bgColor = requestWrapper.getQueryParameter("chartData_bgColor", false, "");
    String chartData_borderSize = requestWrapper.getQueryParameter("chartData_borderSize", false, "");
    String chartData_borderColor = requestWrapper.getQueryParameter("chartData_borderColor", false, "");
    String legendTitle_fntName = requestWrapper.getQueryParameter("legendTitle_fntName", false, "");
    String legendTitle_fntSize = requestWrapper.getQueryParameter("legendTitle_fntSize", false, "");
    String legendTitle_bold = requestWrapper.getQueryParameter("legendTitle_bold", false, "");
    String legendTitle_italic = requestWrapper.getQueryParameter("legendTitle_italic", false, "");
    String legendTitle_under = requestWrapper.getQueryParameter("legendTitle_underline", false, "");
    String legendTitle_strike = requestWrapper.getQueryParameter("legendTitle_strike", false, "");
    String legendTitle_align = requestWrapper.getQueryParameter("legendTitle_alignH", false, "");
    String legendTitle_fgColor = requestWrapper.getQueryParameter("legendTitle_fgColor", false, "");
    String legendTitle_bgColor = requestWrapper.getQueryParameter("legendTitle_bgColor", false, "");
    String legendTitle_borderSize = requestWrapper.getQueryParameter("legendTitle_borderSize", false, "");
    String legendTitle_borderColor = requestWrapper.getQueryParameter("legendTitle_borderColor", false, "");
    String XAxisLabel_fntName = requestWrapper.getQueryParameter("XAxisLabel_fntName", false, "");
    String XAxisLabel_fntSize = requestWrapper.getQueryParameter("XAxisLabel_fntSize", false, "");
    String XAxisLabel_bold = requestWrapper.getQueryParameter("XAxisLabel_bold", false, "");
    String XAxisLabel_italic = requestWrapper.getQueryParameter("XAxisLabel_italic", false, "");
    String XAxisLabel_under = requestWrapper.getQueryParameter("XAxisLabel_underline", false, "");
    String XAxisLabel_strike = requestWrapper.getQueryParameter("XAxisLabel_strike", false, "");
    String XAxisLabel_align = requestWrapper.getQueryParameter("XAxisLabel_alignH", false, "");
    String XAxisLabel_fgColor = requestWrapper.getQueryParameter("XAxisLabel_fgColor", false, "");
    String XAxisLabel_bgColor = requestWrapper.getQueryParameter("XAxisLabel_bgColor", false, "");
    String XAxisLabel_borderSize = requestWrapper.getQueryParameter("XAxisLabel_borderSize", false, "");
    String XAxisLabel_borderColor = requestWrapper.getQueryParameter("XAxisLabel_borderColor", false, "");
    String YAxisLabel_fntName = requestWrapper.getQueryParameter("YAxisLabel_fntName", false, "");
    String YAxisLabel_fntSize = requestWrapper.getQueryParameter("YAxisLabel_fntSize", false, "");
    String YAxisLabel_bold = requestWrapper.getQueryParameter("YAxisLabel_bold", false, "");
    String YAxisLabel_italic = requestWrapper.getQueryParameter("YAxisLabel_italic", false, "");
    String YAxisLabel_under = requestWrapper.getQueryParameter("YAxisLabel_underline", false, "");
    String YAxisLabel_strike = requestWrapper.getQueryParameter("YAxisLabel_strike", false, "");
    String YAxisLabel_align = requestWrapper.getQueryParameter("YAxisLabel_alignH", false, "");
    String YAxisLabel_fgColor = requestWrapper.getQueryParameter("YAxisLabel_fgColor", false, "");
    String YAxisLabel_bgColor = requestWrapper.getQueryParameter("YAxisLabel_bgColor", false, "");
    String YAxisLabel_borderSize = requestWrapper.getQueryParameter("YAxisLabel_borderSize", false, "");
    String YAxisLabel_borderColor = requestWrapper.getQueryParameter("YAxisLabel_borderColor", false, "");
    String ZAxisLabel_fntName = requestWrapper.getQueryParameter("ZAxisLabel_fntName", false, "");
    String ZAxisLabel_fntSize = requestWrapper.getQueryParameter("ZAxisLabel_fntSize", false, "");
    String ZAxisLabel_bold = requestWrapper.getQueryParameter("ZAxisLabel_bold", false, "");
    String ZAxisLabel_italic = requestWrapper.getQueryParameter("ZAxisLabel_italic", false, "");
    String ZAxisLabel_under = requestWrapper.getQueryParameter("ZAxisLabel_underline", false, "");
    String ZAxisLabel_strike = requestWrapper.getQueryParameter("ZAxisLabel_strike", false, "");
    String ZAxisLabel_align = requestWrapper.getQueryParameter("ZAxisLabel_alignH", false, "");
    String ZAxisLabel_fgColor = requestWrapper.getQueryParameter("ZAxisLabel_fgColor", false, "");
    String ZAxisLabel_bgColor = requestWrapper.getQueryParameter("ZAxisLabel_bgColor", false, "");
    String ZAxisLabel_borderSize = requestWrapper.getQueryParameter("ZAxisLabel_borderSize", false, "");
    String ZAxisLabel_borderColor = requestWrapper.getQueryParameter("ZAxisLabel_borderColor", false, "");
    String XAxisValues_fntName = requestWrapper.getQueryParameter("XAxisValues_fntName", false, "");
    String XAxisValues_fntSize = requestWrapper.getQueryParameter("XAxisValues_fntSize", false, "");
    String XAxisValues_bold = requestWrapper.getQueryParameter("XAxisValues_bold", false, "");
    String XAxisValues_italic = requestWrapper.getQueryParameter("XAxisValues_italic", false, "");
    String XAxisValues_under = requestWrapper.getQueryParameter("XAxisValues_underline", false, "");
    String XAxisValues_strike = requestWrapper.getQueryParameter("XAxisValues_strike", false, "");
    String XAxisValues_align = requestWrapper.getQueryParameter("XAxisValues_alignH", false, "");
    String XAxisValues_fgColor = requestWrapper.getQueryParameter("XAxisValues_fgColor", false, "");
    String XAxisValues_bgColor = requestWrapper.getQueryParameter("XAxisValues_bgColor", false, "");
    String XAxisValues_borderSize = requestWrapper.getQueryParameter("XAxisValues_borderSize", false, "");
    String XAxisValues_borderColor = requestWrapper.getQueryParameter("XAxisValues_borderColor", false, "");
    String YAxisValues_fntName = requestWrapper.getQueryParameter("YAxisValues_fntName", false, "");
    String YAxisValues_fntSize = requestWrapper.getQueryParameter("YAxisValues_fntSize", false, "");
    String YAxisValues_bold = requestWrapper.getQueryParameter("YAxisValues_bold", false, "");
    String YAxisValues_italic = requestWrapper.getQueryParameter("YAxisValues_italic", false, "");
    String YAxisValues_under = requestWrapper.getQueryParameter("YAxisValues_underline", false, "");
    String YAxisValues_strike = requestWrapper.getQueryParameter("YAxisValues_strike", false, "");
    String YAxisValues_align = requestWrapper.getQueryParameter("YAxisValues_alignH", false, "");
    String YAxisValues_fgColor = requestWrapper.getQueryParameter("YAxisValues_fgColor", false, "");
    String YAxisValues_bgColor = requestWrapper.getQueryParameter("YAxisValues_bgColor", false, "");
    String YAxisValues_borderSize = requestWrapper.getQueryParameter("YAxisValues_borderSize", false, "");
    String YAxisValues_borderColor = requestWrapper.getQueryParameter("YAxisValues_borderColor", false, "");
    String ZAxisValues_fntName = requestWrapper.getQueryParameter("ZAxisValues_fntName", false, "");
    String ZAxisValues_fntSize = requestWrapper.getQueryParameter("ZAxisValues_fntSize", false, "");
    String ZAxisValues_bold = requestWrapper.getQueryParameter("ZAxisValues_bold", false, "");
    String ZAxisValues_italic = requestWrapper.getQueryParameter("ZAxisValues_italic", false, "");
    String ZAxisValues_under = requestWrapper.getQueryParameter("ZAxisValues_underline", false, "");
    String ZAxisValues_strike = requestWrapper.getQueryParameter("ZAxisValues_strike", false, "");
    String ZAxisValues_align = requestWrapper.getQueryParameter("ZAxisValues_alignH", false, "");
    String ZAxisValues_fgColor = requestWrapper.getQueryParameter("ZAxisValues_fgColor", false, "");
    String ZAxisValues_bgColor = requestWrapper.getQueryParameter("ZAxisValues_bgColor", false, "");
    String ZAxisValues_borderSize = requestWrapper.getQueryParameter("ZAxisValues_borderSize", false, "");
    String ZAxisValues_borderColor = requestWrapper.getQueryParameter("ZAxisValues_borderColor", false, "");
    String startNewPage = requestWrapper.getQueryParameter("startNewPage", false, "");
    String repeatOnEveryPage = requestWrapper.getQueryParameter("repeatOnEveryPage", false, "");
    String avoidPageBreak = requestWrapper.getQueryParameter("avoidPageBreak", false, "");
    String repeatHead = requestWrapper.getQueryParameter("repeatHead", false, "");
    String repeatFoot = requestWrapper.getQueryParameter("repeatFoot", false, "");
    String dupRow = requestWrapper.getQueryParameter("dupRow", false, "");
    String posH = requestWrapper.getQueryParameter("posH", false, "");
    String posV = requestWrapper.getQueryParameter("posV", false, "");
    String attachToH = requestWrapper.getQueryParameter("attachToH", false, "");
    String attachH = requestWrapper.getQueryParameter("attachH", false, "");
    String attachToV = requestWrapper.getQueryParameter("attachToV", false, "");
    String attachV = requestWrapper.getQueryParameter("attachV", false, "");
    String axisCommands = requestWrapper.getQueryParameter("axisCommands", false, "");
    String iReport     = requestWrapper.getQueryParameter("iReport", true, "0");
    int nReportIndex = Integer.parseInt(iReport);
    boolean prefUnitIsInch =  Boolean.valueOf(sPrefUnitIsInch).booleanValue();
    UnitType prefUnit = prefUnitIsInch ? UnitType.INCH : UnitType.MILLIMETER;
    ReportEngine objReportEngine = reportEngines.getServiceFromStorageToken(strEntry);
    DocumentInstance doc = objReportEngine.getDocumentFromStorageToken(strEntry);
    CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, sBid);
    ReportBlock block = cellInfo.m_block;
    if (!axisCommands.equals("")) {
BlockAxis blockAxis;
for (int axisIdx = 3; axisIdx <= 5 ; axisIdx++)
{
    blockAxis = block.getAxis(AxisType.fromInt(axisIdx));
blockAxis.removeAll();
}
String[] axisCommandsList = ViewerTools.convertStringToArray(axisCommands);
    String command = null;
int axisTypeSrc;
int idxSrc;
    String id;
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
        _logger.info("Add Expression "+ re + " in axis type = " + AxisType.fromInt(axisTypeSrc) + " at idx=" + idxSrc + " with ID=" + id);
       if (idxSrc == -1 ) {
          blockAxis.addExpr(re) ;
       } else {
           blockAxis.insertExpr(re, idxSrc);
        }
       break;
          case 'd': 
          axisTypeSrc = Integer.parseInt(arrayCommands[1]);
        idxSrc = Integer.parseInt(arrayCommands[2]);
        _logger.info("Delete Expression in axis type = " + AxisType.fromInt(axisTypeSrc) + " at idx=" + idxSrc);
        blockAxis = block.getAxis(AxisType.fromInt(axisTypeSrc));
            blockAxis.removeExprAt(idxSrc);
        break;
        }
       }
    }
    Graph graph = (Graph) block.getRepresentation();
    Attributes atts = graph.getAttributes();
_logger.info("block name:" + name);
    block.setName(name);
    if (!showWhenEmpty.equals("")) {
        block.setShowWhenEmpty(Boolean.valueOf(showWhenEmpty).booleanValue());
    }
    if (!width.equals("")) {
        graph.setUnit(prefUnit);
        graph.setWidth(ViewerTools.toServerUnit(width, prefUnit));
    }
    if (!height.equals("")) {
        graph.setUnit(prefUnit);
        graph.setHeight(ViewerTools.toServerUnit(height, prefUnit));
    }
    if (!chartTitleShow.equals("")) {
        graph.getTitle().setTitle(chartTitle);
        graph.getTitle().setVisible(Boolean.valueOf(chartTitleShow).booleanValue());
    }
    if (!legend.equals("")) {
        graph.getLegend().setVisible((Boolean.valueOf(legend).booleanValue()));
    if (!posLegend.equals("") && Boolean.valueOf(legend).booleanValue()) {
        graph.getLegend().setPosition(LegendPosition.fromInt(Integer.parseInt(posLegend)));
        }
    }
    if (!dataValues.equals("")) {
        graph.getData().setVisible((Boolean.valueOf(dataValues).booleanValue()));
    }
    if (!threeDLook.equals("")) {
        graph.set3DLook(Boolean.valueOf(threeDLook).booleanValue());
    }
    if (fromFormatPainter) {
String noneBG = requestWrapper.getQueryParameter("noneBG", false);    
    if (!noneBG.equals("")) {
       boolean hasNoBG = Boolean.valueOf(noneBG).booleanValue();
       if (hasNoBG || !bgColor.equals("-1,-1,-1"))
       {
_logger.info("fromFormatPainter:" + fromFormatPainter + ", hasNoBG:" + hasNoBG + ", bgColor:" + bgColor);       
  if (!bgColor.equals("")) {
      atts.setBackground( ViewerTools.toColor(ViewerTools.split(bgColor, ",")));
   }
       }
    }
} else {
    if (!bgColor.equals("")) {
       atts.setBackground( ViewerTools.toColor(ViewerTools.split(bgColor, ",")));
    }     
}
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
    boolean _isPie = Boolean.valueOf(isPie).booleanValue();    
    _logger.info("floor:" + floor + ", leftWall:" + leftWall + ", rightWall:" + rightWall);
    if (!_isPie && graph.is3D()) {
    _logger.info("not is a pie or is3D");
    if (!floor.equals("")) {
graph.setBottomWallVisible(Boolean.valueOf(floor).booleanValue());
}
    if (!leftWall.equals("")) {  
        _logger.info("leftWall:" + Boolean.valueOf(leftWall).booleanValue());
graph.setLeftWallVisible(Boolean.valueOf(leftWall).booleanValue());
    }
    if (!rightWall.equals("")) {  
    _logger.info("rightWall:" + Boolean.valueOf(rightWall).booleanValue());
graph.setRightWallVisible(Boolean.valueOf(rightWall).booleanValue());
    }
} else {
_logger.info("is2D");
if (!floor.equals("")) {
graph.setRightWallVisible(Boolean.valueOf(floor).booleanValue());
}
}
if (!floorColor.equals("")) {
_logger.info("floorColor:"+ ViewerTools.toColor(ViewerTools.split(floorColor, ",")));
graph.setWallColor( ViewerTools.toColor(ViewerTools.split(floorColor, ",")));
}
    if (!asPercent.equals("")) {
graph.setValueInPercentage(Boolean.valueOf(asPercent).booleanValue());
    }
      GraphAxisProperties graphXAxisProp = graph.getAxis(GraphAxis.X);
      if (!x_label.equals("")) {
              graphXAxisProp.getLabel().setVisible(Boolean.valueOf(x_label).booleanValue());
      }
      if (!x_frequency.equals("")) {
              ((GraphAxisValues) graphXAxisProp.getValues()).setFrequency( Integer.parseInt(x_frequency));
      }
      if (!x_orientation.equals("")) {
              ((GraphAxisValues) graphXAxisProp.getValues()).setOrientation(FontOrientation.fromInt(Integer.parseInt(x_orientation)));
      }
      if (!x_grid.equals("")) {
              graphXAxisProp.getGrid().setVisible(Boolean.valueOf(x_grid).booleanValue());
              if (!x_gridColor.equals("") && Boolean.valueOf(x_grid).booleanValue()) {
                      graphXAxisProp.getGrid().setColor(ViewerTools.toColor(ViewerTools.split(x_gridColor, ",")));
              }
      }
    if (!x_formatNb.equals("")) {
       FormatNumber x_format = getFormatNumber(objReportEngine, x_formatNb);
       _logger.info("FormatNumber:" + x_format);
       int max_x = Integer.parseInt(x_max_idx);
       for (int i=0; i < max_x; i++) {
           graphXAxisProp.setFormatNumber(i, x_format);
        }
    }
      GraphAxisProperties graphYAxisProp = graph.getAxis(GraphAxis.Y);
      if (!y_label.equals("")) {
              graphYAxisProp.getLabel().setVisible(Boolean.valueOf(y_label).booleanValue());
      }
      if (!y_frequency.equals("")) {
              ((GraphAxisValues) graphYAxisProp.getValues()).setFrequency( Integer.parseInt(y_frequency));
      }
      if (!y_orientation.equals("")) {
              ((GraphAxisValues) graphYAxisProp.getValues()).setOrientation(FontOrientation.fromInt(Integer.parseInt(y_orientation)));
      }
      if (!y_grid.equals("")) {
              graphYAxisProp.getGrid().setVisible(Boolean.valueOf(y_grid).booleanValue());
              if (!y_gridColor.equals("") && Boolean.valueOf(y_grid).booleanValue()) {
                      graphYAxisProp.getGrid().setColor(ViewerTools.toColor(ViewerTools.split(y_gridColor, ",")));
              }
      }
      if (!hasMinValue.equals("")) {
if (!minValue.equals("")) 
{
graphYAxisProp.setMinValueGraph(Double.parseDouble(minValue));
}
graphYAxisProp.hasMinValue(Boolean.valueOf(hasMinValue).booleanValue());
      }
      if (!hasMaxValue.equals("")) {
if (!maxValue.equals("")) 
{
graphYAxisProp.setMaxValueGraph(Double.parseDouble(maxValue));
}
graphYAxisProp.hasMaxValue(Boolean.valueOf(hasMaxValue).booleanValue());
      }
      if (!logScale.equals("")) {
              graphYAxisProp.setLogarithmic(Boolean.valueOf(logScale).booleanValue());
      }
    if (!y_formatNb.equals("")) {
       FormatNumber y_format = getFormatNumber(objReportEngine, y_formatNb);
       _logger.info("FormatNumber:" + y_formatNb);
       int max_y = Integer.parseInt(y_max_idx);
       for (int i=0; i < max_y; i++) {
              graphYAxisProp.setFormatNumber(i, y_format);
        }
    }
      GraphAxisProperties graphZAxisProp = graph.getAxis(GraphAxis.Z);
      if (!z_label.equals("")) {
              graphZAxisProp.getLabel().setVisible(Boolean.valueOf(z_label).booleanValue());
      }
      if (!z_frequency.equals("")) {
              ((GraphAxisValues) graphZAxisProp.getValues()).setFrequency(Integer.parseInt(z_frequency));
      }
      if (!z_hasMinValue.equals("")) {
              graphZAxisProp.hasMinValue(Boolean.valueOf(z_hasMinValue).booleanValue());
              if (!z_minValue.equals("") && Boolean.valueOf(z_hasMinValue).booleanValue()) {
                      graphZAxisProp.setMinValueGraph(Double.parseDouble(z_minValue));
              }
      }
      if (!z_hasMaxValue.equals("")) {
              graphZAxisProp.hasMaxValue(Boolean.valueOf(z_hasMaxValue).booleanValue());
              if (!z_maxValue.equals("") && Boolean.valueOf(z_hasMaxValue).booleanValue()) {
                      graphZAxisProp.setMaxValueGraph(Double.parseDouble(z_maxValue));
              }
      }
      if (!z_logScale.equals("")) {
              graphZAxisProp.setLogarithmic(Boolean.valueOf(z_logScale).booleanValue());
      }
      if (!z_orientation.equals("")) {
              ((GraphAxisValues) graphZAxisProp.getValues()).setOrientation(FontOrientation.fromInt(Integer.parseInt(z_orientation)));
      }
      if (!z_grid.equals("")) {
              graphZAxisProp.getGrid().setVisible(Boolean.valueOf(z_grid).booleanValue());
      }
      if (!z_gridColor.equals("") && Boolean.valueOf(z_grid).booleanValue()) {
              graphZAxisProp.getGrid().setColor(ViewerTools.toColor(ViewerTools.split(z_gridColor, ",")));
      }
    if (!z_formatNb.equals("")) {
       FormatNumber z_format = getFormatNumber(objReportEngine, z_formatNb);
       _logger.info("FormatNumber:" + z_formatNb);
       int max_z = Integer.parseInt(z_max_idx);
       for (int i=0; i < max_z; i++) {
           graphZAxisProp.setFormatNumber(i, z_format);
        }
    }
    Decoration deco;
    Font font;
    Attributes attr;
    Border border;
    SimpleBorder sb;
    if (graph.getTitle().isVisible())
    {
        deco = (Decoration) graph.getTitle();
        font = deco.getFont();
        attr = deco.getAttributes();
        if (!title_fntName.equals(""))
            font.setName(title_fntName);
        if (!title_fntSize.equals(""))
            font.setSize(Integer.parseInt(title_fntSize));
        if (!title_bold.equals(""))
            font.setStyle(buildFontStyle(title_bold.equals("1"), isItalic(font), isUnder(font), isStrike (font)));
        if (!title_italic.equals(""))
            font.setStyle(buildFontStyle(isBold(font) , title_italic.equals("1"), isUnder(font), isStrike (font)));
        if (!title_under.equals(""))
            font.setStyle(buildFontStyle(isBold(font), isItalic(font), title_under.equals("1"), isStrike (font)));
        if (!title_align.equals(""))
            deco.getAlignment().setHorizontal(title_align.equals("0") ? HAlignmentType.LEFT : (title_align.equals("1") ? HAlignmentType.CENTER : HAlignmentType.RIGHT));
        if (!title_bgColor.equals(""))
            attr.setBackground( ViewerTools.toColor(ViewerTools.split(title_bgColor, ",")));
        if (!title_fgColor.equals(""))
        {
            attr.setForeground( ViewerTools.toColor(ViewerTools.split(title_fgColor, ",")));
        }
        border = (Border) attr.getBorder();
        sb = null;
        if (border instanceof SimpleBorder) {
            sb = (SimpleBorder) border;
        } else if (border instanceof ComplexBorder) {
            sb = ((ComplexBorder) border).toSimple();
        }
        if (!title_borderSize.equals("")) {
           sb.setSize(BorderSize.fromInt(Integer.parseInt(title_borderSize)));
        }
        if (!title_borderColor.equals("")) {
           sb.setColor( ViewerTools.toColor(ViewerTools.split(title_borderColor, ",")));
        }
    }
    if (graph.getData().isVisible())
    {
        deco = (Decoration) graph.getData();
        font = deco.getFont();
        attr = deco.getAttributes();
        if (!chartData_fntName.equals(""))
            font.setName(chartData_fntName);
        if (!chartData_fntSize.equals(""))
            font.setSize(Integer.parseInt(chartData_fntSize));
        if (!chartData_bold.equals(""))
            font.setStyle(buildFontStyle(chartData_bold.equals("1"), isItalic(font), isUnder(font), isStrike (font)));
        if (!chartData_italic.equals(""))
            font.setStyle(buildFontStyle(isBold(font) , chartData_italic.equals("1"), isUnder(font), isStrike (font)));
        if (!chartData_under.equals(""))
            font.setStyle(buildFontStyle(isBold(font), isItalic(font), chartData_under.equals("1"), isStrike (font)));
        if (!chartData_align.equals(""))
            deco.getAlignment().setHorizontal(chartData_align.equals("0") ? HAlignmentType.LEFT : (chartData_align.equals("1") ? HAlignmentType.CENTER : HAlignmentType.RIGHT));
        if (!chartData_bgColor.equals(""))
            attr.setBackground( ViewerTools.toColor(ViewerTools.split(chartData_bgColor, ",")));
        if (!chartData_fgColor.equals(""))
        {
            attr.setForeground( ViewerTools.toColor(ViewerTools.split(chartData_fgColor, ",")));
        }
        border = (Border) attr.getBorder();
        sb = null;
        if (border instanceof SimpleBorder) {
            sb = (SimpleBorder) border;
        } else if (border instanceof ComplexBorder) {
            sb = ((ComplexBorder) border).toSimple();
        }
        if (!chartData_borderSize.equals("")) {
           sb.setSize(BorderSize.fromInt(Integer.parseInt(chartData_borderSize)));
        }
        if (!chartData_borderColor.equals("")) {
           sb.setColor( ViewerTools.toColor(ViewerTools.split(chartData_borderColor, ",")));
        }
    }
    if (graph.getLegend().isVisible())
    {
_logger.info("Legend is visible.");
        deco = (Decoration) graph.getLegend();
        font = deco.getFont();
        attr = deco.getAttributes();
        if (!legendTitle_fntName.equals(""))
            font.setName(legendTitle_fntName);
        if (!legendTitle_fntSize.equals(""))
            font.setSize(Integer.parseInt(legendTitle_fntSize));
        if (!legendTitle_bold.equals(""))
            font.setStyle(buildFontStyle(legendTitle_bold.equals("1"), isItalic(font), isUnder(font), isStrike (font)));
        if (!legendTitle_italic.equals(""))
            font.setStyle(buildFontStyle(isBold(font) , legendTitle_italic.equals("1"), isUnder(font), isStrike (font)));
        if (!legendTitle_under.equals(""))
            font.setStyle(buildFontStyle(isBold(font), isItalic(font), legendTitle_under.equals("1"), isStrike (font)));
_logger.info("legendTitle_bold" + legendTitle_bold);
_logger.info("legendTitle_italic" + legendTitle_italic);
_logger.info("legendTitle_under" + legendTitle_under);
_logger.info("legendTitle_align" + legendTitle_align);
        if (!legendTitle_align.equals(""))
            deco.getAlignment().setHorizontal(legendTitle_align.equals("0") ? HAlignmentType.LEFT : (legendTitle_align.equals("1") ? HAlignmentType.CENTER : HAlignmentType.RIGHT));
        if (!legendTitle_bgColor.equals(""))
            attr.setBackground( ViewerTools.toColor(ViewerTools.split(legendTitle_bgColor, ",")));
        if (!legendTitle_fgColor.equals(""))
        {
            attr.setForeground( ViewerTools.toColor(ViewerTools.split(legendTitle_fgColor, ",")));
        }
        border = (Border) attr.getBorder();
        sb = null;
        if (border instanceof SimpleBorder) {
            sb = (SimpleBorder) border;
        } else if (border instanceof ComplexBorder) {
            sb = ((ComplexBorder) border).toSimple();
        }
        if (!legendTitle_borderSize.equals("")) {
           sb.setSize(BorderSize.fromInt(Integer.parseInt(legendTitle_borderSize)));
        }
        if (!legendTitle_borderColor.equals("")) {
           sb.setColor( ViewerTools.toColor(ViewerTools.split(legendTitle_borderColor, ",")));
        }
    }
    if (graphXAxisProp.getLabel().isVisible())
    {
        deco = (Decoration) graphXAxisProp.getLabel();
        font = deco.getFont();
        attr = deco.getAttributes();
        if (!XAxisLabel_fntName.equals(""))
            font.setName(XAxisLabel_fntName);
        if (!XAxisLabel_fntSize.equals(""))
            font.setSize(Integer.parseInt(XAxisLabel_fntSize));
        if (!XAxisLabel_bold.equals(""))
            font.setStyle(buildFontStyle(XAxisLabel_bold.equals("1"), isItalic(font), isUnder(font), isStrike (font)));
        if (!XAxisLabel_italic.equals(""))
            font.setStyle(buildFontStyle(isBold(font) , XAxisLabel_italic.equals("1"), isUnder(font), isStrike (font)));
        if (!XAxisLabel_under.equals(""))
            font.setStyle(buildFontStyle(isBold(font), isItalic(font), XAxisLabel_under.equals("1"), isStrike (font)));
        if (!XAxisLabel_align.equals(""))
            deco.getAlignment().setHorizontal(XAxisLabel_align.equals("0") ? HAlignmentType.LEFT : (XAxisLabel_align.equals("1") ? HAlignmentType.CENTER : HAlignmentType.RIGHT));
        if (!XAxisLabel_bgColor.equals(""))
            attr.setBackground( ViewerTools.toColor(ViewerTools.split(XAxisLabel_bgColor, ",")));
        if (!XAxisLabel_fgColor.equals(""))
        {
            attr.setForeground( ViewerTools.toColor(ViewerTools.split(XAxisLabel_fgColor, ",")));
        }
        border = (Border) attr.getBorder();
        sb = null;
        if (border instanceof SimpleBorder) {
            sb = (SimpleBorder) border;
        } else if (border instanceof ComplexBorder) {
            sb = ((ComplexBorder) border).toSimple();
        }
        if (!XAxisLabel_borderSize.equals("")) {
           sb.setSize(BorderSize.fromInt(Integer.parseInt(XAxisLabel_borderSize)));
        }
        if (!XAxisLabel_borderColor.equals("")) {
           sb.setColor( ViewerTools.toColor(ViewerTools.split(XAxisLabel_borderColor, ",")));
        }
    }
    deco = (Decoration) graphXAxisProp.getValues();
    font = deco.getFont();
    attr = deco.getAttributes();
    if (!XAxisValues_fntName.equals(""))
        font.setName(XAxisValues_fntName);
    if (!XAxisValues_fntSize.equals(""))
        font.setSize(Integer.parseInt(XAxisValues_fntSize));
    if (!XAxisValues_bold.equals(""))
        font.setStyle(buildFontStyle(XAxisValues_bold.equals("1"), isItalic(font), isUnder(font), isStrike (font)));
    if (!XAxisValues_italic.equals(""))
        font.setStyle(buildFontStyle(isBold(font) , XAxisValues_italic.equals("1"), isUnder(font), isStrike (font)));
    if (!XAxisValues_under.equals(""))
        font.setStyle(buildFontStyle(isBold(font), isItalic(font), XAxisValues_under.equals("1"), isStrike (font)));
    if (!XAxisValues_align.equals(""))
        deco.getAlignment().setHorizontal(XAxisValues_align.equals("0") ? HAlignmentType.LEFT : (XAxisValues_align.equals("1") ? HAlignmentType.CENTER : HAlignmentType.RIGHT));
    if (!XAxisValues_bgColor.equals(""))
        attr.setBackground( ViewerTools.toColor(ViewerTools.split(XAxisValues_bgColor, ",")));
    if (!XAxisValues_fgColor.equals(""))
    {
        attr.setForeground( ViewerTools.toColor(ViewerTools.split(XAxisValues_fgColor, ",")));
    }
    border = (Border) attr.getBorder();
    sb = null;
    if (border instanceof SimpleBorder) {
        sb = (SimpleBorder) border;
    } else if (border instanceof ComplexBorder) {
        sb = ((ComplexBorder) border).toSimple();
    }
    if (!XAxisValues_borderSize.equals("")) {
       sb.setSize(BorderSize.fromInt(Integer.parseInt(XAxisValues_borderSize)));
    }
    if (!XAxisValues_borderColor.equals("")) {
       sb.setColor( ViewerTools.toColor(ViewerTools.split(XAxisValues_borderColor, ",")));
    }
    if (graphYAxisProp.getLabel().isVisible())
    {
        deco = (Decoration) graphYAxisProp.getLabel();
        font = deco.getFont();
        attr = deco.getAttributes();
        if (!YAxisLabel_fntName.equals(""))
            font.setName(YAxisLabel_fntName);
        if (!YAxisLabel_fntSize.equals(""))
            font.setSize(Integer.parseInt(YAxisLabel_fntSize));
        if (!YAxisLabel_bold.equals(""))
            font.setStyle(buildFontStyle(YAxisLabel_bold.equals("1"), isItalic(font), isUnder(font), isStrike (font)));
        if (!YAxisLabel_italic.equals(""))
            font.setStyle(buildFontStyle(isBold(font) , YAxisLabel_italic.equals("1"), isUnder(font), isStrike (font)));
        if (!YAxisLabel_under.equals(""))
            font.setStyle(buildFontStyle(isBold(font), isItalic(font), YAxisLabel_under.equals("1"), isStrike (font)));
        if (!YAxisLabel_align.equals(""))
            deco.getAlignment().setHorizontal(YAxisLabel_align.equals("0") ? HAlignmentType.LEFT : (YAxisLabel_align.equals("1") ? HAlignmentType.CENTER : HAlignmentType.RIGHT));
        if (!YAxisLabel_bgColor.equals(""))
            attr.setBackground( ViewerTools.toColor(ViewerTools.split(YAxisLabel_bgColor, ",")));
        if (!YAxisLabel_fgColor.equals(""))
        {
            attr.setForeground( ViewerTools.toColor(ViewerTools.split(YAxisLabel_fgColor, ",")));
        }
        border = (Border) attr.getBorder();
        sb = null;
        if (border instanceof SimpleBorder) {
            sb = (SimpleBorder) border;
        } else if (border instanceof ComplexBorder) {
            sb = ((ComplexBorder) border).toSimple();
        }
        if (!YAxisLabel_borderSize.equals("")) {
           sb.setSize(BorderSize.fromInt(Integer.parseInt(YAxisLabel_borderSize)));
        }
        if (!YAxisLabel_borderColor.equals("")) {
           sb.setColor( ViewerTools.toColor(ViewerTools.split(YAxisLabel_borderColor, ",")));
        }
    }
    deco = (Decoration) graphYAxisProp.getValues();
    font = deco.getFont();
    attr = deco.getAttributes();
    if (!YAxisValues_fntName.equals(""))
        font.setName(YAxisValues_fntName);
    if (!YAxisValues_fntSize.equals(""))
        font.setSize(Integer.parseInt(YAxisValues_fntSize));
    if (!YAxisValues_bold.equals(""))
        font.setStyle(buildFontStyle(YAxisValues_bold.equals("1"), isItalic(font), isUnder(font), isStrike (font)));
    if (!YAxisValues_italic.equals(""))
        font.setStyle(buildFontStyle(isBold(font) , YAxisValues_italic.equals("1"), isUnder(font), isStrike (font)));
    if (!YAxisValues_under.equals(""))
        font.setStyle(buildFontStyle(isBold(font), isItalic(font), YAxisValues_under.equals("1"), isStrike (font)));
    if (!YAxisValues_align.equals(""))
        deco.getAlignment().setHorizontal(YAxisValues_align.equals("0") ? HAlignmentType.LEFT : (YAxisValues_align.equals("1") ? HAlignmentType.CENTER : HAlignmentType.RIGHT));
    if (!YAxisValues_bgColor.equals(""))
        attr.setBackground( ViewerTools.toColor(ViewerTools.split(YAxisValues_bgColor, ",")));
    if (!YAxisValues_fgColor.equals(""))
    {
        attr.setForeground( ViewerTools.toColor(ViewerTools.split(YAxisValues_fgColor, ",")));
    }
    border = (Border) attr.getBorder();
    sb = null;
    if (border instanceof SimpleBorder) {
        sb = (SimpleBorder) border;
    } else if (border instanceof ComplexBorder) {
        sb = ((ComplexBorder) border).toSimple();
    }
    if (!YAxisValues_borderSize.equals("")) {
       sb.setSize(BorderSize.fromInt(Integer.parseInt(YAxisValues_borderSize)));
    }
    if (!YAxisValues_borderColor.equals("")) {
       sb.setColor( ViewerTools.toColor(ViewerTools.split(YAxisValues_borderColor, ",")));
    }
    if (graphZAxisProp.getLabel().isVisible())
    {
        deco = (Decoration) graphZAxisProp.getLabel();
        font = deco.getFont();
        attr = deco.getAttributes();
        if (!ZAxisLabel_fntName.equals(""))
            font.setName(ZAxisLabel_fntName);
        if (!ZAxisLabel_fntSize.equals(""))
            font.setSize(Integer.parseInt(ZAxisLabel_fntSize));
        if (!ZAxisLabel_bold.equals(""))
            font.setStyle(buildFontStyle(ZAxisLabel_bold.equals("1"), isItalic(font), isUnder(font), isStrike (font)));
        if (!ZAxisLabel_italic.equals(""))
            font.setStyle(buildFontStyle(isBold(font) , ZAxisLabel_italic.equals("1"), isUnder(font), isStrike (font)));
        if (!ZAxisLabel_under.equals(""))
            font.setStyle(buildFontStyle(isBold(font), isItalic(font), ZAxisLabel_under.equals("1"), isStrike (font)));
        if (!ZAxisLabel_align.equals(""))
            deco.getAlignment().setHorizontal(ZAxisLabel_align.equals("0") ? HAlignmentType.LEFT : (ZAxisLabel_align.equals("1") ? HAlignmentType.CENTER : HAlignmentType.RIGHT));
        if (!ZAxisLabel_bgColor.equals(""))
            attr.setBackground( ViewerTools.toColor(ViewerTools.split(ZAxisLabel_bgColor, ",")));
        if (!ZAxisLabel_fgColor.equals(""))
        {
            attr.setForeground( ViewerTools.toColor(ViewerTools.split(ZAxisLabel_fgColor, ",")));
        }
        border = (Border) attr.getBorder();
        sb = null;
        if (border instanceof SimpleBorder) {
            sb = (SimpleBorder) border;
        } else if (border instanceof ComplexBorder) {
            sb = ((ComplexBorder) border).toSimple();
        }
        if (!ZAxisLabel_borderSize.equals("")) {
           sb.setSize(BorderSize.fromInt(Integer.parseInt(ZAxisLabel_borderSize)));
        }
        if (!ZAxisLabel_borderColor.equals("")) {
           sb.setColor( ViewerTools.toColor(ViewerTools.split(ZAxisLabel_borderColor, ",")));
        }
    }
    deco = (Decoration) graphZAxisProp.getValues();
    font = deco.getFont();
    attr = deco.getAttributes();
    if (!ZAxisValues_fntName.equals(""))
        font.setName(ZAxisValues_fntName);
    if (!ZAxisValues_fntSize.equals(""))
        font.setSize(Integer.parseInt(ZAxisValues_fntSize));
    if (!ZAxisValues_bold.equals(""))
        font.setStyle(buildFontStyle(ZAxisValues_bold.equals("1"), isItalic(font), isUnder(font), isStrike (font)));
    if (!ZAxisValues_italic.equals(""))
        font.setStyle(buildFontStyle(isBold(font) , ZAxisValues_italic.equals("1"), isUnder(font), isStrike (font)));
    if (!ZAxisValues_under.equals(""))
        font.setStyle(buildFontStyle(isBold(font), isItalic(font), ZAxisValues_under.equals("1"), isStrike (font)));
    if (!ZAxisValues_align.equals(""))
        deco.getAlignment().setHorizontal(ZAxisValues_align.equals("0") ? HAlignmentType.LEFT : (ZAxisValues_align.equals("1") ? HAlignmentType.CENTER : HAlignmentType.RIGHT));
    if (!ZAxisValues_bgColor.equals(""))
        attr.setBackground( ViewerTools.toColor(ViewerTools.split(ZAxisValues_bgColor, ",")));
    if (!ZAxisValues_fgColor.equals(""))
    {
        attr.setForeground( ViewerTools.toColor(ViewerTools.split(ZAxisValues_fgColor, ",")));
    }
    border = (Border) attr.getBorder();
    sb = null;
    if (border instanceof SimpleBorder) {
        sb = (SimpleBorder) border;
    } else if (border instanceof ComplexBorder) {
        sb = ((ComplexBorder) border).toSimple();
    }
    if (!ZAxisValues_borderSize.equals("")) {
       sb.setSize(BorderSize.fromInt(Integer.parseInt(ZAxisValues_borderSize)));
    }
    if (!ZAxisValues_borderColor.equals("")) {
       sb.setColor( ViewerTools.toColor(ViewerTools.split(ZAxisValues_borderColor, ",")));
    }
    if (!startNewPage.equals("")) {
        block.startOnNewPage(Boolean.valueOf(startNewPage).booleanValue());
    }
    if (!repeatOnEveryPage.equals("")) {
        block.repeatOnEveryPage(Boolean.valueOf(repeatOnEveryPage).booleanValue());
    }
    if (!avoidPageBreak.equals("")) {
        block.avoidPageBreak(Boolean.valueOf(avoidPageBreak).booleanValue());
    }
    if (!dupRow.equals("")) {
        block.setDuplicateRowAggregation(Boolean.valueOf(dupRow).booleanValue());
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
         _logger.info("elemV=" + elemV + ", elemH= " + elemH);
          _logger.info("detach ALL;");
          block.deleteAttachment();
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
doc.applyFormat();
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strQueryString = requestWrapper.getQueryString();
int iPos = strQueryString.indexOf("&sBid");
if (iPos>=0)
  strQueryString = strQueryString.substring(0, iPos);
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
requestWrapper.setQueryString(strQueryString);
_logger.info("<--processFormatChart.jsp");
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
boolean isBold   (Font font) { return  (font.getStyle() & StyleType.BOLD)          == StyleType.BOLD;          }
boolean isItalic (Font font) { return  (font.getStyle() & StyleType.ITALIC)        == StyleType.ITALIC;        }
boolean isUnder  (Font font) { return  (font.getStyle() & StyleType.UNDERLINE)     == StyleType.UNDERLINE;     }
boolean isStrike (Font font) { return  (font.getStyle() & StyleType.STRIKETHROUGH) == StyleType.STRIKETHROUGH; }
int buildFontStyle(boolean bold, boolean italic, boolean under, boolean strike)
{
int style = 0;
if (bold)   style |= StyleType.BOLD;
if (italic) style |= StyleType.ITALIC;
if (under)  style |= StyleType.UNDERLINE;
if (strike) style |= StyleType.STRIKETHROUGH;
return style;
}
FormatNumber getFormatNumber(ReportEngine reEngine, String formatNb)
{
    if (formatNb.trim().equals("null")) return null;
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
        FormatNumber[] allFormats = reEngine .getFormatNumbers(formatType);
        int count = allFormats.length;
        for (int k = 0; k < count; k++)
        {
            FormatNumber format = allFormats[k];
            if (format.getSample().equals(formatNb.trim()))
            {
                return format;
            }
        }
    }
    return null;
}
%>

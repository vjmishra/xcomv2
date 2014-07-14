<%@ page import="com.businessobjects.rebean.wi.*,com.businessobjects.adv_ivcdzview.*,java.io.IOException" %>
<%! 
void printElementInfo(StringBuffer out, String varName, String className, String bid, String parentVarName)
throws IOException
{
        out.append(varName+"=ac(" + parentVarName + ",\"" + className + "\",\"" + bid + "\");");
}
String getColorString(java.awt.Color color)
{
if (color != null) {
            return "cl(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
        } else {
            return "cl(-1,-1,-1)";
        }
}
void printColor(StringBuffer out, java.awt.Color color)
throws IOException
{
    if (color != null) {
        out.append( "cl(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")");
    } else {
        out.append( "cl(-1,-1,-1)");
    }
}
void printBlock(StringBuffer out, ReportBlock block, Representation repr, DHTMLLogger _logger)
throws IOException
{
        int nDimDetails = countBlockVars(block, false);
        int nMeasures   = countBlockVars(block, true);
        StringBuffer s = new StringBuffer("var varBlock = block(");
        s.append( "\"" + ViewerTools.escapeQuotes(block.getName()) + "\",");
        s.append(CurrentCellInfo.getBlockType(repr) + ",");
        s.append(nDimDetails + ",");
        s.append(nMeasures + ",");
        s.append((block.getDuplicateRowAggregation()? "1" : "0") + ",");
        s.append((block.startOnNewPage()? "1" : "0") + ",");
        s.append((block.repeatOnEveryPage()? "1" : "0") + ",");
        s.append((block.avoidPageBreak()? "1" : "0") + ",");
        s.append(((((Position) block).getUnit().value() == UnitType._INCH )? "1" : "0") + ","); 
        s.append(((Position) block).getX() + ",");
        s.append(((Position) block).getY() + ",");
        s.append((block.isShowWhenEmpty()? "1" : "0") + ");");
        out.append(s);
}
int countBlockVars(ReportBlock block, boolean bMeasures)
{
int            nVarCount = 0;
Representation repr      = block.getRepresentation();
BlockType      bType     = repr.getType();
if (repr instanceof GraphType) {
        nVarCount += countAxisVars(block, GraphAxis.X, bMeasures);
nVarCount += countAxisVars(block, GraphAxis.Y, bMeasures);
nVarCount += countAxisVars(block, GraphAxis.Z, bMeasures);
} else {
nVarCount += countAxisVars(block, TableAxis.CONTENT, bMeasures);
nVarCount += countAxisVars(block, TableAxis.VERTICAL, bMeasures);
nVarCount += countAxisVars(block, TableAxis.HORIZONTAL, bMeasures);
}
return nVarCount;
}
int countAxisVars(ReportBlock block, AxisType axisType, boolean bMeasures)
{
int nVarCount = 0;
BlockAxis axis = block.getAxis(axisType);
if (axis != null) {
    int count = axis.getCount();
    for (int i = 0; i < count; i++) {
            ReportExpression expr = axis.getExpr(i);
        if (bMeasures) {
            if ((expr.getQualification() == ObjectQualification.MEASURE) && (expr.getType() == ObjectType.NUMERIC))
            nVarCount++;
            } else {
               if (expr.getQualification() != ObjectQualification.MEASURE)
                   nVarCount++;
        }
        }
    }
    return nVarCount;
}
void printAttributes(StringBuffer out, Attributes att, String varName, boolean bDoNoPrintFGColor)
throws IOException
{
    out.append(varName + ".bgColor=");
    printColor(out, att.getBackground());
    out.append(";");
    String imgURL = (att.getBackgroundImageURL() == null)? "" : att.getBackgroundImageURL();
    out.append(varName  + ".imgURL=\"" + imgURL + "\";");
    if (!bDoNoPrintFGColor)
    {
        out.append(varName  + ".fgColor=");
        printColor(out, att.getForeground());
        out.append(";");
    }
String embimg = (att.getBackgroundImageResource() != null) ? "true" : "false";
    out.append(varName  + ".embimg =" + embimg + ";");
    printBorders(out, att.getBorder(), varName);
}
void printAllAttributes(StringBuffer out, Attributes att, String varName, boolean bDoNoPrintFGColor, Alignment align)
throws IOException
{
out.append(varName + ".bgColor=");
    printColor(out, att.getBackground());
    out.append(";");
String imgURL = att.getBackgroundImageURL();
if (imgURL != null) 
{ 
out.append(varName  + ".imgURL=\"" + imgURL + "\";");
} 
else if (att.getSkin() != null) 
{    
    out.append(varName  + ".skin=\"" + att.getSkin().getName() + "\";");  
}
String embimg = (att.getBackgroundImageResource() != null) ? "true" : "false";
out.append(varName  + ".embimg=" + embimg + ";");    
if ((att.getBackgroundImageResource() != null) || (imgURL != null) || (att.getSkin() != null))
{
out.append(varName  + ".dispMode=" + att.getBackgroundImageDisplayMode().value() + ";");
out.append(varName  + ".HAlign=" + align.getHorizontal().value() + ";");
    out.append(varName  + ".VAlign=" + align.getVertical().value() + ";");
}
    if (!bDoNoPrintFGColor)
    {
        out.append(varName  + ".fgColor=");
        printColor(out, att.getForeground());
        out.append(";");
    }
    printBorders(out, att.getBorder(), varName);
}
void printAlign(StringBuffer out, Alignment align, String varName)
throws IOException
{
    StringBuffer s = new StringBuffer("align(" + varName + ",");
    HAlignmentType hAlign = align.getHorizontal();
    VAlignmentType vAlign = align.getVertical();
    if (hAlign == HAlignmentType.LEFT) {
        s.append("0");
    } else if (hAlign == HAlignmentType.CENTER) {
        s.append("1");
    } else {
        s.append("2");
    }
    s.append(",");
    if (vAlign == VAlignmentType.TOP) {
        s.append("0");
    } else if (vAlign == VAlignmentType.CENTER) {
        s.append("1");
    }else {
        s.append("2");
    }
    s.append(",");
    s.append(align.getWrapText() ? "1" : "0");
    s.append(");");
    out.append(s.toString());
}
void printFont(StringBuffer out, Font font, String varName)
throws IOException
{
    StringBuffer s = new StringBuffer((((font.getStyle() & StyleType.BOLD)==StyleType.BOLD)?"1":"0"));
    s.append("," + (((font.getStyle() & StyleType.ITALIC)==StyleType.ITALIC)?"1":"0"));
    s.append("," + (((font.getStyle() & StyleType.UNDERLINE)==StyleType.UNDERLINE)?"1":"0"));
    s.append("," + (((font.getStyle() & StyleType.STRIKETHROUGH)==StyleType.STRIKETHROUGH)?"1":"0"));
    out.append("fnt(" + varName + ",\"" +  ViewerTools.escapeQuotes(font.getName()) + "\"," + font.getSize() + "," + s.toString() +");");
}
void printBorders(StringBuffer out, Border border, String varName)
throws IOException
{
    if (border instanceof SimpleBorder) {
       out.append("bd(" + varName + ",");
       SimpleBorder sb = (SimpleBorder) border;
       out.append( sb.getSize().value() + ",");
       printColor(out, sb.getColor());
    } else if (border instanceof ComplexBorder) {
       out.append("bs(" + varName + ",");
       ComplexBorder cb = (ComplexBorder) border;
       SimpleBorder sbTop = cb.getTop();
       out.append( sbTop.getSize().value() + ",");
       printColor(out, sbTop.getColor());
       SimpleBorder sbBottom = cb.getBottom();
       out.append( "," + sbBottom.getSize().value() + ",");
       printColor(out, sbBottom.getColor());
       SimpleBorder sbLeft = cb.getLeft();
       out.append( "," + sbLeft.getSize().value() + ",");
       printColor(out, sbLeft.getColor());
       SimpleBorder sbRight = cb.getRight();
       out.append( "," + sbRight.getSize().value() + ",");
       printColor(out, sbRight.getColor());
    }
    out.append(");");
}
%>

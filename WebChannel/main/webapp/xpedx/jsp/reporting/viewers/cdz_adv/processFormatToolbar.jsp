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
%>
<%
response.setDateHeader("expires", 0);
if (!isAlive)
{
objUtils.invalidSessionDialog(out);
return;
}
try
{
_logger.info("-->processFormatToolbar.jsp");
String strEntry      = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
String bids          = requestWrapper.getQueryParameter("bids", true);
String iReport     = requestWrapper.getQueryParameter("iReport", false, "0");
String fontName     = requestWrapper.getQueryParameter("f", false, null);
String fontSize     = requestWrapper.getQueryParameter("s", false, null);
String bold         = requestWrapper.getQueryParameter("b", false, null);
String italic       = requestWrapper.getQueryParameter("i", false, null);
String under        = requestWrapper.getQueryParameter("u", false, null);
String align        = requestWrapper.getQueryParameter("a", false, null);
String valign       = requestWrapper.getQueryParameter("va", false, null);
String wraptext= requestWrapper.getQueryParameter("wt", false, null);
String bg           = requestWrapper.getQueryParameter("bg", false, null);
String fg           = requestWrapper.getQueryParameter("fg", false, null);
    String tb= requestWrapper.getQueryParameter("tb",  false, null);
    String bb= requestWrapper.getQueryParameter("bb", false, null);
    String lb= requestWrapper.getQueryParameter("lb",  false, null);
    String rb= requestWrapper.getQueryParameter("rb",  false, null);
String bc           = requestWrapper.getQueryParameter("bc", false, null);
String bMerge= requestWrapper.getQueryParameter("bMerge", false, null);
int      nReportIndex = Integer.parseInt(iReport);
ReportEngine objReportEngine = reportEngines.getServiceFromStorageToken(strEntry);
DocumentInstance doc = objReportEngine.getDocumentFromStorageToken(strEntry);
ReportElementContainer rs              = doc.getStructure();
ReportContainer        report          = (ReportContainer) rs.getChildAt(nReportIndex);
String[]               bid             = ViewerTools.split(bids,",");
_logger.info("process n elements=" + bid.length);
for (int i = 0; i < bid.length; i++)
{
ReportElement re = report.getReportElement(bid[i]);
Attributes   attr = null;
if ( re == null )
_logger.error("no reportElement found");
if ((re != null) && ((re instanceof SectionContainer)||(re instanceof PageHeaderFooter)||(re instanceof ReportBody)))
{
_logger.info("SectionContainer or PageHeaderFooter or ReportBody");
  Decoration deco = (Decoration) re;
  attr = deco.getAttributes();
  if (!bg.equals("")) {
attr.setBackground(ViewerTools.toColor(ViewerTools.split(bg, ",")));
attr.setBackgroundImageURL(null);
}
}
else if ((re != null) && (re instanceof ReportBlock))
{
_logger.info("ReportBlock");
Representation repr = ((ReportBlock)re).getRepresentation();
if (repr instanceof Graph)
{
_logger.info("Graph");
Decoration deco = (Decoration) ((Graph)repr);
attr = deco.getAttributes();
if (align != null)
deco.getAlignment().setHorizontal(align.equals("0") ? HAlignmentType.LEFT : (align.equals("1") ? HAlignmentType.CENTER : HAlignmentType.RIGHT));
if (valign != null)
deco.getAlignment().setVertical(valign.equals("0") ? VAlignmentType.TOP : (valign.equals("1") ? VAlignmentType.CENTER : VAlignmentType.BOTTOM));
if (bg!=null) {
attr.setBackground( ViewerTools.toColor(ViewerTools.split(bg, ",")));
attr.setBackgroundImageURL(null);
}
if (fg!=null)
{
attr.setForeground( ViewerTools.toColor(ViewerTools.split(fg, ",")));
}
}
if (repr instanceof TableFormBase)
{
_logger.info("Table");
Decoration deco = ((TableFormBase)repr).getBodyTableDecoration();
attr = deco.getAttributes();
}
}
else
{
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid[i]);
Decoration   deco = ((cellInfo.m_tableCell != null) ? (Decoration) cellInfo.m_tableCell : (Decoration) cellInfo.m_cell);
Font         font = deco.getFont();
attr = deco.getAttributes();
if (fontName != null)
font.setName(fontName);
if (fontSize != null)
font.setSize(Integer.parseInt(fontSize));
if (bold != null)
font.setStyle(buildFontStyle(bold.equals("1"), isItalic(font), isUnder(font), isStrike (font)));
if (italic != null)
font.setStyle(buildFontStyle(isBold(font) , italic.equals("1"), isUnder(font), isStrike (font)));
if (under != null)
font.setStyle(buildFontStyle(isBold(font), isItalic(font), under.equals("1"), isStrike (font)));
if (align != null)
deco.getAlignment().setHorizontal(align.equals("0") ? HAlignmentType.LEFT : (align.equals("1") ? HAlignmentType.CENTER : HAlignmentType.RIGHT));
if (valign != null)
deco.getAlignment().setVertical(valign.equals("0") ? VAlignmentType.TOP : (valign.equals("1") ? VAlignmentType.CENTER : VAlignmentType.BOTTOM));
if (wraptext != null)
deco.getAlignment().setWrapText( wraptext.equals("1") );
if (bg!=null) {
attr.setBackground( ViewerTools.toColor(ViewerTools.split(bg, ",")));
attr.setBackgroundImageURL(null);
}
if (fg!=null)
{
attr.setForeground( ViewerTools.toColor(ViewerTools.split(fg, ",")));
}
}
if ( attr != null )
{
if ((lb != null) || (bb!=null) || (rb != null) || (tb != null) ) 
{
_logger.info("lb="+rb+" bb="+bb+" rb="+rb+" tb="+tb);
Border border = (Border) attr.getBorder();
if (border instanceof SimpleBorder)
{
if ( lb.equals(bb) && bb.equals(rb) && rb.equals(tb) )
{
SimpleBorder sb = (SimpleBorder)border;
if ( Integer.parseInt(lb) != -1 )
sb.setSize(BorderSize.fromInt(Integer.parseInt(lb)));
}
else
{
ComplexBorder cb = ((SimpleBorder) border).toComplex();
if ( Integer.parseInt(lb) != -1 )
cb.getLeft().setSize( BorderSize.fromInt(Integer.parseInt(lb)) );
if ( Integer.parseInt(rb) != -1 )
cb.getRight().setSize( BorderSize.fromInt(Integer.parseInt(rb)) );
if ( Integer.parseInt(tb) != -1 )
cb.getTop().setSize( BorderSize.fromInt(Integer.parseInt(tb)) );
if ( Integer.parseInt(bb) != -1 )
cb.getBottom().setSize( BorderSize.fromInt(Integer.parseInt(bb)) );
}
}
if (border instanceof ComplexBorder)
{
ComplexBorder cb = (ComplexBorder)border;
if ( Integer.parseInt(lb) != -1 )
cb.getLeft().setSize( BorderSize.fromInt(Integer.parseInt(lb)) );
if ( Integer.parseInt(rb) != -1 )
cb.getRight().setSize( BorderSize.fromInt(Integer.parseInt(rb)) );
if ( Integer.parseInt(tb) != -1 )
cb.getTop().setSize( BorderSize.fromInt(Integer.parseInt(tb)) );
if ( Integer.parseInt(bb) != -1 )
cb.getBottom().setSize( BorderSize.fromInt(Integer.parseInt(bb)) );
}
}
if ( bc != null )
{
Border border = (Border) attr.getBorder();
if (border instanceof SimpleBorder)
{
SimpleBorder sb = (SimpleBorder) border;
sb.setColor( ViewerTools.toColor(ViewerTools.split(bc, ",")));
}
if (border instanceof ComplexBorder)
{
ComplexBorder cb = (ComplexBorder) border;
cb.getLeft().setColor( ViewerTools.toColor(ViewerTools.split(bc, ",")));
cb.getRight().setColor( ViewerTools.toColor(ViewerTools.split(bc, ",")));
cb.getTop().setColor( ViewerTools.toColor(ViewerTools.split(bc, ",")));
cb.getBottom().setColor( ViewerTools.toColor(ViewerTools.split(bc, ",")));
}
}
}
}
doc.applyFormat();
String strRedirectTo = "";
if (bMerge == null)
{
    strEntry = doc.getStorageToken();
    objUtils.setSessionStorageToken(strEntry, strViewerID, session);
    String strQueryString = requestWrapper.getQueryString();
    int iPos = strQueryString.indexOf("&bids");
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
requestWrapper.setQueryParameter("iViewerID",strViewerID);
requestWrapper.setQueryParameter("iReport",iReport);
requestWrapper.setQueryParameter("bMerge",bMerge);
requestWrapper.setQueryParameter("bids",bids);
}
_logger.info("<--processFormatToolbar.jsp");
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
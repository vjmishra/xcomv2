<%@ include file="incStartpage.jsp" %>
<%@ include file="incWomFunctions.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
    _logger.info("--> incSectionWom");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
    String sBid = requestWrapper.getQueryParameter("sBid", true);
    String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
    DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
    int nReportIndex = Integer.parseInt(iReport);
    _logger.info("bid=" + sBid);
    ReportElementContainer rs= doc.getStructure();
    ReportContainer        report= (ReportContainer) rs.getChildAt(nReportIndex);
    SectionContainer       section = (SectionContainer) report.getReportElement(sBid);
    if (section != null) {
    _logger.info("section" + section);
    StringBuffer sbToOut = new StringBuffer();         
    printSection(sbToOut, section, _logger);
    out.println(sbToOut.toString()); 
    }
    _logger.info("<-- incSectionWom");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>
<%!
  void printSection(StringBuffer out, SectionContainer section, DHTMLLogger _logger)
  throws IOException
  {
      StringBuffer s = new StringBuffer("var varSection = section(");
      ReportCell[] reportCells = section.getSectionCells();
      String sectionName = "";
      for (int i = 0; i < reportCells.length; i++) {
          ReportCell rc = reportCells[i];
          if (rc.isSection()) {
             sectionName = rc.getExpr().getName();
          }
      }
      s.append( "\"" + ViewerTools.escapeQuotes(sectionName) + "\",");
      s.append(((((Position) section).getUnit().value() == UnitType._INCH )? "1" : "0") + ","); 
      s.append((section.isInIndex()? "1" : "0") + ","); 
      Attributes atts = section.getAttributes();
      s.append(getColorString(atts.getBackground()) + ",");
      String skin = (atts.getSkin() != null) ? atts.getSkin().getName() :"";
      s.append("\"" + skin + "\",");
      String imgURL = (atts.getBackgroundImageURL() != null)? atts.getBackgroundImageURL() : "";
      s.append("\"" + imgURL + "\",");
      s.append(atts.getBackgroundImageDisplayMode().value() + ",");
      Alignment align = section.getBackgroundAlignment();
      s.append(align.getHorizontal().value() + ",");
      s.append(align.getVertical().value() + ",");
      s.append((section.startOnNewPage()? "1" : "0") + ",");
      s.append((section.avoidPageBreak()? "1" : "0") + ",");
      s.append(((Position) section).getX() + ",");
      s.append(((Position) section).getY() + ",");
      String embimg = (atts.getBackgroundImageResource() != null) ? "true" : "false";
      s.append( embimg + ");");
      out.append(s);
  }
  void printSectionSkins(StringBuffer out, DocumentInstance doc, ReportEngine objReportEngine)
  throws IOException
  {
      StringBuffer skins = new StringBuffer("doc.reportSkins = new Array;");
      Skin[] sectionSkins = objReportEngine.getBackgroundSkins(SkinReportElementType.SECTION) ;
      skins.append("doc.sectionSkins = new Array;");
      for (int i = 0; i < sectionSkins.length; i++)
      {
          skins.append("doc.sectionSkins[doc.sectionSkins.length] = \"" + ViewerTools.escapeQuotes(((Skin) sectionSkins[i]).getName()) + "\";");
      }
      out.append(skins);
  }
%>
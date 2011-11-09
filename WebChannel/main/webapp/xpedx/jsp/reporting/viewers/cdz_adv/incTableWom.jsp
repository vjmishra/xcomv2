<%@ include file="incStartpage.jsp" %>
<%@ include file="incWomFunctions.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
boolean bIncTableWomOK = false;
try
{
    _logger.info("--> incTableWom");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
    String sBid = requestWrapper.getQueryParameter("sBid", true);
    String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
    String[] arrNodes = ViewerTools.split(iReport,"-");
    DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
    int nReportIndex = Integer.parseInt(iReport);
    _logger.info("bid=" + sBid);
    ReportElementContainer rs= doc.getStructure();
    ReportContainer     report= (ReportContainer) rs.getChildAt(nReportIndex);
    CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, sBid);
    ReportBlock block = cellInfo.m_block;
    if (block != null) {
    _logger.info("block: " + block);
    StringBuffer sbToOut = new StringBuffer();      
    printReportBlockForTable(sbToOut, block, _logger);
    out.println(sbToOut.toString());  
    bIncTableWomOK = true;
    }
    out.println("bIncTableWomOK=" + bIncTableWomOK) ;
    _logger.info("<-- incTableWom");
}
catch(Exception e)
{
out.println("bIncTableWomOK=false");
objUtils.incErrorMsg(e, out);
}
%>
<%!
    void printReportBlockForTable(StringBuffer out, ReportBlock block, DHTMLLogger _logger)
    throws IOException
    {
        Representation repr = block.getRepresentation();
        BlockType blockType = repr.getType();
printBlock(out, block, repr, _logger);
        if (blockType instanceof TableType) {
            printTableForm(out, (TableFormBase) repr);
            if ((blockType == TableType.VTABLE) || (blockType == TableType.HTABLE)) {
                printTable(out, (Table) repr);
          if (blockType == TableType.VTABLE) { 
                out.append("varTable.showEmptyRows=" + (((SimpleTable) repr).isShowEmptyRows()? "1" : "0") + ";");
                } else {
                  out.append("varTable.showEmptyCols=" + (((SimpleTable) repr).isShowEmptyRows()? "1" : "0") + ";");
          }
            }
            if (blockType == TableType.FORM) {
            out.append("var varTable = null;");
          out.append("varTableForm.showEmptyFields=" + (((Form) repr).isShowEmptyFields()? "1" : "0") + ";");
    }
            if (blockType == TableType.XTABLE) {
            printTable(out, (Table) repr);
       out.append("varTable.showEmptyRows=" + (((CrossTable) repr).isShowEmptyRows()? "1" : "0") + ";");
        out.append("varTable.showEmptyCols=" + (((CrossTable) repr).isShowEmptyColumns()? "1" : "0") + ";");
      out.append("varTable.showLeftHeader=" + (((CrossTable) repr).isLeftHeaderVisible()? "1" : "0") + ";");
        out.append("varTable.showTopHeader=" + (((CrossTable) repr).isTopHeaderVisible()? "1" : "0") + ";");        
        out.append("varTable.showObjectNames=" + (((CrossTable) repr).isExtraHeaderVisible()? "1" : "0") + ";");                        
            }
        }
    }
    void printTableForm(StringBuffer out, TableFormBase tableForm)
    throws IOException
    {
        StringBuffer s = new StringBuffer("var varTableForm = tableForm(");
        s.append(((tableForm.getUnit().value()== UnitType._INCH )? "1" : "0") + ","); 
        s.append(tableForm.getCellSpacing() + ",");
        s.append(tableForm.getAlternateColorFrequency() + ",");
        s.append(getColorString(tableForm.getAlternateColor()) + ",");
        Decoration deco = tableForm.getBodyTableDecoration();
        Attributes atts = deco.getAttributes();
        s.append(getColorString(atts.getBackground()) + ",");
        String skin = (atts.getSkin() != null) ? atts.getSkin().getName() :"";
        s.append("\"" + skin + "\",");
        String imgURL = (atts.getBackgroundImageURL() != null)? atts.getBackgroundImageURL() : "";
        s.append("\"" + imgURL + "\",");
        s.append(((atts.getSkin() != null) ? atts.getSkin().getDisplayMode().value() : atts.getBackgroundImageDisplayMode().value()) + ",");
        Alignment align = deco.getBackgroundAlignment();
        s.append(align.getHorizontal().value() + ",");
        s.append(align.getVertical().value() + ",");
        String embimg = (atts.getBackgroundImageResource() != null) ? "true" : "false";
s.append( embimg + ");");
        out.append(s);
    }
    void printTable(StringBuffer out, Table table)
    throws IOException
    {
        StringBuffer s = new StringBuffer("var varTable = table(");
        s.append((table.isHeaderVisible()? "1" : "0") + ",");
        s.append((table.isFooterVisible()? "1" : "0") + ",");
        s.append((table.isHeaderOnEveryPage()? "1" : "0") + ",");
        s.append((table.isFooterOnEveryPage()? "1" : "0") + ");");
        out.append(s);
      }
%>
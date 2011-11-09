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
_logger.info("\n-->processFilterMap.jsp");
String strEntry    = requestWrapper.getQueryParameter("sEntry", true);
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
String sBid        = requestWrapper.getQueryParameter("sBid", false);
String sSrcBid     = requestWrapper.getQueryParameter("srcBid", false);
String filtBid       = requestWrapper.getQueryParameter("filtBid", false);    
String command     = requestWrapper.getQueryParameter("command", true);
String filter      = requestWrapper.getQueryParameter("filter", false);
String iReport     = requestWrapper.getQueryParameter("iReport", true, "0");
String iReportTarget     = requestWrapper.getQueryParameter("targetReportIndex", false, "0");
    int nReportIndex = Integer.parseInt(iReport);
int nReportTargetIndex = Integer.parseInt(iReportTarget);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
ReportStructure rs = doc.getStructure();
ReportContainer rc = (ReportContainer)rs.getReportElement( nReportIndex );
ReportContainer rcDest = null;
FilterContainer filterContainer = null;
String targetBid =  null;
ReportElement srcElem = null;
ReportElement targetElem = null;
switch(command.charAt(0))
{
case '0' : 
_logger.info("remove Filter bid=" + sBid);
ReportElement elemToRm = rc.getReportElement(sBid);
if (elemToRm != null) {
rmFilter(elemToRm, _logger);
}
break;
case '1' : 
_logger.info("substitute Filter " + filter + " of bid=" + sBid);
ReportElement elemToModify = rc.getReportElement(sBid);
_logger.info("add Filter to elem:" + elemToModify);
if (elemToModify != null) {
if (elemToModify instanceof ReportBody) {
elemToModify = (ReportElement) elemToModify .getParent();
}
FilterDecoder filterDecoder = new FilterDecoder(doc.getDictionary());
filterContainer = ((Filterable) elemToModify).createFilter(LogicalOperator.AND); 
filterDecoder.decode(filterContainer, ViewerTools.convertStringToArray(filter));
}
break;
case '2' : 
targetBid = sBid;
_logger.info("move Filter " + filter + " of bid=" + sSrcBid + " to bid= " + targetBid);
srcElem = rc.getReportElement(sSrcBid);
rcDest = (ReportContainer)rs.getReportElement( nReportTargetIndex );
targetElem = rcDest.getReportElement(targetBid);
if ((srcElem != null) && (targetElem != null)) {
copyFilter(srcElem, targetElem, _logger);
rmFilter(srcElem, _logger);
}
break;
case '3' : 
targetBid = sBid;
_logger.info("move Filter " + filter + " of bid=" + sSrcBid + " to bid= " + targetBid);
srcElem = rc.getReportElement(sSrcBid);
rcDest = (ReportContainer)rs.getReportElement( nReportTargetIndex );
targetElem = rcDest.getReportElement(targetBid);
if ((srcElem != null) && (targetElem != null)) {
copyFilter(srcElem, targetElem, _logger);
}
break;
case '4' : 
ReportElement filterElem = rc.getReportElement(filtBid);
filterContainer = ((Filterable) filterElem).getFilter();
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, sBid);
ReportExpression filterByExpr = cellInfo.m_expr;
_logger.info("Exp to remove:" + filterByExpr);
if (filterContainer.getOperator() == LogicalOperator.AND)
{
int count = filterContainer.getChildCount();
_logger.info("Filter:" + filterContainer + ", nb children=" + count);
for (int i = 0; i < count; i++)
{
FilterConditionNode node = filterContainer.getFilterConditionNode(i); 
_logger.info("node:" + node);
if (node instanceof FilterObject)
{
    FilterObject filterNode = (FilterObject) node;
    if (getExprIdOrFormula(filterNode.getExpr()).equals(getExprIdOrFormula(filterByExpr)))
    {
        filterContainer.remove(node);
        break;
    }
}
}
}                        
    _logger.info("Filter after partial remove:" + filterContainer);
break;
}
doc.applyFormat();
filterContainer = null;
srcElem = null;
targetElem = null;
_logger.info("<--processFilterMap.jsp\n");
strEntry = doc.getStorageToken();
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
String strQueryString = requestWrapper.getQueryString();
int iPos = strQueryString.indexOf("&sBid");
if (iPos>=0)
strQueryString = strQueryString.substring(0, iPos);
strQueryString = ViewerTools.updateQueryParameter(strQueryString, "sEntry", strEntry);
requestWrapper.setQueryString(strQueryString);
requestWrapper.setQueryParameter("sFollowBid", sBid);
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
catch(Exception e)
{
out.clearBuffer();
response.setContentType("text/html;charset=UTF-8");
out.println("<html><head><script language=\"javascript\">");
out.println("function okCB() {");
out.println("   var topf=getTopViewerFrameset();");
out.println("   if (topf!=null) topf.restoreAfterError();");
out.println("}");
out.println("</script>");
objUtils.displayErrorMsg(e, "_ERR_FILTER_MAP", false, "okCB", out, session);
}
%>
<%! 
void subCopyFilter(FilterContainer old, FilterContainer src, DHTMLLogger _logger)
{
_logger.info("old FilterContainer: " + old);
int count = src.getChildCount();
for (int i=0; i < count; i++)
{
if (src.getChildAt(i) instanceof FilterContainer) {
_logger.info("FilterContainer: " + src.getChildAt(i));
subCopyFilter(((FilterContainer) old.createFilterConditionContainer(((FilterContainer) src.getChildAt(i)).getOperator())), ((FilterContainer) src.getChildAt(i)), _logger);
} else if (src.getChildAt(i) instanceof FilterObject) {
_logger.info("FilterObject: " + src.getChildAt(i));
old.copyFilterObject((FilterObject) src.getChildAt(i));
}
}
}
void copyFilter(ReportElement srcElem, ReportElement targetElem, DHTMLLogger _logger)
{
if (targetElem instanceof ReportBody) {
targetElem = (ReportElement) targetElem.getParent();
}
if (srcElem instanceof ReportBody) {
srcElem = (ReportElement) srcElem.getParent();
}
FilterContainer srcFilterContainer = null;
if ((srcElem instanceof Filterable) && ((Filterable) srcElem).hasFilter()) {
srcFilterContainer = ((Filterable) srcElem).getFilter();
}
_logger.info("src filter:" + srcFilterContainer);
FilterContainer oldFilter = null;
FilterContainer filterContainer = null;
    if ((targetElem instanceof Filterable) && ((Filterable) targetElem).hasFilter())
  {
    oldFilter = ((Filterable) targetElem).getFilter();
_logger.info("filter before was not empty:" + oldFilter);
    if (oldFilter.getOperator() != srcFilterContainer.getOperator())
{
_logger.info("DIFFERENT OPERATORS : " + oldFilter.getOperator() + " " + srcFilterContainer.getOperator());
filterContainer = ((Filterable) targetElem).createFilter(LogicalOperator.AND);
_logger.info("root node is created : " + filterContainer);
filterContainer.copyFilterContainer(oldFilter);
_logger.info("oldFilter has been copied  : " + oldFilter + " in filterContainer : " + filterContainer);
filterContainer.copyFilterContainer(srcFilterContainer);
_logger.info("srcFilterContainer has been sub copied  : " + srcFilterContainer + " in filterContainer : " + filterContainer);
        } else {
_logger.info("subCopyFilter oldFilter  : " + oldFilter + " in srcFilterContainer : " + srcFilterContainer);
subCopyFilter(oldFilter, srcFilterContainer, _logger);
        }
_logger.info("filters after copy: " + ((Filterable) targetElem));
    } else {
((Filterable) targetElem).copyFilter(srcFilterContainer);
_logger.info("copy over empty filter:" + ((Filterable) targetElem));
    }
}
void rmFilter(ReportElement elemToRm, DHTMLLogger _logger)
{
if (elemToRm instanceof ReportBody) {
elemToRm = (ReportElement) elemToRm.getParent();
}
_logger.info("rm filter: " + ((Filterable) elemToRm)+ " from elem: " + elemToRm);
if ((elemToRm instanceof Filterable) && ((Filterable) elemToRm).hasFilter()) {
((Filterable) elemToRm).removeFilter();
}
}
String getExprIdOrFormula(ReportExpression expr)
{
if (expr == null)
return null;
if (expr instanceof FormulaExpression)
return ((FormulaExpression)expr).getValue();
return expr.getID();
}
%>
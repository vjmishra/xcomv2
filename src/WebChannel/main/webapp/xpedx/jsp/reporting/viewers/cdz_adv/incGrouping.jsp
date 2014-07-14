<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
try
{
_logger.info("-->incGrouping.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true); 
    DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("strEntry = " + strEntry );
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
    String strReport = requestWrapper.getQueryParameter("iReport", false, "0");
    int nReportIndex = Integer.parseInt(strReport);
    String bid = requestWrapper.getQueryParameter("sBid", true);
_logger.info("bid = " + bid);    
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
String expr = cellInfo.m_expr.getName();
String sVarName = requestWrapper.getQueryParameter("sVarName",false,"");
if (sVarName.equals(""))
sVarName = expr+"+";
_logger.info("sVarName = " + sVarName);
out.println("var sDefaultVarName = '" + ViewerTools.escapeQuotes(sVarName) + "'");
_logger.info("<--incGrouping.jsp");
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_FORMULA_VARIABLE", true, out, session);
}
%>
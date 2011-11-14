<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incCustomSort.jsp");
    String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
int nReportIndex = Integer.parseInt(iReport);
_logger.info("strEntry = " + strEntry );
String bid = requestWrapper.getQueryParameter("sBid", false, "");
String ID = requestWrapper.getQueryParameter("sID", false, "");
ReportExpression re = null;
out.println("var aCustomSortLOV = new Array");
out.println("var aTmpVals = new Array");
out.println("var isCustomSortDefined = false");
out.println("var canApplyCustomSort = true");
if (!ID.equals("")) {
_logger.info("use ID = " + ID );
ReportDictionary rd = doc.getDictionary();
re = rd.getChild(ID);
} else {
_logger.info("use bid = " + bid );
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bid);
re = cellInfo.m_expr;
}
if (re != null) {
_logger.info("Try to get custom sort on report expression:" + re );
out.println("_expID = \"" + re.getName() + "\"");
out.println("_qualif=p." +  ViewerTools.getWomType(re.getQualification()));
SortInfo sortInfo = null;
if (re instanceof DPExpression) {
sortInfo = ((DPExpression) re).getSortInfo();
} else if (re instanceof VariableExpression) {
sortInfo = ((VariableExpression) re).getSortInfo();
} else if (re instanceof Link) {
sortInfo = ((Link) re).getSortInfo();
}
String sIsCustomSortDefined = Boolean.toString(sortInfo.isCustomSortDefined());
out.println("isCustomSortDefined = " + sIsCustomSortDefined);
_logger.info("isCustomSortDefined :" + sIsCustomSortDefined);
if (sortInfo.canApplyCustomSort()) {
out.println("canApplyCustomSort = true");
_logger.info("canApplyCustomSort : true");
CustomSortLov csLOV = sortInfo.getCustomSortLov();
CustomSortValues valuesCS = csLOV.getAllValues();
_logger.info("Custom Sort Values #:" + valuesCS.getCount());
int k=0;
for (int i=0; i < valuesCS.getCount(); i++) {
out.println("aCustomSortLOV[" + i + "] = \"" + ViewerTools.escapeQuotes(valuesCS.getValue(i)) + "\"");
if (valuesCS.isValueUserDefined(i)) 
{
out.println("aTmpVals[" + k++ + "]= " + i + ";");
}
}
}
} 
out.println("var bIncCustomSortOK = true");
_logger.info("<--incCustomSort.jsp");
}
catch(CustomSortException cse)
{
out.println("var bIncCustomSortOK = false");
}
catch(ServerException cse)
{
out.println("var bIncCustomSortOK = false");
}
catch(Exception e)
{
out.println("var bIncCustomSortOK = false");
objUtils.incErrorMsg(e, out);
}
%>
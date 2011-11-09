<%@ include file="incStartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("--> incFunctions.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);      
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
StringBuffer buffer = new StringBuffer("\n");
ReportStructure reportStruct = doc.getStructure();
FunctionCategory[] fctCategs=reportStruct.getFunctionCategories();
Function[] fcts;
Function fct;
int catID;
buffer.append("p._formulaFcts=new Array;\n");
for(int catIdx=0;catIdx<fctCategs.length;catIdx++)
{
catID = fctCategs[catIdx].getID();
fcts = reportStruct.getFunctionsByCategoryID(catID);
for(int fctIdx=0;fctIdx<fcts.length;fctIdx++)
{
fct = fcts[fctIdx];
buffer.append("p.newff(p._formulaFcts,'"+ViewerTools.escapeQuotes(fct.getName())+"','"+fct.getID()+"','"+ViewerTools.escapeQuotes(fctCategs[catIdx].getName())+"','"+ViewerTools.escapeQuotes(fct.getReturnType())+"','"+ViewerTools.escapeQuotes(fct.getHint())+"','"+ViewerTools.escapeQuotes(fct.getSyntax())+"','');\n");
}
}
FormulaOperator[] fOpers=reportStruct.getOperators();
buffer.append("p._formulaOpers=new Array;\n");
for(int opIdx=0;opIdx<fOpers.length;opIdx++)
{
buffer.append("p.newfo(p._formulaOpers,'"+ViewerTools.escapeQuotes(fOpers[opIdx].getName())+"','"+fOpers[opIdx].getID()+"');\n");
}
out.println(buffer.toString());
_logger.info("<-- incFunctions.jsp");
}
catch(Exception e)
{
out.println("var incObjectsOK = false;");
objUtils.incErrorMsg(e, out);
}
%>
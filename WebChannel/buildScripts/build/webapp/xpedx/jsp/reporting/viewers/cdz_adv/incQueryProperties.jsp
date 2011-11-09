<%@ include file="incStartpage.jsp" %>
<jsp:useBean id="objQueries" class="com.businessobjects.adv_ivcdzview.ApplyQueries" scope="page" />
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incQueryProperties.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
String strDPIndex = requestWrapper.getQueryParameter("iDPIndex", false, "0");
int iDPIndex = Integer.parseInt(strDPIndex);
objQueries.printContexts(out,doc,iDPIndex);
out.println("currentDP.contexts=arrContexts;");
_logger.info("<--incQueryProperties.jsp");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>
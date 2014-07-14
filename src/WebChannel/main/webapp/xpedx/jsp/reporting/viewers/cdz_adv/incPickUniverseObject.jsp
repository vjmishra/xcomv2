<%@ include file="incStartpage.jsp" %>
<jsp:useBean id="oDS" class="com.businessobjects.adv_ivcdzview.DataSourceTools" scope="page" />
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("--> incPickUniverseObjects.jsp");
ReportEngine engine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
engine.setLocale(strLanguage);
    String univId = requestWrapper.getQueryParameter("univId", true);
DocumentInstance doc = engine.newDocument(univId); 
DataProviders dps = doc.getDataProviders();
DataProvider  dp = dps.getItem(0);
String sViewerFrameName = "window";
DataSource ds = dp.getDataSource();
doc = null;
oDS.printDataSource( out, ds, sViewerFrameName );
out.println("\n;\n var _currentUnv = unv;");
out.println("var incObjectsOK = true;");
_logger.info("<-- incPickUniverseObjects.jsp");
}
catch(Exception e)
{
out.println("var incObjectsOK = false;");
out.println(e.getMessage());
}
%>

<%@ include file="incStartpage.jsp" %>
<jsp:useBean id="oDS" class="com.businessobjects.adv_ivcdzview.DataSourceTools" scope="page" />
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incMapObject.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    _logger.info("IN strEntry = " + strEntry );
String sViewerFrameName = requestWrapper.getQueryParameter("sViewerFrameName", true);
_logger.info(" sViewerFrameName = " + sViewerFrameName );
    String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
    DocumentInstance doc = null;
    doc = (DocumentInstance)session.getAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"));        
    if (doc == null)
    {
        doc = reportEngines.getDocumentFromStorageToken(strEntry);
    }
DataProviders dps = doc.getDataProviders();
    ChangeDataSourceMapping mapping = dps.getChangeDataSourceMapping();
    if (mapping == null)
throw new Exception("VIEWER:_ERR_UNIVERSE");
DataSource ds = mapping.getNewDataSource();
oDS.printDataSource( out, ds, sViewerFrameName );
_logger.info("OUT strEntry = " + strEntry );
_logger.info("<--incMapObject.jsp");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>

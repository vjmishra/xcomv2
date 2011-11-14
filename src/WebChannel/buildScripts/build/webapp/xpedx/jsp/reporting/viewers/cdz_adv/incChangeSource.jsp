<%@ include file="incStartpage.jsp" %>
<jsp:useBean id="objApplyQueries" class="com.businessobjects.adv_ivcdzview.ApplyQueries" scope="page" />
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incChangeSource.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
    _logger.info("IN strEntry = " + strEntry );
String strViewerID = requestWrapper.getQueryParameter("iViewerID",true);
    DocumentInstance doc = null;
    doc = (DocumentInstance)session.getAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"));        
    if (doc == null)
    {
        doc = reportEngines.getDocumentFromStorageToken(strEntry);
    }
String sOldDSID = requestWrapper.getQueryParameter("sOldDSID",true);
    _logger.info("sOldDSID = " + sOldDSID );
String sNewDSID = requestWrapper.getQueryParameter("sNewDSID",true);
    _logger.info("sNewDSID = " + sNewDSID );
DataProviders dps = doc.getDataProviders();
try
{
dps.changeDataSource(sOldDSID,sNewDSID,true);
}
catch ( QueryException qe )
{
_logger.error( qe.getLocalizedMessage() );
_logger.error( qe.getCode() );
throw new Exception ("VIEWER:_ERR_CHANGE_SOURCE");
}
    ChangeDataSourceMapping mapping = dps.getChangeDataSourceMapping();
    DataSource newds = mapping.getNewDataSource();    
    out.println( "var mapArr = new Array;" );
    out.println( "var dpMap = null;" );
    for (int dpIdx=0; dpIdx<mapping.getDataProviderCount(); dpIdx++)
    {
String dpName = mapping.getDataProviderName(dpIdx);
_logger.info("dpName="+dpName);
out.println( "dpMap = newDPMapping('" + ViewerTools.escapeQuotes(dpName) + "'," + dpIdx + ");" );
ChangeDataSourceObjectMapping[] mapObj = mapping.getMappingsForDataProvider(dpIdx);
for (int idx=0; idx < mapObj.length; idx++)
{
ChangeDataSourceMappingDetail mapDetail = mapObj[idx].getMappingDetail();
String sMapDetail = "_" + mapDetail.toString();
out.print( "dpMap.add(" + dpIdx + "," + idx + "," + sMapDetail );
DataSourceObject fromObj = mapObj[idx].getFromObject();
String sFromName = ViewerTools.escapeQuotes( fromObj.getName() );
String sFromID = ViewerTools.escapeQuotes( fromObj.getID() );
String sFromQualif = ViewerTools.getWomType( fromObj.getQualification() );
String sFromType = ViewerTools.getWomDataType( fromObj.getType() );
String sFromDesc = ViewerTools.escapeQuotes( fromObj.getDescription() );
out.print(",'" + sFromName + "','" + sFromID + "',"+ sFromQualif + "," + sFromType + ",'"+ sFromDesc + "'");
DataSourceObject toObj = mapObj[idx].getToObject();
if ( toObj != null )
{
String sTrgName = ViewerTools.escapeQuotes( toObj.getName() );
String sTrgID = ViewerTools.escapeQuotes( toObj.getID() );
String sTrgQualif = ViewerTools.getWomType( toObj.getQualification() );
String sTrgType = ViewerTools.getWomDataType( toObj.getType() );
String sTrgDesc = ViewerTools.escapeQuotes( toObj.getDescription() );
out.print(",'" + sTrgName + "','" + sTrgID + "'," + sTrgQualif + "," + sTrgType + ",'"+ sTrgDesc + "'");
}
out.println( ");" );
}
out.println( "mapArr[mapArr.length] = dpMap;" );
    }
    session.setAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"), doc);
_logger.info("<--incChangeSource.jsp");
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>

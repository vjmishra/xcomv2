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
--><%@ include file="wistartpage.jsp" %>
<%
response.setDateHeader("expires", 0);
try
{
_logger.info("-->processMapObject.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry = " + strEntry );
    String strViewerID   = requestWrapper.getQueryParameter("iViewerID", true);
    DocumentInstance doc = null;
    doc = (DocumentInstance)session.getAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"));
    if (doc == null)
    {
        doc = reportEngines.getDocumentFromStorageToken(strEntry);
    }
String iReport = requestWrapper.getQueryParameter("iReport", false, "0");
int nReportIndex = Integer.parseInt(iReport);
String sDPIndex = requestWrapper.getQueryParameter("iDPIndex",false,"0");
int iDPIndex = Integer.parseInt(sDPIndex);
_logger.info("sDPIndex = " + sDPIndex );
String sMapIndex = requestWrapper.getQueryParameter("iMapIndex",false,"0");
int iMapIndex = Integer.parseInt(sMapIndex);
_logger.info("sMapIndex = " + sMapIndex );
String sTrgID = requestWrapper.getQueryParameter("trgObjID",false,"0");
_logger.info("sTrgID = " + sTrgID );
String sID = requestWrapper.getQueryParameter("id",false,"0");
int iID = Integer.parseInt( sID );
_logger.info("sID = " + sID );
DataProviders dps = doc.getDataProviders();
    ChangeDataSourceMapping mapping = dps.getChangeDataSourceMapping();
    if (mapping == null)
throw new Exception("VIEWER:_ERR_MAP_OBJECT");
DataSource ds = mapping.getNewDataSource();
DataSourceObjects dsos = ds.getClasses();
DataSourceObject newdso = dsos.getChild( sTrgID );
ChangeDataSourceObjectMapping[] arrMapObj = mapping.getMappingsForDataProvider(iDPIndex);
if ( ( iMapIndex < 0 ) || ( iMapIndex > arrMapObj.length ) )
throw new Exception("VIEWER:_ERR_MAP_OBJECT");
ChangeDataSourceObjectMapping mapObj = arrMapObj[iMapIndex];
mapObj.setToObject( newdso );
String sMapDetail = "_" + mapObj.getMappingDetail().toString();
String sTrgName = ViewerTools.escapeQuotes( newdso.getName() );
String sTrgQualif = ViewerTools.getWomType( newdso.getQualification() );
    session.setAttribute(ViewerTools.getSessionVariableKey(strViewerID + ".DocInstance"), doc);
_logger.info("<--processMapObject.jsp");
out.clearBuffer();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script language="javascript" src="scripts/CommonWom.js"></script>
<script language="javascript" src="language/<%=(String)session.getAttribute("CDZ.Language")%>/scripts/dataSourceMapping.js"></script>
<script language="javascript">
function loadCB()
{
var f=parent.getTopFrameset()
if (f)
f=f.getFrame("ThirdDlgFrame")
if (f)
    f.updateChangeSourceDialog(null,<%=iID%>,<%=sTrgQualif%>,'<%=sTrgName%>',<%=sMapDetail%>)
}
</script>
</head>
<body>
</body>
<script language="javascript">setTimeout("loadCB()",1)</script>
</html>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_MAP_OBJECT", true, out, session);
}
%>
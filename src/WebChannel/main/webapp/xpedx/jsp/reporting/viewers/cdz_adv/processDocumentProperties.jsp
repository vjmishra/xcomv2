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
try
{
_logger.info("-->processDocumentProperties.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry",true);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
_logger.info("strEntry = " + strEntry );
String strViewerID = requestWrapper.getQueryParameter("iViewerID",true);
Properties props = doc.getProperties();
String sbQueryDrillOnly = requestWrapper.getQueryParameter("bQueryDrillOnly",false,"false");
if ( sbQueryDrillOnly.equals("true") )
{
String sbUseQueryDrill = requestWrapper.getQueryParameter("bUseQueryDrill",false,"false");
props.setProperty( PropertiesType.DATABASE_CONNECTED_DRILL, sbUseQueryDrill );
_logger.info("PropertiesType.DATABASE_CONNECTED_DRILL="+sbUseQueryDrill);
try
{
doc.setProperties( props );
}
catch(ServerException e)
{
int errCode = e.getErrorCode();
_logger.info("ServerException errCode = " + errCode);
if ( errCode == 30372 ) 
{
%>
<html>
<head>
<script language="javascript">
parent.showErrorMsg()
</script>
</head>
</html>
<%
}
}
_logger.info("<--processDocumentProperties.jsp");
}
else
{
String tmp = requestWrapper.getQueryParameter("bEnhanced",false,"false");
props.setProperty( PropertiesType.ENHANCED_VIEWING, tmp );
_logger.info("PropertiesType.ENHANCED_VIEWING (bEnhanced)="+tmp);
tmp = requestWrapper.getQueryParameter("bRefreshOnOpen",false,"false");
props.setProperty( PropertiesType.REFRESH_ON_OPEN, tmp );
_logger.info("PropertiesType.REFRESH_ON_OPEN (bRefreshOnOpen)="+tmp);
tmp = requestWrapper.getQueryParameter("bUseQueryDrill",false,"false");
props.setProperty( PropertiesType.DATABASE_CONNECTED_DRILL, tmp );
_logger.info("PropertiesType.DATABASE_CONNECTED_DRILL (bUseQueryDrill)="+tmp);
tmp = requestWrapper.getQueryParameter("bMergeDimensions",false,"false");
props.setProperty( PropertiesType.MERGE_DIMENSION, tmp );
_logger.info("PropertiesType.MERGE_DIMENSION (bMergeDimensions)=" + tmp);
doc.setProperties( props );
_logger.info("props = " + props);
tmp = requestWrapper.getQueryParameter("bRegionalFormatting",false,"false");
boolean bRegionalFormatting = (new Boolean(tmp)).booleanValue();
if ( bRegionalFormatting )
{
doc.setFormattingLocale( DocumentLocaleType.BASELOCALE );
_logger.info("Formatting Locale set to DocumentLocaleType.BASELOCALE");
}
else
{
doc.setFormattingLocale( DocumentLocaleType.LOCALE );
_logger.info("Formatting Locale set to DocumentLocaleType.LOCALE");
}
doc.applyFormat();
strEntry = doc.getStorageToken();
_logger.info("strEntry = " + strEntry );
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
_logger.info("<--processDocumentProperties.jsp");
out.clearBuffer();
%>
<jsp:forward page="report.jsp"/>
<%
}
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_DOC_PROPERTIES", true, out, session);
}
%>
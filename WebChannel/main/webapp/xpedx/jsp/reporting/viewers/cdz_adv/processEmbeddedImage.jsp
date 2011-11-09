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
<%@ page import="org.apache.commons.fileupload.*,
org.apache.commons.fileupload.disk.*,
org.apache.commons.fileupload.servlet.*,
com.crystaldecisions.sdk.plugin.CeProgID,
com.businessobjects.sdk.plugin.desktop.webintelligence.IWebIntelligence;"%>
<%
response.setDateHeader("expires", 0);
if (!isAlive) 
{
_logger.info("-->isAlive");
return;
}
try
{
_logger.info("-->processEmbeddedImage.jsp");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry IN = " + strEntry );
String strViewerID = requestWrapper.getQueryParameter("iViewerID", true);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
String iReport = requestWrapper.getQueryParameter("iReport", true, "0");
int nReportIndex = Integer.parseInt(iReport);
String sREType = requestWrapper.getQueryParameter("sREType",false);
_logger.info("sREType = " + sREType);
ReportElementContainer rs = doc.getStructure();
ReportContainer report = (ReportContainer) rs.getChildAt(nReportIndex);
FileItemFactory factory = new DiskFileItemFactory();
ServletFileUpload upload = new ServletFileUpload(factory);
List items = upload.parseRequest(request);
String fileName = null;
long sizeInBytes = 0;
boolean uploadOk = false;
Iterator iter = items.iterator();
while ( iter.hasNext() ) 
{
FileItem item = (FileItem) iter.next();
if ( !item.isFormField() ) 
{
fileName = item.getName();
if ( !isExtensionOk( fileName ) )
{
_logger.error("Incorrect extension");
throw new Exception("VIEWER:_ERR_UPLOADFILE_INCORRECT_EXTENSION");
}
_logger.info("File name = " + fileName);
sizeInBytes = item.getSize();
int maxSize = getMaxFileSizeInBytes( entSession , _logger );
if (sizeInBytes==0)
{
_logger.error("Incorrect file name.");
throw new Exception("VIEWER:_ERR_UPLOADFILE_INCORRECT_FILENAME");
}
if ( sizeInBytes > maxSize )
{
_logger.error("Incorrect size " + sizeInBytes);
throw new Exception("VIEWER:_ERR_UPLOADFILE_INCORRECT_SIZE");
}
_logger.info("File size = " + sizeInBytes);
ResourceManager rmgr = doc.getResourceManager();
InputStream inFile = item.getInputStream();
String rID = rmgr.addResource( item.getContentType(), (int)sizeInBytes, null, null, inFile );
if ( rID == null )
{
_logger.error("addResource failed (rID is null)");
throw new Exception("VIEWER:_ERR_UPLOADFILE");
}
_logger.info("Resource ID = " + rID);
String sBids = requestWrapper.getQueryParameter("bids", false);
_logger.info("sBids = " + sBids);
if ( sBids == null )
sBids = requestWrapper.getQueryParameter("sBid", false);
_logger.info("sBids = " + sBids);
String[] bids = ViewerTools.split(sBids,",");
for (int i = 0; i < bids.length; i++) 
{
Attributes atts = null;
if ( sREType.equals("cell") )
{
CurrentCellInfo cellInfo = new CurrentCellInfo(doc, nReportIndex, bids[i]);
Decoration deco = ((cellInfo.m_tableCell != null) ? (Decoration) cellInfo.m_tableCell : (Decoration) cellInfo.m_cell);
if ( (cellInfo.m_tableCell == null ) && ( cellInfo.m_cell == null ) )
{
_logger.error("Invalid ReportElement");
throw new Exception("VIEWER:_ERR_UPLOADFILE");
}
atts = deco.getAttributes();
}
else
{
ReportElement re = report.getReportElement( bids[i] );
if ( re == null )
{
_logger.error("No ReportElement associated with BID = " + bids[i]);
throw new Exception("VIEWER:_ERR_UPLOADFILE");
}
if ( sREType.equals("section") )
{
if ( !(re instanceof SectionContainer) )
{
_logger.error("Invalid ReportElement");
throw new Exception("VIEWER:_ERR_UPLOADFILE");
}
Decoration deco = (Decoration) re;
atts = deco.getAttributes();
}
if ( sREType.equals("table") )
{
if ( !(re instanceof ReportBlock) )
{
_logger.error("Invalid ReportElement");
throw new Exception("VIEWER:_ERR_UPLOADFILE");
}
ReportBlock block = (ReportBlock) re;
TableFormBase tableForm = (TableFormBase) block.getRepresentation();
Decoration deco = tableForm.getBodyTableDecoration();
atts = deco.getAttributes();
}
if ( sREType.equals("reportHeader") )
{
if ( !(re instanceof ReportContainer) )
{
_logger.error("Invalid ReportElement");
throw new Exception("VIEWER:_ERR_UPLOADFILE");
}
ReportContainer rc = (ReportContainer) re;
PageHeaderFooter head = rc.getPageHeader();
atts = head.getAttributes();
}
if ( sREType.equals("reportBody") )
{
if ( !(re instanceof ReportContainer) )
{
_logger.error("Invalid ReportElement");
throw new Exception("VIEWER:_ERR_UPLOADFILE");
}
ReportContainer rc = (ReportContainer) re;
ReportBody rbody = rc.getReportBody();
atts = rbody.getAttributes();
}
if ( sREType.equals("reportFooter") )
{
if ( !(re instanceof ReportContainer) )
{
_logger.error("Invalid ReportElement");
throw new Exception("VIEWER:_ERR_UPLOADFILE");
}
ReportContainer rc = (ReportContainer) re;
PageHeaderFooter foot = rc.getPageFooter();
atts = foot.getAttributes();
}
}
if ( atts == null )
{
_logger.error("No Attributes");
throw new Exception("VIEWER:_ERR_UPLOADFILE");
}
atts.setBackgroundImageResource( rID );
_logger.info("setBackgroundImageResource( " + rID + " ) done");
 }
uploadOk = true;
}
}
doc.applyFormat();
strEntry = doc.getStorageToken();
_logger.info("strEntry OUT = " + strEntry );
objUtils.setSessionStorageToken(strEntry, strViewerID, session);
requestWrapper.setQueryParameter("sEntry", strEntry);
_logger.info("<--processEmbeddedImage.jsp");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="javascript">
parent.updateEntry(<%=uploadOk%>,"<%=strEntry%>")
</script>
</head>
<body></body>
</html>
<%
}
catch(Exception e)
{
objUtils.displayErrorMsg(e, "_ERR_UPLOADFILE", true, out, session);
}
%>
<%!
private boolean isExtensionOk(String fileName)
{
int lastDot = fileName.lastIndexOf(".");
if ( lastDot == -1 )
return false;
String ext = fileName.substring( lastDot );
ext = ext.trim();
ext = ext.toUpperCase();
if ( !ext.equals(".PNG") 
&& !ext.equals(".JPEG") && !ext.equals(".JPG") 
&& !ext.equals(".GIF")
&& !ext.equals(".BMP") )
return false;
return true;
}
private boolean isMimeTypeOk(String contentType)
{
if ( !contentType.equals("image/png") 
&& !contentType.equals("image/jpeg") && !contentType.equals("image/pjpeg")
&& !contentType.equals("image/gif")  
&& !contentType.equals("image/bmp")  && !contentType.equals("image/x-windows-bmp") )
return false;
return true;
}
private int getMaxFileSizeInBytes(IEnterpriseSession entSession, DHTMLLogger _logger)
throws SDKException
{
int DefaultMaxEmbeddedImageSize = 1024 * 1024;
String WEBI_EMBEDDED_IMAGE_SIZE = "WEBI_EMBEDDED_IMAGE_SIZE";
if (entSession == null)
return DefaultMaxEmbeddedImageSize;
IInfoStore infoStore = (IInfoStore) entSession.getService("InfoStore");
String query = "SELECT SI_WEBI_APPLICATION_PROPERTIES FROM CI_APPOBJECTS WHERE SI_PARENTID = 99 AND SI_PROGID = '" + CeProgID.WEBINTELLIGENCE + "'";
IInfoObjects infoObjects = infoStore.query( query );
if ( infoObjects.size()<=0 )
{
_logger.error("Could not retrieve WebIntelligence InfoObject");
return DefaultMaxEmbeddedImageSize;
}
IWebIntelligence infoObject = (IWebIntelligence) infoObjects.get( 0 );
int maxSize = DefaultMaxEmbeddedImageSize;
try
    {
    String val = infoObject.getProperty( WEBI_EMBEDDED_IMAGE_SIZE ); 
    _logger.info("Value retrieved from CMS  = " + val);
    maxSize = Integer.parseInt( val );
    _logger.info("maxSize = " + maxSize);  
    }
    catch (SDKException.PropertyNotFound e1)
    {
    infoObject.setProperty( WEBI_EMBEDDED_IMAGE_SIZE, (new Integer(DefaultMaxEmbeddedImageSize/1024)).toString() );
     maxSize = DefaultMaxEmbeddedImageSize;
     _logger.info("maxSize = " + maxSize);
    }
    catch (Exception e2)
    {
infoObject.setProperty( WEBI_EMBEDDED_IMAGE_SIZE, (new Integer(DefaultMaxEmbeddedImageSize/1024)).toString() );
     maxSize = DefaultMaxEmbeddedImageSize;
     _logger.info("maxSize = " + maxSize);
    }
    _logger.info("Max Embedded Image size = " + maxSize);
    return maxSize*1024;
}
%>
%>
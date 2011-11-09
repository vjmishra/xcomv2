<%@ page language="java" import="com.businessobjects.rebean.wi.*,com.businessobjects.adv_ivcdzview.*,java.util.*"%><jsp:useBean id="requestWrapper" class="com.businessobjects.adv_ivcdzview.RequestWrapper" scope="page" />
<%
try
{
response.reset();
long now = System.currentTimeMillis();
response.setDateHeader("Expires", now + 120000);
requestWrapper.onStart(request);
requestWrapper.setCharacterEncoding("UTF-8");
String strEntry = requestWrapper.getQueryParameter("sEntry");
if (strEntry == null)
throw new Exception("Internal Error: Missing sEntry parameter.");
String strImageName = requestWrapper.getQueryParameter("name");
if ( strImageName == null )
throw new Exception("Internal Error: Missing name parameter.");
ReportEngines reportEngines = (ReportEngines)session.getAttribute(ViewerTools.SessionReportEngines);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
Image objImage = doc.getImage(strImageName);
ServletOutputStream Output = response.getOutputStream();
response.setContentType( objImage.getContentType() );
response.setContentLength(objImage.getContentLength());
objImage.getContent(Output);
Output.close();
objImage = null;
doc = null;
}
catch(Exception e)
{
e.printStackTrace();
}
%>

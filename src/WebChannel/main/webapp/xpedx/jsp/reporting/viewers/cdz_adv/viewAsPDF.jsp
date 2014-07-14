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
--><%@ page language="java" contentType="application/pdf" errorPage="errorPage4ViewPDF.jsp" import="com.businessobjects.rebean.wi.*,com.businessobjects.adv_ivcdzview.*"%><jsp:useBean id="requestWrapper" class="com.businessobjects.adv_ivcdzview.RequestWrapper" scope="page" /><%
response.reset();
requestWrapper.onStart(request);
requestWrapper.setCharacterEncoding("UTF-8");
String strEntry = requestWrapper.getQueryParameter("sEntry", true);
String strShowTabs = requestWrapper.getQueryParameter("sShowTabs", false, "false");
boolean bShowTabs = strShowTabs.equals("true") ? true : false;
String theAgent = request.getHeader("user-agent");
if (theAgent == null) theAgent = "";
if (theAgent.equalsIgnoreCase("contype"))
{
response.setHeader("Content-Type", "application/pdf");
response.setStatus(HttpServletResponse.SC_OK);
return;
}
ReportEngines reportEngines = (ReportEngines)session.getAttribute(ViewerTools.SessionReportEngines);
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
BinaryView objBinaryView = null;
Reports arrReports = null;
Report objReport = null;
if (bShowTabs)
{
String iReport = requestWrapper.getQueryParameter("iReport", true);
int nReportIndex = Integer.parseInt(iReport);
arrReports = doc.getReports();
objReport = arrReports.getItem(nReportIndex);
objReport.setPaginationMode(PaginationMode.Listing);
objBinaryView = (BinaryView)objReport.getView(OutputFormatType.PDF);
}
else
objBinaryView = (BinaryView)doc.getView(OutputFormatType.PDF);
int iLength = objBinaryView.getContentLength();
response.setContentLength(iLength);
response.setHeader("Content-Type", "application/pdf");
ServletOutputStream Output = response.getOutputStream();
objBinaryView.getContent(Output);
Output.close();
objReport = null;
arrReports = null;
objBinaryView = null;
doc = null;
%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ page import="com.reports.service.ReportData"%>
<%@ page import="com.reports.service.webi.ReportUtils"%>
<%@ page import="java.util.HashMap"%>


<s:set name='_action' value='[0]' />

<%

if(request.getParameter("viewReportAs").equals("html")) {
	ReportData reportData = (ReportData)request.getSession().getAttribute("ReportData");
	out.print(reportData.getHtmlReport());
	
}

if (request.getParameter("viewReportAs").equals("pdf")) {
	ReportData reportData = (ReportData)request.getSession().getAttribute("ReportData");
	response.setContentType("application/pdf");
	response.getOutputStream().write(reportData.getBinaryReport());
}

if (request.getParameter("viewReportAs").equals("opendocument")) {
	
	String rptId = "219528";
	HashMap<String, String> paramVals = new HashMap<String, String>();
	paramVals.put("hdn_xcomMasterCustomer:","0000578605");
	
	ReportUtils ru = new ReportUtils();
	//String finalURL = ru.(rptId, paramVals);
	//response.sendRedirect(finalURL);
}

%>
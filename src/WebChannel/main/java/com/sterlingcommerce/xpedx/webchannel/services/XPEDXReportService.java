package com.sterlingcommerce.xpedx.webchannel.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import com.reports.service.Report;
import com.reports.service.ReportCriteria;
import com.reports.service.ReportData;
import com.reports.service.ReportList;
import com.reports.service.ReportPrompt;
import com.reports.service.ReportPromptNameValue;
import com.reports.service.ReportService;
import com.reports.service.ReportServiceImplService;
import com.reports.service.ReportTypeEnum;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfs.core.YFSSystem;


public class XPEDXReportService {
	public ReportService getReportService() {
		String wcPropertiesFile = "xpedx_reporting.properties";
		XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS(wcPropertiesFile);
		String wsdlUrl= YFSSystem.getProperty("wsdlurl");
		URL url = null;
		try {
			url = new URL(wsdlUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		 
        QName qname = new QName("http://service.reports.com/", "ReportServiceImplService");
 
        ReportServiceImplService implservice = new ReportServiceImplService(url,qname);
        ReportService reportService = implservice.getReportServiceImplPort();
        return reportService;
	}
	
	
	
	public static void main (String args[]) throws IOException {
		
		
		URL url = null;
		try {
			url = new URL("http://localhost:9994/ws/ReportService?wsdl");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		 
        //1st argument service URI, refer to wsdl document above
	//2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://service.reports.com/", "ReportServiceImplService");
 
        ReportServiceImplService implservice = new ReportServiceImplService(url,qname);
        ReportService reportService = implservice.getReportServiceImplPort();
        ReportList reportList = reportService.getReports();
        
        List<Report> custListOfReports = reportList.getCustReportList();
        for (Report report : custListOfReports) {
        	System.out.println("++++++++ Cust Report Id : " + report.getId());
        	System.out.println("++++++++ Cust Report Description : " + report.getDescription());
        	System.out.println("++++++++ Cust Report Name : " + report.getName());
        	System.out.println("++++++++ Cust Report Kind : " + report.getKind());
        	System.out.println("++++++++ Cust Report Cuid : " + report.getCuid());
        	
        	List<ReportPrompt> reportpromptList = report.getMandatoryPrompts();
        	reportpromptList.addAll(report.getOptionalPrompts());	
        	
        	for (ReportPrompt reportPrompt : reportpromptList) {
        		String promptName = reportPrompt.getPromptName();
        		System.out.println("++++++ prompt name ++++++++ " + promptName);
        		List<String> defaultPromptValues = reportPrompt.getDefaultPromptValues();
        		for(String defaultpromptvalue : defaultPromptValues) {
        			System.out.println("+++++++++ default prompt value ++++++++++ " + defaultpromptvalue);
        		}
        	}
        	        	
        }
        
        List<Report> stdListOfReports = reportList.getStdReportList();
        for (Report report : stdListOfReports) {
        	System.out.println("++++++++ Std Report Id : " + report.getId());
        	System.out.println("++++++++ Std Report Description : " + report.getDescription());
        	System.out.println("++++++++ Std Report Name : " + report.getName());
        	System.out.println("++++++++ Std Report Kind : " + report.getKind());
        	System.out.println("++++++++ Std Report Cuid : " + report.getCuid());
        	List<ReportPrompt> reportpromptList = report.getMandatoryPrompts();
        	reportpromptList.addAll(report.getOptionalPrompts());
        	for (ReportPrompt reportPrompt : reportpromptList) {
        		String promptName = reportPrompt.getPromptName();
        		System.out.println("++++++ prompt name ++++++++ " + promptName);
        		List<String> defaultPromptValues = reportPrompt.getDefaultPromptValues();
        		for(String defaultpromptvalue : defaultPromptValues) {
        			System.out.println("+++++++++ default prompt value ++++++++++ " + defaultpromptvalue);
        		}
        	}
        	
        	
        }
        
        ReportCriteria rc = new ReportCriteria();
		rc.setReportId(155303);
		
		List<ReportPromptNameValue> reportPromptValueList = new ArrayList<ReportPromptNameValue>();
		
		String promptCustomer[] = {"0000578605"};
		ReportPromptNameValue p1 = new ReportPromptNameValue();
		p1.setPromptName("hdn_xcomMasterCustomer:");
		p1.setPromptValue(promptCustomer);
		reportPromptValueList.add(p1);
		
		String promptFromDate[] = {"07/01/2013 00:00:00"};
		ReportPromptNameValue p2 = new ReportPromptNameValue();
		p2.setPromptName("caln_Invoice Date From::");
		p2.setPromptValue(promptFromDate);
		reportPromptValueList.add(p2);

		String promptToDate[] = {"07/31/2013 00:00:00"};
		ReportPromptNameValue p3 = new ReportPromptNameValue();
		p3.setPromptName("hdn_xcomMasterCustomer:");
		p3.setPromptValue(promptToDate);
		reportPromptValueList.add(p3);

		rc.setReportPromptNameValue(reportPromptValueList);
		
		rc.setReportType(ReportTypeEnum.EXCEL);
		
		ReportData rd = reportService.executeReport(rc);
		byte[] excelReport = rd.getBinaryReport();
		
		FileOutputStream stream = new FileOutputStream("C:\\excel\\myexcel.xls");
		try {
		    stream.write(excelReport);
		} finally {
		    stream.close();
		}
		
        

	}
}

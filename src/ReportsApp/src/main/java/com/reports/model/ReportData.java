package com.reports.model;

import java.util.List;

public class ReportData {
	String htmlReport;
	byte[] binaryReport;
	
	List<String> missingMandatoryPrompts;
	
	List<String> boeErrorMessages; 
	
	public String getHtmlReport() {
		return htmlReport;
	}
	public void setHtmlReport(String htmlReport) {
		this.htmlReport = htmlReport;
	}
	public byte[] getBinaryReport() {
		return binaryReport;
	}
	
	public void setBinaryReport(byte[] binaryReport) {
		this.binaryReport = binaryReport;
	}
	public List<String> getMissingMandatoryPrompts() {
		return missingMandatoryPrompts;
	}
	public void setMissingMandatoryPrompts(List<String> missingMandatoryPrompts) {
		this.missingMandatoryPrompts = missingMandatoryPrompts;
	}
	public List<String> getBoeErrorMessages() {
		return boeErrorMessages;
	}
	public void setBoeErrorMessages(List<String> boeErrorMessages) {
		this.boeErrorMessages = boeErrorMessages;
	}
	
	
}	

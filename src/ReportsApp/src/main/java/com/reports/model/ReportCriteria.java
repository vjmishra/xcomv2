package com.reports.model;

import java.util.List;

public class ReportCriteria {
	
	int reportId;
	List<ReportPromptNameValue> reportPromptNameValue;
	ReportTypeEnum reportType;
	
	public List<ReportPromptNameValue> getReportPromptNameValue() {
		return reportPromptNameValue;
	}
	public void setReportPromptNameValue(
			List<ReportPromptNameValue> reportPromptNameValue) {
		this.reportPromptNameValue = reportPromptNameValue;
	}
	public int getReportId() {
		return reportId;
	}
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public ReportTypeEnum getReportType() {
		return reportType;
	}
	public void setReportType(ReportTypeEnum reportType) {
		this.reportType = reportType;
	}
	
	

}

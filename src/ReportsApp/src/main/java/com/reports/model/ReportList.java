package com.reports.model;

import java.util.List;

public class ReportList {
	private List<Report> stdReportList;
	private List<Report> custReportList;
	
	public List<Report> getStdReportList() {
		return stdReportList;
	}
	public void setStdReportList(List<Report> stdReportList) {
		this.stdReportList = stdReportList;
	}
	public List<Report> getCustReportList() {
		return custReportList;
	}
	public void setCustReportList(List<Report> custReportList) {
		this.custReportList = custReportList;
	}
	
}

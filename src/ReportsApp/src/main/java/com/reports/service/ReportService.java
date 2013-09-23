package com.reports.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.reports.model.ReportCriteria;
import com.reports.model.ReportData;
import com.reports.model.ReportList;

@WebService
@SOAPBinding(style=SOAPBinding.Style.RPC)
public interface ReportService {
	
	@WebMethod ReportList getReports();
	
	@WebMethod ReportData executeReport(ReportCriteria reportCriteria);
}

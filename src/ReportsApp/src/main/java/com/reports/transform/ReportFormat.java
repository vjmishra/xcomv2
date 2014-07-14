package com.reports.transform;

import com.businessobjects.rebean.wi.DocumentInstance;
import com.reports.model.ReportData;

public interface ReportFormat {
	ReportData convertToReportData(DocumentInstance doc);
}

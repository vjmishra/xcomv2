package com.reports.transform;

import com.businessobjects.rebean.wi.BinaryView;
import com.businessobjects.rebean.wi.DocumentInstance;
import com.businessobjects.rebean.wi.OutputFormatType;
import com.reports.model.ReportData;

public class PdfReportFormat implements ReportFormat {
	
	public PdfReportFormat()
	{
		
	}

	@Override
	public ReportData convertToReportData(DocumentInstance doc) {
		BinaryView docBinaryView = (BinaryView)doc.getView(OutputFormatType.PDF);
		
		ReportData rptData = new ReportData();
		rptData.setBinaryReport(docBinaryView.getContent());
		
		return rptData;
	}

}

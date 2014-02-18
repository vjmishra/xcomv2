package com.reports.transform;

import com.businessobjects.rebean.wi.DocumentInstance;
import com.businessobjects.rebean.wi.HTMLView;
import com.businessobjects.rebean.wi.OutputFormatType;
import com.businessobjects.rebean.wi.Report;
import com.reports.model.ReportData;

public class HtmlReportFormat implements ReportFormat {
	
	public HtmlReportFormat()
	{
		
	}

	@Override
	public ReportData convertToReportData(DocumentInstance doc) {

		Report rpt = doc.getReports().getItem(0);
	    HTMLView rptHtmlView = (HTMLView)rpt.getView(OutputFormatType.DHTML);
	    
	    ReportData rptData = new ReportData();
	    rptData.setHtmlReport(rptHtmlView.getContent());
		return rptData;
	}

}

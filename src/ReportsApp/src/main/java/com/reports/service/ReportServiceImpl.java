package com.reports.service;

import static com.reports.service.ReportConnection.getEnterpriseSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jws.WebService;

import com.businessobjects.rebean.wi.DocumentInstance;
import com.businessobjects.rebean.wi.Lov;
import com.businessobjects.rebean.wi.Prompt;
import com.businessobjects.rebean.wi.Prompts;
import com.businessobjects.rebean.wi.ReportEngine;
import com.businessobjects.rebean.wi.ReportEngines;
import com.businessobjects.rebean.wi.SQLDataProvider;
import com.businessobjects.rebean.wi.SQLSelectStatement;
import com.businessobjects.rebean.wi.Values;
import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.reports.model.Report;
import com.reports.model.ReportCriteria;
import com.reports.model.ReportData;
import com.reports.model.ReportList;
import com.reports.model.ReportPrompt;
import com.reports.model.ReportPromptNameValue;
import com.reports.model.ReportTypeEnum;
import com.reports.transform.ExcelReportFormat;
import com.reports.transform.HtmlReportFormat;
import com.reports.transform.PdfReportFormat;
import com.reports.transform.ReportFormat;

@WebService(endpointInterface = "com.reports.service.ReportService")
public class ReportServiceImpl implements ReportService {

	Map<ReportTypeEnum, ReportFormat> reportFormatsMap = new HashMap<ReportTypeEnum, ReportFormat>();

	String CMS = "";
	String userName = "";
	String userPass = "";
	String auth = "";
	String standardFolder = "";
	String customFolder = "";

	public ReportServiceImpl() {
		
		Properties properties = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports_app.properties");
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CMS = properties.getProperty("CMS");
		userName = properties.getProperty("userName");
		userPass = properties.getProperty("userPass");
		auth = properties.getProperty("auth");
		standardFolder = properties.getProperty("standardFolder");
		customFolder = properties.getProperty("customFolder");
		initializeReportFormats();
	}	

	@Override
	public ReportList getReports() {


		List<Report> stdReports = new ArrayList<Report>();
		List<Report> custReports = new ArrayList<Report>();

		// Authenticate
		try {
			IEnterpriseSession enterpriseSession = getEnterpriseSession(
					userName, userPass, CMS, auth);

			IInfoStore iStore = (IInfoStore) enterpriseSession
					.getService("InfoStore");

			ReportEngines reportEngines = (ReportEngines) enterpriseSession
					.getService("ReportEngines");

			ReportEngine reportEngine = reportEngines
					.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);

			stdReports = buildReportList(standardFolder,
					reportEngine, iStore);
			
			//custReports = buildReportList(customFolder,
					//reportEngine, iStore);

		} catch (SDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ReportList reportList = new ReportList();
		reportList.setCustReportList(custReports);
		reportList.setStdReportList(stdReports);

		return reportList;
	}

	private List<Report> buildReportList(String queryParam,
			ReportEngine reportEngine, IInfoStore iStore) throws SDKException {
		List<Report> reportList = new ArrayList<Report>();
		IInfoObjects iobjs = iStore
				.query("Select SI_ID, SI_NAME, SI_DESCRIPTION, SI_KIND, SI_CUID from CI_INFOOBJECTS where SI_PARENT_CUID='"
						+ queryParam + "' order by SI_NAME");
		if (iobjs.getResultSize() > 0) {
			for (Object o : iobjs) {
				IInfoObject iobj = (IInfoObject) o;
				Report report = constructReport(iobj, reportEngine);
				reportList.add(report);
			}
		}
		
		iobjs.clear();
		return reportList;
	}

	private Report constructReport(IInfoObject iobj, ReportEngine reportEngine) throws SDKException {
		Report report = new Report();
		report.setId(iobj.getID());		
		report.setName(iobj.getTitle());
		report.setDescription(iobj.getDescription());
		report.setCuid(iobj.getCUID());
		report.setKind(iobj.getKind());
		

		DocumentInstance document = reportEngine.openDocument(iobj.getID());

		Prompts prompts = document.getPrompts();

		List<ReportPrompt> optionalPromptList = new ArrayList<ReportPrompt>();
		List<ReportPrompt> mandatoryPromptList = new ArrayList<ReportPrompt>();

		for (int i = 0; i < prompts.getCount(); i++) {
			Prompt prompt = prompts.getItem(i);
			String promptName = prompt.getName();
			if (prompt.isOptional()) {
				optionalPromptList.add(new ReportPrompt(promptName, getBOEPromptValues(prompt)));
			} else {
				mandatoryPromptList.add(new ReportPrompt(promptName, getBOEPromptValues(prompt)));
			}

		}

		report.setMandatoryPrompts(mandatoryPromptList);
		report.setOptionalPrompts(optionalPromptList);

		return report;
	}

	@Override
	public ReportData executeReport(ReportCriteria reportCriteria) {
		// TODO Auto-generated method stub

		int reportId = reportCriteria.getReportId();
		//Map<String, String[]> promptMap = reportCriteria.getPromptMap();
		
		List<ReportPromptNameValue> reportPromptNameValue = reportCriteria.getReportPromptNameValue();
		
		
		
		ReportTypeEnum reportType = reportCriteria.getReportType();

		ReportData reportData = new ReportData();
		;

		// Authenticate
		try {
			IEnterpriseSession enterpriseSession = getEnterpriseSession(
					userName, userPass, CMS, auth);

			ReportEngines reportEngines = (ReportEngines) enterpriseSession
					.getService("ReportEngines");

			ReportEngine reportEngine = reportEngines
					.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);

			// Authenticate
			ReportFormat rf = reportFormatsMap.get(reportType);

			DocumentInstance di = reportEngine.openDocument(reportId);			

			Prompts prompts = di.getPrompts();

			List<String> missingMandatoryReportPrompts = new ArrayList<String>();

			for (int i = 0; i < prompts.getCount(); i++) {
				Prompt prompt = prompts.getItem(i);
				String promptName = prompt.getName();

				// if prompt is mandatory and promptMap value is emtpy add to
				// error condidtion.
				
				for (int j=0; j <reportPromptNameValue.size(); j++) {

					ReportPromptNameValue reportPromptNameValueObj = reportPromptNameValue.get(j); 
					String prmptName = reportPromptNameValueObj.getPromptName();
					if(promptName.equals(prmptName)) {
						if(!prompt.isOptional() && reportPromptNameValue.get(j) == null) {
							missingMandatoryReportPrompts.add(reportPromptNameValue.get(j).getPromptName());
						} else {
							prompt.enterValues(reportPromptNameValue.get(j).getPromptValue());
						} 
					}
					
				}

			}

			if (missingMandatoryReportPrompts.isEmpty()) {
				di.setPrompts();
				di.refresh();
				reportData = rf.convertToReportData(di);
			} else {
				reportData
						.setMissingMandatoryPrompts(missingMandatoryReportPrompts);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return reportData;
	}
	
	private ArrayList<String> getBOEPromptValues(Prompt prompt) {
		ArrayList<String> promptValueList = new ArrayList<String>();
		
		if(prompt.hasLOV()) {
			Lov lov = prompt.getLOV();
			if (lov != null) {
				Values values = lov.getAllValues();
				for (int i=0; i<values.getCount(); i++) {
					promptValueList.add(values.getValue(i));
				}
			}
		}
		
		return promptValueList;
	}

	private void initializeReportFormats() {
		reportFormatsMap.put(ReportTypeEnum.pdf, new PdfReportFormat());
		reportFormatsMap.put(ReportTypeEnum.excel, new ExcelReportFormat());
		reportFormatsMap.put(ReportTypeEnum.html, new HtmlReportFormat());
	}

}

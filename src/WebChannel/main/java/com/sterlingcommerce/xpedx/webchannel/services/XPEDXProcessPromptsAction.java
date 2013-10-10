package com.sterlingcommerce.xpedx.webchannel.services;

import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletResponseAware;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.reports.service.ReportPromptNameValue;
import com.reports.service.Report;
import com.reports.service.ReportCriteria;
import com.reports.service.ReportData;
import com.reports.service.ReportList;
import com.reports.service.ReportPrompt;
import com.reports.service.ReportService;
import com.reports.service.ReportTypeEnum;
import com.reports.service.webi.ReportUtils;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;

@SuppressWarnings("serial")
public class XPEDXProcessPromptsAction extends WCMashupAction implements
		ServletResponseAware {
	private String id;
	private String cuid;
	private String kind;
	private String name;
	private String viewReportAs;
	private String strToken;
	private HttpServletResponse response;
	private ReportData reportData;
	private List<String> errorList = new ArrayList<String>();
	private ArrayList<String> accountList = new ArrayList<String>();
	private ArrayList<String> billToList = new ArrayList<String>();
	private ArrayList<String> shipToList = new ArrayList<String>();
	List<Report> allReportList;
	private String rndrReport;
	private String finalURL;

	public String getFinalURL() {
		return finalURL;
	}

	public void setFinalURL(String finalURL) {
		this.finalURL = finalURL;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletResponse getServletResponse() {
		return response;
	}

	public ReportData getReportData() {
		return reportData;
	}

	public void setReportData(ReportData reportData) {
		this.reportData = reportData;
	}

	public List<Report> getAllReportList() {
		return allReportList;
	}

	public void setAllReportList(List<Report> allReportList) {
		this.allReportList = allReportList;
	}

	public String getViewReportAs() {
		return viewReportAs;
	}

	public void setViewReportAs(String viewReportAs) {
		this.viewReportAs = viewReportAs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRndrReport() {
		return rndrReport;
	}

	public void setRndrReport(String rndrReport) {
		this.rndrReport = rndrReport;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getStrToken() {
		return strToken;
	}

	public void setStrToken(String strToken) {
		this.strToken = strToken;
	}

	public String getCustomerNo(String customerID) {
		String[] custDetails = customerID.split("-");
		String suffix = custDetails[1];
		return suffix;
	}

	public String geteditedCustomerNo(String customerID) {
		String[] custDetails = customerID.split("-");
		String suffix = custDetails[1];
		String regex = "^0*";
		String newsuffix = suffix.replaceAll(regex, "");

		return newsuffix;
	}

	public String getShipToSuffix(String customerID) {
		String[] custDetails = customerID.split("-");
		String suffix = custDetails[2];
		return suffix;
	}

	private final static Logger log = Logger
			.getLogger(XPEDXProcessPromptsAction.class);

	private Document getAllLocationsDoc(String userId)
			throws CannotBuildInputException {
		Document allAssignedCustomerDoc = null;

		if (null == userId) {
			if (log.isDebugEnabled()) {
				log.debug("getAllLocationsDoc: customerID is a required field. Returning a empty Document");
			}
			return allAssignedCustomerDoc;
		}

		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/CustomerAssignment/@UserId", userId);

		Element input = WCMashupHelper.getMashupInput(
				"xpedx-getCustomerAssignments", valueMap, wSCUIContext);

		String inputXml = SCXmlUtil.getString(input);
		if (log.isDebugEnabled()) {
			log.debug("getAllLocationsDoc: Input XML: " + inputXml);
		}

		Object obj = WCMashupHelper.invokeMashup(
				"xpedx-getCustomerAssignments", input, wSCUIContext);
		allAssignedCustomerDoc = ((Element) obj).getOwnerDocument();

		if (null != allAssignedCustomerDoc) {
			if (log.isDebugEnabled()) {
				log.debug("getAllLocationsDoc: Output XML: "
						+ SCXmlUtil.getString((Element) obj));
			}

		}
		return allAssignedCustomerDoc;
	}

	public String execute() {
		LOG.debug("++++++++++++++++++++++++++++++++++++++++++++++"
				+ " In XPEDXProcessWebiPromptAction"
				+ "++++++++++++++++++++++++++++++");
		Map<String, String[]> map = request.getParameterMap();

		/*
		 * ReportEngines reportEngines =
		 * (ReportEngines)request.getSession().getAttribute("ReportEngines");
		 * ReportEngine reportEngine = null; reportEngine =
		 * reportEngines.getService
		 * (ReportEngines.ReportEngineType.WI_REPORT_ENGINE); DocumentInstance
		 * document = reportEngine.openDocument(Integer.parseInt(getId()));
		 * document.refresh(); Prompts prompts = document.getPrompts();
		 */

		/*XPEDXReportService reportService = new XPEDXReportService();
		ReportService intReportService = reportService.getReportService();
		ReportList reportList = intReportService.getReports();
		allReportList = reportList.getCustReportList();
		allReportList.addAll(reportList.getStdReportList());

		List<ReportPrompt> allPrompts = null;

		for (Report report : allReportList) {
			if (report.getId() == Integer.parseInt(getId())) {
				allPrompts = report.getMandatoryPrompts();
				allPrompts.addAll(report.getOptionalPrompts());
				break;
			}
		}*/

		// Ony when All is selected
		String userId = wcContext.getLoggedInUserId();
		String[] strLocType = map.get("selectedLocationType");
		if (strLocType[0].equalsIgnoreCase("All")) {
			try {
				Document outputDoc = getAllLocationsDoc(userId);
				NodeList customerListElem = outputDoc
						.getElementsByTagName("Customer");
				if (customerListElem != null
						&& customerListElem.getLength() > 0) {
					for (int m = 0; m < customerListElem.getLength(); m++) {
						Element customerElem = (Element) customerListElem
								.item(m);
						if (customerElem != null) {
							String strCustId = SCXmlUtil.getAttribute(
									customerElem, "CustomerID");
							Element extElement = (Element) customerElem
									.getFirstChild();
							String extSuffixType = SCXmlUtil.getAttribute(
									extElement, "ExtnSuffixType");
							// Checking For Account,Bill To, Ship to
							// Modified For Jira 3216
							if (extSuffixType.equalsIgnoreCase("C")) {
								// if(strCustId.startsWith("CD") &&
								// strCustId.endsWith("CC")) {
								String account = getCustomerNo(strCustId);
								accountList.add(account);
							} else if (extSuffixType.equalsIgnoreCase("B") == true) {
								// Fix to match Bill to cust id with the
								// customer batch job. No need to send suffix,
								// since we truncate it to 000 while migrating
								// data
								String billto = geteditedCustomerNo(strCustId);// +
																				// " - "
																				// +
																				// getShipToSuffix(strCustId);
								billToList.add(billto);
							} else if (extSuffixType.equalsIgnoreCase("S") == true) {
								String shipto = geteditedCustomerNo(strCustId)
										+ " - " + getShipToSuffix(strCustId);
								shipToList.add(shipto);
							}

						}
					}
				}
			} catch (CannotBuildInputException e) {
				LOG.debug("Error Occured :->" + e.getMessage());

			}

		}
		
		Map<String, String> promptData = new HashMap<String, String>();
		
		ReportUtils ru = new ReportUtils();
		List<String> promptsString = null;

		HttpHost _target = ru.getHttpHost(ReportUtils.getCMSLogonDetails().get(
				"CMS"));

		ArrayList<String> logonTokens = null;
		try {
			logonTokens = ru.logonCMS(
					ReportUtils.getCMSLogonDetails().get("username"),
					ReportUtils.getCMSLogonDetails().get("password"),
					ReportUtils.getCMSLogonDetails().get("authentication"),
					_target);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Boolean isOK = true;
		if (logonTokens.size() < 2) {
			System.out.println("No Tokens Found");
			isOK = false;
		}
		if (isOK) {			
			try {
				promptsString = ru.getPromptsAsString(getId(), _target,
						logonTokens.get(0));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		for (int i = 0; i < promptsString.size(); i++) {
			String promptName = promptsString.get(i);

			String[] paramStringArray = (String[]) map.get(promptName);

			String promptNameArray[] = promptName.split("_");
			String suffix = null;
			String prefix = null;
			if (promptNameArray.length >= 2)
				suffix = promptNameArray[1];
			prefix = promptNameArray[0];

			// Removed as per requirements
			// || prefix.equalsIgnoreCase("caln")
			/*
			 * if (!prompt.isOptional() ) { LOG.debug(
			 * "=====================It is mandatory prompt====================="
			 * + promptName); if(paramStringArray == null) {
			 * errorList.add(suffix + " is mandatory."); } else { boolean
			 * paramString = true; for (int j=0; j<paramStringArray.length; j++)
			 * { if (!paramStringArray[j].trim().equals("")) paramString =
			 * false; } if (paramString) errorList.add(suffix +
			 * " is mandatory."); } }
			 */

			if (prefix.equalsIgnoreCase("ddls") && suffix != null
					&& suffix.equalsIgnoreCase("account:")) {
				strLocType = map.get("selectedLocationType");
				// Only when account has been selected on webiprompt screen
				if (strLocType[0].equalsIgnoreCase("account")) {
					String[] strSapId = request
							.getParameterValues("selectedCustId");
					String temp[] = new String[1];
					temp[0] = getCustomerNo(strSapId[0]);
					LOG.debug("****For Prompt " + promptName
							+ ", Value Passed Is= " + temp[0]);					
					promptData.put(promptName, temp[0]);

				} else if (strLocType[0].equalsIgnoreCase("All")) {
					String array[] = (String[]) accountList
							.toArray(new String[accountList.size()]);
					String arrayCSF = "";
					for (int m = 0; m < array.length; m++) {
						if (m == array.length - 1 )
							arrayCSF = arrayCSF + array[m];
						else 
							arrayCSF = arrayCSF + array[m] + ","; 
						LOG.debug("**** When All Authorized, For Prompt "
								+ promptName + " Value Passed Is= " + array[m]);
					}

					promptData.put(promptName, arrayCSF);

				} else {
					LOG.debug("Entering null values for " + promptName);
				}
			}

			if (prefix.equalsIgnoreCase("ddls") && suffix != null
					&& suffix.equalsIgnoreCase("Ship To:")) {
				strLocType = map.get("selectedLocationType");
				// Only when ship to has been selected on webiprompt screen
				if (strLocType[0].equalsIgnoreCase("Ship To")) {
					String[] strSapId = request
							.getParameterValues("selectedCustId");
					String temp[] = new String[1];
					temp[0] = geteditedCustomerNo(strSapId[0]) + " - "
							+ getShipToSuffix(strSapId[0]);
					LOG.debug("****For Prompt " + promptName
							+ ", Value Passed Is= " + temp[0]);
					promptData.put(promptName, temp[0]);

				} else if (strLocType[0].equalsIgnoreCase("All")) {
					String array[] = (String[]) shipToList
							.toArray(new String[shipToList.size()]);
					String arrayCSF = "";
					for (int m = 0; m < array.length; m++) {
						if (m == array.length - 1 )
							arrayCSF = arrayCSF + array[m];
						else 
							arrayCSF = arrayCSF + array[m] + ",";
						LOG.debug("**** When All Authorized, For Prompt "
								+ promptName + " Value Passed Is= " + array[m]);
					}
					promptData.put(promptName, arrayCSF);

				} else {
					LOG.debug("Entering null values for " + promptName);
				}
			}

			if (prefix.equalsIgnoreCase("ddls") && suffix != null
					&& suffix.equalsIgnoreCase("Bill To:")) {
				strLocType = map.get("selectedLocationType");
				// Only when Bill To has been selected on webiprompt screen
				if (strLocType[0].equalsIgnoreCase("Bill To")) {
					String[] strSapId = request
							.getParameterValues("selectedCustId");
					String temp[] = new String[1];
					// Fix to match Bill to cust id with the customer batch job.
					// No need to send suffix, since we truncate it to 000 while
					// migrating data
					temp[0] = geteditedCustomerNo(strSapId[0]); // + " - " +
																// getShipToSuffix(strSapId[0]);
					LOG.debug("****For Prompt " + promptName
							+ ", Value Passed Is= " + temp[0]);
					promptData.put(promptName, temp[0]);

				} else if (strLocType[0].equalsIgnoreCase("All")) {
					String array[] = (String[]) billToList
							.toArray(new String[billToList.size()]);
					String arrayCSF = ""; 
					for (int m = 0; m < array.length; m++) {
						if (m == array.length - 1 )
							arrayCSF = arrayCSF + array[m];
						else 
							arrayCSF = arrayCSF + array[m] + ",";
						LOG.debug("**** When All Authorized, For Prompt "
								+ promptName + " Value Passed Is= " + array[m]);
					}

					promptData.put(promptName, arrayCSF);

				} else {
					LOG.debug("Entering null values for " + promptName);
				}
			}

			if (errorList.size() == 0) {
				if (paramStringArray != null && paramStringArray.length > 0
						&& paramStringArray[0] != null
						&& !paramStringArray[0].equals("")) {
					for (int m = 0; m < paramStringArray.length; m++) {
						LOG.debug("****For Prompt " + promptName
								+ ", Value Passed Is= " + paramStringArray[m]);
					}
					
					if ("caln".equals(prefix)) {
						String dateValue = paramStringArray[0];
						dateValue = dateValue + " 00:00:00";
						paramStringArray[0] = dateValue; 
					}
					
					
					promptData.put(promptName, paramStringArray[0]);
				} else {
					if (!prefix.equalsIgnoreCase("ddls") && suffix != null) {
						LOG.debug("Entering null values for " + promptName);
					}
				}
			}
		}

		if (errorList.size() > 0) {
			if (log.isDebugEnabled()) {
				log.debug(".............error list greater than 0............................");
			}
			setRndrReport("false");
			Iterator<String> iterator = errorList.iterator();
			while (iterator.hasNext()) {
				log.error("errorList+++++++++++" + iterator.next());
			}

		} else {
			
			String params = "";
			String openDocURL = "";
			try {
				openDocURL = ru.getOpenDoc(getId(), _target, logonTokens.get(0));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (!("").equals(openDocURL)) {
				try {
					params = ru.getParamString(getId(), _target, logonTokens.get(0), promptData);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				setRndrReport("true");
				
				String encodedToken = logonTokens.get(1);
				
				finalURL= openDocURL + params + "&X-SAP-LogonToken=" + encodedToken;
				
			}
			
			// Set the prompts and rerun the report

			//LOG.debug("Setting report prompts ************************************");
			// document.setPrompts();

			// Set the token in case you rerun with new prompts
			/*ReportCriteria reportCriteria = new ReportCriteria();

			reportCriteria.setReportId(Integer.parseInt(getId()));
			reportCriteria.setReportPromptNameValue(reportPromptNameValueList);

			// reportCriteria.setPromptMap(promptValue);

			if (getViewReportAs().equals("pdf")) {
				reportCriteria.setReportType(ReportTypeEnum.PDF);
			}
			if (getViewReportAs().equals("xl")) {				
				reportCriteria.setReportType(ReportTypeEnum.EXCEL);
			}
			if (getViewReportAs().equals("html")) {
				reportCriteria.setReportType(ReportTypeEnum.HTML);
			}

			//reportData = intReportService.executeReport(reportCriteria);

			request.getSession().setAttribute("ReportData", reportData);*/			

			/*ServletOutputStream outs = null;

			if (reportData == null) {
				System.out.println("========== report data is null =========");
			} else {
				if (getViewReportAs().equals("xl")) {
					response.setHeader("Content-Disposition",
							"Attachment;Filename=\"MyReport.xls\"");
					response.setContentType("application/vnd.ms-excel");
					try {
						response.setContentLength(reportData.getBinaryReport().length);
						outs = response.getOutputStream();
						outs.write(reportData.getBinaryReport());
						outs.flush();
						outs.close();
						setRndrReport("false");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return SUCCESS;
				} 
			}*/
						
		}

		return SUCCESS;
	}
}

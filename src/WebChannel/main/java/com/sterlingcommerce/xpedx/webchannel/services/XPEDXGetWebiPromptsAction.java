package com.sterlingcommerce.xpedx.webchannel.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

import com.reports.service.Report;
import com.reports.service.ReportList;
import com.reports.service.ReportPrompt;
import com.reports.service.ReportService;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

@SuppressWarnings("serial")
public class XPEDXGetWebiPromptsAction extends WCMashupAction {
	private String id; 
	private String renderReport;
	private String viewReportAs;
	private String storageToken;
	private List<XPEDXShipToCustomer> assignedShipToList;
	private XPEDXWebiPromptsBean webiPromptsBean[];	
	private List<String> errorNames = new ArrayList<String>();
	private Boolean bool = false;
	List<Report> allReportList;

	public String getViewReportAs() {
		return viewReportAs;
	}

	public void setViewReportAs(String viewReportAs) {
		this.viewReportAs = viewReportAs;
	}

	public List<Report> getAllReportList() {
		return allReportList;
	}

	public void setAllReportList(List<Report> allReportList) {
		this.allReportList = allReportList;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public XPEDXWebiPromptsBean[] getWebiPromptsBean() {
		return webiPromptsBean;
	}

	public void setWebiPromptsBean(XPEDXWebiPromptsBean[] webiPromptsBean) {
		this.webiPromptsBean = webiPromptsBean;
	}

	public String getRenderReport() {
		LOG.debug("Get render report output " + renderReport);
		return renderReport;
	}

	public void setRenderReport(String renderReport) {
		LOG.debug("Setting Report+++++++++ " + renderReport);
		this.renderReport = renderReport;
	}

	public String getStorageToken() {
		return storageToken;
	}

	public void setStorageToken(String storageToken) {
		this.storageToken = storageToken;
	}

	public List<String> getErrorNames() {
		return errorNames;
	}

	public void setErrorNames(List<String> errorNames) {
		if(!this.errorNames.isEmpty())
			this.errorNames.clear();
		
		//Workaround to ignore empty error messages of the format []
		if(errorNames != null && errorNames.size() > 0)
			for(int count = 0 ;count< errorNames.size(); count++){
				if(!errorNames.get(count).equals("[]"))
					this.errorNames.add(errorNames.get(count));
			}
			
	}



	public String getShipToSuffix(String customerID) { 
		String[] custDetails = customerID.split("-");
		String suffix = custDetails[2];
		return suffix;
	}

	public String getCompanyCode(String customerID) {
		String[] custDetails = customerID.split("-");
		String suffix = custDetails[0];
		return suffix;
	}

	public String getCustomerNo(String customerID) {
		String[] custDetails = customerID.split("-");
		String suffix = custDetails[1];
		return suffix;
	}


	public String execute() {

		List<String> errors = getErrorNames();
		Iterator<String> iter = errors.iterator();
		if (errors != null) {
			String errorString = "";
			for (int i=0; i<errors.size(); i++) {
				errorString = errors.get(i);
			}
			LOG.debug("-------------------- errorString = " + errorString);
		}


		/*ReportEngines reportEngines = (ReportEngines)request.getSession().getAttribute("ReportEngines");
		ReportEngine reportEngine = null;
		reportEngine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
		DocumentInstance document = reportEngine.openDocument(Integer.parseInt(getId()));
		Prompts prompts = document.getPrompts(); */
		
		XPEDXReportService reportService = new XPEDXReportService();
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
		}
		
		
		
		//DocumentInstance document = reportEngines.getDocumentFromStorageToken(getCuid());
		//Prompts prompts = document.getPrompts();
		XPEDXWebiPromptsBean bean[] = new XPEDXWebiPromptsBean[allPrompts.size()];
		LOG.debug("++++++++++++++++++++++++++++++++++++++++++prompts count = " + allPrompts.size());		
		for(int i=0; i<allPrompts.size(); i++) {
			//Prompt prompt = prompts.getItem(i);
			ReportPrompt reportPrompt = allPrompts.get(i);
			String promptName = reportPrompt.getPromptName();			
			bean[i] = new XPEDXWebiPromptsBean();
			StringTokenizer tokenizer = new StringTokenizer(promptName, "_");
			int j=0;
			String prefix = "";
			String suffix = "";
			while(tokenizer.hasMoreTokens()) {
				if (j%2 == 0) {
					prefix = tokenizer.nextToken();
					j++;
				} else {
					suffix = tokenizer.nextToken();
				}							
			}

			bean[i].setPrefix(prefix);
			bean[i].setSuffix(suffix);					
			bean[i].setPrmtName(promptName);
			LOG.debug("prefix = " + prefix);
			LOG.debug("suffix = " + suffix);

			if(prefix.equalsIgnoreCase("hdn")) {
				bean[i].setPromptValues(getSterlingPromptValue(suffix));				
			}
			if(prefix.equalsIgnoreCase("mslb") || prefix.equalsIgnoreCase("ddlb")) {
				bean[i].setPromptValues(reportPrompt.getDefaultPromptValues());
			}
			/*if(prefix.equalsIgnoreCase("msls") || prefix.equalsIgnoreCase("ddls")) {
				bean[i].setSuffixList(getSterlingPromptList(prompt, suffix, bean[i]));
			}
*/

		}

		setWebiPromptsBean(bean);

		return SUCCESS;
	}

	/*private List<XPEDXShipAndBillSuffix> getSterlingPromptList(Prompt prompt, String suffix, XPEDXWebiPromptsBean promptsBean) {


		if (suffix.equalsIgnoreCase("Bill To:")) {
			String loggedInCustomerFromSession = XPEDXWCUtils
			.getLoggedInCustomerFromSession(wcContext);
			HashMap<String, ArrayList<String>> allMap = null;
			if (loggedInCustomerFromSession != null
					&& loggedInCustomerFromSession.trim().length() > 0) {
				try {
					allMap = XPEDXWCUtils.getAssignedCustomersMap(loggedInCustomerFromSession,
							wcContext.getLoggedInUserId());
				} catch (CannotBuildInputException e) {
					LOG.error("Error getting all bill to as Map : " + e.getMessage());
				}
			} else {
				try {
					allMap = XPEDXWCUtils.getAssignedCustomersMap(wcContext.getCustomerId(),
							wcContext.getLoggedInUserId());
				} catch (CannotBuildInputException e) {
					LOG.error("Error getting all bill to as Map : " + e.getMessage());
				}
			}
			LOG.debug("All shipTos Map***********=" + allMap);
			List<String> allBillTos = allMap.get(XPEDX_ASSIGNED_CUSTOMER_BILL_TO);
			//List<String> allBillToCustDetail = new ArrayList<String>();
			List<XPEDXShipAndBillSuffix> billToSuffixList = new ArrayList<XPEDXShipAndBillSuffix>();

			XPEDXShipAndBillSuffix suffixBean = null;

			if (allBillTos != null) {
				for(int i=0; i<allBillTos.size(); i++) {
					String custId = allBillTos.get(i);
					LOG.debug("BillTo++++++++++"+ custId);
					try {

						Document docCustDetail = XPEDXWCUtils.getCustomerDetails(custId, wcContext.getStorefrontId());
						String billToCustDetail = getBillToCustDetails(docCustDetail);
						
						LOG.debug("billToCustDetail+++++++++++++" + billToCustDetail);
						//allBillToCustDetail.add(billToCustDetail);						
						suffixBean = new XPEDXShipAndBillSuffix();
						suffixBean.setStrToDisplay(billToCustDetail);
						suffixBean.setSuffixValue(getShipToSuffix(custId));


						//billToMap.put(billToCustDetail, getShipToSuffix(custId));
						billToSuffixList.add(suffixBean);
					} catch (CannotBuildInputException e) {
						LOG.error("Error getting customer details for bill to customer : " + e.getMessage());
					}
				}
				//promptsBean.setSuffixList(billToSuffixList);
				//return allBillToCustDetail;
				return billToSuffixList;

			}
			//promptsBean.setSuffixList(new ArrayList<String>());
			//Map<String, String> billMap[] = new HashMap[0];
			return new ArrayList<XPEDXShipAndBillSuffix>();  

		}

		if (suffix.equalsIgnoreCase("Ship To:")) {
			List<String> assignedShipTos = null;
			HashSet hashSet = null;
			//String	a1= XPEDXWCUtils.getDefaultShipTo();
			//dupsShipTos= XPEDXWCUtils.getAllShipTosAsList(getWCContext());
			//hashSet = new HashSet(dupsShipTos);
			//assignedShipTos= new ArrayList(hashSet);
			//LOG.debug("Ship Tos dupsShipTos ====" + dupsShipTos );
			assignedShipTos = XPEDXWCUtils.getAllAssignedShiptoCustomerIdsForAUser(getWCContext().getLoggedInUserId(),getWCContext());
			LOG.debug("Ship Tos assignedShipTos====" + assignedShipTos);
			if (assignedShipTos == null) {
				assignedShipTos = new ArrayList<String>();
			}

			//List<String> assignedShipToList = new ArrayList<String>();
			List<XPEDXShipAndBillSuffix> shipToSuffixList = new ArrayList<XPEDXShipAndBillSuffix>();			
			int listSize = assignedShipTos.size();
			XPEDXShipAndBillSuffix shipSuffixBean = null;

			if (assignedShipTos != null) {
				if (listSize > 0) {
					for (int index = 0; index < listSize; index++) {
						XPEDXShipToCustomer xPEDXShipToCustomer = null;
						try {
							xPEDXShipToCustomer = XPEDXWCUtils
							.getShipToAdress(assignedShipTos.get(index),
									getWCContext().getStorefrontId());
						} catch (CannotBuildInputException e) {
							LOG.error("Error getting ship to address : " + e.getMessage());
						}
						String customerId = xPEDXShipToCustomer.getCustomerID();
						String shipToSuffix = getShipToSuffix(customerId);	
						String comcode = getCompanyCode(customerId);
						String custNo= getCustomerNo(customerId);
						//String crId=getWCContext().getCustomerId();	
						String stringToDisplay = "";

						if (xPEDXShipToCustomer.getAddressList().size() > 0) 
							stringToDisplay = xPEDXShipToCustomer.getAddressList().get(0) + ", ";

						stringToDisplay = comcode + "- " + custNo + "- " + shipToSuffix + "- " + stringToDisplay + xPEDXShipToCustomer.getCity() + ", " 
						+ xPEDXShipToCustomer.getState() + ", " + xPEDXShipToCustomer.getZipCode();

						//assignedShipToList.add(stringToDisplay);						
						shipSuffixBean = new XPEDXShipAndBillSuffix();
						shipSuffixBean.setStrToDisplay(stringToDisplay);
						shipSuffixBean.setSuffixValue(shipToSuffix);
						//shipToMap.put(stringToDisplay, shipToSuffix);
						shipToSuffixList.add(shipSuffixBean);
					}				
				}
			}					
			//promptsBean.setSuffixList(shipToSuffixList);
			return shipToSuffixList;
		}


		if (suffix.equalsIgnoreCase("Account:")) {
			Element docElement = null;
			List<XPEDXShipAndBillSuffix> list = new ArrayList<XPEDXShipAndBillSuffix>();
			//XPEDXShipAndBillSuffix shipSuffixBean = null;
			XPEDXShipAndBillSuffix suffixBean = null;
			List<XPEDXShipAndBillSuffix> billToSuffixList = new ArrayList<XPEDXShipAndBillSuffix>();
			XPEDXShipToCustomer xPEDXShipToCustomer = null;
			String customerId=getWCContext().getCustomerId();
LOG.debug("In Account, customerId++++++++++++"+ customerId);
			try {
				docElement = prepareAndInvokeMashup("getCustomerExtnParameters");

				if (null != docElement) {
					LOG.debug("Output XML In Account: " + SCXmlUtil.getString(docElement));
					LOG.debug("Output XML: " + SCXmlUtil.getString(docElement));
				}
			} catch (XMLExceptionWrapper e) {	
				LOG.error("Error getting Customer Extn parameters : " + e.getMessage());

			} catch (CannotBuildInputException e) {
				LOG.error("Error getting Customer Extn parameters : " + e.getMessage());

			}

			Element eleExtn = SCXmlUtil.getChildElement(docElement, "Extn");
			String testing =SCXmlUtil.getString(eleExtn);
			LOG.debug("TEST Acount SAP=====>>>"+ testing);
			LOG.debug("TEST Acount SAP=====>>>"+ testing);

			String sapnumber= eleExtn.getAttribute("ExtnSAPNumber");
			String sapname= eleExtn.getAttribute("ExtnSAPName");
			String legacycustnumber= eleExtn.getAttribute("ExtnLegacyCustNumber");

			String accountInfo = sapnumber + " - " + sapname ;

			suffixBean = new XPEDXShipAndBillSuffix();
			suffixBean.setStrToDisplay(accountInfo);
			suffixBean.setSuffixValue(sapnumber);

			billToSuffixList.add(suffixBean);
			return billToSuffixList; 
		} 
		//promptsBean.setSuffixList(new ArrayList<String>());
		//return new ArrayList<String>();
		return new ArrayList<XPEDXShipAndBillSuffix>();
	} */

	private String getBillToCustDetails(Document docCustDetail) {
		String custId = SCXmlUtil.getAttribute(docCustDetail.getDocumentElement(), "CustomerID");
		String billToSuffix = getShipToSuffix(custId);
		String comcode = getCompanyCode(custId);
		String custNo= getCustomerNo(custId);
		Element element = SCXmlUtil.getChildElement(docCustDetail
				.getDocumentElement(), "CustomerAdditionalAddressList");
		element = SCXmlUtil.getChildElement(element, "CustomerAdditionalAddress");
		String testAddress=SCXmlUtil.getString(element);
		LOG.debug("TEST ADDRESS=====>>>"+ testAddress);
		LOG.debug("TEST ADDRESS=====>>>"+ testAddress);

		element = SCXmlUtil.getChildElement(element, "PersonInfo");

		String address1= SCXmlUtil.getAttribute(element,"AddressLine1");
		String country= SCXmlUtil.getAttribute(element,"Country");
		String city= SCXmlUtil.getAttribute(element,"City");
		String state= SCXmlUtil.getAttribute(element,"State");
		String zip= SCXmlUtil.getAttribute(element,"ZipCode");
		//SCXmlUtils.getAttribute
		String billToCustomer = comcode + " - " + custNo + " - " + billToSuffix + " - " + address1 +
		", " + city + ", " + state + ", " + zip;
		return billToCustomer;				
	}

	private List<String> getSterlingPromptValue(String suffix) {		
		Element docElement = null;
		List<String> list = new ArrayList<String>();
		try {
			docElement = prepareAndInvokeMashup("getCustomerExtnParameters");

			if (null != docElement) {
				LOG.debug("Output XML: " + SCXmlUtil.getString(docElement));
			}
		} catch (XMLExceptionWrapper e) {	
			LOG.error("Error getting Customer Extn parameters : " + e.getMessage());
			return list;
		} catch (CannotBuildInputException e) {
			LOG.error("Error getting Customer Extn parameters : " + e.getMessage());
			return list;
		}

		Element eleExtn = SCXmlUtil.getChildElement(docElement, "Extn");


		if (suffix.indexOf("Cust") == 0) {
			System.out.println("+++++++++++ returning cust ++++++++++++ " + eleExtn.getAttribute("ExtnLegacyCustNumber"));
			list.add(eleExtn.getAttribute("ExtnLegacyCustNumber"));			
			return list;
		} 
		if (suffix.indexOf("xcomMasterCustomer") == 0) {
			list.add(eleExtn.getAttribute("ExtnSAPParentAccNo"));
			System.out.println("+++++++++++ returning xcomMasterCustomer ++++++++++++ " + eleExtn.getAttribute("ExtnSAPParentAccNo"));
			LOG.debug("XCOMMSAP ++++++++++++++++" + eleExtn.getAttribute("ExtnSAPParentAccNo"));
			return list;
		}			

		/*if (suffix.indexOf("SAP") == 0) {
			list.add(eleExtn.getAttribute("ExtnSAPNumber"));			
			return list;
		}	*/		

		return list;
	}

	/*private List<String> getBOEPromptValues(Prompt prompt) {
		List<String> promptList = new ArrayList<String>();
		Lov lov = prompt.getLOV();
		Values values = lov.getAllValues();
		for(int j=0; j<values.getCount(); j++) {
			promptList.add(values.getValue(j));					
		}
		return promptList;
	}*/

	private static final Logger LOG = Logger
	.getLogger(XPEDXGetWebiPromptsAction.class);

	private static final String XPEDX_ASSIGNED_CUSTOMER_BILL_TO = "BILL_TO";
}

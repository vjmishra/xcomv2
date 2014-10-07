package com.sterlingcommerce.xpedx.webchannel.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.reports.service.Report;
import com.reports.service.webi.ReportUtils;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import java.util.Map;

/*
 *This class retrieves the report parameters that the user selects on the ReportList page. 
 * 
 */

@SuppressWarnings("serial")
public class XPEDXGetWebiPromptsAction extends WCMashupAction {
	private String id;
	private String renderReport;
	private String name;
	private String viewReportAs;
	private String finalURL;
	private String storageToken;
	private List<XPEDXShipToCustomer> assignedShipToList;
	private XPEDXWebiPromptsBean webiPromptsBean[];
	private List<String> errorNames = new ArrayList<String>();
	private Boolean bool = false;
	List<Report> allReportList;

	public String getFinalURL() {
		return finalURL;
	}

	public void setFinalURL(String finalURL) {
		this.finalURL = finalURL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
		if (!this.errorNames.isEmpty())
			this.errorNames.clear();

		// Workaround to ignore empty error messages of the format []
		if (errorNames != null && errorNames.size() > 0)
			for (int count = 0; count < errorNames.size(); count++) {
				if (!errorNames.get(count).equals("[]"))
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

		String username;
		String password;
		String authentication;
		String CMS;
		List<String> errors = getErrorNames();
		Iterator<String> iter = errors.iterator();
		if (errors != null) {
			String errorString = "";
			for (int i = 0; i < errors.size(); i++) {
				errorString = errors.get(i);
			}
			LOG.debug("-------------------- errorString = " + errorString);
		}


		ReportUtils ru = new ReportUtils();
		List<String> promptsString = null;

		HttpHost _target = null; 

		ArrayList<String> logonTokens = null;
		try {
			//ML - changed logic to read CMS Info from property file only once. 
			Map<String,String> CMSLogonDetails = ReportUtils.getCMSLogonDetails();
			username = CMSLogonDetails.get("username").toString();
			password = CMSLogonDetails.get("password").toString();
			authentication = CMSLogonDetails.get("authentication").toString();
			CMS = CMSLogonDetails.get("CMS").toString();
			//postpone setting _target until CMS is read to avoid calling getCMSLogonDetails() more than once. 
			_target = ru.getHttpHost(CMS);

			//ML:Find out if logonTokens is in session. If not, then go get a new one and store it in the session. 
			//this logic should be reused across this "Report List" page, the "Report Details" page, and the ReportDisplay page to avoid
			//creating too many sessions and violate the SAP license agreement. 
			logonTokens = (ArrayList) request.getSession().getAttribute("logonTokens"); 
			if ((logonTokens == null) || (logonTokens.size() != 2)) {				
				logonTokens = ru.logonCMS(username, password, authentication, _target);
				//store the logonTokens in session for future use
				request.getSession().setAttribute("logonTokens", logonTokens);
			}		
			
		} catch (Exception e) {
			System.out.println("Could not logon to BI/CMS using the supplied credentials.");
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
				System.out.println("Could not logon retrieve report parameters for docID: " + getId().toString());
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

		List<String> defaultPromptValue = new ArrayList<String>();		
		XPEDXWebiPromptsBean bean[] = new XPEDXWebiPromptsBean[promptsString.size()];
		LOG.debug("++++++++++++++++++++++++++++++++++++++++++prompts count = " + promptsString.size());
		//Loop through the parameters/prompts list
		for (int i = 0; i < promptsString.size(); i++) {			
			String promptName = promptsString.get(i);

			bean[i] = new XPEDXWebiPromptsBean();
			StringTokenizer tokenizer = new StringTokenizer(promptName, "_");
			int j = 0;
			String prefix = "";
			String suffix = "";
			while (tokenizer.hasMoreTokens()) {
				if (j % 2 == 0) {
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

			if (prefix.equalsIgnoreCase("hdn")) {
				bean[i].setPromptValues(getSterlingPromptValue(suffix));
			}
			if (prefix.equalsIgnoreCase("mslb")
					|| prefix.equalsIgnoreCase("ddlb")) {
				bean[i].setPromptValues(defaultPromptValue);
			}			
		}
		//Store the prompts bean.
		setWebiPromptsBean(bean); 	

		return SUCCESS;
	}
	
	private String getBillToCustDetails(Document docCustDetail) {
		String custId = SCXmlUtil.getAttribute(
				docCustDetail.getDocumentElement(), "CustomerID");
		String billToSuffix = getShipToSuffix(custId);
		String comcode = getCompanyCode(custId);
		String custNo = getCustomerNo(custId);
		Element element = SCXmlUtil.getChildElement(
				docCustDetail.getDocumentElement(),
				"CustomerAdditionalAddressList");
		element = SCXmlUtil.getChildElement(element,
				"CustomerAdditionalAddress");
		String testAddress = SCXmlUtil.getString(element);
		LOG.debug("TEST ADDRESS=====>>>" + testAddress);
		LOG.debug("TEST ADDRESS=====>>>" + testAddress);

		element = SCXmlUtil.getChildElement(element, "PersonInfo");

		String address1 = SCXmlUtil.getAttribute(element, "AddressLine1");
		String country = SCXmlUtil.getAttribute(element, "Country");
		String city = SCXmlUtil.getAttribute(element, "City");
		String state = SCXmlUtil.getAttribute(element, "State");
		String zip = SCXmlUtil.getAttribute(element, "ZipCode");
		// SCXmlUtils.getAttribute
		String billToCustomer = comcode + " - " + custNo + " - " + billToSuffix
				+ " - " + address1 + ", " + city + ", " + state + ", " + zip;
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
			LOG.error("Error getting Customer Extn parameters : "
					+ e.getMessage());
			return list;
		} catch (CannotBuildInputException e) {
			LOG.error("Error getting Customer Extn parameters : "
					+ e.getMessage());
			return list;
		}

		Element eleExtn = SCXmlUtil.getChildElement(docElement, "Extn");

		if (suffix.indexOf("Cust") == 0) {
			list.add(eleExtn.getAttribute("ExtnLegacyCustNumber"));
			return list;
		}
		if (suffix.indexOf("xcomMasterReportID") == 0) {
						
			String custNum = eleExtn.getAttribute("ExtnSAPParentAccNo");
			
			int firstDigit = getRandomNumber(1,9); 
			int secondDigit = custNum.length() - firstDigit;
			int thirdDigit = getRandomNumber(6, 9);
			int fourthDigit = getRandomNumber(1, 9);
			int digitRandomNumber1 = getRandomNumberOnSize(thirdDigit-6);
			int digitRandomNumber2 = getRandomNumberOnSize(fourthDigit-1);
			int digitRandomNumber3 = getRandomNumberOnSize(getRandomNumber(1,4));
			String finalReportID = "" + firstDigit + secondDigit + thirdDigit + fourthDigit + digitRandomNumber1 + custNum.substring(0, firstDigit) + digitRandomNumber2 + custNum.substring(firstDigit, custNum.length()) + digitRandomNumber3;			
			list.add(finalReportID);
			LOG.debug("XCOMMSAP ++++++++++++++++"
					+ eleExtn.getAttribute("ExtnSAPParentAccNo"));
			return list;
		}
		if (suffix.indexOf("xcomMasterCustomer") == 0) {
			list.add(eleExtn.getAttribute("ExtnSAPParentAccNo"));
			LOG.debug("XCOMMSAP ++++++++++++++++"
					+ eleExtn.getAttribute("ExtnSAPParentAccNo"));
			return list;
		}

		/*
		 * if (suffix.indexOf("SAP") == 0) {
		 * list.add(eleExtn.getAttribute("ExtnSAPNumber")); return list; }
		 */

		return list;
	}
	
	public int getRandomNumber(int min, int max) {
		int result = min + (int)(Math.random() * ((max - min) + 1));
		return result;
	}
	
	public int getRandomNumberOnSize(int number) {
		int digit = (int) Math.pow(10.0, number);
		Random random = new Random();
		int digitRandomNumber = random.nextInt(digit * 9) + digit;
		return digitRandomNumber;
	}

	/*
	 * private List<String> getBOEPromptValues(Prompt prompt) { List<String>
	 * promptList = new ArrayList<String>(); Lov lov = prompt.getLOV(); Values
	 * values = lov.getAllValues(); for(int j=0; j<values.getCount(); j++) {
	 * promptList.add(values.getValue(j)); } return promptList; }
	 */

	private static final Logger LOG = Logger
			.getLogger(XPEDXGetWebiPromptsAction.class);

	private static final String XPEDX_ASSIGNED_CUSTOMER_BILL_TO = "BILL_TO";
}

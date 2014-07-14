package com.sterlingcommerce.xpedx.webchannel.services;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.businessobjects.rebean.wi.DocumentInstance;
import com.businessobjects.rebean.wi.Prompt;
import com.businessobjects.rebean.wi.Prompts;
import com.businessobjects.rebean.wi.ReportEngine;
import com.businessobjects.rebean.wi.ReportEngines;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;

@SuppressWarnings("serial")
public class XPEDXProcessWebiPromptAction extends WCMashupAction {
	private String id; 
	private String cuid;
	private String kind;
	private String name;
	private String strToken;
	private List<String> errorList = new ArrayList<String>();
	private ArrayList<String> accountList = new ArrayList<String>();
	private ArrayList<String> billToList = new ArrayList<String>();
	private ArrayList<String> shipToList = new ArrayList<String>();
	private String rndrReport;

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
		String newsuffix = suffix.replaceAll(regex,"");
		
		return newsuffix;
	}
	public String getShipToSuffix(String customerID) {
		String[] custDetails = customerID.split("-");
		String suffix = custDetails[2];
		return suffix;
	}
	
	private final static Logger log = Logger.getLogger(XPEDXProcessWebiPromptAction.class);

	private Document getAllLocationsDoc(String userId)	throws CannotBuildInputException {
	Document allAssignedCustomerDoc = null;
	
	if (null == userId) {
		if(log.isDebugEnabled()){
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
	if(log.isDebugEnabled()){
	log.debug("getAllLocationsDoc: Input XML: " + inputXml);
	}
	
	
	Object obj = WCMashupHelper.invokeMashup("xpedx-getCustomerAssignments",
			input, wSCUIContext);
	allAssignedCustomerDoc = ((Element) obj).getOwnerDocument();
	
	if (null != allAssignedCustomerDoc) {
		if(log.isDebugEnabled()){
		log.debug("getAllLocationsDoc: Output XML: "	+ SCXmlUtil.getString((Element) obj));
		}
		
	}
	return allAssignedCustomerDoc;
}
	
	public String execute() {
		LOG.debug("++++++++++++++++++++++++++++++++++++++++++++++" +
				" In XPEDXProcessWebiPromptAction" + "++++++++++++++++++++++++++++++");
		HttpSession session = request.getSession();
		Map<String, String[]> map = request.getParameterMap();		
		ReportEngines reportEngines = (ReportEngines)request.getSession().getAttribute("ReportEngines");
		ReportEngine reportEngine = null;
		reportEngine = reportEngines.getService(ReportEngines.ReportEngineType.WI_REPORT_ENGINE);
		DocumentInstance document = reportEngine.openDocument(Integer.parseInt(getId()));
		document.refresh();
		Prompts prompts = document.getPrompts();
		//Ony when All is selected
		String userId = wcContext.getLoggedInUserId();
		String[] strLocType = map.get("selectedLocationType");
		if(strLocType[0].equalsIgnoreCase("All")) {
		try {
			Document outputDoc = getAllLocationsDoc(userId);
			NodeList customerListElem = outputDoc.getElementsByTagName("Customer");
			if(customerListElem!=null && customerListElem.getLength()>0)
			{
				for(int m=0;m<customerListElem.getLength();m++)
				{
					Element customerElem = (Element) customerListElem.item(m);
					if(customerElem!=null) {
						String strCustId = SCXmlUtil.getAttribute(customerElem, "CustomerID");
						Element extElement = (Element) customerElem.getFirstChild();
						String extSuffixType = SCXmlUtil.getAttribute(extElement, "ExtnSuffixType");
						//Checking For Account,Bill To, Ship to
						//Modified For Jira 3216
						if(extSuffixType.equalsIgnoreCase("C")){
						//if(strCustId.startsWith("CD") && strCustId.endsWith("CC")) {
							String account= getCustomerNo(strCustId);
							accountList.add(account);
						}else if(extSuffixType.equalsIgnoreCase("B") == true){
							//Fix to match Bill to cust id with the customer batch job. No need to send suffix, since we truncate it to 000 while migrating data
							String billto= geteditedCustomerNo(strCustId);// + " - " + getShipToSuffix(strCustId);
							billToList.add(billto);
						}else if(extSuffixType.equalsIgnoreCase("S") == true){
							String shipto= geteditedCustomerNo(strCustId) + " - " + getShipToSuffix(strCustId);
							shipToList.add(shipto);
						}
						
					}
				}
			}
		} catch (CannotBuildInputException e) {
			LOG.debug("Error Occured :->" + e.getMessage());
			
		}
	
		
	}
	
		for(int i=0; i<prompts.getCount(); i++) {
			Prompt prompt = prompts.getItem(i);
			String promptName = prompt.getName();
			
			String[] paramStringArray = (String[])map.get(promptName);
			
			String promptNameArray[] = promptName.split("_");
			String suffix = null;
			String prefix = null;
			if(promptNameArray.length >= 2)				
				suffix = promptNameArray[1];
			prefix = promptNameArray[0];

			
			//Removed as per requirements
			//|| prefix.equalsIgnoreCase("caln")
			if (!prompt.isOptional() ) {
				LOG.debug("=====================It is mandatory prompt=====================" + promptName);
				if(paramStringArray == null) {
					errorList.add(suffix + " is mandatory.");
				} else {
					boolean paramString = true;
					for (int j=0; j<paramStringArray.length; j++) {
						if (!paramStringArray[j].trim().equals(""))
							paramString = false;
					}
					if (paramString)
						errorList.add(suffix + " is mandatory.");
				} 								
			}
			
		
			if (prefix.equalsIgnoreCase("ddls") && suffix!=null && suffix.equalsIgnoreCase("account:")) {
				strLocType = map.get("selectedLocationType");
				//Only when account has been selected on webiprompt screen
				if(strLocType[0].equalsIgnoreCase("account")) {
					String[] strSapId = request.getParameterValues("selectedCustId");
					String temp[] = new String[1];
					temp[0]= getCustomerNo(strSapId[0]);
					LOG.debug("****For Prompt " + promptName + ", Value Passed Is= " + temp[0]);
										prompt.enterValues(temp);
	
				}else if(strLocType[0].equalsIgnoreCase("All")){
					String array[] = (String[]) accountList.toArray(new String[accountList.size()]);  
					for(int m=0; m<array.length; m++){
						LOG.debug("**** When All Authorized, For Prompt " + promptName + " Value Passed Is= " + array[m]);
					}
					prompt.enterValues(array);
						
				}else{
					LOG.debug("Entering null values for " + promptName);
				}
			}
			
			if (prefix.equalsIgnoreCase("ddls") && suffix!=null && suffix.equalsIgnoreCase("Ship To:")) {
				strLocType = map.get("selectedLocationType");
				//Only when ship to has been selected on webiprompt screen
				if(strLocType[0].equalsIgnoreCase("Ship To")) {
					String[] strSapId = request.getParameterValues("selectedCustId");
					String temp[] = new String[1];
					temp[0]= geteditedCustomerNo(strSapId[0]) + " - " + getShipToSuffix(strSapId[0]);
					LOG.debug("****For Prompt " + promptName + ", Value Passed Is= " + temp[0]);
					prompt.enterValues(temp);
	
				}else if(strLocType[0].equalsIgnoreCase("All")){
					String array[] = (String[]) shipToList.toArray(new String[shipToList.size()]);  
					for(int m=0; m<array.length; m++){
						LOG.debug("**** When All Authorized, For Prompt " + promptName + " Value Passed Is= " + array[m]);
						}
					prompt.enterValues(array);
						
				}
				else{
					LOG.debug("Entering null values for " + promptName);
				}
			}
			
			if (prefix.equalsIgnoreCase("ddls") && suffix!=null && suffix.equalsIgnoreCase("Bill To:")) {
				strLocType = map.get("selectedLocationType");
				//Only when Bill To has been selected on webiprompt screen
				if(strLocType[0].equalsIgnoreCase("Bill To")) {
					String[] strSapId = request.getParameterValues("selectedCustId");
					String temp[] = new String[1];
					//Fix to match Bill to cust id with the customer batch job. No need to send suffix, since we truncate it to 000 while migrating data
					temp[0]= geteditedCustomerNo(strSapId[0]); //+ " - " + getShipToSuffix(strSapId[0]);
					LOG.debug("****For Prompt " + promptName + ", Value Passed Is= " + temp[0]);
					prompt.enterValues(temp);
	
				}else if(strLocType[0].equalsIgnoreCase("All")){
					String array[] = (String[]) billToList.toArray(new String[billToList.size()]); 
					for(int m=0; m<array.length; m++){
						LOG.debug("**** When All Authorized, For Prompt " + promptName + " Value Passed Is= " + array[m]);
						}
					prompt.enterValues(array);
						
				}
				else{
					LOG.debug("Entering null values for " + promptName);
				}
			}			
			
			
			if(errorList.size() == 0) {
				if (paramStringArray != null && paramStringArray.length > 0 && paramStringArray[0] != null && !paramStringArray[0].equals("")) {
					for(int m=0; m<paramStringArray.length; m++){
						LOG.debug("****For Prompt " + promptName + ", Value Passed Is= " + paramStringArray[m]);
						}
					prompt.enterValues(paramStringArray);
				}
				else {
 						if ( !prefix.equalsIgnoreCase("ddls") && suffix!=null)
 					{
 							LOG.debug("Entering null values for " + promptName);
 					}
				}					
			}				
		}

		if (errorList.size() > 0) {
			if(log.isDebugEnabled()){
			log.debug(".............error list greater than 0............................");
			}
			setRndrReport("false");
			Iterator<String> iterator = errorList.iterator();
			while(iterator.hasNext())
			{
				log.error("errorList+++++++++++"+ iterator.next());
				}
			
			
		}	else {
			//Set the prompts and rerun the report
			
			LOG.debug("Setting report prompts ************************************");
			document.setPrompts();

			//Set the token in case you rerun with new prompts
			String strEntry = document.getStorageToken();

			setStrToken(strEntry);

			setRndrReport("true");
		}




		return SUCCESS;
	}
}

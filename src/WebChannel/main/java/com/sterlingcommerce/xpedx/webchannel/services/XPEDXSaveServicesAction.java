package com.sterlingcommerce.xpedx.webchannel.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.core.YFSSystem;

@SuppressWarnings("serial")
public class XPEDXSaveServicesAction extends WCMashupAction {

	private static final Logger log = Logger
	.getLogger(XPEDXSaveServicesAction.class);
	private static final String customerExtnInformation = "xpedx-customer-getCustomerAllExtnInformation";
	private static final String customerShipToInformationMashUp = "xpedx-customer-getCustomerAddressInformation";
	private String bodyData;
	private List<String> serviceProviderList;
	private List<String> addressList;
	private String contact;
	private String phone;
	private String notes;
	private String serviceProvider;
	private String serviceProviderNumber;
	private String errorMesage;
	private String address1;
	private String address2;
	private String address3;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private String email;
	private String imageUrl;
	private String accountName = "";
	private String billtoCustid = "";
	private String divisionName = "";
	private String serviceProviderNone = "";
	private String attention = "";
	private String storefrontId = "";
	private String salesProfessional = "";
	private String serviceProviderNoneUPS = "";
	private String serviceProviderNumberFEDEX="";
	public String getServiceProviderNumberFEDEX() {
		return serviceProviderNumberFEDEX;
	}

	public void setServiceProviderNumberFEDEX(String serviceProviderNumberFEDEX) {
		this.serviceProviderNumberFEDEX = serviceProviderNumberFEDEX;
	}

	public String getServiceProviderNumberUPS() {
		return serviceProviderNumberUPS;
	}

	public void setServiceProviderNumberUPS(String serviceProviderNumberUPS) {
		this.serviceProviderNumberUPS = serviceProviderNumberUPS;
	}
	private String serviceProviderNumberUPS="";
	
	public String getServiceProviderNoneUPS() {
		return serviceProviderNoneUPS;
	}

	public void setServiceProviderNoneUPS(String serviceProviderNoneUPS) {
		this.serviceProviderNoneUPS = serviceProviderNoneUPS;
	}

	public String getServiceProviderNoneFX() {
		return serviceProviderNoneFX;
	}

	public void setServiceProviderNoneFX(String serviceProviderNoneFX) {
		this.serviceProviderNoneFX = serviceProviderNoneFX;
	}
	private String serviceProviderNoneFX = "";
	
	
	
	public String getSalesProfessional() {
		return salesProfessional;
	}

	public void setSalesProfessional(String salesProfessional) {
		this.salesProfessional = salesProfessional;
	}

	public String getStorefrontId() {
		return storefrontId;
	}

	public void setStorefrontId(String storefrontId) {
		this.storefrontId = storefrontId;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public String getServiceProviderNone() {
		return serviceProviderNone;
	}

	public void setServiceProviderNone(String serviceProviderNone) {
		this.serviceProviderNone = serviceProviderNone;
	}

	public String getBilltoFullName() {
		return accountName;
	}

	public void setBilltoFullName(String billtoFullName) {
		this.accountName = billtoFullName;
	}

	public String getBilltoCustid() {
		return billtoCustid;
	}

	public void setBilltoCustid(String billtoCustid) {
		this.billtoCustid = billtoCustid;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	private static final String SUCCESS = "success";
	private String messageType="sampleRequest";

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getErrorMesage() {
		return errorMesage;
	}

	public void setErrorMesage(String errorMesage) {
		this.errorMesage = errorMesage;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	/*public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
*/
	public String getServiceProviderNumber() {
		return serviceProviderNumber;
	}

	public void setServiceProviderNumber(String serviceProviderNumber) {
		this.serviceProviderNumber = serviceProviderNumber;
	}

	public List<String> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<String> addressList) {
		this.addressList = addressList;
	}

	public String getBodyData() {
		return bodyData;
	}

	public void setBodyData(String bodyData) {
		this.bodyData = bodyData;
	}

	public void setServiceProviderList(List<String> serviceProviderList) {
		this.serviceProviderList = serviceProviderList;
	}

	public List<String> getServiceProviderList() {
		return this.serviceProviderList;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public String execute() {

		String returnVal = SUCCESS;
		try {
			XPEDXShipToCustomer shipToCustomer = null;
			List<String> customerAddressList = null;
			Document outputDocSales = null;  //JIRA 3756 Changes done 
			String customerId = getWCContext().getCustomerId();
			storefrontId = getWCContext().getStorefrontId();
			StringBuffer sb = new StringBuffer();
			String saleRepEmail = "";
			String ccEMail = "";
			String extnECsr1EmailID = "";
			String extnECsr2EmailID = "";
			String sapCustomerKey = "";
			/**
			 * JIRA 3756 -Flag added .
			 */
			boolean checkCCGFlag = false;
			boolean checkCCPFlag = false;
			
			/**
			 * JIRA 3756 -End
			 */
			
			// Getting ship to address and customer details

			shipToCustomer = XPEDXWCUtils.getShipToAdress(customerId,
					storefrontId);
			
			
						/**
			 * JIRA 243
			 * Modified getCustomerDetails method to consider the mashup to be invoked
			 * so that, we get only the required information - here Customer Adress List.
			 * @param inputItems
			 * @return
			 */
			/*
			 * 
			 * JIRA 3756 Code changes Start			
			 */
			outputDocSales = XPEDXWCUtils.getCustomerDetails(getWCContext().getCustomerId(), getWCContext()
					.getStorefrontId(), customerExtnInformation);
			
			Element customerElement = null;
			
			if(outputDocSales != null){
			customerElement = outputDocSales.getDocumentElement();
			
			HashMap<String,String> csrEmailMAP = new HashMap<String,String>();
			csrEmailMAP = getCSREmailID(customerElement);
			if(csrEmailMAP.size()>0){
				if(csrEmailMAP.get("csr1EmailID")!=null && csrEmailMAP.get("csr1EmailID").trim().length() > 0){
					extnECsr1EmailID = csrEmailMAP.get("csr1EmailID");
				}
				if(csrEmailMAP.get("csr2EmailID")!=null && csrEmailMAP.get("csr2EmailID").trim().length() > 0){
					extnECsr2EmailID = csrEmailMAP.get("csr2EmailID");
				}
			}
			
			
			
			HashMap<String,String> salesProfessionalMap = new HashMap<String,String>();
			salesProfessionalMap = getSalesRepInfo(customerElement);
			if(salesProfessionalMap.size() > 0){
				
				if(salesProfessionalMap.get("salesProfessional")!=null && salesProfessionalMap.get("salesProfessional").trim().length() >0){
					salesProfessional = salesProfessionalMap.get("salesProfessional");
				}
				if(salesProfessionalMap.get("salesRepemailId")!=null && salesProfessionalMap.get("salesRepemailId").trim().length() > 0){
					saleRepEmail = salesProfessionalMap.get("salesRepemailId");
				}
			}
			}
			
			/*
			 * 
			 * JIRA 3756 Code changes End			
			 */
			
			if (shipToCustomer != null) {
				customerAddressList = shipToCustomer.getAddressList();
			}

			setAddressList(customerAddressList);

			List<String> list = new ArrayList<String>();
			list.add("FedEx");
			list.add("UPS");
			list.add("None");
			setServiceProviderList(list);

			String customerEmail = "";
 
			/**
			 JIRA 3160 -Start
			 From email should be ebusiness@<brand>.com
			 */
			// Sending customer details to get the email id of customer
			/*if (null != customerDetailsDoc) {
				customerEmail = getCustomerEmail(customerDetailsDoc);
					}*/
			/**
			 JIRA 3160 -End
			 */
			
			/**
			 JIRA 3160 -Start
			 From email address should be ebusiness@brand.com
			 */
			
			if(storefrontId!=null && storefrontId.trim().length() > 0){
				String userName = YFSSystem.getProperty("fromAddress.username");
				sb.append(userName).append("@").append(createHostName());
				customerEmail = sb.toString();
				
			}
			 /**
			  JIRA 3160 -Start
			  Changes for imageLogo
			  */
			
			String imageUrl = "";
			if(storefrontId!=null && storefrontId.trim().length() > 0){
			String imageName = XPEDXWCUtils.getLogoName(storefrontId);
			String imagesRootFolder = XPEDXWCUtils.getImagesRootFolder(ServletActionContext.getRequest());
			if(imagesRootFolder!=null && imagesRootFolder.trim().length() > 0 && imageName!=null && imageName.trim().length() > 0){
				imageUrl = imagesRootFolder + imageName;
				setImageUrl(imageUrl);
			   }
			}
			/**
			 JIRA 3160 -End
			 */
			
			// Getting email id of sample room
			/*Element rootEle = null;

			rootEle = prepareAndInvokeMashup("xpedxgetCustomerList");
*/			
			/**
			 JIRA 3160 -Start
			 Changed toEmail address as per Jira 3160.
			 */

			/*String sampleRoomEmail = "";
			if (null != rootEle) {
				sampleRoomEmail = getSampleRoomEmail(rootEle);
				sampleRoomEmail = "apmca786@gmail.com"; //To Mail
			}*/
			/**
			 JIRA 3160 -End
			 */
			/**
			 JIRA 3160 -Start
			 */
			
			XPEDXShipToCustomer shipToCustomerObject = new XPEDXShipToCustomer();
			shipToCustomerObject =(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			if(shipToCustomerObject!=null){
		
            if(shipToCustomerObject.getBillTo()!=null && shipToCustomerObject.getBillTo().getExtnCustomerName()!=null && shipToCustomerObject.getBillTo().getExtnCustomerName().trim().length() > 0){
				accountName = shipToCustomerObject.getBillTo().getExtnCustomerName();
            }
			 sapCustomerKey = shipToCustomerObject.getBillTo().getParentCustomerKey();
			}
			
			billtoCustid = shipToCustomerObject.getBillTo().getCustomerID();
			
			String csrEmailID = XPEDXWCUtils.setCSREmails(extnECsr1EmailID,extnECsr2EmailID);
			
						
			if(csrEmailID != null && csrEmailID.trim().length() > 0 && saleRepEmail!=null && saleRepEmail.trim().length() > 0){
				ccEMail = csrEmailID + XPEDXConstants.EMAILIDSEPARATOR +saleRepEmail;
			}else if(saleRepEmail != null && saleRepEmail.trim().length() > 0){
				if(log.isDebugEnabled()){
				log.debug("ccEMail"+ccEMail);
				}
				ccEMail = saleRepEmail;				
			}else if(csrEmailID != null && csrEmailID.trim().length() > 0){
				ccEMail = csrEmailID;				
				
			}
			/*
			 * JIRA 3756 -Changes done for JIRA 3756 START 
			 * 
			 */
			if(ccEMail !=null && ccEMail.trim().length() >0 && getEmail() !=null && getEmail().trim().length() > 0){
				ccEMail = ccEMail + XPEDXConstants.EMAILIDSEPARATOR +getEmail();
			}else if(getEmail()!=null && getEmail().trim().length() > 0){
				ccEMail = getEmail();
			}
			/*
			 * JIRA 3756 -Changes done for JIRA 3756 END 
			 * 
			 */
			String paperFSObj = "";
			String paperPSObj = "";
			String checkbothToken = "";
			boolean bothFlag = false;
			List<String> generalList = new ArrayList<String>();
			List<String> paperList = new ArrayList<String>();
			boolean checkbothFlag = false;
			
			if(getBodyData()!=null && getBodyData().trim().length() > 0){
				Pattern p = Pattern.compile("[+\\s][=\\s][_\\s]");
				String[] getPaperNonPaper = 
		                 p.split(getBodyData());
				for(int k=0;k<getPaperNonPaper.length;k++){
					checkbothToken =  getPaperNonPaper[k];
					if(checkbothToken.equalsIgnoreCase("true")){
						bothFlag = true;
					}
				}
				if(bothFlag){
					Pattern p1 = Pattern.compile("[+\\s][=\\s][_\\s]");
					String[] getBothPaperNonPaper = 
			                 p1.split(getBodyData());
					for(int b=0;b<getBothPaperNonPaper.length;b++){
						if(!getBothPaperNonPaper[b].equalsIgnoreCase("true") && !checkbothFlag){
							generalList.add(getBothPaperNonPaper[b]);
							paperFSObj = "facilitySupplies";
							
						}
						else{
							checkbothFlag = true;
							if(checkbothFlag){
								paperList.add(getBothPaperNonPaper[b]);
								paperPSObj = "PaperSupplies";
							}
						}
					}
					
					
				}else{				
				
		        int i = 1;
		        for (int j=0; j<getPaperNonPaper.length; j++){
					String token = getPaperNonPaper[j];//st.nextToken();
				if(token.equalsIgnoreCase("facilitySupplies")){
					paperFSObj = token;
				}else if(token.equalsIgnoreCase("PaperSupplies")){
					paperPSObj = token;

				}
		        }}
		        
			}
			String toEmailGen = getPaperorGeneralPaperEmail(paperFSObj);
			String toEmailPaper = getPaperorGeneralPaperEmail(paperPSObj);
			String sampleServiceRequest = "";
			
			/*JIRA 3756 -Start Check empty condition in toEmail address .
			 * If toEmail is empty then ccEamil should be toEmail .
			 * 
			 * */
			if(((toEmailGen == null || toEmailGen.trim().length() == 0) || (toEmailPaper == null || toEmailPaper.trim().length() == 0)) && (ccEMail!=null && ccEMail.trim().length() > 0)){
				if((toEmailGen == null || toEmailGen.trim().length() == 0) && (paperFSObj.equalsIgnoreCase("facilitySupplies"))){
					toEmailGen = ccEMail;
					ccEMail = "";
				}
				if((toEmailPaper == null || toEmailPaper.trim().length() == 0) && (paperPSObj.equalsIgnoreCase("PaperSupplies"))){
					toEmailPaper = ccEMail;
					ccEMail = "";
				}
				
			}else
			if((toEmailGen == null || toEmailGen.trim().length() > 0) && (toEmailPaper != null && toEmailPaper.trim().length() > 0) && (ccEMail!=null && ccEMail.trim().length() > 0)){
				if(paperFSObj.equalsIgnoreCase("facilitySupplies")){
				toEmailGen = ccEMail;
				checkCCGFlag = true;
				}
				
			}else if((toEmailGen != null && toEmailGen.trim().length() > 0 ) && (toEmailPaper == null || toEmailPaper.trim().length() > 0) && (ccEMail!=null && ccEMail.trim().length() > 0)){
				if(paperPSObj.equalsIgnoreCase("PaperSupplies")){
				toEmailPaper = ccEMail;
				checkCCPFlag = true;
				}
			}
			/**
			 * JIRA 3756 -End.
			 * 
			 */
			
			/**
			 JIRA 3160 -End
			 */
			/*
			 * Below code which make the xml for paper and non paper
			 * */
			if(bothFlag){
				
				if(	toEmailGen != null && toEmailGen.trim().length() > 0){
					// Creating input xml to send an email
					Document outputDoc = null;
					sampleServiceRequest = "SampleServiceRequest";
					if(checkCCGFlag){
						ccEMail = "";
					}
					Element input = createInputXMLForGeneral(customerEmail, toEmailGen,ccEMail,generalList,sampleServiceRequest);
					String inputXml = SCXmlUtil.getString(input);
					LOG.debug("XPEDXSaveServicesAction() Input XML: " + inputXml);
					Object obj = WCMashupHelper.invokeMashup(
							"SendSampleServiceRequest", input, wcContext
									.getSCUIContext());
					outputDoc = ((Element) obj).getOwnerDocument();
					if (null != outputDoc) {
						LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));  //Jira 3554 Changes done
					}
				}
				if(	toEmailPaper != null && toEmailPaper.trim().length() > 0){					
				// Creating input xml to send an email
				Document outputDoc1 = null;
				sampleServiceRequest = "SampleServiceRequestPaper";	
				if(checkCCPFlag){
					ccEMail = "";
				}
				Element input = createInputXMLForGeneral(customerEmail, toEmailPaper,ccEMail,paperList,sampleServiceRequest);
				String inputXml = SCXmlUtil.getString(input);
				LOG.debug("XPEDXSaveServicesAction() Input XML : " + inputXml);
				Object obj = WCMashupHelper.invokeMashup(
						"SendSampleServiceRequest", input, wcContext
								.getSCUIContext());
				outputDoc1 = ((Element) obj).getOwnerDocument();
				if (null != outputDoc1) {
					LOG.debug("Output1 XML: " + SCXmlUtil.getString((Element) obj)); //Jira 3554 Changes done
				}
			}	
			}else{
		if(	toEmailGen != null && toEmailGen.trim().length() > 0 || toEmailPaper != null && toEmailPaper.trim().length() > 0){
			String toMailaddres = "";
			if(toEmailGen != null && toEmailGen.trim().length() > 0){
				toMailaddres = toEmailGen;
				sampleServiceRequest = "SampleServiceRequest";
				if(checkCCGFlag){
					ccEMail = "";
				}
				
			}else if(toEmailPaper != null && toEmailPaper.trim().length() > 0){
				toMailaddres = toEmailPaper;
				sampleServiceRequest = "SampleServiceRequestPaper";				
				if(checkCCPFlag){
					ccEMail = "";
				}
				
			}
			// Creating input xml to send an email
			Document outputDoc = null;
			Element input = createInputXML(customerEmail, toMailaddres,ccEMail,sampleServiceRequest);
			String inputXml = SCXmlUtil.getString(input);
			LOG.debug("XPEDXSaveServicesAction() Input XML: " + inputXml);
			Object obj = WCMashupHelper.invokeMashup(
					"SendSampleServiceRequest", input, wcContext
							.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();  SCXmlUtil.getString(outputDoc);
			if (null != outputDoc) {
				LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj)); //Jira 3554 Changes done
			}
		}
		}
		
		} catch (Exception ex) {
			if (ex.getMessage()!=null) {
				setErrorMesage("Your request can not be processed at this time due to technical issues. Please contact administrator");
				LOG.error("Issue in XPEDXSaveServicesAction"+ex.getMessage()); //Jira 3554 Changes done 
				return returnVal;
			}
			returnVal = "error";
		}
		return returnVal;
	}

	private String getSampleRoomEmail(Element rootEle) {

		Element extnEle = (Element) rootEle.getFirstChild().getFirstChild();
		String sampleEmail = extnEle.getAttribute("ExtnSampleRoomEmailAddress");
		return sampleEmail;
	}
	public String getPaperorGeneralPaperEmail(String paperFSObj){
		String extnDivEmailNonPaper = "";
		String extnDivEmailPaper = "";
		String paperEmailID = "";
		try{
		Document outputDoc = null;
		
		outputDoc = XPEDXWCUtils.getCustomerDetails(getWCContext().getCustomerId(), getWCContext()
				.getStorefrontId(), customerExtnInformation);
		Element customerElement = outputDoc.getDocumentElement();
		
		Element custExtnEle = XMLUtilities.getElement(customerElement, "Extn");
			
		String custDivison = SCXmlUtil.getAttribute(custExtnEle, "ExtnCustOrderBranch");
		String envId= SCXmlUtil.getAttribute(custExtnEle, "ExtnEnvironmentCode");
		if(custDivison!=null && custDivison.trim().length() > 0)
		{
			if(envId!=null && envId.trim().length()>0 && !custDivison.contains("_")){
				outputDoc = XPEDXWCUtils.getOrganizationDetails(custDivison+"_"+envId);
				if(log.isDebugEnabled()){
				log.debug(""+SCXmlUtil.getString(outputDoc));
				}
				
			}else{
				outputDoc = XPEDXWCUtils.getOrganizationDetails(custDivison);
			}
			Element organizationElement = null;
			organizationElement = outputDoc.getDocumentElement();
				if (organizationElement != null) {
					Element eleobjdivi = (Element) organizationElement
							.getFirstChild();
					divisionName = eleobjdivi.getAttribute("OrganizationName");
					Element eleobj = (Element) organizationElement
							.getFirstChild().getFirstChild();
					extnDivEmailNonPaper = eleobj
							.getAttribute("ExtnDivEmailNonPaper"); // ExtnDivEmailPaper
					extnDivEmailPaper = eleobj
							.getAttribute("ExtnDivEmailPaper"); // ExtnDivEmailPaper
				}
			
		}
		
		}catch (Exception ex) {
		if (ex.getMessage()!=null) {
			setErrorMesage("Your request can not be processed at this time due to technical issues. Please contact administrator");
		}}
			
		if(paperFSObj!=null && paperFSObj.trim().length() > 0 && paperFSObj.equalsIgnoreCase("facilitySupplies") ){
			paperEmailID = extnDivEmailNonPaper;
		}else if(paperFSObj!=null && paperFSObj.trim().length() > 0 && paperFSObj.equalsIgnoreCase("PaperSupplies")){
			paperEmailID = extnDivEmailPaper;
		}		
			
       return paperEmailID;
	}
	/*
	 * 
	 * JIRA 3756 Code changes Start -Code Commented 		
	 */
	

	/*private String getCustomerEmail(Document customerDetailsDoc) {
		Element customerDetailsElement = customerDetailsDoc
				.getDocumentElement();
		NodeList custAddAddList = customerDetailsElement
				.getElementsByTagName("CustomerAdditionalAddressList");
		String emailId = "";
		for (int i = 0; i < custAddAddList.getLength(); i++) {
			Element customerAddAddEle = (Element) custAddAddList.item(i);
			Element personalInfoEle = (Element) customerAddAddEle
					.getFirstChild().getFirstChild();
			emailId = personalInfoEle.getAttribute("EMailID");
		}

		return emailId;
	}
*/
	/*
	 * 
	 * JIRA 3756 Code changes End -Code Commented			
	 */
	
	private Element createInputXML(String customerEmail, String sampleRoomEmail ,String ccEMail,String sampleServiceRequest) {
 
		String subjectEmail = "";
		String headerEmail = "";
		String serviceProviderNumbersplit = "";
		String ServiceProvider = "";
		Document templateEmailDoc = YFCDocument.createDocument("Emails")
				.getDocument();
		Element templateElement = templateEmailDoc.getDocumentElement();
		Element emailElement = templateEmailDoc.createElement("Email");
		emailElement.setAttribute("Contact", getContact());
		emailElement.setAttribute("Phone", getPhone());
		emailElement.setAttribute("Notes", getNotes());
		if((getServiceProviderNumberFEDEX()!=null && getServiceProviderNumberFEDEX().trim().length()  > 0)){
			emailElement.setAttribute("ServiceProvider", "FedEx");
			serviceProviderNumbersplit =  getServiceProviderNumberFEDEX().replaceAll(",", "");
			
			
		}else if((getServiceProviderNumberUPS()!=null && getServiceProviderNumberUPS().trim().length()  > 0)){
			emailElement.setAttribute("ServiceProvider", "UPS");
			serviceProviderNumbersplit =  getServiceProviderNumberUPS().replaceAll(",", "");
			
			
		}
		emailElement.setAttribute("ServiceProviderNumber",
				serviceProviderNumbersplit);
		
		if(salesProfessional!=null && salesProfessional.trim().length() > 0){
			emailElement.setAttribute("salesProfessional", getSalesProfessional());			
		}
		if(attention!=null && attention.trim().length() > 0){
			emailElement.setAttribute("Attention", getAttention());
		}
		if (address1 != null && address1.trim().length() > 0) {
			emailElement.setAttribute("address1", getAddress1());
		}
		if (address2 != null && address2.trim().length() > 0) {
			emailElement.setAttribute("address2", getAddress2());
		}
		if (address3 != null && address3.trim().length() > 0) {
			emailElement.setAttribute("address3", getAddress3());
		}
		if (city != null && city.trim().length() > 0) {
			emailElement.setAttribute("city", getCity());
		}
		if (state != null && state.trim().length() > 0) {
			emailElement.setAttribute("state", getState());
		}
		if (zipCode != null && zipCode.trim().length() > 0) {
			emailElement.setAttribute("zipCode", getZipCode());
		}
		if (country != null && country.trim().length() > 0) {
			emailElement.setAttribute("country", getCountry());
		}
		if ((customerEmail != null && customerEmail.trim().length() == 0)
				|| (sampleRoomEmail != null && sampleRoomEmail.trim().length() == 0)){
			setErrorMesage("Your 'From Email'/'To Email' addresses can not be empty in the profile");
		}
		if(getImageUrl()!=null && getImageUrl().trim().length() > 0){
			emailElement.setAttribute("ImageUrl",getImageUrl());
		}
		if(getBilltoCustid()!=null && getBilltoCustid().trim().length()>0){
			emailElement.setAttribute("BillToCustid",truncateBillToCustomerId(getBilltoCustid()));
		}
		if(getDivisionName()!=null && getDivisionName().trim().length()>0){
			emailElement.setAttribute("DivisionName",getDivisionName());
			
		}
		if(getBilltoFullName()!=null && getBilltoFullName().trim().length() > 0){
			emailElement.setAttribute("BilltoFullName", getBilltoFullName());
		}
		if(getEmail()!=null && getEmail().trim().length() > 0){
			emailElement.setAttribute("ExternalEmail", getEmail());
		}
		emailElement.setAttribute("FromEmail", customerEmail);
		emailElement.setAttribute("ToEmail", sampleRoomEmail);
		emailElement.setAttribute("CCEmail",ccEMail);
		if(sampleServiceRequest.equalsIgnoreCase("SampleServiceRequestPaper")){
			subjectEmail = createHostName() + " Paper Sample Request Notification";
			headerEmail = "Paper";
		}
		else{
			subjectEmail = createHostName() + " General Sample Request Notification";
			headerEmail = "General";
		}
		
		emailElement.setAttribute("subjectEmail",subjectEmail);
		emailElement.setAttribute("headerEmail",headerEmail);
		
		
		//emailElement.setAttribute("RequestType", "SampleServiceRequest");
		emailElement.setAttribute("RequestType", sampleServiceRequest);


		List<String> cutomerAddressList = getAddressList();
		Iterator<String> addressListIterator = cutomerAddressList.iterator();

		Element shippingAddressElement = null;
		Element sampleRequestElement = null;

		while (addressListIterator.hasNext()) {
			shippingAddressElement = templateEmailDoc
					.createElement("ShippingAddress");
			shippingAddressElement.setAttribute("address", addressListIterator
					.next());
			emailElement.appendChild(shippingAddressElement);
		}

		if (null == getBodyData() || getBodyData().equals("")) {
			sampleRequestElement = templateEmailDoc
					.createElement("SampleRequest");
			sampleRequestElement.setAttribute("MfgSku", "");
			sampleRequestElement.setAttribute("Mfg", "");
			sampleRequestElement.setAttribute("ItemNumber", "");
			sampleRequestElement.setAttribute("Description", "");
			sampleRequestElement.setAttribute("Qty", "");
			emailElement.appendChild(sampleRequestElement);
		} else {
//			StringTokenizer st = new StringTokenizer(getBodyData(), "+=_");
			Pattern p = Pattern.compile("[+\\s][=\\s][_\\s]");
	        // Split input with the pattern
	        String[] result = 
	                 p.split(getBodyData());
	        int i = 1;
//			while (st.hasMoreTokens())
			for (int j=1; j<result.length; j++){
				String token = result[j];//st.nextToken();
				if (token.equals("*#?")) {
					token = "";
				}

				if (i == 1) {
					sampleRequestElement = templateEmailDoc
							.createElement("SampleRequest");
					
					sampleRequestElement.setAttribute("MfgSku", token);
				}
				if (i == 2) {
					sampleRequestElement.setAttribute("Mfg", token);
				}
				if (i == 3) {
					sampleRequestElement.setAttribute("ItemNumber", token);
				}
				if (i == 4) {
					sampleRequestElement.setAttribute("Description", token);
				}
				if (i == 5) {
					sampleRequestElement.setAttribute("Qty", token);
					emailElement.appendChild(sampleRequestElement);
				}
				i++;
				if (i == 6) {
					i = 1;
				}
			}
		}
		templateElement.appendChild(emailElement);
		if(log.isDebugEnabled()){
		log.debug("*************************EMAIL INFO START**************************************");
		log.debug(SCXmlUtil.getString(emailElement));
		log.debug("*************************EMAIL INFO END**************************************");
		}

		return templateElement;
	}
	private Element createInputXMLForGeneral(String customerEmail, String sampleRoomEmail ,String ccEMail,List generalPaper,String sampleServiceRequest) {
		
		String subjectEmail = "";
		String headerEmail = "";
		String serviceProviderNumbersplit = "";
		String ServiceProvider = "";
		Document templateEmailDoc = YFCDocument.createDocument("Emails")
				.getDocument();
		Element templateElement = templateEmailDoc.getDocumentElement();
		Element emailElement = templateEmailDoc.createElement("Email");
		emailElement.setAttribute("Contact", getContact());
		emailElement.setAttribute("Phone", getPhone());
		emailElement.setAttribute("Notes", getNotes());
		
		if((getServiceProviderNumberFEDEX()!=null && getServiceProviderNumberFEDEX().trim().length()  > 0)){
			emailElement.setAttribute("ServiceProvider", "FedEx");
			serviceProviderNumbersplit =  getServiceProviderNumberFEDEX().replaceAll(",", "");
			
			
		}else if((getServiceProviderNumberUPS()!=null && getServiceProviderNumberUPS().trim().length()  > 0)){
			emailElement.setAttribute("ServiceProvider", "UPS");
			serviceProviderNumbersplit =  getServiceProviderNumberUPS().replaceAll(",", "");
			
			
		}
		emailElement.setAttribute("ServiceProviderNumber",
				serviceProviderNumbersplit);
		if(salesProfessional!=null && salesProfessional.trim().length() > 0){
			emailElement.setAttribute("salesProfessional", getSalesProfessional());			
		}
		if(attention!=null && attention.trim().length() > 0){
			emailElement.setAttribute("Attention", getAttention());
		}
		if (address1 != null && address1.trim().length() > 0) {
			emailElement.setAttribute("address1", getAddress1());
		}
		if (address2 != null && address2.trim().length() > 0) {
			emailElement.setAttribute("address2", getAddress2());
		}
		if (address3 != null && address3.trim().length() > 0) {
			emailElement.setAttribute("address3", getAddress3());
		}
		if (city != null && city.trim().length() > 0) {
			emailElement.setAttribute("city", getCity());
		}
		if (state != null && state.trim().length() > 0) {
			emailElement.setAttribute("state", getState());
		}
		if (zipCode != null && zipCode.trim().length() > 0) {
			emailElement.setAttribute("zipCode", getZipCode());
		}
		if (country != null && country.trim().length() > 0) {
			emailElement.setAttribute("country", getCountry());
		}
		if ((customerEmail != null && customerEmail.trim().length() == 0)
				|| (sampleRoomEmail != null && sampleRoomEmail.trim().length() == 0)){
			setErrorMesage("Your 'From Email'/'To Email' addresses can not be empty in the profile");
		}
		if(getImageUrl()!=null && getImageUrl().trim().length() > 0){
			emailElement.setAttribute("ImageUrl",getImageUrl());
		}
		if(getBilltoCustid()!=null && getBilltoCustid().trim().length()>0){
			emailElement.setAttribute("BillToCustid",truncateBillToCustomerId(getBilltoCustid()));
		}
		if(getDivisionName()!=null && getDivisionName().trim().length()>0){
			emailElement.setAttribute("DivisionName",getDivisionName());
			
		}
		if(getBilltoFullName()!=null && getBilltoFullName().trim().length() > 0){
			emailElement.setAttribute("BilltoFullName", getBilltoFullName());
		}
		if(getEmail()!=null && getEmail().trim().length() > 0){
			emailElement.setAttribute("ExternalEmail", getEmail());
		}
		emailElement.setAttribute("FromEmail", customerEmail);
		emailElement.setAttribute("ToEmail", sampleRoomEmail);
		emailElement.setAttribute("CCEmail",ccEMail);
		if(sampleServiceRequest.equalsIgnoreCase("SampleServiceRequestPaper")){
			subjectEmail = createHostName() + " Paper Sample Request Notification";
			headerEmail = "Paper";
		}
		else{
			subjectEmail = createHostName() + " General Sample Request Notification";
			headerEmail = "General";
			
		}
		emailElement.setAttribute("subjectEmail",subjectEmail);
		emailElement.setAttribute("headerEmail",headerEmail);
		
		//emailElement.setAttribute("RequestType", "SampleServiceRequest");
		emailElement.setAttribute("RequestType", sampleServiceRequest);
		List<String> cutomerAddressList = getAddressList();
		
		Iterator<String> addressListIterator = cutomerAddressList.iterator();

		Element shippingAddressElement = null;
		Element sampleRequestElement = null;

		while (addressListIterator.hasNext()) {
			shippingAddressElement = templateEmailDoc
					.createElement("ShippingAddress");
			shippingAddressElement.setAttribute("address", addressListIterator
					.next());
			emailElement.appendChild(shippingAddressElement);
		}

		if (null == getBodyData() || getBodyData().equals("")) {
			sampleRequestElement = templateEmailDoc
					.createElement("SampleRequest");
			sampleRequestElement.setAttribute("MfgSku", "");
			sampleRequestElement.setAttribute("Mfg", "");
			sampleRequestElement.setAttribute("ItemNumber", "");
			sampleRequestElement.setAttribute("Description", "");
			sampleRequestElement.setAttribute("Qty", "");
			emailElement.appendChild(sampleRequestElement);
		} else if(generalPaper.size()>0){
		    if(log.isDebugEnabled()){
			log.debug(generalPaper);
		    }
			//Iterator<String> bothList = generalPaper.iterator();
			
			int i = 1;
			for(int j=1;j<generalPaper.size()-1;j++){
				
				if (i == 1) {
					sampleRequestElement = templateEmailDoc
							.createElement("SampleRequest");
					
					sampleRequestElement.setAttribute("MfgSku", generalPaper.get(j).toString());
					}
				if (i == 2) {
					sampleRequestElement.setAttribute("Mfg", generalPaper.get(j).toString());
					
					
				}
				if (i == 3) {
					sampleRequestElement.setAttribute("ItemNumber", generalPaper.get(j).toString());
					
					
				}
				if (i == 4) {
					sampleRequestElement.setAttribute("Description", generalPaper.get(j).toString());
					
					
				}
				if (i == 5) {
					sampleRequestElement.setAttribute("Qty", generalPaper.get(j).toString());
					emailElement.appendChild(sampleRequestElement);
				}
				i++;
				if (i == 6) {
					i = 1;
				}
			}
			}
			
		templateElement.appendChild(emailElement);
		if(log.isDebugEnabled()){
		log.debug("*************************EMAIL INFO START**************************************");
		log.debug(SCXmlUtil.getString(emailElement));
		log.debug("*************************EMAIL INFO END**************************************");
		}

		return templateElement;
	}
/*
 * getSalesRepInfo method which get saler professional first name and last name .
 * */
	/*
	 * 
	 * JIRA 3756 Code changes Start			
	 */
	
	public static HashMap<String, String> getSalesRepInfo(Element customerElement) throws XPathExpressionException {
 		
		 String primarySalesRepName = "";
		 String salesRepemailId = "";
		 HashMap<String,String> salesProfessionalplusEmailMap = new HashMap<String,String>();
		 ArrayList<Element> salesRepInfo= SCXmlUtil.getElements(customerElement, "ParentCustomer/Extn/XPEDXSalesRepList/XPEDXSalesRep");		 
		 
		if(salesRepInfo != null){
			//Start EB-3366
			////If there is only one sales rep assigned to a customer
			if(salesRepInfo.size()==1){
				Element salesRepElement=salesRepInfo.get(0);
				salesProfessionalplusEmailMap = getSalesProMap(salesRepElement);
					
			}
			////If there is more than one sales rep assigned to a customer
			else if(salesRepInfo.size() > 1){
				boolean primarySalesRepFound = false;
				for(Element salesRepElement :salesRepInfo){
					if(salesRepElement != null)
					{
						String isPrimarySalesRep = SCXmlUtil.getAttribute(salesRepElement, "PrimarySalesRepFlag");
						if(isPrimarySalesRep!=null && isPrimarySalesRep.equalsIgnoreCase("Y") && !primarySalesRepFound)
						{   primarySalesRepFound = true;
							salesProfessionalplusEmailMap = getSalesProMap(salesRepElement);						
						}		
					}			
						
				}
				//If there is more than one sales rep assigned to a customer, and none of the sales rep is assigned as primary, then get the details of first sales rep(the existing coe)
				if(!primarySalesRepFound){
					Element salesRepElement=salesRepInfo.get(0);
					salesProfessionalplusEmailMap = getSalesProMap(salesRepElement);
				}

			}
		}		
	 	return salesProfessionalplusEmailMap;
	 	}
	
	private static HashMap<String, String>getSalesProMap(Element salesRepElement){
		HashMap<String,String> salesProfessionalplusEmailMap = new HashMap<String,String>();
		String primarySalesRepName = "";
		String salesRepemailId = "";
		String firstName = SCXmlUtil.getXpathAttribute(salesRepElement, "YFSUser/ContactPersonInfo/@FirstName");
		String lastName = SCXmlUtil.getXpathAttribute(salesRepElement, "YFSUser/ContactPersonInfo/@LastName");
		salesRepemailId = SCXmlUtil.getXpathAttribute(salesRepElement, "YFSUser/ContactPersonInfo/@EMailID");
			if(firstName !=null && firstName.trim().length() > 0 && lastName !=null && lastName.trim().length() > 0){
				primarySalesRepName = firstName + " " + lastName ;
			}else if(firstName !=null && firstName.trim().length() > 0){
				primarySalesRepName = firstName;
			}else if(lastName !=null && lastName.trim().length() > 0){
				primarySalesRepName = lastName ; 
			}
			if(salesRepemailId!=null && salesRepemailId.trim().length() > 0){
				salesProfessionalplusEmailMap.put("salesRepemailId",salesRepemailId);
			}
			if(primarySalesRepName!=null && primarySalesRepName.trim().length() > 0){
				salesProfessionalplusEmailMap.put("salesProfessional", primarySalesRepName);
			}
		return salesProfessionalplusEmailMap;
	}
////End EB-3366
	private HashMap<String,String> getCSREmailID(Element customerElement) throws XPathExpressionException{
		
		Element csr1CustServEle = null;
		Element csr2CustServEle = null;
		HashMap<String,String> storeCSREmail = new HashMap<String,String>();
		Element custParntEle = XMLUtilities.getElement(customerElement, "ParentCustomer");
		Element CustExtnParntEle=XMLUtilities.getElement(custParntEle,"Extn");
		String custCSR1UserKey = SCXmlUtil.getAttribute(CustExtnParntEle, "ExtnECSR1Key");
		String custCSR2UserKey = SCXmlUtil.getAttribute(CustExtnParntEle, "ExtnECSR2Key");
		if(custCSR1UserKey!=null && custCSR1UserKey.trim().length()>0) {
			 csr1CustServEle = getUserPersonInfo(custCSR1UserKey);
			 String csr1EmailID = SCXmlUtil.getXpathAttribute(csr1CustServEle, "ContactPersonInfo/@EMailID");
			 storeCSREmail.put("csr1EmailID", csr1EmailID);
				
		}					
		if(custCSR2UserKey!=null && custCSR2UserKey.trim().length()>0) {
			csr2CustServEle = getUserPersonInfo(custCSR2UserKey);
			String csr2EmailID = SCXmlUtil.getXpathAttribute(csr2CustServEle, "ContactPersonInfo/@EMailID");
			 storeCSREmail.put("csr2EmailID", csr2EmailID);
				
		}
		return storeCSREmail;
	}
	/**
	 * JIRA 3756 Changes start for geting CSR email id .
	 * @param userKey
	 * @param loginId
	 * @return
	 */
	private Element getUserPersonInfo(String userKey) {
		IWCContext wcContext = WCContextHelper
		.getWCContext(ServletActionContext.getRequest());
		
		Map<String, String> valueMap = new HashMap<String, String>();
		if(userKey != null && userKey.trim().length()>0)
			valueMap.put("/User/@UserKey", userKey);
		Element input = null;
		try {
			input = WCMashupHelper.getMashupInput(
					"getXpedxUserPersonInfo", valueMap, wcContext
							.getSCUIContext());
		} catch (CannotBuildInputException e) {
			log.error("Unable to get User Person Info. "+ userKey +e.getMessage());
		}
		Object obj = WCMashupHelper.invokeMashup(
				"getXpedxUserPersonInfo", input, wcContext
						.getSCUIContext());
		Document outputDocPersonInfo = ((Element) obj).getOwnerDocument();
		Element wElement = outputDocPersonInfo.getDocumentElement();
		Element userEle = SCXmlUtil.getChildElement(wElement, "User");	
		return userEle;
	}
	/**
	 * JIRA 3756 Changes end
	 */
	
	/**
	 * @return Returns the URL domain appropriate for <code>getStorefrontId()</code>. For example: "xpedx.com", "Saalfeldredistribution.com", etc.
	 */
	private String createHostName() {
		if ("xpedx".equals(getStorefrontId())) {
			return "xpedx.com";
		} else if ("Saalfeld".equals(getStorefrontId())) {
			return "Saalfeldredistribution.com";
		} else {
			throw new IllegalArgumentException("Unexpected storefront id: " + getStorefrontId());
		}
	}
	
	/**
	 * @param billToCustId
	 * @return Returns the first two pieces of the Bill-To Customer ID. For example: billToCustId="95-0001900012-000-M-XX-B" returns "95-0001900012"
	 */
	private String truncateBillToCustomerId(String billToCustId) {
		String[] tokens = billToCustId.split("-");
		return tokens[0] + "-" + tokens[1];
	}
	
}

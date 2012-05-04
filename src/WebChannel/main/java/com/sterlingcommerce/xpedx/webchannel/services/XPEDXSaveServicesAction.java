package com.sterlingcommerce.xpedx.webchannel.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
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
	private String billtoFullName = "";
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
		return billtoFullName;
	}

	public void setBilltoFullName(String billtoFullName) {
		this.billtoFullName = billtoFullName;
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
			Document customerDetailsDoc = null;
			String customerId = getWCContext().getCustomerId();
			storefrontId = getWCContext().getStorefrontId();
			StringBuffer sb = new StringBuffer();
			System.out.println("XPEDXSaveServicesAction():execute()");
			
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
			customerDetailsDoc = XPEDXWCUtils.getCustomerDetails(customerId,
					storefrontId, customerShipToInformationMashUp);

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
				String suffix = YFSSystem.getProperty("fromAddress.suffix");
				sb.append(userName).append("@").append(storefrontId).append(suffix);
				customerEmail = sb.toString();
				System.out.println("customerEmail from email"+customerEmail);
				
			}
			 /**
			  JIRA 3160 -Start
			  Changes for imageLogo
			  */
			
			String imageUrl = "";
			if(storefrontId!=null && storefrontId.trim().length() > 0){
			String imageName = XPEDXWCUtils.getLogoName(storefrontId);
			String imagesRootFolder = YFSSystem.getProperty("ImagesRootFolder");
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
			String ccEMail = "";
			
			XPEDXShipToCustomer shipToCustomerObject =(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			String extnECsr1EmailID = shipToCustomerObject.getBillTo().getExtnECsr1EMailID();
			String extnECsr2EmailID = shipToCustomerObject.getBillTo().getExtnECsr2EMailID();
			String sapCustomerKey = shipToCustomerObject.getBillTo().getParentCustomerKey();
			String firstName = shipToCustomerObject.getBillTo().getFirstName();
			String lastName = shipToCustomerObject.getBillTo().getLastName();
			salesProfessional = getSalesRepInfo(sapCustomerKey,wcContext);
			if((firstName!=null && firstName.trim().length() > 0) && (lastName!=null && lastName.trim().length() > 0)){
			billtoFullName = firstName+lastName;
			}else if(firstName!=null && firstName.trim().length() > 0){
				billtoFullName = firstName;
				
			}else if(lastName!=null && lastName.trim().length() > 0){
				billtoFullName = lastName;
				
			}else{
				billtoFullName = "";
			}
			
			billtoCustid = shipToCustomerObject.getBillTo().getCustomerID();
			
			String csrEmailID = XPEDXWCUtils.setCSREmails(extnECsr1EmailID,extnECsr2EmailID);
			
			String saleRepEmail = XPEDXWCUtils.getSalesRepEmail(sapCustomerKey,wcContext);
			
			
			if(csrEmailID != null && csrEmailID.trim().length() > 0 && saleRepEmail!=null && saleRepEmail.trim().length() > 0){
				ccEMail = csrEmailID + XPEDXConstants.EMAILIDSEPARATOR +saleRepEmail;
				System.out.println("ccEMail"+ccEMail);
			}else if(saleRepEmail != null && saleRepEmail.trim().length() > 0){
				System.out.println("ccEMail"+ccEMail);				
				ccEMail = saleRepEmail;				
			}else if(csrEmailID != null && csrEmailID.trim().length() > 0){
				System.out.println("ccEMail"+ccEMail);
				ccEMail = csrEmailID;				
				
			}
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
					Element input = createInputXMLForGeneral(customerEmail, toEmailGen,ccEMail,generalList,sampleServiceRequest);
					String inputXml = SCXmlUtil.getString(input);
					LOG.debug("XPEDXSaveServicesAction() Input XML: " + inputXml);
					Object obj = WCMashupHelper.invokeMashup(
							"SendSampleServiceRequest", input, wcContext
									.getSCUIContext());
					outputDoc = ((Element) obj).getOwnerDocument();
					if (null != outputDoc) {
						System.out.println("bothFlag---gen"+SCXmlUtil.getString((Element) obj));
						LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
					}
				}
				if(	toEmailPaper != null && toEmailPaper.trim().length() > 0){
					
				// Creating input xml to send an email
				Document outputDoc1 = null;
				sampleServiceRequest = "SampleServiceRequestPaper";				
				Element input = createInputXMLForGeneral(customerEmail, toEmailPaper,ccEMail,paperList,sampleServiceRequest);
				String inputXml = SCXmlUtil.getString(input);
				System.out.println("input xml----"+SCXmlUtil.getString(input));
				LOG.debug("XPEDXSaveServicesAction() Input XML : " + inputXml);
				Object obj = WCMashupHelper.invokeMashup(
						"SendSampleServiceRequest", input, wcContext
								.getSCUIContext());
				outputDoc1 = ((Element) obj).getOwnerDocument();
				if (null != outputDoc1) {
					System.out.println("bothFlag---pep"+SCXmlUtil.getString((Element) obj));
					LOG.debug("Output1 XML: " + SCXmlUtil.getString((Element) obj));
				}
			}	
			}else{
		if(	toEmailGen != null && toEmailGen.trim().length() > 0 || toEmailPaper != null && toEmailPaper.trim().length() > 0){
			String toMailaddres = "";
			if(toEmailGen != null && toEmailGen.trim().length() > 0){
				toMailaddres = toEmailGen;
				sampleServiceRequest = "SampleServiceRequest";
				
				
			}else if(toEmailPaper != null && toEmailPaper.trim().length() > 0){
				toMailaddres = toEmailPaper;
				sampleServiceRequest = "SampleServiceRequestPaper";				
				
				
			}
			// Creating input xml to send an email
			Document outputDoc = null;
			Element input = createInputXML(customerEmail, toMailaddres,ccEMail,sampleServiceRequest);
			String inputXml = SCXmlUtil.getString(input);
			System.out.println("input xml----"+SCXmlUtil.getString(input));
			LOG.debug("XPEDXSaveServicesAction() Input XML: " + inputXml);
			Object obj = WCMashupHelper.invokeMashup(
					"SendSampleServiceRequest", input, wcContext
							.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			if (null != outputDoc) {
				System.out.println("else---"+SCXmlUtil.getString((Element) obj));
				LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
			}
		}
		}
		
		} catch (Exception ex) {
			if (ex.getMessage()!=null) {
				setErrorMesage("Your request can not be processed at this time due to technical issues. Please contact administrator");
				LOG.error("XPEDXSaveServicesAction() Input XML: " + ex.getMessage());
				
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
				System.out.println(""+SCXmlUtil.getString(outputDoc));
				
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

	private String getCustomerEmail(Document customerDetailsDoc) {
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
			emailElement.setAttribute("BillToCustid",getBilltoCustid());
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
			subjectEmail = getStorefrontId() + ".com "+ "" + "Paper Sample Request Notification";
			headerEmail = "Paper";
		}
		else{
			subjectEmail = getStorefrontId() + ".com" + "" + "General Sample Request Notification";
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
		
		log.info("*************************EMAIL INFO START**************************************");
		log.info(SCXmlUtil.getString(emailElement));
		log.info("*************************EMAIL INFO END**************************************");

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
			emailElement.setAttribute("BillToCustid",getBilltoCustid());
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
			subjectEmail = getStorefrontId() + ".com " + "" + "Paper Sample Request Notification";
			headerEmail = "Paper";
		}
		else{
			subjectEmail = getStorefrontId() + ".com " + "" + "General Sample Request Notification";
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
			System.out.println(generalPaper);
			
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
					
					//System.out.println("Mfg"+bothList.next());
				}
				if (i == 3) {
					sampleRequestElement.setAttribute("ItemNumber", generalPaper.get(j).toString());
					
					//System.out.println("ItemNumber"+bothList.next());
				}
				if (i == 4) {
					sampleRequestElement.setAttribute("Description", generalPaper.get(j).toString());
					
					//System.out.println("Description"+bothList.next());
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
		
		log.info("*************************EMAIL INFO START**************************************");
		log.info(SCXmlUtil.getString(emailElement));
		log.info("*************************EMAIL INFO END**************************************");

		return templateElement;
	}
/*
 * getSalesRepInfo method which get saler professional first name and last name .
 * */
	public static String getSalesRepInfo(String sapCustomerKey,IWCContext wcContext) {
 		
		 String SalesRepInfo = "";
		 
	 	 if (sapCustomerKey != null && !sapCustomerKey.equalsIgnoreCase("")) {
	 		 
	 		Element xpedxSalesRep = SCXmlUtil.createDocument("XPEDXSalesRep").getDocumentElement();
	 		xpedxSalesRep.setAttribute("SalesCustomerKey", sapCustomerKey);  
	 		
	 		Document outputDoc;
			
	 		Object obj = WCMashupHelper.invokeMashup("getXpedxSalesRepList", xpedxSalesRep, wcContext.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
	        
	 		NodeList nodeList  = outputDoc.getElementsByTagName("XPEDXSalesRep");  
			int salesRepLength = nodeList.getLength();
			List<String> salesRepUserKeysList = new ArrayList<String>();
			
			for (int counter = 0; counter < salesRepLength ; counter++) {
				Element salesRepElem = (Element) nodeList.item(counter);
				
				String salesUserKey = "";
				if (salesRepElem.hasAttribute("SalesUserKey")) {
					salesUserKey = salesRepElem.getAttribute("SalesUserKey");
				}
				if(salesUserKey !=null && salesUserKey.trim().length() > 0)
					salesRepUserKeysList.add(salesUserKey);
			}
			
			if(salesRepUserKeysList.size() > 0){
				try {
				Element inputElem = WCMashupHelper.getMashupInput("getUserListWithContactPersonInfo", wcContext);
				Element complexQueryElem = SCXmlUtil.getChildElement(inputElem, "ComplexQuery");
				Element OrElem = SCXmlUtil.getChildElement(complexQueryElem, "Or");
				Iterator<String> itr = salesRepUserKeysList.iterator();
				while(itr.hasNext()) {
					String userKey = (String)itr.next();
					if(userKey!=null && !userKey.equals("")){
						Element exp = inputElem.getOwnerDocument().createElement("Exp");
						exp.setAttribute("Name", "UserKey");
						exp.setAttribute("Value", userKey);
						SCXmlUtil.importElement(OrElem, exp);
					}
				}
				 
				outputDoc =((Element) WCMashupHelper.invokeMashup("getUserListWithContactPersonInfo", inputElem, wcContext.getSCUIContext())).getOwnerDocument();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				 
				NodeList personInfoList = outputDoc.getElementsByTagName("ContactPersonInfo");
		 		int contactPersonInfoLength = personInfoList.getLength();
		 		System.out.println("contactPersonInfoLength-"+contactPersonInfoLength);
	 			for (int counter = 0; counter < contactPersonInfoLength ; counter++) {
	 				Element personInfoElem = (Element) personInfoList.item(counter);
	 				if (personInfoElem != null && personInfoElem.hasAttribute("FirstName") && personInfoElem.hasAttribute("LastName")) {
	 					SalesRepInfo = personInfoElem.getAttribute("FirstName") + personInfoElem.getAttribute("LastName");
	 					
	 				}else if(personInfoElem != null && personInfoElem.hasAttribute("FirstName")){
	 					SalesRepInfo = personInfoElem.getAttribute("FirstName");
	 				}else if(personInfoElem != null && personInfoElem.hasAttribute("LastName")){
	 					SalesRepInfo = personInfoElem.getAttribute("LastName");
	 				} else {
	 					SalesRepInfo = "";
	 				}
	 				System.out.println("SCXmlUtil.getString(personInfoElem)"+SCXmlUtil.getString(personInfoElem));
	 			}
		 		
		    }	
	 	}
	 	return SalesRepInfo;
	 	}
	

}

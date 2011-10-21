package com.sterlingcommerce.xpedx.webchannel.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.dom.YFCDocument;

@SuppressWarnings("serial")
public class XPEDXSaveServicesAction extends WCMashupAction {

	private static final Logger log = Logger
	.getLogger(XPEDXSaveServicesAction.class);
	
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

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

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

	public String execute() {

		String returnVal = SUCCESS;
		try {
			XPEDXShipToCustomer shipToCustomer = null;
			List<String> customerAddressList = null;
			Document customerDetailsDoc = null;
			String customerId = getWCContext().getCustomerId();
			String storefrontId = getWCContext().getStorefrontId();

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

			// Sending customer details to get the email id of customer
			if (null != customerDetailsDoc) {
				customerEmail = getCustomerEmail(customerDetailsDoc);
			}

			// Getting email id of sample room
			Element rootEle = null;

			rootEle = prepareAndInvokeMashup("xpedxgetCustomerList");

			String sampleRoomEmail = "";
			if (null != rootEle) {
				sampleRoomEmail = getSampleRoomEmail(rootEle);
			}

			// Creating input xml to send an email
			Document outputDoc = null;
			
			Element input = createInputXML(customerEmail, sampleRoomEmail);
			String inputXml = SCXmlUtil.getString(input);
			LOG.debug("Input XML: " + inputXml);
			Object obj = WCMashupHelper.invokeMashup(
					"SendSampleServiceRequest", input, wcContext
							.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			if (null != outputDoc) {
				LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
			}

		} catch (Exception ex) {
			if (ex.getMessage()!=null) {
				setErrorMesage("Your request can not be processed at this time due to technical issues. Please contact administrator");
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

	private Element createInputXML(String customerEmail, String sampleRoomEmail) {

		Document templateEmailDoc = YFCDocument.createDocument("Emails")
				.getDocument();
		Element templateElement = templateEmailDoc.getDocumentElement();
		Element emailElement = templateEmailDoc.createElement("Email");
		emailElement.setAttribute("Contact", getContact());
		emailElement.setAttribute("Phone", getPhone());
		emailElement.setAttribute("Notes", getNotes());
		emailElement.setAttribute("ServiceProvider", getServiceProvider());
		emailElement.setAttribute("ServiceProviderNumber",
				getServiceProviderNumber());
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
		emailElement.setAttribute("FromEmail", customerEmail);
		emailElement.setAttribute("ToEmail", sampleRoomEmail);
		emailElement.setAttribute("RequestType", "SampleServiceRequest");

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
			for (int j=0; j<result.length; j++){
				String token = result[i];//st.nextToken();
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

}

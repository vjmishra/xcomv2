package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.catalog.ItemDetailsAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;

public class XPEDXItemDetailsAction1 extends ItemDetailsAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String execute()
    {
		String returnVal = null;
		try
			{
			Document outputDoc = null;			
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/Emails/Email/@AccountNumber",AccountNumber);
			valueMap.put("/Emails/Email/@CityRequest",CityRequest);
			valueMap.put("/Emails/Email/@ContactPhoneNumber",ContactPhoneNumber);
			valueMap.put("/Emails/Email/@CustomerJobTitle",CustomerJobTitle);
			valueMap.put("/Emails/Email/@CustomerName",CustomerName);
			valueMap.put("/Emails/Email/@DivisionNumber",DivisionNumber);
			valueMap.put("/Emails/Email/@Manufacturer",Manufacturer);
			valueMap.put("/Emails/Email/@ManufacturerPartNumber",ManufacturerPartNumber);
			valueMap.put("/Emails/Email/@Notes",Notes);
			valueMap.put("/Emails/Email/@Quantity",Quantity);
			valueMap.put("/Emails/Email/@RequestedDeliveryDate",RequestedDeliveryDate);
			valueMap.put("/Emails/Email/@SalesRepresentative1",SalesRepresentative1);
			valueMap.put("/Emails/Email/@ShippingAddress2",ShippingAddress2);
			valueMap.put("/Emails/Email/@ShippingAddress3",ShippingAddress3);
			valueMap.put("/Emails/Email/@StateRequest",StateRequest);
			valueMap.put("/Emails/Email/@UnitofMeasure1",UnitofMeasure1);
			valueMap.put("/Emails/Email/@ZipRequest",ZipRequest);
			valueMap.put("/Emails/Email/@ToEmail",Email1);
			valueMap.put("/Emails/Email/@FromEmail",Email2);
			Element input = WCMashupHelper.getMashupInput("xpedxItemDetailsEmail", valueMap, wcContext.getSCUIContext());
			//System.out.println(""+SCXmlUtil.getString(input));
			String inputXml = SCXmlUtil.getString(input);
			LOG.debug("Input XML: " + inputXml);
			Object obj = WCMashupHelper.invokeMashup("xpedxItemDetailsEmail", input, wcContext.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
				if (null != outputDoc) {
					LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
				}
			}
			catch (Exception ex) {
				log.debug("Not able to retrieve Customer Information:->"
						+ ex.getMessage());
				returnVal = "error";
			}
		return returnVal;
    }


	//Added by anil start

	public String getCustomerName() {
		return CustomerName;
	}
	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public String getCustomerContactName() {
		return CustomerContactName;
	}
	public void setCustomerContactName(String CustomerContactName) {
		this.CustomerContactName = CustomerContactName;
	}

	public String getContactPhoneNumber() {
		return ContactPhoneNumber;
	}
	public void setContactPhoneNumber(String ContactPhoneNumber) {
		this.ContactPhoneNumber = ContactPhoneNumber;
	}
	public String getShippingAddress1() {
		return ShippingAddress1;
	}
	public void setShippingAddress1(String ShippingAddress1) {
		this.ShippingAddress1 = ShippingAddress1;
	}

	public String getShippingAddress2() {
		return ShippingAddress2;
	}
	public void setShippingAddress2(String ShippingAddress2) {
		this.ShippingAddress2 = ShippingAddress2;
	}

	public String getShippingAddress3() {
		return ShippingAddress3;
	}
	public void setShippingAddress3(String ShippingAddress3) {
		this.ShippingAddress3 = ShippingAddress3;
	}

	public String getCityRequest() {
		return CityRequest;
	}
	public void setCityRequest(String CityRequest) {
		this.CityRequest = CityRequest;
	}

	public String getStateRequest() {
		return StateRequest;
	}
	public void setStateRequest(String StateRequest) {
		this.StateRequest = StateRequest;
	}

	public String getZipRequest() {
		return ZipRequest;
	}
	public void setZipRequest(String ZipRequest) {
		this.ZipRequest = ZipRequest;
	}
	public String getAccountNumber() {
	    return AccountNumber;
	}
	public void setAccountNumber(String AccountNumber) {
	     this.AccountNumber = AccountNumber;
	}
	public String getDivisionNumber() {
	    return DivisionNumber;
	}
	public void setDivisionNumber(String DivisionNumber) {
	     this.DivisionNumber = DivisionNumber;
	}
	public String getSalesRepresentative1() {
	    return SalesRepresentative1;
	}
	public void setSalesRepresentative1(String SalesRepresentative1) {
	     this.SalesRepresentative1 = SalesRepresentative1;
	}
	public String getRequestedDeliveryDate() {
	    return RequestedDeliveryDate;
	}
	public void setRequestedDeliveryDate(String RequestedDeliveryDate) {
	     this.RequestedDeliveryDate = RequestedDeliveryDate;
	}
	public String getCustomerJobTitle() {
	    return CustomerJobTitle;
	}
	public void setCustomerJobTitle(String CustomerJobTitle) {
	     this.CustomerJobTitle = CustomerJobTitle;
	}
	public String getNotes() {
	    return Notes;
	}
	public void setNotes(String Notes) {
	     this.Notes = Notes;
	}
	public String getUnitofMeasure1() {
	    return UnitofMeasure1;
	}
	public void setUnitofMeasure1(String UnitofMeasure1) {
	     this.UnitofMeasure1 = UnitofMeasure1;
	}
	public String getManufacturer() {
	    return Manufacturer;
	}
	public void setManufacturer(String Manufacturer) {
	     this.Manufacturer = Manufacturer;
	}
	public String getManufacturerPartNumber() {
	    return ManufacturerPartNumber;
	}
	public void setManufacturerPartNumber(String ManufacturerPartNumber) {
	     this.ManufacturerPartNumber = ManufacturerPartNumber;
	}
	public String getItemdescription() {
	    return Itemdescription;
	}
	public void setItemdescription(String Itemdescription) {
	     this.Itemdescription = Itemdescription;
	}
	public String getQuantity() {
	    return Quantity;
	}
	public void setQuantity(String Quantity) {
	     this.Quantity = Quantity;
	}
	public String getRequestSampleFlag() {
	    return RequestSampleFlag;
	}
	public void setRequestSampleFlag(String RequestSampleFlag) {
	     this.RequestSampleFlag = RequestSampleFlag;
	}

	public String getEmail1() {
	    return Email1;
	}
	public void setEmail1(String Email1) {
	     this.Email1 = Email1;
	}
	public String getEmail2() {
	    return Email2;
	}
	public void setEmail2(String Email2) {
	     this.Email2 = Email2;
	}



	//Added by anil end

	String AccountNumber=null ;
	String CustomerName=null ;
	String CustomerContactName=null ;
	String ContactPhoneNumber=null ;
	String ShippingAddress1=null ;
	String ShippingAddress2=null ;
	String ShippingAddress3=null ;
	String CityRequest=null ;
	String StateRequest=null ;
	String ZipRequest=null;
	String DivisionNumber=null;
	String SalesRepresentative1=null;

	String RequestedDeliveryDate=null ;
	String CustomerJobTitle=null ;
	String Notes=null ;
	String UnitofMeasure1=null ;
	String Manufacturer=null ;
	String ManufacturerPartNumber=null ;
	String Itemdescription=null ;
	String Quantity=null ;
	String RequestSampleFlag=null;
	String Email1=null;
	String Email2=null;
	private static final Logger LOG = Logger
	.getLogger(XPEDXItemDetailsAction.class);
}

package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.List;

/**
 * <PersonInfo AddressLine1="LineAssdr1" AddressLine2="LineAssdr12"
				AddressLine3="" AddressLine4="" AddressLine5="" AddressLine6=""
				AlternateEmailID="" Beeper="" City="LineAssdr1City" Company="Toys-R-US-MasterCustomer"
				Country="IN" DayFaxNo="" DayPhone="" Department=""
				EMailID="manohar@stercomm.com" ErrorTxt="" EveningFaxNo=""
				EveningPhone="" FirstName="Manohar" HttpUrl="" IsCommercialAddress="Y"
				JobTitle="" LastName="Vummadi" MiddleName="" MobilePhone=""
				OtherPhone="" PersonID="" PreferredShipAddress="" State="" Suffix=""
				Title="" UseCount="0" VerificationStatus="" ZipCode="560043" />
 * @author mvummadi
 *
 */
public class XPEDXShipToCustomer implements Cloneable{

	public String getShipToName() {
		return ShipToName;
	}
	public void setShipToName(String shipToName) {
		ShipToName = shipToName;
	}

	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getMiddleName() {
		
		return MiddleName;
	}
	public void setMiddleName(String middleName) {
		MiddleName = middleName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getOrganizationName() {
		return OrganizationName;
	}
	public void setOrganizationName(String organizationName) {
		OrganizationName = organizationName;
	}
	public String getEMailID() {
		return EMailID;
	}
	public void setEMailID(String eMailID) {
		EMailID = eMailID;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getCompany() {
		return Company;
	}
	public void setCompany(String company) {
		Company = company;
	}
	public List<String> getAddressList() {
		return addressList;
	}
	public void setAddressList(List<String> addressList) {
		this.addressList = addressList;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getHTMLValue() {
		return hTMLValue;
	}
	public void setHTMLValue(String hTMLValue) {
		this.hTMLValue = hTMLValue;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getZipCode() {
		return ZipCode;
	}
	public void setZipCode(String zipCode) {
		ZipCode = zipCode;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public String getDayPhone() {
		return DayPhone;
	}
	public void setDayPhone(String dayPhone) {
		DayPhone = dayPhone;
	}
	public String getLocationID() {
		return LocationID;
	}
	public void setLocationID(String locationID) {
		LocationID = locationID;
	}
	
	public String getCityCode() {
		if(CityCode!= null && CityCode.length() > 20){
			CityCode = CityCode.substring(0,20);
		}
		return CityCode;
	}
	public void setCityCode(String cityCode) {
		CityCode = cityCode;
	}

	public String toString() {
		String name = this.getFirstName()+","+this.getMiddleName()+","+this.getLastName();
		String Address = null;
		for(int i=0; i<this.addressList.size();i++){
			if(i==0)
				Address = addressList.get(i);
			else
				Address = Address+","+addressList.get(i);
		}
		Address = Address+","+this.getCity()+","+this.getState()+","+this.getCountry()+","+this.getZipCode();
		String toString = this.getCustomerID()+","+this.getCompany()+","+name+","+Address;
		return toString;
	}
	
	

	public String getParentCustomerID() {
		return parentCustomerID;
	}
	public void setParentCustomerID(String parentCustomerID) {
		this.parentCustomerID = parentCustomerID;
	}
	public String getExtnCustomerClass() {
		return extnCustomerClass;
	}
	public void setExtnCustomerClass(String extnCustomerClass) {
		this.extnCustomerClass = extnCustomerClass;
	}
	public String getExtnShipFromBranch() {
		return extnShipFromBranch;
	}
	public void setExtnShipFromBranch(String extnShipFromBranch) {
		this.extnShipFromBranch = extnShipFromBranch;
	}
	public String getExtnIndustry() {
		return extnIndustry;
	}
	public void setExtnIndustry(String extnIndustry) {
		this.extnIndustry = extnIndustry;
	}
	public String getExtnCurrencyCode() {
		return extnCurrencyCode;
	}
	public void setExtnCurrencyCode(String extnCurrencyCode) {
		this.extnCurrencyCode = extnCurrencyCode;
	}
	public String getExtnCustomerDivision() {
		return extnCustomerDivision;
	}
	public void setExtnCustomerDivision(String extnCustomerDivision) {
		this.extnCustomerDivision = extnCustomerDivision;
	}
	public String getExtnCustomerName() {
		return extnCustomerName;
	}
	public void setExtnCustomerName(String extnCustomerName) {
		this.extnCustomerName = extnCustomerName;
	}
	public String getExtnEnvironmentCode() {
		return extnEnvironmentCode;
	}
	public void setExtnEnvironmentCode(String extnEnvironmentCode) {
		this.extnEnvironmentCode = extnEnvironmentCode;
	}
	public String getExtnCompanyCode() {
		return extnCompanyCode;
	}
	public void setExtnCompanyCode(String extnCompanyCode) {
		this.extnCompanyCode = extnCompanyCode;
	}
	public String getExtnLegacyCustNumber() {
		return extnLegacyCustNumber;
	}
	public void setExtnLegacyCustNumber(String extnLegacyCustNumber) {
		this.extnLegacyCustNumber = extnLegacyCustNumber;
	}
	
	public XPEDXShipToCustomer getBillTo() {
		return billTo;
	}
	public void setBillTo(XPEDXShipToCustomer billTo) {
		this.billTo = billTo;
	}

	public String getExtnUseCustSKU() {
		return extnUseCustSKU;
	}
	public void setExtnUseCustSKU(String extnUseCustSKU) {
		this.extnUseCustSKU = extnUseCustSKU;
	}


	public String getExtnSampleRequestFlag() {
		return extnSampleRequestFlag;
	}
	public void setExtnSampleRequestFlag(String extnSampleRequestFlag) {
		this.extnSampleRequestFlag = extnSampleRequestFlag;
	}
	public String getExtnServiceOptCode() {
		return extnServiceOptCode;
	}
	public void setExtnServiceOptCode(String extnServiceOptCode) {
		this.extnServiceOptCode = extnServiceOptCode;
	}

	public String getExtnMinOrderAmount() {
		return extnMinOrderAmount;
	}
	public void setExtnMinOrderAmount(String extnMinOrderAmount) {
		this.extnMinOrderAmount = extnMinOrderAmount;
	}
	public String getExtnMinChargeAmount() {
		return extnMinChargeAmount;
	}
	public void setExtnMinChargeAmount(String extnMinChargeAmount) {
		this.extnMinChargeAmount = extnMinChargeAmount;
	}
	public String getShipToOrgExtnMinOrderAmt() {
		return shipToOrgExtnMinOrderAmt;
	}
	public void setShipToOrgExtnMinOrderAmt(String shipToOrgExtnMinOrderAmt) {
		this.shipToOrgExtnMinOrderAmt = shipToOrgExtnMinOrderAmt;
	}
	public String getShipToOrgExtnSmallOrderFee() {
		return shipToOrgExtnSmallOrderFee;
	}
	public void setShipToOrgExtnSmallOrderFee(String shipToOrgExtnSmallOrderFee) {
		this.shipToOrgExtnSmallOrderFee = shipToOrgExtnSmallOrderFee;
	}
	public String getShipToOrgOrganizationName() {
		return shipToOrgOrganizationName;
	}
	public void setShipToOrgOrganizationName(String shipToOrgOrganizationName) {
		this.shipToOrgOrganizationName = shipToOrgOrganizationName;
	}
	public String getShipToOrgCorporatePersonInfoState() {
		return shipToOrgCorporatePersonInfoState;
	}
	public void setShipToOrgCorporatePersonInfoState(
			String shipToOrgCorporatePersonInfoState) {
		this.shipToOrgCorporatePersonInfoState = shipToOrgCorporatePersonInfoState;
	}

	public XPEDXOrgNodeDetailsBean getOrganization() {
		return organization;
	}
	public void setOrganization(XPEDXOrgNodeDetailsBean organization) {
		this.organization = organization;
	}

	public String getShipToDivDeliveryCutOffTime() {
		return shipToDivDeliveryCutOffTime;
	}
	public void setShipToDivDeliveryCutOffTime(String shipToDivDeliveryCutOffTime) {
		this.shipToDivDeliveryCutOffTime = shipToDivDeliveryCutOffTime;
	}
	
	public String getExtnShipComplete() {
		return extnShipComplete;
	}
	public void setExtnShipComplete(String extnShipComplete) {
		this.extnShipComplete = extnShipComplete;
	}

	public String getExtnCustOrderBranch() {
		return extnCustOrderBranch;
	}
	public void setExtnCustOrderBranch(String extnCustOrderBranch) {
		this.extnCustOrderBranch = extnCustOrderBranch;
	}

	public String getExtnOrigEnvironmentCode() {
		return extnOrigEnvironmentCode;
	}
	public void setExtnOrigEnvironmentCode(String extnOrigEnvironmentCode) {
		this.extnOrigEnvironmentCode = extnOrigEnvironmentCode;
	}
	public String getExtnECsr1EMailID() {
		return extnECsr1EMailID;
	}
	public void setExtnECsr1EMailID(String extnECsr1EMailID) {
		this.extnECsr1EMailID = extnECsr1EMailID;
	}
	public String getExtnECsr2EMailID() {
		return extnECsr2EMailID;
	}
	public void setExtnECsr2EMailID(String extnECsr2EMailID) {
		this.extnECsr2EMailID = extnECsr2EMailID;
	}
	public String getParentCustomerKey() {
		return parentCustomerKey;
	}
	public void setParentCustomerKey(String parentCustomerKey) {
		this.parentCustomerKey = parentCustomerKey;
	}
	
	public String getCustomerStatus() {
		return customerStatus;
	}
	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}

public String getShipToDivdeliveryInfo() {
		return shipToDivdeliveryInfo;
	}
	public void setShipToDivdeliveryInfo(String shipToDivdeliveryInfo) {
		this.shipToDivdeliveryInfo = shipToDivdeliveryInfo;
	}
	
	
	private String FirstName;
	private String MiddleName;
	private String LastName;
	private String OrganizationName;
	private String EMailID;
	private String City;
	private String CityCode;
	private String extnECsr1EMailID; //JIRA 3162
	private String extnECsr2EMailID; //JIRA 3162
    private String parentCustomerKey;//JIRA 3162
	private String Company;
	private List<String> addressList;
	private String customerID;
	private String hTMLValue;
	private String State;
	private String ZipCode;
	private String Country;
	private String DayPhone;
	private String ShipToName;
	private String LocationID;
	private String parentCustomerID;
	private String extnCustomerClass;
	private String extnShipFromBranch;
	private String extnIndustry;
	private String extnCurrencyCode;
	private String extnCustomerDivision;
	private String extnCustomerName;
	private String extnEnvironmentCode;
	private String extnCompanyCode;
	private String extnLegacyCustNumber;
	private String extnUseCustSKU;
	private String extnSampleRequestFlag;
	private String extnServiceOptCode;
	private String extnMinOrderAmount;
	private String extnMinChargeAmount;
	private String extnCustOrderBranch;
	private String extnOrigEnvironmentCode;
	private String extnShipComplete;
	private String shipToOrgExtnMinOrderAmt;
	private String shipToOrgExtnSmallOrderFee;
	private String shipToOrgOrganizationName;
	private String shipToOrgCorporatePersonInfoState;
	private String shipToDivDeliveryCutOffTime;
	private String shipToDivdeliveryInfo;
	private XPEDXShipToCustomer billTo;
	private XPEDXOrgNodeDetailsBean organization;
	private String customerStatus;
//for performance issue for itemDetail.action
	private String ExtnUseOrderMulUOMFlag;
	private String ExtnPrimarySalesRep;
	private String ExtnSampleRoomEmailAddress;
	private String AccountNumber;
	private String ExtnShipToSuffix;
	private String rootCustomerKey;
	private String customerKey;
	private String buyerOrganizationCode;
	private XPEDXShipToCustomer defaultShipToCustomer;
	private String extnAllowDirectOrderFlag;
	private String extnPriceWareHouse;
	
	public String getExtnShipToSuffix() {
		return ExtnShipToSuffix;
	}
	public void setExtnShipToSuffix(String extnShipToSuffix) {
		ExtnShipToSuffix = extnShipToSuffix;
	}
	public String getAccountNumber() {
		return AccountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		AccountNumber = accountNumber;
	}
	public String getExtnSampleRoomEmailAddress() {
		return ExtnSampleRoomEmailAddress;
	}
	public void setExtnSampleRoomEmailAddress(String extnSampleRoomEmailAddress) {
		ExtnSampleRoomEmailAddress = extnSampleRoomEmailAddress;
	}
	public String getExtnUseOrderMulUOMFlag() {
		return ExtnUseOrderMulUOMFlag;
	}
	public void setExtnUseOrderMulUOMFlag(String extnUseOrderMulUOMFlag) {
		ExtnUseOrderMulUOMFlag = extnUseOrderMulUOMFlag;
	}
	public String getExtnPrimarySalesRep() {
		return ExtnPrimarySalesRep;
	}
	public void setExtnPrimarySalesRep(String extnPrimarySalesRep) {
		ExtnPrimarySalesRep = extnPrimarySalesRep;
	}
//end of Performance issue
	//JIRA 3488 start
	private String extnMaxOrderAmount;
	public String getExtnMaxOrderAmount() {
		return extnMaxOrderAmount;
	}
	public void setExtnMaxOrderAmount(String extnMaxOrderAmount) {
		this.extnMaxOrderAmount = extnMaxOrderAmount;
	}
	/**
	 * @return the defaultShipToCustomer
	 */
	public XPEDXShipToCustomer getDefaultShipToCustomer() {
		return defaultShipToCustomer;
	}
	/**
	 * @param defaultShipToCustomer the defaultShipToCustomer to set
	 */
	public void setDefaultShipToCustomer(XPEDXShipToCustomer defaultShipToCustomer) {
		this.defaultShipToCustomer = defaultShipToCustomer;
	}
	public String getRootCustomerKey() {
		return rootCustomerKey;
	}
	public void setRootCustomerKey(String rootCustomerKey) {
		this.rootCustomerKey = rootCustomerKey;
	}
	public String getCustomerKey() {
		return customerKey;
	}
	public void setCustomerKey(String customerKey) {
		this.customerKey = customerKey;
	}
	public String getBuyerOrganizationCode() {
		return buyerOrganizationCode;
	}
	public void setBuyerOrganizationCode(String buyerOrganizationCode) {
		this.buyerOrganizationCode = buyerOrganizationCode;
	}
	public String getExtnAllowDirectOrderFlag() {
		return extnAllowDirectOrderFlag;
	}
	public void setExtnAllowDirectOrderFlag(String extnAllowDirectOrderFlag) {
		this.extnAllowDirectOrderFlag = extnAllowDirectOrderFlag;
	}
	//JIRA 3488 end
	
	public String getExtnPriceWareHouse() {
		return extnPriceWareHouse;
	}
	public void setExtnPriceWareHouse(String extnPricingWareHouse) {
		this.extnPriceWareHouse = extnPricingWareHouse;
	}
	
	
	//added for jira 4306
	public XPEDXShipToCustomer()
	{
		
	}
	public XPEDXShipToCustomer(String firstName, String middleName,
			String lastName, String organizationName, String eMailID,
			String city, String cityCode, String extnECsr1EMailID,
			String extnECsr2EMailID, String parentCustomerKey, String company,
			List<String> addressList, String customerID, String hTMLValue,
			String state, String zipCode, String country, String dayPhone,
			String shipToName, String locationID, String parentCustomerID,
			String extnCustomerClass, String extnShipFromBranch,
			String extnIndustry, String extnCurrencyCode,
			String extnCustomerDivision, String extnCustomerName,
			String extnEnvironmentCode, String extnCompanyCode,
			String extnLegacyCustNumber, String extnUseCustSKU,
			String extnSampleRequestFlag, String extnServiceOptCode,
			String extnMinOrderAmount, String extnMinChargeAmount,
			String extnCustOrderBranch, String extnOrigEnvironmentCode,
			String extnShipComplete, String shipToOrgExtnMinOrderAmt,
			String shipToOrgExtnSmallOrderFee,
			String shipToOrgOrganizationName,
			String shipToOrgCorporatePersonInfoState,
			String shipToDivDeliveryCutOffTime, String shipToDivdeliveryInfo,
			XPEDXShipToCustomer billTo, XPEDXOrgNodeDetailsBean organization,
			String customerStatus, String extnUseOrderMulUOMFlag,
			String extnPrimarySalesRep, String extnSampleRoomEmailAddress,
			String accountNumber, String extnShipToSuffix,
			String rootCustomerKey, String customerKey,
			String buyerOrganizationCode,
			XPEDXShipToCustomer defaultShipToCustomer,
			String extnAllowDirectOrderFlag, String extnMaxOrderAmount) {
		FirstName = firstName;
		MiddleName = middleName;
		LastName = lastName;
		OrganizationName = organizationName;
		EMailID = eMailID;
		City = city;
		CityCode = cityCode;
		this.extnECsr1EMailID = extnECsr1EMailID;
		this.extnECsr2EMailID = extnECsr2EMailID;
		this.parentCustomerKey = parentCustomerKey;
		Company = company;
		this.addressList = addressList;
		this.customerID = customerID;
		this.hTMLValue = hTMLValue;
		State = state;
		ZipCode = zipCode;
		Country = country;
		DayPhone = dayPhone;
		ShipToName = shipToName;
		LocationID = locationID;
		this.parentCustomerID = parentCustomerID;
		this.extnCustomerClass = extnCustomerClass;
		this.extnShipFromBranch = extnShipFromBranch;
		this.extnIndustry = extnIndustry;
		this.extnCurrencyCode = extnCurrencyCode;
		this.extnCustomerDivision = extnCustomerDivision;
		this.extnCustomerName = extnCustomerName;
		this.extnEnvironmentCode = extnEnvironmentCode;
		this.extnCompanyCode = extnCompanyCode;
		this.extnLegacyCustNumber = extnLegacyCustNumber;
		this.extnUseCustSKU = extnUseCustSKU;
		this.extnSampleRequestFlag = extnSampleRequestFlag;
		this.extnServiceOptCode = extnServiceOptCode;
		this.extnMinOrderAmount = extnMinOrderAmount;
		this.extnMinChargeAmount = extnMinChargeAmount;
		this.extnCustOrderBranch = extnCustOrderBranch;
		this.extnOrigEnvironmentCode = extnOrigEnvironmentCode;
		this.extnShipComplete = extnShipComplete;
		this.shipToOrgExtnMinOrderAmt = shipToOrgExtnMinOrderAmt;
		this.shipToOrgExtnSmallOrderFee = shipToOrgExtnSmallOrderFee;
		this.shipToOrgOrganizationName = shipToOrgOrganizationName;
		this.shipToOrgCorporatePersonInfoState = shipToOrgCorporatePersonInfoState;
		this.shipToDivDeliveryCutOffTime = shipToDivDeliveryCutOffTime;
		this.shipToDivdeliveryInfo = shipToDivdeliveryInfo;
		this.billTo = billTo;
		this.organization = organization;
		this.customerStatus = customerStatus;
		ExtnUseOrderMulUOMFlag = extnUseOrderMulUOMFlag;
		ExtnPrimarySalesRep = extnPrimarySalesRep;
		ExtnSampleRoomEmailAddress = extnSampleRoomEmailAddress;
		AccountNumber = accountNumber;
		ExtnShipToSuffix = extnShipToSuffix;
		this.rootCustomerKey = rootCustomerKey;
		this.customerKey = customerKey;
		this.buyerOrganizationCode = buyerOrganizationCode;
		this.defaultShipToCustomer = defaultShipToCustomer;
		this.extnAllowDirectOrderFlag = extnAllowDirectOrderFlag;
		this.extnMaxOrderAmount = extnMaxOrderAmount;
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		XPEDXShipToCustomer shipToCustomer=new XPEDXShipToCustomer(FirstName, MiddleName,
				LastName, OrganizationName,EMailID,
				City, CityCode, extnECsr1EMailID,
				extnECsr2EMailID,  parentCustomerKey,  Company,
				 addressList,  customerID,  hTMLValue,
				 State,  ZipCode,  Country,  DayPhone,
				 ShipToName,  LocationID,  parentCustomerID,
				 extnCustomerClass,  extnShipFromBranch,
				 extnIndustry,  extnCurrencyCode,
				 extnCustomerDivision,  extnCustomerName,
				 extnEnvironmentCode,  extnCompanyCode,
				 extnLegacyCustNumber,  extnUseCustSKU,
				 extnSampleRequestFlag,  extnServiceOptCode,
				 extnMinOrderAmount,  extnMinChargeAmount,
				 extnCustOrderBranch,  extnOrigEnvironmentCode,
				 extnShipComplete,  shipToOrgExtnMinOrderAmt,
				 shipToOrgExtnSmallOrderFee,
				 shipToOrgOrganizationName,
				 shipToOrgCorporatePersonInfoState,
				 shipToDivDeliveryCutOffTime,  shipToDivdeliveryInfo,
				 billTo,  organization,
				 customerStatus,  ExtnUseOrderMulUOMFlag,
				 ExtnPrimarySalesRep,  ExtnSampleRoomEmailAddress,
				 AccountNumber,  ExtnShipToSuffix,
				 rootCustomerKey,  customerKey,
				 buyerOrganizationCode,
				 defaultShipToCustomer,
				extnAllowDirectOrderFlag,  extnMaxOrderAmount);
		
		return shipToCustomer;
	}
	//end for jira 4306
}

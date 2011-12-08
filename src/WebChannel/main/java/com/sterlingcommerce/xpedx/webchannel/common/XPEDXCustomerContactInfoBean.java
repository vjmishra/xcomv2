package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.ArrayList;

/**
 * @author vkaradik
 *This PoJo holds information about CustomerContact information
 */
public class XPEDXCustomerContactInfoBean {
	private String firstName = "";
	private String lastName = "";
	private String customerContactID = "";
	private String localecode = "";
	private String extnViewInvoices = "";
	private Boolean isEstimator = false;
	private String extnViewReportsFlag = "";
	private String extnViewPricesFlag = "";
	private String extnB2BCatalogView = "";
	private ArrayList<String> usergroupKeyList = new ArrayList<String>();
	private String extnDefaultShipTo = "";
	private String isApprover = "";
	private String msapEmailID;
	private Boolean usergroupKeyListActive = false;
	private String extnPrefCatalog = "";
	private String extnMyItemsLink = "";
	private Integer numberOfAssignedShioTos = 0;
    private String extnOrderConfEmailFlag;
    private String emailID;
    private String personInfoEmailID;
   //Start- Code added to fix XNGTP 2964	
    private String msapExtnUseOrderMulUOMFlag;    
		public XPEDXCustomerContactInfoBean() {
		super();
	}
   public String getMsapExtnUseOrderMulUOMFlag() {
			return msapExtnUseOrderMulUOMFlag;
		}

		public void setMsapExtnUseOrderMulUOMFlag(String msapExtnUseOrderMulUOMFlag) {
			this.msapExtnUseOrderMulUOMFlag = msapExtnUseOrderMulUOMFlag;
		}
		//End- Code added to fix XNGTP 2964	
      public XPEDXCustomerContactInfoBean(String firstName, String lastName, String customerContactID, String msapEmailID,
			String localecode, String extnViewInvoices, Boolean isEstimator,
			String extnViewReportsFlag, String extnViewPricesFlag,
			ArrayList<String> usergroupKeyList, String extnDefaultShipTo,
			String extnPrefCatalog, String isApprover, Boolean usergroupKeyListActive, String extnMyItemsLink, Integer numberOfAssignedShioTos, String extnB2BCatalogView
			,String extnOrderConfEmailFlag,String emailID,String extnUseOrderMulUOMFlag,String personInfoEmailID) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.customerContactID = customerContactID;
		this.msapEmailID = msapEmailID;
		this.localecode = localecode;
		this.extnViewInvoices = extnViewInvoices;
		this.isEstimator = isEstimator;
		this.extnViewReportsFlag = extnViewReportsFlag;
		this.extnViewPricesFlag = extnViewPricesFlag;
		this.usergroupKeyList = usergroupKeyList;
		this.extnDefaultShipTo = extnDefaultShipTo;
		this.extnPrefCatalog = extnPrefCatalog;
		this.isApprover = isApprover;
		this.usergroupKeyListActive = usergroupKeyListActive;
		this.extnMyItemsLink = extnMyItemsLink;
		this.numberOfAssignedShioTos = numberOfAssignedShioTos;
		this.extnB2BCatalogView = extnB2BCatalogView;
		this.extnOrderConfEmailFlag=extnOrderConfEmailFlag;
		this.emailID=emailID;
		this.personInfoEmailID=personInfoEmailID;
		this.msapExtnUseOrderMulUOMFlag=extnUseOrderMulUOMFlag;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLocalecode() {
		return localecode;
	}

	public void setLocalecode(String localecode) {
		this.localecode = localecode;
	}

	public String getExtnViewInvoices() {
		return extnViewInvoices;
	}

	public void setExtnViewInvoices(String extnViewInvoices) {
		this.extnViewInvoices = extnViewInvoices;
	}

	public Boolean isEstimator() {
		return isEstimator;
	}

	public void setIsEstimator(Boolean isEstimator) {
		this.isEstimator = isEstimator;
	}
	
	public String getExtnViewReportsFlag() {
		return extnViewReportsFlag;
	}

	public void setExtnViewReportsFlag(String extnViewReportsFlag) {
		this.extnViewReportsFlag = extnViewReportsFlag;
	}

	public String getExtnViewPricesFlag() {
		return extnViewPricesFlag;
	}

	public void setExtnViewPricesFlag(String extnViewPricesFlag) {
		this.extnViewPricesFlag = extnViewPricesFlag;
	}

	public ArrayList<String> getUsergroupKeyList() {
		return usergroupKeyList;
	}

	public void setUsergroupKeyList(ArrayList<String> usergroupKeyList) {
		this.usergroupKeyList = usergroupKeyList;
	}

	public String getExtnDefaultShipTo() {
		return extnDefaultShipTo;
	}

	public void setExtnDefaultShipTo(String extnDefaultShipTo) {
		this.extnDefaultShipTo = extnDefaultShipTo;
	}

	public String getExtnPrefCatalog() {
		return extnPrefCatalog;
	}

	public void setExtnPrefCatalog(String extnPrefCatalog) {
		this.extnPrefCatalog = extnPrefCatalog;
	}
	
	public String getIsApprover() {
		return isApprover;
	}

	public void setIsApprover(String isApprover) {
		this.isApprover = isApprover;
	}

	public Integer getNumberOfAssignedShioTos() {
		return numberOfAssignedShioTos;
	}

	public void setNumberOfAssignedShioTos(Integer numberOfAssignedShioTos) {
		this.numberOfAssignedShioTos = numberOfAssignedShioTos;
	}

	public Boolean getUsergroupKeyListActive() {
		return usergroupKeyListActive;
	}

	public void setUsergroupKeyListActive(Boolean usergroupKeyListActive) {
		this.usergroupKeyListActive = usergroupKeyListActive;
	}

	public String getExtnMyItemsLink() {
		return extnMyItemsLink;
	}

	public void setExtnMyItemsLink(String extnMyItemsLink) {
		this.extnMyItemsLink = extnMyItemsLink;
	}

	public String getExtnB2BCatalogView() {
		return extnB2BCatalogView;
	}

	public void setExtnB2BCatalogView(String extnB2BCatalogView) {
		this.extnB2BCatalogView = extnB2BCatalogView;
	}

	public Boolean getIsEstimator() {
		return isEstimator;
	}

	public String getCustomerContactID() {
		return customerContactID;
	}

	public void setCustomerContactID(String customerContactID) {
		this.customerContactID = customerContactID;
	}

	public String getMsapEmailID() {
		return msapEmailID;
	}

	public void setMsapEmailID(String msapEmailID) {
		this.msapEmailID = msapEmailID;
	}

	public String getExtnOrderConfEmailFlag() {
		return extnOrderConfEmailFlag;
	}

	public void setExtnOrderConfEmailFlag(String extnOrderConfEmailFlag) {
		this.extnOrderConfEmailFlag = extnOrderConfEmailFlag;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getPersonInfoEmailID() {
		return personInfoEmailID;
	}

	public void setPersonInfoEmailID(String personInfoEmailID) {
		this.personInfoEmailID = personInfoEmailID;
	}


}

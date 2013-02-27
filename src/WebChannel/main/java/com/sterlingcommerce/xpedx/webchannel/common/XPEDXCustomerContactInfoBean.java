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
    private String addEmailID;  //JIRA 3382
    private String spendingLimit;
    private String orderApproveFlag ; //added for XB 226
  //JIRA 3488 Start
	 private String extnmaxOrderAmount;
	 public void setExtnmaxOrderAmount(String extnmaxOrderAmount) {
		this.extnmaxOrderAmount = extnmaxOrderAmount;
	}
	public String getExtnmaxOrderAmount() {
		return extnmaxOrderAmount;
	}
 //JIRA 3488 End
  
  // Added for JIRA 3589 - Performance : XPEDXMyItemsGetShareListAction
    private String extnSuffixType = "";
    public String getExtnSuffixType() {
		return extnSuffixType;
	}
	public void setExtnSuffixType(String extnSuffixType) {
		this.extnSuffixType = extnSuffixType;
	}
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
			,String extnOrderConfEmailFlag,String emailID,String extnUseOrderMulUOMFlag,String personInfoEmailID,String maxOrderAmt,String spendingLimit,String orderApproveFlag) {//added maxOrderAmt for JIRA 3488 orderApproveFalg for Jira226
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
		this.extnmaxOrderAmount=maxOrderAmt;//JIRA 3488 
		this.spendingLimit=spendingLimit;
		this.orderApproveFlag = orderApproveFlag;//XB 226
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

	public String getAddEmailID() {
		return addEmailID;
	}

	public void setAddEmailID(String addEmailID) {
		this.addEmailID = addEmailID;
	}
	
	public String getSpendingLimit() {
		return spendingLimit;
	}
	
	public void setSpendingLimit(String spendingLimit) {
		this.spendingLimit = spendingLimit;
	}
	
	public String getOrderApproveFlag() {
		return orderApproveFlag;
	}
	
	public void setOrderApproveFlag(String orderApproveFlag) {
		this.orderApproveFlag = orderApproveFlag;
	}


}

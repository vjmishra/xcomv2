	
package com.xpedx.sterling.rcp.pca.customercontact.extn;

/**
 * Created on May 13,2010
 *
 */
 
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEvent;
import com.yantra.yfc.rcp.YRCExtendedTableBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCValidationResponse;
import com.yantra.yfc.rcp.YRCWizard;
import com.yantra.yfc.rcp.YRCWizardExtensionBehavior;
import com.yantra.yfc.rcp.YRCXmlUtils;
/**
 * @author vchandra-tw
 * Copyright © 2005-2010 Sterling Commerce, Inc. All Rights Reserved.
 */
 public class CustomerContactWizardExtnBehavior extends YRCWizardExtensionBehavior {

	 Element eleInput =null;
	 String extnViewReportsFlag=null,extnEstimator=null,extnViewPriceFlag=null,extnViewInvoiceFlag=null,extnStockCheckFlag=null;
	/**
	 * This method initializes the behavior class.
	 */
	public void init() {
		
	}
	@Override
	public void handleApiCompletion(YRCApiContext ctx) {
		if (ctx.getInvokeAPIStatus() > 0){
			//Bug fix:878
			/*if (ctx.getApiName().equals("XPXGetChildCustomerListService")) {
					Element outXml = ctx.getOutputXml().getDocumentElement();
					setExtentionModel("extn_XPXCustomerChildList", outXml);					
				}*/
			}
		
		//In case of Invoke API failure
		else if(ctx.getInvokeAPIStatus()==-1 ){
				Element outXml = ctx.getOutputXml().getDocumentElement();
				if("Errors".equals(outXml.getNodeName())){
					YRCPlatformUI.trace("Failed while getting Customer Child List: ", outXml);
					YRCPlatformUI.showError("Failed!", "Unable to get Customer Child List.");
				}
			
		}
		
		super.handleApiCompletion(ctx);
	}
 	
    public String getExtnNextPage(String currentPageId) {
		//TODO
		return null;
    }
    
    public IYRCComposite createPage(String pageIdToBeShown) {
		//TODO
		return null;
	}
    
    public void pageBeingDisposed(String pageToBeDisposed) {
		//TODO
    }

    /**
     * Called when a wizard page is about to be shown for the first time.
     *
     */
    public void initPage(String pageBeingShown) {
		Element inpXml = ((YRCEditorInput) ((YRCWizard) this.getOwnerForm()).wizardInput)
		.getXml();
		
		addEventHandler("extn_btnUserRoleHelp", SWT.FOCUSED);// XNGTP-1185 -Fixed

    	String CustomerContactID = inpXml.getAttribute("CustomerContactID");
    	if( pageBeingShown
				.equals("com.yantra.pca.ycd.rcp.tasks.customerContactEntry.wizardpages.YCDCustomerContactEntryWizardPage")) {
    		if(YRCPlatformUI.isVoid(CustomerContactID) ){
			setControlVisible("extn_manageUserProfile", false);
			//BugFix:878
			/*String apiname = "XPXGetChildCustomerListService";
			Document docInput = YRCXmlUtils
					.createFromString("<Customer  CustomerKey='"
							+ inpXml.getAttribute("CustomerKey") + "'/>");

			YRCApiContext ctx = new YRCApiContext();
			ctx
					.setFormId("com.yantra.pca.ycd.rcp.tasks.customerContactEntry.wizards.YCDCustomerContactEntryWizard");
			ctx.setApiName(apiname);
			ctx.setInputXml(docInput);
			callApi(ctx);*/			
    		}else {
    			//BugFix:878
    			/*setControlVisible("extn_lblDefaultShipTo",false);
    			setControlVisible("extn_comboDefaultShipTo",false);*/
    			//setControlVisible("extn_lblUserType", false);
    			//setControlVisible("extn_userTypeComposite", false);
    			//setControlVisible("extn_radInternalUserType", false);
    			//setControlVisible("extn_radExternalUserType", false);
    		}
		}
	}
    /**
	 * Method for Providing Help icon for User Roles while creating customer contact. XNGTP-1185 -Fixed. 
	 */
	protected void handleEvent(String fieldName, YRCEvent event) {

		String usrRoleHelpInfo=null;
		if("extn_btnUserRoleHelp".equalsIgnoreCase(fieldName)
				&& YRCPlatformUI.isVoid(getFieldValue("extn_btnUserRoleHelp"))){
			usrRoleHelpInfo=
				 "Buyer:  Permission to use the site. Required for all users." + "\n"+
				 "Approver:  Authorizes submission of orders."+"\n"+
				 "Estimator:  Can view pricing and inventory availability. Cannot submit an order." + "\n"+
				 "Stock Check Web Service User:  Stock Check Web Service User for system integrations. (Does not" + 
				 "  control inventory display for regular site user)." + "\n" +
				 "Admin:  Permission to create user profiles, assign roles and ship to locations within" + "\n"+ 
				 "               the account." +"\n"+
				 "View Invoices:  Can view invoices online." + "\n"	+
				 "View Reports:  Can view reports. (Note: User should not view reports if cannot view" + "\n"+ "\t"+ 
				 "        pricing)." + "\n"	+
				 "View Prices:  Can view pricing." + "\n"+
				 "Punch Out User:  Punchout User (punchout integration customers only).";
			
			YRCPlatformUI.showInformation("User Roles Description", usrRoleHelpInfo);
			
			return;
		}
		super.handleEvent(fieldName, event);
	}
 	
 	
	/**
	 * Method for validating the text box.
     */
    public YRCValidationResponse validateTextField(String fieldName, String fieldValue) {
    	// TODO Validation required for the following controls.
		
		// TODO Create and return a response.
		return super.validateTextField(fieldName, fieldValue);
	}
    
    /**
     * Method for validating the combo box entry.
     */
    public void validateComboField(String fieldName, String fieldValue) {
    	// TODO Validation required for the following controls.
		
		// TODO Create and return a response.
		super.validateComboField(fieldName, fieldValue);
    }
    
    /**
     * Method called when a button is clicked.
     */
    public YRCValidationResponse validateButtonClick(String fieldName) {
    	// TODO Validation required for the following controls.
		if("btnConfirm".equals(fieldName))
		{
			//Added for JIRA 4160
			if(!YRCPlatformUI.isVoid(getFieldValue("txtLoginid")))
			{
				String logInID = getFieldValue("txtLoginid");
				boolean upperFound = false;
				for (char c : logInID.toCharArray()) {
				    if (Character.isUpperCase(c)) {
				        upperFound = true;
				        YRCPlatformUI.showInformation("Error!", "Please enter login ID in lower case");
				        return null;
				        }

				    }
				 
			}
			Element eleUserGroupLists = getModel("getUserGroupList_output");
			List userGroupLst = YRCXmlUtils.getChildren(eleUserGroupLists,
					"UserGroup");
			String userGroupId = null, checked = null;
			String adminFlag=null,buyerFlag=null,approverFlag=null,procurementUserFlag=null;
			for (Object object : userGroupLst) {
				Element eleUserGroup = (Element) object;
				userGroupId = eleUserGroup.getAttribute("UsergroupId");
				checked = eleUserGroup.getAttribute("Checked");

				if (YRCPlatformUI.isVoid(checked)) {
					checked = "N";
				}
				if (userGroupId.equalsIgnoreCase("BUYER-ADMIN")) {
					adminFlag=checked;
				}
				if (userGroupId.equalsIgnoreCase("BUYER-USER")) {
					buyerFlag=checked;
				}
				if (userGroupId.equalsIgnoreCase("BUYER-APPROVER")) {
					approverFlag=checked;
				}
				if (userGroupId.equalsIgnoreCase("PROCUREMENT-USER")) {
					procurementUserFlag=checked;
				}
				if(userGroupId.equalsIgnoreCase("ESTIMATOR")){
					extnEstimator=checked;
				}
				if(userGroupId.equalsIgnoreCase("VIEW-PRICES")){
					extnViewPriceFlag=checked;
				}
				if(userGroupId.equalsIgnoreCase("VIEW-INVOICES")){
					extnViewInvoiceFlag=checked;
				}
				if(userGroupId.equalsIgnoreCase("STOCK-CHECK")){
					extnStockCheckFlag=checked;
				}
				if(userGroupId.equalsIgnoreCase("VIEW-REPORTS")){
					extnViewReportsFlag=checked;
				}
			}
			
			if(YRCPlatformUI.isVoid(getFieldValue("txtLoginid")))
			{
				YRCPlatformUI.showInformation("Error!", "Please enter login ID.");
				return null; 
			}
			else if(buyerFlag=="N"){
                YRCPlatformUI.showInformation("Error!", "User cannot be created without the Buyer role. Please select the Buyer role.");
                buyerFlag = "Y";
                return null; 
          }else if (!(adminFlag=="Y" || buyerFlag=="Y" ||approverFlag=="Y" ||procurementUserFlag=="Y" ||
					//extnEstimator=="T" || extnStockCheckFlag=="T" || extnViewInvoiceFlag =="T" ||
						extnEstimator=="Y" || extnStockCheckFlag=="Y" || extnViewInvoiceFlag =="Y" || extnViewPriceFlag=="Y" || extnViewReportsFlag=="Y")
					){
					YRCPlatformUI.showInformation("Error!", "No User Role Selected.");
					return null;
			}	
		}
		if("btnClose".equals(fieldName))
		{
			setDirty(false);
		}
		// TODO Create and return a response.
		return super.validateButtonClick(fieldName);
    }
    
    /**
     * Method called when a link is clicked.
     */
	public YRCValidationResponse validateLinkClick(String fieldName) {
		if ("extn_manageUserProfile".equals(fieldName)) {
			YRCPlatformUI.fireAction("com.xpedx.sterling.rcp.pca.userprofile.action.XPXShowUserProfileAction");
		}		
		return super.validateLinkClick(fieldName);
	}
	
	/**
	 * Create and return the binding data for advanced table columns added to the tables.
	 */
	 public YRCExtendedTableBindingData getExtendedTableBindingData(String tableName, ArrayList tableColumnNames) {
	 	// Create and return the binding data definition for the table.
		
	 	// The defualt super implementation does nothing.
	 	return super.getExtendedTableBindingData(tableName, tableColumnNames);
	 }
	 public Element getLocalModel(String str){
		 return getModel(str);
		 
	 }
	 String[] apinames=null;
	 @Override
	public boolean preCommand(YRCApiContext apiContext) {
		//Only for Customer Contact Creation.
		apinames = apiContext.getApiNames();
		for (int i=0; i<apinames.length ; i++) {
			String userStatus="";
			String apiname = apinames[i];
			if ("manageCustomer".equals(apiname)
					&& "com.yantra.pca.ycd.rcp.tasks.customerContactEntry.wizards.YCDCustomerContactEntryWizard"
							.equals(apiContext.getFormId())) {
				Document docInput = apiContext.getInputXmls()[i];
				eleInput = docInput.getDocumentElement();
				
				Element eleUserGroupLists = getModel("getUserGroupList_output");
				List userGroupLst = YRCXmlUtils.getChildren(eleUserGroupLists,
						"UserGroup");
				String userGroupId = null, checked = null;
				String adminFlag=null,buyerFlag=null,approverFlag=null,procurementUserFlag=null;
				for (Object object : userGroupLst) {
					Element eleUserGroup = (Element) object;
					userGroupId = eleUserGroup.getAttribute("UsergroupId");
					checked = eleUserGroup.getAttribute("Checked");
					if (YRCPlatformUI.isVoid(checked)) {
						checked = "N";
					}
					if (userGroupId.equalsIgnoreCase("STOCK-CHECK")) {
						YRCXmlUtils
								.setAttributeValue(
										eleInput,
										"/Customer/CustomerContactList/CustomerContact/Extn/@ExtnStockCheckWS",
										("Y".equals(checked))?"Y":"N" );
					}
					if (userGroupId.equalsIgnoreCase("VIEW-INVOICES")) {
						YRCXmlUtils
								.setAttributeValue(
										eleInput,
										"/Customer/CustomerContactList/CustomerContact/Extn/@ExtnViewInvoices",
										("Y".equals(checked))?"Y":"N" );
					}
					if (userGroupId.equalsIgnoreCase("VIEW-PRICES")) {
						YRCXmlUtils
								.setAttributeValue(
										eleInput,
										"/Customer/CustomerContactList/CustomerContact/Extn/@ExtnViewPricesFlag",
										checked);
					}
					if (userGroupId.equalsIgnoreCase("VIEW-REPORTS")) {
						YRCXmlUtils
								.setAttributeValue(
										eleInput,
										"/Customer/CustomerContactList/CustomerContact/Extn/@ExtnViewReportsFlag",
										checked);
					}
					// Value to Y/N instead of T/F for JIRA XBT-211
					if (userGroupId.equalsIgnoreCase("ESTIMATOR")) {
						YRCXmlUtils
								.setAttributeValue(
										eleInput,
										"/Customer/CustomerContactList/CustomerContact/Extn/@ExtnEstimator",
										("Y".equals(checked))?"Y":"N" );
					}
					
				}
				 extnViewReportsFlag=YRCXmlUtils
				.getAttributeValue(eleInput,
						"/Customer/CustomerContactList/CustomerContact/Extn/@ExtnViewReportsFlag");
				
				 extnEstimator=YRCXmlUtils
				.getAttributeValue(eleInput,
						"/Customer/CustomerContactList/CustomerContact/Extn/@ExtnEstimator");
				
				 extnViewPriceFlag=YRCXmlUtils
				.getAttributeValue(eleInput,
						"/Customer/CustomerContactList/CustomerContact/Extn/@ExtnViewPricesFlag");
				
				 extnViewInvoiceFlag=YRCXmlUtils
				.getAttributeValue(eleInput,
						"/Customer/CustomerContactList/CustomerContact/Extn/@ExtnViewInvoices");
				
				 extnStockCheckFlag=YRCXmlUtils
				.getAttributeValue(eleInput,
						"/Customer/CustomerContactList/CustomerContact/Extn/@ExtnStockCheckWS");
				
				//In case Of Manager Customer Contact Dont Update ExtnUserType
				if (YRCPlatformUI
						.isVoid(YRCXmlUtils
								.getAttributeValue(getModel("getCustomerContactDetails_output"),
										"/CustomerContact/@UserID"))) {
					YRCXmlUtils
							.setAttributeValue(
									eleInput,
									"/Customer/CustomerContactList/CustomerContact/Extn/@ExtnUserType",
									"EXTERNAL");
				}
				// According to the JIRA 1016
				YRCXmlUtils.setAttributeValue(eleInput, "/Customer/CustomerContactList/CustomerContact/Extn/@ExtnOrderConfEmailFlag", "Y");
				// System.out.println("12");
			} else if( ("createUserHierarchy".equals(apiname) || "modifyUserHierarchy".equals(apiname))
					&& "com.yantra.pca.ycd.rcp.tasks.customerContactEntry.wizards.YCDCustomerContactEntryWizard"
					.equals(apiContext.getFormId())){
				
				// According to the JIRA 1220 end
				Document docUserInputModifyHierarchy = apiContext.getInputXmls()[i];
				//Input to modifyUserHierarchy
				Element eleUserInputModifyHierarchy = docUserInputModifyHierarchy
						.getDocumentElement();
				Element customerStatusElement = YRCXmlUtils.getXPathElement(eleInput, "/Customer/CustomerContactList/CustomerContact");
				userStatus=customerStatusElement.getAttribute("Status");
				if("10".equals(userStatus)){
					YRCXmlUtils.setAttributeValue(eleUserInputModifyHierarchy, "/User/@Activateflag","Y");
				}
				else{
					YRCXmlUtils.setAttributeValue(eleUserInputModifyHierarchy, "/User/@Activateflag","N");
				}
							//Fix for 1650: Set SessionTimeout = 0, only for Customer Contacts.
				if("createUserHierarchy".equals(apiname))
					eleUserInputModifyHierarchy.setAttribute("SessionTimeout", XPXConstants.DEFAULT_SESSION_TIMEOUT);
				
				// This is for removing the following user roles
				// View Invoices ,View Prices,Stock Check,View Reports
				Element eleUserGroupListsModifyHierarchy = YRCXmlUtils
						.getXPathElement(eleUserInputModifyHierarchy,
								"/User/UserGroupLists");
				List userGroupList = YRCXmlUtils.getChildren(
						eleUserGroupListsModifyHierarchy, "UserGroupList");
				String usrGroupID = null;

				for (Object object : userGroupList) {
					Element eleUserGroupList = (Element) object;
					usrGroupID = eleUserGroupList.getAttribute("UsergroupId");

					if ("VIEW-REPORTS".equalsIgnoreCase(usrGroupID)) {
						eleUserGroupList.getParentNode().removeChild(
								eleUserGroupList);
					}
					if ("VIEW-PRICES".equalsIgnoreCase(usrGroupID)) {
						eleUserGroupList.getParentNode().removeChild(
								eleUserGroupList);
					}
					if ("VIEW-INVOICES".equalsIgnoreCase(usrGroupID)) {
						eleUserGroupList.getParentNode().removeChild(
								eleUserGroupList);
					}
					if ("STOCK-CHECK".equalsIgnoreCase(usrGroupID)) {
						eleUserGroupList.getParentNode().removeChild(
								eleUserGroupList);
					}
					if ("ESTIMATOR".equalsIgnoreCase(usrGroupID)) {
						eleUserGroupList.getParentNode().removeChild(
								eleUserGroupList);
					}
				}
				// According to the JIRA 1220 end
			}

		}
		
		return super.preCommand(apiContext);
	}
	 
	 @Override
	public void postCommand(YRCApiContext apiContext) {
				if (apiContext.getApiName().equals("manageCustomer")
					&& "com.yantra.pca.ycd.rcp.tasks.customerContactEntry.wizards.YCDCustomerContactEntryWizard"
							.equals(apiContext.getFormId())) {
					
					if (apiContext.getInvokeAPIStatus() > 0){
						addCustomerAPICall(apiContext);
			
		}}
		 
		super.postCommand(apiContext);
	}
	 /** function used to call API on Buyer Added**/
	 private void addCustomerAPICall(YRCApiContext apiContext){
		 String[] apinames = null;
			Document docInput = apiContext.getOutputXml();
			Element eleInput = docInput.getDocumentElement();
			String CustomerId=getFieldValue("txtLoginid");
			String mailId=getFieldValue("txtEmailID");
			String firstName=getFieldValue("txtFirstName");
			String lastName=getFieldValue("txtLastName");
			String orgCode=eleInput.getAttribute("OrganizationCode");
			String entryType=XPXConstants.ENTRY_TYPE_BUYER_ADDED;
			
			Document[] docInputcustomerAdded = null;
			
			Document docInputQry=YRCXmlUtils.createFromString("<UserUpdateEmail />");

			docInputQry.getDocumentElement().setAttribute("BrandName", orgCode);
			docInputQry.getDocumentElement().setAttribute("EntryType", entryType);
			docInputQry.getDocumentElement().setAttribute("AccountNumber", CustomerId);
			//CustomerContactID added for JIRA 3256
			docInputQry.getDocumentElement().setAttribute("CustomerContactID", CustomerId);
			docInputQry.getDocumentElement().setAttribute("FirstName", firstName);
			docInputQry.getDocumentElement().setAttribute("LastName", lastName);
			docInputQry.getDocumentElement().setAttribute("cEmailAddress", mailId);
			
	    	/*apinames = new String[]{"XPXSendUserUpdateEmailService"};
	    	docInputcustomerAdded = new Document[]{docInputQry};
	    	callApis(apinames, docInputcustomerAdded);*/
	 }
	 
	 private void callApis(String apinames[], Document inputXmls[]) {
			YRCApiContext ctx = new YRCApiContext();
			ctx.setFormId(this.getFormId());
			ctx.setApiNames(apinames);
			ctx.setInputXmls(inputXmls);
			if (!this.getOwnerForm().isDisposed())
				callApi(ctx, this.getOwnerForm());
		}
	// According to the JIRA 1220 START
	 public void postSetModel(String model) {
		 if("getUserGroupList_output".equalsIgnoreCase(model)){
			 Element eleUserGroupLists = getModel("getUserGroupList_output");
			 Element eleCustDetailEle=getModel("getCustomerDetails_output");
			 String activeFlag="";
			 Element eleCustomerContactDetails_output=getModel("getCustomerContactDetails_output");
			 String custId= "";
			 if(!YRCPlatformUI.isVoid(eleCustomerContactDetails_output)){
				 custId = eleCustomerContactDetails_output.getAttribute("CustomerContactID");
			 }
			 if(!YRCPlatformUI.isVoid(eleCustDetailEle)){
				 List nodesList=YRCXmlUtils.getChildren(eleCustDetailEle, "CustomerContact");
				 NodeList nodList=eleCustDetailEle.getElementsByTagName("CustomerContact");
				 for(int i=0;i<nodList.getLength();i++){
					 Element  eleCust=(Element) nodList.item(i);
					 String customerContactId=eleCust.getAttribute("CustomerContactID");
						if(customerContactId.equals(custId)){
							Element statusElement = YRCXmlUtils.getXPathElement(eleCust, "/CustomerContact/User");
							if(!YRCPlatformUI.isVoid(statusElement))
							activeFlag=statusElement.getAttribute("Activateflag");
						}
				 }
			 }
			 Element eleExtnFlags=null;

			 Element stockChkElement = YRCXmlUtils.createFromString("<UserGroup UsergroupId='STOCK-CHECK'  UsergroupKey='STOCK-CHECK' UsergroupName='Stock Check Web Service User'/>").getDocumentElement();
			 Element viewInvoiceElement = YRCXmlUtils.createFromString("<UserGroup UsergroupId='VIEW-INVOICES'  UsergroupKey='VIEW-INVOICES' UsergroupName='View Invoices'/>").getDocumentElement();
			 Element viewReportElement = YRCXmlUtils.createFromString("<UserGroup UsergroupId='VIEW-REPORTS'  UsergroupKey='VIEW-REPORTS' UsergroupName='View Reports'/>").getDocumentElement();
			 Element viewPricesElement = YRCXmlUtils.createFromString("<UserGroup UsergroupId='VIEW-PRICES'  UsergroupKey='VIEW-PRICES' UsergroupName='View Prices'/>").getDocumentElement();
			 Element estimatorElement = YRCXmlUtils.createFromString("<UserGroup UsergroupId='ESTIMATOR'  UsergroupKey='ESTIMATOR' UsergroupName='Estimator'/>").getDocumentElement();

			 if(!YRCPlatformUI.isVoid(eleCustomerContactDetails_output)){
				 eleExtnFlags=YRCXmlUtils.getXPathElement(eleCustomerContactDetails_output, "/CustomerContact/Extn");

				 if("Y".equals(eleExtnFlags.getAttribute("ExtnViewReportsFlag"))){
					 viewReportElement.setAttribute("Checked", eleExtnFlags.getAttribute("ExtnViewReportsFlag"));
				 }
				 if("T".equals(eleExtnFlags.getAttribute("ExtnViewInvoices"))){
					 viewInvoiceElement.setAttribute("Checked", "Y");
				 }
				 if("Y".equals(eleExtnFlags.getAttribute("ExtnViewPricesFlag"))){
					 viewPricesElement.setAttribute("Checked", eleExtnFlags.getAttribute("ExtnViewPricesFlag"));
				 }
				 if("T".equals(eleExtnFlags.getAttribute("ExtnStockCheckWS"))){
					 stockChkElement.setAttribute("Checked", "Y");
				 }
				 if("T".equals(eleExtnFlags.getAttribute("ExtnEstimator"))){
					 estimatorElement.setAttribute("Checked", "Y");
				 }

				 if("Y".equals(activeFlag))
					 YRCXmlUtils.setAttributeValue(eleCustomerContactDetails_output,"/CustomerContact/@Status", "10");
				 else
					 YRCXmlUtils.setAttributeValue(eleCustomerContactDetails_output,"/CustomerContact/@Status", "30");
					 
			 }
			 YRCXmlUtils.importElement(eleUserGroupLists, viewReportElement);
			 YRCXmlUtils.importElement(eleUserGroupLists, stockChkElement);
			 YRCXmlUtils.importElement(eleUserGroupLists, viewInvoiceElement);
			 YRCXmlUtils.importElement(eleUserGroupLists, viewPricesElement);
			 YRCXmlUtils.importElement(eleUserGroupLists, estimatorElement);

			 NodeList userGroupNodeList = eleUserGroupLists.getElementsByTagName("UserGroup");
			 for (int i = 0; i < userGroupNodeList.getLength(); i++) {
				 Element userGroupElement = (Element) userGroupNodeList.item(i);
				 if(userGroupElement.getAttribute("UsergroupName").equalsIgnoreCase("Buyer Admin")){
					 userGroupElement.setAttribute("UsergroupName", "Admin");
				 }
				 if(userGroupElement.getAttribute("UsergroupName").equalsIgnoreCase("Buyer Approver")){
					 userGroupElement.setAttribute("UsergroupName", "Approver");
				 }
				 if(userGroupElement.getAttribute("UsergroupName").equalsIgnoreCase("Buyer User")){
					 userGroupElement.setAttribute("UsergroupName", "Buyer");
					 userGroupElement.setAttribute("Checked", "Y");
				 }
			 }
			 repopulateModel("getUserGroupList_output");
			 repopulateModel("getCustomerContactDetails_output");
			 // According to the JIRA 1220 END
		 }
		 if("getCustomerDetails_output".equalsIgnoreCase(model)){
			 Element eleCustomerContactDetails_output=getModel("getCustomerDetails_output");
			 YRCXmlUtils.getString(eleCustomerContactDetails_output);
			 
			 NodeList nodList=eleCustomerContactDetails_output.getElementsByTagName("CustomerContact");
			 String activeFlag="";
			 for(int i=0;i<nodList.getLength();i++){
				
				 Element  eleCust=(Element) nodList.item(i);
			
					YRCXmlUtils.getString(eleCust);
						Element statusElement = YRCXmlUtils.getXPathElement(eleCust, "/CustomerContact/User");
						if(!YRCPlatformUI.isVoid(statusElement)){
						activeFlag=statusElement.getAttribute("Activateflag");
						

						 if("Y".equals(activeFlag))
							 YRCXmlUtils.setAttributeValue(eleCust,"/CustomerContact/@AggregateStatus", "10");
					//	 getCustomerContactDetails_output:/CustomerContact/@AggregateStatus
						 else
							 YRCXmlUtils.setAttributeValue(eleCust,"/CustomerContact/@AggregateStatus", "30");
						}
					
			 }
	
		 }
		 super.postSetModel(model);
	 }
	 
}
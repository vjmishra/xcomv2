	
package com.xpedx.sterling.rcp.pca.customerDetails.extn;

/**
 * Created on May 13,2010
 *
 */
 
import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCTableColumnTextProvider;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCExtendedTableBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCValidationResponse;
import com.yantra.yfc.rcp.YRCWizardExtensionBehavior;
import com.yantra.yfc.rcp.YRCXmlUtils;
/**
 * @author vchandra-tw
 * Copyright © 2005-2010 Sterling Commerce, Inc. All Rights Reserved.
 */
 public class CustomerDetailsWizardExtnBehavior extends YRCWizardExtensionBehavior {

	 public static  String masterCustomerID;
	 public static  String CustomerName;
	 public static  String customerkey;
	 	 
	/**
	 * This method initializes the behavior class.
	 */
	public void init() {
		//TODO: Write behavior init here.
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
		//TODO
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
		
		// TODO Create and return a response.
		return super.validateButtonClick(fieldName);
    }
    
    /**
     * Method called when a link is clicked.
     */
	public YRCValidationResponse validateLinkClick(String fieldName) {

		if ("extn_lnkManageCustomerProfileRule".equals(fieldName)) {
			YRCPlatformUI.fireAction("com.xpedx.sterling.rcp.pca.customerprofilerule.action.XPXShowCustomerProfileRuleAction");
		}		
		//return new YRCValidationResponse(YRCValidationResponse.YRC_VALIDATION_OK, "Invalid date provided.");
		return super.validateLinkClick(fieldName);
	}
	
	 public void postSetModel(String model) {
		
		 if("getCustomerDetails_output".equalsIgnoreCase(model)){
			 System.out.println("here is ***************************");
			
			 Document docOutput = getModel("getCustomerDetails_output").getOwnerDocument();
			 Element eleCustomerContactDetails_output = docOutput.getDocumentElement();
			 masterCustomerID = eleCustomerContactDetails_output.getAttribute("CustomerID");
			 String OrgName= YRCXmlUtils.getAttributeValue(eleCustomerContactDetails_output, "/Customer/BuyerOrganization/@OrganizationName");
			 String[] trimCustIDArray = masterCustomerID.split("-");
			 String trimCustID = trimCustIDArray[1];
			 CustomerName = OrgName.concat("(").concat(trimCustID).concat(")");
			 
			 customerkey = eleCustomerContactDetails_output.getAttribute("CustomerKey");
			 XPXUtils.setCustomerKey(customerkey);
			 XPXUtils.setMasterCustomerID(masterCustomerID);
			 YRCApiContext customerCTX = new YRCApiContext();
			 XPXUtils.setCustomerName(CustomerName);
			 NodeList nodList=eleCustomerContactDetails_output.getElementsByTagName("CustomerContact");
			 ArrayList eSalesRepList = new ArrayList();
			 for(int i=0;i<nodList.getLength();i++){
				
				 Element  eleCust=(Element) nodList.item(i);
				 String customerID = eleCust.getAttribute("CustomerContactID");			
					Element statusElement = YRCXmlUtils.getXPathElement(eleCust, "/CustomerContact/Extn");
					String eSalesRep = statusElement.getAttribute("ExtnIsSalesRep");
					if("Y".equalsIgnoreCase(eSalesRep)){
						eSalesRepList.add(customerID);
					}
				 }
			 
			 // Added to remove the elements
			 NodeList nodeCustContact=eleCustomerContactDetails_output.getElementsByTagName("CustomerContact");
				for(int i=0;i<nodeCustContact.getLength();i++){
					Element elementCust=(Element) nodeCustContact.item(i);
					String customerID = elementCust.getAttribute("CustomerContactID");
					if (eSalesRepList.contains(customerID)){
						elementCust.getParentNode().removeChild(elementCust);
						i--;
					}
					
				}
			 
			 repopulateModel("getCustomerDetails_output");
			 repopulateModel("getCustomerContactDetails_output");
	
		 }
		 super.postSetModel(model);
	 }
	

	/**
	 * Create and return the binding data for advanced table columns added to the tables.
	 */
	 public YRCExtendedTableBindingData getExtendedTableBindingData(String tableName, ArrayList tableColumnNames) {
	 	// Create and return the binding data definition for the table.
		 final YRCExtendedTableBindingData tblBindingData = new YRCExtendedTableBindingData(tableName);
		 if(YRCPlatformUI.equals(tableName,"tblCustomerContact"))
		 {
		 HashMap<String, YRCTblClmBindingData> bindingDataMap = new HashMap<String, YRCTblClmBindingData>();
		 final YRCTblClmBindingData permissionBindingData = new YRCTblClmBindingData();
		 permissionBindingData.setName("extn_permissionsTblClmn");
		 permissionBindingData.setAttributeBinding("Permissions");
		 permissionBindingData.setTooltipBinding("Permissions");
		 bindingDataMap.put("extn_permissionsTblClmn", permissionBindingData);
		 tblBindingData.setTableColumnBindingsMap(bindingDataMap);

		 permissionBindingData.setLabelProvider(new IYRCTableColumnTextProvider(){

			public String getColumnText(Element arg0) {
				// TODO Auto-generated method stub
				ArrayList<String> roleList = new ArrayList<String>();
				
				String permissions = "";
				Element eleTableItem = (Element)arg0;
				Element extnElement = YRCXmlUtils.getXPathElement(eleTableItem, "/CustomerContact/Extn");
				/*  Start of Modified code for JIra XB-198 User Profile - Permissions 
				 * Modified code by replacing the Conditions equals("F") with ("Y")
				 *  
				 */
				if(YRCPlatformUI.equals("Y", extnElement.getAttribute("ExtnStockCheckWS"))){
					roleList.add("Stock Check Web Service User");
				}	
				if(YRCPlatformUI.equals("Y", extnElement.getAttribute("ExtnViewInvoices"))){
					roleList.add("ViewInvoices");
				}	
				if(YRCPlatformUI.equals("Y", extnElement.getAttribute("ExtnEstimator"))){
					roleList.add("Estimator");
				}
				if(YRCPlatformUI.equals("Y", extnElement.getAttribute("ExtnViewPricesFlag"))){
					roleList.add("ViewPrices");
				}
				if(YRCPlatformUI.equals("Y", extnElement.getAttribute("ExtnViewReportsFlag"))){
					roleList.add("ViewReports");
				}
				
				
				/****End of Modified code for Jira XB-198 User Profile - Permissions ***/
				NodeList userList = eleTableItem.getElementsByTagName("UserGroupList");
				int userLength = userList.getLength();
				for(int userCount=0;userCount<userLength;userCount++)
				{
					Element userElement = (Element)userList.item(userCount);
					String role = userElement.getAttribute("UsergroupKey");
					if(role.contains("BUYER-ADMIN"))
						role = "Admin";
					if(role.contains("BUYER-APPROVER"))
						role = "Approver";
					if(role.contains("BUYER-USER"))
						role = "Buyer";
					if(role.contains("PROCUREMENT-USER"))
						role = "Punch Out User";
					
					roleList.add(role);
				}
				for(int counter=0;counter<roleList.size();counter++)
				{
					if(YRCPlatformUI.equals("", permissions))
					{
						permissions = permissions+roleList.get(counter);
					}
					else
					{
						permissions = permissions+","+roleList.get(counter);
					}
				}
				return permissions;
			}
			 
		 });
		 
		 final YRCTblClmBindingData userNameBindingData = new YRCTblClmBindingData();
		 userNameBindingData.setName("extn_userNameTblClmn");
		 userNameBindingData.setAttributeBinding("UserName");
		 userNameBindingData.setTooltipBinding("UserName");
		 bindingDataMap.put("extn_userNameTblClmn", userNameBindingData);
		 tblBindingData.setTableColumnBindingsMap(bindingDataMap);
		 userNameBindingData.setAttributeBinding("@FirstName;@LastName");
		 userNameBindingData.setKey("customer_contact_key");
	 
		 }	
			//}
	 	// The defualt super implementation does nothing.
	 	//return super.getExtendedTableBindingData(tableName, tableColumnNames);
			return tblBindingData;
	 }
			public String setCustomerID(String masterCustomerID)
		{		
			return masterCustomerID = masterCustomerID; 
		}
			

}

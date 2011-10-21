	
package com.xpedx.sterling.rcp.pca.orderEntry.extn;

/**
 * Created on Jul 27,2010
 *
 */
 
import java.util.ArrayList;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCExtendedTableBindingData;
import com.yantra.yfc.rcp.YRCExtentionBehavior;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;
import com.yantra.yfc.rcp.YRCValidationResponse;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;
/**
 * @author sdodda
 * 
 * Extended Order Entry Customer Search Page Behavior
 */
 public class XPXCustomerSearchPageExtnBehavior extends YRCExtentionBehavior{

	/**
	 * This method initializes the behavior class.
	 */
	public void init() {
		//TODO: Write behavior init here.
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

    	
    	// Get Assigned Customer List to select a Ship To.
    	if("extn_btnGetAssignedCustomers".equals(fieldName)){
    		
    		if(YRCPlatformUI.isTraceEnabled()){
    			YRCPlatformUI.trace("Shared Task: Launch com.xpedx.sterling.rcp.pca.sharedTasks.XPXShipToLookupSharedTask");
    		}
    		
    		// Create Input XML
    		Document docInput = YRCXmlUtils.createDocument("User");
    		Element eleInput = docInput.getDocumentElement();
    		eleInput.setAttribute("UserID", getFieldValue("cmbContactList"));
    		if(YRCPlatformUI.isTraceEnabled()){
    			YRCPlatformUI.trace("Shared Task: Launching with Input XML - "+YRCXmlUtils.getString(docInput));
    		}
    		
    		//Launch Shared Task
    		YRCSharedTaskOutput output = YRCPlatformUI.launchSharedTask( "com.xpedx.sterling.rcp.pca.sharedTasks.XPXShipToLookupSharedTask", eleInput);
    		
    		// Get the response 
    		Element eleContactPersonInfo = output.getOutput();
    		if(!YRCPlatformUI.isVoid(eleContactPersonInfo)){
				String[] displayAddress = new String[7];
				displayAddress[0] = eleContactPersonInfo.getAttribute("AddressLine1");
				displayAddress[1] = eleContactPersonInfo.getAttribute("AddressLine2");
				displayAddress[2] = eleContactPersonInfo.getAttribute("AddressLine3");
				displayAddress[3] = eleContactPersonInfo.getAttribute("City");
				displayAddress[4] = eleContactPersonInfo.getAttribute("State");
				displayAddress[5] = eleContactPersonInfo.getAttribute("Country");
				displayAddress[6] = eleContactPersonInfo.getAttribute("ZipCode");
				eleContactPersonInfo.setAttribute("AddressLine6", YRCPlatformUI.getFormattedString("xpedx_address_key", displayAddress));

				setExtentionModel("Extn_ShipToInfo",eleContactPersonInfo);
				
				//---Get Ship To Customer Details by using getCustomerList API-----------------
				
				//Prepared input 
				Document docGetCustListInput = YRCXmlUtils.createDocument("Customer");
				Element eleGetCustListInput = docGetCustListInput.getDocumentElement();
				eleGetCustListInput.setAttribute("CustomerID", eleContactPersonInfo.getAttribute("CustomerID"));
				eleGetCustListInput.setAttribute("OrganizationCode", eleContactPersonInfo.getAttribute("OrganizationCode"));
				
				//invoking the getCustomerList API to get the ShipTo Customer Details
				YRCApiContext context = new YRCApiContext();
				context.setApiName("XPXGetShipToCustomerList");
				context.setFormId(getFormId());
				context.setInputXml(docGetCustListInput);
				context.setUserData("CustomerType", "ShipTo");
				callApi(context);
				if(YRCPlatformUI.isTraceEnabled()){
					YRCPlatformUI.trace("Shared Task: Output from XPXShipToLookupSharedTask - "+YRCXmlUtils.getString(eleContactPersonInfo));
				}
    		}
    	}
		return super.validateButtonClick(fieldName);
    }
    
    /**
     * Method called when a link is clicked.
     */
	public YRCValidationResponse validateLinkClick(String fieldName) {
    	// TODO Not used as of now because OOTB YCDAddressSharedTask is not working
    	if("extn_ManageShipToAddress".equals(fieldName)){
    		Element eleShipToAddress = getExtentionModel("Extn_ShipToInfo");
    		Element input = YRCXmlUtils.getCopy(eleShipToAddress);
    		input.getOwnerDocument().renameNode(input, input.getNamespaceURI(), "PersonInfo");
    		YRCSharedTaskOutput output = YRCPlatformUI.launchSharedTask(YRCPlatformUI.getShell(), "YCDAddressSharedTask", input);
//    		YRCSharedTaskOutput output = YRCPlatformUI.launchSharedTask(parent, "YCDAddressSharedTask", input);
    		if(null!=output){
    			Element ele = output.getOutput();
//    		System.out.println("output:"+YRCXmlUtils.getString(ele));
    		}
    	}
		return super.validateLinkClick(fieldName);
	}
	
	/**
	 * Create and return the binding data for advanced table columns added to the tables.
	 */
	 public YRCExtendedTableBindingData getExtendedTableBindingData(String tableName, ArrayList tableColumnNames) {
	 	// Create and return the binding data definition for the table.
		
	 	// The default super implementation does nothing.
	 	return super.getExtendedTableBindingData(tableName, tableColumnNames);
	 }
	 
	 @Override
	public boolean preCommand(YRCApiContext apiContext) {
		 
//		Update Order Create Input XML with the Selected Customers "BillTo Extended Attributes and Addresses" 
//		 and "ShipTo Extended Attributes and Addresses" on to the Order.
		String[] apinames = apiContext.getApiNames();
		for (int i = 0; i < apinames.length; i++) {
			String string = apinames[i];
			if ("createOrder".equals(string)) {
				Element eleInput = ((Document) apiContext.getInputXmls()[i]).getDocumentElement();
				Element eleSelectedCustomer = getModel("selectedCustomer");
				Element eleShipToAddress = getExtentionModel("Extn_ShipToInfo");//Selected ShipToCustomer from Popup
				Element eleShipToCustomer = getExtentionModel("Extn_ShipToCustomer");
				
				// Copy ShipToID and BuyerOrganizationCode from the selected ship to customer from popup.
				eleInput.setAttribute("ShipToID", eleShipToAddress.getAttribute("CustomerID"));
				eleInput.setAttribute("BuyerOrganizationCode",
								eleShipToAddress.getAttribute("BuyerOrganizationCode"));
				
				this.updateCustomerAttributes(eleInput, eleSelectedCustomer, eleShipToCustomer);
				if (YRCPlatformUI.isTraceEnabled()) {
					YRCPlatformUI
							.trace("Appended ShipToID, BuyerOrganizationCode and Customer level extended attributes, and invoke createOrder - "+ YRCXmlUtils.getString(eleInput));
				}
				
				// Updating the ShipTo and BillTo addresses on to the CreateOrder XML input
				Element elePersonInfoShipTo = YRCXmlUtils.createChild(eleInput, "PersonInfoShipTo");
//				commented because we are not editing this address as of now.
//				elePersonInfoShipTo.removeAttribute("PersonInfoKey");
				YRCXmlUtils.mergeElement(eleShipToAddress, elePersonInfoShipTo, false);
				//Clearing AddressLine6 attribute because this is being used for displaying Address in the Entry Page, Not required to be saved in DB. 
				elePersonInfoShipTo.setAttribute("AddressLine6", "");

				Element eleBillToAddress = getExtentionModel("Extn_BillToInfo");
				eleInput.setAttribute("BillToKey", eleBillToAddress.getAttribute("PersonInfoKey"));
				eleInput.setAttribute("OrderType", XPXConstants.ORD_TYPE_CUSTOMER);
				//Commented to to merge PersonIndo to Order/PersonInfoBillTo element
//				Element elePersonInfoBillTo = YRCXmlUtils.createChild(eleInput, "PersonInfoBillTo");
//				YRCXmlUtils.mergeElement(eleBillToAddress, elePersonInfoBillTo, false);
//				//Clearing AddressLine6 attribute because this is being used for displaying Address in the Entry Page, Not required to be saved in DB. 
//				elePersonInfoBillTo.setAttribute("AddressLine6", "");
			}
			if("getCustomerList".equals(string)){
				Element eleInput = ((Document) apiContext.getInputXmls()[i]).getDocumentElement();
				//add search condition Suffix type as MC
				if(null != eleInput){
					Element extnElement = YRCXmlUtils.createChild(eleInput, "Extn");
					extnElement.setAttribute("ExtnSuffixType", "MC");
				}
			}
		}
		return super.preCommand(apiContext);
	}
	 
	 @Override
	public void postCommand(YRCApiContext apiContext) {
		 // When we get the Customer Details/List of the selected Customer 
		 // - then stamp Additional Address from the Details to a new Model "Extn_BillToInfo"
		 String[] apinames = apiContext.getApiNames();
			for (int i = 0; i < apinames.length; i++) {
				String string = apinames[i];
				if("getCustomerDetails".equals(string)){
					Element eleOutputCustomer = ((Document) apiContext.getOutputXmls()[i]).getDocumentElement();
					//populateBillToAddress(eleOutputCustomer);
				}
				if("getCustomerList".equals(string)){
					Element eleOutput = ((Document) apiContext.getOutputXmls()[i]).getDocumentElement();
					Element eleOutputCustomer = YRCXmlUtils.getChildElement(eleOutput, "Customer");
					//if(null != eleOutputCustomer)
					//populateBillToAddress(eleOutputCustomer);
				}
			}
			setDirty(false);
		super.postCommand(apiContext);
	}


	/**
	 * @param eleCustomer
	 */
	private void populateBillToAddress(Element eleCustomer) {
		Element eleContactPersonInfo = (Element) YRCXPathUtils.evaluate(eleCustomer, "./CustomerAdditionalAddressList/CustomerAdditionalAddress[@IsDefaultBillTo='Y']/PersonInfo", XPathConstants.NODE);
		if(!YRCPlatformUI.isVoid(eleContactPersonInfo)){
			String[] displayAddress = new String[7];
			displayAddress[0] = eleContactPersonInfo.getAttribute("AddressLine1");
			displayAddress[1] = eleContactPersonInfo.getAttribute("AddressLine2");
			displayAddress[2] = eleContactPersonInfo.getAttribute("AddressLine3");
			displayAddress[3] = eleContactPersonInfo.getAttribute("City");
			displayAddress[4] = eleContactPersonInfo.getAttribute("State");
			displayAddress[5] = eleContactPersonInfo.getAttribute("Country");
			displayAddress[6] = eleContactPersonInfo.getAttribute("ZipCode");
			eleContactPersonInfo.setAttribute("AddressLine6", YRCPlatformUI.getFormattedString("xpedx_address_key", displayAddress));
			setExtentionModel("Extn_BillToInfo", eleContactPersonInfo);
			
		} else {
			Element eleShipToAddress = getExtentionModel("Extn_ShipToInfo");
    		Element input = YRCXmlUtils.getCopy(eleShipToAddress);
    		input.getOwnerDocument().renameNode(input, input.getNamespaceURI(), "PersonInfo");
    		setExtentionModel("Extn_BillToInfo", input);
			//eleContactPersonInfo = YRCXmlUtils.createDocument("PersonInfo").getDocumentElement();
			//setExtentionModel("Extn_BillToInfo", eleContactPersonInfo);
    		
		}
	}
	 
	 private void updateCustomerAttributes(Element eleOrder, Element eleCustomerInfo, Element eleShipToCustomer) {

		Element eleBillToCustomer = getModel("Extn_BillToCustomer");
		
		Element eleOrderExtn = YRCXmlUtils.getChildElement(eleOrder, "Extn", true);
		Element eleBillToCustExtn = YRCXmlUtils.getChildElement(eleBillToCustomer, "Extn");
		Element custShipToCustomerExtn = YRCXmlUtils.getChildElement(eleShipToCustomer, "Extn");
		
		//Stamp Bill To & Ship To Attributes
		eleOrderExtn.setAttribute("ExtnBillToName",
				YRCXmlUtils.getAttributeValue(eleBillToCustomer, "/Customer/BuyerOrganization/@OrganizationName"));
		eleOrderExtn.setAttribute("ExtnShipToName",
				YRCXmlUtils.getAttributeValue(eleShipToCustomer, "/Customer/BuyerOrganization/@OrganizationName"));
		
		//Stamp Bill To Extended Attributes
		// Order/Extn/@ExtnBillToSuffix
		if(!YRCPlatformUI.isVoid(eleBillToCustExtn)){
			eleOrderExtn.setAttribute("ExtnBillToSuffix",eleBillToCustExtn.getAttribute("ExtnBillToSuffix"));
		}
		
		//Stamp Ship To Extended Attributes 
		// Order/@ShipNode, 
		// Order/Extn/@ExtnCompanyId,ExtnCustomerDivision,ExtnCustomerNo,ExtnEnvtId,ExtnETradingID,
		// ExtnShipToSuffix, ExtnShipComplete, ExtnOrderDivision
		if(!YRCPlatformUI.isVoid(custShipToCustomerExtn)){
			String strEnvtId = custShipToCustomerExtn.getAttribute("ExtnEnvironmentCode");
			String strNode = !YRCPlatformUI.isVoid(custShipToCustomerExtn.getAttribute("ExtnShipFromBranch"))?
							custShipToCustomerExtn.getAttribute("ExtnShipFromBranch"):
								custShipToCustomerExtn.getAttribute("ExtnCustOrderBranch");
			String strCustomerDivision = custShipToCustomerExtn.getAttribute("ExtnCustomerDivision");
			
			// Node: will be stamped only when both strNode & strEnvtId are not null
			if(YRCPlatformUI.isVoid(strEnvtId))
				strEnvtId="";
			strNode = XPXUtils.updateNodeSyntax(strEnvtId, strNode);
			strCustomerDivision = XPXUtils.updateNodeSyntax(strEnvtId, strCustomerDivision);
			
			eleOrder.setAttribute("ShipNode",strNode);
			eleOrderExtn.setAttribute("ExtnCompanyId", custShipToCustomerExtn.getAttribute("ExtnCompanyCode"));
			eleOrderExtn.setAttribute("ExtnCustomerDivision", strCustomerDivision);
			eleOrderExtn.setAttribute("ExtnCustomerNo",custShipToCustomerExtn.getAttribute("ExtnLegacyCustNumber"));
			eleOrderExtn.setAttribute("ExtnEnvtId",strEnvtId);
			eleOrderExtn.setAttribute("ExtnETradingID",custShipToCustomerExtn.getAttribute("ExtnETradingID"));
			eleOrderExtn.setAttribute("ExtnShipToSuffix",custShipToCustomerExtn.getAttribute("ExtnShipToSuffix"));
			eleOrderExtn.setAttribute("ExtnShipComplete",eleBillToCustExtn.getAttribute("ExtnShipComplete"));
			eleOrderExtn.setAttribute("ExtnOrderDivision", strNode); 
		}
		
		//eleOrderExtn.setAttribute("ExtnSourceType","1"); // Internal according to Prasanth Nair
		eleOrderExtn.setAttribute("ExtnSourceType","4");// For call center orders it should be 4 - JIRA - 1426

	}

	 @Override
	public void handleApiCompletion(YRCApiContext apiContext) {
		 String[] apinames = apiContext.getApiNames();
			for (int i = 0; i < apinames.length; i++) {
				String string = apinames[i];
				// Getting the ShipToCustomer Details to stamp the order extended attributes.
				if("XPXGetShipToCustomerList".equals(string)){
					if(YRCPlatformUI.equals("ShipTo", apiContext.getUserData("CustomerType"))){
						Element eleOutput = ((Document) apiContext.getOutputXmls()[i]).getDocumentElement();
						Element eleShipToCustomer = YRCXmlUtils.getChildElement(eleOutput, "Customer");
						setExtentionModel("Extn_ShipToCustomer", eleShipToCustomer);

						// Query for Parent Customer Details
						Element eleParentCustomer = YRCXmlUtils.getChildElement(eleShipToCustomer, "ParentCustomer");
						Element eleGetCustListInput = YRCXmlUtils.createDocument("Customer").getDocumentElement();
			    		eleGetCustListInput.setAttribute("CustomerID", eleParentCustomer.getAttribute("CustomerID"));
			    		eleGetCustListInput.setAttribute("OrganizationCode", eleParentCustomer.getAttribute("OrganizationCode"));
			    		eleGetCustListInput.setAttribute("CustomerKey", eleParentCustomer.getAttribute("CustomerKey"));
			    		
			    		//invoking the getCustomerList API
			    		YRCApiContext context = new YRCApiContext();
			    		context.setApiName("XPXGetShipToCustomerList");
			    		context.setFormId(getFormId());
			    		context.setInputXml(eleGetCustListInput.getOwnerDocument());
			    		context.setUserData("CustomerType", "BillTo");
			    		callApi(context);
					} else if(YRCPlatformUI.equals("BillTo", apiContext.getUserData("CustomerType"))){
						Element eleOutput = ((Document) apiContext.getOutputXmls()[i]).getDocumentElement();
						Element eleBillToCustomer = YRCXmlUtils.getChildElement(eleOutput, "Customer");
						setExtentionModel("Extn_BillToCustomer", eleBillToCustomer);
						populateBillToAddress(eleBillToCustomer);
					}
				}
			}
		 
		super.handleApiCompletion(apiContext);
	}
}
//TODO Validation required for a Button control: extn_btnGetAssignedCustomers
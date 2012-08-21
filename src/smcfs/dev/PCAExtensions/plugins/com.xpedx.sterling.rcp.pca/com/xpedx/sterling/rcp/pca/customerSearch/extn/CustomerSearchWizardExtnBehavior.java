package com.xpedx.sterling.rcp.pca.customerSearch.extn;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCTableColumnTextProvider;
import com.yantra.yfc.rcp.YRCApiContext;

import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCEvent;
import com.yantra.yfc.rcp.YRCExtendedTableBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;
import com.yantra.yfc.rcp.YRCValidationResponse;
import com.yantra.yfc.rcp.YRCWizardExtensionBehavior;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class CustomerSearchWizardExtnBehavior extends YRCWizardExtensionBehavior {

	/**
	 * This method initializes the behavior class.
	 */
	private String enterpriseCode=null;
	private String selectedBrandCode=null;
	public void init() {

	}
	private void invokeBrandCodeListAPI() {
		YRCApiContext apiConterxt = new YRCApiContext();
		apiConterxt.setApiName("XPXGetBrandCodeList");
		apiConterxt.setInputXml(YRCXmlUtils.createFromString("<CommonCode CodeType='XPXBrandSF'  OrganizationCode='DEFAULT'/>"));
		apiConterxt.setFormId("com.yantra.pca.ycd.rcp.tasks.customerSearch.wizards.YCDCustomerSearchWizard");
		callApi(apiConterxt);
	}

	/**
	 * Method for Handling the combo box selection event. #Fixed XNGTP-1021
	 */
	protected void handleEvent(String fieldName, YRCEvent event) {

		// If User selected a Blank Organization Code.
		if("cmbOrganizationCode".equalsIgnoreCase(fieldName)
				&& YRCPlatformUI.isVoid(getFieldValue("cmbOrganizationCode"))){
			setExtentionModel("Extn_DivisionList", YRCXmlUtils.createDocument("OrganizationList").getDocumentElement());
			return;
		}

		// If User selected a Valid non-empty Organization Code.
		if("cmbOrganizationCode".equalsIgnoreCase(fieldName)){
			if(!YRCPlatformUI.isVoid(getFieldValue("cmbOrganizationCode"))){

				enterpriseCode=getFieldValue("cmbOrganizationCode");
				//JIRA 3582 - Condition added for reading data from Application Init.
				if (enterpriseCode.startsWith("xped")) {
					prepareDivisionTypeDropdownModel();
				} else {
					this.invokeGetOrgListAPI(this.getBrandCode());
				}
			} else {
				selectedBrandCode = "";
			}
		}

		super.handleEvent(fieldName, event);
	}

	private void invokeGetOrgListAPI(String brandcode) {
		//JIRA 3582 - Condition added for reading data from Application Init. Method added to create XML binding for  xpedx
		if(!YRCPlatformUI.isVoid(brandcode) && !"XPED".equalsIgnoreCase(brandcode)){
			YRCApiContext apiConterxt = new YRCApiContext();
			apiConterxt.setApiName("XPXGetDivisionList");
			apiConterxt.setInputXml(YRCXmlUtils.createFromString("<Organization IsNode='Y'><Extn ExtnBrandCode='"+brandcode+"' ExtnBrandCodeQryType='LIKE'/></Organization>"));
			apiConterxt.setFormId("com.yantra.pca.ycd.rcp.tasks.customerSearch.wizards.YCDCustomerSearchWizard");
			callApi(apiConterxt);
		}
	}

	private void prepareCustomerTypeDropdownModel() {
		Element customerTypes = YRCXmlUtils.createFromString("<CustomerTypes><CustomerType CustomerTypeCode='' CustomerTypeDesc='All'/><CustomerType CustomerTypeCode='MC' CustomerTypeDesc='Master Customer'/><CustomerType CustomerTypeCode='C' CustomerTypeDesc='Customer'/><CustomerType CustomerTypeCode='B' CustomerTypeDesc='Bill-To'/><CustomerType CustomerTypeCode='S' CustomerTypeDesc='Ship-To'/></CustomerTypes>").getDocumentElement();
		setExtentionModel("Extn_CustomerTypes", customerTypes);
	}

	public void handleApiCompletion(YRCApiContext apiContext) {
		if(apiContext.getApiName().equals("XPXGetDivisionList")){
			Document docOutput = apiContext.getOutputXml();
			setExtentionModel("Extn_DivisionList", docOutput.getDocumentElement());
		}

		if(apiContext.getApiName().equals("XPXGetBrandCodeList")){
			Document docOutput = apiContext.getOutputXml();
			setExtentionModel("Extn_XPXBrandCodeList", docOutput.getDocumentElement());

			this.invokeGetOrgListAPI(this.getBrandCode());
		}

		super.handleApiCompletion(apiContext);
	}
	private String getBrandCode() {
		String codeLongDescription=null;
		Element eleOrgSelectionCriteria = getExtentionModel("Extn_XPXBrandCodeList");
		List commonCodeList=YRCXmlUtils.getChildren(eleOrgSelectionCriteria, "CommonCode");
		for (Object object : commonCodeList) {
			Element ele=(Element) object;
			codeLongDescription=ele.getAttribute("CodeLongDescription");
			if(codeLongDescription.equalsIgnoreCase(enterpriseCode)){
				selectedBrandCode=ele.getAttribute("CodeValue");
				break;
			}
		}
		return selectedBrandCode;
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
		if(YRCPlatformUI.equals("com.yantra.pca.ycd.rcp.tasks.customerSearch.wizardpages.YCDCustomerSearchWizardPage", pageBeingShown))
		{	
			prepareCustomerTypeDropdownModel();
			prepareDivisionTypeDropdownModel();
			enterpriseCode = getModel("UserNameSpace").getAttribute("EnterpriseCode");
			invokeBrandCodeListAPI();
			addEventHandler("cmbOrganizationCode", SWT.Selection);//#Fixed XNGTP-1021
		}
	}


	/**
	 * Method for validating the text box.
	 */
	public YRCValidationResponse validateTextField(String fieldName, String fieldValue) {
		return super.validateTextField(fieldName	, fieldValue);
	}

	/**
	 * Method for validating the combo box entry.
	 */
	public void validateComboField(String fieldName, String fieldValue) {
		super.validateComboField(fieldName, fieldValue);
	}

	/**
	 * Method called when a button is clicked.
	 */
	public YRCValidationResponse validateButtonClick(String fieldName) {
		return super.validateButtonClick(fieldName);
	}

	/**
	 * Method called when a link is clicked.
	 */
	public YRCValidationResponse validateLinkClick(String fieldName) {
		return super.validateLinkClick(fieldName);
	}

	/**
	 * Create and return the binding data for advanced table columns added to the tables.
	 */
	public YRCExtendedTableBindingData getExtendedTableBindingData(String tableName, ArrayList tableColumnNames) {
		final YRCExtendedTableBindingData tblBindingData = new YRCExtendedTableBindingData(tableName);
		if(YRCPlatformUI.equals(tableName, "tblBusinessCustomer"))
		{
			HashMap<String, YRCTblClmBindingData> bindingDataMap = new HashMap<String, YRCTblClmBindingData>();
			final YRCTblClmBindingData custTypeBindingData = new YRCTblClmBindingData();
			custTypeBindingData.setName("extn_clmCustomerType");
			custTypeBindingData.setAttributeBinding("Extn/@ExtnSuffixType");
			custTypeBindingData.setTooltipBinding("Extn/@ExtnSuffixType");
			custTypeBindingData.setLabelProvider(new IYRCTableColumnTextProvider(){
				public String getColumnText(Element eleData) {
					String suffixType= YRCXmlUtils.getXPathElement(eleData,"/Customer/Extn").getAttribute("ExtnSuffixType");
					if(!YRCPlatformUI.isVoid(suffixType))
					{
						if ("MC".equals(suffixType)) {
							return "Master Customer";
						} else if ("C".equals(suffixType)) {
							return "Customer";
						} else if ("B".equals(suffixType)) {
							return "Bill-To";
						} else if ("S".equals(suffixType)) {
							return "Ship-To";
						}
					}
					return "";
				}
			});
			bindingDataMap.put("extn_clmCustomerType", custTypeBindingData);

			final YRCTblClmBindingData shipFrmDivBindingData = new YRCTblClmBindingData();
			shipFrmDivBindingData.setName("extn_cml_shipFromDivision");
			shipFrmDivBindingData.setAttributeBinding("Extn/@ExtnShipFromBranch");
			shipFrmDivBindingData.setTooltipBinding("Extn/@ExtnShipFromBranch");
			bindingDataMap.put("extn_cml_shipFromDivision", shipFrmDivBindingData);

			tblBindingData.setTableColumnBindingsMap(bindingDataMap);		

		}
		return tblBindingData;		 
	}

	public boolean preCommand(YRCApiContext cxt) {
		if (cxt.getApiName().equalsIgnoreCase("getCustomerList")) {
			
			String customerIdOrName=getFieldValue("extn_customerIdOrName");
			String newTxtUserID = getFieldValue("txtUserID");
			String newtxtEmailID = getFieldValue("txtEmailID");
			String newtxtDayPhone = getFieldValue("txtDayPhone");
			Element eleInput = cxt.getInputXml().getDocumentElement();
			
			//eleInput.setAttribute("CustomerID","");
			//eleInput.removeAttribute("CallingOrganizationCode");
			//YRCXmlUtils.setAttributeValue(eleInput, "/Customer/Extn/@ExtnBrandCode", selectedBrandCode);
			eleInput.removeAttribute("CustomerID");
			eleInput.removeAttribute("ExtnBrandCode");
			//Added for JIRA 4247
			if(YRCPlatformUI.isVoid(customerIdOrName)){
				NodeList buyerOrgElems = eleInput.getElementsByTagName("BuyerOrganization");
				if(buyerOrgElems!=null && buyerOrgElems.getLength()>0)
				{
					Node buyerOrgElem = buyerOrgElems.item(0);
					eleInput.removeChild(buyerOrgElem);
				}
			}
			
			//Get the binding value for ExtnCustomerDivision
			String customerDivsionStr[]=null;
			String customerDivision=YRCXmlUtils.getAttributeValue(eleInput, "/Customer/Extn/@ExtnShipFromBranch");
			if(!YRCPlatformUI.isVoid(customerDivision)){
				customerDivsionStr=customerDivision.split("_");
				if (!YRCPlatformUI.isVoid(customerDivsionStr[0])) {
					YRCXmlUtils.setAttributeValue(eleInput,
							"/Customer/Extn/@ExtnShipFromBranch",
							customerDivsionStr[0]);
				}
				if (!YRCPlatformUI.isVoid(customerDivsionStr[1])) {
					YRCXmlUtils.setAttributeValue(eleInput,
							"/Customer/Extn/@ExtnEnvironmentCode",
							customerDivsionStr[1]);
				}				
			}
			
			if (!YRCPlatformUI.isVoid(customerIdOrName)) { 
				//Remove any BuyerOrganization elements, if it already exists
				NodeList buyerOrgElems = eleInput.getElementsByTagName("BuyerOrganization");
				if(buyerOrgElems!=null && buyerOrgElems.getLength()>0)
				{
					Node buyerOrgElem = buyerOrgElems.item(0);
					eleInput.removeChild(buyerOrgElem);
				}
				
				Element buyerOrgElement = YRCXmlUtils
						.createFromString(
								"<BuyerOrganization><ComplexQuery Operation='OR'><Or><Exp Name='OrganizationCode' Value='"
										+ customerIdOrName
										+ "' QryType='LIKE'/> <Exp Name='OrganizationName' Value='"
										+ customerIdOrName
										+ "' QryType='LIKE'/></Or></ComplexQuery></BuyerOrganization>")
						.getDocumentElement();
				YRCXmlUtils.importElement(eleInput, buyerOrgElement);
			}
			//Added for JIRA 4247
			if (YRCPlatformUI.isVoid(newTxtUserID) ) {
				Element CustomerElement = YRCXmlUtils.getXPathElement(eleInput, "/Customer/CustomerContactList/CustomerContact");
				if(CustomerElement!=null)
				{
					CustomerElement.removeAttribute("UserID");
				}
			}
			//Added for JIRA 4247
			if (YRCPlatformUI.isVoid(newtxtDayPhone)) {
				Element CustomerElement = YRCXmlUtils.getXPathElement(eleInput, "/Customer/CustomerContactList/CustomerContact");
				if(CustomerElement!=null)
				{
					CustomerElement.removeAttribute("DayPhone");
				}
			}
			cxt.setInputXml(eleInput.getOwnerDocument());
		}
		return true;//super.preCommand(cxt);
	}

	@Override
	//Binding ExtnCustomerDivision code to Extn_DivisionList
	public Object getBindingData(String fieldName) {
		if(fieldName.equalsIgnoreCase("extn_exn_comboDivision")){
			YRCComboBindingData bindingData= new YRCComboBindingData();
			bindingData.setCodeBinding("OrganizationCode");
			bindingData.setName("extn_exn_comboDivision");
			bindingData.setListBinding("Extn_DivisionList:/OrganizationList/Organization");
			bindingData.setSourceBinding("");
			bindingData.setTargetBinding("getCustomerList_input:/Customer/Extn/@ExtnShipFromBranch");
			bindingData.setDescriptionBinding("OrganizationCode;OrganizationName");
			bindingData.setKey("xpedx_DivisionList_ExtnCustomerDivsion");	
			return bindingData;
		}
		return super.getBindingData(fieldName);
	}
	 public void postSetModel(String model) {
		 if("input".equalsIgnoreCase(model)){
			 Element eleInput = getModel("input");
			/**
			 * Setting the dummy value for CustomerID as default 'D' before populating YCDCustomerSearchWizardPage 
			 * Set default value for CustomerID to avoid the POPUP while searching.
			 */ 
			 eleInput.setAttribute("CustomerID", "D");	
			 repopulateModel("input");
		 }
		 super.postSetModel(model);
	 }
	
//JIRA 3582 - Condition added for reading data from Application Init. Method added to create XML binding for  xpedx
   public void prepareDivisionTypeDropdownModel(){
	  
		Element Extn_DivisionList = YRCXmlUtils.createDocument(
				"OrganizationList").getDocumentElement();

		Element attrElemOrg = null;
		HashMap<String, String> divisionMap = new HashMap<String, String>();
		divisionMap = XPXUtils.divisionMap;
	    SortedSet<String> sortedset= new TreeSet<String>(divisionMap.keySet());
	   
	    Iterator<String> it = sortedset.iterator();

	    while (it.hasNext()) {
	    	String key  = it.next();
	    	if ((key != null && !"_".equalsIgnoreCase(key)) &&  (divisionMap.get(key) != null)) {
	    		attrElemOrg = YRCXmlUtils.createChild(Extn_DivisionList,
			"Organization");
	    		attrElemOrg.setAttribute("OrganizationCode", key);
	    		attrElemOrg.setAttribute("OrganizationName", divisionMap.get(key));
	    	}
	    }
		setExtentionModel("Extn_DivisionList", Extn_DivisionList);
		
   }
}


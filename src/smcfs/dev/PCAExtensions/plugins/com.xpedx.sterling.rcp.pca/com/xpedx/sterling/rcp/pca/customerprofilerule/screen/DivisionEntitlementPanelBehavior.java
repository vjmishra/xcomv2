/*
 * Created on Apr 14,2010
 * 
 */
package com.xpedx.sterling.rcp.pca.customerprofilerule.screen;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.customerprofilerule.editor.XPXCustomerProfileRuleEditor;
import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;
/**
 * @author Manas
 * 
 * Generated by MTCE Copyright � 2005, 2006 Sterling Commerce, Inc. All Rights
 * Reserved.
 */

public class DivisionEntitlementPanelBehavior extends YRCBehavior {

	private DivisionEntitlementPanel page;
	private Element customerElementBeforeUpdate;
	private Element targetRulesModel;
	private Element inputElement;
	private String customerKey;
	private String suffixType;
	private CustomerProfileMaintenance parentObj;

	/**
	 * Constructor for the behavior class.
	 */
	public DivisionEntitlementPanelBehavior(DivisionEntitlementPanel ownerComposite,Object inputObject,String formId,CustomerProfileMaintenance parentObj) {
		super(ownerComposite,formId,inputObject);
		this.page = ownerComposite;
		this.inputElement = ((YRCEditorInput) inputObject).getXml();
		this.parentObj = parentObj;
		Element generalInfo= parentObj.getBehavior().getLocalModel("XPXCustomerIn");
		setModel("XPXGetUserList",parentObj.getBehavior().getLocalModel("XPXGetUserList"));
		setModel("SalesRepList",parentObj.getBehavior().getLocalModel("SalesRepList"));
		setModel("PrimarySalesRepList",parentObj.getBehavior().getLocalModel("PrimarySalesRepList"));
		setModel("XPXUseSKUList", YRCXmlUtils.createFromString("<UseSKUList><UseSKU Code='1' Description='Customer Item#' /><UseSKU Code='2' Description='Manufacturer Item#' /><UseSKU Code='3' Description='MPC SKU' /></UseSKUList>").getDocumentElement());
		setModel("XPXCustomerIn",generalInfo);
		customerKey = YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey");
		suffixType = YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(generalInfo, "/CustomerList/Customer/Extn"), "ExtnSuffixType");
		initPage();
		
			
	}
	/**
	 * This method initializes the behavior class.
	 */
	@Override
	public void init() {
//		System.out.println("init().....");
		}

	public void initPage() {
		String brandCode=YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(getModel("XPXCustomerIn"), "/CustomerList/Customer/Extn"), "ExtnBrandCode");
//TODO : Remove hard code value
//		brandCode="BDUN";
		/*if(!YRCPlatformUI.isVoid(brandCode))
		{
			callApi("XPXGetBrandCodeList", YRCXmlUtils.createFromString("<CommonCode CodeType='"+XPXConstants.COMMON_CODE_XPX_BRAND+"' CodeValue='"+brandCode+"'/>"),null);
		}
		
		
		String invoiceDistMethodCode=YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(getModel("XPXCustomerIn"), "/CustomerList/Customer/Extn"), "ExtnInvoiceDistMethod");
//		TODO : Remove hard code value
//		invoiceDistMethodCode="E";
		if(!YRCPlatformUI.isVoid(brandCode))
		{
			callApi("XPXGetInvoiceDistMethodCodeList", YRCXmlUtils.createFromString("<CommonCode CodeType='XPXInvoiceDM' CodeValue='"+invoiceDistMethodCode+"'/>"),null);
			
		}*/
		//Added By Manas
		customerKey = YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey");
		System.out.println("The customerKey is" + customerKey);
		if(!YRCPlatformUI.isVoid(customerKey))
		{
			callApi("XPXGetCustomerList", YRCXmlUtils.createFromString("<Customer  CustomerKey='" + YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey") + "' />"),null);
			
			
		}
		
		
	}
	public void reInitPage() {
		callApi("XPXGetCustomerList", YRCXmlUtils.createFromString("<Customer  CustomerKey='" + YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey") + "' />"),
				"com.xpedx.sterling.rcp.pca.customerprofilerule.screen.CustomerProfileMaintenance");
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yantra.yfc.rcp.YRCBaseBehavior#handleApiCompletion(com.yantra.yfc.rcp.YRCApiContext)
	 */
	@Override
	public void handleApiCompletion(YRCApiContext ctx) {
		if (ctx.getInvokeAPIStatus() > 0){
			if (page.isDisposed()) {
				YRCPlatformUI.trace("Page is Disposed");
			} else {
				if (ctx.getApiName().equals("XPXGetCustomerList")) {
					Element outXml = ctx.getOutputXml().getDocumentElement();
					if(!YRCPlatformUI.isVoid(outXml))
					{
						parentObj.getBehavior().setCustomerDetails(outXml);
						parentObj.refreshTabs(true);
						setDivisionEntitlement(outXml);
						
					}
					
					//repopulateModel(arg0)
					//customerElementBeforeUpdate = outXml;
					//setModel("XPXCustomerIn",outXml);
					//suffixType = YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(customerElementBeforeUpdate, "/CustomerList/Customer/Extn"), "ExtnSuffixType");
					//page.createMainComposite();
					//this.handleCustomerProfileRule(outXml);
					//page.showRootPanel(true);
				}
				if (ctx.getApiName().equals("manageCustomer")) {
					Element outXml = ctx.getOutputXml().getDocumentElement();
					setModel("XPXManageCustomer_output", outXml);
					YRCPlatformUI.setMessage("UPDATE_STATUS_MESSAGE");
					//page.clearPage();
					this.reInitPage();
				}
				if ("XPXGetBrandCodeList".equals(ctx.getApiName())) {
					Element outXml = ctx.getOutputXml().getDocumentElement();
					updateModelWithBrandName(outXml);
				}		
				if ("XPXGetInvoiceDistMethodCodeList".equals(ctx.getApiName())) {
					Element outXml = ctx.getOutputXml().getDocumentElement();
					updateModelWithInvoiceDistMthds(outXml);
				}	
				((XPXCustomerProfileRuleEditor)YRCDesktopUI.getCurrentPart()).showBusy(false);
			}
		}//In case of Invoke API failure
		else if(ctx.getInvokeAPIStatus()==-1){
			Element outXml = ctx.getOutputXml().getDocumentElement();
			if("Errors".equals(outXml.getNodeName())){
					Element errorEle = (Element) outXml.getElementsByTagName(
							"Error").item(0);
					if (!YRCPlatformUI.isVoid(errorEle)) {
						YRCPlatformUI.trace(errorEle
								.getAttribute("ErrorDescription"), outXml);
						YRCPlatformUI.showError("Failed!", errorEle
								.getAttribute("ErrorDescription"));
					}
				}
			}
		
		super.handleApiCompletion(ctx);
	}
	private void setDivisionEntitlement(Element outXml){
		Element generalInfo = getModel("XPXCustomerIn");
		Element custEle = YRCXmlUtils.getXPathElement(generalInfo, "/CustomerList/Customer");
		Element customer = YRCXmlUtils.getXPathElement(outXml, "/CustomerList/Customer");
		String relationShipType = customer.getAttribute("RelationshipType");
		
		System.out.println("The relationshiptype is" + relationShipType);
		Element userElem = YRCPlatformUI.getUserElement();
		
		List nodesList=YRCXmlUtils.getChildren(userElem, "UserGroupLists/UserGroupList");
		NodeList nodList=userElem.getElementsByTagName("UserGroupList");
		for(int i=0;i<nodList.getLength();i++){
		Element eleCust=(Element) nodList.item(i);
		Element groupID = YRCXmlUtils.getXPathElement(eleCust, "/UserGroupList/UserGroup");
		String groupId = groupID.getAttribute("UsergroupId");
		if(relationShipType != null && relationShipType != "" ){
			setFieldValue("divisionEntitlement", "Y");
			getControl("divisionEntitlement").setEnabled(false);
			custEle.setAttribute("RelationshipType", "Y");
			setModel("XPXCustomerIn", generalInfo);
			if (groupId != null && groupId.equalsIgnoreCase("XPXEBusinessAdmin")){
				
				getControl("divisionEntitlement").setEnabled(true);	
			}
		}
		else{
			setFieldValue("divisionEntitlement", "N");
			getControl("divisionEntitlement").setEnabled(false);
			custEle.setAttribute("RelationshipType", "N");
			setModel("XPXCustomerIn", generalInfo);
			if (groupId != null && groupId.equalsIgnoreCase("XPXEBusinessAdmin")){
				
				getControl("divisionEntitlement").setEnabled(true);	
			}
		}
		}
		
	}
	private void updateModelWithBrandName(Element outXml) {
		Element generalInfo = getModel("XPXCustomerIn");
		Element extnEle = YRCXmlUtils.getXPathElement(generalInfo, "/CustomerList/Customer/Extn");
		Element commonCodeEle = YRCXmlUtils.getXPathElement(outXml, "/CommonCodeList/CommonCode");
		extnEle.setAttribute("ExtnBrandCodeName", commonCodeEle.getAttribute("CodeShortDescription"));
		setModel("XPXCustomerIn", generalInfo);
	}
	private void updateModelWithInvoiceDistMthds(Element outXml) {
		Element generalInfo = getModel("XPXCustomerIn");
		Element extnEle = YRCXmlUtils.getXPathElement(generalInfo, "/CustomerList/Customer/Extn");
		Element commonCodeEle = YRCXmlUtils.getXPathElement(outXml, "/CommonCodeList/CommonCode");
		extnEle.setAttribute("ExtnInvoiceDistMethodName", commonCodeEle.getAttribute("CodeShortDescription"));
		setModel("XPXCustomerIn", generalInfo);
	}
	private void handleCustomerProfileRule(Element rulesDetailsElem) {
		setDirty(false);
	}

	private void callApi(String apinames, Document inputXml, String formId) {
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId((!YRCPlatformUI.isVoid(formId))?formId:page.getFormId());
		ctx.setApiName(apinames);
		ctx.setInputXml(inputXml);
		if (!page.isDisposed())
			callApi(ctx, page);
	}
	
	public void updateProfile() {
		if(!validateFieldsBeforeUpdate())
			return;
		targetRulesModel = this.getTargetModel("XPXCustomerOut");
		targetRulesModel.setAttribute("CustomerKey", customerKey);
		targetRulesModel = prepareInputWithCustomerInfo(targetRulesModel);
		String eCSREmail1="";
		String eCSREmail2="";
		String eCSR1LoginId="";
		String eCSR2LoginId="";
		if(!page.comboECSR.isDisposed()){
		 eCSREmail1= (String)YRCXPathUtils.evaluate(this.getModel("XPXGetUserList"), "/UserList/User[@UserKey='"+ getFieldValue("comboECSR") +"']/ContactPersonInfo/@EMailID", XPathConstants.STRING);
		
		eCSREmail2= (String)YRCXPathUtils.evaluate(this.getModel("XPXGetUserList"), "/UserList/User[@UserKey='"+ getFieldValue("comboECSR2") +"']/ContactPersonInfo/@EMailID", XPathConstants.STRING);
		
		eCSR1LoginId= (String)YRCXPathUtils.evaluate(this.getModel("XPXGetUserList"), "/UserList/User[@UserKey='"+ getFieldValue("comboECSR") +"']/@Loginid",XPathConstants.STRING);
		}
		if(!page.comboECSR2.isDisposed())
		eCSR2LoginId= (String)YRCXPathUtils.evaluate(this.getModel("XPXGetUserList"), "/UserList/User[@UserKey='"+ getFieldValue("comboECSR2") +"']/@Loginid",XPathConstants.STRING);
		YRCXmlUtils.setAttributeValue(targetRulesModel, "/Customer/Extn/@ExtnECsr1EMailID",eCSREmail1);
		YRCXmlUtils.setAttributeValue(targetRulesModel, "/Customer/Extn/@ExtnECsr2EMailID",eCSREmail2);
		YRCXmlUtils.setAttributeValue(targetRulesModel, "/Customer/Extn/@ExtnECSR",eCSR1LoginId );
		YRCXmlUtils.setAttributeValue(targetRulesModel, "/Customer/Extn/@ExtnECSR2",eCSR2LoginId);
		callUpdateApi();		
	}	
	private Element prepareInputWithCustomerInfo(Element targetRulesModel2) {
		if ("".equals(this.page.getCustomerAdditionalAddressKey())) {
			YRCXmlUtils
			.getXPathElement(targetRulesModel2,
					"/Customer/CustomerAdditionalAddressList")
			.setAttribute("Reset", "Y");
			
			
			if ("S".equals(getSuffixType())) {
				YRCXmlUtils
						.getXPathElement(targetRulesModel2,
								"/Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress")
						.setAttribute("IsDefaultShipTo", "Y");
				YRCXmlUtils
						.getXPathElement(targetRulesModel2,
								"/Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress")
						.setAttribute("IsShipTo", "Y");

			} else {
				YRCXmlUtils
						.getXPathElement(targetRulesModel2,
								"/Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress")
						.setAttribute("IsDefaultBillTo", "Y");
				YRCXmlUtils
						.getXPathElement(targetRulesModel2,
								"/Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress")
						.setAttribute("IsBillTo", "Y");
			}

		} else {
			YRCXmlUtils.getXPathElement(targetRulesModel2,
					"/Customer/CustomerAdditionalAddressList").setAttribute(
					"Reset", "N");
			YRCXmlUtils
			.getXPathElement(targetRulesModel2,
					"/Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress")
			.setAttribute("CustomerAdditionalAddressKey", this.page.getCustomerAdditionalAddressKey());			
		}
		// TODO Auto-generated method stub
		return targetRulesModel2;
	}
	public void callUpdateApi() {
			YRCApiContext ctx = new YRCApiContext();
			ctx.setApiName("manageCustomer");
			ctx.setInputXml(targetRulesModel.getOwnerDocument());
			ctx.setFormId(page.getFormId());
			ctx.setShowError(false);
			ctx.setUserData("isRefreshReqd", String.valueOf(false));
			callApi(ctx, page);
			((XPXCustomerProfileRuleEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
	}
	public String getSuffixType(){
		return suffixType;
	}
	private boolean validateFieldsBeforeUpdate() {

		boolean noError= true;
		
		if ("Y".equalsIgnoreCase(getFieldValue("chkCustomerLineAcctNumberFlag"))
				&& YRCPlatformUI.isVoid(getFieldValue("txtCustomerLineAccountNumberLabel"))) {

			YRCPlatformUI.showError("MESSAGE_ACCOUNT_LABEL_IS_MANDATORY",
					YRCPlatformUI
							.getString("MESSAGE_ACCOUNT_LABEL_IS_MANDATORY"));
			getControl("txtCustomerLineAccountNumberLabel").setFocus();		
			return false;
		}
		if ("Y".equalsIgnoreCase(getFieldValue("chkCustomerLineField1Flag"))
				&& YRCPlatformUI.isVoid(getFieldValue("txtCustomerLineField1"))) {

			YRCPlatformUI.showError("MESSAGE_FIELD1_IS_MANDATORY",
					YRCPlatformUI
							.getString("MESSAGE_FIELD1_LABEL_IS_MANDATORY"));
			//setFieldInError("txtCustomerLineField1", new YRCValidationResponse(1,"MESSAGE_FIELD1_LABEL_IS_MANDATORY"));
			getControl("txtCustomerLineField1").setFocus();		
			return false;
		}
		if ("Y".equalsIgnoreCase(getFieldValue("chkCustomerLineField2Flag"))
				&& YRCPlatformUI.isVoid(getFieldValue("txtCustomerLineField2"))) {

			YRCPlatformUI.showError("MESSAGE_FIELD2_IS_MANDATORY",
					YRCPlatformUI
							.getString("MESSAGE_FIELD2_LABEL_IS_MANDATORY"));
			getControl("txtCustomerLineField2").setFocus();
			return false;
		}
		if ("Y".equalsIgnoreCase(getFieldValue("chkCustomerLineField3Flag"))
				&& YRCPlatformUI.isVoid(getFieldValue("txtCustomerLineField3"))) {

			YRCPlatformUI.showError("MESSAGE_FIELD3_IS_MANDATORY",
					YRCPlatformUI
							.getString("MESSAGE_FIELD3_LABEL_IS_MANDATORY"));
			getControl("txtCustomerLineField3").setFocus();
			return false;
		}
		if("C".equals(getSuffixType())&& !YRCPlatformUI.isVoid(getFieldValue("txtBuyerId"))&& YRCPlatformUI.isVoid(getFieldValue("txtUomType")))
		{
			YRCPlatformUI.showError("MESSAGE_FIELD4_LABEL_IS_MANDATORY",
					YRCPlatformUI
							.getString("MESSAGE_FIELD4_LABEL_IS_MANDATORY"));
			getControl("txtUomType").setFocus();	
			return false;
		}
		
		Element targetModel =  getTargetModel("XPXCustomerOut");
		if(!YRCPlatformUI.isVoid(YRCXmlUtils.getXPathElement(targetModel, "/Customer/Extn")))
		{	String minAmount="0.0";
			String maxAmount="0.0";
			if(!page.txtMinOrderAmount.isDisposed())
			 minAmount = YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(targetModel, "/Customer/Extn"), "ExtnMinOrderAmount");
			
			if(!page.txtMaxOrderAmount.isDisposed())
			maxAmount = YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(targetModel, "/Customer/Extn"), "ExtnMaxOrderAmount");
			if(YRCPlatformUI.isVoid(minAmount)){
				minAmount="0.0";
			}
			if(YRCPlatformUI.isVoid(maxAmount)){
				maxAmount="0.0";
			}
			BigDecimal minAmountDecimal = new BigDecimal(minAmount);
		    BigDecimal maxAmountDecimal = new BigDecimal(maxAmount);
		    if((!"0.0".equals(minAmount)) && (!"0.0".equals(maxAmount))){
		    	if(maxAmountDecimal.compareTo(minAmountDecimal)<0 )
			    {
			    	YRCPlatformUI.showWarning("warning", "MAX AMOUNT SHOULD BE GREATER THAN MIN AMOUNT!!");
			    	getControl("txtMaxOrderAmount").setFocus();
				    return false;
			    }
		    }
		}
		return noError;
		 
	}	
	public Element getLocalModel(String modelName){
		return getModel(modelName);
	}
	
	public void openDetailEditor(String linkName)
    {
        Element newCustomerElement =prepareEditorInput(linkName);;
        boolean isCustomerEditorFound = false;
        IEditorReference editorReferences[] = PlatformUI.getWorkbench ().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
        for(int i = 0; i < editorReferences.length; i++)
        {
            try
            {
                org.eclipse.ui.IEditorInput tEditorInput = editorReferences [i].getEditorInput();
                org.eclipse.ui.IEditorPart tEditor = editorReferences [i].getEditor(false);
                
                if(tEditor == null || !("com.yantra.pca.ycd.rcp.editors.YCDCustomerEditor".equals(tEditor.getClass().getName())))
                    continue;
                Element customerEditorInput = ((YRCEditorInput)tEditorInput).getXml();
                if(YRCPlatformUI.isVoid(customerEditorInput)|| !YRCPlatformUI.equals(newCustomerElement.getAttribute("CustomerKey"),customerEditorInput.getAttribute("CustomerKey")))
                    continue;
                YRCPlatformUI.openEditor ("com.yantra.pca.ycd.rcp.editors.YCDCustomerEditor",(YRCEditorInput)tEditorInput);
                isCustomerEditorFound = true;
                break;
            }
            catch(Exception e)
            {
                YRCPlatformUI.trace("Exception in getOpenDraftOrderList :", e);
            }
        }

        if(!isCustomerEditorFound)
            YRCPlatformUI.openEditor
("com.yantra.pca.ycd.rcp.editors.YCDCustomerEditor", new YRCEditorInput (newCustomerElement, new String[]{"CustomerKey"}, "YCD_TASK_CUSTOMER_DETAILS"));

}
	
	private Element prepareEditorInput(String linkName)
	{
		Element generalInfo = getModel("XPXCustomerIn");
		Element newCustomerElement = null;
		if("linkMasterCustomer".equals(linkName))
		{
			newCustomerElement = YRCXmlUtils.getXPathElement(generalInfo, "/CustomerList/Customer/ParentMasterCustomer/Customer");
		}
		if("linkCustomer".equals(linkName))
		{
			newCustomerElement = YRCXmlUtils.getXPathElement(generalInfo, "/CustomerList/Customer/ParentCustomerCustomer/Customer");
		}
		if("linkBillTo".equals(linkName))
		{
			newCustomerElement = YRCXmlUtils.getXPathElement(generalInfo, "/CustomerList/Customer/ParentBillToCustomer/Customer");
		}		
	        newCustomerElement.setAttribute("CustomerType","01");
	        return newCustomerElement;
	}
}


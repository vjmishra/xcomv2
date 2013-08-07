	
/*
 * Created on Jul 12,2010
 *
 */
package com.xpedx.sterling.rcp.pca.divisionMaintainance.screen;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.divisionMaintainance.editor.XPXDivisionMaintainanceEditor;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

/**
 * @author jkotha
 *
 * Generated by Sterling RCP Tools
 */
 
public class DivisionMaintainancePanelBehavior extends YRCBehavior {

	/**
	 * Constructor for the behavior class. 
	 */
	private DivisionMaintainancePanel page ;
	private Element inputElement,divisionElementbeforeUpdate;
	
    @SuppressWarnings("deprecation")
	public DivisionMaintainancePanelBehavior(Composite ownerComposite, String formId, Object inputObject) {
        super(ownerComposite, formId);
        this.page = (DivisionMaintainancePanel) ownerComposite;
        this.inputElement = ((YRCEditorInput) inputObject).getXml();
        initPage();
    }
	/**
	 * This method initializes the behavior class.
	 */    
	public void init() {
		Document eleDivisionTypes = YRCXmlUtils.createFromString("<Types><Type TypeCode='Internal' TypeDescription='Internal'/><Type TypeCode='External' TypeDescription='External'/> </Types>");
		setModel("DivisionTypes",eleDivisionTypes.getDocumentElement());
//		System.out.println("init().....");
		// write behavior init here
	}
	
	public void initPage(){
		
		String[] apinames = {"getUserList","getOrganizationHierarchy"};
		Document[] docInput = {
				YRCXmlUtils.createFromString("<User Activateflag='Y' OrganizationKey='"+ YRCPlatformUI.getUserElement().getAttribute("EnterpriseCode") +"' Usertype='EXTERNAL' UsertypeQryType='NE'/>"),
				YRCXmlUtils.createFromString("<Organization  OrganizationName='" + YRCXmlUtils.getAttribute(this.inputElement, "OrganizationName") + "' " +
				"OrganizationCode='" + YRCXmlUtils.getAttribute(this.inputElement, "OrganizationCode") + "' />")						
		};
		callApis(apinames, docInput);
	}
	
	private void callApis(String apinames[], Document inputXmls[]) {
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(page.getFormId());
		ctx.setApiNames(apinames);
		ctx.setInputXmls(inputXmls);
		if (!page.isDisposed())
			callApi(ctx, page);
	}

	private void callApi(String apiName, Document inputXml) {
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId("com.xpedx.sterling.rcp.pca.divisionMaintainance.screen.DivisionMaintainancePanel");
		ctx.setApiName(apiName);
		ctx.setInputXml(inputXml);
//		System.out.println("In API call");
		if (!page.isDisposed())
			callApi(ctx, page);
		
	}
	
	public void handleApiCompletion(YRCApiContext ctx) {
		if (ctx.getInvokeAPIStatus() > 0){
			if (page.isDisposed()) {
				YRCPlatformUI.trace("Page is Disposed");
			} else {
				String[] apinames = ctx.getApiNames();
				for (int i = 0; i < apinames.length; i++) {
					String apiname = apinames[i];
					if (YRCPlatformUI.equals("getOrganizationHierarchy", apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						
						divisionElementbeforeUpdate = outXml;
						//Start display all BrandCodes comma seperated
						Element extnEle =YRCXmlUtils.getChildElement(divisionElementbeforeUpdate, "Extn");
						Element divBrandListEle = YRCXmlUtils.getChildElement(extnEle, "XPXDivBrandList");
						NodeList nl = divBrandListEle.getElementsByTagName("XPXDivBrand");
						int noOfDivBrands = nl.getLength();
						String appendedBrandCode = null;
						for (int j = 0; j < noOfDivBrands; j++) {
							Element eleDivBrand =(Element)nl.item(j);
							if(eleDivBrand.getAttribute("BrandCode") != null){
								if (j == 0){
									appendedBrandCode = eleDivBrand.getAttribute("BrandCode");
								}
								else
									appendedBrandCode = appendedBrandCode.concat(","+eleDivBrand.getAttribute("BrandCode"));
							}
						}
						divBrandListEle.setAttribute("BrandCodes", appendedBrandCode);
						//end
						
						
						Element divListEle = YRCXmlUtils.getChildElement(extnEle, "XPXXferCircleList");
						NodeList nl1 = divListEle.getElementsByTagName("XPXXferCircle");
						int noOfDivBrands1 = nl1.getLength();
						ArrayList refDiv = new ArrayList();
						for (int j = 0; j < noOfDivBrands1; j++) {
							Element eleDivBrand = (Element) nl1.item(j);
							refDiv.add(eleDivBrand.getAttribute("DivisionNo"));
						}
						XPXUtils.setRefDiv(refDiv);
						setModel("Organization", divisionElementbeforeUpdate);
					//	System.out.println("required " + YRCXmlUtils.getString(divisionElementbeforeUpdate));
//					System.out.println(YRCXmlUtils.getString(divisionElementbeforeUpdate));
						page.showRootPanel(true);
						/*START:1 Following Code is used to get the Modified by user details.**/
						String strModifiedBy = YRCXmlUtils.getAttribute(divisionElementbeforeUpdate,"Modifyuserid");
						if(!YRCPlatformUI.isVoid(strModifiedBy)){
							Document docModifiedByUser = YRCXmlUtils.createFromString("<User Loginid='"+strModifiedBy+"' />");
							callApi("getModifiedByUserInfo", docModifiedByUser);
						}
						/*END:1*/
					} else if (YRCPlatformUI.equals("manageOrganizationHierarchy", apiname)) {
						ctx.getOutputXml();
						initPage();
					} else if (YRCPlatformUI.equals("getUserList", apiname)) {
						setModel("ContactList", ctx.getOutputXmls()[i].getDocumentElement());
					} else if (YRCPlatformUI.equals("getModifiedByUserInfo", apiname)) {
						Element eleOutput = ctx.getOutputXmls()[i].getDocumentElement();
						
						/* START:2 - Following code handles:
						 * If Modifyby user id is not a valid CSR id in the system
						 * then displays the unknown user id in the manage screen.***/
						String strLoginId = YRCXmlUtils.getAttributeValue(eleOutput, "/UserList/User/@Loginid");
						if(YRCPlatformUI.isVoid(strLoginId)){
							String strModifiedByuserId = YRCXmlUtils.getAttribute(getModel("Organization"), "Modifyuserid");
							YRCXmlUtils.setAttributeValue(eleOutput, "/UserList/User/@Loginid", strModifiedByuserId);
							YRCXmlUtils.setAttributeValue(eleOutput, "/UserList/User/@Username", "Unknown User");
						}
						/*END:2**/
						
						setModel("ModifiedByUser", eleOutput);
					}
				}
				
			}
			((XPXDivisionMaintainanceEditor)YRCDesktopUI.getCurrentPart()).showBusy(false);
		super.handleApiCompletion(ctx);
		}
		else if(ctx.getInvokeAPIStatus()==-1){
			if ("manageOrganizationHierarchy".equals(ctx.getApiName())) {
				Element outXml = ctx.getOutputXml().getDocumentElement();
				if("Errors".equals(outXml.getNodeName())){
					YRCPlatformUI.trace("Failed while updating the Division Profile: ", outXml);
					YRCPlatformUI.showError("Failed!", "Unable to update Division at this time.");
				}
				((XPXDivisionMaintainanceEditor)YRCDesktopUI.getCurrentPart()).showBusy(false);
			}
		}
			
	}
	
	public void updateDivision() {
		
		if(!validateFields())
		{
			return;
		}
		else
		{
			Element targetModel = null;
			Element sourceModel = null;
			targetModel = getTargetModel("SaveOrganization");
			sourceModel = getModel("Organization");
			targetModel.setAttribute("OrganizationCode", sourceModel.getAttribute("OrganizationCode"));
			//merging corporatePersonInfo from source to target
			Element eleTargetCorpPersonInfo = YRCXmlUtils.getChildElement(targetModel, "CorporatePersonInfo");
			Element eleSourceCorpPersonInfo = YRCXmlUtils.getChildElement(sourceModel, "CorporatePersonInfo");
			YRCXmlUtils.mergeElement(eleSourceCorpPersonInfo, eleTargetCorpPersonInfo, true);
			
			//merging contactpersoninfo from source to target
			Element eleTargetContactPersonInfo = YRCXmlUtils.getChildElement(targetModel, "ContactPersonInfo");
			Element eleSourceContactPersonInfo = YRCXmlUtils.getChildElement(sourceModel, "ContactPersonInfo");
			YRCXmlUtils.mergeElement(eleSourceContactPersonInfo, eleTargetContactPersonInfo, true);
			
			//merging BillingPersonInfo from source to target
			Element eleTargetBillingPersonInfo = YRCXmlUtils.getChildElement(targetModel, "BillingPersonInfo");
			Element eleSourceBillingPersonInfo = YRCXmlUtils.getChildElement(sourceModel, "BillingPersonInfo");
			YRCXmlUtils.mergeElement(eleSourceBillingPersonInfo, eleTargetBillingPersonInfo, true);
			
//			System.out.println("Updating");
			callApi("manageOrganizationHierarchy",targetModel.getOwnerDocument());
			((XPXDivisionMaintainanceEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
//			System.out.println(YRCXmlUtils.getString(targetModel.getOwnerDocument()));
//			callApi("manageOrganizationHierarchy",targetModel);				
		}

	}
	private boolean validateFields() {
		Element targetModel =  getTargetModel("SaveOrganization");
		if(!YRCPlatformUI.isVoid(YRCXmlUtils.getXPathElement(targetModel, "/Organization/Extn")))
		{
			String minAmount = YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(targetModel, "/Organization/Extn"), "ExtnMinOrderAmt");
			String maxAmount = YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(targetModel, "/Organization/Extn"), "ExtnMaxOrderAmt");
			BigDecimal minAmountDecimal = new BigDecimal(minAmount);
		    BigDecimal maxAmountDecimal = new BigDecimal(maxAmount);
		    if(maxAmountDecimal.compareTo(minAmountDecimal)<0)
		    {
		    	YRCPlatformUI.showWarning("warning", "MAX AMOUNT SHOULD BE GREATER THAN MIN AMOUNT!!");
		    	getControl("txtMaxOrderAmount").setFocus();
			    return false;
		    }
		}
		return true;
	}
	
	public void cancelUpdate(){
		initPage();
	}

}
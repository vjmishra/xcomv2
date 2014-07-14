package com.xpedx.sterling.rcp.pca.customerprofilerule.screen;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.customerprofilerule.editor.XPXCustomerProfileRuleEditor;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class CustomerProfileMaintIntegDataPanelBehavior extends YRCBehavior {

	private CustomerProfileMaintIntegDataPanel page;
	private static final String COMMAND_GET_LIST_OF_SHIP_TOS_BY_TEMPLATE = "XPXGetListOfShipTosByTemplateService";
	private static final String COMMAND_MANAGE_CUSTOMER = "manageCustomer";
	private static final String COMMAND_MANAGE_MULTIPLE_CUSTOMERS="XPXManageCustomersService";
	
	public CustomerProfileMaintIntegDataPanelBehavior(Composite ownerComposite, Object inputObject, String formId, CustomerProfileMaintenance parentObj) {
		super(ownerComposite,formId,inputObject);
		page = (CustomerProfileMaintIntegDataPanel)ownerComposite;
		Element generalInfo= parentObj.getBehavior().getLocalModel("XPXCustomerIn");
		setModel("XPXCustomerIn",generalInfo);
		
		init();
	}
	
	@Override
	protected void init() {
		Element eleCustomerDtls = getModel("XPXCustomerIn");
		if(!YRCPlatformUI.isVoid(eleCustomerDtls)){
			Document docCustomer = YRCXmlUtils.createDocument("Customer");
			Element eleInput = docCustomer.getDocumentElement();
			eleCustomerDtls = YRCXmlUtils.getChildElement(eleCustomerDtls, "Customer");
			eleInput.setAttribute("CustomerID", eleCustomerDtls.getAttribute("CustomerID"));
			eleInput.setAttribute("OrganizationCode", eleCustomerDtls.getAttribute("OrganizationCode"));
			eleInput.setAttribute("CustomerKey", eleCustomerDtls.getAttribute("CustomerKey"));
			Element eleTemplate = YRCXmlUtils.createChild(eleInput, "Template");
			Document docListTemplate = YRCXmlUtils.createFromString(new StringBuffer()
			.append("<CustomerList>")
			.append("<Customer CustomerKey='' CustomerID='' OrganizationCode=''>")
			.append("<Extn ExtnSuffixType='' ExtnETradingID='' ExtnEDIEmailAddress=''/>")
			.append("<CustomerAdditionalAddressList TotalNumberOfRecords=''>")
			.append("<CustomerAdditionalAddress>")
			.append("<PersonInfo/>")
			.append("</CustomerAdditionalAddress>")
			.append("</CustomerAdditionalAddressList>")
			.append("</Customer>")
			.append("</CustomerList>").toString());
			YRCXmlUtils.importElement(eleTemplate, docListTemplate.getDocumentElement());
			
			YRCApiContext ctx = new YRCApiContext();
			ctx.setApiName(COMMAND_GET_LIST_OF_SHIP_TOS_BY_TEMPLATE);
			ctx.setInputXml(eleInput.getOwnerDocument());
			ctx.setFormId(getFormId());
			callApi(ctx, page);
//			((XPXCustomerProfileRuleEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
		}
		super.init();
	}

	public void update() {
		Element eleUpdate = getTargetModel("XPXCustomerOut");
		Element eleCustomerDtls=getModel("XPXCustomerIn");
		eleCustomerDtls = YRCXmlUtils.getChildElement(eleCustomerDtls, "Customer");
		eleUpdate.setAttribute("CustomerID", eleCustomerDtls.getAttribute("CustomerID"));
		eleUpdate.setAttribute("OrganizationCode", eleCustomerDtls.getAttribute("OrganizationCode"));
		
		//Commented for removing the eTradin table in B2B maintenance panel in Customer Integration Data Maintenance tab for reducing the API call
		
		/*Element eleUpdateShipTosData = getTargetModel("SaveCustomerList");
		eleUpdateShipTosData.setAttribute("ApiName", "manageCustomer");
		NodeList nlCustomer = eleUpdateShipTosData.getElementsByTagName("Customer");
		for(int i=0;i<nlCustomer.getLength();i++){
			Element eleCustomer =(Element)nlCustomer.item(i);
			if(!(eleCustomer.hasAttribute(CustomerProfileMaintIntegDataPanel.IS_EXTN_ETRADING_ID_MODIFIED) 
					|| eleCustomer.hasAttribute(CustomerProfileMaintIntegDataPanel.IS_EXTN_CUST_EMAIL_ADDRESS_MODIFIED))){
				eleCustomer.getParentNode().removeChild(eleCustomer);
				i--;
			} else {
				Element eleExtn = YRCXmlUtils.getChildElement((Element)eleCustomer, "Extn");
				if(!YRCPlatformUI.isVoid(eleExtn))
					eleExtn.removeAttribute("ExtnSuffixType");
				
				
				
				Element eleCustAddlAddrs = YRCXmlUtils.getChildElement(eleCustomer, "CustomerAdditionalAddressList");
				if(!YRCPlatformUI.isVoid(eleCustAddlAddrs))
					eleCustomer.removeChild(eleCustAddlAddrs);
			}
		}*/
		
		YRCApiContext ctx = new YRCApiContext();
		ctx.setApiNames(new String[]{COMMAND_MANAGE_CUSTOMER});
		Document[] docInput = {eleUpdate.getOwnerDocument()};
		ctx.setInputXmls(docInput);
		ctx.setFormId(getFormId());
//		ctx.setShowError(false);
//		ctx.setUserData("isRefreshReqd", String.valueOf(false));
		callApi(ctx, page);
		((XPXCustomerProfileRuleEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
	}
	
	@Override
	public void handleApiCompletion(YRCApiContext ctx) {
		if (ctx.getInvokeAPIStatus() > 0){
			if (page.isDisposed()) {
				YRCPlatformUI.trace("Page is Disposed");
			} else {
				String[] apinames = ctx.getApiNames();
				Document[] docOutput = ctx.getOutputXmls();
				for (int i = 0; i < apinames.length; i++) {
					String apiname = apinames[i];
					if (apiname.equals(COMMAND_MANAGE_CUSTOMER)) {
						Element outXml = docOutput[i].getDocumentElement();
						setModel("XPXManageCustomer_output", outXml);
					} else if (apiname.equals(COMMAND_MANAGE_MULTIPLE_CUSTOMERS)) {
						Element outXml = docOutput[i].getDocumentElement();
						setModel("XPXManageCustomersService_output", outXml);
						YRCPlatformUI.setMessage("UPDATE_STATUS_MESSAGE");					
					} else if(apiname.equals(COMMAND_GET_LIST_OF_SHIP_TOS_BY_TEMPLATE)){
						Element outXml = docOutput[i].getDocumentElement();
						setModel("ShipToCustomerList", outXml);
					}
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

}
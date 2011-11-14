package com.xpedx.sterling.rcp.pca.customerprofilerule.screen;


import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.customerprofilerule.editor.XPXCustomerProfileRuleEditor;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;
/**
 * @author VJ Mishra
 * 
 * 
 */

public class CustomerETradingIDMaintenancePanelBehaviour extends YRCBehavior {

	private CustomerETradingIDMaintenancePanel page;	
	private static final String COMMAND_GET_LIST_OF_SHIP_TOS_BY_TEMPLATE = "XPXGetListOfShipTosByTemplateService";
	private static final String COMMAND_MANAGE_CUSTOMER = "manageCustomer";
	private static final String COMMAND_MANAGE_MULTIPLE_CUSTOMERS="XPXManageCustomersService";


	/**
	 * Constructor for the behavior class.
	 */
	public CustomerETradingIDMaintenancePanelBehaviour(CustomerETradingIDMaintenancePanel ownerComposite,Object inputObject,String formId,CustomerProfileMaintenance parentObj) {
		super(ownerComposite,formId,inputObject);
		
		page = (CustomerETradingIDMaintenancePanel)ownerComposite;
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

		}
		super.init();
	}

	public void updateAll() {
		boolean validateEmail = false;
		String emailId= null;
		Element eleCustomerDtls=this.getModel("XPXCustomerIn");
		eleCustomerDtls = YRCXmlUtils.getChildElement(eleCustomerDtls, "Customer");
	
		Element eleUpdateShipTosData =null ;
		
		eleUpdateShipTosData = getTargetModel("SaveCustomerList");
		
		eleUpdateShipTosData.setAttribute("ApiName", "manageCustomer");
		NodeList nlCustomer = eleUpdateShipTosData.getElementsByTagName("Customer");
		for(int i=0;i<nlCustomer.getLength();i++){
			Element eleCustomer =(Element)nlCustomer.item(i);
			
			if(!(eleCustomer.hasAttribute(CustomerETradingIDMaintenancePanel.IS_EXTN_ETRADING_ID_MODIFIED) 
					|| eleCustomer.hasAttribute(CustomerETradingIDMaintenancePanel.IS_EXTN_CUST_EMAIL_ADDRESS_MODIFIED))){
				eleCustomer.getParentNode().removeChild(eleCustomer);
				i--;
			} else {
				Element eleExtn = YRCXmlUtils.getChildElement((Element)eleCustomer, "Extn");				
				emailId = eleExtn.getAttribute("ExtnEDIEmailAddress");
				if(emailId != null && !"".equals(emailId)){
					validateEmail  = validateEmail(emailId);
						if(validateEmail == false){
							eleExtn.setAttribute("ExtnEDIEmailAddress", eleExtn.getAttribute("ExtnEDIEmailAddressOldValue"));
							return;
						}
					}
				
				if(!YRCPlatformUI.isVoid(eleExtn))
					eleExtn.removeAttribute("ExtnSuffixType");
				
				Element eleCustAddlAddrs = YRCXmlUtils.getChildElement(eleCustomer, "CustomerAdditionalAddressList");
				if(!YRCPlatformUI.isVoid(eleCustAddlAddrs))
					eleCustomer.removeChild(eleCustAddlAddrs);
			}
		}
		
			YRCApiContext ctx = new YRCApiContext();
			ctx.setApiName(COMMAND_MANAGE_MULTIPLE_CUSTOMERS);
			Document docInput = eleUpdateShipTosData.getOwnerDocument();
			ctx.setInputXml(docInput);
			ctx.setFormId(getFormId());
			callApi(ctx, page);
			((XPXCustomerProfileRuleEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
		
	}
	
	public void update(Element element) {
		boolean validateEmail = false;
		String emailId= null;

		Element eleCustomerDtls=this.getModel("XPXCustomerIn");
		eleCustomerDtls = YRCXmlUtils.getChildElement(eleCustomerDtls, "Customer");
	
		Element eleUpdateShipTosData =null ;
		eleUpdateShipTosData = getTargetModel("SaveCustomerList");
		eleUpdateShipTosData.setAttribute("ApiName", "manageCustomer");
		
//		NodeList nlCustomer = element.getElementsByTagName("CustomerID");
			if(!(element.hasAttribute(CustomerETradingIDMaintenancePanel.IS_EXTN_ETRADING_ID_MODIFIED) 
					|| element.hasAttribute(CustomerETradingIDMaintenancePanel.IS_EXTN_CUST_EMAIL_ADDRESS_MODIFIED))){
				element.getParentNode().removeChild(element);
				
			} else {
				Element eleExtn = YRCXmlUtils.getChildElement((Element)element, "Extn");
				emailId = eleExtn.getAttribute("ExtnEDIEmailAddress");
				if(emailId != null && !"".equals(emailId)){
				validateEmail  = validateEmail(emailId);
					if(validateEmail == false){
						eleExtn.setAttribute("ExtnEDIEmailAddress", eleExtn.getAttribute("ExtnEDIEmailAddressOldValue"));
						return;
					}
				}
				if(!YRCPlatformUI.isVoid(eleExtn))
					eleExtn.removeAttribute("ExtnSuffixType");
				
				
				
				Element eleCustAddlAddrs = YRCXmlUtils.getChildElement(element, "CustomerAdditionalAddressList");
				if(!YRCPlatformUI.isVoid(eleCustAddlAddrs))
					element.removeChild(eleCustAddlAddrs);
			}
			
				YRCApiContext ctx = new YRCApiContext();
				ctx.setApiName(COMMAND_MANAGE_MULTIPLE_CUSTOMERS);
				Document docInput = eleUpdateShipTosData.getOwnerDocument();
				ctx.setInputXml(docInput);
				ctx.setFormId(getFormId());
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

	public boolean validateEmail(String emailId){
		String enteredEmail = emailId;	
		if(XPXUtils.validateEmail(enteredEmail))
        {
			return true;
	    }
        else
        {	
        	String exception = "Invalid Email Format.";
        	YRCPlatformUI.showError("Error", exception);
        	//setFieldInError("txtEmailId", new YRCValidationResponse(YRCValidationResponse.YRC_VALIDATION_ERROR,"Invalid Email ID"));
        	return false;
        }
	}
	public void openDetailEditor(String linkName) {
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

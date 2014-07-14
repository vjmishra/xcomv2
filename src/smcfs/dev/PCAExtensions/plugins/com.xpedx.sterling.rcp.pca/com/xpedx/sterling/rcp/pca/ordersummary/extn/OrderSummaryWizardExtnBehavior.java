package com.xpedx.sterling.rcp.pca.ordersummary.extn;

/**
 * Created on Mar 04,2010
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.orderlines.screen.OrderLinesPanel;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCDateTimeUtils;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCExtendedTableBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCValidationResponse;
import com.yantra.yfc.rcp.YRCWizardExtensionBehavior;
import com.yantra.yfc.rcp.YRCXmlUtils;

/**
 * @author sdodda Copyright © 2005-2009 Sterling Commerce, Inc. All Rights
 *         Reserved.
 */
public class OrderSummaryWizardExtnBehavior extends YRCWizardExtensionBehavior {

	private Element eleInOrderXML;
	private boolean repopulate = false;
	// public static void main(String[] args) {
	// Document doc = YRCXmlUtils.createFromString("<CarrierService
	// CallingOrganizationCode='xpedx' UsedForOrdering='Y'/>");
	// doc.renameNode(doc.getDocumentElement(), doc.getNamespaceURI(), "Xyz");
	// YRCXmlUtils.getString(doc);
	// //doc.renameNode(doc.getDocumentElement()., namespaceURI, qualifiedName)
	//			
	// }

	/**
	 * This method initializes the behavior class.
	 */
	public void init() {
		// : Write behavior init here.

	}

	public String getExtnNextPage(String currentPageId) {
//		System.out.println("getExtnNextPage()");
		return null;
	}

	public IYRCComposite createPage(String pageIdToBeShown) {
//		System.out.println("createPage()");
		IYRCComposite page= null;
		if (pageIdToBeShown.equalsIgnoreCase(OrderLinesPanel.FORM_ID)) {
			page = new OrderLinesPanel(this.getOwnerForm(), SWT.NONE, 
						new YRCEditorInput(getModel("OrderDetails"), this.getInputObject(), new String[] {}, ((YRCEditorInput)this.getInputObject()).getTaskName()), 
						pageIdToBeShown);
		}
		return page;
	}

	public void pageBeingDisposed(String pageToBeDisposed) {
//		System.out.println("pageBeingDisposed()");
	}

	/**
	 * Called when a wizard page is about to be shown for the first time.
	 * 
	 */
	public void initPage(String pageBeingShown) {
//		System.out.println("initPage()");
	}

	/**
	 * Method for validating the text box.
	 */
	public YRCValidationResponse validateTextField(String fieldName, String fieldValue) {
		// Validation required for the following controls.
//		System.out.println("validateTextField()");
		YRCValidationResponse validationResponse = null;
//		if ("extn_txtShipDate".equals(fieldName)) {
//			validationResponse = new YRCValidationResponse(YRCValidationResponse.YRC_VALIDATION_ERROR, "Invalid date provided.");
//			// this.getFieldValue("extn_txtShipDate");
//			// setFieldInError("extn_txtShipDate", validationResponse);
//
//			return validationResponse;
//		}
		// Create and return a response.
		if (validationResponse == null)
			return super.validateTextField(fieldName, fieldValue);
		else
			return validationResponse;
	}

	/**
	 * Method for validating the combo box entry.
	 */
	public void validateComboField(String fieldName, String fieldValue) {
		// TODO Validation required for the following controls.
//		System.out.println("validateComboField()");
		// TODO Create and return a response.
		super.validateComboField(fieldName, fieldValue);
	}

	/**
	 * Method called when a button is clicked.
	 */
	public YRCValidationResponse validateButtonClick(String fieldName) {
		// Validation required for the following controls.
//		System.out.println("validateButtonClick()");
		if ("extn_btnOrderLines".equals(fieldName)) {
			YRCPlatformUI.fireAction("com.xpedx.sterling.rcp.pca.orderlines.action.XPXShowOrderLinesAction");
		}
		if ("extn_btnSavePage".equals(fieldName)) {
			YRCPlatformUI.fireAction("com.xpedx.sterling.rcp.pca.orderlines.action.XPXUpdateOrderAction");
		}
		if ("extn_btnMarkComplete".equals(fieldName)) {
			YRCPlatformUI.fireAction("com.xpedx.sterling.rcp.pca.ordersummary.action.XPXMarkOrderCompleteAction");
		}
		if ("extn_btnDateLookup".equals(fieldName)) {
			String extn_txtShipDate = YRCPlatformUI.showCalendar();
			try {
				setFieldValue("extn_txtShipDate", YRCDateTimeUtils.getFormattedDate(YRCDateTimeUtils.getDate(extn_txtShipDate)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		

		return new YRCValidationResponse(YRCValidationResponse.YRC_VALIDATION_OK, "Invalid date provided.");
	}

	/**
	 * Method called when a link is clicked.
	 */
	public YRCValidationResponse validateLinkClick(String fieldName) {
		// TODO Validation required for the following controls.
//		System.out.println("validateLinkClick()");
		// TODO Create and return a response.
		return super.validateLinkClick(fieldName);
	}

	/**
	 * Create and return the binding data for advanced table columns added to
	 * the tables.
	 */
	public YRCExtendedTableBindingData getExtendedTableBindingData(String tableName, ArrayList tableColumnNames) {
		// Create and return the binding data definition for the table.
//		System.out.println("getExtendedTableBindingData()");
		// The defualt super implementation does nothing.
		return super.getExtendedTableBindingData(tableName, tableColumnNames);
	}

	public void postCommand(YRCApiContext apiContext) {
//		System.out.println("postCommand()");
		String[] apinames = apiContext.getApiNames();
		for (int i = 0; i < apinames.length; i++) {
			String apiname = apinames[i];
			if ("getSalesOrderDetails".equals(apiname)) {
				
				if(null == eleInOrderXML){
					eleInOrderXML = apiContext.getInputXmls()[i].getDocumentElement();
				}
				Document docOutOrder = apiContext.getOutputXmls()[i];
				Element eleOutOrder = docOutOrder.getDocumentElement();
//				Element eleError = YRCXmlUtils.createChild(eleOutOrder, "Error");
//				eleError.setAttribute("ShipDateErrorText", "Ship Date must be a valid date (today or later).");
//				eleError.setAttribute("CustomerPONoErrorText", "Just checking.");
				if(repopulate){
					Element eleOrderDetails = getModel("OrderDetails");
					this.removeChildNodesAndAttributes(eleOrderDetails);
					YRCXmlUtils.mergeElement(eleOutOrder, eleOrderDetails, false);
					repopulateModel("OrderDetails");
					setDirty(false);
					eleOutOrder = eleOrderDetails;
				}
				setFieldValue("extn_txtHdrComments", "");
				setFieldValue("extn_txtInternalComments", "");
				if(YRCPlatformUI.isVoid(YRCXmlUtils.getAttributeValue(eleOutOrder, "/Order/Error/@CustomerCommsntErrorText"))){
					setControlVisible("extn_acceptCustComment", false);
				}
//				if(YRCPlatformUI.isVoid(YRCXmlUtils.getAttributeValue(eleOutOrder, "/Order/Error/@PriceOverrideErrorText"))){
//					setControlVisible("extn_acceptPriceOverride", false);
//				}
				if(YRCPlatformUI.isVoid(YRCXmlUtils.getAttributeValue(eleOutOrder, "/Order/Error/@CustomerPONoErrorText"))){
					setControlVisible("extn_acceptDupPONo", false);
				}
				if(YRCPlatformUI.isVoid(YRCXmlUtils.getAttributeValue(eleOutOrder, "/Order/Error/@ShipCompleteErrorText"))){
					setControlVisible("extn_acceptShipComplete", false);
				}
				if(YRCPlatformUI.isVoid(YRCXmlUtils.getAttributeValue(eleOutOrder, "/Order/Error/@AcceptNoNextBusinessDayErrorText"))){
					setControlVisible("extn_acceptNoNextBusinessDay", false);
				}
				if(YRCPlatformUI.isVoid(YRCXmlUtils.getAttributeValue(eleOutOrder, "/Order/Error/@PreventAutoOrdPlacementErrorText"))){
					setControlVisible("extn_preventAutoOrdPlacement", false);
				}
				if(YRCPlatformUI.isVoid(YRCXmlUtils.getAttributeValue(eleOutOrder, "/Order/Error/@ShipMethodErrorText"))){
					setControlVisible("extn_nonStandardShipMethod", false);
				}
				if(YRCPlatformUI.isVoid(YRCXmlUtils.getAttributeValue(eleOutOrder, "/Order/Error/@ShipToZipCodeErrorText"))){
					setControlVisible("extn_validateShiptoZipCode", false);
				}
				if(YRCPlatformUI.isVoid(YRCXmlUtils.getAttributeValue(eleOutOrder, "/Order/Error/@ReqDeliveryDateErrorText"))){
					setControlVisible("extn_AcceptRequestDeleveryDateDoNotMatch", false);
				}
				repopulate = false;
			}
			if("XPXChangeOrderDetailsService".equals(apiname) || "XPXMarkOrderToCompleteService".equals(apiname)){
				if(!"Errors".trim().equals(apiContext.getOutputXmls()[i].getDocumentElement().getNodeName())){
//					System.out.println("Saved...");
					repopulate = true;
					YRCPlatformUI.trace("eleInOrderXML", YRCXmlUtils.getString(eleInOrderXML));
					YRCApiContext apiConterxt = new YRCApiContext();
					apiConterxt.setApiName("getSalesOrderDetails");
					apiConterxt.setInputXml(eleInOrderXML.getOwnerDocument());
					apiConterxt.setFormId("com.yantra.pca.ycd.rcp.tasks.orderSummary.wizards.YCDOrderSummaryWizard");
					callApi(apiConterxt);
				}
			}
		}
		super.postCommand(apiContext);
	}

	private void removeChildNodesAndAttributes(Element ele) {
		this.removeChildNodes(ele);
		this.removeAttributes(ele);
	}

	/**
	 * @param element
	 */
	private void removeChildNodes(Element element) {
		if (element != null && element.hasChildNodes()) {
			NodeList childList = element.getChildNodes();
			for (int i=0; i<childList.getLength(); i++) {
				element.removeChild(childList.item(i));
			}
		}
	}
	
	/**
	 * @param element
	 */
	private static void removeAttributes(Element element) {
	    /* Remove all the attributes of an element. Just to be safe, make a copy of
	     * attr names and then delete one by one.
	     */
	    NamedNodeMap attrs = element.getAttributes();
	    String[] names = new String[attrs.getLength()];
	    for (int i=0; i<names.length; i++) {
	        names[i] = attrs.item(i).getNodeName();
	    }
	    for (int i=0; i<names.length; i++) {
	        attrs.removeNamedItem(names[i]);
	    }
	}

	@Override
	public boolean preCommand(YRCApiContext apiContext) {
//		System.out.println("preCommand()");
		String[] apinames = apiContext.getApiNames();
		for (int i = 0; i < apinames.length; i++) {
			String apiname = apinames[i];
			if ("getSalesOrderDetails".equals(apiname)) {
				Document docInOrderXML = apiContext.getInputXmls()[i];
				eleInOrderXML = docInOrderXML.getDocumentElement();
			}
		}
		return super.preCommand(apiContext);
	}

	@Override
	public void postSetModel(String namespace) {
//		System.out.println("postSetModel()" + namespace);

		if (namespace.equals("OrderDetails")) {
			try {
				setExtentionModel("extnFailedRules", YRCXmlUtils.createFromString(
						"<Errors>" + "<Error AttributeXPath='1' ErrorMessage='Ship Date must be a valid date (today or later).'/>" + "<Error AttributeXPath='2' ErrorMessage='Just checking.'/>" + "</Errors>").getDocumentElement());
				// setControlVisible("extn_errPONumber", false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.postSetModel(namespace);
	}

	public void updateOrderAction() {
		Element eleTargetModel = getTargetModel("SaveOrder");
		appendDashBoardOverride(eleTargetModel);
		YRCXmlUtils.setAttributeValue(eleTargetModel, "/Order/@OrderHeaderKey", YRCXmlUtils.getAttributeValue(getModel("OrderDetails"), "/Order/@OrderHeaderKey"));
		YRCPlatformUI.trace("SaveOrder", YRCXmlUtils.getString(eleTargetModel));
		YRCApiContext apiConterxt = new YRCApiContext();
		apiConterxt.setApiName("XPXChangeOrderDetailsService");
		apiConterxt.setInputXml(eleTargetModel.getOwnerDocument());
		apiConterxt.setFormId("com.yantra.pca.ycd.rcp.tasks.orderSummary.wizards.YCDOrderSummaryWizard");
		callApi(apiConterxt);
	}

	public void markOrderComplete() {
		Document docTargetModel = YRCXmlUtils.createDocument("Order");
		docTargetModel.getDocumentElement().setAttribute("OrderHeaderKey", YRCXmlUtils.getAttributeValue(getModel("OrderDetails"), "/Order/@OrderHeaderKey")); 
		YRCPlatformUI.trace("Mark Order Complete:", YRCXmlUtils.getString(docTargetModel));
		YRCApiContext apiConterxt = new YRCApiContext();
		apiConterxt.setApiName("XPXMarkOrderToCompleteService");
		apiConterxt.setInputXml(docTargetModel);
		apiConterxt.setFormId("com.yantra.pca.ycd.rcp.tasks.orderSummary.wizards.YCDOrderSummaryWizard");
		callApi(apiConterxt);
	}
	
	public void appendDashBoardOverride(Element eleTargetModel){
		HashMap<String,String>  overrideMap = new HashMap<String,String>();
		overrideMap.put("extn_acceptDupPONo", "DuplicatePO");
		overrideMap.put("extn_acceptShipComplete", "CustomerSelectedShipComplete");
		overrideMap.put("extn_acceptNoNextBusinessDay", "ShipDateNotNextBusinessDay");
		overrideMap.put("extn_preventAutoOrdPlacement", "PreventAutoPlace");
		overrideMap.put("extn_nonStandardShipMethod", "NonStandardShipMethod");
		overrideMap.put("extn_validateShiptoZipCode", "ValidateShiptoZipCode");
		overrideMap.put("extn_AcceptRequestDeleveryDateDoNotMatch", "AllDeliveryDatesDoNotMatch");
		
		for(Entry<String,String> entry:overrideMap.entrySet()){
			if("Y".equals(getFieldValue(entry.getKey()))){
				addDashBoardElement(eleTargetModel,entry.getValue());
			}
		}		
	}
	public void addDashBoardElement(Element eleTargetModel,String ruleId ){
		Element dashBoardElem = null;
		Element dashBoardListElem= null;
		Element extnElem = null;
		if(null==dashBoardListElem){
			extnElem = YRCXmlUtils.getChildElement(eleTargetModel, "Extn", true);
			dashBoardListElem = YRCXmlUtils.getChildElement(extnElem, "XPXDashboardOverrideList", true);
		}
		dashBoardElem = YRCXmlUtils.getChildElement(dashBoardListElem, "XPXDashboardOverride", true);
		dashBoardElem.setAttribute("OrderHeaderKey", YRCXmlUtils.getAttributeValue(getModel("OrderDetails"), "/Order/@OrderHeaderKey"));
		dashBoardElem.setAttribute("OrderLineKey", "");
		dashBoardElem.setAttribute("OverrideFlag", "Y");
		dashBoardElem.setAttribute("RuleId",ruleId);
	}

}
//TODO Validation required for a Button control: extn_btnSave
//TODO Validation required for a Check control: extn_comboShipComplete
//TODO Validation required for a Text control: extn_txtHdrComments
//TODO Validation required for a Check control: extn_acceptShipComplete
//TODO Validation required for a Button control: extn_btnOrderLines
//TODO Validation required for a Combo control: extn_comboOrdCharges
//TODO Validation required for a Text control: extn_txtAttentionName
//TODO Validation required for a Text control: extn_txtPONumber
//TODO Validation required for a Button control: extn_btnSavePage
//TODO Validation required for a Button control: extn_btnMarkComplete
//TODO Validation required for a Text control: extn_txtShipDate
//TODO Validation required for a Check control: extn_nonStandardShipMethod
//TODO Validation required for a Combo control: extn_comboShipMethod
//TODO Validation required for a Text control: extn_txtWillCall
//TODO Validation required for a Button control: extn_btnDateLookup
//TODO Validation required for a Check control: extn_acceptNoNextBusinessDay
//TODO Validation required for a Check control: extn_checkDeliveryHold
//TODO Validation required for a Check control: extn_acceptCustComment
//TODO Validation required for a Check control: extn_AcceptRequestDeleveryDateDoNotMatch
//TODO Validation required for a Check control: extn_validateShiptoZipCode
//TODO Validation required for a Check control: extn_acceptDupPONo
//TODO Validation required for a Check control: extn_preventAutoOrdPlacement
	
package com.xpedx.sterling.rcp.pca.ordersearch.extn;

/**
 * Created on Mar 04,2010
 *
 */
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCTableColumnTextProvider;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCExtendedTableBindingData;
import com.yantra.yfc.rcp.YRCExtendedTableImageProvider;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCValidationResponse;
import com.yantra.yfc.rcp.YRCWizardExtensionBehavior;
import com.yantra.yfc.rcp.YRCXmlUtils;
/**
 * @author sdodda
 * Copyright © 2005-2009 Sterling Commerce, Inc. All Rights Reserved.
 */
 public class OrderSearchWizardExtnBehaviour extends YRCWizardExtensionBehavior {

	/**
	 * This method initializes the behavior class.
	 */
	public void init() {
//		System.out.println("init()");
		YRCApiContext apiConterxt = new YRCApiContext();
		apiConterxt.setApiName("XPXGetStatusList");
		apiConterxt.setInputXml(YRCXmlUtils.createFromString("<CommonCode CallingOrganizationCode='xpedx' CodeLongDescription='GEN' CodeType='XpedxWCStatus' ><OrderBy><Attribute Name='CodeValue' Desc='N'/></OrderBy></CommonCode>"));
		apiConterxt.setFormId("com.yantra.pca.ycd.rcp.tasks.orderSearch.wizards.YCDOrderSearchWizard");
		callApi(apiConterxt);
		
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
    	YRCValidationResponse validationResponse = null;
		if("txtOrderNo".equals(fieldName)){
			validationResponse = new YRCValidationResponse(YRCValidationResponse.YRC_VALIDATION_ERROR,"Invalid data provided.");
        	this.getFieldValue("txtOrderNo");
        	setFieldInError("txtOrderNo", validationResponse);
		}
		// Create and return a response.
    	if(validationResponse==null)
    		return super.validateTextField(fieldName, fieldValue);
    	else
    		return validationResponse;
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
    	// TODO Validation required for the following controls.
		
		// TODO Create and return a response.
		return super.validateLinkClick(fieldName);
	}
	
	/**
	 * Create and return the binding data for advanced table columns added to the tables.
	 */
	 public YRCExtendedTableBindingData getExtendedTableBindingData(String tableName, ArrayList tableColumnNames) {
		
		//Create and return the binding data definition for the table.
		final YRCExtendedTableBindingData tblBindingData = new YRCExtendedTableBindingData(tableName);
		HashMap<String, YRCTblClmBindingData> bindingDataMap = new HashMap<String, YRCTblClmBindingData>();
		
		//Status Column
		final YRCTblClmBindingData sttsBindingData = new YRCTblClmBindingData();
		sttsBindingData.setName("extn_sttsClmn");
		sttsBindingData.setColumnBinding("S");
		sttsBindingData.setAttributeBinding("Status");
		sttsBindingData.setTooltipBinding("Status");
		sttsBindingData.setSortReqd(true);
		sttsBindingData.setLabelProvider(new IYRCTableColumnTextProvider(){
//        	@Override
			public String getColumnText(Element obj) {
				Element eleTableItem = (Element)obj;
				Element eleOrderHoldTypes = YRCXmlUtils.getChildElement(eleTableItem, "OrderHoldTypes");
				String orderStatus=YRCXmlUtils.getXPathElement(eleTableItem, "/Order").getAttribute("Status");
				List listOrderHold = YRCXmlUtils.getChildren(eleOrderHoldTypes, "OrderHoldType");
				
				String Status=null;
				boolean isPendingApproval = false;
				
				for (Object objOrderHold : listOrderHold) {
					Element eleOrderHold = (Element) objOrderHold;
					if("ORDER_LIMIT_APPROVAL".equals(eleOrderHold.getAttribute("HoldType"))){
								Status = YRCXmlUtils.getXPathElement(eleTableItem, "/Order").getAttribute("Status") + " (Pending Approval)";	
								isPendingApproval = true;
							}
					//Condition Added for JIRA 4326
					if(XPXConstants.ORDER_IN_EXCEPTION_HOLD.equals(eleOrderHold.getAttribute("HoldType"))|| XPXConstants.LEGACY_CNCL_ORD_HOLD.equals(eleOrderHold.getAttribute("HoldType"))|| XPXConstants.LEGACY_CNCL_LNE_HOLD.equals(eleOrderHold.getAttribute("HoldType"))|| XPXConstants.LEG_ERR_CODE_HOLD.equals(eleOrderHold.getAttribute("HoldType"))|| XPXConstants.NEEDS_ATTENTION.equals(eleOrderHold.getAttribute("HoldType"))){
						Status = YRCXmlUtils.getXPathElement(eleTableItem, "/Order").getAttribute("Status") + " (CSR Reviewing)";	
						System.out.println("the hold type element is:"+YRCXmlUtils.getString(eleOrderHold));
						isPendingApproval = true;
					}
					}
				
				if(!isPendingApproval){					
					Status = YRCXmlUtils.getXPathElement(eleTableItem, "/Order").getAttribute("Status");
				}
				
				
				return Status;
			}
			
			
        });
		
		bindingDataMap.put("extn_sttsClmn", sttsBindingData);
		
		//Status Column
		final YRCTblClmBindingData chngDateBindingData = new YRCTblClmBindingData();
		chngDateBindingData.setName("extn_chngDateClmn");
		chngDateBindingData.setColumnBinding("Last_Modified_Date");
		chngDateBindingData.setAttributeBinding("Modifyts");
		chngDateBindingData.setDataType("OrderDate");
		chngDateBindingData.setTooltipBinding("LastModifiedDate");
		chngDateBindingData.setSortReqd(true);
		
		bindingDataMap.put("extn_chngDateClmn", chngDateBindingData);
		
		
		
		final YRCTblClmBindingData legacyBindingData = new YRCTblClmBindingData();
		legacyBindingData.setName("extn_legacyOrderNo");
		legacyBindingData.setColumnBinding("Legacy_Order_Number");
		legacyBindingData.setAttributeBinding("Legacy_Order_Number");
		legacyBindingData.setLabelProvider(new IYRCTableColumnTextProvider(){
			//        	@Override
			public String getColumnText(Element obj) {
				Element eleTableItem = (Element)obj;
				String fmtLegacyOrderNumber = null;
				String divisionNo = YRCXmlUtils.getXPathElement(	eleTableItem, "/Order/Extn").getAttribute("ExtnOrderDivision");
				String wareHouseNo[] = divisionNo.split("_");
				String legacyNo = YRCXmlUtils.getXPathElement(eleTableItem, "/Order/Extn").getAttribute("ExtnLegacyOrderNo");
				if(legacyNo !=null && !"".equalsIgnoreCase(legacyNo)){
				String generationNo = YRCXmlUtils.getXPathElement(eleTableItem,
							"/Order/Extn").getAttribute("ExtnGenerationNo");
					

					fmtLegacyOrderNumber = XPXUtils.getFormattedOrderNumber(
							divisionNo, legacyNo, generationNo);
							
					}
				return fmtLegacyOrderNumber;
			}
			
			
        });
			
		bindingDataMap.put("extn_legacyOrderNo", legacyBindingData);
		
		
		final YRCTblClmBindingData createDateBindingData = new YRCTblClmBindingData();
		createDateBindingData.setName("extn_createDateClmn");
		createDateBindingData.setColumnBinding("Created_Date");
		createDateBindingData.setAttributeBinding("OrderDate");
		createDateBindingData.setDataType("OrderDate");
		createDateBindingData.setSortReqd(true);
		createDateBindingData.setTooltipBinding("Created Date");
		
		bindingDataMap.put("extn_createDateClmn", createDateBindingData);
		
		final YRCTblClmBindingData orderTotalBindingData = new YRCTblClmBindingData();
		orderTotalBindingData.setName("extn_orderTotal");
		orderTotalBindingData.setColumnBinding("lbl_Order_total");
		orderTotalBindingData.setAttributeBinding("Extn/@ExtnTotalOrderValue");		
		orderTotalBindingData.setTooltipBinding("OrderTotal");
		orderTotalBindingData.setLabelProvider(new IYRCTableColumnTextProvider(){
//        	@Override
			public String getColumnText(Element obj) {
				Element eleTableItem = (Element)obj;
				String orderTotal=YRCXmlUtils.getXPathElement(eleTableItem, "/Order/Extn").getAttribute("ExtnTotalOrderValue");
				String currencyCode = YRCXmlUtils.getXPathElement(eleTableItem, "/Order/PriceInfo").getAttribute("Currency");
				return YRCPlatformUI.getFormattedCurrency(orderTotal, currencyCode);
			}
        });
		
		bindingDataMap.put("extn_orderTotal", orderTotalBindingData);	
		
		final YRCTblClmBindingData orderTypeBindingData = new YRCTblClmBindingData();
		
		orderTypeBindingData.setName("extn_OrderType");
		orderTypeBindingData.setColumnBinding("Order_Type");
		orderTypeBindingData.setAttributeBinding("OrderType");
		orderTypeBindingData.setTooltipBinding("OrderType");
		orderTypeBindingData.setLabelProvider(new IYRCTableColumnTextProvider(){
			public String getColumnText(Element eleData) {
				String orderType=eleData.getAttribute("OrderType");
				if("Customer".equalsIgnoreCase(orderType)){
					return YRCPlatformUI.getString("Customer_Order");
				}
				else if(XPXUtils.isFullFillmentOrder(eleData)){
					return YRCPlatformUI.getString("Fulfilment_Order");
				}
				else 
					return orderType;
				
			}
		});
		bindingDataMap.put("extn_OrderType", orderTypeBindingData);
		orderTypeBindingData.setSortReqd(true);
		
				
		tblBindingData.setTableColumnBindingsMap(bindingDataMap);		
		
		tblBindingData.setImageProvider(new YRCExtendedTableImageProvider(){
			public String getImageThemeForColumn(Object object, String attribute) {
				boolean needsAttention = false;
				boolean willCallFlag = false;
				boolean rushOrderFlag = false;
				boolean lockedOrder = false;
				boolean extnOUFailureLockFlag = false;
				boolean orderLockFlag = false;
				Element inputEle = (Element) object;
				Element eleWillCall = YRCXmlUtils.getChildElement(inputEle, "Extn");
				Element eleHolds = YRCXmlUtils.getChildElement(inputEle, "OrderHoldTypes");
				//Added for JIRA 4323. CHange to pemanane lock orders. For testing i have done it for WEbHOLD ordres
				System.out.println("The extn fields are :"+ YRCXmlUtils.getString(eleWillCall));
				if("Y".equalsIgnoreCase(eleWillCall.getAttribute("ExtnOUFailureLockFlag"))){
					extnOUFailureLockFlag = true;
				}
				if("Y".equalsIgnoreCase(eleWillCall.getAttribute("ExtnOrderLockFlag"))){
					orderLockFlag = true;
				}
				
				if("Y".equalsIgnoreCase(eleWillCall.getAttribute("ExtnWillCall"))){
					willCallFlag = true;
				}else if("Y".equalsIgnoreCase(eleWillCall.getAttribute("ExtnRushOrderFlag"))){
					rushOrderFlag = true;
				}
				if(eleHolds!=null){
					Iterator itrHold = YRCXmlUtils.getChildren(eleHolds);
					while(itrHold.hasNext()) {
						Element eleHold = (Element) itrHold.next();
						//Customer Suspend case handled for JIRA 3929
						if (((XPXConstants.NEEDS_ATTENTION).equals(eleHold
								.getAttribute("HoldType")))
								&& "1100"
										.equals(eleHold.getAttribute("Status"))
								|| "M0007".equalsIgnoreCase(eleWillCall
										.getAttribute("ExtnHeaderStatusCode"))) {
							needsAttention = true;
							//Start- Added for 3391
						}
						if((XPXConstants.ORDER_IN_EXCEPTION_HOLD).equals(eleHold.getAttribute("HoldType"))){
							lockedOrder = true;
						}
					}
				}
				if(attribute.endsWith("Status")&& willCallFlag  ){
					return "WillCall";
				}
				else if(attribute.endsWith("Status")&& rushOrderFlag  ){
					return "RushOrder";
				}
				else if(attribute.endsWith("Status")&& needsAttention  ){
					return "NeedsAttention";
				}
				else if(attribute.endsWith("Status")&& lockedOrder  ){
					return "NeedsAttention";
				}
				//Added for JIRA 4323. CHange to pemanane lock orders. For testing i have done it for WEbHOLD ordres
				else if(attribute.endsWith("Status")&& extnOUFailureLockFlag  ){
					return "extnOUFailureLockFlag";
				}
				else if(attribute.endsWith("Status")&& orderLockFlag){
					return "orderLockFlag";
				}
				//End- Added for 3391
				else{
					return null;
				}
			}
		});
	 	// The defualt super implementation does nothing.
	 	return tblBindingData;
	 }
	 
	@Override
	public boolean preCommand(YRCApiContext apiContext) {
		if(apiContext.getApiName().equals("getOrderList")){
//			Get the Input XML to modify the API input
			Document docInput = apiContext.getInputXml();
			Element eleInput = docInput.getDocumentElement();
			
			//Added for jira 2540
			//eleInput.setAttribute("BillToID",getFieldValue("extn_txtMasterCustomer")) ;
			
//			Remove DraftOrderFlag attribute which is being defaulted by product.
			//eleInput.removeAttribute("DraftOrderFlag");
			eleInput.setAttribute("DraftOrderFlag","N");

			eleInput.removeAttribute("OrderNo");
			
//			If either FromStatus || ToStatus is sent append StatusQryType='BETWEEN'
			if(!YRCPlatformUI.isVoid(eleInput.getAttribute("FromStatus")) || !YRCPlatformUI.isVoid(eleInput.getAttribute("ToStatus"))){
				eleInput.setAttribute("StatusQryType", "BETWEEN");
			}
			
//			If Needs Attention Check box is checked then create a OrderHoldType element with holdtype ='NEEDS_ATTENTION'
			if("Y".equals(YRCXmlUtils.getAttributeValue(eleInput, "/Order/@NeedsAttentionFlag")) ||"Y".equals(YRCXmlUtils.getAttributeValue(eleInput, "/Order/@CsrReviewFlag")) ){
				Element eleOrderHoldType = YRCXmlUtils.createChild(eleInput, "OrderHoldType");
					eleOrderHoldType.setAttribute("Status", "1100");
					Element eleComplexQry1 = YRCXmlUtils.createChild(eleOrderHoldType, "ComplexQuery");
					Element eleOr1= YRCXmlUtils.createChild(eleComplexQry1, "Or");
//					If Needs Attention Check box is checked then create a OrderHoldType element with holdtype ='NEEDS_ATTENTION'					
					if("Y".equals(YRCXmlUtils.getAttributeValue(eleInput, "/Order/@NeedsAttentionFlag"))){
						Element eleExp1 = YRCXmlUtils.createChild(eleOr1, "Exp");
						eleExp1.setAttribute("Name", "HoldType");
						eleExp1.setAttribute("Value", XPXConstants.NEEDS_ATTENTION);
	
					}
//					If CSR Review Check box is checked then create a OrderHoldType element with holdtype ='ORDER_IN_EXCEPTION_HOLD'
					if("Y".equals(YRCXmlUtils.getAttributeValue(eleInput, "/Order/@CsrReviewFlag"))){
						Element eleExp2 = YRCXmlUtils.createChild(eleOr1, "Exp");
						eleExp2.setAttribute("Name", "HoldType");
						eleExp2.setAttribute("Value", XPXConstants.ORDER_IN_EXCEPTION_HOLD);

					}		
			}
		
				
			
//			Search by Order Type
			if(!YRCPlatformUI.isVoid(getFieldValue("extn_comboDocType"))){
				eleInput.removeAttribute("DocumentType");
				if("0001".equals(getFieldValue("extn_comboDocType"))){
					eleInput.setAttribute("DocumentType",getFieldValue("extn_comboDocType"));
				} else if("0001.CUSTOMER_ORDER".equals(getFieldValue("extn_comboDocType"))){
					eleInput.setAttribute("DocumentType","0001");
					eleInput.setAttribute("OrderType", XPXConstants.ORD_TYPE_CUSTOMER);
				} else if("0001.FULFILLMENT_ORDER".equals(getFieldValue("extn_comboDocType"))){
					eleInput.setAttribute("DocumentType","0001");
					
					Element eleComplexQry = YRCXmlUtils.createChild(eleInput, "ComplexQuery");
					Element eleOr = YRCXmlUtils.createChild(eleComplexQry, "Or");
					Element eleExp1 = YRCXmlUtils.createChild(eleOr, "Exp");
					eleExp1.setAttribute("Name", "OrderType");
					eleExp1.setAttribute("Value", XPXConstants.ORD_TYPE_FO_THIRD_PARTY_ORDER);
					Element eleExp2 = YRCXmlUtils.createChild(eleOr, "Exp");
					eleExp2.setAttribute("Name", "OrderType");
					eleExp2.setAttribute("Value", XPXConstants.ORD_TYPE_FO_DIRECT_ORDER);
					Element eleExp3 = YRCXmlUtils.createChild(eleOr, "Exp");
					eleExp3.setAttribute("Name", "OrderType");
					eleExp3.setAttribute("Value", XPXConstants.ORD_TYPE_FO_STOCK_ORDER);
					Element eleExp4 = YRCXmlUtils.createChild(eleOr, "Exp");
					eleExp4.setAttribute("Name", "OrderType");
					eleExp4.setAttribute("Value", XPXConstants.ORD_TYPE_FO_SPECIAL_ORDER);
				}
			}
			//Added for JIRA 3038
			Element eleExtn = YRCXmlUtils.getChildElement(eleInput, "Extn", true);
			//Element eleExtn = YRCXmlUtils.getXPathElement(eleInput, "/Order/Extn");
			if ("" != eleInput.getAttribute("FromStatus") || "" != eleInput.getAttribute("ToStatus") || null != eleInput.getAttribute("FromStatus")|| null != eleInput.getAttribute("ToStatus")){
					
				eleExtn.setAttribute("FromExtnOrderStatus",eleInput.getAttribute("FromStatus"));
				eleExtn.setAttribute("ToExtnOrderStatus",eleInput.getAttribute("ToStatus"));
				eleExtn.setAttribute("ExtnOrderStatusQryType","BETWEEN");
				}
			
			if(!YRCPlatformUI.isVoid(eleExtn)&& !YRCPlatformUI.isVoid(eleExtn.getAttribute("ExtnShipToName")))
			{
				eleExtn.setAttribute("ExtnShipToNameQryType", "LIKE");
			}
			if(!YRCPlatformUI.isVoid(eleInput)&& !YRCPlatformUI.isVoid(eleInput.getAttribute("CustomerPONo")))
			{
				eleInput.setAttribute("CustomerPONoQryType", "EQ");
			}
			//Add for JIRA 2540
			if(!YRCPlatformUI.isVoid(eleInput)&& !YRCPlatformUI.isVoid(eleInput.getAttribute("BillToID")))
			{
				eleInput.setAttribute("BillToIDQryType", "LIKE");
			}
			//Added for 3391
			if(!YRCPlatformUI.isVoid(eleExtn)&& !YRCPlatformUI.isVoid(eleExtn.getAttribute("ExtnCustomerDivision")))
			{
				eleExtn.setAttribute("ExtnCustomerDivisionQryType", "LIKE");
			}
			
			
			//if("9000".equalsIgnoreCase(eleInput.getAttribute("FromStatus")) || "9000".equalsIgnoreCase(eleInput.getAttribute("ToStatus"))){
				eleExtn.setAttribute("ExtnLegacyOrderType" , "F");
				eleExtn.setAttribute("ExtnLegacyOrderTypeQryType", "NE");
			//}
			System.out.println(YRCXmlUtils.getString(eleInput));
		}
		return super.preCommand(apiContext);
	}
	 public void postCommand(YRCApiContext apiContext){
		 if(apiContext.getApiName().equals("getOrderList")){
			Document doc = apiContext.getOutputXml();
			Element ele = doc.getDocumentElement();
			NodeList nl =ele.getElementsByTagName("Order");
			int noOfOrders = nl.getLength();
//			set DraftOrderFlag='N' when it is equal to 'Y', 
//			doing this because on double click of order row by seeing this flag 
//			Product is routing to AdvancedAddItems Screen(in case of Y) 
//			and OrderSummary Screen(in case of N) respectively, here we always route to OrderSummary Screen.
			for (int i = 0; i < noOfOrders; i++) {
				Element eleOrd =(Element)nl.item(i);
				Element eleExtn = YRCXmlUtils.getChildElement(eleOrd, "Extn", true);
				eleOrd.setAttribute("BillToID",eleExtn.getAttribute("ExtnCustomerNo")) ;
				if("Y".equals(eleOrd.getAttribute("DraftOrderFlag"))){
					eleOrd.setAttribute("DraftOrderFlag", "N");
				}
			}
			apiContext.setOutputXml(doc); 
			
		 }
//		if(apiContext.getApiName().equals("getOrderList")){
//			 Document doc = apiContext.getOutputXml();
//			 Element ele = doc.getDocumentElement();
//			 NodeList nl =ele.getElementsByTagName("Order");
//			 int noOfOrders = nl.getLength();
//			 for (int i = 0; i < noOfOrders; i++) {
//				Element eleOrd =(Element)nl.item(i);
//				
//				eleOrd.setAttribute("OrderName", "Standard");
//				eleOrd.setAttribute("BillToID", "ZIEGLER"+i);
//				if(eleOrd.getAttribute("Status").equals("Backordered")){
//					eleOrd.setAttribute("DisplayStatusDescription", "Completed");
//				} else {
//					eleOrd.setAttribute("DisplayStatusDescription", "Needs Attention");
//				}
//				Element priceInfoEle = (Element) eleOrd.getElementsByTagName("PriceInfo").item(0);
//				Element billToInfoEle = (Element) eleOrd.getElementsByTagName("PersonInfoBillTo").item(0);
//				
//				if(i==0){
//					eleOrd.setAttribute("CustomerFirstName", "xpedx-Colombus, OH - 32");
//					eleOrd.setAttribute("CustomerLastName", "3M");
//					priceInfoEle.setAttribute("TotalAmount", "500");
//					billToInfoEle.setAttribute("AddressLine1", "000001");
//					eleOrd.setAttribute("CustomerContactID", "Johnson, Jennifer");
//					
//				} else if(i==1){
//					eleOrd.setAttribute("CustomerFirstName", "xpedx-Dayton - 33");
//					eleOrd.setAttribute("CustomerLastName", "Cabelas - Omaha");
//					priceInfoEle.setAttribute("TotalAmount", "300");
//					billToInfoEle.setAttribute("AddressLine1", "000006");
//					eleOrd.setAttribute("CustomerContactID", "Whitlock, Brian");
//				} else {
//					eleOrd.setAttribute("CustomerFirstName", "xpedx-Denver - 30");
//					eleOrd.setAttribute("CustomerLastName", "Budco");
//					priceInfoEle.setAttribute("TotalAmount", "284");
////					billToInfoEle.setAttribute("AddressLine1", "000201");
//					eleOrd.setAttribute("CustomerContactID", "");
//				}
//			}
//			 apiContext.setOutputXml(doc);
//		 }
		 super.postCommand(apiContext);
	 }
	 public void postSetModel(String arg0) {
		System.out.println(arg0);
		// TODO Auto-generated method stub
		super.postSetModel(arg0);
	}
	 
	@Override
	public void handleApiCompletion(YRCApiContext apiContext) {
//		Setting Extn_StatusList extension model.
		if(apiContext.getApiName().equals("XPXGetStatusList")){
			Document docOutput = apiContext.getOutputXml();
			setExtentionModel("Extn_StatusList", docOutput.getDocumentElement());
			Element eleDocTypes = YRCXmlUtils.createDocument("DocumentTypes").getDocumentElement();
			Element rootElem = docOutput.getDocumentElement();
			NodeList commonCodeList=rootElem.getElementsByTagName("CommonCode");
			for(int i=0;i<commonCodeList.getLength();i++){
				Element commonCodeElem=(Element) commonCodeList.item(i);
				String codeValue = commonCodeElem.getAttribute("CodeValue");
				if (codeValue.equalsIgnoreCase("1100.5150" )){
					docOutput.getDocumentElement().removeChild(commonCodeElem);
					i--;
					}
				if (codeValue.equalsIgnoreCase("1100.5155" )){
					docOutput.getDocumentElement().removeChild(commonCodeElem);
				}
				
				}
			setExtentionModel("Extn_StatusList", docOutput.getDocumentElement());
			HashMap<String, String> mapDocTypes = new HashMap<String, String>();
			mapDocTypes.put("0001", "Sales Order");
			mapDocTypes.put("0001.CUSTOMER_ORDER", "Customer Order");
			mapDocTypes.put("0001.FULFILLMENT_ORDER", "Fulfillment Order");
			Set<java.util.Map.Entry<String, String>> setDocType = mapDocTypes.entrySet();
			for (Entry<String, String> entryDocType : setDocType) {
				Element eleDocType = YRCXmlUtils.createChild(eleDocTypes, "DocumentType");
				eleDocType.setAttribute("Type", entryDocType.getKey());
				eleDocType.setAttribute("Description", entryDocType.getValue());
			}
			setExtentionModel("Extn_DocumentTypes", eleDocTypes);
		 }
		super.handleApiCompletion(apiContext);
	}
}
//TODO Create and return the binding oldData definition for Table column: extn_sttsClmn
// refer to javadoc of method getExtendedTableBindingData(String , ArrayList) for more information.
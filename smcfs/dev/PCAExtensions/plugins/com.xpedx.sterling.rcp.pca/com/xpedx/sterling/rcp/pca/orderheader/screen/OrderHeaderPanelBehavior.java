package com.xpedx.sterling.rcp.pca.orderheader.screen;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.referenceorder.editor.XPXRefOrderEditor;
import com.xpedx.sterling.rcp.pca.util.TripleDES;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class OrderHeaderPanelBehavior extends YRCBehavior {

	private OrderHeaderPanel page;
	private Element inputElement;
	private Element eleOrderDetails;
	private String couponCode;
	private String customerContactId;
	private String pnaErrorValue;
	private String invoiceDate;
	private String invoiceNo;
	private String shipToId;
	private String LegacyOrderNumber;

	public OrderHeaderPanelBehavior(Composite ownerComposite, String formId, Object inputObject, Element eleOrderDetails) {
        super(ownerComposite, formId, inputObject);
        this.page = (OrderHeaderPanel) ownerComposite;
        this.eleOrderDetails = eleOrderDetails;
        this.inputElement = ((YRCEditorInput) inputObject).getXml();
        setModel("ChargesList",page.getOrderLinesPanel().getPageBehavior().getChargesList());
        setModel("TransferCirclesList",page.getOrderLinesPanel().getPageBehavior().getLocalModel("TransferCirclesList"));
        setModel("OrderDetails",eleOrderDetails);
        customerContactId= eleOrderDetails.getAttribute("CustomerContactID");
        setLegacyOrderNo(eleOrderDetails);
        shipToId = eleOrderDetails.getAttribute("BuyerOrganizationCode");
		Element eleExtn = YRCXmlUtils.getChildElement((Element)eleOrderDetails, "Extn");
		invoiceDate = eleExtn.getAttribute("ExtnInvoicedDate");
		invoiceNo = eleExtn.getAttribute("ExtnInvoiceNo");
		
       this.pnaErrorValue= page.getOrderLinesPanel().getPageBehavior().getPnAErrorValue();
       setFieldValue("lblHeaderErr", pnaErrorValue);
        getCustomerContactDetails();
        if(eleOrderDetails.getAttribute("EntryType").equals("B2B"))
		{
			setControlVisible("btnViewOriginal", true);
		}
        this.couponCode = YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Promotions/Promotion/@PromotionId");
        if(!YRCPlatformUI.isVoid(this.couponCode)){
        	setFieldValue("txtCouponCode", this.couponCode);
        	setControlEditable("txtCouponCode", false);
        }
        else{
        	setControlEditable("txtCouponCode", true);
        }
        //setModel("selectedCustomer",page.getOrderLinesPanel().getPageBehavior().getCustomerDetails());
        setDirty(false);
    }
	public Element getInputElement(){
		return this.inputElement;
	}
	
	private void getCustomerContactDetails() {
		
		String apinames = "getCustomerContactList";
		if(!YRCPlatformUI.isVoid(customerContactId)){
			Document docInput = YRCXmlUtils.createFromString("<CustomerContact CustomerContactID='"+customerContactId+"'/>");
			YRCApiContext ctx = new YRCApiContext();
			ctx.setFormId("com.xpedx.sterling.rcp.pca.orderheader.screen.OrderHeaderPanel");
			ctx.setApiName(apinames);
			ctx.setInputXml(docInput);
			if (!page.isDisposed())
				callApi(ctx, page);
		}
		
	}
	
	public Element getTargetModelforParent()
	{
		 Element eleSaveOrder = this.getTargetModel("SaveOrder");
		 eleSaveOrder.setAttribute("OrderHeaderKey", eleOrderDetails.getAttribute("OrderHeaderKey"));

		 Element eleSaveOrderExtn = YRCXmlUtils.getChildElement(eleSaveOrder, "Extn", true);
		 Element eleActualOrderExtn =  YRCXmlUtils.getChildElement(eleOrderDetails, "Extn");
		 eleSaveOrderExtn.setAttribute("ExtnLegacyOrderNo", eleActualOrderExtn.getAttribute("ExtnLegacyOrderNo"));
		 eleSaveOrderExtn.setAttribute("ExtnEnvtId", eleActualOrderExtn.getAttribute("ExtnEnvtId"));
		 if(!YRCPlatformUI.equals(page.getOldHdrComments(), getFieldValue("txtHdrComments"))){
			 String strHdrInstructionKey = (String)YRCXPathUtils.evaluate(this.eleOrderDetails, "/Order/Instructions/Instruction[@InstructionType='HEADER']/@InstructionDetailKey", XPathConstants.STRING);
			 XPXUtils.addCommentElement(eleSaveOrder,getFieldValue("txtHdrComments"),"HEADER",strHdrInstructionKey);
		 }
		 if(!YRCPlatformUI.equals(page.getOldInternalComments(), getFieldValue("txtInternalComments"))){
			 String strInternalInstructionKey = (String)YRCXPathUtils.evaluate(this.eleOrderDetails, "/Order/Instructions/Instruction[@InstructionType='INTERNAL']/@InstructionDetailKey", XPathConstants.STRING);
			 XPXUtils.addCommentElement(eleSaveOrder,getFieldValue("txtInternalComments"),"INTERNAL",strInternalInstructionKey);
		 }
		 if(!YRCPlatformUI.isVoid(getFieldValue("txtCouponCode")) && !YRCPlatformUI.equals(this.couponCode, getFieldValue("txtCouponCode"))){
			 Element elePromotions = YRCXmlUtils.createChild(eleSaveOrder, "Promotions");
			 Element elePromotion = YRCXmlUtils.createChild(elePromotions, "Promotion");
			 elePromotion.setAttribute("Action", "CREATE");
			 elePromotion.setAttribute("PromotionId", getFieldValue("txtCouponCode"));
		 }
		 
		 appendDashBoardOverride(eleSaveOrder, eleOrderDetails.getAttribute("OrderHeaderKey"));
//		 System.out.println(YRCXmlUtils.getString(eleSaveOrder));
		return eleSaveOrder; 
	}
	
	public void appendDashBoardOverride(Element eleOrder, String orderHeaderkey){
		Element eleExtn = YRCXmlUtils.getChildElement(eleOrder, "Extn", true);
		Element eleDashboardOvrridList = YRCXmlUtils.getChildElement(eleExtn, "XPXDashboardOverrideList", true);
		HashMap<String,String>  overrideMap = new HashMap<String,String>();
		//overrideMap.put("chkAcceptLineComments", "PreventBackOrder");
		overrideMap.put("chkAcceptDupCustPONo", "DuplicatePO");
		overrideMap.put("chkAcceptNoNextBusinessDay", "ShipDateNotNextBusinessDay");
		overrideMap.put("chkAcceptReqDlvryDate", "AllDeliveryDatesDoNotMatch");
		overrideMap.put("chkAcceptNonStrdShipMethod", "NonStandardShipMethod");
		overrideMap.put("chkAcceptShipComplete", "CustomerSelectedShipComplete");
		overrideMap.put("chkPreventAutoOrdPlace", "PreventAutoPlace");
		overrideMap.put("chkAcceptShipToZipCode", "ValidShiptoZipCode");
		overrideMap.put("chkAcceptHeaderComments", "HeaderCommentByCustomer");
		overrideMap.put("chkPreventBackOrder", "PreventBackOrder");
		
		for(Entry<String,String> entry:overrideMap.entrySet()){
			if("Y".equals(getFieldValue(entry.getKey()))){
				addDashBoardElement(eleDashboardOvrridList,entry.getValue(),orderHeaderkey);
			}
		}		
	}
	public void addDashBoardElement(Element dashBoardListElem,String ruleId,String orderHeaderkey){
		Element dashBoardElem = (Element) YRCXPathUtils.evaluate( dashBoardListElem,
				"/XPXDashboardOverrideList/XPXDashboardOverride[@RuleId='"+ruleId+"']",
				XPathConstants.NODE);
		if(null == dashBoardElem)
			dashBoardElem = YRCXmlUtils.createChild(dashBoardListElem, "XPXDashboardOverride");
		dashBoardElem.setAttribute("OrderHeaderKey", orderHeaderkey);
//		dashBoardElem.setAttribute("OrderLineKey", page.getOrderLineKey());
		dashBoardElem.setAttribute("OverrideFlag", "Y");
		dashBoardElem.setAttribute("RuleId",ruleId);
	}
	

	@Override
	public void handleApiCompletion(YRCApiContext ctx) {
		if(ctx.getInvokeAPIStatus() < 1)
        {
            YRCPlatformUI.trace((new StringBuilder()).append("API exception in ").append(ctx.getFormId()).append(" page, ApiName ").append(ctx.getApiName()).append(",Exception : ").toString(), ctx.getException());
        } else if(page.isDisposed())
            YRCPlatformUI.trace((new StringBuilder()).append(ctx.getFormId()).append(" page is disposed, ApiName ").append(ctx.getApiName()).append(",Exception : ").toString());
        else {
//        	if(YRCPlatformUI.equals(ctx.getApiName(), "getXPXReferenceOrderListService"))
//        	{
//	        	this.setRefernceList(ctx);
//	        }
        	if(YRCPlatformUI.equals(ctx.getApiName(), "getCustomerContactList"))
        	{
				Element eleCustomerContactList = ctx.getOutputXml().getDocumentElement();
				Element eleCustomerContact = YRCXmlUtils.getXPathElement(eleCustomerContactList, "/CustomerContactList/CustomerContact");
				setModel("CustomerContactDetails",eleCustomerContact);
				setOrderedByName(eleCustomerContact);
				setLegacyOrderNo(eleCustomerContact);
				
	        }        	
       }
		
	}
	private void setOrderedByName(Element eleCustomerContact) {
		Element referenceElement = getModel("OrderDetails");
		if(YRCPlatformUI.isVoid(YRCXmlUtils.getXPathElement(referenceElement, "/Order/Extn").getAttribute("ExtnOrderedByName")))
		{
			StringBuffer sb= new StringBuffer();
			sb.append(eleCustomerContact.getAttribute("FirstName"));
			if(!YRCPlatformUI.isVoid(eleCustomerContact.getAttribute("FirstName")))
			sb.append(", ");
			sb.append(eleCustomerContact.getAttribute("LastName"));
			YRCXmlUtils.getXPathElement(referenceElement, "/Order/Extn").setAttribute("ExtnOrderedByName",sb.toString());
		}
		setModel("OrderDetails",referenceElement);
	}
	
	/**
	 * Code for setting LegacyOrderNumber.
	 * Created Utility method for Formatting LegacyOrderNumber.
	 *  
	 * @param eleCustomerContact
	 */
	private void setLegacyOrderNo(Element eleCustomerContact){
		Element referenceElement = getModel("OrderDetails");
		String divisionNo = YRCXmlUtils.getXPathElement(referenceElement, "/Order/Extn").getAttribute("ExtnOrderDivision");
		String wareHouseNo[] = divisionNo.split("_");
		String legacyNo = YRCXmlUtils.getXPathElement(referenceElement, "/Order/Extn").getAttribute("ExtnLegacyOrderNo");
		if(legacyNo !=null && !"".equalsIgnoreCase(legacyNo)){
		String generationNo = YRCXmlUtils.getXPathElement(referenceElement,
					"/Order/Extn").getAttribute("ExtnGenerationNo");
			LegacyOrderNumber = wareHouseNo[0].concat(legacyNo).concat(
					generationNo);

			String fmtLegacyOrderNumber = XPXUtils.getFormattedOrderNumber(
					divisionNo, legacyNo, generationNo);
			System.out.println("fmtLegacyOrderNumber : " + fmtLegacyOrderNumber
					+ " , divisionNo: " + divisionNo + " , legacyNo: "
					+ legacyNo + " , generationNo: " + generationNo);

			setFieldValue("txtOrderNo", YRCPlatformUI.getFormattedString(
					"xpedx_OrderNo_key", fmtLegacyOrderNumber));
			}
		
		Element eleOrderHoldTypes = YRCXmlUtils.getChildElement(referenceElement, "OrderHoldTypes");
		List listOrderHold = YRCXmlUtils.getChildren(eleOrderHoldTypes, "OrderHoldType");
		
		for (Object objOrderHold : listOrderHold) {
			Element eleOrderHold = (Element) objOrderHold;
			if("ORDER_LIMIT_APPROVAL".equals(eleOrderHold.getAttribute("HoldType"))){
					String status = YRCXmlUtils.getXPathElement(referenceElement, "/Order").getAttribute("Status") + " (Pending Approval)";				
					YRCXmlUtils.getXPathElement(referenceElement, "/Order").setAttribute("Status", status);
				}
			}
		}
	
	public void viewOriginal()
    {
        Element eReferenceList = page.getOrderLinesPanel().getPageBehavior().getLocalModel("customerOriginalB2BOrder");
        Element eleSelected = (Element) YRCXPathUtils.evaluate(eReferenceList, "./XPXRefOrderHdr", XPathConstants.NODE);
        Element eleEditorInput = YRCXmlUtils.getCopy(eleSelected, true);
        YRCPlatformUI.openEditor(XPXRefOrderEditor.ID_EDITOR, new YRCEditorInput(eleEditorInput, new String[] {"RefOrderHdrKey"}, eleSelected.getAttribute("RefOrderHdrKey")));
    }
	
	protected void setModel(String namespace, Element inputElem) {
		super.setModel(namespace, inputElem);
		if("OrderDetails".equals(namespace)){
			String shipComplete = YRCXmlUtils.getAttributeValue(inputElem, "/Order/Extn/@ExtnShipComplete");
	        if(YRCPlatformUI.isVoid(shipComplete) && "C".equals(page.getOrderLinesPanel().getPageBehavior().getShipCompleteFlag())){
	        	Button chkShipComplete = ((Button)getControl("chkShipComplete"));
	        	if(chkShipComplete!=null)
	        		chkShipComplete.setSelection(true);
	        }
		}
	}
	
	public void openUrl()
    {
        String url = YRCPlatformUI.getYfsSystemProperty("xpedx.invoicing.url");        
        TripleDES tripleDes = new TripleDES();
        String encryptedContactId = null;
        String encryptedShipToId = null;
        String encryptedInvoiceNo = null;
        String encryptInvoiceDate = null;
        String URLEncodedContactId = null;
        String URLEncodedShipToId = null;
        String URLEncodedInvoiceNo = null;
        String encryptedInvoiceDate = null;
        String URLEncodedInvoiceDate = null;
        try {
            //condition to check URL Attributes 
            if(customerContactId !=null && customerContactId != "") {                
                encryptedContactId = tripleDes.encrypt(customerContactId);
                URLEncodedContactId = URLEncoder.encode(encryptedContactId);
            }else{
                YRCPlatformUI.showError("Invoice_user",YRCPlatformUI.getString("Invoice_user"));
                return;
            }
            
            if(shipToId !=null && shipToId != "") {                
                encryptedShipToId = tripleDes.encrypt(shipToId);
                URLEncodedShipToId = URLEncoder.encode(encryptedShipToId);
            }else{
                YRCPlatformUI.showError("Invoice_shiptoId",YRCPlatformUI.getString("Invoice_shiptoId"));
                return;
            }
            
            if(invoiceNo !=null && invoiceNo != "") {                
                encryptedInvoiceNo = tripleDes.encrypt(invoiceNo);
                URLEncodedInvoiceNo = URLEncoder.encode(encryptedShipToId);
            }else{
                YRCPlatformUI.showError("Invoice_invoiceno",YRCPlatformUI.getString("Invoice_invoiceno"));
                return;
            }
            
            if(invoiceDate !=null && invoiceDate != "") {                
                encryptedInvoiceDate = tripleDes.encrypt(invoiceDate);
                URLEncodedInvoiceDate = URLEncoder.encode(encryptedInvoiceDate);
            }else{
                YRCPlatformUI.showError("Invoice_invoicedate",YRCPlatformUI.getString("Invoice_invoicedate"));
                return;
            }
            String finalQueryString = "UserID="+URLEncodedContactId + "&InvoiceNo="+URLEncodedInvoiceNo+ "&shipTo="+URLEncodedShipToId+"&InvoiceDate="+URLEncodedInvoiceDate;
            XPXUtils.accessURL(url, finalQueryString);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }


    }

}

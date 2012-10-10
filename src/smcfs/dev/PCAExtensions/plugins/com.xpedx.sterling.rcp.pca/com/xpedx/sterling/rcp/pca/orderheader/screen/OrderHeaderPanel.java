package com.xpedx.sterling.rcp.pca.orderheader.screen;

import java.util.List;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.orderlines.screen.OrderLinesPanel;
import com.xpedx.sterling.rcp.pca.util.XPXCacheManager;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCButtonBindingData;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCLinkBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTextBindingData;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class OrderHeaderPanel extends Composite implements IYRCComposite {

	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.orderheader.screen.OrderHeaderPanel";
	private Object inputObject;
	private Element eleOrderDetails;
	private Composite pnlRoot;
	private Composite compositeStatusHold;
	private Label lblWebConfirmationNo; 
	public Text txtWebConfirmationNo;
	private SelectionAdapter selectionAdapter;
	private Label lblOrderNo;
    private Text txtOrderNo;
    private Label lblOrderStatus;
    private Text txtOrderStatus;
    private Label lblOrderDate;
    private Text txtOrderDate;
    private Label lblOrderTime;
    private Text txtOrderTime;
    private Label lblTotalAmount;
    private Text txtTotalAmount;
    private Label lblShippedValue;
    private Text txtShippedValue;
    private Label lblETradingID;
    private Text txtETradingID;
    
    private Label lblCustPONo;
    private Text txtCustPONo;
    private Button chkAcceptDupCustPONo;
    private Label lblShipDate;
    private Text txtShipDate;
    private Button chkAcceptNoNextBusinessDay;
    private Label lblErrAcceptNoNextBusinessDay;
    private Label lblErrAcceptReqDlvryDate;
    private Button chkAcceptReqDlvryDate;
    private Button btnShipDateLookup;
    private Label lblErrAcceptNonStrdShipMethod;
    private Button chkAcceptNonStrdShipMethod;
    private Label lblShipComplete;
//  private Button chkShipComplete;
    private Combo comboShipComplete;
    private Button chkAcceptShipComplete;
    private Label lblErrShipComplete;
    
	private Text txtShipToName;
	private Label lblCurrencyCode;
	private Text txtCurrencyCode;
	private Text txtShipToAddress;
	private Text txtBillToAddress;
    
    private Label lblInternalComments;
	private Text txtInternalComments;
	private Label lblHdrComments;
	private Text txtHdrComments;
	private Label lblAttentionName;
	private Text txtAttentionName;
	private Label lblWillCall;
	private Text txtWillCall;
	private Label lblErrCustPONo;
	private Label lblErrPreventAutoOrdPlace;
	private Button chkPreventAutoOrdPlace;
	private Label lblErrAcceptShipToZipCode;
	private Button chkAcceptShipToZipCode;
    private Link linkOnHold;
    private Composite parent;
    private OrderLinesPanel pnlOrderLines;
	private OrderHeaderPanelBehavior myBehavior;
	private String strOldHdrComments;
	private String strOldInternalComments;
	
	//Used only order Edit Order
    private Label lblLegacyOrderType;
    private Text txtLegacyOrderType;
    private Label lblWebHoldFlag;
    private Label lblOrderedBy;
    private Text txtOrderedBy;
    private Label lblOrderSource;
    private Text txtOrderSource;
    private Text txtOrderSttsComment;
    private Label lblTotOrderFreight;
    private Text txtTotOrderFreight;
    private Label lblTotOrderTax;
    private Text txtTotOrderTax;
    private Label lblShipFromDiv;
    private Text txtShipFromDiv;
    
    private Text txtCouponCode;
    
    private Text txtBillTo;
    private Label lblBillTo;
    private Text txtBillToName;
    private Text txtShipTo;
    private Label lblShipTo;
    private Label lblStoreId;
    private Text txtStoreId;
    private Button btnViewOriginal;
    private Label lblOrderHoldCode;
    private Text txtShipComplete;
    private Button chkWillCall;
    private Combo comboShipFromDiv;
    private Text txtLineCount;
    private Label lblLineCount;
    private Label lblOrderedByEmail;
    private Text txtOrderedByEmail;
    private Label lblApprovedBy;
    private Text txtApprovedBy;
    private Label lblBillToInfo;
    private Label lblShipToInfo;
    private Label lblApprovedDate;
    private Text txtApprovedDate;
    private Text txtPCardNo;
    private Label lblPCardNo;
    private Text txtCardExpDt;
    private Label lblCardExpDt;
    private Label lblCardType;
    private Text txtCardType;
    private Label lblNameOnPcard;
    private Text txtNameOnPcard;
    private Label lblCoupon;
    private Text txtOtherCount;
    private Button chkWebHoldFlag;
    private Button chkOrderHoldFlag;
    private Button chkAttentionLines;
    private Label lblAttentionLine;
	private Button chkAcceptHeaderComments;
	private Button chkPreventBackOrder;
	private Label lblPreventBackOrder;
	private Label lblOrderLockFlag;
	private Label lblOrderType;
	private Text txtOrderType;
	public Label lblHeaderErr;
	private List<String> orderPlaceError;
	private Element orderEle;
	private Label lblCSRReviewError;
    
	
	public OrderHeaderPanel(Composite parent, OrderLinesPanel pnlOrderLines, int style,Object inObj, Element eleOrderDetails) {
		super(parent, style);
		this.parent = parent;
		this.inputObject = inObj;
		this.eleOrderDetails=eleOrderDetails;
		this.pnlOrderLines = pnlOrderLines;
		this.orderEle=pnlOrderLines.getPageBehavior().getLocalModel("OriginalOrder");
		selectionAdapter = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Widget ctrl = e.widget;
				String ctrlName = (String) ctrl.getData("name");
				if (!YRCPlatformUI.isVoid(ctrlName)){
					if (YRCPlatformUI.equals(ctrlName, "linkOnHold") || YRCPlatformUI.equals(ctrlName, "buttonOrderHoldIndicator")) {
						Element input = YRCXmlUtils.getCopy(myBehavior.getInputElement());
						input.getOwnerDocument().renameNode(input, input.getNamespaceURI(), "Order");
						YRCEditorInput editorInput = new YRCEditorInput(input, new String[] { "OrderHeaderKey" }, "YCD_TASK_ORDER_HOLDS");
						editorInput.getXml().setAttribute("SupportLevel", "Full");
						editorInput.setTaskName("YCD_TASK_ORDER_HOLDS");
						YRCPlatformUI.openEditor( "com.xpedx.sterling.rcp.pca.orderlines.editor.XPXOrderLinesEditor", editorInput);
					}
				}
			}
		};
		this.initialize();
		this.setBindingForComponents();
		myBehavior = new OrderHeaderPanelBehavior(this, FORM_ID, inputObject, eleOrderDetails);
		updateNonBindedComponents();
	}
	private void initialize() {
		GridLayout gridDataPnl = new GridLayout();
		gridDataPnl.horizontalSpacing = 10;
		gridDataPnl.marginWidth = 3;
		gridDataPnl.marginHeight = 3;
		gridDataPnl.verticalSpacing = 2;
		gridDataPnl.numColumns = 10;
		pnlRoot = new Composite(this, SWT.NONE);
		pnlRoot.setData("name", "pnlRoot");
		pnlRoot.setLayout(gridDataPnl);
		XPXUtils.paintPanel(pnlRoot);
		//this.createPnlTransactionError();
		this.createPnlHeaderInfo();
		this.setLayout(new FillLayout());
		this.setSize(new Point(670, 200));
	}

	private void setBindingForComponents() {
		YRCButtonBindingData chkBoxBindingData = null;
		YRCTextBindingData textBindingData = new YRCTextBindingData();
		if (!isFromOrderEntryWizard()) {
			textBindingData
					.setSourceBinding("OrderDetails:Order/Extn/@ExtnWebConfNum");
			textBindingData
					.setTargetBinding("SaveOrder:/Order/Extn/@ExtnWebConfNum");
			textBindingData.setName("txtWebConfirmationNo");
			txtWebConfirmationNo.setData("YRCTextBindingDefination",
					textBindingData);
		}
        
		/*if(!isFromOrderEntryWizard()){
		textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnLegacyOrderNo");
        //textBindingData.setKey("xpedx_OrderNo_key");
        textBindingData.setName("txtOrderNo");
        txtOrderNo.setData("YRCTextBindingDefination", textBindingData);
		}*/
        
        
        
        if (!isFromOrderEntryWizard()) {
			textBindingData = new YRCTextBindingData();
			textBindingData.setSourceBinding("OrderDetails:Order/@Status");
			textBindingData.setName("txtOrderStatus");
			txtOrderStatus.setData("YRCTextBindingDefination", textBindingData);

			textBindingData = new YRCTextBindingData();
			textBindingData.setSourceBinding("OrderDetails:Order/@OrderDate");
			textBindingData.setName("txtOrderDate");
			textBindingData.setDataType("Date");
			txtOrderDate.setData("YRCTextBindingDefination", textBindingData);
			
			//Fix for 3528
			textBindingData = new YRCTextBindingData();
			textBindingData.setName("txtOrderTime");
			txtOrderTime.setData("YRCTextBindingDefination", textBindingData);
		}
//        
//        //  TODO txtTotalAmount currency XPath not working.
        textBindingData = new YRCTextBindingData();
        textBindingData.setName("txtTotalAmount");
        textBindingData.setSourceBinding("OrderDetails:/Order/Extn/@ExtnTotalOrderValue");
        textBindingData.setCurrencyXPath("OrderDetails:/Order/PriceInfo/@Currency");
        txtTotalAmount.setData("YRCTextBindingDefination", textBindingData);
        
        textBindingData = new YRCTextBindingData();
        textBindingData.setName("txtShippedValue");
        textBindingData.setSourceBinding("OrderDetails:/Order/Extn/@ExtnTotalShipValue");
        textBindingData.setCurrencyXPath("OrderDetails:/Order/PriceInfo/@Currency");
        txtShippedValue.setData("YRCTextBindingDefination", textBindingData);
        
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:/Order/Extn/@ExtnETradingID");
        textBindingData.setName("txtETradingID");
        txtETradingID.setData("YRCTextBindingDefination", textBindingData);
        
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:/Order/@CustomerPONo");
        textBindingData.setTargetBinding("SaveOrder:/Order/@CustomerPONo");
        textBindingData.setName("txtCustPONo");
        txtCustPONo.setData("YRCTextBindingDefination", textBindingData);
        
        if(null != chkAcceptDupCustPONo){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
//			chkBoxBindingData.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnPriceOverrideFlag");
//			chkBoxBindingData.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnPriceOverrideFlag");
			chkBoxBindingData.setName("chkAcceptDupCustPONo");
			chkAcceptDupCustPONo.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
//        
//        /***/
//        //txtShipDate;
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:/Order/@ReqDeliveryDate");
        textBindingData.setTargetBinding("SaveOrder:/Order/@ReqDeliveryDate");
        textBindingData.setDataType("Date");
        textBindingData.setName("txtShipDate");
        txtShipDate.setData("YRCTextBindingDefination", textBindingData);
//        
        if(null != chkAcceptNoNextBusinessDay){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
//			chkBoxBindingData.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnPriceOverrideFlag");
//			chkBoxBindingData.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnPriceOverrideFlag");
			chkBoxBindingData.setName("chkAcceptNoNextBusinessDay");
			chkAcceptNoNextBusinessDay.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
        
        if(null != chkAcceptReqDlvryDate){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
//			chkBoxBindingData.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnPriceOverrideFlag");
//			chkBoxBindingData.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnPriceOverrideFlag");
			chkBoxBindingData.setName("chkAcceptReqDlvryDate");
			chkAcceptReqDlvryDate.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
        
		if(null != chkAcceptNonStrdShipMethod){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
//			chkBoxBindingData.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnPriceOverrideFlag");
//			chkBoxBindingData.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnPriceOverrideFlag");
			chkBoxBindingData.setName("chkAcceptNonStrdShipMethod");
			chkAcceptNonStrdShipMethod.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
		
		/*if (null != chkShipComplete) {
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("C");
			chkBoxBindingData.setUnCheckedBinding("N");
//			if ("Y".equals(pnlOrderLines.getPageBehavior().getShipCompleteFlag())) {
				chkBoxBindingData
						.setSourceBinding("OrderDetails:/Order/Extn/@ExtnShipComplete");
//			}
			chkBoxBindingData
					.setTargetBinding("SaveOrder:/Order/Extn/@ExtnShipComplete");
			chkBoxBindingData.setName("chkShipComplete");
			chkShipComplete.setData("YRCButtonBindingDefination",
					chkBoxBindingData);			
		}*/
	
		
		if(null != chkAcceptShipComplete){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
//			chkBoxBindingData.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnPriceOverrideFlag");
//			chkBoxBindingData.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnPriceOverrideFlag");
			chkBoxBindingData.setName("chkAcceptShipComplete");
			chkAcceptShipComplete.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
//		
//        //txtAttentionName
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:/Order/Extn/@ExtnAttentionName");
        textBindingData.setTargetBinding("SaveOrder:/Order/Extn/@ExtnAttentionName");
        textBindingData.setName("txtAttentionName");
        txtAttentionName.setData("YRCTextBindingDefination", textBindingData);

        if(null != chkPreventAutoOrdPlace){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
//			chkBoxBindingData.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnPriceOverrideFlag");
//			chkBoxBindingData.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnPriceOverrideFlag");
			chkBoxBindingData.setName("chkPreventAutoOrdPlace");
			chkPreventAutoOrdPlace.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
        if(null != chkAcceptShipToZipCode){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
//			chkBoxBindingData.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnPriceOverrideFlag");
//			chkBoxBindingData.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnPriceOverrideFlag");
			chkBoxBindingData.setName("chkAcceptShipToZipCode");
			chkAcceptShipToZipCode.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
        if(null != chkAcceptHeaderComments){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
//			chkBoxBindingData.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnPriceOverrideFlag");
//			chkBoxBindingData.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnPriceOverrideFlag");
			chkBoxBindingData.setName("chkAcceptHeaderComments");
			chkAcceptHeaderComments.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
        if(null != chkPreventBackOrder){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
//			chkBoxBindingData.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnPriceOverrideFlag");
//			chkBoxBindingData.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnPriceOverrideFlag");
			chkBoxBindingData.setName("chkPreventBackOrder");
			chkPreventBackOrder.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}        
        
        
//        
//        /***/
        if (null != linkOnHold) {
			YRCLinkBindingData linkBindingData = new YRCLinkBindingData();
			linkBindingData.setName("linkOnHold");
			linkBindingData
					.setActionId("com.yantra.pca.ycd.rcp.tasks.common.actions.YCDViewLineHoldsAction");
			linkBindingData.setActionHandlerEnabled(true);
			linkBindingData.setKey("");
			linkOnHold.setData("YRCLinkBindingDefination", linkBindingData);
		}

        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnShipToName");
       	textBindingData.setTargetBinding("SaveOrder:/Order/Extn/@ExtnShipToName");
        textBindingData.setName("txtShipToName");
        txtShipToName.setData("YRCTextBindingDefination", textBindingData);
        
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:Order/PriceInfo/@Currency");
        textBindingData.setTargetBinding("SaveOrder:Order/PriceInfo/@Currency");
        textBindingData.setName("txtCurrencyCode");
        txtCurrencyCode.setData("YRCTextBindingDefination", textBindingData);
        
        if(!isFromOrderEntryWizard()){

////            txtLegacyOrderType
            textBindingData = new YRCTextBindingData();
            textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnLegacyOrderType");
            textBindingData.setName("txtLegacyOrderType");
            txtLegacyOrderType.setData("YRCTextBindingDefination", textBindingData);

////        txtOrderedBy - binding to Buyer Name
            textBindingData = new YRCTextBindingData();
            textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnOrderedByName");
            textBindingData.setTargetBinding("SaveOrder:Order/Extn/@ExtnOrderedByName");
            textBindingData.setName("txtOrderedBy");
            txtOrderedBy.setData("YRCTextBindingDefination", textBindingData);
////            txtOrderSource
            textBindingData = new YRCTextBindingData();
            textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnSourceType");
            textBindingData.setName("txtOrderSource");
            txtOrderSource.setData("YRCTextBindingDefination", textBindingData);
////          txtOrderSttsComment - binding
            textBindingData = new YRCTextBindingData();
            textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnOrdStatCom");
            textBindingData.setName("txtOrderSttsComment");
            txtOrderSttsComment.setData("YRCTextBindingDefination", textBindingData);
////            txtTotOrderFreight
            textBindingData = new YRCTextBindingData();
            textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnTotalOrderFreight");
            textBindingData.setCurrencyXPath("OrderDetails:/Order/PriceInfo/@Currency");
            textBindingData.setName("txtTotOrderFreight");
            txtTotOrderFreight.setData("YRCTextBindingDefination", textBindingData);
////            txtTotOrderTax
            textBindingData = new YRCTextBindingData();
            textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnOrderTax");
            textBindingData.setCurrencyXPath("OrderDetails:/Order/PriceInfo/@Currency");
            textBindingData.setName("txtTotOrderTax");
            txtTotOrderTax.setData("YRCTextBindingDefination", textBindingData);
            
            chkBoxBindingData = new YRCButtonBindingData();
    		chkBoxBindingData.setCheckedBinding("Y");
    		chkBoxBindingData.setUnCheckedBinding("N");
    		chkBoxBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnWebHoldFlag");
    		chkBoxBindingData.setTargetBinding("SaveOrder:Order/Extn/@ExtnWebHoldFlag");
    		chkBoxBindingData.setName("chkWebHoldFlag");
    		chkWebHoldFlag.setData("YRCButtonBindingDefination", chkBoxBindingData);
    		
    		 textBindingData = new YRCTextBindingData();
             textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnPcardNo");
//             chkBoxBindingData.setTargetBinding("SaveOrder:Order/Extn/@ExtnPcardNo");
             textBindingData.setName("txtPCardNo");
             txtPCardNo.setData("YRCTextBindingDefination", textBindingData);
             
             textBindingData = new YRCTextBindingData();
             textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnCardExpDt");
//             chkBoxBindingData.setTargetBinding("SaveOrder:Order/Extn/@ExtnCardExpDt");
             textBindingData.setName("txtCardExpDt");
             txtCardExpDt.setData("YRCTextBindingDefination", textBindingData);
             
             textBindingData = new YRCTextBindingData();
             textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnCardType");
//             chkBoxBindingData.setTargetBinding("SaveOrder:Order/Extn/@ExtnCardType");
             textBindingData.setName("txtCardType");
             txtCardType.setData("YRCTextBindingDefination", textBindingData);
             
             textBindingData = new YRCTextBindingData();
             textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnNameOnPcard");
//             chkBoxBindingData.setTargetBinding("SaveOrder:Order/Extn/@ExtnNameOnPcard");
             textBindingData.setName("txtNameOnPcard");
             txtNameOnPcard.setData("YRCTextBindingDefination", textBindingData);
        }
        
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:Order/PersonInfoBillTo/@AddressLine1;OrderDetails:Order/PersonInfoBillTo/@AddressLine2;OrderDetails:Order/PersonInfoBillTo/@AddressLine3;OrderDetails:Order/PersonInfoBillTo/@City;OrderDetails:Order/PersonInfoBillTo/@State;OrderDetails:Order/PersonInfoBillTo/@ZipCode;OrderDetails:Order/PersonInfoBillTo/@Country");
        textBindingData.setKey("xpedx_BillTo_address_key");
        textBindingData.setName("txtBillToAddress");
        txtBillToAddress.setData("YRCTextBindingDefination", textBindingData);
        
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:Order/PersonInfoShipTo/@AddressLine1;OrderDetails:Order/PersonInfoShipTo/@AddressLine2;OrderDetails:Order/PersonInfoShipTo/@AddressLine3;OrderDetails:Order/PersonInfoShipTo/@City;OrderDetails:Order/PersonInfoShipTo/@State;OrderDetails:Order/PersonInfoShipTo/@ZipCode;OrderDetails:Order/PersonInfoShipTo/@Country");
        textBindingData.setKey("xpedx_ShipTo_address_key");
        textBindingData.setName("txtShipToAddress");
        txtShipToAddress.setData("YRCTextBindingDefination", textBindingData);
        
        //if(!isFromOrderEntryWizard()){
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnCustomerDivision;OrderDetails:Order/Extn/@ExtnCustomerNo;OrderDetails:Order/Extn/@ExtnBillToSuffix");
        textBindingData.setKey("xpedx_BillTo_key");
        textBindingData.setName("txtBillTo");
        txtBillTo.setData("YRCTextBindingDefination", textBindingData);
               
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnCustomerDivision;OrderDetails:Order/Extn/@ExtnCustomerNo;OrderDetails:Order/Extn/@ExtnShipToSuffix");
        textBindingData.setKey("xpedx_ShipTo_key");
        textBindingData.setName("txtShipTo");
        txtShipTo.setData("YRCTextBindingDefination", textBindingData);
        //}
        
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnBillToName");
        textBindingData.setName("txtBillToName");
        txtBillToName.setData("YRCTextBindingDefination", textBindingData);
        
        chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnWillCall");
		chkBoxBindingData.setTargetBinding("SaveOrder:Order/Extn/@ExtnWillCall");
		chkBoxBindingData.setName("chkWillCall");
		chkWillCall.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("OrderDetails:/Order/Extn/@ExtnDeliveryHoldFlag");
		chkBoxBindingData.setTargetBinding("SaveOrder:/Order/Extn/@ExtnDeliveryHoldFlag");
		chkBoxBindingData.setName("chkOrderHoldFlag");
		chkOrderHoldFlag.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("CustomerContactDetails:CustomerContact/@EmailID");
        textBindingData.setName("txtOrderedByEmail");
        txtOrderedByEmail.setData("YRCTextBindingDefination", textBindingData);
        
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnApprovedBy");
        textBindingData.setName("txtApprovedBy");
        txtApprovedBy.setData("YRCTextBindingDefination", textBindingData);
        
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:Order/Extn/@ExtnApprovedDate");
        textBindingData.setName("txtApprovedDate");
        txtApprovedDate.setData("YRCTextBindingDefination", textBindingData);
        
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("ShipToCustomer:CustomerList/Customer/Extn/@ExtnShipToStoreNo");
        textBindingData.setName("txtStoreId");
        txtStoreId.setData("YRCTextBindingDefination", textBindingData);
        
        
        YRCComboBindingData cbd = new YRCComboBindingData();
        cbd.setCodeBinding("@DivisionNo");
        cbd.setDescriptionBinding("@DivisionName");
        cbd.setListBinding("TransferCirclesList:/XPXXferCircleList/XPXXferCircle");
        cbd.setSourceBinding("OrderDetails:/Order/@ShipNode");
        cbd.setTargetBinding("SaveOrder:/Order/@ShipNode");
        cbd.setName("comboShipfrom");
        comboShipFromDiv.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
        
        cbd = new YRCComboBindingData();
        cbd.setCodeBinding("@CodeValue");
        cbd.setDescriptionBinding("@CodeShortDescription");
        cbd.setListBinding("ShipComplete:/Code");
		cbd.setSourceBinding("OrderDetails:/Order/Extn/@ExtnShipComplete");
		cbd.setTargetBinding("SaveOrder:/Order/Extn/@ExtnShipComplete");
		cbd.setName("comboShipComplete");
        comboShipComplete.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);

        
              
     }
	
	private void updateNonBindedComponents() {

		if(pnlOrderLines.getPageBehavior().isReadOnlyPage()){
			Control[] controls = new Control[]{
					txtShipFromDiv,
					txtCustPONo,
					txtShipDate,
					btnShipDateLookup,
//					chkShipComplete,
					comboShipComplete,
					txtInternalComments,
					txtHdrComments,
					txtAttentionName,
					txtWillCall,
					compositeStatusHold,

					chkPreventAutoOrdPlace,
					chkAcceptShipToZipCode,
					chkAcceptNonStrdShipMethod,
					chkAcceptNoNextBusinessDay,
					chkAcceptReqDlvryDate,
					chkAcceptShipComplete,	
					chkAcceptDupCustPONo,
					comboShipFromDiv,
					txtCouponCode};
			setControlsEnabled(controls, false);
		}
		if (!isDraftOrder()) {
			if(XPXUtils.isFullFillmentOrder(orderEle)){
				String legacyOrderNumber = YRCXmlUtils.getAttributeValue(orderEle, "/Order/Extn/@ExtnLegacyOrderNo");
					if(!YRCPlatformUI.isVoid(legacyOrderNumber)){
						comboShipFromDiv.setEnabled(false);
						comboShipComplete.setEnabled(false);
					}
			}
		}
		
		// HeaderComment
		String strHdrComment = (String)YRCXPathUtils.evaluate(this.eleOrderDetails, "/Order/Instructions/Instruction[@InstructionType='HEADER']/@InstructionText", XPathConstants.STRING);
		this.myBehavior.setFieldValue("txtHdrComments", strHdrComment);
		strOldHdrComments = strHdrComment; 
		
		// InternalComments
		String strInternalComment = (String)YRCXPathUtils.evaluate(this.eleOrderDetails, "/Order/Instructions/Instruction[@InstructionType='INTERNAL']/@InstructionText", XPathConstants.STRING);
		this.myBehavior.setFieldValue("txtInternalComments", strInternalComment);
		strOldInternalComments = strInternalComment;
		//Legacy order number if not present dont show anything
        String legacyOrderNumber = YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Extn/@ExtnLegacyOrderNo");
        if(YRCPlatformUI.isVoid(legacyOrderNumber)){
        	txtOrderNo.setVisible(false);
        }
        
        // Stamping the No. Of Product Lines and Other Lines.
        txtLineCount.setText(String.valueOf(getOrderLinesPanel().getPageBehavior().getNoOfProductLines()));
        txtOtherCount.setText(String.valueOf(getOrderLinesPanel().getPageBehavior().getNoOfOtherLines()));
        
        // Stamping Order Type
        if(XPXUtils.isFullFillmentOrder(eleOrderDetails)){
        	this.myBehavior.setFieldValue("txtOrderType", YRCPlatformUI.getString("Fulfilment_Order"));
        	//According to JIRA - 1359 
        	if(!YRCPlatformUI.isVoid(legacyOrderNumber)){
        		txtHdrComments.setEnabled(false);
        	}
        }else if("Customer".equals(eleOrderDetails.getAttribute("OrderType"))){
        	this.myBehavior.setFieldValue("txtOrderType", YRCPlatformUI.getString("Customer_Order"));
    	}
        else{
        	this.myBehavior.setFieldValue("txtOrderType", eleOrderDetails.getAttribute("OrderType"));
        }
        
        // Field Limits
        if(null != txtCustPONo && txtCustPONo.isDisposed()==false)
        	txtCustPONo.setTextLimit(22);
	}
	
	private void setControlsEnabled(Control[] controls, boolean enabled) {
		// TODO 
//		controls = pnlRoot.getChildren();
//		pnlRoot.setEnabled(false);
		for (Control control : controls) {
			if(null != control)
				control.setEnabled(enabled);
		}
		
	}
	
	public void createPnlTransactionError(){
		
		createPnlNeedsAttention();
		
		Composite pnlTrnsactionError = new Composite(pnlRoot,SWT.NONE);
		pnlTrnsactionError.setBackgroundMode(SWT.INHERIT_NONE);
		pnlTrnsactionError.setData(YRCConstants.YRC_CONTROL_NAME, "pnlTrnsactionError");
		GridLayout pnlTrnsactionErrorLayout = new GridLayout(3,false);
		pnlTrnsactionErrorLayout.horizontalSpacing = 1;
		pnlTrnsactionErrorLayout.verticalSpacing = 1;
//		pnlTrnsactionErrorLayout.numColumns = 8;
		pnlTrnsactionError.setLayout(pnlTrnsactionErrorLayout);
		
		
		GridData pnlTrnsactionErrorLayoutData = new GridData();
		pnlTrnsactionErrorLayoutData.horizontalAlignment = 4;
		pnlTrnsactionErrorLayoutData.grabExcessHorizontalSpace = true;
		pnlTrnsactionErrorLayoutData.horizontalSpan = 10;
		pnlTrnsactionError.setLayoutData(pnlTrnsactionErrorLayoutData);
		//pnlTrnsactionErrorLayoutData.heightHint = 100;
		//pnlTrnsactionErrorLayoutData.widthHint = 400;
		
		
		
		
		GridData gridDataChk = new GridData();
		gridDataChk.horizontalAlignment = SWT.FILL;
		gridDataChk.grabExcessHorizontalSpace = false;
		gridDataChk.verticalAlignment = SWT.FILL;
//		gridDataChk.widthHint = 10;
		
		GridData gridDataErrLbl = new GridData();
        gridDataErrLbl.horizontalAlignment = SWT.FILL;
        gridDataErrLbl.horizontalSpan = 2;
        gridDataErrLbl.verticalAlignment = SWT.FILL;;
          
        gridDataErrLbl.grabExcessHorizontalSpace = true;
        
		GridData gridDataErrLbl1 = new GridData();
		gridDataErrLbl1.horizontalAlignment = SWT.BEGINNING;
		gridDataErrLbl1.verticalAlignment = SWT.FILL;;
		gridDataErrLbl1.horizontalSpan = 3;
		gridDataErrLbl1.horizontalIndent=2;
		gridDataErrLbl1.grabExcessHorizontalSpace = true;
        
		String errorCode = YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Extn/@ExtnOrdHdrLevelFailedRuleID");
		String errorText = null;
		if(!YRCPlatformUI.isVoid(errorCode)){
			if(errorCode.equals("DuplicatePO")){ 
				errorText = XPXCacheManager.getsetRuleIDDescription(errorCode);
				if (!YRCPlatformUI.isVoid(errorText)) {	
			        chkAcceptDupCustPONo = new Button(pnlTrnsactionError, SWT.CHECK);
					chkAcceptDupCustPONo.setText("");
					chkAcceptDupCustPONo.setVisible(true);
					chkAcceptDupCustPONo.setData("name", "chkAcceptDupCustPONo");
					chkAcceptDupCustPONo.setData("yrc:customType", "Label");
					chkAcceptDupCustPONo.setLayoutData(gridDataChk);
			               
					lblErrCustPONo = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
					lblErrCustPONo.setText(errorText);
					lblErrCustPONo.setLayoutData(gridDataErrLbl);
					lblErrCustPONo.setData("yrc:customType", "RedText10");
				}
			}
			if(errorCode.equals("HeaderCommentByCustomer")){ 
				errorText = XPXCacheManager.getsetRuleIDDescription(errorCode);
				//errorText =	YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Error/@HeaderCommentByCustomer");
				if (!YRCPlatformUI.isVoid(errorText)) {	
			        chkAcceptHeaderComments = new Button(pnlTrnsactionError, SWT.CHECK);
			        chkAcceptHeaderComments.setText("");
			        chkAcceptHeaderComments.setVisible(true);
			        chkAcceptHeaderComments.setData("name", "chkAcceptWithoutComments");
			        chkAcceptHeaderComments.setData("yrc:customType", "Label");
			        chkAcceptHeaderComments.setLayoutData(gridDataChk);
			        
					lblErrCustPONo = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
					lblErrCustPONo.setText(errorText);
					lblErrCustPONo.setLayoutData(gridDataErrLbl);
					lblErrCustPONo.setData("yrc:customType", "RedText10");	        
				}
			}
			if(errorCode.equals("RequireCustomerPO")){ 
				errorText = XPXCacheManager.getsetRuleIDDescription(errorCode);
				//errorText = YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Error/@RequireCustomerPO");
				if (!YRCPlatformUI.isVoid(errorText)) {	
					lblErrCustPONo = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
					lblErrCustPONo.setText(errorText);
					lblErrCustPONo.setLayoutData(gridDataErrLbl1);
					lblErrCustPONo.setData("yrc:customType", "RedText10");
				}
			}
			
			if(errorCode.equals("PreventAutoPlace")){ 
				errorText = XPXCacheManager.getsetRuleIDDescription(errorCode);
				errorText = YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Error/@PreventAutoPlace");
		//		errorText = "chkPreventAutoOrdPlace";
		        if (!YRCPlatformUI.isVoid(errorText)) {
				    chkPreventAutoOrdPlace = new Button(pnlTrnsactionError, SWT.CHECK);
					chkPreventAutoOrdPlace.setText("");
					chkPreventAutoOrdPlace.setVisible(true);
					chkPreventAutoOrdPlace.setData("name", "chkPreventAutoOrdPlace");
					chkPreventAutoOrdPlace.setData("yrc:customType", "Label");
					chkPreventAutoOrdPlace.setLayoutData(gridDataChk);
					lblErrPreventAutoOrdPlace = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
					lblErrPreventAutoOrdPlace.setText(errorText);
					lblErrPreventAutoOrdPlace.setLayoutData(gridDataErrLbl);
					lblErrPreventAutoOrdPlace.setData("yrc:customType", "RedText10");
				}
			}
			
	        if(errorCode.equals("ValidShiptoZipCode")){ 
					errorText = XPXCacheManager.getsetRuleIDDescription(errorCode);
				//errorText = YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Error/@ValidShiptoZipCode");
				if (!YRCPlatformUI.isVoid(errorText)) {
					chkAcceptShipToZipCode = new Button(pnlTrnsactionError, SWT.CHECK);
					chkAcceptShipToZipCode.setText("");
					chkAcceptShipToZipCode.setVisible(true);
					chkAcceptShipToZipCode.setData("name", "chkAcceptShipToZipCode");
					chkAcceptShipToZipCode.setData("yrc:customType", "Label");
					chkAcceptShipToZipCode.setLayoutData(gridDataChk);
					lblErrAcceptShipToZipCode = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
					lblErrAcceptShipToZipCode.setText(errorText);
					lblErrAcceptShipToZipCode.setLayoutData(gridDataErrLbl);
					lblErrAcceptShipToZipCode.setData("yrc:customType", "RedText10");
				}
	        }
			if(errorCode.equals("NonStandardShipMethod")){ 
				errorText = XPXCacheManager.getsetRuleIDDescription(errorCode);
				//errorText = YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Error/@NonStandardShipMethod");
				if (!YRCPlatformUI.isVoid(errorText)) {
							
					        chkAcceptNonStrdShipMethod = new Button(pnlTrnsactionError, SWT.CHECK);
							chkAcceptNonStrdShipMethod.setText("");
							chkAcceptNonStrdShipMethod.setVisible(true);
							chkAcceptNonStrdShipMethod.setData("name", "chkAcceptNonStrdShipMethod");
							chkAcceptNonStrdShipMethod.setData("yrc:customType", "Label");
							chkAcceptNonStrdShipMethod.setLayoutData(gridDataChk);
							lblErrAcceptNonStrdShipMethod = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
							lblErrAcceptNonStrdShipMethod.setText(errorText);
							lblErrAcceptNonStrdShipMethod.setLayoutData(gridDataErrLbl);
							lblErrAcceptNonStrdShipMethod.setData("yrc:customType", "RedText10");
						}
			}
			
			if(errorCode.equals("ShipDateNotNextBusinessDay")){ 
				errorText = XPXCacheManager.getsetRuleIDDescription(errorCode);
				//errorText = YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Error/@ShipDateNotNextBusinessDay");
		        //errorText = "Testing.......";
				if (!YRCPlatformUI.isVoid(errorText)) {
				    chkAcceptNoNextBusinessDay = new Button(pnlTrnsactionError, SWT.CHECK);
					chkAcceptNoNextBusinessDay.setText("");
					chkAcceptNoNextBusinessDay.setVisible(true);
					chkAcceptNoNextBusinessDay.setData("name", "chkAcceptNoNextBusinessDay");
					chkAcceptNoNextBusinessDay.setData("yrc:customType", "Label");
					chkAcceptNoNextBusinessDay.setLayoutData(gridDataChk);
					lblErrAcceptNoNextBusinessDay = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
					lblErrAcceptNoNextBusinessDay.setText(errorText);
					/*gridDataErrLbl.horizontalSpan = 2;*/
					lblErrAcceptNoNextBusinessDay.setLayoutData(gridDataErrLbl);
					lblErrAcceptNoNextBusinessDay.setData("yrc:customType", "RedText10");
				}
			}
			errorText = null;
			if(errorCode.equals("AllDeliveryDatesDoNotMatch")){ 
				errorText = XPXCacheManager.getsetRuleIDDescription(errorCode);
				//errorText = YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Error/@AllDeliveryDatesDoNotMatch");
				//errorText = "Testing.......";
				if (!YRCPlatformUI.isVoid(errorText)) {
					chkAcceptReqDlvryDate = new Button(pnlTrnsactionError, SWT.CHECK);
					chkAcceptReqDlvryDate.setText("");
					chkAcceptReqDlvryDate.setVisible(true);
					chkAcceptReqDlvryDate.setData("name", "chkAcceptReqDlvryDate");
					chkAcceptReqDlvryDate.setData("yrc:customType", "Label");
					chkAcceptReqDlvryDate.setLayoutData(gridDataChk);
					lblErrAcceptReqDlvryDate = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
					lblErrAcceptReqDlvryDate.setText(errorText);
					lblErrAcceptReqDlvryDate.setLayoutData(gridDataErrLbl);
					lblErrAcceptReqDlvryDate.setData("yrc:customType", "RedText10");
				}
			}
					
			if(errorCode.equals("CustomerSelectedShipComplete")){ 
				errorText = XPXCacheManager.getsetRuleIDDescription(errorCode);
				//errorText = YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Error/@CustomerSelectedShipComplete");
				//errorText = "Testing.......";
				if (!YRCPlatformUI.isVoid(errorText)) {
					chkAcceptShipComplete = new Button(pnlTrnsactionError, SWT.CHECK);
					chkAcceptShipComplete.setText("");
					chkAcceptShipComplete.setVisible(true);
					chkAcceptShipComplete.setData("name", "chkAcceptShipComplete");
					chkAcceptShipComplete.setData("yrc:customType", "Label");
					chkAcceptShipComplete.setLayoutData(gridDataChk);
					lblErrShipComplete = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
					lblErrShipComplete.setText(errorText);
					lblErrShipComplete.setLayoutData(gridDataErrLbl);
					lblErrShipComplete.setData("yrc:customType", "RedText10");
				}
			}
			if(errorCode.equals("PreventBackOrder")){ 
				errorText = XPXCacheManager.getsetRuleIDDescription(errorCode);
				//errorText = YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Error/@PreventBackOrder");
				if (!YRCPlatformUI.isVoid(errorText)) {
			        chkPreventBackOrder = new Button(pnlTrnsactionError, SWT.CHECK);
					chkPreventBackOrder.setText("");
					chkPreventBackOrder.setVisible(true);
					chkPreventBackOrder.setData("name", "chkPreventBackOrder");
					chkPreventBackOrder.setData("yrc:customType", "InvalidData");
					chkPreventBackOrder.setLayoutData(gridDataChk);
					lblPreventBackOrder = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
					lblPreventBackOrder.setText("preventive order error");
					lblPreventBackOrder.setLayoutData(gridDataErrLbl);
					lblPreventBackOrder.setData("yrc:customType", "RedText10");
				}
			}
		}
			
		
		//chekcing checkOrderLockFlag() & setting Error lable message @Order header level.  # part of CR changes (2582)
		if(getOrderLinesPanel().getPageBehavior().checkOrderLockFlag())
		{ 
			//Order_Lock_Lbl_Error
		lblOrderLockFlag = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
		if((YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Extn/@ExtnHeaderStatusCode")).equalsIgnoreCase("M0007")){
			lblOrderLockFlag.setText(YRCPlatformUI.getString(""));
		} else{
			lblOrderLockFlag.setText(YRCPlatformUI.getString("Order_Lock_Lbl_Error"));		}
		
		
		lblOrderLockFlag.setLayoutData(gridDataErrLbl);
		lblOrderLockFlag.setData("yrc:customType", "RedText10");
		}
		//Added for CR 4323. The order must be locked if Order update is failing
		if(getOrderLinesPanel().getPageBehavior().checkPermananentOrderLockFlag()){
			lblOrderLockFlag = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
			if((YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/Extn/@ExtnOUFailureLockFlag")).equalsIgnoreCase("Y")){
				lblOrderLockFlag.setText(YRCPlatformUI.getString("Order_update_Lock_Lbl_Error"));
			}
			lblOrderLockFlag.setLayoutData(gridDataErrLbl);
			lblOrderLockFlag.setData("yrc:customType", "RedText10");
			if(pnlOrderLines.getPageBehavior().isReadOnlyPage()){
				Control[] controls = new Control[]{
						txtShipFromDiv,
						txtCustPONo,
						txtShipDate,
						btnShipDateLookup,
//						chkShipComplete,
						chkAttentionLines,
						comboShipComplete,
						txtInternalComments,
						txtHdrComments,
						txtAttentionName,
						txtWillCall,
						compositeStatusHold,
						chkOrderHoldFlag,
						chkWebHoldFlag,
						chkPreventAutoOrdPlace,
						chkAcceptShipToZipCode,
						chkAcceptNonStrdShipMethod,
						chkAcceptNoNextBusinessDay,
						chkAcceptReqDlvryDate,
						chkAcceptShipComplete,	
						chkAcceptDupCustPONo,
						comboShipFromDiv,
						txtCouponCode};
				setControlsEnabled(controls, false);
			}
		}
		
		if(isReviewedByCSR()){
			lblCSRReviewError = new Label(pnlTrnsactionError, SWT.HORIZONTAL);
			lblCSRReviewError.setText("Order is Being Reviewed By CSR.");
			lblCSRReviewError.setLayoutData(gridDataErrLbl);
			lblCSRReviewError.setData("yrc:customType", "RedText10");
			
			}

		
		String status = YRCXmlUtils.getAttributeValue(eleOrderDetails, "/Order/@Status");
		orderPlaceError=getErrorValues();
		if(orderPlaceError.size()!=0 && !"Cancelled".equalsIgnoreCase(status)){
			int count=orderPlaceError.size();
				for(int i=0;i<count;i++){
				 Label lblerrOrder=new Label(pnlTrnsactionError, SWT.HORIZONTAL);
				 String orderPlaceErrorText = (String) orderPlaceError.get(i);
				 int delimiter = orderPlaceErrorText.indexOf("-");
				 orderPlaceErrorText = orderPlaceErrorText.substring(delimiter+1,orderPlaceErrorText.length());
				 lblerrOrder.setText(orderPlaceErrorText);
				 lblerrOrder.setLayoutData(gridDataErrLbl1);
				 lblerrOrder.setData("yrc:customType", "RedText10");
				}
			} 
	}
	
	public void createPnlNeedsAttention(){
		Composite pnlNeedsAttention = new Composite(pnlRoot,SWT.NONE);
		pnlNeedsAttention.setBackgroundMode(SWT.INHERIT_NONE);
		pnlNeedsAttention.setData(YRCConstants.YRC_CONTROL_NAME, "pnlNeedsAttention");
		GridLayout pnlNeedsAttentionLayout = new GridLayout(10,false);
		pnlNeedsAttentionLayout.horizontalSpacing = 1;
		pnlNeedsAttentionLayout.verticalSpacing = 1;
		//pnlTrnsactionErrorLayout.numColumns = 8;
		pnlNeedsAttention.setLayout(pnlNeedsAttentionLayout);
		
		
		GridData pnlNeedsAttentionLayoutData = new GridData();
		pnlNeedsAttentionLayoutData.horizontalAlignment = 4;
		pnlNeedsAttentionLayoutData.grabExcessHorizontalSpace = true;
		pnlNeedsAttentionLayoutData.horizontalSpan = 10;
		pnlNeedsAttention.setLayoutData(pnlNeedsAttentionLayoutData);
			
		GridData gridData1 = new GridData();
		gridData1.verticalAlignment = 2;
		gridData1.widthHint = 815;
		gridData1.horizontalSpan = 5;
		
		GridData gridDataInvoice = new GridData();
		gridDataInvoice.verticalAlignment = 2;
		gridDataInvoice.horizontalAlignment=SWT.CENTER;
		gridDataInvoice.horizontalSpan = 7;
		//adding or changes as part of bug#1600 - SWT.End to center & Indent
		gridDataInvoice.horizontalIndent=810;
		
		
		
		
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = SWT.CENTER;
		gridData2.verticalAlignment = 2;
		//adding or changes as part of bug#1600 - Indentation
		gridData2.horizontalIndent=65;
		
		
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = SWT.LEFT;
		gridData3.verticalAlignment = 2;
	
		
		
		lblHeaderErr = new Label(pnlNeedsAttention, SWT.NONE);
		lblHeaderErr.setLayoutData(gridData1);
		lblHeaderErr.setData("name", "lblHeaderErr");
		lblHeaderErr.setData("yrc:customType", "RedText10");
		
//		Text txtDummy = new Text(pnlNeedsAttention,72);
//		txtDummy.setLayoutData(gridData1);
		chkAttentionLines = new Button(pnlNeedsAttention, SWT.CHECK);
		chkAttentionLines.setText("");
		chkAttentionLines.setVisible(true);
		chkAttentionLines.setData("name", "chkAttentionLines");
		chkAttentionLines.setData("yrc:customType", "Label");
		chkAttentionLines.setLayoutData(gridData2);
		
		
		chkAttentionLines.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if(chkAttentionLines.getSelection()){
					pnlOrderLines.getPageBehavior().getAttention("Checked");
				}else {
					pnlOrderLines.getPageBehavior().getAttention("UnChecked");
				}
			}
		});
		
		lblAttentionLine = new Label(pnlNeedsAttention, SWT.HORIZONTAL);
		lblAttentionLine.setText("Show Needs Attention Lines First");
		lblAttentionLine.setLayoutData(gridData3);
		lblAttentionLine.setData("yrc:customType", "Text");
		
		Link lnkInvoice = new Link(pnlRoot, 0);
		lnkInvoice.setText("View Invoice");
		lnkInvoice.setData("name", "lnkInvoice");
		lnkInvoice.setData("yrc:customType", "Link");
		lnkInvoice.setEnabled(true);
		lnkInvoice.setLayoutData(gridDataInvoice);
		lnkInvoice.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { public void
			 widgetSelected( org.eclipse.swt.events.SelectionEvent e) {
							myBehavior.openUrl();
					}
			});	
		
	}
	
	public void createPnlHeaderInfo() {
		
		
			createPnlTransactionError();
		
		
		GridData gridData1 = new GridData();
		gridData1.widthHint = 105;
		gridData1.horizontalAlignment = SWT.BEGINNING;
		gridData1.verticalAlignment = 4;
				
		GridData gridData2 = new GridData();
		gridData2.widthHint = 165;
		gridData2.heightHint = 20;
		gridData2.horizontalAlignment = SWT.FILL;
		gridData2.verticalAlignment = 4;
				
		GridData gridData3 = new GridData();
		gridData3.widthHint = 110;
		gridData3.horizontalAlignment = SWT.FILL;
		gridData3.verticalAlignment = 4;
		
		GridData gridDataShipToInfoLable = new GridData();
		gridDataShipToInfoLable.widthHint = 110;
		gridDataShipToInfoLable.horizontalAlignment = SWT.FILL;
		gridDataShipToInfoLable.verticalAlignment = 4;
		
				
		GridData gridData4 = new GridData();
		gridData4.widthHint = 120;
		gridData4.horizontalAlignment = SWT.NONE;
		gridData4.verticalAlignment = 4;
		
		GridData gridData5 = new GridData();
		gridData5.widthHint = 70;
		gridData5.grabExcessHorizontalSpace = false;
		gridData5.horizontalAlignment = SWT.BEGINNING;
		gridData5.verticalAlignment = 4;
		
		GridData gridData51 = new GridData();
		gridData51.grabExcessHorizontalSpace = false;
		gridData51.horizontalAlignment = SWT.BEGINNING;
		gridData51.verticalAlignment = 4;
				
		GridData gridData6 = new GridData();
		gridData6.widthHint = 145;
		gridData6.horizontalAlignment = SWT.BEGINNING;
		gridData6.verticalAlignment = 4;
				
		GridData gridData7 = new GridData();
		gridData7.widthHint = 180;
		gridData7.horizontalAlignment = SWT.BEGINNING;
		gridData7.horizontalSpan = 2;
		gridData7.grabExcessHorizontalSpace = true;
			
		//adding Changes as part of 1600 Coupon txt changes
		GridData gridDataCouponTxt = new GridData();
		gridDataCouponTxt.widthHint = 160;
		gridDataCouponTxt.horizontalAlignment = SWT.BEGINNING;
		gridDataCouponTxt.horizontalSpan=2;
		gridDataCouponTxt.grabExcessHorizontalSpace=true;
		gridDataCouponTxt.verticalAlignment = 162;
		
		GridData gridData8 = new GridData();
		gridData8.widthHint = 70;
		gridData8.horizontalAlignment = SWT.BEGINNING;
		gridData8.verticalAlignment = 4;
		
		
		//needs attention
		
		
		
				
		lblBillTo = new Label(pnlRoot, SWT.LEFT);
		lblBillTo.setText("Bill To:");
		lblBillTo.setLayoutData(gridData1);
		lblBillTo.setData("name", "lblBillTo");
		txtBillTo = new Text(pnlRoot, 72);
		txtBillTo.setLayoutData(gridData2);
		txtBillTo.setData("name", "txtBillTo");
		txtBillTo.setEnabled(false);
		
				
		GridData gridData9 = new GridData();
		gridData9.widthHint = 25;
		gridData9.horizontalAlignment = SWT.FILL;
		gridData9.verticalAlignment = 4;
		gridData9.horizontalSpan = 8;
				
		txtBillToName = new Text(pnlRoot, 72);
		txtBillToName.setLayoutData(gridData9);
		txtBillToName.setData("name", "txtBillToName");
		txtBillToName.setEnabled(false);
	
		Text txtDummy111 = new Text(pnlRoot, 72);
		txtDummy111.setLayoutData(gridData6);
		txtDummy111.setEnabled(false);
		
		GridData gridData112 = new GridData();
		gridData112.widthHint = 125;
		gridData112.horizontalAlignment = SWT.FILL;
		gridData112.verticalAlignment = 4;
		gridData112.horizontalSpan = 5;
			
		txtBillToName = new Text(pnlRoot, 72);
		txtBillToName.setLayoutData(gridData112);
		txtBillToName.setData("name", "txtBillToName");
		txtBillToName.setEnabled(false);
				
		
		lblETradingID = new Label(pnlRoot, SWT.LEFT);
		lblETradingID.setText("eTrading ID:");
		lblETradingID.setLayoutData(gridData6);
		lblETradingID.setData("name", "lblETradingID");
		
		txtETradingID = new Text(pnlRoot, 72);
		txtETradingID.setLayoutData(gridData7);
		txtETradingID.setData("name", "txtETradingID");
		txtETradingID.setEnabled(false);

		
		 
		GridData gridDatax11 = new GridData();
		gridDatax11.widthHint = 10;
		gridDatax11.grabExcessHorizontalSpace = false;
		gridDatax11.horizontalAlignment = SWT.BEGINNING;
		gridDatax11.verticalAlignment = 4;
		gridDatax11.horizontalSpan = 1;
		
//		Text txtDummy2011 = new Text(pnlRoot, 72);
//		txtDummy2011.setLayoutData(gridDatax11);
		
		Text txtDummy301 = new Text(pnlRoot, 72);
		txtDummy301.setLayoutData(gridDatax11);
		txtDummy301.setEnabled(false);
		
		 
				
		
					
		lblShipTo = new Label(pnlRoot, SWT.LEFT);
		lblShipTo.setText("Ship To:");
		lblShipTo.setLayoutData(gridData1);
		lblShipTo.setData("name", "lblShipTo");
		txtShipTo = new Text(pnlRoot, 72);
		txtShipTo.setLayoutData(gridData2);
		txtShipTo.setData("name", "txtShipTo");
		txtShipTo.setEnabled(false);
		
		Text txtDummy109 = new Text(pnlRoot, 72);
		txtDummy109.setLayoutData(gridData3);
		txtDummy109.setEnabled(false);
		
		 
		GridData gridDatax300 = new GridData();
		gridDatax300.widthHint = 10;
		gridDatax300.grabExcessHorizontalSpace = false;
		gridDatax300.horizontalAlignment = SWT.BEGINNING;
		gridDatax300.verticalAlignment = 4;
		gridDatax300.horizontalSpan = 1;
		
		Text txtDummy300 = new Text(pnlRoot, 72);
		txtDummy300.setLayoutData(gridDatax300);
		txtDummy300.setEnabled(false);
		 


		
		lblStoreId = new Label(pnlRoot, SWT.LEFT);
		lblStoreId.setText("Store ID:");
		lblStoreId.setLayoutData(gridData4);
		lblStoreId.setData("name", "lblStoreId");
		txtStoreId = new Text(pnlRoot, 72);
		txtStoreId.setLayoutData(gridData5);
		txtStoreId.setData("name", "txtStoreId");
		txtStoreId.setEnabled(false);
		 
		GridData gridDatax10 = new GridData();
		gridDatax10.widthHint = 100;
		gridDatax10.grabExcessHorizontalSpace = false;
		gridDatax10.horizontalAlignment = SWT.BEGINNING;
		gridDatax10.verticalAlignment = 4;
		gridDatax10.horizontalSpan = 1;
		
		Text txtDummy2010 = new Text(pnlRoot, 72);
		txtDummy2010.setLayoutData(gridDatax10);
		txtDummy2010.setEnabled(false);
		 	
		
		Text txtDummy112 = new Text(pnlRoot, 72);
		txtDummy112.setLayoutData(gridData6);
		txtDummy112.setEnabled(false);
		
		GridData gridData31 = new GridData();
		gridData31.widthHint = 80;
		gridData31.horizontalAlignment = SWT.NONE;
		gridData31.verticalAlignment = 4;
		
		Text txtDummy113 = new Text(pnlRoot, 72);
		txtDummy113.setLayoutData(gridData31);
		txtDummy113.setEnabled(false);
		
		//if(!isFromOrderEntryWizard()){
		btnViewOriginal = new Button(pnlRoot, 0);
		btnViewOriginal.setText("View Original");
		btnViewOriginal.setLayoutData(gridData8);
		btnViewOriginal.setData("name", "btnViewOriginal");
		btnViewOriginal.setVisible(false);
		
		btnViewOriginal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				viewOriginal();
				
			}
		});
		//}
		
		
		Text txtDummy106 = new Text(pnlRoot, 72);
		txtDummy106.setLayoutData(gridData1);
		txtDummy106.setEnabled(false);
			
			GridData gridDataM10 = new GridData();
			//gridData101.widthHint = 95;
			gridDataM10.horizontalAlignment = SWT.NONE;
			gridDataM10.verticalAlignment = 4;
			gridDataM10.horizontalSpan = 1;
			//gridData10.verticalIndent = 2;
			GridData gridDataShipToName = new GridData();
			gridDataShipToName.widthHint = 200;
			gridDataShipToName.horizontalAlignment = SWT.FILL;
			gridDataShipToName.verticalAlignment = 4;
			gridDataShipToName.horizontalSpan = 2;
			
			txtShipToName = new Text(pnlRoot, 72);
			txtShipToName.setLayoutData(gridDataShipToName);
			txtShipToName.setData("name", "txtShipToName");
			txtShipToName.setEnabled(false);
			
//			Text txtDummyx109 = new Text(pnlRoot, 72);
//			txtDummyx109.setLayoutData(gridData3);
			
			 
			GridData gridDatax302 = new GridData();
			gridDatax302.widthHint = 10;
			gridDatax302.grabExcessHorizontalSpace = false;
			gridDatax302.horizontalAlignment = SWT.BEGINNING;
			gridDatax302.verticalAlignment = 4;
			gridDatax302.horizontalSpan = 1;
			
			Text txtDummy302 = new Text(pnlRoot, 72);
			txtDummy302.setLayoutData(gridDatax302);
			txtDummy302.setEnabled(false);
			 


			
			
			Text txtDummy105 = new Text(pnlRoot, 72);
			txtDummy105.setLayoutData(gridData3);
			txtDummy105.setEnabled(false);
			
			Text txtDummy104 = new Text(pnlRoot, 72);
			txtDummy104.setLayoutData(gridData4);
			txtDummy104.setEnabled(false);
			
			Text txtDummy103 = new Text(pnlRoot, 72);
			txtDummy103.setLayoutData(gridData5);
			txtDummy103.setEnabled(false);
			
			
			Text txtDummy102 = new Text(pnlRoot, 72);
			txtDummy102.setLayoutData(gridData1);
			txtDummy102.setEnabled(false);

			
			Text txtDummy101 = new Text(pnlRoot, 72);
			txtDummy101.setLayoutData(gridData7);
			txtDummy101.setEnabled(false);
		
 		
		
		if(!isFromOrderEntryWizard()){
		lblWebConfirmationNo = new Label(pnlRoot, SWT.LEFT);
		lblWebConfirmationNo.setText("Web Conf. #:");
		lblWebConfirmationNo.setLayoutData(gridData1);
		lblWebConfirmationNo.setData("name", "lblWebConfirmationNo");
		
		GridData gridData10 = new GridData();
		//gridData101.widthHint = 95;
		gridData10.horizontalAlignment = SWT.NONE;
		gridData10.verticalAlignment = 4;
		gridData10.horizontalSpan = 1;
		//gridData10.verticalIndent = 2;
		
		txtWebConfirmationNo = new Text(pnlRoot, 72);
		txtWebConfirmationNo.setLayoutData(gridData2);
		txtWebConfirmationNo.setData("name", "txtWebConfirmationNo");
		txtWebConfirmationNo.setEnabled(false);
		
		Text txtWebConfirmationNo1 = new Text(pnlRoot, 72);
		txtWebConfirmationNo1.setLayoutData(gridData3);
		txtWebConfirmationNo1.setEnabled(false);
		//txtWebConfirmationNo1.setData("name", "txtWebConfirmationNo1");
		
		 
		GridData gridDatax303 = new GridData();
		gridDatax303.widthHint = 10;
		gridDatax303.grabExcessHorizontalSpace = false;
		gridDatax303.horizontalAlignment = SWT.BEGINNING;
		gridDatax303.verticalAlignment = 4;
		gridDatax303.horizontalSpan = 1;
		
		Text txtDummy303 = new Text(pnlRoot, 72);
		txtDummy303.setLayoutData(gridDatax303);
		txtDummy303.setEnabled(false);
		 


				
		lblOrderSource = new Label(pnlRoot, SWT.LEFT);
		lblOrderSource.setText("Order Source:");
		lblOrderSource.setLayoutData(gridData4);
		lblOrderSource.setData("name", "lblOrderSource");
		
		txtOrderSource = new Text(pnlRoot, 72);
		txtOrderSource.setLayoutData(gridData5);
		txtOrderSource.setData("name", "txtOrderSource");
		txtOrderSource.setEnabled(false);
		
		
		}
		else
		{
			Text txtEntryWebConfirmNo = new Text(pnlRoot,72);
			GridData gridDataE1 = new GridData();
			gridDataE1.widthHint = 95;
			gridDataE1.horizontalAlignment = SWT.NONE;
			gridDataE1.verticalAlignment = 4;
			gridDataE1.horizontalSpan = 10;
			txtEntryWebConfirmNo.setLayoutData(gridDataE1);
			txtEntryWebConfirmNo.setEnabled(false);
		}
		
		lblOrderType = new Label(pnlRoot, SWT.LEFT);
		lblOrderType.setText("Order Type:");
		lblOrderType.setLayoutData(gridData1);
		lblOrderType.setData("name", "lblOrderType");
		
		txtOrderType = new Text(pnlRoot, 72);
		txtOrderType.setLayoutData(gridData7);
		txtOrderType.setData("name", "txtOrderType");
		txtOrderType.setEnabled(false);
		
		 
		GridData gridDatax9 = new GridData();
		gridDatax9.widthHint = 10;
		gridDatax9.grabExcessHorizontalSpace = false;
		gridDatax9.horizontalAlignment = SWT.BEGINNING;
		gridDatax9.verticalAlignment = 4;
		gridDatax9.horizontalSpan = 1;
		
		Text txtDummy209 = new Text(pnlRoot, 72);
		txtDummy209.setLayoutData(gridDatax9);
		txtDummy209.setEnabled(false);
		 
				
		lblOrderNo = new Label(pnlRoot, SWT.LEFT);
		lblOrderNo.setText("Order#:");
		lblOrderNo.setLayoutData(gridData6);
		lblOrderNo.setData("name", "lblOrderNo");
		
		//Commented for 3528
		/*GridData gridData12 = new GridData();
		gridData12.widthHint = 95;
		gridData12.horizontalAlignment = SWT.NONE;
		gridData12.verticalAlignment = 4;
		gridData12.horizontalSpan = 4;*/
		//gridData12.verticalIndent = 2;
		
		txtOrderNo = new Text(pnlRoot, 72);
		txtOrderNo.setLayoutData(gridData2);
		txtOrderNo.setData("name", "txtOrderNo");
		txtOrderNo.setEnabled(false);
		
		 
		GridData gridDatax304 = new GridData();
		gridDatax304.widthHint = 10;
		gridDatax304.grabExcessHorizontalSpace = false;
		gridDatax304.horizontalAlignment = SWT.BEGINNING;
		gridDatax304.verticalAlignment = 4;
		gridDatax304.horizontalSpan = 1;
		
		Text txtDummy304 = new Text(pnlRoot, 72);
		txtDummy304.setLayoutData(gridDatax304);
		txtDummy304.setEnabled(false);
		 


		
		
		if(!isFromOrderEntryWizard()){
			 
			GridData gridDatax305 = new GridData();
			gridDatax305.widthHint = 10;
			gridDatax305.grabExcessHorizontalSpace = false;
			gridDatax305.horizontalAlignment = SWT.BEGINNING;
			gridDatax305.verticalAlignment = 4;
			gridDatax305.horizontalSpan = 1;
			
			Text txtDummy305 = new Text(pnlRoot, 72);
			txtDummy305.setLayoutData(gridDatax305);
			txtDummy305.setEnabled(false);
			 


			
		lblOrderDate = new Label(pnlRoot, SWT.LEFT);
		lblOrderDate.setText("Order Create Date:");
		lblOrderDate.setLayoutData(gridData4);
		lblOrderDate.setData("name", "lblOrderDate");
		txtOrderDate = new Text(pnlRoot, 72);
		txtOrderDate.setLayoutData(gridData5);
		txtOrderDate.setData("name", "txtOrderDate");
		txtOrderDate.setEnabled(false);
		
		}
		else
		{
			GridData gridDatax401 = new GridData();
			gridDatax401.widthHint = 10;
			gridDatax401.grabExcessHorizontalSpace = false;
			gridDatax401.horizontalAlignment = SWT.BEGINNING;
			gridDatax401.verticalAlignment = 4;
			gridDatax401.horizontalSpan = 1;
			
			Text txtDummy401 = new Text(pnlRoot, 72);
			txtDummy401.setLayoutData(gridDatax401);
			txtDummy401.setEnabled(false);
			
			Text txtEntryOrderDt = new Text(pnlRoot,72);
			GridData gridDataE1 = new GridData();
			gridDataE1.widthHint = 95;
			gridDataE1.horizontalAlignment = SWT.NONE;
			gridDataE1.verticalAlignment = 4;
			gridDataE1.horizontalSpan = 2;
			txtEntryOrderDt.setLayoutData(gridDataE1);
			txtEntryOrderDt.setEnabled(false);
		}
		
		
		if(!isFromOrderEntryWizard()){
			
			lblLegacyOrderType = new Label(pnlRoot, SWT.LEFT);
			lblLegacyOrderType.setText("Legacy Order Type:");
			lblLegacyOrderType.setLayoutData(gridData6);
			lblLegacyOrderType.setData("name", "lblLegacyOrderType");
					
			txtLegacyOrderType = new Text(pnlRoot, 72);
			txtLegacyOrderType.setLayoutData(gridData7);
			txtLegacyOrderType.setData("name", "txtLegacyOrderType");
			txtLegacyOrderType.setEnabled(false);
			
			 
			GridData gridDatax8 = new GridData();
			gridDatax8.widthHint = 10;
			gridDatax8.grabExcessHorizontalSpace = false;
			gridDatax8.horizontalAlignment = SWT.BEGINNING;
			gridDatax8.verticalAlignment = 4;
			gridDatax8.horizontalSpan = 1;
			
			Text txtDummy208 = new Text(pnlRoot, 72);
			txtDummy208.setLayoutData(gridDatax8);
			txtDummy208.setEnabled(false);
			 
		
		
		}
		else
		{
			Text txtEntryLegacyOrderType = new Text(pnlRoot,72);
			GridData gridDataE1 = new GridData();
			gridDataE1.widthHint = 95;
			gridDataE1.horizontalAlignment = SWT.NONE;
			gridDataE1.verticalAlignment = 4;
			gridDataE1.horizontalSpan = 3;
			txtEntryLegacyOrderType.setLayoutData(gridDataE1);
			txtEntryLegacyOrderType.setEnabled(false);
		}
		
		
		GridData gridData14 = new GridData();
		gridData14.widthHint = 95;
		gridData14.horizontalAlignment = SWT.NONE;
		gridData14.verticalAlignment = 4;
		gridData14.horizontalSpan = 10;
		//gridData14.verticalIndent = 2;
		Text txtDummy = new Text(pnlRoot, 72);
		txtDummy.setLayoutData(gridData14);
		txtDummy.setEnabled(false);
		
		lblCustPONo = new Label(pnlRoot, SWT.LEFT);
		lblCustPONo.setText("Customer PO#:");
		lblCustPONo.setLayoutData(gridData1);
		lblCustPONo.setData("name", "lblCustPONo");
		GridData gridData16 = new GridData();
		gridData16.widthHint = 160;
		gridData16.heightHint = 20;
		gridData16.horizontalAlignment = SWT.BEGINNING;
		//gridData16.verticalAlignment = 4;
		gridData16.horizontalSpan = 2;
		//gridData16.verticalIndent = 2;
		GridData gridData161 = new GridData();
		gridData161.heightHint = 20;
		gridData161.horizontalAlignment = SWT.BEGINNING;
		//gridData16.verticalAlignment = 4;
		gridData161.horizontalSpan = 2;
		txtCustPONo = new Text(pnlRoot, SWT.BORDER);
		txtCustPONo.setLayoutData(gridData16);
		txtCustPONo.setData("yrc:customType","Text");
		txtCustPONo.setData("name", "txtCustPONo");
		txtCustPONo.setTextLimit(22);
		
		//Fix for 3528
		if(!isFromOrderEntryWizard()){
			 
			GridData gridDatax305 = new GridData();
			gridDatax305.widthHint = 10;
			gridDatax305.grabExcessHorizontalSpace = false;
			gridDatax305.horizontalAlignment = SWT.BEGINNING;
			gridDatax305.verticalAlignment = 4;
			gridDatax305.horizontalSpan = 1;
			
			Text txtDummy305 = new Text(pnlRoot, 72);
			txtDummy305.setLayoutData(gridDatax305);
			txtDummy305.setEnabled(false);
			
		
		lblOrderTime = new Label(pnlRoot, SWT.LEFT);
		lblOrderTime.setText("Order Create Time:");
		lblOrderTime.setLayoutData(gridData4);
		lblOrderTime.setData("name", "lblOrderTime");
		txtOrderTime = new Text(pnlRoot, 72);
		txtOrderTime.setLayoutData(gridData5);
		txtOrderTime.setData("name", "txtOrderTime");
		txtOrderTime.setEnabled(false);
		}
		else
		{
			GridData gridDatax401 = new GridData();
			gridDatax401.widthHint = 10;
			gridDatax401.grabExcessHorizontalSpace = false;
			gridDatax401.horizontalAlignment = SWT.BEGINNING;
			gridDatax401.verticalAlignment = 4;
			gridDatax401.horizontalSpan = 1;
			
			Text txtDummy401 = new Text(pnlRoot, 72);
			txtDummy401.setLayoutData(gridDatax401);
			txtDummy401.setEnabled(false);
			
			Text txtEntryOrderDt = new Text(pnlRoot,72);
			GridData gridDataE1 = new GridData();
			gridDataE1.widthHint = 95;
			gridDataE1.horizontalAlignment = SWT.NONE;
			gridDataE1.verticalAlignment = 4;
			gridDataE1.horizontalSpan = 2;
			txtEntryOrderDt.setLayoutData(gridDataE1);
			txtEntryOrderDt.setEnabled(false);
		}
		

		lblTotOrderFreight = new Label(pnlRoot, SWT.LEFT);
		lblTotOrderFreight.setText("Total Order Freight:");
		lblTotOrderFreight.setLayoutData(gridData6);
		lblTotOrderFreight.setData("name", "lblTotOrderFreight");
		txtTotOrderFreight = new Text(pnlRoot, 72);
		txtTotOrderFreight.setLayoutData(gridData7);
		txtTotOrderFreight.setData("name", "txtTotOrderFreight");
		txtTotOrderFreight.setEnabled(false);
		
		 
		GridData gridDatax7 = new GridData();
		gridDatax7.widthHint = 10;
		gridDatax7.grabExcessHorizontalSpace = false;
		gridDatax7.horizontalAlignment = SWT.BEGINNING;
		gridDatax7.verticalAlignment = 4;
		gridDatax7.horizontalSpan = 1;
		
		Text txtDummy207 = new Text(pnlRoot, 72);
		txtDummy207.setLayoutData(gridDatax7);
		txtDummy207.setEnabled(false);
		 
		
		if(!isFromOrderEntryWizard()){
		lblOrderStatus = new Label(pnlRoot, SWT.LEFT);
		lblOrderStatus.setText("Order Status:");
		lblOrderStatus.setLayoutData(gridData1);
		lblOrderStatus.setData("name", "lblOrderStatus");
		txtOrderStatus = new Text(pnlRoot, 72);
		txtOrderStatus.setLayoutData(gridData2);
		txtOrderStatus.setData("name", "txtOrderStatus");
		txtOrderStatus.setEnabled(false);
		txtOrderSttsComment = new Text(pnlRoot, 72);
		txtOrderSttsComment.setLayoutData(gridData3);
		txtOrderSttsComment.setData("name", "txtOrderSttsComment");
		txtOrderSttsComment.setEnabled(false);
		
		
		}
		else
		{
			Text txtEntryOrderStatus = new Text(pnlRoot,72);
			GridData gridDataE1 = new GridData();
			gridDataE1.widthHint = 95;
			gridDataE1.horizontalAlignment = SWT.NONE;
			gridDataE1.verticalAlignment = 4;
			gridDataE1.horizontalSpan = 3;
			txtEntryOrderStatus.setLayoutData(gridDataE1);
			txtEntryOrderStatus.setEnabled(false);
		}

	
		 
		GridData gridDatax306 = new GridData();
		gridDatax306.widthHint = 10;
		gridDatax306.grabExcessHorizontalSpace = false;
		gridDatax306.horizontalAlignment = SWT.BEGINNING;
		gridDatax306.verticalAlignment = 4;
		gridDatax306.horizontalSpan = 1;
		
		Text txtDummy306 = new Text(pnlRoot, 72);
		txtDummy306.setLayoutData(gridDatax306);
		txtDummy306.setEnabled(false);
		 
		
		lblShipDate = new Label(pnlRoot, SWT.LEFT);
		lblShipDate.setText("Ship Date:");
		lblShipDate.setLayoutData(gridData4);
		lblShipDate.setData("name", "lblShipDate");		
		
		Composite pnlShipDate = new Composite(pnlRoot,SWT.NONE);
		GridLayout pnlShipDateLayout = new GridLayout(2,false);
		pnlShipDateLayout.verticalSpacing = 1;
		pnlShipDateLayout.marginWidth = 0;
		pnlShipDate.setLayout(pnlShipDateLayout);
		
		GridData pnlShipDateLayoutData = new GridData();
		pnlShipDateLayoutData.horizontalAlignment = SWT.BEGINNING;
		pnlShipDate.setLayoutData(pnlShipDateLayoutData);
		pnlShipDate.setLayout(pnlShipDateLayout);
		
		
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = SWT.BEGINNING;
		gridData10.widthHint = 60;
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = SWT.BEGINNING;
		gridData11.widthHint = 10;
		GridData gridDataImg = new GridData(SWT.BEGINNING);
		gridDataImg.heightHint = 19;
		gridDataImg.widthHint = 17;
		gridDataImg.horizontalSpan = 1;
		gridDataImg.grabExcessHorizontalSpace = false;
		gridDataImg.verticalAlignment = 2;
		
		txtShipDate = new Text(pnlShipDate, SWT.BORDER);
		txtShipDate.setTextLimit(35);
		txtShipDate.setLayoutData(gridData10);
		txtShipDate.setData("name", "txtZipCode");
		btnShipDateLookup = new Button(pnlShipDate, 8);
		btnShipDateLookup.setImage(YRCPlatformUI.getImage("DateLookup"));
		btnShipDateLookup.setCursor(new Cursor(btnShipDateLookup.getDisplay(), 21));
		btnShipDateLookup.setLayoutData(gridDataImg);
		btnShipDateLookup.setToolTipText(YRCPlatformUI.getString("Ship_Date"));
		btnShipDateLookup.setData("name", "btnShipDateLookup");
		btnShipDateLookup.addSelectionListener(new SelectionAdapter() {
      	public void widgetSelected(SelectionEvent e) {
      		YRCPlatformUI.showCalendar(txtShipDate);
      	}
		});
		
		
		
		lblTotOrderTax = new Label(pnlRoot, SWT.LEFT);
		lblTotOrderTax.setText("Total Order Tax:");
		lblTotOrderTax.setLayoutData(gridData6);
		lblTotOrderTax.setData("name", "lblTotOrderTax");
		txtTotOrderTax = new Text(pnlRoot, 72);
		txtTotOrderTax.setLayoutData(gridData7);
		txtTotOrderTax.setData("name", "txtTotOrderTax");
		txtTotOrderTax.setEnabled(false);
		
		
		 
		GridData gridDatax6 = new GridData();
		gridDatax6.widthHint = 10;
		gridDatax6.grabExcessHorizontalSpace = false;
		gridDatax6.horizontalAlignment = SWT.BEGINNING;
		gridDatax6.verticalAlignment = 4;
		gridDatax6.horizontalSpan = 1;
		
		Text txtDummy206 = new Text(pnlRoot, 72);
		txtDummy206.setLayoutData(gridDatax6);
		txtDummy206.setEnabled(false);
		 	
       			
		lblOrderHoldCode = new Label(pnlRoot, SWT.LEFT);
		lblOrderHoldCode.setText("Order Hold code:");
		lblOrderHoldCode.setLayoutData(gridData1);
		lblOrderHoldCode.setData("name", "lblOrderHoldCode");

		chkOrderHoldFlag = new Button(pnlRoot, SWT.CHECK);
		chkOrderHoldFlag.setText("");
		chkOrderHoldFlag.setVisible(true);
		chkOrderHoldFlag.setData("yrc:customType", "Label");
		chkOrderHoldFlag.setLayoutData(gridData161);
		chkOrderHoldFlag.setData("name", "chkOrderHoldFlag");
		
		GridData gridDatax307 = new GridData();
		gridDatax307.widthHint = 10;
		gridDatax307.grabExcessHorizontalSpace = false;
		gridDatax307.horizontalAlignment = SWT.BEGINNING;
		gridDatax307.verticalAlignment = 4;
		gridDatax307.horizontalSpan = 1;
		
		Text txtDummy307 = new Text(pnlRoot, 72);
		txtDummy307.setLayoutData(gridDatax307);
		txtDummy307.setEnabled(false);
		 


		
    	lblShipComplete = new Label(pnlRoot, SWT.LEFT);
        lblShipComplete.setText("Ship_Complete");
        lblShipComplete.setLayoutData(gridData4);
        lblShipComplete.setData("name", "lblShipComplete");
		/*chkShipComplete = new Button(pnlRoot, SWT.CHECK);
		chkShipComplete.setText("");
//		chkShipComplete.setVisible(true);
		chkShipComplete.setData("yrc:customType", "Label");
		chkShipComplete.setLayoutData(gridData5);
		chkShipComplete.setData("name", "chkShipComplete");*/
        comboShipComplete = new Combo(pnlRoot, 8);
		comboShipComplete.setLayoutData(gridData5);
		comboShipComplete.setData("name", "comboShipComplete");
		
		lblShippedValue = new Label(pnlRoot, SWT.LEFT);
		lblShippedValue.setText("Total Shippable Value:");
		lblShippedValue.setLayoutData(gridData6);
		lblShippedValue.setData("name", "lblShippedValue");
		txtShippedValue = new Text(pnlRoot, 72);
		txtShippedValue.setLayoutData(gridData7);
		txtShippedValue.setData("name", "txtShippedValue");
		txtShippedValue.setEnabled(false);
		
		 
		GridData gridDatax4 = new GridData();
		gridDatax4.widthHint = 10;
		gridDatax4.grabExcessHorizontalSpace = false;
		gridDatax4.horizontalAlignment = SWT.BEGINNING;
		gridDatax4.verticalAlignment = 4;
		gridDatax4.horizontalSpan = 1;
		
		Text txtDummy204 = new Text(pnlRoot, 72);
		txtDummy204.setLayoutData(gridDatax4);
		txtDummy204.setEnabled(false);
		 
					
		if(!isFromOrderEntryWizard()){
		lblWebHoldFlag = new Label(pnlRoot, SWT.LEFT);
		lblWebHoldFlag.setText("Web Hold Flag:");
		lblWebHoldFlag.setLayoutData(gridData1);
		lblWebHoldFlag.setData("name", "lblWebHoldFlag");
		
		chkWebHoldFlag = new Button(pnlRoot, SWT.CHECK);
		chkWebHoldFlag.setText("");
		chkWebHoldFlag.setVisible(true);
		chkWebHoldFlag.setData("yrc:customType", "Label");
		chkWebHoldFlag.setLayoutData(gridData161);
		chkWebHoldFlag.setData("name", "chkWebHoldFlag");
		}
		else
		{
			Text txtEntryWebHold = new Text(pnlRoot,72);
			GridData gridDataE1 = new GridData();
			gridDataE1.widthHint = 95;
			gridDataE1.horizontalAlignment = SWT.NONE;
			gridDataE1.verticalAlignment = 4;
			gridDataE1.horizontalSpan = 3;
			txtEntryWebHold.setLayoutData(gridDataE1);
			txtEntryWebHold.setEnabled(false);
		}
		
		 
		GridData gridDatax308 = new GridData();
		gridDatax308.widthHint = 10;
		gridDatax308.grabExcessHorizontalSpace = false;
		gridDatax308.horizontalAlignment = SWT.BEGINNING;
		gridDatax308.verticalAlignment = 4;
		gridDatax308.horizontalSpan = 1;
		
		Text txtDummy308 = new Text(pnlRoot, 72);
		txtDummy308.setLayoutData(gridDatax308);
		txtDummy308.setEnabled(false);
		 


		
		lblWillCall = new Label(pnlRoot, SWT.LEFT);
		lblWillCall.setText("Will Call:");
		lblWillCall.setLayoutData(gridData4);
		lblWillCall.setData("name", "lblWillCall");
		chkWillCall = new Button(pnlRoot, SWT.CHECK);
		chkWillCall.setText("");
		chkWillCall.setVisible(true);
		chkWillCall.setData("yrc:customType", "Label");
		chkWillCall.setLayoutData(gridData51);
		chkWillCall.setData("name", "chkWillCall");
		
		lblTotalAmount = new Label(pnlRoot, SWT.LEFT);
		lblTotalAmount.setText("Total Order Value:");
		lblTotalAmount.setLayoutData(gridData6);
		lblTotalAmount.setData("name", "lblTotalAmount");
		txtTotalAmount = new Text(pnlRoot, 72);
		txtTotalAmount.setLayoutData(gridData7);
		txtTotalAmount.setData("name", "txtTotalAmount");
		txtTotalAmount.setEnabled(false);
		
		 
		GridData gridDatax2 = new GridData();
		gridDatax2.widthHint = 10;
		gridDatax2.grabExcessHorizontalSpace = false;
		gridDatax2.horizontalAlignment = SWT.BEGINNING;
		gridDatax2.verticalAlignment = 4;
		gridDatax2.horizontalSpan = 1;
		
		Text txtDummy203 = new Text(pnlRoot, 72);
		txtDummy203.setLayoutData(gridDatax2);
		txtDummy203.setEnabled(false);
		 
		
		lblHdrComments = new Label(pnlRoot, SWT.LEFT);
		lblHdrComments.setText("Header Comments:");
		lblHdrComments.setLayoutData(gridData1);
		lblHdrComments.setData("name", "lblHdrComments");
		
		GridData gridData20 = new GridData();
		gridData20.widthHint = 150;
		gridData20.horizontalAlignment = SWT.FILL;
		gridData20.grabExcessHorizontalSpace = false;
		gridData20.verticalAlignment = 4;
		gridData20.horizontalSpan = 2;
		gridData20.verticalSpan = 2;
		
		txtHdrComments = new Text(pnlRoot, SWT.BORDER|SWT.WRAP|SWT.TAB);
		txtHdrComments.setLayoutData(gridData20);
		txtHdrComments.setTextLimit(250);//According to JIRA - 1354
		txtHdrComments.setData("name", "txtHdrComments");
		
		
		
		//if(!isFromOrderEntryWizard()){
		 
		GridData gridDatax309 = new GridData();
		gridDatax309.widthHint = 10;
		gridDatax309.grabExcessHorizontalSpace = false;
		gridDatax309.horizontalAlignment = SWT.BEGINNING;
		gridDatax309.verticalAlignment = 4;
		gridDatax309.horizontalSpan = 1;
		
		Text txtDummy309 = new Text(pnlRoot, 72);
		txtDummy309.setLayoutData(gridDatax309);
		txtDummy309.setData("name","txtDummy309");
		txtDummy309.setEnabled(false);
		 


		
		lblShipFromDiv = new Label(pnlRoot, SWT.LEFT);
		lblShipFromDiv.setText("Ship From Division:");
		lblShipFromDiv.setLayoutData(gridData4);
		lblShipFromDiv.setData("name", "lblShipFromDiv");
		comboShipFromDiv = new Combo(pnlRoot, 8);
		comboShipFromDiv.setLayoutData(gridData5);
		comboShipFromDiv.setData("name", "comboShipFromDiv");
		
		lblLineCount = new Label(pnlRoot, SWT.LEFT);
		lblLineCount.setText("Line Count:");
		lblLineCount.setLayoutData(gridData6);
		lblLineCount.setData("name", "lblLineCount");
				
		Composite pnlLineCount = new Composite(pnlRoot,SWT.NONE);
		GridLayout pnlLineCountLayout = new GridLayout(4,false);
		pnlShipDateLayout.verticalSpacing = 1;
		pnlShipDateLayout.marginWidth = 0;
		pnlLineCount.setLayout(pnlLineCountLayout);
		
		GridData pnlLineCountLayoutData = new GridData();
		pnlLineCountLayoutData.horizontalAlignment = SWT.BEGINNING;
		//pnlLineCountLayoutData.horizontalSpan = 2;
		pnlLineCount.setLayoutData(pnlLineCountLayoutData);
		pnlLineCount.setLayout(pnlLineCountLayout);
		
		
		GridData gridData40 = new GridData();
		gridData40.horizontalAlignment = SWT.BEGINNING;
		gridData40.widthHint = 30;
		GridData gridData41 = new GridData();
		gridData41.horizontalAlignment = SWT.BEGINNING;
		gridData41.widthHint = 15;
		GridData gridData42 = new GridData();
		gridData42.horizontalAlignment = SWT.BEGINNING;
		gridData42.widthHint = 50;
		GridData gridData43 = new GridData();
		gridData43.horizontalAlignment = SWT.BEGINNING;
		gridData43.widthHint = 15;
		
		Label lblItem = new Label(pnlLineCount, SWT.LEFT);
		lblItem.setText("Item:");
		lblItem.setLayoutData(gridData40);
		lblItem.setData("name", "lblItem");
		txtLineCount = new Text(pnlLineCount, 72);
		txtLineCount.setLayoutData(gridData41);
		txtLineCount.setData("name", "txtLineCount");
		txtLineCount.setEnabled(false);
		Label lblOther = new Label(pnlLineCount, SWT.LEFT);
		lblOther.setText("Other:");
		lblOther.setLayoutData(gridData42);
		lblOther.setData("name", "lblOther");
		txtOtherCount = new Text(pnlLineCount, 72);
		txtOtherCount.setLayoutData(gridData43);
		txtOtherCount.setData("name", "txtOtherCount");
		txtOtherCount.setEnabled(false);
		
		 
		GridData gridDatax = new GridData();
		gridDatax.widthHint = 10;
		gridDatax.grabExcessHorizontalSpace = false;
		gridDatax.horizontalAlignment = SWT.BEGINNING;
		gridDatax.verticalAlignment = 4;
		gridDatax.horizontalSpan = 1;
		
		Text txtDummy201 = new Text(pnlRoot, 72);
		txtDummy201.setLayoutData(gridDatax);
		txtDummy201.setData("name","txtDummy201");
		txtDummy201.setEnabled(false);
		 

		//}
//		else
//		{
//			Text txtEntryShipFromDiv = new Text(pnlRoot,72);
//			GridData gridDataE1 = new GridData();
//			gridDataE1.widthHint = 95;
//			gridDataE1.horizontalAlignment = SWT.NONE;
//			gridDataE1.verticalAlignment = 4;
//			gridDataE1.horizontalSpan = 4;
//			txtEntryShipFromDiv.setLayoutData(gridDataE1);
//		}
		
		
		 
		GridData gridDatax310 = new GridData();
		gridDatax310.widthHint = 10;
		gridDatax310.grabExcessHorizontalSpace = false;
		gridDatax310.horizontalAlignment = SWT.BEGINNING;
		gridDatax310.verticalAlignment = 4;
		gridDatax310.horizontalSpan = 1;
		
		Text txtDummy310 = new Text(pnlRoot, 72);
		txtDummy310.setLayoutData(gridDatax310);
		txtDummy310.setData("name","txtDummy310");
		txtDummy310.setEnabled(false);
		 


		
//		Text txtDummy1 = new Text(pnlRoot,72);
		GridData gridData17 = new GridData();
		gridData17.widthHint = 95;
		gridData17.horizontalAlignment = SWT.NONE;
		gridData17.verticalAlignment = 4;
		gridData17.horizontalSpan = 2;
		//gridData17.verticalIndent = 2;
//		txtDummy1.setLayoutData(gridData17);
//		txtDummy1.setData("name","txtDummy1");
		 
		GridData gridDatax1 = new GridData();
		gridDatax1.widthHint = 10;
		gridDatax1.grabExcessHorizontalSpace = false;
		gridDatax1.horizontalAlignment = SWT.BEGINNING;
		gridDatax1.verticalAlignment = 4;
		gridDatax1.horizontalSpan = 1;
		
		Text txtDummy202 = new Text(pnlRoot, 72);
		txtDummy202.setLayoutData(gridDatax);
		txtDummy202.setData("name","txtDummy202");
		txtDummy202.setEnabled(false);
		
		GridData gridDatax25 = new GridData();
		gridDatax25.widthHint = 10;
		gridDatax25.grabExcessHorizontalSpace = false;
		gridDatax25.horizontalAlignment = SWT.BEGINNING;
		gridDatax25.verticalAlignment = 4;
		gridDatax25.horizontalSpan = 3;
		
		Text txtDummy25 = new Text(pnlRoot, 72);
		txtDummy25.setLayoutData(gridDatax25);
		txtDummy25.setData("name","txtDummy25");
		txtDummy25.setEnabled(false);
		 
		
		lblCurrencyCode = new Label(pnlRoot, SWT.LEFT);
		lblCurrencyCode.setText("Currency Code:");
		lblCurrencyCode.setLayoutData(gridData6);
		lblCurrencyCode.setData("name", "lblCurrencyCode");
		txtCurrencyCode = new Text(pnlRoot, 72);
		txtCurrencyCode.setLayoutData(gridData7);
		txtCurrencyCode.setData("name", "txtCurrencyCode");
		txtCurrencyCode.setEnabled(false);
		
		
		GridData gridData18 = new GridData();
		gridData18.widthHint = 130;
		gridData18.horizontalAlignment = SWT.NONE;
		gridData18.verticalAlignment = 4;
		gridData18.horizontalSpan = 10;
		
		lblInternalComments = new Label(pnlRoot, SWT.LEFT);
		lblInternalComments.setText("Internal Comments:");
		lblInternalComments.setLayoutData(gridData18);
		lblInternalComments.setData("name", "lblInternalComments");
		
		
		Text txtDummyInternal = new Text(pnlRoot,72);
		txtDummyInternal.setLayoutData(gridData1);
		txtDummyInternal.setData("name","txtDummyInternal");
		txtDummyInternal.setEnabled(false);
		txtInternalComments = new Text(pnlRoot, SWT.BORDER|SWT.WRAP|SWT.TAB);
		txtInternalComments.setLayoutData(gridData20);
		txtInternalComments.setData("name", "txtInternalComments");
		//Added for JIRA 3910
		txtInternalComments.setEditable(true);
		txtInternalComments.setTextLimit(250);
		
		 
		GridData gridDatax311 = new GridData();
		gridDatax311.widthHint = 10;
		gridDatax311.grabExcessHorizontalSpace = false;
		gridDatax311.horizontalAlignment = SWT.BEGINNING;
		gridDatax311.verticalAlignment = 4;
		gridDatax311.horizontalSpan = 3;
		
		Text txtDummy311 = new Text(pnlRoot, 72);
		txtDummy311.setLayoutData(gridDatax311);
		txtDummy311.setData("name","txtDummy311");
		txtDummy311.setEnabled(false);
		 


				
//		Text txtDummy2 = new Text(pnlRoot,72);
//		txtDummy2.setLayoutData(gridData17);
//		txtDummy2.setData("name","txtDummy2");
		
		GridData gridData25 = new GridData();
		gridData25.widthHint = 180;
		gridData25.horizontalAlignment = SWT.NONE;
		gridData25.verticalAlignment = 4;
		gridData25.horizontalSpan = 2;
		
		lblOrderedBy = new Label(pnlRoot, SWT.LEFT);
		lblOrderedBy.setText("Order By:");
		lblOrderedBy.setLayoutData(gridData6);
		lblOrderedBy.setData("name", "lblOrderedBy");
		txtOrderedBy = new Text(pnlRoot, 72);
		txtOrderedBy.setLayoutData(gridData25);
		txtOrderedBy.setData("name", "txtOrderedBy");
		txtOrderedBy.setEnabled(false);
		
		 
		GridData gridDatax12 = new GridData();
		gridDatax12.widthHint = 10;
		gridDatax12.grabExcessHorizontalSpace = false;
		gridDatax12.horizontalAlignment = SWT.BEGINNING;
		gridDatax12.verticalAlignment = 4;
		gridDatax12.horizontalSpan = 1;
		
		Text txtDummy2012 = new Text(pnlRoot, 72);
		txtDummy2012.setLayoutData(gridDatax12);
		txtDummy2012.setData("name","txtDummy2012");
		txtDummy2012.setEnabled(false);
		 
		
		
		
		 
		GridData gridDatax312 = new GridData();
		gridDatax312.widthHint = 10;
		gridDatax312.grabExcessHorizontalSpace = false;
		gridDatax312.horizontalAlignment = SWT.BEGINNING;
		gridDatax312.verticalAlignment = 4;
		gridDatax312.horizontalSpan = 1;
		
		Text txtDummy312 = new Text(pnlRoot, 72);
		txtDummy312.setLayoutData(gridDatax312);
		txtDummy312.setData("name","txtDummy312");
		txtDummy312.setEnabled(false);
		
		/*Text txtDummy3 = new Text(pnlRoot,72);
		txtDummy3.setLayoutData(gridData17);
		txtDummy3.setData("name","txtDummy3");*/
		 
		GridData gridDatax13 = new GridData();
		gridDatax13.widthHint = 10;
		gridDatax13.grabExcessHorizontalSpace = false;
		gridDatax13.horizontalAlignment = SWT.BEGINNING;
		gridDatax13.verticalAlignment = 4;
		gridDatax13.horizontalSpan = 3;
		
		Text txtDummy2013 = new Text(pnlRoot, 72);
		txtDummy2013.setLayoutData(gridDatax13);
		txtDummy2013.setData("name","txtDummy2013");
		txtDummy2013.setEnabled(false);
		
		lblOrderedByEmail = new Label(pnlRoot, SWT.LEFT);
		lblOrderedByEmail.setText("Order By Email:");
		lblOrderedByEmail.setLayoutData(gridData6);
		lblOrderedByEmail.setData("name", "lblOrderedByEmail");
		txtOrderedByEmail = new Text(pnlRoot, 72);
		txtOrderedByEmail.setLayoutData(gridData7);
		txtOrderedByEmail.setData("name", "txtOrderedByEmail");
		txtOrderedByEmail.setEnabled(false);
		
		
		
		
		GridData gridData22 = new GridData();
		gridData22.widthHint = 95;
		gridData22.horizontalAlignment = SWT.NONE;
		gridData22.verticalAlignment = 4;
		gridData22.horizontalSpan = 5;
		
		 
		GridData gridDatax313 = new GridData();
		gridDatax313.widthHint = 10;
		gridDatax313.grabExcessHorizontalSpace = false;
		gridDatax313.horizontalAlignment = SWT.BEGINNING;
		gridDatax313.verticalAlignment = 4;
		gridDatax313.horizontalSpan = 1;
		
		Text txtDummy313 = new Text(pnlRoot, 72);
		txtDummy313.setLayoutData(gridDatax313);
		txtDummy313.setData("name","txtDummy313");
		txtDummy313.setEnabled(false);

		
		Text txtDummy4 = new Text(pnlRoot,72);
		txtDummy4.setLayoutData(gridData22);
		txtDummy4.setData("name","txtDummy4");
		txtDummy4.setEnabled(false);
		 
		GridData gridDatax14 = new GridData();
		gridDatax14.widthHint = 10;
		gridDatax14.grabExcessHorizontalSpace = false;
		gridDatax14.horizontalAlignment = SWT.BEGINNING;
		gridDatax14.verticalAlignment = 4;
		gridDatax14.horizontalSpan = 1;
		
		Text txtDummy2014 = new Text(pnlRoot, 72);
		txtDummy2014.setLayoutData(gridDatax14);
		txtDummy2014.setEnabled(false);
		 
		
		lblApprovedBy = new Label(pnlRoot, SWT.LEFT);
		lblApprovedBy.setText("Approved By:");
		lblApprovedBy.setLayoutData(gridData6);
		lblApprovedBy.setData("name", "lblApprovedBy");
		txtApprovedBy = new Text(pnlRoot, 72);
		txtApprovedBy.setLayoutData(gridData7);
		txtApprovedBy.setData("name", "txtApprovedBy");
		txtApprovedBy.setEnabled(false);
		
		
		
		GridData gridData19 = new GridData();
		gridData19.widthHint = 115;
		gridData19.horizontalAlignment = SWT.NONE;
		gridData19.verticalAlignment = 4;
		gridData19.horizontalSpan = 2;
		//gridData19.verticalIndent = 2;
		lblBillToInfo = new Label(pnlRoot, SWT.LEFT);
		lblBillToInfo.setText("Bill To Information:");
		lblBillToInfo.setLayoutData(gridData19);
		lblBillToInfo.setData("name", "lblBillToInfo");
		
		lblShipToInfo = new Label(pnlRoot, SWT.LEFT);
		lblShipToInfo.setText("Ship To Information:");
		lblShipToInfo.setLayoutData(gridDataShipToInfoLable);
		lblShipToInfo.setData("name", "lblShipToInfo");
		
		 
		GridData gridDatax314 = new GridData();
		gridDatax314.widthHint = 10;
		gridDatax314.grabExcessHorizontalSpace = false;
		gridDatax314.horizontalAlignment = SWT.BEGINNING;
		gridDatax314.verticalAlignment = 4;
		gridDatax314.horizontalSpan = 1;
		
		Text txtDummy314 = new Text(pnlRoot, 72);
		txtDummy314.setLayoutData(gridDatax314);
		txtDummy314.setEnabled(false);
		 


		Text txtDummy5 = new Text(pnlRoot,72);
		txtDummy5.setLayoutData(gridData19);
		txtDummy5.setEnabled(false);
		
		lblApprovedDate = new Label(pnlRoot, SWT.LEFT);
		lblApprovedDate.setText("Approved Date:");
		lblApprovedDate.setLayoutData(gridData6);
		lblApprovedDate.setData("name", "lblApprovedDate");
		txtApprovedDate = new Text(pnlRoot, 72);
		txtApprovedDate.setLayoutData(gridData16);
		txtApprovedDate.setData("name", "txtApprovedDate");
		txtApprovedDate.setEnabled(false);
		
		 
		GridData gridDatax21 = new GridData();
		gridDatax21.widthHint = 10;
		gridDatax21.grabExcessHorizontalSpace = false;
		gridDatax21.horizontalAlignment = SWT.BEGINNING;
		gridDatax21.verticalAlignment = 4;
		gridDatax21.horizontalSpan = 1;
		
		Text txtDummy2021 = new Text(pnlRoot, 72);
		txtDummy201.setLayoutData(gridDatax21);
		txtDummy2021.setEnabled(false);
		 
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.BEGINNING;
		gridData.widthHint = 115;
		//gridData.grabExcessHorizontalSpace = false;
		//gridData.grabExcessVerticalSpace = true;
		gridData.heightHint = 100;
		gridData.widthHint = 200;
		gridData.verticalSpan = 30;
		gridData.horizontalSpan = 2;
		//gridData.verticalAlignment = 1;
		gridData.verticalAlignment = SWT.BEGINNING;
		
		GridData gridData30 = new GridData();
		gridData30.widthHint = 45;
		gridData30.horizontalAlignment = SWT.NONE;
		//gridData30.verticalAlignment = 4;
		gridData30.horizontalSpan = 1;
		gridData30.verticalIndent = 1;
		
		
		txtBillToAddress = new Text(pnlRoot, SWT.V_SCROLL|SWT.MULTI|SWT.WRAP|SWT.READ_ONLY|SWT.BORDER);
		txtBillToAddress.setLayoutData(gridData);
		txtBillToAddress.setData("yrc:customType", "Text");
		txtBillToAddress.setData("name", "txtBillToAddress");
		txtBillToAddress.setEnabled(false);
		
//		
//		Text txtDummyAddress = new Text(pnlRoot, 72);
//		txtDummyAddress.setLayoutData(gridData30);
//		txtDummyAddress.setData("yrc:customType", "Text");
//		//txtDummyAddress.setData("name", "txtDummyAddress");
//		txtDummyAddress.setVisible(true);
//				
//		txtShipToAddress = new Text(pnlRoot, SWT.V_SCROLL|SWT.MULTI|SWT.WRAP|SWT.READ_ONLY|SWT.BORDER);
//		txtShipToAddress.setLayoutData(gridData);
//		txtShipToAddress.setData("yrc:customType", "Text");
//		txtShipToAddress.setData("name", "txtShipToAddress");
		setShipToAddressUI();
		
		GridData gridData32 = new GridData();
		gridData32.widthHint = 95;
		gridData32.horizontalAlignment = SWT.NONE;
		//gridData30.verticalAlignment = 4;
		gridData32.horizontalSpan = 2;
		
		GridData gridData33 = new GridData();
		gridData33.widthHint = 95;
		gridData33.horizontalAlignment = SWT.NONE;
		//gridData30.verticalAlignment = 4;
		gridData33.horizontalSpan = 3;
		
		 
		GridData gridDatax316 = new GridData();
		gridDatax316.widthHint = 10;
		gridDatax316.grabExcessHorizontalSpace = false;
		gridDatax316.horizontalAlignment = SWT.BEGINNING;
		gridDatax316.verticalAlignment = 4;
		gridDatax316.horizontalSpan = 1;
		
		Text txtDummy316 = new Text(pnlRoot, 72);
		txtDummy316.setLayoutData(gridDatax316);
		txtDummy316.setEnabled(false);
		 


		
		Text txtDummy6 = new Text(pnlRoot,72); 
		txtDummy6.setLayoutData(gridData32);
		txtDummy6.setEnabled(false);
		
		if(!isFromOrderEntryWizard()){
			
			lblPCardNo = new Label(pnlRoot, SWT.LEFT);
			lblPCardNo.setText("PCard #:");
			lblPCardNo.setLayoutData(gridData6);
			lblPCardNo.setData("name", "lblPCardNo");
			txtPCardNo = new Text(pnlRoot, 72);
			//txtPCardNo.setLayoutData(gridData16);
			txtPCardNo.setLayoutData(gridData7);
			txtPCardNo.setData("name", "txtPCardNo");
			txtPCardNo.setEnabled(false);
			
			 
			GridData gridDatax15 = new GridData();
			gridDatax15.widthHint = 10;
			gridDatax15.grabExcessHorizontalSpace = false;
			gridDatax15.horizontalAlignment = SWT.BEGINNING;
			gridDatax15.verticalAlignment = 4;
			gridDatax15.horizontalSpan = 1;
			
			Text txtDummy2015 = new Text(pnlRoot, 72);
			txtDummy2015.setLayoutData(gridDatax15);
			txtDummy2015.setEnabled(false);
			 
			
		
		}
		else
		{
			
			Text txtDummy607 = new Text(pnlRoot,72); 
			txtDummy607.setLayoutData(gridData30);
			txtDummy607.setEnabled(false);
			Text txtDummy10 = new Text(pnlRoot,72); 
			txtDummy10.setLayoutData(gridData33);
			txtDummy10.setEnabled(false);
			
		}
		
		 
		GridData gridDatax317 = new GridData();
		gridDatax317.widthHint = 10;
		gridDatax317.grabExcessHorizontalSpace = false;
		gridDatax317.horizontalAlignment = SWT.BEGINNING;
		gridDatax317.verticalAlignment = 4;
		gridDatax317.horizontalSpan = 1;
		
		Text txtDummy317 = new Text(pnlRoot, 72);
		txtDummy317.setLayoutData(gridDatax317);
		txtDummy317.setEnabled(false);
		 


		
		Text txtDummy7 = new Text(pnlRoot,72); 
		txtDummy7.setLayoutData(gridData32);
		txtDummy7.setEnabled(false);
		
		if(!isFromOrderEntryWizard()){
						
		lblCardExpDt = new Label(pnlRoot, SWT.LEFT);
		lblCardExpDt.setText("Card Exp. Date:");
		lblCardExpDt.setLayoutData(gridData6);
		lblCardExpDt.setData("name", "lblCardExpDt");
		txtCardExpDt = new Text(pnlRoot, 72);
		//txtCardExpDt.setLayoutData(gridData16);
		txtCardExpDt.setLayoutData(gridData7);
		txtCardExpDt.setData("name", "txtCardExpDt");
		txtCardExpDt.setEnabled(false);
		
		GridData gridDatax16 = new GridData();
		gridDatax16.widthHint = 10;
		gridDatax16.grabExcessHorizontalSpace = false;
		gridDatax16.horizontalAlignment = SWT.BEGINNING;
		gridDatax16.verticalAlignment = 4;
		gridDatax16.horizontalSpan = 1;
		
		Text txtDummy2016 = new Text(pnlRoot, 72);
		txtDummy2016.setLayoutData(gridDatax16);
		txtDummy2016.setEnabled(false);
		}
		else
		{
			
			Text txtDummy605 = new Text(pnlRoot,72); 
			txtDummy605.setLayoutData(gridData30);
			txtDummy605.setEnabled(false);
			Text txtDummy10 = new Text(pnlRoot,72); 
			txtDummy10.setLayoutData(gridData33);
			txtDummy10.setEnabled(false);
		}
		
		 
		GridData gridDatax318 = new GridData();
		gridDatax318.widthHint = 10;
		gridDatax318.grabExcessHorizontalSpace = false;
		gridDatax318.horizontalAlignment = SWT.BEGINNING;
		gridDatax318.verticalAlignment = 4;
		gridDatax318.horizontalSpan = 1;
		
		Text txtDummy318 = new Text(pnlRoot, 72);
		txtDummy318.setLayoutData(gridDatax318);
		txtDummy318.setEnabled(false);
		 


		Text txtDummy8 = new Text(pnlRoot,72); 
		txtDummy8.setLayoutData(gridData32);
		txtDummy8.setEnabled(false);
		
		if(!isFromOrderEntryWizard()){
						
		lblCardType = new Label(pnlRoot, SWT.LEFT);
		lblCardType.setText("Card Type:");
		lblCardType.setLayoutData(gridData6);
		lblCardType.setData("name", "lblCardType");
		txtCardType = new Text(pnlRoot, 72);
		//txtCardType.setLayoutData(gridData16);
		txtCardType.setLayoutData(gridData7);
		txtCardType.setData("name", "txtCardType");
		txtCardType.setEnabled(false);
		
		GridData gridDatax17 = new GridData();
		gridDatax17.widthHint = 10;
		gridDatax17.grabExcessHorizontalSpace = false;
		gridDatax17.horizontalAlignment = SWT.BEGINNING;
		gridDatax17.verticalAlignment = 4;
		gridDatax17.horizontalSpan = 1;
		
		Text txtDummy2017 = new Text(pnlRoot, 72);
		txtDummy2017.setLayoutData(gridDatax17);
		txtDummy2017.setEnabled(false);
		}
		else
		{
			
			Text txtDummy603 = new Text(pnlRoot,72); 
			txtDummy603.setLayoutData(gridData30);
			txtDummy603.setEnabled(false);
			Text txtDummy10 = new Text(pnlRoot,72); 
			txtDummy10.setLayoutData(gridData33);
			txtDummy10.setEnabled(false);
		}
		
		 
		GridData gridDatax320 = new GridData();
		gridDatax320.widthHint = 10;
		gridDatax320.grabExcessHorizontalSpace = false;
		gridDatax320.horizontalAlignment = SWT.BEGINNING;
		gridDatax320.verticalAlignment = 4;
		gridDatax320.horizontalSpan = 1;
		
		Text txtDummy320 = new Text(pnlRoot, 72);
		txtDummy320.setLayoutData(gridDatax320);
		txtDummy320.setEnabled(false);
		 


		Text txtDummy9 = new Text(pnlRoot,72); 
		txtDummy9.setLayoutData(gridData32);
		txtDummy9.setEnabled(false);
		
		if(!isFromOrderEntryWizard()){
			 
		lblNameOnPcard = new Label(pnlRoot, SWT.LEFT);
		lblNameOnPcard.setText("Name on Card:");
		lblNameOnPcard.setLayoutData(gridData6);
		lblNameOnPcard.setData("name", "lblNameOnPcard");
		txtNameOnPcard = new Text(pnlRoot, 72);
		//txtNameOnPcard.setLayoutData(gridData16);
		txtNameOnPcard.setLayoutData(gridData7);
		txtNameOnPcard.setData("name", "txtNameOnPcard");
		txtNameOnPcard.setEnabled(false);
		
		GridData gridDatax18 = new GridData();
		gridDatax18.widthHint = 10;
		gridDatax18.grabExcessHorizontalSpace = false;
		gridDatax18.horizontalAlignment = SWT.BEGINNING;
		gridDatax18.verticalAlignment = 4;
		gridDatax18.horizontalSpan = 1;
		
		Text txtDummy2018 = new Text(pnlRoot, 72);
		txtDummy2018.setLayoutData(gridDatax18);
		txtDummy2018.setEnabled(false);
		}
		else
		{
			
			Text txtDummy601 = new Text(pnlRoot,72); 
			txtDummy601.setLayoutData(gridData30);
			txtDummy601.setEnabled(false);
			Text txtDummy10 = new Text(pnlRoot,72); 
			txtDummy10.setLayoutData(gridData33);
			txtDummy10.setEnabled(false);
			
			
		}
		
		 
		GridData gridDatax321 = new GridData();
		gridDatax321.widthHint = 10;
		gridDatax321.grabExcessHorizontalSpace = false;
		gridDatax321.horizontalAlignment = SWT.BEGINNING;
		gridDatax321.verticalAlignment = 4;
		gridDatax321.horizontalSpan = 1;
		
		Text txtDummy321 = new Text(pnlRoot, 72);
		txtDummy321.setLayoutData(gridDatax321);
		txtDummy321.setEnabled(false);
		 


		Text txtDummy10 = new Text(pnlRoot,72); 
		txtDummy10.setLayoutData(gridData32);
		txtDummy10.setEnabled(false);
				
		lblCoupon = new Label(pnlRoot, SWT.LEFT);
		lblCoupon.setText("Coupon");
		lblCoupon.setLayoutData(gridData6);
		lblCoupon.setData("name", "lblCoupon");
		txtCouponCode = new Text(pnlRoot, SWT.BORDER);
		//txtCouponCode.setLayoutData(gridData16 7);
		txtCouponCode.setLayoutData(gridDataCouponTxt);
		txtCouponCode.setData("name", "txtCouponCode");
		txtCouponCode.setTextLimit(12);
		
		GridData gridDatax19 = new GridData();
		gridDatax19.widthHint = 10;
		gridDatax19.grabExcessHorizontalSpace = false;
		gridDatax19.horizontalAlignment = SWT.BEGINNING;
		gridDatax19.verticalAlignment = 4;
		gridDatax19.horizontalSpan = 1;
		
		Text txtDummy2019 = new Text(pnlRoot, 72);
		txtDummy2019.setLayoutData(gridDatax19);
		txtDummy2019.setEnabled(false);
		
		if(XPXUtils.isFullFillmentOrder(this.eleOrderDetails)){
			txtCouponCode.setEnabled(false);
		}
		

		
		 
		
		
		
		
		
//		GridData gridData32 = new GridData();
//        gridData32.grabExcessHorizontalSpace = false;
//        gridData32.verticalAlignment = 1;
//        GridData gridData17 = new GridData();
//        gridData17.horizontalAlignment = 4;
//        gridData17.grabExcessHorizontalSpace = true;
//        gridData17.verticalAlignment = 1;
//        GridData gridData14 = new GridData();
//        gridData14.horizontalAlignment = 4;
//        gridData14.verticalAlignment = 4;
//        GridData gridData20 = new GridData();
//        gridData20.grabExcessHorizontalSpace = true;
//        gridData20.horizontalAlignment = 4;
//        gridData20.horizontalSpan = 3;
//        GridData gridData19 = new GridData();
//        gridData19.horizontalAlignment = 1;
//        gridData19.horizontalIndent = 0;
//        gridData19.verticalAlignment = 1;
//        GridData gridData16 = new GridData();
//        gridData16.horizontalAlignment = 1;
//        gridData16.verticalAlignment = 2;
//        GridData gridData15 = new GridData();
//        gridData15.horizontalAlignment = 1;
//        gridData15.horizontalIndent = 0;
//        gridData15.verticalAlignment = 2;
//        GridData gridData13 = new GridData();
//        gridData13.horizontalAlignment = 1;
//        gridData13.verticalAlignment = 2;
//        GridData gridData9 = new GridData();
//        gridData9.horizontalAlignment = SWT.FILL;
////        gridData9.grabExcessHorizontalSpace = true;
//        gridData9.horizontalIndent = 0;
//        gridData9.verticalAlignment = 2;
//        GridData gridData22 = new GridData();
//        gridData22.grabExcessHorizontalSpace = true;
//        gridData22.horizontalAlignment = 4;
//        gridData22.horizontalSpan = 3;
//        GridData gridData23 = new GridData();
//        gridData23.grabExcessHorizontalSpace = true;
//        gridData23.horizontalAlignment = 4;
//        GridData gridData7 = new GridData();
//        gridData7.horizontalAlignment = 4;
//        gridData7.grabExcessHorizontalSpace = true;
//        gridData7.verticalAlignment = 2;
//        gridData7.horizontalSpan = 3;
//        GridData gridData11 = new GridData();
//        gridData11.horizontalAlignment = 4;
//        gridData11.grabExcessHorizontalSpace = true;
//        gridData11.horizontalSpan = 1;//deleted
//        gridData11.grabExcessVerticalSpace = false;//true
//        gridData11.verticalAlignment = 4;
//        GridLayout gridLayout1 = new GridLayout(4,false);
//        gridLayout1.marginWidth = 4;//0
//        gridLayout1.verticalSpacing = 3;//1
//        gridLayout1.marginHeight = 3;//0
//        gridLayout1.horizontalSpacing = 5;
//        GridData gridData1 = new GridData();
//		gridData1.horizontalAlignment = 4;
//		gridData1.grabExcessHorizontalSpace = true;
//		gridData1.grabExcessVerticalSpace = true;
//		gridData1.verticalAlignment = 4;
        GridData gridDataLbl = new GridData();
        gridDataLbl.horizontalAlignment = SWT.FILL;
//        gridDataLbl.grabExcessHorizontalSpace=true;
        gridDataLbl.horizontalIndent = 0;
        gridDataLbl.verticalAlignment = 2;
        gridDataLbl.widthHint=130;
        gridDataLbl.horizontalSpan = 1;

	}
	
	private void setShipToAddressUI() {
		GridData gdAttnName = new GridData();
        gdAttnName.horizontalAlignment = SWT.BEGINNING;
        gdAttnName.verticalAlignment = 1;
      //adding or changes as part of bug#1600 - widthHint
        gdAttnName.widthHint = 233;

        GridData gridDatal00 = new GridData();
    	gridDatal00.horizontalAlignment = SWT.BEGINNING;
        gridDatal00.verticalAlignment = 1;
      //adding or changes as part of bug#1600 - widthHint & adding alignments
        gridDatal00.widthHint = 216;
        
		GridLayout gridDataPnl = new GridLayout();
		gridDataPnl.horizontalSpacing = 1;
		gridDataPnl.marginWidth = 1;
		gridDataPnl.marginHeight = 1;
		gridDataPnl.verticalSpacing = 1;
		gridDataPnl.numColumns = 1;
		
		Composite pnlRoottxtn = new Composite(pnlRoot, 0);
		pnlRoottxtn.setData("name", "pnlRoot");
		pnlRoottxtn.setLayout(gridDataPnl);
		
		GridData pnlGridData = new GridData();
		pnlGridData.horizontalAlignment = SWT.FILL;
//		pnlGridData.widthHint = 115;
		pnlGridData.verticalSpan = 5;
		pnlRoottxtn.setLayoutData(pnlGridData);
        
		
		
		txtShipToAddress = new Text(pnlRoottxtn, SWT.V_SCROLL|SWT.MULTI|SWT.WRAP|SWT.READ_ONLY|SWT.BORDER);
		txtShipToAddress.setLayoutData(gridDatal00);
		txtShipToAddress.setData("yrc:customType", "Text");
		txtShipToAddress.setData("name", "txtShipToAddress");
		txtShipToAddress.setEnabled(false);
		
		
		lblAttentionName = new Label(pnlRoottxtn, SWT.LEFT);
        lblAttentionName.setText("Attention_Name");
        lblAttentionName.setLayoutData(gdAttnName);
        lblAttentionName.setData("name", "lblAttentionName");
        
		txtAttentionName = new Text(pnlRoottxtn, 2048);
        txtAttentionName.setLayoutData(gdAttnName);
        txtAttentionName.setTextLimit(30);
        txtAttentionName.setData("name", "txtAttentionName");
	}
	

	
	
	public String getFormId() {
		return FORM_ID;
	}

	
	public String getHelpId() {
		return null;
	}

	
	public IYRCPanelHolder getPanelHolder() {
		return null;
	}

	
	public Composite getRootPanel() {
		return pnlRoot;
	}

	/*public void setCompositeWidths() {
		int newTotalWidth = 0;
		int parentWidth = parent.getSize().x;
		int controlsExcluded = 0;
		int controlsAddedByExtensibility = 0;
		Control al[] = pnlRoot.getChildren();
		for (int i = 0; i < al.length; i++) {
			if (!((String) al[i].getData("name")).startsWith("extn"))
				continue;
			// if (!(al[i] instanceof Group) && (al[i] instanceof Composite))
			// YCDScreenPanelUtils.addPanelBorder((Composite) al[i]);
			controlsAddedByExtensibility++;
		}

		for (int i = 0; i < al.length; i++)
			if (!((GridData) al[i].getLayoutData()).exclude)
				newTotalWidth += ((GridData) al[i].getLayoutData()).widthHint;
			else
				controlsExcluded++;

		if (controlsExcluded == 0 && controlsAddedByExtensibility == 0)
			return;
		newTotalWidth -= 40;
		int consSubtract = 0;
		if (controlsExcluded == 1)
			consSubtract = 125;
		else if (controlsExcluded == 2)
			consSubtract = 130;
		else if (controlsExcluded == 3)
			consSubtract = 140;
		else if (controlsExcluded == 4)
			consSubtract = 155;
		GridData gridData0 = null;
		for (int i = 0; i < al.length; i++) {
			gridData0 = (GridData) al[i].getLayoutData();
			if (gridData0.exclude || gridData0.widthHint == -1)
				continue;
			int pnlWidth = gridData0.widthHint;
			pnlWidth = (pnlWidth * (parentWidth - consSubtract)) / newTotalWidth;
			if (((String) al[i].getData("name")).startsWith("extn")) {
				GridData gd = (GridData) al[i].getLayoutData();
				gridData0 = new GridData();
				gridData0.horizontalAlignment = gd.horizontalAlignment;
				gridData0.grabExcessHorizontalSpace = gd.grabExcessHorizontalSpace;
				gridData0.verticalAlignment = gd.verticalAlignment;
				gridData0.widthHint = pnlWidth;
				gridData0.grabExcessVerticalSpace = gd.grabExcessVerticalSpace;
				al[i].setLayoutData(gridData0);
			} else {
				gridData0.widthHint = pnlWidth;
			}
		}

		pnlRoot.layout(true, true);
	}*/
	public boolean isDraftOrder() {
		String sMaxOrderStatus = YRCXmlUtils.getAttributeValue(orderEle,"/Order/@MaxOrderStatus");
		if(YRCPlatformUI.isVoid(sMaxOrderStatus)){
			return true;
		}
		if("1100".compareTo(sMaxOrderStatus)>0){
			return true;
		}else{
			return false;
		}
		
	}
	public OrderHeaderPanelBehavior getBehavior() {
		return myBehavior;
	}
	public OrderLinesPanel getOrderLinesPanel() {
		return pnlOrderLines;
	}
	public String getOldHdrComments() {
		return strOldHdrComments;
	}
	public String getOldInternalComments() {
		return strOldInternalComments;
	}
	public boolean isFromOrderEntryWizard(){
		return getOrderLinesPanel().getPageBehavior().isFromOrderEntryWizard();
	}
	private void viewOriginal() {
		myBehavior.viewOriginal();
	}
	private List getErrorValues(){
		List errorTextVal=getOrderLinesPanel().getPageBehavior().errList;
		return errorTextVal;
		
	}
	private boolean isReviewedByCSR() {
		boolean returnValue=getOrderLinesPanel().getPageBehavior().isCSRReviewing();
		return returnValue;
	}

		
}
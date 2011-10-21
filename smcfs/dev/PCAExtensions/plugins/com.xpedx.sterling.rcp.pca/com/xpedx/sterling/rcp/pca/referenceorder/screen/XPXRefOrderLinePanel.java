package com.xpedx.sterling.rcp.pca.referenceorder.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCStyledTextBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXRefOrderLinePanel extends Composite implements IYRCComposite {

	private Composite pnlRoot = null;
	private XPXRefOrderLinePanelBehavior myBehavior;
	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.referenceorder.screen.XPXRefOrderLinePanel";

	private Composite parent;
	private Object inputObject;
	private int lineNo;
	private XPXRefOrderLinePanel currentLinePnl;
	
	private Composite pnlPrice;
	private Composite pnlQuantity;
	private Text txtQuantity;
	private Composite pnlUOM;
	//private StyledText stxtPricingUOM;
	private Text stxtPricingUOM;
	private Text stxtUnitPrice;
	private Text txtOthrPrice;
	private Text txtLineNo;
	private Text txtItemDesc;
	public Text txtItemId;
	public Text txtOrderingUOM;
	private Text txtLineType;

	private int linePanelWidth;
	private Composite pnlItemDesc;
	private Composite LinePnl;
	private String sOrderLineKey;
	private Element eleOrderLine;
	private XPXReferenceOrderSummaryPanel pnlOrderLines;
	public Text txtCustItemId;
	private Text txtPricingUOM;
	private Composite pnlOptions;
	private Button btnResubmit;
	public Label lblErr;
	private FocusAdapter focusListenerLatest;
	
	public XPXRefOrderLinePanel(Composite parent, XPXReferenceOrderSummaryPanel pnlOrderLines, int style, Object inObj, int pnlNo, Element eleOrderLine) {
		super(parent, style);
		
		this.parent = parent;
		this.pnlOrderLines = pnlOrderLines;
		this.inputObject = inObj;
		this.eleOrderLine = eleOrderLine;
		lineNo = pnlNo;
		sOrderLineKey = YRCXmlUtils.getAttribute(eleOrderLine, "OrderLineKey");
		
		focusListenerLatest = new FocusAdapter(){
			private String strItemid;
			public void focusGained(FocusEvent e)
            {
                Widget ctrl = e.widget;
                String ctrlName = (String)ctrl.getData("name");
                if(ctrlName != null)
                {
                    if(YRCPlatformUI.equals(ctrlName, "txtCustItemId")){
                        strItemid = txtCustItemId.getText();            
                    }
               
                }
            }

            public void focusLost(FocusEvent e)
            {
                Widget ctrl = e.widget;

        		if(ctrl==txtCustItemId && !YRCPlatformUI.isVoid(txtCustItemId.getText())&& !YRCPlatformUI.equals(strItemid, txtCustItemId.getText()))
        		{
        			myBehavior.getLegacyItemNumberItemInfo(txtCustItemId.getText());
        		}
            }
		};

		
		initialize();
		setBindingForComponents();
		myBehavior = new XPXRefOrderLinePanelBehavior(this, FORM_ID, inputObject, eleOrderLine);
		
	}

	public XPXRefOrderLinePanelBehavior getBehavior() {
		return myBehavior;
	}

	private void initialize() {
		createRootPanel();
		currentLinePnl = this;
		currentLinePnl.setLayout(new FillLayout());
		setSize(new Point(470, 135));
	}

	private void createRootPanel() {
		GridLayout gridDataPnl = new GridLayout();
		gridDataPnl.horizontalSpacing = 0;
		gridDataPnl.marginWidth = 3;
		gridDataPnl.marginHeight = 3;
		gridDataPnl.verticalSpacing = 0;
		pnlRoot = new Composite(this, SWT.NONE);
		pnlRoot.setData("name", "pnlRoot");
		pnlRoot.setLayout(gridDataPnl);
		
		createLineComposite();
	}

	private void createLineComposite() {
		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = 4;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.verticalAlignment = 4;
		GridLayout gridLayout = new GridLayout(4,true);
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
//		gridLayout.numColumns = 4;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		LinePnl = new Composite(getRootPanel(), 0);
		LinePnl.setLayout(gridLayout);
		LinePnl.setLayoutData(gridDataPnl);
		LinePnl.setData("name", "LinePnl");
		LinePnl.setData("yrc:customType", "TaskComposite");
		XPXUtils.paintPanel(LinePnl);

		createItemComposite();
		createQtyComposite();
		createUOMComposite();
		createPriceComposite();
		createButtonComposite();
	}


	private void createButtonComposite() {
		// TODO Auto-generated method stub
		GridLayout gridLayoutPnl = new GridLayout();
		gridLayoutPnl.numColumns = 2;
		
		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = SWT.FILL;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.grabExcessVerticalSpace = true;
		gridDataPnl.horizontalSpan = 4;
		gridDataPnl.verticalAlignment = 2;


		pnlOptions = new Composite(LinePnl, 0);
		pnlOptions.setLayoutData(gridDataPnl);
		pnlOptions.setLayout(gridLayoutPnl);
		pnlOptions.setData("name", "pnlOptions");	
		XPXUtils.paintPanel(pnlOptions);		
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = SWT.END;
//		gridData1.grabExcessHorizontalSpace = true;
		
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = SWT.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		
		lblErr = new Label(pnlOptions, SWT.NONE);
		lblErr.setLayoutData(gridData2);
		lblErr.setData("name", "lblErr");
		lblErr.setData("yrc:customType", "RedText10");
		
		btnResubmit = new Button(pnlOptions, SWT.END);
		btnResubmit.setText("Resubmit");
		btnResubmit.setLayoutData(gridData1);
		btnResubmit.setToolTipText("Resubmit line");
		btnResubmit.setData("name", "btnResubmit");
		btnResubmit.setImage(YRCPlatformUI.getImage("Refresh"));
		btnResubmit.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.reprocessReferenceOrderForLine();
			}
		});
	}


	private void createItemComposite() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 1;
		gridLayout1.verticalSpacing = 1;
		gridLayout1.numColumns = 2;
		gridLayout1.marginWidth = 5;
		gridLayout1.marginHeight = 5;

		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = SWT.FILL;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.grabExcessVerticalSpace = true;
		
		// gridDataPnl.verticalSpan = 2;
//		gridDataPnl.widthHint = 140;
		gridDataPnl.verticalAlignment = 4;

		GridData gridDataLbl = new GridData();
		gridDataLbl.horizontalAlignment = SWT.BEGINNING;
		gridDataLbl.verticalAlignment = SWT.CENTER;
		gridDataLbl.grabExcessVerticalSpace = false;
		gridDataLbl.grabExcessHorizontalSpace = true;
		gridDataLbl.horizontalSpan = 1;
		gridDataLbl.verticalSpan = 1;
		gridDataLbl.minimumWidth = 100;

		GridData gridDataVal = new GridData();
		gridDataVal.horizontalAlignment = SWT.BEGINNING;
		gridDataVal.verticalAlignment = SWT.CENTER;
		gridDataVal.grabExcessVerticalSpace = false;
		gridDataVal.grabExcessHorizontalSpace = true;
		gridDataVal.verticalSpan = 1;
		gridDataVal.widthHint = 50;
		
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = SWT.BEGINNING;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = SWT.CENTER;
		gridData3.grabExcessVerticalSpace = false;
		gridData3.widthHint=50;
//		gridData3.heightHint=16;
		GridData gridData11 = new GridData();
		gridData11.horizontalSpan = 2;
		gridData11.widthHint = 120;
		gridData11.heightHint = 30;
		gridData11.horizontalAlignment = 4;
		gridData11.grabExcessHorizontalSpace = true;
		pnlItemDesc = new Composite(LinePnl, 0);
		pnlItemDesc.setLayoutData(gridDataPnl);
		pnlItemDesc.setLayout(gridLayout1);
		pnlItemDesc.setData("name", "pnlItemDesc");
		XPXUtils.paintPanel(pnlItemDesc);

		Label lblLineNo = new Label(pnlItemDesc, SWT.HORIZONTAL);
		lblLineNo.setText("Line No. # ");
		lblLineNo.setLayoutData(gridDataLbl);
		lblLineNo.setData("yrc:customType", "Label");
		lblLineNo.setData("name", "lblLineNo");

		txtLineNo = new Text(pnlItemDesc, 72);
		txtLineNo.setLayoutData(gridData3);
		txtLineNo.setData("name", "txtLineNo");
		
		Label lblItemId = new Label(pnlItemDesc, SWT.HORIZONTAL);
		lblItemId.setText("Item # ");
		lblItemId.setLayoutData(gridDataLbl);
		lblItemId.setData("yrc:customType", "Label");
		lblItemId.setData("name", "lblItemId");

		//txtItemId = new Text(pnlItemDesc, 72);
		txtItemId = new Text(pnlItemDesc, SWT.BORDER);
		txtItemId.setLayoutData(gridData3);
		txtItemId.setData("name", "txtItemId");
		if("Y".equals(YRCXmlUtils.getAttribute(eleOrderLine, "IsInvalidItemID")) 
				&& (
						(!YRCPlatformUI.isVoid(eleOrderLine.getAttribute("ItemID"))
								&& YRCPlatformUI.isVoid(eleOrderLine.getAttribute("CustomerProductCode")))
						|| (YRCPlatformUI.isVoid(eleOrderLine.getAttribute("ItemID"))
								&& YRCPlatformUI.isVoid(eleOrderLine.getAttribute("CustomerProductCode"))
					)))
		{
			txtItemId.setData("yrc:customType", "InvalidData");
			txtItemId.setToolTipText("Invalid Item");
		}
		
		Label lblCustItemId = new Label(pnlItemDesc, SWT.HORIZONTAL);
		lblCustItemId.setText("Customer Item # ");
		lblCustItemId.setLayoutData(gridDataLbl);
		lblCustItemId.setData("yrc:customType", "Label");
		lblCustItemId.setData("name", "lblCustItemId");

		txtCustItemId = new Text(pnlItemDesc, 2048);
		txtCustItemId.setLayoutData(gridData3);
		txtCustItemId.setData("name", "txtCustItemId");
		txtCustItemId.addFocusListener(focusListenerLatest);
		if("Y".equals(YRCXmlUtils.getAttribute(eleOrderLine, "IsInvalidItemID")) 
				&& (!YRCPlatformUI.isVoid(eleOrderLine.getAttribute("CustomerProductCode")) 
						|| (YRCPlatformUI.isVoid(eleOrderLine.getAttribute("ItemID"))
								&& YRCPlatformUI.isVoid(eleOrderLine.getAttribute("CustomerProductCode")))))
		{
			txtCustItemId.setData("yrc:customType", "InvalidData");
			txtCustItemId.setToolTipText("Invalid Customer Item");
		}
		
		Label lblItemDesc = new Label(pnlItemDesc, SWT.HORIZONTAL);
		lblItemDesc.setText("Description ");
		lblItemDesc.setLayoutData(gridDataLbl);
		lblItemDesc.setData("yrc:customType", "Label");
		lblItemDesc.setData("name", "lblItemDesc");
		txtItemDesc = new Text(pnlItemDesc, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.SCROLL_LINE | SWT.READ_ONLY);
		txtItemDesc.setText("");
		txtItemDesc.setLayoutData(gridData11);
		txtItemDesc.setEditable(false);
		txtItemDesc.setData("name", "txtItemDesc");

		Label lblLineType = new Label(pnlItemDesc, SWT.HORIZONTAL);
		lblLineType.setText("Line Type ");
		lblLineType.setLayoutData(gridDataLbl);
		lblLineType.setData("yrc:customType", "Label");
		lblLineType.setData("name", "lblLineType");
		txtLineType = new Text(pnlItemDesc, 72);
		txtLineType.setLayoutData(gridDataVal);
		txtLineType.setData("name", "txtLineType");
		
//		lblErr = new Label(pnlItemDesc, SWT.WRAP);
//		lblErr.setLayoutData(gridDataLbl);
//		lblErr.setData("yrc:customType", "RedText10");
		
		
	}

	private void createQtyComposite() {

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 1;
		gridLayout1.verticalSpacing = 1;
		gridLayout1.numColumns = 2;
		gridLayout1.marginWidth = 5;
		gridLayout1.marginHeight = 5;

		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = SWT.FILL;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.grabExcessVerticalSpace = true;
//		gridDataPnl.widthHint = 60;
		gridDataPnl.verticalAlignment = 4;
		
		GridData gridDataVal = new GridData();
		gridDataVal.horizontalAlignment = SWT.BEGINNING;
		gridDataVal.verticalAlignment = SWT.CENTER;
		gridDataVal.grabExcessVerticalSpace = false;
		gridDataVal.grabExcessHorizontalSpace = true;
		gridDataVal.horizontalSpan = 1;
		gridDataVal.verticalSpan = 1;
		gridDataVal.widthHint = 55;

		pnlQuantity = new Composite(LinePnl, 0);
		pnlQuantity.setLayout(gridLayout1);
		pnlQuantity.setLayoutData(gridDataPnl);
		pnlQuantity.setData("name", "pnlQuantity");
		XPXUtils.paintPanel(pnlQuantity);
		
		GridData gridDataLbl = new GridData();
		gridDataLbl.horizontalAlignment = SWT.BEGINNING;
		gridDataLbl.verticalAlignment = SWT.CENTER;
		gridDataLbl.grabExcessVerticalSpace = false;
		gridDataLbl.grabExcessHorizontalSpace = true;
		gridDataLbl.horizontalSpan = 1;
		gridDataLbl.verticalSpan = 1;
		gridDataLbl.minimumWidth = 90;
		gridDataLbl.verticalIndent = 1;
		
		Label lblReqQuantity = new Label(pnlQuantity, SWT.HORIZONTAL);
		lblReqQuantity.setText("Requested Quantity ");
		lblReqQuantity.setLayoutData(gridDataLbl);
		lblReqQuantity.setData("yrc:customType", "Label");
		lblReqQuantity.setData("name", "lblReqQuantity");
		txtQuantity = new Text(pnlQuantity, SWT.READ_ONLY);
		txtQuantity.setText("");
		txtQuantity.setLayoutData(gridDataVal);
		txtQuantity.setData("name", "txtQuantity");
		txtQuantity.setToolTipText(YRCPlatformUI.getString("Ordered_Qty_In_Req_UOM_Tooltip"));
	}

	private void createUOMComposite() {

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 5;
		gridLayout1.verticalSpacing = 2;
		gridLayout1.numColumns = 2;
		gridLayout1.marginWidth = 5;
		gridLayout1.marginHeight = 5;

		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = SWT.FILL;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.grabExcessVerticalSpace = true;
//		gridDataPnl.widthHint = 120;
		gridDataPnl.verticalAlignment = 4;
		
		pnlUOM = new Composite(LinePnl, 0);
		pnlUOM.setLayout(gridLayout1);
		pnlUOM.setLayoutData(gridDataPnl);
		pnlUOM.setData("name", "pnlUOM");
		XPXUtils.paintPanel(pnlUOM);

		GridData gridDataLbl = new GridData();
		gridDataLbl.horizontalAlignment = SWT.BEGINNING;
		gridDataLbl.verticalAlignment = SWT.CENTER;
		gridDataLbl.grabExcessVerticalSpace = false;
		gridDataLbl.grabExcessHorizontalSpace = true;
		gridDataLbl.horizontalSpan = 1;
		gridDataLbl.verticalSpan = 1;
		gridDataLbl.minimumWidth = 90;
		gridDataLbl.verticalIndent = 1;

		GridData gridDataVal = new GridData();
		gridDataVal.horizontalAlignment = SWT.BEGINNING;
		gridDataVal.verticalAlignment = SWT.CENTER;
		gridDataVal.grabExcessVerticalSpace = false;
		gridDataVal.grabExcessHorizontalSpace = true;
		gridDataVal.horizontalSpan = 1;
		gridDataVal.verticalSpan = 1;
		gridDataVal.widthHint = 50;
		gridDataVal.verticalIndent = 1;

		Label lblOrderingUOM = new Label(pnlUOM, SWT.HORIZONTAL);
		lblOrderingUOM.setText("Requested UOM ");
		lblOrderingUOM.setLayoutData(gridDataLbl);
		lblOrderingUOM.setData("yrc:customType", "Label");
		lblOrderingUOM.setData("name", "lblOrderingUOM");
		txtOrderingUOM = new Text(pnlUOM, 2048);
		txtOrderingUOM.setText("");
		txtOrderingUOM.setLayoutData(gridDataVal);
		txtOrderingUOM.setData("name", "txtOrderingUOM");
		if("Y".equals(YRCXmlUtils.getAttribute(eleOrderLine, "IsInvalidUOM")))
		{
			txtOrderingUOM.setData("yrc:customType", "InvalidData");
			txtOrderingUOM.setToolTipText("Invalid UOM");
		}

		
	}

	private void createPriceComposite() {

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 5;
		gridLayout1.verticalSpacing = 2;
		gridLayout1.numColumns = 2;
		gridLayout1.marginWidth = 5;
		gridLayout1.marginHeight = 5;

		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = SWT.FILL;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.grabExcessVerticalSpace = true;
//		gridDataPnl.widthHint = 120;
		gridDataPnl.verticalAlignment = 4;
		pnlPrice = new Composite(LinePnl, 0);
		pnlPrice.setLayoutData(gridDataPnl);
		pnlPrice.setLayout(gridLayout1);
		pnlPrice.setData("name", "pnlPrice");
		XPXUtils.paintPanel(pnlPrice);

		GridData gridDataLbl = new GridData();
		gridDataLbl.horizontalAlignment = SWT.BEGINNING;
		gridDataLbl.verticalAlignment = SWT.CENTER;
		gridDataLbl.grabExcessVerticalSpace = false;
		gridDataLbl.grabExcessHorizontalSpace = true;
		gridDataLbl.horizontalSpan = 1;
		gridDataLbl.verticalSpan = 1;
		gridDataLbl.widthHint = 90;
		gridDataLbl.verticalIndent = 1;
		
		GridData gridDataVal = new GridData();
		gridDataVal.horizontalAlignment = SWT.BEGINNING;
		gridDataVal.verticalAlignment = SWT.CENTER;
		gridDataVal.grabExcessVerticalSpace = false;
		gridDataVal.grabExcessHorizontalSpace = true;
		gridDataVal.horizontalSpan = 1;
		gridDataVal.verticalSpan = 1;
		gridDataVal.widthHint = 50;
		gridDataVal.verticalIndent = 1;
		gridDataVal.heightHint=19;
		Label lblPricingUOM = new Label(pnlPrice, SWT.HORIZONTAL);
		lblPricingUOM.setText("Pricing UOM ");
		lblPricingUOM.setLayoutData(gridDataLbl);
		lblPricingUOM.setData("yrc:customType", "Label");
		lblPricingUOM.setData("name", "lblPricingUOM");
		
		
		txtPricingUOM = new Text(pnlPrice, 2048);
		txtPricingUOM.setText("");
		txtPricingUOM.setLayoutData(gridDataVal);
		txtPricingUOM.setData("name", "txtPricingUOM");

//		stxtPricingUOM = new StyledText(pnlPrice, SWT.SIMPLE);
//		stxtPricingUOM.setText("NONE");
//		stxtPricingUOM.setLayoutData(gridDataVal);
//		stxtPricingUOM.setEditable(false);
//		stxtPricingUOM.setData("name", "stxtPricingUOM");

		Label lblUnitPrice = new Label(pnlPrice, SWT.HORIZONTAL);
		lblUnitPrice.setText("Unit Price ");
		lblUnitPrice.setLayoutData(gridDataLbl);
		lblUnitPrice.setData("yrc:customType", "Label");
		lblUnitPrice.setData("name", "lblUnitPrice");
		
		stxtUnitPrice = new Text(pnlPrice, 72);
		// stxtUnitPrice.setText("NONE");
		stxtUnitPrice.setLayoutData(gridDataVal);
		//stxtUnitPrice.setEditable(false);
		stxtUnitPrice.setData("name", "stxtUnitPrice");
		
		
//		Label lblDummy = new Label(pnlPrice, SWT.HORIZONTAL);
//		lblDummy.setLayoutData(gridDataLbl);
				
//		btnPnA = new Button(pnlPrice, 0);
//		btnPnA.setText("PandA");
//		btnPnA.setLayoutData(gridDataVal);
//		btnPnA.setData("name", "btnPnA");
//		
//		
//		btnPnA.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				pnaCall();
//				
//			}
//		});
		
	}

	private void setBindingForComponents() {
		YRCStyledTextBindingData stbd = null;
		YRCTextBindingData tbd = null;
		YRCComboBindingData cbd = null;
		
		if(null != txtLineNo){
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/XPXRefOrderLine/@CustomerLineNO");
			tbd.setName("txtLineNo");
			txtLineNo.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
			tbd.setTargetBinding("XPXReferenceOrderLine:/XPXRefOrderLine/@CustomerLineNO");
		}
		// Item Details Panel
		if (null != txtItemId) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/XPXRefOrderLine/@ItemID");
			tbd.setName("txtItemId");
			txtItemId.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
			tbd.setTargetBinding("XPXReferenceOrderLine:/XPXRefOrderLine/@ItemID");
		}
		
		if (null != txtCustItemId) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/XPXRefOrderLine/@CustomerProductCode");
			tbd.setName("txtCustItemId");
			txtCustItemId.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}		
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("OrderLineTmp:/XPXRefOrderLine/@ItemDesc");
		tbd.setName("txtItemDesc");
		txtItemDesc.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("OrderLineTmp:/XPXRefOrderLine/@LineType");
		tbd.setName("txtLineType");
		txtLineType.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);

// Quantity Panel
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("OrderLineTmp:/XPXRefOrderLine/@OrderedQty");
		tbd.setName("txtQuantity");
		tbd.setDataType("Quantity");
		txtQuantity.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		tbd.setTargetBinding("XPXReferenceOrderLine:/XPXRefOrderLine/@OrderedQty");

		// UOM Panel
		if(null !=txtOrderingUOM){
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/XPXRefOrderLine/@TransactionalUOM");
			tbd.setName("txtOrderingUOM");
			txtOrderingUOM.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
			tbd.setTargetBinding("XPXReferenceOrderLine:/XPXRefOrderLine/@TransactionalUOM");
		}
		
//		if(null !=stxtPricingUOM){
//			stbd = new YRCStyledTextBindingData();
//			stbd.setSourceBinding("OrderLineTmp:/XPXRefOrderLine/@PricingUOM");
//			stbd.setName("stxtPricingUOM");
//			stxtPricingUOM.setData(YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
//		}
		
		if(null !=txtPricingUOM){
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/XPXRefOrderLine/@PricingUOM");
			tbd.setName("txtPricingUOM");
			txtPricingUOM.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}		

// Pricing Panel
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("OrderLineTmp:/XPXRefOrderLine/@UnitPrice");
		tbd.setCurrencyXPath("OrderLineTmp:/XPXRefOrderLine/@Currency");
		tbd.setName("stxtUnitPrice");
		stxtUnitPrice.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
	}

	public String getFormId() {
		return FORM_ID;
	}

	public String getHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	public IYRCPanelHolder getPanelHolder() {
		// TODO Auto-generated method stub
		return null;
	}

	public Composite getRootPanel() {
		return pnlRoot;
	}

	public void setCompositeWidths() {
		int newTotalWidth = 0;
		int parentWidth = parent.getSize().x;
		int controlsExcluded = 0;
		int controlsAddedByExtensibility = 0;
		Control al[] = LinePnl.getChildren();
		for (int i = 0; i < al.length; i++) {
			if (!((String) al[i].getData("name")).startsWith("extn"))
				continue;
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

		linePanelWidth = LinePnl.getSize().x;
		pnlRoot.layout(true, true);
	}
	
	public String getOrderLineKey() {
		return sOrderLineKey;
	}

	public XPXReferenceOrderSummaryPanel getOrderLinesPanel() {
		return pnlOrderLines;
	}
}
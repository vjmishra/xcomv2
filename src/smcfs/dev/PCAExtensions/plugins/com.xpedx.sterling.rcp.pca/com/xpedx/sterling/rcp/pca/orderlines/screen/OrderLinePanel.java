package com.xpedx.sterling.rcp.pca.orderlines.screen;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCButtonBindingData;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCLabelBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCStyledTextBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class OrderLinePanel extends Composite implements IYRCComposite {

	private Composite pnlRoot = null;
	private OrderLinePanelBehavior myBehavior;
	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.orderlines.screen.OrderLinePanel";

	private Composite parent;
	private Object inputObject;
	private int lineNo;
	private OrderLinePanel currentLinePnl;
	private FocusAdapter focusListener;
	private Text txtComment;
	private Text txtCustLinePONo;
	private Text txtCustLineAccNo;
	private Label lblCustLineAccNo;
	private Text txtShipFromBranch;
	private StyledText txtAdjAmount;
	//private Text txtCouponCode;
	
	private Composite pnlOptions;
	private Composite pnlOthers;

	private Button btnRemoveLine;
	private Button btnSplitLine;
	private Button chkPriceOverride;
	private Button chkAcceptLineComments;
	private Button chkPreventBackOrder;
	private Button chkAllowBackOrder;
	private Button chkPriceDiscrepency;
	private Button chkCustLineSeqNo;
	private Button chkCustLineAcctNo;
	private SelectionAdapter selectionAdapter;
	private Text txtQuantity;
	private Combo comboOrderingUOM;
	private Combo comboPricingUOM;
	private Combo comboItemId;
	private StyledText stxtPricingUOM;
	private Text stxtUnitPrice;
	private StyledText stxtBaseUOM;
	private Text txtOthrPrice;
	private StyledText stxtOthrCost;
	private StyledText stxtOthrCostUOM;
	private Label lblErrItemId;
	private Label lblErrLineType;
	private Label lblErrCustItem;
	private Label lblErrCustPONo;
	private Label lblErrReqDlvryDt;
	private Label lblErrCustLine1;
	private Label lblErrCustLine2;
	private Label lblErrCustLine3;
	private Label lblAllowBackOrder;
	private Label lblPreventBackOrder;
	private Label lblPriceDiscrepency;
	private Label lblErrCustLineSeqNo;
	private Label lblErrCustLineAcctNo;
	private Button btnLookupImg;
	private Text txtItemDesc;
	private Text txtItemId;
	private Text txtCustItem;
	private Text txtCustLineSeq;
	private Label lblCustLineSeq;
	private Text txtCustLine1;
	private Label lblCustLine1;
	private Text txtCustLine2;
	private Label lblCustLine2;
	private Text txtCustLine3;
	private Label lblCustLine3;
	private Text txtOrderingUOM;
	private StyledText txtExtnPrice;
	private StyledText txtExtnOrdQty;
	
	private Combo comboLineType;
	private Composite pnlError;
	private int linePanelWidth;
	private Composite LinePnl;
	private boolean newLine = true;
	private String sEnterpriseCode;
	private String sBillToID;
	private String sOrderLineKey;
	private String strOldLineComment;
	private Element eleOrderLine;
	private OrderLinesPanel pnlOrderLines;
	private String strItemid;

	private StyledText txtShippableQtyinReqUOM;
	private Text txtBackOrderedQryInReqUOM;
	private StyledText txtShippableQtyLineTotal;
	private Text txtOrderedQtyTotal;
	private int couponCodeStyle=2048;
	private String lineType;
	private Element shipToCustomerEle;
	private Element orderEle;
	private String strQty;
	private Composite pnlMainData;
	private Composite pnlCustInfo;
	private Composite pnlOthersMain;
	private Composite pnlMainDataChild;
	private Composite pnlLeftData;
	private Composite pnlRightData;
	private Text txtCustomerItemId;
	private StyledText txtLineTotal;
	private Text txtCouponCode;
	private StyledText txtGTM;
	private StyledText txtExtendedCost;
	private Combo comboShipfrom;
	private Label lblReqUOM;
	private Label lblReqUOMShippableQty;
	private FocusAdapter focusListenerLatest;
	private Text txtMfrItemId;
	private Text txtMpcItemId;
	private Label lblComboItemId;
	private StyledText stxtLegacyLineNumber;
	private Text hiddenTxtOrderingUOM;
	private Text hiddenTxtbaseUOM;
	private StyledText hiddenStxtPricingUOM;
	private Text txtOrginalLegacyItem;
	private Text  txtOriginalCustomerItem;
	private Text  txtOriginalOrderQuantity;
	private Text txtOriginalRequiredUOM;
	private Text  txtOProductDescription;
	private Text txtOrginalUnitPrice;
	private Text txtOriginalPriceUOM;
	private Text txtOriginalComments;
	private Text txtOReqDeliveryDate;
	private Text txtOriginalLineTotal;
	private Text txtOriginalTax;
	public Label lblErr;
	private Button chkGTMVariance;
	private Label lblGTMVariance;
	
	private Label lblOrginalLegacyItem;
	private Label lblOProductDescription;
	private Label lblOrginalUnitPrice;
	private Label lblCustLinePONo;
	private Label lblOriginalComments;
	private Label lblOriginalCustomerItem;
	private Label lblOriginalOrderQuantity;
	private Label lblOriginalRequiredUOM;
	private Label lblOriginalPriceUOM;
	private Label lblOReqDeliveryDate; 
	private Label lblOriginalLineTotal;
	private Label lblOriginalTax;
	private Label lblCustInfo;
	public String legacyOrderNumber;
	private Label lblPlacedOrderErr;
	private Label lblErrOrderQty;
	public boolean isCancelledLine=false;

	public OrderLinePanel(Composite parent, OrderLinesPanel pnlOrderLines, int style, Object inObj, int pnlNo, Element eleOrderLine,Element elePandALineResponse, Element uOMList) {
		super(parent, style);
		
		this.parent = parent;
		this.pnlOrderLines = pnlOrderLines;
		this.inputObject = inObj;
		this.eleOrderLine = eleOrderLine;
		this.shipToCustomerEle=pnlOrderLines.getPageBehavior().getShipToCustomerDetails();
		this.orderEle=pnlOrderLines.getPageBehavior().getLocalModel("OriginalOrder");
		lineNo = pnlNo;
		sEnterpriseCode = ((YRCEditorInput) inputObject).getAttributeValue("XPXOrderLinesEditor/@EnterpriseCode");
		sBillToID = ((YRCEditorInput) inputObject).getAttributeValue("XPXOrderLinesEditor/@BillToID");
		sOrderLineKey = YRCXmlUtils.getAttribute(eleOrderLine, "OrderLineKey");
		lineType = YRCXmlUtils.getAttribute(eleOrderLine, "LineType");
		legacyOrderNumber = YRCXmlUtils.getAttributeValue(orderEle, "/Order/Extn/@ExtnLegacyOrderNo");		
		checkPermanentOrderLock();
		if (!YRCPlatformUI.isVoid(sOrderLineKey))
			newLine = false;
		selectionAdapter = new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				Widget ctrl = e.widget;
				String ctrlName = (String) ctrl.getData("name");
				if (!YRCPlatformUI.isVoid(ctrlName))
					if (YRCPlatformUI.equals(ctrlName, "btnLookupImg")) {
						XPXUtils.openItemSearch(inputObject, txtItemId.getText(), sEnterpriseCode, sBillToID, false);
					}
					else if (YRCPlatformUI.equals(ctrlName, "btnRemoveLine")) {
						Boolean cnfrmDelete=YRCPlatformUI.getConfirmation(YRCPlatformUI.getString("Show_Warning_Title"),YRCPlatformUI.getString("Show_Order_Remove_Warning"));
						if(cnfrmDelete){
						Element orderLineTmp=myBehavior.getLocalModel("OrderLineTmp");
						String orderLineKey=YRCXmlUtils.getAttributeValue(orderLineTmp, "/OrderLine/@OrderLineKey");
						String orderHeaderKey=YRCXmlUtils.getAttributeValue(orderLineTmp, "/OrderLine/@OrderHeaderKey");
						if(isNewLine()){
							dispose();
						}else if(XPXUtils.isFullFillmentOrder(orderEle)){
							txtQuantity.setText("0");
							getOrderLinesPanel().getPageBehavior().updateOrderAction();
							//dispose();
						}else{
							myBehavior.removeorderLine(orderLineKey,orderHeaderKey);
							dispose();
						}
						}
					} else if (YRCPlatformUI.equals(ctrlName, "btnSplitLine")) {
						myBehavior.splitLine();
					}
			}
		};
		focusListener = new FocusAdapter(){
			public void focusGained(FocusEvent e)
            {
                Widget ctrl = e.widget;
                String ctrlName = (String)ctrl.getData("name");
                if(ctrlName != null)
                {
                    if(YRCPlatformUI.equals(ctrlName, "txtItemId")){
                        strItemid = txtItemId.getText();            
                    }
                    if(YRCPlatformUI.equals(ctrlName, "txtQuantity")){
                        strQty = txtQuantity.getText();            
                    }                    
                }
            }

            public void focusLost(FocusEvent e)
            {
                Widget ctrl = e.widget;
                if(ctrl == txtItemId)
                    focusLostItemId();

                if(ctrl == txtQuantity)
                    focusLostQty();
            }

            private void focusLostItemId()
            {
                if(YRCPlatformUI.isVoid(txtItemId.getText()))
                {
//                    myBehavior.setItemId(" ");
                    txtItemDesc.setText(" ");
                } else
                if(!YRCPlatformUI.equals(strItemid, txtItemId.getText()))
                    myBehavior.getItemInfo(txtItemId.getText());
            } 
            private void focusLostQty()
            {
                if(YRCPlatformUI.isVoid(txtQuantity.getText()))
                {
//                                    
                } else{
                if(!YRCPlatformUI.equals(strQty, txtQuantity.getText()))
                	if(myBehavior.isValidMulpleItem(txtQuantity.getText()))	
                		myBehavior.firePnA();
            }
            }
		};
		
		
		focusListenerLatest = new FocusAdapter(){
			public void focusGained(FocusEvent e)
            {
                Widget ctrl = e.widget;
                String ctrlName = (String)ctrl.getData("name");
                if(ctrlName != null)
                {
                    if(YRCPlatformUI.equals(ctrlName, "txtItemId")){
                        strItemid = txtItemId.getText();            
                    }
                    if(YRCPlatformUI.equals(ctrlName, "txtQuantity")){
                        strQty = txtQuantity.getText();            
                    }                    
                }
            }

            public void focusLost(FocusEvent e)
            {
                Widget ctrl = e.widget;
                if(ctrl == txtQuantity)
                    focusLostQty();
                else
                     focusLostItemId(ctrl);
            }

            private void focusLostItemId(Widget ctrl)
            {
            	if(isNewLine())
            	{
            		if(ctrl==txtItemId && !YRCPlatformUI.isVoid(txtItemId.getText())&& !YRCPlatformUI.equals(strItemid, txtItemId.getText()))
            		{
            			 myBehavior.getItemInfo(txtItemId.getText(),"LPC");
            		}
            		if(ctrl==txtCustomerItemId && !YRCPlatformUI.isVoid(txtCustomerItemId.getText())&& !YRCPlatformUI.equals(strItemid, txtCustomerItemId.getText()))
            		{
            			myBehavior.getItemInfo(txtCustomerItemId.getText(),"CPN");
            		}
            		/*if(ctrl==txtMfrItemId && !YRCPlatformUI.isVoid(txtMfrItemId.getText())&& !YRCPlatformUI.equals(strItemid, txtMfrItemId.getText()))
            		{
            			myBehavior.getItemInfo(txtMfrItemId.getText(),"MANUFACTURER_ITEM");
            		}*/
            		/*if(ctrl==txtMpcItemId && !YRCPlatformUI.isVoid(txtMpcItemId.getText())&& !YRCPlatformUI.equals(strItemid, txtMpcItemId.getText()))
            		{
            			myBehavior.getItemInfo(txtMpcItemId.getText(),"MPC");
            		}*/
            	}
            }
            private void focusLostQty()
            {
                if(YRCPlatformUI.isVoid(txtQuantity.getText()))
                {
                                    
                } else
                if(!YRCPlatformUI.equals(strQty, txtQuantity.getText()))
                    myBehavior.firePnA();
            }
		};
		initialize();
		setBindingForComponents();
		myBehavior = new OrderLinePanelBehavior(this, FORM_ID, inputObject, eleOrderLine,elePandALineResponse, uOMList);
		
		updateNonBindedComponents();
	}
	private void checkPermanentOrderLock() {
		if(pnlOrderLines.getPageBehavior().isReadOnlyPage()){
			Control[] controls = new Control[]{
					txtItemId,
					btnLookupImg,
					comboItemId,
					txtCustItem,
					txtCustomerItemId,
					txtMfrItemId,
					txtMpcItemId,
					comboLineType,
					txtCustLinePONo,
					txtComment,
					chkAcceptLineComments,
					txtShipFromBranch,
					txtCustLineSeq,
					txtCustLine1,
					txtCustLine2,
					txtCustLine3,
					txtQuantity,
					txtOrderingUOM,
					comboOrderingUOM,
					stxtUnitPrice,
					comboPricingUOM,
					chkPriceOverride,
					txtExtnPrice,
					txtAdjAmount,
					txtCustLineAccNo,
					chkCustLineAcctNo,
					chkCustLineSeqNo,
					btnSplitLine,
					txtOthrPrice,
					txtShippableQtyLineTotal,
					
					txtShippableQtyinReqUOM,
					txtOrderedQtyTotal,
					txtLineTotal,
					txtBackOrderedQryInReqUOM,
					comboShipfrom
					,txtOrginalLegacyItem,txtOriginalCustomerItem,txtOriginalOrderQuantity,txtOriginalRequiredUOM,txtOProductDescription,txtOrginalUnitPrice
					,txtOriginalPriceUOM,txtOriginalLineTotal,txtOriginalTax,txtOriginalComments,txtOReqDeliveryDate,btnRemoveLine
					
			};
			setControlsEnabled(controls, false);
			
		}
		
	}
	/**function used to show-dispose of controls **/
	private void getControlStatus(Control[] controls) {
		
		for (Control control : controls) {
			if (null != control) {
				XPXUtils.paintPanel(pnlCustInfo);
				if(!YRCPlatformUI.isVoid(lblCustInfo))
				{
					lblCustInfo.setVisible(true);
					lblCustInfo.setText("Customer information:");
				}
			}
			
		}
	}

	public OrderLinePanelBehavior getBehavior() {
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
		gridDataPnl.horizontalSpacing = 5;
		gridDataPnl.marginWidth = 3;
		gridDataPnl.marginHeight = 3;
		gridDataPnl.verticalSpacing = 0;
		pnlRoot = new Composite(this, SWT.NONE);
		pnlRoot.setData("name", "pnlRoot");
		pnlRoot.setLayout(gridDataPnl);
		
		if(!isNewLine()){
			couponCodeStyle=72;
		}
		
		createLineComposite();
	}

	private void createLineComposite() {
		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = 4;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.verticalAlignment = 2;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 3;
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 3;
		LinePnl = new Composite(getRootPanel(), 0);
		LinePnl.setLayout(gridLayout);
		LinePnl.setLayoutData(gridDataPnl);
		LinePnl.setData("name", "LinePnl");
		LinePnl.setData("yrc:customType", "TaskComposite");
		XPXUtils.paintPanel(LinePnl);
		
		createErrorComposite();
		createDataComposite();
		createAvailabilityComposite();
//		createItemComposite();
//		createQtyComposite();
//		createUOMComposite();
//		createPriceComposite();
//		createMiscsComposite();

//		createErrorComposite();
		createOptionsComposite();
	}
	private void createErrorComposite() {
		GridData gridDataPnl = new GridData();
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.grabExcessVerticalSpace=true;
		gridDataPnl.verticalAlignment = 2;
		gridDataPnl.horizontalAlignment = SWT.FILL;
		gridDataPnl.horizontalSpan=2;
//		gridDataPnl.widthHint=1045;
		// gridDataPnl.horizontalSpan = 11;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 2;
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 2;
		pnlError = new Composite(LinePnl, 0);
		pnlError.setLayout(gridLayout);
		pnlError.setLayoutData(gridDataPnl);
		pnlError.setData("name", "pnlError");
		
		GridData gridDataChk = new GridData();
		gridDataChk.horizontalAlignment = SWT.BEGINNING;
		gridDataChk.grabExcessHorizontalSpace = false;
		gridDataChk.verticalAlignment = SWT.FILL;
		gridDataChk.horizontalSpan=1;
		gridDataChk.horizontalIndent=20;
		GridData gridDataChk1 = new GridData();
		gridDataChk1.horizontalAlignment = SWT.BEGINNING;
		gridDataChk1.grabExcessHorizontalSpace = false;
		gridDataChk1.verticalAlignment = SWT.FILL;
		gridDataChk1.horizontalSpan=2;
		gridDataChk1.horizontalIndent=20;
		
        GridData gridDataErrLbl = new GridData();
        gridDataErrLbl.horizontalAlignment = SWT.BEGINNING;
        gridDataErrLbl.grabExcessHorizontalSpace = true;
        gridDataErrLbl.verticalAlignment = SWT.FILL;		
        gridDataErrLbl.horizontalSpan=1;
        gridDataErrLbl.horizontalIndent=5;
        
        GridData gridDataErrLbl1 = new GridData();
        gridDataErrLbl1.horizontalAlignment = SWT.FILL;
        gridDataErrLbl1.grabExcessHorizontalSpace = true;
        gridDataErrLbl1.verticalAlignment = SWT.FILL;		
        gridDataErrLbl1.horizontalSpan=2;
        gridDataErrLbl1.horizontalIndent=38;

		String errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@ItemIDErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
			lblErrItemId = new Label(pnlError, SWT.HORIZONTAL);
			lblErrItemId.setLayoutData(gridDataErrLbl1);
			lblErrItemId.setData("yrc:customType", "RedText10");
			lblErrItemId.setData("name", "lblErrItemId");
		}

		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@LineTypeErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
			lblErrLineType = new Label(pnlError, SWT.HORIZONTAL);
			lblErrLineType.setLayoutData(gridDataErrLbl1);
			lblErrLineType.setData("yrc:customType", "RedText10");
			lblErrLineType.setData("name", "lblErrLineType");
		}

		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@CustomerLinePONoErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
			lblErrCustPONo = new Label(pnlError, SWT.HORIZONTAL);
			lblErrCustPONo.setText(errorText);
			lblErrCustPONo.setLayoutData(gridDataErrLbl1);
			lblErrCustPONo.setData("name", "lblErrCustPONo");
			lblErrCustPONo.setData("yrc:customType", "RedText10");
		}

		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@LineCommentsErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
			chkAcceptLineComments = new Button(pnlError, SWT.CHECK);
			chkAcceptLineComments.setText("");
			chkAcceptLineComments.setVisible(true);
			chkAcceptLineComments.setData("name", "chkAcceptLineComments");
			chkAcceptLineComments.setData("yrc:customType", "Label");
			chkAcceptLineComments.setLayoutData(gridDataChk);
			Label lblErrAcceptLineComments = new Label(pnlError, SWT.HORIZONTAL);
			lblErrAcceptLineComments.setText("Accept Line Comments");
			lblErrAcceptLineComments.setLayoutData(gridDataErrLbl);
			lblErrAcceptLineComments.setData("yrc:customType", "RedText10");			
		}
		
		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@CustomerLineSeqNoErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
	        chkCustLineSeqNo = new Button(pnlError, SWT.CHECK);
	        chkCustLineSeqNo.setText("");
		    chkCustLineSeqNo.setVisible(true);
	        chkCustLineSeqNo.setData("name", "chkCustLineSeqNo");
	        chkCustLineSeqNo.setData("yrc:customType", "RedText10");
	        chkCustLineSeqNo.setLayoutData(gridDataChk);			
			lblErrCustLineSeqNo = new Label(pnlError, SWT.HORIZONTAL);
			lblErrCustLineSeqNo.setText(errorText);
			lblErrCustLineSeqNo.setLayoutData(gridDataErrLbl);
			lblErrCustLineSeqNo.setData("yrc:customType", "RedText10");
		}
		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@CustomerLineAcctNoErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
	        chkCustLineAcctNo = new Button(pnlError, SWT.CHECK);
	        chkCustLineAcctNo.setText("");
			chkCustLineAcctNo.setVisible(true);
	        chkCustLineAcctNo.setData("name", "chkCustLineAcctNo");
	        chkCustLineAcctNo.setData("yrc:customType", "RedText10");
	        chkCustLineAcctNo.setLayoutData(gridDataChk);
			
			lblErrCustLineAcctNo = new Label(pnlError, SWT.HORIZONTAL);
			lblErrCustLineAcctNo.setText(errorText);
			lblErrCustLineAcctNo.setLayoutData(gridDataErrLbl);
			lblErrCustLineAcctNo.setData("yrc:customType", "RedText10");
		}
		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@CustLine1ErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
			lblErrCustLine1 = new Label(pnlError, SWT.BEGINNING);
			lblErrCustLine1.setText(errorText);
			lblErrCustLine1.setLayoutData(gridDataErrLbl1);
			lblErrCustLine1.setData("yrc:customType", "RedText10");
			lblErrCustLine1.setData("name", "lblErrCustLine1");
		}

		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@CustLine2ErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
			lblErrCustLine2 = new Label(pnlError, SWT.BEGINNING);
			lblErrCustLine2.setText(errorText);
			lblErrCustLine2.setLayoutData(gridDataErrLbl1);
			lblErrCustLine2.setData("yrc:customType", "RedText10");
			lblErrCustLine2.setData("name", "lblErrCustLine2");
		}

		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@CustLine3ErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
			lblErrCustLine3 = new Label(pnlError, SWT.BEGINNING);
			lblErrCustLine3.setText(errorText);
			lblErrCustLine3.setLayoutData(gridDataErrLbl1);
			lblErrCustLine3.setData("yrc:customType", "RedText10");
			lblErrCustLine3.setData("name", "lblErrCustLine3");
		}
		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@AllowBackOrderErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
		
			chkAllowBackOrder = new Button(pnlError, SWT.CHECK);
			chkAllowBackOrder.setText("");
			//chkAllowBackOrder.setText("Items not available for next day shipment.");
			chkAllowBackOrder.setVisible(true);
			chkAllowBackOrder.setData("name", "chkAllowBackOrder");
			chkAllowBackOrder.setData("yrc:customType", "RedText10");
			chkAllowBackOrder.setLayoutData(gridDataChk);
			lblAllowBackOrder = new Label(pnlError, SWT.HORIZONTAL);
			lblAllowBackOrder.setText(errorText);
			lblAllowBackOrder.setLayoutData(gridDataErrLbl);
			lblAllowBackOrder.setData("yrc:customType", "RedText10");
		}
		
		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@PreventBackOrderErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
//	        chkPreventBackOrder = new Button(pnlError, SWT.CHECK);
//			chkPreventBackOrder.setText("");
//			//chkPreventBackOrder.setText("Prevent back orders.");
//			chkPreventBackOrder.setVisible(true);
//			chkPreventBackOrder.setData("name", "chkPreventBackOrder");
//			chkPreventBackOrder.setData("yrc:customType", "RedText10");
//			chkPreventBackOrder.setLayoutData(gridDataChk);
			lblPreventBackOrder = new Label(pnlError, SWT.HORIZONTAL);
			lblPreventBackOrder.setText(errorText);
			lblPreventBackOrder.setLayoutData(gridDataErrLbl1);
			lblPreventBackOrder.setData("yrc:customType", "RedText10");
		}
		
		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@AcceptPriceOverRideErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
//	        chkPreventBackOrder = new Button(pnlError, SWT.CHECK);
//			chkPreventBackOrder.setText("");
//			//chkPreventBackOrder.setText("Prevent back orders.");
//			chkPreventBackOrder.setVisible(true);
//			chkPreventBackOrder.setData("name", "chkPreventBackOrder");
//			chkPreventBackOrder.setData("yrc:customType", "RedText10");
//			chkPreventBackOrder.setLayoutData(gridDataChk);
			Label lblPriceOverride = new Label(pnlError, SWT.HORIZONTAL);
			lblPriceOverride.setText("Price beyond the allowed tolerance");
			lblPriceOverride.setLayoutData(gridDataErrLbl1);
			lblPriceOverride.setData("yrc:customType", "RedText10");
		}
				
		
		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@PriceDiscrepencyErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
	        chkPriceDiscrepency = new Button(pnlError, SWT.CHECK);
			chkPriceDiscrepency.setVisible(true);
			chkPriceDiscrepency.setData("name", "chkPriceDiscrepency");
			chkPriceDiscrepency.setData("yrc:customType", "Label");
			chkPriceDiscrepency.setLayoutData(gridDataChk);
			lblPriceDiscrepency = new Label(pnlError, SWT.HORIZONTAL);
			lblPriceDiscrepency.setText(errorText);
			lblPriceDiscrepency.setLayoutData(gridDataErrLbl);
			lblPriceDiscrepency.setData("yrc:customType", "RedText10");
			}
		errorText = YRCXmlUtils.getAttributeValue(eleOrderLine, "/OrderLine/Error/@GrossTradingMarginErrorText");
		if (!YRCPlatformUI.isVoid(errorText)) {
	        chkGTMVariance = new Button(pnlError, SWT.CHECK);
	        chkGTMVariance.setVisible(true);
	        chkGTMVariance.setData("name", "chkGTMVariance");
	        chkGTMVariance.setData("yrc:customType", "Label");
	        chkGTMVariance.setLayoutData(gridDataChk);
			lblGTMVariance = new Label(pnlError, SWT.HORIZONTAL);
			lblGTMVariance.setText("Gross Trading Margin beyond the tolerance");
			lblGTMVariance.setLayoutData(gridDataErrLbl);
			lblGTMVariance.setData("yrc:customType", "RedText10");
			}		
				
		lblErr = new Label(pnlError, SWT.NONE);
		lblErr.setLayoutData(gridDataErrLbl1);
		lblErr.setData("name", "lblErr");
		lblErr.setData("yrc:customType", "RedText10");
		
		lblPlacedOrderErr = new Label(pnlError, SWT.NONE);
		lblPlacedOrderErr.setLayoutData(gridDataErrLbl1);
		lblPlacedOrderErr.setData("name", "lblPlacedOrderErr");
		lblPlacedOrderErr.setData("yrc:customType", "RedText10");
		
		lblErrOrderQty = new Label(pnlError, SWT.NONE);
		lblErrOrderQty.setLayoutData(gridDataErrLbl1);
		lblErrOrderQty.setData("name", "lblErrOrderQty");
		lblErrOrderQty.setData("yrc:customType", "RedText10");
		
	}
	
	private void createDataComposite() {
		// TODO Auto-generated method stub
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.numColumns = 1;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;

		GridData gridDataPnl1 = new GridData();
		gridDataPnl1.horizontalAlignment = 2;
		gridDataPnl1.grabExcessHorizontalSpace = false;
		gridDataPnl1.grabExcessVerticalSpace = true;
		// gridDataPnl.verticalSpan = 2;
		gridDataPnl1.widthHint = 1100;
		gridDataPnl1.verticalAlignment = 4;

		pnlMainData = new Composite(LinePnl, 0);
		pnlMainData.setLayoutData(gridDataPnl1);
		pnlMainData.setLayout(gridLayout1);
		pnlMainData.setData("name", "pnlMainData");
		
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 1;
		gridLayout2.verticalSpacing = 1;
		gridLayout2.numColumns = 2;
//		gridLayout2.marginWidth = 5;
//		gridLayout2.marginHeight = 5;

		GridData gridDataPnl2 = new GridData();
		gridDataPnl2.horizontalAlignment = 2;
		gridDataPnl2.grabExcessHorizontalSpace = false;
		gridDataPnl2.grabExcessVerticalSpace = true;
		// gridDataPnl.verticalSpan = 2;
		gridDataPnl2.widthHint = 1100;
		gridDataPnl2.verticalAlignment = 4;
		
		pnlMainDataChild = new Composite(pnlMainData, 0);
		pnlMainDataChild.setLayoutData(gridDataPnl2);
		pnlMainDataChild.setLayout(gridLayout2);
		pnlMainDataChild.setData("name", "pnlMainDataChild");
		
		createLeftDataComposite();
		createRightDataComposite();
		GridData gridDataHeader = new GridData();
		gridDataHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataHeader.verticalAlignment = SWT.CENTER;
		gridDataHeader.grabExcessVerticalSpace = false;
		gridDataHeader.grabExcessHorizontalSpace = true;
		gridDataHeader.horizontalSpan =2;
		gridDataHeader.horizontalIndent=22;
		gridDataHeader.verticalSpan = 1;
		gridDataHeader.widthHint = 150;
		
		lblCustInfo = new Label(pnlMainData, SWT.HORIZONTAL);
		lblCustInfo.setText("Customer information:");
		lblCustInfo.setLayoutData(gridDataHeader);
		lblCustInfo.setData("yrc:customType", "Label");
		lblCustInfo.setData("name", "lblCustInfo");
		if(isNewLine()){
			lblCustInfo.setVisible(false);
		}	
		createCustInfoComposite();
	}

	private void createLeftDataComposite() {
		// TODO Auto-generated method stub
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 2;
		gridLayout2.verticalSpacing = 2;
		gridLayout2.numColumns = 5;

		GridData gridDataPnl2 = new GridData();
		gridDataPnl2.horizontalAlignment = 2;
		gridDataPnl2.grabExcessHorizontalSpace = false;
		gridDataPnl2.grabExcessVerticalSpace = true;
		// gridDataPnl.verticalSpan = 2;
		gridDataPnl2.widthHint = 505;
		gridDataPnl2.verticalAlignment = 4;
		
		pnlLeftData = new Composite(pnlMainDataChild, 0);
		pnlLeftData.setLayoutData(gridDataPnl2);
		pnlLeftData.setLayout(gridLayout2);
		pnlLeftData.setData("name", "pnlLeftData");		
//		XPXUtils.paintPanel(pnlLeftData);
		
		GridData gridDataLbl = new GridData();
		gridDataLbl.horizontalAlignment = SWT.BEGINNING;
		gridDataLbl.verticalAlignment = SWT.FILL;
		gridDataLbl.grabExcessVerticalSpace = false;
		gridDataLbl.grabExcessHorizontalSpace = true;
		gridDataLbl.horizontalSpan = 1;
		gridDataLbl.verticalSpan = 1;
		gridDataLbl.widthHint = 100;
		
		GridData gridDataLbl0 = new GridData();
		gridDataLbl0.horizontalAlignment = SWT.BEGINNING;
		gridDataLbl0.verticalAlignment = SWT.FILL;
		gridDataLbl0.grabExcessVerticalSpace = false;
		gridDataLbl0.grabExcessHorizontalSpace = true;
		gridDataLbl0.horizontalSpan = 1;
		gridDataLbl0.verticalSpan = 1;

		GridData gridDataVal = new GridData();
		gridDataVal.horizontalAlignment = 4;
		gridDataVal.verticalAlignment = SWT.FILL;
		gridDataVal.grabExcessVerticalSpace = false;
		gridDataVal.grabExcessHorizontalSpace = true;
		gridDataVal.horizontalSpan = 4;
		gridDataVal.verticalSpan = 1;
		gridDataVal.widthHint = 75;	
		gridDataVal.heightHint=30;
		

		comboLineType = new Combo(pnlLeftData, 8);
		comboLineType.setLayoutData(gridDataLbl);
		comboLineType.setData("name", "comboLineType");
		comboLineType.addSelectionListener(new SelectionAdapter() {

        public void widgetSelected(SelectionEvent e)
        {
        	String strLineType = myBehavior.getFieldValue("comboLineType");
        	
        	if(YRCPlatformUI.equals(XPXConstants.LEGACY_PRODUCT_TYPE, strLineType)||
        			YRCPlatformUI.equals(XPXConstants.CUSTOMER_PART_NUMBER_TYPE, strLineType)){
        		hideCustomerControlsInfo(true);
        	}
        	else{
        		hideCustomerControlsInfo(false);
        	}
        	
        	if (YRCPlatformUI.isVoid(strLineType)
    				|| YRCPlatformUI.equals(XPXConstants.LEGACY_PRODUCT_TYPE, strLineType)) {
        		lblComboItemId.setVisible(false);
        		
        		comboItemId.setVisible(false);
    			txtItemId.setVisible(true);
//    			btnLookupImg.setVisible(true);
    			txtQuantity.setEnabled(true);
    			
    		}else if(YRCPlatformUI.equals(XPXConstants.CHARGE_TYPE, strLineType)){
    			txtItemId.setVisible(false);
//    			btnLookupImg.setVisible(false);
    			lblComboItemId.setVisible(true);
    			comboItemId.setVisible(true);
    			
    			
    		}else {
    			txtItemId.setVisible(true);
//    			btnLookupImg.setVisible(true);
    			lblComboItemId.setVisible(false);
    			comboItemId.setVisible(false);
    			txtQuantity.setEnabled(true);
    		}
        	myBehavior.firePnA();
        	}
		});
		
		Label lblLegacyItemId = new Label(pnlLeftData, SWT.HORIZONTAL);
		lblLegacyItemId.setText("Legacy Item#:");
		lblLegacyItemId.setLayoutData(gridDataLbl);
		lblLegacyItemId.setData("yrc:customType", "Label");
		lblLegacyItemId.setData("name", "lblLegacyItemId");
		txtItemId = new Text(pnlLeftData, 2048);
		txtItemId.setLayoutData(gridDataLbl);
		txtItemId.setData("name", "txtItemId");
		txtItemId.addFocusListener(focusListenerLatest);	
		
	
		Label lblCustomerItemId = new Label(pnlLeftData, SWT.HORIZONTAL);
		lblCustomerItemId.setText("Cust Item#:");
		lblCustomerItemId.setLayoutData(gridDataLbl);
		lblCustomerItemId.setData("yrc:customType", "Label");
		lblCustomerItemId.setData("name", "lblCustomerItemId");
		txtCustomerItemId = new Text(pnlLeftData, 2048);
		txtCustomerItemId.setLayoutData(gridDataLbl);
		txtCustomerItemId.setData("name", "txtCustomerItemId");
		txtCustomerItemId.addFocusListener(focusListenerLatest);	
		
		stxtLegacyLineNumber = new StyledText(pnlLeftData, SWT.BOLD);
		stxtLegacyLineNumber.setText("");
		stxtLegacyLineNumber.setLayoutData(gridDataLbl);
		stxtLegacyLineNumber.setEditable(false);
		stxtLegacyLineNumber.setData("name", "stxtLegacyLineNumber");
		stxtLegacyLineNumber.setEnabled(false);
		
		GridData gridDataLbl1 = new GridData();
		gridDataLbl1.horizontalAlignment = SWT.END;
		gridDataLbl1.verticalAlignment = SWT.FILL;
		gridDataLbl1.grabExcessVerticalSpace = false;
		gridDataLbl1.grabExcessHorizontalSpace = true;
		gridDataLbl1.horizontalSpan = 2;
		gridDataLbl1.verticalSpan = 1;
		gridDataLbl1.widthHint = 60;

		Label lblMfrItemId = new Label(pnlLeftData, SWT.HORIZONTAL);
		lblMfrItemId.setText("Mfr Item# :");
		lblMfrItemId.setLayoutData(gridDataLbl);
		lblMfrItemId.setData("yrc:customType", "Label");
		lblMfrItemId.setData("name", "lblMfrItemId");
		txtMfrItemId = new Text(pnlLeftData, 2048);
		txtMfrItemId.setLayoutData(gridDataLbl);
		txtMfrItemId.setData("name", "txtMfrItemId");
		txtMfrItemId.addFocusListener(focusListenerLatest);
		txtMfrItemId.setEnabled(false);

		Label lblMpcItemId = new Label(pnlLeftData, SWT.HORIZONTAL);
		lblMpcItemId.setText("MPC # :");
		lblMpcItemId.setLayoutData(gridDataLbl);
		lblMpcItemId.setData("yrc:customType", "Label");
		lblMpcItemId.setData("name", "lblMpcItemId");
		txtMpcItemId = new Text(pnlLeftData, 2048);
		txtMpcItemId.setLayoutData(gridDataLbl);
		txtMpcItemId.setData("name", "txtMpcItemId");
		txtMpcItemId.addFocusListener(focusListenerLatest);
		txtMpcItemId.setEnabled(false);

		lblComboItemId = new Label(pnlLeftData, SWT.HORIZONTAL);
		lblComboItemId.setText("Legacy Item#");
		lblComboItemId.setLayoutData(gridDataLbl);
		lblComboItemId.setData("yrc:customType", "Label");
		lblComboItemId.setData("name", "lblComboItemId");
		
		comboItemId =  new Combo(pnlLeftData, 8);
		comboItemId.setLayoutData(gridDataVal);
		comboItemId.setData("name", "comboItemId");
		comboItemId.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e)
            {
            	myBehavior.setItemDescForCharges();
            }
        });			
		
		Label lblItemDesc = new Label(pnlLeftData, SWT.HORIZONTAL);
		lblItemDesc.setText("Description:");
		lblItemDesc.setLayoutData(gridDataLbl);
		lblItemDesc.setData("yrc:customType", "Label");
		lblItemDesc.setData("name", "lblItemDesc");

		txtItemDesc = new Text(pnlLeftData, 2626);
		txtItemDesc.setText("");
		txtItemDesc.setLayoutData(gridDataVal);
		txtItemDesc.setEditable(false);
		txtItemDesc.setData("name", "txtItemDesc");
		txtItemDesc.setEnabled(false);

		Label lblUnitPrice = new Label(pnlLeftData, SWT.HORIZONTAL);
		lblUnitPrice.setText("Unit Price: ");
		lblUnitPrice.setLayoutData(gridDataLbl);
		lblUnitPrice.setData("yrc:customType", "Label");
		lblUnitPrice.setData("name", "lblUnitPrice");
		
		stxtUnitPrice = new Text(pnlLeftData, 2048);
		// stxtUnitPrice.setText("NONE");
		stxtUnitPrice.setLayoutData(gridDataLbl);
		//stxtUnitPrice.setEditable(false);
		stxtUnitPrice.setData("name", "stxtUnitPrice");
		stxtUnitPrice.setEditable(false);
		stxtUnitPrice.setEnabled(false);
		
		if(!isNewLine()){
			if(isPriceOverrideChecked()){
				stxtUnitPrice.setEditable(true);
			}
		}
		if (true) {
		stxtPricingUOM = new StyledText(pnlLeftData, SWT.SIMPLE);
		stxtPricingUOM.setText("");
		stxtPricingUOM.setLayoutData(gridDataLbl);
		stxtPricingUOM.setEditable(false);
		stxtPricingUOM.setData("name", "stxtPricingUOM");
		stxtPricingUOM.setEnabled(false);
		} else {
			comboPricingUOM = new Combo(pnlLeftData, 8);
			comboPricingUOM.setLayoutData(gridDataLbl);
			comboPricingUOM.setData("name", "comboPricingUOM");
		}

		Label lblPriceOverride = new Label(pnlLeftData, SWT.HORIZONTAL);
		lblPriceOverride.setText("Price Override:");
		lblPriceOverride.setLayoutData(gridDataLbl);
		lblPriceOverride.setData("yrc:customType", "Label");
		lblPriceOverride.setData("name", "lblPriceOverride");
		chkPriceOverride = new Button(pnlLeftData, SWT.CHECK);
		chkPriceOverride.setText("");
		chkPriceOverride.setVisible(true);
		chkPriceOverride.setData("name", "chkPriceOverride");
		chkPriceOverride.setData("yrc:customType", "Label");
		chkPriceOverride.setLayoutData(gridDataLbl0);	
		
		chkPriceOverride.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean checked=chkPriceOverride.getSelection();
				if(checked){
					stxtUnitPrice.setEditable(true);
					stxtUnitPrice.setEnabled(true);
				}
				else{
					stxtUnitPrice.setEditable(false);
					stxtUnitPrice.setEnabled(false);
				}
				
			}
			
		});
		
		Label lblOthrCost = new Label(pnlLeftData, SWT.HORIZONTAL);
		lblOthrCost.setText("Unit Cost:");
		lblOthrCost.setLayoutData(gridDataLbl);
		lblOthrCost.setData("yrc:customType", "Label");
		lblOthrCost.setData("name", "lblOthrCost");
		stxtOthrCost = new StyledText(pnlLeftData, SWT.SIMPLE);
		stxtOthrCost.setText("");
		stxtOthrCost.setLayoutData(gridDataLbl);
		stxtOthrCost.setEditable(false);
		stxtOthrCost.setData("name", "stxtOthrCost");
		stxtOthrCost.setEnabled(false);
		stxtOthrCostUOM = new StyledText(pnlLeftData, SWT.SIMPLE);
		stxtOthrCostUOM.setText("");
		stxtOthrCostUOM.setLayoutData(gridDataLbl);
		stxtOthrCostUOM.setEditable(false);
		stxtOthrCostUOM.setData("name", "stxtOthrCostUOM");
		stxtOthrCostUOM.setEnabled(false);
		Label lblGp = new Label(pnlLeftData, SWT.HORIZONTAL);
		lblGp.setText("Gross Profit:");
		lblGp.setLayoutData(gridDataLbl);
		lblGp.setData("yrc:customType", "Label");
		lblGp.setData("name", "lblGp");
		Label lblGpVal = new Label(pnlLeftData, SWT.HORIZONTAL);
		lblGpVal.setText("");
		lblGpVal.setLayoutData(gridDataLbl);
		lblGpVal.setData("yrc:customType", "Text");
		lblGpVal.setData("name", "lblGpVal");			
		
		Label lblLineComment = new Label(pnlLeftData, SWT.HORIZONTAL);
		lblLineComment.setText("Notes: ");
		lblLineComment.setLayoutData(gridDataLbl);
		lblLineComment.setData("yrc:customType", "Label");
		lblLineComment.setData("name", "lblLineComment");

		txtComment = new Text(pnlLeftData, 2626|SWT.TAB);
		txtComment.setText("");
		txtComment.setLayoutData(gridDataVal);
//		txtItemDesc.setEditable(false);
		txtComment.setData("name", "txtComment");
		
	}

	private void createRightDataComposite() {
		// TODO Auto-generated method stub
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 0;
		gridLayout2.verticalSpacing = 1;
		gridLayout2.numColumns = 6;
		
//		gridLayout2.marginWidth = 5;
//		gridLayout2.marginHeight = 5;

		GridData gridDataPnl2 = new GridData();
		gridDataPnl2.horizontalAlignment = 2;
		gridDataPnl2.grabExcessHorizontalSpace = false;
		gridDataPnl2.grabExcessVerticalSpace = true;
		// gridDataPnl.verticalSpan = 2;
		gridDataPnl2.widthHint = 620;
		gridDataPnl2.verticalAlignment = 4;
		
		pnlRightData = new Composite(pnlMainDataChild, 0);
		pnlRightData.setLayoutData(gridDataPnl2);
		pnlRightData.setLayout(gridLayout2);
		pnlRightData.setData("name", "pnlRightData");
//		XPXUtils.paintPanel(pnlRightData);
		
		GridData gridDataVal2 = new GridData();
		gridDataVal2.horizontalAlignment = SWT.BEGINNING;
		gridDataVal2.verticalAlignment = SWT.FILL;
		gridDataVal2.grabExcessVerticalSpace = false;
		gridDataVal2.grabExcessHorizontalSpace = true;
		gridDataVal2.horizontalSpan = 1;
		gridDataVal2.verticalSpan = 1;
		gridDataVal2.widthHint = 50;

		GridData gridDataLbl3 = new GridData();
		gridDataLbl3.horizontalAlignment = SWT.BEGINNING;
		gridDataLbl3.verticalAlignment = SWT.FILL;
		gridDataLbl3.grabExcessVerticalSpace = false;
		gridDataLbl3.grabExcessHorizontalSpace = true;
		gridDataLbl3.horizontalSpan = 1;
		gridDataLbl3.verticalSpan = 1;
		gridDataLbl3.widthHint = 70;
		
		GridData gridDataLbl2 = new GridData();
		gridDataLbl2.horizontalAlignment = SWT.BEGINNING;
		gridDataLbl2.verticalAlignment = SWT.FILL;
		gridDataLbl2.grabExcessVerticalSpace = false;
		gridDataLbl2.grabExcessHorizontalSpace = true;
		gridDataLbl2.horizontalSpan = 1;
		gridDataLbl2.verticalSpan = 1;
		gridDataLbl2.widthHint = 120;
		
		GridData gridDataLbl1 = new GridData();
		gridDataLbl1.horizontalAlignment = SWT.BEGINNING;
		gridDataLbl1.verticalAlignment = SWT.FILL;
		gridDataLbl1.grabExcessVerticalSpace = false;
		gridDataLbl1.grabExcessHorizontalSpace = true;
		gridDataLbl1.horizontalSpan = 2;
		gridDataLbl1.verticalSpan = 1;
		gridDataLbl1.widthHint = 50;
		gridDataLbl1.heightHint=18;
		
		GridData gridDataLine1Lbl1 = new GridData();
		gridDataLine1Lbl1.horizontalAlignment = SWT.BEGINNING;
		gridDataLine1Lbl1.verticalAlignment = SWT.BEGINNING;
		gridDataLine1Lbl1.grabExcessVerticalSpace = true;
		gridDataLine1Lbl1.grabExcessHorizontalSpace = true;
		gridDataLine1Lbl1.horizontalSpan = 1;
		gridDataLine1Lbl1.verticalSpan = 4;
		gridDataLine1Lbl1.widthHint = 70;

		GridData gridDataLine1Val2 = new GridData();
		gridDataLine1Val2.horizontalAlignment = SWT.BEGINNING;
		gridDataLine1Val2.verticalAlignment = SWT.BEGINNING;
		gridDataLine1Val2.grabExcessVerticalSpace = true;
		gridDataLine1Val2.grabExcessHorizontalSpace = true;
		gridDataLine1Val2.horizontalSpan = 1;
		gridDataLine1Val2.verticalSpan =4;
		gridDataLine1Val2.widthHint = 50;	
		
		GridData gridDataLine1Lbl2 = new GridData();
		gridDataLine1Lbl2.horizontalAlignment = SWT.BEGINNING;
		gridDataLine1Lbl2.verticalAlignment = SWT.BEGINNING;
		gridDataLine1Lbl2.grabExcessVerticalSpace = true;
		gridDataLine1Lbl2.grabExcessHorizontalSpace = true;
		gridDataLine1Lbl2.horizontalSpan = 2;
		gridDataLine1Lbl2.verticalSpan = 4;
		gridDataLine1Lbl2.widthHint = 100;
		
		Label lblBaseOrderedQty = new Label(pnlRightData, SWT.HORIZONTAL);
		lblBaseOrderedQty.setText("Order Qty:");
		lblBaseOrderedQty.setLayoutData(gridDataLine1Lbl1);
		lblBaseOrderedQty.setData("yrc:customType", "Label");
		lblBaseOrderedQty.setData("name", "lblBaseOrderedQty");
		
		txtQuantity = new Text(pnlRightData, 2048);
		txtQuantity.setText("");
		txtQuantity.setLayoutData(gridDataLine1Val2);
		txtQuantity.setData("name", "txtQuantity");
//		txtQuantity.setToolTipText(YRCPlatformUI.getString("Ordered_Qty_In_Req_UOM_Tooltip"));
		txtQuantity.addFocusListener(focusListener);

		if(!isNewLine()&&!YRCPlatformUI.isVoid(legacyOrderNumber))  {
		txtOrderingUOM = new Text(pnlRightData, 2048);
		txtOrderingUOM.setText("");
		txtOrderingUOM.setLayoutData(gridDataLine1Val2);
		txtOrderingUOM.setData("name", "txtOrderingUOM");
		} else {
		comboOrderingUOM = new Combo(pnlRightData, 8);
		comboOrderingUOM.setLayoutData(gridDataLine1Val2);
		comboOrderingUOM.setData("name", "comboOrderingUOM");
		comboOrderingUOM.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e)
            {
            	if(!YRCPlatformUI.isVoid(myBehavior.getFieldValue("txtQuantity"))){
            		//Changes as part of bug#2373- commenting this line to display existed Qty on TxtQty field even if in case UOM changed.
            		//myBehavior.setFieldValue("txtQuantity", "");
            		
            		myBehavior.getControl("txtQuantity").setFocus();
            	}
            	//Invoking PnA service after validating the Items Qty.. changes as part of bug#2373
            	if(myBehavior.isValidMulpleItem(txtQuantity.getText()))	
              	myBehavior.firePnA();
            }
		});
		}
				
		Label lblShipFromBranch = new Label(pnlRightData, SWT.HORIZONTAL);
		lblShipFromBranch.setText("Ship From:");
		lblShipFromBranch.setLayoutData(gridDataLine1Lbl1);
		lblShipFromBranch.setData("yrc:customType", "Label");
		lblShipFromBranch.setData("name", "lblShipFromBranch");
//		txtShipFromBranch = new Text(pnlRightData, 2048);
//		txtShipFromBranch.setText("");
//		txtShipFromBranch.setLayoutData(gridDataLbl1);
//		txtShipFromBranch.setData("name", "txtShipFromBranch");	
		
		comboShipfrom = new Combo(pnlRightData, 8);
		comboShipfrom.setLayoutData(gridDataLine1Lbl2);
		comboShipfrom.setData("name", "comboShipfrom");
	
		//		TODO Shippable Qty in Requested UOM
		Label lblShippableQtyinReqUOM = new Label(pnlRightData, SWT.HORIZONTAL);
		lblShippableQtyinReqUOM.setText("Shippable Qty:");
		lblShippableQtyinReqUOM.setLayoutData(gridDataLbl2);
		lblShippableQtyinReqUOM.setData("yrc:customType", "Label");
		lblShippableQtyinReqUOM.setData("name", "lblShippableQtyinReqUOM");
		
		
		txtShippableQtyinReqUOM = new StyledText(pnlRightData, SWT.SIMPLE);
		txtShippableQtyinReqUOM.setText("");
		txtShippableQtyinReqUOM.setLayoutData(gridDataVal2);
		txtShippableQtyinReqUOM.setData("name", "txtShippableQtyinReqUOM");
		txtShippableQtyinReqUOM.setEditable(false);
		
		lblReqUOMShippableQty = new Label(pnlRightData, SWT.HORIZONTAL);
		lblReqUOMShippableQty.setText("ReqUOM");
		lblReqUOMShippableQty.setLayoutData(gridDataLbl3);
		lblReqUOMShippableQty.setData("yrc:customType", "Text");
		lblReqUOMShippableQty.setData("name", "lblReqUOMShippableQty");
		
		//		TODO BackOrder Qty in Requested UOM
		Label lblBackOrderedQryInReqUOM = new Label(pnlRightData, SWT.HORIZONTAL);
		lblBackOrderedQryInReqUOM.setText("Backorder Qty:");
		lblBackOrderedQryInReqUOM.setLayoutData(gridDataLbl2);
		lblBackOrderedQryInReqUOM.setData("yrc:customType", "Label");
		lblBackOrderedQryInReqUOM.setData("name", "lblBackOrderedQryInReqUOM");
		txtBackOrderedQryInReqUOM = new Text(pnlRightData, 2048);
		txtBackOrderedQryInReqUOM.setLayoutData(gridDataVal2);
		txtBackOrderedQryInReqUOM.setData("name", "txtBackOrderedQryInReqUOM");
		lblReqUOM = new Label(pnlRightData, SWT.HORIZONTAL);
		lblReqUOM.setText("ReqUOM");
		lblReqUOM.setLayoutData(gridDataLbl3);
		lblReqUOM.setData("yrc:customType", "Text");
		lblReqUOM.setData("name", "lblReqUOM");	

		
		Label lblGTM = new Label(pnlRightData, SWT.HORIZONTAL);
		lblGTM.setText("GTM:");
		lblGTM.setLayoutData(gridDataLbl2);
		lblGTM.setData("yrc:customType", "Label");
		lblGTM.setData("name", "lblGTM");
		txtGTM = new StyledText(pnlRightData, SWT.SIMPLE);
		txtGTM.setText("");
		txtGTM.setEditable(false);
		txtGTM.setLayoutData(gridDataLbl1);
		txtGTM.setData("name", "txtGTM");
		txtGTM.setEnabled(false);

		Label lblExtendedCost = new Label(pnlRightData, SWT.HORIZONTAL);
		lblExtendedCost.setText("Extended Cost:");
		lblExtendedCost.setLayoutData(gridDataLbl2);
		lblExtendedCost.setData("yrc:customType", "Label");
		lblExtendedCost.setData("name", "lblExtendedCost");
		txtExtendedCost = new StyledText(pnlRightData, SWT.SIMPLE);
		txtExtendedCost.setEditable(false);
		txtExtendedCost.setText("");
		txtExtendedCost.setLayoutData(gridDataLbl1);
		txtExtendedCost.setData("name", "txtExtendedCost");
		txtExtendedCost.setEnabled(false);

		Label lblOrderedQtyTotal = new Label(pnlRightData, SWT.HORIZONTAL);
		lblOrderedQtyTotal.setText("Ordered Amount:");
		lblOrderedQtyTotal.setLayoutData(gridDataLbl2);
		lblOrderedQtyTotal.setData("yrc:customType", "Label");
		lblOrderedQtyTotal.setData("name", "lblOrderedQtyTotal");
		txtOrderedQtyTotal = new Text(pnlRightData, SWT.SIMPLE);
		txtOrderedQtyTotal.setLayoutData(gridDataLbl1);
		txtOrderedQtyTotal.setData("name", "txtOrderedQtyTotal");
		txtOrderedQtyTotal.setEditable(false);
		txtOrderedQtyTotal.setText("");

		Label lblLineTotal = new Label(pnlRightData, SWT.HORIZONTAL);
		lblLineTotal.setText("Line Total:");
		lblLineTotal.setLayoutData(gridDataLbl2);
		lblLineTotal.setData("yrc:customType", "Label");
		lblLineTotal.setData("name", "lblLineTotal");
		
		txtLineTotal = new StyledText(pnlRightData, SWT.SIMPLE);
		txtLineTotal.setLayoutData(gridDataLbl1);
		txtLineTotal.setData("name", "txtLineTotal");
		txtLineTotal.setEditable(false);
		txtLineTotal.setText("");
		
		Label lblShippableQtyLineTotal = new Label(pnlRightData, SWT.HORIZONTAL);
		lblShippableQtyLineTotal.setText("Shippable Amount:");
		lblShippableQtyLineTotal.setLayoutData(gridDataLbl2);
		lblShippableQtyLineTotal.setData("yrc:customType", "Label");
		lblShippableQtyLineTotal.setData("name", "lblShippableQtyLineTotal");
		
		
		txtShippableQtyLineTotal = new StyledText(pnlRightData, SWT.SIMPLE);
		txtShippableQtyLineTotal.setLayoutData(gridDataLbl1);
		txtShippableQtyLineTotal.setData("name", "txtShippableQtyLineTotal");
		txtShippableQtyLineTotal.setEditable(false);
		txtShippableQtyLineTotal.setText("");
		
		if (!pnlOrderLines.getPageBehavior().isFromOrderEntryWizard()) {
			Label lblAdjAmount = new Label(pnlRightData, SWT.HORIZONTAL);
			lblAdjAmount.setText("Adjustment:");
			lblAdjAmount.setLayoutData(gridDataLbl2);
			lblAdjAmount.setData("yrc:customType", "Label");
			lblAdjAmount.setData("name", "lblAdjAmount");
			
					
			GridData gridDataAdjLbl1 = new GridData();
			gridDataAdjLbl1.horizontalAlignment = SWT.FILL;
			gridDataAdjLbl1.verticalAlignment = SWT.FILL;
			gridDataAdjLbl1.grabExcessVerticalSpace = false;
			gridDataAdjLbl1.grabExcessHorizontalSpace = false;
			gridDataAdjLbl1.horizontalSpan = 2;
			gridDataAdjLbl1.verticalSpan = 1;
			gridDataAdjLbl1.widthHint = 50;
			gridDataAdjLbl1.heightHint=18;
			
			txtAdjAmount = new StyledText(pnlRightData, SWT.FILL);
			txtAdjAmount.setText("");
			txtAdjAmount.setLayoutData(gridDataAdjLbl1);
			txtAdjAmount.setEditable(false);
			txtAdjAmount.setData("name", "txtAdjAmount");
	
		}
		else{
			GridData gridData1 = new GridData();
			gridData1.widthHint = 93;
			gridData1.horizontalAlignment = SWT.FILL;
			gridData1.verticalAlignment = 4;
			gridData1.horizontalSpan = 3;
			gridData1.heightHint=18;
			
			Text txtDummy1 = new Text(pnlRightData, 72);
			txtDummy1.setLayoutData(gridData1);
			txtDummy1.setEnabled(false);
			
		}
		
		
		//adding part of CR changes - 2591
		GridData gridData = new GridData();
		gridData.widthHint = 20;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = 4;
		gridData.horizontalSpan = 1;
		gridData.heightHint=18;
		
	
	
		GridData gridDataExtnOrdQty = new GridData();
		gridDataExtnOrdQty.horizontalAlignment = SWT.BEGINNING;
		gridDataExtnOrdQty.verticalAlignment = SWT.FILL;
		gridDataExtnOrdQty.grabExcessVerticalSpace = false;
		gridDataExtnOrdQty.grabExcessHorizontalSpace = true;
		gridDataExtnOrdQty.horizontalSpan = 2;
		gridDataExtnOrdQty.verticalSpan = 1;
		//gridDataExtnOrdQty.widthHint = 33;		
		
		/*Text txtDummy = new Text(pnlRightData, 72);
		txtDummy.setLayoutData(gridData);*/
				
		GridData gridDataExtnPrice = new GridData();
		gridDataExtnPrice.horizontalAlignment = SWT.BEGINNING;
		gridDataExtnPrice.verticalAlignment = SWT.FILL;
		gridDataExtnPrice.grabExcessVerticalSpace = false;
		gridDataExtnPrice.grabExcessHorizontalSpace = true;
		gridDataExtnPrice.horizontalSpan = 1;
		gridDataExtnPrice.verticalSpan = 1;
		gridDataExtnPrice.widthHint = 93;		
		
		//adding lblExtnOrdQty - new Field	part of bug # 2591	
		Label lblExtnOrdQty = new Label(pnlRightData, SWT.HORIZONTAL);
		lblExtnOrdQty.setText("ExtendedOrdredQty:");
		lblExtnOrdQty.setLayoutData(gridData);
		lblExtnOrdQty.setData("yrc:customType", "Label");
		lblExtnOrdQty.setData("name", "lblExtnOrdQty");
		
		txtExtnOrdQty = new StyledText(pnlRightData, SWT.SIMPLE);
		txtExtnOrdQty.setText("");
		txtExtnOrdQty.setEditable(false);
		txtExtnOrdQty.setLayoutData(gridDataExtnOrdQty);
		txtExtnOrdQty.setData("name", "txtExtnOrdQty");
		txtExtnOrdQty.setEnabled(false);
		
		Label lblExtnPrice = new Label(pnlRightData, SWT.HORIZONTAL);
		lblExtnPrice.setText("Extended:");
		lblExtnPrice.setLayoutData(gridDataExtnPrice);
		lblExtnPrice.setData("yrc:customType", "Label");
		lblExtnPrice.setData("name", "lblExtnPrice");
				
		
		// correcting displaying of this field on UI 
		GridData gridDataExtendedLbl1 = new GridData();
		gridDataExtendedLbl1.horizontalAlignment = SWT.BEGINNING;
		//gridDataExtendedLbl1.verticalAlignment = SWT.FILL;
		gridDataExtendedLbl1.grabExcessVerticalSpace = false;
		gridDataExtendedLbl1.grabExcessHorizontalSpace = false;
		gridDataExtendedLbl1.horizontalSpan = 2;
		gridDataExtendedLbl1.verticalSpan = 1;
		gridDataExtendedLbl1.widthHint = 50;
		gridDataExtendedLbl1.heightHint=18;
		
		txtExtnPrice = new StyledText(pnlRightData, SWT.SIMPLE);
		txtExtnPrice.setText("");
		txtExtnPrice.setEditable(false);
		txtExtnPrice.setLayoutData(gridDataExtendedLbl1);
		/*//adding a condition to get proper alignment & to display its value - during the newLine Creation
		if(isNewLine())
		txtExtnPrice.setLayoutData(gridDataLbl1);
		//txtExtnPrice.setLayoutData(gridDataLbl1);
		else txtExtnPrice.setLayoutData(gridDataExtendedLbl1);*/
		txtExtnPrice.setData("name", "txtExtnPrice");
		
		Label lblBaseUOM = new Label(pnlRightData, SWT.HORIZONTAL);
		lblBaseUOM.setText("Base UOM:");
		lblBaseUOM.setLayoutData(gridDataLbl2);
		lblBaseUOM.setData("yrc:customType", "Label");
		lblBaseUOM.setData("name", "lblBaseUOM");
		stxtBaseUOM = new StyledText(pnlRightData, SWT.SIMPLE);
		stxtBaseUOM.setText("");
		stxtBaseUOM.setLayoutData(gridDataLbl1);
		stxtBaseUOM.setEditable(false);
		stxtBaseUOM.setData("name", "stxtBaseUOM");
		stxtBaseUOM.setEnabled(false);
		
		Label lblCouponCode = new Label(pnlRightData, SWT.HORIZONTAL);
		lblCouponCode.setText("Coupon Code:");
		lblCouponCode.setLayoutData(gridDataLbl2);
		lblCouponCode.setData("yrc:customType", "Label");
		lblCouponCode.setData("name", "lblCouponCode");
		txtCouponCode = new Text(pnlRightData, this.couponCodeStyle);
		txtCouponCode.setLayoutData(gridDataLbl1);
		txtCouponCode.setData("name", "txtCouponCode");
		txtCouponCode.setText("");
		txtCouponCode.setTextLimit(12);
		
	}

	private void createCustInfoComposite() {

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 2;
		gridLayout1.verticalSpacing = 5;
		gridLayout1.numColumns = 8;

		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = 2;
		gridDataPnl.grabExcessHorizontalSpace = false;
		gridDataPnl.grabExcessVerticalSpace = true;
		 gridDataPnl.verticalSpan = 2;
		gridDataPnl.widthHint = 970;
		gridDataPnl.verticalAlignment = 4;
		gridDataPnl.horizontalSpan=6;

		pnlCustInfo = new Composite(pnlMainData, 0);
		pnlCustInfo.setLayoutData(gridDataPnl);
		pnlCustInfo.setLayout(gridLayout1);
		pnlCustInfo.setData("name", "pnlCustInfo");
		//XPXUtils.paintPanel(pnlCustInfo);	
//		XPXUtils.addGradientPanelHeader(pnlCustInfo, "Customer Information:", true);
		GridData gridDataLbl = new GridData();
		gridDataLbl.horizontalAlignment = SWT.BEGINNING;
		gridDataLbl.verticalAlignment = SWT.CENTER;
		gridDataLbl.grabExcessVerticalSpace = true;
		gridDataLbl.grabExcessHorizontalSpace = false;
		gridDataLbl.horizontalSpan = 1;
//		gridDataLbl.verticalSpan = 2;
		gridDataLbl.widthHint = 110;
		
		
		GridData gridDataLblCustVal = new GridData();
		gridDataLblCustVal.horizontalAlignment = SWT.BEGINNING;
		gridDataLblCustVal.verticalAlignment = SWT.BEGINNING;
		gridDataLblCustVal.grabExcessVerticalSpace = true;
		gridDataLblCustVal.grabExcessHorizontalSpace = false;
		gridDataLblCustVal.horizontalSpan = 1;
//		gridDataLbl.verticalSpan = 2;
		gridDataLblCustVal.widthHint = 100;
		
		GridData gridDatadummy1 = new GridData();
		gridDatadummy1.horizontalAlignment = SWT.BEGINNING;
		gridDatadummy1.verticalAlignment = SWT.CENTER;
		gridDatadummy1.grabExcessVerticalSpace = true;
		gridDatadummy1.grabExcessHorizontalSpace = true;
		gridDatadummy1.horizontalSpan = 7;
//		gridDatadummy1.verticalSpan = 2;
		gridDatadummy1.widthHint = 100;
		
		GridData gridDatadummy2 = new GridData();
		gridDatadummy2.horizontalAlignment = SWT.BEGINNING;
		gridDatadummy2.verticalAlignment = SWT.CENTER;
		gridDatadummy2.grabExcessVerticalSpace = true;
		gridDatadummy2.grabExcessHorizontalSpace = true;
		gridDatadummy2.horizontalSpan = 5;
//		gridDatadummy2.verticalSpan = 2;
		gridDatadummy2.widthHint = 100;
		
		GridData gridDatadummy3 = new GridData();
		gridDatadummy3.horizontalAlignment = SWT.BEGINNING;
		gridDatadummy3.verticalAlignment = SWT.CENTER;
		gridDatadummy3.grabExcessVerticalSpace = true;
		gridDatadummy3.grabExcessHorizontalSpace = true;
		gridDatadummy3.horizontalSpan = 5;
//		gridDatadummy3.verticalSpan = 2;
		gridDatadummy3.widthHint = 100;
		
		

		GridData gridDataVal = new GridData();
		gridDataVal.horizontalAlignment = SWT.BEGINNING;
		gridDataVal.verticalAlignment = SWT.CENTER;
		gridDataVal.grabExcessVerticalSpace = false;
		gridDataVal.grabExcessHorizontalSpace = true;
		gridDataVal.horizontalSpan = 1;
//		gridDataVal.verticalSpan = 2;
		gridDataVal.widthHint = 100;
		
		GridData gridDatacustVal = new GridData();
		gridDatacustVal.horizontalAlignment = SWT.BEGINNING;
		gridDatacustVal.verticalAlignment = SWT.CENTER;
		gridDatacustVal.grabExcessVerticalSpace = false;
		gridDatacustVal.grabExcessHorizontalSpace = true;
		gridDatacustVal.horizontalSpan = 3;
//		gridDatacustVal.verticalSpan = 2;
		gridDatacustVal.widthHint = 100;
		
		
		GridData gridDataProductDescription = new GridData();
		gridDataProductDescription.horizontalSpan = 4;
//		gridDataProductDescription.verticalSpan=2;
		gridDataProductDescription.widthHint = -1;
		gridDataProductDescription.heightHint = 30;
		gridDataProductDescription.horizontalAlignment = 4;
		gridDataProductDescription.grabExcessHorizontalSpace = true;
		gridDataProductDescription.grabExcessVerticalSpace=true;
		
		GridData gridDatadummyProdDescription = new GridData();
		gridDatadummyProdDescription.horizontalSpan = 3;
//		gridDatadummyProdDescription.verticalSpan=2;
		gridDatadummyProdDescription.widthHint = -1;
		gridDatadummyProdDescription.heightHint = 30;
		gridDatadummyProdDescription.horizontalAlignment = 4;
		gridDatadummyProdDescription.grabExcessHorizontalSpace = true;
		gridDatadummyProdDescription.grabExcessVerticalSpace = true;
		
		
		if(isB2BCustomerOrder() && !isNewLine()){
		lblOrginalLegacyItem = new Label(pnlCustInfo, SWT.HORIZONTAL);
		lblOrginalLegacyItem.setText("o.Legacy Item #: ");
		lblOrginalLegacyItem.setLayoutData(gridDataLbl);
		lblOrginalLegacyItem.setData("yrc:customType", "Label");
		lblOrginalLegacyItem.setData("name", "lblOrginalLegacyItem");
		txtOrginalLegacyItem = new Text(pnlCustInfo, 2048);
		txtOrginalLegacyItem.setLayoutData(gridDataVal);
		txtOrginalLegacyItem.setData("name", "txtOrginalLegacyItem");

		lblOriginalCustomerItem = new Label(pnlCustInfo, SWT.HORIZONTAL);
		lblOriginalCustomerItem.setText("o.Customer Item #: ");
		lblOriginalCustomerItem.setLayoutData(gridDataLbl);
		lblOriginalCustomerItem.setData("yrc:customType", "Label");
		lblOriginalCustomerItem.setData("name", "lblOriginalCustomerItem");
		txtOriginalCustomerItem = new Text(pnlCustInfo, 2048);
		txtOriginalCustomerItem.setLayoutData(gridDataVal);
		txtOriginalCustomerItem.setData("name", "txtOriginalCustomerItem");
		
		lblOriginalOrderQuantity = new Label(pnlCustInfo, SWT.HORIZONTAL);
		lblOriginalOrderQuantity.setText("o.Order Qty:");
		lblOriginalOrderQuantity.setLayoutData(gridDataLbl);
		lblOriginalOrderQuantity.setData("yrc:customType", "Label");
		lblOriginalOrderQuantity.setData("name", "lblOriginalOrderQuantity");
		txtOriginalOrderQuantity = new Text(pnlCustInfo, 2048);
		txtOriginalOrderQuantity.setLayoutData(gridDataVal);
		txtOriginalOrderQuantity.setData("name", "txtOriginalOrderQuantity");

		lblOriginalRequiredUOM = new Label(pnlCustInfo, SWT.HORIZONTAL);
		lblOriginalRequiredUOM.setText("o. Req UOM :");
		lblOriginalRequiredUOM.setLayoutData(gridDataLbl);
		lblOriginalRequiredUOM.setData("yrc:customType", "Label");
		lblOriginalRequiredUOM.setData("name", "lblOriginalRequiredUOM");
		txtOriginalRequiredUOM = new Text(pnlCustInfo, 2048);
		txtOriginalRequiredUOM.setLayoutData(gridDataVal);
		txtOriginalRequiredUOM.setData("name", "txtOriginalRequiredUOM");
	
		lblOProductDescription = new Label(pnlCustInfo, SWT.HORIZONTAL);
		lblOProductDescription.setText("Description :");
		lblOProductDescription.setLayoutData(gridDataLbl);
		lblOProductDescription.setData("yrc:customType", "Label");
		lblOProductDescription.setData("name", "lblOProductDescription");
		txtOProductDescription = new Text(pnlCustInfo, SWT.V_SCROLL|SWT.MULTI|SWT.WRAP|SWT.BORDER);
		txtOProductDescription.setLayoutData(gridDataProductDescription);
		txtOProductDescription.setData("name", "txtOProductDescription");
		
		Label lblODummyProductDescription = new Label(pnlCustInfo, 16777216);
		lblODummyProductDescription.setText("");
		lblODummyProductDescription.setLayoutData(gridDatadummyProdDescription);
		lblODummyProductDescription.setData("yrc:customType", "Label");
		lblODummyProductDescription.setData("name", "lblODummyProductDescription");
	
		lblOrginalUnitPrice = new Label(pnlCustInfo, SWT.HORIZONTAL);
		lblOrginalUnitPrice.setText("o.Unit Price: ");
		lblOrginalUnitPrice.setLayoutData(gridDataLbl);
		lblOrginalUnitPrice.setData("yrc:customType", "Label");
		lblOrginalUnitPrice.setData("name", "lblOrginalUnitPrice");
		txtOrginalUnitPrice = new Text(pnlCustInfo, 2048);
		txtOrginalUnitPrice.setLayoutData(gridDataVal);
		txtOrginalUnitPrice.setData("name", "txtOrginalUnitPrice");
		
		
		lblOriginalPriceUOM = new Label(pnlCustInfo, SWT.HORIZONTAL);
		lblOriginalPriceUOM.setText("o.  Price UOM: ");
		lblOriginalPriceUOM.setLayoutData(gridDataLbl);
		lblOriginalPriceUOM.setData("yrc:customType", "Label");
		lblOriginalPriceUOM.setData("name", "lblOriginalPriceUOM");
		txtOriginalPriceUOM = new Text(pnlCustInfo, 2048);
		txtOriginalPriceUOM.setLayoutData(gridDataVal);
		txtOriginalPriceUOM.setData("name", "txtOriginalPriceUOM");
		
	
		lblOriginalLineTotal = new Label(pnlCustInfo, SWT.HORIZONTAL);
		lblOriginalLineTotal.setText("o. Line Total:");
		lblOriginalLineTotal.setLayoutData(gridDataLbl);
		lblOriginalLineTotal.setData("yrc:customType", "Label");
		lblOriginalLineTotal.setData("name", "lblOriginalLineTotal");
		txtOriginalLineTotal = new Text(pnlCustInfo, 2048);
		txtOriginalLineTotal.setLayoutData(gridDataVal);
		txtOriginalLineTotal.setData("name", "txtOriginalLineTotal");
		
		
		lblOriginalTax = new Label(pnlCustInfo, SWT.HORIZONTAL);
		lblOriginalTax.setText("o. Tax:");
		lblOriginalTax.setLayoutData(gridDataLbl);
		lblOriginalTax.setData("yrc:customType", "Label");
		lblOriginalTax.setData("name", "lblOriginalTax");
		txtOriginalTax = new Text(pnlCustInfo, 2048);
		txtOriginalTax.setLayoutData(gridDataVal);
		txtOriginalTax.setData("name", "txtOriginalTax");
		}


		String customerLevelLineSeqNoFlag = "Y";
//		Commented check from Customer, because according to JIRA 1315, this should be always displayed in Order Summary screen.
//		String customerLevelLineSeqNoFlag = YRCXmlUtils.getAttributeValue(
//				shipToCustomerEle,
//				"/CustomerList/Customer/Extn/@ExtnCustLineSeqNoFlag");
		
		if (isChecked(customerLevelLineSeqNoFlag)) {
			lblCustLineSeq = new Label(pnlCustInfo, SWT.HORIZONTAL);
			lblCustLineSeq.setText("Line # ");
			lblCustLineSeq.setLayoutData(gridDataLbl);
			lblCustLineSeq.setData("yrc:customType", "Label");
			lblCustLineSeq.setData("name", "lblCustLineSeq");
			txtCustLineSeq = new Text(pnlCustInfo, 2048);
			txtCustLineSeq.setLayoutData(gridDataVal);
			txtCustLineSeq.setData("name", "txtCustLineSeq");
			txtCustLineSeq.setTextLimit(10);
		}
		String customerLevelLinePOFlag = YRCXmlUtils.getAttributeValue(shipToCustomerEle,"/CustomerList/Customer/Extn/@ExtnCustLinePONoFlag");
		if(isLineType(lineType)&& isChecked(customerLevelLinePOFlag)){
			lblCustLinePONo = new Label(pnlCustInfo, SWT.HORIZONTAL);
			lblCustLinePONo.setText("PO# ");
			lblCustLinePONo.setLayoutData(gridDataLbl);
			lblCustLinePONo.setData("yrc:customType", "Label");
			lblCustLinePONo.setData("name", "lblCustLinePONo");
			txtCustLinePONo = new Text(pnlCustInfo, 2048);
			txtCustLinePONo.setLayoutData(gridDataVal);
			txtCustLinePONo.setData("name", "txtCustLinePONo");
			txtCustLinePONo.setTextLimit(18);
		}

		String customerLevelLineAccNoFlag = YRCXmlUtils.getAttributeValue(
				shipToCustomerEle,
				"/CustomerList/Customer/Extn/@ExtnCustLineAccNoFlag");
		if (isLineType(lineType)&& isChecked(customerLevelLineAccNoFlag)) {
			String customerLevelLineAccNoLabel = YRCXmlUtils.getAttributeValue(
					shipToCustomerEle,
					"/CustomerList/Customer/Extn/@ExtnCustLineAccLbl");
			lblCustLineAccNo = new Label(pnlCustInfo, SWT.HORIZONTAL);
			lblCustLineAccNo.setText("Account#:");
			lblCustLineAccNo.setLayoutData(gridDataLbl);
			lblCustLineAccNo.setData("yrc:customType", "Label");
			lblCustLineAccNo.setData("name", "lblCustLineAccNo");
			txtCustLineAccNo = new Text(pnlCustInfo, 2048);
			txtCustLineAccNo.setLayoutData(gridDataVal);
			txtCustLineAccNo.setData("name", "txtCustLineAccNo");
			txtCustLineAccNo.setTextLimit(20);
		}
		
		if(isB2BCustomerOrder()&& !isNewLine()){
			if( txtCustLinePONo != null && !(isChecked(customerLevelLineSeqNoFlag)) && 	!(isChecked(customerLevelLineAccNoFlag))){
				txtCustLinePONo.setLayoutData(gridDatadummy1);
			}
			else if( txtCustLinePONo != null && (isChecked(customerLevelLineSeqNoFlag)) && 	!(isChecked(customerLevelLineAccNoFlag))){
				txtCustLinePONo.setLayoutData(gridDatadummy2);
			}
			if( txtCustLineAccNo != null && !(isChecked(customerLevelLineSeqNoFlag)) && 	(isChecked(customerLevelLineAccNoFlag))){
				 txtCustLineAccNo.setLayoutData(gridDatadummy3);
			}
		}
		
		
		 if(isB2BCustomerOrder() && !isNewLine()){
			Label lblOriginalComments = new Label(pnlCustInfo, SWT.HORIZONTAL);
			lblOriginalComments.setText("Comments");
			lblOriginalComments.setLayoutData(gridDataLbl);
			lblOriginalComments.setData("yrc:customType", "Label");
			lblOriginalComments.setData("name", "lblOriginalComments");
			txtOriginalComments = new Text(pnlCustInfo, 2048);
			txtOriginalComments.setLayoutData(gridDatadummyProdDescription);
			txtOriginalComments.setData("name", "txtOriginalComments");

			Label lblOReqDeliveryDate= new Label(pnlCustInfo, SWT.HORIZONTAL|SWT.WRAP);
			lblOReqDeliveryDate.setText("o. Req Delivery Date:");
			lblOReqDeliveryDate.setLayoutData(gridDataLblCustVal);
			lblOReqDeliveryDate.setData("yrc:customType", "Label");
			lblOReqDeliveryDate.setData("name", "lblOReqDeliveryDate");
			txtOReqDeliveryDate = new Text(pnlCustInfo, 2048);
			txtOReqDeliveryDate.setLayoutData(gridDatacustVal);
			txtOReqDeliveryDate.setData("name", "txtOReqDeliveryDate");
		 }
			
		 GridData gridDataLbl10 = new GridData();
		 gridDataLbl10.horizontalAlignment = SWT.BEGINNING;
		 gridDataLbl10.verticalAlignment = SWT.CENTER;
		 gridDataLbl10.grabExcessVerticalSpace = true;
		 gridDataLbl10.grabExcessHorizontalSpace = false;
		 gridDataLbl10.horizontalSpan = 1;
		 gridDataLbl10.widthHint = 110;
		 
		//  txtCustLine1
		String custLine1Flag = YRCXmlUtils.getAttributeValue(shipToCustomerEle,
				"/CustomerList/Customer/Extn/@ExtnCustLineField1Flag");
		if (isLineType(lineType) && isChecked(custLine1Flag)){
			String custLine1Label = YRCXmlUtils.getAttributeValue(
					shipToCustomerEle,
					"/CustomerList/Customer/Extn/@ExtnCustLineField1Label");
			if(!YRCPlatformUI.isVoid(custLine1Label)){
				lblCustLine1 = new Label(pnlCustInfo, SWT.HORIZONTAL | SWT.WRAP);
				lblCustLine1.setText(custLine1Label);
				lblCustLine1.setLayoutData(gridDataLbl10);
				lblCustLine1.setData("yrc:customType", "Label");
				lblCustLine1.setData("name", "lblCustLine1");
				txtCustLine1 = new Text(pnlCustInfo, 2048);
				txtCustLine1.setLayoutData(gridDataVal);
				txtCustLine1.setData("name", "txtCustLine1");
				txtCustLine1.setTextLimit(20);
			}
		}


				String custLine2Flag = YRCXmlUtils.getAttributeValue(shipToCustomerEle,
				"/CustomerList/Customer/Extn/@ExtnCustLineField2Flag");
				if (isLineType(lineType) && isChecked(custLine2Flag)) {
			String custLine2Label = YRCXmlUtils.getAttributeValue(
					shipToCustomerEle,
					"/CustomerList/Customer/Extn/@ExtnCustLineField2Label");
			if(!YRCPlatformUI.isVoid(custLine2Label)){
				lblCustLine2 = new Label(pnlCustInfo, SWT.HORIZONTAL | SWT.WRAP);
				lblCustLine2.setText(custLine2Label);
				lblCustLine2.setLayoutData(gridDataLbl10);
				lblCustLine2.setData("yrc:customType", "Label");
				lblCustLine2.setData("name", "lblCustLine2");
				txtCustLine2 = new Text(pnlCustInfo, 2048);
				txtCustLine2.setLayoutData(gridDataVal);
				txtCustLine2.setData("name", "txtCustLine2");
				txtCustLine2.setTextLimit(20);
			}
		}

				String custLine3Flag = YRCXmlUtils.getAttributeValue(shipToCustomerEle,
				"/CustomerList/Customer/Extn/@ExtnCustLineField3Flag");
				if (isLineType(lineType) && isChecked(custLine3Flag)){
			String custLine3Label = YRCXmlUtils.getAttributeValue(
					shipToCustomerEle,
					"/CustomerList/Customer/Extn/@ExtnCustLineField3Label");
			if(!YRCPlatformUI.isVoid(custLine3Label)){
				lblCustLine3 = new Label(pnlCustInfo, SWT.HORIZONTAL | SWT.WRAP);
				lblCustLine3.setText(custLine3Label);
				lblCustLine3.setLayoutData(gridDataLbl10);
				lblCustLine3.setData("yrc:customType", "Label");
				lblCustLine3.setData("name", "lblCustLine3");
				txtCustLine3 = new Text(pnlCustInfo, 2048);
				txtCustLine3.setLayoutData(gridDataVal);
				txtCustLine3.setData("name", "txtCustLine3");
				txtCustLine3.setTextLimit(20);
			}
		}
		if(!isNewLine()&& !YRCPlatformUI.isVoid(legacyOrderNumber))
		{
			hiddenTxtOrderingUOM = new Text(pnlCustInfo, 2048);
			hiddenTxtOrderingUOM.setText("");
			hiddenTxtOrderingUOM.setLayoutData(gridDataLbl);
			hiddenTxtOrderingUOM.setData("name", "hiddenTxtOrderingUOM");	
			hiddenTxtOrderingUOM.setVisible(false);
		}
		
		hiddenTxtbaseUOM = new Text(pnlCustInfo, SWT.SIMPLE);
		hiddenTxtbaseUOM.setText("");
		hiddenTxtbaseUOM.setLayoutData(gridDataLbl);
		hiddenTxtbaseUOM.setVisible(false);
		hiddenTxtbaseUOM.setData("name", "hiddenTxtbaseUOM");

		
		
		hiddenStxtPricingUOM = new StyledText(pnlCustInfo, SWT.SIMPLE);
		hiddenStxtPricingUOM.setText("");
		hiddenStxtPricingUOM.setLayoutData(gridDataLbl);
		hiddenStxtPricingUOM.setVisible(false);
		hiddenStxtPricingUOM.setData("name", "hiddenStxtPricingUOM");
		
		Control[] controls=customerInfoControlList(); 
		getControlStatus(controls);
	
	}
	/**
	 * 
	 */
	private void hideCustomerControlsInfo(boolean flag) {
		
		if(!YRCPlatformUI.isVoid(lblCustLinePONo)){
			lblCustLinePONo.setVisible(flag);
		}
		if(!YRCPlatformUI.isVoid(txtCustLinePONo)){
			txtCustLinePONo.setVisible(flag);
		}
		if(!YRCPlatformUI.isVoid(lblCustLineAccNo)){
			lblCustLineAccNo.setVisible(flag);
		}
		if(!YRCPlatformUI.isVoid(txtCustLineAccNo)){
			txtCustLineAccNo.setVisible(flag);
		}
		if(!YRCPlatformUI.isVoid(lblCustLine1)){
			lblCustLine1.setVisible(flag);
		}
		if(!YRCPlatformUI.isVoid(txtCustLine1)){
			txtCustLine1.setVisible(flag);
		}
		if(!YRCPlatformUI.isVoid(lblCustLine2)){
			lblCustLine2.setVisible(flag);
		}
		if(!YRCPlatformUI.isVoid(txtCustLine2)){
			txtCustLine2.setVisible(flag);
		}
		if(!YRCPlatformUI.isVoid(lblCustLine3)){
			lblCustLine3.setVisible(flag);
		}
		if(!YRCPlatformUI.isVoid(txtCustLine3)){
			txtCustLine3.setVisible(flag);
		}
		
	}

	private void createOptionsComposite() {
		GridLayout gridLayoutPnl = new GridLayout();
		gridLayoutPnl.numColumns = 2;
		
		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = SWT.FILL;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.grabExcessVerticalSpace = true;
		gridDataPnl.horizontalSpan = 2;
		gridDataPnl.verticalAlignment = 2;


		pnlOptions = new Composite(LinePnl, 0);
		pnlOptions.setLayoutData(gridDataPnl);
		pnlOptions.setLayout(gridLayoutPnl);
		pnlOptions.setData("name", "pnlOptions");
//		XPXUtils.paintPanel(pnlOptions);
		
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = SWT.END;
		gridData1.horizontalSpan=1;
		gridData1.grabExcessHorizontalSpace = true;
		
		btnSplitLine = new Button(pnlOptions, SWT.END);
		btnSplitLine.setText("Copy");
		btnSplitLine.setLayoutData(gridData1);
		btnSplitLine.setToolTipText(YRCPlatformUI.getString("SplitLine"));
		btnSplitLine.setData("name", "btnSplitLine");
		if (isNewLine()) {
			btnSplitLine.setEnabled(false);
		}
		if(YRCPlatformUI.equals(XPXConstants.CHARGE_TYPE, lineType)){
			btnSplitLine.setEnabled(false);
		}
		btnSplitLine.addSelectionListener(selectionAdapter);

		
		GridData gridData2 = new GridData();
		gridData2.horizontalSpan=1;
		gridData1.grabExcessHorizontalSpace = true;
		
		btnRemoveLine = new Button(pnlOptions, SWT.END);
		btnRemoveLine.setText("Delete");
		btnRemoveLine.setLayoutData(gridData2);
		btnRemoveLine.setToolTipText(YRCPlatformUI.getString("RemoveLine"));
		btnRemoveLine.setData("name", "btnRemoveLine");
		
		if (!isDraftOrder()) {
			if(XPXUtils.isFullFillmentOrder(orderEle)){
				String legacyOrderNumber = YRCXmlUtils.getAttributeValue(orderEle, "/Order/Extn/@ExtnLegacyOrderNo");
					if(!YRCPlatformUI.isVoid(legacyOrderNumber)){
						// changes as part of bug# 2542 - adding another
						if(!isNewLine())
						comboShipfrom.setEnabled(false);
						if(!YRCPlatformUI.isVoid(txtOrderingUOM)){
						txtOrderingUOM.setEnabled(false);
						}
					}
					if("M".equals(lineType)){
						btnRemoveLine.setEnabled(false);
						stxtUnitPrice.setEnabled(false);
						if(!YRCPlatformUI.isVoid(txtCustLinePONo)){
						txtCustLinePONo.setEnabled(false);
						}
						comboShipfrom.setEnabled(false);
						if(!YRCPlatformUI.isVoid(txtOrderingUOM)){
							txtOrderingUOM.setEnabled(false);
						}
						chkPriceOverride.setEnabled(false);
						txtComment.setEnabled(false);
					}
					else{
						btnRemoveLine.setEnabled(true);
					}
			}
			else{
				btnRemoveLine.setEnabled(false);
			}
		}
		btnRemoveLine.addSelectionListener(selectionAdapter);
		if(isDraftOrder()){
			btnRemoveLine.setEnabled(true);
			if(isNewLine()){
				btnRemoveLine.setEnabled(true);
			}
			
		}

	}
	public void createAvailabilityComposite() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.verticalSpacing = 2;
		gridLayout1.numColumns = 1;
		gridLayout1.marginWidth = 5;
		gridLayout1.marginHeight = 5;

		GridData gridDataPnl1 = new GridData();
		gridDataPnl1.horizontalAlignment = 4;
		gridDataPnl1.grabExcessHorizontalSpace = true;
		gridDataPnl1.grabExcessVerticalSpace = false;
		gridDataPnl1.horizontalSpan=1;
		// gridDataPnl.verticalSpan = 2;
		gridDataPnl1.widthHint = 300;
		gridDataPnl1.verticalAlignment = 4;
		pnlOthersMain = new Composite(LinePnl, 0);
		pnlOthersMain.setLayoutData(gridDataPnl1);
		pnlOthersMain.setLayout(gridLayout1);
		pnlOthersMain.setData("name", "pnlOthersMain");

		XPXUtils.paintPanel(pnlOthersMain);	
		
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 0;
		gridLayout2.verticalSpacing = 2;
		gridLayout2.numColumns = 4;
		gridLayout2.marginWidth = 0;
		gridLayout2.marginHeight = 5;

		GridData gridDataPnl2 = new GridData();
		gridDataPnl2.horizontalAlignment = 3;
		gridDataPnl2.grabExcessHorizontalSpace = true;
		gridDataPnl2.grabExcessVerticalSpace = false;
		gridDataPnl2.horizontalSpan=4;
		// gridDataPnl.verticalSpan = 2;
		gridDataPnl2.widthHint = 290;
		gridDataPnl2.verticalAlignment = 4;
		
		pnlOthers = new Composite(pnlOthersMain, 0);
		pnlOthers.setLayoutData(gridDataPnl2);
		pnlOthers.setLayout(gridLayout2);
		pnlOthers.setData("name", "pnlOthers");
		
}
	public void addWareHouseDetails(Element wareHouseLocation, int recordCount) {
		GridData gridDivLbHeader = new GridData();
		gridDivLbHeader.horizontalAlignment = SWT.BEGINNING;
		gridDivLbHeader.verticalAlignment = SWT.FILL;
		gridDivLbHeader.grabExcessVerticalSpace = true;
		gridDivLbHeader.grabExcessHorizontalSpace = false;
		gridDivLbHeader.minimumWidth=90;

		GridData gridLbBohHeader = new GridData();
		gridLbBohHeader.horizontalAlignment = SWT.FILL;
		gridLbBohHeader.verticalAlignment = SWT.BOTTOM;
		gridLbBohHeader.grabExcessVerticalSpace = false;
		gridLbBohHeader.grabExcessHorizontalSpace = true;
		gridLbBohHeader.horizontalSpan = 1;
		gridLbBohHeader.verticalSpan = 1;
		gridLbBohHeader.widthHint = 21;
		
			
		GridData gridLbDaysHeader = new GridData();
		gridLbDaysHeader.horizontalAlignment = SWT.FILL;
		gridLbDaysHeader.verticalAlignment = SWT.BOTTOM;
		gridLbDaysHeader.grabExcessVerticalSpace = false;
		gridLbDaysHeader.grabExcessHorizontalSpace = true;
		gridLbDaysHeader.horizontalSpan = 1;
		gridLbDaysHeader.verticalSpan = 1;
		gridLbDaysHeader.widthHint = 39;
		
		GridData gridLbUOMHeader = new GridData();
		gridLbUOMHeader.horizontalIndent=18;
		gridLbUOMHeader.horizontalAlignment = SWT.FILL;
		gridLbUOMHeader.verticalAlignment = SWT.BOTTOM;
		gridLbUOMHeader.grabExcessVerticalSpace = false;
		gridLbUOMHeader.grabExcessHorizontalSpace = true;
		gridLbUOMHeader.horizontalSpan = 1;
		gridLbUOMHeader.verticalSpan = 1;
		gridLbUOMHeader.widthHint = 39;
		
		GridData gridDataDivsion = new GridData();
		gridDataDivsion.horizontalAlignment = SWT.BEGINNING;
		gridDataDivsion.verticalAlignment = SWT.FILL|SWT.WRAP;
		gridDataDivsion.grabExcessVerticalSpace = true;
		gridDataDivsion.grabExcessHorizontalSpace = true;
		gridDataDivsion.minimumWidth = 80;
		
		GridData gridDataAvailQtyVal = new GridData();
		gridDataAvailQtyVal.horizontalAlignment = SWT.BEGINNING;
		gridDataAvailQtyVal.verticalAlignment = SWT.FILL|SWT.WRAP;
		gridDataAvailQtyVal.grabExcessVerticalSpace = true;
		gridDataAvailQtyVal.grabExcessHorizontalSpace = true;
		gridDataAvailQtyVal.minimumWidth = 35;
		
		
		GridData gridDataUOM = new GridData();
		gridDataUOM.horizontalAlignment = SWT.BEGINNING;
		gridDataUOM.horizontalIndent=18;
		gridDataUOM.verticalAlignment = SWT.FILL|SWT.WRAP;
		gridDataUOM.grabExcessVerticalSpace = true;
		gridDataUOM.grabExcessHorizontalSpace = true;
		gridDataUOM.minimumWidth = 60;
		
		GridData gridDataDays = new GridData();
		gridDataDays.horizontalAlignment = SWT.CENTER;
		gridDataDays.horizontalIndent=18;
		gridDataDays.verticalAlignment = SWT.FILL|SWT.WRAP;
		gridDataDays.grabExcessVerticalSpace = true;
		gridDataDays.grabExcessHorizontalSpace = true;
		gridDataDays.minimumWidth = 30;
		
		
		Element eleCustDetails = (Element) getOrderLinesPanel().getPageBehavior().getCustomerDetails();
		Element custDetailsExtnElement = YRCXmlUtils.getChildElement(eleCustDetails, "Extn");
		String envCode = custDetailsExtnElement.getAttribute("ExtnEnvironmentCode");
		String wareHouseTmp = YRCXmlUtils.getChildElement(wareHouseLocation,
		"Warehouse").getTextContent();
		String divisionCode1=wareHouseTmp+"_"+envCode;
		String wareHouse=XPXUtils.divisionMap.get(divisionCode1);
		
		
//		String wareHouse = YRCXmlUtils.getChildElement(wareHouseLocation,
//				"Warehouse").getTextContent();
		String noOfDays = YRCXmlUtils.getChildElement(wareHouseLocation,
				"NumberOfDays").getTextContent();
		String availableQuantity = YRCXmlUtils.getChildElement(
				wareHouseLocation, "AvailableQty").getTextContent();
		String requestedQtyUOM = YRCXmlUtils.getChildElement(
				wareHouseLocation, "RequestedQtyUOM").getTextContent();		

		if (null != wareHouse) {
			if(recordCount==0)
			{
				GridData gridDataHeader = new GridData();
				gridDataHeader.horizontalAlignment = SWT.CENTER;
				gridDataHeader.verticalAlignment = SWT.BOTTOM;
				gridDataHeader.grabExcessVerticalSpace = false;
				gridDataHeader.grabExcessHorizontalSpace = true;
				gridDataHeader.horizontalSpan = 4;
				gridDataHeader.verticalSpan = 1;
				gridDataHeader.widthHint = 70;
		
				
				Label lblHeader = new Label(pnlOthers, SWT.HORIZONTAL);
				lblHeader.setText("Availability");
				lblHeader.setLayoutData(gridDataHeader);
				lblHeader.setData("yrc:customType", "Label");
				setTheme(lblHeader, "Label");

				Label lblBillingMTValHeader = new Label(pnlOthers, SWT.HORIZONTAL);
				lblBillingMTValHeader.setText("Divison");
				lblBillingMTValHeader.setLayoutData(gridDivLbHeader);
				lblBillingMTValHeader.setData("yrc:customType", "Label");
				setTheme(lblBillingMTValHeader, "Label");

				Label lblAvailQtyValHeader = new Label(pnlOthers, SWT.HORIZONTAL);
				lblAvailQtyValHeader.setText("BOH");
				lblAvailQtyValHeader.setLayoutData(gridLbBohHeader);
				lblAvailQtyValHeader.setData("yrc:customType", "Label");
				setTheme(lblAvailQtyValHeader, "Label");
				
				Label lblUomHeader = new Label(pnlOthers, SWT.HORIZONTAL);
				lblUomHeader.setText("UOM");
				lblUomHeader.setLayoutData(gridLbUOMHeader);
				lblUomHeader.setData("yrc:customType", "Label");
				setTheme(lblUomHeader, "Label");
				
				Label lblNoOfDaysHeader = new Label(pnlOthers, SWT.HORIZONTAL);
				lblNoOfDaysHeader.setText("#Days");
				lblNoOfDaysHeader.setLayoutData(gridLbDaysHeader);
				lblNoOfDaysHeader.setData("yrc:customType", "Label");
				setTheme(lblNoOfDaysHeader, "Label");
			}
			
				Label lblBillingMTVal = new Label(pnlOthers, SWT.WRAP);
				lblBillingMTVal.setText(wareHouse);
				lblBillingMTVal.setLayoutData(gridDataDivsion);
				lblBillingMTVal.setData("yrc:customType", "Label");
				setTheme(lblBillingMTVal, "Text");
			
				Label lblAvailQtyVal = new Label(pnlOthers, SWT.WRAP);
				lblAvailQtyVal.setText(availableQuantity);
				lblAvailQtyVal.setLayoutData(gridDataAvailQtyVal);
				lblAvailQtyVal.setData("yrc:customType", "Text");
				setTheme(lblAvailQtyVal, "Text");
				
				Label lblUom = new Label(pnlOthers, SWT.WRAP);
				lblUom.setText(requestedQtyUOM);
				lblUom.setLayoutData(gridDataUOM);
				lblUom.setData("yrc:customType", "Text");
				setTheme(lblUom, "Text");
				
				Label lblNoOfDays = new Label(pnlOthers, SWT.WRAP);
				lblNoOfDays.setText(noOfDays);
				lblNoOfDays.setLayoutData(gridDataDays);
				lblNoOfDays.setData("yrc:customType", "Text");
				setTheme(lblNoOfDays, "Text");
			
		}
		//pnlOthersMain.layout(true, true);
		pnlOthersMain.layout(true, true);
		LinePnl.layout(true,true);
		pnlOthersMain.layout(true, true);
			}
	public void clearWareHouseDetails(){
		Control childIterator[] = pnlOthers.getChildren();
		int noOfChildren = childIterator.length;
		for (int k = 0; k < noOfChildren; k++) {
			 Control con =(Control)childIterator[k];
			 con.dispose();
			
		}
	}
	private void setTheme(Control control, String theme){
		control.setBackground(YRCPlatformUI.getBackGroundColor(theme));
		control.setForeground(YRCPlatformUI.getForeGroundColor(theme));
		control.setFont(YRCPlatformUI.getFont(theme));	
	}



	private void updateNonBindedComponents() {

		if(pnlOrderLines.getPageBehavior().isReadOnlyPage() || isCancelledLine){
			Control[] controls = new Control[]{
					txtItemId,
					btnLookupImg,
					comboItemId,
					txtCustItem,
					txtCustomerItemId,
					txtMfrItemId,
					txtMpcItemId,
					comboLineType,
					txtCustLinePONo,
					txtComment,
					chkAcceptLineComments,
					txtShipFromBranch,
					txtCustLineSeq,
					txtCustLine1,
					txtCustLine2,
					txtCustLine3,
					txtQuantity,
					txtOrderingUOM,
					comboOrderingUOM,
					stxtUnitPrice,
					comboPricingUOM,
					chkPriceOverride,
					txtExtnPrice,
					txtAdjAmount,
					txtCustLineAccNo,
					chkCustLineAcctNo,
					chkCustLineSeqNo,
					btnSplitLine,
					txtOthrPrice,
					txtShippableQtyLineTotal,
					
					txtShippableQtyinReqUOM,
					txtOrderedQtyTotal,
					txtLineTotal,
					txtBackOrderedQryInReqUOM,
					comboShipfrom
					,txtOrginalLegacyItem,txtOriginalCustomerItem,txtOriginalOrderQuantity,txtOriginalRequiredUOM,txtOProductDescription,txtOrginalUnitPrice
					,txtOriginalPriceUOM,txtOriginalLineTotal,txtOriginalTax,txtOriginalComments,txtOReqDeliveryDate,btnRemoveLine
					
			};
			setControlsEnabled(controls, false);
		}
		if(!isNewLine()){
			Control[] controls = new Control[]{
					txtItemId,
					btnLookupImg,
					comboItemId,
					comboLineType,
					txtCustItem,
					txtCustomerItemId,
					txtMfrItemId,
					txtMpcItemId,
					txtShippableQtyinReqUOM,
					txtOrderedQtyTotal,
					txtLineTotal,
					txtExtnPrice,
					txtBackOrderedQryInReqUOM,
					txtShippableQtyLineTotal,
					txtAdjAmount					
			};
			setControlsEnabled(controls, false);
		}
		if(isNewLine()){
			Control[] controls = new Control[]{
					txtShippableQtyinReqUOM,
					txtOrderedQtyTotal,
					txtLineTotal,
					txtExtnPrice,
					txtBackOrderedQryInReqUOM,
					txtShippableQtyLineTotal,
					txtAdjAmount
					
			};
			Element orderLineTmp=myBehavior.getLocalModel("OrderLineTmp");
			String PrimeLineNo=YRCXmlUtils.getAttributeValue(orderLineTmp, "/OrderLine/@PrimeLineNo");
			setControlsEnabled(controls, false);
			if(YRCPlatformUI.isVoid(PrimeLineNo)) //--For Checking whether line is new or copied
				hideCustomerControlsInfo(false);
		}
					
		if(YRCPlatformUI.equals(XPXConstants.CHARGE_TYPE, lineType)){
			txtItemId.setVisible(true);
			comboItemId.setVisible(true);
//			btnLookupImg.setVisible(false);
			txtQuantity.setEnabled(false);
			
		}else{
			lblComboItemId.setVisible(false);
			comboItemId.setVisible(false);
		}
		
		String status=YRCXmlUtils.getAttributeValue(this.orderEle,"/Order/@MinOrderStatus");
		if(!YRCPlatformUI.isVoid(status)){
			Double minOrderStatus=Double.valueOf(status);
			if(1100.5450 < minOrderStatus){
				btnSplitLine.setEnabled(false);
				btnRemoveLine.setEnabled(false);
				txtQuantity.setEnabled(false);
				stxtUnitPrice.setEnabled(false);
//				txtComment.setEnabled(false);
			}
		}
		/* Start - Changes made to fix issue 3173 */
		if(XPXUtils.isFullFillmentOrder(this.orderEle)){
			Control[] controls = new Control[]{
					txtComment					
			};
			setControlsEnabled(controls, false);
		}
		/* End - Changes made to fix issue 3173 */
		// LineComment
		String strLineComment = (String)YRCXPathUtils.evaluate(this.eleOrderLine, "/OrderLine/Instructions/Instruction[@InstructionType='LINE']/@InstructionText", XPathConstants.STRING);
		this.myBehavior.setFieldValue("txtComment", strLineComment);
		strOldLineComment = strLineComment;
		// Adj Amount - commenting these below 2 lines as we already mapped strAdjAmount field with ExtnLegOrderLineAdjustments value in bindings method
		//String strAdjAmount = (String)YRCXPathUtils.evaluate(this.eleOrderLine, "/OrderLine/LineCharges/LineCharge[@ChargeCategory='ADJUSTMENT_AMOUNT']/@ChargeAmount", XPathConstants.STRING);
		//		this.myBehavior.setFieldValue("txtAdjAmount", strAdjAmount);
		
				// Coupon Code
		/*String strCouponCode = (String)YRCXPathUtils.evaluate(this.eleOrderLine, "/OrderLine/Awards/Award/@AwardId", XPathConstants.STRING);
		this.myBehavior.setFieldValue("txtCouponCode", strCouponCode);*/
		//	In case of create Order default ShipFromDiv from the Customer.
		if(pnlOrderLines.getPageBehavior().isFromOrderEntryWizard() || isNewLine()){
			this.myBehavior.setFieldValue("txtShipFromBranch", pnlOrderLines.getPageBehavior().getDefaultShipNode());
			}
		if(null != txtCustLinePONo && txtCustLinePONo.isDisposed()==false)
			txtCustLinePONo.setTextLimit(18);
	}

	private void setControlsEnabled(Control[] controls, boolean enabled) { 
//		controls = pnlRoot.getChildren();
//		pnlRoot.setEnabled(false);
		for (Control control : controls) {
			if(null != control)
				control.setEnabled(enabled);
		}
		
	}
	private void setBindingForComponents() {
		YRCStyledTextBindingData stbd = null;
		YRCTextBindingData tbd = null;
		YRCComboBindingData cbd = null;
		YRCLabelBindingData lblbd = null;

		// Item Details Panel
		if (null != txtItemId) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/Item/@ItemID");
			//tbd.setTargetBinding("OrderLineNS:/OrderLine/Item/@ItemID");
			// tbd.setMandatory(true);
			tbd.setName("txtItemId");
			txtItemId.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}
		if(null != comboItemId){
			cbd = new YRCComboBindingData();
			cbd.setCodeBinding("@CodeValue");
			cbd.setDescriptionBinding("@CodeShortDescription");
			cbd.setListBinding("ChargesList:/CommonCodeList/CommonCode");
			cbd.setSourceBinding("OrderLineTmp:/OrderLine/Item/@ItemID");
			//cbd.setTargetBinding("OrderLineNS:/OrderLine/Item/@ItemID");
			cbd.setName("comboItemId");
			comboItemId.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		}
		if (!YRCPlatformUI.isVoid(lblErrItemId)) {
			lblbd = new YRCLabelBindingData();
			lblbd.setSourceBinding("OrderLineTmp:/OrderLine/Error/@ItemIDErrorText");
			lblbd.setName("lblErrItemId");
			lblErrItemId.setData(YRCConstants.YRC_LABEL_BINDING_DEFINITION, lblbd);
		}
		
		if(null !=stxtLegacyLineNumber){
			stbd = new YRCStyledTextBindingData();
			stbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnLegacyLineNumber");
//			if(isNewLine()){
//				stbd.setSourceBinding("PnAResponse:/Item/@PricingUOM");
//				stbd.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnPricingUOM");
//			}
			stbd.setName("stxtLegacyLineNumber");
			stxtLegacyLineNumber.setData(YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
		}
		
		if (null != txtCustomerItemId) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/Item/@CustomerItem");
			//tbd.setTargetBinding("OrderLineNS:/OrderLine/Item/@ItemID");
			// tbd.setMandatory(true);
			tbd.setName("txtCustomerItemId");
			txtCustomerItemId.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}
		
		if (null != txtMfrItemId) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/Item/@ManufacturerItem");
			//tbd.setTargetBinding("OrderLineNS:/OrderLine/Item/@ItemID");
			// tbd.setMandatory(true);
			tbd.setName("txtMfrItemId");
			txtMfrItemId.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}
		
		if (null != txtMpcItemId) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/Order/Extn/@ExtnMpc");
			//tbd.setTargetBinding("OrderLineNS:/OrderLine/Item/@ItemID");
			// tbd.setMandatory(true);
			tbd.setName("txtMpcItemId");
			txtMpcItemId.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}
		if (!YRCPlatformUI.isVoid(lblErrCustItem)) {
			lblbd = new YRCLabelBindingData();
			lblbd.setSourceBinding("OrderLineTmp:/OrderLine/Error/@CustomerItemErrorText");
			lblbd.setName("lblErrCustItem");
			lblErrCustItem.setData(YRCConstants.YRC_LABEL_BINDING_DEFINITION, lblbd);
		}
		
		tbd = new YRCTextBindingData();
		if((!YRCPlatformUI.isVoid(lineType) && lineType.equals("M")) || (!YRCPlatformUI.isVoid(lineType) && lineType.equals("C"))){
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/Item/@ItemShortDesc");}
		else{
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/ItemDetails/PrimaryInformation/@ExtendedDescription");
		}
		tbd.setName("txtItemDesc");
		// stbd.setDynamic(true);
		txtItemDesc.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@CodeValue");
		cbd.setDescriptionBinding("@CodeShortDescription");
		cbd.setListBinding("LineTypes:/CommonCodeList/CommonCode");
		cbd.setSourceBinding("OrderLineTmp:/OrderLine/@LineType");
		cbd.setTargetBinding("OrderLineNS:/OrderLine/@LineType");
		cbd.setName("comboLineType");
		comboLineType.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		
		if (!YRCPlatformUI.isVoid(lblErrLineType)) {
			lblbd = new YRCLabelBindingData();
			lblbd.setSourceBinding("OrderLineTmp:/OrderLine/@LineTypeErrorText");
			lblbd.setName("lblErrLineType");
			lblErrLineType.setData(YRCConstants.YRC_LABEL_BINDING_DEFINITION, lblbd);
		}
		cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@DivisionNo");
		cbd.setDescriptionBinding("@DivisionName");
		cbd.setListBinding("TransferCirclesList:/XPXXferCircleList/XPXXferCircle");
		cbd.setSourceBinding("OrderLineTmp:/OrderLine/@ShipNode");
		cbd.setTargetBinding("OrderLineNS:/OrderLine/@ShipNode");
		cbd.setName("comboShipfrom");
		comboShipfrom.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		
//		txtCustLinePONo
		if (null != txtCustLinePONo) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/@CustomerPONo");
			tbd.setTargetBinding("OrderLineNS:/OrderLine/@CustomerPONo");
			tbd.setName("txtCustLinePONo");
			// tbd.setMandatory(true);
			txtCustLinePONo.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,
					tbd);
		}
		// txtCustLineSeq
		if (null != txtCustLineSeq) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/@CustomerLinePONo");
			tbd.setTargetBinding("OrderLineNS:/OrderLine/@CustomerLinePONo");
			tbd.setName("txtCustLineSeq");
			txtCustLineSeq.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,
					tbd);
		}
		if (null != txtCustLineAccNo) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnCustLineAccNo");
			tbd.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnCustLineAccNo");
			tbd.setName("txtCustLineAccNo");
			txtCustLineAccNo.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,
					tbd);
		}
		
//		txtCustLine1
		if (null != txtCustLine1) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnCustLineField1");
			tbd.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnCustLineField1");
			tbd.setName("txtCustLine1");
			txtCustLine1.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}
		
//		txtCustLine2
		if (null != txtCustLine2) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnCustLineField2");
			tbd.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnCustLineField2");
			tbd.setName("txtCustLine2");
			txtCustLine2.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}
//		txtCustLine3
		if (null != txtCustLine3) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnCustLineField3");
			tbd.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnCustLineField3");
			tbd.setName("txtCustLine3");
			txtCustLine3.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}

// Quantity Panel
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("OrderLineTmp:/OrderLine/OrderLineTranQuantity/@OrderedQty");
		tbd.setTargetBinding("OrderLineNS:/OrderLine/OrderLineTranQuantity/@OrderedQty");
		tbd.setName("txtQuantity");
	//	tbd.setDataType("Quantity");
		txtQuantity.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);

		
		// UOM Panel

		if(null !=txtOrderingUOM){
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/OrderLineTranQuantity/@TransactionalUOMDesc");
//			tbd.setTargetBinding("OrderLineNS:/OrderLine/OrderLineTranQuantity/@TransactionalUOM");
			tbd.setName("txtOrderingUOM");
			txtOrderingUOM.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}
		if(null !=hiddenTxtOrderingUOM){
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/OrderLineTranQuantity/@TransactionalUOM");
			tbd.setTargetBinding("OrderLineNS:/OrderLine/OrderLineTranQuantity/@TransactionalUOM");
			tbd.setName("hiddenTxtOrderingUOM");
			hiddenTxtOrderingUOM.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}		
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("OrderLineTmp:/OrderLine/Item/@UnitOfMeasure");
		
		tbd.setName("hiddenTxtbaseUOM");
		hiddenTxtbaseUOM.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		
		
		if(null != comboOrderingUOM){
            cbd = new YRCComboBindingData();
            cbd.setCodeBinding("@UnitOfMeasure");
            cbd.setDescriptionBinding("@UnitOfMeasureDesc;@Conversion");
            cbd.setListBinding("UOMList:UOMList/UOM");
            
            //cbd.setSourceBinding("OrderLineTmp:/OrderLine/Item/@UnitOfMeasure");
            //cbd.setTargetBinding("OrderLineNS:/OrderLine/OrderLineTranQuantity/@TransactionalUOM");
            
            if(isNewLine()){
            cbd.setKey("DisplayConversionKey");
            cbd.setSourceBinding("OrderLineTmp:/OrderLine/Item/@UnitOfMeasure");
            cbd.setTargetBinding("OrderLineNS:/OrderLine/OrderLineTranQuantity/@TransactionalUOM");
            }else{     
            	  cbd.setKey("DisplayConversionKey");
                  cbd.setSourceBinding("OrderLineTmp:/OrderLine/OrderLineTranQuantity/@TransactionalUOM");
                  cbd.setTargetBinding("OrderLineNS:/OrderLine/OrderLineTranQuantity/@TransactionalUOM");
            }
            cbd.setName("comboOrderingUOM");
            comboOrderingUOM.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);

		}

		/*can be removed as it is being populated in behaviour class itself*/
		
		if(null !=hiddenStxtPricingUOM){
			stbd = new YRCStyledTextBindingData();
			stbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnPricingUOM");
			if(isNewLine()){
			//	stbd.setSourceBinding("PnAResponse:/PriceAndAvailability/Items/Item/@PricingUOM");
				stbd.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnPricingUOM");
			}
			stbd.setName("hiddenStxtPricingUOM");
			hiddenStxtPricingUOM.setData(YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
		}
		
		if(null != comboPricingUOM){
			cbd = new YRCComboBindingData();
			cbd.setCodeBinding("@UnitOfMeasure");
			cbd.setDescriptionBinding("@UnitOfMeasureDesc");
			cbd.setListBinding("UOMList:UOMList/UOM");
			cbd.setSourceBinding("OrderLineTmp:/OrderLine/Item/@UnitOfMeasure");
			cbd.setTargetBinding("OrderLineNS:/OrderLine/LinePriceInfo/@PricingUOM");
			cbd.setName("comboPricingUOM");
			comboPricingUOM.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		}
		
		if (!YRCPlatformUI.isVoid(lblReqUOM)) {
			lblbd = new YRCLabelBindingData();
			lblbd.setSourceBinding("OrderLineTmp:/OrderLine/OrderLineTranQuantity/@TransactionalUOMDesc");
			lblbd.setName("lblReqUOM");
			lblReqUOM.setData(YRCConstants.YRC_LABEL_BINDING_DEFINITION, lblbd);
		}
		
		if (!YRCPlatformUI.isVoid(lblReqUOMShippableQty)) {
			lblbd = new YRCLabelBindingData();
			lblbd.setSourceBinding("OrderLineTmp:/OrderLine/OrderLineTranQuantity/@TransactionalUOMDesc");
			lblbd.setName("lblReqUOMShippableQty");
			lblReqUOMShippableQty.setData(YRCConstants.YRC_LABEL_BINDING_DEFINITION, lblbd);
		}
		
		stbd = new YRCStyledTextBindingData();
		stbd.setSourceBinding("OrderLineTmp:/OrderLine/Item/@UnitOfMeasureDesc");
		stbd.setName("stxtBaseUOM");
		// stbd.setDynamic(true);
		stxtBaseUOM.setData(YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);

// Pricing Panel
		tbd = new YRCTextBindingData();
//		if (pnlOrderLines.getPageBehavior().orderStatusLessThanPlaced() 
//				&& XPXUtils.isCustomerOrder(pnlOrderLines.getPageBehavior().getOrderDetails())) {
//			tbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnUnitPriceDiscount");
//			tbd.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnUnitPriceDiscount");
//		}else{
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnUnitPrice");
			tbd.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnUnitPrice");
//		}
//		tbd.setSourceBinding("OrderLineTmp:/OrderLine/LinePriceInfo/@UnitPrice");
//		tbd.setTargetBinding("OrderLineNS:/OrderLine/LinePriceInfo/@UnitPrice");
		tbd.setName("stxtUnitPrice");
		// stbd.setDynamic(true);
		stxtUnitPrice.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		

		// txtExtnPrice
		stbd = new YRCStyledTextBindingData();
		stbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnExtendedPrice");
		//stbd.setCurrencyXPath("OrderLineTmp:/OrderLine/@Currency");
		//tbd.setTargetBinding("OrderLineNS:/OrderLine/LineOverallTotals/@ExtendedPrice");
		stbd.setName("txtExtnPrice");
		txtExtnPrice.setData(YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
		
		//Added as part of CR's Changes - 2591
		stbd = new YRCStyledTextBindingData();
		stbd.setSourceBinding("OrderLineTmp:/OrderLine/@OriginalOrderedQty");
		stbd.setName("txtExtnOrdQty");
		txtExtnOrdQty.setData(YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
		
		
		// txtExtnPrice
		if (!pnlOrderLines.getPageBehavior().isFromOrderEntryWizard()) {

			stbd = new YRCStyledTextBindingData();
			//Setting up txtAdjAmount field with ExtnLegOrderLineAdjustments value  & also setting currency which is set in OrdLineTmp -in OrdLinePanelBheavior- internal bug
		stbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnLegOrderLineAdjustments");
			//stbd.setSourceBinding("OrderLineTmp:/OrderLine/LineCharges/LineCharge/@ChargeAmount");
		stbd.setCurrencyXPath("OrderLineTmp:/OrderLine/@Currency");
			stbd.setName("txtAdjAmount");
			txtAdjAmount.setData(YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
			}
		YRCButtonBindingData chkBoxBindingData = null;
		if(null != chkPriceOverride){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
			chkBoxBindingData.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnPriceOverrideFlag");
			chkBoxBindingData.setTargetBinding("OrderLineNS:/OrderLine/Extn/@ExtnPriceOverrideFlag");
			chkBoxBindingData.setName("chkPriceOverride");
			chkPriceOverride.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
		if(null != chkCustLineAcctNo){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
			chkBoxBindingData.setName("chkCustLineAcctNo");
			chkCustLineAcctNo.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
		if(null != chkCustLineSeqNo){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
			chkBoxBindingData.setName("chkCustLineSeqNo");
			chkCustLineSeqNo.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
		if(null != chkAllowBackOrder){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
			chkBoxBindingData.setName("chkAllowBackOrder");
			chkAllowBackOrder.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
		if(null != chkPreventBackOrder){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
			chkBoxBindingData.setName("chkPreventBackOrder");
			chkPreventBackOrder.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
		if(null != chkPriceDiscrepency){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
			chkBoxBindingData.setName("chkPriceDiscrepency");
			chkPriceDiscrepency.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}
		if(null != chkAcceptLineComments){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
			chkBoxBindingData.setName("chkAcceptLineComments");
			chkAcceptLineComments.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}		
		if(null != chkGTMVariance){
			chkBoxBindingData = new YRCButtonBindingData();
			chkBoxBindingData.setCheckedBinding("Y");
			chkBoxBindingData.setUnCheckedBinding("N");
			chkBoxBindingData.setName("chkGTMVariance");
			chkGTMVariance.setData("YRCButtonBindingDefination", chkBoxBindingData);
		}			

		if (!YRCPlatformUI.isVoid(lblErrReqDlvryDt)) {
			lblbd = new YRCLabelBindingData();
			lblbd.setSourceBinding("OrderLineTmp:/OrderLine/Error/@ReqDeliveryDateErrorText");
			lblbd.setName("lblErrReqDlvryDt");
			lblErrReqDlvryDt.setData(YRCConstants.YRC_LABEL_BINDING_DEFINITION, lblbd);
		} 
		
		if(null != txtOrginalLegacyItem){

	        tbd = new YRCTextBindingData();
	        tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@ItemID");
			tbd.setName("txtOrginalLegacyItem");
			txtOrginalLegacyItem.setData("YRCTextBindingDefination", tbd);
		
			}
	        if(null != txtOriginalCustomerItem){

	            tbd = new YRCTextBindingData();
	            tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@CustomerProductCode");
	    		tbd.setName("txtOriginalCustomerItem");
	    		txtOriginalCustomerItem.setData("YRCTextBindingDefination", tbd);
	    	
	    		}
	        if(null != txtOriginalOrderQuantity){

	            tbd = new YRCTextBindingData();
	            tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@OrderedQty");
	    		tbd.setName("txtOriginalOrderQuantity");
	    		txtOriginalOrderQuantity.setData("YRCTextBindingDefination", tbd);
	    	
	    		}

	        if(null != txtOProductDescription){

	            tbd = new YRCTextBindingData();
	            tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@ItemDesc");
	    		tbd.setName("txtOProductDescription");
	    		txtOProductDescription.setData("YRCTextBindingDefination", tbd);
	    	
	    		}
	        if(null != txtOrginalUnitPrice){

	            tbd = new YRCTextBindingData();
	            tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@UnitPrice");
	    		tbd.setName("txtOrginalUnitPrice");
	    		txtOrginalUnitPrice.setData("YRCTextBindingDefination", tbd);
	    	
	    		}

	        if(null != txtOriginalComments){

	            tbd = new YRCTextBindingData();
	            tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@LineNotes");
	    		tbd.setName("txtOriginalComments");
	    		txtOriginalComments.setData("YRCTextBindingDefination", tbd);
	    	
	    		}
	        if(null != txtOReqDeliveryDate){

	            tbd = new YRCTextBindingData();
	            tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@ReqLineDelvryDate");
	            tbd.setDataType("Date");
	            tbd.setName("txtOReqDeliveryDate");
	    		txtOReqDeliveryDate.setData("YRCTextBindingDefination", tbd);
	    	
	    		}
//		stxtOthrCost
		stbd = new YRCStyledTextBindingData();
		stbd.setSourceBinding("PnAResponse:/Item/@Cost");
		stbd.setName("stxtOthrCost");
		// stbd.setDynamic(true);
		stxtOthrCost.setData(YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
		
        if(!isNewLine()){
	//		txtShippableQtyinReqUOM - /OrderLine/Extn/@ExtnReqShipOrdQty
	        //tbd = new YRCTextBindingData();
        	stbd = new YRCStyledTextBindingData();
        	stbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnReqShipOrdQty");
        	stbd.setName("txtShippableQtyinReqUOM");		
			txtShippableQtyinReqUOM.setData(YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
	
	//		txtBackOrderedQryInReqUOM - /OrderLine/Extn/@ExtnReqBackOrdQty
	        tbd = new YRCTextBindingData();
			tbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnReqBackOrdQty");
			tbd.setName("txtBackOrderedQryInReqUOM");
			txtBackOrderedQryInReqUOM.setData("YRCTextBindingDefination", tbd);
	
			stbd = new YRCStyledTextBindingData();
			stbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnLineShippableTotal");
			stbd.setCurrencyXPath("OrderLineTmp:/OrderLine/@Currency");
			stbd.setName("txtShippableQtyLineTotal");
			txtShippableQtyLineTotal.setData(YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
			String test = txtShippableQtyLineTotal.getText();
			
			
			
			stbd = new YRCStyledTextBindingData();
			stbd.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnLineOrderedTotal");
			stbd.setCurrencyXPath("OrderLineTmp:/OrderLine/@Currency");
			stbd.setName("txtLineTotal");
			txtLineTotal.setData(YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
			
			
			
	
	//		txtOrderedQtyTotal - /OrderLine/Extn/@ExtnLineOrderedTotal
	       	YRCTextBindingData stbd1 = new YRCTextBindingData();
	      //stbd1.setSourceBinding("OrderLineTmp:/OrderLine/LineOverallTotals/@LineTotal");
	       	stbd1.setSourceBinding("OrderLineTmp:/OrderLine/Extn/@ExtnLineOrderedTotal");
	       	stbd1.setCurrencyXPath("OrderLineTmp:/OrderLine/@Currency");
	       	//	stbd1.setCurrencyXPath("OrderDetails:/Order/PriceInfo/@Currency");
	       	stbd1.setName("txtOrderedQtyTotal");
			txtOrderedQtyTotal.setData("YRCTextBindingDefination", stbd1);
        }
		
        if(null != txtOrginalLegacyItem){

            tbd = new YRCTextBindingData();
            tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@ItemID");
    		tbd.setName("txtOrginalLegacyItem");
    		txtOrginalLegacyItem.setData("YRCTextBindingDefination", tbd);
    	
    		}
            if(null != txtOriginalCustomerItem){

                tbd = new YRCTextBindingData();
                tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@CustomerProductCode");
        		tbd.setName("txtOriginalCustomerItem");
        		txtOriginalCustomerItem.setData("YRCTextBindingDefination", tbd);
        	
        		}
            if(null != txtOriginalOrderQuantity){

                tbd = new YRCTextBindingData();
                tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@OrderedQty");
        		tbd.setName("txtOriginalOrderQuantity");
        		txtOriginalOrderQuantity.setData("YRCTextBindingDefination", tbd);
        	
        		}

            if(null != txtOProductDescription){

                tbd = new YRCTextBindingData();
                tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@ItemDesc");
        		tbd.setName("txtOProductDescription");
        		txtOProductDescription.setData("YRCTextBindingDefination", tbd);
        	
        		}
            if(null != txtOrginalUnitPrice){

                tbd = new YRCTextBindingData();
                tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@UnitPrice");
        		tbd.setName("txtOrginalUnitPrice");
        		txtOrginalUnitPrice.setData("YRCTextBindingDefination", tbd);
        	
        		}

            if(null != txtOriginalComments){

                tbd = new YRCTextBindingData();
                tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@LineNotes");
        		tbd.setName("txtOriginalComments");
        		txtOriginalComments.setData("YRCTextBindingDefination", tbd);
        	
        		}
            if(null != txtOReqDeliveryDate){

                tbd = new YRCTextBindingData();
                tbd.setSourceBinding("customerB2BOriginalOrder:/XPXRefOrderLine/@ReqLineDelvryDate");
                tbd.setDataType("Date");
                tbd.setName("txtOReqDeliveryDate");
        		txtOReqDeliveryDate.setData("YRCTextBindingDefination", tbd);
        	
        		}

		
//		txtCouponCode
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("OrderLineTmp:/OrderLine/Awards/Award/@AwardId");
//		tbd.setTargetBinding("OrderLineNS:/OrderLine/Awards/Award/@AwardId");
		tbd.setName("txtCouponCode");
		txtCouponCode.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
      
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

	public void setCompositeWidths() {
		int newTotalWidth = 0;
		int parentWidth = parent.getSize().x;
		int controlsExcluded = 0;
		int controlsAddedByExtensibility = 0;
		Control al[] = LinePnl.getChildren();
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

		linePanelWidth = LinePnl.getSize().x;
		pnlRoot.layout(true, true);
	}

	public boolean isNewLine() {
		return newLine;
	}

	public String getOrderLineKey() {
		return sOrderLineKey;
	}

	public OrderLinesPanel getOrderLinesPanel() {
		return pnlOrderLines;
	}

	public String getOldLineComment() {
		return strOldLineComment;
	}

	public boolean isFromOrderEntryWizardAndNotNewLine(){
		return (getOrderLinesPanel().getPageBehavior().isFromOrderEntryWizard());
	}
	private boolean isChecked(String arg){
		if("Y".equalsIgnoreCase(arg)){
			return true;
		}
		return false;
	}
	private boolean isLineType(String arg){
		if(isNewLine()||"P".equalsIgnoreCase(arg)||"S".equalsIgnoreCase(arg)){
			return true;
		}
		return false;
	}
	
	public boolean isDraftOrder() {
		String sMaxOrderStatus = YRCXmlUtils.getAttributeValue(orderEle,"/Order/@MaxOrderStatus");
		String draftOrderFl=YRCXmlUtils.getAttributeValue(this.orderEle,"/Order/@DraftOrderFlag");
		if ("Y".equalsIgnoreCase(draftOrderFl)){
			setDeleteButtonEnable();
		}
		if(YRCPlatformUI.isVoid(sMaxOrderStatus) && !"".equalsIgnoreCase(sMaxOrderStatus)){
			return true;
		}
		if("1100".compareTo(sMaxOrderStatus)>0 && !"".equalsIgnoreCase(sMaxOrderStatus)){
			return true;
		}else{
			return false;
		}
		
	}
	
	public void setDeleteButtonEnable(){
		btnRemoveLine.setEnabled(true);
		if(isNewLine()){
			btnRemoveLine.setEnabled(true);
		}	
		
	}
	
	public boolean isB2BCustomerOrder(){
		String msgLineId="";
 	   Element extnElement = YRCXmlUtils.getChildElement(eleOrderLine, "Extn");
 	   if(extnElement!=null){
		msgLineId = extnElement.getAttribute("ExtnMsgLineId");
 	   }
		
		 if(orderEle.getAttribute("EntryType").equals("B2B") &&orderEle.getAttribute("OrderType").equals("Customer")&& !("".equals(msgLineId)) )
			return true;	
		 else 
			 return false;
	}
	/**function is used to get a list of customerInformation controls **/ 
	private Control[] customerInfoControlList() {
		return new Control[] {lblOrginalLegacyItem,lblOProductDescription,lblOrginalUnitPrice,lblCustLinePONo,lblOriginalComments,
				lblOriginalCustomerItem,lblOriginalOrderQuantity,lblOriginalRequiredUOM,
				lblOriginalPriceUOM,lblOReqDeliveryDate,lblOriginalLineTotal,lblOriginalTax,
				txtOrginalUnitPrice,txtOriginalPriceUOM, txtOriginalComments, txtOReqDeliveryDate,txtOriginalLineTotal,
				txtOrginalLegacyItem, txtOriginalCustomerItem, txtOriginalOrderQuantity, txtOriginalRequiredUOM, txtOriginalLineTotal,
				txtOriginalTax,txtOProductDescription,lblCustLineAccNo,txtCustLineAccNo,txtCustLine1,
				lblCustLine1,lblCustLine2,lblCustLine3,txtCustLine3,txtCustLine2,lblCustLineSeq,txtCustLineSeq
		};
	}
	
	public boolean isPriceOverrideChecked(){
		boolean returnValue=false;
		Element extnorderLineEle=YRCXmlUtils.getChildElement(eleOrderLine, "Extn");
		 String chkValue=extnorderLineEle.getAttribute("ExtnPriceOverrideFlag");
		 
		 if("Y".equalsIgnoreCase(chkValue))
			 return true;
		
		return returnValue;
		
	}

}
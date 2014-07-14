package com.xpedx.sterling.rcp.pca.referenceorder.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCLinkBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTextBindingData;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXRefOrderHeaderPanel extends Composite implements IYRCComposite {

	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.orderheader.screen.OrderHeaderPanel";
	private Object inputObject;
	private Element eleOrderDetails;
	private Composite pnlRoot;
	private Label lblETradingID;
    private Text txtETradingID;
    
    private Text txtShipToAddress;
    private Text txtBillToAddress;
    private Text txtDateReceived;
    private Text txtComments;
    private Text txtCustomerPONO;
    private Text txtBillToID;
    private Link linkMasterCustomer;
    
    private Composite parent;
    private XPXReferenceOrderSummaryPanel pnlOrderLines;
	private XPXRefOrderHeaderPanelBehavior myBehavior;
	
	public XPXRefOrderHeaderPanel(Composite parent, XPXReferenceOrderSummaryPanel pnlOrderLines, int style,Object inObj, Element eleOrderDetails) {
		super(parent, style);
		this.parent = parent;
		this.inputObject = inObj;
		this.eleOrderDetails=eleOrderDetails;
		this.pnlOrderLines = pnlOrderLines;
		this.initialize();
		this.setBindingForComponents();
		myBehavior = new XPXRefOrderHeaderPanelBehavior(this, FORM_ID, inputObject, eleOrderDetails);
	}
	private void initialize() {
		GridLayout pnlLayout = new GridLayout();
		pnlLayout.horizontalSpacing = 2;
		pnlLayout.marginWidth = 3;
		pnlLayout.marginHeight = 3;
		pnlLayout.verticalSpacing = 2;
		pnlLayout.numColumns = 4;
		
		pnlRoot = new Composite(this, SWT.NONE);
		pnlRoot.setData("name", "pnlRoot");
		pnlRoot.setLayout(pnlLayout);
		XPXUtils.paintPanel(pnlRoot);
		this.createPnlHeaderInfo();
		this.setLayout(new FillLayout());
		this.setSize(new Point(470, 135));
	}

	private void setBindingForComponents() {
		YRCTextBindingData textBindingData = null;
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:/XPXRefOrderHdr/@EtradingID");
        textBindingData.setName("txtETradingID");
        txtETradingID.setData("YRCTextBindingDefination", textBindingData);
        
        if(!YRCPlatformUI.isVoid(txtBillToID)){
	        textBindingData = new YRCTextBindingData();
	        textBindingData.setSourceBinding("OrderDetails:/XPXRefOrderHdr/@BillToId");
	        textBindingData.setName("txtBillToID");
	        txtBillToID.setData("YRCTextBindingDefination", textBindingData);
        }
        
        if(null != linkMasterCustomer){
	        YRCLinkBindingData lnkdb = new YRCLinkBindingData();
			lnkdb.setSourceBinding("CustomerDetails:Customer/BuyerOrganization/@OrganizationName");
			lnkdb.setName("linkMasterCustomer");
			linkMasterCustomer.setData(YRCConstants.YRC_LINK_BINDING_DEFINATION,lnkdb);
        }
        
		textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:/XPXRefOrderHdr/@CustomerPONO");
        textBindingData.setName("txtCustomerPONO");
        txtCustomerPONO.setData("YRCTextBindingDefination", textBindingData);
        
		textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:/XPXRefOrderHdr/@InstructionText");
        textBindingData.setName("txtComments");
        txtComments.setData("YRCTextBindingDefination", textBindingData);

        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:/XPXRefOrderHdr/@Createts");
        textBindingData.setDataType("Date");
        textBindingData.setName("txtDateReceived");
        txtDateReceived.setData("YRCTextBindingDefination", textBindingData);
        
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:XPXRefOrderHdr/@BillToName;OrderDetails:XPXRefOrderHdr/@BillToAddr1;OrderDetails:XPXRefOrderHdr/@BillToCity;OrderDetails:XPXRefOrderHdr/@BillToState;XPXRefOrderHdr/@BillToZip");
        textBindingData.setKey("xpedx_address_key_For_Reference_Order");
        textBindingData.setName("txtBillToAddress");
        txtBillToAddress.setData("YRCTextBindingDefination", textBindingData);
        
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("OrderDetails:XPXRefOrderHdr/@ShipToName;OrderDetails:XPXRefOrderHdr/@ShipToAddr1;OrderDetails:XPXRefOrderHdr/@ShipToCity;OrderDetails:XPXRefOrderHdr/@ShipToState;OrderDetails:XPXRefOrderHdr/@ShipToZip");
        textBindingData.setKey("xpedx_address_key_For_Reference_Order");
        textBindingData.setName("txtShipToAddress");
        txtShipToAddress.setData("YRCTextBindingDefination", textBindingData);
        
    }
	
	public void createPnlHeaderInfo() {
		
		GridData gridDataLbl = new GridData();
        gridDataLbl.horizontalAlignment = SWT.FILL;
//        gridDataLbl.grabExcessHorizontalSpace=true;
        gridDataLbl.horizontalIndent = 0;
        gridDataLbl.verticalAlignment = 2;
        gridDataLbl.widthHint=130;
        gridDataLbl.horizontalSpan = 1;
//        gridDataLbl.widthHint = 90;
        GridData gridDataVal = new GridData();
        gridDataVal.horizontalAlignment = SWT.FILL;
        gridDataVal.grabExcessHorizontalSpace = true;
        gridDataVal.verticalAlignment = 2;
        gridDataVal.horizontalSpan = 3;
//        gridDataVal.widthHint = 90;
        
        lblETradingID = new Label(pnlRoot, SWT.LEFT);
        lblETradingID.setText("eTrading_ID");
        lblETradingID.setLayoutData(gridDataLbl);
        lblETradingID.setData("name", "lblETradingID");
        txtETradingID = new Text(pnlRoot, 72);
        GridData gdETradingID = new GridData();
        gdETradingID.horizontalAlignment = SWT.BEGINNING;
        gdETradingID.verticalAlignment = 2;
        gdETradingID.horizontalSpan = 3;
        txtETradingID.setLayoutData(gdETradingID);
        txtETradingID.setData("name", "txtETradingID");
        if("Y".equals(YRCXmlUtils.getAttribute(eleOrderDetails, "IsInvalidETradingID"))) 
        {   		
	        txtETradingID.setData("yrc:customType", "InvalidData");
	        txtETradingID.setToolTipText("Invalid eTrading ID");
		}
        
        Label lblBillToID = new Label(pnlRoot, SWT.LEFT);
        lblBillToID.setText("Customer");
        lblBillToID.setLayoutData(gridDataLbl);
        lblBillToID.setData("name", "lblBillToID");
//        txtBillToID = new Text(pnlRoot, 72);
//        txtBillToID.setLayoutData(gridDataVal);
//        txtBillToID.setData("name", "txtBillToID");
        linkMasterCustomer = new Link(pnlRoot, 0);
		linkMasterCustomer.setData("name", "linkMasterCustomer");
		linkMasterCustomer.setData("yrc:customType", "Link");
		linkMasterCustomer.setLayoutData(gridDataVal);
		linkMasterCustomer.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { public void
			widgetSelected( org.eclipse.swt.events.SelectionEvent e) {
					myBehavior.openDetailEditor((String) linkMasterCustomer.getData("name"));
					}
			});	
		
		Label lblCustomerPONO = new Label(pnlRoot, SWT.LEFT);
        lblCustomerPONO.setText("Customer_PO_NO");
        lblCustomerPONO.setLayoutData(gridDataLbl);
        lblCustomerPONO.setData("name", "lblCustomerPONO");
        txtCustomerPONO = new Text(pnlRoot, 72);
        txtCustomerPONO.setLayoutData(gridDataVal);
        txtCustomerPONO.setData("name", "txtCustomerPONO");
        
        Label lblComments = new Label(pnlRoot, SWT.LEFT);
        lblComments.setText("Comments");
        lblComments.setLayoutData(gridDataLbl);
        lblComments.setData("name", "lblComments");
        GridData gridDataTextArea = new GridData();
        gridDataTextArea.horizontalAlignment = SWT.BEGINNING;
        gridDataTextArea.verticalAlignment = SWT.BEGINNING;
        gridDataTextArea.verticalAlignment = 2;
        gridDataTextArea.horizontalSpan = 3;
        gridDataTextArea.widthHint = 300;
        gridDataTextArea.heightHint = 80;
        txtComments = new Text(pnlRoot, SWT.V_SCROLL|SWT.MULTI|SWT.WRAP|SWT.READ_ONLY|SWT.BORDER);
        txtComments.setLayoutData(gridDataTextArea);
        txtComments.setData("name", "txtComments");
        
        Label lblDateReceived = new Label(pnlRoot, SWT.LEFT);
        lblDateReceived.setText("Date_Received");
        lblDateReceived.setLayoutData(gridDataLbl);
        lblDateReceived.setData("name", "lblDateReceived");
        txtDateReceived = new Text(pnlRoot, 72);
        txtDateReceived.setLayoutData(gridDataVal);
        txtDateReceived.setData("name", "txtDateReceived");
        
        Label lblBillToAddress = new Label(pnlRoot, SWT.LEFT);
        lblBillToAddress.setText("Bill_To_Add");
        lblBillToAddress.setLayoutData(gridDataLbl);
        lblBillToAddress.setData("name", "lblBillToAddress");
        txtBillToAddress = new Text(pnlRoot, SWT.V_SCROLL|SWT.MULTI|SWT.WRAP|SWT.READ_ONLY|SWT.BORDER);
        txtBillToAddress.setLayoutData(gridDataTextArea);
        txtBillToAddress.setData("yrc:customType", "Text");
        txtBillToAddress.setData("name", "txtBillToAddress");
                
        Label lblShipToAddress = new Label(pnlRoot, SWT.LEFT);
        lblShipToAddress.setText("Ship_To_Add");
        lblShipToAddress.setLayoutData(gridDataLbl);
        lblShipToAddress.setData("name", "lblShipToAddress");
        txtShipToAddress = new Text(pnlRoot, SWT.V_SCROLL|SWT.MULTI|SWT.WRAP|SWT.READ_ONLY|SWT.BORDER);
        txtShipToAddress.setLayoutData(gridDataTextArea);
        txtShipToAddress.setData("yrc:customType", "Text");
        txtShipToAddress.setData("name", "txtShipToAddress");
        
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
	}
	public XPXRefOrderHeaderPanelBehavior getBehavior() {
		return myBehavior;
	}
	public XPXReferenceOrderSummaryPanel getOrderLinesPanel() {
		return pnlOrderLines;
	}

}

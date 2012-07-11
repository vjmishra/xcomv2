package com.xpedx.sterling.rcp.pca.customerprofilerule.screen;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCCellModifier;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.IYRCTableColumnTextProvider;
import com.yantra.yfc.rcp.YRCButtonBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCScrolledCompositeListener;
import com.yantra.yfc.rcp.YRCStyledTextBindingData;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class CustomerProfileMaintIntegDataPanel extends Composite implements IYRCComposite {

	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.customerprofilerule.screen.CustomerProfileMaintIntegDataPanel";
	public static final String EXTN_ETRADING_ID_OLD_VALUE = "ExtnETradingIDOldValue";
	public static final String EXTN_ETRADING_ID = "ExtnETradingID";
	public static final String EXTN_CUST_EMAIL_ADDRESS_OLD_VALUE = "ExtnEDIEmailAddressOldValue";
	public static final String EXTN_CUST_EMAIL_ADDRESS = "ExtnEDIEmailAddress";
	public static final String IS_EXTN_ETRADING_ID_MODIFIED = "IsExtnETradingIDModified";
	public static final String IS_EXTN_CUST_EMAIL_ADDRESS_MODIFIED = "IsExtnEDIEmailAddressModified";
	private CustomerProfileMaintIntegDataPanelBehavior myBehavior;
	private Composite pnlRoot;
	private ScrolledComposite bodyScrolledPanel;
	private Composite pnlBody;
	private Button btnUpdate;
	private Text txtIdentity;
    private Text txtSharedSecret;
    private Text txtStartPageURL;
    private Text txtXSLTFilename;
    private Button chkShowMyItems;
    private Button chkUNSPSCCodeReg;
    private Text txtReplaceChars;
    private Button chkUseOCISAPParam;
    private Text txtComments;
	
	private StyledText stxtMasterCustomerNo;
	private StyledText stxtMasterCustomerName;
//	Removed - JIRA 1109 - stxtCustomerNo,stxtCustomerName 
//	private StyledText stxtCustomerNo;
//	private StyledText stxtCustomerName;
    
	private Text txtBuyerID;
	private Button radIsEdiUom;
	private Button radIsXpedxUom;
	private Text txtSupportEmailAddr;
	private Button chkPurchaseOrd;
	private Text txtPOComments;
	private Button chkPOConfirmation;
	private Text txtPOConfirmationComments;
	private Button chkInvoice;
	private Text txtInvoiceComments;
	private Button chkPnAFlag;
	private Text txtPnAFlagComments;
	private Table tblShipCustomers;
	private Text txtUserNameParam;
	private Text txtUserpwdParam;
	private Text txtUserEmailTemplate;
	private Text txtPathOfUserId;
	
	public CustomerProfileMaintIntegDataPanel(Composite parent, int style, 
			Object inputObject, CustomerProfileMaintenance parentObj) {
		super(parent, style);
		initialize();
		setBindingForComponents();
		myBehavior = new CustomerProfileMaintIntegDataPanelBehavior(this, inputObject, FORM_ID, parentObj);
	}

	private void setBindingForComponents() {
		
		YRCTextBindingData tbd = null;
		YRCButtonBindingData chkBd = null;
		YRCButtonBindingData radBd = null;
		YRCStyledTextBindingData stbd = null;
		
//		Removed - JIRA 1109 - stxtCustomerNo,stxtCustomerName
//		stbd = new YRCStyledTextBindingData();
//		stbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/ParentCustomer/Customer/@CustomerID");
//		stbd.setName("stxtCustomerNo");
//		stxtCustomerNo.setData( YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
//		
//		stbd = new YRCStyledTextBindingData();
//		stbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/ParentCustomer/Customer/@OrganizationName");
//		stbd.setName("stxtCustomerName");
//		stxtCustomerName.setData( YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);

		stbd = new YRCStyledTextBindingData();
		stbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/ParentMasterCustomer/Customer/@CustomerID");
		stbd.setName("stxtMasterCustomerNo");
		stxtMasterCustomerNo.setData( YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
		
		stbd = new YRCStyledTextBindingData();
		stbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/ParentMasterCustomer/Customer/@OrganizationName");
		stbd.setName("stxtMasterCustomerName");
		stxtMasterCustomerName.setData( YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
		
		// PunchOut Panel Fields binding
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnCustIdentity");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnCustIdentity");
		tbd.setName("txtIdentity");
		txtIdentity.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnSharedSecret");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnSharedSecret");
		tbd.setName("txtSharedSecret");
		txtSharedSecret.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnStartPageURL");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnStartPageURL");
		tbd.setName("txtStartPageURL");
		txtStartPageURL.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnXSLTFileName");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnXSLTFileName");
		tbd.setName("txtXSLTFilename");
		txtXSLTFilename.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnCXmlUserXPath");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnCXmlUserXPath");
		tbd.setName("txtPathOfUserId");
		txtPathOfUserId.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		chkBd = new YRCButtonBindingData();
		chkBd.setCheckedBinding("Y");
		chkBd.setUnCheckedBinding("N");
		chkBd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnMyItemsLink");
		chkBd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnMyItemsLink");
		chkBd.setName("chkShowMyItems");
		chkShowMyItems.setData("YRCButtonBindingDefination", chkBd);
		
		chkBd = new YRCButtonBindingData();
		chkBd.setCheckedBinding("Y");
		chkBd.setUnCheckedBinding("N");
		chkBd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnUNSPSCRequired");
		chkBd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnUNSPSCRequired");
		chkBd.setName("chkUNSPSCCodeReg");
		chkUNSPSCCodeReg.setData("YRCButtonBindingDefination", chkBd);
	    
	    tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnReplaceCharacter");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnReplaceCharacter");
		tbd.setName("txtReplaceChars");
		txtReplaceChars.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		chkBd = new YRCButtonBindingData();
		chkBd.setCheckedBinding("Y");
		chkBd.setUnCheckedBinding("N");
		chkBd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnUseOCInSAPParamFlag");
		chkBd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnUseOCInSAPParamFlag");
		chkBd.setName("chkUseOCISAPParam");
		chkUseOCISAPParam.setData("YRCButtonBindingDefination", chkBd);

		
	    tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnUsernameParam");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnUsernameParam");
		tbd.setName("txtUserNameParam");
		txtUserNameParam.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
	    tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnUserEmailTemplate");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnUserEmailTemplate");
		tbd.setName("txtUserEmailTemplate");
		txtUserEmailTemplate.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
	    tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnUserPwdParam");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnUserPwdParam");
		tbd.setName("txtUserpwdParam");
		txtUserpwdParam.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
	    tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnPunchOutComments");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnPunchOutComments");
		tbd.setName("txtComments");
		txtComments.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		// B2B Transaction Attributes binding.
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnBuyerID");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnBuyerID");
		tbd.setName("txtBuyerID");
		txtBuyerID.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		radBd = new YRCButtonBindingData();
		radBd.setName("radIsEdiUom");
		radBd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnUomType");
		radBd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnUomType");
		radBd.setCheckedBinding("Y");
		radIsEdiUom.setData("YRCButtonBindingDefination", radBd);

		radBd = new YRCButtonBindingData();
		radBd.setName("radIsXpedxUom");
		radBd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnUomType");
		radBd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnUomType");
		radBd.setCheckedBinding("N");
		radIsXpedxUom.setData("YRCButtonBindingDefination", radBd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnSupportEmailAddress");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnSupportEmailAddress");
		tbd.setName("txtSupportEmailAddr");
		txtSupportEmailAddr.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		chkBd = new YRCButtonBindingData();
		chkBd.setCheckedBinding("Y");
		chkBd.setUnCheckedBinding("N");
		chkBd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnPurchaseOrderFlag");
		chkBd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnPurchaseOrderFlag");
		chkBd.setName("chkPurchaseOrd");
		chkPurchaseOrd.setData("YRCButtonBindingDefination", chkBd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnPurchaseOrderFlagComments");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnPurchaseOrderFlagComments");
		tbd.setName("txtPOComments");
		txtPOComments.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		chkBd = new YRCButtonBindingData();
		chkBd.setCheckedBinding("Y");
		chkBd.setUnCheckedBinding("N");
		chkBd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnPOAckFlag");
		chkBd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnPOAckFlag");
		chkBd.setName("chkPuchkPOConfirmationrchaseOrd");
		chkPOConfirmation.setData("YRCButtonBindingDefination", chkBd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnPOConfirmComments");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnPOConfirmComments");
		tbd.setName("txtPOConfirmationComments");
		txtPOConfirmationComments.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		chkBd = new YRCButtonBindingData();
		chkBd.setCheckedBinding("Y");
		chkBd.setUnCheckedBinding("N");
		chkBd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@Extn810InvoiceFlag");
		chkBd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@Extn810InvoiceFlag");
		chkBd.setName("chkInvoice");
		chkInvoice.setData("YRCButtonBindingDefination", chkBd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnInvoiceEDIComments");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnInvoiceEDIComments");
		tbd.setName("txtInvoiceComments");
		txtInvoiceComments.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		chkBd = new YRCButtonBindingData();
		chkBd.setCheckedBinding("Y");
		chkBd.setUnCheckedBinding("N");
		chkBd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnPnAEDIFlag");
		chkBd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnPnAEDIFlag");
		chkBd.setName("chkPnAFlag");
		chkPnAFlag.setData("YRCButtonBindingDefination", chkBd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnPnAEDIFlagComments");
		tbd.setTargetBinding("XPXCustomerOut:/Customer/Extn/@ExtnPnAEDIFlagComments");
		tbd.setName("txtPnAFlagComments");
		txtPnAFlagComments.setData( YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
        
	}

	private void initialize() {
		createRootPanel();
		this.setLayout(new FillLayout());
		setSize(new org.eclipse.swt.graphics.Point(1000, 200));
		
	}

	private void createRootPanel() {
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.verticalSpacing = 2;
		gridLayout5.marginHeight = 50;
		gridLayout5.marginWidth = 50;
		pnlRoot = new Composite(this, SWT.NONE);
//		pnlRoot.setData("yrc:customType", "TaskComposite");
		pnlRoot.setData("name", "pnlRoot");
		pnlRoot.setLayout(gridLayout5);
		showRootPanel(true);
		// createCompositeHeaderHolder();
		createScrollCmpstForPnlBody();
		createComposite();
		createPunchoutMaintComposite();
		createB2BMaintComposite();
		
		GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.BEGINNING;
        gridData.verticalAlignment = SWT.BEGINNING;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalIndent = 3;
//        gridData.widthHint = 500;
        
		btnUpdate = new Button(pnlBody, 0);
		btnUpdate.setText("Update");
		btnUpdate.setLayoutData(gridData);
		btnUpdate.setData("name", "btnUpdate");
		btnUpdate.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected( org.eclipse.swt.events.SelectionEvent e) {
				 myBehavior.update();
				 
			}
		}
		);
		
		adjustScrollPnl(bodyScrolledPanel, pnlBody,
				getRootPanel(), true, true);
		
	}
	public void adjustScrollPnl(ScrolledComposite scrPnl, Composite scrChild,
			Composite scrParent, boolean isHScrollReqd, boolean isVScrollReqd) {

		Control childIterator[] = scrChild.getChildren();
		int noOfChildren = childIterator.length;
		int HEIGHT = 5;
		int WIDTH = 5;
		int selectedHeight = 0;
		int selectedPanelHeight = 0;
		for (int k = 0; k < noOfChildren; k++) {
			int boundHeight = childIterator[k].getBounds().height;
			int boundWidth = childIterator[k].getBounds().width;
			if (isVScrollReqd) {

				HEIGHT += 10;
				if (WIDTH < boundWidth)
					WIDTH = boundWidth;
			}
			if (!isHScrollReqd)
				continue;
			WIDTH += boundWidth + 5;
			if (HEIGHT < boundHeight)
				HEIGHT = boundHeight;
		}
//		scrPnl.setMinSize(1000, HEIGHT+500);
		 scrPnl.setMinSize(1000, 1000);
		if (isVScrollReqd
				&& (selectedHeight < scrPnl.getOrigin().y || selectedHeight
						+ selectedPanelHeight > scrPnl.getSize().y
						+ scrPnl.getOrigin().y))
			scrPnl.setOrigin(0, selectedHeight);
		scrParent.layout(true, true);
	}

	private void createPunchoutMaintComposite() {
		GridLayout gridLayout5 = new GridLayout(2,true);
		gridLayout5.verticalSpacing = 2;
		
		GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.BEGINNING;
        gridData.verticalAlignment = SWT.TOP;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalIndent = 3;
        gridData.widthHint = 500;
        
		Composite pnlPunchOut = new Composite(pnlBody, SWT.NONE);
//		pnlPunchOut.setData("yrc:customType", "TaskComposite");
		pnlPunchOut.setData("name", "pnlPunchOut");
		pnlPunchOut.setLayout(gridLayout5);
		pnlPunchOut.setLayoutData(gridData);
		
        XPXUtils.addGradientPanelHeader(pnlPunchOut, "Punch_Out_Maintenance",true);
		
     // Controls 
		GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = SWT.END;
        gridData1.widthHint = 200;
        gridData1.grabExcessHorizontalSpace = true;
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = SWT.BEGINNING;
        gridData2.widthHint = 200;

        // Identity
        Label lblIdentity = new Label(pnlPunchOut, SWT.RIGHT);
        lblIdentity.setText("Identity");
        lblIdentity.setLayoutData(gridData1);
        lblIdentity.setData("name", "lblIdentity");
        txtIdentity = new Text(pnlPunchOut, SWT.BORDER);
        txtIdentity.setLayoutData(gridData2);
        txtIdentity.setData("name", "txtIdentity");
        txtIdentity.setText("12345678");

//      Shared Secret
        Label lblSharedSecret = new Label(pnlPunchOut, SWT.RIGHT);
        lblSharedSecret.setText("Shared_Secret");
        lblSharedSecret.setLayoutData(gridData1);
        lblSharedSecret.setData("name", "lblSharedSecret");
        txtSharedSecret = new Text(pnlPunchOut, SWT.BORDER);
        txtSharedSecret.setLayoutData(gridData2);
        txtSharedSecret.setData("name", "txtSharedSecret");
        txtSharedSecret.setText("12345678B");
        txtSharedSecret.setTextLimit(30);
        
//      Start Page URL
        Label lblStartPageURL = new Label(pnlPunchOut, SWT.RIGHT);
        lblStartPageURL.setText("Start_Page_URL");
        lblStartPageURL.setLayoutData(gridData1);
        lblStartPageURL.setData("name", "lblStartPageURL");
        txtStartPageURL = new Text(pnlPunchOut, SWT.BORDER);
        txtStartPageURL.setLayoutData(gridData2);
        txtStartPageURL.setData("name", "txtStartPageURL");
        txtStartPageURL.setText("12345678B");
        txtStartPageURL.setTextLimit(100);

//      XSLT Filename
        Label lblXSLTFilename = new Label(pnlPunchOut, SWT.RIGHT);
        lblXSLTFilename.setText("XSLT_Filename");
        lblXSLTFilename.setLayoutData(gridData1);
        lblXSLTFilename.setData("name", "lblXSLTFilename");
        txtXSLTFilename = new Text(pnlPunchOut, SWT.BORDER);
        txtXSLTFilename.setLayoutData(gridData2);
        txtXSLTFilename.setData("name", "txtXSLTFilename");
        txtXSLTFilename.setText("12345678B");
        txtXSLTFilename.setTextLimit(60);
        
        Label lblPathOfUserId = new Label(pnlPunchOut, SWT.RIGHT);
        lblPathOfUserId.setText("Path_of_user_id");
        lblPathOfUserId.setLayoutData(gridData1);
        lblPathOfUserId.setData("name", "lblPathOfUserId");
        txtPathOfUserId = new Text(pnlPunchOut, SWT.BORDER);
        txtPathOfUserId.setLayoutData(gridData2);
        txtPathOfUserId.setData("name", "txtPathOfUserId");

//      Show My Items
        Label lblShowMyItems = new Label(pnlPunchOut, SWT.RIGHT);
        lblShowMyItems.setText("Show_My_Items");
        lblShowMyItems.setLayoutData(gridData1);
        lblShowMyItems.setData("name", "lblShowMyItems");
        chkShowMyItems = new Button(pnlPunchOut, SWT.CHECK);
		chkShowMyItems.setLayoutData(new GridData());
		chkShowMyItems.setData("name", "chkShowMyItems");
		
//		UNSPSC Code Required
        Label lblUNSPSCCodeReg = new Label(pnlPunchOut, SWT.RIGHT);
        lblUNSPSCCodeReg.setText("UNSPSC_Code_Required");
        lblUNSPSCCodeReg.setLayoutData(gridData1);
        lblUNSPSCCodeReg.setData("name", "lblUNSPSCCodeReg");
        chkUNSPSCCodeReg = new Button(pnlPunchOut, SWT.CHECK);
		chkUNSPSCCodeReg.setLayoutData(new GridData());
		chkUNSPSCCodeReg.setData("name", "chkUNSPSCCodeReg");

//		XSLT Filename
        Label lblReplaceChars = new Label(pnlPunchOut, SWT.RIGHT);
        lblReplaceChars.setText("Replace_Chars");
        lblReplaceChars.setLayoutData(gridData1);
        lblReplaceChars.setData("name", "lblReplaceChars");
        txtReplaceChars = new Text(pnlPunchOut, SWT.BORDER);
        txtReplaceChars.setLayoutData(gridData2);
        txtReplaceChars.setData("name", "txtReplaceChars");
        txtReplaceChars.setText("12345678B");
        txtReplaceChars.setTextLimit(20);
        
        GridData gridDataSpan2 = new GridData();
        gridDataSpan2.horizontalAlignment = SWT.BEGINNING;
        gridDataSpan2.grabExcessHorizontalSpace = true;
        gridDataSpan2.horizontalSpan = 2;
        gridDataSpan2.minimumWidth = 150;
        Label lblOCISAPMaint = new Label(pnlPunchOut, SWT.BOLD);
        lblOCISAPMaint.setText("OCI_SAP_Maintenance");
        lblOCISAPMaint.setLayoutData(gridDataSpan2);
        lblOCISAPMaint.setData("name", "lblOCISAPMaint");

        Label lblUseOCISAPParam = new Label(pnlPunchOut, SWT.RIGHT);
        lblUseOCISAPParam.setText("Use_OCI_r_SAP_Param");
        lblUseOCISAPParam.setLayoutData(gridData1);
        lblUseOCISAPParam.setData("name", "lblUseOCISAPParam");
        chkUseOCISAPParam = new Button(pnlPunchOut, SWT.CHECK);
		chkUseOCISAPParam.setLayoutData(new GridData());
		chkUseOCISAPParam.setData("name", "chkUseOCISAPParam");
		
        Label lblUserNameParam = new Label(pnlPunchOut, SWT.RIGHT);
        lblUserNameParam.setText("Username_Param");
        lblUserNameParam.setLayoutData(gridData1);
        lblUserNameParam.setData("name", "lblUserNameParam");
        txtUserNameParam = new Text(pnlPunchOut, SWT.BORDER);
        txtUserNameParam.setLayoutData(new GridData());
        txtUserNameParam.setData("name", "txtUserNameParam");
        txtUserNameParam.setTextLimit(30);
        
        Label lblUserEmailTemplate = new Label(pnlPunchOut, SWT.RIGHT);
        lblUserEmailTemplate.setText("User_email_template");
        lblUserEmailTemplate.setLayoutData(gridData1);
        lblUserEmailTemplate.setData("name", "lblUserEmailTemplate");
        txtUserEmailTemplate = new Text(pnlPunchOut, SWT.BORDER);
        txtUserEmailTemplate.setLayoutData(new GridData());
        txtUserEmailTemplate.setData("name", "txtUserEmailTemplate");
        txtUserEmailTemplate.setTextLimit(30);

        Label lblUserpwdParam = new Label(pnlPunchOut, SWT.RIGHT);
        lblUserpwdParam.setText("Userpwd_Param");
        lblUserpwdParam.setLayoutData(gridData1);
        lblUserpwdParam.setData("name", "lblUserpwdParam");
        txtUserpwdParam = new Text(pnlPunchOut, SWT.BORDER);
        txtUserpwdParam.setLayoutData(new GridData());
        txtUserpwdParam.setData("name", "txtUserpwdParam");
        txtUserpwdParam.setTextLimit(30);
        
		Label lblComments = new Label(pnlPunchOut, SWT.BOLD);
        lblComments.setText("Comments:");
        lblComments.setLayoutData(gridDataSpan2);
        lblComments.setData("name", "lblComments");
        
        GridData gridDataSpan2b = new GridData();
		gridDataSpan2b.horizontalAlignment = SWT.FILL;
		gridDataSpan2b.verticalAlignment = SWT.FILL;
		gridDataSpan2b.grabExcessHorizontalSpace = true;
		gridDataSpan2b.grabExcessVerticalSpace = true;
		gridDataSpan2b.heightHint = 75;
		gridDataSpan2b.horizontalSpan = 2;
		
		txtComments = new Text(pnlPunchOut, 2626);
		txtComments.setLayoutData(gridDataSpan2b);
		txtComments.setData("yrc:customType", "Text");
		txtComments.setData("name", "txtComments");
		txtComments.setTextLimit(250);
	}

	private void createB2BMaintComposite() {
		GridLayout gridLayout5 = new GridLayout(2,false);
		gridLayout5.verticalSpacing = 2;
		
		GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.BEGINNING;
        gridData.verticalAlignment = SWT.TOP;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalIndent = 3;
        gridData.widthHint = 500;
        
		Composite pnlB2BMaint = new Composite(pnlBody, SWT.NONE);
//		pnlB2BMaint.setData("yrc:customType", "TaskComposite");
		pnlB2BMaint.setData("name", "pnlPunchOut");
		pnlB2BMaint.setLayout(gridLayout5);
		pnlB2BMaint.setLayoutData(gridData);
		
        XPXUtils.addGradientPanelHeader(pnlB2BMaint, "B2B Maintenance",true);
		
     // Controls 
		GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = SWT.BEGINNING;
        gridData1.widthHint = 150;
        
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = SWT.BEGINNING;
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.widthHint = 150;

        // Buyer ID
        Label lblBuyerID = new Label(pnlB2BMaint, SWT.LEFT);
        lblBuyerID.setText("Buyer_ID");
        lblBuyerID.setLayoutData(gridData1);
        lblBuyerID.setData("name", "lblBuyerID");
        txtBuyerID = new Text(pnlB2BMaint, SWT.BORDER);
        txtBuyerID.setLayoutData(gridData2);
        txtBuyerID.setData("name", "txtBuyerID");
        txtBuyerID.setText("12345678");

//        UOM Type
        Label lblUOMType = new Label(pnlB2BMaint, SWT.LEFT);
        lblUOMType.setText("UOM_Type");
        lblUOMType.setLayoutData(gridData1);
        lblUOMType.setData("name", "lblUOMType");
        GridLayout gridLayoutPnlUOM = new GridLayout();
		gridLayoutPnlUOM.numColumns = 3;
		gridLayoutPnlUOM.makeColumnsEqualWidth = true;
		GridData gridDataPnlUOM = new GridData();
		gridDataPnlUOM.horizontalAlignment = SWT.BEGINNING;
		gridDataPnlUOM.grabExcessHorizontalSpace = true;
		gridDataPnlUOM.verticalAlignment = 2;
		gridDataPnlUOM.widthHint = 500;

		Composite pnlUOMRadioBttns = new Composite(pnlB2BMaint, 0);
		pnlUOMRadioBttns.setLayout(gridLayoutPnlUOM);
		pnlUOMRadioBttns.setLayoutData(gridDataPnlUOM);
		pnlUOMRadioBttns.setData("name", "pnlUOMRadioBttns");

		radIsEdiUom = new Button(pnlUOMRadioBttns, SWT.RADIO);
		radIsEdiUom.setText("EDI");
		radIsEdiUom.setData("name", "radIsEdiUom");
		radIsEdiUom.setData("yrc:customType", "Label");

		radIsXpedxUom = new Button(pnlUOMRadioBttns, SWT.RADIO);
		radIsXpedxUom.setText("xpedx");
		radIsXpedxUom.setData("name", "radIsXpedxUom");
		radIsXpedxUom.setData("yrc:customType", "Label");
		
		// Support Email Address
        Label lblSupportEmailAddr = new Label(pnlB2BMaint, SWT.LEFT);
        lblSupportEmailAddr.setText("Support_Email_Address");
        lblSupportEmailAddr.setLayoutData(gridData1);
        lblSupportEmailAddr.setData("name", "lblSupportEmailAddr");
        txtSupportEmailAddr = new Text(pnlB2BMaint, SWT.BORDER);
        txtSupportEmailAddr.setLayoutData(gridData2);
        txtSupportEmailAddr.setData("name", "txtSupportEmailAddr");
        txtSupportEmailAddr.setText("12345678");
        txtSupportEmailAddr.setToolTipText("Comma Seperated Value; To notify on missing/invalid e Trading IDs.");
        txtSupportEmailAddr.setTextLimit(200);
        
        GridData gridDataSpan2 = new GridData();
        gridDataSpan2.horizontalAlignment = SWT.BEGINNING;
        gridDataSpan2.grabExcessHorizontalSpace = true;
        gridDataSpan2.horizontalSpan = 2;
//        gridDataSpan2.minimumWidth = 150;
        
        StyledText stxtSupportEmailAddressStaticComments = new StyledText(pnlB2BMaint, 72);
        stxtSupportEmailAddressStaticComments.setText("Comma Seperated Value; To notify on missing/invalid e Trading IDs.");
        stxtSupportEmailAddressStaticComments.setLayoutData(gridDataSpan2);
        stxtSupportEmailAddressStaticComments.setData("name", "stxtSupportEmailAddressStaticComments");
        
        GridLayout gridLayoutChld = new GridLayout(4,false);
		gridLayoutChld.verticalSpacing = 2;
		gridLayoutChld.numColumns = 4;
		
		GridData gridDataChld = new GridData();
        gridDataChld.horizontalAlignment = SWT.FILL;
        gridDataChld.verticalAlignment = SWT.TOP;
        gridDataChld.grabExcessHorizontalSpace = true;
        gridDataChld.grabExcessVerticalSpace = true;
        gridDataChld.horizontalIndent = 3;
        gridDataChld.horizontalSpan = 2;
//        gridDataChld.widthHint = 500;
        
		Composite pnlB2BMaintChild = new Composite(pnlB2BMaint, SWT.NONE);
//		pnlB2BMaintChild.setData("yrc:customType", "TaskComposite");
		pnlB2BMaintChild.setData("name", "pnlB2BMaintChild");
		pnlB2BMaintChild.setLayout(gridLayoutChld);
		pnlB2BMaintChild.setLayoutData(gridDataChld);
//		
		GridData gridDataLbl = new GridData();
		gridDataLbl.horizontalAlignment = SWT.END;
		gridDataLbl.verticalAlignment = SWT.TOP;
		gridDataLbl.widthHint = 140;
		
		GridData gridDataChk = new GridData();
		gridDataChk.horizontalAlignment = SWT.BEGINNING;
		gridDataChk.verticalAlignment = SWT.TOP;
		gridDataChk.widthHint = 40;
		
		GridData gridDataLbl1 = new GridData();
		gridDataLbl1.horizontalAlignment = SWT.END;
		gridDataLbl1.verticalAlignment = SWT.TOP;
		gridDataLbl1.widthHint = 80;
		
		GridData gridDataTA = new GridData();
        gridDataTA.horizontalAlignment = SWT.FILL;
        gridDataTA.grabExcessHorizontalSpace = true;
        gridDataTA.grabExcessVerticalSpace = false;
        gridDataTA.heightHint = 80;
//        gridDataTA.widthHint = 100;
        gridDataTA.verticalAlignment = SWT.TOP;

//		Purchase Order
		Label lblPurchaseOrd = new Label(pnlB2BMaintChild, SWT.RIGHT);
		lblPurchaseOrd.setText("Purchase Order");
		lblPurchaseOrd.setLayoutData(gridDataLbl);
		lblPurchaseOrd.setData("name", "lblPurchaseOrd");
		chkPurchaseOrd = new Button(pnlB2BMaintChild, SWT.CHECK|SWT.LEFT);
		chkPurchaseOrd.setText("");
		chkPurchaseOrd.setVisible(true);
		chkPurchaseOrd.setData("yrc:customType", "Label");
		chkPurchaseOrd.setLayoutData(gridDataChk);
		chkPurchaseOrd.setData("name", "chkPurchaseOrd");
		
		Label lblPOComments = new Label(pnlB2BMaintChild, SWT.RIGHT);
        lblPOComments.setText("Comments");
        lblPOComments.setLayoutData(gridDataLbl1);
        lblPOComments.setData("name", "lblPOComments");
        txtPOComments = new Text(pnlB2BMaintChild, 2626);
        txtPOComments.setLayoutData(gridDataTA);
        txtPOComments.setData("yrc:customType", "Text");
        txtPOComments.setData("name", "txtPOComments");
        txtPOComments.setTextLimit(200);
        
//		Purchase Order Confirmation
		Label lblPOConfirmation = new Label(pnlB2BMaintChild, SWT.RIGHT);
		lblPOConfirmation.setText("PO Confirmation");
		lblPOConfirmation.setLayoutData(gridDataLbl);
		lblPOConfirmation.setData("name", "lblPOConfirmation");
		chkPOConfirmation = new Button(pnlB2BMaintChild, SWT.CHECK|SWT.LEFT);
		chkPOConfirmation.setText("");
		chkPOConfirmation.setVisible(true);
		chkPOConfirmation.setData("yrc:customType", "Label");
		chkPOConfirmation.setLayoutData(gridDataChk);
		chkPOConfirmation.setData("name", "chkPOConfirmation");
		
		Label lblPOConfirmationComments = new Label(pnlB2BMaintChild, SWT.RIGHT);
        lblPOConfirmationComments.setText("Comments");
        lblPOConfirmationComments.setLayoutData(gridDataLbl1);
        lblPOConfirmationComments.setData("name", "lblPOConfirmationComments");
        txtPOConfirmationComments = new Text(pnlB2BMaintChild, 2626);
        txtPOConfirmationComments.setLayoutData(gridDataTA);
        txtPOConfirmationComments.setData("yrc:customType", "Text");
        txtPOConfirmationComments.setData("name", "txtPOConfirmationComments");
        txtPOConfirmationComments.setTextLimit(200);
        
//		Invoice
		Label lblInvoice = new Label(pnlB2BMaintChild, SWT.RIGHT);
		lblInvoice.setText("Invoice");
		lblInvoice.setLayoutData(gridDataLbl);
		lblInvoice.setData("name", "lblInvoice");
		chkInvoice = new Button(pnlB2BMaintChild, SWT.CHECK|SWT.LEFT);
		chkInvoice.setText("");
		chkInvoice.setVisible(true);
		chkInvoice.setData("yrc:customType", "Label");
		chkInvoice.setLayoutData(gridDataChk);
		chkInvoice.setData("name", "chkInvoice");
		
		Label lblInvoiceComments = new Label(pnlB2BMaintChild, SWT.RIGHT);
        lblInvoiceComments.setText("Comments");
        lblInvoiceComments.setLayoutData(gridDataLbl1);
        lblInvoiceComments.setData("name", "lblInvoiceComments");
        txtInvoiceComments = new Text(pnlB2BMaintChild, 2626);
        txtInvoiceComments.setLayoutData(gridDataTA);
        txtInvoiceComments.setData("yrc:customType", "Text");
        txtInvoiceComments.setData("name", "txtInvoiceComments");
        txtInvoiceComments.setTextLimit(200);

//		Price and Availability
		Label lblPnAFlag = new Label(pnlB2BMaintChild, SWT.RIGHT);
		lblPnAFlag.setText("Price_and_Availability");
		lblPnAFlag.setLayoutData(gridDataLbl);
		lblPnAFlag.setData("name", "lblPnAFlag");
		chkPnAFlag = new Button(pnlB2BMaintChild, SWT.CHECK|SWT.LEFT);
		chkPnAFlag.setText("");
		chkPnAFlag.setVisible(true);
		chkPnAFlag.setData("yrc:customType", "Label");
		chkPnAFlag.setLayoutData(gridDataChk);
		chkPnAFlag.setData("name", "chkPnAFlag");
		
		Label lblPnAFlagComments = new Label(pnlB2BMaintChild, SWT.RIGHT);
        lblPnAFlagComments.setText("Comments");
        lblPnAFlagComments.setLayoutData(gridDataLbl1);
        lblPnAFlagComments.setData("name", "lblPnAFlagComments");
        txtPnAFlagComments = new Text(pnlB2BMaintChild, 2626);
        txtPnAFlagComments.setLayoutData(gridDataTA);
        txtPnAFlagComments.setData("yrc:customType", "Text");
        txtPnAFlagComments.setData("name", "txtPnAFlagComments");
        txtPnAFlagComments.setTextLimit(200);

	}

	private void createComposite() {

		// Header Panel
		GridLayout gridLayout5 = new GridLayout(3,false);
		gridLayout5.verticalSpacing = 2;
		
		GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.BEGINNING;
        gridData.verticalAlignment = SWT.TOP;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalIndent = 3;
        gridData.widthHint = 500;
        
		Composite pnlHeader = new Composite(pnlBody, SWT.NONE);
//		pnlHeader.setData("yrc:customType", "TaskComposite");
		pnlHeader.setData("name", "pnlHeader");
		pnlHeader.setLayout(gridLayout5);
		pnlHeader.setLayoutData(gridData);
		XPXUtils.paintPanel(pnlHeader);

		// Controls 
		GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = SWT.BEGINNING;
        gridData1.widthHint = 110;
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = SWT.BEGINNING;
        gridData2.widthHint = 180;
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = SWT.BEGINNING;
        gridData3.widthHint = 180;

        // Master Customer
        Label lblMasterCustomer = new Label(pnlHeader, SWT.LEFT);
        lblMasterCustomer.setText("Master_Customer");
        lblMasterCustomer.setLayoutData(gridData1);
        lblMasterCustomer.setData("name", "lblMasterCustomer");
        
        stxtMasterCustomerNo = new StyledText(pnlHeader, 72);
        stxtMasterCustomerNo.setLayoutData(gridData2);
        stxtMasterCustomerNo.setData("name", "stxtMasterCustomerNo");
        stxtMasterCustomerNo.setWordWrap(true);
        
        stxtMasterCustomerName = new StyledText(pnlHeader, 72);
        stxtMasterCustomerName.setLayoutData(gridData3);
        stxtMasterCustomerName.setData("name", "stxtMasterCustomerName");
        stxtMasterCustomerName.setWordWrap(true);
        	
//        Removed - JIRA 1109 - stxtCustomerNo,stxtCustomerName
//      Customer
//        Label lblCustomer = new Label(pnlHeader, SWT.NONE);
//        lblCustomer.setText("Customer");
//        lblCustomer.setLayoutData(gridData1);
//        lblCustomer.setData("name", "lblCustomer");
//        stxtCustomerNo = new StyledText(pnlHeader, 72);
//        stxtCustomerNo.setLayoutData(gridData2);
//        stxtCustomerNo.setData("name", "stxtCustomerNo");
//        stxtCustomerNo.setWordWrap(true);
//        
//        stxtCustomerName = new StyledText(pnlHeader, 72);
//        stxtCustomerName.setLayoutData(gridData3);
//        stxtCustomerName.setData("name", "stxtCustomerName");
//        stxtCustomerName.setWordWrap(true);
        
	}

	/**
	 * 
	 */
	private void createScrollCmpstForPnlBody() {
		// Scroll Panel Start
		GridData gridDataScroll = new GridData();
		gridDataScroll.horizontalAlignment = SWT.FILL;
		gridDataScroll.grabExcessHorizontalSpace = true;
		gridDataScroll.grabExcessVerticalSpace = true;
		gridDataScroll.verticalAlignment = SWT.FILL;
		// gridData1.heightHint = 450;

		bodyScrolledPanel = new ScrolledComposite(getRootPanel(), SWT.V_SCROLL);
		bodyScrolledPanel.setLayoutData(gridDataScroll);
		bodyScrolledPanel.setExpandHorizontal(true);
		bodyScrolledPanel.setExpandVertical(true);
		bodyScrolledPanel.setAlwaysShowScrollBars(true);
		bodyScrolledPanel.addListener(SWT.CURSOR_SIZEALL,
				new YRCScrolledCompositeListener(bodyScrolledPanel));

		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 3;
		gridLayout.verticalSpacing = 3;
		gridLayout.marginWidth = 1;
		gridLayout.marginHeight = 1;
		gridLayout.numColumns = 1;

		GridData gridData0 = new GridData();
		gridData0.horizontalAlignment = SWT.FILL;
		gridData0.grabExcessVerticalSpace = true;
		gridData0.grabExcessHorizontalSpace = true;
		gridData0.verticalAlignment = SWT.FILL;
//		gridData0.heightHint = 500;
		pnlBody = new Composite(bodyScrolledPanel, 0);
		pnlBody.setLayoutData(gridData0);
		pnlBody.setLayout(gridLayout);
		pnlBody.setData("name", "pnlBody");
		// XPXUtils.paintPanel(pnlPrimaryInformation);

		GridLayout gridLayout1 = new GridLayout();
		bodyScrolledPanel.setLayout(gridLayout1);
		bodyScrolledPanel.setContent(pnlBody);
//		bodyScrolledPanel.setData("yrc:customType", "TaskComposite");
		bodyScrolledPanel.setData("name", "bodyScrolledPanel");
		// XPXUtils.paintPanel(scrolledPnlforInfo);

		// Scroll Panel End
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
		// TODO Auto-generated method stub
		return pnlRoot;
	}
	
	public void showRootPanel(boolean show) {
		pnlRoot.setVisible(show);
	}
}

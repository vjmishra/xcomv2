package com.xpedx.sterling.rcp.pca.userprofile.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCButtonBindingData;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCScrolledCompositeListener;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;
import com.yantra.yfc.rcp.YRCWizardBehavior;

public class UserProfileInfoDetails extends Composite implements IYRCComposite {
	private Composite pnlRoot = null;

	private UserProfileInfoDetailsBehavior myBehavior;

	private YRCWizardBehavior wizBehavior;

	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.userprofile.screen.UserProfileInfoDetails"; // @jve:decl-index=0:

	private Object inputObject;

	private ScrolledComposite bodyScrolledPanel = null;
	private Label lblPrimApprover;
	private Label lblAlterApprover;
	private Combo comboPrimApprover;
	private Combo comboAlterApprover;
	private Label lblCurrencyType;
	private Label lblSpendingLimit;
	private Combo comboCurrencyType;
	private Text txtSpendingLimit;
	private Button btnUpdate;
	private Label lblUserID;
	private Label lblUserType;
	private Label lblMaxOrderAmount;
	private Label lblMinOrderAmount;
	private Label lblReceiveOrderConfirmEmails;
	private Label lblPrefferedCatalog;
	private Label lblDefaultShipTo;
	private Label lblPOList;
	private Label lblInvoiceEmailId;
	private Label lblAcceptTAndC;
	private Label lblFaxNumber;
	private Label lblLastLoginDate;
	private Label lblPreferredCatalogView;
	 Text txtMaxOrderAmount;
	 Text txtMinOrderAmount;
	private Text txtCustomerID;
	private Text txtPOList;
	
	private Text  txtDefaultShipToAddress;
	private Combo comboPrefferedCatalog;
	public Table itemPOList;
	public Table itemAdditionalEmails;
	private Text txtUserID;
	private Button chkViewInvoices;
	private Button chkEstimator;
	private Button chkStockCheckWS;
	private Composite pnlPOListHolder;
	private TableColumn tblPOListId;
	private Composite pnlPOListButtons;
	private Button btnAdd;
	private Button btnRmv;
	private Button defaultShipto;
	private Text txtInvoiceEmailId;
	private Button chkViewPrices;	
	private Button chkViewReports;	
	private Button chkProcurementUser;
	private Button chkReceiveOrderConfirmationEmail;
	private Button chkReceiveOrderCancellationEmail;
	private Button chkReceiveOrderShipmentEmail;
//	private Button chkReceiveOrderUpdateEmail; //Removed for the BugFix id-1004
	private Button chkeceiveBackOrderEmail;
	private Button chkAcceptTAndC;
	private Text txtFaxNumber;
	private Text txtLastLoginDate;
	private Text txtTCAccepted;
	private Combo comboB2BCatalogView;
	private Button fullView;
	private Button condensedView;
	private Button miniView;
	private Button paperGridView;
	private Button btnAssign;
	private Button btnRemove;
	private Button radUserActive;
	private Button radUserSuspend;
	private Label lblUserStatus;
	private Composite pnlBody ;
	private Button chkBuyer;
	private Button chkApprover;
	private Button chkAdmin;
	private Button btnPassword;
	private Label lblPassword;
	private Text txtFirstName;
	private Label lblFirstName;
	private Text txtLastName;
	private Label lblLastName;
	private Text txtEmailAddress;
	private Label lblEmailAddress;
	private Text txtPhone;
	private Label lblPhone;
	private Text txtAddressLine1;
	private Label lblAddressLine1;
	private Text txtAddressLine2;
	private Label lblAddressLine2;
	private Text txtAddressLine3;
	private Label lblAddressLine3;
	private Text txtCity;
	private Label lblCity;
	private Text txtState;
	private Label lblState;
	private Text txtZipCode;
	private Label lblZipCode;
	private Text txtCountry;
	private Label lblCountry;
	private Composite pnlSiteSettings = null;
	private Button radItem1;
	private Button radItem2;
	private Button radItem4;
	private Button radMatrix;
	private Label lblOrderConfirmationAddressList;
	private Composite pnlOrderConfirmListButtons;
	private Composite pnlOrderConfirmList;
	private Composite pnlOrderConfirmListHolder;
	public Table orderConfirmList;
	private TableColumn tblOrderConfirmListId;
	private Composite pnlLocations = null;
	private Text txtOrderConfirmationList;
	private Composite pnlInvoiceListHolder;
	private TableColumn tblInvoiceListId;
	private Composite pnlInvoiceListButtons;
	private Composite pnlInvoiceList;
	public Table invoiceList;
	private Text txtDummy1;
	private Text txtDummy2;
	private Text txtDummy3;
	private Text txtZip4;
	private Composite pnlPOLIst;
	private Button btnUsrRolesHelpInfo;
	
//	private Composite pnlRadioButtons;
//	private Button radInternal;
//	private Button radExternal;
//	private Composite pnlAdditionalEmails;
//	private Composite pnlAdditionalEmailsHolder;
//	private TableColumn tblAdditionalEmailsId;
//	private Composite pnlAdditionalEmailsButtons;
//	private Text txtPrefferedCatalog;
//	private Text txtAdditionalEmailAddress;
//	private Combo comboReceiveOrderConfirmEmails;

	public UserProfileInfoDetails(Composite parent, int style,
			Object inputObject, Element customerContactEle) {
		super(parent, style);
		this.inputObject = inputObject;
		
		
		initialize();
		setBindingForComponents();
		myBehavior = new UserProfileInfoDetailsBehavior(this, inputObject,
				customerContactEle);
		checkUserPermissions();
		adjustScrollPnl(bodyScrolledPanel, pnlBody,
				getRootPanel(), true, true);

	}
	/**
	 * This method validates the user authorization
	 * to update the screen controls
	 */	
	private void checkUserPermissions() {
		if (!YRCPlatformUI
				.hasPermission(XPXConstants.RES_ID_MANAGE_CUSTOMER_CONTACT)) {
			setControlsEnabled(getAllEditableControls(), false);
		}
//		 else {
//			if (!YRCPlatformUI
//					.hasPermission(XPXConstants.RES_ID_MANAGE_CUSTOMER_OR_USER_INTEG_ATTR)) {
//
//				setControlsEnabled(getIntegrationDataControls(), false);
//			}
//		}
		if(!YRCPlatformUI.hasPermission(XPXConstants.RES_ID_RESET_PASSWORD)){
			setControlsEnabled(new Control[]{btnPassword},false);
			
		}
		
	}
//	private Control[] getIntegrationDataControls() {
//		System.out.println("inside getIntegrationDataControls method");
//		return new Control[] {txtEmployeeId};
//	}

	private Control[] getAllEditableControls() {
		//removed txtAdditionalEmailAddress, comboReceiveOrderConfirmEmails, itemAdditionalEmails,
		// should we have this editable: txtEmployeeId, 
		return new Control[] { btnUpdate,txtLastName, txtMaxOrderAmount, txtMinOrderAmount,
				txtPOList, defaultShipto,
				comboPrefferedCatalog, itemPOList,
				chkViewInvoices,
				chkEstimator, chkStockCheckWS, btnAdd, btnRmv,
				txtInvoiceEmailId, chkViewPrices, chkViewReports,
				chkReceiveOrderConfirmationEmail,
				chkReceiveOrderCancellationEmail, chkReceiveOrderShipmentEmail,
				chkeceiveBackOrderEmail, chkAcceptTAndC, txtFaxNumber,
				txtLastLoginDate, comboB2BCatalogView, fullView, condensedView,
				miniView, paperGridView, btnAssign, btnRemove };
	}	
	
	private void setControlsEnabled(Control[] controls, boolean enabled) {
		for (Control control : controls) {
			if (null != control)
				control.setEnabled(enabled);
		}

	}
	private void initialize() {
		GridLayout thisLayout = new GridLayout(1,false);
		thisLayout.marginHeight = 0;
		thisLayout.marginWidth = 0;
		setLayout(thisLayout);
		createRootPanel();
		setVisible(true);
		setSize(new org.eclipse.swt.graphics.Point(1000, 2000));
	}

	private void createRootPanel() {
		pnlRoot = new Composite(this, SWT.NONE);
        pnlRoot.setBackgroundMode(0);
        pnlRoot.setData("name", "pnlRoot");
        pnlRoot.setData("yrc:customType", "TaskComposite");
        GridLayout pnlRootlayout = new GridLayout(1, false);
        pnlRoot.setLayout(pnlRootlayout);
		
		GridData pnlRootLayoutData = new GridData();
		pnlRootLayoutData.horizontalAlignment = SWT.FILL;
		pnlRootLayoutData.verticalAlignment = SWT.FILL;
		pnlRootLayoutData.grabExcessHorizontalSpace = true;
		pnlRootLayoutData.grabExcessVerticalSpace = true;
		pnlRoot.setLayoutData(pnlRootLayoutData);
		createScrollCmpstForPnlBody();
		createMiscPnl();
		createGeneralInfoHeaderPnl();
		createGeneralInfoPnl();
		createLocationsHeaderPnl();
		createLocationsPnl();
		createSiteSettingsHeaderPnl();
		createSiteSettingsPnl();
		
//		adjustScrollPnl(bodyScrolledPanel, pnlBody,
//				getRootPanel(), true, true);
	}
	
	private void createMiscPnl() {
		GridData gridData16 = new GridData();
		gridData16.horizontalAlignment = 3;
		gridData16.grabExcessHorizontalSpace = true;
		gridData16.horizontalSpan = 1;
		gridData16.horizontalIndent = 0;
		gridData16.verticalAlignment = 2;

		btnUpdate = new Button(pnlRoot, 0);
		btnUpdate.setText("Update_User_Profile");
		btnUpdate.setLayoutData(gridData16);
		btnUpdate.setData("name", "btnUpdate");
	}

	
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
		bodyScrolledPanel.setMinHeight(100);
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
		
		pnlBody = new Composite(bodyScrolledPanel, 0);
		pnlBody.setLayoutData(gridData0);
		pnlBody.setLayout(gridLayout);
		pnlBody.setData("name", "pnlBody");

		GridLayout gridLayout1 = new GridLayout();
		bodyScrolledPanel.setLayout(gridLayout1);
		bodyScrolledPanel.setContent(pnlBody);
//		bodyScrolledPanel.setData("yrc:customType", "TaskComposite");
		bodyScrolledPanel.setData("name", "bodyScrolledPanel");
		
		// Scroll Panel End
	}
	


	

	
	
	//added by madhura
	private void createGeneralInfoHeaderPnl() {
		Composite pnlGeneralInfoHeader = new Composite(pnlBody,SWT.NONE);
		pnlGeneralInfoHeader.setBackgroundMode(SWT.INHERIT_NONE);
		pnlGeneralInfoHeader.setData(YRCConstants.YRC_CONTROL_NAME, "pnlGeneralInfoHeader");
		GridLayout pnlGeneralInfoHeaderLayout = new GridLayout();
		pnlGeneralInfoHeaderLayout.horizontalSpacing = 1;
		pnlGeneralInfoHeaderLayout.verticalSpacing = 1;
		pnlGeneralInfoHeaderLayout.marginHeight = 2;
		pnlGeneralInfoHeaderLayout.marginWidth = 2;
		pnlGeneralInfoHeaderLayout.numColumns = 3;
		pnlGeneralInfoHeader.setLayout(pnlGeneralInfoHeaderLayout);
		
		GridData pnlGeneralInfoHeaderLayoutData = new GridData();
		pnlGeneralInfoHeaderLayoutData.horizontalAlignment = 4;
		pnlGeneralInfoHeaderLayoutData.grabExcessHorizontalSpace = true;
		//pnlGeneralInfoHeaderLayoutData.grabExcessVerticalSpace = true;
		pnlGeneralInfoHeader.setLayoutData(pnlGeneralInfoHeaderLayoutData);
		pnlGeneralInfoHeader.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		
		
		Label lblGeneralInfo = new Label(pnlGeneralInfoHeader, SWT.LEFT);
		GridData GenralInfoData = new GridData();
		GenralInfoData.grabExcessHorizontalSpace = true;
		lblGeneralInfo.setLayoutData(GenralInfoData);
		lblGeneralInfo.setText("General Info");
		lblGeneralInfo.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		
	}
	
	private void createGeneralInfoPnl() {
		
		Composite pnlGeneralInfo = new Composite(pnlBody,SWT.NONE);
		pnlGeneralInfo.setBackgroundMode(SWT.INHERIT_NONE);
		pnlGeneralInfo.setData(YRCConstants.YRC_CONTROL_NAME, "pnlGeneralInfo");
		
		GridLayout pnlGeneralInfoLayout = new GridLayout(3,true);
		pnlGeneralInfoLayout.horizontalSpacing = 30;
		pnlGeneralInfoLayout.verticalSpacing = 1;
		pnlGeneralInfoLayout.marginHeight = 2;
		pnlGeneralInfoLayout.marginWidth = 2;
		//pnlGeneralInfoLayout.numColumns = 3;
		pnlGeneralInfo.setLayout(pnlGeneralInfoLayout);
		
		GridData pnlGeneralInfoLayoutData = new GridData();
		pnlGeneralInfoLayoutData.horizontalAlignment = 4;
		pnlGeneralInfoLayoutData.grabExcessHorizontalSpace = true;
//		pnlGeneralInfoLayoutData.grabExcessVerticalSpace = true;
		pnlGeneralInfo.setLayoutData(pnlGeneralInfoLayoutData);
		
		GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = 4;
        gridData2.horizontalIndent = 100;
        gridData2.widthHint = 150;
        gridData2.verticalIndent = 8;
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = SWT.BEGINNING;
        gridData3.widthHint = 200;
        gridData3.horizontalSpan = 2;
        
		
        //controls
        lblUserID = new Label(pnlGeneralInfo, SWT.LEFT);
		lblUserID.setText("User_ID");
		lblUserID.setLayoutData(gridData2);
		lblUserID.setData("name", "lblUserID");
		txtUserID = new Text(pnlGeneralInfo, SWT.READ_ONLY);
		txtUserID.setText("");
		txtUserID.setLayoutData(gridData3);
		txtUserID.setTextLimit(60);
		txtUserID.setData("name", "txtUserID");
		
		lblUserStatus = new Label(pnlGeneralInfo, SWT.LEFT);
		lblUserStatus.setText("User_Status");
		lblUserStatus.setLayoutData(gridData2);
		lblUserStatus.setData("name", "lblUserStatus");
		Composite pnlUserRadButtons = new Composite(pnlGeneralInfo,SWT.NONE);
		pnlUserRadButtons.setLayout(pnlGeneralInfoLayout);
		pnlUserRadButtons.setLayoutData(gridData3);
		
		radUserActive = new Button(pnlUserRadButtons, SWT.RADIO);
		radUserActive.setText("Active");
		radUserActive.setData("name", "radUserActive");
		radUserActive.setData("yrc:customType", "Label");
		radUserSuspend = new Button(pnlUserRadButtons, SWT.RADIO);
		radUserSuspend.setText("Suspend");
		radUserSuspend.setData("name", "radUserSuspend");
		radUserSuspend.setData("yrc:customType", "Label");
		
		lblAcceptTAndC = new Label(pnlGeneralInfo, SWT.LEFT);
		lblAcceptTAndC.setText("TC_Accepted");
		lblAcceptTAndC.setLayoutData(gridData2);
		lblAcceptTAndC.setData("name", "lblAcceptTAndC");
		txtTCAccepted = new Text(pnlGeneralInfo, SWT.READ_ONLY);
		txtTCAccepted.setLayoutData(gridData3);
		txtTCAccepted.setData("name", "txtTCAccepted");
		
		lblLastLoginDate = new Label(pnlGeneralInfo, SWT.LEFT);
		lblLastLoginDate.setText("Last_Login_Date");
		lblLastLoginDate.setLayoutData(gridData2);
		lblLastLoginDate.setData("name", "lblLastLoginDate");
		txtLastLoginDate = new Text(pnlGeneralInfo, SWT.READ_ONLY);
		txtLastLoginDate.setLayoutData(gridData3);
		txtLastLoginDate.setData("name", "txtLastLoginDate");
		
		this.addUserTypeControls(pnlGeneralInfo, pnlGeneralInfoLayout, gridData2);
		
		lblPassword = new Label(pnlGeneralInfo, SWT.NONE);
		lblPassword.setText("Contact_Password");
		lblPassword.setLayoutData(gridData2);
		lblPassword.setData("name", "lblPassword");
		btnPassword = new Button(pnlGeneralInfo,SWT.NONE);
		btnPassword.setText("Reset_Password");
		btnPassword.setLayoutData(gridData3);
		btnPassword.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resetCustomPassword();
				
			}
		});
		
		lblFirstName = new Label(pnlGeneralInfo, SWT.LEFT);
		lblFirstName.setText("First_Name");
		lblFirstName.setLayoutData(gridData2);
		lblFirstName.setData("name", "lblFirstName");
		txtFirstName = new Text(pnlGeneralInfo, SWT.BORDER);
		txtFirstName.setText("");
		txtFirstName.setLayoutData(gridData3);
		txtFirstName.setTextLimit(64);
		txtFirstName.setData("name", "txtFirstName");
		
		lblLastName = new Label(pnlGeneralInfo, SWT.LEFT);
		lblLastName.setText("Last_Name");
		lblLastName.setLayoutData(gridData2);
		lblLastName.setData("name", "lblLastName");
		txtLastName = new Text(pnlGeneralInfo, SWT.BORDER);
		txtLastName.setText("");
		txtLastName.setLayoutData(gridData3);
		txtLastName.setTextLimit(64);
		txtLastName.setData("name", "txtLastName");
		
		lblEmailAddress = new Label(pnlGeneralInfo, SWT.LEFT);
		lblEmailAddress.setText("Email_Addresses");
		lblEmailAddress.setLayoutData(gridData2);
		lblEmailAddress.setData("name", "lblEmailAddress");
		txtEmailAddress = new Text(pnlGeneralInfo, SWT.BORDER);
		txtEmailAddress.setText("");
		txtEmailAddress.setLayoutData(gridData3);
		txtEmailAddress.setTextLimit(150);
		txtEmailAddress.setData("name", "txtEmailAddress");
		
		lblPhone = new Label(pnlGeneralInfo, SWT.LEFT);
		lblPhone.setText("Phone");
		lblPhone.setLayoutData(gridData2);
		lblPhone.setData("name", "lblPhone");
		txtPhone = new Text(pnlGeneralInfo, SWT.BORDER);
		txtPhone.setLayoutData(gridData3);
		txtPhone.setTextLimit(40);
		txtPhone.setData("name", "txtPhone");
		
		lblFaxNumber = new Label(pnlGeneralInfo, SWT.LEFT);
		lblFaxNumber.setText("Contact_Fax_Number");
		lblFaxNumber.setLayoutData(gridData2);
		lblFaxNumber.setData("name", "lblFaxNumber");
		txtFaxNumber = new Text(pnlGeneralInfo, SWT.BORDER);
		txtFaxNumber.setLayoutData(gridData3);
		txtFaxNumber.setTextLimit(40);
		txtFaxNumber.setData("name", "txtFaxNumber");
		
		this.addContactAddress(pnlGeneralInfo, gridData2, gridData3);
		
		// hidden fields to 
		txtDummy1 = new Text(pnlGeneralInfo, SWT.BORDER);
		txtDummy1.setLayoutData(gridData3);
		txtDummy1.setVisible(false);
		
		txtDummy2 = new Text(pnlGeneralInfo, SWT.BORDER);
		txtDummy2.setLayoutData(gridData3);
		txtDummy2.setVisible(false);
		
		txtDummy3 = new Text(pnlGeneralInfo, SWT.BORDER);
		txtDummy3.setLayoutData(gridData3);
		txtDummy3.setVisible(false);
		
	}
	/**
	 * @param pnlGeneralInfo
	 * @param gridData2
	 * @param gridData3
	 */
	private void addContactAddress(Composite pnlGeneralInfo, GridData gridData2, GridData gridData3) {
		lblAddressLine1 = new Label(pnlGeneralInfo, SWT.LEFT);
		lblAddressLine1.setText("Address_Line_1");
		lblAddressLine1.setLayoutData(gridData2);
		lblAddressLine1.setData("name", "lblAddressLine1");
		txtAddressLine1 = new Text(pnlGeneralInfo, SWT.BORDER);
		txtAddressLine1.setLayoutData(gridData3);
		txtAddressLine1.setTextLimit(70);
		txtAddressLine1.setData("name", "txtAddressLine1");
		
		lblAddressLine2 = new Label(pnlGeneralInfo, SWT.LEFT);
		lblAddressLine2.setText("Address_Line_2");
		lblAddressLine2.setLayoutData(gridData2);
		lblAddressLine2.setData("name", "lblAddressLine2");
		txtAddressLine2 = new Text(pnlGeneralInfo, SWT.BORDER);
		txtAddressLine2.setLayoutData(gridData3);
		txtAddressLine2.setTextLimit(70);
		txtAddressLine2.setData("name", "txtAddressLine2");
		
		lblAddressLine3 = new Label(pnlGeneralInfo, SWT.LEFT);
		lblAddressLine3.setText("Address_Line_3");
		lblAddressLine3.setLayoutData(gridData2);
		lblAddressLine3.setData("name", "lblAddressLine3");
		txtAddressLine3 = new Text(pnlGeneralInfo, SWT.BORDER);
		txtAddressLine3.setLayoutData(gridData3);
		txtAddressLine3.setTextLimit(70);
		txtAddressLine3.setData("name", "txtAddressLine3");
		
		lblCity = new Label(pnlGeneralInfo, SWT.LEFT);
		lblCity.setText("City");
		lblCity.setLayoutData(gridData2);
		lblCity.setData("name", "lblCity");
		txtCity = new Text(pnlGeneralInfo, SWT.BORDER);
		txtCity.setLayoutData(gridData3);
		txtCity.setTextLimit(35);
		txtCity.setData("name", "txtCity");
		
		lblState = new Label(pnlGeneralInfo, SWT.LEFT);
		lblState.setText("State/Province");
		lblState.setLayoutData(gridData2);
		lblState.setData("name", "lblState");
		txtState = new Text(pnlGeneralInfo, SWT.BORDER);
		txtState.setLayoutData(gridData3);
		txtState.setTextLimit(35);
		txtState.setData("name", "txtState");
		
		lblZipCode = new Label(pnlGeneralInfo, SWT.LEFT);
		lblZipCode.setText("ZipCode+Zip4");
		lblZipCode.setLayoutData(gridData2);
		lblZipCode.setData("name", "lblZipCode");
		
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = 4;
       	gridData5.widthHint = 200;
		gridData5.horizontalSpan = 2;
		gridData5.grabExcessVerticalSpace = true;
				
		Composite pnlZipText = new Composite(pnlGeneralInfo,SWT.NONE);
		GridLayout pnlZipTextLayout = new GridLayout(2,false);
		pnlZipTextLayout.verticalSpacing = 1;
		pnlZipTextLayout.marginWidth = 0;
		pnlZipText.setLayout(pnlZipTextLayout);
		
		GridData pnlZipTextLayoutData = new GridData();
		pnlZipTextLayoutData.horizontalAlignment = SWT.BEGINNING;
		pnlZipTextLayoutData.horizontalSpan = 2;
		pnlZipText.setLayoutData(pnlZipTextLayoutData);
		pnlZipText.setLayout(pnlZipTextLayout);
		pnlZipText.setLayoutData(pnlZipTextLayoutData);
		
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = SWT.BEGINNING;
		gridData10.widthHint = 100;
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = SWT.BEGINNING;
		gridData11.widthHint = 60;
		
		txtZipCode = new Text(pnlZipText, SWT.BORDER);
		txtZipCode.setTextLimit(35);
		txtZipCode.setLayoutData(gridData10);
		txtZipCode.setData("name", "txtZipCode");
		txtZip4 =  new Text(pnlZipText, SWT.BORDER);
		txtZip4.setLayoutData(gridData11);
		txtZip4.setTextLimit(10);
		txtZip4.setData("name", "txtZip4");
		
		lblCountry = new Label(pnlGeneralInfo, SWT.LEFT);
		lblCountry.setText("Country");
		lblCountry.setLayoutData(gridData2);
		lblCountry.setData("name", "lblCountry");
		txtCountry = new Text(pnlGeneralInfo, SWT.BORDER);
		txtCountry.setLayoutData(gridData3);
		txtCountry.setTextLimit(40);
		txtCountry.setData("name", "txtCountry");
	}
	/**
	 * @param pnlGeneralInfo
	 * @param pnlGeneralInfoLayout
	 * @param gridData2
	 */
	private void addUserTypeControls(Composite pnlGeneralInfo, GridLayout pnlGeneralInfoLayout, GridData gridData2) {
		lblUserType = new Label(pnlGeneralInfo, 16384);
		lblUserType.setText("Contact_User_Type");
		lblUserType.setLayoutData(gridData2);
		lblUserType.setData("name", "lblUserType");
		
		GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = 4;
        gridData4.widthHint = 200;
        gridData4.horizontalSpan = 2;
        gridData4.grabExcessVerticalSpace = true;
		
		Composite pnlUserTypeChkButtons = new Composite(pnlGeneralInfo,SWT.NONE);
		pnlUserTypeChkButtons.setLayout(pnlGeneralInfoLayout);
		pnlUserTypeChkButtons.setLayoutData(gridData4);
		chkBuyer = new Button(pnlUserTypeChkButtons, SWT.CHECK);
		chkBuyer.setText("Buyer");
		chkBuyer.setVisible(true);
		chkBuyer.setData("yrc:customType", "Label");
		chkBuyer.setData("name", "chkBuyer");
		chkBuyer.setEnabled(false);
		chkApprover = new Button(pnlUserTypeChkButtons, SWT.CHECK);
		chkApprover.setText("Approver");
		chkApprover.setVisible(true);
		chkApprover.setData("yrc:customType", "Label");
		chkApprover.setData("name", "chkApprover");
		chkEstimator = new Button(pnlUserTypeChkButtons, SWT.CHECK);
		chkEstimator.setText("Estimator");
		chkEstimator.setVisible(true);
		chkEstimator.setData("yrc:customType", "Label");
		chkEstimator.setData("name", "chkEstimator");
		chkStockCheckWS = new Button(pnlUserTypeChkButtons, SWT.CHECK);
		chkStockCheckWS.setText("Stock_Check");
		chkStockCheckWS.setVisible(true);
		chkStockCheckWS.setData("yrc:customType", "Label");
		chkStockCheckWS.setData("name", "chkStockCheckWS");
		chkAdmin = new Button(pnlUserTypeChkButtons, SWT.CHECK);
		chkAdmin.setText("Admin");
		chkAdmin.setVisible(true);
		chkAdmin.setData("yrc:customType", "Label");
		chkAdmin.setData("name", "chkAdmin");
		
		chkViewInvoices = new Button(pnlUserTypeChkButtons, SWT.CHECK);
		chkViewInvoices.setText("View_Invoices");
		chkViewInvoices.setVisible(true);
		chkViewInvoices.setData("yrc:customType", "Label");
		chkViewInvoices.setData("name", "chkViewInvoices");
		chkViewReports = new Button(pnlUserTypeChkButtons, SWT.CHECK);
		chkViewReports.setText("View_Reports");
		chkViewReports.setVisible(true);
		chkViewReports.setData("yrc:customType", "Label");
		chkViewReports.setData("name", "chkViewReports");
		chkViewPrices = new Button(pnlUserTypeChkButtons, SWT.CHECK);
		chkViewPrices.setText("View_Prices");
		chkViewPrices.setVisible(true);
		chkViewPrices.setData("yrc:customType", "Label");
		chkViewPrices.setData("name", "chkViewPrices");
		chkProcurementUser = new Button(pnlUserTypeChkButtons, SWT.CHECK);
		chkProcurementUser.setText("Procurement_User");
		chkProcurementUser.setVisible(true);
		chkProcurementUser.setData("yrc:customType", "Label");
		chkProcurementUser.setData("name", "chkProcurementUser");
		
		btnUsrRolesHelpInfo=new Button(pnlUserTypeChkButtons,SWT.ICON_INFORMATION);
		btnUsrRolesHelpInfo.setImage(YRCPlatformUI.getImage("Help"));
		btnUsrRolesHelpInfo.setText("User Roles Description");
		btnUsrRolesHelpInfo.setVisible(true);
		final String usrRoleHelpInfo=
			 "Buyer:  Permission to use the site. Required for all users." + "\n"+
			 "Approver:  Authorizes submission of orders."+"\n"+
			 "Estimator:  Can view pricing and inventory availability. Cannot submit an order." + "\n"+
			 "Stock Check:  Stock Check Web Service User for system integrations. (Does not" + "\n"+ "\t"+ 
			 "      control inventory display for regular site user)." + "\n" +
			 "Admin:  Permission to create user profiles, assign roles and ship to locations within" + "\n"+ 
			 "               the account." +"\n"+
			 "View Invoices:  Can view invoices online." + "\n"	+
			 "View Reports:  Can view reports. (Note: User should not view reports if cannot view" + "\n"+ "\t"+ 
			 "        pricing)." + "\n"	+
			 "View Prices:  Can view pricing." + "\n"+
			 "Procurement User:  Punchout User (punchout integration customers only).";
		//Providing Help icon for User Roles while updating profile. XNGTP-1185 -Fixed
		btnUsrRolesHelpInfo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				YRCPlatformUI.showInformation("User Roles Description", usrRoleHelpInfo);
			}
		});
	}
	
//	added by madhura
	private void createLocationsHeaderPnl() {
		Composite pnlLocationHeader = new Composite(pnlBody,SWT.NONE);
		pnlLocationHeader.setBackgroundMode(SWT.INHERIT_NONE);
		pnlLocationHeader.setData(YRCConstants.YRC_CONTROL_NAME, "pnlLocationHeader");
		GridLayout pnlLocationHeaderLayout = new GridLayout();
		pnlLocationHeaderLayout.horizontalSpacing = 1;
		pnlLocationHeaderLayout.verticalSpacing = 1;
		pnlLocationHeaderLayout.marginHeight = 2;
		pnlLocationHeaderLayout.marginWidth = 2;
		pnlLocationHeaderLayout.numColumns = 3;
		pnlLocationHeader.setLayout(pnlLocationHeaderLayout);
		
		GridData pnlLocationHeaderLayoutData = new GridData();
		pnlLocationHeaderLayoutData.horizontalAlignment = 4;
		pnlLocationHeaderLayoutData.grabExcessHorizontalSpace = true;
		pnlLocationHeader.setLayoutData(pnlLocationHeaderLayoutData);
		pnlLocationHeader.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		
		
		Label lblGeneralInfo = new Label(pnlLocationHeader, SWT.LEFT);
		GridData GenralInfoData = new GridData();
		GenralInfoData.grabExcessHorizontalSpace = true;
		lblGeneralInfo.setLayoutData(GenralInfoData);
		lblGeneralInfo.setText("Locations");
		lblGeneralInfo.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		
	}
	
	private void createLocationsPnl() {
		
		pnlLocations = new Composite(pnlBody,SWT.NONE);
		GridLayout pnlLocationsLayout = new GridLayout(4,true);
		pnlLocationsLayout.verticalSpacing = 1;
		pnlLocationsLayout.marginHeight = 2;
		pnlLocationsLayout.marginWidth = 2;
		pnlLocations.setLayout(pnlLocationsLayout);
		
		GridData pnlLocationsLayoutData = new GridData();
		pnlLocationsLayoutData.horizontalAlignment = 4;
//		pnlLocationsLayoutData.verticalAlignment = 4;
		pnlLocationsLayoutData.grabExcessHorizontalSpace = true;
		pnlLocationsLayoutData.grabExcessVerticalSpace = true;
		pnlLocations.setLayoutData(pnlLocationsLayoutData);
		pnlLocations.setData(YRCConstants.YRC_CONTROL_NAME, "pnlLocations");
		
		GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = 4;
        gridData2.horizontalIndent = 100;
        gridData2.widthHint = 100;
        gridData2.verticalIndent = 8;
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = SWT.BEGINNING;
        gridData3.widthHint = 150;
		//gridData3.horizontalSpan = 2;
        
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = SWT.BEGINNING;
        gridData4.widthHint = 150;
        gridData4.heightHint = 100;
        
        GridData gridDataCustID = new GridData();
        gridDataCustID.horizontalAlignment = SWT.BEGINNING;
        gridDataCustID.widthHint = 200;
//        gridDataCustID.horizontalSpan = 2;
        
        lblDefaultShipTo = new Label(pnlLocations, SWT.NONE);
		lblDefaultShipTo.setText("Contact_Default_ShipTo");
		lblDefaultShipTo.setLayoutData(gridData2);
		lblDefaultShipTo.setData("name", "lblDefaultShipTo");
		txtCustomerID = new Text(pnlLocations, SWT.BORDER);
		txtCustomerID.setText("");
		txtCustomerID.setLayoutData(gridDataCustID);
		txtCustomerID.setTextLimit(500);
		txtCustomerID.setData("name", "txtCustomerID");
		txtCustomerID.setEditable(false);
		 defaultShipto=new Button(pnlLocations,SWT.ICON_INFORMATION);
	    // defaultShipto.setImage(YRCPlatformUI.getImage("Help"));
	     defaultShipto.setText("Select Default Ship to");
	     defaultShipto.setLayoutData(gridData3);
	     defaultShipto.setData("name", "defaultShipto");
		
		//added by suneetha
		txtDefaultShipToAddress=new Text(pnlLocations,SWT.WRAP);
		txtDefaultShipToAddress.setLayoutData(gridData4);
		txtDefaultShipToAddress.setData("name","txtDefaultShipToAddress");

		defaultShipto.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
		/*	public void widgetDefaultSelected(SelectionEvent e) {
				addDefaultShipToAddress("ShipTo");
			}*/
			
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.addDefaultShipToAddress("defaultShipto");	
			}
		});
		
		
	}
	
	private void createSiteSettingsHeaderPnl() {
		Composite pnlSiteSettingsHeader = new Composite(pnlBody,SWT.NONE);
		pnlSiteSettingsHeader.setBackgroundMode(SWT.INHERIT_NONE);
		pnlSiteSettingsHeader.setData(YRCConstants.YRC_CONTROL_NAME, "pnlSiteSettingsHeader");
		GridLayout pnlpnlSiteSettingsHeaderHeaderLayout = new GridLayout();
		pnlpnlSiteSettingsHeaderHeaderLayout.horizontalSpacing = 1;
		pnlpnlSiteSettingsHeaderHeaderLayout.verticalSpacing = 1;
		pnlpnlSiteSettingsHeaderHeaderLayout.marginHeight = 2;
		pnlpnlSiteSettingsHeaderHeaderLayout.marginWidth = 2;
		pnlpnlSiteSettingsHeaderHeaderLayout.numColumns = 3;
		pnlSiteSettingsHeader.setLayout(pnlpnlSiteSettingsHeaderHeaderLayout);
		
		GridData pnlSiteSettingsHeaderLayoutData = new GridData();
		pnlSiteSettingsHeaderLayoutData.horizontalAlignment = 4;
		pnlSiteSettingsHeaderLayoutData.grabExcessHorizontalSpace = true;
		pnlSiteSettingsHeader.setLayoutData(pnlSiteSettingsHeaderLayoutData);
		pnlSiteSettingsHeader.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		
		
		Label lblGeneralInfo = new Label(pnlSiteSettingsHeader, SWT.LEFT);
		GridData GenralInfoData = new GridData();
		GenralInfoData.grabExcessHorizontalSpace = true;
		lblGeneralInfo.setLayoutData(GenralInfoData);
		lblGeneralInfo.setText("Site Settings");
		lblGeneralInfo.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
	}
	
	private void createSiteSettingsPnl() {
		//to do
		pnlSiteSettings = new Composite(pnlBody,SWT.NONE);
		GridLayout pnlGeneralInfoLayout = new GridLayout(3,true);
		//pnlGeneralInfoLayout.horizontalSpacing = 1;
		pnlGeneralInfoLayout.verticalSpacing = 1;
		pnlGeneralInfoLayout.marginHeight = 2;
		pnlGeneralInfoLayout.marginWidth = 2;
		//pnlGeneralInfoLayout.horizontalSpacing = 9;
		
		//pnlGeneralInfoLayout.numColumns = 3;
		pnlSiteSettings.setLayout(pnlGeneralInfoLayout);
		
		GridData pnlGeneralInfoLayoutData = new GridData();
		pnlGeneralInfoLayoutData.horizontalAlignment = 4;
		pnlGeneralInfoLayoutData.grabExcessHorizontalSpace = true;
		pnlGeneralInfoLayoutData.grabExcessVerticalSpace = true;
		pnlSiteSettings.setLayoutData(pnlGeneralInfoLayoutData);
		pnlSiteSettings.setData(YRCConstants.YRC_CONTROL_NAME, "pnlSiteSettings");
		
		GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = 4;
        gridData2.horizontalIndent = 100;
        gridData2.widthHint = 150;
        gridData2.verticalIndent = 8;
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = SWT.BEGINNING;
        //gridData3.grabExcessHorizontalSpace = true;
        gridData3.widthHint = 200;
        gridData3.horizontalSpan = 2;
		
		lblMinOrderAmount = new Label(pnlSiteSettings, SWT.LEFT);
		lblMinOrderAmount.setText("Minimum_Order_Amount");
		lblMinOrderAmount.setLayoutData(gridData2);
		lblMinOrderAmount.setData("name", "lblMinOrderAmount");
		txtMinOrderAmount = new Text(pnlSiteSettings, SWT.BORDER);
		txtMinOrderAmount.setText("");
		txtMinOrderAmount.setLayoutData(gridData3);
		txtMinOrderAmount.setTextLimit(20);
		txtMinOrderAmount.setData("name", "txtMinOrderAmount");
		
		lblMaxOrderAmount = new Label(pnlSiteSettings, SWT.LEFT);
		lblMaxOrderAmount.setText("Maximum_Order_Amount");
		lblMaxOrderAmount.setLayoutData(gridData2);
		lblMaxOrderAmount.setData("name", "lblMaxOrderAmount");
		txtMaxOrderAmount = new Text(pnlSiteSettings, SWT.BORDER);
		txtMaxOrderAmount.setText("");
		txtMaxOrderAmount.setLayoutData(gridData3);
		txtMaxOrderAmount.setTextLimit(20);
		txtMaxOrderAmount.setData("name", "txtMaxOrderAmount");
		
		//Added For CR 2407
		lblPrimApprover = new Label(pnlSiteSettings, SWT.NONE);
		lblPrimApprover.setText("Primary Approver");
		lblPrimApprover.setLayoutData(gridData2);
		lblPrimApprover.setData("name", "lblPrimApprover");
		comboPrimApprover = new Combo(pnlSiteSettings, 8);
		comboPrimApprover.setLayoutData(gridData3);
		comboPrimApprover.setTextLimit(50);
		comboPrimApprover.setData("name", "comboPrimApprover");
		
		
		lblAlterApprover = new Label(pnlSiteSettings, SWT.NONE);
		lblAlterApprover.setText("Alternate Approver");
		lblAlterApprover.setLayoutData(gridData2);
		lblAlterApprover.setData("name", "lblAlterApprover");
		comboAlterApprover = new Combo(pnlSiteSettings, 8);
		comboAlterApprover.setLayoutData(gridData3);
		comboAlterApprover.setTextLimit(50);
		comboAlterApprover.setData("name", "comboAlterApprover");
		
		/* Start- For Jira 3264 */
		lblCurrencyType = new Label(pnlSiteSettings, SWT.NONE);
		lblCurrencyType.setText("Currency Type");
		lblCurrencyType.setLayoutData(gridData2);
		lblCurrencyType.setData("name", "lblCurrencyType");
		comboCurrencyType = new Combo(pnlSiteSettings, 8);
		comboCurrencyType.setLayoutData(gridData3);
		comboCurrencyType.setTextLimit(50);
		comboCurrencyType.setData("name", "comboCurrencyType");
		
		lblSpendingLimit = new Label(pnlSiteSettings, SWT.LEFT);
		lblSpendingLimit.setText("Spending Limit");
		lblSpendingLimit.setLayoutData(gridData2);
		lblSpendingLimit.setData("name", "lblSpendingLimit");
		txtSpendingLimit = new Text(pnlSiteSettings, SWT.BORDER);
		txtSpendingLimit.setText("");
		txtSpendingLimit.setLayoutData(gridData3);
		txtSpendingLimit.setTextLimit(15);
		txtSpendingLimit.setData("name", "txtSpendingLimit");
		
		/*End- For Jira 3264 */
		
		lblPreferredCatalogView = new Label(pnlSiteSettings, SWT.LEFT);
		lblPreferredCatalogView.setText("Preferred_Catalog_View");
		lblPreferredCatalogView.setLayoutData(gridData2);
		lblPreferredCatalogView.setData("name", "lblPreferredCatalogView");
		GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = 4;
        //gridData3.grabExcessHorizontalSpace = true;
        gridData4.widthHint = 200;
        gridData4.horizontalSpan = 2;
        gridData4.grabExcessVerticalSpace = true;
		
		Composite pnlPreferredCatRadButtons = new Composite(pnlSiteSettings,SWT.NONE);
		pnlPreferredCatRadButtons.setLayout(pnlGeneralInfoLayout);
		pnlPreferredCatRadButtons.setLayoutData(gridData4);
		radItem1 = new Button(pnlPreferredCatRadButtons, SWT.RADIO);
		radItem1.setText("1_Item/Row");
		radItem1.setData("name", "radItem1");
		radItem1.setData("yrc:customType", "Label");
		radItem2 = new Button(pnlPreferredCatRadButtons, SWT.RADIO);
		radItem2.setText("2_Item/Row");
		radItem2.setData("name", "radItem2");
		radItem2.setData("yrc:customType", "Label");
		radItem4 = new Button(pnlPreferredCatRadButtons, SWT.RADIO);
		radItem4.setText("4_Item/Row");
		radItem4.setData("name", "radItem1");
		radItem4.setData("yrc:customType", "Label");
		radMatrix = new Button(pnlPreferredCatRadButtons, SWT.RADIO);
		radMatrix.setText("Matrix-No_Images");
		radMatrix.setData("name", "radMatrix");
		radMatrix.setData("yrc:customType", "Label");
		
		lblPrefferedCatalog = new Label(pnlSiteSettings, SWT.LEFT);
		lblPrefferedCatalog.setText("Preffered_Catalog");
		lblPrefferedCatalog.setLayoutData(gridData2);
		lblPrefferedCatalog.setData("name", "lblPrefferedCatalog");
//		txtPrefferedCatalog = new Text(pnlSiteSettings, SWT.BORDER);
//		txtPrefferedCatalog.setText("");
//		txtPrefferedCatalog.setLayoutData(gridData3);
//		txtPrefferedCatalog.setTextLimit(500);
//		txtPrefferedCatalog.setData("name", "txtPrefferedCatalog");
		
		comboPrefferedCatalog = new Combo(pnlSiteSettings, 8);
		comboPrefferedCatalog.setLayoutData(gridData3);
		comboPrefferedCatalog.setData("name", "comboPrefferedCatalog");
		
		
		GridData gridData12 = new GridData();
        gridData12.horizontalAlignment = 4;
        gridData12.horizontalIndent = 100;
        //gridData12.widthHint = 175;
        gridData12.verticalIndent = 8;
        
		lblOrderConfirmationAddressList = new Label(pnlSiteSettings, SWT.WRAP);
		lblOrderConfirmationAddressList.setText("Order_Confirmation_Address_List");
		lblOrderConfirmationAddressList.setLayoutData(gridData12);
		lblOrderConfirmationAddressList.setData("name", "lblOrderConfirmationAddressList");
		
		GridLayout gridLayoutx1 = new GridLayout(3,false);
		gridLayoutx1.horizontalSpacing = 1;
		gridLayoutx1.verticalSpacing = 5;
		gridLayoutx1.marginHeight = 2;
		gridLayoutx1.marginWidth = 2;
		
		
		GridData gridDatax1 = new GridData();
		gridDatax1.horizontalAlignment = SWT.BEGINNING;
		gridDatax1.horizontalSpan = 2;
		gridDatax1.verticalIndent = 10;
		
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = 4;
		
		pnlOrderConfirmList = new Composite(pnlSiteSettings, SWT.NONE);
		pnlOrderConfirmList.setLayout(gridLayoutx1);
		pnlOrderConfirmList.setLayoutData(gridDatax1);
		pnlOrderConfirmList.setData("name", "pnlOrderConfirmList");
		
		txtOrderConfirmationList = new Text(pnlOrderConfirmList, SWT.BORDER);
		txtOrderConfirmationList.setText("");
		txtOrderConfirmationList.setLayoutData(gridData8);
		txtOrderConfirmationList.setTextLimit(500);
		txtOrderConfirmationList.setData("name", "txtOrderConfirmationList");	
		
		createPnlOrderConfirmListButtons();
		createPnlOrderConfirmListHolder();
		//createPnlAdditionalEmailsButtons();
		//createPnlAdditionalEmailsHolder();
		
		lblReceiveOrderConfirmEmails = new Label(pnlSiteSettings, SWT.LEFT);
		lblReceiveOrderConfirmEmails.setText("Receive_E-Mails");
		lblReceiveOrderConfirmEmails.setLayoutData(gridData2);
		lblReceiveOrderConfirmEmails.setData("name", "lblReceiveOrderConfirmEmails");
		
		GridLayout pnlChkInfoLayout = new GridLayout(1,false);
		pnlChkInfoLayout.horizontalSpacing = 1;
		pnlChkInfoLayout.verticalSpacing = 1;
		pnlChkInfoLayout.marginHeight = 2;
		pnlChkInfoLayout.marginWidth = 2;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = SWT.FILL;;
        //gridData3.grabExcessHorizontalSpace = true;
		gridData6.widthHint = 150;
		gridData6.horizontalSpan = 2;
		gridData6.verticalSpan = 1;
		gridData6.grabExcessVerticalSpace = true;
		gridData6.verticalIndent =10;
		
		
		Composite pnlReceiveEmailChkButtons = new Composite(pnlSiteSettings,SWT.NONE);
		pnlReceiveEmailChkButtons.setLayout(pnlChkInfoLayout);
		pnlReceiveEmailChkButtons.setLayoutData(gridData6);
		chkReceiveOrderConfirmationEmail = new Button(pnlReceiveEmailChkButtons, SWT.CHECK);
		chkReceiveOrderConfirmationEmail.setText("Received_Order_Confirmation_Email");
		chkReceiveOrderConfirmationEmail.setVisible(true);
		chkReceiveOrderConfirmationEmail.setData("yrc:customType", "Label");
		chkReceiveOrderConfirmationEmail.setData("name", "chkReceiveOrderConfirmationEmail");
		chkReceiveOrderCancellationEmail = new Button(pnlReceiveEmailChkButtons, SWT.CHECK);
		chkReceiveOrderCancellationEmail.setText("Received_Order_cancellation_Email");
		chkReceiveOrderCancellationEmail.setVisible(true);
		chkReceiveOrderCancellationEmail.setData("yrc:customType", "Label");
		chkReceiveOrderCancellationEmail.setData("name", "chkReceiveOrderCancellationEmail");

		//Removed for the BugFix id-1004	
/*		chkReceiveOrderUpdateEmail = new Button(pnlReceiveEmailChkButtons, SWT.CHECK);
		chkReceiveOrderUpdateEmail.setText("Received_Order_Update_Email_By_CSR");
		chkReceiveOrderUpdateEmail.setVisible(true);
		chkReceiveOrderUpdateEmail.setData("yrc:customType", "Label");
		chkReceiveOrderUpdateEmail.setData("name", "chkReceiveOrderUpdateEmail");*/
		//End of Removed for the BugFix id-1004
		chkReceiveOrderShipmentEmail = new Button(pnlReceiveEmailChkButtons, SWT.CHECK);
		chkReceiveOrderShipmentEmail.setText("Received_Order_Shipment_Email");
		chkReceiveOrderShipmentEmail.setVisible(true);
		chkReceiveOrderShipmentEmail.setData("yrc:customType", "Label");
		chkReceiveOrderShipmentEmail.setData("name", "chkReceiveOrderShipmentEmail");
		
		chkeceiveBackOrderEmail = new Button(pnlReceiveEmailChkButtons, SWT.CHECK);
		chkeceiveBackOrderEmail.setText("Received_Back_Order_Email");
		chkeceiveBackOrderEmail.setVisible(true);
		chkeceiveBackOrderEmail.setData("yrc:customType", "Label");
		chkeceiveBackOrderEmail.setData("name", "chkeceiveBackOrderEmail");
		
		
		/**********************************/
		
		lblInvoiceEmailId = new Label(pnlSiteSettings, SWT.WRAP);
		lblInvoiceEmailId.setText("Email_Address_For_Invoice");
		lblInvoiceEmailId.setLayoutData(gridData12);
		lblInvoiceEmailId.setData("name", "lblInvoiceEmailId");
		
		GridLayout gridLayoutx2 = new GridLayout(3,false);
		gridLayoutx2.horizontalSpacing = 1;
		gridLayoutx2.verticalSpacing = 1;
		gridLayoutx2.marginHeight = 2;
		gridLayoutx2.marginWidth = 2;
		
		GridData gridDatax2 = new GridData();
		gridDatax2.horizontalAlignment = SWT.BEGINNING;
		gridDatax2.horizontalSpan = 2;
		gridDatax2.verticalIndent = 10;
		
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = 4;

        			
		pnlInvoiceList = new Composite(pnlSiteSettings, SWT.NONE);
		pnlInvoiceList.setLayout(gridLayoutx2);
		pnlInvoiceList.setLayoutData(gridDatax2);
		pnlInvoiceList.setData("name", "pnlInvoiceList");

		txtInvoiceEmailId = new Text(pnlInvoiceList, SWT.BORDER);
		txtInvoiceEmailId.setText("");
		txtInvoiceEmailId.setLayoutData(gridData9);
		txtInvoiceEmailId.setTextLimit(500);
		txtInvoiceEmailId.setData("name", "txtInvoiceEmailId");	
		
		createPnlInvoiceListButtons();
		createPnlInvoiceListHolder();
		
		/**********************************/
		
		lblPOList = new Label(pnlSiteSettings, SWT.NONE);
		lblPOList.setText("PO_List");
		lblPOList.setLayoutData(gridData2);
		lblPOList.setData("name", "lblPOList");
		
		GridLayout gridLayoutx = new GridLayout(3,false);
		gridLayoutx.horizontalSpacing = 1;
		gridLayoutx.verticalSpacing = 1;
		gridLayoutx.marginHeight = 2;
		gridLayoutx.marginWidth = 2;
		
		GridData gridDatax = new GridData();
		gridDatax.horizontalAlignment = SWT.BEGINNING;
		gridDatax.horizontalSpan = 2;
		gridDatax.verticalIndent = 10;
		
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = 4;

		pnlPOLIst = new Composite(pnlSiteSettings, SWT.NONE);
		pnlPOLIst.setLayout(gridLayoutx);
		pnlPOLIst.setLayoutData(gridDatax);
		pnlPOLIst.setData("name", "pnlPOLIst");

		txtPOList = new Text(pnlPOLIst, SWT.BORDER);
		txtPOList.setText("");
		txtPOList.setLayoutData(gridData7);
		txtPOList.setTextLimit(500);
		txtPOList.setData("name", "txtPOList");	
		
		createPnlPOListButtons();
		createPnlPOListHolder();

	}
	
	private void createPnlInvoiceListButtons() {
		pnlInvoiceListButtons = new Composite(pnlInvoiceList, SWT.NONE);
		pnlInvoiceListButtons.setBackgroundMode(0);
		pnlInvoiceListButtons.setData("name", "pnlInvoiceListButtons");
		GridData pnlInvoiceListButtonslayoutData = new GridData();
		pnlInvoiceListButtonslayoutData.horizontalAlignment = SWT.BEGINNING;
		pnlInvoiceListButtons.setLayoutData(pnlInvoiceListButtonslayoutData);
		GridLayout pnlInvoiceListButtonslayout = new GridLayout(1, false);
		pnlInvoiceListButtonslayout.marginHeight = 0;
		pnlInvoiceListButtonslayout.marginWidth = 0;
		pnlInvoiceListButtons.setLayout(pnlInvoiceListButtonslayout);
		btnAdd = new Button(pnlInvoiceListButtons, 8);
		GridData btnAddListlayoutData = new GridData();
		btnAddListlayoutData.horizontalAlignment = SWT.BEGINNING;
		btnAddListlayoutData.grabExcessHorizontalSpace = true;
		btnAdd.setLayoutData(btnAddListlayoutData);
		btnAdd.setText("Btn_Add");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addInvoiceEmail();
				txtInvoiceEmailId.setFocus();
			}
		});
		btnRmv = new Button(pnlInvoiceListButtons, 8);
		GridData btnRemovelayoutData = new GridData();
		btnRemovelayoutData.horizontalAlignment = SWT.BEGINNING;
		btnRemovelayoutData.verticalAlignment = 1;
		btnRmv.setLayoutData(btnRemovelayoutData);
		btnRmv.setText("Btn_Remove");
		btnRmv.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeInvoiceEmails();
			}
		});
	}
	
	private void createPnlInvoiceListHolder() {
		pnlInvoiceListHolder = new Composite(pnlInvoiceList, SWT.NONE);
		pnlInvoiceListHolder.setBackgroundMode(0);
		pnlInvoiceListHolder.setData("name", "pnlInvoiceListHolder");
		GridData pnlInvoiceListHolderlayoutData = new GridData();
		pnlInvoiceListHolderlayoutData.horizontalAlignment = 4;
		pnlInvoiceListHolderlayoutData.verticalAlignment = 1;
		pnlInvoiceListHolderlayoutData.grabExcessHorizontalSpace = true;
		pnlInvoiceListHolderlayoutData.widthHint = 300;
		pnlInvoiceListHolderlayoutData.heightHint = 150;
		pnlInvoiceListHolder.setLayoutData(pnlInvoiceListHolderlayoutData);
		GridLayout pnlInvoiceListHolderlayout = new GridLayout(1, false);
		pnlInvoiceListHolderlayout.verticalSpacing = 1;
		pnlInvoiceListHolderlayout.marginHeight = 1;
		pnlInvoiceListHolderlayout.marginWidth = 1;
		pnlInvoiceListHolder.setLayout(pnlInvoiceListHolderlayout);
		createInvoiceList();
	}
	
	private void createInvoiceList() {
		invoiceList = new Table(pnlInvoiceListHolder, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		invoiceList.setHeaderVisible(true);
		invoiceList.setLinesVisible(true);
		invoiceList.setData("name", "invoiceList");
		GridData invoiceListlayoutData = new GridData();
		invoiceListlayoutData.horizontalAlignment = 4;
		invoiceListlayoutData.verticalAlignment = 4;
		invoiceListlayoutData.grabExcessHorizontalSpace = true;
		invoiceListlayoutData.grabExcessVerticalSpace = true;
		invoiceList.setLayoutData(invoiceListlayoutData);
		tblInvoiceListId = new TableColumn(invoiceList, SWT.NONE);
		tblInvoiceListId.setWidth(300);
		tblInvoiceListId.setResizable(true);
		tblInvoiceListId.setMoveable(true);
	}
	
	private void createPnlOrderConfirmListButtons() {
		pnlOrderConfirmListButtons = new Composite(pnlOrderConfirmList, 0);
		pnlOrderConfirmListButtons.setBackgroundMode(0);
		pnlOrderConfirmListButtons.setData("name", "pnlOrderConfirmListButtons");
		GridData pnlOrderConfirmListButtonslayoutData = new GridData();
		pnlOrderConfirmListButtonslayoutData.horizontalAlignment = 4;
		pnlOrderConfirmListButtons.setLayoutData(pnlOrderConfirmListButtonslayoutData);
		GridLayout pnlOrderConfirmListButtonslayout = new GridLayout(1, false);
		pnlOrderConfirmListButtonslayout.marginHeight = 0;
		pnlOrderConfirmListButtonslayout.marginWidth = 0;
		pnlOrderConfirmListButtons.setLayout(pnlOrderConfirmListButtonslayout);
		btnAdd = new Button(pnlOrderConfirmListButtons, 8);
		GridData btnOrderConfirmationListlayoutData = new GridData();
		btnOrderConfirmationListlayoutData.horizontalAlignment = SWT.BEGINNING;
		btnOrderConfirmationListlayoutData.grabExcessHorizontalSpace = true;
		btnAdd.setLayoutData(btnOrderConfirmationListlayoutData);
		btnAdd.setText("Btn_Add");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addEmail();
				txtOrderConfirmationList.setFocus();
			}
		});
		btnRmv = new Button(pnlOrderConfirmListButtons, 8);
		GridData btnRemovelayoutData = new GridData();
		btnRemovelayoutData.horizontalAlignment = SWT.BEGINNING;
		btnRmv.setLayoutData(btnRemovelayoutData);
		btnRmv.setText("Btn_Remove");
		btnRmv.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeEmailss();
			}
		});
	}
	
	private void createPnlOrderConfirmListHolder() {
		pnlOrderConfirmListHolder = new Composite(pnlOrderConfirmList, 0);
		pnlOrderConfirmListHolder.setBackgroundMode(0);
		pnlOrderConfirmListHolder.setData("name", "pnlOrderConfirmListHolder");
		GridData pnlOrderConfirmListHolderlayoutData = new GridData();
		pnlOrderConfirmListHolderlayoutData.horizontalAlignment = 4;
		pnlOrderConfirmListHolderlayoutData.verticalAlignment = 1;
		pnlOrderConfirmListHolderlayoutData.grabExcessHorizontalSpace = true;
		pnlOrderConfirmListHolderlayoutData.widthHint = 300;
		pnlOrderConfirmListHolderlayoutData.heightHint = 150;
		pnlOrderConfirmListHolder.setLayoutData(pnlOrderConfirmListHolderlayoutData);
		GridLayout pnlOrderConfirmListHolderlayout = new GridLayout(1, false);
		pnlOrderConfirmListHolderlayout.verticalSpacing = 1;
		pnlOrderConfirmListHolderlayout.marginHeight = 1;
		pnlOrderConfirmListHolderlayout.marginWidth = 1;
		pnlOrderConfirmListHolder.setLayout(pnlOrderConfirmListHolderlayout);
		createOrderConfirmList();
	}
	
	private void createOrderConfirmList() {
		//to do
		orderConfirmList = new Table(pnlOrderConfirmListHolder, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		orderConfirmList.setHeaderVisible(true);
		orderConfirmList.setLinesVisible(true);
		orderConfirmList.setData("name", "orderConfirmList");
		GridData orderConfirmListlayoutData = new GridData();
		orderConfirmListlayoutData.horizontalAlignment = 4;
		orderConfirmListlayoutData.verticalAlignment = 4;
		orderConfirmListlayoutData.grabExcessHorizontalSpace = true;
		orderConfirmListlayoutData.grabExcessVerticalSpace = true;
		orderConfirmList.setLayoutData(orderConfirmListlayoutData);
		tblOrderConfirmListId = new TableColumn(orderConfirmList, SWT.NONE);
		tblOrderConfirmListId.setWidth(300);
		tblOrderConfirmListId.setResizable(true);
		tblOrderConfirmListId.setMoveable(true);
	}

	private void createPnlPOListHolder() {
		pnlPOListHolder = new Composite(pnlPOLIst, SWT.NONE);
		pnlPOListHolder.setBackgroundMode(0);
		pnlPOListHolder.setData("name", "pnlPOListHolder");
		GridData pnlPOListHolderlayoutData = new GridData();
		pnlPOListHolderlayoutData.horizontalAlignment = 4;
		pnlPOListHolderlayoutData.verticalAlignment = 1;
		pnlPOListHolderlayoutData.grabExcessHorizontalSpace = true;
		pnlPOListHolderlayoutData.widthHint = 300;
		pnlPOListHolderlayoutData.heightHint = 150;
		pnlPOListHolder.setLayoutData(pnlPOListHolderlayoutData);
		GridLayout pnlPOListHolderlayout = new GridLayout(1, false);
		pnlPOListHolderlayout.horizontalSpacing = 1;
		pnlPOListHolderlayout.verticalSpacing = 1;
		pnlPOListHolderlayout.marginHeight = 1;
		pnlPOListHolderlayout.marginWidth = 1;
		pnlPOListHolder.setLayout(pnlPOListHolderlayout);
		createPOList();
	}

	private void createPOList() {
		itemPOList = new Table(pnlPOListHolder, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		itemPOList.setHeaderVisible(true);
		itemPOList.setLinesVisible(true);
		//itemPOList.setItemCount(0);
		itemPOList.setData("name", "itemPOList");
		GridData itemPOListlayoutData = new GridData();
		itemPOListlayoutData.horizontalAlignment = 4;
		itemPOListlayoutData.verticalAlignment = 4;
		itemPOListlayoutData.grabExcessHorizontalSpace = true;
		itemPOListlayoutData.grabExcessVerticalSpace = true;
		itemPOList.setLayoutData(itemPOListlayoutData);
		tblPOListId = new TableColumn(itemPOList, SWT.LEFT);
		tblPOListId.setResizable(true);
		tblPOListId.setWidth(300);
		tblPOListId.setMoveable(true);
	}

	private void createPnlPOListButtons() {
		pnlPOListButtons = new Composite(pnlPOLIst, 0);
		pnlPOListButtons.setBackgroundMode(0);
		pnlPOListButtons.setData("name", "pnlPOListButtons");
		GridData pnlPOListButtonslayoutData = new GridData();
		pnlPOListButtonslayoutData.horizontalAlignment = 4;
		pnlPOListButtons.setLayoutData(pnlPOListButtonslayoutData);
		GridLayout pnlPOListButtonslayout = new GridLayout(1, false);
		pnlPOListButtonslayout.marginHeight = 0;
		pnlPOListButtonslayout.marginWidth = 0;
		pnlPOListButtons.setLayout(pnlPOListButtonslayout);
		btnAdd = new Button(pnlPOListButtons, 8);
		GridData btnPOListlayoutData = new GridData();
		btnPOListlayoutData.horizontalAlignment = SWT.BEGINNING;
		btnPOListlayoutData.grabExcessHorizontalSpace = true;
		btnAdd.setLayoutData(btnPOListlayoutData);
		btnAdd.setText("Btn_AddPO_Arrow");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addPONumber();
				txtPOList.setFocus();
			}
		});
		btnRmv = new Button(pnlPOListButtons, 8);
		GridData btnRemovelayoutData = new GridData();
		btnRemovelayoutData.horizontalAlignment = SWT.BEGINNING;
		btnRmv.setLayoutData(btnRemovelayoutData);
		btnRmv.setText("Btn_Remove_PO");
		btnRmv.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removePOs();
			}
		});
	}

	private void removePOs() {
		myBehavior.removePOs();
	}

	private void addPONumber() {
		myBehavior.addPONumber();
	}

	private void removeEmailss() {
		myBehavior.removeEmails();
	}

	private void addEmail() {
		myBehavior.addEmail();
	}

	private void removeInvoiceEmails() {
		myBehavior.removeInvoiceEmails();
	}

	private void addInvoiceEmail() {
		myBehavior.addInvoiceEmail();
	}

	private void setBindingForComponents() {
		YRCButtonBindingData chkBoxBindingData = null;
		YRCTextBindingData tbd = null;
		YRCComboBindingData cbd = null;

		tbd = new YRCTextBindingData();
		//tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnEmployeeID");
		//tbd.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnEmployeeID");
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@UserID");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/@UserID");
		tbd.setName("txtUserID");
		txtUserID.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		YRCButtonBindingData rdbBindingData = new YRCButtonBindingData();
		rdbBindingData.setName("radUserActive");
		rdbBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@Status");
		rdbBindingData.setTargetBinding("XPXResultOut:/CustomerContact/@Status");
		rdbBindingData.setCheckedBinding("10");
		radUserActive.setData("YRCButtonBindingDefination",rdbBindingData);
		
		YRCButtonBindingData rdbBindingData1 = new YRCButtonBindingData();
		rdbBindingData1.setName("radUserSuspend");
		rdbBindingData1.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@Status");
		rdbBindingData1.setTargetBinding("XPXResultOut:/CustomerContact/@Status");
		//rdbBindingData1.setCheckedBinding("20");
		rdbBindingData1.setCheckedBinding("30"); //---Binding value change to 30 for BugFixId-966
		radUserSuspend.setData("YRCButtonBindingDefination",rdbBindingData1);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomercontactExtn:/XPXCustomercontactExtn/@TAndCAcceptedOn");
		tbd.setDataType("Date");
		tbd.setName("txtTCAccepted");
		txtTCAccepted.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomercontactExtn:/XPXCustomercontactExtn/@LastLoginDate");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnLastLoginDate");
		tbd.setDataType("Date");
		tbd.setName("txtLastLoginDate");
		txtLastLoginDate.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/User/UserGroupLists/@BuyerUser");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/User/UserGroupLists/@BuyerUser");
		chkBoxBindingData.setName("chkBuyer");
		chkBuyer.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/User/UserGroupLists/@BuyerApprover");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/User/UserGroupLists/@BuyerApprover");
		chkBoxBindingData.setName("chkApprover");
		chkApprover.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/User/UserGroupLists/@BuyerAdmin");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/User/UserGroupLists/@BuyerAdmin");
		chkBoxBindingData.setName("chkAdmin");
		chkAdmin.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/User/UserGroupLists/@ProcurementUser");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/User/UserGroupLists/@ProcurementUser");
		chkBoxBindingData.setName("chkProcurementUser");
		chkProcurementUser.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("T");
		chkBoxBindingData.setUnCheckedBinding("F");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnEstimator");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnEstimator");
		chkBoxBindingData.setName("chkEstimator");
		chkEstimator.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("T");
		chkBoxBindingData.setUnCheckedBinding("F");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnStockCheckWS");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnStockCheckWS");
		chkBoxBindingData.setName("chkStockCheckWS");
		chkStockCheckWS.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnViewInvoices");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnViewInvoices");
		chkBoxBindingData.setName("chkViewInvoices");
		chkViewInvoices.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnViewReportsFlag");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnViewReportsFlag");
		chkBoxBindingData.setName("chkViewReports");
		chkViewReports.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnViewPricesFlag");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnViewPricesFlag");
		chkBoxBindingData.setName("chkViewPrices");
		chkViewPrices.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@FirstName");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/@FirstName");
		tbd.setName("txtFirstName");
		txtFirstName.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@LastName");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/@LastName");
		tbd.setName("txtLastName");
		txtLastName.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@EmailID");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/@EmailID");
		tbd.setName("txtEmailAddress");
		txtEmailAddress.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@DayPhone");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/@DayPhone");
		tbd.setName("txtPhone");
		txtPhone.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@AddressLine1");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@AddressLine1");
		tbd.setName("txtAddressLine1");
		txtAddressLine1.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@AddressLine2");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@AddressLine2");
		tbd.setName("txtAddressLine2");
		txtAddressLine2.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@AddressLine3");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@AddressLine3");
		tbd.setName("txtAddressLine3");
		txtAddressLine3.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@City");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@City");
		tbd.setName("txtCity");
		txtCity.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@State");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@State");
		tbd.setName("txtState");
		txtState.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@ZipCode");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@ZipCode");
		tbd.setName("txtZipCode");
		txtZipCode.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/Extn/@ExtnZip4");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/Extn/@ExtnZip4");
		tbd.setName("txtZip4");
		txtZip4.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@Country");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@Country");
		tbd.setName("txtCountry");
		txtCountry.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnMaxOrderAmount");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnMaxOrderAmount");
		tbd.setName("txtMaxOrderAmount");
		tbd.setDataType("Price");
		txtMaxOrderAmount.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);

		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnMinOrderAmount");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnMinOrderAmount");
		tbd.setDataType("Price");
		tbd.setName("txtMinOrderAmount");
		txtMinOrderAmount.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);

		cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@CategoryID");
		cbd.setDescriptionBinding("@ShortDescription");
		cbd.setListBinding("XPXSearchCatalogIndex:/CatalogSearch/CategoryList/Category");
		cbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnPrefCatalog");
		cbd.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnPrefCatalog");
		cbd.setName("comboPrefferedCatalog");
		comboPrefferedCatalog.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);

		/*cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@CustomerID");
		cbd.setDescriptionBinding("@CustomerID");
		cbd.setListBinding("XPXGetCustomerAssignmentList:/CustomerList/Customer");
		cbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnDefaultShipTo");
		cbd.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnDefaultShipTo");
		cbd.setName("comboDefaultShipTo");
		comboDefaultShipTo.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);*/
		
		cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@CustomerContactID");
		cbd.setDescriptionBinding("@CustomerContactID");
		cbd.setListBinding("CustomerContactDetails:/CustomerContactList/CustomerContact");
		cbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@ApproverUserId");
		//cbd.setTargetBinding("XPXCustomercontactExtn:/CustomerContact/@ApproverUserId");
		cbd.setName("comboPrimApprover");
		comboPrimApprover.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		
		cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@CustomerContactID");
		cbd.setDescriptionBinding("@CustomerContactID");
		cbd.setListBinding("CustomerContactDetails:/AdminCustomerList/CustomerContact");
		cbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@ApproverProxyUserId");
		//cbd.setTargetBinding("CustomerContactDetails:/CustomerContactList/CustomerContact/@CustomerContactID");
		cbd.setName("comboAlterApprover");
		comboAlterApprover.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		
		/*Start- For Jira 3264*/
		cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@CurrencyValue");
		cbd.setDescriptionBinding("@CurrencyType");
		cbd.setListBinding("GetCurrencyType:/CurrencyType");
		cbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@SpendingLimitCurrency");
		cbd.setName("comboCurrencyType");
		comboCurrencyType.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@SpendingLimit");
		tbd.setName("txtSpendingLimit");
		txtSpendingLimit.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		/*End- For Jira 3264*/
		
		YRCButtonBindingData radItem1ButtonBindingData = new YRCButtonBindingData();
		radItem1ButtonBindingData.setName("radItem1");
		radItem1ButtonBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnB2BCatalogView");
		radItem1ButtonBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnB2BCatalogView");
		radItem1ButtonBindingData.setCheckedBinding("0");
		radItem1.setData("YRCButtonBindingDefination",radItem1ButtonBindingData);
		
		YRCButtonBindingData radItem2ButtonBindingData = new YRCButtonBindingData();
		radItem2ButtonBindingData.setName("radItem2");
		radItem2ButtonBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnB2BCatalogView");
		radItem2ButtonBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnB2BCatalogView");
		radItem2ButtonBindingData.setCheckedBinding("1");
		radItem2.setData("YRCButtonBindingDefination",radItem2ButtonBindingData);
		
		YRCButtonBindingData radItem4ButtonBindingData = new YRCButtonBindingData();
		radItem4ButtonBindingData.setName("radItem4");
		radItem4ButtonBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnB2BCatalogView");
		radItem4ButtonBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnB2BCatalogView");
		radItem4ButtonBindingData.setCheckedBinding("2");
		radItem4.setData("YRCButtonBindingDefination",radItem4ButtonBindingData);
		
		YRCButtonBindingData radItemMatrixButtonBindingData = new YRCButtonBindingData();
		radItemMatrixButtonBindingData.setName("radMatrix");
		radItemMatrixButtonBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnB2BCatalogView");
		radItemMatrixButtonBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnB2BCatalogView");
		radItemMatrixButtonBindingData.setCheckedBinding("3");
		radMatrix.setData("YRCButtonBindingDefination",radItemMatrixButtonBindingData);

		YRCTableBindingData bindingData = new YRCTableBindingData();
		YRCTblClmBindingData colBindings[] = new YRCTblClmBindingData[1];
		colBindings[0] = new YRCTblClmBindingData();
		colBindings[0].setAttributeBinding("PONumber");
		colBindings[0].setColumnBinding("PO_Number");
		colBindings[0].setSortReqd(true);
		bindingData.setSortRequired(true);
		bindingData.setSourceBinding("XPXPOList:/POList/PO");
		bindingData.setName("itemPOList");
		bindingData.setTblClmBindings(colBindings);
		itemPOList.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION,
				bindingData);

		bindingData = new YRCTableBindingData();
		YRCTblClmBindingData colBindings1[] = new YRCTblClmBindingData[1];
		colBindings1[0] = new YRCTblClmBindingData();
		colBindings1[0].setAttributeBinding("Address");
		colBindings1[0].setColumnBinding("Email_Address");
		colBindings1[0].setSortReqd(true);
		bindingData.setSortRequired(true);
		bindingData.setSourceBinding("XPXEmailList:/EmailsList/Email");
		bindingData.setName("orderConfirmList");
		bindingData.setTblClmBindings(colBindings1);
		orderConfirmList.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION,bindingData);
		
		bindingData = new YRCTableBindingData();
		YRCTblClmBindingData colBindings2[] = new YRCTblClmBindingData[1];
		colBindings2[0] = new YRCTblClmBindingData();
		colBindings2[0].setAttributeBinding("InvoiceAddress");
		colBindings2[0].setColumnBinding("Email_Address1");
		colBindings2[0].setSortReqd(true);
		bindingData.setSortRequired(true);
		bindingData.setSourceBinding("XPXInvoiceEmailList:/InvoiceEmailList/InvoiceEmail");
		bindingData.setName("invoiceList");
		bindingData.setTblClmBindings(colBindings2);
		invoiceList.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION,bindingData);

		YRCButtonBindingData bbd = new YRCButtonBindingData();
		bbd.setName("btnUpdate");
		bbd.setActionHandlerEnabled(true);
		bbd.setActionId("com.xpedx.sterling.rcp.pca.userprofile.action.XPXUpdateUserProfileAction");
		btnUpdate.setData(YRCConstants.YRC_BUTTON_BINDING_DEFINATION, bbd);

		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@DayFaxNo");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/@DayFaxNo");
		tbd.setName("txtFaxNumber");
		txtFaxNumber.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);	
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/@CustomerContactKey");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/@CustomerContactKey");
		tbd.setName("txtDummy1");
		txtDummy1.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		txtDummy1.setVisible(false);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/@CustomerAdditionalAddressKey");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/@CustomerAdditionalAddressKey");
		tbd.setName("txtDummy2");
		txtDummy2.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		txtDummy2.setVisible(false);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@PersonInfoKey");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@PersonInfoKey");
		tbd.setName("txtDummy3");
		txtDummy3.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		txtDummy3.setVisible(false);
		
//		tbd = new YRCTextBindingData();
//		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnPrefCatalog");
//		tbd.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnPrefCatalog");
//		tbd.setName("txtPrefferedCatalog");
//		txtPrefferedCatalog.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
//		/*YRCButtonBindingData radMfgButtonBindingData = new YRCButtonBindingData();
//		radMfgButtonBindingData.setName("radExternal");
//		radMfgButtonBindingData
//				.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnUserType");
//		
//		 * radMfgButtonBindingData
//		 * .setTargetBinding("XPXResultOut:/CustomerContact/@UserType");
//		 
//		radMfgButtonBindingData.setCheckedBinding("EXTERNAL");
//		radExternal.setData("YRCButtonBindingDefination",
//				radMfgButtonBindingData);
//
//		YRCButtonBindingData radInternalButtonBindingData = new YRCButtonBindingData();
//		radInternalButtonBindingData.setName("radInternal");
//		radInternalButtonBindingData
//				.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnUserType");
//		
//		 * radInternalButtonBindingData
//		 * .setTargetBinding("XPXResultOut:/CustomerContact/@UserType");
//		 
//		radInternalButtonBindingData.setCheckedBinding("INTERNAL");
//		radInternal.setData("YRCButtonBindingDefination",
//				radInternalButtonBindingData);*/
//
//		/*cbd = new YRCComboBindingData();
//		cbd.setCodeBinding("@OptionType");
//		cbd.setDescriptionBinding("@OptionType");
//		cbd
//				.setListBinding("XPXRecieveOrderConfirmationEmails:/OptionList/Option");
//		cbd
//				.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnOrderEmailFormat");
//		cbd
//				.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnOrderEmailFormat");
//		cbd.setName("comboReceiveOrderConfirmEmails");
//		//comboReceiveOrderConfirmEmails.setData(
//		//		YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);*/
//		
//		/*Screen updates begin:Anjanee*/
//		tbd = new YRCTextBindingData();
//		tbd
//				.setSourceBinding("XPXCustomerContactIn:/CustomerContact//Extn/@ExtnInvoiceEMailID");
//		tbd.setTargetBinding("XPXResultOut:/CustomerContact//Extn/@ExtnInvoiceEMailID");
//		tbd.setName("txtInvoiceEmailId");
//		txtInvoiceEmailId.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);	
//
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnOrderConfEmailFlag");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnOrderConfEmailFlag");
		chkBoxBindingData.setName("chkReceiveOrderConfirmationEmail");
		chkReceiveOrderConfirmationEmail.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnOrderCancelEmailFlag");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnOrderCancelEmailFlag");
		chkBoxBindingData.setName("chkReceiveOrderCancellationEmail");
		chkReceiveOrderCancellationEmail.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		//Removed for the BugFix id-1004
		// TODO Order Update Email Flag is not present in the Customer Contact extensions.
/*		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnOrderUpdateEmailFlag");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnOrderUpdateEmailFlag");
		chkBoxBindingData.setName("chkReceiveOrderUpdateEmail");
		chkReceiveOrderUpdateEmail.setData("YRCButtonBindingDefination", chkBoxBindingData);*/
		
		//End of Removed for the BugFix id-1004
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnOrderShipEmailFlag");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnOrderShipEmailFlag");
		chkBoxBindingData.setName("chkReceiveOrderShipmentEmail");
		chkReceiveOrderShipmentEmail.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnBackOrderEmailFlag");
		chkBoxBindingData.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnBackOrderEmailFlag");
		chkBoxBindingData.setName("chkeceiveBackOrderEmail");
		chkeceiveBackOrderEmail.setData("YRCButtonBindingDefination", chkBoxBindingData);

//		/*chkBoxBindingData = new YRCButtonBindingData();
//		chkBoxBindingData.setCheckedBinding("Y");
//		chkBoxBindingData.setUnCheckedBinding("N");
//		chkBoxBindingData
//				.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnAcceptTAndCFlag");
//		chkBoxBindingData
//				.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnAcceptTAndCFlag");
//		chkBoxBindingData.setName("chkAcceptTAndC");
//		//chkAcceptTAndC.setData("YRCButtonBindingDefination",
//		//		chkBoxBindingData);*/
//
//		/*YRCButtonBindingData fullViewButtonBindingData = new YRCButtonBindingData();
//		fullViewButtonBindingData.setName("fullView");
//		fullViewButtonBindingData
//				.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnB2BCatalogView");
//		fullViewButtonBindingData
//				.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnB2BCatalogView");
//		fullViewButtonBindingData.setCheckedBinding("0");
//		//fullView.setData("YRCButtonBindingDefination",
//		//		fullViewButtonBindingData);
//		
//		YRCButtonBindingData condensedViewButtonBindingData = new YRCButtonBindingData();
//		condensedViewButtonBindingData.setName("condensedView");
//		condensedViewButtonBindingData
//				.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnB2BCatalogView");
//		condensedViewButtonBindingData
//				.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnB2BCatalogView");
//		condensedViewButtonBindingData.setCheckedBinding("1");
//		//condensedView.setData("YRCButtonBindingDefination",
//		//		condensedViewButtonBindingData);
//		
//		YRCButtonBindingData miniViewButtonBindingData = new YRCButtonBindingData();
//		miniViewButtonBindingData.setName("miniView");
//		miniViewButtonBindingData
//				.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnB2BCatalogView");
//		miniViewButtonBindingData
//				.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnB2BCatalogView");
//		miniViewButtonBindingData.setCheckedBinding("2");
//		//miniView.setData("YRCButtonBindingDefination",
//		//		miniViewButtonBindingData);
//		
//		YRCButtonBindingData paperGridViewButtonBindingData = new YRCButtonBindingData();
//		paperGridViewButtonBindingData.setName("paperGridView");
//		paperGridViewButtonBindingData
//				.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnB2BCatalogView");
//		paperGridViewButtonBindingData
//				.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnB2BCatalogView");
//		paperGridViewButtonBindingData.setCheckedBinding("3");
//		//paperGridView.setData("YRCButtonBindingDefination",
//		//		paperGridViewButtonBindingData);*/
//		/*Screen updates end:Anjanee*/
//		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("XPXCustomerContactIn:/CustomerContact/Extn/@ExtnDefaultShipTo");
		tbd.setTargetBinding("XPXResultOut:/CustomerContact/Extn/@ExtnDefaultShipTo");
		tbd.setName("txtCustomerID");
		txtCustomerID.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("DefaultShipToAddress:/ShipTo/@FirstName;DefaultShipToAddress:/ShipTo/@LastName;"
				+"DefaultShipToAddress:/ShipTo/@AddressLine1;DefaultShipToAddress:/ShipTo/@AddressLine2;"
				+"DefaultShipToAddress:/ShipTo/@AddressLine3;DefaultShipToAddress:/ShipTo/@City;"
				+"DefaultShipToAddress:/ShipTo/@State;DefaultShipToAddress:/ShipTo/@ZipCode;"
				+"DefaultShipToAddress:/ShipTo/@Country");
		tbd.setKey("xpedx_ShipToBillTo_address_key");
		tbd.setName("txtDefaultShipToAddress");
		txtDefaultShipToAddress.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
	}

	
	private void resetCustomPassword() {
		myBehavior.resetCustomPassword();
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

				HEIGHT += boundHeight + 5;
				if (WIDTH < boundWidth)
					WIDTH = boundWidth;
			}
			if (!isHScrollReqd)
				continue;
			WIDTH += boundWidth + 5;
			if (HEIGHT < boundHeight)
				HEIGHT = boundHeight;
		}
		scrPnl.setMinSize(WIDTH, HEIGHT);
//		scrPnl.setMinSize(1000, scrChild.getDisplay().getBounds().height);
//		scrPnl.setMinSize(1000, 1150);
		if (isVScrollReqd
				&& (selectedHeight < scrPnl.getOrigin().y || selectedHeight
						+ selectedPanelHeight > scrPnl.getSize().y
						+ scrPnl.getOrigin().y))
			scrPnl.setOrigin(0, selectedHeight);
		scrParent.layout(true, true);
	}

	public String getFormId() {
		return FORM_ID;
	}

	public Composite getRootPanel() {
		return pnlRoot;
	}

	public Object getInput() {
		Object inObject = null;
		org.eclipse.ui.part.WorkbenchPart currentPart = YRCDesktopUI
				.getCurrentPart();
		inObject = ((YRCEditorPart) currentPart).getEditorInput();
		return inObject;
	}

	public void showRootPanel(boolean show) {
		pnlRoot.setVisible(show);
	}

	public IYRCPanelHolder getPanelHolder() {
		// TODO Complete getPanelHolder
		return null;
	}

	public String getHelpId() {
		// TODO Complete getHelpId
		return null;
	}

	/* Added */
	public YRCWizardBehavior getWizardBehavior() {
		return wizBehavior;
	}

	/* Added */
	public void setWizBehavior(YRCWizardBehavior wizBehavior) {
		this.wizBehavior = wizBehavior;
	}

	public UserProfileInfoDetailsBehavior getBehavior() {
		return myBehavior;
	}
	//added by suneetha
	/*private void addDefaultShipToAddress(String fieldName) {
		myBehavior.addDefaultShipToAddress(fieldName);
	}*/
	
	/* Removed Additional EMails 
	private void createPnlAdditionalEmailsHolder() {
		//pnlAdditionalEmailsHolder = new Composite(pnlAdditionalEmails, 0);
		pnlAdditionalEmailsHolder = new Composite(pnlSiteSettings, 0);
		pnlAdditionalEmailsHolder.setBackgroundMode(0);
		pnlAdditionalEmailsHolder.setData("name", "pnlAdditionalEmailsHolder");
		GridData pnlAdditionalEmailsHolderlayoutData = new GridData();
		pnlAdditionalEmailsHolderlayoutData.horizontalAlignment = 4;
		pnlAdditionalEmailsHolderlayoutData.verticalAlignment = 4;
		pnlAdditionalEmailsHolderlayoutData.grabExcessHorizontalSpace = true;
		pnlAdditionalEmailsHolderlayoutData.grabExcessVerticalSpace = true;
		pnlAdditionalEmailsHolderlayoutData.widthHint = 300;
		pnlAdditionalEmailsHolder
				.setLayoutData(pnlAdditionalEmailsHolderlayoutData);
		GridLayout pnlAdditionalEmailsHolderlayout = new GridLayout(1, false);
		pnlAdditionalEmailsHolderlayout.verticalSpacing = 1;
		pnlAdditionalEmailsHolderlayout.marginHeight = 1;
		pnlAdditionalEmailsHolderlayout.marginWidth = 1;
		pnlAdditionalEmailsHolder.setLayout(pnlAdditionalEmailsHolderlayout);
		createAdditionalEmails();
	}

	private void createAdditionalEmails() {
		itemAdditionalEmails = new Table(pnlAdditionalEmailsHolder, 67586);
		itemAdditionalEmails.setHeaderVisible(true);
		itemAdditionalEmails.setLinesVisible(true);
		itemAdditionalEmails.setData("name", "itemAdditionalEmails");
		GridData itemAdditionalEmailslayoutData = new GridData();
		itemAdditionalEmailslayoutData.horizontalAlignment = 4;
		itemAdditionalEmailslayoutData.verticalAlignment = 4;
		itemAdditionalEmailslayoutData.grabExcessHorizontalSpace = true;
		itemAdditionalEmailslayoutData.grabExcessVerticalSpace = true;
		itemAdditionalEmails.setLayoutData(itemAdditionalEmailslayoutData);
		tblAdditionalEmailsId = new TableColumn(itemAdditionalEmails, 16384);
		tblAdditionalEmailsId.setWidth(133);
		tblAdditionalEmailsId.setResizable(true);
		tblAdditionalEmailsId.setMoveable(true);
	}

	private void createPnlAdditionalEmailsButtons() {
		//pnlAdditionalEmailsButtons = new Composite(pnlAdditionalEmails, 0);
		pnlAdditionalEmailsButtons = new Composite(pnlSiteSettings, 0);
		pnlAdditionalEmailsButtons.setBackgroundMode(0);
		pnlAdditionalEmailsButtons
				.setData("name", "pnlAdditionalEmailsButtons");
		GridData pnlAdditionalEmailsButtonslayoutData = new GridData();
		pnlAdditionalEmailsButtonslayoutData.horizontalAlignment = 4;
		pnlAdditionalEmailsButtonslayoutData.verticalAlignment = 4;
		pnlAdditionalEmailsButtons
				.setLayoutData(pnlAdditionalEmailsButtonslayoutData);
		GridLayout pnlAdditionalEmailsButtonslayout = new GridLayout(1, false);
		pnlAdditionalEmailsButtonslayout.marginHeight = 0;
		pnlAdditionalEmailsButtonslayout.marginWidth = 0;
		pnlAdditionalEmailsButtons.setLayout(pnlAdditionalEmailsButtonslayout);
		btnAssign = new Button(pnlAdditionalEmailsButtons, 8);
		GridData btnAdditionalEmailslayoutData = new GridData();
		btnAdditionalEmailslayoutData.horizontalAlignment = 16777216;
		btnAdditionalEmailslayoutData.verticalAlignment = 16777224;
		btnAdditionalEmailslayoutData.grabExcessHorizontalSpace = true;
		btnAdditionalEmailslayoutData.grabExcessVerticalSpace = true;
		btnAdditionalEmailslayoutData.heightHint = 25;
		btnAdditionalEmailslayoutData.widthHint = 90;
		btnAssign.setLayoutData(btnAdditionalEmailslayoutData);
		btnAssign.setText("Btn_AddEmail_Arrow");
		btnAssign.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addEmail();
			}
		});
		btnRemove = new Button(pnlAdditionalEmailsButtons, 8);
		GridData btnRemovelayoutData = new GridData();
		btnRemovelayoutData.horizontalAlignment = 16777216;
		btnRemovelayoutData.verticalAlignment = 1;
		btnRemovelayoutData.grabExcessVerticalSpace = true;
		btnRemovelayoutData.heightHint = 25;
		btnRemovelayoutData.widthHint = 90;
		btnRemove.setLayoutData(btnRemovelayoutData);
		btnRemove.setText("Btn_Remove_Emails");
		btnRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeEmailss();
			}
		});
	}
	* **************************** */

}
package com.xpedx.sterling.rcp.pca.user.internal.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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
import com.yantra.yfc.rcp.IYRCCellModifier;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.IYRCTableImageProvider;
import com.yantra.yfc.rcp.YRCButtonBindingData;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCScrolledCompositeListener;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;
import com.yantra.yfc.rcp.YRCXmlUtils;


public class XPXCreateInternalUserPanel extends Composite implements
		IYRCComposite {

	private Composite pnlRoot = null;
	
	private Composite pnlBody ;
	
	private ScrolledComposite bodyScrolledPanel = null;

	private XPXCreateInternalUserPanelBehavior myBehavior;

	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.user.internal.screen.XPXCreateInternalUserPanel";
	
	private Button btnUpdate;
	private Button refreshUserData;
	private Label lblNetworkId;
	private Text txtNetworkId;
	private Label lblEmployeeId;
	private Text txtEmployeeId;
	private Label lblName;
	private Text txtName;
	private Label lblEmailAddr;
	private Text txtEmailAddr;
	private Label lblCreateDate;
	private Text txtCreateDate;
	private Label lblLastLoginDate;
	private Text txtLastLoginDate;
	private Label lblUserRoles;
	private Label lblOrganization;
//	private Text txtOrganization;
	private Label lblAddress1;
	private Text txtAddress1;
	private Label lblAddress2;
	private Text txtAddress2;
	private Label lblAddress3;
	private Text txtAddress3;
	private Label lblCity;
	private Text txtCity;
	private Label lblState;
	private Text txtState;
	private Label lblCountry;
	private Text txtCountry;
	private Label lblPostalCode;
	private Text txtPostalCode;
	private Label lblPhoneNumber;
	private Text txtPhoneNumber;
	private Text txtLocaleCode;
	private Text txtZip4;
	private Text txtdummy;
	private Text txtDisplayUserId;
	private Text txtCompany;
	Table tblUserGroups;
	TableColumn tblClmnCheckBox;
	TableColumn tblclmUsergroupName;
	TableColumn tblclmUsergroupKey;
	private Element inputElement;
	private XPXInternalUserSearchListScreen invokerPage;
	private Label lblTeamAssignment;
	private Combo cmbDataSecurityGroupID;
	String strCustomerEmail;

	public XPXCreateInternalUserPanel(Composite parent, int style,Object inputObject) {
		super(parent, style);
		this.inputElement = ((YRCEditorInput) inputObject).getXml();
		initialize();
		setBindingForComponents();
		//myBehavior = new XPXCreateInternalUserPanelBehavior(this, FORM_ID,inputObject);
		myBehavior = new XPXCreateInternalUserPanelBehavior(this, FORM_ID);
		adjustScrollPnl(bodyScrolledPanel, pnlBody,
				getRootPanel(), true, true);

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
		createSecurityAdminHeaderPnl();
		createSecurityAdminPnl();
		createContactInfoHeaderPnl();
		createContactInfoPnl();
		
	}
	
	private void createContactInfoHeaderPnl(){
		Composite pnlContactInfoHeader = new Composite(pnlBody,SWT.NONE);
		pnlContactInfoHeader.setBackgroundMode(SWT.INHERIT_NONE);
		pnlContactInfoHeader.setData(YRCConstants.YRC_CONTROL_NAME, "pnlLocationHeader");
		GridLayout pnlContactInfoHeaderLayout = new GridLayout();
		pnlContactInfoHeaderLayout.horizontalSpacing = 1;
		pnlContactInfoHeaderLayout.verticalSpacing = 1;
		pnlContactInfoHeaderLayout.marginHeight = 2;
		pnlContactInfoHeaderLayout.marginWidth = 2;
		pnlContactInfoHeaderLayout.numColumns = 3;
		pnlContactInfoHeader.setLayout(pnlContactInfoHeaderLayout);
		
		GridData pnlContactInfoHeaderLayoutData = new GridData();
		pnlContactInfoHeaderLayoutData.horizontalAlignment = 4;
		pnlContactInfoHeaderLayoutData.grabExcessHorizontalSpace = true;
		pnlContactInfoHeader.setLayoutData(pnlContactInfoHeaderLayoutData);
		pnlContactInfoHeader.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		
		
		Label lblGeneralInfo = new Label(pnlContactInfoHeader, SWT.LEFT);
		GridData GenralInfoData = new GridData();
		GenralInfoData.grabExcessHorizontalSpace = true;
		lblGeneralInfo.setLayoutData(GenralInfoData);
		lblGeneralInfo.setText("Contact_Information");
		lblGeneralInfo.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
	}
	
	private void createContactInfoPnl(){
		Composite pnlContactInfo = new Composite(pnlBody,SWT.NONE);
		pnlContactInfo.setBackgroundMode(SWT.INHERIT_NONE);
		pnlContactInfo.setData(YRCConstants.YRC_CONTROL_NAME, "pnlContactInfoPnl");
		
		GridLayout pnlContactInfoLayout = new GridLayout(3,true);
		pnlContactInfoLayout.horizontalSpacing = 30;
		pnlContactInfoLayout.verticalSpacing = 1;
		pnlContactInfoLayout.marginHeight = 2;
		pnlContactInfoLayout.marginWidth = 2;
		pnlContactInfo.setLayout(pnlContactInfoLayout);
		
		GridData pnlContactInfoLayoutData = new GridData();
		pnlContactInfoLayoutData.horizontalAlignment = 4;
		pnlContactInfoLayoutData.grabExcessHorizontalSpace = true;
		pnlContactInfo.setLayoutData(pnlContactInfoLayoutData);
		
		
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
        
        lblOrganization = new Label(pnlContactInfo, SWT.LEFT);
        lblOrganization.setText("Organization");
        lblOrganization.setLayoutData(gridData2);
        lblOrganization.setData("name", "lblOrganization");
        txtCompany = new Text(pnlContactInfo, SWT.BORDER);
        txtCompany.setText(XPXConstants.DEFAULT_CSR_USER_COMPANY);
        txtCompany.setLayoutData(gridData3);
        txtCompany.setTextLimit(50);
        txtCompany.setData("name", "txtCompany");
        txtCompany.setEditable(false);
		
		lblAddress1 = new Label(pnlContactInfo, SWT.LEFT);
		lblAddress1.setText("Address1");
		lblAddress1.setLayoutData(gridData2);
		lblAddress1.setData("name", "lblAddress1");
		txtAddress1 = new Text(pnlContactInfo, SWT.BORDER);
		txtAddress1.setText("");
		txtAddress1.setLayoutData(gridData3);
		txtAddress1.setTextLimit(70);
		txtAddress1.setData("name", "txtAddress1");
		
		lblAddress2 = new Label(pnlContactInfo, SWT.LEFT);
		lblAddress2.setText("Address2");
		lblAddress2.setLayoutData(gridData2);
		lblAddress2.setData("name", "lblAddress2");
		txtAddress2 = new Text(pnlContactInfo, SWT.BORDER);
		txtAddress2.setText("");
		txtAddress2.setLayoutData(gridData3);
		txtAddress2.setTextLimit(70);
		txtAddress2.setData("name", "txtAddress2");
		
		lblAddress3 = new Label(pnlContactInfo, SWT.LEFT);
		lblAddress3.setText("Address3");
		lblAddress3.setLayoutData(gridData2);
		lblAddress3.setData("name", "lblAddress3");
		txtAddress3 = new Text(pnlContactInfo, SWT.BORDER);
		txtAddress3.setText("");
		txtAddress3.setLayoutData(gridData3);
		txtAddress3.setTextLimit(70);
		txtAddress3.setData("name", "txtAddress3");
		
		lblCity = new Label(pnlContactInfo, SWT.LEFT);
		lblCity.setText("City");
		lblCity.setLayoutData(gridData2);
		lblCity.setData("name", "lblCity");
		txtCity = new Text(pnlContactInfo, SWT.BORDER);
		txtCity.setText("");
		txtCity.setLayoutData(gridData3);
		txtCity.setTextLimit(35);
		txtCity.setData("name", "txtCity");
		
		lblState = new Label(pnlContactInfo, SWT.LEFT);
		lblState.setText("State/Province");
		lblState.setLayoutData(gridData2);
		lblState.setData("name", "lblState");
		txtState = new Text(pnlContactInfo, SWT.BORDER);
		txtState.setText("");
		txtState.setLayoutData(gridData3);
		txtState.setTextLimit(35);
		txtState.setData("name", "txtState");
		
		lblCountry = new Label(pnlContactInfo, SWT.LEFT);
		lblCountry.setText("Country");
		lblCountry.setLayoutData(gridData2);
		lblCountry.setData("name", "lblCountry");
		txtCountry = new Text(pnlContactInfo, SWT.BORDER);
		txtCountry.setText("");
		txtCountry.setLayoutData(gridData3);
		txtCountry.setTextLimit(40);
		txtCountry.setData("name", "txtCountry");
		
		lblPostalCode = new Label(pnlContactInfo, SWT.LEFT);
		lblPostalCode.setText("PostalCode+Zip4");
		lblPostalCode.setLayoutData(gridData2);
		lblPostalCode.setData("name", "lblPostalCode");
		
		Composite pnlZipText = new Composite(pnlContactInfo,SWT.NONE);
		
		
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
			
		
		txtPostalCode = new Text(pnlZipText, SWT.BORDER);
		txtPostalCode.setText("");
		txtPostalCode.setTextLimit(35);
		txtPostalCode.setLayoutData(gridData10);
		txtPostalCode.setData("name", "txtZipCode");
		txtZip4 =  new Text(pnlZipText, SWT.BORDER);
		txtZip4.setText("");
		txtZip4.setLayoutData(gridData11);
		txtZip4.setTextLimit(35);
		txtZip4.setData("name", "txtZip4");
		
		
		lblPhoneNumber = new Label(pnlContactInfo, SWT.LEFT);
		lblPhoneNumber.setText("Phone Number");
		lblPhoneNumber.setLayoutData(gridData2);
		lblPhoneNumber.setData("name", "lblPhoneNumber");
		txtPhoneNumber = new Text(pnlContactInfo, SWT.BORDER);
		txtPhoneNumber.setText("");
		txtPhoneNumber.setLayoutData(gridData3);
		txtPhoneNumber.setTextLimit(40);
		txtPhoneNumber.setData("name", "txtPhoneNumber");
		
//		txtOrganization = new Text(pnlContactInfo, SWT.BORDER);
//		txtOrganization.setText("");
//		txtOrganization.setLayoutData(gridData3);
//		txtOrganization.setTextLimit(50);
//		txtOrganization.setData("name", "txtOrganization");
//		txtOrganization.setVisible(false);
		
		
	}
		
	private void createSecurityAdminHeaderPnl(){
		Composite pnlSecurityAdminHeader = new Composite(pnlBody,SWT.NONE);
		pnlSecurityAdminHeader.setBackgroundMode(SWT.INHERIT_NONE);
		pnlSecurityAdminHeader.setData(YRCConstants.YRC_CONTROL_NAME, "pnlSecurityAdminHeader");
		GridLayout pnlSecurityAdminHeaderLayout = new GridLayout();
		pnlSecurityAdminHeaderLayout.horizontalSpacing = 1;
		pnlSecurityAdminHeaderLayout.verticalSpacing = 1;
		pnlSecurityAdminHeaderLayout.marginHeight = 2;
		pnlSecurityAdminHeaderLayout.marginWidth = 2;
		pnlSecurityAdminHeaderLayout.numColumns = 3;
		pnlSecurityAdminHeader.setLayout(pnlSecurityAdminHeaderLayout);
		
		GridData pnlSecurityAdminHeaderLayoutData = new GridData();
		pnlSecurityAdminHeaderLayoutData.horizontalAlignment = 4;
		pnlSecurityAdminHeaderLayoutData.grabExcessHorizontalSpace = true;
		pnlSecurityAdminHeader.setLayoutData(pnlSecurityAdminHeaderLayoutData);
		pnlSecurityAdminHeader.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		
		
		Label lblSecurityAdmin = new Label(pnlSecurityAdminHeader, SWT.LEFT);
		GridData GenralInfoData = new GridData();
		GenralInfoData.grabExcessHorizontalSpace = true;
		lblSecurityAdmin.setLayoutData(GenralInfoData);
		lblSecurityAdmin.setText("Security Admin");
		lblSecurityAdmin.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
	}
	
	private void createSecurityAdminPnl(){
		Composite pnlSecurityAdmin = new Composite(pnlBody,SWT.NONE);
//		pnlSecurityAdmin.setBackgroundMode(SWT.INHERIT_NONE);
		pnlSecurityAdmin.setData(YRCConstants.YRC_CONTROL_NAME, "pnlGeneralInfo");
		
		GridLayout pnlSecurityAdminLayout = new GridLayout(3,true);
		pnlSecurityAdminLayout.horizontalSpacing = 30;
		pnlSecurityAdminLayout.verticalSpacing = 1;
		pnlSecurityAdminLayout.marginHeight = 2;
		pnlSecurityAdminLayout.marginWidth = 2;
		pnlSecurityAdmin.setLayout(pnlSecurityAdminLayout);
		
		GridData pnlSecurityAdminLayoutData = new GridData();
		pnlSecurityAdminLayoutData.horizontalAlignment = 4;
		pnlSecurityAdminLayoutData.grabExcessHorizontalSpace = true;
		pnlSecurityAdmin.setLayoutData(pnlSecurityAdminLayoutData);
		
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
        lblNetworkId = new Label(pnlSecurityAdmin, SWT.LEFT);
        lblNetworkId.setText("Network_ID");
        lblNetworkId.setLayoutData(gridData2);
        lblNetworkId.setData("name", "lblNetworkId");
        txtNetworkId = new Text(pnlSecurityAdmin, SWT.BORDER);
		txtNetworkId.setText("");
		txtNetworkId.setLayoutData(gridData3);
		txtNetworkId.setTextLimit(50);
		txtNetworkId.setData("name", "txtNetworkId");
		
		//---changed by sukumar-tw for New LDAP service to fetch user details
		txtNetworkId.addFocusListener(new FocusAdapter() {
			String prevTxtNeworkVal="";
			public void focusGained(FocusEvent e) {
				prevTxtNeworkVal=txtNetworkId.getText();

			}

			public void focusLost(FocusEvent evt) {
				String currTxtNeworkVal=txtNetworkId.getText();

				if((txtNetworkId.getText()=="")||(currTxtNeworkVal.equals(prevTxtNeworkVal))){
				
					if((txtNetworkId.getText()=="")){
						myBehavior.clearData();
					}
				}
				else{
					myBehavior.loadData(); //--function used to fetch user details
				}
			}

			});
		
		//---End of changed by sukumar-tw for New LDAP service to fetch user details

		lblEmployeeId = new Label(pnlSecurityAdmin, SWT.LEFT);
		lblEmployeeId.setText("Employee_ID");
		lblEmployeeId.setLayoutData(gridData2);
		lblEmployeeId.setData("name", "lblEmployeeId");
		txtEmployeeId = new Text(pnlSecurityAdmin, SWT.BORDER);
		txtEmployeeId.setText("");
		txtEmployeeId.setLayoutData(gridData3);
		txtEmployeeId.setTextLimit(25);
		txtEmployeeId.setData("name", "txtEmployeeId");
		txtEmployeeId.setEditable(false);
		
		lblName = new Label(pnlSecurityAdmin, SWT.LEFT);
		lblName.setText("Name");
		lblName.setLayoutData(gridData2);
		lblName.setData("name", "lblName");
		txtName = new Text(pnlSecurityAdmin, SWT.BORDER);
		txtName.setText("");
		txtName.setLayoutData(gridData3);
		txtName.setTextLimit(50);
		txtName.setData("name", "txtName");
		txtName.setEditable(false);
		
		lblEmailAddr = new Label(pnlSecurityAdmin, SWT.LEFT);
		lblEmailAddr.setText("Email_Address");
		lblEmailAddr.setLayoutData(gridData2);
		lblEmailAddr.setData("name", "lblEmailAddr");
		txtEmailAddr = new Text(pnlSecurityAdmin, SWT.BORDER);
		txtEmailAddr.setText("");
		txtEmailAddr.setLayoutData(gridData3);
		txtEmailAddr.setTextLimit(150);
		txtEmailAddr.setData("name", "txtEmailAddr");
		txtEmailAddr.setEditable(false);
		
		lblCreateDate = new Label(pnlSecurityAdmin, SWT.LEFT);
		lblCreateDate.setText("Create_Date");
		lblCreateDate.setLayoutData(gridData2);
		lblCreateDate.setData("name", "lblCreateDate");
		txtCreateDate = new Text(pnlSecurityAdmin, SWT.BORDER);
		txtCreateDate.setText("");
		txtCreateDate.setLayoutData(gridData3);
		txtCreateDate.setTextLimit(50);
		txtCreateDate.setData("name", "txtCreateDate");
		txtCreateDate.setEditable(false);
		txtCreateDate.setEnabled(false);
		
		lblLastLoginDate = new Label(pnlSecurityAdmin, SWT.LEFT);
		lblLastLoginDate.setText("Last_Login_Date");
		lblLastLoginDate.setLayoutData(gridData2);
		lblLastLoginDate.setData("name", "lblLastLoginDate");
		txtLastLoginDate = new Text(pnlSecurityAdmin, SWT.BORDER);
		txtLastLoginDate.setText("");
		txtLastLoginDate.setLayoutData(gridData3);
		txtLastLoginDate.setTextLimit(50);
		txtLastLoginDate.setData("name", "txtLastLoginDate");
		txtLastLoginDate.setEditable(false);
		txtLastLoginDate.setEnabled(false);
		
		lblUserRoles = new Label(pnlSecurityAdmin, SWT.LEFT);
		lblUserRoles.setText("User_Roles");
		lblUserRoles.setLayoutData(gridData2);
		lblUserRoles.setData("name", "lblUserRoles");
		
		
		
		
		
		
		
		tblUserGroups = new Table(pnlSecurityAdmin, SWT.SINGLE | SWT.H_SCROLL 	| SWT.V_SCROLL |SWT.BORDER);
		tblUserGroups.setData(YRCConstants.YRC_CONTROL_NAME, "tblUserGroups");
//	    tblUserGroups.setHeaderVisible(true);
//		tblUserGroups.setLinesVisible(true);
		GridData tblUserGroupslayoutData = new GridData();
		tblUserGroupslayoutData.horizontalAlignment = SWT.BEGINNING;
		tblUserGroupslayoutData.verticalAlignment = 4;
		tblUserGroupslayoutData.verticalIndent = 10;
		tblUserGroupslayoutData.heightHint = 100;
		tblUserGroupslayoutData.widthHint = 200;
		tblUserGroupslayoutData.horizontalSpan = 2;
		tblUserGroups.setLayoutData(tblUserGroupslayoutData);
		tblClmnCheckBox = new TableColumn(tblUserGroups, SWT.CENTER);
		tblClmnCheckBox.setWidth(19);
		tblClmnCheckBox.setResizable(false);
		tblClmnCheckBox.setMoveable(false);
		tblclmUsergroupName = new TableColumn(tblUserGroups, SWT.LEFT);
		tblclmUsergroupName.setWidth(180);
		tblclmUsergroupName.setResizable(false);
		tblclmUsergroupName.setMoveable(true);
		tblclmUsergroupKey = new TableColumn(tblUserGroups, SWT.CENTER);
//		tblclmUsergroupKey.setWidth(100);
		tblclmUsergroupKey.setResizable(false);
		tblclmUsergroupKey.setMoveable(true);
	
		lblTeamAssignment = new Label(pnlSecurityAdmin, SWT.LEFT);
		lblTeamAssignment.setText("Team");
		lblTeamAssignment.setLayoutData(gridData2);
		lblTeamAssignment.setData("name", "lblTeamAssignment");
		cmbDataSecurityGroupID = new Combo(pnlSecurityAdmin, 8);
		cmbDataSecurityGroupID.setLayoutData(gridData3);
        
		
		// Dummy Controls to bind the default data 
		txtLocaleCode = new Text(pnlSecurityAdmin, SWT.BORDER);
		txtLocaleCode.setText("");
		txtLocaleCode.setLayoutData(gridData3);
		txtLocaleCode.setData("name", "txtLocaleCode");
		txtLocaleCode.setVisible(false);
		
		txtdummy = new Text(pnlSecurityAdmin, SWT.BORDER);
		txtdummy.setText("");
		txtdummy.setLayoutData(gridData3);
		txtdummy.setData("name", "txtdummy");
		txtdummy.setVisible(false);
		
		txtDisplayUserId = new Text(pnlSecurityAdmin, SWT.BORDER);
		txtDisplayUserId.setText("");
		txtDisplayUserId.setLayoutData(gridData3);
		txtDisplayUserId.setData("name", "txtDisplayUserId");
		txtDisplayUserId.setVisible(false);
	
	}
	
	
	
	
	private void createScrollCmpstForPnlBody() {
		// Scroll Panel Start
		GridData gridDataScroll = new GridData();
		gridDataScroll.horizontalAlignment = SWT.FILL;
		gridDataScroll.grabExcessHorizontalSpace = true;
		gridDataScroll.grabExcessVerticalSpace = true;
		gridDataScroll.verticalAlignment = SWT.FILL;
		
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
		bodyScrolledPanel.setData("name", "bodyScrolledPanel");
		
		

		// Scroll Panel End
	}
	
//EB-2690 As a call center user my name could change so I want admin users to have the option to update my name so that the correct name displays on changes made by me

	private void createMiscPnl() {
		refreshUserData = new Button(pnlRoot, 0);
		GridData btnRefreshUserlayoutData = new GridData();
		btnRefreshUserlayoutData.horizontalAlignment = 0;
		btnRefreshUserlayoutData.verticalAlignment = 2;
		btnRefreshUserlayoutData.heightHint = 27;
		btnRefreshUserlayoutData.widthHint = 100;
		refreshUserData.setLayoutData(btnRefreshUserlayoutData);
		refreshUserData.setText("Refresh User Data");		
		refreshUserData.setData("name", "refreshUserData");
		
		GridData gridData16 = new GridData();
		gridData16.horizontalAlignment = 3;
		gridData16.grabExcessHorizontalSpace = true;
		gridData16.horizontalSpan = 1;
		gridData16.horizontalIndent = 0;
		gridData16.verticalAlignment = 2;

		
		btnUpdate = new Button(pnlRoot, 0);
		btnUpdate.setText("Create");
		btnUpdate.setLayoutData(gridData16);
		btnUpdate.setData("name", "btnUpdate");
	}
	
	private void setBindingForComponents() {
		YRCTextBindingData tbd = null;
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/@Loginid");
		tbd.setTargetBinding("XPXInternalUser:/User/@Loginid");
		tbd.setName("txtNetworkId");
		txtNetworkId.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/Extn/@ExtnEmployeeId");
		tbd.setTargetBinding("XPXInternalUser:/User/Extn/@ExtnEmployeeId");
		tbd.setName("txtEmployeeId");
		txtEmployeeId.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/@Username");
		tbd.setTargetBinding("XPXInternalUser:/User/@Username");
		tbd.setName("txtName");
		txtName.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/ContactPersonInfo/@EMailID");
		tbd.setTargetBinding("XPXInternalUser:/User/ContactPersonInfo/@EMailID");
		tbd.setName("txtEmailAddr");
		txtEmailAddr.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/@Createts");
		tbd.setTargetBinding("XPXInternalUser:/User/@Createts");
		tbd.setName("txtCreateDate");
		txtCreateDate.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/Extn/@ExtnLastLoginDate");
		tbd.setTargetBinding("XPXInternalUser:/User/Extn/@ExtnLastLoginDate");
		tbd.setName("txtLastLoginDate");
		txtLastLoginDate.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/ContactPersonInfo/@Company");
		tbd.setTargetBinding("XPXInternalUser:/User/ContactPersonInfo/@Company");
		tbd.setName("txtCompany");
		txtCompany.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		
		YRCTableBindingData tblUserGroupsbd = new YRCTableBindingData();
		tblUserGroupsbd.setName("tblUserGroups");
		tblUserGroupsbd.setSourceBinding("getUserGroupLists_output:/UserGroupList/UserGroup");
		tblUserGroupsbd.setTargetBinding("XPXInternalUser:/User/UserGroupLists");
	
		
		YRCTblClmBindingData tblUserGroupsClmBd[] = new YRCTblClmBindingData[3];
		String[] tblUserGroupsEditors = new String[3];
		int tblUserGroupsCounter=0;
		
		tblUserGroupsClmBd[tblUserGroupsCounter] = new YRCTblClmBindingData();
		tblUserGroupsClmBd[tblUserGroupsCounter].setName("tblClmnCheckBox");
		tblUserGroupsClmBd[tblUserGroupsCounter].setKeyMatchBinding("@UsergroupKey", "@UsergroupKey");	
		tblUserGroupsClmBd[tblUserGroupsCounter].setSourceBinding("getUserGroupLists_output:/UserGroupList");
		tblUserGroupsClmBd[tblUserGroupsCounter].setAttributeBinding("@Checked");		
		tblUserGroupsClmBd[tblUserGroupsCounter].setCheckedBinding("Y");
		tblUserGroupsClmBd[tblUserGroupsCounter].setUnCheckedBinding("N");
        tblUserGroupsEditors[tblUserGroupsCounter] = YRCConstants.YRC_CHECK_BOX_CELL_EDITOR;
		tblUserGroupsClmBd[tblUserGroupsCounter].setTargetAttributeBinding("/UserGroupList/@Checked");
		tblUserGroupsCounter++;
		
		tblUserGroupsClmBd[tblUserGroupsCounter] = new YRCTblClmBindingData();
		tblUserGroupsClmBd[tblUserGroupsCounter].setName("tblclmUsergroupName");
		tblUserGroupsClmBd[tblUserGroupsCounter].setAttributeBinding("@UsergroupName");		
		tblUserGroupsClmBd[tblUserGroupsCounter].setColumnBinding("Group_Name");
		tblUserGroupsCounter++;
		
		tblUserGroupsClmBd[tblUserGroupsCounter] = new YRCTblClmBindingData();
		tblUserGroupsClmBd[tblUserGroupsCounter].setName("tblclmUsergroupKey");
		tblUserGroupsClmBd[tblUserGroupsCounter].setAttributeBinding("@UsergroupKey");		
		tblUserGroupsClmBd[tblUserGroupsCounter].setColumnBinding("UsergroupKey");
		tblUserGroupsClmBd[tblUserGroupsCounter].setVisible(false);
		tblUserGroupsCounter++;
		
		tblUserGroupsbd.setTblClmBindings(tblUserGroupsClmBd) ;
        tblUserGroupsbd.setDefaultSort(false);
		tblUserGroupsbd.setCopySourceModelAttributes(true);
		tblUserGroupsbd.setImageProvider(new IYRCTableImageProvider(){
			public String getImageThemeForColumn(Object element, int columnIndex) {
				if(columnIndex == 0){
					Element checked = (Element) element;
					String sAlreadyChecked = checked.getAttribute("Checked");
					return getCheckBoxImage(checked, sAlreadyChecked);
				}
				return "";
			}
		});
		
		tblUserGroupsbd.setCellTypes(tblUserGroupsEditors);
		tblUserGroupsbd.setCellModifierRequired(true);
		tblUserGroupsbd.setCellModifier(new IYRCCellModifier(){
			
			protected int allowModifiedValue(String property, String value, Element element) {
				return 0;
			}

			public Object getValue(Object object, String property) {
				return super.getValue(object, property);
			}

			protected boolean allowModify(String property, String value, Element element) {
				return true;
			}
			protected String getModifiedValue(String property, String value, Element element) {
				if (property.equals("@Checked") && (YRCPlatformUI.isVoid(element.getAttribute("Checked")) || element.getAttribute("Checked").equals("N")))	{
					element.setAttribute("Checked","Y");
				}else if (property.equals("@Checked")){
					element.setAttribute("Checked","N");
				}
				return value;
			}});
		//}

		tblUserGroupsbd.setKeyNavigationRequired(true);
		tblUserGroups.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, tblUserGroupsbd);
		YRCComboBindingData cbd = new YRCComboBindingData();
		cbd.setName("cmbDataSecurityGroupID");
        cbd.setSourceBinding("/User/@DataSecurityGroupId");
        cbd.setTargetBinding("XPXInternalUser:/User/@DataSecurityGroupId");
        cbd.setCodeBinding("TeamId");
        cbd.setListBinding("getTeamList_output:TeamList/Team");
        cbd.setDescriptionBinding("Description");
		cmbDataSecurityGroupID.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		
//		tbd = new YRCTextBindingData();
//		tbd.setSourceBinding("/User/@OrganizationKey");
//		tbd.setTargetBinding("XPXInternalUser:/User/@OrganizationKey");
//		tbd.setName("txtOrganization");
//		txtOrganization.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/ContactPersonInfo/@AddressLine1");
		tbd.setTargetBinding("XPXInternalUser:/User/ContactPersonInfo/@AddressLine1");
		tbd.setName("txtAddress1");
		txtAddress1.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/ContactPersonInfo/@AddressLine2");
		tbd.setTargetBinding("XPXInternalUser:/User/ContactPersonInfo/@AddressLine2");
		tbd.setName("txtAddress2");
		txtAddress2.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/ContactPersonInfo/@AddressLine3");
		tbd.setTargetBinding("XPXInternalUser:/User/ContactPersonInfo/@AddressLine3");
		tbd.setName("txtAddress3");
		txtAddress3.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/ContactPersonInfo/@City");
		tbd.setTargetBinding("XPXInternalUser:/User/ContactPersonInfo/@City");
		tbd.setName("txtCity");
		txtCity.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/ContactPersonInfo/@State");
		tbd.setTargetBinding("XPXInternalUser:/User/ContactPersonInfo/@State");
		tbd.setName("txtState");
		txtState.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/ContactPersonInfo/@Country");
		tbd.setTargetBinding("XPXInternalUser:/User/ContactPersonInfo/@Country");
		tbd.setName("txtCountry");
		txtCountry.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/ContactPersonInfo/@ZipCode");
		tbd.setTargetBinding("XPXInternalUser:/User/ContactPersonInfo/@ZipCode");
		tbd.setName("txtPostalCode");
		txtPostalCode.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/ContactPersonInfo/Extn/@ExtnZip4");
		tbd.setTargetBinding("XPXInternalUser:/User/ContactPersonInfo/Extn/@ExtnZip4");
		tbd.setName("txtZip4");
		txtZip4.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/ContactPersonInfo/@DayPhone");
		tbd.setTargetBinding("XPXInternalUser:/User/ContactPersonInfo/@DayPhone");
		tbd.setName("txtPhoneNumber");
		txtPhoneNumber.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		
		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("/User/@UserKey");
		tbd.setTargetBinding("XPXInternalUser:/User/@UserKey");
		tbd.setName("txtdummy");
		txtdummy.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		
//		tbd = new YRCTextBindingData();
//		tbd.setSourceBinding("/User/@Loginid");
//		tbd.setTargetBinding("XPXInternalUser:/User/@Loginid");
//		tbd.setName("txtDisplayUserId");
//		txtDisplayUserId.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		YRCButtonBindingData bbd = new YRCButtonBindingData();
		bbd.setName("btnUpdate");
		btnUpdate.setData(YRCConstants.YRC_BUTTON_BINDING_DEFINATION, bbd);
		
		btnUpdate.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.create();
				
							
			}
		});
		
		

		

				
		
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
		scrPnl.setMinSize(1000, 1150);
		if (isVScrollReqd
				&& (selectedHeight < scrPnl.getOrigin().y || selectedHeight
						+ selectedPanelHeight > scrPnl.getSize().y
						+ scrPnl.getOrigin().y))
			scrPnl.setOrigin(0, selectedHeight);
		scrParent.layout(true, true);
	}

	
	public Element getPageInput() {
		return inputElement;
	}
	
	public void setPageInput(Element elePageInput) {
		this.inputElement = elePageInput;
	}
	
	public String getFormId() {
		// TODO Auto-generated method stub
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
	
	public static String getCheckBoxImage(Element eUserGroup, String sAlreadyChecked) {
		if(YRCXmlUtils.getBooleanAttribute(eUserGroup,"Updated")){
			if(sAlreadyChecked != null && sAlreadyChecked.equals("Y")){
				return "TableChecked";
			}else {
				return "TableUnchecked";
			}
		}else{
			if(sAlreadyChecked != null && sAlreadyChecked.equals("Y")){
				return "TableChecked";
			}else {
				return "TableUnchecked";
			}
		}
	}
	
	public XPXInternalUserSearchListScreen getInvokerPage() {
		return invokerPage;
	}
	
}

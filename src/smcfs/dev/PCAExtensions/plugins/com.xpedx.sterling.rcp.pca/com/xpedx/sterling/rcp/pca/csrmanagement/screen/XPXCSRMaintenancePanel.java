package com.xpedx.sterling.rcp.pca.csrmanagement.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.IYRCTableImageProvider;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;


public class XPXCSRMaintenancePanel  extends Composite  implements IYRCComposite {

    public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.csrmanagement.screen.XPXCSRMaintenancePanel";
    private XPXCSRMaintenancePanelBehavior myBehavior;

	private Composite pnlRoot = null;
	private Group grpSearchFields = null;
	private Composite pnlButtons = null;
	private Button btnSearch = null;
	private Button btnReset = null;
	private Group pnlSearchResults = null;
	private Composite pnlTitleHolder = null;
	private Label lblTitleIcon = null;
	private Composite pnlTableHolder = null;
	Table tblSearchResults = null;

    //newly added ones
	public Combo cmbEnterprise;
	private Label lblEnterprise;
	private Label lblOldCSR;
	public Combo cmbCSRs;
	private Label lblNewCSR;
	public Combo cmbNewCSRs;
	private Button btnUpdate = null;	
	private Group grpSearchFields1 = null;
	private Group grpAssignmentUpdateFields = null;
	public Combo cmbCSROptions;
	private Label lblCSROptions;


	public XPXCSRMaintenancePanel(Composite parent, int style, Object inputObject) {
		super(parent, style);

		initialize();
        setBindingForComponents();
        myBehavior = new XPXCSRMaintenancePanelBehavior(this, FORM_ID,inputObject);
	}

	public String getFormId() {
		return FORM_ID;
	}

	public Composite getRootPanel() {
		return pnlRoot;
	}

	private void initialize() {
		createRootPanel();
		this.setLayout(new FillLayout());
		setSize(new org.eclipse.swt.graphics.Point(800,600));
	}

	private void createRootPanel() {
		pnlRoot = new Composite(this, SWT.NONE);
		createSearchFieldsGroup();
		createPnlSearchResults();
        pnlRoot.setLayout(new GridLayout());
	}

	private void createPnlSearchResults() {
		GridData pnlSearchResultsGD = new org.eclipse.swt.layout.GridData();
		GridLayout pnlSearchResultsGL = new GridLayout();
		pnlSearchResults = new Group(pnlRoot, SWT.NONE);		   
		createPnlTitleHolder();
		createPnlTableHolder();
		pnlSearchResults.setLayout(pnlSearchResultsGL);
		pnlSearchResults.setLayoutData(pnlSearchResultsGD);
		pnlSearchResultsGL.numColumns = 2;
		pnlSearchResultsGL.horizontalSpacing = 0;
		pnlSearchResultsGL.verticalSpacing = 0;
		pnlSearchResultsGL.marginWidth = 0;
		pnlSearchResultsGL.marginHeight = 0;
		pnlSearchResultsGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlSearchResultsGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlSearchResultsGD.grabExcessHorizontalSpace = true;
		pnlSearchResultsGD.grabExcessVerticalSpace = true;
	}

	private void createPnlTableHolder() {
		GridLayout gridLayout8 = new GridLayout();
		GridData pnlTableHolderGD = new org.eclipse.swt.layout.GridData();
		pnlTableHolder = new Composite(pnlSearchResults, SWT.NONE);		   
		createTblSearchResults();
		pnlTableHolder.setLayoutData(pnlTableHolderGD);
		pnlTableHolder.setLayout(gridLayout8);
		pnlTableHolderGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlTableHolderGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlTableHolderGD.grabExcessHorizontalSpace = true;
		pnlTableHolderGD.grabExcessVerticalSpace = true;
		pnlTableHolderGD.horizontalSpan = 2;
		gridLayout8.horizontalSpacing = 0;
		gridLayout8.verticalSpacing = 0;
		gridLayout8.marginWidth = 0;
		gridLayout8.marginHeight = 0;
	}
	
	private void createTblSearchResults() {
		GridData tblSearchResultsGD = new org.eclipse.swt.layout.GridData();
		tblSearchResults = new Table(pnlTableHolder,SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		tblSearchResults.setData("name", "tblSearchResults");	
		tblSearchResults.setEnabled(true);

		
		TableColumn clmCheckBox = new TableColumn(tblSearchResults, SWT.CHECK);
		clmCheckBox.setToolTipText("Check_This");
		clmCheckBox.setWidth(80);
		clmCheckBox.setResizable(false);
		clmCheckBox.setMoveable(false);
		
		TableColumn clmCustomer = new TableColumn(tblSearchResults, SWT.NONE);
		clmCustomer.setText("Customer");
		clmCustomer.setWidth(100);
		
		TableColumn clmStorefrontCode = new TableColumn(tblSearchResults, SWT.NONE);
		clmStorefrontCode.setText("CSR1");
		clmStorefrontCode.setWidth(50);
		
		TableColumn clmDivision = new TableColumn(tblSearchResults, SWT.NONE);
		clmDivision.setText("CSR2");
		clmDivision.setWidth(50);		
			
		tblSearchResultsGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblSearchResultsGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblSearchResultsGD.grabExcessHorizontalSpace = true;
		tblSearchResultsGD.grabExcessVerticalSpace = true;
		tblSearchResultsGD.horizontalSpan = 2;
		tblSearchResults.setLayoutData(tblSearchResultsGD);
		tblSearchResults.setHeaderVisible(true);
		tblSearchResults.setLinesVisible(true);
	}
	
	private void createPnlTitleHolder() {
		GridLayout pnlTitleHolderGL = new GridLayout();
		GridData pnlTitleHolderGD = new org.eclipse.swt.layout.GridData();
		pnlTitleHolder = new Composite(pnlSearchResults, SWT.NONE);		   
		lblTitleIcon = new Label(pnlTitleHolder, SWT.NONE);
		
		Image img = YRCPlatformUI.getImage("ListTitle");
		
		
		if (img != null) {
			img.setBackground(YRCPlatformUI.getBackGroundColor("Composite"));
			lblTitleIcon.setImage(img);
		}
		else {
			lblTitleIcon.setText("");
		}
		
		pnlTitleHolder.setLayoutData(pnlTitleHolderGD);
		pnlTitleHolder.setLayout(pnlTitleHolderGL);
		pnlTitleHolderGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;

		pnlTitleHolderGD.grabExcessHorizontalSpace = true;
		
		pnlTitleHolderGL.numColumns = 2;
		pnlTitleHolderGL.horizontalSpacing = 5;
		pnlTitleHolderGL.verticalSpacing = 0;
		pnlTitleHolderGL.marginWidth = 0;
		pnlTitleHolderGL.marginHeight = 0;
		
		XPXUtils.addGradientPanelHeader(pnlTitleHolder, "CSR_CUSTOMER_LIST", true);
	}
		
	private void createSearchFieldsGroup() {
		GridLayout srchGroupLayout = new GridLayout();
		
		GridData gdSrchGroup = new GridData();
		grpSearchFields1 = new Group(pnlRoot, SWT.NONE);
		
		gdSrchGroup.grabExcessHorizontalSpace = true;
		grpSearchFields1.setLayoutData(gdSrchGroup);
		grpSearchFields1.setLayout(srchGroupLayout);
		srchGroupLayout.numColumns = 4;
		gdSrchGroup.verticalAlignment = SWT.FILL;
		gdSrchGroup.grabExcessHorizontalSpace = true;
		gdSrchGroup.heightHint = 120;
		
		GridLayout grpSrchLayout = new GridLayout();
		GridData gdSrchGroup1 = new GridData();
		grpSearchFields = new Group(grpSearchFields1, SWT.NONE);
		grpSearchFields.setText("CSR_SELECT_CRITERIA");
		grpSearchFields.setData("yrc:customType","XPXGroupStyle");
		gdSrchGroup1.verticalAlignment = SWT.FILL;
		gdSrchGroup1.grabExcessHorizontalSpace = true;
		gdSrchGroup1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gdSrchGroup1.heightHint = 100;
		grpSearchFields.setLayoutData(gdSrchGroup1);
		grpSearchFields.setLayout(grpSrchLayout);
		grpSrchLayout.numColumns = 2;
		
		lblEnterprise = new Label(grpSearchFields, SWT.LEFT);
		GridData lblEnterpriselayoutData = new GridData();
		lblEnterpriselayoutData.horizontalAlignment = 16777224;
		lblEnterpriselayoutData.verticalAlignment = 16777216;
		lblEnterprise.setLayoutData(lblEnterpriselayoutData);
		lblEnterprise.setText("Enterprise");	
		
		cmbEnterprise = new Combo(grpSearchFields,SWT.READ_ONLY);
		GridData cmbEnterpriseLayoutData = new GridData();
		cmbEnterpriseLayoutData.widthHint = 170;
		cmbEnterpriseLayoutData.grabExcessHorizontalSpace = true;
		cmbEnterpriseLayoutData.horizontalAlignment = 2;
		cmbEnterprise.setLayoutData(cmbEnterpriseLayoutData);
		
		lblOldCSR = new Label(grpSearchFields, SWT.LEFT);
		GridData lblOldCSRlayoutData = new GridData();
		lblOldCSRlayoutData.horizontalAlignment = 16777224;
		lblOldCSRlayoutData.verticalAlignment = 16777216;
		lblOldCSR.setLayoutData(lblOldCSRlayoutData);
		lblOldCSR.setText("OLD_CSR");
		
		cmbCSRs = new Combo(grpSearchFields,SWT.READ_ONLY);
		GridData cmbCSRsLayoutData = new GridData();
		cmbCSRsLayoutData.widthHint = 170;
		cmbCSRsLayoutData.grabExcessHorizontalSpace = true;
		cmbCSRsLayoutData.horizontalAlignment = 2;
		cmbCSRs.setLayoutData(cmbCSRsLayoutData);		
		createButtonComposite();
		
		
		GridLayout grpAssignmentUpdateLayout = new GridLayout();
		GridData gdAssignmentUpdateGroup = new GridData();
		grpAssignmentUpdateFields = new Group(grpSearchFields1, SWT.NONE);
		grpAssignmentUpdateFields.setText("CSR_ASSIGNMNET_UPDATE");
		grpAssignmentUpdateFields.setData("yrc:customType","XPXGroupStyle");
		gdAssignmentUpdateGroup.verticalAlignment = SWT.FILL;
		gdAssignmentUpdateGroup.grabExcessHorizontalSpace = true;
		gdAssignmentUpdateGroup.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		grpAssignmentUpdateFields.setLayoutData(gdSrchGroup1);
		grpAssignmentUpdateFields.setLayout(grpSrchLayout);
		grpAssignmentUpdateLayout.numColumns = 2;
		
		lblNewCSR = new Label(grpAssignmentUpdateFields, SWT.LEFT);
		GridData lblNewCSRLayoutData = new GridData();
		lblNewCSRLayoutData.horizontalAlignment = 16777224;
		lblNewCSRLayoutData.verticalAlignment = 16777216;
		lblNewCSR.setLayoutData(lblNewCSRLayoutData);
		lblNewCSR.setText("NEW_CSR");
		
		cmbNewCSRs = new Combo(grpAssignmentUpdateFields,SWT.READ_ONLY);
		GridData cmbNewCSRsLayoutData = new GridData();
		cmbNewCSRsLayoutData.widthHint = 170;
		cmbNewCSRsLayoutData.grabExcessHorizontalSpace = true;
		cmbNewCSRsLayoutData.horizontalAlignment = 2;
		cmbNewCSRs.setLayoutData(cmbNewCSRsLayoutData);	
		
		lblCSROptions = new Label(grpAssignmentUpdateFields, SWT.LEFT);
		GridData lblCSROptionsLayoutData = new GridData();
		lblCSROptionsLayoutData.horizontalAlignment = 16777224;
		lblCSROptionsLayoutData.verticalAlignment = 16777216;
		lblCSROptions.setLayoutData(lblCSROptionsLayoutData);
		lblCSROptions.setText("CSR_UPDATE_FOR");	
		
		cmbCSROptions = new Combo(grpAssignmentUpdateFields,SWT.READ_ONLY);
		GridData cmbCSROptionsLayoutData = new GridData();
		cmbCSROptionsLayoutData.widthHint = 170;
		cmbCSROptionsLayoutData.grabExcessHorizontalSpace = true;
		cmbCSROptionsLayoutData.horizontalAlignment = 2;
		cmbCSROptions.setLayoutData(cmbCSROptionsLayoutData);
		cmbCSROptions.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				tblSearchResults.setFocus();
			}
		});
		createUpdateButtonComposite();

	}

	private void createButtonComposite() {
		GridLayout btnPanelLayout = new GridLayout();
		GridData btnPanelLayoutData = new GridData();
		pnlButtons = new Composite(grpSearchFields, SWT.NONE);		   
		btnPanelLayoutData.grabExcessHorizontalSpace = true;
		btnPanelLayoutData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		btnPanelLayoutData.horizontalSpan = 4;
		pnlButtons.setLayoutData(btnPanelLayoutData);
		pnlButtons.setLayout(btnPanelLayout);
		btnPanelLayout.numColumns = 3;
		// button creation
		btnSearch = new Button(pnlButtons, SWT.NONE);
		btnSearch.setText("Search");
		
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		btnSearch.setLayoutData(gridData);
		btnSearch.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.search();
				tblSearchResults.setFocus();
			}
		});
		
		btnReset = new Button(pnlButtons, SWT.NONE);
		btnReset.setText("Reset");
		btnReset.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.reset();
			}
		});

	}
	private void createUpdateButtonComposite() {
		GridLayout btnPanelLayout = new GridLayout();
		GridData btnPanelLayoutData = new GridData();
		pnlButtons = new Composite(grpAssignmentUpdateFields, SWT.NONE);		   
		btnPanelLayoutData.grabExcessHorizontalSpace = true;
		btnPanelLayoutData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		btnPanelLayoutData.horizontalSpan = 4;
		pnlButtons.setLayoutData(btnPanelLayoutData);
		pnlButtons.setLayout(btnPanelLayout);
		btnPanelLayout.numColumns = 3;
		// button creation
		btnUpdate = new Button(pnlButtons, SWT.NONE);
		btnUpdate.setText("Update");
		
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		btnUpdate.setLayoutData(gridData);
		btnUpdate.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.update();
				tblSearchResults.setFocus();
			}
		});
	}
	private void setBindingForComponents() {
	    setBindingForSearchCriteria();
	    setBindingForSearchResults();
    }
	
    private void setBindingForSearchCriteria() {
    	YRCComboBindingData cbd = null;
    	YRCDesktopUI.getCurrentPart().showBusy(true);
    	cbd = new YRCComboBindingData();
		cbd.setName("Enterprise");
	    cbd.setSourceBinding("SearchCriteria:/XPXCSRS/@OrganizationCode");
        cbd.setTargetBinding("SearchCriteria:/XPXCSRS/@OrganizationCode");
        cbd.setCodeBinding("OrganizationCode");
        cbd.setListBinding("OrgList:OrganizationList/Organization");
        cbd.setDescriptionBinding("OrganizationCode");
        cmbEnterprise.setData("YRCComboBindingDefination", cbd);
        
        cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@UserKey");
		cbd.setDescriptionBinding("@Loginid");
		cbd.setListBinding("XPXGetUserList:/UserList/User");
		cbd.setSourceBinding("SearchCriteria:/XPXCSRS/@ExtnECSR1Key");
		cbd.setTargetBinding("SearchCriteria:/XPXCSRS/@ExtnECSR1Key");		
		cbd.setName("CSR");
		cmbCSRs.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		
		cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@UserKey");
		cbd.setDescriptionBinding("@Loginid");
		cbd.setListBinding("XPXGetUserList:/UserList/User");
		cbd.setSourceBinding("update:/XPXCSRS/@ExtnECSR2Key");
		cbd.setTargetBinding("update:/XPXCSRS/@ExtnECSR2Key");
		cbd.setName("cmbNewCSRs");
		cmbNewCSRs.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		
		cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@CodeValue");
        cbd.setDescriptionBinding("@CodeShortDescription");
        cbd.setListBinding("CSROptions:/Code");
		cbd.setSourceBinding("update:/XPXCSRS/@CSROption");
		cbd.setTargetBinding("update:/XPXCSRS/@CSROption");
		cbd.setName("comboShipComplete");
		cmbCSROptions.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		YRCDesktopUI.getCurrentPart().showBusy(false);
    }
    
    private void setBindingForSearchResults() {
    	YRCDesktopUI.getCurrentPart().showBusy(true);
    	YRCTableBindingData bindingData = new YRCTableBindingData();
        YRCTblClmBindingData colBindings[] = new YRCTblClmBindingData[4];
        bindingData.setTargetBinding("replace_input:/CustomerList");
        bindingData.setSourceBinding("CustomerList:/Customer");        
        bindingData.setName("tblSearchResults");   
       		
        colBindings[1] = new YRCTblClmBindingData();
        colBindings[1].setAttributeBinding("@CustomerID");
        
        colBindings[1].setColumnBinding("Customer");
  
        
        colBindings[2] = new YRCTblClmBindingData();
        colBindings[2].setAttributeBinding("Extn/@ExtnECSR");
        colBindings[2].setColumnBinding("CSR1");
        
        
        colBindings[3] = new YRCTblClmBindingData();
        colBindings[3].setAttributeBinding("Extn/@ExtnECSR2");
        colBindings[3].setColumnBinding("CSR2");

        
		colBindings[0] = new YRCTblClmBindingData();
        colBindings[0].setName("clmCheckBox");
        colBindings[0].setAttributeBinding("@Replace");
        colBindings[0].setColumnBinding("Replace");
        colBindings[0].setCheckedBinding("Y");
        colBindings[0].setUnCheckedBinding("N");
        colBindings[0].setFilterReqd(false);
        colBindings[0].setTargetAttributeBinding("Customer/@Checked");    
   
        
        bindingData.setImageProvider(new IYRCTableImageProvider() {
			public String getImageThemeForColumn(Object element, int columnIndex) {
				Element orderline = (Element) element;
				String sAlreadyChecked = orderline.getAttribute("Replace"); 
				if (columnIndex == 0) {
					if (YRCPlatformUI.equals(sAlreadyChecked, "Y")) {
						return "TableCheckboxCheckedImageLarge";
					} else if (YRCPlatformUI.equals(sAlreadyChecked, "N") || YRCPlatformUI.equals(sAlreadyChecked, "")) {
						return "TableCheckboxUnCheckedImageLarge";
					}
				}
				return null;
			}
		});

        
        String[] editors = new String[tblSearchResults.getColumnCount()];
		editors[0] = YRCConstants.YRC_CHECK_BOX_CELL_EDITOR;
		
		
		 bindingData.setTblClmBindings(colBindings);
		 bindingData.setCellTypes(editors);
	     bindingData.setCellModifierRequired(true);
	     bindingData.setKeyNavigationRequired(true);

	        
        tblSearchResults.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, bindingData);
        YRCDesktopUI.getCurrentPart().showBusy(false);
    }
    
   
    public IYRCPanelHolder getPanelHolder() {
        return null;
    }

	public String getHelpId() {
		return null;
	}

	public XPXCSRMaintenancePanelBehavior getMyBehavior() {
		return myBehavior;
	}

}

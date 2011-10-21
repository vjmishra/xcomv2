package com.xpedx.sterling.rcp.pca.user.internal.screen;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPaginatedSearchAndListComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCButtonBindingData;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPaginationData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;


public class XPXInternalUserSearchListScreen extends Composite  implements IYRCComposite,IYRCPaginatedSearchAndListComposite {

    public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.user.internal.screen.XPXInternalUserSearchListScreen";
    private XPXInternalUserSearchListBehaviour myBehavior;

    private Composite pnlSearchTop;
    private Composite pnlSearchCriteriaTitle;
    private Composite pnlSearchCriteria;
    private Composite pnlBasicSearchCriteriaBody;
    private Composite cmpstSearchResultsAndControls;
    private Composite resultsPanel;
    private Composite pnlResultsHeader;
    private Composite pnlOrderSearchResults; 
    private Label lblSearchCriteriaTitle;
    private Label lblResultsTitle;
    
    Table tblSearchResults = null;
    
    
	private Composite pnlRoot = null;
	private Composite pnlButtons = null;
	private Button btnSearch = null;
	
	
	
	
	private SelectionAdapter selectionAdapter;
	
	private Label lblOrganization;
	private Combo comboOrganization;
	private Label lblUserID;
	private Text txtUserID;
	private Label lblStatus;
	private Label lblName;
	private Combo comboNameQueryType;
	private Text txtName;
	private Button radStatus1;
	private Button radStatus2;
	private TableColumn clmUserID;
	private TableColumn clmUserName;

	public XPXInternalUserSearchListScreen(Composite parent, int style, Object inputObject) {
		super(parent, style);

		selectionAdapter = new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				Widget ctrl = e.widget;
				String ctrlName = (String) ctrl.getData("name");
				if (!YRCPlatformUI.isVoid(ctrlName)){
					if (YRCPlatformUI.equals(ctrlName, "btnSearch"))
						myBehavior.search();
					
				}
			}
		};
		initialize();
        setBindingForComponents();
        myBehavior = new XPXInternalUserSearchListBehaviour(this, FORM_ID,inputObject);
	}

	private void initialize() {
		setData("name", "InternalUserSearchAndList");
        GridLayout thislayout = new GridLayout(1, false);
        thislayout.marginHeight = 0;
        thislayout.marginWidth = 0;
        setLayout(thislayout);
        createPnlRoot();
	}

	private void createPnlRoot() {
		
        pnlRoot = new Composite(this, SWT.NONE);
        pnlRoot.setBackgroundMode(0);
        pnlRoot.setData("name", "pnlRoot");
        GridData pnlRootlayoutData = new GridData();
        pnlRootlayoutData.horizontalAlignment = 4;
        pnlRootlayoutData.verticalAlignment = 4;
        pnlRootlayoutData.grabExcessHorizontalSpace = true;
        pnlRootlayoutData.grabExcessVerticalSpace = true;
        pnlRoot.setLayoutData(pnlRootlayoutData);
        pnlRoot.setData("yrc:customType", "TaskComposite");
        GridLayout pnlRootlayout = new GridLayout(1, false);
        pnlRootlayout.horizontalSpacing = 2;
        pnlRootlayout.verticalSpacing = 0;
        pnlRootlayout.marginHeight = 2;
        pnlRootlayout.marginWidth = 2;
        pnlRoot.setLayout(pnlRootlayout);
        createPnlSearchTop();
        createPnlButtons();
        createCmpstSearchResultsAndControls();
        
	}
	
	private void createPnlSearchTop() {
		
		pnlSearchTop = new Composite(pnlRoot, SWT.NONE);
		pnlSearchTop.setBackgroundMode(SWT.INHERIT_NONE);
	
		pnlSearchTop.setData(YRCConstants.YRC_CONTROL_NAME, "pnlSearchTop");
		
		GridData pnlSearchToplayoutData = new GridData();
		pnlSearchToplayoutData.horizontalAlignment = 4;
		pnlSearchToplayoutData.verticalAlignment = 4;
		pnlSearchToplayoutData.grabExcessHorizontalSpace = true;
		pnlSearchTop.setLayoutData(pnlSearchToplayoutData);
			GridLayout pnlSearchToplayout = new GridLayout(1, false);
		pnlSearchToplayout.horizontalSpacing = 0;
		pnlSearchToplayout.verticalSpacing = 0;
		pnlSearchToplayout.marginHeight = 1;
		pnlSearchToplayout.marginWidth = 1;
		pnlSearchTop.setLayout(pnlSearchToplayout);
		pnlSearchTop.addPaintListener(new org.eclipse.swt.events.PaintListener() {
			public void paintControl(org.eclipse.swt.events.PaintEvent e) {
				XPXUtils.paintControl(e);
			}
		});
		createPnlSearchCriteriaTitle();
		createPnlSearchCriteria();
	}

	private void createPnlSearchCriteriaTitle(){
		
		pnlSearchCriteriaTitle = new Composite(pnlSearchTop, SWT.NONE);
		pnlSearchCriteriaTitle.setBackgroundMode(SWT.INHERIT_NONE);
		pnlSearchCriteriaTitle.setData(YRCConstants.YRC_CONTROL_NAME, "pnlSearchCriteriaTitle");
		GridData pnlSearchCriteriaTitlelayoutData = new GridData();
		pnlSearchCriteriaTitlelayoutData.horizontalAlignment = 4;
		pnlSearchCriteriaTitlelayoutData.verticalAlignment = 4;
		pnlSearchCriteriaTitlelayoutData.grabExcessHorizontalSpace = true;
		pnlSearchCriteriaTitle.setLayoutData(pnlSearchCriteriaTitlelayoutData);
		pnlSearchCriteriaTitle.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		GridLayout pnlSearchCriteriaTitlelayout = new GridLayout(3, false);
		pnlSearchCriteriaTitlelayout.horizontalSpacing = 2;
		pnlSearchCriteriaTitlelayout.verticalSpacing = 2;
		pnlSearchCriteriaTitlelayout.marginHeight = 2;
		pnlSearchCriteriaTitlelayout.marginWidth = 2;
		pnlSearchCriteriaTitle.setLayout(pnlSearchCriteriaTitlelayout);
		
		lblSearchCriteriaTitle = new Label(pnlSearchCriteriaTitle, SWT.CENTER);
		GridData lblSearchCriteriaTitlelayoutData = new GridData();
		lblSearchCriteriaTitlelayoutData.verticalAlignment = 16777216;
		lblSearchCriteriaTitlelayoutData.horizontalAlignment = GridData.BEGINNING;
		lblSearchCriteriaTitlelayoutData.grabExcessHorizontalSpace = true;
		lblSearchCriteriaTitle.setLayoutData(lblSearchCriteriaTitlelayoutData);
		lblSearchCriteriaTitle.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		lblSearchCriteriaTitle.setText("Internal_User_Search_Criteria");
		

	}

	
	private void createPnlSearchCriteria() {
		pnlSearchCriteria = new Composite(pnlSearchTop, 0);
        pnlSearchCriteria.setBackgroundMode(0);
        pnlSearchCriteria.setData("name", "pnlSearchCriteria");
        GridData pnlSearchCriterialayoutData = new GridData();
        pnlSearchCriterialayoutData.horizontalAlignment = 4;
        pnlSearchCriterialayoutData.verticalAlignment = 4;
        pnlSearchCriterialayoutData.grabExcessHorizontalSpace = true;
        pnlSearchCriterialayoutData.grabExcessVerticalSpace = true;
        pnlSearchCriteria.setLayoutData(pnlSearchCriterialayoutData);
        GridLayout pnlSearchCriterialayout = new GridLayout(4, false);
        pnlSearchCriteria.setLayout(pnlSearchCriterialayout);
        createControlsToSearchCriteria();
		
	}
	private void createControlsToSearchCriteria(){
		pnlBasicSearchCriteriaBody = new Composite(pnlSearchCriteria, SWT.NONE);
		pnlBasicSearchCriteriaBody.setBackgroundMode(SWT.INHERIT_NONE);
	
		pnlBasicSearchCriteriaBody.setData(YRCConstants.YRC_CONTROL_NAME, "pnlBasicSearchCriteriaBody");
		
		GridData pnlBasicSearchCriteriaBodylayoutData = new GridData();
		pnlBasicSearchCriteriaBodylayoutData.horizontalAlignment = 4;
		pnlBasicSearchCriteriaBodylayoutData.verticalAlignment = 4;
		pnlBasicSearchCriteriaBodylayoutData.grabExcessHorizontalSpace = true;
		pnlBasicSearchCriteriaBodylayoutData.grabExcessVerticalSpace = true;
		pnlBasicSearchCriteriaBody.setLayoutData(pnlBasicSearchCriteriaBodylayoutData);
		GridLayout pnlBasicSearchCriteriaBodylayout = new GridLayout(4, false);
		pnlBasicSearchCriteriaBody.setLayout(pnlBasicSearchCriteriaBodylayout);
		
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = SWT.LEFT;
		gridData1.widthHint = 100;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = 4;
		gridData2.widthHint = 300;
		gridData2.verticalIndent = 8;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = 4;
		gridData3.widthHint = 150;
		gridData3.verticalIndent = 8;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = 4;
		gridData4.widthHint = 150;
		gridData4.grabExcessHorizontalSpace = true;
		
		lblOrganization = new Label(pnlBasicSearchCriteriaBody, SWT.LEFT);
		lblOrganization.setText("Organization");
		lblOrganization.setLayoutData(gridData1);
		lblOrganization.setData("name", "lblOrganization");
		comboOrganization = new Combo(pnlBasicSearchCriteriaBody, 8);
		comboOrganization.setLayoutData(gridData2);
		comboOrganization.setData("name", "comboOrganization");
		lblUserID = new Label(pnlBasicSearchCriteriaBody, SWT.LEFT);
		lblUserID.setText("User_ID_Starts_With");
		lblUserID.setLayoutData(gridData3);
		lblUserID.setData("name", "lblUserID");
		txtUserID = new Text(pnlBasicSearchCriteriaBody, SWT.BORDER);
		txtUserID.setText("");
		txtUserID.setLayoutData(gridData4);
		txtUserID.setTextLimit(500);
		txtUserID.setData("name", "txtUserID");
		
		lblName = new Label(pnlBasicSearchCriteriaBody, SWT.LEFT);
		lblName.setText("Name");
		lblName.setLayoutData(gridData1);
		lblName.setData("name", "lblName");
		
		Composite pnlName = new Composite(pnlBasicSearchCriteriaBody,SWT.NONE);
			
		GridLayout pnlNameLayout = new GridLayout(2,false);
		pnlNameLayout.verticalSpacing = 1;
		pnlNameLayout.marginWidth = 0;
		pnlName.setLayout(pnlNameLayout);
		
		GridData pnlNameLayoutData = new GridData();
		pnlNameLayoutData.horizontalAlignment = SWT.BEGINNING;
		pnlName.setLayoutData(pnlNameLayoutData);
		
		
		
		pnlName.setLayout(pnlNameLayout);
		pnlName.setLayoutData(pnlNameLayoutData);
		
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = SWT.BEGINNING;
		gridData5.widthHint = 60;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = SWT.BEGINNING;
		gridData6.widthHint = 100;
			
		
		comboNameQueryType = new Combo(pnlName, 8);
		comboNameQueryType.setLayoutData(gridData5);
		comboNameQueryType.setData("name", "comboNameQueryType");
		txtName =  new Text(pnlName, SWT.BORDER);
		txtName.setText("");
		txtName.setLayoutData(gridData6);
		txtName.setTextLimit(500);
		txtName.setData("name", "txtName");
		
		lblStatus = new Label(pnlBasicSearchCriteriaBody, SWT.LEFT);
		lblStatus.setText("Status");
		lblStatus.setLayoutData(gridData3);
		lblStatus.setData("name", "lblStatus");
		
		Composite pnlUserStatusRadButtons = new Composite(pnlBasicSearchCriteriaBody,SWT.NONE);
		pnlUserStatusRadButtons.setLayout(pnlNameLayout);
		pnlUserStatusRadButtons.setLayoutData(gridData4);
		radStatus1 = new Button(pnlUserStatusRadButtons, SWT.RADIO);
		radStatus1.setText("Active");
		radStatus1.setData("name", "radStatus1");
		radStatus1.setData("yrc:customType", "Label");
		radStatus2 = new Button(pnlUserStatusRadButtons, SWT.RADIO);
		radStatus2.setText("Susupend");
		radStatus2.setData("name", "radStatus2");
		radStatus2.setData("yrc:customType", "Label");
	}
	

	
	
	
	private void createPnlButtons(){
		
		pnlButtons = new Composite(pnlRoot, SWT.NONE);
		pnlButtons.setBackgroundMode(SWT.INHERIT_NONE);
		pnlButtons.setData(YRCConstants.YRC_CONTROL_NAME, "pnlButtons");
		GridData pnlButtonslayoutData = new GridData();
		pnlButtonslayoutData.horizontalAlignment = GridData.FILL;
		pnlButtonslayoutData.verticalAlignment = 4;

		pnlButtons.setLayoutData(pnlButtonslayoutData);
		GridLayout pnlButtonslayout = new GridLayout(4, false);
		pnlButtonslayout.marginHeight = 2;
		pnlButtonslayout.marginWidth = 0;
		pnlButtons.setLayout(pnlButtonslayout);
	
		btnSearch = new Button(pnlButtons, SWT.PUSH);
		btnSearch.setData("name","btnSearch");
		GridData btnSearchlayoutData = new GridData();
		btnSearchlayoutData.horizontalAlignment = 16777224;
		btnSearchlayoutData.verticalAlignment = 16777216;
		btnSearchlayoutData.heightHint = 25;
		btnSearchlayoutData.widthHint = 80;
		btnSearch.setLayoutData(btnSearchlayoutData);
		btnSearch.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "Button");
		btnSearch.setText("Search");
		btnSearch.addSelectionListener(selectionAdapter);
	
	}
	
	private void createCmpstSearchResultsAndControls(){
		
		cmpstSearchResultsAndControls = new Composite(pnlRoot, SWT.NONE);
		cmpstSearchResultsAndControls.setBackgroundMode(SWT.INHERIT_NONE);
	
		cmpstSearchResultsAndControls.setData(YRCConstants.YRC_CONTROL_NAME, "cmpstSearchResultsAndControls");
		
		GridData cmpstSearchResultsAndControlslayoutData = new GridData();
		cmpstSearchResultsAndControlslayoutData.horizontalAlignment = 4;
		cmpstSearchResultsAndControlslayoutData.verticalAlignment = 4;
		cmpstSearchResultsAndControlslayoutData.grabExcessHorizontalSpace = true;
		cmpstSearchResultsAndControlslayoutData.grabExcessVerticalSpace = true;
		cmpstSearchResultsAndControls.setLayoutData(cmpstSearchResultsAndControlslayoutData);
			GridLayout cmpstSearchResultsAndControlslayout = new GridLayout(1, false);
		cmpstSearchResultsAndControlslayout.horizontalSpacing = 0;
		cmpstSearchResultsAndControlslayout.verticalSpacing = 0;
		cmpstSearchResultsAndControlslayout.marginHeight = 0;
		cmpstSearchResultsAndControlslayout.marginWidth = 0;
		cmpstSearchResultsAndControls.setLayout(cmpstSearchResultsAndControlslayout);
		createResultsPanel();

	}

	private void createResultsPanel(){
	
		resultsPanel = new Composite(cmpstSearchResultsAndControls, SWT.NONE);
		resultsPanel.setBackgroundMode(SWT.INHERIT_NONE);
	
		resultsPanel.setData(YRCConstants.YRC_CONTROL_NAME, "resultsPanel");
		
		GridData resultsPanellayoutData = new GridData();
		resultsPanellayoutData.horizontalAlignment = 4;
		resultsPanellayoutData.verticalAlignment = 4;
		resultsPanellayoutData.grabExcessHorizontalSpace = true;
		resultsPanellayoutData.grabExcessVerticalSpace = true;
		resultsPanel.setLayoutData(resultsPanellayoutData);
			GridLayout resultsPanellayout = new GridLayout(1, false);
		resultsPanellayout.verticalSpacing = 1;
		resultsPanellayout.marginHeight = 1;
		resultsPanellayout.marginWidth = 1;
		resultsPanel.setLayout(resultsPanellayout);
		resultsPanel.addPaintListener(new org.eclipse.swt.events.PaintListener() {
			public void paintControl(org.eclipse.swt.events.PaintEvent e) {
				XPXUtils.paintControl(e);
			}
		});
		createPnlResultsHeader();
		createPnlOrderSearchResults();

	}

	private void createPnlResultsHeader(){
	
		pnlResultsHeader = new Composite(resultsPanel, SWT.NONE);
		pnlResultsHeader.setBackgroundMode(SWT.INHERIT_NONE);
	
		pnlResultsHeader.setData(YRCConstants.YRC_CONTROL_NAME, "pnlResultsHeader");
		
		GridData pnlResultsHeaderlayoutData = new GridData();
		pnlResultsHeaderlayoutData.horizontalAlignment = 4;
		pnlResultsHeaderlayoutData.verticalAlignment = 4;
		pnlResultsHeaderlayoutData.grabExcessHorizontalSpace = true;
		pnlResultsHeader.setLayoutData(pnlResultsHeaderlayoutData);
		pnlResultsHeader.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		
		GridLayout pnlResultsHeaderlayout = new GridLayout(1, false);
		pnlResultsHeaderlayout.horizontalSpacing = 2;
		pnlResultsHeaderlayout.verticalSpacing = 2;
		pnlResultsHeaderlayout.marginHeight = 2;
		pnlResultsHeaderlayout.marginWidth = 2;
		pnlResultsHeader.setLayout(pnlResultsHeaderlayout);
		lblResultsTitle = new Label(pnlResultsHeader, SWT.LEFT);
		GridData lblResultsTitlelayoutData = new GridData();
		lblResultsTitlelayoutData.verticalAlignment = 16777216;
	
		lblResultsTitle.setLayoutData(lblResultsTitlelayoutData);
		lblResultsTitle.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		lblResultsTitle.setText("Internal_Users_List");
	}

	private void createPnlOrderSearchResults(){
		
		pnlOrderSearchResults = new Composite(resultsPanel, SWT.NONE);
		pnlOrderSearchResults.setBackgroundMode(SWT.INHERIT_NONE);
	
		pnlOrderSearchResults.setData(YRCConstants.YRC_CONTROL_NAME, "pnlOrderSearchResults");
		
		GridData pnlOrderSearchResultslayoutData = new GridData();
		pnlOrderSearchResultslayoutData.horizontalAlignment = 4;
		pnlOrderSearchResultslayoutData.verticalAlignment = 4;
		pnlOrderSearchResultslayoutData.grabExcessHorizontalSpace = true;
		pnlOrderSearchResultslayoutData.grabExcessVerticalSpace = true;
		pnlOrderSearchResults.setLayoutData(pnlOrderSearchResultslayoutData);
			GridLayout pnlOrderSearchResultslayout = new GridLayout(1, false);
		pnlOrderSearchResultslayout.horizontalSpacing = 0;
		pnlOrderSearchResultslayout.verticalSpacing = 0;
		pnlOrderSearchResultslayout.marginHeight = 0;
		pnlOrderSearchResultslayout.marginWidth = 0;
		pnlOrderSearchResults.setLayout(pnlOrderSearchResultslayout);
		createTblSearchResults();


	}
	
	private void createTblSearchResults(){
		
		tblSearchResults = new Table(pnlOrderSearchResults, SWT.WRAP | SWT.MULTI | SWT.FULL_SELECTION);
	
		tblSearchResults.setHeaderVisible(true);
	
		tblSearchResults.setLinesVisible(true);
		tblSearchResults.setData(YRCConstants.YRC_CONTROL_NAME, "tblSearchResults");
		
		GridData tblSearchResultslayoutData = new GridData();
		tblSearchResultslayoutData.horizontalAlignment = 4;
		tblSearchResultslayoutData.verticalAlignment = 4;
		tblSearchResultslayoutData.grabExcessHorizontalSpace = true;
		tblSearchResultslayoutData.grabExcessVerticalSpace = true;
		tblSearchResults.setLayoutData(tblSearchResultslayoutData);
		tblSearchResults.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				myBehavior.mouseDoubleClick(e, "tblSearchResults");
			}
		});
		
		clmUserID = new TableColumn(tblSearchResults, SWT.LEFT);
		clmUserID.setText("User_ID");
		clmUserID.setWidth(200);
		clmUserID.setResizable(true);
		clmUserID.setMoveable(false);
		
		clmUserName = new TableColumn(tblSearchResults, SWT.LEFT);
		clmUserName.setText("User_Name");
		clmUserName.setWidth(200);
		clmUserName.setResizable(true);
		clmUserName.setMoveable(false);
		
		
		
	}

	private void setBindingForComponents() {
		
		YRCTextBindingData tbd = null;
		YRCComboBindingData cbd = null;
		YRCButtonBindingData bbd = null;
		
		cbd = new YRCComboBindingData();
        cbd.setName("comboOrganization");
        cbd.setSourceBinding("SearchCriteria:/User/@OrganizationKey");
        cbd.setTargetBinding("SearchCriteria:/User/@OrganizationKey");
        cbd.setCodeBinding("OrganizationKey");
        cbd.setListBinding("OrgList:OrganizationList/Organization");
        cbd.setDescriptionBinding("OrganizationCode");
        comboOrganization.setData("YRCComboBindingDefination", cbd);
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtUserID");
        tbd.setTargetBinding("SearchCriteria:/User/@Loginid");
        tbd.setSourceBinding("SearchCriteria:/User/@Loginid");
        txtUserID.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
        
        YRCButtonBindingData radItem1ButtonBindingData = new YRCButtonBindingData();
		radItem1ButtonBindingData.setName("radStatus1");
		radItem1ButtonBindingData.setSourceBinding("SearchCriteria:/User/@Activateflag");
		radItem1ButtonBindingData.setTargetBinding("SearchCriteria:/User/@Activateflag");
		radItem1ButtonBindingData.setCheckedBinding("Y");
		radStatus1.setData("YRCButtonBindingDefination",radItem1ButtonBindingData);
		
		YRCButtonBindingData radItem1ButtonBindingData1 = new YRCButtonBindingData();
		radItem1ButtonBindingData1.setName("radStatus2");
		radItem1ButtonBindingData1.setSourceBinding("SearchCriteria:/User/@Activateflag");
		radItem1ButtonBindingData1.setTargetBinding("SearchCriteria:/User/@Activateflag");
		radItem1ButtonBindingData1.setCheckedBinding("N");
		radStatus2.setData("YRCButtonBindingDefination",radItem1ButtonBindingData1);
        
        cbd = new YRCComboBindingData();
        cbd.setName("comboNameQueryType");
        cbd.setSourceBinding("SearchCriteria:/User/@UsernameQryType");
        cbd.setTargetBinding("SearchCriteria:/User/@UsernameQryType");
        cbd.setCodeBinding("QueryType");
        cbd.setListBinding("getQueryTypeList_output:/QueryTypeList/StringQueryTypes/QueryType");
        cbd.setDescriptionBinding("QueryTypeDesc");
        comboNameQueryType.setData("YRCComboBindingDefination", cbd);
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtName");
        tbd.setTargetBinding("SearchCriteria:/User/@Username");
        tbd.setSourceBinding("SearchCriteria:/User/@Username");
        txtName.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
		
		        
        // Search Results Table bindings
        YRCTableBindingData tblSearchResultsbd = new YRCTableBindingData();
		tblSearchResultsbd.setName("tblSearchResults");
		tblSearchResultsbd.setSourceBinding("UserList:UserList/User");
	
		YRCTblClmBindingData tblSearchResultsClmBd[] = new YRCTblClmBindingData[2];
		int tblSearchResultsCounter=0;
		
				
		tblSearchResultsClmBd[tblSearchResultsCounter] = new YRCTblClmBindingData();
		tblSearchResultsClmBd[tblSearchResultsCounter].setName("clmUserID");
		tblSearchResultsClmBd[tblSearchResultsCounter].setAttributeBinding("@Loginid");		
		tblSearchResultsClmBd[tblSearchResultsCounter].setFilterReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setColumnBinding("User_ID");
		tblSearchResultsCounter++;
		tblSearchResultsClmBd[tblSearchResultsCounter] = new YRCTblClmBindingData();
		tblSearchResultsClmBd[tblSearchResultsCounter].setName("clmUserName");
		tblSearchResultsClmBd[tblSearchResultsCounter].setAttributeBinding("@Username");		
		tblSearchResultsClmBd[tblSearchResultsCounter].setFilterReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setColumnBinding("User_Name");
		tblSearchResultsCounter++;
		tblSearchResultsbd.setTblClmBindings(tblSearchResultsClmBd) ;
		tblSearchResultsbd.setSortRequired(true);
		tblSearchResultsbd.setDefaultSort(true);
		tblSearchResults.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, tblSearchResultsbd);
    }

    public IYRCPanelHolder getPanelHolder() {
        // TODO Complete getPanelHolder
        return null;
    }

	public String getHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	public XPXInternalUserSearchListBehaviour getMyBehavior() {
		return myBehavior;
	}
	
	public String getFormId() {
		return FORM_ID;
	}
	
	public Composite getRootPanel() {
		return pnlRoot;
	}

	//@Override
	public YRCPaginationData getPaginationData() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public boolean hide() {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public boolean resetSearchCriteria() {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public boolean search() {
		// TODO Auto-generated method stub
		return false;
	}
}
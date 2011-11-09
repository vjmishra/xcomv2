package com.xpedx.sterling.rcp.pca.myitems.screen;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PartInitException;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXPaginationBehavior;
import com.xpedx.sterling.rcp.pca.util.XPXPaginationComposite;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCCellModifier;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPaginatedSearchAndListComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.IYRCTableColumnTextProvider;
import com.yantra.yfc.rcp.IYRCTableLinkProvider;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPaginationData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;

/**
 * @author Sunith Dodda
 *
 * My Items List Search and List Panel.
 */
public class XPXMyItemsSearchListScreen extends XPXPaginationComposite  implements IYRCComposite,IYRCPaginatedSearchAndListComposite {

    public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.myitems.screen.XPXMyItemsSearchListScreen";
    private XPXMyItemsSearchListScreenBehavior myBehavior;

    private Composite pnlSearchTop;
    private Composite pnlSearchCriteriaTitle;
    private Composite pnlSearchCriteria;
    private Composite cmpstEnterpriseCode;
    private Composite pnlBasicSearchCriteriaBody;
    private Composite cmpstSearchResultsAndControls;
    private Composite resultsPanel;
    private Composite pnlResultsHeader;
    private Composite pnlOrderSearchResults; 
    private Label lblSearchCriteriaTitle;
    private Label lblResultsTitle;
//	search fields declaration starts here
    private Label lblEnterprise;
	private Combo cmbEnterprise;
	Combo cmbDivisions;
	private Text txtCustomer;
	private Text txtUserId;
    
    Table tblSearchResults = null;
    Table tblResults = null;
    
	private Composite pnlRoot = null;
	private Composite pnlButtons = null;
	private Button btnSearch = null;
	private Button btnReset = null;
	private Button btnCreate;
	private SelectionAdapter selectionAdapter;
	

	public XPXMyItemsSearchListScreen(Composite parent, int style, Object inputObject) {
		super(parent, style);

		selectionAdapter = new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				Widget ctrl = e.widget;
				String ctrlName = (String) ctrl.getData("name");
				if (!YRCPlatformUI.isVoid(ctrlName)){
					if (YRCPlatformUI.equals(ctrlName, "btnSearch")){
						//Fetches the first page of search results
						myBehavior.getFirstPage();
					}
				}
			}
		};
		initialize();
        setBindingForComponents();
        myBehavior = new XPXMyItemsSearchListScreenBehavior(this, FORM_ID,inputObject);
	}

	private void initialize() {
		setData("name", "pnlMyItemsSearchAndList");
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
		
		lblSearchCriteriaTitle = new Label(pnlSearchCriteriaTitle, SWT.LEFT);
		GridData lblSearchCriteriaTitlelayoutData = new GridData();
		lblSearchCriteriaTitlelayoutData.verticalAlignment = 16777216;
		lblSearchCriteriaTitlelayoutData.grabExcessHorizontalSpace = true;
		lblSearchCriteriaTitle.setLayoutData(lblSearchCriteriaTitlelayoutData);
		lblSearchCriteriaTitle.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		lblSearchCriteriaTitle.setText("Search_Criteria_For_MyItems_List");
		
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
        GridLayout pnlSearchCriterialayout = new GridLayout(2, false);
        pnlSearchCriteria.setLayout(pnlSearchCriterialayout);
        createCmpstEnterpriseCode();
        createControlsToPnlSearchCriteria();
		
	}
	
	private void createControlsToPnlSearchCriteria() {
		pnlBasicSearchCriteriaBody = new Composite(pnlSearchCriteria, SWT.NONE);
		pnlBasicSearchCriteriaBody.setBackgroundMode(SWT.INHERIT_NONE);
		pnlBasicSearchCriteriaBody.setData(YRCConstants.YRC_CONTROL_NAME, "pnlBasicSearchCriteriaBody");
		
		GridData pnlBasicSearchCriteriaBodylayoutData = new GridData();
		pnlBasicSearchCriteriaBodylayoutData.horizontalAlignment = 4;
		pnlBasicSearchCriteriaBodylayoutData.verticalAlignment = 4;
		pnlBasicSearchCriteriaBodylayoutData.grabExcessHorizontalSpace = true;
		pnlBasicSearchCriteriaBodylayoutData.grabExcessVerticalSpace = true;
		pnlBasicSearchCriteriaBody.setLayoutData(pnlBasicSearchCriteriaBodylayoutData);
		GridLayout pnlBasicSearchCriteriaBodylayout = new GridLayout(3, false);
		pnlBasicSearchCriteriaBody.setLayout(pnlBasicSearchCriteriaBodylayout);
		
		
		Label lblCustomer = new Label(pnlBasicSearchCriteriaBody, SWT.LEFT);
		GridData lblCustomerlayoutData = new GridData();
		lblCustomerlayoutData.verticalAlignment = 16777216;
		lblCustomer.setLayoutData(lblCustomerlayoutData);
		lblCustomer.setText("Customer_ID");
		
		GridData txtCustomerlayoutData = new GridData();
		txtCustomerlayoutData.horizontalAlignment = 4;
		txtCustomerlayoutData.widthHint = 120;
		txtCustomerlayoutData.grabExcessHorizontalSpace=false;
		txtCustomerlayoutData.horizontalSpan = 2;
		txtCustomer = new Text(pnlBasicSearchCriteriaBody, SWT.BORDER);
		txtCustomer.setLayoutData(txtCustomerlayoutData);
		txtCustomer.setEditable(true);
		txtCustomer.setTextLimit(50);
		
		
		Label lblUserId = new Label(pnlBasicSearchCriteriaBody, SWT.LEFT);
		GridData lblUserIdlayoutData = new GridData();
		lblUserIdlayoutData.verticalAlignment = 16777216;
		lblUserId.setLayoutData(lblUserIdlayoutData);
		lblUserId.setText("User Id");
		
		GridData txtUserIdlayoutData = new GridData();
		txtUserIdlayoutData.horizontalAlignment = 4;
		txtUserIdlayoutData.widthHint = 120;
		txtUserIdlayoutData.grabExcessHorizontalSpace=false;
		txtUserIdlayoutData.horizontalSpan = 2;
		txtUserId = new Text(pnlBasicSearchCriteriaBody, SWT.BORDER);
		txtUserId.setLayoutData(txtCustomerlayoutData);
		txtUserId.setEditable(true);
		txtUserId.setTextLimit(50);

	}

	private void createCmpstEnterpriseCode(){
		
		cmpstEnterpriseCode = new Composite(pnlSearchCriteria, SWT.NONE);
		cmpstEnterpriseCode.setBackgroundMode(SWT.INHERIT_NONE);
		cmpstEnterpriseCode.setData(YRCConstants.YRC_CONTROL_NAME, "cmpstEnterpriseCode");
		GridData cmpstEnterpriseCodelayoutData = new GridData();
		cmpstEnterpriseCodelayoutData.horizontalAlignment = 4;
		cmpstEnterpriseCodelayoutData.verticalAlignment = 16777216;
		cmpstEnterpriseCodelayoutData.grabExcessHorizontalSpace = true;
		cmpstEnterpriseCodelayoutData.horizontalSpan = 2;
		cmpstEnterpriseCode.setLayoutData(cmpstEnterpriseCodelayoutData);
		GridLayout cmpstEnterpriseCodelayout = new GridLayout(2, false);
		cmpstEnterpriseCodelayout.marginHeight = 0;
		cmpstEnterpriseCode.setLayout(cmpstEnterpriseCodelayout);
		
		lblEnterprise = new Label(cmpstEnterpriseCode, SWT.LEFT);
		GridData lblEnterpriselayoutData = new GridData();
		lblEnterpriselayoutData.horizontalAlignment = 16777224;
		lblEnterpriselayoutData.verticalAlignment = 16777216;
		lblEnterprise.setLayoutData(lblEnterpriselayoutData);
		lblEnterprise.setText("Enterprise");
	
		cmbEnterprise = new Combo(cmpstEnterpriseCode, SWT.READ_ONLY);
		GridData cmbEnterpriselayoutData = new GridData();
		cmbEnterpriselayoutData.horizontalAlignment = 4;
		cmbEnterprise.setLayoutData(cmbEnterpriselayoutData);
		
		cmpstEnterpriseCode.setVisible(false);
	}
	
	private void createPnlButtons(){
		
		pnlButtons = new Composite(pnlRoot, SWT.NONE);
		pnlButtons.setBackgroundMode(SWT.INHERIT_NONE);
		pnlButtons.setData(YRCConstants.YRC_CONTROL_NAME, "pnlButtons");
		GridData pnlButtonslayoutData = new GridData();
		pnlButtonslayoutData.horizontalAlignment = 4;
		pnlButtonslayoutData.verticalAlignment = 4;
		pnlButtonslayoutData.grabExcessHorizontalSpace = true;
		pnlButtonslayoutData.horizontalSpan = 2;
		pnlButtons.setLayoutData(pnlButtonslayoutData);
		pnlButtons.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "TaskComposite");
		GridLayout pnlButtonslayout = new GridLayout(5, false);
		pnlButtonslayout.marginHeight = 2;
		pnlButtonslayout.marginWidth = 0;
		pnlButtons.setLayout(pnlButtonslayout);
//		createPnlShowHideCriteria();
//		createPnlOrderByOptions();
	
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
	
		btnReset = new Button(pnlButtons, SWT.PUSH);
		GridData btnResetlayoutData = new GridData();
		btnResetlayoutData.horizontalAlignment = 2;
		btnResetlayoutData.heightHint = 25;
		btnResetlayoutData.widthHint = 80;
		btnReset.setLayoutData(btnResetlayoutData);
		btnReset.setText("Reset");
		/**changed for XIRA ID-1176**/
		btnReset.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.reset();
				
			}
		});
		btnCreate = new Button(pnlButtons, SWT.NONE);
		btnCreate.setText("Create_myitems");
		btnCreate.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.create();
			}
		});
		Button btnReplace = new Button(pnlButtons, SWT.NONE);
		btnReplace.setText("Replacement_Tool");
		btnReplace.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.openReplacementTool();
			}
		});
		
		Button btnShiptoList = new Button(pnlButtons, SWT.NONE);
		btnShiptoList.setText("Get Assigned Ship To ");
		btnShiptoList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.selectShipToAddress();
				
			}
		});
		
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
		lblResultsTitle.setText("List_Of_My_Items_Lists");
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
//		createCmpPaginationPanel() ;
//		createPnlHistoryRecentSwitch();
		
		createPaginationLinks(pnlOrderSearchResults, myBehavior.PAGINATION_STRATEGY_FOR_MIL_SEARCH);
	}
	
	private void createTblSearchResults(){
		
		GridData gridDataTblShipCustomers = new org.eclipse.swt.layout.GridData();
        gridDataTblShipCustomers.horizontalSpan = 4;

        tblResults = new Table(pnlOrderSearchResults, SWT.SINGLE | SWT.H_SCROLL| SWT.V_SCROLL|SWT.FULL_SELECTION);
		
		TableColumn clmCustAddress = new TableColumn(tblResults, SWT.NONE);
		clmCustAddress.setWidth(130);
		clmCustAddress.setText("Column 1");
		
		TableColumn clmCreatedBy = new TableColumn(tblResults, SWT.NONE);
		clmCreatedBy.setWidth(130);
		
		TableColumn clmLastModDate = new TableColumn(tblResults, SWT.NONE);
		clmLastModDate.setWidth(130);
		
		TableColumn clmLastModUser = new TableColumn(tblResults, SWT.NONE);
		clmLastModUser.setWidth(130);
		
		TableColumn clmAction = new TableColumn(tblResults, SWT.NONE);
		clmAction.setWidth(130);
//		clmAction.setText("Column 2");
		
		gridDataTblShipCustomers.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridDataTblShipCustomers.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridDataTblShipCustomers.grabExcessHorizontalSpace = true;
		gridDataTblShipCustomers.grabExcessVerticalSpace = true;
		tblResults.setLayoutData(gridDataTblShipCustomers);
		tblResults.setHeaderVisible(true);
		tblResults.setLinesVisible(true);
		
		// HardCoded Values for the height to make up 
		// for the requirement to increase the 
		// column height.
		tblResults.addListener(SWT.MeasureItem, new Listener() { 
			  public void handleEvent(Event event) { 
			     event.height = 30;
			  }
		});

		tblResults.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				myBehavior.mouseDoubleClick(e, "tblResults");
			}
		});
	}
	
	private void setBindingForComponents() {
		YRCComboBindingData cbd = new YRCComboBindingData();
		cbd.setName("cmbEnterprise");
        cbd.setSourceBinding("SearchCriteria:/XPEDXMyItemsList/@EnterpriseKey");
        cbd.setTargetBinding("SearchCriteria:/XPEDXMyItemsList/@EnterpriseKey");
        cbd.setCodeBinding("OrganizationKey");
        cbd.setListBinding("OrgList:OrganizationList/Organization");
        cbd.setDescriptionBinding("OrganizationCode");
        cmbEnterprise.setData("YRCComboBindingDefination", cbd);

        
        YRCTextBindingData textBindingData = new YRCTextBindingData();
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("SearchCriteria:/XPEDXMyItemsList/@CustomerID");
        textBindingData.setTargetBinding("SearchCriteria:/XPEDXMyItemsList/@CustomerID");
        textBindingData.setName("txtCustomer");
        txtCustomer.setData("YRCTextBindingDefination", textBindingData);
        
        
      //  txtUserId
        
        textBindingData = new YRCTextBindingData();
        textBindingData = new YRCTextBindingData();
        textBindingData.setSourceBinding("SearchCriteria:/XPEDXMyItemsList/@SharePrivate");
        textBindingData.setTargetBinding("SearchCriteria:/XPEDXMyItemsList/@SharePrivate");
        textBindingData.setName("txtUserId");
        txtUserId.setData("YRCTextBindingDefination", textBindingData);

        /************/
        YRCTableBindingData tblResultsBinding = new YRCTableBindingData();
		YRCTblClmBindingData colBindings1[] = new YRCTblClmBindingData[tblResults.getColumnCount()];
		colBindings1[0] = new YRCTblClmBindingData();
		colBindings1[0].setName("clmCustAddress");
		colBindings1[0].setAttributeBinding("@Name;@Desc;XPEDXMyItemsItemsList/@TotalNumberOfRecords");
		colBindings1[0].setKey("My_Search_Item_List_Name_Key");
        colBindings1[0].setColumnBinding("List_Nm");
        colBindings1[0].setSortReqd(true);
        colBindings1[0].setSortBinding("@Name");
		colBindings1[0].setLinkReqd(true);
		
       
        
        colBindings1[1] = new YRCTblClmBindingData();
		colBindings1[1].setName("clmCreatedBy");
		colBindings1[1].setAttributeBinding("@Createuserid");
        colBindings1[1].setColumnBinding("Created_By");
        colBindings1[1].setSortReqd(true);
        
        colBindings1[2] = new YRCTblClmBindingData();
		colBindings1[2].setName("clmLastModDate");
		colBindings1[2].setAttributeBinding("@Modifyts");
        colBindings1[2].setColumnBinding("Last_Modified_Date");
        colBindings1[2].setSortReqd(true);
        
        colBindings1[3] = new YRCTblClmBindingData();
		colBindings1[3].setName("clmLastModUser");
		colBindings1[3].setAttributeBinding("@Modifyuserid");
        colBindings1[3].setColumnBinding("Last_Modified_User");
        colBindings1[3].setSortReqd(true);
        
		colBindings1[4] = new YRCTblClmBindingData();
		colBindings1[4].setName("clmAction");
        colBindings1[4].setAttributeBinding("@Action");
        colBindings1[4].setColumnBinding("Action");        
        colBindings1[4].setLabelProvider(new IYRCTableColumnTextProvider(){
	
			public String getColumnText(Element eleData) {
				return "Select";
			}
        	
        });
        colBindings1[4].setTargetAttributeBinding("XPEDXMyItemsList/@Action");
        cbd = new YRCComboBindingData();
		cbd.setCodeBinding("Id");
		cbd.setListBinding("Actions:Actions/Action");
		cbd.setDescriptionBinding("DisplayName");
		colBindings1[4].setBindingData(cbd);
		colBindings1[4].setSortReqd(true);
        
        IYRCCellModifier cellModifier = new IYRCCellModifier() {
			protected boolean allowModify(String property, String value, Element element) {
//				System.out.println("allowModify-->property:["+property+"]- value["+value+"]- element["+YRCXmlUtils.getString(element)+"]");
				return true;
			}

			protected int allowModifiedValue(String property, String value, Element element) {
				return IYRCCellModifier.MODIFY_OK;
			}

			protected String getModifiedValue(String property, String value, Element element) {
				
				if(YRCPlatformUI.equals(XPXMyItemsSearchListScreenBehavior.ACTION_EDIT, value)){
					// Open My Items List Detail Editor
					try {
						myBehavior.edit(element);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				} else if(YRCPlatformUI.equals(XPXMyItemsSearchListScreenBehavior.ACTION_DELETE, value)){
					if(YRCPlatformUI.getConfirmation("TITLE_KEY_Confirm_Delete", "MSG_KEY_Confirm_My_Items_List_Delete", element.getAttribute("Name"))){
						myBehavior.delete(element);
						myBehavior.search();
						YRCPlatformUI.showInformation("TITLE_KEY_Successfully_Deleted", "MSG_KEY_Deleted_My_Items_List_Successfully", element.getAttribute("Name"));
						myBehavior.getFirstPage();
					}
				}
				
//				System.out.println("getModifiedValue---->property:["+property+"]- value["+value+"]- element["+YRCXmlUtils.getString(element)+"]");
				// once the operation is performed returning blank in order to clear the drop down.
				return "";
			}

			@Override
			protected void handleCheckBoxSelection(boolean flag, String s, Element element1) {
				
				super.handleCheckBoxSelection(flag, s, element1);
			}
			
		};
		String[] editors = new String[tblResults.getColumnCount()];
		editors[4] = YRCConstants.YRC_COMBO_BOX_CELL_EDITOR;
		tblResultsBinding.setCellTypes(editors);
		tblResultsBinding.setCellModifierRequired(true);
		tblResultsBinding.setCellModifier(cellModifier);
		tblResultsBinding.setSortRequired(true);
		tblResultsBinding.setSourceBinding("XPEDXMyItemsListList:/XPEDXMyItemsListList/XPEDXMyItemsList");
        tblResultsBinding.setTargetBinding("SaveXPEDXMyItemsListList:/XPEDXMyItemsListList");
        tblResultsBinding.setName("tblResultz");
        tblResultsBinding.setTblClmBindings(colBindings1);
        tblResultsBinding.setKeyNavigationRequired(true);
        tblResultsBinding.setLinkProvider( new IYRCTableLinkProvider(){
        	public String getLinkTheme(Object element, int columnIndex) {
        			return "TableLink";
        	   	}
        	
        	public void linkSelected(Object element, int columnIndex) {
           		Element eleTableItem = (Element)element;
           	           			
           			try {
						myBehavior.edit(eleTableItem);
					} catch (PartInitException e) {
			
						e.printStackTrace();
					}
           	
           		return;
           	}
        });
        tblResults.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, tblResultsBinding);

        /************/
    }

    public IYRCPanelHolder getPanelHolder() {
        // TODO Complete getPanelHolder
        return null;
    }

	public String getHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	public XPXMyItemsSearchListScreenBehavior getMyBehavior() {
		return myBehavior;
	}
	
	public String getFormId() {
		return FORM_ID;
	}
	
	public Composite getRootPanel() {
		return pnlRoot;
	}

	public YRCPaginationData getPaginationData() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hide() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean resetSearchCriteria() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean search() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public XPXPaginationBehavior getPaginationBehavior() {
		// TODO Auto-generated method stub
		return getMyBehavior();
	}
}
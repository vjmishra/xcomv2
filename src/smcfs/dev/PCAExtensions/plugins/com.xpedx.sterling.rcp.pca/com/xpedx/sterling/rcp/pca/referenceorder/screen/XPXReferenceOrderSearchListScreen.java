package com.xpedx.sterling.rcp.pca.referenceorder.screen;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCDefaultServerImageProvider;
import com.yantra.yfc.rcp.IYRCPaginatedSearchAndListComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.IYRCTableColorProvider;
import com.yantra.yfc.rcp.IYRCTableColumnTextProvider;
import com.yantra.yfc.rcp.IYRCTableLinkProvider;
import com.yantra.yfc.rcp.YRCButtonBindingData;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPaginationData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;
import com.yantra.yfc.rcp.YRCXmlUtils;

/**
 * @author sdodda
 *
 * Articles Search and List Panel.
 */
public class XPXReferenceOrderSearchListScreen extends Composite  implements IYRCComposite,IYRCPaginatedSearchAndListComposite {

    public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.referenceorder.screen.XPXReferenceOrderSearchListScreen";
    private XPXReferenceOrderSearchListScreenBehavior myBehavior;

    private Composite pnlSearchTop;
    private Composite pnlSearchCriteriaTitle;
    private Composite pnlSearchCriteria;
    private Composite cmpstEnterpriseCode;
    private Composite pnlBasicSearchCriteriaBody;
    private Composite pnlBasicChkSearchCriteriaBody;
    private Composite cmpstSearchResultsAndControls;
    private Composite resultsPanel;
    private Composite pnlResultsHeader;
    private Composite pnlOrderSearchResults; 
    private Label lblSearchCriteriaTitle;
    private Label lblExpandCollapseIcon;
    private Label lblResultsTitle;
//	search fields declaration starts here
    private Label lblEnterprise;
    private Label lblETradingID;
	private Combo cmbEnterprise;
    private Text txtETradingID;
    private Label lblBuyerID;
    private Text txtBuyerID;
    private Label lblCustomerPO;
    private Text txtCustomerPO;
    private Label lblOrderDate;
    private Composite pnlOrderDate;
    private Label lblFromDate;
    private Text txtFromDate;
    private Button btnFromDateCalendar;
    private Label lblToDate;
    private Text txtToDate;
    private Button btnToDateCalendar;
    
	YRCButtonBindingData chkBoxBindingData = null;
	private Button chkIncludeMarkOdrComplete;
	private Button chkShowInvalidETradingIDOnly;
	
	private Label lblShowInvalidETradingIDOnly;
	private Label lblIncludeMarkOdrComplete;

    
    Table tblSearchResults = null;
    private TableColumn clmReprocess;
    private TableColumn clmMarkOrderComplete;
    private TableColumn clmCustName;
    private TableColumn clmETradingId;
    private TableColumn clmCustDivision;
    private TableColumn clmShipTo;
    private TableColumn clmOriginalOrder;
    private TableColumn clmReceivedDate;
    private TableColumn clmCustPO;
    
	private Composite pnlRoot = null;
	private Composite pnlButtons = null;
	private Button btnSearch = null;
	private Button btnReset = null;
	private Composite pnlTitleHolder = null;
	private Label lblTitleIcon = null;
	private Composite pnlTableHolder = null;
	private SelectionAdapter selectionAdapter;
	private Label lblCustomerName;
	private Text txtCustomerName;
	private Label lblCustomerDivision;
	private Text txtCustomerDivision;
	

	public XPXReferenceOrderSearchListScreen(Composite parent, int style, Object inputObject) {
		super(parent, style);

		selectionAdapter = new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				Widget ctrl = e.widget;
				String ctrlName = (String) ctrl.getData("name");
				if (!YRCPlatformUI.isVoid(ctrlName)){
					if (YRCPlatformUI.equals(ctrlName, "btnSearch"))
						myBehavior.search();
					else if (YRCPlatformUI.equals(ctrlName, "btnReset"))
						myBehavior.reset();
					else if(YRCPlatformUI.equals(ctrlName, "btnFromDateCalendar"))
						YRCPlatformUI.showCalendar(txtFromDate);
			        else if(YRCPlatformUI.equals(ctrlName, "btnToDateCalendar"))
			        	YRCPlatformUI.showCalendar(txtToDate);
				}
			}
		};
		initialize();
        setBindingForComponents();
        myBehavior = new XPXReferenceOrderSearchListScreenBehavior(this, FORM_ID,inputObject);
	}

	private void initialize() {
		setData("name", "referenceOrderSearchAndList");
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
		lblSearchCriteriaTitle.setText("Order_Search_Criteria");
		
//		lblExpandCollapseIcon = new Label(pnlSearchCriteriaTitle, SWT.LEFT);
//		lblExpandCollapseIcon.setImage(YRCPlatformUI.getImage("CollapseUp"));
//		GridData lblExpandCollapseIconlayoutData = new GridData();
//		lblExpandCollapseIconlayoutData.horizontalAlignment = 16777224;
//		lblExpandCollapseIconlayoutData.verticalAlignment = 16777216;
//		lblExpandCollapseIcon.setLayoutData(lblExpandCollapseIconlayoutData);
//		lblExpandCollapseIcon.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
//		lblExpandCollapseIcon.setText("lblExpandCollapseIcon");
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

		lblCustomerDivision = new Label(pnlBasicSearchCriteriaBody, SWT.LEFT);
		GridData lblCustomerDivisionlayoutData = new GridData();
		lblCustomerDivisionlayoutData.verticalAlignment = 16777216;
		lblCustomerDivision.setLayoutData(lblCustomerDivisionlayoutData);
		lblCustomerDivision.setText("Customer_Division");
		
		txtCustomerDivision = new Text(pnlBasicSearchCriteriaBody, SWT.BORDER);
		txtCustomerDivision.setData("name", "txtCustomerDivision");
		GridData txtCustomerDivisionlayoutData = new GridData();
		txtCustomerDivisionlayoutData.horizontalAlignment = 4;
		txtCustomerDivisionlayoutData.grabExcessHorizontalSpace = true;
		txtCustomerDivisionlayoutData.horizontalSpan = 2;
		txtCustomerDivision.setLayoutData(txtCustomerDivisionlayoutData);
		txtCustomerDivision.addSelectionListener(selectionAdapter);			
		
		lblCustomerName = new Label(pnlBasicSearchCriteriaBody, SWT.LEFT);
		GridData lblCustomerNamelayoutData = new GridData();
		lblCustomerNamelayoutData.verticalAlignment = 16777216;
		lblCustomerName.setLayoutData(lblCustomerNamelayoutData);
		lblCustomerName.setText("Customer_Name");
		
		txtCustomerName = new Text(pnlBasicSearchCriteriaBody, SWT.BORDER);
		txtCustomerName.setData("name", "txtCustomerName");
		GridData txtCustomerNamelayoutData = new GridData();
		txtCustomerNamelayoutData.horizontalAlignment = 4;
		txtCustomerNamelayoutData.grabExcessHorizontalSpace = true;
		txtCustomerNamelayoutData.horizontalSpan = 2;
		txtCustomerName.setLayoutData(txtCustomerNamelayoutData);
		txtCustomerName.addSelectionListener(selectionAdapter);		
		
		lblETradingID = new Label(pnlBasicSearchCriteriaBody, SWT.LEFT);
		GridData lblETradingIDlayoutData = new GridData();
		lblETradingIDlayoutData.verticalAlignment = 16777216;
		lblETradingID.setLayoutData(lblETradingIDlayoutData);
		lblETradingID.setText("eTrading ID Containing");
		
		txtETradingID = new Text(pnlBasicSearchCriteriaBody, SWT.BORDER);
		txtETradingID.setData("name", "txtETradingID");
		GridData txtETradingIDlayoutData = new GridData();
		txtETradingIDlayoutData.horizontalAlignment = 4;
		txtETradingIDlayoutData.grabExcessHorizontalSpace = true;
		txtETradingIDlayoutData.horizontalSpan = 2;
		txtETradingID.setLayoutData(txtETradingIDlayoutData);
		txtETradingID.addSelectionListener(selectionAdapter);
		
		lblCustomerPO = new Label(pnlBasicSearchCriteriaBody, SWT.LEFT);
		GridData lblCustomerPOlayoutData = new GridData();
		lblCustomerPOlayoutData.verticalAlignment = 16777216;
		lblCustomerPO.setLayoutData(lblCustomerPOlayoutData);
		lblCustomerPO.setText("Customer_PO");
		
		txtCustomerPO = new Text(pnlBasicSearchCriteriaBody, SWT.BORDER);
		txtCustomerPO.setData("name", "txtCustomerPO");
		GridData txtCustomerPOlayoutData = new GridData();
		txtCustomerPOlayoutData.horizontalAlignment = 4;
		txtCustomerPOlayoutData.grabExcessHorizontalSpace = true;
		txtCustomerPOlayoutData.horizontalSpan = 2;
		txtCustomerPO.setLayoutData(txtCustomerPOlayoutData);
		txtCustomerPO.addSelectionListener(selectionAdapter);
		
		lblOrderDate = new Label(pnlBasicSearchCriteriaBody, 16384);
        GridData lblOrderDatelayoutData = new GridData();
        lblOrderDatelayoutData.verticalAlignment = 1;
        lblOrderDate.setLayoutData(lblOrderDatelayoutData);
        lblOrderDate.setText("OS_Order_Date");
        this.createPnlOrderDate();
        
		pnlBasicChkSearchCriteriaBody = new Composite(pnlBasicSearchCriteriaBody, SWT.NONE);
		pnlBasicChkSearchCriteriaBody.setBackgroundMode(SWT.INHERIT_NONE);
	
		pnlBasicChkSearchCriteriaBody.setData(YRCConstants.YRC_CONTROL_NAME, "pnlBasicChkSearchCriteriaBody");
		
		GridData pnlBasicChkSearchCriteriaBodylayoutData = new GridData();
		pnlBasicChkSearchCriteriaBodylayoutData.horizontalAlignment = 4;
		pnlBasicChkSearchCriteriaBodylayoutData.verticalAlignment = 4;
		pnlBasicChkSearchCriteriaBody.setLayoutData(pnlBasicChkSearchCriteriaBodylayoutData);
		GridLayout pnlBasicChkSearchCriteriaBodylayout = new GridLayout(2, false);
		pnlBasicChkSearchCriteriaBody.setLayout(pnlBasicChkSearchCriteriaBodylayout);
        
        chkShowInvalidETradingIDOnly = new Button(pnlBasicChkSearchCriteriaBody, SWT.CHECK);
        chkShowInvalidETradingIDOnly.setVisible(true);
        chkShowInvalidETradingIDOnly.setData("yrc:customType", "Label");
		GridData showInvalidETradingIDlayoutData = new GridData();
		chkShowInvalidETradingIDOnly.setLayoutData(showInvalidETradingIDlayoutData);
		chkShowInvalidETradingIDOnly.setData("name", "chkShowInvalidETradingIDOnly");
		// chkShowInvalidETradingIDOnly.addSelectionListener(selectionAdapter);
		
		lblShowInvalidETradingIDOnly = new Label(pnlBasicChkSearchCriteriaBody, SWT.NONE);
		lblShowInvalidETradingIDOnly.setText("Show_eTrading_ID_errors_only");
		GridData lblShowInvalidETradingIDOnlylayoutData = new GridData();
		lblShowInvalidETradingIDOnly.setLayoutData(lblShowInvalidETradingIDOnlylayoutData);
		lblShowInvalidETradingIDOnly.setData("name", "lblShowInvalidETradingIDOnly");
		        
		chkIncludeMarkOdrComplete = new Button(pnlBasicChkSearchCriteriaBody, SWT.CHECK);
		chkIncludeMarkOdrComplete.setVisible(true);
		chkIncludeMarkOdrComplete.setData("yrc:customType", "Label");
		GridData markOrderCompletelayoutData = new GridData();
		chkIncludeMarkOdrComplete.setLayoutData(markOrderCompletelayoutData);
		chkIncludeMarkOdrComplete.setData("name", "chkIncludeMarkOdrComplete");
		// chkIncludeMarkOdrComplete.addSelectionListener(selectionAdapter);
		
		lblIncludeMarkOdrComplete = new Label(pnlBasicChkSearchCriteriaBody, SWT.NONE);
		lblIncludeMarkOdrComplete.setText("Include Marked Complete");
		GridData lblIncludeMarkOdrCompletelayoutData = new GridData();
		lblIncludeMarkOdrComplete.setLayoutData(lblIncludeMarkOdrCompletelayoutData);
		lblIncludeMarkOdrComplete.setData("name", "lblIncludeMarkOdrComplete");		
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
//		cmpstEnterpriseCodelayoutData.exclude = true;	
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
//		cmbEnterprise.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//					
//			}
//			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
//				
////				helper.widgetDefaultSelected(e, "cmbEnterprise");
//				
//			}
//		});
	}
	
	private void createPnlOrderDate()
    {
		pnlOrderDate = new Composite(pnlBasicSearchCriteriaBody, 0);
        pnlOrderDate.setBackgroundMode(0);
        pnlOrderDate.setData("name", "pnlOrderDate");
        GridData pnlOrderDatelayoutData = new GridData();
        pnlOrderDatelayoutData.horizontalAlignment = 4;
        pnlOrderDatelayoutData.verticalAlignment = 4;
        pnlOrderDatelayoutData.grabExcessHorizontalSpace = true;
        pnlOrderDatelayoutData.horizontalSpan = 2;
        pnlOrderDate.setLayoutData(pnlOrderDatelayoutData);
        GridLayout pnlOrderDatelayout = new GridLayout(3, false);
        pnlOrderDatelayout.horizontalSpacing = 2;
        pnlOrderDatelayout.verticalSpacing = 2;
        pnlOrderDatelayout.marginHeight = 0;
        pnlOrderDatelayout.marginWidth = 0;
        pnlOrderDate.setLayout(pnlOrderDatelayout);
        lblFromDate = new Label(pnlOrderDate, 16384);
        GridData lblFromDatelayoutData = new GridData();
        lblFromDatelayoutData.verticalAlignment = 16777216;
        lblFromDate.setLayoutData(lblFromDatelayoutData);
        lblFromDate.setText("OS_From");
        txtFromDate = new Text(pnlOrderDate, 2048);
        GridData txtFromDatelayoutData = new GridData();
        txtFromDatelayoutData.horizontalAlignment = 4;
        txtFromDatelayoutData.grabExcessHorizontalSpace = true;
        txtFromDate.setLayoutData(txtFromDatelayoutData);
        txtFromDate.addSelectionListener(selectionAdapter);
        btnFromDateCalendar = new Button(pnlOrderDate, 8);
        btnFromDateCalendar.setImage(YRCPlatformUI.getImage("DateLookup"));
        GridData btnFromDateCalendarlayoutData = new GridData();
        btnFromDateCalendarlayoutData.horizontalAlignment = 2;
        btnFromDateCalendarlayoutData.heightHint = 19;
        btnFromDateCalendarlayoutData.widthHint = 17;
        btnFromDateCalendar.setLayoutData(btnFromDateCalendarlayoutData);
        btnFromDateCalendar.addSelectionListener(selectionAdapter);
        lblToDate = new Label(pnlOrderDate, 16384);
        GridData lblToDatelayoutData = new GridData();
        lblToDatelayoutData.verticalAlignment = 16777216;
        lblToDate.setLayoutData(lblToDatelayoutData);
        lblToDate.setText("OS_To");
        txtToDate = new Text(pnlOrderDate, 2048);
        GridData txtToDatelayoutData = new GridData();
        txtToDatelayoutData.horizontalAlignment = 4;
        txtToDatelayoutData.grabExcessHorizontalSpace = true;
        txtToDate.setLayoutData(txtToDatelayoutData);
        txtToDate.addSelectionListener(selectionAdapter);
        btnToDateCalendar = new Button(pnlOrderDate, 8);
        btnToDateCalendar.setImage(YRCPlatformUI.getImage("DateLookup"));
        GridData btnToDateCalendarlayoutData = new GridData();
        btnToDateCalendarlayoutData.horizontalAlignment = 2;
        btnToDateCalendarlayoutData.heightHint = 19;
        btnToDateCalendarlayoutData.widthHint = 17;
        btnToDateCalendar.setLayoutData(btnToDateCalendarlayoutData);
        btnToDateCalendar.addSelectionListener(selectionAdapter);
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
		GridLayout pnlButtonslayout = new GridLayout(4, false);
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
		btnReset.setData("name","btnReset");
		GridData btnResetlayoutData = new GridData();
		btnResetlayoutData.horizontalAlignment = 2;
		btnResetlayoutData.heightHint = 25;
		btnResetlayoutData.widthHint = 80;
		btnReset.setLayoutData(btnResetlayoutData);
		btnReset.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "Button");
		btnReset.setText("Reset");
		btnReset.addSelectionListener(selectionAdapter);
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
		lblResultsTitle.setText("Reference_Order_List");
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
		
		clmReprocess = new TableColumn(tblSearchResults, SWT.LEFT);
		clmReprocess.setToolTipText("Reprocess");
		clmReprocess.setAlignment(SWT.LEFT);
		
		clmMarkOrderComplete = new TableColumn(tblSearchResults, SWT.LEFT);
		clmMarkOrderComplete.setToolTipText("Mark Order Complete");
		clmMarkOrderComplete.setAlignment(SWT.LEFT);
		
		clmETradingId = new TableColumn(tblSearchResults, SWT.LEFT);
		clmETradingId.setText("eTrading_ID");
		clmETradingId.setWidth(70);
		clmETradingId.setResizable(true);
		clmETradingId.setMoveable(false);
		
		
		clmCustPO = new TableColumn(tblSearchResults, SWT.LEFT);
		clmCustPO.setText("Customer_PO_NO");
		clmCustPO.setWidth(70);
		clmCustPO.setResizable(true);
		clmCustPO.setMoveable(false);
		
		clmCustName = new TableColumn(tblSearchResults, SWT.LEFT);
		clmCustName.setText("Customer_Name");
		clmCustName.setWidth(60);
		clmCustName.setResizable(true);
		clmCustName.setMoveable(false);		
		
		clmCustDivision = new TableColumn(tblSearchResults, SWT.LEFT);
		clmCustDivision.setText("Customer_Division_List");
		clmCustDivision.setWidth(70);
		clmCustDivision.setResizable(true);
		clmCustDivision.setMoveable(false);
		
		clmShipTo = new TableColumn(tblSearchResults, SWT.LEFT);
		clmShipTo.setText("Ship_To_List");
		clmShipTo.setWidth(80);
		clmShipTo.setResizable(true);
		clmShipTo.setMoveable(false);
		
		clmOriginalOrder = new TableColumn(tblSearchResults, SWT.LEFT);
		clmOriginalOrder.setText("Original_Order");
		clmOriginalOrder.setWidth(60);
		clmOriginalOrder.setResizable(true);
		clmOriginalOrder.setMoveable(false);
		
		clmReceivedDate = new TableColumn(tblSearchResults, SWT.LEFT);
		clmReceivedDate.setText("Received_On_List");
		clmReceivedDate.setWidth(80);
		clmReceivedDate.setResizable(true);
		clmReceivedDate.setMoveable(false);
		
	}

	private void setBindingForComponents() {
		
		YRCTextBindingData tbd = null;
		YRCComboBindingData cbd = null;
		YRCButtonBindingData bbd = null;
		
		cbd = new YRCComboBindingData();
        cbd.setName("cmbEnterprise");
        cbd.setSourceBinding("SearchCriteria:/XPXRefOrderHdr/@EnterpriseKey");
        cbd.setTargetBinding("SearchCriteria:/XPXRefOrderHdr/@EnterpriseKey");
        cbd.setCodeBinding("OrganizationKey");
        cbd.setListBinding("OrgList:OrganizationList/Organization");
        cbd.setDescriptionBinding("OrganizationCode");
        cmbEnterprise.setData("YRCComboBindingDefination", cbd);
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtETradingID");
        tbd.setTargetBinding("SearchCriteria:/XPXRefOrderHdr/@EtradingID");
        tbd.setSourceBinding("SearchCriteria:/XPXRefOrderHdr/@EtradingID");
        txtETradingID.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtCustomerDivision");
        tbd.setTargetBinding("SearchCriteria:/XPXRefOrderHdr/@CustomerDivision");
        tbd.setSourceBinding("SearchCriteria:/XPXRefOrderHdr/@CustomerDivision");
        txtCustomerDivision.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);        
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtCustomerName");
        tbd.setTargetBinding("SearchCriteria:/XPXRefOrderHdr/@CustomerName");
        tbd.setSourceBinding("SearchCriteria:/XPXRefOrderHdr/@CustomerName");
        txtCustomerName.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtCustomerPO");
        tbd.setTargetBinding("SearchCriteria:/XPXRefOrderHdr/@CustomerPONO");
        tbd.setSourceBinding("SearchCriteria:/XPXRefOrderHdr/@CustomerPONO");
        txtCustomerPO.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtFromDate");
        tbd.setTargetBinding("SearchCriteria:/XPXRefOrderHdr/@FromOrderDate");
        tbd.setSourceBinding("SearchCriteria:/XPXRefOrderHdr/@FromOrderDate");
        txtFromDate.setData("YRCTextBindingDefination", tbd);
        bbd = new YRCButtonBindingData();
        bbd.setName("btnFromDateCalendar");
        btnFromDateCalendar.setData("YRCButtonBindingDefination", bbd);
        btnFromDateCalendar.setCursor(new Cursor(btnFromDateCalendar.getDisplay(), 21));
        
        chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("");
		chkBoxBindingData.setSourceBinding("SearchCriteria:/XPXRefOrderHdr/@IsInvalidETradingID");
		chkBoxBindingData.setTargetBinding("SearchCriteria:/XPXRefOrderHdr/@IsInvalidETradingID");
		chkBoxBindingData.setName("chkShowInvalidETradingIDOnly");
		chkShowInvalidETradingIDOnly.setData("YRCButtonBindingDefination",chkBoxBindingData);
        
        chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("SearchCriteria:/XPXRefOrderHdr/@IsMarkOdrCompleteFlag");
		chkBoxBindingData.setTargetBinding("SearchCriteria:/XPXRefOrderHdr/@IsMarkOdrCompleteFlag");
		chkBoxBindingData.setName("chkIncludeMarkOdrComplete");
		chkIncludeMarkOdrComplete.setData("YRCButtonBindingDefination",chkBoxBindingData);

        
        tbd = new YRCTextBindingData();
        tbd.setName("txtToDate");
        tbd.setTargetBinding("SearchCriteria:/XPXRefOrderHdr/@ToOrderDate");
        tbd.setSourceBinding("SearchCriteria:/XPXRefOrderHdr/@ToOrderDate");
        txtToDate.setData("YRCTextBindingDefination", tbd);
        bbd = new YRCButtonBindingData();
        bbd.setName("btnToDateCalendar");
        btnToDateCalendar.setData("YRCButtonBindingDefination", bbd);
        btnToDateCalendar.setCursor(new Cursor(btnToDateCalendar.getDisplay(), 21));
        
        // Search Results Table bindings
        YRCTableBindingData tblSearchResultsbd = new YRCTableBindingData();
		tblSearchResultsbd.setName("tblSearchResults");
		tblSearchResultsbd.setSourceBinding("XPXRefOrderHdrList:XPXRefOrderHdrList/XPXRefOrderHdr");
	
		YRCTblClmBindingData tblSearchResultsClmBd[] = new YRCTblClmBindingData[9];
		int tblSearchResultsCounter=0;
		
		tblSearchResultsClmBd[tblSearchResultsCounter] = new YRCTblClmBindingData();
		tblSearchResultsClmBd[tblSearchResultsCounter].setName("clmReprocess");
		tblSearchResultsClmBd[tblSearchResultsCounter].setAttributeBinding("@EtradingID");
		tblSearchResultsClmBd[tblSearchResultsCounter].setLinkReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setIgnoreText(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setServerImageConfiguration(YRCConstants.IMAGE_SMALL);
		tblSearchResultsClmBd[tblSearchResultsCounter].setColumnBinding("Resubmit");
		tblSearchResultsClmBd[tblSearchResultsCounter].setDefaultServerImageProvider(new IYRCDefaultServerImageProvider(){
			public String getImageTheme(Object obj) {
				Element eleTableItem = (Element)obj;
				if(YRCPlatformUI.equals("Y", eleTableItem.getAttribute("IsReprocessibleFlag")))
					return "Refresh";
				else 
					return null;
			}
			
		});
		tblSearchResultsCounter++;
		
		tblSearchResultsClmBd[tblSearchResultsCounter] = new YRCTblClmBindingData();
		tblSearchResultsClmBd[tblSearchResultsCounter].setName("clmMarkOrderComplete");
		tblSearchResultsClmBd[tblSearchResultsCounter].setAttributeBinding("@IsMarkOdrCompleteFlag");
		tblSearchResultsClmBd[tblSearchResultsCounter].setLinkReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setLabelProvider(new IYRCTableColumnTextProvider(){

	
			public String getColumnText(Element obj) {
				Element eleTableItem = (Element)obj;
				if(YRCPlatformUI.equals("N", eleTableItem.getAttribute("IsMarkOdrCompleteFlag")))
					return "MarkComplete";
				else
					return "";
			}
			
		});
		tblSearchResultsCounter++;
		
		tblSearchResultsClmBd[tblSearchResultsCounter] = new YRCTblClmBindingData();
		tblSearchResultsClmBd[tblSearchResultsCounter].setName("clmCustName");
		tblSearchResultsClmBd[tblSearchResultsCounter].setAttributeBinding("@CustomerName");		
		tblSearchResultsClmBd[tblSearchResultsCounter].setFilterReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setSortReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setColumnBinding("Customer_Name_List");
		tblSearchResultsCounter++;
		
		tblSearchResultsClmBd[tblSearchResultsCounter] = new YRCTblClmBindingData();
		tblSearchResultsClmBd[tblSearchResultsCounter].setName("clmCustDivision");
		tblSearchResultsClmBd[tblSearchResultsCounter].setAttributeBinding("@CustomerDivision");		
		tblSearchResultsClmBd[tblSearchResultsCounter].setFilterReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setSortReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setColumnBinding("Customer_Division_List");
		tblSearchResultsCounter++;
		tblSearchResultsClmBd[tblSearchResultsCounter] = new YRCTblClmBindingData();
		tblSearchResultsClmBd[tblSearchResultsCounter].setName("clmETradingId");
		tblSearchResultsClmBd[tblSearchResultsCounter].setAttributeBinding("@EtradingID");		
		tblSearchResultsClmBd[tblSearchResultsCounter].setFilterReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setSortReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setColumnBinding("eTrading_ID");
		tblSearchResultsbd.setColorProvider(new IYRCTableColorProvider() {

			public String getColorTheme(Object element, int chkShowInvalidETradingIDOnly) {
				
				if(chkShowInvalidETradingIDOnly==4 && "Y".equals(YRCXmlUtils.getAttribute((Element) element, "IsInvalidETradingID")))
				{
					return "InvalidData";
				}
				return null;
			}

		});
		tblSearchResultsCounter++;
		tblSearchResultsClmBd[tblSearchResultsCounter] = new YRCTblClmBindingData();
		tblSearchResultsClmBd[tblSearchResultsCounter].setName("clmCustPO#");
		tblSearchResultsClmBd[tblSearchResultsCounter].setAttributeBinding("@CustomerPONO");		
		tblSearchResultsClmBd[tblSearchResultsCounter].setFilterReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setSortReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setColumnBinding("Customer_PO_NO");
		tblSearchResultsCounter++;
			
		tblSearchResultsClmBd[tblSearchResultsCounter] = new YRCTblClmBindingData();
		tblSearchResultsClmBd[tblSearchResultsCounter].setName("clmShipTo");
		tblSearchResultsClmBd[tblSearchResultsCounter].setAttributeBinding("@ShipToName");		
		tblSearchResultsClmBd[tblSearchResultsCounter].setFilterReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setSortReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setColumnBinding("Ship_To_List");
		
		tblSearchResultsCounter++;
		
		tblSearchResultsClmBd[tblSearchResultsCounter] = new YRCTblClmBindingData();
		tblSearchResultsClmBd[tblSearchResultsCounter].setName("clmReceivedDate");
		tblSearchResultsClmBd[tblSearchResultsCounter].setAttributeBinding("@Createts");		
		tblSearchResultsClmBd[tblSearchResultsCounter].setFilterReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setSortReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setColumnBinding("Received_On_List");
		tblSearchResultsClmBd[tblSearchResultsCounter].setDataType("OrderDate");
		
		tblSearchResultsClmBd[tblSearchResultsCounter].setLabelProvider(new IYRCTableColumnTextProvider(){

			public String getColumnText(Element obj) {
				Element eleTableItem = (Element)obj;
				String dateReturn="";
				String dateTmp=eleTableItem.getAttribute("OrderDate");
			    SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			    Date date=new Date();
				try {
					date = sdfSource.parse(dateTmp);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy/MM/dd hh:mm aaa");
			    dateReturn=sdfDestination.format(date);
				    
				return dateReturn;
			}
			
		});
		tblSearchResultsCounter++;
		
		tblSearchResultsClmBd[tblSearchResultsCounter] = new YRCTblClmBindingData();
		tblSearchResultsClmBd[tblSearchResultsCounter].setName("clmOriginalOrder");
		tblSearchResultsClmBd[tblSearchResultsCounter].setFilterReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setLinkReqd(true);
		tblSearchResultsClmBd[tblSearchResultsCounter].setColumnBinding("Original_Order");
		tblSearchResultsClmBd[tblSearchResultsCounter].setLabelProvider(new IYRCTableColumnTextProvider(){

			@Override
			public String getColumnText(Element element) {
			//	Element eleTableItem = (Element)element;
				return "View";
			}
			
		});
		
		tblSearchResultsCounter++;
		
		tblSearchResultsbd.setLinkProvider(new IYRCTableLinkProvider(){
        	public String getLinkTheme(Object element, int columnIndex) {
        		Element eleTableItem = (Element)element;
        		if((columnIndex == 0 && !YRCPlatformUI.equals("Y", eleTableItem.getAttribute("IsReprocessable"))) 
        				|| (columnIndex == 1 && !YRCPlatformUI.equals("Y", eleTableItem.getAttribute("IsMarkedComplete")))||columnIndex == 8){
        			return "TableLink";
        		}
        		return null;
        	}
        	
        	public void linkSelected(Object element, int columnIndex) {
        		Element eleTableItem = (Element)element;
        		if(columnIndex == 0 && !YRCPlatformUI.equals("N", eleTableItem.getAttribute("IsReprocessibleFlag"))&& !YRCPlatformUI.equals("Y", eleTableItem.getAttribute("IsMarkOdrCompleteFlag"))){
        			myBehavior.reprocessRefOrder(eleTableItem);
        			return ;
        		} else if(columnIndex == 1 && !YRCPlatformUI.equals("Y", eleTableItem.getAttribute("IsMarkOdrCompleteFlag"))&&!YRCPlatformUI.equals("N", eleTableItem.getAttribute("IsReprocessibleFlag"))){
        			myBehavior.markRefOrderComplete(eleTableItem);
        			return ;
        		} 
        		else if(columnIndex == 8){
        			TableItem tblItems[] = tblSearchResults.getSelection();
        			myBehavior.openMultipleOrders(tblItems);
        		}
        	}
        });


//		tblSearchResultsbd.setImageProvider(new IYRCTableImageProvider()	{
//
//			public String getImageThemeForColumn(Object element, int columnIndex) {
//				Element eleTableItem = (Element)element;
//				if(columnIndex == 4 && !YRCPlatformUI.equals("Y", eleTableItem.getAttribute("IsReprocessable"))){
//					return "Copy";
//				}
//				return null;
//			}
//        	
//        }
//        );
		 
		tblSearchResultsbd.setTblClmBindings(tblSearchResultsClmBd) ;
		tblSearchResultsbd.setSortRequired(true);
		tblSearchResultsbd.setDefaultSort(true);
		tblSearchResultsbd.setSortAttributeBinding("@Createts");
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

	public XPXReferenceOrderSearchListScreenBehavior getMyBehavior() {
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
}
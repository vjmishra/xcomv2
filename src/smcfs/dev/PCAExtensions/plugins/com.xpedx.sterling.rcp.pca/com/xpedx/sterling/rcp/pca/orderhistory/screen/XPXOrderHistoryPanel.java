/*
 * Created on April 17,2012
 *
 */
package com.xpedx.sterling.rcp.pca.orderhistory.screen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

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
import org.eclipse.ui.PartInitException;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.orderhistory.screen.XPXOrderHistoryPanelBehavior;
import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.xpedx.sterling.rcp.pca.util.XPXPaginationBehavior;
import com.xpedx.sterling.rcp.pca.util.XPXPaginationComposite;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCCellModifier;
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

//extends XPXPaginationComposite  implements IYRCComposite
public class XPXOrderHistoryPanel extends XPXPaginationComposite  implements IYRCComposite,IYRCPaginatedSearchAndListComposite {


    public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.orderhistory.screen.XPXOrderHistoryPanel";
    private XPXOrderHistoryPanelBehavior myBehavior;

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
    private Label lblExpandCollapseIcon;
    private Label lblResultsTitle;
//	search fields declaration starts here
    private Label lblSearchBy;
    private Label lblOrderType;
    private Label lblOrderStatus;
    private Label lblWillCall;
    private Label lblAttention;
    private Label lblRushOrder;
    private Label lblWillCallText;
    private Label lblAttentionText;
    private Label lblRushOrderText;
    private Label lblCompany;
    private Label lblAccount;
    private Label lblSuffix;
    private Label lblShipFrom;
    private Text txtCompany;
    private Text txtAccount;
    private Text txtSuffix;
    private Text txtShipFrom;
    private Text txtOrderStatus;
    private Text txtSearchButton;
    private Text txtClearButton;
	private Combo cmbSearchBy;
	private Combo cmbOrderStatus;
	private Combo cmbOrderType;
    private Label lblBuyerID;
    private Text txtSearchBy;
    private Text txtOrderType;
    private Label lblOrderDate;
    private Label lblDateFormat;
    private Composite pnlOrderDate;
    private Composite pnlIcons;
    private Composite pnlFilter;
    private Composite pnlSearch;
    private Composite pnlStatus;
    private Label lblFromDate;
    private Text txtFromDate;
    private Button btnFromDateCalendar;
    private Label lblToDate;
    private Text txtToDate;
    private Button btnToDateCalendar;
    
	YRCButtonBindingData chkBoxBindingData = null;

    
    Table tblSearchResults = null;
    
    private TableColumn clmStatus;
    private TableColumn clmWebConfirmation;
    private TableColumn clmOrderNum;
    private TableColumn clmPONum;
    private TableColumn clmBillTo;
    private TableColumn clmShipToName;
    private TableColumn clmOrderedBy;
    private TableColumn clmOrdered;
    private TableColumn clmAmount;
    private TableColumn clmShipDate;
    private TableColumn clmCompany;
    private TableColumn clmWarehouse;
    private TableColumn clmOrderType;
	private Composite pnlRoot = null;
	private Composite pnlButtons = null;
	private Button btnSearch = null;
	private Button btnClear = null;
	private Composite pnlTitleHolder = null;
	private Label lblTitleIcon = null;
	private Composite pnlTableHolder = null;
	private SelectionAdapter selectionAdapter;
	

	public XPXOrderHistoryPanel(Composite parent, int style, Object inputObject) {
		super(parent, style);

		selectionAdapter = new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				Widget ctrl = e.widget;
				String ctrlName = (String) ctrl.getData("name");
				if (!YRCPlatformUI.isVoid(ctrlName)){
					if (YRCPlatformUI.equals(ctrlName, "btnSearch"))
						//myBehavior.search();
						myBehavior.getFirstPage();
					else if (YRCPlatformUI.equals(ctrlName, "btnClear"))
						myBehavior.reset();
					else if(YRCPlatformUI.equals(ctrlName, "btnFromDateCalendar"))
						YRCPlatformUI.showCalendar(txtFromDate);
			        else if(YRCPlatformUI.equals(ctrlName, "btnToDateCalendar"))
			        	YRCPlatformUI.showCalendar(txtToDate);
			        else if(YRCPlatformUI.equals(ctrlName, "cmbSearchBy")){
			        	String searchByValue = cmbSearchBy.getText();
			        	if(searchByValue == null || searchByValue == ""){
			        		txtSearchBy.setVisible(false);
			        	}
			        	else{
			        		txtSearchBy.setVisible(true);
			        	}
			        }
			        	
				}
			}
		};
		initialize();
        setBindingForComponents();
        setBindingORDERist();
        myBehavior = new XPXOrderHistoryPanelBehavior(this, FORM_ID,inputObject);
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
		
	}

	
	private void createPnlSearchCriteria() {
		pnlSearchCriteria = new Composite(pnlSearchTop, 2);
        pnlSearchCriteria.setBackgroundMode(0);
        pnlSearchCriteria.setData("name", "pnlSearchCriteria");
        GridData pnlSearchCriterialayoutData = new GridData();
        pnlSearchCriterialayoutData.horizontalAlignment = 4;
        pnlSearchCriterialayoutData.verticalAlignment = 4;
        pnlSearchCriterialayoutData.grabExcessHorizontalSpace = true;
        pnlSearchCriterialayoutData.grabExcessVerticalSpace = true;
        pnlSearchCriteria.setLayoutData(pnlSearchCriterialayoutData);
        GridLayout pnlSearchCriterialayout = new GridLayout(3, false);
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
		pnlBasicSearchCriteriaBody.setLayoutData(pnlBasicSearchCriteriaBodylayoutData);
		GridLayout pnlBasicSearchCriteriaBodylayout = new GridLayout(4, false);
		pnlBasicSearchCriteriaBody.setLayout(pnlBasicSearchCriteriaBodylayout);			
		
		lblOrderType = new Label(pnlBasicSearchCriteriaBody, 16384);
		GridData lblOrderTypelayoutData = new GridData();
		lblOrderTypelayoutData.verticalAlignment = 1;
		lblOrderType.setLayoutData(lblOrderTypelayoutData);
		lblOrderType.setText("Order Type:");
		
		cmbOrderType = new Combo(pnlBasicSearchCriteriaBody, SWT.READ_ONLY);
		GridData cmbOrderTypelayoutData = new GridData();
		cmbOrderTypelayoutData.verticalAlignment = 1;
		cmbOrderTypelayoutData.horizontalSpan = 2;
		cmbOrderTypelayoutData.horizontalAlignment = 4;
		cmbOrderTypelayoutData.grabExcessVerticalSpace = true;
		cmbOrderTypelayoutData.grabExcessHorizontalSpace = true;
		cmbOrderType.setLayoutData(cmbOrderTypelayoutData);
		
		txtOrderType = new Text(pnlBasicSearchCriteriaBody, SWT.BORDER);
		GridData txtOrderTypelayoutData = new GridData();
		txtOrderTypelayoutData.horizontalAlignment = 4;
		txtOrderTypelayoutData.grabExcessHorizontalSpace = true;
		txtOrderType.setLayoutData(txtOrderTypelayoutData);
		txtOrderType.setData("name", "txtOrderType");
		txtOrderType.setVisible(false);
		
		this.createPnlFilter();
        
        
	}

	private void createCmpstEnterpriseCode(){
		
		cmpstEnterpriseCode = new Composite(pnlSearchCriteria, SWT.NONE);
		cmpstEnterpriseCode.setBackgroundMode(SWT.INHERIT_NONE);
		cmpstEnterpriseCode.setData(YRCConstants.YRC_CONTROL_NAME, "cmpstEnterpriseCode");
		GridData cmpstEnterpriseCodelayoutData = new GridData();
		cmpstEnterpriseCodelayoutData.horizontalAlignment = 4;
		cmpstEnterpriseCodelayoutData.verticalAlignment = 4;
		cmpstEnterpriseCodelayoutData.grabExcessHorizontalSpace = true;
		cmpstEnterpriseCodelayoutData.grabExcessVerticalSpace = true;
		cmpstEnterpriseCodelayoutData.horizontalSpan = 2;
//		cmpstEnterpriseCodelayoutData.exclude = true;	
		cmpstEnterpriseCode.setLayoutData(cmpstEnterpriseCodelayoutData);
		GridLayout cmpstEnterpriseCodelayout = new GridLayout(3, false);
		cmpstEnterpriseCodelayout.marginHeight = 0;
		cmpstEnterpriseCode.setLayout(cmpstEnterpriseCodelayout);
		
		
		lblSearchBy = new Label(cmpstEnterpriseCode, SWT.LEFT);
		GridData lblSearchBylayoutData = new GridData();
		lblSearchBylayoutData.horizontalAlignment = 4;
		lblSearchBylayoutData.verticalAlignment = 16777216; 
		lblSearchBy.setLayoutData(lblSearchBylayoutData);
		lblSearchBy.setText("Search By:");
		
		cmbSearchBy = new Combo(cmpstEnterpriseCode, SWT.READ_ONLY);
		GridData cmbSearchBylayoutData = new GridData();
		cmbSearchBylayoutData.horizontalAlignment = 4;
		cmbSearchBy.setLayoutData(cmbSearchBylayoutData);
		cmbSearchBy.addSelectionListener(selectionAdapter);
		
		txtSearchBy = new Text(cmpstEnterpriseCode, SWT.BORDER);
		GridData txtSearchBylayoutData = new GridData();
		txtSearchBylayoutData.horizontalAlignment = 4;
		txtSearchBylayoutData.grabExcessHorizontalSpace = true;
		txtSearchBy.setLayoutData(txtSearchBylayoutData);
		txtSearchBy.setData("name", "txtSearchBy");
		txtSearchBy.setVisible(false);
		
		lblOrderStatus = new Label(cmpstEnterpriseCode, SWT.LEFT);
		GridData lblOrderStatuslayoutData = new GridData();
		lblOrderStatuslayoutData.horizontalAlignment = 4;
		lblOrderStatuslayoutData.verticalAlignment = 16777216;
		lblOrderStatus.setLayoutData(lblOrderStatuslayoutData);
		lblOrderStatus.setText("Order Status:");
		
		cmbOrderStatus = new Combo(cmpstEnterpriseCode, SWT.READ_ONLY);
		GridData cmbOrderStatuslayoutData = new GridData();
		cmbOrderStatuslayoutData.horizontalAlignment = 4;
		cmbOrderStatus.setLayoutData(cmbOrderStatuslayoutData);
		
		txtOrderStatus = new Text(cmpstEnterpriseCode, SWT.BORDER);
		GridData txtOrderStatuslayoutData = new GridData();
		txtOrderStatuslayoutData.horizontalAlignment = 4;
		txtOrderStatuslayoutData.grabExcessHorizontalSpace = true;
		txtOrderStatus.setLayoutData(txtSearchBylayoutData);
		txtOrderStatus.setData("name", "txtOrderStatus");
		txtOrderStatus.setVisible(false);
		
		lblOrderDate = new Label(cmpstEnterpriseCode, SWT.LEFT);
        GridData lblOrderDatelayoutData = new GridData();
        lblOrderDatelayoutData.verticalAlignment = 16777216;
        lblOrderDate.setLayoutData(lblOrderDatelayoutData);
        lblOrderDate.setText("OS_Order_Date");
        this.createPnlOrderDate();
        
        this.createPnlIcons();

	}
	
	private void createPnlOrderDate()
    {
		pnlOrderDate = new Composite(cmpstEnterpriseCode, 0);
        pnlOrderDate.setBackgroundMode(0);
        pnlOrderDate.setData("name", "pnlOrderDate");
        GridData pnlOrderDatelayoutData = new GridData();
        pnlOrderDatelayoutData.horizontalAlignment = 4;
        pnlOrderDatelayoutData.verticalAlignment = 4;
        pnlOrderDatelayoutData.grabExcessHorizontalSpace = true;
        pnlOrderDatelayoutData.horizontalSpan = 2;
        pnlOrderDate.setLayoutData(pnlOrderDatelayoutData);
        GridLayout pnlOrderDatelayout = new GridLayout(7, false);
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
        txtFromDatelayoutData.grabExcessHorizontalSpace = false;
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
        txtToDatelayoutData.grabExcessHorizontalSpace = false;
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
        lblDateFormat = new Label(pnlOrderDate, 16384);
        GridData lblDateFormatlayoutData = new GridData();
        lblDateFormatlayoutData.verticalAlignment = 16777216;
        lblDateFormat.setLayoutData(lblDateFormatlayoutData);
        lblDateFormat.setText("(mm/dd/yyyy)");
        
    }
	
	private void createPnlFilter()
    {
		pnlFilter = new Composite(pnlBasicSearchCriteriaBody, 0);
		pnlFilter.setBackgroundMode(0);
		pnlFilter.setData("name", "pnlFilter");
        GridData pnlFilterlayoutData = new GridData();
        pnlFilterlayoutData.horizontalAlignment = 4;
        pnlFilterlayoutData.verticalAlignment = 4;
        pnlFilterlayoutData.grabExcessHorizontalSpace = true;
        pnlFilterlayoutData.horizontalSpan = 4;
        pnlFilter.setLayoutData(pnlFilterlayoutData);
        GridLayout pnlFilterlayout = new GridLayout(4, false);
        pnlFilterlayout.horizontalSpacing = 2;
        pnlFilterlayout.verticalSpacing = 2;
        pnlFilterlayout.marginHeight = 0;
        pnlFilterlayout.marginWidth = 0;
        pnlFilter.setLayout(pnlFilterlayout);
        
        lblCompany = new Label(pnlFilter, SWT.LEFT);
		GridData lblCompanylayoutData = new GridData();
		lblCompanylayoutData.horizontalAlignment = 4;
		lblCompanylayoutData.verticalAlignment = 16777216; 
		lblCompany.setLayoutData(lblCompanylayoutData);
		lblCompany.setText("Company");
		
		lblAccount = new Label(pnlFilter, SWT.LEFT);
		GridData lblAccountlayoutData = new GridData();
		lblAccountlayoutData.horizontalAlignment = 4;
		lblAccountlayoutData.verticalAlignment = 16777216; 
		lblAccount.setLayoutData(lblAccountlayoutData);
		lblAccount.setText("Bill-To #");
		
		lblSuffix = new Label(pnlFilter, SWT.LEFT);
		GridData lblSuffixlayoutData = new GridData();
		lblSuffixlayoutData.horizontalAlignment = 4;
		lblSuffixlayoutData.verticalAlignment = 16777216; 
		lblSuffix.setLayoutData(lblSuffixlayoutData);
		lblSuffix.setText("Ship-To Suffix #");
		
		lblShipFrom = new Label(pnlFilter, SWT.LEFT);
		GridData lblShipFromlayoutData = new GridData();
		lblShipFromlayoutData.horizontalAlignment = 4;
		lblShipFromlayoutData.verticalAlignment = 16777216; 
		lblShipFrom.setLayoutData(lblShipFromlayoutData);
		lblShipFrom.setText("Warehouse");
        
		txtCompany = new Text(pnlFilter, SWT.BORDER);
		GridData txtCompanylayoutData = new GridData();
		txtCompanylayoutData.horizontalAlignment = 4;
		txtCompanylayoutData.grabExcessHorizontalSpace = true;
		txtCompany.setLayoutData(txtCompanylayoutData);
		txtCompany.setData("name", "txtCompany");
		
		txtAccount = new Text(pnlFilter, SWT.BORDER);
		GridData txtAccountlayoutData = new GridData();
		txtAccountlayoutData.horizontalAlignment = 4;
		txtAccountlayoutData.grabExcessHorizontalSpace = true;
		txtAccount.setLayoutData(txtAccountlayoutData);
		txtAccount.setData("name", "txtAccount");
		
		txtSuffix = new Text(pnlFilter, SWT.BORDER);
		GridData txtSuffixlayoutData = new GridData();
		txtSuffixlayoutData.horizontalAlignment = 4;
		txtSuffixlayoutData.grabExcessHorizontalSpace = true;
		txtSuffix.setLayoutData(txtSuffixlayoutData);
		txtSuffix.setData("name", "txtSuffix");
		
		txtShipFrom = new Text(pnlFilter, SWT.BORDER);
		GridData txtShipFromlayoutData = new GridData();
		txtShipFromlayoutData.horizontalAlignment = 4;
		txtShipFromlayoutData.grabExcessHorizontalSpace = true;
		txtShipFrom.setLayoutData(txtShipFromlayoutData);
		txtShipFrom.setData("name", "txtShipFrom");
		this.createPnlSearch();
    }
	
	private void createPnlSearch(){
		pnlSearch = new Composite(pnlBasicSearchCriteriaBody, SWT.None);
		pnlSearch.setBackgroundMode(0);
		pnlSearch.setData("name", "pnlSearch");
		GridData pnlSearchlayoutData = new GridData();
		pnlSearchlayoutData.horizontalAlignment = 4;
		pnlSearchlayoutData.verticalAlignment = 4;
		pnlSearchlayoutData.grabExcessHorizontalSpace = true;
		pnlSearchlayoutData.horizontalSpan = 4;
		pnlSearch.setLayoutData(pnlSearchlayoutData);
		GridLayout pnlSearchlayout = new GridLayout(4, false);
		pnlSearchlayout.marginHeight = 2;
		pnlSearchlayout.marginWidth = 0;
		pnlSearch.setLayout(pnlSearchlayout);
//		createPnlShowHideCriteria();
//		createPnlOrderByOptions();
		
		
		txtClearButton = new Text(pnlSearch, SWT.BORDER);
		GridData txtClearButtonlayoutData = new GridData();
		txtClearButtonlayoutData.horizontalAlignment = 4;
		txtClearButtonlayoutData.grabExcessHorizontalSpace = true;
		txtClearButton.setLayoutData(txtClearButtonlayoutData);
		txtClearButton.setData("name", "txtClearButton");
		txtClearButton.setVisible(false);
		
		txtSearchButton = new Text(pnlSearch, SWT.BORDER);
		GridData txtSearchButtonlayoutData = new GridData();
		txtSearchButtonlayoutData.horizontalAlignment = 4;
		txtSearchButtonlayoutData.grabExcessHorizontalSpace = true;
		txtSearchButton.setLayoutData(txtSearchButtonlayoutData);
		txtSearchButton.setData("name", "txtSearchButton");
		txtSearchButton.setVisible(false);
		
		
		btnClear = new Button(pnlSearch, SWT.PUSH);
		btnClear.setData("name","btnClear");
		GridData btnClearlayoutData = new GridData();
		btnClearlayoutData.horizontalAlignment = 4;
		btnClearlayoutData.heightHint = 25;
		btnClearlayoutData.widthHint = 80;
		btnClear.setLayoutData(btnClearlayoutData);
		btnClear.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "Button");
		btnClear.setText("Clear");
		btnClear.addSelectionListener(selectionAdapter);
		
		btnSearch = new Button(pnlSearch, SWT.PUSH);
		btnSearch.setData("name","btnSearch");
		GridData btnSearchlayoutData = new GridData();
		btnSearchlayoutData.horizontalAlignment = 4;
		btnSearchlayoutData.heightHint = 25;
		btnSearchlayoutData.widthHint = 80;
		btnSearch.setLayoutData(btnSearchlayoutData);
		btnSearch.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "Button");
		btnSearch.setText("Search");
		btnSearch.addSelectionListener(selectionAdapter);
	}
	
	private void createPnlIcons(){
		pnlIcons = new Composite(cmpstEnterpriseCode, 0);
		pnlIcons.setBackgroundMode(0);
		pnlIcons.setData("name", "pnlIcons");
        GridData pnlIconslayoutData = new GridData();
        pnlIconslayoutData.horizontalAlignment = 4;
        pnlIconslayoutData.verticalAlignment = 4;
        pnlIconslayoutData.grabExcessHorizontalSpace = true;
        pnlIconslayoutData.horizontalSpan = 2;
        pnlIcons.setLayoutData(pnlIconslayoutData);
        GridLayout pnlIconslayout = new GridLayout(2, false);
        pnlIconslayout.horizontalSpacing = 2;
        pnlIconslayout.verticalSpacing = 2;
        pnlIconslayout.marginHeight = 0;
        pnlIconslayout.marginWidth = 0;
        pnlIcons.setLayout(pnlIconslayout);
		
		lblWillCall = new Label(pnlIcons, 16384);
        GridData lblWillCalllayoutData = new GridData();
        lblWillCalllayoutData.verticalAlignment = 1;
        lblWillCall.setLayoutData(lblWillCalllayoutData);
        lblWillCall.setImage(YRCPlatformUI.getImage("WillCall"));
        
        lblWillCallText = new Label(pnlIcons, 16384);
        GridData lblWillCallTextlayoutData = new GridData();
        lblWillCallTextlayoutData.verticalAlignment = 1;
        lblWillCallText.setLayoutData(lblWillCallTextlayoutData);
        lblWillCallText.setText("Will Call");
        
        lblAttention = new Label(pnlIcons, 16384);
        GridData lblAttentionlayoutData = new GridData();
        lblAttentionlayoutData.verticalAlignment = 1;
        lblAttention.setLayoutData(lblAttentionlayoutData);
        lblAttention.setImage(YRCPlatformUI.getImage("NeedsAttention"));
        
        lblAttentionText = new Label(pnlIcons, 16384);
        GridData lblAttentionTextlayoutData = new GridData();
        lblAttentionTextlayoutData.verticalAlignment = 1;
        lblAttentionText.setLayoutData(lblAttentionTextlayoutData);
        lblAttentionText.setText("Attention");
        
        lblRushOrder = new Label(pnlIcons, 16384);
        GridData lblRushOrderlayoutData = new GridData();
        lblRushOrderlayoutData.verticalAlignment = 1;
        lblRushOrder.setLayoutData(lblRushOrderlayoutData);
        lblRushOrder.setImage(YRCPlatformUI.getImage("RushOrder"));
        
        lblRushOrderText = new Label(pnlIcons, 16384);
        GridData lblRushOrderTextlayoutData = new GridData();
        lblRushOrderTextlayoutData.verticalAlignment = 1;
        lblRushOrderText.setLayoutData(lblRushOrderTextlayoutData);
        lblRushOrderText.setText("Rush Order");
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
		createPaginationLinks(pnlOrderSearchResults, myBehavior.PAGINATION_STRATEGY_FOR_MIL_REPTOOL_SEARCH);
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
		
		clmStatus = new TableColumn(tblSearchResults, SWT.LEFT);
		clmStatus.setText("Status");
		clmStatus.setWidth(80);
		clmStatus.setResizable(true);
		clmStatus.setMoveable(false);
		
		clmOrderType = new TableColumn(tblSearchResults, SWT.LEFT);
		clmOrderType.setText("Order Type");
		clmOrderType.setWidth(80);
		clmOrderType.setResizable(true);
		clmOrderType.setMoveable(false);
		
		clmWebConfirmation = new TableColumn(tblSearchResults, SWT.LEFT);
		clmWebConfirmation.setText("Web Confirmation");
		clmWebConfirmation.setWidth(80);
		clmWebConfirmation.setResizable(true);
		clmWebConfirmation.setMoveable(false);
		
		clmOrderNum = new TableColumn(tblSearchResults, SWT.LEFT);
		clmOrderNum.setText("Order #");
		clmOrderNum.setWidth(80);
		clmOrderNum.setResizable(true);
		clmOrderNum.setMoveable(false);
		
		clmPONum = new TableColumn(tblSearchResults, SWT.LEFT);
		clmPONum.setText("PO #");
		clmPONum.setWidth(80);
		clmPONum.setResizable(true);
		clmPONum.setMoveable(false);
		
		clmCompany = new TableColumn(tblSearchResults, SWT.LEFT);
		clmCompany.setText("Company");
		clmCompany.setWidth(120);
		clmCompany.setResizable(true);
		clmCompany.setMoveable(false);
		
		clmWarehouse = new TableColumn(tblSearchResults, SWT.LEFT);
		clmWarehouse.setText("Warehouse");
		clmWarehouse.setWidth(80);
		clmWarehouse.setResizable(true);
		clmWarehouse.setMoveable(false);
		
		clmBillTo = new TableColumn(tblSearchResults, SWT.LEFT);
		clmBillTo.setText("Bill-To #");
		clmBillTo.setWidth(60);
		clmBillTo.setResizable(true);
		clmBillTo.setMoveable(false);
		
		clmShipToName = new TableColumn(tblSearchResults, SWT.LEFT);
		clmShipToName.setText("Ship-To Name");
		clmShipToName.setWidth(80);
		clmShipToName.setResizable(true);
		clmShipToName.setMoveable(false);
		
		clmOrderedBy = new TableColumn(tblSearchResults, SWT.LEFT);
		clmOrderedBy.setText("Ordered By");
		clmOrderedBy.setWidth(80);
		clmOrderedBy.setResizable(true);
		clmOrderedBy.setMoveable(false);
		
		clmOrdered = new TableColumn(tblSearchResults, SWT.LEFT);
		clmOrdered.setText("Ordered");
		clmOrdered.setWidth(80);
		clmOrdered.setResizable(true);
		clmOrdered.setMoveable(false);
		
		clmAmount = new TableColumn(tblSearchResults, SWT.LEFT);
		clmAmount.setText("Amount");
		clmAmount.setWidth(80);
		clmAmount.setResizable(true);
		clmAmount.setMoveable(false);
		
		clmShipDate = new TableColumn(tblSearchResults, SWT.LEFT);
		clmShipDate.setText("Ship Date");
		clmShipDate.setWidth(80);
		clmShipDate.setResizable(true);
		clmShipDate.setMoveable(false);
		
		
	}

	private void setBindingForComponents() {
		
		YRCTextBindingData tbd = null;
		YRCComboBindingData cbd = null;
		YRCButtonBindingData bbd = null;
        
        cbd = new YRCComboBindingData();
        cbd.setName("cmbSearchBy");
        cbd.setSourceBinding("");
        cbd.setTargetBinding("");
        cbd.setCodeBinding("SearchByValue");
        cbd.setListBinding("SearchBy:/Search");
        cbd.setDescriptionBinding("SearchByDescription");
        cmbSearchBy.setData("YRCComboBindingDefination", cbd);
        
        cbd = new YRCComboBindingData();
        cbd.setName("cmbOrderType");
        cbd.setSourceBinding("");
        cbd.setTargetBinding("");
        cbd.setCodeBinding("OrderTypeValue");
        cbd.setListBinding("OrderType:/OrdType");
        cbd.setDescriptionBinding("OrderTypeDescription");
        cmbOrderType.setData("YRCComboBindingDefination", cbd);
        
        cbd = new YRCComboBindingData();
        cbd.setName("cmbOrderStatus");
        cbd.setSourceBinding("");
        cbd.setTargetBinding("");
        cbd.setCodeBinding("OrderStatusValue");
        cbd.setListBinding("OrderStatus:/OrdStatus");
        cbd.setDescriptionBinding("OrderStatusDescription");        
        cmbOrderStatus.setData("YRCComboBindingDefination", cbd);
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtFromDate");
        tbd.setTargetBinding("SearchCriteria:/XPXRefOrderHdr/@FromOrderDate");
        tbd.setSourceBinding("SearchCriteria:/XPXRefOrderHdr/@FromOrderDate");
        txtFromDate.setData("YRCTextBindingDefination", tbd);
        bbd = new YRCButtonBindingData();
        bbd.setName("btnFromDateCalendar");
        btnFromDateCalendar.setData("YRCButtonBindingDefination", bbd);
        btnFromDateCalendar.setCursor(new Cursor(btnFromDateCalendar.getDisplay(), 21));

        
        tbd = new YRCTextBindingData();
        tbd.setName("txtToDate");
        tbd.setTargetBinding("SearchCriteria:/XPXRefOrderHdr/@ToOrderDate");
        tbd.setSourceBinding("SearchCriteria:/XPXRefOrderHdr/@ToOrderDate");
        txtToDate.setData("YRCTextBindingDefination", tbd);
        bbd = new YRCButtonBindingData();
        bbd.setName("btnToDateCalendar");
        btnToDateCalendar.setData("YRCButtonBindingDefination", bbd);
        btnToDateCalendar.setCursor(new Cursor(btnToDateCalendar.getDisplay(), 21));
        
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtSearchBy");
        txtSearchBy.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtCompany");
        txtCompany.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtAccount");
        txtAccount.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtSuffix");
        txtSuffix.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
        
        tbd = new YRCTextBindingData();
        tbd.setName("txtShipFrom");
        txtShipFrom.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
               
    }
	
	private void setBindingORDERist(){
		YRCTextBindingData textBindingData = new YRCTextBindingData();
        textBindingData = new YRCTextBindingData();
  
        
		/************/
		YRCComboBindingData cbd = new YRCComboBindingData();
        YRCTableBindingData tblResultsBinding1 = new YRCTableBindingData();
		YRCTblClmBindingData colBindings11[] = new YRCTblClmBindingData[tblSearchResults.getColumnCount()];
		colBindings11[0] = new YRCTblClmBindingData();
		colBindings11[0].setName("clmStatus");
		colBindings11[0].setAttributeBinding("@Status");
		colBindings11[0].setColumnBinding("Status");
        colBindings11[0].setSortReqd(true);
        colBindings11[0].setSortBinding("@Status");
        colBindings11[0].setServerImageConfiguration(YRCConstants.IMAGE_SMALL);
		colBindings11[0].setFilterReqd(true);
		colBindings11[0].setDefaultServerImageProvider(new IYRCDefaultServerImageProvider(){
			public String getImageTheme(Object obj) {
				boolean willCallFlag = false;
				boolean rushOrderFlag = false;
				boolean needsAttention = false;
				boolean extnOUFailureLockFlag = false;
				boolean orderLockFlag = false;
				Element eleTableItem = (Element)obj;
				Element eleWillCall = YRCXmlUtils.getChildElement(eleTableItem, "Extn");
				Element eleHolds = YRCXmlUtils.getChildElement(eleTableItem, "OrderHoldTypes");
				//Added for JIRA 4323. CHange to pemanane lock orders. For testing i have done it for WEbHOLD ordres
				if("Y".equalsIgnoreCase(eleWillCall.getAttribute("ExtnOUFailureLockFlag"))){
					extnOUFailureLockFlag = true;
				}
				if("Y".equalsIgnoreCase(eleWillCall.getAttribute("ExtnOrderLockFlag"))){
					orderLockFlag = true;
				}
				if("Y".equalsIgnoreCase(eleWillCall.getAttribute("ExtnWillCall"))){
					willCallFlag = true;
				}else if("Y".equalsIgnoreCase(eleWillCall.getAttribute("ExtnRushOrderFlag"))){
					rushOrderFlag = true;
				}
				if(eleHolds!=null){
					Iterator itrHold = YRCXmlUtils.getChildren(eleHolds);
					while(itrHold.hasNext()) {
						Element eleHold = (Element) itrHold.next();
						//Customer Suspend case handled for JIRA 3929
		if (((XPXConstants.NEEDS_ATTENTION).equals(eleHold.getAttribute("HoldType")))&& "1100".equals(eleHold.getAttribute("Status"))|| "M0007".equalsIgnoreCase(eleWillCall.getAttribute("ExtnHeaderStatusCode"))) {
									needsAttention = true;
								}
					}
				}
				
				if(willCallFlag){
					return "WillCall";
				}
				else if(rushOrderFlag){
					return "RushOrder";
				}
				else if (needsAttention){
					return "NeedsAttention";
				}
				//Added for JIRA 4323. CHange to pemanane lock orders. For testing i have done it for WEbHOLD ordres
				else if(extnOUFailureLockFlag  ){
					return "extnOUFailureLockFlag";
				}
				else if(orderLockFlag){
					return "orderLockFlag";
				}
				else
					return null;
			}
			
		});
		
		
		colBindings11[1] = new YRCTblClmBindingData();
		colBindings11[1].setName("clmOrderType");
		colBindings11[1].setAttributeBinding("@OrderType");
		colBindings11[1].setColumnBinding("Order Type");
        colBindings11[1].setSortReqd(true);
   		//colBindings11[1].setLinkReqd(true);
		colBindings11[1].setFilterReqd(true);
		
		colBindings11[2] = new YRCTblClmBindingData();
		colBindings11[2].setName("clmWebConfirmation");
		colBindings11[2].setAttributeBinding("Extn/@ExtnWebConfNum");
		colBindings11[2].setColumnBinding("Web Confirmation");
        colBindings11[2].setSortReqd(true);
   		//colBindings11[1].setLinkReqd(true);
		colBindings11[2].setFilterReqd(true);
		
		colBindings11[3] = new YRCTblClmBindingData();
		colBindings11[3].setName("clmOrderNum");
		colBindings11[3].setAttributeBinding("@FormatedLegacyOrderNo");
        colBindings11[3].setColumnBinding("Order #");
        colBindings11[3].setSortReqd(true);
        colBindings11[3].setFilterReqd(true);
        
        colBindings11[4] = new YRCTblClmBindingData();
		colBindings11[4].setName("clmPONum");
		colBindings11[4].setAttributeBinding("@CustomerPONo");
		
        colBindings11[4].setColumnBinding("PO #");
        colBindings11[4].setSortReqd(true);
        colBindings11[4].setFilterReqd(true);
        
        colBindings11[5] = new YRCTblClmBindingData();
		colBindings11[5].setName("clmCompany");
		colBindings11[5].setAttributeBinding("Extn/@ExtnSAPParentName");
        colBindings11[5].setColumnBinding("Company");
        colBindings11[5].setSortReqd(true);
        colBindings11[5].setFilterReqd(true);
        
        colBindings11[6] = new YRCTblClmBindingData();
		colBindings11[6].setName("clmWarehouse");
		colBindings11[6].setAttributeBinding("Extn/@ExtnCustomerDivision");
        colBindings11[6].setColumnBinding("Warehouse");
        colBindings11[6].setSortReqd(true);
        colBindings11[6].setFilterReqd(true);
		
		colBindings11[7] = new YRCTblClmBindingData();
		colBindings11[7].setName("clmBillTo");
		colBindings11[7].setAttributeBinding("Extn/@ExtnCustomerNo");
        colBindings11[7].setColumnBinding("Bill-To #");
        colBindings11[7].setSortReqd(true);
        colBindings11[7].setFilterReqd(true);
        
   		colBindings11[8] = new YRCTblClmBindingData();
		colBindings11[8].setName("clmShipToName");
		colBindings11[8].setAttributeBinding("Extn/@ExtnShipToName");
        colBindings11[8].setColumnBinding("Ship-To Name");
        colBindings11[8].setSortReqd(true);
        colBindings11[8].setFilterReqd(true);
        
        colBindings11[9] = new YRCTblClmBindingData();
		colBindings11[9].setName("clmOrderedBy");
        colBindings11[9].setAttributeBinding("Extn/@ExtnOrderedByName");
        colBindings11[9].setColumnBinding("Ordered By");   
        colBindings11[9].setFilterReqd(true);
        colBindings11[9].setSortReqd(true);
      
        colBindings11[10] = new YRCTblClmBindingData();
		colBindings11[10].setName("clmOrdered");
		colBindings11[10].setAttributeBinding("@OrderDate");
        colBindings11[10].setColumnBinding("Ordered");
        colBindings11[10].setSortReqd(true);
        colBindings11[10].setFilterReqd(true);
        
        colBindings11[11] = new YRCTblClmBindingData();
		colBindings11[11].setName("clmAmount");
		colBindings11[11].setAttributeBinding("Extn/@ExtnTotalOrderValue");
        colBindings11[11].setColumnBinding("Amount");
        colBindings11[11].setSortReqd(true);
        colBindings11[11].setFilterReqd(true);
        
        colBindings11[12] = new YRCTblClmBindingData();
		colBindings11[12].setName("clmShipDate");
		colBindings11[12].setAttributeBinding("@ReqDeliveryDate");
        colBindings11[12].setColumnBinding("Ship Date");
        colBindings11[12].setSortReqd(true);
        colBindings11[12].setFilterReqd(true);
        
        tblResultsBinding1.setSourceBinding("OrderListModel:/Page/Output/OrderList/Order");
		tblResultsBinding1.setName("tblResultz");
        tblResultsBinding1.setTblClmBindings(colBindings11);
        tblResultsBinding1.setKeyNavigationRequired(true);
        tblResultsBinding1.setFilterReqd(true);
        tblResultsBinding1.setSortRequired(true);
        tblSearchResults.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, tblResultsBinding1);
        


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

	public XPXOrderHistoryPanelBehavior getMyBehavior() {
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

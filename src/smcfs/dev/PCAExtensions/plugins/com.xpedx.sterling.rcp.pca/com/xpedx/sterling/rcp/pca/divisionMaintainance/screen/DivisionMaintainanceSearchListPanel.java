package com.xpedx.sterling.rcp.pca.divisionMaintainance.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
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
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.IYRCTableColumnTextProvider;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;

public class DivisionMaintainanceSearchListPanel extends Composite implements IYRCComposite{
	
	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.divisionMaintainance.screen.DivisionMaintainanceSearchListPanel";
	
	private DivisionMaintainanceSearchListPanelBehavior myBehavior;
	private Composite pnlRoot = null;
	private Label lblBrandToSearch;
	private Label lblDivisionNameToSearch;
	private Combo cmbBrandToSearch;
	private Text txtDivisionNameToSearch;
	private Composite pnlButtons, pnlSearchTop, resultsPanel, pnlResultsHeader, pnlOrderSearchResults;
	private Button btnSearch;
	private Button btnReset;
	private Composite cmpstSearchResultsAndControls;
	Table tblResults = null;
	private Label lblResultsTitle;
	private SelectionListener selectionListener;

	private Listener listener;
	public DivisionMaintainanceSearchListPanel(Composite parent, int style) {
		super(parent, style);
		listener = new Listener(){

			public void handleEvent(Event event) {
				Widget ctrl = event.widget;
				String ctrlName = (String) ctrl.getData("name");
				if (!YRCPlatformUI.isVoid(ctrlName)){
					if (YRCPlatformUI.equals(ctrlName, "cmbBrandToSearch") 
							|| YRCPlatformUI.equals(ctrlName, "txtDivisionNameToSearch")
									|| YRCPlatformUI.equals(ctrlName, "btnSearch")) {
						myBehavior.search();
					}
			    }
				
			}
			
		};
		selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Widget ctrl = e.widget;
				String ctrlName = (String) ctrl.getData("name");
				if (!YRCPlatformUI.isVoid(ctrlName)){
					if (YRCPlatformUI.equals(ctrlName, "btnSearch")) {
						myBehavior.search();
					}
			    }
			};
        };
		
        initialize();
		setBindingForComponents();
		myBehavior = new DivisionMaintainanceSearchListPanelBehavior(this,FORM_ID);
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
        pnlRoot.setBackgroundMode(0);
        pnlRoot.setData("name", "pnlRoot");
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
		
		Composite pnlSearchCriteriaTitle = new Composite(pnlSearchTop, SWT.NONE);
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
		
		Label lblSearchCriteriaTitle = new Label(pnlSearchCriteriaTitle, SWT.LEFT);
		GridData lblSearchCriteriaTitlelayoutData = new GridData();
		lblSearchCriteriaTitlelayoutData.verticalAlignment = 16777216;
		lblSearchCriteriaTitlelayoutData.grabExcessHorizontalSpace = true;
		lblSearchCriteriaTitle.setLayoutData(lblSearchCriteriaTitlelayoutData);
		lblSearchCriteriaTitle.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		lblSearchCriteriaTitle.setText("Division_Search_Criteria");
		
	}

	
	private void createPnlSearchCriteria() {
		Composite pnlSearchCriteria = new Composite(pnlSearchTop, 0);
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
        
        lblBrandToSearch = new Label(pnlSearchCriteria,SWT.NONE);
        lblBrandToSearch.setText("Division_Brand");
        cmbBrandToSearch = new Combo(pnlSearchCriteria,8);
        GridData gdCriteria = new GridData();
        gdCriteria.widthHint = 100;
        cmbBrandToSearch.setLayoutData(gdCriteria);
        cmbBrandToSearch.addListener(14, (Listener) listener);
        
        lblDivisionNameToSearch = new Label(pnlSearchCriteria,SWT.NONE);
        lblDivisionNameToSearch.setText("Division");
        txtDivisionNameToSearch = new Text(pnlSearchCriteria,2048);
        txtDivisionNameToSearch.setLayoutData(gdCriteria);
        txtDivisionNameToSearch.addListener(14, (Listener) listener);
        
	}

	private void createPnlButtons() {
		
		pnlButtons = new Composite(pnlRoot, SWT.NONE);
		pnlButtons.setBackgroundMode(SWT.INHERIT_NONE);
		pnlButtons.setData(YRCConstants.YRC_CONTROL_NAME, "pnlButtons");
		GridData pnlButtonslayoutData = new GridData();
		pnlButtonslayoutData.horizontalAlignment = GridData.FILL;
		pnlButtonslayoutData.verticalAlignment = GridData.FILL;
		pnlButtonslayoutData.grabExcessHorizontalSpace = true;
		pnlButtonslayoutData.horizontalSpan = 2;
		pnlButtons.setLayoutData(pnlButtonslayoutData);
		pnlButtons.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "TaskComposite");
		GridLayout pnlButtonslayout = new GridLayout(4, false);
		pnlButtonslayout.marginHeight = 2;
		pnlButtonslayout.marginWidth = 0;
		pnlButtons.setLayout(pnlButtonslayout);
		
		//search button Creation
		btnSearch = new Button(pnlButtons, SWT.PUSH);
		btnSearch.setData("name","btnSearch");
		btnSearch.setText("Division_Search");
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		btnSearch.setLayoutData(gridData);
		btnSearch.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "Button");
		//search button click listener
		btnSearch.addSelectionListener(selectionListener);
		
		//reset button
		btnReset = new Button(pnlButtons, SWT.PUSH);
		btnReset.setText("Division_Reset");
		GridData btnResetlayoutData = new GridData();
		btnResetlayoutData.horizontalAlignment = 2;
		btnResetlayoutData.heightHint = 25;
		btnResetlayoutData.widthHint = 80;
		btnReset.setLayoutData(btnResetlayoutData);
		//reset button click listener
		btnReset.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.reset();
			}
		});
		
	}

	
	private void setBindingForComponents() {
	    setBindingForSearchCriteria();
	    setBindingForSearchResults();
    }

	private void setBindingForSearchCriteria() {
		
		YRCComboBindingData cbd = new YRCComboBindingData();
		cbd.setSourceBinding("DivisionSearchCriteria:/Organization/Extn/@ExtnBrandCode");
		cbd.setTargetBinding("DivisionSearchCriteria:/Organization/Extn/@ExtnBrandCode");
		cbd.setCodeBinding("@CodeValue");
		cbd.setDescriptionBinding("@CodeShortDescription");
		cbd.setListBinding("BrandCodesList:/CommonCodeList/CommonCode");
		cbd.setName("cmbBrandToSearch");
		cmbBrandToSearch.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		cmbBrandToSearch.setData(YRCConstants.YRC_CONTROL_NAME,"cmbBrandToSearch");
		
		YRCTextBindingData bdDivisionNameToSearch = new YRCTextBindingData();
		bdDivisionNameToSearch.setName("txtDivisionNameToSearch");
		bdDivisionNameToSearch.setTargetBinding("DivisionSearchCriteria:/Organization/@OrganizationCodeOrName");
		bdDivisionNameToSearch.setSourceBinding("DivisionSearchCriteria:/Organization/@OrganizationCodeOrName");
		txtDivisionNameToSearch.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, bdDivisionNameToSearch);
		txtDivisionNameToSearch.setData(YRCConstants.YRC_CONTROL_NAME,"lblDivisionNameToSearch");
		
	}
	
	private void setBindingForSearchResults() {
		YRCTableBindingData bindingData = new YRCTableBindingData();
		YRCTblClmBindingData colBindings[] = new YRCTblClmBindingData[tblResults.getColumnCount()];
		
		colBindings[0] = new YRCTblClmBindingData();
		colBindings[0].setAttributeBinding("Extn/@ExtnBrandCode");
		colBindings[0].setColumnBinding("Brand");
		colBindings[0].setSortReqd(true);
		colBindings[0].setLabelProvider(new IYRCTableColumnTextProvider(){
			public String getColumnText(Element obj) {
				Element eleTableItem = (Element)obj;
				return myBehavior.getBrandDescription(eleTableItem);
			}
        });
		
		colBindings[1] = new YRCTblClmBindingData();
		colBindings[1].setAttributeBinding("Extn/@ExtnCompanyCode");
		colBindings[1].setColumnBinding("Company_Code");
		colBindings[1].setSortReqd(true);
		
		colBindings[2] = new YRCTblClmBindingData();
		colBindings[2].setAttributeBinding("OrganizationName");
		colBindings[2].setColumnBinding("Division_Name");
		colBindings[2].setSortReqd(true);
		
		colBindings[3] = new YRCTblClmBindingData();
		colBindings[3].setAttributeBinding("OrganizationCode");
		colBindings[3].setColumnBinding("Division_Code");
		colBindings[3].setSortReqd(true);
		
		bindingData.setSortRequired(true);
		bindingData.setSourceBinding("/OrganizationList/Organization");
		bindingData.setName("tblResults");
		bindingData.setTblClmBindings(colBindings);
		tblResults.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, bindingData);
		
	}

	private void createCmpstSearchResultsAndControls() {
		
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

	private void createResultsPanel() {
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
	
	private void createPnlResultsHeader() {
		
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
		lblResultsTitle.setText("Division_Search_Results");
		
		Image img = YRCPlatformUI.getImage("ListTitle");
		if (img != null) {
			img.setBackground(YRCPlatformUI.getBackGroundColor("Composite"));
			lblResultsTitle.setImage(img);
		}
	}

	private void createPnlOrderSearchResults() {
		
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

	private void createTblSearchResults() {
		GridData tblDivisionSearchResultsGD = new org.eclipse.swt.layout.GridData();
		tblResults = new Table(pnlOrderSearchResults,SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tblResults.setData("name", "tblSearchResults");
		
		TableColumn clmBrand = new TableColumn(tblResults, SWT.NONE);
		clmBrand.setText("Brand");
		clmBrand.setWidth(150);//TODO: set appropriate width
		
		TableColumn clmCompanyCode = new TableColumn(tblResults, SWT.NONE);
		clmCompanyCode.setText("Company_Code");
		clmCompanyCode.setWidth(150);//TODO: set appropriate width
		
		TableColumn clmDivisionName = new TableColumn(tblResults, SWT.NONE);
		clmDivisionName.setText("Division_Name");
		clmDivisionName.setWidth(300);//TODO: set appropriate width
		
		TableColumn clmDivisionCode = new TableColumn(tblResults, SWT.NONE);
		clmDivisionCode.setText("Division_Code");
		clmDivisionCode.setWidth(150);//TODO: set appropriate width
		
		tblDivisionSearchResultsGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblDivisionSearchResultsGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblDivisionSearchResultsGD.grabExcessHorizontalSpace = true;
		tblDivisionSearchResultsGD.grabExcessVerticalSpace = true;
		tblDivisionSearchResultsGD.horizontalSpan = 2;
		tblResults.setLayoutData(tblDivisionSearchResultsGD);
		tblResults.setHeaderVisible(true);
		tblResults.setLinesVisible(true);
		tblResults.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				myBehavior.mouseDoubleClick(e, "tblDivisionSearchResults");
			}
		});
		
	}

	public String getHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	public IYRCPanelHolder getPanelHolder() {
		// TODO Auto-generated method stub
		return null;
	}

}

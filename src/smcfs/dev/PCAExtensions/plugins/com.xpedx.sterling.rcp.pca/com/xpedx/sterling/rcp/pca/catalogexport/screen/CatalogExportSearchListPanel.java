/*
 * Created on Aug 17,2010
 *
 */
package com.xpedx.sterling.rcp.pca.catalogexport.screen;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.IYRCTableColumnTextProvider;
import com.yantra.yfc.rcp.IYRCTableLinkProvider;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;

/**
 * @author vchandra
 *
 * Articles Search and List Panel.
 */
public class CatalogExportSearchListPanel extends Composite  implements IYRCComposite {

    public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.catalogexport.screen.CatalogExportSearchListPanel";
    private CatalogExportSearchListPanelBehavior myBehavior;

	private Composite pnlRoot = null;
	private Group grpSearchFields = null;

	private Label lblBrand;
	private Combo cmbBrand;

	private Composite pnlButtons = null;
	private Button btnSearch = null;
	private Button btnReset = null;
	private Button btnCreate;
	private Group pnlSearchResults = null;
	private Composite pnlTitleHolder = null;
	private Label lblTitleIcon = null;
	private Composite pnlTableHolder = null;
	Table tblSearchResults = null;
	// search fields declaration starts here
	private Label lblURL = null;
	private Label lblLabel=null;
	private Text txtURL = null;
	private Text txtLabel=null;
	private Combo cmbLabelQryType;
	private Combo cmbURLQryType;
	public CatalogExportSearchListPanel(Composite parent, int style, Object inputObject) {
		super(parent, style);
		initialize();
        setBindingForComponents();
        myBehavior = new CatalogExportSearchListPanelBehavior(this, FORM_ID,inputObject);
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
		tblSearchResults = new Table(pnlTableHolder,SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tblSearchResults.setData("name", "tblSearchResults");
		TableColumn clmLabel = new TableColumn(tblSearchResults, SWT.NONE);
		clmLabel.setText("Label_LBL");
//		clmLabel.setWidth((getDisplay().getBounds().width-450)/2);//TODO: set appropriate width
		clmLabel.setWidth(150);	
		
		TableColumn clmURL = new TableColumn(tblSearchResults, SWT.NONE);
		clmURL.setText("URL_LBL");
//		clmURL.setWidth((getDisplay().getBounds().width-450)/2);//TODO: set appropriate width
		clmURL.setWidth(320);
		
		TableColumn clmBrand = new TableColumn(tblSearchResults, SWT.NONE);
		clmBrand.setText("Division_Brand");
//		clmBrand.setWidth((getDisplay().getBounds().width-450)/2);//TODO: set appropriate width
		clmBrand.setWidth(160);
		
		tblSearchResultsGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblSearchResultsGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblSearchResultsGD.grabExcessHorizontalSpace = true;
		tblSearchResultsGD.grabExcessVerticalSpace = true;
		tblSearchResultsGD.horizontalSpan = 2;
		tblSearchResults.setLayoutData(tblSearchResultsGD);
		tblSearchResults.setHeaderVisible(true);
		tblSearchResults.setLinesVisible(true);
		tblSearchResults.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				myBehavior.mouseDoubleClick(e, "tblSearchResults");
			}
		});
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
		
//		lblTitle = new Label(pnlTitleHolder, SWT.NONE);
//		lblTitle.setText("Articles_List");
		
//		lblTitle.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE,"Articles_List");
		
		pnlTitleHolder.setLayoutData(pnlTitleHolderGD);
		pnlTitleHolder.setLayout(pnlTitleHolderGL);
		pnlTitleHolderGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
//		pnlTitleHolderGD.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		pnlTitleHolderGD.grabExcessHorizontalSpace = true;
		
		pnlTitleHolderGL.numColumns = 2;
		pnlTitleHolderGL.horizontalSpacing = 5;
		pnlTitleHolderGL.verticalSpacing = 0;
		pnlTitleHolderGL.marginWidth = 0;
		pnlTitleHolderGL.marginHeight = 0;
		
		XPXUtils.addGradientPanelHeader(pnlTitleHolder, "Catalog_Export_List", true);
	}
		
	private void createSearchFieldsGroup() {
		GridLayout srchGroupLayout = new GridLayout();
		GridData gdSrchGroup = new GridData();
		grpSearchFields = new Group(pnlRoot, SWT.NONE);
		
		gdSrchGroup.grabExcessHorizontalSpace = true;
		gdSrchGroup.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		grpSearchFields.setLayoutData(gdSrchGroup);
		grpSearchFields.setLayout(srchGroupLayout);
		srchGroupLayout.numColumns = 3;
		
		XPXUtils.addGradientPanelHeader(grpSearchFields, "Catalog_Export_Search", true);
		
		// search creation starts here
		// Article Name
		lblLabel = new Label(grpSearchFields,SWT.NONE);
		lblLabel.setText("Label_LBL");
		
		cmbLabelQryType = new Combo(grpSearchFields, 8);
		GridData cmbOrganizationNameQryTypelayoutData1 = new GridData();
		cmbOrganizationNameQryTypelayoutData1.horizontalAlignment = 4;
		cmbLabelQryType.setLayoutData(cmbOrganizationNameQryTypelayoutData1);
		
		txtLabel = new Text(grpSearchFields,SWT.BORDER);
		GridData gdLabel = new GridData();
		gdLabel.grabExcessHorizontalSpace = true;
		gdLabel.widthHint=80;
		//gdLabel.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtLabel.setLayoutData(gdLabel);
		
		lblURL = new Label(grpSearchFields,SWT.NONE);
		lblURL.setText("URL_LBL");
		
		cmbURLQryType = new Combo(grpSearchFields, 8);
        GridData cmbOrganizationNameQryTypelayoutData = new GridData();
        cmbOrganizationNameQryTypelayoutData.horizontalAlignment = 4;
        cmbURLQryType.setLayoutData(cmbOrganizationNameQryTypelayoutData);
        
		txtURL = new Text(grpSearchFields,SWT.BORDER);
		GridData gdOrderName = new GridData();
		gdOrderName.widthHint=80;
		gdOrderName.grabExcessHorizontalSpace = true;
		//gdOrderName.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtURL.setLayoutData(gdOrderName);

		lblBrand = new Label(grpSearchFields, SWT.NONE);
		lblBrand.setText("Division_Brand");
		
		cmbBrand = new Combo(grpSearchFields, 8);
		GridData cmbOrganizationNameQryTypelayoutData2 = new GridData();
		cmbOrganizationNameQryTypelayoutData1.horizontalAlignment = 4;
		cmbBrand.setLayoutData(cmbOrganizationNameQryTypelayoutData2);
		createButtonComposite();
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
		btnSearch.setText("Export_Search");
		
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
		btnReset.setText("Export_Reset");
		btnReset.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.reset();
			}
		});
		
		btnCreate = new Button(pnlButtons, SWT.NONE);
		btnCreate.setText("Export_Create_New");
		btnCreate.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.create();
			}
		});
	}

	private void setBindingForComponents() {
	    setBindingForSearchCriteria();
	    setBindingForSearchResults();
    }
	
    private void setBindingForSearchCriteria() {
    	 YRCComboBindingData  cmbQryTypebd = new YRCComboBindingData();
         cmbQryTypebd.setName("cmbOrganizationNameQryType");
         cmbQryTypebd.setSourceBinding("SearchCriteria:/XPXCatalogExp/@LabelQryType");
         //cmbOrganizationNameQryTypebd.setTargetBinding("getCustomerList_input:/Customer/BuyerOrganization/@OrganizationNameQryType");
         cmbQryTypebd.setTargetBinding("SearchCriteria:/XPXCatalogExp/@UrlQryType");
         cmbQryTypebd.setCodeBinding("QueryType");
         cmbQryTypebd.setListBinding("getQueryTypeList_output:/QueryTypeList/StringQueryTypes/QueryType");
         cmbQryTypebd.setDescriptionBinding("QueryTypeDesc");
         cmbQryTypebd.setBundleDriven(true);
         cmbURLQryType.setData("YRCComboBindingDefination", cmbQryTypebd);
        
    	YRCTextBindingData bdURL = new YRCTextBindingData();
        //bdOrderName.setName("OrderName");
        bdURL.setTargetBinding("SearchCriteria:/XPXCatalogExp/@Url");
        bdURL.setSourceBinding("SearchCriteria:/XPXCatalogExp/@Url");
        txtURL.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,bdURL);
        lblURL.setData(YRCConstants.YRC_CONTROL_NAME,"lblURL");
        
        YRCTextBindingData bdLabel = new YRCTextBindingData();
        //bdOrderName.setName("OrderName");
        bdLabel.setTargetBinding("SearchCriteria:/XPXCatalogExp/@Label");
        bdLabel.setSourceBinding("SearchCriteria:/XPXCatalogExp/@Label");
        txtLabel.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,bdLabel);
        lblLabel.setData(YRCConstants.YRC_CONTROL_NAME,"lblLabel");
        
        cmbQryTypebd = new YRCComboBindingData();
        cmbQryTypebd.setName("cmbOrganizationNameQryType");
        cmbQryTypebd.setSourceBinding("SearchCriteria:/XPXCatalogExp/@LabelQryType");
        //cmbOrganizationNameQryTypebd.setTargetBinding("getCustomerList_input:/Customer/BuyerOrganization/@OrganizationNameQryType");
        cmbQryTypebd.setTargetBinding("SearchCriteria:/XPXCatalogExp/@LabelQryType");
        cmbQryTypebd.setCodeBinding("QueryType");
        cmbQryTypebd.setListBinding("getQueryTypeList_output:/QueryTypeList/StringQueryTypes/QueryType");
        cmbQryTypebd.setDescriptionBinding("QueryTypeDesc");
        cmbQryTypebd.setBundleDriven(true);
        cmbLabelQryType.setData("YRCComboBindingDefination", cmbQryTypebd);
        
        YRCComboBindingData cbd = new YRCComboBindingData();
		cbd.setSourceBinding("SearchCriteria:/XPXCatalogExp/@BrandCode");
		cbd.setTargetBinding("SearchCriteria:/XPXCatalogExp/@BrandCode");
		cbd.setCodeBinding("@CodeValue");
		cbd.setDescriptionBinding("@CodeShortDescription");
		cbd.setListBinding("BrandCodesList:/CommonCodeList/CommonCode");
		cbd.setName("cmbBrand");
		cmbBrand.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		cmbBrand.setData(YRCConstants.YRC_CONTROL_NAME,"cmbBrand");

    }
    private void setBindingForSearchResults() {
        YRCTableBindingData bindingData = new YRCTableBindingData();
        YRCTblClmBindingData colBindings[] = new YRCTblClmBindingData[3];
        colBindings[0] = new YRCTblClmBindingData();
        colBindings[0].setAttributeBinding("Label");
        colBindings[0].setColumnBinding("Label_LBL");
        colBindings[0].setSortReqd(true); 
        colBindings[1] = new YRCTblClmBindingData();
        colBindings[1].setAttributeBinding("Url");
        colBindings[1].setColumnBinding("URL_LBL");
        colBindings[1].setLinkReqd(true);
        colBindings[1].setSortReqd(true);   
		colBindings[2] = new YRCTblClmBindingData();
		colBindings[2].setAttributeBinding("BrandCode");
		colBindings[2].setColumnBinding("Division_Brand");
		colBindings[2].setSortReqd(true);
		colBindings[2].setLabelProvider(new IYRCTableColumnTextProvider(){
			public String getColumnText(Element obj) {
				Element eleTableItem = (Element)obj;
				return myBehavior.getBrandDescription(eleTableItem);
			}
        });
        bindingData.setSortRequired(true);
        bindingData.setSourceBinding("/XPXCatalogExpList/XPXCatalogExp");
        bindingData.setName("tblSearchResults");
        bindingData.setTblClmBindings(colBindings);
        bindingData.setLinkProvider(new IYRCTableLinkProvider(){
        	public String getLinkTheme(Object element, int columnIndex) {
        			return "TableLink";
        	}
        	
        	public void linkSelected(Object element, int columnIndex) {
        		Element eleTableItem = (Element)element;
        		if(!YRCPlatformUI.isVoid(eleTableItem.getAttribute("Url"))){
        		XPXUtils.accessURL(eleTableItem.getAttribute("Url"), "");
        		}
        	}
        });
        tblSearchResults.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, bindingData);
    }

    public IYRCPanelHolder getPanelHolder() {
        // TODO Complete getPanelHolder
        return null;
    }

	public String getHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	public CatalogExportSearchListPanelBehavior getMyBehavior() {
		return myBehavior;
	}
	public void setMyBehavior(CatalogExportSearchListPanelBehavior myBehavior) {
		this.myBehavior = myBehavior;
	}
	public Button getBtnCreate() {
		return btnCreate;
	}
	
}
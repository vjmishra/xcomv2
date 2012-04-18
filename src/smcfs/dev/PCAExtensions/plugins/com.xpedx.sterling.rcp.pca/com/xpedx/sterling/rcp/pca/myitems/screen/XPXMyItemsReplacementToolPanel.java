/*
 * Created on Dec 03,2010
 *
 */
package com.xpedx.sterling.rcp.pca.myitems.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXPaginationBehavior;
import com.xpedx.sterling.rcp.pca.util.XPXPaginationComposite;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.IYRCTableImageProvider;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCListBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;

/**
 * @author Administrator
 *
 * Generated by MTCE
 */
public class XPXMyItemsReplacementToolPanel extends XPXPaginationComposite  implements IYRCComposite {

    public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.myitems.screen.XPXMyItemsReplacementToolPanel";
    private XPXMyItemsReplacementToolPanelBehavior myBehavior;

	private Composite pnlRoot = null;
	private Group grpSearchFields = null;
	private Group grpSrchByDivsFields=null;
	private Group grpSrchByCustomersFields=null;
	private Composite pnlButtons = null;
	private Button btnSearch = null;
	private Button btnReset = null;
	private Group pnlSearchResults = null;
	private Composite pnlTitleHolder = null;
	private Label lblTitleIcon = null;
	private Label lblTitle = null;
	private Composite pnlBottomButtonHolder = null;
	private Button btnProceed = null;
	private Composite pnlTableHolder = null;
	Table tblSearchResults = null;
	// search fields declaration starts here
	
	private Label lblDivisionID = null;
	private List listDivisions;
	private Text txtDivisionID = null;
	
	private Label lblMasterCustomer = null;
	private Label lblCustomer = null;
	private Label lblBillToCustomer = null;
	private Label lblShipToCustomer = null;
	private Combo cmbMasterCustomer;
	private Combo cmbCustomer;
	private Combo cmbBillToCustomer;
	private Combo cmbShipToCustomer;
	private Label lblLPC = null;
	private Text txtLPC = null;
	private Label lblReplaceLPC = null;
	private Text txtReplaceLPC = null;
	private Button btnReplaceItemList=null;
	public Combo cmbEnterprise;
	private Label lblEnterprise;
	private Button getParents=null;
	private Text BillToId = null;
	private Text ShipToId = null;
	private Text SAPId = null;
	private Text MasterCustomerId = null;
	private Composite pnlDivisions; 
		
	public XPXMyItemsReplacementToolPanel(Composite parent, int style, String enterpriseKey) {
		super(parent, style);
		initialize();
        setBindingForComponents();
        myBehavior = new XPXMyItemsReplacementToolPanelBehavior(this, FORM_ID);
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
        pnlRoot.setData("yrc:customType", "TaskComposite");
	}

	private void createPnlSearchResults() {
		GridData pnlSearchResultsGD = new org.eclipse.swt.layout.GridData();
		GridLayout pnlSearchResultsGL = new GridLayout();
		pnlSearchResults = new Group(pnlRoot, SWT.NONE);
		createPnlTitleHolder();
		createPnlBottomButtonHolder();
		//Create the pagination Links
        createPaginationLinks(pnlSearchResults, myBehavior.PAGINATION_STRATEGY_FOR_MIL_REPTOOL_SEARCH);
        
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
		
		TableColumn clmDivision = new TableColumn(tblSearchResults, SWT.LEFT);
		clmDivision.setText("DivisionName_DivisionNo_Key");
		clmDivision.setWidth(150);
		
		TableColumn clmName = new TableColumn(tblSearchResults, SWT.LEFT);
		clmName.setText("My_Items_List_Name");
		clmName.setWidth(150);
		
		TableColumn clmDesc = new TableColumn(tblSearchResults, SWT.LEFT);
		clmDesc.setText("My_Items_List_Desc");
		clmDesc.setWidth(150);
		
		TableColumn clmListType = new TableColumn(tblSearchResults, SWT.NONE);
		clmListType.setWidth(130);
		
		TableColumn clmSapName = new TableColumn(tblSearchResults, SWT.NONE);
		clmSapName.setWidth(130);
		

		TableColumn clmCheckBox = new TableColumn(tblSearchResults, SWT.LEFT);
		clmCheckBox.setToolTipText("Check_This");
		clmCheckBox.setWidth(30);
		clmCheckBox.setResizable(false);
		
		tblSearchResultsGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblSearchResultsGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblSearchResultsGD.grabExcessHorizontalSpace = true;
		tblSearchResultsGD.grabExcessVerticalSpace = true;
		tblSearchResultsGD.horizontalSpan = 2;
		tblSearchResults.setLayoutData(tblSearchResultsGD);
		tblSearchResults.setHeaderVisible(true);
		tblSearchResults.setLinesVisible(true);
	}
	
	private void createPnlBottomButtonHolder() {
		RowLayout pnlBottomButtonHolderRL = new RowLayout();
		GridData pnlBottomButtonHolderGD = new GridData();
		pnlBottomButtonHolder = new Composite(pnlSearchResults, SWT.NONE);		   
		btnProceed = new Button(pnlBottomButtonHolder, SWT.NONE);
		pnlBottomButtonHolder.setLayoutData(pnlBottomButtonHolderGD);
		pnlBottomButtonHolder.setLayout(pnlBottomButtonHolderRL);
		pnlBottomButtonHolderGD.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		btnProceed.setText("Replace");
		
		pnlBottomButtonHolderRL.marginTop = 0;
		pnlBottomButtonHolderRL.marginBottom = 5;
		btnProceed.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
//				if(tblSearchResults.getSelectionIndex() >-1) 
				    myBehavior.proceed();
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
		
		lblTitle = new Label(pnlTitleHolder, SWT.NONE);
		lblTitle.setText("List_Of_Replacable_My_Items_List");
		
//		lblTitle.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE,"Replacement Tool");
		
		pnlTitleHolder.setLayoutData(pnlTitleHolderGD);
		pnlTitleHolder.setLayout(pnlTitleHolderGL);
		pnlTitleHolderGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlTitleHolderGD.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		pnlTitleHolderGD.grabExcessHorizontalSpace = true;
		
		pnlTitleHolderGL.numColumns = 2;
		pnlTitleHolderGL.horizontalSpacing = 5;
		pnlTitleHolderGL.verticalSpacing = 0;
		pnlTitleHolderGL.marginWidth = 0;
		pnlTitleHolderGL.marginHeight = 0;
	}
		
	private void createSearchFieldsGroup() {
		GridLayout srchGroupLayout = new GridLayout();
		GridData gdSrchGroup = new GridData();
		grpSearchFields = new Group(pnlRoot, SWT.NONE);
		gdSrchGroup.grabExcessHorizontalSpace = true;
		gdSrchGroup.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		grpSearchFields.setLayoutData(gdSrchGroup);
		grpSearchFields.setLayout(srchGroupLayout);
		srchGroupLayout.numColumns = 4;
		// search fields creation starts here
		
		GridLayout grpLPCLayout = new GridLayout();
		GridData gdLPCGroup = new GridData();
		Group grpReplaceLPCFields = new Group(grpSearchFields, SWT.NONE);
		grpReplaceLPCFields.setText("Group_Replace_LPC_Panel");
		grpReplaceLPCFields.setData("yrc:customType","XPXGroupStyle");
		gdLPCGroup.grabExcessHorizontalSpace = true;
		gdLPCGroup.horizontalSpan = 4;
		gdLPCGroup.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		grpReplaceLPCFields.setLayoutData(gdLPCGroup);
		grpReplaceLPCFields.setLayout(grpLPCLayout);
		grpLPCLayout.numColumns = 4;
		
		lblLPC = new Label(grpReplaceLPCFields, SWT.NONE);
		lblLPC.setText("Current_Legacy_Product_Code");
		txtLPC = new Text(grpReplaceLPCFields, SWT.BORDER);
		GridData gdLPC = new GridData();
		gdLPC.grabExcessHorizontalSpace = true;
		gdLPC.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtLPC.setLayoutData(gdLPC);
		
		lblReplaceLPC = new Label(grpReplaceLPCFields, SWT.NONE);
		lblReplaceLPC.setText("Replace_With_LPC");
		txtReplaceLPC = new Text(grpReplaceLPCFields, SWT.BORDER);
		GridData gdReplaceLPC = new GridData();
		gdReplaceLPC.grabExcessHorizontalSpace = true;
		gdReplaceLPC.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtReplaceLPC.setLayoutData(gdReplaceLPC);
		txtReplaceLPC.setData("name", "txtReplaceLPC");
		
		addArticleToDivisions();
//		GridLayout grpSrchCriteriaLayout = new GridLayout();
//		GridData gdSrchCriteriaGroup = new GridData();
//		Group grpSrchCriteriaFields = new Group(grpSearchFields, SWT.NONE);
//		grpSrchCriteriaFields.setText("Group_Search_Criteria_To_Get_List_Of_My_Items_List");
//		grpSrchCriteriaFields.setData("yrc:customType","XPXGroupStyle");
//		gdSrchCriteriaGroup.grabExcessHorizontalSpace = true;
//		gdSrchCriteriaGroup.horizontalSpan = 4;
//		gdSrchCriteriaGroup.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
//		grpSrchCriteriaFields.setLayoutData(gdSrchCriteriaGroup);
//		grpSrchCriteriaFields.setLayout(grpSrchCriteriaLayout);
//		grpSrchCriteriaLayout.numColumns = 2;
		
		// Search by Divisions
		
		GridLayout grpSrchByDivsLayout = new GridLayout();
		GridData gdSrchByDivsGroup = new GridData();
		grpSrchByDivsFields = new Group(grpSearchFields, SWT.NONE);
		grpSrchByDivsFields.setText("Group_Search_By_Divisions");
		grpSrchByDivsFields.setData("yrc:customType","XPXGroupStyle");
		gdSrchByDivsGroup.verticalAlignment = SWT.FILL;
		gdSrchByDivsGroup.grabExcessHorizontalSpace = true;
		gdSrchByDivsGroup.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		grpSrchByDivsFields.setLayoutData(gdSrchByDivsGroup);
		grpSrchByDivsFields.setLayout(grpSrchByDivsLayout);
		grpSrchByDivsLayout.numColumns = 2;
		
		/*lblDivisionID = new Label(grpSrchByDivsFields,SWT.NONE);
		lblDivisionID.setText("Division");
		GridData gdDivisionLbl = new GridData();
		gdDivisionLbl.verticalSpan = 3;
		gdDivisionLbl.grabExcessVerticalSpace = true;
		gdDivisionLbl.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		lblDivisionID.setLayoutData(gdDivisionLbl);
		
		listDivisions = new List(grpSrchByDivsFields, SWT.SINGLE| SWT.V_SCROLL | SWT.BORDER);
		GridData listDivisionslayoutData = new GridData();
		listDivisionslayoutData.horizontalAlignment = 4;
		listDivisionslayoutData.verticalAlignment = 4;
		listDivisionslayoutData.verticalSpan = 3;
		listDivisionslayoutData.heightHint = 50;
		listDivisionslayoutData.grabExcessVerticalSpace = true;
//		listDivisionslayoutData.grabExcessHorizontalSpace = true;
		listDivisions.setLayoutData(listDivisionslayoutData);
//		listDivisions.setData("yrc:customType", "Combo");
		listDivisions.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String[] strDivision = listDivisions.getSelection();
				String txtDivisionIDs = txtDivisionID.getText();
				if(null!=strDivision && strDivision.length>0){
					if( !YRCPlatformUI.isVoid(txtDivisionIDs)){
						txtDivisionID.setText(txtDivisionIDs+","+strDivision[0]);
					} else {
						txtDivisionID.setText(strDivision[0]);
					}
				}
			}
		});*/
		
		txtDivisionID = new Text(grpSrchByDivsFields,SWT.BORDER);
		GridData gdDivisionID = new GridData();
//		gdDivisionID.widthHint = 140;
		gdDivisionID.verticalSpan = 1;
		gdDivisionID.horizontalSpan = 2;
		gdDivisionID.grabExcessVerticalSpace = true;
		gdDivisionID.grabExcessHorizontalSpace = true;
		gdDivisionID.horizontalAlignment = GridData.FILL;
		gdDivisionID.verticalAlignment = GridData.FILL;
		txtDivisionID.setLayoutData(gdDivisionID);
		
		
		createPnlReplaceButtonHolder();
		
		// Search by Customers
		GridLayout grpSrchByCustomersLayout = new GridLayout();
		GridData gdSrchByCustomersGroup = new GridData();
		grpSrchByCustomersFields = new Group(grpSearchFields, SWT.NONE);
		grpSrchByCustomersFields.setText("Group_Search_By_Customers");
		grpSrchByCustomersFields.setData("yrc:customType","XPXGroupStyle");
		gdSrchByCustomersGroup.verticalAlignment = SWT.FILL;
		gdSrchByCustomersGroup.grabExcessHorizontalSpace = true;
		gdSrchByCustomersGroup.horizontalAlignment = SWT.FILL;
		grpSrchByCustomersFields.setLayoutData(gdSrchByCustomersGroup);
		grpSrchByCustomersFields.setLayout(grpSrchByCustomersLayout);
		grpSrchByCustomersLayout.numColumns = 2;
		
		GridData cmbCustomerslayoutData = new GridData();
		cmbCustomerslayoutData.widthHint = 170;
		cmbCustomerslayoutData.horizontalAlignment = GridData.FILL;
		cmbCustomerslayoutData.grabExcessHorizontalSpace = true;
		
		
		lblEnterprise = new Label(grpSrchByCustomersFields, SWT.LEFT);
		GridData lblEnterpriselayoutData = new GridData();
		lblEnterpriselayoutData.horizontalAlignment = 16777224;
		lblEnterpriselayoutData.verticalAlignment = 16777216;
		lblEnterprise.setLayoutData(lblEnterpriselayoutData);
		lblEnterprise.setText("Enterprise");
		
		
		cmbEnterprise = new Combo(grpSrchByCustomersFields,SWT.READ_ONLY);
		GridData cmbEnterpriselayoutData = new GridData();
		cmbEnterpriselayoutData.horizontalAlignment = 4;
		cmbEnterprise.setLayoutData(cmbCustomerslayoutData);
		/*cmbEnterprise.addModifyListener(new ModifyListener(){

			
			public String strOldValue = "";
			public String strNewValue = "";
			int i=0;
			public void modifyText(ModifyEvent e) {
				Combo ctrl = (Combo)e.getSource();
				if(ctrl.getItemCount()>0 && ctrl.getSelectionIndex()>=0){
					strNewValue =ctrl.getItem(ctrl.getSelectionIndex());
					System.out.println("modifyText["+(i++)+"-OLD:"+strOldValue+"-NEW:"+strNewValue+"="+ctrl.getItem(ctrl.getSelectionIndex())+"]");
					if(!YRCPlatformUI.equals(strNewValue,strOldValue)){
						myBehavior.getCustomers("MC");
					
					}
					strOldValue = strNewValue;
				}
			}
		});*/
		
		lblMasterCustomer = new Label(grpSrchByCustomersFields,SWT.NONE);
		lblMasterCustomer.setText("Master_Customer");
		/*cmbMasterCustomer = new Combo(grpSrchByCustomersFields, SWT.DROP_DOWN);
		cmbMasterCustomer.setLayoutData(cmbCustomerslayoutData);
		cmbMasterCustomer.setData("name", "cmbMasterCustomer");
		cmbMasterCustomer.addModifyListener(new ModifyListener(){

			public int i=0;
			public String strOldValue = "";
			public String strNewValue = "";
			public void modifyText(ModifyEvent e) {
				Combo ctrl = (Combo)e.getSource();
				if(ctrl.getItemCount()>0 && ctrl.getSelectionIndex()>=0){
					strNewValue = myBehavior.getFieldValue("cmbMasterCustomer");
//					System.out.println("modifyText["+(i++)+"-OLD:"+strOldValue+"-NEW:"+strNewValue+"="+ctrl.getItem(ctrl.getSelectionIndex())+"]");
					if(!YRCPlatformUI.equals(strNewValue,strOldValue)){
						myBehavior.getCustomers(strNewValue, "C", "MC");
					}
					strOldValue = strNewValue;
				}
			}
		});*/
		MasterCustomerId = new Text(grpSrchByCustomersFields, SWT.BORDER);
		GridData gdLPC4 = new GridData();
		gdLPC4.grabExcessHorizontalSpace = true;
		gdLPC4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		MasterCustomerId.setLayoutData(gdLPC4);
		MasterCustomerId.setData("name", "MasterCustomerId");
		
		lblCustomer = new Label(grpSrchByCustomersFields,SWT.NONE);
		lblCustomer.setText("SAP_Customer");
		/*cmbCustomer = new Combo(grpSrchByCustomersFields, SWT.DROP_DOWN);
		cmbCustomer.setData("name", "cmbCustomer");
		cmbCustomer.setLayoutData(cmbCustomerslayoutData);
		cmbCustomer.addModifyListener(new ModifyListener(){

			public int i=0;
			public String strOldValue = "";
			public String strNewValue = "";
			public void modifyText(ModifyEvent e) {
				Combo ctrl = (Combo)e.getSource();
				if(ctrl.getItemCount()>0 && ctrl.getSelectionIndex()>=0){
					strNewValue = myBehavior.getFieldValue("cmbCustomer");
//					System.out.println("modifyText["+(i++)+"-OLD:"+strOldValue+"-NEW:"+strNewValue+"="+ctrl.getItem(ctrl.getSelectionIndex())+"]");
					
					if(!YRCPlatformUI.equals(strNewValue,strOldValue)){
						myBehavior.getCustomers(strNewValue, "B", "C");
					}
					strOldValue = strNewValue;
				}
			}
		});*/
		SAPId = new Text(grpSrchByCustomersFields, SWT.BORDER);
		GridData gdLPC3 = new GridData();
		gdLPC3.grabExcessHorizontalSpace = true;
		gdLPC3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		SAPId.setLayoutData(gdLPC3);
		SAPId.setData("name", "SAPId");
		

		lblBillToCustomer = new Label(grpSrchByCustomersFields,SWT.NONE);
		lblBillToCustomer.setText("Bill_To_Customer");
		/*cmbBillToCustomer = new Combo(grpSrchByCustomersFields, SWT.DROP_DOWN);
		cmbBillToCustomer.setLayoutData(cmbCustomerslayoutData);
		cmbBillToCustomer.addModifyListener(new ModifyListener(){

			public int i=0;
			public String strOldValue = "";
			public String strNewValue = "";
			public void modifyText(ModifyEvent e) {
				Combo ctrl = (Combo)e.getSource();
				if(ctrl.getItemCount()>0 && ctrl.getSelectionIndex()>=0){
					strNewValue = myBehavior.getFieldValue("cmbBillToCustomer");
//					System.out.println("modifyText["+(i++)+"-OLD:"+strOldValue+"-NEW:"+strNewValue+"="+ctrl.getItem(ctrl.getSelectionIndex())+"]");
					if(!YRCPlatformUI.equals(strNewValue,strOldValue)){
						myBehavior.getCustomers(strNewValue, "S", "B");
					}
					strOldValue = strNewValue;
				}
			}
		});*/
		
		BillToId = new Text(grpSrchByCustomersFields, SWT.BORDER);
		GridData gdLPC1 = new GridData();
		gdLPC1.grabExcessHorizontalSpace = true;
		gdLPC1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		BillToId.setLayoutData(gdLPC1);
		BillToId.setData("name", "BillToId");

		
		lblShipToCustomer = new Label(grpSrchByCustomersFields,SWT.NONE);
		lblShipToCustomer.setText("Ship_To_Customer");
		/*cmbShipToCustomer = new Combo(grpSrchByCustomersFields, SWT.DROP_DOWN);
		cmbShipToCustomer.setLayoutData(cmbCustomerslayoutData);*/
		
		ShipToId = new Text(grpSrchByCustomersFields, SWT.BORDER);
		GridData gdLPC2 = new GridData();
		gdLPC2.grabExcessHorizontalSpace = true;
		gdLPC2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		ShipToId.setLayoutData(gdLPC2);
		ShipToId.setData("name", "ShipToId");
		
		createButtonComposite();
	}
	private Table tblDivisions;
	private void addArticleToDivisions() {
		pnlDivisions = new Composite(grpSearchFields, 0);
		pnlDivisions.setBackgroundMode(0);
		pnlDivisions.setData("name", "pnlDivisions");
		GridData gdDivisions = new GridData();
		gdDivisions.horizontalAlignment = 4;
		gdDivisions.verticalAlignment = 4;
		gdDivisions.grabExcessHorizontalSpace = true;
		gdDivisions.grabExcessVerticalSpace = true;
		gdDivisions.horizontalSpan = 2;
		pnlDivisions.setLayoutData(gdDivisions);
		GridLayout layoutDivisions = new GridLayout(1, false);
		layoutDivisions.marginHeight = 0;
		layoutDivisions.marginWidth = 0;
		layoutDivisions.numColumns = 2;
		pnlDivisions.setLayout(layoutDivisions);
		
		tblDivisions = new Table(pnlDivisions, 67586);
		tblDivisions.setHeaderVisible(true);
		tblDivisions.setLinesVisible(true);
		tblDivisions.setData("name", "tblDivisions");
		GridData gdSrcDivisions = new GridData();
		gdSrcDivisions.horizontalAlignment = 4;
		gdSrcDivisions.verticalAlignment = 4;
		gdSrcDivisions.grabExcessHorizontalSpace = true;
		gdSrcDivisions.grabExcessVerticalSpace = true;
		gdSrcDivisions.heightHint = 100;
		tblDivisions.setLayoutData(gdSrcDivisions);
		TableColumn clmSrcDivisions = new TableColumn(tblDivisions, 16384);
		clmSrcDivisions.setWidth(70);
		clmSrcDivisions.setResizable(true);
		clmSrcDivisions.setMoveable(true);
		
		TableColumn clmSrcDivisionsName = new TableColumn(tblDivisions, 16384);
		clmSrcDivisionsName.setWidth(133);
		clmSrcDivisionsName.setResizable(true);
		clmSrcDivisionsName.setMoveable(true);
		

		
		/* Bindings....*****/
		YRCTableBindingData tblbd = new YRCTableBindingData();
		YRCTblClmBindingData colBindings[] = new YRCTblClmBindingData[2];
		colBindings[0] = new YRCTblClmBindingData();
		colBindings[0].setAttributeBinding("OrganizationCode");
		colBindings[0].setColumnBinding("Division");
		colBindings[0].setSortReqd(true);
		colBindings[1] = new YRCTblClmBindingData();
		colBindings[1].setAttributeBinding("OrganizationName");
		colBindings[1].setColumnBinding("Name");
		colBindings[1].setSortReqd(true);
		tblbd.setSortRequired(true);
		tblbd.setSourceBinding("Divisions:/OrganizationList/Organization");
		tblbd.setName("tblDivisions");
		tblbd.setTblClmBindings(colBindings);
		tblDivisions.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, tblbd);
		tblDivisions.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) { 
				Widget ctrl = e.widget;
			    String ctrlName = (String)ctrl.getData("name");
			    if(ctrlName != null)
			    {
			    	TableItem tblItems[] = getTblDivisions().getSelection();
			        if(tblItems.length > 0){
			        	for(int i = 0; i < tblItems.length; i++){
			        	Element eleDetailsInput = (Element)tblItems[i].getData();
			        	String txtDivisionIDs = txtDivisionID.getText();
						
							if( !YRCPlatformUI.isVoid(txtDivisionIDs)){
								txtDivisionID.setText(txtDivisionIDs+","+eleDetailsInput.getAttribute("OrganizationCode"));
							} else {
								txtDivisionID.setText(eleDetailsInput.getAttribute("OrganizationCode"));
							}
						}
			        	
						}
			        }
				}
		});
	}
		
	private void createPnlReplaceButtonHolder() {

		RowLayout pnlBottomButtonHolderRL1 = new RowLayout();
		GridData pnlBottomButtonHolderGD1 = new GridData();
		Label dummyLbl=new Label(grpSrchByDivsFields, SWT.RIGHT);
		dummyLbl.setText("");
		pnlBottomButtonHolder = new Composite(grpSrchByDivsFields, SWT.RIGHT);		   
		btnReplaceItemList = new Button(pnlBottomButtonHolder, SWT.RIGHT|SWT.END);
		pnlBottomButtonHolder.setLayoutData(pnlBottomButtonHolderGD1);
		pnlBottomButtonHolder.setLayout(pnlBottomButtonHolderRL1);
		pnlBottomButtonHolderGD1.horizontalAlignment =SWT.END;
		btnReplaceItemList.setText("Replace");
		pnlBottomButtonHolderRL1.marginTop = 0;
		pnlBottomButtonHolderRL1.marginBottom = 5;
		btnReplaceItemList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
//				if(tblSearchResults.getSelectionIndex() >-1) 
				    myBehavior.replaceItems();
			}
		});
		
		
	}

	private void createButtonComposite() {
		GridLayout btnPanelLayout = new GridLayout();
		GridData btnPanelLayoutData = new GridData();
		pnlButtons = new Composite(grpSrchByCustomersFields, SWT.NONE);		   
		btnPanelLayoutData.grabExcessHorizontalSpace = true;
		btnPanelLayoutData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		btnPanelLayoutData.horizontalSpan = 6;
		pnlButtons.setLayoutData(btnPanelLayoutData);
		pnlButtons.setLayout(btnPanelLayout);
		btnPanelLayout.numColumns = 3;
		// button creation
		btnSearch = new Button(pnlButtons, SWT.NONE);
		btnSearch.setText("Generate_List");
		
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		btnSearch.setLayoutData(gridData);
		btnReset = new Button(pnlButtons, SWT.NONE);
		btnReset.setText("Reset");
		
		btnSearch.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) { 
				if(YRCPlatformUI.isVoid(myBehavior.getFieldValue("txtLPC"))){
					YRCPlatformUI.showError("Message", "Legacy Product Code is mandatory.");
					//myBehavior.getControl("txtLPC").setFocus();
					txtLPC.setFocus();
					return;
				}
				//myBehavior.search();
				//Fetches the first page of search results
				myBehavior.getParentCustomers();
				//myBehavior.getFirstPage();
				tblSearchResults.setFocus();
			}
		});
		
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
		
    	YRCComboBindingData cbd = null;
    	YRCTextBindingData tbd = new YRCTextBindingData();
    	tbd.setName("txtLPC");
    	tbd.setTargetBinding("/XPEDXMyItemsList/XPEDXMyItemsItemsList/XPEDXMyItemsItems/@ItemId");
    	tbd.setSourceBinding("/XPEDXMyItemsList/XPEDXMyItemsItemsList/XPEDXMyItemsItems/@ItemId");
    	txtLPC.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
    	lblLPC.setData(YRCConstants.YRC_CONTROL_NAME,"lblLPC");
        
    	tbd = new YRCTextBindingData();
    	tbd.setName("txtReplaceLPC");
    	tbd.setTargetBinding("/XPEDXMyItemsList/@ReplaceWithLPC");
    	tbd.setSourceBinding("/XPEDXMyItemsList/@ReplaceWithLPC");
    	txtReplaceLPC.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
    	lblReplaceLPC.setData(YRCConstants.YRC_CONTROL_NAME,"lblReplaceLPC");
    	
    	
    	cbd = new YRCComboBindingData();
		cbd.setName("cmbEnterprise");
	    cbd.setSourceBinding("searchEnterprise:/XPEDXMyItemsList/@EnterpriseKey");
        cbd.setTargetBinding("searchEnterprise:/XPEDXMyItemsList/@EnterpriseKey");
        cbd.setCodeBinding("OrganizationCode");
        cbd.setListBinding("OrgList:OrganizationList/Organization");
        cbd.setDescriptionBinding("OrganizationCode");
        cmbEnterprise.setData("YRCComboBindingDefination", cbd);
    	
    	/*YRCListBindingData listbd = new YRCListBindingData();
    	listbd.setName("listDivisions");
    	listbd.setSourceBinding("SearchCriteria:/XPEDXMyItemsList/@Division");
    	listbd.setTargetBinding("SearchCriteria:/XPEDXMyItemsList/@Division");
    	listbd.setCodeBinding("ShipnodeKey");
    	listbd.setListBinding("TeamNodesList:TeamNodesList/TeamNodes");
    	listbd.setDescriptionBinding("ShipnodeKey");
        listDivisions.setData(YRCConstants.YRC_LIST_BINDING_DEFINITION, listbd);*/
        YRCTextBindingData bdDivisionID = new YRCTextBindingData();
        bdDivisionID.setName("DivisionID");
        bdDivisionID.setTargetBinding("/XPEDXMyItemsList/XPEDXMyItemsListShareList/XPEDXMyItemsListShare/@DivisionID");
        bdDivisionID.setSourceBinding("");
        txtDivisionID.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,bdDivisionID);
       // lblDivisionID.setData(YRCConstants.YRC_CONTROL_NAME,"lblDivisionID");
        
       
    }
    
    private void setBindingForSearchResults() {
        YRCTableBindingData bindingData = new YRCTableBindingData();
        YRCTblClmBindingData colBindings[] = new YRCTblClmBindingData[6];
        bindingData.setName("tblSearchResults");
        bindingData.setTargetBinding("replace_input:/XPEDXMyItemsListList");
        bindingData.setSourceBinding("XpedxMilBothLstList:/XpedxMilBothLstList/XpedxMilBothLst");

        colBindings[0] = new YRCTblClmBindingData();
        colBindings[0].setName("clmDivision");
        colBindings[0].setAttributeBinding("@OrganizationName;@OrganizationCode");
        colBindings[0].setKey("DivisionName_DivisionNo_Key");
        colBindings[0].setColumnBinding("Division");
        colBindings[0].setSortReqd(true);

        colBindings[1] = new YRCTblClmBindingData();
        colBindings[1].setAttributeBinding("Name");
        colBindings[1].setColumnBinding("Name");
        colBindings[1].setSortReqd(true);

        colBindings[2] = new YRCTblClmBindingData();
        colBindings[2].setAttributeBinding("@Desc");
        colBindings[2].setColumnBinding("Desc");
        colBindings[2].setSortReqd(true);
        
        colBindings[3] = new YRCTblClmBindingData();
        colBindings[3].setName("clmListType");
        colBindings[3].setAttributeBinding("@ListType");
		colBindings[3].setColumnBinding("ListType/Creator");
		colBindings[3].setSortReqd(true);
		
		colBindings[4] = new YRCTblClmBindingData();
        colBindings[4].setName("clmSapName");
        colBindings[4].setAttributeBinding("@MsapName");
		colBindings[4].setColumnBinding("SAP Parent Name");
		colBindings[4].setSortReqd(true);
		
		colBindings[5] = new YRCTblClmBindingData();
        colBindings[5].setName("clmCheckBox");
        colBindings[5].setAttributeBinding("@Replace");
        colBindings[5].setCheckedBinding("Y");
        colBindings[5].setUnCheckedBinding("N");
        colBindings[5].setFilterReqd(false);
        colBindings[5].setTargetAttributeBinding("XPEDXMyItemsList/@Checked");
       
        
        bindingData.setImageProvider(new IYRCTableImageProvider() {
			public String getImageThemeForColumn(Object element, int columnIndex) {
				Element orderline = (Element) element;
				String sAlreadyChecked = orderline.getAttribute("Replace"); 
				if (columnIndex == 5) {
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
		editors[5] = YRCConstants.YRC_CHECK_BOX_CELL_EDITOR;
		
		bindingData.setFilterReqd(false);
		bindingData.setTblClmBindings(colBindings);
		bindingData.setCellTypes(editors);
		bindingData.setCellModifierRequired(true);
        bindingData.setKeyNavigationRequired(true);
        bindingData.setSortRequired(true);
        bindingData.setDefaultSort(false);;
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

	public XPXMyItemsReplacementToolPanelBehavior getBehavior() {
		return myBehavior;
	}
	@Override
	public XPXPaginationBehavior getPaginationBehavior() {
		// TODO Auto-generated method stub
		return getBehavior();
	}
	public Table getTblDivisions() {
		return tblDivisions;
	}
	
}


package com.xpedx.sterling.rcp.pca.tasks.myitems.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXSortListener;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCCellModifier;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.IYRCTableImageProvider;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;

public class XPXMyItemsListDetailsPanel extends Composite implements IYRCComposite{
	
	private static final String IS_MODIFIED = "isModified";

	private static final String QTY = "Qty";

	private static final String QTY_OLD_VALUE = "QtyOldValue";

	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.tasks.myitems.screen.XPXMyItemsListDetailsPanel";
	
    private XPXMyItemsListDetailsPanelBehavior myBehavior;

	private Composite pnlRoot = null;

	private Composite pnlProfileInfo;

	private Group grpSearchFields;

	private Label lblListName;

	private Text txtListName;

	private Label lblListDesc;

	private Text txtListDesc;

	private Composite pnlButtonHolder;

	private Button btnRemoveItems;

	private Button btnImportList;

	private Button btnExportList;

	private Button btnSaveChanges;

	private Table tblItemsList;

	private Group pnlItemsList;

	private Composite pnlTableHolder;

	private Composite pnlTitleHolder;

	private Label lblTitleIcon;

	private Composite pnlResultHolder;

	private Label lblResult;

	private Button btnEditSharedList;
	
	private Button btnAddItem;

	public XPXMyItemsListDetailsPanel(Composite parent, int style, Object inputObject) {
		super(parent, style);
		initialize();
		setBindingForComponents();
        myBehavior = new XPXMyItemsListDetailsPanelBehavior(this, FORM_ID,inputObject);
	}

	public String getFormId() {
		return FORM_ID;
	}

	public Composite getRootPanel() {
		return pnlRoot;
	}

	private void initialize() {
		GridLayout gridLayoutPnl = new GridLayout(1, false);
		gridLayoutPnl.horizontalSpacing = 2;
		gridLayoutPnl.marginWidth = 3;
		gridLayoutPnl.marginHeight = 3;
		gridLayoutPnl.verticalSpacing = 2;

		pnlRoot = new Composite(this, SWT.NONE);
		pnlRoot.setData("name", "pnlRoot");
		pnlRoot.setLayout(gridLayoutPnl);
		this.setLayout(new FillLayout());
		showRootPanel(true);
		createInfoComposite();
		setSize(new org.eclipse.swt.graphics.Point(600, 600));
	}
	public void showRootPanel(boolean show) {
		pnlRoot.setVisible(show);
	}
	private void createInfoComposite() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginWidth = 1;
		gridLayout1.marginHeight = 1;

		GridData gridData = new GridData();
		gridData.horizontalAlignment = 4;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = 4;
		pnlProfileInfo = new Composite(getRootPanel(), 0);
		pnlProfileInfo.setLayoutData(gridData);
		pnlProfileInfo.setLayout(gridLayout1);
		pnlProfileInfo.setData("name", "pnlProfileInfo");
		createPnlInfo();
		createpnlButtonHolder();
		createItemListsPnlInfo();
//		adjustScrollPnl(bodyScrolledPanel, pnlItemLists,
//				pnlBody, true, true);
		

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

				HEIGHT += 10;
				if (WIDTH < boundWidth)
					WIDTH = boundWidth;
			}
			if (!isHScrollReqd)
				continue;
			WIDTH += boundWidth + 5;
			if (HEIGHT < boundHeight)
				HEIGHT = boundHeight;
		}
//		scrPnl.setMinSize(1000, HEIGHT+500);
		 scrPnl.setMinSize(1000, 3500);
		if (isVScrollReqd
				&& (selectedHeight < scrPnl.getOrigin().y || selectedHeight
						+ selectedPanelHeight > scrPnl.getSize().y
						+ scrPnl.getOrigin().y))
			scrPnl.setOrigin(0, selectedHeight);
		scrParent.layout(true, true);
	}
	private void createPnlInfo() {

		GridLayout srchGroupLayout = new GridLayout();
		GridData gdSrchGroup = new GridData();
		grpSearchFields = new Group(pnlProfileInfo, SWT.NONE);

		gdSrchGroup.grabExcessHorizontalSpace = true;
		gdSrchGroup.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		grpSearchFields.setLayoutData(gdSrchGroup);
		grpSearchFields.setLayout(srchGroupLayout);
		srchGroupLayout.numColumns = 2;

		lblListName = new Label(grpSearchFields, SWT.NONE);
		lblListName.setData(YRCConstants.YRC_CONTROL_NAME, "lblArticleName");
		lblListName.setText("My_Items_List_Name");

		txtListName = new Text(grpSearchFields, SWT.BORDER);
		txtListName.setTextLimit(50);
		GridData gdListName = new GridData();
		gdListName.grabExcessHorizontalSpace = true;
		gdListName.horizontalSpan = 1;
		gdListName.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtListName.setLayoutData(gdListName);

		lblListDesc = new Label(grpSearchFields, SWT.NONE);
		lblListDesc.setText("My_Items_List_Desc");

		txtListDesc = new Text(grpSearchFields, SWT.BORDER);
		txtListDesc.setLayoutData(gdListName);
		txtListDesc.setData("yrc:customType", "Text");
		txtListDesc.setData("name", "txtListDesc");
		txtListDesc.setTextLimit(50);
	}	

	private void createpnlButtonHolder() {
		GridData gridData10 = new GridData();
		gridData10.heightHint = 25;
		gridData10.widthHint = 80;
		GridData gridData5 = new GridData();
		gridData5.heightHint = 25;
		gridData5.widthHint = 80;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = 4;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = 4;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns =8;
		pnlButtonHolder = new Composite(pnlProfileInfo, 0);
		pnlButtonHolder.setLayout(gridLayout2);
		pnlButtonHolder.setLayoutData(gridData2);
		pnlButtonHolder.setData("name", "pnlButtonHolder");
		pnlButtonHolder.setData("yrc:customType", "TaskComposite");

		GridData gdListName = new GridData();
		gdListName.grabExcessHorizontalSpace = true;
		gdListName.horizontalSpan = 2;
		gdListName.widthHint = 200;
		lblResult = new Label(pnlButtonHolder, SWT.FILL);
		lblResult.setText("");
		lblResult.setLayoutData(gdListName);
		lblResult.setVisible(false);
		
		
		btnAddItem = new Button(pnlButtonHolder, 0);
		btnAddItem.setText("Add_Items");
		btnAddItem.setLayoutData(gridData10);
		btnAddItem.setData("name", "btnAddItem");
		btnAddItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.addItem();
			}
		});
		
		btnRemoveItems = new Button(pnlButtonHolder, 0);
		btnRemoveItems.setText("Remove_Items");
		btnRemoveItems.setLayoutData(gridData10);
		btnRemoveItems.setData("name", "btnRemoveItems");
		btnRemoveItems.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.removeItems();
			}
		});
		
		btnEditSharedList = new Button(pnlButtonHolder, SWT.NONE);
		btnEditSharedList.setText("Edit Share List");
		btnRemoveItems.setLayoutData(gridData10);
		btnEditSharedList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.editShareList();
			}
		});
		
		// Cancel button
		btnImportList = new Button(pnlButtonHolder, 0);
		btnImportList.setText("Import_List");
		btnImportList.setLayoutData(gridData10);
		btnImportList.setData("name", "btnImportList");
		btnImportList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.importItemsList();
			}
		});
		
		btnExportList = new Button(pnlButtonHolder, 0);
		btnExportList.setText("Export_List");
		btnExportList.setLayoutData(gridData10);
		btnExportList.setData("name", "btnExportList");
		btnExportList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.exportItemsList();
			}
		});		
		btnSaveChanges = new Button(pnlButtonHolder, 0);
		btnSaveChanges.setText("Save_Changes");
		btnSaveChanges.setLayoutData(gridData10);
		btnSaveChanges.setData("name", "btnSaveChanges");		
		btnSaveChanges.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.saveChangesToMyItemsList();
			}
		});
		
		}

	private void setBindingForComponents() {
		YRCTextBindingData tbd = new YRCTextBindingData();
		tbd.setName("txtListName");
		tbd.setTargetBinding("SaveXPEDXMyItemsListDetail:/XPEDXMyItemsList/@Name");
		tbd.setSourceBinding("getXPEDXMyItemsListDetail:/XPEDXMyItemsList/@Name");
		txtListName.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);

		tbd = new YRCTextBindingData();
		tbd.setName("txtListDesc");
		tbd.setTargetBinding("SaveXPEDXMyItemsListDetail:/XPEDXMyItemsList/@Desc");
		tbd.setSourceBinding("getXPEDXMyItemsListDetail:/XPEDXMyItemsList/@Desc");
		txtListDesc.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		setBindingForItemsListTable();
    }
    private void setBindingForItemsListTable() {
        YRCTableBindingData bindingData = new YRCTableBindingData();
        YRCTblClmBindingData colBindings[] = new YRCTblClmBindingData[10];

        colBindings[0] = new YRCTblClmBindingData();
        colBindings[0].setName("tblClmCheckbox");
        colBindings[0].setAttributeBinding("@Checked");
        colBindings[0].setColumnBinding("#");
        colBindings[0].setCheckedBinding("Y");
        colBindings[0].setUnCheckedBinding("N");
        colBindings[0].setVisible(true);
        colBindings[0].setFilterReqd(false);
        colBindings[0].setTargetAttributeBinding("XPEDXMyItemsItems/@Checked");
        
         colBindings[1] = new YRCTblClmBindingData();
        colBindings[1].setAttributeBinding("Name");
        colBindings[1].setColumnBinding("Product Name");
//        colBindings[1].setSortReqd(true);
/*        colBindings[1].setTargetAttributeBinding("ItemId");
        colBindings[1].setAttributeBinding("@Desc;@ItemId" ); 
        colBindings[1].setColumnBinding("Product Name");
        colBindings[1].setKey("My_Item_List_Name_Key");
*/      
        colBindings[1].setTargetAttributeBinding("XPEDXMyItemsItems/@Name");
        colBindings[1].setSortReqd(true);
        
        
        colBindings[2] = new YRCTblClmBindingData();
        colBindings[2].setAttributeBinding("ItemId");
        colBindings[2].setColumnBinding("SKU#");
//        colBindings[1].setSortReqd(true);
        colBindings[2].setTargetAttributeBinding("XPEDXMyItemsItems/@ItemId");
        
        colBindings[3] = new YRCTblClmBindingData();
        colBindings[3].setAttributeBinding(QTY);
        colBindings[3].setColumnBinding("Quantity");
//        colBindings[1].setSortReqd(true);
        colBindings[3].setTargetAttributeBinding("XPEDXMyItemsItems/@Qty");
        
        colBindings[4] = new YRCTblClmBindingData();        
        colBindings[4].setAttributeBinding("UomDesc"); 
        colBindings[4].setColumnBinding("UOM");        
        colBindings[4].setSortReqd(true);
        
        colBindings[5] = new YRCTblClmBindingData();        
        colBindings[5].setAttributeBinding("JobId"); 
        colBindings[5].setColumnBinding("Cust.LineAcct#1");    
        colBindings[5].setTargetAttributeBinding("XPEDXMyItemsItems/@JobId");
        colBindings[5].setSortReqd(true);
        
        colBindings[6] = new YRCTblClmBindingData();        
        colBindings[6].setAttributeBinding("ItemCustomField1"); 
        colBindings[6].setColumnBinding("Cust.LineField1");   
        colBindings[6].setTargetAttributeBinding("XPEDXMyItemsItems/@ItemCustomField1");
        colBindings[6].setSortReqd(true);
        
        colBindings[7] = new YRCTblClmBindingData();        
        colBindings[7].setAttributeBinding("ItemCustomField2"); 
        colBindings[7].setColumnBinding("Cust.LineField2");   
        colBindings[7].setTargetAttributeBinding("XPEDXMyItemsItems/@ItemCustomField2");
        colBindings[7].setSortReqd(true);
        
        
        colBindings[8] = new YRCTblClmBindingData();        
        colBindings[8].setAttributeBinding("ItemCustomField3"); 
        colBindings[8].setColumnBinding("Cust.LineField3");  
        colBindings[8].setTargetAttributeBinding("XPEDXMyItemsItems/@ItemCustomField3");
        colBindings[8].setSortReqd(true);
        
        colBindings[9] = new YRCTblClmBindingData();        
        colBindings[9].setAttributeBinding("ItemOrder"); 
        colBindings[9].setColumnBinding("Sequence");     
        colBindings[9].setTargetAttributeBinding("XPEDXMyItemsItems/@ItemOrder");
        colBindings[9].setSortReqd(true);
        
        bindingData.setImageProvider(new IYRCTableImageProvider() {
        	public String getImageThemeForColumn(Object element, int columnIndex) {
        	Element orderline = (Element) element;
        	String sAlreadyChecked = orderline.getAttribute("Checked");
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

        
        IYRCCellModifier cellModifier = new IYRCCellModifier() {
			protected boolean allowModify(String property, String value, Element element) {
//				System.out.println("allowModify-->property:["+property+"]- value["+value+"]- element["+YRCXmlUtils.getString(element)+"]");
				return true;
			}

			protected int allowModifiedValue(String property, String value, Element element) {
				return IYRCCellModifier.MODIFY_OK;
			}

			protected String getModifiedValue(String property, String value, Element element) {
				if("@Qty".equals(property)){
					String oldValue = "0.00";
					if(element.hasAttribute(QTY_OLD_VALUE)){
						oldValue = element.getAttribute(QTY_OLD_VALUE);
					}
					else {
						oldValue = element.getAttribute(QTY);
						if ("".equalsIgnoreCase(element.getAttribute(QTY))
								|| "null".equalsIgnoreCase(element
										.getAttribute(QTY))) {
							oldValue = "0.00";
							element.setAttribute(QTY_OLD_VALUE, value);
							element.setAttribute(QTY, value);
						} else {
							element.setAttribute(QTY_OLD_VALUE, oldValue);
						}
					}
					
					try {
						if(!YRCPlatformUI.equals(Double.valueOf(value),Double.valueOf(oldValue))){
							element.setAttribute(IS_MODIFIED, "Y");
						} else {
							if(element.hasAttribute(IS_MODIFIED)){
								element.removeAttribute(IS_MODIFIED);
							}
						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				
				return value;
			}
        };
        
		String[] editors = new String[tblItemsList.getColumnCount()];
		editors[0] = YRCConstants.YRC_CHECK_BOX_CELL_EDITOR;
		editors[3] = YRCConstants.YRC_TEXT_BOX_CELL_EDITOR;
		bindingData.setCellTypes(editors);
		bindingData.setCellModifierRequired(true);
		bindingData.setCellModifier(cellModifier);
		bindingData.setSortRequired(true);
        bindingData.setSourceBinding("getXPEDXMyItemsListDetail:/XPEDXMyItemsList/XPEDXMyItemsItemsList/XPEDXMyItemsItems");
        bindingData.setTargetBinding("SaveXPEDXMyItemsListDetail:/XPEDXMyItemsList/XPEDXMyItemsItemsList");
        bindingData.setName("tblItemsList");
        bindingData.setTblClmBindings(colBindings);
        bindingData.setKeyNavigationRequired(true);
        tblItemsList.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, bindingData);
    }
	
	private void createItemListsPnlInfo() {
		GridData pnlItemsListGD = new org.eclipse.swt.layout.GridData();
		GridLayout pnlItemsListGL = new GridLayout();
		pnlItemsList = new Group(pnlProfileInfo, SWT.NONE);		   
		createPnlTitleHolder();
		createPnlTableHolder();
		pnlItemsList.setLayout(pnlItemsListGL);
		pnlItemsList.setLayoutData(pnlItemsListGD);
		pnlItemsListGL.numColumns = 2;
		pnlItemsListGL.horizontalSpacing = 0;
		pnlItemsListGL.verticalSpacing = 0;
		pnlItemsListGL.marginWidth = 0;
		pnlItemsListGL.marginHeight = 0;
		pnlItemsListGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlItemsListGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlItemsListGD.grabExcessHorizontalSpace = true;
		pnlItemsListGD.grabExcessVerticalSpace = true;
	}
	
	private void createPnlTableHolder() {
		GridLayout gridLayout8 = new GridLayout();
		GridData pnlTableHolderGD = new org.eclipse.swt.layout.GridData();
		pnlTableHolder = new Composite(pnlItemsList, SWT.NONE);		   
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
		GridData tblItemsListGD = new org.eclipse.swt.layout.GridData();
		tblItemsList = new Table(pnlTableHolder,SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );
		tblItemsList.setData("name", "tblItemsList");
	
		TableColumn tblClmCheckbox = new TableColumn(tblItemsList, SWT.NONE);
		tblClmCheckbox.setText("#");
		tblClmCheckbox.setWidth(30);//
		tblClmCheckbox.setResizable(false);
		
		TableColumn clmProductDesc = new TableColumn(tblItemsList, SWT.NONE);
		clmProductDesc.setText("Product_desc");
		clmProductDesc.setWidth(150);
		

		TableColumn clmProductSKU = new TableColumn(tblItemsList, SWT.NONE);
		clmProductSKU.setText("SKU");
		clmProductSKU.setWidth(150);
		
		
		TableColumn clmQuantity = new TableColumn(tblItemsList, SWT.NONE);
		clmQuantity.setText("Quantity");
		clmQuantity.setWidth(50);
		
		TableColumn clmUom = new TableColumn(tblItemsList, SWT.NONE);
		clmUom.setText("UOM");
		clmUom.setWidth(50);
		
		
		TableColumn clmCustAccountField = new TableColumn(tblItemsList, SWT.NONE);
		clmCustAccountField.setText("Cust.LineAcct#1");
		clmCustAccountField.setWidth(50);
		
		TableColumn clmCustField1 = new TableColumn(tblItemsList, SWT.NONE);
		clmCustField1.setText("Cust.LineField1");
		clmCustField1.setWidth(50);
		
		TableColumn clmCustField2 = new TableColumn(tblItemsList, SWT.NONE);
		clmCustField2.setText("Cust.LineField2");
		clmCustField2.setWidth(50);
		
		TableColumn clmCustField3 = new TableColumn(tblItemsList, SWT.NONE);
		clmCustField3.setText("Cust.LineField3");
		clmCustField3.setWidth(50);
		
		
		TableColumn clmSequence = new TableColumn(tblItemsList, SWT.NONE);
		clmSequence.setText("Sequence");
		clmSequence.setWidth(50);
		

		tblItemsListGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblItemsListGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblItemsListGD.grabExcessHorizontalSpace = true;
		tblItemsListGD.grabExcessVerticalSpace = true;
//		tblItemsListGD.horizontalSpan = 2;
		tblItemsList.setLayoutData(tblItemsListGD);
		tblItemsList.setHeaderVisible(true);
		tblItemsList.setLinesVisible(true);

	}
	
	private void createPnlTitleHolder() {
		GridLayout pnlTitleHolderGL = new GridLayout();
		GridData pnlTitleHolderGD = new org.eclipse.swt.layout.GridData();
		pnlTitleHolder = new Composite(pnlItemsList, SWT.NONE);		   
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
		
		XPXUtils.addGradientPanelHeader(pnlTitleHolder, "List of Items", true);
	}
	
 
    public IYRCPanelHolder getPanelHolder() {
        // TODO Complete getPanelHolder
        return null;
    }

	public String getHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	public XPXMyItemsListDetailsPanelBehavior getMyBehavior() {
		return myBehavior;
	}
	public void showResultMessage(String resultMessage, String isupdated)
	{
		
		if (isupdated != null) {
			this.lblResult.setText(resultMessage);
			this.lblResult.setVisible(true);

		} else {
			this.lblResult.setText("");
			this.lblResult.setVisible(false);

		}

	}

	public void setMyBehavior(XPXMyItemsListDetailsPanelBehavior myBehavior) {
		this.myBehavior = myBehavior;
	}
}

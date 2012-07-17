package com.xpedx.sterling.rcp.pca.tasks.myitems.screen;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.yfc.rcp.IYRCApiCallbackhandler;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCScrolledCompositeListener;
import com.yantra.yfc.rcp.YRCTextBindingData;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXCreateNewMyItemsListPopupPanel extends Composite implements IYRCComposite{
	
	private Composite pnlRoot;
	private Group grpSearchFields;
	private Label lblListName;
	private Text txtListName;
	private Label lblCustomerId;
	private Text txtCustomerId;
	private Label lblCustomerName;
	private Label lblCustomerNumber;
	private Text txtCustomerName;
	private Text txtCustomerNumber;
	private Text txtCustomerIDSelected;
	private Composite pnlButtonHolder;
	private Button btnSearchChildCustomers;
	private Button btnCancel;
	private XPXCreateNewMyItemsListPanelBehavior myBehavior;
	private Element elePageInput;
	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.tasks.myitems.screen.XPXCreateNewMyItemsListPopupPanel";
	private Composite pnlProfileInfo;
	private ScrolledComposite scrolledPnlforInfo;
	private Composite pnlPrimaryInformation;
	private Label lblListDesc;
	private Text txtListDesc;
	private Button btnCreate;
	private Label lblCustomers;
	private Combo comboCustomers;
	private YRCComboBindingData cbd;
	private Button btnGetSharedList;
	private Button btnReset;
	Tree tree;
	TreeItem localiItem=null;
	private Composite pnlEntry;
		
	public XPXCreateNewMyItemsListPopupPanel(Composite parent, int style, Object inputObject) {
		super(parent, style);
		elePageInput = (Element) inputObject;
		initialize();
		setBindingForComponents();
		myBehavior = new XPXCreateNewMyItemsListPanelBehavior(this, FORM_ID);
		adjustScrollPnl(scrolledPnlforInfo, pnlPrimaryInformation,pnlEntry, false, true);
		pnlRoot.layout(true, true);
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
	
	private void createInfoComposite() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginWidth = 1;
		gridLayout1.marginHeight = 1;

		GridData gridData = new GridData();
		gridData.horizontalAlignment = 4;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = 4;
		pnlEntry = new Composite(getRootPanel(), 0);
		pnlEntry.setLayoutData(gridData);
		pnlEntry.setLayout(gridLayout1);
		pnlEntry.setData("name", "pnlEntry");
		createPnlInfo();
	}

	private void createPnlInfo() {

		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = SWT.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = SWT.FILL;
		scrolledPnlforInfo = new ScrolledComposite(pnlEntry, SWT.V_SCROLL);
		scrolledPnlforInfo.setLayoutData(gridData1);
		scrolledPnlforInfo.setExpandHorizontal(true);
		scrolledPnlforInfo.setExpandVertical(true);
		scrolledPnlforInfo.setAlwaysShowScrollBars(true);
		scrolledPnlforInfo.addListener(SWT.CURSOR_SIZEALL,
				new YRCScrolledCompositeListener(scrolledPnlforInfo));

		GridLayout gridLayout = new GridLayout();
		// gridLayout.horizontalSpacing = 3;
		gridLayout.verticalSpacing = 3;
		gridLayout.marginWidth = 1;
		gridLayout.marginHeight = 1;
		gridLayout.numColumns = 1;

		GridData gridData0 = new GridData();
		gridData0.horizontalAlignment = SWT.FILL;
		gridData0.grabExcessVerticalSpace = false;
		gridData0.grabExcessHorizontalSpace = true;
		gridData0.verticalAlignment = SWT.FILL;

		pnlPrimaryInformation = new Composite(scrolledPnlforInfo, 0);
		pnlPrimaryInformation.setLayoutData(gridData0);
		pnlPrimaryInformation.setLayout(gridLayout);
		pnlPrimaryInformation.setData("name", "pnlPrimaryInformation");

		GridLayout gridLayout1 = new GridLayout();
		scrolledPnlforInfo.setLayout(gridLayout1);
		scrolledPnlforInfo.setContent(pnlPrimaryInformation);
		scrolledPnlforInfo.setData("yrc:customType", "TaskComposite");
		scrolledPnlforInfo.setData("name", "scrolledPnlforInfo");
		
		GridLayout srchGroupLayout = new GridLayout();
		GridData gdSrchGroup = new GridData();
		grpSearchFields = new Group(pnlPrimaryInformation, SWT.NONE);

		gdSrchGroup.grabExcessHorizontalSpace = true;
		gdSrchGroup.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		grpSearchFields.setLayoutData(gdSrchGroup);
		grpSearchFields.setLayout(srchGroupLayout);
		srchGroupLayout.numColumns = 3;

		// Add fields starts here
		// List Name
		lblListName = new Label(grpSearchFields, SWT.NONE);
		lblListName.setData(YRCConstants.YRC_CONTROL_NAME, "lblArticleName");
		lblListName.setText("My_Items_List_Name");

		txtListName = new Text(grpSearchFields, SWT.BORDER);
		txtListName.setTextLimit(50);
		GridData gdListName = new GridData();
		gdListName.grabExcessHorizontalSpace = true;
		gdListName.horizontalSpan = 2;
		gdListName.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtListName.setLayoutData(gdListName);

		// List Description
		lblListDesc = new Label(grpSearchFields, SWT.NONE);
		lblListDesc.setText("My_Items_List_Desc");

		txtListDesc = new Text(grpSearchFields, SWT.BORDER);
		txtListDesc.setLayoutData(gdListName);
		txtListDesc.setData("yrc:customType", "Text");
		txtListDesc.setData("name", "txtListDesc");
		txtListDesc.setTextLimit(50);
		
		GridData gdCustomerId = new GridData();
		gdCustomerId.grabExcessHorizontalSpace = true;
		gdCustomerId.horizontalSpan = 2;
		gdCustomerId.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		
		lblCustomerId = new Label(grpSearchFields, SWT.NONE);
		lblCustomerId.setText("Complete Master Customer Number");

		txtCustomerId = new Text(grpSearchFields, SWT.BORDER);
		txtCustomerId.setLayoutData(gdCustomerId);
		txtCustomerId.setData("yrc:customType", "Text");
		txtCustomerId.setData("name", "txtCustomerId");
		txtCustomerId.setTextLimit(50);	
		
		lblCustomerNumber = new Label(grpSearchFields, SWT.NONE);
		lblCustomerNumber.setText("Master Customer Number Starts With");

		txtCustomerNumber = new Text(grpSearchFields, SWT.BORDER);
		txtCustomerNumber.setLayoutData(gdCustomerId);
		txtCustomerNumber.setData("yrc:customType", "Text");
		txtCustomerNumber.setData("name", "txtCustomerNumber");
		txtCustomerNumber.setTextLimit(50);
		
		lblCustomerName = new Label(grpSearchFields, SWT.NONE);
		lblCustomerName.setText("Master Customer Name Starts With");
		
		txtCustomerName = new Text(grpSearchFields, SWT.BORDER);
		txtCustomerName.setLayoutData(gdCustomerId);
		txtCustomerName.setData("yrc:customType", "Text");
		txtCustomerName.setData("name", "txtCustomerName");
		txtCustomerName.setTextLimit(50);
		
		GridData gdSearchButton = new GridData();
		gdSearchButton.grabExcessHorizontalSpace = false;
		gdSearchButton.horizontalSpan = 3;
		gdSearchButton.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gdSearchButton.widthHint = 50;
		
		btnSearchChildCustomers = new Button(grpSearchFields, 0);
		btnSearchChildCustomers.setText("Search");
		btnSearchChildCustomers.setLayoutData(gdSearchButton);
		btnSearchChildCustomers.setData("name", "btnSearchChildCustomers");
		btnSearchChildCustomers.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.searchCustomers();
			}
		});		
		
		lblCustomers = new Label(grpSearchFields, SWT.NONE);
		lblCustomers.setText("Customer:");
		lblCustomers.setData("name", "lblCustomers");

		/*comboCustomers = new Combo(grpSearchFields, 8);
		comboCustomers.setLayoutData(gdCustomerId);
		comboCustomers.setTextLimit(50);
		comboCustomers.setData("name", "comboCustomers");*/
		
		txtCustomerIDSelected = new Text(grpSearchFields, SWT.BORDER);
		txtCustomerIDSelected.setLayoutData(gdCustomerId);
		txtCustomerIDSelected.setData("yrc:customType", "Text");
		txtCustomerIDSelected.setData("name", "txtCustomerIDSelected");
		txtCustomerIDSelected.setTextLimit(50);
		txtCustomerIDSelected.setEditable(false);
		
		
		GridData gdbtnSharedButton = new GridData();
		gdbtnSharedButton.grabExcessHorizontalSpace = true;
		gdbtnSharedButton.horizontalSpan = 2;
		gdbtnSharedButton.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gdbtnSharedButton.widthHint = 50;
		
		btnReset = new Button(grpSearchFields, 0);
		btnReset.setText("Reset");
		btnReset.setLayoutData(gdbtnSharedButton);
		btnReset.setData("name", "btnReset");
		btnReset.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				disposeTreeStructure();
			}
		});
		
		
		
		GridData gdbtnResetButton = new GridData();
		gdbtnResetButton.grabExcessHorizontalSpace = false;
		gdbtnResetButton.horizontalSpan = 1;
		gdbtnResetButton.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gdbtnResetButton.widthHint = 150;
		
		btnGetSharedList = new Button(grpSearchFields, 0);
		btnGetSharedList.setText("Get_child_customers");
		btnGetSharedList.setLayoutData(gdbtnResetButton);
		btnGetSharedList.setData("name", "btnGetSharedList");
		btnGetSharedList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.getSharedList();
			}
		});

		
		GridLayout gridLayoutTree = new GridLayout();
		gridLayoutTree.marginWidth = 1;
		gridLayoutTree.marginHeight = 1;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = 4;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = 4;
		pnlProfileInfo = new Composite(pnlPrimaryInformation, 0);
		pnlProfileInfo.setLayoutData(gridData);
		pnlProfileInfo.setLayout(gridLayoutTree);
		pnlProfileInfo.setData("name", "pnlProfileInfo");
		
		Label lblSearchCriteriaTitle = new Label(pnlProfileInfo, SWT.LEFT);
		GridData lblSearchCriteriaTitlelayoutData = new GridData();
		lblSearchCriteriaTitlelayoutData.verticalAlignment = 16777216;
		lblSearchCriteriaTitlelayoutData.grabExcessHorizontalSpace = true;
		lblSearchCriteriaTitle.setLayoutData(lblSearchCriteriaTitlelayoutData);
		lblSearchCriteriaTitle.setText("MIL_Sharable_List");
		
		this.createTreeStructure(pnlProfileInfo); //-function to create tree structure
		
		
		
		createpnlButtonHolder();
	}	

	private void setBindingForComponents() {
		YRCTextBindingData tbd = new YRCTextBindingData();
		tbd.setName("txtListName");
		tbd.setTargetBinding("SaveMyItemsList:/XPEDXMyItemsList/@Name");
		tbd.setSourceBinding("/XPEDXMyItemsList/@Name");
		txtListName.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);

		tbd = new YRCTextBindingData();
		tbd.setName("txtListDesc");
		tbd.setTargetBinding("SaveMyItemsList:/XPEDXMyItemsList/@Desc");
		tbd.setSourceBinding("/XPEDXMyItemsList/@Desc");
		txtListDesc.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setName("txtCustomerId");
		tbd.setTargetBinding("SaveMyItemsList:/MyItemsList/@QryCustomerID");
		txtCustomerId.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setName("txtCustomerName");
		tbd.setTargetBinding("SaveMyItemsList:/MyItemsList/@QryCustomerID");
		txtCustomerName.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setName("txtCustomerNumber");
		tbd.setTargetBinding("SaveMyItemsList:/MyItemsList/@QryCustomerID");
		txtCustomerNumber.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setName("txtCustomerIDSelected");
		tbd.setTargetBinding("SaveMyItemsList:/XPEDXMyItemsList/@CustomerID");
		txtCustomerIDSelected.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		/*cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@CustomerID");
		cbd.setDescriptionBinding("@CustomerID");
		cbd.setListBinding("XPXGetCustomerListService:/CustomerList/Customer");
//		cbd.setSourceBinding("XPXCustomerIn:/CustomerList/Customer/Extn/@ExtnECSR1Key");
		cbd.setTargetBinding("SaveMyItemsList:/XPEDXMyItemsList/@CustomerID");
		cbd.setName("comboCustomers");
		comboCustomers.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);	*/
	}


	public String getFormId() {
		return FORM_ID;
	}

	public Composite getRootPanel() {
		return pnlRoot;
	}
	
	private void createpnlButtonHolder() {
		GridData gridData10 = new GridData();
		gridData10.heightHint = 25;
		gridData10.widthHint = 80;
		GridData gridData5 = new GridData();
		gridData5.heightHint = 25;
		gridData5.widthHint = 80;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = 3;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = SWT.BEGINNING;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		pnlButtonHolder = new Composite(pnlRoot, 0);
		pnlButtonHolder.setLayout(gridLayout2);
		pnlButtonHolder.setLayoutData(gridData2);
		pnlButtonHolder.setData("name", "pnlButtonHolder");
		pnlButtonHolder.setData("yrc:customType", "TaskComposite");
		
		btnCreate = new Button(pnlButtonHolder, 0);
		btnCreate.setText("MyItemsList_Create");
		btnCreate.setLayoutData(gridData5);
		btnCreate.setData("name", "btnCreate");
		btnCreate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.createMyItemsList();
			}
		});
		
		// Cancel button
		btnCancel = new Button(pnlButtonHolder, 0);
		btnCancel.setText("MyItemsList_Close");
		btnCancel.setLayoutData(gridData10);
		btnCancel.setData("name", "btnCancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getParent().getShell().close();
			}
		});
//		adjustScrollPnl(scrolledPnlforInfo, pnlPrimaryInformation,pnlEntry, false, true);
	}

	public void adjustScrollPnl(ScrolledComposite scrPnl, Composite scrChild, Composite scrParent, boolean isHScrollReqd, boolean isVScrollReqd) {
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
		if (isVScrollReqd && (selectedHeight < scrPnl.getOrigin().y || selectedHeight + selectedPanelHeight > scrPnl.getSize().y + scrPnl.getOrigin().y))
			scrPnl.setOrigin(0, selectedHeight);
		scrParent.layout(true, true);
	}

	public Object getInput() {
		Object inObject = null;
		org.eclipse.ui.part.WorkbenchPart currentPart = YRCDesktopUI
				.getCurrentPart();
		inObject = ((YRCEditorPart) currentPart).getEditorInput();
		return inObject;
	}

	/**
	 * Returns Input Object of the Panel
	 * 
	 * @return Element object
	 */
	public Element getPageInput() {
		return elePageInput;
	}

	public String getHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	public IYRCPanelHolder getPanelHolder() {
		// TODO Auto-generated method stub
		return null;
	}	

	public XPXCreateNewMyItemsListPanelBehavior getMyBehavior() {
		return myBehavior;
	}
	
	public void showRootPanel(boolean show) {
		pnlRoot.setVisible(show);
	}
	
	/** STARTS Here *****/


	private void createTreeStructure(Composite composite) {

		

		
		tree = new Tree (composite, SWT.CHECK|SWT.NONE);
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//--Expand listner used to add functionality on expanding the tree like Calling API & Setting data to Tree Items
		tree.addListener(SWT.Expand, new Listener()
		{
			public void handleEvent(Event event) 
			{
				//Expansion Event handler

				String parentValue = null;
				localiItem = (TreeItem) event.item;
				Element eleCust = (Element) localiItem.getData("data");
				if(null != eleCust){
					parentValue = eleCust.getAttribute("CustomerID");
				} else {
					parentValue=localiItem.getText();
				}
				
				IYRCApiCallbackhandler handler = new IYRCApiCallbackhandler(){

					//--handleApiCompletion function used to call API & fetch the response from API
					public void handleApiCompletion(YRCApiContext ctx) {
						System.out.println("ctx:"+ctx);
						if (ctx.getInvokeAPIStatus() > 0){
							if (myBehavior.getOwnerForm().isDisposed()) {
								YRCPlatformUI.trace("Page is Disposed");
							} else {
								String[] apinames = ctx.getApiNames();
								for (int i = 0; i < apinames.length; i++) {
									
									String apiname = apinames[i];
									if ("getCustomerList".equals(apiname)) {
																		
										Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
										if(null != outXml) {
											NodeList customerNodeList = outXml.getElementsByTagName("Customer");
											ArrayList<Element> arrlst=new ArrayList<Element>();
											for (int j = 0; j <  customerNodeList.getLength(); j++) {
												
												Element customerNode = (Element) customerNodeList.item(j);
												arrlst.add(customerNode);
																							
												
											}
											((XPXCreateNewMyItemsListPopupPanel)myBehavior.getOwnerForm()).setTreeValues(localiItem, arrlst);
										}
									}
								}
							}
						}
					}
					
				};
				
				TreeItem[] childItems = localiItem.getItems();
				if(childItems.length==1 && YRCPlatformUI.isVoid(childItems[0].getText())) {
					YRCApiContext ctx = new YRCApiContext();
					ctx.setApiName("getCustomerList");
					ctx.setInputXml(YRCXmlUtils.createFromString("<Customer ParentCustomerKey='"+YRCXmlUtils.getAttribute(eleCust, "CustomerKey")+"' />"));
					ctx.setFormId(getFormId());
					ctx.setShowError(false);
					ctx.setUserData("isRefreshReqd", String.valueOf(false));
					YRCPlatformUI.callApi(ctx, handler);
				}
			
			}
		}
		);
		//---Selection Listener used to Check & uncheck the parent  & Viceversa base on condition
		tree.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	        	//Check box event Handler

	            if (event.detail == SWT.CHECK) {
	                TreeItem item = (TreeItem) event.item;
	                boolean checked = item.getChecked();
	                checkItems(item, checked);     //--function used to check child nodes if parent is checked
	                checkPath( item.getParentItem(), checked, false); //--function used to check parent if all its child are checked
	            }
	        
	        }
	    });
		
	}
	
	/**--setTreeValues function used to set the values of child Tree Items in Tree structure- **/
	public void setTreeValues(Object parent, List custId) {
		
		if(null == parent){
			TreeItem iItem = new TreeItem (tree, SWT.NULL);
			Element eleCust=(Element) custId.get(0);
			String CustomerID="";

			String orgName=YRCXmlUtils.getAttributeValue(eleCust, "Customer/BuyerOrganization/@OrganizationName");
			String orgId=YRCXmlUtils.getAttributeValue(eleCust, "Customer/BuyerOrganization/@OrganizationCode");
			String customerType=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnSuffixType");
			CustomerID=orgId;

			iItem.setText(orgName+" ("+CustomerID+")");
			iItem.setData("data", eleCust);
			iItem.setItemCount(1);
		}
		
		if (parent instanceof TreeItem) {
			TreeItem localiItem2 = (TreeItem) parent;
			
			// remove dummy item.
			TreeItem[] childItems = localiItem2.getItems();
			if(childItems.length==1 && YRCPlatformUI.isVoid(childItems[0].getText())) {
				childItems[0].dispose();
			}
			
			for (int i=0; i<custId.size(); i++) 
			{
				Element eleCust = (Element)custId.get(i);
				String CustomerID="";
				TreeItem iiItem = new TreeItem (localiItem2, 1);
				String orgName=YRCXmlUtils.getAttributeValue(eleCust, "Customer/BuyerOrganization/@OrganizationName");
				String orgId=YRCXmlUtils.getAttributeValue(eleCust, "Customer/BuyerOrganization/@OrganizationCode");
			
				String shipFromBranch=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnShipFromBranch");
				String legacyNo=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnLegacyCustNumber");
				String billTosuffix=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnBillToSuffix");
				String shipToSuffix=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnShipToSuffix");
				String customerType=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnSuffixType");
				
				if("MC".equalsIgnoreCase(customerType)){
					CustomerID=orgId;
				}
				else if("C".equalsIgnoreCase(customerType)){
					 CustomerID=orgId;
				}
				else if("B".equalsIgnoreCase(customerType)){
					CustomerID=shipFromBranch+"-"+legacyNo+"-"+billTosuffix;
				}
				else if("S".equalsIgnoreCase(customerType)){
						CustomerID=shipFromBranch+"-"+legacyNo+"-"+shipToSuffix;
				}
				iiItem.setText(orgName+" ("+CustomerID+")");
				
				iiItem.setData("data",eleCust);
				iiItem.setItemCount(1);
				
			}
			
			/* new child will be marked checked automatically, if the parent is already checked.*/
			if (custId.size()>0 && localiItem2.getChecked()) {
                checkItems(localiItem2, true);
                checkPath( localiItem2.getParentItem(), true, false);
            }
		}
	}

	void checkItems(TreeItem item, boolean checked) {

	    item.setGrayed(false);
	    item.setChecked(checked);

	    TreeItem[] items = item.getItems();
	    for (int i = 0; i < items.length; i++) {

	        checkItems(items[i], checked);
	    }
	}
    
    void checkPath( TreeItem item, boolean checked, boolean grayed) {

	    if (item == null) return;

	    if (grayed) {

	        checked = true;
	    } else {

	        int index = 0;
	        TreeItem[] items = item.getItems();
	        int count= item.getItemCount();
	        int i=0;
	        while (index < items.length) {
	        	
	            TreeItem child = items[index];
	            if(child.getChecked()){
	            	i++;
	            }
	            index++;

	        }
	        
            if(count==i){
            	checked=true;
            }
            else
            	checked=false;
	    }
	    item.setChecked(checked);

	    grayed=false;
	    checkPath(item.getParentItem(), checked, grayed);
    }
    
    /** Prepare Input - Used to prepare the input for the Update API */
	public void getTargetModelForUpdateAssignments() {
		TreeItem[] item = tree.getItems();
		this.iterateThroughChilds(item,false); 
	}
	

	/** function used to iterate through tree & find the function based on their qualification for Addition or deletion of node */
	private void iterateThroughChilds(TreeItem[] item, boolean isParentChecked) {
		
		for (TreeItem treeItem : item) {
			
			Element eleCust = (Element)treeItem.getData("data");
			String strCustID = eleCust.getAttribute("CustomerID");
			TreeItem iiparent=null;
			String strMILShareKey = null;
			String strCustPath = null;
			String strDivisionID = null;
			iiparent=treeItem.getParentItem();
			if(iiparent!=null){
				if(iiparent.getChecked()){
					isParentChecked=true;
				}
				else
					isParentChecked=false;
			}
			
			if(!isParentChecked && treeItem.getChecked() ){
					strDivisionID = this.getDivisionID(treeItem);
					strCustPath = this.getCustomerPath(treeItem);
					myBehavior.createManageAssignmentInput(strCustID, strCustPath, strDivisionID, true);   //---used to create an entry into DB.
			}
			
			TreeItem[] childItem = treeItem.getItems();
			if(childItem.length==1){
				if(YRCPlatformUI.isVoid(childItem[0].getText())){
					continue;
				}
			}
			this.iterateThroughChilds(childItem, isParentChecked);
		}
	}
	
	/** Get Method - Customer Path */
	private String getCustomerPath(TreeItem treeItem) {
		
		Element eleSelectedCust = (Element)treeItem.getData("data");
		String strCustomerID = eleSelectedCust.getAttribute("CustomerID");
		String strCustomerPath = strCustomerID;
		
		TreeItem parentItem = treeItem.getParentItem();
		while(null != parentItem){
			Element eleCust = (Element)parentItem.getData("data");
			strCustomerID = eleCust.getAttribute("CustomerID");
			if( !YRCPlatformUI.isVoid(strCustomerPath))
				strCustomerPath = "|" + strCustomerPath;
			strCustomerPath = strCustomerID + strCustomerPath;
			parentItem = parentItem.getParentItem();
		}
		if(!YRCPlatformUI.isVoid(myBehavior.getCustomerPathPrefix())){
			strCustomerPath = myBehavior.getCustomerPathPrefix() +"|"+ strCustomerPath; 
		}
		return strCustomerPath;
	}
	
	/** Get Method - Division ID */
	private String getDivisionID(TreeItem treeItem) {
		String strDivisionID = "";
		Element eleSelectedCust = (Element)treeItem.getData("data");
		Element eleExtn = YRCXmlUtils.getChildElement(eleSelectedCust, "Extn");
		strDivisionID = eleExtn.getAttribute("ExtnShipFromBranch");
		return strDivisionID;
	}
	private void disposeTreeStructure(){
		tree.dispose();
		txtCustomerIDSelected.setText("");         
		txtCustomerId.setText("");
		txtCustomerName.setText("");
		txtCustomerNumber.setText("");
		txtListName.setText("");
		txtListDesc.setText("");
		createTreeStructure(pnlProfileInfo);
		XPXCreateNewMyItemsListPanelBehavior.oldCustomerID = " ";
	}
}

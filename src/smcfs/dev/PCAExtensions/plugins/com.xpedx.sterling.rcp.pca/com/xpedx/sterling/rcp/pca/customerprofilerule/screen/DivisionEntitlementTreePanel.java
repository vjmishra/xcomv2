/*
 * Created on Apr 14,2010
 *
 */
package com.xpedx.sterling.rcp.pca.customerprofilerule.screen;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.yfc.rcp.IYRCApiCallbackhandler;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class DivisionEntitlementTreePanel extends Composite implements
		IYRCComposite {

	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.customerprofilerule.screen.DivisionEntitlementTreePanel";

	private DivisionEntitlementTreePanelBehavior myBehavior;

	private Composite pnlRoot = null;
	private Composite pnlProfileInfo;
	private Composite compositeMiscPnl;
	private Button btnUpdate;
	private Button btnCancel;

	Tree tree=null;
	TreeItem localiItem=null;
	ArrayList<String> customerarray = new ArrayList<String>();

	public DivisionEntitlementTreePanel(Composite parent, int style,
			Object inputObject, CustomerProfileMaintenance customerProfileMaintenance) {
		super(parent, style);

		initialize();
		setBindingForComponents();
		myBehavior = new DivisionEntitlementTreePanelBehavior(
				this, inputObject, customerProfileMaintenance);
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
		createInfoComposite(); //--function to show Parent UI
		setSize(new org.eclipse.swt.graphics.Point(1000, 1000));
	}

	private void createTreeStructure(Composite composite) {

		tree = new Tree (composite, SWT.CHECK|SWT.NONE);
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Expand listener to add functionality on expanding the tree like Calling API & Setting data to Tree Items
		tree.addListener(SWT.Expand, new Listener()
		{
			public void handleEvent(Event event)
			{
				localiItem = (TreeItem) event.item;
				Element eleCust = (Element) localiItem.getData("data");
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

									if ("XPXGetCustomerList".equals(apiname)) {
										Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
										if(null != outXml) {
											NodeList customerNodeList = outXml.getElementsByTagName("Customer");
											ArrayList<Element> arrlst=new ArrayList<Element>();

											for (int j = 0; j <  customerNodeList.getLength(); j++) {
												Element customerNode = (Element) customerNodeList.item(j);
												arrlst.add(customerNode);
											}
											((DivisionEntitlementTreePanel)myBehavior.getOwnerForm()).setTreeValues(localiItem, arrlst);
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
					ctx.setApiName("XPXGetCustomerList");
					ctx.setInputXml(YRCXmlUtils.createFromString(
							"<Customer ParentCustomerKey='" + YRCXmlUtils.getAttribute(eleCust, "CustomerKey") + "' />"));
					ctx.setFormId(getFormId());
					ctx.setShowError(false);
					ctx.setUserData("isRefreshReqd", String.valueOf(false));
					YRCPlatformUI.callApi(ctx, handler);
				}
			}
		}
		);

		//---Selection Listener used to Check & uncheck the parent  & Vice versa basde on condition
		tree.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	            if (event.detail == SWT.CHECK) {
	                TreeItem item = (TreeItem) event.item;
	                boolean checked = item.getChecked();

	                if(checked){
	                	customerarray.add(item.getText());
	                }

	                checkItems(item, checked);     //--function used to check child nodes if parent is checked
	                checkPath( item.getParentItem(), checked, false); //--function used to check parent if all its child are checked
	            }
	        }
	    });

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
		createTreeStructure(pnlProfileInfo); //-function to create tree structure
		createCompositeMiscPnl(); //--function to create lower panel containing Buttons
	}

	private void createCompositeMiscPnl() {

		GridData gridData10 = new GridData();
		gridData10.heightHint = 25;
		gridData10.widthHint = 80;
		GridData gridData5 = new GridData();
		gridData5.heightHint = 25;
		gridData5.widthHint = 120;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = 3;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = SWT.BEGINNING;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;

		compositeMiscPnl = new Composite(pnlProfileInfo, 0);
		compositeMiscPnl.setLayoutData(gridData2);
		compositeMiscPnl.setLayout(gridLayout2);
		compositeMiscPnl.setData("yrc:customType", "TaskComposite");
		compositeMiscPnl.setData("name", "compositeMiscPnl");

		btnUpdate = new Button(compositeMiscPnl, 0);
		btnUpdate.setText("Update_Customer_Assignment");
		btnUpdate.setLayoutData(gridData5);
		btnUpdate.setData("name", "btnUpdate");
		setTheme(btnUpdate, "Button");
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				myBehavior.updateAction();
			}
		});

		// Cancel button
		btnCancel = new Button(compositeMiscPnl, 0);
		btnCancel.setText("Customer_Assignment_Close");
		btnCancel.setLayoutData(gridData10);
		btnCancel.setData("name", "btnCancel");
		setTheme(btnCancel, "Button");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor((YRCEditorPart)YRCDesktopUI.getCurrentPart(), true);
			}
		});
	}

	private void setTheme(Control control, String theme){
		control.setBackground(YRCPlatformUI.getBackGroundColor(theme));
		control.setForeground(YRCPlatformUI.getForeGroundColor(theme));
		control.setFont(YRCPlatformUI.getFont(theme));
	}
	private void setBindingForComponents() {
	}

	public void getTargetModelForUpdateAssignments() {
		TreeItem[] item = tree.getItems();
		this.iterateThroughChilds(item,false);
	}

	// function to iterate through tree & find the function based on their qualification for Addition or deletion of node
	private void iterateThroughChilds(TreeItem[] item, boolean isParentChecked) {
		ArrayList<String> deleteCustIdList= new ArrayList<String>();
		ArrayList<String> addCustIdList= new ArrayList<String>();

		for (TreeItem treeItem : item) {
			Element eleCust = (Element)treeItem.getData("data");
			String strCustID = eleCust.getAttribute("CustomerID");
			TreeItem iiparent=null;

			iiparent=treeItem.getParentItem();
			if(iiparent!=null){
				if(iiparent.getChecked()){
					isParentChecked=true;
				}
				else
					isParentChecked=false;
			}

			if(isParentChecked){
				if(treeItem.getData("OldValue").equals("true")){
					deleteCustIdList.add(strCustID);
				}
			}
			else {
				if(treeItem.getData("OldValue").equals("true") && !treeItem.getChecked() ){
					deleteCustIdList.add(strCustID);

				} else if(!treeItem.getData("OldValue").equals("true") && treeItem.getChecked()){
					addCustIdList.add(strCustID);
				}
			}

			TreeItem[] childItem = treeItem.getItems();
			if(childItem.length==1){
				if(YRCPlatformUI.isVoid(childItem[0].getText())){
					continue;
				}
			}

			this.iterateThroughChilds(childItem, isParentChecked);
		}

		if(deleteCustIdList!= null && deleteCustIdList.size()>0){
			myBehavior.createManageAssignmentInput(deleteCustIdList, false); //---used to delete an entry from DB.
		}
		if(addCustIdList!=null && addCustIdList.size()>0){
			myBehavior.createManageAssignmentInput(addCustIdList,true);   //---used to create an entry in  DB.
		}
	}

	// function to update child values after update action
	public void resetTreeAssignedValues(List assignedList) {
		TreeItem[] item = tree.getItems();
		resetselectedValue(item,assignedList);
	}

	private void resetselectedValue(TreeItem[] item, List assignedList) {

		for (TreeItem treeItem : item) {

			Element eleCust = (Element)treeItem.getData("data");
			String strCustID = eleCust.getAttribute("CustomerID");

			if(assignedList.contains(strCustID)){
				treeItem.setData("OldValue", "true");
			}
			else{
				treeItem.setData("OldValue", "false");
			}

			TreeItem[] childItem = treeItem.getItems();
			if(childItem.length==1){
				if(YRCPlatformUI.isVoid(childItem[0].getText())){
					continue;
				}
			}
			this.resetselectedValue(childItem, assignedList);
		}
	}

	public String getFormId() {
		return FORM_ID;
	}

	public Composite getRootPanel() {
		return pnlRoot;
	}

	public void showRootPanel(boolean show) {
		pnlRoot.setVisible(show);
	}

	public IYRCPanelHolder getPanelHolder() {
		// TODO Complete getPanelHolder
		return null;
	}

	public String getHelpId() {
		// TODO Complete getHelpId
		return null;
	}

	public DivisionEntitlementTreePanelBehavior getBehavior() {
		return myBehavior;
	}

	/** function to set the values of child nodes in Tree structure **/
	public void setTreeValues(Object parent, List<Element> custId) {

		if(null == parent){
			TreeItem iItem = new TreeItem (tree, SWT.NULL);
			Element eleCust=custId.get(0);
			String CustomerID="";
			String orgName=YRCXmlUtils.getAttributeValue(eleCust, "Customer/BuyerOrganization/@OrganizationName");
			String orgId  =YRCXmlUtils.getAttributeValue(eleCust, "Customer/BuyerOrganization/@OrganizationCode");
			CustomerID=orgId;

			iItem.setText(orgName+" ("+CustomerID+")");
			iItem.setData("data", eleCust);
			if(myBehavior.isThisEntitled(eleCust)){ //TODO base on children? (but haven't expanded/loaded yet?)
				iItem.setChecked(true);
				iItem.setData("OldValue", "true");
			}
			else{
				iItem.setData("OldValue", "false");
			}
			iItem.setItemCount(1);
		}

		if (parent instanceof TreeItem) {
			TreeItem localiItem2 = (TreeItem) parent;

			// remove dummy item.
			TreeItem[] childItems = localiItem2.getItems();
			if(childItems.length==1 && YRCPlatformUI.isVoid(childItems[0].getText())) {
				childItems[0].dispose();
			}

			boolean hascheckedChild = false;
			boolean hasUncheckedChild = false;
			for (int i=0; i<custId.size(); i++)
			{
				Element eleCust = custId.get(i);
				StringBuffer address= new StringBuffer();
				TreeItem iiItem = new TreeItem (localiItem2, 1);

				String orgName=YRCXmlUtils.getAttributeValue(eleCust, "Customer/BuyerOrganization/@OrganizationName");
				String orgId=YRCXmlUtils.getAttributeValue(eleCust, "Customer/BuyerOrganization/@OrganizationCode");

				String customerType=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnSuffixType");

				String status = YRCXmlUtils.getAttributeValue(eleCust, "Customer/@Status");

				String CustomerID=orgId; //TODO need for display?
//				if("MC".equalsIgnoreCase(customerType)){
//					CustomerID=orgId;
//				}
//				else if("C".equalsIgnoreCase(customerType)){
//					 CustomerID=orgId;
//				}
//				else if("B".equalsIgnoreCase(customerType)){
//					CustomerID=shipFromBranch+"-"+legacyNo+"-"+billTosuffix;
//				}
//				else if("S".equalsIgnoreCase(customerType)){
//						CustomerID=shipFromBranch+"-"+legacyNo+"-"+shipToSuffix;
//				}

				String displayString = orgName+" ("+CustomerID+")"+address.toString();
				//TODO want to indicated suspended like on Assignment page?
				if (("B".equalsIgnoreCase(customerType)|| ("S".equalsIgnoreCase(customerType)))
						&& "30".equals(status)){ // 30=Suspended (this should be a constant somewhere)
					displayString = "(Suspended)" + displayString;
					iiItem.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
					iiItem.setFont(JFaceResources.getFontRegistry().getItalic(""));
				}
				iiItem.setText(displayString);

				iiItem.setData("data",eleCust);
				if(myBehavior.isThisEntitled(eleCust)){
					iiItem.setChecked(true);
					iiItem.setData("OldValue", "true");
					hascheckedChild = true;
				}
				else{
					iiItem.setData("OldValue", "false");
					hasUncheckedChild = true;
				}
				iiItem.setItemCount(1);
			}

			if (custId.size()>0) {
				boolean parentShouldBeChecked = hascheckedChild && !hasUncheckedChild;
				localiItem2.setChecked(parentShouldBeChecked);
			}

			/* new child will be marked checked automatically, if the parent is already checked.*/
			if (custId.size()>0 && localiItem2.getChecked()) {
                checkItems(localiItem2, true);
                checkPath( localiItem2.getParentItem(), true, false);
            }
		}
	}
}

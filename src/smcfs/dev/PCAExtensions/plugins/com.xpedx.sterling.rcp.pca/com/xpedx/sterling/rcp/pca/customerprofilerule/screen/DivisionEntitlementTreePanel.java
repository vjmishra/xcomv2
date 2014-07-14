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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.yfc.rcp.IYRCApiCallbackhandler;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCApiContext;
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

	Tree tree;
	TreeItem localiItem;
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

		Label lblTreeTitle = new Label(composite, SWT.CENTER);
		GridData lblGridData = new GridData();
		lblGridData.verticalAlignment = 16777216;
		lblGridData.grabExcessHorizontalSpace = true;
		lblTreeTitle.setLayoutData(lblGridData);
		lblTreeTitle.setText("Expand tree to show whether Division Entitlement field applies for each Ship-To of this customer.");

		tree = new Tree (composite, SWT.CHECK|SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		tree.setLayoutData(gridData);

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

		// Selection Listener to check & uncheck the parent & vice versa based on condition
		tree.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	            if (event.detail == SWT.CHECK) {
	                TreeItem item = (TreeItem) event.item;
	                boolean checked = item.getChecked();

	                if(checked){
	                	customerarray.add(item.getText()); //TODO how/is this referenced?
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
		btnUpdate.setText("Update_Profile");
		btnUpdate.setLayoutData(gridData10);
		btnUpdate.setData("name", "btnUpdate");
		setTheme(btnUpdate, "Button");
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				myBehavior.updateAction();
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
		TreeItem[] items = tree.getItems();
		iterateThroughChildren(items);
	}

	public void allowEdit(boolean allowed) {
		if (!allowed) {
			YRCPlatformUI.trace("Entitlements: User not authorized to update Apply Division Entitlement fields");

			btnUpdate.setEnabled(false);
			//tree.setBackground(getDisplay().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
		}
	}

	// function to iterate through tree & create updates for Entitlements on ShipTos
	private void iterateThroughChildren(TreeItem[] item) {

		YRCPlatformUI.trace("DivisionEntitlementTreePanel.iterateThroughChildren - size of tree:" +item.length);
		for (TreeItem treeItem : item) {

			// only want to update entitlement on ShipTo's and only for those that have changed
			boolean changed = treeItem.getData("OldValue").equals("true") != treeItem.getChecked();
			if (treeItem.getData("isShipTo") != null && (Boolean)treeItem.getData("isShipTo"))

			if (changed && (treeItem.getData("isShipTo") != null) && (Boolean)treeItem.getData("isShipTo") ){
				Element eleCust = (Element)treeItem.getData("data");
				String strCustKey = eleCust.getAttribute("CustomerKey");

				boolean entitlementEnabled = treeItem.getChecked();

				myBehavior.createManageCustomerInput(strCustKey, entitlementEnabled);

				// For now, assuming update will be successful and changing OldValue here
				//  Change this to wait for response and only change if update was successful?
				//  - this would require the new set-based API to return updated cust records
				String newVal = entitlementEnabled ? "true" : "false";
				treeItem.setData("OldValue", newVal);
			}

			// Recurse on the children
			TreeItem[] childItem = treeItem.getItems();
			if(childItem.length==1){
				if(YRCPlatformUI.isVoid(childItem[0].getText())){
					continue;
				}
			}
			this.iterateThroughChildren(childItem);
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
		// Complete getPanelHolder ?
		return null;
	}

	public String getHelpId() {
		// Complete getHelpId ?
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
			if(myBehavior.isThisEntitled(eleCust)){
				// Ideally, for master/customer/bill-to would be based on children,
				//   but haven't expanded/loaded yet! Auto-expand?? (big performance hit)
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
				String customerID="";
				StringBuffer address= new StringBuffer();
				TreeItem iiItem = new TreeItem (localiItem2, 1);

				String orgName=YRCXmlUtils.getAttributeValue(eleCust, "Customer/BuyerOrganization/@OrganizationName");
				String orgId=YRCXmlUtils.getAttributeValue(eleCust, "Customer/BuyerOrganization/@OrganizationCode");

				String add1 = YRCXmlUtils.getAttributeValue(eleCust, "Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@AddressLine1");
				String add2 = YRCXmlUtils.getAttributeValue(eleCust, "Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@AddressLine2");
				String city = YRCXmlUtils.getAttributeValue(eleCust, "Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@City");
				String country = YRCXmlUtils.getAttributeValue(eleCust, "Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@Country");
				String state = YRCXmlUtils.getAttributeValue(eleCust, "Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@State");
				String zip = YRCXmlUtils.getAttributeValue(eleCust, "Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo/@ZipCode");

				String shipFromBranch=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnShipFromBranch");
//				String custDivision=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnCustomerDivision");
				String legacyNo=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnLegacyCustNumber");
				String billTosuffix=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnBillToSuffix");
				String shipToSuffix=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnShipToSuffix");
				String customerType=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnSuffixType");

				String status = YRCXmlUtils.getAttributeValue(eleCust, "Customer/@Status");

				// *** NOTE: this code was copied from CustomerAssignmentPanel for consistent display.
				// Display changes made here should probably be made there and vice-versa.
				// (but decided that the orgId is likely preferable to ShipFromBranch, e.g. 60 vs. 68

				if("MC".equalsIgnoreCase(customerType)){
					customerID=orgId;
				}
				else if("C".equalsIgnoreCase(customerType)){
					customerID=orgId;
				}
				else if("B".equalsIgnoreCase(customerType)){
					customerID=shipFromBranch+"-"+legacyNo+"-"+billTosuffix;
				}
				else if("S".equalsIgnoreCase(customerType)){
					customerID=shipFromBranch+"-"+legacyNo+"-"+shipToSuffix;
				}

				if(add1 !=null && add1.trim().length()>0) {
					address.append(" "+add1);
				}
				if(add2 !=null && add2.trim().length()>0) {
					address.append(", "+add2);
				}
				if(city !=null && city.trim().length()>0) {
					address.append(", "+city);
				}
				if(state !=null && state.trim().length()>0) {
					address.append(", "+state);
				}
				if(zip !=null && zip.trim().length()>0) {
					address.append(" "+zip);
				}
				if(country !=null && country.trim().length()>0) {
					address.append(" "+country);
				}

				if (("B".equalsIgnoreCase(customerType)|| ("S".equalsIgnoreCase(customerType)))){
					iiItem.setText(customerID +", " +orgName+ ", " +address.toString());
					if ("S".equalsIgnoreCase(customerType)) {
						iiItem.setFont(JFaceResources.getFontRegistry().getBold(""));
						iiItem.setData("isShipTo", Boolean.TRUE);
					}
				}else{
					iiItem.setText(orgName+" ("+customerID+")"+address.toString());
				}

				if (("B".equalsIgnoreCase(customerType)|| ("S".equalsIgnoreCase(customerType))) && status!=null && status.equals("30")){
					iiItem.setText("[Suspended] do not use " + customerID +", " +orgName+ address.toString());
					iiItem.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
					iiItem.setFont(JFaceResources.getFontRegistry().getItalic(""));
				}

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

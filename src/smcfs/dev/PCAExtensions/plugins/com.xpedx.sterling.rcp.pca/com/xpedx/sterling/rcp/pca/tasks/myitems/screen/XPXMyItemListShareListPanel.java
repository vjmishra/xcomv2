package com.xpedx.sterling.rcp.pca.tasks.myitems.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.yfc.rcp.IYRCApiCallbackhandler;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXMyItemListShareListPanel extends Composite implements
		IYRCComposite {

	private XPXMyItemListShareListPanelBehavior myBehavior;
	private Object inputObject;
	private Composite pnlRoot;
	private Composite pnlProfileInfo;
	Tree tree;
	TreeItem localiItem=null;
	private Composite compositeMiscPnl;
	private Button btnUpdate;
	private Button btnCancel;
	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.tasks.myitems.screen.XPXMyItemListShareListPanel";
	
	public XPXMyItemListShareListPanel(Composite parent, int style,
			Object inputObject) {
		super(parent, style);
		this.inputObject = inputObject;
		Element eleMILDtls = (Element)this.inputObject;
		initialize();
		myBehavior = new XPXMyItemListShareListPanelBehavior(this, eleMILDtls);
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
	
	private void createTreeStructure(Composite composite) {
		
		tree = new Tree (composite, SWT.CHECK|SWT.NONE);
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//--Expand listener used to add functionality on expanding the tree like Calling API & Setting data to Tree Items
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
											((XPXMyItemListShareListPanel)myBehavior.getOwnerForm()).setTreeValues(localiItem, arrlst);
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
			public void widgetSelected(SelectionEvent e) {
				myBehavior.updateAction();
			}
		});
		
//		Cancel button
		btnCancel = new Button(compositeMiscPnl, 0);
		btnCancel.setText("Customer_Assignment_Close");
		btnCancel.setLayoutData(gridData10);
		btnCancel.setData("name", "btnCancel");
		setTheme(btnCancel, "Button");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getParent().getShell().close();
			}
		});

	}
	
	private void setTheme(Control control, String theme){
		control.setBackground(YRCPlatformUI.getBackGroundColor(theme));
		control.setForeground(YRCPlatformUI.getForeGroundColor(theme));
		control.setFont(YRCPlatformUI.getFont(theme));	
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
			if(myBehavior.isThisCustomerAssigned(eleCust)){
				iItem.setChecked(true);
				iItem.setData("MILShareElement", myBehavior.getAssignedMap().get(orgId));
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
				if(myBehavior.isThisCustomerAssigned(eleCust)){
					iiItem.setChecked(true);
					iiItem.setData("MILShareElement", myBehavior.getAssignedMap().get(orgId));
					iiItem.setData("OldValue", "true");
				}
				else{
					iiItem.setData("OldValue", "false");
				}
				iiItem.setItemCount(1);
				
			}
			
			/* new child will be marked checked automatically, if the parent is already checked.*/
			if (custId.size()>0 && localiItem2.getChecked()) {
                checkItems(localiItem2, true);
                checkPath( localiItem2.getParentItem(), true, false);
            }
		}
						
		
	}
	
	public void showRootPanel(boolean show) {
		pnlRoot.setVisible(show);
	}
	
	@Override
	public String getFormId() {
		return FORM_ID;
	}

	@Override
	public String getHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IYRCPanelHolder getPanelHolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Composite getRootPanel() {
		return pnlRoot;
	}
	
	
	/** Refresh - used to update child values after update action */
	public void resetTreeAssignedValues(Map assignedMap) {
		TreeItem[] item = tree.getItems();
		resetselectedValue(item,assignedMap);
		
	}
	
	private void resetselectedValue(TreeItem[] item, Map assignedMap) {
			
		for (TreeItem treeItem : item) {
			
			Element eleCust = (Element)treeItem.getData("data");
			String strCustID = eleCust.getAttribute("CustomerID");
			if(assignedMap.containsKey(strCustID)){
				treeItem.setData("MILShareElement", assignedMap.get(strCustID));
				treeItem.setData("OldValue", "true");
			}
			else{
				if(null != treeItem.getData("MILShareElement")){
					treeItem.setData("MILShareElement",null);
				}
				treeItem.setData("OldValue", "false");
			}
			TreeItem[] childItem = treeItem.getItems();
			if(childItem.length==1){
				if(YRCPlatformUI.isVoid(childItem[0].getText())){
					continue;
				}
			}
			this.resetselectedValue(childItem, assignedMap);
		}
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
			
			if(isParentChecked){
				if(treeItem.getData("OldValue").equals("true")){
					strMILShareKey = this.getMILShareKey(treeItem);
					strDivisionID = this.getDivisionID(treeItem);
					strCustPath = this.getCustomerPath(treeItem);
					myBehavior.createManageAssignmentInput(strMILShareKey, strCustID, strCustPath, strDivisionID, false);
				}
			}
			else {
				if(treeItem.getData("OldValue").equals("true") && !treeItem.getChecked() ){
					strMILShareKey = this.getMILShareKey(treeItem);
					strDivisionID = this.getDivisionID(treeItem);
					strCustPath = this.getCustomerPath(treeItem);
					myBehavior.createManageAssignmentInput(strMILShareKey, strCustID, strCustPath, strDivisionID, false);   //---used to delete an entry from DB.
				} else if(!treeItem.getData("OldValue").equals("true") && treeItem.getChecked()){
					//MIL Share Key will not be available as this is being newly added. 
					strMILShareKey = this.getMILShareKey(treeItem);
					strDivisionID = this.getDivisionID(treeItem);
					strCustPath = this.getCustomerPath(treeItem);
					myBehavior.createManageAssignmentInput(strMILShareKey, strCustID, strCustPath, strDivisionID, true);   //---used to create an entry in  DB.
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
	}

	/** Get Method - MIL Share Key */
	private String getMILShareKey(TreeItem treeItem) {
		String strMILShareKey = null;
		Element eleMILShare = (Element) treeItem.getData("MILShareElement");
		if(null != eleMILShare){
			strMILShareKey = eleMILShare.getAttribute("MyItemsListShareKey");
		}
		return strMILShareKey;
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
	
}

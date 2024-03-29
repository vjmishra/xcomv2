package com.xpedx.sterling.rcp.pca.userprofile.screen;

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

public class CustomerAssignmentPanel extends Composite implements IYRCComposite {
	private Composite pnlRoot = null;

	private CustomerAssignmentPanelBehavior myBehavior;

//	private YRCWizardBehavior wizBehavior;

	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.userprofile.screen.CustomerAssignmentPanel"; // @jve:decl-index=0:

	private Composite pnlProfileInfo;
	private Composite compositeMiscPnl;

	public Object inputObject;

	private Button btnUpdate;
	private Button btnAuthLocations;
	private Button btnExportAuthLocations;
	
	private Button btnCancel;
	Tree tree=null;
	TreeItem localiItem=null;
	ArrayList customerarray = new ArrayList();

	public CustomerAssignmentPanel(Composite parent, int style,
			Object inputObject, Element customerContactEle) {
		super(parent, style);
		this.inputObject = inputObject;

		initialize();
		setBindingForComponents();
		myBehavior = new CustomerAssignmentPanelBehavior(this, inputObject,
				customerContactEle);
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
		
		//--Expand listner used to add functionality on expanding the tree like Calling API & Setting data to Tree Items
		tree.addListener(SWT.Expand, new Listener()
		{
			public void handleEvent(Event event) 
			{
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
											((CustomerAssignmentPanel)myBehavior.getOwnerForm()).setTreeValues(localiItem, arrlst);
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
	            if (event.detail == SWT.CHECK) {
	                TreeItem item = (TreeItem) event.item;
	                boolean checked = item.getChecked();
	                item.getText();
	                if(checked==true){
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
	        item.setChecked(true);
	        item.setGrayed(true);

	    } else {

	        int index = 0;
	        TreeItem[] items = item.getItems();
	        int count= item.getItemCount();
	        int i=0;
	        while (index < items.length) {
	        	
	            TreeItem child = items[index];
	            if(!child.getGrayed()){
	            	item.setGrayed(child.getGrayed());
	            }
	            if(count ==1 && child.getChecked()){
	            	i++;
	            }else{
	            if(child.getChecked()){
	            	i++;
	            }
	            }
	            index++;

	        }
	      
	        if(i==0){
	        	if(item.getText().contains("-M")){
	        		 if(item.getGrayed()== true){
				         checked=true;
			       	  	 item.setChecked(grayed);
			           	 item.setGrayed(grayed);
			           	 grayed=false;
				        	}
	        		 else{
		            	 checked=false;            	
		            	 item.setChecked(checked);
		            	 item.setGrayed(true);
		            	 grayed=false;
		            }	 
	        	}
	        	else if(item.getText().contains("-S")){
	        		 if(item.getGrayed()== true){
				         checked=true;
			       	  	 item.setChecked(checked);
			           	 item.setGrayed(false);
			           	 grayed=false;
				        	}
	        		 else{
		            	 checked=false;            	
		            	 item.setChecked(checked);
		            	 item.setGrayed(false);
		            	 grayed=false;
		            }	 
	        	}
	        	 else if(count==i){
	            	  	checked=true;
	            	  	 item.setChecked(checked);
	                	 item.setGrayed(false);
	                	 grayed=false;
	            }
	            else{
	            	 checked=false;            	
	            	 item.setChecked(false);
	            	 item.setGrayed(false);
	            	 grayed=false;
	            }
	        }
	        
	        else if(count==i){
            	  	checked=true;
            	  	 item.setChecked(checked);
                	 item.setGrayed(false);
                	 grayed=false;
            }
            else{
            	 checked=false;            	
            	 item.setChecked(checked);
            	 item.setGrayed(false);
            	 grayed=false;
            }
	    }
    //item.setChecked(checked);
	    
    
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
		gridLayout2.numColumns = 4;
		
		compositeMiscPnl = new Composite(pnlProfileInfo, 0);
		compositeMiscPnl.setLayoutData(gridData2);
		compositeMiscPnl.setLayout(gridLayout2);
		compositeMiscPnl.setData("yrc:customType", "TaskComposite");
		compositeMiscPnl.setData("name", "compositeMiscPnl");
		
		GridData gridData6 = new GridData();
		gridData6.heightHint = 25;
		gridData6.widthHint = 145;

		btnAuthLocations = new Button(compositeMiscPnl, 0);
		btnAuthLocations.setText("Authorized Locations");
		btnAuthLocations.setLayoutData(gridData6);
		btnAuthLocations.setData("name", "btnAuthLocations");
		setTheme(btnAuthLocations, "Button");
		btnAuthLocations.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.showAuthLocations();
			}
		});
		btnExportAuthLocations = new Button(compositeMiscPnl, 0);
		btnExportAuthLocations.setText("Export Authorized Locations");
		btnExportAuthLocations.setLayoutData(gridData6);
		btnExportAuthLocations.setData("name", "btnExportAuthLocations");
		setTheme(btnExportAuthLocations, "Button");
		btnExportAuthLocations.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e)  {
				myBehavior.exportAuthLocations();
			}
		});
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
	//---function used to iterate through tree & find the function based on their qualification for Addition or deletion of node
	private void iterateThroughChilds(TreeItem[] item, boolean isParentChecked) {
		// Added For XB-638
		ArrayList<String> deleteCustIdList= new ArrayList<String>();
		ArrayList<String> addCustIdList= new ArrayList<String>();
		for (TreeItem treeItem : item) {
			
			Element eleCust = (Element)treeItem.getData("data");
			String strCustID = eleCust.getAttribute("CustomerID");
			TreeItem iiparent=null;
			
			iiparent=treeItem.getParentItem();
			if(iiparent!=null){
				if((iiparent.getChecked() && iiparent.getGrayed()==false) && treeItem.getGrayed()==false){
					isParentChecked=true;
				}
				else
					isParentChecked=false;
			}
				
			if(isParentChecked){
				//if(treeItem.getData("OldValue").equals("true")){
					//myBehavior.createManageAssignmentInput(strCustID, false);
					//XB-638 Changes
					deleteCustIdList.add(strCustID);
				//}
			}
			else {
				if(treeItem.getData("OldValue").equals("true") && !treeItem.getChecked() ){
					//myBehavior.createManageAssignmentInput(strCustID, false); //---used to delete an entry from DB.
					//XB-638 Changes
					deleteCustIdList.add(strCustID);
					
				} else if(!treeItem.getData("OldValue").equals("true") && (treeItem.getChecked() && treeItem.getGrayed()==false)){
					//myBehavior.createManageAssignmentInput(strCustID, true);   //---used to create an entry in  DB.
					//XB-638 Changes
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
		//XB-638 Changes
		if(deleteCustIdList!= null && deleteCustIdList.size()>0){
			myBehavior.createManageAssignmentInput(deleteCustIdList, false); //---used to delete an entry from DB.
		}
		if(addCustIdList!=null && addCustIdList.size()>0){
			myBehavior.createManageAssignmentInput(addCustIdList,true);   //---used to create an entry in  DB.
		}
	}
	//---function used to update child values after update action
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

	public CustomerAssignmentPanelBehavior getBehavior() {
		return myBehavior;
	}
/**--setTreeValues function used to set the values of child nodes in Tree structure- **/
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
				String legacyNo=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnLegacyCustNumber");
				String billTosuffix=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnBillToSuffix");
				String shipToSuffix=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnShipToSuffix");
				String customerType=YRCXmlUtils.getAttributeValue(eleCust, "Customer/Extn/@ExtnSuffixType");

				String status = YRCXmlUtils.getAttributeValue(eleCust, "Customer/@Status");

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
				
				if(add1 !=null && add1.trim().length()>0)
				{
					address.append(" "+add1);
				}
				
				if(add2 !=null && add2.trim().length()>0)
				{
					address.append(", "+add2);
				}
				if(city !=null && city.trim().length()>0)
				{
					address.append(", "+city);
				}
				if(state !=null && state.trim().length()>0)
				{
					address.append(", "+state);
				}
				if(zip !=null && zip.trim().length()>0)
				{
					address.append(" "+zip);
				}
				if(country !=null && country.trim().length()>0)
				{
					address.append(" "+country);
				}
				
				if (("B".equalsIgnoreCase(customerType)|| ("S".equalsIgnoreCase(customerType)))){
					iiItem.setText(CustomerID +", " +orgName+ ", " +address.toString());					
					iiItem.setFont(JFaceResources.getFontRegistry().getBold(""));
				}else{
					iiItem.setText(orgName+" ("+CustomerID+")"+address.toString());
				}
				
				if (("B".equalsIgnoreCase(customerType)|| ("S".equalsIgnoreCase(customerType))) && status!=null && status.equals("30")){
					iiItem.setText("[Suspended] do not use " + CustomerID +", " +orgName+ address.toString());
					iiItem.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
					iiItem.setFont(JFaceResources.getFontRegistry().getItalic(""));
				}
				
				iiItem.setData("data",eleCust);
				if(myBehavior.isThisCustomerAssigned(eleCust)){
					iiItem.setChecked(true);
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
}

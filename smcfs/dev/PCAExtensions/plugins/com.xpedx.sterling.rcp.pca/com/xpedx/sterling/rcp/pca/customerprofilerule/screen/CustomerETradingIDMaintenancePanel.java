package com.xpedx.sterling.rcp.pca.customerprofilerule.screen;

import java.util.ArrayList;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCCellModifier;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.IYRCTableColumnTextProvider;
import com.yantra.yfc.rcp.IYRCTableLinkProvider;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCScrolledCompositeListener;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;
/**
 * @author VJ Mishra
 * 
 * 
 */
public class CustomerETradingIDMaintenancePanel extends Composite implements IYRCComposite {

	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.customerprofilerule.screen.CustomerETradingIDMaintenancePanel";
	public static final String EXTN_ETRADING_ID_OLD_VALUE = "ExtnETradingIDOldValue";
	public static final String EXTN_ETRADING_ID = "ExtnETradingID";
	public static final String EXTN_CUST_EMAIL_ADDRESS_OLD_VALUE = "ExtnEDIEmailAddressOldValue";
	public static final String EXTN_CUST_EMAIL_ADDRESS = "ExtnEDIEmailAddress";
	public static final String IS_EXTN_ETRADING_ID_MODIFIED = "IsExtnETradingIDModified";
	public static final String IS_EXTN_CUST_EMAIL_ADDRESS_MODIFIED = "IsExtnEDIEmailAddressModified";
	private CustomerETradingIDMaintenancePanelBehaviour myBehavior;
	private CustomerProfileMaintenance parentObj;
	private Composite pnlRoot;
	private ScrolledComposite bodyScrolledPanel;
	private Composite pnlBody;
	private Button btnUpdate;
	private Table tblShipCustomers;
	private Composite pnlHeader;
	private Label lblFiller;
	private Link linkMasterCustomer;
	private Link linkCustomer;
	private Link linkBillTo;
	
	public CustomerETradingIDMaintenancePanel(Composite parent, int style, 
			Object inputObject, CustomerProfileMaintenance parentObj) {
		super(parent, style);
		this.parentObj = parentObj;
		initialize();
		setBindingForComponents();
		myBehavior = new CustomerETradingIDMaintenancePanelBehaviour(this, inputObject, FORM_ID, parentObj);
	}

	private void setBindingForComponents() {
		YRCTableBindingData tblShipCustsBindingData = new YRCTableBindingData();
		YRCTblClmBindingData colBindings[] = new YRCTblClmBindingData[tblShipCustomers.getColumnCount()];

		colBindings[0] = new YRCTblClmBindingData();
		colBindings[0].setName("clmShipAddress");
		colBindings[0].setColumnBinding("Ship To Address");
        colBindings[0].setSortReqd(true);
        colBindings[0].setLabelProvider(new IYRCTableColumnTextProvider(){

			
			public String getColumnText(Element eleData) {
				Element elePersonInfo = (Element) YRCXPathUtils.evaluate(eleData, "CustomerAdditionalAddressList/CustomerAdditionalAddress[@IsDefaultShipTo='Y']/PersonInfo", XPathConstants.NODE);
				if(!YRCPlatformUI.isVoid(elePersonInfo)){
					Object[] data = new Object[7];
					data[0] = elePersonInfo.getAttribute("AddressLine1");
					data[1] = elePersonInfo.getAttribute("AddressLine2");
					data[2] = elePersonInfo.getAttribute("AddressLine3");
					data[3] = elePersonInfo.getAttribute("City");
					data[4] = elePersonInfo.getAttribute("State");
					data[5] = elePersonInfo.getAttribute("Country");
					data[6] = elePersonInfo.getAttribute("ZipCode");
					return YRCPlatformUI.getFormattedString("xpedx_address_key", data);
					
				} else {
					return eleData.getAttribute("CustomerID");
				}
			}
        	
        });

		colBindings[1] = new YRCTblClmBindingData();
		colBindings[1].setName("clmCustAddress");
		colBindings[1].setSortReqd(true);
        colBindings[1].setAttributeBinding("CustomerID");
        colBindings[1].setColumnBinding("Ship To");
        colBindings[1].setTargetAttributeBinding("Customer/@CustomerID");
       
        
		colBindings[2] = new YRCTblClmBindingData();
		colBindings[2].setName("clmETradingID");
        colBindings[2].setAttributeBinding("Extn/@ExtnETradingID");
        colBindings[2].setColumnBinding("ETradingID");
        colBindings[2].setTargetAttributeBinding("Customer/Extn/@ExtnETradingID");
        
		colBindings[3] = new YRCTblClmBindingData();
		colBindings[3].setName("clmCustEmailAddress");
        colBindings[3].setAttributeBinding("Extn/@ExtnEDIEmailAddress");
        colBindings[3].setColumnBinding("Customer Email Addresss");
        colBindings[3].setTargetAttributeBinding("Customer/Extn/@ExtnEDIEmailAddress");
        

		
		colBindings[4] = new YRCTblClmBindingData();
		colBindings[4].setName("clmUpdateLnk");
		colBindings[4].setLinkReqd(true);
		colBindings[4].setColumnBinding("update");
		colBindings[4].setLabelProvider(new IYRCTableColumnTextProvider(){
	
			public String getColumnText(Element eleData) {
				return "Update";
			}
        	
        });

		      
        IYRCCellModifier cellModifier = new IYRCCellModifier() {
			protected boolean allowModify(String property, String value, Element element) {
				return true;
			}

			protected int allowModifiedValue(String property, String value, Element element) {
				return IYRCCellModifier.MODIFY_OK;
			}

			protected String getModifiedValue(String property, String value, Element element) {
				
				Element eleExtn = YRCXmlUtils.getChildElement(element, "Extn");
				if("Extn/@ExtnETradingID".equals(property)){
					String oldValue = "";
					if(eleExtn.hasAttribute(EXTN_ETRADING_ID_OLD_VALUE)){
						oldValue = eleExtn.getAttribute(EXTN_ETRADING_ID_OLD_VALUE);
					} else {
						oldValue = eleExtn.getAttribute(EXTN_ETRADING_ID);
						eleExtn.setAttribute(EXTN_ETRADING_ID_OLD_VALUE, oldValue);
					}
					
					if(!YRCPlatformUI.equals(value,oldValue)){
						element.setAttribute(IS_EXTN_ETRADING_ID_MODIFIED, "Y");
					} else {
						if(element.hasAttribute(IS_EXTN_ETRADING_ID_MODIFIED)){
							element.removeAttribute(IS_EXTN_ETRADING_ID_MODIFIED);
						}
					}	
				} else if ("Extn/@ExtnEDIEmailAddress".equals(property)){
					String oldValue = "";
					if(eleExtn.hasAttribute(EXTN_CUST_EMAIL_ADDRESS_OLD_VALUE)){
						oldValue = eleExtn.getAttribute(EXTN_CUST_EMAIL_ADDRESS_OLD_VALUE);
					} else {
						oldValue = eleExtn.getAttribute(EXTN_CUST_EMAIL_ADDRESS);
						eleExtn.setAttribute(EXTN_CUST_EMAIL_ADDRESS_OLD_VALUE, oldValue);
					}
					
					if(!YRCPlatformUI.equals(value,oldValue)){
						element.setAttribute(IS_EXTN_CUST_EMAIL_ADDRESS_MODIFIED, "Y");
					} else {
						if(element.hasAttribute(IS_EXTN_CUST_EMAIL_ADDRESS_MODIFIED)){
							element.removeAttribute(IS_EXTN_CUST_EMAIL_ADDRESS_MODIFIED);
						}
					}
				}

				return value;
			}


		};
		String[] editors = new String[tblShipCustomers.getColumnCount()];
//		editors[1] = YRCConstants.YRC_TEXT_BOX_CELL_EDITOR;
		editors[2] = YRCConstants.YRC_TEXT_BOX_CELL_EDITOR;
		editors[3] = YRCConstants.YRC_TEXT_BOX_CELL_EDITOR;
		tblShipCustsBindingData.setCellTypes(editors);
		tblShipCustsBindingData.setCellModifierRequired(true);
		tblShipCustsBindingData.setCellModifier(cellModifier);
        tblShipCustsBindingData.setSourceBinding("ShipToCustomerList:/CustomerList/Customer");
        tblShipCustsBindingData.setTargetBinding("SaveCustomerList:/CustomerList");
        tblShipCustsBindingData.setName("tblShipCustomers");
        tblShipCustsBindingData.setTblClmBindings(colBindings);
        tblShipCustsBindingData.setKeyNavigationRequired(true);
        tblShipCustsBindingData.setSortRequired(true);
        tblShipCustsBindingData.setLinkProvider( new IYRCTableLinkProvider(){
        	public String getLinkTheme(Object element, int columnIndex) {
        		//Element eleTableItem = (Element)element;
        		if(columnIndex == 4){
        			return "TableLink";
        		}
        		return null;
        	}
        	
        	public void linkSelected(Object element, int columnIndex) {
           		Element eleTableItem = (Element)element;
           		if(columnIndex == 4){
           			myBehavior.update(eleTableItem);
           			return;
           		}
           	}
        });
        tblShipCustomers.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, tblShipCustsBindingData);
        
	}

	private void initialize() {
		createRootPanel();
		this.setLayout(new FillLayout());
		setSize(new org.eclipse.swt.graphics.Point(1000, 200));
		
	}

	private void createRootPanel() {
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.verticalSpacing = 2;
		gridLayout5.marginHeight = 50;
		gridLayout5.marginWidth = 100;
		pnlRoot = new Composite(this, SWT.NONE);
//		pnlRoot.setData("yrc:customType", "TaskComposite");
		pnlRoot.setData("name", "pnlRoot");
		pnlRoot.setLayout(gridLayout5);
		showRootPanel(true);
		
		createScrollCmpstForPnlBody();
		createComposite();
		
		GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.BEGINNING;
        gridData.verticalAlignment = SWT.BEGINNING;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalIndent = 3;
//        gridData.widthHint = 500;
    	btnUpdate = new Button(pnlBody, 0);
		btnUpdate.setText("Update All");
		
		btnUpdate.setLayoutData(gridData);
		btnUpdate.setData("name", "btnUpdate");
		btnUpdate.setAlignment(SWT.RIGHT);
		btnUpdate.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected( org.eclipse.swt.events.SelectionEvent e) {
				 myBehavior.updateAll();
				 
			}
		}
		);
		
	/*	adjustScrollPnl(bodyScrolledPanel, pnlBody,
				getRootPanel(), true, true);*/
		
	}
	
	
	private void createComposite() {

		// Header Panel
		GridLayout gridLayout5 = new GridLayout(3,false);
		gridLayout5.verticalSpacing = 2;
		
		GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.BEGINNING;
        gridData.verticalAlignment = SWT.TOP;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalIndent = 3;
        gridData.widthHint = 800;
        
		pnlHeader = new Composite(pnlBody, SWT.NONE);
//		pnlHeader.setData("yrc:customType", "TaskComposite");
		pnlHeader.setData("name", "pnlHeader");
		pnlHeader.setLayout(gridLayout5);
		pnlHeader.setLayoutData(gridData);
		XPXUtils.paintPanel(pnlHeader);

		// Controls 
		GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = SWT.BEGINNING;
        gridData1.widthHint = 200;
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = SWT.BEGINNING;
        gridData2.widthHint = 200;
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = SWT.BEGINNING;
        gridData3.widthHint = 200;

        this.addParentCustomerLinks();
        
        GridData gridDataTblShipCustomers = new org.eclipse.swt.layout.GridData();
        gridDataTblShipCustomers.horizontalSpan = 3;

		tblShipCustomers = new Table(pnlHeader, SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL 	| SWT.V_SCROLL);
		
		TableColumn clmCustAddress = new TableColumn(tblShipCustomers, SWT.NONE);
		clmCustAddress.setWidth(130);
		clmCustAddress.setText("Column 1");
		
		TableColumn clmETradingID = new TableColumn(tblShipCustomers, SWT.NONE);
		clmETradingID.setWidth(130);
		clmETradingID.setText("Column 2");

		TableColumn clmCustEmailAddr = new TableColumn(tblShipCustomers, SWT.NONE);
		clmCustEmailAddr.setWidth(130);

		TableColumn clmCustShipAddress= new TableColumn(tblShipCustomers, SWT.NONE);
		clmCustShipAddress.setWidth(130);
		clmCustShipAddress.setText("Ship To Address");
		
		TableColumn clmUpdateLnk= new TableColumn(tblShipCustomers, SWT.NONE);
		clmUpdateLnk.setWidth(130);
				
		gridDataTblShipCustomers.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridDataTblShipCustomers.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridDataTblShipCustomers.grabExcessHorizontalSpace = true;
		gridDataTblShipCustomers.heightHint = 300;
		tblShipCustomers.setLayoutData(gridDataTblShipCustomers);
		tblShipCustomers.setHeaderVisible(true);
		tblShipCustomers.setLinesVisible(true);
		
		// HardCoded Values for the height to make up 
		// for the requirement to increase the 
		// column height.
		tblShipCustomers.addListener(SWT.MeasureItem, new Listener() { 
			  public void handleEvent(Event event) { 
			     event.height = 20;
			  }
		});
		
        
	}

	/**
	 * 
	 */
	private void createScrollCmpstForPnlBody() {
		// Scroll Panel Start
		GridData gridDataScroll = new GridData();
		gridDataScroll.horizontalAlignment = SWT.FILL;
		gridDataScroll.grabExcessHorizontalSpace = true;
		gridDataScroll.grabExcessVerticalSpace = true;
		gridDataScroll.verticalAlignment = SWT.FILL;

		bodyScrolledPanel = new ScrolledComposite(getRootPanel(), SWT.V_SCROLL);
		bodyScrolledPanel.setLayoutData(gridDataScroll);
		bodyScrolledPanel.setExpandHorizontal(true);
		bodyScrolledPanel.setExpandVertical(true);
		bodyScrolledPanel.setAlwaysShowScrollBars(true);
		bodyScrolledPanel.addListener(SWT.CURSOR_SIZEALL,
		new YRCScrolledCompositeListener(bodyScrolledPanel));

		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 3;
		gridLayout.verticalSpacing = 3;
		gridLayout.marginWidth = 1;
		gridLayout.marginHeight = 1;
		gridLayout.numColumns = 1;

		GridData gridData0 = new GridData();
		gridData0.horizontalAlignment = SWT.FILL;
		gridData0.grabExcessVerticalSpace = true;
		gridData0.grabExcessHorizontalSpace = true;
		gridData0.verticalAlignment = SWT.FILL;
		pnlBody = new Composite(bodyScrolledPanel, 0);
		pnlBody.setLayoutData(gridData0);
		pnlBody.setLayout(gridLayout);
		pnlBody.setData("name", "pnlBody");

		GridLayout gridLayout1 = new GridLayout();
		bodyScrolledPanel.setLayout(gridLayout1);
		bodyScrolledPanel.setContent(pnlBody);
//		bodyScrolledPanel.setData("yrc:customType", "TaskComposite");
		bodyScrolledPanel.setData("name", "bodyScrolledPanel");
		// XPXUtils.paintPanel(scrolledPnlforInfo);

		// Scroll Panel End
	}

	protected void addParentCustomerLinks() {
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = SWT.END;
		gridData1.verticalAlignment = 2;
		gridData1.widthHint = 30;
		
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = SWT.BEGINNING;
		gridData2.grabExcessHorizontalSpace = false;
		gridData2.verticalAlignment = 2;
		gridData2.widthHint = 150;
		
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = SWT.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = 2;
			
		
		Element generalInfo = parentObj.getBehavior().getLocalModel("XPXCustomerIn");
		ArrayList listCustomers = YRCXmlUtils.getChildren(generalInfo, "Customer");
		if(!YRCPlatformUI.isVoid(listCustomers)&& listCustomers.size()>0)
		{
			Element customerEle = (Element)listCustomers.get(0);
			if(YRCXmlUtils.getChildren(customerEle,"ParentMasterCustomer").size()>0)
			{
				addTab(gridData1,"dummyMasterCustomer");
				Label lblMasterCustomer = new Label(pnlHeader, SWT.NONE);
				lblMasterCustomer.setText("Master Customer:");
				lblMasterCustomer.setLayoutData(gridData2);
				lblMasterCustomer.setData("name", "lblMasterCustomer");
				linkMasterCustomer = new Link(pnlHeader, 0);
				linkMasterCustomer.setText(YRCXmlUtils.getXPathElement(customerEle,"/Customer/ParentMasterCustomer/Customer").getAttribute("CustomerID")+"+"+YRCXmlUtils.getXPathElement(customerEle,"/Customer/ParentMasterCustomer/Customer").getAttribute("OrganizationName"));
				linkMasterCustomer.setData("name", "linkMasterCustomer");
				linkMasterCustomer.setData("yrc:customType", "Link");
				linkMasterCustomer.setEnabled(true);
				linkMasterCustomer.setLayoutData(gridData3);
				linkMasterCustomer.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
						public void widgetSelected( org.eclipse.swt.events.SelectionEvent e) {
							myBehavior.openDetailEditor((String) linkMasterCustomer.getData("name"));
						}
					});				
			}
			if(YRCXmlUtils.getChildren(customerEle,"ParentCustomerCustomer").size()>0)
			{
				addTab(gridData1,"dummyCustomer");
				Label lblCustomer = new Label(pnlHeader, SWT.NONE);
				lblCustomer.setText("Customer:");
				lblCustomer.setLayoutData(gridData2);
				lblCustomer.setData("name", "lblCustomer");
				linkCustomer = new Link(pnlHeader, 0);
				linkCustomer.setText(YRCXmlUtils.getXPathElement(customerEle,"/Customer/ParentCustomerCustomer/Customer").getAttribute("CustomerID")+"+"+YRCXmlUtils.getXPathElement(customerEle,"/Customer/ParentCustomerCustomer/Customer").getAttribute("OrganizationName"));
				linkCustomer.setData("name", "linkCustomer");
				linkCustomer.setLayoutData(gridData3);
				linkCustomer.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
					public void widgetSelected( org.eclipse.swt.events.SelectionEvent e) {
						myBehavior.openDetailEditor((String) linkCustomer.getData("name"));
					}
				});
			}
			if(YRCXmlUtils.getChildren(customerEle,"ParentBillToCustomer").size()>0)
			{
				addTab(gridData1,"dummyBillTo");
				Label lblBillTo = new Label(pnlHeader, SWT.NONE);
				lblBillTo.setText("Bill-To:");
				lblBillTo.setLayoutData(gridData2);
				lblBillTo.setData("name", "lblBillTo");
				linkBillTo = new Link(pnlHeader, 0);
				linkBillTo.setText(YRCXmlUtils.getXPathElement(customerEle,"/Customer/ParentBillToCustomer/Customer").getAttribute("CustomerID")+"+"+YRCXmlUtils.getXPathElement(customerEle,"/Customer/ParentBillToCustomer/Customer").getAttribute("OrganizationName"));
				linkBillTo.setData("name", "linkBillTo");
				linkBillTo.setLayoutData(gridData3);
				linkBillTo.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
					public void widgetSelected( org.eclipse.swt.events.SelectionEvent e) {
						myBehavior.openDetailEditor((String) linkBillTo.getData("name"));
					}
				});
			}
		}
	}
	
	public String getFormId() {
		return FORM_ID;
	}

	public String getHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IYRCPanelHolder getPanelHolder() {
		// TODO Auto-generated method stub
		return null;
	}

	public Composite getRootPanel() {
		return pnlRoot;
	}
	
	public void showRootPanel(boolean show) {
		pnlRoot.setVisible(show);
	}
	
	private void addTab(GridData gridData2 , String name) {
		lblFiller = new Label(pnlHeader, SWT.NONE);
		lblFiller.setText("");
		lblFiller.setLayoutData(gridData2);
		lblFiller.setData("name", name);
	}
}
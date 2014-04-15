package com.xpedx.sterling.rcp.pca.sharedTasks.shiptolookup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.w3c.dom.Element;

import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.IYRCTableColumnTextProvider;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCLabelBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;

public class XPXShowListOfAuthorizedLocationsPanel extends Composite  implements IYRCComposite {

	private Composite pnlRoot = null;
	private Composite pnlTitleHolder = null;
	private Label lblTitleIcon = null;
	private XPXShowListOfAuthorizedLocationsPanelBehaviour myBehavior;
	private Element elePageInput;
	private Group pnlResults;
	Table tblResults;
    public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.sharedTasks.shiptolookup.XPXShowListOfAuthorizedLocationsPanel";    
    
	public XPXShowListOfAuthorizedLocationsPanel(Composite parent, int style, Element inputObject) {
		super(parent, style);
		elePageInput = (Element) inputObject;
		initialize();
		setBindingForComponents();
		myBehavior = new XPXShowListOfAuthorizedLocationsPanelBehaviour(this, FORM_ID, elePageInput);
	}
	
	private void initialize() {
		createRootPanel();		
		this.setLayout(new FillLayout());
		setSize(new org.eclipse.swt.graphics.Point(600,800));
	}
	
	private void createRootPanel() {
		pnlRoot = new Composite(this, SWT.NONE);
        pnlRoot.setLayout(new GridLayout());
        createPnlResults();
        
	}
	private void setBindingForComponents() {
		
		YRCLabelBindingData lblBindingData = new YRCLabelBindingData();
		lblBindingData.setName("lblTitleIcon");
		lblBindingData.setSourceBinding("/XPXCustomerAssignmentViewList/@TotalNumberOfBillToRecords;/XPXCustomerAssignmentViewList/@TotalNumberOfRecords");
		lblBindingData.setKey("authorized_locations_key");
		lblTitleIcon.setData(YRCConstants.YRC_LABEL_BINDING_DEFINITION,lblBindingData);
		
    	YRCTableBindingData bindingData = new YRCTableBindingData();
        YRCTblClmBindingData colBindings[] = new YRCTblClmBindingData[5];
        
        colBindings[0] = new YRCTblClmBindingData();
        colBindings[0].setAttributeBinding("MSAPCustomerID");
        colBindings[0].setColumnBinding("Master Customer Number");
        colBindings[0].setSortReqd(true);
        
        colBindings[1] = new YRCTblClmBindingData();
        colBindings[1].setAttributeBinding("SAPCustomerID");
        colBindings[1].setColumnBinding("Customer Number");
        colBindings[1].setSortReqd(true); 
        
        colBindings[2] = new YRCTblClmBindingData();
        colBindings[2].setAttributeBinding("BillToCustomerID");
        colBindings[2].setColumnBinding("Bill-To Number");
        colBindings[2].setLabelProvider(new IYRCTableColumnTextProvider(){
			public String getColumnText(Element obj) {
				Element eleTableItem = (Element)obj;
				String billToCustomerID  = eleTableItem.getAttribute("BillToCustomerID");
				 if(!YRCPlatformUI.isVoid(billToCustomerID) && billToCustomerID.lastIndexOf("-M-XX-B")!=-1) {					
					return billToCustomerID.substring(0,billToCustomerID.lastIndexOf("-M-XX-B"));
				   }else{
					   return "";
				   }							
			}
        });
        colBindings[2].setSortReqd(true); 
        
        colBindings[3] = new YRCTblClmBindingData();
        colBindings[3].setAttributeBinding("ShipToCustomerID");
        colBindings[3].setColumnBinding("Ship-To Number");
        colBindings[3].setLabelProvider(new IYRCTableColumnTextProvider(){
			public String getColumnText(Element obj) {
				Element eleTableItem = (Element)obj;
				String shipToCustomerID  = eleTableItem.getAttribute("ShipToCustomerID");
				 if(!YRCPlatformUI.isVoid(shipToCustomerID) && shipToCustomerID.lastIndexOf("-M-XX-S")!=-1) {					 
					 return shipToCustomerID.substring(0,shipToCustomerID.lastIndexOf("-M-XX-S"));						 
				   }else{
					   return "";
				   }							
			}
        });
        colBindings[3].setSortReqd(true);   
        
        colBindings[4] = new YRCTblClmBindingData();
        colBindings[4].setAttributeBinding("@ShipToCustomerName;@AddressLine1;@AddressLine2;@City;@State;@Country;@ZipCode");
        colBindings[4].setColumnBinding("Name/Address");    
        colBindings[4].setLabelProvider(new IYRCTableColumnTextProvider(){
			public String getColumnText(Element obj) {
				Element eleTableItem = (Element)obj;
				
				String status = eleTableItem.getAttribute("Status");
				String shipToCustomerName = eleTableItem.getAttribute("ShipToCustomerName");
				String add1 = eleTableItem.getAttribute("AddressLine1");
				String add2 = eleTableItem.getAttribute("AddressLine2");				
				String city = eleTableItem.getAttribute("City");
				String state = eleTableItem.getAttribute("State");
				String country = eleTableItem.getAttribute("Country");
				String zipCode = eleTableItem.getAttribute("ZipCode");
				String firstZip=zipCode;
				String lastZip="";			
				if(!YRCPlatformUI.isVoid(zipCode) && zipCode.length()>5){
				    firstZip=zipCode.substring(0, 5);
				    lastZip="-"+zipCode.substring(5);
				  }
				StringBuffer addressString = new StringBuffer();
				 if(!YRCPlatformUI.isVoid(status) && "30".equalsIgnoreCase(status.trim()))
					 addressString.append("[Suspended] do not use ");
				 if(!YRCPlatformUI.isVoid(shipToCustomerName))
					 addressString.append(shipToCustomerName);
				 if(!YRCPlatformUI.isVoid(add1))
					 addressString.append(", "+add1);
				 if(!YRCPlatformUI.isVoid(add2))
					 addressString.append(", "+add2);
				 if(!YRCPlatformUI.isVoid(city))
					 addressString.append(", "+city);				
				 if(!YRCPlatformUI.isVoid(state))
					 addressString.append(", "+state);
				 if(!YRCPlatformUI.isVoid(firstZip))
					 addressString.append(", "+firstZip+lastZip);
				 if(!YRCPlatformUI.isVoid(country))
					 addressString.append(", "+country);
				 return addressString.toString();
			}
        });
        colBindings[4].setSortReqd(true);

        bindingData.setSortRequired(true);
        bindingData.setSourceBinding("/XPXCustomerAssignmentViewList/XPXCustomerAssignmentView");
        bindingData.setName("tblSearchResults");
        bindingData.setTblClmBindings(colBindings);
        tblResults.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, bindingData);
    }
	private void createPnlResults() {
		GridData pnlResultsGD = new org.eclipse.swt.layout.GridData();
		GridLayout pnlResultsGL = new GridLayout();
		pnlResults = new Group(pnlRoot, SWT.NONE);		   
		pnlResults.setLayout(pnlResultsGL);
		pnlResults.setLayoutData(pnlResultsGD);
		pnlResultsGL.numColumns = 5;
		pnlResultsGL.horizontalSpacing = 0;
		pnlResultsGL.verticalSpacing = 0;
		pnlResultsGL.marginWidth = 0;
		pnlResultsGL.marginHeight = 0;
		pnlResultsGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlResultsGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlResultsGD.grabExcessHorizontalSpace = true;
		pnlResultsGD.grabExcessVerticalSpace = true;
		createPnlTitleHolder();
		createTblResults();
		
	}
	
	private void createPnlTitleHolder() {
		GridLayout pnlTitleHolderGL = new GridLayout();
		GridData pnlTitleHolderGD = new org.eclipse.swt.layout.GridData();
		pnlTitleHolder = new Composite(pnlResults, SWT.NONE);		   
		lblTitleIcon = new Label(pnlTitleHolder, SWT.NONE);	
		pnlTitleHolder.setLayoutData(pnlTitleHolderGD);
		pnlTitleHolder.setLayout(pnlTitleHolderGL);
		pnlTitleHolderGD.horizontalAlignment = org.eclipse.swt.layout.GridData.HORIZONTAL_ALIGN_END;
		pnlTitleHolderGD.grabExcessHorizontalSpace = true;		
		pnlTitleHolderGL.numColumns = 2;
		pnlTitleHolderGL.horizontalSpacing = 5;
		pnlTitleHolderGL.verticalSpacing = 0;
		pnlTitleHolderGL.marginWidth = 0;
		pnlTitleHolderGL.marginHeight = 0;	
	}
	private void createTblResults() {
		GridData tblResultsGD = new org.eclipse.swt.layout.GridData();	
		tblResults = new Table(pnlResults,SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);	

		tblResults.setData("name", "tblResults");
		
		TableColumn clmMasterCustomerNumber = new TableColumn(tblResults, SWT.NONE);
		clmMasterCustomerNumber.setText("Master Customer Number");
		clmMasterCustomerNumber.setWidth(150);
		
		TableColumn clmCustomerNumber = new TableColumn(tblResults, SWT.NONE);
		clmCustomerNumber.setText("Customer Number");
		clmCustomerNumber.setWidth(150);
		
		TableColumn clmBillToCustomerNumber = new TableColumn(tblResults, SWT.NONE);
		clmBillToCustomerNumber.setText("Bill-To Number");
		clmBillToCustomerNumber.setWidth(150);
		
		TableColumn clmShipToCustomerNumber = new TableColumn(tblResults, SWT.NONE);
		clmShipToCustomerNumber.setText("ShipToID");
		clmShipToCustomerNumber.setWidth(150);
		
		TableColumn clmNameAndAddress = new TableColumn(tblResults, SWT.NONE);
		clmNameAndAddress.setText("Address");
		clmNameAndAddress.setWidth(550);
		
		tblResultsGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblResultsGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblResultsGD.grabExcessHorizontalSpace = true;
		tblResultsGD.grabExcessVerticalSpace = true;
		tblResultsGD.horizontalSpan = 5;
		tblResults.setLayoutData(tblResultsGD);
		tblResults.setHeaderVisible(true);
		tblResults.setLinesVisible(true);
	}
	@Override
	public String getFormId() {
		 return FORM_ID;
	}

	@Override
	public String getHelpId() {
		return null;
	}

	@Override
	public IYRCPanelHolder getPanelHolder() {
		return null;
	}

	@Override
	public Composite getRootPanel() {
		return pnlRoot;
	}
	
}

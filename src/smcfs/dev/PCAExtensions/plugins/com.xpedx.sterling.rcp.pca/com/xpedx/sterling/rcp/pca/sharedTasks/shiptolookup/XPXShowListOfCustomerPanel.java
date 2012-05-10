package com.xpedx.sterling.rcp.pca.sharedTasks.shiptolookup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXPaginationBehavior;
import com.xpedx.sterling.rcp.pca.util.XPXPaginationComposite;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCXmlUtils;

/**
 * @author Manas
 *
 * Generated by Sterling RCP Tools
 * Copyright � 2005, 2006 Sterling Commerce, Inc. All Rights Reserved.
 */
public class XPXShowListOfCustomerPanel extends XPXPaginationComposite implements IYRCComposite{
	
	private Composite pnlRoot = null;
    private XPXShowListOfCustomerPanelBehaviour myBehavior;
	private Element elePageInput;
	private Group pnlResults;
	Table tblResults;
	public Element eleSelected;
	private String isMil;
    public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.sharedTasks.shiptolookup.XPXShowListOfCustomerPanel";

	public XPXShowListOfCustomerPanel(Composite parent, int style, Element inputObject) {
		super(parent, style);
		elePageInput = (Element) inputObject;
		isMil = elePageInput.getAttribute("MIL");
		initialize();
		setBindingForComponents();
        myBehavior = new XPXShowListOfCustomerPanelBehaviour(this, FORM_ID, elePageInput);
	}
	
	private void initialize() {
		createRootPanel();
		this.setLayout(new FillLayout());
		setSize(new org.eclipse.swt.graphics.Point(600,400));
	}
	
    private void setBindingForComponents() {
    	YRCTableBindingData bindingData = new YRCTableBindingData();
        YRCTblClmBindingData colBindings[] = new YRCTblClmBindingData[2];
        colBindings[0] = new YRCTblClmBindingData();
        colBindings[0].setAttributeBinding("CustomerID");
        colBindings[0].setColumnBinding("CustomerID");
        colBindings[0].setSortReqd(true); 
        
        colBindings[1] = new YRCTblClmBindingData();
        colBindings[1].setAttributeBinding("@FirstName;@LastName;");
        colBindings[1].setColumnBinding("UserName");
        colBindings[1].setKey("xpedx_username_key");
        colBindings[1].setSortReqd(true);
        colBindings[1].setSortBinding("@FirstName");
        
        bindingData.setSortRequired(true);
        bindingData.setSourceBinding("CustomerList:/CustomerList/Customer");
        bindingData.setName("tblSearchResults");
        bindingData.setTblClmBindings(colBindings);
        tblResults.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, bindingData);
    }
    
    public String getFormId() {
        return FORM_ID;
    }
    
    public Composite getRootPanel() {
        return pnlRoot;
    }

	private void createRootPanel() {
		pnlRoot = new Composite(this, SWT.NONE);
        pnlRoot.setLayout(new GridLayout());
        createPnlResults();
        
	}
	
	/**-------------------------------------------------***/
	private void createPnlResults() {
		GridData pnlResultsGD = new org.eclipse.swt.layout.GridData();
		GridLayout pnlResultsGL = new GridLayout();
		pnlResults = new Group(pnlRoot, SWT.NONE);		   
		pnlResults.setLayout(pnlResultsGL);
		pnlResults.setLayoutData(pnlResultsGD);
		pnlResultsGL.numColumns = 2;
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
		super.createPaginationLinks(pnlResults, XPXShowListOfCustomerPanelBehaviour.PAGINATION_STRATEGY_FOR_SEARCH);
	}
	private void createPnlTitleHolder() {
		XPXUtils.addGradientPanelHeader(pnlResults, "Customer Contact List", true);
	}
	
	private void createTblResults() {
		GridData tblResultsGD = new org.eclipse.swt.layout.GridData();
		if(isMil.equalsIgnoreCase("MIL"))
		{
		tblResults = new Table(pnlResults,SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		}
		else
		{
		tblResults = new Table(pnlResults,SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);	
		}
		tblResults.setData("name", "tblResults");
		TableColumn clmArticleName = new TableColumn(tblResults, SWT.NONE);
		clmArticleName.setText("CustomerID");
		clmArticleName.setWidth(150);//TODO: set appropriate width
		
		TableColumn clmStartDate = new TableColumn(tblResults, SWT.NONE);
		clmStartDate.setText("UserName");
		clmStartDate.setWidth(150);//TODO: set appropriate width
		
		tblResultsGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblResultsGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblResultsGD.grabExcessHorizontalSpace = true;
		tblResultsGD.grabExcessVerticalSpace = true;
		tblResultsGD.horizontalSpan = 2;
		tblResults.setLayoutData(tblResultsGD);
		tblResults.setHeaderVisible(true);
		tblResults.setLinesVisible(true);
		tblResults.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				myBehavior.mouseDoubleClick(e, "tblResults");
				getParent().getShell().close();
			}
		});
		
		
		if(isMil.equalsIgnoreCase("MIL"))
		{
			
		Button btnShiptoList = new Button(pnlRoot, SWT.NONE);
		btnShiptoList.setText("OK ");
		btnShiptoList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.getVal();
				getParent().getShell().close();
			}
		});
		}
	}
	
	
	
	public IYRCPanelHolder getPanelHolder() {
        // TODO Complete getPanelHolder
        return null;
    }
    
    public String getHelpId() {
		// TODO Complete getHelpId
		return null;
	}

	public Element getSelectedShipTo() {
		return eleSelected;
	}

	public XPXPaginationBehavior getPaginationBehavior() {
		return myBehavior;
	}


}

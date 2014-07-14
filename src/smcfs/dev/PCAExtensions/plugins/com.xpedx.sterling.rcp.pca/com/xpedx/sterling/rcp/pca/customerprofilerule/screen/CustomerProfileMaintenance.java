package com.xpedx.sterling.rcp.pca.customerprofilerule.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCScrolledCompositeListener;
import com.yantra.yfc.rcp.YRCWizardBehavior;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class CustomerProfileMaintenance  extends Composite implements IYRCComposite {
	private Composite pnlRoot = null;
	private CustomerProfileRulePanel pnlCustomerProfileRulesObj= null;
	private CustomerProfileInfoPanel pnlGeneralInfoObj = null;
	private Composite pnlDivisionEntitlement = null;
	private CustomerProfileMaintenanceBehavior myBehavior;
	private YRCWizardBehavior wizBehavior;
	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.customerprofilerule.screen.CustomerProfileMaintenance"; // @jve:decl-index=0:
	private Composite pnlLines;
	private ScrolledComposite scrolledPnlforLines;
	private Composite compositeHolder;
	private Composite pnlDynamicLineParent;
	private Composite MiscPnl;
	private Object inputObject;
	private YRCScrolledCompositeListener listener;
	private TabFolder tabFolder = null;
	private Composite pnlCustomerProfileRules = null;
	private Composite pnlCustomerGeneralInfo= null;
	private Composite pnlETrading = null;
	private Button btnUpdateRules;
	private int pnlNo;
	private CustomerProfileMaintIntegDataPanel pnlMaintIntegDataObj;
	private CustomerETradingIDMaintenancePanel  eTradingDataObj;
    private boolean refreshRulesTab=false;
	private TabItem itmPnlCustomerProfileRules;
	String extSuffixType;

	public CustomerProfileMaintenance(Composite parent, int style, Object inputObject) {
		super(parent, style);

		this.inputObject=inputObject;
		pnlNo = 1;
		initialize();
		//setBindingForComponents();
		myBehavior = new CustomerProfileMaintenanceBehavior(this,inputObject);

	}
	private void initialize()
    {
		setData("name", "this");
		GridLayout thislayout = new GridLayout(1, false);
		thislayout.horizontalSpacing = 0;
		thislayout.verticalSpacing = 0;
		thislayout.marginHeight = 0;
		thislayout.marginWidth = 0;
		setLayout(thislayout);
		GridData gridDataPlnRoot = new GridData();
		gridDataPlnRoot.horizontalAlignment = 4;
		gridDataPlnRoot.verticalAlignment = 4;
		gridDataPlnRoot.grabExcessHorizontalSpace = true;
		gridDataPlnRoot.grabExcessVerticalSpace = true;
		setLayoutData(gridDataPlnRoot);
		createPnlRoot();
    }
	private void createPnlRoot()
    {
		pnlRoot = new Composite(this, 0);
		pnlRoot.setData("name", "pnlRoot");
		GridData pnlRootlayoutData = new GridData();
		pnlRootlayoutData.horizontalAlignment = 4;
		pnlRootlayoutData.verticalAlignment = 4;
		pnlRootlayoutData.grabExcessHorizontalSpace = true;
		pnlRootlayoutData.grabExcessVerticalSpace = true;
		pnlRoot.setLayoutData(pnlRootlayoutData);
		GridLayout gridLayoutPnl = new GridLayout(1, false);
		pnlRoot.setLayout(gridLayoutPnl);

		//createTabFolder();
    }
	public void createTabFolder(Element generalInfo)
    {
		tabFolder = new TabFolder(pnlRoot, 128);
		tabFolder.setData("name", "tabFolder");
		GridData tabFolderlayoutData = new GridData();
		tabFolderlayoutData.horizontalAlignment = 4;
		tabFolderlayoutData.verticalAlignment = 4;
		tabFolderlayoutData.grabExcessHorizontalSpace = true;
		tabFolderlayoutData.grabExcessVerticalSpace = true;
		tabFolder.setLayoutData(tabFolderlayoutData);
		tabFolder.setData("yrc:customType", "TaskComposite");
		tabFolder.setLayout(new GridLayout(1, false));

		tabFolder.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
		        if(isRefreshTabs())
		        {
			        if(!YRCPlatformUI.isVoid(tabFolder.getSelection())&& "Manage Customer Rules".equals(tabFolder.getSelection()[0].getText()))
			        {
			        	pnlCustomerProfileRulesObj.dispose();
			        	createPnlCustomerProfileRules();
			        }
		        }
		        refreshTabs(false);
		      }

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		  });
//			XPXUtils.paintPanel(tabFolder);
		createPnlCustomerGeneralInfoData(generalInfo);

		createDivisionEntitlement();

		if (this.isRulesAvailable()) {

			createPnlCustomerProfileRules();
		}

		if (this.isMasterCustomer() && YRCPlatformUI.hasPermission(XPXConstants.RES_ID_MANAGE_CUSTOMER_OR_USER_INTEG_ATTR)) {

			createPnlCustomerProfileMaintIntegData();
		}
//		createPnlManageArticles();
		createPnlCustomerETradingData();
		pnlRoot.layout(true, true);
    }

	private boolean isMasterCustomer() {
		Element eleExtn = YRCXmlUtils.getXPathElement(myBehavior.getCustomerDetails(),
		"/CustomerList/Customer/Extn");
		if(eleExtn != null){
			extSuffixType = YRCXmlUtils.getAttribute(eleExtn , "ExtnSuffixType");
			if ("MC".equals(extSuffixType)) {
				return true;
			}
		}
		return false;
	}

	private boolean isShipToCustomer() {
		Element eleExtn = YRCXmlUtils.getXPathElement(myBehavior.getCustomerDetails(),
		"/CustomerList/Customer/Extn");
		if(eleExtn != null){
			extSuffixType = YRCXmlUtils.getAttribute(eleExtn , "ExtnSuffixType");
			if ("S".equals(extSuffixType)) {
				return true;
			}
		}
		return false;
	}


	private boolean isRulesAvailable() {
		Element eleExtn = YRCXmlUtils.getXPathElement(myBehavior.getCustomerDetails(),
		"/CustomerList/Customer/Extn");
		if(eleExtn != null){
			extSuffixType = YRCXmlUtils.getAttribute(eleExtn , "ExtnSuffixType");
			if ("C".equals(extSuffixType)||"B".equals(extSuffixType)) {
				return true;
			}
		}
		return false;
	}

	private void createPnlCustomerProfileMaintIntegData() {

		pnlMaintIntegDataObj =new CustomerProfileMaintIntegDataPanel(tabFolder, SWT.NONE, this.inputObject, this);
		pnlMaintIntegDataObj.setData("name", "pnlMaintIntegDataPanel");

		TabItem itmPnlCustomerProfileRules = new TabItem(tabFolder, 0);
		itmPnlCustomerProfileRules.setText(YRCPlatformUI.getString("Tab_Customer_Maintain_Integration_Data"));
		itmPnlCustomerProfileRules.setControl(pnlMaintIntegDataObj);
	}

	private void createPnlCustomerETradingData() {

		eTradingDataObj =new CustomerETradingIDMaintenancePanel(tabFolder, SWT.NONE, this.inputObject, this);
		eTradingDataObj.setData("name", "pnlETradingDataPanel");

		TabItem itmPnlCustomerProfileInfo = new TabItem(tabFolder, 0);
		itmPnlCustomerProfileInfo.setText(YRCPlatformUI.getString("Tab_eTrading_Maintainenance"));
		itmPnlCustomerProfileInfo.setControl(eTradingDataObj);

	}

	private void createPnlCustomerProfileRules()
    {
		pnlCustomerProfileRulesObj =new CustomerProfileRulePanel(tabFolder, SWT.NONE,this.inputObject, this);
		pnlCustomerProfileRulesObj.setData("name", "pnlProfileRulesPanel");
		if(!isRefreshTabs())
		{
			itmPnlCustomerProfileRules = new TabItem(tabFolder, 0);
			itmPnlCustomerProfileRules.setText(YRCPlatformUI.getString("Tab_Customer_Rules"));
			itmPnlCustomerProfileRules.setControl(pnlCustomerProfileRulesObj);
		}
		else
		{
			itmPnlCustomerProfileRules.setControl(pnlCustomerProfileRulesObj);

		}


    }
	private void createPnlCustomerGeneralInfoData(Element generalInfo)
    {

		pnlGeneralInfoObj =new CustomerProfileInfoPanel(tabFolder, SWT.NONE,this.inputObject, this);
		pnlGeneralInfoObj.setData("name", "pnlGeneralInfoPanel");

		TabItem itmPnlCustomerProfileInfo = new TabItem(tabFolder, 0);
		itmPnlCustomerProfileInfo.setText(YRCPlatformUI.getString("Tab_Customer_Profile_Info"));
		itmPnlCustomerProfileInfo.setControl(pnlGeneralInfoObj);
    }
	/**
	 * Adds 'Manage Articles' as Tab Item to the Customer Maintenance TabFolder
	 */

	private void createDivisionEntitlement()
    {
		if (isShipToCustomer()) {
			pnlDivisionEntitlement = new DivisionEntitlementPanel(tabFolder, SWT.NONE,this.inputObject, this);
		}
		else {
			pnlDivisionEntitlement = new DivisionEntitlementTreePanel(tabFolder, SWT.NONE,this.inputObject, this);
		}
		pnlDivisionEntitlement.setData("name", "pnlDivisionEntitlement");

		TabItem itmPnlArticles = new TabItem(tabFolder, 0);
		itmPnlArticles.setText(YRCPlatformUI.getString("Entitlement"));
		itmPnlArticles.setControl(pnlDivisionEntitlement);
    }

	public String getFormId() {
		return FORM_ID;
	}
	public Composite getRootPanel() {
		return pnlRoot;
	}

	public Object getInput() {
		Object inObject = null;
		org.eclipse.ui.part.WorkbenchPart currentPart = YRCDesktopUI.getCurrentPart();
		inObject = ((YRCEditorPart) currentPart).getEditorInput();
		return inObject;
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

	/* Added */
	public YRCWizardBehavior getWizardBehavior() {
		return wizBehavior;
	}

	/* Added */
	public void setWizBehavior(YRCWizardBehavior wizBehavior) {
		this.wizBehavior = wizBehavior;
	}
	public CustomerProfileMaintIntegDataPanel getMaintIntegDataObj(){
		return pnlMaintIntegDataObj;
	}
	public CustomerProfileRulePanel getCustomerProfileRuleObj(){
		return pnlCustomerProfileRulesObj;
	}
	public CustomerProfileInfoPanel getCustomerProfileInfoObj(){
		return pnlGeneralInfoObj;
	}
	public CustomerProfileMaintenanceBehavior getBehavior(){
		return myBehavior;
	}
	public boolean isRefreshTabs() {
		return refreshRulesTab;
	}
	public void refreshTabs(boolean refreshTabs) {
		this.refreshRulesTab = refreshTabs;
	}
}
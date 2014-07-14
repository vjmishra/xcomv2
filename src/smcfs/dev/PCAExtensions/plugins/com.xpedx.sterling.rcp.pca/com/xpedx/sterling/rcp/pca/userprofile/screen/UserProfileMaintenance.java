package com.xpedx.sterling.rcp.pca.userprofile.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCWizardBehavior;

public class UserProfileMaintenance extends Composite implements IYRCComposite {
	private Composite pnlRoot = null;

	private UserProfileMaintenanceBehavior myBehavior;

	private YRCWizardBehavior wizBehavior;

	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.userprofile.screen.UserProfileMaintenance"; // @jve:decl-index=0:

	private Composite pnlProfileInfo;

	private ScrolledComposite scrolledPnlforInfo;

	private Composite pnlPrimaryInformation;

	private Composite compositeMiscPnl;

	private Object inputObject;

	private int pnlNo;

	private Button btnUpdate;

	private Label lblEmployeeId;

	private Label lblUserType;

	private Label lblMaxOrderAmount;

	private Label lblMinOrderAmount;

	private Label lbAdditionalEmailAddress;

	private Label lblB2BCatalogView;

	private Label lblReceiveOrderConfirmEmails;

	private Label lblPrefferedCatalog;

	private Label lblDefaultShipTo;

	private Label lblPOList;

	private Text txtMaxOrderAmount;

	private Text txtMinOrderAmount;

	private Text txtAdditionalEmailAddress;

	private Text txtPOList;

	private Combo comboDefaultShipTo;

	private Combo comboPrefferedCatalog;

	private Combo comboReceiveOrderConfirmEmails;

	private Button chkB2BCatalogView;

	public Table itemPOList;

	public Table itemAdditionalEmails;

	private Text txtEmployeeId;

	private Label lblViewInvoices;

	private Label lblEstimator;

	private Label lblStockCheckWS;

	private Button chkViewInvoices;

	private Button chkEstimator;

	private Button chkStockCheckWS;

	private Composite pnlAdditionalEmailsHolder;

	private Composite pnlPOListBody;

	private Composite pnlPOListHolder;

	private TableColumn tblPOListId;

	private Composite pnlPOListButtons;

	private Button btnAdd;

	private Button btnRmv;

	private Composite pnlAdditionalEmailsBody;

	private TableColumn tblAdditionalEmailsId;

	private Composite pnlAdditionalEmailsButtons;

	private Button btnAssign;

	private Button btnRemove;
	
	private Element customerContactEle;
	
	public UserProfileMaintenance(Composite parent, int style,
			Object inputObject, Element customerContactEle) {
super(parent, style);
		
		this.inputObject=inputObject;
		pnlNo = 1;
		this.customerContactEle =customerContactEle;
		initialize();
		
		//setBindingForComponents();
		myBehavior = new UserProfileMaintenanceBehavior(this,inputObject);

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

		createTabFolder();  
    }
	private TabFolder tabFolder;
	public void createTabFolder()
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
		createPnlUserProfileInfo();
		createPnlCustomerAssignmentInfoData();
		pnlRoot.layout(true, true);
    }
	private UserProfileInfoDetails pnlUserProfileInfoDetailsObj;
	private void createPnlUserProfileInfo()
    {
		pnlUserProfileInfoDetailsObj =new UserProfileInfoDetails(tabFolder, SWT.NONE,this.inputObject,customerContactEle);
		pnlUserProfileInfoDetailsObj.setData("name", "pnlUserProfileInfoDetailsObj");
		
		TabItem itmUserProfileInfoDetails = new TabItem(tabFolder, 0);
		itmUserProfileInfoDetails.setText(YRCPlatformUI.getString("Tab_User_Info"));
		itmUserProfileInfoDetails.setControl(pnlUserProfileInfoDetailsObj);
       
    }
	private CustomerAssignmentPanel pnlCustomerAssignmentInfoObj;
	private void createPnlCustomerAssignmentInfoData()
    {
		
		pnlCustomerAssignmentInfoObj =new CustomerAssignmentPanel(tabFolder, SWT.NONE,this.inputObject, customerContactEle);
		pnlCustomerAssignmentInfoObj.setData("name", "pnlCustomerAssignmentInfoObj");

		TabItem itmPnlCustomerAssignmentInfo = new TabItem(tabFolder, 0);
		itmPnlCustomerAssignmentInfo.setText(YRCPlatformUI.getString("Tab_Customer_Assignment_Info"));
		itmPnlCustomerAssignmentInfo.setControl(pnlCustomerAssignmentInfoObj);
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
	public UserProfileInfoDetails getUserProfileInfoDetailsObj(){
		return pnlUserProfileInfoDetailsObj;
	}
	
	public CustomerAssignmentPanel getCustomerAssignmentInfoObj(){
		return pnlCustomerAssignmentInfoObj;
	}
	
	public UserProfileMaintenanceBehavior getBehavior(){
		return myBehavior;
	}
}

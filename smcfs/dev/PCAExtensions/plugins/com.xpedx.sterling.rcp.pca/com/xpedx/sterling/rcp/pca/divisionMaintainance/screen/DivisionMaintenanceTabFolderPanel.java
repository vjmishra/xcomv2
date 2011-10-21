	
/*
 * Created on Jul 19,2010
 *
 */
package com.xpedx.sterling.rcp.pca.divisionMaintainance.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.articles.screen.ArticlesSearchListPanel;
import com.xpedx.sterling.rcp.pca.catalogexport.screen.CatalogExportSearchListPanel;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCPlatformUI;

/**
 * @author sdodda
 *
 * Holds Division Maintenance Tab Folder Panel: which has 2 Tab Items 'Division Info' and 'Manage Articles'
 */

public class DivisionMaintenanceTabFolderPanel extends Composite implements IYRCComposite {

	private Composite pnlRoot = null;
	private TabFolder tabFolder = null;
	private DivisionMaintainancePanel pnlDivMaintainance = null;
	private ArticlesSearchListPanel pnlManageArticles = null;
	private CatalogExportSearchListPanel pnlCatalogExport = null;
	private Object inputObject = null;
    private DivisionMaintenanceTabFolderPanelBehavior myBehavior;
    public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.divisionMaintainance.screen.DivisionMaintenanceTabFolderPanel";

	public DivisionMaintenanceTabFolderPanel(Composite parent, int style, Object inputObject) {
		super(parent, style);
		this.inputObject = inputObject;
		initialize();
        setBindingForComponents();
        myBehavior = new DivisionMaintenanceTabFolderPanelBehavior(this, FORM_ID, inputObject);
	}
	
	private void initialize() {
		createRootPanel();
		this.setLayout(new FillLayout());
		setSize(new org.eclipse.swt.graphics.Point(300,200));
	}
	
    private void setBindingForComponents() {
        //TODO: set all bindings here
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
        this.createTabFolder(null);
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
		createPnlDivisionData();
//		createPnlManageArticles();
		createPnlCatalogExport();
		pnlRoot.layout(true, true);
    }
	
	private void createPnlDivisionData()
    {
		pnlDivMaintainance = new DivisionMaintainancePanel(tabFolder, SWT.NONE,this.inputObject);
		pnlDivMaintainance.setData("name", "pnlDivMaintainance");

		TabItem itmPnlDivisionData = new TabItem(tabFolder, 0);
		itmPnlDivisionData.setText(YRCPlatformUI.getString("Tab_Division_Info"));
		itmPnlDivisionData.setControl(pnlDivMaintainance);
    }
	/**
	 * Adds 'Manage Articles' as Tab Item to the Customer Maintenance TabFolder
	 */
	private void createPnlManageArticles()
    {
		
		pnlManageArticles = new ArticlesSearchListPanel(tabFolder, SWT.NONE,this.inputObject);
		pnlManageArticles.setData("name", "pnlManageArticles");

		TabItem itmPnlArticles = new TabItem(tabFolder, 0);
		itmPnlArticles.setText(YRCPlatformUI.getString("Tab_Articles"));
		itmPnlArticles.setControl(pnlManageArticles);
    }
	private void createPnlCatalogExport()
    {
		
		pnlCatalogExport = new CatalogExportSearchListPanel(tabFolder, SWT.NONE,this.inputObject);
		pnlCatalogExport.setData("name", "pnlCatalogExport");

		TabItem itmPnlCatalogExports = new TabItem(tabFolder, 0);
		itmPnlCatalogExports.setText(YRCPlatformUI.getString("TAB_CATALOG_EXPORT"));
		itmPnlCatalogExports.setControl(pnlCatalogExport);
    }
	
	public IYRCPanelHolder getPanelHolder() {
        // TODO Complete getPanelHolder
        return null;
    }
    
    public String getHelpId() {
		// TODO Complete getHelpId
		return null;
	}
  
}

/*
 * Created on Jul 09,2010
 *
 */
package com.xpedx.sterling.rcp.pca.tasks.catalogexport.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.articles.screen.ArticlesSearchListPanel;
import com.xpedx.sterling.rcp.pca.catalogexport.screen.CatalogExportSearchListPanel;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTextBindingData;

/**
 * @author vchandra
 * 
 * Manage Articles Panel, used while Creating a new Article and modifying an
 * existing article. it expects a inputObject(Element) XML like this:<br>
 * 
 * <pre>
 * &lt;XPXArticle OrganizationCode=&quot;required&quot; XPXDivision=&quot;required&quot; ArticleName=&quot;optional&quot;/&gt;
 * </pre>
 * 
 * If ArticleName is sent Article Name field will not be editable.
 */

public class XPXManageCatalogExportPopupPanel extends Composite implements
		IYRCComposite {

	private Composite pnlRoot;
	private Group grpSearchFields;
	private Label lblArticleName;
	private Text txtArticleName;
	private Label lblArticleDesc;
	private Text txtArticleDesc;
	private Label lblStartDate;
	private Text txtStartDate;
	private Button btnStartDateLookup;
	private Label lblEndDate;
	private Text txtEndDate;
	private Label lblLastModifiedData;
	private Text txtLastModifiedData;
	private Label lblModifiedBy;
	private Text txtModifiedBy;
	private Button btnEndDateLookup;
	private Composite pnlButtonHolder;
	private Button btnCreate;
	private Button btnDelete;
	private Button btnCancel;
	private XPXManageCatalogExportPopupPanelBehavior myBehavior;
	private CatalogExportSearchListPanel invokerPage;
	private Element elePageInput;
	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.tasks.catalogexport.screen.XPXManageCatalogExportPopupPanel";
	private Label lblLabel;
	private Label lblURL;
	private Text txtLabel;
	private Text txtURL;
	private Label lblBrand;
	private Combo cmbBrand;
	Element elebrands=null;
	public XPXManageCatalogExportPopupPanel(Composite parent, int style, Object inputObject) {
		super(parent, style);
		if (parent instanceof CatalogExportSearchListPanel) {
			invokerPage = (CatalogExportSearchListPanel) parent;
		}
		elePageInput = (Element) inputObject;
		
		elebrands=invokerPage.getMyBehavior().getEleBrands();

		initialize();
		setBindingForComponents();
		myBehavior = new XPXManageCatalogExportPopupPanelBehavior(this, FORM_ID,elebrands);
		updateNonBindedComponents();
		pnlRoot.layout(true, true);
	}

	private void initialize() {
		createRootPanel();
		this.setLayout(new FillLayout());
		setSize(new org.eclipse.swt.graphics.Point(300, 200));
	}

	private void setBindingForComponents() {
		YRCTextBindingData tbd = new YRCTextBindingData();
		tbd.setName("txtURL");
		tbd.setTargetBinding("SaveArticle:/XPXCatalogExp/@Url");
		tbd.setSourceBinding("/XPXCatalogExp/@Url");
		txtURL.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);

		tbd = new YRCTextBindingData();
		tbd.setName("txtLabel");
		if(null != txtLabel && txtLabel.isDisposed()==false){
			txtLabel.setEditable(true);
		}
		tbd.setTargetBinding("SaveArticle:/XPXCatalogExp/@Label");
		tbd.setSourceBinding("/XPXCatalogExp/@Label");
		txtLabel.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		YRCComboBindingData cbd=new YRCComboBindingData();
		cbd.setTargetBinding("SaveArticle:/XPXCatalogExp/@BrandCode");
		cbd.setSourceBinding("/XPXCatalogExp/@BrandCode");
		cbd.setCodeBinding("@CodeValue");
		cbd.setDescriptionBinding("@CodeShortDescription");
		cbd.setListBinding("BrandCodesList:/CommonCodeList/CommonCode");
		cbd.setName("cmbBrand");
		cmbBrand.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
		cmbBrand.setData(YRCConstants.YRC_CONTROL_NAME,"cmbBrand");
		

	}
	private void updateNonBindedComponents() {
		
		if(null != txtLabel && txtLabel.isDisposed()==false){
			txtLabel.setEditable(true);
		}
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
		createPnlBody();
		createpnlButtonHolder();
		pnlRoot.setData("yrc:customType", "TaskComposite");
	}

	private void createPnlBody() {
		GridLayout srchGroupLayout = new GridLayout();
		GridData gdSrchGroup = new GridData();
		grpSearchFields = new Group(pnlRoot, SWT.NONE);

		gdSrchGroup.grabExcessHorizontalSpace = true;
		gdSrchGroup.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		grpSearchFields.setLayoutData(gdSrchGroup);
		grpSearchFields.setLayout(srchGroupLayout);
		srchGroupLayout.numColumns = 4;

		XPXUtils.addGradientPanelHeader(grpSearchFields, "CATALOG_EXPORT_HEADER", true);

		// Add fields starts here
		// Article Name Field
		
		lblLabel = new Label(grpSearchFields, SWT.NONE);
		lblLabel.setData(YRCConstants.YRC_CONTROL_NAME, "lblLabel");
		lblLabel.setText("Label_LBL");

		txtLabel = new Text(grpSearchFields, SWT.BORDER);
		txtLabel.setTextLimit(50);
		txtLabel.setEditable(true);
		/*if(null != txtLabel && txtLabel.isDisposed()==false){
			txtLabel.setEditable(true);
		}*/
		GridData gdLabel = new GridData();
		gdLabel.grabExcessHorizontalSpace = true;
		//gdLabel.horizontalSpan = 2;
		gdLabel.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtLabel.setLayoutData(gdLabel);

		lblURL = new Label(grpSearchFields, SWT.NONE);
		lblURL.setData(YRCConstants.YRC_CONTROL_NAME, "lblURL");
		lblURL.setText("URL_LBL");
		
		txtURL = new Text(grpSearchFields, SWT.BORDER);
		txtURL.setTextLimit(50);
		txtURL.setEditable(true);
		GridData gdArticleName = new GridData();
		gdArticleName.grabExcessHorizontalSpace = true;
		gdArticleName.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtURL.setLayoutData(gdArticleName);
		
		lblBrand = new Label(grpSearchFields, SWT.NONE);
		lblBrand.setData(YRCConstants.YRC_CONTROL_NAME, "lblBrand");
		lblBrand.setText("Division_Brand");

		cmbBrand = new Combo(grpSearchFields, 8);
		cmbBrand.setLayoutData(gdArticleName);
		
		
	}

	private void createpnlButtonHolder() {
		GridData gridData10 = new GridData();
		gridData10.heightHint = 25;
		gridData10.widthHint = 80;
		GridData gridData5 = new GridData();
		gridData5.heightHint = 25;
		gridData5.widthHint = 80;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = 3;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = SWT.BEGINNING;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 3;
		pnlButtonHolder = new Composite(pnlRoot, 0);
		pnlButtonHolder.setLayout(gridLayout2);
		pnlButtonHolder.setLayoutData(gridData2);
		pnlButtonHolder.setData("name", "pnlButtonHolder");
		pnlButtonHolder.setData("yrc:customType", "TaskComposite");
		
		//Create Button, name of the button will be changed dynamically to Update in case of Article getting Created Successfully
		btnCreate = new Button(pnlButtonHolder, 0);
		btnCreate.setText("Export_Create");
		btnCreate.setLayoutData(gridData5);
		btnCreate.setData("name", "btnCreate");
		btnCreate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(YRCPlatformUI.isVoid(myBehavior.getFieldValue("txtLabel"))){
					YRCPlatformUI.showInformation("Label", "Please Enter a Label");
					txtLabel.setFocus();
				}else if(YRCPlatformUI.isVoid(myBehavior.getFieldValue("txtURL"))){
					YRCPlatformUI.showInformation("URL", "Please Enter a URL");
					txtURL.setFocus();
				}else if(YRCPlatformUI.isVoid(myBehavior.getFieldValue("cmbBrand"))){
					YRCPlatformUI.showInformation("Brand", "Please Select a Brand value");
					cmbBrand.setFocus();
				}
				else{
				myBehavior.create();
				}
			}
		});
		//Delete button
		btnDelete = new Button(pnlButtonHolder, 0);
		btnDelete.setText("Export_Delete");
		btnDelete.setLayoutData(gridData10);
		btnDelete.setData("name", "btnDelete");
		btnDelete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.delete();
				
			}
		});
		btnDelete.setEnabled(false);
		//Cancel button
		btnCancel = new Button(pnlButtonHolder, 0);
		btnCancel.setText("Export_Close");
		btnCancel.setLayoutData(gridData10);
		btnCancel.setData("name", "btnCancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getParent().getShell().close();
			}
		});
	}

	public IYRCPanelHolder getPanelHolder() {
		return null;
	}

	public String getHelpId() {
		// TODO Complete getHelpId
		return null;
	}

	/**
	 * Returns Input Object of the Panel
	 * @return Element object
	 */
	public Element getPageInput() {
		return elePageInput;
	}

	/**
	 * Returns base Panel which is invoking this Task: ArticlesSearchListPanel
	 * @return ArticlesSearchListPanel
	 */
	public CatalogExportSearchListPanel getInvokerPage() {
		return invokerPage;
	}

	public void setPageInput(Element elePageInput) {
		this.elePageInput = elePageInput;
	}

}


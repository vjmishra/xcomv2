/*
 * Created on Jul 09,2010
 *
 */
package com.xpedx.sterling.rcp.pca.tasks.articles.screen;

import java.util.HashMap;

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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.articles.screen.ArticlesSearchListPanel;
import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCButtonBindingData;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;

/**
 * @author sdodda
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

public class XPXManageArticlePopupPanel extends Composite implements
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
	private Button btnCancel;
	private XPXManageArticlePopupPanelBehavior myBehavior;
	private ArticlesSearchListPanel invokerPage;
	private Element elePageInput;
	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.tasks.articles.screen.XPXManageArticlePopupPanel";
	private Label lblArticleType;
	private Composite pnlRadioButtons;	
	private Button btnStorefrontCode;
	private Button btnDivision;
	private YRCComboBindingData cbd;
	private HashMap map = new HashMap();

	public XPXManageArticlePopupPanel(Composite parent, int style, Object inputObject) {
		super(parent, style);
		if (parent instanceof ArticlesSearchListPanel) {
			invokerPage = (ArticlesSearchListPanel) parent;
		}
		elePageInput = (Element) inputObject;
		initialize();
		setBindingForComponents();
		getCodeValue();
		myBehavior = new XPXManageArticlePopupPanelBehavior(this, FORM_ID);
		checkUserPermissions();
		pnlRoot.layout(true, true);
	}

	private void initialize() {
		createRootPanel();
		this.setLayout(new FillLayout());
		setSize(new org.eclipse.swt.graphics.Point(300, 200));
	}

	private void setBindingForComponents() {
		YRCTextBindingData tbd = new YRCTextBindingData();
		tbd.setName("txtArticleName");
		tbd.setTargetBinding("SaveArticle:/XPXArticle/@ArticleName");
		tbd.setSourceBinding("/XPXArticle/@ArticleName");
		txtArticleName.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);

		tbd = new YRCTextBindingData();
		tbd.setName("txtArticleDesc");
		tbd.setTargetBinding("SaveArticle:/XPXArticle/@Article");
		//Binding data like this for a field which has max size 2000 creating problems even Text area.
//		bdArticleDesc.setSourceBinding("/XPXArticle/@Article");
		txtArticleDesc.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setName("txtStartDate");
		tbd.setTargetBinding("SaveArticle:/XPXArticle/@StartDate");
		tbd.setSourceBinding("/XPXArticle/@StartDate");
		txtStartDate.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);

		tbd = new YRCTextBindingData();
		tbd.setName("txtEndDate");
		tbd.setTargetBinding("SaveArticle:/XPXArticle/@EndDate");
		tbd.setSourceBinding("/XPXArticle/@EndDate");
		txtEndDate.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setName("txtLastModifiedData");
		tbd.setTargetBinding("SaveArticle:/XPXArticle/@Modifyts");
		tbd.setSourceBinding("/XPXArticle/@Modifyts");
		txtLastModifiedData.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		tbd = new YRCTextBindingData();
		tbd.setName("txtModifiedBy");
		tbd.setTargetBinding("SaveArticle:/XPXArticle/@Modifyuserid");
		tbd.setSourceBinding("/XPXArticle/@Modifyuserid");
		txtModifiedBy.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		
		YRCButtonBindingData btnStorefrontCodeBindingData = new YRCButtonBindingData();
		btnStorefrontCodeBindingData.setName("btnStorefrontCode");
		btnStorefrontCodeBindingData
				.setTargetBinding("SaveArticle:/XPXArticle/@ArticleType");
		btnStorefrontCodeBindingData
				.setSourceBinding("/XPXArticle/@ArticleType");
		//todo:verify exact string
		btnStorefrontCodeBindingData.setCheckedBinding("S");
		btnStorefrontCode.setData("YRCButtonBindingDefination",
				btnStorefrontCodeBindingData);
//		btnStorefrontCode.notifyListeners(1,new SelectionEvent());
		
		YRCButtonBindingData btnDivisionBindingData = new YRCButtonBindingData();
		btnDivisionBindingData.setName("btnDivision");
		btnDivisionBindingData
				.setTargetBinding("SaveArticle:/XPXArticle/@ArticleType");
		btnDivisionBindingData
				.setSourceBinding("/XPXArticle/@ArticleType");
		btnDivisionBindingData.setCheckedBinding("D");
		btnDivision.setData("YRCButtonBindingDefination",
				btnDivisionBindingData);
		
		cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@OrganizationCode");
		cbd.setDescriptionBinding("@OrganizationCode");
		cbd.setListBinding("StoreFronts:/OrganizationList/Organization");		
		cbd.setSourceBinding("XPXArticle:XPXArticle/@OrganizationCode");
		cbd.setTargetBinding("SaveArticle:/XPXArticle/@OrganizationCode");
		cbd.setName("comboStorefrontCode");		
		comboStorefrontCode.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);		
		

	}

	private Table tblDivisions;
	private Table tblAssignedToDivisions;

	private Label lblStorefronCode;
	private Combo comboStorefrontCode;
	private void addArticleToDivisions() {
		pnlDivisions = new Composite(grpSearchFields, 0);
		pnlDivisions.setBackgroundMode(0);
		pnlDivisions.setData("name", "pnlDivisions");
		GridData gdDivisions = new GridData();
		gdDivisions.horizontalAlignment = 4;
		gdDivisions.verticalAlignment = 4;
		gdDivisions.grabExcessHorizontalSpace = true;
		gdDivisions.grabExcessVerticalSpace = true;
		gdDivisions.horizontalSpan = 3;
		pnlDivisions.setLayoutData(gdDivisions);
		GridLayout layoutDivisions = new GridLayout(1, false);
		layoutDivisions.marginHeight = 0;
		layoutDivisions.marginWidth = 0;
		layoutDivisions.numColumns = 3;
		pnlDivisions.setLayout(layoutDivisions);
		
		tblDivisions = new Table(pnlDivisions, 67586);
		tblDivisions.setHeaderVisible(true);
		tblDivisions.setLinesVisible(true);
		tblDivisions.setData("name", "tblDivisions");
		GridData gdSrcDivisions = new GridData();
		gdSrcDivisions.horizontalAlignment = 4;
		gdSrcDivisions.verticalAlignment = 4;
		gdSrcDivisions.grabExcessHorizontalSpace = true;
		gdSrcDivisions.grabExcessVerticalSpace = true;
		gdSrcDivisions.heightHint = 100;
		tblDivisions.setLayoutData(gdSrcDivisions);
		TableColumn clmSrcDivisions = new TableColumn(tblDivisions, 16384);
		clmSrcDivisions.setWidth(70);
		clmSrcDivisions.setResizable(true);
		clmSrcDivisions.setMoveable(true);
		
		TableColumn clmSrcDivisionsName = new TableColumn(tblDivisions, 16384);
		clmSrcDivisionsName.setWidth(133);
		clmSrcDivisionsName.setResizable(true);
		clmSrcDivisionsName.setMoveable(true);
		
		/* Bindings....*****/
		YRCTableBindingData tblbd = new YRCTableBindingData();
		YRCTblClmBindingData colBindings[] = new YRCTblClmBindingData[2];
		colBindings[0] = new YRCTblClmBindingData();
		colBindings[0].setAttributeBinding("OrganizationCode");
		colBindings[0].setColumnBinding("Division");
		colBindings[0].setSortReqd(true);
		colBindings[1] = new YRCTblClmBindingData();
		colBindings[1].setAttributeBinding("OrganizationName");
		colBindings[1].setColumnBinding("Name");
		colBindings[1].setSortReqd(true);
		tblbd.setSortRequired(true);
		tblbd.setSourceBinding("Divisions:/OrganizationList/Organization");
		tblbd.setName("tblDivisions");
		tblbd.setTblClmBindings(colBindings);
		tblDivisions.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, tblbd);
		
		/* ******/
		Composite pnlManageButtons = new Composite(pnlDivisions, 0);
		pnlManageButtons.setBackgroundMode(0);
		pnlManageButtons.setData("name", "pnlManageButtons");
		GridData gdManageButtons = new GridData();
		gdManageButtons.horizontalAlignment = 4;
		gdManageButtons.verticalAlignment = 4;
		pnlManageButtons.setLayoutData(gdManageButtons);
		GridLayout layoutManageButtons = new GridLayout(1, false);
		layoutManageButtons.marginHeight = 0;
		layoutManageButtons.marginWidth = 0;
		pnlManageButtons.setLayout(layoutManageButtons);
		Button btnAdd = new Button(pnlManageButtons, 8);
		GridData btnPOListlayoutData = new GridData();
		btnPOListlayoutData.horizontalAlignment = 16777216;
		btnPOListlayoutData.verticalAlignment = 16777224;
		btnPOListlayoutData.grabExcessHorizontalSpace = true;
		btnPOListlayoutData.grabExcessVerticalSpace = true;
		btnPOListlayoutData.heightHint = 25;
		btnPOListlayoutData.widthHint = 90;
		btnAdd.setLayoutData(btnPOListlayoutData);
		btnAdd.setData("name","btnAdd");
		btnAdd.setText("Assign-->");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.addSelectedDivisions(e);
				System.out.println("btnAdd Selected");
			}
		});
		Button btnRmv = new Button(pnlManageButtons, 8);
		GridData btnRemovelayoutData = new GridData();
		btnRemovelayoutData.horizontalAlignment = 16777216;
		btnRemovelayoutData.verticalAlignment = 1;
		btnRemovelayoutData.grabExcessVerticalSpace = true;
		btnRemovelayoutData.heightHint = 25;
		btnRemovelayoutData.widthHint = 90;
		btnRmv.setLayoutData(btnRemovelayoutData);
		btnRmv.setData("name","btnRmv");
		btnRmv.setText("<--Remove");
		btnRmv.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.removeSelectedDivisions(e);			
			}
		});
		
		/* *******/
		
		tblAssignedToDivisions = new Table(pnlDivisions, 67586);
		tblAssignedToDivisions.setHeaderVisible(true);
		tblAssignedToDivisions.setLinesVisible(true);
		tblAssignedToDivisions.setData("name", "tblAssignedToDivisions");
		GridData gdAssignedToDivisions = new GridData();
		gdAssignedToDivisions.horizontalAlignment = 4;
		gdAssignedToDivisions.verticalAlignment = 4;
		gdAssignedToDivisions.grabExcessHorizontalSpace = true;
		gdAssignedToDivisions.grabExcessVerticalSpace = true;
		gdAssignedToDivisions.heightHint = 100;
		tblAssignedToDivisions.setLayoutData(gdAssignedToDivisions);
		TableColumn clmAssignedToDivisions = new TableColumn(tblAssignedToDivisions, 16384);
		clmAssignedToDivisions.setWidth(70);
		clmAssignedToDivisions.setResizable(true);
		clmAssignedToDivisions.setMoveable(true);
		
		TableColumn clmAssignedToDivisionsName = new TableColumn(tblAssignedToDivisions, 16384);
		clmAssignedToDivisionsName.setWidth(133);
		clmAssignedToDivisionsName.setResizable(true);
		clmAssignedToDivisionsName.setMoveable(true);
		
		tblbd = new YRCTableBindingData();
		colBindings = new YRCTblClmBindingData[2];
		colBindings[0] = new YRCTblClmBindingData();
		colBindings[0].setAttributeBinding("OrganizationCode");
		colBindings[0].setColumnBinding("Division");
		colBindings[0].setSortReqd(true);
		colBindings[1] = new YRCTblClmBindingData();
		colBindings[1].setAttributeBinding("OrganizationName");
		colBindings[1].setColumnBinding("Name");
		colBindings[1].setSortReqd(true);
		tblbd.setSortRequired(true);
		tblbd.setSourceBinding("AssignedDivisions:/OrganizationList/Organization");
		tblbd.setName("tblAssignedToDivisions");
		tblbd.setTblClmBindings(colBindings);
		tblAssignedToDivisions.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, tblbd);
		
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
		if(XPXConstants.DEFAULT_STOREFRONT_COMBO_CHECK.equals("Y"))
		{
			btnStorefrontCode.setSelection(true);
			//pnlDivisions.setVisible(false);
			lblStorefronCode.setVisible(true);
			comboStorefrontCode.setVisible(true);
		}
		else
		{
			btnDivision.setSelection(true);
			pnlDivisions.setVisible(true);
		//	lblStorefronCode.setVisible(false);
		//	comboStorefrontCode.setVisible(false);
			
		}
	}

	private void createPnlBody() {
		GridLayout srchGroupLayout = new GridLayout();
		GridData gdSrchGroup = new GridData();
		grpSearchFields = new Group(pnlRoot, SWT.NONE);

		gdSrchGroup.grabExcessHorizontalSpace = true;
		gdSrchGroup.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		grpSearchFields.setLayoutData(gdSrchGroup);
		grpSearchFields.setLayout(srchGroupLayout);
		srchGroupLayout.numColumns = 3;

		XPXUtils.addGradientPanelHeader(grpSearchFields, "Article_Header", true);

		// Add fields starts here
		// Article Name Field
		lblArticleName = new Label(grpSearchFields, SWT.NONE);
		lblArticleName.setData(YRCConstants.YRC_CONTROL_NAME, "lblArticleName");
		lblArticleName.setText("Article_Name");

		txtArticleName = new Text(grpSearchFields, SWT.BORDER);
		txtArticleName.setTextLimit(50);
		GridData gdArticleName = new GridData();
		gdArticleName.grabExcessHorizontalSpace = true;
		gdArticleName.horizontalSpan = 2;
		gdArticleName.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtArticleName.setLayoutData(gdArticleName);

		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = SWT.FILL;
		//	        gridData9.grabExcessHorizontalSpace = true;
		gridData9.horizontalIndent = 4;
		gridData9.verticalAlignment = 2;
		gridData9.widthHint = 250;
		
		lblArticleType = new Label(grpSearchFields, SWT.NONE);
		lblArticleType.setData(YRCConstants.YRC_CONTROL_NAME, "lblArticleType");
		lblArticleType.setLayoutData(gridData9);
		lblArticleType.setText("Article_Type");
////TODO make it a radio button
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		gridLayout2.makeColumnsEqualWidth = true;

		pnlRadioButtons = new Composite(grpSearchFields, 0);
		pnlRadioButtons.setLayout(gridLayout2);
		pnlRadioButtons.setLayoutData(gdArticleName);
		pnlRadioButtons.setData("name", "pnlRadioButtons");
		
		btnStorefrontCode = new Button(pnlRadioButtons, 16);
		btnStorefrontCode.setText("Storefront_Code");
		btnStorefrontCode.setData("name", "btnStorefrontCode");
		btnStorefrontCode.setData("yrc:customType", "Label");
		btnStorefrontCode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//pnlDivisions.setVisible(false);
				lblStorefronCode.setVisible(true);
				comboStorefrontCode.setVisible(true);					
			}

		});	
		
		btnDivision = new Button(pnlRadioButtons, 16);
		btnDivision.setText("Division");
		btnDivision.setData("name", "btnDivision");
		btnDivision.setData("yrc:customType", "Label");
		btnDivision.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				pnlDivisions.setVisible(true);
				//lblStorefronCode.setVisible(false);
				//comboStorefrontCode.setVisible(false);				
			}
		});		

		
		lblStorefronCode = new Label(grpSearchFields, SWT.NONE);
		lblStorefronCode.setText("Storefront_Code");
//		lblStorefronCode.setLayoutData(gridData9);
		lblStorefronCode.setData("name", "lblStorefronCode");
		
		comboStorefrontCode = new Combo(grpSearchFields, 8);
		comboStorefrontCode.setLayoutData(gdArticleName);
		comboStorefrontCode.setTextLimit(50);
		comboStorefrontCode.setData("name", "comboStorefrontCode");
		comboStorefrontCode.setText("xpedx");
		comboStorefrontCode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
			   			myBehavior.getDivisionList(map.get(comboStorefrontCode.getText()).toString());
			   			 
			}

		});	
		
		addArticleToDivisions();

		// Article Field
		lblArticleDesc = new Label(grpSearchFields, SWT.NONE);
		lblArticleDesc.setText("Article_Desc");

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.heightHint = 100;
		gridData.verticalAlignment = SWT.FILL;
		gridData.horizontalSpan = 2;
		txtArticleDesc = new Text(grpSearchFields, 2626);
		txtArticleDesc.setLayoutData(gridData);
		txtArticleDesc.setData("yrc:customType", "Text");
		txtArticleDesc.setData("name", "txtArticleDesc");
		txtArticleDesc.setTextLimit(2000);

		GridData gridData71 = new GridData();
		gridData71.horizontalAlignment = 4;
		gridData71.grabExcessHorizontalSpace = true;
		gridData71.verticalAlignment = 2;
		GridData gridData11 = new GridData();
		gridData11.horizontalSpan = 1;
		gridData11.widthHint = 17;
		gridData11.heightHint = 19;
		// Start Date Field
		lblStartDate = new Label(grpSearchFields, SWT.LEFT);
		lblStartDate.setText("Article_Start_Date");
		lblStartDate.setData("name", "lblStartDate");
		txtStartDate = new Text(grpSearchFields, 2048);
		txtStartDate.setLayoutData(gridData71);
		btnStartDateLookup = new Button(grpSearchFields, 0);
		btnStartDateLookup.setText("");
		btnStartDateLookup.setLayoutData(gridData11);
		btnStartDateLookup.setImage(YRCPlatformUI.getImage("DateLookup"));
		btnStartDateLookup.setCursor(new Cursor(btnStartDateLookup.getDisplay(), 21));
		btnStartDateLookup.setData("name", "btnStartDateLookup");
		btnStartDateLookup.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				YRCPlatformUI.showCalendar(txtStartDate);
			}
		});

		// End Date Field
		lblEndDate = new Label(grpSearchFields, SWT.LEFT);
		lblEndDate.setText("Article_End_Date");
		lblEndDate.setData("name", "lblEndDate");
		txtEndDate = new Text(grpSearchFields, 2048);
		txtEndDate.setLayoutData(gridData71);
		btnEndDateLookup = new Button(grpSearchFields, 0);
		btnEndDateLookup.setText("");
		btnEndDateLookup.setLayoutData(gridData11);
		btnEndDateLookup.setImage(YRCPlatformUI.getImage("DateLookup"));
		btnEndDateLookup.setCursor(new Cursor(btnEndDateLookup.getDisplay(), 21));
		btnEndDateLookup.setData("name", "btnEndDateLookup");
		btnEndDateLookup.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				YRCPlatformUI.showCalendar(txtEndDate);
			}
		});

		GridData gdOthers = new GridData();
		gdOthers.grabExcessHorizontalSpace = true;
		gdOthers.horizontalSpan = 2;
		gdOthers.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		
		lblLastModifiedData = new Label(grpSearchFields, SWT.LEFT);
		lblLastModifiedData.setText("Last_Modified_Date");
		lblLastModifiedData.setData("name", "lblLastModifiedData");
		lblLastModifiedData.setVisible(false);
		txtLastModifiedData = new Text(grpSearchFields, 72);
		txtLastModifiedData.setLayoutData(gdOthers);
		txtLastModifiedData.setVisible(false);
		
		lblModifiedBy = new Label(grpSearchFields, SWT.LEFT);
		lblModifiedBy.setText("Modified_By_User");
		lblModifiedBy.setData("name", "lblModifiedBy");
		lblModifiedBy.setVisible(false);
		txtModifiedBy = new Text(grpSearchFields, 72);
		txtModifiedBy.setLayoutData(gdOthers);
		txtModifiedBy.setVisible(false);
		
	}
	private Composite pnlDivisions; 
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
		gridLayout2.numColumns = 2;
		pnlButtonHolder = new Composite(pnlRoot, 0);
		pnlButtonHolder.setLayout(gridLayout2);
		pnlButtonHolder.setLayoutData(gridData2);
		pnlButtonHolder.setData("name", "pnlButtonHolder");
		pnlButtonHolder.setData("yrc:customType", "TaskComposite");
		
		//Create Button, name of the button will be changed dynamically to Update in case of Article getting Created Successfully
		btnCreate = new Button(pnlButtonHolder, 0);
		btnCreate.setText("Article_Create");
		btnCreate.setLayoutData(gridData5);
		btnCreate.setData("name", "btnCreate");
		btnCreate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				myBehavior.create();
			}
		});
		
		//Cancel button
		btnCancel = new Button(pnlButtonHolder, 0);
		btnCancel.setText("Article_Close");
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
	public ArticlesSearchListPanel getInvokerPage() {
		return invokerPage;
	}

	public void setPageInput(Element elePageInput) {
		this.elePageInput = elePageInput;
	}

	public Table getTblDivisions() {
		return tblDivisions;
	}

	public Table getTblAssignedToDivisions() {
		return tblAssignedToDivisions;
	}
	/**
	 * This method validates the user authorization
	 * to update the screen controls
	 */	
	private void checkUserPermissions() {
		if (!YRCPlatformUI.hasPermission(XPXConstants.RES_ID_MANAGE_NEWS)) {
			setControlsEnabled(getAllEditableControls(), false);
		}
	}
	private Control[] getAllEditableControls() {
		return new Control[] { txtArticleName, txtArticleDesc, txtStartDate,
				txtEndDate, txtLastModifiedData, txtModifiedBy,btnStartDateLookup,
				btnEndDateLookup, btnCreate, tblDivisions,
				tblAssignedToDivisions, comboStorefrontCode, btnStorefrontCode,
				btnDivision };
	}	
    private void setControlsEnabled(Control[] controls, boolean enabled) {
		for (Control control : controls) {
			if (null != control)
				control.setEnabled(enabled);
		}

	}
    
    private HashMap getCodeValue(){
      
      map.put("BulkleyDunton"  , "BDUN");
      map.put("Saalfeld", "SAAL");
      map.put("xpedxCanada","XPCA");
      map.put("xpedx","XPED");
      map.put("DEFAULT","DEFAULT");
      
      return map;
    }

}

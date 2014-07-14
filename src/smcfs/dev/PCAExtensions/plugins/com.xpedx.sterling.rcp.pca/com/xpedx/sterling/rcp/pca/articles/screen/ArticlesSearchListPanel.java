/*
 * Created on Jul 08,2010
 *
 */
package com.xpedx.sterling.rcp.pca.articles.screen;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.IYRCTableColumnTextProvider;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCTableBindingData;
import com.yantra.yfc.rcp.YRCTblClmBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;


/**
 * @author sdodda
 *
 * Articles Search and List Panel.
 */
public class ArticlesSearchListPanel extends Composite  implements IYRCComposite {

    public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.articles.screen.ArticlesSearchListPanel";
    private ArticlesSearchListPanelBehavior myBehavior;

	private Composite pnlRoot = null;
	private Group grpSearchFields = null;
	private Composite pnlButtons = null;
	private Button btnSearch = null;
	private Button btnReset = null;
	private Button btnCreate;
	private Group pnlSearchResults = null;
	private Composite pnlTitleHolder = null;
	private Label lblTitleIcon = null;
	private Composite pnlTableHolder = null;
	Table tblSearchResults = null;
	// search fields declaration starts here
	
	private Label lblArticleName = null;
	private Text txtArticleName = null;
	private Label lblDivisionCode = null;
	private Text txtDivisionCode = null;
	
//	public Element getCustomerDetails() {
//		return eleCustomerDetails;
//	}

	public ArticlesSearchListPanel(Composite parent, int style, Object inputObject) {
		super(parent, style);

		initialize();
        setBindingForComponents();
        myBehavior = new ArticlesSearchListPanelBehavior(this, FORM_ID,inputObject);
	}

	public String getFormId() {
		return FORM_ID;
	}

	public Composite getRootPanel() {
		return pnlRoot;
	}

	private void initialize() {
		createRootPanel();
		this.setLayout(new FillLayout());
		setSize(new org.eclipse.swt.graphics.Point(800,600));
	}

	private void createRootPanel() {
		pnlRoot = new Composite(this, SWT.NONE);
		createSearchFieldsGroup();
		createPnlSearchResults();
        pnlRoot.setLayout(new GridLayout());
	}

	private void createPnlSearchResults() {
		GridData pnlSearchResultsGD = new org.eclipse.swt.layout.GridData();
		GridLayout pnlSearchResultsGL = new GridLayout();
		pnlSearchResults = new Group(pnlRoot, SWT.NONE);		   
		createPnlTitleHolder();
		createPnlTableHolder();
		pnlSearchResults.setLayout(pnlSearchResultsGL);
		pnlSearchResults.setLayoutData(pnlSearchResultsGD);
		pnlSearchResultsGL.numColumns = 2;
		pnlSearchResultsGL.horizontalSpacing = 0;
		pnlSearchResultsGL.verticalSpacing = 0;
		pnlSearchResultsGL.marginWidth = 0;
		pnlSearchResultsGL.marginHeight = 0;
		pnlSearchResultsGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlSearchResultsGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlSearchResultsGD.grabExcessHorizontalSpace = true;
		pnlSearchResultsGD.grabExcessVerticalSpace = true;
	}
	
	private void createPnlTableHolder() {
		GridLayout gridLayout8 = new GridLayout();
		GridData pnlTableHolderGD = new org.eclipse.swt.layout.GridData();
		pnlTableHolder = new Composite(pnlSearchResults, SWT.NONE);		   
		createTblSearchResults();
		pnlTableHolder.setLayoutData(pnlTableHolderGD);
		pnlTableHolder.setLayout(gridLayout8);
		pnlTableHolderGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlTableHolderGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		pnlTableHolderGD.grabExcessHorizontalSpace = true;
		pnlTableHolderGD.grabExcessVerticalSpace = true;
		pnlTableHolderGD.horizontalSpan = 2;
		gridLayout8.horizontalSpacing = 0;
		gridLayout8.verticalSpacing = 0;
		gridLayout8.marginWidth = 0;
		gridLayout8.marginHeight = 0;
	}
	
	private void createTblSearchResults() {
		GridData tblSearchResultsGD = new org.eclipse.swt.layout.GridData();
		tblSearchResults = new Table(pnlTableHolder,SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tblSearchResults.setData("name", "tblSearchResults");

		TableColumn clmArticleType = new TableColumn(tblSearchResults, SWT.NONE);
		clmArticleType.setText("Article_Type");
		clmArticleType.setWidth(100);//TODO: set appropriate width		

		
		TableColumn clmArticleName = new TableColumn(tblSearchResults, SWT.NONE);
		clmArticleName.setText("Article_Name");
		clmArticleName.setWidth(150);//TODO: set appropriate width
		
		TableColumn clmCustomer = new TableColumn(tblSearchResults, SWT.NONE);
		clmCustomer.setText("Customer");
		clmCustomer.setWidth(250);
		TableColumn clmStorefrontCode = new TableColumn(tblSearchResults, SWT.NONE);
		clmStorefrontCode.setText("Storefront_Code");
		clmStorefrontCode.setWidth(150);//TODO: set appropriate width
		
		TableColumn clmDivision = new TableColumn(tblSearchResults, SWT.NONE);
		clmDivision.setText("Divisions");
		clmDivision.setWidth(150);//TODO: set appropriate width
		
		TableColumn clmStartDate = new TableColumn(tblSearchResults, SWT.NONE);
		clmStartDate.setText("Article_Start_Date");
		clmStartDate.setWidth(100);//TODO: set appropriate width
		
		TableColumn clmEndDate = new TableColumn(tblSearchResults, SWT.NONE);
		clmEndDate.setText("Article_End_Date");
		clmEndDate.setWidth(100);//TODO: set appropriate width

		TableColumn clmLastModifiedDate = new TableColumn(tblSearchResults, SWT.NONE);
		clmLastModifiedDate.setText("Last_Modified_Date");
		clmLastModifiedDate.setWidth(100);//TODO: set appropriate width

		TableColumn clmModifiedBy = new TableColumn(tblSearchResults, SWT.NONE);
		clmModifiedBy.setText("Modified_By_User");
		clmModifiedBy.setWidth(150);//TODO: set appropriate width
		
		tblSearchResultsGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblSearchResultsGD.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tblSearchResultsGD.grabExcessHorizontalSpace = true;
		tblSearchResultsGD.grabExcessVerticalSpace = true;
		tblSearchResultsGD.horizontalSpan = 2;
		tblSearchResults.setLayoutData(tblSearchResultsGD);
		tblSearchResults.setHeaderVisible(true);
		tblSearchResults.setLinesVisible(true);
		tblSearchResults.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				myBehavior.mouseDoubleClick(e, "tblSearchResults");
			}
		});
	}
	
	private void createPnlTitleHolder() {
		GridLayout pnlTitleHolderGL = new GridLayout();
		GridData pnlTitleHolderGD = new org.eclipse.swt.layout.GridData();
		pnlTitleHolder = new Composite(pnlSearchResults, SWT.NONE);		   
		lblTitleIcon = new Label(pnlTitleHolder, SWT.NONE);
		
		Image img = YRCPlatformUI.getImage("ListTitle");
		
		
		if (img != null) {
			img.setBackground(YRCPlatformUI.getBackGroundColor("Composite"));
			lblTitleIcon.setImage(img);
		}
		else {
			lblTitleIcon.setText("");
		}
		
//		lblTitle = new Label(pnlTitleHolder, SWT.NONE);
//		lblTitle.setText("Articles_List");
		
//		lblTitle.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE,"Articles_List");
		
		pnlTitleHolder.setLayoutData(pnlTitleHolderGD);
		pnlTitleHolder.setLayout(pnlTitleHolderGL);
		pnlTitleHolderGD.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
//		pnlTitleHolderGD.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		pnlTitleHolderGD.grabExcessHorizontalSpace = true;
		
		pnlTitleHolderGL.numColumns = 2;
		pnlTitleHolderGL.horizontalSpacing = 5;
		pnlTitleHolderGL.verticalSpacing = 0;
		pnlTitleHolderGL.marginWidth = 0;
		pnlTitleHolderGL.marginHeight = 0;
		
		XPXUtils.addGradientPanelHeader(pnlTitleHolder, "Articles_List", true);
	}
		
	private void createSearchFieldsGroup() {
		GridLayout srchGroupLayout = new GridLayout();
		GridData gdSrchGroup = new GridData();
		grpSearchFields = new Group(pnlRoot, SWT.NONE);
		
		gdSrchGroup.grabExcessHorizontalSpace = true;
		gdSrchGroup.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		grpSearchFields.setLayoutData(gdSrchGroup);
		grpSearchFields.setLayout(srchGroupLayout);
		srchGroupLayout.numColumns = 4;
		
		XPXUtils.addGradientPanelHeader(grpSearchFields, "Articles_Search", true);
		
		// search creation starts here
		// Article Name
		lblArticleName = new Label(grpSearchFields,SWT.NONE);
		lblArticleName.setText("Article_Name");
		
		txtArticleName = new Text(grpSearchFields,SWT.BORDER);
		GridData gdOrderName = new GridData();
		gdOrderName.grabExcessHorizontalSpace = true;
		gdOrderName.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtArticleName.setLayoutData(gdOrderName);
		
		// Article Name
		lblDivisionCode = new Label(grpSearchFields,SWT.NONE);
		lblDivisionCode.setText("Divisions");
		
		txtDivisionCode = new Text(grpSearchFields,SWT.BORDER);
		GridData gdDivisionCode = new GridData();
		gdDivisionCode.grabExcessHorizontalSpace = true;
		gdDivisionCode.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtDivisionCode.setLayoutData(gdDivisionCode);
		
		createButtonComposite();
	}

	private void createButtonComposite() {
		GridLayout btnPanelLayout = new GridLayout();
		GridData btnPanelLayoutData = new GridData();
		pnlButtons = new Composite(grpSearchFields, SWT.NONE);		   
		btnPanelLayoutData.grabExcessHorizontalSpace = true;
		btnPanelLayoutData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		btnPanelLayoutData.horizontalSpan = 4;
		pnlButtons.setLayoutData(btnPanelLayoutData);
		pnlButtons.setLayout(btnPanelLayout);
		btnPanelLayout.numColumns = 3;
		// button creation
		btnSearch = new Button(pnlButtons, SWT.NONE);
		btnSearch.setText("Article_Search");
		
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		btnSearch.setLayoutData(gridData);
		btnSearch.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.search();
				tblSearchResults.setFocus();
			}
		});
		
		btnReset = new Button(pnlButtons, SWT.NONE);
		btnReset.setText("Article_Reset");
		btnReset.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.reset();
			}
		});
		
		btnCreate = new Button(pnlButtons, SWT.NONE);
		btnCreate.setText("Article_Create_New_Article");
		btnCreate.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.create();
			}
		});
	}

	private void setBindingForComponents() {
	    setBindingForSearchCriteria();
	    setBindingForSearchResults();
    }
	
    private void setBindingForSearchCriteria() {
		
        YRCTextBindingData tbd = new YRCTextBindingData();
        tbd.setName("ArticleName");
        tbd.setTargetBinding("SearchCriteria:/XPXArticle/@ArticleName");
        tbd.setSourceBinding("SearchCriteria:/XPXArticle/@ArticleName");
        txtArticleName.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
        lblArticleName.setData(YRCConstants.YRC_CONTROL_NAME,"lblArticleName");
        
        tbd = new YRCTextBindingData();
        tbd.setName("XPXDivision");
        tbd.setTargetBinding("SearchCriteria:/XPXArticle/@XPXDivision");
        tbd.setSourceBinding("SearchCriteria:/XPXArticle/@XPXDivision");
        txtDivisionCode.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION,tbd);
        lblDivisionCode.setData(YRCConstants.YRC_CONTROL_NAME,"lblArticleName");

    }
    
    private void setBindingForSearchResults() {
        YRCTableBindingData bindingData = new YRCTableBindingData();
        YRCTblClmBindingData colBindings[] = new YRCTblClmBindingData[9];

        

        colBindings[0] = new YRCTblClmBindingData();
        colBindings[0].setAttributeBinding("ArticleType");
        colBindings[0].setColumnBinding("Article_Type");
        colBindings[0].setSortReqd(true);
        colBindings[0].setLabelProvider(new IYRCTableColumnTextProvider(){
//        	@Override
			public String getColumnText(Element obj) {
				Element eleTableItem = (Element)obj;
				if(YRCPlatformUI.equals("D", eleTableItem.getAttribute("ArticleType")))
					return "Division";
				else
				if(YRCPlatformUI.equals("C", eleTableItem.getAttribute("ArticleType")))
					return "Customer";
				else
				if(YRCPlatformUI.equals("S", eleTableItem.getAttribute("ArticleType")))
					return "Storefront";
				else
					return "";
			}
        });
        colBindings[1] = new YRCTblClmBindingData();
        colBindings[1].setAttributeBinding("ArticleName");
        colBindings[1].setColumnBinding("Article_Name");
        colBindings[1].setSortReqd(true);
       //EB-1088 show the actual Customer Name and Account number
        colBindings[2] = new YRCTblClmBindingData();
        colBindings[2].setAttributeBinding("CustomerID");
        colBindings[2].setColumnBinding("Customer");
        colBindings[2].setLabelProvider(new IYRCTableColumnTextProvider(){
        	// 	@Override
			public String getColumnText(Element obj) {
				Element eleTableItem = (Element)obj;
				if(YRCPlatformUI.equals("C", eleTableItem.getAttribute("ArticleType"))){
					String customerNameAndAcct = eleTableItem.getAttribute("CustomerID").split("-")[1];
					return myBehavior.getCustomerName(eleTableItem.getAttribute("CustomerID"))+" ("+customerNameAndAcct+")";				
				}
				else return "";				
			}
        });
        colBindings[2].setSortReqd(true);
        colBindings[3] = new YRCTblClmBindingData();
        colBindings[3].setAttributeBinding("OrganizationCode");
        colBindings[3].setColumnBinding("Storefront_Code");
        colBindings[3].setSortReqd(true);
        colBindings[4] = new YRCTblClmBindingData();
        colBindings[4].setAttributeBinding("XPXDivision");
        colBindings[4].setColumnBinding("Divisions");
        colBindings[4].setSortReqd(true);
        colBindings[5] = new YRCTblClmBindingData();
        colBindings[5].setAttributeBinding("StartDate");
        colBindings[5].setColumnBinding("Article_Start_Date");
        colBindings[5].setDataType("Date");
        colBindings[5].setSortReqd(true);   
        colBindings[6] = new YRCTblClmBindingData();
        colBindings[6].setAttributeBinding("EndDate");
        colBindings[6].setColumnBinding("Article_End_Date");
        colBindings[6].setSortReqd(true);
        colBindings[7] = new YRCTblClmBindingData();
        colBindings[7].setAttributeBinding("Modifyts");
        colBindings[7].setColumnBinding("Last_Modified_Date");
        colBindings[7].setSortReqd(true);
        colBindings[8] = new YRCTblClmBindingData();
        colBindings[8].setAttributeBinding("Modifyuserid");
        colBindings[8].setColumnBinding("Modified_By_User");
        colBindings[8].setSortReqd(true);
        
        bindingData.setSortRequired(true);
        
        bindingData.setSourceBinding("/XPXArticleList/XPXArticle");
        bindingData.setName("tblSearchResults");
        bindingData.setTblClmBindings(colBindings);
        tblSearchResults.setData(YRCConstants.YRC_TABLE_BINDING_DEFINATION, bindingData);
    }
    
   
    public IYRCPanelHolder getPanelHolder() {
        // TODO Complete getPanelHolder
        return null;
    }

	public String getHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArticlesSearchListPanelBehavior getMyBehavior() {
		return myBehavior;
	}
}
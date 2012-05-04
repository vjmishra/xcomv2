package com.xpedx.sterling.rcp.pca.tasks.myitems.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCComboBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCLabelBindingData;
import com.yantra.yfc.rcp.YRCStyledTextBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;

public class XPXAddItemPopupPanel extends Composite implements
IYRCComposite {

	private Composite pnlRoot;
	private Composite pnlProfileInfo;
	private XPXAddItemPopupPanelBehavior myBehavior;
	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.tasks.myitems.screen.XPXAddItemPopupPanel";
	private Combo comboLineType;
	private Text txtItemId;
	private Text txtItemDesc;
	private Text txtUOM;
	private Text txtQuantity;
	
	private Text txtCustLineAccount;
	private Text txtLinePO;
	private Composite pnlButtonHolder;
	private Button btnCancel;
	private Button btnAdd;
	private Element inputObject;
	

	public XPXAddItemPopupPanel(Composite parent, int style, Element inputObject, XPXMyItemsListDetailsPanel myItemListref) {
		super(parent, style);
		initialize();
		this.inputObject=inputObject;
		setBindingForComponents();
		myBehavior = new XPXAddItemPopupPanelBehavior(this, FORM_ID,inputObject,myItemListref);
		
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
		//showRootPanel(true);
		createAddItemComposite();
		createpnlButtonHolder();
		setSize(new org.eclipse.swt.graphics.Point(600, 600));
	}
	private void createAddItemComposite() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginWidth = 1;
		gridLayout1.marginHeight = 1;
		gridLayout1.numColumns=4;

		GridData gridData = new GridData();
		gridData.horizontalAlignment = 4;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = 4;
		
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 2;
		gridLayout2.verticalSpacing = 2;
		gridLayout2.numColumns = 6;

		GridData gridDataPnl2 = new GridData();
		gridDataPnl2.horizontalAlignment = 2;
		gridDataPnl2.grabExcessHorizontalSpace = false;
		gridDataPnl2.grabExcessVerticalSpace = true;
		// gridDataPnl.verticalSpan = 2;
		gridDataPnl2.widthHint = 420;
		gridDataPnl2.verticalAlignment = 4;
		
        GridData gridDatabl1 = new GridData();
        gridDatabl1.horizontalAlignment = SWT.FILL;
        gridDatabl1.grabExcessHorizontalSpace = true;
        gridDatabl1.verticalAlignment = SWT.FILL;		
        gridDatabl1.horizontalSpan=2;
        gridDatabl1.verticalSpan=1;
        gridDatabl1.horizontalIndent=38;
        
        GridData gridDatabldesc = new GridData();
        gridDatabldesc.horizontalAlignment = SWT.FILL;
        gridDatabldesc.grabExcessHorizontalSpace = true;
        gridDatabldesc.verticalAlignment = SWT.FILL;		
        gridDatabldesc.horizontalSpan=2;
        gridDatabldesc.verticalSpan=25;
        gridDatabldesc.horizontalIndent=38;
        
        
        
        
		pnlProfileInfo = new Composite(getRootPanel(), 0);
		pnlProfileInfo.setLayoutData(gridDataPnl2);
		pnlProfileInfo.setLayout(gridLayout2);
		pnlProfileInfo.setData("name", "pnlProfileInfo");

		
		comboLineType = new Combo(pnlProfileInfo, 8);
		comboLineType.setLayoutData(gridDatabl1);
		comboLineType.setData("name", "comboLineType");
		
		
		Label lblLegacyItemId = new Label(pnlProfileInfo, SWT.HORIZONTAL);
		lblLegacyItemId.setText("Item#");
		lblLegacyItemId.setLayoutData(gridDatabl1);
		lblLegacyItemId.setData("yrc:customType", "Label");
		lblLegacyItemId.setData("name", "lblLegacyItemId");
		txtItemId = new Text(pnlProfileInfo, 2048);
		txtItemId.setLayoutData(gridDatabl1);
		txtItemId.setData("name", "txtItemId");
		txtItemId.addFocusListener(new FocusAdapter(){
			String oldItemId="";
			public void focusGained(FocusEvent e) {
				 oldItemId=txtItemId.getText();
			}
			
			public void focusLost(FocusEvent evt) {
				String currentItemId=txtItemId.getText();
				if(!currentItemId.equals(oldItemId)){
					String strLineType = myBehavior.getFieldValue("comboLineType");
					if("S".equals(strLineType)||"P".equals(strLineType)){
						myBehavior.getItemInfo(currentItemId,strLineType);
	            		
	            	}
					
				}
			}
		});
		
		Label lbldummy1 = new Label(pnlProfileInfo, SWT.HORIZONTAL);
		lbldummy1.setText("");
		lbldummy1.setLayoutData(gridDatabl1);
		lbldummy1.setData("yrc:customType", "Label");
		lbldummy1.setData("name", "lbldummy1");
		

		
//		Label lbldummyUOM = new Label(pnlProfileInfo, SWT.HORIZONTAL);
//		lbldummyUOM.setText("");
//		lbldummyUOM.setLayoutData(gridDatabl1);
//		lbldummyUOM.setData("yrc:customType", "Label");
//		lbldummyUOM.setData("name", "lbldummy1");
		Label lblUOM = new Label(pnlProfileInfo, SWT.HORIZONTAL);
		lblUOM.setText("UOM");
		lblUOM.setLayoutData(gridDatabl1);
		lblUOM.setData("yrc:customType", "Label");
		lblUOM.setData("name", "lblUOM");
		txtUOM = new Text(pnlProfileInfo, 2048);
		txtUOM.setLayoutData(gridDatabl1);
		txtUOM.setData("name", "txtUOM");
		
		Label lbldummy2 = new Label(pnlProfileInfo, SWT.HORIZONTAL);
		lbldummy2.setText("");
		lbldummy2.setLayoutData(gridDatabl1);
		lbldummy2.setData("yrc:customType", "Label");
		lbldummy2.setData("name", "lbldummy1");
		
		Label lblQuantity = new Label(pnlProfileInfo, SWT.HORIZONTAL);
		lblQuantity.setText("Quantity");
		lblQuantity.setLayoutData(gridDatabl1);
		lblQuantity.setData("yrc:customType", "Label");
		lblQuantity.setData("name", "lblQuantity");
		txtQuantity = new Text(pnlProfileInfo, 2048);
//		txtQuantity.setText("0.00");
		txtQuantity.setLayoutData(gridDatabl1);
		txtQuantity.setData("name", "txtQuantity");
		
		Label lbldummy4 = new Label(pnlProfileInfo, SWT.HORIZONTAL);
		lbldummy4.setText("");
		lbldummy4.setLayoutData(gridDatabl1);
		lbldummy4.setData("yrc:customType", "Label");
		lbldummy4.setData("name", "lbldummy4");

		Label lblCustomerLineAcct = new Label(pnlProfileInfo, SWT.HORIZONTAL);
		lblCustomerLineAcct.setText("Cust Line Acc#");
		lblCustomerLineAcct.setLayoutData(gridDatabl1);
		lblCustomerLineAcct.setData("yrc:customType", "Label");
		lblCustomerLineAcct.setData("name", "lblCustomerLineFieldOne");
		txtCustLineAccount = new Text(pnlProfileInfo, 2048);
		txtCustLineAccount.setLayoutData(gridDatabl1);
		txtCustLineAccount.setData("name", "txtCustLineAccount");

		Label lbldummy5 = new Label(pnlProfileInfo, SWT.HORIZONTAL);
		lbldummy5.setText("");
		lbldummy5.setLayoutData(gridDatabl1);
		lbldummy5.setData("yrc:customType", "Label");
		lbldummy5.setData("name", "lbldummy4");

		/*Label lblCustomerLineFieldOne = new Label(pnlProfileInfo,
				SWT.HORIZONTAL);
		lblCustomerLineFieldOne.setText("Cust Line Field1#");
		lblCustomerLineFieldOne.setLayoutData(gridDatabl1);
		lblCustomerLineFieldOne.setData("yrc:customType", "Label");
		lblCustomerLineFieldOne.setData("name", "lblCustomerLineFieldOne");
		txtCustLineNoOne = new Text(pnlProfileInfo, 2048);
		txtCustLineNoOne.setLayoutData(gridDatabl1);
		txtCustLineNoOne.setData("name", "txtCustLineNoOne");*/

		/*Label lbldummy6 = new Label(pnlProfileInfo, SWT.HORIZONTAL);
		lbldummy6.setText("");
		lbldummy6.setLayoutData(gridDatabl1);
		lbldummy6.setData("yrc:customType", "Label");
		lbldummy6.setData("name", "lbldummy5");
*/
		/*Label lblCustomerLineFieldTwo = new Label(pnlProfileInfo,
				SWT.HORIZONTAL);
		lblCustomerLineFieldTwo.setText("Cust Line Field2#");
		lblCustomerLineFieldTwo.setLayoutData(gridDatabl1);
		lblCustomerLineFieldTwo.setData("yrc:customType", "Label");
		lblCustomerLineFieldTwo.setData("name", "lblCustomerLineFieldTwo");
		txtCustLineNoTwo = new Text(pnlProfileInfo, 2048);
		txtCustLineNoTwo.setLayoutData(gridDatabl1);
		txtCustLineNoTwo.setData("name", "txtCustLineNoTwo");*/

/*		Label lbldummy7 = new Label(pnlProfileInfo, SWT.HORIZONTAL);
		lbldummy7.setText("");
		lbldummy7.setLayoutData(gridDatabl1);
		lbldummy7.setData("yrc:customType", "Label");
		lbldummy7.setData("name", "lbldummy6");
*/
		Label lblLinePO = new Label(pnlProfileInfo,
				SWT.HORIZONTAL);
		lblLinePO.setText("Line PO#");
		lblLinePO.setLayoutData(gridDatabl1);
		lblLinePO.setData("yrc:customType", "Label");
		lblLinePO.setData("name", "lblCustomerLineFieldThree");
		txtLinePO = new Text(pnlProfileInfo, 2048);
		txtLinePO.setLayoutData(gridDatabl1);
		txtLinePO.setData("name", "txtLinePO");


		Label lbldummy8 = new Label(pnlProfileInfo, SWT.HORIZONTAL);
		lbldummy8.setText("");
		lbldummy8.setLayoutData(gridDatabl1);
		lbldummy8.setData("yrc:customType", "Label");
		lbldummy8.setData("name", "lbldummy3");

		Label lblItemDesc = new Label(pnlProfileInfo, SWT.HORIZONTAL);
		lblItemDesc.setText("Description");
		lblItemDesc.setLayoutData(gridDatabl1);
		lblItemDesc.setData("yrc:customType", "Label");
		lblItemDesc.setData("name", "lblItemDesc");
		txtItemDesc = new Text(pnlProfileInfo, SWT.V_SCROLL | SWT.MULTI
				| SWT.WRAP | SWT.BORDER);
		txtItemDesc.setLayoutData(gridDatabldesc);
		txtItemDesc.setData("name", "txtItemDesc");

		
//		setBindingForComponents();
		
	}
	
	
	
	private void setBindingForComponents() {
		
		YRCStyledTextBindingData stbd = null;
		YRCTextBindingData tbd = null;
		YRCComboBindingData cbd = null;
		YRCLabelBindingData lblbd = null;
		
		cbd = new YRCComboBindingData();
		cbd.setCodeBinding("@CodeValue");
		cbd.setDescriptionBinding("@CodeShortDescription");
		cbd.setListBinding("LineTypes:/CommonCodeList/CommonCode");
        cbd.setSourceBinding("LineTypes:/CommonCodeList/SelectItem/@CodeValue");
		cbd.setName("comboLineType");
		comboLineType.setData(YRCConstants.YRC_COMBO_BINDING_DEFINATION, cbd);
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
		gridLayout2.numColumns = 2;
		pnlButtonHolder = new Composite(pnlRoot, 0);
		pnlButtonHolder.setLayout(gridLayout2);
		pnlButtonHolder.setLayoutData(gridData2);
		pnlButtonHolder.setData("name", "pnlButtonHolder");
		pnlButtonHolder.setData("yrc:customType", "TaskComposite");
		
		btnAdd = new Button(pnlButtonHolder, 0);
		btnAdd.setText("Add");
		btnAdd.setLayoutData(gridData5);
		btnAdd.setData("name", "btnAdd");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					myBehavior.addItemToList();
//					getParent().getShell().close();
			}
		});
		
		// Cancel button
		btnCancel = new Button(pnlButtonHolder, 0);
		btnCancel.setText("Cancel");
		btnCancel.setLayoutData(gridData10);
		btnCancel.setData("name", "btnCancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getParent().getShell().close();
			}
		});
	}


	public String getFormId() {
			return FORM_ID;
	}

	public String getHelpId() {
			return null;
	}

	public IYRCPanelHolder getPanelHolder() {
		return null;
	}

	public Composite getRootPanel() {
		return pnlRoot;
	}
	

}

/*
 * Created on Apr 14,2010
 *
 */
package com.xpedx.sterling.rcp.pca.customerprofilerule.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXConstants;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCButtonBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCLabelBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCStyledTextBindingData;
import com.yantra.yfc.rcp.YRCTextBindingData;
import com.yantra.yfc.rcp.YRCXmlUtils;

/**
 * @author Vchandre
 * 
 * Generated by MTCE Copyright � 2005, 2006 Sterling Commerce, Inc. All Rights
 * Reserved.
 */

public class CustomerProfileRuleLinePanel extends Composite implements IYRCComposite {
	private Composite pnlRoot = null;
	private CustomerProfileRuleLineBehavior myBehavior;
	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.customerprofilerule.screen.CustomerProfileRulePanel";
	private Composite parent;
	private Object inputObject;
	private int lineNo;
	private CustomerProfileRuleLinePanel currentLinePnl;
	private Composite pnlOptions;
	private Button chkRuleSelected;
	private Composite pnlRuleDesc;
	private Composite pnlParam1;
	private Composite pnlParam2;
	private Composite pnlParam3;
	private Text txtParam1;
	private Text txtParamRule; //Added by VJ
	private Text txtParam2;
	private Text txtParam3;
//	private Label lblRuleDesc;
	private Text txtRuleKey;
	private int linePanelWidth;
	private Composite LinePnl;
	private boolean newLine = true;
	private Element eleRuleLine;
	private String ruleId;
	private boolean showParam1=false;
	private boolean showtext=false;
	private boolean enablechk=false;
	private boolean showParam2=false;
	private boolean showParam3=false;
	private StyledText stxtRuleDesc;
	private Label ruleLabel;
	private boolean labelRequired=false;
	private String strLabelValue="";
	
	private CustomerProfileRulePanel pnlRuleLines;
	
	public CustomerProfileRuleLinePanel(Composite parent, CustomerProfileRulePanel pnlOrderLines, int style, Object inObj, int pnlNo, Element eleRuleLine) {
		super(parent, style);
		
		this.parent = parent;
		this.pnlRuleLines = pnlOrderLines;
		this.inputObject = inObj;
		this.eleRuleLine = eleRuleLine;
		lineNo = pnlNo;
		ruleId = YRCXmlUtils.getAttribute(this.eleRuleLine, "RuleID");
		if("AcceptPriceOverRide".equals(ruleId)||"PriceDiscrepency".equals(ruleId)||"RequireCustomerLineField1".equals(ruleId)||"RequireCustomerLineField2".equals(ruleId)|| "RequireCustomerLineField3".equals(ruleId)||"GrossTradingMargin".equals(ruleId)){
			if("AcceptPriceOverRide".equals(ruleId) || "GrossTradingMargin".equals(ruleId)){
				enablechk=true;
				labelRequired=true;
				strLabelValue="Variance %";
			}
			if("GrossTradingMargin".equals(ruleId)){
				showParam2= true;
				strLabelValue="Variance range%";
			}
			showParam1= true;
		}
		//XB-519 showtext added for manage rule tab text field
		if("RequiredCustomerLinePO".equals(ruleId)||"RequiredCustomerLineAccountNo".equals(ruleId)){//today
			
			showtext= true;
			
		}
		
		//Element eleCustomer = this.pnlRuleLines.parentObj.getBehavior().getLocalModel("XPXCustomerIn");
		//Element extnElement = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/Extn");
		//txtCustomerLinePONumberMsg.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLinePOLbl"));
		//txtCustLineAcctMsg.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLineAccLbl"));
		
		//System.out.println("Testing Line 106 ****: "+ YRCXmlUtils.getString(eleCustomer));
		initialize();
		setBindingForComponents();
		myBehavior = new CustomerProfileRuleLineBehavior(this, FORM_ID, inputObject, eleRuleLine);
		populateCustomerLineFields(this.pnlRuleLines.parentObj);
	}
	
	private void populateCustomerLineFields(CustomerProfileMaintenance maintenance) {
		Element customerDetailsEle = maintenance.getBehavior().getCustomerDetails();
		//Added billTo XB-519
		String lineData = null ;
		if(!YRCPlatformUI.isVoid(customerDetailsEle))
		{
			if("RequireCustomerLineField1".equals(ruleId))
			{
				Element extnElement = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/Extn");
				txtParam1.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLineField1Label"));
			}
			if("RequireCustomerLineField2".equals(ruleId))
			{
				Element extnElement = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/Extn");
				txtParam1.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLineField2Label"));
			}
			if("RequireCustomerLineField3".equals(ruleId))
			{
				Element extnElement = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/Extn");
				txtParam1.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLineField3Label"));
			}
			//XB-519 Added RequiredCustomerLinePO, RequiredCustomerLineAccountNo for displaying text 
			if("RequiredCustomerLinePO".equals(ruleId))
			{
				Element extnElement = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/Extn");
				//txtParam1.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLinePOLbl"));
				//billTo = YRCXmlUtils.getAttribute(extnElement, "ExtnCustLinePOLbl");
				//billTo = XPXUtils.poLbl;
				lineData = XPXUtils.getPoLbl();
				if(lineData != null && (! "".equalsIgnoreCase(lineData))){
					txtParamRule.setText(lineData);
				}else{
					txtParamRule.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLinePOLbl"));
				}
				
				
			}
			if("RequiredCustomerLineAccountNo".equals(ruleId))
			{
				Element extnElement = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/Extn");
				//txtParam1.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLineAccLbl"));
				txtParamRule.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLineAccLbl"));
				//billTo = XPXUtils.lineAcc;
				lineData = XPXUtils.getLineAcc();
				if(lineData != null && (! "".equalsIgnoreCase(lineData))){
					txtParamRule.setText(lineData);
				}else{
					txtParamRule.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLineAccLbl"));
				}
					

			}
			Element extnEle = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/Extn");
			String suffixType=extnEle.getAttribute("ExtnSuffixType");
			if("B".equals(suffixType)){
				Element parentExtnElement = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/ParentCustomer/Extn");
				if(!YRCPlatformUI.isVoid(parentExtnElement)){
					if("RequireCustomerLineField1".equals(ruleId))
					{
						txtParam1.setText(YRCXmlUtils.getAttribute(parentExtnElement, "ExtnCustLineField1Label"));
					}
					if("RequireCustomerLineField2".equals(ruleId))
					{
						txtParam1.setText(YRCXmlUtils.getAttribute(parentExtnElement, "ExtnCustLineField2Label"));
					}
					if("RequireCustomerLineField3".equals(ruleId))
					{
						txtParam1.setText(YRCXmlUtils.getAttribute(parentExtnElement, "ExtnCustLineField3Label"));
					}	
				}
				//XB- 519 Added RequiredCustomerLinePO, RequiredCustomerLineAccountNo for displaying text 
				if("RequiredCustomerLinePO".equals(ruleId))
				{
					Element extnElement = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/ParentCustomer/Extn");
					//txtParam1.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLinePOLbl"));
					txtParamRule.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLinePOLbl"));
					txtParamRule.setEditable(false);
					//txtParam1.setEditable(false);
					//billTo = XPXUtils.poLbl;
					lineData = XPXUtils.getPoLbl();
					if(lineData != null && (! "".equalsIgnoreCase(lineData))){
						txtParamRule.setText(lineData);
					}else{
						txtParamRule.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLinePOLbl"));
					}
				}
				
				if("RequiredCustomerLineAccountNo".equals(ruleId))
				{
					Element extnElement = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/ParentCustomer/Extn");
					//txtParam1.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLineAccLbl"));
					//txtParam1.setEditable(false);
					//billTo = XPXUtils.lineAcc;
					lineData = XPXUtils.getLineAcc();
					if(lineData != null && (! "".equalsIgnoreCase(lineData))){
						txtParamRule.setText(lineData);
					}else{
						txtParamRule.setText(YRCXmlUtils.getAttribute(extnElement, "ExtnCustLineAccLbl"));
					}
				}
				// XB-519 Added below code to disable the fields at Billto Rules Tab
				setControlsEnabled(readOnlyforSAPChilds(), false);
			}
		}
	}
	private void setControlsEnabled(Control[] controls, boolean enabled) {
		for (Control control : controls) {
			if (null != control && !control.isDisposed())
				control.setEnabled(enabled);
		}
	}
	private Control[] readOnlyforSAPChilds(){
		return new Control[] {
				txtParamRule	
		}; 
	}
	private void setBindingForComponents() {
		YRCButtonBindingData chkBoxBindingData = new YRCButtonBindingData();
		chkBoxBindingData.setCheckedBinding("Y");
		chkBoxBindingData.setUnCheckedBinding("N");
		chkBoxBindingData.setSourceBinding("Source:/XPXRuleDefn/@Selected");
		chkBoxBindingData.setTargetBinding("Target:/XPXRuleDefn/@Selected");
		chkBoxBindingData.setName("chkRuleSelected");
		chkRuleSelected.setData("YRCButtonBindingDefination", chkBoxBindingData);
		
		YRCStyledTextBindingData stbd = new YRCStyledTextBindingData();
		stbd.setSourceBinding("Source:/XPXRuleDefn/@RuleLongDesc");
		stbd.setName("stxtRuleDesc");
		stxtRuleDesc.setData(YRCConstants.YRC_STYLED_TEXT_BINDING_DEFINATION, stbd);
		YRCTextBindingData tbd =null;
		if (showParam1) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("Source:/XPXRuleDefn/@Param1");
			tbd.setTargetBinding("Target:/XPXRuleDefn/@Param1");
			tbd.setName("txtParam1");
			txtParam1.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}
		if (showParam2) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("Source:/XPXRuleDefn/@Param2");
			tbd.setTargetBinding("Target:/XPXRuleDefn/@Param2");
			tbd.setName("txtParam2");
			txtParam2.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}
		if (showParam3) {
			tbd = new YRCTextBindingData();
			tbd.setSourceBinding("Source:/XPXRuleDefn/@Param3");
			tbd.setTargetBinding("Target:/XPXRuleDefn/@Param3");
			tbd.setName("txtParam3");
			txtParam3.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);
		}

		tbd = new YRCTextBindingData();
		tbd.setSourceBinding("Source:/XPXRuleDefn/@RuleKey");
		tbd.setTargetBinding("Target:/XPXRuleDefn/@RuleKey");
		tbd.setName("txtRuleKey");
		txtRuleKey.setData(YRCConstants.YRC_TEXT_BINDING_DEFINATION, tbd);

	}
	private void initialize() {
		createRootPanel();
		currentLinePnl = this;
		currentLinePnl.setLayout(new FillLayout());
//		setSize(new Point(1050, 200));
	}

	private void createRootPanel() {
		GridLayout gridDataPnl = new GridLayout();
		gridDataPnl.horizontalSpacing = 0;
		gridDataPnl.marginWidth = 0;
		gridDataPnl.marginHeight = 0;
		gridDataPnl.verticalSpacing = 0;
		pnlRoot = new Composite(this, SWT.NONE);
		pnlRoot.setData("name", "pnlRoot");
		pnlRoot.setLayout(gridDataPnl);
		createLineComposite();
	}

	private void createLineComposite() {
		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = 4;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.verticalAlignment = 0;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 5;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		LinePnl = new Composite(getRootPanel(), 0);
		LinePnl.setLayout(gridLayout);
		LinePnl.setLayoutData(gridDataPnl);
		LinePnl.setData("name", "LinePnl");

		createOptionsComposite();
		createRuleIdDescComposite();
		createParam1Composite();
		createParam2Composite();
		createParam3Composite();		
	}

	private void createOptionsComposite() {
		GridLayout gridLayoutPnl = new GridLayout();
		gridLayoutPnl.numColumns = 2;
		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = 0;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.grabExcessVerticalSpace = true;
		gridDataPnl.verticalSpan = 2;
		gridDataPnl.verticalAlignment = 4;
		
		GridData gridDataImg = new GridData();
		gridDataImg.horizontalAlignment = 2;
		gridDataImg.verticalAlignment = SWT.BEGINNING;
		gridDataImg.grabExcessVerticalSpace = false;
		gridDataImg.grabExcessHorizontalSpace = true;
		gridDataImg.widthHint = 50;
		pnlOptions = new Composite(LinePnl, 0);
		pnlOptions.setLayoutData(gridDataPnl);
		pnlOptions.setLayout(gridLayoutPnl);
		pnlOptions.setData("name", "pnlOptions");
		
		txtRuleKey = new Text(pnlOptions, 2048);
		txtRuleKey.setText("");
		txtRuleKey.setLayoutData(gridDataImg);
		txtRuleKey.setData("name", "txtRuleKey");
		txtRuleKey.setVisible(false);
		
		
		chkRuleSelected = new Button(pnlOptions, SWT.CHECK);
		chkRuleSelected.setText("");
		chkRuleSelected.setVisible(true);
		//|| isIntegAdmin()
		if(	"RequireCustomerPO".equals(ruleId) )
		{
			chkRuleSelected.setEnabled(false);
		}
		chkRuleSelected.setData("name", "chkRuleSelected");
		chkRuleSelected.setData("yrc:customType", "Label");
		chkRuleSelected.setLayoutData(gridDataImg);
		
	}


	private void createRuleIdDescComposite() {

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.numColumns = 1;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 5;

		GridData gridDataVal = new GridData();
		gridDataVal.horizontalAlignment = SWT.BEGINNING;
		gridDataVal.verticalAlignment = SWT.CENTER;
		gridDataVal.grabExcessVerticalSpace = false;
		gridDataVal.grabExcessHorizontalSpace = true;
		gridDataVal.horizontalSpan = 1;
		gridDataVal.verticalSpan = 1;

		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = SWT.BEGINNING;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.grabExcessVerticalSpace = true;
		// gridDataPnl.verticalSpan = 2;
		gridDataPnl.widthHint = 500;
		gridDataPnl.verticalAlignment = 4;
		pnlRuleDesc = new Composite(LinePnl, 0);
		pnlRuleDesc.setLayout(gridLayout1);
		pnlRuleDesc.setLayoutData(gridDataPnl);
		pnlRuleDesc.setData("name", "pnlRuleDesc");
		
		//Rule Description
//		lblRuleDesc = new Label(pnlRuleDesc, SWT.HORIZONTAL);
////		lblLineType.setText("");
//		lblRuleDesc.setLayoutData(gridDataVal);
//		lblRuleDesc.setData("yrc:customType", "Label");
//		lblRuleDesc.setData("name", "lblLineType");
		stxtRuleDesc = new StyledText(pnlRuleDesc, SWT.SIMPLE);
		stxtRuleDesc.setText("");
		stxtRuleDesc.setLayoutData(gridDataVal);
		stxtRuleDesc.setEditable(false);
		stxtRuleDesc.setData("name", "stxtRuleDesc");
		stxtRuleDesc.setWordWrap(false);
	}
	private void createParam1Composite() {

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.verticalSpacing = 0;
		//gridLayout1.numColumns = 1;
		gridLayout1.numColumns = 2;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 5;
		
		//XB-519 Modified for UI
		/*GridData gridDataVal = new GridData();
		gridDataVal.horizontalAlignment = SWT.BEGINNING;
		gridDataVal.verticalAlignment = 2;
		//gridDataVal.grabExcessVerticalSpace = false;
		gridDataVal.grabExcessHorizontalSpace = true;
		gridDataVal.horizontalSpan = 3;
		//gridDataVal.verticalSpan = 1;
		gridDataVal.widthHint = 300;*/
        GridData gridDataVal = new GridData();
        gridDataVal.horizontalAlignment = SWT.CENTER;
        gridDataVal.verticalAlignment = 1;
        gridDataVal.grabExcessVerticalSpace = false;
        gridDataVal.grabExcessHorizontalSpace = true;
        gridDataVal.horizontalSpan = 1;
        gridDataVal.verticalSpan = 1;
        gridDataVal.widthHint = 300; // this changes the text field width


		
		GridData gridDataVal2 = new GridData();
		gridDataVal2.horizontalAlignment = SWT.CENTER;
		gridDataVal2.verticalAlignment = SWT.CENTER;
		gridDataVal2.grabExcessVerticalSpace = false;
		gridDataVal2.grabExcessHorizontalSpace = true;
		gridDataVal2.horizontalSpan = 1;
		gridDataVal2.verticalSpan = 1;
		gridDataVal2.widthHint = 120;

		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = SWT.BEGINNING;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.grabExcessVerticalSpace = true;
		// gridDataPnl.verticalSpan = 2;
		//gridDataPnl.widthHint = 100;
		gridDataPnl.widthHint = 160;
		gridDataPnl.verticalAlignment = 4;
		pnlParam1 = new Composite(LinePnl, 0);
		pnlParam1.setLayout(gridLayout1);
		pnlParam1.setLayoutData(gridDataPnl);
		pnlParam1.setData("name", "pnlParam1");
		

		
		ruleLabel = new Label(pnlParam1,SWT.LEFT);
		ruleLabel.setText(strLabelValue);
		ruleLabel.setLayoutData(gridDataVal2);
		ruleLabel.setData("name","ruleLabel");
		if(labelRequired){
			ruleLabel.setVisible(true);
		}
		else{
			ruleLabel.setVisible(false);
		}

		txtParam1 = new Text(pnlParam1, 2048|SWT.LEFT);
		txtParam1.setLayoutData(gridDataVal);
		txtParam1.setText("");
		txtParam1.setData("name", "txtParam1");
		if(showParam1){
			txtParam1.setVisible(true);
		}else{
			txtParam1.setVisible(false);
		}
		if(enablechk){
			txtParam1.setEditable(true);
		}
		else{
			txtParam1.setEditable(false);
		}
//		if(isIntegAdmin()){
//			txtParam1.setEditable(false);
//		}
	}
	private void createParam2Composite() {

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.numColumns = 1;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 5;

		GridData gridDataVal = new GridData();
		gridDataVal.horizontalAlignment = SWT.CENTER;
		gridDataVal.verticalAlignment = SWT.CENTER;
		gridDataVal.grabExcessVerticalSpace = false;
		gridDataVal.grabExcessHorizontalSpace = true;
		gridDataVal.horizontalSpan = 1;
		gridDataVal.verticalSpan = 1;
		gridDataVal.widthHint = 60;

		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = SWT.BEGINNING;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.grabExcessVerticalSpace = true;
		// gridDataPnl.verticalSpan = 2;
		gridDataPnl.widthHint = 100;
		gridDataPnl.verticalAlignment = 4;
		pnlParam2 = new Composite(LinePnl, 0);
		pnlParam2.setLayout(gridLayout1);
		pnlParam2.setLayoutData(gridDataPnl);
		pnlParam2.setData("name", "pnlParam2");

		txtParam2 = new Text(pnlParam2, 2048);
		txtParam2.setText("");
		txtParam2.setLayoutData(gridDataVal);
		txtParam2.setData("name", "txtParam2");
		if(showParam2){
			txtParam2.setVisible(true);
		}else{
			txtParam2.setVisible(false);
		}

	}
	private void createParam3Composite() {

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.numColumns = 1;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 5;

		//XB-519 Modified For UI Kubra
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = SWT.BEGINNING;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = 2;
		gridData3.widthHint = 300;
		gridData3.horizontalSpan=3;
		
		//XB-519 - TextField UI On Rules TAB
		GridData gridDataVal = new GridData();
		gridDataVal.horizontalAlignment = SWT.BEGINNING;
		gridDataVal.verticalAlignment = SWT.CENTER;
		gridDataVal.grabExcessVerticalSpace = false;
		gridDataVal.grabExcessHorizontalSpace = false;
		gridDataVal.horizontalSpan = 1;
		gridDataVal.verticalSpan = 1;
		gridDataVal.widthHint = 60;

		GridData gridDataPnl = new GridData();
		gridDataPnl.horizontalAlignment = SWT.BEGINNING;
		gridDataPnl.grabExcessHorizontalSpace = true;
		gridDataPnl.grabExcessVerticalSpace = true;
		// gridDataPnl.verticalSpan = 2;
		gridDataPnl.widthHint = 100;
		gridDataPnl.verticalAlignment = 4;
		pnlParam3 = new Composite(LinePnl, 0);
		pnlParam3.setLayout(gridLayout1);
		pnlParam3.setLayoutData(gridDataPnl);
		pnlParam3.setData("name", "pnlParam3");
//		XPXUtils.paintPanel(pnlParam3);
		
		txtParam3 = new Text(pnlParam3, 2048);
		txtParam3.setLayoutData(gridDataVal);
		txtParam3.setText("");
		txtParam3.setData("name", "txtParam3");
		if(showParam3){
			txtParam3.setVisible(true);
		}else{
			txtParam3.setVisible(false);
		}
		
		//XB-519 VJ Changes
		txtParamRule = new Text(pnlParam1, 2048);
		txtParamRule.setLayoutData(gridData3);
		txtParamRule.setTextLimit(22);
		txtParamRule.setData("name", "txtParamRule");
		
		if(showtext){
			txtParamRule.setVisible(true);
			txtParamRule.setEditable(true);
			
			
		}else{
			txtParamRule.setVisible(false);
			txtParamRule.setEditable(false);
			
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

	public void setCompositeWidths() {
		int newTotalWidth = 0;
		int parentWidth = parent.getSize().x;
		int controlsExcluded = 0;
		int controlsAddedByExtensibility = 0;
		Control al[] = LinePnl.getChildren();
		for (int i = 0; i < al.length; i++) {
			if (!((String) al[i].getData("name")).startsWith("extn"))
				continue;
			// if (!(al[i] instanceof Group) && (al[i] instanceof Composite))
			// YCDScreenPanelUtils.addPanelBorder((Composite) al[i]);
			controlsAddedByExtensibility++;
		}

		for (int i = 0; i < al.length; i++)
			if (!((GridData) al[i].getLayoutData()).exclude)
				newTotalWidth += ((GridData) al[i].getLayoutData()).widthHint;
			else
				controlsExcluded++;

		if (controlsExcluded == 0 && controlsAddedByExtensibility == 0)
			return;
		newTotalWidth -= 40;
		int consSubtract = 0;
		if (controlsExcluded == 1)
			consSubtract = 125;
		else if (controlsExcluded == 2)
			consSubtract = 130;
		else if (controlsExcluded == 3)
			consSubtract = 140;
		else if (controlsExcluded == 4)
			consSubtract = 155;
		GridData gridData0 = null;
		for (int i = 0; i < al.length; i++) {
			gridData0 = (GridData) al[i].getLayoutData();
			if (gridData0.exclude || gridData0.widthHint == -1)
				continue;
			int pnlWidth = gridData0.widthHint;
			pnlWidth = (pnlWidth * (parentWidth - consSubtract)) / newTotalWidth;
			if (((String) al[i].getData("name")).startsWith("extn")) {
				GridData gd = (GridData) al[i].getLayoutData();
				gridData0 = new GridData();
				gridData0.horizontalAlignment = gd.horizontalAlignment;
				gridData0.grabExcessHorizontalSpace = gd.grabExcessHorizontalSpace;
				gridData0.verticalAlignment = gd.verticalAlignment;
				gridData0.widthHint = pnlWidth;
				gridData0.grabExcessVerticalSpace = gd.grabExcessVerticalSpace;
				al[i].setLayoutData(gridData0);
			} else {
				gridData0.widthHint = pnlWidth;
			}
		}

		linePanelWidth = LinePnl.getSize().x;
		pnlRoot.layout(true, true);
	}

	public boolean isNewLine() {
		return newLine;
	}

	public CustomerProfileRuleLineBehavior getBehavior() {
		return myBehavior;
	}
	public String getRuleId(){
		return ruleId;
	}
	public boolean isIntegAdmin(){
		if(YRCPlatformUI.hasPermission(XPXConstants.RES_ID_MANAGE_CUSTOMER_OR_USER_INTEG_ATTR))
		    return true;
		else
			return false;
		//pnlRuleLines.parentObj.getBehavior().getLocalModel("UserNameSpace")
	}
	

	public CustomerProfileRulePanel getPnlRuleLines(){
		return pnlRuleLines;
	}



	public boolean isCustomerFieldChecked(String ruleId) {
		Element customerDetailsEle = this.pnlRuleLines.parentObj.getBehavior().getCustomerDetails();
		String custLineField="";
		String billto;
		if(!YRCPlatformUI.isVoid(customerDetailsEle))
		{
			Element extnEle = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/Extn");
			String suffixType=extnEle.getAttribute("ExtnSuffixType");
			if("B".equals(suffixType)){
				Element parentExtnElement = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/ParentCustomer/Extn");
				if(!YRCPlatformUI.isVoid(parentExtnElement)){
					if("RequireCustomerLineField1".equals(ruleId))
					{
						custLineField=YRCXmlUtils.getAttribute(parentExtnElement, "ExtnCustLineField1Flag");
					}
					if("RequireCustomerLineField2".equals(ruleId)){
						custLineField=YRCXmlUtils.getAttribute(parentExtnElement, "ExtnCustLineField2Flag");
					}
					if("RequireCustomerLineField3".equals(ruleId)){
						custLineField=YRCXmlUtils.getAttribute(parentExtnElement, "ExtnCustLineField3Flag");
					}
					if("RequiredCustomerLinePO".equals(ruleId))
					{
						custLineField=YRCXmlUtils.getAttribute(parentExtnElement, "ExtnCustLinePONoFlag");
					}
					
				}
			}	
						
			if("C".equals(suffixType)){
				Element extnElement = YRCXmlUtils.getXPathElement(customerDetailsEle,"/CustomerList/Customer/Extn");
				if("RequireCustomerLineField1".equals(ruleId))
				{
					custLineField=YRCXmlUtils.getAttribute(extnElement, "ExtnCustLineField1Flag");
				}
				if("RequireCustomerLineField2".equals(ruleId)){
					custLineField=YRCXmlUtils.getAttribute(extnElement, "ExtnCustLineField2Flag");
				}
				if("RequireCustomerLineField3".equals(ruleId)){
					custLineField=YRCXmlUtils.getAttribute(extnElement, "ExtnCustLineField3Flag");
				}
				if("RequiredCustomerLinePO".equals(ruleId))
				{
					custLineField=YRCXmlUtils.getAttribute(extnElement, "ExtnCustLinePONoFlag");
				}
			}

			if("Y".equals(custLineField))
				return true;
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
}

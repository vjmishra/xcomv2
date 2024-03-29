/*
 * Created on Apr 14,2010
 *
 */
package com.xpedx.sterling.rcp.pca.customerprofilerule.screen;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCButtonBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCScrolledCompositeListener;
import com.yantra.yfc.rcp.YRCTextBindingData;
import com.yantra.yfc.rcp.YRCWizard;
import com.yantra.yfc.rcp.YRCWizardBehavior;
import com.yantra.yfc.rcp.YRCXmlUtils;

/**
 * @author sdodda
 * 
 * Generated by MTCE Copyright � 2005, 2006 Sterling Commerce, Inc. All Rights
 * Reserved.
 */

public class CustomerProfileRulePanel extends Composite implements IYRCComposite {


		private Composite pnlRoot = null;
		private CustomerProfileRulePanelBehavior myBehavior;
		private YRCWizardBehavior wizBehavior;
		public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.customerprofilerule.screen.CustomerProfileRulePanel"; // @jve:decl-index=0:
		private Composite pnlLines;
		private ScrolledComposite scrolledPnlforLines;
		private Composite compositeHolder;
		private Composite pnlDynamicLineParent;
		private Composite MiscPnl;
		public CustomerProfileMaintenance parentObj=null;
		String extSuffixType="";

		private YRCScrolledCompositeListener listener;

		private Button btnUpdateRules;
		private int pnlNo;
		private String billTo;
		private String lineAcc;
		public List<String> errorMessageList=new ArrayList<String>();
		public CustomerProfileRulePanel(Composite parent, int style, Object inputObject, CustomerProfileMaintenance parentObj) {
			super(parent, style);
			this.parentObj=parentObj;
			pnlNo = 1;
			this.extSuffixType=parentObj.extSuffixType;
			initialize();
			setBindingForComponents();
			myBehavior = new CustomerProfileRulePanelBehavior(this,inputObject);

		}

		private void initialize() {
			
//			this.setLayout(new FillLayout());
			GridLayout thislayout = new GridLayout(1, true);
	        thislayout.marginHeight = 0;
	        thislayout.marginWidth = 0;
	        setLayout(thislayout);
	        createRootPanel();
			//setSize(new org.eclipse.swt.graphics.Point(1060,780));
		}

		private void setBindingForComponents() {
			YRCButtonBindingData bbd = new YRCButtonBindingData();
			bbd.setName("btnUpdateRules");
			bbd.setActionHandlerEnabled(true);
			bbd.setActionId("com.xpedx.sterling.rcp.pca.customerprofilerule.action.XPXUpdateCustomerProfileRuleAction");
			btnUpdateRules.setData(YRCConstants.YRC_BUTTON_BINDING_DEFINATION, bbd);
			
		}
		public String getFormId() {
			return FORM_ID;
		}
		public Composite getRootPanel() {
			return this;
		}
		private void createRootPanel() {
			/*GridLayout gridLayout5 = new GridLayout();
			gridLayout5.verticalSpacing = 2;
			gridLayout5.marginHeight = 0;
			gridLayout5.marginWidth = 0;
			
			GridData gridData9 = new GridData();
			gridData9.horizontalAlignment = 4;
			gridData9.grabExcessHorizontalSpace = true;
			gridData9.verticalAlignment = 2;
			
			pnlRoot = new Composite(this, SWT.NONE);
			pnlRoot.setData("yrc:customType", "TaskComposite");
			pnlRoot.setData("name", "parent");
			pnlRoot.setLayout(gridLayout5);
			pnlRoot.setLayoutData(gridData9);
			XPXUtils.paintPanel(pnlRoot);
			showRootPanel(true);*/
			setData("name", "this");
	        GridLayout thislayout = new GridLayout(1, true);
	        thislayout.marginHeight = 0;
	        thislayout.marginWidth = 0;
	        setLayout(thislayout);
			createComposite();
			createCompositeHolder();
		}

		private void createCompositeHolder() {
			GridLayout gridLayout6 = new GridLayout();
			gridLayout6.marginWidth = 0;
			gridLayout6.marginHeight = 0;
			GridData gridData9 = new GridData();
			gridData9.horizontalAlignment = 4;
			gridData9.grabExcessHorizontalSpace = true;
			gridData9.verticalAlignment = 2;
			compositeHolder = new Composite(getRootPanel(), 0);
			compositeHolder.setLayoutData(gridData9);
			compositeHolder.setLayout(gridLayout6);
			compositeHolder.setData("yrc:customType", "TaskComposite");
			compositeHolder.setData("name", "compositeHolder");
			createMiscPnl();
		}

		private void createMiscPnl() {
			GridData gridData6 = new GridData();
			gridData6.horizontalAlignment = 1;
			gridData6.grabExcessHorizontalSpace = true;
			gridData6.horizontalSpan = 1;
			gridData6.horizontalIndent = 0;
			gridData6.verticalAlignment = 2;
			GridData gridData16 = new GridData();
			gridData16.horizontalAlignment = 3;
			gridData16.grabExcessHorizontalSpace = true;
			gridData16.horizontalSpan = 1;
			gridData16.horizontalIndent = 0;
			gridData16.verticalAlignment = 2;
			GridLayout gridLayout4 = new GridLayout();
			gridLayout4.numColumns = 7;
			gridLayout4.horizontalSpacing = 2;
			gridLayout4.marginWidth = 5;
			gridLayout4.marginHeight = 1;
			gridLayout4.makeColumnsEqualWidth = false;
			GridData gridData5 = new GridData();
			gridData5.grabExcessHorizontalSpace = true;
			gridData5.verticalAlignment = 4;
			gridData5.horizontalAlignment = 4;

			MiscPnl = new Composite(compositeHolder, 0);
			MiscPnl.setLayoutData(gridData5);
			MiscPnl.setLayout(gridLayout4);
			MiscPnl.setData("name", "MiscPnl");
			
			btnUpdateRules = new Button(MiscPnl, 0);
			btnUpdateRules.setText("Update Rules");
			btnUpdateRules.setLayoutData(gridData16);
			btnUpdateRules.setData("name", "btnUpdateRules");
			
			btnUpdateRules.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					YRCPlatformUI.fireAction("com.xpedx.sterling.rcp.pca.customerprofilerule.action.XPXUpdateCustomerProfileRuleAction");					
				}
			});
			
		}

		private void createComposite() {
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.marginWidth = 1;
			gridLayout1.marginHeight = 1;
			GridData gridData = new GridData();
			gridData.horizontalAlignment = 4;
			gridData.grabExcessHorizontalSpace = false;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = 4;
			pnlLines = new Composite(getRootPanel(), 0);
			pnlLines.setLayoutData(gridData);
			pnlLines.setLayout(gridLayout1);
			pnlLines.setData("name", "composite");
			createScrolledComposite();
		}

		private void createScrolledComposite() {
			GridData gridData1 = new GridData();
			gridData1.horizontalAlignment = 4;
			gridData1.grabExcessHorizontalSpace = true;
			gridData1.grabExcessVerticalSpace = true;
			gridData1.verticalAlignment = 4;
			scrolledPnlforLines = new ScrolledComposite(pnlLines, 512);
			scrolledPnlforLines.setLayoutData(gridData1);
			scrolledPnlforLines.setExpandHorizontal(true);
			scrolledPnlforLines.setExpandVertical(true);
			scrolledPnlforLines.setAlwaysShowScrollBars(false);
			listener = new YRCScrolledCompositeListener(scrolledPnlforLines);
			// listener = new YCDCompositeListener(scrolledPnlforLines);
			createComposite1();
			scrolledPnlforLines.setLayout(new GridLayout());
			scrolledPnlforLines.setContent(pnlDynamicLineParent);
			scrolledPnlforLines.setData("yrc:customType", "TaskComposite");
			scrolledPnlforLines.setData("name", "scrolledComposite");
		}

		private void createComposite1() {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			gridLayout.verticalSpacing = 1;
			gridLayout.marginWidth = 0;
			gridLayout.marginHeight = 0;
			gridLayout.horizontalSpacing = 5;
			GridData gridData0 = new GridData();
			gridData0.horizontalAlignment = 4;
			gridData0.grabExcessVerticalSpace = true;
			gridData0.grabExcessHorizontalSpace = true;
			gridData0.verticalAlignment = 4;
			pnlDynamicLineParent = new Composite(scrolledPnlforLines, 0);
			pnlDynamicLineParent.setLayout(gridLayout);
			pnlDynamicLineParent.setData("name", "pnlDynamicPnlParent");
			pnlDynamicLineParent.setLayoutData(gridData0);
			pnlDynamicLineParent.setData("yrc:customType", "TaskComposite");
			XPXUtils.paintPanel(pnlDynamicLineParent);

		}
		public void createLines(ArrayList listRuleLine, String header) {
			addPanel(header);
			for (int i = 0; i < listRuleLine.size(); i++)
				createLine(i, (Element) listRuleLine.get(i));
			adjustScrollPnl(scrolledPnlforLines, pnlDynamicLineParent, pnlLines, false, true);

		}
		private void addPanel(String header){
			GridLayout gridLayout8 = new GridLayout();
			gridLayout8.marginHeight = 0;
			gridLayout8.marginWidth = 0;
			GridData gridData0 = new GridData();
			gridData0.horizontalAlignment = 0;
			gridData0.grabExcessVerticalSpace = false;
			gridData0.grabExcessHorizontalSpace = true;
			gridData0.verticalAlignment = 0;
			Composite compositeHeaderHolder = new Composite(pnlDynamicLineParent, 0);
			compositeHeaderHolder.setLayoutData(gridData0);
			compositeHeaderHolder.setData("name", "compositeHeaderHolder");
			compositeHeaderHolder.setLayout(gridLayout8);
			compositeHeaderHolder.addListener(26, listener);
			GridData gridData16 = new GridData();
			gridData16.widthHint = 1050;
			gridData16.heightHint = 20;
			gridData16.horizontalAlignment = SWT.BEGINNING;
			gridData16.verticalAlignment = 2;
			gridData16.horizontalIndent = 0;
			gridData16.grabExcessHorizontalSpace = false;
			Label labelHeaderItem = new Label(compositeHeaderHolder, SWT.HORIZONTAL);
			labelHeaderItem.setText(header);
			labelHeaderItem.setLayoutData(gridData16);
			labelHeaderItem.setBackgroundImage(YRCPlatformUI.getImage("PanelHeaderImage"));
		}
		public void adjustScrollPnl(ScrolledComposite scrPnl, Composite scrChild, Composite scrParent, boolean isHScrollReqd, boolean isVScrollReqd) {
			Control childIterator[] = scrChild.getChildren();
			int noOfChildren = childIterator.length;
			int HEIGHT = 5;
			int WIDTH = 5;
			int selectedHeight = 0;
			int selectedPanelHeight = 0;
			for (int k = 0; k < noOfChildren; k++) {
				int boundHeight = childIterator[k].getBounds().height;
				int boundWidth = childIterator[k].getBounds().width;
				if (isVScrollReqd) {

					HEIGHT += boundHeight + 5;
					if (WIDTH < boundWidth)
						WIDTH = boundWidth;
				}
				if (!isHScrollReqd)
					continue;
				WIDTH += boundWidth + 5;
				if (HEIGHT < boundHeight)
					HEIGHT = boundHeight;
			}

			scrPnl.setMinSize(WIDTH, HEIGHT);
			if (isVScrollReqd && (selectedHeight < scrPnl.getOrigin().y || selectedHeight + selectedPanelHeight > scrPnl.getSize().y + scrPnl.getOrigin().y))
				scrPnl.setOrigin(0, selectedHeight);
			scrParent.layout(true, true);
		}

		private CustomerProfileRuleLinePanel createLine(int i, Element eleOrderLine) {
			Composite pnlParam1;
			Text txtCustLineAcctMsg;
			GridData gridData0 = new GridData();
			Composite pnlCustomerProfileInfo = null;
			gridData0.horizontalAlignment = 0;
			gridData0.grabExcessVerticalSpace = false;
			gridData0.grabExcessHorizontalSpace = true;
			gridData0.verticalAlignment = 0;
			gridData0.widthHint = 1050;
			Object inObject = getInput();
			eleOrderLine = YRCXmlUtils.getCopy(eleOrderLine);
			CustomerProfileRuleLinePanel repeatingLinePnl1 = new CustomerProfileRuleLinePanel(pnlDynamicLineParent, this, SWT.NONE, inObject, pnlNo, eleOrderLine);
			//OrderLinePanel repeatingLinePnl1 = null;
			pnlNo++;
			repeatingLinePnl1.setData("name", "repeatingLinePnl");
			repeatingLinePnl1.setLayoutData(gridData0);
			repeatingLinePnl1.addListener(26, listener);
			// repeatingLinePnl.addListener(26, listener);
			repeatingLinePnl1.setCompositeWidths();
//			repeatingLinePnlAry[i] = repeatingLinePnl1;
			// myBehavior.addAndBindNewControl(repeatingLinePnl1);
			return repeatingLinePnl1;
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
		public void getTargetModelAndCallUpdateApi(boolean isRefreshReqd) {
			String linePO = "";
			String lineAcc = "";
			myBehavior.createRuleXML();
			Control childIterator[] = pnlDynamicLineParent.getChildren();
			int noOfChildren = childIterator.length;
			for (int k = 0; k < noOfChildren; k++) {
				if(! (childIterator[k] instanceof CustomerProfileRuleLinePanel)){
					continue;
				}
				CustomerProfileRuleLinePanel childpnl = (CustomerProfileRuleLinePanel) childIterator[k];
				Element ruleLineElem = childpnl.getBehavior().getTargetModelforParent();
				if(noOfChildren==7 && k==5 || noOfChildren==5 && k==3)
				{
					XPXUtils.setLineAcc(childpnl.getBehavior().getFieldValue("txtParamRule"));
					lineAcc = childpnl.getBehavior().getFieldValue("txtParamRule");
					if(lineAcc != null && lineAcc != ""){
						XPXUtils.setLineAcc(lineAcc);
					}
					
				}
				else if(noOfChildren==7 && k==6 || noOfChildren==5 && k==4)
				{
					linePO = childpnl.getBehavior().getFieldValue("txtParamRule");
					if(linePO != null && linePO != ""){
					XPXUtils.setPoLbl(linePO);
					}
					
				}
				myBehavior.setUpdatedValues(ruleLineElem,lineAcc,linePO);
				if(ruleLineElem != null){
					if(childpnl.getBehavior().validateForErrors()){
						return;
					}
					myBehavior.appendRuleLine(ruleLineElem);
				}
				
			}
			
			if(errorMessageList.size()>0){
				String errorMessageGTM="";
				String errorMessage="";
				String errrorValueFinal="";
				if(errorMessageList.contains("Gross Trade Margin")){
					errorMessageGTM="Gross Trade Margin";
					errorMessageList.remove("Gross Trade Margin");
				}
				for(int i=0;i<errorMessageList.size();i++){
					errorMessage=errorMessage.concat(errorMessageList.get(i)+",");
				}

							
					//String errorMsg="Please provide Values/Flags for "+errorMessage+" in  Corporate Info at Customer Level.";
					String errorGTM="Please provide Values for Gross Trading Margin. ";
					if(YRCPlatformUI.isVoid(errorMessageGTM)){
					
						//errrorValueFinal=errorMsg;
					}
					else if(YRCPlatformUI.isVoid(errorMessage)){
						errrorValueFinal=errorGTM;
					}
					else
						errrorValueFinal=errorGTM;
					//Added for XB-120
					if(!YRCPlatformUI.isVoid(errrorValueFinal)){
						YRCPlatformUI.showError("Error",errrorValueFinal);
						}
						else{
							myBehavior.callUpdateApi();
						}
					
					errorMessageList.clear();
			}
			else{
			myBehavior.callUpdateApi();
			}
		}
		
		public CustomerProfileRulePanelBehavior getPageBehavior() {
			return myBehavior;
		}
		
		public void clearLines() {
			Control childIterator[] = pnlDynamicLineParent.getChildren();
			int noOfChildren = childIterator.length;
			for (int k = 0; k < noOfChildren; k++) {
				if((childIterator[k] instanceof CustomerProfileRuleLinePanel)){
					CustomerProfileRuleLinePanel childpnl = (CustomerProfileRuleLinePanel) childIterator[k];
					childpnl.dispose();
				} else {
					Composite childpnl = (Composite) childIterator[k];
					childpnl.dispose();
				}
				
			}
			//System.out.println("Clear Lines.");
		}	
}

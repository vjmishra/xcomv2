package com.xpedx.sterling.rcp.pca.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCLabelBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;

public class XPXCollapsibleCompositeHelper {

	private Label lblTitle=null, lblImage=null;
	private Composite cmpBody=null, cmpHeader=null, cmpHeaderSpace=null, collapsibleComposite=null;//, cmpRoot=null;
	private String id="";
	private String title="";
	private ArrayList listeners;
    private final String EXPAND="expand";
    private final String COLLAPSE="collapse";
	private boolean autoRefresh=true;
	private boolean adjustGrabExcessOnExpandCollapse=false;
	
    private MouseListener expandOrCollapse = new MouseListener(){
		public void mouseDoubleClick(MouseEvent e) {}
		public void mouseDown(MouseEvent e) {
			if(cmpBody.isVisible()){
				collapsePanel();
				fireListener(COLLAPSE, true);
			}else{
				expandPanel();
				fireListener(EXPAND, true);
			}
				
		}
		public void mouseUp(MouseEvent e) {}
	};

	private PaintListener paintListener = new PaintListener(){
		public void paintControl(PaintEvent e) {
			XPXUtils.paintControl(e);
		}
	};
	private ControlListener listener;
	private boolean overrideLayout;
    
	/**
	 * This method adds a standard collapsible header to the composite passed. A new composite will be added between the composite passed and its parent.   This composite will be a YRCCollapsibleComposite.
	 * NOTE: This method will create a composite as the parent of the panel passed as the form. The helper to this form will be returned
	 * @param title <code> String </code> the title which will be displayed in the header of the collapsible panel
	 * @param form <code> Composite </code> the form composite that will be expanded and collapsed.
	 * @return A handle to the helper for the collapsible form created.
	 */
	public static XPXCollapsibleCompositeHelper createCollapsibleHeader(String title, Composite form){
		return createCollapsibleHeader(title, form, "");
	}

	/**
	 * This method adds a standard collapsible header to the composite passed and gives it a unique id so that it can be identified.
	 * NOTE: This method will create a composite as the parent of the panel passed as the form. The helper to this form will be returned
	 * @param title <code> String </code> the title which will be displayed in the header of the collapsible panel
	 * @param form <code> Composite </code> the form composite that will be expanded and collapsed.
	 * @param id <code> String </code> the unique id for this collapsible composite. This will also be appended to all of the objects created to ensure that they all have unique control names.
	 * @return A handle to the helper for the collapsible form created.
	 */
	public static XPXCollapsibleCompositeHelper createCollapsibleHeader(String title, Composite form, String id){
		return createCollapsibleHeader(title, form, id, null);
	}
	
	/**
	 * This method adds an advanced collapsible header to the composite passed.  It is advanced because you can also provide a form which will appear in the header. This allows you to put checkboxes, radio buttons, or additional text in the header of the collapsible panel
	 * NOTE: This method will create a composite as the parent of the panel passed as the form. The helper to this form will be returned
	 * @param title <code> String </code> the title which will be displayed in the header of the collapsible panel
	 * @param form <code> Composite </code> the form composite that will be expanded and collapsed.
	 * @param headerForm <code> Composite </code> this is the form that will appear in the header. This will usually be just a single object as it needs to fit in the space of the header panel.
	 * @return A handle to the helper for the collapsible form created.
	 */
	public static XPXCollapsibleCompositeHelper createCollapsibleHeader(String title, Composite form, Composite headerForm){
		return createCollapsibleHeader(title, form, "", headerForm);
	}
	
	/**
	 * This method adds an advanced collapsible composite to the composite passed. This method allows you to specify both a header form and an id.
	 * NOTE: This method will create a composite as the parent of the panel passed as the form. The helper to this form will be returned
	 * @param title <code> String </code> the title which will be displayed in the header of the collapsible panel
	 * @param form <code> Composite </code> the form composite that will be expanded and collapsed.
	 * @param id <code> String </code> the unique id for this collapsible composite. This will also be appended to all of the objects created to ensure that they all have unique control names.
	 * @param headerForm <code> Composite </code> this is the form that will appear in the header. This will usually be just a single object as it needs to fit in the space of the header panel.
	 * @return A handle to the helper for the collapsible form created.
	 */
	public static XPXCollapsibleCompositeHelper createCollapsibleHeader(String title, Composite form, String id, Composite headerForm){
		XPXCollapsibleCompositeHelper newPanel = new XPXCollapsibleCompositeHelper(form.getParent(), form.getStyle(), title, form, id, headerForm, true);
		return newPanel;
	}
	
	public static XPXCollapsibleCompositeHelper createCollapsibleHeader(String title, Composite form, String id, Composite headerForm, boolean overrideLayout){
		XPXCollapsibleCompositeHelper newPanel = new XPXCollapsibleCompositeHelper(form.getParent(), form.getStyle(), title, form, id, headerForm,overrideLayout);
		return newPanel;
	}
	
	/**
	 * Creates a collapsible composite helper for a composite which is already formatted correctly.
	 *  formatted correctly means that the composite passed has two child controls and both are components It also means that the first composite is the header composite contains three children The first is a label (title label) the second is a composite (header workspace), and the last is a label (Image)
	 * @param collapsibleComposite - the properly formatted composite which the helper is to be created for
	 * @param id - A unique idnetifier for this composite. This will be used to identify the composite when there are multiple collapsible composites on the screen.
	 */
	public XPXCollapsibleCompositeHelper(Composite collapsibleComposite, String id){
		this.collapsibleComposite=collapsibleComposite;
		this.id = id;
		initializeObjects();
		
		addListeners();
	}
	
	private XPXCollapsibleCompositeHelper(Composite parent, int style, String title, Composite form, String id, Composite headerForm, boolean overrideLayout){
		this.overrideLayout = overrideLayout;
		collapsibleComposite = new Composite(parent, style);
		GridData gridData =  new GridData();
		gridData.grabExcessVerticalSpace=true;
		gridData.grabExcessHorizontalSpace=true;
		gridData.horizontalAlignment=SWT.FILL;
		gridData.verticalAlignment=SWT.FILL;
		collapsibleComposite.setLayoutData(gridData);
		
		GridLayout layoutData = new GridLayout();
		layoutData.marginHeight=1;
		layoutData.marginWidth=1;
		layoutData.horizontalSpacing=0;
		layoutData.verticalSpacing=0;
		collapsibleComposite.setLayout(layoutData);
		collapsibleComposite.setData(YRCConstants.YRC_CONTROL_NAME, "YRCCollapsibleComposite");
		
		cmpBody = form;
		this.title = title;
		this.id = id;
		cmpHeaderSpace = headerForm;
		
		createRootPanel();
	}

	private void createRootPanel() {
        createHeaderPanel();
        
        GridData gridData2 = new GridData();
		gridData2.horizontalAlignment=SWT.FILL;
		gridData2.verticalAlignment=SWT.FILL;
		gridData2.grabExcessHorizontalSpace=true;
		gridData2.grabExcessVerticalSpace=true;
		gridData2.verticalIndent=0;
		if(YRCPlatformUI.isVoid(cmpBody)){
			cmpBody = new Composite(collapsibleComposite, SWT.NONE);
			cmpBody.setData(YRCConstants.YRC_CONTROL_NAME, "collapsibleWorkspace");
		}else{
			cmpBody.setParent(collapsibleComposite);
		}
		cmpBody.setLayoutData(gridData2);
		collapsibleComposite.addPaintListener(paintListener);
	}
	
	private void createHeaderPanel(){
		GridData gridData = new GridData();
		gridData.horizontalAlignment=SWT.FILL;
		gridData.grabExcessHorizontalSpace=true;
		gridData.verticalAlignment=SWT.FILL;
		gridData.grabExcessVerticalSpace=false;
		gridData.heightHint = 17;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.verticalSpacing=0;
		gridLayout.marginHeight=0;
		gridLayout.marginWidth=0;
//		cmpHeader = new Composite(cmpRoot, SWT.NONE);
		cmpHeader = new Composite(collapsibleComposite, SWT.NONE);
		cmpHeader.setLayout(gridLayout);
		cmpHeader.setLayoutData(gridData);
		cmpHeader.setData(YRCConstants.YRC_CONTROL_NAME, "pnlHeader");
		cmpHeader.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		cmpHeader.addMouseListener(expandOrCollapse);
				
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment=SWT.FILL;
		gridData1.grabExcessHorizontalSpace=true;
		gridData1.verticalAlignment=SWT.FILL;
		if(!YRCPlatformUI.isVoid(cmpHeaderSpace) && overrideLayout){
			gridData1.grabExcessHorizontalSpace=false;
		}else{
			gridData1.grabExcessHorizontalSpace=true;
		}
		gridData1.grabExcessVerticalSpace=false;
		gridData1.horizontalIndent=2;
		lblTitle = new Label(cmpHeader, SWT.NONE);
		if(!YRCPlatformUI.isVoid(title)){
			lblTitle.setText(title);
		}	
		lblTitle.setLayoutData(gridData1);
		lblTitle.setData(YRCConstants.YRC_CONTROL_NAME, "lblTitle");
		lblTitle.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		listener = new ControlListener() {
			boolean isResizeInProgress= false;
			public void controlResized(ControlEvent e) {
				if (!isResizeInProgress) {
					isResizeInProgress = true;
					Display.getDefault().asyncExec(new Runnable() {

						public void run() {
							showTooltip();
						}

					});

					isResizeInProgress = false;
					lblTitle.removeControlListener(listener);
					lblTitle.setToolTipText(title);
				}
			}
			private void showTooltip() {
				Point requiredSize = lblTitle.computeSize (SWT.DEFAULT, SWT.DEFAULT,true);
				Point labelSize = lblTitle.getSize();
				boolean fullyVisible = requiredSize.x <= (labelSize.x- 60);
				if(!fullyVisible){
					String currentText = lblTitle.getText();
					int len = lblTitle.getText().length();
					if (len > 7) {
						String string = currentText.substring(0, len - 4)+ "...";
						lblTitle.setText(string);
						lblTitle.getParent().layout(true, true);
						showTooltip();
					}					
				}
			}
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub

			}

		};
		lblTitle.addMouseListener(expandOrCollapse);
		lblTitle.addControlListener(listener);

		
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment=SWT.FILL;
		gridData2.verticalAlignment=SWT.FILL;
		gridData2.grabExcessHorizontalSpace=true;
		gridData2.grabExcessVerticalSpace=false;
		if(!YRCPlatformUI.isVoid(cmpHeaderSpace)){
			cmpHeaderSpace.setParent(cmpHeader);
			if(this.overrideLayout)
				cmpHeaderSpace.setLayoutData(gridData2);
	
			//this ensures that the header panel theme is consistent 
			cmpHeaderSpace.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
			Control[] ctrls = cmpHeaderSpace.getChildren();
			for (int i = 0; i < ctrls.length; i++) {
				Control ctrl = ctrls[i];
				if(!YRCPlatformUI.isVoid(ctrl)){
					//if the theme of the object has already been set then we will leave it alone in case the programmer intends it to be that theme
					//otherwise set the theme to the panel header theme
					if(YRCPlatformUI.isVoid(ctrl.getData(YRCConstants.YRC_CONTROL_CUSTOMTYPE))){
						ctrl.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
					}
				}
			}
			cmpHeaderSpace.addMouseListener(expandOrCollapse);
		}
		
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment=SWT.END;
		gridData3.verticalAlignment=SWT.FILL;
		gridData3.grabExcessHorizontalSpace=false;
		gridData3.grabExcessVerticalSpace=false;
		lblImage = new Label(cmpHeader, SWT.LEFT);
		lblImage.setImage(YRCPlatformUI.getImage("CollapseUp"));
		lblImage.setLayoutData(gridData3);
		lblImage.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		
		lblImage.setData(YRCConstants.YRC_CONTROL_NAME, "lblImage");
		lblImage.setToolTipText(YRCPlatformUI.getString("Icon_TT_Collapse"));
		lblImage.addMouseListener(expandOrCollapse);
		
		YRCLabelBindingData lblImagebd = new YRCLabelBindingData();
		lblImagebd.setName("lblExpandCollapseIcon");
		lblImagebd.setImageBinding(true); 
		lblImage.setData(YRCConstants.YRC_LABEL_BINDING_DEFINITION, lblImagebd);
		
	}
	
	private void addListeners() {
		if(!YRCPlatformUI.isVoid(cmpHeader)){
			cmpHeader.addMouseListener(expandOrCollapse);
		}
		if(!YRCPlatformUI.isVoid(cmpHeaderSpace)){
			cmpHeaderSpace.addMouseListener(expandOrCollapse);
		}
		if(!YRCPlatformUI.isVoid(lblTitle)){
			lblTitle.addMouseListener(expandOrCollapse);
		}
		if(!YRCPlatformUI.isVoid(lblImage)){
			lblImage.addMouseListener(expandOrCollapse);
		}
	}

	private void initializeObjects() {
		//find the objects
		Control[] children = collapsibleComposite.getChildren();
		for (int i = 0; i < children.length; i++) {
			Control ctrl = children[i];
			if(ctrl instanceof Composite){
				if(YRCPlatformUI.isVoid(cmpHeader)){
					cmpHeader = (Composite)ctrl;
					Control[] hdrCtrls = cmpHeader.getChildren();
					for(int j = 0; j < hdrCtrls.length; j++) {
						Control hdrCtrl = hdrCtrls[i];
						if(hdrCtrl instanceof Composite){
							cmpHeaderSpace = (Composite)hdrCtrl;
							break;
						}
					}
				}else if(YRCPlatformUI.isVoid(cmpBody)){
					cmpBody = (Composite)ctrl;
				}else{
					YRCPlatformUI.trace("CollapsiblePanel Initialization Issue: collapsible panel may not work properly due.  \n Reason: root panel passed has too many children composites");
				}
			}else{
				YRCPlatformUI.trace("CollapsiblePanel Initialization Issue: collapsible panel may not work properly due.  \n Reason: root panel passed contains objects that are not composites");
			}
		}

		//throw errors if needed
		if(YRCPlatformUI.isVoid(cmpHeader) || YRCPlatformUI.isVoid(cmpBody)){
			YRCPlatformUI.trace("CollapsiblePanel Initialization Issue: collapsible panel will not work.  \n Reason: panel passed does not contain header and body composites");
			return;
		}
		children = cmpHeader.getChildren();
		for (int i = 0; i < children.length; i++) {
			Control ctrl = children[i];
			if(ctrl instanceof Label){
				if(YRCPlatformUI.isVoid(lblTitle)){
					lblTitle = (Label)ctrl;
				}else if(YRCPlatformUI.isVoid(lblImage)){
					lblImage = (Label)ctrl;
					lblImage.setImage(YRCPlatformUI.getImage("CollapseUp"));
					lblImage.setToolTipText(YRCPlatformUI.getString("Icon_TT_Collapse"));
					lblImage.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
				}else{
					YRCPlatformUI.trace("CollapsiblePanel Initialization Issue: collapsible panel may not work properly due.  \n Reason: header panel has too many labels");
				}
			}else if(ctrl instanceof Composite){
				if(YRCPlatformUI.isVoid(cmpHeaderSpace)){
					cmpHeaderSpace = (Composite)ctrl;
				}else{
					YRCPlatformUI.trace("CollapsiblePanel Initialization Issue: collapsible panel may not work properly due.  \n Reason: header panel contains too many child composites");
				}
			}else{
				YRCPlatformUI.trace("CollapsiblePanel Initialization Issue: collapsible panel may not work properly due.  \n Reason: header panel contains unknown objects");
			}
		}
	}
	

	/**
	 * This object automatically does a page layout(true, true) on expand and collapse. If you have other changes you need to make before
	 * the layout is executed you can disable the auto refresh and execute it yourself.
	 */
	public void disableAutoRefreshOnCollapseAndExpand(){
		autoRefresh=false;
	}
	
	/**
	 * when the panel is expanded or collapsed the gridData grabExcessVerticalSpace attribute will be toggled from true to false.
	 * This ensure that the screen doesn't end up with any white space. This is only needed in certain scenarios where there are multiple
	 * panels on the screen that all grab excess.
	 */
	public void enableAdjustGrabExcessOnExpandCollapse(){
		adjustGrabExcessOnExpandCollapse=true;
	}
	
	/*this method is so that the parent screen can collapse the panel.  
	 * Having this method allows us to know if the collapse was user driven or not*/
	public void collapse(){
		collapsePanel();
		fireListener(COLLAPSE, false);
	}
	
	
	private void collapsePanel(){
		if(adjustGrabExcessOnExpandCollapse){
			//we have to set the layout to not grab excess to ensure that the panel will collapse all the way without taking up any white space
			((GridData)collapsibleComposite.getLayoutData()).grabExcessVerticalSpace=false;
		}
		((GridData)cmpBody.getLayoutData()).exclude = true;
		cmpBody.setVisible(false);
		lblImage.setImage(YRCPlatformUI.getImage("CollapseDown"));
		lblImage.setToolTipText(YRCPlatformUI.getString("Icon_TT_Expand"));
		if(autoRefresh){
			Composite currentPage = YRCDesktopUI.getCurrentPage();
			currentPage.layout(true, true);
		}
	}
	
	/*this method is so that the parent screen can expand the panel.  
	 * Having this method allows us to know if the expand was user driven or not*/
	public void expand(){
		expandPanel();
		fireListener(EXPAND, false);
	}
	
	private void expandPanel(){
		if(adjustGrabExcessOnExpandCollapse){
			//we have to set the layout to grab excess to ensure that the panel will expand all the way and fill the panel correctly
			((GridData)collapsibleComposite.getLayoutData()).grabExcessVerticalSpace=true;
		}
		((GridData)cmpBody.getLayoutData()).exclude = false;
		cmpBody.setVisible(true);
		lblImage.setImage(YRCPlatformUI.getImage("CollapseUp"));
		lblImage.setToolTipText(YRCPlatformUI.getString("Icon_TT_Collapse"));
		if(autoRefresh){
			Composite currentPage = YRCDesktopUI.getCurrentPage();
			if (!currentPage.isDisposed())
				currentPage.layout(true, true);
		}
	}
	
	public boolean isCollapsed(){
		return !cmpBody.isVisible();		
	}
	
	public String getID(){
		return id;
	}
	
	public Composite getBodyComposite(){
		return cmpBody;
	}
	
	public Composite getCustomHeaderComposite(){
		return cmpHeaderSpace;
	}
	
	public Composite getRootComposite(){
		return collapsibleComposite;
	}
	
	private void fireListener(String method, boolean userDriven){
		if(!YRCPlatformUI.isVoid(listeners)){
			Iterator it = listeners.iterator();
			while(it.hasNext()){
				Object listener = it.next();
				if(method==EXPAND){
					((IXPXCollapsibleCompositeListener)listener).panelExpanded(id, userDriven);
				}else if(method==COLLAPSE){
					((IXPXCollapsibleCompositeListener)listener).panelCollapsed(id, userDriven);
				}
			}
		}
	}
	
	public void addCollapsibleCompositeListener(IXPXCollapsibleCompositeListener plistener){
		if (YRCPlatformUI.isVoid(listeners))
			listeners = new ArrayList();
		listeners.add(plistener);
	}
	public void removeCollapsibleCompositeListener(IXPXCollapsibleCompositeListener plistener){
		if(!YRCPlatformUI.isVoid(listeners)){
			listeners.remove(plistener);
		}
	}
	
	public boolean setFocus(){
		if(!YRCPlatformUI.isVoid(cmpHeaderSpace)){
			Control focusableControl = findFocusableControl(cmpHeaderSpace);
			if(!YRCPlatformUI.isVoid(focusableControl)){
				return focusableControl.setFocus();
			}
		}
		
		return cmpBody.setFocus();
	}
	
	private Control findFocusableControl(Composite parent){
		Control[] children = cmpHeaderSpace.getChildren();
		for(int i = 0; i < children.length; i++) {
			Control child = children[i];
			if(!YRCPlatformUI.isVoid(child)){
				if(child instanceof Composite){
					Control focusableControl = findFocusableControl((Composite)child);
					if(!YRCPlatformUI.isVoid(focusableControl)){
						return focusableControl;
					}
				}else if(child instanceof Label){
					continue;
				}else{
					return child;
				}
			}
		}
		return null;
	}
}
/*
 * Created on Apr 14,2010
 *
 */
package com.xpedx.sterling.rcp.pca.orderlines.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.orderheader.screen.OrderHeaderPanel;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.xpedx.sterling.rcp.pca.util.XPXLineSeqNoComparator;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.IYRCPanelHolder;
import com.yantra.yfc.rcp.YRCButtonBindingData;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCExtentionBehavior;
import com.yantra.yfc.rcp.YRCLabelBindingData;
import com.yantra.yfc.rcp.YRCLinkBindingData;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCScrolledCompositeListener;
import com.yantra.yfc.rcp.YRCWizardBehavior;
import com.yantra.yfc.rcp.YRCXmlUtils;

/**
 * @author sdodda
 * 
 * Generated by MTCE Copyright � 2005, 2006 Sterling Commerce, Inc. All Rights
 * Reserved.
 */

public class OrderLinesPanel extends Composite implements IYRCComposite {

	public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.orderlines.screen.OrderLinesPanel"; // @jve:decl-index=0:

	private Composite pnlRoot = null;
	private OrderLinesPanelBehavior myBehavior;
	private YRCExtentionBehavior extensionBehavior;
	private YRCWizardBehavior wizBehavior;

	Composite pnlHeader;
	private Composite pnlPrimaryInformation;
	Composite pnlOrderHdr;
	private Composite pnlLines;
	private ScrolledComposite scrolledPnlforLines;
	private Composite compositeHolder;
	private Composite pnlDynamicLineParent;
	private Composite pnlTitleOrderLines;
	private Composite MiscPnl;
	private ScrolledComposite scrolledPnlforHdr;
	private YRCScrolledCompositeListener scrolledPnlforHdrLsnr;
	private YRCScrolledCompositeListener scrolledPnlforLinesLsnr;
	private Link lnkAddLines;
	private Button btnUpdateOrder;
	private Button btnIsReviewed;
	private Button btnConfirmOrder;
	private Button btnPlaceOrder;
	private Button btnMarkOrderComplete;
	private int pnlNo;
	Label lblExpandCollapseIcon;
	public OrderHeaderPanel orderHeaderPanel;
	public HashMap<String, Element> itemUOMListMap=new HashMap<String, Element> ();
	public HashMap<String, String> itemMultipleListMap=new HashMap<String, String> ();

	public OrderLinesPanel(Composite parent, int style, Object inputObject, String formId) {
		super(parent, style);
		pnlNo = 1;
		initialize();
		setBindingForComponents();
		myBehavior = new OrderLinesPanelBehavior(this, FORM_ID, inputObject);

	}
	public OrderLinesPanel(Composite parent, int style, Object inputObject, String formId,int noOfLines, Element eleCustomerInfo) {
		super(parent, style);
		pnlNo = 1;
		initialize();
		setBindingForComponents();
		myBehavior = new OrderLinesPanelBehavior(this, FORM_ID, inputObject,true, eleCustomerInfo);
	}
	private void initialize() {
		createRootPanel();
		this.setLayout(new FillLayout());
		setSize(new org.eclipse.swt.graphics.Point(1000, 200));
	}

	private void setBindingForComponents() {
		// set all bindings here
		YRCLinkBindingData lbd = new YRCLinkBindingData();
		lbd.setName("lnkAddLines");
		lbd.setActionId("com.xpedx.sterling.rcp.pca.orderlines.action.XPXAddOrderLinesAction");
		lbd.setActionHandlerEnabled(true);
		lnkAddLines.setData(YRCConstants.YRC_LINK_BINDING_DEFINATION, lbd);

		YRCButtonBindingData bbd = new YRCButtonBindingData();
		bbd.setName("btnUpdateOrder");
		bbd.setActionHandlerEnabled(true);
		bbd.setActionId("com.xpedx.sterling.rcp.pca.orderlines.action.XPXUpdateOrderAction");
		btnUpdateOrder.setData("YRCButtonBindingDefination", bbd);
		
		bbd = new YRCButtonBindingData();
		bbd.setName("btnIsReviewed");
		bbd.setActionHandlerEnabled(true);
	//	bbd.setActionId("com.xpedx.sterling.rcp.pca.orderlines.action.XPXUpdateOrderAction");
		btnIsReviewed.setData("YRCButtonBindingDefination", bbd);
		
		bbd = new YRCButtonBindingData();
		bbd.setName("btnConfirmOrder");
		bbd.setActionHandlerEnabled(true);
		bbd.setActionId("com.xpedx.sterling.rcp.pca.orderlines.action.XPXConfirmOrderAction");
		btnConfirmOrder.setData("YRCButtonBindingDefination", bbd);

		//btnPlaceOrder
		bbd = new YRCButtonBindingData();
		bbd.setName("btnPlaceOrder");
		bbd.setActionHandlerEnabled(true);
		bbd.setActionId("com.xpedx.sterling.rcp.pca.orderlines.action.XPXPlaceOrderAction");
		btnPlaceOrder.setData("YRCButtonBindingDefination", bbd);
		
//		btnMarkOrderComplete
		bbd = new YRCButtonBindingData();
		bbd.setName("btnMarkOrderComplete");
		bbd.setActionHandlerEnabled(true);
		bbd.setActionId("com.xpedx.sterling.rcp.pca.ordersummary.action.XPXMarkOrderCompleteAction");
		btnMarkOrderComplete.setData("YRCButtonBindingDefination", bbd);
		
		
        /*START - Collapsible Composite***/
        YRCLabelBindingData lblExpandCollapseIconbd = new YRCLabelBindingData();
        lblExpandCollapseIconbd.setName("lblExpandCollapseIcon");
        lblExpandCollapseIconbd.setImageBinding(true);
        lblExpandCollapseIcon.setData("YRCLabelBindingDefinition", lblExpandCollapseIconbd);
        /*END - Collapsible Composite***/
      

	}

	public String getFormId() {
		return FORM_ID;
	}

	public Composite getRootPanel() {
		return pnlRoot;
	}

	private void createRootPanel() {
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.verticalSpacing = 2;
		gridLayout5.marginHeight = 0;
		gridLayout5.marginWidth = 0;
		pnlRoot = new Composite(this, SWT.NONE);
		pnlRoot.setData("yrc:customType", "TaskComposite");
		pnlRoot.setData("name", "parent");
		pnlRoot.setLayout(gridLayout5);
		showRootPanel(true);
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
		compositeHolder = new Composite(pnlRoot, 0);
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
		gridData16.widthHint = 150;
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
		lnkAddLines = new Link(MiscPnl, 0);
		lnkAddLines.setText("ADD_LINES");
		lnkAddLines.setData("yrc:customType", "Link");
		lnkAddLines.setLayoutData(gridData6);
		lnkAddLines.setData("name", "lnkAddLines");
		
		btnIsReviewed = new Button(MiscPnl, 0);
		btnIsReviewed.setText("Remove Needs Attention");
		btnIsReviewed.setLayoutData(gridData16);
		btnIsReviewed.setData("name", "btnIsReviewed");
		btnIsReviewed.setVisible(false);
		//Addde button listner. The click of this button will change the hold status to 1300
		btnIsReviewed.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				myBehavior.changeHoldStatus();
			}
		});
		
		btnUpdateOrder = new Button(MiscPnl, 0);
		btnUpdateOrder.setText("Update_Order");
		btnUpdateOrder.setLayoutData(gridData16);
		btnUpdateOrder.setData("name", "btnUpdateOrder");
		btnConfirmOrder = new Button(MiscPnl, 0);
		btnConfirmOrder.setText("Confirm_Order");
		btnConfirmOrder.setLayoutData(gridData16);
		btnConfirmOrder.setData("name", "btnConfirmOrder");
		
		btnPlaceOrder = new Button(MiscPnl, 0);
		btnPlaceOrder.setText("Place_Orders");
		btnPlaceOrder.setLayoutData(gridData16);
		btnPlaceOrder.setData("name", "btnPlaceOrder");
		
		btnMarkOrderComplete = new Button(MiscPnl, 0);
		btnMarkOrderComplete.setText("Mark_Complete");
		btnMarkOrderComplete.setLayoutData(gridData16);
		btnMarkOrderComplete.setData("name", "btnMarkOrderComplete");
	}

	private void createComposite() {
		createHeaderComposite();
		createPnlOrderLines();
	}
	
	private void createHeaderComposite()
    {
		
		
        GridLayout gridLayout5 = new GridLayout();
        gridLayout5.marginWidth = 1;
        gridLayout5.marginHeight = 1;
        gridLayout5.verticalSpacing = 0;
        gridLayout5.horizontalSpacing = 0;
        GridData gridData21 = new GridData();
        gridData21.horizontalAlignment = 4;
        gridData21.grabExcessHorizontalSpace = true;
        gridData21.grabExcessVerticalSpace = false;
        gridData21.verticalAlignment = 4;
        pnlHeader = new Composite(pnlRoot, SWT.NONE);
        pnlHeader.setLayoutData(gridData21);
        pnlHeader.setLayout(gridLayout5);
         pnlHeader.setData("name", "pnlHeader");
         pnlHeader.addPaintListener(new org.eclipse.swt.events.PaintListener() {
 			public void paintControl(org.eclipse.swt.events.PaintEvent e) {
 				XPXUtils.paintControl(e);
 			}
 		});

       
        
        GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginWidth = 1;
		gridLayout1.marginHeight = 1;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = 4;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = 4;
        pnlOrderHdr = new Composite(pnlHeader, SWT.NONE);
        pnlOrderHdr.setLayoutData(gridData);
        pnlOrderHdr.setLayout(gridLayout1);
        pnlOrderHdr.setData("name", "pnlOrderHdr");
        pnlOrderHdr.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
        
		GridLayout pnlOrderHdrLayout = new GridLayout(3, false);
		pnlOrderHdrLayout.horizontalSpacing = 2;
		pnlOrderHdrLayout.verticalSpacing = 2;
		pnlOrderHdrLayout.marginHeight = 2;
		pnlOrderHdrLayout.marginWidth = 2;
		pnlOrderHdr.setLayout(pnlOrderHdrLayout);
        
        
       Label primaryDetailsTitle = new Label(pnlOrderHdr, SWT.LEFT);
		GridData primaryDetailsTitlelayoutData = new GridData();
		primaryDetailsTitlelayoutData.verticalAlignment = 16777216;
		primaryDetailsTitlelayoutData.grabExcessHorizontalSpace = true;
		primaryDetailsTitle.setLayoutData(primaryDetailsTitlelayoutData);
		primaryDetailsTitle.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		primaryDetailsTitle.setText("Primary_Details");
		/*START - Collapsible Composite***/
		lblExpandCollapseIcon = new Label(pnlOrderHdr, SWT.LEFT);
		lblExpandCollapseIcon.setImage(YRCPlatformUI.getImage("CollapseUp"));
		GridData lblExpandCollapseIconlayoutData = new GridData();
		lblExpandCollapseIconlayoutData.horizontalAlignment = 16777224;
		lblExpandCollapseIconlayoutData.verticalAlignment = 16777216;
		lblExpandCollapseIcon.setLayoutData(lblExpandCollapseIconlayoutData);
		lblExpandCollapseIcon.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "PanelHeader");
		lblExpandCollapseIcon.setText("lblExpandCollapseIcon");
		/*END - Collapsible Composite***/
		

        createPnlHeaderPrimaryInfo();
    }
	
	private void createPnlHeaderPrimaryInfo() {
        
        GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = 4;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = 4;
		gridData1.heightHint = 400; 
		gridData1.widthHint = 400;
		scrolledPnlforHdr = new ScrolledComposite(pnlHeader, SWT.V_SCROLL|SWT.H_SCROLL);
		scrolledPnlforHdr.setLayoutData(gridData1);
		scrolledPnlforHdr.setExpandHorizontal(true);
		scrolledPnlforHdr.setExpandVertical(true);
		scrolledPnlforHdr.setAlwaysShowScrollBars(true);
			//scrolledPnlforHdrLsnr = new YRCScrolledCompositeListener(scrolledPnlforHdr);

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
		pnlPrimaryInformation = new Composite(scrolledPnlforHdr, 0);
		pnlPrimaryInformation.setLayoutData(gridData0);
		pnlPrimaryInformation.setLayout(gridLayout);
		pnlPrimaryInformation.setData("name", "pnlPrimaryInformation");
		pnlPrimaryInformation.setData("yrc:customType", "TaskComposite");
		
		GridLayout gridLayoutScroll = new GridLayout();
		
		scrolledPnlforHdr.setLayout(gridLayoutScroll);
		scrolledPnlforHdr.setContent(pnlPrimaryInformation);
		scrolledPnlforHdr.setData("yrc:customType", "TaskComposite");
		scrolledPnlforHdr.setData("name", "scrolledPnlforHdr");

	}

	private void createPnlOrderLines()
    {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.marginWidth = 1;
        gridLayout.verticalSpacing = 1;
        gridLayout.marginHeight = 1;
        gridLayout.horizontalSpacing = 0;
        gridLayout.makeColumnsEqualWidth = false;
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = 4;
        gridData11.grabExcessHorizontalSpace = true;
        gridData11.horizontalSpan = 4;
        gridData11.verticalSpan = 1;
        gridData11.grabExcessVerticalSpace = true;
        gridData11.verticalAlignment = 4;
        gridData11.exclude = true;
        gridData11.minimumHeight = 100;
        GridLayout gridLayout5 = new GridLayout();
        gridLayout5.marginWidth = 1;
        gridLayout5.marginHeight = 1;
        gridLayout5.verticalSpacing = 0;
        gridLayout5.horizontalSpacing = 0;
        GridData gridData21 = new GridData();
        gridData21.horizontalAlignment = 4;
        gridData21.grabExcessHorizontalSpace = true;
        gridData21.grabExcessVerticalSpace = false;
        gridData21.horizontalSpan = 2;
        gridData21.verticalAlignment = 4;
		pnlTitleOrderLines = new Composite(getRootPanel(), 0);
        pnlTitleOrderLines.setLayoutData(gridData21);
        pnlTitleOrderLines.setLayout(gridLayout5);
        XPXUtils.addGradientPanelHeader(pnlTitleOrderLines, "Order_Lines", true);

        GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginWidth = 1;
		gridLayout1.marginHeight = 1;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = 4;
		gridData.grabExcessHorizontalSpace = true;
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
		scrolledPnlforLines = new ScrolledComposite(pnlLines, 512|SWT.H_SCROLL);
		scrolledPnlforLines.setLayoutData(gridData1);
		scrolledPnlforLines.setExpandHorizontal(true);
		scrolledPnlforLines.setExpandVertical(true);
		scrolledPnlforLines.setAlwaysShowScrollBars(false);
//		scrolledPnlforLinesLsnr = new YRCScrolledCompositeListener(scrolledPnlforLines);

		createComposite1();
		scrolledPnlforLines.setLayout(new GridLayout());
		scrolledPnlforLines.setContent(pnlDynamicLineParent);
		scrolledPnlforLines.setData("yrc:customType", "TaskComposite");
		scrolledPnlforLines.setData("name", "scrolledComposite");
	}

	private void createComposite1() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 5;
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

	/*--------------------------------------------------------------------------------------------------------********/
	public void addBlankLines(int addNoOfLines) {
		ArrayList listOrderLine = new ArrayList();
		for (int i = 0; i < addNoOfLines; i++) {
			listOrderLine.add(YRCXmlUtils.createFromString("<OrderLine />").getDocumentElement());
		}
		createLines(listOrderLine,null, null);
	}
	public void addBlankLines() {
		ArrayList listOrderLine = new ArrayList();
		int addNoOfLines = 1;
		for (int i = 0; i < addNoOfLines; i++) {
			listOrderLine.add(YRCXmlUtils.createFromString("<OrderLine />").getDocumentElement());
		}
		createLines(listOrderLine,null, null);
	}

	public void createLines(ArrayList listOrderLine, Element PandAResponse, Element uOMList) {
//		repeatingLinePnlAry = new Object[listOrderLine.size()];
		//changes as part of fixing bug#2597 -- eleOLUOMList global ele has created
		Element eleOLUOMList = uOMList;
		
		//--Checking for New Order.
		Element eleOrdLineNewOrder = (Element) listOrderLine.get(0);
		String lineNumberForOrder = eleOrdLineNewOrder.getAttribute("PrimeLineNo");
		if(!YRCPlatformUI.isVoid(lineNumberForOrder)){
		Collections.sort(listOrderLine, new XPXLineSeqNoComparator());
		}
		Element placedOrderEle=myBehavior.getTargetModel("placedOrderLineErrorMap");
		Element orderUOMList=myBehavior.getTargetModel("orderUOMList");
		for (int i = 0; i < listOrderLine.size(); i++){
			Element PandALineResponse = null;
			Element eleUOMList=null;
			String lineErrorReturn="";
			Element eleOrdLine = (Element) listOrderLine.get(i);
			String lineNumber = eleOrdLine.getAttribute("PrimeLineNo");
			if(!YRCPlatformUI.isVoid(lineNumber)){
				//int i = Integer.valueOf(item.getLineNumber()).intValue(); 
				int j = Integer.valueOf(eleOrdLine.getAttribute("PrimeLineNo")).intValue(); 
				if (null != PandAResponse) {
					NodeList nl=PandAResponse.getElementsByTagName("Item");
					int count=nl.getLength();
					for(int k=0;k<count;k++){
						Element ele=(Element)nl.item(k);
						if(!YRCPlatformUI.isVoid(ele)){
						String line=YRCXmlUtils.getChildElement(ele, "LineNumber").getTextContent();
						int x=Integer.valueOf(line).intValue();
						if(x==j)
							PandALineResponse=ele;
					}
					}
				}
			}
			if(YRCPlatformUI.isVoid(uOMList) && !YRCPlatformUI.isVoid(lineNumber)){

				YRCXmlUtils.getString(eleOrdLine);
				if(!YRCPlatformUI.isVoid(eleOrdLine)){
				String itemId=YRCXmlUtils.getXPathElement(eleOrdLine, "/OrderLine/Item").getAttribute("ItemID");
				if(itemUOMListMap.containsKey(itemId)){
					//changes as part of fixing bug#2597
					eleOLUOMList=itemUOMListMap.get(itemId);
					}
			}
			}
			if(!YRCPlatformUI.isVoid(lineNumber)){
				
				String itemId=YRCXmlUtils.getXPathElement(eleOrdLine, "/OrderLine/Item").getAttribute("ItemID");
				if(itemMultipleListMap.containsKey(itemId)){
					String orderMultiple=itemMultipleListMap.get(itemId);
					eleOrdLine.setAttribute("OrderMultiple", orderMultiple);
				}
				}
			if(!YRCPlatformUI.isVoid(placedOrderEle)){
				Element extnEle=YRCXmlUtils.getChildElement(eleOrdLine, "Extn");
				if(!YRCPlatformUI.isVoid(extnEle)){
					String WeblineNo=extnEle.getAttribute("ExtnWebLineNumber");
					if(XPXUtils.isCustomerOrder((Element)((HashMap)myBehavior.getModelsMap().get(OrderLinesPanel.FORM_ID)).get("OrderDetails"))){
						NodeList nl=placedOrderEle.getElementsByTagName("Order");
						int counter=nl.getLength();
						for(int ii=0;ii<counter;ii++){
							Element ele=(Element)nl.item(ii);
							if(!YRCPlatformUI.isVoid(ele)){
								NodeList nodeOrderLine=placedOrderEle.getElementsByTagName("OrderLine");
								int lineCounter=nodeOrderLine.getLength();
								for(int p=0;p<lineCounter;p++){
									Element eleLine=(Element)nodeOrderLine.item(p);
									Element extnLineEle=YRCXmlUtils.getChildElement(eleLine, "Extn");
									if(!YRCPlatformUI.isVoid(extnLineEle)){
									String extnLineNo=extnLineEle.getAttribute("ExtnWebLineNumber");
									if(extnLineNo.equals(WeblineNo)){
										lineErrorReturn=extnLineEle.getAttribute("ExtnLineStatusCode");
										eleOrdLine.setAttribute("LineErrorValue", lineErrorReturn);
									}
									
								}
								}
							}
						}
					} else {
						String strLineInError = extnEle.getAttribute("ExtnLineStatusCode");
						eleOrdLine.setAttribute("LineErrorValue", strLineInError);
					}
				}
			}
			
			createLine(i,eleOrdLine,PandALineResponse, eleOLUOMList);
		}
		adjustScrollPnl(scrolledPnlforLines, pnlDynamicLineParent, pnlLines, true, true);
//		adjustHdrScrollPnl(scrolledPnlforHdr, scrolledPnlforHdr, pnlPrimaryInformation, true, true);
		adjustScrollPnl(scrolledPnlforHdr, pnlPrimaryInformation, pnlHeader, true, true);

	}

	public void adjustHdrScrollPnl(ScrolledComposite scrPnl, Composite scrChild, Composite scrParent, boolean isHScrollReqd, boolean isVScrollReqd) {
		scrPnl.setMinSize(5, 700);
		scrParent.layout(true, true);
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
				// if(((OrderLinePanel)childIterator[k]).getLineNo() ==
				// highlightedLineNo)
				// {
				// selectedHeight = HEIGHT - 5 * (k + 1);
				// selectedPanelHeight = boundHeight;
				// }
				HEIGHT += boundHeight + 5;
				if (WIDTH < boundWidth)
					WIDTH = boundWidth;
			}
			if (!isHScrollReqd)
				continue;
			WIDTH += 5;
			if (HEIGHT < boundHeight)
				HEIGHT = boundHeight;
		}

		scrPnl.setMinSize(WIDTH, HEIGHT);
		
		if (isVScrollReqd && (selectedHeight < scrPnl.getOrigin().y || selectedHeight + selectedPanelHeight > scrPnl.getSize().y + scrPnl.getOrigin().y))
			scrPnl.setOrigin(0, selectedHeight);
		scrParent.layout(true, true);
	}

	public void createHeader(Element eleOrderDetails) {
		GridData gridData0 = new GridData();
		gridData0.horizontalAlignment = 4;
		gridData0.grabExcessVerticalSpace = false;
		gridData0.grabExcessHorizontalSpace = true;
		gridData0.verticalAlignment = 4;
		Object inObject = getInput();
		eleOrderDetails = YRCXmlUtils.getCopy(eleOrderDetails);
		orderHeaderPanel = new OrderHeaderPanel(pnlPrimaryInformation, this, SWT.NONE, inObject, eleOrderDetails);

		orderHeaderPanel.setData("name", "pnlHeader");
		orderHeaderPanel.setLayoutData(gridData0);
		//pnlHeader.addListener(26, scrolledPnlforHdrLsnr);
		adjustScrollPnl(scrolledPnlforHdr, pnlPrimaryInformation, orderHeaderPanel, true, true);
	}
	
	private OrderLinePanel createLine(int i, Element eleOrderLine, Element elePandALineResponse, Element uOMList) {
		GridData gridData0 = new GridData();
		gridData0.horizontalAlignment = 4;
		gridData0.grabExcessVerticalSpace = false;
		gridData0.grabExcessHorizontalSpace = true;
		gridData0.verticalAlignment = 4;
		Object inObject = getInput();
		eleOrderLine = YRCXmlUtils.getCopy(eleOrderLine);
		OrderLinePanel repeatingLinePnl1 = new OrderLinePanel(pnlDynamicLineParent, this, SWT.NONE, inObject, pnlNo, eleOrderLine, elePandALineResponse, uOMList);
		pnlNo++;
		repeatingLinePnl1.setData("name", "repeatingLinePnl");
		repeatingLinePnl1.setLayoutData(gridData0);
//		repeatingLinePnl1.addListener(26, scrolledPnlforLinesLsnr);
		repeatingLinePnl1.setCompositeWidths();
		return repeatingLinePnl1;
	}

	public Object getInput() {
		Object inObject = null;
		org.eclipse.ui.part.WorkbenchPart currentPart = YRCDesktopUI.getCurrentPart();
		inObject = ((YRCEditorPart) currentPart).getEditorInput();
		return inObject;
	}

	/*--------------------------------------------------------------------------------------------------------********/

	public void showRootPanel(boolean show) {
		pnlRoot.setVisible(show);
	}

	public IYRCPanelHolder getPanelHolder() {
		return null;
	}

	public String getHelpId() {
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

	public void setModelforOrderLines(ArrayList children, String orderHeaderKey, boolean isDraftOrder) {
		setVisible(false);
		pnlRoot.setRedraw(false);
		int k = 0;
		for (Iterator i$ = children.iterator(); i$.hasNext();) {
			Element orderLineElem = (Element) i$.next();

		}
	}

	public void getTargetModelAndCallUpdateApi(boolean isRefreshReqd) {
		myBehavior.clearDashBoardOverride();
		Control headerIterator[] = pnlPrimaryInformation.getChildren();
		OrderHeaderPanel headerpnl = (OrderHeaderPanel)headerIterator[0];
		Element eleOrder = headerpnl.getBehavior().getTargetModelforParent();;
		Control childIterator[] = pnlDynamicLineParent.getChildren();
		int noOfChildren = childIterator.length;
		for (int k = 0; k < noOfChildren; k++) {
			OrderLinePanel childpnl = (OrderLinePanel) childIterator[k];
			if (childpnl.isDisposed())
				continue;
			Element orderLineElem;
			orderLineElem = childpnl.getBehavior().getTargetModelforParent();
			if(!childpnl.getBehavior().validateLineToBeAdded()){
				continue;
			}
			if(childpnl.getBehavior().validateOrderLineBeforeUpdate(orderLineElem)){
				myBehavior.appendOrderLine(eleOrder, orderLineElem,childpnl.getBehavior());
			}else{
				return;
			}
		}
		myBehavior.callUpdateApi();
	}

	public OrderLinesPanelBehavior getPageBehavior() {
		return myBehavior;
	}
	public void setExtentionBehavior(YRCExtentionBehavior extensionBehavior) {
		this.extensionBehavior= extensionBehavior;
	}
	public YRCExtentionBehavior getExtentionBehavior() {
		return extensionBehavior;
	}
	public void clearLines() {
		
		Control headerIterator[] = pnlPrimaryInformation.getChildren();
		OrderHeaderPanel headerpnl = (OrderHeaderPanel)headerIterator[0];
		headerpnl.dispose();
		
		Control childIterator[] = pnlDynamicLineParent.getChildren();
		int noOfChildren = childIterator.length;
		for (int k = 0; k < noOfChildren; k++) {
			OrderLinePanel childpnl = (OrderLinePanel) childIterator[k];
			childpnl.dispose();
		}
	}
	public void setControlsView(Element orderDetailsElem) {
		if(this.getPageBehavior().isReadOnlyPage()){
			Control[] controls = new Control[]{
					lnkAddLines,
					btnUpdateOrder,
					btnConfirmOrder,
					btnPlaceOrder,
					btnMarkOrderComplete
			};
			for (Control control : controls) {
				if(null != control)
					control.setEnabled(false);
			}
		}
		if(this.getPageBehavior().hasErrors())
		{
			btnConfirmOrder.setEnabled(false);
		}
	}
	public void setAllButtonsEnabled(boolean value) {
			Control[] controls = new Control[]{
					lnkAddLines,
					btnUpdateOrder,
					btnConfirmOrder,
					btnPlaceOrder,
					btnMarkOrderComplete
			};
			for (Control control : controls) {
				if(null != control)
					control.setEnabled(value);
			}
	}	
	
	public void clearOrderLines() {
		
		Control childIterator[] = pnlDynamicLineParent.getChildren();
		int noOfChildren = childIterator.length;
		for (int k = 0; k < noOfChildren; k++) {
			OrderLinePanel childpnl = (OrderLinePanel) childIterator[k];
			childpnl.dispose();
		}
	}
	public void setControlsHiddenForCancelOrder(Element orderDetailsElem,boolean value) {
		String status=null;
		Control[] controls = new Control[]{
				lnkAddLines,
				btnUpdateOrder,
				btnPlaceOrder,
				btnMarkOrderComplete
		};
		status=YRCXmlUtils.getAttributeValue(orderDetailsElem,"/Order/@Status");
		for (Control control : controls) {
			if(null != control && "Cancelled".equals(status))
					control.setVisible(value);
		}
}	
	
	public void setControlsEnableForFewOrderStatus(Element orderDetailsElem,boolean value) {
		String status=null;
		Control[] controls = new Control[]{
				lnkAddLines,
				btnUpdateOrder,
				btnPlaceOrder,
				btnMarkOrderComplete
		};
		status=YRCXmlUtils.getAttributeValue(orderDetailsElem,"/Order/@MinOrderStatus");
		if(!YRCPlatformUI.isVoid(status)){
			Double minOrderStatus=Double.valueOf(status);
			for (Control control : controls) {
				if(null != control && 1100.5450 < minOrderStatus){
					control.setEnabled(value);
				}
			}
		}
	}
}

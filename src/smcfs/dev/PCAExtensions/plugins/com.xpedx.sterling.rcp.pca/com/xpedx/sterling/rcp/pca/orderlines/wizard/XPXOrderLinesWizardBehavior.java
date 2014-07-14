package com.xpedx.sterling.rcp.pca.orderlines.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.orderlines.screen.OrderLinesPanel;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCWizardBehavior;

public class XPXOrderLinesWizardBehavior extends YRCWizardBehavior {

	private Object editorInput;
	public static final String MODEL_Order = "Order"; 
	
	public XPXOrderLinesWizardBehavior(Composite ownerComposite, String formId, Object inputObject) {
		super(ownerComposite, formId, inputObject);
		this.editorInput = inputObject;
		init();
	}

	@Override	
	public IYRCComposite createPage(String pageIdToBeShown, Composite pnlRoot) {
		OrderLinesPanel page = null;
		if (pageIdToBeShown.equalsIgnoreCase(OrderLinesPanel.FORM_ID)) {
			page = new OrderLinesPanel(pnlRoot, SWT.NONE, this.editorInput, pageIdToBeShown);
			page.setWizBehavior(this);
			return page;
		}
		return page;
	}

	@Override
	public void pageBeingDisposed(String wizardPageFormId) {
		// TODO Auto-generated method stub

	}

	public void setTaskComplete(boolean flag) {
		// TODO Auto-generated method stub
		
	}

//	private void callOrder() {
//		
//		Document inputXml= getDocument(MODEL_Order,(YRCEditorInput)editorInput);
//		YRCApiContext context = new YRCApiContext();
//		context.setApiName(CMD_Order);
//		context.setFormId(XPXOrderLinesWizard.FORM_ID);
//	    context.setInputXml(inputXml);
//	    callApi(context);
//	}
	
	
	private Element getInputElement()
    {
        return ((YRCEditorInput)editorInput).getXml();
    }

}

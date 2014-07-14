package com.xpedx.sterling.rcp.pca.orderlines.wizard;

import org.eclipse.swt.widgets.Composite;

import com.yantra.yfc.rcp.YRCWizard;
import com.yantra.yfc.rcp.YRCWizardBehavior;

public class XPXOrderLinesWizard extends YRCWizard {

	public XPXOrderLinesWizard(Composite parent, int style, Object wizardInput) {
		super(XPXOrderLinesWizard.FORM_ID, parent, wizardInput, style);
		initializeWizard();
        start();
	}
	
	@Override
	public String getFormId() {
		return FORM_ID;
	}

	
	public XPXOrderLinesWizardBehavior getWizardBehavior() {
		return myBehavior;
	}
	
	@Override
	protected YRCWizardBehavior createBehavior() {
		myBehavior = new XPXOrderLinesWizardBehavior(this, FORM_ID, wizardInput);
        return myBehavior;
	}

	private XPXOrderLinesWizardBehavior myBehavior;
    public static final String FORM_ID = "com.xpedx.sterling.rcp.pca.orderlines.wizard.XPXOrderLinesWizard";
}

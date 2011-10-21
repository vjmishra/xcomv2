package com.xpedx.sterling.rcp.pca.ordersummary.action;

import org.eclipse.jface.action.IAction;

import com.xpedx.sterling.rcp.pca.orderlines.screen.OrderLinesPanel;
import com.xpedx.sterling.rcp.pca.ordersummary.extn.OrderSummaryWizardExtnBehavior;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.YRCAction;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCWizard;

public class XPXMarkOrderCompleteAction extends YRCAction {

	@Override
	public void execute(IAction action) {
		YRCWizard wizard = null;
		IYRCComposite currentPage = null;
		org.eclipse.swt.widgets.Composite comp = YRCDesktopUI.getCurrentPage();
		wizard = (YRCWizard) YRCDesktopUI.getCurrentPage();
		currentPage = (IYRCComposite) wizard.getCurrentPage();
		((OrderLinesPanel) currentPage).getPageBehavior().markOrderComplete();
//		OrderSummaryWizardExtnBehavior extnBehavior = (OrderSummaryWizardExtnBehavior) wizard.getExtensionBehavior();
//		extnBehavior.markOrderComplete();
		
	}
	
	@Override
	protected boolean checkForErrors() {
		// TODO Auto-generated method stub
//		return false;
		 return super.checkForErrors();
	}

	@Override
	protected boolean checkForModifications() {
		// TODO Auto-generated method stub
		return false;
//		 return super.checkForModifications();
	}


}

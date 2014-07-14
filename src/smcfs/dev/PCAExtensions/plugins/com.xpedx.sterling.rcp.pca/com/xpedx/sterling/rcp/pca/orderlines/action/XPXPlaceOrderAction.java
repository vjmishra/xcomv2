package com.xpedx.sterling.rcp.pca.orderlines.action;

import org.eclipse.jface.action.IAction;

import com.xpedx.sterling.rcp.pca.orderlines.screen.OrderLinesPanel;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.YRCAction;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCWizard;

public class XPXPlaceOrderAction extends YRCAction {

	@Override
	public void execute(IAction action) {
		org.eclipse.swt.widgets.Composite comp = YRCDesktopUI.getCurrentPage();
		IYRCComposite currentPage = null;
		YRCWizard wizard = null;
		if (comp instanceof YRCWizard) {
			wizard = (YRCWizard) YRCDesktopUI.getCurrentPage();
			currentPage = (IYRCComposite) wizard.getCurrentPage();
		} else if (comp instanceof IYRCComposite)
			currentPage = (IYRCComposite) comp;
		if (currentPage instanceof OrderLinesPanel) {
			((OrderLinesPanel) currentPage).getPageBehavior().placeOrderAction();
		}
		
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

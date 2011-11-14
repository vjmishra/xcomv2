package com.xpedx.sterling.rcp.pca.ordersummary.action;

import org.eclipse.jface.action.IAction;

import com.xpedx.sterling.rcp.pca.orderlines.screen.OrderLinesPanel;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.YRCAction;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCWizard;

public class SendNeedAttentionMail extends YRCAction {

	@Override
	public void execute(IAction arg0) {
		YRCWizard wizard = null;
		IYRCComposite currentPage = null;
		org.eclipse.swt.widgets.Composite comp = YRCDesktopUI.getCurrentPage();
		wizard = (YRCWizard) YRCDesktopUI.getCurrentPage();
		currentPage = (IYRCComposite) wizard.getCurrentPage();
		((OrderLinesPanel) currentPage).getPageBehavior().sendNeedAttentionMail();
		
	}
	
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

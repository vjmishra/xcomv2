package com.xpedx.sterling.rcp.pca.orderlines.action;

import org.eclipse.jface.action.IAction;

import com.xpedx.sterling.rcp.pca.orderlines.screen.OrderLinesPanel;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.YRCAction;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCWizard;

public class XPXAddOrderLinesAction extends YRCAction {

	public int intT=0;
	@Override
	public void execute(IAction action) {
		YRCWizard orderEntryWizard = (YRCWizard)YRCDesktopUI.getCurrentPage();
        IYRCComposite currentPage = (IYRCComposite)orderEntryWizard.getCurrentPage();
        if(currentPage instanceof OrderLinesPanel)
        {
            OrderLinesPanel page = (OrderLinesPanel)currentPage;
            page.addBlankLines();
        }
	}

	@Override
	protected boolean checkForErrors() {
		// TODO Auto-generated method stub
		return false;
//		return super.checkForErrors();
	}
	@Override
	protected boolean checkForModifications() {
		// TODO Auto-generated method stub
		return false;
//		return super.checkForModifications();
	}
}

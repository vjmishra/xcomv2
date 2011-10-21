package com.xpedx.sterling.rcp.pca.customerprofilerule.action;

import org.eclipse.jface.action.IAction;

import com.xpedx.sterling.rcp.pca.customerprofilerule.screen.CustomerProfileInfoPanel;
import com.xpedx.sterling.rcp.pca.customerprofilerule.screen.CustomerProfileMaintenance;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.YRCAction;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCWizard;

public class XPXUpdateCustomerProfileInfoAction extends YRCAction {
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
		if (currentPage instanceof CustomerProfileMaintenance) {
			CustomerProfileInfoPanel objCustomerProfileInfo = ((CustomerProfileMaintenance)currentPage).getCustomerProfileInfoObj();
			if(null != objCustomerProfileInfo)
				objCustomerProfileInfo.getPageBehavior().updateProfile();
		}
	}
	@Override
	protected boolean checkForErrors() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected boolean checkForModifications() {
		// TODO Auto-generated method stub
		return false;
	}
}

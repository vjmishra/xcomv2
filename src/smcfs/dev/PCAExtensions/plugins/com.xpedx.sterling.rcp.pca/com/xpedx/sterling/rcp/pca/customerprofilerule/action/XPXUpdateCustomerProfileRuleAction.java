package com.xpedx.sterling.rcp.pca.customerprofilerule.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.customerprofilerule.screen.CustomerProfileMaintenance;
import com.xpedx.sterling.rcp.pca.customerprofilerule.screen.CustomerProfileRulePanel;
import com.xpedx.sterling.rcp.pca.orderlines.screen.OrderLinesPanel;
import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.YRCAction;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCWizard;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXUpdateCustomerProfileRuleAction extends YRCAction {

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
			CustomerProfileRulePanel objCustomerProfileRules = ((CustomerProfileMaintenance)currentPage).getCustomerProfileRuleObj();
			if(null != objCustomerProfileRules)
				objCustomerProfileRules.getPageBehavior().updateRules();
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

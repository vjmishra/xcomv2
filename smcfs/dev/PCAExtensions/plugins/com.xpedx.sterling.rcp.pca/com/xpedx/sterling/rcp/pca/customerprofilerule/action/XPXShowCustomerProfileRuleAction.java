package com.xpedx.sterling.rcp.pca.customerprofilerule.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.yantra.yfc.rcp.YRCAction;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCWizard;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXShowCustomerProfileRuleAction extends YRCAction {

	public XPXShowCustomerProfileRuleAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(IAction action) {
		Composite currentPage = YRCDesktopUI.getCurrentPage();
		YRCEditorInput obj=(YRCEditorInput) ((YRCWizard) currentPage).wizardInput;
		Element ele = (Element)obj.getXml();
		Element eleEditorInput = YRCXmlUtils.getCopy(ele, true);
		eleEditorInput.getOwnerDocument().renameNode(eleEditorInput, eleEditorInput.getNamespaceURI(), "XPXCustomerProfileRuleEditor");
		
		YRCPlatformUI.openEditor("com.xpedx.sterling.rcp.pca.customerprofilerule.editor.XPXCustomerProfileRuleEditor", new YRCEditorInput(eleEditorInput, new String[] {"CustomerKey"}, ""));
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

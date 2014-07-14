package com.xpedx.sterling.rcp.pca.divisionMaintainance.task.action;

import org.eclipse.jface.action.IAction;
import org.w3c.dom.Element;

import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;
import com.yantra.yfc.rcp.YRCRelatedTaskAction;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXDivisionMaintainanceAction extends YRCRelatedTaskAction
		 {

	@Override
	public void executeTask(IAction arg0, YRCEditorInput arg1,
			YRCRelatedTask arg2) {
		//Composite currentPage = YRCDesktopUI.getCurrentPage();
		//YRCEditorInput obj=(YRCEditorInput) ((YRCWizard) currentPage).wizardInput;
		Element ele = YRCXmlUtils.createFromString("<XPXDivisionSearchEditor/>").getDocumentElement();
		Element eleEditorInput = YRCXmlUtils.getCopy(ele, true);
		eleEditorInput.getOwnerDocument().renameNode(eleEditorInput, eleEditorInput.getNamespaceURI(), "XPXDivisionSearchEditor");

		YRCPlatformUI.openEditor("com.xpedx.sterling.rcp.pca.divisionMaintainance.editor.XPXDivisionSearchEditor", new YRCEditorInput(eleEditorInput, new String[] {""}, arg2.getId()));

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

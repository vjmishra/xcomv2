package com.xpedx.sterling.rcp.pca.divisionMaintainance.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.yantra.yfc.rcp.YRCAction;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCWizard;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXDivisionMaintainanceAction extends YRCAction{

		public XPXDivisionMaintainanceAction() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void execute(IAction action) {
			Composite currentPage = YRCDesktopUI.getCurrentPage();
			YRCEditorInput obj=(YRCEditorInput) ((YRCWizard) currentPage).wizardInput;
			Element ele = YRCXmlUtils.createFromString("<XPXDivisionSearchEditor/>").getDocumentElement();
			Element eleEditorInput = YRCXmlUtils.getCopy(ele, true);
			eleEditorInput.getOwnerDocument().renameNode(eleEditorInput, eleEditorInput.getNamespaceURI(), "XPXDivisionSearchEditor");

			YRCPlatformUI.openEditor("com.xpedx.sterling.rcp.pca.divisionMaintainance.editor.XPXDivisionSearchEditor", new YRCEditorInput(eleEditorInput, new String[] {""}, ""));
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
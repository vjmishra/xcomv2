package com.xpedx.sterling.rcp.pca.csrmanagement.task.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.part.WorkbenchPart;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.csrmanagement.editor.XPXCSRMaintenanceEditor;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;
import com.yantra.yfc.rcp.YRCRelatedTaskAction;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXCSRMaintenanceAction extends YRCRelatedTaskAction {

	@Override
	public void executeTask(IAction iaction, YRCEditorInput yrceditorinput,
			YRCRelatedTask yrcrelatedtask) {
		WorkbenchPart workbench = YRCDesktopUI.getCurrentPart();
        workbench.setFocus();
        
        Element eInput = YRCXmlUtils.createDocument("XPXCSRS").getDocumentElement();
        YRCEditorInput editorInput = null;
            editorInput = new YRCEditorInput(eInput, new String[] {""}, "");
            boolean b = YRCPlatformUI.openEditor(XPXCSRMaintenanceEditor.ID_EDITOR, editorInput);
	}
	@Override
	protected boolean checkForErrors() {
		return false;
	}
	@Override
	protected boolean checkForModifications() {
		return false;
	}
}

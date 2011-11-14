package com.xpedx.sterling.rcp.pca.myitems.task.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.part.WorkbenchPart;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.myitems.editor.XPXMyItemsSearchListEditor;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;
import com.yantra.yfc.rcp.YRCRelatedTaskAction;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXMyItemsSearchListUIAction extends YRCRelatedTaskAction {

	public XPXMyItemsSearchListUIAction(){
		super();
	}
	@Override
	public void executeTask(IAction arg0, YRCEditorInput arg1,
			YRCRelatedTask arg2) {
		// TODO Auto-generated method stub
		WorkbenchPart workbench = YRCDesktopUI.getCurrentPart();
        workbench.setFocus();
        
        Element eInput = YRCXmlUtils.createDocument("XPEDXMyItemsList").getDocumentElement();
        YRCXmlUtils.setAttribute(eInput, "MyItemsSearchEditorKey", System.currentTimeMillis());
        YRCEditorInput editorInput = null;
            editorInput = new YRCEditorInput(eInput, new String[] {""}, arg2.getId());

        boolean b = YRCPlatformUI.openEditor(XPXMyItemsSearchListEditor.ID_EDITOR, editorInput);

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

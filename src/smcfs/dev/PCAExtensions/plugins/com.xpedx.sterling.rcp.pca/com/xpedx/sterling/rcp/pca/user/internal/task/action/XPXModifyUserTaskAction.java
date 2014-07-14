package com.xpedx.sterling.rcp.pca.user.internal.task.action;

import org.eclipse.jface.action.IAction;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.user.internal.editor.XPXSearchUserEditor;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;
import com.yantra.yfc.rcp.YRCRelatedTaskAction;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXModifyUserTaskAction extends YRCRelatedTaskAction {

	@Override
	public void executeTask(IAction arg0, YRCEditorInput arg1,
			YRCRelatedTask arg2) {
		// TODO Auto-generated method stub
		Element editorInputElement = YRCXmlUtils.createFromString("<XPXUserSearch />").getDocumentElement();
		YRCPlatformUI.openEditor(XPXSearchUserEditor.ID_EDITOR,new YRCEditorInput(editorInputElement,new String[] {""},arg2.getId()));
		

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

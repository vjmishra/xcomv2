package com.xpedx.sterling.rcp.pca.user.internal.task.action;

import org.eclipse.jface.action.IAction;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.articles.editor.XPXArticleMaintenanceEditor;
import com.xpedx.sterling.rcp.pca.user.internal.editor.XPXCreateUserEditor;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;
import com.yantra.yfc.rcp.YRCRelatedTaskAction;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXCreateUserTaskAction extends YRCRelatedTaskAction {

	@Override
	public void executeTask(IAction arg0, YRCEditorInput arg1,
			YRCRelatedTask arg2) {
		// TODO Auto-generated method stub
		Element eInput = YRCXmlUtils.createDocument("XPXUser").getDocumentElement();
        YRCEditorInput editorInput = null;
        editorInput = new YRCEditorInput(eInput, new String[] {""}, arg2.getId());
        YRCPlatformUI.openEditor("com.xpedx.sterling.rcp.pca.user.internal.editor.XPXCreateUserEditor", editorInput);
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

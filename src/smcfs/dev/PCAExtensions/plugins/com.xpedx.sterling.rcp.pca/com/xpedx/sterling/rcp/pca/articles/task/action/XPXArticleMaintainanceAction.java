package com.xpedx.sterling.rcp.pca.articles.task.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.part.WorkbenchPart;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.articles.editor.XPXArticleMaintenanceEditor;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;
import com.yantra.yfc.rcp.YRCRelatedTaskAction;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXArticleMaintainanceAction extends YRCRelatedTaskAction {

	public XPXArticleMaintainanceAction(){
		super();
	}
	@Override
	public void executeTask(IAction arg0, YRCEditorInput arg1,
			YRCRelatedTask arg2) {
		// TODO Auto-generated method stub
		WorkbenchPart workbench = YRCDesktopUI.getCurrentPart();
        workbench.setFocus();
        
        Element eInput = YRCXmlUtils.createDocument("XPXArticle").getDocumentElement();
        YRCXmlUtils.setAttribute(eInput, "ArticleMaintenanceEditorKey", System.currentTimeMillis());
        YRCEditorInput editorInput = null;
            editorInput = new YRCEditorInput(eInput, new String[] {""}, arg2.getId());

//        YRCEditorPart editorPart = YRCPlatformUI.findEditor(XPXArticleMaintenanceEditor.ID_EDITOR, editorInput);
        boolean b = YRCPlatformUI.openEditor(XPXArticleMaintenanceEditor.ID_EDITOR, editorInput);

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

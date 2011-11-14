package com.xpedx.sterling.rcp.pca.articles.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.xpedx.sterling.rcp.pca.articles.screen.ArticlesSearchListPanel;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEditorPart;

public class XPXArticleMaintenanceEditor extends YRCEditorPart {

	public static final String ID_EDITOR = "com.xpedx.sterling.rcp.pca.articles.editor.XPXArticleMaintenanceEditor";
	private Composite pnlRoot;
	public XPXArticleMaintenanceEditor() {
		pnlRoot=null;
	}

	public boolean isDirty() {
        return false;
    }

    public boolean isSaveAsAllowed() {
        return false;
    }
    
	@Override
	public Composite createPartControl(Composite parent, String taskID) {
		YRCEditorInput input = (YRCEditorInput)getEditorInput();
        input.setTaskName(taskID);
        pnlRoot = new ArticlesSearchListPanel(parent, SWT.NONE,input);
        pnlRoot.setData("name", "pnlManageArticles");
        pnlRoot.setData(YRCConstants.YRC_OWNERPART, this);
        
        setPartName(getTitleKey());
        
    	return pnlRoot;
	}

	public void postSetFocus() {
        pnlRoot.setFocus();
    }

    private String getTitleKey()
    {
        return "Manage_Articles";
    }
}

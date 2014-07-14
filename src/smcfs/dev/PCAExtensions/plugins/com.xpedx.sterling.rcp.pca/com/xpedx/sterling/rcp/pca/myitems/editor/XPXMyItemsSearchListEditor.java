package com.xpedx.sterling.rcp.pca.myitems.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.xpedx.sterling.rcp.pca.myitems.screen.XPXMyItemsSearchListScreen;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;

public class XPXMyItemsSearchListEditor extends YRCEditorPart {

	public static final String ID_EDITOR = "com.xpedx.sterling.rcp.pca.myitems.editor.XPXMyItemsSearchListEditor";
	private Composite pnlRoot;

	public XPXMyItemsSearchListEditor() {
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
        
        pnlRoot = new XPXMyItemsSearchListScreen(parent, SWT.NONE,input);
        pnlRoot.setData("name", "pnlMyItemsSearchList");
        pnlRoot.setData(YRCConstants.YRC_OWNERPART, this);
        
    	return pnlRoot;
	}
	@Override
	public String getTitle() {
	    return "My Items Search";
	}
	public void postSetFocus() {
        pnlRoot.setFocus();
    }

    public void showBusy(boolean busy)
    {
        if(busy)
            pnlRoot.setCursor(YRCPlatformUI.getCursor(1));
        else
            pnlRoot.setCursor(null);
    }
}

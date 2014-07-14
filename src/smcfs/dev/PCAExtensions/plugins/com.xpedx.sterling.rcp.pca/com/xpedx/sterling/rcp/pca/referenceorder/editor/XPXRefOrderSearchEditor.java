package com.xpedx.sterling.rcp.pca.referenceorder.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.xpedx.sterling.rcp.pca.referenceorder.screen.XPXReferenceOrderSearchListScreen;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;

public class XPXRefOrderSearchEditor extends YRCEditorPart {

	public static final String ID_EDITOR = "com.xpedx.sterling.rcp.pca.referenceorder.editor.XPXRefOrderSearchEditor";
	private String titleKey = "XPXRefOrderSearchEditor_title";
	private Composite pnlRoot;

	public XPXRefOrderSearchEditor() {
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
        
        pnlRoot = new XPXReferenceOrderSearchListScreen(parent, SWT.NONE,input);
        pnlRoot.setData("name", "pnlReferenceOrderSearchList");
        pnlRoot.setData(YRCConstants.YRC_OWNERPART, this);
        
        setPartName(getTitleKey());
        
    	return pnlRoot;
	}
	
	public void postSetFocus() {
        pnlRoot.setFocus();
    }

    private String getTitleKey()
    {
        return YRCPlatformUI.getString(titleKey);
    }

}
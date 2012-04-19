package com.xpedx.sterling.rcp.pca.orderhistory.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.xpedx.sterling.rcp.pca.orderhistory.screen.XPXOrderHistoryPanel;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;

public class XPXOrderHistoryEditor extends YRCEditorPart{

	public static final String ID_EDITOR = "com.xpedx.sterling.rcp.pca.orderhistory.editor.XPXOrderHistoryEditor";
	private String titleKey = "Customer Order Search";
	private Composite pnlRoot;

	public XPXOrderHistoryEditor() {
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
        
        pnlRoot = new XPXOrderHistoryPanel(parent, SWT.NONE,input);
        pnlRoot.setData("name", "pnlOrderHistory");
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

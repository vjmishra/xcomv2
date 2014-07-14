package com.xpedx.sterling.rcp.pca.csrmanagement.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


import com.xpedx.sterling.rcp.pca.csrmanagement.screen.XPXCSRMaintenancePanel;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEditorPart;

public class XPXCSRMaintenanceEditor extends YRCEditorPart {

	public static final String ID_EDITOR = "com.xpedx.sterling.rcp.pca.csrmanagement.editor.XPXCSRMaintenanceEditor";
	private Composite pnlRoot;
	public XPXCSRMaintenanceEditor() {
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
        pnlRoot = new XPXCSRMaintenancePanel(parent, SWT.NONE,input);
        pnlRoot.setData("name", "pnlManageCSRs");
        pnlRoot.setData(YRCConstants.YRC_OWNERPART, this);
        
        setPartName(getTitleKey());
        
    	return pnlRoot;
	}

	
	public void postSetFocus() {
        pnlRoot.setFocus();
    }

    private String getTitleKey()
    {
        return "Manage CSRs";
    }
}

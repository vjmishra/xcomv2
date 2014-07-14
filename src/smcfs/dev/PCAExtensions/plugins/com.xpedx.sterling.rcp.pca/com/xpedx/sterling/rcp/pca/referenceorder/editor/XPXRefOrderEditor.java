package com.xpedx.sterling.rcp.pca.referenceorder.editor;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.referenceorder.screen.XPXReferenceOrderSummaryPanel;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;

public class XPXRefOrderEditor extends YRCEditorPart {

	public static final String ID_EDITOR = "com.xpedx.sterling.rcp.pca.referenceorder.editor.XPXRefOrderEditor";
    private String titleKey = "XPXRefOrderEditor_title";

    private Composite pnlRoot;

	public void doSave(IProgressMonitor monitor) {
    }

    public void doSaveAs() {
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
        Element e = input.getXml();
        pnlRoot = new XPXReferenceOrderSummaryPanel(parent, SWT.NONE,input);
        pnlRoot.setData(YRCConstants.YRC_OWNERPART, this);
        
        setPartName(getTitleKey());
        
    	return pnlRoot;
	}
	public void postSetFocus() {
        pnlRoot.setFocus();
    }

    public void showBusy(boolean busy) {
        if (busy) {
            setPartName(YRCPlatformUI.getString("wait_title"));
            pnlRoot.setCursor(YRCPlatformUI.getCursor(SWT.CURSOR_WAIT));
        }
        else {
            setPartName(getTitleKey());
            pnlRoot.setCursor(null);
        }
    }
    private String getTitleKey()
    {
        return YRCPlatformUI.getString("XPXRefOrderEditor_title");
    }
}
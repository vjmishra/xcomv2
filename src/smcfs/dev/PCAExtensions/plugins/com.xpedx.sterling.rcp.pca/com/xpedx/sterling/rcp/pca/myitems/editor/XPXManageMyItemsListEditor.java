package com.xpedx.sterling.rcp.pca.myitems.editor;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;
import com.xpedx.sterling.rcp.pca.tasks.myitems.screen.XPXMyItemsListDetailsPanel;
public class XPXManageMyItemsListEditor extends YRCEditorPart {

//	private XPXDivisionMaintainanceWizardBehavior pnlRootBehavior;
	public static final String ID_EDITOR = "com.xpedx.sterling.rcp.pca.myitems.editor.XPXManageMyItemsListEditor";
    private Composite pnlRoot;


    @Override
	protected boolean acceptTask(YRCRelatedTask task) {
		return super.acceptTask(task);
	}

	@Override
	public void setInitializationData(IConfigurationElement cfig,
			String propertyName, Object data) {
		super.setInitializationData(cfig, propertyName, data);
	}

	@Override
	protected void setPartName(String partName) {
		super.setPartName(partName);
	}
	
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

    public Composite createPartControl(Composite parent, String taskID) {
		YRCEditorInput input = (YRCEditorInput)getEditorInput();
		Element ele = (Element) input.getInputObject();
		ele.getOwnerDocument().renameNode(ele, ele.getNamespaceURI(), "XPEDXMyItemsList");
        input.setTaskName(taskID);
        pnlRoot = new XPXMyItemsListDetailsPanel(parent,SWT.NONE,ele);
        pnlRoot.setData("name", "pnlManageMyItemsList");
        pnlRoot.setData(YRCConstants.YRC_OWNERPART, this);
        
        setPartName(YRCPlatformUI.getFormattedString(getTitleKey(),ele.getAttribute("Name")));
        
    	return pnlRoot;
    }

    public void postSetFocus() {
        pnlRoot.setFocus();
    }

    private String getTitleKey()
    {
        return "MyItemsListDetailEditor";
    }
     
}

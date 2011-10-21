package com.xpedx.sterling.rcp.pca.divisionMaintainance.editor;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.divisionMaintainance.screen.DivisionMaintenanceTabFolderPanel;
import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;

public class XPXDivisionMaintainanceEditor extends YRCEditorPart {

//		private XPXDivisionMaintainanceWizardBehavior pnlRootBehavior;
		public static final String ID_EDITOR = "com.xpedx.sterling.rcp.pca.divisionMaintainance.editor.XPXDivisionMaintainanceEditor";
	    private String titleKey = "XPXDivisionMaintainanceEditor_title";

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
	        input.setTaskName(taskID);
	        Element e = input.getXml();
	        pnlRoot = new DivisionMaintenanceTabFolderPanel(parent, SWT.NONE,input);
//	        pnlRoot = new DivisionMaintainanceSearchListPanel(parent,SWT.NONE);
	        pnlRoot.setData(YRCConstants.YRC_OWNERPART, this);
	        
	        setPartName(getTitleKey());//see what should be the title of the editor and give it
	        
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
	        return "Division Maintainance";
	    }
	     
	}

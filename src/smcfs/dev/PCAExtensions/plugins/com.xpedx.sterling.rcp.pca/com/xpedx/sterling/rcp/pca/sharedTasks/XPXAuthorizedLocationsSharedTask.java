package com.xpedx.sterling.rcp.pca.sharedTasks;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.sharedTasks.shiptolookup.XPXShowListOfAuthorizedLocationsPanel;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCDialog;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTask;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;

public class XPXAuthorizedLocationsSharedTask extends YRCSharedTask {

	
    private XPXShowListOfAuthorizedLocationsPanel holderScreen;
    
	public XPXAuthorizedLocationsSharedTask() {
		holderScreen = null;
	}
	
	@Override
	public void execute(Composite parent, String taskId) {
		YRCDesktopUI.getCurrentPart().showBusy(true);
		holderScreen = null;
        Element inputObject = getInput();
        if(!YRCPlatformUI.isVoid(parent))
        {
        	holderScreen = new XPXShowListOfAuthorizedLocationsPanel(parent, SWT.NONE, inputObject);
        } else {
        	holderScreen = new XPXShowListOfAuthorizedLocationsPanel(YRCPlatformUI.getShell(), SWT.NONE, inputObject);
        }
        YRCDialog dialog = new YRCDialog(holderScreen, 530, 400, "Authorized Ship-To Locations", null);
        dialog.open(); 
        YRCDesktopUI.getCurrentPart().showBusy(false);
	}
	@Override
	public YRCSharedTaskOutput getOutput() {
		return null;
	}
}

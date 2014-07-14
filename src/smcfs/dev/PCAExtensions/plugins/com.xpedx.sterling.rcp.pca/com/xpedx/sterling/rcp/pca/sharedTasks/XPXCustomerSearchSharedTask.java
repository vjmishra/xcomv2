package com.xpedx.sterling.rcp.pca.sharedTasks;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.sharedTasks.shiptolookup.XPXShowListOfCustomerPanel;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCDialog;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTask;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;
public class XPXCustomerSearchSharedTask extends YRCSharedTask{

	
	private YRCSharedTaskOutput sharedTaskOutput;
    private XPXShowListOfCustomerPanel holderScreen;
    
	public XPXCustomerSearchSharedTask() {
		holderScreen = null;
		sharedTaskOutput = null;
	}

	@Override
	public void execute(Composite parent, String taskId) {
		YRCDesktopUI.getCurrentPart().showBusy(true);
		holderScreen = null;
        Element inputObject = getInput();
        if(!YRCPlatformUI.isVoid(parent))
        {
        	holderScreen = new XPXShowListOfCustomerPanel(parent, SWT.NONE, inputObject);
        } else {
        	holderScreen = new XPXShowListOfCustomerPanel(YRCPlatformUI.getShell(), SWT.NONE, inputObject);
        }
        YRCDialog dialog = new YRCDialog(holderScreen, 530, 330, YRCPlatformUI.getString("Select CUSTOMER"), null);
        int result = 0;
        try{
        	result = dialog.open();
        }
        catch (Exception e) {
		}
        if(result==YRCDialog.OK){
	        this.setOutput(holderScreen.getSelectedShipTo());
        }
        YRCDesktopUI.getCurrentPart().showBusy(false);
	}

	public void setOutput(Element eleResponse){
		sharedTaskOutput = new YRCSharedTaskOutput();
		if(null != eleResponse)
			sharedTaskOutput.setOutput(eleResponse);
	}
	
	@Override
	public YRCSharedTaskOutput getOutput() {
		return sharedTaskOutput;
	}

}


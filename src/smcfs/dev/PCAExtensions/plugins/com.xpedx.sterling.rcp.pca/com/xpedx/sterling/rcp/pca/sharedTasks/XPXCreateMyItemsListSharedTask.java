package com.xpedx.sterling.rcp.pca.sharedTasks;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.tasks.myitems.screen.XPXCreateNewMyItemsListPopupPanel;
import com.xpedx.sterling.rcp.pca.tasks.myitems.screen.XPXMyItemListShareListPanel;
import com.xpedx.sterling.rcp.pca.tasks.myitems.screen.XPXMyItemsListDetailsPanel;
import com.yantra.yfc.rcp.YRCDialog;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTask;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;

public class XPXCreateMyItemsListSharedTask extends YRCSharedTask {

	private XPXCreateNewMyItemsListPopupPanel createNewMyItemsListPopup;
	private XPXMyItemListShareListPanel editShareListPopup;
	
	public XPXCreateMyItemsListSharedTask() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Composite parent, String taskId) {
		initScreen();
		Element inputObject = getInput();
		if(parent instanceof XPXMyItemsListDetailsPanel)
		{
			launchEditShareListPopup(parent,inputObject);
		} else 
		{
			launchCreateNewMyItemsListPopup(parent, inputObject);
		}
	}

	private void launchCreateNewMyItemsListPopup(Composite parent, Element inputObject) {
		if(!YRCPlatformUI.isVoid(parent))
        {
        	createNewMyItemsListPopup = new XPXCreateNewMyItemsListPopupPanel(parent, 0, inputObject);
        } else
        {
        	createNewMyItemsListPopup = new XPXCreateNewMyItemsListPopupPanel(YRCPlatformUI.getShell(), 0, inputObject);
        }
        YRCDialog dialog = new YRCDialog(createNewMyItemsListPopup, 1000, 900, YRCPlatformUI.getString("TITLE_ADD_NEW_MY_ITEMS_LIST"), null);
        dialog.open();
	}
	
	private void launchEditShareListPopup(Composite parent, Element inputObject) {
		editShareListPopup = new XPXMyItemListShareListPanel(parent, 0, inputObject);
		
        YRCDialog dialog = new YRCDialog(editShareListPopup, 1000, 900, YRCPlatformUI.getString("TITLE_EDIT_SHARE_LIST"), null);
        dialog.open();
	}
	
	private void initScreen() {
		createNewMyItemsListPopup = null;
		editShareListPopup=null;
		
	}

	@Override
	public YRCSharedTaskOutput getOutput() {
		return null;
	}

}
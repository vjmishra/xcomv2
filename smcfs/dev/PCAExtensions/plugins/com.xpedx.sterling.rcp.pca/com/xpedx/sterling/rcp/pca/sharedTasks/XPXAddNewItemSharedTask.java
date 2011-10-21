package com.xpedx.sterling.rcp.pca.sharedTasks;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.tasks.myitems.screen.XPXMyItemsListDetailsPanel;
import com.yantra.yfc.rcp.YRCDialog;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTask;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;
import com.xpedx.sterling.rcp.pca.tasks.myitems.screen.XPXAddItemPopupPanel;

public class XPXAddNewItemSharedTask extends YRCSharedTask{

	private XPXAddItemPopupPanel addIemPopupInstance;
	XPXMyItemsListDetailsPanel myItemList;
	@Override
	public void execute(Composite parent, String taskId) {
		Element inputObject = getInput();
		initScreen();
		myItemList=(XPXMyItemsListDetailsPanel) parent;
	       if(parent instanceof XPXMyItemsListDetailsPanel)
	        {
	    	   launchAddItemListPopup(parent,inputObject,myItemList);
	        }
		
		
	}

	private void launchAddItemListPopup(Composite parent, Element inputObject, XPXMyItemsListDetailsPanel myItemListref) {
		addIemPopupInstance=new XPXAddItemPopupPanel(parent, 0, inputObject,myItemListref);
	
	       YRCDialog dialog = new YRCDialog(addIemPopupInstance, 500, 300, YRCPlatformUI.getString("Add_Items"), null);
	        dialog.open();
		
	}

	@Override
	public YRCSharedTaskOutput getOutput() {
		// TODO Auto-generated method stub
		return null;
	}
	private void initScreen() {
		addIemPopupInstance = null;
		
		
	}
}

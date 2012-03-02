package com.xpedx.sterling.rcp.pca.relatedTasks.action;

import org.eclipse.jface.action.IAction;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;
import com.yantra.yfc.rcp.YRCRelatedTaskAction;

public class XPXHelpDocuments  extends YRCRelatedTaskAction  {

	private String finalQueryString;
	String encryptedUserKey = null;
	

	@Override
	public void executeTask(IAction arg0, YRCEditorInput arg1,
			YRCRelatedTask arg2) {
		// TODO Auto-generated method stub
		if(YRCPlatformUI.getConfirmation("CONFIRMATION", "MSG_KEY_Confirm_URL")){
			String url = YRCPlatformUI.getString("xpedx.help.documents");			
			XPXUtils.accessURL(url, finalQueryString);
			return;
		}
		
	}
	@Override
	protected boolean checkForErrors() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected boolean checkForModifications() {
		// TODO Auto-generated method stub
		return false;
	}
	
}

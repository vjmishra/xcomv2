package com.xpedx.sterling.rcp.pca.relatedTasks.action;

import java.net.URLEncoder;
import org.eclipse.jface.action.IAction;
import org.w3c.dom.Element;
import com.xpedx.sterling.rcp.pca.util.TripleDES;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;
import com.yantra.yfc.rcp.YRCRelatedTaskAction;

public class XPXBusinessExchangeAction extends YRCRelatedTaskAction  {

	private String finalQueryString;
	String encryptedUserKey = null;
	

	@Override
	public void executeTask(IAction arg0, YRCEditorInput arg1,
			YRCRelatedTask arg2) {
		// TODO Auto-generated method stub
		if(YRCPlatformUI.getConfirmation("CONFIRMATION", "MSG_KEY_Confirm_URL")){
			String url = YRCPlatformUI.getString("xpedx.business.exchange");			
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

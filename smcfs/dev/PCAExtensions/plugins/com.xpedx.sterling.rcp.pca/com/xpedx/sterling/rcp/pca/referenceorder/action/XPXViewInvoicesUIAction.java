package com.xpedx.sterling.rcp.pca.referenceorder.action;

import java.net.URLEncoder;
import org.eclipse.jface.action.IAction;
import org.w3c.dom.Element;
import com.xpedx.sterling.rcp.pca.util.TripleDES;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;
import com.yantra.yfc.rcp.YRCRelatedTaskAction;


public class XPXViewInvoicesUIAction extends YRCRelatedTaskAction {

	private String finalQueryString;
	String encryptedUserKey = null;
	String URLEncodedUserKey = null;
	
	


	@Override
	public void executeTask(IAction arg0, YRCEditorInput arg1,
			YRCRelatedTask arg2) {
		// TODO Auto-generated method stub
		    
			YRCPlatformUI.showInformation("Message", "Click OK to continue");
			String url = YRCPlatformUI.getString("xpedx.invoicing.url");
			Element ele = YRCPlatformUI.getUserElement();
			String userKey= ele.getAttribute("UserKey");
			TripleDES tripleDes = new TripleDES();
			
			
			try {
				encryptedUserKey = tripleDes.encrypt(userKey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            URLEncodedUserKey = URLEncoder.encode(encryptedUserKey);
            finalQueryString = "UserID="+ URLEncodedUserKey;
			XPXUtils.accessURL(url, finalQueryString);
			return;
		
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


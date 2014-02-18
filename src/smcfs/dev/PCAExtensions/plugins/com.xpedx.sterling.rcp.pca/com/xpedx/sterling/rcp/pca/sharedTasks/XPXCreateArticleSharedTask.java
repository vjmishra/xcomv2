package com.xpedx.sterling.rcp.pca.sharedTasks;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.tasks.articles.screen.XPXManageArticlePopupPanel;
import com.yantra.yfc.rcp.YRCDialog;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTask;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;

/**
 * @author sdodda
 *
 * Shared Task Manage Article.
 */
public class XPXCreateArticleSharedTask extends YRCSharedTask {

	private XPXManageArticlePopupPanel manageArticlePopup;
	
	public XPXCreateArticleSharedTask() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Composite parent, String taskId) {
		initScreen();
        Element inputObject = getInput();
      //EB-1087 When in Edit mode - modal name should be "Update Article"
        boolean isForUpdateorDelete = false;
        if(!YRCPlatformUI.isVoid(parent))
        {
        	NodeList nodeXPXArticleList	=	inputObject.getOwnerDocument().getElementsByTagName("XPXArticle");
        	if(nodeXPXArticleList!=null && nodeXPXArticleList.getLength()>0){
        		 Element xpxArticleElement = (Element)nodeXPXArticleList.item(0);
        		 String articleKey = xpxArticleElement.getAttribute("ArticleKey");
        		 if (articleKey!=null && articleKey.trim().length()>0)
        			 isForUpdateorDelete = true;        			
        	}
        	manageArticlePopup = new XPXManageArticlePopupPanel(parent, 0, inputObject);
        } else
        {
        	manageArticlePopup = new XPXManageArticlePopupPanel(YRCPlatformUI.getShell(), 0, inputObject);
        }
        YRCDialog dialog = new YRCDialog(manageArticlePopup, 600, 600, YRCPlatformUI.getString("TITLE_ADD_NEW_ARTICLE"), null);
        if(isForUpdateorDelete) 
        	dialog = new YRCDialog(manageArticlePopup, 600, 600, YRCPlatformUI.getString("TITLE_UPDATE_ARTICLE"), null);
        dialog.open();

	}

	private void initScreen() {
		manageArticlePopup = null;
		
	}

	@Override
	public YRCSharedTaskOutput getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

}

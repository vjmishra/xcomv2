package com.xpedx.sterling.rcp.pca.sharedTasks;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.tasks.catalogexport.screen.XPXManageCatalogExportPopupPanel;
import com.yantra.yfc.rcp.YRCDialog;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTask;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;

/**
 * @author vchandra
 *
 * Shared Task Manage Article.
 */
public class XPXCreateCatalogExportSharedTask extends YRCSharedTask {

	private XPXManageCatalogExportPopupPanel manageCatalogExportPopup;
	
	public XPXCreateCatalogExportSharedTask() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Composite parent, String taskId) {
		initScreen();
        Element inputObject = getInput();
        YRCDialog dialog =null;
        if(!YRCPlatformUI.isVoid(parent))
        {
        	manageCatalogExportPopup = new XPXManageCatalogExportPopupPanel(parent, 0, inputObject);
        	dialog = new YRCDialog(manageCatalogExportPopup, 530, 175, YRCPlatformUI.getString("TITLE_MANAGE_CATALOG_EXPORT"), null);
        } else
        {
        	manageCatalogExportPopup = new XPXManageCatalogExportPopupPanel(YRCPlatformUI.getShell(), 0, inputObject);
        	dialog = new YRCDialog(manageCatalogExportPopup, 530, 175, YRCPlatformUI.getString("TITLE_NEW_CATALOG_EXPORT"), null);
        }
        
        dialog.open();

	}

	private void initScreen() {
		manageCatalogExportPopup = null;
		
	}

	@Override
	public YRCSharedTaskOutput getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

}

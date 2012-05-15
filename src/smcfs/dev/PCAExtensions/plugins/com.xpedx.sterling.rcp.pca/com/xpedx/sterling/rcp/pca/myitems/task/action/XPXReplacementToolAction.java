package com.xpedx.sterling.rcp.pca.myitems.task.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.WorkbenchPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.myitems.editor.XPXReplacementToolEditor;
import com.xpedx.sterling.rcp.pca.myitems.screen.XPXMyItemsReplacementToolPanel;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCDialog;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCRelatedTask;
import com.yantra.yfc.rcp.YRCRelatedTaskAction;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXReplacementToolAction extends YRCRelatedTaskAction {

	public XPXReplacementToolAction(){
		super();
	}
	@Override
	public void executeTask(IAction arg0, YRCEditorInput arg1,
			YRCRelatedTask arg2) {
		// TODO Auto-generated method stub
		WorkbenchPart workbench = YRCDesktopUI.getCurrentPart();
        workbench.setFocus();
        
        Element eInput = YRCXmlUtils.createDocument("SearchList").getDocumentElement();
        YRCXmlUtils.setAttribute(eInput, "ReplacementToolEditorKey", System.currentTimeMillis());
        YRCEditorInput editorInput = null;
            editorInput = new YRCEditorInput(eInput, new String[] {""}, arg2.getId());

       // boolean b = YRCPlatformUI.openEditor(XPXReplacementToolEditor.ID_EDITOR, editorInput);
        //Testing For shared task
            String enterpriseKey = "xpedx";
            XPXMyItemsReplacementToolPanel popupReplacementTool  = new XPXMyItemsReplacementToolPanel(new Shell(Display.getDefault()), SWT.NONE,enterpriseKey);
    	    YRCDialog oDialog = new YRCDialog(popupReplacementTool,700,600,"TITLE_KEY_My_Items_Replacement_Tool_UI",null);
    	    oDialog.open();

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

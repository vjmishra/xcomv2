package com.xpedx.sterling.rcp.pca.referenceorder.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.part.WorkbenchPart;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.referenceorder.editor.XPXRefOrderSearchEditor;
import com.yantra.yfc.rcp.YRCAction;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXReferenceOrderSearchUIAction extends YRCAction {

	public XPXReferenceOrderSearchUIAction(){
		super();
	}
	@Override
	public void execute(IAction arg0) {
		
		WorkbenchPart workbench = YRCDesktopUI.getCurrentPart();
        workbench.setFocus();
        
        Element eInput = YRCXmlUtils.createDocument("SearchCriteria").getDocumentElement();
        YRCXmlUtils.setAttribute(eInput, "ReferenceOrderSearchEditorKey", System.currentTimeMillis());
        YRCEditorInput editorInput = null;
            editorInput = new YRCEditorInput(eInput, new String[] {""}, "");

        boolean b = YRCPlatformUI.openEditor(XPXRefOrderSearchEditor.ID_EDITOR, editorInput);
		
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

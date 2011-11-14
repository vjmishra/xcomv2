package com.xpedx.sterling.rcp.pca.user.internal.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.xpedx.sterling.rcp.pca.user.internal.screen.XPXCreateInternalUserPanel;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEditorPart;
import com.yantra.yfc.rcp.YRCPlatformUI;

public class XPXCreateUserEditor extends YRCEditorPart {
	
	public static final String ID_EDITOR = "com.xpedx.sterling.rcp.pca.user.internal.editor.XPXCreateUserEditor";
   

    private Composite pnlRoot;

	public XPXCreateUserEditor() {
		super();
		
	}

	@Override
	public Composite createPartControl(Composite arg0, String arg1) {
		YRCEditorInput input = (YRCEditorInput)getEditorInput();
        input.setTaskName(arg1);
        pnlRoot = new XPXCreateInternalUserPanel(arg0, SWT.NONE,input);
        pnlRoot.setData("name", "pnlCreateUser");
        Element userElement = input.getXml();
        getTitleKey("TITLE_Create_User", null);
        
    	return pnlRoot;
	}
	public boolean isDirty() {
        return false;
    }

	public boolean isSaveAsAllowed() {
        return false;
    }

	public void getTitleKey(String partName, String loginId)
    {
		super.setPartName(YRCPlatformUI.getFormattedString(partName, loginId));
    }
}

package com.xpedx.sterling.rcp.pca.user.internal.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.xpedx.sterling.rcp.pca.user.internal.screen.XPXCreateInternalUserPanel;
import com.xpedx.sterling.rcp.pca.user.internal.screen.XPXInternalUserSearchListScreen;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCEditorPart;

public class XPXSearchUserEditor extends YRCEditorPart {
	public static final String ID_EDITOR = "com.xpedx.sterling.rcp.pca.user.internal.editor.XPXSearchUserEditor";
	private Composite pnlRoot;
	public XPXSearchUserEditor() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public boolean isDirty() {
		return false;
	}

	public boolean isSaveAsAllowed() {

		return false;
	}
	@Override
	public Composite createPartControl(Composite arg0, String arg1) {
		// TODO Auto-generated method stub
//		YRCEditorInput input = (YRCEditorInput)getEditorInput();
//        input.setTaskName(arg1);
        pnlRoot = new XPXInternalUserSearchListScreen(arg0, SWT.NONE, arg1);
        return pnlRoot;
	}

}

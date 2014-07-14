package com.xpedx.sterling.rcp.pca.userprofile.screen;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCEditorInput;

public class UserProfileMaintenanceBehavior extends YRCBehavior {
	private Element inputElement;
	private UserProfileMaintenance page;
	public UserProfileMaintenanceBehavior(
			UserProfileMaintenance userProfileMaintenance,
			Object inputObject) {
		
		super((Composite) userProfileMaintenance, userProfileMaintenance.getFormId(), inputObject);
		this.page = userProfileMaintenance;
		this.inputElement=((YRCEditorInput) inputObject).getXml();
		initPage();
	}
	
	@Override
	public void init() {
		System.out.println("init().....");
	}

	public void initPage() {
		
	}
	
}

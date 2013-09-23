package com.sterlingcommerce.xpedx.webchannel.services;

import com.sterlingcommerce.webchannel.core.WCMashupAction;

@SuppressWarnings("serial")
public class XPEDXDivertReportFlowAction extends WCMashupAction {
	private String id; 
	private String kind;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String execute() {
		
		if (getKind().equalsIgnoreCase("Webi"))
			return "WebiInputPrompt";
		else 
			return "CrystalInputPrompt";
	}
}

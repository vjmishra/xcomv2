package com.sterlingcommerce.xpedx.webchannel.services;

import com.sterlingcommerce.webchannel.core.WCMashupAction;

@SuppressWarnings("serial")
public class XPEDXDivertInputPromptAction extends WCMashupAction {
	private String id; 
	private String cuid;
	private String kind;
	private String name;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
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

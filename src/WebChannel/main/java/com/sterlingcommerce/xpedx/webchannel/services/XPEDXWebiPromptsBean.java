package com.sterlingcommerce.xpedx.webchannel.services;

import java.util.List;
import java.util.Map;

public class XPEDXWebiPromptsBean {
	private String prefix;
	private String suffix;
	private String prmtName;
	private List<String> promptValues;
	private List<XPEDXShipAndBillSuffix> suffixList;


	public List<XPEDXShipAndBillSuffix> getSuffixList() {
		return suffixList;
	}
	public void setSuffixList(List<XPEDXShipAndBillSuffix> suffixList) {
		this.suffixList = suffixList;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public List<String> getPromptValues() {
		return promptValues;
	}
	public void setPromptValues(List<String> promptValues) {
		this.promptValues = promptValues;
	}
	public String getPrmtName() {
		return prmtName;
	}
	public void setPrmtName(String prmtName) {
		this.prmtName = prmtName;
	}
}

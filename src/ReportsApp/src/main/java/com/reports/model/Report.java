package com.reports.model;

import java.util.List;

public class Report {
	int id;
	String name;
	String description;
	String kind;
	String cuid;
	
	List<ReportPrompt> optionalPrompts;
	List<ReportPrompt> mandatoryPrompts;
		

	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getCuid() {
		return cuid;
	}
	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<ReportPrompt> getOptionalPrompts() {
		return optionalPrompts;
	}
	public void setOptionalPrompts(List<ReportPrompt> optionalPrompts) {
		this.optionalPrompts = optionalPrompts;
	}
	public List<ReportPrompt> getMandatoryPrompts() {
		return mandatoryPrompts;
	}
	public void setMandatoryPrompts(List<ReportPrompt> mandatoryPrompts) {
		this.mandatoryPrompts = mandatoryPrompts;
	}
	
	
	
	
}

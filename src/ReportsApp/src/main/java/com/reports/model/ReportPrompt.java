package com.reports.model;

import java.util.List;

public class ReportPrompt {

	String promptName;
	List<String> defaultPromptValues;
	
	public ReportPrompt() {
		
	}
	
	
	public ReportPrompt(String promptName, List<String> defaultPromptValues) {
		super();
		this.promptName = promptName;
		this.defaultPromptValues = defaultPromptValues;
	}
	
	public String getPromptName() {
		return promptName;
	}
	public void setPromptName(String promptName) {
		this.promptName = promptName;
	}
	public List<String> getDefaultPromptValues() {
		return defaultPromptValues;
	}
	public void setDefaultPromptValues(List<String> defaultPromptValues) {
		this.defaultPromptValues = defaultPromptValues;
	}
	
}

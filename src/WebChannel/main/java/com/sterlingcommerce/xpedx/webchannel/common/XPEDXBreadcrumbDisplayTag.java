package com.sterlingcommerce.xpedx.webchannel.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.opensymphony.xwork2.util.ValueStack;

//extends ComponentTagSupport 
public class XPEDXBreadcrumbDisplayTag extends ComponentTagSupport{
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		this.component = new XPEDXBreadcrumbDisplayComponent(stack, req, res, this);
		return this.component;
	}
	
	public String getDisplayRootName() {
		return this.displayRootName;
	}

	public void setDisplayRootName(String displayRootName) {
		this.displayRootName = displayRootName;
	} 

	public String getBreadcrumbSeparator() {
		return this.breadcrumbSeparator;
	}

	public void setBreadcrumbSeparator(String breadcrumbSeparator) {
		this.breadcrumbSeparator = breadcrumbSeparator;
	}

	public boolean isRemovable() {
		return this.removable;
	}

	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	public String getRemoveIcon() {
		return this.removeIcon;
	}

	public void setRemoveIcon(String removeIcon) {
		this.removeIcon = removeIcon;
	}

	public String getDefaultDisplayName() {
		return this.defaultDisplayName;
	}

	public void setDefaultDisplayName(String defaultDisplayName) {
		this.defaultDisplayName = defaultDisplayName;
	}

	public Integer getStartTabIndex() {
		return this.startTabIndex;
	}

	public void setStartTabIndex(Integer startTabIndex) {
		this.startTabIndex = startTabIndex;
	}

	

	protected String displayRootName = null;
	
	protected String defaultDisplayName = null;

	protected String breadcrumbSeparator = null;

	protected boolean removable = false;

	protected String removeIcon = null;

	protected Integer startTabIndex = null;
}

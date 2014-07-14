package com.sterlingcommerce.xpedx.webchannel.profile.user;

public class QuickLinkBean {
	private String quickLinkURL;
	private String showQuickLink;
	private String urlName;
	private int urlOrder;

	public String getQuickLinkURL() {
		return quickLinkURL;
	}

	public String getShowQuickLink() {
		return showQuickLink;
	}

	public String getUrlName() {
		return urlName;
	}

	public int getUrlOrder() {
		return urlOrder;
	}

	public void setQuickLinkURL(String quickLinkURL) {
		this.quickLinkURL = quickLinkURL;
	}

	public void setShowQuickLink(String showQuickLink) {
		this.showQuickLink = showQuickLink;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public void setUrlOrder(int urlOrder) {
		this.urlOrder = urlOrder;
	}

}

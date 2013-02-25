package com.sterlingcommerce.xpedx.webchannel.profile.user;

public class QuickLinkBean {
    private String urlName;
    private String quickLinkURL;
    private int urlOrder;
    private String showQuickLink;

    public String getUrlName() {
	return urlName;
    }

    public void setUrlName(String urlName) {
	this.urlName = urlName;
    }

    public String getQuickLinkURL() {
	return quickLinkURL;
    }

    public void setQuickLinkURL(String quickLinkURL) {
	this.quickLinkURL = quickLinkURL;
    }

    public int getUrlOrder() {
	return urlOrder;
    }

    public void setUrlOrder(int urlOrder) {
	this.urlOrder = urlOrder;
    }

    public String getShowQuickLink() {
	return showQuickLink;
    }

    public void setShowQuickLink(String showQuickLink) {
	this.showQuickLink = showQuickLink;
    }

}

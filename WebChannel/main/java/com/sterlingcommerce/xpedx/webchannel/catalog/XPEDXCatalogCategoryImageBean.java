/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.catalog;

/**
 * @author rugrani
 *
 */
public class XPEDXCatalogCategoryImageBean {
	private String imageUrl = "";
	private String categoryPath = "";
	private String cname = "";
	
	public XPEDXCatalogCategoryImageBean(String cname,String categoryPath,String imageUrl){
		this.cname = cname;
		this.categoryPath = categoryPath;
		this.imageUrl = imageUrl;
	}
	/**
	 * @return the categoryPath
	 */
	public String getCategoryPath() {
		return categoryPath;
	}
	/**
	 * @param categoryPath the categoryPath to set
	 */
	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}
	/**
	 * @return the cname
	 */
	public String getCname() {
		return cname;
	}
	/**
	 * @param cname the cname to set
	 */
	public void setCname(String cname) {
		this.cname = cname;
	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}

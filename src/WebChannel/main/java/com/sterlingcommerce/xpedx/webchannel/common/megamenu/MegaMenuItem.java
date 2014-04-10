package com.sterlingcommerce.xpedx.webchannel.common.megamenu;

import java.util.LinkedList;
import java.util.List;

/**
 * Simple data structure for mega menu items.
 */
public class MegaMenuItem {

	private String id;
	private String path;
	private String name;
	private int count;
	private String breadcrumb;
	private boolean hasBrands;
	private List<MegaMenuItem> subcategories = new LinkedList<MegaMenuItem>();

	public static MegaMenuItem create(String id, String path, String name, int count) {
		MegaMenuItem mmi = new MegaMenuItem();
		mmi.setId(id);
		mmi.setPath(path);
		mmi.setName(name);
		mmi.setCount(count);
		return mmi;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MegaMenuItem [id=").append(id).append(", path=").append(path).append(", name=").append(name).append(", count=").append(count).append(", breadcrumb=").append(breadcrumb).append(", subcategories.size=")
				.append(subcategories.size()).append("]");
		return builder.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(String breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

	public boolean isHasBrands() {
		return hasBrands;
	}

	public void setHasBrands(boolean hasBrands) {
		this.hasBrands = hasBrands;
	}

	public List<MegaMenuItem> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<MegaMenuItem> subcategories) {
		this.subcategories = subcategories;
	}

}

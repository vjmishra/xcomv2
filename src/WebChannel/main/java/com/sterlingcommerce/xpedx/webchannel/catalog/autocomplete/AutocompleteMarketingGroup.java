package com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete;

/*
 * Created on Oct 21, 2013
 */

/**
 * This reprents a Marketing Group in the autocomplete drop down. A Marketing Group is also known as a PUN (Publishing Unit Name).
 *
 * @author Trey Howard
 */
public class AutocompleteMarketingGroup {

	private String key;
	private String cat1;
	private String name;
	private String path;

	@Override
	public String toString() {
		return "Item [key=" + key + " | cat1=" + cat1 + " | name=" + name + " | path=" + path + "]";
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCat1() {
		return cat1;
	}

	public void setCat1(String cat1) {
		this.cat1 = cat1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}

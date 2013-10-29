package com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete;

import java.text.Collator;

/*
 * Created on Oct 21, 2013
 */

/**
 * This reprents a PUN (Publishing Unit Name) in the autocomplete drop down.
 * 
 * @author Trey Howard
 */
public class AutocompleteMarketingGroup implements Comparable<AutocompleteMarketingGroup> {

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

	/*
	 * Sorts by cat1, then by name.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AutocompleteMarketingGroup that) {
		int retval = Collator.getInstance().compare(this.getCat1(), that.getCat1());

		if (retval == 0) {
			retval = Collator.getInstance().compare(this.getPath(), that.getPath());
		}

		return retval;
	}

}

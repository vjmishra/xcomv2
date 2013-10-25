package com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete;

import java.text.Collator;

/*
 * Created on Oct 21, 2013
 */

/**
 * This reprents a PUN (Published Unit Name) in the autocomplete drop down.
 * 
 * @author Trey Howard
 */
public class AutocompletePun implements Comparable<AutocompletePun> {

	private String key;
	private String group;
	private String name;
	private String path;

	@Override
	public String toString() {
		return "Item [key=" + key + " | group=" + group + " | name=" + name + " | path=" + path + "]";
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
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
	 * Sorts by group, then by name.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AutocompletePun that) {
		int retval = Collator.getInstance().compare(this.getGroup(), that.getGroup());

		if (retval == 0) {
			retval = Collator.getInstance().compare(this.getPath(), that.getPath());
		}

		return retval;
	}

}

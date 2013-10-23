package com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete;

import java.text.Collator;
import java.util.Comparator;

/*
 * Created on Oct 21, 2013
 */

/**
 * @author Trey Howard
 */
public class AutocompleteItem implements Comparable<AutocompleteItem> {

	private int id;
	private String group;
	private String name;
	private String path;

	@Override
	public String toString() {
		return "Item [id=" + id + " | group=" + group + " | name=" + name + " | path=" + path + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	public int compareTo(AutocompleteItem that) {
		int retval = Collator.getInstance().compare(this.getGroup(), that.getGroup());

		if (retval == 0) {
			retval = Collator.getInstance().compare(this.getName(), that.getName());
		}

		return retval;
	}

}

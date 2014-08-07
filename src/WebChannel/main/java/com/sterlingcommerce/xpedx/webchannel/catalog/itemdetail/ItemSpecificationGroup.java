package com.sterlingcommerce.xpedx.webchannel.catalog.itemdetail;

import java.util.LinkedList;
import java.util.List;

public class ItemSpecificationGroup {

	private String name;
	private List<ItemSpecification> specifications = new LinkedList<ItemSpecification>();

	public static ItemSpecificationGroup create(String name) {
		ItemSpecificationGroup isg = new ItemSpecificationGroup();
		isg.setName(name);
		return isg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ItemSpecification> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(List<ItemSpecification> specs) {
		this.specifications = specs;
	}

	public boolean addItemSpecification(ItemSpecification e) {
		return specifications.add(e);
	}

}

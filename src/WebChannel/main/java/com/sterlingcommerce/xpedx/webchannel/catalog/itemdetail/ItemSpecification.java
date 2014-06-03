package com.sterlingcommerce.xpedx.webchannel.catalog.itemdetail;

import java.util.Comparator;

/**
 * Data structure for item specifications on the item detail page.
 * @see ItemSpecificationGroup
 */
public class ItemSpecification {

	private String description;
	private String value;
	private int sequence;

	public static Comparator<ItemSpecification> COMPARATOR_SEQUENCE = new Comparator<ItemSpecification>() {
		@Override
		public int compare(ItemSpecification o1, ItemSpecification o2) {
			return o1.getSequence() - o2.getSequence();
		}
	};

	public static ItemSpecification create(String description, String value) {
		ItemSpecification is = new ItemSpecification();
		is.setDescription(description);
		is.setValue(value);
		return is;
	}

	public static ItemSpecification create(String description, String value, int sequence) {
		ItemSpecification is = new ItemSpecification();
		is.setDescription(description);
		is.setValue(value);
		is.setSequence(sequence);
		return is;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}

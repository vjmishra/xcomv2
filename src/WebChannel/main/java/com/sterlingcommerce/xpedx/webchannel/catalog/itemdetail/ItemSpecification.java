package com.sterlingcommerce.xpedx.webchannel.catalog.itemdetail;

import java.util.Comparator;

/**
 * Data structure for item specifications on the item detail page.
 * @see ItemSpecificationGroup
 */
public class ItemSpecification {

	private String label;
	private String id;
	private String value;
	private int sequence;

	/**
	 * Sort ascending by sequence.
	 */
	public static final Comparator<ItemSpecification> COMPARATOR_SEQUENCE = new Comparator<ItemSpecification>() {
		@Override
		public int compare(ItemSpecification o1, ItemSpecification o2) {
			return o1.getSequence() - o2.getSequence();
		}
	};

	public static ItemSpecification create(String id, String label, String value, int sequence) {
		ItemSpecification bean = new ItemSpecification();
		bean.setId(id);
		bean.setLabel(label);
		bean.setValue(value);
		bean.setSequence(sequence);
		return bean;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

package com.sterlingcommerce.xpedx.webchannel.catalog.itemdetail;

import java.util.Comparator;

public class ItemListPrice {

	/**
	 * Sort ascending by cost.
	 */
	public static final Comparator<ItemListPrice> COMPARATOR_COST = new Comparator<ItemListPrice>() {
		@Override
		public int compare(ItemListPrice o1, ItemListPrice o2) {
			double cost1, cost2;
			try {
				cost1 = Double.parseDouble(o1.getCost());
			} catch (NumberFormatException e) {
				return 1;
			}
			try {
				cost2 = Double.parseDouble(o2.getCost());
			} catch (NumberFormatException e) {
				return -1;
			}
			return (int) Math.round(cost1 - cost2);
		};
	};

	public static ItemListPrice create(String unit, String cost) {
		ItemListPrice ilp = new ItemListPrice();
		ilp.setUnit(unit);
		ilp.setCost(cost);
		return ilp;
	}

	private String unit; // qty + uom (qty is optional)
	private String cost; // formatted

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

}

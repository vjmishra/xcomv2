package com.sterlingcommerce.xpedx.webchannel.catalog.itemdetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemListPrice {

	/**
	 * Sort ascending by cost.
	 */
	public static final Comparator<ItemListPrice> COMPARATOR_COST = new Comparator<ItemListPrice>() {
		@Override
		public int compare(ItemListPrice o1, ItemListPrice o2) {
			double cost1, cost2;
			try {
				cost1 = Double.parseDouble(o1.getCost().substring(1));
			} catch (NumberFormatException e) {
				return 1;
			}
			try {
				cost2 = Double.parseDouble(o2.getCost().substring(1));
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

	public static void main(String[] args) {
		List<ItemListPrice> list = new ArrayList<ItemListPrice>();

		list.add(create("10 CTN", "$12.16"));
		list.add(create("40 CTN", "$11.19"));
		list.add(create("1 CTN", "$13.55"));

		Collections.sort(list, COMPARATOR_COST);

		for (ItemListPrice ilp : list) {
			System.out.println(ilp.getUnit() + " " + ilp.getCost());
		}
	}

}

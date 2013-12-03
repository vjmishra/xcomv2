package com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/*
 * Created on Dec 2, 2013
 */

/**
 * Comparator that provides brand-specific sorting of AutocompleteMarketingGroups.
 *
 * @author Trey Howard
 */
public class AutocompleteMarketingGroupComparator implements Comparator<AutocompleteMarketingGroup> {

	private final Map<String, Integer> catWeights;

	public AutocompleteMarketingGroupComparator(String brand) {
		super();
		catWeights = new HashMap<String, Integer>();
		// hard-code the category order by brand
		if ("xpedx".equals(brand)) {
			int weight = 1;
			catWeights.put("Paper", weight++);
			catWeights.put("Facility Supplies", weight++);
			catWeights.put("Graphics", weight++);
			catWeights.put("Packaging", weight++);

		} else if ("Saalfeld".equals(brand)) {
			int weight = 1;
			catWeights.put("Facility Supplies", weight++);
			catWeights.put("Graphics", weight++);
			catWeights.put("Packaging", weight++);
			catWeights.put("Paper", weight++);

		} else {
			throw new IllegalArgumentException("Unexpected brand: " + brand);
		}
	}

	/*
	 * Sorts by cat1, then by name. Note that cat1 sorting is brand-specific.
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final AutocompleteMarketingGroup o1, final AutocompleteMarketingGroup o2) {
		int retval = 0;

		// sort cat1's per brand
		Integer o1Weight = catWeights.get(o1.getCat1());
		Integer o2Weight = catWeights.get(o2.getCat1());
		if (o1Weight == null) {
			throw new IllegalStateException("Unexpected o1.cat1: " + o1Weight);
		}
		if (o2Weight == null) {
			throw new IllegalStateException("Unexpected o2.cat1: " + o2Weight);
		}
		retval = o1Weight - o2Weight;

		if (retval == 0) {
			// secondary sort on path
			retval = Collator.getInstance().compare(o1.getPath(), o2.getPath());
		}

		return retval;
	}

}

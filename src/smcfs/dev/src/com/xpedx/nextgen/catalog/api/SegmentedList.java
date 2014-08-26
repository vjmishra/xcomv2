package com.xpedx.nextgen.catalog.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Groups elements into lists of <code>segmentSize</code>. This class is NOT thread-safe.
 */
public class SegmentedList<T> implements Iterable<List<T>> {

	private final int segmentSize;
	private List<List<T>> allBuckets = new LinkedList<List<T>>();
	private List<T> activeBucket;

	public SegmentedList(Collection<? extends T> list, int segmentSize) {
		super();
		this.segmentSize = segmentSize;
		activeBucket = new ArrayList<T>(segmentSize);
		allBuckets.add(activeBucket);
		addAll(list);
	}

	@Override
	public String toString() {
		return allBuckets.toString();
	}

	public boolean add(T elem) {
		if (activeBucket.size() >= segmentSize) {
			activeBucket = new ArrayList<T>(segmentSize);
			allBuckets.add(activeBucket);
		}
		return activeBucket.add(elem);
	}

	public boolean addAll(Collection<? extends T> elems) {
		for (T elem : elems) {
			add(elem);
		}
		return elems.size() > 0;
	}

	@Override
	public Iterator<List<T>> iterator() {
		return allBuckets.iterator();
	}

}

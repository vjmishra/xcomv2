package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.Arrays;
import java.util.List;

/**
 * O, JUnit, how I miss thee!
 */
public class TestFlexPaginationComponent {

	// --- 1 page

	private static void test_1of1() {
		int currentPage = 1;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 1);
		List<Integer> expected = Arrays.asList(new Integer[] { 1 } );
		assertEquals(expected, displayPages, currentPage);
	}

	// --- 2 pages

	private static void test_1of2() {
		int currentPage = 1;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 2);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_2of2() {
		int currentPage = 2;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 2);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2 } );
		assertEquals(expected, displayPages, currentPage);
	}

	// --- 7 pages

	private static void test_1of7() {
		int currentPage = 1;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 7);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_2of7() {
		int currentPage = 2;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 7);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_3of7() {
		int currentPage = 3;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 7);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_4of7() {
		int currentPage = 4;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 7);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_5of7() {
		int currentPage = 5;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 7);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_6of7() {
		int currentPage = 6;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 7);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_7of7() {
		int currentPage = 7;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 7);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7 } );
		assertEquals(expected, displayPages, currentPage);
	}

	// --- 12 pages

	private static void test_1of12() {
		int currentPage = 1;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 12);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, null, 12 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_2of12() {
		int currentPage = 2;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 12);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, null, 12 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_3of12() {
		int currentPage = 3;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 12);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, null, 12 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_4of12() {
		int currentPage = 4;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 12);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, null, 12 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_5of12() {
		int currentPage = 5;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 12);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, null, 4, 5, 6, null, 12 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_6of12() {
		int currentPage = 6;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 12);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, null, 5, 6, 7, null, 12 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_7of12() {
		int currentPage = 7;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 12);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, null, 6, 7, 8, null, 12 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_8of12() {
		int currentPage = 8;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 12);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, null, 7, 8, 9, null, 12 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_9of12() {
		int currentPage = 9;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 12);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, null, 8, 9, 10, 11, 12 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_10of12() {
		int currentPage = 10;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 12);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, null, 8, 9, 10, 11, 12 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_11of12() {
		int currentPage = 11;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 12);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, null, 8, 9, 10, 11, 12 } );
		assertEquals(expected, displayPages, currentPage);
	}

	private static void test_12of12() {
		int currentPage = 12;
		List<Integer> displayPages = FlexPaginationComponent.getPageNumbers(currentPage, 12);
		List<Integer> expected = Arrays.asList(new Integer[] { 1, null, 8, 9, 10, 11, 12} );
		assertEquals(expected, displayPages, currentPage);
	}

	// --- Test Harness

	private static void assertEquals(Object expected, Object actual, int currentPage) {
		if (!expected.equals(actual)) {
			System.out.println("Assertion failure for " + currentPage + ":	actual=" + actual + "		expected=" + expected);
		}
	}

	public static void main(String[] args) {
		// 1 page
		test_1of1();

		// 2 pages
		test_1of2();
		test_2of2();

		// 7 pages
		test_1of7();
		test_2of7();
		test_3of7();
		test_4of7();
		test_5of7();
		test_6of7();
		test_7of7();

		// 12 page
		test_1of12();
		test_2of12();
		test_3of12();
		test_4of12();
		test_5of12();
		test_6of12();
		test_7of12();
		test_8of12();
		test_9of12();
		test_10of12();
		test_11of12();
		test_12of12();
	}

}

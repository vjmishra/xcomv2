package com.xpedx.nextgen.common.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XPXCatalogDataProcessor {
	final static Pattern xPattern = buildXPattern();
	
	public static void main(String[] args){
		/*String[] searches = new String[] {
        		"m&c",
        		"5%",
        		"5 percent",
        		"PICK & PLUCK",
        		"PICK&PLUCK",
        		"paper",
        		"@",
        		"450 feet",
        		"7 inches",
        		"84 in",
        		"2.75\"",
        		"27in",
        		"6\"",
        		"linerboard \"brd\"",
        		"8.5",
        		"8",
        		"11",
        		"round corners",
        		"1",
        		"1.5",
        		"1 5",
        		"5.1",
        		"7",
        		"7.5",
        		"1/2\"",
        		"2-1/2",
        		"2-1/2\"",
        		"2 1/2\"",
        		"90#",
        		"90 lb",
        		"90 pounds",
        		"64x21",
        		"#9",
        		"number 9",
        		"5\'",
        		"5 foot",
        		"5\" 6\'x 4foot 7 inch",
        		"60 pounds",
        		"abcd 20'x12.54\" abcd",
        		"abcd 20x12' abcd",
        		"20'",
        		"20'x",
        		"60\"",
        		"\"6\"",
        		"2\"x2'x40\"",
        		"6\"",
        		"2\"x 4'",
        		"1.50\"x2.25\""
        		
        		
        };*/
		
		String[] searches = {"P&amp;G P &amp; G P&G P & G"};
		
		
		for (String rawSearch : searches) {
        	// TODO: insert the following where we receive the user's search query
        	// don't search on the user's raw query, preprocess it first
			String search = preprocessCatalogData(rawSearch);
        	
        	
        	System.out.println(rawSearch+"====>"+search);
		}
	}
	
	public static String preprocessCatalogData(String rawSearch) {
		String search = rawSearch;
		search = UnitInfo.preprocess(search);
		search = SymbolInfo.preprocess(search);
		search = preprocessXPatterns(search);
		search = preprocessXPatterns(search);
		return search;
	}
	
	// builds a pattern that can recognize numbers and UOMS separated by 'x'.
	private static Pattern buildXPattern() {
		StringBuilder canonicals = new StringBuilder();
		for (UnitInfo unitInfo : UnitInfo.all) {
			canonicals.append("|"+unitInfo.canonical);
		}
		return Pattern.compile("([0-9"+canonicals.toString()+"])( ?)([Xx])([0-9"+canonicals.toString()+"]?)");
	}
	
	//processes x patterns
	private static String preprocessXPatterns(String rawText){
		Matcher matcher = xPattern.matcher(rawText);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1) + " "+matcher.group(3));
			// handle cases where "x" immediately follows symbol such as in: 2.75"x 8.5"
			if (matcher.group(4).length() > 0)
				sb.append(' ').append(matcher.group(4));
		}
		matcher.appendTail(sb);
		return sb.toString();
		
	}
	/**
	 * Represents a unit that may have multiple synonymous representations in text
	 * <p>There are three attributes of a UnitInfo
	 * <ol> 
	 * <li><b>Symbol</b>: this is a character that is only recognized as a unit if it directly follows a number (with no space between)
	 * <li><b>Canonical</b>: this is the version of the unit that we will always replace with
	 * <li><b>Synonyms</b>: these are other versions of the unit that will be recognized when following numbers (even if there is a space between)
	 * </ol>
	 */
	final static class UnitInfo {
		final String symbol;
		final String canonical;
		final String[] synonyms;
		
		// TODO: Add any new units here
		final private static UnitInfo[] all = new UnitInfo[] {
			new UnitInfo("\"", "in", "ins", "inch", "inches"),
			new UnitInfo("\'", "ft", "foot", "feet"),
			new UnitInfo("#", "lb", "lbs", "pound", "pounds"),
		};
		
		final static HashMap<String,String> toCanonical = buildCanonicalMap();
		final static Pattern unitNamePattern = buildUnitNamePattern();
		final static Pattern unitSymbolPattern = buildUnitSymbolPattern();
		
		private UnitInfo(String symbol, String canonical, String ... synonyms) {
			this.symbol = symbol;
			this.canonical = canonical;
			this.synonyms = synonyms;
		}

		/**
		 * The main function.  This preprocesses the content or query to replace ambiguous symbol patterns with the canonical versions
		 * <p>Note: there are two types of patterns, symbol and name.
		 * <ul>
		 * <li>The Symbol patterns do not allow a space between the unit and the quantity (e.g. 4" not 4 ")
		 * <li>The Name patterns do allow a space between unit and quantity (e.g. 4 in and 4in are both acceptable)
		 * </ul>
		 * @param text
		 * @return
		 */
		public static String preprocess(String search) {
			search = UnitInfo.preprocessSymbolPatterns(search);
			search = UnitInfo.preprocessNamePatterns(search);
			return search;
		}

		// replaces the unit symbol patterns with their canonical forms
		private static String preprocessSymbolPatterns(String rawText) {
			Matcher matcher = unitSymbolPattern.matcher(rawText);
			StringBuffer sb = new StringBuffer();
			while (matcher.find()) {
				String matchString = matcher.group(1);
				int index = rawText.indexOf(matchString);
				index = index-1;
				if(index > -1){
					char quotesValue = rawText.charAt(index);
					if(quotesValue == '"'){
						matcher.appendTail(sb);
						return sb.toString();
					}
				}
				matcher.appendReplacement(sb, matcher.group(1) + toCanonical.get(matcher.group(3)));
				// handle cases where "x" imediately follows symbol such as in: 2.75"x 8.5"
				if (matcher.group(4).length() > 0)
					sb.append(' ').append(matcher.group(4));
			}
			matcher.appendTail(sb);
			return sb.toString();
		}
		
		// replaces the unit name patterns with the unit's canonical form
		private static String preprocessNamePatterns(String rawText) {
			Matcher matcher = unitNamePattern.matcher(rawText);
			StringBuffer sb = new StringBuffer();
			while (matcher.find())
				matcher.appendReplacement(sb, matcher.group(1) + toCanonical.get(matcher.group(3)));
			matcher.appendTail(sb);
			return sb.toString();
		}
		
		

		// builds a pattern that can recognize unit symbol usages
		private static Pattern buildUnitSymbolPattern() {
			StringBuilder symbols = new StringBuilder();
			for (UnitInfo unitInfo : UnitInfo.all) {
				symbols.append(Pattern.quote(unitInfo.symbol));
			}
			return Pattern.compile("([0-9]+(\\.[0-9]+)?)([" + symbols.toString() + "])([xX]?)");
		}
		
		// build the pattern that recognizes unit name usages
		private static Pattern buildUnitNamePattern() {
			StringBuilder synonyms = new StringBuilder();
			for (String synonym : toCanonical.keySet()) {
				if (synonyms.length() > 0)
					synonyms.append("|");
				synonyms.append(synonym);
			}
			return Pattern.compile("([0-9]+(\\.[0-9]+)?) ?(" + synonyms.toString() + ")\\b");
		}

		// builds the map from symbol or synonym to its canonical version
		private static HashMap<String,String> buildCanonicalMap() {
			HashMap<String,String> toCanonical = new HashMap<String,String>();
			for (UnitInfo unitInfo : UnitInfo.all) {
				toCanonical.put(unitInfo.symbol, unitInfo.canonical);
				toCanonical.put(unitInfo.canonical, unitInfo.canonical);
				for (String synonym : unitInfo.synonyms)
					toCanonical.put(synonym, unitInfo.canonical);
			}
			return toCanonical;
		}
		

	}
	
	/**
	 * Represents a symbol, which when found in code is always replaced with its canonical form
	 * <p>There are four attributes of a UnitInfo
	 * <ol> 
	 * <li><b>Is numeric modifier</b>: true to apply this only when immediately following numbers
	 * <li><b>Symbol</b>: The non-text symbol
	 * <li><b>Canonical</b>: The canonical form of the symbol (should be text)
	 * <li><b>Synonyms</b>: All of the different ways the symbol may be represented
	 * </ol>
	 */
	final static class SymbolInfo {
		final boolean isNumericModifier;
		final String symbol;
		final String canonical;
		final String[] synonyms;
		
		// TODO: add new non-unit symbols here
		final public static SymbolInfo[] all = new SymbolInfo[] {
			new SymbolInfo(true, "%", "percent", "pct"),
			new SymbolInfo(false, "&amp;", "and"),
			new SymbolInfo(false, "&", "and")
			//new SymbolInfo(false, "@", "at"),
		};
		
		final static HashMap<String,String> toCanonical = buildCanonicalMap();
		final static Pattern numericSymbolPattern = buildNumericSymbolPattern();
		final static Pattern nonNumericSymbolPattern = buildNonNumericSymbolPattern();
		
		
		private SymbolInfo(boolean isNumericModifier, String symbol, String canonical, String ... synonyms) {
			this.isNumericModifier = isNumericModifier;
			this.symbol = symbol;
			this.canonical = canonical;
			this.synonyms = synonyms;
		}

		/**
		 * The main function.  This preprocesses the content or query to replace ambiguous symbol patterns with the canonical versions
		 * @param text
		 * @return
		 */
		public static String preprocess(String text) {
			text = replaceNumericSymbols(text);
			text = replaceNonNumericSymbols(text);
			return text;
		}
		
		// replaces numeric symbol usages with their canonical forms
		private static String replaceNumericSymbols(String text) {
			Matcher matcher = numericSymbolPattern.matcher(text);
			StringBuffer sb = new StringBuffer();
			while (matcher.find())
				matcher.appendReplacement(sb, matcher.group(1) + toCanonical.get(matcher.group(3).trim()));
			matcher.appendTail(sb);
			return sb.toString();
		}
		
		// replaces non-numeric symbol usages with their canonical forms
		private static String replaceNonNumericSymbols(String text) {
			Matcher matcher = nonNumericSymbolPattern.matcher(text);
			StringBuffer sb = new StringBuffer();
			while (matcher.find())
				matcher.appendReplacement(sb, toCanonical.get(matcher.group(0)));
			matcher.appendTail(sb);
			return sb.toString();
		}

		// builds a pattern that recognizes non-numeric symbol usages
		private static Pattern buildNonNumericSymbolPattern() {
			StringBuilder symbols = new StringBuilder();
			for (SymbolInfo symbolInfo : all) {
				if (symbolInfo.isNumericModifier)
					continue;
				if (symbols.length() > 0)
					symbols.append('|');
				symbols.append(symbolInfo.symbol);
				for (String synonym : symbolInfo.synonyms)
					symbols.append('|').append(synonym);
			}
			return Pattern.compile("(" + symbols + ")");
		}

		// builds a pattern that recognizes numeric symbol usages
		private static Pattern buildNumericSymbolPattern() {
			StringBuilder symbols = new StringBuilder();
			for (SymbolInfo symbolInfo : all) {
				if (!symbolInfo.isNumericModifier)
					continue;
				if (symbols.length() > 0)
					symbols.append('|');
				symbols.append(Pattern.quote(symbolInfo.symbol));
			}
			StringBuilder synonyms = new StringBuilder();
			for (SymbolInfo symbolInfo : all) {
				if (!symbolInfo.isNumericModifier)
					continue;
				synonyms.append(symbolInfo.canonical);
				for (String synonym : symbolInfo.synonyms)
					synonyms.append('|').append(synonym);
			}
			return Pattern.compile("([0-9]+(\\.[0-9]+)?)((" + symbols + ")| ?(" + synonyms + "))");
		}
		
		// builds the map from symbol or synonym to its canonical version
		private static HashMap<String,String> buildCanonicalMap() {
			HashMap<String,String> map = new HashMap<String,String>();
			for (SymbolInfo symbolInfo : all) {
				map.put(symbolInfo.symbol, symbolInfo.canonical);
				map.put(symbolInfo.canonical, symbolInfo.canonical);
				for (String synonym : symbolInfo.synonyms)
					map.put(synonym, symbolInfo.canonical);
			}
			return map;
		}

	}

}

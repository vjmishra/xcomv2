package com.xpedx.nextgen.dashboard;

import java.util.Comparator;

import org.w3c.dom.Element;

public class XpedxPrimeLineNoComparator implements Comparator<Element> {
	
	@Override
	public int compare(Element elem, Element elem1) {		
		//sort on PrimeLineNo
		int primeLineNumber = Integer.parseInt(elem.getAttribute("PrimeLineNo")); 
		int primeLineNumber1 = Integer.parseInt(elem1.getAttribute("PrimeLineNo"));
		
		//Also considering SubLineNo (in case of copy/paste order line scenario)
		int subLineNumber = Integer.parseInt(elem.getAttribute("SubLineNo")); 
		int subLineNumber1 = Integer.parseInt(elem1.getAttribute("SubLineNo"));
		
		if(primeLineNumber==primeLineNumber1 && subLineNumber > subLineNumber1)
			return 1;
		
		return primeLineNumber-primeLineNumber1;		
		
	}

}

package com.xpedx.nextgen.customermanagement.api;

import java.util.Comparator;

import org.w3c.dom.Element;

import edu.emory.mathcs.backport.java.util.Collections;

public class XpedxAvailableLocationComparator implements Comparator<Element>  {
	@Override
		public int compare(Element elem, Element elem1) {		
			String customerPath1 = elem.getAttribute("CustomerPath"); 
			String customerPath2 = elem1.getAttribute("CustomerPath");
			
			return 		customerPath1.compareTo(customerPath2);
			
		}
	
}

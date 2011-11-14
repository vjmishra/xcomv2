package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.util.Comparator;

import org.w3c.dom.Element;

public class XPEDXSortingListPrice implements Comparator<Element> {

	@Override
	public int compare(Element elem, Element elem1) {		
		
			int fromQuantity =Integer.parseInt(elem.getAttribute("FromQuantity"));
			int fromQuantity1 =Integer.parseInt(elem1.getAttribute("FromQuantity"));
			
			if(fromQuantity >fromQuantity1){
				return 1;
			}
			else if(fromQuantity < fromQuantity1){
				return -1;
			}
			else 
				return 0;
		}
	}


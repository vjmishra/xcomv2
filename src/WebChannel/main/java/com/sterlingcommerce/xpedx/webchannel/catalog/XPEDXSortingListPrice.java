package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.util.Comparator;

import org.w3c.dom.Element;

import com.yantra.yfc.util.YFCCommon;

public class XPEDXSortingListPrice implements Comparator<Element> {

	@Override
	public int compare(Element elem, Element elem1) {		
		if(!YFCCommon.isVoid(elem.getAttribute("FromQuantity")) && !YFCCommon.isVoid(elem1.getAttribute("FromQuantity"))){
			float fromQuantity =Float.parseFloat(elem.getAttribute("FromQuantity"));
			float fromQuantity1 =Float.parseFloat(elem1.getAttribute("FromQuantity"));
			
			if(fromQuantity >fromQuantity1){
				return 1;
			}
			else if(fromQuantity < fromQuantity1){
				return -1;
			}	
		}
		return 0;
	}
}

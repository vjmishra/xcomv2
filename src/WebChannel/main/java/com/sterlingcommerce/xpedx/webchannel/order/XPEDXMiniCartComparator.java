package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.Comparator;

import org.w3c.dom.Element;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXMiniCartComparator implements Comparator<Element> {

	@Override
	public int compare(Element elem, Element elem1) {

		String elemOrderLineKey = elem.getAttribute("OrderLineKey");
		String elem1OrderLineKey = elem1.getAttribute("OrderLineKey");
		
		if(YFCCommon.isVoid(elemOrderLineKey)){
			return 1;
		}
		if(YFCCommon.isVoid(elem1OrderLineKey)){
			return -1;
		}
		
		double legacyLineNo = Double.parseDouble(elemOrderLineKey);
		double legacyLineNo1 = Double.parseDouble(elem1OrderLineKey);
		if(legacyLineNo > legacyLineNo1)
			return -1;
		else if(legacyLineNo < legacyLineNo1)
			return 1;
		else
			return 0;
		
	}

}

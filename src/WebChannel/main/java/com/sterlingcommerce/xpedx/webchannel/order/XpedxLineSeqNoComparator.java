package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.Comparator;

import org.w3c.dom.Element;

import com.yantra.yfc.util.YFCCommon;

public class XpedxLineSeqNoComparator implements Comparator<Element> {

	@Override
	public int compare(Element elem, Element elem1) {
		Element extnElem = (Element)elem.getElementsByTagName("Extn").item(0);
		Element extnElem1 = (Element)elem1.getElementsByTagName("Extn").item(0);
		String elemExtnLegacyOrderNo = extnElem.getAttribute("ExtnLegacyLineNumber");
		String elem1ExtnLegacyOrderNo = extnElem1.getAttribute("ExtnLegacyLineNumber");
		if(YFCCommon.isVoid(elemExtnLegacyOrderNo) && YFCCommon.isVoid(elem1ExtnLegacyOrderNo)){
		//sort on prime line number PrimeLineNo
		int primeLineNumber = Integer.parseInt(elem.getAttribute("PrimeLineNo")); 
		int primeLineNumber1 = Integer.parseInt(elem1.getAttribute("PrimeLineNo"));
		return primeLineNumber-primeLineNumber1;
		}
		if(YFCCommon.isVoid(elemExtnLegacyOrderNo)){
		return 1;
		}
		if(YFCCommon.isVoid(elem1ExtnLegacyOrderNo)){
		return -1;
		}
		int legacyLineNo=Integer.parseInt(elemExtnLegacyOrderNo);
		int legacyLineNo1=Integer.parseInt(elem1ExtnLegacyOrderNo);
		if(legacyLineNo > legacyLineNo1)
			return 1;
		else if(legacyLineNo < legacyLineNo1)
			return -1;
		else
			return 0;
		//int op = elemExtnLegacyOrderNo.compareTo(elem1ExtnLegacyOrderNo);
		//return op;
	}

	

}

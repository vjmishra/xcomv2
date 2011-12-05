package com.xpedx.sterling.rcp.pca.util;

import java.util.Comparator;

import org.w3c.dom.Element;

import com.yantra.yfc.rcp.YRCPlatformUI;

public class XPXLineSeqNoComparator implements Comparator<Element> {

	//@Override
	public int compare(Element elem, Element elem1) {
		Element extnElem = (Element)elem.getElementsByTagName("Extn").item(0);
		Element extnElem1 = (Element)elem1.getElementsByTagName("Extn").item(0);
		String elemExtnLegacyOrderNo = extnElem.getAttribute("ExtnLegacyLineNumber");
		String elem1ExtnLegacyOrderNo = extnElem1.getAttribute("ExtnLegacyLineNumber");
		if(YRCPlatformUI.isVoid(elemExtnLegacyOrderNo) && YRCPlatformUI.isVoid(elem1ExtnLegacyOrderNo)){
		//sort on prime line number PrimeLineNo
		int primeLineNumber = Integer.parseInt(elem.getAttribute("PrimeLineNo")); 
		int primeLineNumber1 = Integer.parseInt(elem1.getAttribute("PrimeLineNo"));
		return primeLineNumber-primeLineNumber1;
		}
		if(YRCPlatformUI.isVoid(elemExtnLegacyOrderNo)){
		return 1;
		}
		if(YRCPlatformUI.isVoid(elem1ExtnLegacyOrderNo)){
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
//		int op = elemExtnLegacyOrderNo.compareTo(elem1ExtnLegacyOrderNo);
//		return op;
	}

	

}

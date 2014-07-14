package com.sterlingcommerce.xpedx.webchannel.common;


import java.util.Comparator;

import org.w3c.dom.Element;

import com.yantra.util.YFCUtils;

public class  XpedxSortUOMListByConvFactor implements Comparator<Element>
{
	public int compare(Element elem, Element elem1) {		
		double fromQuantity =0;
		double fromQuantity1 = 0;
		if(!YFCUtils.isVoid(elem.getAttribute("Conversion"))){
			 
			fromQuantity = Double.valueOf(elem.getAttribute("Conversion"));	
		}
		
		if(!YFCUtils.isVoid(elem1.getAttribute("Conversion"))){
			 
			fromQuantity1 = Double.valueOf(elem1.getAttribute("Conversion"));
			
		}
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
	
	


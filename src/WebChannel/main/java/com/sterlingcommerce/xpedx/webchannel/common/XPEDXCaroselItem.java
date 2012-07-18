package com.sterlingcommerce.xpedx.webchannel.common;
/**
 * Created to Simplify Carousel Item display.
 * 
 * @author reddypur
 *
 */
import org.apache.log4j.Logger;
public class XPEDXCaroselItem {
	//Sample Image File : https://content.ipaper.com/ProductImages/I2130545_small.jpg
	
	//CONSTANTS
	public static final String CONTENT_SERVER  = "https://content.ipaper.com";
	public static final String CONTENT_LOCATION = CONTENT_SERVER + "/ProductImages/";
	public static final String IMAGE_NOTON_FILE  = "/swc/xpedx/images/INF_150x150.jpg";
	private static final Logger log = Logger.getLogger(XPEDXCaroselItem.class);
	
	//Attributes
	String carouselItemId = "";
	String carouselItemUOM = "";
	String carouselItemShortDesc = "";
	String carouselItemImageName = "";
	String carouselItemImageURL = "";
	
	
	
	

	@Override
	public String toString() {
		String msg =  "XPEDXCaroselItem [carouselItemId=" + carouselItemId
				+ ", carouselItemUOM=" + carouselItemUOM
				+ ", carouselItemShortDesc=" + carouselItemShortDesc
				+ ", carouselItemImageName=" + carouselItemImageName
				+ ", carouselItemImageURL=" + carouselItemImageURL + "]";
		return msg;
	}
	
	public String getCarouselItemId() {
		return carouselItemId;
	}
	public void setCarouselItemId(String carouselItemId) {
		this.carouselItemId = carouselItemId;
	}
	public String getCarouselItemUOM() {
		return carouselItemUOM;
	}
	public void setCarouselItemUOM(String carouselItemUOM) {
		this.carouselItemUOM = carouselItemUOM;
	}
	public String getCarouselItemShortDesc() {
		if(carouselItemShortDesc == null || carouselItemShortDesc.length() == 0)
			return carouselItemId + " " + carouselItemUOM;
		else
			return carouselItemShortDesc;
	}
	public void setCarouselItemShortDesc(String carouselItemShortDesc) {
		
		this.carouselItemShortDesc = carouselItemShortDesc;
	}

	public String getCarouselItemImageName() {
		
		return carouselItemImageName;
	}
	public void setCarouselItemImageName(String carouselItemImageName) {
		this.carouselItemImageName = carouselItemImageName;
	}
	
	public String getCarouselItemImageURL() {
		if(carouselItemImageName == null || carouselItemImageName.length() == 0)
			return IMAGE_NOTON_FILE;
		else
			return CONTENT_LOCATION+carouselItemImageName;
	}
	
	
	public static void main (String args[]) {
		String commaQty = "1,234,323" ;
		if(log.isDebugEnabled()){
		log.debug("stripCommas(commaQty) : " + stripCommas(commaQty) );
		}
	}

	public static String stripCommas(String commaQty) {
		return commaQty.replaceAll(",", "");
	}



}

/**
 * This class holds the Bracket bean of Price and Availability
 */
package com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability;

/**
 * @author rugrani
 *
 */
public class XPEDXBracket {
	private String bracketQTY;
	private String bracketUOM;
	private String bracketPrice;
	
	public XPEDXBracket() {
		super();
	}
	
	/**
	 * @param bracketQTY
	 * @param bracketUOM
	 * @param bracketPrice
	 */
	public XPEDXBracket(String bracketQTY, String bracketUOM, String bracketPrice) {
		super();
		this.bracketQTY = bracketQTY;
		this.bracketUOM = bracketUOM;
		this.bracketPrice = bracketPrice;
	}
	
	/**
	 * @return the bracketPrice
	 */
	public String getBracketPrice() {
		return bracketPrice;
	}
	/**
	 * @param bracketPrice the bracketPrice to set
	 */
	public void setBracketPrice(String bracketPrice) {
		this.bracketPrice = bracketPrice;
	}
	/**
	 * @return the bracketQTY
	 */
	public String getBracketQTY() {
		return bracketQTY;
	}
	/**
	 * @param bracketQTY the bracketQTY to set
	 */
	public void setBracketQTY(String bracketQTY) {
		this.bracketQTY = bracketQTY;
	}
	/**
	 * @return the bracketUOM
	 */
	public String getBracketUOM() {
		return bracketUOM;
	}
	/**
	 * @param bracketUOM the bracketUOM to set
	 */
	public void setBracketUOM(String bracketUOM) {
		this.bracketUOM = bracketUOM;
	}
}

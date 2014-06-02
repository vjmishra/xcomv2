package com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability;

import java.util.ArrayList;
import java.util.List;

public class XPEDXItemPricingInfo {

	private List<XPEDXBracket> displayPriceForUoms;
	private List bracketsPricingList;
	private String isBracketPricing;
	private String priceCurrencyCode;
	private String categoryPath;

	public XPEDXItemPricingInfo()
	{
		displayPriceForUoms = new ArrayList<XPEDXBracket>();
	}

	public List<XPEDXBracket> getDisplayPriceForUoms() {
		return displayPriceForUoms;
	}

	public void setDisplayPriceForUoms(List<XPEDXBracket> displayPriceForUoms) {
		this.displayPriceForUoms = displayPriceForUoms;
	}

	public List getBracketsPricingList() {
		return bracketsPricingList;
	}

	public void setBracketsPricingList(List bracketsPricingList) {
		this.bracketsPricingList = bracketsPricingList;
	}

	public String getIsBracketPricing() {
		return isBracketPricing;
	}

	public void setIsBracketPricing(String isBracketPricing) {
		this.isBracketPricing = isBracketPricing;
	}

	public String getPriceCurrencyCode() {
		return priceCurrencyCode;
	}

	public void setPriceCurrencyCode(String priceCurrencyCode) {
		this.priceCurrencyCode = priceCurrencyCode;
	}

	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}

}

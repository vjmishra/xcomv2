package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItem;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceAndAvailability;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsGetAvailAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsDetailsGetAvailAction.class);
	
	private HashMap<String, JSONObject> pnaHoverMap;

	private String itemId 			= "";
	private String itemUom 			= "";

	@Override
	public String execute() {
		
		getPandA(getItemId(), getItemUom());
		
		return SUCCESS;
	}
	
	public String getItemId() {
		String res = itemId;
		try {
			if (res == null){
				res = "INVALID_ITEM";
			} else {
				if (res.trim().length() == 0){
					res = "INVALID_ITEM";
				}
			}
		} catch (Exception e) {
			res = "ERROR_INVALID_ITEM";
		}
		return res;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemUom() {
		return itemUom;
	}
	public void setItemUom(String itemUom) {
		this.itemUom = itemUom;
	}
	
	public XPEDXPriceAndAvailability getPandA(String prodId, String prodUom){
		XPEDXPriceAndAvailability res = null;
		try {
			
			prodId = "ITEM1";
			prodUom = "EACH";
			ArrayList<XPEDXItem> inputItems =new ArrayList<XPEDXItem>();
			XPEDXItem item = new XPEDXItem();
			item.setLegacyProductCode(prodId);
			item.setRequestedQtyUOM(prodUom);
			inputItems.add(item);
			res = XPEDXPriceandAvailabilityUtil.getPriceAndAvailability(inputItems);
			pnaHoverMap = XPEDXPriceandAvailabilityUtil.getPnAHoverMap(res.getItems());
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			res = null;
		}
		return res;
	}

	public HashMap<String, JSONObject> getPnaHoverMap() {
		return pnaHoverMap;
	}

	public void setPnaHoverMap(HashMap<String, JSONObject> pnaHoverMap) {
		this.pnaHoverMap = pnaHoverMap;
	}
}


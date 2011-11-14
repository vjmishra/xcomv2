package com.sterlingcommerce.xpedx.webchannel.home;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.CommonCodeDescriptionType;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.utilities.CommonCodeUtil;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

public class XPEDXCatalogHomePageAssetWidgetAction extends WCAction {

	public XPEDXCatalogHomePageAssetWidgetAction()
	{
		
	}
	
	public String execute()
	{
		try {
			this.assetURL = XPEDXWCUtils.getCategoryDomainAssetContent(getAssetId());
			String codeType = "B2BTourUrl";
			Map commonCodes = CommonCodeUtil.getCommonCodes(codeType,CommonCodeDescriptionType.SHORT, wcContext);
			if(!commonCodes.isEmpty())
				tourURL = (String)commonCodes.values().iterator().next();
			else
				tourURL = null;
			
		} catch (CannotBuildInputException e) {
			log.error("Unable to get the TourB2B asset path...: "+e);
			this.assetURL = null;
		} catch (XPathExpressionException xpe)
		{
			log.error("Unable to get the B2BTourUrl common code...: " + xpe);
			this.assetURL = null;
		}
		
		
		return SUCCESS;
	}
	
	public String getCatalogPromotions()
	{
		try {
			String codeType = null;
			if(wcContext.isGuestUser())
			{
				codeType = "Promotions";
			}
			else
			{
				codeType = "UserPromotions";
			}
			promotionItems = XPEDXWCUtils.getPromotionsList(getWCContext(), codeType);

		}catch (Exception e) {
			log.error("Unable to get the promotion item details...: "+e);
		} 
		return SUCCESS;
	}
	
	public Element getAsset() {
		return asset;
	}

	public void setAsset(Element asset) {
		this.asset = asset;
	}

	public String getAssetURL() {
		return assetURL;
	}

	public void setAssetURL(String assetURL) {
		this.assetURL = assetURL;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	
	public String getTourURL() {
		return tourURL;
	}

	public void setTourURL(String tourURL) {
		this.tourURL = tourURL;
	}
	
	public List getPromotionItems() {
		return promotionItems;
	}

	public void setPromotionItems(List promotionItems) {
		this.promotionItems = promotionItems;
	}

	List promotionItems = null;
	private Element asset;
	private String assetURL;
	private String tourURL;
	private String assetId;
	private static final String SUCCESS = "success";
	private static final Logger log = Logger
	.getLogger(XPEDXCatalogHomePageAssetWidgetAction.class);
}

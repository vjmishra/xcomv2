/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.w3c.dom.Element;

import com.opensymphony.xwork2.util.TextUtils;
import com.opensymphony.xwork2.util.ValueStack;
import com.sterlingcommerce.webchannel.compat.SCXmlUtils;
import com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

/**
 * @author vgovindan
 * This class takes care of building JS objects with the items data for the templates in Catalog search results pages.
 */
public class XPEDXItemsDataTemplateComponent extends Component {

	
	public XPEDXItemsDataTemplateComponent(ValueStack stack, HttpServletRequest req,
            HttpServletResponse res, XPEDXItemsDataTemplateTag tag) {
		super(stack);
		this.tag = tag;
		this.req = req;
	}

	@Override
	public boolean end(Writer writer, String body) {
		
		StringBuffer sb = new StringBuffer();
		
		Element item = (Element) findValue(tag.getItemElement(), Element.class);
		int tabidx = 101;		
		SCXmlUtils xmlUtils = XPEDXSCXmlUtils.getInstance();
		Element info = xmlUtils.getChildElement(item, "PrimaryInformation");
		String itemID = validate(item.getAttribute("ItemID"));
		HashMap<String, String> itemUOMList = tag.getItemUomHashMap().get(itemID);
		itemUOMList = itemUOMList == null ? new HashMap<String, String>() : itemUOMList;
		String defaultUOM = validate(tag.getDefaultShowUOMMap().get(itemID));	
		String shortDesc = validate(info.getAttribute("ShortDescription"));
		String desc = validate(info.getAttribute("Description"));
		String itemKey = validate(item.getAttribute("ItemKey"));
		String kitCode = validate(info.getAttribute("KitCode"));
		String unitOfMeasure = validate(item.getAttribute("UnitOfMeasure"));
		Element price = xmlUtils.getChildElement(item, "ComputedPrice");
		String myPrice = "";
		if("BUNDLE".equals(kitCode))
			myPrice = validate(price.getAttribute("BundleTotal"));
		else 
			myPrice = validate(price.getAttribute("UnitPrice"));
		String pImg = (String)findValue("pImg");
		String imageMainURL = validate(xmlUtils.getAttribute(info,"ImageLocation")) + "/" + validate(info.getAttribute("ImageID"));
		if(!"/".equals(imageMainURL)) {
			if (imageMainURL.startsWith("/"))
				pImg = pImg.substring(0, pImg.indexOf("/", 1)) + imageMainURL;
			else
				pImg = pImg.substring(0, pImg.indexOf("/", 1)) + "/" + imageMainURL;
		}
		
		
		
		HashMap<String, String> skuMap = tag.getItemMap().get("ItemID");
		skuMap = skuMap == null ? new HashMap<String, String>() : skuMap;
		XPEDXItemBranchInfoBean itemBranchBean = tag.getItemToItemBranchBeanMap().get("ItemID");
		itemBranchBean = itemBranchBean == null ? new XPEDXItemBranchInfoBean("","","","","","","","") : itemBranchBean;
		String orderMultiple = validate(tag.getOrderMultipleMap().get(itemID));		
		Element b2cItemExtn = xmlUtils.getChildElement(item, "Extn");
		String b2cSize = validate(b2cItemExtn.getAttribute("ExtnSize"));
		String b2cColor = validate(b2cItemExtn.getAttribute("ExtnColor"));
		String b2cMwt = validate(b2cItemExtn.getAttribute("ExtnMwt"));
		String b2cBasis = validate(b2cItemExtn.getAttribute("ExtnBasis"));
		String b2cCert = validate(b2cItemExtn.getAttribute("ExtnCert"));
		String b2cVendorNumber = validate(b2cItemExtn.getAttribute("ExtnVendorNo"));
		//String b2cLegacyId = validate(b2cItemExtn.getAttribute("ExtnLegacyId"));
		String b2cMaterial = validate(b2cItemExtn.getAttribute("ExtnMaterial"));
		String b2cForm = validate(b2cItemExtn.getAttribute("ExtnForm"));
		String b2cCapacity = validate(b2cItemExtn.getAttribute("ExtnCapacity"));
		String b2cModel = validate(b2cItemExtn.getAttribute("ExtnModel"));
		//String b2cGuage = validate(b2cItemExtn.getAttribute("ExtnGauge"));
		String b2cPly = validate(b2cItemExtn.getAttribute("ExtnPly"));
		String b2cstockStatus = validate(itemBranchBean.getInventoryIndicator());
		//String isSuperseded = validate(item.getAttribute("IsItemSuperseded"));
		//String isValid = validate(info.getAttribute("IsValid"));
		//String isModelItem = validate(info.getAttribute("IsModelItem"));
		//String isConfigurable = validate(info.getAttribute("IsConfigurable"));
		//String isPreConfigured = validate(info.getAttribute("IsPreConfigured"));
		//String isPickupable = validate(info.getAttribute("IsPickupAllowed"));
		//String isShippable = validate(info.getAttribute("IsShippingAllowed"));
		//String configurationKey = validate(info.getAttribute("ConfigurationKey"));
		desc = XPEDXWCUtils.trimItemDescription(desc);
		String deschtml = XPEDXMyItemsUtils.formatEscapeCharactersHtml(validate(desc));
		/*String isConfigurableBundle = "N";
		if("BUNDLE".equals(kitCode) && "Y".equals(isConfigurable))
			isConfigurableBundle = "Y";
		*/
		String itemCurrency = validate((String) findValue(tag.getCurrency()));
		boolean isGuestUser = (Boolean)findValue("guestUser");
		
		XPEDXUtilBean utilBean = new XPEDXUtilBean();
		String formattedUnitprice = validate(utilBean.formatPriceWithCurrencySymbol(tag.getCtx(),itemCurrency,myPrice));
		boolean hasTier = false;
		
		sb.append("itemid: \"").append(itemID).append("\",");
		sb.append("itemkey: \"").append(itemKey).append("\",");
		sb.append("uom: \"").append(TextUtils.htmlEncode(unitOfMeasure)).append("\",");
		sb.append("name: \"").append(XPEDXMyItemsUtils.formatEscapeCharactersHtml(shortDesc)).append("\",");
		sb.append("listprice: \"");
			
		String tierPrice = "";
		List<Element> tierPriceList = tag.getPLLineMap().get(itemID);
		if(tierPriceList != null) {
			for (Element tierPrices : tierPriceList) {
				String tierQty = tierPrices.getAttribute("FromQuantity");
				tierQty = (tierQty == null || "".equals(tierQty)) ? "1" : tierQty;
				hasTier = true;
				tierPrice = validate(tierPrices.getAttribute("ListPrice"));
				Element extnTierPrice = xmlUtils.getChildElement(tierPrices, "Extn");
				String tierPriceUOM = validate(extnTierPrice.getAttribute("ExtnTierUom"));
				String PriceUOM = validate(extnTierPrice.getAttribute("ExtnPricingUom"));
				String formattedTierUnitprice = validate(utilBean.formatPriceWithCurrencySymbol(tag.getCtx(),itemCurrency,tierPrice));
				if(formattedTierUnitprice == null || "".equals(formattedTierUnitprice))
					sb.append("<br/>");
				else {
					sb.append(TextUtils.htmlEncode(com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils.getFormattedQty(tierQty)));
					sb.append("&nbsp;");
					try {
						sb.append(TextUtils.htmlEncode(com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils.getFormattedUOMCode(tierPriceUOM))).append("-");
					} catch (Exception e) {
						sb.append(TextUtils.htmlEncode(tierPriceUOM)).append("-");
					} 
					sb.append(formattedTierUnitprice).append("/");
					try {
						sb.append(TextUtils.htmlEncode(com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils.getFormattedUOMCode(PriceUOM)));
					} catch (Exception e) {
						sb.append(TextUtils.htmlEncode(PriceUOM));
					} 
					sb.append("<br/>");
				}
			}
		}
		
		if(hasTier == false && !"".equals(formattedUnitprice)) {
			sb.append(formattedUnitprice).append(" /");
			try {
				sb.append(TextUtils.htmlEncode(com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils.getFormattedUOMCode(unitOfMeasure)));
			} catch (Exception e) {
				sb.append(TextUtils.htmlEncode(unitOfMeasure));
			} 
		}
		sb.append("\",");
		sb.append("icon: \"").append(pImg).append("\",");
		//sb.append("icon: \"").append("https://www.xpedx.com/swc/xpedx/images/INF_150x150.jpg").append("\",");
		sb.append("tabidx: \"").append(tabidx).append("\",");
		
		String uomDesc = "";
		try {
			uomDesc = TextUtils.htmlEncode(com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils.getUOMDescription(unitOfMeasure));
			sb.append("uomDesc: \"").append(uomDesc).append("\",");
		} catch (Exception e) {
			sb.append("uomDesc: \"\",");
		}
		//modified for jira 3253 to format quantity of ordermultiple
		if (isGuestUser == false) {
			if (Integer.parseInt(orderMultiple) > 1) {
				sb.append("uomLink: \"")
						.append("<div class=\\\"notice\\\" style=\\\"margin-right:5px; font-weight: normal;float:right; display:inline;\\\">")
						.append(tag.getOrderMultipleString()).append(com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean.formatQuantityForCommas(orderMultiple))
						.append(" ")
						.append(uomDesc).append("</div>\",");
			} else {
				sb.append("uomLink: \"\",");
			}

		}
		
		sb.append("price: \"").append(TextUtils.htmlEncode(tierPrice)).append("\",");
		tabidx++;
		sb.append("buttons: \"").append("<ul id=\\\"prodlist\\\">").append(deschtml).append("</ul>\",");
		sb.append("material: \"").append(TextUtils.htmlEncode(b2cMaterial)).append("\",");
		sb.append("form: \"").append(TextUtils.htmlEncode(b2cForm)).append("\",");
		sb.append("capacity: \"").append(TextUtils.htmlEncode(b2cCapacity)).append("\",");
		sb.append("model: \"").append(TextUtils.htmlEncode(b2cModel)).append("\",");
		sb.append("ply: \"").append(TextUtils.htmlEncode(b2cPly)).append("\",");
		sb.append("size: \"").append(TextUtils.htmlEncode(b2cSize)).append("\",");
		sb.append("color: \"").append(TextUtils.htmlEncode(b2cColor)).append("\",");
		sb.append("basis: \"").append(TextUtils.htmlEncode(b2cBasis)).append("\",");
		sb.append("mwt: \"").append(TextUtils.htmlEncode(b2cMwt)).append("\",");
		sb.append("vendorNumber: \"").append(TextUtils.htmlEncode(b2cVendorNumber)).append("\",");
		sb.append("stocked: \"");
		if(!"W".equals(b2cstockStatus)) {
			sb.append("M");
		}
		sb.append("\",");
		sb.append("cert: \"");
		if("Y".equals(b2cCert)) {
			sb.append("<span><img width=\\\"20\\\" height=\\\"20\\\" src='<s:url value='/xpedx/images/catalog/green-e-logo_small.png'/>'</span>");
		}
		sb.append("\",");
		sb.append("qtyGreaterThanZeroMsg: \"").append(tag.getQtyString()).append("\",");
		sb.append("partno: \"");
		
		String custUserSKU = (String)tag.getWcContext().getWCAttribute("customerUseSKU");
		if("2".equals(custUserSKU)) {
			sb.append(com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants.MANUFACTURER_ITEM_LABEL)
				.append(": ").append(TextUtils.htmlEncode(validate(skuMap.get("MPN"))));
		} else if("3".equals(custUserSKU)) {
			sb.append(com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants.MPC_ITEM_LABEL)
			.append(": ").append(TextUtils.htmlEncode(validate(skuMap.get("MPC"))));
		} else if("1".equals(custUserSKU)) {
			sb.append(com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants.CUSTOMER_ITEM_LABEL)
			.append(": ").append(TextUtils.htmlEncode(validate(skuMap.get("CPN"))));
		}
		sb.append("\",");
		
		sb.append("uomdisplay: \"<div class=\'uom-select\'><select name='itemUomList' id='itemUomList_")
			.append(itemID).append("'>");
		for (Iterator<Map.Entry <String, String>> iterator = itemUOMList.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry  pair = (Map.Entry ) iterator.next();
			sb.append("<option value='").append(pair.getKey()).append("'");
			if(pair.getValue().toString().equals(defaultUOM))
				sb.append(" selected='selected' ");
			sb.append(">").append(TextUtils.htmlEncode(pair.getValue().toString())).append("</option>");
		}
		sb.append("</select><input type='hidden' id='orderMultiple_");
		sb.append(itemID).append("' value='");
		sb.append(orderMultiple).append("'/></div>\",");
		sb.append("itemtypedesc: \"");
		if(!"W".equals(b2cstockStatus) && isGuestUser == false) {
			sb.append("<div class=\'mill-mfg\'>Mill / Mfg. Item<span class=\'addl-chg\'> - Additional charges may apply</span></div>");
		}
		sb.append("\",");
		sb = parseData(sb);
		
		try {
			writer.append((CharSequence) sb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.end(writer, body);
	}
	
	private String validate(String str) {
		return (str==null ? "" : str);
	}
	
	/**
	 * The data in SB is used by the javascript. Use this method to test and parse any 
	 * invalid character in here.
	 * @param sb
	 * @return
	 */
	private StringBuffer parseData(StringBuffer sb) {
		int startIndex = -1;
		while((startIndex = sb.indexOf("//")) >= 0) {
			sb = sb.replace(startIndex, (startIndex+2), "/");
		}
		return sb;
	}
	
	XPEDXItemsDataTemplateTag tag;
	HttpServletRequest req;

}

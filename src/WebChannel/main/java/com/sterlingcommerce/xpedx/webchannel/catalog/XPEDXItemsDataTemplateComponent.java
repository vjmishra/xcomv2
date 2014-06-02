/**
 *
 */
package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
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
import com.yantra.yfc.util.YFCCommon;

/**
 * @author vgovindan
 * This class takes care of building JS objects with the items data for the templates in Catalog search results pages.
 */
public class XPEDXItemsDataTemplateComponent extends Component {
	XPEDXSCXmlUtils xpedxScxmlUtil;

	public XPEDXItemsDataTemplateComponent(ValueStack stack, HttpServletRequest req,
            HttpServletResponse res, XPEDXItemsDataTemplateTag tag) {
		super(stack);
		this.tag = tag;
		this.req = req;
		xpedxScxmlUtil = new XPEDXSCXmlUtils();
	}

	@Override
	public boolean end(Writer writer, String body) {

		StringBuilder sb = new StringBuilder();

		Element item = (Element) findValue(tag.getItemElement(), Element.class);
		int tabidx = 101;
		SCXmlUtils xmlUtils = XPEDXSCXmlUtils.getInstance();
		Element info = xmlUtils.getChildElement(item, "PrimaryInformation");
		String itemID = validate(item.getAttribute("ItemID"));
		String itemDetailURL=validate(item.getAttribute("itemDetailURL"));
		HashMap<String, String> itemUOMList = tag.getItemUomHashMap().get(itemID);
		itemUOMList = itemUOMList == null ? new HashMap<String, String>() : itemUOMList;
		String defaultUOM = validate(tag.getDefaultShowUOMMap().get(itemID));
		String defaultUomCode = ""; //Added for EB 41,42,43
		//EB-225 - For retrieving the customer UOM of ItemID, from ItemIdcustomerUOM Map
		String custUOM = validate(tag.getItemCustomerUomMap().get(itemID));

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

		String imageUrl = XPEDXWCUtils.getStaticFileLocation() + "/xpedx/images/INF_150x150.jpg";
		String ImageLocation = xpedxScxmlUtil.getAttribute(info, "ImageLocation");
		String ImageID = xpedxScxmlUtil.getAttribute(info, "ImageID");
		if(ImageLocation!= null && ImageID!=null && !("").equals(ImageLocation) && !("").equals(ImageID)) {
			if(ImageLocation.lastIndexOf("/") == ImageLocation.length()-1)
				imageUrl = ImageLocation+ImageID;
			else
				imageUrl = ImageLocation+"/"+ImageID;
		}

		pImg = imageUrl;

		//ItemID value is required not string - JIRA 3538
		HashMap<String, String> skuMap = tag.getItemMap().get(itemID);
		skuMap = skuMap == null ? new HashMap<String, String>() : skuMap;
		XPEDXItemBranchInfoBean itemBranchBean = tag.getItemToItemBranchBeanMap().get(itemID);
		itemBranchBean = itemBranchBean == null ? new XPEDXItemBranchInfoBean("","","","","","","","") : itemBranchBean;
		String orderMultiple = validate(tag.getOrderMultipleMap().get(itemID));
		Element b2cItemExtn = xmlUtils.getChildElement(item, "Extn");
		String b2cSize = validate(b2cItemExtn.getAttribute("ExtnSize"));
		String b2cColor = validate(b2cItemExtn.getAttribute("ExtnColor"));

		// start-- EB-78 Catalog color Grid View

		if (b2cColor.contains("/"))
			b2cColor = b2cColor.replace("/", " / ");

		// END-- EB-78 Catalog color Grid View

		String b2cMwt = validate(b2cItemExtn.getAttribute("ExtnMwt"));
		String b2cBasis = validate(b2cItemExtn.getAttribute("ExtnBasis"));
		String b2cCert = validate(b2cItemExtn.getAttribute("ExtnCert"));
		String b2cVendorNumber = validate(b2cItemExtn.getAttribute("ExtnVendorNo"));
		//String b2cLegacyId = validate(b2cItemExtn.getAttribute("ExtnLegacyId"));
		String b2cMaterial = validate(b2cItemExtn.getAttribute("ExtnMaterial"));
		String b2cForm = validate(b2cItemExtn.getAttribute("ExtnForm"));
		String b2cCapacity = validate(b2cItemExtn.getAttribute("ExtnCapacity"));
		String b2cModel = validate(b2cItemExtn.getAttribute("ExtnModel"));
		String b2cGuage = validate(b2cItemExtn.getAttribute("ExtnGauge"));
		String b2cThickness = validate(b2cItemExtn.getAttribute("ExtnThickness"));
		String b2cPly = validate(b2cItemExtn.getAttribute("ExtnPly"));
		String b2cPackMethod = validate(b2cItemExtn.getAttribute("ExtnPackMethod")); //added for XBT 262 & 258
		String b2cstockStatus = validate(itemBranchBean.getInventoryIndicator());
		String storeFrontId=(String) tag.getWcContext().getStorefrontId();
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
		List<Element> replacmentList =null;
		boolean isGuestUser = (Boolean)findValue("guestUser");
		if(tag.getReplacmentItemsMap()!=null && isGuestUser!=true)
		{
			replacmentList = tag.getReplacmentItemsMap().get(itemID.trim());
		}
		replacmentList = replacmentList == null ? new ArrayList<Element>() : replacmentList;
		String itemCurrency = validate((String) findValue(tag.getCurrency()));


		XPEDXUtilBean utilBean = new XPEDXUtilBean();
		String formattedUnitprice = validate(utilBean.formatPriceWithCurrencySymbol(tag.getCtx(),itemCurrency,myPrice));
		boolean hasTier = false;

		sb.append("itemid: \"").append(itemID).append("\",");
		sb.append("itemkey: \"").append(itemKey).append("\",");
		sb.append("uom: \"").append(TextUtils.htmlEncode(unitOfMeasure)).append("\",");
		sb.append("name: \"").append(XPEDXMyItemsUtils.formatEscapeCharactersHtml(shortDesc)).append("\",");


		if(isGuestUser)
		{
			itemDetailURL="/swc/catalog/itemDetails.action?sfId="+storeFrontId+"&scGuestUser=Y"+"&_bcs_=%11true%12%12%12%2Fswc%2Fcatalog%2Fnavigate.action%3F"+storeFrontId+"%3Dxpedx%26scFlag%3DY%26%12%12catalog%12search%12%11&scFlag=Y"+"&itemID="+itemID+"&unitOfMeasure="+unitOfMeasure+"&selectedView=";
		}else
		{

			itemDetailURL="/swc/catalog/itemDetails.action?sfId=" + storeFrontId + "&_bcs_=%11true%12%12%12%2Fswc%2Fcatalog%2Fnavigate.action%3F"+storeFrontId+"%3Dxpedx%26scFlag%3DY%26%12%12catalog%12search%12%11&scFlag=Y"+"&itemID="+itemID+"&unitOfMeasure="+unitOfMeasure+"&selectedView=";
		}

		sb.append("itemDetailURL: \"").append(itemDetailURL).append("\",");
		sb.append("listprice: \"");
		String tierPrice = "";
		List<Element> tierPriceList = tag.getPLLineMap().get(itemID);
		if(tierPriceList != null) {
			StringBuilder priceList1 = new StringBuilder();
			StringBuilder priceList2 = new StringBuilder();
			String oldUOM = null;
			for (Element tierPrices : tierPriceList) {
				String tierQty = tierPrices.getAttribute("FromQuantity");
				tierQty = (tierQty == null || "".equals(tierQty)) ? "1" : tierQty;
				hasTier = true;
				tierPrice = validate(tierPrices.getAttribute("ListPrice"));
				Element extnTierPrice = xmlUtils.getChildElement(tierPrices, "Extn");
				String tierPriceUOM = validate(extnTierPrice.getAttribute("ExtnTierUom"));
				if(tierPriceUOM != null && tierPriceUOM.equals("dummyUOM")){
					continue;
				}
				String formattedTierUnitprice = validate(utilBean.formatPriceWithCurrencySymbol(tag.getCtx(),itemCurrency,tierPrice));
				if(formattedTierUnitprice == null || "".equals(formattedTierUnitprice)){
					if(oldUOM == null || oldUOM.equals(tierPriceUOM)){
						oldUOM = tierPriceUOM;
						priceList1.append("<br/>");
					}else{
						priceList2.append("<br/>");
					}
				}else {
					/*
					 * Order the price list values based on UOM
					 */
					if(oldUOM == null || oldUOM.equals(tierPriceUOM)){
						oldUOM = tierPriceUOM;
						formatUOMPriceQty(priceList1, tierQty, tierPriceUOM, formattedTierUnitprice);
					}else{
						formatUOMPriceQty(priceList2, tierQty, tierPriceUOM, formattedTierUnitprice);
					}
				}

			}
			sb.append(priceList1);
			sb.append(priceList2);
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
		sb.append("tabidx: \"").append(tabidx).append("\",");

		String uomDesc = "";
		try {
			if(custUOM!=null && custUOM.equalsIgnoreCase(unitOfMeasure)){
				uomDesc = custUOM.substring(2);
			}else{
				uomDesc = TextUtils.htmlEncode(com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils.getUOMDescription(unitOfMeasure));
			}
			sb.append("uomDesc: \"").append(uomDesc).append("\",");
		} catch (Exception e) {
			sb.append("uomDesc: \"\",");
		}
		//modified for jira 3253 to format quantity of ordermultiple
		if (isGuestUser == false) {

			if (!YFCCommon.isVoid(orderMultiple) && Integer.parseInt(orderMultiple) > 1) {
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
		sb.append("gauge: \"").append(TextUtils.htmlEncode(b2cGuage)).append("\",");
		sb.append("thickness: \"").append(TextUtils.htmlEncode(b2cThickness)).append("\",");
		sb.append("basis: \"").append(TextUtils.htmlEncode(b2cBasis)).append("\",");
		sb.append("mwt: \"").append(TextUtils.htmlEncode(b2cMwt)).append("\",");
		sb.append("vendorNumber: \"").append(TextUtils.htmlEncode(b2cVendorNumber)).append("\",");
		sb.append("packMethod: \"").append(TextUtils.htmlEncode(b2cPackMethod)).append("\",");//added for XBT 262 & 258
		sb.append("stocked: \"");
		if(!"W".equals(b2cstockStatus)) {
			sb.append("M");
		}
		sb.append("\",");
		sb.append("cert: \"");
		if("Y".equals(b2cCert)) {
			sb.append("<span><img width=\\\"20\\\" height=\\\"20\\\" src='" + XPEDXWCUtils.getStaticFileLocation() + "/xpedx/images/catalog/green-e-logo_small.png'></span>");
		}
		sb.append("\",");
		sb.append("qtyGreaterThanZeroMsg: \"").append(tag.getQtyString()).append("\",");
		sb.append("partno: \"");
		// Changed for EB 4010
		//Added for EB 47
		String extnMfgItemFlag = (String) tag.getWcContext().getWCAttribute("BILL_TO_CUST_MFG_ITEM_FLAG");
		String extnCustomerItemFlag = (String) tag.getWcContext().getWCAttribute("BILL_TO_CUST_PART_ITEM_FLAG");
		//Commented for EB 47 String custUserSKU = (String)tag.getWcContext().getWCAttribute("customerUseSKU");
		if(extnMfgItemFlag != null && extnMfgItemFlag.equalsIgnoreCase("Y")){
			sb.append(com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants.MANUFACTURER_ITEM_LABEL)
				.append(": ").append(TextUtils.htmlEncode(validate(skuMap.get("MPN"))));

		}

			sb.append("\",");
			/* else if("3".equals(custUserSKU)) {
			sb.append(com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants.MPC_ITEM_LABEL)
			.append(": ").append(TextUtils.htmlEncode(validate(skuMap.get("MPC"))));
		} else if("1".equals(custUserSKU)) {*/
			sb.append("customerItemno: \"");
		if(extnCustomerItemFlag!=null && extnCustomerItemFlag.equalsIgnoreCase("Y")){
			sb.append(com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants.CUSTOMER_ITEM_LABEL)
			.append(": ").append(TextUtils.htmlEncode(validate(skuMap.get("CPN"))));
		}

		//End of EB 47
		sb.append("\",");

		sb.append("uomdisplay: \"<select name='itemUomList' ").append("onmousedown=javascript:document.getElementById(").append("'").append(itemKey).append("'").append(").setAttribute('class',''); ")
			.append("onmouseout=javascript:document.getElementById(").append("'").append(itemKey).append("'").append(").setAttribute('class','itemdiv');")
			.append(" id='itemUomList_")
			.append(itemID).append("'>");
		for (Iterator<Map.Entry <String, String>> iterator = itemUOMList.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry  pair = (Map.Entry ) iterator.next();
			sb.append("<option value='").append(pair.getKey()).append("'");
			if(pair.getValue().toString().equals(defaultUOM)){
				sb.append(" selected='selected' ");
				defaultUomCode = (String) pair.getKey();
			}
			sb.append(">").append(TextUtils.htmlEncode(pair.getValue().toString())).append("</option>");
		}
		sb.append("</select>");
		sb.append("<input type='hidden' id='custUOM_");
		sb.append(itemID).append("' value='");
		sb.append(custUOM).append("'/>");
		//Added for EB 41,42,43
		sb.append("<input type='hidden' id='defaultUOMCode_");
		sb.append(itemID).append("' value='");
		sb.append(defaultUomCode).append("'/>");
		//End of EB EB 41,42,43
		sb.append("<input type='hidden' id='orderMultiple_");
		sb.append(itemID).append("' value='");
		sb.append(orderMultiple).append("'/>\",");
		sb.append("itemtypedesc: \"");
		if(!"W".equals(b2cstockStatus) && isGuestUser == false) {
			sb.append("<div class=\'mill-mfg\'>Mill / Mfg. Item<span class=\'addl-chg\'> - Additional charges may apply</span></div>");
		}
		sb.append("\",");
		if(replacmentList!=null && replacmentList.size()>0){
		StringBuffer repItemsForMiniView = new StringBuffer();
		StringBuffer repItemsForCondensedView = new StringBuffer();
		sb.append("repItem: \"<span style=\\\"color:red;padding-right:2px;font-weight:bold\\\">This item will be replaced once inventory is depleted. Select item:</span>");
		repItemsForCondensedView.append(",repItemsForCondensedView:\"<span style=\\\"color:red;padding-right:2px;font-weight:bold\\\">This item will be replaced once inventory is depleted. Select item:</span>");
		repItemsForMiniView.append(",repItemsForMiniView:\"<span style=\\\"color:red;padding-right:2px;font-weight:bold\\\">This item will be replaced once inventory is depleted. Select item:</span>");
		for(int i=0;i<replacmentList.size();i++ )
		{
		Element replacementItemElement = (Element)replacmentList.get(i);
		String replacementItem = validate(replacementItemElement.getAttribute("ItemID"));
		String replacementItemUOM = validate(replacementItemElement.getAttribute("UnitOfMeasure"));
		if (i>0) sb.append(",");
		//fixed for EB-4010
		//if (i==1) sb.append("<br/>");
		sb.append("<a class=\\\"underlink\\\" href=\\\"javascript:processDetail('");
		sb.append(TextUtils.htmlEncode(replacementItem));
		sb.append("','");
		sb.append(TextUtils.htmlEncode(replacementItemUOM));
		sb.append("');\\\">");
		sb.append("<b>"+TextUtils.htmlEncode(replacementItem)+"</b>");
		sb.append("</a>");

		if (i>0) repItemsForCondensedView.append(",");
		repItemsForCondensedView.append("<a class=\\\"underlink\\\" href=\\\"javascript:processDetail('");
		repItemsForCondensedView.append(TextUtils.htmlEncode(replacementItem));
		repItemsForCondensedView.append("','");
		repItemsForCondensedView.append(TextUtils.htmlEncode(replacementItemUOM));
		repItemsForCondensedView.append("');\\\">");
		repItemsForCondensedView.append("<b>"+TextUtils.htmlEncode(replacementItem)+"</b>");
		repItemsForCondensedView.append("</a>");

		if (i>0) repItemsForMiniView.append(",");
		if((i %2)!=0)repItemsForMiniView.append("<br/>");
		repItemsForMiniView.append("<a class=\\\"underlink\\\" href=\\\"javascript:processDetail('");
		repItemsForMiniView.append(TextUtils.htmlEncode(replacementItem));
		repItemsForMiniView.append("','");
		repItemsForMiniView.append(TextUtils.htmlEncode(replacementItemUOM));
		repItemsForMiniView.append("');\\\">");
		repItemsForMiniView.append("<b>"+TextUtils.htmlEncode(replacementItem)+"</b>");
		repItemsForMiniView.append("</a>");
		}
		sb.append("\"");

		repItemsForMiniView.append("\"");
		sb.append(repItemsForMiniView);
		repItemsForCondensedView.append("\"");
		sb.append(repItemsForCondensedView);
		}
		//sb = parseData(sb);
		//sb = parseData(sb);
		//sb = parseData(sb);

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

	private void formatUOMPriceQty(StringBuilder priceList, String tierQty, String tierPriceUOM, String formattedTierUnitprice){
		String htmlSpace = "&nbsp;";
		String formattedQty = XPEDXWCUtils.getFormattedQty(tierQty);
	    if(formattedQty != null && formattedQty.equals("0")){
	    	formattedQty = htmlSpace+htmlSpace;
	    	priceList.append(formattedQty);
	    }else{
	    	priceList.append(TextUtils.htmlEncode(formattedQty));
	    }
		priceList.append(htmlSpace);
		try {
			priceList.append(TextUtils.htmlEncode(XPEDXWCUtils.getFormattedUOMCode(tierPriceUOM))).append("-");
		} catch (Exception e) {
			priceList.append(TextUtils.htmlEncode(tierPriceUOM)).append("-");
		}
		priceList.append(formattedTierUnitprice);

		priceList.append("<br/>");
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
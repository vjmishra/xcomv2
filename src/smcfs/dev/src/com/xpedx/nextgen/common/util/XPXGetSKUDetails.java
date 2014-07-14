package com.xpedx.nextgen.common.util;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
/*
 * 
 * i/p
 * <Item SKU="" OrganizationCode="" SKUType="" />
 * 
 * o/p
 * <Item ItemID="" OrganizationCode="" UnitOfMeasure="" PricingUOM=""/>
 * 
 */
public class XPXGetSKUDetails implements YIFCustomApi{
	private static YIFApi api = null;
	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	public static final String getItemListTemplate = "global/template/api/getItemList.XPXGetSKUDetails.xml";
	public Document getSKUDetails(YFSEnvironment env, Document inputXML)  throws Exception
	{
		
		api = YIFClientFactory.getInstance().getApi();
		log.debug("InputXML for getSKUDetails is : "+SCXmlUtil.getString(inputXML));
		Element inputElement = inputXML.getDocumentElement();
		String sku = inputElement.getAttribute("SKU");
		String organizationCode = inputElement.getAttribute("OrganizationCode");
		String skuType = inputElement.getAttribute("SKUType");
		String envId = inputElement.getAttribute("EnvironmentCode");
		String Company = inputElement.getAttribute("CompanyCode");
		String customerNumber = inputElement.getAttribute("CustomerNumber");
		String itemId = "";
		if((sku.equals(""))|| (skuType.equals("")))
		{
			YFSException exception = new YFSException("SKU or SKUType is empty");
			
			throw exception;
		}
		//form the input xml for fetching the Item details
		Document inputItemDocument = formInputItemListDocument(env, sku, organizationCode, skuType, itemId,envId,Company,customerNumber);
		Document itemDetailDocument = null;
		
		Element inputItemElement = inputItemDocument.getDocumentElement();
		String itemFromInputDoc = inputItemDocument.getDocumentElement().getAttribute("ItemID");
		
		if((inputItemElement.hasAttribute("ItemID"))&& (itemFromInputDoc.equals("")))
		{
			itemDetailDocument = inputXML;
			Element itemDetailElement = itemDetailDocument.getDocumentElement();
			itemDetailElement.setAttribute("ErrorMessage", "Invalid CPN");
		}
		else
		{
		//get the item list
		env.setApiTemplate("getItemList", getItemListTemplate);
		Document itemListDoc = api.invoke(env, "getItemList", inputItemDocument);
		env.clearApiTemplate("getItemList");
		Element itemElement = null;
		NodeList itemList = itemListDoc.getElementsByTagName("Item");
		int itemLength = itemList.getLength();
		if(itemLength != 0)
		{
			itemDetailDocument = YFCDocument.createDocument().getDocument();
			itemElement = (Element)itemList.item(0);
			itemDetailDocument.appendChild(itemDetailDocument.importNode(itemElement, true));
			itemDetailDocument.renameNode(itemDetailDocument.getDocumentElement(), itemDetailDocument.getNamespaceURI(), "Order");
		}
		else
		{
			itemDetailDocument = inputXML;
			Element itemDetailElement = itemDetailDocument.getDocumentElement();
			itemDetailElement.setAttribute("ErrorMessage", "Invalid SKU");
		}
		}
		
				
		return itemDetailDocument;
		
	}

	private Document formInputItemListDocument(YFSEnvironment env, String sku,
			String organizationCode, String skuType, String itemId, String envId, String company, String customerNumber)
			throws RemoteException {
		Document inputItemDocument = YFCDocument.createDocument("Item").getDocument();
		Element inputItemElement = inputItemDocument.getDocumentElement();
		inputItemElement.setAttribute("OrganizationCode", organizationCode);
		if(log.isDebugEnabled()){
			log.debug("The SKU Type is " + skuType);
		}
		if(skuType.equals("LPC"))
		{
			itemId = sku;
			inputItemElement.setAttribute("ItemID", itemId);
		}
		else
		if(skuType.equals("MPC"))
		{
			Element extnElement = inputItemDocument.createElement("Extn");
			extnElement.setAttribute("ExtnMpcQryType", "LIKE");
			extnElement.setAttribute("ExtnMpc", sku);
			inputItemElement.appendChild(extnElement);
		}
		else
		if(skuType.equals("CPN"))
		{
			//get the itemID from ItemCustXreference
			itemId = getItemIDFromItemCust(env, sku, itemId,envId,company,customerNumber);
			inputItemElement.setAttribute("ItemID", itemId);
		}
		else
		if(skuType.equals("MANUFACTURER_ITEM"))
		{
			Element primaryElement = inputItemDocument.createElement("PrimaryInformation");
			primaryElement.setAttribute("ManufacturerItem", sku);
		}
		return inputItemDocument;
	}

	private String getItemIDFromItemCust(YFSEnvironment env, String sku, String itemId, String envId, String company, String customerNumber) throws RemoteException {
		
		//form the input
		Document inputItemCustDocument = YFCDocument.createDocument("XPXItemcustXref").getDocument();
		Element inputItemCustElement = inputItemCustDocument.getDocumentElement();
		inputItemCustElement.setAttribute("CustomerItemNumber", sku);
		inputItemCustElement.setAttribute("EnvironmentCode", envId);
		inputItemCustElement.setAttribute("CompanyCode", company);
		inputItemCustElement.setAttribute("CustomerNumber",customerNumber);
		Document itemCustListDoc = api.executeFlow(env, "getItemCustXrefListService", inputItemCustDocument);
		NodeList itemCustList = itemCustListDoc.getElementsByTagName("XPXItemcustXref");
		int xrefLength = itemCustList.getLength();
		if(xrefLength != 0)
		{
		Element itemCustElement = (Element)itemCustList.item(0);
		itemId = itemCustElement.getAttribute("LegacyItemNumber");
		}	
		return itemId;
	}
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}


}

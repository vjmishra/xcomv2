package com.xpedx.nextgen.item;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXBulkDeleteItems implements YIFCustomApi {

	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	private Properties _properties = null;
	private static YIFApi api = null;
	
	static {

		try {
			api = YIFClientFactory.getInstance().getApi();

		} catch (YIFClientCreationException e) {
			
			e.printStackTrace();
		}
	}
	

	@Override
	public void setProperties(Properties properties) throws Exception {
		
		_properties = properties;

	}

	public void invoke(YFSEnvironment env, Document inXML) throws Exception
	{
		int totalItems = 0;
		
		Element inputElement = inXML.getDocumentElement();
		String strCount = inputElement.getAttribute("Count");
		if(strCount != null) {
			totalItems = Integer.parseInt(strCount);
		} else
			totalItems = 500;
		
		YFCDocument getItemListInputDoc = YFCDocument.createDocument("Item");
		YFCElement inputDocRootElement = getItemListInputDoc.getDocumentElement();
		inputDocRootElement.setAttribute("MaximumRecords", String.valueOf(totalItems));
		
		YFCDocument getItemListOutputDoc = YFCDocument.createDocument("ItemList");
		YFCElement outputDocRootElement = getItemListOutputDoc.getDocumentElement();
		YFCElement itemElement = getItemListOutputDoc.createElement("Item");
		itemElement.setAttribute("ItemKey","");
		outputDocRootElement.appendChild(itemElement);
		
		
		env.setApiTemplate("getItemList", getItemListOutputDoc.getDocument());
		Document resultDoc = api.invoke(env, "getItemList", getItemListInputDoc.getDocument());
		env.clearApiTemplate("getItemList");

		NodeList itemList = resultDoc.getElementsByTagName("Item");
		
		totalItems += itemList.getLength();
		log.debug("Items count " + itemList.getLength());
		log.debug("Starting to delete items");
		
		YFCDocument manageItemInputDoc = YFCDocument.createDocument("ItemList");
		YFCElement manageItemDocRootElement = manageItemInputDoc.getDocumentElement();
		for(int itemCounter=0; itemCounter<itemList.getLength(); itemCounter++){
			Element itemNode = (Element) itemList.item(itemCounter);
			String itemKey = itemNode.getAttribute("ItemKey");
			
			YFCElement itemElement1 = manageItemInputDoc.createElement("Item");
			itemElement1.setAttribute("Action", "Delete");
			itemElement1.setAttribute("ItemKey",itemKey);
			manageItemDocRootElement.appendChild(itemElement1);
		}
	
		log.debug(manageItemDocRootElement.toString());
		resultDoc = api.invoke(env, "manageItem", manageItemInputDoc.getDocument());
		log.info("Total No. of Item deleted ---- " + totalItems);
	}	
}

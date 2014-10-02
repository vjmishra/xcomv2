package com.xpedx.nextgen.catalog.api;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;

import com.xpedx.nextgen.catalog.api.ItemIndexUtil.ItemMetadata;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.ycm.japi.ue.YCMGetAdditionalCatalogIndexInformationUE;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfs.japi.YFSUserExitException;

/**
 * Adds normally stocked item and customer part number to the Sterling/Lucene item index.
 */
public class XPEDXGetAdditionalCatalogIndexInformationUE implements YCMGetAdditionalCatalogIndexInformationUE {

	private static final YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");

	@Override
	public Document getAdditionalCatalogIndexInformation(YFSEnvironment env, Document inDocumentUE) throws YFSUserExitException {
		String inStockStatus = YFSSystem.getProperty("inventory_indicator_for_in_stock_status");
		if (inStockStatus == null || inStockStatus.trim().length() == 0) {
			inStockStatus = "W";
		}

		if (log.isDebugEnabled()) {
			log.debug("inStockStatus = " + inStockStatus);
		}

		try {
			YFCDocument inDoc = YFCDocument.getDocumentFor(inDocumentUE);
			YFCElement inElem = inDoc.getDocumentElement();
			YFCIterable<YFCElement> searchFieldListIterator = inElem.getChildElement("SearchIndexFieldList").getChildren("SearchField");
			YFCIterable<YFCElement> itemListIterator = inElem.getChildElement("ItemList").getChildren("Item");

			List<String> allItemIDs = getItemIds(inElem.getChildElement("ItemList").getChildren("Item"));
			Map<String, ItemMetadata> metadataForItems = new ItemIndexUtil().getMetadataForItems(env, allItemIDs, inStockStatus);

			YFCDocument outDoc = YFCDocument.createDocument("ItemList");
			if ("en_US".equals(inElem.getAttribute("LocaleCode"))) {
				getLocaleDoc(env, outDoc, searchFieldListIterator, itemListIterator, metadataForItems);
			}
			return outDoc.getDocument();

		} catch (Exception ex) {
			log.error("Problem creating Search Index additional info in user exit", ex);
			throw new YFSUserExitException(ex.getMessage());
		}
	}

	private void getLocaleDoc(YFSEnvironment env, YFCDocument outDoc, YFCIterable<YFCElement> searchFieldListIterator, YFCIterable<YFCElement> itemListIterator, Map<String, ItemMetadata> metadataForItems)
			throws YFSException, RemoteException, YIFClientCreationException {
		YFCElement outElement = outDoc.getDocumentElement();
		// taking one item at a time
		for (YFCElement itemElem : itemListIterator) {
			String itemID = itemElem.getAttribute("ItemID");
			if (log.isDebugEnabled()) {
				log.debug("itemID = " + itemID);
			}

			ItemMetadata im = metadataForItems.get(itemID);

			// Now preparing the output
			YFCElement child = outElement.createChild("Item");
			child.setAttributes(itemElem.getAttributes());
			YFCElement valueListElement = child.createChild("AdditionalCatalogIndexInformationList");
			for (YFCElement searchFieldElement : searchFieldListIterator) {
				YFCElement valueElement = valueListElement.createChild("AdditionalCatalogIndexInformation");
				if (log.isDebugEnabled()) {
					log.debug("searchFieldElement = " + searchFieldElement);
				}
				valueElement.setAttribute("IndexFieldName", searchFieldElement.getAttribute("IndexFieldName"));

				if ("customerNumberPlusPartNumber".equals(searchFieldElement.getAttribute("IndexFieldName"))) {
					Set<String> customerAndItemNumbers = im.getCustomerAndItemNumbers();
					if (log.isDebugEnabled()) {
						log.debug("customerAndItemNumbers = " + customerAndItemNumbers);
					}
					valueElement.setAttribute("Value", ItemIndexUtil.join(customerAndItemNumbers, " "));
				}

				if ("showStockedItems".equals(searchFieldElement.getAttribute("IndexFieldName"))) {
					Set<String> divisionsInStock = im.getDivisionsInStock();
					if (log.isDebugEnabled()) {
						log.debug("divisionsInStock = " + divisionsInStock);
					}
					valueElement.setAttribute("Value", ItemIndexUtil.join(divisionsInStock, " "));
				}

				if ("contractBillTos".equals(searchFieldElement.getAttribute("IndexFieldName"))) {
					Set<String> contractBillTos = im.getContractBillTos();
					if (log.isDebugEnabled()) {
						log.debug("contractBillTos = " + contractBillTos);
					}
					valueElement.setAttribute("Value", ItemIndexUtil.join(contractBillTos, " "));
				}
			}
		}
	}

	private List<String> getItemIds(YFCIterable<YFCElement> itemListIterator) {
		List<String> list = new LinkedList<String>();
		for (YFCElement itemElem : itemListIterator) {
			list.add(itemElem.getAttribute("ItemID"));
		}
		return list;
	}

}

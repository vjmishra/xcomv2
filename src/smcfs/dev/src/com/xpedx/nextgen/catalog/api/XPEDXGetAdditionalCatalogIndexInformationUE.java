package com.xpedx.nextgen.catalog.api;

import java.rmi.RemoteException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycm.japi.ue.YCMGetAdditionalCatalogIndexInformationUE;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfs.japi.YFSUserExitException;

public class XPEDXGetAdditionalCatalogIndexInformationUE implements
		YCMGetAdditionalCatalogIndexInformationUE {
	private static YIFApi api = null;
	YFSEnvironment mEnvironment = null;
	public static String stockStatus;
	private static final YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	public Document getAdditionalCatalogIndexInformation(
			YFSEnvironment environment, Document inDocumentUE)
			throws YFSUserExitException {
		YFCDocument outDocument = null;
		stockStatus = YFSSystem
				.getProperty("inventory_indicator_for_in_stock_status");
		if (stockStatus == null || stockStatus.trim().length() == 0) {
			stockStatus = "W";
		}
		log.debug("XPEDXGetAdditionalCatalogIndexInformationUE_StockStatus : "+stockStatus);
		try {
			mEnvironment = environment;
			YFCDocument inDocument = YFCDocument.getDocumentFor(inDocumentUE);
			YFCElement inElement = inDocument.getDocumentElement();
			YFCIterable<YFCElement> searchFieldListIterator = inElement
					.getChildElement("SearchIndexFieldList").getChildren(
							"SearchField");
			YFCIterable<YFCElement> itemListIterator = inElement
					.getChildElement("ItemList").getChildren("Item");
			outDocument = YFCDocument.createDocument("ItemList");
			if ("en_US".equals(inElement.getAttribute("LocaleCode"))) {
				getDefaultLocaleOutputDoc(outDocument, searchFieldListIterator,
						itemListIterator);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return outDocument.getDocument();
	}

	private void getDefaultLocaleOutputDoc(YFCDocument outDoc,
			YFCIterable<YFCElement> searchFieldListIterator,
			YFCIterable<YFCElement> itemListIterator) throws YFSException,
			RemoteException, YIFClientCreationException {
		getLocaleDoc(outDoc, searchFieldListIterator, itemListIterator,
				" Ext English Desc");
	}

	private void getLocaleDoc(YFCDocument outDoc,
			YFCIterable<YFCElement> searchFieldListIterator,
			YFCIterable<YFCElement> itemListIterator, String appendValue)
			throws YFSException, RemoteException, YIFClientCreationException {
		YFCElement outElement = outDoc.getDocumentElement();
		// taking one item at a time
		for (YFCElement itemElement : itemListIterator) {
			String itemID = itemElement.getAttribute("ItemID");
			String organizationCode = itemElement
					.getAttribute("OrganizationCode");
			String[] divisionsForStockedItem = getDivisionsForStockedItem(
					itemID, organizationCode);
			log.debug("getLocaleDoc_divisionsForStockedItem : "+divisionsForStockedItem);
			NodeList XpxItemcustXrefList = getItemCustomerXDetails(itemID,
					mEnvironment);
			int lengthC = XpxItemcustXrefList.getLength();
			String customerNumberPlusPartNumber = "";
			String RECORD_SEPARATOR = " ";
			// getting all customers for an item
			// appending all records by a record separator
			for (int index = 0; index < lengthC; index++) {
				if (customerNumberPlusPartNumber != null
						&& customerNumberPlusPartNumber.trim().length() > 0) {
					customerNumberPlusPartNumber += RECORD_SEPARATOR;
				}
				Node XpxItemcustXref = XpxItemcustXrefList.item(index);
				NamedNodeMap XpxItemcustXrefAttributes = XpxItemcustXref
						.getAttributes();
				Node customerNumberN = XpxItemcustXrefAttributes
						.getNamedItem("CustomerNumber");
				if (customerNumberN != null
						&& customerNumberN.getTextContent().trim().length() > 0) {
					customerNumberPlusPartNumber += customerNumberN
							.getTextContent();
				}
				Node customerPartNumberN = XpxItemcustXrefAttributes
						.getNamedItem("CustomerItemNumber");
				if (customerPartNumberN != null
						&& customerPartNumberN.getTextContent().trim().length() > 0) {
					customerNumberPlusPartNumber += "|"
							+ customerPartNumberN.getTextContent();
				}
			}
			// Now preparing the output
			YFCElement child = outElement.createChild("Item");
			child.setAttributes(itemElement.getAttributes());
			YFCElement valueListElement = child
					.createChild("AdditionalCatalogIndexInformationList");
			for (YFCElement searchFieldElement : searchFieldListIterator) {
				YFCElement valueElement = valueListElement
						.createChild("AdditionalCatalogIndexInformation");
				log.debug("getLocaleDoc_searchFieldElement:" + searchFieldElement);
				valueElement.setAttribute("IndexFieldName", searchFieldElement
						.getAttribute("IndexFieldName"));
				if (customerNumberPlusPartNumber != null
						&& customerNumberPlusPartNumber.length() > 0) {
					if ("customerNumberPlusPartNumber"
							.equals(searchFieldElement
									.getAttribute("IndexFieldName"))) {
						valueElement.setAttribute("Value",
								customerNumberPlusPartNumber);
					}
				}
				if (divisionsForStockedItem != null
						&& divisionsForStockedItem.length > 0) {
					log.debug("getLocaleDoc_divisionsForStockedItem="+divisionsForStockedItem);
					String divisionForStockedItem = "";
					for (String division : divisionsForStockedItem) {
						if (division != null && division.trim().length() > 0) {
							divisionForStockedItem += division
									+ RECORD_SEPARATOR;
						}
					}
					if ("showNormallyStockedItems".equals(searchFieldElement
							.getAttribute("IndexFieldName"))) {
						log.debug("getLocaleDoc_showNormallyStockedItems=" + divisionForStockedItem.trim());
						valueElement.setAttribute("Value",
								divisionForStockedItem.trim());
					}
					if ("showStockedItems".equals(searchFieldElement
							.getAttribute("IndexFieldName"))) {
						log.debug("getLocaleDoc_showStockedItems=" + divisionForStockedItem.trim());
						valueElement.setAttribute("Value",
								divisionForStockedItem.trim());
					}
				}
			}
		}
	}

	private String[] getDivisionsForStockedItem(String itemID,
			String organizationCode) throws YIFClientCreationException,
			YFSException, RemoteException {
		String[] divisions = null;
		YFCDocument inputDocument = YFCDocument.createDocument("Item");
		YFCElement documentElement = inputDocument.getDocumentElement();
		documentElement.setAttribute("ItemID", itemID);
		documentElement.setAttribute("CallingOrganizationCode",
				organizationCode);
		String templateXML = "<ItemList><Item><Extn><XPXItemExtnList><XPXItemExtn "
				+ "InventoryIndicator='' XPXDivision=''></XPXItemExtn>"
				+ "</XPXItemExtnList></Extn></Item>" + "</ItemList>";
		mEnvironment.setApiTemplate("getItemList", SCXmlUtil
				.createFromString(templateXML));
		YIFApi api = YIFClientFactory.getInstance().getApi();
		Document outputListDocument = api.invoke(mEnvironment,
				"getItemList", inputDocument.getDocument());
		Element outputElement = outputListDocument.getDocumentElement();
		Node wItemNode = outputElement.getFirstChild();
		if (wItemNode != null) {
			Node wExtnNode = wItemNode.getFirstChild();
			if (wExtnNode != null) {
				Node XPXItemExtnListNode = wExtnNode.getFirstChild();
				if (XPXItemExtnListNode != null) {
					NodeList wXPXItemExtnNodeList = XPXItemExtnListNode
							.getChildNodes();
					if (wXPXItemExtnNodeList != null) {
						int length = wXPXItemExtnNodeList.getLength();
						divisions = new String[length];
						for (int index = 0; index < length; index++) {
							Node wXPXItemExtChildNode = wXPXItemExtnNodeList
									.item(index);
							if (wXPXItemExtChildNode != null) {
								NamedNodeMap namedNodeMap = wXPXItemExtChildNode
										.getAttributes();
								if (namedNodeMap != null) {
									
									String wInventoryIndicator = namedNodeMap
											.getNamedItem("InventoryIndicator")
											.getTextContent();
									if (wInventoryIndicator != null
											&& wInventoryIndicator
													.equals(stockStatus)) {
										String wXPXDivision = namedNodeMap
												.getNamedItem("XPXDivision")
												.getTextContent();
										divisions[index] = wXPXDivision;
									}
								}
							}
						}

					}
				}
			}
		}
		return divisions;
	}

	private NodeList getItemCustomerXDetails(String itemID, YFSEnvironment env)
			throws YIFClientCreationException, YFSException, RemoteException {
		env.setDataAccessFilter(false);
		YFCDocument inputDocument = YFCDocument
				.createDocument("XPXItemcustXref");
		YFCElement inputElement = inputDocument.getDocumentElement();
		inputElement.setAttribute("LegacyItemNumber", itemID);
		api = YIFClientFactory.getInstance().getApi();
		Document outputListDocument = api.executeFlow(env,
				"getXPXItemcustXrefList", inputDocument.getDocument());
		Element outputListElement = outputListDocument.getDocumentElement();
		if (outputListElement != null) {
			return outputListElement.getChildNodes();
		}
		return null;
	}

}

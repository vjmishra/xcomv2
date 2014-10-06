package com.xpedx.nextgen.catalog.api;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * Helper class for processing extra metadata for {@link XPEDXGetAdditionalCatalogIndexInformationUE}
 */
public class ItemIndexUtil {

	private static final YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	private static final Logger log4j = Logger.getLogger("veritiv.ItemIndex");

	/**
	 * Gathers additional item metadata needed to include the following in the Sterling/Lucene item index:
	 * <ul>
	 *  <li>Normally stocked item</li>
	 *  <li>Customer part numbers</li>
	 * </ul>
	 * @param env
	 * @param itemIDs
	 * @param inStockStatus
	 * @return Returns a Map formatted as: key=ItemID, value=ItemMetadata
	 * @throws Exception
	 */
	public Map<String, ItemMetadata> getMetadataForItems(YFSEnvironment env, Collection<? extends String> itemIDs, String inStockStatus) throws Exception {
		Map<String, ItemMetadata> metadata = new LinkedHashMap<String, ItemMetadata>();

		// group the item ids for batched calls
		SegmentedList<String> itemIDGroups = new SegmentedList<String>(itemIDs, 100);
		for (List<String> itemIDGroup : itemIDGroups) {

			Map<String, Set<String>> contractBillTosForItem = getContractBillToIds(env, itemIDGroup);

			Map<String, Set<String>> divisionsInStockForItem = getDivisionsInStockForAllItems(env, itemIDGroup, inStockStatus);

			for (Entry<String, Set<String>> entry : divisionsInStockForItem.entrySet()) {
				String itemID = entry.getKey();
				Set<String> divisionsInStock = entry.getValue();

				ItemMetadata im = new ItemMetadata();
				metadata.put(itemID, im);

				im.setDivisionsInStock(divisionsInStock);
				if (contractBillTosForItem.get(itemID) != null) {
					im.setContractBillTos(contractBillTosForItem.get(itemID));
				}

				// this call is NOT batched, since the api limits output to 5000 records and some items have thousands of records (as of Aug 2014, one item has 4000+)
				Set<String> customerAndItemNumbers = getCustomerPartNumbersForItem(env, itemID);
				im.setCustomerAndItemNumbers(customerAndItemNumbers);
			}
		}

		return metadata;
	}

	//--- Contract information - gather BillTos per item
	private Map<String, Set<String>> getContractBillToIds(YFSEnvironment env, Collection<? extends String> itemIds) throws Exception {
		Map<String, Set<String>> contractBillTosForItems = new LinkedHashMap<String, Set<String>>();

		Element itemListOutputElem = getContractElementsFromDB(env, itemIds);

		List<Element> itemElems = SCXmlUtil.getElements(itemListOutputElem, "XPXItemContractExtn");

		for (Element itemElem : itemElems) {
			addBillToForItem(contractBillTosForItems, itemElem);
		}

		return contractBillTosForItems;
	}
	private Element getContractElementsFromDB(YFSEnvironment env, Collection<? extends String> itemIds) throws Exception {

		YFCDocument contractsInputDoc = getContractsApiInput(itemIds);

		final String serviceName = "XPXGetItemContractExtn";
		if (log.isDebugEnabled()) {
			log.warn("Invoking " + serviceName);
			log.warn("itemInputDoc = " + SCXmlUtil.getString(contractsInputDoc.getDocument()));
		}

		env.setApiTemplate(serviceName, SCXmlUtil.createFromString(getContractApiTemplate(env)));
		YIFApi api = YIFClientFactory.getInstance().getApi();

		Document contractsOutputDoc = api.executeFlow(env, serviceName, contractsInputDoc.getDocument());

		return contractsOutputDoc.getDocumentElement();
	}
	private YFCDocument getContractsApiInput(Collection<? extends String> itemIds) {
		YFCDocument contractsInputDoc = YFCDocument.createDocument("XPXItemContractExtn");
		YFCElement contractsInputElem = contractsInputDoc.getDocumentElement();

		YFCElement complexQueryElem = contractsInputElem.createChild("ComplexQuery");
		complexQueryElem.setAttribute("Operation", "OR");

		YFCElement orElem = complexQueryElem.createChild("Or");

		for (String itemId : itemIds) {
			YFCElement expElem = orElem.createChild("Exp");
			expElem.setAttribute("Name", "ItemId");
			expElem.setAttribute("Value", itemId);
		}
		return contractsInputDoc;
	}
	private String getContractApiTemplate(YFSEnvironment env) {
		String templateXml = ""
				+ "<XPXItemContractExtnList>"
				+ "  <XPXItemContractExtn CustomerId='' ItemId=''/>"
				+ "</XPXItemContractExtnList>";
		return templateXml;
	}
	private void addBillToForItem(Map<String, Set<String>> contractBillTosForItems, Element itemElem) {
		Set<String> contractBillTos;
		String itemId = itemElem.getAttribute("ItemId");
		if (contractBillTosForItems.containsKey(itemId)) {
			contractBillTos = contractBillTosForItems.get(itemId);
		}
		else {
			contractBillTos = new LinkedHashSet<String>();
			contractBillTosForItems.put(itemId, contractBillTos);
		}

		// drop chars after first 13 and strip "-" so get "900000442599"
		String billTo = itemElem.getAttribute("CustomerId");
		String[] billToParts = billTo.split("-");
		contractBillTos.add(billToParts[0]+billToParts[1]);
	}

	//--- Divisions that have item in stock (api calls batched for improved performance)
	private Map<String, Set<String>> getDivisionsInStockForAllItems(YFSEnvironment env, Collection<? extends String> itemIDs, String inStockStatus) throws Exception {
		Map<String, Set<String>> divisionsInStockForItems = new LinkedHashMap<String, Set<String>>();

		Element itemListOutputElem = getDivisionsFromDB(env, itemIDs);

		List<Element> itemElems = SCXmlUtil.getElements(itemListOutputElem, "Item");
		for (Element itemElem : itemElems) {
			Set<String> divisionsInStock = new LinkedHashSet<String>();
			divisionsInStockForItems.put(itemElem.getAttribute("ItemID"), divisionsInStock);

			List<Element> extnElems = SCXmlUtil.getElements(itemElem, "Extn/XPXItemExtnList/XPXItemExtn");
			for (Element extnElem : extnElems) {
				String inventoryIndicator = extnElem.getAttribute("InventoryIndicator"); // M, I, or W
				if (inventoryIndicator != null && inventoryIndicator.equals(inStockStatus)) {
					divisionsInStock.add(extnElem.getAttribute("XPXDivision"));
				}
			}
		}

		return divisionsInStockForItems;
	}
	private Element getDivisionsFromDB(YFSEnvironment env, Collection<? extends String> itemIDs)
			throws YIFClientCreationException, RemoteException {

		YFCDocument itemInputDoc = getDivisionsApiInput(itemIDs);

		String templateXml = getDivisionsApiTemplate(env);
		env.setApiTemplate("getItemList", SCXmlUtil.createFromString(getDivisionsApiTemplate(env)));

		if (log.isDebugEnabled()) {
			log.debug("Invoking getItemList");
			log.debug("itemInputDoc = " + SCXmlUtil.getString(itemInputDoc.getDocument()));
			log.debug("templateXml = " + templateXml);
		}

		YIFApi api = YIFClientFactory.getInstance().getApi();

		Document itemListOutputDoc = api.invoke(env, "getItemList", itemInputDoc.getDocument());

		return itemListOutputDoc.getDocumentElement();
	}

	private YFCDocument getDivisionsApiInput(Collection<? extends String> itemIDs) {
		YFCDocument itemInputDoc = YFCDocument.createDocument("Item");
		YFCElement itemInputElem = itemInputDoc.getDocumentElement();
		itemInputElem.setAttribute("CallingOrganizationCode", "xpedx"); // all items are in xpedx org

		YFCElement complexQueryElem = itemInputElem.createChild("ComplexQuery");
		complexQueryElem.setAttribute("Operation", "OR");

		YFCElement orElem = complexQueryElem.createChild("Or");

		for (String itemID : itemIDs) {
			YFCElement expElem = orElem.createChild("Exp");
			expElem.setAttribute("Name", "ItemID");
			expElem.setAttribute("Value", itemID);
		}
		return itemInputDoc;
	}
	private String getDivisionsApiTemplate(YFSEnvironment env) {
		String templateXml = ""
				+ "	<ItemList>"
				+ "		<Item ItemID=''>"
				+ "			<Extn ExtnShortDescription=''>"
				+ "				<XPXItemExtnList>"
				+ "					<XPXItemExtn InventoryIndicator='' XPXDivision='' />"
				+ "				</XPXItemExtnList>"
				+ "			</Extn>"
				+ "		</Item>"
				+ "	</ItemList>";
		return templateXml;
	}


	// calls NOT batched, since the api limits output to 5000 records and some items have thousands of records (as of Aug 2014, one item has 4000+)
	private Set<String> getCustomerPartNumbersForItem(YFSEnvironment env, String itemID) throws Exception {
		Set<String> customerAndItemNumbers = new LinkedHashSet<String>();

		YFCDocument xpxItemcustXrefInputDoc = YFCDocument.createDocument("XPXItemcustXref");
		YFCElement xpxItemcustXrefInputElem = xpxItemcustXrefInputDoc.getDocumentElement();
		xpxItemcustXrefInputElem.setAttribute("LegacyItemNumber", itemID);

		YIFApi api = YIFClientFactory.getInstance().getApi();
		env.setDataAccessFilter(false);

		String templateXml = ""
				+ "	<XPXItemcustXrefList>"
				+ "		<XPXItemcustXref CustomerNumber='' CustomerItemNumber='' />"
				+ "	</XPXItemcustXrefList>";
		env.setApiTemplate("getXPXItemcustXrefList", SCXmlUtil.createFromString(templateXml));

		if (log.isDebugEnabled()) {
			log.debug("Invoking getXPXItemcustXrefList");
			log.debug("xpxItemcustXrefInputDoc = " + SCXmlUtil.getString(xpxItemcustXrefInputDoc.getDocument()));
			log.debug("templateXml = " + templateXml);
		}

		Document xpxItemcustXrefListOutputDoc = api.executeFlow(env, "getXPXItemcustXrefList", xpxItemcustXrefInputDoc.getDocument());

		Element xpxItemcustXrefListOutputElem = xpxItemcustXrefListOutputDoc.getDocumentElement();

		if (xpxItemcustXrefListOutputElem != null) {
			// return a list of concatenated CustomerNumber and CustomerItemNumber
			List<Element> xpxItemcustXrefElems = SCXmlUtil.getElements(xpxItemcustXrefListOutputElem, "XPXItemcustXref");
			for (Element xpxItemcustXrefElem : xpxItemcustXrefElems) {
				String customerNumber = xpxItemcustXrefElem.getAttribute("CustomerNumber");
				String customerItemNumber = xpxItemcustXrefElem.getAttribute("CustomerItemNumber");

				if (!YFCUtils.isVoid(customerNumber) && !YFCUtils.isVoid(customerItemNumber)) {
					customerAndItemNumbers.add(customerNumber + customerItemNumber);
				}
			}
		}

		return customerAndItemNumbers;
	}

	// --- Helper classes

	public static class ItemMetadata {

		private String itemID;
		private Set<String> divisionsInStock       = new LinkedHashSet<String>();
		private Set<String> customerAndItemNumbers = new LinkedHashSet<String>();
		private Set<String> contractBillTos        = new LinkedHashSet<String>();

		public String getItemID() {
			return itemID;
		}
		public void setItemID(String itemID) {
			this.itemID = itemID;
		}
		public Set<String> getDivisionsInStock() {
			return divisionsInStock;
		}
		public void setDivisionsInStock(Set<String> divisionsInStock) {
			this.divisionsInStock = divisionsInStock;
		}
		public Set<String> getCustomerAndItemNumbers() {
			return customerAndItemNumbers;
		}
		public void setCustomerAndItemNumbers(Set<String> customerAndItemNumbers) {
			this.customerAndItemNumbers = customerAndItemNumbers;
		}
		public Set<String> getContractBillTos() {
			return contractBillTos;
		}
		public void setContractBillTos(Set<String> contractBillTos) {
			this.contractBillTos = contractBillTos;
		}
	}

	public static String join(Collection<String> strings, String separator) {
		StringBuilder buf = new StringBuilder(20 * strings.size());
		for (Iterator<String> strIter = strings.iterator(); strIter.hasNext();) {
			buf.append(strIter.next());
			if (strIter.hasNext()) {
				buf.append(separator);
			}
		}
		return buf.toString();
	}

}

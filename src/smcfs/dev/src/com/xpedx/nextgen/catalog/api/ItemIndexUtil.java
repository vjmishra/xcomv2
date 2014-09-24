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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.catalog.api.eb3359.StopWatchFor3359;
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
			log.warn("J: Meta contractBillTosForItem #: " + contractBillTosForItem.size());//TODO remove


			Map<String, Set<String>> divisionsInStockForItem = getDivisionsInStockForAllItems(env, itemIDGroup, inStockStatus);
			for (Entry<String, Set<String>> entry : divisionsInStockForItem.entrySet()) {
				String itemID = entry.getKey();
				log.warn("J:  MetaLoop processing item: " + itemID);//TODO remove
				log.warn("J:   MetaLoop contactBillTos: " + contractBillTosForItem.get(itemID));//TODO remove
				Set<String> divisionsInStock = entry.getValue();

				ItemMetadata im = new ItemMetadata();
				metadata.put(itemID, im);

				im.setDivisionsInStock(divisionsInStock);
				im.setContractBillTos(contractBillTosForItem.get(itemID));

				// this call is NOT batched, since the api limits output to 5000 records and some items have thousands of records (as of Aug 2014, one item has 4000+)
				Set<String> customerAndItemNumbers = getCustomerPartNumbersForItem(env, itemID);
				im.setCustomerAndItemNumbers(customerAndItemNumbers);
			}
		}

		return metadata;
	}

	private Map<String, Set<String>> getContractBillToIds(YFSEnvironment env, Collection<? extends String> itemIDs) throws Exception {
		Map<String, Set<String>> contractBillTosForItems = new LinkedHashMap<String, Set<String>>();

		Element itemListOutputElem = getContractElementsFromApi(env, itemIDs);

		List<Element> itemElems = SCXmlUtil.getElements(itemListOutputElem, "XPXItemContractExtn");
		log.warn("J: # of XPXItemContractExtn: " + itemElems.size());//TODO remove
		for (Element itemElem : itemElems) {
			log.warn("J: itemElem: " + SCXmlUtil.getString(itemElem));//TODO remove
			Set<String> contractBillTos = new LinkedHashSet<String>();
			contractBillTosForItems.put(itemElem.getAttribute("ItemID"), contractBillTos);

			List<Element> customerElems = SCXmlUtil.getElements(itemElem, "CustomerId");
			log.warn("J:  # of CustomerId: " + customerElems.size());//TODO remove
			for (Element extnElem : customerElems) {
				contractBillTos.add(extnElem.getAttribute("CustomerId"));
			}
		}

		return contractBillTosForItems;
	}

	private Element getContractElementsFromApi(YFSEnvironment env, Collection<? extends String> itemIDs) throws Exception {

		// TODO replace this fake stuff with call to new API/Service!!!
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse("<XPXItemContractExtnList> <XPXItemContractExtn CreateTs=\"2014-09-23T13:12:32-04:00\" CustomerId=\"60-0006020657-000-M-XX-B\" ItemId=\"2000920\"/> <XPXItemContractExtn CreateTs=\"2014-09-23T13:12:32-04:00\" CustomerId=\"12-0000304742-000-M-XX-B\" ItemId=\"2001020\"/> <XPXItemContractExtn CreateTs=\"2014-09-23T10:50:39-04:00\" CustomerId=\"60-0006020657-000-M-XX-B\" ItemId=\"2001020\"/> </XPXItemContractExtnList>");
		return doc.getDocumentElement();
	}

	// api calls batched for improved performance
	private Map<String, Set<String>> getDivisionsInStockForAllItems(YFSEnvironment env, Collection<? extends String> itemIDs, String inStockStatus) throws Exception {
		Map<String, Set<String>> divisionsInStockForItems = new LinkedHashMap<String, Set<String>>();

		Element itemListOutputElem = getDivisionsFromApi(env, itemIDs);

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

	private Element getDivisionsFromApi(YFSEnvironment env, Collection<? extends String> itemIDs)
			throws YIFClientCreationException, RemoteException {
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
		env.setApiTemplate("getItemList", SCXmlUtil.createFromString(templateXml));

		YIFApi api = YIFClientFactory.getInstance().getApi();

		if (log.isDebugEnabled()) {
			log.debug("Invoking getItemList");
			log.debug("itemInputDoc = " + SCXmlUtil.getString(itemInputDoc.getDocument()));
			log.debug("templateXml = " + templateXml);
		}

		Document itemListOutputDoc = api.invoke(env, "getItemList", itemInputDoc.getDocument());

		Element itemListOutputElem = itemListOutputDoc.getDocumentElement();
		return itemListOutputElem;
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

		StopWatchFor3359 sw = new StopWatchFor3359(true);
		Document xpxItemcustXrefListOutputDoc = api.executeFlow(env, "getXPXItemcustXrefList", xpxItemcustXrefInputDoc.getDocument());
		if (log4j.isDebugEnabled()) {
			log4j.debug("Completed API call getXPXItemcustXrefList (itemID=" + itemID + "): elapsed = " + sw.stop());
		}

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

package com.xpedx.nextgen.catalog.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
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
			Map<String, Set<String>> divisionsInStockForItem = getDivisionsInStockForAllItems(env, itemIDGroup, inStockStatus);
			for (Entry<String, Set<String>> entry : divisionsInStockForItem.entrySet()) {
				String itemID = entry.getKey();
				Set<String> divisionsInStock = entry.getValue();

				ItemMetadata im = new ItemMetadata();
				metadata.put(itemID, im);

				im.setDivisionsInStock(divisionsInStock);

				// this call is NOT batched, since the api limits output to 5000 records and some items have thousands of records (as of Aug 2014, one item has 4000+)
				Set<String> customerAndItemNumbers = getCustomerPartNumbersForItem(env, itemID);
				im.setCustomerAndItemNumbers(customerAndItemNumbers);
			}
		}

		return metadata;
	}

	// api calls batched for improved performance
	private Map<String, Set<String>> getDivisionsInStockForAllItems(YFSEnvironment env, Collection<? extends String> itemIDs, String inStockStatus) throws Exception {
		Map<String, Set<String>> divisionsInStockForItems = new LinkedHashMap<String, Set<String>>();

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

		//		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		//		System.out.println("itemInputDoc:\n" + SCXmlUtil.getString(itemInputDoc.getDocument()));
		//		Document itemListOutputDoc = fakeItemListResponse(); // XXX JUST FOR TESTING

		Element itemListOutputElem = itemListOutputDoc.getDocumentElement();
		//		System.out.println("========================================");
		//		System.out.println("itemListOutputElem:\n" + SCXmlUtil.getString(itemListOutputElem));

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

		//		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		//		System.out.println("xpxItemcustXrefInputDoc:\n" + SCXmlUtil.getString(xpxItemcustXrefInputDoc.getDocument()));
		//		Document xpxItemcustXrefListOutputDoc = fakeXPXItemcustXrefResponse();

		Element xpxItemcustXrefListOutputElem = xpxItemcustXrefListOutputDoc.getDocumentElement();
		//		System.out.println("========================================");
		//		System.out.println("xpxItemcustXrefListOutputElem:\n" + SCXmlUtil.getString(xpxItemcustXrefListOutputElem));

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
		private Set<String> divisionsInStock = new LinkedHashSet<String>();
		private Set<String> customerAndItemNumbers = new LinkedHashSet<String>();

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

//	// --- Debug/Testing
//
//	private static Document fakeItemListResponse() throws Exception {
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db = dbf.newDocumentBuilder();
//		return db.parse(new File("C:/Users/THOWA14/Desktop/3359/getItemList-output.xml"));
//	}
//
//	private static Document fakeXPXItemcustXrefResponse() throws Exception {
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db = dbf.newDocumentBuilder();
//		return db.parse(new File("C:/Users/THOWA14/Desktop/3359/XPXItemcustXref-output.xml"));
//	}

	public static void main(String[] args) throws Exception {
		List<String> allItemIds = new ArrayList<String>();
		allItemIds.add("2001000");
		allItemIds.add("2001002");

		Map<String, ItemMetadata> metadata = new ItemIndexUtil().getMetadataForItems(null, allItemIds, "W");
		for (Entry<String, ItemMetadata> entry : metadata.entrySet()) {
			System.out.println("-----------------------------------");
			System.out.println(entry.getKey() + " = " + join(entry.getValue().getDivisionsInStock(), " "));
			System.out.println(entry.getKey() + " = " + join(entry.getValue().getCustomerAndItemNumbers(), " "));
		}
	}

}

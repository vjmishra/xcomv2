package com.xpedx.nextgen.catalog.api;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.woodstock.util.frame.Manager;
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

		dumpInputXml(inDocumentUE, "input");
		removeUnentitledItems(inDocumentUE);
		dumpInputXml(inDocumentUE, "filtered");

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

	/**
	 * Writes the input xml to a file in the temp directory. This method is guaranteed to not throw exceptions.
	 */
	private static void dumpInputXml(Document inDocumentUE, String label) {
		String filename = "XPEDXGetAdditionalCatalogIndexInformationUE" + "_" + new Date().getTime() + "_" + label + ".xml";
		File xmlFile = new File(System.getProperty("java.io.tmpdir"), filename);
		System.out.println("Logging inDocumentUE to file: " + xmlFile);

		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(inDocumentUE);
			StreamResult result = new StreamResult(xmlFile);
			transformer.transform(source, result);

		} catch (Exception e) {
			// if unable to write the input xml, at least record that an error occurred. do NOT rethrow exception here
			Writer writer = null;
			try {
				writer = new FileWriter(xmlFile);
				writer.write("Failed to write inDocumentUE to this file. Exception.message: " + e.getMessage() + "\n");
				writer.flush();

			} catch (Exception ignore) {
				// do nothing

			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (Exception ignore2) {
					}
				}
			}
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

	// TODO integrate this with Jim's query for entitled items
	private static void removeUnentitledItems(Document inDocumentUE) {
		Element searchIndexTriggerElem = inDocumentUE.getDocumentElement();
		Element itemListElem = SCXmlUtil.getChildElement(searchIndexTriggerElem, "ItemList");
		List<Element> itemElems = SCXmlUtil.getElements(itemListElem, "Item");

		Set<String> allItemIDs = new LinkedHashSet<String>(itemElems.size());
		for (Element itemElem : itemElems) {
			String itemID = itemElem.getAttribute("ItemID");
			allItemIDs.add(itemID);
		}

		Set<String> entitledItemIDs = pocGetEntitledItemIDs(allItemIDs);

		// remove the unentitled items from the dom
		for (Element itemElem : itemElems) {
			String itemID = itemElem.getAttribute("ItemID");
			if (!entitledItemIDs.contains(itemID)) {
				itemListElem.removeChild(itemElem);
			}
		}

//		System.out.println(SCXmlUtil.getElements(itemListElem, "Item").size());
	}

	private static Set<String> pocGetEntitledItemIDs(Set<String> allItemIDs) {
		// TODO if this works, will refactor into an API call of some sort
		//      guaranteed to not throw any exceptions since this query will fail in non-dev environments
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement stmt = conn.prepareStatement("select item_id from trey_entitled_item where item_id in (" + createQuestionMarks(allItemIDs.size()) + ")");

			int parameterIndex = 1;
			for (String itemID : allItemIDs) {
				stmt.setString(parameterIndex++, itemID);
			}

			ResultSet res = stmt.executeQuery();

			Set<String> entitledItemIDs = new LinkedHashSet<String>();
			while (res.next()) {
				entitledItemIDs.add(res.getString("item_id"));
			}

			System.out.println("Entitlement query succeeded: " + entitledItemIDs.size() + " / " + allItemIDs.size() + " are entitled items");

			return entitledItemIDs;

		} catch (Exception ignore) {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ignore2) {
				}
			}
			return allItemIDs;
		}
	}

	private static String createQuestionMarks(int num) {
		StringBuilder buf = new StringBuilder(2 * num);
		for (int i = 0; i < num; i++) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append("?");
		}
		return buf.toString();
	}

	private static Connection getConnection() throws ClassNotFoundException, SQLException {
		String jdbcDriver = Manager.getProperty("jdbcService", "oraclePool.driver");
		String jdbcURL = Manager.getProperty("jdbcService", "oraclePool.url");
		String jdbcUser = Manager.getProperty("jdbcService", "oraclePool.user");
		String jdbcPassword = Manager.getProperty("jdbcService", "oraclePool.password");

		Class.forName(jdbcDriver);
		return DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
	}

//	public static void main(String[] args) throws Exception {
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder = factory.newDocumentBuilder();
//		Document inDocumentUE = builder.parse(new File("C:/Users/THOWA14/Desktop/8038/XPEDXGetAdditionalCatalogIndexInformationUE-input-1.xml"));
//
//		Set<String> entitledItemIDs = new LinkedHashSet<String>(Arrays.asList("5421865", "2278085"));
//
//		removeUnentitledItems(inDocumentUE, entitledItemIDs);
//	}

}

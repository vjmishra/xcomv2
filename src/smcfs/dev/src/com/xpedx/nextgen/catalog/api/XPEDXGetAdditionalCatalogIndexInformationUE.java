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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

	private long started = new Date().getTime();

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

			long stopped = System.currentTimeMillis();
			System.out.println(started + ": total time = " + (started - stopped));

			return outDoc.getDocument();

		} catch (Exception ex) {
			log.error("Problem creating Search Index additional info in user exit", ex);
			throw new YFSUserExitException(ex.getMessage());
		}
	}

	/**
	 * Writes the input xml to a file in the temp directory. This method is guaranteed to not throw exceptions.
	 */
	private void dumpInputXml(Document inDocumentUE, String label) {
		String filename = String.format("XPEDXGetAdditionalCatalogIndexInformationUE_%s_%s.xml", started, label);
		File xmlFile = new File(System.getProperty("java.io.tmpdir"), filename);
		System.out.println(started + ": Logging inDocumentUE to file: " + xmlFile);

		Writer writer = null;
		try {
			writer = new FileWriter(xmlFile);

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(inDocumentUE);
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);

			writer.flush();

		} catch (Exception e) {
			// if unable to write the input xml, at least record that an error occurred. do NOT rethrow exception here
			try {
				writer.write("Failed to write inDocumentUE to this file. Exception.message: " + e.getMessage() + "\n");
				writer.flush();

			} catch (Exception ignore) {
			}

		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception ignore2) {
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

	/**
	 * Modifies inDocumentUE to remove all Item elements that are not entitlemed (for any user). This method is guaranteed to not throw exceptions.
	 */
	private void removeUnentitledItems(Document inDocumentUE) {
		try {
			Element searchIndexTriggerElem = inDocumentUE.getDocumentElement();
			Element itemListElem = SCXmlUtil.getChildElement(searchIndexTriggerElem, "ItemList");
			List<Element> itemElems = SCXmlUtil.getElements(itemListElem, "Item");

			Set<String> allItemIDs = new LinkedHashSet<String>(itemElems.size());
			for (Element itemElem : itemElems) {
				String itemID = itemElem.getAttribute("ItemID");
				allItemIDs.add(itemID);
			}

			Set<String> entitledItemIDs = pocGetEntitledItemIDs(allItemIDs);

			System.out.println(started + ": " + entitledItemIDs.size() + " / " + allItemIDs.size() + " entitledItemIDs = " + entitledItemIDs);

			// remove the unentitled items from the dom
			for (Element itemElem : itemElems) {
				String itemID = itemElem.getAttribute("ItemID");
				if (!entitledItemIDs.contains(itemID)) {
					itemListElem.removeChild(itemElem);
				}
			}

		} catch (Exception ignore) {
		}
	}

	/**
	 * Finds the entitled item ids in <code>allItemIDs</code>. This method is guaranteed to not throw exceptions.
	 */
	private Set<String> pocGetEntitledItemIDs(Set<String> allItemIDs) {
		// TODO if this works, will refactor into an API call of some sort
		//      this query will fail in non-dev environments, so the catch block returns the all the item ids so as to preserve normal (non-filter) behavior
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		try {
			conn = getConnection();

			stmt = conn.prepareStatement("select trim(item_id) as item_id from trey_entitled_item where item_id in (" + createQuestionMarks(allItemIDs.size()) + ")");

			int parameterIndex = 1;
			for (String itemID : allItemIDs) {
				stmt.setString(parameterIndex++, pad(itemID, 40));
			}

			res = stmt.executeQuery();

			Set<String> entitledItemIDs = new LinkedHashSet<String>();
			while (res.next()) {
				entitledItemIDs.add(res.getString("item_id"));
			}

			System.out.println(started + ": Entitlement query succeeded: " + entitledItemIDs.size() + " / " + allItemIDs.size() + " are entitled items");

			return entitledItemIDs;

		} catch (Exception ignore) {
			System.out.print("started: ");
			ignore.printStackTrace(System.out);

			if (res != null) {
				try {
					res.close();
				} catch (Exception ignore2) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ignore2) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ignore2) {
				}
			}
			return allItemIDs;
		}
	}

	private String pad(String str, int len) {
		StringBuilder buf = new StringBuilder(len);

		buf.append(str);
		while (buf.length() < len) {
			buf.append(" ");
		}
		return buf.toString();
	}

	private String createQuestionMarks(int num) {
		StringBuilder buf = new StringBuilder(2 * num);
		for (int i = 0; i < num; i++) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append("?");
		}
		return buf.toString();
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		String jdbcDriver = Manager.getProperty("jdbcService", "oraclePool.driver");
		String jdbcURL = Manager.getProperty("jdbcService", "oraclePool.url");
		String jdbcUser = Manager.getProperty("jdbcService", "oraclePool.user");
		String jdbcPassword = Manager.getProperty("jdbcService", "oraclePool.password");

//		String jdbcDriver = "oracle.jdbc.OracleDriver";
//		String jdbcURL = "jdbc:oracle:thin:@oratst08.ipaper.com:1521:NGD1";
//		String jdbcUser = "ng";
//		String jdbcPassword = "ng1";

		Class.forName(jdbcDriver);
		return DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
	}

//	public static void main(String[] args) {
//		Set<String> allItemIDs = new LinkedHashSet<String>();
//		allItemIDs.add("2030733");
//		allItemIDs.add("2137515");
//		allItemIDs.add("2181279");
//		allItemIDs.add("2052954");
//		allItemIDs.add("2190881");
//		allItemIDs.add("5126797");
//		allItemIDs.add("5126835");
//		allItemIDs.add("5052667");
//
//		new XPEDXGetAdditionalCatalogIndexInformationUE().pocGetEntitledItemIDs(allItemIDs);
//	}

	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document inDocumentUE = builder.parse(new File("C:/Users/THOWA14/Desktop/8038/XPEDXGetAdditionalCatalogIndexInformationUE-input-1.xml"));

		new XPEDXGetAdditionalCatalogIndexInformationUE().removeUnentitledItems(inDocumentUE);
	}

}

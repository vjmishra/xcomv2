package com.xpedx.nextgen.catalog.api;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.catalog.api.trey4748.StopWatch;
import com.xpedx.nextgen.catalog.api.trey4748.Trey4748SmcfsLogging;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.custom.dbi.XPX_Item_Associations;
import com.yantra.custom.dbi.XPX_Item_Extn;
import com.yantra.custom.dbi.XPX_Itemcust_Xref;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.shared.dbclasses.XPX_Item_AssociationsDBHome;
import com.yantra.shared.dbclasses.XPX_Item_ExtnDBHome;
import com.yantra.shared.dbclasses.XPX_Itemcust_XrefDBHome;
import com.yantra.shared.dbclasses.YFS_ItemDBHome;
import com.yantra.shared.dbclasses.YFS_Item_UOMDBHome;
import com.yantra.shared.dbclasses.YPM_Pricelist_AssignmentDBHome;
import com.yantra.shared.dbclasses.YPM_Pricelist_LineDBHome;
import com.yantra.shared.dbi.YFS_Item;
import com.yantra.shared.dbi.YFS_Item_UOM;
import com.yantra.shared.dbi.YPM_Pricelist_Assignment;
import com.yantra.shared.dbi.YPM_Pricelist_Line;
import com.yantra.shared.ycp.YFSContext;
import com.yantra.yfc.dblayer.PLTQueryBuilder;
import com.yantra.yfc.dblayer.PLTQueryBuilderHelper;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/*
 * This Service is create for performance improvement of Catalog page - By Amar
 */
public class XPXCatalogAllAPI implements YIFCustomApi {

	private static final Logger log4j = Logger.getLogger(XPXCatalogAllAPI.class);

	/** API object. */
	private static YIFApi api = null;// dfsdzcvvc
	private static YFCLogCategory log;
	private String lowestConvUOM = "";
	private int currentConversion;
	private String ExtnIsCustUOMExcl = "";
	private String baseUOM = "";
	private boolean complexQuery = false;
	private Document itemsXrefDoc;
	private Document itemExtnDoc;
	private String companyCode="";
	private String customerDivision="" ;
	private String customerItemNumber="";
	private String customerNumber="";
	static {
		try {
			log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {
			log.debug(e1.getMessage());
		}
	}

	@Override
	public void setProperties(Properties arg0) throws Exception {
	}

	public Document getAllCatalogAPI(YFSEnvironment env, Document inputXML) throws Exception {
		StopWatch sw = new StopWatch();

		Document outputDoc = SCXmlUtil.createDocument("XPXCatalogAllAPIService");

		sw.start();
		getPriceListElement(env, inputXML, outputDoc);
		sw.stop();
		Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "getPriceListElement");
		sw.reset();

		sw.start();
		getCustXrefList(env, inputXML, outputDoc);
		sw.stop();
		Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "getCustXrefList");
		sw.reset();

		sw.start();
		getXPXItemExtnElement(env, inputXML, outputDoc);
		sw.stop();
		Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "getXPXItemExtnElement");
		sw.reset();

		Element uomList = (Element) inputXML.getElementsByTagName("UOMList").item(0);

		if (uomList != null) {
			sw.start();
			getXpedxUOMList(env, SCXmlUtil.createFromString(SCXmlUtil.getString(uomList)), outputDoc);
			sw.stop();
			Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "getXpedxUOMList: uomList=%s", SCXmlUtil.getString(uomList));
			sw.reset();
		}
		return outputDoc;
	}

	private Element getCustXrefList(YFSEnvironment env, Document inputXML, Document outputDoc) throws SQLException {
		StopWatch sw = new StopWatch();

		Element custXrefElement = null;
		if (itemsXrefDoc == null) {
			custXrefElement = SCXmlUtil.createChild(outputDoc.getDocumentElement(), "XPXItemcustXrefList");
		} else {
			custXrefElement = (Element) outputDoc.getElementsByTagName("XPXItemcustXrefList").item(0);
		}

		try {
			Element custXrefList = (Element) inputXML.getElementsByTagName("XPXItemcustXref").item(0);

			companyCode = custXrefList.getAttribute("CompanyCode");
			customerNumber = custXrefList.getAttribute("CustomerNumber");
			customerDivision = custXrefList.getAttribute("CustomerDivision");
			PLTQueryBuilder pltqbItemCustXref = PLTQueryBuilderHelper.createPLTQueryBuilder();
			pltqbItemCustXref.setCurrentTable("XPX_ITEMCUST_XREF");
			pltqbItemCustXref.appendString("CUSTOMER_NUMBER", "=", customerNumber);
			pltqbItemCustXref.appendString(" AND ENVIRONMENT_CODE", "=", custXrefList.getAttribute("EnvironmentCode"));
			if (!YFCCommon.isVoid(companyCode)) {
				pltqbItemCustXref.appendString(" AND COMPANY_CODE", "=", companyCode);
			}
			pltqbItemCustXref.append("AND CUSTOMER_DIVISION ='" + customerDivision + "'");
			NodeList legacyItemNumberList = custXrefList.getElementsByTagName("Exp");
			if (legacyItemNumberList == null) {
				return custXrefElement;
			}

			pltqbItemCustXref.append(" AND LEGACY_ITEM_NUMBER IN ('" + ((Element) legacyItemNumberList.item(0)).getAttribute("Value") + "'");
			for (int i = 1; i < legacyItemNumberList.getLength(); i++) {
				Element custRefElem = (Element) legacyItemNumberList.item(i);
				pltqbItemCustXref.append(",'" + custRefElem.getAttribute("Value") + "'");
			}
			pltqbItemCustXref.append(")");

			Trey4748SmcfsLogging.getInstance().snapshot(env, -1, "BEFORE query xpxItemxrefList: pltqbItemCustXref=%s", pltqbItemCustXref.getReadableWhereClause());
			sw.start();
			List<XPX_Itemcust_Xref> xpxItemxrefList = XPX_Itemcust_XrefDBHome.getInstance().listWithWhere((YFSContext) env, pltqbItemCustXref, 5000);
			sw.stop();
			Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "query xpxItemxrefList: pltqbItemCustXref=%s, xpxItemxrefList.size=%s", pltqbItemCustXref.getReadableWhereClause(), xpxItemxrefList.size());
			sw.reset();

			sw.start();
			Iterator<XPX_Itemcust_Xref> xpxItemxrefIter = xpxItemxrefList.iterator();
			while (xpxItemxrefIter.hasNext()) {
				XPX_Itemcust_Xref xpxItemxref = xpxItemxrefIter.next();
				Element itemXrefEleme = SCXmlUtil.createChild(custXrefElement, "XPXItemcustXref");
				itemXrefEleme.setAttribute("ItemcustRefKey", xpxItemxref.getItemcustRefKey());
				itemXrefEleme.setAttribute("CustomerNumber", xpxItemxref.getCustomer_Number());
				itemXrefEleme.setAttribute("MPC", xpxItemxref.getMpc());
				itemXrefEleme.setAttribute("LegacyItemNumber", xpxItemxref.getLegacy_Item_Number());
				itemXrefEleme.setAttribute("CustomerPartNumber", xpxItemxref.getCustomer_Part_Number());
				itemXrefEleme.setAttribute("CustomerItemNumber", xpxItemxref.getCustomer_Item_Number());
				itemXrefEleme.setAttribute("CustomerDecription", xpxItemxref.getCustomer_Description());
				itemXrefEleme.setAttribute("CustomerUnit", xpxItemxref.getCustomer_Unit());
				itemXrefEleme.setAttribute("ConvFactor", "" + xpxItemxref.getConv_Factor());
				itemXrefEleme.setAttribute("IsCustUOMExcl", xpxItemxref.getIs_Cust_Uom_Excl());
				itemXrefEleme.setAttribute("EnvironmentCode", xpxItemxref.getEnvironment_Code());
				itemXrefEleme.setAttribute("CompanyCode", xpxItemxref.getCompany_Code());
				itemXrefEleme.setAttribute("CustomerDivision", xpxItemxref.getCustomer_Division());
				itemXrefEleme.setAttribute("LegacyUom", xpxItemxref.getLegacy_Uom());
				itemXrefEleme.setAttribute("CustomerUom", xpxItemxref.getCustomer_Uom());
				itemXrefEleme.setAttribute("Createts", "" + xpxItemxref.getCreatets());
				itemXrefEleme.setAttribute("Modifyts", "" + xpxItemxref.getModifyts());
			}
			sw.stop();
			Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "iterate xpxItemxrefList: xpxItemxrefList.size=%s", xpxItemxrefList.size());
			sw.reset();

		} catch (Exception e) {
			log.error("", e);
			log4j.error("", e);
			Trey4748SmcfsLogging.getInstance().snapshot(env, -1, "ERROR getCustXrefList");
		}
		setItemXrefDoc(outputDoc);
		return custXrefElement;
	}

	private Element getXPXItemExtnElement(YFSEnvironment env, Document inputXML, Document outputDoc) throws SQLException {
		StopWatch sw = new StopWatch();

		Element xpxItemExtnList = null;
		if (itemExtnDoc == null) {
			xpxItemExtnList = SCXmlUtil.createChild(outputDoc.getDocumentElement(), "XPXItemExtnList");
		} else {
			xpxItemExtnList = (Element) outputDoc.getElementsByTagName("XPXItemExtnList").item(0);
		}

		try {
			Element itemExtnElelement = (Element) inputXML.getElementsByTagName("XPXItemExtn").item(0);
			if (itemExtnElelement == null) {
				return null;
			}

			PLTQueryBuilder pltqbItemExtn = PLTQueryBuilderHelper.createPLTQueryBuilder();
			pltqbItemExtn.setCurrentTable("XPX_ITEM_EXTN");
			pltqbItemExtn.appendString("ENVIRONMENT_ID", "=", itemExtnElelement.getAttribute("EnvironmentID"));
			pltqbItemExtn.append(" AND XPX_DIVISION ='" + itemExtnElelement.getAttribute("XPXDivision") + "'");

			// checking if association is required query xpx_item_association
			String isAssociationRequired = itemExtnElelement.getAttribute("IsAssociationRequired");
			PLTQueryBuilder pltqbItemAssociation = PLTQueryBuilderHelper.createPLTQueryBuilder();
			if ("Y".equals(isAssociationRequired)) {
				pltqbItemAssociation.setCurrentTable("XPX_ITEM_ASSOCIATIONS");
				pltqbItemAssociation.append(" ITEM_EXTN_KEY IN(");
			}

			NodeList itemIDList = itemExtnElelement.getElementsByTagName("Exp");
			pltqbItemExtn.append(" AND ITEM_ID IN ('" + ((Element) itemIDList.item(0)).getAttribute("Value") + "'");
			for (int i = 1; i < itemIDList.getLength(); i++) {
				Element custRefElem = (Element) itemIDList.item(i);
				pltqbItemExtn.append(",'" + custRefElem.getAttribute("Value") + "'");
			}
			pltqbItemExtn.append(")");

			Trey4748SmcfsLogging.getInstance().snapshot(env, -1, "BEFORE query xpxItemxrefList: pltqbItemExtn=%s", pltqbItemExtn.getReadableWhereClause());
			sw.start();
			List<XPX_Item_Extn> xpxItemExtns = XPX_Item_ExtnDBHome.getInstance().listWithWhere((YFSContext) env, pltqbItemExtn, 5000);
			sw.stop();
			Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "query xpxItemxrefList: pltqbItemExtn=%s, xpxItemExtns.size=%s", pltqbItemExtn.getReadableWhereClause(), xpxItemExtns.size());
			sw.reset();

			sw.start();
			Iterator<XPX_Item_Extn> xpxItemExtnIter = xpxItemExtns.iterator();
			boolean isFirstRecord = true;
			while (xpxItemExtnIter.hasNext()) {
				XPX_Item_Extn xpxItemExtn = xpxItemExtnIter.next();
				Element itemXrefEleme = SCXmlUtil.createChild(xpxItemExtnList, "XPXItemExtn");
				if (isFirstRecord && "Y".equals(isAssociationRequired)) {
					pltqbItemAssociation.append("'").append(xpxItemExtn.getItemExtnKey()).append("'");
					isFirstRecord = false;
				} else if ("Y".equals(isAssociationRequired)) {
					pltqbItemAssociation.append(",'").append(xpxItemExtn.getItemExtnKey()).append("'");
				}
				itemXrefEleme.setAttribute(XPX_Item_Extn.ITEM_KEY, xpxItemExtn.getItem_Key());
				itemXrefEleme.setAttribute(XPX_Item_Extn.ITEMEXTNKEY, xpxItemExtn.getItemExtnKey());
				itemXrefEleme.setAttribute(XPX_Item_Extn.ENVIRONMENTID, xpxItemExtn.getEnvironmentID());
				itemXrefEleme.setAttribute(XPX_Item_Extn.COMPANYCODE, xpxItemExtn.getCompanyCode());
				itemXrefEleme.setAttribute(XPX_Item_Extn.ITEMID, xpxItemExtn.getItemID());
				itemXrefEleme.setAttribute(XPX_Item_Extn.MASTERPRODUCTCODE, xpxItemExtn.getMasterProductCode());
				itemXrefEleme.setAttribute(XPX_Item_Extn.XPXDIVISION, xpxItemExtn.getXPXDivision());
				itemXrefEleme.setAttribute(XPX_Item_Extn.ITEMSTOCKSTATUS, xpxItemExtn.getItemStockStatus());
				itemXrefEleme.setAttribute(XPX_Item_Extn.ORDERMULTIPLE, "" + xpxItemExtn.getOrderMultiple());
				itemXrefEleme.setAttribute(XPX_Item_Extn.INVENTORYINDICATOR, xpxItemExtn.getInventoryIndicator());
				itemXrefEleme.setAttribute(XPX_Item_Extn.ORIGINALINDICATOR, xpxItemExtn.getOriginalIndicator());
				itemXrefEleme.setAttribute(XPX_Item_Extn.CREATETS, "" + xpxItemExtn.getCreatets());
				itemXrefEleme.setAttribute(XPX_Item_Extn.MODIFYTS, "" + xpxItemExtn.getModifyts());
			}
			sw.stop();
			Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "iterate xpxItemxrefList: xpxItemExtns.size=%s", xpxItemExtns.size());
			sw.reset();

			// Adding XPX_ITEM_ASSOCIATION Element in Parent XPX_ITEM_EXTN
			if (!isFirstRecord && "Y".equals(isAssociationRequired)) {
				pltqbItemAssociation.append(")");

				Trey4748SmcfsLogging.getInstance().snapshot(env, -1, "BEFORE query xpxItemAssociations: pltqbItemAssociation=%s", pltqbItemAssociation.getReadableWhereClause());
				sw.start();
				List<XPX_Item_Associations> xpxItemAssociations = XPX_Item_AssociationsDBHome.getInstance().listWithWhere((YFSContext) env, pltqbItemAssociation, 5000);
				sw.stop();
				Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "query xpxItemAssociations: pltqbItemAssociation=%s, xpxItemAssociations.size=%s", pltqbItemAssociation.getReadableWhereClause(), xpxItemAssociations.size());
				sw.reset();

				ArrayList<Element> _xpxItemExtnList = SCXmlUtil.getElements(xpxItemExtnList, "XPXItemExtn");
				Map<String, List<XPX_Item_Associations>> associationMap = new HashMap<String, List<XPX_Item_Associations>>();
				// creating input xml so that we can get xpx_item_extn and xpx_item_cust_xref
				boolean isXPXItemExtnCall = false;
				boolean isXPXItemCustXrefCall = false;
				Document _xpxItemExtnDoc = SCXmlUtil.createDocument("XPXItemExtn");
				Element _xpxItemExtnEle = _xpxItemExtnDoc.getDocumentElement();
				_xpxItemExtnEle.setAttribute("EnvironmentID", itemExtnElelement.getAttribute("EnvironmentID"));
				_xpxItemExtnEle.setAttribute("IsAssociationRequired", "N");
				_xpxItemExtnEle.setAttribute("XPXDivision", itemExtnElelement.getAttribute("XPXDivision"));
				Element xpxItemExtnComplexQuery = SCXmlUtil.createChild(_xpxItemExtnEle, "ComplexQuery");
				Element xpxItemExtnOrElem = SCXmlUtil.createChild(xpxItemExtnComplexQuery, "Or");

				Document _xpxItemCustXrefDoc = SCXmlUtil.createDocument("XPXItemcustXref");
				Element _xpxItemCustXrefEle = _xpxItemCustXrefDoc.getDocumentElement();
				_xpxItemCustXrefEle.setAttribute("CompanyCode", companyCode);
				_xpxItemCustXrefEle.setAttribute("CustomerDivision", customerDivision);
				_xpxItemCustXrefEle.setAttribute("CustomerItemNumber", customerItemNumber);
				_xpxItemCustXrefEle.setAttribute("CustomerNumber", customerNumber);
				_xpxItemCustXrefEle.setAttribute("EnvironmentCode", itemExtnElelement.getAttribute("EnvironmentID"));
				Element xrefComplexQuery = SCXmlUtil.createChild(_xpxItemCustXrefEle, "ComplexQuery");
				Element xrefOrElem = SCXmlUtil.createChild(xrefComplexQuery, "Or");

				if (xpxItemAssociations != null && _xpxItemExtnList != null) {
					sw.start();
					for (XPX_Item_Associations xpxItemAssociation : xpxItemAssociations) {
						List<XPX_Item_Associations> associationsList = new ArrayList<XPX_Item_Associations>();
						if (associationMap.get(xpxItemAssociation.getItemExtnKey()) != null) {
							associationsList = associationMap.get(xpxItemAssociation.getItemExtnKey());
						}
						associationsList.add(xpxItemAssociation);
						associationMap.put(xpxItemAssociation.getItemExtnKey(), associationsList);
						if (!YFCCommon.isVoid(xpxItemAssociation.getAssociatedItemID())) {
							Element xreExpElem = SCXmlUtil.createChild(xrefOrElem, "Exp");
							xreExpElem.setAttribute("Name", "LegacyItemNumber");
							xreExpElem.setAttribute("Value", xpxItemAssociation.getAssociatedItemID());
							xreExpElem.setAttribute("QryType", "EQ");

							Element xpxItemExtnExpElem = SCXmlUtil.createChild(xpxItemExtnOrElem, "Exp");
							xpxItemExtnExpElem.setAttribute("Name", "ItemID");
							xpxItemExtnExpElem.setAttribute("Value", xpxItemAssociation.getAssociatedItemID());
							// Adding item to get association item UOM
							Element uomList = (Element) inputXML.getElementsByTagName("UOMList").item(0);

							if (uomList != null) {
								Element uomOr = (Element) uomList.getElementsByTagName("Or").item(0);
								if (uomOr != null) {
									Element uomExp = SCXmlUtil.createChild(uomOr, "Exp");
									uomExp.setAttribute("Name", "ItemID");
									uomExp.setAttribute("Value", xpxItemAssociation.getAssociatedItemID());
								}
							}
							// adding for uom end
							isXPXItemExtnCall = true;
							isXPXItemCustXrefCall = true;
						}
					}
					sw.stop();
					Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "iterate xpxItemAssociations: xpxItemAssociations.size=%s", xpxItemAssociations.size());
					sw.reset();

					sw.start();
					for (Element _xpxItemExtn : _xpxItemExtnList) {
						Element xpxItemAssocEleme = SCXmlUtil.createChild(_xpxItemExtn, "XPXItemAssociationsList");
						if (associationMap != null) {
							List<XPX_Item_Associations> associationList = associationMap.get(_xpxItemExtn.getAttribute(XPX_Item_Extn.ITEMEXTNKEY));
							if (associationList != null) {
								for (XPX_Item_Associations xpxItemAssociation : associationList) {
									Element xpxItemAssociationEleme = SCXmlUtil.createChild(xpxItemAssocEleme, "XPXItemAssociations");
									xpxItemAssociationEleme.setAttribute(XPX_Item_Associations.ITEMEXTNKEY, xpxItemAssociation.getItemExtnKey());
									xpxItemAssociationEleme.setAttribute(XPX_Item_Associations.ASSOCIATIONTYPE, xpxItemAssociation.getAssociationType());
									xpxItemAssociationEleme.setAttribute(XPX_Item_Associations.ASSOCIATEDITEMID, xpxItemAssociation.getAssociatedItemID());
									xpxItemAssociationEleme.setAttribute(XPX_Item_Associations.ITEMASSOCIATIONKEY, xpxItemAssociation.getItemAssociationKey());
								}
							}
						}
					}
					sw.stop();
					Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "iterate _xpxItemExtnList: _xpxItemExtnList.size=%s", _xpxItemExtnList.size());
					sw.reset();

					setItemExtnDoc(outputDoc);
					if (isXPXItemExtnCall) {
						getXPXItemExtnElement(env, _xpxItemExtnDoc, outputDoc);
					}
					if (isXPXItemCustXrefCall) {
						getCustXrefList(env, _xpxItemCustXrefDoc, outputDoc);
					}
				}
			}
		} catch (Exception e) {
			log.error("", e);
			log4j.error("", e);
			Trey4748SmcfsLogging.getInstance().snapshot(env, -1, "ERROR getXPXItemExtnElement");
		}
		return xpxItemExtnList;
	}

	private Element getPriceListElement(YFSEnvironment env, Document inputXML, Document outputDoc) throws SQLException {
		StopWatch sw = new StopWatch();

		Element priceElement = SCXmlUtil.createChild(outputDoc.getDocumentElement(), "PricelistLineList");
		// getting price list details
		try {
			Element pricelistAssignmentElement = (Element) inputXML.getElementsByTagName("PricelistAssignment").item(0);

			if (pricelistAssignmentElement != null) {
				String customerID = pricelistAssignmentElement.getAttribute("CustomerID");
				String priceWareHouse = pricelistAssignmentElement.getAttribute("ExtnPriceWareHouse");
				PLTQueryBuilder pltqbPriceListAssignment = PLTQueryBuilderHelper.createPLTQueryBuilder();
				pltqbPriceListAssignment.setCurrentTable("YPM_PRICELIST_ASSIGNMENT");
				pltqbPriceListAssignment.append(" PRICELIST_HDR_KEY IN(SELECT DISTINCT PRICELIST_HDR_KEY FROM YPM_PRICELIST_HDR WHERE EXTN_PRICING_WAREHOUSE ='" + priceWareHouse + "' AND PRICING_STATUS = 'ACTIVE' )");
				pltqbPriceListAssignment.append(" AND CUSTOMER_ID ='" + customerID + "'");
				// pltQryBuilder.appendString(" AND PRICING_STATUS", "=","ACTIVE");

				Trey4748SmcfsLogging.getInstance().snapshot(env, -1, "BEFORE query priceListAssignments: pltqbPriceListAssignment=%s", pltqbPriceListAssignment.getReadableWhereClause());
				sw.start();
				List<YPM_Pricelist_Assignment> priceListAssignments = YPM_Pricelist_AssignmentDBHome.getInstance().listWithWhere((YFSContext) env, pltqbPriceListAssignment, 5000);
				sw.stop();
				Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "query priceListAssignments: pltqbPriceListAssignment=%s, priceListAssignments.size=%s", pltqbPriceListAssignment.getReadableWhereClause(), priceListAssignments.size());
				sw.reset();

				Iterator<YPM_Pricelist_Assignment> priceListIter = priceListAssignments.iterator();
				if (priceListIter.hasNext()) {
					PLTQueryBuilder pltqbPriceListLineList = PLTQueryBuilderHelper.createPLTQueryBuilder();
					pltqbPriceListLineList.setCurrentTable("YPM_PRICELIST_LINE");
					pltqbPriceListLineList.append("PRICING_STATUS ='ACTIVE' ");
					if (priceListIter.hasNext()) {
						YPM_Pricelist_Assignment pricelistAssignment = priceListIter.next();
						pltqbPriceListLineList.append("AND pricelist_hdr_key IN  ('" + pricelistAssignment.getPricelist_Header_Key() + "'");
					} else {
						return priceElement;
					}

					while (priceListIter.hasNext()) {
						YPM_Pricelist_Assignment pricelistAssignment = priceListIter.next();
						pltqbPriceListLineList.append(" ,'" + pricelistAssignment.getPricelist_Header_Key() + "'");
					}
					pltqbPriceListLineList.append(")");

					NodeList itemIdComplexQuery = pricelistAssignmentElement.getElementsByTagName("Exp");
					if (itemIdComplexQuery == null) {
						return priceElement;
					}

					pltqbPriceListLineList.append(" AND ITEM_ID IN ('" + ((Element) itemIdComplexQuery.item(0)).getAttribute("Value") + "'");
					for (int i = 1; i < itemIdComplexQuery.getLength(); i++) {
						Element itemIDElement = (Element) itemIdComplexQuery.item(i);
						pltqbPriceListLineList.append(" ,'" + itemIDElement.getAttribute("Value") + "'");
					}
					pltqbPriceListLineList.append(")");

					Trey4748SmcfsLogging.getInstance().snapshot(env, -1, "BEFORE query priceListLineList: pltqbPriceListLineList=%s", pltqbPriceListLineList.getReadableWhereClause());
					sw.start();
					List<YPM_Pricelist_Line> priceListLineList = YPM_Pricelist_LineDBHome.getInstance().listWithWhere((YFSContext) env, pltqbPriceListLineList, 5000);
					sw.stop();
					Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "query priceListLineList: pltqbPriceListLineList=%s, priceListLineList.size=%s", pltqbPriceListLineList.getReadableWhereClause(), priceListLineList.size());
					sw.reset();

					sw.start();
					Iterator<YPM_Pricelist_Line> priceListLineIter = priceListLineList.iterator();
					while (priceListLineIter.hasNext()) {
						YPM_Pricelist_Line priceListLine = priceListLineIter.next();
						Element priceLineElement = SCXmlUtil.createChild(priceElement, "PricelistLine");
						priceLineElement.setAttribute("ItemID", priceListLine.getItem_ID());
						priceLineElement.setAttribute("FromQuantity", "" + priceListLine.getFrom_Quantity());
						priceLineElement.setAttribute("ListPrice", "" + priceListLine.getList_Price());
						Element priceLineExtnElement = SCXmlUtil.createChild(priceLineElement, "Extn");
						priceLineExtnElement.setAttribute("ExtnTierUom", priceListLine.getExtn_Extn_Tier_Uom());
						priceLineExtnElement.setAttribute("ExtnPricingUom", priceListLine.getExtn_Extn_Pricing_Uom());

					}
					sw.stop();
					Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "iterate priceListAssignments: priceListLineList.size=%s", priceListLineList.size());
					sw.reset();
				}
			}
		} catch (Exception e) {
			log.error("", e);
			log4j.error("", e);
			Trey4748SmcfsLogging.getInstance().snapshot(env, -1, "ERROR getPriceListElement");
		}
		return priceElement;
	}

	private Document createItemListDocument(List<YFS_Item> yfsItemList, List<YFS_Item_UOM> yfsItemUOMList) {
		Document itemListDoc = SCXmlUtil.createDocument("ItemList");

		Iterator<YFS_Item_UOM> yfsItemUOMIter = yfsItemUOMList.iterator();
		Map<String, ArrayList<YFS_Item_UOM>> itemUOMMap = new HashMap<String, ArrayList<YFS_Item_UOM>>();
		while (yfsItemUOMIter.hasNext()) {
			YFS_Item_UOM yfsItemUom = yfsItemUOMIter.next();
			ArrayList<YFS_Item_UOM> uomList = itemUOMMap.get(yfsItemUom.getItem_Key());
			if (uomList == null) {
				uomList = new ArrayList<YFS_Item_UOM>();
			}
			uomList.add(yfsItemUom);
			itemUOMMap.put(yfsItemUom.getItem_Key(), uomList);
		}

		Iterator<YFS_Item> yfsItemIter = yfsItemList.iterator();
		while (yfsItemIter.hasNext()) {
			YFS_Item yfsItem = yfsItemIter.next();

			Element itemElem = SCXmlUtil.createChild(itemListDoc.getDocumentElement(), "Item");
			itemElem.setAttribute(YFS_Item.CAN_USE_AS_SERVICE_TOOL, yfsItem.getCan_Use_As_Service_Tool());
			itemElem.setAttribute(YFS_Item.CREATEPROGID, yfsItem.getCreateprogid());
			itemElem.setAttribute(YFS_Item.CREATETS, "" + yfsItem.getCreatets());
			itemElem.setAttribute(YFS_Item.CREATEUSERID, yfsItem.getCreateuserid());
			itemElem.setAttribute("DisplayItemId", yfsItem.getItem_Id());
			itemElem.setAttribute(YFS_Item.GLOBAL_ITEM_ID, yfsItem.getGlobal_Item_Id());
			itemElem.setAttribute(YFS_Item.INHERIT_ATTRS_FROM_CLASSIFICATION, yfsItem.getInherit_Attrs_From_Classification());
			itemElem.setAttribute(YFS_Item.IS_SHIPPING_CNTR, yfsItem.getIs_Shipping_Cntr());
			itemElem.setAttribute(YFS_Item.ITEM_GROUP_CODE, yfsItem.getItem_Group_Code());
			itemElem.setAttribute(YFS_Item.ITEM_ID, yfsItem.getItem_Id());
			itemElem.setAttribute(YFS_Item.ITEM_KEY, yfsItem.getItem_Key());
			itemElem.setAttribute(YFS_Item.LOCKID, "" + yfsItem.getLockid());
			itemElem.setAttribute(YFS_Item.MAX_MODIFYTS, "" + yfsItem.getMax_ModifyTS());
			itemElem.setAttribute(YFS_Item.MODIFYPROGID, yfsItem.getModifyprogid());
			itemElem.setAttribute(YFS_Item.MODIFYTS, "" + yfsItem.getModifyts());
			itemElem.setAttribute(YFS_Item.MODIFYUSERID, yfsItem.getModifyuserid());
			itemElem.setAttribute(YFS_Item.ORGANIZATION_CODE, yfsItem.getOrganization_Code());
			itemElem.setAttribute("UnitOfMeasure", yfsItem.getUom());
			Element alternameUOMListElem = SCXmlUtil.createChild(itemElem, "AlternateUOMList");
			ArrayList<YFS_Item_UOM> uomList = itemUOMMap.get(yfsItem.getItem_Key());
			if (uomList != null) {
				for (YFS_Item_UOM yfsItemUom : uomList) {
					Element alternameUOMElem = SCXmlUtil.createChild(alternameUOMListElem, "AlternateUOM");
					alternameUOMElem.setAttribute("Height", "" + yfsItemUom.getHeight());
					alternameUOMElem.setAttribute("HeightUOM", yfsItemUom.getHeight_Uom());
					alternameUOMElem.setAttribute("IsOrderingUOM", yfsItemUom.getIs_Ordering_Uom());
					alternameUOMElem.setAttribute("ItemKey", yfsItemUom.getItem_Key());
					alternameUOMElem.setAttribute("Length", "" + yfsItemUom.getLength());
					alternameUOMElem.setAttribute("LengthUOM", yfsItemUom.getLength_Uom());
					alternameUOMElem.setAttribute("Quantity", "" + yfsItemUom.getQuantity());
					alternameUOMElem.setAttribute("UnitOfMeasure", yfsItemUom.getUom());
					alternameUOMElem.setAttribute("Weight", "" + yfsItemUom.getWeight());
					alternameUOMElem.setAttribute("WeightUOM", yfsItemUom.getWeight_Uom());
					alternameUOMElem.setAttribute("Width", "" + yfsItemUom.getWidth());
					alternameUOMElem.setAttribute("WidthUOM", yfsItemUom.getWidth_Uom());
				}
			}
		}
		return itemListDoc;
	}

	private Document getXpedxUOMList(YFSEnvironment env, Document inXML, Document outputXML) throws XPathExpressionException, YFSException, YIFClientCreationException,
			RemoteException {
		StopWatch sw = new StopWatch();

		log.beginTimer("XPXUOMListAPI:getUOMList started...");
		LinkedHashMap<String, String> wUOMsToConversionFactors = new LinkedHashMap<String, String>();
		String LegacyCustomerNumber = "";
		String useOrderMulUOMFlag = "";
		String orderMultiple = "";
		String customerDivision = "";
		String itemID = "";

		YFCDocument complexQueryOutDoc = YFCDocument.createDocument("ItemList");
		try {
			YFCDocument inDoc = YFCDocument.getDocumentFor(inXML);
			YFCElement complexQueryElement = inDoc.getDocumentElement().getChildElement("ComplexQuery");
			if (complexQueryElement != null) {
				complexQuery = true;
			}

			Element documentElement = inXML.getDocumentElement();
			String customerID = documentElement.getAttribute("CustomerID");
			itemID = documentElement.getAttribute("ItemID");
			String storeFrontId = documentElement.getAttribute("OrganizationCode");
			// Added to identify this request is from B2B order flow or not
			String entryType = documentElement.getAttribute("EntryType");

			String[] customerIDTokens = customerID.split("\\-");
			if (customerIDTokens != null && customerIDTokens.length > 1) {
				LegacyCustomerNumber = customerIDTokens[1];
			}

			HashMap<String, String> customerDetails = getCustomerDetails(env, inXML);
			if ("false".equals(customerDetails.get("isGetUOMCall"))) {
				return complexQueryOutDoc.getDocument();
			}

			companyCode = customerDetails.get("companyCode");
			customerDivision = customerDetails.get("customerDivision");
			useOrderMulUOMFlag = customerDetails.get("useOrderMulUOMFlag");
			PLTQueryBuilder pltqbItem = PLTQueryBuilderHelper.createPLTQueryBuilder();
			pltqbItem.setCurrentTable("YFS_ITEM");
			pltqbItem.append("organization_code ='" + storeFrontId + "'");
			if (complexQuery) {
				YFCNodeList<YFCElement> itemIDList = complexQueryElement.getElementsByTagName("Exp");
				pltqbItem.append("AND ITEM_ID IN('" + ((YFCElement) itemIDList.item(0)).getAttribute("Value") + "'");
				for (int i = 1; i < itemIDList.getLength(); i++) {
					YFCElement itemIdElem = (YFCElement) itemIDList.item(i);
					pltqbItem.append(" ,'" + itemIdElem.getAttribute("Value") + "'");
				}
				pltqbItem.append(")");

			} else {
				pltqbItem.appendString(" ( trim(ITEM_ID)", "=", itemID);
			}

			boolean isYFSItemCall = false;

			Trey4748SmcfsLogging.getInstance().snapshot(env, -1, "BEFORE query yfsItemList: pltqbItem=%s", pltqbItem.getReadableWhereClause());
			sw.start();
			List<YFS_Item> yfsItemList = YFS_ItemDBHome.getInstance().listWithWhere((YFSContext) env, pltqbItem, 5000);
			sw.stop();
			Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "query yfsItemList: pltqbItem=%s, yfsItemList.size=%s", pltqbItem.getReadableWhereClause(), yfsItemList.size());
			sw.reset();

			PLTQueryBuilder pltqbItemUom = PLTQueryBuilderHelper.createPLTQueryBuilder();
			pltqbItemUom.setCurrentTable("YFS_ITEM_UOM");
			Iterator<YFS_Item> yfsItemIter = yfsItemList.iterator();
			if (yfsItemIter.hasNext()) {
				YFS_Item yfsItem = yfsItemIter.next();
				pltqbItemUom.append(" ITEM_KEY IN('" + yfsItem.getItem_Key() + "'");
				isYFSItemCall = true;
			}
			while (yfsItemIter.hasNext()) {
				YFS_Item yfsItem = yfsItemIter.next();
				pltqbItemUom.append(" ,'" + yfsItem.getItem_Key() + "'");
			}
			pltqbItemUom.append(")");
			Document outputListDocument = null;
			if (isYFSItemCall) {
				Trey4748SmcfsLogging.getInstance().snapshot(env, -1, "BEFORE query yfsItemUOMList: pltqbItemUom=%s", pltqbItemUom.getReadableWhereClause());
				sw.start();
				List<YFS_Item_UOM> yfsItemUOMList = YFS_Item_UOMDBHome.getInstance().listWithWhere((YFSContext) env, pltqbItemUom, 5000);
				sw.stop();
				Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "query yfsItemUOMList: pltqbItemUom=%s, yfsItemUOMList.size=%s", pltqbItemUom.getReadableWhereClause(), yfsItemUOMList.size());
				sw.reset();

				sw.start();
				outputListDocument = createItemListDocument(yfsItemList, yfsItemUOMList);
				sw.stop();
				Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "createItemListDocument: yfsItemList.size=%s, yfsItemUOMList.size=%s", yfsItemList.size(), yfsItemUOMList.size());
				sw.reset();
			} else {
				sw.start();
				Document retval = complexQueryOutDoc.getDocument();
				sw.stop();
				Trey4748SmcfsLogging.getInstance().snapshot(env, sw.getTime(), "complexQueryOutDoc");
				sw.reset();
				return retval;
			}

			Element outputListElement = outputListDocument.getDocumentElement();
			NodeList itemListNodes = outputListElement.getChildNodes();
			int length = itemListNodes.getLength();
			ArrayList<String> itemIds = new ArrayList<String>();
			for (int i = 0; i < length; i++) {
				Node itemNode = itemListNodes.item(i);
				String tmpItemId = itemNode.getAttributes().getNamedItem("ItemID").getTextContent();
				itemIds.add(tmpItemId);
			}
			setItemXrefDoc(outputXML);
			setItemExtnDoc(outputXML);
			if (!complexQuery) {
				for (int i = 0; i < length; i++) {
					Node itemNode = itemListNodes.item(i);
					baseUOM = itemNode.getAttributes().getNamedItem("UnitOfMeasure").getTextContent();
					NodeList itemNodeChildren = itemNode.getChildNodes();
					int length1 = itemNodeChildren.getLength();
					for (int j = 0; j < length1; j++) {
						Node itemNodeChild = itemNodeChildren.item(j);
						if (itemNodeChild != null && itemNodeChild.getNodeName().equals("AlternateUOMList")) {
							handleAternateUOMs(itemNodeChild, wUOMsToConversionFactors);
						}
					}
					orderMultiple = getOrderMultipleValue(itemID, customerDetails);
				}
				handleXpxItemcustXrefList(itemID, LegacyCustomerNumber, customerDivision, useOrderMulUOMFlag, orderMultiple, wUOMsToConversionFactors, env, entryType);
				if (ExtnIsCustUOMExcl != null && ExtnIsCustUOMExcl.equals("Y")) {
					outputXML.appendChild((outputXML.getOwnerDocument().importNode(getOutputDocument(wUOMsToConversionFactors, ""), true)));
					return getOutputDocument(wUOMsToConversionFactors, "");
				}
				Document document = getOutputDocument(wUOMsToConversionFactors, lowestConvUOM);

				log.endTimer("XPXUOMListAPI:getUOMList ended...");
				outputXML.appendChild((outputXML.getOwnerDocument().importNode(document, true)));
				return document;
			} else {
				for (int i = 0; i < length; i++) {
					Node itemNode = itemListNodes.item(i);
					wUOMsToConversionFactors.clear();

					// including the fix by Prashath in the Complex Query also
					itemID = itemNode.getAttributes().getNamedItem("ItemID").getTextContent();
					baseUOM = itemNode.getAttributes().getNamedItem("UnitOfMeasure").getTextContent();

					NodeList itemNodeChildren = itemNode.getChildNodes();
					int length1 = itemNodeChildren.getLength();
					for (int j = 0; j < length1; j++) {
						Node itemNodeChild = itemNodeChildren.item(j);
						if (itemNodeChild != null && itemNodeChild.getNodeName().equals("AlternateUOMList")) {
							handleAternateUOMs(itemNodeChild, wUOMsToConversionFactors);
						}
					}
					orderMultiple = getOrderMultipleValue(itemID, customerDetails);
					handleXpxItemcustXrefList(itemID, LegacyCustomerNumber, customerDivision, useOrderMulUOMFlag, orderMultiple, wUOMsToConversionFactors, env, entryType);
					if (ExtnIsCustUOMExcl != null && ExtnIsCustUOMExcl.equals("Y")) {
						getComplexQueryOutputDocument(wUOMsToConversionFactors, "", complexQueryOutDoc, itemID);
					}
					getComplexQueryOutputDocument(wUOMsToConversionFactors, lowestConvUOM, complexQueryOutDoc, itemID);
				}
				log.endTimer("XPXUOMListAPI:getUOMList ended...");
				outputXML.getDocumentElement().appendChild((outputXML.importNode(complexQueryOutDoc.getDocument().getDocumentElement(), true)));
				return complexQueryOutDoc.getDocument();
			}
		} catch (Exception e) {
			log.error("", e);
			log4j.error("", e);
			Trey4748SmcfsLogging.getInstance().snapshot(env, -1, "ERROR getXpedxUOMList");
		}
		return complexQueryOutDoc.getDocument();
	}

	private HashMap<String, String> getCustomerDetails(YFSEnvironment env, Document inXML) throws YIFClientCreationException, YFSException, RemoteException {
		HashMap<String, String> customerDetails = new HashMap<String, String>();
		Element documentElement = inXML.getDocumentElement();
		Element customerDetailsElem = (Element) documentElement.getElementsByTagName("CustomerDetails").item(0);
		if (customerDetailsElem == null) {
			customerDetails.put("isGetUOMCall", "false");
			return customerDetails;
		} else {
			customerDetails.put("companyCode", customerDetailsElem.getAttribute("ExtnCompanyCode"));
			customerDetails.put("enviromentCode", customerDetailsElem.getAttribute("ExtnEnvironmentCode"));
			customerDetails.put("shipFromBranch", customerDetailsElem.getAttribute("ExtnShipFromBranch"));
			customerDetails.put("customerDivision", customerDetailsElem.getAttribute("ExtnCustomerDivision"));
			customerDetails.put("useOrderMulUOMFlag", customerDetailsElem.getAttribute("ExtnUseOrderMulUOMFlag"));
			return customerDetails;
		}
	}

	private void setItemXrefDoc(Document outputXML) {
		Document xrefDoc= YFCDocument.createDocument().getDocument();
		xrefDoc.appendChild(xrefDoc.importNode(outputXML.getElementsByTagName("XPXItemcustXrefList").item(0), true));
		itemsXrefDoc = xrefDoc;
	}

	private void setItemExtnDoc(Document outputXML) {
		Document itemExtnDoc= YFCDocument.createDocument().getDocument();
		itemExtnDoc.appendChild(itemExtnDoc.importNode(outputXML.getElementsByTagName("XPXItemExtnList").item(0), true));
		this.itemExtnDoc = itemExtnDoc;
	}

	private void handleAternateUOMs(Node itemNodeChild, HashMap<String, String> wUOMsToConversionFactors) {
		NodeList AternateUOMList = itemNodeChild.getChildNodes();
		int length2 = AternateUOMList.getLength();
		for (int k = 0; k < length2; k++) {
			Node AlternateUOM = AternateUOMList.item(k);
			NamedNodeMap namedNodeMap = AlternateUOM.getAttributes();
			Node IsOrderingUOMNode = namedNodeMap.getNamedItem("IsOrderingUOM");
			String IsOrderingUOM = IsOrderingUOMNode.getTextContent();
			if (IsOrderingUOM != null && IsOrderingUOM.equals("Y")) {
				Node unitOfMeasureNode = namedNodeMap.getNamedItem("UnitOfMeasure");
				String unitOfMeasure = unitOfMeasureNode.getTextContent();
				Node quantityNode = namedNodeMap.getNamedItem("Quantity");
				String quantity = quantityNode.getTextContent();
				wUOMsToConversionFactors.put(unitOfMeasure, quantity);
			}
		}
	}

	private String getOrderMultipleValue(String itemId, HashMap<String, String> custDetails) {
		String orderMultiple = "";
		String companyCode = custDetails.get("companyCode");
		String shipFromBranch = custDetails.get("shipFromBranch");
		ArrayList<Element> XpxItemExtnList = SCXmlUtil.getChildren(itemExtnDoc.getDocumentElement(), "XPXItemExtn[@ItemID=" + itemId + "]");
		int length3 = XpxItemExtnList.size();
		for (int m = 0; m < length3; m++) {
			Element XpxItemExtn = XpxItemExtnList.get(m);
			NamedNodeMap XpxItemExtnAttributes = XpxItemExtn.getAttributes();
			Node companyCodeNode = XpxItemExtnAttributes.getNamedItem("CompanyCode");
			String companyCodeL = companyCodeNode.getTextContent();
			Node XPXDivisionNode = XpxItemExtnAttributes.getNamedItem("XPXDivision");
			String XPXDivision = XPXDivisionNode.getTextContent();
			if (companyCodeL.equals(companyCode) && XPXDivision.equals(shipFromBranch)) {
				Node orderMultipleNode = XpxItemExtnAttributes.getNamedItem("OrderMultiple");
				orderMultiple = orderMultipleNode.getTextContent();
			}
		}
		return orderMultiple;
	}

	private void handleXpxItemcustXrefList(String itemID, String customerNumber, String customerBranch, String useOrderMulUOMFlag, String orderMultiple,
			HashMap<String, String> wUOMsToConversionFactors, YFSEnvironment env, String entryType) throws XPathExpressionException, YFSException, RemoteException,
			YIFClientCreationException {

		Node customerUnitNode = null;
		customerUOMList.clear();
		List<Element> XpxItemcustXrefList = XPXUtils.getElements(itemsXrefDoc.getDocumentElement(), "XPXItemcustXref[@LegacyItemNumber=" + itemID + "]");
		int length3 = XpxItemcustXrefList.size();
		for (int m = 0; m < length3; m++) {
			Node XpxItemcustXref = XpxItemcustXrefList.get(m);
			NamedNodeMap XpxItemcustXrefAttributes = XpxItemcustXref.getAttributes();
			Node ExtnIsCustUOMExclNode = XpxItemcustXrefAttributes.getNamedItem("IsCustUOMExcl");
			if (ExtnIsCustUOMExclNode != null) {
				ExtnIsCustUOMExcl = ExtnIsCustUOMExclNode.getTextContent();
			}

			if (entryType == null || entryType.trim().length() <= 0) {
				customerUnitNode = XpxItemcustXrefAttributes.getNamedItem("CustomerUom");
			} else {
				customerUnitNode = XpxItemcustXrefAttributes.getNamedItem("LegacyUom");
			}
			/******** Temporarily retained till new entries get added in CustomerXref table as per modified design ***/
			if (customerUnitNode == null) {
				customerUnitNode = XpxItemcustXrefAttributes.getNamedItem("CustomerUnit");
			}
			/*******************************************************************************************************/

			String customerUnit = customerUnitNode.getTextContent();
			Node ConvFactorNode = XpxItemcustXrefAttributes.getNamedItem("ConvFactor");
			String ConvFactor = ConvFactorNode.getTextContent();
			String legacyConvFact = wUOMsToConversionFactors.get(XpxItemcustXrefAttributes.getNamedItem("LegacyUom") != null ? XpxItemcustXrefAttributes.getNamedItem("LegacyUom").getTextContent() : "");
			if ((!YFCCommon.isVoid(ConvFactor)) && (!YFCCommon.isVoid(legacyConvFact)))
				ConvFactor = "" + (Float.parseFloat(ConvFactor) * Float.parseFloat(legacyConvFact));
			if (ExtnIsCustUOMExcl != null && ExtnIsCustUOMExcl.equals("Y")) {
				wUOMsToConversionFactors.clear();
			}
			if (customerUnit != null && !customerUnit.equalsIgnoreCase("")) {
				customerUOMList.add(customerUnit);
				wUOMsToConversionFactors.put(customerUnit, ConvFactor);
				return;
			}
			if (useOrderMulUOMFlag != null && useOrderMulUOMFlag.equals("Y")) {
				int conversion = getConversion(ConvFactor, orderMultiple);
				if (conversion != -1 && customerUnit != null && customerUnit.length() > 0) {
					if (currentConversion == 0 || (currentConversion != 0 && conversion < currentConversion)) {
						lowestConvUOM = customerUnit;
						currentConversion = conversion;
					}
				}
			}
			wUOMsToConversionFactors.put(customerUnit, ConvFactor);
		}
	}

	private Document getOutputDocument(HashMap<String, String> wUOMsToConversionFactors, String lowestConvUOM) {

		YFCDocument inputDocument = YFCDocument.createDocument("UOMList");
		YFCElement documentElement = inputDocument.getDocumentElement();
		// when there is no customer specific UOM
		if (baseUOM != null && baseUOM.trim().length() > 0 && (ExtnIsCustUOMExcl == null || ExtnIsCustUOMExcl.trim().length() == 0 || !ExtnIsCustUOMExcl.equals("Y"))) {
			YFCElement uOMElement = documentElement.createChild("UOM");
			uOMElement.setAttribute("UnitOfMeasure", baseUOM);
			uOMElement.setAttribute("Conversion", "1");
		}

		if (lowestConvUOM != null && lowestConvUOM.length() > 0) {
			YFCElement uOMElement = documentElement.createChild("UOM");
			uOMElement.setAttribute("UnitOfMeasure", lowestConvUOM);
			uOMElement.setAttribute("Conversion", wUOMsToConversionFactors.get(lowestConvUOM));
		}

		Set<String> set = wUOMsToConversionFactors.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			YFCElement uOMElement = documentElement.createChild("UOM");
			String UnitOfMeasure = (String) iterator.next();
			String Conversion = wUOMsToConversionFactors.get(UnitOfMeasure);
			if (!UnitOfMeasure.equals(lowestConvUOM)) {
				uOMElement.setAttribute("UnitOfMeasure", UnitOfMeasure);
				uOMElement.setAttribute("Conversion", Conversion);
				if (customerUOMList.contains(UnitOfMeasure)) {
					uOMElement.setAttribute("IsCustUOMFlag", "Y");
				}
			}
		}
		return inputDocument.getDocument();
	}

	private void getComplexQueryOutputDocument(HashMap<String, String> wUOMsToConversionFactors, String lowestConvUOM, YFCDocument complexQueryOutDoc, String itemID) {

		YFCElement documentElement = complexQueryOutDoc.getDocumentElement();
		YFCElement itemElement = complexQueryOutDoc.createElement("Item");
		YFCElement UOMListElement = itemElement.createChild("UOMList");
		// when there is no customer specific UOM
		if (baseUOM != null && baseUOM.trim().length() > 0 && (ExtnIsCustUOMExcl == null || ExtnIsCustUOMExcl.trim().length() == 0 || !ExtnIsCustUOMExcl.equals("Y"))) {
			YFCElement uOMElement = UOMListElement.createChild("UOM");
			uOMElement.setAttribute("UnitOfMeasure", baseUOM);
			uOMElement.setAttribute("Conversion", "1");
		}

		if (lowestConvUOM != null && lowestConvUOM.length() > 0) {
			YFCElement uOMElement = UOMListElement.createChild("UOM");
			uOMElement.setAttribute("UnitOfMeasure", lowestConvUOM);
			uOMElement.setAttribute("Conversion", wUOMsToConversionFactors.get(lowestConvUOM));

		}
		Set<String> set = wUOMsToConversionFactors.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			YFCElement uOMElement = UOMListElement.createChild("UOM");
			String UnitOfMeasure = (String) iterator.next();
			String Conversion = wUOMsToConversionFactors.get(UnitOfMeasure);
			if (!UnitOfMeasure.equals(lowestConvUOM)) {
				uOMElement.setAttribute("UnitOfMeasure", UnitOfMeasure);
				// Updated for XB-687 - Start
				if (customerUOMList.contains(UnitOfMeasure)) {
					uOMElement.setAttribute("IsCustUOMFlag", "Y");
				}// Updated for XB-687 - End
				uOMElement.setAttribute("Conversion", Conversion);
			}
		}
		itemElement.setAttribute("ItemID", itemID);
		documentElement.appendChild((YFCNode) itemElement);
	}

	private int getConversion(String convFactor, String orderMultiple) {
		if (convFactor != null && convFactor.length() > 0 && orderMultiple != null && orderMultiple.length() > 0) {
			double convFactorD = Double.parseDouble(convFactor);
			double orderMultipleD = Double.parseDouble(orderMultiple);
			double factor = (convFactorD / orderMultipleD);
			if (Math.abs(factor) == factor) {
				return (int) Math.abs(factor);
			}
		}
		return -1;
	}

	public static ArrayList<String> customerUOMList = new ArrayList<String>();

	public static ArrayList<String> getCustomerUOMList() {
		return customerUOMList;
	}
	public void setCustomerUOMList(ArrayList<String> customerUOMList) {
		XPXCatalogAllAPI.customerUOMList = customerUOMList;
	}

}
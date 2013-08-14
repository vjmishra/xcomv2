package com.xpedx.nextgen.catalog.api;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.xpedx.nextgen.uom.api.XPXUOMListAPI;
import com.yantra.custom.dbi.XPX_Item_Extn;
import com.yantra.custom.dbi.XPX_Itemcust_Xref;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.shared.dbclasses.XPX_Item_ExtnDBHome;
import com.yantra.shared.dbclasses.XPX_Itemcust_XrefDBHome;
import com.yantra.shared.dbclasses.YFS_CustomerDBHome;
import com.yantra.shared.dbclasses.YFS_ItemDBHome;
import com.yantra.shared.dbclasses.YFS_Item_UOMDBHome;
import com.yantra.shared.dbclasses.YFS_Item_UOM_MasterDBCacheHome;
import com.yantra.shared.dbclasses.YFS_Item_UOM_MasterDBHome;
import com.yantra.shared.dbclasses.YPM_Pricelist_AssignmentDBHome;
import com.yantra.shared.dbclasses.YPM_Pricelist_LineDBHome;
import com.yantra.shared.dbi.YFS_Customer;
import com.yantra.shared.dbi.YFS_Item;
import com.yantra.shared.dbi.YFS_Item_UOM;
import com.yantra.shared.dbi.YPM_Pricelist_Assignment;
import com.yantra.shared.dbi.YPM_Pricelist_Line;
import com.yantra.shared.ycp.YFSContext;
import com.yantra.yfc.dblayer.PLTQueryBuilder;
import com.yantra.yfc.dblayer.PLTQueryBuilderHelper;
import com.yantra.yfc.dblayer.YFCQueryBuilder;
import com.yantra.yfc.dblayer.YFCQueryBuilderHelper;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSConnectionHolder;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/*
 * 
 * 
 * This Service is create for performance improvement of Catalog page - By Amar
 * 
 * 
 */
public class XPXCatalogAllAPI implements YIFCustomApi {

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
		// TODO Auto-generated method stub
		
	}

	public Document getAllCatalogAPI(YFSEnvironment env,Document inputXML) throws Exception
	{
		Document outputDoc=SCXmlUtil.createDocument("XPXCatalogAllAPIService");
		getPriceListElement(env,inputXML,outputDoc);
		getCustXrefList(env,inputXML,outputDoc);
		getXPXItemExtnElement(env,inputXML,outputDoc);
		Element uomList=(Element)inputXML.getElementsByTagName("UOMList").item(0);
		
		if(uomList != null)
		{
			getXpedxUOMList(env, SCXmlUtil.createFromString(SCXmlUtil.getString(uomList)), outputDoc);
		}
		return outputDoc;
	}
	
	private Element getCustXrefList(YFSEnvironment env,Document inputXML,Document outputDoc) throws SQLException
	{
		Element custXrefElement=SCXmlUtil.createChild(outputDoc.getDocumentElement(), "XPXItemcustXrefList");
		try
		{
		Element custXrefList=(Element)inputXML.getElementsByTagName("XPXItemcustXref").item(0);
		
		/*StringBuilder query=new StringBuilder("SELECT      XPX_ITEMCUST_XREF.* FROM XPX_ITEMCUST_XREF XPX_ITEMCUST_XREF     WHERE ( ( ( XPX_ITEMCUST_XREF.ENVIRONMENT_CODE =  '"+custXrefList.getAttribute("EnvironmentCode")+"'   )  AND ( XPX_ITEMCUST_XREF.CUSTOMER_NUMBER =  '"+custXrefList.getAttribute("CustomerNumber")+"'   )  AND  ( XPX_ITEMCUST_XREF.CUSTOMER_DIVISION =  '"+custXrefList.getAttribute("CustomerDivision")+"'   ) AND  ( " );
		
		NodeList legacyItemNumberList=	custXrefList.getElementsByTagName("Exp");
		query.append("AND  (  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '"+((Element)legacyItemNumberList.item(0)).getAttribute("Value")+"'   )");
		for(int i=1;i<legacyItemNumberList.getLength();i++)
		{
			Element custRefElem=(Element)legacyItemNumberList.item(i);
			String legacyItemNumber=custRefElem.getAttribute("Value");
			query.append("OR ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '"+legacyItemNumber+"'   )");
		}
		query.append(")");*/
		PLTQueryBuilder pltQryBuilder = PLTQueryBuilderHelper.createPLTQueryBuilder();
		pltQryBuilder.setCurrentTable("XPX_ITEMCUST_XREF");
		pltQryBuilder.appendString("CUSTOMER_NUMBER", "=", custXrefList.getAttribute("CustomerNumber"));
		pltQryBuilder.appendString(" AND ENVIRONMENT_CODE", "=", custXrefList.getAttribute("EnvironmentCode"));
		pltQryBuilder.append("AND CUSTOMER_DIVISION ='"+custXrefList.getAttribute("CustomerDivision")+"'");
		NodeList legacyItemNumberList=	custXrefList.getElementsByTagName("Exp");
		if(legacyItemNumberList == null)
			return custXrefElement;
		/*pltQryBuilder.appendString("AND ( trim(LEGACY_ITEM_NUMBER)", "=", ((Element)legacyItemNumberList.item(0)).getAttribute("Value"));
		for(int i=1;i<legacyItemNumberList.getLength();i++)
		{
			Element custRefElem=(Element)legacyItemNumberList.item(i);
			pltQryBuilder.appendString("OR trim(LEGACY_ITEM_NUMBER)", "=", custRefElem.getAttribute("Value"));
			
		}
		pltQryBuilder.append(")");*/
		pltQryBuilder.append(" AND LEGACY_ITEM_NUMBER IN ('"+((Element)legacyItemNumberList.item(0)).getAttribute("Value")+"'");
		for(int i=1;i<legacyItemNumberList.getLength();i++)
		{
			Element custRefElem=(Element)legacyItemNumberList.item(i);
			pltQryBuilder.append(",'"+custRefElem.getAttribute("Value")+"'");
			
		}
		pltQryBuilder.append(")");
		List<XPX_Itemcust_Xref> xpxItemxrefList=
			XPX_Itemcust_XrefDBHome.getInstance().listWithWhere((YFSContext)env, pltQryBuilder,5000);
		Iterator<XPX_Itemcust_Xref> xpxItemxrefIter=xpxItemxrefList.iterator();
		while(xpxItemxrefIter.hasNext())
		{
			XPX_Itemcust_Xref xpxItemxref=xpxItemxrefIter.next();
			Element itemXrefEleme=SCXmlUtil.createChild(custXrefElement, "XPXItemcustXref");
			itemXrefEleme.setAttribute("ItemcustRefKey",xpxItemxref.getItemcustRefKey());
			itemXrefEleme.setAttribute("CustomerNumber",xpxItemxref.getCustomer_Number() );
			itemXrefEleme.setAttribute("MPC",xpxItemxref.getMpc());
			itemXrefEleme.setAttribute("LegacyItemNumber",xpxItemxref.getLegacy_Item_Number());
			itemXrefEleme.setAttribute("CustomerPartNumber",xpxItemxref.getCustomer_Part_Number());
			itemXrefEleme.setAttribute("CustomerItemNumber",xpxItemxref.getCustomer_Item_Number());
			itemXrefEleme.setAttribute("CustomerDecription",xpxItemxref.getCustomer_Description() );
			itemXrefEleme.setAttribute("CustomerUnit",xpxItemxref.getCustomer_Unit());
			itemXrefEleme.setAttribute("ConvFactor",""+xpxItemxref.getConv_Factor());
			itemXrefEleme.setAttribute("IsCustUOMExcl",xpxItemxref.getIs_Cust_Uom_Excl());
			itemXrefEleme.setAttribute("EnvironmentCode",xpxItemxref.getEnvironment_Code());
			itemXrefEleme.setAttribute("CompanyCode",xpxItemxref.getCompany_Code());
			itemXrefEleme.setAttribute("CustomerDivision",xpxItemxref.getCustomer_Division());
			itemXrefEleme.setAttribute("LegacyUom",xpxItemxref.getLegacy_Uom());
			itemXrefEleme.setAttribute("CustomerUom",xpxItemxref.getCustomer_Uom());
			itemXrefEleme.setAttribute("Createts",""+xpxItemxref.getCreatets());
			itemXrefEleme.setAttribute("Modifyts",""+xpxItemxref.getModifyts());
		}
		/*StringBuilder query=new StringBuilder("SELECT      XPX_ITEMCUST_XREF.* FROM XPX_ITEMCUST_XREF XPX_ITEMCUST_XREF     WHERE ( (  ( XPX_ITEMCUST_XREF.CUSTOMER_NUMBER =  '6806858'   )  AND ( XPX_ITEMCUST_XREF.ENVIRONMENT_CODE =  'M'   )  AND ( XPX_ITEMCUST_XREF.CUSTOMER_DIVISION =  '60'   ) AND  (  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '2035347'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '2110816'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '2115928'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '2189663'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '2202862'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '2265817'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '2312967'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '530153'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '530158'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '530159'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '5302673'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '5333343'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '5342032'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '5414951'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '613682'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '681930'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '689792'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '722377'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '724508'   )  OR  ( XPX_ITEMCUST_XREF.LEGACY_ITEM_NUMBER =  '743064'   )  )  ) ) ");
		Statement stmt =m_Conn.createStatement();
		ResultSet custXREFRs=stmt.executeQuery(query.toString());
		while(custXREFRs.next())
		{
			Element itemXrefEleme=SCXmlUtil.createChild(custXrefElement, "XPXItemcustXref");
			itemXrefEleme.setAttribute("ItemcustRefKey", custXREFRs.getString("ITEMCUST_REF_KEY"));
			itemXrefEleme.setAttribute("CustomerNumber", custXREFRs.getString("CUSTOMER_NUMBER"));
			itemXrefEleme.setAttribute("MPC", custXREFRs.getString("MPC"));
			itemXrefEleme.setAttribute("LegacyItemNumber", custXREFRs.getString("LEGACY_ITEM_NUMBER"));
			itemXrefEleme.setAttribute("CustomerPartNumber", custXREFRs.getString("CUSTOMER_PART_NUMBER"));
			itemXrefEleme.setAttribute("CustomerItemNumber", custXREFRs.getString("CUSTOMER_ITEM_NUMBER"));
			itemXrefEleme.setAttribute("CustomerDecription", custXREFRs.getString("CUSTOMER_DESCRIPTION"));
			itemXrefEleme.setAttribute("CustomerUnit", custXREFRs.getString("CUSTOMER_UNIT"));
			itemXrefEleme.setAttribute("ConvFactor", custXREFRs.getString("CONV_FACTOR"));
			itemXrefEleme.setAttribute("IsCustUOMExcl", custXREFRs.getString("IS_CUST_UOM_EXCL"));
			itemXrefEleme.setAttribute("EnvironmentCode", custXREFRs.getString("ENVIRONMENT_CODE"));
			itemXrefEleme.setAttribute("CompanyCode", custXREFRs.getString("COMPANY_CODE"));
			itemXrefEleme.setAttribute("CustomerDivision", custXREFRs.getString("CUSTOMER_DIVISION"));
			itemXrefEleme.setAttribute("LegacyUom", custXREFRs.getString("LEGACY_UOM"));
			itemXrefEleme.setAttribute("CustomerUom", custXREFRs.getString("CUSTOMER_UOM"));
			itemXrefEleme.setAttribute("Createts", custXREFRs.getString("CREATETS"));
			itemXrefEleme.setAttribute("Modifyts", custXREFRs.getString("MODIFYTS"));
			
		}*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return custXrefElement;
	}
	private Element getXPXItemExtnElement(YFSEnvironment env,Document inputXML,Document outputDoc) throws SQLException
	{
			Element xpxItemExtnList=SCXmlUtil.createChild(outputDoc.getDocumentElement(), "XPXItemExtnList");
			try
			{
				Element itemExtnElelement=(Element)inputXML.getElementsByTagName("XPXItemExtn").item(0);
				if(itemExtnElelement == null)
					return null;
				/*StringBuilder query=new StringBuilder("SELECT      XPX_ITEM_EXTN.* FROM XPX_ITEM_EXTN XPX_ITEM_EXTN     WHERE ( (  ( XPX_ITEM_EXTN.ENVIRONMENT_ID =  'M'   )  AND ( XPX_ITEM_EXTN.XPX_DIVISION =  '68'   ) ");
				 
				NodeList itemIDList=	itemExtnElelement.getElementsByTagName("Exp");
				query.append("AND  (  ( XPX_ITEM_EXTN.ITEM_ID =  '"+5178022+"'   )");
				for(int i=1;i<itemIDList.getLength();i++)
				{
					Element itemExtnElem=(Element)itemIDList.item(i);
					String itemID=itemExtnElem.getAttribute("Value");
					query.append("OR ( XPX_ITEM_EXTN.ITEM_ID =  =  '"+itemID+"'   )");
				}
				query.append(")");*/
				//722377,5414951
				/*StringBuilder query=new StringBuilder("SELECT      XPX_ITEM_EXTN.* FROM XPX_ITEM_EXTN XPX_ITEM_EXTN     WHERE ( (  ( XPX_ITEM_EXTN.ENVIRONMENT_ID =  'M'   )  AND ( XPX_ITEM_EXTN.XPX_DIVISION =  '68'   ) AND  (  ( XPX_ITEM_EXTN.ITEM_ID =  '2035347'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '2110816'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '2115928'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '2189663'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '2202862'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '2265817'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '2312967'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '530153'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '530158'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '530159'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '5302673'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '5333343'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '5342032'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '5414951'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '613682'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '681930'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '689792'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '722377'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '724508'   )  OR  ( XPX_ITEM_EXTN.ITEM_ID =  '743064'   )  )  ) )");
				Statement stmt =m_Conn.createStatement();
				ResultSet xpxItemExtnRs=stmt.executeQuery(query.toString());*/
				
				PLTQueryBuilder pltQryBuilder = PLTQueryBuilderHelper.createPLTQueryBuilder();
				pltQryBuilder.setCurrentTable("XPX_ITEM_EXTN");
				pltQryBuilder.appendString("ENVIRONMENT_ID", "=", itemExtnElelement.getAttribute("EnvironmentID"));
				pltQryBuilder.append(" AND XPX_DIVISION ='"+ itemExtnElelement.getAttribute("XPXDivision") +"'");
				NodeList itemIDList=	itemExtnElelement.getElementsByTagName("Exp");
				/*pltQryBuilder.appendString("AND ( (ITEM_ID)", "=", ((Element)itemIDList.item(0)).getAttribute("Value"));
				for(int i=1;i<itemIDList.getLength();i++)
				{
					Element custRefElem=(Element)itemIDList.item(i);
					pltQryBuilder.appendString("OR (ITEM_ID)", "=", custRefElem.getAttribute("Value"));
					
				}
				pltQryBuilder.append(")");*/
				pltQryBuilder.append(" AND ITEM_ID IN ('"+((Element)itemIDList.item(0)).getAttribute("Value")+"'");
				for(int i=1;i<itemIDList.getLength();i++)
				{
					Element custRefElem=(Element)itemIDList.item(i);
					pltQryBuilder.append(",'"+custRefElem.getAttribute("Value")+"'");
					
				}
				pltQryBuilder.append(")");
				List<XPX_Item_Extn> xpxItemExtns=
					XPX_Item_ExtnDBHome.getInstance().listWithWhere((YFSContext)env, pltQryBuilder,5000);
				Iterator<XPX_Item_Extn> xpxItemExtnIter=xpxItemExtns.iterator();
				while(xpxItemExtnIter.hasNext())
				{
					XPX_Item_Extn xpxItemExtn=xpxItemExtnIter.next();
					Element itemXrefEleme=SCXmlUtil.createChild(xpxItemExtnList, "XPXItemExtn");
					itemXrefEleme.setAttribute(XPX_Item_Extn.ITEM_KEY,xpxItemExtn.getItem_Key());
					itemXrefEleme.setAttribute(XPX_Item_Extn.ITEMEXTNKEY,xpxItemExtn.getItemExtnKey());
					itemXrefEleme.setAttribute(XPX_Item_Extn.ENVIRONMENTID,xpxItemExtn.getEnvironmentID() );
					itemXrefEleme.setAttribute(XPX_Item_Extn.COMPANYCODE,xpxItemExtn.getCompanyCode() );
					itemXrefEleme.setAttribute(XPX_Item_Extn.ITEMID,xpxItemExtn.getItemID());
					itemXrefEleme.setAttribute(XPX_Item_Extn.MASTERPRODUCTCODE,xpxItemExtn.getMasterProductCode());
					itemXrefEleme.setAttribute(XPX_Item_Extn.XPXDIVISION,xpxItemExtn.getXPXDivision());
					itemXrefEleme.setAttribute(XPX_Item_Extn.ITEMSTOCKSTATUS,xpxItemExtn.getItemStockStatus());
					itemXrefEleme.setAttribute(XPX_Item_Extn.ORDERMULTIPLE,""+xpxItemExtn.getOrderMultiple());
					itemXrefEleme.setAttribute(XPX_Item_Extn.INVENTORYINDICATOR,xpxItemExtn.getInventoryIndicator());
					itemXrefEleme.setAttribute(XPX_Item_Extn.ORIGINALINDICATOR,xpxItemExtn.getOriginalIndicator());
					itemXrefEleme.setAttribute(XPX_Item_Extn.CREATETS,""+xpxItemExtn.getCreatets());
					itemXrefEleme.setAttribute(XPX_Item_Extn.MODIFYTS,""+xpxItemExtn.getModifyts());
				}
				/*while(xpxItemExtnRs.next())
				{
					Element itemXrefEleme=SCXmlUtil.createChild(xpxItemExtnList, "XPXItemExtn");
					itemXrefEleme.setAttribute("ItemcustRefKey", xpxItemExtnRs.getString("ITEM_KEY"));
					itemXrefEleme.setAttribute("CustomerNumber", xpxItemExtnRs.getString("ITEM_EXTN_KEY"));
					itemXrefEleme.setAttribute("MPC", xpxItemExtnRs.getString("ENVIRONMENT_ID"));
					itemXrefEleme.setAttribute("LegacyItemNumber", xpxItemExtnRs.getString("COMPANY_CODE"));
					itemXrefEleme.setAttribute("CustomerPartNumber", xpxItemExtnRs.getString("ITEM_ID"));
					itemXrefEleme.setAttribute("CustomerItemNumber", xpxItemExtnRs.getString("MASTER_PRODUCT_CODE"));
					itemXrefEleme.setAttribute("CustomerDecription", xpxItemExtnRs.getString("XPX_DIVISION"));
					itemXrefEleme.setAttribute("CustomerUnit", xpxItemExtnRs.getString("ITEM_STOCK_STATUS"));
					itemXrefEleme.setAttribute("ConvFactor", xpxItemExtnRs.getString("ORDER_MULTIPLE"));
					itemXrefEleme.setAttribute("IsCustUOMExcl", xpxItemExtnRs.getString("INVENTORY_INDICATOR"));
					itemXrefEleme.setAttribute("EnvironmentCode", xpxItemExtnRs.getString("ORIGINAL_INDICATOR"));
					itemXrefEleme.setAttribute("Createts", xpxItemExtnRs.getString("CREATETS"));
					itemXrefEleme.setAttribute("Modifyts", xpxItemExtnRs.getString("MODIFYTS"));
					
				}*/
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return xpxItemExtnList;
			
	}
	
	private Element getPriceListElement(YFSEnvironment env,Document inputXML,Document outputDoc) throws SQLException
	{
		Element priceElement=SCXmlUtil.createChild(outputDoc.getDocumentElement(), "PricelistLineList");
		// getting price list details 
		try
		{
			Element pricelistAssignmentElement=(Element)inputXML.getElementsByTagName("PricelistAssignment").item(0);
			
			
			
			if(pricelistAssignmentElement != null)
			{
				String customerID=pricelistAssignmentElement.getAttribute("CustomerID");
				String priceWareHouse=pricelistAssignmentElement.getAttribute("ExtnPriceWareHouse");
				PLTQueryBuilder pltQryBuilder = PLTQueryBuilderHelper.createPLTQueryBuilder();
				pltQryBuilder.setCurrentTable("YPM_PRICELIST_ASSIGNMENT");
				pltQryBuilder.append(" PRICELIST_HDR_KEY IN(SELECT DISTINCT PRICELIST_HDR_KEY FROM YPM_PRICELIST_HDR WHERE EXTN_PRICING_WAREHOUSE ='" +priceWareHouse+"' AND PRICING_STATUS = 'ACTIVE' )");
				pltQryBuilder.append(" AND CUSTOMER_ID ='"+customerID+"'");
				//pltQryBuilder.appendString(" AND PRICING_STATUS", "=","ACTIVE");
				
				List<YPM_Pricelist_Assignment> priceListAssignments=
					YPM_Pricelist_AssignmentDBHome.getInstance().listWithWhere((YFSContext)env, pltQryBuilder,5000);
				Iterator<YPM_Pricelist_Assignment> priceListIter=priceListAssignments.iterator();
				if(priceListIter.hasNext())
				{
					PLTQueryBuilder pltQryBuilder1 = PLTQueryBuilderHelper.createPLTQueryBuilder();
					pltQryBuilder1.setCurrentTable("YPM_PRICELIST_LINE");
					Element itemElem=(Element)pricelistAssignmentElement.getElementsByTagName("Item").item(0);
					pltQryBuilder1.append("PRICING_STATUS ='ACTIVE' ");
					if(priceListIter.hasNext())
					{
						
						YPM_Pricelist_Assignment pricelistAssignment=priceListIter.next();
						pltQryBuilder1.append("AND pricelist_hdr_key IN  ('"+ pricelistAssignment.getPricelist_Header_Key()+"'");
					}
					else
						return priceElement;
					while(priceListIter.hasNext())
					{
						YPM_Pricelist_Assignment pricelistAssignment=priceListIter.next();
						pltQryBuilder1.append(" ,'"+ pricelistAssignment.getPricelist_Header_Key()+"'");
					}
					pltQryBuilder1.append(")");
					NodeList itemIdComplexQuery=pricelistAssignmentElement.getElementsByTagName("Exp");
					if(itemIdComplexQuery == null)
						return priceElement;
					pltQryBuilder1.append(" AND ITEM_ID IN ('"+((Element)itemIdComplexQuery.item(0)).getAttribute("Value")+"'");
					for(int i=1;i<itemIdComplexQuery.getLength();i++)
					{
						Element itemIDElement=(Element)itemIdComplexQuery.item(i);
						pltQryBuilder1.append(" ,'"+itemIDElement.getAttribute("Value")+"'");
					}
					pltQryBuilder1.append(")");
					List<YPM_Pricelist_Line> priceListLineList=
						YPM_Pricelist_LineDBHome.getInstance().listWithWhere((YFSContext)env, pltQryBuilder1,5000);
					Iterator<YPM_Pricelist_Line> priceListLineIter=priceListLineList.iterator();
					while(priceListLineIter.hasNext())
					{
						YPM_Pricelist_Line priceListLine=priceListLineIter.next();
						Element priceLineElement=SCXmlUtil.createChild(priceElement, "PricelistLine");
						priceLineElement.setAttribute("ItemID",priceListLine.getItem_ID());
						priceLineElement.setAttribute("FromQuantity",""+priceListLine.getFrom_Quantity() );
						priceLineElement.setAttribute("ListPrice",""+priceListLine.getList_Price() );
						Element priceLineExtnElement=SCXmlUtil.createChild(priceLineElement, "Extn");
						priceLineExtnElement.setAttribute("ExtnTierUom",priceListLine.getExtn_Extn_Tier_Uom() );
						priceLineExtnElement.setAttribute("ExtnPricingUom",priceListLine.getExtn_Extn_Pricing_Uom());
						
					}
				}
				/*Element pricelistLine=(Element)inputXML.getElementsByTagName("PricelistLine").item(0);
				ArrayList<Element> itemList=SCXmlUtil.getElements(pricelistLine, "/Item/ComplexQuery/Exp");
				StringBuilder sb=new StringBuilder();
				boolean isPriceCall=false;
				for(Element itemElem : itemList)
				{
					String expName=itemElem.getAttribute("Name");
					String value=itemElem.getAttribute("Value");
					
					if("ItemID".equals(expName) && value != null && value.trim().length() > 0 )
					{				
						sb.append("'").append(value).append("',");
						isPriceCall=true;
					}
				}
				int ind=sb.lastIndexOf(",");
				sb.replace(ind, ind, "");
				if(isPriceCall)
				{
					Statement stmt =m_Conn.createStatement();
					String sql="select YPL.* from YPM_PRICELIST_ASSIGNMENT YPA ,  YPM_PRICELIST_LINE YPL where ypl.pricelist_hdr_key =ypa.pricelist_hdr_key and ypa.customer_id ='"+customerID+"' and PRICING_STATUS='ACTIVE' and in("+sb.toString()+")";
					ResultSet priceListRs=stmt.executeQuery(sql);
					while(priceListRs.next())
					{
						Element priceLineElement=SCXmlUtil.createChild(priceElement, "PricelistLine");
						priceLineElement.setAttribute("ItemID", priceListRs.getString("ITEM_ID"));
						priceLineElement.setAttribute("FromQuantity", priceListRs.getString("FROM_QUANTITY"));
						priceLineElement.setAttribute("ListPrice", priceListRs.getString("LIST_PRICE"));
						Element priceLineExtnElement=SCXmlUtil.createChild(priceLineElement, "Extn");
						priceLineExtnElement.setAttribute("ExtnTierUom", priceListRs.getString("EXTN_TIER_UOM"));
						priceLineExtnElement.setAttribute("ExtnPricingUom", priceListRs.getString("EXTN_PRICING_UOM"));
						
					}
				}*/
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return priceElement;
		
	}
	
	private Document createItemListDocument(List<YFS_Item> yfsItemList,List<YFS_Item_UOM> yfsItemUOMList)
	{		
		Document itemListDoc=SCXmlUtil.createDocument("ItemList");
		
		Iterator<YFS_Item_UOM> yfsItemUOMIter=yfsItemUOMList.iterator();
		Map<String,ArrayList<YFS_Item_UOM>> itemUOMMap=new HashMap<String,ArrayList<YFS_Item_UOM>>();
		while(yfsItemUOMIter.hasNext())
		{
			
			YFS_Item_UOM yfsItemUom=yfsItemUOMIter.next();
			ArrayList<YFS_Item_UOM> uomList=itemUOMMap.get(yfsItemUom.getItem_Key());
			if(uomList == null)
			{
				 uomList=new ArrayList<YFS_Item_UOM>();
			}
			uomList.add(yfsItemUom);
			itemUOMMap.put(yfsItemUom.getItem_Key(), uomList);
			
		}
		Iterator<YFS_Item> yfsItemIter=yfsItemList.iterator();
		while(yfsItemIter.hasNext())
		{
			YFS_Item yfsItem=yfsItemIter.next();
			
			Element itemElem=SCXmlUtil.createChild(itemListDoc.getDocumentElement(), "Item");	
			itemElem.setAttribute(YFS_Item.CAN_USE_AS_SERVICE_TOOL, yfsItem.getCan_Use_As_Service_Tool());
			itemElem.setAttribute(YFS_Item.CREATEPROGID, yfsItem.getCreateprogid());
			itemElem.setAttribute(YFS_Item.CREATETS, ""+yfsItem.getCreatets());
			itemElem.setAttribute(YFS_Item.CREATEUSERID, yfsItem.getCreateuserid());
			itemElem.setAttribute("DisplayItemId", yfsItem.getItem_Id());
			itemElem.setAttribute(YFS_Item.GLOBAL_ITEM_ID, yfsItem.getGlobal_Item_Id());
			itemElem.setAttribute(YFS_Item.INHERIT_ATTRS_FROM_CLASSIFICATION, yfsItem.getInherit_Attrs_From_Classification());
			itemElem.setAttribute(YFS_Item.IS_SHIPPING_CNTR, yfsItem.getIs_Shipping_Cntr());
			itemElem.setAttribute(YFS_Item.ITEM_GROUP_CODE, yfsItem.getItem_Group_Code());
			itemElem.setAttribute(YFS_Item.ITEM_ID, yfsItem.getItem_Id());
			itemElem.setAttribute(YFS_Item.ITEM_KEY, yfsItem.getItem_Key());
			itemElem.setAttribute(YFS_Item.LOCKID, ""+yfsItem.getLockid());
			itemElem.setAttribute(YFS_Item.MAX_MODIFYTS, ""+yfsItem.getMax_ModifyTS());
			itemElem.setAttribute(YFS_Item.MODIFYPROGID, yfsItem.getModifyprogid());
			itemElem.setAttribute(YFS_Item.MODIFYTS, ""+yfsItem.getModifyts());
			itemElem.setAttribute(YFS_Item.MODIFYUSERID, yfsItem.getModifyuserid());
			itemElem.setAttribute(YFS_Item.ORGANIZATION_CODE, yfsItem.getOrganization_Code());
			itemElem.setAttribute("UnitOfMeasure", yfsItem.getUom());
			Element alternameUOMListElem=SCXmlUtil.createChild(itemElem, "AlternateUOMList");
			ArrayList<YFS_Item_UOM> uomList=itemUOMMap.get(yfsItem.getItem_Key());
			if(uomList != null)
			{
				for(YFS_Item_UOM yfsItemUom: uomList)
				{
					Element alternameUOMElem=SCXmlUtil.createChild(alternameUOMListElem, "AlternateUOM");
					alternameUOMElem.setAttribute("Height", ""+yfsItemUom.getHeight());
					alternameUOMElem.setAttribute("HeightUOM", yfsItemUom.getHeight_Uom());
					alternameUOMElem.setAttribute("IsOrderingUOM", yfsItemUom.getIs_Ordering_Uom());
					alternameUOMElem.setAttribute("ItemKey", yfsItemUom.getItem_Key());
					alternameUOMElem.setAttribute("Length",""+ yfsItemUom.getLength());
					alternameUOMElem.setAttribute("LengthUOM", yfsItemUom.getLength_Uom());
					alternameUOMElem.setAttribute("Quantity", ""+yfsItemUom.getQuantity());
					alternameUOMElem.setAttribute("UnitOfMeasure", yfsItemUom.getUom());
					alternameUOMElem.setAttribute("Weight", ""+yfsItemUom.getWeight());
					alternameUOMElem.setAttribute("WeightUOM", yfsItemUom.getWeight_Uom());
					alternameUOMElem.setAttribute("Width", ""+yfsItemUom.getWidth());
					alternameUOMElem.setAttribute("WidthUOM", yfsItemUom.getWidth_Uom());
				}
			}
			
		}
		return itemListDoc;
		
	}
	
	
	private Document getXpedxUOMList(YFSEnvironment env, Document inXML,Document outputXML)
	throws XPathExpressionException, YFSException, YIFClientCreationException, RemoteException {
		log.beginTimer("XPXUOMListAPI:getUOMList started...");
		LinkedHashMap<String, String> wUOMsToConversionFactors = new LinkedHashMap<String, String>();
		String LegacyCustomerNumber = "";
		String companyCode = "";
		String useOrderMulUOMFlag = "";
		String orderMultiple = "";
		String customerDivision = "";
		String itemID = "";
		
		YFCDocument complexQueryOutDoc = YFCDocument.createDocument("ItemList");
		try
		{
			YFCDocument inDoc = YFCDocument.getDocumentFor(inXML);
			YFCElement complexQueryElement = inDoc.getDocumentElement().getChildElement("ComplexQuery");
			if(complexQueryElement != null) {
				complexQuery = true;
			}
			
			Element documentElement = inXML.getDocumentElement();
			String customerID = documentElement.getAttribute("CustomerID");
			itemID = documentElement.getAttribute("ItemID");
			String storeFrontId = documentElement.getAttribute("OrganizationCode");
			//Added to identify this request is from B2B order flow or not
			String entryType=documentElement.getAttribute("EntryType");
			
			String[] customerIDTokens = customerID.split("\\-");
			if (customerIDTokens != null && customerIDTokens.length > 1) {
				LegacyCustomerNumber = customerIDTokens[1];
			}
			
			HashMap<String, String> customerDetails = getCustomerDetails(env, inXML);
			if("false".equals(customerDetails.get("isGetUOMCall")))
			{
				return complexQueryOutDoc.getDocument();
			}
			companyCode = customerDetails.get("companyCode");
			customerDivision = customerDetails.get("customerDivision");
			useOrderMulUOMFlag = customerDetails.get("useOrderMulUOMFlag");
			PLTQueryBuilder pltQryBuilder = PLTQueryBuilderHelper.createPLTQueryBuilder();
			pltQryBuilder.setCurrentTable("YFS_ITEM");
			pltQryBuilder.append("organization_code ='"+storeFrontId+"'");
			if(complexQuery) {
				YFCNodeList<YFCElement> itemIDList=	complexQueryElement.getElementsByTagName("Exp");
				pltQryBuilder.append("AND ITEM_ID IN('"+ ((YFCElement)itemIDList.item(0)).getAttribute("Value")+"'");
				for(int i=1;i<itemIDList.getLength();i++)
				{
					YFCElement itemIdElem=(YFCElement)itemIDList.item(i);
					pltQryBuilder.append(" ,'"+ itemIdElem.getAttribute("Value")+"'");
					
				}
				pltQryBuilder.append(")");
				
			} 
			else {
				pltQryBuilder.appendString(" ( trim(ITEM_ID)", "=", itemID);
			}
			boolean isYFSItemCall=false;
			List<YFS_Item> yfsItemList=
				YFS_ItemDBHome.getInstance().listWithWhere((YFSContext)env, pltQryBuilder,5000);
			
			PLTQueryBuilder pltQryBuilder1 = PLTQueryBuilderHelper.createPLTQueryBuilder();
			 pltQryBuilder1.setCurrentTable("YFS_ITEM_UOM");
			 Iterator<YFS_Item> yfsItemIter=yfsItemList.iterator();
			 if(yfsItemIter.hasNext())
			 {
				 YFS_Item yfsItem=yfsItemIter.next();
				 pltQryBuilder1.append(" ITEM_KEY IN('"+yfsItem.getItem_Key()+"'");
				 isYFSItemCall=true;
			 }
			 while(yfsItemIter.hasNext())
			 {
				 YFS_Item yfsItem=yfsItemIter.next();
				 pltQryBuilder1.append(" ,'"+yfsItem.getItem_Key()+"'");
			 }
			 pltQryBuilder1.append(")");
			 Document outputListDocument =null;
			 if(isYFSItemCall)
			 {
				 List<YFS_Item_UOM> yfsItemUOMList=
					 YFS_Item_UOMDBHome.getInstance().listWithWhere((YFSContext)env, pltQryBuilder1,5000);
				 outputListDocument=createItemListDocument(yfsItemList,yfsItemUOMList);
			 }
			 else
				 return complexQueryOutDoc.getDocument();
			//inputElement.setAttribute("OrganizationCode", storeFrontId);
			//inputElement.setAttribute("CallingOrganizationCode", storeFrontId);
			//Removing the Extn Template as we get all the XPXItemExtn for that item. We are making separate call
			/*env.setApiTemplate("getItemList", SCXmlUtil.createFromString(""
					+ "<ItemList><Item><AlternateUOMList><AlternateUOM />"
					+ "</AlternateUOMList></Item></ItemList>"));
			
			api = YIFClientFactory.getInstance().getApi();
			Document outputListDocument = api.invoke(env, "getItemList",
					inputDocument.getDocument());*/
			Element outputListElement = outputListDocument.getDocumentElement();
			NodeList itemListNodes = outputListElement.getChildNodes();
			int length = itemListNodes.getLength();
			ArrayList<String> itemIds = new ArrayList<String>();
			for(int i=0;i<length;i++) {
				Node itemNode = itemListNodes.item(i);
				String tmpItemId = itemNode.getAttributes().getNamedItem("ItemID").getTextContent();
				itemIds.add(tmpItemId);
			}
			setItemXrefDoc(outputXML);
			setItemExtnDoc(outputXML);
			if(!complexQuery) {
				for (int i = 0; i < length; i++) {
					Node itemNode = itemListNodes.item(i);
					baseUOM = itemNode.getAttributes().getNamedItem("UnitOfMeasure").getTextContent();
					NodeList itemNodeChildren = itemNode.getChildNodes();
					int length1 = itemNodeChildren.getLength();
					for (int j = 0; j < length1; j++) {
						Node itemNodeChild = itemNodeChildren.item(j);
						if (itemNodeChild != null
								&& itemNodeChild.getNodeName().equals(
										"AlternateUOMList")) {
							handleAternateUOMs(itemNodeChild, wUOMsToConversionFactors);
						}
					}
					orderMultiple = getOrderMultipleValue(itemID, customerDetails);
				}
				handleXpxItemcustXrefList(itemID,LegacyCustomerNumber, customerDivision,
						useOrderMulUOMFlag, orderMultiple,
						wUOMsToConversionFactors,env,entryType);
				if (ExtnIsCustUOMExcl != null
						&& ExtnIsCustUOMExcl.equals("Y")) {
					outputXML.appendChild((outputXML.getOwnerDocument().importNode(getOutputDocument(wUOMsToConversionFactors, ""), true)));
					return getOutputDocument(wUOMsToConversionFactors, "");
				}		
				//env.clearApiTemplate("getItemList");
				Document document = getOutputDocument(wUOMsToConversionFactors, lowestConvUOM);
				
				log.endTimer("XPXUOMListAPI:getUOMList ended...");
				outputXML.appendChild((outputXML.getOwnerDocument().importNode(document, true)));
				return document;
			}
			else {
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
						if (itemNodeChild != null
								&& itemNodeChild.getNodeName().equals(
										"AlternateUOMList")) {
							handleAternateUOMs(itemNodeChild, wUOMsToConversionFactors);
						}
					}
					orderMultiple = getOrderMultipleValue(itemID, customerDetails);
					handleXpxItemcustXrefList(itemID,LegacyCustomerNumber, customerDivision,
							useOrderMulUOMFlag, orderMultiple,
							wUOMsToConversionFactors,env,entryType);
					if (ExtnIsCustUOMExcl != null
							&& ExtnIsCustUOMExcl.equals("Y")) {
						getComplexQueryOutputDocument(wUOMsToConversionFactors, "" ,complexQueryOutDoc, itemID);
					}
					getComplexQueryOutputDocument(wUOMsToConversionFactors, lowestConvUOM ,complexQueryOutDoc,itemID);
				}
				log.endTimer("XPXUOMListAPI:getUOMList ended...");
				outputXML.getDocumentElement().appendChild((outputXML.importNode(complexQueryOutDoc.getDocument().getDocumentElement(), true)));
				return complexQueryOutDoc.getDocument();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return complexQueryOutDoc.getDocument();
	}
	
	private HashMap<String, String> getCustomerDetails(YFSEnvironment env,
			Document inXML) throws YIFClientCreationException, YFSException,
			RemoteException {
		HashMap<String, String> customerDetails = new HashMap<String, String>();
		Element documentElement = inXML.getDocumentElement();
		Element customerDetailsElem=(Element)documentElement.getElementsByTagName("CustomerDetails").item(0);
		if(customerDetailsElem == null)
		{
			customerDetails.put("isGetUOMCall", "false");
			return customerDetails;
		}
		customerDetails.put("companyCode", customerDetailsElem.getAttribute("ExtnCompanyCode"));
		customerDetails.put("enviromentCode", customerDetailsElem.getAttribute("ExtnEnvironmentCode"));
		customerDetails.put("shipFromBranch", customerDetailsElem.getAttribute("ExtnShipFromBranch"));
		customerDetails.put("customerDivision", customerDetailsElem.getAttribute("ExtnCustomerDivision"));				
		customerDetails.put("useOrderMulUOMFlag", customerDetailsElem.getAttribute("ExtnUseOrderMulUOMFlag"));
		return customerDetails;
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
	
	private void handleAternateUOMs(Node itemNodeChild,
			HashMap<String, String> wUOMsToConversionFactors) {
		NodeList AternateUOMList = itemNodeChild.getChildNodes();
		int length2 = AternateUOMList.getLength();
		for (int k = 0; k < length2; k++) {
			Node AlternateUOM = AternateUOMList.item(k);
			NamedNodeMap namedNodeMap = AlternateUOM.getAttributes();
			Node IsOrderingUOMNode = namedNodeMap.getNamedItem("IsOrderingUOM");
			String IsOrderingUOM = IsOrderingUOMNode.getTextContent();
			if (IsOrderingUOM != null && IsOrderingUOM.equals("Y")) {
				Node unitOfMeasureNode = namedNodeMap
						.getNamedItem("UnitOfMeasure");
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
		//String customerDiv = custDetails.get("customerDivision");
		String shipFromBranch = custDetails.get("shipFromBranch");
		//String envCode = custDetails.get("enviromentCode");
		ArrayList<Element> XpxItemExtnList = SCXmlUtil.getChildren(itemExtnDoc.getDocumentElement(), "XPXItemExtn[@ItemID="+itemId+"]");
		int length3 = XpxItemExtnList.size();
		for (int m = 0; m < length3; m++) {
			Element XpxItemExtn = XpxItemExtnList.get(m);
			NamedNodeMap XpxItemExtnAttributes = XpxItemExtn.getAttributes();
			Node companyCodeNode = XpxItemExtnAttributes
					.getNamedItem("CompanyCode");
			String companyCodeL = companyCodeNode.getTextContent();
			Node XPXDivisionNode = XpxItemExtnAttributes
					.getNamedItem("XPXDivision");
			String XPXDivision = XPXDivisionNode.getTextContent();
			if (companyCodeL.equals(companyCode)
					&& XPXDivision.equals(shipFromBranch)) {
				Node orderMultipleNode = XpxItemExtnAttributes
						.getNamedItem("OrderMultiple");
				orderMultiple = orderMultipleNode.getTextContent();
			}
		}
		return orderMultiple;
	}
	
	private void handleXpxItemcustXrefList(String itemID,
			String customerNumber, String customerBranch,
			String useOrderMulUOMFlag, String orderMultiple,
			HashMap<String, String> wUOMsToConversionFactors, YFSEnvironment env, String entryType)
			throws XPathExpressionException,YFSException, RemoteException, YIFClientCreationException {
		
		Node customerUnitNode = null;
		customerUOMList.clear();
		/*NodeList XpxItemcustXrefList = getItemCustomerXDetails(itemID,
				customerNumber, customerBranch, env);*/
		/*Begin - Changes made by Mitesh for JIRA# 3641*/
		//ArrayList<Element> XpxItemcustXrefList = SCXmlUtil.getElements(itemsXrefDoc.getDocumentElement(), "XPXItemcustXref[@LegacyItemNumber="+itemID+"]");
		List<Element> XpxItemcustXrefList = XPXUtils.getElements(itemsXrefDoc.getDocumentElement(), "XPXItemcustXref[@LegacyItemNumber="+itemID+"]");
		/*End - Changes made by Mitesh for JIRA# 3641*/
		int length3 = XpxItemcustXrefList.size();
		for (int m = 0; m < length3; m++) {
			Node XpxItemcustXref = XpxItemcustXrefList.get(m);
			NamedNodeMap XpxItemcustXrefAttributes = XpxItemcustXref
					.getAttributes();
			Node ExtnIsCustUOMExclNode = XpxItemcustXrefAttributes
					.getNamedItem("IsCustUOMExcl");
			if (ExtnIsCustUOMExclNode != null) {
				ExtnIsCustUOMExcl = ExtnIsCustUOMExclNode.getTextContent();
			}
			
			
			if(entryType == null || entryType.trim().length()<=0)
			{
			   customerUnitNode = XpxItemcustXrefAttributes
					.getNamedItem("CustomerUom");
			}
			else
			{
				
				customerUnitNode = XpxItemcustXrefAttributes
				.getNamedItem("LegacyUom");
			}
			/********Temporarily retained till new entries get added in CustomerXref table as per modified design***/
			if(customerUnitNode==null)
			{
				customerUnitNode = XpxItemcustXrefAttributes
				.getNamedItem("CustomerUnit");
			}
			/*******************************************************************************************************/
			
			String customerUnit = customerUnitNode.getTextContent();
			Node ConvFactorNode = XpxItemcustXrefAttributes
					.getNamedItem("ConvFactor");
			String ConvFactor = ConvFactorNode.getTextContent();
			//XB-687 - Start
			if (ExtnIsCustUOMExcl != null && ExtnIsCustUOMExcl.equals("Y")) {
				wUOMsToConversionFactors.clear();
			}
			if(customerUnit!=null && !customerUnit.equalsIgnoreCase("")){
				customerUOMList.add(customerUnit);				
				wUOMsToConversionFactors.put(customerUnit, ConvFactor);
				return;
			}
			// Null check added.
			if (useOrderMulUOMFlag != null && useOrderMulUOMFlag.equals("Y")) {
				int conversion = getConversion(ConvFactor, orderMultiple);
				if (conversion != -1 && customerUnit != null
						&& customerUnit.length() > 0) {
					if (currentConversion == 0
							|| (currentConversion != 0 && conversion < currentConversion)) {
						lowestConvUOM = customerUnit;
						currentConversion = conversion;
					}
				}
			}
			wUOMsToConversionFactors.put(customerUnit, ConvFactor);
		}
	}

	private Document getOutputDocument(HashMap<String, String> wUOMsToConversionFactors,
			String lowestConvUOM) {
		
		YFCDocument inputDocument = YFCDocument.createDocument("UOMList");
		YFCElement documentElement = inputDocument.getDocumentElement();
		//when there is no customer specific UOM
		if (baseUOM != null
				&& baseUOM.trim().length() > 0
				&& (ExtnIsCustUOMExcl == null
						|| ExtnIsCustUOMExcl.trim().length() == 0 || !ExtnIsCustUOMExcl
						.equals("Y")))
		{
			YFCElement uOMElement = documentElement.createChild("UOM");
			uOMElement.setAttribute("UnitOfMeasure", baseUOM);
			uOMElement.setAttribute("Conversion", "1");
		}
		/*********************************************************************************************/
		
		if (lowestConvUOM != null && lowestConvUOM.length() > 0) {
			YFCElement uOMElement = documentElement.createChild("UOM");
			uOMElement.setAttribute("UnitOfMeasure", lowestConvUOM);
			uOMElement.setAttribute("Conversion", wUOMsToConversionFactors
					.get(lowestConvUOM));
			
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
				//Start of XB-687
				if(customerUOMList.contains(UnitOfMeasure))
				{
					uOMElement.setAttribute("IsCustUOMFlag", "Y");
				}//End of XB-687
			}
		}
		return inputDocument.getDocument();
	}

	private void getComplexQueryOutputDocument(HashMap<String, String> wUOMsToConversionFactors,
			String lowestConvUOM, YFCDocument complexQueryOutDoc, String itemID) {
		
		YFCElement documentElement = complexQueryOutDoc.getDocumentElement();
		YFCElement itemElement = complexQueryOutDoc.createElement("Item");
		YFCElement UOMListElement = itemElement.createChild("UOMList");
		//when there is no customer specific UOM
		if (baseUOM != null
				&& baseUOM.trim().length() > 0
				&& (ExtnIsCustUOMExcl == null
						|| ExtnIsCustUOMExcl.trim().length() == 0 || !ExtnIsCustUOMExcl
						.equals("Y")))
		{
			YFCElement uOMElement = UOMListElement.createChild("UOM");
			uOMElement.setAttribute("UnitOfMeasure", baseUOM);
			uOMElement.setAttribute("Conversion", "1");
		}
		/*********************************************************************************************/
		
		if (lowestConvUOM != null && lowestConvUOM.length() > 0) {
			YFCElement uOMElement = UOMListElement.createChild("UOM");
			uOMElement.setAttribute("UnitOfMeasure", lowestConvUOM);
			uOMElement.setAttribute("Conversion", wUOMsToConversionFactors
					.get(lowestConvUOM));
			
		}
		Set<String> set = wUOMsToConversionFactors.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			YFCElement uOMElement = UOMListElement.createChild("UOM");
			String UnitOfMeasure = (String) iterator.next();
			String Conversion = wUOMsToConversionFactors.get(UnitOfMeasure);
			if (!UnitOfMeasure.equals(lowestConvUOM)) {
				uOMElement.setAttribute("UnitOfMeasure", UnitOfMeasure);
				//Updated for XB-687 - Start
				if(customerUOMList.contains(UnitOfMeasure))
				{
					uOMElement.setAttribute("IsCustUOMFlag", "Y");
				}//Updated for XB-687 - End
				uOMElement.setAttribute("Conversion", Conversion);
			}
		}
		itemElement.setAttribute("ItemID", itemID);
		documentElement.appendChild((YFCNode)itemElement);
	}
	
	private int getConversion(String convFactor, String orderMultiple) {
		if (convFactor != null && convFactor.length() > 0
				&& orderMultiple != null && orderMultiple.length() > 0) {
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
		this.customerUOMList = customerUOMList;
	}

}
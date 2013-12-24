package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.TooManyClauses;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.struts2.util.ServletContextAware;

import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.woodstock.util.frame.Manager;
import com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete.AutocompleteMarketingGroup;
import com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete.AutocompleteMarketingGroupComparator;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSException;

/*
 * Created on Oct 21, 2013
 */

/**
 * Searches the Marketing Group Index (MGI) to build search results. Entitlements are applied as appropriate based on the current user (anonymous, ship-to, etc).
 * <br><br>
 * This action produces a JSON result for the autocomplete ajax call.
 * <br><br>
 * To see how the MGI is created, see the tools_utils repo, directory MarketingGroupIndexTool.
 *
 * @author Trey Howard
 */
public class AjaxAutocompleteAction extends WCAction implements ServletContextAware {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(AjaxAutocompleteAction.class);

	public static enum ResultStatus {
		OK, TOO_MANY_RESULTS;
	}

	private static Searcher sharedSearcher; // initialized on servlet context startup

	private String searchTerm;

	private List<AutocompleteMarketingGroup> autocompleteMarketingGroups = new ArrayList<AutocompleteMarketingGroup>(0);
	private ResultStatus resultStatus = ResultStatus.OK;

	@Override
	public void setServletContext(ServletContext servletContext) {
		initSharedSearcher();
	}

	/**
	 * Initializes the static variable sharedSearcher. The old sharedSearcher is closed (if applicable).
	 */
	public static void initSharedSearcher() {
		log.debug("Initializing the static sharedSearcher");

		String mgiRoot = YFSSystem.getProperty("marketingGroupIndex.rootDirectory");
		if (mgiRoot == null) {
			log.error("Failed to initialize sharedSearcher - Missing YFS setting in customer_overrides.properties: yfs.marketingGroupIndex.rootDirectory");
			return;
		}

		// TODO fetch active archive record from database
		String indexPath = getActiveIndexPath();
		if (indexPath == null) {
			log.error("Failed to initialize sharedSearcher - No active xpx_mgi_archive record available");
			return;
		}

		File mgiPath = new File(mgiRoot, indexPath);

		if (!mgiPath.canRead()) {
			log.error("Failed to initialize sharedSearcher - Missing directory: " + mgiPath);
			return;
		}

		try {
			if (log.isDebugEnabled()) {
				log.debug("Creating new sharedSearcher: " + mgiPath.getAbsolutePath());
			}

			Searcher oldSearcher = sharedSearcher; // so we can close after creating new one

			sharedSearcher = new IndexSearcher(mgiPath.getAbsolutePath());

			if (oldSearcher != null) {
				log.debug("Closing old sharedSearcher");
				try {
					oldSearcher.close();
				} catch (Exception ignore) {
				}
			}

		} catch (Exception e) {
			log.error("Failed to initialize sharedSearcher - Failed to create Lucene searcher: " + e.getMessage());
			log.debug("", e);
			return;
		}
	}

	/**
	 * @return Returns the index_path for the active xpx_mgi_archive record. Returns null if none found.
	 * @throws SQLException
	 * @throws YIFClientCreationException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws RemoteException
	 * @throws YFSException
	 */
	private static String getActiveIndexPath() {
		// TODO invoke API to get active indexPath - hard code direct db connection for now
		//			YFSEnvironment env = (YFSEnvironment) getWCContext().getSCUIContext().getTransactionContext(true)
		//					.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
		//
		//			try {
		//				YIFApi api = YIFClientFactory.getInstance().getApi();
		//				String inputXml = "<MarketingGroupIndexDetails activeFlag=\"Y\" />";
		//				org.w3c.dom.Document outputListDoc = api.invoke(env, "getMarketingGroupIndexDetails",
		//						getXMLUtils().createFromString(inputXml));
		//
		//			} catch (Exception e) {
		//				throw new RuntimeException(e);
		//			}

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		try {
			String driver = Manager.getProperty("jdbcService", "oraclePool.driver");
			String url = Manager.getProperty("jdbcService", "oraclePool.url");
			String user = Manager.getProperty("jdbcService", "oraclePool.user");
			String password = Manager.getProperty("jdbcService", "oraclePool.password");
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.prepareStatement("select index_path from xpx_mgi_archive where active_flag = 'Y'");
			res = stmt.executeQuery();
			if (res.next()) {
				return res.getString("index_path");
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error("Unexpected error performing query: " + e.getMessage());
			log.debug("", e);
			return null;
		} finally {
			if (res != null) {
				try {
					res.close();
				} catch (Exception ignore) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ignore) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	/**
	 * This AJAX call is used as part of the jquery autocomplete plugin.
	 *
	 * @return
	 * @throws IOException
	 * @throws CorruptIndexException
	 * @see http://api.jqueryui.com/1.8/autocomplete/#option-source
	 */
	@Override
	public String execute() throws CorruptIndexException, IOException {
		if (sharedSearcher == null) {
			log.error("No Lucene Searcher available. Probably a configuration error, see earlier logs for details.");
			return ERROR;
		}

		try {
			autocompleteMarketingGroups = searchIndex(sharedSearcher);
			return SUCCESS;

		} catch (Exception e) {
			log.error("Failed to query Marketing Group Index: " + e.getMessage());
			log.debug("", e);
			return ERROR;
		}
	}

	/**
	 * Perform a lucene search using the given Searcher.
	 *
	 * @param searcher The Lucene Searcher used to perform the search.
	 * @return Returns a list containing an AutocompleteMarketingGroup for each Lucene document found.
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	/*default*/ List<AutocompleteMarketingGroup> searchIndex(Searcher searcher) throws CorruptIndexException, IOException {
		if (searcher == null) {
			throw new IllegalArgumentException("searcher must not be null");
		}
		if (searchTerm == null) {
			throw new IllegalArgumentException("searchTerm must not be null");
		}

		long start = 0;
		long stop = 0;

		if (log.isDebugEnabled()) {
			start = System.currentTimeMillis();
		}

		List<AutocompleteMarketingGroup> marketingGroups = new ArrayList<AutocompleteMarketingGroup>(0);
		try {
			Query query = createQuery();

			TopDocs topDocs = searcher.search(query, 20);

			marketingGroups = new ArrayList<AutocompleteMarketingGroup>(topDocs.scoreDocs.length);

			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = searcher.doc(scoreDoc.doc);
				String key = doc.getField("marketing_group_id").stringValue();
				String name = doc.getField("marketing_group_name").stringValue();
				String path = doc.getField("marketing_group_path").stringValue();
				String cat1 = doc.getField("cat1").stringValue();

				AutocompleteMarketingGroup item = new AutocompleteMarketingGroup();
				item.setKey(key);
				item.setCat1(cat1);
				item.setName(name);
				item.setPath(path);
				marketingGroups.add(item);
			}

			// do NOT use lucene sorting: we want top hits independent of group. we only want to resort for the presentation layer (UI looks funky if they're not grouped together)
			Collections.sort(marketingGroups, new AutocompleteMarketingGroupComparator(getBrand()) {
			});

		} catch (TooManyClauses e) {
			// this happens if we have too many results
			resultStatus = ResultStatus.TOO_MANY_RESULTS;
			// TODO add logic to retry with different wildcards
		}

		if (log.isDebugEnabled()) {
			stop = System.currentTimeMillis();
			log.debug(String.format("Autocomplete search for '%s' returned %s results in %s milliseconds", searchTerm, marketingGroups.size(), stop - start));
			if (ResultStatus.OK != resultStatus) {
				log.debug("Autocomplete failed: resultStatus = " + resultStatus);
			}
		}

		return marketingGroups;
	}

	/**
	 * @return Returns a lucene Query object for searchTerm
	 */
	private Query createQuery() {
		BooleanQuery nestedEntitlementQuery = new BooleanQuery();

		String anonymousBrand = getEntitlementAnonymousBrand();
		if (anonymousBrand != null) {
			Term btTerm = new Term("entitled_anonymous", anonymousBrand.toLowerCase());
			TermQuery btQuery = new TermQuery(btTerm);
			nestedEntitlementQuery.add(new BooleanClause(btQuery, Occur.SHOULD));
		}

		String shipToDivision = getEntitlementDivisionAndBrand();
		if (shipToDivision != null) {
			Term btTerm = new Term("entitled_divisions", shipToDivision.toLowerCase());
			TermQuery btQuery = new TermQuery(btTerm);
			nestedEntitlementQuery.add(new BooleanClause(btQuery, Occur.SHOULD));
		}

		String companyCodeAndLegacyCustId = getEntitlementCompanyCodeAndLegacyCustomerId();
		if (companyCodeAndLegacyCustId != null) {
			Term btTerm = new Term("entitled_customers", companyCodeAndLegacyCustId.toLowerCase());
			TermQuery btQuery = new TermQuery(btTerm);
			nestedEntitlementQuery.add(new BooleanClause(btQuery, Occur.SHOULD));
		}

		BooleanQuery nestedSearchTermQuery = new BooleanQuery();
		String[] tokens = searchTerm.split("\\s+");
		for (String token : tokens) {
			Term tName = new Term("marketing_group_path_parsed", "*" + token.toLowerCase() + "*");
			nestedSearchTermQuery.add(new BooleanClause(new WildcardQuery(tName), Occur.SHOULD));
		}

		// use nested boolean queries to get query: (anon OR div OR cust) AND (searchTerm[0] or searchTerm[1] ...)
		BooleanQuery query = new BooleanQuery();
		query.add(new BooleanClause(nestedEntitlementQuery, Occur.MUST));
		query.add(new BooleanClause(nestedSearchTermQuery, Occur.MUST));

		log.debug("Lucene query: " + query);

		return query;
	}

	/**
	 * @return Returns the storefront id (aka brand).
	 */
	/*default*/ String getBrand() {
		return wcContext.getStorefrontId();
	}

	/**
	 * @return If anonymous user, returns the storefront id (aka brand). Otherwise returns null.
	 */
	/*default*/ String getEntitlementAnonymousBrand() {
		boolean anonymous = XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER) == null;
		return anonymous ? getBrand() : null;
	}

	/**
	 * @return If logged in user and the ship-to is configured to use division entitlements, returns the ship-to's division + brand. Otherwise returns null.
	 */
	/*default*/ String getEntitlementDivisionAndBrand() {
		XPEDXShipToCustomer shipto = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		if (shipto == null) {
			return null;
		}

		if ("N".equals(shipto.getCustomerLevel())) {
			// if customer level entitlement is disabled, then prevent from seeing division entitlements
			return null;
		}
		// developer note: relationship_type is a division (by convention, always the same as extn_ship_from_branch)
		return shipto.getRelationshipType() + wcContext.getStorefrontId();
	}

	/**
	 * @return If logged in user, then returns the ship-to's company code + legacy customer number
	 */
	/*default*/ String getEntitlementCompanyCodeAndLegacyCustomerId() {
		XPEDXShipToCustomer shipto = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		if (shipto == null) {
			return null;
		}

		String companyCode = shipto.getExtnCustomerDivision();
		String legacyCustNum = shipto.getExtnLegacyCustNumber();
		return companyCode + legacyCustNum;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public ResultStatus getResultStatus() {
		return resultStatus;
	}

	public List<AutocompleteMarketingGroup> getAutocompleteMarketingGroups() {
		return autocompleteMarketingGroups;
	}

}

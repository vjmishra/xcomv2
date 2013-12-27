package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete.AutocompleteMarketingGroup;
import com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete.AutocompleteMarketingGroupComparator;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
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
public class AjaxAutocompleteAction extends WCAction {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(AjaxAutocompleteAction.class);

	public static enum ResultStatus {
		OK, TOO_MANY_RESULTS, CONFIG_ERROR, SEARCH_ERROR;
	}

	private static Searcher sharedSearcher; // initialized on servlet context startup
	private static Object MUTEX = new Object();

	private String searchTerm;

	private List<AutocompleteMarketingGroup> autocompleteMarketingGroups = new ArrayList<AutocompleteMarketingGroup>(0);
	private ResultStatus resultStatus = ResultStatus.OK;

	/**
	 * Initializes the static variable sharedSearcher. The old sharedSearcher is closed (if applicable).
	 * @throws IllegalStateException If missing config. If missing active XPXMgiArchive. If missing the Lucene directory.
	 * @throws YIFClientCreationException API error
	 * @throws YFSException API error
	 * @throws IOException Lucene error
	 * @throws CorruptIndexException Lucene error
	 */
	public void initSharedSearcher() throws YFSException, YIFClientCreationException, CorruptIndexException, IOException {
		log.debug("Initializing the static sharedSearcher");

		String mgiRoot = YFSSystem.getProperty("marketingGroupIndex.rootDirectory");
		if (mgiRoot == null) {
			throw new IllegalStateException("Missing YFS setting in customer_overrides.properties: yfs.marketingGroupIndex.rootDirectory");
		}

		String indexPath = getActiveIndexPath();
		if (indexPath == null) {
			throw new IllegalStateException("No active XPXMgiArchive");
		}

		File mgiPath = new File(mgiRoot, indexPath);

		if (!mgiPath.canRead()) {
			throw new IllegalStateException("Missing directory: " + mgiPath);
		}

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
	}

	/**
	 * @return Returns the IndexPath for the active XPXMgiArchive. Returns null if none found.
	 * @throws SQLException
	 * @throws YIFClientCreationException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws RemoteException
	 * @throws YFSException
	 */
	protected String getActiveIndexPath() throws YIFClientCreationException, YFSException, RemoteException {
		YIFApi api = YIFClientFactory.getInstance().getApi();

		YFSEnvironment env  = (YFSEnvironment) getWCContext().getSCUIContext().getTransactionContext(true).getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);

		org.w3c.dom.Document docInput = YFCDocument.createDocument().getDocument();
		Element xpxMgiArchiveListElem = docInput.createElement("XPXMgiArchiveList"); // TODO per testing in api, any tag works here?
		xpxMgiArchiveListElem.setAttribute("ActiveFlag", "Y");
		docInput.appendChild(xpxMgiArchiveListElem);

		org.w3c.dom.Document docOutput = api.executeFlow(env, "getXPXMgiArchiveList", docInput);
		if (docOutput == null) {
			log.warn("API returned null");
			return null;
		}
		NodeList activeMgiArchiveElems = docOutput.getElementsByTagName("XPXMgiArchive");
		if (activeMgiArchiveElems == null || activeMgiArchiveElems.getLength() == 0) {
			log.warn("API returned empty list");
			return null;
		}

		return activeMgiArchiveElems.item(0).getAttributes().getNamedItem("IndexPath").getNodeValue();
	}

	/**
	 * Lazy-loader for sharedSearcher.
	 * @return
	 * @throws IllegalStateException If fails to initialize (wraps exception)
	 */
	protected Searcher getSharedSearcher() {
		synchronized (MUTEX) {
			if (sharedSearcher == null) {
				try {
					initSharedSearcher();
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
			}
		}
		return sharedSearcher;
	}

	/**
	 * This AJAX call is used as part of the jquery autocomplete plugin.
	 *
	 * @return
	 * @throws Exception
	 * @see http://api.jqueryui.com/1.8/autocomplete/#option-source
	 */
	@Override
	public String execute() {
		Searcher searcher;
		try {
			searcher = getSharedSearcher();
		} catch (Exception e) {
			log.error("Failed to initialize sharedSearcher", e);
			resultStatus = ResultStatus.CONFIG_ERROR;
			return ERROR;
		}

		try {
			autocompleteMarketingGroups = searchIndex(searcher);
			return SUCCESS;

		} catch (Exception e) {
			log.error("Failed to query Marketing Group Index", e);
			resultStatus = ResultStatus.SEARCH_ERROR;
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

		if (log.isDebugEnabled()) {
			log.debug("Lucene query: " + query);
		}

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

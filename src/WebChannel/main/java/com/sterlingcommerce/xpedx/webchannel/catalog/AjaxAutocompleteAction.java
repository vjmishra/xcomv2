package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.io.File;
import java.io.IOException;
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

import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete.AutocompleteMarketingGroup;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfs.core.YFSSystem;

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
		OK, TOO_MANY_RESULTS;
	}

	private String searchTerm;

	private List<AutocompleteMarketingGroup> autocompleteMarketingGroups = new ArrayList<AutocompleteMarketingGroup>(0);
	private ResultStatus resultStatus = ResultStatus.OK;

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
		String mgiRoot = YFSSystem.getProperty("marketingGroupIndex.rootDirectory");
		if (mgiRoot == null) {
			log.error("Missing YFS setting in customer_overrides.properties: yfs.marketingGroupIndex.rootDirectory");
			return ERROR;
		}

		if (!new File(mgiRoot).canRead()) {
			log.error("Missing Lucene index: " + mgiRoot);
			return ERROR;
		}

		try {
			autocompleteMarketingGroups = searchIndex(mgiRoot);
			return SUCCESS;

		} catch (Exception e) {
			log.error("Failed to query Marketing Group Index: " + e.getMessage());
			log.debug("", e);
			return ERROR;
		}
	}

	/**
	 * Perform a lucene search against the Marketing Group index. Populates the <code>autocompleteMarketingGroups</code> field which is seralized as a JSON response.
	 *
	 * @param mgiRoot
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	private List<AutocompleteMarketingGroup> searchIndex(String mgiRoot) throws CorruptIndexException, IOException {
		if (searchTerm == null) {
			throw new IllegalArgumentException("searchTerm must not be null");
		}

		Searcher indexSearcher = new IndexSearcher(mgiRoot);

		long start = 0;
		long stop = 0;

		if (log.isDebugEnabled()) {
			start = System.currentTimeMillis();
		}

		List<AutocompleteMarketingGroup> marketingGroups = new ArrayList<AutocompleteMarketingGroup>(0);
		try {
			Query query = createQuery();

			TopDocs topDocs = indexSearcher.search(query, 20);

			marketingGroups = new ArrayList<AutocompleteMarketingGroup>(topDocs.scoreDocs.length);

			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
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
			Collections.sort(marketingGroups);

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
		BooleanQuery query = new BooleanQuery();

		String anonymousBrand = getEntitlementAnonymousBrand();
		if (anonymousBrand != null) {
			Term btTerm = new Term("entitled_anonymous", anonymousBrand.toLowerCase());
			TermQuery btQuery = new TermQuery(btTerm);
			query.add(new BooleanClause(btQuery, Occur.MUST));
		}

//		String shipToDivision = getEntitlementDivisionAndBrand();
//		if (shipToDivision != null) {
//			Term btTerm = new Term("entitled_divisions", shipToDivision.toLowerCase());
//			TermQuery btQuery = new TermQuery(btTerm);
//			query.add(new BooleanClause(btQuery, Occur.MUST));
//		}

		String companyCodeAndLegacyCustId = getEntitlementCompanyCodeAndLegacyCustomerId();
		if (companyCodeAndLegacyCustId != null) {
			Term btTerm = new Term("entitled_customers", companyCodeAndLegacyCustId.toLowerCase());
			TermQuery btQuery = new TermQuery(btTerm);
			query.add(new BooleanClause(btQuery, Occur.MUST));
		}

		String[] tokens = searchTerm.split("\\s+");
		for (String token : tokens) {
			Term tName = new Term("marketing_group_path_parsed", "*" + token.toLowerCase() + "*");
			query.add(new BooleanClause(new WildcardQuery(tName), Occur.SHOULD));
		}

		log.debug("Lucene query: " + query);

		return query;
	}

	/**
	 * @return If anonymous user, returns the storefront id (aka brand). Otherwise returns null.
	 */
	String getEntitlementAnonymousBrand() {
		boolean anonymous = XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER) == null;
		return anonymous ? wcContext.getStorefrontId() : null;
	}

//	String getEntitlementDivisionAndBrand() {
//		XPEDXShipToCustomer shipto = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
//		if (shipto == null) {
//			return null;
//		}
//
//		// TODO need to add customer_level db column
////		if ("N".equals(shipto.getCustomerLevel()) { return null; }
//		return shipto.getExtnCustomerDivision() + wcContext.getStorefrontId();
//	}

	String getEntitlementCompanyCodeAndLegacyCustomerId() {
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

	@SuppressWarnings("all")
	public static void main(String[] args) throws Exception {
		AjaxAutocompleteAction action = new AjaxAutocompleteAction() {
			@Override
			String getEntitlementAnonymousBrand() {
				return null;
			}
//			@Override
//			String getEntitlementDivisionAndBrand() {
//				return "60xpedx";
//			}
			@Override
			String getEntitlementCompanyCodeAndLegacyCustomerId() {
				return "800008310316";
			}
		};

		action.setSearchTerm("spring");
		List<AutocompleteMarketingGroup> mgs = action.searchIndex("C:/Sterling/Foundation/marketinggroupindex");

		for (AutocompleteMarketingGroup mg : mgs) {
			System.out.println("mg:\t" + mg);
		}
	}

}

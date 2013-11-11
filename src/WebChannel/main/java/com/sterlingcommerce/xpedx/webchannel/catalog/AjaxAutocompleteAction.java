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
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;

import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete.AutocompleteMarketingGroup;
import com.yantra.yfs.core.YFSSystem;

/*
 * Created on Oct 21, 2013
 */

/**
 * This action produces a JSON result for the autocomplete ajax call.
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
		String searchIndexRoot = YFSSystem.getProperty("marketingGroupIndex.rootDirectory");
		if (searchIndexRoot == null) {
			log.error("Missing YFS setting in customer_overrides.properties: yfs.marketingGroupIndex.rootDirectory");
			return ERROR;
		}

		if (!new File(searchIndexRoot).canRead()) {
			log.error("Missing Lucene index: " + searchIndexRoot);
			return ERROR;
		}

		try {
			autocompleteMarketingGroups = searchIndex(searchIndexRoot);
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
	 * @param searchIndexRoot
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	private List<AutocompleteMarketingGroup> searchIndex(String searchIndexRoot) throws CorruptIndexException, IOException {
		if (searchTerm == null) {
			throw new IllegalArgumentException("searchTerm must not be null");
		}

		Searcher indexSearcher = new IndexSearcher(searchIndexRoot);

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

		// String safeTerm = searchTerm.replaceAll("\\D+", "");
		String[] tokens = searchTerm.split("\\s+");
		for (String token : tokens) {
			Term tName = new Term("marketing_group_path_parsed", "*" + token.toLowerCase() + "*");
			query.add(new BooleanClause(new WildcardQuery(tName), Occur.SHOULD));
		}

		return query;
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

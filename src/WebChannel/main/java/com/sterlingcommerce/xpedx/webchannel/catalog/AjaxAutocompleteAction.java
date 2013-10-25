package com.sterlingcommerce.xpedx.webchannel.catalog;

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
import com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete.AutocompletePun;

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

	private List<AutocompletePun> autocompletePuns;
	private ResultStatus resultStatus = ResultStatus.OK;

	/**
	 * This AJAX call is used as part of the jquery autocomplete plugin.
	 * 
	 * @return
	 * @throws IOException
	 * @throws CorruptIndexException
	 * @see http://api.jqueryui.com/1.8/autocomplete/#option-source
	 */
	public String execute() throws CorruptIndexException, IOException {
		// String searchIndexRoot = YFSSystem.getProperty("yfs.searchIndex.rootDirectory");
		String searchIndexRoot = "C:/search/index/autocomplete-analyzed";

		searchIndex(searchIndexRoot);

		return SUCCESS;
	}

	/**
	 * Perform a lucene search against the PUN index. Populates the <code>autocompletePuns</code> field which is seralized as a JSON response.
	 * 
	 * @param searchIndexRoot
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	private void searchIndex(String searchIndexRoot) throws CorruptIndexException, IOException {
		if (searchTerm == null) {
			throw new IllegalArgumentException("searchTerm must not be null");
		}

		Searcher indexSearcher = new IndexSearcher(searchIndexRoot);

		long start = 0;
		long stop = 0;

		if (log.isDebugEnabled()) {
			start = System.currentTimeMillis();
		}

		try {
			Query query = createQuery();

			TopDocs topDocs = indexSearcher.search(query, 20);

			autocompletePuns = new ArrayList<AutocompletePun>(topDocs.scoreDocs.length);

			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				String key = doc.getField("pun_key").stringValue();
				String name = doc.getField("pun_name").stringValue();
				String path = doc.getField("pun_path").stringValue();
				String group = doc.getField("cat1").stringValue();

				AutocompletePun item = new AutocompletePun();
				item.setKey(key);
				item.setGroup(group);
				item.setName(name);
				item.setPath(path);
				autocompletePuns.add(item);
			}

			// do NOT use lucene sorting: we want top hits independent of group. we only want to resort for the presentation layer (UI looks funky if they're not grouped together)
			Collections.sort(autocompletePuns);

		} catch (TooManyClauses e) {
			// this happens if we have too many results
			resultStatus = ResultStatus.TOO_MANY_RESULTS;
			// TODO add logic to retry with different wildcards
		}

		if (log.isDebugEnabled()) {
			stop = System.currentTimeMillis();
			log.debug(String.format("Autocomplete search for '%s' completed in %s milliseconds", searchTerm, stop - start));
			if (ResultStatus.OK != resultStatus) {
				log.debug("Autocomplete failed: resultStatus = " + resultStatus);
			}
		}
	}

	/**
	 * @return Returns a lucene Query object for searchTerm
	 */
	private Query createQuery() {
		BooleanQuery query = new BooleanQuery();

		// String safeTerm = searchTerm.replaceAll("\\D+", "");
		String[] tokens = searchTerm.split("\\s+");
		for (String token : tokens) {
			Term tName = new Term("pun_path_parsed", "*" + token.toLowerCase() + "*");
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

	public List<AutocompletePun> getAutocompletePuns() {
		return autocompletePuns;
	}

	public static void main(String[] args) throws CorruptIndexException, IOException {
		long start = System.currentTimeMillis();
		AjaxAutocompleteAction action = new AjaxAutocompleteAction();
		action.setSearchTerm("strathmore square");
		action.execute();
		long stop = System.currentTimeMillis();

		System.out.println(String.format("Search completed in {} milliseconds: ", stop - start));

		System.out.println("resultStatus = " + action.getResultStatus());
		if (action.getAutocompletePuns() != null) {
			System.out.println("action.getAutocompleteItems().size = " + action.getAutocompletePuns().size());
			for (AutocompletePun item : action.getAutocompletePuns()) {
				System.out.println(item);
			}
		}
	}

}

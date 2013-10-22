package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery.TooManyClauses;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;

import com.sterlingcommerce.webchannel.core.WCAction;

/*
 * Created on Oct 9, 2013
 */

/**
 * TODO some code was copy/pasted from XPEDXCatalogAction - refactor to share
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

	private List<AjaxAutocompleteAction.Item> autocompleteItems;
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
		// orderByAttribute = "Item.ExtnBestMatch";
		// sortField = "Item.ExtnBestMatch--A";
		// pageSize = "10";

		// String searchIndexRoot = YFSSystem.getProperty("yfs.searchIndex.rootDirectory");
		String searchIndexRoot = "C:/search/index/autocomplete";

		// TODO search Item.Keywords with length > 2. can we exclude numeric-only? better yet, skip ajax call on client side for numeric-only

		// createIndex(searchIndexRoot);
		searchIndex(searchIndexRoot);

		return SUCCESS;
	}

	private void searchIndex(String searchIndexRoot) throws CorruptIndexException, IOException {
		if (searchTerm == null) {
			throw new IllegalArgumentException("searchTerm must not be null");
		}

		Searcher indexSearcher = new IndexSearcher(searchIndexRoot);

		// BooleanQuery query = new BooleanQuery();
		//
		// Term tName = new Term("pun_name_lower", "*" + searchTerm.toLowerCase() + "*");
		// query.add(new BooleanClause(new WildcardQuery(tName), Occur.SHOULD));
		//
		// Term tPath = new Term("pun_path_lower", "*" + searchTerm.toLowerCase() + "*");
		// query.add(new BooleanClause(new WildcardQuery(tPath), Occur.SHOULD));
		//
		// Term tCat1 = new Term("cat1_lower", "*" + searchTerm.toLowerCase() + "*");
		// query.add(new BooleanClause(new WildcardQuery(tCat1), Occur.SHOULD));
		//
		// Term tCat2 = new Term("cat2_lower", "*" + searchTerm.toLowerCase() + "*");
		// query.add(new BooleanClause(new WildcardQuery(tCat2), Occur.SHOULD));
		//
		// Term tCat3 = new Term("cat3_lower", "*" + searchTerm.toLowerCase() + "*");
		// query.add(new BooleanClause(new WildcardQuery(tCat3), Occur.SHOULD));
		//
		// Term tCat4 = new Term("cat4_lower", "*" + searchTerm.toLowerCase() + "*");
		// query.add(new BooleanClause(new WildcardQuery(tCat4), Occur.SHOULD));

		Query query = new WildcardQuery(new Term("pun_path_lower", "*" + searchTerm.toLowerCase() + "*"));

		try {
			TopDocs topDocs = indexSearcher.search(query, 20);

			System.out.println("scoreDocs.length = " + topDocs.scoreDocs.length);
			autocompleteItems = new ArrayList<AjaxAutocompleteAction.Item>(topDocs.scoreDocs.length);

			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				String id = doc.getField("pun_id").stringValue();
				String name = doc.getField("pun_name").stringValue();
				String path = doc.getField("pun_path").stringValue();

				String group = path.substring(0, path.indexOf('>') - 1);
				path = path.substring(group.length() + 3);

				AjaxAutocompleteAction.Item item = new AjaxAutocompleteAction.Item();
				item.setId(Integer.valueOf(id));
				item.setGroup(group);
				item.setName(name);
				item.setPath(path);
				autocompleteItems.add(item);
			}

		} catch (TooManyClauses e) {
			// this happens if we have too many results
			resultStatus = ResultStatus.TOO_MANY_RESULTS;
		}
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public ResultStatus getResultStatus() {
		return resultStatus;
	}

	public List<AjaxAutocompleteAction.Item> getAutocompleteItems() {
		return autocompleteItems;
	}

	public static class Item {
		private int id;
		private String group;
		private String name;
		private String path;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getGroup() {
			return group;
		}

		public void setGroup(String group) {
			this.group = group;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		@Override
		public String toString() {
			return "Item [id=" + id + " | group=" + group + " | name=" + name + " | path=" + path + "]";
		}
	}

	public static void main(String[] args) throws CorruptIndexException, IOException {
		AjaxAutocompleteAction action = new AjaxAutocompleteAction();
		action.setSearchTerm("spring");
		action.execute();

		System.out.println("resultStatus = " + action.getResultStatus());
		if (action.getAutocompleteItems() != null) {
			for (AjaxAutocompleteAction.Item item : action.getAutocompleteItems()) {
				System.out.println(item);
			}
		}
	}

}

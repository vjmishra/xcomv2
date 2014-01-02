package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import com.sterlingcommerce.woodstock.util.frame.Manager;
import com.yantra.yfs.core.YFSSystem;

/**
 * A debug tool for retrieving some basic info on the marketing group index. This info is primarily intended to validate the integrity of the marketing group index:
 * <ol>
 *  <li>That there are marketing_group records</li>
 *  <li>The Lucene Marketing Group Index (MGI) exists</li>
 *  <li>All marketing_group.marketing_group_id values exist in the MGI</li>
 *  <li>The Lucene Search Index (SI) exists</li>
 *  <li>All marketing_group.marketing_group_id values exist in the SI</li>
 * </ol>
 *
 * @author Trey Howard
 */
@SuppressWarnings("serial")
public class DebugMarketingGroupIndex extends AjaxAutocompleteAction {

	private static final Logger log = Logger.getLogger(DebugMarketingGroupIndex.class);

	private Map<String, Object> debugInfo = new HashMap<String, Object>();

	@Override
	public String execute() {
		debugInfo.put("error", false); // only set to false if something is determined to be wrong

		Connection conn = null;
		try {
			conn = getConnection();

			MinMax mgIds = getMinMaxMarketingGroupIds(conn);

			String mgiRoot = YFSSystem.getProperty("marketingGroupIndex.rootDirectory");
			String siRoot = YFSSystem.getProperty("searchIndex.rootDirectory");

			String indexPath = CACHE_UTIL.getActiveIndexPath();

			analyze(mgIds, mgiRoot, indexPath, siRoot, CACHE_UTIL.getSearcher(false));

		} catch (Exception e) {
			log.error("Unexpected error: " + e.getMessage());
			log.debug("", e);

			debugInfo.put("error", true);
			debugInfo.put("exception", e);
			debugInfo.put("exception_message", e.getMessage());
			debugInfo.put("exception_stacktrace", e.getStackTrace());

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ignore) {
				}
			}
		}

		return SUCCESS;
	}

	private void analyze(MinMax mgIds, String mgiRoot, String indexPath, String siRoot, Searcher mgiSearcher) throws CorruptIndexException, IOException {
		File mgiPath = null;
		if (indexPath == null) {
			debugInfo.put("error", true);
			debugInfo.put("error.indexPath", "no active XPXMgiArchive");
		} else {
			debugInfo.put("XPXMgiArchive.IndexPath", indexPath);
			mgiPath = new File(mgiRoot, indexPath);
		}

		if (mgiRoot == null) {
			debugInfo.put("error", true);
			debugInfo.put("error.lucene_folder", "Missing YFS setting in customer_overrides.properties: yfs.marketingGroupIndex.rootDirectory");
		}

		if (mgiPath == null || !mgiPath.canRead()) {
			debugInfo.put("error", true);
			debugInfo.put("error.lucene_index_missing", "Missing Lucene index: " + mgiPath);
		}

		debugInfo.put("marketing_group.marketing_group_id", mgIds);

		if (mgIds.getMin() == null || mgIds.getMax() == null) {
			debugInfo.put("error", true);
			debugInfo.put("error.null_mgIds", "No marketing_group records found");
		}

		if (mgiSearcher == null) {
			debugInfo.put("error", true);
			debugInfo.put("error.sharedSearcher", "mgiSearcher is null");
		}

		if (!debugInfo.containsKey("error")) {
			// MGI search
			TopDocs mgiMinSearch = mgiSearcher.search(new TermQuery(new Term("marketing_group_id", String.valueOf(mgIds.getMin()))), 1);
			if (mgiMinSearch.scoreDocs.length == 0) {
				debugInfo.put("error", true);
				debugInfo.put("error.mgi_search_none_min", true);
			}

			TopDocs mgiMaxSearch = mgiSearcher.search(new TermQuery(new Term("marketing_group_id", String.valueOf(mgIds.getMax()))), 1);
			if (mgiMaxSearch.scoreDocs.length == 0) {
				debugInfo.put("error", true);
				debugInfo.put("error.mgi_search_none_max", true);
			}

			// SI search
			Searcher siSearcher = new IndexSearcher(siRoot);

			// do a generic search and grab a Item.ExtnMarketingGroupId value and see if its value is in range
			TopDocs siSearch = siSearcher.search(new RangeQuery(new Term("Item.ExtnMarketingGroupId", String.valueOf(mgIds.getMin())), new Term("Item.ExtnMarketingGroupId", String.valueOf(mgIds.getMin() + 100)), true), 20);

			if (siSearch.scoreDocs.length == 0) {
				debugInfo.put("error", true);
				debugInfo.put("error.si_search_empty", true);
			}
		}
	}

	private MinMax getMinMaxMarketingGroupIds(Connection conn) throws SQLException {
		String sql = ""
				+ " select min(mg.marketing_group_id) as min, max(mg.marketing_group_id) as max"
				+ " from marketing_group mg"
				+ " where exists (select 1 from marketing_group_item mgi where mgi.marketing_group_id = mg.marketing_group_id)";

		PreparedStatement stmt = null;
		ResultSet res = null;
		try {
			stmt = conn.prepareStatement(sql);

			res = stmt.executeQuery();

			MinMax mm = new MinMax();
			if (res.next()) {
				mm.setMin(res.getLong("min"));
				if (res.wasNull()) {
					mm.setMin(null);
				}

				mm.setMax(res.getLong("max"));
				if (res.wasNull()) {
					mm.setMax(null);
				}
			}

			return mm;

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
		}
	}

	public Map<String, Object> getDebugInfo() {
		return debugInfo;
	}

	public static class MinMax {
		private Long min;
		private Long max;

		public Long getMin() {
			return min;
		}

		public void setMin(Long min) {
			this.min = min;
		}

		public Long getMax() {
			return max;
		}

		public void setMax(Long max) {
			this.max = max;
		}

		@Override
		public String toString() {
			return String.format("[%s, %s]", getMin(), getMax());
		}
	}

	private static Connection getConnection() throws ClassNotFoundException, SQLException {
		String jdbcURL = Manager.getProperty("jdbcService", "oraclePool.url");
		String jdbcDriver = Manager.getProperty("jdbcService", "oraclePool.driver");
		String jdbcUser = Manager.getProperty("jdbcService", "oraclePool.user");
		String jdbcPassword = Manager.getProperty("jdbcService", "oraclePool.password");

		Class.forName(jdbcDriver);
		return DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
	}

	public static void main(String[] args) throws Exception {
		DebugMarketingGroupIndex action = new DebugMarketingGroupIndex();

		MinMax mgIds = new MinMax();
		mgIds.setMin(807280L);
		mgIds.setMax(876634L);

		String mgiRoot = "C:/Sterling/Foundation/marketinggroupindex";
		String indexPath = "/" + TestAjaxAutocompleteAction.getNewestMgiFolder(mgiRoot).getName();
		String siRoot = "C:/Sterling/Foundation/searchindex/SearchIndex/xpedx/xpedx/MasterCatalog/CatalogIndex_201311192000213044/en_US";

		Searcher mgiSearcher = new IndexSearcher(mgiRoot + indexPath);

		action.analyze(mgIds, mgiRoot, indexPath, siRoot, mgiSearcher);

		Map<String, Object> map = action.getDebugInfo();
		for (String key : map.keySet()) {
			System.out.println(key + ":\t" + map.get(key));
		}
	}

}

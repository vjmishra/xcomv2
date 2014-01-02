package com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;

import com.sterlingcommerce.woodstock.util.frame.Manager;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSException;

/**
 * Stateless singleton for managing the cached Lucene Searcher used by autocomplete. This does NOT cache the Lucene results.
 *
 * @author Trey Howard
 */
public class AutocompleteCacheUtil {

	private static final Logger log = Logger.getLogger(AutocompleteCacheUtil.class);

	//	private static final long CACHE_EXPIRATION = 60 * 60 * 1000; // 1 hour
	private static final long CACHE_EXPIRATION = 10000; // XXX JUST FOR TESTING

	private Searcher cachedSearcher;
	private long cacheLastUpdated;

	/**
	 * Thread-safe lazy-loader for cachedSearcher.
	 *
	 * @param forceRefresh If true, forces the cache to be refreshed.
	 * @return Returns the cachedSearcher. Guaranteed to not return null.
	 * @throws IllegalStateException If the cachedSearcher needed to be initialized and fails to initialize (this wraps underlying exception)
	 */
	public synchronized Searcher getSearcher(boolean forceRefresh) {
		if (forceRefresh || cachedSearcher == null
				|| (System.currentTimeMillis() - cacheLastUpdated > CACHE_EXPIRATION)) {
			try {
				initCachedSearcher();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return cachedSearcher;
	}

	/**
	 * Initializes the cachedSearcher. The old cachedSearcher is closed (if applicable).
	 *
	 * @throws IllegalStateException If missing config. If missing active XPXMgiArchive. If missing the Lucene directory.
	 * @throws YIFClientCreationException API error
	 * @throws YFSException API error
	 * @throws IOException Lucene error
	 * @throws CorruptIndexException Lucene error
	 */
	private void initCachedSearcher() throws YFSException, YIFClientCreationException, CorruptIndexException, IOException {
		log.debug("Initializing the cachedSearcher");

		String mgiRoot = YFSSystem.getProperty("marketingGroupIndex.rootDirectory");
		if (mgiRoot == null) {
			throw new IllegalStateException("Missing YFS setting in customer_overrides.properties: yfs.marketingGroupIndex.rootDirectory");
		}

		String indexPath = getActiveIndexPath();
		File mgiPath = new File(mgiRoot, indexPath);

		if (!mgiPath.canRead()) {
			throw new IllegalStateException("Missing directory: " + mgiPath);
		}

		if (log.isDebugEnabled()) {
			log.debug("Creating new cachedSearcher: " + mgiPath.getAbsolutePath());
		}

		Searcher oldSearcher = cachedSearcher; // so we can close after creating new one

		cachedSearcher = new IndexSearcher(mgiPath.getAbsolutePath());
		cacheLastUpdated = System.currentTimeMillis();

		if (oldSearcher != null) {
			log.debug("Closing old cachedSearcher");
			try {
				oldSearcher.close();
			} catch (Exception ignore) {
			}
		}
	}

	/**
	 * Queries the database to fetch the active index_path. Guaranteed to not return null.
	 *
	 * @return Returns the IndexPath for the active XPXMgiArchive. Returns null if none found.
	 * @throws IllegalStateException If no active XPXMgiArchive
	 * @throws RuntimeException Database error
	 */
	public String getActiveIndexPath() {
		// we could use the getXPXMgiArchiveList API call, but Sterling caches that API too aggressively for it to be useful
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
				throw new IllegalStateException("No active XPXMgiArchive");
			}

		} catch (Exception e) {
			throw new RuntimeException(e);

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

}

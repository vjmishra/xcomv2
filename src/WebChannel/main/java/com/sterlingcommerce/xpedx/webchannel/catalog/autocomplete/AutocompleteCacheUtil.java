package com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;


/**
 * Stateless singleton for managing the cached Lucene Searcher used by autocomplete. This does NOT cache the Lucene results.
 */
public class AutocompleteCacheUtil {

	private static final Logger log = Logger.getLogger(AutocompleteCacheUtil.class);

	private static final long CACHE_EXPIRATION = 60 * 60 * 1000; // 1 hour

	private Searcher cachedSearcher;
	private long cacheLastUpdated;


	/**
	 * Thread-safe lazy-loader for cachedSearcher.
	 *
	 * @param forceRefresh If true, forces the cache to be refreshed.
	 * @param context
	 * @return Returns the cachedSearcher. Guaranteed to not return null.
	 * @throws IllegalStateException If the cachedSearcher needed to be initialized and fails to initialize (this wraps underlying exception)
	 */
	public synchronized Searcher getSearcher(boolean forceRefresh,IWCContext context) {
		if (forceRefresh || cachedSearcher == null
				|| (System.currentTimeMillis() - cacheLastUpdated > CACHE_EXPIRATION)) {
			try {
				initCachedSearcher(context);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return cachedSearcher;
	}

	/**
	 * Initializes the cachedSearcher. The old cachedSearcher is closed (if applicable).
	 *
	 * @param context
	 * @throws IllegalStateException If missing config. If missing active XPXMgiArchive. If missing the Lucene directory.
	 * @throws YIFClientCreationException API error
	 * @throws YFSException API error
	 * @throws IOException Lucene error
	 * @throws CorruptIndexException Lucene error
	 */
	private void initCachedSearcher(IWCContext context) throws YFSException, YIFClientCreationException, CorruptIndexException, IOException {
		log.debug("Initializing the cachedSearcher");

		String mgiRoot = YFSSystem.getProperty("marketingGroupIndex.rootDirectory");
		if (mgiRoot == null) {
			throw new IllegalStateException("Missing YFS setting in customer_overrides.properties: yfs.marketingGroupIndex.rootDirectory");
		}

		String indexPath = getActiveIndexPath(context);
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
	 * @param context
	 * @return Returns the IndexPath for the active XPXMgiArchive. Returns null if none found.
	 * @throws YIFClientCreationException API error.
	 * @throws IllegalStateException If no active XPXMgiArchive
	 */
	public String getActiveIndexPath(IWCContext context) throws YIFClientCreationException {
		SCUIContext wSCUIContext = context.getSCUIContext();
		ISCUITransactionContext scuiTransactionContext = null;

		// EB-6981 - 07/23/2014 - ML: fixing connection leak causing JDBC connection pool to reach its limit.
		// Adding a call to releaseTransactionContext to trigger the connection close.
		try {
			scuiTransactionContext = wSCUIContext.getTransactionContext(true);
			YIFApi api = YIFClientFactory.getInstance().getLocalApi();
			YFSEnvironment env = (YFSEnvironment) scuiTransactionContext.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);

			Document inXML = SCXmlUtil.createFromString("<XPXMgiArchive ActiveFlag='Y'></XPXMgiArchive>");
			Document xpxMgiListOutDoc = api.executeFlow(env, "getXPXMgiArchiveList", inXML);
			Element outputListElement = xpxMgiListOutDoc.getDocumentElement();

			if (outputListElement != null) {
				NodeList xpxArchiveExtnNL = outputListElement.getElementsByTagName("XPXMgiArchive");
				if (xpxArchiveExtnNL.getLength() > 0) {
					Element xpxmgiExtnEle = (Element) xpxArchiveExtnNL.item(0);
					if (xpxmgiExtnEle != null) {
						return xpxmgiExtnEle.getAttribute("IndexPath");
					}
				}
			}

			scuiTransactionContext.commit(); // read-only, but just to be thorough

			return null;

		} catch (Exception e) {
			// rollback the tran
			if (scuiTransactionContext != null) {
				try {
					scuiTransactionContext.rollback();
				} catch (Exception ignore) {
				}
			}
			throw new IllegalStateException("No Active Index path Found", e);

		} finally {
			if (scuiTransactionContext != null && wSCUIContext != null) {
				try {
					// release the transaction to close the connection.
					SCUITransactionContextHelper.releaseTransactionContext(scuiTransactionContext, wSCUIContext);
					scuiTransactionContext = null;
				} catch (Exception ignore) {
				}
			}
		}
	}

}
package com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.yantra.custom.api.CustomApiImpl;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;


/**
 * Stateless singleton for managing the cached Lucene Searcher used by autocomplete. This does NOT cache the Lucene results.
 *
 * @author Trey Howard
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
		try
		{
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
		}catch(Exception ex){}
	}

	/**
	 * Queries the database to fetch the active index_path. Guaranteed to not return null.
	 *
	 * @param context
	 * @return Returns the IndexPath for the active XPXMgiArchive. Returns null if none found.
	 * @throws IllegalStateException If no active XPXMgiArchive
	 * @throws RuntimeException Database error
	 */
	public String getActiveIndexPath(IWCContext context) throws Exception {
		context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		ISCUITransactionContext scuiTransactionContext = wSCUIContext.getTransactionContext(true);

		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);

		YIFApi api =  YIFClientFactory.getInstance().getApi();
		Document inXML = SCXmlUtil.createDocument("<XPXMgiArchive ACTIVE_FLAG='Y'/></XPXMgiArchive>");
		Document xpxMgiListOutDoc = api.executeFlow(env, "getXPXMgiArchiveList", inXML);

		Element outputListElement = xpxMgiListOutDoc.getDocumentElement();
		if(outputListElement!=null){
			NodeList xpxArchiveExtnNL = outputListElement.getElementsByTagName("XPXMgiArchive");

			if(xpxArchiveExtnNL.getLength() > 0 ){
				Element xpxmgiExtnEle = (Element)xpxArchiveExtnNL.item(0);
				if(xpxmgiExtnEle!=null) {
					return xpxmgiExtnEle.getAttribute("IndexPath");
				}
			}
		}

		throw new IllegalStateException("No Active Index path Found");
	}

}
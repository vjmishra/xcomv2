package com.sterlingcommerce.xpedx.webchannel.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.common.megamenu.MegaMenuItem;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

/**
 * A debug tool for retrieving some basic info on the current mega menu cache.
 *
 * @author Trey Howard
 */
@SuppressWarnings("serial")
public class DebugMegaMenuAction extends WCAction {

	private static final Logger log = Logger.getLogger(DebugMegaMenuAction.class);

	// input fields
	private boolean refresh;

	// output fields
	private Map<String, Object> debugInfo = new HashMap<String, Object>();

	@Override
	public String execute() {
		debugInfo.put("error", false); // only set to false if something is determined to be wrong

		try {
			if (wcContext.getCustomerId() == null) {
				debugInfo.put("anonymous", wcContext.getStorefrontId());
			} else {
				debugInfo.put("CustomerID", wcContext.getCustomerId());
			}

			boolean isCached = XPEDXWCUtils.isMegaMenuCached(wcContext);
			debugInfo.put("is_cached", isCached);

			if (refresh) {
				log.info("Purging mega menu cache (parameter refresh=true)");
				XPEDXWCUtils.purgeMegaMenuCache(wcContext);
			}

			if (isCached || refresh) {
				List<MegaMenuItem> megaMenu = XPEDXWCUtils.getMegaMenu(wcContext);
				debugInfo.put("mega_menu", megaMenu);
			}

		} catch (Exception e) {
			log.error("Unexpected error while inspecting mega menu: " + e.getMessage());
			log.debug("", e);

			debugInfo.put("error", true);
			debugInfo.put("exception", e);
			debugInfo.put("exception_message", e.getMessage());
			debugInfo.put("exception_stacktrace", e.getStackTrace());
		}

		return SUCCESS;
	}

	// --- Input and Output fields

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	public Map<String, Object> getDebugInfo() {
		return debugInfo;
	}

}

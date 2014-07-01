package com.sterlingcommerce.xpedx.webchannel.utilities.megamenu;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import au.com.bytecode.opencsv.CSVReader;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.common.Breadcrumb;
import com.sterlingcommerce.webchannel.common.BreadcrumbHelper;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.megamenu.MegaMenuItem;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfs.core.YFSSystem;

/**
 * Helper class to encapsulate the searchCatalogIndex API call and converting it to a list of MegaMenuItem objects.
 * Note that all methods take a context parameter since the mega menu is cached either in the session (authenticated users) or statically (for each anonymous storefront).
 */
public class MegaMenuUtil {

	private static final Logger log = Logger.getLogger(MegaMenuUtil.class);

	public static final String ATTR_MEGA_MENU = "MegaMenuUtil.data"; // session attribute

	// key = storefrontId, value = mega menu
	private static final Cache ANONYMOUS_MEGAMENU;
	static {
		// cache anonymous mega menu for 8 hours (28800 seconds)
		final long timeToLive = 28800;
		CacheManager cacheManager = CacheManager.create();
		cacheManager.addCache(new Cache("anonymousMegaMenu", 10, false, false, timeToLive, timeToLive));
		ANONYMOUS_MEGAMENU = cacheManager.getCache("anonymousMegaMenu");
	}

	// key = storefrontId, value = mutex
	private static final Map<String, Object> ANONYMOUS_MUTEX = new LinkedHashMap<String, Object>();
	static {
		ANONYMOUS_MUTEX.put("xpedx", new Object());
		ANONYMOUS_MUTEX.put("Saalfeld", new Object());
	}

	private static boolean isAnonymous(IWCContext context) {
		return context.getCustomerId() == null;
	}

	private static Object getMutex(IWCContext context) {
		if (isAnonymous(context)) {
			// anonymous mega menu is statically synchronized per storefront
			return ANONYMOUS_MUTEX.get(context.getStorefrontId());
		} else {
			// use session as mutex to avoid multiple identical calls (eg, user loads multiple pages before the API call finishes)
			return context;
		}
	}

	@SuppressWarnings("unchecked")
	private static List<MegaMenuItem> getCachedMegaMenu(IWCContext context) {
		if (isAnonymous(context)) {
			net.sf.ehcache.Element elem = ANONYMOUS_MEGAMENU.get(context.getStorefrontId());
			return (List<MegaMenuItem>) (elem == null ? null : elem.getValue());
		} else {
			return (List<MegaMenuItem>) context.getWCAttribute(ATTR_MEGA_MENU);
		}
	}

	private static void seCachedMegaMenu(IWCContext context, List<MegaMenuItem> megaMenu) {
		if (isAnonymous(context)) {
			ANONYMOUS_MEGAMENU.put(new net.sf.ehcache.Element(context.getStorefrontId(), megaMenu));
		} else {
			context.setWCAttribute(ATTR_MEGA_MENU, megaMenu, WCAttributeScope.LOCAL_SESSION);
		}
	}

	/**
	 * @param context
	 * @return Returns true if the menu is cached.
	 */
	public boolean isDataAvailable(IWCContext context) {
		return getCachedMegaMenu(context) != null;
	}

	/**
	 * Purges the cached menu.
	 * @param context
	 */
	public void purgeData(IWCContext context) {
		if (isAnonymous(context)) {
			ANONYMOUS_MEGAMENU.remove(context.getStorefrontId());
		} else {
			context.removeWCAttribute(ATTR_MEGA_MENU, WCAttributeScope.LOCAL_SESSION);
		}
	}

	/**
	 * Lazy-loads cached mega menu data model.
	 * @param context
	 * @return Returns the mega menu data model that is cached. If not cached,  then performs an API call to fetch it and cache it.
	 * @throws RuntimeException If an API error.
	 */
	public List<MegaMenuItem> getMegaMenu(IWCContext context) {
		synchronized (getMutex(context)) {
			List<MegaMenuItem> megaMenu = getCachedMegaMenu(context);
			if (megaMenu == null) {
				try {
					log.debug("Making API call to get mega menu data model");
					Element searchCatalogIndexInputElem = WCMashupHelper.getMashupInput("xpedxMegaMenuCategories", createMashupValueMap(context), context);
					Document searchCatalogIndexInputDoc = searchCatalogIndexInputElem.getOwnerDocument();

					if (log.isDebugEnabled()) {
						log.debug("searchCatalogIndex input XML:\n"
								+ (searchCatalogIndexInputDoc == null ? null : SCXmlUtil.getString(searchCatalogIndexInputDoc)));
					}

					Element searchCatalogIndexOutputElem = (Element) WCMashupHelper.invokeMashup("xpedxMegaMenuCategories", searchCatalogIndexInputElem, context.getSCUIContext());

					if (log.isDebugEnabled()) {
						log.debug("searchCatalogIndex output XML:\n"
								+ (searchCatalogIndexOutputElem == null ? null : SCXmlUtil.getString(searchCatalogIndexOutputElem)));
					}

					megaMenu = convertCatalogSearch(searchCatalogIndexOutputElem, context);

					seCachedMegaMenu(context, megaMenu);

					if (log.isDebugEnabled()) {
						if (megaMenu == null) {
							log.debug("megaMenu is null");
						} else if (megaMenu.isEmpty()) {
							log.debug("megaMenu is empty");
						} else {
							for (MegaMenuItem cat1 : megaMenu) {
								log.debug("  cat1 = " + cat1);
								for (MegaMenuItem cat2 : cat1.getSubcategories()) {
									log.debug("    cat2 = " + cat2);
									for (MegaMenuItem cat3 : cat2.getSubcategories()) {
										log.debug("      cat3 = " + cat3);
									}
								}
							}
						}
					}

				} catch (Exception e) {
					log.error("", e);
					throw new RuntimeException(e);
				}
			}
			return megaMenu;
		}
	}

	/**
	 * @param context
	 * @return Returns a map in the format:
	 *         key = XPATH expression
	 *         value = value to insert into XML
	 */
	private Map<String, String> createMashupValueMap(IWCContext context) {
		Map<String, String> valueMap = new LinkedHashMap<String, String>();

		valueMap.put("/SearchCatalogIndex@CallingOrganizationCode", context.getStorefrontId());

		valueMap.put("/SearchCatalogIndex/Item/CustomerInformation@CustomerID", context.getCustomerId());

		return valueMap;
	}

	/**
	 * @param catalogSearchElem
	 * @param context
	 * @return Returns a list of MegaMenuItem representing the given CatalogSearch XML. The topmost elements are cat1, etc.
	 */
	private static List<MegaMenuItem> convertCatalogSearch(Element catalogSearchElem, IWCContext context) {
		Set<String> categoriesWithNoBrands = getCategoriesWithNoBrands();

		List<MegaMenuItem> megaMenu = new LinkedList<MegaMenuItem>();

		Element categoryList1 = SCXmlUtil.getChildElement(catalogSearchElem, "CategoryList");
		List<Element> cat1Elems = SCXmlUtil.getChildrenList(categoryList1);
		for (Element cat1Elem : cat1Elems) {
			MegaMenuItem mmCat1 = convertCategory(cat1Elem);
			megaMenu.add(mmCat1);

			Element childCategoryList2 = SCXmlUtil.getChildElement(cat1Elem, "ChildCategoryList");
			List<Element> cat2Elems = SCXmlUtil.getChildrenList(childCategoryList2);
			for (Element cat2Elem : cat2Elems) {
				MegaMenuItem mmCat2 = convertCategory(cat2Elem);
				mmCat2.setBreadcrumb(createBreadcrumbForCategories(mmCat1, null, context));
				mmCat2.setHasBrands(!categoriesWithNoBrands.contains(mmCat2.getId()));
				mmCat1.getSubcategories().add(mmCat2);

				Element childCategoryList3 = SCXmlUtil.getChildElement(cat2Elem, "ChildCategoryList");
				List<Element> cat3Elems = SCXmlUtil.getChildrenList(childCategoryList3);
				for (Element cat3Elem : cat3Elems) {
					MegaMenuItem mmCat3 = convertCategory(cat3Elem);
					mmCat3.setBreadcrumb(createBreadcrumbForCategories(mmCat1, mmCat2, context));
					mmCat2.getSubcategories().add(mmCat3);
				}
			}
		}

		return megaMenu;
	}

	/**
	 * @param categoryElem
	 * @return Returns a MegaMenuItem representing the given Category XML.
	 */
	private static MegaMenuItem convertCategory(Element categoryElem) {
		String id = categoryElem.getAttribute("CategoryID");
		String path = categoryElem.getAttribute("CategoryPath");
		String name = categoryElem.getAttribute("ShortDescription");
		String count = categoryElem.getAttribute("Count");
		return MegaMenuItem.create(id, path, name, Integer.valueOf(count));
	}

	/**
	 * Creates a _bcs_ parameter for the given category(s). Per Sterling convention, the breadcrumb contains a link to each parent category, plus 1 link to the catalog root.
	 *
	 * @param cat1 Must be non-null (all breadcrumbs contain cat1 link)
	 * @param cat2 If non-null, includes a link to this category in the breadcrumb
	 * @param context
	 * @return Returns the value to be used for the <code>_bcs_</code> parameter.
	 */
	private static String createBreadcrumbForCategories(MegaMenuItem cat1, MegaMenuItem cat2, IWCContext context) {
		List<Breadcrumb> bcl = new ArrayList<Breadcrumb>(3);

		Breadcrumb root = new Breadcrumb(null, null, null);
		root.setRoot(true);
		root.setUrl("/swc/catalog/navigate.action?sfId=" + context.getStorefrontId() + "&scFlag=Y");
		root.setGroup("catalog");
		root.setDisplayGroup("search");
		root.setDisplayName(null);
		bcl.add(root);

		Map<String, String> paramsCat1 = new LinkedHashMap<String, String>();
		paramsCat1.put("path", cat1.getPath());
		paramsCat1.put("cname", cat1.getName());
		paramsCat1.put("newOP", "true");
		paramsCat1.put("CategoryC3", "true");

		Breadcrumb bcCat1 = new Breadcrumb("/catalog", "navigate", paramsCat1);
		bcCat1.setUrl("");
		bcCat1.setGroup("catalog");
		bcCat1.setDisplayGroup("search");
		bcCat1.setDisplayName(cat1.getName());
		bcl.add(bcCat1);

		if (cat2 != null) {
			Map<String, String> paramsCat2 = new LinkedHashMap<String, String>();
			paramsCat2.put("path", cat2.getPath());
			paramsCat2.put("cname", cat2.getName());

			Breadcrumb bcCat2 = new Breadcrumb("/catalog", "navigate", paramsCat2);
			bcCat2.setUrl("");
			bcCat2.setGroup("catalog");
			bcCat2.setDisplayGroup("search");
			bcCat2.setDisplayName(cat2.getName());
			bcl.add(bcCat2);
		}

		String bcs = BreadcrumbHelper.serializeBreadcrumb(bcl);
		return bcs;
	}

	/**
	 * @return Returns the category (cat2) ids that have disabled brands button in mega menu.
	 * @see xpedx_webchannel.properties
	 * @see XPEDXConstants#MEGA_MENU_BRANDS_UNAVAILABLE_PROP
	 */
	private static Set<String> getCategoriesWithNoBrands() {
		// lazy-loading to cache the result
		XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS("xpedx_webchannel.properties");

		String brandsUnavailable = YFSSystem.getProperty(XPEDXConstants.MEGA_MENU_BRANDS_UNAVAILABLE_PROP);
		if (brandsUnavailable == null) {
			log.warn("Missing " + XPEDXConstants.MEGA_MENU_BRANDS_UNAVAILABLE_PROP + " from xpedx_webchannel.properties - fallback to brands button on all categories");
			return new HashSet<String>();
		}

		String[] tokens;
		CSVReader reader = null;
		try {
			reader = new CSVReader(new StringReader(brandsUnavailable));
			List<String[]> lines;
			try {
				lines = reader.readAll();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			tokens = lines.isEmpty() ? new String[0] : lines.get(0);

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception ignore) {
				}
			}
		}

		return new HashSet<String>(Arrays.asList(tokens));
	}

}

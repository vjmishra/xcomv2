package com.sterlingcommerce.xpedx.webchannel.utilities.megamenu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.common.Breadcrumb;
import com.sterlingcommerce.webchannel.common.BreadcrumbHelper;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.common.megamenu.MegaMenuItem;

/**
 * Helper class to encapsulate the searchCatalogIndex API call and converting it to a list of MegaMenuItem objects.
 * Note that all methods take a context parameter since the mega menu is cached at the session level.
 *
 * @author Trey Howard
 */
public class MegaMenuUtil {

	private static final Logger log = Logger.getLogger(MegaMenuUtil.class);

	public static final String ATTR_MEGA_MENU = "MegaMenuUtil.data"; // session attribute

	/**
	 * @param context
	 * @return Returns true if the menu is cached in the session.
	 */
	public boolean isDataAvailable(IWCContext context) {
		return context.getWCAttribute(ATTR_MEGA_MENU) != null;
	}

	/**
	 * Removes the mega menu from the session.
	 * @param context
	 */
	public void purgeData(IWCContext context) {
		context.removeWCAttribute(ATTR_MEGA_MENU, WCAttributeScope.LOCAL_SESSION);
	}

	/**
	 * Lazy-loads session cached mega menu data model.
	 * @param context
	 * @return Returns the mega menu data model that is cached in the session. If not currently in the session, then performs an API call to fetch it and caches it in the session.
	 * @throws RuntimeException If an API error.
	 */
	@SuppressWarnings("unchecked")
	public List<MegaMenuItem> getMegaMenu(IWCContext context) {
		// use session as mutex to avoid multiple identical calls (eg, user loads multiple pages before the API call finishes)
		synchronized (context) {
			List<MegaMenuItem> megaMenu = (List<MegaMenuItem>) context.getWCAttribute(ATTR_MEGA_MENU);
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

					context.setWCAttribute(ATTR_MEGA_MENU, megaMenu, WCAttributeScope.LOCAL_SESSION);

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
	/*default*/ static List<MegaMenuItem> convertCatalogSearch(Element catalogSearchElem, IWCContext context) {
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

}
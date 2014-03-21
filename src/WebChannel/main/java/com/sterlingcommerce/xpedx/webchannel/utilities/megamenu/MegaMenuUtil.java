package com.sterlingcommerce.xpedx.webchannel.utilities.megamenu;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.xpedx.webchannel.common.megamenu.MegaMenuItem;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * Helper class to encapsulate the searchCatalogIndex API call and converting it to a list of MegaMenuItem objects.
 * @author Trey Howard
 */
public class MegaMenuUtil {

	private static final Logger log = Logger.getLogger(MegaMenuUtil.class);

	/**
	 * Lazy-loads session cached mega menu data model.
	 * @param context
	 * @return Returns the mega menu data model that is cached in the session. If not currently in the session, then performs an API call to fetch it and caches it in the session.
	 * @throws Exception If an API error.
	 */
	@SuppressWarnings("unchecked")
	public synchronized List<MegaMenuItem> getMegaMenu(IWCContext context) throws Exception {
		List<MegaMenuItem> megaMenu = (List<MegaMenuItem>) context.getWCAttribute("megaMenu");
		if (true || megaMenu == null) {
			log.debug("Making API call to get mega menu data model");
			SCUIContext wSCUIContext = context.getSCUIContext();
			ISCUITransactionContext scuiTransactionContext = wSCUIContext.getTransactionContext(true);
			YFSEnvironment env = (YFSEnvironment) scuiTransactionContext.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
			YIFApi api = YIFClientFactory.getInstance().getApi();

			Document outputTemplate = SCXmlUtil.createFromString(""
					+ "<CatalogSearch CallingOrganizationCode=\"\" CategoryDepth=\"\">"
					+ "	<CategoryList>"
					+ "		<Category CategoryID=\"\" CategoryPath=\"\" Count=\"\" ShortDescription=\"\">"
					+ "			<ChildCategoryList>"
					+ "				<Category CategoryPath=\"\" Count=\"\" ShortDescription=\"\"/>"
					+ "			</ChildCategoryList>"
					+ "		</Category>"
					+ "	</CategoryList>"
					+ "</CatalogSearch>");
			env.setApiTemplate("searchCatalogIndex", outputTemplate);

			Document searchCatalogIndexInputDoc = YFCDocument.createDocument("SearchCatalogIndex").getDocument();
			searchCatalogIndexInputDoc.getDocumentElement().setAttribute("CallingOrganizationCode", context.getStorefrontId());
			searchCatalogIndexInputDoc.getDocumentElement().setAttribute("CategoryDepth", "3");
			searchCatalogIndexInputDoc.getDocumentElement().setAttribute("IgnoreOrdering", "Y");
			searchCatalogIndexInputDoc.getDocumentElement().setAttribute("PageNumber", "1");
			searchCatalogIndexInputDoc.getDocumentElement().setAttribute("PageSize", "1");

			if (log.isDebugEnabled()) {
				log.debug("searchCatalogIndex input XML:\n"
						+ (searchCatalogIndexInputDoc == null ? null : SCXmlUtil.getString(searchCatalogIndexInputDoc)));
			}

			Document searchCatalogIndexOutputDoc = api.invoke(env, "searchCatalogIndex", searchCatalogIndexInputDoc);

			if (log.isDebugEnabled()) {
				log.debug("searchCatalogIndex output XML:\n"
						+ (searchCatalogIndexOutputDoc == null ? null : SCXmlUtil.getString(searchCatalogIndexOutputDoc)));
			}

			env.clearApiTemplate("searchCatalogIndex");

			megaMenu = convertCatalogSearch(searchCatalogIndexOutputDoc.getDocumentElement());

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

			context.setWCAttribute("megaMenu", megaMenu, WCAttributeScope.LOCAL_SESSION);
		}
		return megaMenu;
	}

	/**
	 * @param catalogSearchElem
	 * @return Returns a list of MegaMenuItem representing the given CatalogSearch XML.
	 */
	private static List<MegaMenuItem> convertCatalogSearch(Element catalogSearchElem) {
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
				mmCat1.getSubcategories().add(mmCat2);

				Element childCategoryList3 = SCXmlUtil.getChildElement(cat2Elem, "ChildCategoryList");
				List<Element> cat3Elems = SCXmlUtil.getChildrenList(childCategoryList3);
				for (Element cat3Elem : cat3Elems) {
					MegaMenuItem mmCat3 = convertCategory(cat3Elem);
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

}
